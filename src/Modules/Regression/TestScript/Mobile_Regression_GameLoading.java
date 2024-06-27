package Modules.Regression.TestScript;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;

import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarLog;

public class Mobile_Regression_GameLoading {
	Logger log = Logger.getLogger(Mobile_Regression_CurrencySymbol_MiniPayTable.class.getName());
	public ScriptParameters scriptParameters;
	
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
		
		ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();

		Mobile_HTML_Report tc01=new Mobile_HTML_Report(webdriver,DeviceName,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,gameName);

		MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, tc01, gameName);
		

		
		try {
			if (webdriver != null) {
				webdriver.manage().deleteAllCookies();

				Thread.sleep(20000);
				proxy.newHar();
				//webdriver.get(
						//"https://mobilewebserver9-zensardev.installprogram.eu/MobileWebGames/game/mgs/9_3_0_6473?moduleID=10353&clientID=50300&gameName=gameOfThrones_WaysDesktop&gameTitle=Game%20of%20Thrones%E2%84%A2%20243%20Ways&LanguageCode=en&clientTypeID=40&casinoID=5007&lobbyName=IslandParadise&loginType=InterimUPE&lobbyURL=https://mobilewebserver9-ZensarDev.installprogram.eu/Lobby/en/IslandParadise/Games/3ReelSlots&xmanEndPoints=https://mobilewebserver9-ZensarDev.installprogram.eu/XMan/x.x&bankingURL=https://mobilewebserver9-ZensarDev.installprogram.eu/Lobby/en/IslandParadise/&routerEndPoints=&isPracticePlay=false&username=player87&password=test&host=Desktop&apicommsenabled=false&launchtoken=&version=");
				//webdriver.get("https://mobilewebserver9-zensardev.installprogram.eu/MobileWebGamesDesktop/game/mgs/5_16_3_0?moduleID=11091&clientID=50301&gameName=dragonDanceDesktop&gameTitle=Dragon%20Dance&LanguageCode=en&clientTypeID=40&casinoID=5007&lobbyName=IslandParadise&loginType=InterimUPE&lobbyURL=https://mobilewebserver9-ZensarQA.installprogram.eu/Lobby/en/IslandParadise/Games/3ReelSlots&xmanEndPoints=https://mobilewebserver9-ZensarQA.installprogram.eu/XMan/x.x&bankingURL=https://mobilewebserver9-ZensarQA.installprogram.eu/Lobby/en/IslandParadise/&routerEndPoints=&isPracticePlay=false&username=player85&password=test&host=Desktop&apicommsenabled=false&launchtoken=&version=");
				//webdriver.get("https://mobilewebserver9-zensardev.installprogram.eu/MobileWebGames/game/mgs/9_14_0_7270?lobbyURL=https%3A%2F%2Fmobilewebserver9-zensardev.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FGames%2F3ReelSlots&moduleID=10008&clientID=50301&gameName=5ReelDriveDesktop&gameTitle=5+Reel+Drive&LanguageCode=en&clientTypeID=40&casinoID=5007&lobbyName=IslandParadise&loginType=PlayerService&bankingURL=https%3A%2F%2Fmobilewebserver9-zensardev.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FBanking&xmanEndPoints=https%3A%2F%2Fmobilewebserver9-zensardev.installprogram.eu%2FXMan%2Fx.x&routerEndPoints=&disablePoweredBy=false&currencyFormat=&isPracticePlay=false&logoutURL=https%3A%2F%2Fmobilewebserver9-zensardev.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FLogout&host=Desktop&apiCommsEnabled=false&launchToken=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL2FjY291bnQvdjEiLCJjaWQiOiI1MDA3IiwianRpIjoiLTI4NjQ4NjM4NTc2NzU2MDE3OTciLCJvdWlkIjoiMHgxNzM4MDAwMCIsInN1YiI6IjIxYzQ4NWZhZWUzNTQ0ZjVhMWNhYzU4OGE2MTY1MTU4IiwiY2VydCI6Ik1HUy5Vc2VyU2Vzc2lvblRva2VuLkJsdWVtZXNhLjIwMTUwMzMxIiwiaWF0IjoiMTU3MTM5OTE2NSIsImF1ZCI6IkpvaW5Vc2VyU2Vzc2lvbiJ9.1xNKp8z9XhtVx2eL1i77hqzuPHLipDlWXbuoguIoJ8F7Ab2465tRgTEtSe3lU5M0byemsYxVkyUaYbfnjGM1wSfC8hgWeXOpcxaq4dShzmS0Ua3ECeJyap5Qf_isIbJlfBvoLbQfKrdFg-cnFGvMkKc5biaWYlzqMPD831dmFtg&gaClientId=1513979440.1565593852&activityStatementURL=undefined&username=player370&password=test");
				
				webdriver.get("https://mobilewebserver9-zensarqa2.installprogram.eu/MobileWebGames/game/mgs/9_15_0_7290?moduleID=10145&clientID=50300&gameName=immortalRomanceDesktop&gameTitle=Immortal%20Romance&LanguageCode=en&clientTypeID=40&casinoID=5007&lobbyName=IslandParadise&loginType=InterimUPE&lobbyURL=https://mobilewebserver9-ZensarQA2.installprogram.eu/Lobby/en/IslandParadise/Games/3ReelSlots&xmanEndPoints=https://mobilewebserver9-ZensarQA2.installprogram.eu/XMan/x.x&bankingURL=https://mobilewebserver9-ZensarQA2.installprogram.eu/Lobby/en/IslandParadise/Banking&routerEndPoints=&isPracticePlay=false&username=player787&password=test&host=Mobile&apicommsenabled=false&launchtoken=&version=");
				WebDriverWait wait = new WebDriverWait(webdriver, 1000);

				long startTime1 = System.currentTimeMillis();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='inGameClock']")));
				long endTime = System.currentTimeMillis();
				Har nhar = new Har(proxy.getHar().getLog());
				long endTime2 = System.currentTimeMillis();
				proxy.endHar();
				webdriver.close();
				webdriver.quit();
				log.debug("End time " + endTime);
				log.debug("Game has loaded successfully with in ::" + ((endTime - startTime1) / 1000) + "secs");
				log.debug("har creation rtime ::" + ((endTime2 - endTime) / 1000) + "secs");
				HarLog hardata = nhar.getLog();
				List<HarEntry> entries = hardata.getEntries();
				Iterator<HarEntry> itr = entries.iterator();

				long totalResponseSize = 0;

				while (itr.hasNext()) {
					HarEntry entry = itr.next();
					String requestUrl = entry.getRequest().getUrl();
					long size = entry.getResponse().getBodySize();
				//	if(size>10000){
					log.info("automation Logger" + requestUrl + "---> Size :: " + size);
					System.out.println("automation Logger" + requestUrl + "---> Size :: " + size);
				//	}
					/*if( size ==-1 && !requestUrl.contains("google")){
						break;
					}*/
					totalResponseSize += size;
					//log.info("automation Logger" + requestUrl + "---> Size :: " + size);
				}

				log.info("automation Logger Overall Game loading size" + totalResponseSize);

			}

		}
		// -------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error("Exception in CDN Cache busting", e);
			cfnlib.evalException(e);
		} finally {

		}
	}
}
