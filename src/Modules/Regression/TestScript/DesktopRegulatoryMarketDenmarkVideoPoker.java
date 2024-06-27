package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;

public class DesktopRegulatoryMarketDenmarkVideoPoker {
	
	
	Logger log = Logger.getLogger(DesktopSanitySuiteVideoPoker.class.getName());
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
		String status1=null;
		int mintDetailCount=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		String languageCode = null;

		Desktop_HTML_Report denmarkReport = new Desktop_HTML_Report(webdriver,browserName,filePath,startTime,mstrTCName,mstrTCDesc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,status1,gameName);
		ExtentHtmlReporter htmlReporter;
		ExtentReports extent=null;
		ExtentTest report=null;

		log.info("Report object created");
		log.info("Framework="+framework);
		
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, denmarkReport, gameName);

		CommonUtil util = new CommonUtil();



		try{
			int envid ;
			String lobbyName;
			RestAPILibrary apiObj = new RestAPILibrary();

			htmlReporter= new ExtentHtmlReporter(filePath+mstrModuleName+"/"+util.createTimeStampStr()+"_"+browserName+"_"+gameName+"_"+mstrTCName+".html");
			htmlReporter.config().setDocumentTitle("Execution Report");
			htmlReporter.config().setReportName("Sanity Suite Report");

			//	/ZAF_JAR_GameChangers/src/com/zensar/automation/library/report/extentReport_config.xml
			//	htmlReporter.loadXMLConfig(new File(System.getProperty("user.dir")+"\\extentReport_config.xml"));
			extent= new ExtentReports();
			extent.attachReporter(htmlReporter);
			report=extent.createTest("Sanity Test");
			boolean isSettingClose=false;
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
				int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("ModuleId"));
				int cid=Integer.parseInt(TestPropReader.getInstance().getProperty("DesktopClientId"));
				String strFileName=TestPropReader.getInstance().getProperty("SanityTestDataPath");
				File testDataFile=new File(strFileName);
				List<String> copiedFiles=new ArrayList<>();
				if(	util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles))
					log.debug("Test dat is copy in test Server for Username="+userName);


				String url = cfnlib.XpathMap.get("ApplicationURL");
				String launchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
				log.info("url = " +launchURl);
				denmarkReport.detailsAppend("Sanity Suite Test cases", "Sanity Suite Test cases", "", "");

				if(	cfnlib.loadGame(launchURl))
				{
					//report.log(Status.PASS, MarkupHelper.createLabel(" Game loaded succesfull",  ExtentColor.GREEN));
					denmarkReport.detailsAppend("Verify that Game loaded successfully", "Game should load", "Game Loaded successfully", "Pass");


					if(framework.equalsIgnoreCase("Force")){
						cfnlib.setNameSpace();
					}


					//*************Click on feature screen if present in the game
					cfnlib.newFeature();
					cfnlib.waitFor(cfnlib.XpathMap.get("DealButton"));
					
					
				}else
				{
					denmarkReport.detailsAppend("Verify that Game loaded successfully", "Game should load", "Game not Loaded ,hence no further test cases are executed", "Fail");

				}
				
				if(cfnlib.quickSpinDisabledForDenmark())
					
				{
					denmarkReport.detailsAppend("Verify that quick spin is disable", "Quick spin should be disable", "Quick spin is disabled", "Pass");
				}
				
				else
				{
					denmarkReport.detailsAppend("Verify that quick spin is disable", "Quick spin should be disable", "Quick spin is not disabled", "Fail");
				}
				
				
				if(cfnlib.clockDisplayForDenmark())
				{
					denmarkReport.detailsAppend("Verify that clock is displayed", "Clock should be display", "Clock is displayed", "Pass");
				}
				else
				{
					denmarkReport.detailsAppend("Verify that clock is displayed", "Clock should be display", "Clock is not displayed", "Fail");
				}
				
				if(cfnlib.helpLinkDisplayForDenmark())
				{
					denmarkReport.detailsAppend("Verify that help Link is displayed", "Help Link should be display", "Help Link is displayed", "Pass");
				}
				else
				{
					denmarkReport.detailsAppend("Verify that help Link is displayed", "Help Link should be display", "Help Link is not displayed", "Fail");
				}
				if(cfnlib.linkToPlayerProtection())
				{
					denmarkReport.detailsAppend("Verify that Player Protection Link is displayed", "Player Protection Link should be display", "Player Protection Link is displayed", "Pass");
				}
				else
				{
					denmarkReport.detailsAppend("Verify that Player Protection Link is displayed", "Player Protection Link should be display", "Player Protection Link is not displayed", "Fail");
				}
					
			}
			else
			{
				log.debug("Webdriver is null hence execution stop");
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
			denmarkReport.endReport();
			//extent.flush();
			//add code to delete the testdata file copied
			webdriver.close();
			webdriver.quit();
			proxy.abort();
			Thread.sleep(1000);	   
		}	
	}

}
