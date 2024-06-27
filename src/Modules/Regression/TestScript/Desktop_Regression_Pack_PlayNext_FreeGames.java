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
 * @author VC66297
 *
 */
public class Desktop_Regression_Pack_PlayNext_FreeGames 
{	
	Logger log = Logger.getLogger(Desktop_Regression_Pack_PlayNext_FreeGames.class.getName()); 
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
			
		log.info("Before Launching the game");System.out.println("Before Launching the game");
		ExcelDataPoolManager excelpoolmanager = new ExcelDataPoolManager();
		Desktop_HTML_Report lvcReport = new Desktop_HTML_Report(webdriver,browserName,filePath,startTime,mstrTCName,mstrTCDesc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,status1,gameName);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,lvcReport, gameName);
		CommonUtil util = new CommonUtil();
		RestAPILibrary apiObj = new RestAPILibrary();			
		List<String> copiedFiles=new ArrayList<>();
		int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDDesktop"));
		try
		{				 
			if(webdriver!=null)
			{					
				String strFileName = TestPropReader.getInstance().getProperty("FreeGamesTestDataPath");
				File testDataFile = new File(strFileName);
				String testDataExcelPath = TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				int languageCnt = excelpoolmanager.rowCount(testDataExcelPath, Constant.LANG_XL_SHEET_NAME);
				languageCnt=1;
				String offerExpirationUtcDate = util.getCurrentTimeWithDateAndTime();
				String strdefaultNoOfFreeGames = cfnlib.XpathMap.get("DefaultNoOfFreeGames");
				System.out.println("Number of Free Games Assigned : " + strdefaultNoOfFreeGames);
				log.debug("Number of Free Games Assigned : " + strdefaultNoOfFreeGames);
				int defaultNoOfFreeGames = (int) Double.parseDouble(strdefaultNoOfFreeGames);
				boolean isGameLoaded = false;							
				List<Map<String, String>> currencyList = util.readCurrList();// mapping				
				for (Map<String, String> currencyMap : currencyList) 
				{
					//userName=util.randomStringgenerator();
					
					userName = "harsha83";
					

					
					
					
					boolean isFreeGameAssigned = false;
					// verifying free games
					int maxFGRetCount = 5;int count = 0;
					while(count < maxFGRetCount)
					{	
					String CurrencyName = currencyMap.get(Constant.ISONAME).trim();
					String currencyID = currencyMap.get(Constant.ID).trim();
					String isoCode = currencyMap.get(Constant.ISOCODE).trim();
					System.out.println(isoCode);
					String currencyName = currencyMap.get(Constant.ISONAME).trim();
					String languageCurrency = currencyMap.get(Constant.LANGUAGECURRENCY).trim();
					String regExpr = currencyMap.get(Constant.REGEXPRESSION).trim();
					String regExprNoSymbol = currencyMap.get(Constant.REGEXPRESSION_NOSYMBOL).trim();
					
					
					
					String url = cfnlib.XpathMap.get("ApplicationURL");
					System.out.println("Currency Name :  " + currencyName);
					log.debug("Currency Name :  " + currencyName);	
					
					
					if(util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles, currencyID, isoCode))
					{
						System.out.println("Username : " + userName);System.out.println("CurrencyName: " + currencyName);
						log.debug("Test data is copied in the test Server for Username = " + userName);
						Thread.sleep(3000);																												
						/*if(util.migrateUser(userName))
							{*/
								log.debug("User Migration : PASS");System.out.println("User Migration : PASS");	
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
												
					//			if(util.updateUserBalance(userName,balance))								
					//			{
									log.debug("Balance Updated as "+balance );
									System.out.println("Balance Updated as "+balance );
									isFreeGameAssigned = cfnlib.assignFreeGames(userName, offerExpirationUtcDate, mid, cid, languageCnt, defaultNoOfFreeGames);
									System.out.println("Free Games assignement status is : " + isFreeGameAssigned);log.debug("Free Games assignement status is : " + isFreeGameAssigned);
									Thread.sleep(30000);
					

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
										
					                isFreeGameAssigned =true;
									if (isFreeGameAssigned) 
									{		                
										String launchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1" );
										launchURl = launchURl.replaceAll("\\blanguageCode=.*?(&|$)", "languageCode="+languageCurrency+"$1");
										System.out.println(launchURl); log.info("url = " +launchURl);					
										if("Yes".equalsIgnoreCase(cfnlib.XpathMap.get("Clock")))
										{										
											cfnlib.loadGame(launchURl);
											
											Thread.sleep(10000);
										}
										else
										{
											cfnlib.webdriver.navigate().to(launchURl);
											cfnlib.waitForPageToBeReady();
											Thread.sleep(10000);
										}				
										
										//resize browser   waitTill
									//	cfnlib.resizeBrowser(1280, 960);
																				
										if(imageLibrary.isImageAppears("FreeGamesPlayLater"))
										{	
											lvcReport.detailsAppend("Verify Game launched ", "Game should be launched", "Game is launched", "PASS");
										}
										else
										{
											lvcReport.detailsAppend("Verify Game launchaed ", "Game should be launched", "Game is not launched", "FAIL");
										}	Thread.sleep(3000);						
										boolean isfreeGameEntryInfoVisible = cfnlib.verifyRegularExpression(lvcReport,regExpr,cfnlib.freeGameEntryInfo(lvcReport,regExpr,imageLibrary,"FreeGamesInfoText"));
										if (isfreeGameEntryInfoVisible)
										{
											System.out.println("Free Games Entry Screen Info Icon Text Validation : PASS");log.debug("Free Games Entry Screen Info Icon Text Validation : PASS");
											lvcReport.detailsAppend("Free Games"," Entry Screen Info Text Validation"," Entry Screen Info Text Validation", "PASS");
										}
										else
										{
											System.out.println("Free Games Entry Screen Info Icon Text Validation : FAIL");log.debug("Free Games Entry Screen Info Icon Text Validation : FAIL");
											lvcReport.detailsAppend("Free Games"," Entry Screen Info Text Validation","Free Games Entry Screen Info", "FAIL");
										}
										// Check Play Later 
										Thread.sleep(5000);
										if(imageLibrary.isImageAppears("FreeGamesPlayNow"))
										{
											
											lvcReport.detailsAppend("Verify FG Play Later button visible", "Play Later button Should be visible", "FG Play Later button is visible", "PASS");
											Thread.sleep(3000);
											imageLibrary.click("FreeGamesPlayLater");
											
											//	cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("freeGamesOffersPlayLaterButtonx"),"return " + cfnlib.XpathMap.get("freeGamesOffersPlayLaterButtony"));
											Thread.sleep(3000);
											if(cfnlib.XpathMap.get("NFD").equalsIgnoreCase("Yes"))
											{
												
													//check for visiblity of nfd button and take screenshot
													Thread.sleep(5000);
													if(imageLibrary.isImageAppears("NFDButton"))
													{
														//lvcReport.detailsAppend("Verify Continue button is visible to check FG Play Later ", "Continue buttion should be visible", "Continue button is visible", "PASS");
														//Click on nfd button
														
															imageLibrary.click("NFDButton");
															
															lvcReport.detailsAppend("Verify if able to click on continue",
																	"Should be able to click on continue button", "Able to click on continue button",
																	"PASS");
															System.out.println("NFD button clicked");
													
													      Thread.sleep(5000);
													  
														if(imageLibrary.isImageAppears("Spin"))
														{
															System.out.println("Base Scene after FG Play Later");log.debug("Base Scene after FG Play Later");
															lvcReport.detailsAppend("Verify Base Scene after FG Play Later", "Base is visible", "Free Game Scene is visible", "PASS");
														}
														else
														{
															System.out.println("Base Scene after FG Play Later is not visible");log.debug("Base Scene after FG Play Later is not visible");
															lvcReport.detailsAppend("Verify Base Scene after FG Play Later", "Base is visible", "Free Game Scene is not visible", "FAIL");
														}
													}
													else
													{
														lvcReport.detailsAppend("Verify Continue button is visible after FG Play Later ", "Continue buttion should be visible", "Continue button is not visible", "FAIL");
													}
												}
												else
												{				
													Thread.sleep(5000);
											
													if(imageLibrary.isImageAppears("Spin"))
													{
														System.out.println("Base Scene after FG Play Later");log.debug("Base Scene after FG Play Later");
														lvcReport.detailsAppend("Verify Base Scene after FG Play Later", "Base is visible", "Free Game Scene is visible", "PASS");
													}
													else
													{
														System.out.println("Base Scene after FG Play Later is not visible");log.debug("Base Scene after FG Play Later is not visible");
														lvcReport.detailsAppend("Verify Base Scene after FG Play Later", "Base is visible", "Free Game Scene is not visible", "FAIL");
													}																									
												}	
											}
																										
										}	
										else
										{
											lvcReport.detailsAppend("Verify FG Play Later button visible", "Play Later button Should be visible", "FG Play Later button is not visible", "FAIL");
										}										
										// Refresh Game to test "Delete Offer" case
										cfnlib.RefreshGame("clock");
										Thread.sleep(5000);									
										if(imageLibrary.isImageAppears("FreeGamesPlayNow"))
										{	
											lvcReport.detailsAppend("Verify Game launchaed after refresh to check Discard FG Offer", "Game should be launched", "Game is launched", "PASS");										
											if(imageLibrary.isImageAppears("FGDelete"))	
											{
												lvcReport.detailsAppend("Verify FG Discard offer Button visible", "FG Discard offer Button should be visible", "FG Discard offer Button is visible", "PASS");
												imageLibrary.click("FGDelete");
												
												Thread.sleep(1000);											
												if(imageLibrary.isImageAppears("FGDiscard"))	
												{	
													lvcReport.detailsAppend("Verify FG Discard page visible", "FG Discard page should be visible", "Discard page is visible", "PASS");
													imageLibrary.click("FGDiscard");
													
													Thread.sleep(5000);
													if(imageLibrary.isImageAppears("NFDButton"))
													{
														lvcReport.detailsAppend("Verify Continue button is visible after discarding FG ", "FG should be discarded and Continue buttion shold be visible", "Continue button is visible", "PASS");													
													}		
													else
													{
														lvcReport.detailsAppend("Verify Continue button is visible after discarding FG ", "FG should be discarded and Continue buttion shold be visible", "Continue button is not visible", "FAIL");
													}
												}
												else
												{
													lvcReport.detailsAppend("VerifyFG Discard page visible ", "FG Discard page should be visible", "Discard page is not visible", "FAIL");
												}												
											}
											else
											{
												lvcReport.detailsAppend("Verify FG Discard offer Button visible", "FG Discard offer Button should be visible", "FG Delete offer Button is not visible", "FAIL");
											}										
										}
										else
										{
											lvcReport.detailsAppend("Verify Game launchaed after refresh to check Discard FG Offer ", "Game should be launched", "Game is not launched", "FAIL");
										}
										//Assign Free Games to test Normal win and Big win
										boolean isFreeGameAssigned1 = false;
										isFreeGameAssigned1 = cfnlib.assignFreeGames(userName, offerExpirationUtcDate, mid, cid, languageCnt, defaultNoOfFreeGames);
										System.out.println("Free Games assignement status is : " + isFreeGameAssigned1);log.debug("Free Games assignement status is : " + isFreeGameAssigned1);
										Thread.sleep(10000);	
									//	boolean isFreeGameAssigned1 = true;
										if (isFreeGameAssigned1) 
										{	
											Thread.sleep(10000);
											cfnlib.RefreshGame("clock");
											Thread.sleep(5000);											
											//click on play now button on Free Games Intro Screen 											
											if(imageLibrary.isImageAppears("FreeGamesPlayNow"))
											{
												imageLibrary.click("FreeGamesPlayNow");										
												Thread.sleep(3000);																
												if(cfnlib.XpathMap.get("NFD").equalsIgnoreCase("Yes"))
												{
													if(cfnlib.XpathMap.get("NFDHasHook").equalsIgnoreCase("Yes"))
													{
														//check for visiblity of nfd button and take screenshot
														
														if(imageLibrary.isImageAppears("NFDButton"))
														{
															lvcReport.detailsAppend("Verify Continue button is visible ", "Continue buttion is visible", "Continue button is visible", "PASS");
															//Click on nfd button
															Thread.sleep(5000);
															imageLibrary.click("NFDButton");
														//	cfnlib.ClickByCoordinates("return "+cfnlib.XpathMap.get("ClicktoContinueCoordinatex"),"return "+cfnlib.XpathMap.get("ClicktoContinueCoordinatey"));
															Thread.sleep(10000);
															if(imageLibrary.isImageAppears("Spin"))															
															{
																System.out.println("Free Game Scene is visible");log.debug("Free Game Scene is visible");
																lvcReport.detailsAppend("Verify Free Game Scene", "Free Game Scene is visible", "Free Game Scene is visible", "PASS");
															}
															else
															{
																System.out.println("Free Game Scene is not visible");log.debug("Free Game Scene is not visible");
																lvcReport.detailsAppend("Verify Free Game Scene", "Free Game Scene is visible", "Free Game Scene is not visible", "PASS");
															}
														}
														else
														{
														lvcReport.detailsAppend("Verify Continue button is visible ", "Continue buttion is visible", "Continue button is not visible", "FAIL");
														}
													}
												}
												else
												{						
													
													if(imageLibrary.isImageAppears("Spin"))	
													{
														System.out.println("Free Game Scene is visible");log.debug("Free Game Scene is visible");
														lvcReport.detailsAppend("Verify Free Game Scene", "Free Game Scene is visible", "Free Game Scene is visible", "PASS");
													}
													else
													{
														System.out.println("Free Game Scene is not visible");log.debug("Free Game Scene is not visible");
														lvcReport.detailsAppend("Verify Free Game Scene", "Free Game Scene is visible", "Free Game Scene is not visible", "PASS");
													}																
												}	
											}
											// Gets the credit Amount && verifies Currency Format and check the currency format
											boolean creditAmount = cfnlib.verifyRegularExpression(lvcReport,regExpr,cfnlib.getConsoleText("return " + cfnlib.XpathMap.get("Creditvalue")));
											if(creditAmount)
											{
												System.out.println("Free Game credit Amount : PASS");log.debug("Free Game Game credit Amount : PASS");
												lvcReport.detailsAppendFolder("Free Game Game", "credit Amount", "credit Amount", "PASS","");
											}
											else
											{
												System.out.println("Free Game credit Amount : FAIL");log.debug("Free Game Game credit Amount : FAIL");
												lvcReport.detailsAppendFolder("Free Game Game", "credit Amount", "credit Amount", "FAIL","");
											}	
											
											 
											
											// method is used to Spin the Spin Button (Normal Win)
											try {
												if(imageLibrary.isImageAppears("Spin"))	 {
													cfnlib.details_append_folderOnlyScreeshot(webdriver, "SpinBtnWrk");
													Thread.sleep(2000);
													// click on spin button
													imageLibrary.click("Spin");
												//	cfnlib.ClickByCoordinates("return " + cfnlib.XpathMap.get("SpinBtnCoordinatex"),"return " + cfnlib.XpathMap.get("SpinBtnCoordinatey"));
													Thread.sleep(10000);
													// check if spin is successful,take screenshot
													lvcReport.detailsAppend("verify BaseGame normal win","Spin Button is working", "Spin Button is working", "pass");
													// method is used to get the Win amt and check the currency format
													boolean winFormatVerification = cfnlib.verifyRegularExpression(lvcReport, regExpr,
															cfnlib.getCurrentWinAmt1(lvcReport, CurrencyName));
													if (winFormatVerification) {
														System.out.println("Base Game Win Value : PASS");
														log.debug("Base Game Win Value : PASS");
														lvcReport.detailsAppend("verify Base Game win amt", " Win Amt",
																"Win Amt", "PASS");
													} else {
														System.out.println("Base Game Win Value : FAIL");
														log.debug("Base Game Win Value : FAIL");
														lvcReport.detailsAppend("verify Base Game win amt", " Win Amt",
																"Win Amt", "FAIL");
													}
												} else {
													cfnlib.details_append_folderOnlyScreeshot(webdriver, "SpinBtnNtWrk");
													lvcReport.detailsAppend("verify BaseGame normal win",
															"Spin Button is not working", "Spin Button is not working", "Fail",
															"" + CurrencyName);

												}
											} catch (Exception e) {
												log.error(e.getMessage(), e);
												cfnlib.evalException(e);
											}
											Thread.sleep(2000);
																								
											// method is used to Spin the Spin Button (Big Win)
											try {
												if(imageLibrary.isImageAppears("Spin")) {
													Thread.sleep(3000);
													imageLibrary.click("Spin");
													Thread.sleep(20000);
													// Verifies the Big Win currency format
													boolean bigWinFormatVerification = cfnlib.verifyRegularExpression(lvcReport, 
															regExpr, cfnlib.verifyFGBigWin(lvcReport,imageLibrary));
													if (bigWinFormatVerification) {
														System.out.println("Base Game BigWin Value : PASS");
														log.debug("Base Game BigWin Value : PASS");
														lvcReport.detailsAppend("verify Base Game big win", "Big Win Amt",
																"Big Win Amt", "PASS");
													} else {
														System.out.println("Base Game BigWin Value : FAIL");
														log.debug("Base Game BigWin Value : FAIL");
														lvcReport.detailsAppend("verify Base Game big win", " Big Win Amt",
																"Big Win Amt", "FAIL");
													}

												}
											} catch (Exception e) {
												log.error(e.getMessage(), e);
												cfnlib.evalException(e);
											}
											
											// free games on refresh, Resume and Info Text currency validation					
											cfnlib.RefreshGame("clock");
											Thread.sleep(5000);
											boolean freeGamesOnRefresh =cfnlib.verifyRegularExpression(lvcReport,regExpr,cfnlib.freeGameOnRefresh(lvcReport,currencyName,"isFGResumeBtnVisible","isFGResumeInfoBtnVisible","FGResumeInfoBtnx","FGResumeInfoBtny","FGinfoTxt",imageLibrary));
											if(freeGamesOnRefresh )
											{
												System.out.println("Free Game On Refresh : PASS");log.debug("Free Game On Refresh : PASS");
												lvcReport.detailsAppendFolder("Free Game", "On Refresh FG Info Text Validation", "On Refresh FG Info Text Validation ", "PASS",currencyName);					
											}
											else
											{
												System.out.println("Free Game On Refresh : FAIL");log.debug("Free Game On Refresh : FAIL");
												lvcReport.detailsAppendFolder("Free Game", "On Refresh FG Info Text Validation", "On Refresh FG Info Text Validation ", "FAIL",currencyName);
											}Thread.sleep(2000);						
											//Click on Resume 
											
											imageLibrary.click("FGResumePlay");											
											Thread.sleep(5000);
											//click on Continue
											if(cfnlib.XpathMap.get("NFD").equalsIgnoreCase("Yes"))
											{
												if(cfnlib.XpathMap.get("NFDHasHook").equalsIgnoreCase("Yes"))
												{
													//check for visiblity of nfd button and take screenshot
													if(imageLibrary.isImageAppears("NFDButton"))
													{
														lvcReport.detailsAppend("Verify Continue button is visible ", "Continue buttion is visible", "Continue button is visible", "PASS");
													}
													else
													{
														lvcReport.detailsAppend("Verify Continue button is visible ", "Continue buttion is visible", "Continue button is not visible", "FAIL");
													}								
													//Click on nfd button
													Thread.sleep(2000);
													imageLibrary.click("NFDButton");
												//	cfnlib.ClickByCoordinates("return "+cfnlib.XpathMap.get("ClicktoContinueCoordinatex"),"return "+cfnlib.XpathMap.get("ClicktoContinueCoordinatey"));
													Thread.sleep(10000);
													
													if(imageLibrary.isImageAppears("Spin"))
													{
														lvcReport.detailsAppend("Verify Free Game Scene", "Free Game Scene is visible", "Free Game Scene is visible", "PASS");
													}
													else
													{
														lvcReport.detailsAppend("Verify Free Game Scene", "Free Game Scene is visible", "Free Game Scene is not visible", "PASS");
													}												
												}
											}					
											else
											{		
												if(imageLibrary.isImageAppears("Spin"))												
												{
													lvcReport.detailsAppend("Verify Free Game Scene", "Free Game Scene is visible", "Free Game Scene is visible", "PASS");
												}
												else
												{
													lvcReport.detailsAppend("Verify Free Game Scene", "Free Game Scene is visible", "Free Game Scene is not visible", "PASS");
												}												
											}
											// Gets the credit Amount && verifies Currency Format and check the currency format	
											boolean creditAmount1 = cfnlib.verifyRegularExpression(lvcReport,regExpr,cfnlib.getConsoleText("return "+cfnlib.XpathMap.get("CreditValue")));
											if(creditAmount1)
											{
												System.out.println("Free Game Game credit Amount : PASS");log.debug("Free Game Game credit Amount : PASS");
												lvcReport.detailsAppendFolder("Free Game", "credit Amount", "credit Amount", "PASS","");
											}
											else
											{
												System.out.println("Free Game credit Amount : FAIL");log.debug("Free Game Game credit Amount : FAIL");
												lvcReport.detailsAppendFolder("Free Game", "credit Amount", "credit Amount", "FAIL","");
											}																	
											// FG Summary
											cfnlib.SpinUntilFGSummary("FGSummaryBackToGameBtn", imageLibrary);
											boolean FGSummaryWonAmount = cfnlib.verifyRegularExpression(lvcReport,regExpr,cfnlib.getConsoleText("return "+cfnlib.XpathMap.get("FGSummaryWonAmount")));
											if(FGSummaryWonAmount)
											{
												System.out.println("Free Game Summary won Amount : PASS");log.debug("Free Game Summary won Amount : PASS");
												lvcReport.detailsAppendFolder("Free Game ", "credit Summary won Amount", "credit Summary won Amount", "PASS","");
											}
											else
											{
												System.out.println("Free Game Summary won Amount : PASS");log.debug("Free  Game Summary won Amount : FAIL");
												lvcReport.detailsAppendFolder("Free Game ", "credit Summary won Amount", "credit Summary won Amount", "FAIL","");
											}
										}else 
										{ 
											System.out.println("Free Games is not assigned second time: FAIL");
											log.debug("Free Games is not assigned second time: FAIL");
										}										
					/*		}else
								{
									System.out.println("Free Games is not assigned : FAIL");
									log.debug("Free Games is not assigned : FAIL");
								}*/
							/*}
							else
							{
								System.out.println("Balance is not updated : FAIL");
								log.debug("Balance is not updated : FAIL");
							}
						}else
						{
							System.out.println("User Migration : FAIL");
							log.debug("User Migration : FAIL");
						}	
								*/
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
			lvcReport.endReport();			
			webdriver.close();
			webdriver.quit();
			//proxy.abort();			
		}		
	}	
}
