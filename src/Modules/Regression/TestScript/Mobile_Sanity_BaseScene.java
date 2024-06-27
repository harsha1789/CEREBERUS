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

/**
 * This Script traverse all the panels and take the screen shot of the all screens such as paytable, bet settings, auto play etc.
 * It reads the test data excel sheet for configured languages.
 * 
 */

public class Mobile_Sanity_BaseScene {
	Logger log = Logger.getLogger(Mobile_Sanity_BaseScene.class.getName());
	public ScriptParameters scriptParameters;

		public void script() throws Exception{
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
			String osPlatform=scriptParameters.getOsPlatform();
			String osVersion=scriptParameters.getOsVersion();
			String Status=null;
			int mintDetailCount=0;
			//int mintSubStepNo=0;
			int mintPassed=0;
			int mintFailed=0;
			int mintWarnings=0;
			int mintStepNo=0;
			
		Mobile_HTML_Report sanityreport=new Mobile_HTML_Report(webdriver,DeviceName,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,gameName);

		MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, sanityreport, gameName);
		List<String> copiedFiles=new ArrayList<>();
		RestAPILibrary apiobj=new RestAPILibrary();
		CommonUtil util = new CommonUtil();
		int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDMobile"));
		cfnlib.setOsPlatform(scriptParameters.getOsPlatform());
		try{
			// Step 1
			if(webdriver!=null)
			{		
				
				userName=util.randomStringgenerator();
				String strFileName=TestPropReader.getInstance().getProperty("MobileSanityTestDataPath");
				File testDataFile=new File(strFileName);
				
				if(	util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles))
				{	
					
				String url = cfnlib.xpathMap.get("ApplicationURL");
				String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
				log.info("url = " +LaunchURl);
				if("LANDSCAPE".equalsIgnoreCase(cfnlib.xpathMap.get("Orientation")))
				{webdriver.context("NATIVE_APP");
				webdriver.rotate(ScreenOrientation.LANDSCAPE);
				webdriver.context("CHROMIUM");
				}
				
				if(cfnlib.loadGame(LaunchURl))
				{
				sanityreport.detailsAppend("Sanity Suite Test cases", "Sanity Suite Test cases", "", "");
				//cfnlib.Func_navigate(url);
				
				
				if(framework.equalsIgnoreCase("Force")){
					cfnlib.setNameSpace();
					}
				
				
				
				if(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame"))||(Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad"))))
				{
					cfnlib.funcFullScreen();
					Thread.sleep(1000);
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
				
				cfnlib.waitForSpinButton();
				Thread.sleep(1000);
				//verify  spin button state with quick spin scenario
				cfnlib.verifySpinBtnState(sanityreport);
				
				sanityreport.detailsAppend("Follwing are the Autoplay verificetion test case", "Verify Autoplay", "", "");

				
				// verifying autoplay scenarioes
				
				if(!(Constant.FORCE_ULTIMATE_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame"))) && !(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame")))){
					//Lets start with Auto play scenarios
					cfnlib.verifyAutoplayConsoleOptions(sanityreport);
					}
				
				// verify  the auto play panel 
				
				cfnlib.verifyAutoplayPanelOptions(sanityreport);
				
				// Below code is commmented as this scenarioes inclueded in Regression suite
				
				sanityreport.detailsAppend("Follwing are the Bet value verificetion test case", "Verify Bet value", "", "");

				cfnlib.verifyALLBetValues(sanityreport);
	
				sanityreport.detailsAppend("Follwing are the Quick Bet value verificetion test case", "Verify Quick Bet value", "", "");

				//Lets check the Quick bets
				cfnlib.verifyAllQuickBets(sanityreport);
				
				//Change Bet and refresh with out spin and verify previous bet value present or not
				boolean isBetChangedOnRefresh = cfnlib.isBetChangedOnRefresh();
				
				 if(isBetChangedOnRefresh){
					 sanityreport.detailsAppend("Verify that is Bet Changed On Refresh", "Is Bet Changed On Refresh","Bet Changed On Refresh", "Fail");	
					 System.out.println("isBetChangedOnRefresh :: FAIL");
					}else{
						sanityreport.detailsAppend("Verify that is is Bet Changed On Refresh", "Is Bet Changed On Refresh","Bet not Changed On Refresh", "Pass");	
						 System.out.println("isBetChangedOnRefresh :: PASS");
					}
					sanityreport.detailsAppend("Follwing are the MiniPaytable verificetion test case", "Verify Minipaytable ", "", "");
				
				if(!gameName.contains("Scratch") && 
					"no".equalsIgnoreCase(cfnlib.xpathMap.get("IsRespinFeature"))
					&& cfnlib.isPaytableAvailable())
				{
					//To validate Mini Paytable for all bets
					//In sanity check for one  bet and in include this in regression
					String curntBet;
					try {
						cfnlib.openBetPanel();
						Thread.sleep(1000);

							curntBet = cfnlib.setMaxBet();
							
							Thread.sleep(1000);
							cfnlib.closeTotalBet();
							String minimumbet= cfnlib.getMinimumBet();
							String betValue = curntBet;

							do {

								curntBet = betValue;
								
								cfnlib.validateMiniPaytable(curntBet,sanityreport);
								if(Class.forName(Thread.currentThread().getStackTrace()[1].getClassName()).equals("Modules.Regression.TestScript.Mobile_Regression_Suit"))
								{
								
								cfnlib.openBetPanel();
								Thread.sleep(1000);

								betValue = cfnlib.setTheNextLowBet();
								Thread.sleep(2000);
								cfnlib.closeTotalBet();
								}
								else
								{
								//	cfnlib.close_TotalBet();
									break;
								}
							} while (!betValue.equalsIgnoreCase(minimumbet));
						} catch (Exception e) {
							
							log.error(e.getMessage(),e);
						
						}
						
				 }
				
				sanityreport.detailsAppend("Follwing are the paytable verification test cases", "Verify paytable ", "", "");

				 if(!gameName.contains("Scratch") && cfnlib.isPaytableAvailable())
				 {
				// paytable  verification
					cfnlib.capturePaytableScreenshot(sanityreport, "paytable");
					cfnlib.paytableClose();
				 cfnlib.Payoutvarificationforallbet(sanityreport);
				 }
				 sanityreport.detailsAppend("Follwing are the MenuOption nevigation  test case", "Verify MenuOption nevigation", "", "");

				 cfnlib.verifyMenuOptionNavigations(sanityreport);
				 
				 
				}else
				{
					sanityreport.detailsAppend("Verify that the application should Logo and game name in Base scene in Practice Play", "Game logo and game name with base scene should display", "Game logo and game name with base scene is not display", "Fail");

				}
			}else{
				log.debug("Unable to copy test data file on the environment hence skipping execution");
				sanityreport.detailsAppend("", "", "Unable to copy test data on server,hence skipping futher execution", "fail");

			}
		}
	}
		//-------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error(e.getMessage(),e);
		cfnlib.evalException(e);
		}
		finally
		{
			sanityreport.endReport();
			if(!copiedFiles.isEmpty())
			{
				if(TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
						util.deleteBluemesaTestDataFiles(mid,cid,copiedFiles);
				else
						apiobj.deleteAxiomTestDataFiles(copiedFiles);
			}
			webdriver.close();
			webdriver.quit();
			Thread.sleep(3000);
		}	
	}
}