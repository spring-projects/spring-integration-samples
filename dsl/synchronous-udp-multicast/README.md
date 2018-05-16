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

The application sends 10 messages (`foo0` ... `foo9`) to a kafka topic `si.topic`; this must exist or the broker must be configured to auto-create topics.

It then dynamically creates a new inbound adapter for a different topic `si.new.topic` and sends 10 messages there.

The message-driven adapter receives the messages and places them in a `QueueChannel` which the application reads using a no-arg gateway method and writes to stdout:

    Sending 10 messages...
    Send to Kafka: foo0
    Send to Kafka: foo1
    Send to Kafka: foo2
    Send to Kafka: foo3
    Send to Kafka: foo4
    Send to Kafka: foo5
    Send to Kafka: foo6
    Send to Kafka: foo7
    Send to Kafka: foo8
    Send to Kafka: foo9
    GenericMessage [payload=foo0, headers={kafka_offset=847, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.topic}]
    GenericMessage [payload=foo1, headers={kafka_offset=848, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.topic}]
    GenericMessage [payload=foo2, headers={kafka_offset=849, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.topic}]
    GenericMessage [payload=foo3, headers={kafka_offset=850, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.topic}]
    GenericMessage [payload=foo4, headers={kafka_offset=851, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.topic}]
    GenericMessage [payload=foo5, headers={kafka_offset=852, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.topic}]
    GenericMessage [payload=foo6, headers={kafka_offset=853, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.topic}]
    GenericMessage [payload=foo7, headers={kafka_offset=854, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.topic}]
    GenericMessage [payload=foo8, headers={kafka_offset=855, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.topic}]
    GenericMessage [payload=foo9, headers={kafka_offset=856, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.topic}]
    Adding an adapter for a second topic and sending 10 messages...
    Send to Kafka: bar0
    Send to Kafka: bar1
    Send to Kafka: bar2
    Send to Kafka: bar3
    Send to Kafka: bar4
    Send to Kafka: bar5
    Send to Kafka: bar6
    Send to Kafka: bar7
    Send to Kafka: bar8
    Send to Kafka: bar9
    GenericMessage [payload=bar0, headers={kafka_offset=190, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.new.topic}]
    GenericMessage [payload=bar1, headers={kafka_offset=191, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.new.topic}]
    GenericMessage [payload=bar2, headers={kafka_offset=192, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.new.topic}]
    GenericMessage [payload=bar3, headers={kafka_offset=193, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.new.topic}]
    GenericMessage [payload=bar4, headers={kafka_offset=194, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.new.topic}]
    GenericMessage [payload=bar5, headers={kafka_offset=195, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.new.topic}]
    GenericMessage [payload=bar6, headers={kafka_offset=196, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.new.topic}]
    GenericMessage [payload=bar7, headers={kafka_offset=197, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.new.topic}]
    GenericMessage [payload=bar8, headers={kafka_offset=198, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.new.topic}]
    GenericMessage [payload=bar9, headers={kafka_offset=199, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.new.topic}]

Notice that the offset header increases on each run (the topic is not removed, to demonstrate that the offset is retained between executions).
