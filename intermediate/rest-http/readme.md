# Introduction

This sample demonstrates how you can send an HTTP request to a *Spring Integration* HTTP service while utilizing *Spring Integration*'s HTTP Path usage. This sample also uses [Spring Security][] for HTTP Basic authentication. With the HTTP Path facility, the client program can send requests with URL Variables.

The sample consists of two parts:

* Client and
* Server

The *Client* program is provided as a [JUnit][] test:

* RestHttpClientTest

This test-case can be used to test the HTTP Path usage. It uses Spring's [RestTemplate][] to assemble and send HTTP requests. The *Server*, on the other hand, is using Spring Integration's HTTP Endpoint configuration. 
The provided project itself is a web project and you can build the project using [Gradle][] and deploy the resulting 
war under `target/rest-http-*.war` to Servlet Containers such as [Jetty][] or [Apache Tomcat][]:

	$ gradlew :rest-http:build

# To run this sample

1. Deploy the project
  - If you are using [Spring Tool Suite][] (STS) and project is imported as Eclipse project in your workspace you can just execute 'Run on Server'
  - You can also run **gradlew :rest-http:build** and generate the WAR file that you can deploy to Servlet Containers
2. Run the simple JUnit Test: **org.springframework.integration.samples.rest.RestHttpClientTest**
  - You may change the URI Variable value in the test to see different results.

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

# The REST API

If you like to access the server's REST API yourself, please use the following URLs:

## Return All Employees

http://localhost:8080/rest-http/services/employee/0/search

## Return Specific Employees

http://localhost:8080/rest-http/services/employee/{employeeId}/search

For example using [cURL][] you can list the details for a specific user with:

	$ curl -u SPRING:spring http://localhost:8080/rest-http/services/employee/1/search

This should produce output similar to this:

	<?xml version="1.0" encoding="UTF-8" standalone="yes"?><EmployeeList><Employee><employeeId>1</employeeId><fname>John</fname><lname>Doe</lname></Employee><returnStatus>0</returnStatus><returnStatusMsg>Success</returnStatusMsg></EmployeeList>

## Security

The REST Endpoint is using [Spring Security][]. The security credentials are:

* Username: SPRING
* Password: spring

They are stored in `src/main/resources/users.properties`.

[Apache Tomcat]: http://tomcat.apache.org/
[cURL]: http://en.wikipedia.org/wiki/CURL
[Jetty]: http://www.eclipse.org/jetty/
[JUnit]: http://junit.org/
[Gradle]: http://www.gradle.org
[RestTemplate]: http://static.springsource.org/spring/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html
[Spring Security]: http://www.springsource.org/spring-security
[Spring Tool Suite]: http://www.springsource.org/sts
