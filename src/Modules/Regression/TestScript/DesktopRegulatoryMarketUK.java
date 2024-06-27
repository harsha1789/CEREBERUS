package Modules.Regression.TestScript;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 * This script traverse and capture all the screen shots related to bonus unlock .
 * It reads the test data excel sheet for configured languages.
 * This script need test data  which will trigger bonus on first click and free spins
 * @author sg56207
 * */

public class DesktopRegulatoryMarketUK {
	Logger log = Logger.getLogger(DesktopRegulatoryMarketUK.class.getName());
	public ScriptParameters scriptParameters;


	public void script() throws Exception {
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
		String languageDescription=null;
		String languageCode=null;
		String status=null;
		int mintDetailCount=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;

		ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();
		Desktop_HTML_Report report = new Desktop_HTML_Report(webdriver, browserName, filePath, startTime, mstrTCName,
				mstrTCDesc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings, mintStepNo, status,
				gameName);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,report, gameName);


		CommonUtil util = new CommonUtil();
		int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int  cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDDesktop"));
		List<String> copiedFiles=new ArrayList<>();
		RestAPILibrary apiobj=new RestAPILibrary();

		try {
			if (webdriver != null) 
			{
				/*String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				WebDriverWait wait=new WebDriverWait(webdriver,60);
				String strFileName=TestPropReader.getInstance().getProperty("FreeSpinsTestDataPath");
				File testDataFile=new File(strFileName);
				
				userName=util.randomStringgenerator();
				
				log.debug("Test dat is copy in test Server for Username="+userName);
*/
					String url = cfnlib.XpathMap.get("ApplicationURL");
					String launchURL = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
					log.info("url = " +launchURL);
					if(cfnlib.loadGame(launchURL))
					{
					log.debug("navigated to url ");

					if(framework.equalsIgnoreCase("Force")){
						cfnlib.setNameSpace();
					}
					cfnlib.newFeature();
					// Click on Spin button
					report.detailsAppend("UK Regulatory Market", "", "", "");
					cfnlib.waitForSpinButton();
					//Verify thr game name on base Scane
					
					if(cfnlib.isGameNameVisibleInCurrentScene())
					{
						report.detailsAppend("In Game Clock Visible game play", "The clock must be visible  game play. ", "The clock visible game play. ", "Pass");
					}
					else
					{
						report.detailsAppend("In Game Clock Visible game play", "The clock must be visible  game play. ", "The clock is not visible game play. ", "Fail");

					}
				/*	
					
					// Spin duration must be above 2.5 secs
					
					long reelSpinTime=cfnlib.reelSpinDuration();
					
					if(reelSpinTime > 2.5)
					{
						report.detailsAppend("Reel spin duration should be more than 2.5 sec", "Reel spin duration  should be more than 2.5 sec", "Reel spin duration is more than 2.5 sec, the average time of  5 spins is :"+reelSpinTime +"Sec", "Pass");
					}
					else
					{
						report.detailsAppend("Reel spin duration should be more than 2.5 sec", "Reel  spin duration should be more than 2.5 sec", "Reel spin duration is less than 2.5 sec, the average time of  5 spins is : "+reelSpinTime +"Sec", "Fail");

					}
					//Check the in game Icon
					 //verify help page navigation from game
					 boolean help = cfnlib.verifyhelpenavigation(report);
					 if(help){
						 log.debug("Help navigation verified succesfully");
					 }
					 else {
						 report.detailsAppend("Verify that Navigation on Help screen", "Navigation on Help screen", "Navigation on Help screen not Done", "fail");
					 }
					 //verify navigation to responsible gaming from game menu
					 boolean respgame = cfnlib.verifyresponsiblegamingenavigation(report);
					 if(respgame){
						 log.debug("Responsible gameing navigation verified succesfully");
					 }
					 else {
						 report.detailsAppend("Verify that Navigation on ResponsibleGaming screen ", "Navigation on ResponsibleGaming screen","Navigation on ResponsibleGaming screen not Done", "fail");
					 }

					 //verify navigation to playcheck gaming from game menu
					 boolean playcheck = cfnlib.verifyplaychecknavigation(report);
					 if(playcheck){
						 log.debug("Navigation on playcheck succesfully");
					 }
					 else {
						 report.detailsAppend("Verify that Navigation on playcheck", "Navigation on playcheck", "Navigation on playcheck not Done", "fail");
					 }

					 //verify navigation to cashCheckButton gaming from game menu
					 boolean cashCheckButton =cfnlib.verifycashChecknavigation(report);
					 if(cashCheckButton){
						 log.debug("Navigated to Cashcheck page succesfully");
					 }
					 else {
						 report.detailsAppend("Verify that Navigation on cashCheck", "Navigation on cashCheck", "Navigation on cashCheck done", "fail");
					 }
					 ////verify navigation to loyaltyButton gaming from game menu
					 boolean loyaltyButton =cfnlib.verifyloyaltynavigation(report);
					 if(loyaltyButton){
						 log.debug("Navigated to Loyalty page succesfully");
					 }
					 else {
						 report.detailsAppend("Verify that Navigation on loyalty", "Navigation on loyalty", "Navigation on loyalty is not Done", "fail");
					 }*/
					 //Verify credit bubble 
					 //cfnlib.creditBubble();
					/*Verify autoplay refresh scenario
					 * */
					boolean onrefresh = cfnlib.isAutoplayOnAfterRefresh();

					if(onrefresh)
					{	
						report.detailsAppend("Autoplay Refresh","  After the refresh, autoplay should be terminated.","autoplay terminated.", "Pass");
					}
					else
					{
						report.detailsAppend("Autoplay Refresh","After the refresh, autoplay should be terminated", "autoplay not terminated.","Fail");	
					}

			}else
			{
				report.detailsAppend("Game load ", "should navigate to game link", "Fail to load game", "Fail");
			}
				}

	}
	// -------------------Handling the exception---------------------//
	catch (Exception e) {
		log.error(e.getMessage(),e);
		cfnlib.evalException(e);
	}
	// -------------------Closing the connections---------------//
	finally {
		report.endReport();
		/*if(!copiedFiles.isEmpty())
		{
			if(TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
				util.deleteBluemesaTestDataFiles(mid,cid,copiedFiles);
			else
				apiobj.deleteAxiomTestDataFiles(copiedFiles);
		}*/

		webdriver.close();
		webdriver.quit();
		Thread.sleep(1000);
	}
}


}
