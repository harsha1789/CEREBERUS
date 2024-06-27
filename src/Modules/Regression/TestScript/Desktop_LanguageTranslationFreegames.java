/*package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.zensar.automation.api.RestAPILibrary;
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

*//**
 * This script is used to verify language translations
 * 
 * 
 *
 *         This Script is for Language verification
 *         ===========================================================================
 *         1.1.Free game page(PlayNow, PlayLater, Discard, Keep It)
 *         1.2 Credit, total Bet 
 *         1.3 AutoPlay 
 *         1.4 Max Bet 
 *         1.5 HamburgerMenu (Settings,
 *         Paytable,Lobby,Banking)
 * 
 * 
 * * TestData ============== 1.Base Game 1.1 Normal Win 1.2 Big Win 1.3 Bonus(If Applicable)
 * 
 * 
 * @author SB64689
 *
 *//*
public class Desktop_LanguageTranslationFreegames {

	Logger log = Logger.getLogger(Desktop_LanguageTranslationFreegames.class.getName());
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
		RestAPILibrary apiObj = new RestAPILibrary();
		List<String> copiedFiles = new ArrayList<>();
		int mid = Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int cid = Integer.parseInt(TestPropReader.getInstance().getProperty("CIDDesktop"));

		try {
			// Step 1
			if (webdriver != null) {
				String strFileName = TestPropReader.getInstance().getProperty("FreeGamesTestDataPath");
				File testDataFile = new File(strFileName);
				String testDataExcelPath = TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				int languageCnt = excelpoolmanager.rowCount(testDataExcelPath, Constant.LANG_XL_SHEET_NAME);
				languageCnt = 1;
				String offerExpirationUtcDate = util.getCurrentTimeWithDateAndTime();
				String strdefaultNoOfFreeGames = cfnlib.XpathMap.get("DefaultNoOfFreeGames");
				System.out.println("Number of Free Games Assigned : " + strdefaultNoOfFreeGames);
				log.debug("Number of Free Games Assigned : " + strdefaultNoOfFreeGames);
				int defaultNoOfFreeGames = (int) Double.parseDouble(strdefaultNoOfFreeGames);
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
						boolean isFreeGameAssigned = false;
						// verifying free games
						int maxFGRetCount = 5;int count = 0;
							
						userName = "swati120";
						//userName = util.randomStringgenerator();
						System.out.println("Username:" + userName);log.debug("Username:" + userName);
						
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
						String CoinsPerLine = LanguageMap.get(Constant.BaseNewCoinsPerLine).trim();
						String Lobby = LanguageMap.get(Constant.Lobby).trim();
						String Maxbet= LanguageMap.get(Constant.BaseMaxbet).trim();
						String PlayNow = LanguageMap.get(Constant.PlayNow).trim();
						String PlayLater = LanguageMap.get(Constant.PlayLater).trim();
						String KeepIt = LanguageMap.get(Constant.KeepIt).trim();
						String DiscardOffer = LanguageMap.get(Constant.DiscardOffer).trim();
						String Discard = LanguageMap.get(Constant.Discard).trim();
						String Freegames = LanguageMap.get(Constant.Freegames).trim();
						String Congratulations = LanguageMap.get(Constant.FGCongratulations).trim();
	
						String url = cfnlib.XpathMap.get("ApplicationURL");
						
						isFreeGameAssigned = cfnlib.assignFreeGames(userName, offerExpirationUtcDate, mid, cid, languageCnt, defaultNoOfFreeGames);
						System.out.println("Free Games assignement status is : " + isFreeGameAssigned);log.debug("Free Games assignement status is : " + isFreeGameAssigned);
						Thread.sleep(30000);
						
						
						//comments after
						 //isFreeGameAssigned =true;
						
						if(gameName.contains("Desktop"))
						{   
							java.util.regex.Pattern  str=java.util.regex.Pattern.compile("Desktop");
							Matcher  substing=str.matcher(gameName);
							while(substing.find())
							{
								startindex=substing.start();													
							}
							strGameName=gameName.substring(0, startindex);
							log.debug("newgamename="+strGameName);
						}
						else
						{
							strGameName=gameName;
						}
						
						isFreeGameAssigned = cfnlib.assignFreeGames(userName, offerExpirationUtcDate, mid, cid,languageCnt, defaultNoOfFreeGames);
						System.out.println("Free Games assignement status is : " + isFreeGameAssigned);log.debug("Free Games assignement status is : " + isFreeGameAssigned);
						Thread.sleep(5000);

						                isFreeGameAssigned =true;
										if (isFreeGameAssigned) 
										{		                
											String launchURl = url.replaceFirst("\\busername=.*?(&|$)", "username=" + userName + "$1");

											if (launchURl.contains("LanguageCode"))
												urlNew = launchURl.replaceAll("LanguageCode=en", "LanguageCode=" + languageCode);
											else if (launchURl.contains("languagecode"))
												urlNew = launchURl.replaceAll("languagecode=en", "languagecode=" + languageCode);
											else if (launchURl.contains("languageCode"))
												urlNew = launchURl.replaceAll("languageCode=en", "languageCode=" + languageCode);				
											if("Yes".equalsIgnoreCase(cfnlib.XpathMap.get("Clock")))
											{										
												cfnlib.loadGame(launchURl);
												
												Thread.sleep(10000);
											}
											else
											{
												cfnlib.webdriver.navigate().to(launchURl);
												cfnlib.waitForPageToBeReady();
												Thread.sleep(10000);
											}	
						
											report.detailsAppend("*** CURRENCY AND LANGUAGE ***", "Currency: "+languageCode+", Language: "+languageCurrency,"", "");
											
											report.detailsAppend("Sanity Suite Test cases in "+languageCurrency+" for "+languageCode+" ", "Sanity Suite Test cases", "", "");
											
											report.detailsAppendNoScreenshot("Verify language translations in freegames","Translations should be in " + languageDescription + " correctly", "Pass",languageCode);

						cfnlib.waitForElement("freeGamesOffersPlayNowButtonPresent");
						//if (imageLibrary.isImageAppears("FreeGamesPlayLater")) {
							if (cfnlib.isElementVisible("FreeGamesPlayLater")) {
							report.detailsAppend("Verify Game launched ", "Game should be launched", "Game is launched",
									"PASS");
						} else {
							report.detailsAppend("Verify Game launchaed ", "Game should be launched",
									"Game is not launched", "FAIL");
						}

						Thread.sleep(5000);

						if (imageLibrary.isImageAppears("FreeGamesPlayNow"))

						{
							*//************************* Freegame Text **********************************//*
							boolean isFreegamesCorrect = cfnlib.compareText(Freegames, "getFreegamesText", "yes");
							if (isFreegamesCorrect) {
								System.out.println("Correct translation for Freegames text");
								log.debug("Correct translation for Freegames text");

								report.detailsAppendNoScreenshot("Verify Freegames text translation",
										"Freegames text should display correctly in " + languageDescription,
										"Freegames is correct", "Pass");
							} else {
								System.out.println("Incorrect translation for Freegames text");
								log.debug("Incorrect translation for Freegames text");
								report.detailsAppendNoScreenshot("Verify Freegames text translation",
										"Freegames text should display correctly in " + languageDescription,
										"Freegames is Incorrect", "Fail");

							}

							*//************************** Congratulations Text**********************************//*
							boolean isCongratulationsCorrect = cfnlib.compareText(Congratulations, "getCongratulationsText",
									"yes");
							if (isCongratulationsCorrect) {
								System.out.println("Correct translation for Congratulations text");
								log.debug("Correct translation for Congratulations text");

								report.detailsAppendNoScreenshot("Verify Congratulations text translation",
										"Congratulations text should display correctly in " + languageDescription,
										"Congratulations is correct", "Pass");
							} else {
								System.out.println("Incorrect translation for Congratulations text");
								log.debug("Incorrect translation for Congratulations text");
								report.detailsAppendNoScreenshot("Verify Congratulations text translation",
										"Congratulations text should display correctly in " + languageDescription,
										"Congratulations is Incorrect", "Fail");
							}

							*//************************* PlayLater Text **********************************//*
							boolean isPlayLaterCorrect = cfnlib.compareText(PlayLater, "getPlayLaterText", "yes");
							if (isPlayLaterCorrect) {
								System.out.println("Correct translation for PlayLater text");
								log.debug("Correct translation for PlayLater text");

								report.detailsAppendNoScreenshot("Verify PlayLater text translation",
										"PlayLater text should display correctly in " + languageDescription,
										"PlayLater is correct", "Pass");
							} else {
								System.out.println("Incorrect translation for PlayLater text");
								log.debug("Incorrect translation for PlayLater text");
								report.detailsAppendNoScreenshot("Verify PlayLater text translation",
										"PlayLater text should display correctly in " + languageDescription,
										"PlayLater is Incorrect", "Fail");
							}

							*//************************* PlayLater Text **********************************//*
							boolean isPlayNowCorrect = cfnlib.compareText(PlayNow, "getPlayNowText", "yes");
							if (isPlayNowCorrect) {
								System.out.println("Correct translation for PlayNow text");
								log.debug("Correct translation for PlayNow text");

								report.detailsAppendNoScreenshot("Verify PlayNow text translation",
										"PlayNow text should display correctly in " + languageDescription,
										"PlayNow is correct", "Pass");
							} else {
								System.out.println("Incorrect translation for PlayNow text");
								log.debug("Incorrect translation for PlayNow text");
								report.detailsAppendNoScreenshot("Verify PlayNow text translation",
										"PlayNow text should display correctly in " + languageDescription,
										"PlayLater is Incorrect", "Fail");
							}

							report.detailsAppend("Verify FG Play Later button visible",
									"Play Later button Should be visible", "FG Play Later button is visible", "PASS");
							Thread.sleep(3000);
							imageLibrary.click("FreeGamesPlayLater");

							Thread.sleep(3000);
							if (cfnlib.XpathMap.get("NFD").equalsIgnoreCase("Yes")) {
								System.out.println("PlayLater button clicked");
								// check for visiblity of nfd button and take screenshot
								Thread.sleep(5000);
								//if (imageLibrary.isImageAppears("NFDButton")) {
									imageLibrary.click("NFDButton");

									report.detailsAppend("Verify if able to click on continue",
											"Should be able to click on continue button",
											"Able to click on continue button", "PASS");
									System.out.println("NFD button clicked");
									Thread.sleep(5000);

								
									*//************************ Credit *********************************//*
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

									*//************************ Bet *********************************//*
									boolean isBetCorrect = cfnlib.compareText(Bet, "getBetText", "yes");
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

									try {
										// if (imageLibrary.isImageAppears("BetButton")) {
										if (cfnlib.isElementVisible("IsBetButtonVisible")) {

											imageLibrary.click("BetButton");
											Thread.sleep(35000);
											report.detailsAppendFolder("Verify able to click on Bet button",
													"Should be able to click on Bet button", "Clicked on Bet button",
													"Pass", languageCode);

										}

										Thread.sleep(2000);

										*//************************ total bet *********************************//*

										boolean isTotalBetCorrect = cfnlib.compareText(TotalBet, "getTotalBetText",
												"yes");
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

										*//************************ coin size slider *********************************//*

										if (cfnlib.XpathMap.get("CoinSizeSliderPresent").equalsIgnoreCase("Yes")) {
											boolean isCoinSizeCorrect = cfnlib.compareText(CoinSize, "getCoinSizeText",
													"yes");
											if (isCoinSizeCorrect) {
												System.out.println("Correct translation for CoinSize");
												log.debug("Correct translation for CoinSize");
												report.detailsAppendNoScreenshot("Verify CoinSize text translation",
														"CoinSize text should display correctly in "
																+ languageDescription,
														"CoinSize is correct", "Pass");
											} else {
												System.out.println("Incorrect translation for CoinSize");
												log.debug("Incorrect translation for CoinSize");
												report.detailsAppendNoScreenshot("Verify CoinSize text translation",
														"CoinSize text should display correctly in "
																+ languageDescription,
														"CoinSize is Incorrect", "Fail");
											}
										}

										Thread.sleep(2000);

										*//************************* coin Per line slider*********************************//*

										
										  if(cfnlib.XpathMap.get("CoinsPerLineSliderPresent").equalsIgnoreCase("Yes"))
										  { boolean isCoinsPerLineCorrect = cfnlib.compareText(CoinsPerLine,
										  "getCoinsPerLineText","yes"); if (isCoinsPerLineCorrect) {
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
										  
										  Thread.sleep(2000);
										 

										*//************************ Quick bet *********************************//*

										boolean isquickbetCorrect = cfnlib.compareText(QuickBet, "isquickbetCorrect",
												"yes");
										if (isquickbetCorrect) {
											System.out.println("Correct translation for quick bet Correct");
											log.debug("Correct translation for quick bet Correct");
											report.detailsAppendNoScreenshot("Verify quick bet Correct translation",
													"quick bet Correct text should display correctly in "
															+ languageDescription,
													"quick bet is correct", "Pass");
										} else {
											System.out.println("Incorrect translation for quick bet Correct");
											log.debug("Incorrect translation for quick bet Correct");
											report.detailsAppendNoScreenshot(
													"Verify quick bet Correct text translation",
													"quick bet Correct text should display correctly in "
															+ languageDescription,
													"quick bet is Incorrect", "Fail");
										}

									} catch (Exception e) {
										log.error(e.getMessage(), e);

									}

									try {

										*//************************ Menu button *********************************//*

										if (cfnlib.isElementVisible("isMenuBtnVisible")) {

											imageLibrary.click("Menu");
											Thread.sleep(35000);
											report.detailsAppendFolder("Verify language translations in Menu panel",
													"Should be able to click on Menu button", "Clicked on Menu button",
													"Pass", languageCode);
											
										}

										// Thread.sleep(2000);

										*//************************ Settings *********************************//*

										boolean isSettingsCorrect = cfnlib.compareText(Settings, "getSettingsText",
												"yes");
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

										*//************************ Banking *********************************//*

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

										*//************************ Paytable *********************************//*
										if (cfnlib.isElementVisible("isPaytableBtnVisible")) {
											boolean isPaytableCorrect = cfnlib.compareText(PAYTABLE, "getPaytableText",
													"yes");
											if (isPaytableCorrect) {
												System.out.println("Correct translation for Paytable text");
												log.debug("Correct translation for Paytable text");
												report.detailsAppendNoScreenshot("Verify Paytable text translation",
														"Paytable text should display correctly in "
																+ languageDescription,
														"Paytable is correct", "Pass");
											} else {
												System.out.println("Incorrect translation for Paytable text");
												log.debug("Incorrect translation for Paytable text");
												report.detailsAppendNoScreenshot("Verify Paytable text translation",
														"Paytable text should display correctly in "
																+ languageDescription,
														"Paytable is Incorrect", "Fail");
											}

											imageLibrary.click("Paytable");
											Thread.sleep(35000);
											report.detailsAppendFolder("Verify able to click on Paytable button",
													"Should be able to click on Paytable button", "Clicked on Paytable",
													"Pass", languageCode);

											Thread.sleep(2000);

											imageLibrary.click("PaytableBack");

											Thread.sleep(35000);
											report.detailsAppendFolder("Verify able to click on Paytable back button",
													"Should be able to click on Paytable back button",
													"clicked on Paytable Back button", "Pass", languageCode);

										}

										*//************************ Lobby *********************************//*
										
										  boolean isLobbyCorrect = cfnlib.compareText(Lobby, "getLobbyText","yes"); if
										  (isLobbyCorrect) { System.out.println("Correct translation for Lobby text");
										  log.debug("Correct translation for Lobby text");
										  report.detailsAppendNoScreenshot("Verify Lobby text translation",
										  "Lobby text should display correctly in " + languageDescription,
										  "Lobby is correct", "Pass"); } else {
										  System.out.println("Incorrect translation for Lobby text");
										  log.debug("Incorrect translation for Lobby text");
										  report.detailsAppendNoScreenshot("Verify Lobby text translation",
										  "Lobby text should display correctly in " + languageDescription,
										  "Lobby is Incorrect", "Fail"); }
										 

									} catch (Exception e) {
										log.error(e.getMessage(), e);

									}

									Thread.sleep(2000);

									try {
										if (cfnlib.isElementVisible("isSettingsBtnVisible")) {

											imageLibrary.click("Settings");
											Thread.sleep(35000);
											report.detailsAppendFolder("Verify language translations in Settings panel",
													"Should be able to click on Settings button",
													"Clicked on Settings button", "Pass", languageCode);

											
											 * report.detailsAppendFolder("Verify able to click on Settings button",
											 * "Should be able to click on Settings button",
											 * "Able to click on Settings button", "Pass", languageCode);
											 
										}

										// Thread.sleep(2000);

										*//************************ Sounds *********************************//*

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

										*//************************* quick spin on setting*********************************//*

										boolean isQuickSpinCorrect = cfnlib.compareText(QuickSpin, "getQuickSpinText",
												"yes");
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
									*//************************ Autoplay Page *********************************//*

									if (cfnlib.isElementVisible("isAutoplayMenuBtnVisible")) {

										imageLibrary.click("Autoplay");
										Thread.sleep(35000);
										report.detailsAppendFolder("Verify able to click on Autoplay button",
												"Should be able to click on Autoplay button",
												"Clicked on Autoplay button", "Pass", languageCode);

										*//************************ autoplay Spin Text *********************************//*

										boolean isSpinAutoPlayCorrect = cfnlib.compareText(Spin, "getAutoplaySpinText",
												"yes");
										if (isSpinAutoPlayCorrect) {
											System.out.println("Correct translation for AutoplaySpin text");
											log.debug("Correct translation for AutoplaySpin text");
											report.detailsAppendNoScreenshot("Verify AutoplaySpin text translation",
													"AutoplaySpin text should display correctly in "
															+ languageDescription,
													"AutoplaySpin is correct", "Pass");
										} else {
											System.out.println("Incorrect translation for AutoplaySpin text");
											log.debug("Incorrect translation for AutoplaySpin text");
											report.detailsAppendNoScreenshot("Verify AutoplaySpin text translation",
													"AutoplaySpin text should display correctly in "
															+ languageDescription,
													"AutoplaySpin is Incorrect", "Fail");
										}

										*//************************ Autoplay total bet *********************************//*

										boolean istotalBetAutoplayCorrect = cfnlib.compareText(TotalBet,
												"gettotalBetAutoplayText", "yes");
										if (istotalBetAutoplayCorrect) {
											System.out.println("Correct translation for total Bet Autoplay text");
											log.debug("Correct translation for total Bet Autoplay text");
											report.detailsAppendNoScreenshot(
													"Verify total Bet Autoplay text translation",
													"total Bet Autoplay text should display correctly in "
															+ languageDescription,
													"total Bet Autoplay is correct", "Pass");
										} else {
											System.out.println("Incorrect translation for total Bet Autoplay text");
											log.debug("Incorrect translation for total Bet Autoplay text");
											report.detailsAppendNoScreenshot(
													"Verify total Bet Autoplay text translation",
													"total Bet Autoplay text should display correctly in "
															+ languageDescription,
													"total Bet Autoplay is Incorrect", "Fail");
										}

										*//************************ Autoplay text *********************************//*

										boolean isAutoplayCorrect = cfnlib.compareText(Autoplay, "getAutoplayText",
												"yes");
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

										*//************************ Start Autoplay *********************************//*

										
										 * boolean isStartAutoplayCorrect = cfnlib.compareText(StartAutoplay,
										 * "getStartAutoplayText", "yes"); if (isStartAutoplayCorrect) {
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
										 
										imageLibrary.click("MenuClose");

									} else {
										report.detailsAppend("Verify Continue button is visible after FG Play Later ",
												"Continue buttion should be visible", "Continue button is not visible",
												"FAIL");
									}
								//}
							} else {
								Thread.sleep(5000);

								//if (imageLibrary.isImageAppears("Spin")) 
									if (cfnlib.isElementVisible("Spin")) {
									System.out.println("Base Scene after FG Play Later");
									log.debug("Base Scene after FG Play Later");
									report.detailsAppend("Verify Base Scene after FG Play Later", "Base is visible",
											"Free Game Scene is visible", "PASS");
								} else {
									System.out.println("Base Scene after FG Play Later is not visible");
									log.debug("Base Scene after FG Play Later is not visible");
									report.detailsAppend("Verify Base Scene after FG Play Later", "Base is visible",
											"Free Game Scene is not visible", "FAIL");
								}
							}
						} else {
							report.detailsAppend("Verify FG Play Later button visible",
									"Play Later button Should be visible", "FG Play Later button is not visible",
									"FAIL");
						}}else
						{
							report.detailsAppend("Verify FG Play Later button visible", "Play Later button Should be visible", "FG Play Later button is not visible", "FAIL");
						}	

						// ===========================================================================================
						*//*************************** Check Delete offer***********************************//*

						// Refresh Game to test "Delete Offer" case
						cfnlib.RefreshGame("clock");
						Thread.sleep(5000);
						//if (imageLibrary.isImageAppears("FreeGamesPlayNow"))
							if (cfnlib.isElementVisible("FreeGamesPlayNow")){
							report.detailsAppend("Verify Game launchaed after refresh to check Discard FG Offer",
									"Game should be launched", "Game is launched", "PASS");
							//if (imageLibrary.isImageAppears("FGDelete")) {
							if(cfnlib.isElementVisible("FGDelete")) {
								report.detailsAppend("Verify FG Discard offer Button visible",
										"FG Discard offer Button should be visible",
										"FG Discard offer Button is visible", "PASS");
								imageLibrary.click("FGDelete");

								Thread.sleep(350000);
								//if (imageLibrary.isImageAppears("FGDiscard")) 
									if (cfnlib.isElementVisible("FGDiscard")){

									System.out.println("Clicked on free game delete button");

									*//************** Check Discard Text ***************************************//*
									boolean isDiscardCorrect = cfnlib.compareText(Discard, "getDiscardText", "yes");
									if (isDiscardCorrect) {
										System.out.println("Correct translation for Discard text");
										log.debug("Correct translation for Discard text");
										report.detailsAppendNoScreenshot("Verify Discard text translation",
												"Discard text should display correctly in " + languageDescription,
												"Discard is correct", "Pass");
									} else {
										System.out.println("Incorrect translation for Discard text");
										log.debug("Incorrect translation for Discard text");
										report.detailsAppendNoScreenshot("Verify Discard text translation",
												"Discard text should display correctly in " + languageDescription,
												"Discard is Incorrect", "Fail");
									}
									
									*//************** Check Keep It Text ***************************************//*
									boolean isKeepItCorrect = cfnlib.compareText(KeepIt, "getKeepItText", "yes");
									if (isKeepItCorrect) {
										System.out.println("Correct translation for KeepIt text");
										log.debug("Correct translation for KeepIt text");
										report.detailsAppendNoScreenshot("Verify KeepIt text translation",
												"KeepIt text should display correctly in " + languageDescription,
												"KeepIt is correct", "Pass");
									} else {
										System.out.println("Incorrect translation for KeepIt text");
										log.debug("Incorrect translation for KeepIt text");
										report.detailsAppendNoScreenshot("Verify KeepIt text translation",
												"KeepIt text should display correctly in " + languageDescription,
												"KeepIt is Incorrect", "Fail");
									}

									report.detailsAppend("Verify FG Discard page visible",
											"FG Discard page should be visible", "Discard page is visible", "PASS");
									imageLibrary.click("FGDiscard");

									Thread.sleep(1000);
									if (cfnlib.isElementVisible("NFDButton")) {
										report.detailsAppend("Verify Continue button is visible after discarding FG ",
												"FG should be discarded and Continue buttion shold be visible",
												"Continue button is visible", "PASS");
									} else {
										report.detailsAppend("Verify Continue button is visible after discarding FG ",
												"FG should be discarded and Continue buttion shold be visible",
												"Continue button is not visible", "FAIL");
									}
								} else {
									report.detailsAppend("VerifyFG Discard page visible ",
											"FG Discard page should be visible", "Discard page is not visible", "FAIL");
								}
							} else {
								report.detailsAppend("Verify FG Discard offer Button visible",
										"FG Discard offer Button should be visible",
										"FG Delete offer Button is not visible", "FAIL");
							}
						} else {
							report.detailsAppend("Verify Game launchaed after refresh to check Discard FG Offer ",
									"Game should be launched", "Game is not launched", "FAIL");
						}
						// Assign Free Games to test Normal win and Big win
						boolean isFreeGameAssigned1 = false;
						isFreeGameAssigned1 = cfnlib.assignFreeGames(userName, offerExpirationUtcDate, mid, cid,
								languageCnt, defaultNoOfFreeGames);
						System.out.println("Free Games assignement status is : " + isFreeGameAssigned1);
						log.debug("Free Games assignement status is : " + isFreeGameAssigned1);
						Thread.sleep(60000);

						if (isFreeGameAssigned1) {
							Thread.sleep(5000);
							cfnlib.RefreshGame("clock");
							Thread.sleep(5000);
							// click on play now button on Free Games Intro Screen
							if (imageLibrary.isImageAppears("FreeGamesPlayNow")) {
								imageLibrary.click("FreeGamesPlayNow");
								Thread.sleep(3000);
								if (cfnlib.XpathMap.get("NFD").equalsIgnoreCase("Yes")) {
									if (cfnlib.XpathMap.get("NFDHasHook").equalsIgnoreCase("Yes")) {
										// check for visiblity of nfd button and take screenshot

										if (imageLibrary.isImageAppears("NFDButton")) {
											report.detailsAppend("Verify Continue button is visible ",
													"Continue buttion is visible", "Continue button is visible",
													"PASS");
											// Click on nfd button

											Thread.sleep(5000);

											imageLibrary.click("NFDButton");
											Thread.sleep(10000);

											if (imageLibrary.isImageAppears("Spin")) {
												System.out.println("Free Game Scene is visible");
												log.debug("Free Game Scene is visible");
												report.detailsAppend("Verify Free Game Scene",
														"Free Game Scene is visible", "Free Game Scene is visible",
														"PASS");
											} else {
												System.out.println("Free Game Scene is not visible");
												log.debug("Free Game Scene is not visible");
												report.detailsAppend("Verify Free Game Scene",
														"Free Game Scene is visible", "Free Game Scene is not visible",
														"PASS");
											}
										} else {
											report.detailsAppend("Verify Continue button is visible ",
													"Continue buttion is visible", "Continue button is not visible",
													"FAIL");
										}
									}
								} else {

									if (imageLibrary.isImageAppears("Spin")) {
										System.out.println("Free Game Scene is visible");
										log.debug("Free Game Scene is visible");
										report.detailsAppend("Verify Free Game Scene", "Free Game Scene is visible",
												"Free Game Scene is visible", "PASS");
									} else {
										System.out.println("Free Game Scene is not visible");
										log.debug("Free Game Scene is not visible");
										report.detailsAppend("Verify Free Game Scene", "Free Game Scene is visible",
												"Free Game Scene is not visible", "PASS");
									}
								}
							}

							// method is used to Spin the Spin Button (Normal Win)
							try {
								if (imageLibrary.isImageAppears("Spin")) {
									cfnlib.details_append_folderOnlyScreeshot(webdriver, "SpinBtnWrk");
									Thread.sleep(2000);

									// click on spin button
									imageLibrary.click("Spin");
									Thread.sleep(9000);

									// check if spin is successful,take screenshot
									report.detailsAppend("verify BaseGame normal win", "Spin Button is working",
											"Spin Button is working", "pass");

								} else {
									cfnlib.details_append_folderOnlyScreeshot(webdriver, "SpinBtnNtWrk");
									report.detailsAppend("verify BaseGame normal win", "Spin Button is not working",
											"Spin Button is not working", "Fail", "" + languageCode);

								}
							} catch (Exception e) {
								log.error(e.getMessage(), e);
								cfnlib.evalException(e);
							}
							Thread.sleep(2000);

							// method is used to Spin the Spin Button (Big Win)
							try {
								if (imageLibrary.isImageAppears("Spin")) {
									Thread.sleep(3000);
									imageLibrary.click("Spin");
									Thread.sleep(9000);
									// Verifies the Big Win currency format

								}
							} catch (Exception e) {
								log.error(e.getMessage(), e);
								cfnlib.evalException(e);
							}

							// free games on refresh, Resume and Info Text currency validation
							cfnlib.RefreshGame("clock");
							Thread.sleep(5000);

							// Click on Resume

							imageLibrary.click("FGResumePlay");
							Thread.sleep(5000);
							// click on Continue
							if (cfnlib.XpathMap.get("NFD").equalsIgnoreCase("Yes")) {
								if (cfnlib.XpathMap.get("NFDHasHook").equalsIgnoreCase("Yes")) {
									// check for visiblity of nfd button and take screenshot
									if (imageLibrary.isImageAppears("NFDButton")) {
										report.detailsAppend("Verify Continue button is visible ",
												"Continue buttion is visible", "Continue button is visible", "PASS");
									} else {
										report.detailsAppend("Verify Continue button is visible ",
												"Continue buttion is visible", "Continue button is not visible",
												"FAIL");
									}
									// Click on nfd button
									Thread.sleep(2000);

									imageLibrary.click("NFDButton");
									Thread.sleep(10000);

									if (imageLibrary.isImageAppears("Spin")) {
										report.detailsAppend("Verify Free Game Scene", "Free Game Scene is visible",
												"Free Game Scene is visible", "PASS");
									} else {
										report.detailsAppend("Verify Free Game Scene", "Free Game Scene is visible",
												"Free Game Scene is not visible", "PASS");
									}
								}
							} else {
								if (imageLibrary.isImageAppears("Spin")) {
									report.detailsAppend("Verify Free Game Scene", "Free Game Scene is visible",
											"Free Game Scene is visible", "PASS");
								} else {
									report.detailsAppend("Verify Free Game Scene", "Free Game Scene is visible",
											"Free Game Scene is not visible", "PASS");
								}
							}

							// FG Summary

						} else {
							System.out.println("Free Games is not assigned second time: FAIL");
							log.debug("Free Games is not assigned second time: FAIL");
						}
					} catch (Exception e) {
						log.error(e.getMessage(), e);
						cfnlib.evalException(e);
					}
					// -------------------Closing the connections---------------//

				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			cfnlib.evalException(e);
		} finally {
			report.endReport();
			webdriver.close();
			webdriver.quit();
			// proxy.abort();
			Thread.sleep(1000);
		}
	}
}
*/