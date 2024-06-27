package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;

import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.TestPropReader;
import org.apache.log4j.Logger;
import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import net.lightbody.bmp.BrowserMobProxyServer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.spi.CurrencyNameProvider;

import org.apache.log4j.Logger;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.ImageLibrary;
import com.zensar.automation.library.TestPropReader;
import com.zensar.automation.model.Currency;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop_CS;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop_Force;
import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile_CS;
import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile_CS_Renovate;
import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile_Force;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 * This Script is for Low Value Currency - Base Game, Free Spins
 * ===========================================================================
 * 1.Base Game =========== 1.1.Credits 1.2.Bet 1.3.Autoplay 1.4.Bet Pannel (Min
 * , Max & All Bet Values) 1.5.PayTable , Pay-Outs Validation & Branding
 * Validation 1.6.Win 1.7.Big Win 1.8.Bonus Game 1.9 Menu Items Validation &
 * their navigations 1.10 Top Icons Navigations 1.11 Clock Validation 1.12
 * Refresh on Win (In Base Game , Free Spins)
 * 
 * 2.Free Spins ============= 2.1.Credits 2.2.Total Win 2.3.Big Win -1st Spin
 * 2.4.Normal Win - 2nd Spin 2.5.Big Win - 3rd Spin 2.5.Summary Screen Total Win
 * Validation
 * 
 * 
 * TestData ============== 1.Base Game 1.1 Big Win 1.2 Normal Win 1.3 Big Win
 * 1.4 Bonus 2.Free Spins 2.1 Big Win - 1st Spin 2.2 Normal Win -2nd Spin 2.3
 * Big Win -3rd Spin
 *
 * 
 * @author pb61055
 * 
 */

public class Mobile_Regression_Pack_PlayNext_BaseScene_Modified {
	public ScriptParameters scriptParameters;
	Logger log = Logger.getLogger(Mobile_Regression_Pack_PlayNext_BaseScene_Modified.class.getName());

	public void script() throws Exception {

		String mstrTC_Name = scriptParameters.getMstrTCName();
		String mstrTC_Desc = scriptParameters.getMstrTCDesc();
		String mstrModuleName = scriptParameters.getMstrModuleName();
		BrowserMobProxyServer proxy = scriptParameters.getProxy();
		String startTime = scriptParameters.getStartTime();
		String filePath = scriptParameters.getFilePath();
		AppiumDriver<WebElement> webdriver = scriptParameters.getAppiumWebdriver();
		String userName = scriptParameters.getUserName();
		String DeviceName = scriptParameters.getDeviceName();
		String framework = scriptParameters.getFramework();
		String gameName = scriptParameters.getGameName();
		String language = "Paytable";
		String Status = null;
		int mintDetailCount = 0;
		int mintPassed = 0;
		int mintFailed = 0;
		int mintWarnings = 0;
		int mintStepNo = 0;
		String urlNew = null;
		Mobile_HTML_Report report = new Mobile_HTML_Report(webdriver, DeviceName, filePath, startTime, mstrTC_Name,
				mstrTC_Desc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings, mintStepNo, Status,
				gameName);
		MobileFunctionLibraryFactory mobileFunctionLibraryFactory = new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib = mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,
				report, gameName);
		CommonUtil util = new CommonUtil();
		RestAPILibrary apiObj = new RestAPILibrary();
		cfnlib.setOsPlatform(scriptParameters.getOsPlatform()); // To get OSPlatform

		List<String> copiedFiles = new ArrayList<>();
		int mid = Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		System.out.println("MID : " + mid);
		log.debug("MID : " + mid);
		int cid = Integer.parseInt(TestPropReader.getInstance().getProperty("CIDMobile"));
		System.out.println("CID : " + cid);
		log.debug("CID : " + cid);

		try {
 
			// Step 1
			if (webdriver != null) 
			
			{

				// Implement code for test data copy depending on the env
				String strFileName = TestPropReader.getInstance().getProperty("MobileCurrencyTestDataPath");
				File testDataFile = new File(strFileName);

				List<Map<String, String>> currencyList = util.readCurrList();// mapping

				for (Map<String, String> currencyMap : currencyList) 
				{
					userName = util.randomStringgenerator();
					
					// Step 2: To get the languages in MAP and load the language specific url

					String currencyID = currencyMap.get(Constant.ID).trim();
					String isoCode = currencyMap.get(Constant.ISOCODE).trim();
					String currencyName = currencyMap.get(Constant.ISONAME).trim();
					String languageCurrency = currencyMap.get(Constant.LANGUAGECURRENCY).trim();
					String regExpr = currencyMap.get(Constant.REGEXPRESSION).trim();
					String regExprNoSymbol = currencyMap.get(Constant.REGEXPRESSION_NOSYMBOL).trim();

					if (util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles, currencyID, isoCode))
					{
						System.out.println("Username : " + userName);
						System.out.println("CurrencyName : " + currencyName);
					
						log.debug("Updating the balance");
						String balance="10000000";
										
						if(cfnlib.xpathMap.get("LVC").equalsIgnoreCase("Yes")) 
						{
							if(util.migrateUser(userName))
							{
								log.debug("Able to migrate user");
								System.out.println("Able to migrate user");

								log.debug("Updating the balance");
								balance="700000000000";
								Thread.sleep(120000);		
								Thread.sleep(120000);
							}
						}
					
						util.updateUserBalance(userName,balance);
						Thread.sleep(3000);	

						
						log.debug("Balance Updated as " + balance);
						System.out.println("Balance Updated as " + balance);

						String url = cfnlib.xpathMap.get("ApplicationURL");
						String launchURl = url.replaceFirst("\\busername=.*?(&|$)", "username=" + userName + "$1");
						
						if(launchURl.contains("LanguageCode"))
							urlNew = launchURl.replaceAll("LanguageCode=en", "LanguageCode=" + currencyName);
							else if(launchURl.contains("languagecode"))
							urlNew = launchURl.replaceAll("languagecode=en", "languagecode=" + currencyName);
							else if(launchURl.contains("languageCode"))
							urlNew = launchURl.replaceAll("languageCode=en", "languageCode=" + currencyName);

							log.info("url = " + urlNew);
							System.out.println(urlNew);
							
							webdriver.context("NATIVE_APP");
							ImageLibrary imageLibrary = new ImageLibrary(webdriver, gameName, "Mobile");
							imageLibrary.setScreenOrientation(webdriver.getOrientation().toString().toLowerCase());
							
							
							cfnlib.setChromiumWebViewContext();
							webdriver.navigate().to(urlNew);
							Thread.sleep(15000);
							cfnlib.funcFullScreen();
							Thread.sleep(5000);
								
							report.detailsAppend("*** CURRENCY AND LANGUAGE ***", "Currency: "+currencyName+", Language: "+languageCurrency,"", "");
							
							report.detailsAppendFolder("Currency Name is " + currencyName,"Currency ID is " + currencyID, "ISO Code is " + isoCode, "PASS", currencyName);
								
								

							if ("Yes".equalsIgnoreCase(cfnlib.xpathMap.get("NFD"))) {
							
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
									
							//cfnlib.verifyquickbetImg(report, currencyName, regExpr,imageLibrary);
									
							cfnlib.setMaxBet(report,imageLibrary,currencyName);		
						
										
							//**************** Paytable payouts currency format ************
							
							report.detailsAppend("Following are the Paytable test cases", "Verify Paytable payouts", "", "");		
							
							cfnlib.validatePaytablePayoutsCurrencyFormat(report, imageLibrary, currencyName, regExpr,isoCode);		
							
							

							//*************** Win Scenarios *******************
			
							report.detailsAppend("Following are the win test cases", "Verify win in base scene", "", "");
								
							cfnlib.verifyNormalWinCurrencyFormat(report,imageLibrary,currencyName,regExpr,isoCode);
							
							cfnlib.verifyBigWinCurrencyFormat(report,imageLibrary,currencyName,regExpr,isoCode);
							
							if(cfnlib.xpathMap.get("IsBonusWinAvailable").equalsIgnoreCase("Yes"))
							{
								cfnlib.verifyBonusWinCurrencyFormat(report,imageLibrary,currencyName,regExpr,isoCode);
							}
										

							//*************** FREE SPINS *******************
							
							
							report.detailsAppend("Following are the Freespin test cases", "Verify Freespin scene", "", "");	
							
							if (TestPropReader.getInstance().getProperty("IsFreeSpinAvailable").equalsIgnoreCase("yes")) 
							{
								cfnlib.verifyFreeSpinsCurrencyFormat(report,imageLibrary,currencyName,regExpr,isoCode,regExprNoSymbol);
								Thread.sleep(30000);							
		
								cfnlib.verifyFreeSpinsSummaryCurrencyFormat(report,imageLibrary,currencyName);
								
							}	
										
										

								}else
				{
					log.debug("Unable to Copy testdata");
					report.detailsAppendFolder("unable to copy test dat to server ", " ", "", "Fail", currencyName);

				}
						
			
		
					
				} // closing for loop of currency mapping

			}
		} // closing try block
			// -------------------Handling the exception---------------------//

		catch (Exception e) {
			// log.error(e.getMessage(), e);
			cfnlib.evalException(e);
		}

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
		}
	}

}
