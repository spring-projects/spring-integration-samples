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

For the [BaseX][] example to work, **please comment out the Saxon dependency in the `pom.xml` file!!** Otherwise, you may encounter the following exception:

	Caused by: javax.xml.xquery.XQException:
	  XQJTO013 - Values of 'standalone' and 'omit-xml-declaration' conflict
		at net.xqj.basex.bin.R.a(Unknown Source)
		at net.xqj.basex.bin.R.a(Unknown Source)
		at net.xqj.basex.bin.A.a(Unknown Source)
		at net.xqj.basex.bin.A.a(Unknown Source)
		at net.xqj.basex.bin.ar.a(Unknown Source)
		at net.xqj.basex.bin.ao.a(Unknown Source)
		at net.xqj.basex.bin.ao.a(Unknown Source)
		at net.xqj.basex.bin.v.bindNode(Unknown Source)
		at org.springframework.integration.xquery.XQueryExecutor.execute(XQueryExecutor.java:266)
		... 23 more
	Caused by: net.sf.saxon.trans.XPathException: Values of 'standalone' and 'omit-xml-declaration' conflict
		at net.sf.saxon.serialize.XMLEmitter.writeDeclaration(XMLEmitter.java:218)
		at net.sf.saxon.serialize.XMLEmitter.openDocument(XMLEmitter.java:151)
		at net.sf.saxon.serialize.XMLEmitter.startElement(XMLEmitter.java:296)
		at net.sf.saxon.event.NamespaceReducer.startElement(NamespaceReducer.java:66)
		at net.sf.saxon.dom.DOMSender.outputElement(DOMSender.java:195)
		at net.sf.saxon.dom.DOMSender.walkNode(DOMSender.java:149)
		at net.sf.saxon.dom.DOMSender.send(DOMSender.java:82)
		at net.sf.saxon.dom.DOMObjectModel.sendSource(DOMObjectModel.java:232)
		at net.sf.saxon.event.Sender.send(Sender.java:228)
		at net.sf.saxon.IdentityTransformer.transform(IdentityTransformer.java:39)
		... 32 more

Once commented out, run from the command line:

	$ mvn clean package -DskipTests=true

followed by:

	$ mvn exec:java

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