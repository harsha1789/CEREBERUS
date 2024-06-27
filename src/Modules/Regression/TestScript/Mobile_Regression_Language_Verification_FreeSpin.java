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
import org.openqa.selenium.support.ui.WebDriverWait;

import com.zensar.automation.api.RestAPILibrary;
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
 * This script traverse and capture all the screen shots related to free Spins.
 * It reads the test data excel sheet for configured languages.
 * @author Premlata
 * */
public class Mobile_Regression_Language_Verification_FreeSpin {
	public static Logger log = Logger.getLogger(Mobile_Regression_Language_Verification_FreeGames.class.getName());
	public ScriptParameters scriptParameters;


	public void script() throws Exception {
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
		int checkedOutDeviceNum=scriptParameters.getCheckedOutDeviceNum();


		String status=null;
		int mintDetailCount=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		String languageDescription,languageCode=null;
		ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();
		
		Mobile_HTML_Report language=new Mobile_HTML_Report(webdriver,deviceName,filePath,startTime,mstrTCName,mstrTCDesc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,status,gameName);
		WebDriverWait wait;
		MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, language, gameName);
		

		CommonUtil util = new CommonUtil();
		int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int  cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDMobile"));
		List<String> copiedFiles=new ArrayList<>();
		RestAPILibrary apiobj=new RestAPILibrary();

		cfnlib.setOsPlatform(scriptParameters.getOsPlatform());
		try{

			// Step 1
			if(webdriver!=null)
			{		
				Map<String, String> rowData2 = null;
				Map<String, String> rowData3 = null;
				String urlNew=null;
				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				wait=new WebDriverWait(webdriver,60);
				
				String strFileName=TestPropReader.getInstance().getProperty("MobileFreeSpinsTestDataPath");
				File testDataFile=new File(strFileName);

				userName=util.randomStringgenerator();
				if(	util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles))
				{
					log.debug("Test dat is copy in test Server for Username="+userName);
				
				 
				String url = cfnlib.xpathMap.get("ApplicationURL");
				String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
				log.debug("url = " +LaunchURl);
				if("LANDSCAPE".equalsIgnoreCase(cfnlib.xpathMap.get("Orientation")))
				{
					String context=webdriver.getContext();
					webdriver.context("NATIVE_APP");
						webdriver.rotate(ScreenOrientation.LANDSCAPE);
						webdriver.context(context);
				}	
				cfnlib.loadGame(LaunchURl);
				cfnlib.threadSleep(3000);
				webdriver.navigate().refresh();


				if(framework.equalsIgnoreCase("Force")){
					cfnlib.setNameSpace();
				}


				int rowCount2 = excelpoolmanager.rowCount(testDataExcelPath, "LanguageCodes");
				// Reading the language code into the list map
				List<Map> list= util.readLangList();
				log.debug("Total number of Languages configured"+rowCount2);
				Thread.sleep(6000);
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
				Thread.sleep(3000);
				if(TestPropReader.getInstance().getProperty("IsProgressiveGame").equalsIgnoreCase("Yes")){
					cfnlib.setMaxBet();
				}
				cfnlib.spinclick();
				Thread.sleep(1000);
				
				//Jackpot Bonus Language check for Progresssive Games
				if(TestPropReader.getInstance().getProperty("IsProgressiveGame").equalsIgnoreCase("Yes"))
				{
					language.detailsAppend("Verify Different Languages on Jockpot feature","Jackpot feature should display as per respective language", "", "" );

					for (int j = 1; j < rowCount2; j++) {

						// Step 2: To get the languages in MAP and load the language specific url

						rowData2 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j);
						languageDescription = rowData2.get(Constant.LANGUAGE).trim();
						languageCode = rowData2.get(Constant.LANG_CODE).trim();

						// method to wait for jackpotscene
						cfnlib.jackpotSceneWait();
						Thread.sleep(12000);
						language.detailsAppendFolder("Verify that the application should display jackpot scene in " + languageCode + " ","jackpot scene should display in " + languageDescription + " ","jackpot scene displays in " + languageDescription + " ", "Pass", languageCode);
						Thread.sleep(12000);
						language.detailsAppendFolder("Verify that the application should display jackpot scene in " + languageCode + " ","jackpot scene should display in " + languageDescription + " ","jackpot scene displays in " + languageDescription + " ", "Pass", languageCode);
						Thread.sleep(8000);
						language.detailsAppendFolder("Verify that the application should display jackpot scene in " + languageCode + " ","jackpot scene should display in " + languageDescription + " ","jackpot scene displays in " + languageDescription + " ", "Pass", languageCode);
						
						// for updating language in URL and then Refresh
						if (j + 1 != rowCount2) {
							rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",j + 1);
							String nextLanguage = rowData3.get("Language Code").trim();
							String currentUrl = webdriver.getCurrentUrl();
							if(currentUrl.contains("LanguageCode"))
								 urlNew = currentUrl.replaceAll("LanguageCode=" + languageCode,"LanguageCode=" + nextLanguage);
							else if(currentUrl.contains("languagecode"))
								urlNew = currentUrl.replaceAll("languagecode=" + languageCode,"languagecode=" + nextLanguage);
							else if(currentUrl.contains("languageCode"))
								urlNew = currentUrl.replaceAll("languageCode=" + languageCode,"languageCode=" + nextLanguage);

							
							
							cfnlib.loadGame(urlNew);
							if((Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad"))))
							{
								cfnlib.newFeature();
							}
							cfnlib.funcFullScreen();
						} else {
							Thread.sleep(2000);
							rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", 1);
							String nextLanguage = rowData3.get("Language Code").trim();

							String currentUrl = webdriver.getCurrentUrl();
							if(currentUrl.contains("LanguageCode"))
								 urlNew = currentUrl.replaceAll("LanguageCode=" + languageCode,"LanguageCode=" + nextLanguage);
							else if(currentUrl.contains("languagecode"))
								urlNew = currentUrl.replaceAll("languagecode=" + languageCode,"languagecode=" + nextLanguage);
							else if(currentUrl.contains("languageCode"))
								urlNew = currentUrl.replaceAll("languageCode=" + languageCode,"languageCode=" + nextLanguage);

							cfnlib.loadGame(urlNew);
							if((Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad"))))
							{
								cfnlib.newFeature();
							}
						}
					}

					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cfnlib.xpathMap.get("clock"))));
					Thread.sleep(4000);
					cfnlib.funcFullScreen();
					Thread.sleep(3000);
					cfnlib.clickAtButton("return "+cfnlib.xpathMap.get("ClickOnJackpotSpinButton"));
					//cfnlib.spinclick();
					Thread.sleep(3000);
					
					// method to wait for jackpotscene summary screen
					cfnlib.elementWait("return "+cfnlib.xpathMap.get("isJackpotBonusContinueButtonVisible"), true);
					for (int j = 1; j < rowCount2; j++) {

						// Step 2: To get the languages in MAP and load the language specific url
						rowData2 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j);
						languageDescription = rowData2.get(Constant.LANGUAGE).trim();
						languageCode = rowData2.get(Constant.LANG_CODE).trim();
						cfnlib.elementWait("return "+cfnlib.xpathMap.get("isJackpotBonusContinueButtonVisible"), true);
						language.detailsAppendFolder("Verify that the application should display jackpot summary screen in " + languageCode + " ","Jakcpot summary screen should display in " + languageDescription + " ","Jackpot summary screen displays in " + languageDescription + " ", "Pass", languageCode);
						// for updating language in URL and then Refresh
						if (j + 1 != rowCount2) {
							rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",j + 1);
							String nextLanguage = rowData3.get("Language Code").trim();
							String currentUrl = webdriver.getCurrentUrl();
							if(currentUrl.contains("LanguageCode"))
								 urlNew = currentUrl.replaceAll("LanguageCode=" + languageCode,"LanguageCode=" + nextLanguage);
							else if(currentUrl.contains("languagecode"))
								urlNew = currentUrl.replaceAll("languagecode=" + languageCode,"languagecode=" + nextLanguage);
							else if(currentUrl.contains("languageCode"))
								urlNew = currentUrl.replaceAll("languageCode=" + languageCode,"languageCode=" + nextLanguage);

							cfnlib.loadGame(urlNew);
							//click on continue button
							if((Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad"))))
							{
								cfnlib.newFeature();
							}
						} else {
							Thread.sleep(2000);
							rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", 1);
							String nextLanguage = rowData3.get("Language Code").trim();

							String currentUrl = webdriver.getCurrentUrl();
							if(currentUrl.contains("LanguageCode"))
								 urlNew = currentUrl.replaceAll("LanguageCode=" + languageCode,"LanguageCode=" + nextLanguage);
							else if(currentUrl.contains("languagecode"))
								urlNew = currentUrl.replaceAll("languagecode=" + languageCode,"languagecode=" + nextLanguage);

							cfnlib.loadGame(urlNew);
							//click on continue button
							if((Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad"))))
							{
								cfnlib.newFeature();
							}
						}
					}
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cfnlib.xpathMap.get("clock"))));
					Thread.sleep(3000);
					cfnlib.clickAtButton("return "+cfnlib.xpathMap.get("ClickOnJackpotSummaryContinue"));
				}
				
				cfnlib.funcFullScreen();
				// FreeSpinEntryScreen string should be yes in excel sheet, if entry screen is present
				String FreeSpinEntryScreen = cfnlib.xpathMap.get("FreeSpinEntryScreen");				
				String b2 =cfnlib.entryScreen_Wait(FreeSpinEntryScreen);
				if(b2.equalsIgnoreCase("freeSpin")) 
				{	wait=new WebDriverWait(webdriver,60);
					
				
					webdriver.navigate().refresh();
					if(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame"))||(Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad"))))
					{
						wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cfnlib.xpathMap.get("OneDesign_NewFeature_ClickToContinue"))));
						cfnlib.newFeature();
						//Thread.sleep(1000);
						//cfnlib.funcFullScreen();
					}
					else
					{
						wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cfnlib.xpathMap.get("clock"))));
						cfnlib.funcFullScreen();
						Thread.sleep(1000);
						cfnlib.newFeature();
					}
					language.detailsAppend("Verify Different Languages on Free spin entry screen","Free spin entry screen should display as per respective language", "", ""); 
					for(int j=1;j<rowCount2;j++){

						//Step 2: To get the languages in MAP and load the language specific url 
						rowData2 =  excelpoolmanager.readExcelByRow(testDataExcelPath,"LanguageCodes", j);
						languageDescription =  rowData2.get("Language").trim(); 
						languageCode = rowData2.get("Language Code").trim();

						//For Progressive games
						if( TestPropReader.getInstance().getProperty("IsProgressiveGame").equalsIgnoreCase("Yes"))
						{
							//fullscreen overlay not present in progressive jackpot bonus hence performing refresh here again  
							if(j==1){
								webdriver.navigate().refresh();
								//click on continue button
								if((Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad"))))
								{
									wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cfnlib.xpathMap.get("OneDesign_NewFeature_ClickToContinue"))));
									cfnlib.newFeature();
								}
								wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cfnlib.xpathMap.get("clock"))));
							}
							cfnlib.elementWait("return "+cfnlib.xpathMap.get("isJackpotBonusContinueButtonVisible"), true);
							System.out.println(cfnlib.getConsoleBooleanText("return "+cfnlib.xpathMap.get("isJackpotBonusContinueButtonVisible")));
							cfnlib.clickAtButton("return "+cfnlib.xpathMap.get("ClickOnJackpotSummaryContinue"));
							
							Thread.sleep(1000);
						}
						
						
						cfnlib.entryScreen_Wait(FreeSpinEntryScreen);
						//cfnlib.funcFullScreen();
						language.detailsAppendFolder("Verify that the application should display Free spin entry screen in " +languageCode+" ","Free spin entry screen should display in " +languageDescription+" ","Free spin entry screen displays in " +languageDescription+ " ", "Pass", languageCode); 
						//for updating language in URL and then Refresh
						if (j + 1 != rowCount2)
						{ 
							rowData3 =  excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j+1); 
							String nextLanguage =  rowData3.get("Language Code").toString().trim();

							String currentUrl = webdriver.getCurrentUrl(); 
							System.out.println("currentUrl"+currentUrl);
							if(currentUrl.contains("LanguageCode"))
								 urlNew = currentUrl.replaceAll("LanguageCode=" + languageCode,"LanguageCode=" + nextLanguage);
							else if(currentUrl.contains("languagecode"))
								urlNew = currentUrl.replaceAll("languagecode=" + languageCode,"languagecode=" + nextLanguage);
							else if(currentUrl.contains("languageCode"))
								urlNew = currentUrl.replaceAll("languageCode=" + languageCode,"languageCode=" + nextLanguage);

							cfnlib.loadGame(urlNew); 
							if(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame"))||(Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad"))))
							{
									wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cfnlib.xpathMap.get("OneDesign_NewFeature_ClickToContinue"))));
									cfnlib.newFeature();
									//Thread.sleep(1000);
									//cfnlib.funcFullScreen();
							} 

						}
						else {
							Thread.sleep(2000);
							rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", 1);
							String nextLanguage = rowData3.get(Constant.LANG_CODE).trim();

							String currentUrl = webdriver.getCurrentUrl();
							if(currentUrl.contains("LanguageCode"))
								 urlNew = currentUrl.replaceAll("LanguageCode=" + languageCode,"LanguageCode=" + nextLanguage);
							else if(currentUrl.contains("languagecode"))
								urlNew = currentUrl.replaceAll("languagecode=" + languageCode,"languagecode=" + nextLanguage);
							else if(currentUrl.contains("languageCode"))
								urlNew = currentUrl.replaceAll("languageCode=" + languageCode,"languageCode=" + nextLanguage);

							cfnlib.loadGame(urlNew);
							if((Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad"))))
							{
								cfnlib.newFeature();
							}
							// click on jackpot summary continue button
							if( TestPropReader.getInstance().getProperty("IsProgressiveGame").equalsIgnoreCase("Yes"))
							{
							cfnlib.clickAtButton("return "+cfnlib.xpathMap.get("ClickOnJackpotSummaryContinue"));
							}
							if("yes".equals(cfnlib.xpathMap.get("isFreeSpinSelectionAvailable")))
							{
								String strfreeSpinSelectionCount=cfnlib.xpathMap.get("NoOfFreeSpinBonusSelection");
								int freeSpinSelectionCount = (int) Double.parseDouble(strfreeSpinSelectionCount);
								
								if(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame")))
								{
									wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cfnlib.xpathMap.get("OneDesign_NewFeature_ClickToContinue"))));
									cfnlib.newFeature();
									cfnlib.funcFullScreen();
								}
								cfnlib.clickBonusSelection(freeSpinSelectionCount);
							}
							else{
								
								if( TestPropReader.getInstance().getProperty("IsProgressiveGame").equalsIgnoreCase("Yes"))
								{
									cfnlib.elementWait("return "+cfnlib.xpathMap.get("isJackpotBonusContinueButtonVisible"), true);
									cfnlib.clickAtButton("return "+cfnlib.xpathMap.get("ClickOnJackpotSummaryContinue"));
									Thread.sleep(6000);
									cfnlib.clickToContinue();
									cfnlib.FS_continue();
									Thread.sleep(4000);
								}
								else{
									//cfnlib.clickToContinue();
									//Click on freespins into continue button
									if("yes".equals(cfnlib.xpathMap.get("isFreeSpinIntroContinueBtnAvailable")))
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
						}
					}
				} 
				else{
					System.out.println("Free Spin Entry screen is not present in Game"); } 
				Thread.sleep(3000);
				cfnlib.FSSceneLoading();

				String startFreeSpinButton = cfnlib.xpathMap.get("StartFreeSpinButton");
				if(startFreeSpinButton.equalsIgnoreCase("Yes")) {
					webdriver.navigate().refresh();
					cfnlib.FSSceneLoading();
					language.detailsAppend("Verify the Different Languages in Free Spin","Free Spin window should display as per respective language", "", "");
					for (int j = 1; j < rowCount2; j++) {
						// Step 2: To get the languages in MAP and load the language specific url
						rowData2 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j);
						languageDescription = rowData2.get("Language").toString();
						languageCode = rowData2.get("Language Code").toString().trim();

						// method for wait and validation.
						cfnlib.FSSceneLoading();
						Thread.sleep(2000);
						language.detailsAppendFolder("Verify that the application should display Free Spin scene in " + languageCode + " ","Free Spin scene should display in " + languageDescription + " ","Free Spin scene displays in " + languageDescription + " ", "Pass", languageCode);
						// for updating language in URL and then Refresh
						if (j + 1 != rowCount2) {
							rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j + 1);
							String nextLanguage = rowData3.get("Language Code").toString().trim();

							String currentUrl = webdriver.getCurrentUrl();
							if(currentUrl.contains("LanguageCode"))
								 urlNew = currentUrl.replaceAll("LanguageCode=" + languageCode,"LanguageCode=" + nextLanguage);
							else if(currentUrl.contains("languagecode"))
								urlNew = currentUrl.replaceAll("languagecode=" + languageCode,"languagecode=" + nextLanguage);
							else if(currentUrl.contains("languageCode"))
								urlNew = currentUrl.replaceAll("languageCode=" + languageCode,"languageCode=" + nextLanguage);

							webdriver.navigate().to(urlNew);
						} else {
							Thread.sleep(2000);
							rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", 1);
							String nextLanguage = rowData3.get("Language Code").toString().trim();

							String currentUrl = webdriver.getCurrentUrl();
							if(currentUrl.contains("LanguageCode"))
								 urlNew = currentUrl.replaceAll("LanguageCode=" + languageCode,"LanguageCode=" + nextLanguage);
							else if(currentUrl.contains("languagecode"))
								urlNew = currentUrl.replaceAll("languagecode=" + languageCode,"languagecode=" + nextLanguage);
							else if(currentUrl.contains("languageCode"))
								urlNew = currentUrl.replaceAll("languageCode=" + languageCode,"languageCode=" + nextLanguage);

							webdriver.navigate().to(urlNew);
						}

					}
					cfnlib.FSSceneLoading();
					cfnlib.FS_Start();
				}
				//wait=new WebDriverWait(webdriver,60);
				Thread.sleep(1000);
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

				cfnlib.FSSceneLoading();
				language.detailsAppend("Verify the Different Languages in Free Spin","Free Spin window should display as per respective language", "", "");
				for (int j = 1; j < rowCount2; j++) {

					// Step 2: To get the languages in MAP and load the language specific url

					rowData2 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j);
					languageDescription = rowData2.get("Language");
					languageCode = rowData2.get("Language Code").trim();

					// method for wait and validation.
					cfnlib.FSSceneLoading();
					Thread.sleep(8000);

					language.detailsAppendFolder("Verify that the application should display Free Spin scene in " + languageCode + " ","Free Spin scene should display in " + languageDescription + " ","Free Spin scene displays in " + languageDescription + " ", "Pass" , languageCode);
					// for updating language in URL and then Refresh
					if (j + 1 != rowCount2) {
						rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",j + 1);
						String nextLanguage = rowData3.get("Language Code").toString().trim();

						String currentUrl = webdriver.getCurrentUrl();
						if(currentUrl.contains("LanguageCode"))
							 urlNew = currentUrl.replaceAll("LanguageCode=" + languageCode,"LanguageCode=" + nextLanguage);
						else if(currentUrl.contains("languagecode"))
							urlNew = currentUrl.replaceAll("languagecode=" + languageCode,"languagecode=" + nextLanguage);
						else if(currentUrl.contains("languageCode"))
							urlNew = currentUrl.replaceAll("languageCode=" + languageCode,"languageCode=" + nextLanguage);

						cfnlib.loadGame(urlNew);
						cfnlib.newFeature();
						cfnlib.funcFullScreen();

					} else {
						Thread.sleep(2000);
						rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", 1);
						String nextLanguage = rowData3.get("Language Code").toString().trim();

						String currentUrl = webdriver.getCurrentUrl();
						if(currentUrl.contains("LanguageCode"))
							 urlNew = currentUrl.replaceAll("LanguageCode=" + languageCode,"LanguageCode=" + nextLanguage);
						else if(currentUrl.contains("languagecode"))
							urlNew = currentUrl.replaceAll("languagecode=" + languageCode,"languagecode=" + nextLanguage);
						else if(currentUrl.contains("languageCode"))
							urlNew = currentUrl.replaceAll("languageCode=" + languageCode,"languageCode=" + nextLanguage);

						cfnlib.loadGame(urlNew);
					}
				}
				cfnlib.newFeature();
				cfnlib.funcFullScreen();
				cfnlib.FSSceneLoading();
				Thread.sleep(4000);
				log.info("Freespin continue button is about to click");
				cfnlib.FS_continue();
				log.info("Freespin continue button is clicked and awaiting for spin Button"); 	
				
				cfnlib.waitSummaryScreen();
				
				webdriver.navigate().refresh();
				language.detailsAppend("Verify the Different Languages in Free Spin Summary Screen",	"Free Spin Summary Screen should display as per respective language", "", "");
				for (int j = 1; j < rowCount2; j++) {

					// Step 2: To get the languages in MAP and load the language specific url
					//rowData2 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j);
					rowData2=list.get(j); 
					languageDescription = rowData2.get("Language").toString();
					languageCode = rowData2.get("Language Code").toString().trim();
					if(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame")))
					{
						wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cfnlib.xpathMap.get("OneDesign_NewFeature_ClickToContinue"))));
					}
					cfnlib.newFeature();
					// method for wait and validation.		
					cfnlib.waitSummaryScreen();
					language.detailsAppendFolder("Verify that the application should display Free Spin Summary in " + languageCode + " ","Free Spin Summary should display in " + languageDescription + " ","Free Spin Summary displays in " + languageDescription + " ", "Pass", languageCode);
					// for updating language in URL and then Refresh
					if (j + 1 != rowCount2) {
						rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",j + 1);
						String nextLanguage = rowData3.get("Language Code").toString().trim();
						String currentUrl = webdriver.getCurrentUrl();
						if(currentUrl.contains("LanguageCode"))
							 urlNew = currentUrl.replaceAll("LanguageCode=" + languageCode,"LanguageCode=" + nextLanguage);
						else if(currentUrl.contains("languagecode"))
							urlNew = currentUrl.replaceAll("languagecode=" + languageCode,"languagecode=" + nextLanguage);
						else if(currentUrl.contains("languageCode"))
							urlNew = currentUrl.replaceAll("languageCode=" + languageCode,"languageCode=" + nextLanguage);

						cfnlib.loadGame(urlNew);
						//cfnlib.func_FullScreen();
					} 
				}
			}else
			{
				log.debug("Unable to copy test data file on the environment hence skipping execution");
				language.detailsAppend("", "", "Unable to copy test data on server,hence skipping futher execution", "fail");
			
			}
			}
		}//TRY
		//-------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error(e.getMessage(),e);
			cfnlib.evalException(e);
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
			Thread.sleep(3000);
		}	
	}
}