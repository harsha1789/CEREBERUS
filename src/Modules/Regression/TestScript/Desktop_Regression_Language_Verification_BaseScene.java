package Modules.Regression.TestScript;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

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
 * This Script traverse all the panels and take the screen shot of the all screens such as paytable, bet settings, auto play etc.
 * It reads the test data excel sheet for configured languages.
 * @author Premlata
 */

public class Desktop_Regression_Language_Verification_BaseScene{
	
	Logger log = Logger.getLogger(Desktop_Regression_Language_Verification_BaseScene.class.getName());
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
		String languageCode=null;
		String status=null;
		int mintDetailCount=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		
		ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();
		Desktop_HTML_Report language = new Desktop_HTML_Report(webdriver,browserName,filePath,startTime,mstrTCName,mstrTCDesc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,status,gameName);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, language, gameName);

		try{
			// Step 1 
			if(webdriver!=null)
			{		
				Map<String, String> rowData2 = null;
				Map<String, String> rowData3 = null;
				CommonUtil util = new CommonUtil();
				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				String urlNew=null;
			
				String url = cfnlib.XpathMap.get("ApplicationURL");
				String launchURL = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
				log.info("url = " +launchURL);
							
				cfnlib.loadGame(launchURL);
			     log.debug("navigated to url ");
							
			    //wait to load assets
				cfnlib.threadSleep(10000);
			     
				if(framework.equalsIgnoreCase(Constant.FORCE)){
					cfnlib.setNameSpace();
					}
				List<Map> list= util.readLangList();
				
				int rowCount2 = excelpoolmanager.rowCount(testDataExcelPath, Constant.LANG_XL_SHEET_NAME);
				for(int j=1;j<rowCount2;j++){
					//Step 2: To get the languages in MAP and load the language specific url
					rowData2 = list.get(j);
					languageDescription = rowData2.get(Constant.LANGUAGE).trim();
					languageCode = rowData2.get(Constant.LANG_CODE).trim();
					
					language.detailsAppend("Verify the Language code is " +languageCode+" ", " Application window should be displayed in " +languageDescription+"", "", "");
					
					cfnlib.splashScreen(language,languageCode);					
					
					language.detailsAppendFolder(" Verify that the application should display with Game Logo and game name in Base Scene", "Game logo and game name with base scene should display", "Game logo and game name with base scene displays", "Pass", languageCode);
					Thread.sleep(1000);
					
					cfnlib.newFeature();
					Thread.sleep(1500);
					
					//Not required for force
					
					if(!framework.equalsIgnoreCase(Constant.FORCE)){
						cfnlib.verifyStopLanguage(language, languageCode);
					}
						
				
					Thread.sleep(2000);
					// Open and Capture Screen shot of Bet Screen
					boolean b = cfnlib.open_TotalBet();
					if (b){
						Thread.sleep(500);
					 language.detailsAppendFolder("Verify language on the Bet Amount Screen", "Bet Amount Screen should be display", "Bet Amount Screen should be displayed in " +languageDescription+ " language ", "Pass", languageCode);
					}
					else{
					 language.detailsAppendFolder("Verify language on the Bet Amount Screen", "Bet Amount Screen should be display", "Bet Amount Screen doesn't display", "Fail", languageCode);   
					}
					// Close the Bet Screen
					cfnlib.close_TotalBet();
					
				
					// Open and Capture Screen shot of Autoplay Screen
					boolean openAutoplay = cfnlib.openAutoplay();
					if (openAutoplay){
						 language.detailsAppendFolder("Verify language on the Autoplay Screen", "Autoplay Screen should be display", "Autoplay Screen should be displayed", "Pass", languageCode);
					}
					else{
						 language.detailsAppendFolder("Verify language on the Autoplay Screen", "Autoplay Screen should be display", "Autoplay Screen doesn't display", "Fail", languageCode);   
					}
					// Close Autoplay
					cfnlib.close_Autoplay();

					// Open and Capture Screen shot of menu Screen
					boolean menuOpen = cfnlib.menuOpen();
					if(!framework.equalsIgnoreCase("CS_Renovate")){
						if(menuOpen){
							Thread.sleep(1000);
							 language.detailsAppendFolder("Verify that Language of menu link is " + languageDescription + " ", "Language of Menu Links should be " +languageDescription+ "", "Menu Links are displaying in " +languageDescription+ " language", "pass", languageCode);
						}
						else {
							 language.detailsAppendFolder("Verify that Language" + languageDescription + "should display properly on menu links", "Language inside Menu Links should be " +languageDescription+ " ", "Menu Links are not displaying", "fail", languageCode);
						}
					}
					// Close Menu screen
					cfnlib.menuClose();

					//check first whether setting avilable or not
					if(!framework.equalsIgnoreCase("CS")&& cfnlib.checkAvilability(cfnlib.XpathMap.get("isMenuSettingsBtnVisible"))){
					boolean openSetting= cfnlib.settingsOpen();
					if(openSetting){
						 language.detailsAppendFolder("Verify that Language on settings screen is " + languageDescription + " ", "Language in Settigns Screen should be " +languageDescription+ " ", "Language inside Settings screens is " +languageDescription+ " ", "pass", languageCode);
					}
					else {
						 language.detailsAppendFolder("Verify that Language back on menu is " + languageDescription +"should be display properly on menu links", "Language inside Menu Links should be in " +languageDescription+ " language", "Language inside Menu Links is displaying", "fail", languageCode);
					}

					cfnlib.settingsBack();
					}
					
					if(TestPropReader.getInstance().getProperty("IsProgressiveGame").equalsIgnoreCase("Yes"))
					{
						
						//To capture  motivator and messages in Progressive game
						//Need player with win
						if(TestPropReader.getInstance().getProperty("IsMotivators&Messages").equalsIgnoreCase("Yes"))
						{
						String strNoOfMsgWithReelsSpinning = cfnlib.XpathMap.get("noOfMsgWithReelsSpinning");
						int noOfMsgWithReelsSpinning = (int) Double.parseDouble(strNoOfMsgWithReelsSpinning); 
						
						String strNoOfMsgWithReelsResolved = cfnlib.XpathMap.get("noOfMsgWithReelsResolved");
						int noOfMsgWithReelsResolved = (int) Double.parseDouble(strNoOfMsgWithReelsResolved); 
						
						
						
						String strNOfMotivators = cfnlib.XpathMap.get("noOfMotivators");
						int noOfMotivators = (int) Double.parseDouble(strNOfMotivators); 
						
						for (int spinCount=0; spinCount < noOfMsgWithReelsResolved; spinCount++)
						{
							cfnlib.spinclick();
							if(spinCount< noOfMsgWithReelsSpinning)
							{
								language.detailsAppendFolder("Verify Mesaage display below reels in language  " + languageDescription + " ", "Mesaage should be in  " +languageDescription+ " ", "Message is in  " +languageDescription+ " ", "pass", languageCode);
							}
							cfnlib.waitForSpinButtonstop();
							language.detailsAppendFolder("Verify Mesaage display below reels in language  " + languageDescription + " ", "Mesaage should be in  " +languageDescription+ " ", "Message is in  " +languageDescription+ " ", "pass", languageCode);
							Thread.sleep(1000);
						}
						}
						// Jackpot History
						cfnlib.verifyJackPotBonuswithScreenShots(language,languageCode);
						
					}
					
					
					if(!gameName.contains("Scratch") && cfnlib.checkAvilability(cfnlib.XpathMap.get("isMenuPaytableBtnVisible"))){
					//Open payatable and capture screen shots
					cfnlib.capturePaytableScreenshot(language, languageCode);
					// Closes the paytable
					cfnlib.paytableClose();
					}
					
					// To open and capture Story in Game (Applicable for the game immortal romances)
					if(cfnlib.checkAvilability(cfnlib.XpathMap.get("isPaytableStoryExists"))){
							cfnlib.Verifystoryoptioninpaytable(language, languageCode);
							cfnlib.paytableClose();
						log.debug("Done for story in paytable");
						}
					
					//Incase of Respin games it takes the bet dialog screenshot on bet change
					if("yes".equalsIgnoreCase(cfnlib.XpathMap.get("IsRespinFeature")))	
					{
						cfnlib.waitForSpinButton();
						Thread.sleep(1000);
						cfnlib.spinclick();
						cfnlib.waitForSpinButtonstop();
						Thread.sleep(5000);
						cfnlib.open_TotalBet();
						cfnlib.setMaxBet();
						cfnlib.close_TotalBet();
						language.detailsAppendFolder("Verify that Language on Bet Dialog screen is " + languageDescription + " ", "Language in Bet Dialog Screen should be " +languageDescription+ " ", "Language inside Bet Dialog screens is " +languageDescription+ " ", "pass", languageCode);
						cfnlib.clickAtButton(cfnlib.XpathMap.get("ClickOnBetDialogNo"));
						log.debug("Done for bet dialog");
					}

					//Language Change logic:: for updating language in URL and then Refresh 
					if (j + 1 != rowCount2){
						 rowData3 = list.get(j+1);
						String languageCode2 = rowData3.get(Constant.LANG_CODE).trim();

						String currentUrl = webdriver.getCurrentUrl();
						
						if(currentUrl.contains("LanguageCode"))
							urlNew= currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+languageCode2);
						else if(currentUrl.contains("languagecode"))
							urlNew = currentUrl.replaceAll("languagecode="+languageCode, "languagecode="+languageCode2);
						else if(currentUrl.contains("languageCode"))
							urlNew = currentUrl.replaceAll("languageCode="+languageCode, "languageCode="+languageCode2);
						
						cfnlib.loadGame(urlNew);
						
						String error=cfnlib.XpathMap.get("Error");
						if(cfnlib.isElementPresent(error))
						{
							language.detailsAppend("Verify the Language code is " +languageCode2+" ", " Application window should be displayed in " +languageDescription+"", "", "");
							language.detailsAppend("Verify that any error is coming","error must not come","error is coming", "fail");
							if (j + 2 != rowCount2){
								 rowData3 = list.get(j+2);
								String languageCode3 = rowData3.get(Constant.LANG_CODE).trim();

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
		}	    
		//-------------------Handling the exception---------------------//
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
