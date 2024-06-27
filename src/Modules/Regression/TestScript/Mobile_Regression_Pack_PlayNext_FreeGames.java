package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.ImageLibrary;
import com.zensar.automation.library.TestPropReader;
import com.zensar.automation.model.Currency;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import ch.qos.logback.core.net.SyslogOutputStream;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import net.lightbody.bmp.BrowserMobProxyServer;


/**
 *     
 * @author VC66297
 * @modified PB61055
 */
public class Mobile_Regression_Pack_PlayNext_FreeGames{
	
	public static Logger log = Logger.getLogger(Mobile_Regression_Pack_PlayNext_FreeGames.class.getName());
	public ScriptParameters scriptParameters;

	public void script() throws Exception {

		String mstrTCName=scriptParameters.getMstrTCName();
		String mstrTCDesc=scriptParameters.getMstrTCDesc();
		String mstrModuleName=scriptParameters.getMstrModuleName();
		BrowserMobProxyServer proxy=scriptParameters.getProxy();	
		String startTime=scriptParameters.getStartTime();
		String filePath=scriptParameters.getFilePath();
		String userName=scriptParameters.getUserName();
		String browserName=scriptParameters.getBrowserName();
		String framework=scriptParameters.getFramework();
		String gameName=scriptParameters.getGameName();
		AppiumDriver<WebElement> webdriver=scriptParameters.getAppiumWebdriver();
		String deviceName=scriptParameters.getDeviceName();
		String status=null;
		int mintDetailCount=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		String urlNew = null;

		ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();
		Mobile_HTML_Report report=new Mobile_HTML_Report(webdriver,deviceName,filePath,startTime,mstrTCName,mstrTCDesc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,status,gameName);
		
		MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, report, gameName);

		
		CommonUtil util = new CommonUtil();
		RestAPILibrary apiobj=new RestAPILibrary();
		
		List<String> copiedFiles=new ArrayList<>();
		cfnlib.setOsPlatform(scriptParameters.getOsPlatform());

		int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int  cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDMobile"));
	
		try {
			// -------Getting webdriver of proper test site-------//
			if (webdriver != null) 
			{

				

				
				
				//./Mobile_Regression_Language_Verification_FreeGames.testdata
				String strFileName=TestPropReader.getInstance().getProperty("MobileFreeGamesTestDataPath");
				File testDataFile=new File(strFileName);
				
				
				//assigning 2 free game offers to validate discard
				String noOfFreeGameOffers=cfnlib.xpathMap.get("noOfOffers");
				int noOfOffers=(int) Double.parseDouble(noOfFreeGameOffers);
				
				String offerExpirationUtcDate=util.getCurrentTimeWithDateAndTime();
				String strdefaultNoOfFreeGames=cfnlib.xpathMap.get("DefaultNoOfFreeGames");
				int defaultNoOfFreeGames=(int) Double.parseDouble(strdefaultNoOfFreeGames);
				
				
               
				List<Map<String, String>> currencyList = util.readCurrList();// mapping


				String url = cfnlib.xpathMap.get("ApplicationURL");
				for (Map<String, String> currencyMap : currencyList) {

					try {

						String isoCode = currencyMap.get(Constant.ISOCODE).trim();
						String currencyID = currencyMap.get(Constant.ID).trim();
						String currencyName = currencyMap.get(Constant.ISONAME).trim();
						String languageCurrency = currencyMap.get(Constant.LANGUAGECURRENCY).trim();
						String regExpr = currencyMap.get(Constant.REGEXPRESSION).trim();

						System.out.println(languageCurrency);
						log.debug(this + " I am processing currency:  " + currencyName);
											
						
						report.detailsAppend("*** CURRENCY AND LANGUAGE ***", "Currency: "+currencyName+", Language: "+languageCurrency,"", "");
						
						
						
						boolean isFreeGameAssigned=false;
						
						
							//Generating Random user
							//userName=util.randomStringgenerator();
							//System.out.println(userName);
						    //userName="Zen_1i6o4pe";
							
							webdriver.navigate().to(url);
							Thread.sleep(15000);
							
	
							//Copy Test data to server
							if(	util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles,currencyID,isoCode))
							{
								log.debug("Test dat is copy in test Server for Username="+userName);
								
										isFreeGameAssigned=cfnlib.assignFreeGames(userName,offerExpirationUtcDate,mid,cid,noOfOffers,defaultNoOfFreeGames);
										System.out.println("free games assigned: "+isFreeGameAssigned);
										Thread.sleep(15000);
										log.debug(" Currency Name :  "+currencyName+"Language Name :"+languageCurrency);		
										if(isFreeGameAssigned) 
										{
											log.debug("Updating the balance");
											String balance="10000000";
											
											
											
											if(cfnlib.xpathMap.get("LVC").equalsIgnoreCase("Yes")) 
											{
												util.migrateUser(userName);
												
												log.debug("Able to migrate user");
												System.out.println("Able to migrate user");
												
												log.debug("Updating the balance");
												balance="700000000000";
												Thread.sleep(60000);
											
											}
											
											util.updateUserBalance(userName,balance);
											Thread.sleep(3000);
											
											
											String launchURl = url.replaceFirst("\\busername=.*?(&|$)", "username=" + userName + "$1");
																														


											log.info("url "+ launchURl);
											System.out.println("url "+ launchURl);

											webdriver.navigate().to(launchURl);
											Thread.sleep(15000);
												//cfnlib.funcFullScreen();
												
												
												report.detailsAppend("Following are the FreeGames Entry Screen test cases", "Verify FreeGames Entry Screen", "", "");
												
												
												ImageLibrary imageLibrary = new ImageLibrary(webdriver, gameName, "Mobile");

												imageLibrary.setScreenOrientation(webdriver.getOrientation().toString().toLowerCase());
												
												// Check Play Later 
												webdriver.context("NATIVE_APP");
												Thread.sleep(3000);
												if (imageLibrary.isImageAppears("FGPlayLater")) 
												{
													
													report.detailsAppendFolder("Verify freegames entry screen",
															"freegames entry screen should display",
															"freegames entry screen is displaying","Pass",currencyName);
													
													boolean isfreeGameEntryInfoVisible = cfnlib.verifyRegularExpressionPlayNext(report,regExpr,cfnlib.freeGameEntryInfo(imageLibrary,"fgInfotxt"),isoCode);
													if (isfreeGameEntryInfoVisible)
													{
														System.out.println("Free Games Entry Screen Info Icon Text Validation : Pass");
														log.debug("Free Games Entry Screen Info Icon Text Validation : Pass");
														report.detailsAppendFolder("Verify freegames info currency format",
																"freegames info currency should display with correct currency format",
																"freegames info currency displaying with correct currency format","Pass",currencyName);
													}
													else
													{
														System.out.println("Free Games Entry Screen Info Icon Text Validation : Fail");
														log.debug("Free Games Entry Screen Info Icon Text Validation : Fail");
														report.detailsAppendFolder("Verify freegames info currency format",
																"freegames info currency should display with correct currency format",
																"freegames info currency displaying with incorrect currency format","Fail",currencyName);}
													
													Thread.sleep(2000);
													
													report.detailsAppendFolder("Verify FG Play Later button visible", "Play Later button Should be visible", "FG Play Later button is visible", "Pass",currencyName);
													
													webdriver.context("NATIVE_APP");
													Thread.sleep(2000);
													imageLibrary.click("FGPlayLater");
													
													//Thread.sleep(3000);
													
													if(cfnlib.xpathMap.get("NFD").equalsIgnoreCase("Yes"))
													{
														
															if (imageLibrary.isImageAppears("NFDButton"))
															{
																report.detailsAppendFolder("Verify Continue button is visible after clicking on FG Play Later ", "Continue buttion should be visible", "Continue button is visible", "Pass",currencyName);
																
																//Click on nfd button
																//cfnlib.closeOverlayForLVC();
																imageLibrary.click("NFDButton");
																Thread.sleep(3000);
																if (imageLibrary.isImageAppears("Spin"))
																{
																	System.out.println("Base Scene after FG Play Later");log.debug("Base Scene after FG Play Later");
																	report.detailsAppendFolder("Verify Free Games Base Scene is visible after clicking on FG Play Later", " Freegames BaseScene should be visible", "FreeGame BaseScene is visible", "Pass",currencyName);
																}
																else
																{
																	System.out.println("Base Scene after FG Play Later is not visible");log.debug("Base Scene after FG Play Later is not visible");
																	report.detailsAppendFolder("Verify Free Games Base Scene is visible after clicking on FG Play Later", " Freegames BaseScene should be visible", "FreeGame BaseScene is not visible", "Fail",currencyName);
																	}
															}
															else
															{
																report.detailsAppendFolder("Verify Continue button is visible after clicking on FG Play Later ", "Continue buttion should be visible", "Continue button is not visible", "Fail",currencyName);
															}
														
													}
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
												Thread.sleep(3000);
												//webdriver.navigate().to(urlNew);
												webdriver.navigate().refresh();												
												Thread.sleep(5000);
												//cfnlib.funcFullScreen();
												Thread.sleep(1000);
												webdriver.context("NATIVE_APP");
												Thread.sleep(1000);
												if (imageLibrary.isImageAppears("FGDelete"))
												{	
													imageLibrary.click("FGDelete");
													report.detailsAppendFolder("Verify Game launched after refresh to check Discard FG Offer", "Game should be launched", "Game is launched", "Pass",currencyName);										
														
														if (imageLibrary.isImageAppears("FGDiscard"))
														{	
															report.detailsAppendFolder("Check that Discard Offer screen is displaying", "Discard Offer screen should display", "Discard Offer screen is displaying", "Pass", currencyName);
															
																
															cfnlib.confirmDiscardOffer(imageLibrary);
															
															
															if (imageLibrary.isImageAppears("NFDButton"))
																report.detailsAppendFolder("Check that Next free games offer entry screen is displaying", "Next Free Games entry screen should display","Free Game entry screen is displaying", "Pass", currencyName);
															else 
																report.detailsAppendFolder("Check that Next free games offer entry screen is displaying", "Next Free Games entry screen should display","Free Game entry screen is not displaying", "Fail", currencyName);
														
															
														}
														else
														{
															report.detailsAppendFolder("Check that Discard Offer screen is displaying", "Discard Offer screen should display", "Discard Offer screen is not displaying", "Fail", currencyName);
														}												
																						
												}
												else
												{
													report.detailsAppendFolder("Verify Game launchaed after refresh to check Discard FG Offer ", "Game should be launched", "Game is not launched", "Fail",currencyName);
												}				
										
												//Click on continue button
											//	cfnlib.closeOverlayForLVC();
												webdriver.context("NATIVE_APP");
												if (imageLibrary.isImageAppears("FGPlayNow")) {
													Thread.sleep(1000);
													
													
													
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
													//String test=cfnlib.funcGetText("FGPlayNow");
													
													
													
													
													
													imageLibrary.click("FGPlayNow");
													
													if(cfnlib.xpathMap.get("NFD").equalsIgnoreCase("Yes"))
													{
														
															if (imageLibrary.isImageAppears("NFDButton"))
															{
																report.detailsAppendFolder("Verify Continue button is visible after clicking on FG Play Later ", "Continue buttion should be visible", "Continue button is visible", "Pass",currencyName);
																
																//Click on nfd button
																//cfnlib.closeOverlayForLVC();
																imageLibrary.click("NFDButton");
																Thread.sleep(3000);
															}
															
													}		
												
												
													report.detailsAppend("Following is the FreeGames Credit value verification test case","Verify Credit value in free games", "", "");
													
													//log.debug("Free Game Credit Value : "+cfnlib.funcGetText("Creditvalue"));
													
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
													Thread.sleep(5000);
													boolean credits = cfnlib.verifyRegularExpressionPlayNext(report, regExpr,
															cfnlib.funcGetText("CreditValue"),isoCode);
											
													if (credits) {
														System.out.println("Free Game Credit Value : Pass");
														log.debug("Free Game Credit Value : Pass");
														report.detailsAppendFolder("Verify the currency format in credit ",
														"Credit should display in correct currency format " ,
																"Credit is displaying in correct currency format ", "Pass",currencyName);
										
																
													} else {
														System.out.println("Free Game Credit Value : Fail");
														log.debug("Free Game Credit Value : Fail");
														report.detailsAppendFolder("Verify the currency format in credit ",
																"Credit should display in correct currency format " ,
																"Credit is displaying in incorrect currency format ", "Fail",currencyName);
													}

													Thread.sleep(4000);	
													webdriver.context("NATIVE_APP");
													report.detailsAppend("Following are the FreeGames win test cases", "Verify FreeGames win in base scene", "", "");
												
											
										
												
												//To verify normal win in base game
												try
												{
													if (imageLibrary.isImageAppears("Spin"))
													{
														System.out.println("Free Game Scene is visible");
														log.debug("Free Game Scene is visible");
														report.detailsAppendFolder("Verify Free Game Scene after clicking Play Now", "Free Game Scene should be visible", "Free Game Scene is visible", "Pass",currencyName);
																												
													
														cfnlib.spinclick(imageLibrary);							
														Thread.sleep(4000);	
										
														
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
														boolean winFormatVerification = cfnlib.verifyRegularExpressionPlayNext(report, regExpr,cfnlib.getCurrentWinAmt(report, currencyName),isoCode);
														if (winFormatVerification) 
														{
															report.detailsAppendFolder("verify win amount currency format in free games", "Win amount should be in correct currency format","Win amount is in correct currency format", "Pass",currencyName);
														log.debug("Free Game Win Value : Pass");
														System.out.println("Free Game Win Value : Pass");
															
															}
														else 
														{
															report.detailsAppendFolder("verify win amount in free games", "Win amount should be in correct currency format","Win amount is in incorrect currency format", "Fail",currencyName);
															log.debug("Free Game Win Value : Fail");
															System.out.println("Free Game Win Value : Fail");
															
														}
													}
												} catch (Exception e) {
													log.error(e.getMessage(), e);
												}
										
												
												webdriver.context("NATIVE_APP");
												//To verify big win in base game
												try {
												//method is used to Spins to get Big win and check the currency 
												if(cfnlib.xpathMap.get("isBigWinAvailable").equalsIgnoreCase("Yes"))	
												{ 
													
													if (imageLibrary.isImageAppears("Spin")) 
														{
															cfnlib.spinclick(imageLibrary);		
															Thread.sleep(8000);
												
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
															// Verifies the Big Win currency format
															boolean bigWinFormatVerification = cfnlib.verifyRegularExpressionPlayNext(report,regExpr, cfnlib.verifyBigWin(report, currencyName),isoCode);
													if (bigWinFormatVerification) 
													{
														report.detailsAppendFolder("verify bigwin amount currency format in free games", "BigWin amount should be in correct currency format","BigWin amount is in correct currency format", "Pass",currencyName);
														log.debug("Free Game BigWin Value : Pass");
														System.out.println("Free Game BigWin Value : Pass");
													
													} else 
													{
														report.detailsAppendFolder("verify bigwin amount currency format in free games", "BigWin amount should be in correct currency format","BigWin amount is in incorrect currency format", "Fail",currencyName);
														log.debug("Free Game BigWin Value : Fail");
														System.out.println("Free Game BigWin Value : Fail");													
													}

														}
												}
													} catch (Exception e) {
														log.error(e.getMessage(), e);
													}
												

												//To verify Amazing win in base game
													try {
														if(cfnlib.xpathMap.get("isBonusWinInFreeGames").equalsIgnoreCase("Yes"))
														{
															if (imageLibrary.isImageAppears("Spin")) 
															{
																cfnlib.spinclick(imageLibrary);	
																Thread.sleep(8000);
												
																// method is used to get the Win amt and check the currency format
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
																boolean winVerification = cfnlib.verifyRegularExpressionPlayNext(report, regExpr,cfnlib.verifyBonusWin(report, currencyName),isoCode);
																if (winVerification) 
																{
																	report.detailsAppendFolder("verify Bonus win amount currency format in free games", "Bonus win amount should be in correct currency format","Bonus win amount is in correct currency format", "Pass",currencyName);
																	System.out.println("Free Game bonue Win Value : Pass");
																	log.debug("Free Game Win Value : Pass");
													
																} else 
																{
																	report.detailsAppendFolder("verify Bonus win amount currency format in free games", "Bonus win amount should be in correct currency format","Bonus win amount is in incorrect currency format", "Fail",currencyName);
																	System.out.println("Free Game bonus Win Value : Fail");
																	log.debug("Free Game bonus Win Value : Fail");
													
																}
											
															} else 
															{
																report.detailsAppendFolder("verify Free games Bonus win","Spin Button is not working", "Spin Button is not working", "Fail",currencyName);

															}
														}
													} catch (Exception e) {
														log.error(e.getMessage(), e);
														cfnlib.evalException(e);
													}
												
																	
										
										Thread.sleep(8000);
										
										if(cfnlib.xpathMap.get("gameHasOverlayAfterWin").equalsIgnoreCase("Yes"))
										{
											cfnlib.closeOverlayForLVC();
											Thread.sleep(1000);
											cfnlib.closeOverlayForLVC();
											
										}
										
										
										webdriver.context("NATIVE_APP");
										
										if (imageLibrary.isImageAppears("FreeGameBaseDiscard"))
										{	
											imageLibrary.click("FreeGameBaseDiscard");
											report.detailsAppendFolder("Verify Discard FG Offer on base scene", "Verify Discard FG Offer on base scene", "Verify Discard FG Offer on base scene", "Pass",currencyName);										
												
												if (imageLibrary.isImageAppears("FreeGameDiscardOffer"))
												{	
													report.detailsAppendFolder("Check that Discard Offer screen is displaying", "Discard Offer screen should display", "Discard Offer screen is displaying", "Pass", currencyName);
													
													imageLibrary.click("FreeGameDiscardOffer");
												}
												else
												{
													report.detailsAppendFolder("Check that Discard Offer screen is displaying", "Discard Offer screen should display", "Discard Offer screen is not displaying", "Fail", currencyName);
												}												
																				
										}
										else
										{
											report.detailsAppendFolder("Verify Game launchaed after refresh to check Discard FG Offer ", "Game should be launched", "Game is not launched", "Fail",currencyName);
										}			
										Thread.sleep(5000);	
										cfnlib.clickBaseSceneDiscardButton();
										report.detailsAppendFolder("Verify Discard offer is visible on baseScene", "Discard offer should be visible on baseScene", "Discard offer is visible on baseScene", "Pass", currencyName);
										
										boolean isOfferDiscarded = cfnlib.confirmDiscardOffer();
										if(isOfferDiscarded)
											report.detailsAppendFolder("Check that Offer is discarded successfully and Free Games Summary Screen should display", "Offer should be discarded successfully and Free Games Summary Screen should display", "Offer is discarded and Free Games Summary Screen is displaying", "Pass", currencyName);
										else
											report.detailsAppendFolder("Check that Offer is discarded successfully and Free Games Summary Screen should display", "Offer should be discarded successfully and Free Games Summary Screen should display", "Offer is not discarded and Free Games Summary Screen is not displaying", "Fail", currencyName);

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
										boolean FGSummaryWonAmount = cfnlib.verifyRegularExpressionPlayNext
												(report,regExpr,cfnlib.GetConsoleText("return "+cfnlib.xpathMap.get("FGSummaryAmount")),isoCode);
										if(FGSummaryWonAmount)
										{
											System.out.println("Free Game Summary won Amount : Pass");
											log.debug("Free Game Summary won Amount : Pass");
											report.detailsAppendFolder("Verify freegames summary currency when win occurs ",
													"freegames summary win should display in correct currency format",
													"freegames summary win displaying in correct currency format","Pass",currencyName);
										}
										else
										{
											System.out.println("Free Game Summary won Amount : Pass");
											log.debug("Free  Game Summary won Amount : Fail");
											report.detailsAppendFolder("Verify freegames summary currency when win occurs ",
													"freegames summary win should display with correct currency format",
													"freegames summary win displaying in incorrect currency format","Fail",currencyName);
										}
												}
												else
												{
													System.out.println("Free Game Scene is not visible");
													log.debug("Free Game Scene is not visible");
													report.detailsAppendFolder("Verify Free Game Scene after clicking Play Now", "Free Game Scene should be visible", "Free Game Scene is not visible", "Fail",currencyName);
												}	
										
									
							
									
								}else 
								{
									log.error("Skipping the execution as free games assignment Failed");
									report.detailsAppendFolder("skipping the execution as free games assignment Failed ", " ", "", "Fail", currencyName);
								}
					
							}else
							{
								log.debug("Unable to Copy testdata");
								report.detailsAppendFolder("unable to copy test dat to server ", " ", "", "Fail", currencyName);
							}
						
				
					
					System.out.println("Done for free games");
							
					
					
					} // try
					catch (Exception e) {
						log.error("Exception occur while processing currency", e);
						report.detailsAppend("Exception occur while processing currency ", " ", "", "Fail");
						cfnlib.evalException(e);
					}

				} // for loop : mapping currencies

			}
		}
		// -------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error(e.getMessage(), e);
			cfnlib.evalException(e);
		} 
		finally {
			report.endReport();
			if(!copiedFiles.isEmpty())
			{
				if(TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
						util.deleteBluemesaTestDataFiles(mid,cid,copiedFiles);
				else
						apiobj.deleteAxiomTestDataFiles(copiedFiles);
			}
			webdriver.close();
			webdriver.quit();
			//proxy.abort();
			Thread.sleep(1000);
		}
	}
}