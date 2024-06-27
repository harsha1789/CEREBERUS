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


public class DesktopCurrencyVideoPoker {
	
	Logger log = Logger.getLogger(DesktopCurrencyVideoPoker.class.getName());
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
		String isoId=null;
		String isoCode=null;
		String isoName = null;
		String languageCurrency = null;
		String status1=null;
		int mintDetailCount=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		
		ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();
		Desktop_HTML_Report currencyReport = new Desktop_HTML_Report(webdriver,browserName,filePath,startTime,mstrTCName,mstrTCDesc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,status1,gameName);
		ExtentHtmlReporter htmlReporter;
		ExtentReports extent=null;
		ExtentTest report=null;
		
		log.info("Report object created");
		log.info("Framework="+framework);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,currencyReport, gameName);

		CommonUtil util = new CommonUtil();
		Map<String, String> rowData2 = null;
		
		
		try{
			int envid ;
			String lobbyName;
			RestAPILibrary apiObj = new RestAPILibrary();
			String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
			
			htmlReporter= new ExtentHtmlReporter(filePath+mstrModuleName+"/"+util.createTimeStampStr()+"_"+browserName+"_"+gameName+"_"+mstrTCName+".html");
			htmlReporter.config().setDocumentTitle("Execution Report");
			htmlReporter.config().setReportName("Sanity Suite Report");
			
			extent= new ExtentReports();
			extent.attachReporter(htmlReporter);
			report=extent.createTest("Sanity Test");
			
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
					
				
				//Implement code for test data copy depdeding on env
				int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
				int cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDDesktop"));
				String strFileName=TestPropReader.getInstance().getProperty("SanityTestDataPath");
				File testDataFile=new File(strFileName);
				List<String> copiedFiles=new ArrayList<>();
				
				
				
				int rowCount2 = excelpoolmanager.rowCount(testDataExcelPath, Constant.CURRENCY_XL_SHEET_NAME);
				
				List<Map<String, String>> curList= util.readCurrList();
				
				
				for(int j=1;j<rowCount2;j++){
					userName=util.randomStringgenerator();
				//Step 2: To get the languages in MAP and load the language specific url
				rowData2 = curList.get(j);
				isoId = rowData2.get(Constant.ID).trim();
				isoCode = rowData2.get(Constant.ISOCODE).trim();
				isoName = rowData2.get(Constant.ISONAME).trim();
				languageCurrency = rowData2.get(Constant.LANGUAGECURRENCY).trim();
				
				if(	util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles,isoId))
					log.debug("Test dat is copy in test Server for Username="+userName);
				
				
				String url = cfnlib.XpathMap.get("ApplicationURL");
				String launchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1" );
				launchURl = launchURl.replaceAll("\\blanguagecode=.*?(&|$)", "languagecode="+languageCurrency+"$1");
				System.out.println(launchURl);
				log.info("url = " +launchURl);
				currencyReport.detailsAppendFolder("Currency Suite Test cases "+isoName, "Currency Suite Test cases"+isoName, "", "","");
				
				
				if(	cfnlib.loadGame(launchURl))
				{
					
					currencyReport.detailsAppendFolder(" Verify that the application should display with Game Logo and game name in Base Scene", "Game logo and game name with base scene should display", "Game logo and game name with base scene displays", "Pass", isoCode+"_"+languageCurrency);
					Thread.sleep(1000);
					
					cfnlib.newFeature();
					Thread.sleep(1500);
					
					
					//********************Total Bet
					if(cfnlib.open_TotalBet())
					{
					
						currencyReport.detailsAppendFolder("Verify bet setting Screen", "On Clicking bet button ,Bet Setting overlay should display", "Bet Setting overlay is display", "Pass",isoCode+"_"+languageCurrency);
						cfnlib.resizeBrowser(600, 800);
						Thread.sleep(1000);
						currencyReport.detailsAppendFolder("Verify bet setting Screen", "On Clicking bet button ,Bet Setting overlay should display", "Bet Setting overlay is display", "Pass",isoCode+"_"+languageCurrency);
						cfnlib.resizeBrowser(400, 600);
						Thread.sleep(1000);
						currencyReport.detailsAppendFolder("Verify bet setting Screen", "On Clicking bet button ,Bet Setting overlay should display", "Bet Setting overlay is display", "Pass",isoCode+"_"+languageCurrency);
						webdriver.manage().window().maximize();
						cfnlib.close_TotalBet();
					}
					else
					{
						currencyReport.detailsAppendFolder("Verify bet setting Screen", "On Clicking bet button ,Bet Setting overlay should display", "Bet Setting overlay is display", "Fail",isoCode+"_"+languageCurrency);

					}
					
					//******************Deal button
					if(cfnlib.dealClick())
					{
						currencyReport.detailsAppendFolder("Verify deal functionality", "On Clicking deal button", "Draw button should be displayed", "Pass", isoCode+"_"+languageCurrency);
						cfnlib.resizeBrowser(600, 800);
						Thread.sleep(1000);
						currencyReport.detailsAppendFolder("Verify deal functionality", "On Clicking deal button", "Draw button should be displayed", "Pass", isoCode+"_"+languageCurrency);
						cfnlib.resizeBrowser(400, 600);
						Thread.sleep(1000);
						currencyReport.detailsAppendFolder("Verify deal functionality", "On Clicking deal button", "Draw button should be displayed", "Pass", isoCode+"_"+languageCurrency);
						webdriver.manage().window().maximize();
						

						//****************Draw button
						if(cfnlib.drawClick())
						{
							
							currencyReport.detailsAppendFolder("Verify draw functionality", "On Clicking draw button", "Draw button should be displayed", "Pass", isoCode+"_"+languageCurrency);
							cfnlib.resizeBrowser(600, 800);
							Thread.sleep(1000);
							currencyReport.detailsAppendFolder("Verify draw functionality", "On Clicking draw button", "Draw button should be displayed", "Pass", isoCode+"_"+languageCurrency);
							cfnlib.resizeBrowser(400, 600);
							Thread.sleep(1000);
							currencyReport.detailsAppendFolder("Verify draw functionality", "On Clicking draw button", "Draw button should be displayed", "Pass", isoCode+"_"+languageCurrency);
							webdriver.manage().window().maximize();
							
							
							//*********Checking draw and collect functionality
							cfnlib.drawCollectBaseGame(currencyReport, isoCode+"_"+languageCurrency);
						}
						else
						{
							currencyReport.detailsAppendFolder("Verify draw functionality", "On Clicking draw button", "Draw button is not displayed", "Fail", isoCode+"_"+languageCurrency);
						}					
						
						
					}
					else
					{
						currencyReport.detailsAppend("Verify deal functionality", "On Clicking deal button", "Draw button is not displayed", "Fail", isoCode+"_"+languageCurrency);
					}
					
					
								
					//*************Double to functionality
					cfnlib.doubleToGambleReached(currencyReport,isoCode+"_"+languageCurrency);
					
					
					//*************Paytable functionality
					cfnlib.paytableClickVideoPoker(currencyReport,isoCode+"_"+languageCurrency);
					
				}else
				{
					currencyReport.detailsAppendFolder(" Verify that the application should display with Game Logo and game name in Base Scene", "Game logo and game name with base scene should display", "Unable to load game", "Fail", isoCode+"_"+languageCurrency);

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
			currencyReport.endReport();
			//extent.flush();
			//add code to delete the testdata file copied
			webdriver.close();
			webdriver.quit();
			proxy.abort();
			Thread.sleep(1000);	   
		}	
	
}

}
