package Modules.Regression.TestScript;
import java.io.File;
import java.util.Map;

import org.apache.log4j.Logger;
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

public class Mobile_Regression_Language_Verification_Custom {
Logger log = Logger.getLogger(Mobile_Regression_Language_Verification_BaseScene.class.getName());
	
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
		
	Mobile_HTML_Report language=new Mobile_HTML_Report(webdriver,DeviceName,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,gameName);
		
	MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
	CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, language, gameName);
	
		String classname= this.getClass().getSimpleName();
		String xmlFilePath = System.getProperty("user.dir") + "\\src\\Modules\\Regression\\TestData\\GameData\\"
				+ classname + ".testdata";
		File sourceFile = new File(xmlFilePath);
		/* Casinoas1 ip for qa */
		File destFile = new File("\\\\10.247.224.110\\C$\\MGS_TestData\\" + classname + ".testdata");
		/* Casinoas1 ip for dev */
		//public File destFile = new File("\\\\10.247.224.22\\c$\\MGS_TestData\\"+classname+".testdata");
		/* Casinoas1 ip for qa2 */
		//public File destFile = new File("\\\\10.247.225.0\\C$\\MGS_TestData\\" + classname + ".testdata");
		
		CommonUtil 	util = new CommonUtil();
		//DataBaseFunctions createplayer=new DataBaseFunctions("zensardev","Casino","10.247.208.54","1402","5001");
		DataBaseFunctions createplayer=new DataBaseFunctions("zensarQA","Casino","10.247.208.62","1402","5001");
		//DataBaseFunctions createplayer=new DataBaseFunctions("zensarqa2","Casino","10.247.208.113","1402","5001");
		//CreateUserInDB createplayer = new CreateUserInDB();
		try{
			// Step 1
			if(webdriver!=null)
			{		
				Map<String, String> rowData2 = null;
				Map<String, String> rowData3 = null;
				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				/*userName = gcfnlib.randomStringgenerator();
				System.out.println("The New username is ==" + userName);
				createplayer.createUser(userName, "0", 0);*/
			String	password = Constant.PASSWORD;
				// ---Update the Xml File of Papyrus Data----------//
				//	gcfnlib.changePlayerName(userName, constant.clientID, xmlFilePath);
				String url=cfnlib.xpathMap.get("ApplicationURL");

				// ---Copy the Papyrus Data to The CasinoAs1 Server---------//
				//gcfnlib.copyFolder(sourceFile, destFile);
				String obj = cfnlib.funcNavigate(url);
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
						for(int j=1;j<rowCount2;j++){

							//Step 2: To get the languages in MAP and load the language specific url
							rowData2 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",  j);
							String languageDescription = rowData2.get("Language").toString();
							String languageCode = rowData2.get("Language Code").toString().trim();
						
						    language.detailsAppendFolder("Verify the Language code is " +languageCode+" ", " Application window should be displayed in " +languageDescription+"", "", "",languageCode);
						
						    cfnlib.splashScreen(language,languageCode);

							//to remove hand gesture
							cfnlib.funcFullScreen();	
						    language.detailsAppendFolder("Verify that the application should display with Game Logo and game name in Base Scene", "Game logo and game name with base scene should display", "Game logo and game name with base scene displays", "Pass", languageCode);
						    Thread.sleep(2000);
						    
						   //cfnlib.verifyStopLanguage(language,languageCode);
						   //to take screenshot for jackpot summary of Major Millions Game
						    //need to uncomment for jackpot cfnlib.jackpotSummary(language, languageCode);
						    
							// Capture Screen shot for Bet Screen
					/*		cfnlib.open_TotalBet();
							language.details_append_folder("Verify that language on the Bet Amount Screen", "Bet Amount should be display", "Bet Amount should be displayed in " +languageDescription+ " language", "Pass", languageCode);
							
							cfnlib.close_TotalBet();*/
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
							//to remove hand gesture
							//	cfnlib.Func_FullScreen();			
						    if (j + 1 != rowCount2){
								rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j+1);
								languageDescription = rowData3.get("Language").toString();
								String languageCode2 = rowData3.get("Language Code").toString().trim();
							
							String currentUrl = webdriver.getCurrentUrl();
							String url_new = currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+languageCode2);
							webdriver.navigate().to(url_new);
							String error=cfnlib.xpathMap.get("Error");
							if(cfnlib.isElementPresent(error))
							{
								language.detailsAppendFolder("Verify the Language code is " +languageCode2+" ", " Application window should be displayed in " +languageDescription+"", "", "", languageCode2);
								language.detailsAppendFolder("Verify that any error is coming","error must not come","error is coming", "fail", languageCode2);
								 if (j + 2 != rowCount2){
									rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j+2);
									String languageCode3 = rowData3.get("Language Code").toString().trim();
								
								currentUrl = webdriver.getCurrentUrl();
								 url_new = currentUrl.replaceAll("LanguageCode="+languageCode2, "LanguageCode="+languageCode3);
								webdriver.navigate().to(url_new);
								
								}
								j++;
							}
							}
						}
						/*String logout= cfnlib.Func_logout();
						System.out.println("The logout title is "+logout);
						if(logout.trim()!=null&&logout.equalsIgnoreCase("Login"))
						{
							language.details_append("Verify that user has successfully logged out ", "User should be successfully logged out", "User is Successfully logged out", "pass");
						}
						else 
						{
							language.details_append("Verify that user has successfully logged out ", "User should be successfully logged out", "User is not Successfully logged out", "fail");
						}*/
					}
							    
			
		}//TRY
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