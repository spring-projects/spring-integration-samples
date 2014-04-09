/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.integration.samples.jms;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.context.support.GenericXmlApplicationContext;

import static org.springframework.integration.samples.jms.JmsApplicationDriver.configureApplication;

/**
 * @author Gunnar Hillert
 */
public class ChannelAdapterDemoTest {

    private final static String[] configFilesChannelAdapterDemo = {
		"/META-INF/spring/integration/common.xml",
		"/META-INF/spring/integration/inboundChannelAdapter.xml",
		"/META-INF/spring/integration/outboundChannelAdapter.xml"
	};

    public static final String PAYLOAD = "jms test";

    @Test
	public void testChannelAdapterDemo() throws InterruptedException {
        JmsApplicationDriver driver = configureApplication(configFilesChannelAdapterDemo);
        driver.send(PAYLOAD);
		Assert.assertEquals(PAYLOAD, driver.receive());
	}
}
