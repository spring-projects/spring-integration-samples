/*
 * Copyright 2016-2018 the original author or authors.
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

package org.springframework.integration.samples.dsl.synchronous.multicast;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.context.IntegrationFlowContext;
import org.springframework.integration.samples.dsl.synchronous.multicast.starter.SynchronousUDPStarter;

/**
 * @author Daniel Andres Pelaez Lopez
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public SynchronousUDPStarter synchronousUDPStarter(IntegrationFlowContext flowContext,
                                                       @Value("${synchronous.multicast.group}") String group,
                                                       @Value("${synchronous.multicast.port}") Integer port) {
        SynchronousUDPStarter synchronousUDPStarter = new SynchronousUDPStarter(flowContext, group, port);

        synchronousUDPStarter.init();

        return synchronousUDPStarter;
    }

}
