package org.springframework.integration.samples.dsl.webflux;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.reactivestreams.Publisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.webflux.dsl.WebFlux;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;


@SpringBootApplication
public class WebFluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebFluxApplication.class, args);
    }

    @Bean
    public Publisher<Message<JsonNode>> reactiveSource() {
        return IntegrationFlows.
                from(WebFlux.inboundChannelAdapter("/messages")
                        .requestMapping(r -> r
                                .methods(HttpMethod.POST)
                        )
                        .requestPayloadType(JsonNode.class)
                )
                .split()
                .channel(MessageChannels.flux())
                .<TextNode, String>route(o -> o.asText(),
                        m -> m.defaultOutputToParentFlow()
                                .subFlowMapping("latte macchiato", f -> f.handle((p, h) -> p))
                                .subFlowMapping("caffe", f -> f.handle((p, h) -> p)))
                .toReactivePublisher();
    }


    @Bean
    public IntegrationFlow eventMessages() {
        return IntegrationFlows
                .from(WebFlux.inboundGateway("/events")
                        .requestMapping(m -> m.produces(MediaType.TEXT_EVENT_STREAM_VALUE)))
                .handle((p, h) -> Flux.from(reactiveSource())
                        .map(Message::getPayload))
                .get();
    }

}
