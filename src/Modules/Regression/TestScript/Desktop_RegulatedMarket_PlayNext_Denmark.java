package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
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

/**
 * @author SP68146
 * Quick Spin
 * Spin / Stop
 * Spin : Reel landing
 * Game Help navigation
 * Game Player Protection
 * Game clock and game name 
 * Currency Format
 * Session Reminder
 * 
 * 
 * 
 * * TestData 
 * =============
 * 1.Base Game
 * 1.1 Big Win
 * 1.2 Normal Win
 * 1.3 Bonus(If Applicable)
 * 2.Free Spins
 * 
 * 
 */
public class Desktop_RegulatedMarket_PlayNext_Denmark {

	Logger log = Logger.getLogger(Desktop_RegulatedMarket_PlayNext_Denmark.class.getName());
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
		String languageDescription = null;
		String languageCode = null;
		ImageLibrary imageLibrary;
		int envId = 0;

		ExcelDataPoolManager excelpoolmanager = new ExcelDataPoolManager();
		Desktop_HTML_Report DenmarkReport = new Desktop_HTML_Report(webdriver, browserName, filePath, startTime, mstrTCName,
				mstrTCDesc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings, mintStepNo, status1,
				gameName);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory = new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib = desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,
				DenmarkReport, gameName);
		CommonUtil util = new CommonUtil();
		RestAPILibrary apiObj = new RestAPILibrary();
		List<String> copiedFiles = new ArrayList<>();
		int mid = Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int cid = Integer.parseInt(TestPropReader.getInstance().getProperty("CIDDesktop"));
		webdriver.manage().window().maximize();
		try {
			// Step 1
			if (webdriver != null) 
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
				
				
				Map<String, String> rowData2 = null;

				String testDataExcelPath = TestPropReader.getInstance().getProperty("TestData_Excel_Path");

				String strFileName = TestPropReader.getInstance().getProperty("SanityTestDataPath");
				File testDataFile = new File(strFileName);

				List<Map<String, String>> marketList = util.readMarketList();// mapping
				for (Map<String, String> marketMap : marketList) {
					try {
						// Step 2: To get the market specific values
						String regMarket = marketMap.get("RegMarketName").trim();
								
						if (regMarket.equalsIgnoreCase("Denmark")) 
						{
							String productID = marketMap.get("ProductId").trim().replace(".0", "");
							int productId = Integer.parseInt(productID);

							String marketTypeID = marketMap.get("MarketTypeId").trim().replace(".0", "");
							int marketTypeId = Integer.parseInt(marketTypeID);
							
							String isoCode = marketMap.get("CurrencyIsoCode").trim();
							String balance = marketMap.get("Balance").trim();
							String regExpr = marketMap.get("RegExpression").trim();
							String market = marketMap.get("market").trim();
							String brand = marketMap.get("brand").trim();
							String retrunUrlParam = marketMap.get("retrunUrlParam").trim();
							String marketUrl = marketMap.get("marketUrl").trim();

							List<Map> list = util.readLangList();
							int rowCount2 = excelpoolmanager.rowCount(testDataExcelPath, Constant.LANG_XL_SHEET_NAME);
							for (int j = 1; j < rowCount2; j++) {
								// Step 2: To get the languages in MAP and load the language specific url
								rowData2 = list.get(j);
								languageDescription = rowData2.get(Constant.LANGUAGE).trim();
								languageCode = rowData2.get(Constant.LANG_CODE).trim();
							
								log.debug(this + "*** I am processing Regulated market: " + regMarket+" ***");
								System.out.println("*** I am processing Regulated market: " + regMarket+" ***");

								userName = util.randomStringgenerator();
							//	userName ="userDenmark";
								System.out.println(userName);log.debug(userName);
								String url = cfnlib.XpathMap.get("ApplicationURL");
								if (util.copyFilesToTestServerForRegMarket(mid, cid, testDataFile, userName, regMarket,envId, productId, isoCode, marketTypeId, copiedFiles)) 
								{
									Thread.sleep(3000);									
									log.debug("Updating the balance");									
									util.updateUserBalance(userName,balance);
									Thread.sleep(5000);	
									util.setSessionReminderOnAxiom(userName, "180");									
									String launchURl = url.replaceFirst("\\busername=.*?(&|$)", "username=" + userName + "$1");
									launchURl = launchURl.replaceAll("productId=5007", "productId=" + productId);									
									launchURl = launchURl.replaceAll("market=dotcom", "market=" + market);
									launchURl = launchURl.replaceAll("brand=islandparadise", "brand=" + brand);
									launchURl = launchURl.replaceFirst("IslandParadise", retrunUrlParam);
									if (launchURl.contains("LanguageCode"))
										urlNew = launchURl.replaceAll("LanguageCode=en","LanguageCode=" + languageCode);
									else if (launchURl.contains("languagecode"))
										urlNew = launchURl.replaceAll("languagecode=en","languagecode=" + languageCode);
									else if (launchURl.contains("languageCode"))
										urlNew = launchURl.replaceAll("languageCode=en","languageCode=" + languageCode);
									log.debug(this + " I am processing currency:  " + isoCode);								
									DenmarkReport.detailsAppend("*** CURRENCY AND LANGUAGE ***", "Currency: "+isoCode+", Language: "+languageCode,"", "");
									
									//balance update
									webdriver.navigate().to(urlNew);
									Thread.sleep(10000);
									log.info("url = " + urlNew);
									System.out.println(urlNew);

									DenmarkReport.detailsAppendFolder("Verify Game should be launched", "Game is launched","Game is launched", "Pass",languageCode);
									
									if("Yes".equalsIgnoreCase(cfnlib.XpathMap.get("NFD"))) {
										if(imageLibrary.isImageAppears("NFDButton"))
										{
											System.out.println("NFD button is visible");log.debug("NFD button is visible");
											DenmarkReport.detailsAppendFolder("Verify Continue button is visible ", "Continue buttion is visible", "Continue button is visible", "Pass",languageCode);										
											Thread.sleep(2000);
											imageLibrary.click("NFDButton");
								
										}
										else
										{
											System.out.println("NFD button is not visible");log.debug("NFD button is not visible");
											DenmarkReport.detailsAppendFolder("Verify Continue button is visible ", "Continue buttion is visible", "Continue button is not visible", "Fail",languageCode);
										}
									}	
						
				//***********************************************************************************************************************
																	
									DenmarkReport.detailsAppend("Following are the GameName test cases", "Verify GameName", "", "");
									cfnlib.gameNameOnTopBar(DenmarkReport);																	
									if(imageLibrary.isImageAppears("Menu"))
									{
										imageLibrary.click("Menu");
										Thread.sleep(2000);	
										DenmarkReport.detailsAppendFolder("Menu Open ", "Menu should be open", "Menu is Opened", "Pass",languageCode);
										if(imageLibrary.isImageAppears("Paytable"))
										{
											//click on PayTable
											imageLibrary.click("Paytable");
											Thread.sleep(2000);																
											if(cfnlib.elementVisible_Xpath(cfnlib.XpathMap.get("PaytableClose")))
											{
												DenmarkReport.detailsAppendFolder("PayTable Open ", "PayTable should be opened", "PayTable is opened", "Pass",languageCode);
												Thread.sleep(2000);
												cfnlib.gameNameOnTopBar(DenmarkReport);																						
												cfnlib.func_Click(cfnlib.XpathMap.get("PaytableClose"));
												Thread.sleep(2000);
											}
										}
									}								
									try {

										// click on menu if settings are under menu
										if ("Yes".equalsIgnoreCase(cfnlib.XpathMap.get("settingsUnderMenu"))) {
											if (imageLibrary.isImageAppears("Menu")) {

												imageLibrary.click("Menu");
												Thread.sleep(2000);
											}
										}
										if (imageLibrary.isImageAppears("Settings")) {
											// click on menu
											imageLibrary.click("Settings");
											Thread.sleep(2000);
											DenmarkReport.detailsAppendFolder("Settings Open ", "Settings should be open", "Settings is Opened", "Pass",languageCode);
											cfnlib.gameNameOnTopBar(DenmarkReport);
											imageLibrary.click("MenuClose");
										}

									} catch (Exception e) {
										e.getMessage();
									}
						
				//*****************************************Normal Win******************************************************************************
									
									DenmarkReport.detailsAppend("Following are the Game test cases", "Verify Game testcases", "", "");
									if(imageLibrary.isImageAppears("Spin"))
									{
										imageLibrary.click("Spin");
										imageLibrary.click("Menu");								
										imageLibrary.click("MenuClose");
										if((imageLibrary.isImageAppears("SpinDisabled")))
										{
											DenmarkReport.detailsAppendFolder("Verify reels do not stop spinning when menu is opened ", "Reels should not stop spinning", "Reels are spinning", "Pass",languageCode);
																						
										}
										else
										{
											DenmarkReport.detailsAppendFolder("Verify reels do not stop spinning when menu is opened ", "Reels should not stop spinning", "Reels are not spinning", "Fail",languageCode);
											
										}										
									}
									
				//*****************************************Big Win******************************************************************************
								
									if(imageLibrary.isImageAppears("Spin"))
									{
										imageLibrary.click("Spin");
										
										if((imageLibrary.isImageAppears("SpinDisabled")))
										{
											DenmarkReport.detailsAppendFolder("Verify Spin button is disabled when reels are spinning", "Spin button should be disabled", "Spin button is disabled", "Pass",languageCode);
																						
										}
										else
										{
											DenmarkReport.detailsAppendFolder("Verify Spin button is disabled when reels are spinning", "Spin button should be disabled", "Spin button is not disabled", "Fail",languageCode);
											
										}										
									}
																											
				//****************************************Bonus*******************************************************************************
													
									if(imageLibrary.isImageAppears("Spin"))
									{
										imageLibrary.click("Spin");
										Thread.sleep(1000);
										if((imageLibrary.isImageAppears("SpinDisabled")))
										{
											DenmarkReport.detailsAppendFolder("Verify Spin button is disabled when reels are spinning", "Spin button should be disabled", "Spin button is disabled", "Pass",languageCode);
																						
										}
										else
										{
											DenmarkReport.detailsAppendFolder("Verify Spin button is disabled when reels are spinning", "Spin button should be disabled", "Spin button is not disabled", "Fail",languageCode);
											
										}										
									}																				
							
				//***********************************Session Reminder scenarios *****************************************************				
									DenmarkReport.detailsAppend("Following are the Session Reminder test cases", "Verify Session Reminder", "", "");
									
									if(cfnlib.waitUntilSessionReminder()) {
										
										DenmarkReport.detailsAppendFolder("Verify Session Reminder popup ", "Session Reminder popup should be displayed", "Session Reminder popup is displayed", "Pass",languageCode);
										
				//************************************Session Reminder Wins and Losses************************************************
										if(cfnlib.elementVisible_Xpath(cfnlib.XpathMap.get("SessionReminderWins"))&&cfnlib.elementVisible_Xpath(cfnlib.XpathMap.get("SessionReminderLosses"))) {
											DenmarkReport.detailsAppendFolder("Verify Session Reminder Wins and Losses ", "Session Reminder Wins and Losses", "Session Reminder Wins and Losses are displayed", "Pass",languageCode);
										}
										else {
											DenmarkReport.detailsAppendFolder("Verify Session Reminder Wins and Losses ", "Session Reminder Wins and Losses", "Session Reminder Wins and Losses are not displayed", "fail",languageCode);
										}
										
				//************************************	Session Duration *************************************************************				
										if(cfnlib.elementVisible_Xpath(cfnlib.XpathMap.get("SessionReminderDuration"))) {
											DenmarkReport.detailsAppendFolder("Verify Session Reminder Duration ", "Session Reminder Duration", "Session Reminder Duration is displayed", "Pass",languageCode);
										}
										else {
											DenmarkReport.detailsAppendFolder("Verify Session Reminder Duration ", "Session Reminder Duration", "Session Reminder Duration is not displayed", "Fail",languageCode);
										}
										
				//***********************************Ensure dismiss the session reminder and get back to game *********************************************
										cfnlib.func_Click(cfnlib.XpathMap.get("SessionReminderContinueBTN"));
										if(imageLibrary.isImageAppears("Spin")) {
											DenmarkReport.detailsAppendFolder("Ensure dismiss the session reminder and get back to game ", "BaseScene should be displayed", "BaseScene is displayed and exited from session reminder", "Pass",languageCode);
										}
										else {
											DenmarkReport.detailsAppendFolder("Ensure dismiss the session reminder and get back to game ", "BaseScene should be displayed", "session reminder pop up is not exited", "fail",languageCode);
											
										}
																				
				//************************************ Set Session for 1 hr************************************************************
										util.setSessionReminderOnAxiom(userName, "3600");
										webdriver.navigate().to(urlNew);
										Thread.sleep(10000);
										log.info("url = " + urlNew);
										System.out.println(urlNew);
										
										if("Yes".equalsIgnoreCase(cfnlib.XpathMap.get("NFD"))) {
										if(imageLibrary.isImageAppears("NFDButton"))
										{
											System.out.println("NFD button is visible");log.debug("NFD button is visible");										
											Thread.sleep(2000);
											imageLibrary.click("NFDButton");
									
										}
										else
										{
											System.out.println("NFD button is not visible");log.debug("NFD button is not visible");
										//	DenmarkReport.detailsAppendFolder("Verify Continue button is visible ", "Continue buttion is visible", "Continue button is not visible", "Fail",languageCode);
										}
									}
								}
								else {
									DenmarkReport.detailsAppendFolder("Verify Session Reminder popup ", "Session Reminder popup should be displayed", "Session Reminder popup is not displayed", "fail",languageCode);
								}
																		
							DenmarkReport.detailsAppend("Following are the Quick Spin related test cases", "Verify quick spin", "", "");								
									
							if("Yes".equalsIgnoreCase(cfnlib.XpathMap.get("QuickSpinAvailable")))
							{
				
										if(imageLibrary.isImageAppears("QuickSpinDisabled"))
									{
										DenmarkReport.detailsAppendFolder("Verify Quick Spin on Base scene ", "Quick Spin should not be enabled", "Quick Spin is not enabled", "Pass",languageCode);
										
									}
									else
									{
										DenmarkReport.detailsAppendFolder("Verify Quick Spin on Base scene ", "Quick Spin should not be enabled", "Quick Spin is enabled", "Fail",languageCode);											
										
									}	
											
									try {

										// click on menu if settings are under menu
										if ("Yes".equalsIgnoreCase(cfnlib.XpathMap.get("settingsUnderMenu"))) {
											if (imageLibrary.isImageAppears("Menu")) {

												imageLibrary.click("Menu");
												Thread.sleep(2000);
											}
										}
										if (imageLibrary.isImageAppears("Settings")) {
											// click on menu
											imageLibrary.click("Settings");
											Thread.sleep(2000);
											if(imageLibrary.isImageAppears("QuickSpinDisabled"))
											{
												DenmarkReport.detailsAppendFolder("Verify Quick Spin on Settings Screen ", "Quick Spin should not be enabled", "Quick Spin is not enabled", "Pass",languageCode);
											
												}
											else
											{
												DenmarkReport.detailsAppendFolder("Verify Quick Spin on Settings Screen ", "Quick Spin should not be enabled", "Quick Spin is enabled", "Fail",languageCode);											
											
												}	
											
											imageLibrary.click("MenuClose");
										}
									} catch (Exception e) {
										e.getMessage();
									}										
								}		
									
					//***********************************************************************************************************************
									
								DenmarkReport.detailsAppend("Following are the Clock test cases", "Verify Clock testcases", "", "");
								//verifying clock on top bar
								cfnlib.clockOnTopBar(DenmarkReport);
									
								if(imageLibrary.isImageAppears("Menu"))
								{
									imageLibrary.click("Menu");
									Thread.sleep(2000);	
									if(imageLibrary.isImageAppears("Paytable"))
									{
										//click on PayTable
										imageLibrary.click("Paytable");
										Thread.sleep(2000);																
										if(cfnlib.elementVisible_Xpath(cfnlib.XpathMap.get("PaytableClose")))
										{
											DenmarkReport.detailsAppendFolder("PayTable open", "PayTable should be opened", "PayTable is opened", "Pass",languageCode);
											Thread.sleep(2000);
											cfnlib.clockOnTopBar(DenmarkReport);																				
											cfnlib.func_Click(cfnlib.XpathMap.get("PaytableClose"));
											Thread.sleep(2000);											
										}
									}
								}								
								try {

									// click on menu if settings are under menu
									if ("Yes".equalsIgnoreCase(cfnlib.XpathMap.get("settingsUnderMenu"))) {
										if (imageLibrary.isImageAppears("Menu")) {
											imageLibrary.click("Menu");
											Thread.sleep(2000);
										}
									}
									if (imageLibrary.isImageAppears("Settings")) {
											// click on menu
										imageLibrary.click("Settings");
										Thread.sleep(2000);
										DenmarkReport.detailsAppendFolder("Settings Open ", "Settings should be open", "Settings is Opened", "Pass",languageCode);
										cfnlib.clockOnTopBar(DenmarkReport);
										imageLibrary.click("MenuClose");
									}

								} catch (Exception e) {
									e.getMessage();
								}
									
								if(imageLibrary.isImageAppears("BetButton"))						
								{								
									//click on bet menu
									System.out.println("Bet button is visible");log.debug("Bet button is visible");										
									imageLibrary.click("BetButton");
									Thread.sleep(2000);
									DenmarkReport.detailsAppendFolder("Bet menu", "Bet menu", "Bet menu is opened", "Pass",languageCode);
									cfnlib.clockOnTopBar(DenmarkReport);
									imageLibrary.click("MenuClose");
								}
									
						//***********************************************************************************************************************
									
								DenmarkReport.detailsAppend("Following are the Value Adds Player protection and help navigation test cases", "Verify Value Adds testcases", "", "");
									
								if(cfnlib.func_Click(cfnlib.XpathMap.get("HelpMenu")))
								{		
									Thread.sleep(1000);
									DenmarkReport.detailsAppendFolder("Verify if menu on top bar is open", "Menu on top bar should be open", "Menu on top bar is open", "Pass",languageCode);
									cfnlib.verifyPlayerProtection(DenmarkReport, imageLibrary, languageCode);
									Thread.sleep(5000);									
									cfnlib.verifyHelp(DenmarkReport, imageLibrary, languageCode);
									Thread.sleep(2000);																		
								}
									
						//***********************************************************************************************************************
									
								DenmarkReport.detailsAppend("Following are the Player balance test cases", "Verify Player balance testcases", "", "");	
										
								try {
										boolean credits = cfnlib.verifyRegularExpression(DenmarkReport, regExpr,
										cfnlib.getConsoleText("return " + cfnlib.XpathMap.get("Creditvalue")));
										System.out.println("Credit balance is "+ cfnlib.getConsoleText("return " + cfnlib.XpathMap.get("Creditvalue")));
										if (credits) {
												
											DenmarkReport.detailsAppendFolder("Verify the currency format in credit ","Credit should display in correct currency format " ,
											"Credit is displaying in correct currency format ", "Pass",languageCode);																
										} else {
												
											DenmarkReport.detailsAppendFolder("Verify the currency format in credit ","Credit should display in correct currency format " ,
											"Credit is displaying in incorrect currency format ", "Fail",languageCode);
									    }
									} catch (Exception e) {
										log.error(e.getMessage(), e);
										cfnlib.evalException(e);
								}
																
								try {
										boolean betAmt = cfnlib.verifyRegularExpression(DenmarkReport, regExpr,
										cfnlib.getConsoleText("return " + cfnlib.XpathMap.get("BetTextValue")));
										System.out.println("Bet Value is "+ cfnlib.getConsoleText("return " + cfnlib.XpathMap.get("BetTextValue")));
										
										if (betAmt) {
										log.debug("Base Game Bet Value : Pass");
										DenmarkReport.detailsAppendFolder("Verify the currency format for Bet ","Bet should display in correct currency format " ,
												"Bet is displaying in correct currency format ", "Pass",languageCode);											
										} else {
											log.debug("Base Game Bet Value : Fail");
											DenmarkReport.detailsAppendFolder("Verify the currency format for Bet ","Bet should display in correct currency format " ,
												"Bet is displaying in incorrect currency format ", "Fail",languageCode);
										}
									} catch (Exception e) {
										log.error(e.getMessage(), e);
								}								
								
				//*********************************************************Free Spins*********************************************								
							if(imageLibrary.isImageAppears("Spin"))
								{
									imageLibrary.click("Spin");
									
									if((imageLibrary.isImageAppears("SpinDisabled")))
									{
										DenmarkReport.detailsAppendFolder("Verify Spin button is disabled when reels are spinning", "Spin button should be disabled", "Spin button is disabled", "Pass",languageCode);
																				
									}
									else
									{
										DenmarkReport.detailsAppendFolder("Verify Spin button is disabled when reels are spinning", "Spin button should be disabled", "Spin button is not disabled", "Fail",languageCode);
										
									}										
								}
																																	
								}
								else 
								{
									System.out.println("Unable to Copy testdata");log.debug("Unable to Copy testdata");
									DenmarkReport.detailsAppendFolder("unable to copy test dat to server ", " ", "", "Fail",
											languageCode);
								}

							}//reading languages

						}// run flag
						
					} // try
					catch (Exception e) {
						System.out.println("Exception occur while processing currency: " + e);
						log.error("Exception occur while processing currency", e);
						DenmarkReport.detailsAppend("Exception occur while processing currency ", " ", "", "Fail");
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
			DenmarkReport.endReport();
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