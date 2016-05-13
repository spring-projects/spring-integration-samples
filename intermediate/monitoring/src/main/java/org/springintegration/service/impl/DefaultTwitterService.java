/*
 * Copyright 2002-2012 the original author or authors
 *
 *	 Licensed under the Apache License, Version 2.0 (the "License");
 *	 you may not use this file except in compliance with the License.
 *	 You may obtain a copy of the License at
 *
 *		 http://www.apache.org/licenses/LICENSE-2.0
 *
 *	 Unless required by applicable law or agreed to in writing, software
 *	 distributed under the License is distributed on an "AS IS" BASIS,
 *	 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	 See the License for the specific language governing permissions and
 *	 limitations under the License.
 */
package org.springintegration.service.impl;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.endpoint.SourcePollingChannelAdapter;
import org.springframework.integration.model.TwitterMessage;
import org.springframework.integration.monitor.IntegrationMBeanExporter;
import org.springframework.integration.service.TwitterService;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.jmx.export.annotation.ManagedMetric;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.jmx.support.MetricType;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Service;

/**
 * Implementation of the TwitterService interface.
 * In the org.springintegration package because SI excludes all org.springframework.integration.*
 * classes from MBean export.
 */
@Service
@ManagedResource
public class DefaultTwitterService implements TwitterService {

	/** Holds a collection of polled Twitter messages */
	private Map<Long, TwitterMessage> twitterMessages;

	@Autowired
	private MessageChannel controlBusChannel;

	private long totalTweets;

	@Autowired(required=false)
	@Qualifier("dummyTwitter")
	private SourcePollingChannelAdapter dummyTwitter;

	@Autowired(required=false)
	private IntegrationMBeanExporter exporter;
	/**
	 * Constructor that initializes the 'twitterMessages' Map as a simple LRU
	 * cache. @See http://blogs.oracle.com/swinger/entry/collections_trick_i_lru_cache
	 */
	public DefaultTwitterService() {

		twitterMessages = new LinkedHashMap<Long, TwitterMessage>( 10, 0.75f, true ) {

			private static final long serialVersionUID = 1L;

			@Override
			protected boolean removeEldestEntry( java.util.Map.Entry<Long, TwitterMessage> entry ) {
				return size() > 10;
			}

		};

	}


	/**
	 * @return the totalTweets
	 */
	@ManagedMetric(metricType=MetricType.COUNTER)
	public long getTotalTweets() {
		return totalTweets;
	}


	/** {@inheritDoc} */
	@Override
	public Collection<TwitterMessage> getTwitterMessages() {
		return twitterMessages.values();
	}

	/** {@inheritDoc} */
	@Override
	public void startTwitterAdapter() {

		Message<String> operation = MessageBuilder.withPayload("@twitter.start()").build();

		this.controlBusChannel.send(operation);

		if (this.dummyTwitter != null) {
			this.controlBusChannel.send(MessageBuilder.withPayload("@dummyTwitter.start()").build());
		}
	}

	/** {@inheritDoc} */
	@Override
	public void stopTwitterAdapter() {

		Message<String> operation = MessageBuilder.withPayload("@twitter.stop()").build();

		this.controlBusChannel.send(operation);

		if (this.dummyTwitter != null) {
			this.controlBusChannel.send(MessageBuilder.withPayload("@dummyTwitter.stop()").build());
		}
	}


	@Override
	public void shutdown() {
		Message<String> operation = MessageBuilder.withPayload("@integrationMBeanExporter.stopActiveComponents(false, 20000)").build();

		if (this.exporter != null) {
			this.controlBusChannel.send(operation);
		}
	}


	/**
	 * Called by Spring Integration to populate a simple LRU cache.
	 *
	 * @param tweet - The Spring Integration tweet object.
	 * @throws InterruptedException
	 */
	public void addTwitterMessages(Tweet tweet) throws Exception {
		if ("SomeUser".equals(tweet.getFromUser())) {
			Thread.sleep(2000);
		}
		this.totalTweets++;
		this.twitterMessages.put(tweet.getCreatedAt().getTime(), new TwitterMessage(tweet.getCreatedAt(),
				tweet.getText(),
				tweet.getFromUser(),
				tweet.getProfileImageUrl()));
	}

}
