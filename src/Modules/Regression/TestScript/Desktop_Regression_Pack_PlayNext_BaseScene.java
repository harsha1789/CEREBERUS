package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;

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
import com.zensar.automation.library.ImageLibrary;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop_PlayNext;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;

/**Game Name
 * =============================
 * BreakAwayDeluxe
 * BreakAway Lucky Wilds
 * 
 * 1.Base Game
 * ===========
 * 1.1.Credits 
 * 1.2.Bet
 * 1.3.Autoplay
 * 1.4.Bet Panel (Min , Max & All Bet Values)
 * 1.5.PayTable , Pay-Outs Validation & Branding Validation  
 * 1.6.Big Win
 * 1.7.Amazing Win 
 * 
 * 2.Free Spins
 * =============
 * 2.1.Credits
 * 2.2.Total Win
 * 2.3.Big Win -1st Spin
 * 2.4.Normal Win - 2nd Spin * 
 * 2.5.Summary Screen Total Win Validation
 * 
 *  * TestData 
 * ==============
 * 1.Base Game
 * 1.1 Big Win
 * 1.2 Normal Win
 * 1.3 Bonus(If Applicable)
 * 
 * 2.Free Spins
 * 2.1 Big Win - 1st Spin 
 * 2.2 Normal Win -2nd Spin
 * This Script is for play.next
 * 
 * @author VC66297
 * @modified PB6
 */

public class Desktop_Regression_Pack_PlayNext_BaseScene {

	Logger log = Logger.getLogger(Desktop_Regression_Pack_PlayNext_BaseScene.class.getName()); 
	public ScriptParameters scriptParameters;
	public void script() throws Exception
	{

		String mstrTCName=scriptParameters.getMstrTCName();
		String mstrTCDesc=scriptParameters.getMstrTCDesc();
		String mstrModuleName=scriptParameters.getMstrModuleName();
		WebDriver webdriver=scriptParameters.getDriver();
		BrowserMobProxyServer proxy=scriptParameters.getProxy();
		String startTime=scriptParameters.getStartTime();
		String filePath=scriptParameters.getFilePath();
		String userName=scriptParameters.getUserName();
		String browserName=scriptParameters.getBrowserName();
		String framework=scriptParameters.getFramework();
		String gameName=scriptParameters.getGameName();
		String status1=null;
		int mintDetailCount=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		String urlNew=null;
		int startindex=0;
		String strGameName =null;
		ImageLibrary imageLibrary;			
		
		Desktop_HTML_Report report = new Desktop_HTML_Report(webdriver,browserName,filePath,startTime,mstrTCName,mstrTCDesc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,status1,gameName);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,report, gameName);
		CommonUtil util = new CommonUtil();
		RestAPILibrary apiObj = new RestAPILibrary();					
		List<String> copiedFiles=new ArrayList<>();
		int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDDesktop"));
		
		try
		{
			// Step 1 
			if(webdriver!=null)
			{	
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
				imageLibrary = new ImageLibrary(webdriver, strGameName, "Desktop");
			//	String strFileName = TestPropReader.getInstance().getProperty("SanityTestDataPath");
				//File testDataFile = new File(strFileName);
				List<Map<String, String>> currencyList = util.readCurrList();// mapping
				for (Map<String, String> currencyMap : currencyList) 
				{
				try {									
					// Step 2: To get the languages in MAP and load the language specific url
					String isoCode = currencyMap.get(Constant.ISOCODE).trim();
					String currencyID = currencyMap.get(Constant.ID).trim();
					String currencyName = currencyMap.get(Constant.ISONAME).trim();
					String languageCurrency = currencyMap.get(Constant.LANGUAGECURRENCY).trim();
					String regExpr = currencyMap.get(Constant.REGEXPRESSION).trim();
					String url = cfnlib.XpathMap.get("ApplicationURL");						
					log.debug(this + " I am processing currency:  " + currencyName);
											
					report.detailsAppend("*** CURRENCY AND LANGUAGE ***", "Currency: "+currencyName+", Language: "+languageCurrency,"", "");
					
					report.detailsAppend("Sanity Suite Test cases in "+languageCurrency+" for "+currencyName+" ", "Sanity Suite Test cases", "", "");
					
					//userName = util.randomStringgenerator();
					userName = "harsha83";
					System.out.println(userName);
					
				//	if (util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles, currencyID,isoCode))
					//{
						Thread.sleep(3000);
						
						log.debug("Updating the balance");
						String balance="10000000";
											
						if(cfnlib.XpathMap.get("LVC").equalsIgnoreCase("Yes")) 
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
						if (launchURl.contains("LanguageCode"))
							urlNew = launchURl.replaceAll("LanguageCode=en", "LanguageCode=" + languageCurrency);
						else if (launchURl.contains("languagecode"))
							urlNew = launchURl.replaceAll("languagecode=en", "languagecode=" + languageCurrency);
						else if (launchURl.contains("languageCode"))
							urlNew = launchURl.replaceAll("languageCode=en", "languageCode=" + languageCurrency);

						log.info("url = " + urlNew);
						System.out.println(urlNew);
						
						if("Yes".equalsIgnoreCase(cfnlib.XpathMap.get("Clock")))
						{
							webdriver.manage().deleteAllCookies();
							cfnlib.loadGame(urlNew);
							Thread.sleep(3000);
						}
						else
						{
							webdriver.manage().deleteAllCookies();							
							cfnlib.webdriver.navigate().to(urlNew);
							cfnlib.waitForPageToBeReady();
						}							
						Thread.sleep(10000);
						report.detailsAppend("Verify Game launchaed ", "Game should be launched", "Game is launched", "PASS");
						//resize browser
						//cfnlib.resizeBrowser(1280, 960);
						//check for visibility of nfd button and take screenshot
						if("Yes".equalsIgnoreCase(cfnlib.XpathMap.get("NFD"))) {
							report.detailsAppend("Verify Game launchaed ", "Game should be launched", "Game is launched", "PASS");
							//resize browser
							//cfnlib.resizeBrowser(1280, 960);							
							//check for visibility of continue button and take screenshot
							if(imageLibrary.isImageAppears("NFDButton"))
							{
								System.out.println("NFD button is visible");log.debug("NFD button is visible");
								report.detailsAppendFolder("Verify Continue button is visible ", "Continue buttion is visible", "Continue button is visible", "PASS",languageCurrency);										
								Thread.sleep(10000);
								imageLibrary.click("NFDButton");
								imageLibrary.Canvasclick("NFDButton");
								Thread.sleep(20000);
						
							}
							else
							{
								System.out.println("NFD button is not visible");
								log.debug("NFD button is not visible");
								report.detailsAppendFolder("Verify Continue button is visible ", "Continue buttion is visible", "Continue button is not visible", "FAIL",languageCurrency);
							}
						}		
						
						if(imageLibrary.isImageAppears("Spin"))
						{
							System.out.println("Spin button is  visible");
							report.detailsAppendFolder("Verify Base Scene", "Base Scene is visible", "Base Scene is visible", "PASS",languageCurrency);
						}
						else
						{
							System.out.println("Spin button is not visible");
							report.detailsAppendFolder("Verify Base Scene", "Base Scene is visible", "Base Scene is not visible", "FAIL",languageCurrency);
						}									
						Thread.sleep(5000);

						// Gets the Credit Amount && verifies Currency Format and check the currency format
						report.detailsAppend("Following is the Credit value verification test case","Verify Credit value", "", "");	
						try {
								boolean credits = cfnlib.verifyRegularExpression(report, regExpr,
								cfnlib.getConsoleText("return " + cfnlib.XpathMap.get("Creditvalue")));

								if (credits) {
									System.out.println("Base Game Credit Value : Pass");
									log.debug("Base Game Credit Value : Pass");
									report.detailsAppendFolder("Verify the currency format in credit ","Credit should display in correct currency format " ,
									"Credit is displaying in correct currency format ", "Pass",languageCurrency);																
								} else {
									System.out.println("Base Game Credit Value : Fail");
									log.debug("Base Game Credit Value : Fail");
									report.detailsAppendFolder("Verify the currency format in credit ","Credit should display in correct currency format " ,
									"Credit is displaying in incorrect currency format ", "Fail",languageCurrency);
								}
						} catch (Exception e) {
							log.error(e.getMessage(), e);
							cfnlib.evalException(e);
					}
						
					// Gets the Bet Amount & verifies Currency Format and check the currency format
					
					report.detailsAppend("Following is the Bet value verification test case","Verify Bet value", "", "");
					try {
							boolean betAmt = cfnlib.verifyRegularExpression(report, regExpr,
							cfnlib.getConsoleText("return " + cfnlib.XpathMap.get("BetTextValue")));

							if (betAmt) {
							log.debug("Base Game Bet Value : Pass");
							report.detailsAppendFolder("Verify the currency format for Bet ","Bet should display in correct currency format " ,
									"Bet is displaying in correct currency format ", "Pass",languageCurrency);											
							} else {
								log.debug("Base Game Bet Value : Fail");
								report.detailsAppendFolder("Verify the currency format for Bet ","Bet should display in correct currency format " ,
									"Bet is displaying in incorrect currency format ", "Fail",languageCurrency);
							}
						} catch (Exception e) {
							log.error(e.getMessage(), e);
							cfnlib.evalException(e);
						}

							
					try {
							if (imageLibrary.isImageAppears("BetButton"))
							{
								imageLibrary.click("BetButton");
								System.out.println("Clicked on BetButton");
								Thread.sleep(5000);
								report.detailsAppend("Following are the Quick Bet value verification test case","Verify Quick Bet value", "", "");
								
								// Lets check the Quick bets
								cfnlib.verifyquickbetImg(report, languageCurrency, regExpr,imageLibrary);
							}
						} catch (Exception e) {
							log.error(e.getMessage(), e);
							cfnlib.evalException(e);
						}
					
						// method is used to scroll the pay-table and validate the pay-outs and check
						// the currency format
						report.detailsAppend("Following are the Paytable test cases", "Verify Paytable payouts", "", "");		
						try {
							if (imageLibrary.isImageAppears("Menu")) {
								// click on menu
								imageLibrary.click("Menu");
								Thread.sleep(2000);

								if (imageLibrary.isImageAppears("Paytable")) 
								{								
									// click on menu
									Thread.sleep(2000);
									imageLibrary.click("Paytable");
									Thread.sleep(2000);

									boolean scrollPaytable = cfnlib.paytableScroll(report, languageCurrency);
									if (scrollPaytable) {
										System.out.println("Paytable Open : PASS");
										log.debug("Paytable Open : PASS");
										boolean scatterAndWildPayouts = cfnlib.validatePayoutsFromPaytable(report,
												languageCurrency, regExpr);
										boolean symbolPayouts=cfnlib.verifyGridPayouts(report,regExpr,languageCurrency);
										if (scatterAndWildPayouts&&symbolPayouts) 
										{
											report.detailsAppendNoScreenshot(
													"Verify Paytable payout currency format for selected bet value with the game currency format",
													"Paytable payout verification with the game currency format ",
													"Paytbale payout verification  with the game currency format is done and is correct",
													"Pass");
											log.debug("Paytable currency format: PASS");
											
										} else {
											report.detailsAppendNoScreenshot(
													"Verify Paytable payout currency format for selected bet value with the game currency format",
													"Paytable payout verification with the game currency format ",
													"Paytable payout verification with the game currency format is done but failed coz some formats are not matched",
													"Fail");
											log.debug("Paytable currency format: FAIL");
										}
										
									} else {
										System.out.println("Paytable Open : FAIL");
										log.debug("Paytable Open : FAIL");
										report.detailsAppendFolder("Base Game", "PayOut Amount",
												"PayOut Amount", "FAIL",languageCurrency);
									}
									// close pay table
									if (cfnlib.XpathMap.get("PaytableXpath").equalsIgnoreCase("Yes")) {		
										cfnlib.func_Click(cfnlib.XpathMap.get("PaytableClose"));
										System.out.println("Paytable Closed : PASS");
										log.debug("Paytable Closed : PASS");
										Thread.sleep(2000);										
									}
								} 
							}
						} catch (Exception e) {
							log.error(e.getMessage(), e);
							cfnlib.evalException(e);
						}					
						
						
						// method is used to check Progressive Bars
						try 
						{
							if(cfnlib.XpathMap.get("isProgressiveBarAvailable").equalsIgnoreCase("Yes")) 
							{
								report.detailsAppend("Following are the Progressive bar test cases", "Verify Progressive bar", "", "");			
							if (cfnlib.isElementVisible("isProgressiveBar1")) {								
								Thread.sleep(2000);
								
								// check if spin is successful,take screenshot
								report.detailsAppendFolder("verify Progressive Bar1",
										" Progressive Bar1", " Progressive Bar1 visible", "PASS",languageCurrency);
								boolean grandValue = cfnlib.verifyRegularExpression(report, regExpr,
										cfnlib.getConsoleText("return " + cfnlib.XpathMap.get("ProgressiveBar1Value")));
								if (grandValue) {									
									report.detailsAppendFolder("verify Progressive Bar1 Value", " Progressive Bar1 Value",
											"Progressive Bar1 Value", "PASS",languageCurrency);
								} else {								
									report.detailsAppendFolder("verify Progressive Bar1 value", " Progressive Bar1 Value",
											"Progressive Bar1 Value", "FAIL",languageCurrency);
								}										
							} else {
							
								report.detailsAppendFolder("verify Progressive Bar 1",
										"Progressive Bar 1 ", "Progressive  Bar1 not visible", "Fail", languageCurrency);
							}							
							
							if (cfnlib.isElementVisible("isProgressiveBar2")) {								
								Thread.sleep(2000);
								
								// check if spin is successful,take screenshot
								report.detailsAppendFolder("verify verify Progressive Bar 2 ",
										" Progressive Bar2", " Progressive Bar2 is visible", "PASS",languageCurrency);
								boolean grandValue = cfnlib.verifyRegularExpression(report, regExpr,
										cfnlib.getConsoleText("return " + cfnlib.XpathMap.get("ProgressiveBar2Value")));
								if (grandValue) {									
									report.detailsAppendFolder("verify Progressive Bar2 Value", " Progressive Bar2 value",
											"Progressive Bar2 Value", "PASS",languageCurrency);
								} else {									
									report.detailsAppendFolder("verify Progressive Bar2 Value", " Progressive Bar2 Value",
											"Progressive Bar2 Value", "FAIL",languageCurrency);
								}		
								
							} else {
							
								report.detailsAppendFolder("verify Progressive Bar2","Progressive Bar2", "Progressive Bar2 not visible", "Fail",languageCurrency);

							}
							
							if (cfnlib.isElementVisible("isProgressiveBar3")) {								
								Thread.sleep(2000);
								
								// check if spin is successful,take screenshot
								report.detailsAppendFolder("verify Progressive Bar3",
										"Progressive Bar3", "Progressive Bar3 is visible", "PASS",languageCurrency);
								boolean grandValue = cfnlib.verifyRegularExpression(report, regExpr,
										cfnlib.getConsoleText("return " + cfnlib.XpathMap.get("ProgressiveBar3Value")));
								if (grandValue) {
								
									report.detailsAppendFolder("verifyProgressive Bar3 Value", "verify Progressive Bar3 Value",
											"verify Progressive Bar3 Value", "PASS",languageCurrency);
								} else {								
									report.detailsAppendFolder("verify verify Progressive Bar3 value", " verify Progressive Bar3 Value",
											"verify Progressive Bar3 Value", "FAIL",languageCurrency);
								}		
								
							} else {
							
								report.detailsAppendFolder("verify verify Progressive Bar3","verify Progressive Bar3 ", "verify Progressive Bar3 not visible", "Fail", languageCurrency);	
							}
						} 
						}catch (Exception e) 
						{
							log.error(e.getMessage(), e);
							cfnlib.evalException(e);
						}					
						Thread.sleep(2000);			
						report.detailsAppend("Following are the win test cases", "Verify win in base scene", "", "");
						
						// method is used to Spin the Spin Button (Normal Win)
						try {
							if (imageLibrary.isImageAppears("Spin")) {
							
								// click on spin button
								imageLibrary.click("Spin");
								Thread.sleep(5000);
								// check if spin is successful,take screenshot
								//report.detailsAppendFolder("verify BaseGame normal win","Spin Button is working", "Spin Button is working", "PASS",languageCurrency);
								// method is used to get the Win amount and check the currency format
								boolean winFormatVerification = cfnlib.verifyRegularExpression(report, regExpr,
										cfnlib.getCurrentWinAmt(report, languageCurrency));
								if (winFormatVerification) {
									System.out.println("Base Game Win Value : PASS");
									log.debug("Base Game Win Value : PASS");
									report.detailsAppendFolder("verify Base Game win amt", " Win Amt",
											"Win Amt", "PASS",languageCurrency);
								} else {
									System.out.println("Base Game Win Value : FAIL");
									log.debug("Base Game Win Value : FAIL");
									report.detailsAppendFolder("verify Base Game win amt", " Win Amt",
											"Win Amt", "FAIL",languageCurrency);
								}
							} else {
								
								report.detailsAppendFolder("verify BaseGame normal win",
										"Spin Button is not working", "Spin Button is not working", "Fail",
										"" + languageCurrency);
							}
						} catch (Exception e) {
							log.error(e.getMessage(), e);
							cfnlib.evalException(e);
						}
						Thread.sleep(3000);

						// method is used to Spin the Spin Button (Big Win)
						try {
							if (imageLibrary.isImageAppears("Spin")) {
								imageLibrary.click("Spin");
								Thread.sleep(5000);
								// Verifies the Big Win currency format
								boolean bigWinFormatVerification = cfnlib.verifyRegularExpression(report,
										regExpr, cfnlib.verifyBigWinImg(report, imageLibrary));
								if (bigWinFormatVerification) {
									System.out.println("Base Game BigWin Value : PASS");
									log.debug("Base Game BigWin Value : PASS");
									report.detailsAppendFolder("verify Base Game big win", "Big Win Amt",
											"Big Win Amt", "PASS",languageCurrency);
								} else {
									System.out.println("Base Game BigWin Value : FAIL");
									log.debug("Base Game BigWin Value : FAIL");
									report.detailsAppendFolder("verify Base Game big win", " Big Win Amt",
											"Big Win Amt", "FAIL",languageCurrency);
								}
							}
						} catch (Exception e) {
							log.error(e.getMessage(), e);
							cfnlib.evalException(e);
						}

						Thread.sleep(10000);
						
						// method is used to Spin the Spin Button (Amazing Win)
						try {
							if (imageLibrary.isImageAppears("Spin")) {
							
								// click on spin button
								imageLibrary.click("Spin");
								Thread.sleep(8000);
								// method is used to get the Win amt and check the currency format

								boolean winVerification = cfnlib.verifyRegularExpression(report, regExpr,
										cfnlib.verifyAmgWinImg(report, imageLibrary));
								if (winVerification) {
									System.out.println("Base Game Bonus win Value : PASS");
									log.debug("Base Game Win Value : PASS");
									report.detailsAppendFolder("verify Base Game Bonus win amt", "Bonus Win Amt",
											"Bonus Win Amt", "PASS",languageCurrency);
								} else {
									System.out.println("Base Game Bonus Win Value : FAIL");
									log.debug("Base Game Win Value : FAIL");
									report.detailsAppendFolder("verify Base Game Bonus win amt", " Win Amt",
											"Bonus Win Amt", "FAIL",languageCurrency);
								}
							
							} else {
							
										report.detailsAppendFolder("verify BaseGame Amazing win",
										"Spin Button is not working", "Spin Button is not working", "FAIL",
										languageCurrency);
							}
						} catch (Exception e) {
							log.error(e.getMessage(), e);
							cfnlib.evalException(e);
						}
						
						report.detailsAppend("Following are the Freespin test cases", "Verify Freespin scene", "", "");	
						
						if (TestPropReader.getInstance().getProperty("IsFreeSpinAvailable").equalsIgnoreCase("yes")) 
						{
							// click on spin button
							imageLibrary.click("Spin");
							Thread.sleep(20000);

							if(cfnlib.XpathMap.get("tapToContinueFreespinsAvailable").equalsIgnoreCase("Yes"))
							{
								//cfnlib.waitForElement("tapToContinueFreespins");
								if(cfnlib.XpathMap.get("isFSHaveMultipleOtions").equalsIgnoreCase("Yes"))
								{
									imageLibrary.click("tapToFSOption1");
									Thread.sleep(3000);
								}
								imageLibrary.click("tapToContinueFreespins");
							}
														
							Thread.sleep(10000);							
							// Free spins  1St Spins to get correct Win
							boolean winFormatVerificationINFS = cfnlib.verifyRegularExpression(report,regExpr, cfnlib.verifyFSCurrentWinAmt(report, languageCurrency));
							if (winFormatVerificationINFS) {
								System.out.println("Free Games Win Value : PASS");
								log.debug("Free Games Win Value : PASS");
								report.detailsAppendFolder("Free Spins", "Win Amt", "Win Amt", "PASS",languageCurrency);
							} else {
								System.out.println("Free Games Win Value : FAIL");
								log.debug("Free Games Win Value : FAIL");
								report.detailsAppendFolder("Free Spins", "Win Amt", "Win Amt", "FAIL",languageCurrency);
							}					
							//Thread.sleep(10000);
							// method is used to get the current big win and check the currency format
							boolean bigWinFormatVerificationINFS = cfnlib.verifyRegularExpression(report,regExpr, cfnlib.verifyFreeSpinBigWin(report, languageCurrency));
							if (bigWinFormatVerificationINFS) {
								System.out.println("Free Spins BigWin Value : PASS");
								log.debug("Free Spins BigWin Value : PASS");
								report.detailsAppendFolder("Free Spins", "Big Win Amt", "Big Win Amt","PASS",languageCurrency);
							} else {
								System.out.println("Free Spins BigWin Value : FAIL");
								log.debug("Free Spins BigWin Value : FAIL");
								report.detailsAppendFolder("Free Spins", "Big Win Amt", "Big Win Amt","FAIL",languageCurrency);
							}
							
							Thread.sleep(15000);
							// method is used to get the current Amazing win and check the currency format
							  String bonus =cfnlib.func_GetText("FSBonusWin");
                              System.out.println("Free Spins bonus win value : PASS" +bonus);
							boolean amzWinFormatVerificationINFS = cfnlib.verifyRegularExpression(report,regExpr, cfnlib.func_GetText("FSBonusWin"));
							if (amzWinFormatVerificationINFS) {
								System.out.println("Free Spins Amazing Value : PASS");
								log.debug("Free Spins Amazing Value : PASS");
								report.detailsAppendFolder("Free Spins", "Amazing Win Amt", "Amazing Win Amt","PASS",languageCurrency);
							} else {
								System.out.println("Free Spins BigWin Value : FAIL");
								log.debug("Free Spins BigWin Value : FAIL");
								report.detailsAppendFolder("Free Spins", "Amazing Win Amt", "Amazing Win Amt","FAIL",languageCurrency);
							}
							
							String cred =cfnlib.func_GetText("FSBalanceText");
							System.out.println("Credit Value : "+cred);
								// method is used to get the current credit and check the currency format
								boolean bonusCredits = cfnlib.verifyRegularExpression(report, regExpr,
										cfnlib.func_GetText("FSBalanceText"));
								if (bonusCredits) {
									
									report.detailsAppendFolder("Free Spins", "Credit Amt", "Credit Amt", "PASS",languageCurrency);
								} else {
									
									report.detailsAppendFolder("Free Spins", "Credit Amt", "Credit Amt", "FAIL",languageCurrency);
								}
							
							// method is used to get the current total win and check the currency format
							String totalFs =cfnlib.func_GetText("totalWinInFS");
                            System.out.println("Free Spins Total win value : PASS" +totalFs);
							boolean bonusBetAmt = cfnlib.verifyRegularExpression(report, regExpr,
									cfnlib.func_GetText("totalWinInFS"));
							if (bonusBetAmt) {
								System.out.println("Free Spins total Win Value : PASS");
								log.debug("Free Spins Win Value : PASS");
								report.detailsAppendFolder("Free Spins", "Total Win Amt", "Total Win Amt","PASS",languageCurrency);
							} else {
								System.out.println("Free Spins Win Value : FAIL");
								log.debug("Free Spins Win Value : FAIL");
								report.detailsAppendFolder("Free Spins", "Total Win Amt", "Total Win Amt","FAIL",languageCurrency);
							}
							// method is used to verify free spins summary screen and check the currency
							// format
							Thread.sleep(30000);							
							if (cfnlib.waitForElement("FSSummaryScreen")) 
							{
								Thread.sleep(8000);
								boolean fsSummaryScreen = cfnlib.verifyRegularExpression(report,
										regExpr,cfnlib.func_GetText("FSSummaryScreenWinAmt"));
								if (fsSummaryScreen) {
									System.out.println("Free Spins Summary Screen : PASS");
									log.debug("Free Spins Summary Screen : PASS");
									report.detailsAppendFolder("Free Spins", " Summary Screen"," Summary Screen", "PASS",languageCurrency);
								} else {
									System.out.println("Free Spins Summary Screen : FAIL");
									log.debug("Free Spins Summary Screen : FAIL");
									report.detailsAppendFolder("Free Spins", " Summary Screen"," Summary Screen", "FAIL",languageCurrency);
								}
							} // Closing play next Summary Screen
							// method is used to verify free spins summary screen clicked and Base game opened					
							 
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
						}
															
				}//test data
				// else
				//	{
				//		log.debug("Unable to Copy testdata");
					//	report.detailsAppendFolder("unable to copy test dat to server ", " ", "", "Fail", languageCurrency);

					//} 
				//}
				catch ( Exception e) 
				{
					log.error("Exception occur while processing currency",e);
					report.detailsAppend("Exception occur while processing currency ", " ", "", "Fail");
					cfnlib.evalException(e);
				}
			}	
		}
	}
										
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
		} // closing finally block
	}

}
