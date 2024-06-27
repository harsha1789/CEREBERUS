package Modules.Regression.TestScript;

import java.io.File;
import java.lang.Object;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.handler.SwitchToWindow;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.TestPropReader;
import com.zensar.automation.model.Currency;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile_CS;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import ch.qos.logback.core.net.SyslogOutputStream;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.offset.PointOption;
import net.lightbody.bmp.BrowserMobProxyServer;
/**
 * This Low value currency script verifies currency format and symbol in 
 
    Jackpot summary win 

 * Input currencies from test data file.
 * Configuration steps: set Default Bet value in corresponding game test data file.
 * 
 * @author HT67091
 *
 */
public class Mobile_Regression_LowValueCurrencyChecks_FreeGames2_CS{
	
	public static Logger log = Logger.getLogger(Mobile_Regression_LowValueCurrencyChecks_FreeGames2_CS.class.getName());
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


		ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();
		Mobile_HTML_Report currencyReport=new Mobile_HTML_Report(webdriver,deviceName,filePath,startTime,mstrTCName,mstrTCDesc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,status,gameName);
		
		MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, currencyReport, gameName);

		
		CommonUtil util = new CommonUtil();
		
		RestAPILibrary apiobj=new RestAPILibrary();
		
		
		List<String> copiedFiles=new ArrayList<>();
		cfnlib.setOsPlatform(scriptParameters.getOsPlatform());

		int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDMobile"));
	
		try {
			// -------Getting webdriver of proper test site-------//
			if (webdriver != null) 
			{

				WebDriverWait waitNew = new WebDriverWait(webdriver, 20);
				//./Desktop_Regression_CurrencySymbol.testdata
				String strFileName=TestPropReader.getInstance().getProperty("FreeGamesTestDataPath");
				File testDataFile=new File(strFileName);
				
				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				int languageCnt = excelpoolmanager.rowCount(testDataExcelPath, Constant.LANG_XL_SHEET_NAME);

				String strdefaultNoOfFreeGames=cfnlib.xpathMap.get("DefaultNoOfFreeGames");
				int defaultNoOfFreeGames=(int) Double.parseDouble(strdefaultNoOfFreeGames);
				String balanceTypeID=cfnlib.xpathMap.get("BalanceTypeID");
				Double dblBalanceTypeID=Double.parseDouble(balanceTypeID);
				//String balanceTypeID="10";
				String offerExpirationUtcDate=util.getCurrentTimeWithDateAndTime();
				boolean isGameLoaded = false;
               
				
				List<Map<String, String>> currencyList= util.readCurrList();// mapping

				String url = cfnlib.xpathMap.get("FreeGameApplicationURL");
				//String url ="https://mobile-app1-gtp73.installprogram.eu/mobileWebGames/game/mgs?displayName=Halloweenies&gameId=halloweenies&gameVersion=halloweenies_ComponentStore_1_4_8_398&moduleId=12531&clientId=40300&clientTypeId=40&languageCode=en&productId=5007&brand=islandparadise&loginType=InterimUPE&returnUrl=https://mobile-app1-gtp73.installprogram.eu/lobby/en/IslandParadise/games/&bankingUrl=https://mobile-app1-gtp73.installprogram.eu/lobby/en/IslandParadise&routerEndPoints=&currencyFormat=&isPracticePlay=False&username=Zen_psuupkz&password=test1234$&host=mobile";
				System.out.println("Application url is "+url);
				webdriver.navigate().to(url);
				
				for (Map<String, String> currencyMap:currencyList) 
				{
					try
					{			
						String isoCode = currencyMap.get(Constant.ISOCODE).trim();
						String currencyID = currencyMap.get(Constant.ID).trim();
						String currencyName = currencyMap.get(Constant.ISONAME).trim();
						String languageCurrency = currencyMap.get(Constant.LANGUAGECURRENCY).trim();
						String currencyFormat=currencyMap.get(Constant.DISPALYFORMAT).trim();
						String regExpression=currencyMap.get(Constant.REGEXPRESSION).trim();

						log.debug(this+" I am processing cuurency:  "+currencyName);
				
						currencyReport.detailsAppend("Verify currency name "+currencyName, " Verify currency name "+currencyName,"","");
								
						//Generating Random user
						userName=util.randomStringgenerator();
					//	userName="Zen_psuupkz";
						System.out.println("Username is "+userName);
										
						//Copy Test data to server
						//if(	util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles,currencyID,isoCode))
							//log.debug("Test data is copy in test Server for Username="+userName);
										
						boolean isFreeGameAssigned=true;
						// verifying free games	
						int maxFGRetCount=3;
						int count=0;
						//while(count<maxFGRetCount) 
							isFreeGameAssigned=cfnlib.assignFreeGames21(userName,offerExpirationUtcDate,mid,cid,languageCnt,defaultNoOfFreeGames);
							System.out.println("free games assigned: "+isFreeGameAssigned);
							Thread.sleep(4000);		
							
							if(isFreeGameAssigned) 
							{
								String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
								log.info("url = " +LaunchURl);
								System.out.println(LaunchURl);

								log.debug(" Currency Name :  "+currencyName);		
								isGameLoaded=cfnlib.loadGame(LaunchURl);	
								webdriver.navigate().to(LaunchURl);
								
										
								if (isGameLoaded) 
								{
								//	Thread.sleep(3000);
									/*if(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame"))||(Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad"))))
									{
										cfnlib.newFeature();
										Thread.sleep(1000);
										//cfnlib.funcFullScreen();
									}
									else
									{
										cfnlib.funcFullScreen();
										Thread.sleep(1000);
									//cfnlib.newFeature();

									}
									Thread.sleep(3000);
									*/
									
								//	cfnlib.waitForSpinButton2();
									
									


									if(framework.equalsIgnoreCase("CS"))
									{
										cfnlib.setNameSpace2();
									}
									
									waitNew.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='btnMenu']")));

									WebElement menuEle = webdriver.findElement(By.xpath("//*[@id='btnMenu']"));
									
									clickWithWebElementAndroid(webdriver, menuEle, 0);
									
									clickWithWebElementAndroid(webdriver, menuEle, 0);
									
									clickWithWebElementAndroid(webdriver, menuEle, 0);
									
									boolean s= isAutoplayAvailable2();
									
									if(s==true)
									{
										System.out.println("Play now button is available");
										WebElement playNw = webdriver.findElement(By.id("free-games-offer-play-now"));
										//boolean b= if()
										//clickWithWebElement(webdriver, spiEle, 0);
										//playNw.click();
										//TouchUtils
										clickWithWebElementAndroid(webdriver, playNw, 0);
										Thread.sleep(2000);
									}
									else
									{
										System.out.println("Resume button is available");
										WebElement playNw = webdriver.findElement(By.id("free-games-resume-resume"));
									
										clickWithWebElementAndroid(webdriver, playNw, 0);
										Thread.sleep(2000);
										System.out.println("Play now button is now available");
										System.out.println("Play now button is now available");
									}
									
									//cfnlib.clickPlayNow2();		
											
											
									
										

									WebElement spiEle = webdriver.findElement(By.id("btnSpin"));
									//clickWithWebElement(webdriver, spiEle, 0);
									spiEle.click();
									clickWithWebElementAndroid(webdriver, spiEle, 0);
									Thread.sleep(2000);
									
									
								
										
									cfnlib.clickPlayNow2();
									
									//To verify big win in base game
									 spiEle = webdriver.findElement(By.id("btnSpin"));
									//clickWithWebElement(webdriver, spiEle, 0);
									spiEle.click();
									clickWithWebElementAndroid(webdriver, spiEle, 0);
									Thread.sleep(2000);
									
									
									boolean bigwincurrency = cfnlib. verifyBigWinCurrencyFormatForLVC(regExpression,currencyReport,currencyName);
									if (bigwincurrency) {
										currencyReport.detailsAppendFolder("Verify currency when bigwin occurs in base game ",
												"biWin  should display with correct currency format and and currency symbol ",
												"bigwin  displaying with correct currency format and and currency symbol ","Pass",currencyName);

									} else {
										currencyReport.detailsAppendFolder("Verify currency when bigwin occurs in base game ",
												"bigwin should display with currency ",
												"bigwin is not  displaying with currency ", "Fail",currencyName);
									}
									
									Thread.sleep(10000);
									cfnlib.waitForSpinButtonstop();
									
									for (int k=0;k<(defaultNoOfFreeGames-1);k++)
									{
										System.out.println("count k = "+k);
										//cfnlib.spinclick();
										
										
										 spiEle = webdriver.findElement(By.id("btnSpin"));
										//clickWithWebElement(webdriver, spiEle, 0);
										spiEle.click();
										clickWithWebElementAndroid(webdriver, spiEle, 0);
										Thread.sleep(2000);
										
										
										
										cfnlib.waitForSpinButtonstop();
										Thread.sleep(5000);
										System.out.println("clicked on spin for count k = "+k);
									}
									Thread.sleep(3000);
									
									String amount =cfnlib.getWinAmtFromFreegames();
									if (amount!=null  &&  !amount.equals(""))
									{
											cfnlib. freeGameSummaryWinCurrFormatForLVC(regExpression,currencyReport,currencyName);	
											break;
									}
									else
									{
											System.out.println("There is no win during free games");
											log.debug("There is no win during free games");
									}
									
										
									
								}// game load
								else
								{
									log.debug("Unable to load the game");
								}
								
							}//else 
							{
								log.error("Skipping the execution as free games assignment failed");
							}
							count++;	
						//}//while loop
						System.out.println("Done for free games");
						
						}//try 
						catch ( Exception e) {
							cfnlib.evalException(e);

						}
						finally{
							try{
								webdriver.navigate().refresh();
								cfnlib.error_Handler(currencyReport);
							}catch(Exception e)
							{
								log.error("Exception occur in Finally Webdriver.refresh()..");
								log.error(e.getMessage(),e);
							}
						}
						
					}// for 
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

	


	public void tapOnCoordinates(AppiumDriver<WebElement> webdriver, final double x, final double y) {
		// new TouchAction(webdriver).tap((int)x, (int)y).perform();
		// int X=(int) Math.round(x); // it will round off the values
		// int Y=(int) Math.round(y);
		System.out.println("X cor - " + x + "," + " Y cor - " + y);
		System.out.println(x);
		System.out.println(y);
		TouchAction action = new TouchAction(webdriver);

		action.press(PointOption.point((int) x, (int) y)).release().perform();

		// action.tap(PointOption.point(X, Y)).perform();

	}



	public void clickWithWebElementAndroid(AppiumDriver<WebElement> webdriver,WebElement webViewElement,int Xcoordinate_AddValue)
	{
	String s = null;
	try
	{

	JavascriptExecutor javaScriptExe = (JavascriptExecutor) webdriver;
	int webViewWidth = ((Long) javaScriptExe.executeScript("return window.innerWidth || document.body.clientWidth")).intValue();
	int webViewHeight = ((Long) javaScriptExe.executeScript("return window.innerHeight || document.body.clientHeight")).intValue();
	Dimension elementSize = webViewElement.getSize();
	int webViewElementCoX = webViewElement.getLocation().getX() + (elementSize.width / 2);
	int webViewElementCoY = webViewElement.getLocation().getY() + (elementSize.height / 2);



	double connectedDeviceWidth = typeCasting(javaScriptExe.executeScript("return window.screen.width * window.devicePixelRatio"),webdriver);
	double connectedDeviceHeight = typeCasting(javaScriptExe.executeScript("return window.screen.height * window.devicePixelRatio"),webdriver);



	System.out.println("webViewHeight "+webViewHeight);
	System.out.println("connectedDeviceHeight "+connectedDeviceHeight);



	int webViewInnerHeight = ((Long) javaScriptExe
	.executeScript("return window.innerHeight || document.body.clientHeight")).intValue();
	System.out.println("webViewInnerHeight " + webViewInnerHeight);
	int webViewOuterHeight = ((Long) javaScriptExe
	.executeScript("return window.outerHeight || document.body.clientHeight")).intValue();
	System.out.println("webViewOuterHeight " + webViewOuterHeight);
	int webViewOffsetHeight = ((Long) javaScriptExe
	.executeScript("return window.offsetHeight || document.body.clientHeight")).intValue();
	System.out.println("webViewOffsetHeight " + webViewOffsetHeight);
	int webViewBottomHeight = webViewOffsetHeight - webViewInnerHeight;
	System.out.println("webViewBottomHeight " + webViewBottomHeight);

	AndroidDriver<WebElement> androidDriver =(AndroidDriver<WebElement>) webdriver;


	long statusBarHeight=(Long)androidDriver.getCapabilities().getCapability("statBarHeight");
	System.out.println("statusBarHeight "+statusBarHeight);

	double pixelRatio =0 ;
	Object pixelRatioObject = androidDriver.getCapabilities().getCapability("pixelRatio");

	if(pixelRatioObject instanceof Long ){
	pixelRatio=(Long)androidDriver.getCapabilities().getCapability("pixelRatio");

	}else if (pixelRatioObject instanceof Double ) {
	pixelRatio=(Double)androidDriver.getCapabilities().getCapability("pixelRatio");
	}


	/***************************************/
	double navigationBarHeight=0;
	double urlHeight=0.0;



	double webViewOffsetHeightInPixel= webViewOffsetHeight*pixelRatio;

	// to check the game in fullscreen
	double hightDifference = connectedDeviceHeight - webViewOffsetHeightInPixel;


	boolean isGameInFullScreenMode = (hightDifference < 2 && hightDifference > -2 );
	System.out.println(" isGameInFullScreenMode ::"+isGameInFullScreenMode);

	if(!isGameInFullScreenMode)
	{
	// Calculate the navigation Bar height and urlHeigt
	navigationBarHeight = connectedDeviceHeight - (webViewOffsetHeightInPixel + statusBarHeight);



	System.out.println("navigationBarHeight " + navigationBarHeight);
	urlHeight = ((webViewOffsetHeight - webViewHeight) * pixelRatio) + statusBarHeight;
	System.out.println(" urlHeight and statusBarHeight " + urlHeight);
	}

	/***************************************/


	String curContext=webdriver.getContext();
	webdriver.context("NATIVE_APP");








	/*if(xpathMap.get("serviceUrl").equalsIgnoreCase("yes"))
	urlHeight = (double) screenHeight * (0.12);
	else if(xpathMap.get("serviceUrl").equalsIgnoreCase("no"))
	urlHeight = 0.0;
	else
	urlHeight = (double) screenHeight * (0.22); */
	//urlHeight=0.0;
	double relativeScreenViewHeight = connectedDeviceHeight - urlHeight-navigationBarHeight;
	double nativeViewEleX = (double) (((double) webViewElementCoX / (double) webViewWidth) * connectedDeviceWidth);
	//double nativeViewEleY = (double) (((double) webViewElementCoY / (double) webViewHeight) );
	double nativeViewEleY = (double) (((double) webViewElementCoY / (double) webViewHeight)* relativeScreenViewHeight);
	tapOnCoordinates(webdriver,nativeViewEleX+Xcoordinate_AddValue, (nativeViewEleY + urlHeight + 1));
	webdriver.context(curContext);
	} catch (Exception e)
	{
	e.getStackTrace();
	}
	}
	
	
	
	 	public static String getImageAsEncodedString(String path) {
		String imgEncodeString = null;
		File refImgFile = new File(path);

		try {
			if (refImgFile.exists()) {
				imgEncodeString = Base64.getEncoder().encodeToString(Files.readAllBytes(refImgFile.toPath()));
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		return imgEncodeString;
	}

	public double typeCasting(Object object, AppiumDriver<WebElement> driver) {
		int value = 0;
		JavascriptExecutor js = ((JavascriptExecutor) driver);
		try {
			if (object instanceof Long)
				value = ((Long) object).intValue();
			else if (object instanceof Double)
				value = ((Double) object).intValue();

		} catch (Exception e) {
			e.getMessage();
		}
		return value;
	}
	
	public boolean isAutoplayAvailable2() throws InterruptedException 
	{
		Thread.sleep(2000);
		boolean isAutoplayAvailable = false;
		String autoplay ="Play Now";
		try
		{
		func_click(autoplay);
		isAutoplayAvailable = true;
		}
		catch (Exception e) 
		{
			log.error(e.getMessage(), e);
		}
		return isAutoplayAvailable;
		
	}


	private void func_click(String autoplay) {
		// TODO Auto-generated method stub
		
	}
	
		
	}




