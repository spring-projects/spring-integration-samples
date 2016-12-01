/*
 * Copyright 2002-2014 the original author or authors.
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

package org.springframework.integration.samples.mongodb.util;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.springframework.util.StringUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 *
 * @author Oleg Zhurakousky
 */
public class StringConverter extends MappingMongoConverter {

	public StringConverter(
			MongoDbFactory mongoDbFactory,
			MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext) {
		super(new DefaultDbRefResolver(mongoDbFactory), mappingContext);
	}

	@Override
	public void write(Object source, Bson target) {
		Document document = (Document) target;
		String strPerson = (String) source;
		String[] parsedStrPerson = StringUtils.tokenizeToStringArray(strPerson, ",");
		document.put("fname", parsedStrPerson[0]);
		document.put("lname", parsedStrPerson[1]);
		DBObject innerObject = new BasicDBObject();
		innerObject.put("city", parsedStrPerson[2]);
		innerObject.put("street", parsedStrPerson[3]);
		innerObject.put("zip", parsedStrPerson[4]);
		innerObject.put("state", parsedStrPerson[5]);
		document.put("address", innerObject);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S> S read(Class<S> clazz, Bson source) {
		Document document = (Document) source;
		return (S) ((document.get("fname") + ", ")
				+ document.get("lname") + ", "
				+ document.get("city") + ", "
				+ document.get("street") + ", "
				+ document.get("zip") + ", "
				+ document.get("state") + ", ");
	}

}

