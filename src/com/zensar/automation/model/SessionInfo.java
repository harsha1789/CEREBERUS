package com.zensar.automation.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SessionInfo {

	@JsonProperty("Version")
	long Version;
	@JsonProperty("UserType")
	long UserType;
	@JsonProperty("UserID")
	long UserID;
	@JsonProperty("SessionID")
	long SessionID;
	@JsonProperty("ProductID")
	long ProductID;
	@JsonProperty("ServerID")
	long ServerID;
	@JsonProperty("ClientType")
	long ClientType;
	@JsonProperty("ModuleID")
	long ModuleID;
	@JsonProperty("ClientID")
	long ClientID;
	@JsonProperty("UserAgent")
	String UserAgent;
	@JsonProperty("iDevice")
	String iDevice;
	@JsonProperty("SessionGuid")
	String SessionGuid;
	@JsonProperty("Site")
	String Site;
	@JsonProperty("CorrelationID")
	String CorrelationID;
	@JsonProperty("Host")
	long Host;
	//@JsonProperty("Geolocation")
	//Geolocation Geolocation;
	@JsonProperty("PrivateMode")
	long PrivateMode;
	@JsonProperty("HomeScreen")
	long HomeScreen;
	@JsonProperty("AppID")
	long AppID;
	@JsonProperty("AppVersion")
	String AppVersion;
	@JsonProperty("GameVersion")
	String GameVersion;
	
	
	/**
	 * @return the version
	 */
	public long getVersion() {
		return Version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(long version) {
		Version = version;
	}
	/**
	 * @return the userType
	 */
	public long getUserType() {
		return UserType;
	}
	/**
	 * @param userType the userType to set
	 */
	public void setUserType(long userType) {
		UserType = userType;
	}
	/**
	 * @return the userID
	 */
	public long getUserID() {
		return UserID;
	}
	/**
	 * @param userID the userID to set
	 */
	public void setUserID(long userID) {
		UserID = userID;
	}
	/*public Geolocation getGeolocation() {
		return Geolocation;
	}
	public void setGeolocation(Geolocation geolocation) {
		Geolocation = geolocation;
	}
	/**
	 * @return the sessionID
	 */
	public long getSessionID() {
		return SessionID;
	}
	/**
	 * @param sessionID the sessionID to set
	 */
	public void setSessionID(long sessionID) {
		SessionID = sessionID;
	}
	/**
	 * @return the productID
	 */
	public long getProductID() {
		return ProductID;
	}
	/**
	 * @param productID the productID to set
	 */
	public void setProductID(long productID) {
		ProductID = productID;
	}
	/**
	 * @return the serverID
	 */
	public long getServerID() {
		return ServerID;
	}
	/**
	 * @param serverID the serverID to set
	 */
	public void setServerID(long serverID) {
		ServerID = serverID;
	}
	/**
	 * @return the clientType
	 */
	public long getClientType() {
		return ClientType;
	}
	/**
	 * @param clientType the clientType to set
	 */
	public void setClientType(long clientType) {
		ClientType = clientType;
	}
	/**
	 * @return the moduleID
	 */
	public long getModuleID() {
		return ModuleID;
	}
	/**
	 * @param moduleID the moduleID to set
	 */
	public void setModuleID(long moduleID) {
		ModuleID = moduleID;
	}
	/**
	 * @return the clientID
	 */
	public long getClientID() {
		return ClientID;
	}
	/**
	 * @param clientID the clientID to set
	 */
	public void setClientID(long clientID) {
		ClientID = clientID;
	}
	/**
	 * @return the userAgent
	 */
	public String getUserAgent() {
		return UserAgent;
	}
	/**
	 * @param userAgent the userAgent to set
	 */
	public void setUserAgent(String userAgent) {
		UserAgent = userAgent;
	}
	/**
	 * @return the iDevice
	 */
	public String getiDevice() {
		return iDevice;
	}
	/**
	 * @param iDevice the iDevice to set
	 */
	public void setiDevice(String iDevice) {
		this.iDevice = iDevice;
	}
	/**
	 * @return the sessionGuid
	 */
	public String getSessionGuid() {
		return SessionGuid;
	}
	/**
	 * @param sessionGuid the sessionGuid to set
	 */
	public void setSessionGuid(String sessionGuid) {
		SessionGuid = sessionGuid;
	}
	/**
	 * @return the site
	 */
	public String getSite() {
		return Site;
	}
	/**
	 * @param site the site to set
	 */
	public void setSite(String site) {
		Site = site;
	}
	/**
	 * @return the correlationID
	 */
	public String getCorrelationID() {
		return CorrelationID;
	}
	/**
	 * @param correlationID the correlationID to set
	 */
	public void setCorrelationID(String correlationID) {
		CorrelationID = correlationID;
	}
	/**
	 * @return the host
	 */
	public long getHost() {
		return Host;
	}
	/**
	 * @param host the host to set
	 */
	public void setHost(long host) {
		Host = host;
	}
	
	/**
	 * @return the homeScreen
	 */
	public long getHomeScreen() {
		return HomeScreen;
	}
	/**
	 * @param homeScreen the homeScreen to set
	 */
	public void setHomeScreen(long homeScreen) {
		HomeScreen = homeScreen;
	}
	/**
	 * @return the appID
	 */
	public long getAppID() {
		return AppID;
	}
	/**
	 * @param appID the appID to set
	 */
	public void setAppID(long appID) {
		AppID = appID;
	}
	/**
	 * @return the appVersion
	 */
	public String getAppVersion() {
		return AppVersion;
	}
	/**
	 * @param appVersion the appVersion to set
	 */
	public void setAppVersion(String appVersion) {
		AppVersion = appVersion;
	}
	/**
	 * @return the gameVersion
	 */
	public String getGameVersion() {
		return GameVersion;
	}
	/**
	 * @param gameVersion the gameVersion to set
	 */
	public void setGameVersion(String gameVersion) {
		GameVersion = gameVersion;
	}
	
}
