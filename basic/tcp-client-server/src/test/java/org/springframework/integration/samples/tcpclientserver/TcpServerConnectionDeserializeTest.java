package org.springframework.integration.samples.tcpclientserver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.MessagingException;
import org.springframework.integration.core.MessageHandler;
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
        SubscribableChannel channel = (SubscribableChannel) incomingServerChannel;
        channel.subscribe(new AbstractReplyProducingMessageHandler(){

            @Override
            protected Object handleRequestMessage(Message<?> requestMessage) {
                System.out.println("hello!");
                return requestMessage;
            }
        });

        String sourceMessage = wrapWithStxEtx("Hello World");

        String result = gw.send(sourceMessage);
        System.out.println(result);
        assertEquals("echo:Hello world!", result);
    }

    private String wrapWithStxEtx(String s) {
        StringWriter writer = new StringWriter();
        writer.write(ByteArrayStxEtxSerializer.STX);
        writer.write(s);
        writer.write(ByteArrayStxEtxSerializer.ETX);
        return writer.toString();
    }
}
