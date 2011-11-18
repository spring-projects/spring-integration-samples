The project contains sample JDBC adapters using Spring integration
JDBC Adapters are used to connectthe database systems from Spring integration application 


###########################Setting up derby###########################

If you intend to use some other database, skip this section and goto outbound channel adapters section 

1. Goto http://db.apache.org/derby/derby_downloads.html and download the latest version of derby DB.
When this sample was developed 10.8.2.2 was the latest version. If you however choose to use 
a newer or older version, update the property value for property "derbyclient.driver.version" in pom.xml
to the right version you are using.

2. Extract the downloaded jar file

3. Go to folder bin in the extracted derby folder and execute startNetworkServer (.bat for windows)
This should start the Derby server. By default it starts listening on port 1527

4. The simple in built client "ij" can be used to create the required table in derby.

5. Execute ij (.bat for windows)

6. run the command "connect 'jdbc:derby://localhost:1527/TestDatabase;create=true';" for first time
subequently use "connect 'jdbc:derby://localhost:1527/TestDatabase';" 

7. Execute the create table script from the file Person_Derby.sql

The above steps should setup derby for use. 
You can use ij to execute the select commands to query the table data.


We have the following examples in this particular Project

###########################Outbound Channel Adapter######################################

This adapter is used to insert/update records in a table based on the values contained
in the message payload that is received over the channel to the adapter.

The following are necessary for configuring the adapter
1. The datasource or the JDBC template
2. The the update/insert query to be executed.
3. Optional SQL Parameter source factory

In our config file "jdbcOutboundAdapterConfig.xml", we have defined an outbound-channel-adapter
for the datasource of derby database. Some instructions given to setup derby database.
You are free to use any other databae, however you need to appropriately change the datasource
details defined in the config

The adapter does the following.
1. It creates a message with payload of type org.springframework.integration.samples.jdbc.Person
2. This message is sent over the channel outboundJdbcChannelOne.
3. Messages over this channel are delivered to the outbound-channel-adapter to execute the given insert.
4. The provided org.springframework.integration.jdbc.ExpressionEvaluatingSqlParameterSourceFactory
is used to get an instance of org.springframework.jdbc.core.namedparam.SqlParameterSource. That is 
used to resolve the given named parameter to the value. In our case the SpEL is executed to yield 
a value that will be inserted. See the config for more details.

For executing the program and see the results, execute the junit test case
org.springframework.integration.samples.jdbc.OutboundChannelAdapterTest

Query the database for table Person to see the result. 

