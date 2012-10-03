Café Sample Application - JMS Implementation
============================================

See the parent-level **README.md** for more details, but the flow of the implementation should follow this diagram:


	                                                                                          Barista
	                                                                     hotDrinks       ____________________
	                                                                    |==========| -->|                    |
	                     orders                   drinks               /                | prepareHotDrink()  |
	Place Order ->Cafe->|======|->OrderSplitter->|======|->DrinkRouter                  |                    |
	                                                                   \ coldDrinks     | prepareColdDrink() |
	                                                                    |==========| -->|                    |
	                                                                                    |____________________|
	
	                                                Legend: |====| - channels


## Instructions for running the CafeDemo JMS sample

### Distributed components
To run this configuration, start an instance of ActiveMQ with the openwire/TCP connector available on the default port (61616). There are no credentials of which to be aware. Please execute the following classes in order:

   1. **CafeDemoAppBaristaColdActiveMQ - starts the ColdDrink Barista
   2. **CafeDemoAppBaristaHotActiveMQ  - starts the HotDrink Barista
   3. **CafeDemoAppOperationsActiveMQ  - starts the Cafe Operations (order splitter, drink router, etc).
   4. **CafeDemoAppAcitveMQ            - places the orders

### JMS backed components

See **CafeDemoActiveMQBackedChannels** for an example of how to use the JMS-backed channels. No need to start an external ActiveMQ because one is started internally