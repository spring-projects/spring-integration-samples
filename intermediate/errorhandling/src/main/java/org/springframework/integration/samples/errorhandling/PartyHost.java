/* Copyright 2002-2017 the original author or authors.
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

package org.springframework.integration.samples.errorhandling;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.integration.annotation.MessageEndpoint;

/**
 * Sends invitations to <code>PartyGuest</code>s and likes to be notified of
 * delivery failures of course.
 *
 * @author Iwein Fuld
 * @author Gary Russell
 */
@MessageEndpoint
public class PartyHost {
	private final Log logger = LogFactory.getLog(PartyHost.class);
	private final AtomicInteger counter = new AtomicInteger(0);

	public Invitation nextInvitation() {
		return new Invitation(counter.incrementAndGet());
	}

	public void onInvitationFailed(Invitation inv) {
		logger.info("Host received failure notification for: " + inv);
	}
}
