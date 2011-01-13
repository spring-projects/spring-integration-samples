This example demonstrates simple request/reply communication when using a pair of HTTP Inbound/Outbound gateways

It consists of two parts - Client and Server.

To run this sample
   1) deploy project 
   			- If you are using STS and project is imported as Eclipse project in your workspace you can just execute 'Run on Server'
   			- You can also run 'mvn clean install' and generate the WAR file that you can deploy the conventional way
   2) run the simple test client program: org.springframework.integration.samples.http.DemoHttpClient
   
   The gateway initiates a simple request posting "Hello" to the server and server responds by appending "from the other side" 
   to the message payload and returns.
   
   You should see the following output from the server:
   
INFO : org.springframework.integration.samples.http.HttpClientDemo - Replied with: Hello from the other side