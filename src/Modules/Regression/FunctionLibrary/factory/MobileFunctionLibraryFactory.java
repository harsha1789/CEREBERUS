package Modules.Regression.FunctionLibrary.factory;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import com.zensar.automation.framework.report.Mobile_HTML_Report;

import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile_CS;
import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile_CS_Renovate;
import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile_Force;
import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile_PlayNext;
import io.appium.java_client.AppiumDriver;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 * 
 * Factory class creates mobile function library based on framework type.
 * @author ak47374
 *
 */

public class MobileFunctionLibraryFactory {
	
	public static final Logger log = Logger.getLogger(MobileFunctionLibraryFactory.class.getName());
	
	/**
	 * This method returns function library object based on framework type.
	 * @param framework
	 * @param webdriver
	 * @param proxy
	 * @param sanityReport
	 * @param gameName
	 * @return
	 */
	public CFNLibrary_Mobile getFunctionLibrary(String framework,AppiumDriver<WebElement>  webdriver, BrowserMobProxyServer proxy,Mobile_HTML_Report report ,String  gameName) {
	CFNLibrary_Mobile cfnlib=null;
	
	log.debug("Framework received ::" + framework);

	try {
		if(framework.equalsIgnoreCase("PN")){
			cfnlib=new CFNLibrary_Mobile(webdriver,proxy,report,gameName);
		}else if(framework.equalsIgnoreCase("CS_Renovate")){
			cfnlib=new CFNLibrary_Mobile_CS_Renovate(webdriver, proxy, report, gameName);
		}else if(framework.equalsIgnoreCase("Force")){
			cfnlib=new CFNLibrary_Mobile_Force(webdriver, proxy, report, gameName);
		}else if(framework.equalsIgnoreCase("PlayNext")){
			cfnlib=new CFNLibrary_Mobile_PlayNext(webdriver, proxy, report, gameName);
		}else{
			cfnlib=new CFNLibrary_Mobile_CS(webdriver,proxy,report,gameName);
		}
	} catch (IOException e) {
		log.error("IOException occured during mobile library creation::" ,e);
	}
	
	return cfnlib;

	}
}
