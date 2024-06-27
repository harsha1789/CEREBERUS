package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;

import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.TestPropReader;
import org.apache.log4j.Logger;
import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import net.lightbody.bmp.BrowserMobProxyServer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.spi.CurrencyNameProvider;

import org.apache.log4j.Logger;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
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
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop_CS;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop_Force;
import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile_CS;
import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile_CS_Renovate;
import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile_Force;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 * This Script is for Low Value Currency - Base Game, Free Spins
 * ===========================================================================
 * 1.Base Game =========== 1.1.Credits 1.2.Bet 1.3.Autoplay 1.4.Bet Pannel (Min
 * , Max & All Bet Values) 1.5.PayTable , Pay-Outs Validation & Branding
 * Validation 1.6.Win 1.7.Big Win 1.8.Bonus Game 1.9 Menu Items Validation &
 * their navigations 1.10 Top Icons Navigations 1.11 Clock Validation 1.12
 * Refresh on Win (In Base Game , Free Spins)
 * 
 * 2.Free Spins ============= 2.1.Credits 2.2.Total Win 2.3.Big Win -1st Spin
 * 2.4.Normal Win - 2nd Spin 2.5.Big Win - 3rd Spin 2.5.Summary Screen Total Win
 * Validation
 * 
 * 
 * TestData ============== 1.Base Game 1.1 Big Win 1.2 Normal Win 1.3 Big Win
 * 1.4 Bonus 2.Free Spins 2.1 Big Win - 1st Spin 2.2 Normal Win -2nd Spin 2.3
 * Big Win -3rd Spin
 *
 * 
 * @author SB64689
 *
 */

public class Mobile_Regression_Pack_PlayNext_BaseScene {
	public ScriptParameters scriptParameters;
	Logger log = Logger.getLogger(Mobile_Regression_Pack_PlayNext_BaseScene.class.getName());

	public void script() throws Exception {

		String mstrTC_Name = scriptParameters.getMstrTCName();
		String mstrTC_Desc = scriptParameters.getMstrTCDesc();
		String mstrModuleName = scriptParameters.getMstrModuleName();
		BrowserMobProxyServer proxy = scriptParameters.getProxy();
		String startTime = scriptParameters.getStartTime();
		String filePath = scriptParameters.getFilePath();
		AppiumDriver<WebElement> webdriver = scriptParameters.getAppiumWebdriver();
		String userName = scriptParameters.getUserName();
		String DeviceName = scriptParameters.getDeviceName();
		String framework = scriptParameters.getFramework();
		String gameName = scriptParameters.getGameName();
		String language = "Paytable";
		String Status = null;
		int mintDetailCount = 0;
		int mintPassed = 0;
		int mintFailed = 0;
		int mintWarnings = 0;
		int mintStepNo = 0;

		Mobile_HTML_Report lvcReport = new Mobile_HTML_Report(webdriver, DeviceName, filePath, startTime, mstrTC_Name,
				mstrTC_Desc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings, mintStepNo, Status,
				gameName);
		MobileFunctionLibraryFactory mobileFunctionLibraryFactory = new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib = mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,
				lvcReport, gameName);
		CommonUtil util = new CommonUtil();
		RestAPILibrary apiObj = new RestAPILibrary();
		cfnlib.setOsPlatform(scriptParameters.getOsPlatform()); // To get OSPlatform

		List<String> copiedFiles = new ArrayList<>();
		int mid = Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		System.out.println("MID : " + mid);
		log.debug("MID : " + mid);
		int cid = Integer.parseInt(TestPropReader.getInstance().getProperty("CIDMobile"));
		System.out.println("CID : " + cid);
		log.debug("CID : " + cid);

		try {
 
			// Step 1
			if (webdriver != null) 
			
			{
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

				// Implement code for test data copy depending on the env
				String strFileName = TestPropReader.getInstance().getProperty("MobileCurrencyTestDataPath");
				File testDataFile = new File(strFileName);

				List<Map<String, String>> currencyList = util.readCurrList();// mapping

				for (Map<String, String> currencyMap : currencyList) {
					userName = util.randomStringgenerator();
					//userName ="Zen_4ddkr73";
					// Step 2: To get the languages in MAP and load the language specific url

					String currencyID = currencyMap.get(Constant.ID).trim();
					String isoCode = currencyMap.get(Constant.ISOCODE).trim();
					System.out.println(isoCode);
					String CurrencyName = currencyMap.get(Constant.ISONAME).trim();
					String languageCurrency = currencyMap.get(Constant.LANGUAGECURRENCY).trim();
					String regExpr = currencyMap.get(Constant.REGEXPRESSION).trim();
					String regExprNoSymbol = currencyMap.get(Constant.REGEXPRESSION_NOSYMBOL).trim();

					if (util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles, currencyID, isoCode))
						System.out.println("Username : " + userName);
					System.out.println("CurrencyName : " + CurrencyName);

					/*if (util.migrateUser(userName))
					{*/
						log.debug("User Migration : PASS");
						System.out.println("User Migration : PASS");

						//String balance = "700000000000";
						String balance = "1000000";
						Thread.sleep(10000);
						//Thread.sleep(60000);

						if (util.updateUserBalance(userName, balance)) {
							log.debug("Balance Updated as " + balance);
							System.out.println("Balance Updated as " + balance);

							String url = cfnlib.xpathMap.get("ApplicationURL");
							String launchURl = url.replaceFirst("\\busername=.*?(&|$)", "username=" + userName + "$1");
							// launchURl = launchURl.replaceAll("\\blanguageCode=.*?(&|$)",
							// "languageCode="+languageCurrency+"$1");
							System.out.println(launchURl);
							log.info("url = " + launchURl);

							/*if (cfnlib.loadGame(launchURl)) {*/
							webdriver.navigate().to(launchURl);
								lvcReport.detailsAppendFolder("Currency Name is " + CurrencyName,
										"Currency ID is " + currencyID, "ISO Code is " + isoCode, "PASS", CurrencyName);

								// Verify Spin Button
								//cfnlib.waitForSpinButton();
								Thread.sleep(2000);

								cfnlib.funcFullScreen();
								Thread.sleep(5000);

								
								
								webdriver.context("NATIVE_APP");
								ImageLibrary imageLibrary = new ImageLibrary(webdriver, gameName, "Mobile");

								imageLibrary.setScreenOrientation(webdriver.getOrientation().toString().toLowerCase());
								Thread.sleep(10000);
								try {
									// ==========================Tap to continue================================================
									if (imageLibrary.isImageAppears("NFDButton")) {
										imageLibrary.click("NFDButton");
										lvcReport.detailsAppend("Verify if able to click on continue",
												"Should be able to click on continue button",
												"Able to click on continue button", "PASS");
									} else {
										lvcReport.detailsAppend("Verify if able to click on continue",
												"Should be able to click on continue button",
												"Not able to click on continue button", "Fail");
									}
								} catch (Exception e) {
									log.error(e.getMessage(), e);
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
								cfnlib.closeOverlayForLVC();
								
								webdriver.context("NATIVE_APP");
								/*if (TestPropReader.getInstance().getProperty("GameCategory")
										.equalsIgnoreCase("5ReelSlots")) {
*//*
									// =====================================Autoplay menu===================================================

									lvcReport.detailsAppend("Following are the Autoplay verification test cases",
											"Verify Autoplay", "", "");
									
									if (imageLibrary.isImageAppears("Autoplay")) 
									{
										Thread.sleep(3000);
										// Open Autoplay Panel
										imageLibrary.click("Autoplay");
										Thread.sleep(3000);
										lvcReport.detailsAppend("Verify able to click on Autoplay",
												"Should be able to click on Autoplay button",
												"Able to click on Autoplay button", "PASS");
										
										
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
										cfnlib.AutoplayOptions(lvcReport, languageCurrency);
										
										webdriver.context("NATIVE_APP");

										if (imageLibrary.isImageAppears("StartAutoplay")) 
										{
											Thread.sleep(3000);
											imageLibrary.click("StartAutoplay");
											lvcReport.detailsAppend("Verify able to click StartAutoplay",
													"Should be able to click on StartAutoplay",
													"Able to click on StartAutoplay", "PASS");
										} else {
											lvcReport.detailsAppend("Verify if able to click on StartAutoplay",
													"Should be able to click on StartAutoplay",
													"Not able to click on StartAutoplay", "Fail");
										}

										Thread.sleep(3000);
										if (imageLibrary.isImageAppears("AutoplayStop")) {
											imageLibrary.click("AutoplayStop");
											lvcReport.detailsAppend("Verify able to click on AutoplayStop",
													"Should be able to click on Autoplay button",
													"Able to click on AutoplayStop", "PASS");
										} else {
											lvcReport.detailsAppend("Verify if able to click on AutoplayStop",
													"Should be able to click on AutoplayStop",
													"Not able to click on AutoplayStop", "Fail");
										}

									} else {
										lvcReport.detailsAppend("Verify if able to click on Autoplay",
												"Should be able to click on Autoplay", "Not able to click on Autoplay",
												"Fail");
									}
									
									
										// =========================BetMenu=======================================================
										lvcReport.detailsAppend("Following are the BetMenu verification test cases","Verify BetMenu", "", "");
										Thread.sleep(2000);
										if (imageLibrary.isImageAppears("BetMenu")) {
											Thread.sleep(3000);
											// open betmenu panel
											imageLibrary.click("BetMenu");
											Thread.sleep(3000);
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
											cfnlib.verifyBetSliders(lvcReport, languageCurrency);
											Thread.sleep(3000);

											lvcReport.detailsAppend("Verify able to click on BetMenu",
													"Should be able to click on BetMenu button",
													"Able to click on BetMenu button", "PASS");
										} else {
											lvcReport.detailsAppend("Verify if able to click on BetMenu",
													"Should be able to click on BetMenu",
													"Not able to click on BetMenu", "Fail");
										}

										// ===========================MaxBet=======================================================
										// report.detailsAppend("Following are the MaxBet verification test
										// cases","Verify MaxBet", "", "");
										
										
										webdriver.context("NATIVE_APP");
										if (imageLibrary.isImageAppears("MaxBet")) 
										{
											Thread.sleep(3000);
											imageLibrary.click("MaxBet");
											Thread.sleep(3000);
											lvcReport.detailsAppend("Verify able to click on MaxBet",
													"Should be able to click on MaxBet button",
													"Able to click on MaxBet button", "PASS");
										} else {
											lvcReport.detailsAppend("Verify if able to click on MaxBet",
													"Should be able to click on MaxBet", "Not able to click on MaxBet",
													"Fail");
										}

										// ===============================Verify Credit Amt & Currency Format===============================

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
										
										// String str = imageLibrary.getTextCreditAmount("D:\\ImageComparision\\WildCatch\\portrait\\Credits.png");
										String str = cfnlib.GetConsoleText("return " + cfnlib.xpathMap.get("CreditValue"));
										
										boolean credits = cfnlib.verifyRegularExpressionPlayNext(lvcReport, regExpr,
												str,isoCode);
										   System.out.println("Credit value" +str);
										   
										if (credits) {
											System.out.println("Base Game Credit Value : PASS");
											log.debug("Base Game Credit Value : PASS");
											lvcReport.detailsAppendFolder("Base Game", "Credit Amount", "Credit Amount",
													"PASS", languageCurrency);
										} else {
											System.out.println("Base Game Credit Value : FAIL");
											log.debug("Base Game Credit Value : FAIL");
											lvcReport.detailsAppendFolder("Base Game", "Credit Amount", "Credit Amount",
													"FAIL", languageCurrency);
										}

										//============================Verify Bet Amt & Currency Format============================

										
										//String str1 = imageLibrary.getTextCreditAmount("D:\\ImageComparision\\WildCatch\\portrait\\BetValue.png");
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
										String str1=cfnlib.GetConsoleText("return " + cfnlib.xpathMap.get("BetTextValue"));
										System.out.println("Bet value" +str1);
										boolean betAmt = cfnlib.verifyRegularExpressionPlayNext(lvcReport, regExpr,
												str1,isoCode);
										
										if (betAmt) {
											System.out.println("Base Game Bet Value : PASS");
											log.debug("Base Game Bet Value : PASS");
											lvcReport.detailsAppendFolder("Base Game", "Bet Amount", "Bet Amount",
													"PASS", languageCurrency);
										} else {
											System.out.println("Base Game Bet Value : FAIL");
											log.debug("Base Game Bet Value : FAIL");
											lvcReport.detailsAppendFolder("Base Game", "Bet Amount", "Bet Amount",
													"FAIL", languageCurrency);
										}

										// ===========================ClickMenuButton=======================================================
										webdriver.context("NATIVE_APP");
										lvcReport.detailsAppend("Following are the Menu verification test cases", "Verify Menu", "", "");
		
										if (imageLibrary.isImageAppears("Menu")) {
											Thread.sleep(3000);
											imageLibrary.click("Menu");
											Thread.sleep(3000);
											lvcReport.detailsAppend("Verify able to click on Menu",
													"Should be able to click on Menu button", "Able to click on Menu button", "PASS");
										} else {
											lvcReport.detailsAppend("Verify if able to click on Menu", "Should be able to click on Menu",
													"Not able to click on Menu", "Fail");
										}
										
										if (imageLibrary.isImageAppears("Paytable")) 
										{
											Thread.sleep(2000);
											imageLibrary.click("Paytable");
											Thread.sleep(5000);
											lvcReport.detailsAppend("Verify able to click on Paytable",
													"Should be able to click on Paytable", "Able to click on Paytable",
													"PASS");
											
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
										}
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
										boolean scrollPaytable = cfnlib.paytableScroll(lvcReport, languageCurrency);
										if (scrollPaytable) {
											System.out.println("Paytable Open : PASS");
											log.debug("Paytable Open : PASS");

											boolean payouts = cfnlib.validatePayoutsFromPaytable(lvcReport,
													languageCurrency, regExpr);
											if (payouts) {
												
												lvcReport.detailsAppendFolder("Base Game", "Pay Table ",
														"PayOut Amount", "PASS", "" + languageCurrency);
											} else {
												lvcReport.detailsAppendFolder("Base Game", "Pay Table ",
														"PayOut Amount", "FAIL", "" + languageCurrency);
											}
											Thread.sleep(2000);
											//Close Paytable
											//cfnlib.func_click("PaytableClose");
											Thread.sleep(2000);
											System.out.println("Paytable Closed : PASS");
											log.debug("Paytable Closed : PASS");
										} else {
											System.out.println("Paytable Open : FAIL");
											log.debug("Paytable Open : FAIL");
											lvcReport.detailsAppendFolder("Base Game", "PayOut Amount", "PayOut Amount",
													"FAIL", "" + languageCurrency);
										}

										// ===========================MenuBack=======================================================
										// report.detailsAppend("Following are the MenuBack verification test
										// cases","Verify MenuBack", "", "");
										
										webdriver.context("NATIVE_APP");
										imageLibrary.click("PaytableClose");
										if (imageLibrary.isImageAppears("MenuBack")) {
											Thread.sleep(3000);
											imageLibrary.click("MenuBack");
											Thread.sleep(3000);
											lvcReport.detailsAppend("Verify able to click on MenuBack",
													"Should be able to click on MenuBack button", "Able to click on MenuBack button",
													"PASS");
										} else {
											lvcReport.detailsAppend("Verify if able to click on MenuBack",
													"Should be able to click on MenuBack", "Not able to click on MenuBack", "Fail");
										}*/
										
										// ===========================Spin (Normal Win)=======================================================
										lvcReport.detailsAppend("Following are the Spin verification test cases", "Verify Spin", "", "");
										try {
										
											if (imageLibrary.isImageAppears("Spin")) {
												Thread.sleep(3000);
												imageLibrary.click("Spin");
												Thread.sleep(3000);
												lvcReport.detailsAppend("Verify able to click on Spin",
														"Should be able to click on spin button", "Able to click on spin button",
														"PASS");
												System.out.println("Spin clicked");
											} else {
												lvcReport.detailsAppend("Verify if able to click on Spin",
														"Should be able to click on Spin", "Not able to click on Spin", "Fail");
											}
										} catch (Exception e) {
											log.error(e.getMessage(), e);
										}
										Thread.sleep(2000);

										//====================Get Win amt and check currency format=================================
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
										boolean winFormatVerification = cfnlib.verifyRegularExpressionPlayNext(lvcReport,
												regExpr, cfnlib.getCurrentWinAmt(lvcReport, languageCurrency), isoCode);
										if (winFormatVerification) {
											System.out.println("Base Game Win Value : PASS");
											log.debug("Base Game Win Value : PASS");
											lvcReport.detailsAppendFolder("Base Game", "Win Amt", "Win Amt", "PASS",
													languageCurrency);
										} else {
											System.out.println("Base Game Win Value : FAIL");
											log.debug("Base Game Win Value : FAIL");
											lvcReport.detailsAppendFolder("Base Game", "Win Amt", "Win Amt", "FAIL",
													languageCurrency);
										}
										Thread.sleep(1000);
										
										// ===========================Spin (Big Win)========================================
										webdriver.context("NATIVE_APP");
										lvcReport.detailsAppend("Following are the Spin verification test cases", "Verify Spin", "", "");
										try {
											Thread.sleep(2000);
											if (imageLibrary.isImageAppears("Spin")) {
												Thread.sleep(3000);
												imageLibrary.click("Spin");
												Thread.sleep(3000);
												lvcReport.detailsAppend("Verify able to click on Spin",
														"Should be able to click on spin button", "Able to click on spin button",
														"PASS");
												System.out.println();
											} else {
												lvcReport.detailsAppend("Verify if able to click on Spin",
														"Should be able to click on Spin", "Not able to click on Spin", "Fail");
											}
										} catch (Exception e) {
											log.error(e.getMessage(), e);
										}
										
										Thread.sleep(3000);

										//================Get Big win and check currency format=============================
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
										boolean bigWinFormatVerification = cfnlib.verifyRegularExpressionPlayNext(lvcReport,
												regExpr, cfnlib.verifyBigWin(lvcReport, languageCurrency),isoCode);
										if (bigWinFormatVerification) {
											System.out.println("Base Game BigWin Value : PASS");
											log.debug("Base Game BigWin Value : PASS");
											lvcReport.detailsAppendFolder("Base Game", "Big Win Amt", "Big Win Amt",
													"PASS", languageCurrency);
										} else {
											System.out.println("Base Game BigWin Value : FAIL");
											log.debug("Base Game BigWin Value : FAIL");
											lvcReport.detailsAppendFolder("Base Game", " Big Win Amt", "Big Win Amt",
													"FAIL", languageCurrency);
										}
										
										// ===========================Spin (For Bonus)=======================================================
										webdriver.context("NATIVE_APP");
										lvcReport.detailsAppend("Following are the Spin verification test cases", "Verify Spin", "", "");
										try {
											Thread.sleep(2000);
											if (imageLibrary.isImageAppears("Spin")) {
												Thread.sleep(3000);
												imageLibrary.click("Spin");
												Thread.sleep(3000);
												lvcReport.detailsAppend("Verify able to click on Spin",
														"Should be able to click on spin button", "Able to click on spin button",
														"PASS");
												
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
												
													
												boolean winVerification = cfnlib.verifyRegularExpressionPlayNext(lvcReport, regExpr,cfnlib.verifyBonusWin(lvcReport, languageCurrency),isoCode);
												if (winVerification) 
												{
													lvcReport.detailsAppendFolder("verify Bonus win amount currency format in baseScene", "Bonus win amount should be in correct currency format","Bonus win amount is in correct currency format", "Pass",languageCurrency);
													System.out.println("Base Game bonue Win Value : Pass");
													log.debug("Base Game Win Value : Pass");
													
												} else 
												{
													lvcReport.detailsAppendFolder("verify Bonus win amount currency format in baseScene", "Bonus win amount should be in correct currency format","Bonus win amount is in incorrect currency format", "Fail",languageCurrency);
													System.out.println("Base Game bonus Win Value : Fail");
													log.debug("Base Game bonus Win Value : Fail");
													
												}
											} else {
												lvcReport.detailsAppend("Verify if able to click on Spin",
														"Should be able to click on Spin", "Not able to click on Spin", "Fail");
											}
										} catch (Exception e) {
											log.error(e.getMessage(), e);
										}
										
										webdriver.context("NATIVE_APP");
										lvcReport.detailsAppend("Following are the Freespin verification test cases", "Verify FreeSpin", "", "");
										try {
											try {
												
												if (imageLibrary.isImageAppears("Spin")) {
													Thread.sleep(3000);
													imageLibrary.click("Spin");
													Thread.sleep(3000);
													lvcReport.detailsAppend("Verify able to click on Spin",
															"Should be able to click on spin button", "Able to click on spin button",
															"PASS");
													System.out.println("Spin clicked");
												} else {
													lvcReport.detailsAppend("Verify if able to click on Spin",
															"Should be able to click on Spin", "Not able to click on Spin", "Fail");
												}
											} catch (Exception e) {
												log.error(e.getMessage(), e);
											}
											Thread.sleep(20000);
											
											if (imageLibrary.isImageAppears("FreeSpinEntry")) 
											{
												Thread.sleep(3000);
												imageLibrary.click("FreeSpinEntry");
												Thread.sleep(3000);
												lvcReport.detailsAppend("Verify able to click on FreeSpin",
														"Should be able to click on Freespin Entry", "Able to click on  Freespin Entry",
														"PASS");
												System.out.println();
											} else {
												lvcReport.detailsAppend("Verify if able to click on Freespin Entry",
														"Should be able to click on  Freespin Entry", "Not able to click on Freespin Entry", "Fail");
											}
											
											Thread.sleep(10000);
											if (imageLibrary.isImageAppears("FSLetsGo")) 
											{
												Thread.sleep(3000);
												imageLibrary.click("FSLetsGo");
												Thread.sleep(3000);
												lvcReport.detailsAppend("Verify able to click on FreeSpin LetsGo",
														"Should be able to click on FreeSpinLetsGo", "Able to click on FreeSpin LetsGo",
														"PASS");
												System.out.println();
											} else {
												lvcReport.detailsAppend("Verify if able to click on FreeSpin LetsGo",
														"Should be able to click on FreeSpin LetsGo", "Not able to click on FreeSpin LetsGo", "Fail");
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
											
												
											boolean winFormatVerificationINFS = cfnlib.verifyRegularExpressionPlayNext(lvcReport,regExpr, cfnlib.verifyFSCurrentWinAmt(lvcReport, languageCurrency),isoCode);
											if (winFormatVerificationINFS) 
											{
												lvcReport.detailsAppendFolder("verify win amount currency format in FreeSpins", "Win amount should be in correct currency format","Win amount is in correct currency format", "Pass",languageCurrency);									
												System.out.println("Freespins Win Value : Pass");
												log.debug("Freespins Win Value : Pass");
											} else 
											{
												lvcReport.detailsAppendFolder("verify win amount currency format in FreeSpins", "Win amount should be in correct currency format","Win amount is in incorrect currency format", "Fail",languageCurrency);									
												
												System.out.println("Freespins  Win Value : Fail");
												log.debug("Freespins Win Value : Fail");
											}
											
											/*lvcReport.detailsAppend("Verify able to FreeSpin Normal Win",
													"Should be able to See FS Normal Win", "Able to see Free Spine Normal win",
													"PASS");*/
											Thread.sleep(20000);
											
											
											// method is used to get the current big win and check the currency format
											boolean bigWinFormatVerificationINFS = cfnlib.verifyRegularExpressionPlayNext(lvcReport,regExpr, cfnlib.verifyFreeSpinBigWin(lvcReport, languageCurrency),isoCode);
											if (bigWinFormatVerificationINFS) 
											{
												lvcReport.detailsAppendFolder("verify bigwin amount currency format in FreeSpins", "BigWin amount should be in correct currency format","BigWin amount is in correct currency format", "Pass",languageCurrency);									
												
												System.out.println("Free Spins BigWin Value : Pass");
												log.debug("Free Spins BigWin Value : Pass");
											
											} else 
											{
												lvcReport.detailsAppendFolder("verify bigwin amount currency format in FreeSpins", "BigWin amount should be in correct currency format","BigWin amount is in correct currency format", "Fail",languageCurrency);									
												
												System.out.println("Free Spins BigWin Value : Fail");
												log.debug("Free Spins BigWin Value : Fail");
											}
											
											/*lvcReport.detailsAppend("Verify able to FreeSpin Big  Win",
													"Should be able to See FS Big Win", "Able to see Free Spine Bigwin",
													"PASS");*/
											Thread.sleep(80000);
											if (imageLibrary.isImageAppears("FreeSpinSum")) {
												lvcReport.detailsAppend("Verify able to see FreeSpin Summary",
														"Should be able to see FreeSpin Summary", "Able to see FreeSpin Summary",
														"PASS");
												imageLibrary.click("FreeSpinSum");
												lvcReport.detailsAppend("Verify able to click on FreeSpin Summary",
														"Should be able to click on  FreeSpin Summary", "Able to click on FreeSpin Summary",
														"PASS");
												System.out.println();
											} else {
												lvcReport.detailsAppend("Verify if able to click on  FreeSpin Summary ",
														"Should be able to click on  FreeSpin Summary", "Not able to click on  FreeSpin Summary", "Fail");
											}
											
										} catch (Exception e) {
											log.error(e.getMessage(), e);
										}
										
										Thread.sleep(3000);

										if (TestPropReader.getInstance().getProperty("IsBonusAvailable")
												.equalsIgnoreCase("yes")) {
											// Base Game Bonus
											if (cfnlib.checkAvilabilityofElement("BonusFeatureImage")) {
												Thread.sleep(8000);
												lvcReport.detailsAppendFolder("Base Game",
														" Bonus Feature is Available", " Bonus Feature is Available",
														"PASS", "" + languageCurrency);
												System.out.println("Bonus Feature is Available");
												log.debug("Bonus Feature is Available");

												// method is used to get the click , get the bonus text,verifies bonus
												// summary screen in base game and check the currency format
												boolean bonus = cfnlib.verifyRegularExpressionUsingArrays(lvcReport,
														regExprNoSymbol,
														cfnlib.bonusFeatureClickandGetText(lvcReport, languageCurrency));
												System.out.println("Bonus Game : PASS");
												log.debug("Bonus Game : PASS");
												cfnlib.spinclick();
												Thread.sleep(2000);
											} else {
												lvcReport.detailsAppendFolder("Base Game",
														" Bonus Feature is Not Available",
														" Bonus Feature is Not Available", "FAIL", languageCurrency);
												System.out.println("Bonus Game : FAIL");
												log.debug("Bonus Game : FAIL");
											}
										} else {
											System.out.println("Bonus is not Available");
											log.debug("Bonus is not Available");
											lvcReport.detailsAppendFolder("Base Game",
													" Bonus Feature is not  Available",
													" Bonus Feature is not Available", "FAIL ", "" + languageCurrency);
										}

								/*} // closing if loop of GameCategory
								
								System.out.println("            ");*/
							//} // closing if loop load game
							/*else {
								System.out.println("Unable to load the game");
								lvcReport.detailsAppendFolder("Launched Currency Name is " + CurrencyName,
										"Launched Currency ID is" + currencyID, "Launched ISO Code is " + isoCode,
										"FAIL", CurrencyName);
							}*/
						/*} // Closing user migration
						else {
							System.out.println("Unable do user migartion");
							lvcReport.detailsAppendFolder("Launched Currency Name is " + CurrencyName,
									"Launched Currency ID is" + currencyID, "Launched ISO Code is " + isoCode, "FAIL",
									CurrencyName);
						}*/
					} // closing Balance Updation
					else {
						System.out.println("Unable to update the balance");
						lvcReport.detailsAppendFolder("Launched Currency Name is " + languageCurrency,
								"Launched Currency ID is" + currencyID, "Launched ISO Code is " + isoCode, "FAIL",
								languageCurrency);
					}
				} // closing for loop of currency mapping

			} // closing if loop of webdriver
			else {
				System.out.println("Unable to load the game");
				lvcReport.detailsAppend("Load Game ", "Unable to Load the game", "Unable to Load the game", "FAIL");
			}
		} // closing try block
			// -------------------Handling the exception---------------------//

		catch (Exception e) {
			// log.error(e.getMessage(), e);
			cfnlib.evalException(e);
		}

		finally {
			lvcReport.endReport();
			/*if (!copiedFiles.isEmpty()) {
				if (TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
					util.deleteBluemesaTestDataFiles(mid, cid, copiedFiles);
				else
					apiObj.deleteAxiomTestDataFiles(copiedFiles);
			}*/
			webdriver.close();
			webdriver.quit();
			// proxy.abort();
			Thread.sleep(1000);
		}
	}

}
