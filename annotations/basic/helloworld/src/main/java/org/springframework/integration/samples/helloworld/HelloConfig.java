package org.springframework.integration.samples.helloworld;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;

@Configuration
@EnableIntegration
@IntegrationComponentScan
@ComponentScan
public class HelloConfig {

	@Bean
	public PollableChannel outputChannel() {
		return MessageChannels.queue(10).get();
	}

	@Bean
	public MessageChannel inputChannel() {
		return MessageChannels.direct().get();
	}

	@Bean
	public IntegrationFlow helloFlow(final HelloService helloService) {
		return IntegrationFlows.from(inputChannel()).handle(new GenericHandler<String>() {
			public Object handle(String arg0, Map<String, Object> arg1) {
				return helloService.sayHello(arg0);
			}
		}).channel(outputChannel()).get();
	}

}
