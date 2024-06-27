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
8. Bet setting page and bet cha
nges on bet setting is reflecting on base game.
 */

public class MobileSanitySuiteVideoPoker {
	Logger log = Logger.getLogger(MobileSanitySuiteVideoPoker.class.getName());
	public ScriptParameters scriptParameters;

	public void script() throws Exception{
		String mstrTCName=scriptParameters.getMstrTCName();
		String mstrTCDesc=scriptParameters.getMstrTCDesc();
		String mstrModuleName=scriptParameters.getMstrModuleName();
		BrowserMobProxyServer proxy=scriptParameters.getProxy();
		String startTime=scriptParameters.getStartTime();
		String filePath=scriptParameters.getFilePath();

		AppiumDriver<WebElement> webdriver=scriptParameters.getAppiumWebdriver();
		String deviceName=scriptParameters.getDeviceName();
		String userName=scriptParameters.getUserName();
		String frameWork=scriptParameters.getFramework();
		String gameName=scriptParameters.getGameName();
		//int checkedOutDeviceNum=scriptParameters.getCheckedOutDeviceNum();


		String status=null;
		int mintDetailCount=0;
		//int mintSubStepNo=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		String languageCode = null;
		Mobile_HTML_Report sanityReport=new Mobile_HTML_Report(webdriver,deviceName,filePath,startTime,mstrTCName,mstrTCDesc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,status,gameName);

		log.info("Report object created");
		log.info("Framework="+frameWork);

		MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(frameWork, webdriver, proxy, sanityReport, gameName);
		
		CommonUtil util = new CommonUtil();
		cfnlib.setOsPlatform(scriptParameters.getOsPlatform());
		try{
			int envid ;
			String lobbyName;
			boolean isSettingClose=false;
			RestAPILibrary apiObj = new RestAPILibrary();
			// Step 1
			if(webdriver!=null)
			{		
				if( TestPropReader.getInstance().getProperty("EnvironmentName")==null ||
						TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
				{
					lobbyName="bluemesa";
					TestPropReader.getInstance().setProperty("EnvironmentName","bluemesa");	
					envid=Integer.parseInt(TestPropReader.getInstance().getProperty("EnvironmentID"));
				}
				if(TestPropReader.getInstance().getProperty("EnvironmentName")!=null)
				{
					lobbyName=TestPropReader.getInstance().getProperty("EnvironmentName").toLowerCase();
				}


				userName=util.randomStringgenerator();

				//Implement code for test data copy depdeding on env
				int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
				int cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDMobile"));
				String strFileName=TestPropReader.getInstance().getProperty("MobileSanityTestDataPath");
				File testDataFile=new File(strFileName);
				List<String> copiedFiles=new ArrayList<>();
				if(	util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles))
					log.debug("Test dat is copy in test Server for Username="+userName);


				String url = cfnlib.xpathMap.get("ApplicationURL");
				String launchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
				log.debug("url = " +launchURl);

				if("LANDSCAPE".equalsIgnoreCase(cfnlib.xpathMap.get("Orientation")))
				{webdriver.context("NATIVE_APP");
				webdriver.rotate(ScreenOrientation.LANDSCAPE);
				webdriver.context("CHROMIUM");
				}

				sanityReport.detailsAppend("Sanity Suite Test cases", "Sanity Suite Test cases", "", "");

				if(cfnlib.loadGame(launchURl))
				{
					sanityReport.detailsAppend("Verify that Game loaded successfully", "Game should load", "Game Loaded successfully", "Pass");

					if(frameWork.equalsIgnoreCase("Force")){
						cfnlib.setNameSpace();
					}

					if(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame"))||(Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad"))))
					{
						cfnlib.newFeature();
						Thread.sleep(1000);
						cfnlib.funcFullScreen();
					}
					else
					{
						//to remove hand gesture
						cfnlib.funcFullScreen();
						Thread.sleep(1000);
						cfnlib.newFeature();
					}
					if(cfnlib.dealClick())
					{
						sanityReport.detailsAppend("Verify Deal button  functionality", "On Game load deal button should be clickable ", "Deal button is clickable on game load", "Pass");

						if(cfnlib.drawClick())
						{
							sanityReport.detailsAppend("Verify Draw button  functionality", " Draw button should be clickable ", "Draw button is clickable ", "Pass");
							cfnlib.drawCollectBaseGame(sanityReport,languageCode);

						}
						else
						{
							sanityReport.detailsAppend("Verify Draw button  functionality", " Draw button should be clickable ", "unable to click on Draw button ", "Fail");
						}
					}
					else
					{
						sanityReport.detailsAppend("Verify Deal functionality", "On Clicking deal button ,draw button should displayed", "Draw button is not displayed", "Fail");
					}


					//***********Checking double and collect functionality
					cfnlib.doubleToCollect(sanityReport);

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
						sanityReport.detailsAppend("Verify setting Screen", "On Clicking setting button ,Setting overlay should display", "Setting overlay is display display", "Pass");

						if(cfnlib.settingsBack())
							isSettingClose=true;
						log.debug("Setting overlay close");
					}
					else
					{
						//report
						//report.log(Status.FAIL, MarkupHelper.createLabel("Setting is not open",  ExtentColor.RED));
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
						double initialBetValue=cfnlib.getBetAmtDouble();
						if(cfnlib.openTotalBetBoolean())
						{
							//report and scrren shot
							sanityReport.detailsAppend("Verify bet setting Screen", "On Clicking bet button ,Bet Setting overlay should display", "Bet Setting overlay is display", "Pass");
							if(cfnlib.moveCoinSizeSlider())
							{
								double newBet=cfnlib.getBetAmtDouble();

								if(initialBetValue!=newBet)
								{
									//report
									sanityReport.detailsAppend("Verify bet Change in Consol", "On Changing bet  ,Bet should Change in consol", "Bet is change on consol", "Pass");

								}
								else
								{
									//report
									sanityReport.detailsAppend("Verify bet Change in Consol", "On Changing bet  ,Bet should Change in consol", "Bet is not change on consol", "Fail");

								}
							}

							else{

								sanityReport.detailsAppend("Verify bet Change in Consol", "On Changing bet  ,Bet should Change in consol", "Bet is not change on consol,as unable to move coin Slider", "Fail");
								cfnlib.closeTotalBet();
							}
						}
						else
						{
							//report and screen shot
							sanityReport.detailsAppend("Verify bet setting Screen", "On Clicking bet button ,Bet Setting overlay should display", "Bet Setting overlay is display", "Fail");

						}


					}
					else{
						sanityReport.detailsAppend("Verify setting Screen", "On Clicking setting button ,Setting overlay should display", "Setting overlay is not close ,hence aborting further test ", "Fail");

					}

				}else
				{
					sanityReport.detailsAppend("Verify that Game loaded successfully", "Game should load", "Game not Loaded ,hence no further test cases are executed", "Fail");

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
			sanityReport.endReport();
			webdriver.close();
			webdriver.quit();
			Thread.sleep(1000);
		}	
	}
}