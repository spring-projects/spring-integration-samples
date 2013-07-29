package org.springframework.integration.samples.hbase.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.integration.samples.beans.DocumentMeta;
import org.springframework.integration.samples.beans.ProcessedDocument;
import org.springframework.integration.samples.beans.RoutingKey;
import org.springframework.integration.samples.hbase.utils.HBaseTableUtils;

public class TestUtils {
	
	@Test
	public void testCreateTable() throws IOException{
		List<ProcessedDocument> docs = new ArrayList<ProcessedDocument>();
		HBaseTableUtils.persist(docs, "test-output", "value");
	}
	
	@Test
	public void testTableDeletion() throws IOException{
		HBaseTableUtils.deleteTable("test-output");
	}

	
	@Test
	public void testPersist() throws IOException{
		List<ProcessedDocument> docs = new ArrayList<ProcessedDocument>();
		generateFakeProcessedDoc(docs);
		HBaseTableUtils.persist(docs, "test-output","value");
	}

	private void generateFakeProcessedDoc(List<ProcessedDocument> docs) {
		ProcessedDocument od = new ProcessedDocument();
		od.setSource(RoutingKey.mysource.toString());
		DocumentMeta meta = new DocumentMeta();
		meta.setTimestamp(new Date().getTime());
		meta.setLanguage("en");
		List<String> categories= new ArrayList<String>();
		categories.add("software");
		meta.setCategories(categories);
		od.setMeta(meta);
		od.setUrl("http://test.it/someurl");
		docs.add(od);
	}
}
