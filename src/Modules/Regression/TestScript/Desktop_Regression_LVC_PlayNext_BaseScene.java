package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;
/**
 * This Script is for play.next games
 * 
 * @author pb61055
 *
 */

public class Desktop_Regression_LVC_PlayNext_BaseScene 
{
	Logger log = Logger.getLogger(Desktop_Regression_LVC_PlayNext_BaseScene.class.getName()); 
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
			
		
		Desktop_HTML_Report lvcReport = new Desktop_HTML_Report(webdriver,browserName,filePath,startTime,mstrTCName,mstrTCDesc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,status1,gameName);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,lvcReport, gameName);
		CommonUtil util = new CommonUtil();
		RestAPILibrary apiObj = new RestAPILibrary();
		
		List<String> copiedFiles=new ArrayList<>();
     	
		try
		{
		
			// Step 1 
			if(webdriver!=null)
			{	
				
				//Implement code for test data copy depending on the env
			//	String strFileName=TestPropReader.getInstance().getProperty("CurrencyTestDataPath");
			//	File testDataFile=new File(strFileName);
				
				//List<Map<String, String>> currencyList= util.readCurrList();// mapping
					
				//Step 2: To get the languages in MAP and load the language specific url
					
				String currencyID = "28";
				String isoCode = "THB";
				//System.out.println(isoCode);
				String CurrencyName = "Thai Baht";
				String languageCurrency = "en";
				String regExpr="฿(?:[0-9]{0,3})((?:,[0-9]{3})|(?:[0-9]{0,3}))((?:,[0-9]{3})|(?:[0-9]{0,3})).[0-9]{2}";
				String regExprNoSymbol="(?:[0-9]{0,3})((?:,[0-9]{3})|(?:[0-9]{0,3}))((?:,[0-9]{3})|(?:[0-9]{0,3})).[0-9]{2}";
					
				//if(	util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles,currencyID,isoCode))
               // System.out.println("Username : "+userName); System.out.println("CurrencyName : "+CurrencyName);
				
				String url = cfnlib.XpathMap.get("ApplicationURL");				
				if("Yes".equalsIgnoreCase(cfnlib.XpathMap.get("Clock")))
				{
					cfnlib.loadGame(url);
					Thread.sleep(1000);
					}
				else
				{
					cfnlib.webdriver.navigate().to(url);
					cfnlib.waitForPageToBeReady();
				}
				
				Thread.sleep(9000);
				
				
				lvcReport.detailsAppend("Verify Game launchaed ", "Game should be launched", "Game is launched", "PASS");
			
				
				//resize browser
				cfnlib.resizeBrowser(1280, 960);
				Thread.sleep(2000);
				
				if(cfnlib.XpathMap.get("NFD").equalsIgnoreCase("Yes"))
				{
					if(cfnlib.XpathMap.get("NFDHasHook").equalsIgnoreCase("Yes"))
					{
						//check for visiblity of nfd button and take screenshot
						if(cfnlib.isElementVisible("isNFDButtonVisible"))
						{
							lvcReport.detailsAppend("Verify Continue button is visible ", "Continue buttion is visible", "Continue button is visible", "Pass");
						}
						else
						{
							lvcReport.detailsAppend("Verify Continue button is visible ", "Continue buttion is visible", "Continue button is not visible", "Fail");
						}
						
						//Click on nfd button
						Thread.sleep(5000);
						//cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("ClicktoContinueCoordinatex"), "return " + cfnlib.XpathMap.get("ClicktoContinueCoordinatey"));
						cfnlib.ClickByCoordinates("return "+cfnlib.XpathMap.get("ClicktoContinueCoordinatex"),"return "+cfnlib.XpathMap.get("ClicktoContinueCoordinatey"));
						Thread.sleep(5000);
						if(cfnlib.isElementVisible("isSpinBtnVisible"))
						{
							lvcReport.detailsAppend("Verify Base Scene", "Base Scene is visible", "Base Scene is visible", "Pass");
						}
						else
						{
							lvcReport.detailsAppend("Verify Base Scene", "Base Scene is visible", "Base Scene is not visible", "Pass");
						}
						
						// Gets the credit Amount && verifies Currency Format and check the currency format
				/*		boolean creditAmount = cfnlib.verifyRegularExpression(lvcReport,regExpr,cfnlib.getConsoleText("return "+cfnlib.XpathMap.get("CreditValue")));
						if(creditAmount)
						{
							System.out.println("Base Game credit Amount : PASS");log.debug("Base Game credit Amount : PASS");
							lvcReport.detailsAppendFolder("Base Game", "credit Amount", "credit Amount", "PASS",""+CurrencyName);
						}
						else
						{
							System.out.println("Base Game credit Amount : FAIL");log.debug("Base Game credit Amount : FAIL");
							lvcReport.detailsAppendFolder("Base Game", "credit Amount", "credit Amount", "FAIL",""+CurrencyName);
						}
			
						// Gets the Bet Amt && verifies Currency Format and check the currency format
						boolean betAmt = cfnlib.verifyRegularExpression(lvcReport,regExpr,cfnlib.getConsoleText("return "+cfnlib.XpathMap.get("BetButtonLabel")));
						if(betAmt)
						{
							System.out.println("Base Game Bet Value : PASS");log.debug("Base Game Bet Value : PASS");
							lvcReport.detailsAppendFolder("Base Game", "Bet Amount", "Bet Amount", "PASS",""+CurrencyName);
						}
						else
						{
							System.out.println("Base Game Bet Value : FAIL");log.debug("Base Game Bet Value : FAIL");
							lvcReport.detailsAppendFolder("Base Game", "Bet Amount", "Bet Amount", "FAIL",""+CurrencyName);
						}
						*/
						//enter if loop only when bet button is visible on base game
					}
				}
						
				if(cfnlib.isElementVisible("IsBetButtonVisible"))
				{
							//click on bet menu
							Thread.sleep(2000);
							cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("BetMenuCoordinatex"), "return " + cfnlib.XpathMap.get("BetMenuCoordinatey"));
							Thread.sleep(2000);
							
							//check if max bet button is visible to know whether bet panel is open
							if(cfnlib.isElementVisible("isMaxBetVisible"))
							{
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
										//cfnlib.details_append_folderOnlyScreeshot(webdriver,"CoinSizeSliderWorking");
										lvcReport.detailsAppendFolder("Coin Size Slider", "Coin Size Slider is working should work", "Coin Size Slider is working", "PASS",""+CurrencyName);
										}
									else
									{
										cfnlib.details_append_folderOnlyScreeshot(webdriver,"CoinSizeSliderNtWorking");
										lvcReport.detailsAppendFolder("Coin Size Slider", "Coin Size Slider is working should work", "Coin Size Slider is not working", "FAIL",""+CurrencyName);}
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
										lvcReport.detailsAppendFolder("Coins per Line Slider", "Coins Per Line Slider is working should work", "Coins Per Line Slider is working", "PASS",""+CurrencyName);
										}
									else
									{
										lvcReport.detailsAppendFolder("Coins per Line Slider", "Coins Per Line Slider is working should work", "Coins Per Line Slider is not working", "PASS",""+CurrencyName);
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
										lvcReport.detailsAppendFolder( "Line Slider", "Line Slider is working should work", "Line Slider is not working", "PASS",""+CurrencyName);
										}
									else
									{
										lvcReport.detailsAppendFolder("Line Slider", "Line Slider is working should work", "Line Slider is not working", "PASS",""+CurrencyName);
									}
								
								}
								cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("MaxBetCoordinatex"), "return " + cfnlib.XpathMap.get("MaxBetCoordinatey"));
								Thread.sleep(2000);
							}
						} // Bet Validation end
				
				//Quickspin on base game
				if(cfnlib.XpathMap.get("QuickSpinonBaseGame").equalsIgnoreCase("Yes"))
				{
					if(cfnlib.isElementVisible("isQuickSpinBGVisible"))
					{
						//click on bet menu
						Thread.sleep(2000);
						cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("QuickSpinBGCoordinatex"), "return " + cfnlib.XpathMap.get("QuickSpinBGCoordinatey"));
						Thread.sleep(2000);
						
						if(cfnlib.isElementVisible("isQuickSpinBGVisible"))
						{
							lvcReport.detailsAppendFolder("Quick spin Buttom ", "Quick Spin button should displayed", "Quick Spin button is displayed", "PASS",""+CurrencyName);
						}
						else
						{
							lvcReport.detailsAppendFolder("Quick spin Buttom ", "Quick Spin button should displayed", "Quick Spin button is not displayed", "FAIL",""+CurrencyName);
						}
							
					}
				}
				// Menu+Paytable Validation
				if(cfnlib.isElementVisible("isMenuBtnVisible"))
				{
					lvcReport.detailsAppendFolder("Menu Button ", "Menu Button should visible", "Menu Button is visible", "Pass",""+CurrencyName);
					//click on menu
					Thread.sleep(2000);
					cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("MenuCoordinatex"), "return " + cfnlib.XpathMap.get("MenuCoordinatey"));
					Thread.sleep(2000);
					
					
					if(cfnlib.isElementVisible("isMenuBackBtnVisible"))
					{
						lvcReport.detailsAppendFolder("Menu Open ", "Menu should open", "Menu is Opened", "PASS",""+CurrencyName);
						if(cfnlib.isElementVisible("isPaytableBtnVisible"))
						{
							lvcReport.detailsAppendFolder("PayTable Button ", "PayTable should visible", "PayTable is visible", "PASS",""+CurrencyName);
							
							//click on PayTable
							Thread.sleep(2000);
							cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("PaytableCoordinatex"), "return " + cfnlib.XpathMap.get("PaytableCoordinatey"));
							Thread.sleep(2000);
							
							
							if(cfnlib.elementVisible_Xpath(cfnlib.XpathMap.get("PaytableClose")))
							{
								lvcReport.detailsAppendFolder("PayTable ", "PayTable should opened", "PayTable is opened", "PASS",""+CurrencyName);
								Thread.sleep(2000);
								cfnlib.func_Click(cfnlib.XpathMap.get("PaytableClose"));
								Thread.sleep(2000);	
							}
							else
							{
								lvcReport.detailsAppendFolder("PayTable ", "PayTable should opened", "PayTable is not opened", "FAIL",""+CurrencyName);
							}
								
						}
						else
						{
							lvcReport.detailsAppendFolder("PayTable Button", "PayTable button should be visible", "PayTable button is not visible", "FAIL",""+CurrencyName);
						}
					}	
						
					else
					{
						lvcReport.detailsAppendFolder("Menu Open ", "Menu should be opened", "Menu is not Opened", "FAIL",""+CurrencyName);}
				}
				else
				{
					lvcReport.detailsAppendFolder("Menu Button ", "Menu Button should visible", "Menu Button is not visible", "FAIL",""+CurrencyName);
				}
				//Setting Toggle
				
				if(cfnlib.isElementVisible("isMenuBtnVisible"))
				{
					//click on menu
					Thread.sleep(2000);
					cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("MenuCoordinatex"), "return " + cfnlib.XpathMap.get("MenuCoordinatey"));
					Thread.sleep(2000);
					
					if(cfnlib.isElementVisible("isSettingsBtnVisible"))
					{
						
						//click on menu
						Thread.sleep(2000);
						cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("SettingsCoordinatex"), "return " + cfnlib.XpathMap.get("SettingsCoordinatey"));
						Thread.sleep(2000);
						
						if(cfnlib.isElementVisible("isMenuBackBtnVisible"))
						{
							//turbo toggle
							if(cfnlib.isElementVisible("isSettingsTurboVisible"))
							{
								//click on quickspin toggle
								Thread.sleep(2000);
								cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("SettingsTurboCoordinatex"), "return " + cfnlib.XpathMap.get("SettingsTurboCoordinatey"));
								Thread.sleep(2000);
								//take screenshot of sound toggle working
								lvcReport.detailsAppendFolder("Turbo toggle in settings ", "Turbo toggle in settings should work", "Turbo toggle in settings is working", "PASS",""+CurrencyName);
							}
							
							
							//sound
							if(cfnlib.isElementVisible("isSoundsVisible"))
							{
								cfnlib.details_append_folderOnlyScreeshot(webdriver,"SettingsOpened");
								//click on sound toggle
								Thread.sleep(2000);
								cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("SoundsCoordinatex"), "return " + cfnlib.XpathMap.get("SoundsCoordinatey"));
								Thread.sleep(2000);
								//take screenshot of sound toggle working
								lvcReport.detailsAppendFolder("Sound toggle in settings ", "Sound toggle in settings should work", "Sound toggle in settings is working", "PASS",""+CurrencyName);
							}
							
							//close settings
							Thread.sleep(2000);
							cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("MenuBackBtnCoordinatex"), "return " + cfnlib.XpathMap.get("MenuBackBtnCoordinatey"));
							Thread.sleep(2000);
							
							if(cfnlib.isElementVisible("isSpinBtnVisible"))
							{
								lvcReport.detailsAppendFolder("Base Scene after Settings closed ", "Base Scene after Settings closed", "We are on Base Scene after Settings closed", "PASS",""+CurrencyName);	
							}
							else
							{
								lvcReport.detailsAppendFolder("Base Scene after Settings closed ", "Base Scene after Settings closed", "We are not on Base Scene after Settings closed", "FAIL",""+CurrencyName);
							}
						}
					}
				}
				
				//help menu
				if(cfnlib.XpathMap.get("HelpMenuPresent").equalsIgnoreCase("Yes"))
				{
					//click on help menu
					Thread.sleep(2000);
					cfnlib.func_Click(cfnlib.XpathMap.get("HelpMenu"));
					Thread.sleep(2000);
				}
				
				if(cfnlib.elementVisible_Xpath(cfnlib.XpathMap.get("HelpIcon")))
				{
					cfnlib.details_append_folderOnlyScreeshot(webdriver,"HelpIconVisible");
					lvcReport.detailsAppendFolder("Help Icon ", "Help Icon", "Help Icon is visible", "PASS",""+CurrencyName);
					//click on help
					Thread.sleep(2000);
					cfnlib.func_Click(cfnlib.XpathMap.get("HelpIcon"));
					Thread.sleep(2000);
					
					
					//if help doesn't opens in same window
	      			if((cfnlib.XpathMap.get("SameWindow")).equalsIgnoreCase("No"))
	      			{
	      				//close help window,come back to game window
	      				cfnlib.navigate_back();
	      				Thread.sleep(1000);
	      			}
	      			//help opens in same window
	      			else
	      				if((cfnlib.XpathMap.get("SameWindow")).equalsIgnoreCase("Yes"))
	      				{
	      					//refresh
	      					try
	      					{
	      						Thread.sleep(3000);
	      					cfnlib.RefreshGame("clock");
	      					Thread.sleep(3000);
	      					
	      					//click nfd
	      					Thread.sleep(3000);
							cfnlib.clickAtButton(cfnlib.XpathMap.get("baseIntroTapToContinueButtonClick"));								
						
	      					} catch(Exception e)
	      	                {
	      	              	  log.error(e.getMessage(),e);
	      	              	  cfnlib.evalException(e);
	      	                }
	      					
	      				}
	      			
	      			//take screenshot ,back from help
	      			Thread.sleep(3000);
					if(cfnlib.isElementVisible("isSpinBtnVisible"))
					{
						cfnlib.details_append_folderOnlyScreeshot(webdriver,"BackFromHelp");
						lvcReport.detailsAppendFolder("Back from help", "Back from help", "On BaseScene, Back from help", "PASS",""+CurrencyName);
					}
					else
					{
						cfnlib.details_append_folderOnlyScreeshot(webdriver,"NtBackFromHelp");
						lvcReport.detailsAppendFolder("Back from help", "Back from help", "Not on BaseScene, Back from help", "FAIL",""+CurrencyName);
					}	
				
				}
				else
				{
					lvcReport.detailsAppendFolder("Help icon", "Help icon", "Help icon is not visible", "FAIL",""+CurrencyName);
				}		
				
				//autoplay menu
				if(cfnlib.isElementVisible("isAutoplayMenuBtnVisible"))
				{
					//click on autoplay button
					Thread.sleep(2000);
					cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("AutoplayMenuBtnCoordinatex"), "return " + cfnlib.XpathMap.get("AutoplayMenuBtnCoordinatey"));
					Thread.sleep(2000);
					
					if(cfnlib.isElementVisible("isAutoplay10xVisible"))
					{
						lvcReport.detailsAppendFolder("Auto play menu", "Auto play menu", "Auto play menu is opened", "PASS",""+CurrencyName);
						
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
									lvcReport.detailsAppendFolder("Autoplay", "Spin slider", "Spin slider is working", "PASS",""+CurrencyName);
								}
								else
								{
									lvcReport.detailsAppendFolder("Autoplay", "Spin slider", "Spin slider is not working", "FAIL",""+CurrencyName);
								}
							}
						}catch (Exception e) {
							log.error(e.getMessage(),e);
							cfnlib.evalException(e);
						}
						
						
						//toal bet slider
						
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
									lvcReport.detailsAppendFolder("Autoplay", "Total bet slider", "Total bet slider is working", "PASS",""+CurrencyName);
								}
								else
								{
									lvcReport.detailsAppendFolder("Autoplay", "Total bet slider", "Total bet slider is not working", "Fail",""+CurrencyName);	}
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
									lvcReport.detailsAppendFolder("Autoplay", "Win Limit", "Win Limit is working", "PASS",""+CurrencyName);
								}
								else
								{
									lvcReport.detailsAppendFolder("Autoplay", "Win Limit", "Win Limit is not working", "Fail",""+CurrencyName);
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
									lvcReport.detailsAppendFolder("Autoplay", "Loss Limit", "Loss Limit slider is working", "PASS",""+CurrencyName);
								}
								else
								{
									lvcReport.detailsAppendFolder("Autoplay", "Loss Limit", "Loss Limit slider is not working", "FAIL",""+CurrencyName);
								}
							}
						}
						catch (Exception e) {
							log.error(e.getMessage(),e);
							cfnlib.evalException(e);
						}
					}
					else
					{
						lvcReport.detailsAppendFolder("Auto play menu", "Auto play menu", "Auto play menu is not opened", "FAIL",""+CurrencyName);
					}	
				}
				
				
			}//Webdriver Closing
				
				
				
		}//closing try block 
		//-------------------Handling the exception---------------------//
		catch (Exception e) 
		{
			log.error(e.getMessage(),e);
			
		}
		//-------------------Closing the connections---------------//
		finally
		{
			lvcReport.endReport();
			
			webdriver.close();
			webdriver.quit();
			//proxy.abort();
			Thread.sleep(1000);
		}//closing finally block	
	
}

}
