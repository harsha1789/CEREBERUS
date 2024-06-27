package Modules.Regression.TestScript;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.library.CommonUtil;

import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 * Mobile ReelThunder "gtp 131" 
 * This script verifies the Malta check points & below are the following .
 * =======================================================================
  * Launch the Malta with EN  
	 * 1.Verify Session Reminder(4 Mins)
	 * 2.Verify Currency Format
	 * 3.Verify Reel Spin Duration 
	 * 4.Verify TopBar Visibility
	 * 5.Verify Clock & Game Name from TopBar
	 * 6.Verify Help Icon from TopBar and its Navigation
	 * 7.Verify Quick Spin is Available
	 * 8.Verify Stop is Available
	 * 9.Verify Menu , Settings, Bet & Pay-table 
	 * 10.Verify (Help,Responsible Gaming & Game History) from Menu & its Navigation 
 * 
 *
 */
public class  Mobile_Regression_RegulatoryMarket_Malta

{
		public ScriptParameters scriptParameters;
		Logger log = Logger.getLogger(Mobile_Regression_RegulatoryMarket_Malta.class.getName());
		public void script() throws Exception 
		{
			
			String mstrTC_Name=scriptParameters.getMstrTCName();
			String mstrTC_Desc=scriptParameters.getMstrTCDesc();
			String mstrModuleName=scriptParameters.getMstrModuleName();
			BrowserMobProxyServer proxy=scriptParameters.getProxy();
			String startTime=scriptParameters.getStartTime();
			String filePath=scriptParameters.getFilePath();
			AppiumDriver<WebElement> webdriver=scriptParameters.getAppiumWebdriver();
			
			String DeviceName=scriptParameters.getDeviceName();
			String framework=scriptParameters.getFramework();
			String gameName=scriptParameters.getGameName();
			String language ="Sweden";
			String Status=null;
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
			
			
			Mobile_HTML_Report malta =new Mobile_HTML_Report(webdriver,DeviceName,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,gameName);
			MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
			CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,malta, gameName);
			CommonUtil util = new CommonUtil();
			cfnlib.setOsPlatform(scriptParameters.getOsPlatform()); //To get OSPlatform 
			
			try 
			{
				if (webdriver != null) 
				{
					
					//userName=util.randomStringgenerator();
					//util.randomUserCreationForRegMarkets(userName, regMarket, envId, productId,currencyIsoCode,marketTypeID);
					//util.updateUserBalance(userName, balance);
					String url = cfnlib.xpathMap.get("Portugal_url");
					//String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
					cfnlib.loadGame(url);	
					log.debug("url = " +url);
						
						Thread.sleep(2000);
						
						if (framework.equalsIgnoreCase("Force")) 
						{
							cfnlib.setNameSpace();
							log.debug("Force Games");

						}
						Thread.sleep(2000);
						
						 //Full screen overlay 	
		                if(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame"))||(Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad"))))
		                {
		                	//cfnlib.newFeature();
		                	Thread.sleep(1000);
		                	cfnlib.funcFullScreen();
		                }
		                else
		                {
		                	cfnlib.funcFullScreen();
		                	Thread.sleep(1000);
		                	//cfnlib.newFeature();
		                }

		                 
	                  //session reminder
						boolean sessionReminderVisible=cfnlib.sessionReminderPresent(malta);
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

						// Verify TopBar is visible or not
						boolean isTopBarVisible = cfnlib.topBarVisibilitycheck();
						Thread.sleep(2000);

						if (isTopBarVisible)
						{
							log.debug("Topbar is visible");
							malta.detailsAppend("Verify TopBar ", "TopBar is Displayed", "TopBar is Displayed", "PASS");
						}
						else 
						{
							log.debug("Topbar is not visible");
							malta.detailsAppend("Verify TopBar ", "TopBar is Displayed", "TopBar is Displayed", "FAIL");
						}
						
						// Game name on the Top Bar
						String isGameName = cfnlib.gameNameFromTopBar(malta);
						if (isGameName != null) 
						{
							malta.detailsAppend(" Gamename  on Topbar", "Gamename is displayed", "Gamename is displayed","PASS");
						}
						else
						{
							malta.detailsAppend(" Gamename  on Topbar", "Gamename isn't displayed","Gamename isn't displayed", "FAIL");
							Thread.sleep(2000);
						}

						
						// Clock on the Top Bar 
						String clock = cfnlib.clockFromTopBar();
						if (clock != null && !clock.equalsIgnoreCase(""))
						{
							malta.detailsAppend(" Clock on Topbar", "Clock is Displayed ", "Clock is Displayed", "PASS");
						} 
						else
						{
							malta.detailsAppend(" Clock on Topbar", "Clock is Displayed", "Clock is Displayed ", "FAIL");
						}

						// Help Icon on the Top Bar & Navigation 
						boolean help = cfnlib.helpIcon(malta);
						if (help)
						{
							malta.detailsAppend(" Help on Topbar", "Help is displayed", "Help is displayed", "PASS");
						} 
						else
						{
							malta.detailsAppend(" Help on Topbar", "Help is displayed", "Help is displayed", "FAIL");
							
						}
						
						
						// Verify the quick spin button availability in Germany Market 

						boolean isquickspin = cfnlib.isQuickspinAvailable();
						Thread.sleep(1000);
						if (isquickspin) 
						{
							malta.detailsAppend(" Quickspin ", "Quickspin is available ", "Quickspin is available", "PASS");
						}
						else
						{
							malta.detailsAppend(" Quickspin ", "Quickspin is not available ", "Quickspin is not available", "FAIL");
						}
						Thread.sleep(2000);

						// Verify the stop button availability in Germany Market 

						boolean isstopbtn = cfnlib.isStopButtonAvailable();
						Thread.sleep(1000);
						if (isstopbtn) 
						{
							malta.detailsAppend("Stop button", "Stop button is available", "Stop button is available","PASS");
						}
						else 
						{
							malta.detailsAppend("Stop button", "Stop button is not available", "Stop button is not available","FAIL");
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
								malta.detailsAppend("Menu Close", "Menu is Closed ", "Menu is Closed", "PASS");
							} 
							else
							{
								log.debug("Menu Closed");
								malta.detailsAppend("Menu Close", "Menu is Closed ", "Menu is Closed", "FAIL");
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
							log.debug("Settings Opend");
							malta.detailsAppend("Settings Open", "Settings is Open ", "Settings is Open", "PASS");
							Thread.sleep(3000);
							boolean closeSettings = cfnlib.settingsBack();
							if (closeSettings) 
							{
								log.debug("Settings closed");
								malta.detailsAppend("Settings closed", "Settings is Closed ", "Settings is Closed","PASS");
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
						boolean openBet = cfnlib.openTotalBet();
						if (openBet)
						{
							log.debug("Bet Opend");
							malta.detailsAppend("Bet Open", "Bet is Open ", "Bet is Open", "PASS");
							Thread.sleep(2000);
							cfnlib.closeTotalBet();
							Thread.sleep(2000);
							log.debug("Bet Closed");
							malta.detailsAppend("Bet Closed", "Bet is Closed ", "Bet is Closed", "PASS");
						}
						else 
						{
							malta.detailsAppend("Bet Open", "Bet is Open ", "Bet is Open", "FAIL");
						}
						
						//Verify PayTable
		                if(!gameName.contains("Scratch") && cfnlib.checkAvilability(cfnlib.xpathMap.get("isMenuPaytableBtnVisible")))
		                {
							cfnlib.capturePaytableScreenshot(malta,language);
							log.debug("Paytable opened");
							Thread.sleep(2000);
							cfnlib.paytableClose();	
							log.debug("Paytable closed"); 
							Thread.sleep(2000);
		                }
				
						// Verify Help from Menu
						boolean helpfromMenu = cfnlib.maltaHelpFromTopBarMenu(malta);
						if (helpfromMenu) 
						{
							malta.detailsAppend("Help from Menu ","Help is clicked and navigated back to base game ","Help is clicked and navigated back to base game ", "PASS");																
						}
						else 
						{
							malta.detailsAppend("Verify Top Bar Help and Navigation ","Help is clicked and navigated back to base game ","Help is clicked and navigated back to base game ", "FAIL");
						}
						Thread.sleep(2000);
					
						
						// Verify  Game Hitory from Menu
						boolean gameHistoryFromMenu = cfnlib.maltaGameHistoryFromTopBarMenu(malta);
						if (gameHistoryFromMenu) 
						{
							malta.detailsAppend("Game History from Menu ", " Game History is clicked and navigated back to base game ", " Game History is clicked and navigated back to base game ", "PASS");	
						}
						else 
						{
							malta.detailsAppend("Game History from Menu ", " Game History is clicked and navigated back to base game ", " Game History is clicked and navigated back to base game ", "FAIL");
						}
						
						Thread.sleep(2000);
						
						// Verify Resposible Gaming from Menu
						boolean responsiblegaming = cfnlib.maltaResponsibleGamingFromTopBarMenu(malta);
						if (responsiblegaming) 
						{
							malta.detailsAppend("ResponsibleGaming from Menu ", "ResponsibleGaming is clicked and navigated back to base game ", "ResponsibleGaming is clicked and navigated back to base game ", "PASS");
							
						}
						else 
						{
							malta.detailsAppend("ResponsibleGaming from Menu ", "ResponsibleGaming is clicked and navigated back to base game ", "ResponsibleGaming is clicked and navigated back to base game ", "FAIL");
						}
						Thread.sleep(2000);
					
						log.debug("!!!!!!! Done with Germany Market !!!!!!!");
						log.debug("                                           ");
                  
				}// Close if Loop
				
			} // Close Try Block
		
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
