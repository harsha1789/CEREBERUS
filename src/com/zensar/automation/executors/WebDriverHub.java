package com.zensar.automation.executors;

import com.zensar.automation.framework.library.WebDriverManagerHub;

/*
 * This class will launch the hub at specified port in localhost
 * */
public class WebDriverHub {

	public static void main(String[] args)
	{
		WebDriverManagerHub hub=new WebDriverManagerHub();
		 hub.launchHub("4444") ; 
	}
}
