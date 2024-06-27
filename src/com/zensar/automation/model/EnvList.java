package com.zensar.automation.model;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.zensar.automation.framework.model.DeviceInfo;

public class EnvList  {

	@JsonProperty("devices")
	public List<DeviceInfo> devices=null;
	
	@JsonProperty("Success")
	public String strSuccess=null;
	
	@JsonProperty("CustomMessage")
	public String strCustomMessage=null;
	
	@JsonProperty("DataObject")
	public DataObject dataObject=null;

	
	
	
	public List<DeviceInfo> getDevices() {
		return devices;
	}

	public void setDevices(List<DeviceInfo> devices) {
		this.devices = devices;
	}

	
	public String getSuccess() {
		return strSuccess;
	}

	public void setSuccess(String success) {
		this.strSuccess = success;
	}

	
	public String getCustomMessage() {
		return strCustomMessage;
	}
	public void setCustomMessage(String customMessage) {
		this.strCustomMessage = customMessage;
	}

	public DataObject getDataObject() {
		return dataObject;
	}

	public void setDataObject(DataObject dataObject) {
		this.dataObject = dataObject;
	}

	
	
	
	

	
	
	
}
