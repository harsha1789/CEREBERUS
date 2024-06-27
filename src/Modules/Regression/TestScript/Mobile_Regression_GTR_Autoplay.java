package Modules.Regression.TestScript;

import java.io.File;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.Constant;

import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import net.lightbody.bmp.BrowserMobProxyServer;

public class Mobile_Regression_GTR_Autoplay 
{
	Logger log = Logger.getLogger(Mobile_Regression_GTR_Autoplay .class.getName());
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
		//String deviceLockKey=scriptParameters.getDeviceLockKey();
		String gameName=scriptParameters.getGameName();
		//int checkedOutDeviceNum=scriptParameters.getCheckedOutDeviceNum();
		
		String Status=null;
		int mintDetailCount=0;
		//int mintSubStepNo=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		 String classname = this.getClass().getSimpleName();
		String xmlFilePath= System.getProperty("user.dir")+"\\src\\Modules\\Regression\\TestData\\GameData\\"+classname+".testdata";
		 File sourceFile = new File(xmlFilePath);
		 File destFile=null;
		Mobile_HTML_Report gtr_autoplay=new Mobile_HTML_Report(webdriver,DeviceName,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,gameName);
 
		MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, gtr_autoplay, gameName);
		
		/*GlobalcfnLibrary gcfnlib = new GlobalcfnLibrary();
		
	   // DataBaseFunctions createplayer=new DataBaseFunctions("zensardev","Casino","10.247.208.54","1402","5001");
		DataBaseFunctions createplayer=new DataBaseFunctions("zensardev2","Casino","10.247.208.107","1402","5001");
		//DataBaseFunctions createplayer=new DataBaseFunctions("zensarqa","Casino","10.247.208.62","1402","5001");
		//DataBaseFunctions createplayer=new DataBaseFunctions("zensarqa2","Casino","10.247.208.113","1402","5001");
		*/
		try{
			// Step 1
			if(webdriver!=null)
			{		
				Map<String, String> rowData2 = null;
				Map<String, String> rowData3 = null;
				/*userName = gcfnlib.randomStringgenerator();
				log.debug("The New username is ==" + userName);
				createplayer.createUser(userName, "0", 0);
				
				 Casinoas1 ip for qa2 
				//destFile = new File("\\\\10.247.225.0\\C$\\MGS_TestData\\" +classname + "_" + userName +".testdata");
				 Casinoas1 ip for qa 
				//destFile = new File("\\\\10.247.224.110\\C$\\MGS_TestData\\" +classname + "_" + userName +".testdata");
				 Casinoas1 ip for dev 
				//destFile = new File("\\\\10.247.224.22\\c$\\MGS_TestData\\"+classname + "_" + userName +".testdata");
				 Casinoas1 ip for dev2 
				destFile = new File("\\\\10.247.224.239\\c$\\MGS_TestData\\"+classname + "_" + userName +".testdata");
				
				// ---Update the Xml File of Papyrus Data----------//
				gcfnlib.changePlayerName(userName, xmlFilePath);
				
				// ----Copy the Papyrus Data to The CasinoAs1 Server---------//
				gcfnlib.copyFolder(sourceFile, destFile);
				*/
				//String newFeatureScreen = cfnLibrary_Desktop.XpathMap.get("newFeatureScreen");
				
				
				/*String url=cfnlib.XpathMap.get("ApplicationURL");
				String obj=cfnlib.Func_navigate(url);
				String videoRecording=cfnlib.XpathMap.get("videoRecording");
				if(videoRecording.equalsIgnoreCase("yes"))
				{
				screen.StartScreenRecording(classname);
				}
					
				if(obj!=null)
				{
			          gtr_autoplay.details_append("Open Browser and Enter Lobby URL in address bar and click Enter", "Island Paradise Lobby should open", "Island Paradise lobby is Open", "Pass");
				}
				else
				{
					 gtr_autoplay.details_append("Open browser and Enter Game URL in address bar and click Enter", "Island Paradise lobby should be displayed", "Island paradise is not displayed", "Fail");
				}
				// Step s1.2 Login to application and verify the game title
				String password = constant.Password;
				String GameTitle=cfnlib.Func_LoginBaseScene(userName, password);
				
				
				
				//String GameTitle= cfnlib.func_LoginPracticePlay();
				
				//String GameTitle= cfnlib.func_LoginPracticePlay();		
				if(GameTitle!=null)
				{
					 gtr_autoplay.details_append("Verify that the application should display Game Logo and game name in Base Scene in Practice Play", "Game logo and game name with base scene should display", "Game logo and game name with base scene displays", "Pass");
				}
				else {
					 gtr_autoplay.details_append("Verify that the application should Logo and game name in Base scene in Practice Play", "Game logo and game name with base scene should display", "Game logo and game name with base scene is not display", "Fail");
				}
				
				//-------------------------------------------
*/
				if(framework.equalsIgnoreCase("Force")){
					cfnlib.setNameSpace();
				}
				String url = cfnlib.xpathMap.get("ApplicationURL");
				String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
				System.out.println("url = " +LaunchURl);
				if("LANDSCAPE".equalsIgnoreCase(cfnlib.xpathMap.get("Orientation")))
				{webdriver.context("NATIVE_APP");
				webdriver.rotate(ScreenOrientation.LANDSCAPE);
				webdriver.context("CHROMIUM");
				}
				cfnlib.loadGame(LaunchURl);

				if(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame")))
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
				
				boolean iSautoplay=cfnlib.ISAutoplayAvailable();
				if(iSautoplay)
				{	
				gtr_autoplay.detailsAppend("verify that AutoPlay�must be available in Base Game�only", "AutoPlay�must be available in Base Game�only", "AutoPlay�is available in Base Game�only", "pass");
				}
				else
				{
					gtr_autoplay.detailsAppend("verify that AutoPlay�must be available in Base Game�only", "AutoPlay�must be available in Base Game�only", "AutoPlay�is not available in Base Game�only", "fail");
				}	
				
				
				boolean is_quickspinon = cfnlib.autoPlayWithQSOn();
				if(is_quickspinon)
				{
					gtr_autoplay.detailsAppend("verify that AutoPlay with QuickSpin on must be available.","AutoPlay with QuickSpin on must be available.", "AutoPlay with QuickSpin is available", "Pass");
				}
				else{
					gtr_autoplay.detailsAppend("verify that AutoPlay with QuickSpin on must be available.","AutoPlay with QuickSpin on must be available.", "AutoPlay with QuickSpin is available", "Fail");
				}
				
				
				boolean autoplay=cfnlib.isAutoplayWithFreespin();
				if(autoplay)
				{
				  log.debug("Autoplay does not resume after free spin");
			      gtr_autoplay.detailsAppend("verify that Once the Bonus/Free Spins feature completes, the previous AutoPlay session must not resume.", "Once the Bonus/Free Spins feature completes, the previous AutoPlay session must not resume.�", "Once the Bonus/Free Spins feature completes, the previous AutoPlay session not resumed.","pass" );
				}
				else
				{
					 log.debug("Autoplay is  resume after free spin");
				      gtr_autoplay.detailsAppend("verify that Once the Bonus/Free Spins feature completes, the previous AutoPlay session must not resume.", "Once the Bonus/Free Spins feature completes, the previous AutoPlay session must not resume.�", "Once the Bonus/Free Spins feature completes, the previous AutoPlay session not resumed.","fail" );
					
				}
			     
				
				
				boolean spinvalue=cfnlib.autoplaySpinSelection();
				if(spinvalue)
				{	
				gtr_autoplay.detailsAppend("verify that The player must be able to select the number of spins to play.","The player must be able to select the number of spins to play.","The player able to select the number of spins to play.", "pass");
				}
				else
				{
					gtr_autoplay.detailsAppend("verify that The player must be able to select the number of spins to play.","The player must be able to select the number of spins to play.", "The player not able to select the number of spins to play.", "fail");
				}	
				
				boolean spin_session=cfnlib.isAutoplaySessionEnd();
				if(spin_session)
				{	
				gtr_autoplay.detailsAppend(" Verify that Once the selected number of spins has played out, the AutoPlay session must immediately end"," that Once the selected number of spins has played out, the AutoPlay session must immediately end","Autopaly has immediately stop.", "pass");
				}
				else
				{
					gtr_autoplay.detailsAppend("verify  that Once the selected number of spins has played out, the AutoPlay session must immediately end"," that Once the selected number of spins has played out, the AutoPlay session must immediately end", "Autoplay session has not immediatly stop.","fail");	
			    }	
				
				boolean max_Spin=cfnlib.maxSpinChk();
				if(max_Spin)
				{	
				gtr_autoplay.detailsAppend(" verify that The maximum number of spins allowed must be 100.","  The maximum number of spins allowed must be 100.","maximunm no of spins allowed are 100.", "pass");
				}
				else
				{
					gtr_autoplay.detailsAppend("verify that The maximum number of spins allowed must be 100.","  The maximum number of spins allowed must be 100.", "maximunm no of spins allowed are 100.","fail");	
			    }	
				
				boolean isautoplaywin = cfnlib.isAutoplayWindow();
				if(isautoplaywin)
				{	
				gtr_autoplay.detailsAppend(" verify that On selection of AutoPlay, the player must always be taken to an AutoPlay settings screen/dialog/window etc."," On selection of AutoPlay, the player must always be taken to an AutoPlay settings screen/dialog/window etc.","Player is on Autoplay setting/window/dialog .", "pass");
				}
				else
				{
					gtr_autoplay.detailsAppend("verify that On selection of AutoPlay, the player must always be taken to an AutoPlay settings screen/dialog/window etc."," On selection of AutoPlay, the player is not taken to an AutoPlay settings screen/dialog/window etc.", "Player is not on Autoplay setting/window/dialog .","fail");	
			    }	
				boolean onrefresh = cfnlib.autoplayOnRefresh();
				if(onrefresh)
				{	
				gtr_autoplay.detailsAppend(" verify On refresh, the previous AutoPlay session must not resume.","  On refresh, the previous AutoPlay session must not resume.","The previous AutoPlay session has not resume", "pass");
				
				}
				else
				{
					gtr_autoplay.detailsAppend("verify On refresh, the previous AutoPlay session must not resume."," On refresh, the previous AutoPlay session has resume.", "The previous AutoPlay session has resume","fail");	
			    }
				//Desktop_HTML_Report.recorder.stop();
					boolean focus = cfnlib.autoplayFocusRemoved();
				if(focus)
				{	
				gtr_autoplay.detailsAppend(" verify autoPlay must not resume when the game content is no longer visible (focus being removed).","  autoPlay must not resume when the game content is no longer visible (focus being removed).","Autoplay  continue when game content is not visible", "pass");
				}
				else
				{
					gtr_autoplay.detailsAppend("verify autoPlay must not resume when the game content is no longer visible (focus being removed)."," autoPlay must not resume when the game content is no longer visible (focus being removed).", "Autoplay continue when game content is visible","fail");	
			    }
				
				//cfnlib.verifyGameAnalytics();
				
				
				//log out the current user and creating new user in UK Regulatory market
				String uk_url = cfnlib.xpathMap.get("UK_url");
				cfnlib.loadGame(uk_url);
				cfnlib.newFeature();
				//gtr_autoplay.videoRecording("AutoplayBetSelection");
				boolean isautoplay=cfnlib.ISAutoplayAvailable();
				if(isautoplay)
				{	
					gtr_autoplay.detailsAppend("verify that AutoPlay may include a list of AutoPlay presets (quick select spin options).", " AutoPlay may include a list of AutoPlay presets (quick select spin options).", "that AutoPlay may include a list of AutoPlay presets (quick select spin options).", "pass");
				}
				else
				{
					gtr_autoplay.detailsAppend("verify that AutoPlay may include a list of AutoPlay presets (quick select spin options).", "AutoPlay may include a list of AutoPlay presets (quick select spin options).", "that AutoPlay may include a list of AutoPlay presets (quick select spin options).", "fail");
				}	
				
			      
				boolean betselection=cfnlib.autoplaybetselection();
				if(betselection)
				{	
					gtr_autoplay.detailsAppend("verify that The player must be able to select their bet on this settings screen (including the total bet).", " The player must be able to select their bet on this settings screen (including the total bet).", "  The player is able to select their bet on this settings screen (including the total bet).", "video");
				}
				else
				{
					gtr_autoplay.detailsAppend("verify that The player must be able to select their bet on this settings screen (including the total bet).", "The player must be able to select their bet on this settings screen (including the total bet).", "The player is not able to select their bet on this settings screen (including the total bet).", "video");
				}	
				
				
				boolean flag=cfnlib.isLossLimitLowerThanCurBet();
				if(flag)
				{	
					gtr_autoplay.detailsAppend("verify that The player must not be able to select a Loss Limit lower than their current bet value.", " The player must not be able to select a Loss Limit lower than their current bet value.", "The player must not be able to select a Loss Limit lower than their current bet value.", "pass");
				}
				else
				{
					gtr_autoplay.detailsAppend("verify that The player must not be able to select a Loss Limit lower than their current bet value.", " The player must not be able to select a Loss Limit lower than their current bet value.", " The player  able to select a Loss Limit lower than their current bet value.", "fail");
				}	
				//gtr_autoplay.videoRecording("Is Loss Limit Reached");
				boolean losslimitchk=cfnlib.isLossLimitReached();
				if(losslimitchk)
				{
					gtr_autoplay.detailsAppend("verify that when the Loss Limit is reached, a message must show on screen, indicating that the Loss Limit has been reached", " when the Loss Limit is reached, a message must show on screen, indicating that the Loss Limit has been reached. ", "  when the Loss Limit is reached, a message must show on screen, indicating that the Loss Limit has been reached.", "video");
				}
				else
				{
					gtr_autoplay.detailsAppend("verify that when the Loss Limit is reached, a message must show on screen, indicating that the Loss Limit has been reached", " when the Loss Limit is reached, a message must show on screen, indicating that the Loss Limit has been reached. ", "  when the Loss Limit is reached, no message show on screen, indicating that the Loss Limit has been reached.", "video");

				}
				//gtr_autoplay.videoRecording("Is Win Limit Reached");
				boolean winlimitchk=cfnlib.isWinLimitReached();
				if(winlimitchk)
				{
					gtr_autoplay.detailsAppend("verify that when the Win Limit is reached, a message must show on screen, indicating that the Win Limit has been reached", " when the Win Limit is reached, a message must show on screen, indicating that the Win Limit has been reached. ", "  when the Win Limit is reached, a message must show on screen, indicating that the Win Limit has been reached.", "video");
				}
				else
				{
					gtr_autoplay.detailsAppend("verify that when the Win Limit is reached, a message must show on screen, indicating that the Win Limit has been reached", " when the Win Limit is reached, a message must show on screen, indicating that the Win Limit has been reached. ", "  when the Win Limit is reached, no message show on screen, indicating that the Win Limit has been reached.", "video");

				}
				
				
				//Start video
			      //gtr_autoplay.videoRecording("Autoplay_focus_removedUK");
				boolean focus1 = cfnlib.autoplayFocusRemovedUK();
				 //End video
				if(focus1 )
				{
					gtr_autoplay.detailsAppend(" verify On refresh, the previous AutoPlay session must  resume.","  On refresh, the previous AutoPlay session must  resume.","The previous AutoPlay session has  resume", "video");
					}
				else
				{
					gtr_autoplay.detailsAppend(" verify On refresh, the previous AutoPlay session must  resume.","  On refresh, the previous AutoPlay session must  resume.","The previous AutoPlay session has  resume", "video");				

				}
   

			}
		}
		//-------------------Handling the exception---------------------//
		catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
		//	screen.StopScreenRecording(videoPath);
			gtr_autoplay.endReport();
			webdriver.close();
			webdriver.quit();
			proxy.abort();
			Thread.sleep(3000);
		}	

	}
}
