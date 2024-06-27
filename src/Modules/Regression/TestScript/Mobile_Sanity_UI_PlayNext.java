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
 * Generic script
 *
 *@author PB61055
 */

public class Mobile_Sanity_UI_PlayNext {
	Logger log = Logger.getLogger(Mobile_Sanity_UI_PlayNext.class.getName());
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

		String Status = null;
		int mintDetailCount = 0;
		// int mintSubStepNo=0;
		int mintPassed = 0;
		int mintFailed = 0;
		int mintWarnings = 0;
		int mintStepNo = 0;

		ExcelDataPoolManager excelpoolmanager = new ExcelDataPoolManager();
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
							Thread.sleep(3000);
						
							log.debug("Updating the balance");
							String balance="10000000";
							
							if(cfnlib.xpathMap.get("LVC").equalsIgnoreCase("Yes")) 
							{
								util.migrateUser(userName);
								
								log.debug("Able to migrate user");
								System.out.println("Able to migrate user");
								
								log.debug("Updating the balance");
								balance="700000000000";
								Thread.sleep(120000);	
								Thread.sleep(120000);
							}
							
							util.updateUserBalance(userName,balance);
							Thread.sleep(3000);	
							
							String launchURl = url.replaceFirst("\\busername=.*?(&|$)", "username=" + userName + "$1");
							System.out.println("launchURl = " + launchURl);
				
							
						if (webdriver instanceof AndroidDriver) {
							webdriver.context("CHROMIUM");
						} else if (webdriver instanceof IOSDriver) {
							Set<String> contexts = webdriver.getContextHandles();
							for (String context : contexts) {
								if (context.startsWith("WEBVIEW")) {
									log.debug("context going to set in IOS is:" + context);
									webdriver.context(context);
								}
							}
						}
						// Launch URL
						webdriver.navigate().to(launchURl);
						Thread.sleep(15000);
						cfnlib.funcFullScreen();
						//Thread.sleep(5000);
						//cfnlib.closeOverlayForLVC();
						//Thread.sleep(5000);
						
						report.detailsAppend("*** CURRENCY AND LANGUAGE ***", "Currency: "+currencyName+", Language: "+languageCurrency,"", "");
						
						report.detailsAppendFolder("Verify Game launchaed ", "Game should be launched", "Game is launched","PASS",currencyName);
						
						webdriver.context("NATIVE_APP");
						

						ImageLibrary imageLibrary = new ImageLibrary(webdriver, gameName, "Mobile");

						imageLibrary.setScreenOrientation(webdriver.getOrientation().toString().toLowerCase());
										
						
						Thread.sleep(5000);
						try {
						
							// ==========================Tap to continue================================================
							if(cfnlib.xpathMap.get("NFD").equalsIgnoreCase("Yes"))
							{
							
							if (imageLibrary.isImageAppears("NFDButton")) {
								imageLibrary.click("NFDButton");
								report.detailsAppendFolder("Verify if able to click on continue",
										"Should be able to click on continue button",
										"Able to click on continue button", "PASS",currencyName);
							} else {
								report.detailsAppendFolder("Verify if able to click on continue",
										"Should be able to click on continue button",
										"Not able to click on continue button", "Fail",currencyName);
							}
						}
						}catch (Exception e) {
							log.error(e.getMessage(), e);
						}
						
					
						 webdriver.navigate().refresh();                                                                                                
                         Thread.sleep(5000);
                         cfnlib.funcFullScreen();
                         Thread.sleep(1000);                        
                         report.detailsAppendFolder("Verify refresh of base scene",
                                         "Should be able to refresh",
                                         "Refresh done", "PASS",currencyName);
                         webdriver.context("NATIVE_APP");
                         if(cfnlib.xpathMap.get("NFD").equalsIgnoreCase("Yes")) {
						
                        if (imageLibrary.isImageAppears("NFDButton")) {
                            imageLibrary.click("NFDButton");
                            report.detailsAppendFolder("Verify if able to click on continue",
                                            "Should be able to click on continue button",
                                            "Able to click on continue button", "PASS",currencyName);
                    } else {
                            report.detailsAppendFolder("Verify if able to click on continue",
                                            "Should be able to click on continue button",
                                            "Not able to click on continue button", "Fail",currencyName);
                    }
                         }
						webdriver.context("NATIVE_APP");

						// =========================Quick spin on base game======================================================
						Thread.sleep(3000);
						if(cfnlib.xpathMap.get("QuickSpinonBaseGame").equalsIgnoreCase("Yes"))
						{
						
						report.detailsAppend("Following is the QuickSpin verification test case", "Verify QuickSpin",
								"", "");
						if (imageLibrary.isImageAppears("QuickSpinOff")) 
						{
							Thread.sleep(2000);
							imageLibrary.click("QuickSpinOff");
							Thread.sleep(2000);
							report.detailsAppendFolder("Verify able to click on QuickSpin",
									"Should be able to click on QuickSpin button", "Able to click on QuickSpin button",
									"PASS",currencyName);
							
							//report.detailsAppendFolder("Win booster Button ", "Win booster Button should be clicked", "Win booster button is clicked", "PASS",currencyName);
							
						} else {
							report.detailsAppendFolder("Verify if able to click on QuickSpin",
									"Should be able to click on QuickSpin", "Not able to click on QuickSpin", "Fail",currencyName);
							//report.detailsAppendFolder("Win booster Button ", "Win booster Button should be clicked", "Win booster button is not clicked", "FAIL",currencyName);
							
						}
						}

						// ==================Autoplay menu========================================================================
						report.detailsAppend("Following are the Autoplay verification test cases", "Verify Autoplay",
								"", "");
					
						if (imageLibrary.isImageAppears("Autoplay")) 
						{
							Thread.sleep(2000);
							//Open Autoplay Panel
							imageLibrary.click("Autoplay");						
							Thread.sleep(5000);				         
							report.detailsAppendFolder("Verify able to click on Autoplay",
									"Should be able to click on Autoplay button", "Able to click on Autoplay button",
									"PASS",currencyName);
							webdriver.context("NATIVE_APP");
                            Thread.sleep(5000);        
                            webdriver.navigate().refresh();                                                                                                
                            Thread.sleep(5000);
                            cfnlib.funcFullScreen();
                            Thread.sleep(1000);                        
                            report.detailsAppendFolder("Verify refresh of Autoplay",
                                            "Should be able to refresh",
                                            "Refresh done", "PASS",currencyName);
                            webdriver.context("NATIVE_APP");
                            if(cfnlib.xpathMap.get("NFD").equalsIgnoreCase("Yes"))
                            {
                            
                            if (imageLibrary.isImageAppears("NFDButton")) {
                                    imageLibrary.click("NFDButton");
                                    report.detailsAppendFolder("Verify if able to click on continue",
                                                    "Should be able to click on continue button",
                                                    "Able to click on continue button", "PASS",currencyName);
                            } else {
                                    report.detailsAppendFolder("Verify if able to click on continue",
                                                    "Should be able to click on continue button",
                                                    "Not able to click on continue button", "Fail",currencyName);
                            }
                    }
                            imageLibrary.click("Autoplay");                                                
                            Thread.sleep(5000);        
							if (webdriver instanceof AndroidDriver) {
								webdriver.context("CHROMIUM");
							}
							else if (webdriver instanceof IOSDriver) {
								Set<String> contexts = webdriver.getContextHandles();
								for (String context : contexts) {
									if (context.startsWith("WEBVIEW")) {
										log.debug("context going to set in IOS is:" + context);
										webdriver.context(context);
									}
								}
							}
							
							cfnlib.AutoplayOptions(report,currencyName);
							
							
							webdriver.context("NATIVE_APP");
							Thread.sleep(5000);		
							if (imageLibrary.isImageAppears("StartAutoplay")) 
							{
								Thread.sleep(2000);
								imageLibrary.click("StartAutoplay");
								Thread.sleep(5000);
								report.detailsAppendFolder("Verify able to click StartAutoplay",
										"Should be able to click on StartAutoplay", "Able to click on StartAutoplay",
										"PASS",currencyName);
							} else {
								report.detailsAppendFolder("Verify if able to click on StartAutoplay",
										"Should be able to click on StartAutoplay", "Not able to click on StartAutoplay",
										"Fail",currencyName);
							}

							
								
							if (imageLibrary.isImageAppears("AutoplayStop")) 
							{
								
								imageLibrary.click("AutoplayStop");
								Thread.sleep(5000);
								report.detailsAppendFolder("Verify able to click on AutoplayStop",
										"Should be able to click on Autoplay button", "Able to click on AutoplayStop",
										"PASS",currencyName);
							} else {
								report.detailsAppendFolder("Verify if able to click on AutoplayStop",
										"Should be able to click on AutoplayStop", "Not able to click on AutoplayStop",
										"Fail",currencyName);
							}
							
						} else {
							report.detailsAppendFolder("Verify if able to click on Autoplay",
									"Should be able to click on Autoplay", "Not able to click on Autoplay", "Fail",currencyName);
						}

						/*if (webdriver instanceof AndroidDriver) {
							webdriver.context("CHROMIUM");
						} else if (webdriver instanceof IOSDriver) {
							Set<String> contexts = webdriver.getContextHandles();
							for (String context : contexts) {
								if (context.startsWith("WEBVIEW")) {
									log.debug("context going to set in IOS is:" + context);
									webdriver.context(context);
								}
							}
						}*/
					//	cfnlib.closeOverlayForLVC();
						
						
						
						// ===========================Spin=======================================================
						report.detailsAppend("Following are the Spin verification test cases", "Verify Spin", "", "");
						try {
							Thread.sleep(2000);
							if (imageLibrary.isImageAppears("Spin")) {
								imageLibrary.click("Spin");
								report.detailsAppendFolder("Verify able to click on Spin",
										"Should be able to click on spin button", "Able to click on spin button",
										"PASS",currencyName);
								System.out.println();
							} else {
								report.detailsAppendFolder("Verify if able to click on Spin",
										"Should be able to click on Spin", "Not able to click on Spin", "Fail",currencyName);
							}
						} catch (Exception e) {
							log.error(e.getMessage(), e);
						}
						
						
						
						webdriver.context("NATIVE_APP");
						
						
						// =========================BetMenu=======================================================
						report.detailsAppend("Following are the BetMenu verification test cases", "Verify BetMenu", "",
								"");

						if (imageLibrary.isImageAppears("BetMenu")) 
						{
							Thread.sleep(2000);
							//open betmenu panel
							imageLibrary.click("BetMenu");
							Thread.sleep(5000);
							
							report.detailsAppendFolder("Verify able to click on BetMenu",
									"Should be able to click on BetMenu button", "Able to click on BetMenu button",
									"PASS",currencyName);
                            webdriver.navigate().refresh();                                                                                                
                            Thread.sleep(5000);
                            cfnlib.funcFullScreen();
                            Thread.sleep(1000);                        
                            report.detailsAppendFolder("Verify refresh of BetMenu",
                                            "Should be able to refresh",
                                            "Refresh done", "PASS",currencyName);
                            webdriver.context("NATIVE_APP");
                            if(cfnlib.xpathMap.get("NFD").equalsIgnoreCase("Yes"))
                            {
                            
                            if (imageLibrary.isImageAppears("NFDButton")) {
                                    imageLibrary.click("NFDButton");
                                    report.detailsAppendFolder("Verify if able to click on continue",
                                                    "Should be able to click on continue button",
                                                    "Able to click on continue button", "PASS",currencyName);
                            } else {
                                    report.detailsAppendFolder("Verify if able to click on continue",
                                                    "Should be able to click on continue button",
                                                    "Not able to click on continue button", "Fail",currencyName);
                            }
                    }
                            
                            imageLibrary.click("BetMenu");
                            Thread.sleep(5000);
							if (webdriver instanceof AndroidDriver) {
								webdriver.context("CHROMIUM");
							}
							else if (webdriver instanceof IOSDriver) {
								Set<String> contexts = webdriver.getContextHandles();
								for (String context : contexts) {
									if (context.startsWith("WEBVIEW")) {
										log.debug("context going to set in IOS is:" + context);
										webdriver.context(context);
									}
								}
							}
							cfnlib.verifyBetSliders(report,currencyName);	
							Thread.sleep(3000);
							
						} else {
							report.detailsAppendFolder("Verify if able to click on BetMenu",
									"Should be able to click on BetMenu", "Not able to click on BetMenu", "Fail",currencyName);
						}

						// ===========================MaxBet=======================================================
						// report.detailsAppend("Following are the MaxBet verification test
						// cases","Verify MaxBet", "", "");
						
						
						webdriver.context("NATIVE_APP");
						Thread.sleep(2000);
						if (imageLibrary.isImageAppears("BetMax"))
						{
							Thread.sleep(2000);
							imageLibrary.click("BetMax");
							Thread.sleep(5000);
							report.detailsAppendFolder("Verify able to click on MaxBet",
									"Should be able to click on MaxBet button", "Able to click on MaxBet button",
									"PASS",currencyName);
						} else {
							report.detailsAppendFolder("Verify if able to click on MaxBet",
									"Should be able to click on MaxBet", "Not able to click on MaxBet", "Fail",currencyName);
						}

						

						
						
						// ===========================Menu=======================================================
						report.detailsAppend("Following are the Menu verification test cases", "Verify Menu", "", "");
					
						if (imageLibrary.isImageAppears("Menu")) 
						{
							Thread.sleep(2000);
							imageLibrary.click("Menu");
							Thread.sleep(5000);
							report.detailsAppendFolder("Verify able to click on Menu",
									"Should be able to click on Menu button", "Able to click on Menu button", "PASS",currencyName);
						} else {
							report.detailsAppendFolder("Verify if able to click on Menu", "Should be able to click on Menu",
									"Not able to click on Menu", "Fail",currencyName);
						}

						// ===========================Paytable=======================================================
						report.detailsAppend("Following are the Paytable verification test cases", "Verify Paytable",
								"", "");
						
						if (imageLibrary.isImageAppears("Paytable")) 
						{
							Thread.sleep(2000);
							imageLibrary.click("Paytable");
							Thread.sleep(5000);
							report.detailsAppendFolder("Verify able to click on Paytable",
									"Should be able to click on Paytable", "Able to click on Paytable",
									"PASS",currencyName);
							
						
							   Thread.sleep(5000);        
                               webdriver.navigate().refresh();                                                                                                
                               Thread.sleep(5000);
                               cfnlib.funcFullScreen();
                               Thread.sleep(1000);                        
                               report.detailsAppendFolder("Verify refresh of Paytable",
                                               "Should be able to refresh",
                                               "Refresh done", "PASS",currencyName);
                               webdriver.context("NATIVE_APP");
                               if(cfnlib.xpathMap.get("NFD").equalsIgnoreCase("Yes"))
                               {
                               
                               if (imageLibrary.isImageAppears("NFDButton")) {
                                       imageLibrary.click("NFDButton");
                                       report.detailsAppendFolder("Verify if able to click on continue",
                                                       "Should be able to click on continue button",
                                                       "Able to click on continue button", "PASS",currencyName);
                               } else {
                                       report.detailsAppendFolder("Verify if able to click on continue",
                                                       "Should be able to click on continue button",
                                                       "Not able to click on continue button", "Fail",currencyName);
                               }
                       }
                               imageLibrary.click("Menu");
                               Thread.sleep(5000);
                               imageLibrary.click("Paytable");
                               Thread.sleep(5000);
                               
							
							/*if (webdriver instanceof AndroidDriver) {
								webdriver.context("CHROMIUM");
							}
							else if (webdriver instanceof IOSDriver) {
								Set<String> contexts = webdriver.getContextHandles();
								for (String context : contexts) {
									if (context.startsWith("WEBVIEW")) {
										log.debug("context going to set in IOS is:" + context);
										webdriver.context(context);
									}
								}
							}*/
							
							// need to modify the method
							//cfnlib.verifyMenuOptions(report,currencyName);
							
							//===================Validate Paytable, Payouts and check currency format=====================
							if (webdriver instanceof AndroidDriver) {
								webdriver.context("CHROMIUM");
							} else if (webdriver instanceof IOSDriver) {
								Set<String> contexts = webdriver.getContextHandles();
								for (String context : contexts) {
									if (context.startsWith("WEBVIEW")) {
										log.debug("context going to set in IOS is:" + context);
										webdriver.context(context);
									}
								}
							}
							boolean scrollPaytable = cfnlib.paytableScroll(report, currencyName);
							
							// ===========================MenuBack=======================================================
							// report.detailsAppend("Following are the MenuBack verification test
							// cases","Verify MenuBack", "", "");
							
							webdriver.context("NATIVE_APP");
							imageLibrary.click("PaytableClose");
							
							
						}
						
						webdriver.context("NATIVE_APP");

						if (imageLibrary.isImageAppears("Menu")) 
						{
							Thread.sleep(2000);
							imageLibrary.click("Menu");
							Thread.sleep(5000);
						
							
						}
						
						// ===========================Settings=======================================================
						report.detailsAppend("Following are the Settings verification test cases", "Verify Settings",
								"", "");
						Thread.sleep(1000);
						if (imageLibrary.isImageAppears("Settings")) 
						{
							Thread.sleep(3000);
							imageLibrary.click("Settings");
							Thread.sleep(3000);
							report.detailsAppendFolder("Verify able to click on Settings",
									"Should be able to click on Settings button", "Able to click on Settings button",
									"PASS",currencyName);
						} else {
							report.detailsAppendFolder("Verify if able to click on Settings",
									"Should be able to click on Settings", "Not able to click on Settings", "Fail",currencyName);
							
                        }
                        
                        if (webdriver instanceof AndroidDriver) {
                                webdriver.context("CHROMIUM");
                        } else if (webdriver instanceof IOSDriver) {
                                Set<String> contexts = webdriver.getContextHandles();
                                for (String context : contexts) {
                                        if (context.startsWith("WEBVIEW")) {
                                                log.debug("context going to set in IOS is:" + context);
                                                webdriver.context(context);
                                        }
                                }
						}

						// ===========================MenuBack=======================================================
						// report.detailsAppend("Following are the MenuBack verification test
						// cases","Verify MenuBack", "", "");
						Thread.sleep(2000);
						if (imageLibrary.isImageAppears("Menu")) {
							imageLibrary.click("Menu");
							Thread.sleep(2000);
							imageLibrary.click("MenuBack");
							report.detailsAppendFolder("Verify able to click on MenuBack",
									"Should be able to click on MenuBack button", "Able to click on MenuBack button",
									"PASS",currencyName);
						} else {
							report.detailsAppendFolder("Verify if able to click on MenuBack",
									"Should be able to click on MenuBack", "Not able to click on MenuBack", "Fail",currencyName);
						}

						
						

						// ===========================HelpMenu=======================================================
						report.detailsAppend("Following are the HelpOnTopBar verification test cases",
								"Verify HelpOnTopBar", "", "");
						Thread.sleep(2000);
						/*if (imageLibrary.isImageAppears("HelpOnTopBar")) 
						{
							Thread.sleep(2000);
							imageLibrary.click("HelpOnTopBar");
							Thread.sleep(2000);
							report.detailsAppend("Verify able to click on HelpOnTopBar",
									"Should be able to click on HelpOnTopBar", "Able to click on HelpOnTopBar", "PASS");
							Thread.sleep(3000);
							if (imageLibrary.isImageAppears("HelpMenu")) {
								Thread.sleep(3000);
								imageLibrary.click("HelpMenu");
								Thread.sleep(3000);
								report.detailsAppend("Verify able to click on HelpMenu",
										"Should be able to click on HelpMenu", "Able to click on HelpMenu", "PASS");
							} else {
								report.detailsAppend("Verify if able to click on HelpMenu",
										"Should be able to click on HelpMenu", "Not able to click on HelpMenu", "Fail");
							}

						} else {
							report.detailsAppend("Verify if able to click on HelpOnTopBar",
									"Should be able to click on HelpOnTopBar", "Not able to click on HelpOnTopBar",
									"Fail");
						}
						*/
						if (webdriver instanceof AndroidDriver) {
							webdriver.context("CHROMIUM");
						} else if (webdriver instanceof IOSDriver) {
							Set<String> contexts = webdriver.getContextHandles();
							for (String context : contexts) {
								if (context.startsWith("WEBVIEW")) {
									log.debug("context going to set in IOS is:" + context);
									webdriver.context(context);
								}
							}
						}
						Thread.sleep(3000);
						if(cfnlib.xpathMap.get("HelpMenuPresent").equalsIgnoreCase("Yes"))
						{
							cfnlib.verifyHelpOnTopbar(report,currencyName);
						}
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
