package org.springframework.integration.samples.graphql;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.graphql.GraphQlService;
import org.springframework.graphql.RequestInput;
import org.springframework.graphql.RequestOutput;
import org.springframework.graphql.data.method.annotation.support.AnnotatedControllerConfigurer;
import org.springframework.graphql.execution.ExecutionGraphQlService;
import org.springframework.graphql.execution.GraphQlSource;
import org.springframework.integration.channel.FluxMessageChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.graphql.outbound.GraphQlMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

@SpringJUnitConfig
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GraphqlIntegrationFlowTests {

    @Autowired
    private FluxMessageChannel inputChannel;

    @Autowired
    private FluxMessageChannel resultChannel;

    @Autowired
    private PollableChannel errorChannel;

    @Test
    public void testQuery() {

        StepVerifier verifier = StepVerifier.create(
                        Flux.from(this.resultChannel)
                                .map(Message::getPayload)
                                .cast(RequestOutput.class)
                )
                .consumeNextWith(result -> {
                    assertThat(result).isInstanceOf(RequestOutput.class);
                    Map<String, Object> data = result.getData();
                    Map<String, Object> query = (Map<String, Object>) data.get("orderById");
                    assertThat((List) query.get("orders"))
                            .filteredOn("orderId", in("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"))
                            .hasSize(10);
                })
                .thenCancel()
                .verifyLater();

        this.inputChannel.send(
                MessageBuilder
                        .withPayload(new RequestInput("{ orders { orderId, amount } }", null, Collections.emptyMap(), null, UUID.randomUUID().toString()))
                        .build()
        );
    }

    @Test
    public void testQueryWithArgument() {

        String fakeId = "1";
        double fakeAmount = 10.00;

        StepVerifier verifier = StepVerifier.create(
                        Flux.from(this.resultChannel)
                                .map(Message::getPayload)
                                .cast(RequestOutput.class)
                )
                .consumeNextWith(result -> {
                    assertThat(result).isInstanceOf(RequestOutput.class);
                    Map<String, Object> data = result.getData();
                    Map<String, Object> query = (Map<String, Object>) data.get("orderById");
                    assertThat(query.get("orderId")).isEqualTo(fakeId);
                    assertThat(query.get("amount")).isEqualTo(fakeAmount);
                })
                .thenCancel()
                .verifyLater();

        this.inputChannel.send(
                MessageBuilder
                        .withPayload(new RequestInput("{ orderById(orderId: \"" + fakeId + "\") { orderId, amount } }", null, Collections.emptyMap(), null, UUID.randomUUID().toString()))
                        .build()
        );

        verifier.verify(Duration.ofSeconds(10));

    }

    @Test
    public void testQueryWithSchemaMapping() {

        StepVerifier verifier = StepVerifier.create(
                        Flux.from(this.resultChannel)
                                .map(Message::getPayload)
                                .cast(RequestOutput.class)
                )
                .consumeNextWith(result -> {
                    assertThat(result).isInstanceOf(RequestOutput.class);
                    Map<String, Object> data = result.getData();
                    List<Map<String, Object>> customers = (List<Map<String, Object>>) data.get("customers");
                    assertThat(customers)
                            .filteredOn("customerId", in("0", "1"))
                            .hasSize(2);

                    Map<String, Object> customer0 = (Map<String, Object>) customers.get(0);
                    List<Map<String, Object>> ordersCustomer0 = (List<Map<String, Object>>) customer0.get("ordersByCustomer");
                    assertThat(ordersCustomer0)
                            .filteredOn("orderId", in("2", "4", "6", "8", "10"))
                            .hasSize(5);
                })
                .thenCancel()
                .verifyLater();

        this.inputChannel.send(
                MessageBuilder
                        .withPayload(new RequestInput("{ customers { customerId, name, ordersByCustomer { orderId } } }", null, Collections.emptyMap(), null, UUID.randomUUID().toString()))
                        .build()
        );

        verifier.verify(Duration.ofSeconds(10));

    }

    @Configuration
    @EnableIntegration
    static class TestConfig {

        @Bean
        OrderController orderController() {

            return new OrderController();
        }

        @Bean
        IntegrationFlow graphqlQueryMessageHandlerFlow(GraphQlMessageHandler handler) {

            return IntegrationFlows.from(MessageChannels.flux("inputChannel"))
                    .handle(handler)
                    .channel(c -> c.flux("resultChannel"))
                    .get();
        }

        @Bean
        GraphQlMessageHandler handler(GraphQlService graphQlService) {

            return new GraphQlMessageHandler(graphQlService);
        }

        @Bean
        GraphQlService graphQlService(GraphQlSource graphQlSource) {

            return new ExecutionGraphQlService(graphQlSource);
        }

        @Bean
        GraphQlSource graphQlSource(AnnotatedControllerConfigurer annotatedDataFetcherConfigurer) {

            return GraphQlSource.builder()
                    .schemaResources(new ClassPathResource("graphql/schema.graphqls"))
                    .configureRuntimeWiring(annotatedDataFetcherConfigurer)
                    .build();
        }

        @Bean
        AnnotatedControllerConfigurer annotatedDataFetcherConfigurer() {

            return new AnnotatedControllerConfigurer();
        }

        @Bean
        PollableChannel errorChannel() {

            return new QueueChannel();
        }

    }

}
