Control Bus Sample
==================

This example demonstrates the functionality of the Control Bus component available with Spring Integration. The Control Bus uses SpEL to send a Control Message to start/stop an inbound adapter.

This sample supports both **XML-based** and **Java-based** Spring Integration configurations. You can select which configuration to use by setting the active Spring profile.

## Configuration Options

This sample supports two configuration modes:

* **Java Configuration** (`java-config` profile) - Uses Java `@Configuration` classes from the `config` package. This is the **default** mode.
* **XML Configuration** (`xml-config` profile) - Uses XML configuration files from `META-INF/spring/integration/`

### Profile Selection

Only one configuration profile should be active at a time. The `java-config` and `xml-config` profiles are mutually exclusive.

* If no profile is explicitly provided, `java-config` is used by default (as configured in `application.properties`).
* To use XML configuration, explicitly activate the `xml-config` profile at runtime.

## Running the Sample

### Using Java Configuration (Default)

The sample uses Java configuration by default. Execute **ControlBusDemoTest** in the **org.springframework.integration.samples.controlbus** package. The test method `demoControlBusWithJavaConfig()` demonstrates the Java configuration mode.

In this mode, the application context is defined using Spring Integration Java configuration classes instead of XML. The configuration classes are located in the `org.springframework.integration.samples.controlbus.config` package.

### Using XML Configuration

To run the sample with XML configuration, activate the `xml-config` profile. The test method `demoControlBusWithXmlConfig()` demonstrates this mode.

This mode uses the legacy XML-based wiring from `ControlBusDemo-context.xml`. The XML namespace is only active when the `xml-config` profile is enabled.

## Expected Output

You will see output similar to this:

```
INFO : org.springframework.integration.samples.controlbus.ControlBusDemoTest - Received before adapter started: null
INFO : org.springframework.integration.samples.controlbus.ControlBusDemoTest - Received before adapter started: [Payload=Hello][Headers={timestamp=1294950897714, id=240e72fb-93b0-4d38-8fe8-b701cf7e9a5d}]
INFO : org.springframework.integration.samples.controlbus.ControlBusDemoTest - Received after adapter stopped: null
```
