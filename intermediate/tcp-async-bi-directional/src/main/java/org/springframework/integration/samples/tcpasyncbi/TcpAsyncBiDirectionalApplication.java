package org.springframework.integration.samples.tcpasyncbi;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.ip.IpHeaders;
import org.springframework.integration.ip.dsl.Tcp;
import org.springframework.integration.ip.tcp.connection.AbstractClientConnectionFactory;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpConnectionCloseEvent;
import org.springframework.integration.ip.tcp.connection.TcpConnectionOpenEvent;
import org.springframework.integration.splitter.AbstractMessageSplitter;
import org.springframework.messaging.Message;

@SpringBootApplication
public class TcpAsyncBiDirectionalApplication {

	public static void main(String[] args) {
		SpringApplication.run(TcpAsyncBiDirectionalApplication.class, args);
	}

	@Bean
	SampleProperties properties() {
		return new SampleProperties();
	}

}

@Configuration
class ClientPeer {

	@Bean
	public AbstractClientConnectionFactory Client(SampleProperties properties) {
		return Tcp.netClient("localhost", properties.getServerPort()).get();
	}

	@Bean
	public IntegrationFlow clientOut(AbstractClientConnectionFactory client) {
		return IntegrationFlows.from(() -> "Hello from client", e -> e.poller(Pollers.fixedDelay(3000)))
				.handle(Tcp.outboundAdapter(client))
				.get();
	}

	@Bean
	public IntegrationFlow clientIn(AbstractClientConnectionFactory client) {
		return IntegrationFlows.from(Tcp.inboundAdapter(client))
				.transform(Transformers.objectToString())
				.log(msg -> "client: " + msg.getPayload())
				.get();
	}

}

@Configuration
class ServerPeer {

	private final Set<String> clients = ConcurrentHashMap.newKeySet();

	@Bean
	public AbstractServerConnectionFactory server(SampleProperties properties) {
		return Tcp.netServer(properties.getServerPort()).get();
	}

	@Bean
	public IntegrationFlow serverIn(AbstractServerConnectionFactory server) {
		return IntegrationFlows.from(Tcp.inboundAdapter(server))
				.transform(Transformers.objectToString())
				.log(msg -> "server: " + msg.getPayload())
				.get();
	}

	@Bean
	public IntegrationFlow serverOut(AbstractServerConnectionFactory server) {
		return IntegrationFlows.from(() -> "seed", e -> e.poller(Pollers.fixedDelay(5000)))
				.split(new AbstractMessageSplitter() {

					@Override
					protected Object splitMessage(Message<?> message) {
						return ServerPeer.this.clients.iterator();
					}

				})
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
