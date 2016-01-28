package com.redhat.smelatam.mr;

import org.infinispan.distexec.mapreduce.Collector;
import org.infinispan.distexec.mapreduce.Mapper;

import com.redhat.smelatam.model.MessageDataRecord;

public class ClientTypeMapper implements Mapper<String, String, String, Double> {

	private static final long serialVersionUID = 1L;
	
	public void map(String recordID, String mdrStr, Collector<String,Double> collector) {
		MessageDataRecord mdr = new MessageDataRecord(mdrStr);
		collector.emit(mdr.getClientType(), 1.0);
		System.out.println("Record ID: " + recordID + " - ClientType: " + mdr.getClientType() );
	};

}
