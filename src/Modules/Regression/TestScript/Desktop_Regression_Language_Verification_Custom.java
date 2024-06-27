package Modules.Regression.TestScript;

import java.io.File;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
//import org.skyscreamer.jsonassert.JSONCompareResult;

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

public class Desktop_Regression_Language_Verification_Custom {
	Logger log = Logger.getLogger(Desktop_Regression_Language_Verification_Custom.class.getName());
	public ScriptParameters scriptParameters;
	
	
	public String gameLanguage = null;
	//public JSONCompareResult languageResult;

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
		String classname = this.getClass().getSimpleName();
		int mintDetailCount=0;
		//int mintSubStepNo=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();
		String xmlFilePath = System.getProperty("user.dir") + "\\src\\Modules\\Regression\\TestData\\GameData\\"
				+ classname + ".testdata";
		File sourceFile = new File(xmlFilePath);
		 File destFile = new File("\\\\"+TestPropReader.getInstance().getProperty("Casinoas1IP")+"\\c$\\MGS_TestData\\"+classname +".testdata");

		Desktop_HTML_Report language = new Desktop_HTML_Report(webdriver, browsername, filePath, startTime, mstrTC_Name,
				mstrTC_Desc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings, mintStepNo, Status,
				gameName);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, language, gameName);


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
				String password = Constant.PASSWORD;
				// ----------Update the Xml File of Papyrus Data----------//
				util.changePlayerName(userName,  xmlFilePath);
				
				// --------Copy the Papyrus Data to The CasinoAs1 Server---------//
				util.copyFolder(sourceFile, destFile);
				String url = cfnlib.XpathMap.get("ApplicationURL");
				String obj = cfnlib.func_navigate(url);
				if (obj != null) {
					language.detailsAppend("Open Browser and Enter Lobby URL in address bar and click Enter","Island Paradise Lobby should open", "Island Paradise lobby is Open", "Pass");
				} else 
				{
					language.detailsAppend("Open browser and Enter Lobby URL in address bar and click Enter","Island Paradise lobby should Open", "Island paradise is not displayed", "Fail");
				}
				long loadingTime = cfnlib.Random_LoginBaseScene(userName);
				if (loadingTime < 10.0) {
					language.detailsAppend("Check that user is able to login and Observe initial loading time","" + gameName + " Game should be launched and initial loading time should be less than 10 seconds","" + gameName + " is launched successfully and initial loading time is "+loadingTime+" Seconds", "Pass");
				} else {
					language.detailsAppend("Check that user is able to login and Observe initial loading time","" + gameName + " Game should be launched and initial loading time should be less than 10 seconds","" + gameName + " is launched successfully and initial loading time is "+loadingTime+" Seconds", "Fail");
				}						

				int rowCount2 = excelpoolmanager.rowCount(testDataExcelPath, "LanguageCodes");
				for(int j=1;j<rowCount2;j++){

					createplayer.updateBalance(userName);
					//Step 2: To get the languages in MAP and load the language specific url
					rowData2 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j);
					languageDescription = rowData2.get("Language").toString();
					languageCode = rowData2.get("Language Code").toString().trim();
				
					language.detailsAppendFolder("Verify the Language code is " +languageCode+" ", " Application window should be displayed in " +languageDescription+"", "", "", languageCode);
    
				    cfnlib.splashScreen(language,languageCode);

				    language.detailsAppendFolder("Verify that the application should display with Game Logo and game name in Base Scene", "Game logo and game name with base scene should display", "Game logo and game name with base scene displays", "Pass", languageCode);
				    Thread.sleep(1000);
				    
				    int width = 700;
				    int height = 575;
				    cfnlib.resizeBrowser(width, height);
				    Thread.sleep(2000);
				    language.detailsAppendFolder("Verify that the application should display correctly after resize", "Game logo and game name with base scene should display after resize", "Game logo and game name with base scene displays properly after resize", "Pass", languageCode);
				    cfnlib.maxiMizeBrowser();
				    Thread.sleep(1500);
				    cfnlib.verifyStopLanguage(language, languageCode);
				   // cfnlib.spinclick();
				    
				    //to take screenshot for jackpot summary of Major Millions Game
				   cfnlib.jackpotSummary(language, languageCode);
				    // Capture Screen shot for Bet Screen
				   /* cfnlib.open_TotalBet();
					language.details_append_folder("Verify that language on the Bet Amount Screen", "Bet Amount Screen should be display", "Bet Amount Screen should be displayed in " +languageDescription+ " language", "Pass", languageCode);
					cfnlib.close_TotalBet();*/
					
					boolean openAutoplay = cfnlib.openAutoplay();
					   if (openAutoplay){
					   language.detailsAppendFolder("Verify language on the Autoplay Screen", "Autoplay Screen should be display", "Autoplay Screen should be displayed", "Pass", languageCode);
					   }
					   else{
					   language.detailsAppendFolder("Verify language on the Autoplay Screen", "Autoplay Screen should be display", "Autoplay Screen doesn't display", "Fail", languageCode);   
					   }
					   cfnlib.close_Autoplay();
					
					if(framework.equalsIgnoreCase("CS")){
					boolean menuOpen = cfnlib.menuOpen();
					
					if(menuOpen){
						language.detailsAppendFolder("Verify that Language of menu link is " + languageDescription + " ", "Language of Menu Links should be " +languageDescription+ "", "Menu Links are displaying in " +languageDescription+ " language", "pass", languageCode);
					}
					else {
						language.detailsAppendFolder("Verify that Language" + languageDescription + "should be display properly on menu links", "Language inside Menu Links should be in " +languageDescription+ " language", "Language inside Menu Links is displaying", "fail", languageCode);
					}
					
					cfnlib.menuClose();
					}
					
					boolean openSetting= cfnlib.settingsOpen();
					if(openSetting){
						language.detailsAppendFolder("Verify that Language on settings screen is " + languageDescription + " ", "Language in Settigns Screen should be " +languageDescription+ " ", "Language inside Settings screens is " +languageDescription+ " ", "pass", languageCode);
					}
					else {
						language.detailsAppendFolder("Verify that Language back on menu is " + languageDescription +"should be display properly on menu links", "Language inside Menu Links should be in " +languageDescription+ " language", "Language inside Menu Links is displaying", "fail", languageCode);
					}

					cfnlib.settingsBack();
						
					cfnlib.capturePaytableScreenshot(language, languageCode);
					
					//cfnlib.paytableClose();
					
					//for updating language in URL and then Refresh 
					if (j + 1 != rowCount2)
					  { 
						  rowData3 =  excelpoolmanager.readExcelByRow(testDataExcelPath,
					  "LanguageCodes", j+1); 
					  String nextLanguage =  rowData3.get("Language Code").toString().trim();
					  
					  String currentUrl = webdriver.getCurrentUrl(); 
					  String url_new = currentUrl.replaceAll("LanguageCode="+languageCode,
					  "LanguageCode="+nextLanguage);
					  webdriver.navigate().to(url_new);
					  String error=cfnlib.XpathMap.get("Error");
					  if(cfnlib.isElementPresent(error))
						{
							language.detailsAppendFolder("Verify the Language code is " +nextLanguage+" ", " Application window should be displayed in " +languageDescription+"", "", "", nextLanguage);
							language.detailsAppendFolder("Verify that any error is coming","error must not come","error is coming", "fail", nextLanguage);
							for(int l=j+2; l<=j+2; l++){
								rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",  l);
								String languageCode3 = rowData3.get("Language Code").toString().trim();
							
							currentUrl = webdriver.getCurrentUrl();
							 url_new = currentUrl.replaceAll("LanguageCode="+nextLanguage, "LanguageCode="+languageCode3);
							webdriver.navigate().to(url_new);
							
							}
							j++;
						}
					  }
					  else {
							Thread.sleep(2000);
							rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", 1);
							String nextLanguage = rowData3.get("Language Code").toString().trim();

							String currentUrl = webdriver.getCurrentUrl();
							String url_new = currentUrl.replaceAll("LanguageCode=" + languageCode,
									"LanguageCode=" + nextLanguage);
							webdriver.navigate().to(url_new);
							
						}}  

				
				
				//int rowCount2 = excelpoolmanager.rowCount(constant.testData_Excel_path, "LanguageCodes");
		/*		cfnlib.spinclick();
				Thread.sleep(15000);

				
				// FreeSpinEntryScreen string should be yes in excel sheet, if entry screen is present
				
				String FreeSpinEntryScreen = cfnLibrary_Desktop_CS.XpathMap.get("FreeSpinEntryScreen");				
				 String b2 = cfnlib.entryScreen_Wait(FreeSpinEntryScreen);
				 if(b2.equalsIgnoreCase("CLICK TO CONTINUE")) {
				  language.details_append("Verify Different Languages on Free spin entry screen","Free spin entry screen should display as per respective language", "", ""); 
				  for(int j=1;j<rowCount2;j++){
				  
				  //Step 2: To get the languages in MAP and load the language specific url 
				  rowData2 =  excelpoolmanager.readExcelByRow(Constant.testData_Excel_path,"LanguageCodes", j);
				  languageDescription =  rowData2.get("Language").toString(); 
				  languageCode = rowData2.get("Language Code").toString().trim();
				  
				  cfnlib.entryScreen_Wait("FreeSpinEntryScreen");
				  language.details_append_folder("Verify that the application should display Free spin entry screen in " +languageCode+" ",
				  "Free spin entry screen should display in " +languageDescription+" ","Free spin entry screen displays in " +languageDescription+
				  " ", "Pass", languageCode); 
				  //for updating language in URL and then Refresh
				  if (j + 1 != rowCount2)
				  { 
					  rowData3 =  excelpoolmanager.readExcelByRow(Constant.testData_Excel_path,
				  "LanguageCodes", j+1); 
				  String nextLanguage =  rowData3.get("Language Code").toString().trim();
				  
				  String currentUrl = webdriver.getCurrentUrl(); 
				  String url_new = currentUrl.replaceAll("LanguageCode="+languageCode,
				  "LanguageCode="+nextLanguage);
				  webdriver.navigate().to(url_new); 
				  }
				  else {
						Thread.sleep(2000);
						rowData3 = excelpoolmanager.readExcelByRow(Constant.testData_Excel_path, "LanguageCodes", 1);
						String nextLanguage = rowData3.get("Language Code").toString().trim();

						String currentUrl = webdriver.getCurrentUrl();
						String url_new = currentUrl.replaceAll("LanguageCode=" + languageCode,
								"LanguageCode=" + nextLanguage);
						webdriver.navigate().to(url_new);
						cfnlib.clickToContinue();
					}
				  }
				  } 
				 else{
				  System.out.println("Free Spin Entry screen is not present in Game"); } 
				 
				Thread.sleep(1000);
				cfnlib.FSSceneLoading();
				
				String startFreeSpinButton = cfnLibrary_Desktop_CS.XpathMap.get("StartFreeSpinButton");
				if(startFreeSpinButton.equalsIgnoreCase("Yes")) {
				webdriver.navigate().refresh();
				cfnlib.FSSceneLoading();
				language.details_append("Verify the Different Languages in Free Spin",
						"Free Spin window should display as per respective language", "", "");
				for (int j = 1; j < rowCount2; j++) {

					// Step 2: To get the languages in MAP and load the language specific url

					rowData2 = excelpoolmanager.readExcelByRow(Constant.testData_Excel_path, "LanguageCodes", j);
					languageDescription = rowData2.get("Language").toString();
					languageCode = rowData2.get("Language Code").toString().trim();

					// method for wait and validation.
					cfnlib.FSSceneLoading();

					language.details_append_folder(
							"Verify that the application should display Free Spin scene in " + languageCode + " ",
							"Free Spin scene should display in " + languageDescription + " ",
							"Free Spin scene displays in " + languageDescription + " ", "Pass", languageCode);
					// for updating language in URL and then Refresh
					if (j + 1 != rowCount2) {
						rowData3 = excelpoolmanager.readExcelByRow(Constant.testData_Excel_path, "LanguageCodes",
								j + 1);
						String nextLanguage = rowData3.get("Language Code").toString().trim();

						String currentUrl = webdriver.getCurrentUrl();
						String url_new = currentUrl.replaceAll("LanguageCode=" + languageCode,
								"LanguageCode=" + nextLanguage);
						webdriver.navigate().to(url_new);
					} else {
						Thread.sleep(2000);
						rowData3 = excelpoolmanager.readExcelByRow(Constant.testData_Excel_path, "LanguageCodes", 1);
						String nextLanguage = rowData3.get("Language Code").toString().trim();

						String currentUrl = webdriver.getCurrentUrl();
						String url_new = currentUrl.replaceAll("LanguageCode=" + languageCode,
								"LanguageCode=" + nextLanguage);
						webdriver.navigate().to(url_new);
					}

				}
				cfnlib.FSSceneLoading();
				cfnlib.FS_Start();
				}
				Thread.sleep(10000);
				webdriver.navigate().refresh();
				
				cfnlib.FSSceneLoading();
				language.details_append("Verify the Different Languages in Free Spin",
						"Free Spin window should display as per respective language", "", "");
				for (int j = 1; j < rowCount2; j++) {

					// Step 2: To get the languages in MAP and load the language specific url

					rowData2 = excelpoolmanager.readExcelByRow(Constant.testData_Excel_path, "LanguageCodes", j);
					languageDescription = rowData2.get("Language").toString();
					languageCode = rowData2.get("Language Code").toString().trim();

					// method for wait and validation.
					cfnlib.FSSceneLoading();

					language.details_append_folder(
							"Verify that the application should display Free Spin scene in " + languageCode + " ",
							"Free Spin scene should display in " + languageDescription + " ",
							"Free Spin scene displays in " + languageDescription + " ", "Pass", languageCode);
					// for updating language in URL and then Refresh
					if (j + 1 != rowCount2) {
						rowData3 = excelpoolmanager.readExcelByRow(Constant.testData_Excel_path, "LanguageCodes",
								j + 1);
						String nextLanguage = rowData3.get("Language Code").toString().trim();

						String currentUrl = webdriver.getCurrentUrl();
						String url_new = currentUrl.replaceAll("LanguageCode=" + languageCode,
								"LanguageCode=" + nextLanguage);
						webdriver.navigate().to(url_new);
					} else {
						Thread.sleep(2000);
						rowData3 = excelpoolmanager.readExcelByRow(Constant.testData_Excel_path, "LanguageCodes", 1);
						String nextLanguage = rowData3.get("Language Code").toString().trim();

						String currentUrl = webdriver.getCurrentUrl();
						String url_new = currentUrl.replaceAll("LanguageCode=" + languageCode,
								"LanguageCode=" + nextLanguage);
						webdriver.navigate().to(url_new);
					}

				}
			
				cfnlib.FSSceneLoading();
				cfnlib.FS_continue();
				cfnlib.waitForSpinButton();
				//cfnlib.waitSummaryScreen();
				
				webdriver.navigate().refresh();
				language.details_append("Verify the Different Languages in Free Spin Summary Screen",
						"Free Spin Summary Screen should display as per respective language", "", "");
				for (int j = 1; j < rowCount2; j++) {

					// Step 2: To get the languages in MAP and load the language specific url

					rowData2 = excelpoolmanager.readExcelByRow(Constant.testData_Excel_path, "LanguageCodes", j);
					languageDescription = rowData2.get("Language").toString();
					languageCode = rowData2.get("Language Code").toString().trim();

					// method for wait and validation.
					cfnlib.waitSummaryScreen();
					Thread.sleep(2000);
					language.details_append_folder(
							"Verify that the application should display Free Spin Summary in " + languageCode + " ",
							"Free Spin Summary should display in " + languageDescription + " ",
							"Free Spin Summary displays in " + languageDescription + " ", "Pass", languageCode);
					// for updating language in URL and then Refresh
					if (j + 1 != rowCount2) {
						rowData3 = excelpoolmanager.readExcelByRow(Constant.testData_Excel_path, "LanguageCodes",
								j + 1);
						String nextLanguage = rowData3.get("Language Code").toString().trim();
						String currentUrl = webdriver.getCurrentUrl();
						String url_new = currentUrl.replaceAll("LanguageCode=" + languageCode,
								"LanguageCode=" + nextLanguage);
						webdriver.navigate().to(url_new);
					} 
				} 
*/
				// Step 5: Logout From the Game
				/*String logout = cfnlib.Func_logout_OD();
				Thread.sleep(3000);
				if (logout.trim() != null) {
					language.details_append("Verify that user has successfully logged out ",
							"User should be successfully logged out", "User is Successfully logged out", "pass");
				} else {
					language.details_append("Verify that user has successfully logged out ",
							"User should be successfully logged out", "User is not Successfully logged out", "fail");
				}*/
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
			webdriver.close();
			webdriver.quit();
			proxy.abort();
			Thread.sleep(1000);
		}
	}
}
