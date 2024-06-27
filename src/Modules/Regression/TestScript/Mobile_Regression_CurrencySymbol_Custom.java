package Modules.Regression.TestScript;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.DataBaseFunctions;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import net.lightbody.bmp.BrowserMobProxyServer;

public class Mobile_Regression_CurrencySymbol_Custom {
Logger log = Logger.getLogger(Mobile_Regression_CurrencySymbol_Custom .class.getName());
	
	public ScriptParameters scriptParameters;

	//-------------------Main script defination---------------------//
	public void script() throws Exception {

		String mstrTC_Name=scriptParameters.getMstrTCName();
		String mstrTC_Desc=scriptParameters.getMstrTCDesc();
		String mstrModuleName=scriptParameters.getMstrModuleName();
		BrowserMobProxyServer proxy=scriptParameters.getProxy();
		String startTime=scriptParameters.getStartTime();
		String filePath=scriptParameters.getFilePath();
		
		//AndroidDriver<WebElement> webdriver=scriptParameters.getWebdriver();
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
		String classname= this.getClass().getSimpleName();

		 String xmlFilePath = System.getProperty("user.dir") + "\\src\\Modules\\Regression\\TestData\\GameData\\"
				+ classname + ".testdata";
		File sourceFile = new File(xmlFilePath);
		File destFile=null;
		double defaultBet1=0.0;
		ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();
		
		webdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		Mobile_HTML_Report currency=new Mobile_HTML_Report(webdriver,DeviceName,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,gameName);
		CommonUtil util=new CommonUtil();

		DataBaseFunctions createplayer=new DataBaseFunctions("zensardev2","Casino","10.247.208.107","1402","5001");
		MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, currency, gameName);
		

		try{
			//-------------------Getting webdriver of proper test site---------------------//
			if(webdriver!=null)
			{		
				Map<String, String> rowData1 = null;
				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				int rowCount1 = excelpoolmanager.rowCount(testDataExcelPath, "CurrencySymbol");
				for(int j=1;j<rowCount1;j++){
					rowData1 =  excelpoolmanager.readExcelByRow(testDataExcelPath,"CurrencySymbol", j);
					String CurrencyID =  rowData1.get("CurrencyID").toString(); 
					//double CurrencyID1 = Double.parseDouble(CurrencyID);
					//currency.details_append_folder("Verify game for currency","Game should work with currency", "", "");

					String CurrencyName = rowData1.get("Name").toString().trim();	
					// currency.details_append_folder("Verify game for " +CurrencyName+" ", "Game should work with " +CurrencyName+" ", "", "");

					String multiplier = rowData1.get("Multiplier").toString();
					double multiplierValue = Double.parseDouble(multiplier);

					userName = util.randomStringgenerator();
					System.out.println("The New username is ==" + userName);
					createplayer.createUser(userName, CurrencyID, 0);

					/* Casinoas1 ip for qa2 */
					//destFile = new File("\\\\10.247.225.0\\C$\\MGS_TestData\\" + classname + "_" + GameName +".testdata");
					/* Casinoas1 ip for qa */
					//destFile = new File("\\\\10.247.224.110\\C$\\MGS_TestData\\" +classname + "_" + GameName +".testdata");
					/* Casinoas1 ip for dev */
					//destFile = new File("\\\\10.247.224.22\\c$\\MGS_TestData\\"+classname + "_" + GameName +".testdata");
					/* CAsinoas1 ip for dev2*/
					destFile = new File("\\\\10.247.224.239\\c$\\MGS_TestData\\"+classname + "_" + userName +".testdata");
					// ----Update the Xml File of Papyrus Data----------//
					util.changePlayerName(userName, xmlFilePath);

					// -----Copy the Papyrus Data to The CasinoAs1 Server---------//
					util.copyFolder(sourceFile, destFile);

					String url = cfnlib.xpathMap.get("ApplicationURL");
					cfnlib.funcNavigate(url);
					currency.detailsAppendFolder("Verify game for "+CurrencyName+" currency","Game should work with "+CurrencyName+" currency", "", "", CurrencyName);
					/*if (obj != null) {
					 currency.details_append_folder("Open Browser and Enter Lobby URL in address bar and click Enter","Island Paradise Lobby should open", "Island Paradise lobby is Open", "Pass",CurrencyName);
				} else {
					 currency.details_append_folder("Open browser and Enter Lobby URL in address bar and click Enter","Island Paradise lobby should Open", "Island paradise is not displayed", "Fail",CurrencyName);
				}*/
					long loadingTime = cfnlib.Random_LoginBaseScene(userName);
					if (loadingTime < 10.0) {
						currency.detailsAppendFolder("Check that user is able to login and Observe initial loading time","" + gameName + " Game should be launched and initial loading time should be less than 10 seconds","" + gameName + " is launched successfully and initial loading time is "+loadingTime+" Seconds", "Pass","");
					} else {
						currency.detailsAppendFolder("Check that user is able to login and Observe initial loading time","" + gameName + " Game should be launched and initial loading time should be less than 10 seconds","" + gameName + " is launched successfully and initial loading time is "+loadingTime+" Seconds", "Fail","");
					}
					cfnlib.funcFullScreen();

					//currency.details_append_folder("Verify Base Scene for "+CurrencyName+" currency","" + GameName + " Should display in "+CurrencyName+" currency","Game is displaying in "+CurrencyName+" currency", "Pass",CurrencyName);

					String currencySymbol = cfnlib.getCurrencySymbol();
					System.out.println(CurrencyName + "currency symbol is " +currencySymbol);


					//to get current bet in the game
					String bet = cfnlib.getCurrentBet();
					double currentBet = Double.parseDouble(bet);

					//Taking default bet of the game from excel to calculate multiplier
					//String defaultBet = cfnLibrary_Mobile.XpathMap.get("DefaultBet");
					//double defaultBet1 = Double.parseDouble(defaultBet);

					//use below code if you don't want to take default bet from excel.
					//below code will take bet of $ currency as default bet.
					if(CurrencyID.equals("0") || CurrencyID.equals("0.0")){
						defaultBet1 = currentBet;
					}

					double currentMultiplier = currentBet/defaultBet1; 
					//to verify if multiplier in game is same as actual multiplier of currency
					if(multiplierValue==currentMultiplier){
						currency.detailsAppendFolder("Verify that currency Mutiplier for currency is "+multiplierValue+" ", "Currency multiplier should be "+multiplierValue+" for this currency", "Currency multiplier is "+multiplierValue+" for this currency", "Pass","");
					}
					else{
						currency.detailsAppendFolder("Verify that currency Mutiplier for currency is "+multiplierValue+" ", "Currency multiplier should be "+multiplierValue+" for this currency", "Currency multiplier is "+currentMultiplier+" for this currency", "Fail","");
					}

					boolean b = cfnlib.betCurrencySymbol(currencySymbol);
					if(b){
						currency.detailsAppendFolder("Verify that currency on the Bet Console", "Bet console should display "+currencySymbol+" currency symbol", "Bet console displays "+currencySymbol+" currency symbol", "Pass", CurrencyName);
					}
					else{
						currency.detailsAppendFolder("Verify that currency on the Bet Console", "Bet console should display "+currencySymbol+" currency symbol", "Bet console does not display "+currencySymbol+" currency symbol", "Fail", CurrencyName);
					}

					cfnlib.open_Bet();
					//to verify currency symbol in bet settings page
					boolean b2 = cfnlib.betSettingCurrencySymbol(currencySymbol,currency, bet);
					if(b2){
						currency.detailsAppendFolder("Verify that currency on the Bet Settings Screen", "Total Bet and Coin Size should not display "+currencySymbol+" currency symbol", "Total Bet and Coin Size does not display "+currencySymbol+" currency symbol", "Pass",CurrencyName);
					}
					else{
						currency.detailsAppendFolder("Verify that currency on the Bet Settings Screen", "Total Bet and Coin Size should not display "+currencySymbol+" currency symbol", "Total Bet or Coin Size displays "+currencySymbol+" currency symbol", "Fail",CurrencyName);
					}
					cfnlib.closeTotalBet();

					//cfnlib.paytableOpen(currency);
					for(int bonusSelection=1;bonusSelection<=4;bonusSelection++)
					{
					cfnlib.waitForSpinButton();
					cfnlib.spinclick();
					String FreeSpinEntryScreen = cfnlib.xpathMap.get("FreeSpinEntryScreen");
					String str = cfnlib.entryScreen_Wait(FreeSpinEntryScreen);
					if(str.equalsIgnoreCase("freeSpin")) {
						cfnlib.clickBonusSelection(bonusSelection);
					}
					else{
						System.out.println("Free Spin Entry screen is not present in Game");
					}

					cfnlib.FSSceneLoading(bonusSelection);
					//wait until win occurs and capture screenshot.
					boolean b3 = cfnlib.waitforWinAmount(currencySymbol, currency, str);
					if(b3)
					{
						currency.detailsAppendFolder("Verify free spin scene when win occurs", "Win in Free spin scene should display with currency", "Free Spin scene is displaying with currency", "Pass", CurrencyName);
					}
					else
					{
						currency.detailsAppendFolder("Verify free spin scene when win occurs", "Win in Free spin scene should display with currency", "Free Spin scene is not displaying with currency", "Fail", CurrencyName);
					}

					cfnlib.waitForSpinButton();
					webdriver.navigate().refresh();
					cfnlib.acceptAlert();
					cfnlib.waitSummaryScreen();

					currency.detailsAppendFolder("Verify that the application should display Free Spin Summary","Free Spin Summary should display","Free Spin Summary displays","Pass", CurrencyName);
					}
					cfnlib.waitForSpinButton();
					//Step 5: Logout From the Game
					String logout= cfnlib.Func_logout_OD();
					if(!logout.isEmpty())
					{
						//currency.details_append_folder("Verify that user has successfully logged out ", "User should be successfully logged out", "User is Successfully logged out", "Pass",CurrencyName);
					}
					else 
					{
						currency.detailsAppendFolder("Verify that user has successfully logged out ", "User should be successfully logged out", "User is not Successfully logged out", "Fail",CurrencyName);
					}
				}				
			}		
		}//TRY
		//-------------------Handling the exception---------------------//
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			currency.endReport();
			if(destFile.delete()){
				System.out.println(destFile.getName() + " is deleted!");
			}else{
				System.out.println("Delete operation is failed.");
			}
			webdriver.close();
			webdriver.quit();
			//Global.appiumService.stop();
		}
	}
}