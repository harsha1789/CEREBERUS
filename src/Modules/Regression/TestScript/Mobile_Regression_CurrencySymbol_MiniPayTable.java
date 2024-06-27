package Modules.Regression.TestScript;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.DataBaseFunctions;
import com.zensar.automation.library.TestPropReader;
import com.zensar.automation.model.Currency;

import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import net.lightbody.bmp.BrowserMobProxyServer;
/**
 * This currency script verifies  Multiplier, Navigation in Base Scene, Bet Console, Free Spin Scene and Free Spin Summary Screen.
 * Input currencies from test data file.
 * 
 * @author Havish Jain
 *
 */
public class Mobile_Regression_CurrencySymbol_MiniPayTable {
	Logger log = Logger.getLogger(Mobile_Regression_CurrencySymbol_MiniPayTable.class.getName());
	public ScriptParameters scriptParameters;
	static ArrayList<Currency> currencyList =null;
	
	public void script() throws Exception {
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
		
		ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();

		Mobile_HTML_Report currencyreport=new Mobile_HTML_Report(webdriver,DeviceName,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,gameName);

		MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, currencyreport, gameName);
		

		CommonUtil util = new CommonUtil();
		
		DataBaseFunctions dbobject = new DataBaseFunctions(TestPropReader.getInstance().getProperty("ServerName"),
				TestPropReader.getInstance().getProperty("dataBaseName"),
				TestPropReader.getInstance().getProperty("serverIp"), TestPropReader.getInstance().getProperty("port"),
				TestPropReader.getInstance().getProperty("serverID"));
		
	
		try {
			// -------Getting webdriver of proper test site-------//
			if (webdriver != null) {
				Map<String, String> rowData1 = null;

				

				// Read the currency from Database
			
				
				String strStart = cfnlib.xpathMap.get("start");
				int start = (int) Double.parseDouble(strStart);
				
				String strEnd = cfnlib.xpathMap.get("end");
				int end = (int) Double.parseDouble(strEnd);
				
				currencyList=dbobject.getCurrencyData(start,end);
				int currencysize=currencyList.size();

				if(currencysize==0)
				{
					log.error("Error While reading the currencies from datbase");
				}
				
				String url = cfnlib.xpathMap.get("ApplicationURL");
			
				//   int i=0;i<currencyList.size();i++
				
				Thread.sleep(new Random().nextInt(20)*1000);
				
				for (Currency currency:currencyList ) 
				{
					if(currency.getStatus().equalsIgnoreCase("Open")){
						currency.setStatus("Close");
						
						System.out.println(this+"I am processing cuurency"+currency.getIsoCode());
					try{
						
							//rowData1 = excelpoolmanager.readExcelByRow(Constant.TESTDATA_EXCEL_PATH, "CurrencySymbol", j);
							String CurrencyID = currency.getCurrencyID();
							

							String CurrencyName = currency.getIsoName().trim();
							String multiplier = currency.getCurrencyMultiplier();
							String currencyFormat=currency.getCurrencyFormat();
							double multiplierValue = Double.parseDouble(multiplier);

							

							String isoCode = currency.getIsoCode().toString().trim();
							
							String gameNameNew=TestPropReader.getInstance().getProperty("GameName");
							String capGameName = gameNameNew.replaceAll("[a-z0-9]", "").trim();
							 userName = "Zen" + capGameName + "_" + currency.getIsoCode().trim();
							
							
							CurrencyName=CurrencyName.concat(isoCode);
							
							
							String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
							System.out.println("url = " +LaunchURl);
							log.info("url = " +LaunchURl);
							cfnlib.loadGame(LaunchURl);
							
							
							
							//uncomment the below block code if your using game loadding from lobby
							/*cfnlib.Func_navigate(url_new);
							currencyReport.details_append("Verifed  currency name "+CurrencyName, "Verifed  currency name "+CurrencyName,"Verifed  currency name"+CurrencyName,"pass");
							if (isGameLoaded) {
								
								//below commented code is for checking loading time with Random_LoginBaseScene () function
								if (loadingTime < 10.0) {
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
								if(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame")))
								{
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
								cfnlib.setMaxBet();
								cfnlib.miniPaytableScreeShots(currencyreport,CurrencyName);
					
					}//try 
					catch ( WebDriverException e) {

						cfnlib.evalException(e);

					}
					finally{
						
						webdriver.navigate().refresh();
						if(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.xpathMap.get("TypeOfGame"))||(Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad"))))

						{
							cfnlib.newFeature();
						}
						else{
						cfnlib.waitForSpinButton();
						}
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
			currencyreport.endReport();
			}

			webdriver.close();
			webdriver.quit();
			proxy.abort();
			Thread.sleep(1000);
		}
	}
