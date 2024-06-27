package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

//import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 * This script traverse and capture all the screen shots related to bonus unlock .
 * It reads the test data excel sheet for configured languages.
 * This script need test data  which will trigger bonus on first click and free spins
 * @author sg56207
 * */

public class DesktopRegressionLanguageVerificationBonus {
	Logger log = Logger.getLogger(DesktopRegressionLanguageVerificationBonus.class.getName());
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
		Desktop_HTML_Report language = new Desktop_HTML_Report(webdriver, browserName, filePath, startTime, mstrTCName,
				mstrTCDesc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings, mintStepNo, status,
				gameName);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,language, gameName);


		CommonUtil util = new CommonUtil();
		int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int  cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDDesktop"));
		List<String> copiedFiles=new ArrayList<>();
		RestAPILibrary apiobj=new RestAPILibrary();

		try {
			if (webdriver != null) 
			{
				Map<String, String> rowData2 = null;
				Map<String, String> rowData3 = null;
				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				WebDriverWait wait=new WebDriverWait(webdriver,60);
				String strFileName=TestPropReader.getInstance().getProperty("FreeSpinsTestDataPath");
				File testDataFile=new File(strFileName);

				
				
				
				userName=util.randomStringgenerator();
				if(	util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles))
				{
					log.debug("Test dat is copy in test Server for Username="+userName);

					String urlNew=null;
					String url = cfnlib.XpathMap.get("ApplicationURL");
					String launchURL = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
					log.info("url = " +launchURL);
					cfnlib.loadGame(launchURL);
					log.debug("navigated to url ");


					if(framework.equalsIgnoreCase("Force")){
						cfnlib.setNameSpace();
					}

					cfnlib.newFeature();
					int rowCount2 = excelpoolmanager.rowCount(testDataExcelPath,  Constant.LANG_XL_SHEET_NAME);
					// Reading the language code in to list map
					@SuppressWarnings("rawtypes")
					List<Map> list= util.readLangList();

					log.debug("Total number of Languages configured"+rowCount2);
					// Click on Spin button
					cfnlib.waitForSpinButton();
					Thread.sleep(3000);

					cfnlib.spinclick();
					Thread.sleep(1000);
					cfnlib.elementWait("return "+cfnlib.XpathMap.get("currentScene"), "PLAYING_ACTIVE_BONUS");
					language.detailsAppend("Verify Language translations on Bonus feature","Bonus feature should display as per respective language", "", "" );
					for (int j = 1; j < rowCount2; j++) {
						rowData2 = list.get(j);
						languageDescription = rowData2.get(Constant.LANGUAGE).trim();
						languageCode = rowData2.get(Constant.LANG_CODE).trim();
						log.debug("Waiting after language change,to bonus selction scene to come");
						cfnlib.elementWait("return "+cfnlib.XpathMap.get("currentScene"), "PLAYING_ACTIVE_BONUS");
						log.debug("Wait over");
						Thread.sleep(2000);
						language.detailsAppendFolder("Verify Language translations on Bonus feature in  " + languageCode + " ","Bonus scene should display in " + languageDescription + " ","Bonus scene displays in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
						Thread.sleep(2000);

						// for updating language in URL and then Refresh
						if (j + 1 != rowCount2) {
							log.debug("Going for next lang");
							rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, Constant.LANG_XL_SHEET_NAME,j + 1);
							String nextLanguage = rowData3.get(Constant.LANG_CODE).trim();
							log.debug("read next lang from excel"+ nextLanguage);
							String currentUrl = webdriver.getCurrentUrl();

							if(currentUrl.contains("LanguageCode"))
								urlNew= currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+nextLanguage);
							else if(currentUrl.contains("languagecode"))
								urlNew = currentUrl.replaceAll("languagecode="+languageCode, "languagecode="+nextLanguage);

							cfnlib.loadGame(urlNew);
							if((Constant.YES.equalsIgnoreCase(cfnlib.XpathMap.get("continueBtnOnGameLoad"))))
							{
								cfnlib.newFeature();
							}
							log.debug("Load game with lang ::"+nextLanguage);
						} else {
							log.debug("Going to load eng lang");
							Thread.sleep(2000);
							rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath,  Constant.LANG_XL_SHEET_NAME, 1);
							String nextLanguage = rowData3.get(Constant.LANG_CODE).trim();
							log.debug("read next lang from excel"+ nextLanguage);
							String currentUrl = webdriver.getCurrentUrl();
							if(currentUrl.contains("LanguageCode"))
								urlNew= currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+nextLanguage);
							else if(currentUrl.contains("languagecode"))
								urlNew = currentUrl.replaceAll("languagecode="+languageCode, "languagecode="+nextLanguage);

							cfnlib.loadGame(urlNew);
							if((Constant.YES.equalsIgnoreCase(cfnlib.XpathMap.get("continueBtnOnGameLoad"))))
							{
								cfnlib.newFeature();
							}
							log.debug("Load game with lang ::"+ nextLanguage);
						}
					}

					/*Below code is to unlock the bonus depending upon the NoOfBonusSelection and take screen shot in each language
					 * */

					cfnlib.unlockBonus(language);
					// Check for free spins trigger ,after bonus match 
					if(cfnlib.checkAvilability(cfnlib.XpathMap.get("IsFreeSpinsTrigger")))
					{
						// FreeSpinEntryScreen string should be yes in excel sheet, if entry screen is present
						String freeSpinEntryScreen = cfnlib.XpathMap.get("FreeSpinEntryScreen");				
						String b2 = cfnlib.entryScreen_Wait(freeSpinEntryScreen);
						log.info("FreeSpinEntryScreen response"+b2);
						if(b2.equalsIgnoreCase("freeSpin")) {
							webdriver.navigate().refresh();

							if(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.XpathMap.get("TypeOfGame"))|| (Constant.YES.equalsIgnoreCase(cfnlib.XpathMap.get("continueBtnOnGameLoad"))))
							{
								wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cfnlib.XpathMap.get("OneDesign_NewFeature_ClickToContinue"))));
								cfnlib.newFeature();
								Thread.sleep(1000);
							}
							else
							{
								wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cfnlib.XpathMap.get("clock"))));
								Thread.sleep(1000);
								cfnlib.newFeature();
							}

							language.detailsAppendNoScreenshot("Verify Different Languages on Free spin entry screen","Free spin entry screen should display as per respective language", "", "" );

							//If free spin entry screen present, iterate through each language
							for(int j=1;j<rowCount2;j++){

								//Step 2: To get the languages in MAP and load the language specific url 
								rowData2 = list.get(j);
								languageDescription =  rowData2.get(Constant.LANGUAGE).trim(); 
								languageCode = rowData2.get(Constant.LANG_CODE).trim();
								log.info("FreeSpinEntryScreen response"+languageDescription+" "+languageCode);

								cfnlib.entryScreen_Wait(freeSpinEntryScreen);
								log.info("After wait"+languageDescription+" "+languageCode);
								language.detailsAppendFolder("Verify that the application should display Free spin entry screen in " +languageCode+" ","Free spin entry screen should display in " +languageDescription+" ","Free spin entry screen displays in " +languageDescription+ " ", "Pass", languageCode); 
								//for updating language in URL and then Refresh
								if (j + 1 != rowCount2)
								{ 
									log.info("FreeSpinEntryScreen if block");
									rowData3 = list.get(j+1);
									String nextLanguage =  rowData3.get(Constant.LANG_CODE).trim();

									String currentUrl = webdriver.getCurrentUrl(); 
									if(currentUrl.contains("LanguageCode"))
										urlNew= currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+nextLanguage);
									else if(currentUrl.contains("languagecode"))
										urlNew = currentUrl.replaceAll("languagecode="+languageCode, "languagecode="+nextLanguage);

									cfnlib.loadGame(urlNew); 
									if((Constant.YES.equalsIgnoreCase(cfnlib.XpathMap.get("continueBtnOnGameLoad"))))
									{
										cfnlib.newFeature();
									}

								}
								else {
									log.info("FreeSpinEntryScreen Else block");
									Thread.sleep(2000);
									rowData3 = list.get(1);
									String nextLanguage = rowData3.get(Constant.LANG_CODE).trim();

									String currentUrl = webdriver.getCurrentUrl();
									if(currentUrl.contains("LanguageCode"))
										urlNew= currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+nextLanguage);
									else if(currentUrl.contains("languagecode"))
										urlNew = currentUrl.replaceAll("languagecode="+languageCode, "languagecode="+nextLanguage);

									cfnlib.loadGame(urlNew);
									if((Constant.YES.equalsIgnoreCase(cfnlib.XpathMap.get("continueBtnOnGameLoad"))))
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

						//iterate through each language
						for (int j = 1; j < rowCount2; j++) {
							// Step 2: To get the languages in MAP and load the language specific url
							rowData2=list.get(j);
							languageDescription = rowData2.get(Constant.LANGUAGE).trim();
							languageCode = rowData2.get(Constant.LANG_CODE).trim();
							cfnlib.newFeature();
							// method for wait and validation.
							cfnlib.waitSummaryScreen();

							language.detailsAppendFolder("Verify that the application should display Free Spin Summary in " + languageCode + " ","Free Spin Summary should display in " + languageDescription + " ","Free Spin Summary displays in " + languageDescription + " ", "Pass", languageCode);
							// for updating language in URL and then Refresh
							if (j + 1 != rowCount2) {
								rowData3=list.get(j + 1);
								String nextLanguage = rowData3.get(Constant.LANG_CODE).trim();
								String currentUrl = webdriver.getCurrentUrl();
								if(currentUrl.contains("LanguageCode"))
									urlNew= currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+nextLanguage);
								else if(currentUrl.contains("languagecode"))
									urlNew = currentUrl.replaceAll("languagecode="+languageCode, "languagecode="+nextLanguage);

								cfnlib.loadGame(urlNew);
								if((Constant.YES.equalsIgnoreCase(cfnlib.XpathMap.get("continueBtnOnGameLoad"))))
								{
									cfnlib.newFeature();
								}


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
							if((Constant.YES.equalsIgnoreCase(cfnlib.XpathMap.get("continueBtnOnGameLoad"))))
							{
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
							if((Constant.YES.equalsIgnoreCase(cfnlib.XpathMap.get("continueBtnOnGameLoad"))))
							{
								cfnlib.newFeature();
							}

						}
					}

				}
			}else
			{
				log.debug("Unable to copy test data file on the environment hence skipping execution");
				language.detailsAppend("", "", "Unable to copy test data on server,hence skipping futher execution", "fail");
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
		if(!copiedFiles.isEmpty())
		{
			if(TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
				util.deleteBluemesaTestDataFiles(mid,cid,copiedFiles);
			else
				apiobj.deleteAxiomTestDataFiles(copiedFiles);
		}

		webdriver.close();
		webdriver.quit();
		proxy.abort();
		Thread.sleep(1000);
	}
}


}
