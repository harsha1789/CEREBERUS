package Modules.Regression.FunctionLibrary.factory;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.zensar.automation.framework.report.Desktop_HTML_Report;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop_CS;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop_CS_Renovate;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop_Force;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop_PlayNext;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 * 
 * Factory class creates desktop function library based on framework type.
 * @author ak47374
 *
 */

public class DesktopFunctionLibraryFactory {
	
	public static final Logger log = Logger.getLogger(DesktopFunctionLibraryFactory.class.getName());
	
	/**
	 * This method returns function library object based on framework type.
	 * @param framework
	 * @param webdriver
	 * @param proxy
	 * @param sanityReport
	 * @param gameName
	 * @return
	 */
	public CFNLibrary_Desktop getFunctionLibrary(String framework,WebDriver  webdriver, BrowserMobProxyServer proxy,Desktop_HTML_Report sanityReport ,String  gameName) 
	{
	CFNLibrary_Desktop cfnlib=null;
	
	log.debug("Framework received ::" + framework);

	try {
		if (framework.equalsIgnoreCase("PN")) {
			cfnlib = new CFNLibrary_Desktop(webdriver, proxy, sanityReport, gameName);
		} else if (framework.equalsIgnoreCase("Force")) {
			cfnlib = new CFNLibrary_Desktop_Force(webdriver, proxy, sanityReport, gameName);
		} else if (framework.equalsIgnoreCase("CS_Renovate")) {
			cfnlib = new CFNLibrary_Desktop_CS_Renovate(webdriver, proxy, sanityReport, gameName);
		} else if (framework.equalsIgnoreCase("PlayNext")) {
			cfnlib = new CFNLibrary_Desktop_PlayNext(webdriver, proxy, sanityReport, gameName);
		} else {
			cfnlib = new CFNLibrary_Desktop_CS(webdriver, proxy, sanityReport, gameName);
		}
	} catch (IOException e) {
		log.error("IOException Occured during Library creation::" ,e);
	}
	
	return cfnlib;

	}
}
