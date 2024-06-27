package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.TestPropReader;
import com.zensar.automation.model.Currency;

import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 * This currency script verifies  Multiplier, Navigation in Base Scene, Bet Console, Free Spin Scene and Free Spin Summary Screen.
 * Input currencies from test data file.
 * 
 * @author Havish Jain
 *
 */
public class Mobile_Regression_CurrencySymbol {

	Logger log = Logger.getLogger(Mobile_Regression_CurrencySymbol.class.getName());
	public ScriptParameters scriptParameters;
	static List<Currency> currencyList =null;



	//-------------------Main script defination---------------------//
	public void script() throws Exception {
		String mstrTCName=scriptParameters.getMstrTCName();
		String mstrTCDesc=scriptParameters.getMstrTCDesc();
		String mstrModuleName=scriptParameters.getMstrModuleName();
		BrowserMobProxyServer proxy=scriptParameters.getProxy();
		String startTime=scriptParameters.getStartTime();
		String filePath=scriptParameters.getFilePath();
		AppiumDriver<WebElement> webdriver=scriptParameters.getAppiumWebdriver();
		String deviceName=scriptParameters.getDeviceName();
		String userName=scriptParameters.getUserName();
		String framework=scriptParameters.getFramework();
		String gameName=scriptParameters.getGameName();
		int checkedOutDeviceNum=scriptParameters.getCheckedOutDeviceNum();


		String status=null;
		int mintDetailCount=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		
		webdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Mobile_HTML_Report currencyReport=new Mobile_HTML_Report(webdriver,deviceName,filePath,startTime,mstrTCName,mstrTCDesc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,status,gameName);
		CommonUtil util=new CommonUtil();
		RestAPILibrary apiobj=new RestAPILibrary();
		boolean isGameLoaded = true;
		ArrayList<String> supportedCurrList=null;

		MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, currencyReport, gameName);
		
		
		List<String> copiedFiles=new ArrayList<>();
		cfnlib.setOsPlatform(scriptParameters.getOsPlatform());
		
		int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDMobile"));
		String strFileName=TestPropReader.getInstance().getProperty("MobileCurrencyTestDataPath");
		try{
			//-------------------Getting webdriver of proper test site---------------------//
			if(webdriver!=null)
			{		
				
				File testDataFile=new File(strFileName);
				
				// Read the Currency from the database

				String strStart = cfnlib.xpathMap.get("start");
				int start = (int) Double.parseDouble(strStart);

				String strEnd = cfnlib.xpathMap.get("end");
				int end = (int) Double.parseDouble(strEnd);
				boolean isProgressive=TestPropReader.getInstance().getProperty("IsProgressiveGame").equalsIgnoreCase("Yes");

				currencyList=util.getCurrencyListFromExel(start,end);
				int currencysize=currencyList.size();
				log.info("No of currency read from database="+currencysize);
			
				if(currencysize==0)
				{
					log.error("Error While reading the currencies from datbase");
				}

				//Below check supported currency check
				//Read the Supported currency list from xls ,next execute only for those currencies
				
				supportedCurrList = util.readSupportedCurrenciesList();
				boolean isSupportedCurConfigured=!supportedCurrList.isEmpty();
				
				String url = cfnlib.xpathMap.get("ApplicationURL");

				Thread.sleep(new Random().nextInt(20)*1000);

				for(Currency currency:currencyList){
					log.info("cuurency read"+currency.getIsoCode());

					if((!isSupportedCurConfigured)|| (isSupportedCurConfigured && supportedCurrList.contains(currency.getIsoCode().trim())))
					{
							if(currency.getStatus().equalsIgnoreCase("Open")){
								currency.setStatus("Close");

								log.debug(this+"I am processing cuurency"+currency.getIsoCode());
								try{

									String currencyID = currency.getCurrencyID();
									String currencyName = currency.getIsoName().trim();
									String multiplier = currency.getCurrencyMultiplier();
									String currencyFormat=currency.getCurrencyFormat();
									double multiplierValue = Double.parseDouble(multiplier);

				
									
									String isoCode = currency.getIsoCode().toString().trim();

									String gameNameNew=TestPropReader.getInstance().getProperty("GameName");
									//String capGameName = gameNameNew.replaceAll("[a-z0-9]", "").trim();
									
									userName=util.randomStringgenerator();
									
									/*Below condition is to validate currency users
									 
									if(System.getProperty("Token") !=null)
									{
										userName ="Zen" + capGameName+checkedOutDeviceNum+ "_" + currency.getIsoCode().trim();
									}
									else
									{
										userName="Zen" + capGameName + "_" + currency.getIsoCode().trim();
									}*/
									
									//Copy Test data to server
									if(	util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles,currencyID,isoCode))
										log.debug("Test data is copy in test Server for Username="+userName);
	
									
									
									String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
									log.info("url = " +LaunchURl);
									isGameLoaded=cfnlib.loadGame(LaunchURl);	


									currencyName=currencyID+"-"+currencyName.concat(isoCode);

									currencyReport.detailAppend("Verify game for "+currencyName+" currency","Game should work with "+currencyName+" currency", "", "", currencyName);
						
									if(isGameLoaded)
									{

										currencyReport.detailsAppendFolder("Verify Base Scene for "+currencyName+" currency","" + gameName + " Should display in "+currencyName+" currency","Game is displaying in "+currencyName+" currency", "Pass",currencyName);
										if(framework.equalsIgnoreCase("Force")){
											cfnlib.setNameSpace();
										}
										
										if(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame"))||(Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad"))))
										{
											cfnlib.newFeature();
											Thread.sleep(1000);
											cfnlib.funcFullScreen();
										}
										else
										{
											cfnlib.funcFullScreen();
											Thread.sleep(1000);
											cfnlib.newFeature();

										}
										cfnlib.waitForSpinButton();
										// set bet to default bet
										cfnlib.setDefaultBet();
										//Wait till bet reflect on console
										Thread.sleep(2000);

										//to get current bet in the game
										String bet = cfnlib.getCurrentBet();
										if(bet.contains(","))
											bet=bet.replace(",", ".");
										double currentBet = Double.parseDouble(bet);

										//Taking default bet of the game from excel to calculate multiplier
										String defaultBet = cfnlib.xpathMap.get("DefaultBet");
										double defaultBet1 = Double.parseDouble(defaultBet);
										log.debug("default game read from excel sheet ="+defaultBet1);


										//use below code if you don't want to take default bet from excel.
										//below code will take bet of $ currency as default bet.
										/*if(CurrencyID.equals("0") || CurrencyID.equals("0.0")){
									defaultBet1 = currentBet;
								}*/

										double currentMultiplier = currentBet/defaultBet1; 
										//to verify if multiplier in game is same as actual multiplier of currency
										if(multiplierValue==currentMultiplier){
											currencyReport.detailsAppendFolder("Verify that currency Mutiplier for currency is "+multiplierValue+" ", "Currency multiplier should be "+multiplierValue+" for this currency", "Currency multiplier is "+multiplierValue+" for this currency", "Pass",currencyName);
										}
										else{
											currencyReport.detailsAppendFolder("Verify that currency Mutiplier for currency is "+multiplierValue+" ", "Currency multiplier should be "+multiplierValue+" for this currency", "Currency multiplier is "+currentMultiplier+" for this currency", "Fail",currencyName);
										}


										boolean b = cfnlib.betCurrencySymbol(currencyFormat);
										if(b){
											currencyReport.detailsAppendFolder("Verify that currency format on the Bet Console",
													"Bet console should display the correct currency format ",
													"Bet console display the correct currency format", "Pass",
													currencyName);
										}
										else{
											currencyReport.detailsAppendFolder("Verify that currency format on the Bet Console",
													"Bet console should display the correct currency format ",
													"Bet console does not display the correct currency format", "Fail",
													currencyName);
										}



										String currencySymbol = cfnlib.getCurrencySymbol();
										log.debug(currencyName + "currency symbol is " +currencySymbol);

										boolean result=cfnlib.verifyCurrencyFormat(currencyFormat);

										if (result) {
											currencyReport.detailsAppendFolder("Verify that currency format in credit ",
													"Credit should display in correct currency format " ,
													"Crdit is displaying in correct currency format ", "Pass",
													currencyName);
										} else {
											currencyReport.detailsAppendFolder("Verify that currency format in credit ",
													"Credit should display in correct currency format " ,
													"Credit is not displaying in correct currency format ", "Fail",
													currencyName);
										}
										cfnlib.setMaxBet();
										if("yes".equalsIgnoreCase(cfnlib.xpathMap.get("IsRespinFeature")))
										{
											if(cfnlib.checkAvilability(cfnlib.xpathMap.get("MaxBetDailoagVisible")))
												cfnlib.clickAtButton("return  "+cfnlib.xpathMap.get("MaxBetDailoag"));

										}
										Thread.sleep(2000);


										//check and update the credits if bet value is more and refresh

										String credit = cfnlib.getCurrentCredits();
										credit=credit.replaceAll("Credits: ", "");
										credit=credit.replaceAll("[^0-9]", "");


										double currentCredit = Double.parseDouble(credit);
										log.debug("Current credit:"+currentCredit);
										//receiving the bet
										bet = cfnlib.getCurrentBet();
										bet=bet.replaceAll("[^0-9]", "");
										currentBet = Double.parseDouble(bet);
										log.debug("Current Bet:"+currentBet);

										if(currentBet>currentCredit){
											log.debug("Updating the balance");
											util.updateUserBalance(userName,(currentBet+1000)*100);
											webdriver.navigate().refresh();
											cfnlib.newFeature();
											cfnlib.setMaxBet();
										}


										// to verify currency format in bet console

										cfnlib.open_Bet();
										Thread.sleep(1000);
										//to verify currency symbol in bet settings page
										boolean b2 = cfnlib.betSettingCurrencySymbol(currencyFormat,currencyReport,currencyName);
										if(b2){
											currencyReport.detailsAppendFolder("Verify that currency on the Bet Settings Screen",
													"Total Bet  should  display " + currencySymbol + " currency symbol",
													"Total Bet displaying " + currencySymbol + " currency symbol",
													"Pass", currencyName);					}
										else{
											currencyReport.detailsAppendFolder("Verify that currency on the Bet Settings Screen",
													"Total Bet should display " + currencySymbol + " currency symbol",
													"Total Bet not display " + currencySymbol + " currency symbol", "Fail",
													currencyName);
										}
										cfnlib.closeTotalBet();
										Thread.sleep(1000);
										//Verify the paytable for currecy 
										if(!gameName.contains("Scratch") &&  cfnlib.checkAvilability(cfnlib.xpathMap.get("isMenuPaytableBtnVisible")))
										{
											cfnlib.verifyPaytableCurrency(currencyReport,currencyName);
										}

										if(isProgressive)
										{
											cfnlib.verifyJackPotBonuswithScreenShots(currencyReport,currencyName);

										}


										cfnlib.spinclick();
										//Check currency in free spin

										Thread.sleep(5000);
										if(!gameName.contains("Scratch")&& TestPropReader.getInstance().getProperty("IsFreeSpinAvailable").equalsIgnoreCase("yes"))
										{
											
											if(isProgressive)
											{
												//add sleep
												cfnlib.jackpotSceneWait();
												Thread.sleep(15000);
												currencyReport.detailsAppendFolder("Verify that the application should display jackpot scene with currency symbol","jackpot scene should display with currency symbol","jackpot scene displays with currency symbol","Pass", currencyName);
												cfnlib.clickAtButton("return "+cfnlib.xpathMap.get("ClickOnJackpotSpinButton"));
												//cfnlib.spinclick();
												// method to wait for jackpotscene summary screen
												cfnlib.elementWait("return "+cfnlib.xpathMap.get("isJackpotBonusContinueButtonVisible"), true);

												currencyReport.detailsAppendFolder("Verify that the application should display jackpot summary screen with currency symbol","jackpot scene should display summary screen with currency symbol","jackpot summary screen displays with currency symbol","Pass", currencyName);
												Thread.sleep(1000);
												cfnlib.clickAtButton("return "+cfnlib.xpathMap.get("ClickOnJackpotSummaryContinue"));
											}

											String FreeSpinEntryScreen = cfnlib.xpathMap.get("FreeSpinEntryScreen");
											String str = cfnlib.entryScreen_Wait(FreeSpinEntryScreen);
											if (str.equalsIgnoreCase("freeSpin")) {
												if("yes".equals(cfnlib.xpathMap.get("isFreeSpinSelectionAvailable")))
												{
													cfnlib.clickBonusSelection(1);

												}
												else
												{
													if( isProgressive)
													{
														cfnlib.FSSceneLoading();
														cfnlib.clickToContinue();
														cfnlib.FS_continue();
													}
													else{
														//cfnlib.clickToContinue();
														//Click on freespins into continue button
														if("yes".equals(cfnlib.xpathMap.get("isFreeSpinIntroContinueBtnAvailable")))
														{
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
											else{
												log.debug("Free Spin Entry screen is not present in Game");
											}


											cfnlib.FSSceneLoading();
											//wait until win occurs and capture screenshot.
											boolean b3 = cfnlib.waitforWinAmount(currencyFormat,currencyReport,currencyName);

											if (b3) {
												currencyReport.detailsAppendFolder("Verify free spin scene when win occurs",
														"Win in Free spin scene should display with currency",
														"Free Spin scene is displaying with currency", "Pass", currencyName);
											} else {
												if("no".equalsIgnoreCase(cfnlib.xpathMap.get("BigWinlayers")))
												{
													Thread.sleep(4000);
													currencyReport.detailsAppendFolder("There is No big win layers in the game",
															"NO big win layers in game",
															"NO big win layers in game", "pass",currencyName);
												
												}
												else{
												currencyReport.detailsAppendFolder("Verify free spin scene when win occurs",
														"Win in Free spin scene should display with currency",
														"Free Spin scene is not displaying with currency", "Fail", currencyName);
												}
											}
											cfnlib.waitSummaryScreen();
											boolean fscurrency = cfnlib. freeSpinWinSummaryCurrencyFormat(currencyFormat);

											if (fscurrency) {
												currencyReport.detailsAppendFolderOnlyScreeshot(currencyName);

											} else {
												currencyReport.detailsAppendFolderOnlyScreeshot(currencyName);
											}

											Thread.sleep(2000);
											cfnlib.funcFullScreen();
										}// end free spin block

										if(TestPropReader.getInstance().getProperty("IsFreeSpinAvailable").equalsIgnoreCase("no"))
										{

											boolean bigwincurrency = cfnlib. verifyBigWinCurrencyFormat(currencyFormat,currencyReport,currencyName);
											if (bigwincurrency) {
												currencyReport.detailsAppendFolder("Verify currency when bigwin occurs",
														"biWin  should display with currency",
														"bigwin  displaying with currency","Pass",currencyName);

											} else {
												currencyReport.detailsAppendFolder("Verify currency when bigwin occurs",
														"bigwin should display with currency",
														"bigwin is not  displaying with currency", "Fail",currencyName);
											}
										}
										//Check Currency in ReSpin Overlay

										if("yes".equalsIgnoreCase(cfnlib.xpathMap.get("IsRespinFeature")))	
										{
											cfnlib.waitForSpinButton();
											cfnlib.spinclick();
											cfnlib.waitForSpinButtonstop();
											Thread.sleep(5000);
											currencyReport.detailsAppendFolderOnlyScreeshot( currencyName);
											boolean respincurrency=cfnlib.reSpinOverlayCurrencyFormat(currencyFormat);
											currencyReport.detailsAppendFolderOnlyScreeshot(currencyName);

										}

									}//if block 
								}//try
								catch ( Exception e) {
									log.error("Exception while Processing Currency :: "+currency.getIsoCode());
									log.error(e.getStackTrace());
									cfnlib.evalException(e);

								}
								finally{
									try{
										webdriver.navigate().refresh();
										cfnlib.error_Handler(currencyReport);
										cfnlib.waitForSpinButton();
									}catch(Exception e)
									{
										log.error("Exception occur in Finally Webdriver.refresh()..");
										log.error(e.getMessage(),e);
									}
								}
							}else{
								log.info("I am skipping this currency as it is not open::"+currency.getIsoCode());
							}
						}else
						{
							log.debug("I am skipping this currency as it is not supported::"+currency.getIsoCode());
						}
					}//for
				}
		//-------------------Handling the exception---------------------//
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			cfnlib.evalException(e);
		}
		finally{
			currencyReport.endReport();
			//delete Test data
			if(!copiedFiles.isEmpty())
			{
				if(TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
						util.deleteBluemesaTestDataFiles(mid,cid,copiedFiles);
				else
						apiobj.deleteAxiomTestDataFiles(copiedFiles);
			}
			
			webdriver.close();
			webdriver.quit();
		}
	}
}