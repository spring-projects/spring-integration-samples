To run this sample
   1) deploy project 
   			- If you are using STS and project is imported as Eclipse project in your workspace you can just execute 'Run on Server'
   			- You can also run 'mvn clean install' and generate the WAR file that you can deploy the conventional way
   2) run the simple test client program: org.springframework.integration.samples.multipart.MultipartClient
   
   You should see the following output from the server:
   
   ### Successfully recieved multipart request ###
	company - SpringSource
	company-logo - as UploadedMultipartFile: spring09_logo.png