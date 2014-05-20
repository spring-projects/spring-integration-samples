/*
 * Copyright 2002-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.springframework.integration.samples.cafe;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import org.springframework.core.io.FileSystemResource;
import org.springframework.integration.scripting.ScriptExecutor;
import org.springframework.integration.scripting.jsr223.ScriptExecutorFactory;
import org.springframework.scripting.support.ResourceScriptSource;
/**
 * @author David Turanski
 *
 */
public class ScriptTests {
	@Test
	public void testRuby() {
		ScriptExecutor executor = ScriptExecutorFactory.getScriptExecutor("ruby");

		Order order = new Order(0);
		order.addItem(DrinkType.LATTE, 2, false);
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("payload", order.getItems().get(0));
		variables.put("timeToPrepare", 1L);

		Object obj = executor.executeScript(
				new ResourceScriptSource(new FileSystemResource("scripts/ruby/barista.rb")), variables);
		assertNotNull(obj);
		assertTrue(obj instanceof Drink);

	}

	@Test
	@Ignore
	public void testPython() {
		ScriptExecutor executor = ScriptExecutorFactory.getScriptExecutor("python");
		Order order = new Order(0);
		order.addItem(DrinkType.LATTE, 2, false);
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("payload", order.getItems().get(0));
		variables.put("timeToPrepare", "1");

		Object obj = executor.executeScript(new ResourceScriptSource(
				new FileSystemResource("scripts/python/barista.py")), variables);
		assertNotNull(obj);
		assertTrue(obj instanceof Drink);

	}
}
