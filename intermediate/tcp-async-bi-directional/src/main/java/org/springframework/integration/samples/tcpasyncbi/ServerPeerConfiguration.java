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

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.ip.IpHeaders;
import org.springframework.integration.ip.dsl.Tcp;
import org.springframework.integration.ip.dsl.TcpNetServerConnectionFactorySpec;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpConnectionCloseEvent;
import org.springframework.integration.ip.tcp.connection.TcpConnectionOpenEvent;

/**
 * Server peer configuration.
 *
 * @author Gary Russell
 * @author Glenn Renfro
 * @since 5.3
 *
 */
@Configuration
public class ServerPeerConfiguration {

	private final Set<String> clients = ConcurrentHashMap.newKeySet();

	@Bean
	public TcpNetServerConnectionFactorySpec server(SampleProperties properties) {
		return Tcp.netServer(properties.getServerPort());
	}

	@Bean
	public IntegrationFlow serverIn(AbstractServerConnectionFactory server) {
		return IntegrationFlow.from(Tcp.inboundAdapter(server))
				.transform(Transformers.objectToString())
				.log(msg -> "server: " + msg.getPayload())
				.get();
	}

	@Bean
	public IntegrationFlow serverOut(AbstractServerConnectionFactory server) {
		return IntegrationFlow.fromSupplier(() -> "seed", e -> e.poller(Pollers.fixedDelay(5000)))
				.split(this.clients, "iterator")
				.enrichHeaders(h -> h.headerExpression(IpHeaders.CONNECTION_ID, "payload"))
				.transform(p -> "Hello from server")
				.handle(Tcp.outboundAdapter(server))
				.get();
	}

	@EventListener
	public void open(TcpConnectionOpenEvent event) {
		if (event.getConnectionFactoryName().equals("server")) {
			this.clients.add(event.getConnectionId());
		}
	}

	@EventListener
	public void close(TcpConnectionCloseEvent event) {
		this.clients.remove(event.getConnectionId());
	}

}
