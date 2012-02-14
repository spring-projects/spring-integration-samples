Error Handling Sample
=====================

Demonstrates the handling of Exceptions in an asynchronous messaging environment. View the **errorHandlingDemo.xml** configuration file. Notice the use of a **Header Enricher** within a **Chain**, that establishes an **error-channel** reference prior to passing the message to a **Service Activator**.

In order to run the sample, execute **PartyDemoTest** in package **org.springframework.integration.samples.errorhandling**.