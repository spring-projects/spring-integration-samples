/*
 * Copyright 2016 the original author or authors.
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

import java.util.Properties;

import javax.annotation.PostConstruct;

import org.I0Itec.zkclient.ZkClient;
import org.apache.kafka.common.errors.TopicExistsException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;

import kafka.admin.AdminUtils;
import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;

/**
 * @author Gary Russell
 * @author Artem Bilan
 * @since 4.3
 */
@SpringBootApplication
@IntegrationComponentScan
public class Application {

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext context =
				new SpringApplicationBuilder(Application.class)
				.web(false)
				.run(args);

		KafkaGateway kafkaGateway = context.getBean(KafkaGateway.class);
		for (int i = 0; i < 10; i++) {
			String message = "foo" + i;
			System.out.println("Send to Kafka: " + message);
			kafkaGateway.sendToKafka(message);
		}

		Message<?> received = kafkaGateway.receiveFromKafka();
		while (received != null) {
			System.out.println(received);
			received = kafkaGateway.receiveFromKafka();
		}

		context.close();
		System.exit(0);
	}

	@Value("${kafka.topic}")
	private String topic;

	@Value("${kafka.messageKey}")
	private String messageKey;

	@Value("${kafka.zookeeper.connect}")
	private String zookeeperConnect;

	@PostConstruct
	public void init() {
		ZkClient zkClient = new ZkClient(this.zookeeperConnect, 6000, 6000, ZKStringSerializer$.MODULE$);
		ZkUtils zkUtils = new ZkUtils(zkClient, null, false);
		try {
			AdminUtils.createTopic(zkUtils, topic, 1, 1, new Properties(), null);
		}
		catch (TopicExistsException e) {
			// no-op
		}
	}

	@MessagingGateway
	public interface KafkaGateway {

		@Gateway(requestChannel = "toKafka.input")
		void sendToKafka(String payload);

		@Gateway(replyChannel = "fromKafka", replyTimeout = 10000)
		Message<?> receiveFromKafka();

	}

	@Bean
	public IntegrationFlow toKafka(KafkaTemplate<?, ?> kafkaTemplate) {
		return f -> f
				.handle(Kafka.outboundChannelAdapter(kafkaTemplate)
						.topic(this.topic)
						.messageKey(this.messageKey));
	}

	@Bean
	public IntegrationFlow fromKafka(ConsumerFactory<?, ?> consumerFactory) {
		return IntegrationFlows
				.from(Kafka.messageDrivenChannelAdapter(consumerFactory, this.topic))
				.channel(c -> c.queue("fromKafka"))
				.get();
	}

}
