Http Sample
===========

This example demonstrates simple request/reply communication when using a pair of **HTTP Inbound/Outbound Gateways**.

The sample consists of two parts:

* Client - Basic command-line application
* Server - Web application (War-file)

## Running the sample

### Server

#### Command Line Using Gradle

The easiest way to run the **server** is to use the [Gradle Jetty Plugin](http://www.gradle.org/docs/current/userguide/jetty_plugin.html).
 Simply execute:

    $ gradlew :http:jettyRun

This command starts a Jetty servlet container running on port 8080 serving the application. 
Alternatively you can also package the war-file and deploy it manually to a servlet container of your choosing. For that to happen execute:

    $ gradlew :http:build

The resulting war-file will be located in the **target** folder.

#### Using an IDE such as SpringSource Tool Suite™ (STS)

If you are using [STS](http://www.springsource.com/developer/sts) and the project is imported as an Eclipse project into your workspace, you can just execute **Run on Server**. This will start the **server** application. 

### Client

#### Command Line Using Gradle

In order to run the **client** using Gradle, execute:

    $ gradlew :http:run

This will package the application and run it using the [Gradle Application Plugin](http://www.gradle.org/docs/current/userguide/application_plugin.html)

#### Using an IDE such as SpringSource Tool Suite™ (STS)

In STS (Eclipse), go to package **org.springframework.integration.samples.http**, right-click **HttpClientDemo** and select **Run as** --> **Java Application**. This will run the **client** application.

### Output
  
The gateway (**client**) initiates a simple request posting "Hello" to the **server** and the **server** responds by appending **from the other side** to the message payload and returns. You should see the following output from the server:
   
    ++++++++++++ Replied with: Hello from the other side ++++++++++++

