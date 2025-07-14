package org.springframework.integration.samples.si4demo;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.samples.si4demo.annotations.Application;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class ApplicationTests {

	@Test
	public void contextLoads() {
	}

}
