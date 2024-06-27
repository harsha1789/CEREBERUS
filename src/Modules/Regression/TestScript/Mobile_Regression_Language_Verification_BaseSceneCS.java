package Modules.Regression.TestScript;
import java.util.Map;


import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 * This Script traverse all the panels and take the screen shot of the all screens such as paytable, bet settings, auto play etc.
 * It reads the test data excel sheet for configured languages.
 * @author Premlata
 */

public class Mobile_Regression_Language_Verification_BaseSceneCS {
	
	public ScriptParameters scriptParameters;

	Logger log = Logger.getLogger(Mobile_Regression_Language_Verification_BaseSceneCS.class.getName());
	
	String videoPath=System.getProperty("user.dir")+"\\src\\Result\\Videos";

	
	public void script() throws Exception{
		

		String mstrTC_Name=scriptParameters.getMstrTCName();
		String mstrTC_Desc=scriptParameters.getMstrTCDesc();
		String mstrModuleName=scriptParameters.getMstrModuleName();
		BrowserMobProxyServer proxy=scriptParameters.getProxy();
		String startTime=scriptParameters.getStartTime();
		String filePath=scriptParameters.getFilePath();
		AppiumDriver<WebElement> webdriver=scriptParameters.getAppiumWebdriver();
		String DeviceName=scriptParameters.getDeviceName();
		String userName=scriptParameters.getUserName();
		String framework=scriptParameters.getFramework();
		//String deviceLockKey=scriptParameters.getDeviceLockKey();
		String gameName=scriptParameters.getGameName();
		//int checkedOutDeviceNum=scriptParameters.getCheckedOutDeviceNum();


		String Status=null;
		int mintDetailCount=0;
		//int mintSubStepNo=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		String urlNew=null;
		ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();
	

		Mobile_HTML_Report language=new Mobile_HTML_Report(webdriver,DeviceName,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,gameName);


		MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, language, gameName);
		cfnlib.setOsPlatform(scriptParameters.getOsPlatform());
		
		try{
			// Step 1
			if(webdriver!=null)
			{		
				Map<String, String> rowData2 = null;
				Map<String, String> rowData3 = null;
				
				CommonUtil util = new CommonUtil();
				userName=util.randomStringgenerator();

				String url=cfnlib.xpathMap.get("ApplicationURL");
				String launchURL = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
				//cfnlib.Func_navigate(url);
				
				String obj = cfnlib.funcNavigate(launchURL);
				System.out.println("launchURL "+launchURL);
				
				cfnlib.waitForSpinButton(); 
				
				//****************************************************************************************************//
				
			/*	
				MobileElement el4 = (MobileElement) webdriver.findElement(By.id(cfnlib.xpathMap.get("OneDesign_HamburgerID")));
				el4.click();
				
				WebDriverWait wait = new WebDriverWait(webdriver, 60);
				
				
				wait.until(ExpectedConditions.elementToBeClickable(By.id(cfnlib.xpathMap.get("OneDesign_PaytableID"))));
				
				//MobileElement element = (MobileElement) webdriver.findElement(By.id(cfnlib.xpathMap.get("OneDesign_PaytableID")));
				
				//element.click();
				
				//WebElement element1 = (WebElement) webdriver.findElement(By.id(cfnlib.xpathMap.get("OneDesign_PaytableID")));
				
				//element1.click();
				language.detailsAppend("Open Browser and Enter Lobby URL in address bar and click Enter", "Island Paradise Lobby should open", "Island Paradise lobby is Open", "Pass");
			
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"navigationPaytables\"]")));
				
				MobileElement el = (MobileElement) webdriver.findElement(By.xpath("//*[@id=\"navigationPaytables\"]"));
				if(el != null)
				{
					el.click();
					
				}
				
				cfnlib.clickWithWebElement(el, 0);*/
				
				//****************************************************************************************************//
				
				//cfnlib.nativeClickByID(cfnlib.xpathMap.get("OneDesign_PaytableID"));
					
				if(obj!=null)
				{
					language.detailsAppend("Open Browser and Enter Lobby URL in address bar and click Enter", "Island Paradise Lobby should open", "Island Paradise lobby is Open", "Pass");
				}
				
				else
				{
					language.detailsAppend("Open browser and Enter Game URL in address bar and click Enter", "Island Paradise lobby should be displayed", "Island paradise is not displayed", "Fail");
				}
				
					
				//cfnlib.funcFullScreen(language);

				cfnlib.funcFullScreen();
				cfnlib.spinclick();
				cfnlib.open_Autoplay();
				cfnlib.openTotalBet();

				
				//cfnlib.funcFullScreen();
				

				//webdriver.navigate().refresh();
				
				
			/*	if(Framework.equalsIgnoreCase("Force")){
					cfnlib.setNameSpace();
					}
				//CLick on spin button for win
				cfnlib.spinclickwithwin();*/

				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				int rowCount = excelpoolmanager.rowCount(testDataExcelPath, "LanguageCodes");
                for(int j=1;j<rowCount;j++){
                    //Step 2: To get the languages in MAP and load the language specific url
                    rowData2 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",  j);
                    String languageDescription = rowData2.get("Language").toString();
                    String languageCode = rowData2.get("Language Code").toString().trim();
                    System.out.println(languageCode +","+ languageDescription);
					
					System.out.println(languageCode +","+ languageDescription);
					
							
					cfnlib.splashScreen(language,languageCode);					
					
					language.detailsAppendFolder(" Verify that the application should display with Game Logo and game name in Base Scene", "Game logo and game name with base scene should display", "Game logo and game name with base scene displays", "Pass", languageCode);
					Thread.sleep(2000);
					
			
					
					//to remove hand gesture
					//cfnlib.func_FullScreen(language);
					//cfnlib.funcFullScreen();
					
					cfnlib.openTotalBet();
					
					
					cfnlib.openTotalBet();
				   	language.detailsAppendFolder("Verify that language on the Bet Settings Screen in portrait mode", "Bet Settings Screen should display in portrait mode", "Bet Settings Screen displays in " +languageDescription+ " language", "Pass", languageCode);
					
				   	cfnlib.clickOnAddressBar();
					cfnlib.threadSleep(1000);
					language.detailsAppendFolder("Total bet after enabling address bar", "Total bet with address bar should be enabled in" +languageDescription+"", "Total bet with address bar is enabled in" +languageDescription+",verify screenshot", "Pass", languageCode);
					
					cfnlib.clickOnAddressBar();
					cfnlib.threadSleep(1000);
				   	
					cfnlib.funcLandscape();
					language.detailsAppendFolder("Verify that language on the Bet Settings Screen in landscape mode", "Bet Settings Screen should display in landscape mode", "Bet Settings Screen displays in " +languageDescription+ " language", "Pass", languageCode);

					cfnlib.funcPortrait();
					cfnlib.clickOnAddressBar();
					
					cfnlib.closeTotalBet();
					log.debug("Done for bet");
					
					
					cfnlib.waitForSpinButton(); 
					Thread.sleep(2000);
					cfnlib.spinclick();
					Thread.sleep(500);
					language.detailsAppendFolder("Verify Game in Language " +languageCode+" ", " Basescene window should be displayed in " +languageDescription+"", "Basescene window should be displayed in " +languageDescription+"", "Basescene window is displayed in " +languageDescription+"", languageCode);
					cfnlib.clickOnAddressBar();
					language.detailsAppendFolder("Base scene after enabling address bar", "Base scene with address bar should be enabled in" +languageDescription+"", "Base scene with address bar is enabled in " +languageDescription+",verify screenshot", "Pass", languageCode);
					cfnlib.clickOnAddressBar();
					cfnlib.funcLandscape();
					language.detailsAppendFolder("Verify language on the Base scene Screen in landscape mode", "Base scene Screen should be display in landscape mode", "Base scene Screen should be displayed", "Pass", languageCode);
					cfnlib.funcPortrait();
					language.detailsAppendFolder("Verify language on the Base scene Screen in portrait mode", "Base scene Screen should be display in portrait mode", "Base scene Screen should be displayed", "Pass", languageCode);
					cfnlib.clickOnAddressBar();
					//Capture Screen shot for Autoplay Screen
					Thread.sleep(4000);
					boolean openAutoplay = cfnlib.open_Autoplay();
					if (openAutoplay){						
						language.detailsAppendFolder("Verify language on the Autoplay Screen in portrait mode", "Autoplay Screen should be display in portrait mode", "Autoplay Screen should be displayed", "Pass", languageCode);
						cfnlib.clickOnAddressBar();
						language.detailsAppendFolder("Autoplay after enabling address bar", "Autoplay with address bar should be enabled in " +languageDescription+"", "Autoplay with address bar is enabled in " +languageDescription+",verify screenshot", "Pass", languageCode);
						cfnlib.clickOnAddressBar();
					}
					else{
						language.detailsAppendFolder("Verify language on the Autoplay Screen in portrait mode", "Autoplay Screen should be display", "Autoplay Screen doesn't display", "Fail", languageCode);   
						cfnlib.clickOnAddressBar();
						language.detailsAppendFolder("Autoplay after enabling address bar", "Autoplay with address bar should be enabled in " +languageDescription+"", "Autoplay with address bar is enabled in " +languageDescription+",verify screenshot", "Pass", languageCode);
						cfnlib.clickOnAddressBar();
					}
					
					cfnlib.funcLandscape();
					if (openAutoplay){
						language.detailsAppendFolder("Verify language on the Autoplay Screen in landscape mode", "Autoplay Screen should be display in landscape mode", "Autoplay Screen should be displayed", "Pass", languageCode);
					}
					else{
						language.detailsAppendFolder("Verify language on the Autoplay Screen in landscape mode", "Autoplay Screen should be display", "Autoplay Screen doesn't display", "Fail", languageCode);   
					}
					
					cfnlib.funcPortrait();	
					if (openAutoplay){
						language.detailsAppendFolder("Verify language on the Autoplay Screen in Portrait mode", "Autoplay Screen should be display in Portrait mode", "Autoplay Screen should be displayed", "Pass", languageCode);
					}
					else{
						language.detailsAppendFolder("Verify language on the Autoplay Screen in Portrait mode", "Autoplay Screen should be display", "Autoplay Screen doesn't display", "Fail", languageCode);   
					}
					cfnlib.clickOnAddressBar();
					cfnlib.close_Autoplay();
					log.debug("Done for Autoplay");

									
					
					if(!gameName.contains("Scratch")){
						//cfnlib.funcLandscape();	
						//cfnlib.clickOnAddressBar();
						//cfnlib.paytableOpenScroll(language, languageCode);
									
					log.debug("Done for paytable");
					}		
				
					if (j + 1 != rowCount){
						rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j+1);
						languageDescription = rowData3.get("Language").toString();
						String languageCode2 = rowData3.get("Language Code").toString().trim();

						//String currentUrl = webdriver.getCurrentUrl();
						String currentUrl = cfnlib.xpathMap.get("ApplicationURL");
						System.out.println(currentUrl);
						if(currentUrl.contains("LanguageCode"))
							urlNew= currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+languageCode2);
						else if(currentUrl.contains("languagecode"))
							urlNew = currentUrl.replaceAll("languagecode="+languageCode, "languagecode="+languageCode2);
						System.out.println(urlNew);
						webdriver.navigate().to(urlNew);
						String error=cfnlib.xpathMap.get("Error");
						
						if(cfnlib.isElementPresent(error))
						{
							language.detailsAppendFolder("Verify the Language code is " +languageCode2+" ", " Application window should be displayed in " +languageDescription+"", "", "", languageCode2);
							language.detailsAppendFolder("Verify that any error is coming","error must not come","error is coming", "fail", languageCode2);
							if (j + 2 != rowCount){
								rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j+2);
								String languageCode3 = rowData3.get("Language Code").toString().trim();
								currentUrl = webdriver.getCurrentUrl();
								if(currentUrl.contains("LanguageCode"))
									urlNew= currentUrl.replaceAll("LanguageCode="+languageCode2, "LanguageCode="+languageCode3);
								else if(currentUrl.contains("languagecode"))
									urlNew = currentUrl.replaceAll("languagecode="+languageCode2, "languagecode="+languageCode3);
								webdriver.navigate().to(urlNew);
							}
							j++;
						}
					}

				
					//to remove hand gesture
					//cfnlib.Func_FullScreen();			
					/*if (j + 1 != rowCount2){
						rowData3 = excelpoolmanager.readExcelByRow(Constant.TESTDATA_EXCEL_PATH, "LanguageCodes", j+1);
						languageDescription = rowData3.get("Language").toString();
						String languageCode2 = rowData3.get("Language Code").toString().trim();

						//String currentUrl = webdriver.getCurrentUrl();
						String currentUrl = cfnlib.xpathMap.get("BaseApplicationURL");
						System.out.println(currentUrl);
						String url_new = currentUrl.replaceAll("LanguageCode=en", "LanguageCode="+languageCode2);
						System.out.println(url_new);
						webdriver.navigate().to(url_new);
						String error=cfnlib.xpathMap.get("Error");
						
						if(cfnlib.isElementPresent(error))
						{
							language.detailsAppendFolder("Verify the Language code is " +languageCode2+" ", " Application window should be displayed in " +languageDescription+"", "", "", languageCode2);
							language.detailsAppendFolder("Verify that any error is coming","error must not come","error is coming", "fail", languageCode2);
							if (j + 2 != rowCount2){
								rowData3 = excelpoolmanager.readExcelByRow(Constant.TESTDATA_EXCEL_PATH, "LanguageCodes", j+2);
								String languageCode3 = rowData3.get("Language Code").toString().trim();
								currentUrl = webdriver.getCurrentUrl();
								url_new = currentUrl.replaceAll("LanguageCode="+languageCode2, "LanguageCode="+languageCode3);
								webdriver.navigate().to(url_new);
							}
							j++;
						}*/
					}
				}
			
		}
			
		//-------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		finally
		{
			language.endReport();
			webdriver.close();
			webdriver.quit();
			//proxy.abort();
			Thread.sleep(3000);
		}	
	}
}