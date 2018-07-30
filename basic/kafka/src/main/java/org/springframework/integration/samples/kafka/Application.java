/*
 * Copyright 2015-2018 the original author or authors.
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

import java.util.Collections;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.context.IntegrationFlowContext;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter;
import org.springframework.integration.kafka.outbound.KafkaProducerMessageHandler;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.KafkaNull;
import org.springframework.kafka.support.TopicPartitionInitialOffset;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.GenericMessage;

/**
 * @author Gary Russell
 * @since 4.2
 */
@SpringBootApplication
@EnableConfigurationProperties(KafkaAppProperties.class)
public class Application {

	@Autowired
	private KafkaAppProperties properties;

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext context
				= new SpringApplicationBuilder(Application.class)
					.web(WebApplicationType.NONE)
					.run(args);
		context.getBean(Application.class).runDemo(context);
		context.close();
	}

	private void runDemo(ConfigurableApplicationContext context) {
		MessageChannel toKafka = context.getBean("toKafka", MessageChannel.class);
		System.out.println("Sending 10 messages...");
		Map<String, Object> headers = Collections.singletonMap(KafkaHeaders.TOPIC, this.properties.getTopic());
		for (int i = 0; i < 10; i++) {
			toKafka.send(new GenericMessage<>("foo" + i, headers));
		}
		System.out.println("Sending a null message...");
		toKafka.send(new GenericMessage<>(KafkaNull.INSTANCE, headers));
		PollableChannel fromKafka = context.getBean("fromKafka", PollableChannel.class);
		Message<?> received = fromKafka.receive(10000);
		int count = 0;
		while (received != null) {
			System.out.println(received);
			received = fromKafka.receive(++count < 11 ? 10000 : 1000);
		}
		System.out.println("Adding an adapter for a second topic and sending 10 messages...");
		addAnotherListenerForTopics(this.properties.getNewTopic());
		headers = Collections.singletonMap(KafkaHeaders.TOPIC, this.properties.getNewTopic());
		for (int i = 0; i < 10; i++) {
		    toKafka.send(new GenericMessage<>("bar" + i, headers));
		}
		received = fromKafka.receive(10000);
		count = 0;
		while (received != null) {
		    System.out.println(received);
			received = fromKafka.receive(++count < 10 ? 10000 : 1000);
		}
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
		handler.setMessageKeyExpression(new LiteralExpression(this.properties.getMessageKey()));
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
				new ContainerProperties(new TopicPartitionInitialOffset(this.properties.getTopic(), 0)));
	}

	@Bean
	public KafkaMessageDrivenChannelAdapter<String, String>
				adapter(KafkaMessageListenerContainer<String, String> container) {
		KafkaMessageDrivenChannelAdapter<String, String> kafkaMessageDrivenChannelAdapter =
				new KafkaMessageDrivenChannelAdapter<>(container);
		kafkaMessageDrivenChannelAdapter.setOutputChannel(fromKafka());
		return kafkaMessageDrivenChannelAdapter;
	}

	@Bean
	public PollableChannel fromKafka() {
		return new QueueChannel();
	}

	/*
	 * Boot's autoconfigured KafkaAdmin will provision the topics.
	 */

	@Bean
	public NewTopic topic(KafkaAppProperties properties) {
		return new NewTopic(properties.getTopic(), 1, (short) 1);
	}

	@Bean
	public NewTopic newTopic(KafkaAppProperties properties) {
		return new NewTopic(properties.getNewTopic(), 1, (short) 1);
	}

	@Autowired
	private IntegrationFlowContext flowContext;

	@Autowired
	private KafkaProperties kafkaProperties;

	public void addAnotherListenerForTopics(String... topics) {
		Map<String, Object> consumerProperties = kafkaProperties.buildConsumerProperties();
		// change the group id so we don't revoke the other partitions.
		consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG,
				consumerProperties.get(ConsumerConfig.GROUP_ID_CONFIG) + "x");
		IntegrationFlow flow =
			IntegrationFlows
				.from(Kafka.messageDrivenChannelAdapter(
						new DefaultKafkaConsumerFactory<String, String>(consumerProperties), topics))
				.channel("fromKafka")
				.get();
		this.flowContext.registration(flow).register();
	}

}
