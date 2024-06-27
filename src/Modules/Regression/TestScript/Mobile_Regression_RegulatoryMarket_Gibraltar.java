package Modules.Regression.TestScript;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Mobile_HTML_Report;

import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 * This script verifies the Gibraltar check points.
 * @author Premlata
 *
 */
public class Mobile_Regression_RegulatoryMarket_Gibraltar {
	
	public Logger log = Logger.getLogger(Desktop_Regression_RegulatoryMarket_Gibraltar.class.getName());
	public ScriptParameters scriptParameters;
	public void script() throws Exception {
		String mstrTC_Name=scriptParameters.getMstrTCName();
		String mstrTC_Desc=scriptParameters.getMstrTCDesc();
		String mstrModuleName=scriptParameters.getMstrModuleName();
		BrowserMobProxyServer proxy=scriptParameters.getProxy();
		String startTime=scriptParameters.getStartTime();
		String filePath=scriptParameters.getFilePath();
		
		//AndroidDriver<WebElement> webdriver=scriptParameters.getWebdriver();
		AppiumDriver<WebElement> webdriver=scriptParameters.getAppiumWebdriver();
		
		String DeviceName=scriptParameters.getDeviceName();
		String userName=scriptParameters.getUserName();
		String framework=scriptParameters.getFramework();
		//String deviceLockKey=scriptParameters.getDeviceLockKey();
		String gameName=scriptParameters.getGameName();
		//int checkedOutDeviceNum=scriptParameters.getCheckedOutDeviceNum();
		
		
		String Status=null;
		String obj,GameTitle=null;
		int mintDetailCount=0;
		//int mintSubStepNo=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		
		webdriver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
		Mobile_HTML_Report Gibraltar=new Mobile_HTML_Report(webdriver,DeviceName,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,gameName);
		MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, Gibraltar, gameName);
		
		
		WebDriverWait wait = new WebDriverWait(webdriver, 50);
		try{
			@SuppressWarnings("unused")
			
			String applicationName = null;

			//-------------------Getting webdriver of proper test site---------------------//
			// Step 1 open lobby and verify lucky twins name
			Gibraltar.detailsAppend("Verify Gibraltor Regualtory markets features", "Gibraltor Regualtory markets Features should be displayed", "", "");
			if(webdriver!=null)
			{
				String url= cfnlib.xpathMap.get("app_url_Gibraltar_Mobile");
					obj=cfnlib.funcNavigateDirectURL(url);
					if(obj!=null){

						Gibraltar.detailsAppend(" Verify that direct URL for Gibraltar is launched with help URL set as google URL ", "User should be able to open game with direct URL for Gibraltar ", "Game is launched successfully", "Pass");
					}
					else{

						Gibraltar.detailsAppend(" Verify that direct URL for Gibraltar is launched with help URL set as google URL ", "User should be able to open game with direct URL for Gibraltar ", "Game is not launched successfully", "Fail");
					}
					if(framework.equalsIgnoreCase("Force")){
						cfnlib.setNameSpace();
						}				
					//verifying Help Icon
					
					boolean helpIcon = cfnlib.verifyHelp();
					if (helpIcon) {
						Gibraltar.detailsAppend(
								"Verify The help icon now displays after the " + gameName + " game loads ",
								"Help Icon should be displayed", "Help icon is displayed on the game page", "Pass");
						System.out.println("Help Icon is visible on the screen");

					} else {

						Gibraltar.detailsAppend(
								"Verify The help icon now displays after the " + gameName + " game loads ",
								"Help Icon should be displayed", "Help icon is not displayed on the game page", "Fail");
						System.out.println("Help icon is not present");
					}
					
					// Clicking on the help icon and Verifying that the player is navigated to a new page of Help
					String googleTitle=cfnlib.clickHelp();

				if (googleTitle.equalsIgnoreCase("Google")) {
					Gibraltar.detailsAppend(
							"Verify the help icon should be navigated to Google Page ",
							"Help page should be navigated", "Help page is sucessfully navigated to Google Page", "pass");

				} else {
					Gibraltar.detailsAppend(
							"Verify the help icon should be navigated to Google Page ",
							"Help page should be navigated", "Help page is unable to navigate to Google Page", "fail");
	
				}
			
			}
		
		}
		catch (Exception e) {
			log.error(e.getMessage(),e);
			cfnlib.evalException(e);
			//ExceptionHandler eHandle = new ExceptionHandler(e, webdriver, Tc15);
			//eHandle.evalException();
		}

		finally {
			// Global.browserproxy.abort();
			// ---------- Closing the report----------//

			Gibraltar.endReport();
			// ---------- Closing the webdriver ----------//

			webdriver.close();
			webdriver.quit();
			// webdriver.quit();
			// Global.appiumService.stop();

		}
	}
}
