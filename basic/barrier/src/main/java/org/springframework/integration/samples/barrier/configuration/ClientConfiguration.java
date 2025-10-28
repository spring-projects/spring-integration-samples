package org.springframework.integration.samples.barrier.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.http.dsl.Http;
import org.springframework.messaging.MessageChannel;

/**
 * Configure client-side Spring Integration flows for the Barrier pattern sample.
 * <p>
 * Define the integration flow for sending HTTP requests to the server endpoint.
 * Demonstrate how to configure an HTTP outbound gateway that sends POST requests
 * and waits for responses.
 * <p>
 * Activate this configuration using the "java-config" profile.
 *
 * @author Glenn Renfro
 */
@Configuration
@Profile("java-config")
public class ClientConfiguration {

	/**
	 * The URL endpoint of the server's HTTP inbound gateway.
	 * Defaults to http://localhost:8080/postGateway if not configured.
	 */
	@Value("${barrier.url:http://localhost:8080/postGateway}")
	private String url;

	/**
	 * Define the main client integration flow.
	 * <p>
	 * Receive messages from the {@link #requestChannel()} and send them
	 * via HTTP POST to the configured server URL. Wait for and return
	 * the HTTP response as a String.
	 *
	 * @return the integration flow definition
	 */
	@Bean
	public IntegrationFlow clientFlow() {
		return IntegrationFlow.from(requestChannel())
			.handle(Http.outboundGateway(url)
				.httpMethod(HttpMethod.POST)
				.expectedResponseType(String.class))
			.get();
	}

	/**
	 * Create the request channel that feeds messages into the client flow.
	 * <p>
	 * Use a direct channel that provides point-to-point semantics,
	 * delivering messages to a single subscriber.
	 *
	 * @return a new DirectChannel instance
	 */
	@Bean
	public MessageChannel requestChannel() {
		return new DirectChannel();
	}

}
