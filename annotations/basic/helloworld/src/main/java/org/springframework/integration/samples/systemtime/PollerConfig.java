package org.springframework.integration.samples.systemtime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.handler.LoggingHandler.Level;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.support.PeriodicTrigger;

/**
 * <int:inbound-channel-adapter expression=
 * "T(java.lang.System).currentTimeMillis()" channel="logger">
 * <int:poller fixed-delay="20000" max-messages-per-poll="2" />
 * </int:inbound-channel-adapter>
 * 
 * <int:logging-channel-adapter id="logger" logger-name=
 * "org.springframework.integration.samples.helloworld"/>
 * 
 * <task:executor id="executor" queue-capacity="20" pool-size="5-20"/>
 * 
 */
@Configuration
@EnableIntegration
@IntegrationComponentScan
public class PollerConfig {
	
	@Bean
	public MessageChannel logger() {
		return MessageChannels.direct().get();
	}

	@Bean(name = PollerMetadata.DEFAULT_POLLER)
	public PollerMetadata defaultPoller() {
		PollerMetadata pollerMetadata = new PollerMetadata();
		pollerMetadata.setTrigger(new PeriodicTrigger(20000));
		pollerMetadata.setMaxMessagesPerPoll(2);
		return pollerMetadata;
	}

	@Bean
	@InboundChannelAdapter(channel="logger", poller = @Poller(PollerMetadata.DEFAULT_POLLER))
	public MessageSource<Long> inbound() {
		MessageSource<Long> source = new MessageSource<Long>() {
			public Message<Long> receive() {
				return MessageBuilder.withPayload(System.currentTimeMillis()).build();
			}
		};
		return source;
	}

	@Bean
	public IntegrationFlow systemTimeFlow(MessageChannel logger) {
		LoggingHandler loggingHandler = new LoggingHandler(Level.INFO);
		loggingHandler.setLoggerName("org.springframework.integration.samples.systemtime");
		return IntegrationFlows
				.from(logger)
				.handle(loggingHandler)
				.get();
	}

}
