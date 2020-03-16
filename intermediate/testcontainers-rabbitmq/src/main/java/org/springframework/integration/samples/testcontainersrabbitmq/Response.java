/*
 * Copyright 2002-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.integration.samples.testcontainersrabbitmq;

import java.util.UUID;

public class Response {

	private final UUID requestId;

	private final String message;

	public Response(UUID requestId, String message) {
		this.requestId = requestId;
		this.message = message;
	}

	public UUID getRequestId() {
		return this.requestId;
	}

	public String getMessage() {
		return this.message;
	}

	@Override
	public String toString() {
		return "Response{" +
				"requestId=" + this.requestId +
				", message='" + this.message + '\'' +
				'}';
	}

}
