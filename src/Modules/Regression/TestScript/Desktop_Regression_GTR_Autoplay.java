package Modules.Regression.TestScript;

import java.io.File;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;


/**
 * 
 * This is the script verifies the auto play GTR scenarios.
 * This implementation is for force.
 * 
 * @author Snehal
 *
 */
public class Desktop_Regression_GTR_Autoplay {
	Logger log = Logger.getLogger(Desktop_Regression_GTR_Autoplay.class.getName());
	public ScriptParameters scriptParameters;
	
	
	//public ATUTestRecorder recorder;
	@SuppressWarnings("unused")
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
		String languageDescription=null;
		String languageCode=null;
		String Status=null;
		int mintDetailCount=0;
		//int mintSubStepNo=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		
		 File destFile;
		 String classname = this.getClass().getSimpleName();
		 String xmlFilePath = System.getProperty("user.dir") + "\\src\\Modules\\Regression\\TestData\\GameData\\"
				+ classname + ".testdata";
		 File sourceFile = new File(xmlFilePath);
		Desktop_HTML_Report gtr_autoplay = new Desktop_HTML_Report(webdriver,Browsername,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,gameName);
		String GameTitle=null;

		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, gtr_autoplay, gameName);


		/*	GlobalcfnLibrary gcfnlib = new GlobalcfnLibrary();
		DataBaseFunctions createplayer = new DataBaseFunctions(TestPropReader.getInstance().getProperty("ServerName"),
				TestPropReader.getInstance().getProperty("dataBaseName"),
				TestPropReader.getInstance().getProperty("serverIp"), TestPropReader.getInstance().getProperty("port"),
				TestPropReader.getInstance().getProperty("serverID"));
		 */

		try{
			// Step 1 
			if(webdriver!=null)
			{		
				Map<String, String> rowData2 = null;
				Map<String, String> rowData3 = null;

				//need to uncomment if using random player , and test data
				/*
				userName = gcfnlib.randomStringgenerator();
				log.debug("The New username is ==" + userName);
				createplayer.createUser(userName, "0", 0);

				destFile = new File("\\\\"+TestPropReader.getInstance().getProperty("Casinoas1IP")+"\\c$\\MGS_TestData\\"+classname + "_" + userName +".testdata");

				// ---Update the Xml File of Papyrus Data----------//
				gcfnlib.changePlayerName(userName, xmlFilePath);

				// ----Copy the Papyrus Data to The CasinoAs1 Server---------//
				gcfnlib.copyFolder(sourceFile, destFile);

				 */

				String url = cfnlib.XpathMap.get("ApplicationURL");
				String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
				log.info("url = " +LaunchURl);
				cfnlib.loadGame(LaunchURl);


				//String obj = cfnlib.func_navigate(url);

				//log.debug("opening website");
				/*if(obj!=null){
					gtr_autoplay.details_append("Open Browser and Enter Lobby URL in address bar and click Enter", "Island Paradise Lobby should open", "Island Paradise lobby is Open", "Pass");
				}
				else {
					gtr_autoplay.details_append("Open browser and Enter Lobby URL in address bar and click Enter", "Island Paradise lobby should Open", "Island paradise is not displayed", "Fail");
				}*/
				// Capture Screen shot for BaseScene after login

				//String password = constant.Password;

				//Need to uncomment if using random player

				/*String GameTitle=cfnlib.Func_LoginBaseScene(userName, password);

				if(GameTitle != null){
					gtr_autoplay.details_append("Verify that the application should display Game Logo and game name in Base Scene in Practice Play", "Game logo and game name with base scene should display", "Game logo and game name with base scene displays", "Pass");
				}
				else {
					gtr_autoplay.details_append("Verify that the application should Logo and game name in Base scene in Practice Play", "Game logo and game name with base scene should display", "Game logo and game name with base scene is not display", "Fail");
				}*/


				if(framework.equalsIgnoreCase("Force")){
					cfnlib.setNameSpace();
				}
				
				cfnlib.newFeature();
				cfnlib.waitForSpinButton();
				//Verify the autoplay button availability
				boolean isautoplay=cfnlib.ISAutoplayAvailable();
				if(isautoplay)
				{	
					gtr_autoplay.detailsAppend("Verify that AutoPlay�must be available in Base Scene", "AutoPlay�must be available in BaseScene", "AutoPlay�is available in Base Scene", "pass");
				}
				else
				{
					gtr_autoplay.detailsAppend("verify that AutoPlay�must be available in Base Scene", "AutoPlay�must be available in Base Scene", "AutoPlay�is not available in BaseScene", "fail");
				}	

				cfnlib.autoplayPresets( gtr_autoplay);


				cfnlib.autoPlay_with_QS_On( gtr_autoplay);



				long reeltime=cfnlib.Reelspin_speed_During_Autoplay();

				if(reeltime<=3)
				{
					gtr_autoplay.detailsAppend("The 3 second spin rule timing must be adhered to.","The 3 second spin rule timing must be adhered to.", "The 3 second spin rule timing is achive.", "Pass");  

				}
				else
					gtr_autoplay.detailsAppend("The 3 second spin rule timing must be adhered to.","The 3 second spin rule timing must be adhered to.", "The 3 second spin rule timing is not achive, taking more time", "Fail");  


				boolean spinvalue=cfnlib.autoplay_spin_selection();

				if(spinvalue)
					gtr_autoplay.detailsAppend("verify that The player must be able to select the number of spins to play.","The player must be able to select the number of spins to play.", "The player  able to select the number of spins to play.", "Pass");  
				else
					gtr_autoplay.detailsAppend("verify that The player must be able to select the number of spins to play.","The player must be able to select the number of spins to play.", "The player not  able to select the number of spins to play.", "Fail");  


				boolean spin_session=cfnlib.is_autoplay_session_end();

				if(spin_session)
				{	
					gtr_autoplay.detailsAppend(" Verify that Once the selected number of spins has played out, the AutoPlay session must immediately end"," that Once the selected number of spins has played out, the AutoPlay session must immediately end","Autopaly has immediately stop.", "Pass");
				}
				else
				{
					gtr_autoplay.detailsAppend("verify  that Once the selected number of spins has played out, the AutoPlay session must immediately end"," that Once the selected number of spins has played out, the AutoPlay session must immediately end", "Autoplay session has not immediatly stop.","Fail");	
				}	

				boolean max_Spin=cfnlib.max_spin_chk();
				if(max_Spin)
				{	
					gtr_autoplay.detailsAppend(" verify that The maximum number of spins allowed must be 100.","  The maximum number of spins allowed must be 100.","maximunm no of spins allowed are 100.", "Pass");
				}
				else
				{
					gtr_autoplay.detailsAppend("verify that The maximum number of spins allowed must be 100.","  The maximum number of spins allowed must be 100.", "maximunm no of spins allowed are 100.","Fail");	
				}

				/*boolean isautoplaywin = cfnlib.is_autoplay_window();
				if(isautoplaywin)
				{	
				gtr_autoplay.details_append(" verify that On selection of AutoPlay, the player must always be taken to an AutoPlay settings screen/dialog/window etc."," On selection of AutoPlay, the player must always be taken to an AutoPlay settings screen/dialog/window etc.","Player is on Autoplay setting/window/dialog .", "pass");
				}
				else
				{
					gtr_autoplay.details_append("verify that On selection of AutoPlay, the player must always be taken to an AutoPlay settings screen/dialog/window etc."," On selection of AutoPlay, the player is not taken to an AutoPlay settings screen/dialog/window etc.", "Player is not on Autoplay setting/window/dialog .","fail");	
			    }*/

				boolean onrefresh = cfnlib.isAutoplayOnAfterRefresh();

				if(onrefresh)
				{	
					gtr_autoplay.detailsAppend(" verify On refresh, the previous AutoPlay session must not resume.","  On refresh, the previous AutoPlay session must not resume.","The previous AutoPlay session has not resume", "Pass");
				}
				else
				{
					gtr_autoplay.detailsAppend("verify On refresh, the previous AutoPlay session must not resume."," On refresh, the previous AutoPlay session has resume.", "The previous AutoPlay session has resume","Fail");	
				}


				boolean focus = cfnlib.Autoplay_focus_removed();
				if(focus)
				{
					gtr_autoplay.detailsAppend(" verify autoPlay must not stop when the game content is no longer visible (focus being removed).","  AutoPlay must not stop when the game content is no longer visible (focus being removed).","Autoplay has not stoped when game content is not visible", "Pass");
				}
				else
					gtr_autoplay.detailsAppend(" verify autoPlay must not stop when the game content is no longer visible (focus being removed).","  AutoPlay must not stop when the game content is no longer visible (focus being removed).","Autoplay has  stoped when game content is not visible", "Fail");


				// GTR Free Spin
				cfnlib.waitForSpinButton();
				String currentUrl = webdriver.getCurrentUrl();
				String url_new = currentUrl.replaceAll("username="+userName, "username="+cfnlib.XpathMap.get("FreeSpinUserName"));
				cfnlib.loadGame(url_new);

				boolean autoplay=cfnlib.is_autoplay_with_freespin(gtr_autoplay);
				/*
				//log out the current user and creating new user in UK Regulatory market

				String gameTitle=cfnlib.Func_Login_RegulatoryMarket_UK(gcfnlib, password);
				log.debug("Game Name="+gameTitle);
				cfnlib.newFeature();

				 */

				//Loding game in UK regulatory market to check autoplay scenarioes
				gtr_autoplay.detailsAppend("GTR_Autoplay UK market scenarioes", "UK market scenarioes", "", "");
				String uk_url = cfnlib.XpathMap.get("UK_url");
				cfnlib.loadGame(uk_url);
				cfnlib.waitForSpinButton();
				boolean isautoplaypresent=cfnlib.ISAutoplayAvailable();
				if(isautoplaypresent)
				{	
					gtr_autoplay.detailsAppend("verify that AutoPlay may include a list of AutoPlay presets (quick select spin options).", " AutoPlay may include a list of AutoPlay presets (quick select spin options).", "that AutoPlay may include a list of AutoPlay presets (quick select spin options).", "pass");
				}
				else
				{
					gtr_autoplay.detailsAppend("verify that AutoPlay may include a list of AutoPlay presets (quick select spin options).", "AutoPlay may include a list of AutoPlay presets (quick select spin options).", "that AutoPlay may include a list of AutoPlay presets (quick select spin options).", "fail");
				}	



				cfnlib.is_autoplay_setting(gtr_autoplay);



				boolean betselection=cfnlib.autoplaybetselection();

				if(betselection)
				{	
					gtr_autoplay.detailsAppend("verify that The player must be able to select their bet on this settings screen (including the total bet).", " The player must be able to select their bet on this settings screen (including the total bet).", "  The player is able to select their bet on this settings screen (including the total bet).", "Pass");
				}
				else
				{
					gtr_autoplay.detailsAppend("verify that The player must be able to select their bet on this settings screen (including the total bet).", "The player must be able to select their bet on this settings screen (including the total bet).", "The player is not able to select their bet on this settings screen (including the total bet).", "Fail");
				}	


				
				boolean result=cfnlib.is_Loss_Limit_Reached(gtr_autoplay);



				boolean winresult=cfnlib.is_Win_Limit_Reached(gtr_autoplay);





				boolean focus1 = cfnlib.Autoplay_focus_removed_UK();

				if(focus1 )
				{
					gtr_autoplay.detailsAppend(" verify On refresh, the previous AutoPlay session must  resume.","  On refresh, the previous AutoPlay session must  resume.","The previous AutoPlay session has  resume", "Pass");
				}
				else
				{
					gtr_autoplay.detailsAppend(" verify On refresh, the previous AutoPlay session must  resume.","  On refresh, the previous AutoPlay session must  resume.","The previous AutoPlay session has  resume", "Fail");				

				}


			}
		}	    
		//-------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error("Exception GTR autoplay", e);
			cfnlib.evalException(e);
		}
		//-------------------Closing the connections---------------//
		finally
		{	// delete the player created randomly from database.
			/*if(destFile.delete()){
				System.out.println(destFile.getName() + " is deleted!");
			}else{
				System.out.println("Test Data Delete operation is failed.");
			}*/

			webdriver.close();
			webdriver.quit();
			proxy.abort();
			Thread.sleep(1000);	   
		}	
	}

}
