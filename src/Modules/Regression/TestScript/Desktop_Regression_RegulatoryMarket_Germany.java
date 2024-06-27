package Modules.Regression.TestScript;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;

public class Desktop_Regression_RegulatoryMarket_Germany
{

	/**
	 * Desktop ReelThunder "gtp 131" 
	 * This script verifies the Germany check points & below are the following .
	 * =======================================================================
	 * Launch the Germany with EN & DA
	 * 1.Verify Session Reminder(4 Mins)
	 * 2.Verify Currency Format
	 * 3.Verify Reel Spin Duration 
	 * 4.Verify TopBar Visibility
	 * 5.Verify Clock from TopBar
	 * 6.Verify Help Text from TopBar and its Navigation
	 * 7.Verify Autoplay 
	 * 8.Verify Quick Spin
	 * 9.Verify Stop 
	 * 10.Verify Menu , Settings, Bet & Paytable 
	 * 11.Verify (Help,Responsible Gaming & Game History) from Menu & its Navigation 
	 * 
	 *
	 */

	public ScriptParameters scriptParameters;
	Logger log = Logger.getLogger(Desktop_Regression_RegulatoryMarket_Germany.class.getName());
	public void script() throws Exception {
		String mstrTCName=scriptParameters.getMstrTCName();
		String mstrTCDesc=scriptParameters.getMstrTCDesc();
		String mstrModuleName=scriptParameters.getMstrModuleName();
		WebDriver webdriver=scriptParameters.getDriver();
		BrowserMobProxyServer proxy=scriptParameters.getProxy();
		String startTime=scriptParameters.getStartTime();
		String filePath=scriptParameters.getFilePath();
		String browserName=scriptParameters.getBrowserName();
		String framework=scriptParameters.getFramework();
		String gameName=scriptParameters.getGameName();
		String status=null;
		String language = "GermanyPaytable";
		int mintDetailCount=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		String userName ;
		String regMarket = "Germany";
		int envId = 1658;
		int productId = 5159;
		String currencyIsoCode = "EUR";
		int marketTypeID = 31;
		double balance = 10000;
		
		Desktop_HTML_Report germany = new Desktop_HTML_Report(webdriver, browserName, filePath, startTime, mstrTCName,mstrTCDesc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings, mintStepNo, status,gameName);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,germany, gameName);
		CommonUtil util = new CommonUtil();
		String mid=TestPropReader.getInstance().getProperty("MID");
		String  cid=TestPropReader.getInstance().getProperty("CIDDesktop");
		
		try 
		{
			if (webdriver != null) 
			{
				for (int i = 1; i < 3; i++)
				{
				userName=util.randomStringgenerator();
				util.randomUserCreationForRegMarkets(userName, regMarket, envId, productId,currencyIsoCode,marketTypeID);
				util.updateUserBalance(userName, balance);
		        cfnlib.germanyBetSettings(userName,mid,cid);
				String url = cfnlib.XpathMap.get("Germany-EN");
			
					if(i==1)
					{	
						String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
						log.debug("url = " +LaunchURl);
						cfnlib.loadGame(LaunchURl);	
					}
					else
					{
						String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
						if(LaunchURl.contains("languagecode"))
							LaunchURl=	LaunchURl.replace("languagecode=en","languagecode=de");
						else if(LaunchURl.contains("languageCode"))
							LaunchURl =LaunchURl.replace("languageCode=en","languageCode=de");	
						log.debug("url = " +LaunchURl);
						cfnlib.loadGame(LaunchURl);	
					}
					
					if (framework.equalsIgnoreCase("Force")) 
					{
						cfnlib.setNameSpace();
						log.debug("Force Games");
					}
					Thread.sleep(2000);
					
					/*	 Uncomment if the game has Continue Button on Load Game 
					// This is for Continue Button on Loading Screen
					if ((Constant.YES.equalsIgnoreCase(cfnlib.XpathMap.get("continueBtnOnGameLoad"))))
					{
						cfnlib.newFeature();
					}
					
							
					//Verify Coin Size Changed Pop up 
					cfnlib.isCoinSizeChanged(germany);
					
					//cfnlib.waitForSpinButton();
				
					*/	
					
					//session reminder
					boolean sessionReminderVisible=cfnlib.isSessionReminderPresent(germany);
					Thread.sleep(1000);
					if(sessionReminderVisible) 
					{
						germany.detailsAppend("Verify Session Reminder  ","Session Reminder is present and clicked on Continue Button ","Session Reminder is present and clicked on Continue Button","PASS");
					}
					else
					{
						germany.detailsAppend("Verify Session Reminder ","Session Reminder is not present and unable to click Continue Button","Session Reminder is not present and unable to click Continue Button","FAIL");
					
					}

					
					// Currency Check
					boolean curencycheck = cfnlib.germanyCurrencyCheck(germany);
					if (curencycheck)
					{
						log.debug("YES, The Currency is SAME");
						germany.detailsAppend(" Currency", "Currency is Same", "Currency is Same", "PASS");
					}
					else 
					{
						log.debug("NO, The Currency is Difference");
						germany.detailsAppend(" Currency", "Currency is Same", "Currency is Same", "FAIL");
					}
					
					
					//To find the reel spin duration for a single spin	
					long isReelspin=cfnlib.reelSpinDuratioN(germany);
					if(isReelspin >= 5000)
					{
						log.debug("Reel Spin Duration , STATUS : PASS");
						germany.detailsAppend( "Reel Spin Duration ", "Reel Spin Duration in Sec is :" +isReelspin, "Reel Spin Duration  in Sec is :" +isReelspin, "PASS");
					}
					else
					{
						log.debug("Reel Spin Duration , STATUS : FAIL");
						germany.detailsAppend("Reel Spin Duration ", "Reel Spin Duration in Sec is :" +isReelspin, "Reel Spin Duration  in Sec is :" +isReelspin, "FAIL");
					}	

					// Verify TopBar is visble or not

					boolean isTopBarVisible = cfnlib.verifyTopBarVisible();
					Thread.sleep(2000);

					if (isTopBarVisible)
					{
						germany.detailsAppend("Verify TopBar ", "TopBar is Displayed", "TopBar is Displayed", "PASS");
					}
					else 
					{
						log.debug("Topbar is not visible");
						germany.detailsAppend("Verify TopBar ", "TopBar is Displayed", "TopBar is Displayed", "FAIL");
					}

					
					// Clock on the Top Bar 
					boolean clock = cfnlib.clockFromTopBar(germany);
					if (clock )
					{
						germany.detailsAppend(" Clock on Topbar", "Clock is Displayed ", "Clock is Displayed", "PASS");
					} 
					else
					{
						germany.detailsAppend(" Clock on Topbar", "Clock is Displayed", "Clock is Displayed ", "FAIL");
					}


					//Verify Help Text Link from TopBar 
					boolean helptext = cfnlib.helpTextLink(germany);
					if (helptext) 
					{
						germany.detailsAppend("  Help Text  from TopBar ", "Help is Displayed and its Navigation", "Help is Displayed and its Navigation", "PASS");															
					}
					else 
					{
						germany.detailsAppend("  Help Text  from TopBar ", "Help is Displayed and its Navigation", "Help is Displayed and its Navigation", "FAIL");	
					}
						
					
					// Verify the Autoplay button availability in germany Market 

					boolean isAutoplay = cfnlib.verifyAutoplayAvailabilty();
					Thread.sleep(1000);
					if (isAutoplay) 
					{
						germany.detailsAppend(" Autoplay ", "Autoplay is available ", "Autoplay is available", "FAIL");
					}
					else
					{
						germany.detailsAppend(" Autoplay ", "Autoplay is not available ", "Autoplay is not available", "PASS");
					}
					
					
					// Verify the quickspin button availability in germany Market 

					boolean isquickspin = cfnlib.verifyQuickspinAvailablity();
					Thread.sleep(1000);
					if (isquickspin) 
					{
						germany.detailsAppend(" Quickspin ", "Quickspin is available ", "Quickspin is available", "FAIL");
					}
					else
					{
						germany.detailsAppend(" Quickspin ", "Quickspin is not available ", "Quickspin is not available", "PASS");
					}
					Thread.sleep(2000);

					// Verify the stop button availability in germany Market 

					boolean isstopbtn = cfnlib.verifyStopButtonAvailablity();
					Thread.sleep(1000);
					if (isstopbtn) 
					{
						germany.detailsAppend("Stop button", "Stop button is available", "Stop button is available","FAIL");
					}
					else 
					{
						germany.detailsAppend("Stop button", "Stop button is not available", "Stop button is not available","PASS");
					}
					Thread.sleep(2000);
					
					
					// Menu open & close
					boolean menuopen = cfnlib.menuOpen();
					if (menuopen) 
					{
						log.debug("Menu Opend");
						germany.detailsAppend("Menu Open", "Menu is Open ", "Menu is Open", "PASS");
						Thread.sleep(3000);
						boolean closemenu = cfnlib.menuClose();
						if (closemenu)
						{
							log.debug("Menu Closed");
							germany.detailsAppend("Menu Closed", "Menu is Closed ", "Menu is Closed", "PASS");
						} 
						else
						{
							log.debug("Menu Closed");
							germany.detailsAppend("Menu Closed", "Menu is Closed ", "Menu is Closed", "FAIL");
						}
						Thread.sleep(2000);
					} 
					else
					{
						germany.detailsAppend("Menu Open", "Menu is Open ", "Menu is Open", "FAIL");
					}

					// Verify Settings open & close
					boolean opensettings = cfnlib.settingsOpen();
					if (opensettings) 
					{
						log.debug("Settings Opened");
						germany.detailsAppend("Settings Open", "Settings is Open ", "Settings is Open", "PASS");
						Thread.sleep(3000);
						boolean closeSettings = cfnlib.settingsBack();
						if (closeSettings) 
						{
							log.debug("Settings Closed");
							germany.detailsAppend("Settings Closed", "Settings is Closed ", "Settings is Closed","PASS");
						}
						else 
						{
							log.debug("Settings Closed");
							germany.detailsAppend("Settings Closed", "Settings is Closed ", "Settings is Closed","FAIL");
						}
						Thread.sleep(2000);
					} 
					else
					{
						germany.detailsAppend("Settings Open", "Settings is Open ", "Settings is Open", "FAIL");
					}

					
					// Verify Bet open & close
					boolean openBet = cfnlib.open_TotalBet();
					if (openBet)
					{
						log.debug("Bet Opend");
						germany.detailsAppend("Bet Open", "Bet is Open ", "Bet is Open", "PASS");
						Thread.sleep(2000);
						cfnlib.close_TotalBet();
						Thread.sleep(2000);
						log.debug("Bet Closed");
						germany.detailsAppend("Bet Closed", "Bet is Closed ", "Bet is Closed", "PASS");

					}
					else 
					{
						germany.detailsAppend("Bet Open", "Bet is Open ", "Bet is Open", "FAIL");
					}
					
					
					//Verify PayTable 
					if(!gameName.contains("Scratch") && cfnlib.checkAvilability(cfnlib.XpathMap.get("isMenuPaytableBtnVisible")))
					{
						cfnlib.capturePaytableScreenshot(germany,language);
						log.debug("Paytable opened");
						Thread.sleep(2000);
						cfnlib.paytableClose();	
						log.debug("Paytable closed"); 
						Thread.sleep(2000);
					}
				
					
					// Verify Help from Menu
					boolean help = cfnlib.germanyHelpFromTopBarMenu(germany);
					if (help) 
					{
						germany.detailsAppend("  Help from Menu ", "Help is Displayed and its Navigation", "Help is Displayed and its Navigation", "PASS");															
					}
					else 
					{
						germany.detailsAppend("  Help from Menu ", "Help is Displayed and its Navigation", "Help is Displayed and its Navigation", "FAIL");	
					}
					Thread.sleep(2000);
					
					// Verify Resposible Gaming from Menu
					boolean responsiblegaming = cfnlib.germanyResponsibleGamingFromTopBarMenu(germany);
					if (responsiblegaming) 
					{
						germany.detailsAppend(" Responsible Gaming from Menu ", "Responsible Gaming is Displayed and its Navigation", " Responsible Gaming is Displayed and its Navigation", "PASS"); 
					}
					else 
					{
						germany.detailsAppend(" Responsible Gaming from Menu ", "Responsible Gaming is Displayed and its Navigation", " Responsible Gaming is Displayed and its Navigation", "FAIL");
					}
					Thread.sleep(2000);
	
					// Verify  Game Hitory from Menu
					boolean gameHistoryFromMenu = cfnlib.germanyGameHistoryFromTopBarMenu(germany);
					if (gameHistoryFromMenu) 
					{
						germany.detailsAppend("Game History from Menu ", " Game History is Displayed and its Navigation", "Game History is Displayed and its Navigation", "PASS");
						
					}
					else 
					{
						germany.detailsAppend("Game History from Menu ", " Game History is Displayed and its Navigation", "Game History is Displayed and its Navigation", "FAIL");
					}
						

				    Thread.sleep(2000);
				    log.debug("!!!!!!! Done with Germany Market !!!!!!!");
				    log.debug("                                           ");

				}// Close Finally 

			}// Close For Loop

		}// Close Try Block
		
		// -------------------Handling the exception---------------------//

		catch (Exception e) 
		{
			log.error(e.getMessage(), e);
			cfnlib.evalException(e);
		}

		finally {

			// Global.browserproxy.abort();
			// ---------- Closing the report----------//

			germany.endReport();
			// ---------- Closing the webdriver ----------//
			webdriver.close();

			webdriver.quit();

			// Global.appiumService.stop();
		}
	}

}
