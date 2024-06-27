package com.zensar.automation.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@SuppressWarnings("serial")
@JsonIgnoreProperties

public class PresetResponse implements Serializable {
	
	Preset[] presets=null;

	public Preset[] getPresets() {
		return presets;
	}

	public void setPresets(Preset[] presets) {
		this.presets = presets;
	}

}
