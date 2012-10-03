Café Sample Application - Pure SI Implementation
================================================

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


## Instructions for running the CafeDemo sample

The example comes with two identical configurations. One is ANNOTATION-based another is XML-based

To run this sample simply execute the CafeDemoApp classes in the **org.springframework.integration.samples.cafe.xml** or  **org.springframework.integration.samples.cafe.annotation** package.

See the configuration files in the **META-INF/spring/integration** path.

