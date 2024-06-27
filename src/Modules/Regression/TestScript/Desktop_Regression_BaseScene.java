package Modules.Regression.TestScript;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;


/**
 * Description:Regression suite
 * Test cases:
 * 1] Spin button status
 * 2] Autoply Console options
 * 3] Autoply panel options
 * 4] QuickBet panel
 * 5] Bet panel
 * 6] Menu option nevigation
 * 7] Mini paytable
 * 8] Bet satus on refresh
 * 9] Paytable screenshots
 * 10]Payout for bet
 * @author Premlata
 */

public class Desktop_Regression_BaseScene{

	Logger log = Logger.getLogger(Desktop_Regression_BaseScene.class.getName());
	public ScriptParameters scriptParameters;
	
	
	public void script() throws Exception{
		
		String mstrTC_Name=scriptParameters.getMstrTCName();
		String mstrTC_Desc=scriptParameters.getMstrTCDesc();
		String mstrModuleName=scriptParameters.getMstrModuleName();
		WebDriver webdriver=scriptParameters.getDriver();
		BrowserMobProxyServer proxy=scriptParameters.getProxy();
		String startTime=scriptParameters.getStartTime();
		String filePath=scriptParameters.getFilePath();
		String userName=scriptParameters.getUserName();
		String Browsername=scriptParameters.getBrowserName();
		String framework=scriptParameters.getFramework();
		String gameName=scriptParameters.getGameName();
		String Status=null;
		int mintDetailCount=0;
		//int mintSubStepNo=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		
		
		
		
		
		Desktop_HTML_Report language = new Desktop_HTML_Report(webdriver,Browsername,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,gameName);

		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, language, gameName);

		try{
			// Step 1 
			if(webdriver!=null)
			{		
				String url = cfnlib.XpathMap.get("ApplicationURL");
				String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
				System.out.println("url = " +LaunchURl);
				cfnlib.loadGame(LaunchURl);
			
				/*String obj = cfnlib.func_navigate(url);
				log.debug("opening webiste");

				if(obj!=null){
						language.details_append("Open Browser and Enter Lobby URL in address bar and click Enter", "Island Paradise Lobby should open", "Island Paradise lobby is Open", "Pass");
				}
				else {
						language.details_append("Open browser and Enter Lobby URL in address bar and click Enter", "Island Paradise lobby should Open", "Island paradise is not displayed", "Fail");
				}
				// Capture Screen shot for BaseScene after login

				String password = constant.Password;
				String GameTitle=cfnlib.Func_LoginBaseScene(userName, password);
				if(GameTitle != null){
						language.details_append("Verify that the application should display Game Logo and game name in Base Scene in Practice Play", "Game logo and game name with base scene should display", "Game logo and game name with base scene displays", "Pass");
				}
				else {
						language.details_append("Verify that the application should Logo and game name in Base scene in Practice Play", "Game logo and game name with base scene should display", "Game logo and game name with base scene is not display", "Fail");
				}
							cfnlib.newFeature();
							Thread.sleep(1500);
				 */
				if(framework.equalsIgnoreCase("Force")){
					cfnlib.setNameSpace();
				}

				cfnlib.newFeature();
				cfnlib.waitForSpinButton();
				//verify  spin button state with quick spin scenario

				cfnlib.verifySpinBtnState(language);


				//Lets start with Auto play scenarios
				cfnlib.verifyAutoplayConsoleOptions(language);

				//open the auto play panel and start spinning

				cfnlib.verifyAutoplayPanelOptions(language);		
				
				//include in regression test
				boolean isAutoplayOnAfterRefresh = cfnlib.isAutoplayOnAfterRefresh();
				if(isAutoplayOnAfterRefresh){
					language.detailsAppend("Verify that is Autoplay On AfterRefresh", "Is Autoplay continue  AfterRefresh", "Autoplay  does not continue  after Refresh", "Pass");
					
				}else{
					language.detailsAppend("Verify that is Autoplay On AfterRefresh", "Is Autoplay continue  after Refresh", "Autoplay  continue after Refresh", "Fail");
					
				}

				//Hooks required for KWIKWIN //include in regression test
				boolean isAutoplayPauseOnFocusChange = cfnlib.isAutoplayPauseOnFocusChange(language );
				if(isAutoplayPauseOnFocusChange){
					language.detailsAppend("Verify that is Autoplay Pause On Focus Change", "Is Autoplay Pause On Focus Change", "Autoplay Pause On Focus Change", "Pass");
				}else{
					language.detailsAppend("Verify that isAutoplay Pause On Focus Change", "Autoplay Pause On Focus Change", "Autoplay not Pause On Focus Change", "Fail");
				}
				 
				//include in regression test
						
				boolean isAutoplayStoppedOnMenuClick = cfnlib.isAutoplayStoppedOnMenuClick();
				if(isAutoplayStoppedOnMenuClick){
					language.detailsAppend("Verify that is Autoplay Stopped On MenuClick", "Is Autoplay Stopped On MenuClick", "Autoplay Stopped On MenuClick", "Pass");
					log.debug("PASS");
				}else{
					language.detailsAppend("Verify that is Autoplay Stopped On MenuClick", "Is Autoplay Stopped On MenuClick", "Autoplay has not Stopped On MenuClick", "Fail");
					log.debug("FAIL");
				}	

				//String currentUrl = webdriver.getCurrentUrl();
				
				
				
				cfnlib.verifyALLBetValues(language);

				
				//Lets check the Quick bets
				cfnlib.verifyAllQuickBets(language);


				//Change Bet and refresh with out spin and verify previous bet value present or not
				boolean isBetChangedOnRefresh = cfnlib.isBetChangedOnRefresh();

				if(isBetChangedOnRefresh){
					language.detailsAppend("Verify that is Bet Changed On Refresh", "Is Bet Changed On Refresh","Bet Changed On Refresh", "Fail");	
					System.out.println("isBetChangedOnRefresh :: FAIL");
				}else{
					language.detailsAppend("Verify that is is Bet Changed On Refresh", "Is Bet Changed On Refresh","Bet not Changed On Refresh", "Pass");	
					System.out.println("isBetChangedOnRefresh :: PASS");
				}
				if(!gameName.contains("Scratch") && "no".equalsIgnoreCase(cfnlib.XpathMap.get("IsRespinFeature"))){
					//To validate Mini Paytable for all bets
					
					String curntBet;
					try {
						cfnlib.openBetPanel();
						Thread.sleep(1000);

						curntBet = cfnlib.setMaxBet();
						Thread.sleep(1000);
						cfnlib.close_TotalBet();
						String minimumbet= cfnlib.getMinimumBet();
						String betValue = curntBet;

						do {
							
							curntBet = betValue;

							cfnlib.validateMiniPaytable(curntBet,language);
							if((Class.forName(Thread.currentThread().getStackTrace()[1].getClassName())).toString().contains("Modules.Regression.TestScript.Desktop_Regression_BaseScene"))
							{
								cfnlib.openBetPanel();
								Thread.sleep(1000);

								betValue = cfnlib.setTheNextLowBet();
								Thread.sleep(2000);
								cfnlib.close_TotalBet();
								System.out.println( "end time of minipaytable one bet="+System.currentTimeMillis());
							}
							else
							{
								//cfnlib.close_TotalBet();
								break;
							}
						} while (!betValue.equalsIgnoreCase(minimumbet));
					} catch (Exception e) {

						e.printStackTrace();
					}

				}
				if(!gameName.contains("Scratch"))
				// paytable  verification
				{cfnlib.capturePaytableScreenshot(language, "paytable");
				
				cfnlib.paytableClose();
				
				// Payout verification
				// check for max bet payout in sanity and include all in regression
				cfnlib.Payoutvarificationforallbet(language);
				}


				//To verify menu nevigations
				cfnlib.verifyMenuOptionNavigations(language);
			}
		}	    
		//-------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error(e.getMessage(),e);
			cfnlib.evalException(e);
		}
		//-------------------Closing the connections---------------//
		finally
		{
			language.endReport();
			webdriver.close();
			webdriver.quit();
			proxy.abort();
			Thread.sleep(1000);	   
		}	
	}
}
