/*
 * Copyright 2002-2019 the original author or authors.
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

package org.springframework.integration.samples.mongodb.util;

import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import com.mongodb.client.MongoClients;

/**
 *
 * @author Oleg Zhurakousky
 */
public class DemoUtils {

	public static MongoDatabaseFactory prepareMongoFactory(String... additionalCollectionToDrop) throws Exception {
		MongoDatabaseFactory mongoDbFactory = new SimpleMongoClientDatabaseFactory(MongoClients.create(), "test");
		MongoTemplate template = new MongoTemplate(mongoDbFactory);
		template.dropCollection("messages");
		template.dropCollection("data");
		for (String additionalCollection : additionalCollectionToDrop) {
			template.dropCollection(additionalCollection);
		}
		return mongoDbFactory;
	}

}
