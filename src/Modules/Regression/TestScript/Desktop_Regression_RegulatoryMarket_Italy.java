package Modules.Regression.TestScript;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.library.CommonUtil;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;

public class Desktop_Regression_RegulatoryMarket_Italy

{
	/**
	 * Desktop ReelThunder "gtp 131" This script verifies the Italy check points.
	 * ============================================= 
	 * Launch the Italy with EN & IT
	 * 1. Verify Take to game play request present or not
	 * 2. Verify Scroll Bar
	 * 3.Verify Credit amount is same as selected one from game play request screen
	 * 4.Verify Reel Spin Duration
	 * 5. Verify Currency Check
	 * 6. Verify Top Bar
	 * 7.Verify Game Name Top Bar 
	 * 8.Verify Clock Top Bar
	 * 9.Verify Player ProtectionIcon from Top Bar 
	 * 10.Top Bar Menu : Help , Responsible Gaming and Player protection
	 * 11.Quick Spin Availability 
	 * 12.Stop Button Availability
	 * 
	 *
	 */

	public ScriptParameters scriptParameters;

	Logger log = Logger.getLogger(Desktop_Regression_RegulatoryMarket_Italy.class.getName()); // To get Logs

	// -------------------Main script defination---------------------//
	public void script() throws Exception {
		String mstrTCName = scriptParameters.getMstrTCName();
		String mstrTCDesc = scriptParameters.getMstrTCDesc();
		String mstrModuleName = scriptParameters.getMstrModuleName();
		WebDriver webdriver = scriptParameters.getDriver();
		BrowserMobProxyServer proxy = scriptParameters.getProxy();
		String startTime = scriptParameters.getStartTime();
		String filePath = scriptParameters.getFilePath();
		String browserName = scriptParameters.getBrowserName();
		String framework = scriptParameters.getFramework();
		String gameName = scriptParameters.getGameName();
		String status = null;
		int mintDetailCount = 0;
		int mintSubStepNo=0;
		int mintPassed = 0;
		int mintFailed = 0;
		int mintWarnings = 0;
		int mintStepNo = 0;
		String userName ;
		String regMarket = "Italy";
		int envId = 1658;
		int productId = 5100;
		String currencyIsoCode = "EUR";
		int marketTypeID = 2;
		double balance = 10000;

		Desktop_HTML_Report italy = new Desktop_HTML_Report(webdriver, browserName, filePath, startTime, mstrTCName,mstrTCDesc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings, mintStepNo, status,gameName);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,italy, gameName);
		CommonUtil util = new CommonUtil();

		try {
			if (webdriver != null) {

				for (int i = 1; i < 3; i++) 
				{
					userName=util.randomStringgenerator();
					util.randomUserCreationForRegMarkets(userName, regMarket, envId, productId,currencyIsoCode,marketTypeID);
					util.updateUserBalance(userName, balance);
					String url = cfnlib.XpathMap.get("Italy_url");
					if(i==1)
					{	
						String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
						log.debug("url = " +LaunchURl);
						cfnlib.loadGame(LaunchURl);	
					}
					else
					{
						String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
						
						if(LaunchURl.contains("languagecode"))
							LaunchURl=	LaunchURl.replace("languagecode=en","languagecode=it");
						else if(LaunchURl.contains("languageCode"))
							LaunchURl =LaunchURl.replace("languageCode=en","languageCode=it");
							
						log.debug("url = " +LaunchURl);
						cfnlib.loadGame(LaunchURl);	
					}

					if (framework.equalsIgnoreCase("Force"))
					{
						cfnlib.setNameSpace();
						System.out.println("Force Games");

					}

					/*
					 * Verify Take to game screen feature appears or not && verify the scroll bar
					 * and set the amount
					 */

					String amount = cfnlib.XpathMap.get("Italy_amount"); // Italy amount from Excel
					log.debug("Amount from Excel is " + amount);
					if (amount != null) 
					{
						cfnlib.italyScrollBarAmount(italy, amount); // method for scroll bar
						log.debug("Bet selected and clicked on contine and now moved to the game ");
						italy.detailsAppend("Game Play ", " Game Play is Done ", " Game Play is Done", "PASS");
					}
					else
					{
						italy.detailsAppend("Game Play is Done", "Game Play is not Done ", "Game Play is not Done ","FAIL");
					}
                 /*
					// This is for Continue Button on Loading Screen
					if ((Constant.YES.equalsIgnoreCase(cfnlib.XpathMap.get("continueBtnOnGameLoad")))) 
					{
						cfnlib.newFeature();
					}
					*/

					/*
					 * Compare the Credit Amount from the Game play request to Base Game / Verify
					 * Credit amount is same selected one
					 */

					boolean isCreditAmount = cfnlib.italyCreditAmountComparission();
					if (isCreditAmount) 
					{
						italy.detailsAppend("Credit Comparission from Game Play", "Credits are Same","Credits are Same", "PASS");
					} 
					else {
						italy.detailsAppend("Credit Comparission from Game Play", "Credits are not Same","Credits are not Same", "PASS");
					}

					// To find the reel spin duration for a single spin
					long isReelspin = cfnlib.reelSpinDuratioN(italy);
					if (isReelspin < 4000)
					{
						System.out.println("Reel Spin Duration , STATUS : PASS");
						italy.detailsAppend("Reel Spin Duration ", "Reel Spin Duration in Sec is :" + isReelspin,"Reel Spin Duration  in Sec is :" + isReelspin, "PASS");
					} 
					else
					{
						System.out.println("Reel Spin Duration , STATUS : FAIL");
						italy.detailsAppend("Reel Spin Duration ", "Reel Spin Duration in Sec is :" + isReelspin,"Reel Spin Duration  in Sec is :" + isReelspin, "FAIL");
					}

					// Currency Check
					boolean curencycheck = cfnlib.italyCurrencyCheck(italy);
					if (curencycheck)
					{
						System.out.println("YES, The Currency is SAME");
						italy.detailsAppend(" Currency", "Currency is Same", "Currency is Same", "PASS");
					} 
					else 
					{
						System.out.println("NO, The Currency is Difference");
						italy.detailsAppend(" Currency", "Currency is Same", "Currency is Same", "FAIL");
					}

					// Verify TopBar is visble or not

					boolean isTopBarVisible = cfnlib.verifyTopBarVisible();
					Thread.sleep(2000);
					if (isTopBarVisible) 
					{
						italy.detailsAppend("Verify TopBar ", "TopBar is displayed", "TopBar is didplayed", "PASS");
					} 
					else 
					{
						System.out.println("Topbar is not visible");
						italy.detailsAppend("Verify TopBar must be is displayed", "TopBar is not didplayed","TopBar is not didplayed", "FAIL");
					}

					// Game name on the Top Bar 

					String isGameName = cfnlib.gameNameFromTopBar(italy);
					if (isGameName != null) {

						italy.detailsAppend("Gamename on Topbar", "Gamename is displayed ", "Gamename is displayed ","PASS");
					}
					else {
						italy.detailsAppend("Gamename on Topbar", "Gamename is displayed ", "Gamename is displayed ","FAIL");
					}

					boolean playerprotectionicon = cfnlib.playerProtectionIcon(italy);
					if (playerprotectionicon) 
					{
						italy.detailsAppend("PlayerProtection Icon ", "PlayerProtection and its Navigation ","PlayerProtection and its Navigation", "PASS");

					}

					else {
						italy.detailsAppend("PlayerProtection Icon ", "PlayerProtection and its Navigation ","PlayerProtection and its Navigation", "FAIL");
					}

					// Clock on the Top Bar 
					boolean clock = cfnlib.clockFromTopBar(italy); // clockOnTheTopBar
					if (clock ) 
					{
						italy.detailsAppend(" Clock on Topbar", "Clock is Displayed ", "Clock is Displayed ", "PASS");
					} 
					else 
					{
						italy.detailsAppend(" Clock on Topbar", "Clock is Displayed ", "Clock is Displayed ", "FAIL");
					}

					// Verify Help from Menu
					boolean help = cfnlib.italyHelpFromTopBarMenu(italy);
					if (help)
					{
						italy.detailsAppend("Help ", "Help is Displayed and its Navigation", "Help is Displayed and its Navigation", "PASS");
					}

					else
					{
						italy.detailsAppend("Help ", "Help is Displayed and its Navigation", "Help is Displayed and its Navigation", "FAIL");
					}

					// Verify if Player Protection from Menu
					boolean playerprotection = cfnlib.italyPlayerProtectionFromTopBarMenu(italy);
					if (playerprotection)
					{
						italy.detailsAppend("PlayerProtection ", "PlayerProtection is Displayed and its Navigation", "PlayerProtection is Displayed and its Navigation", "PASS");
					}
					else 
					{
						italy.detailsAppend("PlayerProtection ", "PlayerProtection is Displayed and its Navigation", "PlayerProtection is Displayed and its Navigation", "FAIL");
					}

					Thread.sleep(3000);

					// Verify Resposible Gaming from Menu
					boolean responsiblegaming = cfnlib.italyResponsibleGamingFromMenu(italy);
					if (responsiblegaming) {
						italy.detailsAppend("ResponsibleGaming ", "ResponsibleGaming is Displayed and its Navigation", "ResponsibleGaming is Displayed and its Navigation", "PASS");
					}
					else 
					{
						italy.detailsAppend("ResponsibleGaming ", "ResponsibleGaming is Displayed and its Navigation", "ResponsibleGaming is Displayed and its Navigation", "FAIL");
					}
					Thread.sleep(3000);

					// Verify the quickspin button Availability 

					boolean isquickspin = cfnlib.verifyQuickspinAvailablity();
					Thread.sleep(1000);
					if (isquickspin) 
					{
						italy.detailsAppend(" Quickspin ", "Quickspin is available ", "Quickspin is available", "FAIL");
					} 
					else
					{
						italy.detailsAppend(" Quickspin ", "Quickspin is not available ", "Quickspin is not available","PASS");
					}
					Thread.sleep(2000);

					// Verify the stop button Availability 

					boolean isstopbtn = cfnlib.verifyStopButtonAvailablity();
					Thread.sleep(1000);
					if (isstopbtn)
					{
						italy.detailsAppend("Stop button", "Stop button is available", "Stop button is available","FAIL");
					} else {
						italy.detailsAppend("Stop button", "Stop button is not available","Stop button is not available", "PASS");
					}
					Thread.sleep(2000);

					System.out.println("!!!!!!! Done with Italy Market !!!!!!!");
					System.out.println("                                           ");

				}

			}

		}
		// TRY
		// -------------------Handling the exception---------------------//

		catch (Exception e) {
			log.error(e.getMessage(), e);
			cfnlib.evalException(e);
		}

		finally {

			// Global.browserproxy.abort();
			// ---------- Closing the report----------//

			italy.endReport();
			// ---------- Closing the webdriver ----------//
			webdriver.close();

			webdriver.quit();

			// Global.appiumService.stop();
		}
	}

}
