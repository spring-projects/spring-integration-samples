package org.springframework.integration.samples.multitcp;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

@MessageEndpoint
public class ServerEndpoint {
	
    @ServiceActivator(inputChannel="toSA")
	public Msg test(Msg h) {
		System.out.println("Hello, client : " + h.getId() + " .");
		h.setData("OK");
		return h;
	}

}