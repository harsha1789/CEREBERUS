package Modules.Regression.TestScript;



import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
 * This script verifies the UK check points.
 * UK Market Check Points :
	  =============================================
		Session Reminder
		Bonus fund notification
		Reel Spin Duration
		Top Bar Visible or Not
		currency
		Net Position and it's updation
		Compare the  Game Name
		Compare the Bet Value
		Verify  Clock on the Top Bar
		Verify Help link on top bar
		Verify the Quick Spin is present or not
		Verify the Stop is present or not
		Click on Help & Navigate to that page
		Click on Transaction history  & Navigate to that page
		Click on Game history & Navigate to that page
		Click on Player Protection & Navigate to that page
	
 	Requires Test data: 4 spins
 	 =============================================
		1st spin no win - to check reel spin duration
		2nd spin Win greater than bet - to check net position updating after win
		3rd spin no win - to check net position is updating after no win
		4th spin no win - to verify stop button
	
	Set Session reminder  - 3 minutes
 * @author pb61055
 *
 */
public class Desktop_Regression_RegulatoryMarket_Uk {
	public ScriptParameters scriptParameters;
	Logger log = Logger.getLogger(Desktop_Regression_RegulatoryMarket_Uk.class.getName());
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
		String regMarket ="UK";
		int envId = 1658;
		int productId = 5118;
		String currencyIsoCode = "GBP";
		int marketTypeID = 10;
		double balance=100000;
		
		
		Desktop_HTML_Report reportUK = new Desktop_HTML_Report(webdriver, browserName, filePath, startTime, mstrTCName,
				mstrTCDesc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings, mintStepNo, status,
				gameName);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,reportUK, gameName);

		CommonUtil util = new CommonUtil();
		int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int  cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDDesktop"));
		List<String> copiedFiles=new ArrayList<>();
		
		try {
			if (webdriver != null) 
			{	
				String strFileName=TestPropReader.getInstance().getProperty("FreeSpinsTestDataPath");
				File testDataFile=new File(strFileName);
			
				userName=util.randomStringgenerator();
				util.randomUserCreationForRegMarkets(userName, regMarket, envId, productId,currencyIsoCode,marketTypeID);
				util.updateUserBalance(userName,balance);
				if(util.copyFilesToTestServerForRegMarket( mid, cid, testDataFile, userName,  regMarket, envId, productId, currencyIsoCode , marketTypeID, copiedFiles))
				{
					log.debug("Test dat is copy in test Server for Username="+userName);

					
					
					String url = cfnlib.XpathMap.get("UK_url");
					String launchURL = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
					log.info("url = " +launchURL);
					cfnlib.loadGame(launchURL);
					log.debug("navigated to url ");
				
					
					if(framework.equalsIgnoreCase("Force"))
					{
						cfnlib.setNameSpace();
					}
			
					Thread.sleep(5000);
					
					// click on continue button if any
					if((Constant.YES.equalsIgnoreCase(cfnlib.XpathMap.get("continueBtnOnGameLoad"))))
					{
						cfnlib.newFeature();
					}
					
					//waiting for spin button
					cfnlib.waitForSpinButton();
					
					//To verify whether session reminder is present or not
					boolean sessionReminderVisible=cfnlib.sessionReminderPresent();
					Thread.sleep(1000);
					if(sessionReminderVisible) 
					{
						reportUK.detailsAppend("Verify Session Reminder must be present","Session Reminder must be present","Session Reminder is present","pass");
					
						//To click on continue button in session reminder
						cfnlib.selectContinueSession();
						Thread.sleep(2000);
					}
					else
					{
						reportUK.detailsAppend("Verify Session Reminder must be present","Session Reminder must be present","Session Reminder is not present","fail");
						System.out.println("Session reminder is not visible on screen");
						log.debug("Session reminder is not visible on screen");
						Thread.sleep(2000);
					}
			
					//To find the reel spin duration for a single spin	
					long reelSpinDuration=cfnlib.reelSpinDuration();
					if(reelSpinDuration >= 2500)
					{
						reportUK.detailsAppend("Verify Reel spin duration for uk is greater than or equal to 2.5 seconds", "Reel spin duration for uk should be greater than or equal to 2.5 seconds " , "Reel spin duration for uk is correct, "+reelSpinDuration+" milliseconds", "pass");			
					}
					else
					{
						reportUK.detailsAppend("Verify Reel spin duration for uk is greater than or equal to 2.5 seconds", "Reel spin duration for uk should be greater than or equal to 2.5 seconds " , "Reel spin duration for uk is incorrect, "+reelSpinDuration+" milliseconds", "fail");			
					}
			
					//To verify currency symbol and currency format
					String currencyName =cfnlib.XpathMap.get("ukCurrencyName");
					String currencyFormat=cfnlib.XpathMap.get("ukCurrencyFormat");
					String currencySymbol = cfnlib.getCurrencySymbol();
					log.info(currencyName + "currency symbol is " + currencySymbol);
					String poundSymbol= cfnlib.XpathMap.get("ukPoundSymbol");;
					if(poundSymbol.equals(currencySymbol))
					{
						reportUK.detailsAppend("Verify that currency symbol in credit is British Pound ",
								"Credit should display in British Pound currency symbol : £ " ,
								"Credit is displaying in British Pound currency symbol " +currencySymbol, "Pass");
					}
					else
					{
						reportUK.detailsAppend("Verify that currency symbol in credit is British Pound ",
								"Credit should display in British Pound currency symbol : £ " ,
								"Credit is not displaying in British Pound currency symbol " +currencySymbol, "Fail");
					}

					// verify currency format in credits
					boolean result=cfnlib.verifyCurrencyFormat(currencyFormat);

					if (result) {
						reportUK.detailsAppend("Verify that currency format in credit ",
								"Credit should display in correct currency format " ,
								"Credit is displaying in correct currency format ", "Pass");
					} else {
						reportUK.detailsAppend("Verify that currency format in credit ",
								"Credit should display in correct currency format " ,
								"Credit is not displaying in correct currency format ", "Fail");
					}
						
				
					//verifying whether Top bar is visible or not	
					boolean isTopBarVisible=cfnlib.isTopBarVisible();
					Thread.sleep(2000);
					if(isTopBarVisible) 
					{	
						reportUK.detailsAppend("Verify TopBar must be is displayed", "TopBar should be displayed", "TopBar is displayed", "pass");
					
						//Verifying game name on Top bar
						cfnlib.gameNameOnTopBar(reportUK);
						Thread.sleep(2000);
					
						//Verifying bet value on Top bar
						cfnlib.betOnTopBar(reportUK);
						Thread.sleep(2000);
					
						//verifying help text link on top bar
						cfnlib.verifyHelpTextlink(reportUK);
						Thread.sleep(2000);
					
						//verifying clock on top bar
						cfnlib.clockOnTopBar(reportUK);
						Thread.sleep(2000);
					
						//verifying net position on top bar
						boolean isNetPositionUpdated=cfnlib.verifyNetPosition(reportUK);
						if(isNetPositionUpdated)
						{
							Thread.sleep(2000);
							webdriver.navigate().refresh();
							cfnlib.waitForSpinButton();
							if ((Constant.YES.equalsIgnoreCase(cfnlib.XpathMap.get("continueBtnOnGameLoad")))) 
							{
								cfnlib.newFeature();
							}
							Thread.sleep(2000);
							//To check refresh scenario, net position should be 0 after refresh
							try 
							{
								String netPositionAfterRefresh = webdriver.findElement(By.xpath(cfnlib.XpathMap.get("isNetPosition"))).getText();
								String netValue = netPositionAfterRefresh.replaceAll("[^0-9..]", "");
								String netPos = "0.00";
							
								if (netValue.equals(netPos)) 
								{
									System.out.println("Net position updated correctly after refresh");
									reportUK.detailsAppend("verify Net position is 0 after refresh ",
											"Net position should be 0 after refresh ", "Net position is 0 after refresh", "Pass");
								} 
							}
							catch(Exception e) 
							{
									System.out.println("Net position not updated correctly after refresh");
									reportUK.detailsAppend("verify Net position is 0 after refresh ",
											"Net position should be 0 after refresh ", "Net position is not 0 after refresh", "fail");
							}
					
							// To check refresh scenario, Bonus reminder should be present after refresh
							try
							{
								boolean isBRVisible = webdriver.findElement(By.xpath(cfnlib.XpathMap.get("isBonusReminderPresent"))).isDisplayed();
								if (isBRVisible) 
								{
									//verify bonus reminder and terms & conditions navigation
									cfnlib.verifyBonusReminder(reportUK);
									reportUK.detailsAppend("verify Bonus Reminder is present after refresh ","Bonus Reminder should be present after refresh", "Bonus Reminder is present after refresh","Pass");
									System.out.println("Bonus Reminder is present after refresh");
								} 
								else 
								{
									System.out.println("Bonus Reminder is not present after refresh");
									reportUK.detailsAppend("verify Bonus Reminder is present after refresh ","Bonus Reminder should be present after refresh", "Bonus Reminder is not present after refresh","Fail");
								}
							}
						catch (Exception e)
						{
							log.error("Error in checking Bonus Reminder availability ", e);
							reportUK.detailsAppend("Verify Bonus Reminder must be is available",
									"Bonus Reminder Should be is availabler", "Bonus Reminder is not available", "fail");
							System.out.println("Bonus Reminder is not available");
						}
					}
					
					//To verify menu navigations from top bar
					cfnlib.verifyMenuOptionNavigationsForUK(reportUK);
					Thread.sleep(2000);
									
					}					
					else
					{
						System.out.println("Topbar is not visible");
						reportUK.detailsAppend("Verify TopBar must be is displayed", "TopBar should be displayed", "TopBar is not displayed", "fail");
					}
					
					//Verify the quick spin button availability
					boolean isquickspin=cfnlib.isQuickspinAvailable();
					Thread.sleep(1000);
					if(!isquickspin)
					{	
						reportUK.detailsAppend("Verify that Quickspin must not be available in Base Scene", "Quickspin must not be available in BaseScene", "Quickspin is not available in Base Scene", "pass");
					}
					else
					{
						reportUK.detailsAppend("verify that Quickspin must not be available in Base Scene", "Quickspin must not be available in Base Scene", "Quickspin is available in BaseScene", "fail");
					}	
					Thread.sleep(2000);
			
					//Verify the stop button availability
					boolean isstopbtn=cfnlib.isStopButtonAvailable();
					Thread.sleep(1000);
					if(!isstopbtn)
					{	
						reportUK.detailsAppend("Verify that Stop button must not be available in Base Scene", "Stop button must not be available in BaseScene", "Stop button is not available in Base Scene", "pass");
					}
					else
					{	
						reportUK.detailsAppend("verify that Stop button must not be available in Base Scene", "Stop button must not be available in Base Scene", "Stop button is available in BaseScene", "fail");
					}	
					Thread.sleep(2000);
					}else
					{
						log.debug("Unable to copy test data file on the environment hence skipping execution");
						reportUK.detailsAppend("", "", "Unable to copy test data on server,hence skipping futher execution", "fail");
					}
				}// end of if block
			}// end of try block
		
		//-------------------Handling the exception---------------------//		
		catch (Exception e) 
		{
			log.error(e.getMessage(),e);
			cfnlib.evalException(e);
		}

		//-------------------Closing the connections---------------//
		finally 
		{

			// ---------- Closing the report----------//
			reportUK.endReport();
			
			// ---------- Closing the webdriver ----------//
			webdriver.close();
			webdriver.quit();

		}
	}
}
	
	