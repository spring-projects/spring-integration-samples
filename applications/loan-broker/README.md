Loan Broker Application
=======================

The Loan Broker sample is distributed as a Gradle project, so you can run it in two ways.

### Gradle (command line):

Execute **gradlew :loan-broker:run**. You should see output that looks similar to this:

	. . . . .
	INFO : org.springframework.integration.loanbroker.demo.LoanBrokerDemo - 
	********* Best Quote: 
	====== Loan Quote =====
	Lender: StubBank-11
	Loan amount: $270,279
	Quotation Date: Fri Mar 12 02:19:35 EST 2010
	Expiration Date: Tue Mar 16 02:19:35 EDT 2010
	Term: 17 years
	Rate: 5.9092593%
	=======================
	. . . . .

### Eclipse/STS (with m2eclipse plug-in)

1. Import project into Eclipse/STS. If the m2eclipse plugin is installed, the dependencies will be downloaded automatically.
2. Run the **org.springframework.integration.sample.loanbroker.demo.LoanBrokerDemo** class located in **src/test/java**.


# Loan Shark Extension

This extension to the loan broker sample shows how to exchange messages between Spring Integration applications (and other technologies) using UDP. Any loan quotes over 5.2% will be sent to the loanshark application.

* Deploy the **loanshark** sample to a web container (e.g. **gradlew :loan-broker:jettyRun**); or 
* run the perl (**udps.pl**) or groovy (**udps.groovy**) scripts from the command line.

Run the **LoanBrokerSharkDetectorDemo** test case. You can see the banks that were detected as loan sharks in the **loanshark web UI**.

If you use multicast (the default), messages will be received by the Roo UI war, the perl script, and the groovy script. If not using multicast, only one of the receivers can be used.

To change from multicast to unicast, change the udpOut adapter in **shark-detector-config.xml** to set **host** to **localhost** and multicast to **false**. Refer to the **README.md** file in the loanshark sample project to change it from multicast to unicast for either the Roo application or the groovy script.

