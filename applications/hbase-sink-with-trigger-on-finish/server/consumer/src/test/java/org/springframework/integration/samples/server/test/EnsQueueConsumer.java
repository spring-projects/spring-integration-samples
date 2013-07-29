/*
 * Copyright 2002-2010 the original author or authors.
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

package org.springframework.integration.samples.server.test;

import org.junit.After;
import org.junit.Test;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.samples.server.workers.QueueWorker;

/**
 * Server application than can be run as an app or unit test.
 * 
 * @author Mark Pollack
 */
public class EnsQueueConsumer {

	private ClassPathXmlApplicationContext context;

	public static void main(String[] args) {
		new EnsQueueConsumer().run();
	}

	@After
	public void close() {
		if (context != null) {
			context.close();
		}
	}

	

	@Test
	public void run() {
		context = new ClassPathXmlApplicationContext("mysource-consumer/bootstrap-config.xml");
		configMySourceWorker();
//		configOtherSourceWorker();
//		LOG.info("Queue consumer mysource isRunning(): " + context.isRunning());
	}


//	private void configOtherSourceWorker() {
//		// config some other Worker
//		try {
//			QueueWorker w = (QueueWorker) context
//					.getBean("mysource2ServerWorker");
//			AbstractMessageListenerContainer listCont = (AbstractMessageListenerContainer) context
//					.getBean("mysource2ListenerContainer");
//			w.setListenerContainer(listCont);
//		} catch (NoSuchBeanDefinitionException ex) {
//			LOG.warn("mysource2ServerWorker consumer not declared");
//		}
//	}

	private void configMySourceWorker() {
		// config Mysource Worker
		try {
			QueueWorker w = (QueueWorker) context.getBean("mysourceServerWorker");
			AbstractMessageListenerContainer listCont = (AbstractMessageListenerContainer) context
					.getBean("mysourceListenerContainer");
			w.setListenerContainer(listCont);
		} catch (NoSuchBeanDefinitionException ex) {
//			LOG.warn("Mysource consumer not declared");
		}
	}

}
