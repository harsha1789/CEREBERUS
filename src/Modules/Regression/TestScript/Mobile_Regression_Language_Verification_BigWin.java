package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;

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


public class Mobile_Regression_Language_Verification_BigWin {
	Logger log = Logger.getLogger(Mobile_Regression_Language_Verification_BaseScene.class.getName());

	public ScriptParameters scriptParameters;

	public void script() throws Exception {
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
		String osPlatform=scriptParameters.getOsPlatform();

		String Status=null;
		int mintDetailCount=0;
		//int mintSubStepNo=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;

		ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();

		Mobile_HTML_Report language=new Mobile_HTML_Report(webdriver,DeviceName,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,gameName);

		MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, language, gameName);
		

		CommonUtil util = new CommonUtil();
		DataBaseFunctions createplayer = new DataBaseFunctions(TestPropReader.getInstance().getProperty("ServerName"),
				TestPropReader.getInstance().getProperty("dataBaseName"),
				TestPropReader.getInstance().getProperty("serverIp"), TestPropReader.getInstance().getProperty("port"),
				TestPropReader.getInstance().getProperty("serverID"));
		int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDMobile"));
		List<String> copiedFiles=new ArrayList<>();
		String urlNew=null;
		cfnlib.setOsPlatform(osPlatform);
		try{


			// Step 1
			if(webdriver!=null)
			{		
				Map<String, String> rowData2 = null;
				Map<String, String> rowData3 = null;
				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				//userName = gcfnlib.randomStringgenerator();
				//System.out.println("The New username is ==" + userName);
				//createplayer.createUser(userName, "0", 0);
				// -----Update the Xml File of Papyrus Data----------//
				//gcfnlib.changePlayerName(userName,  xmlFilePath);

				/*String gameNamenew=TestPropReader.getInstance().getProperty("GameName");
				String capGameName = gameNamenew.replaceAll("[a-z0-9]", "").trim();

				if(System.getProperty("Token") !=null){
					userName = "BigWin_" + capGameName.trim()+checkedOutDeviceNum;
				}

				log.info("The New username is ==" + userName);

				String url=cfnlib.xpathMap.get("ApplicationURL");
				String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
				System.out.println("url = " +LaunchURl);
				log.info("url = " +LaunchURl);
				if("LANDSCAPE".equalsIgnoreCase(cfnlib.xpathMap.get("Orientation")))
				{String context=webdriver.getContext();
				webdriver.context("NATIVE_APP");
				webdriver.rotate(ScreenOrientation.LANDSCAPE);
				webdriver.context(context);
				}
				cfnlib.loadGame(LaunchURl);
				log.debug("navigated to url ");*/

				//destFile = new File("\\\\"+TestPropReader.getInstance().getProperty("Casinoas1IP")+"\\c$\\MGS_TestData\\"+classname + "_" + userName +".testdata");


				// -----Copy the Papyrus Data to The CasinoAs1 Server---------//
				//gcfnlib.copyFolder(sourceFile, destFile);
				/*String obj = cfnlib.Func_navigate(url);
				if (obj != null) {
					language.details_append("Open Browser and Enter Lobby URL in address bar and click Enter","Island Paradise Lobby should open", "Island Paradise lobby is Open", "Pass");
				} else {
					language.details_append("Open browser and Enter Lobby URL in address bar and click Enter","Island Paradise lobby should Open", "Island paradise is not displayed", "Fail");
				}
				long loadingTime = cfnlib.Random_LoginBaseScene(userName);
				if (loadingTime < 10.0) {
					language.details_append("Check that user is able to login and Observe initial loading time","" + GameName + " Game should be launched and initial loading time should be less than 10 seconds","" + GameName + " is launched successfully and initial loading time is "+loadingTime+" Seconds", "Pass");
				} else {
					language.details_append("Check that user is able to login and Observe initial loading time","" + GameName + " Game should be launched and initial loading time should be less than 10 seconds","" + GameName + " is launched successfully and initial loading time is "+loadingTime+" Seconds", "Fail");
				}*/


				int rowCount2 = excelpoolmanager.rowCount(testDataExcelPath, "LanguageCodes");
				List<Map> list= util.readLangList();
				log.debug("Total number of Languages configured"+rowCount2);

					/*cfnlib.newFeature();
				cfnlib.func_FullScreen();*/
				//Generating random username
				userName=util.randomStringgenerator();
				String strFileName=TestPropReader.getInstance().getProperty("MobileBigWinTestDataPath");
				File testDataFile=new File(strFileName);
				
				if(	util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles))
				{
				String url = cfnlib.xpathMap.get("ApplicationURL");
				String launchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
				if("LANDSCAPE".equalsIgnoreCase(cfnlib.xpathMap.get("Orientation")))
				{webdriver.context("NATIVE_APP");
				webdriver.rotate(ScreenOrientation.LANDSCAPE);
				webdriver.context("CHROMIUM");
				}
				cfnlib.loadGame(launchURl);
				System.out.println(launchURl);

				if(framework.equalsIgnoreCase("Force")){
					cfnlib.setNameSpace();
				}

				for(int j=1;j<rowCount2;j++){
					//Step 2: To get the languages in MAP and load the language specific url
					rowData2 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",  j);
					String languageDescription = rowData2.get("Language").toString();
					String languageCode = rowData2.get("Language Code").toString().trim();					
					System.out.println("Language Code:"+languageCode);

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

					// setting maximum bet
					cfnlib.setMaxBet();
					//--------Bigwin test
					
					cfnlib.spinclick();					
					cfnlib.waitForSpinButton();
					boolean result=cfnlib.waitForbigwin();
					if(result)
					{
						language.detailsAppendFolder("Verify that big win screen is displaying with overlay ","Big win screen must display ,Once big win triggers ","On  triggering big win,Big win screen is displaying","pass",languageCode);
						System.out.println("Bigwin screenshot captured");
						// set BigWinlayers = yes if their is bigwin,superwin, megawin layers are present in the game to take 3 screenshots
						if("Yes".equalsIgnoreCase(cfnlib.xpathMap.get("BigWinlayers")))
						{
							for(int i =0;i<=2;i++)
							{
								System.out.println("wait for Bigwin");
								cfnlib.waitForbigwin();
								Thread.sleep(8000);
								System.out.println("after wait for Bigwin");
								//=========		 
								language.detailsAppendFolder("Verify that big win screen is displaying with overlay ","Big win screen must display ,Once big win triggers ","On  triggering big win,Big win screen is displaying","pass",languageCode);
								log.debug("Bigwinlayer captured" + i);
								System.out.println("Bigwinlayer captured" + i);;
							}	
						}
						//To click on overlay
						cfnlib.closeOverlay();
						Thread.sleep(2000);
						language.detailsAppendFolder("Verify that big win screen countup is completed ","Big win count up should be completed ","Big win count up is completed","pass",languageCode);

					}
					else{
						language.detailsAppendFolder("Verify that big win screen is displaying with overlay ","Big win screen must display ,Once big win triggers ","On  triggering big win,Big win screen is not displaying","fail",languageCode);

					}
					
					if (j + 1 != rowCount2){
						rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j+1);
						languageDescription = rowData3.get("Language").toString();
						String languageCode2 = rowData3.get("Language Code").toString().trim();

						String currentUrl = webdriver.getCurrentUrl();
						
						if(currentUrl.contains("LanguageCode"))
							urlNew= currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+languageCode2);
						else if(currentUrl.contains("languagecode"))
							urlNew = currentUrl.replaceAll("languagecode="+languageCode, "languagecode="+languageCode2);
						else if(currentUrl.contains("languageCode"))
							urlNew = currentUrl.replaceAll("languageCode=" + languageCode,"languageCode=" + languageCode2);

						webdriver.navigate().to(urlNew);
						Thread.sleep(3000);
						System.out.println(urlNew);
						log.debug(urlNew);
					}
				}


			}
			}


		}
		// -------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error(e.getMessage(),e);
			cfnlib.evalException(e);
		}
		finally
		{
			language.endReport();
			language.buildAutomationSummary();
			//gcfnlib.deleteFile(destFile);
			/*if(destFile.delete()){
				System.out.println(destFile.getName() + " is deleted!");

			}else{
				System.out.println("Delete operation is failed.");
			}*/
			webdriver.close();
			webdriver.quit();
			//proxy.abort();
			Thread.sleep(3000);
		}
	}
}
