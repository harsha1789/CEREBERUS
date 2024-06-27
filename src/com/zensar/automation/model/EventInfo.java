package com.zensar.automation.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EventInfo {
	@JsonProperty("Version")
	long Version;
	@JsonProperty("EventType")
	long EventType;
	@JsonProperty("EventSequence")
	long EventSequence;
	@JsonProperty("enabled")
	boolean enabled;
	@JsonProperty("item")
	String item;
	@JsonProperty("Duration")
	long Duration;
	@JsonProperty("ResponseDuration")
	long ResponseDuration;
	@JsonProperty("TransactionId")
	String TransactionId;
	@JsonProperty("SpinMethod")
	long SpinMethod;
	@JsonProperty("Orientation")
	long Orientation;
	@JsonProperty("CurrencyMode")
	long CurrencyMode;
	@JsonProperty("SpinSpeed")
	long SpinSpeed;
	@JsonProperty("GameSounds")
	long GameSounds;
	@JsonProperty("WinSounds")
	long WinSounds;
	@JsonProperty("GameloadType")
	long GameloadType;
	@JsonProperty("BackgroundSounds")
	long BackgroundSounds;
	
	
	
	
	public long getBackgroundSounds() {
		return BackgroundSounds;
	}
	public void setBackgroundSounds(long backgroundSounds) {
		BackgroundSounds = backgroundSounds;
	}
	public long getResponseDuration() {
		return ResponseDuration;
	}
	public void setResponseDuration(long responseDuration) {
		ResponseDuration = responseDuration;
	}
	public String getTransactionId() {
		return TransactionId;
	}
	public void setTransactionId(String transactionId) {
		TransactionId = transactionId;
	}
	public long getSpinMethod() {
		return SpinMethod;
	}
	public void setSpinMethod(long spinMethod) {
		SpinMethod = spinMethod;
	}
	public long getOrientation() {
		return Orientation;
	}
	public void setOrientation(long orientation) {
		Orientation = orientation;
	}
	public long getCurrencyMode() {
		return CurrencyMode;
	}
	public void setCurrencyMode(long currencyMode) {
		CurrencyMode = currencyMode;
	}
	public long getSpinSpeed() {
		return SpinSpeed;
	}
	public void setSpinSpeed(long spinSpeed) {
		SpinSpeed = spinSpeed;
	}
	public long getGameSounds() {
		return GameSounds;
	}
	public void setGameSounds(long gameSounds) {
		GameSounds = gameSounds;
	}
	public long getWinSounds() {
		return WinSounds;
	}
	public void setWinSounds(long winSounds) {
		WinSounds = winSounds;
	}
	public long getDuration() {
		return Duration;
	}
	public void setDuration(long duration) {
		Duration = duration;
	}
	public long getVersion() {
		return Version;
	}
	public void setVersion(long version) {
		Version = version;
	}
	public long getEventType() {
		return EventType;
	}
	public void setEventType(long eventType) {
		EventType = eventType;
	}
	public long getEventSequence() {
		return EventSequence;
	}
	public void setEventSequence(long eventSequence) {
		EventSequence = eventSequence;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public long getGameloadType() {
		return GameloadType;
	}
	public void setGameloadType(long gameloadType) {
		GameloadType = gameloadType;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	
	@Override
	public String toString() {
		StringBuffer eventInfoSBuffer = new StringBuffer();
		
		eventInfoSBuffer.append("Version : " +Version +"\n");
		eventInfoSBuffer.append("EventType : " +EventType +"\n");
		eventInfoSBuffer.append("Duratin : " +Duration +"\n");
		eventInfoSBuffer.append("EventSequence : " +EventSequence +"\n");
		eventInfoSBuffer.append("GameloadType : " +GameloadType +"\n");
		eventInfoSBuffer.append("EventType : " +EventType +"\n");
		return super.toString();
	}
	

	
	
	
}
