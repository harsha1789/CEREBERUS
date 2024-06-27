package Modules.Regression.TestScript;

import java.io.File;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.DataBaseFunctions;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop_CS;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop_CS_Renovate;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop_Force;
import net.lightbody.bmp.BrowserMobProxyServer;

public class Desktop_Regression_CurrencySymbol_Custom {
	Logger log = Logger.getLogger(Desktop_Regression_CurrencySymbol_Custom.class.getName());
	public ScriptParameters scriptParameters;
	
	

	public void script() throws Exception {
		String classname = this.getClass().getSimpleName();
		String xmlFilePath = TestPropReader.getInstance().getProperty("MobileFreeSpinsTestDataPath");
		String mstrTC_Name=scriptParameters.getMstrTCName();
		String mstrTC_Desc=scriptParameters.getMstrTCDesc();
		String mstrModuleName=scriptParameters.getMstrModuleName();
		WebDriver webdriver=scriptParameters.getDriver();
		BrowserMobProxyServer proxy=scriptParameters.getProxy();
		String startTime=scriptParameters.getStartTime();
		String filePath=scriptParameters.getFilePath();
		String userName=scriptParameters.getUserName();
		String Browsername=scriptParameters.getBrowserName();
		String Framework=scriptParameters.getFramework();
		String GameName=scriptParameters.getGameName();
		String Status=null;
		int mintDetailCount=0;
		//int mintSubStepNo=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		File sourceFile = new File(xmlFilePath);
		File destFile=null;
		double defaultBet1 = 0;
		 
		ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();
		Desktop_HTML_Report currency = new Desktop_HTML_Report(webdriver, Browsername, filePath, startTime, mstrTC_Name,
				mstrTC_Desc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings, mintStepNo, Status,
				GameName);
		CFNLibrary_Desktop cfnlib = null;

		if (Framework.equalsIgnoreCase("PN")) {
			cfnlib = new CFNLibrary_Desktop(webdriver, proxy, currency, GameName);
		} else if (Framework.equalsIgnoreCase("Force")) {
			cfnlib = new CFNLibrary_Desktop_Force(webdriver, proxy, currency, GameName);
		} else if (Framework.equalsIgnoreCase("CS_Renovate")) {
			cfnlib = new CFNLibrary_Desktop_CS_Renovate(webdriver, proxy, currency, GameName);
		} else {
			cfnlib = new CFNLibrary_Desktop_CS(webdriver, proxy, currency, GameName);
		}

		CommonUtil util = new CommonUtil();

		DataBaseFunctions createplayer = new DataBaseFunctions(TestPropReader.getInstance().getProperty("ServerName"),
				TestPropReader.getInstance().getProperty("dataBaseName"),
				TestPropReader.getInstance().getProperty("serverIp"), TestPropReader.getInstance().getProperty("port"),
				TestPropReader.getInstance().getProperty("serverID"));

		try {
			// -------Getting webdriver of proper test site-------//
			if (webdriver != null) {
				Map<String, String> rowData1 = null;
				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				int rowCount1 = excelpoolmanager.rowCount(testDataExcelPath, "CurrencySymbol");
				for (int j = 1; j < rowCount1; j++) {
					rowData1 = excelpoolmanager.readExcelByRow(testDataExcelPath, "CurrencySymbol", j);
					String CurrencyID = rowData1.get("CurrencyID").toString();
					// double CurrencyID1 = Double.parseDouble(CurrencyID);

					String CurrencyName = rowData1.get("Name").toString().trim();
					String multiplier = rowData1.get("Multiplier").toString();
					// long multiplierValue = Long.parseLong(multiplier);
					// int multi = Integer.parseInt(multiplier);
					double multiplierValue = Double.parseDouble(multiplier);

					userName = util.randomStringgenerator();
					log.debug("The New username is ==" + userName);
					createplayer.createUser(userName, CurrencyID, 0);

					destFile = new File("\\\\" + TestPropReader.getInstance().getProperty("Casinoas1IP")
							+ "\\c$\\MGS_TestData\\" + classname + "_" + userName + ".testdata");

					// --------Update the Xml File of Papyrus Data----------//
					util.changePlayerName(userName, xmlFilePath);
					String url = cfnlib.XpathMap.get("ApplicationURL");
					;

					// -----Copy the Papyrus Data to The CasinoAs1 Server-----//
					util.copyFolder(sourceFile, destFile);
					cfnlib.func_navigate(url);
					currency.detailsAppendFolder("Verify game for " + CurrencyName + " currency",
							"Game should work with " + CurrencyName + " currency", "", "", "");
					/*
					 * if (obj != null) { currency.details_append(
					 * "Open Browser and Enter Lobby URL in address bar and click Enter"
					 * , "Island Paradise Lobby should open",
					 * "Island Paradise lobby is Open", "Pass"); } else {
					 * currency.details_append(
					 * "Open browser and Enter Lobby URL in address bar and click Enter"
					 * , "Island Paradise lobby should Open",
					 * "Island paradise is not displayed", "Fail"); }
					 */
					long loadingTime = cfnlib.Random_LoginBaseScene(userName);
					if (loadingTime < 10.0) {
						currency.detailsAppend("Check that user is able to login and Observe initial loading time",
								"" + GameName
										+ " Game should be launched and initial loading time should be less than 10 seconds",
								"" + GameName + " is launched successfully and initial loading time is " + loadingTime
										+ " Seconds",
								"Pass");
					} else {
						currency.detailsAppend("Check that user is able to login and Observe initial loading time",
								"" + GameName
										+ " Game should be launched and initial loading time should be less than 10 seconds",
								"" + GameName + " is launched successfully and initial loading time is " + loadingTime
										+ " Seconds",
								"Fail");
					}

					String currencySymbol = cfnlib.getCurrencySymbol();
					log.debug(CurrencyName + "currency symbol is " + currencySymbol);
					// to get current bet in the game
					String bet = cfnlib.getCurrentBet();
					double currentBet = Double.parseDouble(bet);

					// Taking default bet of the game from excel to calculate
					// multiplier
					// String defaultBet =
					// cfnLibrary_Desktop.XpathMap.get("DefaultBet");
					// double defaultBet1 = Double.parseDouble(defaultBet);

					// use below code if you don't want to take default bet from
					// excel.
					// below code will take bet of $ currency as default bet.

					if (CurrencyID.equals("0") || CurrencyID.equals("0.0")) {
						defaultBet1 = currentBet;
					}

					double currentMultiplier = currentBet / defaultBet1;

					// to verify if multiplier in game is same as actual
					// multiplier of currency
					if (multiplierValue == currentMultiplier) {
						currency.detailsAppendFolder(
								"Verify that currency Multiplier for currency is " + multiplierValue + " ",
								"Currency multiplier should be " + multiplierValue + " for this currency",
								"Currency multiplier is " + multiplierValue + " for this currency", "Pass", "");
					} else {
						currency.detailsAppendFolder(
								"Verify that currency Mutiplier for currency is " + multiplierValue + " ",
								"Currency multiplier should be " + multiplierValue + " for this currency",
								"Currency multiplier is " + currentMultiplier + " for this currency", "Fail", "");
					}
					// to verify currency symbol in bet console
					boolean b = cfnlib.betCurrencySymbol(currencySymbol);
					if (b) {
						currency.detailsAppendFolder("Verify that currency on the Bet Console",
								"Bet console should display " + currencySymbol + " currency symbol",
								"Bet console displays " + currencySymbol + " currency symbol", "Pass", CurrencyName);
					} else {
						currency.detailsAppendFolder("Verify that currency on the Bet Console",
								"Bet console should display " + currencySymbol + " currency symbol",
								"Bet console does not display " + currencySymbol + " currency symbol", "Fail",
								CurrencyName);
					}
					cfnlib.open_TotalBet();
					// to verify currency symbol in bet settings page
					boolean b2 = cfnlib.betSettingCurrencySymbol(currencySymbol,currency,CurrencyName);
					if (b2) {
						currency.detailsAppendFolder("Verify that currency on the Bet Settings Screen",
								"Total Bet and Coin Size should not display " + currencySymbol + " currency symbol",
								"Total Bet and Coin Size does not display " + currencySymbol + " currency symbol",
								"Pass", CurrencyName);
					} else {
						currency.detailsAppendFolder("Verify that currency on the Bet Settings Screen",
								"Total Bet and Coin Size should not display " + currencySymbol + " currency symbol",
								"Total Bet or Coin Size displays " + currencySymbol + " currency symbol", "Fail",
								CurrencyName);
					}
					cfnlib.close_TotalBet();

					// cfnlib.paytableOpen(currency);
					for (int bonusSelection = 1; bonusSelection <= 4; bonusSelection++) {
						cfnlib.waitForSpinButton();
						cfnlib.spinclick();
						String FreeSpinEntryScreen = cfnlib.XpathMap.get("FreeSpinEntryScreen");
						String str = cfnlib.entryScreen_Wait(FreeSpinEntryScreen);
						if (str.equalsIgnoreCase("freeSpin")) {
							cfnlib.clickBonusSelection(bonusSelection);
						} else {
							log.debug("Free Spin Entry screen is not present in Game");
						}

						cfnlib.FSSceneLoading(bonusSelection);
						// wait until win occurs and capture screenshot.
						boolean b3 = cfnlib.waitforWinAmount(str, currency, CurrencyName);
						if (b3) {
							currency.detailsAppendFolder("Verify free spin scene when win occurs",
									"Win in Free spin scene should display with currency",
									"Free Spin scene is displaying with currency", "Pass", CurrencyName);
						} else {
							currency.detailsAppendFolder("Verify free spin scene when win occurs",
									"Win in Free spin scene should display with currency",
									"Free Spin scene is not displaying with currency", "Fail", CurrencyName);
						}

						cfnlib.waitForSpinButton();
						webdriver.navigate().refresh();
						cfnlib.acceptAlert();
						cfnlib.waitSummaryScreen();

						currency.detailsAppendFolder("Verify that the application should display Free Spin Summary",
								"Free Spin Summary should display", "Free Spin Summary displays", "Pass", CurrencyName);
					}
					// Step 5: Logout From the Game
					String logout = cfnlib.Func_logout_OD();
					if (logout.trim() != null) {
						currency.detailsAppendFolder("Verify that user has successfully logged out ",
								"User should be successfully logged out", "User is Successfully logged out", "Pass",
								"");
					} else {
						currency.detailsAppendFolder("Verify that user has successfully logged out ",
								"User should be successfully logged out", "User is not Successfully logged out", "Fail",
								"");
					}
				}
			}
		}
		// -------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error(e.getMessage(),e);
		} finally {
			currency.endReport();
			if (destFile.delete()) {
				log.debug(destFile.getName() + " is deleted!");
			} else {
				log.debug("Test Data Delete operation is failed.");
			}
			webdriver.close();
			webdriver.quit();
			proxy.abort();
			Thread.sleep(1000);
		}
	}
}