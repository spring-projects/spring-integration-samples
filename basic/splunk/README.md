Splunk Sample
=============================================
This is a basic example of use  of spring integration splunk adapter.
The example dependes from 1.5.0.0  splunk SDK and from Splunk Adapter 1.2.0.BUILD-SNAPSHOT, documentation about adapter
can be found at https://github.com/spring-projects/spring-integration-splunk 
 	
At the moment spring integration can consume data in 5 ways

* Blocking
* Non blocking
* Saved search
* Realtime
* Export

This example used 
* Blocking Mode as consumer 
* Send a custom Event using submit and index type as data writers provided.

## Running the sample
download splunk 6.3 
####Add index
./splunk add index tinyIndex
./splunk add index someIndex
####Start
./splunk start
Launch TinySpluckProducer main and  TinySpluckConsumer
