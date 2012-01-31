package org.springframework.integration.samples.cafe.xml;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: ceposta
 * Date: 1/27/12
 * Time: 7:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class CafeDemoAppOperationsActiveMQ {


	public static void main(String[] args) throws InterruptedException, IOException {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/spring/integration/activemq/cafeDemo-amq-config.xml",
                                                                                "/META-INF/spring/integration/activemq/cafeDemo-amq-operations.xml");

        System.in.read();
        context.close();
    }
}
