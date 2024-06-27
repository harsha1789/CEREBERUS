package Modules.Regression.TestScript;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.DataBaseFunctions;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;

		
/**
 * This Script traverse all the panels and take the screen shot of the all screens such as paytable, bet settings, auto play, bigwin, jackpot , freespin etc.
 * It reads the test data excel sheet for configured languages.
 * @author Saloni
 */

public class Desktop_Regression_Language_Verification_Compatibility{
	
	Logger log = Logger.getLogger(Desktop_Regression_Language_Verification_Compatibility.class.getName());
	public ScriptParameters scriptParameters;
	


	public void script() throws Exception{
		
		String mstrTCName=scriptParameters.getMstrTCName();
		String mstrTCDesc=scriptParameters.getMstrTCDesc();
		String mstrModuleName=scriptParameters.getMstrModuleName();
		WebDriver webdriver=scriptParameters.getDriver();
		BrowserMobProxyServer proxy=scriptParameters.getProxy();
		String startTime=scriptParameters.getStartTime();
		String filePath=scriptParameters.getFilePath();
		String userName=scriptParameters.getUserName();
		String browserName=scriptParameters.getBrowserName();
		String framework=scriptParameters.getFramework();
		String gameName=scriptParameters.getGameName();
		String languageDescription=null;
		String languageCode=null;
		String status=null;
		int mintDetailCount=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		
		ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();
		String destFile=null;
		Desktop_HTML_Report language = new Desktop_HTML_Report(webdriver,browserName,filePath,startTime,mstrTCName,mstrTCDesc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,status,gameName);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, language, gameName);
		int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDDesktop"));

		
		List<String> copiedFiles=new ArrayList<>();

		RestAPILibrary apiobj=new RestAPILibrary();
		CommonUtil util = new CommonUtil();
		DataBaseFunctions createplayer = new DataBaseFunctions(TestPropReader.getInstance().getProperty("ServerName"),
				TestPropReader.getInstance().getProperty("dataBaseName"),
				TestPropReader.getInstance().getProperty("serverIp"), TestPropReader.getInstance().getProperty("port"),
				TestPropReader.getInstance().getProperty("serverID"));
		
		
		
		try{
			// Step 1 
			if(webdriver!=null)
			{		
				Map<String, String> rowData2 = null;
				Map<String, String> rowData3 = null;
				
				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				String urlNew=null;
				
				//sv65878
				
					
					
					String url1 = cfnlib.XpathMap.get("ApplicationURL");
					String launchURL = url1.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
					log.info("url = " +launchURL);
								
					cfnlib.loadGame(launchURL);
				     log.debug("navigated to url ");
					
				    //wait to load assets
					cfnlib.threadSleep(10000);
					
					cfnlib.closeOverlay();
					
				
								
				
				//String url = cfnlib.XpathMap.get("ApplicationURL");
				//userName = "Zen_zou2wlu";

				if(framework.equalsIgnoreCase(Constant.FORCE)){
					cfnlib.setNameSpace();
					}
				List<Map> list= util.readLangList();
				
				
				int rowCount2 = excelpoolmanager.rowCount(testDataExcelPath, Constant.LANG_XL_SHEET_NAME);
				for(int j=1;j<rowCount2;j++){     
						
					
					
					
					//Step 2: To get the languages in MAP and load the language specific url
					rowData2 = list.get(j);
					languageDescription = rowData2.get(Constant.LANGUAGE).trim();
					languageCode = rowData2.get(Constant.LANG_CODE).trim();
					
					if(true) 
					{
						
						String StrfileNamebase=TestPropReader.getInstance().getProperty("BaseSceneTestDataPath");
						File testDataFile = new File(StrfileNamebase);
						userName=util.randomStringgenerator();
						
						if(	util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles)) {
							String currUrl = webdriver.getCurrentUrl();
							String launchcurrURL = currUrl.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
							cfnlib.loadGame(launchcurrURL);
						     log.debug("navigated to url ");
										
						    //wait to load assets
							cfnlib.threadSleep(10000);
							
							
							cfnlib.closeOverlay();
							
							
						
					language.detailsAppend("Verify the Language code is " +languageCode+" ", " Application window should be displayed in " +languageDescription+"", "", "");
					
					
					
					cfnlib.splashScreen(language,languageCode);					
					
					language.detailsAppendFolder(" Verify that the application should display with Game Logo and game name", "Game logo and game name should display", "Game logo and game name displays", "Pass", languageCode);
					Thread.sleep(4000);
					
					cfnlib.closeOverlay();
					//cfnlib.newFeature();
					Thread.sleep(1500);
					
					//Not required for force
					
					if(!framework.equalsIgnoreCase(Constant.FORCE)){
						cfnlib.verifyStopLanguage(language, languageCode);
					}
						
				
					Thread.sleep(2000);
					
					
					
					
					
					
//======================================= Open and Capture Screen shot of Bet Screen
				boolean b = cfnlib.open_TotalBetupdated(language, languageCode);
					if (b){
						Thread.sleep(500);
					 language.detailsAppendFolder("Verify language on the Bet Amount Screen is "+ languageDescription +"", "Bet Amount Screen should be display in", "Bet Amount Screen should be displayed in " +languageDescription+ " language ", "PASS", languageCode);
					
					 //Refresh
					 webdriver.navigate().refresh();
					 Thread.sleep(7000);
					 
					 cfnlib.closeOverlay();
					// cfnlib.newFeature();
					 language.detailsAppendFolder(" Refresh on the Bet Screen Panel", "Refresh under Bet Screen ", "Refresh under Bet Screen " , "PASS", languageCode);
					 
					}
					else{
					 language.detailsAppendFolder("Verify language on the Bet Amount Screen", "Bet Amount Screen should be display", "Bet Amount Screen doesn't display", "Fail", languageCode);   
					}
					// Close the Bet Screen
				//	cfnlib.close_TotalBet();  
					
			
					
// =================================Autoplay Quick option Enable Disable and performance Screenshot (UI + func)
	
				
				  cfnlib.closeOverlay();
				  Thread.sleep(3000);
				  System.out.println("Autoplay Open");
				  cfnlib.autoPlay_with_QSUpdated(language, languageCode);
				  System.out.println("Autoplay Close");
				 
					
//==================================== Open and Capture Screen shot of menu Screen
					boolean menuOpen = cfnlib.menuOpen();
					if(!framework.equalsIgnoreCase("CS_Renovate")){
						if(menuOpen){
							
							language.detailsAppendFolder("Verify that Language of menu link is " + 
							languageDescription + " ", "Language of Menu Links should be " +languageDescription+ "", 
							"Menu Links are displaying in " +languageDescription+ " language", "pass", languageCode);
							
								cfnlib.resizeBrowser(400,800);
								Thread.sleep(1000);
								language.detailsAppendFolder("verify the Browser Resize under Menu  ", " Menu Browser Resize screen shot","Menu Browser Resize screen shot ", "PASS", languageCode);
								Thread.sleep(2000);
								
								webdriver.manage().window().maximize();
								language.detailsAppendFolder("verify the Browser FullScreen under Menu ", " Menu Browser FullScreen screen shot","Menu Browser FullScreen screen shot ", "PASS", languageCode);
								Thread.sleep(2000);
								
								
								
							/*	webdriver.navigate().refresh();
								 Thread.sleep(6000);
								 language.detailsAppend("Verify Menu Refresh ",
											"Menu Screen is Refreshed", "Menu Refreshed ",
											"PASS");
								 Thread.sleep(4000);
								 cfnlib.closeOverlay();
								// cfnlib.newFeature();
							
							*/
							 
							 
							 	}
						else {
							 language.detailsAppendFolder("Verify that Language" + languageDescription + "should display properly on menu links", "Language inside Menu Links should be " +languageDescription+ " ", "Menu Links are not displaying", "fail", languageCode);
						}
					}
					// Close Menu screen
					cfnlib.menuClose();
					 cfnlib.closeOverlay();

					
					
					
//==================================== Open and Capture Screen shot of Settings
					
					//check first whether setting avilable or not
					if(!framework.equalsIgnoreCase("CS")&& cfnlib.checkAvilability(cfnlib.XpathMap.get("isMenuSettingsBtnVisible"))){
					boolean openSetting= cfnlib.settingsOpen();
					if(openSetting){
						 language.detailsAppendFolder("Verify that Language on settings screen is " + languageDescription + " ", "Language in Settigns Screen should be " +languageDescription+ " ", "Language inside Settings screens is " +languageDescription+ " ", "pass", languageCode);
					
						 
						 
						 cfnlib.resizeBrowser(400,800);
							Thread.sleep(1000);
							language.detailsAppendFolder("verify the Browser Resize under Settings  ", " Settings Browser Resize screen shot","Settings Browser Resize screen shot ", "PASS", languageCode);
							Thread.sleep(2000);
							
							webdriver.manage().window().maximize();
							language.detailsAppendFolder("verify the Browser FullScreen under Settings ", " Settings Browser FullScreen screen shot","Settings Browser FullScreen screen shot ", "PASS", languageCode);
							Thread.sleep(2000);
							
							
							
							webdriver.navigate().refresh();
							 Thread.sleep(3000);
							 language.detailsAppend("Verify Settings Refresh ",
										"Settings Screen is Refreshed", "Settings Refreshed ",
										"PASS");
							 Thread.sleep(6000);
							 cfnlib.closeOverlay();
						//	 cfnlib.newFeature();
					
					}
					else {
						 language.detailsAppendFolder("Verify that Language back on menu is " + languageDescription +"should be display properly on menu links", "Language inside Menu Links should be in " +languageDescription+ " language", "Language inside Menu Links is displaying", "fail", languageCode);
					}

					cfnlib.settingsBack();
					}
	
				
					
//==================================== Open and Capture Screen shot of Motivator message					
					if(TestPropReader.getInstance().getProperty("IsProgressiveGame").equalsIgnoreCase("Yes"))
					{
						
						//To capture  motivator and messages in Progressive game
						//Need player with win
						if(TestPropReader.getInstance().getProperty("IsMotivators&Messages").equalsIgnoreCase("Yes"))
						{
						String strNoOfMsgWithReelsSpinning = cfnlib.XpathMap.get("noOfMsgWithReelsSpinning");
						int noOfMsgWithReelsSpinning = (int) Double.parseDouble(strNoOfMsgWithReelsSpinning); 
						
						String strNoOfMsgWithReelsResolved = cfnlib.XpathMap.get("noOfMsgWithReelsResolved");
						int noOfMsgWithReelsResolved = (int) Double.parseDouble(strNoOfMsgWithReelsResolved); 
						
						
						
						String strNOfMotivators = cfnlib.XpathMap.get("noOfMotivators");
						int noOfMotivators = (int) Double.parseDouble(strNOfMotivators); 
						
						for (int spinCount=0; spinCount < noOfMsgWithReelsResolved; spinCount++)
						{
							cfnlib.spinclick();
							if(spinCount< noOfMsgWithReelsSpinning)
							{
								language.detailsAppendFolder("Verify Mesaage display below reels in language  " + languageDescription + " ", "Mesaage should be in  " +languageDescription+ " ", "Message is in  " +languageDescription+ " ", "pass", languageCode);
							}
							cfnlib.waitForSpinButtonstop();
							language.detailsAppendFolder("Verify Mesaage display below reels in language  " + languageDescription + " ", "Mesaage should be in  " +languageDescription+ " ", "Message is in  " +languageDescription+ " ", "pass", languageCode);
							Thread.sleep(1000);
						}
						}
						// Jackpot History
						cfnlib.verifyJackPotBonuswithScreenShots(language,languageCode);
						
					}
					
//==================================== PAytable===============================
					
					Thread.sleep(5000);
				
					cfnlib.closeOverlay();
					
					Thread.sleep(3000);
					
					
					if(!gameName.contains("Scratch") && cfnlib.checkAvilability(cfnlib.XpathMap.get("isMenuPaytableBtnVisible"))){
					
					//Open payatable and capture screen shots
					cfnlib.capturePaytableScreenshot(language, languageCode);
					// Closes the paytable
					cfnlib.paytableClose();
					}
					
					// To open and capture Story in Game (Applicable for the game immortal romances)
					if(cfnlib.checkAvilability(cfnlib.XpathMap.get("isPaytableStoryExists"))){
							cfnlib.Verifystoryoptioninpaytable(language, languageCode);
							cfnlib.paytableClose();
						log.debug("Done for story in paytable");
						}
					
					//Incase of Respin games it takes the bet dialog screenshot on bet change
					if("yes".equalsIgnoreCase(cfnlib.XpathMap.get("IsRespinFeature")))	
					{
						cfnlib.waitForSpinButton();
						Thread.sleep(1000);
						cfnlib.spinclick();
						cfnlib.waitForSpinButtonstop();
						Thread.sleep(5000);
						cfnlib.open_TotalBet();
						cfnlib.setMaxBet();
						cfnlib.close_TotalBet();
						language.detailsAppendFolder("Verify that Language on Bet Dialog screen is " + languageDescription + " ", "Language in Bet Dialog Screen should be " +languageDescription+ " ", "Language inside Bet Dialog screens is " +languageDescription+ " ", "pass", languageCode);
						cfnlib.clickAtButton(cfnlib.XpathMap.get("ClickOnBetDialogNo"));
						log.debug("Done for bet dialog");
					}
					
					Thread.sleep(2000);
					cfnlib.paytableClose();
		//==================================== PAytable_resize===============================			
					
					Thread.sleep(2000);
					cfnlib.closeOverlay();
				
					if(!gameName.contains("Scratch") && cfnlib.checkAvilability(cfnlib.XpathMap.get("isMenuPaytableBtnVisible"))){
						
						
						
						//resize
						 cfnlib.resizeBrowser(400,800);
							Thread.sleep(1000);
							language.detailsAppendFolder("verify the Browser Resize under Paytable  ", " Paytable Browser Resize screen shot","Paytable Browser Resize screen shot ", "PASS", languageCode);
							Thread.sleep(2000);
							
							
					//Open payatable and capture screen shots
					cfnlib.capturePaytableScreenshot_Resize(language, languageCode);
					// Closes the paytable
					cfnlib.paytableClose();
					}
					
					// To open and capture Story in Game (Applicable for the game immortal romances)
					if(cfnlib.checkAvilability(cfnlib.XpathMap.get("isPaytableStoryExists"))){
							cfnlib.Verifystoryoptioninpaytable(language, languageCode);
							cfnlib.paytableClose();
						log.debug("Done for story in paytable");
						}
					
					//Incase of Respin games it takes the bet dialog screenshot on bet change
					if("yes".equalsIgnoreCase(cfnlib.XpathMap.get("IsRespinFeature")))	
					{
						cfnlib.waitForSpinButton();
						Thread.sleep(1000);
						cfnlib.spinclick();
						cfnlib.waitForSpinButtonstop();
						Thread.sleep(5000);
						cfnlib.open_TotalBet();
						cfnlib.setMaxBet();
						cfnlib.close_TotalBet();
						language.detailsAppendFolder("Verify that Language on Bet Dialog screen is " + languageDescription + " ", "Language in Bet Dialog Screen should be " +languageDescription+ " ", "Language inside Bet Dialog screens is " +languageDescription+ " ", "pass", languageCode);
						cfnlib.clickAtButton(cfnlib.XpathMap.get("ClickOnBetDialogNo"));
						log.debug("Done for bet dialog");
					}
					webdriver.manage().window().maximize();
					cfnlib.paytableClose();
					cfnlib.closeOverlay();
					
					
					}
						
						else
						{
							log.debug("Unable to copy test data file on the environment hence skipping execution");
							language.detailsAppend("", "", "Unable to copy test data on server,hence skipping futher execution", "fail");
						}
						
					}
					
		
	//===========================================BIGWIN======================================================================
					
					
					//put BigwinChecks = yes in testdata to execute this code
					if("yes".equalsIgnoreCase(cfnlib.XpathMap.get("BigwinChecks"))) {
						
						userName=util.randomStringgenerator();
						String strFileName=TestPropReader.getInstance().getProperty("BigWinTestDataPath");
						File testDataFile1=new File(strFileName);
						
						if(	util.copyFilesToTestServer(mid, cid, testDataFile1, userName, copiedFiles))
						{	
						     
						     
						//String url = cfnlib.XpathMap.get("ApplicationURL");
						String currUrl1 = webdriver.getCurrentUrl();
						String launchURl1 = currUrl1.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
						
						log.info("url = " +launchURl1);
						System.out.println(launchURl1);
						
						cfnlib.loadGame(launchURl1);
						 log.debug("navigated to url ");
						
						if(framework.equalsIgnoreCase("Force")){
							cfnlib.setNameSpace();
							
						}
						
						
						language.detailsAppendFolder("Verify Game in Language " +languageCode+" ", " Application window should be displayed in " +languageDescription+"", "", "", languageCode);
						
						Thread.sleep(6000);
						cfnlib.closeOverlay();// Added while immortal romance game.
						cfnlib.waitForSpinButton();
						cfnlib.setMaxBet();
						//--------Bigwin test
						cfnlib.spinclick();
						cfnlib.waitForSpinButton();
						boolean result=cfnlib.waitForBigWin();
						
						if(result)
						{	language.detailsAppendFolder("Verify that big win screen is displaying with overlay  ","Big win screen must display , Once big win triggers ","On  triggering big win,Big win screen is displaying","pass",languageCode);
							
							
						//*************************************RESIZE******************sv65878
							
							cfnlib.resizeBrowser(400,800);
							Thread.sleep(1000);
							language.detailsAppendFolder("verify the Browser Resize under Bigwin ", " Bigwin Browser Resize screen shot","Bigwin Browser Resize screen shot ", "PASS", languageCode);
							Thread.sleep(2000);
							
							webdriver.manage().window().maximize();
							language.detailsAppendFolder("verify the Browser FullScreen under Bigwin", " Bigwin Browser FullScreen screen shot","Bigwin Browser FullScreen screen shot ", "PASS", languageCode);
							Thread.sleep(2000);
							
							
							
							
							// set BigWinlayers = yes if their is bigwin,superwin, megawin layers are present in the game to take 3 screenshots
							if("Yes".equalsIgnoreCase(cfnlib.XpathMap.get("BigWinlayers")))
							{
								for(int i =0;i<=2;i++)
								{
									cfnlib.waitForBigWin();
									language.detailsAppendFolder("Verify that big win screen is displaying with overlay ","Big win screen must display ,Once big win triggers ","On  triggering big win,Big win screen is displaying","pass",languageCode);
	 						     log.debug("Bigwinlayer captured" + i);
	 						     System.out.println("Bigwinlayer captured" + i);
								}
								
								
								
								
									
									
								
								
								
								
								
							}
						else{
							log.info("Big win Layers not present in game as not mention in testdata file");
							}
							//To click on overlay
							Thread.sleep(5000);
							cfnlib.closeOverlay();
							Thread.sleep(2000);
							language.detailsAppendFolder("Verify that big win screen countup is completed ","Big win count up should be completed ","Big win count up is completed","pass",languageCode);
						}
						else
						{
							language.detailsAppendFolder("Verify that big win screen is displaying with overlay ","Big win screen must display ,Once big win triggers ","On  triggering big win,Big win screen is not displaying","fail",languageCode);

						}

						
						}
						
						else
						{
							log.debug("Unable to copy test data file on the environment hence skipping execution");
							language.detailsAppend("", "", "Unable to copy test data on server,hence skipping futher execution", "fail");
						}
						
						
					}	
						
						
						
						
//=========================================== Freespin========================================================
						if("yes".equalsIgnoreCase(cfnlib.XpathMap.get("FreespinChecks"))) {
							
							WebDriverWait wait=new WebDriverWait(webdriver,60);
							String strFileNameFree=TestPropReader.getInstance().getProperty("FreeSpinsTestDataPath");
							File testDataFileFreeSpin=new File(strFileNameFree);

							userName=util.randomStringgenerator();
						//	userName = "Zen_mpra8h6";

						
						
						
						
						if(	util.copyFilesToTestServer(mid, cid, testDataFileFreeSpin, userName, copiedFiles))
						{
							log.debug("Test data is copy in test Server for Username="+userName);
							
							String urlFree = webdriver.getCurrentUrl();
							String launchURLFreeSpin = urlFree.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
							log.info("url = " +launchURLFreeSpin);
							cfnlib.loadGame(launchURLFreeSpin);
							Thread.sleep(15000);
							log.debug("navigated to url ");
							
							
							
							//continueBtn
							//cfnlib.newFeature();
							
							cfnlib.closeOverlay();
							
					
							@SuppressWarnings("rawtypes")
							List<Map> list1= util.readLangList();
							log.debug("Total number of Languages configured"+rowCount2);

							
							
							// Click on Spin button
							cfnlib.waitForSpinButton();
							Thread.sleep(3000);
							
							//maxBet
							if(TestPropReader.getInstance().getProperty("IsProgressiveGame").equalsIgnoreCase("Yes")){
								cfnlib.setMaxBet();
							}
							
							//spinBtn
							cfnlib.spinclick();
							Thread.sleep(1000);
							
							if(TestPropReader.getInstance().getProperty("IsProgressiveGame").equalsIgnoreCase("Yes"))
							{
								language.detailsAppend("Verify Different Languages on Jockpot feature","Jackpot feature should display as per respective language", "", "" );
								cfnlib.jackpotSceneWait();
								printTime("After wait Jackpot scene");
								Thread.sleep(12000);
								language.detailsAppendFolder("Verify that the application should display jackpot scene in " + languageCode + " ","jackpot scene should display in " + languageDescription + " ","jackpot scene displays in " + languageDescription + " ", "Pass", languageCode);
								printTime("After first screen shot");
								Thread.sleep(12000);
								language.detailsAppendFolder("Verify that the application should display jackpot scene in " + languageCode + " ","jackpot scene should display in " + languageDescription + " ","jackpot scene displays in " + languageDescription + " ", "Pass", languageCode);
								Thread.sleep(8000);
								printTime("After second screen shot");
								language.detailsAppendFolder("Verify that the application should display jackpot scene in " + languageCode + " ","jackpot scene should display in " + languageDescription + " ","jackpot scene displays in " + languageDescription + " ", "Pass", languageCode);
								printTime("After Third screen shot");
								
								
								//*******************************sv65878-RESIZE
								
								
								cfnlib.resizeBrowser(400,800);
								Thread.sleep(1000);
								language.detailsAppendFolder("verify the Browser Resize under Jackpot Summary ", " Jackpot Summary Browser Resize screen shot","Jackpot Summary Browser Resize screen shot ", "PASS", languageCode);
								Thread.sleep(2000);
								
								webdriver.manage().window().maximize();
								language.detailsAppendFolder("verify the Browser FullScreen under Jackpot Summary", " Jackpot Summary Browser FullScreen screen shot","Jackpot Summary Browser FullScreen screen shot ", "PASS", languageCode);
								Thread.sleep(2000);
							

							
							
							printTime("Going to click jackpot spin");
							cfnlib.clickAtButton("return "+cfnlib.XpathMap.get("ClickOnJackpotSpinButton"));
						
							//cfnlib.spinclick();
							Thread.sleep(3000);
							printTime("Going to click Continue :: "+"return "+cfnlib.XpathMap.get("isJackpotBonusContinueButtonVisible"));
							
							// method to wait for jackpotscene summary screen
							cfnlib.elementWait("return "+cfnlib.XpathMap.get("isJackpotBonusContinueButtonVisible"), true);
							printTime("Continue clicked");
							
							language.detailsAppendFolder("Verify that the application should display jackpot summary screen in " + languageCode + " ","Jakcpot summary screen should display in " + languageDescription + " ","Jackpot summary screen displays in " + languageDescription + " ", "Pass", languageCode);
							
							
							//sv65878 - resize
							
							
							
							if(gameName.contains("ImmortalRomanceMegaMoolah"))
							{
								cfnlib.closeOverlay();
								
							}
							else
							{
								cfnlib.clickAtButton("return "+cfnlib.XpathMap.get("ClickOnJackpotSummaryContinue"));
							}
							
							
							}
							
							
							// FreeSpinEntryScreen string should be yes in excel sheet, if entry screen is present
							String FreeSpinEntryScreen = cfnlib.XpathMap.get("FreeSpinEntryScreen");				
							String b2 = cfnlib.entryScreen_Wait(FreeSpinEntryScreen);
							log.info("FreeSpinEntryScreen response"+b2);
							if(b2.equalsIgnoreCase("freeSpin")) {
		
								webdriver.navigate().refresh();

								//if(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.XpathMap.get("TypeOfGame"))|| (Constant.YES.equalsIgnoreCase(cfnlib.XpathMap.get("continueBtnOnGameLoad"))))
							//	{
								//	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cfnlib.XpathMap.get("OneDesign_NewFeature_ClickToContinue"))));
									Thread.sleep(5000);
									cfnlib.closeOverlay();
									//cfnlib.newFeature();
									Thread.sleep(3000);
							//	}
							/*
							 * else {
							 * wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cfnlib.
							 * XpathMap.get("clock")))); Thread.sleep(1000); cfnlib.closeOverlay(); }
							 */
								
								cfnlib.closeOverlay();
								language.detailsAppend("Verify Different Languages on Free spin entry screen","Free spin entry screen should display as per respective language", "", "" );
							
								
								
								
								log.info("FreeSpinEntryScreen response"+languageDescription+" "+languageCode);

								//For Progressive games
								if( TestPropReader.getInstance().getProperty("IsProgressiveGame").equalsIgnoreCase("Yes"))
								{
									cfnlib.elementWait("return "+cfnlib.XpathMap.get("isJackpotBonusContinueButtonVisible"), true);
									
									Thread.sleep(1000);
									if(gameName.contains("ImmortalRomanceMegaMoolah"))
									{
										Thread.sleep(1000);
										cfnlib.closeOverlay();
										
									}
									else
									{
										cfnlib.clickAtButton("return "+cfnlib.XpathMap.get("ClickOnJackpotSummaryContinue"));
									}
									
									
								}
								
								
								
						//		cfnlib.entryScreen_Wait(FreeSpinEntryScreen);
							//	log.info("After wait"+languageDescription+" "+languageCode);
								

					
								
								
							}
							else{
								log.debug("Free Spin Entry screen is not present in Game"); 
							}
							
							
							language.detailsAppendFolder("Verify that the application should display Free spin entry screen in " +languageCode+" ","Free spin entry screen should display in " +languageDescription+" ","Free spin entry screen displays in " +languageDescription+ " ", "Pass", languageCode); 	
							
							
							
							
							
							//RESIZE
							
							cfnlib.resizeBrowser(400,800);
							Thread.sleep(1000);
							language.detailsAppendFolder("verify the Browser Resize under FreeSpin Entry Screen ", " FreeSpin Entry Screen Browser Resize screen shot","FreeSpin Entry Screen Browser Resize screen shot ", "PASS", languageCode);
							Thread.sleep(2000);
							
							webdriver.manage().window().maximize();
							language.detailsAppendFolder("verify the Browser FullScreen under FreeSpin Entry Screen", " FreeSpin Entry Screen Browser FullScreen screen shot","FreeSpin Entry Screen Browser FullScreen screen shot ", "PASS", languageCode);
							Thread.sleep(2000);
							
							
							
							
							
							
							// Logic to capture start freespin screen button scene
							String startFreeSpinButton = cfnlib.XpathMap.get("StartFreeSpinButton");
							log.info("startFreeSpinButton Value is ::" +startFreeSpinButton );
							if(startFreeSpinButton.equalsIgnoreCase("Yes")) 
							{
								log.info("startFreeSpin is in progress" );
								webdriver.navigate().refresh();
								
								Thread.sleep(5000);
								cfnlib.closeOverlay();
								cfnlib.FSSceneLoading();
								
								language.detailsAppend("Verify the Different Languages in Free Spin","Free Spin window should display as per respective language", "", "");
								Thread.sleep(2000);
								//correct>>
								language.detailsAppendFolder("Verify that the application should display Free Spin scene in " + languageCode + " ","Free Spin scene should display in " + languageDescription + " ","Free Spin scene displays in " + languageDescription + " ", "Pass", languageCode);
								
							
								cfnlib.FS_Start();
							
							
							
							}
							
							
							
							if("yes".equals(cfnlib.XpathMap.get("isFreeSpinSelectionAvailable")))
							{
								String strfreeSpinSelectionCount=cfnlib.XpathMap.get("NoOfFreeSpinBonusSelection");
								int freeSpinSelectionCount = (int) Double.parseDouble(strfreeSpinSelectionCount);

								if(!(gameName.contains("ImmortalRomanceMegaMoolah")) && Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.XpathMap.get("TypeOfGame")))
								{
								
									/*
									 * wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cfnlib.
									 * XpathMap.get("OneDesign_NewFeature_ClickToContinue")))); cfnlib.newFeature();
									 */
									//.sleep(5000);
									//cfnlib.closeOverlay();
								}
								cfnlib.clickBonusSelection(freeSpinSelectionCount);
							}
							else{
								if( TestPropReader.getInstance().getProperty("IsProgressiveGame").equalsIgnoreCase("Yes"))
								{
									cfnlib.elementWait("return "+cfnlib.XpathMap.get("isJackpotBonusContinueButtonVisible"), true);
									cfnlib.clickAtButton("return "+cfnlib.XpathMap.get("ClickOnJackpotSummaryContinue"));
									Thread.sleep(8000);
									cfnlib.clickToContinue();
									Thread.sleep(2000);
									cfnlib.FS_continue();
									Thread.sleep(4000);
								}
								else{
									//cfnlib.clickToContinue();
									//Click on freespins into continue button
									if("yes".equals(cfnlib.XpathMap.get("isFreeSpinIntroContinueBtnAvailable")))
									{
										cfnlib.clickToContinue();
									}
									else
									{
										System.out.println("There is not Freespins Into Continue button in this game");
										log.debug("There is not Freespins Into Continue button in this game");
									}
								}
							}

							
							
							Thread.sleep(1000);
							webdriver.navigate().refresh();
							Thread.sleep(8000);
							cfnlib.closeOverlay();
							//cfnlib.newFeature();
							cfnlib.acceptAlert();
							log.info("After awaiting for free spin page" );
							cfnlib.FSSceneLoading();
							language.detailsAppend("Verify the Different Languages in Free Spin","Free Spin window should display as per respective language", "", "");
			
							//discuss>>
							Thread.sleep(8000);
							
							cfnlib.closeOverlay();
							
							language.detailsAppendFolder("Verify that the application should display Free Spin scene in " + languageCode + " ","Free Spin scene should display in " + languageDescription + " ","Free Spin scene displays in " + languageDescription + " ", "Pass", languageCode);
						
							
							
							//cfnlib.newFeature();
							//cfnlib.FSSceneLoading();
							//Thread.sleep(5000);
							log.info("Freespin continue button is about to click");
							cfnlib.FS_continue();

							log.info("Freespin continue button is clicked and awaiting for spin Button");
							Thread.sleep(5000);
							cfnlib.waitSummaryScreen();
							Thread.sleep(2000);
						//	language.detailsAppendFolder("Verify that the application should display Free Spin Summary in " + languageCode + " ","Free Spin Summary should display in " + languageDescription + " ","Free Spin Summary displays in " + languageDescription + " ", "Pass", languageCode);

							// Collects the screen shot for the summary screen
							webdriver.navigate().refresh();	
							Thread.sleep(5000);
							cfnlib.closeOverlay();
						//	cfnlib.newFeature();
							cfnlib.acceptAlert();
							
							
							language.detailsAppend("Verify the Different Languages in Free Spin Summary Screen","Free Spin Summary Screen should display as per respective language", "", "");
							printTime("before EN ");
							
							cfnlib.closeOverlay();
							cfnlib.waitSummaryScreen();

							language.detailsAppendFolder("Verify that the application should display Free Spin Summary in " + languageCode + " ","Free Spin Summary should display in " + languageDescription + " ","Free Spin Summary displays in " + languageDescription + " ", "Pass", languageCode);
							
							
							
							
							//sv65878 -RESIZE
							
							cfnlib.resizeBrowser(400,800);
							Thread.sleep(1000);
							language.detailsAppendFolder("verify the Browser Resize under FreeSpin Summary Screen ", " FreeSpin Summary Screen Browser Resize screen shot","FreeSpin Summary Screen Browser Resize screen shot ", "PASS", languageCode);
							Thread.sleep(2000);
							
							webdriver.manage().window().maximize();
							language.detailsAppendFolder("verify the Browser FullScreen under FreeSpin Summary Screen", " FreeSpin Summary Screen Browser FullScreen screen shot","FreeSpin Summary Screen Browser FullScreen screen shot ", "PASS", languageCode);
							Thread.sleep(2000);
							
							
							
							//Thread.sleep(10000);

														
							
						}
						else
						{
							log.debug("Unable to copy test data file on the environment hence skipping execution");
							language.detailsAppend("", "", "Unable to copy test data on server,hence skipping futher execution", "fail");
						}
						
						
						
						}
					
						
						
						
						
						
						
						
						
						
						
						
						
					
						
						
						
						
						
						
					

					//Language Change logic:: for updating language in URL and then Refresh 
					if (j + 1 != rowCount2){
						 rowData3 = list.get(j+1);
						String languageCode2 = rowData3.get(Constant.LANG_CODE).trim();

						String currentUrl = webdriver.getCurrentUrl();
						
						if(currentUrl.contains("LanguageCode"))
							urlNew= currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+languageCode2);
						else if(currentUrl.contains("languagecode"))
							urlNew = currentUrl.replaceAll("languagecode="+languageCode, "languagecode="+languageCode2);
						else if(currentUrl.contains("languageCode"))
							urlNew = currentUrl.replaceAll("languageCode="+languageCode, "languageCode="+languageCode2);
						
						cfnlib.loadGame(urlNew);
						
						String error=cfnlib.XpathMap.get("Error");
						if(cfnlib.isElementPresent(error))
						{
							language.detailsAppend("Verify the Language code is " +languageCode2+" ", " Application window should be displayed in " +languageDescription+"", "", "");
							language.detailsAppend("Verify that any error is coming","error must not come","error is coming", "fail");
							if (j + 2 != rowCount2){
								 rowData3 = list.get(j+2);
								String languageCode3 = rowData3.get(Constant.LANG_CODE).trim();

								currentUrl = webdriver.getCurrentUrl();
								if(currentUrl.contains("LanguageCode"))
									urlNew= currentUrl.replaceAll("LanguageCode="+languageCode2, "LanguageCode="+languageCode3);
								else if(currentUrl.contains("languagecode"))
									urlNew = currentUrl.replaceAll("languagecode="+languageCode2, "languagecode="+languageCode3);
								else if(currentUrl.contains("languageCode"))
									urlNew = currentUrl.replaceAll("languageCode="+languageCode2, "languageCode="+languageCode3);
								
								cfnlib.loadGame(urlNew);
							}
							j++;
						}
					}
				}   
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
			
			
			if(!copiedFiles.isEmpty())
			{
				if(TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
						util.deleteBluemesaTestDataFiles(mid,cid,copiedFiles);
				else
						apiobj.deleteAxiomTestDataFiles(copiedFiles);
			}
			webdriver.close();
			webdriver.quit();
			proxy.abort();
			Thread.sleep(1000);	   
		}	
	}



	public void printTime(String text)
	{
		Calendar cal = Calendar.getInstance();
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String stringDate2 = sdf.format(cal.getTime());
		System.out.println(text + stringDate2);
	}
}
