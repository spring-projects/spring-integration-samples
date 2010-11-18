package org.springframework.integration.samples.helloworld;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DelayerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		new ClassPathXmlApplicationContext("META-INF/spring/integration/delay.xml");
		System.in.read();
	}

}
