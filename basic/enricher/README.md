Spring Integration - Enricher Sample
====================================

# Overview

This sample demonstrates how the Content Enricher pattern can be used in Spring Integration. The Content Enricher allows you to augment a message with additional data from an external source without requiring the original sender to know about the enrichment process.

This sample supports both **XML-based** and **Java-based** Spring Integration configurations. You can select which configuration to use by setting the active Spring profile.

## Configuration Options

This sample supports two configuration modes:

* **Java Configuration** (`java-config` profile) - Uses Java `@Configuration` classes from the `config` package. This is the **default** mode.
* **XML Configuration** (`xml-config` profile) - Uses XML configuration files from `META-INF/spring/integration/`

### Profile Selection

Only one configuration profile should be active at a time. The `java-config` and `xml-config` profiles are mutually exclusive.

* If no profile is explicitly provided, `java-config` is used by default (as configured in `application.properties`).
* To use XML configuration, explicitly activate the `xml-config` profile at runtime.

# Getting Started

## Using Java Configuration (Default)

The sample uses Java configuration by default. You can run the sample application by either:

* Running the "Main" class from within your IDE (Right-click on Main class --> Run As --> Java Application)
* Or from the command line execute:
    
    $ gradlew :enricher:run

In this mode, the application context is defined using Spring Integration Java configuration classes instead of XML. The configuration classes are located in the `org.springframework.integration.samples.enricher.config` package.

## Using XML Configuration

To run the sample with XML configuration, activate the `xml-config` profile:

* From your IDE, set the system property: `-Dspring.profiles.active=xml-config`
* Or from the command line:
    
    $ gradlew :enricher:run -Dspring.profiles.active=xml-config

This mode uses the legacy XML-based wiring from `spring-integration-context.xml`.

# How It Works

This example illustrates the usage of the Content Enricher pattern.           
                                                                          
Once the application has started, please execute the various Content Enricher examples by:                               
     
* entering 1 + Enter
* entering 2 + Enter
* entering 3 + Enter
                                                                     
3 different message flows are triggered. For use-cases 1+2 a **User** object containing only the **username** is passed in. For use-case 3 a Map with the **username** key is passed in and enriched with the **User** object using the **user** key:                          
                                                                          
* **1**: In the *Enricher*, pass the full **User** object to the **request channel**. 
* **2**: In the *Enricher*, pass only the **username** to the **request channel** by using the **request-payload-expression** attribute.   
* **3**: In the *Enricher*, pass only the username to the **request channel**, executing the same Service Activator as in **2**.

## About This Pattern

Spring Integration is moving toward Java configuration as the primary style for new development, while XML configuration remains available for users who prefer that style or are migrating older systems. This sample demonstrates how both approaches can coexist in the same project using Spring profiles to select the desired configuration mode at runtime.

This pattern (Java first, XML as an alternative) is being applied across other samples in this repository, using `basic/barrier` as a reference model.

# Resources

For help please take a look at the Spring Integration documentation:

https://docs.spring.io/spring-integration/reference/

