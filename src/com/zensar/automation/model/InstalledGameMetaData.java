package com.zensar.automation.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/*This object model refers to currently  installed game on axiom environment*/

@JsonIgnoreProperties(ignoreUnknown = true)
public class InstalledGameMetaData {

		@JsonProperty("success")
		public String strSuccess=null;
		
		@JsonProperty("customMessage")
		public String strcustomMessage=null;
		
		@JsonProperty("dataObject")
		List<GameMetaData> dataObject=null;
		
		
		public String getStrSuccess() {
			return strSuccess;
		}

		public void setStrSuccess(String strSuccess) {
			this.strSuccess = strSuccess;
		}

		public List<GameMetaData> getDataObject() {
			return dataObject;
		}

		public void setDataObject(List<GameMetaData> dataObject) {
			this.dataObject = dataObject;
		}

		public String getStrcustomMessage() {
			return strcustomMessage;
		}

		public void setStrcustomMessage(String strcustomMessage) {
			this.strcustomMessage = strcustomMessage;
		}

		

	
}
