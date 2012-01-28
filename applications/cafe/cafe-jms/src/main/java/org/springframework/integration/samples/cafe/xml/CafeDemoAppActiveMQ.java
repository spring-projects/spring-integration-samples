package org.springframework.integration.samples.cafe.xml;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.samples.cafe.Cafe;
import org.springframework.integration.samples.cafe.DrinkType;
import org.springframework.integration.samples.cafe.Order;

import java.io.IOException;

/**
 * Main class for sending orders that will be handled in separate, distributed
 * processes. See the README.md file for more information on the order in which
 * to start the processes
 *
 * @author ceposta
 */
public class CafeDemoAppActiveMQ {

	/**
	 * place some orders
	 * @param context spring context
	 * @param count the number of standard orders
	 */
	public static void order(AbstractApplicationContext context, int count){
		Cafe cafe = (Cafe) context.getBean("cafe");
		for (int i = 1; i <= count; i++) {
			Order order = new Order(i);
			order.addItem(DrinkType.LATTE, 2, false);
			order.addItem(DrinkType.MOCHA, 3, true);
			cafe.placeOrder(order);
		}
	}

	public static void main(String[] args) throws InterruptedException, IOException {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/spring/integration/activemq/cafeDemo-amq-config.xml",
                                                                                "/META-INF/spring/integration/activemq/cafeDemo-amq-xml.xml");
        order(context, 25);
        context.close();
    }
}
