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
package org.springframework.integration.samples.mongodb.outbound;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.samples.mongodb.domain.Address;
import org.springframework.integration.samples.mongodb.domain.Person;
import org.springframework.integration.samples.mongodb.util.DemoUtils;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
/**
*
* @author Oleg Zhurakousky
* @author Gary Russell
*/
public class MongoDbOutboundAdapterDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		DemoUtils.prepareMongoFactory(); // will clean up MOngoDb
		new MongoDbOutboundAdapterDemo().runDefaultAdapter();
//		new MongoDbOutboundAdapterDemo().runAdapterWithConveter();
	}

	public void runDefaultAdapter() throws Exception {

		@SuppressWarnings("resource")
		ClassPathXmlApplicationContext context =
				new ClassPathXmlApplicationContext("mongodb-out-config.xml", MongoDbOutboundAdapterDemo.class);

		MessageChannel messageChannel = context.getBean("deafultAdapter", MessageChannel.class);
		messageChannel.send(new GenericMessage<Person>(this.createPersonA()));
		messageChannel.send(new GenericMessage<Person>(this.createPersonB()));
		messageChannel.send(new GenericMessage<Person>(this.createPersonC()));
	}

	public void runAdapterWithConveter() throws Exception {

		@SuppressWarnings("resource")
		ClassPathXmlApplicationContext context =
				new ClassPathXmlApplicationContext("mongodb-out-config.xml", MongoDbOutboundAdapterDemo.class);

		MessageChannel messageChannel = context.getBean("adapterWithConverter", MessageChannel.class);
		messageChannel.send(new GenericMessage<String>("John, Dow, Palo Alto, 3401 Hillview Ave, 94304, CA"));
	}

	private Person createPersonA(){
		Address address = new Address();
		address.setCity("Palo Alto");
		address.setStreet("3401 Hillview Ave");
		address.setZip("94304");
		address.setState("CA");

		Person person = new Person();
		person.setFname("John");
		person.setLname("Doe");
		person.setAddress(address);

		return person;
	}

	private Person createPersonB(){
		Address address = new Address();
		address.setCity("San Francisco");
		address.setStreet("123 Main st");
		address.setZip("94115");
		address.setState("CA");

		Person person = new Person();
		person.setFname("Josh");
		person.setLname("Doe");
		person.setAddress(address);

		return person;
	}

	private Person createPersonC(){
		Address address = new Address();
		address.setCity("Philadelphia");
		address.setStreet("2323 Market st");
		address.setZip("19152");
		address.setState("PA");

		Person person = new Person();
		person.setFname("Jane");
		person.setLname("Doe");
		person.setAddress(address);

		return person;
	}
}
