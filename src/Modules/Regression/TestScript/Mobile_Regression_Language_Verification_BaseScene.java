package Modules.Regression.TestScript;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 * This Script traverse all the panels and take the screen shot of the all screens such as paytable, bet settings, auto play etc.
 * It reads the test data excel sheet for configured languages.
 * @author Premlata
 */

public class Mobile_Regression_Language_Verification_BaseScene {
	Logger log = Logger.getLogger(Mobile_Regression_Language_Verification_BaseScene.class.getName());

	public ScriptParameters scriptParameters;

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
		String osPlatform=scriptParameters.getOsPlatform();
		String osVersion=scriptParameters.getOsVersion();


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

		log.info("Framework"+framework);
		MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, language, gameName);
		
		cfnlib.setOsPlatform(osPlatform);
		cfnlib.setOsVersion(osVersion);
		try{
			// Step 1
			if(webdriver!=null)
			{		
				Map<String, String> rowData2 = null;
				Map<String, String> rowData3 = null;
				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				String url = cfnlib.xpathMap.get("ApplicationURL");
				String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
				System.out.println("url = " +LaunchURl);
				if("LANDSCAPE".equalsIgnoreCase(cfnlib.xpathMap.get("Orientation")))
				{
					String context=webdriver.getContext();
				webdriver.context("NATIVE_APP");
				webdriver.rotate(ScreenOrientation.LANDSCAPE);
				webdriver.context(context);
				}
				cfnlib.loadGame(LaunchURl);
				/*webdriver.navigate().to(LaunchURl);
				log.debug("navigated to url ");
				Thread.sleep(10000)*/;

				//wait to load assets
				Thread.sleep(15000);
				if(framework.equalsIgnoreCase("Force")){
					cfnlib.setNameSpace();
				}

				int rowCount2 = excelpoolmanager.rowCount(testDataExcelPath, "LanguageCodes");
				for(int j=1;j<rowCount2;j++){
					//Step 2: To get the languages in MAP and load the language specific url
					rowData2 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",  j);
					String languageDescription = rowData2.get("Language").toString();
					String languageCode = rowData2.get("Language Code").toString().trim();
					language.detailsAppend("Verify the Language code is " +languageCode+" ", " Application window should be displayed in " +languageDescription+"", "", "");

					cfnlib.splashScreen(language,languageCode);					

					language.detailsAppendFolder(" Verify that the application should display with Game Logo and game name in Base Scene", "Game logo and game name with base scene should display", "Game logo and game name with base scene displays", "Pass", languageCode);
					Thread.sleep(1000);


					if(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame"))||(Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad"))))
					{
						//cfnlib.funcFullScreen();
						Thread.sleep(1000);
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
					log.debug("handled new feature screen after spash screen");

					language.detailsAppendFolder("Verify Game in Language " +languageCode+" ", " Basescene window should be displayed in " +languageDescription+"", "Basescene window should be displayed in " +languageDescription+"", "Pass", languageCode);

					if(framework.equalsIgnoreCase("CS")){
						cfnlib.setMaxBetPanel();
						cfnlib.waitForSpinButton(); 
						cfnlib.spinclick();
						webdriver.navigate().refresh();
						cfnlib.waitForSpinButton(); 
						cfnlib.funcFullScreen();
					}

					/*//Not required for force
					if(!Framework.equalsIgnoreCase("Force")){
						cfnlib.verifyStopLanguage(language, languageCode);
					}

					// Capture screenshot for Win History Component
					boolean flag=cfnlib.winHistoryClick();
					if(flag)
					{

						language.details_append_folder("Verify language on the Win History Screen", "Win History Screen should be display", "Win History Screen should be displayed in " +languageDescription+ " language", "Pass", languageCode);
					}
					else
					{
						 language.details_append_folder("Verify language on the Win History Screen", "Win History Screen should be display", "Win History Screen doesn't display", "Fail", languageCode);
					}
					cfnlib.winHistoryClose();
					 */

					//Capture Screen shot for Bet Screen
					cfnlib.openTotalBet();
					language.detailsAppendFolder("Verify that language on the Bet Settings Screen", "Bet Settings Screen should display", "Bet Settings Screen displays in " +languageDescription+ " language", "Pass", languageCode);
					if(framework.equalsIgnoreCase("CS"))
					{

						cfnlib.funcLandscape();
						language.detailsAppendFolder("Verify that language on the Bet Settings Screen in landscape mode", "Bet Settings Screen should display in landscape mode", "Bet Settings Screen displays in " +languageDescription+ " language", "Pass", languageCode);

						cfnlib.funcPortrait();
					}
					cfnlib.closeTotalBet();
					log.debug("Done for bet");

					Thread.sleep(2000);
					boolean openAutoplay = cfnlib.open_Autoplay();
					if (openAutoplay){
						language.detailsAppendFolder("Verify language on the Autoplay Screen", "Autoplay Screen should be display", "Autoplay Screen should be displayed", "Pass", languageCode);
					}
					else{
						language.detailsAppendFolder("Verify language on the Autoplay Screen", "Autoplay Screen should be display", "Autoplay Screen doesn't display", "Fail", languageCode);   
					}
					if(framework.equalsIgnoreCase("CS"))
					{
						cfnlib.funcLandscape();
						if (openAutoplay){
							language.detailsAppendFolder("Verify language on the Autoplay Screen in landscape mode", "Autoplay Screen should be display in landscape mode", "Autoplay Screen should be displayed", "Pass", languageCode);
						}
						else{
							language.detailsAppendFolder("Verify language on the Autoplay Screen in landscape mode", "Autoplay Screen should be display", "Autoplay Screen doesn't display", "Fail", languageCode);   
						}

						cfnlib.funcPortrait();	
					}
					cfnlib.close_Autoplay();
					log.debug("Done for Autoplay");

					Thread.sleep(2000);
					if(!framework.equalsIgnoreCase("CS_Renovate")){
						boolean menuOpen = cfnlib.menuOpen();
						if(menuOpen){
							language.detailsAppendFolder("Verify that Language of menu link is " + languageDescription + " ", "Language of Menu Links should be " +languageDescription+ "", "Menu Links are displaying in " +languageDescription+ " language", "pass", languageCode);
						}
						else {
							language.detailsAppendFolder("Verify that Language" + languageDescription + "should be display properly on menu links", "Language inside Menu Links should be in " +languageDescription+ " language", "Language inside Menu Links is displaying", "fail", languageCode);
						}
						cfnlib.menuClose();
						log.debug("Done for Menu");
					}
					
					Thread.sleep(2000);
					//Need to uncomment for OneDesign Console
					if(!framework.equalsIgnoreCase("CS")&&cfnlib.checkAvilability(cfnlib.xpathMap.get("isMenuSettingsBtnVisible"))){
						boolean openSetting= cfnlib.settingsOpen();
						if(openSetting){
							language.detailsAppendFolder("Verify that Language on settings screen is " + languageDescription + " ", "Language in Settigns Screen should be " +languageDescription+ " ", "Language inside Settings screens is " +languageDescription+ " ", "pass", languageCode);
						}
						else {
							language.detailsAppendFolder("Verify that Language back on menu is " + languageDescription +"should be display properly on menu links", "Language inside Menu Links should be in " +languageDescription+ " language", "Language inside Menu Links is displaying", "fail", languageCode);
						}
						cfnlib.settingsBack();
						log.debug("Done for Setting");
					}


					Thread.sleep(2000);
					if(!gameName.contains("Scratch") && cfnlib.checkAvilability(cfnlib.xpathMap.get("isMenuPaytableBtnVisible"))){
						//Open payatable and capture screen shots
						cfnlib.capturePaytableScreenshot(language, languageCode);
						if(!framework.equalsIgnoreCase("CS"))
							cfnlib.paytableClose();
						log.debug("Done for paytable");
					}
					if(TestPropReader.getInstance().getProperty("IsProgressiveGame").equalsIgnoreCase("Yes"))
					{
						if(TestPropReader.getInstance().getProperty("IsMotivators&Messages").equalsIgnoreCase("Yes"))
						{
							String strNoOfMsgWithReelsSpinning = cfnlib.xpathMap.get("noOfMsgWithReelsSpinning");
							int noOfMsgWithReelsSpinning = (int) Double.parseDouble(strNoOfMsgWithReelsSpinning); 

							String strNoOfMsgWithReelsResolved = cfnlib.xpathMap.get("noOfMsgWithReelsResolved");
							int noOfMsgWithReelsResolved = (int) Double.parseDouble(strNoOfMsgWithReelsResolved); 



							String strNOfMotivators = cfnlib.xpathMap.get("noOfMotivators");
							int noOfMotivators = (int) Double.parseDouble(strNOfMotivators); 

							for (int spinCount=0; spinCount < noOfMsgWithReelsResolved; spinCount++)
							{
								cfnlib.spinclick();
								if(spinCount< noOfMsgWithReelsSpinning)
								{
									Thread.sleep(1000);
									language.detailsAppendFolder("Verify Mesaage display below reels in language  " + languageDescription + " ", "Mesaage should be in  " +languageDescription+ " ", "Message is in  " +languageDescription+ " ", "pass", languageCode);
								}
								cfnlib.waitForSpinButtonstop();
								Thread.sleep(1000);
								language.detailsAppendFolder("Verify Mesaage display below reels in language  " + languageDescription + " ", "Mesaage should be in  " +languageDescription+ " ", "Message is in  " +languageDescription+ " ", "pass", languageCode);

							}
						}

						cfnlib.verifyJackPotBonuswithScreenShots(language, languageCode);
					}
					
					//Incase of Respin games it takes the bet dialog screenshot on bet change

					if("yes".equalsIgnoreCase(cfnlib.xpathMap.get("IsRespinFeature")))	
					{
						cfnlib.waitForSpinButton();

						Thread.sleep(1000);
						cfnlib.spinclick();
						cfnlib.waitForSpinButtonstop();
						Thread.sleep(5000);
						cfnlib.openTotalBet();
						cfnlib.setMaxBet();
						cfnlib.threadSleep(2000);
						cfnlib.closeTotalBet();
						language.detailsAppendFolderOnlyScreeshot(languageCode);
						cfnlib.clickOnReSpinOverlay();
						log.debug("Done for bet dialog");

					}
					
					
					
				

					
					



					// To open and capture Story in Game (Applicable for the game immortal romances)
					if(cfnlib.checkAvilability(cfnlib.xpathMap.get("isPaytableStoryExists"))){
						cfnlib.Verifystoryoptioninpaytable(language, languageCode);
						cfnlib.paytableClose();
						log.debug("Done for story in paytable");
					}

					if (j + 1 != rowCount2){
						rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j+1);
						languageDescription = rowData3.get("Language").trim();
						String languageCode2 = rowData3.get("Language Code").trim();

						String currentUrl = webdriver.getCurrentUrl();
						if(currentUrl.contains("LanguageCode"))
							urlNew= currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+languageCode2);
						else if(currentUrl.contains("languagecode"))
							urlNew = currentUrl.replaceAll("languagecode="+languageCode, "languagecode="+languageCode2);
						else if(currentUrl.contains("languageCode"))
							urlNew = currentUrl.replaceAll("languageCode="+languageCode, "languageCode="+languageCode2);

						
						log.info("Privious Language Code in Url= "+languageCode+"\nNext Language code= "+languageCode2+"\nNew Url after replacing language code:"+urlNew);
						cfnlib.loadGame(urlNew);
						String error=cfnlib.xpathMap.get("Error");

						if(cfnlib.isElementPresent(error))
						{
							language.detailsAppendFolder("Verify the Language code is " +languageCode2+" ", " Application window should be displayed in " +languageDescription+"", "", "", languageCode2);
							language.detailsAppendFolder("Verify that any error is coming","General error should not display","General Error is Diplay", "fail", languageCode2);

							if (j + 2 != rowCount2){
								rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j+2);
								String languageCode3 = rowData3.get("Language Code").toString().trim();
								currentUrl = webdriver.getCurrentUrl();
								if(currentUrl.contains("LanguageCode"))
									urlNew= currentUrl.replaceAll("LanguageCode="+languageCode2, "LanguageCode="+languageCode3);
								else if(currentUrl.contains("languagecode"))
									urlNew = currentUrl.replaceAll("languagecode="+languageCode2, "languagecode="+languageCode3);
								else if(currentUrl.contains("languageCode"))
									urlNew = currentUrl.replaceAll("languageCode="+languageCode2, "languageCode="+languageCode3);

								cfnlib.loadGame(urlNew);
							}

							j++;
						}
					}
				}
			}





			/*Map<String, String> rowData2 = null;
			Map<String, String> rowData3 = null;

			String url=cfnlib.xpathMap.get("BaseApplicationURL");
			String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");

			url=url+userName;
			//cfnlib.Func_navigate(url);

			String obj = cfnlib.funcNavigate(url);
			if(obj!=null)
			{
				language.details_append("Open Browser and Enter Lobby URL in address bar and click Enter", "Island Paradise Lobby should open", "Island Paradise lobby is Open", "Pass");
			}

			else
			{
				language.details_append("Open browser and Enter Game URL in address bar and click Enter", "Island Paradise lobby should be displayed", "Island paradise is not displayed", "Fail");
			}

			//cfnlib.funcFullScreen();
			new WebDriverWait(webdriver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cfnlib.xpathMap.get("clock"))));
			Thread.sleep(5000);
			cfnlib.funvFullScreenOff();
			Thread.sleep(2000);
			cfnlib.setMaxBetPanel(); 




			int rowCount2 = excelpoolmanager.rowCount(testDataExcelPath, "LanguageCodes");
			for(int j=1;j<rowCount2;j++){
				//Step 2: To get the languages in MAP and load the language specific url
				rowData2 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",  j);
				String languageDescription = rowData2.get("Language").toString();
				String languageCode = rowData2.get("Language Code").toString().trim();
				language.details_append("Verify the Language code is " +languageCode+" ", " Application window should be displayed in " +languageDescription+"", "", "");



				System.out.println(languageCode +","+ languageDescription);

				if(j==2)
				{
					System.out.println("In j 2");
				}

				cfnlib.splashScreen(language,languageCode);					


				language.details_append_folder(" Verify that the application should display with Game Logo and game name in Base Scene", "Game logo and game name with base scene should display", "Game logo and game name with base scene displays", "Pass", languageCode);
				new WebDriverWait(webdriver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cfnlib.xpathMap.get("clock"))));
				Thread.sleep(1000);


				//to remove hand gesture
				cfnlib.funvFullScreenOff();

				language.details_append_folder("Verify Game in Language " +languageCode+" ", " Basescene window should be displayed in " +languageDescription+"", "Basescene window should be displayed in " +languageDescription+"", "Basescene window is displayed in " +languageDescription+"", languageCode);

				language.details_append_folder("Verify language on the Base scene Screen in portrait mode", "Base scene Screen should be display in portrait mode", "Base scene Screen should be displayed", "Pass", languageCode);
				cfnlib.funcLandscape();
				language.details_append_folder("Verify language on the Base scene Screen in landscape mode", "Base scene Screen should be display in landscape mode", "Base scene Screen should be displayed", "Pass", languageCode);
				cfnlib.funcPortrait();




				//Capture Screen shot for Bet Screen
		        cfnlib.openTotalBet();
			   	language.details_append_folder("Verify that language on the Bet Settings Screen in portrait mode", "Bet Settings Screen should display in portrait mode", "Bet Settings Screen displays in " +languageDescription+ " language", "Pass", languageCode);



				cfnlib.closeTotalBet();
				log.debug("Done for bet"); 


				//Capture Screen shot for Autoplay Screen

				boolean openAutoplay = cfnlib.open_Autoplay();
				if (openAutoplay){
					//cfnlib.sliderMove(cfnlib.XpathMap.get("spinSlider"));
					//cfnlib.sliderMove(cfnlib.XpathMap.get("winLimitSlider"));
					//cfnlib.sliderMove(cfnlib.XpathMap.get("lossLimitSlider"));						
					language.details_append_folder("Verify language on the Autoplay Screen in portrait mode", "Autoplay Screen should be display in portrait mode", "Autoplay Screen should be displayed", "Pass", languageCode);
				}
				else{
					language.details_append_folder("Verify language on the Autoplay Screen in portrait mode", "Autoplay Screen should be display", "Autoplay Screen doesn't display", "Fail", languageCode);   
				}


				cfnlib.close_Autoplay();
				log.debug("Done for Autoplay");




			if(!gameName.contains("Scratch"))
			{
				cfnlib.capturePaytableScreenshot(language, languageCode);
				//cfnlib.paytableOpenScroll(language, languageCode);									
				log.debug("Done for paytable");
			}	


				if (j + 1 != rowCount2){
					rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j+1);
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
						language.details_append_folder("Verify the Language code is " +languageCode2+" ", " Application window should be displayed in " +languageDescription+"", "", "", languageCode2);
						language.details_append_folder("Verify that any error is coming","error must not come","error is coming", "fail", languageCode2);
						if (j + 2 != rowCount2){
							rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j+2);
							String languageCode3 = rowData3.get("Language Code").toString().trim();
							currentUrl = webdriver.getCurrentUrl();
							url_new = currentUrl.replaceAll("LanguageCode="+languageCode2, "LanguageCode="+languageCode3);
							webdriver.navigate().to(url_new);
						}
						j++;
					}
				}
			}*/
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