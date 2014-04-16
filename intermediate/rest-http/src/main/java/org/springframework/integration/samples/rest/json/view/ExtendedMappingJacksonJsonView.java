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
package org.springframework.integration.samples.rest.json.view;

import java.util.Map;

import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 * ExtendedMappingJacksonJsonView.java: This class extends the Spring's MappingJacksonJsonView
 * <p>
 * Note: The source code for this class is taken from the forum posted by AhungerArtist
 *       at http://forum.springsource.org/archive/index.php/t-84006.html
 * <p>
 * @author Vigil Bose
 * @author Gary Russell
 */
public class ExtendedMappingJacksonJsonView extends MappingJackson2JsonView {

	@SuppressWarnings({"rawtypes" })
	@Override
	protected Object filterModel(Map<String, Object> model){
		Object result = super.filterModel(model);
		if (!(result instanceof Map)){
			return result;
		}

		Map map = (Map) result;
		if (map.size() == 1){
			return map.values().toArray()[0];
		}
		return map;
	}
}


