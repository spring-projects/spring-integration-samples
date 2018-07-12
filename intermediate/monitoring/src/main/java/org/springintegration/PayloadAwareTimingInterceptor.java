/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springintegration;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.util.StopWatch;

/**
 * A sample channel interceptor that illustrates a technique to capture elapsed times
 * based on message payload types.
 *
 * @author Gary Russell
 * @since 2.2
 *
 */
@ManagedResource
public class PayloadAwareTimingInterceptor implements ChannelInterceptor {

	private final ThreadLocal<StopWatchHolder> stopWatchHolder = new ThreadLocal<PayloadAwareTimingInterceptor.StopWatchHolder>();

	private final Map<Class<?>, Stats> statsMap = new ConcurrentHashMap<Class<?>, PayloadAwareTimingInterceptor.Stats>();

	/**
	 *
	 * @param classes An array of types for which statistics will be captured; if
	 * not supplied {@link Object} will be added as a catch-all.
	 */
	public PayloadAwareTimingInterceptor(Class<?>[] classes) {
		for (Class<?> clazz : classes) {
			this.statsMap.put(clazz, new Stats());
		}
		if (!this.statsMap.containsKey(Object.class)) {
			this.statsMap.put(Object.class, new Stats());
		}
	}

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		this.stopWatchHolder.set(new StopWatchHolder(stopWatch, message.getPayload().getClass()));
		return message;
	}

	@Override
	public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
		StopWatchHolder holder = this.stopWatchHolder.get();
		if (holder != null) {
			holder.getStopWatch().stop();
			Stats stats = this.statsMap.get(holder.getType());
			if (stats == null) {
				stats = this.statsMap.get(Object.class);
			}
			stats.add(holder.getStopWatch().getLastTaskTimeMillis());
		}
	}

	@ManagedOperation
	public String[] getSummary() {
		String[] data = new String[this.statsMap.size()];
		int i = 0;
		for (Entry<Class<?>, Stats> entry : this.statsMap.entrySet()) {
			data[i++] = entry.getKey().getName() + " " + entry.getValue().toString();
		}
		return data;
	}

	@ManagedOperation
	public long getCount(String className) throws Exception {
		return this.statsMap.get(Class.forName(className)).getCount();
	}

	@ManagedOperation
	public long getLastTime(String className) throws Exception {
		return this.statsMap.get(Class.forName(className)).getLastTime();
	}

	@ManagedOperation
	public float getAverage(String className) throws Exception {
		return this.statsMap.get(Class.forName(className)).getAverage();
	}

	private class StopWatchHolder {

		private final StopWatch stopWatch;

		private final Class<?> type;

		/**
		 * @param stopWatch
		 * @param type
		 */
		public StopWatchHolder(StopWatch stopWatch, Class<?> type) {
			this.stopWatch = stopWatch;
			this.type = type;
		}

		public StopWatch getStopWatch() {
			return stopWatch;
		}

		public Class<?> getType() {
			return type;
		}
	}

	private class Stats {

		private long count;

		private long totalTime;

		private float average;

		private long lastTime;

		public long getCount() {
			return count;
		}

		public long getLastTime() {
			return lastTime;
		}

		public float getAverage() {
			return average;
		}

		public synchronized void add(long time) {
			this.count++;
			this.lastTime = time;
			this.totalTime += time;
			this.average = (float) this.totalTime / (float) this.count;
		}

		@Override
		public String toString() {
			return "Stats [count=" + count + ", average=" + average + ", lastTime=" + lastTime + "]";
		}


	}
}
