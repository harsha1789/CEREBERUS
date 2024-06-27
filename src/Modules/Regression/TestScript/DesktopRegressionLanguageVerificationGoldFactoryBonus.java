package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
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
 * This script traverse and capture all the screen shots related to bonus unlock .
 * It reads the test data excel sheet for configured languages.
 * It require testdata which will trigger on first spin.it will take screen shots of Reactor and free spin bonus
 * @author sg56207
 */

public class DesktopRegressionLanguageVerificationGoldFactoryBonus{

	Logger log = Logger.getLogger(DesktopRegressionLanguageVerificationGoldFactoryBonus.class.getName());
	public ScriptParameters scriptParameters;



	public void script() throws Exception{

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
		Desktop_HTML_Report language = new Desktop_HTML_Report(webdriver,browserName,filePath,startTime,mstrTCName,mstrTCDesc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,status,gameName);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, language, gameName);

		CommonUtil util = new CommonUtil();
		int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int  cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDDesktop"));
		List<String> copiedFiles=new ArrayList<>();
		RestAPILibrary apiobj=new RestAPILibrary();

		try{
			// Step 1 
			if(webdriver!=null)
			{		
				Map<String, String> rowData2 = null;
				Map<String, String> rowData3 = null;
				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				String urlNew=null;
				WebDriverWait wait=new WebDriverWait(webdriver,60);
				String strFileName=TestPropReader.getInstance().getProperty("FreeSpinsTestDataPath");
				File testDataFile=new File(strFileName);

				userName=util.randomStringgenerator();
				if(	util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles))
				{
					log.debug("Test dat is copy in test Server for Username="+userName);

					String url = cfnlib.XpathMap.get("ApplicationURL");
					String launchURL = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
					log.info("url = " +launchURL);

					cfnlib.loadGame(launchURL);
					log.debug("navigated to url ");
					

					if(framework.equalsIgnoreCase(Constant.FORCE)){
						cfnlib.setNameSpace();
					}
					List<Map> list= util.readLangList();
					String strbonusCount=cfnlib.XpathMap.get("NoOfBonusSelection");
					int bonusCount=(int) Double.parseDouble(strbonusCount);
					Map <String, String> paramMap=new HashMap<>();

					language.detailsAppendNoScreenshot("Verify Language translations on Bonus feature","Bonus feature should display as per respective language", "", "" );

					int rowCount2 = excelpoolmanager.rowCount(testDataExcelPath, Constant.LANG_XL_SHEET_NAME);
					for(int j=1;j<rowCount2;j++){
						//Step 2: To get the languages in MAP and load the language specific url
						rowData2 = list.get(j);
						languageDescription = rowData2.get(Constant.LANGUAGE).trim();
						languageCode = rowData2.get(Constant.LANG_CODE).trim();
						
						cfnlib.threadSleep(15000);
						
						language.detailsAppendFolder("Verify the Language translation on bonus scene in " +languageCode+" ", " Game Bonus must display in  " +languageDescription+"", "", "",languageCode);

						cfnlib.newFeature();
						cfnlib.waitForSpinButton();
						cfnlib.spinclick();
						cfnlib.elementWait("return "+cfnlib.XpathMap.get("currentScene"), "PLAYING_ACTIVE_BONUS");
						language.detailsAppendFolder("Verify Language translations in Boiler bonus transition in " + languageCode + " "," Boiler bonus  transition in " + languageDescription + " "," Boiler bonus transition display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
						cfnlib.elementWait("return "+cfnlib.XpathMap.get("IsPickContainerTextVisible"), true);
						language.detailsAppendFolder("Verify Language translations on Pick bonus message in " + languageCode + " ","  Pick bonus message in " + languageDescription + " ","  Pick bonus message display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);

						String selectBonus=cfnlib.XpathMap.get("SelectBonus");
						for(int iteamCnt=0;iteamCnt<bonusCount;iteamCnt++)
						{
							Thread.sleep(2000);
							paramMap.put("param1",Integer.toString(iteamCnt));
							String newSelectBonusHook=cfnlib.replaceParamInHook(selectBonus,paramMap);

							String clickOnBonus="return "+newSelectBonusHook;
							cfnlib. clickAtButton(clickOnBonus);
							Thread.sleep(3000);
							if(("1").equalsIgnoreCase(cfnlib.getConsoleText("return "+cfnlib.XpathMap.get("getCurrentItemPickedResultId")))
									 )
							{
								Thread.sleep(2000);
								language.detailsAppendFolder("Verify Language translations on Boiler bonus free spin Bonus pick in" + languageCode + " "," Boiler bonus free spin bonus pick transition in " + languageDescription + " "," Boiler bonus free spin bonus pick transition display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
								Thread.sleep(3300);
								//cfnlib.elementWait("return "+cfnlib.XpathMap.get("IsBonusLevelSpritevisible"), true);

								language.detailsAppendFolder("Verify Language translations in Boiler bonus free spin transition in " + languageCode + " "," Boiler bonus free spin transition in " + languageDescription + " "," Boiler bonus free spin transition display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
								cfnlib.elementWait("return "+cfnlib.XpathMap.get("currentScene"), "FREESPINS_STARTING");
								cfnlib.elementWait("return "+cfnlib.XpathMap.get("IsFreespinInstructionTextVisible"), true);

								language.detailsAppendFolder("Verify Language translations in Boiler bonus free spin selection text in " + languageCode + " "," Boiler bonus free spin selection text in " + languageDescription + " "," Boiler bonus free spin selction text display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);

								Thread.sleep(2000);
								//click on spin button to select the free spin count
								cfnlib.clickAtButton(cfnlib.XpathMap.get("ClickonBonusFreeSpin" ));
								Thread.sleep(500);
								language.detailsAppendFolder("Verify Language translations in Boiler bonus free spin wish text in " + languageCode + " "," Boiler bonus free spin wish text in " + languageDescription + " "," Boiler bonus free spin wish text display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
								cfnlib.elementWait("return "+cfnlib.XpathMap.get("CongratsTextVisible"), true);
								Thread.sleep(800);
								language.detailsAppendFolder("Verify Language translations in Boiler bonus free spin award text in " + languageCode + " "," Boiler bonus free spin Award text in " + languageDescription + " "," Boiler bonus free spin award text display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
								Thread.sleep(3200);
								//cfnlib.elementWait("return "+cfnlib.XpathMap.get("IsEnterToFSBounusVisible"), true);

								language.detailsAppendFolder("Verify Language translations in Boiler bonus free spin transition text in " + languageCode + " "," Boiler bonus free spin transition text in " + languageDescription + " "," Boiler bonus free spin transition text display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
								Thread.sleep(4000);
								cfnlib.elementWait("return "+cfnlib.XpathMap.get("currentScene"), "FREESPINS");

								language.detailsAppendFolder("Verify Language translations in  free spin  in " + languageCode + " "," free spin  in " + languageDescription + " "," free spin  in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
								Thread.sleep(6000);
								language.detailsAppendFolder("BigWin " + languageCode + " "," free spin  in " + languageDescription + " "," free spin  in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
								
								Thread.sleep(7000);
								language.detailsAppendFolder("Super Big Win " + languageCode + " "," free spin  in " + languageDescription + " "," free spin  in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
								
								cfnlib.clickAtCoordinates((long)50, (long)60);
								Thread.sleep(1200);
								language.detailsAppendFolder("Mega Big Win " + languageCode + " "," free spin  in " + languageDescription + " "," free spin  in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
								
								Thread.sleep(60000);
								language.detailsAppendFolder("Inbetween freespin" + languageCode + " "," free spin  in " + languageDescription + " "," free spin  in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
								Thread.sleep(20000);
								
								cfnlib.elementWait("return "+cfnlib.XpathMap.get("ISBonusSummaryDisplay"), true,180);
								Thread.sleep(2000);
								language.detailsAppendFolder("Verify Language translations in Boiler bonus free spin summary text in " + languageCode + " "," Boiler bonus free spin summary text in " + languageDescription + " "," Boiler bonus free spin summary text display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
								Thread.sleep(3500);
								//cfnlib.elementWait("return "+cfnlib.XpathMap.get("IsBonusCompleteSpriteVisible"), true);

								language.detailsAppendFolder("Verify Language translations of transition from free spin scene to boiler bonus text in " + languageCode + " "," transition from free spin scene to boiler bonus text in  " + languageDescription + " "," transition from free spin scene to boiler bonus text in display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
								cfnlib.elementWait("return "+cfnlib.XpathMap.get("currentScene"), "PLAYING_ACTIVE_BONUS");
								
							cfnlib.threadSleep(7000);

							}//if

							if(("2").equalsIgnoreCase(cfnlib.getConsoleText("return "+cfnlib.XpathMap.get("getCurrentItemPickedResultId")))
									)
							{
								Thread.sleep(2000);
								language.detailsAppendFolder("Verify Language translations of  Reactor Bonus pick in" + languageCode + " "," Reactor bonus pick transition in " + languageDescription + " "," Reactor bonus pick transition display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
								Thread.sleep(3000);

								language.detailsAppendFolder("Verify Language translations in  Reactor bonus  transition in " + languageCode + " "," Reactor bonus transition in " + languageDescription + " "," Reactor bonus transition display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
								cfnlib.elementWait("return "+cfnlib.XpathMap.get("IsPickContainerTextVisible"), true,180);
								language.detailsAppendFolder("Verify Language translations in Reactor bonus container text in " + languageCode + " "," Reactor bonus container text in " + languageDescription + " "," Reactor bonus container text display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
								String selectReactorBonus=cfnlib.XpathMap.get("SelectReactorBonus");
								int bonuscnt=12;
								do{
									Thread.sleep(4000);
									paramMap.put("param1",Integer.toString(bonuscnt));
									String newSelectReactorBonusHook=cfnlib.replaceParamInHook(selectReactorBonus,paramMap);
									
									String clickOnReactorBonus="return "+newSelectReactorBonusHook;
									Thread.sleep(1000);
									System.out.println("Going to click on ::"+clickOnReactorBonus);
									cfnlib.clickAtButton(clickOnReactorBonus);
									Thread.sleep(3000);
									bonuscnt++;
								}while(!(("13").equalsIgnoreCase(cfnlib.getConsoleText("return "+cfnlib.XpathMap.get("getCurrentItemPickedResultId")))));

								Thread.sleep(3000);
								language.detailsAppendFolder("Verify Language translations in Reactor bonus  text in " + languageCode + " "," Reactor bonus text in " + languageDescription + " "," Reactor bonus  text display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
								
								cfnlib.elementWait("return "+cfnlib.XpathMap.get("ISBonusSummaryDisplay"), true);
								cfnlib.elementWait("return "+cfnlib.XpathMap.get("IsReturingTextVisible"), true);

								Thread.sleep(2000);
								language.detailsAppendFolder("Verify Language translations in Reactor bonus  summary text in " + languageCode + " "," Reactor bonus summary text in " + languageDescription + " "," Reactor bonus summary text display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
								Thread.sleep(4000);

								language.detailsAppendFolder("Verify Language translations of transition from Reactor bonus scene to boiler bonus text in " + languageCode + " "," transition from Reactor bonus to boiler bonus text in  " + languageDescription + " "," transition from reactor bonus to boiler bonus text in display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
								cfnlib.elementWait("return "+cfnlib.XpathMap.get("currentScene"), "PLAYING_ACTIVE_BONUS");

							}

							if(iteamCnt==bonusCount-1)
							{
								cfnlib.elementWait("return "+cfnlib.XpathMap.get("ISBonusSummaryDisplay"), true);
								Thread.sleep(2000);
								language.detailsAppendFolder("Verify Language translations in  bonus  summary text in " + languageCode + " ","  bonus summary text in " + languageDescription + " "," bonus summary text display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
								Thread.sleep(3500);

								language.detailsAppendFolder("Verify Language translations of transition from  bonus scene to base scene text in " + languageCode + " "," transition from  bonus to base scene text in  " + languageDescription + " "," transition from bonus to base scene text in display in " + languageDescription + " ,verify screenshot", "Pass", languageCode);
								cfnlib.elementWait("return "+cfnlib.XpathMap.get("currentScene"), "SLOT");
							}


						}
						//Language Change logic:: for updating language in URL and then Refresh 
						if (j + 1 != rowCount2){
							rowData3 = list.get(j+1);
							String languageCode2 = rowData3.get(Constant.LANG_CODE).trim();

							String currentUrl = webdriver.getCurrentUrl();

							if(currentUrl.contains("LanguageCode"))
								urlNew= currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+languageCode2);
							else if(currentUrl.contains("languagecode"))
								urlNew = currentUrl.replaceAll("languagecode="+languageCode, "languagecode="+languageCode2);
							cfnlib.loadGame(urlNew);

						}
					}   
				}
			}

		}    
		//-------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error(e.getMessage(),e);
			cfnlib.evalException(e);
		}
		//-------------------Closing the connections---------------//
		finally
		{
			language.endReport();
			webdriver.close();
			webdriver.quit();
			proxy.abort();
			Thread.sleep(1000);	   
		}	
	}
}
