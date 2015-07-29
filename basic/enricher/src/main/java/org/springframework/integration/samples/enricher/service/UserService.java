/*
 * Copyright 2002-2011 the original author or authors
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package org.springframework.integration.samples.enricher.service;

import java.util.Map;

import org.springframework.integration.samples.enricher.domain.User;

/**
 * Provides user services.
 */
public interface UserService {

    /**
     * Retrieves a user based on the provided user. User object is routed to the
     * "findUserEnricherChannel" channel.
     */
    User findUser(User user);

    /**
     * Retrieves a user based on the provided user. User object is routed to the
     * "findUserByUsernameEnricherChannel" channel.
     */
	User findUserByUsername(User user);

    /**
     * Retrieves a user based on the provided username that is provided as a Map
     * entry using the mapkey 'username'. Map object is routed to the
     * "findUserWithMapChannel" channel.
     */
	Map<String, Object> findUserWithUsernameInMap(Map<String, Object> userdata);

}
