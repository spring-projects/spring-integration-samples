package org.springframework.integration.samples.testcontainersrabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;

@Slf4j
@Configuration
@EnableIntegration
@IntegrationComponentScan
public class IntegrationConfig {

    @Bean
    public IntegrationFlow request( RabbitTemplate amqpTemplate ) {

        return f -> f
                .log()
                .handle(
                        Amqp.outboundGateway( amqpTemplate )
                                .exchangeName( "downstream" )
                                .routingKey( "downstream.request" )
                );
    }

}
