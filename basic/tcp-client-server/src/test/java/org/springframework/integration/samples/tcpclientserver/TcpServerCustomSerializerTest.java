package org.springframework.integration.samples.tcpclientserver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.core.SubscribableChannel;
import org.springframework.integration.handler.AbstractReplyProducingMessageHandler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author: ceposta
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/META-INF/spring/integration/tcpServerCustomSerialize-context.xml"})
public class TcpServerCustomSerializerTest {

    @Autowired
	CustomSimpleGateway gw;

    @Autowired
    @Qualifier("incomingServerChannel")
    MessageChannel incomingServerChannel;

    @Test
    public void testHappyPath() {

        // add a listener to this channel, otherwise there is not one defined
        // the reason we use a listener here is so we can assert truths on the
        // message and/or payload
        SubscribableChannel channel = (SubscribableChannel) incomingServerChannel;
        channel.subscribe(new AbstractReplyProducingMessageHandler(){

            @Override
            protected Object handleRequestMessage(Message<?> requestMessage) {
                CustomOrder payload = (CustomOrder) requestMessage.getPayload();

                // we assert during the processing of the messaging that the
                // payload is just the content we wanted to send without the
                // framing bytes (STX/ETX)
                assertEquals(123, payload.getNumber());
                assertEquals("PINGPONG02", payload.getSender());
                assertEquals("You got it to work!", payload.getMessage());
                return requestMessage;
            }
        });

        String sourceMessage = "123PINGPONG02000019You got it to work!";
        CustomOrder result = gw.send(sourceMessage);
        System.out.println(result);
        assertEquals(sourceMessage, result);


    }
}
