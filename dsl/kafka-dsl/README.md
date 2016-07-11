Spring Integration Java DSL and Apache Kafka Sample
==============

This example demonstrates the use of `Kafka09` Namespace Factory from Spring Integration Java DSL.

## Running the sample

Start Apache Zookeeper and Apache Kafka according to the documentation for the Apache Kafka project.

    $ gradlew :kafka:run

This will package the application and run it using the [Gradle Application Plugin](http://www.gradle.org/docs/current/userguide/application_plugin.html)

#### Using an IDE such as SpringSource Tool Suite™ (STS)

In STS (Eclipse), go to package **org.springframework.integration.samples.kafka**, right-click **Application** and select **Run as** --> **Java Application** (or Spring Boot Application).

### Output

The application sends 10 messages (`foo0` ... `foo9`) to a kafka topic `si.topic` (which is created if necessary).

The message-driven adapter receives the messages and places them in a `QueueChannel` which the application reads and
writes to stdout:

	GenericMessage [payload=foo0, headers={kafka_offset=21, kafka_messageKey=si.key, kafka_topic=si.topic, kafka_partitionId=0, kafka_nextOffset=22}]
	GenericMessage [payload=foo1, headers={kafka_offset=22, kafka_messageKey=si.key, kafka_topic=si.topic, kafka_partitionId=0, kafka_nextOffset=23}]
	GenericMessage [payload=foo2, headers={kafka_offset=23, kafka_messageKey=si.key, kafka_topic=si.topic, kafka_partitionId=0, kafka_nextOffset=24}]
	GenericMessage [payload=foo3, headers={kafka_offset=24, kafka_messageKey=si.key, kafka_topic=si.topic, kafka_partitionId=0, kafka_nextOffset=25}]
	GenericMessage [payload=foo4, headers={kafka_offset=25, kafka_messageKey=si.key, kafka_topic=si.topic, kafka_partitionId=0, kafka_nextOffset=26}]
	GenericMessage [payload=foo5, headers={kafka_offset=26, kafka_messageKey=si.key, kafka_topic=si.topic, kafka_partitionId=0, kafka_nextOffset=27}]
	GenericMessage [payload=foo6, headers={kafka_offset=27, kafka_messageKey=si.key, kafka_topic=si.topic, kafka_partitionId=0, kafka_nextOffset=28}]
	GenericMessage [payload=foo7, headers={kafka_offset=28, kafka_messageKey=si.key, kafka_topic=si.topic, kafka_partitionId=0, kafka_nextOffset=29}]
	GenericMessage [payload=foo8, headers={kafka_offset=29, kafka_messageKey=si.key, kafka_topic=si.topic, kafka_partitionId=0, kafka_nextOffset=30}]
	GenericMessage [payload=foo9, headers={kafka_offset=30, kafka_messageKey=si.key, kafka_topic=si.topic, kafka_partitionId=0, kafka_nextOffset=31}]

Notice that the offset header increases on each run (the topic is not removed, to demonstrate that the offset is retained between executions).
