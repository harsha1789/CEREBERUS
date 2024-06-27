package Modules.Regression.TestScript;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.ImageLibrary;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 * This script is used to verify language translations
 * 
 *  
 *         This Script is for Language verification
 *         ===========================================================================
 *         1.1.Base game Credit, total Bet 1.2 AutoPlay 1.3.Max Bet 1.5.HamburgerMenu (Settings,
 *         Paytable,Lobby,Banking)
 *         
 * 
 *  * TestData 
 * ==============
 * 1.Base Game
 * 1.1 Normal Win
 * 1.2 Big Win
 * 1.3 Bonus(If Applicable)

 * 
 * @author SB64689
 *
 */
public class Desktop_LanguageTranslationBasescene
 {
	Logger log = Logger.getLogger(Desktop_LanguageTranslationBasescene.class.getName());
	public ScriptParameters scriptParameters;
	private String languageCurrency;

	public void script() throws Exception {

		String mstrTCName = scriptParameters.getMstrTCName();
		String mstrTCDesc = scriptParameters.getMstrTCDesc();
		String mstrModuleName = scriptParameters.getMstrModuleName();
		WebDriver webdriver = scriptParameters.getDriver();
		BrowserMobProxyServer proxy = scriptParameters.getProxy();
		String startTime = scriptParameters.getStartTime();
		String filePath = scriptParameters.getFilePath();
		String userName = scriptParameters.getUserName();
		String browserName = scriptParameters.getBrowserName();
		String framework = scriptParameters.getFramework();
		String gameName = scriptParameters.getGameName();
		String status = null;
		int mintDetailCount = 0;
		int mintPassed = 0;
		int mintFailed = 0;
		int mintWarnings = 0;
		int mintStepNo = 0;
		int startindex = 0;
		String strGameName = null;
		String urlNew = null;

		ExcelDataPoolManager excelpoolmanager = new ExcelDataPoolManager();
		Desktop_HTML_Report report = new Desktop_HTML_Report(webdriver, browserName, filePath, startTime, mstrTCName,
				mstrTCDesc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings, mintStepNo, status,
				gameName);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory = new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib = desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,
				report, gameName);
		Wait Wait = new WebDriverWait(webdriver, 3000);
		CommonUtil util = new CommonUtil();
		try {
			// Step 1
			if (webdriver != null) {

				if (gameName.contains("Desktop")) {
					java.util.regex.Pattern str = java.util.regex.Pattern.compile("Desktop");
					Matcher substing = str.matcher(gameName);
					while (substing.find()) {
						startindex = substing.start();
					}
					strGameName = gameName.substring(0, startindex);
					log.debug("newgamename=" + strGameName);
				} else {
					strGameName = gameName;
				}

				ImageLibrary imageLibrary = new ImageLibrary(webdriver, strGameName, "Desktop");

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
						String AMAZING= LanguageMap.get(Constant.AMAZING).trim();
					/*	String CoinsPerLine = LanguageMap.get(Constant.BaseNewCoinsPerLine).trim();
						String Lobby = LanguageMap.get(Constant.Lobby).trim();
						String Maxbet= LanguageMap.get(Constant.BaseMaxbet).trim();
					*/	//String Congratulations= LanguageMap.get(Constant.FGCongratulations).trim();
						
						//report.detailsAppend("*** LANGUAGE TRANSLATION  ***", " Language: " + languageDescription, "","");

						String url = cfnlib.XpathMap.get("ApplicationURL");

						userName = util.randomStringgenerator();
						System.out.println(userName);
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

						report.detailsAppend("*** CURRENCY AND LANGUAGE ***", "LanguageCode: "+languageCode+", Language: "+languageDescription,"", "");
							
						//report.detailsAppendNoScreenshot("Verify language translations in baseScene","Translations should be in " + languageDescription + " correctly", "Pass",languageCode);
						try {

							/********************* click on NFD ****************************/
							imageLibrary.click("NFDButton");
							Thread.sleep(5000);
							
							report.detailsAppendFolder("Verify able to click on NFD button",
									"Should be able to click on NFD button", "Clicked on NFD button", "Pass",
									languageCode);
							
							Thread.sleep(1000);
							
							report.detailsAppend("Following is the Credit verification test case","Verify Credit translation", "", "");	
						
							/************************ Credit *********************************/
							boolean isCreditCorrect = cfnlib.compareText(Credits, "getCreditsText", "yes");
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
							
							report.detailsAppend("Following is the Bet verification test case","Verify Bet translation", "", "");
							/************************ Bet *********************************/
							boolean isBetCorrect = cfnlib.compareText(Bet, "getBetText", "yes");
							if (isBetCorrect) {
								System.out.println("Correct translation for bet text");
								log.debug("Correct translation for bet text");
								report.detailsAppendNoScreenshot("Verify Bet text translation",
										"Bet text should display correctly in " + languageDescription, "Bet is correct",
										"Pass");

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
							//if (imageLibrary.isImageAppears("BetButton")) {
							if (cfnlib.isElementVisible("IsBetButtonVisible")) {
								
								imageLibrary.click("BetButton");
 								Thread.sleep(35000);
								report.detailsAppendFolder("Verify able to click on Bet button",
										"Should be able to click on Bet button", "Clicked on Bet button", "Pass",
										languageCode);

							}

							Thread.sleep(2000);

							/************************ total bet *********************************/
							//report.detailsAppend("Following is the total bet verification test case","Verify total bet translation", "", "");	
							boolean isTotalBetCorrect = cfnlib.compareText(TotalBet, "getTotalBetText", "yes");
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
							//report.detailsAppend("Following is the CoinSize verification test case","Verify total bet translation", "", "");	
							if (cfnlib.XpathMap.get("CoinSizeSliderPresent").equalsIgnoreCase("Yes")) {
								boolean isCoinSizeCorrect = cfnlib.compareText(CoinSize, "getCoinSizeText", "yes");
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

							/************************
							 * coin Per line slider
							 *********************************/

							
							  /*if(cfnlib.XpathMap.get("CoinsPerLineSliderPresent").equalsIgnoreCase("Yes"))
							  { 
								  boolean isCoinsPerLineCorrect = cfnlib.compareText(CoinsPerLine,"getCoinsPerLineText","yes"); if (isCoinsPerLineCorrect) {
							  System.out.println("Correct translation for Coins Per Line");
							  log.debug("Correct translation for Coins Per Line");
							  report.detailsAppendNoScreenshot("Verify Coins Per Line text translation",
							  "Coins Per Line text should display correctly in " + languageDescription,
							  "Coins Per Line is correct", "Pass"); } else {
							 System.out.println("Incorrect translation for Coins Per Line");
							  log.debug("Incorrect translation for Coins Per Line");
							  report.detailsAppendNoScreenshot("Verify Coins Per Line text translation",
							  "Coins Per Line text should display correctly in " + languageDescription,
							  "Coins Per Line is Incorrect", "Fail"); } }
							  
							  Thread.sleep(2000);
							 

							*//************************ Max bet *********************************//*

							
							  boolean isMaxbetCorrect = cfnlib.compareText(Maxbet, "getMaxBetText","yes");
							  if (isMaxbetCorrect) { System.out.println("Correct translation for Max bet");
							  log.debug("Correct translation for Max bet");
							  report.detailsAppendNoScreenshot("Verify Max bet text translation",
							  "Max bet text should display correctly in " + languageDescription,
							  "Max bet is correct", "Pass"); } else {
							  System.out.println("Incorrect translation for Max bet");
							  log.debug("Incorrect translation for Max bet");
							  report.detailsAppendNoScreenshot("Verify Max bet text translation",
							  "Max bet text should display correctly in " + languageDescription,
							  "Max bet is Incorrect", "Fail"); }
							  */
							  Thread.sleep(2000);
							 

							/************************ Quick bet *********************************/

							boolean isquickbetCorrect = cfnlib.compareText(QuickBet, "isquickbetCorrect", "yes");
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
							report.detailsAppend("Following is the Menu panel verification test case","Verify Menu Panel translation", "", "");
							/************************ Menu button *********************************/

							if (cfnlib.isElementVisible("isMenuBtnVisible")) {

								imageLibrary.click("Menu");
								Thread.sleep(35000);
								report.detailsAppendFolder("Verify language translations in Menu panel",
										"Should be able to click on Menu button", "Clicked on Menu button", "Pass",
										languageCode);
								
							}

							Thread.sleep(2000);

							/************************ Settings *********************************/

							boolean isSettingsCorrect = cfnlib.compareText(Settings, "getSettingsText", "yes");
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

							boolean isBankingCorrect = cfnlib.compareText(Banking, "getBankingText", "yes");
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
								boolean isPaytableCorrect = cfnlib.compareText(PAYTABLE, "getPaytableText", "yes");
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

								imageLibrary.click("Paytable");
								Thread.sleep(35000);
								report.detailsAppendFolder("Verify able to click on Paytable button",
										"Should be able to click on Paytable button", "Clicked on Paytable", "Pass",
										languageCode);

								Thread.sleep(2000);

								imageLibrary.click("PaytableBack");
								
								Thread.sleep(35000);
								report.detailsAppendFolder("Verify able to click on Paytable back button",
										"Should be able to click on Paytable back button",
										"clicked on Paytable Back button", "Pass", languageCode);

							}

							/************************ Lobby *********************************/
							
							 /* boolean isLobbyCorrect = cfnlib.compareText(Lobby, "getLobbyText","yes"); if
							  (isLobbyCorrect) { System.out.println("Correct translation for Lobby text");
							  log.debug("Correct translation for Lobby text");
							  report.detailsAppendNoScreenshot("Verify Lobby text translation",
							  "Lobby text should display correctly in " + languageDescription,
							  "Lobby is correct", "Pass"); } else {
							  System.out.println("Incorrect translation for Lobby text");
							  log.debug("Incorrect translation for Lobby text");
							  report.detailsAppendNoScreenshot("Verify Lobby text translation",
							  "Lobby text should display correctly in " + languageDescription,
							  "Lobby is Incorrect", "Fail"); }*/
							 

						} catch (Exception e) {
							log.error(e.getMessage(), e);

						}

						Thread.sleep(35000);

						try {
							//if (cfnlib.isElementVisible("isSettingsBtnVisible")) {
							imageLibrary.click("Menu");
							Thread.sleep(25000);
							imageLibrary.click("Settings");
							report.detailsAppend("Following is the Settings verification test case","Verify Settings translation", "", "");
							Thread.sleep(35000);
							report.detailsAppendFolder("Verify language translations in Settings panel",
										"Should be able to click on Settings button", "Clicked on Settings button",
										"Pass", languageCode);

									//}

							//Thread.sleep(2000);

							/************************ Sounds *********************************/

							boolean isSoundCorrect = cfnlib.compareText(Sounds, "getSoundsText", "yes");
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

							/************************
							 * quick spin on setting
							 *********************************/

							boolean isQuickSpinCorrect = cfnlib.compareText(QuickSpin, "getQuickSpinText", "yes");
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

						// try {
						/************************ Autoplay Page *********************************/
						
						report.detailsAppend("Following is the Autoplay verification test case","Verify Autoplay translation", "", "");
						if (cfnlib.isElementVisible("isAutoplayMenuBtnVisible")) {
							
							imageLibrary.click("Autoplay");
							Thread.sleep(35000);
							report.detailsAppendFolder("Verify able to click on Autoplay button",
									"Should be able to click on Autoplay button", "Clicked on Autoplay button", "Pass",
									languageCode);

							/************************ autoplay Spin Text *********************************/

							boolean isSpinAutoPlayCorrect = cfnlib.compareText(Spin, "getAutoplaySpinText", "yes");
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

							boolean istotalBetAutoplayCorrect = cfnlib.compareText(TotalBet, "gettotalBetAutoplayText","yes");
							if (istotalBetAutoplayCorrect) {
								System.out.println("Correct translation for total Bet Autoplay text");
								log.debug("Correct translation for total Bet Autoplay text");
								report.detailsAppendNoScreenshot("Verify total Bet Autoplay text translation",
										"total Bet Autoplay text should display correctly in " + languageDescription,
										"total Bet Autoplay is correct", "Pass");
							} else {
								System.out.println("Incorrect translation for total Bet Autoplay text");
								log.debug("Incorrect translation for total Bet Autoplay text");
								report.detailsAppendNoScreenshot("Verify total Bet Autoplay text translation",
										"total Bet Autoplay text should display correctly in " + languageDescription,
										"total Bet Autoplay is Incorrect", "Fail");
							}

							/************************ Autoplay text *********************************/

							boolean isAutoplayCorrect = cfnlib.compareText(Autoplay, "getAutoplayText", "yes");
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

							/*boolean isStartAutoplayCorrect = cfnlib.compareText(StartAutoplay, "getStartAutoplayText",
									"yes");
							if (isStartAutoplayCorrect) {
								System.out.println("Correct translation for Start Autoplay text");
								log.debug("Correct translation for Start Autoplay text");
								report.detailsAppendNoScreenshot("Verify Start Autoplay text translation",
										"Start Autoplay text should display correctly in " + languageDescription,
										"Start Autoplay is correct", "Pass");
							} else {
								System.out.println("Incorrect translation for Start Autoplay text");
								log.debug("Incorrect translation for Start Autoplay text");
								report.detailsAppendNoScreenshot("Verify Start Autoplay text translation",
										"Start Autoplay text should display correctly in " + languageDescription,
										"Start Autoplay is Incorrect", "Fail");
							}
*/
							imageLibrary.click("MenuClose");

							/************************ Click on spin(Normal win) *********************************/
							
							report.detailsAppend("Following is the Winning verification test case","Verify Winning translation", "", "");
							
							if (cfnlib.isElementVisible("isSpinBtnVisible")) {
								
								imageLibrary.click("Spin");
								Thread.sleep(2000);
								report.detailsAppendFolder("Verify able to click on Spin button",
										"Should be able to click on Spin button", "Clicked on Spin button", "Pass",
										languageCode);
								}
							Thread.sleep(2500);
							
							
							/************************ Click on spin(Bigwin win) *********************************/
							if (cfnlib.isElementVisible("isSpinBtnVisible")) {
								
								imageLibrary.click("Spin");
								Thread.sleep(2000);
								report.detailsAppendFolder("Verify able to click on Spin button",
										"Should be able to click on Spin button", "Clicked on Spin button", "Pass",
										languageCode);
								}
							
							
							Thread.sleep(2500);
							boolean isBigwinCorrect = cfnlib.compareText(BIGWIN, "getBigwinText", "yes");
							if (isBigwinCorrect) {
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
							
							if (cfnlib.isElementVisible("isSpinBtnVisible")) {
								
								imageLibrary.click("Spin");
								Thread.sleep(2000);
								report.detailsAppendFolder("Verify able to click on Spin button",
										"Should be able to click on Spin button", "Clicked on Spin button", "Pass",
										languageCode);
								}
							
							Thread.sleep(2500);
							boolean isAmazingCorrect = cfnlib.compareText(AMAZING, "getBigwinText", "yes");
							if (isAmazingCorrect) {
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
						
						/****************Free Spins****************************************/
						
					/*	report.detailsAppend("Following are the Freespin test cases", "Verify Freespin scene", "", "");	
						
						if (TestPropReader.getInstance().getProperty("IsFreeSpinAvailable").equalsIgnoreCase("yes")) 
						{
							// click on spin button
							imageLibrary.click("Spin");
							Thread.sleep(20000);

							if(cfnlib.XpathMap.get("tapToContinueFreespinsAvailable").equalsIgnoreCase("Yes"))
							{
								//cfnlib.waitForElement("tapToContinueFreespins");
								imageLibrary.click("tapToContinueFreespins");
							}
														
							Thread.sleep(10000);							
							// Free spins  1St Spins to get correct Win text
											
							
							Thread.sleep(15000);
							// method is used to get the current Amazing win and check the currency format
							  String bonus =cfnlib.func_GetText("FSBonusWin");
                              System.out.println("Free Spins bonus win value : PASS" +bonus);
							
							
							String cred =cfnlib.func_GetText("FSBalanceText");
							System.out.println("Credit Value : "+cred);
								// method is used to get the current credit and check the currency format
								
							
							// method is used to get the current total win and check the currency format
							String totalFs =cfnlib.func_GetText("totalWinInFS");
                            System.out.println("Free Spins Total win value : PASS" +totalFs);
							
							// method is used to verify free spins summary screen and check the currency
							// format
							Thread.sleep(30000);							
							if (cfnlib.waitForElement("FSSummaryScreen")) 
							{
								Thread.sleep(8000);
								
							} // Closing play next Summary Screen
										
							 
							if (cfnlib.waitForElement("FSSummaryScreen")) {
								Thread.sleep(2000);
								// click on spin button
								imageLibrary.click("FSSummaryTap");
								Thread.sleep(9000);
								if (cfnlib.isElementVisible("isSpinBtnVisible")) {
								
									report.detailsAppendFolder("Verify FS summary screen click", "FS summary screen click"," FS summary screen clicked", "PASS",languageCurrency);
								} else {
									System.out.println("Free Spins Summary Screen : FAIL");
									log.debug("Free Spins Summary Screen : FAIL");
									report.detailsAppendFolder("Verify FS summary screen click", "FS summary screen click"," FS summary screen not clicked","FAIL",languageCurrency);
								}
							} // Closing Summary Screen
						}*/
						
						
						
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
