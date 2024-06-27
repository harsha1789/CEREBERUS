package com.zensar.automation.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataObject {
	
	@JsonProperty("EnvironmentId")
	public String  environmentId=null ;
	
	@JsonProperty("EnvironmentCategoryId")
	String  environmentCategoryId=null ;
	
	@JsonProperty("VirtualStatusId")
	String  virtualStatusId=null ;
	
	@JsonProperty("Name")
	String  environmentName=null ;
	
	@JsonProperty("ExternalId")
	String  externalId=null ;
	
	@JsonProperty("CreatedDate")
	String  createdDate=null ;
	
	@JsonProperty("LastFoundOnline")
	String  lastFoundOnline=null ;
	
	@JsonProperty("IsActive")
	String  isActive=null ;
	
	@JsonProperty("AllowReprovision")
	String  allowReprovision=null ;
	
	@JsonProperty("LastForceUpdate")
	String  lastForceUpdate=null ;
	
	@JsonProperty("Version")
	String  version=null ;
	
	@JsonProperty("OctopusServerId")
	String  octopusServerId=null ;
	
	@JsonProperty("VirtualMachines")
	List<VirtualMachines> virtualMachines=null;
	

	
	public String getEnvironmentId() {
		return environmentId;
	}

	public void setEnvironmentId(String environmentId) {
		this.environmentId = environmentId;
	}

	public String getEnvironmentCategoryId() {
		return environmentCategoryId;
	}

	public void setEnvironmentCategoryId(String environmentCategoryId) {
		this.environmentCategoryId = environmentCategoryId;
	}

	public String getVirtualStatusId() {
		return virtualStatusId;
	}

	public void setVirtualStatusId(String virtualStatusId) {
		this.virtualStatusId = virtualStatusId;
	}

	public String getName() {
		return environmentName;
	}

	public void setName(String name) {
		environmentName = name;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastFoundOnline() {
		return lastFoundOnline;
	}

	public void setLastFoundOnline(String lastFoundOnline) {
		this.lastFoundOnline = lastFoundOnline;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getAllowReprovision() {
		return allowReprovision;
	}

	public void setAllowReprovision(String allowReprovision) {
		this.allowReprovision = allowReprovision;
	}

	public String getLastForceUpdate() {
		return lastForceUpdate;
	}

	public void setLastForceUpdate(String lastForceUpdate) {
		this.lastForceUpdate = lastForceUpdate;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getOctopusServerId() {
		return octopusServerId;
	}

	public void setOctopusServerId(String octopusServerId) {
		this.octopusServerId = octopusServerId;
	}

	public List<VirtualMachines> getVirtualMachines() {
		return virtualMachines;
	}

	public void setVirtualMachines(List<VirtualMachines> virtualMachines) {
		this.virtualMachines = virtualMachines;
	}

	
	

}
