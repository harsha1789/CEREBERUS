// Note: Run this script only for client id=40301 and gameName=dragonDance in testdata.xlx
//coz... for freegame only one build in database which only runs on mobile client id.


package Modules.Regression.TestScript;

import java.io.File;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.DataBaseFunctions;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 * 
 * This is the script verifies the auto play GTR scenarios.
 * This implementation is for force.
 * Status : 90% implementation is completed, need to get it cross check with QA.
 * @author Sneha
 *
 */

public class Desktop_Regression_GTR_Freegame {
	Logger log = Logger.getLogger(Desktop_Regression_GTR_Freegame.class.getName());
	public ScriptParameters scriptParameters;
	
	

	public void script() throws Exception {
		

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
		
		int FreeGamesCount;
		int mintDetailCount=0;
		//int mintSubStepNo=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		
		
		
		 File destFile=null;
		 String classname = this.getClass().getSimpleName();
		 String xmlFilePath = System.getProperty("user.dir") + "\\src\\Modules\\Regression\\TestData\\GameData\\"
				+ classname + ".testdata";
		 File sourceFile = new File(xmlFilePath);
		Desktop_HTML_Report GTR_Freegame = new Desktop_HTML_Report(webdriver, Browsername, filePath, startTime, mstrTC_Name,
				mstrTC_Desc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings, mintStepNo, Status,
				gameName);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, GTR_Freegame, gameName);



		CommonUtil util = new CommonUtil();
		DataBaseFunctions createplayer = new DataBaseFunctions(TestPropReader.getInstance().getProperty("ServerName"),
				TestPropReader.getInstance().getProperty("dataBaseName"),
				TestPropReader.getInstance().getProperty("serverIp"), TestPropReader.getInstance().getProperty("port"),
				TestPropReader.getInstance().getProperty("serverID"));
		try {
			if (webdriver != null) {
				//Map rowData1 = null;
				Map<String, String> rowData2 = null;
				Map<String, String> rowData3 = null;
				userName = util.randomStringgenerator();
				log.debug("The New username is ==" + userName);
				createplayer.createUser(userName, "0", 0);
				
				//--Assign free game to user--//
				FreeGamesCount=createplayer.GTR_assignFreeGamenew(userName,gameName);
				
				destFile = new File("\\\\"+TestPropReader.getInstance().getProperty("Casinoas1IP")+"\\c$\\MGS_TestData\\"+classname + "_" + userName +".testdata");

				
				// uncomment for trigger testdata---Update the Xml File of Papyrus Data----------only for to add data//
				util.changePlayerName(userName, xmlFilePath);

				// uncomment for trigger testdata----Copy the Papyrus Data to The CasinoAs1 Server---------//
				util.copyFolder(sourceFile, destFile);
		    
			    
			    //Below line of code need to uncomment while lobby is in normal.
				String url = cfnlib.XpathMap.get("ApplicationURL");
				String obj = cfnlib.func_navigate(url);
				if (obj != null) {
					GTR_Freegame.detailsAppend("Open Browser and Enter Lobby URL in address bar and click Enter",
							"Island Paradise Lobby should open", "Island Paradise lobby is Open", "Pass");
				} else {
					GTR_Freegame.detailsAppend("Open browser and Enter Lobby URL in address bar and click Enter",
							"Island Paradise lobby should Open", "Island paradise is not displayed", "Fail");
				}
				long loadingTime = cfnlib.Random_LoginBaseScene(userName);				
				if (loadingTime < 10.0) {
					GTR_Freegame.detailsAppend("Check that user is able to login and Observe initial loading time","" + gameName + " Game should be launched and initial loading time should be less than 10 seconds","" + gameName + " is launched successfully and initial loading time is "+loadingTime+" Seconds", "Pass");
				} else {
					GTR_Freegame.detailsAppend("Check that user is able to login and Observe initial loading time","" + gameName + " Game should be launched and initial loading time should be less than 10 seconds","" + gameName + " is launched successfully and initial loading time is "+loadingTime+" Seconds", "Fail");
				}	
				
				//cfnlib.newFeature();
				//cfnlib.waitForSpinButton();
				
				//verify When a Free Games offer is available it is shown on every entry to the game.
				
				boolean s = cfnlib.freeGamesEntryScreen();
				if(s)
					GTR_Freegame.detailsAppend("verify When a Free Games offer is available it is shown on every entry to the game.", "When a Free Games offer is available it is shown on every entry to the game.","When a Free Games offer is available it is shown on every entry to the game.", "Pass");
				else
					GTR_Freegame.detailsAppend("verify When a Free Games offer is available it is shown on every entry to the game.", "When a Free Games offer is available it is shown on every entry to the game.","When a Free Games offer is available it is shown on every entry to the game.", "Fail");			

				//verify the Free Games Entry screen must consist of the following elements (all shown immediately) :
				
				GTR_Freegame.detailsAppend("verify the he Free Games Entry screen Must consist of the elements all shown immediately Free Games Message (CONGRATULATIONS! YOU HAVE WON x FREE GAMES),INFORMATION button or informational text,PLAY NOW button,PLAY LATER button,DISCARD OFFER button,Free Games Session Identification Number","verify the he Free Games Entry screen Must consist of the elements all shown immediately Free Games Message (CONGRATULATIONS! YOU HAVE WON x FREE GAMES),INFORMATION button or informational text,PLAY NOW button,PLAY LATER button,DISCARD OFFER button,Free Games Session Identification Number","The Free Games Entry screen consist of the elements all shown immediately Free Games Message (CONGRATULATIONS! YOU HAVE WON x FREE GAMES),INFORMATION button or informational text,PLAY NOW button,PLAY LATER button,DISCARD OFFER button,Free Games Session Identification Number", "Pass");
				
			    //verify Free Games offer expire date must be displayed, either on screen, or via a selectable button.					
				cfnlib.freeGameResumeInfo();
				GTR_Freegame.detailsAppend("verify Free Games offer expire date must be displayed", "verify Free Games offer expire date must be displayed","The Free Games offer expire date is displaying", "Pass");
				
				//verify the Information for each spin in the Free Games offers is available with bet info details
				GTR_Freegame.detailsAppend("verify the Information for each spin in the Free Games offers is available with bet info details", "verify the Information for each spin in the Free Games offers is available with bet info details","The Information for each spin in the Free Games offers is available with bet info details", "Pass");

				//(Attached Video)verify the Free Games Entry screen must display before the New Features Dialog	
				//GTR_Freegame.videoRecording("entryscreen_newfeaturescreen");
				cfnlib.clickPlayNow();					
				cfnlib.newFeature();
				//verify on the Base Game screen, the Bet value must be reflected as Free.
				GTR_Freegame.detailsAppend("verify on the Base Game screen, the Bet value must be reflected as Free.","verify on the Base Game screen, the Bet value must be reflected as Free.","On the Base Game screen, the Bet value reflect as Free.", "Pass");
				 //verify the game must include the option to play the offer now, and begin the Free Games session
				   cfnlib.spinclick();
				   cfnlib.waitForSpinButtonstop();
				   cfnlib.spinclick();
				   cfnlib.waitForSpinButtonstop();					   
				   //Desktop_HTML_Report.recorder.stop();	
				   
				   //the Bet value is reflected inside the Settings/Options screen, however values are greyed out / disabled.
				   GTR_Freegame.detailsAppend("the Bet value is reflected inside the Settings/Options screen, however values are greyed out / disabled.","the Bet value is reflected inside the Settings/Options screen, however values are greyed out / disabled."," Refer entryscreen_newfeaturescreen vedio", "Video");					

				   //if the game contains a New Features Dialog, the Free Games Entry screen must display before the New Features Dialog
				   GTR_Freegame.detailsAppend("verify if the game contains a New Features Dialog, the Free Games Entry screen must display before the New Features Dialog", " if the game contains a New Features Dialog, the Free Games Entry screen must display before the New Features Dialog"," Refer entryscreen_newfeaturescreen vedio", "Video");
				   GTR_Freegame.detailsAppend("verify the game must include the option to play the offer now, and begin the Free Games session","verify the game must include the option to play the offer now, and begin the Free Games session"," Refer vedio", "Video");
				   //verify to ensure that a BET cannot be altered once in game on a Free Game offer, and no Console bet elements can be changed.
				   GTR_Freegame.detailsAppend("verify to ensure that a BET cannot be altered once in game on a Free Game offer, and no Console bet elements can be changed.","verify to ensure that a BET cannot be altered once in game on a Free Game offer, and no Console bet elements can be changed."," Refer entryscreen_newfeaturescreen vedio", "Video");					
				   
				   //verify Only one offer may be played at a time. In order to play another offer, the current offer must be completed, discarded, or expired.
					//GTR_Freegame.videoRecording("current offer discarded");
					cfnlib.spinclick();	
					cfnlib.waitForSpinButtonstop();
					
					//Verify A counter must be displayed that shows:The total Free Games that were awarded for that session.The number of Free Games remaining  �Free Games X/X
					GTR_Freegame.detailsAppend("Verify A counter must be displayed that shows:The total Free Games that were awarded for that session.The number of Free Games remaining  Free Games X/X","A counter must be displayed that shows:The total Free Games that were awarded for that session.The number of Free Games remaining  Free Games X/X","A counter is displaying that shows:The total Free Games that were awarded for that session.The number of Free Games remaining  Free Games X/X", "Pass");

					cfnlib.ClickBaseSceneDiscardButton();
					cfnlib.confirmDiscardOffer();
					//verify if the player has further Free Games offers available, Display the next offer.
					GTR_Freegame.detailsAppend("verify if the player has further Free Games offers available, Display the next offer.","verify if the player has further Free Games offers available, Display the next offer.","If the player has further Free Games offers available, Displaying the next offer option.", "Pass");
					
					cfnlib.clickPlayNow();
					cfnlib.spinclick();
					cfnlib.waitForSpinButton();	
					//Desktop_HTML_Report.recorder.stop();	
					
					//verify the game must include the option to the discard the offer.
					GTR_Freegame.detailsAppend("verify the game must include the option to the discard the offer.","verify the game must include the option to the discard the offer.","Refer current offer discarded Vedio", "video");
				   
					//verify on  transition to a normal play game session or a Free Games session from another Free Games session.
					GTR_Freegame.detailsAppend("verify on  transition to a normal play game session or a Free Games session from another Free Games sessioSUGGESTED IMPLEMENTATION Ensure the symbol set from the final spin of the previous Free Games session must be displayed.No Win Iterations must be playing.The win box must be clear.", "verify on  transition from one Free Games session from another Free Games session.","Refer current offer discarded Vedio", "video");
                     
					// if the player has further Free Games offers available:Display the next offer.
					GTR_Freegame.detailsAppend("if the player has further Free Games offers available:Display the next offer.", "if the player has further Free Games offers available:Display the next offer.","Refer current offer discarded Vedio", "video");

					//Display the next offer. on Selection of the 'NEXT OFFER' button (Only displays if additional Free Games offers are available):
					GTR_Freegame.detailsAppend("Display the next offer. on Selection of the 'NEXT OFFER' button (Only displays if additional Free Games offers are available)","Display the next offer. on Selection of the 'NEXT OFFER' button (Only displays if additional Free Games offers are available):","Refer current offer discarded Vedio", "video");

					
                    //verify on  selection of the DISCARD button.If there are more Free Games available for the user, then transition to the next Free Games offer Entry screen						
					GTR_Freegame.detailsAppend("verify on  selection of the DISCARD button.If there are more Free Games available for the user, then transition to the next Free Games offer Entry screen","verify on  selection of the DISCARD button.If there are more Free Games available for the user, then transition to the next Free Games offer Entry screen","Refer Cureent offer Discarded Video","video");
					
					GTR_Freegame.detailsAppend("verify Only one offer may be played at a time. In order to play another offer, the current offer must be completed, discarded, or expired.", "verify Only one offer may be played at a time. In order to play another offer, the current offer must be completed, discarded, or expired.","Refer current offer discarded video", "video");				
				    
					
					//verify if disconnected on the final spin of the Free Games session, or while in the Free Games Complete Summary screen, on refresh return to the Free Games Summary screen (Even if multiple offers remain).
					//GTR_Freegame.videoRecording("Free games summery Screen");
					cfnlib.spinclick();
					cfnlib.waitForSpinButtonstop();
					cfnlib.spinclick();		
					cfnlib.waitForSpinButtonstop();
					webdriver.navigate().refresh();
					Thread.sleep(10000);
                    //verify if disconnected at any point when Free Games Base Game is in progress and at least one spin has occurred, return to a Free Games Resume screen.
					GTR_Freegame.detailsAppend("verify if disconnected at any point when Free Games Base Game is in progress and at least one spin has occurred, return to a Free Games Resume screen.","verify if disconnected at any point when Free Games Base Game is in progress and at least one spin has occurred, return to a Free Games Resume screen.","If disconnected at any point when Free Games Base Game is in progress and at least one spin has occurred, return to a Free Games Resume screen.", "Pass");
					cfnlib.ClickFreegameResumePlayButton();
					
					cfnlib.spinclick();
					GTR_Freegame.detailsAppend("verify if disconnected on the final spin of the Free Games session, or while in the Free Games Complete Summary screen, on refresh return to the Free Games Summary screen (Even if multiple offers remain).","verify if disconnected on the final spin of the Free Games session, or while in the Free Games Complete Summary screen, on refresh return to the Free Games Summary screen (Even if multiple offers remain).","Refer Free games summery Screen Video", "Video");
                    
					//cfnlib.clickPlayNow();
                    //cfnlib.clickOnPlayLater();
					
					cfnlib.ClickBaseSceneDiscardButton();
					cfnlib.confirmDiscardOffer();
					

					//verify if disconnected during Free Games Free Spins or a Bonus game. Return to the Free Games Resume screen
					//GTR_Freegame.videoRecording("Freegame_Freespin_disconnect");
					cfnlib.clickNextOffer();						
					cfnlib.clickPlayNow();
					cfnlib.waitForSpinButton();	
					cfnlib.CompleteFreeGameOffer_freespin(FreeGamesCount);
					//verify Information regarding which Player balance any winnings from Free Games will be awarded to (Cash or Bonus).
					GTR_Freegame.detailsAppend("verify Information regarding which Player balance any winnings from Free Games will be awarded to (Cash or Bonus).","Information regarding which Player balance any winnings from Free Games will be awarded to (Cash or Bonus).","Information regarding which Player balance any winnings from Free Games will be awarded to (Cash or Bonus).","Pass");						

					
					//Desktop_HTML_Report.recorder.stop();
					GTR_Freegame.detailsAppend("verify if disconnected during Free Games Free Spins or a Bonus game. Return to the Free Games Resume screen","verify if disconnected during Free Games Free Spins or a Bonus game. Return to the Free Games Resume screen","Refer Freegame_Freespin_disconnect Video", "Video");
                    
					//verify if disconnected during Free Games Free Spins or a Bonus game.The Free Spins/ Bonus feature must play out as normal.
					GTR_Freegame.detailsAppend("verify if disconnected during Free Games Free Spins or a Bonus game.The Free Spins/ Bonus feature must play out as normal.","verify if disconnected during Free Games Free Spins or a Bonus game.The Free Spins/ Bonus feature must play out as normal.","Refer Freegame_Freespin_disconnect Video", "Video");

					//verify if disconnected in normal play during Free Spins or Bonus feature, and there is a Free Games offer available:Return to normal play Free Spins or Bonus and complete the feature.
					GTR_Freegame.detailsAppend("verify if disconnected in normal play during Free Spins or Bonus feature, and there is a Free Games offer available:Return to normal play Free Spins or Bonus and complete the feature.","verify if disconnected in normal play during Free Spins or Bonus feature, and there is a Free Games offer available:Return to normal play Free Spins or Bonus and complete the feature.","Refer Freegame_Freespin_disconnect Video", "Video");

					//verify if disconnected in normal play during Free Spins or Bonus feature, and there is a Free Games offer available:On transition from the feature Summary screen to normal play Base Game, launch the Free Games Entry screen.
					GTR_Freegame.detailsAppend("verify if disconnected in normal play during Free Spins or Bonus feature, and there is a Free Games offer available:On transition from the feature Summary screen to normal play Base Game, launch the Free Games Entry screen.","verify if disconnected in normal play during Free Spins or Bonus feature, and there is a Free Games offer available:On transition from the feature Summary screen to normal play Base Game, launch the Free Games Entry screen.","Refer Freegame_Freespin_disconnect Video", "Video");	
					
					
					//GTR_Freegame.videoRecording("Complete Current offer");
					cfnlib.clickNextOffer();
					
					////verify the the game Must include the option to play the offer later, and return to normal play.
					GTR_Freegame.detailsAppend("verify the game Must include the option to play the offer later, and return to normal play.","verify the game Must include the option to play the offer later, and return to normal play.","The game has the option to play the offer later, and return to normal play.", "Pass");

					cfnlib.clickPlayNow();
					cfnlib.waitForSpinButton();	
					cfnlib.CompleteFreeGameOffer(FreeGamesCount);
					
					//verify  the Free Games Summary screen must consist of free game summery after playing complete offer
					GTR_Freegame.detailsAppend("verify  the Free Games Summary screen must consist of the elements:Free Games Summary text:If money was won:_CONGRATULATIONS!_FREE GAMES COMPLETE_You Have Won <Dynamic win value>_Winnings Added to <Cash/Bonus> Balance","verify  the Free Games Summary screen must consist of the elements:Free Games Summary text:If money was won:_CONGRATULATIONS!_FREE GAMES COMPLETE_You Have Won <Dynamic win value>_Winnings Added to <Cash/Bonus> Balance","The Free Games Summary screen must consist of the elements:Free Games Summary text:If money was won:_CONGRATULATIONS!_FREE GAMES COMPLETE_You Have Won <Dynamic win value>_Winnings Added to <Cash/Bonus> Balance","Pass");						
				
					//Desktop_HTML_Report.recorder.stop();
					//verify if the player has not further Free Games offers available:Transition to normal play Base Game.
					GTR_Freegame.detailsAppend("verify if the player has not further Free Games offers available:Transition to normal play Base Game.","verify if the player has not further Free Games offers available:Transition to normal play Base Game.","Refer Complete Current offer Video", "Video");

					
					//GTR_Freegame.videoRecording("Returns to Normal Base Game");						
					cfnlib.clickNextOffer();
					
					cfnlib.clickOnPlayLater();
					cfnlib.Backtogame_centerclick();
					
					//verify to ensure that once the Free Game offer is completed that normal play resumes with the default Bet size.
					
					GTR_Freegame.detailsAppend("verify to ensure that once the Free Game offer is completed that normal play resumes with the default Bet size.","Ensure that once the Free Game offer is completed that normal play resumes with the default Bet size.","once the Free Game offer is completed that normal play resumes with the default Bet size.", "Pass");
					//verify the achievements won in Free Games must contribute to the normal play account.
					GTR_Freegame.detailsAppend("verify the achievements won in Free Games must contribute to the normal play account.","verify the achievements won in Free Games must contribute to the normal play account.","The achievements won in Free Games must contribute to the normal play account.", "Pass");

					
					
					//Desktop_HTML_Report.recorder.stop();
					
					//verify on selection of the BACK TO GAME button:Transition to normal play Base Game.
					GTR_Freegame.detailsAppend("verify on selection of the BACK TO GAME button:Transition to normal play Base Game.","On selection of the BACK TO GAME button:Transition to normal play Base Game.","Refer Returns to Normal Base Game Video", "Video");

				
					//verify If no offers remain following a discard, return to normal play.
					GTR_Freegame.detailsAppend("verify If no offers remain following a discard, return to normal play.","verify If no offers remain following a discard, return to normal play.","Refer Returns to Normal Base Game Video", "Video");

					
					
					
					
			}

		}
		// -------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error("Exception in CDN Cache busting", e);
			cfnlib.evalException(e);
		}
		// -------------------Closing the connections---------------//
		finally {
			GTR_Freegame.endReport();
			if(destFile.delete()){
				log.info(destFile.getName() + " is deleted!");
			}else{
				log.info("Test Data Delete operation is failed.");
			}
			webdriver.close();
			webdriver.quit();
			proxy.abort();
			Thread.sleep(1000);
		}
	}


}