package org.springframework.integration.samples.cafe.xml;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Main class for starting up the distributed task of processing orders including
 * splitting the orders, sending them to the hot/cold baristas, aggregating the orders
 * and delivering them to the waiters.
 *
 * See the README.md file for more information on the order in which
 * to start the processes
 *
 * @author ceposta
 */
public class CafeDemoAppOperationsActiveMQ {


	public static void main(String[] args) throws InterruptedException, IOException {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/spring/integration/activemq/cafeDemo-amq-config.xml",
                                                                                "/META-INF/spring/integration/activemq/cafeDemo-amq-operations.xml");

        System.out.println("Press Enter/Return to exit");
        System.in.read();
        context.close();
    }
}
