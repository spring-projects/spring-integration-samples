package org.springframework.integration.samples.test.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.integration.samples.zookeeper.ZkCachedListener;
import org.springframework.integration.samples.zookeeper.ZkUtils;

import com.google.common.io.Closeables;

public class TestZkCachedListener {
	
	private static final Logger LOG = Logger.getLogger(TestZkCachedListener.class);
	private static final int ZK_PORT = 2181;
	private static final String CONNECTION_URL = "localhost:"+ZK_PORT;
	private static final String PERSISTENT_PATH = "/example/cache";
	private CuratorFramework client;
	private TestingServer server;

	@Before
	public void init() throws Exception {
		// create temporary zookeeper dir in /tmp on port 2281
		server = new TestingServer(ZK_PORT);
		final int maxRetries = 3;
		final int baseSleepTimeMs = 1000;
		String connectionUrl = CONNECTION_URL;
		// Comment to use external Zookeeper instead of embedded
		connectionUrl = server.getConnectString();
		client = CuratorFrameworkFactory.newClient(connectionUrl, 
				new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries));
		client.start();
	}
	
	@Override
	@SuppressWarnings("deprecation")
	@After
	public void finalize() throws Exception {
		if (server!=null)
			Closeables.closeQuietly(server);
	}
	
	
	@SuppressWarnings("deprecation")
	@Test
	public void test() throws InterruptedException {
		final ZkCachedListener l = new ZkCachedListener(client, PERSISTENT_PATH);
		l.startListening();
		ZkUtils.setValue(client, PERSISTENT_PATH, "zNode1", "somevalue");
		
		//get count immediately and 5 secs after stop
		getActiveNodesCount(l);
		l.stopListening();
		Thread.sleep(5 * 1000);
		getActiveNodesCount(l);

		Closeables.closeQuietly(client);
		
	}

	private void getActiveNodesCount(final ZkCachedListener l) throws InterruptedException {
		Thread.sleep(1 * 1000);
		int count = l.getChildrenCount();
		LOG.info("Found "+count+" active zNodes");
	}
	private void getActiveNodesCount(String path) throws Exception {
		Thread.sleep(1 * 1000);
		int count =client.getChildren().forPath(path).size();
		LOG.info("Found "+count+" active zNodes");
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testAddAndRemoveNode() throws Exception {
		ZkUtils.setValue(client, PERSISTENT_PATH, "zNode1", "somevalue");
		getActiveNodesCount(PERSISTENT_PATH);
		
		ZkUtils.remove(client, PERSISTENT_PATH, "zNode1");
		
		Thread.sleep(5 * 1000);
		getActiveNodesCount(PERSISTENT_PATH);
		
		Closeables.closeQuietly(client);
		
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testAddAndRemoveNodeSeq() throws Exception {
		String nodeName = ZkUtils.setValueSeq(client, PERSISTENT_PATH, "zNode1", "somevalue");
		getActiveNodesCount(PERSISTENT_PATH);
		
		ZkUtils.remove(client, PERSISTENT_PATH + "/zNode1",  nodeName);
		
		Thread.sleep(5 * 1000);
		getActiveNodesCount(PERSISTENT_PATH);
		
		Closeables.closeQuietly(client);
		
	}
	
}
