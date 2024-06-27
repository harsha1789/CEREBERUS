package Modules.Regression.TestScript;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;

import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 *This script is sanity check for IOs 15 and above version
 *@author sg56207
 */

public class  MobileVerificationLVC {
	Logger log = Logger.getLogger(MobileVerificationLVC.class.getName());

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
		String framework=scriptParameters.getFramework();
		String gameName=scriptParameters.getGameName();


		String status=null;
		int mintDetailCount=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		String urlNew=null;
		ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();
		Mobile_HTML_Report lvcReport=new Mobile_HTML_Report(webdriver,deviceName,filePath,startTime,mstrTCName,mstrTCDesc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,status,gameName);

		log.info("Framework"+framework);
		MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, lvcReport, gameName);

		CommonUtil util = new CommonUtil();
		cfnlib.setOsPlatform(scriptParameters.getOsPlatform());
		int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int  cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDMobile"));
		List<String> copiedFiles=new ArrayList<>();
		RestAPILibrary apiobj=new RestAPILibrary();

		try{
			// Step 1
			if(webdriver!=null)
			{		
				Map<String, String> rowData2 = null;
				Map<String, String> rowData3 = null;
				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				int languageCnt = excelpoolmanager.rowCount(testDataExcelPath, Constant.LANG_XL_SHEET_NAME);

				String url = cfnlib.xpathMap.get("ApplicationURL");
				String offerExpirationUtcDate=util.getCurrentTimeWithDateAndTime();
				//creating random player
				String strFileName=TestPropReader.getInstance().getProperty("MobileFreeSpinsTestDataPath");
				File testDataFile=new File(strFileName);
				
				List<Map<String, String>> currencyList= util.readCurrList();// mapping

				
				
				for (Map<String, String> currencyMap:currencyList) 
				{
								
						String isoCode = currencyMap.get(Constant.ISOCODE).trim();
						String currencyID = currencyMap.get(Constant.ID).trim();
						String currencyName = currencyMap.get(Constant.ISONAME).trim();
						String languageCurrency = currencyMap.get(Constant.LANGUAGECURRENCY).trim();
						String currencyFormat=currencyMap.get(Constant.DISPALYFORMAT).trim();
						String regExpression=currencyMap.get(Constant.REGEXPRESSION).trim();

				
				for(int orientation=0;orientation<2;orientation++) {

				userName=util.randomStringgenerator();
				if(util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles,currencyID,isoCode))
				{
					log.debug("Test dat is copy in test Server for Username="+userName);

					if(orientation==0 && !webdriver.getOrientation().equals(ScreenOrientation.PORTRAIT))
					{
						String context=webdriver.getContext();
						webdriver.context("NATIVE_APP");
						webdriver.rotate(ScreenOrientation.PORTRAIT);
						webdriver.context(context);
					}else if(orientation==1 && !webdriver.getOrientation().equals(ScreenOrientation.LANDSCAPE)){
						String context=webdriver.getContext();
						webdriver.context("NATIVE_APP");
						webdriver.rotate(ScreenOrientation.LANDSCAPE);
						webdriver.context(context);
					}

					String launchURL = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
					
					cfnlib.loadGame(launchURL);
					log.debug("Url Loaded= "+launchURL);

					Thread.sleep(20000);
					
					
				
					//new feature and continue button verification 
					if(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame"))||(Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad"))))
					{
						cfnlib.newFeature();
						lvcReport.detailsAppendFolder(" Verify that the application should display new feature baner or continue button on game load", " New feature baner or continue button on game load", " New feature baner or continue button on game load", "Pass", currencyName);
						cfnlib.funcFullScreen();
						Thread.sleep(2000);
								
					}
					else
					{
						lvcReport.detailsAppendFolder(" Verify that the application should display new feature baner or continue button on game load", " New feature baner or continue button on game load", " New feature baner or continue button on game load", "Pass", currencyName);
						cfnlib.funcFullScreen();
						Thread.sleep(2000);
						cfnlib.newFeature();

					}
						

					cfnlib.waitForSpinButton();
					Thread.sleep(2000);

					if(framework.equalsIgnoreCase("Force"))
					{
						cfnlib.setNameSpace();
					}
					Thread.sleep(3000);


					//Capture Screen shot for Bet Screen
					if(cfnlib.openTotalBet())
					{
						lvcReport.detailsAppendFolder("Verify Bet on the Screen", "Bet Screen should be display", "Bet Screen should be displayed", "Pass", currencyName);
						Thread.sleep(2000);
						cfnlib.closeTotalBet();
						log.debug("Done for bet");
					}
					else
					{
						lvcReport.detailsAppendFolder("Verify Bet on the Screen", "Bet Screen should be display", "Bet Screen is not displayed", "fail", currencyName);
						log.debug("fail to check bet panel");
					}

					//Autoplay panel verification
					boolean openAutoplay = cfnlib.open_Autoplay();
					if (openAutoplay)
					{
						log.debug("Autoplay  open");
						lvcReport.detailsAppendFolder("Verify Autoplay on the Screen", "Autoplay Screen should be display", "Autoplay Screen should be displayed", "Pass", currencyName);
						Thread.sleep(2000);
						cfnlib.close_Autoplay();
						log.debug("Done for Autoplay");

					}
					else
					{
						lvcReport.detailsAppendFolder("Verify Autoplay on the Screen", "Autoplay Screen should be display", "Autoplay Screen is not displayed", "Fail", currencyName);
					}
						
					//menu verification
					if(!framework.equalsIgnoreCase("CS_Renovate")){
					boolean menuOpen = cfnlib.menuOpen();
					if(menuOpen)
					{
						lvcReport.detailsAppendFolder("Verify menu on the Screen", "Menu Screen should be display", "Menu Screen should be displayed", "Pass", currencyName);
						Thread.sleep(2000);
						cfnlib.menuClose();
						Thread.sleep(2000);
						log.debug("Done for Menu");
					}
					else
					{
						lvcReport.detailsAppendFolder("Verify menu on the Screen", "Menu Screen should be display", "Menu Screen is not displayed", "Fail", currencyName);
					}
					
					}
					
					//Setting verification
					if(!framework.equalsIgnoreCase("CS")&&cfnlib.checkAvilability(cfnlib.xpathMap.get("isMenuSettingsBtnVisible")))
					{
						boolean openSetting= cfnlib.settingsOpen();
						if(openSetting)
						{
							Thread.sleep(2000);
							lvcReport.detailsAppendFolder("Verify Settings on the Screen", "Settings Screen should be display", "Settings Screen should be displayed", "Pass", currencyName);							
							Thread.sleep(2000);
							cfnlib.settingsBack();
							log.debug("Done for Setting");
						}
						else 
						{
							lvcReport.detailsAppendFolder("Verify Settings on the Screen", "Settings Screen should be display", "Settings Screen is not displayed", "Pass", currencyName);
						}
								
					 }

					//pay table verification
					if(!gameName.contains("Scratch") && cfnlib.checkAvilability(cfnlib.xpathMap.get("isMenuPaytableBtnVisible")))
					{
						//Open pay table and capture screen shots
						cfnlib.capturePaytableScreenshot(lvcReport, currencyName);
						if(!framework.equalsIgnoreCase("CS"))
							cfnlib.paytableClose();
						log.debug("Done for paytable");
					}

					// To open and capture Story in Game (Applicable for the game like immortal romances)
					if(cfnlib.checkAvilability(cfnlib.xpathMap.get("isPaytableStoryExists")))
					{
						cfnlib.Verifystoryoptioninpaytable(lvcReport, currencyName);
						cfnlib.paytableClose();
						log.debug("Done for story in paytable");
					}
							
					
					//Base Game - During Game Play
					cfnlib.spinclick();
					lvcReport.detailsAppendFolder("Verify Base scene during game play"," ","verify screenshot ","pass",currencyName);
					cfnlib.waitForSpinButtonstop();
					Thread.sleep(2000);
					
					//Base Game - Regular Win
					String currentBet = cfnlib.getCurrentBet();
					cfnlib.validateCreditForWinLoss(currentBet,lvcReport,currencyName);
					Thread.sleep(3000);
					
					//Base Game - Big Win
					cfnlib.spinclick();
					cfnlib.waitForSpinButton();
					boolean result=cfnlib.waitForbigwin();
					if(result)
					{
						log.debug("Bigwin display in scene");
						lvcReport.detailsAppendFolder("Verify that big win scean is displaying ","Big win screen must display ,Once big win triggers ","On  triggering big win,Big win screen is displaying","Pass",currencyName);

						// set BigWinlayers = yes if their is bigwin,superwin, megawin layers are present in the game to take 3 screenshots
						if("Yes".equalsIgnoreCase(cfnlib.xpathMap.get("BigWinlayers")))
						{
							for(int i =0;i<=2;i++)
							{
								cfnlib.waitForbigwin();
								lvcReport.detailsAppendFolder("Verify that big win screen is displaying with overlay ","Big win screen must display ,Once big win triggers ","On  triggering big win,Big win screen is displaying","Pass",currencyName);
								log.debug("Bigwinlayer captured" + i);
							}	
						}
					}
					else
					{	
						lvcReport.detailsAppendFolder("Verify that big win screen is displaying ","Big win screen must display ,Once big win triggers ","On  triggering big win,Big win screen is not displaying","Fail",currencyName);
					}
					Thread.sleep(10000);
					cfnlib.waitForSpinButtonstop();

					
					//verify Game Feature Free spin 
					cfnlib.spinclick();
					if( TestPropReader.getInstance().getProperty("IsFreeSpinAvailable").equalsIgnoreCase("yes"))
					{
								
						Thread.sleep(3000);
						cfnlib.waitForWinDisplay();
						String freeSpinEntryScreen = cfnlib.xpathMap.get("FreeSpinEntryScreen");
						String str = cfnlib.entryScreen_Wait(freeSpinEntryScreen);
						if (str.equalsIgnoreCase("freeSpin")) 
						{
							log.debug("Inside free spin");
							cfnlib.entryScreen_Wait(freeSpinEntryScreen);
		
							if("yes".equals(cfnlib.xpathMap.get("isFreeSpinSelectionAvailable")))
							{
								cfnlib.clickBonusSelection(1);
							}
							else 
							{
								if( TestPropReader.getInstance().getProperty("IsProgressiveGame").equalsIgnoreCase("Yes"))
								{
									cfnlib.FSSceneLoading();
									cfnlib.clickToContinue();
									cfnlib.FS_continue();
								}
								else
								{
									//Click on free spins into continue button
									if("yes".equals(cfnlib.xpathMap.get("isFreeSpinIntroContinueBtnAvailable")))
									{
											cfnlib.clickToContinue();
									}
									else
									{
										System.out.println("There is not Freespins Into Continue button in this game");
										log.debug("There is not Freespins Into Continue button in this game");
									}
								}
							}
						} 
								
						Thread.sleep(3000);
								
						cfnlib.FSSceneLoading();
								
						if(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame")))
						{
							Thread.sleep(2000);
						}
						log.debug("waiting for freespins summary");
						cfnlib.waitSummaryScreen();
						}
					
				
					
				}else
				{
					lvcReport.detailsAppend("unable to copy test dat to server ", " ", "", "");

				}
				}
			}
			}
		}



		//-------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		finally
		{
			lvcReport.endReport();
			if(!copiedFiles.isEmpty())
			{
				if(TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
					util.deleteBluemesaTestDataFiles(mid,cid,copiedFiles);
				else
					apiobj.deleteAxiomTestDataFiles(copiedFiles);
			}
			webdriver.close();
			webdriver.quit();
			//proxy.abort();
			Thread.sleep(3000);
		}	
	}
}