package org.springframework.integration.samples.tcpasyncbi;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.channel.AbstractMessageChannel;
import org.springframework.integration.endpoint.SourcePollingChannelAdapter;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

@SpringBootTest
@SpringIntegrationTest(noAutoStartup = "clientOut.org.springframework.integration.config.SourcePollingChannelAdapterFactoryBean#0")
class TcpAsyncBiDirectionalApplicationTests {

	@Autowired
	@Qualifier("clientOut.org.springframework.integration.config.SourcePollingChannelAdapterFactoryBean#0")
	private SourcePollingChannelAdapter adapter;

	@Autowired
	@Qualifier("clientIn.channel#0")
	private AbstractMessageChannel clientIn;

	@Autowired
	@Qualifier("serverIn.channel#0")
	private AbstractMessageChannel serverIn;

	@Test
	void testBothReceive() throws InterruptedException {
		CountDownLatch serverLatch = new CountDownLatch(1);
		CountDownLatch clientLatch = new CountDownLatch(1);
		this.serverIn.addInterceptor(new ChannelInterceptor() {

			@Override
			@Nullable
			public Message<?> preSend(Message<?> message, MessageChannel channel) {
				serverLatch.countDown();
				return message;
			}

		});
		this.clientIn.addInterceptor(new ChannelInterceptor() {

			@Override
			@Nullable
			public Message<?> preSend(Message<?> message, MessageChannel channel) {
				clientLatch.countDown();
				return message;
			}

		});
		this.adapter.start();
		assertThat(serverLatch.await(10, TimeUnit.SECONDS)).isTrue();
		assertThat(clientLatch.await(10, TimeUnit.SECONDS)).isTrue();
	}

}
