/*
 * Copyright 2020-present the original author or authors.
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

package org.springframework.integration.samples.tcpasyncbi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.ip.dsl.Tcp;
import org.springframework.integration.ip.dsl.TcpNetClientConnectionFactorySpec;
import org.springframework.integration.ip.tcp.connection.AbstractClientConnectionFactory;

/**
 * Client peer configuration.
 *
 * @author Gary Russell
 * @author Glenn Renfro
 * @since 5.3
 *
 */
@Configuration
public class ClientPeerConfiguration {

	@Bean
	public TcpNetClientConnectionFactorySpec client1(SampleProperties properties) {
		return Tcp.netClient("localhost", properties.getServerPort());
	}

	@Bean
	public IntegrationFlow client1Out(AbstractClientConnectionFactory client1) {
		return IntegrationFlow.fromSupplier(() -> "Hello from client1", e -> e.id("client1Adapter")
						.poller(Pollers.fixedDelay(3000)))
				.handle(Tcp.outboundAdapter(client1))
				.get();
	}

	@Bean
	public IntegrationFlow client1In(AbstractClientConnectionFactory client1) {
		return IntegrationFlow.from(Tcp.inboundAdapter(client1))
				.transform(Transformers.objectToString())
				.log(msg -> "client1: " + msg.getPayload())
				.get();
	}

	@Bean
	public TcpNetClientConnectionFactorySpec client2(SampleProperties properties) {
		return Tcp.netClient("localhost", properties.getServerPort());
	}

	@Bean
	public IntegrationFlow client2Out(AbstractClientConnectionFactory client2) {
		return IntegrationFlow.fromSupplier(() -> "Hello from client2", e -> e.id("client2Adapter")
						.poller(Pollers.fixedDelay(2000)))
				.handle(Tcp.outboundAdapter(client2))
				.get();
	}

	@Bean
	public IntegrationFlow client2In(AbstractClientConnectionFactory client2) {
		return IntegrationFlow.from(Tcp.inboundAdapter(client2))
				.transform(Transformers.objectToString())
				.log(msg -> "client2: " + msg.getPayload())
				.get();
	}

}
