The Loan Broker sample is distributed as a valid Eclipse/Maven project, so you can run it in two ways.

Maven (command line):
1. Execute 'mvn install'
   You should see output that looks similar to this:
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
	
Eclipse/STS (with m2eclipse plug-in)
1. Import project into Eclipse/STS. If the m2eclipse plugin is installed, the dependencies will be downloaded automatically.
2. Run the 'org.springframework.integration.samples.loanbroker.demo.LoanBrokerDemo' class located in 'src/test/java'.
