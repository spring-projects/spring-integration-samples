package org.springframework.integration.samples.server.workers.beans;

import lombok.Data;

import org.springframework.integration.samples.producer.gateway.HBaseGateway;

@Data
public class QueueWorkerParams {
	private HBaseGateway gw;
	private String familyColumn;
	private String queueHost;
	private String queuePort;
	private String queueAdminUser;
	private String queueAdminPassword; 
	private String queueExchange;
}
