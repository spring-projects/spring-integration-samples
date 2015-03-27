Dynamic Poller Sample
=====================

By default this application will (poll) print out the current system time every 5 seconds. From the command line you can enter a non-negative numeric value to change the polling period (in milliseconds) at runtime. 

Under the cover an **Inbound Channel Adapter** polls for the current system time. The **Poller**, which is used by the **Inbound Channel Adapter**, is configured with a custom Trigger (*org.springframework.integration.samples.poller.DynamicPeriodicTrigger*). The resulting message contains as payload the time in milliseconds and the message is sent to a **Logging Channel Adapter**, which will print the time to the command prompt.

When changing the polling period, the change to the trigger will occur after the NEXT poll at the current rate. Therefore, if the current polling period is 60 seconds and you change it to 1 second, it can take up to 60 seconds to take effect, depending on when in the polling cycle you make the change.

### Running the Application

You can run the application by either:

* running the "Main" class from within STS (Right-click on Main class --> Run As --> Java Application)
* or from the command line using the [Gradle](http://www.gradle.org):
    
    $ gradlew :dynamic-poller:runHelloWorldApp

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
	Please enter a non-negative numeric value and press <enter>:
	INFO : org.springframework.integration.samples.poller - 2012-02-15 12:14:37.503
	INFO : org.springframework.integration.samples.poller - 2012-02-15 12:14:42.504
	INFO : org.springframework.integration.samples.poller - 2012-02-15 12:14:47.505


