package org.springframework.integration.samples.beans;

import java.util.UUID;

import lombok.Getter;
import lombok.ToString;

import org.springframework.integration.samples.beans.DocumentToIndex;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Domain object representing a document to store in HBase
 * 
 * @author Flavio Pompermaier
 */

@Getter
@ToString
public class HBaseDocument {

	@JsonProperty("_id")
	private final String id;
	private final DocumentToIndex document;

	public HBaseDocument(DocumentToIndex d) {
		this.document = d;
		//TODO find the best row key pattern! (that allows for FuzzyRowFilter)	
		id = UUID.randomUUID().toString();
	}

	

}
