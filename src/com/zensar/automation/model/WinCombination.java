package com.zensar.automation.model;

public class WinCombination {
	
	int numSymbolsRequired;
	String symbols;
	int payout;
	
	/**
	 * @return the numSymbolsRequired
	 */
	public int getNumSymbolsRequired() {
		return numSymbolsRequired;
	}
	/**
	 * @param numSymbolsRequired the numSymbolsRequired to set
	 */
	public void setNumSymbolsRequired(int numSymbolsRequired) {
		this.numSymbolsRequired = numSymbolsRequired;
	}
	/**
	 * @return the symbols
	 */
	public String getSymbols() {
		return symbols;
	}
	/**
	 * @param symbols the symbols to set
	 */
	public void setSymbols(String symbols) {
		this.symbols = symbols;
	}
	/**
	 * @return the payout
	 */
	public int getPayout() {
		return payout;
	}
	/**
	 * @param payout the payout to set
	 */
	public void setPayout(int payout) {
		this.payout = payout;
	}
	

}
