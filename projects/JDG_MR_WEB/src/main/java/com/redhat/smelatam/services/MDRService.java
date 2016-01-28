package com.redhat.smelatam.services;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.infinispan.AdvancedCache;
import org.infinispan.commons.util.CloseableIterator;

import twitter4j.TwitterException;

@Stateless
public class MDRService {
	
	
	@Inject
	AdvancedCache<String,String> reqCache;

	@SuppressWarnings("deprecation")
	public Map<String,String> uploadMDRs(String cdrFile){
		String[] cdrs= new String[0];
		cdrs = URLDecoder.decode(cdrFile).split("\\*");
		for(int i=0;i<cdrs.length;i++){
			TwitterDumper.reqCache.putAsync(String.valueOf(i), cdrs[i]);
		}
		try {
			java.lang.Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String,String> m = new HashMap<String,String>();
		for(Entry<String,String> entry :reqCache.entrySet()){
			m.put(entry.getKey(), entry.getValue());
		}
		return m;
	}
	
	public Map<String,String> getMDRs(int count){

		Map<String,String> m = new HashMap<String,String>();
		if(count==0)
			count = reqCache.entrySet().size();
		/*
		CloseableIterator<Entry<String, String>> iter = reqCache.entrySet().iterator();
		for(int i=0;i<count && iter.hasNext();i++){
			Entry<String,String> entry = iter.next();
			m.put(entry.getKey(), entry.getValue());
		}
		iter.close();
		*/
		DecimalFormat df = new DecimalFormat("000000");
		for (int i=0;i<count;i++)
		{
			String key = df.format(reqCache.entrySet().size()-i);
			m.put(key, reqCache.get(key));
			
		}
		return m;
	}

	public int getMDRSize() {
		return reqCache.entrySet().size();
	}

}
