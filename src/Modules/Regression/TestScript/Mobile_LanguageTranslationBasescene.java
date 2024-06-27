package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.ImageLibrary;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 * This script is used to verify language translations
 * 
 *
 * 
 * This Script is for Language verification
 * ===========================================================================
 * 1.1.Base game Credit, total Bet 1.2 AutoPlay 1.3.Max Bet 1.5.HamburgerMenu
 * (Settings, Paytable,Lobby,Banking)
 * 
 * @author SB64689
 *
 */
public class Mobile_LanguageTranslationBasescene {

	Logger log = Logger.getLogger(Mobile_LanguageTranslationBasescene.class.getName());
	public ScriptParameters scriptParameters;

	public void script() throws Exception {

		String mstrTCName = scriptParameters.getMstrTCName();
		String mstrTCDesc = scriptParameters.getMstrTCDesc();
		String mstrModuleName = scriptParameters.getMstrModuleName();
		// AppiumDriver<WebElement> webdriver = (AppiumDriver<WebElement>)
		// scriptParameters.getDriver();
		AppiumDriver<WebElement> webdriver = scriptParameters.getAppiumWebdriver();
		BrowserMobProxyServer proxy = scriptParameters.getProxy();
		String startTime = scriptParameters.getStartTime();
		String filePath = scriptParameters.getFilePath();
		String userName = scriptParameters.getUserName();
		String browserName = scriptParameters.getBrowserName();
		String framework = scriptParameters.getFramework();
		String gameName = scriptParameters.getGameName();
		String DeviceName = scriptParameters.getDeviceName();
		String status = null;
		int mintDetailCount = 0;
		int mintPassed = 0;
		int mintFailed = 0;
		int mintWarnings = 0;
		int mintStepNo = 0;
		int startindex = 0;
		String strGameName = null;
		String urlNew = null;
		int mid = Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int cid = Integer.parseInt(TestPropReader.getInstance().getProperty("CIDMobile"));
		List<String> copiedFiles = new ArrayList<>();

		ExcelDataPoolManager excelpoolmanager = new ExcelDataPoolManager();
		Mobile_HTML_Report report = new Mobile_HTML_Report(webdriver, DeviceName, filePath, startTime, mstrTCName,
				mstrTCDesc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings, mintStepNo, status,
				gameName);

		log.info("Framework" + framework);
		MobileFunctionLibraryFactory mobileFunctionLibraryFactory = new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib = mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, report,
				gameName);

		cfnlib.setOsPlatform(scriptParameters.getOsPlatform());

		try {
			// Step 1
			if (webdriver != null) {
				CommonUtil util = new CommonUtil();
				if (gameName.contains("Mobile")) {
					java.util.regex.Pattern str = java.util.regex.Pattern.compile("Mobile");
					Matcher substing = str.matcher(gameName);
					while (substing.find()) {
						startindex = substing.start();
					}
					strGameName = gameName.substring(0, startindex);
					log.debug("newgamename=" + strGameName);
				} else {
					strGameName = gameName;
				}

				ImageLibrary imageLibrary = new ImageLibrary(webdriver, gameName, "Mobile");

				// List<Map<String, String>> langList = util.readLangTransList();
				List<Map<String, String>> langList = util.readPlayNext_LangTransList();
				for (Map<String, String> LanguageMap : langList) {
					try {

						String languageDescription = LanguageMap.get(Constant.Language).trim();
						String languageCode = LanguageMap.get(Constant.LanguageCode).trim();
						String Bet = LanguageMap.get(Constant.Bet1).trim();
						String TotalBet = LanguageMap.get(Constant.TotalBet1).trim();
						String Banking = LanguageMap.get(Constant.Banking1).trim();
						String Settings = LanguageMap.get(Constant.Settings1).trim();
						String Sounds = LanguageMap.get(Constant.Sounds1).trim();
						String CoinSize = LanguageMap.get(Constant.CoinSize1).trim();
						String BIGWIN = LanguageMap.get(Constant.newbigwin).trim();
						String Banking1 = LanguageMap.get(Constant.Banking1).trim();
						String TOTALWIN = LanguageMap.get(Constant.TOTALWIN).trim();
						String PAYTABLE = LanguageMap.get(Constant.newPay).trim();
						String Credits = LanguageMap.get(Constant.Credits).trim();
						String Spin = LanguageMap.get(Constant.Spin).trim();

						String QuickSpin = LanguageMap.get(Constant.QuickSpin).trim();
						String Autoplay = LanguageMap.get(Constant.Autoplay).trim();
						String QuickBet = LanguageMap.get(Constant.QuickBet).trim();
						String AMAZING = LanguageMap.get(Constant.AMAZING).trim();
						//String CoinsPerLine = LanguageMap.get(Constant.NewCoinsPerLine).trim();
						/*
						 * String Lobby = LanguageMap.get(Constant.Lobby).trim(); String Maxbet=
						 * LanguageMap.get(Constant.Maxbet).trim();
						 */

						// report.detailsAppend("*** LANGUAGE TRANSLATION ***", " Language: " +
						// languageDescription, "","");

						String url = cfnlib.xpathMap.get("ApplicationURL");

						userName = util.randomStringgenerator();
						System.out.println(userName);
						String strFileName = TestPropReader.getInstance().getProperty("SanityTestDataPath");
						File testDataFile = new File(strFileName);

						if (util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles, languageCode)) {

							String launchURl = url.replaceFirst("\\busername=.*?(&|$)", "username=" + userName + "$1");

							if (launchURl.contains("LanguageCode"))
								urlNew = launchURl.replaceAll("LanguageCode=en", "LanguageCode=" + languageCode);
							else if (launchURl.contains("languagecode"))
								urlNew = launchURl.replaceAll("languagecode=en", "languagecode=" + languageCode);
							else if (launchURl.contains("languageCode"))
								urlNew = launchURl.replaceAll("languageCode=en", "languageCode=" + languageCode);

							log.info("url = " + urlNew);
							System.out.println(urlNew);
							System.out.println(languageCode);

							webdriver.navigate().to(urlNew);
							Thread.sleep(10000);

							report.detailsAppendNoScreenshot("Verify language translations in baseScene",
									"Translations should be in " + languageDescription + " correctly", "Pass",
									languageCode);
							try {
								
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
								// ==========================Tap to continue================================================
								if (cfnlib.xpathMap.get("NFD").equalsIgnoreCase("Yes")) {
									if (imageLibrary.isImageAppears("NFDButton")) {
										imageLibrary.click("NFDButton");
										report.detailsAppendFolder("Verify able to click on NFD button",
												"Should be able to click on NFD button", "Clicked on NFD button",
												"Pass", languageCode);
									} else {
										report.detailsAppendFolder("Verify if able to click on continue",
												"Should be able to click on continue button",
												"Not able to click on continue button", "Fail", languageCode);
									}
								}
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
								
								
								/********************************************** Credit *******************************************************/
							 	boolean isCreditCorrect = cfnlib.compareText(Credits, "getCreditsText", "hasHook");
								if (isCreditCorrect) {
									System.out.println("Correct translation for credits text");
									log.debug("Correct translation for credits text");

									report.detailsAppendNoScreenshot("Verify credits text translation",
											"Credits text should display correctly in " + languageDescription,
											"Credits is correct", "Pass");
								} else {
									System.out.println("Incorrect translation for credits text");
									log.debug("Incorrect translation for credits text");
									report.detailsAppendNoScreenshot("Verify credits text translation",
											"Credits text should display correctly in " + languageDescription,
											"Credits is Incorrect", "Fail");

								}

								Thread.sleep(1000);

								/********************************************** Bet *******************************************************/
								boolean isBetCorrect = cfnlib.compareText(Bet, "getBetText", "hasHook");
								if (isBetCorrect) {
									System.out.println("Correct translation for bet text");
									log.debug("Correct translation for bet text");
									report.detailsAppendNoScreenshot("Verify Bet text translation",
											"Bet text should display correctly in " + languageDescription,
											"Bet is correct", "Pass");

								} else {
									System.out.println("Incorrect translation for bet text");
									log.debug("Incorrect translation for bet text");
									report.detailsAppendNoScreenshot("Verify Bet text translation",
											"Bet text should display correctly in " + languageDescription,
											"Bet is Incorrect", "Fail");
								}

							} catch (Exception e) {
								log.error(e.getMessage(), e);
							}

							try {
								
								webdriver.context("NATIVE_APP");
								
								if (cfnlib.isElementVisible("IsBetButtonVisible")) {

									imageLibrary.click("BetButton");

									report.detailsAppendFolder("Verify able to click on Bet button",
											"Should be able to click on Bet button", "Clicked on Bet button", "Pass",
											languageCode);

								}

								Thread.sleep(2000);

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
								
								/************************ total bet *********************************/

								boolean isTotalBetCorrect = cfnlib.compareText(TotalBet, "getTotalBetText", "hasHook");
								if (isTotalBetCorrect) {
									System.out.println("Correct translation for Total bet");
									log.debug("Correct translation for Total bet");
									report.detailsAppendNoScreenshot("Verify Total bet text translation",
											"Total bet text should display correctly in " + languageDescription,
											"Total bet is correct", "Pass");
								} else {
									System.out.println("Incorrect translation for Total bet");
									log.debug("Incorrect translation for Total bet");
									report.detailsAppendNoScreenshot("Verify Total bet text translation",
											"Total bet text should display correctly in " + languageDescription,
											"Total bet is Incorrect", "Fail");
								}
								Thread.sleep(1000);

								/************************ coin size slider *********************************/

								if (cfnlib.xpathMap.get("CoinSizeSliderPresent").equalsIgnoreCase("Yes")) {
									boolean isCoinSizeCorrect = cfnlib.compareText(CoinSize, "getCoinSizeText", "hasHook");
									if (isCoinSizeCorrect) {
										System.out.println("Correct translation for CoinSize");
										log.debug("Correct translation for CoinSize");
										report.detailsAppendNoScreenshot("Verify CoinSize text translation",
												"CoinSize text should display correctly in " + languageDescription,
												"CoinSize is correct", "Pass");
									} else {
										System.out.println("Incorrect translation for CoinSize");
										log.debug("Incorrect translation for CoinSize");
										report.detailsAppendNoScreenshot("Verify CoinSize text translation",
												"CoinSize text should display correctly in " + languageDescription,
												"CoinSize is Incorrect", "Fail");
									}
								}

								Thread.sleep(2000);
								
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

								/************************** coin Per line slider  *********************************/

								/*if (cfnlib.xpathMap.get("CoinsPerLineSliderPresent").equalsIgnoreCase("hasHook")) {
									boolean isCoinsPerLineCorrect = cfnlib.compareText(CoinsPerLine,
											"getCoinsPerLineText", "Yes");
									if (isCoinsPerLineCorrect) {
										System.out.println("Correct translation for Coins Per Line");
										log.debug("Correct translation for Coins Per Line");
										report.detailsAppendNoScreenshot("Verify Coins Per Line text translation",
												"Coins Per Line text should display correctly in "
														+ languageDescription,
												"Coins Per Line is correct", "Pass");
									} else {
										System.out.println("Incorrect translation for Coins Per Line");
										log.debug("Incorrect translation for Coins Per Line");
										report.detailsAppendNoScreenshot("Verify Coins Per Line text translation",
												"Coins Per Line text should display correctly in "
														+ languageDescription,
												"Coins Per Line is Incorrect", "Fail");
									}
								}
*/
								Thread.sleep(2000);

								/************************ Max bet *********************************//*
																									 * 
																									 * boolean
																									 * isMaxbetCorrect =
																									 * cfnlib.
																									 * compareText(
																									 * Maxbet,
																									 * "getMaxBetText",
																									 * "Yes"); if
																									 * (isMaxbetCorrect)
																									 * { System.out.
																									 * println("Correct translation for Max bet"
																									 * ); log.
																									 * debug("Correct translation for Max bet"
																									 * ); report.
																									 * detailsAppendNoScreenshot("Verify Max bet text translation"
																									 * ,
																									 * "Max bet text should display correctly in "
																									 * +
																									 * languageDescription,
																									 * "Max bet is correct"
																									 * , "Pass"); } else
																									 * { System.out.
																									 * println("Incorrect translation for Max bet"
																									 * ); log.
																									 * debug("Incorrect translation for Max bet"
																									 * ); report.
																									 * detailsAppendNoScreenshot("Verify Max bet text translation"
																									 * ,
																									 * "Max bet text should display correctly in "
																									 * +
																									 * languageDescription,
																									 * "Max bet is Incorrect"
																									 * , "Fail"); }
																									 * 
																									 * Thread.sleep(2000
																									 * );
																									 */

								/************************ Quick bet *********************************/

								boolean isquickbetCorrect = cfnlib.compareText(QuickBet, "getquickbetText", "hasHook");
								if (isquickbetCorrect) {
									System.out.println("Correct translation for quick bet Correct");
									log.debug("Correct translation for quick bet Correct");
									report.detailsAppendNoScreenshot("Verify quick bet Correct translation",
											"quick bet Correct text should display correctly in " + languageDescription,
											"quick bet is correct", "Pass");
								} else {
									System.out.println("Incorrect translation for quick bet Correct");
									log.debug("Incorrect translation for quick bet Correct");
									report.detailsAppendNoScreenshot("Verify quick bet Correct text translation",
											"quick bet Correct text should display correctly in " + languageDescription,
											"quick bet is Incorrect", "Fail");
								}

							} catch (Exception e) {
								log.error(e.getMessage(), e);

							}

							try {
								
								webdriver.context("NATIVE_APP");
								
								/************************ Menu button *********************************/

								if (cfnlib.isElementVisible("isMenuBtnVisible")) {

									imageLibrary.click("Menu");
									report.detailsAppendFolder("Verify language translations in Menu panel",
											"Should be able to click on Menu button", "Clicked on Menu button", "Pass",
											languageCode);
									Thread.sleep(2000);
								}

								Thread.sleep(2000);
								
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

								/************************ Settings *********************************/

								boolean isSettingsCorrect = cfnlib.compareText(Settings, "getSettingsText", "hasHook");
								if (isSettingsCorrect) {
									System.out.println("Correct translation for Settinsg text");
									log.debug("Correct translation for Settinsg text");
									report.detailsAppendNoScreenshot("Verify Settings text translation",
											"Settings text should display correctly in " + languageDescription,
											"Settings is correct", "Pass");
								} else {
									System.out.println("Incorrect translation for Settinsg text");
									log.debug("Incorrect translation for Settinsg text");
									report.detailsAppendNoScreenshot("Verify Settings text translation",
											"Settings text should display correctly in " + languageDescription,
											"Settings is Incorrect", "Fail");
								}

								Thread.sleep(2000);

								/************************ Banking *********************************/

								boolean isBankingCorrect = cfnlib.compareText(Banking, "getBankingText", "hasHook");
								if (isBankingCorrect) {
									System.out.println("Correct translation for Banking text");
									log.debug("Correct translation for Banking text");
									report.detailsAppendNoScreenshot("Verify Banking text translation",
											"Banking text should display correctly in " + languageDescription,
											"Banking is correct", "Pass");
								} else {
									System.out.println("Incorrect translation for Banking text");
									log.debug("Incorrect translation for Banking text");
									report.detailsAppendNoScreenshot("Verify Banking text translation",
											"Banking text should display correctly in " + languageDescription,
											"Banking is Incorrect", "Fail");
								}

								Thread.sleep(2000);

								/************************ Paytable *********************************/
								
								if (cfnlib.isElementVisible("isPaytableBtnVisible")) {
									boolean isPaytableCorrect = cfnlib.compareText(PAYTABLE, "getPaytableText", "hasHook");
									if (isPaytableCorrect) {
										System.out.println("Correct translation for Paytable text");
										log.debug("Correct translation for Paytable text");
										report.detailsAppendNoScreenshot("Verify Paytable text translation",
												"Paytable text should display correctly in " + languageDescription,
												"Paytable is correct", "Pass");
									} else {
										System.out.println("Incorrect translation for Paytable text");
										log.debug("Incorrect translation for Paytable text");
										report.detailsAppendNoScreenshot("Verify Paytable text translation",
												"Paytable text should display correctly in " + languageDescription,
												"Paytable is Incorrect", "Fail");
									}
									
									webdriver.context("NATIVE_APP");
									
									imageLibrary.click("Paytable");
									report.detailsAppendFolder("Verify able to click on Paytable button",
											"Should be able to click on Paytable button", "Clicked on Paytable", "Pass",
											languageCode);

									Thread.sleep(2000);

									imageLibrary.click("PaytableBack");
									Thread.sleep(2000);
									report.detailsAppendFolder("Verify able to click on Paytable back button",
											"Should be able to click on Paytable back button",
											"clicked on Paytable Back button", "Pass", languageCode);

								}

								/************************ Lobby *********************************//*
																									 * 
																									 * 
																									 * boolean
																									 * isLobbyCorrect =
																									 * cfnlib.
																									 * compareText(
																									 * Lobby,
																									 * "getLobbyText",
																									 * "Yes"); if
																									 * (isLobbyCorrect)
																									 * { System.out.
																									 * println("Correct translation for Lobby text"
																									 * ); log.
																									 * debug("Correct translation for Lobby text"
																									 * ); report.
																									 * detailsAppendNoScreenshot("Verify Lobby text translation"
																									 * ,
																									 * "Lobby text should display correctly in "
																									 * +
																									 * languageDescription,
																									 * "Lobby is correct"
																									 * , "Pass"); } else
																									 * { System.out.
																									 * println("Incorrect translation for Lobby text"
																									 * ); log.
																									 * debug("Incorrect translation for Lobby text"
																									 * ); report.
																									 * detailsAppendNoScreenshot("Verify Lobby text translation"
																									 * ,
																									 * "Lobby text should display correctly in "
																									 * +
																									 * languageDescription,
																									 * "Lobby is Incorrect"
																									 * , "Fail"); }
																									 */

							} catch (Exception e) {
								log.error(e.getMessage(), e);

							}

							Thread.sleep(2000);
							
							webdriver.context("NATIVE_APP");

							try {
								if (cfnlib.isElementVisible("isSettingsBtnVisible")) {

									imageLibrary.click("Settings");
									report.detailsAppendFolder("Verify language translations in Settings panel",
											"Should be able to click on Settings button", "Clicked on Settings button",
											"Pass", languageCode);
								}

								Thread.sleep(2000);
								
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

								/************************ Sounds *********************************/

								boolean isSoundCorrect = cfnlib.compareText(Sounds, "getSoundsText", "hasHook");
								if (isSoundCorrect) {
									System.out.println("Correct translation for Sounds text");
									log.debug("Correct translation for Sounds text");
									report.detailsAppendNoScreenshot("Verify Sounds text translation",
											"Sounds text should display correctly in " + languageDescription,
											"Sounds is correct", "Pass");
								} else {
									System.out.println("Incorrect translation for Sounds text");
									log.debug("Incorrect translation for Sounds text");
									report.detailsAppendNoScreenshot("Verify Sounds text translation",
											"Sounds text should display correctly in " + languageDescription,
											"Sounds is Incorrect", "Fail");
								}

								/************************* quick spin on setting *********************************/

								boolean isQuickSpinCorrect = cfnlib.compareText(QuickSpin, "getQuickSpinText", "hasHook");
								if (isQuickSpinCorrect) {
									System.out.println("Correct translation for QuickSpin text");
									log.debug("Correct translation for QuickSpin text");
									report.detailsAppendNoScreenshot("Verify QuickSpin text translation",
											"QuickSpin text should display correctly in " + languageDescription,
											"QuickSpin is correct", "Pass");
								} else {
									System.out.println("Incorrect translation for QuickSpin text");
									log.debug("Incorrect translation for QuickSpin text");
									report.detailsAppendNoScreenshot("Verify QuickSpin text translation",
											"QuickSpin text should display correctly in " + languageDescription,
											"QuickSpin is Incorrect", "Fail");
								}

							} catch (Exception e) {
								log.error(e.getMessage(), e);
							}

							webdriver.context("NATIVE_APP");
							
							// try {
							/************************ Autoplay Page *********************************/

							if (cfnlib.isElementVisible("isAutoplayMenuBtnVisible")) {

								imageLibrary.click("Autoplay");
								report.detailsAppendFolder("Verify able to click on Autoplay button",
										"Should be able to click on Autoplay button", "Clicked on Autoplay button",
										"Pass", languageCode);

							
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
								
								
								/************************ autoplay Spin Text *********************************/

								boolean isSpinAutoPlayCorrect = cfnlib.compareText(Spin, "getAutoplaySpinText", "hasHook");
								if (isSpinAutoPlayCorrect) {
									System.out.println("Correct translation for AutoplaySpin text");
									log.debug("Correct translation for AutoplaySpin text");
									report.detailsAppendNoScreenshot("Verify AutoplaySpin text translation",
											"AutoplaySpin text should display correctly in " + languageDescription,
											"AutoplaySpin is correct", "Pass");
								} else {
									System.out.println("Incorrect translation for AutoplaySpin text");
									log.debug("Incorrect translation for AutoplaySpin text");
									report.detailsAppendNoScreenshot("Verify AutoplaySpin text translation",
											"AutoplaySpin text should display correctly in " + languageDescription,
											"AutoplaySpin is Incorrect", "Fail");
								}

								/************************ Autoplay total bet *********************************/

								boolean istotalBetAutoplayCorrect = cfnlib.compareText(TotalBet,	
										"gettotalBetAutoplayText", "hasHook");
								if (istotalBetAutoplayCorrect) {
									System.out.println("Correct translation for total Bet Autoplay text");
									log.debug("Correct translation for total Bet Autoplay text");
									report.detailsAppendNoScreenshot("Verify total Bet Autoplay text translation",
											"total Bet Autoplay text should display correctly in "
													+ languageDescription,
											"total Bet Autoplay is correct", "Pass");
								} else {
									System.out.println("Incorrect translation for total Bet Autoplay text");
									log.debug("Incorrect translation for total Bet Autoplay text");
									report.detailsAppendNoScreenshot("Verify total Bet Autoplay text translation",
											"total Bet Autoplay text should display correctly in "
													+ languageDescription,
											"total Bet Autoplay is Incorrect", "Fail");
								}

								/************************ Autoplay text *********************************/

								boolean isAutoplayCorrect = cfnlib.compareText(Autoplay, "getAutoplayText", "hasHook");
								if (isAutoplayCorrect) {
									System.out.println("Correct translation for Autoplay text");
									log.debug("Correct translation for Autoplay text");
									report.detailsAppendNoScreenshot("Verify Autoplay text translation",
											"Autoplay text should display correctly in " + languageDescription,
											"Autoplay is correct", "Pass");
								} else {
									System.out.println("Incorrect translation for Autoplay text");
									log.debug("Incorrect translation for Autoplay text");
									report.detailsAppendNoScreenshot("Verify Autoplay text translation",
											"Autoplay text should display correctly in " + languageDescription,
											"Autoplay is Incorrect", "Fail");
								}

								/************************ Start Autoplay *********************************/

								/*
								 * boolean isStartAutoplayCorrect = cfnlib.compareText(StartAutoplay,
								 * "getStartAutoplayText", "Yes"); if (isStartAutoplayCorrect) {
								 * System.out.println("Correct translation for Start Autoplay text");
								 * log.debug("Correct translation for Start Autoplay text");
								 * report.detailsAppendNoScreenshot("Verify Start Autoplay text translation",
								 * "Start Autoplay text should display correctly in " + languageDescription,
								 * "Start Autoplay is correct", "Pass"); } else {
								 * System.out.println("Incorrect translation for Start Autoplay text");
								 * log.debug("Incorrect translation for Start Autoplay text");
								 * report.detailsAppendNoScreenshot("Verify Start Autoplay text translation",
								 * "Start Autoplay text should display correctly in " + languageDescription,
								 * "Start Autoplay is Incorrect", "Fail"); }
								 */
								
								webdriver.context("NATIVE_APP");
								
								imageLibrary.click("MenuClose");

								/************************
								 * Click on spin(Normal win)
								 *********************************/
								if (cfnlib.isElementVisible("isSpinBtnVisible")) {

									imageLibrary.click("Spin");
									Thread.sleep(2000);
									report.detailsAppendFolder("Verify able to click on Spin button",
											"Should be able to click on Spin button", "Clicked on Spin button", "Pass",
											languageCode);
								}
								Thread.sleep(2500);

								/************************
								 * Click on spin(Bigwin win)
								 *********************************/
								if (cfnlib.isElementVisible("isSpinBtnVisible")) {

									imageLibrary.click("Spin");
									Thread.sleep(2000);
									report.detailsAppendFolder("Verify able to click on Spin button",
											"Should be able to click on Spin button", "Clicked on Spin button", "Pass",
											languageCode);
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
								
								Thread.sleep(2500);
								boolean isBigwinCorrect = cfnlib.compareText(BIGWIN, "getBigwinText", "hasHook");
								if (isAutoplayCorrect) {
									System.out.println("Correct translation for Bigwin text");
									log.debug("Correct translation for Bigwin text");
									report.detailsAppendNoScreenshot("Verify Bigwin text translation",
											"Bigwin text should display correctly in " + languageDescription,
											"Bigwin is correct", "Pass");
								} else {
									System.out.println("Incorrect translation for Bigwin text");
									log.debug("Incorrect translation for Bigwin text");
									report.detailsAppendNoScreenshot("Verify Bigwin text translation",
											"Bigwin text should display correctly in " + languageDescription,
											"Bigwin is Incorrect", "Fail");
								}

								Thread.sleep(2000);
								/************************ Click on spin(Bonus win) *********************************/
								webdriver.context("NATIVE_APP");
								
								if (cfnlib.isElementVisible("isSpinBtnVisible")) {

									imageLibrary.click("Spin");
									Thread.sleep(2000);
									report.detailsAppendFolder("Verify able to click on Spin button",
											"Should be able to click on Spin button", "Clicked on Spin button", "Pass",
											languageCode);
								}

								Thread.sleep(2500);
								
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
								
								boolean isAmazingCorrect = cfnlib.compareText(AMAZING, "getBigwinText", "hasHook");
								if (isAutoplayCorrect) {
									System.out.println("Correct translation for AMAZING text");
									log.debug("Correct translation for AMAZING text");
									report.detailsAppendNoScreenshot("Verify AMAZING text translation",
											"AMAZING text should display correctly in " + languageDescription,
											"AMAZING is correct", "Pass");
								} else {
									System.out.println("Incorrect translation for AMAZING text");
									log.debug("Incorrect translation for AMAZING text");
									report.detailsAppendNoScreenshot("Verify AMAZING text translation",
											"AMAZING text should display correctly in " + languageDescription,
											"AMAZING is Incorrect", "Fail");
								}

							}
						} else {
							System.out.println("Unable to Copy testdata");
							log.debug("Unable to Copy testdata");
							report.detailsAppendFolder("unable to copy test data to server ", " ", "", "Fail",
									languageCode);
						}

					} catch (Exception e) {
						log.error(e.getMessage(), e);
						cfnlib.evalException(e);
					}

				}
			}
			// -------------------Handling the exception---------------------//
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			cfnlib.evalException(e);
		}
		// -------------------Closing the connections---------------//
		finally {
			report.endReport();
			webdriver.close();
			webdriver.quit();
			// proxy.abort();
			Thread.sleep(1000);
		}
	}
}
