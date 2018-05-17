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
import org.springframework.integration.ip.IpHeaders;
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
    private static final String REPLY_ORIGINAL_CHANNEL_ID_HEADER = "replyOriginChannelId";
    private static final String ERROR_ORIGINAL_CHANNEL_ID_HEADER = "errorOriginChannelId";
    private static final String BASE_URL = "http://{host}:8080/";
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
        //UDPMulticastGateway is the gateway through we are going to consume this flow
        return IntegrationFlows.from(UDPMulticastGateway.class)
                .enrichHeaders(m -> m
                        .header(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON_VALUE))
                //Persist the replayChannel and  errorChannel headers, getting IDs
                .enrichHeaders(HeaderEnricherSpec::headerChannelsToString)
                //Transform the message adding the replayChannel and errorChannel IDs as a part of the payload
                //To retrieve later after the response arrives
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
                //Add new headers to the Message named replyOriginChannelId and errorOriginChannelId from the payload we receive
                .enrichHeaders(h -> h
                        .headerFunction(REPLY_ORIGINAL_CHANNEL_ID_HEADER, m -> ((ExtendedMessage) m.getPayload()).getReplyOriginChannelId())
                        .headerFunction(ERROR_ORIGINAL_CHANNEL_ID_HEADER, m -> ((ExtendedMessage) m.getPayload()).getErrorOriginChannelId()))
                //Get the read data we want to handle
                .transform(ExtendedMessage::getData)
                .handle(new UDPMulticastHandler(), "handle")
                //Publish a Message response using the httpOutbound channel
                .publishSubscribeChannel(p -> p
                        .subscribe(s -> s
                                //The inboundMulticastAdapter does not have replyChannel and errorChannel by default
                                //So, we add a NullChannel where httpOutbound is going to respond
                                //We do not care about if this request is successful, this communication flow is still unreliable
                                .enrichHeaders(h -> h
                                        .header(MessageHeaders.REPLY_CHANNEL, new NullChannel(), true)
                                        .header(MessageHeaders.ERROR_CHANNEL, new NullChannel(), true))
                                //Send the message to httpOutbound channel
                                .channel(HTTP_OUTBOUND_CHANNEL)))
                .get();
    }

    private StandardIntegrationFlow getHttpOutboundFlow() {
        return IntegrationFlows.from(HTTP_OUTBOUND_CHANNEL)
                .enrichHeaders(m -> m
                        .header(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON_VALUE))
                .handle(Http.outboundGateway(BASE_URL + MESSAGES_PATH)
                        .httpMethod(HttpMethod.POST)
                        //The headers we created in the UDPInboundFlow are mapped here to headers in the HTTP Request
                        //So, we can retrieve those from the request origin side
                        .mappedRequestHeaders(REPLY_ORIGINAL_CHANNEL_ID_HEADER, ERROR_ORIGINAL_CHANNEL_ID_HEADER)
                        .expectedResponseType(String.class)
                        //Get the request origin IP to response
                        .uriVariable("host", m -> m.getHeaders().get(IpHeaders.HOSTNAME)))
                .get();
    }

    private StandardIntegrationFlow getHttpInboundFlow() {
        return IntegrationFlows.from(
                Http.inboundGateway(MESSAGES_PATH)
                        .requestMapping(m -> m.methods(HttpMethod.POST)
                                .consumes(MimeTypeUtils.APPLICATION_JSON_VALUE)
                                .produces(MimeTypeUtils.APPLICATION_JSON_VALUE))
                        //The headers we created in the HttpOutboundFlow in the HTTP Request are mapped here to headers in the Message
                        //The headers names are in lower case
                        .mappedRequestHeaders(REPLY_ORIGINAL_CHANNEL_ID_HEADER, ERROR_ORIGINAL_CHANNEL_ID_HEADER)
                        .requestPayloadType(String.class))
                //Publish the Message to two subscribers
                .publishSubscribeChannel(p -> p
                        //First subscriber: response to HttpInboundFlow
                        //The Message with the default replyChannel and errorChannel are redirect using a bridge
                        //The bridge resolves those headers and uses those channels
                        .subscribe(IntegrationFlowDefinition::bridge)

                        //Second subscriber: response to UDPMulticastGateway (UDPOutboundFlow)
                        //The replyChannel and errorChannel headers are replaced with the replyOriginChannelId and errorOriginChannelId we sent at the beginning
                        //The bridge resolves those headers and uses those channels to unblock the UDPMulticastGateway
                        .subscribe(sub -> sub
                                .enrichHeaders(h -> h
                                        .headerFunction(MessageHeaders.REPLY_CHANNEL, m -> m
                                                .getHeaders().get(REPLY_ORIGINAL_CHANNEL_ID_HEADER.toLowerCase()), true)
                                        .headerFunction(MessageHeaders.ERROR_CHANNEL, m -> m
                                                .getHeaders().get(ERROR_ORIGINAL_CHANNEL_ID_HEADER.toLowerCase()), true))
                                .bridge())
                )
                .get();
    }
}
