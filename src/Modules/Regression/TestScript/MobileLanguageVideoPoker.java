package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;

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

/*@author :sg56207
 * This script require wining player test data to check the scenario correctly
	1. Test data creation and user creation through script 
 * Description: This script collec the screen shots for all configure languages in language xls sheet
 * Test data require: This script require a testdata with win values 
 * 
 * */
public class MobileLanguageVideoPoker {


	Logger log = Logger.getLogger(MobileLanguageVideoPoker.class.getName());
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
		String frameWork=scriptParameters.getFramework();
		String gameName=scriptParameters.getGameName();
		//int checkedOutDeviceNum=scriptParameters.getCheckedOutDeviceNum();


		String status=null;
		int mintDetailCount=0;
		//int mintSubStepNo=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		String languageDescription=null;
		String languageCode=null;

		ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();

		Mobile_HTML_Report languageReport=new Mobile_HTML_Report(webdriver,deviceName,filePath,startTime,mstrTCName,mstrTCDesc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,status,gameName);

		log.info("Report object created");
		log.info("Framework="+frameWork);
		MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(frameWork, webdriver, proxy, languageReport, gameName);
		
		cfnlib.setOsPlatform(scriptParameters.getOsPlatform());
		CommonUtil util = new CommonUtil();
		Map<String, String> rowData2 = null;
		Map<String, String> rowData3 = null;


		try{
			int envid ;
			String lobbyName;
			RestAPILibrary apiObj = new RestAPILibrary();
			String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");




			// Step 1 
			if(webdriver!=null)
			{	


				if( TestPropReader.getInstance().getProperty("EnvironmentName")==null ||
						TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
					{
						lobbyName="bluemesa";
						TestPropReader.getInstance().setProperty("EnvironmentName","bluemesa");	
						envid=Integer.parseInt(TestPropReader.getInstance().getProperty("EnvironmentID"));
					}
						if(TestPropReader.getInstance().getProperty("EnvironmentName")!=null)
					{
							lobbyName=TestPropReader.getInstance().getProperty("EnvironmentName").toLowerCase();
					}
					

				userName=util.randomStringgenerator();

				//Implement code for test data copy depdeding on env
				int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MId"));
				int cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDMobile"));
				String strFileName=TestPropReader.getInstance().getProperty("MobileSanityTestDataPath");
				File testDataFile=new File(strFileName);
				List<String> copiedFiles=new ArrayList<>();
				if(	util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles))
					log.debug("Test dat is copy in test Server for Username="+userName);


				String url = cfnlib.xpathMap.get("ApplicationURL");
				String launchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
				log.info("url = " +launchURl);
				if("LANDSCAPE".equalsIgnoreCase(cfnlib.xpathMap.get("Orientation")))
				{webdriver.context("NATIVE_APP");
				webdriver.rotate(ScreenOrientation.LANDSCAPE);
				webdriver.context("CHROMIUM");
				}

				languageReport.detailAppend("Language Suite Test cases", "Language Suite Test cases", "", "","");


				if(	cfnlib.loadGame(launchURl))
				{
					List<Map> list= util.readLangList();

					int rowCount2 = excelpoolmanager.rowCount(testDataExcelPath, Constant.LANG_XL_SHEET_NAME);
					for(int j=1;j<rowCount2;j++){
						//Step 2: To get the languages in MAP and load the language specific url
						rowData2 = list.get(j);
						languageDescription = rowData2.get(Constant.LANGUAGE).trim();
						languageCode = rowData2.get(Constant.LANG_CODE).trim();

						languageReport.detailsAppend("Verify the Language code is " +languageCode+" ", " Application window should be displayed in " +languageDescription+"", "", "");


						//******************** Splash screen screen shot
					//	cfnlib.splashScreen(languageReport,languageCode);	
						languageReport.detailsAppendFolder(" Verify that the application should display with Game Logo and game name in Base Scene", "Game logo and game name with base scene should display", "Game logo and game name with base scene displays", "Pass", languageCode);
						Thread.sleep(1000);
						if(frameWork.equalsIgnoreCase("Force")){
							cfnlib.setNameSpace();
						}

						if(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame"))||(Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad"))))
						{
							cfnlib.newFeature();
							Thread.sleep(1000);
							cfnlib.funcFullScreen();
						}
						else
						{
							//to remove hand gesture
							cfnlib.funcFullScreen();
							Thread.sleep(1000);
							cfnlib.newFeature();
						}
						if(cfnlib.openTotalBetBoolean())
						{
							//report and scrren shot
							languageReport.detailsAppendFolder("Verify bet setting Screen", "On Clicking bet button ,Bet Setting overlay should display", "Bet Setting overlay is display", "Pass",languageCode);
							cfnlib.funcLandscape();
							languageReport.detailsAppendFolder("Verify bet setting Screen", "On Clicking bet button ,Bet Setting overlay should display in Lanscape orientation", "Bet Setting overlay is display", "Pass",languageCode);
							cfnlib.funcPortrait();
							cfnlib.closeTotalBet();
						}
						else
						{
							//report and screen shot
							languageReport.detailsAppendFolder("Verify bet setting Screen", "On Clicking bet button ,Bet Setting overlay should display", "Bet Setting overlay is display", "Fail",languageCode);

						}
						//******************Deal button
						if(cfnlib.dealClick())
						{
							
							languageReport.detailsAppendFolder("Verify deal functionality", "On Clicking deal button", "Draw button should be displayed", "Pass", languageCode);
							cfnlib.funcLandscape();
							Thread.sleep(1000);
							languageReport.detailsAppendFolder("Verify deal functionality", "On Clicking deal button in landscape orientation", "Draw button should be displayed in landscape orientation", "Pass", languageCode);
							cfnlib.funcPortrait();
							if(cfnlib.drawClick())
							{
								languageReport.detailsAppendFolder("Verify draw functionality", "On Clicking draw button", "Draw button should be displayed", "Pass", languageCode);
								cfnlib.funcLandscape();
								Thread.sleep(1000);
								languageReport.detailsAppendFolder("Verify draw functionality", "On Clicking draw button", "Draw button should be displayed", "Pass", languageCode);
								cfnlib.funcPortrait();
								Thread.sleep(1000);
								cfnlib.drawCollectBaseGame(languageReport,languageCode);

							}else
							{
								languageReport.detailsAppendFolder("Verify draw functionality", "On Clicking draw button", "Draw button is not displayed", "Fail", languageCode);
							}

						}
						else
						{
							languageReport.detailAppend("Verify deal functionality", "On Clicking deal button", "Draw button is not displayed,fail to click on deal button", "Fail", languageCode);
						}


						

						//*************Double to functionality
						cfnlib.doubleToGambleReached(languageReport,languageCode);


						//*************Paytable functionality
						cfnlib.paytableClickVideoPoker(languageReport,languageCode);

						if(cfnlib.settingsOpen())
						{
							languageReport.detailsAppendFolder("Verify setting Screen", "On Clicking setting button ,Setting overlay should display", "Setting overlay is display", "Pass",languageCode);
							cfnlib.funcLandscape();
							languageReport.detailsAppendFolder("Verify setting Screen", "On Clicking setting button ,Setting overlay should display in landsacpe orintation", "Setting overlay is display", "Pass",languageCode);
							cfnlib.funcPortrait();
							if(cfnlib.settingsBack())
								{	
									log.debug("Setting overlay close");
									languageReport.detailsAppendFolder("Verify setting Screen", "On Clicking setting back button ,Setting overlay should close", "Setting overlay is close", "Pass",languageCode);
								}
						}
						else
						{
							languageReport.detailAppend("Verify setting Screen", "On Clicking setting button ,Setting overlay should display", "Setting overlay is not display", "Fail",languageCode);

						}
						//Language Change logic:: for updating language in URL and then Refresh 
						if (j + 1 != rowCount2){
							rowData3 = list.get(j+1);
							String languageCode2 = rowData3.get(Constant.LANG_CODE).trim();

							String currentUrl = webdriver.getCurrentUrl();
							String urlNew = currentUrl.replaceAll("languagecode="+languageCode, "languagecode="+languageCode2);
							webdriver.navigate().to(urlNew);
							String error=cfnlib.xpathMap.get("Error");
							if(cfnlib.isElementPresent(error))
							{
								languageReport.detailsAppend("Verify the Language code is " +languageCode2+" ", " Application window should be displayed in " +languageDescription+"", "", "");
								languageReport.detailsAppend("Verify that any error is coming","error must not come","error is coming", "fail");
								if (j + 2 != rowCount2){
									rowData3 = list.get(j+2);
									String languageCode3 = rowData3.get(Constant.LANG_CODE).trim();

									currentUrl = webdriver.getCurrentUrl();
									urlNew = currentUrl.replaceAll("LanguageCode="+languageCode2, "LanguageCode="+languageCode3);
									cfnlib.loadGame(urlNew);
								}
								j++;
							}
						}
					}  
				}
				else{
					languageReport.detailsAppendFolder(" Verify that the application should display with Game Logo and game name in Base Scene", "Game logo and game name with base scene should display", "Game logo and game name with base scene not display", "Fail", languageCode);

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
			languageReport.endReport();
			//extent.flush();
			//add code to delete the testdata file copied
			webdriver.close();
			webdriver.quit();
			proxy.abort();
			Thread.sleep(1000);	   
		}	

	}
}
