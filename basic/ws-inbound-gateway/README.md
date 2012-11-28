Web Service Inbound Gateway Sample 
==================================

This sample demonstrates a barebones *inbound Web Service Gateway*. Take a look at **web.xml** in the **WEB-INF** directory where the [Spring Web Services][] Message-dispatching Servlet is defined. Then have a look at the **spring-ws-config.xml** file (also in the **WEB-INF** directory) where the Spring WS EndpointMapping is defined. Finally, view the *Spring Integration* configuration in the **inbound-gateway-config.xml** file within the **org.springframework.integration.samples.ws** package where the actual *Gateway* is defined along with a *Channel* and *Service Activator*.

To use the *Gateway*, you can run the tests that are located within the **src/test/java** directory. One is for standalone testing of the gateway itself, while the other tests the *Gateway* running on a web server. The latter uses [Spring Web Services][]' client-side support. Alternatively, you can simply start the server, and then send invocations with any standalone HTTP client testing tool. The request format should be similar to the following and should be *POST*ed to the service URL (e.g. http://localhost:8080/ws-inbound-gateway/echoservice):

	<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
	<SOAP-ENV:Header/>
	<SOAP-ENV:Body>
	<echoRequest xmlns="http://www.springframework.org/spring-ws/samples/echo">hello</echoRequest>
	</SOAP-ENV:Body>
	</SOAP-ENV:Envelope>

[Spring Web Services]: http://www.springsource.org/spring-web-services