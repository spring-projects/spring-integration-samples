package org.springframework.integration.samples.splunk.inbound;

import org.springframework.context.support.ClassPathXmlApplicationContext;


public class TinySpluckConsumer
{

    private static final String CONFIG = "si-splunk-consumer-context.xml";


    public static void main(final String args[])
    {
        final ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(CONFIG, TinySpluckConsumer.class);
        ctx.start();

    }


}
