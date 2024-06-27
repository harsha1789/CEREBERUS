package Modules.Regression.TestScript;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
 * This script traverse and capture all the screen shots related to free Spins.
 * It reads the test data excel sheet for configured languages.
 * @author AK47374
 * */

public class Desktop_Regression_Language_Verification_FreeSpin {
	Logger log = Logger.getLogger(Desktop_Regression_Language_Verification_FreeSpin.class.getName());
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
					int rowCount2 = excelpoolmanager.rowCount(testDataExcelPath, "LanguageCodes");
					// Reading the language code in to list map
					@SuppressWarnings("rawtypes")
					List<Map> list= util.readLangList();

					log.debug("Total number of Languages configured"+rowCount2);
					// Click on Spin button
					cfnlib.waitForSpinButton();
					Thread.sleep(3000);
					if(TestPropReader.getInstance().getProperty("IsProgressiveGame").equalsIgnoreCase("Yes")){
						cfnlib.setMaxBet();
					}
					cfnlib.spinclick();
					Thread.sleep(1000);


					//Jackpot Bonus Language check for Progresssive Games 
					//Need testdata which will trigger jackpot in free spin on first spin
					if(TestPropReader.getInstance().getProperty("IsProgressiveGame").equalsIgnoreCase("Yes"))
					{
						language.detailsAppend("Verify Different Languages on Jockpot feature","Jackpot feature should display as per respective language", "", "" );
						for (int j = 1; j < rowCount2; j++) {

							// Step 2: To get the languages in MAP and load the language specific url

							rowData2 = list.get(j);
							languageDescription = rowData2.get(Constant.LANGUAGE).trim();
							languageCode = rowData2.get(Constant.LANG_CODE).trim();
							printTime("Before wait Jackpot scene");
							// method to wait for jackpotscene
							Thread.sleep(2000);
							cfnlib.jackpotSceneWait();
							printTime("Afetr wait Jackpot scene");
							Thread.sleep(12000);
							language.detailsAppendFolder("Verify that the application should display jackpot scene in " + languageCode + " ","jackpot scene should display in " + languageDescription + " ","jackpot scene displays in " + languageDescription + " ", "Pass", languageCode);
							printTime("Afetr first screen shot");
							Thread.sleep(12000);
							language.detailsAppendFolder("Verify that the application should display jackpot scene in " + languageCode + " ","jackpot scene should display in " + languageDescription + " ","jackpot scene displays in " + languageDescription + " ", "Pass", languageCode);
							Thread.sleep(8000);
							printTime("Afetr second screen shot");
							language.detailsAppendFolder("Verify that the application should display jackpot scene in " + languageCode + " ","jackpot scene should display in " + languageDescription + " ","jackpot scene displays in " + languageDescription + " ", "Pass", languageCode);
							printTime("Afetr Third screen shot");

							// for updating language in URL and then Refresh
							if (j + 1 != rowCount2) {
								printTime("Going for next lang");
								rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",j + 1);
								String nextLanguage = rowData3.get(Constant.LANG_CODE).trim();
								printTime("read next lang from excel"+ nextLanguage);
								String currentUrl = webdriver.getCurrentUrl();

								if(currentUrl.contains("LanguageCode"))
									urlNew= currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+nextLanguage);
								else if(currentUrl.contains("languagecode"))
									urlNew = currentUrl.replaceAll("languagecode="+languageCode, "languagecode="+nextLanguage);
								else if(currentUrl.contains("languageCode"))
									urlNew = currentUrl.replaceAll("languageCode=" + languageCode,"languageCode=" + nextLanguage);


								cfnlib.loadGame(urlNew);
								if((Constant.YES.equalsIgnoreCase(cfnlib.XpathMap.get("continueBtnOnGameLoad"))))
								{
									cfnlib.newFeature();
								}
								printTime("Load game with lang ::"+nextLanguage);
							} else {
								printTime("Going to load eng lang");
								Thread.sleep(2000);
								rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", 1);
								String nextLanguage = rowData3.get(Constant.LANG_CODE).trim();
								printTime("read next lang from excel"+ nextLanguage);
								String currentUrl = webdriver.getCurrentUrl();
								if(currentUrl.contains("LanguageCode"))
									urlNew= currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+nextLanguage);
								else if(currentUrl.contains("languagecode"))
									urlNew = currentUrl.replaceAll("languagecode="+languageCode, "languagecode="+nextLanguage);
								else if(currentUrl.contains("languageCode"))
									urlNew = currentUrl.replaceAll("languageCode=" + languageCode,"languageCode=" + nextLanguage);

								cfnlib.loadGame(urlNew);
								if((Constant.YES.equalsIgnoreCase(cfnlib.XpathMap.get("continueBtnOnGameLoad"))))
								{
									cfnlib.newFeature();
								}
								printTime("Load game with lang ::"+ nextLanguage);
							}
						}
						wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cfnlib.XpathMap.get("clock"))));
						Thread.sleep(3000);
						printTime("Going to click jackpot spin");
						cfnlib.clickAtButton("return "+cfnlib.XpathMap.get("ClickOnJackpotSpinButton"));
					//	cfnlib.spinclick();
						Thread.sleep(3000);
						printTime("Going to click Continue :: "+"return "+cfnlib.XpathMap.get("isJackpotBonusContinueButtonVisible"));
						// method to wait for jackpotscene summary screen
						cfnlib.elementWait("return "+cfnlib.XpathMap.get("isJackpotBonusContinueButtonVisible"), true);
						printTime("Continue clicked");
						for (int j = 1; j < rowCount2; j++) {

							// Step 2: To get the languages in MAP and load the language specific url
							rowData2 = list.get(j);
							languageDescription = rowData2.get(Constant.LANGUAGE).trim();
							languageCode = rowData2.get(Constant.LANG_CODE).trim();
							cfnlib.elementWait("return "+cfnlib.XpathMap.get("isJackpotBonusContinueButtonVisible"), true);
							language.detailsAppendFolder("Verify that the application should display jackpot summary screen in " + languageCode + " ","Jakcpot summary screen should display in " + languageDescription + " ","Jackpot summary screen displays in " + languageDescription + " ", "Pass", languageCode);
							// for updating language in URL and then Refresh
							if (j + 1 != rowCount2) {
								rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",j + 1);
								String nextLanguage = rowData3.get(Constant.LANG_CODE).trim();
								String currentUrl = webdriver.getCurrentUrl();
								if(currentUrl.contains("LanguageCode"))
									urlNew= currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+nextLanguage);
								else if(currentUrl.contains("languagecode"))
									urlNew = currentUrl.replaceAll("languagecode="+languageCode, "languagecode="+nextLanguage);
								else if(currentUrl.contains("languageCode"))
									urlNew = currentUrl.replaceAll("languageCode=" + languageCode,"languageCode=" + nextLanguage);

								cfnlib.loadGame(urlNew);
								if((Constant.YES.equalsIgnoreCase(cfnlib.XpathMap.get("continueBtnOnGameLoad"))))
								{
									cfnlib.newFeature();
								}
							} else {
								Thread.sleep(2000);
								rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", 1);
								String nextLanguage = rowData3.get(Constant.LANG_CODE).trim();

								String currentUrl = webdriver.getCurrentUrl();
								if(currentUrl.contains("LanguageCode"))
									urlNew= currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+nextLanguage);
								else if(currentUrl.contains("languagecode"))
									urlNew = currentUrl.replaceAll("languagecode="+languageCode, "languagecode="+nextLanguage);
								else if(currentUrl.contains("languageCode"))
									urlNew = currentUrl.replaceAll("languageCode=" + languageCode,"languageCode=" + nextLanguage);

								cfnlib.loadGame(urlNew);
								if((Constant.YES.equalsIgnoreCase(cfnlib.XpathMap.get("continueBtnOnGameLoad"))))
								{
									cfnlib.newFeature();
								}
							}
						}
						wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cfnlib.XpathMap.get("clock"))));
						Thread.sleep(2000);
						cfnlib.clickAtButton("return "+cfnlib.XpathMap.get("ClickOnJackpotSummaryContinue"));

					}

					// FreeSpinEntryScreen string should be yes in excel sheet, if entry screen is present
					String FreeSpinEntryScreen = cfnlib.XpathMap.get("FreeSpinEntryScreen");				
					String b2 = cfnlib.entryScreen_Wait(FreeSpinEntryScreen);
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

						language.detailsAppend("Verify Different Languages on Free spin entry screen","Free spin entry screen should display as per respective language", "", "" );

						//If free spin entry screen present, iterate through each language
						for(int j=1;j<rowCount2;j++){

							//Step 2: To get the languages in MAP and load the language specific url 
							rowData2 = list.get(j);
							languageDescription =  rowData2.get(Constant.LANGUAGE).trim(); 
							languageCode = rowData2.get(Constant.LANG_CODE).trim();
							log.info("FreeSpinEntryScreen response"+languageDescription+" "+languageCode);

							//For Progressive games
							if( TestPropReader.getInstance().getProperty("IsProgressiveGame").equalsIgnoreCase("Yes"))
							{
								cfnlib.elementWait("return "+cfnlib.XpathMap.get("isJackpotBonusContinueButtonVisible"), true);
								cfnlib.clickAtButton("return "+cfnlib.XpathMap.get("ClickOnJackpotSummaryContinue"));
								Thread.sleep(1000);
							}

							cfnlib.entryScreen_Wait(FreeSpinEntryScreen);
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
								else if(currentUrl.contains("languageCode"))
									urlNew = currentUrl.replaceAll("languageCode=" + languageCode,"languageCode=" + nextLanguage);

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
								else if(currentUrl.contains("languageCode"))
									urlNew = currentUrl.replaceAll("languageCode=" + languageCode,"languageCode=" + nextLanguage);

								cfnlib.loadGame(urlNew);
								if((Constant.YES.equalsIgnoreCase(cfnlib.XpathMap.get("continueBtnOnGameLoad"))))
								{
									cfnlib.newFeature();
								}
								// click on jackpot summary continue button
								if(TestPropReader.getInstance().getProperty("IsProgressiveGame").equalsIgnoreCase("Yes"))
								{
								cfnlib.clickAtButton("return "+cfnlib.XpathMap.get("ClickOnJackpotSummaryContinue"));
								}
								if("yes".equals(cfnlib.XpathMap.get("isFreeSpinSelectionAvailable")))
								{
									String strfreeSpinSelectionCount=cfnlib.XpathMap.get("NoOfFreeSpinBonusSelection");
									int freeSpinSelectionCount = (int) Double.parseDouble(strfreeSpinSelectionCount);

									if(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.XpathMap.get("TypeOfGame")))
									{
										wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cfnlib.XpathMap.get("OneDesign_NewFeature_ClickToContinue"))));
										cfnlib.newFeature();
									}
									cfnlib.clickBonusSelection(freeSpinSelectionCount);
								}
								else{
									if( TestPropReader.getInstance().getProperty("IsProgressiveGame").equalsIgnoreCase("Yes"))
									{
										cfnlib.elementWait("return "+cfnlib.XpathMap.get("isJackpotBonusContinueButtonVisible"), true);
										cfnlib.clickAtButton("return "+cfnlib.XpathMap.get("ClickOnJackpotSummaryContinue"));
										Thread.sleep(8000);
										cfnlib.clickToContinue();
										Thread.sleep(2000);
										cfnlib.FS_continue();
										Thread.sleep(4000);
									}
									else{
										//cfnlib.clickToContinue();
										//Click on freespins into continue button
										if("yes".equals(cfnlib.XpathMap.get("isFreeSpinIntroContinueBtnAvailable")))
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
						log.debug("Free Spin Entry screen is not present in Game"); 
					} 

					Thread.sleep(1000);
					cfnlib.FSSceneLoading();

					// Logic to capture start freespin screen button scene
					String startFreeSpinButton = cfnlib.XpathMap.get("StartFreeSpinButton");
					log.info("startFreeSpinButton Value is ::" +startFreeSpinButton );
					if(startFreeSpinButton.equalsIgnoreCase("Yes")) 
					{
						log.info("startFreeSpin is in progress" );
						webdriver.navigate().refresh();
						cfnlib.FSSceneLoading();
						language.detailsAppend("Verify the Different Languages in Free Spin","Free Spin window should display as per respective language", "", "");
						//iterate through each language
						for (int j = 1; j < rowCount2; j++) {

							// Step 2: To get the languages in MAP and load the language specific url

							rowData2 = list.get(j);
							languageDescription = rowData2.get(Constant.LANGUAGE).trim();
							languageCode = rowData2.get(Constant.LANG_CODE).trim();

							// method for wait and validation.
							cfnlib.FSSceneLoading();

							language.detailsAppendFolder("Verify that the application should display Free Spin scene in " + languageCode + " ","Free Spin scene should display in " + languageDescription + " ","Free Spin scene displays in " + languageDescription + " ", "Pass", languageCode);
							// for updating language in URL and then Refresh
							if (j + 1 != rowCount2) {
								rowData3=list.get(j + 1);
								String nextLanguage = rowData3.get(Constant.LANG_CODE).trim();

								String currentUrl = webdriver.getCurrentUrl();

								if(currentUrl.contains("LanguageCode"))
									urlNew= currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+nextLanguage);
								else if(currentUrl.contains("languagecode"))
									urlNew = currentUrl.replaceAll("languagecode="+languageCode, "languagecode="+nextLanguage);
								else if(currentUrl.contains("languageCode"))
									urlNew = currentUrl.replaceAll("languageCode=" + languageCode,"languageCode=" + nextLanguage);

								cfnlib.loadGame(urlNew);
							} else {
								Thread.sleep(2000);
								rowData3=list.get(1);
								String nextLanguage = rowData3.get(Constant.LANG_CODE).toString().trim();

								String currentUrl = webdriver.getCurrentUrl();
								if(currentUrl.contains("LanguageCode"))
									urlNew= currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+nextLanguage);
								else if(currentUrl.contains("languagecode"))
									urlNew = currentUrl.replaceAll("languagecode="+languageCode, "languagecode="+nextLanguage);
								else if(currentUrl.contains("languageCode"))
									urlNew = currentUrl.replaceAll("languageCode=" + languageCode,"languageCode=" + nextLanguage);

								cfnlib.loadGame(urlNew);
							}
						}
						cfnlib.FSSceneLoading();
						cfnlib.FS_Start();
					}
					Thread.sleep(1000);
					webdriver.navigate().refresh();
					cfnlib.newFeature();
					cfnlib.acceptAlert();
					log.info("After awaiting for free spin page" );
					cfnlib.FSSceneLoading();
					language.detailsAppend("Verify the Different Languages in Free Spin","Free Spin window should display as per respective language", "", "");
					for (int j = 1; j < rowCount2; j++) {

						// Step 2: To get the languages in MAP and load the language specific url

						rowData2 = list.get(j);
						languageDescription = rowData2.get(Constant.LANGUAGE).trim();
						languageCode = rowData2.get(Constant.LANG_CODE).trim();

						// method for wait and validation.
						cfnlib.FSSceneLoading();
						Thread.sleep(8000);
						language.detailsAppendFolder("Verify that the application should display Free Spin scene in " + languageCode + " ","Free Spin scene should display in " + languageDescription + " ","Free Spin scene displays in " + languageDescription + " ", "Pass", languageCode);
						// for updating language in URL and then Refresh
						if (j + 1 != rowCount2) {
							rowData3=list.get(j + 1);
							String nextLanguage = rowData3.get(Constant.LANG_CODE).trim();

							String currentUrl = webdriver.getCurrentUrl();
							if(currentUrl.contains("LanguageCode"))
								urlNew= currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+nextLanguage);
							else if(currentUrl.contains("languagecode"))
								urlNew = currentUrl.replaceAll("languagecode="+languageCode, "languagecode="+nextLanguage);
							else if(currentUrl.contains("languageCode"))
								urlNew = currentUrl.replaceAll("languageCode=" + languageCode,"languageCode=" + nextLanguage);

							webdriver.navigate().to(urlNew);
							cfnlib.newFeature();
							String error=cfnlib.XpathMap.get("Error");
							/*if(cfnlib.isElementPresent(error))
						{
							language.details_append("Verify the Language code is " +languageCode+" ", " Application window should be displayed in " +languageDescription+"", "", "");
							language.details_append("Verify that any error is coming","error must not come","error is coming", "fail");
							if (j + 2 != rowCount2){
								rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",  j+2);
								String languageCode3 = rowData3.get(Constant.LANG_CODE).toString().trim();

								currentUrl = webdriver.getCurrentUrl();
								urlNew = currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+languageCode3);
								webdriver.navigate().to(urlNew);
							}
							j++;
						}*/
						} else {
							Thread.sleep(2000);
							rowData3=list.get(1);
							String nextLanguage = rowData3.get(Constant.LANG_CODE).trim();
							String currentUrl = webdriver.getCurrentUrl();
							if(currentUrl.contains("LanguageCode"))
								urlNew= currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+nextLanguage);
							else if(currentUrl.contains("languagecode"))
								urlNew = currentUrl.replaceAll("languagecode="+languageCode, "languagecode="+nextLanguage);
							else if(currentUrl.contains("languageCode"))
								urlNew = currentUrl.replaceAll("languageCode=" + languageCode,"languageCode=" + nextLanguage);

							cfnlib.loadGame(urlNew);

							String error=cfnlib.XpathMap.get("Error");
							/*if(cfnlib.isElementPresent(error))
						{
							language.details_append("Verify the Language code is " +languageCode+" ", " Application window should be displayed in " +languageDescription+"", "", "");
							language.details_append("Verify that any error is coming","error must not come","error is coming", "fail");
							if (j + 2 != rowCount2){
								rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",  j+2);
								String languageCode3 = rowData3.get(Constant.LANG_CODE).toString().trim();

								currentUrl = webdriver.getCurrentUrl();
								url_new = currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+languageCode3);
								webdriver.navigate().to(url_new);
							}
							j++;
						}*/
						}

					}
					cfnlib.newFeature();
					cfnlib.FSSceneLoading();
					Thread.sleep(5000);
					log.info("Freespin continue button is about to click");
					cfnlib.FS_continue();

					log.info("Freespin continue button is clicked and awaiting for spin Button");
					Thread.sleep(5000);
					cfnlib.waitSummaryScreen();
					Thread.sleep(2000);
					// Collects the screen shot for the summary screen
					webdriver.navigate().refresh();				
					cfnlib.acceptAlert();
					printTime("before EN ");
					language.detailsAppend("Verify the Different Languages in Free Spin Summary Screen","Free Spin Summary Screen should display as per respective language", "", "");

					//iterate through each language
					for (int j = 1; j < rowCount2; j++) {
						printTime("before new feature"+ languageCode);
						// Step 2: To get the languages in MAP and load the language specific url
						rowData2=list.get(j);
						languageDescription = rowData2.get(Constant.LANGUAGE).trim();
						languageCode = rowData2.get(Constant.LANG_CODE).trim();
						cfnlib.newFeature();
						printTime("after new feature"+ languageCode);
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
							else if(currentUrl.contains("languageCode"))
								urlNew = currentUrl.replaceAll("languageCode=" + languageCode,"languageCode=" + nextLanguage);

							cfnlib.loadGame(urlNew);
							/*if((Constant.YES.equalsIgnoreCase(cfnlib.XpathMap.get("continueBtnOnGameLoad"))))
							{
								cfnlib.newFeature();
							}*/
							if(!framework.equalsIgnoreCase("Force")){
								String error=cfnlib.XpathMap.get("Error");
								/*if(cfnlib.isElementPresent(error))
						{
							language.details_append("Verify the Language code is " +languageCode+" ", " Application window should be displayed in " +languageDescription+"", "", "");
							language.details_append("Verify that any error is coming","error must not come","error is coming", "fail");
							if (j + 2 != rowCount2){
								//rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",  j+2);
								rowData3=list.get(j+2);
								String languageCode3 = rowData3.get(Constant.LANG_CODE).toString().trim();

								currentUrl = webdriver.getCurrentUrl();
								url_new = currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+languageCode3);
								webdriver.navigate().to(url_new);
							}
							j++;
						}*/
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


	public void printTime(String text)
	{
		Calendar cal = Calendar.getInstance();
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String stringDate2 = sdf.format(cal.getTime());
		System.out.println(text + stringDate2);
	}
}
