/*
 * Copyright 2016 the original author or authors.
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

package org.springframework.integration.samples.jpa;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.OpenJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.util.ClassUtils;

/**
 * @author Artem Bilan
 * @since 4.2
 */
@Configuration
@ConditionalOnClass({LocalContainerEntityManagerFactoryBean.class,
		EnableTransactionManagement.class, EntityManager.class})
@Conditional(OpenJpaAutoConfiguration.OpenJpaEntityManagerCondition.class)
@AutoConfigureAfter({DataSourceAutoConfiguration.class})
@Profile("openJpa")
public class OpenJpaAutoConfiguration extends JpaBaseConfiguration {

	public OpenJpaAutoConfiguration(DataSource dataSource, JpaProperties properties,
			ObjectProvider<JtaTransactionManager> jtaTransactionManagerProvider) {
		super(dataSource, properties, jtaTransactionManagerProvider);
	}

	@Override
	protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
		return new OpenJpaVendorAdapter();
	}

	@Override
	protected Map<String, Object> getVendorProperties() {
		return new HashMap<String, Object>();
	}


	@Order(Ordered.HIGHEST_PRECEDENCE + 20)
	static class OpenJpaEntityManagerCondition extends SpringBootCondition {

		@Override
		public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
			if (ClassUtils.isPresent("org.apache.openjpa.persistence.OpenJPAEntityManager", context.getClassLoader())) {
				return ConditionOutcome.match("found OpenJPAEntityManager class");
			}
			else {
				return ConditionOutcome.noMatch("did not find OpenJPAEntityManager class");
			}
		}

	}

}
