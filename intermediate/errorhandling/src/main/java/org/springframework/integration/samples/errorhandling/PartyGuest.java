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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.integration.annotation.MessageEndpoint;

/**
 * This guest receives invitations, but returns them to sender with a wrong
 * address exception message.
 *
 * @author Iwein Fuld
 * @author Gary Russell
 */
@MessageEndpoint
public class PartyGuest {
	private final Log logger = LogFactory.getLog(PartyGuest.class);

	public void onInvitation(Invitation invitation) {
		logger.info("Guest is throwing exception");
		throw new RuntimeException("Wrong address");
	}

}
