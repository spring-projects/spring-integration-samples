This sample demonstrates how you can send a multipart request to a Spring Integration's HTTP service using Spring's RestTemplate
It consists of two parts - Client and Server.

Client uses Spring's RestTemplate to assemble and send multipart request

Server is Spring Integration's HTTP endpoint configuration.

To run this sample
   1) deploy project 
   			- If you are using STS and project is imported as Eclipse project in your workspace you can just execute 'Run on Server'
   			- You can also run 'mvn clean install' and generate the WAR file that you can deploy the conventional way
   2) run the simple test client program: org.springframework.integration.samples.multipart.MultipartClient
   
   You should see the following output from the server:
   
INFO : ...MultipartClient - Successfully recieved multipart request: {company=[[Ljava.lang.String;@147e8bd9], company-logo=[org.springframework.integration.http.UploadedMultipartFile@f5e12]}
INFO : ...MultipartClient - company - SpringSource
INFO : org.springframework.integration.samples.multipart.MultipartClient - company-logo - as UploadedMultipartFile: spring09_logo.png
