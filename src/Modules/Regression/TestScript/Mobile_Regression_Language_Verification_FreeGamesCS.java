package Modules.Regression.TestScript;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.DataBaseFunctions;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.offset.PointOption;
import net.lightbody.bmp.BrowserMobProxyServer;


/**
 * This script traverse and capture all the screen shots related to free games.
 * It reads the test data excel sheet for configured languages.
 * @author  HT67091
 *
 */
public class Mobile_Regression_Language_Verification_FreeGamesCS {
	public static Logger log = Logger.getLogger(Mobile_Regression_Language_Verification_FreeGames.class.getName());
	public ScriptParameters scriptParameters;
	
	public void script() throws Exception{

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


		String status=null;
		int mintDetailCount=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		String urlNew=null;
		ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();
		Mobile_HTML_Report report=new Mobile_HTML_Report(webdriver,deviceName,filePath,startTime,mstrTCName,mstrTCDesc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,status,gameName);

		log.info("Framework"+framework);
		MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, report, gameName);
		
		CommonUtil util = new CommonUtil();
		userName=util.createRandomUser();
		cfnlib.setOsPlatform(scriptParameters.getOsPlatform());
		int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int  cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDMobile"));
		List<String> copiedFiles=new ArrayList<>();
		RestAPILibrary apiobj=new RestAPILibrary();

		

		
		try{
			// Step 1
			if(webdriver!=null)
			{		
				Map<String, String> rowData2 = null;
				Map<String, String> rowData3 = null;

								
				
				boolean isFreeGameAssigned=false;
				String strdefaultNoOfFreeGames=cfnlib.xpathMap.get("DefaultNoOfFreeGames");
				int defaultNoOfFreeGames=(int) Double.parseDouble(strdefaultNoOfFreeGames);
				String balanceTypeID=cfnlib.xpathMap.get("BalanceTypeID");
				Double dblBalanceTypeID=Double.parseDouble(balanceTypeID);
				String offerExpirationUtcDate=util.getCurrentTimeWithDateAndTime();
				
				balanceTypeID=""+dblBalanceTypeID.intValue()+"";
				
				//Assign free games offers to user depending upon the languages configured 
			/*	if(TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
				{

					isFreeGameAssigned=cfnlib.addFreeGameToUserInBluemesa( userName,defaultNoOfFreeGames ,  offerExpirationUtcDate,  balanceTypeID,  mid, cid,4);
				}
				else
				{
					isFreeGameAssigned=cfnlib.addFreeGameToUserInAxiom(userName,defaultNoOfFreeGames,offerExpirationUtcDate,balanceTypeID,mid,cid,4);
				}
				
*/
				
				///////Remove after immortal rommances game execution
				//String LaunchURl=url+userName;
				//System.out.println("url = " +LaunchURl);
				//webdriver.context("NATIVE_APP");
				//webdriver.rotate(ScreenOrientation.LANDSCAPE);
				//webdriver.context("CHROMIUM");
				String url = cfnlib.xpathMap.get("ApplicationURL");
				//String launchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
				System.out.println(url);
				webdriver.navigate().to(url);
				log.debug("navigated to url ");
				
				
				
				WebElement element2 = webdriver.findElement(By.id("btnBetClickContainer"));
				Thread.sleep(5000);
				clickWithWebElement(webdriver, element2, 0);
				////////////
				
				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				
				if(framework.equalsIgnoreCase("CS")){
					cfnlib.setNameSpace();
					}
				int rowCount2 = excelpoolmanager.rowCount(testDataExcelPath, "LanguageCodes");
				for(int j=1;j<rowCount2;j++){
					//Step 2: To get the languages in MAP and load the language specific url
					rowData2 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",  j);
					String languageDescription = rowData2.get("Language").toString();
					String languageCode = rowData2.get("Language Code").toString().trim();
					System.out.println(languageCode +","+ languageDescription);

					report.detailsAppendFolder("Verify Free Games Scenes in "+languageDescription+" Language","Free Games Scenes should display in "+languageDescription+" language", "", "", languageCode);
				
					
					if(j!=1)
					cfnlib.freeGamesContinueExpiry();

					//Capture Free Game Entry Screen
					boolean s = cfnlib.freeGamesEntryScreen();
					if(s)
					{
						cfnlib.clickOnAddressBar();
						report.detailsAppendFolder("Check that free games entry screen is displaying in Portrait mode", "Free Games entry screen should display","Free Game entry screen is displaying", "Pass", languageCode);
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(4000);
						report.detailsAppendFolder("Freegames entry screen with Address bar", "Freegames entry screen with Address bar in " +languageDescription+"", "Freegames entry screen with Address bar in " +languageDescription+",verify screenshot", "Pass", languageCode);
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(2000);
					}
					else {
						report.detailsAppendFolder("Check that free games entry screen is displaying", "Free Games entry screen should display","Free Game entry screen is not displaying", "Fail", languageCode);
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(4000);
						report.detailsAppendFolder("Freegames entry screen with Address bar", "Freegames entry screen with Address bar in " +languageDescription+"", "Freegames entry screen with Address bar in " +languageDescription+",verify screenshot", "Pass", languageCode);
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(2000);
					}
					boolean b = cfnlib.freeGameEntryInfo();
					if(b)
					{
						report.detailsAppendFolder("Check that free games entry screen is displaying with free games details in Portrait mode", "Free Games entry screen should display with free games details","Free Game entry screen is displaying with free games details", "Pass", languageCode);
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(4000);
						report.detailsAppendFolder("Freegames entry screen displaying free games details with Address bar", "Freegames entry screen displaying free games with Address bar in " +languageDescription+"", "Freegames entry screen displaying free games with Address bar in" +languageDescription+",verify screenshot", "Pass", languageCode);
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(2000);
					}
					else {
						report.detailsAppendFolder("Check that free games entry screen is displaying with free games details", "Free Games entry screen should display with free games details","Free Game entry screen is not displaying with free games details", "Fail", languageCode);
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(4000);
						report.detailsAppendFolder("Freegames entry screen displaying free games details with Address bar", "Freegames entry screen displaying free games with Address bar in " +languageDescription+"", "Freegames entry screen displaying free games with Address bar in " +languageDescription+",verify screenshot", "Pass", languageCode);
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(2000);
					}
					boolean b1 = cfnlib.clickPlayNow();
					if(b1)
					{
						report.detailsAppendFolder("Check that Base Scene in free games is displaying in Portrait mode", "Base Scene in Free Games should display", "Base Scene in Free Games is displaying with free games details", "Pass", languageCode);
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(4000);
						report.detailsAppendFolder("Base Scene in free games is displaying  with Address bar", "Base Scene in free games is displaying  with Address bar in " +languageDescription+"", "Base Scene in free games is displaying with Address bar in " +languageDescription+",verify screenshot", "Pass", languageCode);
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(2000);
					}
					else {
						report.detailsAppendFolder("Check that Base Scene in free games is displaying", "Base Scene in Free Games should display", "Base Scene in Free Games is not displaying with free games details", "Fail", languageCode);
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(4000);
						report.detailsAppendFolder("Base Scene in free games is displaying  with Address bar ", "Base Scene in free games is displaying  with Address bar in " +languageDescription+"", "Base Scene in free games is displaying with Address bar in " +languageDescription+",verify screenshot", "Pass", languageCode);
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(2000);
					}
					//cfnlib.funcFullScreen();
					cfnlib.spinclick();
					Thread.sleep(10000);
					
					webdriver.navigate().refresh();
					Thread.sleep(7000);
					//cfnlib.newFeature();// Added while immortal romance game.
					Thread.sleep(7000);
					//cfnlib.funcFullScreen();
					//Free Games resume Screen
					String s1 = cfnlib.freeGamesResumescreen();
					if(s1!=null)
					{
						report.detailsAppendFolder("Check that free games resume screen is displaying in Portrait mode", "Free Games resume screen should display","Free Game resume screen is displaying", "Pass", languageCode);
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(4000);
						report.detailsAppendFolder("free games resume screen is displaying with Address bar", "free games resume screen is displaying with Address bar in " +languageDescription+"", "free games resume screen is displaying with Address bar in " +languageDescription+",verify screenshot", "Pass", languageCode);
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(2000);
					}
					else {
						report.detailsAppendFolder("Check that free games resume screen is displaying", "Free Games resume screen should display","Free Game resume screen is not displaying", "Fail", languageCode);
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(4000);
						report.detailsAppendFolder("free games resume screen is displaying with Address bar ", "free games resume screen is displaying with Address bar in " +languageDescription+"", "free games resume screen is displaying with Address bar in " +languageDescription+",verify screenshot", "Pass", languageCode);
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(2000);
					}
					boolean b2 = cfnlib.freeGameResumeInfo();
					if(b2)
					{
						report.detailsAppendFolder("Check that free games entry screen is displaying with free games details in Portrait mode", "Free Games entry screen should display with free games details","Free Game entry screen is displaying with free games details", "Pass", languageCode);
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(4000);
						report.detailsAppendFolder("free games entry screen is displaying with free games details with Address bar", "free games entry screen is displaying with free games details with Address bar in " +languageDescription+"", "free games entry screen is displaying with free games details with Address bar in " +languageDescription+",verify screenshot", "Pass", languageCode);
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(2000);
					}
					else {
						report.detailsAppendFolder("Check that free games entry screen is displaying with free games details", "Free Games entry screen should display with free games details","Free Game entry screen is not displaying with free games details", "Fail", languageCode);
						
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(4000);
						report.detailsAppendFolder("free games entry screen is displaying with free games details with Address bar", "free games entry screen is displaying with free games details with Address bar in " +languageDescription+"", "free games entry screen is displaying with free games details with Address bar in " +languageDescription+",verify screenshot", "Pass", languageCode);
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(2000);
					}
					boolean b3 = cfnlib.resumeScreenDiscardClick();
					if(b3)
					{
						report.detailsAppendFolder("Check that Discard Offer screen is displaying in portrait mode", "Discard Offer screen should display", "Discard Offer screen is displaying", "Pass", languageCode);
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(4000);
						report.detailsAppendFolder("Discard Offer screen is displaying with Address bar", "Discard Offer screen is displaying with Address bar in " +languageDescription+"", "Discard Offer screen is displaying with Address bar in " +languageDescription+",verify screenshot", "Pass", languageCode);
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(2000);
					}
					else {
						report.detailsAppendFolder("Check that Discard Offer screen is displaying", "Discard Offer screen should display", "Discard Offer screen is not displaying", "Fail", languageCode);
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(4000);
						report.detailsAppendFolder("Discard Offer screen is displaying with Address bar", "Discard Offer screen is displaying with Address bar in " +languageDescription+"", "Discard Offer screen is displaying with Address bar in " +languageDescription+",verify screenshot", "Pass", languageCode);
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(2000);
					}
					boolean b4 = cfnlib.confirmDiscardOffer();
					if(b4)
					{
						report.detailsAppendFolder("Check that Offer is discarded successfully and Free Games Summary Screen should display in portrait mode", "Offer should be discarded successfully and Free Games Summary Screen should display", "Offer is discarded and Free Games Summary Screen is displaying", "Pass", languageCode);
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(4000);
						report.detailsAppendFolder("Offer is discarded successfully and Free Games Summary Screen is displayed with Address bar", "Offer is discarded successfully and Free Games Summary Screen is displayed with Address bar in " +languageDescription+"", "Offer is discarded successfully and Free Games Summary Screen is displayed with Address bar in " +languageDescription+",verify screenshot", "Pass", languageCode);
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(2000);
					}
					else {
						report.detailsAppendFolder("Check that Offer is discarded successfully and Free Games Summary Screen should display", "Offer should be discarded successfully and Free Games Summary Screen should display", "Offer is not discarded and Free Games Summary Screen is not displaying", "Fail", languageCode);
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(4000);
						report.detailsAppendFolder("Offer is discarded successfully and Free Games Summary Screen is displayed with Address bar", "Offer is discarded successfully and Free Games Summary Screen is displayed with Address bar in " +languageDescription+"", "Offer is discarded successfully and Free Games Summary Screen is displayed with Address bar in " +languageDescription+",verify screenshot", "Pass", languageCode);
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(2000);
					}
					cfnlib.clickNextOffer();
					cfnlib.clickPlayNow();

					cfnlib.funcFullScreen();
					cfnlib.spinclick();
					Thread.sleep(15000);
					report.detailsAppendFolder("Check Base Scene in free games after Spin in portrait mode", "Win in Base Scene in Free Games should display if there is any win", "Base Scene in Free Games is displaying with win if win occurs", "Pass", languageCode);
					cfnlib.clickOnAddressBar();
					cfnlib.threadSleep(4000);
					report.detailsAppendFolder("Base Scene in free games after Spin with Address bar", "Base Scene in free games after Spin with Address bar in " +languageDescription+"", "Base Scene in free games after Spin with Address bar in " +languageDescription+",verify screenshot", "Pass", languageCode);
					cfnlib.clickOnAddressBar();
					cfnlib.threadSleep(2000);
					
					

					cfnlib.clickBaseSceneDiscard();

					boolean b6 = cfnlib.confirmDiscardOffer();
					if(b6)
					{
						report.detailsAppendFolder("Check that Offer is discarded successfully and Free Games Summary Screen should display in portrait mode", "Offer should be discarded successfully and Free Games Summary Screen should display", "Offer is discarded and Free Games Summary Screen is displaying", "Pass", languageCode);
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(4000);
						report.detailsAppendFolder("Offer is discarded successfully and Free Games Summary Screen is displayed with Address bar", "Offer is discarded successfully and Free Games Summary Screen is displayed with Address bar in " +languageDescription+"", "Offer is discarded successfully and Free Games Summary Screen is displayed with Address bar in " +languageDescription+",verify screenshot", "Pass", languageCode);
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(2000);
					}
					else {
						report.detailsAppendFolder("Check that Offer is discarded successfully and Free Games Summary Screen should display", "Offer should be discarded successfully and Free Games Summary Screen should display", "Offer is not discarded and Free Games Summary Screen is not displaying", "Fail", languageCode);
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(4000);
						report.detailsAppendFolder("Offer is discarded successfully and Free Games Summary Screen is displayed with Address bar", "Offer is discarded successfully and Free Games Summary Screen is displayed with Address bar in " +languageDescription+"", "Offer is discarded successfully and Free Games Summary Screen is displayed with Address bar in " +languageDescription+",verify screenshot", "Pass", languageCode);
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(2000);
					}
					webdriver.navigate().refresh();
					Thread.sleep(15000);
					cfnlib.newFeature();// Added while immortal romance game.
				//	cfnlib.funcFullScreen();

					boolean b5 = cfnlib.freeGamesExpriyScreen();
					if(b5)
					{
						report.detailsAppendFolder("Check that Free Game Session Expire Screen is displaying in portrait mode", "Free Game Session Expire Screen should display", "Free Game Session Expire Screen is displaying", "Pass", languageCode);
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(4000);
						report.detailsAppendFolder("Free Game Session Expire Screen is displaying with Address bar", "Free Game Session Expire Screen is displaying with Address bar in " +languageDescription+"", "Free Game Session Expire Screen is displaying with Address bar in " +languageDescription+",verify screenshot", "Pass", languageCode);
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(2000);

					}
					else {
						report.detailsAppendFolder("Check that Free Game Session Expire Screen is displaying", "Free Game Session Expire Screen should display", "Free Game Session Expire Screen is not displaying", "Fail", languageCode);
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(4000);
						report.detailsAppendFolder("Free Game Session Expire Screen is displaying with Address bar", "Free Game Session Expire Screen is displaying with Address bar in " +languageDescription+"", "Free Game Session Expire Screen is displaying with Address bar in " +languageDescription+",verify screenshot", "Pass", languageCode);
						cfnlib.clickOnAddressBar();
						cfnlib.threadSleep(2000);
					}
					if (j + 1 != rowCount2){
						rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j+1);
						languageDescription = rowData3.get("Language").toString();
						String languageCode2 = rowData3.get("Language Code").toString().trim();

						String currentUrl = webdriver.getCurrentUrl();

						if(currentUrl.contains("LanguageCode"))
							urlNew= currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+languageCode2);
						else if(currentUrl.contains("languagecode"))
							urlNew = currentUrl.replaceAll("languagecode="+languageCode, "languagecode="+languageCode2);
						webdriver.navigate().to(urlNew);
						cfnlib.newFeature();// Added while immortal romance game.
						String error=cfnlib.xpathMap.get("Error");
						if(cfnlib.isElementPresent(error))
						{
							report.detailsAppendFolder("Verify the Language code is " +languageCode2+" ", " Application window should be displayed in " +languageDescription+"", "", "", languageCode2);
							report.detailsAppendFolder("Verify that any error is coming","error must not come","error is coming", "Fail", languageCode2);
							if (j + 2 != rowCount2){
								rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",  j+2);
								String languageCode3 = rowData3.get("Language Code").toString().trim();

								currentUrl = webdriver.getCurrentUrl();
								if(currentUrl.contains("LanguageCode"))
									urlNew= currentUrl.replaceAll("LanguageCode="+languageCode2, "LanguageCode="+languageCode3);
								else if(currentUrl.contains("languagecode"))
									urlNew = currentUrl.replaceAll("languagecode="+languageCode2, "languagecode="+languageCode3);
								webdriver.navigate().to(urlNew);
								cfnlib.newFeature();// Added while immortal romance game.
							}
							j++;
						}
					
					}
				}
				
				
				String strFileName="./"+gameName+"/TestData/"+this.getClass().getSimpleName()+".testdata";
				File testDataFile=new File(strFileName);
				userName=util.createRandomUser();
				userName=util.randomStringgenerator();
				if(	util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles))
				{
				
				}
			}
		}
		//-------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error(e.getMessage(),e);
			cfnlib.evalException(e);
		}
		finally
		{
			report.endReport();
			webdriver.close();
			webdriver.quit();
			//proxy.abort();
			Thread.sleep(3000);
		}	
	}
	
	

	public void clickWithWebElement(AppiumDriver<WebElement> webdriver, WebElement webViewElement,
			int Xcoordinate_AddValue) {
		String s = null;
		try {
			double urlHeight = 0.0;
			JavascriptExecutor javaScriptExe = (JavascriptExecutor) webdriver;
			int webViewInnerWidth = ((Long) javaScriptExe
					.executeScript("return window.innerWidth || document.body.clientWidth")).intValue();
			System.out.println("webViewInnerWidth " + webViewInnerWidth);
			int webViewOuterWidth = ((Long) javaScriptExe
					.executeScript("return window.outerWidth || document.body.clientWidth")).intValue();
			System.out.println("webViewInnerWidth " + webViewInnerWidth);
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
			Dimension elementSize = webViewElement.getSize();
			System.out.println("elementSize " + elementSize);
			int webViewElementCoX = webViewElement.getLocation().getX() + (elementSize.width / 2);
			System.out.println("webViewElementCoX " + webViewElementCoX);
			int webViewElementCoY = webViewElement.getLocation().getY() + (elementSize.height / 2);
			System.out.println("webViewElementCoY " + webViewElementCoY); // double connectedDeviceWidth =
																			// typeCasting(javaScriptExe.executeScript("return
																			// window.screen.width *
																			// window.devicePixelRatio"), webdriver);
			// double connectedDeviceHeight =
			// typeCasting(javaScriptExe.executeScript("return window.screen.height *
			// window.devicePixelRatio"), webdriver);
			// //System.out.println("connectedDeviceWidth "+connectedDeviceWidth);
			// System.out.println("connectedDeviceHeight "+connectedDeviceHeight);
			String curContext = webdriver.getContext();
			webdriver.context("NATIVE_APP");
			urlHeight = (double) webViewOuterHeight - (double) webViewInnerHeight;
			System.out.println("url height " + urlHeight);
			// urlHeight=0.0;
			double relativeScreenViewHeight = webViewOuterHeight - urlHeight;
			System.out.println("relativeScreenViewHeight " + relativeScreenViewHeight);
			double nativeViewEleX = (double) (((double) webViewElementCoX / (double) webViewInnerWidth)
					* webViewOuterWidth);
			System.out.println("nativeViewEleX " + nativeViewEleX);
			double nativeViewEleY = (double) (((double) webViewElementCoY / (double) webViewOuterHeight)
					* relativeScreenViewHeight);
			System.out.println("nativeViewEleY " + nativeViewEleY);
			tapOnCoordinates(webdriver, (nativeViewEleX + Xcoordinate_AddValue), ((nativeViewEleY + urlHeight + 1)));
			webdriver.context(curContext);
		} catch (Exception e) {
			e.getStackTrace();
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

}



