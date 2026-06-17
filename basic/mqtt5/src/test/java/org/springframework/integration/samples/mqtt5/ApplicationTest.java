package org.springframework.integration.samples.mqtt5;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.test.context.MockIntegrationContext;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.integration.test.mock.MockIntegration.messageArgumentCaptor;
import static org.springframework.integration.test.mock.MockIntegration.mockMessageHandler;

@SpringBootTest
@SpringIntegrationTest
@Testcontainers
public class ApplicationTest {

	@Container
	static final GenericContainer<?> MOSQUITTO =
			new GenericContainer<>(DockerImageName.parse("eclipse-mosquitto:2.0"))
					.withExposedPorts(1883)
					.withCommand("mosquitto -c /mosquitto-no-auth.conf");

	@DynamicPropertySource
	static void mqttProperties(DynamicPropertyRegistry registry) {
		registry.add("mqtt.broker.host", MOSQUITTO::getHost);
		registry.add("mqtt.broker.port", () -> MOSQUITTO.getMappedPort(1883));
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

		assertTrue(receiveLatch.await(10, TimeUnit.SECONDS));
		verify(mockMessageHandler).handleMessage(any());
		assertEquals("foo sent to MQTT5, received from MQTT5", captor.getValue().getPayload());
	}
}
