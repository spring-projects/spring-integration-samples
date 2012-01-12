Loan Shark Application
======================

See **README.txt** in the **loan-broker sample** for information about this sample.

To change the multicast code in **udps.groovy** to rely on unicast instead, simply change the *MulticastSocket* type to *DatagramSocket* and remove the next 2 lines that deal with joining the multicast group. Then, in the configuration of the actual adapter, set: 

	host="127.0.0.1" and multicast="false".

For the Roo/Spring Integration application: 

* Edit the **integrationContext.xml** file
* On the udpIn adapter, set **multicast** to false and
* Remove the **multicast-address** attribute.