package org.springframework.integration.samples.test.zookeeper;
import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

public class SimpleCuratorTests {

	public static CuratorFramework createSimple(String connectionString) {
		// these are reasonable arguments for the ExponentialBackoffRetry. The
		// first
		// retry will wait 1 second - the second will wait up to 2 seconds - the
		// third will wait up to 4 seconds.
		ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000,
				3);

		// The simplest way to get a CuratorFramework instance. This will use
		// default values.
		// The only required arguments are the connection string and the retry
		// policy
		return CuratorFrameworkFactory.newClient(connectionString, retryPolicy);
	}

	public static CuratorFramework createWithOptions(String connectionString,
			RetryPolicy retryPolicy, int connectionTimeoutMs,
			int sessionTimeoutMs) {
		// using the CuratorFrameworkFactory.builder() gives fine grained
		// control
		// over creation options. See the CuratorFrameworkFactory.Builder
		// javadoc
		// details
		return CuratorFrameworkFactory.builder()
				.connectString(connectionString).retryPolicy(retryPolicy)
				.connectionTimeoutMs(connectionTimeoutMs)
				.sessionTimeoutMs(sessionTimeoutMs)
				// etc. etc.
				.build();
	}

	public static void create(CuratorFramework client, String path,
			byte[] payload) throws Exception {
		// this will create the given ZNode with the given data
		client.create().forPath(path, payload);
	}

	public static void createEphemeral(CuratorFramework client, String path,
			byte[] payload) throws Exception {
		// this will create the given EPHEMERAL ZNode with the given data
		client.create().withMode(CreateMode.EPHEMERAL).forPath(path, payload);
	}

	public static String createEphemeralSequential(CuratorFramework client,
			String path, byte[] payload) throws Exception {
		// this will create the given EPHEMERAL-SEQUENTIAL ZNode with the given
		// data using Curator protection.

		/*
		 * Protection Mode:
		 * 
		 * It turns out there is an edge case that exists when creating
		 * sequential-ephemeral nodes. The creation can succeed on the server,
		 * but the server can crash before the created node name is returned to
		 * the client. However, the ZK session is still valid so the ephemeral
		 * node is not deleted. Thus, there is no way for the client to
		 * determine what node was created for them.
		 * 
		 * Even without sequential-ephemeral, however, the create can succeed on
		 * the sever but the client (for various reasons) will not know it.
		 * Putting the create builder into protection mode works around this.
		 * The name of the node that is created is prefixed with a GUID. If node
		 * creation fails the normal retry mechanism will occur. On the retry,
		 * the parent path is first searched for a node that has the GUID in it.
		 * If that node is found, it is assumed to be the lost node that was
		 * successfully created on the first try and is returned to the caller.
		 */
		return client.create().withProtection()
				.withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
				.forPath(path, payload);
	}

	public static void setData(CuratorFramework client, String path,
			byte[] payload) throws Exception {
		// set data for the given node
		client.setData().forPath(path, payload);
	}

	public static void setDataAsyncWithCallback(CuratorFramework client,
			BackgroundCallback callback, String path, byte[] payload)
			throws Exception {
		// this is another method of getting notification of an async completion
		client.setData().inBackground(callback).forPath(path, payload);
	}

	public static void delete(CuratorFramework client, String path)
			throws Exception {
		// delete the given node
		client.delete().forPath(path);
	}

	public static void guaranteedDelete(CuratorFramework client, String path)
			throws Exception {
		// delete the given node and guarantee that it completes

		/*
		 * Guaranteed Delete
		 * 
		 * Solves this edge case: deleting a node can fail due to connection
		 * issues. Further, if the node was ephemeral, the node will not get
		 * auto-deleted as the session is still valid. This can wreak havoc with
		 * lock implementations.
		 * 
		 * 
		 * When guaranteed is set, Curator will record failed node deletions and
		 * attempt to delete them in the background until successful. NOTE: you
		 * will still get an exception when the deletion fails. But, you can be
		 * assured that as long as the CuratorFramework instance is open
		 * attempts will be made to delete the node.
		 */

		client.delete().guaranteed().forPath(path);
	}

	public static List<String> watchedGetChildren(CuratorFramework client,
			String path) throws Exception {
		/**
		 * Get children and set a watcher on the node. The watcher notification
		 * will come through the CuratorListener (see setDataAsync() above).
		 */
		return client.getChildren().watched().forPath(path);
	}

	public static List<String> watchedGetChildren(CuratorFramework client,
			String path, Watcher watcher) throws Exception {
		/**
		 * Get children and set the given watcher on the node.
		 */
		return client.getChildren().usingWatcher(watcher).forPath(path);
	}
	public static void main(String[] args) throws Exception {
		CuratorFramework client = createSimple("localhost:2181");
		client.start();
		
		final String path = "/zkEphemeral";
		createEphemeral(client, path, "sometest1".getBytes());
		
		watchedGetChildren(client, path);
		CuratorListener     localListener = new CuratorListener() {
			
			public void eventReceived(CuratorFramework client, CuratorEvent event)
					throws Exception {
				// TODO Auto-generated method stub
				if ( event.getWatchedEvent() != null )
				{
					System.out.println("Watched! " + event);
				}
				
			}
		};
        client.getCuratorListenable().addListener(localListener);
	
        
        
        
		Watcher watcher = new Watcher() {
			
			public void process(WatchedEvent event) {
				System.out.println("Watched! "+ event);
				
			}
		};
		watchedGetChildren(client, path,watcher);
		delete(client, path);
		System.out.println("Done!");
		
	}
}
