package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
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

public class DesktopLanguageVideoPoker {
	
	
	Logger log = Logger.getLogger(DesktopLanguageVideoPoker.class.getName());
	public ScriptParameters scriptParameters;

		// <---------------sg56207--->
	/*	//extent report class

			//builds a new report using the html template 
			 ExtentHtmlReporter htmlReporter;
			 
			 ExtentReports extent;
			 
			//helps to generate the logs in test report.
			 
			 ExtentTest test;

*/	
	
	
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
		String status1=null;
		int mintDetailCount=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		
		ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();
		Desktop_HTML_Report languageReport = new Desktop_HTML_Report(webdriver,browserName,filePath,startTime,mstrTCName,mstrTCDesc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,status1,gameName);
		ExtentHtmlReporter htmlReporter;
		ExtentReports extent=null;
		ExtentTest report=null;
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, languageReport, gameName);

		log.info("Report object created");
		log.info("Framework="+framework);
		
		CommonUtil util = new CommonUtil();
		Map<String, String> rowData2 = null;
		Map<String, String> rowData3 = null;
		
		
		try{
			int envid ;
			String lobbyName;
			RestAPILibrary apiObj = new RestAPILibrary();
			String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
			
			htmlReporter= new ExtentHtmlReporter(filePath+mstrModuleName+"/"+util.createTimeStampStr()+"_"+browserName+"_"+gameName+"_"+mstrTCName+".html");
			htmlReporter.config().setDocumentTitle("Execution Report");
			htmlReporter.config().setReportName("Sanity Suite Report");
			
		//	/ZAF_JAR_GameChangers/src/com/zensar/automation/library/report/extentReport_config.xml
		//	htmlReporter.loadXMLConfig(new File(System.getProperty("user.dir")+"\\extentReport_config.xml"));
			extent= new ExtentReports();
			extent.attachReporter(htmlReporter);
			report=extent.createTest("Language Test");
			
			// Step 1 
			if(webdriver!=null)
			{	
				
				
				if( TestPropReader.getInstance().getProperty("EnvironmentName")==null ||
						TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
				{
						envid=Integer.parseInt(TestPropReader.getInstance().getProperty("EnvironmentID"));
				}
					if(TestPropReader.getInstance().getProperty("EnvironmentName")!=null)
				{
						lobbyName=TestPropReader.getInstance().getProperty("EnvironmentName").toLowerCase();
				}
					else
				{
						lobbyName="bluemesa";
						TestPropReader.getInstance().setProperty("EnvironmentName","bluemesa");
				}
					
				userName=util.randomStringgenerator();
				
				//Implement code for test data copy depdeding on env
				int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
				int cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDDesktop"));
				String strFileName=TestPropReader.getInstance().getProperty("SanityTestDataPath");
				File testDataFile=new File(strFileName);
				List<String> copiedFiles=new ArrayList<>();
				if(	util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles))
					log.debug("Test dat is copy in test Server for Username="+userName);
				
				
				String url = cfnlib.XpathMap.get("ApplicationURL");
				String launchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
				log.info("url = " +launchURl);
				languageReport.detailsAppendFolder("Language Suite Test cases", "Language Suite Test cases", "", "","");
				
				
				if(	cfnlib.loadGame(launchURl))
				{
					
				
					List<Map> list= util.readLangList();
				
					int rowCount2 = excelpoolmanager.rowCount(testDataExcelPath, Constant.LANG_XL_SHEET_NAME);
					for(int j=1;j<rowCount2;j++){
					//Step 2: To get the languages in MAP and load the language specific url
					rowData2 = list.get(j);
					languageDescription = rowData2.get(Constant.LANGUAGE).trim();
					languageCode = rowData2.get(Constant.LANG_CODE).trim();
					
					languageReport.detailsAppend("Verify the Language code is " +languageCode+" ", " Application window should be displayed in " +languageDescription+"", "", "");
					
					
					//******************** Splash screen screen shot
					//cfnlib.splashScreen(languageReport,languageCode);	
					languageReport.detailsAppendFolder(" Verify that the application should display with Game Logo and game name in Base Scene", "Game logo and game name with base scene should display", "Game logo and game name with base scene displays", "Pass", languageCode);
					Thread.sleep(1000);
					
					cfnlib.newFeature();
					Thread.sleep(1500);
					
					
					if(cfnlib.open_TotalBet())
					{
						//report and scrren shot
						languageReport.detailsAppendFolder("Verify bet setting Screen", "On Clicking bet button ,Bet Setting overlay should display", "Bet Setting overlay is display", "Pass",languageCode);
						cfnlib.resizeBrowser(600, 800);
						Thread.sleep(1000);
						languageReport.detailsAppendFolder("Verify bet setting Screen", "On Clicking bet button ,Bet Setting overlay should display", "Bet Setting overlay is display", "Pass", languageCode);
						cfnlib.resizeBrowser(400, 600);
						Thread.sleep(1000);
						languageReport.detailsAppendFolder("Verify bet setting Screen", "On Clicking bet button ,Bet Setting overlay should display", "Bet Setting overlay is display", "Pass",languageCode);
						webdriver.manage().window().maximize();
						cfnlib.close_TotalBet();
					}
					else
					{
						//report and screen shot
						languageReport.detailsAppendFolder("Verify bet setting Screen", "On Clicking bet button ,Bet Setting overlay should display", "Bet Setting overlay is display", "Fail",languageCode);

					}
					
					
					
					//******************Deal button
					if(cfnlib.dealClick())
					{
						languageReport.detailsAppendFolder("Verify deal functionality", "On Clicking deal button", "Draw button should be displayed", "Pass", languageCode);
						cfnlib.resizeBrowser(600, 800);
						Thread.sleep(1000);
						languageReport.detailsAppendFolder("Verify deal functionality", "On Clicking deal button", "Draw button should be displayed", "Pass", languageCode);
						cfnlib.resizeBrowser(400,600);
						Thread.sleep(1000);
						languageReport.detailsAppendFolder("Verify deal functionality", "On Clicking deal button", "Draw button should be displayed", "Pass", languageCode);
						webdriver.manage().window().maximize();
						
						//****************Draw button
						if(cfnlib.drawClick())
						{
							
							languageReport.detailsAppendFolder("Verify draw functionality", "On Clicking draw button", "Draw button should be displayed", "Pass", languageCode);
							cfnlib.resizeBrowser(600, 800);
							Thread.sleep(1000);
							languageReport.detailsAppendFolder("Verify draw functionality", "On Clicking draw button", "Draw button should be displayed", "Pass", languageCode);
							cfnlib.resizeBrowser(400, 600);
							Thread.sleep(1000);
							languageReport.detailsAppendFolder("Verify draw functionality", "On Clicking draw button", "Draw button should be displayed", "Pass", languageCode);
							webdriver.manage().window().maximize();
							
							
							//*********Checking draw and collect functionality
							cfnlib.drawCollectBaseGame(languageReport,languageCode);
						}
						else
						{
							languageReport.detailsAppendFolder("Verify draw functionality", "On Clicking draw button", "Draw button is not displayed", "Fail", languageCode);
						}
					}
					else
					{
						languageReport.detailsAppend("Verify deal functionality", "On Clicking deal button", "Draw button is not displayed", "Fail", languageCode);
					}
										
					
					//*************Double to functionality
					cfnlib.doubleToGambleReached(languageReport,languageCode);
					
					
					//*************Paytable functionality
					cfnlib.paytableClickVideoPoker(languageReport,languageCode);
					
					if(cfnlib.settingsOpen())
					{
						languageReport.detailsAppendFolder("Verify setting Screen", "On Clicking setting button ,Setting overlay should display", "Setting overlay is display", "Pass",languageCode);
						cfnlib.resizeBrowser(800, 600);
						Thread.sleep(1000);
						languageReport.detailsAppendFolder("Verify setting Screen", "On Clicking setting button ,Setting overlay should display", "Setting overlay is display", "Pass",languageCode);
						cfnlib.resizeBrowser(400, 600);
						Thread.sleep(1000);
						languageReport.detailsAppendFolder("Verify setting Screen", "On Clicking setting button ,Setting overlay should display", "Setting overlay is display", "Pass",languageCode);
						webdriver.manage().window().maximize();
						cfnlib.func_Click(cfnlib.XpathMap.get("SettingClose"));
						log.debug("Setting overlay close");
					}
					else
					{
						languageReport.detailsAppend("Verify setting Screen", "On Clicking setting button ,Setting overlay should display", "Setting overlay is not display", "Fail",languageCode);

					}
					
					
					
					//Language Change logic:: for updating language in URL and then Refresh 
					if (j + 1 != rowCount2){
						 rowData3 = list.get(j+1);
						String languageCode2 = rowData3.get(Constant.LANG_CODE).trim();

						String currentUrl = webdriver.getCurrentUrl();
						String urlNew = currentUrl.replaceAll("languagecode="+languageCode, "languagecode="+languageCode2);
						webdriver.navigate().to(urlNew);
						String error=cfnlib.XpathMap.get("Error");
						if(cfnlib.isElementPresent(error))
						{
							languageReport.detailsAppend("Verify the Language code is " +languageCode2+" ", " Application window should be displayed in " +languageDescription+"", "", "");
							languageReport.detailsAppend("Verify that any error is coming","error must not come","error is coming", "fail");
							if (j + 2 != rowCount2){
								 rowData3 = list.get(j+2);
								String languageCode3 = rowData3.get(Constant.LANG_CODE).trim();

								currentUrl = webdriver.getCurrentUrl();
								urlNew = currentUrl.replaceAll("LanguageCode="+languageCode2, "LanguageCode="+languageCode3);
								webdriver.navigate().to(urlNew);
						}
							j++;
						}
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
				languageReport.endReport();
				//extent.flush();
				//add code to delete the testdata file copied
				webdriver.close();
				webdriver.quit();
				proxy.abort();
				Thread.sleep(1000);	   
			}	
		
	}
}
