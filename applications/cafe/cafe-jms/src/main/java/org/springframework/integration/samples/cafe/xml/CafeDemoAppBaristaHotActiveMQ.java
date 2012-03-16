package org.springframework.integration.samples.cafe.xml;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Main class for starting up the distributed task of handling/creating the Hot¡
 * beverages. See the README.md file for more information on the order in which
 * to start the processes
 *
 * @author ceposta
 */
public class CafeDemoAppBaristaHotActiveMQ {


	public static void main(String[] args) throws InterruptedException, IOException {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/spring/integration/activemq/cafeDemo-amq-config.xml",
                                                                                "/META-INF/spring/integration/activemq/cafeDemo-amq-baristaHot-xml.xml");

        System.out.println("Press Enter/Return to exit");
        System.in.read();
        context.close();
    }
}
