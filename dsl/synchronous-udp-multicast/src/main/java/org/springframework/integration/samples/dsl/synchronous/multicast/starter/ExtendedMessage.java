/*
 * Copyright 2014-2017 the original author or authors.
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

package org.springframework.integration.samples.dsl.synchronous.multicast.starter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Daniel Andres Pelaez Lopez
 */
public class ExtendedMessage {
    private final String replyOriginChannelId;
    private final String errorOriginChannelId;
    private final String data;

    @JsonCreator
    ExtendedMessage(@JsonProperty("replyOriginChannelId") String replyOriginChannelId,
                    @JsonProperty("errorOriginChannelId") String errorOriginChannelId,
                    @JsonProperty("data") String data) {
        this.replyOriginChannelId = replyOriginChannelId;
        this.errorOriginChannelId = errorOriginChannelId;
        this.data = data;
    }

    public String getReplyOriginChannelId() {
        return replyOriginChannelId;
    }

    public String getErrorOriginChannelId() {
        return errorOriginChannelId;
    }

    public String getData() {
        return data;
    }
}
