package Modules.Regression.TestScript;

import java.io.File;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 * 
 * This is the script verifies the poker star  GTR scenarios.
 * This implementation is for force.
 * Status : implementation is inprogress.
 * @author Snehal
 *
 */

public class Desktop_Regression_GTR_Pokerstar {
	Logger log = Logger.getLogger(Desktop_Regression_GTR_Pokerstar.class.getName());
	public ScriptParameters scriptParameters;
	
		
	
	
	
	public void script() throws Exception{
		String mstrTC_Name=scriptParameters.getMstrTCName();
		String mstrTC_Desc=scriptParameters.getMstrTCDesc();
		String mstrModuleName=scriptParameters.getMstrModuleName();
		WebDriver webdriver=scriptParameters.getDriver();
		BrowserMobProxyServer proxy=scriptParameters.getProxy();
		String startTime=scriptParameters.getStartTime();
		String filePath=scriptParameters.getFilePath();
		String Browsername=scriptParameters.getBrowserName();
		String framework=scriptParameters.getFramework();
		String gameName=scriptParameters.getGameName();
		
		String Status=null;
		int mintDetailCount=0;
		//int mintSubStepNo=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		
		 File destFile;
		 String classname = this.getClass().getSimpleName();
		 String xmlFilePath = System.getProperty("user.dir") + "\\src\\Modules\\Regression\\TestData\\GameData\\"
				+ classname + ".testdata";
		 File sourceFile = new File(xmlFilePath);
		Desktop_HTML_Report gtr_pokerstar = new Desktop_HTML_Report(webdriver,Browsername,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,gameName);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, gtr_pokerstar, gameName);

		
		//Getting PokerStart Lobby url
		String url = cfnlib.XpathMap.get("PokerStarLobby_Url");
		
		String obj = cfnlib.pokerStar_game_load(url);
		if(obj!=null)
		{
			gtr_pokerstar.detailsAppend("Open Browser and Enter PokerstarLobby URL in address bar and redirect to pokerstar game", "Redirected Game Should Open", "Redirected game is open", "Pass");
		}
		else{
			gtr_pokerstar.detailsAppend("Open Browser and Enter PokerstarLobby URL in address bar and redirect to pokerstar game", "Redirected Game Should Open", "Redirected game is not open", "Fail");
		}
		
		
		
		
		
		
	}
}
