package org.springframework.integration.samples.server;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class WorkerConstants {
	// TODO parametrize
	public static final String CONTROL_RK_SUFFIX = ".control";
	public static final String RESTART_RK_SUFFIX = ".restart";
	public static final String PERSISTENT_PATH = "/example";
	public static final String ZK_CONNECTION_URL = "localhost:2181";
	public static final int DEFAULT_TIMEOUT = 60;
	private static final String OUTPUT_TABLE_SUFFIX = "-output";
	private static final int ZK_BASE_SLEEP_MS = 1000;
	private static final int ZK_MAX_RETRY = 3;
	
	public static CuratorFramework getZkClient() {
		final ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(ZK_BASE_SLEEP_MS, ZK_MAX_RETRY);
		return CuratorFrameworkFactory.newClient(WorkerConstants.ZK_CONNECTION_URL, retryPolicy);
	}

	public static String getOutputTableName(String datapool) {
		return datapool + WorkerConstants.OUTPUT_TABLE_SUFFIX;
	}
}
