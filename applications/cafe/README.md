Cafe Sample Application
=======================

The Cafe sample emulates a simple operation of the Coffee shop when modeled using Enterprise Integration Patterns (EIP). It is inspired by one of the samples featured in Gregor Hohpe's Ramblings. The domain is that of a Cafe, and the basic flow is depicted in the following diagram:


	                                                                                          Barista
					                                                     hotDrinks       ____________________        
	                                                                    |==========| -->|                    |
	                     orders                   drinks               /                | prepareHotDrink()  |
	Place Order ->Cafe->|======|->OrderSplitter->|======|->DrinkRouter                  |                    |
	                                                                   \ coldDrinks     | prepareColdDrink() |
	                                                                    |==========| -->|                    |
	                                                                                    |____________________|
										
														Legend: |====| - channels  
													
                                                                       
The Order object may contain multiple OrderItems. Once the order is placed, a **Splitter** will break the composite order message into a single message per drink. Each of these is then processed by a **Router** that determines whether the drink is hot or cold (checking the OrderItem object's 'isIced' property). The Barista prepares each drink, but hot and cold drink preparation are handled by two distinct methods: 

* prepareHotDrink
* prepareColdDrink

The prepared drinks are then sent to the Waiter where they are aggregated into a Delivery object.

## Instructions for running the CafeDemo sample

1. The example comes with two identical configurations. One is ANNOTATION-based another is XML-based

2. To run this sample simply execute the CafeDemoApp test classes in the **org.springframework.integration.samples.cafe.xml** or  **org.springframework.integration.samples.cafe.annotation** package.

3. The example also provides an alternative configuration that uses AMQP channels to distribute the components in the **CafeDemo** sample. To run this alternative configuration of the sample, be sure to have a RabbitMQ broker started on localhost:5672 configured with the default guest|guest client credentials on the / vHost, then execute the following test classes in order:
   
   1. **cafeDemoAppBaristaColdAmqp** - starts the Cold Drink Barista
   2. **cafeDemoAppBaristaHotAmqp**  - starts the Hot Drink Barista
   3. **cafeDemoAppAmqp**            - starts the Cafe Storefront (Places 100 orders on the orders queue)
   4. **cafeDemoAppOperationsAmqp**  - starts the Cafe Operations (OrderSplitter, DrinkRouter, PreparedDrinkAggregator)
   
**Note**: All AMQP exchanges, queues, and bindings needed for this sample are defined within the different xml config files that support the above test classes.

4. You can also find an example of using JMS with Active MQ to distribute the components in the **CafeDemo** sample. To run this configuration, start an instance of ActiveMQ with the openwire/TCP connector available on the default port (61616). There are no credentials of which to be aware. Please execute the following classes in order:
   1. **CafeDemoAppBaristaColdActiveMQ - starts the ColdDrink Barista
   2. **CafeDemoAppBaristaHotActiveMQ  - starts the HotDrink Barista
   3. **CafeDemoAppOperationsActiveMQ  - starts the Cafe Operations (order splitter, drink router, etc).
   4. **CafeDemoAppAcitveMQ            - places the orders

5. See **CafeDemoActiveMQBackedChannels** for an example of how to use the JMS-backed channels. No need to start an external ActiveMQ because one is started internally
Upon running any of the alternatives, you should see the output similar to this:

	INFO : org.springframework.integration.samples.cafe.annotation.Barista - task-scheduler-1 prepared cold drink #1 for order #1: iced 3 shot MOCHA
	INFO : org.springframework.integration.samples.cafe.annotation.Barista - task-scheduler-1 prepared cold drink #2 for order #2: iced 3 shot MOCHA
	INFO : org.springframework.integration.samples.cafe.annotation.Barista - task-scheduler-1 prepared cold drink #3 for order #3: iced 3 shot MOCHA
	INFO : org.springframework.integration.samples.cafe.annotation.Barista - task-scheduler-1 prepared cold drink #4 for order #4: iced 3 shot MOCHA
	INFO : org.springframework.integration.samples.cafe.annotation.Barista - task-scheduler-2 prepared hot drink #1 for order #1: hot 2 shot LATTE
	-----------------------
	Order #1
	Iced MOCHA, 3 shots.
	Hot LATTE, 2 shots.
	-----------------------
	INFO : org.springframework.integration.samples.cafe.annotation.Barista - task-scheduler-1 prepared cold drink #5 for order #5: iced 3 shot MOCHA
	INFO : org.springframework.integration.samples.cafe.annotation.Barista - task-scheduler-1 prepared cold drink #6 for order #6: iced 3 shot MOCHA
	INFO : org.springframework.integration.samples.cafe.annotation.Barista - task-scheduler-1 prepared cold drink #7 for order #7: iced 3 shot MOCHA
	INFO : org.springframework.integration.samples.cafe.annotation.Barista - task-scheduler-1 prepared cold drink #8 for order #8: iced 3 shot MOCHA
	INFO : org.springframework.integration.samples.cafe.annotation.Barista - task-scheduler-1 prepared cold drink #9 for order #9: iced 3 shot MOCHA
	INFO : org.springframework.integration.samples.cafe.annotation.Barista - task-scheduler-2 prepared hot drink #2 for order #2: hot 2 shot LATTE
	-----------------------
	Order #2
	Iced MOCHA, 3 shots.
	Hot LATTE, 2 shots.
	-----------------------
   			
Happy integration :-)