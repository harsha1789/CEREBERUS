	package Modules.Regression.TestScript;

	import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop_CS;
import net.lightbody.bmp.BrowserMobProxyServer;

	/**
	 * This script verifies the Gibraltar check points.
	 * 1. Help should redirect to google
	 * @author Premlata
	 *
	 */
public class Desktop_Regression_RegulatoryMarket_Gibraltar{
	public ScriptParameters scriptParameters;

	public static Logger log = Logger.getLogger(Desktop_Regression_RegulatoryMarket_Gibraltar.class.getName());
	
	public void script() throws Exception {
		String mstrTC_Name=scriptParameters.getMstrTCName();
		String mstrTC_Desc=scriptParameters.getMstrTCDesc();
		String mstrModuleName=scriptParameters.getMstrModuleName();
		WebDriver webdriver=scriptParameters.getDriver();
		BrowserMobProxyServer proxy=scriptParameters.getProxy();
		String startTime=scriptParameters.getStartTime();
		String filePath=scriptParameters.getFilePath();
		String userName=scriptParameters.getUserName();
		String Browsername=scriptParameters.getBrowserName();
		String Framework=scriptParameters.getFramework();
		String GameName=scriptParameters.getGameName();
		String languageDescription=null;
		String languageCode=null;
		String Status=null;
		int mintDetailCount=0;
		//int mintSubStepNo=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		WebDriverWait wait;
		Desktop_HTML_Report Gibraltar=new Desktop_HTML_Report(webdriver,Browsername,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,GameName);
		CFNLibrary_Desktop cfnlib=null;
	

		if(Framework.equalsIgnoreCase("PN")){
			cfnlib=new CFNLibrary_Desktop(webdriver,proxy,Gibraltar,GameName);
			boolean canvasGame = true;
		}else{
			cfnlib=new CFNLibrary_Desktop_CS(webdriver,proxy,Gibraltar,GameName);
		}
		
		wait = new WebDriverWait(webdriver, 50);
		try{
			//-------------------Getting webdriver of proper test site---------------------//
			// Step 1 open lobby and verify lucky twins name
			Gibraltar.detailsAppend("Verify Gibraltor Regualtory markets features", "Gibraltor Regualtory markets Features should be displayed", "", "");
			if(webdriver!=null)
			{
			String title=cfnlib.func_navigate_DirectURL(cfnlib.XpathMap.get("app_url_Gibraltar_Desktop"));
			if(title!=null){
					Gibraltar.detailsAppend(" Verify that direct URL for Gibraltar is launched with help URL set as google URL ", "User should be able to open game with direct URL for Gibraltar ", "Game is launched successfully", "Pass");
				}
					else{
							Gibraltar.detailsAppend(" Verify that direct URL for Gibraltar is launched with help URL set as google URL ", "User should be able to open game with direct URL for Gibraltar ", "Game is not launched successfully", "Fail");
						}
			if(Framework.equalsIgnoreCase("Force")){
				cfnlib.setNameSpace();
				}
						//verifying Help Icon
						boolean helpIcon = cfnlib.verifyHelp();
						if (helpIcon) {
							Gibraltar.detailsAppend(
									"Verify The help icon now displays after the game loads " + GameName + " ",
									"Help Icon should be displayed", "Help icon is displayed on the game page", "Pass");
							System.out.println("Help Icon is visible on the screen");
	
						} else {
							Gibraltar.detailsAppend(
									"Verify The help icon now displays after the game loads" + GameName + " ",
									"Help Icon should be displayed", "Help icon is not displayed on the game page", "fail");
							System.out.println("Help icon is not present");
						}
				
						// Clicking on the help icon
						String googleTitle=cfnlib.clickHelp();
					

					// Verifying that the player is navigated to a new page(Help
					

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
		}
		finally {
			
			// ---------- Closing the report----------//
			Gibraltar.endReport();
			// ---------- Closing the webdriver ----------//
			webdriver.close();
			webdriver.quit();
		}
	}
}
