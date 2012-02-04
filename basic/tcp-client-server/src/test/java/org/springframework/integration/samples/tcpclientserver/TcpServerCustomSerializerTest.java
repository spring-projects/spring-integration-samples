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

import java.io.*;
import java.net.Socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author: ceposta
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/META-INF/spring/integration/tcpServerCustomSerialize-context.xml"})
public class TcpServerCustomSerializerTest {

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


        Socket socket = null;
        Writer out = null;
        BufferedReader in = null;
        try {
            socket = new Socket("localhost", 11111);
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            out.write(sourceMessage);
            out.flush();

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            StringBuffer str = new StringBuffer();

            int c;
            while ((c = in.read()) != -1) {
                str.append((char) c);
            }

            String response = str.toString();
            assertEquals(sourceMessage, response);

        } catch (IOException e) {
            fail("Test ended with an exception " + e.getMessage());
        }
        finally {
            try {
                socket.close();
                out.close();
                in.close();

            } catch (Exception e) {
                // swallow exception
            }

        }



    }
}
