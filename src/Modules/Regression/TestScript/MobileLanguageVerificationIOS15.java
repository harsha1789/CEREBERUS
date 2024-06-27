package Modules.Regression.TestScript;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 *This script is sanity check for IOs 15 and above version
 *@author sg56207
 */

public class  MobileLanguageVerificationIOS15 {
	Logger log = Logger.getLogger(MobileLanguageVerificationIOS15.class.getName());

	public ScriptParameters scriptParameters;

	public void script() throws Exception{


		String mstrTCName=scriptParameters.getMstrTCName();
		String mstrTCDesc=scriptParameters.getMstrTCDesc();
		String mstrModuleName=scriptParameters.getMstrModuleName();
		BrowserMobProxyServer proxy=scriptParameters.getProxy();
		String startTime=scriptParameters.getStartTime();
		String filePath=scriptParameters.getFilePath();
		AppiumDriver<WebElement> webdriver=scriptParameters.getAppiumWebdriver();
		String deviceName=scriptParameters.getDeviceName();
		String userName=scriptParameters.getUserName();
		String framework=scriptParameters.getFramework();
		String gameName=scriptParameters.getGameName();


		String status=null;
		int mintDetailCount=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		String urlNew=null;
		ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();
		Mobile_HTML_Report language=new Mobile_HTML_Report(webdriver,deviceName,filePath,startTime,mstrTCName,mstrTCDesc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,status,gameName);

		log.info("Framework"+framework);
		MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, language, gameName);

		CommonUtil util = new CommonUtil();
		cfnlib.setOsPlatform(scriptParameters.getOsPlatform());
		int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int  cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDMobile"));
		List<String> copiedFiles=new ArrayList<>();
		RestAPILibrary apiobj=new RestAPILibrary();

		try{
			// Step 1
			if(webdriver!=null)
			{		
				Map<String, String> rowData2 = null;
				Map<String, String> rowData3 = null;
				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				int languageCnt = excelpoolmanager.rowCount(testDataExcelPath, Constant.LANG_XL_SHEET_NAME);

				String url = cfnlib.xpathMap.get("ApplicationURL");
				String offerExpirationUtcDate=util.getCurrentTimeWithDateAndTime();
				//creating random player
				String strFileName=TestPropReader.getInstance().getProperty("MobileFreeSpinsTestDataPath");
				File testDataFile=new File(strFileName);

				userName=util.randomStringgenerator();
				if(	util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles))
				{
					log.debug("Test dat is copy in test Server for Username="+userName);



					String launchURL = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
					if("LANDSCAPE".equalsIgnoreCase(cfnlib.xpathMap.get("Orientation")))
					{
						String context=webdriver.getContext();
						webdriver.context("NATIVE_APP");
						webdriver.rotate(ScreenOrientation.LANDSCAPE);
						webdriver.context(context);
					}
					cfnlib.loadGame(launchURL);
					log.debug("Url Loaded= "+launchURL);

					if(framework.equalsIgnoreCase("Force")){
						cfnlib.setNameSpace();
					}

					int rowCount2 = excelpoolmanager.rowCount(testDataExcelPath, "LanguageCodes");
					for(int j=1;j<rowCount2;j++){
						//Step 2: To get the languages in MAP and load the language specific url
						rowData2 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",  j);
						String languageDescription = rowData2.get("Language").toString();
						String languageCode = rowData2.get("Language Code").toString().trim();
						language.detailsAppend("Verify the Language code is " +languageCode+" ", " Application window should be displayed in " +languageDescription+"", "", "");

						//Splash screen verification
						if(cfnlib.splashScreen(language,languageCode))					
						{
							log.debug("splashScreen capture");
							//new feature and continue button verification 
							if(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame"))||(Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad"))))
							{
								cfnlib.newFeature();
								language.detailsAppendFolder(" Verify that the application should display new feature baner or continue button on game load", " New feature baner or continue button on game load", " New feature baner or continue button on game load", "Pass", languageCode);
								cfnlib.funcFullScreen();
								Thread.sleep(1000);
								
							}
							else
							{
								language.detailsAppendFolder(" Verify that the application should display new feature baner or continue button on game load", " New feature baner or continue button on game load", " New feature baner or continue button on game load", "Pass", languageCode);
								cfnlib.funcFullScreen();
								Thread.sleep(2000);
							cfnlib.newFeature();

							}
							if(j!=1)
								{
								cfnlib.freeGamesContinueExpiry();
								cfnlib.clickAtButton("return "+cfnlib.xpathMap.get("FGCompleteGameCenterBtn"));;
								}

							cfnlib.waitForSpinButton();
							language.detailsAppendFolder("Base Scene before enabling address bar", " Basescene  should be displayed in " +languageDescription+"", "Basescene  should be displayed in " +languageDescription+",verify screenshot", "Pass", languageCode);
							cfnlib.clickOnAddressBar();
							cfnlib.threadSleep(1000);
							language.detailsAppendFolder("Base Scene after disabling address bar", " Basescene  should be displayed in " +languageDescription+"", "Basescene  should be displayed in " +languageDescription+",verify screenshot", "Pass", languageCode);

							cfnlib.clickOnAddressBar();
							cfnlib.threadSleep(1000);

							//Capture Screen shot for Bet Screen
							if(cfnlib.openTotalBet())
							{
								language.detailsAppendFolder("Bet overlay before enabling address bar", "Bet Settings Screen should display", "Bet Settings Screen displays in " +languageDescription+ " language,verify the screenshot", "Pass", languageCode);
								cfnlib.clickOnAddressBar();
								cfnlib.threadSleep(1000);
								language.detailsAppendFolder(" Bet Settings Screen after disabling address bar", "Bet Settings Screen should display", "Bet Settings Screen displays in " +languageDescription+ " language,verify the screenshot", "Pass", languageCode);
								cfnlib.closeTotalBet();
								log.debug("Done for bet");
							}else
							{
								language.detailsAppendFolder("Verify that language on the Bet Settings Screen", "Bet Settings Screen should display", "Bet Settings Screen displays in " +languageDescription+ " language", "Fail", languageCode);
								log.debug("fail to check bet panel");
							}

							//Autoplay panel verification
							boolean openAutoplay = cfnlib.open_Autoplay();
							if (openAutoplay){
								log.debug("Autoplay  open");
								language.detailsAppendFolder("Autoplay Screen before addressbar enabling", "Autoplay Screen should be display", "Autoplay Screen should be displayed,verify screensht", "Pass", languageCode);
								cfnlib.clickOnAddressBar();
								cfnlib.threadSleep(1000);
								language.detailsAppendFolder("Autoplay screen after disabling addressbar", "Autoplay Screen should be display", "Autoplay Screen should be displayed,verify screenshot", "Pass", languageCode);
								cfnlib.close_Autoplay();
								log.debug("Done for Autoplay");

							}
							else{
								
								language.detailsAppendFolder("Verify language on the Autoplay Screen", "Autoplay Screen should be display", "Autoplay Screen doesn't display", "Fail", languageCode);   
							}
							//menu verification
							if(!framework.equalsIgnoreCase("CS_Renovate")){
								boolean menuOpen = cfnlib.menuOpen();
								if(menuOpen){
									language.detailsAppendFolder("Menu overlay before enabling addressbar ", "Language of Menu Links should be " +languageDescription+ "", "Menu Links are displaying in " +languageDescription+ " language,verify screenshot", "pass", languageCode);
									cfnlib.clickOnAddressBar();
									cfnlib.threadSleep(1000);
									language.detailsAppendFolder(" Menu overlay after disabling addressbar", "Language of Menu Links should be " +languageDescription+ "", "Menu Links are displaying in " +languageDescription+ " language,verify screenshot", "pass", languageCode);
								}
								else {
									language.detailsAppendFolder("Verify that Language" + languageDescription + "should be display properly on menu links", "Language inside Menu Links should be in " +languageDescription+ " language", "Language inside Menu Links is displaying", "fail", languageCode);
								}
								cfnlib.menuClose();
								cfnlib.clickOnAddressBar();
								cfnlib.threadSleep(1000);
								log.debug("Done for Menu");
							}
							//Setting verification
							if(!framework.equalsIgnoreCase("CS")&&cfnlib.checkAvilability(cfnlib.xpathMap.get("isMenuSettingsBtnVisible"))){
								boolean openSetting= cfnlib.settingsOpen();
								if(openSetting){
									Thread.sleep(1000);
									language.detailsAppendFolder("settings screen before enabling address bar ", "Language in Settigns Screen should be " +languageDescription+ " ", "Language inside Settings screens is " +languageDescription+ " ", "pass", languageCode);
									cfnlib.clickOnAddressBar();
									cfnlib.threadSleep(1000);
									language.detailsAppendFolder("Setting screen after disabling addressbar ", "Language in Settigns Screen should be " +languageDescription+ " ", "Language inside Settings screens is " +languageDescription+ " ", "pass", languageCode);

								}
								else {
									language.detailsAppendFolder("Verify that Language back on menu is " + languageDescription +"should be display properly on menu links", "Language inside Menu Links should be in " +languageDescription+ " language", "Language inside Menu Links is displaying", "fail", languageCode);
								}
								cfnlib.settingsBack();
								cfnlib.clickOnAddressBar();
								cfnlib.threadSleep(1000);
								log.debug("Done for Setting");
							}

							//paytable verification
							if(!gameName.contains("Scratch") && cfnlib.checkAvilability(cfnlib.xpathMap.get("isMenuPaytableBtnVisible"))){
								//Open payatable and capture screen shots
								cfnlib.capturePaytableScreenshot(language, languageCode);
								if(!framework.equalsIgnoreCase("CS"))
								{
									language.detailsAppendFolder("paytable before enabling addressbar", "Language in paytable Screen should be " +languageDescription+ " ", "Language inside paytable screens is " +languageDescription+ " ", "pass", languageCode);

									cfnlib.clickOnAddressBar();
									cfnlib.threadSleep(1000);
									language.detailsAppendFolder("paytable after disabling addressbar", "Language in paytable Screen should be " +languageDescription+ " ", "Language inside paytable screens is " +languageDescription+ " ", "pass", languageCode);

									cfnlib.paytableClose();
								}
								log.debug("Done for paytable");
							}

							// To open and capture Story in Game (Applicable for the game like immortal romances)
							if(cfnlib.checkAvilability(cfnlib.xpathMap.get("isPaytableStoryExists"))){
								cfnlib.Verifystoryoptioninpaytable(language, languageCode);
								cfnlib.paytableClose();
								log.debug("Done for story in paytable");
							}
							//Base Game - During Game Play
							cfnlib.spinclick();
							language.detailsAppendFolder("Verify Base scene during game play"," ","verify screenshot ","pass",languageCode);
							cfnlib.waitForSpinButtonstop();
							Thread.sleep(2000);
							//Base Game - Regular Win
							String currentBet = cfnlib.getCurrentBet();
							cfnlib.validateCreditForWinLoss(currentBet,language,languageCode);
							Thread.sleep(3000);
							//Base Game - Big Win
							cfnlib.spinclick();
							cfnlib.waitForSpinButton();
							boolean result=cfnlib.waitForbigwin();
							if(result)
							{
								log.debug("Bigwin display in scene");
								language.detailsAppendFolder("Verify that big win scean is displaying with overlay ","Big win screen must display ,Once big win triggers ","On  triggering big win,Big win screen is displaying","pass",languageCode);

								// set BigWinlayers = yes if their is bigwin,superwin, megawin layers are present in the game to take 3 screenshots
								if("Yes".equalsIgnoreCase(cfnlib.xpathMap.get("BigWinlayers")))
								{
									for(int i =0;i<=2;i++)
									{
										cfnlib.waitForbigwin();
										language.detailsAppendFolder("Verify that big win screen is displaying with overlay ","Big win screen must display ,Once big win triggers ","On  triggering big win,Big win screen is displaying","pass",languageCode);
										log.debug("Bigwinlayer captured" + i);
									}	
								}
							}
							else{
								language.detailsAppendFolder("Verify that big win screen is displaying with overlay ","Big win screen must display ,Once big win triggers ","On  triggering big win,Big win screen is not displaying","fail",languageCode);

							}
							cfnlib.waitForSpinButtonstop();

							//verify Game Feature – Free spin 

							cfnlib.spinclick();
							if( TestPropReader.getInstance().getProperty("IsFreeSpinAvailable").equalsIgnoreCase("yes"))
							{
								
								Thread.sleep(3000);
								cfnlib.waitForWinDisplay();
								String freeSpinEntryScreen = cfnlib.xpathMap.get("FreeSpinEntryScreen");
								String str = cfnlib.entryScreen_Wait(freeSpinEntryScreen);
								if (str.equalsIgnoreCase("freeSpin")) {
									log.debug("Inside free spin");
									cfnlib.refresh();
									cfnlib.entryScreen_Wait(freeSpinEntryScreen);
									language.detailsAppendFolder("Verify free entry screen before enabling address bar ","Free spin entry screen should display in " +languageDescription+" ","Free spin entry screen displays in " +languageDescription+ " ", "Pass", languageCode); 
									cfnlib.clickOnAddressBar();
									language.detailsAppendFolder("Verify free entry screen after disabling  ","Free spin entry screen should display in " +languageDescription+" ","Free spin entry screen displays in " +languageDescription+ " ", "Pass", languageCode); 

									if("yes".equals(cfnlib.xpathMap.get("isFreeSpinSelectionAvailable")))
									{
										cfnlib.clickBonusSelection(1);
									}
									else 
									{
										if(cfnlib.getConsoleBooleanText("return "+cfnlib.xpathMap.get("IsFS_EntryContinueBtnVisible")))
										{
											cfnlib.clickToContinue();
										
										}else{
										cfnlib.threadSleep(2000);
										cfnlib.FS_continue();
										}
									}
								Thread.sleep(3000);
								cfnlib.FSSceneLoading();
								language.detailsAppendFolder("Free spin scene before enabling address bar","Free spin scene  should display in " +languageDescription+" ","Free spin scenedisplays in " +languageDescription+ " ", "Pass", languageCode); 
								cfnlib.clickOnAddressBar();
								cfnlib.threadSleep(1000);
								language.detailsAppendFolder("Free spin scene in after disabling address bar" ,"Free spin scene  should display in " +languageDescription+" ","Free spin scenedisplays in " +languageDescription+ " ", "Pass", languageCode); 
								if(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame")))
								{
									cfnlib.clickOnAddressBar();
									cfnlib.threadSleep(1000);
								}
								log.debug("waiting for freespins summary");
								cfnlib.waitSummaryScreen();
								language.detailsAppendFolder("Free spin summary before enabling address bar","Free spin summary  should display in " +languageDescription+" ","Free spin summary displays in " +languageDescription+ " ", "Pass", languageCode); 
								cfnlib.clickOnAddressBar();
								cfnlib.threadSleep(1000);
								language.detailsAppendFolder(" Free spin summary  after disabling address bar " ,"Free spin summary  should display in " +languageDescription+" ","Free spin summary displays in " +languageDescription+ " ", "Pass", languageCode); 

								}
								else{
									log.debug("Free Spin Entry screen is not present in Game");
									language.detailsAppendFolder("Verify that the application should display Free spin entry screen in " +languageCode+" ","Free spin entry screen should display in " +languageDescription+" ","Free spin entry screen not displays in " +languageDescription+ " ", "Fail", languageCode); 

								}
							}else
							{
								language.detailsAppendNoScreenshot("Verify free spin feature "," ","No free spin feature available in the game","pass");

							}
							//game feature -Free games
							//assign free games to above created user
							cfnlib.waitForSpinButton();
							cfnlib.waitForSpinButtonstop();
							String strdefaultNoOfFreeGames=cfnlib.xpathMap.get("DefaultNoOfFreeGames");
							int defaultNoOfFreeGames=(int) Double.parseDouble(strdefaultNoOfFreeGames);
							String balanceTypeID=cfnlib.xpathMap.get("BalanceTypeID");
							Double dblBalanceTypeID=Double.parseDouble(balanceTypeID);

							balanceTypeID=""+dblBalanceTypeID.intValue()+"";

							//Get mid ,cidMobile ,cidDesktop from test properties 
							boolean isFreeGameAssigned=false;

							//Assign free games offers to user depending upon the languages configured 
							if(TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
							{

								isFreeGameAssigned=cfnlib.addFreeGameToUserInBluemesa( userName,defaultNoOfFreeGames ,  offerExpirationUtcDate,  balanceTypeID,  mid, cid,4);
							}
							else
							{
								isFreeGameAssigned=cfnlib.addFreeGameToUserInAxiom(userName,defaultNoOfFreeGames,offerExpirationUtcDate,balanceTypeID,mid,cid,4);
							}

							if(isFreeGameAssigned) {
								log.debug("Freegames Assigned to user");
								cfnlib.refresh();
								Thread.sleep(4000);
								boolean isFGAssign = cfnlib.freeGamesEntryScreen();
								if(isFGAssign)
								{	language.detailsAppendFolder("Free games entry screen before enabling address bar", "Free Games entry screen should display","Free Game entry screen is displaying", "Pass", languageCode);
									cfnlib.clickOnAddressBar();
									cfnlib.threadSleep(1000);
									language.detailsAppendFolder("free games entry screen after disabling address bar", "Free Games entry screen should display","Free Game entry screen is displaying", "Pass", languageCode);
								}
								else
									language.detailsAppendFolder("Check that free games entry screen is displaying", "Free Games entry screen should display","Free Game entry screen is not displaying", "Fail", languageCode);
								cfnlib.clickPlayNow();

								cfnlib.funcFullScreen();
								cfnlib.waitForSpinButton();
								cfnlib.spinclick();
								Thread.sleep(1000);
								language.detailsAppendFolder("Check Base Scene in free games before enabling address bar", "Win in Base Scene in Free Games should display if there is any win", "Base Scene in Free Games is displaying with win if win occurs", "Pass", languageCode);
								cfnlib.clickOnAddressBar();
								cfnlib.threadSleep(1000);
								language.detailsAppendFolder("Check Base Scene in free games after disabling address bar", "Win in Base Scene in Free Games should display if there is any win", "Base Scene in Free Games is displaying with win if win occurs", "Pass", languageCode);

								cfnlib.clickBaseSceneDiscard();

								boolean b6 = cfnlib.confirmDiscardOffer();
								if(b6)
								{	language.detailsAppendFolder("Check that Offer is discarded successfully and Free Games Summary Screen should display before enabling address bar", "Offer should be discarded successfully and Free Games Summary Screen should display", "Offer is discarded and Free Games Summary Screen is displaying", "Pass", languageCode);
									cfnlib.clickOnAddressBar();
									cfnlib.threadSleep(1000);
									language.detailsAppendFolder("Check that Offer is discarded successfully and Free Games Summary Screen should display after disabling address bar", "Offer should be discarded successfully and Free Games Summary Screen should display", "Offer is discarded and Free Games Summary Screen is displaying", "Pass", languageCode);
									
								}else
									language.detailsAppendFolder("Check that Offer is discarded successfully and Free Games Summary Screen should display", "Offer should be discarded successfully and Free Games Summary Screen should display", "Offer is not discarded and Free Games Summary Screen is not displaying", "Fail", languageCode);

								cfnlib.refresh();
								Thread.sleep(2000);
								boolean b5 = cfnlib.freeGamesExpriyScreen();
								if(b5)
								{	language.detailsAppendFolder("Check that Free Game Session Expire Screen is displaying before enabling address bar", "Free Game Session Expire Screen should display", "Free Game Session Expire Screen is displaying", "Pass", languageCode);
									cfnlib.clickOnAddressBar();
									cfnlib.threadSleep(1000);
									language.detailsAppendFolder("Check that Free Game Session Expire Screen is displaying after disabling address bar", "Free Game Session Expire Screen should display", "Free Game Session Expire Screen is displaying", "Pass", languageCode);
								}
								else
									language.detailsAppendFolder("Check that Free Game Session Expire Screen is displaying", "Free Game Session Expire Screen should display", "Free Game Session Expire Screen is not displaying", "Fail", languageCode);

							
							if (j + 1 != rowCount2){
								rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j+1);
								languageDescription = rowData3.get("Language").trim();
								String languageCode2 = rowData3.get("Language Code").trim();

								String currentUrl = webdriver.getCurrentUrl();
								if(currentUrl.contains("LanguageCode"))
									urlNew= currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+languageCode2);
								else if(currentUrl.contains("languagecode"))
									urlNew = currentUrl.replaceAll("languagecode="+languageCode, "languagecode="+languageCode2);


								log.info("Privious Language Code in Url= "+languageCode+"\nNext Language code= "+languageCode2+"\nNew Url after replacing language code:"+urlNew);
								cfnlib.loadGame(urlNew);
								String error=cfnlib.xpathMap.get("Error");

								if(cfnlib.isElementPresent(error))
								{
									language.detailsAppendFolder("Verify the Language code is " +languageCode2+" ", " Application window should be displayed in " +languageDescription+"", "", "", languageCode2);
									language.detailsAppendFolder("Verify that any error is coming","General error should not display","General Error is Diplay", "fail", languageCode2);

									if (j + 2 != rowCount2){
										rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j+2);
										String languageCode3 = rowData3.get("Language Code").toString().trim();
										currentUrl = webdriver.getCurrentUrl();
										if(currentUrl.contains("LanguageCode"))
											urlNew= currentUrl.replaceAll("LanguageCode="+languageCode2, "LanguageCode="+languageCode3);
										else if(currentUrl.contains("languagecode"))
											urlNew = currentUrl.replaceAll("languagecode="+languageCode2, "languagecode="+languageCode3);

										cfnlib.loadGame(urlNew);
									}

									j++;
								}
							}
						}
						else
						{
							log.debug("Fail to assign free games");
							language.detailsAppendFolder("Verify free game feature "," ","fail to assign free game to user","fail",languageCode);

						}
					}else{
							language.detailsAppendFolder(" Verify that the application should display with Game Logo and game name preloader image", "Game logo and game name should display on preloader image", "Game logo and game name not display on preloader image", "Fail", languageCode);

						}
					}
				}else
				{
					language.detailsAppend("unable to copy test dat to server ", " ", "", "");

				}
			}
		}



		//-------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error(e.getMessage(),e);
		}
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
			//proxy.abort();
			Thread.sleep(3000);
		}	
	}
}