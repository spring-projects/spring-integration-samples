Café Sample Application
=======================

## Overview

The *Café Sample* emulates the simple operation of a coffee shop that is modeled using *[Enterprise Integration Patterns][]* (EIP). The sample is inspired by one of the samples featured in [Gregor Hohpe][]'s ramblings. For more details, please see: *[Starbucks Does Not Use Two-Phase Commit][]*. The domain is that of a [café][], and the basic flow is depicted in the following diagram:


	                                                                                          Barista
	                                                                     hotDrinks       ____________________
	                                                                    |==========| -->|                    |
	                     orders                   drinks               /                | prepareHotDrink()  |
	Place Order ->Cafe->|======|->OrderSplitter->|======|->DrinkRouter                  |                    |
	                                                                   \ coldDrinks     | prepareColdDrink() |
	                                                                    |==========| -->|                    |
	                                                                                    |____________________|
	
	                                                Legend: |====| - channels
	
                                                                       
The *Order* object may contain multiple *OrderItems*. Once the order is placed, a **Splitter** will break the composite order message into a single message per drink. Each of these is then processed by a **Router** that determines whether the drink is hot or cold (checking the *OrderItem* object's 'isIced' property). The *Barista* prepares each drink, but hot and cold drink preparation are handled by two distinct methods: 

* prepareHotDrink
* prepareColdDrink

The prepared drinks are then sent to the *Waiter* where they are aggregated into a *Delivery* object.

## Café Sample Implementations

There are currently three implementations of the *Café Sample* that use:

1. Spring Integration channels and components
2. [AMQP][] ([RabbitMQ][]) to demonstrate a distributed architecture
3. [JMS][] ([ActiveMQ][]) to demonstrate JMS-backed queues and a distributed architecture

All three implementations follow the same flow described above. See each one's **README.md** file for more details regarding the respective implementations.

Upon running any of the alternatives, you should see the output similar to this:

	INFO : Barista - task-scheduler-1 prepared cold drink #1 for order #1: iced 3 shot MOCHA
	INFO : Barista - task-scheduler-1 prepared cold drink #2 for order #2: iced 3 shot MOCHA
	INFO : Barista - task-scheduler-1 prepared cold drink #3 for order #3: iced 3 shot MOCHA
	INFO : Barista - task-scheduler-1 prepared cold drink #4 for order #4: iced 3 shot MOCHA
	INFO : Barista - task-scheduler-2 prepared hot drink #1 for order #1: hot 2 shot LATTE
	-----------------------
	Order #1
	Iced MOCHA, 3 shots.
	Hot LATTE, 2 shots.
	-----------------------
	INFO : Barista - task-scheduler-1 prepared cold drink #5 for order #5: iced 3 shot MOCHA
	INFO : Barista - task-scheduler-1 prepared cold drink #6 for order #6: iced 3 shot MOCHA
	INFO : Barista - task-scheduler-1 prepared cold drink #7 for order #7: iced 3 shot MOCHA
	INFO : Barista - task-scheduler-1 prepared cold drink #8 for order #8: iced 3 shot MOCHA
	INFO : Barista - task-scheduler-1 prepared cold drink #9 for order #9: iced 3 shot MOCHA
	INFO : Barista - task-scheduler-2 prepared hot drink #2 for order #2: hot 2 shot LATTE
	-----------------------
	Order #2
	Iced MOCHA, 3 shots.
	Hot LATTE, 2 shots.
   			
Happy integration :-)

[ActiveMQ]: http://activemq.apache.org/
[AMQP]: http://en.wikipedia.org/wiki/Advanced_Message_Queuing_Protocol
[café]: http://en.wikipedia.org/wiki/Caf%C3%A9
[Enterprise Integration Patterns]: http://www.eaipatterns.com/eaipatterns.html
[Gregor Hohpe]: http://www.eaipatterns.com/gregor.html
[JMS]: http://en.wikipedia.org/wiki/Java_Message_Service
[RabbitMQ]: http://www.rabbitmq.com/
[Starbucks Does Not Use Two-Phase Commit]: http://www.eaipatterns.com/ramblings/18_starbucks.html
