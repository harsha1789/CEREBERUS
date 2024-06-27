package com.zensar.automation.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PatchDataResponse {
	
@JsonProperty("patchID")
int patchID;
@JsonProperty("architecture")
String architecture;

@JsonProperty("casinoAS1ip")
String casinoAS1ip;

@JsonProperty("userName")
String userName;

@JsonProperty("fileName")
String fileName;

@JsonProperty("errorMessage")
String errorMessage;

@JsonProperty("gameTypeList")
String [] gameTypeList;

@JsonProperty("serviceVersionList")
String[] serviceVersionList;

@JsonProperty("fileList")
String[] fileList;

public int getPatchID() {
	return patchID;
}

public void setPatchID(int patchID) {
	this.patchID = patchID;
}

public String getArchitecture() {
	return architecture;
}

public void setArchitecture(String architecture) {
	this.architecture = architecture;
}

public String getCasinoAS1ip() {
	return casinoAS1ip;
}

public void setCasinoAS1ip(String casinoAS1ip) {
	this.casinoAS1ip = casinoAS1ip;
}

public String getUserName() {
	return userName;
}

public void setUserName(String userName) {
	this.userName = userName;
}

public String getFileName() {
	return fileName;
}

public void setFileName(String fileName) {
	this.fileName = fileName;
}

public String getErrorMessage() {
	return errorMessage;
}

public void setErrorMessage(String errorMessage) {
	this.errorMessage = errorMessage;
}

public String[] getGameTypeList() {
	return gameTypeList;
}

public void setGameTypeList(String[] gameTypeList) {
	this.gameTypeList = gameTypeList;
}

public String[] getServiceVersionList() {
	return serviceVersionList;
}

public void setServiceVersionList(String[] serviceVersionList) {
	this.serviceVersionList = serviceVersionList;
}

public String[] getFileList() {
	return fileList;
}

public void setFileList(String[] fileList) {
	this.fileList = fileList;
}


	
}
