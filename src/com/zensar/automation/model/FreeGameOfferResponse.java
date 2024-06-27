package com.zensar.automation.model;

public class FreeGameOfferResponse {
String offerId;
String costPerBet;
String offerName;
String offerAvailableFromUtcDate;
int defaultNumberOfGames;


public String getOfferId() {
	return offerId;
}
public void setOfferId(String offerId) {
	this.offerId = offerId;
}
public String getCostPerBet() {
	return costPerBet;
}
public void setCostPerBet(String costPerBet) {
	this.costPerBet = costPerBet;
}
public String getOfferName() {
	return offerName;
}
public void setOfferName(String offerName) {
	this.offerName = offerName;
}
public String getOfferAvailableFromUtcDate() {
	return offerAvailableFromUtcDate;
}
public void setOfferAvailableFromUtcDate(String offerAvailableFromUtcDate) {
	this.offerAvailableFromUtcDate = offerAvailableFromUtcDate;
}
public int getDefaultNumberOfGames() {
	return defaultNumberOfGames;
}
public void setDefaultNumberOfGames(int defaultNumberOfGames) {
	this.defaultNumberOfGames = defaultNumberOfGames;
}
}

