package com.zensar.automation.model;

public class HelpFileCount {
	
	int liCount;
	int pCount;
	int divCount;
	int tableCount;
	int trCount;
	int tdCount;
	
	
	public int getLiCount() {
		return liCount;
	}


	public void setLiCount(int liCount) {
		this.liCount = liCount;
	}


	public int getpCount() {
		return pCount;
	}


	public void setpCount(int pCount) {
		this.pCount = pCount;
	}


	public int getDivCount() {
		return divCount;
	}


	public void setDivCount(int divCount) {
		this.divCount = divCount;
	}


	public int getTableCount() {
		return tableCount;
	}


	public void setTableCount(int tableCount) {
		this.tableCount = tableCount;
	}


	public int getTrCount() {
		return trCount;
	}


	public void setTrCount(int trCount) {
		this.trCount = trCount;
	}


	public int getTdCount() {
		return tdCount;
	}


	public void setTdCount(int tdCount) {
		this.tdCount = tdCount;
	}


	@Override
	public String toString() {
		return "HelpFileCount [divCount=" + divCount + ", liCount=" + liCount + ", pCount=" + pCount + ", tableCount="
				+ tableCount + ", tdCount=" + tdCount + ", trCount=" + trCount + "]";
	}

}
