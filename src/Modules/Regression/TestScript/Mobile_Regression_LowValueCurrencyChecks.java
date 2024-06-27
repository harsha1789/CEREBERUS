package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.TestPropReader;
import com.zensar.automation.model.Currency;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import ch.qos.logback.core.net.SyslogOutputStream;
import io.appium.java_client.AppiumDriver;
import net.lightbody.bmp.BrowserMobProxyServer;
/**
 * This Low value currency script verifies currency format with minimum and maximum bet in
 
 	Orientation - Portrait and Landscape
 
   1. Credits(balance)
   2. Bet
   3. Quick bets
   4. Auto play (Screenshot)
   5. Win in base game
   6. big win, super win and mega win  in base game
   7. Free Spins big win, super win and mega win 
   8. Free Spins Summary
   9. All features total win
  10. Mini paytable
  11. Paytable

  Input from currencies test data file.
  
  Test Data format-
  Normal reels
  		1st spin - Normal/minimum win
  		2nd spin - Big win, super win and mega win  in base game
  		3rd spin - Triggers free spins
  		4th spin - reels with all symbols for mini paytble (if applicable/ not for re-spins game)
  		
  Free spin reels
  		1st spin - Free Spins big win, super win and mega win
           	 	 - rest all no wins
   
 * @author pb61055
 *
 */
public class Mobile_Regression_LowValueCurrencyChecks{
	
	public static Logger log = Logger.getLogger(Mobile_Regression_LowValueCurrencyChecks.class.getName());
	public ScriptParameters scriptParameters;

	public void script() throws Exception {

		String mstrTCName=scriptParameters.getMstrTCName();
		String mstrTCDesc=scriptParameters.getMstrTCDesc();
		String mstrModuleName=scriptParameters.getMstrModuleName();
		BrowserMobProxyServer proxy=scriptParameters.getProxy();	
		String startTime=scriptParameters.getStartTime();
		String filePath=scriptParameters.getFilePath();
		String userName=scriptParameters.getUserName();
		String browserName=scriptParameters.getBrowserName();
		String framework=scriptParameters.getFramework();
		String gameName=scriptParameters.getGameName();
		AppiumDriver<WebElement> webdriver=scriptParameters.getAppiumWebdriver();
		String deviceName=scriptParameters.getDeviceName();
		String status=null;
		int mintDetailCount=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;

		ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();
		Mobile_HTML_Report currencyReport=new Mobile_HTML_Report(webdriver,deviceName,filePath,startTime,mstrTCName,mstrTCDesc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,status,gameName);
		
		MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, currencyReport, gameName);
		
		CommonUtil util = new CommonUtil();
		RestAPILibrary apiobj=new RestAPILibrary();
		
		List<String> copiedFiles=new ArrayList<>();
		cfnlib.setOsPlatform(scriptParameters.getOsPlatform());
		
		int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int  cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDMobile"));
		
		try {
			// -------Getting webdriver of proper test site-------//
			if (webdriver != null) 
			{
			//./Desktop_Regression_CurrencySymbol.testdata
			String strFileName=TestPropReader.getInstance().getProperty("MobileCurrencyTestDataPath");
			File testDataFile=new File(strFileName);
			
			String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
			int languageCnt = excelpoolmanager.rowCount(testDataExcelPath, Constant.LANG_XL_SHEET_NAME);
			//int rowCount2 = excelpoolmanager.rowCount(testDataExcelPath, Constant.CURRENCY_XL_SHEET_NAME); // get row values
			//System.out.println(rowCount2);

			
			boolean isGameLoaded = false;
           
			
			List<Map<String, String>> currencyList= util.readCurrList();// mapping

			String url = cfnlib.xpathMap.get("ApplicationURL");
			
			for (Map<String, String> currencyMap:currencyList) 
			{
		
				try
				{
					
					String isoCode = currencyMap.get(Constant.ISOCODE).trim();
					String currencyID = currencyMap.get(Constant.ID).trim();
					String currencyName = currencyMap.get(Constant.ISONAME).trim();
					String languageCurrency = currencyMap.get(Constant.LANGUAGECURRENCY).trim();
					String currencyFormat=currencyMap.get(Constant.DISPALYFORMAT).trim();
					String regExpression=currencyMap.get(Constant.REGEXPRESSION).trim();


					log.debug(this+" I am processing cuurency:  "+currencyName);
			
					currencyReport.detailsAppend("Verify currency name "+currencyName, " Verify currency name "+currencyName,"","");
				
					
					
					
					for(int orientation=0;orientation<2;orientation++) 
					{
						if(orientation==0 && !webdriver.getOrientation().equals(ScreenOrientation.PORTRAIT))
						{
							boolean portrait= cfnlib.funcPortrait();
														
							if(portrait)
							{
								log.debug("Able to rotate portrait");
								System.out.println("Able to rotate portrait");
								currencyReport.detailsAppendFolder("Execution in Portrait mode ", " ", "", "Pass", currencyName);
							}
							else
							{
								log.debug("Unable to rotate portrait");
								currencyReport.detailsAppendFolder("Unable to rotate portrait ", " ", "", "Fail", currencyName);
							}
						}
						else if(orientation==1 && !webdriver.getOrientation().equals(ScreenOrientation.LANDSCAPE))
						{
							boolean landscape=cfnlib.funcLandscape();
							
							if(landscape)
							{
								log.debug("Able to rotate landscape");
								System.out.println("Able to rotate landscape");
								currencyReport.detailsAppendFolder("Execution in Landscape mode ", " ", "", "Pass", currencyName);
							}
							else
							{
								log.debug("Unable to rotate landscape");
								currencyReport.detailsAppendFolder("Unable to rotate landscape ", " ", "", "Fail", currencyName);
							}
						}		
					
					
					
						for(int i=1;i<3;i++)
						{	
							//Generating Random user
							userName=util.randomStringgenerator();
					
						
							//Copy Test data to server
							if(	util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles,currencyID,isoCode))
							{
								log.debug("Test dat is copy in test Server for Username="+userName);
			
								if(	util.migrateUser(userName))
								{
									log.debug("Able to migrate user");
									System.out.println("Able to migrate user");
									
									log.debug("Updating the balance");
									String balance="700000000000";
									Thread.sleep(60000);
								
									
									if(util.updateUserBalance(userName,balance))
									{
										log.debug("Able to update user balance");
										System.out.println("Able to update user balance");
								
										String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
										log.info("url = " +LaunchURl);
								
										isGameLoaded=cfnlib.loadGameForLVC(LaunchURl);	

					
								if (isGameLoaded) 
								{
									currencyReport.detailsAppendFolder("Verify Base Scene for "+currencyName+" currency","" + gameName + " Should display in "+currencyName+" currency","Game is displaying in "+currencyName+" currency", "Pass",currencyName);
						
						
									if((Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad")))||(Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueOnBaseScene"))))
									{
										if("Yes".equalsIgnoreCase(cfnlib.xpathMap.get("CntBtnNoXpath"))) 
										{	 cfnlib.funcFullScreen();
										     Thread.sleep(1000);
										     cfnlib.closeOverlay();
									     }
										
										else	{	
										cfnlib.newFeature();
										Thread.sleep(1000);
										cfnlib.funcFullScreen();
										}
									}	
									else
									{
										cfnlib.funcFullScreen();
										Thread.sleep(1000);
										//cfnlib.newFeature();
									}
				
									if(Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("fullScreenOverOnTopofContinue")))
									{
										cfnlib.fullScreenOverOnTopofContinue();
									}
				
									cfnlib.waitForSpinButton();
									log.debug(" Currency Name :  "+currencyName);


									if(framework.equalsIgnoreCase("Force"))
									{
										cfnlib.setNameSpace();
									}
																		
									if(i==1)
									{
									
										// set bet to minimum bet
										cfnlib.setMinBet();
										System.out.println("With minimum bet");
										currencyReport.detailsAppendNoScreenshot("Verify the currency format with minimu bet credit ",
												"Currency format win minimum bet " ,
												"Currency format with minimum bet ", "Pass");
									}
									else
									{
										// set bet to max bet
										cfnlib.setMaxBet();
										System.out.println("With maximum bet");
										currencyReport.detailsAppendNoScreenshot("Verify the currency format with maximum bet credit ",
												"Currency format win maximum bet " ,
												"Currency format with maximum bet ", "Pass");
									}
	
					
									
						// To get currency symbol in credits
						String currencySymbol = cfnlib.getCurrencySymbol();
						log.info(currencyName + "currency symbol is " + currencySymbol);

					   // verify currency symbol and currency format in credit
					   boolean result=cfnlib.verifyCurrencyFormatForCredits(regExpression);

					   if (result) 
					   {
							currencyReport.detailsAppendFolder("Verify that currency format and currency symbol in credit ",
									"Credit should display in correct currency format and currency symbol " ,
									"Credit is displaying in correct currency format and currency symbol ", "Pass",
									currencyName);
						} 
					   else 
					   {
							currencyReport.detailsAppendFolder("Verify that currency format and currency symbol in credit ",
									"Credit should display in correct currency format and currency symbol " ,
									"Credit is not displaying in correct currency format and currency symbol ", "Fail",
									currencyName);
						}
									
									
						//To verify currency symbol and currency format in bet
						boolean b = cfnlib.betCurrencySymbolForLVC(regExpression);
						if (b) {
								currencyReport.detailsAppendFolder("Verify that currency format and currency symbol on the Bet Console ",
									"Bet console should display the correct currency format and currency symbol ",
									"Bet console display the correct currency format and currency symbol ", "Pass",
									currencyName);
						} else {
								currencyReport.detailsAppendFolder("Verify that currency format on the Bet Console and currency symbol ",
									"Bet console should display the correct currency format and currency symbol ",
									"Bet console is not display the correct currency format and currency symbol ", "Fail",
								currencyName);
						}
								
						// to verify currency format in bet console
						cfnlib.openTotalBet();
						// to verify currency symbol in bet settings page
						boolean b2 = cfnlib.betSettingCurrencySymbol(currencyFormat,currencyReport,currencyName);
						if (b2) {
								currencyReport.detailsAppendFolder("Verify that currency on the Bet Settings Screen",
										"Total Bet  should  display " + currencySymbol + " currency symbol ",
										"Total Bet displaying " + currencySymbol + " currency symbol ",
										"Pass", currencyName);
						} else {
								currencyReport.detailsAppendFolder("Verify that currency on the Bet Settings Screen",
										"Total Bet should display " + currencySymbol + " currency symbol",
										"Total Bet not display " + currencySymbol+ " currency symbol", "Fail",
										currencyName);
						}
						cfnlib.closeOverlayForGamePannelsLVC();
						Thread.sleep(1000);
									
						// Open and Capture Screen shot of Autoplay Screen
						boolean openAutoplay = cfnlib.open_Autoplay();
						if (openAutoplay)
						{
							currencyReport.detailsAppendFolder("Verify Autoplay on the Screen", "Autoplay Screen should be display", "Autoplay Screen should be displayed", "Pass", currencyName);
									
						}
									
						else
						{			
							currencyReport.detailsAppendFolder("Verify Autoplay on the Screen", "Autoplay Screen should be display", "Autoplay Screen doesn't display", "Fail", currencyName);   
						}
									
						// Close Auto play
						cfnlib.closeOverlayForGamePannelsLVC();
						Thread.sleep(1000);
						
						
						//To verify normal win in base game
						cfnlib.spinclick();
						cfnlib.waitForSpinButtonstop();
						Thread.sleep(8000);
									
						boolean winCurrency=cfnlib.verifyWinAmtCurrencyFormatForLVC(regExpression);
						if (winCurrency) 
						{
							currencyReport.detailsAppendFolder("Verify currency when win occurs ",
									"win should display with correct currency format and and currency symbol",
									"win displaying with correct currency format and and currency symbol ","Pass",currencyName);

						} else {
							currencyReport.detailsAppendFolder("Verify currency when win occurs ",
									"win should display with correct currency format and and currency symbol ",
									"win is not  displaying with correct currency format and and currency symbol ", "Fail",currencyName);
						}
									
								
						//To verify big win in base game
						cfnlib.spinclick();
						boolean bigwincurrency = cfnlib. verifyBigWinCurrencyFormatForLVC(regExpression,currencyReport,currencyName);
						if (bigwincurrency) {
							currencyReport.detailsAppendFolderOnlyScreeshot(currencyName);
							currencyReport.detailsAppendFolder("Verify currency when bigwin occurs in base game ",
									"biWin  should display with correct currency format and and currency symbol ",
									"bigwin  displaying with correct currency format and and currency symbol ","Pass",currencyName);
							Thread.sleep(2000);
							currencyReport.detailsAppendFolderOnlyScreeshot(currencyName);

						} else {
							currencyReport.detailsAppendFolder("Verify currency when bigwin occurs in base game ",
									"bigwin should display with currency ",
									"bigwin is not  displaying with currency ", "Fail",currencyName);
						}
						Thread.sleep(2000);
						cfnlib.waitForSpinButtonstop();	
						
						
						
						//Check currency in free spin
						if(!gameName.contains("Scratch") && TestPropReader.getInstance().getProperty("IsFreeSpinAvailable").equalsIgnoreCase("yes"))
						{
							
							//To verify currency format in free spins
							cfnlib.spinclick();
							Thread.sleep(8000);
							
							
							
							String freeSpinEntryScreen = cfnlib.xpathMap.get("FreeSpinEntryScreen");
							
							//wait till if the game having free spin entry screen
							String str = cfnlib.entryScreen_Wait_Upadated_FreeSpin(freeSpinEntryScreen);
							if (str.equalsIgnoreCase("freeSpin")) 
							{
								if("yes".equals(cfnlib.xpathMap.get("isFreeSpinSelectionAvailable")))
								{
									Thread.sleep(3000);
									
									boolean isBonusSelectionVisible=cfnlib.isFreeSpinTriggered();
									if(isBonusSelectionVisible)
									{
										cfnlib.clickBonusSelection(1);
									}
									else
									{
										log.debug("Bonus selection is not visible");
									}
								}
								else
								{
									if( TestPropReader.getInstance().getProperty("IsProgressiveGame").equalsIgnoreCase("Yes"))
									{
										cfnlib.FSSceneLoading();
										//cfnlib.clickToContinue();
										//cfnlib.FS_continue();
									}
									else
									{
										//Click on free spins into continue button
										if("yes".equals(cfnlib.xpathMap.get("isFreeSpinIntroContinueBtnAvailable")))
										{
											currencyReport.detailsAppendFolderOnlyScreeshot(currencyName);
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
							else 
							{
								log.debug("Free Spin Entry screen is not present in Game");
							}
							cfnlib.FSSceneLoading();

							// wait until win occurs and capture screenshot.
							boolean b3 = cfnlib.verifyBigWinCurrencyFormatForLVC(regExpression,currencyReport,currencyName);
							if (b3) {
									currencyReport.detailsAppendFolderOnlyScreeshot(currencyName);
									currencyReport.detailsAppendFolder("Verify free spin scene when win occurs",
												"Win in Free spin scene should display with correct currency format and and currency symbol",
												"Win in Free Spin scene is displaying with correct currency format and and currency symbol", "Pass",currencyName);
									Thread.sleep(2000);
									currencyReport.detailsAppendFolderOnlyScreeshot(currencyName);

							} else {
										if("No".equalsIgnoreCase(cfnlib.xpathMap.get("BigWinlayers")))
										{
											Thread.sleep(4000);
											currencyReport.detailsAppendFolder("There is No big win layers in the game",
													"NO big win layers in game",
													"NO big win layers in game", "pass",currencyName);

										}else{
											currencyReport.detailsAppendFolder("Verify free spin scene when win occurs",
													"Win in Free spin scene should display with correct currency format and and currency symbol",
													"Free Spin scene is not displaying with correct currency format and and currency symbol", "Fail",currencyName);
										}
									}
									
									cfnlib.waitSummaryScreen();
									Thread.sleep(1000);

									cfnlib. freeSpinSummaryWinCurrFormatForLVC(regExpression,currencyReport,currencyName);
									
							}
								
								Thread.sleep(3000);
								
								//To click on continue button in freespins summary screen
								if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame"))) 
								{
									cfnlib.closeOverlayForLVC();
								}
								
								cfnlib.waitForSpinButton();
								
								// To get currency symbol in credits after all features win
								String currencySymbolAfterWin = cfnlib.getCurrencySymbol();
								log.info(currencyName + "currency symbol is " + currencySymbolAfterWin);

								// verify currency symbol and currency format in credit after all features win
								boolean allWinCurrFormat=cfnlib.verifyCurrencyFormatForCredits(regExpression);

								if (allWinCurrFormat) {
									currencyReport.detailsAppendFolder("Verify that currency format and currency symbol in credit after all features win",
											"Credit should display in correct currency format and currency symbol after all features win " ,
											"Credit is displaying in correct currency format and currency symbol after all features win ", "Pass",
											currencyName);
								} else {
									currencyReport.detailsAppendFolder("Verify that currency format and currency symbol in credit after all features win ",
											"Credit should display in correct currency format and currency symbol after all features win " ,
											"Credit is not displaying in correct currency format and currency symbol after all features win ", "Fail",
											currencyName);
								}
								
								
						
								//verifying mini paytable
								if(!gameName.contains("Scratch") && "no".equalsIgnoreCase(cfnlib.xpathMap.get("IsRespinFeature"))&& cfnlib.isPaytableAvailable())
								{
									try 
									{
										if("yes".equalsIgnoreCase(cfnlib.xpathMap.get("symbolNameVariesInScenes")))
										{
											cfnlib.spinclick();
											cfnlib.waitForSpinButtonstop();
											Thread.sleep(300);
										}
										
										cfnlib.validateMiniPaytableForLVC(currencyReport,regExpression,currencyName);
										Thread.sleep(1000);
										cfnlib.closeOverlayForLVC();
												
									} catch (Exception e) {

										log.error(e.getStackTrace());
										log.error("Error in validating mini paytable "+e);
										log.error(e.getMessage());
									}

								}
								
													
								//Verify the pay table currency format
								if(!gameName.contains("Scratch") &&  cfnlib.checkAvilability(cfnlib.xpathMap.get("isMenuPaytableBtnVisible")))
								{
									cfnlib.payoutverificationforBetLVC(currencyReport,regExpression,currencyName);											
								}										
						
								}// game load
								else
								{
									log.debug("Unable to load the game");
									currencyReport.detailsAppendFolder("Unable to load the game ", " ", "", "Fail", currencyName);
	
								}
					}// user balance update
					else
					{
						log.debug("Unable to update balance");
						currencyReport.detailsAppendFolder("Unable to update balance ", " ", "", "Fail", currencyName);

					}
					}// migrating user
					else
					{
						log.debug("Unable to migrate user");
						currencyReport.detailsAppendFolder("Unable to migrate user ", " ", "", "Fail", currencyName);

					}				
								
				}// test data					
				else
				{
					log.debug("Unable to Copy testdata");
					currencyReport.detailsAppendFolder("unable to copy test dat to server ", " ", "", "Fail", currencyName);

				}
					}// for loop min and max
				}// Orientation
			}//try 
			catch ( Exception e) 
			{
				log.error("Exception occur while processing currency",e);
				currencyReport.detailsAppend("Exception occur while processing currency ", " ", "", "Fail");
				cfnlib.evalException(e);
			}
				
			}// for loop : mapping currencies 
	}// if: web driver
}//try
		
	// -------------------Handling the exception---------------------//
	catch (Exception e) {
		log.error(e.getMessage(), e);
		cfnlib.evalException(e);
	} finally {
		currencyReport.endReport();
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
		Thread.sleep(1000);
	}
}
}