package Modules.Regression.TestScript;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 * This script verifies the Spain check points.
 *  Spain Market Check Points :
   =============================================
   
    1.verify the overlay appears before the game loads.
    2.verify cooling of period is set
	3.verify the reminder period and loss limit balance.
	4.Stop button not available.
	5.Quick spin not available.
	6.verify  game works with session reminder dialog.
	7.verify game name, clock and help link on top bar
	8.verify help is opening from menu on top bar
	9..Verify wagers,balance and payouts updation
	10.Verify game works until loss limit dialog
	11.Verify cooling off period dialog displays after game
	12.Currency
	
	Requires Test data: 5 spins ( depends on the game bet values)
	 =============================================
	1st spin no win - to verify stop button
	2nd spin no win- to check reel spin duration
	3rd spin win greater than bet  - to check payouts, wagers and balance is upadating after win
	4th spin no win - to check payouts, wagers and balance is upadating after no win
	5th spin spins no win - waiting for loss limit
 * @author pb61055
 *
 */


public class Desktop_Regression_RegulatoryMarket_Spain {
	public ScriptParameters scriptParameters;
	Logger log = Logger.getLogger(Desktop_Regression_RegulatoryMarket_Spain.class.getName());
	public void script() throws Exception {
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
		String userName=scriptParameters.getUserName();
		String status=null;
		int mintDetailCount=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		String regMarket = "Spain";
		int envId = 1658;
		int productId = 5104;
		String currencyIsoCode = "EUR";
		int marketTypeID = 4;

		Desktop_HTML_Report reportSpain = new Desktop_HTML_Report(webdriver, browserName, filePath, startTime, mstrTCName,
				mstrTCDesc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings, mintStepNo, status,
				gameName);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,reportSpain, gameName);

		CommonUtil util = new CommonUtil();
		int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int  cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDDesktop"));
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
						String url = cfnlib.XpathMap.get("Spain_url");
						
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
								LaunchURl=	LaunchURl.replace("languagecode=en","languagecode=es");
							else if(LaunchURl.contains("LanguageCode"))
								LaunchURl =LaunchURl.replace("LanguageCode=en","LanguageCode=es");
							
							log.debug("url = " +LaunchURl);
							cfnlib.loadGame(LaunchURl);	
						}
						if(framework.equalsIgnoreCase("Force"))
						{
							cfnlib.setNameSpace();
						}

						Thread.sleep(2000);
												
						//verify Slot game limit overlay visible
						boolean isSlotGameLimitPresent=cfnlib.slotGameLimitsOverlay();
						if(isSlotGameLimitPresent)
						{
							reportSpain.detailsAppend("Verify the Slot Game Limits Overlay appears before the game loads ", "Overlay should appear before the game loads", "Overlay is appearing before the game loads", "Pass");
						}
						else
						{
							reportSpain.detailsAppend("Verify the Slot Game Limits Overlay appears before the game loads ", "Overlay should appear before the game loads", " Overlay is not appearing before the game loads ", "Fail");
						}
						
						//Get the loss limit data to fill Slot game limits form
						String lossLimitFromExcel = cfnlib.XpathMap.get("LossLimitValidData");
						
						//Adding two zeros after decimal point
						DecimalFormat df=new DecimalFormat("#.00");
						String lossLimit=df.format(Double.parseDouble(lossLimitFromExcel));
						
						Thread.sleep(3000);
						
						//fill the Slot game limits form
						boolean b = cfnlib.fillStartSessionLossForm(lossLimit ,reportSpain);
						if(b)
				          {
							reportSpain.detailsAppend("Fill in valid values in all the fields and click on Set Limit button", "Overlay is closed", "Overlay is closed successfully", "Pass");
						  }
						else 
						  {
							reportSpain.detailsAppend("Fill in valid values in all the fields and click on Set Limit button", "Overlay is closed", "Overlay is not closed", "Fail");
						  }
						
						//to set Spain cooling of period
						cfnlib.spainCoolingOffPeriod(reportSpain);
						
						// Check continue button exists and clicks
						if((Constant.YES.equalsIgnoreCase(cfnlib.XpathMap.get("continueBtnOnGameLoad"))))
						{
							cfnlib.newFeature();
						}
						
						//Verify the quick spin button availability
						boolean isquickspin=cfnlib.isQuickspinAvailable();
						Thread.sleep(1000);
						if(!isquickspin)
						{	
							reportSpain.detailsAppend("Verify that Quickspin must not be available in Base Scene", "Quickspin must not be available in BaseScene", "Quickspin is not available in Base Scene", "pass");
							
						}
						else
						{
							reportSpain.detailsAppend("verify that Quickspin must not be available in Base Scene", "Quickspin must not be available in Base Scene", "Quickspin is available in BaseScene", "fail");
						}	
						Thread.sleep(2000);
						
						//Verify the stop button availability
						boolean stopbutton=cfnlib.isStopButtonAvailable();
						if(!stopbutton)
						{
							reportSpain.detailsAppend("Verify the Stop button is not available in Spain Regulatory Markets.", "Stop Button should not display.", "Stop Button is not gettting displayed in game", "Pass");

						}
						else
						{
							reportSpain.detailsAppend("Verify the Stop button is not available in Spain Regulatory Markets.", "Stop Button should not display.", " Stop Button is gettting displayed in game", "Fail");

						}
						
						//verify session reminder overlay is present
						boolean header = cfnlib.waitUntilSessionReminder();
						if(header)
				          {
							reportSpain.detailsAppend("To check if session reminder overlay appear after reminder period", "Session reminder overlay should appear after reminder period", "Sessionreminder overlay appear after reminder period", "Pass");
						  }
						else 
						  {
							reportSpain.detailsAppend("To check if session reminder overlay appear after reminder period", "Session reminder overlay should appear after reminder period", "Sessionreminder overlay doesn't appear after reminder period", "Fail");
						  }
						
						//click on continue in session reminder
						cfnlib.spainContinueSession();
						System.out.println("Session reminder closed");
						Thread.sleep(2000);
						
						//verify game name on top bar
						cfnlib.gameNameOnTopBar(reportSpain);
						Thread.sleep(2000);
						
						//verify help text link on top bar
						cfnlib.verifyHelpTextlink(reportSpain);
						Thread.sleep(2000);
						
						//verify clock on top bar
						cfnlib.clockOnTopBar(reportSpain);
						Thread.sleep(2000);
						
						//verify menu navigation 
						cfnlib.verifyMenuOptionNavigationsForSpain(reportSpain);
						
						//verify reel spin duration
						long isReelspin=cfnlib.reelSpinDuration();
						if(isReelspin > 3000)
						{
							reportSpain.detailsAppend("Verify Reel spin duration for spain is greater than 3 seconds", "Reel spin duration for spain should be greater than 3 seconds " , "Reel spin duration for spain is correct, "+isReelspin+" milliseconds", "pass");			
						}
						else
						{
							reportSpain.detailsAppend("Verify Reel spin duration for spain is greater than 3 seconds", "Reel spin duration for spain should be greater than 3 seconds " , "Reel spin duration for spain is incorrect, "+isReelspin+" milliseconds", "fail");			
						}
						
						
						//To verify currency symbol and currency format
						String currencyName = cfnlib.XpathMap.get("spainCurrencyName");
						String currencyFormat=cfnlib.XpathMap.get("spainCurrencyFormat");
						String currencySymbol = cfnlib.getCurrencySymbolForSpain();
						log.info(currencyName + "currency symbol is " + currencySymbol);
						String euroSymbol= cfnlib.XpathMap.get("spainCurrencySymbol");
						if(euroSymbol.equals(currencySymbol))
						{
							reportSpain.detailsAppend("Verify that currency symbol in credit is Euro ",
									"Credit should display in correct currency symbol : € " ,
									"Credit is displaying in correct currency symbol " +currencySymbol, "Pass");
						}
						else
						{
							reportSpain.detailsAppend("Verify that currency symbol in credit is Euro ",
									"Credit should display in euro currency symbol : € " ,
									"Credit is not displaying in euro currency symbol " +currencySymbol, "Fail");
						}

						// verify currency format in credits
						boolean result=cfnlib.verifyCurrencyFormatForSpain(currencyFormat);

						if (result) {
							reportSpain.detailsAppend("Verify that currency format in credit ",
									"Credit should display in correct currency format " ,
									"Credit is displaying in correct currency format ", "Pass",
									currencyName);
						} else {
							reportSpain.detailsAppend("Verify that currency format in credit ",
									"Credit should display in correct currency format " ,
									"Credit is not displaying in correct currency format ", "Fail");
						}
						
						//wait for slot game limit when loss limit reach
						boolean title = cfnlib.waitUntilSessionLoss(lossLimit,reportSpain);
						if(title)
				          {
							reportSpain.detailsAppend("Verify if Loss Limit Summary overlay appear",
									"Loss Limit Summary overlay should appear", "Loss Limit summary overlay appears", "Pass");}
						else 
						  {
							reportSpain.detailsAppend("To check if Loss Limit Summary overlay appear",
									"Loss Limit Summary overlay should appear", "Loss Limit summary overlay does not appear", "fail"); }
						
						//To close session loss overlay
						cfnlib.closeSessionLossPopup();	
						Thread.sleep(2000);
						
						//close required
						reportSpain.detailsAppend("To check if close required appear", "close required should appear", "close required appeared", "Pass");
						Thread.sleep(2000);
						
						//refresh the page
						webdriver.navigate().refresh();		
						
						//close cooling off period
						cfnlib.closeCoolingOffPeriod(reportSpain);
						Thread.sleep(2000);
						
					}
					else
					{
						log.debug("Unable to copy test data file on the environment hence skipping execution");
						reportSpain.detailsAppend("", "", "Unable to copy test data on server,hence skipping futher execution", "fail");
					}
				}//end of for loop
				
			}//end of if block 
			
	}// end of try block
				
				
				
		//-------------------Handling the exception---------------------//		
		catch (Exception e) 
			{
				log.error(e.getMessage(),e);
				cfnlib.evalException(e);
			}
		//-------------------Closing the connections---------------//
		finally 
			{
				// Global.browserproxy.abort();
				// ---------- Closing the report----------//

				reportSpain.endReport();
				// ---------- Closing the webdriver ----------//

				webdriver.close();
				webdriver.quit();

			}
	}	
}
