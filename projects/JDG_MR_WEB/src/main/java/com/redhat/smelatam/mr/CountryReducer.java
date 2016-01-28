package com.redhat.smelatam.mr;

import java.util.Iterator;

import org.infinispan.distexec.mapreduce.Reducer;

public class CountryReducer implements Reducer<String, Double> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Double reduce(String country, Iterator<Double> quantity) {
		Double totalQuantity=0.0;
		
		while(quantity.hasNext()) {
			totalQuantity += quantity.next();
		}
		System.out.println("Country: " + country + " quantity: " + totalQuantity);
		return totalQuantity;
	}
	

}
