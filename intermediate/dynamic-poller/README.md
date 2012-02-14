Dynamic Poller Sample
=====================

By default this application will (poll) print out the current system time every 5 seconds. From the command line you can enter a non-negative numeric value to change the polling period (in milliseconds) at runtime. 

Under the cover an **Inbound Channel Adapter** polls for the current system time. The **Poller** which is used by the **Inbound Channel Adapter** is configured with a custom trigger. The resulting message contains as payload the time in milliseconds and the message is sent to a **Logging Channel Adapter**, which will print the time to the command prompt.

You can run the application by either

* running the "Main" class from within STS (Right-click on Main class --> Run As --> Java Application)
* or from the command line using the [Exec Maven Plugin](http://mojo.codehaus.org/exec-maven-plugin/):
    - mvn package exec:java

You should see output like the following:

	INFO : org.springframework.integration.samples.poller.Main - 
	==========================================================
                                                          
	 Welcome to the Spring Integration Dynamic Poller Sample! 
                                                          
	    For more information please visit:                    
	    http://www.springsource.org/spring-integration        
                                                          
	==========================================================
	INFO : org.springframework.integration.samples.poller.Main - 
	=========================================================
                                                         
	    Please press 'q + Enter' to quit the application.    
                                                         
	=========================================================
	Please enter a non-negative numeric value and press <enter>: INFO : org.springframework.integration.samples.poller - 1329257519165
	INFO : org.springframework.integration.samples.poller - 1329257524207
	INFO : org.springframework.integration.samples.poller - 1329257529208
	INFO : org.springframework.integration.samples.poller - 1329257534209
	INFO : org.springframework.integration.samples.poller - 1329257539210

