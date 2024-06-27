package com.zensar.automation.model;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

public class GetAllBlueMesaEnvironmentsResponse {

	
	@JsonProperty("Success")
	String success=null;
	
	@JsonProperty("CustomMessage")
	String customMessage=null;
	
	@JsonProperty("DataObject")
	List<AllBlueMesaEnvironments> allBlueMesaEnvironments=null;

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getCustomMessage() {
		return customMessage;
	}

	public void setCustomMessage(String customMessage) {
		this.customMessage = customMessage;
	}

	public List<AllBlueMesaEnvironments> getAllBlueMesaEnvironments() {
		return allBlueMesaEnvironments;
	}

	public void setAllBlueMesaEnvironments(List<AllBlueMesaEnvironments> allBlueMesaEnvironments) {
		this.allBlueMesaEnvironments = allBlueMesaEnvironments;
	}

	
	
	
	

	
	
	

	
	
	
}
