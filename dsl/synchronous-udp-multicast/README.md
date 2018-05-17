Spring Integration Java DSL synchronous UDP multicast
==============

This example demonstrates the use of `Http Inbound Components`, `Http Outbound Components` and `UDP Adapters` to create a UDP multicast synchronous gateway.

## Flow

The idea is to send a UDP multicast message in an synchronous way, so, it waits until a response arrieves from any of the UDP nodes joined to the multicast group.

                Client                                          Server
    
    udpMulticastOutbound    -->>   udpMulticastInbound
                                                                        |
                                                                        |
                                                                        v
                                                                        v
          httpInbound                <<--       httpOutbound                 
    
    

## Running the sample

Run the test example.

    $ gradlew :synchronous-udp-multicast:test
