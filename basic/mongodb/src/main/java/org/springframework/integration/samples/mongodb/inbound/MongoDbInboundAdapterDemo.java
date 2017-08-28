/*
 * Copyright 2002-2017 the original author or authors.
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
package org.springframework.integration.samples.mongodb.inbound;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.samples.mongodb.outbound.MongoDbOutboundAdapterDemo;
import org.springframework.integration.samples.mongodb.util.DemoUtils;

/**
 *
 * @author Oleg Zhurakousky
 * @author Gary Russell
 */
public class MongoDbInboundAdapterDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		DemoUtils.prepareMongoFactory(); // will clean up MongoDb
		new MongoDbOutboundAdapterDemo().runDefaultAdapter(); // will load data into MongoDb

		new MongoDbInboundAdapterDemo().runDefaultAdapter();
	}

	public void runDefaultAdapter() throws Exception {

		ClassPathXmlApplicationContext context =
				new ClassPathXmlApplicationContext("mongodb-in-config.xml", MongoDbInboundAdapterDemo.class);

		Thread.sleep(3000);
		context.close();
	}
}
