package Modules.Regression.TestScript;

import java.io.File;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.DataBaseFunctions;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;

public class Desktop_Regression_Language_Verification_Custom1 
{
	Logger log = Logger.getLogger(Desktop_Regression_Language_Verification_Custom1.class.getName());
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
		String browsername=scriptParameters.getBrowserName();
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
		ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();

		String classname = this.getClass().getSimpleName();
		String xmlFilePath = System.getProperty("user.dir") + "\\src\\Modules\\Regression\\TestData\\GameData\\"
				+ classname + ".testdata";
		File sourceFile = new File(xmlFilePath);
		 File destFile = new File("\\\\"+TestPropReader.getInstance().getProperty("Casinoas1IP")+"\\c$\\MGS_TestData\\"+classname +".testdata");

		Desktop_HTML_Report language = new Desktop_HTML_Report(webdriver, browsername, filePath, startTime, mstrTC_Name,
				mstrTC_Desc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings, mintStepNo, Status,
				gameName);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,language, gameName);



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
				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				userName = util.randomStringgenerator();
				System.out.println("The New username is ==" + userName);
				createplayer.createUser(userName, "0", 0);
				
				destFile = new File("\\\\"+TestPropReader.getInstance().getProperty("Casinoas1IP")+"\\c$\\MGS_TestData\\"+classname + "_" + userName +".testdata");
				
				// ---Update the Xml File of Papyrus Data----------//
				util.changePlayerName(userName, xmlFilePath);
				
				// ----Copy the Papyrus Data to The CasinoAs1 Server---------//
				util.copyFolder(sourceFile, destFile);
				String url = cfnlib.XpathMap.get("ApplicationURL");
				
				String obj = cfnlib.func_navigate(url);
				if (obj != null) {
					 language.detailsAppend("Open Browser and Enter Lobby URL in address bar and click Enter","Island Paradise Lobby should open", "Island Paradise lobby is Open", "Pass");
				} else {
					 language.detailsAppend("Open browser and Enter Lobby URL in address bar and click Enter","Island Paradise lobby should Open", "Island paradise is not displayed", "Fail");
				}
				long loadingTime = cfnlib.Random_LoginBaseScene(userName);
				if (loadingTime < 10.0) {
					 language.detailsAppend("Check that user is able to login and Observe initial loading time","" + gameName + " Game should be launched and initial loading time should be less than 10 seconds","" + gameName + " is launched successfully and initial loading time is "+loadingTime+" Seconds", "Pass");
				} else {
					 language.detailsAppend("Check that user is able to login and Observe initial loading time","" + gameName + " Game should be launched and initial loading time should be less than 10 seconds","" + gameName + " is launched successfully and initial loading time is "+loadingTime+" Seconds", "Fail");
				}	
				
				int rowCount2 = excelpoolmanager.rowCount(testDataExcelPath, "LanguageCodes");
				for(int bonusSelection=2;bonusSelection<=4;bonusSelection++)
				{
				cfnlib.waitForSpinButton();
				cfnlib.spinclick();
				// FreeSpinEntryScreen string should be yes in excel sheet, if entry screen is present
				// language.details_append("Verify Different Languages on Free spin entry screen","Free spin entry screen should display as per respective language", "", "" );
				String FreeSpinEntryScreen = cfnlib.XpathMap.get("FreeSpinEntryScreen");				
				String b2 = cfnlib.entryScreen_Wait(FreeSpinEntryScreen);
				Thread.sleep(6000);
				 if(b2.equalsIgnoreCase("freeSpin")) 
				 {
				 for(int j=1;j<rowCount2;j++)
				 {

					 //Step 2: To get the languages in MAP and load the language specific url 
				  rowData2 =  excelpoolmanager.readExcelByRow(testDataExcelPath,"LanguageCodes", j);
				  languageDescription =  rowData2.get("Language").toString(); 
				  languageCode = rowData2.get("Language Code").toString().trim();
				 				  
				  cfnlib.entryScreen_Wait(FreeSpinEntryScreen);
				  Thread.sleep(4000);
				   language.detailsAppendFolder("Verify that the application should display Free spin entry screen in " +languageCode+" ","Free spin entry screen should display in " +languageDescription+" ","Free spin entry screen displays in " +languageDescription+ " ", "Pass", languageCode); 
				  //for updating language in URL and then Refresh
				  if (j + 1 != rowCount2)
				  { 
					  rowData3 =  excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j+1); 
				  String nextLanguage =  rowData3.get("Language Code").toString().trim();
				  
				  String currentUrl = webdriver.getCurrentUrl(); 
				  String url_new = currentUrl.replaceAll("LanguageCode="+languageCode,"LanguageCode="+nextLanguage);
				  webdriver.navigate().to(url_new); 
				  }
				  else {
						Thread.sleep(2000);
						rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", 1);
						String nextLanguage = rowData3.get("Language Code").toString().trim();

						String currentUrl = webdriver.getCurrentUrl();
						String url_new = currentUrl.replaceAll("LanguageCode=" + languageCode, "LanguageCode=" + nextLanguage);
						webdriver.navigate().to(url_new);
						//cfnlib.clickToContinue();
						cfnlib.clickBonusSelection(bonusSelection);
					}
				  }
					 
				  } 
				 else{
				  System.out.println("Free Spin Entry screen is not present in Game"); } 
				 
				Thread.sleep(1000);
				cfnlib.FSSceneLoading(bonusSelection);
				
				String startFreeSpinButton = cfnlib.XpathMap.get("StartFreeSpinButton");
				if(startFreeSpinButton.equalsIgnoreCase("Yes")) {
				webdriver.navigate().refresh();
				cfnlib.acceptAlert();
				cfnlib.FSSceneLoading(bonusSelection);
				 language.detailsAppend("Verify the Different Languages in Free Spin","Free Spin window should display as per respective language", "", "");
				for (int j = 1; j < rowCount2; j++) {

					// Step 2: To get the languages in MAP and load the language specific url

					rowData2 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j);
					languageDescription = rowData2.get("Language").toString();
					languageCode = rowData2.get("Language Code").toString().trim();

					// method for wait and validation.
					cfnlib.FSSceneLoading(bonusSelection);

					language.detailsAppendFolder("Verify that the application should display Free Spin scene in " + languageCode + " ","Free Spin scene should display in " + languageDescription + " ","Free Spin scene displays in " + languageDescription + " ", "Pass", languageCode);
					// for updating language in URL and then Refresh
					if (j + 1 != rowCount2) {
						rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",
								j + 1);
						String nextLanguage = rowData3.get("Language Code").toString().trim();

						String currentUrl = webdriver.getCurrentUrl();
						String url_new = currentUrl.replaceAll("LanguageCode=" + languageCode,"LanguageCode=" + nextLanguage);
						webdriver.navigate().to(url_new);
					} else {
						Thread.sleep(2000);
						rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", 1);
						String nextLanguage = rowData3.get("Language Code").toString().trim();

						String currentUrl = webdriver.getCurrentUrl();
						String url_new = currentUrl.replaceAll("LanguageCode=" + languageCode,"LanguageCode=" + nextLanguage);
						webdriver.navigate().to(url_new);
					}
				}
				cfnlib.FSSceneLoading(bonusSelection);
				cfnlib.FS_Start();
				}
				Thread.sleep(1000);
				webdriver.navigate().refresh();
				
				cfnlib.acceptAlert();
				
				cfnlib.FSSceneLoading(bonusSelection);
				 language.detailsAppend("Verify the Different Languages in Free Spin","Free Spin window should display as per respective language", "", "");
				for (int j = 1; j < rowCount2; j++) {

					// Step 2: To get the languages in MAP and load the language specific url

					rowData2 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j);
					languageDescription = rowData2.get("Language").toString();
					languageCode = rowData2.get("Language Code").toString().trim();

					// method for wait and validation.
					cfnlib.FSSceneLoading(bonusSelection);

					language.detailsAppendFolder("Verify that the application should display Free Spin scene in " + languageCode + " ","Free Spin scene should display in " + languageDescription + " ","Free Spin scene displays in " + languageDescription + " ", "Pass", languageCode);
					// for updating language in URL and then Refresh
					if (j + 1 != rowCount2) {
						rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",
								j + 1);
						String nextLanguage = rowData3.get("Language Code").toString().trim();

						String currentUrl = webdriver.getCurrentUrl();
						String url_new = currentUrl.replaceAll("LanguageCode=" + languageCode,"LanguageCode=" + nextLanguage);
						webdriver.navigate().to(url_new);
					} else {
						Thread.sleep(2000);
						rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", 1);
						String nextLanguage = rowData3.get("Language Code").toString().trim();

						String currentUrl = webdriver.getCurrentUrl();
						String url_new = currentUrl.replaceAll("LanguageCode=" + languageCode,"LanguageCode=" + nextLanguage);
						webdriver.navigate().to(url_new);
					}

				}
			
				cfnlib.FSSceneLoading(bonusSelection);
				Thread.sleep(1000);
				cfnlib.FS_continue();
				cfnlib.waitForSpinButton();
				//cfnlib.waitSummaryScreen();
				
				webdriver.navigate().refresh();
				cfnlib.acceptAlert();
				 language.detailsAppend("Verify the Different Languages in Free Spin Summary Screen","Free Spin Summary Screen should display as per respective language", "", "");
				for (int j = 1; j < rowCount2; j++) {

					// Step 2: To get the languages in MAP and load the language specific url
					rowData2 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j);
					languageDescription = rowData2.get("Language").toString();
					languageCode = rowData2.get("Language Code").toString().trim();
					// method for wait and validation.
					cfnlib.waitSummaryScreen();
					language.detailsAppendFolder("Verify that the application should display Free Spin Summary in " + languageCode + " ","Free Spin Summary should display in " + languageDescription + " ","Free Spin Summary displays in " + languageDescription + " ", "Pass", languageCode);
					// for updating language in URL and then Refresh
					if (j + 1 != rowCount2) {
						rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",j + 1);
						String nextLanguage = rowData3.get("Language Code").toString().trim();
						String currentUrl = webdriver.getCurrentUrl();
						String url_new = currentUrl.replaceAll("LanguageCode=" + languageCode,"LanguageCode=" + nextLanguage);
						webdriver.navigate().to(url_new);
					} 
				} 
			}
			}
		}
		// -------------------Handling the exception---------------------//
		catch (Exception e) {
			cfnlib.evalException();
			System.out.println(e);
			// e.getMessage();
		}
		// -------------------Closing the connections---------------//
		finally {
			language.endReport();
			if(destFile.delete()){
				log.debug(destFile.getName() + " is deleted!");
			}else{
				log.debug("Test Data Delete operation is failed.");
			}
			webdriver.close();
			webdriver.quit();
			proxy.abort();
			Thread.sleep(1000);
		}
	}
	}
