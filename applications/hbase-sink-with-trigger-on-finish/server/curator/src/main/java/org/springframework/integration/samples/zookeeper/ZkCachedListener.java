package org.springframework.integration.samples.zookeeper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.utils.ZKPaths;
import org.apache.log4j.Logger;

import com.google.common.io.Closeables;

/*
 * An example of the PathChildrenCache. The example "harness" is a command processor
 * that allows adding/updating/removed nodes in a path. A PathChildrenCache keeps a
 * cache of these changes and outputs when updates occurs.
 */
public class ZkCachedListener {
	private static final Logger LOG = Logger.getLogger(ZkCachedListener.class);
	private final CuratorFramework client;
	private PathChildrenCache cache;
	private final String path;

	public ZkCachedListener(CuratorFramework client, String path) {
		this.client = client;
		this.path = path;
	}

	public void startListening() {
		// in this example we will cache data. Notice that this is optional.
		cache = new PathChildrenCache(client, path, true);
		try {
			cache.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		addListener(cache);
	}

	@SuppressWarnings("deprecation")
	public void stopListening() {
		Closeables.closeQuietly(cache);
		cache = null;
	}

	private static void addListener(PathChildrenCache cache) {
		// a PathChildrenCacheListener is optional. Here, it's used just to log
		// changes
		PathChildrenCacheListener listener = new PathChildrenCacheListener() {
			@Override
			public void childEvent(CuratorFramework client,
					PathChildrenCacheEvent event) throws Exception {
				final String eventPath = event.getData().getPath();
				final String nodeFromPath = ZKPaths.getNodeFromPath(eventPath);
				switch (event.getType()) {
					case CHILD_ADDED : {
						LOG.debug("Node added: " + nodeFromPath);
						break;
					}

					case CHILD_UPDATED : {
						LOG.debug("Node changed: " + nodeFromPath);
						break;
					}

					case CHILD_REMOVED : {
						LOG.debug("Node removed: " + nodeFromPath);
						break;
					}
					case INITIALIZED :
					default :
						break;
				}
			}
		};
		cache.getListenable().addListener(listener);
	}

	public int getChildrenCount() {
		if(cache == null)
			return 0;
		final int currentSize = cache.getCurrentData().size();
		return currentSize;
	}

}