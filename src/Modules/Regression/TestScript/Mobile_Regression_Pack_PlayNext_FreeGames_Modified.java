package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

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
import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import ch.qos.logback.core.net.SyslogOutputStream;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 * 
 * @author pb61055
 *
 */
public class Mobile_Regression_Pack_PlayNext_FreeGames_Modified{
	
	public static Logger log = Logger.getLogger(Mobile_Regression_Pack_PlayNext_FreeGames_Modified.class.getName());
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
		String urlNew = null;

		ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();
		Mobile_HTML_Report report=new Mobile_HTML_Report(webdriver,deviceName,filePath,startTime,mstrTCName,mstrTCDesc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,status,gameName);
		
		MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, report, gameName);

		
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
				//./Mobile_Regression_Language_Verification_FreeGames.testdata
				String strFileName=TestPropReader.getInstance().getProperty("MobileFreeGamesTestDataPath");
				File testDataFile=new File(strFileName);
				
				
				//assigning 2 free game offers to validate discard
				String noOfFreeGameOffers=cfnlib.xpathMap.get("noOfOffers");
				int noOfOffers=(int) Double.parseDouble(noOfFreeGameOffers);
				
				String offerExpirationUtcDate=util.getCurrentTimeWithDateAndTime();
				String strdefaultNoOfFreeGames=cfnlib.xpathMap.get("DefaultNoOfFreeGames");
				int defaultNoOfFreeGames=(int) Double.parseDouble(strdefaultNoOfFreeGames);
				
				boolean isFreeGameAssigned=false;
               
				List<Map<String, String>> currencyList = util.readCurrList();// mapping


				String url = cfnlib.xpathMap.get("ApplicationURL");
				for (Map<String, String> currencyMap : currencyList) {

					try {

						String isoCode = currencyMap.get(Constant.ISOCODE).trim();
						String currencyID = currencyMap.get(Constant.ID).trim();
						String currencyName = currencyMap.get(Constant.ISONAME).trim();
						String languageCurrency = currencyMap.get(Constant.LANGUAGECURRENCY).trim();
						String regExpr = currencyMap.get(Constant.REGEXPRESSION).trim();

						System.out.println(languageCurrency);
						log.debug(this + " I am processing currency:  " + currencyName);
											
						
						
						//Copy Test data to server
						if(	util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles,currencyID,isoCode))
						{
							
							//Generating Random user
							userName=util.randomStringgenerator();
							
							log.debug("Test dat is copy in test Server for Username="+userName);

							isFreeGameAssigned=cfnlib.assignFreeGames(userName,offerExpirationUtcDate,mid,cid,noOfOffers,defaultNoOfFreeGames);
							System.out.println("free games assigned: "+isFreeGameAssigned);
							Thread.sleep(15000);
							log.debug(" Currency Name :  "+currencyName+"Language Name :"+languageCurrency);		
							if(isFreeGameAssigned) 
							{
								log.debug("Updating the balance");
								String balance="10000000";
											
								if(cfnlib.xpathMap.get("LVC").equalsIgnoreCase("Yes")) 
								{
									util.migrateUser(userName);
										
									log.debug("Able to migrate user");
									System.out.println("Able to migrate user");
												
									log.debug("Updating the balance");
									balance="700000000000";
									Thread.sleep(60000);	
								}
											
								util.updateUserBalance(userName,balance);
								Thread.sleep(3000);

																														
								String launchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1" );
								if (launchURl.contains("LanguageCode"))
									urlNew = launchURl.replaceAll("LanguageCode=en", "LanguageCode=" + languageCurrency);
								else if (launchURl.contains("languagecode"))
									urlNew = launchURl.replaceAll("languagecode=en", "languagecode=" + languageCurrency);
								else if (launchURl.contains("languageCode"))
									urlNew = launchURl.replaceAll("languageCode=en", "languageCode=" + languageCurrency);

								log.info("url = " + urlNew);
								
								webdriver.navigate().to(urlNew);
								Thread.sleep(15000);
								cfnlib.funcFullScreen();
								
								ImageLibrary imageLibrary = new ImageLibrary(webdriver, gameName, "Mobile");

								imageLibrary.setScreenOrientation(webdriver.getOrientation().toString().toLowerCase());
												
								
								report.detailsAppend("*** CURRENCY AND LANGUAGE ***", "Currency: "+currencyName+", Language: "+languageCurrency,"", "");

								//******** Free games screen - Info text *********			
								
								report.detailsAppend("Following are the FreeGames Entry Screen test cases", "Verify FreeGames Entry Screen", "", "");
												
								cfnlib.verifyFreeGamesEntryScreen(report, imageLibrary, currencyName,regExpr,isoCode);
								
								
								//********** Play Later *******
								
								report.detailsAppend("Following is the FG Play Later test cases", "Verify FG Play Later", "", "");	
								
								cfnlib.freeGamesPlayLater(report, imageLibrary, currencyName);
												
								if ("Yes".equalsIgnoreCase(cfnlib.xpathMap.get("NFD"))) {
									cfnlib.clickOnBaseSceneContinueButton(report, imageLibrary,currencyName);
								}					
								
													
								//*********** Entry Screen Delete **********
								
								report.detailsAppend("Following is the FG Entry Screen Delete test cases", "Verify FG Entry Screen Delete", "", "");	
								
								// Refresh Game to test "Delete Offer" case
								cfnlib.refreshGame(report,imageLibrary,currencyName);
								
								cfnlib.freeGamesDeleteEntryScreen(report, imageLibrary, currencyName);
												
								//*********** Play now **********			
								
								// Refresh Game to get the second FG offer
								//cfnlib.refreshGame(report,imageLibrary,currencyName);
											
								cfnlib.freeGamesPlayNow(report, imageLibrary, currencyName);		
														
								if ("Yes".equalsIgnoreCase(cfnlib.xpathMap.get("NFD"))) {
									cfnlib.clickOnBaseSceneContinueButton(report, imageLibrary,currencyName);
								}
											
								report.detailsAppend("Following is the FreeGames Credit value verification test case","Verify Credit value in free games", "", "");
								
								cfnlib.verifyCreditsCurrencyFormat(report, regExpr);				
												
								//*************** Win Scenarios *******************
								
								report.detailsAppend("Following are the win test cases", "Verify win in base scene", "", "");
									
								cfnlib.verifyNormalWinCurrencyFormat(report,imageLibrary,currencyName,regExpr, isoCode);
								
								cfnlib.verifyBigWinCurrencyFormat(report,imageLibrary,currencyName,regExpr,isoCode);
								
								
								if(cfnlib.xpathMap.get("isBonusWinInFreeGames").equalsIgnoreCase("Yes"))
								{
									cfnlib.verifyBonusWinCurrencyFormat(report,imageLibrary,currencyName,regExpr,isoCode);
									
								}
								
								//*************** Base game Screen Delete *******************
								
								cfnlib.freeGamesDeleteBaseGameScreen(report, imageLibrary, currencyName);	
								
								
								//*************** Free Games Summary *******************		
								
								cfnlib.verifyFGSummaryScreen(report, imageLibrary, currencyName, regExpr,isoCode);	
											
							
							
							
								}else 
								{
									log.error("Skipping the execution as free games assignment Failed");
									report.detailsAppendFolder("skipping the execution as free games assignment Failed ", " ", "", "Fail", currencyName);
								}
					
							}else
							{
								log.debug("Unable to Copy testdata");
								report.detailsAppendFolder("unable to copy test dat to server ", " ", "", "Fail", currencyName);
							}
						
					
					} // try
					catch (Exception e) {
						log.error("Exception occur while processing currency", e);
						report.detailsAppend("Exception occur while processing currency ", " ", "", "Fail");
						cfnlib.evalException(e);
					}

				} // for loop : mapping currencies

			}
		}
		// -------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error(e.getMessage(), e);
			cfnlib.evalException(e);
		} 
		finally {
			report.endReport();
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