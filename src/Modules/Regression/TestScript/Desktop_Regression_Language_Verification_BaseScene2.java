package Modules.Regression.TestScript;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.utils.Constant;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;

		
/**
 * This Script traverse all the panels and take the screen shot of the all screens such as paytable, bet settings, auto play etc.
 * It reads the test data excel sheet for configured languages.
 * @author Premlata
 */

public class Desktop_Regression_Language_Verification_BaseScene2{
	
	Logger log = Logger.getLogger(Desktop_Regression_Language_Verification_BaseScene2.class.getName());
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
				//url="https://mobilewebserver9-zensarqa.installprogram.eu/MobileWebGames/game/mgs/9_13_1_7236?lobbyURL=https%3A%2F%2Fmobilewebserver9-zensarqa.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FGames%2F3ReelSlots&moduleID=10430&clientID=50300&gameName=rugbyStarDesktop&gameTitle=Rugby+Star&LanguageCode=en&clientTypeID=40&casinoID=5007&lobbyName=IslandParadise&loginType=PlayerService&bankingURL=https%3A%2F%2Fmobilewebserver9-zensarqa.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FBanking&xmanEndPoints=https%3A%2F%2Fmobilewebserver9-zensarqa.installprogram.eu%2FXMan%2Fx.x&routerEndPoints=&disablePoweredBy=false&currencyFormat=&isPracticePlay=false&logoutURL=https%3A%2F%2Fmobilewebserver9-zensarqa.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FLogout&host=Desktop&apiCommsEnabled=false&launchToken=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL2FjY291bnQvdjEiLCJjaWQiOiI1MDA3IiwianRpIjoiMjg3OTUxNTM5ODE3MDY0NDY2NCIsIm91aWQiOiIweDE4ODkwMDAwIiwic3ViIjoiOTM5M2EyNGVkMTgxNGJmN2JjY2UwY2QwMWY0NzYzMzAiLCJjZXJ0IjoiTUdTLlVzZXJTZXNzaW9uVG9rZW4uQmx1ZW1lc2EuMjAxNTAzMzEiLCJpYXQiOiIxNTY5MDg4Mzk0IiwiYXVkIjoiSm9pblVzZXJTZXNzaW9uIn0.c7sxk9PH40XYdi0M3eDfmm0JBwz2aexfVS-di4mUiAa9UIpe2LKuJ5FJp4Np_XRGxRXNv2GyxSQnazIGkALbgQO5Xu4qmXwBHWqHRGwA_eq6jdLREeuegdPBNvufxYgxzJl3qnqgzPYE-uSONBRWkla68v2LU6_1SsJofpyh_tA&gaClientId=1513979440.1565593852&activityStatementURL=undefined&username=player73&password=test";
				//url="https://mobilewebserver9-zensardev.installprogram.eu/MobileWebGames/game/mgs/9_14_0_7270?lobbyURL=https%3A%2F%2Fmobilewebserver9-zensardev.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FGames%2F3ReelSlots&moduleID=10430&clientID=40300&gameName=rugbyStar&gameTitle=Rugby+Star&LanguageCode=en&clientTypeID=40&casinoID=5007&lobbyName=IslandParadise&loginType=PlayerService&bankingURL=https%3A%2F%2Fmobilewebserver9-zensardev.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FBanking&xmanEndPoints=https%3A%2F%2Fmobilewebserver9-zensardev.installprogram.eu%2FXMan%2Fx.x&routerEndPoints=&disablePoweredBy=false&currencyFormat=&isPracticePlay=false&logoutURL=https%3A%2F%2Fmobilewebserver9-zensardev.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FLogout&host=Desktop&apiCommsEnabled=false&launchToken=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL2FjY291bnQvdjEiLCJjaWQiOiI1MDA3IiwianRpIjoiMzYyOTM1Nzg3NjE1ODg4NDY4Iiwib3VpZCI6IjB4MThBQjAwMDAiLCJzdWIiOiIyN2NjODI5M2QyMDI0ZjVmYmUyZmE1MmViNGZmZjViYSIsImNlcnQiOiJNR1MuVXNlclNlc3Npb25Ub2tlbi5CbHVlbWVzYS4yMDE1MDMzMSIsImlhdCI6IjE1NjkzOTU5NjIiLCJhdWQiOiJKb2luVXNlclNlc3Npb24ifQ.0DUJymcZBtOzkfxcWQgoE8gAlncIJrYw6wHWEn2TRQjssZ2X29II7907SPfYPuUiROYjQMb3BYcttYmjU75cJgLasqyJcCvA4c7snuSf1lf_Gbqps0XtJHlGbgyh4f-aVV038p2g-WVXb0d2zJENQI-ef1nmx92MWGmhRHXDDTA&gaClientId=undefined&activityStatementURL=undefined&username=player684&password=test";
				//url="https://mobilewebserver9-zensarmill.installprogram.eu/MobileWebGames/game/mgs/9_12_1_7166?lobbyURL=https%3A%2F%2Fmobilewebserver9-zensarmill.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FGames%2F3ReelSlots&moduleID=18626&clientID=40300&gameName=luckyLeprechaunScratch&gameTitle=Lucky+Leprechaun+Scratch&LanguageCode=en&clientTypeID=40&casinoID=5007&lobbyName=IslandParadise&loginType=PlayerService&bankingURL=https%3A%2F%2Fmobilewebserver9-zensarmill.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FBanking&xmanEndPoints=https%3A%2F%2Fmobilewebserver9-zensarmill.installprogram.eu%2FXMan%2Fx.x&routerEndPoints=&disablePoweredBy=false&currencyFormat=&isPracticePlay=false&logoutURL=https%3A%2F%2Fmobilewebserver9-zensarmill.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FLogout&host=Desktop&apiCommsEnabled=false&launchToken=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL2FjY291bnQvdjEiLCJjaWQiOiI1MDA3IiwianRpIjoiLTQ4NjA3ODcyNjgwMDI3NTgxNzYiLCJvdWlkIjoiMHgxODc4MDAwMCIsInN1YiI6ImM5Nzk4MGUwNzVkZDQ4NGY5ODk0YzYzM2NlOWQ5YjJiIiwiY2VydCI6Ik1HUy5Vc2VyU2Vzc2lvblRva2VuLkJsdWVtZXNhLjIwMTUwMzMxIiwiaWF0IjoiMTU2OTgyMzAzMSIsImF1ZCI6IkpvaW5Vc2VyU2Vzc2lvbiJ9.fcSmh3hZ9EP5efVucEMN29nSnRhT-_QA6PHt5iXbOyk3rxrPerWFbLicloY4BNGsQ1oYBY9Jf7ffrlltmmqGorCkw5XI9Uop68kbiAZCaWD1A8pcQrucJf3sgN2FqEYrfIALAZgAvU2qkI1TDXIIrBh8RTE4ab67PjM86nMxZ38&gaClientId=1745905567.1569822849&activityStatementURL=undefined&username=player108&password=test";
				url="https://mobilewebserver9-zensarmillqa.installprogram.eu/MobileWebGames/game/mgs/9_15_0_7290?lobbyURL=https%3A%2F%2Fmobilewebserver9-zensarmillqa.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FGames%2F3ReelSlots&moduleID=18626&clientID=40300&gameName=luckyLeprechaunScratch&gameTitle=Lucky+Leprechaun+Scratch&LanguageCode=en&clientTypeID=40&casinoID=5007&lobbyName=IslandParadise&loginType=PlayerService&bankingURL=https%3A%2F%2Fmobilewebserver9-zensarmillqa.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FBanking&xmanEndPoints=https%3A%2F%2Fmobilewebserver9-zensarmillqa.installprogram.eu%2FXMan%2Fx.x&routerEndPoints=&disablePoweredBy=false&currencyFormat=&isPracticePlay=false&logoutURL=https%3A%2F%2Fmobilewebserver9-zensarmillqa.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FLogout&host=Desktop&apiCommsEnabled=false&launchToken=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL2FjY291bnQvdjEiLCJjaWQiOiI1MDA3IiwianRpIjoiLTI4NTYzMDQ0NzMyNzk5NjM4MzQiLCJzdWIiOiI1NGMzZWE3N2Q0NDA0ODMxYjdkOWE5YzgzZTQxMDc4OSIsImNlcnQiOiJNR1MuVXNlclNlc3Npb25Ub2tlbi5CbHVlbWVzYS4yMDE1MDMzMSIsImlhdCI6IjE1NzAxNzg0MzIiLCJhdWQiOiJKb2luVXNlclNlc3Npb24ifQ.XEaVhPvgCBoD2JzSopm23rXjQ0JwF35iqDt0ZEA1sNgh8oeelIJMbHhZu56aJzrnR0lmOTdU9Q3MLMtv2M-yJ9PIIXCjybrYjPT4IM8ezQHIuw8nJiUh5R0lTM5KPrIDmmTyHN6xCECNVL-XTfCVF1gH3b2emIrnWfCZS8_SZAo&gaClientId=1513979440.1565593852&activityStatementURL=undefined&username=player484&password=test";
				String obj = cfnlib.func_navigate(url);
				log.debug("opening webiste");

				if(obj!=null){
						language.detailsAppend("Open Browser and Enter Lobby URL in address bar and click Enter", "Island Paradise Lobby should open", "Island Paradise lobby is Open", "Pass");
				}
				else {
						language.detailsAppend("Open browser and Enter Lobby URL in address bar and click Enter", "Island Paradise lobby should Open", "Island paradise is not displayed", "Fail");
				}
				// Capture Screen shot for BaseScene after login
				
				String password = Constant.PASSWORD;
				String GameTitle=cfnlib.Func_LoginBaseScene(userName, password);
				if(GameTitle != null){
						language.detailsAppend("Verify that the application should display Game Logo and game name in Base Scene in Practice Play", "Game logo and game name with base scene should display", "Game logo and game name with base scene displays", "Pass");
				}
				else {
						language.detailsAppend("Verify that the application should Logo and game name in Base scene in Practice Play", "Game logo and game name with base scene should display", "Game logo and game name with base scene is not display", "Fail");
				}
				
				
				if(framework.equalsIgnoreCase("Force")){
					cfnlib.setNameSpace();
					}
				cfnlib.newFeature();
				Thread.sleep(1500);
				
				//Lets start with Auto play scenarios
				
				//cfnlib.verifyAutoplayConsoleOptions(language, classname, languageCode);
				
				//open the auto play panel and start spinning
				
				//cfnlib.verifyAutoplayPanelOptions(language, classname, languageCode);		
				
				/*boolean isAutoplayOnAfterRefresh = cfnlib.isAutoplayOnAfterRefresh();
				if(isAutoplayOnAfterRefresh){
					//language.details_append("Verify that is Autoplay On AfterRefresh", "Is Autoplay On AfterRefresh", "Autoplay On AfterRefresh", "Fail");
					//System.out.println("FAIL");
				}else{
					//language.details_append("Verify that is Autoplay On AfterRefresh", "Is Autoplay On AfterRefresh", "Autoplay is not On AfterRefresh", "Pass");
					//System.out.println("PASS");
				}
				
				//Hooks required for KWIKWIN
				boolean isAutoplayPauseOnFocusChange = cfnlib.isAutoplayPauseOnFocusChange();
				if(isAutoplayPauseOnFocusChange){
					//REport
					//language.details_append("Verify that is Autoplay Pause On Focus Change", "Is Autoplay Pause On Focus Change", "Autoplay Pause On Focus Change", "Pass");
				}else{
					//REport
					//language.details_append("Verify that isAutoplay Pause On Focus Change", "Autoplay Pause On Focus Change", "Autoplay not Pause On Focus Change", "Fail");
				}
				
				//Trigger freespins on autoplay and check the autoplay status		
				boolean isAutoplayStoppedOnMenuClick = cfnlib.isAutoplayStoppedOnMenuClick();
				if(isAutoplayStoppedOnMenuClick){
					//language.details_append("Verify that is Autoplay Stopped On MenuClick", "Is Autoplay Stopped On MenuClick", "Autoplay Stopped On MenuClick", "Pass");
					//System.out.println("PASS");
				}else{
					//language.details_append("Verify that is Autoplay Stopped On MenuClick", "Is Autoplay Stopped On MenuClick", "Autoplay has not Stopped On MenuClick", "Fail");
					//System.out.println("FAIL");
				}					
				//Now QuickSpin Scenarios				
				boolean isStopBtnAvailbleOnQuickSpin = cfnlib.isAutoplayStoppedOnMenuClick();
				if(isAutoplayPauseOnFocusChange){
					//language.details_append("Verify that is StopBtn Availble On QuickSpin ", "Is StopBtn Availble On QuickSpin", "StopBtn Availble On QuickSpin", "Pass");					
					//REport
				}else{
					//language.details_append("Verify that is StopBtn Availble On QuickSpin", "Is StopBtn Availble On QuickSpin", "StopBtn Availble On QuickSpin", "Fail");
					//REport
				}*/
				
				/*String currentUrl = webdriver.getCurrentUrl();*/
				
				
				//cfnlib.verifyALLBetValues(language, classname, languageCode);
	
				/*//Lets Start Bet scenarios
				
				String currentBet;
				try {
					cfnlib.openBetPanel();
					Thread.sleep(1000);
					
					currentBet = cfnlib.setMaxBet();
					Thread.sleep(2000);
					cfnlib.close_TotalBet();
					
					String betValue =currentBet;
					
					do{
					
					currentBet = betValue;
					boolean isBetChangedIntheConsole = cfnlib.isBetChangedIntheConsole(currentBet);	
					
					for(int spinCounter = 0; spinCounter< 10; spinCounter++){
					String currentCreditAmount= cfnlib.getCurrentCredits();
					//Spin Here
					cfnlib.spinclick();
					
					boolean isCreditDeducted = cfnlib.isCreditDeducted(currentCreditAmount, currentBet);
							
					//lets wait till the win comes
					cfnlib.waitForSpinButtonstop();
					
					boolean isPlayerWon = cfnlib.isPlayerWon();
					if(isPlayerWon){
						boolean isWinAddedToCredit = cfnlib.isWinAddedToCredit(currentCreditAmount, currentBet);
						if(isWinAddedToCredit){
							language.details_append("Verify that is Win Added To Credit", "Is Win Added To Credit","Win Added To Credit", "Pass");					
						}else{
							language.details_append("Verify that is Win Added To Credit", "is Win Added To Credit","Win not Added To Credit", "Fail");	
						}
						break;
					}
					}
					cfnlib.openBetPanel();
					Thread.sleep(1000);
					betValue = cfnlib.setTheNextLowBet();
					Thread.sleep(2000);
					cfnlib.close_TotalBet();

					}while(!betValue.equalsIgnoreCase(currentBet));
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				//Lets check the Quick bets
				//cfnlib.verifyAllQuickBets();
				//Change Bet and refresh with out spin and verify previous bet value present or not
				/* boolean isBetChangedOnRefresh = cfnlib.isBetChangedOnRefresh();
				
				 if(isBetChangedOnRefresh){
						language.details_append("Verify that is Bet Changed On Refresh", "Is Bet Changed On Refresh","Bet Changed On Refresh", "Fail");	
					 System.out.println("isBetChangedOnRefresh :: FAIL");
					}else{
						language.details_append("Verify that is is Bet Changed On Refresh", "Is Bet Changed On Refresh","Bet not Changed On Refresh", "Pass");	
						 System.out.println("isBetChangedOnRefresh :: PASS");
					}*/	
				//=======================================================================================================
				
				/*//To validate Mini Paytable for all bets
				String curntBet;
				try {
					cfnlib.openBetPanel();
					Thread.sleep(1000);

					curntBet = cfnlib.setMaxBet();
					Thread.sleep(2000);
					cfnlib.close_TotalBet();

					String betValue = curntBet;

					do {

						curntBet = betValue;
						
						cfnlib.validateMiniPaytable(curntBet);
						
						cfnlib.openBetPanel();
						Thread.sleep(1000);

						betValue = cfnlib.setTheNextLowBet();
						Thread.sleep(2000);
						cfnlib.close_TotalBet();

					} while (!betValue.equalsIgnoreCase(curntBet));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
						 
			/*		//verify Menu to be open
					boolean menuOpen = cfnlib.menuOpen();
						if(menuOpen){
							 language.details_append_folder("Verify that Navigation of menu link is " + languageDescription + " ", "Language of Menu Links should be " +languageDescription+ "", "Menu Links are displaying in " +languageDescription+ " language", "pass", languageCode);
						}
						else {
							 language.details_append_folder("Verify that Navigation" + languageDescription + "should display properly on menu links", "Language inside Menu Links should be " +languageDescription+ " ", "Menu Links are not displaying", "fail", languageCode);
						}
					//verify navigating to banking page.
					cfnlib.CheckNavigateGameToBanking(language, classname, languageCode);
					
					cfnlib.newFeature();
					
					//navigation from game to setting page
					cfnlib.verifysettingsOptions(language, classname, languageCode);	
					cfnlib.settingsBack();
					
					//navigation from game to paytable
					cfnlib.verifypaytablenavigation(language, classname, languageCode);
					
	
					//verify help page navigation from game
					boolean help = cfnlib.verifyhelpenavigation(language, classname, languageCode);
					if(help){
						 language.details_append_folder("Verify that Navigation on Help screen is " + languageDescription + " ", "Language in Help Screen should be " +languageDescription+ " ", "Language inside Help screens is " +languageDescription+ " ", "pass", languageCode);
					}
					else {
						 language.details_append_folder("Verify that Navigation on Help is " + languageDescription +"should be display properly on Help", "Language inside Help should be in " +languageDescription+ " language", "Language inside Help Links is displaying", "fail", languageCode);
					}
					//verify navigation to responsible gaming from game menu
					boolean respgame = cfnlib.verifyresponsiblegamingenavigation(language, classname, languageCode);
					if(respgame){
						 language.details_append_folder("Verify that Navigation on ResponsibleGaming screen is " + languageDescription + " ", "Language in ResponsibleGaming Screen should be " +languageDescription+ " ", "Language inside ResponsibleGaming screens is " +languageDescription+ " ", "pass", languageCode);
					}
					else {
						 language.details_append_folder("Verify that Navigation on ResponsibleGaming is " + languageDescription +"should be display properly on ResponsibleGaming", "Language inside Help should be in " +languageDescription+ " language", "Language inside ResponsibleGaming Links is displaying", "fail", languageCode);
					}
					
					//verify navigation to playcheck gaming from game menu
					boolean playcheck = cfnlib.verifyplaychecknavigation(language, classname, languageCode);
					if(playcheck){
						System.out.println("Navigated to playcheck page succesfully");
					}
					else {
						 language.details_append_folder("Verify that Navigation on playcheck is " + languageDescription +"should be display properly on playcheck", "Language inside Help should be in " +languageDescription+ " language", "Language inside playcheck Links is displaying", "fail", languageCode);
					}
		
					//verify navigation to cashCheckButton gaming from game menu
					boolean cashCheckButton = cfnlib.verifycashChecknavigation(language, classname, languageCode);
					if(cashCheckButton){
						System.out.println("Navigated to Cashcheck page succesfully");
					}
					else {
						 language.details_append_folder("Verify that Navigation on cashCheck is " + languageDescription +"should be display properly on cashCheck", "Language inside Help should be in " +languageDescription+ " language", "Language inside cashCheck Links is displaying", "fail", languageCode);
					}
					////verify navigation to loyaltyButton gaming from game menu
					boolean loyaltyButton = cfnlib.verifyloyaltynavigation(language, classname, languageCode);
					if(loyaltyButton){
						System.out.println("Navigated to Loyalty page succesfully");
					}
					else {
						 language.details_append_folder("Verify that Navigation on loyalty is " + languageDescription +"should be display properly on loyalty", "Language inside Help should be in " +languageDescription+ " language", "Language inside loyalty Links is displaying", "fail", languageCode);
					}
					
				//verify navigation to lobby gaming from game menu
				boolean lobbyButton = cfnlib.verifylobbynavigation();
			     if(lobbyButton){
				language.details_append_folder("Verify that Navigation on lobby screen is " + languageDescription + " ", "Language in lobby Screen should be " +languageDescription+ " ", "Language inside lobby screens is " +languageDescription+ " ", "pass", languageCode);
				}
				else {
				language.details_append_folder("Verify that Navigation on lobby is " + languageDescription +"should be display properly on lobby", "Language inside Help should be in " +languageDescription+ " language", "Language inside lobby Links is displaying", "fail", languageCode);
				}*/
				 
				 //Menu code add over here
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
