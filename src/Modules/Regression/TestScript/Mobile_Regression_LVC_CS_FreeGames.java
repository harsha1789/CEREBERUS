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
 * This Script is for Low Value Currency - Free Games
 * ===========================================================================
 * 1.Free Games
 * ===========
 * 1.1.Summary Screen Validation - Info , Pay Now, Pay Later , Delete Offer, Menu 
 * 1.2.Summary Screen - Info Icon - Amount Validation 
 * 1.3.Clicking on PayNow Button 
 * 1.4.Credit Value Validation 
 * 1.5.Win , Big Win
 * 1.6.Refresh - Validate Resume Button & Info Icon - Amount Validation 
 * 1.7 Back to Base Game
 * 
 * 2.TestData
 * =============
 * 2.1 Normal Win
 * 2.2 Big Win
 * 2.3 Big Win - On Refresh 
 *
 * 
 * 
 * @author rk61073
 *
 */
public class Mobile_Regression_LVC_CS_FreeGames
{
	
	public static Logger log = Logger.getLogger(Mobile_Regression_LVC_CS_FreeGames.class.getName());
	public ScriptParameters scriptParameters;

	public void script() throws Exception 
	{

		String mstrTC_Name=scriptParameters.getMstrTCName();
		String mstrTC_Desc=scriptParameters.getMstrTCDesc();
		String mstrModuleName=scriptParameters.getMstrModuleName();
		BrowserMobProxyServer proxy=scriptParameters.getProxy();
		String startTime=scriptParameters.getStartTime();
		String filePath=scriptParameters.getFilePath();
		AppiumDriver<WebElement> webdriver=scriptParameters.getAppiumWebdriver();
		String userName=scriptParameters.getUserName();
		String DeviceName=scriptParameters.getDeviceName();
		String framework=scriptParameters.getFramework();
		String gameName=scriptParameters.getGameName();
		String language = "Paytable";
		String Status=null;
		int mintDetailCount=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;

		ExcelDataPoolManager excelpoolmanager = new ExcelDataPoolManager();
		Mobile_HTML_Report lvcReport =new Mobile_HTML_Report(webdriver,DeviceName,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,gameName);
		MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,lvcReport, gameName);

		cfnlib.setOsPlatform(scriptParameters.getOsPlatform()); //To get OSPlatform
		CommonUtil util = new CommonUtil();
		RestAPILibrary apiobj = new RestAPILibrary();

		List<String> copiedFiles = new ArrayList<>();
		int mid = Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		System.out.println("MID: " + mid);log.debug("MID: " + mid);
		int cid = Integer.parseInt(TestPropReader.getInstance().getProperty("CIDMobile"));
		System.out.println("CID: " + cid);log.debug("CID: " + cid);
	
		
		
		try 
		{
			// -------Getting webdriver of proper test site-------//
			if (webdriver != null) 
			{

				String strFileName = TestPropReader.getInstance().getProperty("FreeGamesTD");
				File testDataFile = new File(strFileName);

				String testDataExcelPath = TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				int languageCnt = excelpoolmanager.rowCount(testDataExcelPath, Constant.LANG_XL_SHEET_NAME);

				String offerExpirationUtcDate = util.getCurrentTimeWithDateAndTime();
				String strdefaultNoOfFreeGames = cfnlib.xpathMap.get("DefaultNoOfFreeGames");
				System.out.println("Number of Free Games Assigned : " + strdefaultNoOfFreeGames);
				log.debug("Number of Free Games Assigned : " + strdefaultNoOfFreeGames);
				int defaultNoOfFreeGames = (int) Double.parseDouble(strdefaultNoOfFreeGames);
				boolean isGameLoaded = false;

				List<Map<String, String>> currencyList = util.readCurrList();// mapping
				String url = cfnlib.xpathMap.get("ApplicationURL");

				for (Map<String, String> currencyMap : currencyList) 
				{
					try 
					{
						String isoCode = currencyMap.get(Constant.ISOCODE).trim();
						String currencyID = currencyMap.get(Constant.ID).trim();
						String currencyName = currencyMap.get(Constant.ISONAME).trim();
						String languageCurrency = currencyMap.get(Constant.LANGUAGECURRENCY).trim();
						String regExpr=currencyMap.get(Constant.REGEXPRESSION).trim();
						String regExprNoSymbol=currencyMap.get(Constant.REGEXPRESSION_NOSYMBOL).trim();

						System.out.println("Currency Name :  " + currencyName);
						log.debug("Currency Name :  " + currencyName);
						lvcReport.detailsAppendNoScreenshot("Currency Name is " + currencyName,"Currency ID is " + currencyID, "ISO Code is " + isoCode, "PASS");

						boolean isFreeGameAssigned = false;
						// verifying free games
						int maxFGRetCount = 3;int count = 0;
						while (count < maxFGRetCount)
						{
							// Generating Random user
							userName = util.randomStringgenerator();
							System.out.println("Username:" + userName);log.debug("Username:" + userName);

							// Copy Test data to server
							if (util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles, currencyID,isoCode))
							{
								log.debug("Test data is copied in the test Server for Username = " + userName);
								Thread.sleep(15000);

								isFreeGameAssigned = cfnlib.assignFreeGames(userName, offerExpirationUtcDate, mid, cid,languageCnt, defaultNoOfFreeGames);
								System.out.println("Free Games assignement status is : " + isFreeGameAssigned);log.debug("Free Games assignement status is : " + isFreeGameAssigned);
								Thread.sleep(10000);
								
								for(int orientation=0;orientation<2;orientation++)
								{
								     if(orientation==0 && !webdriver.getOrientation().equals(ScreenOrientation.PORTRAIT))
								{
									boolean portrait= cfnlib.funcPortrait();
								if(portrait)
								{
								     log.debug("Able to rotate portrait");
								     System.out.println("Able to rotate portrait");
								}
								}
									  else if(orientation==1 && !webdriver.getOrientation().equals(ScreenOrientation.LANDSCAPE))
								{
								       boolean landscape=cfnlib.funcLandscape();
								if(landscape)
								{
										log.debug("Able to rotate landscape");
										System.out.println("Able to rotate landscape");
								}
							}
								
							if (isFreeGameAssigned) 
								{
									String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)","username=" + userName + "$1");
									log.info("Url = " + LaunchURl);System.out.println("Url = " +LaunchURl);
									isGameLoaded = cfnlib.loadGame(LaunchURl);
	
									
									if (isGameLoaded) 
									{
										cfnlib.isFreeGamesVisible(LaunchURl);
										
										
										if ((Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad")))|| (Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueOnBaseScene"))))
										{
											cfnlib.newFeature();
										}

										cfnlib.waitForSpinButton();

										if (framework.equalsIgnoreCase("CS")) 
										{
											cfnlib.setNameSpace();
										}
										Thread.sleep(3000);

										//method is to verify the freegames Entry Screen and get text 
										boolean isFGAssign = cfnlib.freeGamesEntryScreen(lvcReport,currencyName);
										if (isFGAssign)
										{
											System.out.println("Free Games Entry Screen : PASS");log.debug("Free Games Entry Screen : PASS");
											//lvcReport.detailsAppendFolder("Free Games"," Entry Screen Validations","Entry Screen Validations", "PASS", currencyName);
										}
										else
										{
											System.out.println("Free Games Entry Screen : FAIL");log.debug("Free Games Entry Screen : FAIL");
											lvcReport.detailsAppendFolder("Free Games"," Entry Screen Validations"," Entry Screen Validations", "FAIL", currencyName);
										}
										Thread.sleep(1000);

										// Click on info and verify currency
										boolean isfreeGameEntryInfoVisible = cfnlib.verifyRegularExpression(lvcReport,regExpr,cfnlib.freeGameEntryInfo(lvcReport,currencyName,"FreeGamesInformation","FreeGamesInfoText"));
										if (isfreeGameEntryInfoVisible)
										{
											System.out.println("Free Games Entry Screen Info Icon Text Validation : PASS");log.debug("Free Games Entry Screen Info Icon Text Validation : PASS");
											//lvcReport.detailsAppendFolder("Free Games"," Entry Screen Info Text Validation","Free Games Entry Screen Info", "PASS", currencyName);
										}
										else
										{
											System.out.println("Free Games Entry Screen Info Icon Text Validation : FAIL");log.debug("Free Games Entry Screen Info Icon Text Validation : FAIL");
											lvcReport.detailsAppendFolder("Free Games"," Entry Screen Info Text Validation","Free Games Entry Screen Info", "FAIL", currencyName);
										}
										
										cfnlib.funcFullScreen();
										Thread.sleep(5000);
											
										//click on play now button on Free Spins Intro Screen 
										boolean playNowButtonAvailability = cfnlib.NativeClickByXpath_CS("playNow");
										if (playNowButtonAvailability)
										{
											System.out.println("Free Games Entry Screen Play Now Button Clicked : PASS");log.debug("Free Games Entry Screen Play Now Button Clicked : PASS");
											lvcReport.detailsAppendFolder("Free Games","Play Button Clicked ","Play Button Clicked ", "PASS", currencyName);
										}
										else
										{
											System.out.println("Free Games Entry Screen Play Now Button Clicked : FAIL");log.debug("Free Games Entry Screen Play Now Button Clicked : FAIL");
											lvcReport.detailsAppendFolder("Free Games","Play Button Clicked ","Play Button Clicked ", "FAIL", currencyName);
										}
										
										// Getts the Credit Amt && verifies Currency Format	and check the currency format	
									    
									    boolean credits = cfnlib.verifyRegularExpression(lvcReport,regExpr,cfnlib.func_GetTextID("Creditvalue"));
									    if(credits)
									    {
									    	System.out.println("Free Game Credit Value : PASS");log.debug("Free Game Credit Value : PASS");
									    	lvcReport.detailsAppendFolder("Free Games","Credit Amount","Credit Amount", "PASS", currencyName);
										}
										else
										{
											System.out.println("Free Game Credit Value : FAIL");log.debug("Free Game Credit Value : FAIL");
											lvcReport.detailsAppendFolder("Free Games","Credit Amount","Credit Amount", "PASS", currencyName);
										}
										

										// To verify Big win  
										cfnlib.spinclick();Thread.sleep(2000); 
										
										 //method is used to Spins to get Big win and check the currency 
										boolean bigWinFormatVerification =	cfnlib.verifyRegularExpression(lvcReport,regExpr,cfnlib.verifyBigWin(lvcReport,currencyName));
										if(bigWinFormatVerification)
										{
											System.out.println("Free Game BigWin Value : PASS");log.debug("Free Game BigWin Value : PASS");
											//lvcReport.detailsAppendFolder("Base Game", "Big Win Amt", ""+bigWinFormatVerification, "PASS",currencyName);
										}
										else
										{
											System.out.println("Free Game BigWin Value : FAIL");log.debug("Free Game BigWin Value : FAIL");
											lvcReport.detailsAppendFolder("Free Game", " Big Win Amt", ""+bigWinFormatVerification, "FAIL",currencyName);
										}Thread.sleep(2000); 
										
										// To verify Normal win  
										cfnlib.spinclick();Thread.sleep(2000); 
								
										//method is used  to get the Win amt and check the currency format	
										boolean winFormatVerification =	cfnlib.verifyRegularExpression(lvcReport,regExpr,cfnlib.getCurrentWinAmt(lvcReport,currencyName));
										if(winFormatVerification)
										{
											System.out.println("Free Game Win Value : PASS");log.debug("Free Game Win Value : PASS");
											lvcReport.detailsAppendFolder("Free Game", "Win Amt", ""+winFormatVerification, "PASS",currencyName);
										}
										else
										{
											System.out.println("Free Game Win Value : FAIL");log.debug("Free Game Win Value : FAIL");
											lvcReport.detailsAppendFolder("Free Game", "Win Amt", ""+winFormatVerification, "FAIL",currencyName);
										}Thread.sleep(1000);
									
										System.out.println("Refresh the Game");log.debug("Refresh the Game");
									
										// free games on refresh 
										boolean freeGamesOnRefresh =cfnlib.freeGameOnRefresh(lvcReport,currencyName);
										if(freeGamesOnRefresh)
										{
											System.out.println("Free Game On Refresh : PASS");log.debug("Free Game On Refresh : PASS");
											lvcReport.detailsAppendFolder("Free Game", "On Refresh", ""+freeGamesOnRefresh, "PASS",currencyName);
										}
										else
										{
											System.out.println("Free Game On Refresh : FAIL");log.debug("Free Game On Refresh : FAIL");
											lvcReport.detailsAppendFolder("Free Game", "On Refresh", ""+freeGamesOnRefresh, "FAIL",currencyName);
										}Thread.sleep(2000);
										
										// To verify Big win  on Refresh
										cfnlib.spinclick();Thread.sleep(2000); 
									
										//Free Game on Refersh 
										boolean refreshOnWin = cfnlib.verifyRegularExpression(lvcReport,regExpr,cfnlib.verifyBigWinOnRefresh(lvcReport,currencyName));
										if(refreshOnWin)
										{
											System.out.println("Free Game BigWin Value on Refresh : PASS");log.debug("Free Game BigWin Value on Refresh : PASS");
											lvcReport.detailsAppendFolder("Free Game", "Big Win Amt on Refresh", ""+refreshOnWin, "PASS",""+currencyName);
										}
										else
										{
											System.out.println("Free Game BigWin Value on Refresh : PASS");log.debug("Free Game BigWin Value on Refresh : PASS");
											lvcReport.detailsAppendFolder("Base Game", " Big Win Amt", ""+bigWinFormatVerification, "FAIL",""+currencyName);
										}Thread.sleep(3000); 
										
										// method is for Back to BaseGame
										boolean backtobasegame = cfnlib.verifyRegularExpression(lvcReport,regExpr,cfnlib.freeGameBackToBaseGame(lvcReport,currencyName));
										if(backtobasegame)
										{
											System.out.println("Free Game Back to Base Game : PASS");log.debug("Free Game Back to Base Game : PASS");
											lvcReport.detailsAppendFolder("Free Game", "Back to Base Game Screen ", "Back to Base Game Screen ", "PASS",currencyName);
											
										}
										else
										{
											System.out.println("Free Game Back to Base Game : FAIL");log.debug("Free Game Back to Base Game : FAIL");
											lvcReport.detailsAppendFolder("Free Game", "Back to Base Game Screen ", "Back to Base Game Screen ", "FAIL",currencyName);
										}Thread.sleep(2000);
										
										
										Thread.sleep(10000);
										System.out.println("            ");
									} // game load
									else 
									{
										log.debug("Unable to load the game");
										lvcReport.detailsAppend("Unable to load the game ", " ", "", "FAIL");
									}

								}//free game assignment
								else
								{
									log.error("Skipping the execution as free games assignment failed");
									lvcReport.detailsAppend(
											"kipping the execution as free games assignment failed ", " ", "", "FAIL");
								}
								}//for loop for orientaion
							}//copy files to Server
							
							else
							{
								log.debug("Unable to Copy testdata");
								lvcReport.detailsAppend("unable to copy test dat to server ", " ", "", "FAIL");

							}
							count++;
						} // while loop

						System.out.println("Done for free games");

					} // closing try
					catch (Exception e)
					{
						log.error("Exception occur while processing currency", e);
						lvcReport.detailsAppend("Exception occur while processing currency ", " ", "", "");
						cfnlib.evalException(e);

					}

				} // closing for
			}
		}
		// -------------------Handling the exception---------------------//
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
			cfnlib.evalException(e);
		}
		finally 
		{
			lvcReport.endReport();
			if (!copiedFiles.isEmpty()) 
			{
				if (TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
					util.deleteBluemesaTestDataFiles(mid, cid, copiedFiles);
				else
					apiobj.deleteAxiomTestDataFiles(copiedFiles);
			}
			webdriver.close();
			webdriver.quit();
			// proxy.abort();
			Thread.sleep(1000);
			
		}
	}
}