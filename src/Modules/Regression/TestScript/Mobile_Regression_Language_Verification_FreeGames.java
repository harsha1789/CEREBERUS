package Modules.Regression.TestScript;
import java.io.File;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.DataBaseFunctions;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 * This script traverse and capture all the screen shots related to free games.
 * It reads the test data excel sheet for configured languages.
 * @author AK47374
 *
 */
public class Mobile_Regression_Language_Verification_FreeGames {
	public static final Logger log = Logger.getLogger(Mobile_Regression_Language_Verification_FreeGames.class.getName());
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
		int checkedOutDeviceNum=scriptParameters.getCheckedOutDeviceNum();


		String Status=null;
		int mintDetailCount=0;
		//int mintSubStepNo=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;

		ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();
		String classname= this.getClass().getSimpleName();
		String xmlFilePath= TestPropReader.getInstance().getProperty("MobileFreeGamesTestDataPath");
		File src= new File(xmlFilePath);      		
		File sourceFile = new File(xmlFilePath);
		File destFile=null;
		Mobile_HTML_Report freeGamesReport=new Mobile_HTML_Report(webdriver,DeviceName,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,gameName);
		WebDriverWait wait;
		
		MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, freeGamesReport, gameName);
		
		CommonUtil util = new CommonUtil();
		
		

		/*DataBaseFunctions createplayer = new DataBaseFunctions(TestPropReader.getInstance().getProperty("ServerName"),
				TestPropReader.getInstance().getProperty("dataBaseName"),
				TestPropReader.getInstance().getProperty("serverIp"), TestPropReader.getInstance().getProperty("port"),
				TestPropReader.getInstance().getProperty("serverID"));*/		
		
		cfnlib.setOsPlatform(scriptParameters.getOsPlatform());
		try{
			// Step 1
			if(webdriver!=null)
			{		
				Map<String, String> rowData2 = null;
				Map<String, String> rowData3 = null;
				boolean isFreeGameAssigned=false;
				String offerExpirationUtcDate=util.getCurrentTimeWithDateAndTime();
				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				int languageCnt = excelpoolmanager.rowCount(testDataExcelPath, Constant.LANG_XL_SHEET_NAME);
				//Create the random user in configured environment ie. in bluemesa or axiom
				userName=util.createRandomUser();
				log.debug("The New username is ==" + userName);
				//assign free games to above created user
				
				String strdefaultNoOfFreeGames=cfnlib.xpathMap.get("DefaultNoOfFreeGames");
				int defaultNoOfFreeGames=(int) Double.parseDouble(strdefaultNoOfFreeGames);
				String balanceTypeID=cfnlib.xpathMap.get("BalanceTypeID");
				Double dblBalanceTypeID=Double.parseDouble(balanceTypeID);

				balanceTypeID=""+dblBalanceTypeID.intValue()+"";


				//Get mid ,cidMobile ,cidDesktop from test properties 
				int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
				int cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDMobile"));
				
				
				//Assign free games offers to user depending upon the languages configured 
				if(TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
				{

					isFreeGameAssigned=cfnlib.addFreeGameToUserInBluemesa( userName,defaultNoOfFreeGames ,  offerExpirationUtcDate,  balanceTypeID,  mid, cid,languageCnt*2);
				}
				else
				{
					isFreeGameAssigned=cfnlib.addFreeGameToUserInAxiom(userName,defaultNoOfFreeGames,offerExpirationUtcDate,balanceTypeID,mid,cid,languageCnt*2);
				}
				
				if(isFreeGameAssigned) {

				/*
				if (!GameName.contains("Scratch")) {
				destFile = new File("\\\\"+TestPropReader.getInstance().getProperty("Casinoas1IP")+"\\c$\\MGS_TestData\\"+classname + "_" + userName +".testdata");
				// ---Update the Xml File of Papyrus Data----------//
				//gcfnlib.changePlayerName(userName,  xmlFilePath);

				// ----Copy the Papyrus Data to The CasinoAs1 Server---------//
				gcfnlib.copyFolder(sourceFile, destFile);
				}*/
				String urlNew = null;
				String url = cfnlib.xpathMap.get("ApplicationURL");
				String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
				System.out.println("url = " +LaunchURl);
				if("LANDSCAPE".equalsIgnoreCase(cfnlib.xpathMap.get("Orientation")))
				{webdriver.context("NATIVE_APP");
				webdriver.rotate(ScreenOrientation.LANDSCAPE);
				webdriver.context("CHROMIUM");
				}
				cfnlib.loadGame(LaunchURl);

				Thread.sleep(3000);


				/*String obj = cfnlib.Func_navigate(url);
				if (obj != null) {
					Tc01.details_append("Open Browser and Enter Lobby URL in address bar and click Enter",
							"Island Paradise Lobby should open", "Island Paradise lobby is Open", "Pass");
				} else {
					Tc01.details_append("Open browser and Enter Lobby URL in address bar and click Enter",
							"Island Paradise lobby should Open", "Island paradise is not displayed", "Fail");
				}

				long loadingTime = cfnlib.Random_LoginBaseScene(userName);
				if(Framework.equalsIgnoreCase("Force")){
					cfnlib.setNameSpace();
					}
				if (loadingTime < 10.0) {
					Tc01.details_append("Check that user is able to login and Observe initial loading time","" + GameName + " Game should be launched and initial loading time should be less than 10 seconds","" + GameName + " is launched successfully and initial loading time is "+loadingTime+" Seconds", "Pass");
				} else {
					Tc01.details_append("Check that user is able to login and Observe initial loading time","" + GameName + " Game should be launched and initial loading time should be less than 10 seconds","" + GameName + " is launched successfully and initial loading time is "+loadingTime+" Seconds", "Fail");
				}	
				 */
				/*	cfnlib.newFeature();
				cfnlib.func_FullScreen();*/

				if(framework.equalsIgnoreCase("Force")){
					cfnlib.setNameSpace();
				}
				int rowCount2 = excelpoolmanager.rowCount(testDataExcelPath, "LanguageCodes");
				for(int j=1;j<rowCount2;j++){
					//Step 2: To get the languages in MAP and load the language specific url
					rowData2 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",  j);
					String languageDescription = rowData2.get("Language").toString();
					String languageCode = rowData2.get("Language Code").toString().trim();

					freeGamesReport.detailsAppendFolder("Verify Free Games Scenes in "+languageDescription+" Language","Free Games Scenes should display in "+languageDescription+" language", "", "", languageCode);

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
					if(j!=1)
				    cfnlib.freeGamesContinueExpiry();
					Thread.sleep(3000);

					//Capture Free Game Entry Screen
					boolean isFGAssign = cfnlib.freeGamesEntryScreen();
					if(isFGAssign)
						freeGamesReport.detailsAppendFolder("Check that free games entry screen is displaying", "Free Games entry screen should display","Free Game entry screen is displaying", "Pass", languageCode);
					else
						freeGamesReport.detailsAppendFolder("Check that free games entry screen is displaying", "Free Games entry screen should display","Free Game entry screen is not displaying", "Fail", languageCode);

					boolean b = cfnlib.freeGameEntryInfo();
					if(b)
						freeGamesReport.detailsAppendFolder("Check that free games entry screen is displaying with free games details", "Free Games entry screen should display with free games details","Free Game entry screen is displaying with free games details", "Pass", languageCode);
					else
						freeGamesReport.detailsAppendFolder("Check that free games entry screen is displaying with free games details", "Free Games entry screen should display with free games details","Free Game entry screen is not displaying with free games details", "Fail", languageCode);

					boolean b1 = cfnlib.clickPlayNow();
					if(b1)
						freeGamesReport.detailsAppendFolder("Check that Base Scene in free games is displaying", "Base Scene in Free Games should display", "Base Scene in Free Games is displaying with free games details", "Pass", languageCode);
					else
						freeGamesReport.detailsAppendFolder("Check that Base Scene in free games is displaying", "Base Scene in Free Games should display", "Base Scene in Free Games is not displaying with free games details", "Fail", languageCode);

					cfnlib.funcFullScreen();
					cfnlib.spinclick();
					Thread.sleep(10000);
					
					if(TestPropReader.getInstance().getProperty("IsProgressiveGame").equalsIgnoreCase("Yes"))
					{
						cfnlib.verifyJackPotBonuswithScreenShots(freeGamesReport, languageCode);
					}
					wait=new WebDriverWait(webdriver,60);
					webdriver.navigate().refresh();
					if(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame"))||(Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad"))))
					{
						wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cfnlib.xpathMap.get("OneDesign_NewFeature_ClickToContinue"))));
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
					//Free Games resume Screen
					//cfnlib.newFeature();
					String s1 = cfnlib.freeGamesResumescreen();
					if(s1!=null)
						freeGamesReport.detailsAppendFolder("Check that free games resume screen is displaying", "Free Games resume screen should display","Free Game resume screen is displaying", "Pass", languageCode);
					else
						freeGamesReport.detailsAppendFolder("Check that free games resume screen is displaying", "Free Games resume screen should display","Free Game resume screen is not displaying", "Fail", languageCode);

					boolean b2 = cfnlib.freeGameResumeInfo();
					if(b2)
						freeGamesReport.detailsAppendFolder("Check that free games entry screen is displaying with free games details", "Free Games entry screen should display with free games details","Free Game entry screen is displaying with free games details", "Pass", languageCode);
					else
						freeGamesReport.detailsAppendFolder("Check that free games entry screen is displaying with free games details", "Free Games entry screen should display with free games details","Free Game entry screen is not displaying with free games details", "Fail", languageCode);

					boolean b3 = cfnlib.resumeScreenDiscardClick();
					if(b3)
						freeGamesReport.detailsAppendFolder("Check that Discard Offer screen is displaying", "Discard Offer screen should display", "Discard Offer screen is displaying", "Pass", languageCode);
					else
						freeGamesReport.detailsAppendFolder("Check that Discard Offer screen is displaying", "Discard Offer screen should display", "Discard Offer screen is not displaying", "Fail", languageCode);

					boolean b4 = cfnlib.confirmDiscardOffer();
					if(b4)
						freeGamesReport.detailsAppendFolder("Check that Offer is discarded successfully and Free Games Summary Screen should display", "Offer should be discarded successfully and Free Games Summary Screen should display", "Offer is discarded and Free Games Summary Screen is displaying", "Pass", languageCode);
					else
						freeGamesReport.detailsAppendFolder("Check that Offer is discarded successfully and Free Games Summary Screen should display", "Offer should be discarded successfully and Free Games Summary Screen should display", "Offer is not discarded and Free Games Summary Screen is not displaying", "Fail", languageCode);

					cfnlib.clickNextOffer();
					cfnlib.clickPlayNow();

					cfnlib.funcFullScreen();
					cfnlib.spinclick();
					Thread.sleep(10000);
					//Tc01.details_append_folder("Check Base Scene in free games after Spin", "Win in Base Scene in Free Games should display if there is any win", "Base Scene in Free Games is displaying with win if win occurs", "Pass", languageCode);

					cfnlib.clickBaseSceneDiscard();

					boolean b6 = cfnlib.confirmDiscardOffer();
					if(b6)
						freeGamesReport.detailsAppendFolder("Check that Offer is discarded successfully and Free Games Summary Screen should display", "Offer should be discarded successfully and Free Games Summary Screen should display", "Offer is discarded and Free Games Summary Screen is displaying", "Pass", languageCode);
					else
						freeGamesReport.detailsAppendFolder("Check that Offer is discarded successfully and Free Games Summary Screen should display", "Offer should be discarded successfully and Free Games Summary Screen should display", "Offer is not discarded and Free Games Summary Screen is not displaying", "Fail", languageCode);

					webdriver.navigate().refresh();
					if(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame"))||(Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad"))))
					{
						wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cfnlib.xpathMap.get("OneDesign_NewFeature_ClickToContinue"))));
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

					//cfnlib.newFeature();
					boolean b5 = cfnlib.freeGamesExpriyScreen();
					if(b5)
						freeGamesReport.detailsAppendFolder("Check that Free Game Session Expire Screen is displaying", "Free Game Session Expire Screen should display", "Free Game Session Expire Screen is displaying", "Pass", languageCode);
					else
						freeGamesReport.detailsAppendFolder("Check that Free Game Session Expire Screen is displaying", "Free Game Session Expire Screen should display", "Free Game Session Expire Screen is not displaying", "Fail", languageCode);

					if (j + 1 != rowCount2){
						rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j+1);
						languageDescription = rowData3.get("Language").toString();
						String languageCode2 = rowData3.get("Language Code").toString().trim();

						String currentUrl = webdriver.getCurrentUrl();
					
						if(currentUrl.contains("LanguageCode"))
							urlNew= currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+languageCode2);
						else if(currentUrl.contains("languagecode"))
							urlNew = currentUrl.replaceAll("languagecode="+languageCode, "languagecode="+languageCode2);

						cfnlib.loadGame(urlNew);
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
						String error=cfnlib.xpathMap.get("Error");
						if(cfnlib.isElementPresent(error))
						{
							freeGamesReport.detailsAppendFolder("Verify the Language code is " +languageCode2+" ", " Application window should be displayed in " +languageDescription+"", "", "", languageCode2);
							freeGamesReport.detailsAppendFolder("Verify that any error is coming","error must not come","error is coming", "Fail", languageCode2);
							if (j + 2 != rowCount2){
								rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",  j+2);
								String languageCode3 = rowData3.get("Language Code").toString().trim();

								currentUrl = webdriver.getCurrentUrl();
								if(currentUrl.contains("LanguageCode"))
									urlNew= currentUrl.replaceAll("LanguageCode="+languageCode2, "LanguageCode="+languageCode3);
								else if(currentUrl.contains("languagecode"))
									urlNew = currentUrl.replaceAll("languagecode="+languageCode2, "languagecode="+languageCode3);

								cfnlib.loadGame(urlNew);
								cfnlib.newFeature();// Added while immortal romance game.
							}
							j++;
						}

					}
				}
				}else {
					log.error("Skipping the execution as free games assignment failed");
				}
			}
		}
		//-------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error(e.getMessage(),e);
			cfnlib.evalException(e);
		}
		finally
		{
			freeGamesReport.endReport();
			if(destFile.delete()){
				System.out.println(destFile.getName() + " is deleted!");
			}else{
				System.out.println("Delete operation is failed.");
			}
			webdriver.close();
			webdriver.quit();
			proxy.abort();
			Thread.sleep(3000);
		}	
	}
}