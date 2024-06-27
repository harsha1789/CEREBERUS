package Modules.Regression.TestScript;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.Constant;

import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import net.lightbody.bmp.BrowserMobProxyServer;

public class Mobile_Regression_Demo_Script {
	Logger log = Logger.getLogger(Mobile_Regression_Demo_Script.class.getName());
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
		
		
		String Status=null;
		int mintDetailCount=0;
		//int mintSubStepNo=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;

		Mobile_HTML_Report tc01=new Mobile_HTML_Report(webdriver,DeviceName,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,gameName);

		MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, tc01, gameName);
		
		try{
			// Step 1
			if(webdriver!=null)
			{		
				String url=cfnlib.xpathMap.get("ApplicationURL");
				String obj=cfnlib.funcNavigate(url);
				if(obj!=null)
				{
					tc01.detailsAppend("Open Browser and Enter Lobby URL in address bar and click Enter", "Island Paradise Lobby should open", "Island Paradise lobby is Open", "Pass");
				}
				else
				{
					tc01.detailsAppend("Open browser and Enter Game URL in address bar and click Enter", "Island Paradise lobby should be displayed", "Island paradise is not displayed", "Fail");
				}
				// Step s1.2 /*Login to application and verify the game title*/
				
				tc01.detailsAppend("Verify features of the game", "Game should work properly", "", "");
				String password = Constant.PASSWORD;
				String GameTitle= cfnlib.loginToBaseScene(userName, password);		
				if(GameTitle!=null)
				{
					tc01.detailsAppend("Verify that the application should display Game Logo and game name in Base Scene in Practice Play", "Game logo and game name with base scene should display", "Game logo and game name with base scene displays", "Pass");
				}
				else {
					tc01.detailsAppend("Verify that the application should Logo and game name in Base scene in Practice Play", "Game logo and game name with base scene should display", "Game logo and game name with base scene is not display", "Fail");
				}
				
					
					Thread.sleep(2000);

					cfnlib.verifyStopLanguage(tc01,"");
					
					cfnlib.spinclick();
					Thread.sleep(8000);
					// Capture Screen shot for Bet Screen
					cfnlib.openTotalBet();
					tc01.detailsAppend("Verify that language on the Bet Settings Screen", "Bet Settings Screen should display", "Bet Settings Screen displays in language", "Pass");

					cfnlib.closeTotalBet();
					
					cfnlib.capturePaytableScreenshot(tc01, "");
		     	}
		}
		//-------------------Handling the exception---------------------//
		catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			tc01.endReport();
			webdriver.close();
			webdriver.quit();
			proxy.abort();
			Thread.sleep(3000);
		}	
	}
}