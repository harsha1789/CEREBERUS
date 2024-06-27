package Modules.Regression.TestScript;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.ImageLibrary;
import com.zensar.automation.library.TestPropReader;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 * This Script is for Low Value Currency - Free Games
 * ===========================================================================
 * 1.Free Games
 * ===========
 * 1.1.Summary Screen Validation - Info , Play Now, Play Later , Delete Offer 
 * 1.2.Summary Screen - Info Icon - Amount Validation 
 * 1.3.Clicking on PayNow Button 
 * 1.4.Credit Value Validation 
 * 1.5.Win, Big Win
 * 1.6.Refresh - Validate Resume Button & Info Icon - Amount Validation 
 * 1.7 Summary Screen Total Amount Validation & Back to Base Game 
 * 
 * 
 * 2.TestData
 * =============
 * 2.1 Normal win 
 * 2.2 Big win
 * 2.3 Bonus (If Applicable)
 * 
 * 
 * @author pb61055
 *
 */
public class Desktop_Regression_Pack_PlayNext_FreeGames_Modified 
{	
	Logger log = Logger.getLogger(Desktop_Regression_Pack_PlayNext_FreeGames_Modified.class.getName()); 
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
		int startindex=0;
		String strGameName =null;
		String urlNew=null;
			
		log.info("Before Lunching the game");System.out.println("Before Lunching the game");
		ExcelDataPoolManager excelpoolmanager = new ExcelDataPoolManager();
		Desktop_HTML_Report report = new Desktop_HTML_Report(webdriver,browserName,filePath,startTime,mstrTCName,mstrTCDesc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,status1,gameName);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,report, gameName);
		CommonUtil util = new CommonUtil();
		RestAPILibrary apiObj = new RestAPILibrary();			
		List<String> copiedFiles=new ArrayList<>();
		int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDDesktop"));
		try
		{				 
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
							
				ImageLibrary imageLibrary = new ImageLibrary(webdriver, strGameName, "Desktop");
			               
				String strFileName = TestPropReader.getInstance().getProperty("FreeGamesTestDataPath");
				File testDataFile = new File(strFileName);
				String testDataExcelPath = TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				
				//assigning 2 free game offers to validate discard
				String noOfFreeGameOffers=cfnlib.XpathMap.get("noOfFGOffers");
				int noOfFGOffers=(int) Double.parseDouble(noOfFreeGameOffers);
				
				String offerExpirationUtcDate = util.getCurrentTimeWithDateAndTime();
				String strdefaultNoOfFreeGames = cfnlib.XpathMap.get("DefaultNoOfFreeGames");
				int defaultNoOfFreeGames = (int) Double.parseDouble(strdefaultNoOfFreeGames);
				
				boolean isFreeGameAssigned = false;
				
				List<Map<String, String>> currencyList = util.readCurrList();// mapping				
				for (Map<String, String> currencyMap : currencyList) 
				{
					String currencyID = currencyMap.get(Constant.ID).trim();
					String isoCode = currencyMap.get(Constant.ISOCODE).trim();
					String currencyName = currencyMap.get(Constant.ISONAME).trim();
					String languageCurrency = currencyMap.get(Constant.LANGUAGECURRENCY).trim();
					String regExpr = currencyMap.get(Constant.REGEXPRESSION).trim();
					
					
					report.detailsAppend("*** CURRENCY AND LANGUAGE ***", "Currency: "+currencyName+", Language: "+currencyName,"", "");
					
					
					String url = cfnlib.XpathMap.get("ApplicationURL");
					System.out.println("Currency Name :  " + currencyName);
					log.debug("Currency Name :  " + currencyName);	
					
					userName = util.randomStringgenerator();
					System.out.println(userName);
				
					if (util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles, currencyID,isoCode))
					{
						Thread.sleep(3000);
					
						log.debug("Updating the balance");
						String balance="10000000";
										
						if(cfnlib.XpathMap.get("LVC").equalsIgnoreCase("Yes")) 
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
					
						isFreeGameAssigned = cfnlib.assignFreeGames(userName, offerExpirationUtcDate, mid, cid, noOfFGOffers, defaultNoOfFreeGames);
						System.out.println("Free Games assignement status is : " + isFreeGameAssigned);
						log.debug("Free Games assignement status is : " + isFreeGameAssigned);
						Thread.sleep(30000);
					
						if (isFreeGameAssigned) 
						{	
							
							String launchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1" );
							if (launchURl.contains("LanguageCode"))
								urlNew = launchURl.replaceAll("LanguageCode=en", "LanguageCode=" + languageCurrency);
							else if (launchURl.contains("languagecode"))
								urlNew = launchURl.replaceAll("languagecode=en", "languagecode=" + languageCurrency);
							else if (launchURl.contains("languageCode"))
								urlNew = launchURl.replaceAll("languageCode=en", "languageCode=" + languageCurrency);

							log.info("url = " + urlNew);
							
							cfnlib.loadGame(urlNew);
							Thread.sleep(5000);
							
							
							//******** Free games screen - Info text *********			
							
							report.detailsAppend("Following are the FreeGames Entry Screen test cases", "Verify FreeGames Entry Screen", "", "");
							
							cfnlib.verifyFreeGamesEntryScreen(report, imageLibrary, currencyName,regExpr);
							
							//********** Play Later *******
							
							report.detailsAppend("Following is the FG Play Later test cases", "Verify FG Play Later", "", "");	
							
							cfnlib.freeGamesPlayLater(report, imageLibrary, currencyName);
							
							
							if ("Yes".equalsIgnoreCase(cfnlib.XpathMap.get("NFD"))) {
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
							
							if ("Yes".equalsIgnoreCase(cfnlib.XpathMap.get("NFD"))) {
								cfnlib.clickOnBaseSceneContinueButton(report, imageLibrary,currencyName);
							}
								
											
							report.detailsAppend("Following is the FreeGames Credit value verification test case","Verify Credit value in free games", "", "");
							
							cfnlib.verifyCreditsCurrencyFormat(report, regExpr);	
							
							//*************** Win Scenarios *******************
							
							report.detailsAppend("Following are the win test cases", "Verify win in base scene", "", "");
								
							cfnlib.verifyNormalWinCurrencyFormat(report,imageLibrary,currencyName,regExpr);
							
							cfnlib.verifyFGBigWinCurrencyFormat(report,imageLibrary,currencyName,regExpr);
							
							
							//*************** Resume *******************			 
											
											
							// Refresh Game to get the Resume screen
							cfnlib.refreshGame(report,imageLibrary,currencyName);		
											
							cfnlib.verifyFGResumeScreen(report, imageLibrary, currencyName, regExpr);
							
							
							//*************** Free Games Summary *******************		
											
							cfnlib.verifyFGSummaryScreen(report, imageLibrary, currencyName, regExpr);												
															
						
						}else 
						{ 
							System.out.println("Free Games is not assigned second time: Fail");
							log.debug("Free Games is not assigned second time: Fail");
						}		
					
					} //copy files to Server				
				}//for loop
			}//Webdriver if
	   }//closing try block 
		//-------------------Handling the exception---------------------//
		catch (Exception e) 
		{
			log.error(e.getMessage(),e);
		}
		//-------------------Closing the connections---------------//
		finally
		{
			report.endReport();			
			webdriver.close();
			webdriver.quit();
			//proxy.abort();			
		}		
	}	
}
