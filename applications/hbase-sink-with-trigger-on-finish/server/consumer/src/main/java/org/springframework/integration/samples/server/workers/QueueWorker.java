package org.springframework.integration.samples.server.workers;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.Getter;
import lombok.Setter;

import org.apache.curator.framework.CuratorFramework;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.TableNotFoundException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.support.RabbitGatewaySupport;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.integration.samples.beans.DocumentToIndex;
import org.springframework.integration.samples.beans.control.RestartMessage;
import org.springframework.integration.samples.beans.control.StopMessage;
import org.springframework.integration.samples.hbase.utils.HBaseTableUtils;
import org.springframework.integration.samples.managers.QueueFinalizer;
import org.springframework.integration.samples.server.WorkerConstants;
import org.springframework.integration.samples.zookeeper.ZkUtils;

public class QueueWorker extends RabbitGatewaySupport{

	private static final Logger LOG = Logger.getLogger(QueueWorker.class);
	
	@Getter
	@Setter
	protected AbstractMessageListenerContainer listenerContainer;

	protected HTable table;
	protected String zkNodeName;
	protected final String source;
	protected final CuratorFramework zkClient;
	protected final String familyColumn;

	public QueueWorker(String datapool, String familyColumn) {
		this(datapool,familyColumn, true);
			
		Configuration conf = HBaseConfiguration.create();
		HBaseAdmin admin = null;
		try {
			admin = new HBaseAdmin(conf);
			final byte[] tableNameBytes = Bytes.toBytes(datapool);
			try {
				admin.getTableDescriptor(tableNameBytes);
			} catch (TableNotFoundException e) {
				HBaseTableUtils.createTableIfNotExists(datapool,
						familyColumn, conf);
			} finally {
				if (admin != null)
					try {
						admin.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}

			table = new HTable(conf, datapool);
		} catch (MasterNotRunningException e) {
			e.printStackTrace();
		} catch (ZooKeeperConnectionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected QueueWorker(String datapool, String familyColumn, boolean simple) {
		this.source = datapool;
		this.familyColumn = familyColumn;
		// Comment to use external Zookeeper instead of embedded
		zkClient = WorkerConstants.getZkClient();
		zkClient.start();
		zkNodeName = ZkUtils.setValueSeq(zkClient, WorkerConstants.PERSISTENT_PATH, datapool); 
	}

	public void handleMessage(DocumentToIndex doc2index) {
		boolean stop = doc2index.isStop();
		if (stop) {
			//send stop message to topic queue
			final long startTimestamp = doc2index.getStartTimestamp();
			final StopMessage stopMsg = new StopMessage();
			stopMsg.setTimestamp(startTimestamp);
			stopMsg.setSource(doc2index.getSource().toString());
			final RabbitTemplate topicRabbitTemplate = getRabbitTemplate();
			final String rk = source + WorkerConstants.CONTROL_RK_SUFFIX;
			topicRabbitTemplate.convertAndSend(rk,stopMsg);
			LOG.debug("STOP Message for [" + doc2index.getRoutingKey() + "] sent to topic exchange");
			return;
		}

		handleMessageInternal(doc2index);
	}

	protected void handleMessageInternal(DocumentToIndex doc2index) {
		Put put = HBaseTableUtils.getDocToIndexAsPut(doc2index, familyColumn);
		if (put == null) {
			LOG.error("Obtained null put for " + doc2index);
			return;
		}
		put(put);
		LOG.debug("Message for [" + doc2index.getRoutingKey() + "] processed");
	}
	public void handleMessage(StopMessage stopMsg){
		final String zkNodeNameCopy = zkNodeName;
		zkNodeName = null;
		ExecutorService executor = Executors.newSingleThreadExecutor();
		final String persistentPath = WorkerConstants.PERSISTENT_PATH + "/" + source;
		Runnable worker = new QueueFinalizer(listenerContainer, table ,zkClient, persistentPath, zkNodeNameCopy);
		executor.execute(worker);
		executor.shutdown();
	} 
	public void handleMessage(RestartMessage restartMsg){
		if(zkNodeName!=null){
			LOG.info("Ignore restart message: this node is not waiting for restart ["+zkNodeName+"]");
			return;
		}
		zkNodeName = ZkUtils.setValueSeq(zkClient, WorkerConstants.PERSISTENT_PATH, source);
		LOG.info("Tell zookeper this node is active again ["+zkNodeName+"]");
	} 

	private synchronized void put(Put put) {
		try {
			table.put(put);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
