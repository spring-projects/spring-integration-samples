Spring Integration - XQuery Sample
==================================

## Overview

This sample executes a simple [XQuery][] script. It uses the *[Spring Integration XQuery][]* module, which is part of the [Spring Integration Extensions][] project. The sample uses one of the following 3 XQuery processors:

* [Saxon][]
* [Sedna][]
* [BaseX][]

The example for [Saxon][] will work out of the box. [Sedna][] and [BaseX][] are in fact [XML Databases][], which run as external processes. For [Sedna][]- and [BaseX][]-specific setup instructions, please visit:

* http://xqj.net/sedna/
* http://xqj.net/basex/

## Running the Sample

From the command line execute:

	$ mvn clean package

followed by:

	$ mvn exec:java

The application should start up and you will see the following screen on which you can choose which XQuery processor to use:

	=========================================================

	    Welcome to the Spring Integration XQuery Sample!

	    For more information please visit:
	    http://www.springintegration.org/

	=========================================================
	Which XQuery Processor would you like to use? <enter>:
		1. Use Saxon
		2. Use Sedna
		3. Use BaseX
		q. Quit the application
	Enter you choice:

### Note regarding BaseX

The [BaseX][] dependency conflicts with [Sedna][]. Therefore, this sample applies a separate Maven profile for [BaseX][]. If not triggering that special profile when you try to execute the [BaseX][] example, you may encounter the following error message:

	Detected the Sedna library to be present. This conflicts with BaseX. Please start the application from the command line using:

	mvn exec:java -Dbasex

Consequently, execute from the command line:

	$ mvn exec:java -Dbasex

The [BaseX][] sample will now execute successfully.

## Details

The used XQuery Script, located under `src/main/resources/data/xquery.xql`, is quite simple:

	<customers>
	 { //customers/customer/name }
	</customers>

All it does is extracting the customer names from the following XML document:

	<customers>
		<customer id="1">
			<name>Foo Industries</name>
			<industry>Chemical</industry>
			<city>Glowing City</city>
		</customer>
		<customer id="2">
			<name>Bar Refreshments</name>
			<industry>Beverage</industry>
			<city>Desert Town</city>
		</customer>
		<customer id="3">
			<name>Hello World Services</name>
			<industry>Travel</industry>
			<city>Coral Sands</city>
		</customer>
	</customers>


The XML document is located at `src/main/resources/data/customers.xml`.

The resulting XML document should look like:

	<customers>
	   <name>Foo Industries</name>
	   <name>Bar Refreshments</name>
	   <name>Hello World Services</name>
	</customers>

[Saxon]: http://saxon.sourceforge.net/
[Sedna]: http://www.sedna.org/
[BaseX]: http://basex.org/
[Spring Integration XQuery]: https://github.com/SpringSource/spring-integration-extensions/tree/master/spring-integration-xquery
[Spring Integration Extensions]: https://github.com/SpringSource/spring-integration-extensions
[XML Databases]: http://en.wikipedia.org/wiki/XML_database
[XQuery]: http://en.wikipedia.org/wiki/XQuery