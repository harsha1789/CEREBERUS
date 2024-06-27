package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.DataBaseFunctions;
import com.zensar.automation.library.TestPropReader;
import com.zensar.automation.model.Currency;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop_CS;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop_CS_Renovate;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop_Force;
import net.lightbody.bmp.BrowserMobProxyServer;
/**
 * This script will unlock all bonus of Immortal Romance game
 * Input currencies from test data file.
 * 
 * @author Snehal Gaikwad
 *
 */
public class ImmoratalRomanceBonus{
	public ExcelDataPoolManager excelpoolmanager = new ExcelDataPoolManager();
	public String path = System.getProperty("user.dir");
	public WebDriver webdriver;
	public BrowserMobProxyServer proxy;
	public String classname = this.getClass().getSimpleName();
	public String mstrTC_Name, mstrTC_Desc, mstrModuleName;
	public String filePath;
	public String startTime;
	
	public int mintDetailCount;
	public int mintSubStepNo;
	public int mintPassed;
	public int mintFailed;
	public int mintWarnings;
	public int mintStepNo;
	public String Status;
	public String Browsername;
	public String userName;
	double defaultBet1 = 0;
	public String Framework;
	public String GameName;
	public String xmlFilePath = TestPropReader.getInstance().getProperty("CurrencyTestDataPath");
	public File sourceFile = new File(xmlFilePath);
	public String destFile;
	public static Logger log = Logger.getLogger(Desktop_Regression_CurrencySymbol.class.getName());
	static ArrayList<Currency> currencyList =null;

	public void script() throws Exception {
		Desktop_HTML_Report currencyReport = new Desktop_HTML_Report(webdriver, Browsername, "D:\\ScreenShots\\", startTime, mstrTC_Name,
				mstrTC_Desc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings, mintStepNo, Status,
				GameName);
		CFNLibrary_Desktop cfnlib = null;
		WebDriverWait wait=new WebDriverWait(webdriver, 180);
		long loadingTime = 0;
		boolean isGameLoaded = true;
		String forceNamespace=null;
		
		
		if (Framework.equalsIgnoreCase("PN")) {
			cfnlib = new CFNLibrary_Desktop(webdriver, proxy, currencyReport, GameName);
		} else if (Framework.equalsIgnoreCase("Force")) {
			cfnlib = new CFNLibrary_Desktop_Force(webdriver, proxy, currencyReport, GameName);
		} else if (Framework.equalsIgnoreCase("CS_Renovate")) {
			cfnlib = new CFNLibrary_Desktop_CS_Renovate(webdriver, proxy, currencyReport, GameName);
		} else {
			cfnlib = new CFNLibrary_Desktop_CS(webdriver, proxy, currencyReport, GameName);
		}
	
		String FreeSpinEntryScreen = cfnlib.XpathMap.get("FreeSpinEntryScreen");
		CommonUtil util = new CommonUtil();

		DataBaseFunctions dbobject = new DataBaseFunctions(TestPropReader.getInstance().getProperty("ServerName"),
				TestPropReader.getInstance().getProperty("dataBaseName"),
				TestPropReader.getInstance().getProperty("serverIp"), TestPropReader.getInstance().getProperty("port"),
				TestPropReader.getInstance().getProperty("serverID"));

		try {
			// -------Getting webdriver of proper test site-------//
			if (webdriver != null) {
			

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
				
			
				//   int i=0;i<currencyList.size();i++
				
				Thread.sleep(new Random().nextInt(20)*1000);
				
				for (Currency currency:currencyList ) 
				{
					if(currency.getStatus().equalsIgnoreCase("Open")){
						currency.setStatus("Close");
						
						System.out.println(this+"I am processing cuurencyy"+currency.getIsoCode());
					try{
						
							
							String CurrencyID = currency.getCurrencyID();
							

							String CurrencyName = currency.getIsoName().trim();
							String multiplier = currency.getCurrencyMultiplier();
							String currencyFormat=currency.getCurrencyFormat();
							double multiplierValue = Double.parseDouble(multiplier);

							

							String isoCode = currency.getIsoCode().toString().trim();
							userName="Zen_"+isoCode.trim();
							
							
							 String Url="https://mobilewebserver9-zensardev.installprogram.eu/MobileWebGames/game/mgs/9_17_2_7470?moduleID=10145&clientID=50300&gameName=immortalRomanceDesktop&gameTitle=Immortal%20Romance&LanguageCode=en&clientTypeID=40&casinoID=5007&lobbyName=IslandParadise&loginType=InterimUPE&lobbyURL=https://mobilewebserver9-ZensarDev.installprogram.eu/Lobby/en/IslandParadise/Games/3ReelSlots&xmanEndPoints=https://mobilewebserver9-ZensarDev.installprogram.eu/XMan/x.x&bankingURL=https://mobilewebserver9-ZensarDev.installprogram.eu/Lobby/en/IslandParadise/Banking&routerEndPoints=&isPracticePlay=false&username=player45&password=test&host=Desktop&apicommsenabled=true&launchtoken=&version=";

							
							String url_new = Url.replaceAll("username=player45", "username="+userName);
							cfnlib.func_navigate(url_new);
							if(Framework.equalsIgnoreCase("Force")){
								cfnlib.setNameSpace();
							}
							cfnlib.newFeature();
							
							cfnlib.waitForSpinButton();
								// below if block is for immortal romance
								if(GameName.equalsIgnoreCase("immortalRomanceDesktop"))
								{
									
									for(int i=1; i<=4; i++)
									{
										 cfnlib.spinclick();
										 log.debug("clicked on spin butoon");
										 cfnlib.entryScreen_Wait(FreeSpinEntryScreen);
										 cfnlib.clickBonusSelection(i);
										 log.debug("bonus selected for ="+i);
										 log.debug("openpaytable while freespin");
										 System.out.println("Paytable Opening");
										 Thread.sleep(3000);
										 cfnlib.capturePaytableScreenshot(currencyReport,CurrencyName+"/"+ i );
										 
										 cfnlib.paytableClose();
										 cfnlib.waitSummaryScreen();
										 //mgs.mobile.casino.slotbuilder.v1.automation.getControlById('BonusSelectionComponent').isGoldenActivated
										 if(!(cfnlib.GetConsoleBooleanText("return "+forceNamespace+".getControlById('BonusSelectionComponent').isGoldenActivated")))
										 {
											 for(int j =1;j<=4;j++){
										 
											 cfnlib.waitForSpinButton();
											 cfnlib.spinclick();
											 cfnlib.entryScreen_Wait(FreeSpinEntryScreen);
											 cfnlib.clickBonusSelection(1);
											 Thread.sleep(2000);
											 cfnlib.waitSummaryScreen();
											 cfnlib.waitForSpinButton();
										 }
										 }
										}
								}
																		
					}//try 
					catch ( WebDriverException e) {

						cfnlib.evalException(e);

					}
					finally{
						
						webdriver.navigate().refresh();
					}
					

					}else{
						System.out.println("I am skipping this currency as it is not open::"+currency.getIsoCode());
						}
					
				}//for 
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