package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;


/**
 * This script traverse and capture all the screen shots related to bonus .
 * It reads the test data excel sheet for configured languages and currency
 * This require a test data which will trigger bonus.collect screenshots for piggy bank bonus and robber bonus
 * @author pb61055
 * */

public class DesktopRegressionLanguageVerificationbustTheBankBonusCurrency{

	Logger log = Logger.getLogger(DesktopRegressionLanguageVerificationbustTheBankBonusCurrency.class.getName());
	public ScriptParameters scriptParameters;



	public void script() throws Exception{

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
		String status=null;
		int mintDetailCount=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		String isoId=null;
		String isoCode=null;
		String isoName = null;
		String languageCurrency = null;
		String currencyFormat=null;

		ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();
		Desktop_HTML_Report language = new Desktop_HTML_Report(webdriver, browserName, filePath, startTime, mstrTCName,
				mstrTCDesc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings, mintStepNo, status,
				gameName);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,language, gameName);

		CommonUtil util = new CommonUtil();
		int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int  cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDDesktop"));
		List<String> copiedFiles=new ArrayList<>();
		RestAPILibrary apiobj=new RestAPILibrary();

		try{
			// Step 1 
			if(webdriver!=null)
			{		
				Map<String, String> rowData2 = null;
				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				String urlNew=null;
				WebDriverWait wait=new WebDriverWait(webdriver,60);
				String strFileName=TestPropReader.getInstance().getProperty("FreeSpinsTestDataPath");
				File testDataFile=new File(strFileName);

				int rowCount2 = excelpoolmanager.rowCount(testDataExcelPath, Constant.CURRENCY_XL_SHEET_NAME);

				List<Map<String, String>> curList= util.readCurrList();


				for(int j=1;j<rowCount2;j++)
				{
					userName=util.randomStringgenerator();
					rowData2 = curList.get(j);
					isoId = rowData2.get(Constant.ID).trim();
					isoCode = rowData2.get(Constant.ISOCODE).trim().replace("\u00A0", "");
					isoName = rowData2.get(Constant.ISONAME).trim().replace("\u00A0", "");
					languageCurrency = rowData2.get(Constant.LANGUAGECURRENCY).trim();
					currencyFormat=rowData2.get(Constant.DISPALYFORMAT).trim();



					if(	util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles,isoId,isoCode))
					{
						log.debug("Test dat is copy in test Server for Username="+userName);

						String url = cfnlib.XpathMap.get("ApplicationURL");
						String launchURL = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
						//condition added for titan and v 
						if(launchURL.contains("LanguageCode"))
							urlNew = launchURL.replaceAll("\\bLanguageCode=.*?(&|$)", "LanguageCode="+languageCurrency+"$1");

						else if(launchURL.contains("languagecode"))
							urlNew = launchURL.replaceAll("\\blanguagecode=.*?(&|$)", "languagecode="+languageCurrency+"$1");


						log.info("url = " +urlNew);

						if(cfnlib.loadGame(urlNew))
						{
							log.debug("navigated to url ");

							
							if(framework.equalsIgnoreCase(Constant.FORCE)){
								cfnlib.setNameSpace();
							}
							//Step 2: To get the languages in MAP and load the language specific url
							language.detailsAppendFolder("Verify translations on Bonus feature","Bonus feature should display as per respective language and currency", "", "" ,isoCode+"_"+languageCurrency);

							System.out.println("==================Language currency ================ "+languageCurrency);
							log.debug("==================Language currency ================ "+languageCurrency);
							//Clicking on continue button
							cfnlib.newFeature();
							System.out.println("clicked on continue");
							
							//Wait for spin button
							cfnlib.waitForSpinButton();
							
							//Getting currency symbol from base scene credit
							String currencySymbol = cfnlib.getCurrencySymbol();
							log.info(isoName+"currency symbol is " + currencySymbol);

							//Setting Max bet
							cfnlib.setMaxBet();
							System.out.println("Set Max bet");
							//check and update the credits if bet value is more and refresh

							String credit = cfnlib.getCurrentCredits();
							cfnlib.threadSleep(3000);
							credit=credit.toLowerCase().replace("credits: ", "");
							credit=credit.replaceAll("[^0-9]", "");


							double currentCredit = Double.parseDouble(credit);
							log.debug("Current credit:"+currentCredit);
							//receiving the bet
							String bet = cfnlib.getCurrentBet();
							bet=bet.replaceAll("[^0-9]", "");
							double currentBet = Double.parseDouble(bet);
							log.debug("Current Bet:"+currentBet);

							if(currentBet>currentCredit){
								log.debug("Updating the balance");
								util.updateUserBalance(userName,(currentBet+1000)*100);
								webdriver.navigate().refresh();
								cfnlib.newFeature();
								cfnlib.setMaxBet();
							}

							language.detailsAppendFolder("Verify Language translations in Base scene "  + isoCode+"Currency and in "+languageCurrency + " "," Base scene in " + isoCode+"Currency and in "+languageCurrency + " "," Base scene in "  + isoCode+"Currency and in "+languageCurrency + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);
							cfnlib.threadSleep(5000);
							
							System.out.println("================== PIGGY BANK BONUS ================ ");
							log.debug("================== PIGGY BANK BONUS ================ ");
							//Clicking on spin button
							cfnlib.spinclick();
							System.out.println("spin is clicked");
							Thread.sleep(2500);
							
							//Piggy bank lands on reels
							language.detailsAppendFolder("Verify Language translations in Base scene  for PIGGY BANK BONUS " + isoCode+" Currency and in "+languageCurrency +" "," Base scene in " + isoCode+" Currency and in "+languageCurrency +" "," Base scene in "  + isoCode+" Currency and in "+languageCurrency + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);
							Thread.sleep(500);
							
							//Piggy bank enlarges with currency symbol on it
							language.detailsAppendFolder("Verify Currency symbol on piggy bank in " + isoCode+" Currency and in "+languageCurrency +" "," Currency symbol on piggy bank in " + isoCode+ "Currency and in "+languageCurrency +" "," Currency symbol on piggy bank in "  + isoCode+" Currency and in "+languageCurrency + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);					
							
							//Piggy bank scatter with Crack text
							if(cfnlib.elementWait("return "+cfnlib.XpathMap.get("CrackTextVisible"), true ))
							{
								language.detailsAppendFolder("Verify Language translations on Piggy bank 1 " + isoCode+" Currency and in "+languageCurrency +" "," Piggy bank in " + isoCode+" Currency and in "+languageCurrency +" "," Piggy bank in "  + isoCode+" Currency and in "+languageCurrency + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);
								language.detailsAppendFolder("Verify Language translations on Piggy bank 2 " + isoCode+" Currency and in "+languageCurrency +" "," Piggy bank in " + isoCode+" Currency and in "+languageCurrency +" "," Piggy bank in "  + isoCode+" Currency and in "+languageCurrency + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);
								language.detailsAppendFolder("Verify Language translations on Piggy bank 3 " + isoCode+" Currency and in "+languageCurrency +" "," Piggy bank in " + isoCode+" Currency and in "+languageCurrency +" "," Piggy bank in "  + isoCode+" Currency and in "+languageCurrency + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);						
							}
							else
							{
								language.detailsAppendFolder("Verify Language translations on Piggy bank " + isoCode+" Currency and in "+languageCurrency +" "," Piggy bank in " + isoCode+" Currency and in "+languageCurrency +" "," Piggy bank in "  + isoCode+" Currency and in "+languageCurrency + " ,verify screenshot", "fail", isoCode+"_"+languageCurrency);
								
							}
							
							//Verifying the currency format for Piggy bank bonus
							boolean isPiggyBankBonus=cfnlib.piggyBankBonus(currencyFormat);
							if(isPiggyBankBonus)
							{
								language.detailsAppendFolder("Verify currency when Piggy bank bonus game win occurs",
										"Piggy bank bonus game win should display in "+ currencySymbol +" currency symbol" ,
										"Piggy bank bonus game win displaying in "+ currencySymbol +" with currency ","Pass",isoCode+"_"+languageCurrency);
								System.out.println("Piggy Bank Bonus currency format is correct");
							}
							else
							{
								language.detailsAppendFolder("Verify currency when Piggy bank bonus game win occurs",
										"Piggy bank bonus game win should display in "+ currencySymbol +" currency symbol" ,
										"Piggy bank bonus game win displaying in "+ currencySymbol +" with currency ","fail",isoCode+"_"+languageCurrency);
								System.out.println("Piggy Bank Bonus currency format is incorrect");
							}
							Thread.sleep(4000);		
													
							language.detailsAppendFolder("Verify Smashing Language translations on 1 " + isoCode+"Currency and in "+languageCurrency +" "," Base scene in " + isoCode+"Currency and in "+languageCurrency +" "," Base scene in "  + isoCode+" Currency and in "+languageCurrency + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);				
							language.detailsAppendFolder("Verify Smashing Language translations on 2 " + isoCode+"Currency and in "+languageCurrency +" "," Base scene in " + isoCode+"Currency and in "+languageCurrency +" "," Base scene in "  + isoCode+" Currency and in "+languageCurrency + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);				
							Thread.sleep(2000);
								
							
							//final win in piggy bank/completion of piggybank
							if(cfnlib.elementWait("return "+cfnlib.XpathMap.get("IsPiggyBonusComplete"), true ))
							{	
								language.detailsAppendFolder("Verify final Currency for piggy bank bonus win" + isoCode+" Currency and in "+languageCurrency +" "," Currency for piggy bank bonus win " + isoCode+" Currency and in "+languageCurrency +" "," Currency for piggy bank bonus win "  + isoCode+" Currency and in "+languageCurrency + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);
								Thread.sleep(8000);
								language.detailsAppendFolder("Verify final Currency for piggy bank bonus win" + isoCode+" Currency and in "+languageCurrency +" "," Currency for piggy bank bonus win " + isoCode+" Currency and in "+languageCurrency +" "," Currency for piggy bank bonus win "  + isoCode+" Currency and in "+languageCurrency + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);
								Thread.sleep(5000);
								language.detailsAppendFolder("Verify final Currency for piggy bank bonus win" + isoCode+" Currency and in "+languageCurrency +" "," Currency for piggy bank bonus win " + isoCode+" Currency and in "+languageCurrency +" "," Currency for piggy bank bonus win "  + isoCode+" Currency and in "+languageCurrency + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);
								
							}
							else
							{
								language.detailsAppendFolder("Verify final Currency for piggy bank bonus win" + isoCode+" Currency and in "+languageCurrency +" "," Currency for piggy bank bonus win " + isoCode+" Currency and in "+languageCurrency +" "," Currency for piggy bank bonus win "  + isoCode+" Currency and in "+languageCurrency + " ,verify screenshot", "fail", isoCode+"_"+languageCurrency);				
								
							}
							
							
							Thread.sleep(5000);
							
							
							System.out.println("================== ROBBER BONUS ================ ");
							log.debug("================== ROBBER BONUS ================ ");
														
							//Checking 4 scenarios in robber bonus screen
							for(int i=1;i<5;i++)
							{
								// waiting for Bet Icon availability
								if(cfnlib.elementWait("return "+cfnlib.XpathMap.get("BetIconCurrState"), "active"))
								{
									System.out.println(i);
									
									cfnlib.spinclick();
									Thread.sleep(4000);
									
									language.detailsAppendFolder("Verify Language translations in Robber bonus in Scenario "+ i + isoCode+" Currency and in "+languageCurrency +" "," Robber bonus in " + isoCode+" Currency and in "+languageCurrency +" "," Robber bonus in "  + isoCode+" Currency and in "+languageCurrency + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);
									Thread.sleep(1000);
									
									//verifying currency format in robber bonus screen
									boolean bigwincurrency = cfnlib. verifyBigWinCurrencyFormat(currencyFormat,language,isoCode+"_"+languageCurrency);
									if (bigwincurrency) {
										language.detailsAppendFolder("Verify currency in bigwin occurs",
												"biWin  should display with currency",
												"bigwin  displaying with currency","Pass",isoCode+"_"+languageCurrency);
										System.out.println("Robber Bonus currency format is correct");

									} else {
										language.detailsAppendFolder("Verify currency in bigwin occurs",
												"bigwin should display with currency",
												"bigwin is not  displaying with currency", "Fail",isoCode+"_"+languageCurrency);
										System.out.println("Robber Bonus currency format is incorrect");
									}
									Thread.sleep(2000);
									language.detailsAppendFolder("Verify Language translations in Robber bonus in Scenario "+ i + isoCode+" Currency and in "+languageCurrency +" "," Robber bonus in " + isoCode+" Currency and in "+languageCurrency +" "," Robber bonus in "  + isoCode+" Currency and in "+languageCurrency + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);
									Thread.sleep(1000);
									
								}	
								else 
								{
									language.detailsAppendFolder("Verify Bet icon is visible", "Bet icon is visible", "Bet icon is not visible", "fail", isoCode+"_"+languageCurrency);
								}
								
							}
							Thread.sleep(3000);
							System.out.println("================== SAFE BONUS ================ ");
							log.debug("================== SAFE BONUS ================ ");
							
							
							for(int i=1;i<7;i++)
							{
								// waiting for Bet Icon availability
								if(cfnlib.elementWait("return "+cfnlib.XpathMap.get("BetIconCurrState"), "active"))
								{
									System.out.println(i);

									cfnlib.spinclick();
									Thread.sleep(6000);
									if(cfnlib.XpathMap.get("SafeBonusWinAnimation")!=null)
									{
										Thread.sleep(1000);
										language.detailsAppendFolder("Verify Safe bonus Animation " +i + isoCode+" Currency and in "+languageCurrency +" "," Base scene in " + isoCode+"Currency and in "+languageCurrency +" "," Base scene in "  + isoCode+" Currency and in "+languageCurrency + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);				
										Thread.sleep(4000);
										language.detailsAppendFolder("Verify Safe bonus Animation text " +i  + isoCode+" Currency and in "+languageCurrency +" "," Base scene in " + isoCode+"Currency and in "+languageCurrency +" "," Base scene in "  + isoCode+" Currency and in "+languageCurrency + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);													
										
										Thread.sleep(5000);
										boolean isSafeBonusCurrency = cfnlib.verifySafeBonusCurrency(currencyFormat);
										if (isSafeBonusCurrency) 
										{
											language.detailsAppendFolder("Verify currency when Piggy bank bonus game win occurs",
												"Safe bonus game win should display in "+ currencySymbol +" currency symbol" ,
												"Safe bonus game win displaying in "+ currencySymbol +" with currency ","Pass",isoCode+"_"+languageCurrency);
											System.out.println("Safe Bonus currency format is correct");
										}
										else
										{
											language.detailsAppendFolder("Verify currency when Safe bonus game win occurs",
												"Safe bonus game win should display in "+ currencySymbol +" currency symbol" ,
												"Safe bonus game win displaying in "+ currencySymbol +" with currency ","fail",isoCode+"_"+languageCurrency);
											System.out.println("Safe Bonus currency format is incorrect");
										}		
									}
									language.detailsAppendFolder("Verify Safe bonus Animation text for " +i  + isoCode+" Currency and in "+languageCurrency +" "," Base scene in " + isoCode+"Currency and in "+languageCurrency +" "," Base scene in "  + isoCode+" Currency and in "+languageCurrency + " ,verify screenshot", "Pass", isoCode+"_"+languageCurrency);													
									
								}
								else 
								{
									language.detailsAppendFolder("Verify Bet icon is visible", "Bet icon is visible", "Bet icon is not visible", "fail", isoCode+"_"+languageCurrency);
								}
								Thread.sleep(3000);
							}						
						}  else
						{
							language.detailsAppend("Verify game url launched", "Game url should open", "unable to load game url", "fail");
						} 

					}else
					{
						log.debug("Unable to copy test data file on the environment hence skipping execution");
						language.detailsAppend("", "", "Unable to copy test data on server,hence skipping futher execution", "fail");
					}
				}

			}    
		}//-------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error(e.getMessage(),e);
			cfnlib.evalException(e);
		}
		//-------------------Closing the connections---------------//
		finally
		{
			language.endReport();
			webdriver.close();
			webdriver.quit();
			proxy.abort();
			Thread.sleep(1000);	   
		}	
	}
}
