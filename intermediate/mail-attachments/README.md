Spring Integration - Processing Email Attachments Sample
========================================================

# Overview

This example demonstrates how emails including their attachments can be processed using Spring Integration. This sample uses the following Spring Integration components:

* Mail Inbound Channel Adapter
* Chain
* Transformer
* Splitter
* File Outbound Channel Adapter

# Getting Started

In order to use this sample you must have access to a mail-server. You can either use an external server (e.g. GMail) or you can also easily setup your own mail server using Apache James 3.0 (http://james.apache.org/). You can find instructions for setting up a basic instance at:

* http://james.apache.org/server/3/quick-start.html
* http://hillert.blogspot.com/2011/05/testing-email-notifications-with-apache.html

In **src/main/resources/META-INF/spring/integration/spring-integration-context.xml** change the following to reflect the settings for your mail server.

    store-uri="imap://test:test@localhost:143/INBOX" 

Lastly, before you run the example, please make sure that your email inbox contains some messages. 

You can run this sample by either.

* running the "Main" class from within STS (Right-click on Main class --> Run As --> Java Application)
* or from the command line:
    
    $ gradlew :mail-attachments:run

Once started, the configured mail server will be polled for new email messages every 5 seconds.

--------------------------------------------------------------------------------

For help please take a look at the Spring Integration documentation:

http://www.springsource.org/spring-integration

