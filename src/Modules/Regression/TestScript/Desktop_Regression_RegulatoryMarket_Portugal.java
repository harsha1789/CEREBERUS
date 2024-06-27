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

public class Desktop_Regression_RegulatoryMarket_Portugal
{

	/**
	 * Desktop ReelThunder "gtp 95" 
	 * This script verifies the Portugal check points & below are the following .
	 * =======================================================================
	 * 1.Launch the Portugal with EN & pt 
	 * 2.Verify Currency Format
	 * 3.Verify Reel Spin Duration 
	 * 4.Verify TopBar Visibility
	 * 5.Verify Clock from TopBar
	 * 6.Verify Game Name from TopBar
	 * 7.Verify Quick Spin is Available
	 * 8.Verify Stop is Available
	 * 9.Verify Autoplay 
	 * 10.Verify Menu , Settings, Bet & Pay-table 
	 * 11.Verify (Help & Game History) from Menu & its Navigation 
	 *  
	 * 
	 *
	 */

	public ScriptParameters scriptParameters;
	Logger log = Logger.getLogger(Desktop_Regression_RegulatoryMarket_Portugal.class.getName());
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
		String language = "PortugalPaytable";
		int mintDetailCount=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		String userName="" ;
		String regMarket = "Portugal"; 
		int envid = 1658;
		int productId = 5528;
		String currencyIsoCode = "EUR";
		int marketTypeID = 27;
		double balance = 10000;
		String lobbyname= "PortugalQuickfire";
		String languages[]= {"en","pt"};
		
		Desktop_HTML_Report portugal = new Desktop_HTML_Report(webdriver, browserName, filePath, startTime, mstrTCName,mstrTCDesc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings, mintStepNo, status,gameName);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,portugal, gameName);
		CommonUtil util = new CommonUtil();
		int moduleId=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int  clientId=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDDesktop"));
		String gameVersion = TestPropReader.getInstance().getProperty("gameVersion");
		
		try 
		{
			if (webdriver != null) 
			{
				for(int i=0;i<languages.length;i++)
				{
				userName=util.quickFireRandomStringgenerator();
				System.out.println(userName);
				//envid=Integer.parseInt(TestPropReader.getInstance().getProperty("EnvironmentID"));
				//util.updateUserBalance(userName, balance);
				util.quickfireGamesCreateURL( userName, gameVersion, moduleId,  clientId,lobbyname,"desktop");
				String url = cfnlib.XpathMap.get("portugal_URL");		
               // String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)", "externalToken="+userName+"$1");
                String LaunchURl = url.replace("externalToken=token1","externalToken="+userName);
				if(LaunchURl.contains("languagecode"))
				{
				 LaunchURl=	LaunchURl.replace("languagecode=en","languagecode="+languages[i]);
				 cfnlib.loadGame(LaunchURl);log.debug("url = " +LaunchURl);
				}
				else if(LaunchURl.contains("languageCode"))
				{
				    LaunchURl=	LaunchURl.replace("languageCode=en","languageCode="+languages[i]);
				    cfnlib.loadGame(LaunchURl);log.debug("url = " +LaunchURl);
				}
				else
				{
					String launchUrl =LaunchURl;
                    String langCode = "&languageCode="+languages[i];
                    String LaunchURL = launchUrl.concat(langCode);
					cfnlib.loadGame(LaunchURL);log.debug("url = " +LaunchURL);
				}
				
					
					if (framework.equalsIgnoreCase("Force")) 
					{
						cfnlib.setNameSpace();
						log.debug("Force Games");
					}
					Thread.sleep(2000);
					
				
			      // Currency Check
					boolean curencycheck = cfnlib.portugalCurrencyCheck(portugal);
					if (curencycheck)
					{
						log.debug("YES, The Currency is SAME");
						portugal.detailsAppend(" Currency", "Currency is Same", "Currency is Same", "PASS");
					}
					else 
					{
						log.debug("NO, The Currency is Difference");
						portugal.detailsAppend(" Currency", "Currency is Same", "Currency is Same", "FAIL");
					}
			
				
					//To find the reel spin duration for a single spin	
					long isReelspin=cfnlib.reelSpinDuratioN(portugal);
					if(isReelspin >= 3000)
					{
						log.debug("Reel Spin Duration , STATUS : PASS");
						portugal.detailsAppend( "Reel Spin Duration ", "Reel Spin Duration in Sec is :" +isReelspin, "Reel Spin Duration  in Sec is :" +isReelspin, "PASS");
					}
					else
					{
						log.debug("Reel Spin Duration , STATUS : FAIL");
						portugal.detailsAppend("Reel Spin Duration ", "Reel Spin Duration in Sec is :" +isReelspin, "Reel Spin Duration  in Sec is :" +isReelspin, "FAIL");
					}
					
					// Verify TopBar is visble or not
					boolean isTopBarVisible = cfnlib.verifyTopBarVisible();
					Thread.sleep(2000);
					if (isTopBarVisible)
					{
						portugal.detailsAppend("Verify TopBar ", "TopBar is Displayed", "TopBar is Displayed", "PASS");
					}
					else 
					{
						log.debug("Topbar is not visible");
						portugal.detailsAppend("Verify TopBar ", "TopBar is Displayed", "TopBar is Displayed", "FAIL");
					}
					
					//Game name on the Top Bar 
					String isGameName=cfnlib.gameNameFromTopBar(portugal);
					if(isGameName != null)
					{
						portugal.detailsAppend(" Gamename  on Topbar", "Gamename is displayed", "Gamename is displayed", "PASS");
					}
					else
					{
						portugal.detailsAppend(" Gamename  on Topbar", "Gamename isn't displayed", "Gamename isn't displayed", "FAIL");
						Thread.sleep(2000);
					}

					
					// Clock on the Top Bar 
					boolean clock = cfnlib.clockFromTopBar(portugal);
					if (clock )
					{
						portugal.detailsAppend(" Clock on Topbar", "Clock is Displayed ", "Clock is Displayed", "PASS");
					} 
					else
					{
						portugal.detailsAppend(" Clock on Topbar", "Clock is Displayed", "Clock is Displayed ", "FAIL");
					}
					
					
					// Verify the Autoplay button availability in germany Market 

					boolean isAutoplay = cfnlib.verifyAutoplayAvailabilty();
					Thread.sleep(1000);
					if (isAutoplay) 
					{
						portugal.detailsAppend(" Autoplay ", "Autoplay is available ", "Autoplay is available", "PASS");
					}
					else
					{
						portugal.detailsAppend(" Autoplay ", "Autoplay is not available ", "Autoplay is not available", "FAIL");
					}
					
					// Verify the stop button availability in germany Market 

					boolean isstopbtn = cfnlib.verifyStopButtonAvailablity();
					Thread.sleep(1000);
					if (isstopbtn) 
					{
						portugal.detailsAppend("Stop button", "Stop button is Available", "Stop button is Available","FAIL");
					}
					else 
					{
						portugal.detailsAppend("Stop button", "Stop button is not Available", "Stop button is not  Available","PASS");
					}
					Thread.sleep(2000);
					
					
					// Verify the quickspin button availability in germany Market 

					boolean isquickspin = cfnlib.verifyQuickspinAvailablity();
					Thread.sleep(1000);
					if (isquickspin) 
					{
						portugal.detailsAppend("Quickspin ", "Quickspin is  Available ", "Quickspin is Available", "FAIL");
					}
					else
					{
						portugal.detailsAppend("Quickspin ", "Quickspin is not  Available ", "Quickspin is not Available", "PASS");
					}
					Thread.sleep(2000);

					
					// Menu open & close
					boolean menuopen = cfnlib.menuOpen();
					if (menuopen) 
					{
						log.debug("Menu Opend");
						portugal.detailsAppend("Menu Open", "Menu is Open ", "Menu is Open", "PASS");
						Thread.sleep(3000);
						boolean closemenu = cfnlib.menuClose();
						if (closemenu)
						{
							log.debug("Menu Closed");
							portugal.detailsAppend("Menu Closed", "Menu is Closed ", "Menu is Closed", "PASS");
						} 
						else
						{
							log.debug("Menu Closed");
							portugal.detailsAppend("Menu Closed", "Menu is Closed ", "Menu is Closed", "FAIL");
						}
						Thread.sleep(2000);
					} 
					else
					{
						portugal.detailsAppend("Menu Open", "Menu is Open ", "Menu is Open", "FAIL");
					}

					// Verify Settings open & close
					boolean opensettings = cfnlib.settingsOpen();
					if (opensettings) 
					{
						log.debug("Settings Opened");
						portugal.detailsAppend("Settings Open", "Settings is Open ", "Settings is Open", "PASS");
						Thread.sleep(3000);
						boolean closeSettings = cfnlib.settingsBack();
						if (closeSettings) 
						{
							log.debug("Settings Closed");
							portugal.detailsAppend("Settings Closed", "Settings is Closed ", "Settings is Closed","PASS");
						}
						else 
						{
							log.debug("Settings Closed");
							portugal.detailsAppend("Settings Closed", "Settings is Closed ", "Settings is Closed","FAIL");
						}
						Thread.sleep(2000);
					} 
					else
					{
						portugal.detailsAppend("Settings Open", "Settings is Open ", "Settings is Open", "FAIL");
					}

					
					// Verify Bet open & close
					boolean openBet = cfnlib.open_TotalBet();
					if (openBet)
					{
						log.debug("Bet Opend");
						portugal.detailsAppend("Bet Open", "Bet is Open ", "Bet is Open", "PASS");
						Thread.sleep(2000);
						cfnlib.close_TotalBet();
						Thread.sleep(2000);
						log.debug("Bet Closed");
						portugal.detailsAppend("Bet Closed", "Bet is Closed ", "Bet is Closed", "PASS");

					}
					else 
					{
						portugal.detailsAppend("Bet Open", "Bet is Open ", "Bet is Open", "FAIL");
					}
					
					
					//Verify PayTable 
					if(!gameName.contains("Scratch") && cfnlib.checkAvilability(cfnlib.XpathMap.get("isMenuPaytableBtnVisible")))
					{
						cfnlib.capturePaytableScreenshot(portugal,language);
						log.debug("Paytable opened");
						Thread.sleep(2000);
						cfnlib.paytableClose();	
						log.debug("Paytable closed"); 
						Thread.sleep(2000);
					}
				
					// Verify Help from Menu
					boolean help = cfnlib.portugalHelpFromTopBarMenu(portugal);
					if (help) 
					{
						portugal.detailsAppend("  Help from Menu ", "Help is Displayed and its Navigation", "Help is Displayed and its Navigation", "PASS");															
					}
					else 
					{
						portugal.detailsAppend("  Help from Menu ", "Help is Displayed and its Navigation", "Help is Displayed and its Navigation", "FAIL");	
					}
					Thread.sleep(2000);
					
	
					// Verify  Game Hitory from Menu
					boolean gameHistoryFromMenu = cfnlib.portugalGameHistoryFromTopBarMenu(portugal);
					if (gameHistoryFromMenu) 
					{
						portugal.detailsAppend("Game History from Menu ", " Game History is Displayed and its Navigation", "Game History is Displayed and its Navigation", "PASS");
						
					}
					else 
					{
						portugal.detailsAppend("Game History from Menu ", " Game History is Displayed and its Navigation", "Game History is Displayed and its Navigation", "FAIL");
					}
						

				    Thread.sleep(2000);
				    log.debug("!!!!!!! Done with Portugal Market !!!!!!!");
				    log.debug("                                           ");	
				}//close for loop
				
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

			portugal.endReport();
			// ---------- Closing the webdriver ----------//
			webdriver.close();

			webdriver.quit();

			// Global.appiumService.stop();
		}
	}

}
