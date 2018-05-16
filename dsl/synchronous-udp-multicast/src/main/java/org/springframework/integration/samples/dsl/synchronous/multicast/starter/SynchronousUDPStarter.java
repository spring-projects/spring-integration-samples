/*
 * Copyright 2014-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.integration.samples.dsl.synchronous.multicast.starter;

import org.springframework.http.HttpMethod;
import org.springframework.integration.channel.NullChannel;
import org.springframework.integration.dsl.*;
import org.springframework.integration.dsl.context.IntegrationFlowContext;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.ip.dsl.Udp;
import org.springframework.integration.samples.dsl.synchronous.multicast.gateway.UDPMulticastGateway;
import org.springframework.integration.samples.dsl.synchronous.multicast.handler.UDPMulticastHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.util.MimeTypeUtils;

/**
 * @author Daniel Andres Pelaez Lopez
 */
public class SynchronousUDPStarter {
    private static final String MESSAGES_PATH = "messages/";
    private static final String REPLY_CHANNEL_ID_HEADER = "replyChannelId";
    private static final String ERROR_CHANNEL_ID_HEADER = "errorChannelId";
    private static final String BASE_URL = "http://localhost:8080/";
    private static final String HTTP_OUTBOUND_CHANNEL = "httpOutbound";
    private final IntegrationFlowContext flowContext;
    private final String group;
    private final Integer port;

    public SynchronousUDPStarter(IntegrationFlowContext flowContext, String group, Integer port) {
        this.flowContext = flowContext;
        this.group = group;
        this.port = port;
    }

    public void init() {
        StandardIntegrationFlow httpInbound = getHttpInboundFlow();

        StandardIntegrationFlow httpOutbound = getHttpOutboundFlow();

        StandardIntegrationFlow udpInbound = getUDPInboundFlow();

        StandardIntegrationFlow udpOutbound = getUDPOutboundFlow();

        flowContext.registration(httpInbound).id("httpInboundFlow").register();
        flowContext.registration(httpOutbound).id("httpOutboundFlow").register();
        flowContext.registration(udpInbound).id("udpInboundFlow").register();
        flowContext.registration(udpOutbound).id("udpOutboundFlow").register();

        httpInbound.start();
        httpOutbound.start();
        udpInbound.start();
        udpOutbound.start();
    }

    private StandardIntegrationFlow getUDPOutboundFlow() {
        return IntegrationFlows.from(UDPMulticastGateway.class)
                .enrichHeaders(m -> m
                        .header(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON_VALUE))
                .enrichHeaders(HeaderEnricherSpec::headerChannelsToString)
                .transform(new ExtendedMessageTransformer())
                .transform(Transformers.toJson())
                .handle(Udp.outboundMulticastAdapter(group, port))
                .get();
    }

    private StandardIntegrationFlow getUDPInboundFlow() {
        return IntegrationFlows.from(Udp.inboundMulticastAdapter(port, group))
                .enrichHeaders(m -> m
                        .header(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON_VALUE))
                .transform(Transformers.fromJson(ExtendedMessage.class))
                .enrichHeaders(h -> h
                        .headerFunction(REPLY_CHANNEL_ID_HEADER, m -> ((ExtendedMessage) m.getPayload()).getReplyChannelId())
                        .headerFunction(ERROR_CHANNEL_ID_HEADER, m -> ((ExtendedMessage) m.getPayload()).getErrorChannelId()))
                .transform(ExtendedMessage::getData)
                .handle(new UDPMulticastHandler(), "handle")
                .publishSubscribeChannel(p -> p
                        .subscribe(s -> s
                                .enrichHeaders(h -> h
                                        .header(MessageHeaders.REPLY_CHANNEL, new NullChannel(), true)
                                        .header(MessageHeaders.ERROR_CHANNEL, new NullChannel(), true))
                                .channel(HTTP_OUTBOUND_CHANNEL)))
                .get();
    }

    private StandardIntegrationFlow getHttpOutboundFlow() {
        return IntegrationFlows.from(HTTP_OUTBOUND_CHANNEL)
                .enrichHeaders(m -> m
                        .header(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON_VALUE))
                .handle(Http.outboundGateway(BASE_URL + MESSAGES_PATH)
                        .httpMethod(HttpMethod.POST)
                        .mappedRequestHeaders(REPLY_CHANNEL_ID_HEADER, ERROR_CHANNEL_ID_HEADER)
                        .expectedResponseType(String.class))
                .get();
    }

    private StandardIntegrationFlow getHttpInboundFlow() {
        return IntegrationFlows.from(
                Http.inboundGateway(MESSAGES_PATH)
                        .requestMapping(m -> m.methods(HttpMethod.POST)
                                .consumes(MimeTypeUtils.APPLICATION_JSON_VALUE)
                                .produces(MimeTypeUtils.APPLICATION_JSON_VALUE))
                        .mappedRequestHeaders(REPLY_CHANNEL_ID_HEADER, ERROR_CHANNEL_ID_HEADER)
                        .requestPayloadType(String.class))
                .publishSubscribeChannel(p -> p
                        .subscribe(IntegrationFlowDefinition::bridge)
                        .subscribe(sub -> sub
                                .enrichHeaders(h -> h
                                        .headerFunction(MessageHeaders.REPLY_CHANNEL, m -> m
                                                .getHeaders().get(REPLY_CHANNEL_ID_HEADER.toLowerCase()), true)
                                        .headerFunction(MessageHeaders.ERROR_CHANNEL, m -> m
                                                .getHeaders().get(ERROR_CHANNEL_ID_HEADER.toLowerCase()), true))
                                .bridge())
                )
                .get();
    }
}
