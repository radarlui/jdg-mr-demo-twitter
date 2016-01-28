package com.redhat.smelatam.services;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.infinispan.AdvancedCache;
import org.infinispan.distexec.mapreduce.MapReduceTask;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.remoting.transport.Address;

import com.redhat.smelatam.mr.CountryMapper;
import com.redhat.smelatam.mr.CountryReducer;
import com.redhat.smelatam.mr.ClientTypeMapper;
import com.redhat.smelatam.mr.ClientTypeReducer;

@Stateless
public class MRService {

	@Inject
	AdvancedCache<String,String> reqCache;
	
	
	public Set<Entry<String, Double>> processCountry(){
		int count=100;
		Map<String,Double> lines = 
				new MapReduceTask<String,String,String,Double>(
						reqCache.getAdvancedCache(),true)
						.mappedWith(new CountryMapper())
						.reducedWith(new CountryReducer())
						.execute();
		
		Set<Entry<String, Double>> linesTrunc = new HashSet<Entry<String,Double>>();
		Iterator<Entry<String, Double>> iter = lines.entrySet().iterator();
		for(int i=0;i<count && iter.hasNext();i++){
			Entry<String, Double> entry = iter.next();
			linesTrunc.add(entry);
		}
		return linesTrunc;
	}
	
	public Set<Entry<String, Double>> processClientType(){
		Map<String,Double> clients = 
				new MapReduceTask<String,String,String,Double>(
						reqCache.getAdvancedCache(),true)
						.mappedWith(new ClientTypeMapper())
						.reducedWith(new ClientTypeReducer())
						.execute();
		
		double[] top5 = new double[5];
		
		HashMap newMap = new HashMap();
		for (Iterator<String> i=clients.keySet().iterator();i.hasNext();)
		{
		
			String key = i.next();
			//if (charges.get(key)>=10)
			if (clients.get(key)>top5[0])
			{
				System.out.println("New top5: "+clients.get(key));
				
				top5[0]=clients.get(key);
				Arrays.sort(top5);
			}
		}
		for (Iterator<String> i=clients.keySet().iterator();i.hasNext();)
		{
		
			String key = i.next();
			if (clients.get(key)>=top5[0])
			{
				newMap.put(key, clients.get(key));				
				System.out.println("Top5: "+clients.get(key));
			}
		}
		

		
		
		
		return newMap.entrySet();
	}
	
	
	
	public String getClusterStats(){
		EmbeddedCacheManager ecm = reqCache.getCacheManager();
		
		String cluster = ecm.getClusterName();
		List<Address> members = ecm.getMembers();
		String membersArr = "";
		Set<String> cacheNames = ecm.getCacheNames();
		String cacheNamesStr ="";
		Iterator<String> iter = cacheNames.iterator();
		while(iter.hasNext()){
			cacheNamesStr+="\"" + iter.next() + "\"";
			if(iter.hasNext())
				cacheNamesStr+=",";
		}
		
		for(int i=0;i<members.size();i++){
			membersArr+="\"" + members.get(i).toString() + "\"";
			if(i!=(members.size()-1))
				membersArr+=",";
		}
		String resp = "{\"clusterName\": \"" + cluster + "\", \"clusterMembers\":[" + membersArr + "]" +", \"cacheNames\":[" + cacheNamesStr + "]" +  "}";
		return resp;
	}
}
