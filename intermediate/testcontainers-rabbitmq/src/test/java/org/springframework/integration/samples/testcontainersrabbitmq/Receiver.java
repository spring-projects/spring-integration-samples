package org.springframework.integration.samples.testcontainersrabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
@Component
public class Receiver {

    private static final Map<Integer, String> messages;

    static {

        messages = new HashMap<>();

        messages.put( 1, "This is message 1" );
        messages.put( 2, "This is message 2" );
        messages.put( 3, "This is message 3" );
        messages.put( 4, "This is message 4" );
        messages.put( 5, "This is message 5" );

    }

    @PostConstruct
    public void initialize() {
        log.info( "Receiver initialized!" );
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue( value = "downstream.request", durable = "true" ),
                    exchange = @Exchange( value = "downstream", ignoreDeclarationExceptions = "true", type = "topic" ),
                    key = "downstream.request.#"
            )
    )
    @SendTo( "downstream.results" )
    public Message<Response> handleMessage( Request request, @Header( "request-correlation-id" ) String correlationId ) throws IOException {

        log.info( "handleMessage : received message [{}]", request );

        Integer messageId;
        if( request.getMessageId().isPresent() ) {

            messageId = request.getMessageId().get();

        } else {

            messageId = new Random().ints( 1, 5 ).findFirst().getAsInt();
        }

        Response fakeResponse = new Response( request.getId(), messages.get( messageId ) );

        return MessageBuilder
                .withPayload( fakeResponse )
                .setHeader( "request-correlation-id", correlationId )
                .build();
    }

}
