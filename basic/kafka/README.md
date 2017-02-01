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
    GenericMessage [payload=foo0, headers={kafka_offset=657, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.topic}]
    GenericMessage [payload=foo1, headers={kafka_offset=658, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.topic}]
    GenericMessage [payload=foo2, headers={kafka_offset=659, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.topic}]
    GenericMessage [payload=foo3, headers={kafka_offset=660, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.topic}]
    GenericMessage [payload=foo4, headers={kafka_offset=661, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.topic}]
    GenericMessage [payload=foo5, headers={kafka_offset=662, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.topic}]
    GenericMessage [payload=foo6, headers={kafka_offset=663, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.topic}]
    GenericMessage [payload=foo7, headers={kafka_offset=664, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.topic}]
    GenericMessage [payload=foo8, headers={kafka_offset=665, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.topic}]
    GenericMessage [payload=org.springframework.kafka.support.KafkaNull@4d9d1b69, headers={kafka_offset=667, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.topic}]
    Adding an adapter for a second topic and sending 10 messages...
    GenericMessage [payload=foo0, headers={kafka_offset=70, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.new.topic}]
    GenericMessage [payload=foo1, headers={kafka_offset=71, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.new.topic}]
    GenericMessage [payload=foo2, headers={kafka_offset=72, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.new.topic}]
    GenericMessage [payload=foo3, headers={kafka_offset=73, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.new.topic}]
    GenericMessage [payload=foo4, headers={kafka_offset=74, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.new.topic}]
    GenericMessage [payload=foo5, headers={kafka_offset=75, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.new.topic}]
    GenericMessage [payload=foo6, headers={kafka_offset=76, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.new.topic}]
    GenericMessage [payload=foo7, headers={kafka_offset=77, kafka_receivedMessageKey=si.key, kafka_receivedPartitionId=0, kafka_receivedTopic=si.new.topic}]

Notice that the offset header increases on each run (the topic is not removed, to demonstrate that the offset is retained
between executions).
