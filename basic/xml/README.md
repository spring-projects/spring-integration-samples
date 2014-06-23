XML Sample
==========

This example demonstrates the following aspects of the [Extensible Markup Language][] (XML) support available with *Spring Integration*:

1. [XPath][] Splitter - to split an order with multiple items into several order messages for separate processing.
2. [XPath][] Router - to route messages according to the evaluation of an [XPath][] expression which tests to see if the order item is in stock.
3. [XPath][] Expression - which tests to see if the order item is in stock
3. [XSLT][] Transformer - to transform the payload of the order message into a resupply message where the order item is found to be out of stock.

## Running the Sample

To run the sample, execute the class **org.springframework.integration.samples.xml.BookOrderProcessingTestApp**. 
Alternatively, you can run the sample using [Gradle Application Plugin](http://www.gradle.org/docs/current/userguide/application_plugin.html) by executing:

    $ gradlew :xml:run

You should see the following output:

	INFO : org.springframework.integration.samples.xml.WarehouseDispatch - Warehouse dispatching orderItem: 
	<?xml version="1.0" encoding="UTF-8" standalone="no"?>
	<orderItem in-stock="true" xmlns="http://www.example.org/orders">
		<isbn>0321200683</isbn>
		<quantity>2</quantity>
	</orderItem>

	INFO : org.springframework.integration.samples.xml.ExternalResupply - Placing resupply order: 
	<?xml version="1.0" encoding="UTF-8" standalone="no"?>
	<bb:bigBooksOrder xmlns:bb="http://www.example.org/orders-bigbooks"
		xmlns:sb="http://www.example.org/orders">
		<bb:order>
			<bb:purchaser>smallbooks</bb:purchaser>
			<bb:quantity>5</bb:quantity>
			<bb:isbn>1590596439</bb:isbn>
		</bb:order>
	</bb:bigBooksOrder>
	
[Extensible Markup Language]: http://en.wikipedia.org/wiki/XML
[XPath]: http://en.wikipedia.org/wiki/XPath
[XSLT]: http://en.wikipedia.org/wiki/XSLT
