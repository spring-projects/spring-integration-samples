This is the scripted implementation of the standard cafe sample application. You can choose among javascript,
groovy, ruby, and python scripting languages. The functionality is basically identical in all cases to the 
original cafe demo. 

Instructions for running the CafeDemo sample
-------------------------------------------------------------------------------
The script language is passed as a command line argument. This may be run directly from maven:
>mvn clean compile

>mvn exec:exec -Dlang=[language]

This sample also demonstrates the use of Spring Integration's groovy control bus which enables
control bus messages to be Groovy scripts which may invoke operations on application components.

To demonstrate the control bus, while the CafeDemoApp is running, execute in a separate window:

>mvn exec:exec -Pcontrol-bus

This will use groovy scripts to 
 - Query the waiter for the total number of orders delivered
 - If the total orders > 3, stop the inbound adaptor on the cafe (the order flow). The Cafe application
 will continue to run, but eventually the output will stop when all pending orders have completed. 

The Cafe sample emulates a simple operation of the Coffee shop shop when modeled using EIP
It is inspired by one of the samples featured in Gregor Hohpe's Ramblings.
The domain is that of a Cafe, and the basic flow is depicted in the following diagram:


                                                                                        Barista (scripted)
				                                                    hotDrinks        _______________________        
                                                                   	|==========| -->|                       |
                     	 orders                   drinks           /	            | prepareDrink()        |
Place Order ->Customer->|======|->OrderSplitter->|======|->DrinkRouter              |   timeToPrepare=5 sec |
                               (scripted)              (scripted)  \ coldDrinks     | prepareDrink()        |
                                                                    |==========| -->|   timeToPrepare=1 sec |
                                                                                    |_______________________|
										
													Legend: |====| - channels  
													
													                                                                       
The Order object may contain multiple OrderItems. Once the order is placed, a Splitter will break the
composite order message into a single message per drink. Each of these is then processed by a Router that
determines whether the drink is hot or cold (checking the OrderItem object's 'isIced' property). The Barista
prepares each drink. Hot and cold drink preparation are handled by two distinct script invocations of 
'prepareDrink', each with different variable assignments.  

The prepared drinks are then sent to the Waiter where they are aggregated into a Delivery object.

   			
Happy integration :-)