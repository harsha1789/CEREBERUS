package Modules.Regression.TestScript;
import java.io.File;
import java.io.FileInputStream;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.DataBaseFunctions;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop_Force;
import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile_Force;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import net.lightbody.bmp.BrowserMobProxyServer;

/**     Mobile ReelThunder "gtp 131"
 * * This script verifies the Italy check points.
	 * =============================================
	 *  Launch the Italy with EN & IT 
	 *  1. Verify Take to game play request  present or not
	 *  2. Verify Scroll Bar  
	 *  3. Verify Credit amount is same as selected one from game play request screen 
	 *  4. Verify Reel Spin Duration 
	 *  5. Verify Currency Check  
	 *  6. Verify Top Bar
	 *  7. Verify Game Name Top Bar
	 *  8. Verify Clock Top Bar
	 *  9.Verify Player Protection Icon from Top Bar
	 *  10.Top Bar Menu : Help , Responsible Gaming and Player protection
	 *  11.Quick Spin Availabilty
	 *  12.Stop Button Availabilty 
     * 
 *
 */
public class  Mobile_Regression_RegulatoryMarket_Italy

{
	
		public ScriptParameters scriptParameters;
		Logger log = Logger.getLogger(Mobile_Regression_RegulatoryMarket_Italy.class.getName());
		public void script() throws Exception 
		{
			
			String mstrTC_Name=scriptParameters.getMstrTCName();
			String mstrTC_Desc=scriptParameters.getMstrTCDesc();
			String mstrModuleName=scriptParameters.getMstrModuleName();
			BrowserMobProxyServer proxy=scriptParameters.getProxy();
			String startTime=scriptParameters.getStartTime();
			String filePath=scriptParameters.getFilePath();
			
			//AndroidDriver<WebElement> webdriver=scriptParameters.getWebdriver();
			AppiumDriver<WebElement> webdriver=scriptParameters.getAppiumWebdriver();
			
			String DeviceName=scriptParameters.getDeviceName();
		//	String userName=scriptParameters.getUserName();
			String framework=scriptParameters.getFramework();
			//String deviceLockKey=scriptParameters.getDeviceLockKey();
			String gameName=scriptParameters.getGameName();
			//int checkedOutDeviceNum=scriptParameters.getCheckedOutDeviceNum();
			
			//WebDriver webdriver=scriptParameters.getDriver();
			
			String Status=null;
			//String languageCode=null;
			String applicationName,obj,GameTitle=null;
			int mintDetailCount=0;
			//int mintSubStepNo=0;
			int mintPassed=0;
			int mintFailed=0;
			int mintWarnings=0;
			int mintStepNo=0;
			String userName ;
			String regMarket = "Italy";
			int envId = 1658;
			int productId = 5100;
			String currencyIsoCode = "EUR";
			int marketTypeID = 2;
			
			String classname= this.getClass().getSimpleName();
			String xmlFilePath= System.getProperty("user.dir")+"\\src\\Modules\\Regression\\TestData\\GameData\\"+classname+".testdata";
			File src= new File(xmlFilePath);      		
			File destFile = new File("\\\\10.247.224.22\\C$\\MGS_TestData\\"+classname+".testdata");
			Mobile_HTML_Report italy =new Mobile_HTML_Report(webdriver,DeviceName,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,gameName);
			MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
			CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,italy, gameName);
			CommonUtil util = new CommonUtil();
			cfnlib.setOsPlatform(scriptParameters.getOsPlatform()); //To get OSPlatform 
			
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
			try 
			{
				if (webdriver != null) 
				{	
					
					for(int i=1;i<3;i++)
					{
						//userName=util.randomStringgenerator();
						//util.randomUserCreationForRegMarkets(userName, regMarket, envId, productId,currencyIsoCode,marketTypeID);
						
						if(i==1)
						{	
							String url = cfnlib.xpathMap.get("Italy-EN");
							cfnlib.loadGame(url);	
							/*String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
							log.debug("url = " +LaunchURl);
							cfnlib.loadGame(LaunchURl);	*/
						}
						else
						{
						/*	String url = cfnlib.xpathMap.get("Germany_Url");
							String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
							
							if(LaunchURl.contains("languagecode"))
								LaunchURl=	LaunchURl.replace("languagecode=en","languagecode=de");
							else if(LaunchURl.contains("LanguageCode"))
								LaunchURl =LaunchURl.replace("LanguageCode=en","LanguageCode=de");
								
							log.debug("url = " +LaunchURl);
							cfnlib.loadGame(LaunchURl);	*/
						}
						Thread.sleep(6000);
						
						
						
	
							
            //Verify Take to game screen  feature appears or not && verify the scroll bar and set the amount 
				
				String amount=cfnlib.xpathMap.get("Italy_amount"); //Italy amount from Excel 
				System.out.println("Amount from Excel is "+amount);
				Thread.sleep(3000);
				
				if(amount!=null)
				{
				cfnlib.italyScrollBarCickAmount(italy, amount); // method for Scroll Bar 
				
				  italy.detailsAppend("  Game play ", "  Game play is selected "," Game play is selected", "PASS");	
				}
				else
				{
					italy.detailsAppend(" Verify that  game is accessible only after Take to Game feature ", " Game  not lauched after credit selected "," Game  not lauched after credit selected ", "FAIL");	
				}
				 
		if(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame"))||(Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad"))))
				{
			 //  cfnlib.funcFullScreen();
			      cfnlib.newFeature();
				  Thread.sleep(5000);	
				cfnlib.funcFullScreen();
				}
				else
				{
					cfnlib.funcFullScreen();
					Thread.sleep(1000);
					cfnlib.newFeature();

				} 
		Thread.sleep(3000);
		
				
		
		/*	
		//Compare the Credit Amount from the Game play request to Base Game / Verify Credit amount is same selected one
		String isCreditAmount=cfnlib.italyCreditAmountComparission();
		if(isCreditAmount!= null)
		{
			
			italy.detailsAppend(" Credit Amount ", "credit Amounts are  same", "credit  Amounts are same", "PASS");
		}
		else
		{
			italy.detailsAppend(" Credit Amount ", "credit Amounts are  same", "credit  Amounts are same", "FAIL");
		}
		
		//Credit Comparission from Game play 
		boolean curencycheck = cfnlib.italyCurrencyCheck(italy);
		if (curencycheck)
		{
            System.out.println("YES, The Currency is SAME");
			italy.detailsAppend(" Currency", "Currency is Same" ,"Currency is Same", "PASS");
		}
		else
		{
			 System.out.println("NO, The Currency is Difference");
			italy.detailsAppend(" Currency", "Currency is Same" ,"Currency is Same", "FAIL");
		}
	
		
					// Verify TopBar is visble or not
					boolean isTopBarVisible = cfnlib.topBarVisibilitycheck();
					Thread.sleep(2000);

					if (isTopBarVisible)
					{
						italy.detailsAppend(" TopBar", "TopBar is Displayed", "TopBar is Displayed", "PASS");
					}
            		else
					{
						System.out.println("Topbar is not visible");
						italy.detailsAppend(" TopBar", "TopBar is Displayed", "TopBar is  Displayed", "FAIL");
					}
				
			
						
				//Game name on the Top Bar 
				String isGameName=cfnlib.gameNameFromTopBar(italy);
				if(isGameName != null)
				{
					italy.detailsAppend(" Gamename  on Topbar", "Gamename is displayed", "Gamename is displayed", "PASS");
				}
				else
				{
					italy.detailsAppend(" Gamename  on Topbar", "Gamename isn't displayed", "Gamename isn't displayed", "FAIL");
					Thread.sleep(2000);
				}
				
				//Clock on the Top Bar 
				String clock=cfnlib.clockFromTopBar();
				if(clock != null && !clock.equalsIgnoreCase(""))
				{
					italy.detailsAppend(" Clock  on Topbar", "Clock is displayed", "Clock is displayed", "PASS");
				}
				else
				{
					italy.detailsAppend(" Clock  on Topbar", "Clock isn't displayed", "Clock isn't displayed", "FAIL");
					Thread.sleep(2000);
				}
				
		*/
				// Verify if Top Bar Player Protection Icon   
		         boolean playerprotectionicon = cfnlib.playerProtectionIcon (italy);
				if(playerprotectionicon)
				{
					italy.detailsAppend(" PlayerProtection Top Bar ", "PlayerProtection is Displayed","PlayerProtection is Displayed", "PASS");
					Thread.sleep(3000);
				}
				
				else 
				{
					italy.detailsAppend(" PlayerProtection Top Bar ", "PlayerProtection isn't  Displayed","PlayerProtection isn't Displayed", "FAIL");
				}
				
				
				//Italy Help From Menu 
							boolean help = cfnlib.italyHelpFromTopBarMenu (italy);
							if(help)
							{
								italy.detailsAppend("  Help TopBar ", "Help is Displayed","Help is Displayed", "PASS");
								
								boolean helpFromMenu=cfnlib.italyOpenHelpFromMenu(italy); //Verify weather Help is clicked and navigated Game 
								 cfnlib.funcFullScreen();
								if(helpFromMenu)
								{
									italy.detailsAppend("TopBar Help  and Navigation  ", "Help is clicked and navigated back to base game ","Help is clicked and navigated back to base game ", "PASS");
								}
								else
								{
																
									italy.detailsAppend("TopBar Help  and Navigation  ", "Help is clicked and navigated back to base game ","Help is clicked and navigated back to base game ", "FAIL");
								}
								
							}
							else
							{
								italy.detailsAppend("  Help TopBar ", "Help is Displayed","Help is Displayed", "FAIL");
							}
				
				
				// Verify Resposible Gaming  from Menu  
				boolean responsiblegaming = cfnlib.italyResponsibleGamingFromMenu (italy);
						if(responsiblegaming)
						{
							italy.detailsAppend("TopBar Responsiblegaming ", "Responsiblegaming is Displayed","Responsiblegaming is Displayed", "PASS");
							
							boolean responsibleGamingFromMenu=cfnlib.italyOpenResponsibleGamingFromMenu(italy); //Verify weather Help is clicked and navigated Game  
							 cfnlib.funcFullScreen();
							if(responsibleGamingFromMenu)
							{
								italy.detailsAppend("TopBar Responsiblegaming  and Navigation  ", "Responsiblegaming is clicked and navigated back to base game ","Responsiblegaming is clicked and navigated back to base game ", "PASS");
							}
							else
							{
															
								italy.detailsAppend("TopBar Responsiblegaming  and Navigation  ", "Responsiblegaming is clicked and navigated back to base game ","Responsiblegaming is clicked and navigated back to base game ", "FAIL");
							}
						}
							
						// Verify if Player Protection from Menu  
						boolean playerprotection = cfnlib.italyPlayerProtectionFromMenu (italy); 
								if(playerprotection)
								{
									italy.detailsAppend(" Top Bar PlayerProtection ", "PlayerProtection is displayed","PlayerProtection is displayed", "PASS");
									
									boolean helpclick=cfnlib.italyOpenPlayerProtectionFromMenu(italy); //Verify weather Help is clicked and navigated Game 
									 cfnlib.funcFullScreen();
									if(helpclick)
									{
										italy.detailsAppend(" Top Bar PlayerProtection  and Navigation  ", "PlayerProtection is clicked and navigated back to base game ","PlayerProtection is clicked and navigated back to base game ", "PASS");
									}
									else
									{
																	
										italy.detailsAppend(" Top Bar PlayerProtection  and Navigation  ", "PlayerProtection is clicked and navigated back to base game ","PlayerProtection is clicked and navigated back to base game ", "FAIL");
									}	Thread.sleep(3000);
								}
								
								else 
								{
									italy.detailsAppend(" Top Bar PlayerProtection", "PlayerProtection is   displayed","PlayerProtection is  displayed", "FAIL");
								}Thread.sleep(3000);
							
					
				
				
				
			
                //Verify the quickspin button availability in Italy Market
				
				boolean isquickspin=cfnlib.verifyQuickSpinAvailablity();
				Thread.sleep(1000);
				if(isquickspin)
				{	
					italy.detailsAppend(" Quickspin ", "Quickspin is available ", "Quickspin is available", "FAIL");
				}
				else
				{
					italy.detailsAppend(" Quickspin ", "Quickspin is not available ", "Quickspin is not available", "PASS");
				}	
				Thread.sleep(2000);
				
				
				//Verify the stop button availability in Italy Market
				
				boolean isstopbtn=cfnlib.verifyStopButtonAvailablity();
				Thread.sleep(1000);
				if(isstopbtn)
				{	
					italy.detailsAppend(" Stop Button  ", "Stop Button  is available ", "Stop Button  is available", "FAIL");
				}
				else
				{
					italy.detailsAppend(" Stop Button ", "Stop Button is not available ", "Stop Button is not available", "PASS");
				}	
				Thread.sleep(2000);
				
				//To find the reel spin duration for a single spin	
				
				long isReelspin=cfnlib.reelSpinDuratioN(italy);
				if(isReelspin < 4000)
				{
					System.out.println("Reel Spin Duration , STATUS : PASS");
					italy.detailsAppend("Verify Reel Spin is less than FOUR  ", "Reel Spin Duration in Sec is :" +isReelspin, "Reel Spin Duration  in Sec is :" +isReelspin, "PASS");
				}
				else
				{
					System.out.println("Reel Spin Duration , STATUS : FAIL");
					italy.detailsAppend("Verify Reel Spin is less than FOUR  ", "Reel Spin Duration in Sec is :" +isReelspin, "Reel Spin Duration  in Sec is :" +isReelspin, "FAIL");
				}
				
				//Refresh 
				//cfnlib.refresh();
				
			System.out.println("!!!!!!! Done with Italy Market !!!!!!!");
			System.out.println("                                           ");

					} // Close Finally 
					
				}// Close For Loop
				
			} // Close Try Block 
			
			
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

				italy.endReport();
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