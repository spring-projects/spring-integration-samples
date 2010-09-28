/*
 * Copyright 2002-2010 the original author or authors.
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

package org.springframework.integration.samples.jmx;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

/**
 * @author Mark Fisher
 * @since 2.0
 */
@Component
@ManagedResource
public class StopWatch implements InitializingBean {

	private final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

	private volatile Future<?> future;

	private final AtomicInteger seconds = new AtomicInteger();

	@ManagedAttribute
	public int getSeconds() {
		return this.seconds.get();
	}

	public void afterPropertiesSet() {
		this.start();
	}

	@ManagedOperation
	public void start() {
		this.scheduler.initialize();
		this.future = this.scheduler.scheduleAtFixedRate(new Runnable() {
			public void run() {
				seconds.incrementAndGet();
			}
		}, 1000);
	}

	@ManagedOperation
	public void stop() {
		this.future.cancel(true);
	}

	@ManagedOperation
	public void reset() {
		this.seconds.set(0);
	}

}
