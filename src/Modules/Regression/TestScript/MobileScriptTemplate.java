package Modules.Regression.TestScript;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 * This is a template to create Mobile Script.
 * @author Premlata
 */

public class MobileScriptTemplate {
	Logger log = Logger.getLogger(MobileScriptTemplate.class.getName());

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
		//String deviceLockKey=scriptParameters.getDeviceLockKey();
		String gameName=scriptParameters.getGameName();
		//int checkedOutDeviceNum=scriptParameters.getCheckedOutDeviceNum();
		String osPlatform=scriptParameters.getOsPlatform();
		String osVersion=scriptParameters.getOsVersion();


		String Status=null;
		int mintDetailCount=0;
		//int mintSubStepNo=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		String urlNew=null;
		ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();
		Mobile_HTML_Report language=new Mobile_HTML_Report(webdriver,DeviceName,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,gameName);

		log.info("Framework"+framework);
		MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, language, gameName);
		
		cfnlib.setOsPlatform(osPlatform);
		cfnlib.setOsVersion(osVersion);
		try{
			// Step 1
			if(webdriver!=null)
			{		
				Map<String, String> rowData2 = null;
				Map<String, String> rowData3 = null;
				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				String url = cfnlib.xpathMap.get("ApplicationURL");
				String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
				System.out.println("url = " +LaunchURl);
				if("LANDSCAPE".equalsIgnoreCase(cfnlib.xpathMap.get("Orientation")))
				{
					String context=webdriver.getContext();
				webdriver.context("NATIVE_APP");
				webdriver.rotate(ScreenOrientation.LANDSCAPE);
				webdriver.context(context);
				}
				cfnlib.loadGame(LaunchURl);
				cfnlib.threadSleep(15000);
				}
		}

			 	

			//-------------------Handling the exception---------------------//
			catch (Exception e) {
				log.error(e.getMessage(),e);
			}
			finally
			{
				language.endReport();
				webdriver.close();
				webdriver.quit();
				//proxy.abort();
				Thread.sleep(3000);
			}	
		}
	}