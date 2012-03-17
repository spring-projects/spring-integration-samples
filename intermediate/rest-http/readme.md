# Introduction

This sample demonstrates how you can send an HTTP request to a Spring Integration's HTTP service while utilizing Spring Integration's new HTTP Path usage;
This sample also uses Spring Security for HTTP Basic authentication. With HTTP Path facility, the client program can send requests with URL Variables.

It consists of two parts - Client and Server.

The following client program can be used to test the HTTP Path usage.

1. RestHttpClientTest. It uses Spring's RestTemplate to assemble and send HTTP request

Server is Spring Integration's HTTP endpoint configuration.

To run this sample

1. deploy project
    - If you are using STS and project is imported as Eclipse project in your workspace you can just execute 'Run on Server'
    - You can also run 'mvn clean install' and generate the WAR file that you can deploy the conventional way
2. run the simple JUNIT Test: org.springframework.integration.samples.rest.RestHttpClientTest
      You may change the URI Variable value in the test to see different results.

For example, when you give 0 as the URL Variable's value in the test, then you should see the following output from the server:

	14:01:34,337  INFO main rest.RestHttpClientTest:95 - The employee list size :2
	14:01:34,353  INFO main rest.RestHttpClientTest:101 - <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
	<EmployeeList>
	    <Employee>
	        <employeeId>1</employeeId>
	        <fname>John</fname>
	        <lname>Doe</lname>
	    </Employee>
	    <Employee>
	        <employeeId>2</employeeId>
	        <fname>Jane</fname>
	        <lname>Doe</lname>
	    </Employee>
	    <returnStatus>0</returnStatus>
	    <returnStatusMsg>Success</returnStatusMsg>
	</EmployeeList>

	14:01:34,556  INFO main rest.RestHttpClientTest:121 - Return Status :[0]
	14:01:34,556  INFO main rest.RestHttpClientTest:122 - Return Status Message :[Success]
	{"employee":[{"employeeId":1,"fname":"John","lname":"Doe"},{"employeeId":2,"fname":"Jane","lname":"Doe"}],"returnStatus":"0","returnStatusMsg":"Success"}

