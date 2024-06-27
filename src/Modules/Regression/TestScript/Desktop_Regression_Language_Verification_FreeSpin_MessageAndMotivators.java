package Modules.Regression.TestScript;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

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

public class Desktop_Regression_Language_Verification_FreeSpin_MessageAndMotivators {
	Logger log = Logger.getLogger(Desktop_Regression_Language_Verification_FreeSpin_MessageAndMotivators.class.getName());
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
		String browsername=scriptParameters.getBrowserName();
		String framework=scriptParameters.getFramework();
		String gameName=scriptParameters.getGameName();
		String languageDescription=null;
		String languageCode=null;
		String Status=null;
		int mintDetailCount=0;
		//int mintSubStepNo=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;

		ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();
		Desktop_HTML_Report language = new Desktop_HTML_Report(webdriver, browsername, filePath, startTime, mstrTCName,
				mstrTCDesc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings, mintStepNo, Status,
				gameName);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, language, gameName);


		CommonUtil util = new CommonUtil();
		/*
		DataBaseFunctions createplayer = new DataBaseFunctions(TestPropReader.getInstance().getProperty("ServerName"),
				TestPropReader.getInstance().getProperty("dataBaseName"),
				TestPropReader.getInstance().getProperty("serverIp"), TestPropReader.getInstance().getProperty("port"),
				TestPropReader.getInstance().getProperty("serverID"));*/

		try {
			if (webdriver != null) 
			{
				Map<String, String> rowData2 = null;
				Map<String, String> rowData3 = null;
				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				WebDriverWait wait=new WebDriverWait(webdriver,60);
				String urlNew=null;
				String url = cfnlib.XpathMap.get("ApplicationURL");
				String launchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
				log.info("url = " +launchURl);
				cfnlib.loadGame(launchURl);
				log.debug("navigated to url ");


				if(framework.equalsIgnoreCase("Force")){
					cfnlib.setNameSpace();
				}


				cfnlib.newFeature();
				int rowCount2 = excelpoolmanager.rowCount(testDataExcelPath, "LanguageCodes");
				// Reading the language code in to list map
				List<Map> list= util.readLangList();

				log.debug("Total number of Languages configured"+rowCount2);

				for (int j = 1; j < rowCount2; j++) {
					cfnlib.waitForSpinButton();
					Thread.sleep(3000);
					// Click on Spin button
					cfnlib.spinclick();
					String FreeSpinEntryScreen = cfnlib.XpathMap.get("FreeSpinEntryScreen");				
					String b2 = cfnlib.entryScreen_Wait(FreeSpinEntryScreen);
					Thread.sleep(4000);
					printTime("before new feature"+ languageCode);
					// Step 2: To get the languages in MAP and load the language specific url
					rowData2=list.get(j);
					languageDescription = rowData2.get(Constant.LANGUAGE).trim();
					languageCode = rowData2.get(Constant.LANG_CODE).trim();
					cfnlib.FSSceneLoading();


			int count2=0;		
					for(int count =0; count < 15; count++)
					{
						if(cfnlib.GetConsoleBooleanText("return window.theForce.game.automation.reelIsSpinning")&&count2 <4)
						{
								
								language.detailsAppendFolder("Verify Mesaage display when reels not resolved  in language  " + languageDescription + " ", "Mesaage should be in  " +languageDescription+ " ", "Message is in  " +languageDescription+ " ", "Pass", languageCode);
								count2++;
								Thread.sleep(400);
						}
						else
						{
							language.detailsAppendFolder("Verify Mesaage display,When reels  are resolved in language  " + languageDescription + " ", "Mesaage should be in  " +languageDescription+ " ", "Message is in  " +languageDescription+ " ", "Pass", languageCode);	
							Thread.sleep(700);
						}
					}
					
					
					
					webdriver.navigate().refresh();
					cfnlib.FSSceneLoading();
					Thread.sleep(5000);
					cfnlib.verifyJackPotBonuswithScreenShots(language,languageCode);
					Thread.sleep(3000);
					log.info("Freespin continue button is about to click");
					cfnlib.FS_continue();
					
					
					cfnlib.waitSummaryScreen();
					Thread.sleep(6000);


					if (j + 1 != rowCount2) {
						rowData3=list.get(j + 1);
						String nextLanguage = rowData3.get(Constant.LANG_CODE).trim();
						String currentUrl = webdriver.getCurrentUrl();
						if(currentUrl.contains("LanguageCode"))
							urlNew= currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+nextLanguage);
						else if(currentUrl.contains("languagecode"))
							urlNew = currentUrl.replaceAll("languagecode="+languageCode, "languagecode="+nextLanguage);
						cfnlib.loadGame(urlNew);


					}

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
			//gcfnlib.deleteFile(destFile);
			/*if(destFile.delete()){
				log.debug(destFile.getName() + " is deleted!");
			}else{
				log.debug("Test Data Delete operation is failed.");
			}*/
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
