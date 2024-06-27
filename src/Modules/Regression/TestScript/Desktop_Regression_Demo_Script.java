package Modules.Regression.TestScript;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.utils.Constant;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop_CS;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop_CS_Renovate;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop_Force;
import net.lightbody.bmp.BrowserMobProxyServer;


/**
 *  This is the script created for basic demo purpose functionalities, such as spin, bet and paytable.
 * @author AK47374
 *
 */

public class Desktop_Regression_Demo_Script{
	Logger log = Logger.getLogger(Desktop_Regression_Demo_Script.class.getName());
	public ScriptParameters scriptParameters;

	public void script() throws Exception{
		
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
		String Status=null;
		int mintDetailCount=0;
		//int mintSubStepNo=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		
		Desktop_HTML_Report report = new Desktop_HTML_Report(webdriver,Browsername,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,GameName);
		CFNLibrary_Desktop cfnlib=null;
		if(Framework.equalsIgnoreCase("PN")){
			cfnlib=new CFNLibrary_Desktop(webdriver,proxy,report,GameName);
		}else if(Framework.equalsIgnoreCase("Force")){
			cfnlib=new CFNLibrary_Desktop_Force(webdriver,proxy,report,GameName);
		}else if(Framework.equalsIgnoreCase("CS_Renovate")){
			cfnlib=new CFNLibrary_Desktop_CS_Renovate(webdriver,proxy,report,GameName);
		}else{
			cfnlib=new CFNLibrary_Desktop_CS(webdriver,proxy,report,GameName);
		}
		try{
			// Step 1 
			if(webdriver!=null)
			{		
				String url = cfnlib.XpathMap.get("ApplicationURL");
				String obj=cfnlib.func_navigate(url);	
				if(obj!=null)
				{
					report.detailsAppend("Open browser and Enter game URL in address bar and click Enter", "Island Paradise Lobby should be displayed", "Island Paradise lobby is displayed", "Pass");
				}
				else
				{
					report.detailsAppend("Open browser and Enter Game URL in address bar and click Enter", "Island Paradise lobby should be displayed", "Island paradise is not displayed", "Fail");	
				}
				
				log.debug("The New username is =="+ userName);
				String password = Constant.PASSWORD;
				String GameTitle=cfnlib.Func_LoginBaseScene(userName, password);
				if(GameTitle.trim()!=null)
				{
					report.detailsAppend("Check that user is able to login with username and password and  Title verified ", "User should be logged in successfully and "+GameName+" Game should be launched ", "Logged in succesfully and "+GameName+" is launched. ", "Pass");
				}
				else
				{
					report.detailsAppend("Check that user is not able to login with username and password", "User should be logged in successfully and "+GameName+" Game should be launched ", "Logged in succesfully and "+GameName+" is not launched. ", "Fail");
				}

				cfnlib.spinclick();
				Thread.sleep(13000);
				
				cfnlib.spinclick();
				Thread.sleep(13000);
				
				boolean b = cfnlib.open_TotalBet();
				if (b){
					report.detailsAppend("Verify Bet Amount Screen", "Bet Amount Screen should be display", "Bet Amount Screen displays", "Pass");
				}
				else{
					report.detailsAppend("Verify Bet Amount Screen", "Bet Amount Screen should be display", "Bet Amount Screen doesn't display", "Fail");   
				}
				cfnlib.close_TotalBet();
				Thread.sleep(1000);
				
				cfnlib.capturePaytableScreenshot(report, "");

				cfnlib.paytableClose();
			}
		}	    
		//-------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		//-------------------Closing the connections---------------//
		finally
		{
			report.endReport();
			webdriver.close();
			webdriver.quit();
			proxy.abort();
			Thread.sleep(1000);	   
		}	
	}
}
