package com.redhat.smelatam.mr;

import java.util.Date;
import org.infinispan.distexec.mapreduce.Collector;
import org.infinispan.distexec.mapreduce.Mapper;

import com.redhat.smelatam.model.MessageDataRecord;

public class CountryMapper implements Mapper<String,String,String,Double>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void map(String recordID, String mdrStr, Collector<String, Double> collector) {
		MessageDataRecord mdr = new MessageDataRecord(mdrStr);
		collector.emit(mdr.getCountry(), 1.0);		
		System.out.println("recordID: " + recordID + " - country: " + mdr.getCountry());
		
	}

	
	
}
