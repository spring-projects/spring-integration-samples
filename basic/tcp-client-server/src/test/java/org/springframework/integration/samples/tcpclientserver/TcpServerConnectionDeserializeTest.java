package org.springframework.integration.samples.tcpclientserver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.core.SubscribableChannel;
import org.springframework.integration.handler.AbstractReplyProducingMessageHandler;
import org.springframework.integration.ip.tcp.serializer.ByteArrayStxEtxSerializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

/**
 * @author: ceposta
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/META-INF/spring/integration/tcpServerConnectionDeserialize-context.xml"})
public class TcpServerConnectionDeserializeTest {

    @Autowired
	SimpleGateway gw;

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
                byte[] payload = (byte[]) requestMessage.getPayload();

                // we assert during the processing of the messaging that the
                // payload is just the content we wanted to send without the
                // framing bytes (STX/ETX)
                assertEquals("Hello World!", new String(payload));
                return requestMessage;
            }
        });

        String sourceMessage = wrapWithStxEtx("Hello World!");
        String result = gw.send(sourceMessage);
        System.out.println(result);
        assertEquals("Hello World!", result);
    }

    /**
     * Show, explicitly, how the stream would look if you had to manually create it.
     *
     * See more about TCP synchronous communication for more about framing the stream
     * with STX/ETX:  http://en.wikipedia.org/wiki/Binary_Synchronous_Communications
     *
     * @param content
     * @return a string that is wrapped with the STX/ETX framing bytes
     */
    private String wrapWithStxEtx(String content) {
        StringWriter writer = new StringWriter();
        writer.write(ByteArrayStxEtxSerializer.STX);
        writer.write(content);
        writer.write(ByteArrayStxEtxSerializer.ETX);
        return writer.toString();
    }
}
