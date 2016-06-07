/*
 * Copyright 2014 the original author or authors.
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

package org.springframework.integration.samples.websocket.standard.server;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.Executors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.splitter.DefaultMessageSplitter;
import org.springframework.integration.transformer.AbstractPayloadTransformer;
import org.springframework.integration.transformer.HeaderEnricher;
import org.springframework.integration.transformer.support.ExpressionEvaluatingHeaderValueMessageProcessor;
import org.springframework.integration.websocket.ServerWebSocketContainer;
import org.springframework.integration.websocket.outbound.WebSocketOutboundMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;

/**
 * @author Artem Bilan
 * @since 3.0
 */
@Configuration
@EnableAutoConfiguration
public class Application {

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
		System.out.println("Hit 'Enter' to terminate");
		System.in.read();
		ctx.close();
	}

	@Bean
	public ServerWebSocketContainer serverWebSocketContainer() {
		return new ServerWebSocketContainer("/time").withSockJs();
	}

	@Bean
	@InboundChannelAdapter(value = "splitChannel", poller = @Poller(fixedDelay = "1000", maxMessagesPerPoll = "1"))
	public MessageSource<?> webSocketSessionsMessageSource() {
		return new MessageSource<Iterator<String>>() {

			@Override
			public Message<Iterator<String>> receive() {
				return new GenericMessage<Iterator<String>>(serverWebSocketContainer().getSessions().keySet().iterator());
			}

		};
	}

	@Bean
	public MessageChannel splitChannel() {
		return new DirectChannel();
	}

	@Bean
	@ServiceActivator(inputChannel = "splitChannel")
	public MessageHandler splitter() {
		DefaultMessageSplitter splitter = new DefaultMessageSplitter();
		splitter.setOutputChannelName("headerEnricherChannel");
		return splitter;
	}

	@Bean
	public MessageChannel headerEnricherChannel() {
		return new ExecutorChannel(Executors.newCachedThreadPool());
	}

	@Bean
	@Transformer(inputChannel = "headerEnricherChannel", outputChannel = "transformChannel")
	public HeaderEnricher headerEnricher() {
		return new HeaderEnricher(Collections.singletonMap(SimpMessageHeaderAccessor.SESSION_ID_HEADER,
				new ExpressionEvaluatingHeaderValueMessageProcessor<Object>("payload", null)));
	}

	@Bean
	@Transformer(inputChannel = "transformChannel", outputChannel = "sendTimeChannel")
	public AbstractPayloadTransformer<?, ?> transformer() {
		return new AbstractPayloadTransformer<Object, Object>() {
			@Override
			protected Object transformPayload(Object payload) throws Exception {
				return DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.DEFAULT).format(new Date());
			}

		};
	}


	@Bean
	public MessageChannel sendTimeChannel() {
		return new PublishSubscribeChannel();
	}


	@Bean
	@ServiceActivator(inputChannel = "sendTimeChannel")
	public MessageHandler webSocketOutboundAdapter() {
		return new WebSocketOutboundMessageHandler(serverWebSocketContainer());
	}

	@Bean
	@ServiceActivator(inputChannel = "sendTimeChannel")
	public MessageHandler loggingChannelAdapter() {
		LoggingHandler loggingHandler = new LoggingHandler("info");
		loggingHandler.setLogExpressionString(
				"'The time ' + payload + ' has been sent to the WebSocketSession ' + headers.simpSessionId");
		return loggingHandler;
	}

}
