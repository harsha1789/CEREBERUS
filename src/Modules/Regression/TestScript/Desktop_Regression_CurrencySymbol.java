package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.TestPropReader;
import com.zensar.automation.model.Currency;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;
/**
 * This currency script verifies  Multiplier, Navigation in Base Scene, Bet Console, Free Spin Scene and Free Spin Summary Screen.
 * Input currencies from test data file.
 * Configation steps: set Default Bet value in corrosponding game test data file.
 * 
 * @author Havish Jain
 *
 */
public class Desktop_Regression_CurrencySymbol {


	public static Logger log = Logger.getLogger(Desktop_Regression_CurrencySymbol.class.getName());
	static List<Currency> currencyList =null;
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

		Desktop_HTML_Report currencyReport = new Desktop_HTML_Report(webdriver, browserName, filePath, startTime, mstrTCName,
				mstrTCDesc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings, mintStepNo, status,
				gameName);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, currencyReport, gameName);

		boolean isGameLoaded = true;
		ArrayList<String> supportedCurrList=null;
		String freeSpinEntryScreen = cfnlib.XpathMap.get("FreeSpinEntryScreen");
		CommonUtil util = new CommonUtil();
		RestAPILibrary apiobj=new RestAPILibrary();
		
		List<String> copiedFiles=new ArrayList<>();
		int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int  cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDDesktop"));
		
		try {
			// -------Getting webdriver of proper test site-------//
			if (webdriver != null) {

				//./Desktop_Regression_CurrencySymbol.testdata
				String strFileName=TestPropReader.getInstance().getProperty("CurrencyTestDataPath");
				File testDataFile=new File(strFileName);
			

				String strStart = cfnlib.XpathMap.get("start");
				int start = (int) Double.parseDouble(strStart);

				String strEnd = cfnlib.XpathMap.get("end");
				int end = (int) Double.parseDouble(strEnd);

				boolean isProgressive=TestPropReader.getInstance().getProperty("IsProgressiveGame").equalsIgnoreCase("Yes");
				
				//Read currency list from excel
				currencyList=util.getCurrencyListFromExel(start,end);
				int currencysize=currencyList.size();
				log.info("No of currencies read from database="+currencysize);

				if(currencysize==0)
				{
					log.error("Error While reading the currencies from datbase");
				}
				//Below check is supported currency check
				//Read the Supported currency list from xls ,next execute only for those currencies
				supportedCurrList = util.readSupportedCurrenciesList();
				boolean isSupportedCurConfigured=!supportedCurrList.isEmpty();

				String url = cfnlib.XpathMap.get("ApplicationURL");

				Thread.sleep(new Random().nextInt(30)*1000);

				for (Currency currency:currencyList ) 
				{
					log.info("currency reading"+currency.getIsoCode());

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

								String newgameName=TestPropReader.getInstance().getProperty("GameName");
								//Generating Random user
								userName=util.randomStringgenerator();

								String isoCode = currency.getIsoCode().trim();

								currencyName=currencyID+"-"+currencyName.concat(isoCode);

								//Copy Test data to server
								if(	util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles,currencyID,isoCode))
									log.debug("Test dat is copy in test Server for Username="+userName);


								String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
								log.info("url = " +LaunchURl);

								isGameLoaded=cfnlib.loadGame(LaunchURl);	

								currencyReport.detailsAppend("Verify currency name "+currencyName, "Verify currency name "+currencyName,"","");
								if (isGameLoaded) {

									if(framework.equalsIgnoreCase("Force")){
										cfnlib.setNameSpace();
									}
									cfnlib.newFeature();
									cfnlib.waitForSpinButton();

									// set bet to default bet
									cfnlib.setDefaultBet();
									//Wait till bet reflect on console
									Thread.sleep(2000);
									// to get current bet from the game
									String bet = cfnlib.getCurrentBet();
									if(bet.contains(","))
										bet=bet.replace(",", ".");
									double currentBet = Double.parseDouble(bet);
									log.debug("current bet="+currentBet);

									// Taking default bet of the game from excel to calculate multiplier
									String defaultBet = cfnlib.XpathMap.get("DefaultBet");
									double defaultBet1 = Double.parseDouble(defaultBet);
									log.debug("default game read from excel sheet ="+defaultBet1);
									// use below code if you don't want to take default bet from excel.
									// below code will take bet of $ currency as default
									// bet.

									/*if (currencyID.equals("0") || currencyID.equals("0.0")) {
									defaultBet1 = currentBet;
								}*/

									double currentMultiplier = currentBet / defaultBet1;
									log.debug("current Multiplier ="+currentMultiplier);
									// to verify if multiplier in game is same as actual
									// multiplier of currency
									if (multiplierValue == currentMultiplier) {
										currencyReport.detailsAppendFolder(
												"Verify that currency Multiplier for currency is " + multiplierValue + " ",
												"Currency multiplier should be " + multiplierValue + "  for this currency",
												"Currency multiplier is " + multiplierValue + " for this currency ", "Pass",
												currencyName);
									} else {
										currencyReport.detailsAppendFolder(
												"Verify that currency Mutiplier for currency is " + multiplierValue + " ",
												"Currency multiplier should be " + multiplierValue + " for this currency",
												"Currency multiplier is " + currentMultiplier + " for this currency", "Fail",
												currencyName);
									}
									boolean b = cfnlib.betCurrencySymbol(currencyFormat);
									
									cfnlib.threadSleep(5000);
									
									if (b) {
										currencyReport.detailsAppendFolder("Verify that currency format on the Bet Console",
												"Bet console should display the correct currency format ",
												"Bet console display the correct currency format", "Pass",
												currencyName);
									} else {
										currencyReport.detailsAppendFolder("Verify that currency format on the Bet Console",
												"Bet console should display the correct currency format ",
												"Bet console does not display the correct currency format", "Fail",
												currencyName);
									}


									String currencySymbol = cfnlib.getCurrencySymbol();
									log.info(currencyName + "currency symbol is " + currencySymbol);

									// verify currency format credit format
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
									Thread.sleep(2000);
									if("yes".equalsIgnoreCase(cfnlib.XpathMap.get("IsRespinFeature")))
									{
										if(cfnlib.checkAvilability(cfnlib.XpathMap.get("MaxBetDailoagVisible")))
											cfnlib.clickAtButton("return  "+cfnlib.XpathMap.get("MaxBetDailoag"));

									}

									//check and update the credits if bet value is more and refresh

									String credit = cfnlib.getCurrentCredits();
									credit=credit.toLowerCase().replace("credits: ", "");
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

									cfnlib.open_TotalBet();
									Thread.sleep(2000);
									// to verify currency symbol in bet settings page
									boolean b2 = cfnlib.betSettingCurrencySymbol(currencyFormat,currencyReport,currencyName);
									if (b2) {
										currencyReport.detailsAppendFolder("Verify that currency on the Bet Settings Screen",
												"Total Bet  should  display " + currencySymbol + " currency symbol ",
												"Total Bet displaying " + currencySymbol + " currency symbol ",
												"Pass", currencyName);
									} else {
										currencyReport.detailsAppendFolder("Verify that currency on the Bet Settings Screen",
												"Total Bet should display " + currencySymbol + " currency symbol",
												"Total Bet not display " + currencySymbol+ " currency symbol", "Fail",
												currencyName);
									}
									cfnlib.close_TotalBet();
									Thread.sleep(2000);
									if(isProgressive)
									{
										cfnlib.verifyJackPotBonuswithScreenShots(currencyReport,currencyName);

									}
									//Verify the paytable for currecy 
									if(!gameName.contains("Scratch") &&  cfnlib.checkAvilability(cfnlib.XpathMap.get("isMenuPaytableBtnVisible")))
										{
											cfnlib.verifyPaytableCurrency(currencyReport,currencyName);
										
										}
									cfnlib.spinclick();
									Thread.sleep(5000);
									//Check currency in free spin

									if(!gameName.contains("Scratch") && TestPropReader.getInstance().getProperty("IsFreeSpinAvailable").equalsIgnoreCase("yes"))
									{
										//Jackpot Bonus Language check for Progresssive Games
										if(TestPropReader.getInstance().getProperty("IsProgressiveGame").equalsIgnoreCase("Yes")) 
										{
											cfnlib.jackpotSceneWait();
											Thread.sleep(15000);
											currencyReport.detailsAppendFolder(
													"Verify that the application should display jackpot scene with currency symbol",
													"jackpot scene should display with currency symbol",
													"jackpot scene displays with currency symbol", "Pass", currencyName);
											
											cfnlib.clickAtButton("return "+cfnlib.XpathMap.get("ClickOnJackpotSpinButton"));
											//cfnlib.spinclick();
											// method to wait for jackpotscene summary screen
											cfnlib.elementWait("return "+cfnlib.XpathMap.get("isJackpotBonusContinueButtonVisible"), true);

											currencyReport.detailsAppendFolder(
													"Verify that the application should display jackpot summary screen with currency symbol",
													"jackpot scene should display summary screen with currency symbol",
													"jackpot summary screen displays with currency symbol", "Pass", currencyName);
											Thread.sleep(3000);
											cfnlib.clickAtButton("return "+cfnlib.XpathMap.get("ClickOnJackpotSummaryContinue"));
										}

										freeSpinEntryScreen = cfnlib.XpathMap.get("FreeSpinEntryScreen");
										//wait till if the game having free spin entry screen
										String str = cfnlib.entryScreen_Wait(freeSpinEntryScreen);
										if (str.equalsIgnoreCase("freeSpin")) {
											if("yes".equals(cfnlib.XpathMap.get("isFreeSpinSelectionAvailable")))
											{
												//cfnlib.newFeature();
												cfnlib.clickBonusSelection(1);
											}
											else
											{
												if( TestPropReader.getInstance().getProperty("IsProgressiveGame").equalsIgnoreCase("Yes"))
												{
													cfnlib.FSSceneLoading();
													cfnlib.clickToContinue();
													cfnlib.FS_continue();
												}
												else{

													//cfnlib.clickToContinue();
													//Click on freespins into continue button
													if("yes".equals(cfnlib.XpathMap.get("isFreeSpinIntroContinueBtnAvailable")))
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
										} else {
											log.debug("Free Spin Entry screen is not present in Game");
										}

										cfnlib.FSSceneLoading();

										// wait until win occurs and capture screenshot.
										boolean b3 = cfnlib. waitforWinAmount(currencyFormat,currencyReport,currencyName);

										if (b3) {
											currencyReport.detailsAppendFolder("Verify free spin scene when win occurs",
													"Win in Free spin scene should display with currency",
													"Win in Free Spin scene is displaying with currency", "Pass",currencyName);

										} else {
											if("No".equalsIgnoreCase(cfnlib.XpathMap.get("BigWinlayers")))
											{
												Thread.sleep(4000);
												currencyReport.detailsAppendFolder("There is No big win layers in the game",
														"NO big win layers in game",
														"NO big win layers in game", "pass",currencyName);

											}else{
												currencyReport.detailsAppendFolder("Verify free spin scene when win occurs",
														"Win in Free spin scene should display with currency",
														"Free Spin scene is not displaying with currency", "Fail",currencyName);
											}
										}
										cfnlib.waitSummaryScreen();
										Thread.sleep(1000);

										boolean fscurrency = cfnlib. freeSpinSummaryWinCurrFormat(currencyFormat);
										if (fscurrency) {
											currencyReport.detailsAppendFolderOnlyScreenShot(currencyName);
											log.debug("Free spin summary win currency validated...");
										} else {
											currencyReport.detailsAppendFolderOnlyScreenShot(currencyName);
											log.debug("Free spin summary win currency not validated...");
										}

									}
									if(TestPropReader.getInstance().getProperty("IsFreeSpinAvailable").equalsIgnoreCase("no"))
									{

										boolean bigwincurrency = cfnlib. verifyBigWinCurrencyFormat(currencyFormat,currencyReport,currencyName);
										if (bigwincurrency) {
											currencyReport.detailsAppendFolder("Verify currency in bigwin occurs",
													"biWin  should display with currency",
													"bigwin  displaying with currency","Pass",currencyName);

										} else {
											currencyReport.detailsAppendFolder("Verify currency in bigwin occurs",
													"bigwin should display with currency",
													"bigwin is not  displaying with currency", "Fail",currencyName);
										}
									}
									//Check Currency in ReSpin Overlay

									if("yes".equalsIgnoreCase(cfnlib.XpathMap.get("IsRespinFeature")))	
									{
										cfnlib.waitForSpinButton();

										Thread.sleep(1000);
										cfnlib.spinclick();
										cfnlib.waitForSpinButtonstop();
										Thread.sleep(5000);
										currencyReport.detailsAppendFolderOnlyScreenShot( currencyName);
										boolean respincurrency=cfnlib.reSpinOverlayCurrencyFormat(currencyFormat);
										currencyReport.detailsAppendFolderOnlyScreenShot(currencyName);
									}

								}//if block game loading

							}//try 
							catch ( Exception e) {
								log.error("Exception while Processing Currency :: "+currency.getIsoCode()+e.getMessage(),e);
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
							log.debug("I am skipping this currency as it is not open::"+currency.getIsoCode());
						}

					}else{
						log.debug("I am skipping this currency as it is not Configured in supported Currency list::"+currency.getIsoCode());
					}
				}// For 
			}
		}
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
			//proxy.abort();
			Thread.sleep(1000);
		}
	}
}