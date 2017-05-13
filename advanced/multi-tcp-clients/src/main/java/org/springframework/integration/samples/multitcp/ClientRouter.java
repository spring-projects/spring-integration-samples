package org.springframework.integration.samples.multitcp;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Router;

@MessageEndpoint
public class ClientRouter {
	
	@Router(inputChannel="input")
	public String resolveComChannel(Msg payload) {
		return "input-" + payload.getId();
	}
}
