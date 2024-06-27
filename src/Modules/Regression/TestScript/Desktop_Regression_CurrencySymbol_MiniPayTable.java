package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.utils.Util;
import com.zensar.automation.library.DataBaseFunctions;
import com.zensar.automation.library.TestPropReader;
import com.zensar.automation.model.Currency;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;
/**
 * This currency script verifies  Multiplier, Navigation in Base Scene, Bet Console, Free Spin Scene and Free Spin Summary Screen.
 * Input currencies from test data file.
 * 
 * @author Havish Jain
 *
 */
public class Desktop_Regression_CurrencySymbol_MiniPayTable {
	
	public static Logger log = Logger.getLogger(Desktop_Regression_CurrencySymbol_MiniPayTable.class.getName());
	static ArrayList<Currency> currencyList =null;
	public ScriptParameters scriptParameters;

	public void script() throws Exception {

		String mstrTC_Name=scriptParameters.getMstrTCName();
		String mstrTC_Desc=scriptParameters.getMstrTCDesc();
		String mstrModuleName=scriptParameters.getMstrModuleName();
		WebDriver webdriver=scriptParameters.getDriver();
		BrowserMobProxyServer proxy=scriptParameters.getProxy();
		String startTime=scriptParameters.getStartTime();
		String filePath=scriptParameters.getFilePath();
		String userName=scriptParameters.getUserName();
		String Browsername=scriptParameters.getBrowserName();
		String framework=scriptParameters.getFramework();
		String gameName=scriptParameters.getGameName();
		
		String Status=null;
		int mintDetailCount=0;
		//int mintSubStepNo=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		
		String xmlFilePath = TestPropReader.getInstance().getProperty("CurrencyTestDataPath");
		File sourceFile = new File(xmlFilePath);
		String destFile=null;
		double defaultBet1 = 0;
		
		Desktop_HTML_Report currencyReport = new Desktop_HTML_Report(webdriver, Browsername, "D:\\ScreenShots\\", startTime, mstrTC_Name,
				mstrTC_Desc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings, mintStepNo, Status,
				gameName);
		
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, currencyReport, gameName);

		
		WebDriverWait wait=new WebDriverWait(webdriver, 180);
		long loadingTime = 0;
		boolean isGameLoaded = true;
		
		Util util = new Util();

		DataBaseFunctions dbobject = new DataBaseFunctions(TestPropReader.getInstance().getProperty("ServerName"),
				TestPropReader.getInstance().getProperty("dataBaseName"),
				TestPropReader.getInstance().getProperty("serverIp"), TestPropReader.getInstance().getProperty("port"),
				TestPropReader.getInstance().getProperty("serverID"));

		try {
			// -------Getting webdriver of proper test site-------//
			if (webdriver != null) {
				Map<String, String> rowData1 = null;

				//int rowCount1 = excelpoolmanager.rowCount(Constant.TESTDATA_EXCEL_PATH, "CurrencySymbol");

				// Read the currency from Database
			
				
				String strStart = cfnlib.XpathMap.get("start");
				int start = (int) Double.parseDouble(strStart);
				
				String strEnd = cfnlib.XpathMap.get("end");
				int end = (int) Double.parseDouble(strEnd);
				
				currencyList=dbobject.getCurrencyData(start,end);
				int currencysize=currencyList.size();

				if(currencysize==0)
				{
					log.error("Error While reading the currencies from datbase");
				}
				
				String url = cfnlib.XpathMap.get("ApplicationURL");
			
				//   int i=0;i<currencyList.size();i++
				
				Thread.sleep(new Random().nextInt(20)*1000);
				
				for (Currency currency:currencyList ) 
				{
					if(currency.getStatus().equalsIgnoreCase("Open")){
						currency.setStatus("Close");
						
						System.out.println(this+"I am processing cuurency"+currency.getIsoCode());
					try{
						
							String CurrencyID = currency.getCurrencyID();
							String CurrencyName = currency.getIsoName().trim();
							String multiplier = currency.getCurrencyMultiplier();
							String currencyFormat=currency.getCurrencyFormat();
							double multiplierValue = Double.parseDouble(multiplier);

							

							String isoCode = currency.getIsoCode().toString().trim();
							//userName="Zen_"+isoCode.trim();
							
							String gameNamenew=TestPropReader.getInstance().getProperty("GameName");
							String capGameName = gameNamenew.replaceAll("[a-z0-9]", "").trim();
							userName = "Zen" + capGameName + "_" + currency.getIsoCode().trim();
							
							
							CurrencyName=CurrencyName.concat(isoCode);
							
							String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
							System.out.println("url = " +LaunchURl);
							
							cfnlib.loadGame(LaunchURl);
							
							//String url_new = url.replaceAll("username=player45", "username="+userName);
							//cfnlib.func_navigate(url_new);
							currencyReport.detailsAppend("Verifed  currency name "+CurrencyName, "Verifed  currency name "+CurrencyName,"Verifed  currency name"+CurrencyName,"pass");
							if (isGameLoaded) {
								
								//below commented code is for checking loading time with Random_LoginBaseScene () function
								/*if (loadingTime < 10.0) {
									currencyReport.details_append_folder(
											"Check that user is able to login and Observe initial loading time",
											"" + GameName
											+ " Game should be launched and initial loading time should be less than 10 seconds",
											"" + GameName + " is launched successfully and initial loading time is "
													+ loadingTime + " Seconds",
													"Pass", CurrencyName);
								} else {
									currencyReport.details_append_folder(
											"Check that user is able to login and Observe initial loading time",
											"" + GameName
											+ " Game should be launched and initial loading time should be less than 10 seconds",
											"" + GameName + " is launched successfully and initial loading time is "
													+ loadingTime + " Seconds",
													"Fail", CurrencyName);
								}*/
								if(framework.equalsIgnoreCase("Force")){
									cfnlib.setNameSpace();
								}
								cfnlib.newFeature();
								
								
								
								Thread.sleep(3000);

								cfnlib.setMaxBet();
								cfnlib.miniPaytableScreeShots(currencyReport,CurrencyName);
								}
								
								

						
					}//try 
					catch ( WebDriverException e) {

						cfnlib.evalException(e);

					}
					finally{
						
						webdriver.navigate().refresh();
						cfnlib.waitForSpinButton();
					}
					//prevUsername=userName;

				}else{
					System.out.println("I am skipping this currency as it is not open::"+currency.getIsoCode());
					}
					
				}// For 
			}
		}
		// -------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error(e.getMessage(), e);
			cfnlib.evalException(e);
		} finally {
			currencyReport.endReport();

			//Remove the test data that copied
			if(destFile!=null && !destFile.equalsIgnoreCase("")){
				util.deleteFile(destFile);
			}

			webdriver.close();
			webdriver.quit();
			proxy.abort();
			Thread.sleep(1000);
		}
	}
}