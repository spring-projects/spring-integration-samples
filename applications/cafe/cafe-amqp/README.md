Café Sample Application - AMQP Implementation
=============================================

See the parent-level README.md for more details, but the flow of the implementation should follow this diagram:


	                                                                                          Barista
	                                                                     hotDrinks       ____________________
	                                                                    |==========| -->|                    |
	                     orders                   drinks               /                | prepareHotDrink()  |
	Place Order ->Cafe->|======|->OrderSplitter->|======|->DrinkRouter                  |                    |
	                                                                   \ coldDrinks     | prepareColdDrink() |
	                                                                    |==========| -->|                    |
	                                                                                    |____________________|

	                                                Legend: |====| - channels


## Instructions for running the CafeDemo AMQP sample

### Distributed components
To run this alternative configuration of the sample, be sure to have a RabbitMQ broker started on localhost:5672 configured with the default guest|guest client credentials on the / vHost, then execute the following test classes in order:

   1. **cafeDemoAppBaristaColdAmqp** - starts the Cold Drink Barista
   2. **cafeDemoAppBaristaHotAmqp**  - starts the Hot Drink Barista
   3. **cafeDemoAppAmqp**            - starts the Cafe Storefront (Places 100 orders on the orders queue)
   4. **cafeDemoAppOperationsAmqp**  - starts the Cafe Operations (OrderSplitter, DrinkRouter, PreparedDrinkAggregator)

**Note**: All AMQP exchanges, queues, and bindings needed for this sample are defined within the different xml config files that support the above test classes.
