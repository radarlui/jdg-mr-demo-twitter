package com.redhat.smelatam.endpoints;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import com.redhat.smelatam.services.MDRService;
@Stateless
@Path("/mr")
public class MDREndpoint {
	
	@Inject
	MDRService cus;
	
	@POST
	@Path("/MDRUpload")
	@Produces("application/json")
	@Consumes("multipart/form-data")
	public Map<String, String> uploadCDRs(MultipartFormDataInput cdrFile){
		
		String content="";
		
		Map<String, List<InputPart>> uploadForm = cdrFile.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("cdrFile");
		for (InputPart inputPart : inputParts) {
				try {
					content += inputPart.getBodyAsString();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
		}
			return cus.uploadMDRs(content);
	}
	
	@GET
	@Path("/MDRGet")
	@Produces("application/json")
	public Map<String,String> getMDRs(@QueryParam("count") @DefaultValue("0") int count){
		return cus.getMDRs(count);
	}
	
	@GET
	@Path("/MDRGetSize")
	@Produces("application/json")
	public int getCDRSize(){
		return cus.getMDRSize();
	}
	
	
}
