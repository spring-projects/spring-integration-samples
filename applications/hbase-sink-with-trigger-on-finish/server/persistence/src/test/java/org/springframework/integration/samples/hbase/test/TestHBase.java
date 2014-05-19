package org.springframework.integration.samples.hbase.test;
/*
 * Compile and run with:
 * javac -cp `hbase classpath` TestHBase.java 
 * java -cp `hbase classpath` TestHBase
 */
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.integration.samples.beans.DocumentToIndex;
import org.springframework.integration.samples.beans.HBaseDocument;
import org.springframework.integration.samples.json.test.JsonMocksGenerator;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestHBase {
	private static final String TEST_TABLE = "test";
	public static void main(String[] args) throws Exception {
		createTestDoc();
		if(true)
			System.exit(0);
		Configuration conf = HBaseConfiguration.create();
		HBaseAdmin admin = new HBaseAdmin(conf);
		// 1 - test connection (essentially)
//		HTableDescriptor[] tables = admin.listTables();
//		for (HTableDescriptor hTableDescriptor : tables) {
//			System.out.println(hTableDescriptor);
//		}

		// 2 - test creation of a record
		try {
			HTable table = new HTable(conf, TEST_TABLE);
//			HBaseDocument testDoc = createTestDoc();
//			storeDocument(testDoc, table);
//			if(true)
//				System.exit(0);

			// 3 - test row filter
			scanByTimestampAndKey(table);
		} finally {
			admin.close();
		}
	}
	private static void scanByTimestampAndKey(HTable table) throws IOException {
		Scan scan = new Scan();
		//filter by timestamps
		//scan.setTimeRange(1372844707703L, Long.MAX_VALUE);
		scan.setTimeRange(1372844707704L, Long.MAX_VALUE);//
		final byte[] cfBytes = Bytes.toBytes("value");
		final byte[] qualifierBytes = Bytes.toBytes("json");
		scan.addColumn(cfBytes, qualifierBytes);
//		Filter filter1 = new RowFilter(
//				CompareFilter.CompareOp.GREATER_OR_EQUAL,
//				new SubstringComparator("000|000|000|"));
//		scan.setFilter(filter1);
		ResultScanner scanner1 = table.getScanner(scan);
		final ObjectMapper mapper = new ObjectMapper();
		for (Result res : scanner1) {
			deserializeAndPrint(cfBytes, qualifierBytes, mapper, res);
		}
		scanner1.close();
	}
	private static void deserializeAndPrint(final byte[] cfBytes,
			final byte[] qualifierBytes, final ObjectMapper mapper, Result res)
			throws IOException, JsonParseException, JsonMappingException {
		byte[] value = res.getValue(cfBytes,qualifierBytes);
		String jsonString = Bytes.toString(value);
		DocumentToIndex d = mapper.readValue(jsonString, DocumentToIndex.class);
		System.out.println(d);
	}
	private static HBaseDocument createTestDoc() {
		DocumentToIndex d = JsonMocksGenerator.generateFakeDocument();
		HBaseDocument m = new HBaseDocument(d);
		return m;
	}
}