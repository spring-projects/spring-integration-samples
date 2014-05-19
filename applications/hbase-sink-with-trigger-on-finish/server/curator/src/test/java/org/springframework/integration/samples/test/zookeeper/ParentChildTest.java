package org.springframework.integration.samples.test.zookeeper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;

import com.google.common.io.Closeables;

/*
 * An example of the PathChildrenCache. The example "harness" is a command processor
 * that allows adding/updating/removed nodes in a path. A PathChildrenCache keeps a
 * cache of these changes and outputs when updates occurs.
 */
public class ParentChildTest {
	private static final String PATH = "/example/cache";
	private static final String CONNECTION_URL = "localhost:2181";

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		CuratorFramework client = null;
		PathChildrenCache cache = null;
		try {
			client = CuratorFrameworkFactory.newClient(CONNECTION_URL, 
					new ExponentialBackoffRetry(1000, 3));
			client.start();

			// in this example we will cache data. Notice that this is optional.
			cache = new PathChildrenCache(client, PATH, true);
			cache.start();


			addListener(cache);
			
			processCommands(client, cache);
		} finally {
			Closeables.closeQuietly(cache);
			Closeables.closeQuietly(client);
		}
	}

	private static void addListener(PathChildrenCache cache) {
		// a PathChildrenCacheListener is optional. Here, it's used just to log
		// changes
		PathChildrenCacheListener listener = new PathChildrenCacheListener() {
			@Override
			public void childEvent(CuratorFramework client,
					PathChildrenCacheEvent event) throws Exception {
				switch (event.getType()) {
					case CHILD_ADDED : {
						System.out.println("Node added: "
								+ ZKPaths.getNodeFromPath(event.getData()
										.getPath()));
						break;
					}

					case CHILD_UPDATED : {
						System.out.println("Node changed: "
								+ ZKPaths.getNodeFromPath(event.getData()
										.getPath()));
						break;
					}

					case CHILD_REMOVED : {
						System.out.println("Node removed: "
								+ ZKPaths.getNodeFromPath(event.getData()
										.getPath()));
						break;
					}
					case INITIALIZED:
					default: break;
				}
			}
		};
		cache.getListenable().addListener(listener);
	}

	private static void processCommands(CuratorFramework client,
			PathChildrenCache cache) throws Exception {
		// More scaffolding that does a simple command line processor

		printHelp();

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		boolean done = false;
		while (!done) {
			System.out.print("> ");

			String command = in.readLine().trim();
			String[] parts = command.split("\\s");
			if (parts.length == 0) {
				continue;
			}
			String operation = parts[0];
			String args[] = Arrays.copyOfRange(parts, 1, parts.length);

			if (operation.equalsIgnoreCase("help")
					|| operation.equalsIgnoreCase("?")) {
				printHelp();
			} else if (operation.equalsIgnoreCase("q")
					|| operation.equalsIgnoreCase("quit")) {
				done = true;
			} else if (operation.equals("set")) {
				setValue(client, command, args);
			} else if (operation.equals("refresh")) {
				cache.rebuild();
			} else if (operation.equals("remove")) {
				remove(client, command, args);
			} else if (operation.equals("list")) {
				list(cache);
			}

			Thread.sleep(1000); // just to allow the console output to catch up
		}

	}

	private static void list(PathChildrenCache cache) {
		final int currentSize = cache.getCurrentData().size();
		if (currentSize == 0) {
			System.out.println("* empty *");
		} else {
			System.out.println("Found "+currentSize+" zNodes");
			for (ChildData data : cache.getCurrentData()) {
				System.out.println(data.getPath() + " = "
						+ new String(data.getData()));
			}
		}
	}

	private static void remove(CuratorFramework client, String command,
			String[] args) throws Exception {
		if (args.length != 1) {
			System.err.println("syntax error (expected remove <path>): "
					+ command);
			return;
		}

		String name = args[0];
		if (name.contains("/")) {
			System.err.println("Invalid node name" + name);
			return;
		}
		String path = ZKPaths.makePath(PATH, name);

		try {
			client.delete().forPath(path);
		} catch (KeeperException.NoNodeException e) {
			// ignore
		}
	}

	private static void setValue(CuratorFramework client, String command,
			String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("syntax error (expected set <path> <value>): "
					+ command);
			return;
		}

		String name = args[0];
		if (name.contains("/")) {
			System.err.println("Invalid node name" + name);
			return;
		}
		String path = ZKPaths.makePath(PATH, name);

		byte[] bytes = args[1].getBytes();
		try {
			client.setData().forPath(path, bytes);
		} catch (KeeperException.NoNodeException e) {
			client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, bytes);
		}
	}

	private static void printHelp() {
		System.out
				.println("An example of using PathChildrenCache. This example is driven by entering commands at the prompt:\n");
		System.out
				.println("set <name> <value>: Adds or updates a node with the given name");
		System.out
				.println("remove <name>: Deletes the node with the given name");
		System.out.println("delete: List the nodes/values in the cache");
		System.out.println("refresh: Refresh the cache");
		System.out.println("quit: Quit the example");
		System.out.println();
	}
}