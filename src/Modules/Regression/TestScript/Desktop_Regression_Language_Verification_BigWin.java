package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

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
 * This script takes the screen shot for big win with all languages configure in language configuration sheet
 * 
 * @author AK47374
 * */

public class Desktop_Regression_Language_Verification_BigWin {
	Logger log = Logger.getLogger(Desktop_Regression_Language_Verification_BigWin.class.getName());
	public ScriptParameters scriptParameters;
		
	
	public void script() throws Exception {
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
		Desktop_HTML_Report language = new Desktop_HTML_Report(webdriver, browserName, filePath, startTime, mstrTCName,
				mstrTCDesc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings, mintStepNo, status,
				gameName);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, language, gameName);
		int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDDesktop"));

		List<String> copiedFiles=new ArrayList<>();


		CommonUtil util = new CommonUtil();
		DataBaseFunctions createplayer = new DataBaseFunctions(TestPropReader.getInstance().getProperty("ServerName"),
				TestPropReader.getInstance().getProperty("dataBaseName"),
				TestPropReader.getInstance().getProperty("serverIp"), TestPropReader.getInstance().getProperty("port"),
				TestPropReader.getInstance().getProperty("serverID"));
		
		try {
			if (webdriver != null) 
			{
				Map<String, String> rowData2 = null;
				Map<String, String> rowData3 = null;
				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				String urlNew=null;
				//Generating random username	
				userName=util.randomStringgenerator();
				String strFileName=TestPropReader.getInstance().getProperty("BigWinTestDataPath");
				File testDataFile=new File(strFileName);
				
				if(	util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles))
				{	

				String url = cfnlib.XpathMap.get("ApplicationURL");
				String launchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
				log.info("url = " +launchURl);
				System.out.println(launchURl);
				cfnlib.loadGame(launchURl);
				
				if(framework.equalsIgnoreCase("Force")){
					cfnlib.setNameSpace();
					
				}
			
				//log.debug("The New username is ==" + userName);
				//createplayer.createUser(userName, "0", 0);
				//Commented to transfer file automatically
				//destFile = new File("\\\\"+TestPropReader.getInstance().getProperty("Casinoas1IP")+"\\c$\\MGS_TestData\\"+classname + "_" + userName +".testdata");
				//destFile = new File("\\\\"+TestPropReader.getInstance().getProperty("Casinoas1IP")+"\\c$\\MGS_TestData\\"+classname + "_" + userName +".testdata");
				
				//destFile ="smb://" + TestPropReader.getInstance().getProperty("Casinoas1IP")
				//		+"/c$/MGS_TestData/"+classname + "_" + userName +".testdata";
				// ---Update the Xml File of Papyrus Data----------//
				//gcfnlib.changePlayerName(userName, xmlFilePath);
				
				// ----Copy the Papyrus Data to The CasinoAs1 Server---------//
				//gcfnlib.copyFolder(sourceFile, destFile);					
				/*String url = cfnlib.XpathMap.get("ApplicationURL");
				String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+randomUserName+"$1");
				log.info("url = " +LaunchURl);
				System.out.println("url = " +LaunchURl);
				cfnlib.loadGame(LaunchURl);*/
							
				
				/*String obj = cfnlib.func_navigate(url);
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
				
				int rowCount2 = excelpoolmanager.rowCount(testDataExcelPath, Constant.LANG_XL_SHEET_NAME);
				for(int j=1;j<rowCount2;j++){
					//Step 2: To get the languages in MAP and load the language specific url
					rowData2 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",  j);
					languageDescription = rowData2.get("Language").trim();
					languageCode = rowData2.get("Language Code").trim();
					log.info("Language Code:"+languageCode);
					language.detailsAppendFolder("Verify Game in Language " +languageCode+" ", " Application window should be displayed in " +languageDescription+"", "", "", languageCode);
				
					cfnlib.newFeature();// Added while immortal romance game.
					cfnlib.waitForSpinButton();
					cfnlib.setMaxBet();
					//--------Bigwin test
					cfnlib.spinclick();
					cfnlib.waitForSpinButton();
					boolean result=cfnlib.waitForBigWin();
					
					if(result)
					{
						language.detailsAppendFolder("Verify that big win screen is displaying with overlay  ","Big win screen must display , Once big win triggers ","On  triggering big win,Big win screen is displaying","pass",languageCode);
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
						Thread.sleep(2000);
						cfnlib.closeOverlay();
						Thread.sleep(2000);
						language.detailsAppendFolder("Verify that big win screen countup is completed ","Big win count up should be completed ","Big win count up is completed","pass",languageCode);
					}
					else
					{
						language.detailsAppendFolder("Verify that big win screen is displaying with overlay ","Big win screen must display ,Once big win triggers ","On  triggering big win,Big win screen is not displaying","fail",languageCode);

					}

					//Language Change logic:: for updating language in URL and then Refresh 
					if (j + 1 != rowCount2){
						rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",  j+1);
						String languageCode2 = rowData3.get("Language Code").toString().trim();
						log.info("priviouse languages is "+languageCode+"_"+"New Lnaguage is "+languageCode2);

						String currentUrl = webdriver.getCurrentUrl();
						if(currentUrl.contains("LanguageCode"))
							urlNew= currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+languageCode2);
						else if(currentUrl.contains("languagecode"))
							urlNew = currentUrl.replaceAll("languagecode="+languageCode, "languagecode="+languageCode2);
						else if(currentUrl.contains("languageCode"))
							urlNew = currentUrl.replaceAll("languageCode=" + languageCode,"languageCode=" + languageCode2);

						cfnlib.loadGame(urlNew);
						Thread.sleep(3000);
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
		// -------------------Closing the connections---------------//
		finally {
			language.endReport();
			language.buildAutomationSummary();
			util.deleteFile(destFile);
			/*if(destFile.delete()){
				log.debug(destFile.getName() + " is deleted!");
			}else{
				log.debug("Test Data Delete operation is failed.");
			}*/
			webdriver.close();
			webdriver.quit();
			proxy.abort();
			Thread.sleep(1000);
		}
	}
}
