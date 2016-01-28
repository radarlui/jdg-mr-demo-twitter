package com.redhat.smelatam.endpoints;

import java.util.Map.Entry;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.redhat.smelatam.services.MRService;

@Stateless
@Path("/mr")
public class MREndpoint {
	@Inject
	MRService bs;
	
	@GET
	@Path("/processCountry")
	@Produces("application/json")
	public Set<Entry<String, Double>> processBills(){
		return bs.processCountry();
	}
	
	@GET
	@Path("/processClientType")
	@Produces("application/json")
	public Set<Entry<String, Double>> processStats(){
		return bs.processClientType();
	}
	@GET
	@Path("/clusterStats")
	@Produces("application/json")
	public String getClusterStats(){
		return bs.getClusterStats();
	}
}
