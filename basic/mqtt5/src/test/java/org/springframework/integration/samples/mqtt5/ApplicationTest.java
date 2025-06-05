package org.springframework.integration.samples.mqtt5;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.integration.test.mock.MockIntegration.messageArgumentCaptor;
import static org.springframework.integration.test.mock.MockIntegration.mockMessageHandler;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.test.context.MockIntegrationContext;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@SpringIntegrationTest
public class ApplicationTest {

	@BeforeAll
	static void setupBroker() {
		BrokerRunning brokerRunning = BrokerRunning.isRunning(1883);
	}

	@Autowired
	private MockIntegrationContext mockIntegrationContext;

	@Autowired
	private IntegrationFlow mqttOutFlow;

	@Test
	void testMqttFlow() throws InterruptedException {
		ArgumentCaptor<Message<?>> captor = messageArgumentCaptor();
		CountDownLatch receiveLatch = new CountDownLatch(1);
		MessageHandler mockMessageHandler = mockMessageHandler(captor).handleNext(m -> receiveLatch.countDown());

		mockIntegrationContext.substituteMessageHandlerFor(
				"mqttInFlow.org.springframework.integration.config.ConsumerEndpointFactoryBean#1",
				mockMessageHandler);

		mqttOutFlow.getInputChannel().send(new GenericMessage<>("foo"));

		assertThat(receiveLatch.await(10, TimeUnit.SECONDS)).isTrue();
		verify(mockMessageHandler).handleMessage(any());
		assertThat(captor.getValue().getPayload())
				.isEqualTo("foo sent to MQTT5, received from MQTT5");
	}
}
