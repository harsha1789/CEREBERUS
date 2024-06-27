package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;


/**
 * @author sg56207
 * This script require wining player test data to check the scenario correctly
1. Test data creation and user creation through script 
2. Loading screen
3. Game is loading without error
4. Draw the cards and win functionality
	Held the cards
	Trigger win
5. Double feature
	Click on double button
	Verify double to screen
	Collect win on double feature and verify credit balance
6. Paytable is displaying
7. Setting page is displaying
8. Bet setting page and bet changes on bet setting is reflecting on base game.
 */

public class DesktopSanitySuiteVideoPoker
{

	Logger log = Logger.getLogger(DesktopSanitySuiteVideoPoker.class.getName());
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
		String languageCode = null;

		Desktop_HTML_Report sanityReport = new Desktop_HTML_Report(webdriver,browserName,filePath,startTime,mstrTCName,mstrTCDesc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,status1,gameName);

		log.info("Report object created");
		log.info("Framework="+framework);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, sanityReport, gameName);

		CommonUtil util = new CommonUtil();



		try{
			int envid ;
			String lobbyName;
			RestAPILibrary apiObj = new RestAPILibrary();


			boolean isSettingClose=false;
			// Step 1 
			if(webdriver!=null)
			{	


				if( TestPropReader.getInstance().getProperty("EnvironmentName")==null ||
						TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
				{
					envid=Integer.parseInt(TestPropReader.getInstance().getProperty("EnvironmentID"));
				}


				if(TestPropReader.getInstance().getProperty("EnvironmentName")!=null)
				{
					lobbyName=TestPropReader.getInstance().getProperty("EnvironmentName").toLowerCase();
				}
				else
				{
					lobbyName="bluemesa";
					TestPropReader.getInstance().setProperty("EnvironmentName","bluemesa");
				}

				userName=util.randomStringgenerator();

				//Implement code for test data copy depdeding on env
				int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
				int cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDDesktop"));
				String strFileName=TestPropReader.getInstance().getProperty("SanityTestDataPath");
				File testDataFile=new File(strFileName);
				List<String> copiedFiles=new ArrayList<>();
				if(	util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles))
					log.debug("Test dat is copy in test Server for Username="+userName);


				String url = cfnlib.XpathMap.get("ApplicationURL");
				String launchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
				log.info("url = " +launchURl);
				sanityReport.detailsAppend("Sanity Suite Test cases", "Sanity Suite Test cases", "", "");

				if(	cfnlib.loadGame(launchURl))
				{
					sanityReport.detailsAppend("Verify that Game loaded successfully", "Game should load", "Game Loaded successfully", "Pass");

					if(framework.equalsIgnoreCase("Force")){
						cfnlib.setNameSpace();
					}
					

					//*************Click on feature screen if present in the game
					cfnlib.newFeature();
					cfnlib.waitFor(cfnlib.XpathMap.get("DealButton"));
					
					
					if(cfnlib.dealClick())
					{
						sanityReport.detailsAppend("Verify Deal functionality", "On Clicking deal button ,draw button should displayed", "Draw button is displayed", "Pass");

						if(cfnlib.drawClick())
						{
							sanityReport.detailsAppend("Verify Draw functionality", "On Clicking draw button ,Collect and double to button should displayed", "Collect and double to button is displayed", "Pass");
							cfnlib.drawCollectBaseGame(sanityReport, languageCode);

						}

						else
						{
							sanityReport.detailsAppend("Verify Draw functionality", "On Clicking draw button ,Collect and double to button should displayed", "Collect and double to button is not displayed", "Fail");
						}		

					}
					else
					{
						sanityReport.detailsAppend("Verify Deal functionality", "On Clicking deal button ,draw button should displayed", "Draw button is not displayed", "Fail");
					}

					//***********Checking double and collect functionality
					cfnlib.doubleToCollect(sanityReport);
					
					cfnlib.moveCoinSizeSliderToMaxBet(sanityReport,languageCode);
					cfnlib.close_TotalBet();
					//************Verify paytable is present
					if(cfnlib.verifyPaytablePresent())
					{
						sanityReport.detailsAppend("Verify paytable Screen", "Paytable should display on base game", "Paytabley is display on base game", "Pass");
					}
					else
					{
						sanityReport.detailsAppend("Verify paytable Screen", "Paytable should display on base game", "Paytabley is not display on base game", "Fail");
					}

					/*verify the setting screen 
				1.click on hamburgur menu
				2.click on settings button
				3.take a screen shot and verify
					 */
					if(cfnlib.settingsOpen())
					{
						sanityReport.detailsAppend("Verify setting Screen", "On Clicking setting button ,Setting overlay should display", "Setting overlay is display", "Pass");

						cfnlib.func_Click(cfnlib.XpathMap.get("SettingClose"));
						isSettingClose = true;
						log.debug("Setting overlay close");
					}
					else
					{
						//report
						sanityReport.detailsAppend("Verify setting Screen", "On Clicking setting button ,Setting overlay should display", "Setting overlay is not display", "Fail");

					}

					/*Verify the bet settings
					 * 1.Read Initial bet
					 * 2.open bet settings
					 * 3.Take a scrren shot
					 * 4.change the bet by sliding the slider
					 * 5. Read the bet from console and verify
					 * */
					if(isSettingClose)
					{
						String initialBetValue=cfnlib.func_GetText(cfnlib.XpathMap.get("ConsoleBet"));
						if(cfnlib.open_TotalBet())
						{
							//report and scrren shot
							sanityReport.detailsAppend("Verify bet setting Screen", "On Clicking bet button ,Bet Setting overlay should display", "Bet Setting overlay is display", "Pass");

							cfnlib.close_TotalBet();
						}
						else
						{
							//report and screen shot
							sanityReport.detailsAppend("Verify bet setting Screen", "On Clicking bet button ,Bet Setting overlay should display", "Bet Setting overlay is display", "Fail");

						}
						cfnlib.moveCoinSizeSlider();
						String newBet=cfnlib.func_GetText(cfnlib.XpathMap.get("ConsoleBet"));

						if(!initialBetValue.equalsIgnoreCase(newBet))
						{
							//report
							sanityReport.detailsAppend("Verify bet Change in Consol", "On Changing bet  ,Bet should Change in consol", "Bet is change on consol", "Pass");

						}
						else
						{
							//report
							sanityReport.detailsAppend("Verify bet Change in Consol", "On Changing bet  ,Bet should Change in consol", "Bet is not change on consol", "Fail");

						}
						cfnlib.close_TotalBet();
					}
					else{
						sanityReport.detailsAppend("Verify setting Screen", "On Clicking setting button ,Setting overlay should display", "Setting overlay is not close ,hence aborting further test ", "Fail");

					}	
				}else
				{
					sanityReport.detailsAppend("Verify that Game loaded successfully", "Game should load", "Game not Loaded ,hence no further test cases are executed", "Fail");

				}
			}
			else
			{
				log.debug("Webdriver is null hence execution stop");
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
			webdriver.close();
			webdriver.quit();
			proxy.abort();
			Thread.sleep(1000);	   
		}	
	}
}
