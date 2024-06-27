package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.ThreeDEval;
import org.openqa.selenium.WebDriver;

import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;

public class Desktop_Sanity_UI_PlayNext2 {
	Logger log = Logger.getLogger(Desktop_Sanity_UI_PlayNext2.class.getName()); 
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
		int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDDesktop"));

     	
		try
		{
			// Step 1 
			if(webdriver!=null)
			{	
				
				String strFileName = TestPropReader.getInstance().getProperty("CurrencyTestDataPath");
				File testDataFile = new File(strFileName);
				List<Map<String, String>> currencyList = util.readCurrList();// mapping
				for (Map<String, String> currencyMap : currencyList) 
				{
				//userName=util.randomStringgenerator();					
					userName = "First1";
					// Step 2: To get the languages in MAP and load the language specific url
					String currencyID = currencyMap.get(Constant.ID).trim();
					String isoCode = currencyMap.get(Constant.ISOCODE).trim();
					System.out.println(isoCode);
					String CurrencyName = currencyMap.get(Constant.ISONAME).trim();
					String languageCurrency = currencyMap.get(Constant.LANGUAGECURRENCY).trim();
					String regExpr = currencyMap.get(Constant.REGEXPRESSION).trim();
					String regExprNoSymbol = currencyMap.get(Constant.REGEXPRESSION_NOSYMBOL).trim();
					String url = cfnlib.XpathMap.get("ApplicationURL");

					//if (util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles, currencyID, isoCode))
						System.out.println("Username : " + userName);System.out.println("CurrencyName: " + CurrencyName);
												
						if("Yes".equalsIgnoreCase(cfnlib.XpathMap.get("Clock")))
						{
							webdriver.manage().deleteAllCookies();
							cfnlib.loadGame(url);
							Thread.sleep(1000);
							cfnlib.waitForPageToBeReady();
						}
						else
						{
							webdriver.manage().deleteAllCookies();
							cfnlib.webdriver.navigate().to(url);
							cfnlib.waitForPageToBeReady();
						}				
						Thread.sleep(10000);
						lvcReport.detailsAppend("Verify Game launchaed ", "Game should be launched", "Game is launched", "PASS");
						//resize browser
						//cfnlib.resizeBrowser(1280, 960);
						cfnlib.resizeBrowser(1209, 692);
						//
							cfnlib.waitForElement("isSpinBtnVisible");
							if(cfnlib.isElementVisible("isSpinBtnVisible"))
							{
								lvcReport.detailsAppend("Verify Base Scene", "Base Scene is visible", "Base Scene is visible", "PASS");
							}
							else
							{
								lvcReport.detailsAppend("Verify Base Scene", "Base Scene is visible", "Base Scene is not visible", "FAIL");
							}
						// Gets the credit Amount && verifies Currency Format and check the currency format
							boolean creditAmount = cfnlib.verifyRegularExpression(lvcReport,regExpr,cfnlib.getConsoleText("return "+cfnlib.XpathMap.get("CreditValue")));
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
							
							// Check Bet in settings by moving coinsize and Coins per line
							if(cfnlib.isElementVisible("isMenuBtnVisible"))
							{
								lvcReport.detailsAppendFolder("Menu Button", "Menu Button for Bet Settings", "Menu Button is visible", "PASS",""+CurrencyName);
								cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("MenuCoordinatex"), "return " + cfnlib.XpathMap.get("MenuCoordinatey"));
								Thread.sleep(2000);
								cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("SettingsCoordinatex"), "return " + cfnlib.XpathMap.get("SettingsCoordinatey"));
								Thread.sleep(2000);	
								//CoinSize
								if(cfnlib.isElementVisible("isCoinSizeDropDownVisible"))
								{
									lvcReport.detailsAppendFolder("Bet Setting", "Coin size", "Coin Size dropdown is present", "PASS",""+CurrencyName);
									Thread.sleep(2000);
									cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("CoinSizeDDx"), "return " + cfnlib.XpathMap.get("CoinSizeDDy"));
									Thread.sleep(2000);
									cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("CoinsPerLineMaxx"), "return " + cfnlib.XpathMap.get("CoinsPerLineMaxy"));
									Thread.sleep(2000);
									cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("CoinSizeLablex"), "return " + cfnlib.XpathMap.get("CoinSizeLabley"));
								
								}
								else
								{
									lvcReport.detailsAppendFolder("Bet Setting", "Coin size", "Coin Size dropdown is not present", "FAIL",""+CurrencyName);
								}
								// Coins Per Line
								if(cfnlib.isElementVisible("isCoinsPerLineDropDownVisible"))
								{
									lvcReport.detailsAppendFolder("Bet Setting", "Coin per line", "Coin per line dropdown is present", "PASS",""+CurrencyName);
									Thread.sleep(2000);
									cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("CoinsPerLineDDx"), "return " + cfnlib.XpathMap.get("CoinsPerLineDDy"));	
									Thread.sleep(2000);
									cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("CoinsPerLineMaxx"), "return " + cfnlib.XpathMap.get("CoinsPerLineMaxy"));
									Thread.sleep(1000);
									cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("CoinSizeLablex"), "return " + cfnlib.XpathMap.get("CoinSizeLabley"));							
									Thread.sleep(1000);
								}
								else
								{
									lvcReport.detailsAppendFolder("Bet Setting", "Coin per line", "Coin per line dropdown is not present", "FAIL",""+CurrencyName);
								}
								// Bet value in bet setting after changing coin size and coins per line
								String bet1=cfnlib.getConsoleText("return "+cfnlib.XpathMap.get("TotalBetValue"));
								// get Back from setting
								cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("SettingBackButtonx"), "return " + cfnlib.XpathMap.get("SettingBackButtony"));
								Thread.sleep(1000);
								cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("MenuCoordinatex"), "return " + cfnlib.XpathMap.get("MenuCoordinatey"));
								Thread.sleep(1000);
								String bet2=cfnlib.getConsoleText("return "+cfnlib.XpathMap.get("BetButtonLabel"));
								Thread.sleep(1000);
								if(bet1.equals(bet2))
								{
									lvcReport.detailsAppendFolder("Bet Settings", "Settings total bet and Basescene bet", "Settings total bet and Basescene bet are matching", "PASS",""+CurrencyName);
								}
								else
								{
									lvcReport.detailsAppendFolder("Bet Settings", "Settings total bet and Basescene bet", "Settings total bet and Basescene bet are not matching", "FAIL",""+CurrencyName);
								}
								
							}
							else
							{
								lvcReport.detailsAppendFolder("Menu Button", "Menu Button for Bet Settings", "Menu Button is not visible", "FAIL",""+CurrencyName);
							}
							
							// Check sound and quick spin toggles
							if(cfnlib.isElementVisible("isMenuBtnVisible"))
							{
								lvcReport.detailsAppendFolder("Menu Button", "Menu Button for sound and Quick spin toggle Settings", "Menu Button is visible", "PASS",""+CurrencyName);
								cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("MenuCoordinatex"), "return " + cfnlib.XpathMap.get("MenuCoordinatey"));
								Thread.sleep(2000);
								cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("SettingsCoordinatex"), "return " + cfnlib.XpathMap.get("SettingsCoordinatey"));
								Thread.sleep(2000);	
								if(cfnlib.isElementVisible("isCoinSizeDropDownVisible"))
								{
									lvcReport.detailsAppendFolder("Settings", "Settings before toggle", "Settings are displayed", "PASS",""+CurrencyName);
									Thread.sleep(1000);
									if(cfnlib.isElementVisible("isSoundsToggleVisible"))
									{
										cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("SoundsTogglex"), "return " + cfnlib.XpathMap.get("SoundsToggley"));
										Thread.sleep(1000);
										lvcReport.detailsAppendFolder("Settings", "Sound toggle", "Sound setting toggled", "PASS",""+CurrencyName);
									}
									else
									{
										lvcReport.detailsAppendFolder("Settings", "Sound toggle", "Sound toggled is not visible", "FAIL",""+CurrencyName);
									}
									if(cfnlib.isElementVisible("isQuickSpinTogglePresent"))
									{
										cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("QuickSpinTogglex"), "return " + cfnlib.XpathMap.get("QuickSpinToggley"));
										Thread.sleep(1000);
										lvcReport.detailsAppendFolder("Settings", "QuickSpin toggle", "QuickSpin  setting toggled", "PASS",""+CurrencyName);
									}
									else
									{
										lvcReport.detailsAppendFolder("Settings", "QuickSpin toggle", "QuickSpin toggled is not visible", "FAIL",""+CurrencyName);
									}
								}
								//get Back from settings
								cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("SettingBackButtonx"), "return " + cfnlib.XpathMap.get("SettingBackButtony"));
								Thread.sleep(1000);
								cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("MenuCoordinatex"), "return " + cfnlib.XpathMap.get("MenuCoordinatey"));
								Thread.sleep(1000);
								if(cfnlib.isElementVisible("isSpinBtnVisible"))
								{
									lvcReport.detailsAppend("Base Scene", "Back to Base Scene from settings", "Base Scene is visible", "PASS");
								}
								else
								{
									lvcReport.detailsAppend("Base Scene", "Back to Base Scene from settings", "Base Scene is noy visible", "FAIL");
								}
							}
							else
							{
								lvcReport.detailsAppendFolder("Menu Button", "Menu Button for Sound and quick spin Settings", "Menu Button is not visible", "FAIL",""+CurrencyName);
							}
							// Menu+Paytable Validation
							if(cfnlib.isElementVisible("isMenuBtnVisible"))
							{
								cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("MenuCoordinatex"), "return " + cfnlib.XpathMap.get("MenuCoordinatey"));
								Thread.sleep(2000);
								cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("PaytableBtnCoordinatex"), "return " + cfnlib.XpathMap.get("PaytableBtnCoordinatey"));
								
								Thread.sleep(2000);	
								if(cfnlib.checkAvilabilityofElement("winUpTo"))
								{
									lvcReport.detailsAppendFolder("Paytable", "Paytable page", "Paytable page is displayed", "PASS",""+CurrencyName);
									
									//Scroll down to end of paytable page and take screen shot
									cfnlib.Scrolling(cfnlib.XpathMap.get("PaytableLastEle"));
									Thread.sleep(2000);
									lvcReport.detailsAppendFolder("Paytable", "Paytable page", "Scrolled down till end of Paytable", "PASS",""+CurrencyName);
									//Check Microgaming lable
									if(cfnlib.checkAvilabilityofElement("MicrogamingLable"))
									{
										lvcReport.detailsAppendFolder("Microgaming lable", "Microgaming lable", "Microgaming lable is available", "PASS",""+CurrencyName);
									}
									else
									{
										lvcReport.detailsAppendFolder("Microgaming lable", "Microgaming lable", "Microgaming lable is not available", "FAIL",""+CurrencyName);
									}
								}
									
								else
								{
									lvcReport.detailsAppendFolder("Paytable", "Paytable page", "Paytable page is not displayed", "FAIL",""+CurrencyName);
								}
											
								// back to basegame from paytable
								
								//cfnlib.ClickByCoordinatesWithAdjust("return " + cfnlib.XpathMap.get("PaytableBackBtnx"), "return " + cfnlib.XpathMap.get("PaytableBackBtny"),10,10);
								cfnlib.func_click("PaytableBackBtn");
								Thread.sleep(1000);
								cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("MenuCoordinatex"), "return " + cfnlib.XpathMap.get("MenuCoordinatey"));
								Thread.sleep(1000);
								if(cfnlib.isElementVisible("isSpinBtnVisible"))
								{
									lvcReport.detailsAppend("Base Scene", "Back to Base Scene from Paytable", "Base Scene is visible", "PASS");
								}
								else
								{
									lvcReport.detailsAppend("Base Scene", "Back to Base Scene from Paytable", "Base Scene is not visible", "FAIL");
								}
								
							}
						
						//Help validation
						if(cfnlib.elementVisible_Xpath(cfnlib.XpathMap.get("HelpIcon")))
						{						
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
						//AutoPlay Check
						if(cfnlib.isElementVisible("isAutoplayBTNVisible"))
						{
							//click on autoplay button
							lvcReport.detailsAppendFolder("Autoplay", "Autoplay Button", "Autoplay Button is Visible", "PASS",""+CurrencyName);
							cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("StartAutoplayBTNx"), "return " + cfnlib.XpathMap.get("StartAutoplayBTNy"));
							Thread.sleep(2000);						
							if(cfnlib.isElementVisible("isAutoplay10xVisible"))
							{
								lvcReport.detailsAppendFolder("Autoplay", "Autoplay 10x", "Autoplay 10x Visible", "PASS",""+CurrencyName);
								cfnlib.ClickByCoordinatesWithAdjust("return " + cfnlib.XpathMap.get("Autoplay10xCoordinatex"), "return " + cfnlib.XpathMap.get("Autoplay10xCoordinatey"), -10, -10);
								Thread.sleep(15000);
								if(cfnlib.isElementVisible("isAutoplayStopVisible"))
								{
									lvcReport.detailsAppendFolder("Autoplay", "Autoplay 10x", "Autoplay 10x started", "PASS",""+CurrencyName);
									cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("AutoplayStopCoordinatex"), "return " + cfnlib.XpathMap.get("AutoplayStopCoordinatey"));
									Thread.sleep(3000);
									cfnlib.waitForElement("isSpinBtnVisible");
									if(cfnlib.isElementVisible("isSpinBtnVisible"))
									{
										lvcReport.detailsAppend("Verify Base Scene", "Base Scene after Autoplay", "Landed to Base Scene after Autoplay", "PASS");
									}
									else
									{
										lvcReport.detailsAppend("Verify Base Scene", "Base Scene after Autoplay", "Not Landed to Base Scene after Autoplay", "FAIL");
									}																									
								}
								else
								{
									lvcReport.detailsAppendFolder("Autoplay", "Autoplay 10x", "Autoplay 10x not started", "FAIL",""+CurrencyName);
								}
							}
							else
							{
								lvcReport.detailsAppendFolder("Autoplay", "Autoplay 10x", "Autoplay 10x is not Visible", "FAIL",""+CurrencyName);
							}
						}
						else
						{
							lvcReport.detailsAppendFolder("Autoplay", "Autoplay Button", "Autoplay Button is not Visible", "FAIL",""+CurrencyName);
						}
						
						}														
					}		
				
			}
		
catch (Exception e) 
{
	log.error(e.getMessage(),e);
	System.out.println(e.getMessage());
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
