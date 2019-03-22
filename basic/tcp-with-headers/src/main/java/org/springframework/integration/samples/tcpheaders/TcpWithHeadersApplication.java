/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.integration.samples.tcpheaders;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.LoggingHandler.Level;
import org.springframework.integration.ip.dsl.Tcp;
import org.springframework.integration.ip.tcp.connection.MessageConvertingTcpMessageMapper;
import org.springframework.integration.ip.tcp.serializer.MapJsonSerializer;
import org.springframework.integration.support.converter.MapMessageConverter;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.util.StringUtils;

@SpringBootApplication
public class TcpWithHeadersApplication {

	public static void main(String[] args) {
		SpringApplication.run(TcpWithHeadersApplication.class, args);
	}

	// Client side

	public interface TcpExchanger {

		public String exchange(String data, @Header("type") String type);

	}

	@Bean
	public IntegrationFlow client(@Value("${tcp.port:1234}") int port) {
		return IntegrationFlows.from(TcpExchanger.class)
				.handle(Tcp.outboundGateway(Tcp.netClient("localhost", port)
						.deserializer(jsonMapping())
						.serializer(jsonMapping())
						.mapper(mapper())))
				.get();
	}

	// Server side

	@Bean
	public IntegrationFlow server(@Value("${tcp.port:1234}") int port) {
		return IntegrationFlows.from(Tcp.inboundGateway(Tcp.netServer(port)
						.deserializer(jsonMapping())
						.serializer(jsonMapping())
						.mapper(mapper())))
				.log(Level.INFO, "exampleLogger", "'Received type header:' + headers['type']")
				.route("headers['type']", r -> r
						.subFlowMapping("upper",
								subFlow -> subFlow.transform(String.class, p -> p.toUpperCase()))
						.subFlowMapping("lower",
								subFlow -> subFlow.transform(String.class, p -> p.toLowerCase())))
				.get();
	}

	// Common

	@Bean
	public MessageConvertingTcpMessageMapper mapper() {
		MapMessageConverter converter = new MapMessageConverter();
		converter.setHeaderNames("type");
		return new MessageConvertingTcpMessageMapper(converter);
	}

	@Bean
	public MapJsonSerializer jsonMapping() {
		return new MapJsonSerializer();
	}

	// Console

	@Bean
	@DependsOn("client")
	public ApplicationRunner runner(TcpExchanger exchanger,
			ConfigurableApplicationContext context) {

		return args -> {
			System.out.println("Enter some text; if it starts with a lower case character,\n"
					+ "it will be uppercased by the server; otherwise it will be lowercased;\n"
					+ "enter 'quit' to end");
			Scanner scanner = new Scanner(System.in);
			String request;
			if (scanner.hasNextLine()) {
				request = scanner.nextLine();
				while (!"quit".equals(request.toLowerCase())) {
					if (StringUtils.hasText(request)) {
						String result = exchanger.exchange(request,
								Character.isLowerCase(request.charAt(0)) ? "upper" : "lower");
						System.out.println(result);
					}
					if (scanner.hasNextLine()) {
						request = scanner.nextLine();
					}
					else {
						request = "quit";
					}
				}
			}
			scanner.close();
			context.close();
		};
	}

}
