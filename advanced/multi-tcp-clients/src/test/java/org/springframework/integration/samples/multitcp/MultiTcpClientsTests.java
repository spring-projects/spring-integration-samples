package org.springframework.integration.samples.multitcp;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.samples.multitcp.ClientsInitPostProcessor.SimpleGateway;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = App.class)
@SpringBootTest
public class MultiTcpClientsTests {

    @Autowired
    private  SimpleGateway gateway;
    
	@Test
	public void test() {
		Msg h1 = new Msg();
		h1.setId(1);
		h1 = this.gateway.send(h1);
		Assert.assertNotNull(h1);
		Assert.assertNotNull(h1.getData());
		
		Msg h2 = new Msg();
		h2.setId(2);
		h2 = this.gateway.send(h2);
		Assert.assertNotNull(h2);
		Assert.assertNotNull(h2.getData());
		
	}

}
