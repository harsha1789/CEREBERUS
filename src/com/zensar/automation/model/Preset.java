package com.zensar.automation.model;

public class Preset {
int	 moduleID;
int clientID;
int patchID;
String patchStatus;
int gameID;
String created;
String modified;
String installedClientName;


public String getCreated() {
	return created;
}
public void setCreated(String created) {
	this.created = created;
}
public String getModified() {
	return modified;
}
public void setModified(String modified) {
	this.modified = modified;
}
public String getInstalledClientName() {
	return installedClientName;
}
public void setInstalledClientName(String installedClientName) {
	this.installedClientName = installedClientName;
}


public int getmoduleID() {
	return moduleID;
}
public void setmoduleID(int mID) {
	this.moduleID = mID;
}
public int getclientID() {
	return clientID;
}
public void setclientID(int cID) {
	this.clientID = cID;
}
public int getPatchID() {
	return patchID;
}
public void setPatchID(int patchID) {
	this.patchID = patchID;
}
public String getPatchStatus() {
	return patchStatus;
}
public void setPatchStatus(String patchStatus) {
	this.patchStatus = patchStatus;
}
public int getGameID() {
	return gameID;
}
public void setGameID(int gameID) {
	this.gameID = gameID;
}




}
