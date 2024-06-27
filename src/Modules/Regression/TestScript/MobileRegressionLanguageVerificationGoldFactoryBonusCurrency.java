package Modules.Regression.TestScript;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;

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
 * This require a test data which will trigger bonus.collect screenshots for Reactor and free spin bonus
 * @author sg56207
 * */

public class MobileRegressionLanguageVerificationGoldFactoryBonusCurrency {
	Logger log = Logger.getLogger(MobileRegressionLanguageVerificationGoldFactoryBonusCurrency.class.getName());

	public ScriptParameters scriptParameters;

	public void script() throws Exception{


		String mstrTC_Name=scriptParameters.getMstrTCName();
		String mstrTC_Desc=scriptParameters.getMstrTCDesc();
		String mstrModuleName=scriptParameters.getMstrModuleName();
		BrowserMobProxyServer proxy=scriptParameters.getProxy();
		String startTime=scriptParameters.getStartTime();
		String filePath=scriptParameters.getFilePath();
		AppiumDriver<WebElement> webdriver=scriptParameters.getAppiumWebdriver();
		String DeviceName=scriptParameters.getDeviceName();
		String userName=scriptParameters.getUserName();
		String framework=scriptParameters.getFramework();
		String gameName=scriptParameters.getGameName();
		String osPlatform=scriptParameters.getOsPlatform();
		String osVersion=scriptParameters.getOsVersion();
		
		//int checkedOutDeviceNum=scriptParameters.getCheckedOutDeviceNum();


		String status=null;
		int mintDetailCount=0;
		//int mintSubStepNo=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		String isoId=null;
		String isoCode=null;
		String isoName = null;
		String languageCurrency = null;

		String urlNew=null;
		ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();
		Mobile_HTML_Report language=new Mobile_HTML_Report(webdriver,DeviceName,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,status,gameName);

		log.info("Framework"+framework);
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
				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");


				String strFileName=TestPropReader.getInstance().getProperty("MobileFreeSpinsTestDataPath");
				File testDataFile=new File(strFileName);


				int rowCount2 = excelpoolmanager.rowCount(testDataExcelPath, Constant.CURRENCY_XL_SHEET_NAME);

				List<Map<String, String>> curList= util.readCurrList();

				String strbonusCount=cfnlib.xpathMap.get("NoOfBonusSelection");
				int bonusCount=(int) Double.parseDouble(strbonusCount);

				String selectBonus=cfnlib.xpathMap.get("SelectBonus");
				Map <String, String> paramMap=new HashMap<>();

				for(int j=1;j<rowCount2;j++){

					userName=util.randomStringgenerator();
					rowData2 = curList.get(j);
					isoId = rowData2.get(Constant.ID).trim();
					isoCode = rowData2.get(Constant.ISOCODE).trim().replace("\u00A0", "");
					isoName = rowData2.get(Constant.ISONAME).trim().replace("\u00A0", "");
					languageCurrency = rowData2.get(Constant.LANGUAGECURRENCY).trim();


					userName=util.randomStringgenerator();
					if(	util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles,isoId,isoCode))
					{
						log.debug("Testdata is copy in test Server for Username="+userName);

						String url = cfnlib.xpathMap.get("ApplicationURL");
						String launchURL = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
						//condition added for titan and v 
						if(launchURL.contains("LanguageCode"))
							urlNew = launchURL.replaceAll("\\bLanguageCode=.*?(&|$)", "LanguageCode="+languageCurrency+"$1");

						else if(launchURL.contains("languagecode"))
							urlNew = launchURL.replaceAll("\\blanguagecode=.*?(&|$)", "languagecode="+languageCurrency+"$1");

						log.debug("url = " +urlNew);
						System.out.println("url = " +urlNew);
						if("LANDSCAPE".equalsIgnoreCase(cfnlib.xpathMap.get("Orientation")))
						{
							String context=webdriver.getContext();
							webdriver.context("NATIVE_APP");
							webdriver.rotate(ScreenOrientation.LANDSCAPE);
							webdriver.context(context);
						}
						if(cfnlib.loadGame(urlNew))
						{
							
							if(j==1) {
							cfnlib.threadSleep(15000);
							webdriver.navigate().refresh();
							System.out.println("Wait completed");
							
							}
							cfnlib.threadSleep(15000);
							
							if(framework.equalsIgnoreCase("Force")){
								cfnlib.setNameSpace();
							}
							//Step 2: To get the languages in MAP and load the language specific url
							
							language.detailsAppendFolder("Verify the Language translation on bonus scene in " +isoCode+"Currency and in "+languageCurrency+" ", " Game Bonus must display in  " +isoCode+"Currency and in "+languageCurrency+"", "", "",isoCode+"_"+languageCurrency);

							double dOsVersion = 0;
							if(osVersion!=null&&!osVersion.equals("")){
								dOsVersion=Double.parseDouble(osVersion);
							}
							if(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame"))||(Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad"))))
							{
								//Full screen overlay not occurs in Ios 15 cdevices
								
								cfnlib.funcFullScreen();
								
								Thread.sleep(3000);
							//	cfnlib.waitForSpinButton();
								Thread.sleep(2000);
								if(!(osPlatform.equalsIgnoreCase(Constant.IOS)&& dOsVersion >= 15 ) ){
								cfnlib.newFeature();
								}
							}
							else
							{

								cfnlib.funcFullScreen();
								Thread.sleep(2000);
								cfnlib.newFeature();

							}
							
							cfnlib.threadSleep(5000);
							cfnlib.waitForSpinButton();
							cfnlib.setMaxBet();
							//check and update the credits if bet value is more and refresh

							String credit = cfnlib.getCurrentCredits();
							credit=credit.toLowerCase().replace("credits: ", "");
							credit=credit.replaceAll("[^0-9]", "");


							double currentCredit = Double.parseDouble(credit);
							log.debug("Current credit:"+currentCredit);
							//receiving the bet
							String bet = cfnlib.getCurrentBet();
							bet=bet.replaceAll("[^0-9]", "");
							double currentBet = Double.parseDouble(bet);
							log.debug("Current Bet:"+currentBet);

							if(currentBet>currentCredit){
								log.debug("Updating the balance");
								util.updateUserBalance(userName,(currentBet+1000)*10);
								webdriver.navigate().refresh();
								cfnlib.waitForSpinButton();
								Thread.sleep(1000);
								cfnlib.funcFullScreen();
								Thread.sleep(1000);
								cfnlib.newFeature();
								cfnlib.setMaxBet();
							}
							language.detailsAppendFolder("Verify Language translations in Base scene "  + isoCode+"Currency and in "+languageCurrency + " "," Base scene in " + isoCode+"Currency and in "+languageCurrency + " "," Base scene in "  + isoCode+"Currency and in "+languageCurrency + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);

							cfnlib.spinclick();

							if(cfnlib.elementWait("return "+cfnlib.xpathMap.get("currentScene"), "PLAYING_ACTIVE_BONUS"))
							{
							language.detailsAppendFolder("Verify Language translations in Boiler bonus transition in " + isoCode+"Currency and in "+languageCurrency + " "," Boiler bonus  transition in " + isoCode+"Currency and in "+languageCurrency + " "," Boiler bonus transition display in " + isoCode+"Currency and in "+languageCurrency + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);
							cfnlib.elementWait("return "+cfnlib.xpathMap.get("IsPickContainerTextVisible"), true);
							language.detailsAppendFolder("Verify Language translations on Pick bonus message in " + isoCode+"Currency and in "+languageCurrency + " ","  Pick bonus message in " + isoCode+"Currency and in "+languageCurrency + " ","  Pick bonus message display in " + isoCode+"Currency and in "+languageCurrency + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);
							System.out.println("Bonus count :: "+bonusCount);
							for(int iteamCnt=0;iteamCnt<bonusCount;iteamCnt++)
							{
								Thread.sleep(12000);
								paramMap.put("param1",Integer.toString(iteamCnt));
								String newSelectBonusHook=cfnlib.replaceParamInHook(selectBonus,paramMap);

								String clickOnBonus="return "+newSelectBonusHook;
								cfnlib. clickAtButton(clickOnBonus);
								Thread.sleep(4000);
								if(("1").equalsIgnoreCase(cfnlib.GetConsoleText("return "+cfnlib.xpathMap.get("getCurrentItemPickedResultId"))) )
								{
									Thread.sleep(2000);
									language.detailsAppendFolder("Verify Language translations on Boiler bonus free spin Bonus pick up container" + isoCode+"Currency and in "+languageCurrency + " "," Boiler bonus free spin bonus pick container in " + isoCode+"Currency and in "+languageCurrency  + " "," Boiler bonus free spin bonus pick container display in " + isoCode+"Currency and in "+languageCurrency  + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);
									Thread.sleep(3300);

									language.detailsAppendFolder("Verify Language translations in Boiler bonus free spin transition in " + isoCode+"Currency and in "+languageCurrency  + " "," Boiler bonus free spin transition in " + isoCode+"Currency and in "+languageCurrency  + " "," Boiler bonus free spin transition display in " + isoCode+"Currency and in "+languageCurrency  + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);
									cfnlib.elementWait("return "+cfnlib.xpathMap.get("currentScene"), "FREESPINS_STARTING");
									cfnlib.elementWait("return "+cfnlib.xpathMap.get("IsFreespinInstructionTextVisible"), true);

									language.detailsAppendFolder("Verify Language translations in Boiler bonus free spin selection text in " + isoCode+"Currency and in "+languageCurrency  + " "," Boiler bonus free spin selection text in " + isoCode+"Currency and in "+languageCurrency  + " "," Boiler bonus free spin selction text display in " + isoCode+"Currency and in "+languageCurrency  + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);

									Thread.sleep(2000);
									//click on spin button to select the free spin count
									cfnlib.clickAtButton(cfnlib.xpathMap.get("ClickonBonusFreeSpin" ));
									Thread.sleep(500);
									language.detailsAppendFolder("Verify Language translations in Boiler bonus free spin wish text in " + isoCode+"Currency and in "+languageCurrency  + " "," Boiler bonus free spin wish text in " + isoCode+"Currency and in "+languageCurrency  + " "," Boiler bonus free spin wish text display in " + isoCode+"Currency and in "+languageCurrency  + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);
									cfnlib.elementWait("return "+cfnlib.xpathMap.get("CongratsTextVisible"), true);
									Thread.sleep(800);
									language.detailsAppendFolder("Verify Language translations in Boiler bonus free spin award text in " + isoCode+"Currency and in "+languageCurrency  + " "," Boiler bonus free spin Award text in " + isoCode+"Currency and in "+languageCurrency  + " "," Boiler bonus free spin award text display in " + isoCode+"Currency and in "+languageCurrency  + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);
									Thread.sleep(3200);

									language.detailsAppendFolder("Verify Language translations in Boiler bonus free spin transition text in " + isoCode+"Currency and in "+languageCurrency  + " "," Boiler bonus free spin transition text in " + isoCode+"Currency and in "+languageCurrency  + " "," Boiler bonus free spin transition text display in " + isoCode+"Currency and in "+languageCurrency  + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);
									Thread.sleep(4000);
									cfnlib.elementWait("return "+cfnlib.xpathMap.get("currentScene"), "FREESPINS");

									language.detailsAppendFolder("Verify Language translations in  free spin  in " + isoCode+"Currency and in "+languageCurrency  + " "," free spin  in " + isoCode+"Currency and in "+languageCurrency  + " "," free spin  in " + isoCode+"Currency and in "+languageCurrency  + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);

									Thread.sleep(4000);
									language.detailsAppendFolder("BigWin " + isoCode + " "," free spin  in " + languageCurrency + " "," free spin  in " + languageCurrency + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);
									
									Thread.sleep(5000);
									language.detailsAppendFolder("Super Big Win " + isoCode + " "," free spin  in " + languageCurrency + " "," free spin  in " + languageCurrency + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);
									
									cfnlib.clickAtCoordinates((long)50, (long)60);
									Thread.sleep(1500);
									language.detailsAppendFolder("Mega Big Win " + isoCode + " "," free spin  in " + languageCurrency + " "," free spin  in " + languageCurrency + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);
									Thread.sleep(1500);
									language.detailsAppendFolder("Inbetween freespin" + isoCode + " "," free spin  in " + languageCurrency + " "," free spin  in " + languageCurrency + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);
									
									for(int counter =0; counter<55; counter++) {
										System.out.println("CLicking on Big win");
										Thread.sleep(4000);
										cfnlib.clickAtCoordinates((long)50, (long)60);
									}
									/*Thread.sleep(60000);
									language.detailsAppendFolder("Inbetween freespin" + isoCode + " "," free spin  in " + languageCurrency + " "," free spin  in " + languageCurrency + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);
									Thread.sleep(27000);*/
									System.out.println("Freespin :: Waiting for summary text");
									cfnlib.elementWait("return "+cfnlib.xpathMap.get("ISBonusSummaryDisplay"), true,600);
									
									Thread.sleep(2000);
									language.detailsAppendFolder("Verify Language translations in Boiler bonus free spin summary text in " + isoCode+"Currency and in "+languageCurrency  + " "," Boiler bonus free spin summary text in " + isoCode+"Currency and in "+languageCurrency  + " "," Boiler bonus free spin summary text display in " + isoCode+"Currency and in "+languageCurrency  + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);
									Thread.sleep(3000);
									System.out.println("Freespin :: Waiting for summary transition");
									language.detailsAppendFolder("Verify Language translations of transition from free spin scene to boiler bonus text in " + isoCode+"Currency and in "+languageCurrency  + " "," transition from free spin scene to boiler bonus text in  " + isoCode+"Currency and in "+languageCurrency  + " "," transition from free spin scene to boiler bonus text in display in " + isoCode+"Currency and in "+languageCurrency  + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);
									cfnlib.elementWait("return "+cfnlib.xpathMap.get("currentScene"), "PLAYING_ACTIVE_BONUS");
									System.out.println("Freespin :: Waiting for summary transition completed");
									cfnlib.threadSleep(9000);
								}//if

								if(("2").equalsIgnoreCase(cfnlib.GetConsoleText("return "+cfnlib.xpathMap.get("getCurrentItemPickedResultId"))))
								{
									Thread.sleep(1000);
									language.detailsAppendFolder("Verify Language translations of  Reactor Bonus pick in" + isoCode+"Currency and in "+languageCurrency  + " "," Reactor bonus pick transition in " + isoCode+"Currency and in "+languageCurrency  + " "," Reactor bonus pick transition display in " + isoCode+"Currency and in "+languageCurrency  + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);
									Thread.sleep(1800);

									language.detailsAppendFolder("Verify Language translations in  Reactor bonus  transition in " + isoCode+"Currency and in "+languageCurrency  + " "," Reactor bonus transition in " + isoCode+"Currency and in "+languageCurrency  + " "," Reactor bonus transition display in " + isoCode+"Currency and in "+languageCurrency  + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);
									cfnlib.elementWait("return "+cfnlib.xpathMap.get("IsPickContainerTextVisible"), true);
									language.detailsAppendFolder("Verify Language translations in Reactor bonus container text in " + isoCode+"Currency and in "+languageCurrency  + " "," Reactor bonus container text in " + isoCode+"Currency and in "+languageCurrency  + " "," Reactor bonus container text display in " + isoCode+"Currency and in "+languageCurrency  + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);
									String selectReactorBonus=cfnlib.xpathMap.get("SelectReactorBonus");
									int bonuscnt=12;
									Thread.sleep(10000);
									do{
										Thread.sleep(4000);
										paramMap.put("param1",Integer.toString(bonuscnt));
										String newSelectReactorBonusHook=cfnlib.replaceParamInHook(selectReactorBonus,paramMap);

										String clickOnReactorBonus="return "+newSelectReactorBonusHook;
										Thread.sleep(1000);
										cfnlib.clickAtButton(clickOnReactorBonus);
										Thread.sleep(3000);
										bonuscnt++;
									}while(!(("13").equalsIgnoreCase(cfnlib.GetConsoleText("return "+cfnlib.xpathMap.get("getCurrentItemPickedResultId")))));
									log.debug("Exited while loop after matching");
									Thread.sleep(3000);
									language.detailsAppendFolder("Verify Language translations in Reactor bonus  text in " + isoCode+"Currency and in "+languageCurrency  + " "," Reactor bonus text in " + isoCode+"Currency and in "+languageCurrency  + " "," Reactor bonus  text display in " + isoCode+"Currency and in "+languageCurrency  + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);

									Thread.sleep(2000);
									language.detailsAppendFolderOnlyScreeshot(isoCode+"_"+languageCurrency);
									Thread.sleep(1000);
									language.detailsAppendFolderOnlyScreeshot(isoCode+"_"+languageCurrency);
									
									cfnlib.elementWait("return "+cfnlib.xpathMap.get("ISBonusSummaryDisplay"), true);
									cfnlib.elementWait("return "+cfnlib.xpathMap.get("IsReturingTextVisible"), true);

									Thread.sleep(2000);
									language.detailsAppendFolder("Verify Language translations in Reactor bonus  summary text in " + isoCode+"Currency and in "+languageCurrency  + " "," Reactor bonus summary text in " + isoCode+"Currency and in "+languageCurrency  + " "," Reactor bonus summary text display in " + isoCode+"Currency and in "+languageCurrency  + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);
									Thread.sleep(3000);

									language.detailsAppendFolder("Verify Language translations of transition from Reactor bonus scene to boiler bonus text in " + isoCode+"Currency and in "+languageCurrency  + " "," transition from Reactor bonus to boiler bonus text in  " + isoCode+"Currency and in "+languageCurrency  + " "," transition from reactor bonus to boiler bonus text in display in " + isoCode+"Currency and in "+languageCurrency  + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);
									cfnlib.elementWait("return "+cfnlib.xpathMap.get("currentScene"), "PLAYING_ACTIVE_BONUS");

								}

								if(iteamCnt==bonusCount-1)
								{
									Thread.sleep(4200);
									language.detailsAppendFolderOnlyScreeshot(isoCode+"_"+languageCurrency);
									         
									cfnlib.elementWait("return "+cfnlib.xpathMap.get("ISBonusSummaryDisplay"), true);
									Thread.sleep(2000);
									language.detailsAppendFolder("Verify Language translations in  bonus  summary text in " + isoCode+"Currency and in "+languageCurrency  + " ","  bonus summary text in " + isoCode+"Currency and in "+languageCurrency  + " "," bonus summary text display in " + isoCode+"Currency and in "+languageCurrency  + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);
									Thread.sleep(4000);

									language.detailsAppendFolder("Verify Language translations of transition from  bonus scene to base scene text in " + isoCode+"Currency and in "+languageCurrency  + " "," transition from  bonus to base scene text in  " + isoCode+"Currency and in "+languageCurrency  + " "," transition from bonus to base scene text in display in " + isoCode+"Currency and in "+languageCurrency  + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);
									cfnlib.elementWait("return "+cfnlib.xpathMap.get("currentScene"), "SLOT");
								}


							}
							Thread.sleep(3000);
							language.detailsAppendFolder("Verify Language translations on Base scene after bonus in "  + isoCode+"Currency and in "+languageCurrency + " "," Base scane in " + isoCode+"Currency and in "+languageCurrency + " "," Base scene in "  + isoCode+"Currency and in "+languageCurrency + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);

							}else
							{
								language.detailsAppend("verify Bonus trigger or not", "Bonus should triggerd", "no bonus trigger ,hense skipping the test case", "fail");
							}
						}  else
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
		}

		//-------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error(e.getMessage(),e);
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