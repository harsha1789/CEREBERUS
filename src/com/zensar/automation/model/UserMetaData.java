package com.zensar.automation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class model hold the user meta data
 * @author sg56207
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserMetaData {

	@JsonProperty("userId")
	public String userID;

	@JsonProperty("loginName")
	public String loginName;
	@JsonProperty("serverId")
	public int serverID;
	@JsonProperty("balance")
	public double balance;
	@JsonProperty("productId")
	public int productId;
	
	@JsonProperty("moduleId")
	public int moduleId;
	@JsonProperty("clientId")
	public int clientId;
	@JsonProperty("DefaultNumChips")
	public int DefaultNumChips;
	@JsonProperty("MinBet")
	public int MinBet;
	@JsonProperty("MaxBet")
	public int MaxBet;
	@JsonProperty("DefaultChipSize")
	public int DefaultChipSize;
	
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public int getServerID() {
		return serverID;
	}
	public void setServerID(int serverID) {
		this.serverID = serverID;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	//Added by Harsha on 8th Nov 2022.
		public int getModuleID() {
			return moduleId;
		}
		public void setModuleID(int moduleId) {
			this.moduleId = moduleId;
		}
			
		public int getClientID() {
			return clientId;
		}
		public void setCLientID(int clientId) {
			this.clientId = clientId;
		}
		
		
		//DefaultNumChips,DefaultChipSize,MinBet,MaxBet
		
		public int getDefaultNumChips() {
			return DefaultNumChips;
		}
		public void setDefaultNumChips(int DefaultNumChips) {
			this.DefaultNumChips = DefaultNumChips;
		}
		
		public int getDefaultChipSize() {
			return DefaultChipSize;
		}
		public void setDefaultChipSize(int DefaultChipSize) {
			this.DefaultChipSize = DefaultChipSize;
		}
		
		public double getMinBet() {
			return MinBet;
		}
		public void setMinBet(int MinBet) {
			this.MinBet = MinBet;
		}
		
		public double getMaxBet() {
			return MaxBet;
		}
		public void setMaxBet(int MaxBet) {
			this.MaxBet = MaxBet;
		}
}
