package org.springframework.integration.samples.testcontainersrabbitmq;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import({ Receiver.class, IntegrationConfigTest.Config.class })
class TestcontainersRabbitmqApplicationTests {

	@Test
	void contextLoads() {
	}

}
