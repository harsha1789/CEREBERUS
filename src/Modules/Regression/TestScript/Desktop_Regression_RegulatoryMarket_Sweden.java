package Modules.Regression.TestScript;



import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.TestPropReader;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;
/**
 * This script verifies the Sweden check points.
 * Sweden Market Check Points :
	  =============================================
		Reel Spin Duration
		Top Bar is visible or Not
		currency format 
		currency symbol
		Compare the  Game Name
		Compare the Bet Value
		verify session duration
		Verify Clock on the Top Bar
		Verify Help link on top bar
		Verify the Quick Spin is present or not
		Verify the Stop is present or not
		Click on Help & Navigate to that page
		Verify top three logos on top bar and navigate
		Sweden - language translation for autoplay,bet, 
				Settings panels and paytable
		
	Set Session reminder  - 2 minutes
 * @author pb61055
 *
 */
public class Desktop_Regression_RegulatoryMarket_Sweden {
	public ScriptParameters scriptParameters;
	Logger log = Logger.getLogger(Desktop_Regression_RegulatoryMarket_Sweden.class.getName());
	public void script() throws Exception {
		String mstrTCName=scriptParameters.getMstrTCName();
		String mstrTCDesc=scriptParameters.getMstrTCDesc();
		String mstrModuleName=scriptParameters.getMstrModuleName();
		WebDriver webdriver=scriptParameters.getDriver();
		BrowserMobProxyServer proxy=scriptParameters.getProxy();
		String startTime=scriptParameters.getStartTime();
		String filePath=scriptParameters.getFilePath();
		String username=scriptParameters.getUserName();
		String browserName=scriptParameters.getBrowserName();
		String framework=scriptParameters.getFramework();
		String gameName=scriptParameters.getGameName();
		String languageCode="en";
		String languageCode2="sv";
		String languageDescription="Sweden";
		String status=null;
		int mintDetailCount=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		String userName ;
		String regMarket = "Sweden";
		int productId = 5143;
		String currencyIsoCode = "SEK";
		int marketTypeID = 6;
		double balance = 10000;

		Desktop_HTML_Report reportSweden = new Desktop_HTML_Report(webdriver, browserName, filePath, startTime, mstrTCName,
				mstrTCDesc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings, mintStepNo, status,
				gameName);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,reportSweden, gameName);

		CommonUtil util = new CommonUtil();
		
		
		try 
		{
			if (webdriver != null) 
			{	
				for(int i=1;i<3;i++)
				{
					int envId=Integer.parseInt(TestPropReader.getInstance().getProperty("EnvironmentID"));
					
					userName=util.randomStringgenerator();
					util.randomUserCreationForRegMarkets(userName, regMarket, envId, productId,currencyIsoCode,marketTypeID);
					util.updateUserBalance(userName, balance);
					String url = cfnlib.XpathMap.get("Sweden_Url");
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
						LaunchURl=	LaunchURl.replace("languagecode=en","languagecode=de");
					else if(LaunchURl.contains("languageCode"))
						LaunchURl =LaunchURl.replace("languageCode=en","languageCode=de");
						
					log.debug("url = " +LaunchURl);
					cfnlib.loadGame(LaunchURl);	
				}
					
					Thread.sleep(2000);
					
						if(framework.equalsIgnoreCase("Force"))
						{
							cfnlib.setNameSpace();
						}

						Thread.sleep(2000);
						
						// click on continue button if any
						if((Constant.YES.equalsIgnoreCase(cfnlib.XpathMap.get("continueBtnOnGameLoad"))))
						{
							cfnlib.newFeature();
						}		
						
						
						//To verify whether session reminder is present or not
						boolean sessionReminderVisible=cfnlib.sessionReminderPresent();
						Thread.sleep(1000);
						if(sessionReminderVisible) 
						{							
							cfnlib.selectContinueSession();
							Thread.sleep(2000);
						}
						else
						{
							log.debug("Session reminder is not visible on screen");
							Thread.sleep(2000);
						}
						
						//To verify currency symbol and currency format
						String currencyName = cfnlib.XpathMap.get("swedenCurrencyName");
						String currencyFormat=cfnlib.XpathMap.get("swedenCurrencyFormat");
						String currencySymbol = cfnlib.getCurrencySymbolForSweden();
						log.info(currencyName + "currency symbol is " + currencySymbol);
						String swedishKronaSymbol= cfnlib.XpathMap.get("swedenCurrencySymbol");
						if(swedishKronaSymbol.equals(currencySymbol))
						{
							reportSweden.detailsAppend("Verify that currency symbol in credit is swedish krona ",
									"Credit should display in correct currency symbol : kr " ,
									"Credit is displaying in correct currency symbol " +currencySymbol, "Pass");
						}
						else
						{
							reportSweden.detailsAppend("Verify that currency symbol in credit is swedish krona ",
									"Credit should display in swedish krona currency symbol : kr " ,
									"Credit is not displaying in swedish krona currency symbol " +currencySymbol, "Fail");
						}

						// verify currency format in credits
						boolean result=cfnlib.verifyCurrencyFormatForSweden(currencyFormat);

						if (result) {
							reportSweden.detailsAppend("Verify that currency format in credit ",
									"Credit should display in correct currency format " ,
									"Credit is displaying in correct currency format ", "Pass");
						} else {
							reportSweden.detailsAppend("Verify that currency format in credit ",
									"Credit should display in correct currency format " ,
									"Credit is not displaying in correct currency format ", "Fail");
						}
						
						
						if(i!=1)
						{
							if(!gameName.contains("Scratch") && cfnlib.checkAvilability(cfnlib.XpathMap.get("isMenuPaytableBtnVisible"))){
								//Open payatable and capture screen shots
								cfnlib.capturePaytableScreenshot(reportSweden, languageCode2);
								// Closes the paytable
								cfnlib.paytableClose();
								}
							
							// Open and Capture Screen shot of Bet Screen
							boolean b = cfnlib.open_TotalBet();
							if (b){
								Thread.sleep(500);
								reportSweden.detailsAppendFolder("Verify language on the Bet Amount Screen", "Bet Amount Screen should be display", "Bet Amount Screen should be displayed in " +languageDescription+ " language ", "Pass", languageCode2);
							}
							else{
								reportSweden.detailsAppendFolder("Verify language on the Bet Amount Screen", "Bet Amount Screen should be display", "Bet Amount Screen doesn't display", "Fail", languageCode2);   
							}
							// Close the Bet Screen
							cfnlib.close_TotalBet();
							
						
							// Open and Capture Screen shot of Autoplay Screen
							boolean openAutoplay = cfnlib.openAutoplay();
							if (openAutoplay){
								reportSweden.detailsAppendFolder("Verify language on the Autoplay Screen", "Autoplay Screen should be display", "Autoplay Screen should be displayed", "Pass", languageCode2);
							}
							else{
								reportSweden.detailsAppendFolder("Verify language on the Autoplay Screen", "Autoplay Screen should be display", "Autoplay Screen doesn't display", "Fail", languageCode2);   
							}
							// Close Autoplay
							cfnlib.close_Autoplay();


							//check first whether setting avilable or not
							if(!framework.equalsIgnoreCase("CS")&& cfnlib.checkAvilability(cfnlib.XpathMap.get("isMenuSettingsBtnVisible"))){
							boolean openSetting= cfnlib.settingsOpen();
							if(openSetting){
								reportSweden.detailsAppendFolder("Verify that Language on settings screen is " + languageDescription + " ", "Language in Settigns Screen should be " +languageDescription+ " ", "Language inside Settings screens is " +languageDescription+ " ", "pass", languageCode2);
							}
							else {
								reportSweden.detailsAppendFolder("Verify that Language back on menu is " + languageDescription +"should be display properly on menu links", "Language inside Menu Links should be in " +languageDescription+ " language", "Language inside Menu Links is displaying", "fail", languageCode2);
							}

							cfnlib.settingsBack();
							}
							
							Thread.sleep(2000);
						}
						Thread.sleep(2000);
						
						boolean isTopBarVisible=cfnlib.isTopBarVisible();
						Thread.sleep(2000);
						if(isTopBarVisible) 
						{	
							reportSweden.detailsAppend("Verify TopBar must be is displayed", "TopBar should be displayed", "TopBar is displayed", "pass");
							
							//verify game name on top bar
							cfnlib.gameNameOnTopBar(reportSweden);
							Thread.sleep(2000);
							
							//Verifying bet value on Top bar
							cfnlib.betOnTopBar(reportSweden);
							Thread.sleep(2000);
							
							//verifying help text link on top bar
							cfnlib.verifyHelpTextlink(reportSweden);
							Thread.sleep(2000);
							
							//verify session duration on topbar
							cfnlib.verifySessionDurationOnTopBar(reportSweden);
							Thread.sleep(2000);
							
							//verify balance on topbar
							//cfnlib.verifyBalanceOnTopBar(reportSweden);
							//Thread.sleep(2000);
							
							// Verify sweden logos on top bar
							cfnlib.swedenRegMarketLogosFromTopBar(reportSweden);
							
							//verify 
							cfnlib.verifyMenuOptionNavigationsForSweden(reportSweden);
							Thread.sleep(2000);
							
						}
						
						//Verify the quick spin button availability
						boolean isquickspin=cfnlib.isQuickspinAvailable();
						Thread.sleep(1000);
						if(!isquickspin)
						{	
							reportSweden.detailsAppend("Verify that Quickspin must not be available in Base Scene", "Quickspin must not be available in BaseScene", "Quickspin is not available in Base Scene", "pass");
							
						}
						else
						{
							reportSweden.detailsAppend("verify that Quickspin must not be available in Base Scene", "Quickspin must not be available in Base Scene", "Quickspin is available in BaseScene", "fail");
						}	
						Thread.sleep(2000);
						
						//Verify the stop button availability
						boolean stopbutton=cfnlib.isStopButtonAvailable();
						if(!stopbutton)
						{
							reportSweden.detailsAppend("Verify the Stop button is not available in Spain Regulatory Markets.", "Stop Button should not display.", "Stop Button is not gettting displayed in game", "Pass");
						}
						else
						{
							reportSweden.detailsAppend("Verify the Stop button is not available in Spain Regulatory Markets.", "Stop Button should not display.", " Stop Button is gettting displayed in game", "Fail");

						}
						Thread.sleep(2000);
						
						//verify reel spin duration
						long isReelspin=cfnlib.reelSpinDuration();
						if(isReelspin >= 3000)
						{
							if(isReelspin<=4000) 
							{
								reportSweden.detailsAppend("Verify Reel spin duration for sweden is greater than 3 and less than 4 seconds", "Reel spin duration for sweden should be greater than 3 and less than 4 seconds " , "Reel spin duration for sweden is correct, "+isReelspin+" milliseconds", "pass");			
								
							}
						}
						else
						{
							reportSweden.detailsAppend("Verify Reel spin duration for sweden is greater than 3 and less than 4 seconds", "Reel spin duration for sweden should be greater than 3 and less than 4 seconds " , "Reel spin duration for sweden is incorrect, "+isReelspin+" milliseconds", "fail");			
						}
						Thread.sleep(2000);
						
						
						//To verify whether session reminder is present or not
						boolean sessionReminderUpdated=cfnlib.sessionReminderPresent();
						Thread.sleep(1000);
						if(sessionReminderUpdated) 
						{
							reportSweden.detailsAppend("Verify Session Reminder must be present","Session Reminder must be present","Session Reminder is present","pass");
							Thread.sleep(2000);
							
							//To click on continue button in session reminder
							cfnlib.selectContinueSession();
							Thread.sleep(2000);
						}
						else
						{
							reportSweden.detailsAppend("Verify Session Reminder must be present","Session Reminder must be present","Session Reminder is not present","fail");
							System.out.println("Session reminder is not visible on screen");
							log.debug("Session reminder is not visible on screen");
							Thread.sleep(2000);
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

			// ---------- Closing the report----------//
			reportSweden.endReport();
			
			// ---------- Closing the webdriver ----------//
			webdriver.close();
			webdriver.quit();

		}
	}
}
	
	