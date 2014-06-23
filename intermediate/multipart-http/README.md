Multipart Http Sample
=====================

This sample demonstrates how you can send a *multipart request* to a Spring Integration's HTTP service using 2 scenarios:

1. Spring's RestTemplate
2. Spring Integration Http Outbound Gateway

It consists of two parts - Client and Server. There are two client programs:

1. MultipartRestClient. It uses Spring's RestTemplate to assemble and send  multipart request
2. MultipartClientForHttpOutboundClient. It uses Spring Integration Http Outbound Gateway to send multipart request.

The interesting part about this client is the way it assembles the Multipart request using a plain old Map.

The Server is Spring Integration's HTTP endpoint configuration.

To run this sample:

1. Deploy project
   * If you are using STS and project is imported as Eclipse project in your workspace you can just execute **Run on Server**
   * You can also run **gradlew :multipart-http:build** and generate the WAR file that you can deploy the conventional
    way
2. run the simple test client program: **org.springframework.integration.samples.multipart.MultipartClient**

You should see the following output from the server:

	INFO : ...MultipartClient - Successfully received multipart request: {company=[[Ljava.lang.String;@147e8bd9], company-logo=[org.springframework.integration.http.UploadedMultipartFile@f5e12]}
	INFO : ...MultipartClient - company - SpringSource
	INFO : org.springframework.integration.samples.multipart.MultipartClient - company-logo - as UploadedMultipartFile: spring09_logo.png
