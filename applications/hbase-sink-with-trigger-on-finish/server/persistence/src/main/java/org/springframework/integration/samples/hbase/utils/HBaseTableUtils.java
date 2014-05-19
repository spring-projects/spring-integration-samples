package org.springframework.integration.samples.hbase.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableExistsException;
import org.apache.hadoop.hbase.TableNotFoundException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Row;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.regionserver.StoreFile.BloomType;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.Threads;
import org.springframework.integration.samples.beans.DocumentToIndex;
import org.springframework.integration.samples.beans.HBaseDocument;
import org.springframework.integration.samples.beans.ProcessedDocument;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HBaseTableUtils {

	public static final String JSON_CONTENT_QUALIFIER = "json";
	private static final int MAX = 16;
	private static final String THREAD_POOL_PREFIX = "hbase-table-pool";

	public static Date getDayBefore(Date date) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return cal.getTime();
	}
	
	public static void deleteTable(String tableName) throws IOException {
		Configuration conf = HBaseConfiguration.create();
		HBaseAdmin hbaseAdmin = new HBaseAdmin(conf);
		try{
			hbaseAdmin.disableTable(tableName);
			hbaseAdmin.deleteTable(tableName);
			hbaseAdmin.close();
		}catch(TableNotFoundException ex){
			//ignore
		}
	}
	/**
	 * Create a table if it doesn't exists
	 * 
	 * @param tableName
	 * @param conf
	 * @param hbaseAdmin
	 * @param families
	 * @return An HTable instance for the created table.
	 * @throws IOException
	 */
	public static void createTableIfNotExists(String tableName,
			String familyStr, Configuration conf) throws IOException {
		final byte[] tableNameArray = Bytes.toBytes(tableName);
		final byte[] family = Bytes.toBytes(familyStr);

		try {
			createTable(tableNameArray, family, conf);
		} catch (TableExistsException e) {
			// do not throw error in this case
		}
	}

	private static HTable createTable(byte[] tableName, byte[] family,
			Configuration conf) throws IOException {
		HTableDescriptor desc = new HTableDescriptor(tableName);
		HColumnDescriptor hcd = new HColumnDescriptor(family);
		//TODO requires installation of snappy lib in Hbase installation
		//hcd.setCompressionType(Algorithm.SNAPPY);
		
		// Disable blooms (they are on by default as of 0.95) but we disable
		// them here because
		// tests have hard coded counts of what to expect in block cache,
		// etc., and blooms being on is interfering.
		hcd.setBloomFilterType(BloomType.NONE);
		desc.addFamily(hcd);
		Configuration c = new Configuration(conf);
		HBaseAdmin hbaseAdmin = new HBaseAdmin(conf);
		hbaseAdmin.createTable(desc);
		hbaseAdmin.close();
		return new HTable(c, tableName);
	}

	public static void persist(List<ProcessedDocument> docs, String tableName, String familyColumn) {
		try {
			Configuration conf = HBaseConfiguration.create();
			HConnection conn = HConnectionManager.createConnection(conf);
			createTableIfNotExists(tableName, familyColumn, conf);

			List<Row> rows = generateBatchBulk(docs,familyColumn);
			writeBulk(tableName, conn, rows);
		} catch (ZooKeeperConnectionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void writeBulk(String tableName, HConnection conn,
			List<Row> rows) throws IOException {

		ExecutorService pool = getPool();
		HTable table = null;
		try {
			table = new HTable(Bytes.toBytes(tableName), conn, pool);
			table.batch(rows);
		} catch (InterruptedException ix) {
			throw new IOException(ix);
		} finally {
			if (table != null) {
				table.close();
			}
		}
	}

	private static ExecutorService getPool() {
		ExecutorService pool = new ThreadPoolExecutor(1, MAX, 60,
				TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),
				Threads.newDaemonThreadFactory(THREAD_POOL_PREFIX));
		((ThreadPoolExecutor) pool).allowCoreThreadTimeOut(true);
		return pool;
	}

	private static List<Row> generateBatchBulk(List<ProcessedDocument> docs, String familyColumn) {
		List<Row> rows = new ArrayList<Row>();
		for (ProcessedDocument pd : docs) {
			Put put = getProcessedDocAsPut(pd,familyColumn);
			rows.add(put);
		}
		return rows;
	}
	private static Put getProcessedDocAsPut(ProcessedDocument pd, String familyColumn) {
		// set row key
		Put put = new Put(Bytes.toBytes(pd.getId()));

		// set value of value:json column
		String docJSON;
		try {
			docJSON = new ObjectMapper().writeValueAsString(pd);
			final byte[] cfBytes = Bytes.toBytes(familyColumn);
			final byte[] qualifierBytes = Bytes.toBytes(JSON_CONTENT_QUALIFIER);
			final byte[] jsonBytes = Bytes.toBytes(docJSON);
			put.add(cfBytes, qualifierBytes, jsonBytes);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return put;
	}

	public static Put getDocToIndexAsPut(DocumentToIndex doc2index, String familyColumn) {
		HBaseDocument m = new HBaseDocument(doc2index);
		final String id = m.getId();
		Put put = new Put(Bytes.toBytes(id));
		ObjectMapper mapper = new ObjectMapper();
		String docJSON;
		try {
			docJSON = mapper.writeValueAsString(m.getDocument());
			final byte[] cfBytes = Bytes.toBytes(familyColumn);
			final byte[] qualifierBytes = Bytes.toBytes(JSON_CONTENT_QUALIFIER);
			final byte[] jsonBytes = Bytes.toBytes(docJSON);
			put.add(cfBytes, qualifierBytes, jsonBytes);
			return put;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static ProcessedDocument deserializeDoc(String jsonString)
			throws IOException, JsonParseException, JsonMappingException {
		final ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(jsonString, ProcessedDocument.class);
	}
	
	public static Scan getTempStorageScanner(byte[] family, byte[] qualifier,
			Date start) throws IOException {
		Scan scan = new Scan();
		scan.setCacheBlocks(false); // don't set to true for MR jobs
		// filter by timestamps
		if(start!=null){
			//TODO better management of synch!
			Date yesterday = HBaseTableUtils.getDayBefore(start);
			scan.setTimeRange(yesterday.getTime(), Long.MAX_VALUE);
		}
		scan.addColumn(family, qualifier);
		scan.setCaching(1000);// 1000
		scan.setMaxVersions(1);// only last version
		return scan;
	}

}
