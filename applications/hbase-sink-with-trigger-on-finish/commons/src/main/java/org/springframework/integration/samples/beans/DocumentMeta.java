package org.springframework.integration.samples.beans;

import java.util.List;

import lombok.Data;

@Data
public class DocumentMeta {
	private long timestamp;
	private String language;
	private List<String> categories;
}