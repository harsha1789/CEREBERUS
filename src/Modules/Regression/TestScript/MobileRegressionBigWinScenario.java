package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;

import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import net.lightbody.bmp.BrowserMobProxyServer;

public class MobileRegressionBigWinScenario 
{
	Logger log = Logger.getLogger(MobileRegressionBigWinScenario .class.getName());
	public ScriptParameters scriptParameters;
	
	
	public void script() throws Exception{
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
		//int checkedOutDeviceNum=scriptParameters.getCheckedOutDeviceNum();
		
		String status=null;
		int mintDetailCount=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
	
		Mobile_HTML_Report report=new Mobile_HTML_Report(webdriver,deviceName,filePath,startTime,mstrTCName,mstrTCDesc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,status,gameName);
 
		MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, report, gameName);
		List<String> copiedFiles=new ArrayList<>();
		RestAPILibrary apiobj=new RestAPILibrary();
		CommonUtil util = new CommonUtil();
		int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDMobile"));
		cfnlib.setOsPlatform(scriptParameters.getOsPlatform());
		try{
			if(webdriver!=null)
			{		
				userName=util.randomStringgenerator();
				String strFileName=TestPropReader.getInstance().getProperty("MobileBigWinTestDataPath");
				File testDataFile=new File(strFileName);
				
				if(	util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles))
				{
				if(framework.equalsIgnoreCase("Force")){
					cfnlib.setNameSpace();
				}
				String url = cfnlib.xpathMap.get("ApplicationURL");
				String launchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
				if("LANDSCAPE".equalsIgnoreCase(cfnlib.xpathMap.get("Orientation")))
				{webdriver.context("NATIVE_APP");
				webdriver.rotate(ScreenOrientation.LANDSCAPE);
				webdriver.context("CHROMIUM");
				}
				cfnlib.loadGame(launchURl);

				if(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame")))
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
				cfnlib.bigWinWithSpin(report);
				cfnlib.validateMenuInBigWin( report);
				cfnlib.validatePaytableNavigationInBigWin( report);
				cfnlib.bigWinQuickSpinOnOffValidation(report);
				cfnlib.bigwinwithAutoplay(report);
				cfnlib.bigWinWithOrientationChange(report);
				}else{
					log.debug("Unable to copy test data file on the environment hence skipping execution");
					report.detailsAppend("", "", "Unable to copy test data on server,hence skipping futher execution", "fail");

				}
			}
		}
		//-------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error(e.getMessage(),e);
			}
		finally
		{
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
			proxy.abort();
			Thread.sleep(3000);
		}	

	}
}
