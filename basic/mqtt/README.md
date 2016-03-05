Spring Integration - MQTT Sample
================================

# Overview

This sample demonstrates basic functionality of the **Spring Integration MQTT Adapters**.

It assumes a broker is running on localhost on port 1883.

Once the application is started, you enter some text on the command prompt and a message containing that entered text is
dispatched to the MQTT topic. In return that message is retrieved by Spring Integration and then logged.

# How to Run the Sample

If you imported the example into your IDE, you can just run class **org.springframework.integration.samples.mqtt.Application**.
For example in [SpringSource Tool Suite](http://www.springsource.com/developer/sts) (STS) do:

* Right-click on SampleSimple class --> Run As --> Spring Boot App

(or run from the boot console).

Alternatively, you can start the sample from the command line:

* ./gradlew :mqtt:run

Enter some data (e.g. `foo`) on the console; you will see `foo sent to MQTT, received from MQTT`

Ctrl-C to terminate.

