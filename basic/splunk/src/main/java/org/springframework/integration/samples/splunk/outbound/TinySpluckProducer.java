package org.springframework.integration.samples.splunk.outbound;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.samples.splunk.model.OrderSuspectEvent;
import org.springframework.integration.splunk.event.SplunkEvent;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;


public class TinySpluckProducer
{

    private static final String CONFIG = "si-splunk-producer-context.xml";

    private static final Logger log = LoggerFactory.getLogger(TinySpluckProducer.class);

    public static void main(final String args[])
    {
        final ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(CONFIG, TinySpluckProducer.class);
        ctx.start();
        final MessageChannel channelRestoutput = ctx.getBean("outputToSplunk", MessageChannel.class);
        final MessageChannel channelStreamoutput = ctx.getBean("outputToSplunkIndex", MessageChannel.class);
        
        sendWithRest(channelRestoutput);
        
        sendWithStream(channelStreamoutput);

        log.info("Done");

    }
    
    
    private static void sendWithRest( MessageChannel channel)
    {
        log.info("Class {}", channel.getClass());
        ConvertToTopicAndPartitionFunction convertToTopicAndPartitionFunction = new ConvertToTopicAndPartitionFunction();
        IntStream.range(1, 5000).forEach(i -> {
            channel.send(MessageBuilder.withPayload(convertToTopicAndPartitionFunction.apply(i)).build());
            log.info("Message Sent to splunk" + i);
        });
        log.info("Done");
    }
    
    
    private static void sendWithStream( MessageChannel channel)
    {
        log.info("Class {}", channel.getClass());
        ConvertToTopicAndPartitionFunction convertToTopicAndPartitionFunction = new ConvertToTopicAndPartitionFunction();
        IntStream.range(1, 5000).forEach(i -> {
            channel.send(MessageBuilder.withPayload(convertToTopicAndPartitionFunction.apply(i)).build());
            log.info("Message Sent to splunk" + i);
        });
        log.info("Done");
    }
    
    
    

    private static class ConvertToTopicAndPartitionFunction implements Function<Integer, SplunkEvent>
    {

        Random randomGenerator = new Random();

        /**
         * {@inheritDoc}
         */
        @Override
        public SplunkEvent apply(Integer t)
        {
            OrderSuspectEvent sd = new OrderSuspectEvent();
            sd.setEan(String.valueOf(t * randomGenerator.nextInt(100)));
            sd.setCommonSeverity("ALERT");
            sd.setEmailuser("nobody@gmail.com");
            sd.setOrderNumber("21501001010101");
            return sd;
        }

    }

}
