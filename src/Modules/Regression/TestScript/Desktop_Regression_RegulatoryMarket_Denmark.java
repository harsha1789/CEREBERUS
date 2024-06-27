package Modules.Regression.TestScript;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.library.CommonUtil;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;



public class Desktop_Regression_RegulatoryMarket_Denmark
{
	/**
	 * Desktop ReelThunder "gtp 131"
	 * This script verifies the Denmark check points.
	 * =============================================
	 * Launch with Denmark -( EN && DA )
	 *  1. Verify Session Reminder  present or not
	 *  2. Verify Reel Spin Duration
	 *  3. Verify Top Bar Game Name and Compare  
	 *  4. Verify Game Name on Top Bar 
	 *  5. Verify Clock from Top Bar 
	 *  6. Verify Menu (Help & Player Protection )
	 *  7. Verify Currency 
	 *  8. Verify Quick Spin 
	 *  9.Verify Stop Button 
	 *  10.Verify Close
	 *  
	 *  
	 * 
	 *
	 */

	public ScriptParameters scriptParameters;

	Logger log = Logger.getLogger(Desktop_Regression_RegulatoryMarket_Italy.class.getName()); 
	// -------------------Main script defination---------------------//
	public void script() throws Exception {
		String mstrTC_Name = scriptParameters.getMstrTCName();
		String mstrTC_Desc = scriptParameters.getMstrTCDesc();
		String mstrModuleName = scriptParameters.getMstrModuleName();
		WebDriver webdriver = scriptParameters.getDriver();
		BrowserMobProxyServer proxy = scriptParameters.getProxy();
		String startTime = scriptParameters.getStartTime();
		String filePath = scriptParameters.getFilePath();
		String Browsername = scriptParameters.getBrowserName();
		String Framework = scriptParameters.getFramework();
		String GameName = scriptParameters.getGameName();
		String Status = null;
		int mintDetailCount = 0;
	   // int mintSubStepNo=0;
		int mintPassed = 0;
		int mintFailed = 0;
		int mintWarnings = 0;
		int mintStepNo = 0;
		String userName ;
		String regMarket = "Denmark";
		int envId = 1658;
		int productId = 5106;
		String currencyIsoCode = "DKK";
		int marketTypeID = 5;	
		double balance = 10000;

		Desktop_HTML_Report denmark = new Desktop_HTML_Report(webdriver, Browsername, filePath, startTime, mstrTC_Name,mstrTC_Desc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings, mintStepNo, Status,GameName);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(Framework, webdriver, proxy,denmark, GameName);
		CommonUtil util = new CommonUtil();
		

		try 
		{
			if (webdriver != null) 
			{	
				for(int i=1;i<3;i++)
				{
					userName=util.randomStringgenerator();
					util.randomUserCreationForRegMarkets(userName, regMarket, envId, productId,currencyIsoCode,marketTypeID);
			        //util.updateUserBalance(userName, balance);
					String url = cfnlib.XpathMap.get("Denmark");
					
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

				
				if (Framework.equalsIgnoreCase("Force"))
				{
					cfnlib.setNameSpace();
					System.out.println("Force Games");
					
				}
			
		/*	//This is for Continue Button on Loading Screen 
				if((Constant.YES.equalsIgnoreCase(cfnlib.XpathMap.get("continueBtnOnGameLoad"))))
				{
				cfnlib.newFeature();
				}
				
			*/	

			//session reminder
			boolean sessionReminderVisible=cfnlib.isSessionReminderPresent(denmark);
			Thread.sleep(1000);
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
				denmark.detailsAppend("Verify Reel Spin is less than FOUR  ", "Reel Spin Duration in Sec is :" +isReelspin, "Reel Spin Duration", "FAIL");
			}		
						
				
					// Verify TopBar is visble or not

					boolean isTopBarVisible = cfnlib.verifyTopBarVisible();
					Thread.sleep(2000);
					if (isTopBarVisible)
					{
						denmark.detailsAppend("Verify TopBar", "TopBar is displayed ", "TopBar is didplayed", "PASS");
					} 
					else
					{
						log.debug("Topbar is not visible");
						denmark.detailsAppend("Verify TopBar must be is displayed", "TopBar is not didplayed","TopBar is not didplayed", "FAIL");
					}

					// Game name on the Top Bar

					String isGameName = cfnlib.gameNameFromTopBar(denmark);
					if (isGameName != null)
					{
						denmark.detailsAppend("Verify Topbar Gamename", "Gamename is displayed on Topbar","Gamename is displayed on Topbar", "PASS");
					}
                   else 
                    {
						denmark.detailsAppend("Verify Topbar Gamename", "Gamename is displayed on Topbar","Gamename is not displayed on Topbar", "FAIL");
					}

					// Clock on the Top Bar
					boolean clock = cfnlib.clockFromTopBar(denmark); // clockOnTheTopBar
					if (clock)
					{
						denmark.detailsAppend("Verify Topbar Clock", "Clock is displayed on Topbar","Clock is displayed on Topbar", "PASS");
					}
					else 
					{
						denmark.detailsAppend("Verify Topbar Clock", "Clock is not displayed on Topbar","Clock is not displayed on Topbar", "FAIL");
					}

					//Verify Help Text Link from TopBar 
					boolean helptext = cfnlib.helpTextLink(denmark);
					if (helptext) 
					{
						denmark.detailsAppend("  Help Text  from TopBar ", "Help is Displayed and its Navigation", "Help is Displayed and its Navigation", "PASS");															
					}
					else 
					{
						denmark.detailsAppend("  Help Text  from TopBar ", "Help is Displayed and its Navigation", "Help is Displayed and its Navigation", "FAIL");	
					}
					
					
					// Verify if Player Protection from Menu
					boolean playerprotection = cfnlib.denmarkPlayerProtectionFromMenu (denmark);
					if(playerprotection)
					{
                     denmark.detailsAppend(" Top Bar PlayerProtection ", "PlayerProtection is displayed and its Navigation","PlayerProtection is displayed is displayed and its Navigation", "PASS");
					}
                   else 
					{
						denmark.detailsAppend(" Top Bar PlayerProtection ", "PlayerProtection is displayed and its Navigation","PlayerProtection is displayed is displayed and its Navigation", "FAIL");
					}
					Thread.sleep(3000);			
				
				
					// Denmark_CreditAmountComparission
					boolean curencycheck1 = cfnlib.denmarkCurrencyCheck(denmark);
					if (curencycheck1)
					{
						log.debug("YES , The currency is Same");
						denmark.detailsAppend("Verify Currency", "Currency is Same", "Currency is Same", "PASS");
					}
					else 
					{
						log.debug("YES , The currency is Different");
						denmark.detailsAppend("Verify Currency", "Currency is Same", "Currency is Same", "FAIL");
					}
				
			
				Thread.sleep(3000);
				
						
				//Verify the quickspin button availability 

				boolean isquickspin = cfnlib.verifyQuickspinAvailablity();
				Thread.sleep(1000);
				if (isquickspin) 
				{
				     denmark.detailsAppend(" Quickspin ","Quickspin is available ", "Quickspin is available","FAIL");
				}
				else 
				{
					 denmark.detailsAppend(" Quickspin ","Quickspin is not available ", "Quickspin is not available","PASS");
				}
				Thread.sleep(2000);
				
				

				//Verify the stop button availability
				boolean isstopbtn = cfnlib.verifyStopButtonAvailablity();
				Thread.sleep(1000);
				if (isstopbtn) 
				{
					denmark.detailsAppend("Stop button","Stop button is available", "Stop button is available","FAIL");
				} 
				else
				{
					denmark.detailsAppend("Stop button","Stop button is not available", "Stop button is not available","PASS");
				}
				Thread.sleep(2000);
			
				
				/*		//Close 
				boolean close = cfnlib.close(denmark);
				if(close)
				{
					//webdriver.navigate().refresh();	
					cfnlib.refresh(denmark); // Refresh and Clicked on Continue 
					Thread.sleep(5000);
				    //denmark.detailsAppend("Verify Refresh ", "Refreshed Sucessfully  ","Refreshed Sucessfully", "PASS");
				}
				else
				{
					//denmark.detailsAppend("Verify Refresh ", "Refreshed UnSucessfully  ","Refreshed UnSucessfully", "FAIL");
				}*/

				
				
				System.out.println("!!!!!!! Done with Denmark Market !!!!!!!");
				System.out.println("                                                      ");
				
			}
		    }
		}
		// TRY
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

			denmark.endReport();
			// ---------- Closing the webdriver ----------//
			webdriver.close();

			webdriver.quit();

			// Global.appiumService.stop();
		}
	}

}





