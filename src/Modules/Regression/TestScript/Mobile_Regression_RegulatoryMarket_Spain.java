package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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

import java.text.DecimalFormat;


/**
 *    Mobile BustTheBank "Zensar Dev 2"
 *    This script verifies the Denmark check points.
	  =============================================
  Launch the game with Spain (EN & SP)
  1.verify overlay
  2.Fill the Game Overlay
  3.Verify Cooling off period
  4.Verify Quick Spin 
  5.Verify Stop Button
  6.Verify Game name from Topbar
  7. Verify Clock from Topbar
  8. Verify help icon from Topbar
  9.verify the Menu(Help)
  10.Wait until session loss reminder
  11.Wait until session loss 
  12.Close session loss popup
  13.Verify Currency 
  14.Verify Reel spin Duration
  
   

 */


public class Mobile_Regression_RegulatoryMarket_Spain 
{
	public ScriptParameters scriptParameters;
	Logger log = Logger.getLogger(Mobile_Regression_RegulatoryMarket_Spain.class.getName());
	public void script() throws Exception 
	{
		String mstrTC_Name=scriptParameters.getMstrTCName();
		String mstrTC_Desc=scriptParameters.getMstrTCDesc();
		String mstrModuleName=scriptParameters.getMstrModuleName();
		BrowserMobProxyServer proxy=scriptParameters.getProxy();
		String startTime=scriptParameters.getStartTime();
		String filePath=scriptParameters.getFilePath();
		String browserName=scriptParameters.getBrowserName();
		String framework=scriptParameters.getFramework();
		String osPlatform=scriptParameters.getOsPlatform();
		String gameName=scriptParameters.getGameName();
		String DeviceName=scriptParameters.getDeviceName();		
		String languageCode=null;
		String Status=null;
		int mintDetailCount=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		String userName ;
		String regMarket = "Spain";
		int envId = 1658;
		int productId = 5104;
		String currencyIsoCode = "EUR";
		int marketTypeID = 4;
		double balance = 10000;


		AppiumDriver<WebElement> webdriver=scriptParameters.getAppiumWebdriver();
		CommonUtil util = new CommonUtil();
		Mobile_HTML_Report spain =new Mobile_HTML_Report(webdriver,DeviceName,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,gameName);
		MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,spain, gameName);
		cfnlib.setOsPlatform(scriptParameters.getOsPlatform());
		int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int  cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDMobile"));
		List<String> copiedFiles=new ArrayList<>();
		
		
		try 
		{
			if (webdriver != null) 
			{	
				String strFileName=TestPropReader.getInstance().getProperty("FreeSpinsTestDataPath");
				File testDataFile=new File(strFileName);
			
				
				for(int i=1;i<3;i++)
				{
					userName=util.randomStringgenerator();
					if(util.copyFilesToTestServerForRegMarket( mid, cid, testDataFile, userName,  regMarket, envId, productId, currencyIsoCode , marketTypeID, copiedFiles))
					{
					
					util.updateUserBalance(userName, balance);
					String url = cfnlib.xpathMap.get("Spain_url");
					
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
						cfnlib.loadGame(LaunchURl);	
						log.debug("url = " +LaunchURl);
					}
						
			if(framework.equalsIgnoreCase("Force"))
			{
				cfnlib.setNameSpace();
			}
			Thread.sleep(4000);
			
			
			
			cfnlib.spainStartNewSession(spain,languageCode);// To check if Start Session Button is Present.
			
			Thread.sleep(2000);
			
			//verify Slot game limit overly visible
			boolean isSlotGameLimitPresent=cfnlib.overLay();
			if(isSlotGameLimitPresent)
			{
				spain.detailsAppendFolder(" Overlay ", "Overlay is Present ", "Overlay is Present ", "PASS",languageCode);
			}
			else
			{
				spain.detailsAppendFolder(" Overlay ", "Overlay is  Present ", "Overlay is  Present ", "FAIL",languageCode);
			}
			
			
			
			//Get the loss limit data to fill Slot game limits form
			//String lossLimitFromExcel = cfnlib.xpathMap.get("LossLimitValidData");
			String lossLimitFromExcel = cfnlib.xpathMap.get("LossLimitValidData");
			log.debug("Loss Limit Value from Excel is :"+lossLimitFromExcel);
			
			//Adding two zeros after decimal point
			
			DecimalFormat df=new DecimalFormat("#.00");
			String lossLimit=df.format(Double.parseDouble(lossLimitFromExcel));
			log.debug("Loss Limit Value from Excel after adding two  zeros after decimal is :"+lossLimit);
			
			
			Thread.sleep(3000);
			//fill the Slot game limits form
			boolean slotgamelimit = cfnlib.fillStartSessionLossForm(lossLimit ,spain,languageCode);
			if(slotgamelimit)
	          {
				spain.detailsAppendFolder("Filled all the valid limits in the Slot Game Limits","Sucessfully Filled", "Sucessfully Filled", "PASS",languageCode);
			  }
			else 
			  {
				spain.detailsAppendFolder("Filled all the valid limits in the Slot Game Limits","UnSucessfully Filled", "UnSucessfully Filled", "FAIL",languageCode);
			  }
			
			
			
			//to set spain cooling of period
			cfnlib.spainCoolingOffPeriod(spain);
			
		// Check continue button exists and clicks
			if((Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad"))))
			{
				cfnlib.newFeature();
				spain.detailsAppendFolder("Verify Continue Button ","Clicked on Continue Button", "Clicked on Continue Button", "PASS",languageCode);
				Thread.sleep(3000);
			}
			
			
			cfnlib.funcFullScreen();
			Thread.sleep(3000);
			
		
			
			//Verify the quickspin button availability
			boolean isquickspin=cfnlib.verifyQuickSpinAvailablity();
			Thread.sleep(2000);
			if(!isquickspin)
			{	
				spain.detailsAppendFolder("Quickspin ", "Quickspin is not Available ", "Quickspin is not Available", "PASS",languageCode);
				
			}
			else
			{
				spain.detailsAppendFolder("Quickspin ", "Quickspin is  Available ", "Quickspin is  Available", "FAIL",languageCode);
			}
			
			Thread.sleep(2000);
			
			
			
			//Verify the stop button availability
			boolean stopbutton=cfnlib.verifyStopButtonAvailablity();
			if(!stopbutton)
			{	
				spain.detailsAppendFolder("Stop Button ", " Stop Button is not Available ", " Stop Button is not Available", "PASS",languageCode);
					
			}
			else
			{
				spain.detailsAppendFolder("Stop Button ", " Stop Button is  Available ", " Stop Button is  Available", "FAIL",languageCode);
			}
				
			Thread.sleep(2000);
			
			
			//cfnlib.game_NameOnTopBar(spain);
			// Game name on the Top Bar
			String isGameName = cfnlib.gameNameFromTopBar(spain);
			if (isGameName != null) 
			{
				spain.detailsAppend(" Gamename  on Topbar", "Gamename is displayed", "Gamename is displayed","PASS");
			}
			else 
			{
				spain.detailsAppend(" Gamename  on Topbar", "Gamename is not displayed","Gamename is not displayed", "FAIL");
				Thread.sleep(2000);
			}
			
				
			// Clock on the Top Bar
			String clock = cfnlib.clockFromTopBar();
			if (clock != null && !clock.equalsIgnoreCase(""))
			{
				spain.detailsAppend(" Clock  on Topbar", "Clock is displayed", "Clock is displayed", "PASS");
			}
			else 
			{
				spain.detailsAppend(" Clock  on Topbar", "Clock is not displayed", "Clock is not displayed","FAIL");
				Thread.sleep(2000);
			}


				//verify help Icon on topbar
				//cfnlib.verifyHelpTextlink(spain); 
				boolean helpText = cfnlib.helpTextLink(spain);
				if (helpText)
				{
					spain.detailsAppend("Help","Help text and its Navigation", "Help text and its Navigation","PASS");
				}
				else
				{
				    spain.detailsAppend("Help","Help text and its Navigation", "Help text and its Navigation","FAIL");
				}
				Thread.sleep(2000);
		
				//verify menu navigation help 
			     //cfnlib.verifyMenuOptionNavigationsForSpain(spain);cfnlib.funcFullScreen();
				boolean help = cfnlib.spainHelpFromTopBarMenu(spain);
				if (help)
				{
					spain.detailsAppend(" Help","Help  click and its Navigation ","Help  click and its Navigation ", "PASS");
				}
				else
				{
					spain.detailsAppend(" Help","Help  click and its Navigation ","Help  click and its Navigation ", "FAIL");
				}
				
			   //verify reelspin duration
				long isReelspin=cfnlib.reelSpinDuratioN(spain);
				if(isReelspin > 3000)
				{
					log.debug("Reel Spin Duration , STATUS : PASS");
					spain.detailsAppend("Verify Reel Spin is more than Threee ", "Reel Spin Duration", "Reel Spin Duration", "PASS");
				}
				else
				{
					log.debug("Reel Spin Duration , STATUS : FAIL");
					spain.detailsAppend("Verify Reel Spin is more than Three  ", "Reel Spin Duration", "Reel Spin Duration", "PASS");
				}
				
				//verify currency format
				boolean spaincurencycheck1 = cfnlib.spainCurrencyCheck(spain);
				if (spaincurencycheck1)
				{
					log.debug("YES, The Currency is Same ");
					spain.detailsAppend("Verify Currency","Currency is Same", "Currency is Same","PASS");
					
				}
				else
				{
					log.debug("No, The Currency is Different ");
				    spain.detailsAppend("Verify Currency","Currency is not Same", "Currency is not Same","FAIL");
				}


				Thread.sleep(2000);
				
			
			
			//verify gamelimitreminder overlay is present
			boolean gamelimitreminder = cfnlib.waitUntilSessionReminder(languageCode);
			if(gamelimitreminder)
	          {
				spain.detailsAppendFolder(" Game Limit Reminder", "Game Limit Reminder is present ", "Game Limit Reminder is present ", "PASS",languageCode);
				//click on continue in session reminder
				cfnlib.spainContinueSession();
				spain.detailsAppendFolder(" Game Limit Reminder", "Game Limit Reminder is clicked ", "Game Limit Reminder is clicked ", "PASS",languageCode);
				 
			  }
			else 
			  {
				System.out.println("Slot Game Limit Reminder is not  Present and unable to click on continue ");
				spain.detailsAppendFolder("Verify Game Limit Reminder", "Game Limit Reminder is not present & Unable to click on Continue", "Game Limit Reminder is not present & Unable to click on Continue", "FAIL",languageCode);
			  }
			
	
			//wait for slot game limit when loss limit reach
			boolean title1 = cfnlib.waitUntilSessionLoss(lossLimit);//td needed
			if(title1)
	          {
				
				Thread.sleep(2000);
				
				spain.detailsAppendFolder("Verify Loss Limit reached overlay and closed the session ", "Loss Limit reached overlay. which should appear after loss limit is reached and closed the session ", "Loss Limit reached overlay . which appears after loss limit is reached and closed the session", "PASS",languageCode);
			  }
			else 
			  {
				spain.detailsAppendFolder("Verify if,  Loss Limit reached overlay and closed the session", "Loss Limit reached overlay . which doesn't appear after loss limit is reached and unable to close the session", "Loss Limit reached overlay . which doesn't appear after loss limit is reached and unable to close the session", "FAIL",languageCode);
			  }
			
			//To close session loss overlay
			
			cfnlib.closeSessionLossPopup(spain);	
				
			Thread.sleep(2000);
			
			//refresh the page
			webdriver.navigate().refresh();		
			spain.detailsAppendFolder("  Refresh ", " Refresh is Done ", "Refresh is Done", "PASS",languageCode);
			
			Thread.sleep(5000);

			System.out.println ("!!!!!!!!!!!!!! Done with Spain Market !!!!!!!!!");
			System.out.println ("                                                 ");
			}//end for if - copyFilesToTestServerForRegMarket
			else
			{
				log.debug("Unable to copy test data file on the environment hence skipping execution");
				spain.detailsAppend("", "", "Unable to copy test data on server,hence skipping futher execution", "fail");
		    }
		}// close of if block	
	}//close for loop for 
}//close try loop 
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

			spain.endReport();
			System.out.println("Report Ended");
			
			// ---------- Closing the webdriver ----------//

		    webdriver.close();
			System.out.println("Webdriver Closed");
			webdriver.quit();
			System.out.println("Webdriver quit");
			// Global.appiumService.stop();

		} // Close Finally 
			
	}// Close For Loop
	
} // Close Try Block 








/*
public class Mobile_Regression_RegulatoryMarket_Spain {
	public Logger log = Logger.getLogger(Mobile_Regression_RegulatoryMarket_Spain.class.getName());
	public ScriptParameters scriptParameters;
	

	//-------------------Main script definition---------------------//
	public void script() throws Exception {
		String mstrTC_Name=scriptParameters.getMstrTCName();
		String mstrTC_Desc=scriptParameters.getMstrTCDesc();
		String mstrModuleName=scriptParameters.getMstrModuleName();
		BrowserMobProxyServer proxy=scriptParameters.getProxy();
		String startTime=scriptParameters.getStartTime();
		String filePath=scriptParameters.getFilePath();
		
		//AndroidDriver<WebElement> webdriver=scriptParameters.getWebdriver();
		AppiumDriver<WebElement> webdriver=scriptParameters.getAppiumWebdriver();
		
		String DeviceName=scriptParameters.getDeviceName();
		String userName=scriptParameters.getUserName();
		String framework=scriptParameters.getFramework();
		//String deviceLockKey=scriptParameters.getDeviceLockKey();
		String gameName=scriptParameters.getGameName();
		//int checkedOutDeviceNum=scriptParameters.getCheckedOutDeviceNum();
		
		
		String Status=null;
		String obj=null;
		
		int mintDetailCount=0;
		//int mintSubStepNo=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		
		String path=System.getProperty("user.dir");
		String testData_Excel_path=path+File.separator+"src"+File.separator+"Modules"+File.separator+"Regression"+File.separator+"TestData"+File.separator+"Generic_RMTestData_4.xls";
		String classname= this.getClass().getSimpleName();
		webdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Mobile_HTML_Report Tc10=new Mobile_HTML_Report(webdriver,DeviceName,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,gameName);
		
		MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, Tc10, gameName);
		
		WebDriverWait wait = new WebDriverWait(webdriver, 50);
		try{
			String applicationName = gameName;
			// Step 1 : open lobby 
			Tc10.detailsAppend("Verify Spain Regulatory Market features", "Spain Regulatory Market should be displayed", "", "");
			if(webdriver!=null)
			{
					String Spain_url= cfnlib.xpathMap.get("Spain_url");
						obj=cfnlib.funcNavigate(Spain_url);
						if(obj!=null)
						{
							Tc10.detailsAppend("Open browser and Enter lobby URL in address bar and click Enter", "Spain Lobby should be displayed", "Spain lobby is displayed", "Pass");
						}
						else
						{
							Tc10.detailsAppend("Open browser and Enter lobby URL in address bar and click Enter", "Spain lobby should be displayed", "Spain is not displayed", "Fail");
						}
						String GameTitle=cfnlib.loginToRegulatoryMarketSpain(cfnlib.xpathMap.get("Spain_username"),"test");
						if(GameTitle!=null)
						{
							Tc10.detailsAppend("Check that user is able to login with username and password and  Title verified ", "User should be logged in successfully and "+applicationName+" Game should launch ", "Logged in succesfully and "+applicationName+" is launched. ", "Pass");
						}
						else
						{
							Tc10.detailsAppend("Check that user is not  able to login with username and password", "User should be logged in successfully and "+applicationName+" Game should be launched ", "Logged in succesfully and "+applicationName+" is not launched. ", "Fail");
						}
						
						if(framework.equalsIgnoreCase("Force")){
							cfnlib.setNameSpace();
							}
						cfnlib.checkPage(Tc10);// To check if Start Session Button is Present.
										
						boolean RegulatoryMarket=cfnlib.overLay();
						if(RegulatoryMarket)
						{
							Tc10.detailsAppend("Verify the Slot Game Limits Overlay appears before the game loads ", "Overlay should appear before the game loads", "Overlay is appearing before the game loads", "Pass");
						}
						else
						{
							Tc10.detailsAppend("Verify the Slot Game Limits Overlay appears before the game loads ", "Overlay should appear before the game loads", " Overlay is not appearing before the game loads ", "Fail");
						}	

						String lossLimit = cfnlib.xpathMap.get("LossLimitValidData"); 
						boolean b = cfnlib.fillStartSessionLossForm(lossLimit);
						if(b)
				          {
							Tc10.detailsAppend("Fill in valid values in all the fields and click on Set Limit button", "User should navigate go base scene", "Base scene is displayed successfully", "Pass");
						  }
						else 
						  {
						   	Tc10.detailsAppend("Fill in valid values in all the fields and click on Set Limit button", "User should navigate go base scene", "Base scene is not displayed", "Fail");
						  }
						cfnlib.funcFullScreen();
						cfnlib.settingsOpen();
						
						 
						boolean quickspin=cfnlib.verifyQuickSpin();   
							if(!quickspin)
							{
								Tc10.detailsAppend("Verify the Quick Spin option is not available in Spain Regulatory Markets.  ", "Quick Spin  Option should not display .", "Quick Spin  Option is not gettting displayed in " +applicationName+"", "Pass");
							}
							else
							{
								Tc10.detailsAppend("Verify the Quick Spin option is not available in Denmark Regulatory Markets.", "Quick Spin  Option should not display . ", " Quick Spin  Option is gettting displayed in " +applicationName+" ", "Fail");
							}
						cfnlib.settingsBack();
						cfnlib. verifyResponsibleGamingNavigation(Tc10);
						cfnlib.funcFullScreen();
						String header = cfnlib.waitUntilSessionReminder(Tc10);
						if(header!= null)
				          {
							Tc10.detailsAppend("To check if session reminder overlay appear after reminder period", "Session reminder overlay should appear after reminder period", "Sessionreminder overlay appear after reminder period", "Pass");
						  }
						else 
						  {
						   	Tc10.detailsAppend("To check if session reminder overlay appear after reminder period", "Session reminder overlay should appear after reminder period", "Sessionreminder overlay doesn't appear after reminder period", "Fail");
						  }
						
						cfnlib.spainContinueSession();
	
						//wait for session reminder of loss limit when loss limit reach
						String title = cfnlib.waitUntilSessionLoss();
						if(title!= null)
				          {
							Tc10.detailsAppend("To check if Loss Limit reached overlay appear", "Loss Limit reached overlay should appear after loss limit is reached", "Loss Limit reached overlay appears after loss limit is reached", "Pass");
						  }
						else 
						  {
						   	Tc10.detailsAppend("To check if Loss Limit reached overlay appear", "Loss Limit reached overlay should appear after loss limit is reached", "Loss Limit reached overlay doesn't appear after loss limit is reached", "Fail");
						  }
						//To close session loss pop up
						cfnlib.closeSessionLossPopup(Tc10);
						
						//To verify cooling off period for same game
						cfnlib.coolingOffPeriod(Tc10);
						
						//To verify cooling off period for another game
						cfnlib.coolingOffPeriodNewGame(Constant.INCOMPLETEGAMENAME, Tc10);
						log.debug("Waiting for cooling preiod to over");
						
						
						//wait till cooling off period finishes.
						String coolingoffperiod= cfnlib.xpathMap.get("CoolingOffPeriodinMinute");
						String coolingoffperiod2 = coolingoffperiod.replaceAll("[m\\s]", "");								
						long coolingoffperiod3 = Long.parseLong(coolingoffperiod2);
						Thread.sleep(coolingoffperiod3*60*1000);
						
						log.debug("cooling preiod is over");

						System.out.println("Cooling off period is over");
						
						//Tc10.details_append("Verify Time Limit breach Scenario and Settings Page in Spain Regulatory Market", "Time Limit breach Scenario and Settings Page should work correctly in Spain Regulatory Market", "", "");
						
						cfnlib.relaunchGame();						
						
						cfnlib.checkPage(Tc10);// To check if Start Session Button is Present.
						
						cfnlib.overLay();			
				
						cfnlib.fillStartSessionLossForm(lossLimit);
						cfnlib.funcFullScreen();						

						
						
						//To wait until time limit is reached
						String timeLimit= cfnlib.xpathMap.get("TimeLimitinMinute");
						String timeLimit2 = timeLimit.replaceAll("[m\\s]", "");								
						long timeLimit3 = Long.parseLong(timeLimit2);
						Thread.sleep(timeLimit3*60*1000);
						
						System.out.println("Time Limit has reached");
						
						cfnlib.waitUntilTimeLimitSession(Tc10);
						
						cfnlib.coolingOffPeriod(Tc10);
						
						
						String logout = cfnlib.Func_logout_OD();
						
						if (logout.trim() != null) {
							Tc10.detailsAppend("Verify that user has successfully logged out ",
									"User should be successfully logged out", "User is Successfully logged out", "pass");
						} else {
							Tc10.detailsAppend("Verify that user has successfully logged out ",
									"User should be successfully logged out", "User is not Successfully logged out", "fail");
						}
						
					/*	//wait for coolingoff period - 2; as already above code will take 2 min to execute
						Thread.sleep((coolingoffperiod3-2)*60*1000);
						System.out.println("Cooling off period is over Again");*/
						
					/*	Tc10.details_append("Verify Incomplete Game Dialog Scenario in Spain Regulatory Market", "Incomplete Game Dialog Scenario should work correctly in Spain Regulatory Market", "", "");
						
						cfnlib.relaunchGame();*/
																	
		
		//-------------------Handling the exception---------------------//
			/*	catch (Exception e) {
			//ExceptionHandler eHandle=new ExceptionHandler(e,webdriver,Tc10);
			//eHandle.evalException();
			log.error(e.getMessage(),e);
			cfnlib.evalException(e);
		}
		finally{

			//Global.browserproxy.abort();
			//---------- Closing the report----------//

			Tc10.endReport();
			//---------- Closing the webdriver ----------//
			webdriver.close();
			webdriver.quit();
			//Global.appiumService.stop();
		}
	}
}*/