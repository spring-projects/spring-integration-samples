/*
 * Copyright 2014-2017 the original author or authors.
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

package org.springframework.integration.samples.si4demo.dsl;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.http.inbound.HttpRequestHandlingMessagingGateway;
import org.springframework.integration.http.inbound.RequestMapping;
import org.springframework.integration.ip.tcp.TcpInboundGateway;
import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory;
import org.springframework.integration.json.ObjectToJsonTransformer;
import org.springframework.integration.transformer.ObjectToStringTransformer;
import org.springframework.integration.twitter.outbound.TwitterSearchOutboundGateway;
import org.springframework.messaging.MessageChannel;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

/**
 *
 * Spring Boot app offering telnet and http access to twitter search. Uses the Spring Integration 4.0 Java DSL extension.
 * Adds a filter and only returns the first tweet.
 *
 * <pre class=code>
 * $ telnet localhost 9876
 * Trying 127.0.0.1...
 * Connected to localhost.
 * Escape character is '^]'.
 * #springintegration
 * [{"extraData":{},"id":461548132401438720,"text":"RT @gprussell: Spring Integration 4.0.0.RELEASE is out! ...
 * </pre>
 *
 * <pre class=code>
 * $ curl http://localhost:8080/foo -H"content-type:text/plain" -d '#springintegration'
 * [{"extraData":{},"id":461548132401438720,"text":"RT @gprussell: Spring Integration 4.0.0.RELEASE is out! ...
 * </pre>
 *
 * It also supports typing a hashtag into the console.
 *
 * @author Gary Russell
 * @author Artem Bilan
 *
 * @since 4.0
 *
 */
@Configuration
@ComponentScan
@IntegrationComponentScan
@EnableAutoConfiguration
public class Application {

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
		Scanner scanner = new Scanner(System.in);
		String hashTag = scanner.nextLine();
		System.out.println(ctx.getBean(Gateway.class).sendReceive(hashTag));
		scanner.close();
		ctx.close();
	}

	@MessagingGateway(defaultRequestChannel = "requestChannel")
	public interface Gateway {

		String sendReceive(String in);

	}

	@Autowired
	private Environment env;

	@Bean
	TcpNetServerConnectionFactory cf() {
		return new TcpNetServerConnectionFactory(9876);
	}

	@Bean
	TcpInboundGateway tcpGate() {
		TcpInboundGateway gateway = new TcpInboundGateway();
		gateway.setConnectionFactory(cf());
		gateway.setRequestChannel(requestChannel());
		return gateway;
	}

	@Bean
	public HttpRequestHandlingMessagingGateway httpGate() {
		HttpRequestHandlingMessagingGateway gateway = new HttpRequestHandlingMessagingGateway(true);
		RequestMapping mapping = new RequestMapping();
		mapping.setMethods(HttpMethod.POST);
		mapping.setPathPatterns("/foo");
		gateway.setRequestMapping(mapping);
		gateway.setRequestChannel(requestChannel());
		gateway.setRequestPayloadTypeClass(byte[].class);
		return gateway;
	}

	@Bean
	public MessageChannel requestChannel() {
		return new DirectChannel();
	}

	@Bean
	@DependsOn("errorFlow")
	public IntegrationFlow flow() {
		return IntegrationFlows.from("requestChannel")
				.transform(new ObjectToStringTransformer())
				.filter((String p) -> p.startsWith("#spring"),
						f -> f.discardChannel("rejected"))
				.handle(twitterGate())
				.transform("payload[0]")
				.transform(new ObjectToJsonTransformer())
				.get();
	}

	@Bean
	public IntegrationFlow errorFlow() {
		return IntegrationFlows.from("rejected")
				.transform("'Error: hashtag must start with #spring; got' + payload")
				.get();
	}

	@Bean
	public TwitterSearchOutboundGateway twitterGate() {
		TwitterSearchOutboundGateway gateway = new TwitterSearchOutboundGateway(twitter());
		return gateway;
	}

	@Bean
	public Twitter twitter() {
		return new TwitterTemplate(env.getProperty("twitter.oauth.consumerKey"),
				env.getProperty("twitter.oauth.consumerSecret"),
				env.getProperty("twitter.oauth.accessToken"),
				env.getProperty("twitter.oauth.accessTokenSecret"));
	}

}
