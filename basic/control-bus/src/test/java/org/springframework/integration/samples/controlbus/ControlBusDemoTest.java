/*
 * Copyright 2017-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.integration.samples.controlbus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.GenericMessage;

/**
 * @author Oleg Zhurakousky
 * @author Gary Russell
 * @author Glenn Renfro
 *
 */
public class ControlBusDemoTest {

	private static Log logger = LogFactory.getLog(ControlBusDemoTest.class);

	@Test
	public void demoControlBusWithJavaConfig() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
		ac.setEnvironment(new StandardEnvironment());
		ac.getEnvironment().setActiveProfiles("java-config");
		ac.scan("org.springframework.integration.samples.controlbus.config");
		ac.refresh();
		runControlBusDemo(ac);
		ac.close();
	}

	@Test
	public void demoControlBusWithXmlConfig() {
		ConfigurableApplicationContext ac = new ClassPathXmlApplicationContext(
				"/META-INF/spring/integration/ControlBusDemo-context.xml");
		runControlBusDemo(ac);
		ac.close();
	}

	private void runControlBusDemo(ConfigurableApplicationContext ac) {
		MessageChannel controlChannel = ac.getBean("controlChannel", MessageChannel.class);
		PollableChannel adapterOutputChannel = ac.getBean("adapterOutputChannel", PollableChannel.class);
		logger.info("Received before adapter started: " + adapterOutputChannel.receive(1000));
		controlChannel.send(new GenericMessage<String>("@inboundAdapter.start()"));
		logger.info("Received before adapter started: " + adapterOutputChannel.receive(1000));
		controlChannel.send(new GenericMessage<String>("@inboundAdapter.stop()"));
		logger.info("Received after adapter stopped: " + adapterOutputChannel.receive(1000));
	}
}
