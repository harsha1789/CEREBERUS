package Modules.Regression.TestScript;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.Constant;

import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 * This Script traverse all the panels and take the screen shot of the all screens such as paytable, bet settings, auto play etc.
 * It reads the test data excel sheet for configured languages.
 * 
 */

public class Mobile_Regression_BaseScene {
	Logger log = Logger.getLogger(Mobile_Regression_BaseScene.class.getName());
	public ScriptParameters scriptParameters;

	
	public void script() throws Exception{
		String mstrTC_Name=scriptParameters.getMstrTCName();
		String mstrTC_Desc=scriptParameters.getMstrTCDesc();
		String mstrModuleName=scriptParameters.getMstrModuleName();
		BrowserMobProxyServer proxy=scriptParameters.getProxy();
		String startTime=scriptParameters.getStartTime();
		String filePath=scriptParameters.getFilePath();
		
		AppiumDriver<WebElement> webdriver=scriptParameters.getAppiumWebdriver();
		
		String DeviceName=scriptParameters.getDeviceName();
		String userName=scriptParameters.getUserName();
		String framework=scriptParameters.getFramework();
		String gameName=scriptParameters.getGameName();
		int checkedOutDeviceNum=scriptParameters.getCheckedOutDeviceNum();
		String classname= this.getClass().getSimpleName();
		String xmlFilePath= System.getProperty("user.dir")+"\\src\\Modules\\Regression\\TestData\\GameData\\"+classname+".testdata";

		
		String Status=null;
		int mintDetailCount=0;
		//int mintSubStepNo=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		

		Mobile_HTML_Report sanityreport=new Mobile_HTML_Report(webdriver,DeviceName,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,gameName);

		
		MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, sanityreport, gameName);
		
		
		try{
			// Step 1
			if(webdriver!=null)
			{		
				Map<String, String> rowData2 = null;
				Map<String, String> rowData3 = null;

				
				String url = cfnlib.xpathMap.get("ApplicationURL");
				String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
				System.out.println("url = " +LaunchURl);
				cfnlib.loadGame(LaunchURl);
				
				sanityreport.detailsAppend("Sanity Suite Test cases", "Sanity Suite Test cases", "", "");
				//cfnlib.Func_navigate(url);
				// uncomment below block of code if launching th game from lobby
				/*String obj = cfnlib.Func_navigate(url);
				if(obj!=null)
				{
					language.details_append("Open Browser and Enter Lobby URL in address bar and click Enter", "Island Paradise Lobby should open", "Island Paradise lobby is Open", "Pass");
				}
				
				else
				{
					language.details_append("Open browser and Enter Game URL in address bar and click Enter", "Island Paradise lobby should be displayed", "Island paradise is not displayed", "Fail");
				}*/
				// Step s1.2 Login to application and verify the game title
			/*	String password = constant.Password;
				String GameTitle= cfnlib.Func_LoginBaseScene(userName, password);		
				if(GameTitle!=null)
				{
					language.details_append("Verify that the application should display Game Logo and game name in Base Scene in Practice Play", "Game logo and game name with base scene should display", "Game logo and game name with base scene displays", "Pass");
				}
				else {
					language.details_append("Verify that the application should Logo and game name in Base scene in Practice Play", "Game logo and game name with base scene should display", "Game logo and game name with base scene is not display", "Fail");
				}*/
				
				if(framework.equalsIgnoreCase("Force")){
					cfnlib.setNameSpace();
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
				
				cfnlib.waitForSpinButton();
				Thread.sleep(1000);
				//verify  spin button state with quick spin scenario
				cfnlib.verifySpinBtnState(sanityreport);
				
				// verifying autoplay scenarioes
				
				cfnlib.verifyAutoplayConsoleOptions(sanityreport);
				
				// verify  the auto play panel 
				
				cfnlib.verifyAutoplayPanelOptions(sanityreport);
				
				// Below code is commmented as this scenarioes inclueded in Regression suite
				
				// check Autoplay on refresh
				boolean isAutoplayOnAfterRefresh=cfnlib.isAutoplayOnAfterRefresh();
				
				if(isAutoplayOnAfterRefresh){
					sanityreport.detailsAppend("Verify that is Autoplay On AfterRefresh", "Is Autoplay continue after Refresh", "Autoplay continue after Refresh", "Fail");
					log.debug("FAIL");
				}else{
					sanityreport.detailsAppend("Verify that is Autoplay On AfterRefresh", "Is Autoplay continue after Refresh", "Autoplay does not continue after Refresh", "Pass");
					log.debug("PASS");
				}
				
				boolean isAutoplayPauseOnFocusChange=cfnlib.isAutoplayPauseOnFocusChange(sanityreport);
				if(isAutoplayPauseOnFocusChange){
					sanityreport.detailsAppend("Verify that is Autoplay Pause On Focus Change", "Is Autoplay Pause On Focus Change", "Autoplay Pause On Focus Change", "Pass");
				}else{
					sanityreport.detailsAppend("Verify that isAutoplay Pause On Focus Change", "Autoplay Pause On Focus Change", "Autoplay not Pause On Focus Change", "Fail");
				}
				
				//Trigger freespins on autoplay and check the autoplay status		
				boolean isAutoplayStoppedOnMenuClick = cfnlib.isAutoplayStoppedOnMenuClick();
				if(isAutoplayStoppedOnMenuClick){
					sanityreport.detailsAppend("Verify that is Autoplay Stopped On MenuClick", "Is Autoplay Stopped On MenuClick", "Autoplay Stopped On MenuClick", "Pass");
					log.debug("PASS");
				}else{
					sanityreport.detailsAppend("Verify that is Autoplay Stopped On MenuClick", "Is Autoplay Stopped On MenuClick", "Autoplay has not Stopped On MenuClick", "Fail");
					log.debug("FAIL");
				}	
				
				cfnlib.verifyALLBetValues(sanityreport);
	
				
				//Lets check the Quick bets
				cfnlib.verifyAllQuickBets(sanityreport);
				
				//Change Bet and refresh with out spin and verify previous bet value present or not
				boolean isBetChangedOnRefresh = cfnlib.isBetChangedOnRefresh();
				
				 if(isBetChangedOnRefresh){
					 sanityreport.detailsAppend("Verify that is Bet Changed On Refresh", "Is Bet Changed On Refresh","Bet Changed On Refresh", "Fail");	
					 System.out.println("isBetChangedOnRefresh :: FAIL");
					}else{
						sanityreport.detailsAppend("Verify that is is Bet Changed On Refresh", "Is Bet Changed On Refresh","Bet not Changed On Refresh", "Pass");	
						 System.out.println("isBetChangedOnRefresh :: PASS");
					}
				
					if(!gameName.contains("Scratch") && "no".equalsIgnoreCase(cfnlib.xpathMap.get("IsRespinFeature"))){
						//To validate Mini Paytable for all bets
						String curntBet;
						try {
							cfnlib.openBetPanel();
							Thread.sleep(1000);

							curntBet = cfnlib.setMaxBet();
							
							Thread.sleep(1000);
							cfnlib.closeTotalBet();
							
							
							
							String minimumbet= cfnlib.getMinimumBet();
							String betValue = curntBet;

							do {

								curntBet = betValue;
								
								cfnlib.validateMiniPaytable(curntBet,sanityreport);
								if((Class.forName(Thread.currentThread().getStackTrace()[1].getClassName())).toString().contains("Modules.Regression.TestScript.Mobile_Regression_BaseScene"))
								{
								
								cfnlib.openBetPanel();
								Thread.sleep(1000);

								betValue = cfnlib.setTheNextLowBet();
								Thread.sleep(2000);
								cfnlib.closeTotalBet();
								}
								else
								{
								//	cfnlib.close_TotalBet();
									break;
								}
							} while (!betValue.equalsIgnoreCase(minimumbet));
						} catch (Exception e) {
							
							e.printStackTrace();
						}
						
				 }
				 if(!gameName.contains("Scratch"))
				 {
				// paytable  verification
					cfnlib.capturePaytableScreenshot(sanityreport, "paytable");
					cfnlib.paytableClose();
				// Payout verification
				 cfnlib.Payoutvarificationforallbet(sanityreport);
				 }
				 cfnlib.verifyMenuOptionNavigations(sanityreport);
				 
				 
				}
			
		}
		//-------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		finally
		{
			sanityreport.endReport();
			webdriver.close();
			webdriver.quit();
			//proxy.abort();
			Thread.sleep(3000);
		}	
	}
}