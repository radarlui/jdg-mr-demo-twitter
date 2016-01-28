package com.redhat.smelatam.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class MessageDataRecord implements Serializable {
	private String country;
	private String clientType;
	private String text;
	private Date timestamp;
	

	public MessageDataRecord(String country, String clientType, double quantity,
			Date timestamp) {
		super();
		this.country = country;
		this.clientType = clientType;
		this.text = text;
		this.timestamp = timestamp;
	}
	
	public MessageDataRecord(String str){
		String strFields[] = str.split("\\|");
		this.country=strFields[0];
		this.clientType = strFields[1];
		this.text = strFields[2];
		//this.timestamp=new Date(Long.parseLong(strFields[3]));
		try {
			this.timestamp = DateFormat.getTimeInstance().parse(strFields[3]);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getClientType() {
		return clientType;
	}
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	

	
}
