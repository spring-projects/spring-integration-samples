package org.springframework.integration.samples.barrier2.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.aggregator.BarrierMessageHandler;
import org.springframework.integration.aggregator.HeaderAttributeCorrelationStrategy;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.samples.barrier2.Aggregator;
import org.springframework.integration.samples.barrier2.Gateway;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Configure Spring Integration flows demonstrating error handling with the Barrier pattern.
 * <p>
 * This configuration implements an error-handling scenario where a gateway launches asynchronous
 * tasks and waits for completion, aggregating both successful results and failures. The barrier
 * pattern ensures the calling thread is suspended until all tasks complete, then returns either
 * the aggregated results or throws an exception containing all errors.
 * <p>
 * Flow Overview:
 * <ol>
 *   <li>Gateway receives a collection of integers and a correlation ID</li>
 *   <li>Messages are split into individual elements and sent to a queue</li>
 *   <li>Polled messages are transformed (10 / payload), causing divide-by-zero for zero values</li>
 *   <li>Successful results and errors are aggregated by correlation</li>
 *   <li>Barrier is released when aggregation completes, returning to the caller</li>
 * </ol>
 * <p>
 * Activate this configuration using the "java-config" profile in application.properties.
 *
 * @author Glenn Renfro
 */
@Configuration
@Profile("java-config")
public class ErrorHandlingConfiguration {

	/**
	 * Create the main process channel for broadcasting messages to multiple subscribers.
	 * <p>
	 * Use a publish-subscribe channel to send messages to both the splitter flow
	 * and the barrier flow simultaneously.
	 *
	 * @return a PublishSubscribeChannel for message broadcasting
	 */
	@Bean
	public MessageChannel processChannel() {
		return new PublishSubscribeChannel();
	}

	/**
	 * Create a queue channel for buffering messages awaiting processing.
	 * <p>
	 * Store split messages in this queue until the poller retrieves them for
	 * transformation.
	 *
	 * @return a QueueChannel for message buffering
	 */
	@Bean
	public QueueChannel process() {
		return new QueueChannel();
	}

	/**
	 * Create the aggregator channel for receiving processed messages and errors.
	 * <p>
	 * Direct both successful transformations and errors to this channel for
	 * aggregation by correlation ID.
	 *
	 * @return a DirectChannel for aggregator input
	 */
	@Bean
	public MessageChannel aggregatorChannel() {
		return new DirectChannel();
	}

	/**
	 * Create the release channel for triggering barrier release.
	 * <p>
	 * Send aggregated results to this channel to trigger the barrier handler's
	 * release mechanism, allowing the suspended thread to continue.
	 *
	 * @return a DirectChannel for barrier release signals
	 */
	@Bean
	public MessageChannel release() {
		return new DirectChannel();
	}

	/**
	 * Create the errors channel for receiving transformation failures.
	 * <p>
	 * Route exceptions from the transformation step to this channel for header
	 * enrichment before aggregation.
	 *
	 * @return a DirectChannel for error handling
	 */
	@Bean
	public MessageChannel errors() {
		return new DirectChannel();
	}

	/**
	 * Create a thread pool executor for asynchronous message processing.
	 * <p>
	 * Configure a task executor to handle polling and transformation operations
	 * on separate threads from the calling thread.
	 *
	 * @return a ThreadPoolTaskExecutor for async operations
	 */
	@Bean
	public ThreadPoolTaskExecutor exec() {
		return new ThreadPoolTaskExecutor();
	}

	/**
	 * Define the gateway integration flow for receiving messages from the Gateway interface.
	 * <p>
	 * Create a gateway proxy from the {@link Gateway} interface that routes incoming
	 * messages to the process channel for splitting and barrier processing.
	 *
	 * @return the integration flow for gateway message ingress
	 */
	@Bean
	public IntegrationFlow gatewayFlow() {
		return IntegrationFlow.from(Gateway.class)
			.channel(processChannel())
			.get();
	}

	/**
	 * Define the splitter flow for dividing collections into individual messages.
	 * <p>
	 * Split incoming message payloads into individual elements and send each element
	 * to the process queue channel for independent transformation.
	 *
	 * @return the integration flow for message splitting
	 */
	@Bean
	public IntegrationFlow splitterFlow() {
		return IntegrationFlow.from(processChannel())
			.split()
			.channel(process())
			.get();
	}

	/**
	 * Define the transform flow for processing messages with error handling.
	 * <p>
	 * Poll messages from the queue every second using the configured task executor,
	 * transform each message using the expression "10 / payload" (causing ArithmeticException
	 * for zero values), and route results to the aggregator channel. Send any errors
	 * to the errors channel for special handling.
	 *
	 * @param exec the thread pool executor for polling operations
	 * @return the integration flow for message transformation
	 */
	@Bean
	public IntegrationFlow transformFlow(ThreadPoolTaskExecutor exec) {

		@SuppressWarnings("unchecked")
		org.springframework.integration.core.MessageSource<Object> messageSource =
				() -> (org.springframework.messaging.Message<Object>) process().receive(1000);
		return IntegrationFlow.from(messageSource, e -> e.poller(Pollers.fixedDelay(1000)
						.errorChannel(errors())
						.taskExecutor(exec)))
				.transform("10 / payload")
				.channel(aggregatorChannel())
				.get();
	}

	/**
	 * Create the barrier message handler for suspending the calling thread.
	 * <p>
	 * Configure a barrier with a 10-second timeout that correlates messages using
	 * the "barrierCorrelation" header. Suspend the gateway caller until the barrier
	 * is triggered by the release flow.
	 *
	 * @return the configured BarrierMessageHandler
	 */
	@Bean
	public BarrierMessageHandler errorBarrier() {
		return new BarrierMessageHandler(10000, new HeaderAttributeCorrelationStrategy("barrierCorrelation"));
	}

	/**
	 * Define the barrier flow for suspending the calling thread.
	 * <p>
	 * Send messages from the process channel to the barrier handler, which suspends
	 * the thread until the barrier is released by the release flow.
	 *
	 * @return the integration flow for barrier processing
	 */
	@Bean
	public IntegrationFlow barrierFlow() {
		return IntegrationFlow.from(processChannel())
			.handle(errorBarrier())
			.get();
	}

	/**
	 * Define the aggregator flow for combining results and errors.
	 * <p>
	 * Aggregate messages from the aggregator channel using a custom aggregator
	 * that consolidates both successful results and exceptions. Send the aggregated
	 * result to the release channel to trigger barrier release.
	 *
	 * @return the integration flow for message aggregation
	 */
	@Bean
	public IntegrationFlow aggregatorFlow() {
		return IntegrationFlow.from(aggregatorChannel())
			.aggregate(a -> a.processor(new Aggregator()))
			.channel(release())
			.get();
	}

	/**
	 * Define the release flow for triggering barrier release.
	 * <p>
	 * Send aggregated results from the release channel to the barrier's trigger method,
	 * which releases the suspended thread and returns the result to the gateway caller.
	 *
	 * @return the integration flow for barrier release
	 */
	@Bean
	public IntegrationFlow releaseFlow() {
		return IntegrationFlow.from(release())
			.handle(errorBarrier(), "trigger")
			.get();
	}

	/**
	 * Define the error flow for handling transformation failures.
	 * <p>
	 * Enrich error messages with correlation headers extracted from the failed message,
	 * then route them to the aggregator channel for consolidation with successful results.
	 * Restore the correlationId, sequenceSize, and sequenceNumber headers to enable proper
	 * aggregation of error messages alongside successful transformations.
	 *
	 * @return the integration flow for error processing
	 */
	@Bean
	public IntegrationFlow errorFlow() {
		return IntegrationFlow.from(errors())
			.enrichHeaders(h -> h
				.headerExpression("correlationId", "payload.failedMessage.headers.correlationId")
				.headerExpression("sequenceSize", "payload.failedMessage.headers.sequenceSize")
				.headerExpression("sequenceNumber", "payload.failedMessage.headers.sequenceNumber"))
			.channel(aggregatorChannel())
			.get();
	}

}
