/*
 * Copyright 2015 the original author or authors.
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

package org.springframework.integration.samples.kafka;

import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assume;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import org.springframework.integration.kafka.core.BrokerAddress;
import org.springframework.integration.kafka.core.ZookeeperConnectDefaults;

import com.gs.collections.impl.utility.ListIterate;

import kafka.cluster.Broker;
import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;
import scala.collection.JavaConversions;
import scala.collection.Seq;

/**
 * * A rule that prevents integration tests from failing if the Kafka server is not running or not
 * accessible. If the Kafka server is not running in the background all the tests here will simply be skipped because
 * of a violated assumption (showing as successful).
 * The rule can be declared as static so that it only has to check once for all tests in the enclosing test case, but
 * there isn't a lot of overhead in making it non-static.
 *
 * @author Dave Syer
 * @author Artem Bilan
 * @author Gary Russell
 * @author Marius Bogoevici
 * @since 4.2
 */
public class KafkaRunning extends TestWatcher {

	private static final String ZOOKEEPER_CONNECT_STRING = ZookeeperConnectDefaults.ZK_CONNECT;

	private static final Log logger = LogFactory.getLog(KafkaRunning.class);

	@Override
	public Statement apply(Statement base, Description description) {
		try {
			ZkClient zkClient = new ZkClient(ZOOKEEPER_CONNECT_STRING, 1000, 1000, ZKStringSerializer$.MODULE$);
			Seq<Broker> allBrokersInCluster = ZkUtils.getAllBrokersInCluster(zkClient);
			BrokerAddress[] brokerAddresses = ListIterate
					.collect(JavaConversions.asJavaList(allBrokersInCluster),
							broker -> new BrokerAddress(broker.host(), broker.port()))
					.toArray(new BrokerAddress[allBrokersInCluster.size()]);
			if (brokerAddresses.length == 0) {
				throw new IllegalStateException("No running Kafka brokers");
			}
		}
		catch (Exception e) {
			logger.warn("Not executing tests because basic connectivity test failed");
			Assume.assumeNoException(e);
		}

		return super.apply(base, description);
	}

}
