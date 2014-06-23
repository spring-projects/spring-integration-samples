Spring Integration - Enricher Sample
====================================

# Overview

This sample demonstrates how the Enricher components can be used.

# Getting Started

You can run the sample application by either

* running the "Main" class from within STS (Right-click on Main class --> Run As --> Java Application)
* or from the command line execute:
    
    $ gradlew :enricher:run

This example illustrates the usage of the Content Enricher.           
                                                                          
Once the application has started, please execute the various Content Enricher examples by                               
     
* entering 1 + Enter
* entering 2 + Enter
* entering 3 + Enter
                                                                     
3 different message flows are triggered. For use-cases 1+2 a **User** object containing only the **username** is passed in. For use-case 3 a Map with the **username** key is passed in and enriched with the **User** object using the **user** key:                          
                                                                          
* 1: In the *Enricher*, pass the full **User** object to the **request channel**. 
* 2: In the *Enricher*, pass only the **username** to the **request channel** by using the **request-payload-expression** attribute.   
* 3: In the *Enricher*, pass only the username to the **request channel**, executing the same Service Activator as in **2**.

# Resources

For help please take a look at the Spring Integration documentation:

http://www.springsource.org/spring-integration

