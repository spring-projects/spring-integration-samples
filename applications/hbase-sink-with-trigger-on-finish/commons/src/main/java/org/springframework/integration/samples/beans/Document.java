package org.springframework.integration.samples.beans;

import java.util.List;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class Document {

	private String url;
	private DocumentMeta  meta;
	private String text;
}