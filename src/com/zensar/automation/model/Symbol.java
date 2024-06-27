package com.zensar.automation.model;

import java.util.ArrayList;

public class Symbol {
	
	int payout;
	
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

	public Symbol(String symbolName){
		this.symbolName = symbolName;
	}
	
	String symbolName;
	
	boolean payoutVerified =false;
		
	/**
	 * @return the payoutVerified
	 */
	public boolean isPayoutVerified() {
		return payoutVerified;
	}

	/**
	 * @param payoutVerified the payoutVerified to set
	 */
	public void setPayoutVerified(boolean payoutVerified) {
		this.payoutVerified = payoutVerified;
	}

	ArrayList<WinCombination> winCombList = new ArrayList<>();

	/**
	 * @return the symbolName
	 */
	public String getSymbolName() {
		return symbolName;
	}

	/**
	 * @param symbolName the symbolName to set
	 */
	public void setSymbolName(String symbolName) {
		this.symbolName = symbolName;
	}

	/**
	 * @return the winCombList
	 */
	public ArrayList<WinCombination> getWinCombList() {
		return winCombList;
	}

	/**
	 * @param winCombList the winCombList to set
	 */
	public void setWinCombList(ArrayList<WinCombination> winCombList) {
		this.winCombList = winCombList;
	}
	
	/**
	 * @param winCombList the winCombList to set
	 */
	public void addWinComb(WinCombination winCombination) {
		this.winCombList.add(winCombination);
	}
	
	
	

}
