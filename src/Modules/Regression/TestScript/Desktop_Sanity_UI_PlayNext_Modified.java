package Modules.Regression.TestScript;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.ImageLibrary;
import com.zensar.automation.library.TestPropReader;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 * 
 * @author pb61055
 *
 */
public class Desktop_Sanity_UI_PlayNext_Modified {
	
		Logger log = Logger.getLogger(Desktop_Sanity_UI_PlayNext_Modified.class.getName()); 
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
			String urlNew=null;
			int startindex=0;
			String strGameName =null;
			ImageLibrary imageLibrary;						
			Desktop_HTML_Report sanityReport = new Desktop_HTML_Report(webdriver,browserName,filePath,startTime,mstrTCName,mstrTCDesc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,status1,gameName);
			DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
			CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,sanityReport, gameName);
			CommonUtil util = new CommonUtil();
			RestAPILibrary apiObj = new RestAPILibrary();					
			List<String> copiedFiles=new ArrayList<>();
			int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
			int cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDDesktop"));
			
			try
			{ 
				if(webdriver!=null)
				{	
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
					imageLibrary = new ImageLibrary(webdriver, strGameName, "Desktop");
					String strFileName = TestPropReader.getInstance().getProperty("SanityTestDataPath");
					File testDataFile = new File(strFileName);
					List<Map<String, String>> currencyList = util.readCurrList();// mapping
					for (Map<String, String> currencyMap : currencyList) 
					{
						try {									
							// Step 2: To get the languages in MAP and load the language specific url
							String isoCode = currencyMap.get(Constant.ISOCODE).trim();
							String currencyID = currencyMap.get(Constant.ID).trim();
							String currencyName = currencyMap.get(Constant.ISONAME).trim();
							String languageCurrency = currencyMap.get(Constant.LANGUAGECURRENCY).trim();
							String url = cfnlib.XpathMap.get("ApplicationURL");						
							log.debug(this + " I am processing currency:  " + currencyName);
												
							sanityReport.detailsAppend("*** CURRENCY AND LANGUAGE ***", "Currency: "+currencyName+", Language: "+currencyName,"", "");
						
							sanityReport.detailsAppend("Sanity Suite Test cases in "+currencyName+" for "+currencyName+" ", "Sanity Suite Test cases", "", "");
						
							userName = util.randomStringgenerator();
							System.out.println(userName);
						
							if (util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles, currencyID,isoCode))
							{
								Thread.sleep(3000);
							
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
							
								util.updateUserBalance(userName,balance);
								Thread.sleep(3000);	
							
								String launchURl = url.replaceFirst("\\busername=.*?(&|$)", "username=" + userName + "$1");
								if(launchURl.contains("LanguageCode"))
								urlNew = launchURl.replaceAll("LanguageCode=en", "LanguageCode=" + currencyName);
								else if(launchURl.contains("languagecode"))
								urlNew = launchURl.replaceAll("languagecode=en", "languagecode=" + currencyName);
								else if(launchURl.contains("languageCode"))
								urlNew = launchURl.replaceAll("languageCode=en", "languageCode=" + currencyName);

								log.info("url = " + urlNew);
								System.out.println(urlNew);
							
								
								if ("Yes".equalsIgnoreCase(cfnlib.XpathMap.get("NFD"))) {
									sanityReport.detailsAppendFolder("Verify Splash/Loading screen is visible",
											"Splash/Loading screen should be visible",
											"Splash/Loading screen is visible", "Pass", currencyName);
									Thread.sleep(8000);
									sanityReport.detailsAppendFolder("Verify Continue button on base scene ",
											"Continue buttion", "Continue button", "Pass", currencyName);

									cfnlib.clickOnBaseSceneContinueButton(sanityReport, imageLibrary, currencyName);
								}	
								
								cfnlib.refreshGame( sanityReport, imageLibrary, currencyName);
							
								if(imageLibrary.isImageAppears("Spin"))
									sanityReport.detailsAppendFolder("Verify Base Scene", "Base Scene is visible", "Base Scene is visible", "Pass",currencyName );
								else
									sanityReport.detailsAppendFolder("Verify Base Scene", "Base Scene is visible", "Base Scene is not visible", "Fail",currencyName);										
								
								
								//************** Quick Spin ********************
								
								if(cfnlib.XpathMap.get("QuickSpinonBaseGame").equalsIgnoreCase("Yes"))
								{
									sanityReport. detailsAppend("Following is the QuickSpin verification test case" ,"Verify QuickSpin", "", "");
								
									cfnlib.verifyQuickSpin(sanityReport, imageLibrary, currencyName);
								}								
								
								//************** Bet panel ********************
								
								sanityReport.detailsAppend("Following are the Bet value verification test cases", "Verify Bet values", "", "");	
								
								cfnlib.verifyBetPanel(sanityReport, imageLibrary, currencyName);
								
								//************** Menu, Paytable and Settings *********************		
								
								sanityReport.detailsAppend("Following are the MenuOption test cases", "Verify MenuOptions", "", "");
								
								cfnlib.openMenuPanel(sanityReport, imageLibrary, currencyName);
								
								cfnlib.closeButton(imageLibrary);
								
								cfnlib.verifyPaytable(sanityReport, imageLibrary, currencyName);
					
								cfnlib.verifySettingsPanel(sanityReport, imageLibrary, currencyName);
					
							
								//************** Help Navigation *********************

								sanityReport.detailsAppend("Following is the Help navigation test case", "Verify Help", "", "");
								
								cfnlib.verifyHelpNavigation(sanityReport, imageLibrary, currencyName,"dotCom");
					
								//************** Autoplay *********************
								
								sanityReport.detailsAppend("Following are the Autoplay verification test cases", "Verify Autoplay", "", "");
					
								cfnlib.verifyAutoplayPanel(sanityReport, imageLibrary, currencyName);
								
							
							}
						} // try
						catch (Exception e) {
							System.out.println("Exception occur while processing currency: "+ e); log.error("Exception occur while processing currency", e);
							sanityReport.detailsAppend("Exception occur while processing currency ", " ", "", "Fail");
							cfnlib.evalException(e);
						}

					} // for loop : mapping currencies 
				} //driver Closed
					
			}//closing try block 
			// -------------------Handling the exception---------------------//
			catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			// -------------------Closing the connections---------------//
			finally {
			sanityReport.endReport();
			if(!copiedFiles.isEmpty())
			{
				if(TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
						util.deleteBluemesaTestDataFiles(mid,cid,copiedFiles);
				else
					apiObj.deleteAxiomTestDataFiles(copiedFiles);
			}
				webdriver.close();
				webdriver.quit();
				//proxy.abort();
				Thread.sleep(1000);
		}
			
	}

}