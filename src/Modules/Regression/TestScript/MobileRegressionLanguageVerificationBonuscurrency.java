package Modules.Regression.TestScript;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
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
 * It reads the test data excel sheet for configured languages and currency
 * This require a test data which will trigger bonus.
 * @author sg56207
 * */
public class MobileRegressionLanguageVerificationBonuscurrency {
	public static Logger log = Logger.getLogger(MobileRegressionLanguageVerificationBonuscurrency.class.getName());
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
		String isoId=null;
		String isoCode=null;
		String isoName = null;
		String languageCurrency = null;
        String currencyFormat=null;

		String status=null;
		int mintDetailCount=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		String languageCode=null;
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
				String urlNew=null;
				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				wait=new WebDriverWait(webdriver,60);

				String strFileName=TestPropReader.getInstance().getProperty("MobileFreeSpinsTestDataPath");
				File testDataFile=new File(strFileName);


				int rowCount2 = excelpoolmanager.rowCount(testDataExcelPath, Constant.CURRENCY_XL_SHEET_NAME);

				List<Map<String,String>> curList= util.readCurrList();

				String strbonusCount=cfnlib.xpathMap.get("NoOfBonusSelection");
				int bonusCount=(int) Double.parseDouble(strbonusCount);

				String selectBonus=cfnlib.xpathMap.get("SelectBonus");
				Map <String, String> paramMap=new HashMap<>();


				for(int j=1;j<rowCount2;j++){

					userName=util.randomStringgenerator();
					rowData2 = curList.get(j);
					isoId = rowData2.get(Constant.ID).trim();
					isoCode = rowData2.get(Constant.ISOCODE).trim().replace("\u00A0", "");;
					isoName = rowData2.get(Constant.ISONAME).trim().replace("\u00A0", "");;
					languageCurrency = rowData2.get(Constant.LANGUAGECURRENCY).trim();
					currencyFormat=rowData2.get(Constant.DISPALYFORMAT).trim();	                


					userName=util.randomStringgenerator();
					if(	util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles,isoId))
					{
						log.debug("Test data is copy in test Server for Username="+userName);


						String url = cfnlib.xpathMap.get("ApplicationURL");
						String launchURL = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
						//condition added for titan and v 
						if(launchURL.contains("LanguageCode"))
							urlNew = launchURL.replaceAll("\\bLanguageCode=.*?(&|$)", "LanguageCode="+languageCurrency+"$1");

						else if(launchURL.contains("languagecode"))
							urlNew = launchURL.replaceAll("\\blanguagecode=.*?(&|$)", "languagecode="+languageCurrency+"$1");


						log.debug("url = " +urlNew);
						if("LANDSCAPE".equalsIgnoreCase(cfnlib.xpathMap.get("Orientation")))
						{
							String context=webdriver.getContext();

							webdriver.context("NATIVE_APP");
							webdriver.rotate(ScreenOrientation.LANDSCAPE);
							webdriver.context(context);
						}	
						if(	cfnlib.loadGame(urlNew))
						{

							if(framework.equalsIgnoreCase("Force")){
								cfnlib.setNameSpace();
							}


							// Reading the language code into the list map
							Thread.sleep(2000);
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
							language.detailsAppend("Verify translations on Bonus feature","Bonus feature should display as per respective language and currency", "", "" );

							String currencySymbol = cfnlib.getCurrencySymbol();
							log.debug(isoName + "currency symbol is " +currencySymbol);
							cfnlib.spinclick();
							Thread.sleep(1000);

							if(cfnlib.elementWait("return "+cfnlib.xpathMap.get("currentScene"), "PLAYING_ACTIVE_BONUS"))
							{
								Thread.sleep(2000);

								language.detailsAppendFolder("Verify Language translations on Bonus feature in  " + isoCode+"Currency and in "+languageCurrency + " ","Bonus scene should display in " + isoCode+"Currency and in "+languageCurrency + " ","Bonus scene displays in " + isoCode+"Currency and in "+languageCurrency + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);


								/*Below code is to unlock the bonus depending upon the NoOfBonusSelection and take screen shot in each language
								 * */

								//cfnlib.unlockBonus(language);

								for(int iteamCnt=0;iteamCnt<bonusCount;iteamCnt++)
								{
									paramMap.put("param1",Integer.toString(iteamCnt));
									String newSelectBonusHook=cfnlib.replaceParamInHook(selectBonus,paramMap);
									String clickOnBonus="return "+newSelectBonusHook;
									cfnlib.clickAtButton(clickOnBonus);
									Thread.sleep(3000);
									boolean isBonusFormat=cfnlib.bonusSelection(currencyFormat,iteamCnt);
									if(isBonusFormat)
									{
										language.detailsAppendFolder("Verify Language translations on Bonus feature in  "+ isoCode+" Currency and "+ languageCurrency + " ","Bonus scene should display in " + isoCode+" Currency and in "+ languageCurrency + " ","Bonus scene displays in " + isoCode+" Currency and in "+ languageCurrency + " ,verify screenshot ", "Pass", isoCode+"_"+languageCurrency);
										System.out.println("Book "+iteamCnt+" currency format is correct");
										log.debug("Book "+iteamCnt+" currency format is correct");
									}
									else
									{
										language.detailsAppendFolder("Verify Language translations on Bonus feature in  "+ isoCode+" Currency and "+ languageCurrency + " ","Bonus scene should display in " + isoCode+" Currency and in "+ languageCurrency + " ","Bonus scene displays in " + isoCode+" Currency and in "+ languageCurrency + " ,verify screenshot ", "fail", isoCode+"_"+languageCurrency);
										System.out.println("Book "+iteamCnt+" currency format is incorrect");
										log.debug("Book "+iteamCnt+" currency format is incorrect");
									}
									System.out.println("bonus selection of book "+iteamCnt+" is Done");
									cfnlib.elementWait("return "+cfnlib.xpathMap.get("currentScene"), "PLAYING_ACTIVE_BONUS");
								}
							
								Thread.sleep(10000);

								
								//Verifying bonus game win currency format
								if(cfnlib.checkAvilability(cfnlib.xpathMap.get("IsBonusContinueBtnVisible")))
								{
									boolean bonuscurrency=cfnlib.bonusWinCurrFormat(currencyFormat);
									Thread.sleep(2000);
									if (bonuscurrency)  {
										language.detailsAppendFolder("Verify currency when bonus game win occurs",
												"Bonus game win should display in "+ currencySymbol +" currency symbol" ,
												"Bonus game win displaying in "+ currencySymbol +" with currency ","Pass",isoCode+"_"+languageCurrency);
										
										} else {
											language.detailsAppendFolder("Verify currency when bonus game win occurs",
													"Bonus game win should display in "+ currencySymbol +" currency symbol" ,
													"Bonus game win displaying in "+ currencySymbol +" with currency ","fail",isoCode+"_"+languageCurrency);
									}
								}
								// Check for free spins trigger ,after bonus match 

								if(cfnlib.checkAvilability(cfnlib.xpathMap.get("IsFreeSpinsTrigger")))
								{
									// FreeSpinEntryScreen string should be yes in excel sheet, if entry screen is present
									String freeSpinEntryScreen = cfnlib.xpathMap.get("FreeSpinEntryScreen");				
									String b2 =cfnlib.entryScreen_Wait(freeSpinEntryScreen);
									if(b2.equalsIgnoreCase("freeSpin")) 
									{	
										language.detailsAppendFolder("Verify  Bonus Free spin entry screen  "+ isoCode+"Currency and in "+ languageCurrency + " ","Free spin entry should display in " + isoCode+"Currency and in "+ languageCurrency + " ","Bonus free spin entry displays in " + isoCode+"Currency and in "+ languageCurrency + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);
										cfnlib.clickContinueInBonusGame();
										Thread.sleep(2000);

										cfnlib.FSSceneLoading();
										language.detailsAppendFolder("Verify  Bonus  free spin scene "+ isoCode+"Currency and "+ languageCurrency + " ","Bonus free spin scene should display in " + isoCode+" Currency and in "+ languageCurrency + " ","Bonus free spin scene displays in " + isoCode+" Currency and in "+ languageCurrency + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);

										cfnlib.waitSummaryScreen();
										Thread.sleep(3000);
										language.detailsAppendFolder("Verify  Bonus  summary screen  "+ isoCode+"Currency and in "+ languageCurrency + " ","Bonus summary should display in " + isoCode+"Currency and in "+ languageCurrency + " ","Bonus summary displays in " + isoCode+"Currency and in "+ languageCurrency + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);

									} 
									else{
										log.debug("Free Spin Entry screen is not present in Game"); 
									}

								}else
								{
									language.detailsAppendFolder("Verify  Bonus  summary screen  "+ isoCode+"Currency and in "+ languageCurrency + " ","Bonus summary should display in " + isoCode+"Currency and in "+ languageCurrency + " ","Bonus summary displays in " + isoCode+"Currency and in "+ languageCurrency + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);
									cfnlib.clickContinueInBonusGame();
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