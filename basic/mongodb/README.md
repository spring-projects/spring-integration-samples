Spring Integration - MongoDb Sample
================================

### MongoDb Outbound Channel Adapter

The *MongoDb Outbound Channel Adapter* allows you to write the Message payload to a MongoDb document store. You can see the simplest configuration of the MongoDb Channel Adapter below:

    <int-mongodb:outbound-channel-adapter id="deafultAdapter"/>

It will be initialized with the default instance of the **MongoDbFactory** (the default bean name is 'mongoDbFactory') otherwise you can provide its 
reference via **mongodb-factory** attribute.

Now you can run the **MongoDbOutboundAdapterDemo**. After running it open terminal window and start MongoDb CLI

    $ mongo

Once in MongoDb CLI type a query:

    > db.data.find({})

And you should see the 3 document entries we just created:

    { "_id" : ObjectId("505ff83d03649ed6881d066b"), "_class" : "org.springframework.integration.samples.mongodb.domain.Person", "fname" : "John", "lname" : "Doe", "address" : { "street" : "3401 Hillview Ave", "city" : "Palo Alto", "zip" : "94304", "state" : "CA" } }
    { "_id" : ObjectId("505ff83d03649ed6881d066c"), "_class" : "org.springframework.integration.samples.mongodb.domain.Person", "fname" : "Josh", "lname" : "Doe", "address" : { "street" : "123 Main st", "city" : "San Francisco", "zip" : "94115", "state" : "CA" } }
    { "_id" : ObjectId("505ff83d03649ed6881d066d"), "_class" : "org.springframework.integration.samples.mongodb.domain.Person", "fname" : "Jane", "lname" : "Doe", "address" : { "street" : "2323 Market st", "city" : "Philadelphia", "zip" : "19152", "state" : "PA" } }

Note that our entries were represented by a very simple Java Beans style objects which were converted into JSON string using default converters. Let's try to complicate the issue slightly. We are going to try to store a simple coma delimited String in our **MongoDbOutboundAdapterDemo().runSimpleComplexAdapter()** demo:

    messageChannel.send(new GenericMessage<String>("John Dow, Palo Alto, 3401 Hillview Ave, 94304, CA"));

As you can see we are sending a Message with the String payload which represents a person. Executing this code will result in:

    Caused by: java.lang.IllegalArgumentException: can't serialize class java.lang.Character

That is because MongoDb attempts to convert the underlying string using default set of converters that are based on Java Beans convention. 
In any case that is not what we are looking for in this case anyway since we want to store this object as a document with each field represented individually so we need to parse this string into a set of fields first. To do so we will apply a custom **MongoConverter**. Modify the sample configuration of **simpleAdapterWithConverter** to add a reference to an already configured converter.

	<int-mongodb:outbound-channel-adapter id="adapterWithConverter"
				mongo-converter="stringConverter"/>
				
Run the demo again and you'll see that it will succeed this time and you should see the stored object as:

	{ "_id" : ObjectId("505ffeac0364f8a92337657e"), "fname" : "John", "lname" : "Dow", "address" : { "city" : "Palo Alto", "street" : "3401 Hillview Ave", "zip" : "94304", "state" : "CA" } }

This time the conversion is done using a provided converter and you can see in the StringConverter.write(..) method that all that we are doing is parsing the input String and adding its data as an individual fields to an instance of the DBObject.

For even more complex scenarios you can also bootstrap the adapter with the custom instance of the **MongoTemplate**, but its out of scope of this blog.

### MongoDb Inbound Channel Adapter<

MongoDb Inbound Channel Adapter allows you to read documents from the MongoDb and send them as Message payloads downstream. This simple example shows you how to read the documents stored in the MongoDb. For this example we'll use data stored by the MongoDb Outbound Channel Adapter discussed in the previous section.
As you can see from the example below, the configuration of MongoDb Inbound Channel Adapter is very similar to any other polling Inbound Channel Adapter.

	<int-mongodb:inbound-channel-adapter id="simpleInboundAdapter" channel="splittingChannel" 
									     query="{address.state : 'CA'}">
		<int:poller fixed-rate="60000" max-messages-per-poll="1"/>
	</int-mongodb:inbound-channel-adapter>

Notice the **query** attribute which allows you to provide JSON queries represented as simple String. 	For more information on MongoDb queries please refer to [MongoDb documentation](http://www.mongodb.org/display/DOCS/Querying)
In the above case we are selecting all documents where *state* element of the *address* element is **'CA'**. As you may have guessed the MongoDb Inbound Channel Adapter returns List by default, so you can easily configure a very basic splitter downstream (as in this example)	to process one message at the time. Run the **MongoDbInboundAdapterDemo** and you should see the results in the console:

	04:37:30.720 WARN  . . . { "_id" : { "$oid" : "50601bca0364063859066bcd"} , "_class" : "org.springframework.integration.samples.mongodb.domain.Person" , "fname" : "John" , "lname" : "Doe" , "address" : { "street" : "3401 Hillview Ave" , "city" : "Palo Alto" , "zip" : "94304" , "state" : "CA"}}
	04:37:30.722 WARN  . . . { "_id" : { "$oid" : "50601bca0364063859066bce"} , "_class" : "org.springframework.integration.samples.mongodb.domain.Person" , "fname" : "Josh" , "lname" : "Doe" , "address" : { "street" : "123 Main st" , "city" : "San Francisco" , "zip" : "94115" , "state" : "CA"}}

However, if you know that your query can only return a single result you can avoid returning List by configuring **expect-single-result** attribute setting its value to *true*.

Also, you may wish to do some post-processing to the successfully processed data that was read from the MongoDb. For example; you may want to move or remove a document after its been processed.
You can do this using Transaction Synchronization feature that was added with *Spring Integration 2.2* and which will be discussed in the next blog in this series expected in a few days (from Gary Russell). However the impatient once can get more details now by reading MongoDb Inbound Channel Adapter section of the reference manual [http://static.springsource.org/spring-integration/docs/2.2.0.RC3/reference/htmlsingle/#mongodb-inbound-channel-adapter](http://static.springsource.org/spring-integration/docs/2.2.0.RC3/reference/htmlsingle/#mongodb-inbound-channel-adapter).
