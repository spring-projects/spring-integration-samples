package org.springframework.integration.samples.testcontainersrabbitmq;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@ExtendWith( SpringExtension.class )
@SpringBootTest( webEnvironment = NONE )
@SpringIntegrationTest
@Import({ Receiver.class, IntegrationConfigTest.Config.class })
class IntegrationConfigTest {

    @Autowired
    @Qualifier( "request.input" )
    private MessageChannel requestInput;

    @Test
    public void test() {

        MessagingTemplate messagingTemplate = new MessagingTemplate();

        UUID requestId = UUID.randomUUID();
        Request fakeRequest = new Request( requestId, Optional.of( 1 ) );

        Message<?> receive =
                messagingTemplate
                        .sendAndReceive( requestInput,
                                MessageBuilder
                                        .withPayload( fakeRequest )
                                        .setHeader( "Content-Type", "application/json" )
                                        .setHeader( "request-correlation-id", UUID.randomUUID().toString() )
                                        .build()
                        );
        assertThat( receive ).isNotNull();
        assertThat( receive.getPayload() ).isInstanceOf( Response.class );

        Response actual = (Response) receive.getPayload();
        assertThat( actual.getRequestId() ).isEqualTo( requestId );
        assertThat( actual.getMessage() ).isEqualTo( "This is message 1" );

    }

    @TestConfiguration
    public static class Config {

        public static final String TOPIC_EXCHANGE = "downstream";
        public static final String RESULTS_QUEUE = "downstream.results";
        public static final String RESULTS_ROUTING_KEY = "downstream.results.#";

        @Bean
        TopicExchange topicExchange() {

            return new TopicExchange( TOPIC_EXCHANGE );
        }

        @Bean
        Queue resultsQueue() {

            return new Queue( RESULTS_QUEUE, false );
        }

        @Bean
        Binding resultsBinding( TopicExchange topicExchange, Queue resultsQueue ) {

            return BindingBuilder.bind( resultsQueue )
                    .to( topicExchange )
                    .with( RESULTS_ROUTING_KEY );
        }

    }

}