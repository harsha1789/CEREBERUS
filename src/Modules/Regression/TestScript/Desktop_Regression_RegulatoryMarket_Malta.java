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

public class Desktop_Regression_RegulatoryMarket_Malta
{

	/**
	 * Desktop ReelThunder "gtp 131" 
	 * This script verifies the Malta check points & below are the following .
	 * =======================================================================
	 * Launch the Malta with EN  
	 * 1.Verify Session Reminder(4 Mins)
	 * 2.Verify Currency Format
	 * 3.Verify Reel Spin Duration 
	 * 4.Verify TopBar Visibility
	 * 5.Verify Clock & Game Name from TopBar
	 * 6.Verify Help Text from TopBar and its Navigation
	 * 7.Verify Quick Spin is Available
	 * 8.Verify Stop is Available
	 * 9.Verify Menu , Settings, Bet & Pay-table 
	 * 10.Verify (Help,Responsible Gaming & Game History) from Menu & its Navigation 
	 *  
	 * 
	 *
	 */

	public ScriptParameters scriptParameters;
	Logger log = Logger.getLogger(Desktop_Regression_RegulatoryMarket_Malta.class.getName());
	public void script() throws Exception
	{
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
		String language = "MaltaPaytable";
		int mintDetailCount=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		String userName ;
		String regMarket = "Malta";
		int envId = 1658;
		int productId = 5148;
		String currencyIsoCode = "GBP";
		int marketTypeID = 27;
		double balance = 10000;
		
		Desktop_HTML_Report malta = new Desktop_HTML_Report(webdriver, browserName, filePath, startTime, mstrTCName,mstrTCDesc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings, mintStepNo, status,gameName);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,malta, gameName);
		CommonUtil util = new CommonUtil();
		
		try 
		{
			if (webdriver != null) 
			{
				/*userName=util.randomStringgenerator();
				util.randomUserCreationForRegMarkets(userName, regMarket, envId, productId,currencyIsoCode,marketTypeID);
				//util.updateUserBalance(userName, balance);
				//String url = cfnlib.XpathMap.get("Malta_url");
			    String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
				log.debug("url = " +LaunchURl);
				cfnlib.loadGame(LaunchURl);	*/
				String url = cfnlib.XpathMap.get("Malta_URL");
			    cfnlib.loadGame(url);
					
					if (framework.equalsIgnoreCase("Force")) 
					{
						cfnlib.setNameSpace();
						log.debug("Force Games");
					}
					Thread.sleep(2000);
					
					
					//session reminder
					boolean sessionReminderVisible=cfnlib.isSessionReminderPresent(malta);
					Thread.sleep(1000);
					if(sessionReminderVisible) 
					{
						malta.detailsAppend("Verify Session Reminder  ","Session Reminder is present and clicked on Continue Button ","Session Reminder is present and clicked on Continue Button","PASS");
					}
					else
					{
						malta.detailsAppend("Verify Session Reminder ","Session Reminder is not present and unable to click Continue Button","Session Reminder is not present and unable to click Continue Button","FAIL");
					
					}

					
					// Currency Check
					boolean curencycheck = cfnlib.maltaCurrencyCheck(malta);
					if (curencycheck)
					{
						log.debug("YES, The Currency is SAME");
						malta.detailsAppend(" Currency", "Currency is Same", "Currency is Same", "PASS");
					}
					else 
					{
						log.debug("NO, The Currency is Difference");
						malta.detailsAppend(" Currency", "Currency is Same", "Currency is Same", "FAIL");
					}
				
					
					//To find the reel spin duration for a single spin	
					long isReelspin=cfnlib.reelSpinDuratioN(malta);
					if(isReelspin >= 2500)
					{
						log.debug("Reel Spin Duration , STATUS : PASS");
						malta.detailsAppend( "Reel Spin Duration ", "Reel Spin Duration in Sec is :" +isReelspin, "Reel Spin Duration  in Sec is :" +isReelspin, "PASS");
					}
					else
					{
						log.debug("Reel Spin Duration , STATUS : FAIL");
						malta.detailsAppend("Reel Spin Duration ", "Reel Spin Duration in Sec is :" +isReelspin, "Reel Spin Duration  in Sec is :" +isReelspin, "FAIL");
					}	

					// Verify TopBar is visble or not
					boolean isTopBarVisible = cfnlib.verifyTopBarVisible();
					Thread.sleep(2000);
					if (isTopBarVisible)
					{
						malta.detailsAppend("Verify TopBar ", "TopBar is Displayed", "TopBar is Displayed", "PASS");
					}
					else 
					{
						log.debug("Topbar is not visible");
						malta.detailsAppend("Verify TopBar ", "TopBar is Displayed", "TopBar is Displayed", "FAIL");
					}
					
					//Game name on the Top Bar 
					String isGameName=cfnlib.gameNameFromTopBar(malta);
					if(isGameName != null)
					{
						malta.detailsAppend(" Gamename  on Topbar", "Gamename is displayed", "Gamename is displayed", "PASS");
					}
					else
					{
						malta.detailsAppend(" Gamename  on Topbar", "Gamename isn't displayed", "Gamename isn't displayed", "FAIL");
						Thread.sleep(2000);
					}

					
					// Clock on the Top Bar 
					boolean clock = cfnlib.clockFromTopBar(malta);
					if (clock )
					{
						malta.detailsAppend(" Clock on Topbar", "Clock is Displayed ", "Clock is Displayed", "PASS");
					} 
					else
					{
						malta.detailsAppend(" Clock on Topbar", "Clock is Displayed", "Clock is Displayed ", "FAIL");
					}


					//Verify Help Text Link from TopBar 
					boolean helptext = cfnlib.helpTextLink(malta);
					if (helptext) 
					{
						malta.detailsAppend("  Help Text  from TopBar ", "Help is Displayed and its Navigation", "Help is Displayed and its Navigation", "PASS");															
					}
					else 
					{
						malta.detailsAppend("  Help Text  from TopBar ", "Help is Displayed and its Navigation", "Help is Displayed and its Navigation", "FAIL");	
					}
						
					
					// Verify the quickspin button availability in germany Market 

					boolean isquickspin = cfnlib.isQuickspinAvailable();
					Thread.sleep(1000);
					if (isquickspin) 
					{
						malta.detailsAppend(" Quickspin ", "Quickspin is Available ", "Quickspin is Available", "PASS");
					}
					else
					{
						malta.detailsAppend(" Quickspin ", "Quickspin is not Available ", "Quickspin is not Available", "FAIL");
					}
					Thread.sleep(2000);

					// Verify the stop button availability in germany Market 

					boolean isstopbtn = cfnlib.isStopButtonAvailable();
					Thread.sleep(1000);
					if (isstopbtn) 
					{
						malta.detailsAppend("Stop button", "Stop button is Available", "Stop button is Available","PASS");
					}
					else 
					{
						malta.detailsAppend("Stop button", "Stop button is not Available", "Stop button is not Available","FAIL");
					}
					Thread.sleep(2000);
					
					
					// Menu open & close
					boolean menuopen = cfnlib.menuOpen();
					if (menuopen) 
					{
						log.debug("Menu Opend");
						malta.detailsAppend("Menu Open", "Menu is Open ", "Menu is Open", "PASS");
						Thread.sleep(3000);
						boolean closemenu = cfnlib.menuClose();
						if (closemenu)
						{
							log.debug("Menu Closed");
							malta.detailsAppend("Menu Closed", "Menu is Closed ", "Menu is Closed", "PASS");
						} 
						else
						{
							log.debug("Menu Closed");
							malta.detailsAppend("Menu Closed", "Menu is Closed ", "Menu is Closed", "FAIL");
						}
						Thread.sleep(2000);
					} 
					else
					{
						malta.detailsAppend("Menu Open", "Menu is Open ", "Menu is Open", "FAIL");
					}

					// Verify Settings open & close
					boolean opensettings = cfnlib.settingsOpen();
					if (opensettings) 
					{
						log.debug("Settings Opened");
						malta.detailsAppend("Settings Open", "Settings is Open ", "Settings is Open", "PASS");
						Thread.sleep(3000);
						boolean closeSettings = cfnlib.settingsBack();
						if (closeSettings) 
						{
							log.debug("Settings Closed");
							malta.detailsAppend("Settings Closed", "Settings is Closed ", "Settings is Closed","PASS");
						}
						else 
						{
							log.debug("Settings Closed");
							malta.detailsAppend("Settings Closed", "Settings is Closed ", "Settings is Closed","FAIL");
						}
						Thread.sleep(2000);
					} 
					else
					{
						malta.detailsAppend("Settings Open", "Settings is Open ", "Settings is Open", "FAIL");
					}

					
					// Verify Bet open & close
					boolean openBet = cfnlib.open_TotalBet();
					if (openBet)
					{
						log.debug("Bet Opend");
						malta.detailsAppend("Bet Open", "Bet is Open ", "Bet is Open", "PASS");
						Thread.sleep(2000);
						cfnlib.close_TotalBet();
						Thread.sleep(2000);
						log.debug("Bet Closed");
						malta.detailsAppend("Bet Closed", "Bet is Closed ", "Bet is Closed", "PASS");

					}
					else 
					{
						malta.detailsAppend("Bet Open", "Bet is Open ", "Bet is Open", "FAIL");
					}
					
					
					//Verify PayTable 
					if(!gameName.contains("Scratch") && cfnlib.checkAvilability(cfnlib.XpathMap.get("isMenuPaytableBtnVisible")))
					{
						cfnlib.capturePaytableScreenshot(malta,language);
						log.debug("Paytable opened");
						Thread.sleep(2000);
						cfnlib.paytableClose();	
						log.debug("Paytable closed"); 
						Thread.sleep(2000);
					}
				
					// Verify Help from Menu
					boolean help = cfnlib.maltaHelpFromTopBarMenu(malta);
					if (help) 
					{
						malta.detailsAppend("  Help from Menu ", "Help is Displayed and its Navigation", "Help is Displayed and its Navigation", "PASS");															
					}
					else 
					{
						malta.detailsAppend("  Help from Menu ", "Help is Displayed and its Navigation", "Help is Displayed and its Navigation", "FAIL");	
					}
					Thread.sleep(2000);
					
					// Verify Resposible Gaming from Menu
					boolean responsiblegaming = cfnlib.maltaResponsibleGamingFromTopBarMenu(malta);
					if (responsiblegaming) 
					{
						malta.detailsAppend(" Responsible Gaming from Menu ", "Responsible Gaming is Displayed and its Navigation", " Responsible Gaming is Displayed and its Navigation", "PASS"); 
					}
					else 
					{
						malta.detailsAppend(" Responsible Gaming from Menu ", "Responsible Gaming is Displayed and its Navigation", " Responsible Gaming is Displayed and its Navigation", "FAIL");
					}
					Thread.sleep(2000);
	
					// Verify  Game Hitory from Menu
					boolean gameHistoryFromMenu = cfnlib.maltaGameHistoryFromTopBarMenu(malta);
					if (gameHistoryFromMenu) 
					{
						malta.detailsAppend("Game History from Menu ", " Game History is Displayed and its Navigation", "Game History is Displayed and its Navigation", "PASS");
						
					}
					else 
					{
						malta.detailsAppend("Game History from Menu ", " Game History is Displayed and its Navigation", "Game History is Displayed and its Navigation", "FAIL");
					}
						

				    Thread.sleep(2000);
				    log.debug("!!!!!!! Done with Malta Market !!!!!!!");
				    log.debug("                                           ");

				
			}// Close if Loop

		}// Close Try Block
		
		// -------------------Handling the exception---------------------//

		catch (Exception e) 
		{
			log.error(e.getMessage(), e);
			cfnlib.evalException(e);
		}

		finally 
		{

			// Global.browserproxy.abort();
			// ---------- Closing the report----------//

			malta.endReport();
			// ---------- Closing the webdriver ----------//
			webdriver.close();

			webdriver.quit();

			// Global.appiumService.stop();
		}
	}

}
