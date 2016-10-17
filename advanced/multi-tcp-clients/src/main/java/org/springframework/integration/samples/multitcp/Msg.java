package org.springframework.integration.samples.multitcp;

public class Msg implements java.io.Serializable {

	private static final long serialVersionUID = -7870900666790828122L;
	private int id;
	private String data;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
}
