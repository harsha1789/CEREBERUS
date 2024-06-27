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
 * This Low value currency script verifies currency format with minimum and maximum bet in
 
   1. Free games info
   2. Free games big win, super win and mega win
   3. Free games summary 

  Input from  free games test data file.
  
  Test Data format-
  Normal reels
  1 spin - Big win, super win and mega win
  
 * @author pb61055
 *
 */
public class Desktop_Regression_LowValueCurrencyChecks_FreeGames{
	
	public static Logger log = Logger.getLogger(Desktop_Regression_LowValueCurrencyChecks_FreeGames.class.getName());
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

				//./Desktop_Regression_Language_Verification_FreeGames.testdata
				String strFileName=TestPropReader.getInstance().getProperty("FreeGamesTestDataPath");
				File testDataFile=new File(strFileName);
			
				int noOfOffers=1;

				String offerExpirationUtcDate=util.getCurrentTimeWithDateAndTime();
				String strdefaultNoOfFreeGames=cfnlib.XpathMap.get("DefaultNoOfFreeGames");
				int defaultNoOfFreeGames=(int) Double.parseDouble(strdefaultNoOfFreeGames);
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
										
						boolean isFreeGameAssigned=false;
						// verifying free games	
						int maxFGRetCount=3;
						int count=0;
						while(count<maxFGRetCount) 
						{
							//Generating Random user
							userName=util.randomStringgenerator();
							System.out.println("mid: "+mid);	
							System.out.println("cid: "+cid);
							
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
							
							
										isFreeGameAssigned=cfnlib.assignFreeGames(userName,offerExpirationUtcDate,mid,cid,noOfOffers,defaultNoOfFreeGames);
										System.out.println("free games assigned: "+isFreeGameAssigned);
							
							
							
								if(isFreeGameAssigned) 
								{
									String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
									log.info("url = " +LaunchURl);
									System.out.println(LaunchURl);

									log.debug(" Currency Name :  "+currencyName);		
									isGameLoaded=cfnlib.loadGameForLVC(LaunchURl);
								
										
									if (isGameLoaded) 
									{
										cfnlib.isFreeGamesVisible(LaunchURl);
	
										if((Constant.YES.equalsIgnoreCase(cfnlib.XpathMap.get("continueBtnOnGameLoad")))||(Constant.YES.equalsIgnoreCase(cfnlib.XpathMap.get("continueOnBaseScene"))))
										{
											cfnlib.newFeature();
										}
										
										cfnlib.waitForSpinButton();

										if(framework.equalsIgnoreCase("Force"))
										{
											cfnlib.setNameSpace();
										}
										
										
										boolean isFGAssign = cfnlib.freeGamesEntryScreen();
										if(isFGAssign)
											currencyReport.detailsAppendFolder("Check that free games entry screen is displaying", "Free Games entry screen should display","Free Game entry screen is displaying", "Pass", currencyName);
										else
											currencyReport.detailsAppendFolder("Check that free games entry screen is displaying", "Free Games entry screen should display","Free Game entry screen is not displaying", "Fail", currencyName);
										Thread.sleep(500);
									
										
										
										// Click on info and verify currency
										boolean b = cfnlib.freeGameEntryInfo();
										if(b)
											cfnlib.freeGameInfoCurrencyFormat(regExpression,currencyReport,currencyName);
										else
											currencyReport.detailsAppendFolder("Check that free games entry screen is displaying with free games details", "Free Games entry screen should display with free games details","Free Game entry screen is not displaying with free games details", "Fail", currencyName);
										Thread.sleep(500);
										
										
										
										cfnlib.clickPlayNow();
										Thread.sleep(2000);
									
										
										//To verify big win in base game
										cfnlib.spinclick();
										boolean bigwincurrency = cfnlib. verifyBigWinCurrencyFormatForLVC(regExpression,currencyReport,currencyName);
										if (bigwincurrency) {
											currencyReport.detailsAppendFolder("Verify currency when bigwin occurs in base game ",
													"biWin  should display with correct currency format and and currency symbol ",
													"bigwin  displaying with correct currency format and and currency symbol ","Pass",currencyName);
											Thread.sleep(2000);
											currencyReport.detailsAppendFolderOnlyScreenShot(currencyName);
											Thread.sleep(2000);
											currencyReport.detailsAppendFolderOnlyScreenShot(currencyName);

										} else {
											currencyReport.detailsAppendFolder("Verify currency when bigwin occurs in base game ",
												"bigwin should display with currency ",
												"bigwin is not  displaying with currency ", "Fail",currencyName);
										}									
										cfnlib.waitForSpinButtonstop();
										
										//To click on continue button in freespins summary screen
										if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.XpathMap.get("TypeOfGame"))) 
										{
											cfnlib.closeOverlay();
											Thread.sleep(2000);
										}
										
								
										String amount =cfnlib.getWinAmtFromFreegames();
										if (amount!=null  &&  !amount.equals(""))
										{
											cfnlib. freeGameSummaryWinCurrFormatForLVC(regExpression,currencyReport,currencyName);	
											break;
										}
										else
										{
											System.out.println("There is no win during free games");
											log.debug("There is no win during free games");
											currencyReport.detailsAppendFolder("There is no win or general error during free games, assigning freegame again ", "","","Fail", currencyName);
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
							
							}else 
							{
								log.error("Skipping the execution as free games assignment failed");
								currencyReport.detailsAppendFolder("skipping the execution as free games assignment failed ", " ", "", "Fail", currencyName);
							}
						}else
						{
							log.debug("Unable to Copy testdata");
							currencyReport.detailsAppendFolder("unable to copy test dat to server ", " ", "", "Fail", currencyName);
						}
						count++;
					}//while loop
					
						
					System.out.println("Done for free games");
							
					}//try 
					catch ( Exception e) {
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