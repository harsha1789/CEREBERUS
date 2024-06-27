package com.zensar.automation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties
public class VirtualMachines {
	
	@JsonProperty("Id")
	String  virtualMachineID = null;
	@JsonProperty("Name")
	String  name= null;
	@JsonProperty("EnvironmentId")
	String  environmentId = null;
	@JsonProperty("ExternalIpAddress")
	String  externalIpAddress= null;
	@JsonProperty("ExternalId")
	String  externalId= null;
	@JsonProperty("VMTentacleThumbprint")
	String  vMTentacleThumbprint= null;
	@JsonProperty("VirtualMachineTypeId")
	String  virtualMachineTypeId= null;
	public String getId() {
		return virtualMachineID;
	}
	public void setId(String id) {
		virtualMachineID = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEnvironmentId() {
		return environmentId;
	}
	public void setEnvironmentId(String environmentId) {
		this.environmentId = environmentId;
	}
	public String getExternalIpAddress() {
		return externalIpAddress;
	}
	public void setExternalIpAddress(String externalIpAddress) {
		this.externalIpAddress = externalIpAddress;
	}
	public String getExternalId() {
		return externalId;
	}
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
	public String getVMTentacleThumbprint() {
		return vMTentacleThumbprint;
	}
	public void setVMTentacleThumbprint(String vMTentacleThumbprint) {
		this.vMTentacleThumbprint = vMTentacleThumbprint;
	}
	public String getVirtualMachineTypeId() {
		return virtualMachineTypeId;
	}
	public void setVirtualMachineTypeId(String virtualMachineTypeId) {
		this.virtualMachineTypeId = virtualMachineTypeId;
	}

	
	
	
}
