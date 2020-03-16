package org.springframework.integration.samples.testcontainersrabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter( final ObjectMapper objectMapper ) {

        return new Jackson2JsonMessageConverter( objectMapper );
    }

    @Bean
    public RabbitTemplate amqpTemplate( ConnectionFactory connectionFactory, Jackson2JsonMessageConverter jsonMessageConverter ) {

        RabbitTemplate rabbitTemplate = new RabbitTemplate( connectionFactory );
        rabbitTemplate.setMessageConverter( jsonMessageConverter );
        rabbitTemplate.setReplyAddress( "downstream.results" );
        rabbitTemplate.setReplyTimeout( 10000 );
        rabbitTemplate.setCorrelationKey( "request-correlation-id" );
        rabbitTemplate.setUseDirectReplyToContainer( false );

        return rabbitTemplate;
    }

    @Bean
    public SimpleMessageListenerContainer replyListenerContainer( ConnectionFactory connectionFactory, RabbitTemplate amqpTemplate ) {

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory( connectionFactory );
        container.setQueueNames( "downstream.results" );
        container.setMessageListener( amqpTemplate );

        return container;
    }

}
