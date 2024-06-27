package Modules.Regression.TestScript;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarLog;

public class Desktop_Regression_GameLoading {
	Logger log = Logger.getLogger(Desktop_Regression_GameLoading.class.getName());
	public ScriptParameters scriptParameters;

	public void script() throws Exception {
		String mstrTC_Name=scriptParameters.getMstrTCName();
		String mstrTC_Desc=scriptParameters.getMstrTCDesc();
		String mstrModuleName=scriptParameters.getMstrModuleName();
		WebDriver webdriver=scriptParameters.getDriver();
		BrowserMobProxyServer proxy=scriptParameters.getProxy();
		String startTime1=scriptParameters.getStartTime();
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
		
		Desktop_HTML_Report basescene = new Desktop_HTML_Report(webdriver, Browsername, filePath, startTime1,
				mstrTC_Name, mstrTC_Desc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings,
				mintStepNo, Status, GameName);
		CFNLibrary_Desktop cfnlib = null;

		cfnlib = new CFNLibrary_Desktop(webdriver, proxy, basescene, GameName);

		try {
			if (webdriver != null) {
				webdriver.manage().deleteAllCookies();

				Thread.sleep(20000);
				proxy.newHar();
				// webdriver.get(
				// "https://mobilewebserver9-zensardev.installprogram.eu/MobileWebGames/game/mgs/9_3_0_6473?moduleID=10353&clientID=50300&gameName=gameOfThrones_WaysDesktop&gameTitle=Game%20of%20Thrones%E2%84%A2%20243%20Ways&LanguageCode=en&clientTypeID=40&casinoID=5007&lobbyName=IslandParadise&loginType=InterimUPE&lobbyURL=https://mobilewebserver9-ZensarDev.installprogram.eu/Lobby/en/IslandParadise/Games/3ReelSlots&xmanEndPoints=https://mobilewebserver9-ZensarDev.installprogram.eu/XMan/x.x&bankingURL=https://mobilewebserver9-ZensarDev.installprogram.eu/Lobby/en/IslandParadise/&routerEndPoints=&isPracticePlay=false&username=player87&password=test&host=Desktop&apicommsenabled=false&launchtoken=&version=");
				// webdriver.get("https://mobilewebserver9-zensardev.installprogram.eu/MobileWebGamesDesktop/game/mgs/5_16_3_0?moduleID=11091&clientID=50301&gameName=dragonDanceDesktop&gameTitle=Dragon%20Dance&LanguageCode=en&clientTypeID=40&casinoID=5007&lobbyName=IslandParadise&loginType=InterimUPE&lobbyURL=https://mobilewebserver9-ZensarQA.installprogram.eu/Lobby/en/IslandParadise/Games/3ReelSlots&xmanEndPoints=https://mobilewebserver9-ZensarQA.installprogram.eu/XMan/x.x&bankingURL=https://mobilewebserver9-ZensarQA.installprogram.eu/Lobby/en/IslandParadise/&routerEndPoints=&isPracticePlay=false&username=player85&password=test&host=Desktop&apicommsenabled=false&launchtoken=&version=");
				// webdriver.get("https://mobilewebserver9-zensardev.installprogram.eu/MobileWebGames/game/mgs/9_3_0_6473?moduleID=11091&clientID=50301&gameName=dragonDanceDesktop&gameTitle=Dragon%20Dance&LanguageCode=en&clientTypeID=40&casinoID=5007&lobbyName=IslandParadise&loginType=InterimUPE&lobbyURL=https://mobilewebserver9-ZensarDev.installprogram.eu/Lobby/en/IslandParadise/Games/3ReelSlots&xmanEndPoints=https://mobilewebserver9-ZensarDev.installprogram.eu/XMan/x.x&bankingURL=https://mobilewebserver9-ZensarDev.installprogram.eu/Lobby/en/IslandParadise/Banking&routerEndPoints=&isPracticePlay=false&username=player777&password=test&host=Desktop&apicommsenabled=false&launchtoken=&version=");
				// webdriver.get("https://mobilewebserver9-zensardev.installprogram.eu/MobileWebGames/game/mgs/9_5_1_6814?lobbyURL=https%3A%2F%2Fmobilewebserver9-zensardev.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FGames%2F3ReelSlots&moduleID=10206&clientID=40303&gameName=cricketStar&gameTitle=Cricket+Star&LanguageCode=en&clientTypeID=40&casinoID=5007&lobbyName=IslandParadise&loginType=PlayerService&bankingURL=https%3A%2F%2Fmobilewebserver9-zensardev.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FBanking&xmanEndPoints=https%3A%2F%2Fmobilewebserver9-zensardev.installprogram.eu%2FXMan%2Fx.x&routerEndPoints=&disablePoweredBy=false&currencyFormat=&isPracticePlay=false&logoutURL=https%3A%2F%2Fmobilewebserver9-zensardev.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FLogout&host=Desktop&apiCommsEnabled=false&activityStatementURL=undefined&username=player707&password=test");
				// webdriver.get("https://mobilewebserver9-zensardev2.installprogram.eu/MobileWebGames/game/mgs/9_6_0_6862?moduleID=11091&clientID=50301&gameName=dragonDanceDesktop&gameTitle=Dragon%20Dance&LanguageCode=en&clientTypeID=40&casinoID=5007&lobbyName=IslandParadise&loginType=InterimUPE&lobbyURL=https://mobilewebserver9-ZensarDev2.installprogram.eu/Lobby/en/IslandParadise/Games/3ReelSlots&xmanEndPoints=https://mobilewebserver9-ZensarDev2.installprogram.eu/XMan/x.x&bankingURL=https://mobilewebserver9-ZensarDev2.installprogram.eu/Lobby/en/IslandParadise/Banking&routerEndPoints=&isPracticePlay=false&username=player464&password=test&host=Mobile&apicommsenabled=false&launchtoken=&version=");
				// webdriver.get("https://mobilewebserver9-zensardev2.installprogram.eu/MobileWebGames/game/mgs/9_6_0_6862?lobbyURL=https%3A%2F%2Fmobilewebserver9-zensardev2.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FGames%2F3ReelSlots&moduleID=11091&clientID=40301&gameName=dragonDance&gameTitle=Dragon+Dance&LanguageCode=en&clientTypeID=40&casinoID=5007&lobbyName=IslandParadise&loginType=PlayerService&bankingURL=https%3A%2F%2Fmobilewebserver9-zensardev2.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FBanking&xmanEndPoints=https%3A%2F%2Fmobilewebserver9-zensardev2.installprogram.eu%2FXMan%2Fx.x&routerEndPoints=&disablePoweredBy=false&currencyFormat=&isPracticePlay=false&logoutURL=https%3A%2F%2Fmobilewebserver9-zensardev2.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FLogout&host=Desktop&apiCommsEnabled=false&activityStatementURL=undefined&username=player707&password=test");
				// Cricket star QA2
				// webdriver.get("https://mobilewebserver9-zensarqa2.installprogram.eu/MobileWebGames/game/mgs/9_5_1_6814?lobbyURL=https%3A%2F%2Fmobilewebserver9-zensarqa2.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FGames%2F3ReelSlots&moduleID=10206&clientID=40303&gameName=cricketStar&gameTitle=Cricket+Star&LanguageCode=en&clientTypeID=40&casinoID=5007&lobbyName=IslandParadise&loginType=PlayerService&bankingURL=https%3A%2F%2Fmobilewebserver9-zensarqa2.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FBanking&xmanEndPoints=https%3A%2F%2Fmobilewebserver9-zensarqa2.installprogram.eu%2FXMan%2Fx.x&routerEndPoints=&disablePoweredBy=false&currencyFormat=&isPracticePlay=false&logoutURL=https%3A%2F%2Fmobilewebserver9-zensarqa2.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FLogout&host=Desktop&apiCommsEnabled=false&launchToken=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL2FjY291bnQvdjEiLCJjaWQiOiI1MDA3IiwianRpIjoiLTM4OTI3MDY3Njk0NDMzMjkzODciLCJzdWIiOiI4NjRjNzNjY2UzZTM0YTY1YjQyNjdkMTI0MWFkZGMzZSIsImNlcnQiOiJNR1MuVXNlclNlc3Npb25Ub2tlbi5CbHVlbWVzYS4yMDE1MDMzMSIsImlhdCI6IjE1NTc5ODkzMzgiLCJhdWQiOiJKb2luVXNlclNlc3Npb24ifQ.ava-RSb8l634CsE6UfV8yc1mDxiNUHTAO7xqhRq-OtVfmRZwLcS_GR5GI54o-WkpqK2UuVIwu2D-p_cb_PCoAAANGThjJJkOrTwRNJSkM5hzVuZnRZkhtQCi82T-kdgHPmKSyN0IR7UJsWqU4AQaenqRgMVs0mGzOsoterym_HQ&gaClientId=undefined&activityStatementURL=undefined&username=player786&password=test");
				// Dragon dance QA2
				//Final Furlong webdriver.get("https://mobilewebserver9-zensarqa2.installprogram.eu/MobileWebGames/game/mgs/9_9_0_6998?moduleID=10025&clientID=50314&gameName=finalFurlongDesktop&gameTitle=Final%20Furlong&LanguageCode=en&clientTypeID=40&casinoID=5007&lobbyName=IslandParadise&loginType=InterimUPE&lobbyURL=https://mobilewebserver9-ZensarQA2.installprogram.eu/Lobby/en/IslandParadise/Games/3ReelSlots&xmanEndPoints=https://mobilewebserver9-ZensarQA2.installprogram.eu/XMan/x.x&bankingURL=https://mobilewebserver9-ZensarQA2.installprogram.eu/Lobby/en/IslandParadise/Banking&routerEndPoints=&isPracticePlay=false&username=player679&password=test&host=Desktop&apicommsenabled=false&launchtoken=&version=");
				long startTimeb4 = System.currentTimeMillis();
				log.debug("Started");
				//webdriver.get(
				//		"https://mobilewebserver9-zensarqa2.installprogram.eu/MobileWebGames/game/mgs/9_9_0_6998?moduleID=11091&clientID=50301&gameName=dragonDanceDesktop&gameTitle=Dragon%20Dance&LanguageCode=en&clientTypeID=40&casinoID=5007&lobbyName=IslandParadise&loginType=InterimUPE&lobbyURL=https://mobilewebserver9-ZensarQA2.installprogram.eu/Lobby/en/IslandParadise/Games/3ReelSlots&xmanEndPoints=https://mobilewebserver9-ZensarQA2.installprogram.eu/XMan/x.x&bankingURL=https://mobilewebserver9-ZensarQA2.installprogram.eu/Lobby/en/IslandParadise/Banking&routerEndPoints=&isPracticePlay=false&username=player786&password=test&host=Desktop&apicommsenabled=false&launchtoken=&version=");
				// Dragon dance DEV
				// webdriver.get("https://mobilewebserver9-zensardev.installprogram.eu/MobileWebGames/game/mgs/9_6_0_6862?lobbyURL=https%3A%2F%2Fmobilewebserver9-zensardev.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FGames%2F3ReelSlots&moduleID=11091&clientID=40301&gameName=dragonDance&gameTitle=Dragon+Dance&LanguageCode=en&clientTypeID=40&casinoID=5007&lobbyName=IslandParadise&loginType=PlayerService&bankingURL=https%3A%2F%2Fmobilewebserver9-zensardev.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FBanking&xmanEndPoints=https%3A%2F%2Fmobilewebserver9-zensardev.installprogram.eu%2FXMan%2Fx.x&routerEndPoints=&disablePoweredBy=false&currencyFormat=&isPracticePlay=false&logoutURL=https%3A%2F%2Fmobilewebserver9-zensardev.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FLogout&host=Desktop&apiCommsEnabled=false&launchToken=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL2FjY291bnQvdjEiLCJjaWQiOiI1MDA3IiwianRpIjoiMzA1OTczNzE4MjY5NjQ2MDAzMSIsInN1YiI6IjQ3MjdjN2U5NGZkNjRhYjc4NGMwZmExYmRlZjE1ZDM1IiwiY2VydCI6Ik1HUy5Vc2VyU2Vzc2lvblRva2VuLkJsdWVtZXNhLjIwMTUwMzMxIiwiaWF0IjoiMTU1ODQyODkyMyIsImF1ZCI6IkpvaW5Vc2VyU2Vzc2lvbiJ9.areMAxxqAc1IXLRzmKcnh4HWBZxpSxHkT1xBrZhQHktF2KAPDS5keA9U9jOVkXx4b8fjx0oacwbAoZCbZJ6y7IWeENa-GVB6Jdju4Y9ugtgJmieI8V3BjjG3V8vqNEggl1qaHAM9cE-nVDYADqsbRGCMlmhPXTSR3L2lppq3t-8&gaClientId=982088295.1533637304&activityStatementURL=undefined&username=player786&password=test");
				//webdriver.get(
				//			"https://mobilewebserver9-gametechreview.installprogram.eu/MobileWebGames/game/mgs/9_9_0_6998?moduleID=11091&clientID=50301&gameName=dragonDanceDesktop&gameTitle=Dragon%20Dance&LanguageCode=en&clientTypeID=40&casinoID=5007&lobbyName=IslandParadise&loginType=InterimUPE&lobbyURL=https://mobilewebserver9-GameTechReview.installprogram.eu/Lobby/en/IslandParadise/Games/3ReelSlots&xmanEndPoints=https://mobilewebserver9-GameTechReview.installprogram.eu/XMan/x.x&bankingURL=https://mobilewebserver9-GameTechReview.installprogram.eu/Lobby/en/IslandParadise/Banking&routerEndPoints=&isPracticePlay=false&username=bluemesa1&password=test&host=Desktop&apicommsenabled=false");
						
				//webdriver.get("https://mobilewebserver9-zensarqa2.installprogram.eu/MobileWebGames/game/mgs/9_9_0_6998?moduleID=11091&clientID=50301&gameName=dragonDanceDesktop&gameTitle=Dragon%20Dance&LanguageCode=en&clientTypeID=40&casinoID=5007&lobbyName=IslandParadise&loginType=InterimUPE&lobbyURL=https://mobilewebserver9-ZensarQA2.installprogram.eu/Lobby/en/IslandParadise/Games/3ReelSlots&xmanEndPoints=https://mobilewebserver9-ZensarQA2.installprogram.eu/XMan/x.x&bankingURL=https://mobilewebserver9-ZensarQA2.installprogram.eu/Lobby/en/IslandParadise/Banking&routerEndPoints=&isPracticePlay=false&username=player786&password=test&host=Desktop&apicommsenabled=false&launchtoken=&version=");
				//webdriver.get("https://mobilewebserver9-zensarqa.installprogram.eu/MobileWebGames/game/mgs/9_13_1_7236?lobbyURL=https%3A%2F%2Fmobilewebserver9-zensarqa.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FGames%2F3ReelSlots&moduleID=10008&clientID=50301&gameName=5ReelDriveDesktop&gameTitle=5+Reel+Drive&LanguageCode=en&clientTypeID=40&casinoID=5007&lobbyName=IslandParadise&loginType=PlayerService&bankingURL=https%3A%2F%2Fmobilewebserver9-zensarqa.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FBanking&xmanEndPoints=https%3A%2F%2Fmobilewebserver9-zensarqa.installprogram.eu%2FXMan%2Fx.x&routerEndPoints=&disablePoweredBy=false&currencyFormat=&isPracticePlay=false&logoutURL=https%3A%2F%2Fmobilewebserver9-zensarqa.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FLogout&host=Desktop&apiCommsEnabled=true&launchToken=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL2FjY291bnQvdjEiLCJjaWQiOiI1MDA3IiwianRpIjoiNTcxMjM2Mzg5OTQwMDkyMTUzMyIsIm91aWQiOiIweDY0RDEwMDAwIiwic3ViIjoiNTIzNzJiZmY0ZjIzNGVmY2FkODUyZjVjOTJmMTc1ZjAiLCJjZXJ0IjoiTUdTLlVzZXJTZXNzaW9uVG9rZW4uQmx1ZW1lc2EuMjAxNTAzMzEiLCJpYXQiOiIxNTcxMzk0NzgwIiwiYXVkIjoiSm9pblVzZXJTZXNzaW9uIn0.z_WOir3FcLx_MJC8CqX_2YaoecORAb3QbZgSMWhwGquSoedW4YhtdAPse3ZrpvPJfAvhERGXKoTu4ma87z2BUqLPXPyNKqwKwkaB678OFypm5OzgtYKwE-fjENamtUo4dWw20nDA9P7lkYhnbrvWrlKMd2lRvUtuOvuYzPI0AO0&gaClientId=1513979440.1565593852&activityStatementURL=undefined&username=player786&password=test");
				//webdriver.get("https://mobilewebserver9-zensarqa2.installprogram.eu/MobileWebGames/game/mgs/9_15_0_7290?lobbyURL=https%3A%2F%2Fmobilewebserver9-zensarqa2.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FGames%2F3ReelSlots&moduleID=10008&clientID=50301&gameName=5ReelDriveDesktop&gameTitle=5+Reel+Drive&LanguageCode=en&clientTypeID=40&casinoID=5007&lobbyName=IslandParadise&loginType=PlayerService&bankingURL=https%3A%2F%2Fmobilewebserver9-zensarqa2.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FBanking&xmanEndPoints=https%3A%2F%2Fmobilewebserver9-zensarqa2.installprogram.eu%2FXMan%2Fx.x&routerEndPoints=&disablePoweredBy=false&currencyFormat=&isPracticePlay=false&logoutURL=https%3A%2F%2Fmobilewebserver9-zensarqa2.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FLogout&host=Desktop&apiCommsEnabled=true&launchToken=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL2FjY291bnQvdjEiLCJjaWQiOiI1MDA3IiwianRpIjoiNDM1ODA0NTI0NzQzMzU0Mzc1Iiwib3VpZCI6IjB4MThBQjAwMDAiLCJzdWIiOiJjYzVkZjc0NDZhOGQ0OGM3YjA1NzNmMjQ4MDkwZDBmNiIsImNlcnQiOiJNR1MuVXNlclNlc3Npb25Ub2tlbi5CbHVlbWVzYS4yMDE1MDMzMSIsImlhdCI6IjE1NzEzOTg2NjkiLCJhdWQiOiJKb2luVXNlclNlc3Npb24ifQ.JOn_s4aA3rnAthAng4_LpvlOALptaUQhC4AoVRTqBh_ntQajnsIJGIn-Dna_PSIaFCIhMN7RqbLXkQd_0ideDd8AgyjsLvWyQGjGV4KRiE0gE7bQlNmt82by0EO5fgG_M__TqKrluUKurIMPXHQaqwQ1p1trUcsNpxp4gvNPkAY&gaClientId=1513979440.1565593852&activityStatementURL=undefined&username=AnilK&password=test");
				webdriver.get("https://mobilewebserver9-zensardev.installprogram.eu/MobileWebGames/game/mgs/9_14_0_7270?lobbyURL=https%3A%2F%2Fmobilewebserver9-zensardev.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FGames%2F3ReelSlots&moduleID=10008&clientID=50301&gameName=5ReelDriveDesktop&gameTitle=5+Reel+Drive&LanguageCode=en&clientTypeID=40&casinoID=5007&lobbyName=IslandParadise&loginType=PlayerService&bankingURL=https%3A%2F%2Fmobilewebserver9-zensardev.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FBanking&xmanEndPoints=https%3A%2F%2Fmobilewebserver9-zensardev.installprogram.eu%2FXMan%2Fx.x&routerEndPoints=&disablePoweredBy=false&currencyFormat=&isPracticePlay=false&logoutURL=https%3A%2F%2Fmobilewebserver9-zensardev.installprogram.eu%2FLobby%2Fen%2FIslandParadise%2FLogout&host=Desktop&apiCommsEnabled=false&launchToken=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL2FjY291bnQvdjEiLCJjaWQiOiI1MDA3IiwianRpIjoiLTI4NjQ4NjM4NTc2NzU2MDE3OTciLCJvdWlkIjoiMHgxNzM4MDAwMCIsInN1YiI6IjIxYzQ4NWZhZWUzNTQ0ZjVhMWNhYzU4OGE2MTY1MTU4IiwiY2VydCI6Ik1HUy5Vc2VyU2Vzc2lvblRva2VuLkJsdWVtZXNhLjIwMTUwMzMxIiwiaWF0IjoiMTU3MTM5OTE2NSIsImF1ZCI6IkpvaW5Vc2VyU2Vzc2lvbiJ9.1xNKp8z9XhtVx2eL1i77hqzuPHLipDlWXbuoguIoJ8F7Ab2465tRgTEtSe3lU5M0byemsYxVkyUaYbfnjGM1wSfC8hgWeXOpcxaq4dShzmS0Ua3ECeJyap5Qf_isIbJlfBvoLbQfKrdFg-cnFGvMkKc5biaWYlzqMPD831dmFtg&gaClientId=1513979440.1565593852&activityStatementURL=undefined&username=player370&password=test");
				log.debug("Get Called");
				WebDriverWait wait = new WebDriverWait(webdriver, 1000);

				int noOfFiles = 0;
				long startTimea4 = System.currentTimeMillis();
				log.debug("Game has loaded successfully with in ::" + (startTimea4 - startTimeb4));
				long startTime = System.currentTimeMillis();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='inGameClock']")));
				//wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='backToLobbyContainer']")));////div[@id='backToLobbyContainer']
				long endTime = System.currentTimeMillis();
				
				
				Har nhar = new Har(proxy.getHar().getLog());
				long endTime2 = System.currentTimeMillis();
				Thread.sleep(10000);
				proxy.endHar();
				webdriver.close();
				webdriver.quit();
				log.debug("End time " + endTime);
				log.debug("Game has loaded successfully with in ::" + ((endTime - startTime) / 1000) + "secs");
				log.debug("har creation rtime ::" + ((endTime2 - endTime) / 1000) + "secs");
				HarLog hardata = nhar.getLog();
				List<HarEntry> entries = hardata.getEntries();
				Iterator<HarEntry> itr = entries.iterator();

				long totalResponseSize = 0;

				/*while (itr.hasNext()) {
					noOfFiles++;
					HarEntry entry = itr.next();
					String requestUrl = entry.getRequest().getUrl();
					long size = entry.getResponse().getBodySize();
					// if(size>10000){
					log.info("automation Logger" + requestUrl + "---> Size :: " + size);
					System.out.println("automation Logger" + requestUrl + "---> Size :: " + size);
					// System.out.println(" Content Size " + requestUrl + "--->
					// Size :: " +
					// entry.getResponse().getContent().getEncoding()+" Size--->
					// "+ entry.getResponse().getContent().getSize());
					// }
					if (size == -1 && !requestUrl.contains("google")) {
						break;
					}
					totalResponseSize += size;
					// log.info("automation Logger" + requestUrl + "---> Size ::
					// " + size);
				}

				log.info("automation Logger Overall Game loading size" + totalResponseSize + " Total no files ::"
						+ noOfFiles);
				log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				log.info("                       Audio files                                  ");
				log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

				List<HarEntry> audioEntries = hardata.getEntries();
				Iterator<HarEntry> audioITR = audioEntries.iterator();
				noOfFiles = 0;
				totalResponseSize = 0;

				while (audioITR.hasNext()) {
					HarEntry entry = audioITR.next();
					String requestUrl = entry.getRequest().getUrl();
					if (requestUrl.contains("audio")) {

						noOfFiles++;

						long size = entry.getResponse().getBodySize();
						log.info("automation Logger" + requestUrl + "---> Size :: " + size);
						System.out.println("automation Logger" + requestUrl + "---> Size :: " + size);
						totalResponseSize += size;

					}
				}
				log.info(" Overall audio files loading size" + totalResponseSize + " Total no files ::" + noOfFiles);
				log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				log.info("                       Image files                                  ");
				log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

				List<HarEntry> webpEntries = hardata.getEntries();
				Iterator<HarEntry> webpITR = webpEntries.iterator();
				noOfFiles = 0;
				totalResponseSize = 0;

				while (webpITR.hasNext()) {
					HarEntry entry = webpITR.next();
					String requestUrl = entry.getRequest().getUrl();
					if (requestUrl.contains("webp")) {

						noOfFiles++;

						long size = entry.getResponse().getBodySize();
						log.info("automation Logger" + requestUrl + "---> Size :: " + size);
						System.out.println("automation Logger" + requestUrl + "---> Size :: " + size);

						totalResponseSize += size;

					}
				}
				log.info(" Overall webp files loading size" + totalResponseSize + " Total no files ::" + noOfFiles);

				log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				log.info("                       JS files                                  ");
				log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				List<HarEntry> jsEntries = hardata.getEntries();
				Iterator<HarEntry> jsITR = jsEntries.iterator();
				noOfFiles = 0;
				totalResponseSize = 0;

				while (jsITR.hasNext()) {
					HarEntry entry = jsITR.next();
					String requestUrl = entry.getRequest().getUrl();
					if (requestUrl.contains(".js") && !requestUrl.contains(".json")) {

						noOfFiles++;

						long size = entry.getResponse().getBodySize();

						log.info("automation Logger" + requestUrl + "---> Size :: " + size);
						System.out.println("automation Logger" + requestUrl + "---> Size :: " + size);

						totalResponseSize += size;

					}
				}
				log.info(" Overall JS files loading size" + totalResponseSize + " Total no files ::" + noOfFiles);
				log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				log.info("                       JSON files                                  ");
				log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

				List<HarEntry> jsonEntries = hardata.getEntries();
				Iterator<HarEntry> jsonITR = jsonEntries.iterator();
				noOfFiles = 0;
				totalResponseSize = 0;

				while (jsonITR.hasNext()) {
					HarEntry entry = jsonITR.next();
					String requestUrl = entry.getRequest().getUrl();
					if (requestUrl.contains(".json")) {

						noOfFiles++;

						long size = entry.getResponse().getBodySize();

						log.info("automation Logger" + requestUrl + "---> Size :: " + size);
						System.out.println("automation Logger" + requestUrl + "---> Size :: " + size);

						totalResponseSize += size;

					}
				}
				log.info(" Overall json files loading size" + totalResponseSize + " Total no files ::" + noOfFiles);
				*/
				
				log.info(" Overall JS files loading size" + totalResponseSize + " Total no files ::" + noOfFiles);
				log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				log.info("                       Message files                                  ");
				log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

				List<HarEntry> messageEntries = hardata.getEntries();
				Iterator<HarEntry> messageITR = messageEntries.iterator();
				noOfFiles = 0;
				totalResponseSize = 0;

				while (messageITR.hasNext()) {
					HarEntry entry = messageITR.next();
					String requestUrl = entry.getRequest().getUrl();
					
					if (requestUrl.contains("message")) {
						System.out.println(entry.getRequest().getPostData().getText());
						noOfFiles++;

						//long size = entry.getResponse().getBodySize();

						//log.info("automation Logger" + requestUrl + "---> Size :: " + size);
						//System.out.println("automation Logger" + requestUrl + "---> Size :: " + size);

						//totalResponseSize += size;

					}
				}
				/*log.info(" Overall json files loading size" + totalResponseSize + " Total no files ::" + noOfFiles);

				log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				log.info("                       Other files                                  ");
				log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

				List<HarEntry> otherEntries = hardata.getEntries();
				Iterator<HarEntry> otherITR = otherEntries.iterator();
				noOfFiles = 0;
				totalResponseSize = 0;

				while (otherITR.hasNext()) {
					HarEntry entry = otherITR.next();
					String requestUrl = entry.getRequest().getUrl();
					if (!requestUrl.contains(".js") && !requestUrl.contains(".json") && !requestUrl.contains("audio")
							&& !requestUrl.contains(".webp")) {

						noOfFiles++;

						long size = entry.getResponse().getBodySize();

						log.info("automation Logger" + requestUrl + "---> Size :: " + size);

						totalResponseSize += size;

					}
				}*/
				log.info(" Overall json files loading size" + totalResponseSize + " Total no files ::" + noOfFiles);

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
