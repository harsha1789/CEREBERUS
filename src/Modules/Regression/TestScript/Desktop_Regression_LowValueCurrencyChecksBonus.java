package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.TestPropReader;
import com.zensar.automation.model.Currency;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import ch.qos.logback.core.net.SyslogOutputStream;
import net.lightbody.bmp.BrowserMobProxyServer;
/**
 * This Low value currency script verifies currency format and symbol with minimum and maximum bet in
 
   Bust The Bank
   - verifies piggy bank bonus and safe bonus in btb
   
   BBBS5Reel
   - verifies multiplier bonus
 	
 * @author pb61055
 *
 */
public class Desktop_Regression_LowValueCurrencyChecksBonus{
	
	public static Logger log = Logger.getLogger(Desktop_Regression_LowValueCurrencyChecksBonus.class.getName());
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
		String status=null;
		int mintDetailCount=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;

		ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();
		Desktop_HTML_Report currencyReport = new Desktop_HTML_Report(webdriver, browserName, filePath, startTime, mstrTCName,
				mstrTCDesc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings, mintStepNo, status,
				gameName);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, currencyReport, gameName);

		
		CommonUtil util = new CommonUtil();
		RestAPILibrary apiobj=new RestAPILibrary();
		
		List<String> copiedFiles=new ArrayList<>();
		int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int  cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDDesktop"));
		
		try {
			// -------Getting webdriver of proper test site-------//
			if (webdriver != null) 
			{

				//./Desktop_Regression_CurrencySymbol.testdata
				String strFileName=TestPropReader.getInstance().getProperty("FreeSpinsTestDataPath");
				File testDataFile=new File(strFileName);
				
				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				int languageCnt = excelpoolmanager.rowCount(testDataExcelPath, Constant.LANG_XL_SHEET_NAME);

				boolean isProgressive=TestPropReader.getInstance().getProperty("IsProgressiveGame").equalsIgnoreCase("Yes");
				boolean isGameLoaded = false;
               
				
				List<Map<String, String>> currencyList= util.readCurrList();// mapping

				String url = cfnlib.XpathMap.get("ApplicationURL");
				
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
									System.out.println(LaunchURl);
							
										
							isGameLoaded=cfnlib.loadGameForLVC(LaunchURl);	

										
							if (isGameLoaded) 
							{
								if((Constant.YES.equalsIgnoreCase(cfnlib.XpathMap.get("continueBtnOnGameLoad"))))
								{
									cfnlib.newFeature();
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
									
								
								
								
								
								if(gameName.contains("BarBarBlackSheep5Reel")||(Constant.YES.equalsIgnoreCase(cfnlib.XpathMap.get("isMultiplierBonusAvailable"))))
								{
									
									//To verify big win in base game with bonus multiplier
									cfnlib.spinclick();
										
									cfnlib.isBonusMultiplierVisible();
									Thread.sleep(2000);
									currencyReport.detailsAppendFolderOnlyScreenShot(currencyName);
								
									boolean bigwincurrency = cfnlib. verifyBigWinCurrencyFormatForLVC(regExpression,currencyReport,currencyName);
									if (bigwincurrency) {
										currencyReport.detailsAppendFolderOnlyScreenShot(currencyName);
										currencyReport.detailsAppendFolder("Verify currency when bigwin occurs in base game ",
												"biWin  should display with correct currency format and and currency symbol ",
												"bigwin  displaying with correct currency format and and currency symbol ","Pass",currencyName);
									} else {
										currencyReport.detailsAppendFolder("Verify currency when bigwin occurs in base game ",
												"bigwin should display with currency ",
												"bigwin is not  displaying with currency ", "Fail",currencyName);
									}
									cfnlib.waitForSpinButtonstop();	
										
									//To verify currency format in free spins
									cfnlib.spinclick();
									Thread.sleep(8000);
										
									String freeSpinEntryScreen = cfnlib.XpathMap.get("FreeSpinEntryScreen");
									//wait till if the game having free spin entry screen
									String str = cfnlib.entryScreen_Wait(freeSpinEntryScreen);
									if (str.equalsIgnoreCase("freeSpin")) 
									{
										//Click on free spins into continue button
										if("yes".equals(cfnlib.XpathMap.get("isFreeSpinIntroContinueBtnAvailable")))
										{
												cfnlib.clickToContinue();
										}	
									} 
										
									cfnlib.FSSceneLoading();
									
									cfnlib.isBonusMultiplierVisible();
									Thread.sleep(2000);
									currencyReport.detailsAppendFolderOnlyScreenShot(currencyName);
									
									// wait until win occurs and capture screenshot.
									boolean winValue = cfnlib.verifyBigWinCurrencyFormatForLVC(regExpression,currencyReport,currencyName);
									if (winValue) 
									{
										currencyReport.detailsAppendFolderOnlyScreenShot(currencyName);
										currencyReport.detailsAppendFolder("Verify free spin scene when win occurs",
													"Win in Free spin scene should display with correct currency format and and currency symbol",
													"Win in Free Spin scene is displaying with correct currency format and and currency symbol", "Pass",currencyName);
									}
									else
									{
										currencyReport.detailsAppendFolder("Verify free spin scene when win occurs",
												"Win in Free spin scene should display with correct currency format and and currency symbol",
												"Free Spin scene is not displaying with correct currency format and and currency symbol", "Fail",currencyName);
									}
									cfnlib.waitSummaryScreen();
									Thread.sleep(1000);
									cfnlib. freeSpinSummaryWinCurrFormatForLVC(regExpression,currencyReport,currencyName);	
								}
								else if(gameName.contains("bustTheBank"))
								{
									//Clicking on spin button
									cfnlib.spinclick();
									cfnlib.waitForSpinButtonstop();
									Thread.sleep(10000);
									
									//Piggy bank lands on reels
									currencyReport.detailsAppendNoScreenshot("Verify Piggy bank bonus "," "," ", "Pass");
									
									
									
									//Verifying the currency format for Piggy bank bonus
									boolean isPiggyBonusInCurrencyFormat=cfnlib.verifyWinAmtCurrencyFormatForLVC(regExpression);
									if(isPiggyBonusInCurrencyFormat)
									{
										currencyReport.detailsAppendFolder("Verify currency when Piggy bank bonus win occurs",
												"Piggy bank bonus win should display in correct currency format" ,
												"Piggy bank bonus win is displaying in correct currency format","Pass", currencyName);
										System.out.println("Piggy Bank Bonus currency format is correct");
									}
									else
									{
										currencyReport.detailsAppendFolder("Verify currency when Piggy bank bonus win occurs",
												"Piggy bank bonus win should display in correct currency format" ,
												"Piggy bank bonus win is displaying in incorrect currency format","Fail", currencyName);
										System.out.println("Piggy Bank Bonus currency format is incorrect");
									}
									
									cfnlib.waitForSpinButtonstop();
									Thread.sleep(2000);
									
									
									System.out.println("================== SAFE BONUS ================ ");
									log.debug("================== SAFE BONUS ================ ");
									
									currencyReport.detailsAppendNoScreenshot("Verify Safe bonus ","","","Pass");
									for(int j=1;j<7;j++)
									{
										// waiting for Bet Icon availability
										if(cfnlib.elementWait("return "+cfnlib.XpathMap.get("BetIconCurrState"), "active"))
										{
											
											cfnlib.spinclick();
											Thread.sleep(5000);
											if(cfnlib.XpathMap.get("SafeBonusWinAnimation")!=null)
											{
												Thread.sleep(3000);
												currencyReport.detailsAppendFolderOnlyScreenShot(currencyName);
												cfnlib.waitForSpinButtonstop();
												boolean isSafeBonusInCurrencyFormat=cfnlib.verifyWinAmtCurrencyFormatForLVC(regExpression);
												if (isSafeBonusInCurrencyFormat) 
												{
													currencyReport.detailsAppendFolder("Verify currency when Safe bonus win occurs",
														"Safe bonus win should display in correct currency format" ,
														"Safe bonus win is displaying in correct currency format ","Pass",currencyName);
													System.out.println("Safe Bonus currency format is correct");
												}
												else
												{
													currencyReport.detailsAppendFolder("Verify currency when Safe bonus win occurs",
															"Safe bonus win should display in correct currency format" ,
															"Safe bonus win is displaying in incorrect currency format ","Fail",currencyName);
														System.out.println("Safe Bonus currency format is incorrect");
												}		
											}
										}
								}
								
								}// else if
								

								
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
							
						}// copy files to test server
						else
						{
							log.debug("Unable to Copy testdata");
							currencyReport.detailsAppendFolder("unable to copy test dat to server ", " ", "", "Fail", currencyName);

						}
						
					}//for loop
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
			Thread.sleep(1000);
		}
	}
}