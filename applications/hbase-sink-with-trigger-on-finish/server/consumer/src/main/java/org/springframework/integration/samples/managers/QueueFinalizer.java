package org.springframework.integration.samples.managers;

import java.io.IOException;

import org.apache.curator.framework.CuratorFramework;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.integration.samples.zookeeper.ZkUtils;

public class QueueFinalizer implements Runnable {

	private static final Logger LOG = Logger.getLogger(QueueFinalizer.class);
	private final AbstractMessageListenerContainer listenerContainer;
	private final HTable inputTableName;
	private final String zkPersistentPath;
	private final String zkNodeName;
	private final CuratorFramework zkClient;
	
	public QueueFinalizer(AbstractMessageListenerContainer l, HTable table, 
			CuratorFramework zkClient, String zkPersistentPath, String zkNodeName) {
		this.listenerContainer = l;
		this.inputTableName = table;
		this.zkNodeName = zkNodeName;
		this.zkPersistentPath = zkPersistentPath;
		this.zkClient = zkClient;
	}

	@Override
	public void run() {
		LOG.info("Waiting for this consumer threads to stop ["+zkNodeName+"]");
		listenerContainer.stop();
		flushCommits();
		String infoMsg = "Tell zookeper to remove this node [%s/%s]";
		infoMsg = String.format(infoMsg, zkPersistentPath,zkNodeName);
		LOG.info(infoMsg);
		ZkUtils.remove(zkClient, zkPersistentPath, zkNodeName);
		//restart listening on the queue
		listenerContainer.start();
	}

	private void flushCommits() {
		if(inputTableName==null)
			return;
		try {
			inputTableName.flushCommits();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
