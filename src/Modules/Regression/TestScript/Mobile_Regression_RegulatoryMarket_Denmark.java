package Modules.Regression.TestScript;
import java.io.File;
import java.io.FileInputStream;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
	 * This script verifies the Denmark check points.
	 * =============================================
	 * Launch with Denmark -( EN && DA ) Code languages
	 *  1. Verify Session Reminder  present or not
	 *  2. Verify Reel Spin Duration 
	 *  3. Verify Currency 
	 *  4. Verify Top Bar Visibility 
	 *  5. Verify Top Bar Game Name
	 *  6.Verify Top Bar Menu ( Help , Player Protection)
	 *  7. Verify Top Bar Clock 
	 *  8. Verify Quick Spin Availability 
	 *  9.Verify Stop Button Availability 
	 *  10.Verify Close from the Top Bar and Refresh the Page
	 *  
	 *   
	 * 
	 *
	 */
public class  Mobile_Regression_RegulatoryMarket_Denmark

{
	
		public ScriptParameters scriptParameters;
		Logger log = Logger.getLogger(Mobile_Regression_RegulatoryMarket_Denmark.class.getName());
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
			
			String Status=null;
			int mintDetailCount=0;
			int mintPassed=0;
			int mintFailed=0;
			int mintWarnings=0;
			int mintStepNo=0;
			String userName ;
			String regMarket = "Denmark";
			int envId = 1658;
			int productId = 5106;
			String currencyIsoCode = "DKK";
			int marketTypeID = 5;	
			double balance = 10000;

			Mobile_HTML_Report denmark =new Mobile_HTML_Report(webdriver,DeviceName,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,gameName);
			MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
			CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,denmark, gameName);
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
						String url = cfnlib.xpathMap.get("Denmark_url");
						
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
								LaunchURl=	LaunchURl.replace("languagecode=en","languagecode=da");
							else if(LaunchURl.contains("languageCode"))
								LaunchURl =LaunchURl.replace("languageCode=en","languageCode=da");
							cfnlib.loadGame(LaunchURl);	
							log.debug("url = " +LaunchURl);
						}
				
					
					if (framework.equalsIgnoreCase("Force"))
					{
						cfnlib.setNameSpace();
						System.out.println("Force Games");
						
					}
					
					
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
				boolean sessionReminderVisible=cfnlib.sessionReminderPresent(denmark);
				Thread.sleep(2000);
				 cfnlib.funcFullScreen();
				if(sessionReminderVisible) 
				{
					denmark.detailsAppend("Verify Session Reminder  ","Session Reminder is present and clicked on Continue Button ","Session Reminder is present and clicked on Continue Button","PASS");	
				}
				else
				{
					denmark.detailsAppend("Verify Session Reminder ","Session Reminder is not present and unable to click Continue Button","Session Reminder is not present and unable to click Continue Button","FAIL");
				
				}
				
				
				//To find the reel spin duration for a single spin	
				long isReelspin=cfnlib.reelSpinDuratioN(denmark);
				if(isReelspin < 4000)
				{
					log.debug("Reel Spin Duration , STATUS : PASS");
					denmark.detailsAppend("Verify Reel Spin is less than FOUR  ", "Reel Spin Duration in Sec is :" +isReelspin, "Reel Spin Duration  in Sec is :" +isReelspin, "PASS");
				}
				else
				{
					log.debug("Reel Spin Duration , STATUS : FAIL");
					denmark.detailsAppend("Verify Reel Spin is less than FOUR  ", "Reel Spin Duration in Sec is :" +isReelspin, "Reel Spin Duration  in Sec is :" +isReelspin, "FAIL");
				}
				
				//Denmark currency check 
				boolean curencycheck = cfnlib.denmarkCurrencyCheck(denmark);
				if (curencycheck)
				{
					System.out.println("YES , The currency is Same");
					denmark.detailsAppend("Verify Currency","Currency is Same", "Currency is Same","PASS");
					
				}
				else
				{
					System.out.println("NO , The currency is Different");
					denmark.detailsAppend("Verify Currency","Currency is not Same", "Currency is not Same","FAIL");
					
				}
							
				
				// Verify TopBar is visble or not
				boolean isTopBarVisible = cfnlib.topBarVisibilitycheck();
				Thread.sleep(2000);

				if (isTopBarVisible)
				{
					denmark.detailsAppend(" TopBar", "TopBar is Displayed", "TopBar is Displayed", "PASS");
				}
        		else
				{
					System.out.println("Topbar is not visible");
					denmark.detailsAppend(" TopBar", "TopBar is Displayed", "TopBar is  Displayed", "FAIL");
				}
			
	
					
					// Game name on the Top Bar
					String isGameName = cfnlib.gameNameFromTopBar(denmark);
					if (isGameName != null) 
					{
						denmark.detailsAppend(" Gamename  on Topbar", "Gamename is displayed", "Gamename is displayed","PASS");
					}
					else 
					{
						denmark.detailsAppend(" Gamename  on Topbar", "Gamename is not displayed","Gamename is not displayed", "FAIL");
						Thread.sleep(2000);
					}

					// Clock on the Top Bar
					String clock = cfnlib.clockFromTopBar();
					if (clock != null && !clock.equalsIgnoreCase(""))
					{
						denmark.detailsAppend(" Clock  on Topbar", "Clock is displayed", "Clock is displayed", "PASS");
					}
					else 
					{
						denmark.detailsAppend(" Clock  on Topbar", "Clock is not displayed", "Clock is not displayed","FAIL");
						Thread.sleep(2000);
					}
			
						

					// Verify if Help Text is present or not
					boolean help = cfnlib.denmarkHelpFromTopBarMenu(denmark);
					if (help)
					{

						denmark.detailsAppend("Verify Top Bar Help and Navigation ",
								"Help is clicked and navigated back to base game ",
								"Help is clicked and navigated back to base game ", "PASS");
					}
					else
					{

						denmark.detailsAppend("Verify Top Bar Help and Navigation ",
								"Help is clicked and navigated back to base game ",
								"Help is clicked and navigated back to base game ", "FAIL");
					}
					
					
								
					// Verify if Player Protection from Menu
					boolean playerprotection = cfnlib.denmarkPlayerProtectionFromMenu(denmark);
					if (playerprotection) {

						denmark.detailsAppend("Verify Top Bar PlayerProtection  and Navigation  ",
								"PlayerProtection is clicked and navigated back to base game ",
								"PlayerProtection is clicked and navigated back to base game ", "PASS");
					} else {

						denmark.detailsAppend("Verify Top Bar PlayerProtection  and Navigation  ",
								"PlayerProtection is clicked and navigated back to base game ",
								"PlayerProtection is clicked and navigated back to base game ", "FAIL");
					}
								
										
					// Verify the quickspin button availability in Italy Market
					boolean isquickspin = cfnlib.verifyQuickSpinAvailablity();
					Thread.sleep(1000);
					if (isquickspin)
					{
						denmark.detailsAppend(" Quickspin ", "Quickspin is available ", "Quickspin is available","FAIL");
					}
					else
					{
						denmark.detailsAppend(" Quickspin ", "Quickspin is not available ","Quickspin is not available", "PASS");
					}
					Thread.sleep(2000);

					// Verify the stop button availability in Italy Market

					boolean isstopbtn = cfnlib.verifyStopButtonAvailablity();
					Thread.sleep(1000);
					if (isstopbtn)
					{
						denmark.detailsAppend(" Stop Button  ", "Stop Button  is available ","Stop Button  is available", "FAIL");
					}
					else
					{
						denmark.detailsAppend(" Stop Button ", "Stop Button is not available ","Stop Button is not available", "PASS");
					}
					Thread.sleep(2000);
                 
					System.out.println("!!!!!!! Done with Denmark Market !!!!!!!");
					System.out.println("                                                     ");

					} // Close Finally 
					
				}// Close For Loop
				
			} // Close Try Block 
			//TRY
		//-------------------Handling the exception---------------------//
			catch (Exception e)
			{
				log.error(e.getMessage(), e);
				cfnlib.evalException(e);
			}

			finally 
			{

				// Global.browserproxy.abort();
				// ---------- Closing the report----------//

				denmark.endReport();
				// ---------- Closing the webdriver ----------//
				webdriver.close();

				webdriver.quit();

				// Global.appiumService.stop();
			}
		}

	}

