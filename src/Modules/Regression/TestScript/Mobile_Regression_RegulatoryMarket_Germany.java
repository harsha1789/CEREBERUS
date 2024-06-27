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
 * This script verifies the Germany check points & below are the following .
 * =======================================================================
 * Launch the Germany with EN & DA
 * 1.Verify Session Reminder(4 Minutes)
 * 2.Verify Currency Format
 * 3.Verify Reel Spin Duration 
 * 4.Verify TopBar Visibility
 * 5.Verify Clock from TopBar
 * 6.Verify Help Text from TopBar and its Navigation
 * 7.Verify Auto-play 
 * 8.Verify Quick Spin
 * 9.Verify Stop 
 * 10.Verify Menu , Settings, Bet & Pay-table 
 * 11.Verify (Help,Responsible Gaming & Game History) from the Menu & its Navigation 
 * 
 *
 */
public class  Mobile_Regression_RegulatoryMarket_Germany

{
		public ScriptParameters scriptParameters;
		Logger log = Logger.getLogger(Mobile_Regression_RegulatoryMarket_Germany.class.getName());
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
			String regMarket = "Germany";
			int envId = 1658;
			int productId = 5159;
			String currencyIsoCode = "EUR";
			int marketTypeID = 31;
			double balance = 10000;
			
			
			Mobile_HTML_Report germany =new Mobile_HTML_Report(webdriver,DeviceName,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,gameName);
			MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
			CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,germany, gameName);
			CommonUtil util = new CommonUtil();
			cfnlib.setOsPlatform(scriptParameters.getOsPlatform()); //To get OSPlatform 
			
			
			try 
			{
				if (webdriver != null) 
				{
					for (int i = 1; i < 3; i++)
					{
						userName=util.randomStringgenerator();
						util.randomUserCreationForRegMarkets(userName, regMarket, envId, productId,currencyIsoCode,marketTypeID);
						util.updateUserBalance(userName, balance);
						String url = cfnlib.xpathMap.get("Germany_url");
						
						if(i==1)
						{	
							String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
							cfnlib.loadGame(LaunchURl);	
							log.debug("url = " +LaunchURl);
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
		                	cfnlib.newFeature();
		                	Thread.sleep(1000);
		                	cfnlib.funcFullScreen();
		                }
		                else
		                {
		                	cfnlib.funcFullScreen();
		                	Thread.sleep(1000);
		                	cfnlib.newFeature();
		                }

		                 
	                	//session reminder
						boolean sessionReminderVisible=cfnlib.sessionReminderPresent(germany);
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

						// Verify TopBar is visible or not
						boolean isTopBarVisible = cfnlib.topBarVisibilitycheck();
						Thread.sleep(2000);

						if (isTopBarVisible)
						{
							log.debug("Topbar is visible");
							germany.detailsAppend("Verify TopBar ", "TopBar is Displayed", "TopBar is Displayed", "PASS");
						}
						else 
						{
							log.debug("Topbar is not visible");
							germany.detailsAppend("Verify TopBar ", "TopBar is Displayed", "TopBar is Displayed", "FAIL");
						}

						
						// Clock on the Top Bar 
						String clock = cfnlib.clockFromTopBar();
						if (clock != null && !clock.equalsIgnoreCase(""))
						{
							germany.detailsAppend(" Clock on Topbar", "Clock is Displayed ", "Clock is Displayed", "PASS");
						} 
						else
						{
							germany.detailsAppend(" Clock on Topbar", "Clock is Displayed", "Clock is Displayed ", "FAIL");
						}

						// Help Icon on the Top Bar & Navigation 
						boolean help = cfnlib.helpIcon(germany);
						if (help)
						{
							germany.detailsAppend(" Help on Topbar", "Help is displayed", "Help is displayed", "PASS");
						} 
						else
						{
							germany.detailsAppend(" Help on Topbar", "Help is displayed", "Help is displayed", "FAIL");
							
						}
						
						
						// Verify the Auto-play button availability in Germany Market 

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
						
						
						// Verify the quick spin button availability in Germany Market 

						boolean isquickspin = cfnlib.verifyQuickSpinAvailablity();
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

						// Verify the stop button availability in Germany Market 

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
								germany.detailsAppend("Menu Close", "Menu is Closed ", "Menu is Closed", "PASS");
							} 
							else
							{
								log.debug("Menu Closed");
								germany.detailsAppend("Menu Close", "Menu is Closed ", "Menu is Closed", "FAIL");
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
							log.debug("Settings Opend");
							germany.detailsAppend("Settings Open", "Settings is Open ", "Settings is Open", "PASS");
							Thread.sleep(3000);
							boolean closeSettings = cfnlib.settingsBack();
							if (closeSettings) 
							{
								log.debug("Settings closed");
								germany.detailsAppend("Settings closed", "Settings is Closed ", "Settings is Closed","PASS");
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
						boolean openBet = cfnlib.openTotalBet();
						if (openBet)
						{
							log.debug("Bet Opend");
							germany.detailsAppend("Bet Open", "Bet is Open ", "Bet is Open", "PASS");
							Thread.sleep(2000);
							cfnlib.closeTotalBet();
							Thread.sleep(2000);
							log.debug("Bet Closed");
							germany.detailsAppend("Bet Closed", "Bet is Closed ", "Bet is Closed", "PASS");
						}
						else 
						{
							germany.detailsAppend("Bet Open", "Bet is Open ", "Bet is Open", "FAIL");
						}
						
						//Verify PayTable
		                if(!gameName.contains("Scratch") && cfnlib.checkAvilability(cfnlib.xpathMap.get("isMenuPaytableBtnVisible")))
		                {
							cfnlib.capturePaytableScreenshot(germany,language);
							log.debug("Paytable opened");
							Thread.sleep(2000);
							cfnlib.paytableClose();	
							log.debug("Paytable closed"); 
							Thread.sleep(2000);
		                }
				
						// Verify Help from Menu
						boolean helpfromMenu = cfnlib.germanyHelpFromTopBarMenu(germany);
						if (helpfromMenu) 
						{
							germany.detailsAppend("Verify Top Bar Help and Navigation ",
									"Help is clicked and navigated back to base game ",
									"Help is clicked and navigated back to base game ", "PASS");																
						}
						else 
						{
							germany.detailsAppend("Verify Top Bar Help and Navigation ",
									"Help is clicked and navigated back to base game ",
									"Help is clicked and navigated back to base game ", "FAIL");
						}
						Thread.sleep(2000);
						
						// Verify  Game Hitory from Menu
						boolean gameHistoryFromMenu = cfnlib.germanyGameHistoryFromTopBarMenu(germany);
						if (gameHistoryFromMenu) 
						{
							germany.detailsAppend("  Game History from Menu ", " Game History is Displayed", " Game History is Displayed", "PASS");	
						}
						else 
						{
							germany.detailsAppend(" Game History from Menu ", " Game History is not Displayed", " Game History is not Displayed", "PASS");
						}
						

						Thread.sleep(2000);
						
						// Verify Resposible Gaming from Menu
						boolean responsiblegaming = cfnlib.germanyResponsibleGamingFromTopBarMenu(germany);
						if (responsiblegaming) 
						{
							germany.detailsAppend(" ResponsibleGaming from Menu ", "ResponsibleGaming is Displayed", " Responsible Gaming is Displayed", "PASS");
							
						}
						else 
						{
							germany.detailsAppend(" ResponsibleGaming from Menu ", " ResponsibleGaming is Displayed", " ResponsibleGaming is Displayed", "FAIL");
						}
						
						
					
						log.debug("!!!!!!! Done with Germany Market !!!!!!!");
						log.debug("                                           ");

                    } // Close Finally 
					
				}// Close For Loop
				
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

				germany.endReport();
				// ---------- Closing the webdriver ----------//
				webdriver.close();

				webdriver.quit();

				// Global.appiumService.stop();
			}
		}

	}
