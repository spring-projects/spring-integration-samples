This example demonstrates the following aspects of the CORE EIP support available with Spring Integration:
1. Inbound Channel Adapter
2. Filter
3. Router (SpEL based)
4. Poller with Cron and Interval Trigers

Messages are simply being emitted by the Poller (interval based or cron) triggering 'next()' method of Counter class and 
sent to a 'numbers' channel - Inbound Channel Adapter. From the 'numbers' channel Messages are sent 
to an expression-based router (Spring Expression Language). ALl that router does is simply routing messages 
to OddLogger and EvenLogger service

To execute the Interval-based sample simply run IntervalOddEvenDemoTest class and for Cron-based sample simply 
run CronOddEvenDemo class, You should see the output similar to this:

INFO : org.springframework.integration.samples.oddeven.OddLogger - odd:  1 at 2010-09-16 05:55:46
INFO : org.springframework.integration.samples.oddeven.EvenLogger - even: 2 at 2010-09-16 05:55:49
INFO : org.springframework.integration.samples.oddeven.OddLogger - odd:  3 at 2010-09-16 05:55:52
INFO : org.springframework.integration.samples.oddeven.EvenLogger - even: 4 at 2010-09-16 05:55:55 


