package com.zensar.automation.model;

public class Currency {
	String currencyID;
	String isoCode;
	String isoName;
	String currencyFormat;
	String currencyMultiplier;
	String status;
	String currencySymbol;
	
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the currencyID
	 */
	public String getCurrencyID() {
		return currencyID;
	}
	/**
	 * @param currencyID the currencyID to set
	 */
	public void setCurrencyID(String currencyID) {
		this.currencyID = currencyID;
	}
	/**
	 * @return the isoCode
	 */
	public String getIsoCode() {
		return isoCode;
	}
	/**
	 * @param isoCode the isoCode to set
	 */
	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}
	/**
	 * @return the isoName
	 */
	public String getIsoName() {
		return isoName;
	}
	/**
	 * @param isoName the isoName to set
	 */
	public void setIsoName(String isoName) {
		this.isoName = isoName;
	}
	/**
	 * @return the currencyFormat
	 */
	public String getCurrencyFormat() {
		return currencyFormat;
	}
	/**
	 * @param currencyFormat the currencyFormat to set
	 */
	public void setCurrencyFormat(String currencyFormat) {
		this.currencyFormat = currencyFormat;
	}
	/**
	 * @return the currencyMultiplier
	 */
	public String getCurrencyMultiplier() {
		return currencyMultiplier;
	}
	/**
	 * @param currencyMultiplier the currencyMultiplier to set
	 */
	public void setCurrencyMultiplier(String currencyMultiplier) {
		this.currencyMultiplier = currencyMultiplier;
	}
	
	public String getCurrencySymbol()
	{
		return currencySymbol;
	}
	
	public void getCurrencySymbol(String currencySymbol)
	{
		this.currencySymbol = currencySymbol;
	}
	
	
	
	
	

}
