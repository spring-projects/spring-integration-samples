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
package org.springframework.integration.samples.splunk;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.samples.splunk.event.OrderEvent;
import org.springframework.integration.splunk.event.SplunkEvent;
import org.springframework.integration.splunk.support.SplunkServer;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

/**
 * @author Filippo Balicchia
 * @since 4.2
 */
@SpringBootApplication
@ImportResource("si-splunk-example-context.xml")
public class Application {

	@Value("${splunk.host}")
	private String host;
	@Value("${splunk.port}")
	private String port;
	@Value("${splunk.username}")
	private String username;
	@Value("${splunk.password}")
	private String password;
	@Value("${splunk.owner}")
	private String owner;
	public static void main(String[] args) throws Exception {

		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		MessageChannel channelRestOutput = context.getBean("toSplunk", MessageChannel.class);
		sendWithRest(channelRestOutput);
		System.out.println("Consumer");

	}

	private static void sendWithRest(MessageChannel channel) {

		for (int i = 0; i < 10; i++) {
			channel.send(MessageBuilder.withPayload(createEvent()).build());
		}

	}

	private static SplunkEvent createEvent(){
		int nextInt = ThreadLocalRandom.current().nextInt(1, 1000);
		OrderEvent sd = new OrderEvent();
		sd.setEan(String.valueOf (nextInt));
		sd.setEmailuser("mail@gmail.com");
		sd.setOrderNumber("21501001010101");
		return sd;
	}

	@Bean
	public SplunkServer splunkServerRef() {
		SplunkServer splunkServer = new SplunkServer();
		splunkServer.setPort(Integer.valueOf(port));
		splunkServer.setHost(host);
		splunkServer.setUsername(username);
		splunkServer.setOwner(owner);
		splunkServer.setPassword(password);
		return splunkServer;
	}

}
