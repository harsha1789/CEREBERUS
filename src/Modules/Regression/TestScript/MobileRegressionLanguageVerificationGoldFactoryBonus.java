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
 * This script traverse and capture all the screen shots related to bonus unlock .
 * It reads the test data excel sheet for configured languages.
 * It require testdata which will trigger on first spin.it will take screen shots of Reactor and free spin bonus
 * @author sg56207
 */


public class MobileRegressionLanguageVerificationGoldFactoryBonus {
	Logger log = Logger.getLogger(MobileRegressionLanguageVerificationGoldFactoryBonus.class.getName());

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
				Map<String, String> rowData3 = null;
				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				
				
				String strFileName=TestPropReader.getInstance().getProperty("MobileFreeSpinsTestDataPath");
				File testDataFile=new File(strFileName);

				userName=util.randomStringgenerator();
				if(	util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles))
				{
					log.debug("Testdata is copy in test Server for Username="+userName);
				
				String url = cfnlib.xpathMap.get("ApplicationURL");
				String launchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
				System.out.println("launchURl "+launchURl);
				log.debug("url = " +launchURl);
				if("LANDSCAPE".equalsIgnoreCase(cfnlib.xpathMap.get("Orientation")))
				{
					String context=webdriver.getContext();
					webdriver.context("NATIVE_APP");
					webdriver.rotate(ScreenOrientation.LANDSCAPE);
					webdriver.context(context);
				}
				cfnlib.loadGame(launchURl);
			

				if(framework.equalsIgnoreCase("Force")){
					cfnlib.setNameSpace();
				}
				String strbonusCount=cfnlib.xpathMap.get("NoOfBonusSelection");
				int bonusCount=(int) Double.parseDouble(strbonusCount);
				Map <String, String> paramMap=new HashMap<>();
				language.detailsAppendNoScreenshot("Verify Language translations on Bonus feature","Bonus feature should display as per respective language", "", "" );

				int rowCount2 = excelpoolmanager.rowCount(testDataExcelPath, "LanguageCodes");
				for(int j=1;j<rowCount2;j++){
					//Step 2: To get the languages in MAP and load the language specific url
					rowData2 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",  j);
					String languageDescription = rowData2.get("Language").toString();
					String languageCode = rowData2.get("Language Code").toString().trim();
					language.detailsAppendFolder("Verify the Language translation on bonus scene in " +languageCode+" ", " Game Bonus must display in  " +languageDescription+"", "", "",languageCode);
					
					double dOsVersion = 0;
					if(osVersion!=null&&!osVersion.equals("")){
						dOsVersion=Double.parseDouble(osVersion);
					}
					if(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame"))||(Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad"))))
					{
						
						cfnlib.funcFullScreen();
						Thread.sleep(2000);
						cfnlib.waitForSpinButton();
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
					
					cfnlib.funcFullScreen();
					Thread.sleep(4000);
					cfnlib.waitForSpinButton();
					cfnlib.spinclick();
					
					
					cfnlib.elementWait("return "+cfnlib.xpathMap.get("currentScene"), "PLAYING_ACTIVE_BONUS");
					language.detailsAppendFolder("Verify Language translations in Boiler bonus transition in " + languageCode + " "," Boiler bonus  transition in " + languageDescription + " "," Boiler bonus transition display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
					cfnlib.elementWait("return "+cfnlib.xpathMap.get("IsPickContainerTextVisible"), true);
					language.detailsAppendFolder("Verify Language translations on Pick bonus message in " + languageCode + " ","  Pick bonus message in " + languageDescription + " ","  Pick bonus message display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);

					String selectBonus=cfnlib.xpathMap.get("SelectBonus");
					for(int iteamCnt=0;iteamCnt<bonusCount;iteamCnt++)
					{
						Thread.sleep(2000);
						paramMap.put("param1",Integer.toString(iteamCnt));
						String newSelectBonusHook=cfnlib.replaceParamInHook(selectBonus,paramMap);

						String clickOnBonus="return "+newSelectBonusHook;
						cfnlib. clickAtButton(clickOnBonus);
						Thread.sleep(4000);
						if(("1").equalsIgnoreCase(cfnlib.GetConsoleText("return "+cfnlib.xpathMap.get("getCurrentItemPickedResultId"))) )
						{
							Thread.sleep(2000);
							language.detailsAppendFolder("Verify Language translations on Boiler bonus free spin Bonus pick up container" + languageCode + " "," Boiler bonus free spin bonus pick container in " + languageDescription + " "," Boiler bonus free spin bonus pick container display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
							Thread.sleep(3300);

							language.detailsAppendFolder("Verify Language translations in Boiler bonus free spin transition in " + languageCode + " "," Boiler bonus free spin transition in " + languageDescription + " "," Boiler bonus free spin transition display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
							cfnlib.elementWait("return "+cfnlib.xpathMap.get("currentScene"), "FREESPINS_STARTING");
							cfnlib.elementWait("return "+cfnlib.xpathMap.get("IsFreespinInstructionTextVisible"), true);

							language.detailsAppendFolder("Verify Language translations in Boiler bonus free spin selection text in " + languageCode + " "," Boiler bonus free spin selection text in " + languageDescription + " "," Boiler bonus free spin selction text display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);

							Thread.sleep(2000);
							//click on spin button to select the free spin count
							cfnlib.clickAtButton(cfnlib.xpathMap.get("ClickonBonusFreeSpin" ));
							Thread.sleep(500);
							language.detailsAppendFolder("Verify Language translations in Boiler bonus free spin wish text in " + languageCode + " "," Boiler bonus free spin wish text in " + languageDescription + " "," Boiler bonus free spin wish text display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
							cfnlib.elementWait("return "+cfnlib.xpathMap.get("CongratsTextVisible"), true);
							Thread.sleep(800);
							language.detailsAppendFolder("Verify Language translations in Boiler bonus free spin award text in " + languageCode + " "," Boiler bonus free spin Award text in " + languageDescription + " "," Boiler bonus free spin award text display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
							Thread.sleep(3200);

							language.detailsAppendFolder("Verify Language translations in Boiler bonus free spin transition text in " + languageCode + " "," Boiler bonus free spin transition text in " + languageDescription + " "," Boiler bonus free spin transition text display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
							Thread.sleep(4000);
							cfnlib.elementWait("return "+cfnlib.xpathMap.get("currentScene"), "FREESPINS");

							language.detailsAppendFolder("Verify Language translations in  free spin  in " + languageCode + " "," free spin  in " + languageDescription + " "," free spin  in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
							Thread.sleep(3000);
							language.detailsAppendFolder("BigWin " + languageCode + " "," free spin  in " + languageDescription + " "," free spin  in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
							
							Thread.sleep(6000);
							language.detailsAppendFolder("Super Big Win " + languageCode + " "," free spin  in " + languageDescription + " "," free spin  in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
							
							//cfnlib.clickAtCoordinates((long)50, (long)60);
							Thread.sleep(6000);
							language.detailsAppendFolder("Mega Big Win " + languageCode + " "," free spin  in " + languageDescription + " "," free spin  in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
							
							Thread.sleep(50000);
							language.detailsAppendFolder("Inbetween freespin" + languageCode + " "," free spin  in " + languageDescription + " "," free spin  in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
							Thread.sleep(6000);
							language.detailsAppendFolder("before weight of ISBonusSummaryDisplay" + languageCode + " "," free spin  in " + languageDescription + " "," free spin  in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
							
							cfnlib.elementWait("return "+cfnlib.xpathMap.get("ISBonusSummaryDisplay"), true,180);
							Thread.sleep(2000);
							language.detailsAppendFolder("Verify Language translations in Boiler bonus free spin summary text in " + languageCode + " "," Boiler bonus free spin summary text in " + languageDescription + " "," Boiler bonus free spin summary text display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
							Thread.sleep(2000);

							language.detailsAppendFolder("Verify Language translations of transition from free spin scene to boiler bonus text in " + languageCode + " "," transition from free spin scene to boiler bonus text in  " + languageDescription + " "," transition from free spin scene to boiler bonus text in display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
							cfnlib.elementWait("return "+cfnlib.xpathMap.get("currentScene"), "PLAYING_ACTIVE_BONUS");
							
							cfnlib.threadSleep(9000);

						}//if

						if(("2").equalsIgnoreCase(cfnlib.GetConsoleText("return "+cfnlib.xpathMap.get("getCurrentItemPickedResultId"))))
						{
							Thread.sleep(1000);
							language.detailsAppendFolder("Verify Language translations of  Reactor Bonus pick in" + languageCode + " "," Reactor bonus pick transition in " + languageDescription + " "," Reactor bonus pick transition display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
							Thread.sleep(1800);

							language.detailsAppendFolder("Verify Language translations in  Reactor bonus  transition in " + languageCode + " "," Reactor bonus transition in " + languageDescription + " "," Reactor bonus transition display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
							cfnlib.elementWait("return "+cfnlib.xpathMap.get("IsPickContainerTextVisible"), true);
							language.detailsAppendFolder("Verify Language translations in Reactor bonus container text in " + languageCode + " "," Reactor bonus container text in " + languageDescription + " "," Reactor bonus container text display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
							String selectReactorBonus=cfnlib.xpathMap.get("SelectReactorBonus");
							int bonuscnt=12;
							do{
								cfnlib.threadSleep(4000);
								paramMap.put("param1",Integer.toString(bonuscnt));
								String newSelectReactorBonusHook=cfnlib.replaceParamInHook(selectReactorBonus,paramMap);
								
								String clickOnReactorBonus="return "+newSelectReactorBonusHook;
								Thread.sleep(1000);
								cfnlib.clickAtButton(clickOnReactorBonus);
								Thread.sleep(3000);
								bonuscnt++;
							}while(!(("13").equalsIgnoreCase(cfnlib.GetConsoleText("return "+cfnlib.xpathMap.get("getCurrentItemPickedResultId")))));

							Thread.sleep(3000);
							language.detailsAppendFolder("Verify Language translations in Reactor bonus  text in " + languageCode + " "," Reactor bonus text in " + languageDescription + " "," Reactor bonus  text display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);

							cfnlib.elementWait("return "+cfnlib.xpathMap.get("ISBonusSummaryDisplay"), true);
							cfnlib.elementWait("return "+cfnlib.xpathMap.get("IsReturingTextVisible"), true);

							Thread.sleep(2000);
							language.detailsAppendFolder("Verify Language translations in Reactor bonus  summary text in " + languageCode + " "," Reactor bonus summary text in " + languageDescription + " "," Reactor bonus summary text display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
							Thread.sleep(3000);

							language.detailsAppendFolder("Verify Language translations of transition from Reactor bonus scene to boiler bonus text in " + languageCode + " "," transition from Reactor bonus to boiler bonus text in  " + languageDescription + " "," transition from reactor bonus to boiler bonus text in display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
							cfnlib.elementWait("return "+cfnlib.xpathMap.get("currentScene"), "PLAYING_ACTIVE_BONUS");

						}

						if(iteamCnt==bonusCount-1)
						{
							cfnlib.elementWait("return "+cfnlib.xpathMap.get("ISBonusSummaryDisplay"), true);
							Thread.sleep(2000);
							language.detailsAppendFolder("Verify Language translations in  bonus  summary text in " + languageCode + " ","  bonus summary text in " + languageDescription + " "," bonus summary text display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
							Thread.sleep(3000);

							language.detailsAppendFolder("Verify Language translations of transition from  bonus scene to base scene text in " + languageCode + " "," transition from  bonus to base scene text in  " + languageDescription + " "," transition from bonus to base scene text in display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
							cfnlib.elementWait("return "+cfnlib.xpathMap.get("currentScene"), "SLOT");
						}


					}
					
					
					if (j + 1 != rowCount2){
						rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j+1);
						languageDescription = rowData3.get("Language").trim();
						String languageCode2 = rowData3.get("Language Code").trim();

						String currentUrl = webdriver.getCurrentUrl();
						if(currentUrl.contains("LanguageCode"))
							urlNew= currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+languageCode2);
						else if(currentUrl.contains("languagecode"))
							urlNew = currentUrl.replaceAll("languagecode="+languageCode, "languagecode="+languageCode2);

						
						log.info("Privious Language Code in Url= "+languageCode+"\nNext Language code= "+languageCode2+"\nNew Url after replacing language code:"+urlNew);
						cfnlib.loadGame(urlNew);
						String error=cfnlib.xpathMap.get("Error");

						if(cfnlib.isElementPresent(error))
						{
							language.detailsAppendFolder("Verify the Language code is " +languageCode2+" ", " Application window should be displayed in " +languageDescription+"", "", "", languageCode2);
							language.detailsAppendFolder("Verify that any error is coming","General error should not display","General Error is Diplay", "fail", languageCode2);

							if (j + 2 != rowCount2){
								rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j+2);
								String languageCode3 = rowData3.get("Language Code").toString().trim();
								currentUrl = webdriver.getCurrentUrl();
								if(currentUrl.contains("LanguageCode"))
									urlNew= currentUrl.replaceAll("LanguageCode="+languageCode2, "LanguageCode="+languageCode3);
								else if(currentUrl.contains("languagecode"))
									urlNew = currentUrl.replaceAll("languagecode="+languageCode2, "languagecode="+languageCode3);

								cfnlib.loadGame(urlNew);
							}

							j++;
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