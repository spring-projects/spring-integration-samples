package org.springframework.integration.samples.barrier.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.ValueExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.integration.aggregator.ExpressionEvaluatingCorrelationStrategy;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.aggregator.BarrierMessageHandler;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.samples.barrier.AckAggregator;
import org.springframework.messaging.MessageChannel;

/**
 * Configure server-side Spring Integration flows demonstrating the Barrier pattern.
 * <p>
 * Showcase an integration pattern where messages are:
 * <ol>
 *   <li>Received via HTTP endpoints</li>
 *   <li>Split into individual elements</li>
 *   <li>Published to RabbitMQ with publisher confirmations</li>
 *   <li>Held at a barrier until all confirmations are received</li>
 *   <li>Released as an aggregated result</li>
 * </ol>
 * <p>
 * Ensure that a message is only considered successfully processed when all of its
 * split components have been confirmed by the message broker.
 * <p>
 * Activate this configuration using the "java-config" profile.
 *
 * @author Glenn Renfro
 */
@Configuration
@Profile("java-config")
public class ServerConfiguration {

	/**
	 * Create a channel for receiving incoming messages from HTTP endpoints.
	 *
	 * @return a DirectChannel for point-to-point message delivery
	 */
	@Bean
	public MessageChannel receiveChannel() {
		return new DirectChannel();
	}

	/**
	 * Create a channel for payload messages in the GET gateway flow.
	 *
	 * @return a DirectChannel for point-to-point message delivery
	 */
	@Bean
	public MessageChannel createPayload() {
		return new DirectChannel();
	}

	/**
	 * Create a channel for broadcasting messages to multiple subscribers.
	 * <p>
	 * Use a publish-subscribe channel to send messages to both the barrier flow
	 * and the AMQP outbound flow simultaneously.
	 *
	 * @return a PublishSubscribeChannel for broadcasting messages
	 */
	@Bean
	public MessageChannel processChannel() {
		return new PublishSubscribeChannel();
	}

	/**
	 * Create a channel for receiving transformed messages from the barrier.
	 *
	 * @return a DirectChannel for point-to-point message delivery
	 */
	@Bean
	public MessageChannel transform() {
		return new DirectChannel();
	}

	/**
	 * Create a channel for receiving publisher confirmations (acks and nacks) from RabbitMQ.
	 *
	 * @return a DirectChannel for point-to-point message delivery
	 */
	@Bean
	public MessageChannel confirmations() {
		return new DirectChannel();
	}

	/**
	 * Create a channel for triggering the release of messages held at the barrier.
	 *
	 * @return a DirectChannel for point-to-point message delivery
	 */
	@Bean
	public MessageChannel release() {
		return new DirectChannel();
	}

	/**
	 * Create a channel for handling errors that occur during message processing.
	 *
	 * @return a DirectChannel for point-to-point message delivery
	 */
	@Bean
	public MessageChannel errorChannel() {
		return new DirectChannel();
	}

	/**
	 * Define an HTTP POST endpoint for receiving requests from clients.
	 * <p>
	 * Expose a REST endpoint at "/postGateway" that accepts POST requests
	 * and forwards them to the {@link #receiveChannel()} for processing.
	 *
	 * @return the integration flow for handling POST requests
	 */
	@Bean
	public IntegrationFlow postGatewayFlow() {
		return IntegrationFlow.from(Http.inboundGateway("/postGateway")
				.requestChannel(receiveChannel())
				.errorChannel(errorChannel())
				.requestMapping(m -> m.methods(HttpMethod.POST)))
			.get();
	}

	/**
	 * Define an HTTP GET endpoint for creating a default payload.
	 * <p>
	 * Expose a REST endpoint at "/getGateway" that accepts GET requests
	 * and creates a payload for testing purposes.
	 *
	 * @return the integration flow for handling GET requests
	 */
	@Bean
	public IntegrationFlow getGatewayFlow() {
		return IntegrationFlow.from(Http.inboundGateway("/getGateway")
				.requestChannel(createPayload())
				.errorChannel(errorChannel())
				.requestMapping(m -> m.methods(HttpMethod.GET)))
			.get();
	}

	/**
	 * Define a flow to transform GET requests into a comma-separated payload.
	 * <p>
	 * Generate the default payload "A,B,C" for GET requests
	 * and forward it to the receive channel for processing.
	 *
	 * @return the integration flow for transforming GET requests
	 */
	@Bean
	public IntegrationFlow transformGetFlow() {
		return IntegrationFlow.from(createPayload())
			.transform("'A,B,C'")
			.channel(receiveChannel())
			.get();
	}

	/**
	 * Define a flow to enrich message headers with correlation information.
	 * <p>
	 * Add an "ackCorrelation" header containing the message ID to correlate
	 * publisher confirmations with the original message.
	 *
	 * @return the integration flow for header enrichment
	 */
	@Bean
	public IntegrationFlow headerEnricherFlow() {
		return IntegrationFlow.from(receiveChannel())
			.enrichHeaders(h -> h.header("ackCorrelation", "headers['id']"))
			.channel(processChannel())
			.get();
	}

	/**
	 * Define a flow to split messages and publish them to RabbitMQ with publisher confirmations.
	 * <p>
	 * Perform the following steps:
	 * <ol>
	 *   <li>Receive messages from the process channel</li>
	 *   <li>Filter out HTTP-specific headers</li>
	 *   <li>Split the payload by comma delimiter</li>
	 *   <li>Publish each element to RabbitMQ</li>
	 *   <li>Route confirmations (acks/nacks) to the confirmations channel</li>
	 * </ol>
	 *
	 * @param rabbitTemplate the RabbitMQ template for publishing messages
	 * @return the integration flow for AMQP outbound processing
	 */
	@Bean
	public IntegrationFlow amqpOutboundFlow(RabbitTemplate rabbitTemplate) {
		return IntegrationFlow.from(processChannel())
			.headerFilter("content-type", "content-length")
			.splitWith(s -> s.delimiters(","))
			.handle(Amqp.outboundAdapter(rabbitTemplate)
				.exchangeName("barrier.sample.exchange")
				.routingKey("barrier.sample.key")
				.confirmAckChannel(confirmations())
				.confirmNackChannel(confirmations())
				.returnChannel(errorChannel())
				.confirmCorrelationExpression("#this"))
			.get();
	}

	/**
	 * Create the barrier message handler that holds messages until conditions are met.
	 * <p>
	 * Configure the barrier to wait for all split message parts to be confirmed by the broker
	 * before releasing the aggregated result. Use a 10-second timeout and correlate
	 * messages using the "ackCorrelation" header.
	 *
	 * @return the configured BarrierMessageHandler
	 */
	@Bean
	public BarrierMessageHandler barrier() {
		BarrierMessageHandler handler = new BarrierMessageHandler(10000,
				new ExpressionEvaluatingCorrelationStrategy(new ValueExpression<>("ackCorrelation")));
		handler.setOutputChannelName("transform");
		return handler;
	}

	/**
	 * Define a flow to send messages to the barrier where they wait for confirmations.
	 * <p>
	 * Feed messages from the process channel into the barrier handler,
	 * where they are held until all corresponding confirmations are received.
	 *
	 * @return the integration flow for barrier processing
	 */
	@Bean
	public IntegrationFlow barrierFlow() {
		return IntegrationFlow.from(processChannel())
			.handle(barrier())
			.get();
	}

	/**
	 * Define a flow to extract and transform the result from the barrier.
	 * <p>
	 * Take the aggregated result from the barrier (an array containing
	 * the original message and confirmations) and extract the second element
	 * (index 1), which contains the confirmation data.
	 *
	 * @return the integration flow for transforming barrier output
	 */
	@Bean
	public IntegrationFlow transformFlow() {
		return IntegrationFlow.from(transform())
			.transform("payload[1]")
			.get();
	}

	/**
	 * Define a flow to aggregate publisher confirmations and trigger barrier release.
	 * <p>
	 * Perform the following steps:
	 * <ol>
	 *   <li>Receive individual confirmations from RabbitMQ</li>
	 *   <li>Filter out framework headers</li>
	 *   <li>Aggregate confirmations by correlation ID</li>
	 *   <li>Send the aggregated result to trigger barrier release</li>
	 * </ol>
	 *
	 * @return the integration flow for processing confirmations
	 */
	@Bean
	public IntegrationFlow confirmationsFlow() {
		return IntegrationFlow.from(confirmations())
			.headerFilter("replyChannel", "errorChannel")
			.handle((payload, headers) -> payload)
			.aggregate(a -> a.processor(new AckAggregator()))
			.channel(release())
			.get();
	}

	/**
	 * Define a flow to trigger the barrier to release held messages.
	 * <p>
	 * Invoke the barrier's trigger method to release the corresponding
	 * message from the barrier when all confirmations for a message have been aggregated.
	 *
	 * @return the integration flow for barrier release
	 */
	@Bean
	public IntegrationFlow releaseFlow() {
		return IntegrationFlow.from(release())
			.handle(barrier(), "trigger")
			.get();
	}

	/**
	 * Define a flow to consume messages from the RabbitMQ queue.
	 * <p>
	 * Read messages from the queue and discard them to the null channel.
	 * In a production scenario, process the messages further as needed.
	 *
	 * @param rabbitConnectionFactory the RabbitMQ connection factory
	 * @return the integration flow for AMQP inbound processing
	 */
	@Bean
	public IntegrationFlow amqpInboundFlow(ConnectionFactory rabbitConnectionFactory) {
		return IntegrationFlow.from(Amqp.inboundAdapter(rabbitConnectionFactory, "barrier.sample.queue"))
			.channel("nullChannel")
			.get();
	}

	/**
	 * Create the RabbitMQ queue for receiving published messages.
	 * <p>
	 * Configure the queue as:
	 * <ul>
	 *   <li>Non-durable (not persisted to disk)</li>
	 *   <li>Non-exclusive (can be accessed by multiple connections)</li>
	 *   <li>Auto-delete (deleted when no longer in use)</li>
	 * </ul>
	 *
	 * @return the configured Queue instance
	 */
	@Bean
	public Queue barrierSampleQueue() {
		return new Queue("barrier.sample.queue", false, false, true);
	}

	/**
	 * Create the RabbitMQ exchange for routing messages.
	 * <p>
	 * Configure the exchange as:
	 * <ul>
	 *   <li>Direct exchange (routes based on exact routing key match)</li>
	 *   <li>Non-durable (not persisted to disk)</li>
	 *   <li>Auto-delete (deleted when no longer bound to any queues)</li>
	 * </ul>
	 *
	 * @return the configured DirectExchange instance
	 */
	@Bean
	public DirectExchange barrierSampleExchange() {
		return new DirectExchange("barrier.sample.exchange", false, true);
	}

	/**
	 * Bind the queue to the exchange with a routing key.
	 * <p>
	 * Route messages published to the exchange with routing key "barrier.sample.key"
	 * to the barrier sample queue.
	 *
	 * @param barrierSampleQueue the queue to bind
	 * @param barrierSampleExchange the exchange to bind to
	 * @return the configured Binding instance
	 */
	@Bean
	public Binding barrierSampleBinding(Queue barrierSampleQueue, DirectExchange barrierSampleExchange) {
		return BindingBuilder.bind(barrierSampleQueue)
			.to(barrierSampleExchange)
			.with("barrier.sample.key");
	}

}
