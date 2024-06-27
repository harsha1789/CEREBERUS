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
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.ImageLibrary;
import com.zensar.automation.library.TestPropReader;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;
/**
 * This Script is for play.next ui checks
 * BreakAwayDeluxe
 * BreakAway Lucky Wilds
 * 
 * @author sp68146
 * @Modified VC66297
 * @Modified PB61055- reports, screenshots folder structure and language change code
 *
 */
public class Desktop_Sanity_UI_PlayNext {
	
		Logger log = Logger.getLogger(Desktop_Sanity_UI_PlayNext.class.getName()); 
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
			Desktop_HTML_Report sanityReport = new Desktop_HTML_Report(webdriver,browserName,filePath,startTime,mstrTCName,mstrTCDesc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,status1,gameName);
			DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
			CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,sanityReport, gameName);
			CommonUtil util = new CommonUtil();
			RestAPILibrary apiObj = new RestAPILibrary();					
			List<String> copiedFiles=new ArrayList<>();
			int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
			int cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDDesktop"));
			
			try
			{ 
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
					String strFileName = TestPropReader.getInstance().getProperty("SanityTestDataPath");
					File testDataFile = new File(strFileName);
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
												
							sanityReport.detailsAppend("*** CURRENCY AND LANGUAGE ***", "Currency: "+currencyName+", Language: "+languageCurrency,"", "");
						
							sanityReport.detailsAppend("Sanity Suite Test cases in "+languageCurrency+" for "+currencyName+" ", "Sanity Suite Test cases", "", "");
						
							userName = util.randomStringgenerator();
							//userName="swati119";
							System.out.println(userName);
						
							if (util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles, currencyID,isoCode))
							{
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
								if(launchURl.contains("LanguageCode"))
								urlNew = launchURl.replaceAll("LanguageCode=en", "LanguageCode=" + languageCurrency);
								else if(launchURl.contains("languagecode"))
								urlNew = launchURl.replaceAll("languagecode=en", "languagecode=" + languageCurrency);
								else if(launchURl.contains("languageCode"))
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
								Thread.sleep(12000);
								if("Yes".equalsIgnoreCase(cfnlib.XpathMap.get("NFD"))) {
									sanityReport.detailsAppend("Verify Game launchaed ", "Game should be launched", "Game is launched", "PASS");
									//resize browser
									//cfnlib.resizeBrowser(1280, 960);							
									//check for visibility of continue button and take screenshot
									if(imageLibrary.isImageAppears("NFDButton"))
									{
										
										//refresh scenario
										webdriver.navigate().refresh();
										cfnlib.waitForElement("NFDButton");
										Thread.sleep(2500);
										sanityReport.detailsAppendFolder("Refresh", "Refresh on NFD", "Refresh on NFD", "PASS",languageCurrency);
										System.out.println("NFD button is visible");log.debug("NFD button is visible");
										sanityReport.detailsAppendFolder("Verify Continue button is visible ", "Continue buttion is visible", "Continue button is visible", "PASS",languageCurrency);										
										imageLibrary.click("NFDButton");
										Thread.sleep(3000);
							
									}
									else
									{
										System.out.println("NFD button is not visible");log.debug("NFD button is not visible");
										sanityReport.detailsAppendFolder("Verify Continue button is visible ", "Continue buttion is visible", "Continue button is not visible", "FAIL",languageCurrency);
									}
								}		
							
								if(imageLibrary.isImageAppears("Spin"))
								{
									System.out.println("Spin button is visible");log.debug("Spin button is visible");
									sanityReport.detailsAppendFolder("Verify Base Scene", "Base Scene is visible", "Base Scene is visible", "PASS",languageCurrency );
								}
								else
								{
									System.out.println("Spin button is not visible");log.debug("Spin button is not visible");
									sanityReport.detailsAppendFolder("Verify Base Scene", "Base Scene is visible", "Base Scene is not visible", "FAIL",languageCurrency);
								}												
								
								sanityReport.detailsAppend("Following are the Bet value verification test cases", "Verify Bet values", "", "");	
								//enter if loop only when bet button is visible on base game
								if(imageLibrary.isImageAppears("BetButton"))						
								{								
									//click on bet menu
									System.out.println("Bet button is visible");log.debug("Bet button is visible");
									Thread.sleep(2000);
									
									imageLibrary.click("BetButton");
									
									//refresh scenario
									webdriver.navigate().refresh();
									cfnlib.waitForElement("NFDButton");
									Thread.sleep(2500);
									sanityReport.detailsAppendFolder("Refresh", "Refresh on Bet Pannel", "Refresh on Bet Pannel", "PASS",languageCurrency);
									System.out.println("NFD button is visible");log.debug("NFD button is visible");
									imageLibrary.click("NFDButton");
									Thread.sleep(3000);
									
									imageLibrary.click("BetButton");
									System.out.println("Clicked on bet button");log.debug("Clicked on bet button");		
									Thread.sleep(2000);
									//check if max bet button is visible to know whether bet panel is open
									if(imageLibrary.isImageAppears("BetMax"))
									{
										System.out.println("Checking Bet options");log.debug("Checking Bet options");
										sanityReport.detailsAppendFolder("Bet menu", "Bet menu", "Bet menu is opened", "PASS",languageCurrency);
										// Check Coin Size Slider
										if(cfnlib.XpathMap.get("CoinSizeSliderPresent").equalsIgnoreCase("Yes"))
										{
											String  bet1=cfnlib.getConsoleText("return "+cfnlib.XpathMap.get("BetMenuBetValue"));			
											Thread.sleep(2000);
											cfnlib.clickAtButton(cfnlib.XpathMap.get("CoinSizeSliderSet"));										
										
											Thread.sleep(2000);			
											String  bet2=cfnlib.getConsoleText("return "+cfnlib.XpathMap.get("BetMenuBetValue"));
											if(bet1.equalsIgnoreCase(bet2) != true)
											{
												sanityReport.detailsAppendFolder("Coin Size Slider", "Coin Size Slider is should work", "Coin Size Slider is working", "PASS",languageCurrency);										
											}
											else
											{
												sanityReport.detailsAppendFolder("Coin Size Slider", "Coin Size Slider should work", "Coin Size Slider is not working", "FAIL",languageCurrency);
											}
										}
									// Check Coins per line slider
										if(cfnlib.XpathMap.get("CoinsPerLineSliderPresent").equalsIgnoreCase("Yes"))
										{
											String  bet1=cfnlib.getConsoleText("return "+cfnlib.XpathMap.get("BetMenuBetValue"));										
											Thread.sleep(2000);
											cfnlib.clickAtButton(cfnlib.XpathMap.get("CoinsPerLineSliderSet"));
											Thread.sleep(2000);										
											String  bet2=cfnlib.getConsoleText("return "+cfnlib.XpathMap.get("BetMenuBetValue"));										
											if(bet1.equalsIgnoreCase(bet2) != true)
											{									
												sanityReport.detailsAppendFolder("Coins per Line Slider", "Coins Per Line Slider should work", "Coins Per Line Slider is working", "PASS",languageCurrency);
											}
											else
											{
												sanityReport.detailsAppendFolder("Coins per Line Slider", "Coins Per Line Slider should work", "Coins Per Line Slider is not working", "FAIL",languageCurrency);
											}							
										}
										if(cfnlib.XpathMap.get("LinesSliderPresent").equalsIgnoreCase("Yes"))
										{
											String  bet1=cfnlib.getConsoleText("return "+cfnlib.XpathMap.get("BetMenuBetValue"));										
											Thread.sleep(2000);
											cfnlib.clickAtButton(cfnlib.XpathMap.get("LinesSliderSet"));
											Thread.sleep(2000);										
											String  bet2=cfnlib.getConsoleText("return "+cfnlib.XpathMap.get("BetMenuBetValue"));										
											if(bet1.equalsIgnoreCase(bet2) != true)
											{
												sanityReport.detailsAppendFolder( "Line Slider", "Line Slider should work", "Line Slider is not working", "PASS",languageCurrency);
											}
											else
											{
												sanityReport.detailsAppendFolder("Line Slider", "Line Slider should work", "Line Slider is not working", "FAIL",languageCurrency);
											}									
										}
								
										imageLibrary.click("BetMax");
										System.out.println("Clicked on Max Bet ");log.debug("Clicked on Max Bet ");
										Thread.sleep(2000);
										if(imageLibrary.isImageAppears("Spin"))
										{									
											sanityReport.detailsAppendFolder("Max Bet", "Click on Max Button", "Base Scene is visible", "PASS",languageCurrency );
										}
										else
										{
											System.out.println("Clicked on Max Bet not working");log.debug("Clicked on Max Bet not working");
											sanityReport.detailsAppendFolder("Max Bet", "Click on Max Button", "Base Scene is visible not visible", "FAIL",languageCurrency );
										}
										Thread.sleep(2000);
									}
								} // Bet Validation end
													
								//Quick spin on base game							
								if(cfnlib.XpathMap.get("QuickSpinonBaseGame").equalsIgnoreCase("Yes"))
								{
									
									sanityReport. detailsAppend("Following is the QuickSpin verification test case" ,"Verify QuickSpin", "", "");
									System.out.println("Quick spin validation ");log.debug("Quick spin validation ");
									if(imageLibrary.isImageAppears("QuickSpinOn"))
									{
										//click on bet menu
										Thread.sleep(2000);
										imageLibrary.click("QuickSpinOn");
										Thread.sleep(2000);							
										if(imageLibrary.isImageAppears("QuickSpinOff"))
										{
											sanityReport.detailsAppendFolder("Quick spin Buttom ", "Quick Spin button should be clicked", "Quick Spin button is clicked", "PASS",languageCurrency);
										}
										else{
											sanityReport.detailsAppendFolder("Quick spin Buttom ", "Quick Spin button should be clicked", "unable to click on quick spin", "FAIL",languageCurrency);
										}								
									}
									else {
										sanityReport.detailsAppendFolder("Quick spin Buttom ", "Quick Spin button should be available", "quick spin button is not visible", "FAIL",languageCurrency);				
									}
								}
					
								sanityReport.detailsAppend("Following are the MenuOption test cases", "Verify MenuOptions", "", "");
					
								// Menu and Paytable Validation
								//click on menu
								System.out.println("Paytable Validation started");log.debug("Paytable Validation started");
								Thread.sleep(2000);											
								if(imageLibrary.isImageAppears("Menu"))
								{
									imageLibrary.click("Menu");
									sanityReport.detailsAppendFolder("Menu Open ", "Menu should open", "Menu is Opened", "PASS",languageCurrency);
									if(imageLibrary.isImageAppears("Paytable"))
									{
										sanityReport.detailsAppendFolder("PayTable Button ", "PayTable should visible", "PayTable is visible", "PASS",languageCurrency);
					
										Thread.sleep(2000);
										//click on PayTable
										imageLibrary.click("Paytable");
										
										//refresh scenario
										webdriver.navigate().refresh();
										cfnlib.waitForElement("NFDButton");
										Thread.sleep(2500);
										sanityReport.detailsAppendFolder("Refresh", "Refresh on Paytable", "Refresh on Paytable", "PASS",languageCurrency);
										System.out.println("NFD button is visible");log.debug("NFD button is visible");
										imageLibrary.click("NFDButton");
										Thread.sleep(3000);
										
										imageLibrary.click("Menu");
										Thread.sleep(2000);
										
										//click on PayTable
										imageLibrary.click("Paytable");
										Thread.sleep(2000);																
										if(cfnlib.elementVisible_Xpath(cfnlib.XpathMap.get("PaytableClose")))
										{
											sanityReport.detailsAppendFolder("PayTable ", "PayTable should opened", "PayTable is opened", "PASS",languageCurrency);
											Thread.sleep(2000);
											boolean scrollPaytable = cfnlib.paytableScroll(sanityReport, languageCurrency);
											if (scrollPaytable) {										
												sanityReport.detailsAppendFolder("PayTable ", "PayTable Scroll ", "PayTable Scroll", "Pass",languageCurrency);
									
											} else {
										
												sanityReport.detailsAppendFolder("PayTable ", "PayTable Scroll ", "PayTable Scroll", "FAIL", languageCurrency);
											}
											cfnlib.func_Click(cfnlib.XpathMap.get("PaytableClose"));
											Thread.sleep(2000);
											if(imageLibrary.isImageAppears("Spin"))
											{
												System.out.println("Paytable Closed");log.debug("Paytable Closed");
												sanityReport.detailsAppendFolder("Verify Paytable Closed", "Paytable should be Closed", "Base Scene is visible after paytable closed", "PASS", languageCurrency );
											}
											else
											{
												System.out.println("Paytable is not Closed");log.debug("Paytable is not Closed");
												sanityReport.detailsAppendFolder("Verify Paytable Closed", "Paytable should be Closed", "Paytable is not closed", "FAIL", languageCurrency );
											}										
										}
										else
										{
											sanityReport.detailsAppendFolder("PayTable ", "PayTable should opened", "PayTable is not opened", "FAIL",languageCurrency);
										}									
									}
									else
									{
										sanityReport.detailsAppendFolder("PayTable Button", "PayTable button should be visible", "PayTable button is not visible", "FAIL",languageCurrency);
									}
								}								
								else
								{
									sanityReport.detailsAppendFolder("Menu Open ", "Menu should be opened", "Menu is not Opened", "FAIL",languageCurrency);
								}
								//Setting Toggle
								try {
									System.out.println("Checking Settings toggles");log.debug("Checking Settings toggles");
									if(imageLibrary.isImageAppears("Menu"))
									{
										//click on menu if settings are under menu
										if("Yes".equalsIgnoreCase(cfnlib.XpathMap.get("settingsUnderMenu"))) {
											imageLibrary.click("Menu");
											Thread.sleep(2000);	
											}
						
										if(imageLibrary.isImageAppears("Settings"))
										{			
											//refresh scenario
											webdriver.navigate().refresh();
											cfnlib.waitForElement("NFDButton");
											Thread.sleep(2500);
											System.out.println("NFD button is visible");log.debug("NFD button is visible");
											imageLibrary.click("NFDButton");
											Thread.sleep(3000);
											
											imageLibrary.click("Menu");
											Thread.sleep(2000);	
											
											//click on menu
											imageLibrary.click("Settings");
											Thread.sleep(2000);														
											sanityReport.detailsAppendFolder("Setting open ", "Settings should be open", "Settings is open", "PASS",languageCurrency);													
											//quick spin toggle
											if(cfnlib.isElementVisible("isSettingsTurboVisible"))
											{
												//click on quick spin toggle
												Thread.sleep(2000);
												cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("SettingsTurboCoordinatex"), "return " + cfnlib.XpathMap.get("SettingsTurboCoordinatey"));
												Thread.sleep(2000);
												//take screenshot of sound toggle working
												sanityReport.detailsAppendFolder("Turbo toggle in settings ", "Turbo toggle in settings should work", "Turbo toggle in settings is working", "PASS",languageCurrency);
											}
								
											//sound
											if(cfnlib.isElementVisible("isVoiceOvers"))
											{									
												//click on sound toggle
												Thread.sleep(2000);
												cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("VoiceOversToggleX"), "return " + cfnlib.XpathMap.get("VoiceOversToggleY"));
												Thread.sleep(2000);
												//take screenshot of sound toggle working
												sanityReport.detailsAppendFolder("Voice over toggle in settings ", "Voice over toggle in settings should work", "Voice over toggle in settings is working", "PASS",languageCurrency);							
											}
								
											//sound
											if(cfnlib.isElementVisible("isSoundsVisible"))
											{									
												//click on sound toggle
												Thread.sleep(2000);
												cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("SoundsCoordinatex"), "return " + cfnlib.XpathMap.get("SoundsCoordinatey"));
												Thread.sleep(2000);
												//take screenshot of sound toggle working
												sanityReport.detailsAppendFolder("Sound toggle in settings ", "Sound toggle in settings should work", "Sound toggle in settings is working", "PASS",languageCurrency);								
											}							
										//close settings
											Thread.sleep(2000);
											if("Yes".equalsIgnoreCase(cfnlib.XpathMap.get("settingsUnderMenu"))) {
 												imageLibrary.click("MenuClose");
												Thread.sleep(2000);	
											}
											else {
												imageLibrary.click("SettingsClose");							
											}
											Thread.sleep(2000);								
											if(imageLibrary.isImageAppears("Spin"))
											{
												sanityReport.detailsAppendFolder("Base Scene after Settings closed ", "Base Scene after Settings closed", "We are on Base Scene after Settings closed", "PASS",languageCurrency);	
											}
											else
											{
												sanityReport.detailsAppendFolder("Base Scene after Settings closed ", "Base Scene after Settings closed", "We are not on Base Scene after Settings closed", "FAIL",languageCurrency);
											}						
										}
									}	
								}catch (Exception e) {
									e.getMessage();
								}

								sanityReport.detailsAppend("Following is the Help navigation test case", "Verify Help", "", "");	
					
								System.out.println("Checking help Menu");log.debug("Checking help Menu");
								if(cfnlib.XpathMap.get("HelpMenuPresent").equalsIgnoreCase("Yes"))
								{
									//click on help menu
									Thread.sleep(2000);
									cfnlib.func_Click(cfnlib.XpathMap.get("HelpMenu"));
									Thread.sleep(2000);
								}					
								if(cfnlib.elementVisible_Xpath(cfnlib.XpathMap.get("HelpIcon")))
								{						
									sanityReport.detailsAppendFolder("Help Icon ", "Help Icon", "Help Icon is visible", "PASS",languageCurrency);
									//click on help
									Thread.sleep(2000);
									cfnlib.func_Click(cfnlib.XpathMap.get("HelpIcon"));
									Thread.sleep(3000);	
									sanityReport.detailsAppendFolder("Verify if help navigation ", "Help should navigated", "Help is navigated", "PASS",languageCurrency);
									//if help doesn't opens in same window
									if((cfnlib.XpathMap.get("SameWindow")).equalsIgnoreCase("No"))
									{
										//close help window,come back to game window
										cfnlib.navigate_back(sanityReport,languageCurrency);
										Thread.sleep(1000);
									}
									//help opens in same window
									else if((cfnlib.XpathMap.get("SameWindow")).equalsIgnoreCase("Yes"))
									{
										//refresh
										try
										{
											Thread.sleep(3000);
											cfnlib.RefreshGame("clock");
											//webdriver.navigate().back();
											Thread.sleep(15000);	
											if("Yes".equalsIgnoreCase(cfnlib.XpathMap.get("NFD")))
											{
												Thread.sleep(5000);	
												imageLibrary.click("NFDButton");
												//cfnlib.clickAtButton(cfnlib.XpathMap.get("baseIntroTapToContinueButtonClick"));			      					 
											}							
										} catch(Exception e)
										{
											log.error(e.getMessage(),e);
											cfnlib.evalException(e);
										}
		      					
									}		      			
									//take screenshot ,back from help
									Thread.sleep(3000);
									if(imageLibrary.isImageAppears("Spin"))
									{
							
										sanityReport.detailsAppendFolder("Back from help", "Back from help", "On BaseScene, Back from help", "PASS",languageCurrency);
									}
									else
									{
							
										sanityReport.detailsAppendFolder("Back from help", "Back from help", "Not on BaseScene, Back from help", "FAIL",languageCurrency);
									}	
					
								}
								else
								{
									sanityReport.detailsAppendFolder("Help icon", "Help icon", "Help icon is not visible", "FAIL",languageCurrency);
								}
					
					
								sanityReport.detailsAppend("Following are the Autoplay verification test cases", "Verify Autoplay", "", "");
					
								//autoplay menu
								System.out.println("Checking Autoplay Options");log.debug("Checking Autoplay Options");
								if(imageLibrary.isImageAppears("Autoplay")) 
								{
									
									//refresh scenario
									webdriver.navigate().refresh();
									cfnlib.waitForElement("NFDButton");
									Thread.sleep(2500);
									sanityReport.detailsAppendFolder("Refresh", "Refresh on Autoplay", "Refresh on Autoplay", "PASS",languageCurrency);
									System.out.println("NFD button is visible");log.debug("NFD button is visible");
									imageLibrary.click("NFDButton");
									Thread.sleep(3000);
									
									//click on autoplay button						
									imageLibrary.click("Autoplay");
									Thread.sleep(2000);						
									if(imageLibrary.isImageAppears("Autoplay10x"))
									{
										sanityReport.detailsAppendFolder("Auto play menu", "Auto play menu", "Auto play menu is opened", "PASS",languageCurrency);
										//check if sliders working
										//spin slider set							
										try
										{
											if(cfnlib.XpathMap.get("SpinSliderPresent").equalsIgnoreCase("Yes"))
											{
												String  s1=cfnlib.getConsoleText("return "+cfnlib.XpathMap.get("SpinSliderValue"));									
												Thread.sleep(2000);
												cfnlib.clickAtButton(cfnlib.XpathMap.get("SpinSliderSet"));
												Thread.sleep(2000);									
												String  s2=cfnlib.getConsoleText("return "+cfnlib.XpathMap.get("SpinSliderValue"));
									
												if(s1.equalsIgnoreCase(s2) != true)
												{
													sanityReport.detailsAppendFolder("Autoplay", "Spin slider", "Spin slider is working", "PASS",languageCurrency);
												}
												else
												{
													sanityReport.detailsAppendFolder("Autoplay", "Spin slider", "Spin slider is not working", "FAIL",languageCurrency);
												}
											}
										}catch (Exception e) {
											log.error(e.getMessage(),e);
											cfnlib.evalException(e);
										}														
										//total bet slider							
										try
										{
											if(cfnlib.XpathMap.get("TotalBetSliderPresent").equalsIgnoreCase("Yes"))
											{
												String  s1=cfnlib.getConsoleText("return "+cfnlib.XpathMap.get("TotalBetSliderValue"));									
												Thread.sleep(2000);
												cfnlib.clickAtButton(cfnlib.XpathMap.get("TotalBetSliderSet"));
												Thread.sleep(2000);									
												String  s2=cfnlib.getConsoleText("return "+cfnlib.XpathMap.get("TotalBetSliderValue"));									
												if(s1.equalsIgnoreCase(s2) != true)
												{
													sanityReport.detailsAppendFolder("Autoplay", "Total bet slider", "Total bet slider is working", "PASS",languageCurrency);
												}
												else
												{
													sanityReport.detailsAppendFolder("Autoplay", "Total bet slider", "Total bet slider is not working", "FAIL",languageCurrency);	}
											}
										}catch (Exception e) {
											log.error(e.getMessage(),e);
											cfnlib.evalException(e);
										}							
										//winlimit
										try
										{
											if(cfnlib.XpathMap.get("WinLimitSliderPresent").equalsIgnoreCase("Yes"))
											{
												String  s1=cfnlib.getConsoleText("return "+cfnlib.XpathMap.get("WinLimitSliderValue"));									
												Thread.sleep(2000);
												cfnlib.clickAtButton(cfnlib.XpathMap.get("WinLimitSliderSet"));
												Thread.sleep(2000);									
												String  s2=cfnlib.getConsoleText("return "+cfnlib.XpathMap.get("WinLimitSliderValue"));									
												if(s1.equalsIgnoreCase(s2) != true)
												{
													sanityReport.detailsAppendFolder("Autoplay", "Win Limit", "Win Limit is working", "PASS",languageCurrency);
												}
												else
												{
													sanityReport.detailsAppendFolder("Autoplay", "Win Limit", "Win Limit is not working", "FAIL",languageCurrency);
												}
											}
										}catch (Exception e) {
											log.error(e.getMessage(),e);
											cfnlib.evalException(e);
										}														
										//loss limit							
										try
										{
											if(cfnlib.XpathMap.get("LossLimitSliderPresent").equalsIgnoreCase("Yes"))
											{
												String  s1=cfnlib.getConsoleText("return "+cfnlib.XpathMap.get("LossLimitSliderValue"));									
												Thread.sleep(2000);
												cfnlib.clickAtButton(cfnlib.XpathMap.get("LossLimitSliderSet"));
												Thread.sleep(2000);									
												String  s2=cfnlib.getConsoleText("return "+cfnlib.XpathMap.get("LossLimitSliderValue"));									
												if(s1.equalsIgnoreCase(s2) != true)
												{
													sanityReport.detailsAppendFolder("Autoplay", "Loss Limit", "Loss Limit slider is working", "PASS",languageCurrency);
												}
												else
												{
													sanityReport.detailsAppendFolder("Autoplay", "Loss Limit", "Loss Limit slider is not working", "FAIL",languageCurrency);
												}
											}
										}
										catch (Exception e) {
											log.error(e.getMessage(),e);
											cfnlib.evalException(e);
										}
							
										// Checks for Autoplay 
										try
										{
											if(imageLibrary.isImageAppears("AutoplayStart"))
											{
												imageLibrary.click("AutoplayStart");
												Thread.sleep(5000);
												System.out.println("Clicked on start  Autoplay"); log.debug("Clicked on start Autoplay");
												if(imageLibrary.isImageAppears("AutoplayStop"))
												{
													sanityReport.detailsAppendFolder("Autoplay", "Autoplay should work", "Autoplay is working", "PASS",languageCurrency);
												}
												else
												{
													sanityReport.detailsAppendFolder("Autoplay", "Autoplay should work", "Autoplay is not working", "FAIL",languageCurrency);
												}
											}
											else 
											{
												sanityReport.detailsAppendFolder("Autoplay", "Start Autoplay BTN visibility", "Start Autoplay BTN is not Visible", "FAIL",languageCurrency);
											}	
										}	
										catch(Exception e)
										{
											log.error(e.getMessage(),e);
											cfnlib.evalException(e);
										}
									}
									else
									{
										sanityReport.detailsAppendFolder("Auto play menu", "Auto play menu", "Auto play menu is not opened", "FAIL",languageCurrency);
									}	
						
								}else
								{
									sanityReport.detailsAppendFolder("Auto play menu", "Auto play menu", "Auto play menu is not opened", "FAIL",languageCurrency);
								}
					
							}					
							else 
							{
								System.out.println("Unable to Copy testdata");log.debug("Unable to Copy testdata");
								sanityReport.detailsAppendFolder("unable to copy test dat to server ", " ", "", "Fail",
										languageCurrency);
							}
						} // try
						catch (Exception e) {
							System.out.println("Exception occur while processing currency: "+ e); log.error("Exception occur while processing currency", e);
							sanityReport.detailsAppend("Exception occur while processing currency ", " ", "", "Fail");
							cfnlib.evalException(e);
						}

					} // for loop : mapping currencies 
				} //driver Closed
					
			}//closing try block 
			// -------------------Handling the exception---------------------//
			catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			// -------------------Closing the connections---------------//
			finally {
			sanityReport.endReport();
			if(!copiedFiles.isEmpty())
			{
				if(TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
						util.deleteBluemesaTestDataFiles(mid,cid,copiedFiles);
				else
					apiObj.deleteAxiomTestDataFiles(copiedFiles);
			}
				webdriver.close();
				webdriver.quit();
				//proxy.abort();
				Thread.sleep(1000);
		}
			
	}

}