package org.springframework.integration.samples.hbase.mappers;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.integration.samples.beans.DocumentToIndex;
import org.springframework.integration.samples.beans.RoutingKey;
import org.springframework.integration.samples.hbase.utils.HBaseTableUtils;
import org.springframework.integration.samples.producer.gateway.HBaseGateway;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SampleMapper extends TableMapper<Text, Text> {

	private static final String GROUP_NAME = "SAMPLE";
	private static final String PROGRESS_COUNTER_NAME = "inspected_rows";
	private final HBaseGateway gw = new HBaseGateway();
	
	/**
	 * Maps the data from (row, values) to (null, null)
	 * 
	 * @param row
	 *            The current table row key.
	 * @param columns
	 *            The columns.
	 * @param context
	 *            The current context.
	 * @throws IOException
	 *             When something is broken with the data.
	 * @throws InterruptedException
	 * @see org.apache.hadoop.mapreduce.Mapper#map(KEYIN, VALUEIN,
	 *      org.apache.hadoop.mapreduce.Mapper.Context)
	 */
	@Override
	public void map(ImmutableBytesWritable row, Result columns, Context context)
			throws IOException, InterruptedException {
		for (KeyValue column : columns.list()) {
			final byte[] columnValue = column.getValue();
			final byte[] columnFamily = column.getFamily();
			final byte[] columnQualifier = column.getQualifier();
			final String family = Bytes.toString(columnFamily);
			final String qual = Bytes.toString(columnQualifier);
			Configuration conf = context.getConfiguration();
			String valueFamily = conf.get("family-column");//value
			// byte [] columnBytes = KeyValue.makeColumn(columnFamily,
			// columnQualifier);
			boolean isJsonColumn = valueFamily.equals(family)
					&& HBaseTableUtils.JSON_CONTENT_QUALIFIER.equals(qual);
			if (columnValue.length > 0 && isJsonColumn) {
				//track progress
				final Counter progressCounter = getProgressCounter(context);
				progressCounter.increment(1);
				DocumentToIndex docToIndex = deserializeDoc(columnValue);
				docToIndex.setRoutingKey(RoutingKey.secondstep);
				docToIndex.setSource(docToIndex.getSource());
				gw.sendDocumentToPersist(docToIndex);
			}
		}
	}
	
	private Counter getProgressCounter(final Context context) {
		return context.getCounter(GROUP_NAME, PROGRESS_COUNTER_NAME);
	}
	private DocumentToIndex deserializeDoc(byte[] columnValue)
			throws IOException, JsonParseException, JsonMappingException {
		String jsonString = Bytes.toString(columnValue);
		final ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(jsonString, DocumentToIndex.class);
	}

	@Override
	protected void setup(Context context)
			throws IOException, InterruptedException {
		super.setup(context);
		Configuration conf = context.getConfiguration();
		String queueHost = conf.get("queue-host");//localhost
		final String portStr = conf.get("queue-port");
		int queuePort = new Integer(portStr);//5672
		String adminUsername = conf.get("queue-adminuser");//guest
		String adminPassword = conf.get("queue-adminpwd");//guest
		String directExchange = conf.get("queue-exchange");
		
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(queueHost);
		connectionFactory.setUsername(adminUsername);
		connectionFactory.setPassword(adminPassword);
		connectionFactory.setPort(queuePort);
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
		rabbitTemplate.setExchange(directExchange);
		gw.setRabbitTemplate(rabbitTemplate);
	}

}
