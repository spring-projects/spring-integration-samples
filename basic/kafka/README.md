Apache Kafka Sample
==============

This example demonstrates the use Kafka **Outbound Channel Adapter** and **Message-Driven Channel Adapter**.

It uses Java configuration for the adapters.

## Running the sample

Start Apache Zookeeper and Apache Kafka according to the documentation for the Apache Kafka project.

    $ gradlew :kafka:run

This will package the application and run it using the [Gradle Application Plugin](http://www.gradle.org/docs/current/userguide/application_plugin.html)

#### Using an IDE such as SpringSource Tool Suite™ (STS)

In STS (Eclipse), go to package **org.springframework.integration.samples.kafka**, right-click **Application** and select **Run as** --> **Java Application** (or Spring Boot Application).

### Output

The application sends 10 messages (`foo0` ... `foo9`) to a kafka topic `si.topic`; this must exist or the broker must be configured to auto-create topics.

It then sends a message with a `KafkaNull` payload.

It then dynamically creates a new inbound adapter for a different topic `si.new.topic` and sends 10 messages there.

The message-driven adapter receives the messages and places them in a `QueueChannel` which the application reads and
writes to stdout:

    Sending 10 messages...
    Sending a null message...
    GenericMessage [payload=foo0, headers={kafka_offset=857, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.topic}]
    GenericMessage [payload=foo1, headers={kafka_offset=858, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.topic}]
    GenericMessage [payload=foo2, headers={kafka_offset=859, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.topic}]
    GenericMessage [payload=foo3, headers={kafka_offset=860, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.topic}]
    GenericMessage [payload=foo4, headers={kafka_offset=861, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.topic}]
    GenericMessage [payload=foo5, headers={kafka_offset=862, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.topic}]
    GenericMessage [payload=foo6, headers={kafka_offset=863, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.topic}]
    GenericMessage [payload=foo7, headers={kafka_offset=864, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.topic}]
    GenericMessage [payload=foo8, headers={kafka_offset=865, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.topic}]
    GenericMessage [payload=foo9, headers={kafka_offset=866, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.topic}]
    GenericMessage [payload=org.springframework.kafka.support.KafkaNull@3546d80f, headers={kafka_offset=867, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.topic}]
    Adding an adapter for a second topic and sending 10 messages...
    GenericMessage [payload=bar0, headers={kafka_offset=200, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.new.topic}]
    GenericMessage [payload=bar1, headers={kafka_offset=201, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.new.topic}]
    GenericMessage [payload=bar2, headers={kafka_offset=202, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.new.topic}]
    GenericMessage [payload=bar3, headers={kafka_offset=203, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.new.topic}]
    GenericMessage [payload=bar4, headers={kafka_offset=204, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.new.topic}]
    GenericMessage [payload=bar5, headers={kafka_offset=205, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.new.topic}]
    GenericMessage [payload=bar6, headers={kafka_offset=206, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.new.topic}]
    GenericMessage [payload=bar7, headers={kafka_offset=207, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.new.topic}]
    GenericMessage [payload=bar8, headers={kafka_offset=208, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.new.topic}]
    GenericMessage [payload=bar9, headers={kafka_offset=209, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.new.topic}]

Notice that the offset header increases on each run (the topic is not removed, to demonstrate that the offset is retained
between executions).
