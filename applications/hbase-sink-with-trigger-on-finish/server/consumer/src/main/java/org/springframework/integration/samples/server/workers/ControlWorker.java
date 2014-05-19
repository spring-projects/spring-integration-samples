package org.springframework.integration.samples.server.workers;

import java.io.IOException;
import java.util.Date;

import org.apache.curator.framework.CuratorFramework;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.support.RabbitGatewaySupport;
import org.springframework.integration.samples.beans.control.RestartMessage;
import org.springframework.integration.samples.beans.control.StopMessage;
import org.springframework.integration.samples.hbase.jobs.SampleJob;
import org.springframework.integration.samples.hbase.utils.HBaseTableUtils;
import org.springframework.integration.samples.server.WorkerConstants;
import org.springframework.integration.samples.server.workers.beans.QueueWorkerParams;

public class ControlWorker extends RabbitGatewaySupport {

	private static final Logger LOG = Logger.getLogger(ControlWorker.class);
	protected final CuratorFramework zKclient;
	private final QueueWorkerParams params;
	
	public ControlWorker(QueueWorkerParams params) {
		zKclient = WorkerConstants.getZkClient();
		zKclient.start();
		this.params = params;
	}

	public void handleMessage(StopMessage stopMessage) {
		handleStop(stopMessage);		
	}

	private synchronized void handleStop(StopMessage stopMessage) {
		String destQueue = getDestinationQueue(stopMessage.getSource());
		try {
			waitForAllConsumersStop(destQueue);
			handleStopInternal(stopMessage);
		} catch (Exception e) {
			LOG.error(e);
		}
		LOG.info("HandleStop finished. Send restart message to consumers of "+destQueue);
		final String rk = destQueue + WorkerConstants.RESTART_RK_SUFFIX;
		getRabbitTemplate().convertAndSend(rk,new RestartMessage());
	}

	protected String getDestinationQueue(String datapool) {
		return datapool;
	}

	protected void handleStopInternal(StopMessage stopMessage) {
		LOG.info("All consumers stopped. Starting data transformation (e.g. indexing on Solr or whatever)");
		long startTimestamp = stopMessage.getTimestamp();
		final String datapool = stopMessage.getSource();
		triggerDataTransformation(startTimestamp,datapool);
	}

	private void waitForAllConsumersStop(String datapool) throws Exception {
		int timeout = WorkerConstants.DEFAULT_TIMEOUT;
		//wait until timeout or all consumer detached
		while (getActiveConsumersCount(datapool)  > 0 || timeout == 0) {
			timeout--;
			LOG.info("Active consumers poll timeout: "+timeout);
			sleep1second();
		}
	}

	private int getActiveConsumersCount(String datapool) throws Exception {
		String persistentPath = WorkerConstants.PERSISTENT_PATH + "/" +datapool;
		final int count = zKclient.checkExists().forPath(persistentPath).getNumChildren();
		LOG.info("Active consumers poll: "+count);
		return count;
	}

	private void sleep1second() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void triggerDataTransformation(long startTimestamp, String source) {
		Date startDate = new Date(startTimestamp);
		try {
			final String outputTable = WorkerConstants.getOutputTableName(source);
			HBaseTableUtils.deleteTable(outputTable);
			Configuration conf = HBaseConfiguration.create();
			conf.set("family-column", params.getFamilyColumn());// value
			conf.set("queue-host", params.getQueueHost());// localhost
			conf.set("queue-port", params.getQueuePort());// 5672
			conf.set("queue-adminuser", params.getQueueAdminUser());// guest
			conf.set("queue-adminpwd", params.getQueueAdminPassword());// guest
			conf.set("queue-exchange", params.getQueueExchange());
			Job job = SampleJob.createSampleJob(conf, source,
					startDate);
			boolean success = job.waitForCompletion(true);
			// recompute resuls once finished
			if (success) {
				// do whatever you want..could send a message to another queue
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
