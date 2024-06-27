package Modules.Regression.TestScript;

import java.io.File;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop_CS;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop_CS_Renovate;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop_Force;
import net.lightbody.bmp.BrowserMobProxyServer;

public class CurrencySymbolTest {
	public ExcelDataPoolManager excelpoolmanager = new ExcelDataPoolManager();
	public String path = System.getProperty("user.dir");
	public WebDriver webdriver;
	public BrowserMobProxyServer proxy;
	public String classname = this.getClass().getSimpleName();
	public String mstrTC_Name, mstrTC_Desc, mstrModuleName;
	public String filePath;
	public String startTime;
	Map<String, String> XpathMap;
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
	
	
	public String destFile;
	public static Logger log = Logger.getLogger(Desktop_Regression_CurrencySymbol.class.getName());
	public ScriptParameters scriptParameters;
	
	public void script() throws Exception {
		
		String mstrTC_Name=scriptParameters.getMstrTCName();
		String mstrTC_Desc=scriptParameters.getMstrTCDesc();
		String mstrModuleName=scriptParameters.getMstrModuleName();
		WebDriver webdriver=scriptParameters.getDriver();
		BrowserMobProxyServer proxy=scriptParameters.getProxy();
		String startTime=scriptParameters.getStartTime();
		String filePath=scriptParameters.getFilePath();
		String username=scriptParameters.getUserName();
		String Browsername=scriptParameters.getBrowserName();
		String Framework=scriptParameters.getFramework();
		String GameName=scriptParameters.getGameName();
		String languageCode=null;
		String Status=null;
		int mintDetailCount=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		
		
		
		String xmlFilePath = TestPropReader.getInstance().getProperty("CurrencyTestDataPath");
		File sourceFile = new File(xmlFilePath);
		
		Desktop_HTML_Report currencyReport = new Desktop_HTML_Report(webdriver, Browsername, filePath, startTime, mstrTC_Name,
				mstrTC_Desc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings, mintStepNo, Status,
				GameName);
		CFNLibrary_Desktop cfnlib = null;
		username = "Zen_IEP";
		languageCode = "en";
		
		long loadingTime = 0;
		boolean isGameLoaded = true;

		if (Framework.equalsIgnoreCase("PN")) {
			cfnlib = new CFNLibrary_Desktop(webdriver, proxy, currencyReport, GameName);
		} else if (Framework.equalsIgnoreCase("Force")) {
			cfnlib = new CFNLibrary_Desktop_Force(webdriver, proxy, currencyReport, GameName);
		} else if (Framework.equalsIgnoreCase("CS_Renovate")) {
			cfnlib = new CFNLibrary_Desktop_CS_Renovate(webdriver, proxy, currencyReport, GameName);
		} else {
			cfnlib = new CFNLibrary_Desktop_CS(webdriver, proxy, currencyReport, GameName);
		}


		/*DataBaseFunctions dbobject = new DataBaseFunctions(TestPropReader.getInstance().getProperty("ServerName"),
				TestPropReader.getInstance().getProperty("dataBaseName"),
				TestPropReader.getInstance().getProperty("serverIp"), TestPropReader.getInstance().getProperty("port"),
				TestPropReader.getInstance().getProperty("serverID"));*/
		String url = "https://mobilewebserver9-zensarqa2.installprogram.eu/MobileWebGames/game/mgs/9_15_0_7290?moduleID=10145&clientID=50300&gameName=immortalRomanceDesktop&gameTitle=Immortal%20Romance&LanguageCode=en&clientTypeID=40&casinoID=5007&lobbyName=IslandParadise&loginType=InterimUPE&lobbyURL=https://mobilewebserver9-ZensarQA2.installprogram.eu/Lobby/en/IslandParadise/Games/3ReelSlots&xmanEndPoints=https://mobilewebserver9-ZensarQA2.installprogram.eu/XMan/x.x&bankingURL=https://mobilewebserver9-ZensarQA2.installprogram.eu/Lobby/en/IslandParadise/Banking&routerEndPoints=&isPracticePlay=false&username="+username+"&password=test&host=Desktop&apicommsenabled=false&launchtoken=&version=";
	
		webdriver.navigate().to(url);
		WebDriverWait wait = new WebDriverWait(webdriver, 50);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='continueButton continueButtonFadeIn']")));
		 
		if(Framework.equalsIgnoreCase("Force")){
			cfnlib.setNameSpace();
			}
		 //new feature click
		 webdriver.findElement(By.xpath("//*[@class='continueButton continueButtonFadeIn']")).click();
		cfnlib.waitForSpinButton();
		for(int i=1; i<=4; i++){
		 cfnlib.spinclick();
		 log.debug("clicked on spin butoon");
		 cfnlib.clickBonusSelection(i);
		 log.debug("bonus selected for ="+i);
		 log.debug("openpaytable while freespin");
		 Thread.sleep(3000);
		 cfnlib.capturePaytableScreenshot(currencyReport,languageCode );
		 
		 cfnlib.paytableClose();
		 log.debug("openstory while freespin");
		 Thread.sleep(4000);
		// cfnlib.Verifystoryoptioninpaytable(currencyReport, languageCode);
		 cfnlib.paytableClose();
		 cfnlib.waitSummaryScreen();
		 for(int j =1;j<=4;j++){
			 cfnlib.spinclick();
			 cfnlib.clickBonusSelection(i);
			 cfnlib.waitSummaryScreen();
			 cfnlib.waitForSpinButton();
		 }
		 
		}
		 
		 
		 

	}
}

