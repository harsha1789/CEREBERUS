package Modules.Regression.TestScript;
import java.io.File;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.DataBaseFunctions;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import net.lightbody.bmp.BrowserMobProxyServer;

public class Mobile_Regression_Language_Verification_Custom1 {
Logger log = Logger.getLogger(Mobile_Regression_Language_Verification_Custom1.class.getName());
	
	public ScriptParameters scriptParameters;
	
	public void script() throws Exception{
		String mstrTC_Name=scriptParameters.getMstrTCName();
		String mstrTC_Desc=scriptParameters.getMstrTCDesc();
		String mstrModuleName=scriptParameters.getMstrModuleName();
		BrowserMobProxyServer proxy=scriptParameters.getProxy();
		String startTime=scriptParameters.getStartTime();
		String filePath=scriptParameters.getFilePath();
		
		//AndroidDriver<WebElement> webdriver=scriptParameters.getWebdriver();
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
		
		ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();
		String languageDescription,languageCode=null;
		
	String classname= this.getClass().getSimpleName();
	 String xmlFilePath = System.getProperty("user.dir") + "\\src\\Modules\\Regression\\TestData\\GameData\\"+ classname + ".testdata";
	 File sourceFile = new File(xmlFilePath);
	 File destFile=null;
		
	Mobile_HTML_Report language=new Mobile_HTML_Report(webdriver,DeviceName,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,gameName);
		
	MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
	CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, language, gameName);
	
		CommonUtil util = new CommonUtil();
		//DataBaseFunctions createplayer=new DataBaseFunctions("zensardev","Casino","10.247.208.54","1402","5001");
		DataBaseFunctions createplayer=new DataBaseFunctions("zensarQA","Casino","10.247.208.62","1402","5001");
		//DataBaseFunctions createplayer=new DataBaseFunctions("zensardev2","Casino","10.247.208.107","1402","5001");
		//DataBaseFunctions createplayer=new DataBaseFunctions("zensarqa2","Casino","10.247.208.113","1402","5001");
		//CreateUserInDB createplayer = new CreateUserInDB();
		try {
			if (webdriver != null) {
				//Map rowData1 = null;
				Map<String, String> rowData2 = null;
				Map<String, String> rowData3 = null;
				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				userName = util.randomStringgenerator();
				System.out.println("The New username is ==" + userName);
				createplayer.createUser(userName, "0", 0);
				
				/* Casinoas1 ip for qa2 */
				//destFile = new File("\\\\10.247.225.0\\C$\\MGS_TestData\\" +classname + "_" + userName +".testdata");
				/* Casinoas1 ip for qa */
				destFile = new File("\\\\10.247.224.110\\C$\\MGS_TestData\\" +classname + "_" + userName +".testdata");
				/* Casinoas1 ip for dev */
				//destFile = new File("\\\\10.247.224.22\\c$\\MGS_TestData\\"+classname + "_" + userName +".testdata");
				/* Casinoas1 ip for dev2 */
				//destFile = new File("\\\\10.247.224.239\\c$\\MGS_TestData\\"+classname + "_" + userName +".testdata");
				
				// ---Update the Xml File of Papyrus Data----------//
				util.changePlayerName(userName, xmlFilePath);
				
				// ----Copy the Papyrus Data to The CasinoAs1 Server---------//
				util.copyFolder(sourceFile, destFile);
				String url = cfnlib.xpathMap.get("ApplicationURL");
				
				String obj = cfnlib.funcNavigate(url);
				if (obj != null) {
					 //need to uncomment "Open Browser and Enter Lobby URL in address bar and click Enter","Island Paradise Lobby should open", "Island Paradise lobby is Open", "Pass");
				} else {
					 //need to uncomment "Open browser and Enter Lobby URL in address bar and click Enter","Island Paradise lobby should Open", "Island paradise is not displayed", "Fail");
				}
				long loadingTime = cfnlib.Random_LoginBaseScene(userName);
				if (loadingTime < 10.0) {
					 //need to uncomment "Check that user is able to login and Observe initial loading time","" + GameName + " Game should be launched and initial loading time should be less than 10 seconds","" + GameName + " is launched successfully and initial loading time is "+loadingTime+" Seconds", "Pass");
				} else {
					 //need to uncomment "Check that user is able to login and Observe initial loading time","" + GameName + " Game should be launched and initial loading time should be less than 10 seconds","" + GameName + " is launched successfully and initial loading time is "+loadingTime+" Seconds", "Fail");
				}	
				cfnlib.funcFullScreen();
				int rowCount2 = excelpoolmanager.rowCount(testDataExcelPath, "LanguageCodes");
				for(int bonusSelection=1;bonusSelection<=4;bonusSelection++)
				{
				cfnlib.waitForSpinButton();
				cfnlib.spinclick();
				// FreeSpinEntryScreen  language.details_append_folder(string should be yes in excel sheet, if entry screen is present
				 // language.details_append_folder("Verify Different Languages on Free spin entry screen","Free spin entry screen should display as per respective language", "", "" ,"");
				String FreeSpinEntryScreen = cfnlib.xpathMap.get("FreeSpinEntryScreen");				
				String b2 = cfnlib.entryScreen_Wait(FreeSpinEntryScreen);
				 if(b2.equalsIgnoreCase("freeSpin")) 
				 {
				 for(int j=1;j<rowCount2;j++){

					 //Step 2: To get the languages in MAP and load the language specific url 
				  rowData2 =  excelpoolmanager.readExcelByRow(testDataExcelPath,"LanguageCodes", j);
				  languageDescription =  rowData2.get("Language").toString(); 
				  languageCode = rowData2.get("Language Code").toString().trim();
				 				  
				  cfnlib.entryScreen_Wait(FreeSpinEntryScreen);
				  Thread.sleep(5000);
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
						 cfnlib.entryScreen_Wait(FreeSpinEntryScreen);
						//cfnlib.clickToContinue();
						cfnlib.clickBonusSelection(bonusSelection);
					}
				  }
				  } 
				 else{
				  System.out.println("Free Spin Entry screen is not present in Game"); } 
				 
				Thread.sleep(1000);
				cfnlib.FSSceneLoading(bonusSelection);
				
				String startFreeSpinButton = cfnlib.xpathMap.get("StartFreeSpinButton");
				if(startFreeSpinButton.equalsIgnoreCase("Yes")) {
				webdriver.navigate().refresh();
				cfnlib.acceptAlert();
				cfnlib.FSSceneLoading(bonusSelection);
				 //need to uncomment "Verify the Different Languages in Free Spin","Free Spin window should display as per respective language", "", "");
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
				cfnlib.funcFullScreen();
				cfnlib.acceptAlert();
				
				cfnlib.FSSceneLoading(bonusSelection);
				 //need to uncomment "Verify the Different Languages in Free Spin","Free Spin window should display as per respective language", "", "");
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
						cfnlib.funcFullScreen();
					} else {
						Thread.sleep(2000);
						rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", 1);
						String nextLanguage = rowData3.get("Language Code").toString().trim();

						String currentUrl = webdriver.getCurrentUrl();
						String url_new = currentUrl.replaceAll("LanguageCode=" + languageCode,"LanguageCode=" + nextLanguage);
						webdriver.navigate().to(url_new);
						cfnlib.FSSceneLoading(bonusSelection);
						cfnlib.funcFullScreen();
					}

				}
			
				cfnlib.FSSceneLoading(bonusSelection);
				Thread.sleep(1000);
				cfnlib.FS_continue();
				cfnlib.waitForSpinButton();
				//cfnlib.waitSummaryScreen();
				
				webdriver.navigate().refresh();
				cfnlib.funcFullScreen();
				cfnlib.acceptAlert();
				 //need to uncomment "Verify the Different Languages in Free Spin Summary Screen","Free Spin Summary Screen should display as per respective language", "", "");
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
						cfnlib.funcFullScreen();
					} 
				} 
			}
			}
		}
		//-------------------Handling the exception---------------------//
		catch (Exception e) {
			/*ExceptionHandler eHandle=new ExceptionHandler(e,webdriver,language);
			eHandle.evalException();*/
		e.printStackTrace();
		}
		finally
		{
			//Thread.sleep(5000);

			language.endReport();
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