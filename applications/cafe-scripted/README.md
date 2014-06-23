Cafe Demo - Scripted Implementation
===================================

This is the scripted implementation of the classic **cafe** sample application. You can choose among **javascript**, **groovy**, **ruby**, and **python** scripting languages. The functionality is basically identical in all cases to the original cafe demo. 

# Instructions for running the CafeDemo sample

The script language is passed as a command line argument. This may be run directly from Gradle:

	$ gradlew :cafe-scripted:runCafeDemoApp -Plang=[language]

## Groovy Control Bus

This sample also demonstrates the use of Spring Integration's **groovy control bus** which accepts Groovy scripts as control messages. These scripts may invoke lifecycle operations on adapters or operations on managed beans.

To demonstrate the control bus, while the CafeDemoApp is running, execute in a separate window:

	$ gradlew :cafe-scripted:runControlBus

This will use groovy scripts to 

 * Query the waiter for the total number of orders delivered
 * If the total orders > 3, stop the inbound adaptor on the cafe (the order flow). The Cafe application will continue to run, but eventually the output will stop when all pending orders have completed.


