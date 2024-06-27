package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;


/**
 * Description:Sanity suite
 * Test cases:
 * 1] Spin button status
 * 2] Autoply Console options
 * 3] Autoply panel options
 * 4] QuickBet panel
 * 5] Bet panel
 * 6] Menu option nevigation
 * 7] Mini paytable
 * 8] Bet satus on refresh
 * 9] Paytable screenshots
 * 10]Payout for bet
 * 11] Requires Testdata
 * @author Premlata
 */

public class Desktop_Sanity_BaseScene{

	Logger log = Logger.getLogger(Desktop_Sanity_BaseScene.class.getName());
	public ScriptParameters scriptParameters;



	public void script() throws Exception{

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
		String status1=null;
		int mintDetailCount=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;

		Desktop_HTML_Report sanityReport = new Desktop_HTML_Report(webdriver, browserName, filePath, startTime,
				mstrTCName, mstrTCDesc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings,
				mintStepNo, status1, gameName);

		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, sanityReport, gameName);
		List<String> copiedFiles=new ArrayList<>();
		RestAPILibrary apiobj=new RestAPILibrary();
		CommonUtil util = new CommonUtil();
		
		int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDDesktop"));
		

		try{
			// Step 1 
			if(webdriver!=null)
			{		
				userName=util.randomStringgenerator();
				String strFileName=TestPropReader.getInstance().getProperty("SanityTestDataPath");
				File testDataFile=new File(strFileName);
				
				if(	util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles))
				{	
					log.debug("Test dat is copy in test Server for Username="+userName);
					String url = cfnlib.XpathMap.get("ApplicationURL");
					String launchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
					log.info("url = " +launchURl);
					cfnlib.loadGame(launchURl);
					sanityReport.detailsAppend("Sanity Suite Test cases", "Sanity Suite Test cases", "", "");
					if(framework.equalsIgnoreCase("Force")){
					cfnlib.setNameSpace();
				}
				// Check continue button exists and clicks
				cfnlib.newFeature();

				// wait for spin button
				cfnlib.waitForSpinButton();


				//verify  spin button state with quick spin scenario

				cfnlib.verifySpinBtnState(sanityReport);

				sanityReport.detailsAppend("Follwing are the Autoplay verificetion test case", "Verify Autoplay", "", "");

				if(!(Constant.FORCE_ULTIMATE_CONSOLE.equalsIgnoreCase(cfnlib.XpathMap.get("TypeOfGame")))&& !(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.XpathMap.get("TypeOfGame")))){
					//Lets start with Auto play scenarios
					cfnlib.verifyAutoplayConsoleOptions(sanityReport);
				}
				//open the auto play panel and start spinning

				cfnlib.verifyAutoplayPanelOptions(sanityReport);		


				//In sanity check for one bet and in include this in regression
				sanityReport.detailsAppend("Follwing are the Bet value verificetion test case", "Verify Bet value", "", "");
				cfnlib.verifyALLBetValues(sanityReport);

				//In sanity check for one quick bet and in include this in regression
				//Lets check the Quick bets
				sanityReport.detailsAppend("Follwing are the Quick Bet value verificetion test case", "Verify Quick Bet value", "", "");

				cfnlib.verifyAllQuickBets(sanityReport);


				//Change Bet and refresh with out spin and verify previous bet value present or not
				boolean isBetChangedOnRefresh = cfnlib.isBetChangedOnRefresh();

				if(isBetChangedOnRefresh){
					sanityReport.detailsAppend("Verify that is Bet Changed On Refresh", "Is Bet Changed On Refresh","Bet Changed On Refresh", "Fail");	
					log.info("isBetChangedOnRefresh :: FAIL");

				}else{
					sanityReport.detailsAppend("Verify that is is Bet Changed On Refresh", "Is Bet Changed On Refresh","Bet not Changed On Refresh", "Pass");	
					log.info("isBetChangedOnRefresh :: PASS");
				}

				sanityReport.detailsAppend("Follwing are the MiniPaytable verificetion test case", "Verify Minipaytable ", "", "");


				if(!gameName.contains("Scratch") && 
						"no".equalsIgnoreCase(cfnlib.XpathMap.get("IsRespinFeature"))
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
						cfnlib.close_TotalBet();
						String minimumbet= cfnlib.getMinimumBet();
						String betValue = curntBet;

						do {
							curntBet = betValue;

							cfnlib.validateMiniPaytable(curntBet,sanityReport);
							if(Class.forName(Thread.currentThread().getStackTrace()[1].getClassName()).equals("Modules.Regression.TestScript.Desktop_Regression_Suit"))
							{
								cfnlib.openBetPanel();
								Thread.sleep(1000);

								betValue = cfnlib.setTheNextLowBet();
								Thread.sleep(2000);
								cfnlib.close_TotalBet();
							}
							else
							{
								break;
							}
						} while (!betValue.equalsIgnoreCase(minimumbet));
					} catch (Exception e) {

						log.error(e.getStackTrace());
					}

				}
				sanityReport.detailsAppend("Follwing are the paytable verification test cases", "Verify paytable ", "", "");

				if(!gameName.contains("Scratch") && cfnlib.isPaytableAvailable())
					// paytable  verification
				{
					cfnlib.capturePaytableScreenshot(sanityReport, "paytable");

					cfnlib.paytableClose();

					// Payout verification
					// check for max bet payout in sanity and include all in regression
					cfnlib.Payoutvarificationforallbet(sanityReport);
				}

				sanityReport.detailsAppend("Follwing are the MenuOption nevigation  test case", "Verify MenuOption nevigation", "", "");

				//To verify menu nevigations
				cfnlib.verifyMenuOptionNavigations(sanityReport);
				}else{
					log.debug("Unable to copy test data file on the environment hence skipping execution");
					sanityReport.detailsAppend("", "", "Unable to copy test data on server,hence skipping futher execution", "fail");

				}
			}
		}	    
		//-------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error(e.getMessage(),e);
			cfnlib.evalException(e);
		}
		//-------------------Closing the connections---------------//
		finally
		{
			sanityReport.endReport();
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
			Thread.sleep(1000);	   
		}	
	}
}
