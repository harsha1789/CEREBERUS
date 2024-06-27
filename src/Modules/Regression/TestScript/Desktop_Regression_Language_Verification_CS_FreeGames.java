/*package Modules.Regression.TestScript;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;

*//**
 * This script traverse and capture all the screen shots related to free games.
 * It reads the test data excel sheet for configured languages.
 * @author HT67091
 *
 *//*

public class Desktop_Regression_Language_Verification_CS_FreeGames {
	
	public static Logger log = Logger.getLogger(Desktop_Regression_Language_Verification_CS_FreeGames.class.getName());
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
		String languageDescription=null;
		String languageCode=null;
		String status=null;
		int mintDetailCount=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		
		ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();
		String xmlFilePath = TestPropReader.getInstance().getProperty("FreeGamesTestDataPath");
		File sourceFile = new File(xmlFilePath);
		Desktop_HTML_Report language = new Desktop_HTML_Report(webdriver, browserName, filePath, startTime, mstrTCName,
				mstrTCDesc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings, mintStepNo, status,
				gameName);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,language, gameName);


		CommonUtil util = new CommonUtil();
		
		
		
		String urlNew=null;
		String url = cfnlib.XpathMap.get("FreeGameApplicationURLDesktop");
		
		cfnlib.loadGame(url);
		webdriver.navigate().to(url);
		log.debug("navigated to url ");
		
		WebElement element = webdriver.findElement(By.id("free-games-offer-play-now"));
		JavascriptExecutor js= (JavascriptExecutor)webdriver;
		js.executeScript("arguments[0].click();", element);
		
		
		WebElement menuEle = webdriver.findElement(By.xpath("//*[@id='free-games-offer-play-now']"));
		
		menuEle.click();
		
	
		
		
		try {
			if (webdriver != null) {
				Map<String, String> rowData2 = null;
				Map<String, String> rowData3 = null;
				boolean isFreeGameAssigned=true;
				String offerExpirationUtcDate=util.getCurrentTimeWithDateAndTime();
				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				int languageCnt = excelpoolmanager.rowCount(testDataExcelPath, Constant.LANG_XL_SHEET_NAME);
				//Create the random user in configured environment ie. in bluemesa or axiom
				String randomUserName=util.createRandomUser();
				log.debug("The New username is ==" + randomUserName);
				//assign free games to above created user
				
			
			
				String strdefaultNoOfFreeGames=cfnlib.XpathMap.get("DefaultNoOfFreeGames");
				int defaultNoOfFreeGames=(int) Double.parseDouble(strdefaultNoOfFreeGames);
				String balanceTypeId=cfnlib.XpathMap.get("BalanceTypeID");
				Double dblBalanceTypeID=Double.parseDouble(balanceTypeId);

				balanceTypeId=""+dblBalanceTypeID.intValue()+"";
  
				//Implement code for test data copy depending on the env
				String strFileName=TestPropReader.getInstance().getProperty("CurrencyTestDataPath");
				File testDataFile=new File(strFileName);
				
				
				

				//Get mid ,cidMobile ,cidDesktop from test properties 
				int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
				//int cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDDesktop"));
				
				//Assign free games offers to user depending upon the languages configured 
				if(TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
				{

					isFreeGameAssigned=cfnlib.addFreeGameToUserInBluemesa( randomUserName,defaultNoOfFreeGames ,  offerExpirationUtcDate,  balanceTypeId,  mid, 50300,languageCnt*2);
				}
				else
				{
					isFreeGameAssigned=cfnlib.addFreeGameToUserInAxiom(randomUserName,defaultNoOfFreeGames,offerExpirationUtcDate,balanceTypeId,mid,50300,languageCnt*2);
				}
			 
				if(isFreeGameAssigned) {
			//	*****************************
				String url1=null;
				String url11 = cfnlib.XpathMap.get("ApplicationURL");
				//String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+randomUserName+"$1");
				String LaunchURl = url;
				log.info("url = " +LaunchURl);
				cfnlib.loadGame(LaunchURl);
				webdriver.navigate().to(LaunchURl);
				log.debug("navigated to url ");
							
				cfnlib.newFeature();// Added while immortal romance game.			
							if(framework.equalsIgnoreCase("CS")){
								cfnlib.setNameSpace();
								}
							
			
				
				// Process all the configured languages form the language sheet 
				//not needed for 10 lang 
							for(int j=1;j<languageCnt;j++){
					// To get the languages in MAP and load the language specific url
					rowData2 = excelpoolmanager.readExcelByRow(testDataExcelPath, Constant.LANG_XL_SHEET_NAME,  j);
					languageDescription = rowData2.get(Constant.LANGUAGE).trim();
					System.out.println("Language is"+languageDescription);
					languageCode = rowData2.get(Constant.LANG_CODE).trim();
					System.out.println("Language  code is"+languageCode);
					
					cfnlib.newFeature();// Added while immortal romance game.
					
					language.detailsAppendFolder("Verify Free Games Scenes in "+languageDescription+" Language","Free Games Scenes should display in "+languageDescription+" language", "", "", languageCode);
					
					if(j!=2)
					cfnlib.freeGamesContinueExpiry();
					Thread.sleep(3000);
					//Capture Free Game Entry Screen
					boolean isFGAssign = cfnlib.freeGamesEntryScreen();
					if(isFGAssign)
						language.detailsAppendFolder("Check that free games entry screen is displaying", "Free Games entry screen should display","Free Game entry screen is displaying", "Pass", languageCode);
					else
						language.detailsAppendFolder("Check that free games entry screen is displaying", "Free Games entry screen should display","Free Game entry screen is not displaying", "Fail", languageCode);
					
					// Click on info and capture screen shot
					boolean b = cfnlib.freeGameEntryInfo();
					if(b)
						language.detailsAppendFolder("Check that free games entry screen is displaying with free games details", "Free Games entry screen should display with free games details","Free Game entry screen is displaying with free games details", "Pass", languageCode);
					else
						language.detailsAppendFolder("Check that free games entry screen is displaying with free games details", "Free Games entry screen should display with free games details","Free Game entry screen is not displaying with free games details", "Fail", languageCode);

					// Click on play now and capture screen shot of base scene
					boolean b1 = cfnlib.clickPlayNow();
					if(b1)
						language.detailsAppendFolder("Check that Base Scene in free games is displaying", "Base Scene in Free Games should display", "Base Scene in Free Games is displaying with free games details", "Pass", languageCode);
					else
						language.detailsAppendFolder("Check that Base Scene in free games is displaying", "Base Scene in Free Games should display", "Base Scene in Free Games is not displaying with free games details", "Fail", languageCode);
					
					Thread.sleep(2000);
					if(TestPropReader.getInstance().getProperty("IsProgressiveGame").equalsIgnoreCase("Yes"))
					{
						cfnlib.verifyJackPotBonuswithScreenShots(language, languageCode);
					}

					// Click on Spin button 
					cfnlib.spinclick();
					Thread.sleep(4000);

					webdriver.navigate().refresh();
					Thread.sleep(4000);
					cfnlib.newFeature();// Added while immortal romance game.
					cfnlib.acceptAlert();

					//Free Games resume Screen
					String s1 = cfnlib.freeGamesResumescreen();
					if(s1!=null)
						language.detailsAppendFolder("Check that free games resume screen is displaying", "Free Games resume screen should display","Free Game resume screen is displaying", "Pass", languageCode);
					else
						language.detailsAppendFolder("Check that free games resume screen is displaying", "Free Games resume screen should display","Free Game resume screen is not displaying", "Fail", languageCode);
					
					// capture screen shot of Resume screen info
					boolean b2 = cfnlib.freeGameResumeInfo();
					if(b2)
						language.detailsAppendFolder("Check that free games entry screen is displaying with free games details", "Free Games entry screen should display with free games details","Free Game entry screen is displaying with free games details", "Pass", languageCode);
					else
						language.detailsAppendFolder("Check that free games entry screen is displaying with free games details", "Free Games entry screen should display with free games details","Free Game entry screen is not displaying with free games details", "Fail", languageCode);
					
					// Click on discard button of resume screen and capture screen shot
					boolean b3 = cfnlib.resumeScreenDiscardClick();
					if(b3)
						language.detailsAppendFolder("Check that Discard Offer screen is displaying", "Discard Offer screen should display", "Discard Offer screen is displaying", "Pass", languageCode);
					else
						language.detailsAppendFolder("Check that Discard Offer screen is displaying", "Discard Offer screen should display", "Discard Offer screen is not displaying", "Fail", languageCode);

					// Click on confirm discard button and capture screen shot
					boolean b4 = cfnlib.confirmDiscardOffer();
				//	if(b4)
				//		language.detailsAppendFolder("Check that Offer is discarded successfully and Free Games Summary Screen should display", "Offer should be discarded successfully and Free Games Summary Screen should display", "Offer is discarded and Free Games Summary Screen is displaying", "Pass", languageCode);
				//	else
					//	language.detailsAppendFolder("Check that Offer is discarded successfully and Free Games Summary Screen should display", "Offer should be discarded successfully and Free Games Summary Screen should display", "Offer is not discarded and Free Games Summary Screen is not displaying", "Fail", languageCode);

					// Clicks on next offer
					cfnlib.clickNextOffer();
					// Clicks on Play now
					cfnlib.clickPlayNow();
					// Clicks on Spin
					cfnlib.spinclick();
					Thread.sleep(3000);
					language.detailsAppendFolder("Check Base Scene in free games after Spin", "Win in Base Scene in Free Games should display if there is any win", "Base Scene in Free Games is displaying with win if win occurs", "Pass", languageCode);
					Thread.sleep(2000);
					
					//Discards offer from base scene
					cfnlib.clickBaseSceneDiscard();

					//Clicks on confirm discard button and captures the screen shot
					boolean b5 = cfnlib.confirmDiscardOffer();
					if(b5)
						language.detailsAppendFolder("Check that Offer is discarded successfully and Free Games Summary Screen should display", "Offer should be discarded successfully and Free Games Summary Screen should display", "Offer is discarded and Free Games Summary Screen is displaying", "Pass", languageCode);
					else
						language.detailsAppendFolder("Check that Offer is discarded successfully and Free Games Summary Screen should display", "Offer should be discarded successfully and Free Games Summary Screen should display", "Offer is not discarded and Free Games Summary Screen is not displaying", "Fail", languageCode);

					//Refresh and captures the screen shot of expire screen.
					webdriver.navigate().refresh();
					Thread.sleep(5000);
					cfnlib.newFeature();// Added while immortal romance game.
					cfnlib.acceptAlert();
					
					boolean b6 = cfnlib.freeGamesExpriyScreen();
					if(b6)
						language.detailsAppendFolder("Check that Free Game Session Expire Screen is displaying", "Free Game Session Expire Screen should display", "Free Game Session Expire Screen is displaying", "Pass", languageCode);
					else
						language.detailsAppendFolder("Check that Free Game Session Expire Screen is displaying", "Free Game Session Expire Screen should display", "Free Game Session Expire Screen is not displaying", "Fail", languageCode);

					// Logic to change to next language
					if (j + 1 != languageCnt){
						rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, Constant.LANG_XL_SHEET_NAME, j+1);
						languageDescription = rowData3.get(Constant.LANGUAGE).trim();
						String languageCode2 = rowData3.get(Constant.LANG_CODE).trim();

						String currentUrl = webdriver.getCurrentUrl();
						
						if(currentUrl.contains("LanguageCode"))
							urlNew= currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+languageCode2);
						else if(currentUrl.contains("languagecode"))
							urlNew = currentUrl.replaceAll("languagecode="+languageCode, "languagecode="+languageCode2);
						cfnlib.loadGame(urlNew);
						Thread.sleep(2000);
						cfnlib.newFeature();// Added while immortal romance game.
						String error=cfnlib.XpathMap.get("Error");
						if(cfnlib.isElementPresent(error))
						{
							language.detailsAppendFolder("Verify the Language code is " +languageCode2+" ", " Application window should be displayed in " +languageDescription+"", "", "", languageCode2);
							language.detailsAppendFolder("Verify that any error is coming","error must not come","error is coming", "Fail", languageCode2);
							if (j + 2 != languageCnt){
								rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, Constant.LANG_XL_SHEET_NAME,  j+2);
								String languageCode3 = rowData3.get(Constant.LANG_CODE).trim();

								currentUrl = webdriver.getCurrentUrl();
								if(currentUrl.contains("LanguageCode"))
									urlNew= currentUrl.replaceAll("LanguageCode="+languageCode2, "LanguageCode="+languageCode3);
								else if(currentUrl.contains("languagecode"))
									urlNew = currentUrl.replaceAll("languagecode="+languageCode2, "languagecode="+languageCode3);
								cfnlib.loadGame(urlNew);
								Thread.sleep(5000);
								cfnlib.newFeature();// Added while immortal romance game.

							}
							j++;
						}
					}
				}
				}else {
					log.error("Skipping the execution as free games assignment failed");
				}
				
				int rowCount2 = excelpoolmanager.rowCount(testDataExcelPath, Constant.CURRENCY_XL_SHEET_NAME); // get row values 
				System.out.println(rowCount2);
				List<Map<String, String>> curList= util.readCurrList();
				
				for(int j=0;j<rowCount2;j++)
			
				{
				userName=util.randomStringgenerator();
					
				//Step 2: To get the languages in MAP and load the language specific url
				rowData2 = curList.get(j);
				String currencyID = rowData2.get(Constant.ID).trim();
				String isoId = rowData2.get(Constant.ID).trim();
				String isoCode = rowData2.get(Constant.ISOCODE).trim();
				System.out.println(isoCode);
				String CurrencyName = rowData2.get(Constant.ISONAME).trim();
				String languageCurrency = rowData2.get(Constant.LANGUAGECURRENCY).trim();
				String regExpr=rowData2.get(Constant.REGEXPRESSION).trim();
				String regExprNoSymbol=rowData2.get(Constant.REGEXPRESSION_NOSYMBOL).trim();
				
				Thread.sleep(5000);
				//method is used to Spin the Spin Buttton  (BIG Win)
				cfnlib.spinclick();
				Thread.sleep(5000); 
				
	            //method is used to Spins to get Big win and check the currency format
				
				boolean bigWinFormatVerification =	cfnlib.verifyRegularExpression(language,regExpr,cfnlib.verifyBigWin(language));
				if(bigWinFormatVerification)
				{
					language.detailsAppendFolder("Base Game", "Big Win Amt", "Big Win Amt", "PASS",""+CurrencyName);
				}
				else
				{
					language.detailsAppendFolder("Base Game", " Big Win Amt", "Big Win Amt", "FAIL",""+CurrencyName);
				}Thread.sleep(5000); 
				
				
				//method is used to Spin the Spin Buttton and check the currency format  (Bonus in Base Game)
				cfnlib.spinclick();
				Thread.sleep(3000); 
				
				//Base Game Bonus 
				if (cfnlib.checkAvilabilityofElement("BonusFeatureImage"))
				{
					language.detailsAppendFolder("Base Game", " Bonus Feature is Available", " Bonus Feature is Available", "PASS",""+CurrencyName);
				 
				  //method is used to get the click , get the bonus text,verifies bonus summary screen in base game and check the currency format
				  cfnlib.verifyRegularExpressionUsingArrays(language,regExprNoSymbol,cfnlib.bonusFeatureClickandGetText(language,CurrencyName));
				}
				else
				{
					language.detailsAppendFolder("Base Game", " Bonus Feature is Not Available", " Bonus Feature is Not Available", "FAIL",""+CurrencyName);
				}
				
				 // }//closing for loop		
						
				}
			}

		}
		// -------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error(e.getMessage(),e);
			cfnlib.evalException(e);
		}
		// -------------------Closing the connections---------------//
		finally {
			language.endReport();
			if(destFile.delete()){
				System.out.println(destFile.getName() + " is deleted!");
			}else{
				System.out.println("Test Data Delete operation is failed.");
			}
			webdriver.close();
			webdriver.quit();
			//proxy.abort();
			Thread.sleep(1000);
		}
	}
}
*/