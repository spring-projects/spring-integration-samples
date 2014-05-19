package org.springframework.integration.samples.json.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.integration.samples.beans.Document;
import org.springframework.integration.samples.beans.DocumentMeta;
import org.springframework.integration.samples.beans.DocumentToIndex;
import org.springframework.integration.samples.beans.RoutingKey;

public class JsonMocksGenerator {
	static final RoutingKey rk = RoutingKey.mysource;
	
	public static DocumentToIndex generateFakeDocument() {
		Document doc1 = getDoc1();
		Document doc2 = getDoc2();

		List<Document> docs = new ArrayList<Document>();
		docs.add(doc1);
		docs.add(doc2);

		final DocumentToIndex documentToIndex = new DocumentToIndex();
		documentToIndex.setRoutingKey(rk);
		documentToIndex.setSource(rk.toString());
		documentToIndex.setId(1+"");
		documentToIndex.setDocs(docs);
		return documentToIndex;
	}
	public static  Document getDoc2() {
		List<String> doc2categories = new ArrayList<String>();
		doc2categories.add("economics");
		DocumentMeta doc2meta = new DocumentMeta();
		doc2meta.setTimestamp(new Date().getTime());
		doc2meta.setCategories(doc2categories);
		doc2meta.setLanguage("en");

		Document doc2 = new Document();
		doc2.setUrl("URL1");
		doc2.setMeta(doc2meta);
		doc2.setText("Some other text");
		return doc2;
	}

	public static  Document getDoc1() {
		List<String> doc1categories = new ArrayList<String>();
		doc1categories.add("politics");
		doc1categories.add("computer");
		doc1categories.add("economics");

		DocumentMeta doc1meta = new DocumentMeta();
		doc1meta.setTimestamp(new Date().getTime());
		doc1meta.setCategories(doc1categories);
		doc1meta.setLanguage("en");

		Document doc1 = new Document();
		doc1.setUrl("URLn");
		doc1.setMeta(doc1meta);
		doc1.setText("Some text");
		return doc1;
	}

}
