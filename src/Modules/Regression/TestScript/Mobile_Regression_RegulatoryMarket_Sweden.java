
package Modules.Regression.TestScript;
import java.util.Map;

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

/**     Mobile ReelThunder "gtp 131"
 * * This script verifies the Sweden check points.
	 * =============================================
	 *  Launch the Sweden with EN & IT 
	 *  1. Verify session Reminder
	 *  2. Verify Game Name  
	 *  3. Verify Help Icon from Top Bar and Navigation 
	 *  4. Verify Reel Spin Duration 
	 *  5. Verify Currency Check  
	 *  6. Verify Top Bar Sweden  logs and Navigation 
	 *  7. Verify session Duration from  Top Bar
	 *  8. Compare Balance and Bet from Top Bar 
	 *  9. Menu open , Settings , Auto-play , Bet , PayTable 
	 *  10.Top Bar Menu : Help and Navigation 
	 *  11.Quick Spin Availability
	 *  12.Stop Button Availability 
     * 
 *   Session Duration is 4 Minutes
 */
public class  Mobile_Regression_RegulatoryMarket_Sweden

{
	
		public ScriptParameters scriptParameters;
		Logger log = Logger.getLogger(Mobile_Regression_RegulatoryMarket_Sweden.class.getName());
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
			String locator = null ;
			int mintDetailCount=0;
			//int mintSubStepNo=0;
			int mintPassed=0;
			int mintFailed=0;
			int mintWarnings=0;
			int mintStepNo=0;
			String userName ;
			String regMarket = "Sweden";
			int envId = 1658;
			int productId = 5143;
			String currencyIsoCode = "SEK";
			int marketTypeID = 6;
			double balance = 10000;
			
		
			Mobile_HTML_Report sweden =new Mobile_HTML_Report(webdriver,DeviceName,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,gameName);
			MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
			CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,sweden, gameName);
			CommonUtil util = new CommonUtil();
			cfnlib.setOsPlatform(scriptParameters.getOsPlatform()); //To get OSPlatform 
		
			try 
			{
			
				if (webdriver != null) 
				{	
					for(int i=1;i<3;i++)
					{
						userName=util.randomStringgenerator();
						util.randomUserCreationForRegMarkets(userName, regMarket, envId, productId,currencyIsoCode,marketTypeID);
						util.updateUserBalance(userName, balance);
						String url = cfnlib.xpathMap.get("Sweden_url");
						
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
								LaunchURl=	LaunchURl.replace("languagecode=en","languagecode=sv");
							else if(LaunchURl.contains("languageCode"))
								LaunchURl =LaunchURl.replace("languageCode=en","languageCode=sv");
							cfnlib.loadGame(LaunchURl);		
							log.debug("url = " +LaunchURl);
						}
						
						Thread.sleep(2000);
						
						
						
						if(framework.equalsIgnoreCase("Force"))
		                {
		                	cfnlib.setNameSpace();
		                }
											
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
		               
		                //waiting for spin button
		                cfnlib.waitForSpinButton(); 
					
		           	// Verify & Click on session reminder
					boolean sessionReminderVisible = cfnlib.sessionReminderPresent(sweden); // Verify if SessionReminder is present or not
					Thread.sleep(2000);
					if (sessionReminderVisible)
					{
						sweden.detailsAppend("Verify Session Reminder  ","Session Reminder is present and clicked on Continue Button ","Session Reminder is present and clicked on Continue Button", "PASS");
					} 
					else 
					{
						sweden.detailsAppend("Verify Session Reminder ","Session Reminder is not present and unable to click Continue Button","Session Reminder is not present and unable to click Continue Button", "FAIL");
					}

			
				// Verify TopBar is visble or not
					boolean isTopBarVisible = cfnlib.topBarVisibilitycheck();
					Thread.sleep(2000);

					if (isTopBarVisible)
					{
						sweden.detailsAppend(" TopBar", "TopBar is Displayed", "TopBar is Displayed", "PASS");
					}
					else 
					{

						sweden.detailsAppend(" TopBar", "TopBar is Displayed", "TopBar is  Displayed", "FAIL");
					}
				
					
					// Game name on the Top Bar
					String isGameName = cfnlib.gameNameFromTopBar(sweden);
					if (isGameName != null) 
					{
						sweden.detailsAppend(" Gamename  on Topbar", "Gamename is displayed", "Gamename is displayed","PASS");
					}
					else
					{
						sweden.detailsAppend(" Gamename  on Topbar", "Gamename isn't displayed","Gamename isn't displayed", "FAIL");
						Thread.sleep(2000);
					}
					
					// Verify if Top Bar bet with console balance
					String bet = cfnlib.betComparissionFromTopBar(); 
					if (bet != null) 
					{
						sweden.detailsAppend(" Compare Bet ", "Both are Same", "Both are Same", "PASS");

					}
					else 
					{
						sweden.detailsAppend(" Compare Bet ", "Both are Same", "Both are Same", "FAIL");
					}
					
					
					// Verify if Top Bar bet with console balance
					String balance1 = cfnlib.creditComparissionfromFromTopBar(); 
					if (balance1 != null) 
					{
						sweden.detailsAppend(" Compare Balance ", "Both are Same", "Both are Same", "PASS");

					}
					else 
					{
						sweden.detailsAppend(" Compare Balance ", "Both are Same", "Both are Same", "FAIL");
					}
					

					// Verify if Top Bar Session Duration time
			        String sessionDuration = cfnlib.sessionDurationFromTopBar ();
					if(sessionDuration != null)
					{
						sweden.detailsAppend(" Session Duration from Top Bar ", " Session Duration is Displayed"," Session Duration is Displayed", "PASS");
						Thread.sleep(3000);
					}
					
					else 
					{
						sweden.detailsAppend(" Session Duration from Top Bar ", " Session Duration is Displayed"," Session Duration is Displayed", "FAIL");	
					}
					

					// Reel Spin Duration
					long isReelspin=cfnlib.reelSpinDuratioN(sweden);
						if(isReelspin >= 3000)
						{
							if(isReelspin <= 4000)
							{
							     log.debug("Reel Spin Duration , STATUS : PASS");
							     sweden.detailsAppend(" Reel Spin is less than greater than Four and less than Three  ", "Reel Spin Duration in Sec is :" +isReelspin, "Reel Spin Duration  in Sec is :" +isReelspin, "PASS");
							}
							else
							{
								log.debug("Reel Spin Duration , STATUS : FAIL");
								sweden.detailsAppend("Reel Spin is less than greater than Four and less than Three ", "Reel Spin Duration in Sec is :" +isReelspin, "Reel Spin Duration  in Sec is :" +isReelspin, "FAIL");	
							}
						}
						else
						{
							log.debug("Reel Spin Duration , STATUS : FAIL");
							sweden.detailsAppend("Reel Spin is less than greater than Four and less than Three ", "Reel Spin Duration in Sec is :" +isReelspin, "Reel Spin Duration  in Sec is :" +isReelspin, "FAIL");
						}
						
							
					

					//Sweden currency check 
					boolean curencycheck =cfnlib.swedenCurrencyCheck(sweden);
					if (curencycheck)
					{
					    log.debug("YES, The Currency is Same ");
						sweden.detailsAppend("Verify Currency","Currency is Same", "Currency is Same","PASS");
						
					}
					else
					{
					    log.debug("No, The Currency is Different ");
						sweden.detailsAppend("Verify Currency","Currency is not Same", "Currency is not Same","FAIL");
						
					}
					
					
                   //Verify the quickspin button availability in Italy Market
					
					boolean isquickspin=cfnlib.verifyQuickSpinAvailablity();
					if(isquickspin)
					{	
						sweden.detailsAppend(" Quickspin ", "Quickspin is available ", "Quickspin is available", "FAIL");
					}
					else
					{
						sweden.detailsAppend(" Quickspin ", "Quickspin is not available ", "Quickspin is not available", "PASS");
					}	
					Thread.sleep(2000);
					
					
					//Verify the stop button availability in Italy Market
					
					boolean isstopbtn=cfnlib.verifyStopButtonAvailablity();
					
					if(isstopbtn)
					{	
						sweden.detailsAppend(" Stop Button  ", "Stop Button  is available ", "Stop Button  is available", "FAIL");
					}
					else
					{
						sweden.detailsAppend(" Stop Button ", "Stop Button is not available ", "Stop Button is not available", "PASS");
					}	
					Thread.sleep(2000);
					
					// Menu open & close
					boolean menuopen = cfnlib.menuOpen();
					if (menuopen) 
					{
						log.debug("Menu Opend");
						sweden.detailsAppend("Menu Open", "Menu is Open ", "Menu is Open", "PASS");
						Thread.sleep(3000);
						boolean closemenu = cfnlib.menuClose();
						if (closemenu)
						{
							log.debug("Menu Closed");
							sweden.detailsAppend("Menu Closed", "Menu is Closed ", "Menu is Closed", "PASS");
						} 
						else
						{
							log.debug("Menu Closed");
							sweden.detailsAppend("Menu Closed", "Menu is Closed ", "Menu is Closed", "FAIL");
						}
						Thread.sleep(2000);
					} 
					else
					{
						sweden.detailsAppend("Menu Open", "Menu is Open ", "Menu is Open", "FAIL");
					}

					// Verify Settings open & close
					boolean opensettings = cfnlib.settingsOpen();
					if (opensettings) 
					{
						log.debug("Settings Opendd");
						sweden.detailsAppend("Settings Open", "Settings is Open ", "Settings is Open", "PASS");
						Thread.sleep(3000);
						boolean closeSettings = cfnlib.settingsBack();
						if (closeSettings) 
						{
							log.debug("Settings Closed");
							sweden.detailsAppend("Settings Closed", "Settings is Closed ", "Settings is Closed","PASS");
						}
						else 
						{
							log.debug("Settings Closed");
							sweden.detailsAppend("Settings Closed", "Settings is Closed ", "Settings is Closed","FAIL");
						}
						Thread.sleep(2000);
					} 
					else
					{
						sweden.detailsAppend("Settings Open", "Settings is Open ", "Settings is Open", "FAIL");
					}

					// Verify Autoplay open & close
					boolean openAutoplay = cfnlib.open_Autoplay();
					if (openAutoplay) 
					{
						log.debug("Autoplay Opend");
						sweden.detailsAppend("Autoplay Open", "Autoplay is Open ", "Autoplay is Open", "PASS");
						Thread.sleep(3000);
						cfnlib.close_Autoplay();
						Thread.sleep(2000);
						log.debug("Autoplay Closed");
						sweden.detailsAppend("Autoplay Closed", "Autoplay is Closed ", "Autoplay is Closed", "PASS");

					}
					else 
					{
						sweden.detailsAppend("Autoplay Open", "Autoplay is Open ", "Autoplay is Open", "FAIL");
					}

					// Verify Bet open & close
					boolean openBet = cfnlib.openTotalBet();
					if (openBet)
					{
						log.debug("Bet Opend");
						sweden.detailsAppend("Bet Open", "Bet is Open ", "Bet is Open", "PASS");
						Thread.sleep(2000);
						cfnlib.closeTotalBet();
						Thread.sleep(2000);
						log.debug("Bet Closed");
						sweden.detailsAppend("Bet Closed", "Bet is Closed ", "Bet is Closed", "PASS");

					}
					else 
					{
						sweden.detailsAppend("Bet Open", "Bet is Open ", "Bet is Open", "FAIL");
					}
					
					//Verify PayTable 
						cfnlib.capturePaytableScreenshot(sweden,language);
						log.debug("Paytable opened");
						Thread.sleep(2000);
						cfnlib.paytableClose();	
						log.debug("Paytable closed"); 
						Thread.sleep(2000);
				
		                
		             // Help Icon on the Top Bar & Navigation 
						boolean help = cfnlib.helpIcon(sweden);
						if (help)
						{
							sweden.detailsAppend(" Help on Topbar", "Help is Displayed", "Help is Displayed", "PASS");
						} 
						else
						{
							sweden.detailsAppend(" Help on Topbar", "Help is Displayed", "Help is Displayed", "FAIL");
							
						}
						Thread.sleep(2000);
							
						
						// Verify if Help Text from Menu is present or not
						boolean menu = cfnlib.swedenTopBarMenu(sweden);
						if (menu) 
						{
							sweden.detailsAppend("Verify Top Bar Help and Navigation ","Help is clicked and navigated back to base game ","Help is clicked and navigated back to base game ", "PASS");
							
						}

						else 
						{
							sweden.detailsAppend("Verify Top Bar Help and Navigation ","Help is clicked and navigated back to base game ","Help is clicked and navigated back to base game ", "FAIL");
						}
                  	
					// Verify TopBar logos is visble or not
					boolean logosFromTopBar = cfnlib.swedenLogosFromTopBar(sweden);
					Thread.sleep(2000);
					if (logosFromTopBar) 
					{
							sweden.detailsAppend(" Logos", "All three Logos are Displayed", "All three Logos are Displayed", "PASS");
					}
					else 
					{
							sweden.detailsAppend(" Logos", "All three Logos are not Displayed", "All three Logos are not Displayed", "PASS");
					}
						 Thread.sleep(2000);
		  
				
					
				System.out.println(" !!!!!!!!!!!!! Done with Swewden Market !!!!!!!!!!!!!!!! ");
			    System.out.println("                                           ");

					} // Close Finally 
					
				}// Close For Loop
				
			} // Close Try Block 
			//TRY
		//-------------------Handling the exception---------------------//
			
			catch (Exception e)
			{
				log.error(e.getMessage(),e);
				cfnlib.evalException(e);
			}
			finally 
			{
				// Global.browserproxy.abort();
				// ---------- Closing the report----------//

				sweden.endReport();
				System.out.println("Report Ended");
				// ---------- Closing the webdriver ----------//

			    webdriver.close();
				System.out.println("Webdriver Closed");
				webdriver.quit();
				System.out.println("Webdriver quit");
				// Global.appiumService.stop();

			}	
	}//method
}
