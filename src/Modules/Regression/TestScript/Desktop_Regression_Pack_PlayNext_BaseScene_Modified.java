package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.ImageLibrary;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop_PlayNext;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;

/**Game Name
 * =============================
 * BreakAwayDeluxe
 * BreakAway Lucky Wilds
 * 
 * 1.Base Game
 * ===========
 * 1.1.Credits 
 * 1.2.Bet
 * 1.3.Autoplay
 * 1.4.Bet Panel (Min , Max & All Bet Values)
 * 1.5.PayTable , Pay-Outs Validation & Branding Validation  
 * 1.6.Big Win
 * 1.7.Amazing Win 
 * 
 * 2.Free Spins
 * =============
 * 2.1.Credits
 * 2.2.Total Win
 * 2.3.Big Win -1st Spin
 * 2.4.Normal Win - 2nd Spin *
 * 2.5.Summary Screen Total Win Validation
 * 
 *  * TestData 
 * ==============
 * 1.Base Game
 * 1.1 Big Win
 * 1.2 Normal Win
 * 1.3 Bonus(If Applicable)
 * 
 * 2.Free Spins
 * 2.1 Big Win - 1st Spin 
 * 2.2 Normal Win -2nd Spin
 * This Script is for play.next
 * 
 *@author pb61055
 */

public class Desktop_Regression_Pack_PlayNext_BaseScene_Modified {

	Logger log = Logger.getLogger(Desktop_Regression_Pack_PlayNext_BaseScene_Modified.class.getName()); 
	public ScriptParameters scriptParameters;
	public void script() throws Exception
	{

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
		String urlNew=null;
		int startindex=0;
		String strGameName =null;
		ImageLibrary imageLibrary;			
		
		Desktop_HTML_Report report = new Desktop_HTML_Report(webdriver,browserName,filePath,startTime,mstrTCName,mstrTCDesc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,status1,gameName);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,report, gameName);
		CommonUtil util = new CommonUtil();
		RestAPILibrary apiObj = new RestAPILibrary();					
		List<String> copiedFiles=new ArrayList<>();
		int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		System.out.println(mid);
		int cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDDesktop"));
		System.out.println(cid);
		try
		{
			// Step 1 
			if(webdriver!=null)
			{	
				if(gameName.contains("Desktop"))
				{   
					java.util.regex.Pattern  str=java.util.regex.Pattern.compile("Desktop");
					Matcher  substing=str.matcher(gameName);
					while(substing.find())
					{
						startindex=substing.start();													
					}
					strGameName=gameName.substring(0, startindex);
					log.debug("newgamename="+strGameName);
				}
				else
				{
					strGameName=gameName;
				}					
				imageLibrary = new ImageLibrary(webdriver, strGameName, "Desktop");
				String strFileName = TestPropReader.getInstance().getProperty("CurrencyTestDataPath");
				File testDataFile = new File(strFileName);
				List<Map<String, String>> currencyList = util.readCurrList();// mapping
				for (Map<String, String> currencyMap : currencyList) 
				{
				try {									
					// Step 2: To get the languages in MAP and load the language specific url
					String isoCode = currencyMap.get(Constant.ISOCODE).trim();
					String currencyID = currencyMap.get(Constant.ID).trim();
					String currencyName = currencyMap.get(Constant.ISONAME).trim();
					String languageCurrency = currencyMap.get(Constant.LANGUAGECURRENCY).trim();
					String regExpr = currencyMap.get(Constant.REGEXPRESSION).trim();
					String url = cfnlib.XpathMap.get("ApplicationURL");						
					log.debug(this + " I am processing currency:  " + currencyName);
											
					report.detailsAppend("*** CURRENCY AND LANGUAGE ***", "Currency: "+currencyName+", Language: "+currencyName,"", "");
					
					report.detailsAppend("Sanity Suite Test cases in "+languageCurrency+" for "+currencyName+" ", "Sanity Suite Test cases", "", "");
					
					userName = util.randomStringgenerator();
					//userName="Zen_jznkccf";
					System.out.println(userName);
					
					if (util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles, currencyID,isoCode))
					{
						Thread.sleep(3000);
						
						log.debug("Updating the balance");
						String balance="10000000";
											
						if(cfnlib.XpathMap.get("LVC").equalsIgnoreCase("Yes")) 
						{
							if(util.migrateUser(userName))
							{
								log.debug("Able to migrate user");
								System.out.println("Able to migrate user");

								log.debug("Updating the balance");
								balance="700000000000";
							Thread.sleep(120000);		
							
							}
						}
						
						util.updateUserBalance(userName,balance);
						Thread.sleep(3000);	
						
						String launchURl = url.replaceFirst("\\busername=.*?(&|$)", "username=" + userName + "$1");
						
						log.info("url = " + launchURl);
						System.out.println(launchURl);
				

						if ("Yes".equalsIgnoreCase(cfnlib.XpathMap.get("NFD"))) {
							report.detailsAppendFolder("Verify Splash/Loading screen is visible",
									"Splash/Loading screen should be visible",
									"Splash/Loading screen is visible", "Pass", currencyName);
							Thread.sleep(8000);
							report.detailsAppendFolder("Verify Continue button on base scene ",
									"Continue buttion", "Continue button", "Pass", currencyName);

							cfnlib.clickOnBaseSceneContinueButton(report, imageLibrary, currencyName);
						}	
						
						if(imageLibrary.isImageAppears("Spin"))
							report.detailsAppendFolder("Verify Base Scene", "Base Scene is visible", "Base Scene is visible", "Pass",currencyName );
						else
							report.detailsAppendFolder("Verify Base Scene", "Base Scene is visible", "Base Scene is not visible", "Fail",currencyName);							
					

						//************** Credit currency format ************
						
						report.detailsAppend("Following is the Credit value verification test case","Verify Credit value", "", "");	
						
						cfnlib.verifyCreditsCurrencyFormat(report, regExpr);
					
					
						//************** Bet currency format ************
						
						report.detailsAppend("Following is the Bet value verification test case","Verify Bet value", "", "");
						
						cfnlib.verifyBetCurrencyFormat(report, regExpr);
							
					
						//************** Quick bets ************
						
						report.detailsAppend("Following are the Quick Bet value verification test case","Verify Quick Bet value", "", "");
						
						cfnlib.openBetPanel(report,imageLibrary,currencyName);		
								
						cfnlib.verifyquickbetImg(report, currencyName, regExpr,imageLibrary);
							
					
						//**************** Paytable payouts currency format ************
						
						report.detailsAppend("Following are the Paytable test cases", "Verify Paytable payouts", "", "");		
						
						cfnlib.validatePaytablePayoutsCurrencyFormat(report, imageLibrary, currencyName, regExpr);
									
						
						//*************** Progreesive bar *******************
						cfnlib.verifyProgressiveBar(report, imageLibrary, currencyName, regExpr);
						
						
						
						//*************** Win Scenarios *******************
		
						report.detailsAppend("Following are the win test cases", "Verify win in base scene", "", "");
							
						cfnlib.verifyNormalWinCurrencyFormat(report,imageLibrary,currencyName,regExpr);
						
						cfnlib.verifyBigWinCurrencyFormat(report,imageLibrary,currencyName,regExpr);
						
						if(cfnlib.XpathMap.get("IsBonusWinAvailable").equalsIgnoreCase("Yes"))
						{
							cfnlib.verifyBonusWinCurrencyFormat(report,imageLibrary,currencyName,regExpr);
						}
						
						
						//*************** FREE SPINS *******************
						
						if (TestPropReader.getInstance().getProperty("IsFreeSpinAvailable").equalsIgnoreCase("yes")) 
						{
							report.detailsAppend("Following are the Freespin test cases", "Verify Freespin scene", "", "");	
							
							cfnlib.verifyFreeSpinsCurrencyFormat(report,imageLibrary,currencyName,regExpr);
							Thread.sleep(30000);							
	
							cfnlib.verifyFreeSpinsSummaryCurrencyFormat(report,imageLibrary,currencyName,regExpr);
							
							cfnlib.closeFreeSpins(report,imageLibrary,currencyName);
						}
												
				}//test data
				 else
					{
						log.debug("Unable to Copy testdata");
						report.detailsAppendFolder("unable to copy test dat to server ", " ", "", "Fail", currencyName);

					}
				}// try
				catch ( Exception e) 
				{
					log.error("Exception occur while processing currency",e);
					report.detailsAppend("Exception occur while processing currency ", " ", "", "Fail");
					cfnlib.evalException(e);
				}
			}	
		}
	}
										
		// -------------------Handling the exception---------------------//
		catch (Exception e) {
		log.error(e.getMessage(), e);

		}
		// -------------------Closing the connections---------------//
		finally {
			report.endReport();
			if (!copiedFiles.isEmpty()) {
				if (TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
					util.deleteBluemesaTestDataFiles(mid, cid, copiedFiles);
				else
					apiObj.deleteAxiomTestDataFiles(copiedFiles);
			}
			webdriver.close();
			webdriver.quit();
			// proxy.abort();
			Thread.sleep(1000);
		} // closing finally block
	}

}
