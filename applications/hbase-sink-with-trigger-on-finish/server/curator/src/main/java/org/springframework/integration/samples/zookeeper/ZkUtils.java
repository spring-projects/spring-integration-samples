package org.springframework.integration.samples.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.ZKPaths;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

public class ZkUtils {
	private static final Logger LOG = Logger.getLogger(ZkUtils.class);
	
	public static void setValue(CuratorFramework client, String persistentPath, String name, String value){

		if (name.contains("/")) {
			LOG.error("Invalid node name" + name);
			return;
		}
		String path = ZKPaths.makePath(persistentPath, name);

		try {
			createPersistenPathIfnotExists(client, persistentPath, path);
			
			byte[] bytes = value.getBytes();
			try {
				client.setData().forPath(path, bytes);
			} catch (KeeperException.NoNodeException e) {
				client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, bytes);
			}
		} catch (Exception e) {
			LOG.error(e);
		}
		
	}

	private static void createPersistenPathIfnotExists(CuratorFramework client,
			String persistentPath, String path) throws Exception {
			Stat s = client.checkExists().forPath(persistentPath);
			if(s==null)
				client.create().creatingParentsIfNeeded().forPath(path);
	}
	
	public static String setValueSeq(CuratorFramework client, String persistentPath, String suffix, String value){

		if (suffix.contains("/")) {
			LOG.error("Invalid node name" + suffix);
			return null;
		}
		persistentPath +=  "/" + suffix;
		String path = ZKPaths.makePath(persistentPath, "zn_") ;
		String ret = null;
		try {
			byte[] bytes = value.getBytes();
			final CreateMode mode = CreateMode.EPHEMERAL_SEQUENTIAL;
			ret = client.create().creatingParentsIfNeeded().withMode(mode).forPath(path, bytes);
			ret = ret.substring(persistentPath.length() + 1);
		} catch (Exception e) {
			LOG.error(e);
		}
		return ret;
		
	}
	public static String setValueSeq(CuratorFramework client, String persistentPath, String suffix){
		return setValueSeq(client, persistentPath, suffix,"started");		
	}
	
	public static void remove(CuratorFramework client, String persistentPath, String name) {
	
		if (name.contains("/")) {
			LOG.error("Invalid node name" + name);
			return;
		}
		String path = ZKPaths.makePath(persistentPath, name);

		try {
			client.delete().forPath(path);
		} catch (KeeperException.NoNodeException e) {
			// ignore
		} catch (Exception e) {
			LOG.error(e);
		}
	}
}
