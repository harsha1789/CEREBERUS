package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.TestPropReader;
import com.zensar.automation.model.Currency;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import net.lightbody.bmp.BrowserMobProxyServer;
/**
 * This currency script verifies  HotInk bonus currency and Free Spin Summary Screen currency.
 * Input currencies from test data file.
 * Configation steps: set Default Bet value in corrosponding game test data file.
 * 
 * @author Priyanka Bethi
 *
 */
public class Mobile_Regression_BonusCurrencySymbol {
	Logger log = Logger.getLogger(Mobile_Regression_BonusCurrencySymbol.class.getName());
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
		String strFileName=TestPropReader.getInstance().getProperty("MobileFreeSpinsTestDataPath");
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
				
				String strbonusCount=cfnlib.xpathMap.get("NoOfBonusSelection");
				int bonusCount=(int) Double.parseDouble(strbonusCount);

				String selectBonus=cfnlib.xpathMap.get("SelectBonus");
				Map <String, String> paramMap=new HashMap<>();
				
				//Read currency list from excel
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
									String currencyFormat=currency.getCurrencyFormat();
									String isoCode = currency.getIsoCode().toString().trim();
									
									//Generating Random user
									userName=util.randomStringgenerator();
									
									currencyName=currencyID+"-"+currencyName.concat(isoCode);
									
									//Copy Test data to server
									if(	util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles,currencyID,isoCode))
										log.debug("Test data is copy in test Server for Username="+userName);
	
									
									
									String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
									log.info("url = " +LaunchURl);
									isGameLoaded=cfnlib.loadGame(LaunchURl);	

						
									if(isGameLoaded)
									{

										currencyReport.detailsAppendFolder("Verify Base Scene for "+currencyName+" currency ","" + gameName + " Should display in "+currencyName+" currency ","Game is displaying in "+currencyName+" currency ", "Pass",currencyName);
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
										Thread.sleep(2000);
										
										String currencySymbol = cfnlib.getCurrencySymbol();
										log.debug(currencyName + "currency symbol is " +currencySymbol);
										
										cfnlib.spinclick();
										Thread.sleep(1000);

										//Check Bonus game has been entered
										if(cfnlib.elementWait("return "+cfnlib.xpathMap.get("currentScene"), "PLAYING_ACTIVE_BONUS"))
										{
											Thread.sleep(2000);
										
											//To unlock the bonus depending upon the NoOfBonusSelection
											for(int iteamCnt=0;iteamCnt<bonusCount;iteamCnt++)
											{
												paramMap.put("param1",Integer.toString(iteamCnt));
												String newSelectBonusHook=cfnlib.replaceParamInHook(selectBonus,paramMap);
												String clickOnBonus="return "+newSelectBonusHook;
												cfnlib.clickAtButton(clickOnBonus);
												System.out.println("clicked on "+iteamCnt);
												Thread.sleep(4000);
											}
											currencyReport.detailsAppendFolderOnlyScreeshot(currencyName);
										}
										
										Thread.sleep(10000);

										//Verifying bonus game win currency format
										if(cfnlib.checkAvilability(cfnlib.xpathMap.get("IsBonusContinueBtnVisible")))
										{
											boolean bonuscurrency=cfnlib.bonusWinCurrFormat(currencyFormat);
											Thread.sleep(2000);
											if (bonuscurrency)  {
												currencyReport.detailsAppendFolder("Verify currency when bonus game win occurs",
														"Bonus game win should display in "+ currencySymbol +" currency symbol" ,
														"Bonus game win displaying in "+ currencySymbol +" with currency ","Pass",currencyName);
												} else {
												currencyReport.detailsAppendFolder("Verify currency when bonus game win occurs",
														"Bonus game win should display in "+ currencySymbol +" currency symbol",
														"Bonus game win not displaying in "+ currencySymbol +" with currency " ,"fail",currencyName);
											}
										}
										
										//Check freespin availability and verify freespins summary win currency format
										if(cfnlib.checkAvilability(cfnlib.xpathMap.get("IsFreeSpinsTrigger")))
										{
											//click on continue button after bonus game completion
											cfnlib.clickContinueInBonusGame();
											
											//wait for summary screen and verify currency format
											cfnlib.waitSummaryScreen();
											Thread.sleep(1000);

										}	
										else
										{
											cfnlib.clickContinueInBonusGame();
										}
												
										
									}//if block 
								}//try
								catch ( Exception e) {
									log.error("Exception while Processing Currency :: "+currency.getIsoCode());
									//log.error(e.getStackTrace());
									log.error(e.getMessage(),e);
									cfnlib.evalException(e);

								}
								finally{
									try{
										webdriver.navigate().refresh();
										cfnlib.error_Handler(currencyReport);
										//cfnlib.waitForSpinButton();
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


	
	