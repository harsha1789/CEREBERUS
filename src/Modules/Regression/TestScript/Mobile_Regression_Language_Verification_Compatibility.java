package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 * This Script traverse all the panels and take the screen shot of the all
 * screens such as paytable, bet settings, auto play ,Menu etc. This script
 * traverse and capture all the screen shots related to BigWin This script
 * traverse and capture all the screen shots related to free Spins. It reads the
 * test data excel sheet for configured languages.
 * 
 * @author TS64283
 */

public class Mobile_Regression_Language_Verification_Compatibility {

	Logger log = Logger.getLogger(Mobile_Regression_Language_Verification_Compatibility.class.getName());

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
		String userName = scriptParameters.getUserName();
		String framework = scriptParameters.getFramework();
		// String deviceLockKey=scriptParameters.getDeviceLockKey();
		String gameName = scriptParameters.getGameName();
		// int checkedOutDeviceNum=scriptParameters.getCheckedOutDeviceNum();
		String osPlatform = scriptParameters.getOsPlatform();
		String osVersion = scriptParameters.getOsVersion();

		String Status = null;
		int mintDetailCount = 0;
		// int mintSubStepNo=0;
		int mintPassed = 0;
		int mintFailed = 0;
		int mintWarnings = 0;
		int mintStepNo = 0;
		String urlNew = null;

		ExcelDataPoolManager excelpoolmanager = new ExcelDataPoolManager();
		Mobile_HTML_Report language = new Mobile_HTML_Report(webdriver, DeviceName, filePath, startTime, mstrTC_Name,
				mstrTC_Desc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings, mintStepNo, Status,
				gameName);
		log.info("Framework" + framework);
		MobileFunctionLibraryFactory mobileFunctionLibraryFactory = new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib = mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,
				language, gameName);
		WebDriverWait wait;
		CommonUtil util = new CommonUtil();
		int mid = Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int cid = Integer.parseInt(TestPropReader.getInstance().getProperty("CIDMobile"));
		List<String> copiedFiles = new ArrayList<>();
		RestAPILibrary apiobj = new RestAPILibrary();
		cfnlib.setOsPlatform(osPlatform);
		cfnlib.setOsVersion(osVersion);

		try {

			if (webdriver != null) {

				Map<String, String> rowData2 = null;
				Map<String, String> rowData3 = null;
				String url = cfnlib.xpathMap.get("ApplicationURL");
				String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)", "username=" + userName + "$1");
				wait = new WebDriverWait(webdriver, 120);
				System.out.println("url = " + LaunchURl);

				// If you want run whole script in LANDSCAPE mode then add Orientation =
				// LANDSCAPE in testdata file
				if ("LANDSCAPE".equalsIgnoreCase(cfnlib.xpathMap.get("Orientation"))) {
					String context = webdriver.getContext();
					webdriver.context("NATIVE_APP");
					webdriver.rotate(ScreenOrientation.LANDSCAPE);
					webdriver.context(context);
				}

				cfnlib.loadGame(LaunchURl);

				if (framework.equalsIgnoreCase("Force")) 
				{
					cfnlib.setNameSpace();
				}

				String testDataExcelPath = TestPropReader.getInstance().getProperty("TestData_Excel_Path");

				int rowCount2 = excelpoolmanager.rowCount(testDataExcelPath, "LanguageCodes");

				for (int j = 1; j < rowCount2; j++) {

					// Step 2: To get the languages in MAP and load the language specific url

					rowData2 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j);
					String languageDescription = rowData2.get("Language").toString();
					String languageCode = rowData2.get("Language Code").toString().trim();

					// If you want to run script for BaseScene then add in testdata sheet
					// BaseSceneChecks = YES
					if ("YES".equalsIgnoreCase(cfnlib.xpathMap.get("BaseSceneChecks"))) {

						log.debug("Total number of Languages configured for BaseSceneChecks " + rowCount2);

						String strFileName = TestPropReader.getInstance().getProperty("MobileBaseSceneTestDataPath");

						File testDataFile = new File(strFileName);

						userName = util.randomStringgenerator();

						if (util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles)) {
							
							log.debug("Test data is copy in test Server for Username=" + userName);

							System.out.println("Data set for BaseSceneChecks for this user  " + userName);

							String LaunchURlBasc = webdriver.getCurrentUrl();

							String LaunchURlBascChecks = LaunchURlBasc.replaceFirst("\\busername=.*?(&|$)",
									"username=" + userName + "$1");

							log.debug("url = " + LaunchURlBascChecks);

							cfnlib.loadGame(LaunchURlBascChecks);

							System.out.println("URL launch for BaseSceneChecks");

							cfnlib.threadSleep(5000);

							if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame"))
									|| (Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad")))) {
								if ("Yes".equalsIgnoreCase(cfnlib.xpathMap.get("CntBtnNoXpath"))) {
									cfnlib.elementWait("return " + cfnlib.xpathMap.get("CntBtnNoXpathSatus"), true);
									Thread.sleep(2000);
									cfnlib.funcFullScreen();
									Thread.sleep(2000);
									cfnlib.closeOverlay();
								} else {
									cfnlib.funcFullScreen();
									Thread.sleep(3000);
									cfnlib.newFeature();
									Thread.sleep(1000);
									cfnlib.funcFullScreen();
								}
							} else {
								cfnlib.funcFullScreen();
								Thread.sleep(1000);
								cfnlib.newFeature();
							}

							language.detailsAppend("Verify the Language code is " + languageCode + " ",
									" Application window should be displayed in " + languageDescription + "", "", "");

							cfnlib.splashScreen(language, languageCode);

							Thread.sleep(1000);

							language.detailsAppendFolder(
									" Verify that the application should display with Game Logo and game name ",
									"Game logo and game name should display", "Game logo and game name  displays",
									"Pass", languageCode);
							
							if ("LANDSCAPE".equalsIgnoreCase(cfnlib.xpathMap.get("Yes"))) {
								cfnlib.funcLandscape();
								Thread.sleep(1000);
								language.detailsAppendFolder(
										" Verify that the application should display with Game Logo and game name in Landscape Mode ",
										"Game logo and game name should display in Landscape Mode",
										"Game logo and game name  displays in Landscape Mode ", "Pass", languageCode);
								cfnlib.funcPortrait();
							}

							if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame"))
									|| (Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad")))) {

								if ("Yes".equalsIgnoreCase(cfnlib.xpathMap.get("CntBtnNoXpath"))) {
									cfnlib.elementWait("return " + cfnlib.xpathMap.get("CntBtnNoXpathSatus"), true);
									Thread.sleep(2000);
									cfnlib.funcFullScreen();
									Thread.sleep(2000);
									cfnlib.closeOverlay();
								} else {
									cfnlib.funcFullScreen();
									Thread.sleep(3000);
									cfnlib.newFeature();
									Thread.sleep(1000);
									cfnlib.funcFullScreen();
								}

							} else {

								cfnlib.funcFullScreen();
								Thread.sleep(1000);
								cfnlib.newFeature();

							}

							cfnlib.waitForSpinButton();

							log.debug("handled new feature screen after splash screen");

							language.detailsAppendFolder("Verify Game in Language " + languageCode + " ",
									" Basescene window should be displayed in " + languageDescription + "",
									"Basescene window should be displayed in " + languageDescription + "", "Pass",
									languageCode);
							if ("LANDSCAPE".equalsIgnoreCase(cfnlib.xpathMap.get("Yes"))) {

								cfnlib.funcLandscape();
								Thread.sleep(1000);
								language.detailsAppendFolder(
										"Verify Game in Language in Landscape" + languageCode + " ",
										" Basescene window should be displayed in " + languageDescription + "",
										"Basescene window should be displayed in " + languageDescription + "", "Pass",
										languageCode);
								cfnlib.funcPortrait();
							}

							// Capture Screen shot for Bet Screen

							boolean OpenTotalBet = cfnlib.openTotalBet();

							if (OpenTotalBet) {

								language.detailsAppendFolder("Verify that language on the Bet Settings Screen",
										"Bet Settings Screen should display",
										"Bet Settings Screen displays in " + languageDescription + " language", "Pass",
										languageCode);

								if ("LANDSCAPE".equalsIgnoreCase(cfnlib.xpathMap.get("Yes"))) {

									cfnlib.funcLandscape();
									Thread.sleep(1000);
									language.detailsAppendFolder(
											"Verify that language on the Bet Settings Screen in landscape mode",
											"Bet Settings Screen should display in landscape mode",
											"Bet Settings Screen displays in " + languageDescription + " language",
											"Pass", languageCode);
									cfnlib.funcPortrait();
								}
							} else {
								language.detailsAppendFolder("Verify that language on the Bet Settings Screen",
										"Bet Settings Screen should display", "Bet Settings Screen doesn't display",
										"FAIL", languageCode);
							}

							cfnlib.closeTotalBet();

							log.debug("Done for bet");

							// Capture Screen shot for Autoplay
							Thread.sleep(1000);
							boolean openAutoplay = cfnlib.open_Autoplay();
							if (openAutoplay) {

								language.detailsAppendFolder("Verify language on the Autoplay Screen",
										"Autoplay Screen should be display", "Autoplay Screen should be displayed",
										"Pass", languageCode);

								if ("LANDSCAPE".equalsIgnoreCase(cfnlib.xpathMap.get("Yes"))) {

									cfnlib.funcLandscape();
									Thread.sleep(1000);
									language.detailsAppendFolder(
											"Verify language on the Autoplay Screen in landscape mode",
											"Autoplay Screen should be display in landscape mode",
											"Autoplay Screen should be displayed", "Pass", languageCode);
									cfnlib.funcPortrait();

								}
							}

							else {
								language.detailsAppendFolder("Verify language on the Autoplay Screen",
										"Autoplay Screen should be display", "Autoplay Screen doesn't display", "Fail",
										languageCode);
							}

							cfnlib.close_Autoplay();
							log.debug("Done for Autoplay");

							Thread.sleep(1000);

							// Autoplay play

							cfnlib.autoPlay_with_QSUpdated(language);

							cfnlib.waitForSpinButton();

							// Capture Screen shot for Menu Screen
							Thread.sleep(2000);
							if (!framework.equalsIgnoreCase("CS_Renovate")) {
								boolean menuOpen = cfnlib.menuOpen();
								if (menuOpen) {
									language.detailsAppendFolder(
											"Verify that Language of menu link is " + languageDescription + " ",
											"Language of Menu Links should be " + languageDescription + "",
											"Menu Links are displaying in " + languageDescription + " language", "pass",
											languageCode);
									if ("LANDSCAPE".equalsIgnoreCase(cfnlib.xpathMap.get("Yes"))) {

										cfnlib.funcLandscape();
										Thread.sleep(1000);
										language.detailsAppendFolder(
												"Verify that Language of menu link is in Landscape mode "
														+ languageDescription + " ",
												"Language of Menu Links should be in display in landscape mode "
														+ languageDescription + "",
												"Menu Links are displaying in landscape mode" + languageDescription
														+ " language",
												"pass", languageCode);
										cfnlib.funcPortrait();

									}

								} else {
									language.detailsAppendFolder(
											"Verify that Language" + languageDescription
													+ "should be display properly on menu links",
											"Language inside Menu Links should be in " + languageDescription
													+ " language",
											"Language inside Menu Links is displaying", "fail", languageCode);
								}
								cfnlib.menuClose();
								log.debug("Done for Menu");
							}

							Thread.sleep(1000);
							// Need to uncomment for OneDesign Console
							if (!framework.equalsIgnoreCase("CS")
									&& cfnlib.checkAvilability(cfnlib.xpathMap.get("isMenuSettingsBtnVisible"))) {
								boolean openSetting = cfnlib.settingsOpen();
								if (openSetting) {
									language.detailsAppendFolder(
											"Verify that Language on settings screen is " + languageDescription + " ",
											"Language in Settigns Screen should be " + languageDescription + " ",
											"Language inside Settings screens is " + languageDescription + " ", "pass",
											languageCode);
									if ("LANDSCAPE".equalsIgnoreCase(cfnlib.xpathMap.get("Yes"))) {

										cfnlib.funcLandscape();
										Thread.sleep(2000);
										language.detailsAppendFolder(
												"Verify that Language on settings screen is displayed in landscape mode"
														+ languageDescription + " ",
												"Language in Settigns Screen should in belandscapr mode   "
														+ languageDescription + " ",
												"Language inside Settings screens is in landscape mode "
														+ languageDescription + " ",
												"pass", languageCode);

										cfnlib.funcPortrait();
									}

								} else {
									language.detailsAppendFolder(
											"Verify that Language back on menu is " + languageDescription
													+ "should be display properly on menu links",
											"Language inside Menu Links should be in " + languageDescription
													+ " language",
											"Language inside Menu Links is displaying", "fail", languageCode);
								}
								cfnlib.settingsBack();
								log.debug("Done for Setting");
							}

							// Open payatable and capture screen shots all
							Thread.sleep(2000);
							if (!gameName.contains("Scratch")
									&& cfnlib.checkAvilability(cfnlib.xpathMap.get("isMenuPaytableBtnVisible"))) {

								cfnlib.capturePaytableScreenshot(language, languageCode);
								if (!framework.equalsIgnoreCase("CS"))
									cfnlib.paytableClose();
								log.debug("Done for paytable");
							}

							Thread.sleep(2000);
							//// Open payatable and capture screen shots all in Landscape mode
							if (!gameName.contains("Scratch")
									&& cfnlib.checkAvilability(cfnlib.xpathMap.get("isMenuPaytableBtnVisible"))) {

								if ("LANDSCAPE".equalsIgnoreCase(cfnlib.xpathMap.get("Yes"))) {

									cfnlib.funcLandscape();
									Thread.sleep(2000);
									cfnlib.capturePaytableScreenshotWithLandscape(language, languageCode);
									Thread.sleep(1000);
									cfnlib.funcPortrait();

									if (!framework.equalsIgnoreCase("CS"))
										cfnlib.paytableClose();
									log.debug("Done for paytable");
								}
							}

							cfnlib.waitForSpinButton();

							if (TestPropReader.getInstance().getProperty("IsProgressiveGame").equalsIgnoreCase("Yes")) {
								if (TestPropReader.getInstance().getProperty("IsMotivators&Messages")
										.equalsIgnoreCase("Yes")) {
									String strNoOfMsgWithReelsSpinning = cfnlib.xpathMap
											.get("noOfMsgWithReelsSpinning");
									int noOfMsgWithReelsSpinning = (int) Double
											.parseDouble(strNoOfMsgWithReelsSpinning);

									String strNoOfMsgWithReelsResolved = cfnlib.xpathMap
											.get("noOfMsgWithReelsResolved");
									int noOfMsgWithReelsResolved = (int) Double
											.parseDouble(strNoOfMsgWithReelsResolved);

									String strNOfMotivators = cfnlib.xpathMap.get("noOfMotivators");
									int noOfMotivators = (int) Double.parseDouble(strNOfMotivators);

									for (int spinCount = 0; spinCount < noOfMsgWithReelsResolved; spinCount++) {
										cfnlib.spinclick();
										if (spinCount < noOfMsgWithReelsSpinning) {
											Thread.sleep(1000);
											language.detailsAppendFolder(
													"Verify Mesaage display below reels in language  "
															+ languageDescription + " ",
													"Mesaage should be in  " + languageDescription + " ",
													"Message is in  " + languageDescription + " ", "pass",
													languageCode);
										}
										cfnlib.waitForSpinButtonstop();
										Thread.sleep(1000);
										language.detailsAppendFolder(
												"Verify Mesaage display below reels in language  " + languageDescription
														+ " ",
												"Mesaage should be in  " + languageDescription + " ",
												"Message is in  " + languageDescription + " ", "pass", languageCode);

									}
								}

								cfnlib.verifyJackPotBonuswithScreenShots(language, languageCode);
							}

							// Incase of Respin games it takes the bet dialog screenshot on bet change

							if ("yes".equalsIgnoreCase(cfnlib.xpathMap.get("IsRespinFeature"))) {
								cfnlib.waitForSpinButton();

								Thread.sleep(1000);
								cfnlib.spinclick();
								cfnlib.waitForSpinButtonstop();
								Thread.sleep(5000);
								cfnlib.openTotalBet();
								cfnlib.setMaxBet();
								cfnlib.threadSleep(2000);
								cfnlib.closeTotalBet();
								language.detailsAppendFolderOnlyScreeshot(languageCode);
								cfnlib.clickOnReSpinOverlay();
								log.debug("Done for bet dialog");

							}

							// To open and capture Story in Game (Applicable for the game immortal romances)
							if (cfnlib.checkAvilability(cfnlib.xpathMap.get("isPaytableStoryExists"))) {
								cfnlib.Verifystoryoptioninpaytable(language, languageCode);
								cfnlib.paytableClose();

								Thread.sleep(2000);
								if ("LANDSCAPE".equalsIgnoreCase(cfnlib.xpathMap.get("Yes"))) {

									cfnlib.funcLandscape();
									Thread.sleep(2000);
									cfnlib.VerifystoryoptioninpaytableWithLandscape(language, languageCode);
									Thread.sleep(2000);
									cfnlib.funcPortrait();
								}

								log.debug("Done for story in paytable");
							}

						} else {
							log.debug(
									"Unable to copy FreeSpin test data file on the environment hence skipping execution");
							language.detailsAppend("", "",
									"Unable to copy FreeSpin test data on server,hence skipping futher execution",
									"fail");
						}
					}

					// Refreshing After Bascscene Checks
					webdriver.navigate().refresh();

					// If You want to run script for BigWin then add in testdata sheet BigwinChecks
					// = YES

					if ("YES".equalsIgnoreCase(cfnlib.xpathMap.get("BigwinChecks"))) {

						log.debug("Total number of Languages configured for Bigwin " + rowCount2);

						language.detailsAppend("Verify Different Languages on Bigwin feature",
								"Bigwin feature should display as per respective language", "", "");

						String strFileName = TestPropReader.getInstance().getProperty("MobileBigWinTestDataPath");

						File testDataFile = new File(strFileName);

						userName = util.randomStringgenerator();

						if (util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles)) {
							System.out.println("Data set for Bigwin for this user  " + userName);

							String launchURlBig = webdriver.getCurrentUrl();

							String launchURlBigWin = launchURlBig.replaceFirst("\\busername=.*?(&|$)",
									"username=" + userName + "$1");

							cfnlib.loadGame(launchURlBigWin);
							
							Thread.sleep(5000);

							System.out.println("URl :" + launchURlBigWin);

							if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame"))
									|| (Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad")))) {

								if ("Yes".equalsIgnoreCase(cfnlib.xpathMap.get("CntBtnNoXpath"))) {
									cfnlib.elementWait("return " + cfnlib.xpathMap.get("CntBtnNoXpathSatus"), true);
									Thread.sleep(2000);
									cfnlib.funcFullScreen();
									Thread.sleep(2000);
									cfnlib.closeOverlay();

								} else {
									cfnlib.funcFullScreen();
									cfnlib.newFeature();
									Thread.sleep(3000);
									cfnlib.funcFullScreen();
								}
							} else {

								cfnlib.funcFullScreen();
								Thread.sleep(1000);
								cfnlib.newFeature();

							}

							cfnlib.waitForSpinButton();
							// cfnlib.funcFullScreen();

							// setting maximum bet
							cfnlib.setMaxBet();
							// --------Bigwin test

							cfnlib.spinclick();
							cfnlib.waitForSpinButton();
							boolean result = cfnlib.waitForbigwin();
							if (result) {
								language.detailsAppendFolder("Verify that big win screen is displaying with overlay ",
										"Big win screen must display ,Once big win triggers ",
										"On  triggering big win,Big win screen is displaying", "pass", languageCode);
								if ("LANDSCAPE".equalsIgnoreCase(cfnlib.xpathMap.get("Yes"))) {

									cfnlib.funcLandscape();
									Thread.sleep(2000);
									language.detailsAppendFolder(
											"Verify that big win screen is displaying with overlay in landscape mode",
											"Big win screen must display in landscape mode ,Once big win triggers ",
											"On  triggering big win,Big win screen is displaying in landscape mode",
											"pass", languageCode);

									cfnlib.funcPortrait();
								}
								System.out.println("Bigwin screenshot captured");
								// set BigWinlayers = yes if their is bigwin,superwin, megawin layers are
								// present in the game to take 3 screenshots
								if ("Yes".equalsIgnoreCase(cfnlib.xpathMap.get("BigWinlayers"))) {
									for (int i = 0; i <= 2; i++) {
										System.out.println("wait for Bigwin");
										cfnlib.waitForbigwin();
										Thread.sleep(8000);
										System.out.println("after wait for Bigwin");
										// =========
										language.detailsAppendFolder(
												"Verify that big win screen is displaying with overlay ",
												"Big win screen must display ,Once big win triggers ",
												"On  triggering big win,Big win screen is displaying", "pass",
												languageCode);
										log.debug("Bigwinlayer captured" + i);
										System.out.println("Bigwinlayer captured" + i);
										;
									}
								}
								// To click on overlay
								cfnlib.closeOverlay();
								Thread.sleep(2000);
								language.detailsAppendFolder("Verify that big win screen countup is completed ",
										"Big win count up should be completed ", "Big win count up is completed",
										"pass", languageCode);
								if ("LANDSCAPE".equalsIgnoreCase(cfnlib.xpathMap.get("Yes"))) {

									cfnlib.funcLandscape();
									Thread.sleep(2000);
									language.detailsAppendFolder(
											"Verify that big win screen countup is completed in Landscape mode ",
											"Big win count up should be completed Landscape mode ",
											"Big win count up is completed Landscape mode ", "pass", languageCode);

									cfnlib.funcPortrait();
								}

							} else {
								language.detailsAppendFolder("Verify that big win screen is displaying with overlay ",
										"Big win screen must display ,Once big win triggers ",
										"On  triggering big win,Big win screen is not displaying", "fail",
										languageCode);

							}

						} else {

							log.debug(
									"Unable to copy BigWin test data file on the environment hence skipping execution");
							language.detailsAppend("", "",
									"Unable to copy BigWin test data on server,hence skipping futher execution",
									"fail");

						}
					}

					// If You want to run script for FreeSpin then add in testdata sheet
					// FreeSpinChecks = YES

					if ("YES".equalsIgnoreCase(cfnlib.xpathMap.get("FreeSpinChecks"))) {
						log.debug("Total number of Languages configured" + rowCount2);

						String strFileName = TestPropReader.getInstance().getProperty("MobileFreeSpinsTestDataPath");

						File testDataFile = new File(strFileName);

						userName = util.randomStringgenerator();

						if (util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles)) {

							log.debug("Test dat is copy in test Server for Username=" + userName);

							System.out.println("Data set for FreeSpinChecks for this user  " + userName);

							String LaunchURlFree = webdriver.getCurrentUrl();

							String LaunchURlFreeSpin = LaunchURlFree.replaceFirst("\\busername=.*?(&|$)",
									"username=" + userName + "$1");

							log.debug("url = " + LaunchURlFreeSpin);

							cfnlib.loadGame(LaunchURlFreeSpin);

							cfnlib.threadSleep(5000);

							if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame"))
									|| (Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad")))) {
								if ("Yes".equalsIgnoreCase(cfnlib.xpathMap.get("CntBtnNoXpath"))) {
									cfnlib.elementWait("return " + cfnlib.xpathMap.get("CntBtnNoXpathSatus"), true);
									Thread.sleep(2000);
									cfnlib.funcFullScreen();
									Thread.sleep(2000);
									cfnlib.closeOverlay();
								} else {
									cfnlib.funcFullScreen();
									Thread.sleep(3000);
									cfnlib.newFeature();
									Thread.sleep(1000);
									cfnlib.funcFullScreen();
								}
							} else {
								cfnlib.funcFullScreen();
								Thread.sleep(1000);
								cfnlib.newFeature();
							}

							cfnlib.waitForSpinButton();

							if (TestPropReader.getInstance().getProperty("IsProgressiveGame").equalsIgnoreCase("Yes")) {
								cfnlib.setMaxBet();
							}
							Thread.sleep(1000);

							cfnlib.spinclick();

							Thread.sleep(1000);

							// Jackpot Bonus Language check for Progresssive Games
							if (TestPropReader.getInstance().getProperty("IsProgressiveGame").equalsIgnoreCase("Yes")) {
								language.detailsAppend("Verify Different Languages on Jockpot feature ",
										"Jackpot feature should display as per respective language", "", "");

								// method to wait for jackpotscene

								cfnlib.jackpotSceneWait();

								Thread.sleep(8000);
								
								language.detailsAppendFolder(
										"Verify that the application should display jackpot scene in " + languageCode
												+ " ",
										"jackpot scene should display in " + languageDescription + " ",
										"jackpot scene displays in " + languageDescription + " ", "Pass", languageCode);
								Thread.sleep(5000);
								language.detailsAppendFolder(
										"Verify that the application should display jackpot scene in " + languageCode
												+ " ",
										"jackpot scene should display in " + languageDescription + " ",
										"jackpot scene displays in " + languageDescription + " ", "Pass", languageCode);
								Thread.sleep(5000);
								language.detailsAppendFolder(
										"Verify that the application should display jackpot scene in " + languageCode
												+ " ",
										"jackpot scene should display in " + languageDescription + " ",
										"jackpot scene displays in " + languageDescription + " ", "Pass", languageCode);

								if ("LANDSCAPE".equalsIgnoreCase(cfnlib.xpathMap.get("Yes"))) {

									cfnlib.funcLandscape();
									
									Thread.sleep(1000);
									language.detailsAppendFolder(
											"Verify that the application should display jackpot scene in Landscape"
													+ languageCode + " ",
											"jackpot scene should display in Landscape " + languageDescription + " ",
											"jackpot scene displays in Landscape " + languageDescription + " ", "Pass",
											languageCode);
									Thread.sleep(2000);
									language.detailsAppendFolder(
											"Verify that the application should display jackpot scene in landscape "
													+ languageCode + " ",
											"jackpot scene should display in landscape " + languageDescription + " ",
											"jackpot scene displays in landscape " + languageDescription + " ", "Pass",
											languageCode);
									Thread.sleep(2000);
									language.detailsAppendFolder(
											"Verify that the application should display jackpot scene in landscape "
													+ languageCode + " ",
											"jackpot scene should display in landscape " + languageDescription + " ",
											"jackpot scene displays in landscape " + languageDescription + " ", "Pass",
											languageCode);

									cfnlib.funcPortrait();
								}
								Thread.sleep(1000);

								cfnlib.clickAtButton("return " + cfnlib.xpathMap.get("ClickOnJackpotSpinButton"));

								

								Thread.sleep(5000);

								// method to wait for jackpotscene summary screen

								cfnlib.elementWait(
										"return " + cfnlib.xpathMap.get("isJackpotBonusContinueButtonVisible"), true);
								Thread.sleep(2000);

								language.detailsAppendFolder(
										"Verify that the application should display jackpot summary screen in "
												+ languageCode + " ",
										"Jakcpot summary screen should display in " + languageDescription + " ",
										"Jackpot summary screen displays in " + languageDescription + " ", "Pass",
										languageCode);
								if ("LANDSCAPE".equalsIgnoreCase(cfnlib.xpathMap.get("Yes"))) {

									cfnlib.funcLandscape();
									Thread.sleep(1000);
									language.detailsAppendFolder(
											"Verify that the application should display jackpot summary screen in Landscape "
													+ languageCode + " ",
											"Jakcpot summary screen should display in Landscape " + languageDescription
													+ " ",
											"Jackpot summary screen displays in Landscape " + languageDescription + " ",
											"Pass", languageCode);

									cfnlib.funcPortrait();
								}

								cfnlib.clickAtButton("return " + cfnlib.xpathMap.get("ClickOnJackpotSummaryContinue"));
								
								cfnlib.closeOverlay();

								Thread.sleep(3000);

							}

							webdriver.navigate().refresh();

							Thread.sleep(4000);

							if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame"))
									|| (Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad")))) {
								if ("Yes".equalsIgnoreCase(cfnlib.xpathMap.get("CntBtnNoXpath"))) {
									cfnlib.elementWait("return " + cfnlib.xpathMap.get("CntBtnNoXpathSatus"), true);
									Thread.sleep(2000);
									cfnlib.funcFullScreen();

								} else {
									cfnlib.funcFullScreen();
									wait.until(ExpectedConditions.visibilityOfElementLocated(
											By.xpath(cfnlib.xpathMap.get("OneDesign_NewFeature_ClickToContinue"))));
									cfnlib.newFeature();
									Thread.sleep(4000);
									cfnlib.funcFullScreen();

								}
							} else {
								wait.until(ExpectedConditions
										.visibilityOfElementLocated(By.xpath(cfnlib.xpathMap.get("clock"))));
								cfnlib.funcFullScreen();
								Thread.sleep(1000);
								cfnlib.newFeature();
							}

							// FreeSpinEntryScreen string should be yes in excel sheet, if entry screen is
							// present
							String FreeSpinEntryScreen = cfnlib.xpathMap.get("FreeSpinEntryScreen");
							String b2 = cfnlib.entryScreen_Wait(FreeSpinEntryScreen);
							if (b2.equalsIgnoreCase("freeSpin")) {
								language.detailsAppend("Verify Different Languages on Free spin entry screen",
										"Free spin entry screen should display as per respective language", "", "");
								
								cfnlib.entryScreen_Wait(FreeSpinEntryScreen);

								language.detailsAppendFolder(
										"Verify that the application should display Free spin entry screen in "
												+ languageCode + " ",
										"Free spin entry screen should display in " + languageDescription + " ",
										"Free spin entry screen displays in " + languageDescription + " ", "Pass",
										languageCode);

								if ("LANDSCAPE".equalsIgnoreCase(cfnlib.xpathMap.get("Yes"))) {

									cfnlib.funcLandscape();
									Thread.sleep(2000);
									language.detailsAppendFolder(
											"Verify that the application should display Free spin entry screen in Landscape "
													+ languageCode + " ",
											"Free spin entry screen should display in Landscape  " + languageDescription
													+ " ",
											"Free spin entry screen displays in Landscape  " + languageDescription
													+ " ",
											"Pass", languageCode);
									cfnlib.funcPortrait();
								}

								if ("yes".equals(cfnlib.xpathMap.get("isFreeSpinSelectionAvailable"))) {
									String strfreeSpinSelectionCount = cfnlib.xpathMap
											.get("NoOfFreeSpinBonusSelection");
									int freeSpinSelectionCount = (int) Double.parseDouble(strfreeSpinSelectionCount);

									/*
									 * if(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get(
									 * "TypeOfGame"))) {
									 * wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cfnlib.
									 * xpathMap.get("OneDesign_NewFeature_ClickToContinue")))); cfnlib.newFeature();
									 * cfnlib.funcFullScreen(); }
									 */
									cfnlib.clickBonusSelection(freeSpinSelectionCount);
								} else {

									if (TestPropReader.getInstance().getProperty("IsProgressiveGame")
											.equalsIgnoreCase("Yes")) {
										cfnlib.elementWait(
												"return " + cfnlib.xpathMap.get("isJackpotBonusContinueButtonVisible"),
												true);
										cfnlib.clickAtButton(
												"return " + cfnlib.xpathMap.get("ClickOnJackpotSummaryContinue"));
										Thread.sleep(6000);
										cfnlib.clickToContinue();
										cfnlib.FS_continue();
										Thread.sleep(4000);
									} else {
										// cfnlib.clickToContinue();
										// Click on freespins into continue button
										if ("yes".equals(cfnlib.xpathMap.get("isFreeSpinIntroContinueBtnAvailable"))) {
											cfnlib.clickToContinue();
										} else {
											System.out.println(
													"There is not Freespins Into Continue button in this game");
											log.debug("There is not Freespins Into Continue button in this game");
										}
									}
								}

							}

							else {
								System.out.println("Free Spin Entry screen is not present in Game");
							}

							Thread.sleep(2000);

							cfnlib.FSSceneLoading();

							String startFreeSpinButton = cfnlib.xpathMap.get("StartFreeSpinButton");
							if (startFreeSpinButton.equalsIgnoreCase("Yes")) {
								webdriver.navigate().refresh();
								cfnlib.FSSceneLoading();
								language.detailsAppend("Verify the Different Languages in Free Spin",
										"Free Spin window should display as per respective language", "", "");

								language.detailsAppendFolder(
										"Verify that the application should display Free Spin scene in " + languageCode
												+ " ",
										"Free Spin scene should display in " + languageDescription + " ",
										"Free Spin scene displays in " + languageDescription + " ", "Pass",
										languageCode);

								cfnlib.funcFullScreen();
								cfnlib.FS_Start();
							}
							// wait=new WebDriverWait(webdriver,60);
							Thread.sleep(2000);
							
							webdriver.navigate().refresh();

							Thread.sleep(4000);

							if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame"))
									|| (Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad")))) {
								if ("Yes".equalsIgnoreCase(cfnlib.xpathMap.get("CntBtnNoXpath"))) {
									cfnlib.elementWait("return " + cfnlib.xpathMap.get("CntBtnNoXpathSatus"), true);
									Thread.sleep(2000);
									cfnlib.funcFullScreen();
									Thread.sleep(2000);
									cfnlib.closeOverlay();
								} else {
									cfnlib.funcFullScreen();
									wait.until(ExpectedConditions.visibilityOfElementLocated(
											By.xpath(cfnlib.xpathMap.get("OneDesign_NewFeature_ClickToContinue"))));
									cfnlib.newFeature();
									Thread.sleep(3000);
									cfnlib.funcFullScreen();
								}
							} else {
								cfnlib.funcFullScreen();
								Thread.sleep(1000);
								cfnlib.newFeature();
							}

							cfnlib.FSSceneLoading();

							language.detailsAppend("Verify the Different Languages in Free Spin",
									"Free Spin window should display as per respective language", "", "");

							language.detailsAppendFolder(
									"Verify that the application should display Free Spin scene in " + languageCode
											+ " ",
									"Free Spin scene should display in " + languageDescription + " ",
									"Free Spin scene displays in " + languageDescription + " ", "Pass", languageCode);

							if ("LANDSCAPE".equalsIgnoreCase(cfnlib.xpathMap.get("Yes"))) {
								cfnlib.funcLandscape();
								Thread.sleep(1000);
								language.detailsAppendFolder(
										"Verify that the application should display Free Spin scene in LandScape "
												+ languageCode + " ",
										"Free Spin scene should display in LandScape " + languageDescription + " ",
										"Free Spin scene displays in LandScape " + languageDescription + " ", "Pass",
										languageCode);
								cfnlib.funcPortrait();
							}

							Thread.sleep(1000);
							log.info("Freespin continue button is about to click");
							cfnlib.FS_continue();
							log.info("Freespin continue button is clicked and awaiting for spin Button");
							language.detailsAppend("Verify the Different Languages in Free Spin Summary Screen",
									"Free Spin Summary Screen should display as per respective language", "", "");
							Thread.sleep(2000);
							cfnlib.waitSummaryScreen();
							language.detailsAppendFolder(
									"Verify that the application should display Free Spin Summary in " + languageCode
											+ " ",
									"Free Spin Summary should display in " + languageDescription + " ",
									"Free Spin Summary displays in " + languageDescription + " ", "Pass", languageCode);
							
							webdriver.navigate().refresh();
							
							Thread.sleep(3000);

							if ("LANDSCAPE".equalsIgnoreCase(cfnlib.xpathMap.get("Yes"))) {
								cfnlib.funcLandscape();
								Thread.sleep(2000);
								if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame"))
										|| (Constant.YES
												.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad")))) {
									if ("Yes".equalsIgnoreCase(cfnlib.xpathMap.get("CntBtnNoXpath"))) {
										cfnlib.elementWait("return " + cfnlib.xpathMap.get("CntBtnNoXpathSatus"), true);
										Thread.sleep(1000);
										cfnlib.funcFullScreen();

									} else {
										cfnlib.funcFullScreen();
										wait.until(ExpectedConditions.visibilityOfElementLocated(
												By.xpath(cfnlib.xpathMap.get("OneDesign_NewFeature_ClickToContinue"))));
										cfnlib.newFeature();
										Thread.sleep(3000);
										cfnlib.funcFullScreen();
									}
								} else {
									cfnlib.funcFullScreen();
									Thread.sleep(1000);
									cfnlib.newFeature();
								}

								cfnlib.funcFullScreen();
								Thread.sleep(500);
								language.detailsAppendFolder(
										"Verify that the application should display Free Spin Summary in "
												+ languageCode + " ",
										"Free Spin Summary should display in " + languageDescription + " ",
										"Free Spin Summary displays in " + languageDescription + " ", "Pass",
										languageCode);
								cfnlib.funcPortrait();
							}
						} else {
							log.debug(
									"Unable to copy FreeSpin test data file on the environment hence skipping execution");
							language.detailsAppend("", "",
									"Unable to copy FreeSpin test data on server,hence skipping futher execution",
									"fail");
						}
					}

					// Language Change logic:: for updating language in URL and then Refresh

					if (j + 1 != rowCount2) {
						rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j + 1);
						languageDescription = rowData3.get("Language").trim();
						String languageCode2 = rowData3.get("Language Code").trim();

						String currentUrl = webdriver.getCurrentUrl();
						if (currentUrl.contains("LanguageCode"))
							urlNew = currentUrl.replaceAll("LanguageCode=" + languageCode,
									"LanguageCode=" + languageCode2);
						else if (currentUrl.contains("languagecode"))
							urlNew = currentUrl.replaceAll("languagecode=" + languageCode,
									"languagecode=" + languageCode2);
						else if (currentUrl.contains("languageCode"))
							urlNew = currentUrl.replaceAll("languageCode=" + languageCode,
									"languageCode=" + languageCode2);

						log.info("Privious Language Code in Url= " + languageCode + "\nNext Language code= "
								+ languageCode2 + "\nNew Url after replacing language code:" + urlNew);
						cfnlib.loadGame(urlNew);
						String error = cfnlib.xpathMap.get("Error");

						if (cfnlib.isElementPresent(error)) {
							language.detailsAppendFolder("Verify the Language code is " + languageCode2 + " ",
									" Application window should be displayed in " + languageDescription + "", "", "",
									languageCode2);
							language.detailsAppendFolder("Verify that any error is coming",
									"General error should not display", "General Error is Diplay", "fail",
									languageCode2);

							if (j + 2 != rowCount2) {
								rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j + 2);
								String languageCode3 = rowData3.get("Language Code").toString().trim();
								currentUrl = webdriver.getCurrentUrl();
								if (currentUrl.contains("LanguageCode"))
									urlNew = currentUrl.replaceAll("LanguageCode=" + languageCode2,
											"LanguageCode=" + languageCode3);
								else if (currentUrl.contains("languagecode"))
									urlNew = currentUrl.replaceAll("languagecode=" + languageCode2,
											"languagecode=" + languageCode3);
								else if (currentUrl.contains("languageCode"))
									urlNew = currentUrl.replaceAll("languageCode=" + languageCode2,
											"languageCode=" + languageCode3);

								cfnlib.loadGame(urlNew);
							}

							j++;
						}
					}
				}
			}
		}

		// -------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (!copiedFiles.isEmpty()) {
				if (TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
					util.deleteBluemesaTestDataFiles(mid, cid, copiedFiles);
				else
					apiobj.deleteAxiomTestDataFiles(copiedFiles);
			}

			language.endReport();
			webdriver.close();
			webdriver.quit();
			// proxy.abort();
			Thread.sleep(3000);
		}
	}
}