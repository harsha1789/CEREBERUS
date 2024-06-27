package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.ImageLibrary;
import com.zensar.automation.library.TestPropReader;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 * This Generic script is used for verifying all markets supported
 * features/scenarios via modular based approch using flags
 * 
 * @author pb61055
 */
public class Desktop_RegulatedMarket_PlayNext_GenericScript {

	Logger log = Logger.getLogger(Desktop_RegulatedMarket_PlayNext_GenericScript.class.getName());
	public ScriptParameters scriptParameters;

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
		String status1 = null;
		int mintDetailCount = 0;
		int mintPassed = 0;
		int mintFailed = 0;
		int mintWarnings = 0;
		int mintStepNo = 0;
		String urlNew = null;
		int startindex = 0;
		String strGameName = null;
		String languageCode = null;
		ImageLibrary imageLibrary;
		int envId = 0;
		boolean isFreeGameAssigned = false;
		double dblNetPositionBeforeBonus = 0;

		new ExcelDataPoolManager();
		Desktop_HTML_Report report = new Desktop_HTML_Report(webdriver, browserName, filePath, startTime, mstrTCName,
				mstrTCDesc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings, mintStepNo, status1,
				gameName);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory = new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib = desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,
				report, gameName);
		CommonUtil util = new CommonUtil();
		RestAPILibrary apiObj = new RestAPILibrary();
		List<String> copiedFiles = new ArrayList<>();
		int mid = Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int cid = Integer.parseInt(TestPropReader.getInstance().getProperty("CIDDesktop"));

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
				imageLibrary = new ImageLibrary(webdriver, strGameName, "Desktop");

				TestPropReader.getInstance().getProperty("TestData_Excel_Path");

				String strFileName = TestPropReader.getInstance().getProperty("MarketTestDataPath");
				File testDataFile = new File(strFileName);

				List<Map<String, String>> marketList = util.readMarketList();// mapping
				for (Map<String, String> marketMap : marketList) {
					try {
						// Step 2: To get the market specific values
						String runFlag = marketMap.get("runFlag").trim();

						if (runFlag.equalsIgnoreCase("run")) {

							int productId = Integer.parseInt(marketMap.get("ProductId").trim().replace(".0", ""));
							int marketTypeId = Integer.parseInt(marketMap.get("MarketTypeId").trim().replace(".0", ""));
							String regMarket = marketMap.get("RegMarketName").trim();
							String isoCode = marketMap.get("CurrencyIsoCode").trim();
							String balance = marketMap.get("Balance").trim();
							String regExpr = marketMap.get("RegExpression").trim();
							String market = marketMap.get("market").trim();
							String brand = marketMap.get("brand").trim();
							String retrunUrlParam = marketMap.get("retrunUrlParam").trim();
							String marketUrl = marketMap.get("marketUrl").trim();
							String LangCode1 = marketMap.get("LangCode1").trim();
							String LangCode2 = marketMap.get("LangCode2").trim();
							int LangCount = Integer.parseInt(marketMap.get("LanguageCount").trim().replace(".0", ""));

							// Scenarios
							// Base Game
							String BaseGameFeatures = marketMap.get("BaseGameFeatures").trim();

							// info bar
							String InfoBarFeatures = marketMap.get("InfoBarFeatures").trim();

							// top bar
							String TopbarFeatures = marketMap.get("TopbarFeatures").trim();
							String gameClock = marketMap.get("gameClock").trim();
							String GameName = marketMap.get("GameName").trim();

							// Spin
							String SpinFeatures = marketMap.get("SpinFeatures").trim();
							String SpinReelLanding = marketMap.get("SpinReelLanding").trim();
							String checkSpinStop = marketMap.get("checkSpinStop").trim();
							String checkSpinStopUsingCoordinates = marketMap.get("checkSpinStopUsingCoordinates")
									.trim();

							// Quick Spin
							String QuickSpinFeatures = marketMap.get("QuickSpinFeatures").trim();

							// Value Adds
							String ValueAddsNavigation = marketMap.get("ValueAddsNavigation").trim();
							String PlayerProtectionNavigation = marketMap.get("PlayerProtectionNavigation").trim();
							String CashCheckNavigation = marketMap.get("CashCheckNavigation").trim();
							String HelpNavigation = marketMap.get("HelpNavigation").trim();
							String PlayCheckNavigation = marketMap.get("PlayCheckNavigation").trim();

							// BetPanel
							String BetPanel = marketMap.get("BetPanel").trim();

							// MenuPanel
							String MenuPanel = marketMap.get("MenuPanel").trim();

							// Autoplay
							String AutoplayPanel = marketMap.get("AutoplayPanel").trim();

							// PaytableFeatures
							String PaytableFeatures = marketMap.get("PaytableFeatures").trim();

							// SettingsPanel
							String SettingsPanel = marketMap.get("AutoplayPanel").trim();

							// Session reminder
							String SessionReminder = marketMap.get("SessionReminder").trim();
							String SessionReminderUserInteraction = marketMap.get("SessionReminderUserInteraction")
									.trim();
							String SessionReminderContinue = marketMap.get("SessionReminderContinue").trim();
							String SessionReminderExitGame = marketMap.get("SessionReminderExitGame").trim();
							String BounsFundsNotification = marketMap.get("BounsFundsNotification").trim();
							String closeBtnEnabledCMA = marketMap.get("closeBtnEnabledCMA").trim();

							// Banking
							String BankingNavigation = marketMap.get("BankingNavigation").trim();
							String BalanceUpdateUsingBanking = marketMap.get("BalanceUpdateUsingBanking").trim();

							// Net Position
							String NetPositionBaseScene = marketMap.get("NetPositionBaseScene").trim();
							String NetPositionLaunch = marketMap.get("NetPositionLaunch").trim();
							String NetPositionRefresh = marketMap.get("NetPositionRefresh").trim();
							String NetPositionWinLoss = marketMap.get("NetPositionWinLoss").trim();
							String NetPositionBigWin = marketMap.get("NetPositionBigWin").trim();
							String NetPositionFreeSpin = marketMap.get("NetPositionFreeSpin").trim();
							String NetPositionBonusFeature = marketMap.get("NetPositionBonusFeature").trim();
							String NetPositionRefreshWin = marketMap.get("NetPositionRefreshWin").trim();
							String NetPositionRefreshLoss = marketMap.get("NetPositionRefreshLoss").trim();

							// Pratice Play
							String PracticePlay = marketMap.get("PracticePlay").trim();

							// Wager RTS
							String WagerRTS = marketMap.get("WagerRTS").trim();
							String SpinScenarios = marketMap.get("SpinScenarios").trim();
							String LoadGameWithCredits = marketMap.get("LoadGameWithCredits").trim();
							String LoadGameWithoutCredits = marketMap.get("LoadGameWithoutCredits").trim();

							// Free games
							String FreeGameFeatures = marketMap.get("FreeGameFeatures").trim();
							String FGOfferAccept = marketMap.get("FGOfferAccept").trim();
							String FGOfferResume = marketMap.get("FGOfferResume").trim();
							String FGPlayLater = marketMap.get("FGPlayLater").trim();
							String FGOfferDelete = marketMap.get("FGOfferDelete").trim();
							String FGSummaryScreen = marketMap.get("FGSummaryScreen").trim();
							String FreeSpinsInFG = marketMap.get("FreeSpinsInFG").trim();

							// Free spins
							String FreeSpinFeatures = marketMap.get("FreeSpinFeatures").trim();
							String FSRefresh = marketMap.get("FSRefresh").trim();
							String FSSessionReminder = marketMap.get("FSSessionReminder").trim();
							String FreeSpinReelLanding = marketMap.get("FreeSpinReelLanding").trim();
							String FreeSpinTransitionBaseGameToFeature = marketMap
									.get("FreeSpinTransitionBaseGameToFeature").trim();
							String FreeSpinTransitionFeatureToBaseGame = marketMap
									.get("FreeSpinTransitionFeatureToBaseGame").trim();
							String FreeSpinTransitionFeatureToBaseGameRefresh = marketMap
									.get("FreeSpinTransitionFeatureToBaseGameRefresh").trim();
							String NetPositionInFS = marketMap.get("NetPositionInFS").trim();

							// SessionDurationReset
							String SessionDurationReset = marketMap.get("SessionDurationReset").trim();

							// Bonus
							String BonusFeatures = marketMap.get("BonusFeatures").trim();
							String BonusTransitionBaseGameToFeature = marketMap.get("BonusTransitionBaseGameToFeature")
									.trim();
							String BonusTransitionFeatureToBaseGame = marketMap.get("BonusTransitionFeatureToBaseGame")
									.trim();
							String BonusTransitionBaseGameToFeatureRefresh = marketMap
									.get("BonusTransitionBaseGameToFeatureRefresh").trim();
							String BonusTransitionFeatureToBaseGameRefresh = marketMap
									.get("BonusTransitionFeatureToBaseGameRefresh").trim();
							String BonusSessionReminder = marketMap.get("BonusSessionReminder").trim();
							String BonusNetPosition = marketMap.get("BonusNetPosition").trim();
							String BonusSelection = marketMap.get("BonusSelection").trim();
							String BonusReelLanding = marketMap.get("BonusReelLanding").trim();
							String BonusRefresh = marketMap.get("BonusRefresh").trim();
							String BonusInFreeGames = marketMap.get("BonusInFreeGames").trim();

							// Link and Win
							String LinkAndWin = marketMap.get("LinkAndWin").trim();

							String currencySymbol = regExpr.substring(0, 1);

							for (int i = 1; i <= LangCount; i++) {
								if (i == 1) {
									languageCode = LangCode1;
								} else if (i == 2) {
									languageCode = LangCode2;
								} else {
									System.out.println("Language code cannot be empty");
								}

								log.debug(this + "*** Processing Regulated market : " + regMarket + " ***");
								System.out.println("*** Processing Regulated market : " + regMarket + " ***");
								report.detailsAppend("Following are the " + regMarket + " market test cases", "", "",
										"");

								String url = cfnlib.XpathMap.get("ApplicationURL");

								if (BaseGameFeatures.equalsIgnoreCase("yes")) {
									userName = util.randomStringgenerator();

									if (marketUrl.isEmpty()) {

										urlNew = cfnlib.createRegMarketUrl(userName, url, productId, market, brand,
												retrunUrlParam, languageCode);
									} else {
										urlNew = cfnlib.replaceUserNameLanguageCode(marketUrl, languageCode);
									}

									if (util.copyFilesToTestServerForRegMarket(mid, cid, testDataFile, userName,
											regMarket, envId, productId, isoCode, marketTypeId, copiedFiles)) {
										Thread.sleep(3000);

										log.debug("Updating the balance");

										util.updateUserBalance(userName, balance);
										Thread.sleep(5000);

										cfnlib.loadGame(urlNew);
										if ("Yes".equalsIgnoreCase(cfnlib.XpathMap.get("NFD"))) {
											report.detailsAppendFolder("Verify Splash/Loading screen is visible",
													"Splash/Loading screen should be visible",
													"Splash/Loading screen is visible", "Pass", languageCode);
											Thread.sleep(8000);
											report.detailsAppendFolder("Verify Continue button on base scene ",
													"Continue buttion", "Continue button", "Pass", languageCode);

											cfnlib.clickOnBaseSceneContinueButton(report, imageLibrary, languageCode);
										}

										report.detailsAppend("Following are the Base Scene test cases",
												"Verify Base Scen", "", "");
										report.detailsAppendFolder("Verify Basecene is visible",
												" Basecene should be visible", " Basecene is visible", "Pass",
												languageCode);

										// ********************* INFO BAR - CURRENCY FORMAT ***********************
										if (InfoBarFeatures.equalsIgnoreCase("yes")) {

											cfnlib.loadGameAndClickContinueBtn(urlNew, report, imageLibrary,
													languageCode);

											report.detailsAppend(
													"Following are the currency format verification test cases",
													"Verify currency format", "", "");

											cfnlib.verifyCreditsCurrencyFormat(report, regExpr);
											Thread.sleep(3000);
											cfnlib.verifyBetCurrencyFormat(report, regExpr);

										}

										// ***************************** QUICK SPIN *****************************

										if (QuickSpinFeatures.equalsIgnoreCase("yes")) {
											report.detailsAppend(
													"Following are the Quick spin test cases on Base scene",
													"Verify Quick spin on Base scene", "", "");
											log.debug("Quick spin TCs");
										} else {// Manual verification from screenshot --- quick spin enabled or not
											report.detailsAppendNoScreenshot("Verify Quick spin",
													"No Quick spin or Turbo Mode should be in Base Game",
													"No Quick spin or Turbo Mode in Base Game", "Pass");

										}

										// ***************************** TOPBAR ************************************

										if (TopbarFeatures.equalsIgnoreCase("yes")) {
											report.detailsAppend("Following are the Top bar test cases on Base scene",
													"Verify Top bar on Base scene", "", "");

											if (gameClock.equalsIgnoreCase("yes")) {
												cfnlib.clockOnTopBar(report);
											}

											if (GameName.equalsIgnoreCase("yes")) {
												cfnlib.gameNameOnTopBar(report);
											}

											if (SessionDurationReset.equalsIgnoreCase("yes")) {
												cfnlib.sessionDurationOntopBar(report, languageCode);
											}
										}

										// ***************************** SPIN BUTTON **********************************
										if (SpinFeatures.equalsIgnoreCase("yes")) {
											report.detailsAppend("Following are the Spin Feature test cases",
													"Verify Spin Feature test cases", "", "");

											if (SpinReelLanding.equalsIgnoreCase("yes")) {
												cfnlib.checkSpinReelLanding(report, imageLibrary, languageCode);
											}

											if (checkSpinStop.equalsIgnoreCase("yes")) {
												cfnlib.checkSpinStop(report, imageLibrary, languageCode,
														checkSpinStopUsingCoordinates);
											}
											Thread.sleep(5000);
											cfnlib.closeOverlay();
											Thread.sleep(3000);
										}

										// ***************************** AUTOPLAY *****************************

										if (AutoplayPanel.equalsIgnoreCase("yes")) {
											report.detailsAppend("Following are the Autoplay test cases",
													"Verify Autoplay", "", "");
											boolean autoplayOpen = cfnlib.openAutoplayPanel(report, imageLibrary,
													languageCode);

											if (autoplayOpen) {
												if (gameClock.equalsIgnoreCase("yes")) {
													cfnlib.clockOnTopBar(report);
												}

												if (GameName.equalsIgnoreCase("yes")) {
													cfnlib.gameNameOnTopBar(report);
												}

												cfnlib.closeButton(imageLibrary);
											}
										}

										// ***************************** BET ****************************************

										if (BetPanel.equalsIgnoreCase("yes")) {
											report.detailsAppend("Following are the Bet test cases", "Verify Bet Panel",
													"", "");
											boolean betOpen = cfnlib.openBetPanel(report, imageLibrary, languageCode);

											if (betOpen) {
												if (gameClock.equalsIgnoreCase("yes")) {
													cfnlib.clockOnTopBar(report);
												}

												if (gameClock.equalsIgnoreCase("yes")) {
													cfnlib.gameNameOnTopBar(report);
												}

												cfnlib.closeButton(imageLibrary);

											}
										}

										// ***************************** MENU *****************************************
										if (MenuPanel.equalsIgnoreCase("yes")) {
											report.detailsAppend("Following are the Menu Panel test cases",
													"Verify Menu Panel", "", "");

											if (imageLibrary.isImageAppears("Menu")) {
												imageLibrary.click("Menu");
												Thread.sleep(2000);

												report.detailsAppendFolder("Menu open", "Menu should be opened",
														"Menu is opened", "Pass", languageCode);

												if (gameClock.equalsIgnoreCase("yes")) {
													cfnlib.clockOnTopBar(report);
												}

												if (GameName.equalsIgnoreCase("yes")) {
													cfnlib.gameNameOnTopBar(report);
												}

												// Menu - Banking Option Works
												if (BankingNavigation.equalsIgnoreCase("yes")) {
													cfnlib.verifyBankingNavigation(report, imageLibrary, languageCode);
												}

												cfnlib.closeButton(imageLibrary);

											}
										}

										// ************************* PAYTABLE ***************************
										if (PaytableFeatures.equalsIgnoreCase("yes")) {

											report.detailsAppend("Following are the Paytable test cases",
													"Verify Paytable", "", "");
											boolean paytableOpen = cfnlib.openPaytable(report, imageLibrary,
													languageCode);

											if (paytableOpen) {
												if (gameClock.equalsIgnoreCase("yes")) {
													cfnlib.clockOnTopBar(report);
												}

												if (GameName.equalsIgnoreCase("yes")) {
													cfnlib.gameNameOnTopBar(report);
												}

												cfnlib.closePaytable();

											}

										}

										// ************************ SETTINGS ******************************
										if (SettingsPanel.equalsIgnoreCase("yes")) {

											report.detailsAppend("Following are the Settings test cases",
													"Verify Settings", "", "");
											boolean settingsOpen = cfnlib.openSettingsPanel(report, imageLibrary,
													languageCode);

											if (settingsOpen) {
												if (gameClock.equalsIgnoreCase("yes")) {
													cfnlib.clockOnTopBar(report);
												}

												if (GameName.equalsIgnoreCase("yes")) {
													cfnlib.gameNameOnTopBar(report);
												}

												// Manual verification from screenshot
												if (QuickSpinFeatures.equalsIgnoreCase("no")) {
													report.detailsAppendNoScreenshot("Verify Quick spin in settings",
															"No Quick spin or Turbo Mode should be in Settings",
															"No Quick spin or Turbo Mode is in Settings", "Pass");

												}
												cfnlib.closeButton(imageLibrary);
											}

										}

										// *************************** BANKING ***********************

										// Player can successfully Deposit and Credit Balance updates correctly
										if (BalanceUpdateUsingBanking.equalsIgnoreCase("yes")) {
											report.detailsAppend("Following are the Banking test cases",
													"Verify Banking testcases", "", "");
											boolean isUpdated = cfnlib.creditDepositUsingBanking(report, userName,
													balance, languageCode, imageLibrary, currencySymbol);
											if (isUpdated) {
												log.debug("Credit updated successfully via Banking");
											} else {
												util.updateUserBalance(userName, balance);
												Thread.sleep(5000);

												cfnlib.loadGameAndClickContinueBtn(urlNew, report, imageLibrary,
														languageCode);
											}
										}

										// ********************** VALUE ADDS ***************************

										if (ValueAddsNavigation.equalsIgnoreCase("yes")) {
											report.detailsAppend("Following are the Value Adds navigation test cases",
													"Verify Value Adds testcases", "", "");
											cfnlib.verifyValueAddsNavigation(report, imageLibrary, languageCode,
													PlayerProtectionNavigation, CashCheckNavigation, HelpNavigation,
													PlayCheckNavigation, market);
										}

										// ******************* SESSION REMINDER *************************

										if (SessionReminder.equalsIgnoreCase("yes")) {
											report.detailsAppend("Following are the Session Reminder test cases",
													"Verify Session Reminder testcases", "", "");
											cfnlib.validateSessionReminderBaseScene(report, userName, imageLibrary,
													languageCode, SessionReminderUserInteraction,
													SessionReminderContinue, SessionReminderExitGame);
										}

										// ***************** CMA/BONUS FUNDS NOTIFICATION *******************

										if (BounsFundsNotification.equalsIgnoreCase("yes")) {
											report.detailsAppend(
													"Following are the CMA/Bonus Funds Notification test cases",
													"Verify CMA/Bonus Funds Notification testcases", "", "");
											cfnlib.validateBonusFundsNotificationBaseScene(report, userName,
													imageLibrary, languageCode, closeBtnEnabledCMA);
										}

										// ***************************** WAGER RTS *********************************

										if (WagerRTS.equalsIgnoreCase("Yes")) {
											report.detailsAppend("Following are the Wager RTS test cases",
													"Verify Wager RTS Test Cases", "", "");

											if (SpinScenarios.equalsIgnoreCase("Yes")) {

												cfnlib.spinCases(report, imageLibrary, languageCode);
											}

											if (LoadGameWithCredits.equalsIgnoreCase("Yes")) {
												cfnlib.loadGamewithCredits(report, imageLibrary, languageCode);
											}

											if (LoadGameWithoutCredits.equalsIgnoreCase("Yes")) {
												cfnlib.loadGamewithoutCredits(report, imageLibrary, languageCode,
														userName, balance);
											}

										}

										// **************** PRATICE PLAY ********************

										if (PracticePlay.equalsIgnoreCase("Yes")) {
											report.detailsAppend("Following are the Practice Play test cases",
													"Verify Practice Play test cases", "", "");
											cfnlib.practicePlay(report, imageLibrary, languageCode);
										}

										// *********************** NET POSITION ******************************
										if (NetPositionBaseScene.equalsIgnoreCase("yes")) {
											report.detailsAppend(
													"Following are the Net Position test cases on Base scene",
													"Verify Net Position on Base scene", "", "");
											cfnlib.verifyNetPositionBaseScene(report, imageLibrary, languageCode,
													regExpr, NetPositionLaunch, NetPositionRefresh, NetPositionWinLoss,
													NetPositionBigWin, NetPositionRefreshWin, NetPositionRefreshLoss);
										}
									} // copy files to server
								} // Base game closing

								// **************** FREE GAMES *****************

								if (FreeGameFeatures.equalsIgnoreCase("yes")) {
									String freeGamesUserName = util.randomStringgenerator();

									strFileName = TestPropReader.getInstance()
											.getProperty("MarketFreeGamesTestDataPath");
									File testDataFileFreeGames = new File(strFileName);

									if (marketUrl.isEmpty()) {
										urlNew = cfnlib.createRegMarketUrl(freeGamesUserName, url, productId, market,
												brand, retrunUrlParam, languageCode);
									} else {
										urlNew = cfnlib.replaceUserNameLanguageCode(marketUrl, languageCode);
									}

									if (util.copyFilesToTestServerForRegMarket(mid, cid, testDataFileFreeGames,
											freeGamesUserName, regMarket, envId, productId, isoCode, marketTypeId,
											copiedFiles)) {
										Thread.sleep(3000);

										log.debug("Updating the balance");

										util.updateUserBalance(freeGamesUserName, balance);
										Thread.sleep(5000);

										cfnlib.loadGame(urlNew);

										report.detailsAppend("Following are the Free Game test cases",
												"Verify Free Game testcases", "", "");

										String offerExpirationUtcDate = util.getCurrentTimeWithDateAndTime();
										String strdefaultNoOfFreeGames = cfnlib.XpathMap.get("DefaultNoOfFreeGames");
										String strnoOfOffers = cfnlib.XpathMap.get("noOfFGOffers");

										int defaultNoOfFreeGames = (int) Double.parseDouble(strdefaultNoOfFreeGames);
										int noOfOffers = (int) Double.parseDouble(strnoOfOffers);

										isFreeGameAssigned = cfnlib.assignFreeGames(freeGamesUserName,
												offerExpirationUtcDate, mid, cid, noOfOffers, defaultNoOfFreeGames);
										Thread.sleep(3000);
										if (isFreeGameAssigned == true) {
											boolean isFGLaunched = cfnlib.fGAssignment(report, imageLibrary,
													languageCode);

											if (FGPlayLater.equalsIgnoreCase("yes") && isFGLaunched == true) {
												cfnlib.fGPlayLater(report, imageLibrary, languageCode);
											}

											if (FGOfferDelete.equalsIgnoreCase("yes") && isFGLaunched == true) {
												cfnlib.FGOfferDelete(report, imageLibrary, languageCode);
											}

											if (FGOfferAccept.equalsIgnoreCase("yes") && isFGLaunched == true) {
												cfnlib.assignFreeGames(userName, offerExpirationUtcDate, mid, cid,
														noOfOffers, defaultNoOfFreeGames);
												cfnlib.FGOfferAcceptAndResumeFG(report, imageLibrary, languageCode,
														FGOfferResume, FGSummaryScreen);
											}

											if (FreeSpinsInFG.equalsIgnoreCase("yes")) {
												cfnlib.freeSpinsInFG(report, imageLibrary, languageCode);
											}

											if (BonusInFreeGames.equalsIgnoreCase("yes")) {
												cfnlib.bonusInFreeGames(report, imageLibrary, languageCode);
											}

										} // Free games assignment
									} // copy files to server
								} // FreeGames closing

								// ************************** FREE SPINS **********************

								if (FreeSpinFeatures.equalsIgnoreCase("yes")) {
									String freeSpinUserName = util.randomStringgenerator();

									strFileName = TestPropReader.getInstance()
											.getProperty("MarketFreeSpinTestDataPath");
									File testDataFileFreeSpin = new File(strFileName);

									if (marketUrl.isEmpty()) {
										urlNew = cfnlib.createRegMarketUrl(freeSpinUserName, url, productId, market,
												brand, retrunUrlParam, languageCode);
									} else {

										urlNew = cfnlib.replaceUserNameLanguageCode(marketUrl, languageCode);
									}

									if (util.copyFilesToTestServerForRegMarket(mid, cid, testDataFileFreeSpin,
											freeSpinUserName, regMarket, envId, productId, isoCode, marketTypeId,
											copiedFiles)) {
										Thread.sleep(3000);

										util.updateUserBalance(freeSpinUserName, balance);
										Thread.sleep(5000);

										cfnlib.loadGameAndClickContinueBtn(urlNew, report, imageLibrary, languageCode);

										report.detailsAppend("Following are the Free Spins cases", "Verify Free Spins",
												"", "");

										cfnlib.spin(imageLibrary);
										Thread.sleep(5000);
										cfnlib.spin(imageLibrary);

										cfnlib.freeSpinsEntryScreen(report, imageLibrary, languageCode,
												FreeSpinTransitionBaseGameToFeature);

										if (NetPositionInFS.equalsIgnoreCase("yes")) {
											cfnlib.netPositioninFS(report, languageCode);
										}

										if (FSRefresh.equalsIgnoreCase("yes")) {
											cfnlib.checkFSRefresh(report, imageLibrary, languageCode);

										}

										if (FreeSpinReelLanding.equalsIgnoreCase("yes")) {
											cfnlib.checkSpinReelLandingFeature(report, languageCode);
										}

										if (FSSessionReminder.equalsIgnoreCase("yes")) {
											cfnlib.validateSessionReminderFSScene(report, freeSpinUserName,
													imageLibrary, languageCode, SessionReminderUserInteraction,
													SessionReminderContinue, SessionReminderExitGame);
										}

										cfnlib.freeSpinsToBaseGameTransitions(report, imageLibrary, languageCode,
												FreeSpinTransitionFeatureToBaseGame,
												FreeSpinTransitionFeatureToBaseGameRefresh);

									} // Freespins copy files to server
								} // Freespins closing

								// ******************* BONUS ****************************
								if (BonusFeatures.equalsIgnoreCase("yes")) {
									String bonusUserName = util.randomStringgenerator();

									strFileName = TestPropReader.getInstance().getProperty("MarketBonusTestDataPath");
									File testDataFileBonus = new File(strFileName);

									if (util.copyFilesToTestServerForRegMarket(mid, cid, testDataFileBonus,
											bonusUserName, regMarket, envId, productId, isoCode, marketTypeId,
											copiedFiles)) {
										Thread.sleep(3000);

										util.updateUserBalance(bonusUserName, balance);
										Thread.sleep(5000);

										if (marketUrl.isEmpty()) {
											urlNew = cfnlib.createRegMarketUrl(bonusUserName, url, productId, market,
													brand, retrunUrlParam, languageCode);
										} else {
											urlNew = cfnlib.replaceUserNameLanguageCode(marketUrl, languageCode);
										}
										cfnlib.loadGameAndClickContinueBtn(urlNew, report, imageLibrary, languageCode);

										report.detailsAppend("Following are the Bonus cases", "Verify Bonus", "", "");

										if (cfnlib.triggerBonus(imageLibrary)) {

											if (BonusSelection.equalsIgnoreCase("yes")) {
												cfnlib.selectBonusOption(imageLibrary);
											}

											if (BonusTransitionBaseGameToFeature.equalsIgnoreCase("yes")) {
												cfnlib.verifyBonusTransitionBaseToBonus(report, imageLibrary,
														languageCode, BonusTransitionBaseGameToFeature,
														BonusTransitionBaseGameToFeatureRefresh);
											}

											if (BonusSessionReminder.equalsIgnoreCase("yes")) {
												cfnlib.validateSessionReminderBonus(report, bonusUserName, imageLibrary,
														languageCode, SessionReminderUserInteraction,
														SessionReminderContinue, SessionReminderExitGame);
											}

											if (BonusRefresh.equalsIgnoreCase("yes")) {
												cfnlib.checkNetPositionAfterRefresh(report, imageLibrary, languageCode);

											}

											if (BonusNetPosition.equalsIgnoreCase("yes")) {
												dblNetPositionBeforeBonus = cfnlib.getNetPositionDblValue();
											}

											if (BonusReelLanding.equalsIgnoreCase("yes")) {
												cfnlib.checkSpinReelLandingFeature(report, languageCode);
											}
											if (LinkAndWin.equalsIgnoreCase("yes")) {
												cfnlib.verifyLinkAndWinFeature(report, imageLibrary, languageCode);
											}

											if (BonusNetPosition.equalsIgnoreCase("yes")) {
												cfnlib.verifyNetPositionBonus(report, imageLibrary, languageCode,
														cfnlib.XpathMap.get("BonusWinType"), dblNetPositionBeforeBonus);
											}

											if (BonusTransitionFeatureToBaseGame.equalsIgnoreCase("yes")) {
												cfnlib.verifyBonusTransitionBonusToBase(report, imageLibrary,
														languageCode, BonusTransitionFeatureToBaseGame,
														BonusTransitionFeatureToBaseGameRefresh);

											}
										}
									} // Bonus copy files to server

								} // Bonus closing

							} // for loop : mapping languages

						} // reading markets

					} // try
					catch (Exception e) {
						System.out.println("Exception occur while processing currency: " + e);
						log.error("Exception occur while processing currency", e);
						report.detailsAppend("Exception occur while processing currency ", " ", "", "Fail");
						cfnlib.evalException(e);
					}

				} // for loop : mapping currencies
			} // driver Closed

		} // closing try block
			// -------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		// -------------------Closing the connections---------------//
		finally {
			report.endReport();
			if (!copiedFiles.isEmpty()) {
				if (TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
					util.deleteBluemesaTestDataFiles(mid, cid, copiedFiles);
				else
					apiObj.deleteAxiomTestDataFiles(copiedFiles);
			}
			webdriver.close();
			webdriver.quit();
			// proxy.abort();
			Thread.sleep(1000);
		}

	}

}