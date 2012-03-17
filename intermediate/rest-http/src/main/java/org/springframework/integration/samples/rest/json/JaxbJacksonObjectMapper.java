/*
 * Copyright 2002-2010 the original author or authors.
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
package org.springframework.integration.samples.rest.json;

import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;

/**
 * JaxbJacksonObjectMapper.java: This is the custom JAXB JSON ObjectMapper
 * <p>
 * NOTE: The source code is provided by Gunnar Hillert in his blog posted at
 * http://hillert.blogspot.com/2011/01/marshal-json-data-using-jackson-in.html.
 * I modified a little bit to use the latest {@link DeserializationConfig} API
 * instead of deprecated ones.
 * <p> *  
 * @author Vigil Bose
 */
public class JaxbJacksonObjectMapper extends ObjectMapper {

	/**
	 * Annotation introspector to use for serialization process 
	 * is configured separately for serialization and deserialization purposes
	 */
	public JaxbJacksonObjectMapper() {
		  final AnnotationIntrospector introspector
		      = new JacksonAnnotationIntrospector();
		  super.getDeserializationConfig()
		       .withAnnotationIntrospector(introspector);
		  super.getSerializationConfig()
		       .withAnnotationIntrospector(introspector);
		  
	}
}
