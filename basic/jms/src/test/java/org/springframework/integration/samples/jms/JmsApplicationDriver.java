package org.springframework.integration.samples.jms;

import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.support.MessageBuilder;

public final class JmsApplicationDriver {
    public static final String NO_REPLY = "";
    private GenericXmlApplicationContext applicationContext;

    private JmsApplicationDriver() {}

    public static JmsApplicationDriver configureApplication(String[] configFiles) {
        JmsApplicationDriver driver = new JmsApplicationDriver();
        System.setProperty("spring.profiles.active", "testCase");
        driver.applicationContext = new GenericXmlApplicationContext(configFiles);

        return driver;
    }

    public String receive() {
        final QueueChannel queueChannel = applicationContext.getBean("queueChannel", QueueChannel.class);

        @SuppressWarnings("unchecked")
        Message<String> reply = (Message<String>) queueChannel.receive(20000);

        if (reply == null) {
            return NO_REPLY;
        }

        String payload = reply.getPayload();
        applicationContext.close();

        return payload;
    }

    public void send(String payload) {
        final MessageChannel stdinToJmsoutChannel = applicationContext.getBean("stdinToJmsoutChannel", MessageChannel.class);
        stdinToJmsoutChannel.send(MessageBuilder.withPayload(payload).build());
    }
}