package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;

public class DesktopRegressionBigWinScenario {
	Logger log = Logger.getLogger(DesktopRegressionBigWinScenario.class.getName());
	public ScriptParameters scriptParameters;
	public Desktop_HTML_Report report;
	public  WebDriver webdriver;

	public void script() throws Exception {
		String mstrTCName=scriptParameters.getMstrTCName();
		String mstrTCDesc=scriptParameters.getMstrTCDesc();
		String mstrModuleName=scriptParameters.getMstrModuleName();
		webdriver=scriptParameters.getDriver();
		BrowserMobProxyServer proxy=scriptParameters.getProxy();
		String startTime=scriptParameters.getStartTime();
		String filePath=scriptParameters.getFilePath();
		String userName=scriptParameters.getUserName();
		String browserName=scriptParameters.getBrowserName();
		String framework=scriptParameters.getFramework();
		String gameName=scriptParameters.getGameName();
		String status=null;
		int mintDetailCount=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;

		report = new Desktop_HTML_Report(webdriver, browserName, filePath, startTime, mstrTCName,
				mstrTCDesc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings, mintStepNo, status,
				gameName);
		CommonUtil util = new CommonUtil();
		List<String> copiedFiles=new ArrayList<>();
		RestAPILibrary apiobj=new RestAPILibrary();

		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, report, gameName);
		int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDDesktop"));
		

		try {
			if (webdriver != null) 
			{
				userName=util.randomStringgenerator();
				String strFileName=TestPropReader.getInstance().getProperty("BigWinTestDataPath");
				File testDataFile=new File(strFileName);
				
				if(	util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles))
				{	

				String url = cfnlib.XpathMap.get("ApplicationURL");
				String launchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
				log.info("url = " +launchURl);
				cfnlib.loadGame(launchURl);
				if(framework.equalsIgnoreCase("Force")){
					cfnlib.setNameSpace();
					
				}

				cfnlib.newFeature();

				cfnlib.bigWinWithSpin( report);
				cfnlib.validateMenuInBigWin(report);

				cfnlib.validatePaytableNavigationInBigWin(report);

				//	cfnlib.validateBigwinOTappingBetButton(report);

				cfnlib.bigWinQuickSpinOnOffValidation(report);

				cfnlib.bigwinwithAutoplay(report);

				cfnlib.bigWinOnMinimize(report);

				cfnlib.bigWinResizeValidation(report,2000, 2000);
				cfnlib.bigWinTabValidation(report);
				}else
				{
					log.debug("Unable to copy test data file on the environment hence skipping execution");
					report.detailsAppend("", "", "Unable to copy test data on server,hence skipping futher execution", "fail");

				}

			}
		}
		// -------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error(e.getMessage(),e);
			cfnlib.evalException(e);
		}
		finally {
			report.endReport();
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
