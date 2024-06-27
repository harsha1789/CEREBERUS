package Modules.Regression.TestScript;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import net.lightbody.bmp.BrowserMobProxyServer;


/**
 * Mobile ReelThunder "gtp 95" 
 * This script verifies the Portugal check points & below are the following .
 * =======================================================================
 * 1. Launch the Portugal with EN  & pt
 * 2.Verify Currency Format
 * 3.Verify Reel Spin Duration 
 * 4.Verify TopBar Visibility
 * 5.Verify Clock from TopBar
 * 6.Verify Game Name from TopBar
 * 7.Verify Quick Spin is Available
 * 8.Verify Stop is Available
 * 9.Verify Auto-play 
 * 10.Verify Menu , Settings, Bet & Pay-table 
 * 11.Verify (Help & Game History) from Menu & its Navigation 
 *  
 * 
 * 
 *
 */
public class  Mobile_Regression_RegulatoryMarket_Portugal

{
		public ScriptParameters scriptParameters;
		Logger log = Logger.getLogger(Mobile_Regression_RegulatoryMarket_Portugal.class.getName());
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
			String regMarket = "Portugal";
			int envId = 1658;
			int productId = 5148;
			String currencyIsoCode = "EUR";
			int marketTypeID = 27;
			double balance = 10000;
			String lobbyname= "PortugalQuickfire";
			String languages[]= {"en","pt"};
			
			Mobile_HTML_Report portugal =new Mobile_HTML_Report(webdriver,DeviceName,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,gameName);
			MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
			CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,portugal, gameName);
			int moduleId=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
			int  clientId=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDDesktop"));
			String gameVersion = TestPropReader.getInstance().getProperty("gameVersion");
			cfnlib.setOsPlatform(scriptParameters.getOsPlatform()); //To get OSPlatform 
			CommonUtil util = new CommonUtil();
			
			
			try 
			{
				if (webdriver != null) 
				{
					for(int i=0;i<2;i++)
					{
					userName=util.quickFireRandomStringgenerator();
					System.out.println(userName);
					//envid=Integer.parseInt(TestPropReader.getInstance().getProperty("EnvironmentID"));
					//util.updateUserBalance(userName, balance);
					util.quickfireGamesCreateURL( userName, gameVersion, moduleId,  clientId,lobbyname,"desktop");
					String url = cfnlib.xpathMap.get("portugal_URL");		
	               // String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)", "externalToken="+userName+"$1");
	                String LaunchURl = url.replace("externalToken=","externalToken="+userName);
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

						// Verify TopBar is visible or not
						boolean isTopBarVisible = cfnlib.topBarVisibilitycheck();
						Thread.sleep(2000);

						if (isTopBarVisible)
						{
							log.debug("Topbar is visible");
							portugal.detailsAppend("Verify TopBar ", "TopBar is Displayed", "TopBar is Displayed", "PASS");
						}
						else 
						{
							log.debug("Topbar is not visible");
							portugal.detailsAppend("Verify TopBar ", "TopBar is Displayed", "TopBar is Displayed", "FAIL");
						}
						
						// Game name on the Top Bar
						String isGameName = cfnlib.gameNameFromTopBar(portugal);
						if (isGameName != null) 
						{
							portugal.detailsAppend(" Gamename  on Topbar", "Gamename is displayed", "Gamename is displayed","PASS");
						}
						else
						{
							portugal.detailsAppend(" Gamename  on Topbar", "Gamename isn't displayed","Gamename isn't displayed", "FAIL");
							Thread.sleep(2000);
						}

						
						// Clock on the Top Bar 
						String clock = cfnlib.clockFromTopBar();
						if (clock != null && !clock.equalsIgnoreCase(""))
						{
							portugal.detailsAppend(" Clock on Topbar", "Clock is Displayed ", "Clock is Displayed", "PASS");
						} 
						else
						{
							portugal.detailsAppend(" Clock on Topbar", "Clock is Displayed", "Clock is Displayed ", "FAIL");
						}
						
						// Verify the Auto-play button availability in Germany Market 

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

						boolean isquickspin = cfnlib.verifyQuickSpinAvailablity();
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
								portugal.detailsAppend("Menu Close", "Menu is Closed ", "Menu is Closed", "PASS");
							} 
							else
							{
								log.debug("Menu Closed");
								portugal.detailsAppend("Menu Close", "Menu is Closed ", "Menu is Closed", "FAIL");
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
							log.debug("Settings Opend");
							portugal.detailsAppend("Settings Open", "Settings is Open ", "Settings is Open", "PASS");
							Thread.sleep(3000);
							boolean closeSettings = cfnlib.settingsBack();
							if (closeSettings) 
							{
								log.debug("Settings closed");
								portugal.detailsAppend("Settings closed", "Settings is Closed ", "Settings is Closed","PASS");
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
						boolean openBet = cfnlib.openTotalBet();
						if (openBet)
						{
							log.debug("Bet Opend");
							portugal.detailsAppend("Bet Open", "Bet is Open ", "Bet is Open", "PASS");
							Thread.sleep(2000);
							cfnlib.closeTotalBet();
							Thread.sleep(2000);
							log.debug("Bet Closed");
							portugal.detailsAppend("Bet Closed", "Bet is Closed ", "Bet is Closed", "PASS");
						}
						else 
						{
							portugal.detailsAppend("Bet Open", "Bet is Open ", "Bet is Open", "FAIL");
						}
						
						//Verify PayTable
		                if(!gameName.contains("Scratch") && cfnlib.checkAvilability(cfnlib.xpathMap.get("isMenuPaytableBtnVisible")))
		                {
							cfnlib.capturePaytableScreenshot(portugal,language);
							log.debug("Paytable opened");
							Thread.sleep(2000);
							cfnlib.paytableClose();	
							log.debug("Paytable closed"); 
							Thread.sleep(2000);
		                }
				
						// Verify Help from Menu
						boolean helpfromMenu = cfnlib.portugalHelpFromTopBarMenu(portugal);
						if (helpfromMenu) 
						{
							portugal.detailsAppend("Help from Menu ","Help is clicked and navigated back to base game ","Help is clicked and navigated back to base game ", "PASS");																
						}
						else 
						{
							portugal.detailsAppend("Verify Top Bar Help and Navigation ","Help is clicked and navigated back to base game ","Help is clicked and navigated back to base game ", "FAIL");
						}
						Thread.sleep(2000);
					
						// Verify Resposible Gaming from Menu
						boolean responsiblegaming = cfnlib.portugalGameHistoryFromTopBarMenu(portugal);
						if (responsiblegaming) 
						{
							portugal.detailsAppend("ResponsibleGaming from Menu ", "ResponsibleGaming is clicked and navigated back to base game ", "ResponsibleGaming is clicked and navigated back to base game ", "PASS");
							
						}
						else 
						{
							portugal.detailsAppend("ResponsibleGaming from Menu ", "ResponsibleGaming is clicked and navigated back to base game ", "ResponsibleGaming is clicked and navigated back to base game ", "FAIL");
						}
						Thread.sleep(2000);
					
						log.debug("!!!!!!! Done with Portugal Market !!!!!!!");
						log.debug("                                           ");
					}//close for loop
                  
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

				portugal.endReport();
				// ---------- Closing the webdriver ----------//
				webdriver.close();

				webdriver.quit();

				// Global.appiumService.stop();
			}
		}

	}
