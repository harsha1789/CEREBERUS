package com.zensar.automation.framework.api;

public class DeviceNotAvailableException extends Exception {

	/**
	 * default serial version
	 */
	private static final long serialVersionUID = 1L;
	
	public DeviceNotAvailableException(String msg)
	{
		super(msg);
	}
	public DeviceNotAvailableException(String msg,Throwable throwable)
	{
		super(msg,throwable);
	}

}
