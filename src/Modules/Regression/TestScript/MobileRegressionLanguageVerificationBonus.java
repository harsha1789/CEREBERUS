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
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 * This script traverse and capture all the screen shots related to bonus .
 * It reads the test data excel sheet for configured languages.
 * @author sg56207
 * */
public class MobileRegressionLanguageVerificationBonus {
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
					log.debug("Test data is copy in test Server for Username="+userName);


					String url = cfnlib.xpathMap.get("ApplicationURL");
					String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
					log.debug("url = " +LaunchURl);
					if("LANDSCAPE".equalsIgnoreCase(cfnlib.xpathMap.get("Orientation")))
					{webdriver.context("NATIVE_APP");
					webdriver.rotate(ScreenOrientation.LANDSCAPE);
					webdriver.context("CHROMIUM");
					}	
					if(	cfnlib.loadGame(LaunchURl))
					{

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
							cfnlib.funcFullScreen();
							Thread.sleep(1000);
							cfnlib.newFeature();
						}
						else
						{
							cfnlib.funcFullScreen();
							Thread.sleep(1000);
							cfnlib.newFeature();
						}
						cfnlib.waitForSpinButton();

						cfnlib.spinclick();
						Thread.sleep(1000);
						if(cfnlib.elementWait("return "+cfnlib.xpathMap.get("currentScene"), "PLAYING_ACTIVE_BONUS"))
						{
							language.detailsAppend("Verify Language translations on Bonus feature","Bonus feature should display as per respective language", "", "" );


							for (int j = 1; j < rowCount2; j++) {

								// Step 2: To get the languages in MAP and load the language specific url
								rowData2=list.get(j);
								languageDescription = rowData2.get(Constant.LANGUAGE).trim();
								languageCode = rowData2.get(Constant.LANG_CODE).trim();

								log.debug("Waiting after language change,to bonus selction scene to come");
								cfnlib.elementWait("return "+cfnlib.xpathMap.get("currentScene"), "PLAYING_ACTIVE_BONUS");
								log.debug("Wait over");
								Thread.sleep(2000);
								language.detailsAppendFolder("Verify Language translations on Bonus feature in  " + languageCode + " ","Bonus scene should display in " + languageDescription + " ","Bonus scene displays in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
								Thread.sleep(2000);
								// for updating language in URL and then Refresh
								if (j + 1 != rowCount2) {
									rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",j + 1);
									String nextLanguage = rowData3.get("Language Code").trim();
									String currentUrl = webdriver.getCurrentUrl();
									if(currentUrl.contains("LanguageCode"))
										urlNew = currentUrl.replaceAll("LanguageCode=" + languageCode,"LanguageCode=" + nextLanguage);
									else if(currentUrl.contains("languagecode"))
										urlNew = currentUrl.replaceAll("languagecode=" + languageCode,"languagecode=" + nextLanguage);



									cfnlib.loadGame(urlNew);
									if((Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad"))))
									{
										cfnlib.funcFullScreen();
										Thread.sleep(1000);
										cfnlib.newFeature();
									}
									log.debug("Load game with lang ::"+nextLanguage);
									
								} else {
									log.debug("Going to load eng lang");
									Thread.sleep(2000);
									rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", 1);
									String nextLanguage = rowData3.get("Language Code").trim();

									String currentUrl = webdriver.getCurrentUrl();
									if(currentUrl.contains("LanguageCode"))
										urlNew = currentUrl.replaceAll("LanguageCode=" + languageCode,"LanguageCode=" + nextLanguage);
									else if(currentUrl.contains("languagecode"))
										urlNew = currentUrl.replaceAll("languagecode=" + languageCode,"languagecode=" + nextLanguage);

									cfnlib.loadGame(urlNew);
									if((Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad"))))
									{
										cfnlib.funcFullScreen();
										Thread.sleep(2000);
										cfnlib.newFeature();
									}
									log.debug("Load game with lang ::"+ nextLanguage);
								}
							}

							wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cfnlib.xpathMap.get("clock"))));
							
							

							/*Below code is to unlock the bonus depending upon the NoOfBonusSelection and take screen shot in each language
							 * */

							cfnlib.unlockBonus(language);
							// Check for free spins trigger ,after bonus match 

							if(cfnlib.checkAvilability(cfnlib.xpathMap.get("IsFreeSpinsTrigger")))
							{
								// FreeSpinEntryScreen string should be yes in excel sheet, if entry screen is present
								String freeSpinEntryScreen = cfnlib.xpathMap.get("FreeSpinEntryScreen");				
								String b2 =cfnlib.entryScreen_Wait(freeSpinEntryScreen);
								if(b2.equalsIgnoreCase("freeSpin")) 
								{	wait=new WebDriverWait(webdriver,60);


								webdriver.navigate().refresh();
								if(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame"))||(Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad"))))
								{
									wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cfnlib.xpathMap.get("OneDesign_NewFeature_ClickToContinue"))));
									cfnlib.funcFullScreen();
									cfnlib.newFeature();
									
								}
								else
								{
									wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cfnlib.xpathMap.get("clock"))));
									cfnlib.funcFullScreen();
									Thread.sleep(1000);
									cfnlib.newFeature();
								}
								language.detailsAppendNoScreenshot("Verify Different Languages on Free spin entry screen","Free spin entry screen should display as per respective language", "", "" );
								for(int j=1;j<rowCount2;j++){

									//Step 2: To get the languages in MAP and load the language specific url 
									rowData2 =  excelpoolmanager.readExcelByRow(testDataExcelPath,"LanguageCodes", j);
									languageDescription =  rowData2.get("Language").trim(); 
									languageCode = rowData2.get("Language Code").trim();


									cfnlib.entryScreen_Wait(freeSpinEntryScreen);
									language.detailsAppendFolder("Verify that the application should display Free spin entry screen in " +languageCode+" ","Free spin entry screen should display in " +languageDescription+" ","Free spin entry screen displays in " +languageDescription+ " ", "Pass", languageCode); 
									//for updating language in URL and then Refresh
									if (j + 1 != rowCount2)
									{ 
										rowData3 =  excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j+1); 
										String nextLanguage =  rowData3.get("Language Code").toString().trim();

										String currentUrl = webdriver.getCurrentUrl(); 
										if(currentUrl.contains("LanguageCode"))
											urlNew = currentUrl.replaceAll("LanguageCode=" + languageCode,"LanguageCode=" + nextLanguage);
										else if(currentUrl.contains("languagecode"))
											urlNew = currentUrl.replaceAll("languagecode=" + languageCode,"languagecode=" + nextLanguage);

										cfnlib.loadGame(urlNew); 
										if(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame"))||(Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad"))))
										{
											wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cfnlib.xpathMap.get("OneDesign_NewFeature_ClickToContinue"))));
											cfnlib.newFeature();
										} 

									}
									else {

										log.info("FreeSpinEntryScreen Else block");
										Thread.sleep(2000);
										rowData3 =list.get(1);
										String nextLanguage = rowData3.get(Constant.LANG_CODE).trim();

										String currentUrl = webdriver.getCurrentUrl();
										if(currentUrl.contains("LanguageCode"))
											urlNew = currentUrl.replaceAll("LanguageCode=" + languageCode,"LanguageCode=" + nextLanguage);
										else if(currentUrl.contains("languagecode"))
											urlNew = currentUrl.replaceAll("languagecode=" + languageCode,"languagecode=" + nextLanguage);

										cfnlib.loadGame(urlNew);
										if((Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad"))))
										{
											cfnlib.newFeature();
										}
										cfnlib.clickToContinue();

									}
								}
								} 
								else{
									log.debug("Free Spin Entry screen is not present in Game"); 
								}
								Thread.sleep(1000);
								cfnlib.FSSceneLoading();

								cfnlib.waitSummaryScreen();
								language.detailsAppendNoScreenshot("Verify Different Languages on Free summary screen","Free spin entry screen should display as per respective language,verify screenshots", "", "" );
								webdriver.navigate().refresh();
								for (int j = 1; j < rowCount2; j++) {

									// Step 2: To get the languages in MAP and load the language specific url
									rowData2=list.get(j); 
									languageDescription = rowData2.get("Language").toString();
									languageCode = rowData2.get("Language Code").toString().trim();

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

										cfnlib.loadGame(urlNew);
											
									} 
								}
							}else
							{

								language.detailsAppend("Verify language translation on bonus summary scene ","", "", "" );
								for(int j=1;j<rowCount2;j++){

									//Step 2: To get the languages in MAP and load the language specific url 
									rowData2 = list.get(j);
									languageDescription =  rowData2.get(Constant.LANGUAGE).trim(); 
									languageCode = rowData2.get(Constant.LANG_CODE).trim();
									log.info("bonus Screen response"+languageDescription+" "+languageCode);

									log.info("After wait"+languageDescription+" "+languageCode);
									language.detailsAppendFolder("Verify bonus summary screen in " +languageCode+" ","Bonus summary screen should display in " +languageDescription+" ","Bonus summary screen displays in " +languageDescription+ " ", "Pass", languageCode); 
									//for updating language in URL and then Refresh
									if (j + 1 != rowCount2)
									{ 
										log.info("Bonus Screen if block");
										rowData3 = list.get(j+1);
										String nextLanguage =  rowData3.get(Constant.LANG_CODE).trim();

										String currentUrl = webdriver.getCurrentUrl(); 
										if(currentUrl.contains("LanguageCode"))
											urlNew= currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+nextLanguage);
										else if(currentUrl.contains("languagecode"))
											urlNew = currentUrl.replaceAll("languagecode="+languageCode, "languagecode="+nextLanguage);

										cfnlib.loadGame(urlNew); 
										if((Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad"))))
										{
											cfnlib.funcFullScreen();
											Thread.sleep(1000);
											cfnlib.newFeature();
										}
										
									}
									else {
										log.info("Bonus Screen Else block");
										Thread.sleep(2000);
										rowData3 = list.get(1);
										String nextLanguage = rowData3.get(Constant.LANG_CODE).trim();

										String currentUrl = webdriver.getCurrentUrl();
										if(currentUrl.contains("LanguageCode"))
											urlNew= currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+nextLanguage);
										else if(currentUrl.contains("languagecode"))
											urlNew = currentUrl.replaceAll("languagecode="+languageCode, "languagecode="+nextLanguage);

										cfnlib.loadGame(urlNew);
										if((Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad"))))
										{
											cfnlib.newFeature();
										}

									}
								}


							}
						}else
						{
							language.detailsAppend("verify Bonus trigger or not", "Bonus should triggerd", "no bonus trigger ,hense skipping the test case", "fail");
						}
					}else
					{
						language.detailsAppend("Verify game url launched", "Game url should open", "unable to load game url", "fail");
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
			Thread.sleep(1000);
		}	
	}
}