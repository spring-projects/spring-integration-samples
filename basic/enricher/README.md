Spring Integration - Enricher Sample
================================

# Overview

This sample demonstrates how the Enricher components can be used.

# Getting Started

You can run the sample application by either

* running the "Main" class from within STS (Right-click on Main class --> Run As --> Java Application)
* or from the command line execute:
    - mvn package
    - mvn exec:java

This example illustrates the usage of the Content Enricher by covering 2 use-cases.
Once the application is started, you can execute each individual use case by:

* entering 1 + Enter
* entering 2 + Enter

Each use case will trigger slightly different Spring Integration message flows. For
both flows a **User** object containing only the **username** is passed in through a *Gateway*.

However the Enrichment is executed differently for each flow:

* 1: In the *Enricher*, pass the full **User** object to the **request channel**.
* 2: In the *Enricher*, pass only the **username** to the **request channel** by using the **request-payload-expression** attribute.

# Resources

For help please take a look at the Spring Integration documentation:

http://www.springsource.org/spring-integration

