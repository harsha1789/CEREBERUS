package com.zensar.automation.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Analytics {

	@JsonProperty("SessionInfo")
	SessionInfo SessionInfo=null;
	
	@JsonProperty("EventInfo")
	EventInfo EventInfo = null;

	public SessionInfo getSessionInfo() {
		return SessionInfo;
	}

	public EventInfo getEventInfo() {
		return EventInfo;
	}

	
}
