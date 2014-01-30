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

package org.springframework.integration.sts;

import org.junit.Ignore;
import org.junit.Test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.service.StringConversionService;

/**
 * Verify that the Spring Integration Application Context starts successfully.
 */
@Ignore
public class StringConversionServiceTest {

    @Test
    public void testStartupOfSpringInegrationContext() throws Exception{
        final ApplicationContext context
            = new ClassPathXmlApplicationContext("/META-INF/spring/integration/spring-integration-context.xml",
                                                  StringConversionServiceTest.class);
        Thread.sleep(2000);
    }

    @Test
    public void testConvertStringToUpperCase() {
        final ApplicationContext context
            = new ClassPathXmlApplicationContext("/META-INF/spring/integration/spring-integration-context.xml",
                                                  StringConversionServiceTest.class);

        final StringConversionService service = context.getBean(StringConversionService.class);

        final String stringToConvert = "I love Spring Integration";
        final String expectedResult  = "I LOVE SPRING INTEGRATION";

      //  final String convertedString = service.convertToUpperCase(stringToConvert);

      //  assertEquals("Expecting that the string is converted to upper case.",
      //dd                expectedResult, convertedString);

    }

}
