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
 * This script traverse and capture all the screen shots related to four free Spins.
 * It reads the test data excel sheet for configured languages.
 * @author SV65878
 * */

public class Desktop_Regression_Language_Verification_Compatibility_All_FreeSpin {
	Logger log = Logger.getLogger(Desktop_Regression_Language_Verification_Compatibility_All_FreeSpin.class.getName());
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
		String status=null;
		String urlNew=null;
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
				String strFileName=TestPropReader.getInstance().getProperty("FourFreeSpinsTestDataPath");
				File testDataFile=new File(strFileName);
				
				System.out.println("comatibility");

				userName=util.randomStringgenerator();
				if(	util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles))
				{
					log.debug("Test dat is copy in test Server for Username="+userName);

					
					
					String url = cfnlib.XpathMap.get("ApplicationURL");
					
					String launchURL = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
					
					log.info("url = " +launchURL);
					
					cfnlib.loadGame(launchURL);
					
					log.debug("navigated to url ");
					
                        Thread.sleep(8000);

					if(framework.equalsIgnoreCase("Force")){
						cfnlib.setNameSpace();
					}
					
                        Thread.sleep(2000);
                     
					
					int rowCount2 = excelpoolmanager.rowCount(testDataExcelPath, "LanguageCodes");
					// Reading the language code in to list map
					@SuppressWarnings("rawtypes")
					List<Map> list= util.readLangList();

					log.debug("Total number of Languages configured"+rowCount2);
					
					for (int j = 1; j < rowCount2; j++) {

						// Step 2: To get the languages in MAP and load the language specific url

						rowData2 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j);
						String languageDescription = rowData2.get("Language").toString();
						String languageCode = rowData2.get("Language Code").toString().trim();

						cfnlib.closeOverlay();
					    log.debug("Total number of Languages configured" + rowCount2);
						
						cfnlib.waitForSpinButton();
						
						Thread.sleep(1000);
						
						cfnlib.spinclick();
						
						Thread.sleep(2000);

						cfnlib.elementWait("return " + cfnlib.XpathMap.get("FreeScrinVisible"), true);

						boolean UnlockStatus = cfnlib.UnlockAllFreeSpin();
						
						System.out.println(UnlockStatus);
						
                       if (UnlockStatus) {
							
							log.info("Screenshot for  ALL Freespin Feature Unlocking Status");
							
							language.detailsAppend(
									"Verify Different Languages on Free spin entry screen All Feature is Unlocked",
									"Free spin entry screen should display All Feature Unlocked as per respective language",
									"", "");

							language.detailsAppendFolder(
									"Verify that the application should display Free spin entry screen status  All Feature Unlocked in "
											+ languageCode + " ",
									"Free spin entry screen should display in " + languageDescription
											+ "With All Feature Unlocked" + " ",
									"Free spin entry screen displays in " + languageDescription + " "
											+ "With All Feature Unlocked ",
									"Pass", languageCode);

							cfnlib.resizeBrowser(1200, 900);

							Thread.sleep(1000);

							language.detailsAppendFolder(
									"Verify that the application should display Free spin entry screen status All Feature Unlocked After Resize "
											+ languageCode + " ",
									"Free spin entry screen should display in " + languageDescription
											+ "With All Feature Unlocked After Resize" + " ",
									"Free spin entry screen displays in " + languageDescription + " "
											+ "With All Feature Unlocked ",
									"Pass", languageCode);

							cfnlib.maxiMizeBrowser();
							
							Thread.sleep(2000);

							log.info("Screenshot done  ALL Freespin Feature Unlocking Status");

							for (int i = 1; i <= 4; i++) {

                                Thread.sleep(1000);	
                                
                                language.detailsAppend("Verify the Different Languages in Free Spin For ",
										"Free Spin window should display as per respective language", "", "");
                                
								cfnlib.clickBonusSelection(i);
								
								cfnlib.FSSceneLoading();
								
								webdriver.navigate().refresh();
								
								Thread.sleep(2000);
								
								cfnlib.elementWait("return " + cfnlib.XpathMap.get("CntBtnNoXpathSatus"), true);
								
								Thread.sleep(1000);
								
								cfnlib.closeOverlay();
								Thread.sleep(1000);
								
								cfnlib.closeOverlay();
								
								cfnlib.FSSceneLoading();
								Thread.sleep(2000);
								language.detailsAppendFolder(
										"Verify that the application should display Free Spin scene in " + languageCode
												+ " " + "For " +i +" "
												+ " Feature ",
										"Free Spin scene should display in " + languageDescription + " " + "For "
												+i +" " + " Feature ",
										"Free Spin scene displays in " + languageDescription + " " + "For "
												+ +i +" "+ " Feature ",
										"Pass", languageCode);

								cfnlib.resizeBrowser(1200, 900);

								Thread.sleep(1000);

								language.detailsAppendFolder(
										"Verify that the application should display Free Spin scene After Resize Window"
												+ languageCode + " " + "For "
												+i +" " + " Feature ",
										"Free Spin scene should display in " + languageDescription + " " + "For "
												+i +" " + " Feature ",
										"Free Spin scene displays in " + languageDescription + " " + "For "
												+i +" " + " Feature ",
										"Pass", languageCode);

								cfnlib.maxiMizeBrowser();

								language.detailsAppend("Verify the Different Languages in Free Spin Summary Screen",
										"Free Spin Summary Screen should display as per respective language", "", "");
								
								
								Thread.sleep(1000);
								log.info("Freespin continue button is about to click");
								cfnlib.FS_continue();
								
								log.info("Freespin continue button is clicked and awaiting for spin Button");
								
								Thread.sleep(3000);
								
								if(i>=2) 
								{
									Thread.sleep(i*3000);
								}
								
								
								cfnlib.waitSummaryScreen();
								
							   Thread.sleep(1000);
								
								
								language.detailsAppendFolder(
										"Verify that the application should display Free Spin Summary in "
												+ languageCode + " " + "For "
												+i +" " + " Feature ",
										"Free Spin Summary should display in " + languageDescription + " " + "For "
												+i +" "+ " Feature ",
										"Free Spin Summary displays in " + languageDescription + " " + "For "
												+i +" " + " Feature ",
										"Pass", languageCode);
								
								cfnlib.resizeBrowser(1200, 900);
								
								language.detailsAppendFolder(
										"Verify that the application should display Free Spin Summary in  After Resize Window"
												+ languageCode + " " + "For "
												+i +" " + " Feature ",
										"Free Spin Summary should display in " + languageDescription + " " + "For "
												+i +" "+ " Feature ",
										"Free Spin Summary displays in " + languageDescription + " " + "For "
												+i +" " + " Feature ",
										"Pass", languageCode);
								
                                   cfnlib.maxiMizeBrowser();

								
							
								Thread.sleep(4000);

								cfnlib.waitForSpinButton();

								if (i <= 3) 
								{
									cfnlib.spinclick();

									Thread.sleep(5000);

									cfnlib.elementWait("return " + cfnlib.XpathMap.get("FreeScrinVisible"), true);
								}
							}
						} 
						else {
							
							System.out.println("Feacture is not unlocked");
							
						}

                // Language Change logic:: for updating language in URL and then Refresh

					if (j + 1 != rowCount2) {
						rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j + 1);
						languageDescription = rowData3.get("Language").trim();
						String languageCode2 = rowData3.get("Language Code").trim();

						String currentUrl = webdriver.getCurrentUrl();
						if (currentUrl.contains("LanguageCode"))
							urlNew = currentUrl.replaceAll("LanguageCode=" + languageCode,
									"LanguageCode=" + languageCode2);
						else if (currentUrl.contains("languagecode"))
							urlNew = currentUrl.replaceAll("languagecode=" + languageCode,
									"languagecode=" + languageCode2);
						else if (currentUrl.contains("languageCode"))
							urlNew = currentUrl.replaceAll("languageCode=" + languageCode,
									"languageCode=" + languageCode2);

						log.info("Privious Language Code in Url= " + languageCode + "\nNext Language code= "
								+ languageCode2 + "\nNew Url after replacing language code:" + urlNew);
						cfnlib.loadGame(urlNew);
						String error = cfnlib.XpathMap.get("Error");

						if (cfnlib.isElementPresent(error)) {
							language.detailsAppendFolder("Verify the Language code is " + languageCode2 + " ",
									" Application window should be displayed in " + languageDescription + "", "",
									"", languageCode2);
							language.detailsAppendFolder("Verify that any error is coming",
									"General error should not display", "General Error is Diplay", "fail",
									languageCode2);

							if (j + 2 != rowCount2) {
								rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",
										j + 2);
								String languageCode3 = rowData3.get("Language Code").toString().trim();
								currentUrl = webdriver.getCurrentUrl();
								if (currentUrl.contains("LanguageCode"))
									urlNew = currentUrl.replaceAll("LanguageCode=" + languageCode2,
											"LanguageCode=" + languageCode3);
									else if (currentUrl.contains("languagecode"))
										urlNew = currentUrl.replaceAll("languagecode=" + languageCode2,
												"languagecode=" + languageCode3);
									else if (currentUrl.contains("languageCode"))
										urlNew = currentUrl.replaceAll("languageCode=" + languageCode2,
												"languageCode=" + languageCode3);

									cfnlib.loadGame(urlNew);
								}

							}
							j++;
						}
					

				}
			}
				else
				{
					log.debug("Unable to copy test data file on the environment hence skipping execution");
					language.detailsAppend("", "", "Unable to copy test data on server,hence skipping futher execution", "fail");
				}
				
			}

		  }catch (Exception e) {
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