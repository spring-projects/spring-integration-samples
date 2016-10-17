package org.springframework.integration.samples.multitcp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "conn")
public class Conn  implements Serializable {
	private static final long serialVersionUID = 3322493348403761089L;

	private List<Server> servers = new ArrayList<Server>();

	@PostConstruct
    public void init() {
        for(Server current : this.getServers()) {
        	System.out.println("Regist server : " + current.getIp() + ", port : " + current.getPort());
        }
    }
    
    public List<Server> getServers() {
		return servers;
	}

	public static class Server{

    	
    	private String id;
		private String ip;
		private String serializer;
		private String deserializer;
    	private int port;
    	private int sotimeout;
    	private int requestTimeout;
    	private int remoteTimeout;

    	public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getIp() {
			return ip;
		}
		public void setIp(String ip) {
			this.ip = ip;
		}
		public String getSerializer() {
			return serializer;
		}
		public void setSerializer(String serializer) {
			this.serializer = serializer;
		}
		public String getDeserializer() {
			return deserializer;
		}
		public void setDeserializer(String deserializer) {
			this.deserializer = deserializer;
		}
		public int getPort() {
			return port;
		}
		public void setPort(int port) {
			this.port = port;
		}
		public int getSotimeout() {
			return sotimeout;
		}
		public void setSotimeout(int sotimeout) {
			this.sotimeout = sotimeout;
		}
		public int getRequestTimeout() {
			return requestTimeout;
		}
		public void setRequestTimeout(int requestTimeout) {
			this.requestTimeout = requestTimeout;
		}
		public int getRemoteTimeout() {
			return remoteTimeout;
		}
		public void setRemoteTimeout(int remoteTimeout) {
			this.remoteTimeout = remoteTimeout;
		}
    	
    }

}
