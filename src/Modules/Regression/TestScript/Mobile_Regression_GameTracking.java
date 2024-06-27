package Modules.Regression.TestScript;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.library.DataBaseFunctions;
import com.zensar.automation.library.TestPropReader;
import com.zensar.automation.model.Analytics;

import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarLog;

/**
 * This Script traverse all the panels and take the screen shot of the all screens such as paytable, bet settings, auto play etc.
 * It reads the test data excel sheet for configured languages.
 * @author Premlata
 */

public class Mobile_Regression_GameTracking {
	Logger log = Logger.getLogger(Mobile_Regression_GameTracking.class.getName());
	public ScriptParameters scriptParameters;

	public void script() throws Exception{
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

		Mobile_HTML_Report trackingReport=new Mobile_HTML_Report(webdriver,DeviceName,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,gameName);

		MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, trackingReport, gameName);
		
		try {
			if (webdriver != null) {
				webdriver.manage().deleteAllCookies();
				
				Thread.sleep(2000);
				startHarReading();
				
				WebDriverWait wait = new WebDriverWait(webdriver, 250);
				log.debug("Started");
				userName="player723";
				String url ="https://mobilewebserver9-zensardev2.installprogram.eu/MobileWebGames/game/mgs/9_18_1_7561?moduleID=10206&clientID=50300&gameName=breakAwayDesktop&gameTitle=Break%20Away&LanguageCode=en&clientTypeID=40&casinoID=5007&lobbyName=IslandParadise&loginType=InterimUPE&lobbyURL=https://mobilewebserver9-ZensarDev2.installprogram.eu/Lobby/en/IslandParadise/Games/3ReelSlots&xmanEndPoints=https://mobilewebserver9-ZensarDev2.installprogram.eu/XMan/x.x&bankingURL=https://mobilewebserver9-ZensarDev2.installprogram.eu/Lobby/en/IslandParadise/Banking&routerEndPoints=&isPracticePlay=false&username="+userName+"&password=test&host=Desktop&apicommsenabled=false&launchtoken=&version=";
				webdriver.get(url);
				long startTimeb4 = System.currentTimeMillis();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='inGameClock']")));
				long endTime = System.currentTimeMillis();
				System.out.println("game Load time"+(endTime-startTimeb4));
				// Har nhar = new Har(proxy.getHar().getLog());
				alert(webdriver,"Intial Verification");
				Har nhar = getHar();
				Thread.sleep(10000);
				// proxy.endHar();
				endHarReading();
			

				print(nhar);
				if(framework.equalsIgnoreCase("Force")){
					cfnlib.setNameSpace();
					}
				
				validateEventType(100,nhar,trackingReport,"Is the GameLoadDomContentLoaded (100) value coming through?");
				validateEventTypeDuration(100,nhar,trackingReport,"Is Duration for GameLoadDomContentLoaded coming through?");
				
				validateEventType(101,nhar,trackingReport,"Is the GameLoadDomContentComplete (101) value coming through?");
				validateEventTypeDuration(101,nhar,trackingReport,"Is Duration for GameLoadDomContentCompletecoming through?");
				
				validateEventType(102,nhar,trackingReport,"	Is the GameLoadAttempt (102) value coming through?");
				validateEventTypeDuration(102,nhar,trackingReport,"Is Duration for GameLoadAttempt coming through?");
				
				validateEventType(103,nhar,trackingReport,"Is the GameLoadSuccess (103) value coming through?");
				validateEventTypeDuration(103,nhar,trackingReport,"Is Duration for GameLoadSuccess coming through?");
				
				validateLoadType(103,nhar,trackingReport,"Is the FirstLoad (1) value coming through?",1);
				
				System.out.println("_____________________________________Game Load type Verification__________________________________________________");
				alert(webdriver,"Game Load type Verification ");
				startHarReading();
				webdriver.navigate().refresh();
				
				Har nhar1 = getHar();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='inGameClock']")));
				Thread.sleep(5000);
				endHarReading();
				//print(nhar1);
				System.out.println("________________________________________________________________________________________________________________");
				
				validateLoadType(103,nhar1,trackingReport,"Is the Cached (2) value coming through?",2);
				
				System.out.println("_____________________________________105__________________________________________________");
				alert(webdriver,"105 Verification");
				startHarReading();
				webdriver.navigate().refresh();
				Thread.sleep(3000);
				webdriver.navigate().refresh();
				//Thread.sleep(1000);
				Har nhar2 = getHar();
				endHarReading();
				print(nhar2);
				System.out.println("________________________________________________________________________________________________________________");
				
				validateEventType(105,nhar2,trackingReport,"Is the GameLoadAbort (105) value coming through?");
				validateEventTypeDuration(105,nhar2,trackingReport,"Is the GameLoadAbort (105) value coming through?");
				
				
				
				System.out.println("_____________________________________104__________________Working________________________________");
				alert(webdriver,"Routing verificaation");
				url = url.replace("mgs/", "mgs/1");
				WebDriverWait wait104 = new WebDriverWait(webdriver, 120);
				startHarReading();
				webdriver.get(url);
				wait104.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='inGameClock']")));
				Thread.sleep(2000);
				Har nhar104 = getHar();
				endHarReading();
				print(nhar104);
				System.out.println("________________________________________________________________________________________________________________");
				validateEventType(104,nhar104,trackingReport,"Is the GameLoadRedirect (104) value coming through?");
				validateEventTypeDuration(104,nhar104,trackingReport,"Is the GameLoadRedirect (104) value coming through?");
			
				
				System.out.println("_____________________________________Spin ________________________________");
				alert(webdriver,"Spin Spin Method");
				startHarReading();
				webdriver.get(url);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='inGameClock']")));
				startHarReading();
				cfnlib.spinclick();
				Thread.sleep(2000);
				cfnlib.waitForSpinButtonstop();
				Thread.sleep(2000);
				//Thread.sleep(10000);
				Har nharSpin = getHar();
				endHarReading();
				print(nharSpin);
				validateSpinMethod(-1,nharSpin,trackingReport,"Is the Click (7) value coming through?",7);
				validateSpinSpeed(0,nharSpin,trackingReport,"Is the Normal (1) value coming through?",1);
				System.out.println("________________________________________________________________________________________________________________");
				
				System.out.println("_____________________________________Check spin stop ________________________________");
				alert(webdriver,"Check spin stop");
				startHarReading();
				cfnlib.clickspintostop();
				Thread.sleep(3000);
				Har nharSpinStop = getHar();
				endHarReading();
				print(nharSpinStop);
				validateSpinSpeed(0,nharSpinStop,trackingReport,"Is the SpinStop value (3) coming through?",3);
				
				System.out.println("_____________________________________Sound On check_____________________________________________________");
				alert(webdriver,"Sound On check");
				startHarReading();
				Thread.sleep(3000);
				cfnlib.setSoundFalgActive(true);
				
				Thread.sleep(2000);
				cfnlib.spinclick();
				Thread.sleep(10000);
				Har nharSoundOn = getHar();
				endHarReading();
				print(nharSoundOn);
				validateBackGroundSounds(0,nharSpinStop,trackingReport,"GameSounds field: Is the On (1) value coming through?",1);
				
				System.out.println("_____________________________________Sound off check_____________________________________________________");
				alert(webdriver,"Sound off check");
				startHarReading();		
				Thread.sleep(2000);
				cfnlib.setSoundFalgActive(false);
				Thread.sleep(3000);
				
				cfnlib.spinclick();
				Thread.sleep(2000);
				cfnlib.waitForSpinButtonstop();
				Thread.sleep(2000);
				cfnlib.spinclick();
				cfnlib.waitForSpinButtonstop();
				Thread.sleep(2000);
				Har nharSoundOff = getHar();
				endHarReading();
				print(nharSoundOff);
				validateBackGroundSounds(0,nharSoundOff,trackingReport,"GameSounds field: Is the Off (2) value coming through?",2);
				
				System.out.println("_____________________________________Set quick spin on ________________________________");
				alert(webdriver,"quick spin on spin method");
				startHarReading();
				
				cfnlib.setQuickSpinOn();
				
				Thread.sleep(3000);
				
				cfnlib.spinclick();
				Thread.sleep(2000);
				cfnlib.waitForSpinButtonstop();
				Thread.sleep(2000);
				Har nharQuickSpin = getHar();
				endHarReading();
				print(nharQuickSpin);
				
				validateSpinSpeed(0,nharQuickSpin,trackingReport,"Is the Normal (1) value coming through?",2);
						
				
				System.out.println("_____________________________________Autoplay scenarios ________________________________");
				alert(webdriver,"Autoplay spinMethod");
				
				startHarReading();
				cfnlib.startAutoPlay();
				
				Thread.sleep(12000);
				Har autoPlay = getHar();
				endHarReading();
				print(autoPlay);
				
				validateSpinMethod(0,autoPlay,trackingReport,"Is the Click (7) value coming through?",4);
				
				String freeSpinUser= "player204";
				url = url.replaceAll(userName, freeSpinUser);
				System.out.println(url);
				webdriver.get(url);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='inGameClock']")));
				
				Thread.sleep(2000);
				if(framework.equalsIgnoreCase("Force")){
					cfnlib.setNameSpace();
					}
				cfnlib.waitForSpinButton();
				Thread.sleep(5000);
				cfnlib.spinclick();
				
				Thread.sleep(2000);
				
				String screenName = cfnlib.entryScreen_Wait("yes");
				log.info("FreeSpinEntryScreen response"+screenName);
				
				
				//HamBurgerMenu
				//
				
				//Credit
				
				
				//win
				
				//reel
				
				//autopaly
				
				//Quickspin
				
				//spin
				
				//bet
				
				//MaxBet
				
				//anywhere on screen
				
				//Logo
				
				//Click oN Free Spin Entry page
				
				//Every where on the Freespin page
				
				//Click on summary page
				
				
				
				
				
				
				
				if(screenName.equalsIgnoreCase("freeSpin")) {
					startHarReading();
				}
				
				
				
				//cfnlib.waitForSpinButton();
				cfnlib.waitSummaryScreen();
				cfnlib.waitForSpinButton();
				Har freeSpinHar = getHar();
				endHarReading();
				print(freeSpinHar);
				validateSpinMethod(0,freeSpinHar, trackingReport,"Is the FreeSpin (5) value coming through",5);
				
				DataBaseFunctions createplayer = new DataBaseFunctions(TestPropReader.getInstance().getProperty("ServerName"),
						TestPropReader.getInstance().getProperty("dataBaseName"),
						TestPropReader.getInstance().getProperty("serverIp"), TestPropReader.getInstance().getProperty("port"),
						TestPropReader.getInstance().getProperty("serverID"));
				
				startHarReading();
				//createplayer.createUser(username, currency, usertype);
				String freeGamesUser= "AnilK";
				
				createplayer.assignFreeGame(freeGamesUser, gameName);
				
				url = url.replaceAll(freeSpinUser, freeGamesUser);
				System.out.println(url);
				webdriver.get(url);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='inGameClock']")));
				
								
				cfnlib.clickFregamesPlay();
				
				cfnlib.spinclick();
				cfnlib.waitForSpinButton();
				Thread.sleep(2000);
				Har freeGameHar = getHar();
				endHarReading();
				print(freeGameHar);
				validateSpinMethod(0,freeGameHar, trackingReport,"Is the FreeGameSpin (6) value coming through?",6);
				
				/*System.out.println("_____________________________________106____________________Notworking______________________________");
				startHarReading();
				webdriver.navigate().refresh();
				Thread.sleep(8000);
				changeProxyToOffline();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='inGameClock']")));
				//Thread.sleep(1000);
				Har nhar2 = getHar();
				endHarReading();
				print(nhar2);
				System.out.println("________________________________________________________________________________________________________________");*/
				
				
			}

		}
		// -------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error("Exception in CDN Cache busting", e);
			cfnlib.evalException(e);
		} finally {
			trackingReport.endReport();
			webdriver.close();
			webdriver.quit();
		}
}
	
	
	public void startHarReading() {
		BrowserMobProxyServer proxy=scriptParameters.getProxy();

		proxy.newHar();

	}

	public Har getHar() {
		BrowserMobProxyServer proxy=scriptParameters.getProxy();

		Har nhar = new Har(proxy.getHar().getLog());

		return nhar;
	}

	public void endHarReading() {
		BrowserMobProxyServer proxy=scriptParameters.getProxy();

		proxy.endHar();

	}
	
	public void changeProxyToOffline() {
		BrowserMobProxyServer proxy=scriptParameters.getProxy();

		proxy.setLatency(0, TimeUnit.MILLISECONDS);
		proxy.setReadBandwidthLimit(0);
		proxy.setWriteBandwidthLimit(0);
	}

	
	public void validateEventType( int eventType,Har nhar, Mobile_HTML_Report trackingReport,String message){
		


		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		HarLog hardata = nhar.getLog();
		boolean isEventPresent = false;


		log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		log.info("                       Message files                                  ");
		log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

		List<HarEntry> messageEntries = hardata.getEntries();
		Iterator<HarEntry> messageITR = messageEntries.iterator();

		while (messageITR.hasNext()) {
			HarEntry entry = messageITR.next();
			String requestUrl = entry.getRequest().getUrl();

			if (requestUrl.contains("message")) {
				//System.out.println(entry.getRequest().getPostData().getText());
				Analytics gameAnalytics;
				try {
					gameAnalytics = mapper.readValue(entry.getRequest().getPostData().getText(), Analytics.class);
					//System.out.println("Event Type:"+gameAnalytics.getEventInfo().getEventType());
					//System.out.println("Duration :"+gameAnalytics.getEventInfo().getDuration());
					
					if(gameAnalytics.getEventInfo().getEventType()==eventType){
						isEventPresent=true;
						trackingReport.detailsAppend(message, message +"Should log ", eventType+" Event Type is Present", "PASS");
					}
					//System.out.println("Duration :"+gameAnalytics.getEventInfo().getDuration());
				} catch (JsonParseException e) {
					log.error("JsonParseException", e);
				} catch (JsonMappingException e) {
					log.error("JsonMappingException", e);
				} catch (IOException e) {
					log.error("IOException", e);
				}catch (Exception e) {
					log.error("Exception", e);
				}

			}
		}
		
		if(!isEventPresent){
		try {
			trackingReport.detailsAppend(message, message +"Should log ", "Event Type not found", "FAIL");
		} catch (Exception e) {
			log.error("Exception", e);
		}
		}
	
		
	}
	
	
	
	
	public void validateSpinSpeed(int eventType,Har nhar, Mobile_HTML_Report trackingReport,String message,int spinSpeed){
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		HarLog hardata = nhar.getLog();
		boolean isSpinSpeed = false;

		List<HarEntry> messageEntries = hardata.getEntries();
		Iterator<HarEntry> messageITR = messageEntries.iterator();

		while (messageITR.hasNext()) {
			HarEntry entry = messageITR.next();
			
			String requestUrl = entry.getRequest().getUrl();

			if (requestUrl.contains("message")) {
				//System.out.println(entry.getRequest().getPostData().getText());
				Analytics gameAnalytics;
				try {
					gameAnalytics = mapper.readValue(entry.getRequest().getPostData().getText(), Analytics.class);
					
					if (eventType >= 0 && gameAnalytics.getEventInfo().getEventType() == eventType) {

						if (gameAnalytics.getEventInfo().getSpinSpeed() == spinSpeed) {
							isSpinSpeed = true;

							trackingReport.detailsAppend(message, message + "Should log ",
									"Spin Speed ::  " + spinSpeed + " is Logged", "PASS");
						}

					} else {
						if (gameAnalytics.getEventInfo().getSpinSpeed() == spinSpeed) {
							isSpinSpeed = true;

							trackingReport.detailsAppend(message, message + "Should log ",
									"Spin Speed ::  " + spinSpeed + " is Logged", "PASS");
						}
					}
					
					//System.out.println("Event Type:"+gameAnalytics.getEventInfo().getEventType());
					//System.out.println("Duration :"+gameAnalytics.getEventInfo().getDuration());
				} catch (JsonParseException e) {
					log.error("JsonParseException", e);
				} catch (JsonMappingException e) {
					log.error("JsonMappingException", e);
				} catch (IOException e) {
					log.error("IOException", e);
				}catch (Exception e) {
					log.error("Exception", e);
				}

			}
		}
		try {
		if(!isSpinSpeed){
		
			trackingReport.detailsAppend(message, message +"Should log ", "Spin Speed ::  "+spinSpeed +" is Not Logged", "FAIL");
		
		}		
		} catch (Exception e) {
			log.error("Exception", e);
		}

		
	}
	
	
public void validateSpinMethod( int eventType,Har nhar, Mobile_HTML_Report trackingReport,String message,int spinMethod){
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		HarLog hardata = nhar.getLog();
		boolean isSpinMethod = false;

		List<HarEntry> messageEntries = hardata.getEntries();
		Iterator<HarEntry> messageITR = messageEntries.iterator();

		while (messageITR.hasNext()) {
			HarEntry entry = messageITR.next();
			
			String requestUrl = entry.getRequest().getUrl();

			if (requestUrl.contains("message")) {
				//System.out.println(entry.getRequest().getPostData().getText());
				Analytics gameAnalytics;
				try {
					gameAnalytics = mapper.readValue(entry.getRequest().getPostData().getText(), Analytics.class);
					//System.out.println("Event Type:"+gameAnalytics.getEventInfo().getEventType());
					//System.out.println("Duration :"+gameAnalytics.getEventInfo().getDuration());
					
					if(eventType >= 0  && gameAnalytics.getEventInfo().getEventType()==eventType){
						
						if(gameAnalytics.getEventInfo().getSpinMethod()==spinMethod){
							isSpinMethod=true;
							
							trackingReport.detailsAppend(message, message +"Should log ", "Spin Method  "+spinMethod +" is Logged", "PASS");
							}
						
					}else{
					
					if(gameAnalytics.getEventInfo().getSpinMethod()==spinMethod){
						isSpinMethod=true;
						
						trackingReport.detailsAppend(message, message +"Should log ", "Spin Method  "+spinMethod +" is Logged", "PASS");
						}
					}
					
					//System.out.println("Event Type:"+gameAnalytics.getEventInfo().getEventType());
					//System.out.println("Duration :"+gameAnalytics.getEventInfo().getDuration());
				} catch (JsonParseException e) {
					log.error("JsonParseException", e);
				} catch (JsonMappingException e) {
					log.error("JsonMappingException", e);
				} catch (IOException e) {
					log.error("IOException", e);
				}catch (Exception e) {
					log.error("Exception", e);
				}

			}
		}
		try {
		if(!isSpinMethod){
		
			trackingReport.detailsAppend(message, message +"Should log ", "Spin Method  "+spinMethod +" is Not Logged", "FAIL");
		
		}		
		} catch (Exception e) {
			log.error("Exception", e);
		}

		
	}
	
public void validateLoadType( int eventType,Har nhar, Mobile_HTML_Report trackingReport,String message,int loadType){
	
	ObjectMapper mapper = new ObjectMapper();
	mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	HarLog hardata = nhar.getLog();
	boolean isEventPresent = false;
	boolean isLoadTypePresent = false;

	List<HarEntry> messageEntries = hardata.getEntries();
	Iterator<HarEntry> messageITR = messageEntries.iterator();

	while (messageITR.hasNext()) {
		HarEntry entry = messageITR.next();
		
		String requestUrl = entry.getRequest().getUrl();

		if (requestUrl.contains("message")) {
			//System.out.println(entry.getRequest().getPostData().getText());
			Analytics gameAnalytics;
			try {
				gameAnalytics = mapper.readValue(entry.getRequest().getPostData().getText(), Analytics.class);
				//System.out.println("Event Type:"+gameAnalytics.getEventInfo().getEventType());
				//System.out.println("Duration :"+gameAnalytics.getEventInfo().getDuration());
				
				if(gameAnalytics.getEventInfo().getEventType()==eventType){
					isEventPresent=true;
					
					long gameLoadType=  gameAnalytics.getEventInfo().getGameloadType();
					if(gameLoadType == loadType){
						isLoadTypePresent=true;
						trackingReport.detailsAppend(message, message +"Should log ", "Load type for Event type:: "+eventType +" is ::"+gameLoadType, "PASS");
					}else{
						isLoadTypePresent=true;
						trackingReport.detailsAppend(message, message +"Should log ", "Load type for Event type:: "+eventType +" is ::"+gameLoadType, "FAIL");		
					}
				}
				//System.out.println("Event Type:"+gameAnalytics.getEventInfo().getEventType());
				//System.out.println("Duration :"+gameAnalytics.getEventInfo().getDuration());
			} catch (JsonParseException e) {
				log.error("JsonParseException", e);
			} catch (JsonMappingException e) {
				log.error("JsonMappingException", e);
			} catch (IOException e) {
				log.error("IOException", e);
			}catch (Exception e) {
				log.error("Exception", e);
			}

		}
	}
	try {
	if(!isEventPresent){
	
		trackingReport.detailsAppend(message, message +"Should log ", "Event Type not found to check the load type", "FAIL");
	
	}else if(isEventPresent&&!isLoadTypePresent){
	
		trackingReport.detailsAppend(message, message +"Should log ", "Event Type not found to check the load type", "FAIL");
	
	}
	
	} catch (Exception e) {
		log.error("Exception", e);
	}

	
}

public void validateBackGroundSounds( int eventType,Har nhar, Mobile_HTML_Report trackingReport,String message,int soundType){
	
	ObjectMapper mapper = new ObjectMapper();
	mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	HarLog hardata = nhar.getLog();
	boolean isEventPresent = false;
	boolean isSoundTypePresent = false;

	List<HarEntry> messageEntries = hardata.getEntries();
	Iterator<HarEntry> messageITR = messageEntries.iterator();

	while (messageITR.hasNext()) {
		HarEntry entry = messageITR.next();
		
		String requestUrl = entry.getRequest().getUrl();

		if (requestUrl.contains("message")) {
			//System.out.println(entry.getRequest().getPostData().getText());
			Analytics gameAnalytics;
			try {
				gameAnalytics = mapper.readValue(entry.getRequest().getPostData().getText(), Analytics.class);
				//System.out.println("Event Type:"+gameAnalytics.getEventInfo().getEventType());
				//System.out.println("Duration :"+gameAnalytics.getEventInfo().getDuration());
				
				if(gameAnalytics.getEventInfo().getEventType() ==eventType){
					isEventPresent=true;
					
					long gameSoundType=  gameAnalytics.getEventInfo().getGameSounds();
					if(gameSoundType == soundType){
						isSoundTypePresent=true;
						trackingReport.detailsAppend(message, message +"Should log ", "Load type for Sound type:: "+eventType +" is ::"+soundType, "PASS");
					}
				}
				//System.out.println("Event Type:"+gameAnalytics.getEventInfo().getEventType());
				//System.out.println("Duration :"+gameAnalytics.getEventInfo().getDuration());
			} catch (JsonParseException e) {
				log.error("JsonParseException", e);
			} catch (JsonMappingException e) {
				log.error("JsonMappingException", e);
			} catch (IOException e) {
				log.error("IOException", e);
			}catch (Exception e) {
				log.error("Exception", e);
			}

		}
	}
	try {
	if(!isEventPresent){
	
		trackingReport.detailsAppend(message, message +"Should log ", "Event Type not found to check the load type", "FAIL");
	
	}else if(isEventPresent&&!isSoundTypePresent){
	
		trackingReport.detailsAppend(message, message +"Should log ", "Event Type not found to check the load type", "FAIL");
	
	}
	
	} catch (Exception e) {
		log.error("Exception", e);
	}

	
}

	public void validateEventTypeDuration( int eventType,Har nhar, Mobile_HTML_Report trackingReport,String message){
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		HarLog hardata = nhar.getLog();
		boolean isEventPresent = false;
		boolean isDurationPresent = false;

		log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		log.info("                       Message files                                ");
		log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

		List<HarEntry> messageEntries = hardata.getEntries();
		Iterator<HarEntry> messageITR = messageEntries.iterator();

		while (messageITR.hasNext()) {
			HarEntry entry = messageITR.next();
			String requestUrl = entry.getRequest().getUrl();

			if (requestUrl.contains("message")) {
				//System.out.println(entry.getRequest().getPostData().getText());
				Analytics gameAnalytics;
				try {
					gameAnalytics = mapper.readValue(entry.getRequest().getPostData().getText(), Analytics.class);
					//System.out.println("Event Type:"+gameAnalytics.getEventInfo().getEventType());
					//System.out.println("Duration :"+gameAnalytics.getEventInfo().getDuration());
					
					if(gameAnalytics.getEventInfo().getEventType()==eventType){
						isEventPresent=true;
						
						long duration=  gameAnalytics.getEventInfo().getDuration();
						if(duration>0){
							isDurationPresent=true;
							trackingReport.detailsAppend(message, message +"Should log ", "Duration for Event type:: "+eventType +" is ::"+duration, "PASS");
						}else{
							trackingReport.detailsAppend(message, message +"Should log ", "Duration for Event type:: "+eventType +" is ::"+duration, "FAIL");		
						}
					}
					//System.out.println("Event Type:"+gameAnalytics.getEventInfo().getEventType());
					//System.out.println("Duration :"+gameAnalytics.getEventInfo().getDuration());
				} catch (JsonParseException e) {
					log.error("JsonParseException", e);
				} catch (JsonMappingException e) {
					log.error("JsonMappingException", e);
				} catch (IOException e) {
					log.error("IOException", e);
				}catch (Exception e) {
					log.error("Exception", e);
				}

			}
		}
		try {
		if(!isEventPresent){
		
			trackingReport.detailsAppend(message, message +"Should log ", "Event Type not found to check the duration", "FAIL");
		
		}else if(isEventPresent&&!isDurationPresent){
		
			trackingReport.detailsAppend(message, message +"Should log ", "Event Type present but Duration not found", "FAIL");
		
		}
		
		} catch (Exception e) {
			log.error("Exception", e);
		}
	
		
	}
	public void print(Har nhar) {

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		HarLog hardata = nhar.getLog();
		

		log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		log.info("                       Message files                                  ");
		log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

		List<HarEntry> messageEntries = hardata.getEntries();
		Iterator<HarEntry> messageITR = messageEntries.iterator();

		while (messageITR.hasNext()) {
			HarEntry entry = messageITR.next();
			String requestUrl = entry.getRequest().getUrl();
			//System.out.println("Request Url::  "+requestUrl);
			if (requestUrl.contains("message")) {
				System.out.println(entry.getRequest().getPostData().getText());
				Analytics gameAnalytics;
				try {
					gameAnalytics = mapper.readValue(entry.getRequest().getPostData().getText(), Analytics.class);
					System.out.println("Event Type:"+gameAnalytics.getEventInfo().getEventType());
					System.out.println("Duration :"+gameAnalytics.getEventInfo().getDuration());
				} catch (JsonParseException e) {
					log.error("JsonParseException", e);
				} catch (JsonMappingException e) {
					log.error("JsonMappingException", e);
				} catch (IOException e) {
					log.error("IOException", e);
				}

			}
		}

	}
	
public void alert(WebDriver driver, String alertString){
		
		try {
			((JavascriptExecutor)driver).executeScript("alert('"+alertString+"')");
			//Thread.sleep(3000);
			Alert alert = driver.switchTo().alert();
			alert.accept();
		} catch (Exception e) {
			
		} 
	}
}