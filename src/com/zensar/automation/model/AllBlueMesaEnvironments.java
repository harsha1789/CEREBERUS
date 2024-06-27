package com.zensar.automation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties
public class AllBlueMesaEnvironments {

	@JsonProperty("Id")
	String  id=null ;

	@JsonProperty("Name")
	String  environmentName=null ;
	
	@JsonProperty("Category")
	String  category=null ;

	@JsonProperty("LastScanStatus")
	String  lastScanStatus=null ;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getLastScanStatus() {
		return lastScanStatus;
	}

	public void setLastScanStatus(String lastScanStatus) {
		this.lastScanStatus = lastScanStatus;
	}

	public String getEnvironmentName() {
		return environmentName;
	}

	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}



}
