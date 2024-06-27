package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.ImageLibrary;
import com.zensar.automation.library.TestPropReader;
import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import net.lightbody.bmp.BrowserMobProxyServer;


/**
 * 
 * @author pb61055
 *
 */

public class Mobile_Sanity_UI_PlayNext_Modified {
	Logger log = Logger.getLogger(Mobile_Sanity_UI_PlayNext_Modified.class.getName());
	public ScriptParameters scriptParameters;

	public void script() throws Exception {

		String mstrTC_Name = scriptParameters.getMstrTCName();
		String mstrTC_Desc = scriptParameters.getMstrTCDesc();
		String mstrModuleName = scriptParameters.getMstrModuleName();
		BrowserMobProxyServer proxy = scriptParameters.getProxy();
		String startTime = scriptParameters.getStartTime();
		String filePath = scriptParameters.getFilePath();
		AppiumDriver<WebElement> webdriver = scriptParameters.getAppiumWebdriver();
		String DeviceName = scriptParameters.getDeviceName();
		String framework = scriptParameters.getFramework();
		String gameName = scriptParameters.getGameName();
		String osPlatform = scriptParameters.getOsPlatform();
		String osVersion = scriptParameters.getOsVersion();
		String userName = scriptParameters.getUserName();
		String urlNew = null;
		String Status = null;
		int mintDetailCount = 0;
		// int mintSubStepNo=0;
		int mintPassed = 0;
		int mintFailed = 0;
		int mintWarnings = 0;
		int mintStepNo = 0;

		Mobile_HTML_Report report = new Mobile_HTML_Report(webdriver, DeviceName, filePath, startTime, mstrTC_Name,
				mstrTC_Desc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings, mintStepNo, Status,
				gameName);

		log.info("Framework" + framework);
		MobileFunctionLibraryFactory mobileFunctionLibraryFactory = new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib = mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, report,
				gameName);
		CommonUtil util = new CommonUtil();
		RestAPILibrary apiObj = new RestAPILibrary();

		List<String> copiedFiles = new ArrayList<>();

		int mid = Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int cid = Integer.parseInt(TestPropReader.getInstance().getProperty("CIDMobile"));
		
		cfnlib.setOsPlatform(scriptParameters.getOsPlatform());

		try {
			if (webdriver != null)
			{					
				
				String strFileName=TestPropReader.getInstance().getProperty("MobileSanityTestDataPath");
				File testDataFile=new File(strFileName);
				
				
				List<Map<String, String>> currencyList = util.readCurrList();// mapping
				for (Map<String, String> currencyMap : currencyList) {

					try {
						String isoCode = currencyMap.get(Constant.ISOCODE).trim();
						String currencyID = currencyMap.get(Constant.ID).trim();
						String currencyName = currencyMap.get(Constant.ISONAME).trim();
						String languageCurrency = currencyMap.get(Constant.LANGUAGECURRENCY).trim();
						System.out.println("Context=" + webdriver.getContext());

						userName = util.randomStringgenerator();
						System.out.println(userName);
						
						String url = cfnlib.xpathMap.get("ApplicationURL");

						if(	util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles,currencyID,isoCode))
						{
													
							String launchURl = url.replaceFirst("\\busername=.*?(&|$)", "username=" + userName + "$1");

							if(launchURl.contains("LanguageCode"))
							urlNew = launchURl.replaceAll("LanguageCode=en", "LanguageCode=" + currencyName);
							else if(launchURl.contains("languagecode"))
							urlNew = launchURl.replaceAll("languagecode=en", "languagecode=" + currencyName);
							else if(launchURl.contains("languageCode"))
							urlNew = launchURl.replaceAll("languageCode=en", "languageCode=" + currencyName);

							log.info("url = " + urlNew);
							System.out.println(urlNew);

							
							report.detailsAppend("*** CURRENCY AND LANGUAGE ***", "Currency: "+currencyName+", Language: "+languageCurrency,"", "");
						
						
							webdriver.context("NATIVE_APP");
						

							ImageLibrary imageLibrary = new ImageLibrary(webdriver, gameName, "Mobile");

							imageLibrary.setScreenOrientation(webdriver.getOrientation().toString().toLowerCase());
							
							cfnlib.setChromiumWebViewContext();
							// Launch URL
							webdriver.navigate().to(urlNew);
							Thread.sleep(20000);
							cfnlib.funcFullScreen();
							Thread.sleep(5000);
							//cfnlib.closeOverlayForLVC();
							//Thread.sleep(5000);
						
							
							if ("Yes".equalsIgnoreCase(cfnlib.xpathMap.get("NFD"))) {
								
								report.detailsAppendFolder("Verify Continue button on base scene ",
										"Continue buttion", "Continue button", "Pass", currencyName);

								cfnlib.clickOnBaseSceneContinueButton(report, imageLibrary, currencyName);
							}	
					
						
							if(imageLibrary.isImageAppears("Spin"))
								report.detailsAppendFolder("Verify Base Scene", "Base Scene is visible", "Base Scene is visible", "Pass",currencyName );
							else
								report.detailsAppendFolder("Verify Base Scene", "Base Scene is visible", "Base Scene is not visible", "Fail",currencyName);							
						
									
							cfnlib.refreshGame(report, imageLibrary, currencyName);				
							
							//************** Quick Spin ********************
							
							if(cfnlib.xpathMap.get("QuickSpinonBaseGame").equalsIgnoreCase("Yes"))
							{
								report. detailsAppend("Following is the QuickSpin verification test case" ,"Verify QuickSpin", "", "");
								
								cfnlib.verifyQuickSpin(report, imageLibrary, currencyName);						
							}												
					
							//************** Autoplay *********************
							
							report.detailsAppend("Following are the Autoplay verification test cases", "Verify Autoplay","", "");
				
							cfnlib.verifyAutoplayPanel(report, imageLibrary, currencyName);
						
							//************** Bet panel ********************
							
							report.detailsAppend("Following are the BetMenu verification test cases", "Verify BetMenu", "","");

							cfnlib.verifyBetPanel(report, imageLibrary, currencyName);
					
					
							//************** Menu, Paytable and Settings *********************		
							
							report.detailsAppend("Following are the Menu verification test cases", "Verify Menu", "", "");
					
							cfnlib.openMenuPanel(report, imageLibrary, currencyName);
					
							cfnlib.closeButton(imageLibrary);

							cfnlib.closeButton(imageLibrary);
							
							cfnlib.verifyPaytable(report, imageLibrary, currencyName);
				
							cfnlib.verifySettingsPanel(report, imageLibrary, currencyName);
					
							//************** Help Navigation *********************
							
							report.detailsAppend("Following are the HelpOnTopBar verification test cases","Verify HelpOnTopBar", "", "");
					
							cfnlib.verifyHelpNavigation(report, imageLibrary, currencyName);
						
							}else
							{
								log.debug("Unable to Copy testdata");
								report.detailsAppendFolder("unable to copy test dat to server ", " ", "", "Fail", currencyName);
							}
						} catch (Exception e) {
						log.error("Exception occur while processing currency", e);
						report.detailsAppend("Exception occur while processing currency ", " ", "", "Fail");
						cfnlib.evalException(e);
					}

				} // for loop : mapping currencies
				// } // -------------------Handling the exception---------------------//
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		// -------------------Closing the connections---------------//
		finally {
			report.endReport();
			if(!copiedFiles.isEmpty())
			{
				if(TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
						util.deleteBluemesaTestDataFiles(mid,cid,copiedFiles);
				else
					apiObj.deleteAxiomTestDataFiles(copiedFiles);
			}
			// webdriver.close();
			webdriver.quit(); 
			// proxy.abort();
			Thread.sleep(1000);
		} // closing finally block

	}
}
