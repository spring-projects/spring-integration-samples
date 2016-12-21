/*
 * Copyright 2015 the original author or authors.
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

package org.springframework.integration.samples.kafka;

import java.util.Map;
import java.util.Properties;

import org.I0Itec.zkclient.ZkClient;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.errors.TopicExistsException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter;
import org.springframework.integration.kafka.outbound.KafkaProducerMessageHandler;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.support.KafkaNull;
import org.springframework.kafka.support.TopicPartitionInitialOffset;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.GenericMessage;

import kafka.admin.AdminUtils;
import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;

/**
 * @author Gary Russell
 * @since 4.2
 */
@SpringBootApplication
public class Application {

	@Value("${kafka.topic}")
	private String topic;

	@Value("${kafka.messageKey}")
	private String messageKey;

	@Value("${kafka.zookeeper.connect}")
	private String zookeeperConnect;

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext context
				= new SpringApplicationBuilder(Application.class)
				.web(false)
				.run(args);
		MessageChannel toKafka = context.getBean("toKafka", MessageChannel.class);
		for (int i = 0; i < 10; i++) {
			toKafka.send(new GenericMessage<>("foo" + i));
		}
		toKafka.send(new GenericMessage<>(KafkaNull.INSTANCE));
		PollableChannel fromKafka = context.getBean("received", PollableChannel.class);
		Message<?> received = fromKafka.receive(10000);
		while (received != null) {
			System.out.println(received);
			received = fromKafka.receive(10000);
		}
		context.close();
		System.exit(0);
	}

	@Bean
	public ProducerFactory<?, ?> kafkaProducerFactory(KafkaProperties properties) {
		Map<String, Object> producerProperties = properties.buildProducerProperties();
		producerProperties.put(ProducerConfig.LINGER_MS_CONFIG, 1);
		return new DefaultKafkaProducerFactory<>(producerProperties);
	}

	@ServiceActivator(inputChannel = "toKafka")
	@Bean
	public MessageHandler handler(KafkaTemplate<String, String> kafkaTemplate) {
		KafkaProducerMessageHandler<String, String> handler =
				new KafkaProducerMessageHandler<>(kafkaTemplate);
		handler.setTopicExpression(new LiteralExpression(this.topic));
		handler.setMessageKeyExpression(new LiteralExpression(this.messageKey));
		return handler;
	}

	@Bean
	public ConsumerFactory<?, ?> kafkaConsumerFactory(KafkaProperties properties) {
		Map<String, Object> consumerProperties = properties
				.buildConsumerProperties();
		consumerProperties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 15000);
		return new DefaultKafkaConsumerFactory<>(consumerProperties);
	}

	@Bean
	public KafkaMessageListenerContainer<String, String> container(
			ConsumerFactory<String, String> kafkaConsumerFactory) {
		return new KafkaMessageListenerContainer<>(kafkaConsumerFactory,
				new ContainerProperties(new TopicPartitionInitialOffset(this.topic, 0)));
	}

	@Bean
	public KafkaMessageDrivenChannelAdapter<String, String>
				adapter(KafkaMessageListenerContainer<String, String> container) {
		KafkaMessageDrivenChannelAdapter<String, String> kafkaMessageDrivenChannelAdapter =
				new KafkaMessageDrivenChannelAdapter<>(container);
		kafkaMessageDrivenChannelAdapter.setOutputChannel(received());
		return kafkaMessageDrivenChannelAdapter;
	}

	@Bean
	public PollableChannel received() {
		return new QueueChannel();
	}

	@Bean
	public TopicCreator topicCreator() {
		return new TopicCreator(this.topic, this.zookeeperConnect);
	}

	public static class TopicCreator implements SmartLifecycle {

		private final String topic;

		private final String zkConnect;

		private volatile boolean running;

		public TopicCreator(String topic, String zkConnect) {
			this.topic = topic;
			this.zkConnect = zkConnect;
		}

		@Override
		public void start() {
			ZkUtils zkUtils = new ZkUtils(new ZkClient(this.zkConnect, 6000, 6000,
				ZKStringSerializer$.MODULE$), null, false);
			try {
				AdminUtils.createTopic(zkUtils, topic, 1, 1, new Properties(), null);
			}
			catch (TopicExistsException e) {
				// no-op
			}
			this.running = true;
		}

		@Override
		public void stop() {
		}

		@Override
		public boolean isRunning() {
			return this.running;
		}

		@Override
		public int getPhase() {
			return Integer.MIN_VALUE;
		}

		@Override
		public boolean isAutoStartup() {
			return true;
		}

		@Override
		public void stop(Runnable callback) {
			callback.run();
		}

	}

}
