HBase sink with trigger on finish Application
=======================

## Overview

The *HBase sink with trigger on finish* implements the competing consumers pattern )multiple consumers on a queue) but adds the functionality to call a task (a mapreduce job on received data in this case) on the arrival of a stop message. 
Moreover, the Controller consumer, waits for all other consumers to finish their processing before triggering its processing task (via Zookeeper/Curator).

Feedbacks are welcome!
