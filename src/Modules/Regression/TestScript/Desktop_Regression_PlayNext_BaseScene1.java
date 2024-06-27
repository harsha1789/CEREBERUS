package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop_PlayNext;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 * This Script is for play.next
 * 
 * @author VC66297
 *
 */

public class Desktop_Regression_PlayNext_BaseScene1 {

	Logger log = Logger.getLogger(Desktop_Regression_PlayNext_BaseScene1.class.getName());
	public ScriptParameters scriptParameters;
	public String extentScreenShotPath = null;
	Properties OR = new Properties();
	public Map<String, String> XpathMap;

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
	

		Desktop_HTML_Report playNextReport = new Desktop_HTML_Report(webdriver, browserName, filePath, startTime,
				mstrTCName, mstrTCDesc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings,
				mintStepNo, status1, gameName);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory = new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib = desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,
				playNextReport, gameName);
		CommonUtil util = new CommonUtil();

		RestAPILibrary apiObj = new RestAPILibrary();
		List<String> copiedFiles = new ArrayList<>();
		int mid = Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int cid = Integer.parseInt(TestPropReader.getInstance().getProperty("CIDDesktop"));
	
		try {

			if(webdriver!=null)
			{	
				
				String strFileName = TestPropReader.getInstance().getProperty("CurrencyTestDataPath");
				File testDataFile = new File(strFileName);
				List<Map<String, String>> currencyList = util.readCurrList();// mapping
				for (Map<String, String> currencyMap : currencyList) 
				{
				    userName=util.randomStringgenerator();						 
					// Step 2: To get the languages in MAP and load the language specific url
					String currencyID = currencyMap.get(Constant.ID).trim();
					String isoCode = currencyMap.get(Constant.ISOCODE).trim();
					System.out.println(isoCode);
					String CurrencyName = currencyMap.get(Constant.ISONAME).trim();
					String languageCurrency = currencyMap.get(Constant.LANGUAGECURRENCY).trim();
					String regExpr = currencyMap.get(Constant.REGEXPRESSION).trim();
					String regExprNoSymbol = currencyMap.get(Constant.REGEXPRESSION_NOSYMBOL).trim();
					String url = cfnlib.XpathMap.get("ApplicationURL");

					if (util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles, currencyID, isoCode)) {
						playNextReport.detailsAppend("Create TestData to user ", "Create TestData to user ", "Created TestData", "PASS");
						System.out.println("Username : " + userName);System.out.println("CurrencyName: " + CurrencyName);
						playNextReport.detailsAppend("Create User ", "User Created", "User Created", "PASS");

						 if (util.migrateUser(userName)) { log.debug("User Migration : PASS");
						  System.out.println("User Migration : PASS");
						  playNextReport.detailsAppend("User Migrate ", "User Migrated", "User Migrated", "PASS");
						 
						  String balance = "700000000000"; Thread.sleep(60000);
						 
						  if (util.updateUserBalance(userName, balance)) {
						 log.debug("Able to update user balance");
						 System.out.println("Able to update user balance");
						playNextReport.detailsAppend("Update Balance ", "Balance Updated to user", "Balance Updated to user", "PASS");
						String launchURl = url.replaceFirst("\\busername=.*?(&|$)", "username=" + userName + "$1");
						log.info("url = " + launchURl);
						System.out.println(launchURl);
						
						if("Yes".equalsIgnoreCase(cfnlib.XpathMap.get("Clock")))
						{
							webdriver.manage().deleteAllCookies();
							cfnlib.loadGame(launchURl);
							Thread.sleep(3000);
						}
						else
						{
							webdriver.manage().deleteAllCookies();
							cfnlib.webdriver.navigate().to(launchURl);
							cfnlib.waitForPageToBeReady();
						}				
						Thread.sleep(10000);
						playNextReport.detailsAppend("Verify Game launchaed ", "Game should be launched", "Game is launched", "PASS");
						//resize browser
						cfnlib.resizeBrowser(1280, 960);
						Thread.sleep(10000);
						if(cfnlib.XpathMap.get("NFD").equalsIgnoreCase("Yes"))
						{
							if(cfnlib.XpathMap.get("NFDHasHook").equalsIgnoreCase("Yes"))
							{
								//check for visiblity of nfd button and take screenshot
								if(cfnlib.isElementVisible("isNFDButtonVisible"))
								{
									playNextReport.detailsAppend("Verify Continue button is visible ", "Continue buttion is visible", "Continue button is visible", "PASS");
								}
								else
								{
									playNextReport.detailsAppend("Verify Continue button is visible ", "Continue buttion is visible", "Continue button is not visible", "FAIL");
								}
							    //Click on nfd button
								Thread.sleep(5000);								
								cfnlib.ClickByCoordinates("return "+cfnlib.XpathMap.get("ClicktoContinueCoordinatex"),"return "+cfnlib.XpathMap.get("ClicktoContinueCoordinatey"));
								Thread.sleep(5000);
								if(cfnlib.isElementVisible("isSpinBtnVisible"))
								{
									playNextReport.detailsAppend("Verify Base Scene", "Base Scene is visible", "Base Scene is visible", "PASS");
								}
								else
								{
									playNextReport.detailsAppend("Verify Base Scene", "Base Scene is visible", "Base Scene is not visible", "FAIL");
								}
						
						//enter if loop only when bet button is visible on base game
					     }
						}

						Thread.sleep(5000);

						// Gets the Credit Amt && verifies Currency Format and check the currency format

						boolean credits = cfnlib.verifyRegularExpression(playNextReport, regExpr,
								cfnlib.getConsoleText("return " + cfnlib.XpathMap.get("Creditvalue")));

						if (credits) {
						
							playNextReport.detailsAppend("verify Base Game Credit Amount", "Credit Amount","Credit Amount", "PASS");
						} else {
							
							playNextReport.detailsAppend("verify Base Game Credit Amount", "Credit Amount","Credit Amount", "FAIL");
						}

						// Gets the Bet Amt && verifies Currency Format and check the currency format

						boolean betAmt = cfnlib.verifyRegularExpression(playNextReport, regExpr,
								cfnlib.getConsoleText("return " + cfnlib.XpathMap.get("BetTextValue")));

						if (betAmt) {
							
							playNextReport.detailsAppend("verify Base Game Bet Amount", "Bet Amount",	"Bet Amount", "PASS");
						} else {
							
							playNextReport.detailsAppend("verify Base Game Bet Amount", "Bet Amount","Bet Amount", "FAIL");
						}

					// method is used to Spin the Spin Buttton (Normal Win)
						try {
							if (cfnlib.isElementVisible("isSpinBtnVisible")) {
								cfnlib.details_append_folderOnlyScreeshot(webdriver, "SpinBtnWrk");
								Thread.sleep(2000);
								// click on spin button
								cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("SpinBtnCoordinatex"),"return " + cfnlib.XpathMap.get("SpinBtnCoordinatey"));
								Thread.sleep(9000);
								// check if spin is successful,take screenshot
								playNextReport.detailsAppend("verify BaseGame normal win","Spin Button is working", "Spin Button is working", "pass");
								// method is used to get the Win amt and check the currency format
								boolean winFormatVerification = cfnlib.verifyRegularExpression(playNextReport, regExpr,	cfnlib.getCurrentWinAmt(playNextReport, CurrencyName));
								if (winFormatVerification) {									
									playNextReport.detailsAppend("verify Base Game win amt", " Win Amt","Win Amt", "PASS");
								} else {									
									playNextReport.detailsAppend("verify Base Game win amt", " Win Amt","Win Amt", "FAIL");
								}
							} else {
								cfnlib.details_append_folderOnlyScreeshot(webdriver, "SpinBtnNtWrk");
								playNextReport.detailsAppend("verify BaseGame normal win","Spin Button is not working", "Spin Button is not working", "Fail" + CurrencyName);

							}
						} catch (Exception e) {
							log.error(e.getMessage(), e);
							cfnlib.evalException(e);
						}
						Thread.sleep(2000);

						// method is used to Spin the Spin Button (Big Win)
						try {
							if (cfnlib.isElementVisible("isSpinBtnVisible")) {
								Thread.sleep(3000);
								cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("SpinBtnCoordinatex"),"return " + cfnlib.XpathMap.get("SpinBtnCoordinatey"));
								Thread.sleep(9000);
								// Verifies the Big Win currency format
								boolean bigWinFormatVerification = cfnlib.verifyRegularExpression(playNextReport,regExpr, cfnlib.verifyBigWin(playNextReport, CurrencyName));
								if (bigWinFormatVerification) {
									
									playNextReport.detailsAppend("verify Base Game big win", "Big Win Amt",	"Big Win Amt", "PASS");
								} else {
									
									playNextReport.detailsAppend("verify Base Game big win", " Big Win Amt","Big Win Amt", "FAIL");
								}

							}
						} catch (Exception e) {
							log.error(e.getMessage(), e);
							cfnlib.evalException(e);
						}

						// method is used to Spin the Spin Buttton (Amazing Win)
						try {
							if (cfnlib.isElementVisible("isSpinBtnVisible")) {
								cfnlib.details_append_folderOnlyScreeshot(webdriver, "SpinBtnWrk");
								Thread.sleep(2000);
								// click on spin button
								cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("SpinBtnCoordinatex"),"return " + cfnlib.XpathMap.get("SpinBtnCoordinatey"));
								Thread.sleep(9000);
								// method is used to get the Win amt and check the currency format

								boolean winVerification = cfnlib.verifyRegularExpression(playNextReport, regExpr,cfnlib.verifyAmgWin(playNextReport, CurrencyName));
								if (winVerification) {
									
									playNextReport.detailsAppend("verify Base Game Amazing win amt", " Win Amt"," Win Amt", "PASS");
								} else {
								
									playNextReport.detailsAppend("verify Base Game Amazing win amt", " Win Amt"," Win Amt", "FAIL");
								}
								// check if spin is successful,take screenshot
								playNextReport.detailsAppend("verify BaseGame Amazing win",	"Spin Button is working", "Spin Button is working", "pass", CurrencyName);
							} else {
								cfnlib.details_append_folderOnlyScreeshot(webdriver, "SpinBtnNtWrk");
								playNextReport.detailsAppend("verify BaseGame Amazing win",	"Spin Button is not working", "Spin Button is not working", "Fail", CurrencyName);

							}
						} catch (Exception e) {
							log.error(e.getMessage(), e);
							cfnlib.evalException(e);
						}

					Thread.sleep(2000);
						try {
							// Lets check the Quick bets
							playNextReport.detailsAppend("Follwing are the Quick Bet value verification test case",	"Verify Quick Bet value", "", "");

							if (cfnlib.isElementVisible("IsBetButtonVisible")) {
								// click on bet menu
								Thread.sleep(2000);
								cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("BetMenuCoordinatex"),"return " + cfnlib.XpathMap.get("BetMenuCoordinatey"));
								Thread.sleep(2000);

								cfnlib.verifyquickbet(playNextReport, CurrencyName, regExpr);
							}
						} catch (Exception e) {
							log.error(e.getMessage(), e);
							cfnlib.evalException(e);
						}

						// method is used to scroll the pay-table and validate the pay-outs and check
						// the currency format
					
  					try {
							if (cfnlib.isElementVisible("isMenuBtnVisible")) {

								// click on menu
								Thread.sleep(2000);
								cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("MenuCoordinatex"),"return " + cfnlib.XpathMap.get("MenuCoordinatey"));
								Thread.sleep(2000);

								if (cfnlib.isElementVisible("isPaytableBtnVisible")) {
									cfnlib.details_append_folderOnlyScreeshot(webdriver, "PaytableBtnVisible");
									// click on menu
									Thread.sleep(2000);
									cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("PaytableCoordinatex"),"return " + cfnlib.XpathMap.get("PaytableCoordinatey"));
									Thread.sleep(2000);

										System.out.println("Paytable Open : PASS");
										log.debug("Paytable Open : PASS");
										boolean payouts = cfnlib.validatePayoutsFromPaytable(playNextReport,
												CurrencyName, regExpr);
										cfnlib.verifyGridPayouts(playNextReport,regExpr,CurrencyName);
										if (payouts) {
											playNextReport.detailsAppend("Base Game", "Pay Table ",	"PayOut Amount", "PASS");
										} else {
											playNextReport.detailsAppend("Base Game", "Pay Table ",	"PayOut Amount", "FAIL");
										}

										System.out.println("Paytable Closed : PASS");
										log.debug("Paytable Closed : PASS");
									
									// close paytable
									if (cfnlib.XpathMap.get("PaytableXpath").equalsIgnoreCase("Yes")) {
										Thread.sleep(2000);
										cfnlib.func_Click(cfnlib.XpathMap.get("PaytableClose"));
										Thread.sleep(2000);
										playNextReport.detailsAppend("Base Game", "Pay Table ",	"Pay Table Closed", "PASS");
									}

								} else {
									cfnlib.details_append_folderOnlyScreeshot(webdriver, "PaytableBtnNtVisible");

								}
							}
						} catch (Exception e) {
							log.error(e.getMessage(), e);
							cfnlib.evalException(e);
						}
					
					// method is used to check Progessive Bars
						try {
							if (cfnlib.isElementVisible("isProgressiveBar1")) {								
								Thread.sleep(2000);
								
								// check if spin is successful,take screenshot
								playNextReport.detailsAppend("verify Progressive Bar1",	" Progressive Bar1", " Progressive Bar1 visible", "pass");
								boolean grandValue = cfnlib.verifyRegularExpression(playNextReport, regExpr,
										cfnlib.getConsoleText("return " + cfnlib.XpathMap.get("ProgressiveBar1Value")));
								if (grandValue) {									
									playNextReport.detailsAppend("verify Progressive Bar1 Value", " Progressive Bar1 Value","Progressive Bar1 Value", "PASS");
								} else {								
									playNextReport.detailsAppend("verify Progressive Bar1 value", " Progressive Bar1 Value","Progressive Bar1 Value", "FAIL");
								}										
							} else {
							
								playNextReport.detailsAppend("verify Progressive Bar 1","Progressive Bar 1 ", "Progressive  Bar1 not visible", "Fail","" + CurrencyName);
							}							
							
							if (cfnlib.isElementVisible("isProgressiveBar2")) {								
								Thread.sleep(2000);
								
								// check if spin is successful,take screenshot
								playNextReport.detailsAppend("verify verify Progressive Bar 2 "," Progressive Bar2", " Progressive Bar2 is visible", "pass");
								boolean grandValue = cfnlib.verifyRegularExpression(playNextReport, regExpr,
										cfnlib.getConsoleText("return " + cfnlib.XpathMap.get("ProgressiveBar2Value")));
								if (grandValue) {									
									playNextReport.detailsAppend("verify Progressive Bar2 Value", " Progressive Bar2 value","Progressive Bar2 Value", "PASS");
								} else {									
									playNextReport.detailsAppend("verify Progressive Bar2 Value", " Progressive Bar2 Value","Progressive Bar2 Value", "FAIL");
								}		
								
							} else {
							
								playNextReport.detailsAppend("verify Progressive Bar2","Progressive Bar2", "Progressive Bar2 not visible", "Fail","" + CurrencyName);

							}
							
							if (cfnlib.isElementVisible("isProgressiveBar3")) {								
								Thread.sleep(2000);
								
								// check if spin is successful,take screenshot
								playNextReport.detailsAppend("verify Progressive Bar3",
										"Progressive Bar3", "Progressive Bar3 is visible", "pass");
								boolean grandValue = cfnlib.verifyRegularExpression(playNextReport, regExpr,
										cfnlib.getConsoleText("return " + cfnlib.XpathMap.get("ProgressiveBar3Value")));
								if (grandValue) {
								
									playNextReport.detailsAppend("verifyProgressive Bar3 Value", "verify Progressive Bar3 Value","verify Progressive Bar3 Value", "PASS");
								} else {								
									playNextReport.detailsAppend("verify verify Progressive Bar3 value", " verify Progressive Bar3 Value","verify Progressive Bar3 Value", "FAIL");
								}		
								
							} else {
							
								playNextReport.detailsAppend("verify verify Progressive Bar3","verify Progressive Bar3 ", "verify Progressive Bar3 not visible", "Fail","" + CurrencyName);

							}
						} catch (Exception e) {
							log.error(e.getMessage(), e);
							cfnlib.evalException(e);
						}
											
						Thread.sleep(2000);
						// click on spin button
						cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("SpinBtnCoordinatex"),"return " + cfnlib.XpathMap.get("SpinBtnCoordinatey"));
						Thread.sleep(20000);
						if (TestPropReader.getInstance().getProperty("IsFreeSpinAvailable").equalsIgnoreCase("yes")) {
						
								System.out.println("Free Spins Availablity : PASS");
								log.debug("Free Spins Availablity : PASS");
								playNextReport.detailsAppend("Free Spins", "Free Spins is Available","Free Spins is Available", "PASS");
								// Free spins
								// 1St Spins to get Win
								// method is used to get the current win and check the currency format
								
								boolean winFormatVerificationINFS = cfnlib.verifyRegularExpression(playNextReport,regExpr, cfnlib.verifyFSCurrentWinAmt(playNextReport, CurrencyName));
								if (winFormatVerificationINFS) {
									System.out.println("Free Games Win Value : PASS");
									log.debug("Free Games Win Value : PASS");
									playNextReport.detailsAppend("Free Spins", "Win Amt", "Win Amt", "PASS");
								} else {
									System.out.println("Free Games Win Value : FAIL");
									log.debug("Free Games Win Value : FAIL");
									playNextReport.detailsAppend("Free Spins", "Win Amt", "Win Amt", "FAIL");
								}
							String cred =cfnlib.func_GetText("FSBalanceText");
							System.out.println("Credit Value : "+cred);
								// method is used to get the current credit and check the currency format
								boolean bonusCredits = cfnlib.verifyRegularExpression(playNextReport, regExpr,
										cfnlib.func_GetText("FSBalanceText"));
								if (bonusCredits) {
									System.out.println("Free Spins Credit Value : PASS");
									log.debug("Free Spins Credit Value : PASS");
									playNextReport.detailsAppend("Free Spins", "Credit Amt", "Credit Amt", "PASS");
								} else {
									System.out.println("Free Spins Credit Value : FAIL");
									log.debug("Free Spins Credit Value : FAIL");
									playNextReport.detailsAppend("Free Spins", "Credit Amt", "Credit Amt", "FAIL");
								}
								
                              
                                
								// method is used to get the current big win and check the currency format
								boolean bigWinFormatVerificationINFS = cfnlib.verifyRegularExpression(playNextReport,regExpr, cfnlib.verifyFreeSpinBigWin(playNextReport, CurrencyName));
								if (bigWinFormatVerificationINFS) {
									System.out.println("Free Spins BigWin Value : PASS");
									log.debug("Free Spins BigWin Value : PASS");
									playNextReport.detailsAppend("Free Spins", "Big Win Amt", "Big Win Amt","PASS", CurrencyName);
								} else {
									System.out.println("Free Spins BigWin Value : FAIL");
									log.debug("Free Spins BigWin Value : FAIL");
									playNextReport.detailsAppend("Free Spins", "Big Win Amt", "Big Win Amt","FAIL", CurrencyName);
								}
								
								// method is used to get the current totalwin and check the currency format
								  String totalFs =cfnlib.func_GetText("totalWinInFS");
	                                System.out.println("Free Spins Total win value : PASS" +totalFs);
								boolean bonusBetAmt = cfnlib.verifyRegularExpression(playNextReport, regExpr,
										cfnlib.func_GetText("totalWinInFS"));
								if (bonusBetAmt) {
									System.out.println("Free Spins Win Value : PASS");
									log.debug("Free Spins Win Value : PASS");
									playNextReport.detailsAppend("Free Spins", "Total Win Amt", "Total Win Amt","PASS", CurrencyName);
								} else {
									System.out.println("Free Spins Win Value : FAIL");
									log.debug("Free Spins Win Value : FAIL");
									playNextReport.detailsAppend("Free Spins", "Total Win Amt", "Total Win Amt","FAIL", CurrencyName);
								}
								// method is used to verify free spins summary screen and check the currency
								// format
								Thread.sleep(40000);
								if (cfnlib.waitForElement("FSSummaryScreen")) {
									boolean fsSummaryScreen = cfnlib.verifyRegularExpression(playNextReport,
											regExprNoSymbol,cfnlib.func_GetText("FSSummaryScreenWinAmt"));
									if (fsSummaryScreen) {
										System.out.println("Free Spins Summary Screen : PASS");
										log.debug("Free Spins Summary Screen : PASS");
										playNextReport.detailsAppend("Free Spins", " Summary Screen"," Summary Screen", "PASS", CurrencyName);
									} else {
										System.out.println("Free Spins Summary Screen : FAIL");
										log.debug("Free Spins Summary Screen : FAIL");
										playNextReport.detailsAppend("Free Spins", " Summary Screen"," Summary Screen", "FAIL", CurrencyName);
									}
								} // Closing playnext Summary Screen
								// method is used to verify free spins summary screen clicked and Basegame opened					
								
								if (cfnlib.waitForElement("FSSummaryScreen")) {
									Thread.sleep(2000);
									// click on spin button
									cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("FSSummaryClickX"),"return " + cfnlib.XpathMap.get("FSSummaryClickY"));
									Thread.sleep(9000);
									if (cfnlib.isElementVisible("isSpinBtnVisible")) {
									
										playNextReport.detailsAppend("Verify FS summary screen click", "FS summary screen click"," FS summary screen clicked", "PASS", CurrencyName);
									} else {
										System.out.println("Free Spins Summary Screen : FAIL");
										log.debug("Free Spins Summary Screen : FAIL");
										playNextReport.detailsAppend("Verify FS summary screen click", "FS summary screen click"," FS summary screen not clicked","FAIL", CurrencyName);
									}
								} // Closing playnext Summary Screen
					     		} // Closing playnext
							
							 }
							 }
						}

					}
				}			

		} // closing try block

		// -------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error(e.getMessage(), e);

		}
		// -------------------Closing the connections---------------//
		finally {
			playNextReport.endReport();
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
		} // closing finally block
	}

}
