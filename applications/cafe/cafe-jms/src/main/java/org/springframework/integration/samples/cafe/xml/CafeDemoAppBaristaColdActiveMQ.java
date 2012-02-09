package org.springframework.integration.samples.cafe.xml;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.samples.cafe.Cafe;
import org.springframework.integration.samples.cafe.DrinkType;
import org.springframework.integration.samples.cafe.Order;

import java.io.IOException;

/**
 * Main class for starting up the distributed task of handling/creating the Cold
 * beverages. See the README.md file for more information on the order in which
 * to start the processes
 *
 * @author ceposta
 */
public class CafeDemoAppBaristaColdActiveMQ {


	public static void main(String[] args) throws InterruptedException, IOException {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/spring/integration/activemq/cafeDemo-amq-config.xml",
                                                                                "/META-INF/spring/integration/activemq/cafeDemo-amq-baristaCold-xml.xml");

        System.out.println("Press Enter/Return to exit");
        System.in.read();
        context.close();
    }
}
