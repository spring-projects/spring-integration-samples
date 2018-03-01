/*
 * Copyright 2016-2018 the original author or authors.
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

package org.springframework.integration.samples.dsl.kafka;

import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.context.IntegrationFlowContext;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;

/**
 * @author Gary Russell
 * @author Artem Bilan
 * @since 4.3
 */
@SpringBootApplication
@EnableConfigurationProperties(KafkaAppProperties.class)
public class Application {

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext context =
				new SpringApplicationBuilder(Application.class)
				.web(WebApplicationType.NONE)
				.run(args);
		context.getBean(Application.class).runDemo(context);
		context.close();
	}

	private void runDemo(ConfigurableApplicationContext context) {
		KafkaGateway kafkaGateway = context.getBean(KafkaGateway.class);
		System.out.println("Sending 10 messages...");
		for (int i = 0; i < 10; i++) {
			String message = "foo" + i;
			System.out.println("Send to Kafka: " + message);
			kafkaGateway.sendToKafka(message, this.properties.getTopic());
		}

		for (int i = 0; i < 10; i++) {
			Message<?> received = kafkaGateway.receiveFromKafka();
			System.out.println(received);
		}
		System.out.println("Adding an adapter for a second topic and sending 10 messages...");
		addAnotherListenerForTopics(this.properties.getNewTopic());
		for (int i = 0; i < 10; i++) {
			String message = "bar" + i;
			System.out.println("Send to Kafka: " + message);
			kafkaGateway.sendToKafka(message, this.properties.getNewTopic());
		}
		for (int i = 0; i < 10; i++) {
			Message<?> received = kafkaGateway.receiveFromKafka();
			System.out.println(received);
		}
		context.close();
	}

	@Autowired
	private KafkaAppProperties properties;

	@MessagingGateway
	public interface KafkaGateway {

		@Gateway(requestChannel = "toKafka.input")
		void sendToKafka(String payload, @Header(KafkaHeaders.TOPIC) String topic);

		@Gateway(replyChannel = "fromKafka", replyTimeout = 10000)
		Message<?> receiveFromKafka();

	}

	@Bean
	public IntegrationFlow toKafka(KafkaTemplate<?, ?> kafkaTemplate) {
		return f -> f
				.handle(Kafka.outboundChannelAdapter(kafkaTemplate)
						.messageKey(this.properties.getMessageKey()));
	}

	@Bean
	public IntegrationFlow fromKafkaFlow(ConsumerFactory<?, ?> consumerFactory) {
		return IntegrationFlows
				.from(Kafka.messageDrivenChannelAdapter(consumerFactory, this.properties.getTopic()))
				.channel(c -> c.queue("fromKafka"))
				.get();
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
