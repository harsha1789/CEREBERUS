package Modules.Regression.FunctionLibrary;

import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static io.appium.java_client.touch.offset.PointOption.point;
import static java.time.Duration.ofMillis;
import static org.testng.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.impl.xb.xsdschema.FieldDocument.Field.Xpath;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.Screen;
import org.testng.annotations.Optional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

//import com.sun.media.jfxmediaimpl.platform.osx.OSXPlatform;
import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.driver.MobileDriver;
import com.zensar.automation.framework.library.TestdroidImageRecognition;
import com.zensar.automation.framework.model.Coordinates;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.framework.utils.Util;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.DataBaseFunctions;
import com.zensar.automation.library.ImageLibrary;
import com.zensar.automation.library.TestPropReader;
import com.zensar.automation.model.FreeGameOfferResponse;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarLog;

public class CFNLibrary_Mobile {

	Logger log = Logger.getLogger(CFNLibrary_Mobile.class.getName());

	Properties OR = new Properties();
	WebDriverWait Wait;

	// public RemoteWebDriver webdriver;// changes for selenium grid
	public AppiumDriver<WebElement> webdriver;
	public BrowserMobProxyServer proxy;
	public Mobile_HTML_Report repo1;
	public String GameName;
	public Map<String, String> xpathMap;
	public static final String UTF8_BOM = "\uFEFF";
	public String userName;
	String osVersion;
	String osPlatform;
	public String selectedamout = "";

	public CFNLibrary_Mobile(AppiumDriver<WebElement> webdriver1, BrowserMobProxyServer proxy1, Mobile_HTML_Report tc06,
			String gameName) throws IOException {
		log.info("In mobile super class");
		webdriver = webdriver1;
		proxy = proxy1;
		repo1 = tc06;
		// webdriver.manage().timeouts().implicitlyWait(5, TimeUnit.MINUTES);
		Wait = new WebDriverWait(webdriver, 60);
		String newGameurl = null;
		this.GameName = gameName;

		String testDataExcelPath = TestPropReader.getInstance().getProperty("TestData_Excel_Path");
		ExcelDataPoolManager excelpoolmanager = new ExcelDataPoolManager(testDataExcelPath);
		log.info("Read Excel file");
		Map<String, String> rowData1 = null;
		int rowcount1 = excelpoolmanager.rowCount(testDataExcelPath, GameName);
		log.info("Total row " + rowcount1);

		xpathMap = new HashMap<>();
		for (int i = 1; i < rowcount1; i++) {
			// System.out.println("I value is "+i);
			rowData1 = excelpoolmanager.readExcelByRow(testDataExcelPath, GameName, i);
			String element = rowData1.get("Element").trim();
			String xpath = rowData1.get("Xpath").trim();
			
			xpathMap.put(element, xpath);
		}
		if (TestPropReader.getInstance().getProperty("GameMobileUrl") != null) {
			String gameurl = TestPropReader.getInstance().getProperty("GameMobileUrl");

			log.debug("Before encoding string is=" + gameurl);

			Pattern p = Pattern.compile("\\\\u0026");
			Matcher m = p.matcher(gameurl);

			if (m.find()) {
				newGameurl = m.replaceAll("&");
			} else {
				newGameurl = gameurl;
			}

			log.debug("Decoded Url=" + newGameurl);
			xpathMap.put("ApplicationURL", newGameurl);

		}
	}

	Util clickAt = new Util();

	public void closeOverlay() {

		if (osPlatform.equalsIgnoreCase("iOS")) {
			TouchAction touchAction = new TouchAction(webdriver);
			touchAction.tap(PointOption.point(120, 120)).perform();
		} else {// For Andriod mobile
			Actions act = new Actions(webdriver);
			act.moveByOffset(20, 80).click().build().perform();
			act.moveByOffset(-20, -80).build().perform();

		}

	}

	/*
	 * Date: 07/01/2019 Author:Sneha Jawarkar Description: Freegame_GTR Parameter:
	 * NA
	 */
	public void quickspinwithBasegameFreespin() throws InterruptedException {
		// method signature define in parent class
	}
	/*
	 * Author : Anand Bhayre Description: To fill Spain Start Session Form Param:
	 * Time Limit, Reminder Period, Loss Limit and prevent Future Slot Game for Play
	 * Return: Boolean value
	 */

	public boolean fillSatrtSessionForm(String timeLimit, String reminderPeriod, String lossLimit,
			String preventFuture) {
		boolean ret = false;
		Wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(xpathMap.get("timeLimitInput"))));
		Select sel = new Select(webdriver.findElement(By.xpath(xpathMap.get("timeLimitInput"))));
		sel.selectByVisibleText(timeLimit);
		sel = new Select(webdriver.findElement(By.xpath(xpathMap.get("reminderLimitInput"))));
		sel.selectByVisibleText(reminderPeriod);
		webdriver.findElement(By.xpath(xpathMap.get("lossLimitInput"))).sendKeys(lossLimit);
		sel = new Select(webdriver.findElement(By.xpath(xpathMap.get("preventfutureslotgamefor"))));
		sel.selectByVisibleText(preventFuture);

		webdriver.findElement(By.xpath(xpathMap.get("setlimitbutton"))).click();

		Wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(xpathMap.get("okButton"))));
		webdriver.findElement(By.xpath(xpathMap.get("okButton"))).click();
		closeSavePasswordPopup();
		ret = isElementExist("newFeature", 120);

		return ret;

	}

	/*
	 * Date: 30/04/2019 Description:To verify is_autoplay_window. *Parameter: NA
	 * 
	 * @return boolean
	 */
	public boolean isAutoplayWindow() {

		return true;

	}

	/*
	 * Date: 30/04/2019 Description:To verify autoplay must stop when focus being
	 * removed. Parameter: NA
	 * 
	 * @return boolean
	 */
	public boolean autoplayFocusRemoved() {

		boolean focus = false;
		try {
			webdriver.findElement(By.id(xpathMap.get("AutoplayID"))).click();
			webdriver.findElement(By.id(xpathMap.get("Start_Autoplay_ID"))).click();
			webdriver.runAppInBackground(Duration.ofMinutes(3));
			webdriver.launchApp();
			focus = true;
		}

		catch (Exception e) {
			log.error("Focus not get changed");
			log.error(e.getMessage());

		}
		return focus;

	}

	/**
	 * Date: 26/6/2019 Author: Sneha Jawarkar Description: GTR Reelspin
	 */
	public boolean clickspintostop() {
		return false;
	}

	/* Sneha Jawarkar: Wait for Spin button */
	public boolean waitForSpinButtonstop() {
		return false;
	}

	/**
	 * * Date: 14/05/2019 Author: Sneha Jawarkar. Description: This function is used
	 * in GTR_freegame Parameter: play letter
	 */
	public void clickBaseSceneDiscardButton() {
		// Do nothing , just define in parent class
	}

	/**
	 * * Date: 17/05/2019 Author: Sneha Jawarkar. Description: This function is used
	 * in GTR_freegame Parameter: resume play button
	 */
	public boolean clickFreegameResumePlayButton() {
		return false;
	}

	/**
	 * This method is used to wait till FS scene loads Author: Sneha Jawarkar
	 */
	public boolean freeSpinEntryScene() {
		return false;
	}

	/*
	 * Date: 16/05/2019 Author:Sneha Jawarkar Description: Freegame_GTR Parameter:
	 * NA
	 */
	public boolean completeFreeGameOfferFreespin(int freegamescount) {
		return false;
	}

	/**
	 * Date: 21/6/2019 Author: Sneha Jawarkar Description: GTR Reelspin
	 * 
	 * @throws InterruptedException
	 */
	public long reelSpinspeedDuration() throws InterruptedException {
		long avgspinduration = 0;
		return avgspinduration;
	}

	/*
	 * Date: 16/05/2019 Author:Sneha Jawarkar Description: Freegame_GTR Parameter:
	 * NA
	 */
	public boolean completeFreeGameOffer(int freegamescount) {
		return false;

	}

	/**
	 * * Date: 14/05/2019 Author: Sneha Jawarkar. Description: This function is used
	 * in GTR_freegame Parameter: play letter
	 */
	
	public boolean clickOnPlayLater() {
		return false;
	}
	

	/**
	 * Date:15/5/19 Author:Sneha Jawarkar GTR_Freegame purpose
	 */

	public void backtogameCenterclick() {
		// declearing methode signature
	}

	/*
	 * Date: 16/05/2019 Author:Sneha Jawarkar Description: Freegame_GTR Parameter:
	 * NA
	 */
	public void reelspinDurationDuringWinLoss() throws InterruptedException {
		// declearing methode signature
	}

	/**
	 * Date: 21/6/2019 Author: Sneha Jawarkar Description: GTR Reelspin
	 * 
	 * @throws InterruptedException
	 */
	public long reelSpinSpeedDurationNormal() throws InterruptedException {
		long Avgnormalspinduration = 0;
		return Avgnormalspinduration;
	}

	/**
	 * Date: 21/6/2019 Author: Sneha Jawarkar Description: GTR Reelspin
	 * 
	 * @throws InterruptedException
	 */
	public void reelSpinDifferecneDurationNormalspinQuickspin() throws InterruptedException {
		// declearing methode signature
	}

	/**
	 * Date: 21/6/2019 Author: Sneha Jawarkar Description: GTR Reelspin
	 * 
	 * @throws InterruptedException
	 */
	public long reelspinSpeedDuringAutoplay() {
		long avgAutoplayDuration = 0;
		return avgAutoplayDuration;

	}

	/**
	 * Date: 26/6/2019 Author: Sneha Jawarkar Description: GTR Reelspin
	 * 
	 * @throws Exception
	 */
	public void reelspinInAll() throws Exception { // declearing methode signature
	}

	/*
	 * Date: 5/05/2019 Description:To verify Autoplay after free spin Parameter: NA
	 * 
	 * @return boolean
	 */

	public boolean isAutoplayWithFreespin() {
		boolean freeSpin = false;
		try {
			webdriver.findElement(By.xpath(xpathMap.get("Start_Autoplay"))).click();
			Thread.sleep(5000);
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("spin_button"))));
			log.debug("Free spin over");
			freeSpin = true;
		} catch (Exception e) {
			log.error("Free Spins are not completed");
		}
		return freeSpin;
	}

	/*
	 * Date: 30/04/2019 Description:To verify Auto play on refreshing Parameter: NA
	 * 
	 * @return boolean
	 */
	public boolean autoplayOnRefresh() {
		boolean onrefresh = false;
		try {
			nativeClickByID(xpathMap.get("Start_Autoplay_ID"));
			Thread.sleep(3000);
			webdriver.navigate().refresh();
			funcFullScreen();
			Wait.until(ExpectedConditions.elementToBeClickable(By.id(xpathMap.get("Spin_Button_ID"))));
			log.debug("On refresh previous autoplay session has not resume");
			onrefresh = true;
		} catch (Exception e) {
			log.error("On refresh previous autoplay session has resume");

		}

		return onrefresh;
	}

	/*
	 * Date: 30/04/2019 Description:To verify maximum count of spin. *Parameter: NA
	 * 
	 * @return boolean
	 */
	public boolean maxSpinChk() {
		boolean maxSpin = false;
		try {
			Thread.sleep(5000);
			nativeClickByID(xpathMap.get("AutoplayID"));
			WebElement sizeSlider = webdriver.findElement(By.id(xpathMap.get("SpinSizeSlider_ID")));
			clickWithWebElement(sizeSlider, 500);
			log.debug("drag and drop performed");

			String value2 = webdriver.findElement(By.id(xpathMap.get("SpinCount_ID"))).getText();

			if (value2.equals("100")) {
				maxSpin = true;
			} else {
				maxSpin = false;
			}

		} catch (Exception e) {
			log.error("Session not over after autoplay spin", e);

		}
		return maxSpin;
	}

	/*
	 * Date: 30/04/2019 Description:To verify Autoplay spin session stop Parameter:
	 * NA
	 * 
	 * @return boolean
	 */
	public boolean isAutoplaySessionEnd() {

		boolean spinSession = false;

		try {

			WebElement e = webdriver.findElement(By.id("spinsSlidertrack"));
			Thread.sleep(3000);
			clickWithWebElement(e, -400);
			nativeClickByID(xpathMap.get("Start_Autoplay_ID"));
			log.debug("Clicked Auto paly..");
			Wait.until(ExpectedConditions.elementToBeClickable(By.id(xpathMap.get("Spin_Button_ID"))));
			spinSession = true;

		} catch (Exception e) {
			log.error("Session not over after autoplay spin", e);
		}
		return spinSession;
	}

	/*
	 * Date: 30/04/2019 Description:To verify Autoplay spin selection Parameter: NA
	 * 
	 * @return boolean
	 */

	public boolean autoplaySpinSelection() {
		boolean spinAutoplay = false;
		try {
			WebElement e = webdriver.findElement(By.id("spinsSlidertrack"));
			clickWithWebElement(e, 200);
			spinAutoplay = true;
		} catch (Exception e) {
			log.error("Spin count not getting change.", e);
		}
		return spinAutoplay;
	}

	/*
	 * Date: 30/04/2019 Description:To verify Autoplay with quick spin on Parameter:
	 * NA
	 * 
	 * @return boolean
	 */

	public boolean autoPlayWithQSOn() {
		boolean qsoff = webdriver.findElement(By.xpath(xpathMap.get("QuickSpin_Off"))).isDisplayed();
		WebElement qsoffele = webdriver.findElement(By.xpath(xpathMap.get("QuickSpin_Off")));
		if (qsoff) {
			qsoffele.click();

			boolean qson = webdriver.findElement(By.xpath(xpathMap.get("QuickSpin_On"))).isDisplayed();
			if (qson) {
				webdriver.findElement(By.id(xpathMap.get("AutoplayID"))).click();
			}

		}
		return qsoff;

	}

	public boolean ISAutoplayAvailable() {
		boolean autoplay;
		try {
			autoplay = webdriver.findElement(By.id(xpathMap.get("AutoplayID"))).isDisplayed();
		} catch (Exception e) {
			log.error("Autoplay id button is not visible", e);
			autoplay = false;
		}
		return autoplay;
	}

	public void waitBonus() {
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("M_Bonus"))));
	}

	public String functNavigateURLCacheClear(String url) throws Exception {
		String ret = null;
		try {
			funcLandscape();
			webdriver.get(url);
			Thread.sleep(30000);
			ret = webdriver.getTitle();
		} catch (Exception e) {
			e.printStackTrace();
			evalException(e);
		}
		return ret;
	}

	public String responsiblepara1() {
		String para1 = null;
		try {
			para1 = webdriver.findElement(By.xpath(xpathMap.get("responsibleParagraph1"))).getText();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return para1;
	}

	public String functNavigateURLnewPortrait(String url) throws Exception {
		String ret = null;
		try {
			funcPortrait();
			webdriver.get(url);
			Thread.sleep(30000);
			ret = webdriver.getTitle();
		} catch (Exception e) {
			e.printStackTrace();
			evalException(e);
		}
		return ret;
	}

	public boolean fillSatrtSessionForm1(String timeLimit, String reminderPeriod, String lossLimit,
			String preventFuture) {
		boolean ret = false;
		Wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(xpathMap.get("timeLimitInput"))));
		Select sel = new Select(webdriver.findElement(By.xpath(xpathMap.get("timeLimitInput"))));
		sel.selectByVisibleText(timeLimit);
		sel = new Select(webdriver.findElement(By.xpath(xpathMap.get("reminderLimitInput"))));
		sel.selectByVisibleText(reminderPeriod);
		webdriver.findElement(By.xpath(xpathMap.get("lossLimitInput"))).sendKeys(lossLimit);
		sel = new Select(webdriver.findElement(By.xpath(xpathMap.get("preventfutureslotgamefor"))));
		sel.selectByVisibleText(preventFuture);
		Wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(xpathMap.get("setlimitbutton"))));
		closeSavePasswordPopup();
		ret = isElementExist("setlimitbutton", 120);

		return ret;
	}

	public String refreshpageResponsible() {
		// refresh page
		String refresh = null;
		WebElement slotgametitle;
		try {
			slotgametitle = webdriver.findElement(By.xpath(xpathMap.get("SlotGameLimit")));
			refresh = slotgametitle.getText();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return refresh;
	}

	public String refreshPageSlotGameSummary() {
		String refresh = null;
		WebElement slotgametitle;
		webdriver.navigate().refresh();
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("responsiblegaming"))));
		slotgametitle = webdriver.findElement(By.xpath(xpathMap.get("responsiblegaming")));
		refresh = slotgametitle.getText();
		return refresh;
	}

	/*
	 * Author: Anand Bhayre Description: Click on given element Param: Element Xpath
	 * Key -Target -Expected Return: boolean value
	 */
	public boolean clickOnElement(String targetElementKey, String expectedElementKey) {
		Wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathMap.get(targetElementKey)))).click();
		return isElementExist(expectedElementKey, 60);
	}

	public String verifyFlag() throws InterruptedException {
		nativeClickByID(xpathMap.get("Denamrk_FlagID"));
		return (webdriver.findElement(By.id(xpathMap.get("Denamrk_FlagID"))).getText());
	}

	/*
	 * Date: 03/04/2017 Author: Author: Dhairaya Gautam Description: This function
	 * used for page load Parameter: NA
	 */
	public String verifyResponsibleLinkworking() {
		return (webdriver.findElement(By.id("responsibleGaming")).getText());
	}

	public String getText(String elementKeyName) {
		return webdriver.findElement(By.xpath(xpathMap.get(elementKeyName))).getText();
	}

	// Navigation
	public boolean nativeClickByID(String locator) throws InterruptedException {
		boolean present = false;
		try {
			String context = webdriver.getContext();

			try {
				Wait.until(ExpectedConditions.elementToBeClickable(By.id(locator)));
			} catch (Exception e) {
				log.error("Unable to find" + locator, e);
				// System.out.println("Unable to find"+locator);
			}
			MobileElement element = (MobileElement) webdriver.findElement(By.id(locator));

			if (element != null) {
				if (osPlatform.equalsIgnoreCase("Android")) {
					element.click();
					Thread.sleep(6000);
					System.out.println("Element Found");
					log.debug("Element Found");
					// clickWithWebElement(element, 0);
				} else {
					// for IOS device

					webdriver.executeScript("arguments[0].click();", element);
					Thread.sleep(6000);
					System.out.println("Element Found");
					log.debug("Element Found");

					// element.click();

					/*
					 * Actions actions = new Actions(webdriver); actions.click(element);
					 * actions.build().perform();
					 */
					// JavascriptExecutor js = (JavascriptExecutor) webdriver;
					// js.executeScript("javascript:document.getElementById(xpathMap.get(locator)");

					// IOSElement btn = (IOSElement)new WebDriverWait(webdriver ,
					// 30).until(ExpectedConditions.elementToBeClickable(MobileBy.id(locator)));
					// btn.click();
					// clickWithWebElement(element, 0);
					// element.click();

				}

				present = true;
			}
			webdriver.context(context);
		} catch (NoSuchElementException e) {
			System.out.println(e.getMessage());
			System.out.println("Unable to Navigate to the clicked Page");
			log.debug("Unable to Navigate to the clicked Page");
			present = false;
		}
		return present;
	}
	/*
	 * Author: Anand Description: To test element is present or not Param: Key of
	 * element's xpath from OR Return: Boolean value
	 */

	public boolean isElementExist(String keyName, int timeInSeconds) {
		boolean ret = false;
		try {
			webdriver.manage().timeouts().implicitlyWait(timeInSeconds, TimeUnit.SECONDS);
			if (!webdriver.findElements(By.xpath(xpathMap.get(keyName))).isEmpty()) {
				ret = true;
			}
		} catch (Exception e) {
			webdriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		}
		return ret;
	}

	/**
	 * 
	 * Autohr: Havish Jain Description: This function used for navigating to
	 * Gibraltar url
	 * 
	 * @return true
	 */
	public String funcNavigateDirectURL(String urlNavigate) {
		Wait = new WebDriverWait(webdriver, 500);
		String title = null;
		try {
			webdriver.context("NATIVE_APP");
			webdriver.rotate(ScreenOrientation.LANDSCAPE);
			webdriver.context("CHROMIUM");
			webdriver.navigate().to(urlNavigate);
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("Spin_Button_ID"))));
			funcFullScreen();
			title = webdriver.getTitle();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return title;
	}

	/**
	 * Autohr: Havish Jain Description: This function used for verifying the help
	 * icon in Gibraltar reg market
	 * 
	 * @return
	 */

	public boolean verifyHelp() throws InterruptedException {

		return false;
	}

	/**
	 * Autohr: Havish Jain Description: This function used for clicking help icon in
	 * Gibraltar reg market
	 * 
	 * @return
	 */
	public String clickHelp() {

		String googleTitle = null;

		googleTitle = webdriver.getTitle();

		return googleTitle;
	}

	/*
	 * Date: 13/11/2017 Author: Ashish Kshatriya Description: This function is used
	 * for send keys on text box Parameter: By locator, String keys
	 */
	public boolean funcSendClick(By locator, String keys) {
		try {
			WebElement ele = webdriver.findElement(locator);
			if (ele != null) {
				ele.sendKeys(keys);
			}
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public String funcNavigate(String applicationName, String url) {
		String gameDesktopName = null;
		try {
			webdriver.manage().deleteAllCookies();
			webdriver.get(url);
			Wait.until(ExpectedConditions.elementToBeClickable(By.id(xpathMap.get("LobbyfiveReels"))));
			webdriver.findElement(By.xpath(xpathMap.get("LobbyNewGames"))).click();

			Wait.until(ExpectedConditions.elementToBeClickable(By.id(applicationName)));
			webdriver.findElement(By.id(applicationName)).click();
			gameDesktopName = webdriver.findElement(By.id(applicationName)).getText();
			log.info("application name" + gameDesktopName);
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("loginPopUp"))));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gameDesktopName;
	}

	/*
	 * Date: 03/04/2017 Author: Author: Dhairaya Gautam Description: This function
	 * used for page load Parameter: NA
	 */
	public String funcLogoutSpain() {
		String loginTitle = null;
		try {

			Wait.until(ExpectedConditions.elementToBeClickable(By.id(xpathMap.get("accountsLobbyID")))).click();
			;
			Thread.sleep(2000);

			webdriver.findElement(By.id(xpathMap.get("logoutbuttonId"))).click(); // Clicking on log out button
			Thread.sleep(1000);
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("LoginTitleid"))));
			loginTitle = webdriver.findElement(By.id(xpathMap.get("LoginTitleid"))).getText(); // Clicking on log out
																								// button
			Thread.sleep(1000);
			webdriver.findElement(By.id(xpathMap.get("closeButtonid"))).click(); // Clicking on log out button
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginTitle;
	}

	/**
	 * Date:07/12/2017 Author:premlata mishra This method is to get win amount
	 * 
	 * @return true
	 */
	public String getWinAmt() {
		return null;
	}

	public String getBetAmt() {
		return null;
	}
	/*
	 * Date: 11/04/2017 Author:Robin Dulhani Description: This function verifies the
	 * working of the TOGGLE TOPBAR. Parameter: imagepath
	 */

	public String verifyToggleTopbar() {
		String topbarData = null;
		try {

			Thread.sleep(1000);
			nativeClickByID(xpathMap.get("Toggle_TopBar_ID"));
			webdriver.switchTo().frame(webdriver.findElement(By.name("commonUIIFrame")));
			String stake = webdriver.findElement(By.id("stake")).getText() + "!";
			String paid = webdriver.findElement(By.id("paid")).getText() + "!";
			String balance = webdriver.findElement(By.id("balance")).getText() + "!";
			topbarData = stake + paid + balance;

		} catch (Exception e) {
			e.printStackTrace();

		}
		return topbarData;
	}

	/**
	 * Date: 03/04/2017 Author: Kamal Kumar Vishwakarma Description: Parameter:
	 */
	public int[] getAttributes(String nodeValue, String attribute1, String attribute2, String attribute3) {

		int[] data = new int[4];
		String balance = null;
		String loyaltyBalance = null;
		String totalWin = null;
		int totalWinNew = 0;
		try {
			waitForPageToBeReady();
			Thread.sleep(1000);
			clickAt(xpathMap.get("spinButtonPixels"));
			Thread.sleep(15000);
			String dataFromHar = clickAt.getData(nodeValue, attribute1, attribute2, attribute3, proxy);
			balance = dataFromHar.split(",")[0];
			loyaltyBalance = dataFromHar.split(",")[1];
			totalWin = dataFromHar.split(",")[2];
			totalWinNew = Integer.parseInt(totalWin);

			data[0] = Integer.parseInt(balance);
			data[1] = Integer.parseInt(loyaltyBalance);
			data[2] = Integer.parseInt(totalWin);
			data[3] = totalWinNew;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	/*
	 * Date: 03/04/2017 Author: Author: Dhairaya Gautam Description: This function
	 * used for page load Parameter: NA
	 */
	public boolean navigateSettingsUK() {
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("canvasxPath"))));
			Thread.sleep(1000);
			clickAt(xpathMap.get("bargurMenu"));
			Thread.sleep(500);
			clickAt(xpathMap.get("settingsUK"));
			Thread.sleep(500);
		} catch (Exception exception) {
			log.error(exception);
		}
		return true;
	}

	/*
	 * Date: 03/04/2017 Author: Author: Kanchan B Description: This function use to
	 * open URL page load Parameter: url
	 */
	public void openUrl(String url) {
		try {
			webdriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			webdriver.manage().window().maximize();
			webdriver.get(url);

			Thread.sleep(20000);
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("canvasxPath"))));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Date: 03/04/2017 Author: Author: Dhairaya Gautam Description: This function
	 * used for page load Parameter: NA
	 */

	public boolean clickok() {
		boolean close = false;
		close = webdriver.findElements(By.xpath(xpathMap.get("LossLimitOverOk"))).isEmpty();
		if (!close) {
			webdriver.findElement(By.xpath(xpathMap.get("LossLimitOverOk"))).click();
			close = true;
		}
		return close;
	}

	/*
	 * Date: 06/04/2017 Author: Ashish Kshatriya Description:Verify
	 * SlotGameSession,TotalWagered,TotalPayouts & TotalBalance for regulatory
	 * market Spain Parameter: N/A
	 */
	public boolean lossLimitPopupOverLay() throws InterruptedException {
		try {
			boolean popup = webdriver.findElements(By.id(xpathMap.get("LossLimitOverPopup"))).isEmpty();
			while (!popup) {
				clickAt.clickAt(xpathMap.get("spinButtonPixels"), webdriver, "");
				Thread.sleep(3000);
				boolean popup1 = webdriver.findElements(By.id(xpathMap.get("LossLimitOverPopup"))).isEmpty();
				if (!popup1) {
					break;
				}
			}
			boolean ok = webdriver.findElements(By.id(xpathMap.get("LossLimitOverOk"))).isEmpty();
			if (!ok) {
				webdriver.findElement(By.id(xpathMap.get("LossLimitOverOk"))).click();
			}
			boolean close = webdriver.findElements(By.id(xpathMap.get("LossLimitClose"))).isEmpty();
			if (!close) {
				webdriver.findElement(By.id(xpathMap.get("LossLimitClose"))).click();
			}
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/*
	 * Date: 06/04/2017 Author: Kanchan B Description: This function Navigate Game
	 * application by taking application name Parameter: applicationName to search
	 * in different page
	 */
	public String funcNavigate(String urlNavigate) throws InterruptedException {
		try {

			webdriver.navigate().to(urlNavigate);
			Thread.sleep(5000);
			/*
			 * JavascriptExecutor js = ((JavascriptExecutor)webdriver); String javascript =
			 * "return arguments[0].innerHTML"; String
			 * pageSource=(String)js.executeScript(javascript,
			 * webdriver.findElement(By.tagName("html"))); //System.out.println(pageSource);
			 * if (pageSource.contains("Arthur")) {
			 * log.debug("Inside Microgaming Skin fuctionality");
			 * js.executeScript("arguments[0].scrollIntoView(true);",webdriver.findElement(
			 * By.xpath(XpathMap.get("GameName_Arthur"))));
			 * js.executeScript("arguments[0].click();",
			 * webdriver.findElement(By.id(GameName))); if
			 * (webdriver.findElement(By.xpath(XpathMap.get("PlayNow_Arthur"))).isDisplayed(
			 * )) {
			 * webdriver.findElement(By.xpath(XpathMap.get("GameName_Arthur"))).click(); } }
			 * else { log.debug("Inside Mint Skin block");
			 * 
			 * js.executeScript("arguments[0].scrollIntoView(true);",webdriver.findElement(
			 * By.id(GameName)));
			 * 
			 * js.executeScript("arguments[0].click();",
			 * webdriver.findElement(By.id(GameName))); }
			 * Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.
			 * get("Login"))));
			 */
			// Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("OneDesign_NewFeature_ClickToContinue"))));
			// new WebDriverWait(webdriver,
			// 60).until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("Spin_Button_ID"))));
			log.debug("entered on lobby and clicked on game");
		} catch (Exception e) {
			log.error("Error in navigating url", e);
		}
		return GameName;
	}

	public boolean funcPortrait() throws Exception
	{
		boolean ret = false;
		try{
			String curContext=webdriver.getContext();
			webdriver.context("NATIVE_APP");			
			try {
				webdriver.rotate(ScreenOrientation.PORTRAIT);
			    } catch (WebDriverException ignore) {
			    //Ignore the exception
			}
			webdriver.context(curContext);
			ret=true;
		}
		catch(Exception e)
		{
			evalException(e);
			log.error("Error :" + e);
		}
		return ret;
	}

	public boolean funcLandscape() throws Exception
	{
		boolean ret = false;
		try{
			String curContext=webdriver.getContext();
			webdriver.context("NATIVE_APP");
			try {
				webdriver.rotate(ScreenOrientation.LANDSCAPE); 
			    } catch (WebDriverException ignore) {
			    //Ignore the exception
			}
			webdriver.context(curContext);
			ret=true;
		}
		catch(Exception e)
		{
			evalException(e);
			log.error("Error :" + e);
		}
		return ret;
	}

	/* game play error handling */

	public String funGamePlayError() {
		String gameplay = null;
		try {
			gameplay = webdriver.findElement(By.xpath(xpathMap.get("gameplayerror"))).getText();

		} catch (Exception t1) {
			t1.getMessage();
		}
		return gameplay;
	}

	/*
	 * Date: 10/04/2017 Author: Author: Ashish Kshatriya Description: This function
	 * is used for get Data from the Har Parameter: NA
	 */
	public String parseXML(String nodeValue, String attribute1, String attribute2, String attribute3, String xmlData)
			throws SAXException, IOException, ParserConfigurationException {
		String dataList = "";
		Node nNode;
		NodeList nList;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(xmlData));
		Document doc = dBuilder.parse(is);
		doc.getDocumentElement().normalize();
		log.debug("Root element :" + doc.getDocumentElement().getNodeName());
		nList = doc.getElementsByTagName(nodeValue);
		nNode = nList.item(0);
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) nNode;
			dataList += eElement.getAttribute(attribute1) + ",";
			dataList += eElement.getAttribute(attribute2) + ",";
			dataList += eElement.getAttribute(attribute3);
		}
		return dataList;
	}

	public void checkPage(Mobile_HTML_Report tc10) {
		try {
			webdriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			boolean test = webdriver.findElements(By.xpath(xpathMap.get("spain_StartNewSession"))).isEmpty();
			if (!test) {
				tc10.detailsAppend("Verify that Start New Sessiosn Overlay appears before the game loads ",
						"Start New Sessiosn Overlay should appear before the game loads",
						"Start New Sessiosn Overlay is appearing before the game loads", "Pass");
				funcClick(xpathMap.get("spain_StartNewSession"));
			} else {
				log.debug("Slot Game limit overlay appears");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String funcLoginNew(String username, String password, String applicationName) throws Exception {
		String gameDesktopName;
		Wait.until(ExpectedConditions.elementToBeClickable(By.id(xpathMap.get("LobbyfiveReels"))));

		webdriver.findElement(By.id(xpathMap.get("LobbyfiveReels"))).click();

		gameDesktopName = webdriver.findElement(By.id(applicationName)).getText();
		log.debug("application name " + gameDesktopName);
		webdriver.findElement(By.id(applicationName)).click();

		Wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathMap.get("GameUserName"))));
		webdriver.findElement(By.xpath(xpathMap.get("GameUserName"))).clear();
		webdriver.findElement(By.xpath(xpathMap.get("GameUserName"))).sendKeys(username);
		webdriver.findElement(By.xpath(xpathMap.get("GamePassword"))).clear();
		webdriver.findElement(By.xpath(xpathMap.get("GamePassword"))).sendKeys(password);
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("GameLogin"))));
		webdriver.findElement(By.xpath(xpathMap.get("GameLogin"))).click();
		Thread.sleep(5000);
		String titlenew = webdriver.getTitle();
		closeSavePasswordPopup();
		closeSavePasswordPopup();

		return titlenew;
	}

	/* Verify the last Login Pop up */

	public boolean lastLoginPopup() {

		Wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpathMap.get("loadingContainer"))));
		WebElement lastlogin = webdriver.findElement(By.xpath(xpathMap.get("lastLoginPopup")));
		lastlogin.click();

		return true;
	}

	public String tapAt(String elementKey) throws InterruptedException {
		double userwidth;
		double userheight;
		int userElementX;
		int userElementY;
		int Ty;
		int Tx;
		String userdata = xpathMap.get(elementKey);
		String[] coordinates1 = userdata.split(",");
		userwidth = Integer.parseInt(coordinates1[0]);
		userheight = Integer.parseInt(coordinates1[1]);
		userElementX = Integer.parseInt(coordinates1[2]);
		userElementY = Integer.parseInt(coordinates1[3]);

		webdriver.context("NATIVE_APP");
		double deviceHeight = webdriver.manage().window().getSize().getHeight();
		double deviceWidth = webdriver.manage().window().getSize().getWidth();
		log.debug("Device H=" + deviceHeight + " Device W=" + deviceWidth);
		Tx = (int) ((deviceWidth / userwidth) * userElementX);
		Ty = (int) ((deviceHeight / userheight) * userElementY);

		webdriver.context("CHROMIUM");
		Thread.sleep(5000);
		return Tx + "," + Ty;
	}

	public String functNavigateURLnew(String url) throws Exception {

		String ret = null;

		try {
			funcLandscape();
			webdriver.get(url);
			ret = "Spain";

		} catch (Exception e) {
			e.printStackTrace();
			evalException(e);

		}
		return ret;
	}

	/*
	 * Date: 04/05/2017 Author: Anish Description: This function Func_Login use to
	 * perform login operation Commented By :Kanchan Parameter: applicationName and
	 * url
	 */
	public String funcLogin(String userName, String password) {
		String title = null;
		try {
			webdriver.findElement(By.xpath(xpathMap.get("GameUserName"))).clear();
			webdriver.findElement(By.xpath(xpathMap.get("GameUserName"))).sendKeys(userName);
			webdriver.findElement(By.xpath(xpathMap.get("GamePassword"))).clear();
			webdriver.findElement(By.xpath(xpathMap.get("GamePassword"))).sendKeys(password);
			webdriver.findElement(By.xpath(xpathMap.get("GameLogin"))).click();
			Thread.sleep(3000);
			closeSavePasswordPopup();
			title = webdriver.getTitle();
			log.debug("The Title is" + title);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return title;
	}

	/*
	 * Date: 18/04/2017 Author: Ashish Kshatriya Description: This function is used
	 * for Swipe max Parameter:Swipe Max Coin Size
	 */
	public boolean swipeMaxCoinSize(double maxCoinSize) throws Exception {
		try {
			clickAt(xpathMap.get("SelectCredits"));
			Thread.sleep(300);
			clickAt(xpathMap.get("SelectCoins"));
			Thread.sleep(500);
			clickAt(xpathMap.get("CoinSize"));
			Thread.sleep(500);
			for (double i = 0; i < maxCoinSize; i++) {
				clickAt(xpathMap.get("CoinMax"));
				Thread.sleep(500);
			}
			clickAt(xpathMap.get("CoinSize"));
			Thread.sleep(500);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/*
	 * Date: 18/04/2017 Author: Ashish Kshatriya Description: This function is used
	 * for Swipe min Parameter:Swipe Min Coin Size
	 */
	public boolean swipeMinCoinSize(double minCoinSize) throws Exception {
		try {
			clickAt(xpathMap.get("SelectCredits"));
			Thread.sleep(300);
			clickAt(xpathMap.get("SelectCoins"));
			Thread.sleep(500);
			clickAt(xpathMap.get("CoinSize"));
			Thread.sleep(500);
			for (double i = 0; i < minCoinSize; i++) {
				clickAt(xpathMap.get("CoinMin"));
				Thread.sleep(500);
			}
			clickAt(xpathMap.get("CoinSize"));
			Thread.sleep(500);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/*
	 * Date: 18/04/2017 Author: Ashish Kshatriya Description: This function is used
	 * for Swipe min Bet Size Parameter:Swipe Min Bet Size
	 */
	public boolean swipeMinBetSize(double minBetSize) {
		try {
			clickAt.clickAt(xpathMap.get("SelectCredits"), webdriver, "");
			Thread.sleep(300);
			clickAt.clickAt(xpathMap.get("SelectCoins"), webdriver, "");
			Thread.sleep(500);
			clickAt.clickAt(xpathMap.get("BetSize"), webdriver, "");
			Thread.sleep(500);
			for (int i = 0; i < minBetSize; i++) {
				clickAt.clickAt(xpathMap.get("BetMin"), webdriver, "");
				Thread.sleep(500);
			}
			clickAt.clickAt(xpathMap.get("BetSize"), webdriver, "");
			Thread.sleep(500);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/*
	 * Date: 18/04/2017 Author: Ashish Kshatriya Description: This function is used
	 * for Swipe max Parameter:Swipe Max Bet Size
	 */
	public boolean swipeMaxBetSize(double maxBetSize) throws Exception {
		try {
			clickAt(xpathMap.get("SelectCredits"));
			Thread.sleep(300);
			clickAt(xpathMap.get("SelectCoins"));
			Thread.sleep(500);
			clickAt(xpathMap.get("BetSize"));
			Thread.sleep(500);
			for (double i = 0; i < maxBetSize; i++) {
				clickAt(xpathMap.get("BetMax"));
				Thread.sleep(500);
			}
			clickAt(xpathMap.get("BetSize"));
			Thread.sleep(500);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/*
	 * Date: 04/12/2017 Author:Kanchan Badhiye Description: openhambermenu use to
	 * click on menu screen Parameter: NA
	 */

	public boolean openhambermenu() {
		try {
			clickAt(xpathMap.get("bargurMenu"));
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Date: 13/11/2017 Author: Ashish Kshatriya Description: This function is used
	 * for click on element Parameter: By locator
	 */
	public boolean funcClick(String locator) 
	{
		boolean abletoClick=false;
		try 
		{	
			//webdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			WebElement ele = webdriver.findElement(By.xpath(locator));
			//WebElement ele1 = webdriver.findElement(By.id(locator));
		//	MobileElement ele1 = (MobileElement) webdriver.findElement(By.id(locator));
			if (ele != null) 
			{
				ele.click();
				abletoClick=true;
			}
			
		} catch (NoSuchElementException e) 
		{
			log.error(e);
		}
			return abletoClick;
		
	}

	/**
	 * Date: 30/05/2018 Autohr: Havish Jain Description: This function used to
	 * launch game with Practice Play
	 * 
	 * @return Title
	 * 
	 */

	public String loginWithPracticePlay() {
		Wait = new WebDriverWait(webdriver, 500);
		String title = null;
		try {
			funcClick(xpathMap.get("practice_Play"));
			closeOverlay();
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("func_FullScreen"))));
			title = webdriver.getTitle();
			funcFullScreen();

		} catch (Exception e) {
			e.printStackTrace();

		}
		return title;
	}

	public boolean openTotalBet() {
		return false;
		// declearing method signature

	}

	public void closeTotalBet() throws InterruptedException {
		// declearing method signature
	}

	/**
	 * Date: 29/05/2018 Author: Havish Jain Description: This function is used to
	 * refresh the page and will take screenshot of splash screen before navigating
	 * to Base Scene. Parameter:
	 * 
	 */
	public boolean splashScreen(Mobile_HTML_Report report, String language) {
		boolean status = false;
		Wait = new WebDriverWait(webdriver, 30);
		try {
			webdriver.navigate().refresh();
			
			if("Yes".equalsIgnoreCase(xpathMap.get("LoadingScreen"))) 
			{
				elementWait("return "+xpathMap.get("currentScene"), "LOADING");
			}
			else 
			{
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("preLoaderBackgroundID"))));
			Thread.sleep(1000);
			}
			report.detailsAppendFolder("verify the Splash Screen", "Splash Screen should display",
					"Splash screen is displaying", "pass", language);
			log.debug("Splash screen screenshot has taken");
			if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame"))
					|| (Constant.YES.equalsIgnoreCase(xpathMap.get("continueBtnOnGameLoad")))) {
				
				
				if("Yes".equalsIgnoreCase(xpathMap.get("CntBtnNoXpath"))) 
				{	
					elementWait("return "+xpathMap.get("CntBtnNoXpathSatus"), true);
					
				}
				else
				{
				Wait.until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath(xpathMap.get("ONEDESIGN_NEWFEATURE_CLICKTOCONTINUE"))));
			   }
				
			} else {
				Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get(Constant.CLOCK))));
			}
			status = true;
		} catch (TimeoutException timeoutException) {
			log.error("exception in splshScreen method: " + timeoutException.getMessage(), timeoutException);
		} catch (Exception e) {
			log.error("exception in splshScreen method", e);
		}
		return status;

	}

	/*
	 * Date: 03/04/2017 Author: Author: Dhairaya Gautam Description: This function
	 * used for page load Parameter: NA
	 */
	public String loginToDenmark(String username, String password) {
		String title = null;
		try {
			Thread.sleep(10000);
			webdriver.findElement(By.xpath(xpathMap.get("GameUserName"))).clear();
			Thread.sleep(500);
			webdriver.findElement(By.xpath(xpathMap.get("GameUserName"))).sendKeys(username);
			Thread.sleep(500);
			webdriver.findElement(By.xpath(xpathMap.get("GamePassword"))).clear();
			Thread.sleep(500);
			webdriver.findElement(By.xpath(xpathMap.get("GamePassword"))).sendKeys(password);
			Thread.sleep(500);
			webdriver.findElement(By.xpath(xpathMap.get("GameLogin"))).click();
			Thread.sleep(10000);
			try {
				boolean regulaoryLastLogin = webdriver.findElements(By.xpath(xpathMap.get("regulatoryLastLoginPopup")))
						.isEmpty();
				if (!regulaoryLastLogin) {
					webdriver.findElement(By.xpath(xpathMap.get("GameLogin"))).click();
					Thread.sleep(3000);
					webdriver.findElement(By.xpath(xpathMap.get("regulatoryClose"))).click();

				}
			} catch (Exception e) {
				log.error("Error :" + e);
			}

			Thread.sleep(30000);
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inGameClock")));
			Thread.sleep(500);
			clickAt(xpathMap.get("newfeatures"));
			title = webdriver.getTitle();
			log.info("The Title is " + title);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return title;
	}

	/*
	 * Date: 03/04/2017 Author: Author: Dhairaya Gautam Description: This function
	 * used for page load Parameter: NA
	 */

	public String navigateToRegulatoryMarket(String applicationName, String url) {
		String gameDesktopName = null;
		try {
			Wait = new WebDriverWait(webdriver, 120);
			webdriver.manage().deleteAllCookies();
			webdriver.manage().window().maximize();
			webdriver.get(url);
			waitForPageToBeReady();
			JavascriptExecutor js = (JavascriptExecutor) webdriver;
			js.executeScript("arguments[0].scrollIntoView(true);",
					webdriver.findElement(By.xpath(xpathMap.get("LobbyfiveReels"))));
			Thread.sleep(4000);
			js.executeScript("arguments[0].scrollIntoView(true);", webdriver.findElement(By.id(applicationName)));
			Thread.sleep(1000);
			gameDesktopName = webdriver.findElement(By.id(applicationName)).getText();
			webdriver.findElement(By.id(applicationName)).click();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gameDesktopName;
	}

	/*
	 * Date: 07/04/2017 Author: Dhairaya Gautam Description: Method to verify
	 * responsible gaming link present in Denmark regulatory markets Parameter:image
	 * path
	 *
	 */
	public boolean verifyResponsibleLink(String imagepath) throws InterruptedException {
		Screen screen = new Screen();
		org.sikuli.script.Pattern image = new org.sikuli.script.Pattern(imagepath);
		try {
			Thread.sleep(2000);
			screen.exists(image);
			Thread.sleep(2000);
			closeMenu();
			/*
			 * clickAt(XpathMap.get("responsiblegmaingclick")); Thread.sleep(15000);
			 * if(webdriver.getTitle().equalsIgnoreCase("Responsible Gaming")){
			 * title=webdriver.getTitle();
			 * System.out.println("Resonsible Gaming link is present"); } else{
			 * title=webdriver.getTitle(); System.out.println(title);
			 * /*webdriver.navigate().back(); webdriver.navigate().refresh();
			 * Thread.sleep(30000); closeMenu(); }
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/*
	 * Date: 03/04/2017 Author: Author: Dhairaya Gautam Description: This function
	 * used for page load Parameter: NA
	 */
	public boolean loginToItalyMarket(String username, String password) {
		Wait = new WebDriverWait(webdriver, 500);
		String title = null;
		boolean present = false;

		try {
			webdriver.findElement(By.xpath(xpathMap.get("userName"))).clear();
			webdriver.findElement(By.xpath(xpathMap.get("userName"))).sendKeys(username);
			Thread.sleep(1500);
			webdriver.findElement(By.xpath(xpathMap.get("password"))).clear();
			webdriver.findElement(By.xpath(xpathMap.get("password"))).sendKeys(password);
			Thread.sleep(1500);
			webdriver.findElement((By.xpath(xpathMap.get("Login")))).click();
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("italyHeader"))));
			title = webdriver.getTitle();
			if (title != null) {

				present = true;
			} else {
				present = false;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return present;
	}

	/*
	 * Date: 03/04/2017 Author: Author: Anish Description: This function used for
	 * page load Parameter: NA
	 */
	public String javaScriptExecutor2(String js) {

		String jsResultFinal1;
		String jsResult1;
		JavascriptExecutor jse = webdriver;
		jsResult1 = (String) jse.executeScript("return " + js);
		jsResultFinal1 = jsResult1.replace("$", "");
		log.debug("The Credit Balance is :" + jsResultFinal1);
		return jsResultFinal1;

	}

	public void gameToLobby() {
		try {
			closeTotalBet();
			String lobbyX = "return mgs.mobile.casino.slotbuilder.v1.automation.getControlById('MenuIconPanelComponent.lobbyButton').centerX";
			String lobbyY = "return mgs.mobile.casino.slotbuilder.v1.automation.getControlById('MenuIconPanelComponent.lobbyButton').centerY";
			clickAtCoordinates(lobbyX, lobbyY);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	/*
	 * Date: 03/04/2017 Author: Author: Dhairaya Gautam Description: This function
	 * used for page load Parameter: NA
	 */
	public String clickamount(Mobile_HTML_Report italy, String amount) {
		try {
			Thread.sleep(1000);
			webdriver.findElement(By.id("userInput")).sendKeys(amount);
			Thread.sleep(3000);
			nativeClickByID(xpathMap.get("italySubmit_ID"));
			Thread.sleep(4000);
			italy.detailsAppend(" Verify that Take to Game screen appears ", " Take to Game screen should appear ",
					" Take to Game screen is appearing ", "Pass");
			nativeClickByID(xpathMap.get("italySubmit_ID"));
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("clock_ID"))));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return amount;
	}

	public void jackpotSceneWait() {
		// declearing method signature
	}

	public boolean elementWait(String element, String currentSceneText) {

		while (true) {
			String currentScene = GetConsoleText(element);
			if (currentScene != null && currentScene.equalsIgnoreCase(currentSceneText)) {
				return true;
			}
		}
	}

	public boolean spinclick() throws InterruptedException {

		try {
			clickAt.clickAt(OR.getProperty("spinButtonPixels"), webdriver, "//*[@id='gameCanvas']");
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Date: 14/11/2017 Author:Premlata Mishra Description: This function is used
	 * verify incomplete game dialogue Parameter:String applicationName,String
	 * urlNavigate
	 */
	public String incompleteGameName(String gamename) {
		String incompleteGameName = null;
		try {
			Wait = new WebDriverWait(webdriver, 20);
			JavascriptExecutor js = webdriver;
			js.executeScript("arguments[0].scrollIntoView(true);", webdriver.findElement(By.id(gamename)));

			js.executeScript("arguments[0].click();", webdriver.findElement(By.id(gamename)));
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("defaultOverlayContent")));
			incompleteGameName = webdriver.findElement(By.id("defaultOverlayContent")).getText();
		} catch (Exception e) {
			incompleteGameName = null;
		}
		log.debug("incompletegamegame" + incompleteGameName);
		return incompleteGameName;
	}

	/*
	 * Date: 03/04/2017 Author: Author: Anish Description: This function used for
	 * page load Parameter: NA
	 */
	public String verifyCreditsValue() {
		String balance = null;
		try {
			String dataFromHar = clickAt.getData("Player", "balance", "", "", proxy);
			balance = dataFromHar.split(",")[0];

			Thread.sleep(2000);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return balance;

	}

	// function for check game play req
	/*
	 * Date: 03/04/2017 Author: Author: Dhairaya Gautam Description: This function
	 * used for page load Parameter: NA
	 */
	public String isGamePlay() {
		String s = null;
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='header']")));
			WebElement ele1 = webdriver.findElement(By.xpath("//*[@id='header']"));
			s = ele1.getText();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * Date: 24/05/2018 Author: Havish Jain Description: This function is used to
	 * remove hand gesture and to play in full screen. Parameter:
	 * 
	 * @return boolean
	 */

	public boolean funcFullScreen() {

		Wait = new WebDriverWait(webdriver, 10);
		boolean isOverlayRemove = false;
		try {

			// Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("clock_ID"))));

			if (true) {
				if (osPlatform.equalsIgnoreCase("Android")) {
					String context = webdriver.getContext();
					webdriver.context("NATIVE_APP");
					Dimension size1 = webdriver.manage().window().getSize();
					int startx = size1.width / 2;
					int starty = size1.height / 2;
					TouchAction action = new TouchAction(webdriver);
					action.press(PointOption.point(startx, starty)).release().perform();
					webdriver.context(context);
					//Native_ClickByXpath(xpathMap.get("ClickOnCloseBonusReminder"));
					isOverlayRemove = true;
				} else {
					// For IOS to perform scroll
					// Thread.sleep(2000);
					//System.out.println("current contex"+webdriver.getContext());				
					Dimension size = webdriver.manage().window().getSize();

					int anchor = (int) (size.width * 0.5);
					/*
					 * int startPoint = (int) (size.height * 0.5); int endPoint = (int) (size.height
					 * * 0.4); new TouchAction(webdriver).press(point(anchor,
					 * startPoint)).waitAction(waitOptions(ofMillis(1000))).moveTo(point(anchor,
					 * endPoint)).release().perform();
					 */
					// Added another swipe to avoid swipe issue on few latest OS(1.14)
					// Thread.sleep(1000);
					int startPoint = (int) (size.height * 0.75);
					int endPoint = (int) (size.height * 0.5);
					// new TouchAction(webdriver).press(point(anchor,
					// startPoint)).waitAction(waitOptions(ofMillis(1000))).press(point(anchor,
					// endPoint)).release().perform();
					new TouchAction(webdriver).press(point(anchor, startPoint)).waitAction(waitOptions(ofMillis(1000)))
							.moveTo(point(anchor, endPoint)).release().perform();
					
					isOverlayRemove = true;
				}
			}

			log.debug("tapped on full screen overlay");

		} catch (Exception e) {
			log.error(e.getStackTrace());
			log.error("Error in full screen ", e);
		}
		return isOverlayRemove;
	}

	public boolean verifyImage(String image) {
		boolean test = false;
		try {
			TestdroidImageRecognition findImage = new TestdroidImageRecognition(webdriver);

			if (findImage.findImageOnScreen(image) != null) {
				test = true;
			} else {
				test = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return test;
	}

	/*
	 * Date: 03/04/2017 Author: Author: Dhairaya Gautam Description: This function
	 * used for page load Parameter: NA
	 */
	public String taketoGame() {
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("canvasxPath"))));
		clickAt(xpathMap.get("newfeatures"));

		return null;
	}

	/**
	 * Date: 03/04/2017 Author: Robin Dulhani Description: Returns the title of the
	 * web page. Parameter: Reference of WebDriver.
	 */

	public String getPageTitle(WebDriver w) {
		String title = null;
		try {

			title = w.getTitle();
		} catch (Exception e) {
			log.debug("Error : " + e);

		}

		return title;

	}

	public boolean logintoGibraltarMarket(String username, String password) {
		String title = null;
		try {
			Thread.sleep(3000);
			webdriver.findElement(By.xpath(xpathMap.get("GameUserName"))).clear();
			Thread.sleep(500);
			webdriver.findElement(By.xpath(xpathMap.get("GameUserName"))).sendKeys(username);
			webdriver.findElement(By.xpath(xpathMap.get("GamePassword"))).clear();
			webdriver.findElement(By.xpath(xpathMap.get("GamePassword"))).sendKeys(password);
			webdriver.findElement(By.xpath(xpathMap.get("GameLogin"))).click();

			Thread.sleep(25000);
			try {
				boolean regulaoryLastLogin = webdriver.findElements(By.xpath(xpathMap.get("regulatoryLastLoginPopup")))
						.isEmpty();
				if (!regulaoryLastLogin) {
					webdriver.findElement(By.xpath(xpathMap.get("GameLogin"))).click();
					Thread.sleep(9000);

				}
			} catch (Exception e) {
				log.error("Error : " + e);
			}
			Thread.sleep(15000);
			Thread.sleep(10000);
			title = webdriver.getTitle();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/*
	 * Date: 03/04/2017 Author: Ashish Kshatriya Description: Close Bargur Menu
	 * Parameter: N/A
	 */
	public boolean closeMenu() throws InterruptedException {
		try {

			clickAt.clickAt(xpathMap.get("backFrompayTableMenu"), webdriver, "");
			Thread.sleep(500);
			clickAt.clickAt(xpathMap.get("closeHamBurgurMenu"), webdriver, "");
			Thread.sleep(500);

		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
		}
		return true;
	}

	public boolean verifyQuickSpin(String imagepath) {
		boolean newimage = false;
		try {
			newimage = verifyImage(imagepath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newimage;
	}

	/*
	 * /** Date:31/05/2018 Author:Havish Jain This method is actually not necessery
	 * in component store just declaration needed
	 * 
	 * @return true
	 */
	public boolean settingsBack() {
		return false;
	}

	/**
	 * This method is used to stop is avalable or not Author: Premlata Mishra
	 * 
	 * @return true
	 */
	public boolean verifyStop(String imagepath) throws InterruptedException {
		boolean newimage = false;
		spinclick();
		Screen screen = new Screen();

		org.sikuli.script.Pattern image = new org.sikuli.script.Pattern(imagepath);
		try {
			org.sikuli.script.Region r = screen.exists(image, 5);
			if (r != null) {
				log.debug("image found");
				newimage = true;
			} else {
				log.debug("image not found");
				newimage = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return newimage;
	}

	/**
	 * Date:31/05/2018 Author:Havish Jain This method is used to open the settings
	 * 
	 * @return true
	 * @throws InterruptedException
	 */
	public boolean settingsOpen() throws InterruptedException {

		try {
			clickAt(xpathMap.get("menubuttonPixels"));
			Thread.sleep(500);
			clickAt(xpathMap.get("settings"));
			Thread.sleep(1500);
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	/*
	 * Date: 04/05/2017 Author:Kanchan Badhiye Description: vclickonsetting use to
	 * click on setting screen Parameter: NA
	 */

	public boolean clickonsetting() {
		try {
			clickAt(xpathMap.get("menubuttonPixels"));
			Thread.sleep(500);
			clickAt(xpathMap.get("settings"));
			Thread.sleep(500);
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/*
	 * Date: 04/05/2017 Author:Kanchan Badhiye Description: verify_Win_Amount use to
	 * verify TotalWin Parameter: nodeValue, attributes
	 */
	public int verifyWinAmount(String nodeValue, String attribute1, String attribute2, String attribute3)
			throws InterruptedException {
		Wait = new WebDriverWait(webdriver, 180);
		String balance = null;
		String loyaltyBalance = null;
		String totalWin = null;
		int totalWinNew = 0;
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("canvasxPath"))));
			clickAt(xpathMap.get("spinButton"));
			Thread.sleep(15000);
			String dataFromHar = getData(nodeValue, attribute1, attribute2, attribute3);
			balance = dataFromHar.split(",")[0];
			loyaltyBalance = dataFromHar.split(",")[1];
			totalWin = dataFromHar.split(",")[2];
			totalWinNew = Integer.parseInt(totalWin);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Thread.sleep(2000);
		return totalWinNew;
	}

	/*
	 * Date: 30/03/2017 Author:Anish Gadgil Description:This function is used to
	 * logout from the game. Parameter: No parameters are required
	 */
	public String funcLogout() {
		String loginTitle = null;
		Wait = new WebDriverWait(webdriver, 120);
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("canvasxPath"))));

			clickAt(xpathMap.get("bargurMenu")); // Clicking on hamburger menu
			Thread.sleep(500);
			clickAt(xpathMap.get("gameLink")); // Clicking on Games menu
			waitForPageToBeReady();
			Thread.sleep(5000);
			Wait.until(ExpectedConditions.elementToBeClickable(By.id(xpathMap.get("accountsLobbyID"))));
			Thread.sleep(2000);
			webdriver.findElement(By.id(xpathMap.get("accountsLobbyID"))).click(); // Clickin on Accounts
			Thread.sleep(1000);
			Wait.until(ExpectedConditions.elementToBeClickable(By.id(xpathMap.get("logoutbuttonId"))));
			Thread.sleep(1000);
			webdriver.findElement(By.id("log-out")).click(); // Clicking on log out button
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("dialogTitle")));
			Thread.sleep(1000);
			loginTitle = webdriver.findElement(By.id("dialogTitle")).getText();
			// Clicking on log out button
			webdriver.findElement(By.id("dialogCloseBttn")).click(); // Clicking on log out button
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginTitle;
	}

	public String verifyCurrencySymbolAtFront() throws InterruptedException {
		String symbol;
		Thread.sleep(2000);
		JavascriptExecutor jse = webdriver;
		String currencySymbol = (String) jse.executeScript(
				"return Game.instance._componentMap.BaseScene._componentMap.infoBar._componentMap.balanceLabel.properties.value.text");
		symbol = Character.toString(currencySymbol.charAt(0));
		return symbol;

	}

	public String verifyCurrencySymbolAtBack() throws InterruptedException {
		String symbol2;
		Thread.sleep(2000);
		JavascriptExecutor jse = webdriver;
		String currencySymbol = (String) jse.executeScript(
				"return Game.instance._componentMap.BaseScene._componentMap.infoBar._componentMap.balanceLabel.properties.value.text");
		symbol2 = currencySymbol.substring(0, 2);
		return symbol2;
	}

	public String verifyNoCurrencySymbol() throws InterruptedException {
		String symbol5;
		String[] symbol4;
		Thread.sleep(2000);
		JavascriptExecutor jse = webdriver;
		String currencySymbol = (String) jse.executeScript(
				"return Game.instance._componentMap.BaseScene._componentMap.infoBar._componentMap.balanceLabel.properties.value.text");

		symbol4 = currencySymbol.split(Character.toString(currencySymbol.charAt(0)));
		symbol5 = symbol4[0];
		return symbol5;

	}

	/*
	 * Date: 10/04/2017 Author: Author: Ashish Kshatriya Description: This function
	 * is used for filter Data from the Har Parameter: NA
	 */
	public String getData(String nodeValue, String attribute1, String attribute2, String attribute3) {
		Har nhar;
		HarLog hardata;
		List<HarEntry> entries;
		Iterator<HarEntry> itr;

		nhar = proxy.getHar();
		hardata = nhar.getLog();
		entries = hardata.getEntries();
		itr = entries.iterator();
		String data = "";
		while (itr.hasNext()) {
			try {
				HarEntry entry = itr.next();
				String requestUrl = entry.getRequest().getUrl().toString();
				if (requestUrl.contains("x.x?")) {
					String reloadData = entry.getResponse().getContent().getText();
					log.debug("Response Data is==" + reloadData);
					if (reloadData.contains(nodeValue)) {
						data = parseXML(nodeValue, attribute1, attribute2, attribute3, reloadData);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return data;
	}

	/*
	 * Date: 03/04/2017 Author: Kanchan Badhiye Description: clickAt() use for
	 * getting user data and click on particular button Parameter: userwidth,height
	 * ,x,y
	 */
	public String clickAt(String userdata) {
		double userwidth;
		double userheight;
		int userElementX;
		int userElementY;
		int newY;
		int newX;
		String[] coordinates1 = userdata.split(":");
		userwidth = Integer.parseInt(coordinates1[0]);
		userheight = Integer.parseInt(coordinates1[1]);
		userElementX = Integer.parseInt(coordinates1[2]);
		userElementY = Integer.parseInt(coordinates1[3]);
		Actions act = new Actions(webdriver);
		WebElement ele1 = webdriver.findElement(By.id(xpathMap.get("canvasxPath")));
		double deviceHeight = ele1.getSize().height;
		double deviceWidth = ele1.getSize().width;
		newX = (int) ((deviceWidth / userwidth) * userElementX);
		newY = (int) ((deviceHeight / userheight) * userElementY);
		act.moveToElement(ele1, newX, newY).click().build().perform();
		return newX + "," + newY;
	}

	/*
	 * Date: 03/04/2017 Author: Anish Gadgil Description: Verifying the multiplier
	 * for Currency Parameter: nodeValue, attribute1, attribute2, attribute3
	 */
	public double currencymultiplier(String nodeValue, String attribute1, String attribute2, String attribute3)
			throws InterruptedException {
		double multiplier = 0;
		try {
			Wait = new WebDriverWait(webdriver, 300);
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inGameClock")));
			clickAt(xpathMap.get("spinButton"));
			Thread.sleep(7000);
			String dataFromHar = getData(nodeValue, attribute1, attribute2, attribute3);
			String numchips = dataFromHar.split(",")[0];
			multiplier = Double.parseDouble(numchips);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Thread.sleep(2000);
		return multiplier;
	}

	/*
	 * Date: 04/05/2017 Author: Anish Description: This function Func_Login use to
	 * perform login operation Parameter: username andpassword Commented By :Kanchan
	 */
	public String loginToBaseScene(String username, String password) {
		Wait = new WebDriverWait(webdriver, 500);
		String title = null;

		try {
			webdriver.findElement(By.xpath(xpathMap.get("userName"))).clear();
			webdriver.findElement(By.xpath(xpathMap.get("userName"))).sendKeys(username);
			webdriver.findElement(By.xpath(xpathMap.get("password"))).clear();
			webdriver.findElement(By.xpath(xpathMap.get("password"))).sendKeys(password);

			webdriver.findElement((By.xpath(xpathMap.get("Login")))).click();
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("clock"))));
			title = GameName;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return title;
	}

	/*
	 * Date: 04/04/2017 Author: Anish Gadgil Description: Function Navigate url for
	 * Language Test scripts Parameter: applicationName and urlNavigate
	 */
	public String navigateTourl(String applicationName, String urlNavigate) {
		String gameDesktopName = null;
		try {
			webdriver.navigate().to(urlNavigate);
			webdriver.findElement(By.id(xpathMap.get("LobbyNewGamesIdLAnguage"))).click();
			Thread.sleep(2000);
			gameDesktopName = webdriver.findElement(By.id(applicationName)).getText();
			webdriver.findElement(By.id(applicationName)).click();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gameDesktopName;
	}

	/*
	 * Date: 03/04/2017 Author: Kanchan Badhiye Description: verifyTime
	 * functionality for verifying time Parameter: NA
	 */
	public String verifyTime() {
		Wait = new WebDriverWait(webdriver, 300);
		String strTime = null;
		WebElement time = null;
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("canvasxPath"))));
			time = webdriver.findElement(By.xpath(xpathMap.get("GameTime")));
			strTime = time.getText();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strTime;
	}

	public String verifybacktolobby() {
		String backtolobbytitle = null;
		try {
			// click on hyperlink
			WebElement backtolobby1 = webdriver.findElement(By.xpath(xpathMap.get("backtolobby")));
			backtolobby1.click();
			Thread.sleep(2000);
			// Back To Lobby
			backtolobbytitle = webdriver.getTitle();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return backtolobbytitle;
	}

	public String logoutSpainMarket() {
		String loginTitle = null;
		try {

			Wait.until(ExpectedConditions.elementToBeClickable(By.id(xpathMap.get("accountsLobbyID"))));
			Thread.sleep(2000);
			webdriver.findElement(By.id(xpathMap.get("accountsLobbyID"))).click(); // Clickin on Accounts
			Thread.sleep(1000);
			Wait.until(ExpectedConditions.elementToBeClickable(By.id(xpathMap.get("logoutbuttonId"))));
			Thread.sleep(1000);
			webdriver.findElement(By.id(xpathMap.get("logoutbuttonId"))).click(); // Clicking on log out button
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("LoginTitleid"))));
			Thread.sleep(1000);
			loginTitle = webdriver.findElement(By.id(xpathMap.get("LoginTitleid"))).getText(); // Clicking on log out
																								// button
			webdriver.findElement(By.id(xpathMap.get("closeButtonid"))).click(); // Clicking on log out button
			Thread.sleep(1000);

		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return loginTitle;
	}

	/*
	 * Date: 03/04/2017 Author: Ashish Kshatriya Description: Login with Set game
	 * limit for Regulatory Market Spain Country Parameter: Username, Password,
	 * Application Name
	 */
	public String loginToRegulatoryMarketSpain(String username, String password) {
		Wait = new WebDriverWait(webdriver, 50);
		String title = null;
		try {
			webdriver.findElement(By.xpath(xpathMap.get("userName"))).clear();
			webdriver.findElement(By.xpath(xpathMap.get("userName"))).sendKeys(username);
			webdriver.findElement(By.xpath(xpathMap.get("password"))).clear();
			webdriver.findElement(By.xpath(xpathMap.get("password"))).sendKeys(password);
			Thread.sleep(1000);

			webdriver.findElement((By.xpath(xpathMap.get("Login")))).click();
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("spain_LobbyOK"))));
			Thread.sleep(1000);
			webdriver.findElement((By.xpath(xpathMap.get("spain_LobbyOK")))).click();

			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("spain_SlotGameOverlay"))));
			title = webdriver.getTitle();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return title;
	}

	// Creating random player in uk

	public String loginWithRandomUserUKMarket(CommonUtil gcfnlib, String password) {
		String gameTitle = null;
		try {
			DataBaseFunctions createplayer1 = new DataBaseFunctions(
					TestPropReader.getInstance().getProperty("ServerName"),
					TestPropReader.getInstance().getProperty("dataBaseName"),
					TestPropReader.getInstance().getProperty("serverIp"),
					TestPropReader.getInstance().getProperty("port"),
					TestPropReader.getInstance().getProperty("serverID"));

			userName = gcfnlib.randomStringgenerator();
			log.debug("The New username is ==" + userName);
			createplayer1.createUser(userName, "0", 0);
			String url = xpathMap.get("UK_url");
			funcNavigate(url);
			gameTitle = loginToBaseScene(userName, password);

		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return gameTitle;
	}

	/*
	 * Date: 29/05/2019 Author:Snehal Gaikwad Description: To Check the bet
	 * selection on autoplay bet setting Parameter: NA
	 */

	public boolean autoplaybetselection() {

		boolean betselection = false;
		try {
			GetConsoleText("return " + xpathMap.get("ClickAutoplayOpenMobileBtn"));
			WebElement coinsbet = webdriver.findElement(By.xpath(xpathMap.get("Coins_Bet")));
			Select coins = new Select(coinsbet);
			coins.selectByValue("6");
			webdriver.findElement(By.id(xpathMap.get("Start_Autoplay"))).click();
			Thread.sleep(2000);
			betselection = true;
		} catch (Exception e) {
			log.error("Error in Autoplay Bet Selection");
			betselection = false;

		}
		return betselection;
	}

	/*
	 * Date: 17/05/2019 Author:Snehal Gaikwad Description: To set bet to maximum
	 * Parameter: NA
	 */

	public String setMaxBet() {
		String betAmount = null;
		try {
			String bet = "return " + xpathMap.get("SetMaxBet");
			GetConsoleText(bet);

			betAmount = GetConsoleText("return " + xpathMap.get("BetSizeText"));
		} catch (Exception e) {
			log.error("Exception while setting Max value", e);
			return null;

		}

		return betAmount;
	}

	/*
	 * Date: 17/05/2019 Author:Snehal Gaikwad Description: To Check whether the Loss
	 * Limit is less than current bet (Negative scenario) Parameter: NA
	 */
	public boolean isLossLimitLowerThanCurBet() {
		boolean flag = false;
		try {
			webdriver.findElement(By.id(xpathMap.get("Cancel_Autoplay"))).click();
			Thread.sleep(2000);
			setMaxBet();
			GetConsoleText("return " + xpathMap.get("ClickAutoplayOpenMobileBtn"));
			webdriver.findElement(By.id(xpathMap.get("Start_Autoplay"))).click();
			log.debug("Loss limit is less than current bet error message display");
			flag = true;
		} catch (Exception e) {
			log.error("Loss limit is less than current bet error message not display");
			flag = false;
		}
		return flag;
	}

	public boolean getConsoleBooleanText(String text) {
		boolean consoleText = true;
		JavascriptExecutor js = webdriver;
		try {
			consoleText = (Boolean) js.executeScript(text);
		} catch (JavascriptException e) {
			//log.error("Please check hook");
			//log.error(e.getMessage());
			consoleText = false;
		} catch (Exception e) {
			// Following condition is added if it retuns 1 or 0 insted of true or false
			log.error(e.getMessage());
			consoleText = false;
		}
		return consoleText;
	}

	public void clickAtButton(String button) {
		JavascriptExecutor js = webdriver;
		js.executeScript(button);
	}

	/*
	 * Date: 29/05/2019 Author:Snehal Gaikwad Description: To Check whether the Loss
	 * Limit is reach or not Parameter: NA
	 */
	public boolean isLossLimitReached() {
		boolean flag = false;
		try {
			webdriver.findElement(By.id(xpathMap.get("Cancel_Autoplay"))).click();
			GetConsoleText(
					"return mgs.mobile.casino.slotbuilder.v1.automation.getServiceById('BetService').betModel.setBetValue(5625)");
			Thread.sleep(2000);
			GetConsoleText(
					"return mgs.mobile.casino.slotbuilder.v1.automation.getControlById('AutoplayOptionsComponent').onButtonClicked('autoplayOpenMobileButton')");
			WebElement losslimit = webdriver.findElement(By.xpath(xpathMap.get("Loss_Limit")));
			Select limits = new Select(losslimit);
			limits.selectByValue("10000");

			webdriver.findElement(By.id(xpathMap.get("Start_Autoplay"))).click();
			while (true) {
				if ((!getConsoleBooleanText(
						"return mgs.mobile.casino.slotbuilder.v1.automation.getServiceById('AutoplayService').canAutoplayContinue().autoPlayContinue"))) {
					Thread.sleep(4000);
					clickAtButton(
							"return mgs.mobile.casino.slotbuilder.v1.automation.getControlById('DialogComponent').onButtonClicked('primaryOnlyButton')");
					log.debug("Clicked on Loss Limit reached dailog  ");
					flag = true;
					break;
				} else {
					continue;
				}
			}
		} catch (Exception e) {
			log.error("Loss limit is not reached");
			flag = false;

		}
		return flag;
	}
	/*
	 * Date: 29/05/2019 Author:Snehal Gaikwad Description: To Check whether the win
	 * Limit is reach or not Parameter: NA
	 */

	public boolean isWinLimitReached() {
		boolean flag = false;
		try {
			GetConsoleText(
					"return mgs.mobile.casino.slotbuilder.v1.automation.getServiceById('BetService').betModel.setBetValue(5625)");
			Thread.sleep(2000);
			GetConsoleText(
					"return mgs.mobile.casino.slotbuilder.v1.automation.getControlById('AutoplayOptionsComponent').onButtonClicked('autoplayOpenMobileButton')");
			WebElement losslimit = webdriver.findElement(By.xpath(xpathMap.get("Loss_Limit")));
			Select limits = new Select(losslimit);
			limits.selectByValue("100000");

			WebElement winlimit = webdriver.findElement(By.xpath(xpathMap.get("Win_Limit")));
			Select winlimits = new Select(winlimit);
			winlimits.selectByValue("10000");
			webdriver.findElement(By.id(xpathMap.get("Start_Autoplay"))).click();
			while (true) {
				if (!getConsoleBooleanText(
						"return mgs.mobile.casino.slotbuilder.v1.automation.getServiceById('AutoplayService').canAutoplayContinue().autoPlayContinue")) {
					Thread.sleep(2000);
					clickAtButton(
							"return mgs.mobile.casino.slotbuilder.v1.automation.getControlById('DialogComponent').onButtonClicked('primaryOnlyButton')");
					log.debug("Clicked on Win Limit reached dailog  ");
					flag = true;
					break;
				} else {
					continue;
				}
			}
		} catch (Exception e) {
			log.error("Win limit is not reached");
			flag = false;

		}
		return flag;
	}

	/*
	 * Date: 29/05/2019 Author: Snehal Gaikwad. Description:To verify autoplay must
	 * stop when focus being removed. Parameter: NA
	 * 
	 * @return boolean
	 */
	public boolean autoplayFocusRemovedUK() {

		boolean focus = false;
		try {
			clickAtButton(
					"return mgs.mobile.casino.slotbuilder.v1.automation.getControlById('AutoplayOptionsComponent').onButtonClicked('autoplayMoreOptionsButton')");
			Thread.sleep(1000);
			webdriver.findElement(By.id(xpathMap.get("Start_Autoplay"))).click();
			webdriver.runAppInBackground(Duration.ofSeconds(30));
			focus = true;
		}

		catch (Exception e) {
			log.error("Focus not get changed");
			log.error(e.getMessage());
		}

		return focus;

	}

	/*
	 * Date: 04/05/2017 Author:Kanchan Badhiye Description: paylinesExist use to
	 * verify paylines Parameter: nodeValue, attributes
	 */
	public int paylinesExist() throws InterruptedException {
		String numline = null;
		int totalnumline = 0;
		try {
			clickAt.clickAt(xpathMap.get("spinButtonPixels"), webdriver, "");
			Thread.sleep(3000);
			String dtaFromHar = clickAt.getData("VisArea", "numPaylines", "", "", proxy);
			numline = dtaFromHar.split(",")[0];
			totalnumline = Integer.parseInt(numline);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Thread.sleep(2000);
		return totalnumline;
	}

	/*
	 * Date: 04/05/2017 Author:Kanchan Badhiye Description:
	 * validate_credits_firstSpin use to verify credit amounts afer first spin
	 * Parameter: nodeValue, attributes
	 */
	public int validate_credits_firstSpin() throws InterruptedException {
		Wait = new WebDriverWait(webdriver, 120);
		String balance = null;
		int previousBalance = 0;
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("canvasxPath"))));
			clickAt.clickAt(xpathMap.get("spinButtonPixels"), webdriver, "");
			Thread.sleep(3000);
			String DataFromHar = clickAt.getData("Player", "balance", "", "", proxy);
			balance = DataFromHar.split(",")[0];
			previousBalance = Integer.parseInt(balance);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Thread.sleep(2000);
		return previousBalance;
	}

	/*
	 * Date:19/08/17 Author: Anish Gadgil Description:This function is used to click
	 * anywhere on the screen. Parameters: No Parameters
	 */
	public boolean clickAnywhere() throws InterruptedException {

		clickAt.clickAt(xpathMap.get("menubuttonextended"), webdriver, "");
		Thread.sleep(2000);
		return true;
	}

	/**
	 * Date: 25/10/2017 Author: Kamal Kumar Vishwakarma Description: This function
	 * is used for get Data from the Common Language File provided by Derivco
	 */
	public void getCommonLanguageFile(String language) throws IOException {
		String commonLanguageContent;
		File commonLanguage = new File("languageFiles/" + language + "/language.json");
		commonLanguageContent = clickAt.removeUTF8BOM(FileUtils.readFileToString(commonLanguage, "UTF-8"));

	}

	/**
	 * @return
	 */
	public double verify_Bet_Amount() {
		int betAmount = 0;
		try {
			/*
			 * String betValue=func_GetText(XpathMap.get("betValue")); String
			 * BetValue=func_String_Operation(betValue);
			 */
			// betAmount=Integer.parseInt(BetValue);
			System.out.println(betAmount);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return betAmount;
	}

	/*
	 * Date: 04/05/2017 Author:Kanchan Badhiye Description:
	 * validate_credits_firstSpin use to verify credit amounts afer next spin
	 * Parameter: nodeValue, attributes
	 */
	public int validate_credits_nextSpin() throws InterruptedException {
		Wait = new WebDriverWait(webdriver, 120);
		String balance = null;
		int newBalance = 0;
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("canvasxPath"))));
			clickAt.clickAt(xpathMap.get("spinButtonPixels"), webdriver, "");
			Thread.sleep(5000);
			String DataFromHar = clickAt.getData("Player", "balance", "", "", proxy);
			balance = DataFromHar.split(",")[0];
			newBalance = Integer.parseInt(balance);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Thread.sleep(2000);
		return newBalance;
	}

	/*
	 * Date: 04/05/2017 Author:Kanchan Badhiye Description: verifycredit use to
	 * verify credit amounts Parameter: nodeValue, attributes
	 */
	public String verifyCredit() {
		Wait = new WebDriverWait(webdriver, 120);
		String balance = null;
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("canvasxPath"))));
			clickAt.clickAt(xpathMap.get("spinButtonPixels"), webdriver, "");
			Thread.sleep(3000);
			String DataFromHar = clickAt.getData("Player", "balance", "", "", proxy);
			balance = DataFromHar.split(",")[0];
			// initialbalance1 = balance);
			Thread.sleep(2000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return balance;
	}

	/*
	 * Date: 04/05/2017 Author:Kanchan Badhiye Description: verifySpinBeforeClick()
	 * use to verify balance amount Parameter: nodeValue, attributes
	 */
	public String verifySpinBeforeClick(String nodeValue, String attribute1, String attribute2, String attribute3)
			throws InterruptedException {
		Wait = new WebDriverWait(webdriver, 120);
		String balance = null;
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("canvasxPath"))));
			Thread.sleep(10000);
			String outputData = getData(nodeValue, attribute1, attribute2, attribute3);
			System.out.println("The outputData is " + outputData);
			System.out.println(outputData.split(",")[0]);
			balance = outputData.split(",")[0];
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return balance;
	}

	/*
	 * Date: 06/04/2017 Author: Ashish Kshatriya Description:Overlay popup for Set
	 * Session Limit for regulatory market Spain Parameter: N/A
	 */
	public boolean overLay() throws InterruptedException {
		try {
			boolean SetSessionLimits = webdriver.findElements(By.xpath(xpathMap.get("spain_SlotGameOverlay")))
					.size() > 0;
			if (SetSessionLimits) {

				System.out.println("\n Slot game overlay is present ");
				log.debug("\n Slot game overlay is present ");
				return true;
			}

			else {
				System.out.println("\n Slot game overlay is not present ");
				log.debug("\n Slot game overlay is not present ");
				return false;
			}
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/*
	 * Date: 06/04/2017 Author: Ashish Kshatriya Description:Verify
	 * SlotGameSession,TotalWagered,TotalPayouts & TotalBalance for regulatory
	 * market Spain Parameter: N/A
	 */
	public String[] lossLimit() throws InterruptedException {
		String ar[] = new String[4];
		try {
			boolean SlotGameSession = webdriver.findElements(By.xpath(xpathMap.get("SlotGamesession1"))).size() > 0;
			try {
				if (SlotGameSession)
					;
				String slotGame = webdriver.findElement(By.xpath(xpathMap.get("SlotGamesession1"))).getText().toString()
						.trim();
				ar[0] = slotGame;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			boolean TotalWagered = webdriver.findElements(By.xpath(xpathMap.get("TotalWagered"))).size() > 0;
			try {
				if (TotalWagered)
					;
				String totalWagered = webdriver.findElement(By.xpath(xpathMap.get("TotalWagered"))).getText().toString()
						.trim();
				ar[1] = totalWagered;
			} catch (NoSuchElementException e) {
				e.printStackTrace();
			}
			boolean TotalPayouts = webdriver.findElements(By.xpath(xpathMap.get("TotalPayouts"))).size() > 0;
			try {
				if (TotalPayouts)
					;
				String totalPayout = webdriver.findElement(By.xpath(xpathMap.get("TotalPayouts"))).getText().toString()
						.trim();
				ar[2] = totalPayout;
			} catch (NoSuchElementException e) {
				e.printStackTrace();
			}
			boolean TotalBalance = webdriver.findElements(By.xpath(xpathMap.get("TotalBalance"))).size() > 0;
			try {
				if (TotalBalance)
					;
				String totalBalance = webdriver.findElement(By.xpath(xpathMap.get("TotalBalance"))).getText().toString()
						.trim();
				ar[3] = totalBalance;
			} catch (NoSuchElementException e) {
				e.printStackTrace();
			}
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}
		return ar;
	}

	/*
	 * Date: 03/04/2017 Author: Ashish Kshatriya Description: Set Session Limit for
	 * regulatory market Spain Parameter: LossLimit
	 */
	public String setLimit(String losslimit) throws InterruptedException {
		Wait = new WebDriverWait(webdriver, 300);
		try {
			boolean SetSessionLimits = webdriver.findElements(By.id(xpathMap.get("SetSessionLimitsForm"))).size() > 0;
			if (SetSessionLimits) {
				WebElement setTimeLimit = webdriver.findElement(By.id(xpathMap.get("SelectTimeLimit")));
				selectValue(setTimeLimit, xpathMap.get("TimeLimitdata"));
				Thread.sleep(1000);
				WebElement setReminderPeriod = webdriver.findElement(By.id(xpathMap.get("SelectReminderPeriod")));
				selectValue(setReminderPeriod, xpathMap.get("ReminderPeriod"));
				Thread.sleep(1000);
				WebElement setLossLimit = webdriver.findElement(By.id(xpathMap.get("SelectLossLimit")));
				// setLossLimit.sendKeys(XpathMap.get("LossLimit"));
				setLossLimit.sendKeys(losslimit);
				Thread.sleep(1000);
				/*
				 * WebElement setPreventFutureSlotGame=webdriver.findElement(By.id(XpathMap.get(
				 * "SelectFutureSlotGame"))); selectValue(setPreventFutureSlotGame,
				 * XpathMap.get("FutureSlotGame"));
				 */
				webdriver.findElement(By.id(xpathMap.get("SetLimitButton"))).click();
				waitForPageToBeReady();
				Thread.sleep(500);
				boolean confirmgame = webdriver.findElements(By.id(xpathMap.get("ConfirmLaunchGame"))).size() > 0;
				if (confirmgame) {
					webdriver.findElement(By.id(xpathMap.get("ConfirmLaunchGame"))).click();
					waitForPageToBeReady();
				} else {
				}
				WebElement ele1 = webdriver.findElement(By.id(xpathMap.get("canvasxPath")));
				ele1 = Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("canvasxPath"))));
			}
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}
		String Title = webdriver.getTitle();
		return Title;
	}

	/*
	 * Date: 03/04/2017 Author: Ashish Kshatriya Description: Select value from the
	 * dropdown Parameter: UserName, Password, Application Name
	 */
	public void selectValue(WebElement ele, String selectData) throws InterruptedException {
		try {
			Select select = new Select(ele);
			select.selectByValue(selectData);
			Thread.sleep(500);
		} catch (ElementNotVisibleException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Date: 06/04/2017 Author: Author: Ashish Kshatriya Description: This function
	 * is used for take screenshots in application. Parameter: WebDriver driver
	 */
	public static String createScreenshot(WebDriver webdriver) throws InterruptedException {
		UUID uuid = UUID.randomUUID();
		String screenshotsFolder = "";
		/*
		 * screenshotsFolder = "ImageScreenshot//Mobile//"; File dir = new
		 * File(screenshotsFolder); dir.mkdirs();
		 */
		String imageLocation = "D:\\Mobile_Image_Parallel\\Parallel\\ImageScreenshot\\Mobile";
		File scrFile = ((TakesScreenshot) webdriver).getScreenshotAs(OutputType.FILE);
		Thread.sleep(1000);
		try {
			FileUtils.copyFile(scrFile, new File(imageLocation + uuid + ".png"));
		} catch (IOException e) {
			System.out.println("Error while generating screenshot:\n" + e.toString());
		}
		return imageLocation + uuid + ".png";
	}

	/*
	 * Date: 10/04/2017 Author: Author: Ashish Kshatriya Description: This function
	 * is used for to Verify error has occurred on not in Game Parameter: NA
	 */
	public String errorMessage() throws InterruptedException {
		String getMessage = null;
		try {
			if (webdriver.findElement(By.id(xpathMap.get("errorNotificationPopup"))).isDisplayed()) {
				getMessage = webdriver.findElement(By.id(xpathMap.get("errorNotificationPopupBody"))).getText();
			} else {
			}
		} catch (NoSuchElementException e) {
			// getTest().log(LogStatus.ERROR, "Error has been found on
			// screen"+getTest().addScreenCapture(createScreenshot(webdriver)));
		}
		return getMessage;
	}

	/*
	 * Date: 10/04/2017 Author: Author: Ashish Kshatriya Description: This function
	 * is used for close save password popup in chrome browser Parameter: NA
	 */
	public WebElement closeSavePasswordPopup() {
		WebElement closepopup = null;
		try {
			webdriver.context("NATIVE_APP");
			// Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("closeSavePassword"))));
			boolean pop = webdriver.findElements(By.xpath(xpathMap.get("closeSavePassword"))).size() > 0;
			if (pop) {
				closepopup = webdriver.findElement(By.xpath(xpathMap.get("closeSavePassword")));
				closepopup.click();
				webdriver.context("CHROMIUM");
			} else {
				webdriver.context("CHROMIUM");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return closepopup;
	}

	public void waitForPageToBeReady() {
		JavascriptExecutor js = (JavascriptExecutor) webdriver;
		for (int i = 0; i < 800; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			if (js.executeScript("return document.readyState").toString().equals("complete")) {
				break;
			}
		}
	}

	/*************
	 * code for spain
	 * 
	 * @throws Exception
	 ***********************/
	/* code for verifying slottitle */
	public String verifyslottitle() throws Exception {
		String slotgametitle1 = null;
		WebElement slotgametitle;
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("DefaultOverlayContent"))));

			String innerText = webdriver.findElement(By.xpath(xpathMap.get("DefaultOverlayContent"))).getText();

			if (innerText.contains("Error")) {
				slotgametitle1 = "Error";

			} else {

				slotgametitle = webdriver.findElement(By.xpath(xpathMap.get("SlotGameLimit")));
				slotgametitle1 = slotgametitle.getText();
			}
		} catch (Exception e) {

		}
		return slotgametitle1;
	}

	public String ReminderPeriod() throws Exception {
		String ReminderPeriod1 = null;
		WebElement ReminderTime;
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("DefaultOverlayContent"))));

			String innerText = webdriver.findElement(By.xpath(xpathMap.get("DefaultOverlayContent"))).getText();

			if (innerText.contains("Error")) {
				ReminderPeriod1 = "Error";

			} else {
				ReminderTime = webdriver.findElement(By.xpath(xpathMap.get("ReminderPeriod")));
				ReminderPeriod1 = ReminderTime.getText();
			}
		} catch (Exception e) {

		}
		return ReminderPeriod1;
	}

	public String PreventFuture() throws Exception {
		Exception ex = null;
		String PreventFuture1 = null;
		WebElement futurePrevent;
		try {

			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("DefaultOverlayContent"))));

			String innerText = webdriver.findElement(By.xpath(xpathMap.get("DefaultOverlayContent"))).getText();

			if (innerText.contains("Error")) {
				PreventFuture1 = "Error";

			} else {
				futurePrevent = webdriver.findElement(By.xpath(xpathMap.get("PreventFuture")));

				PreventFuture1 = futurePrevent.getText();
			}
		} catch (Exception e) {

		}
		return PreventFuture1;
	}

	public String verifysetlimitbutton() throws Exception {
		String setlimit1 = null;
		try {

			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("DefaultOverlayContent"))));

			String innerText = webdriver.findElement(By.xpath(xpathMap.get("DefaultOverlayContent"))).getText();

			if (innerText.contains("Error")) {
				setlimit1 = "Error";

			} else {
				WebElement setlimit = webdriver.findElement(By.xpath(xpathMap.get("setlimitbutton")));
				setlimit1 = setlimit.getText();
			}
		} catch (Exception e) {

		}
		return setlimit1;
	}

	public boolean verifyhyperlink() {
		boolean ret = false;
		try {
			// Thread.sleep(30000);
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("DefaultOverlayContent"))));
			String innerText = webdriver.findElement(By.xpath(xpathMap.get("DefaultOverlayContent"))).getText();
			System.out.println(innerText);

			if (innerText.contains("Error")) {
				System.out.println("Game Play Error");
				ret = false;

			} else if (webdriver.findElement(By.xpath(xpathMap.get("backtolobby"))).isDisplayed()) {

				System.out.println("backtolobby is enabled");
				String dialogActionText = webdriver.findElement(By.xpath(xpathMap.get("dialogAction"))).getText();
				System.out.println(dialogActionText);

				if (dialogActionText.contains("START NEW SESSION")) {
					webdriver.findElement(By.xpath(xpathMap.get("startNewSession"))).click();
				}

				ret = true;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ret;
	}

	/**
	 * Date:10-1-2018 Name:Havish Jain Description: this function is clicking on
	 * anyelement it will take screen shot
	 * 
	 * @throws Exception
	 */
	public void GeneralError(Mobile_HTML_Report report) {
		String generalError = null;
		try {
			if (webdriver.findElements(By.xpath(xpathMap.get("General_Error_ID"))).size() > 0)
				;
			{
				generalError = webdriver.findElement(By.xpath(xpathMap.get("General_Error_ID"))).getText();
				webdriver.navigate().to(xpathMap.get("Italy_url"));
			}
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public String Func_navigate_again(String appName) {
		String GameDesktopName = null;
		try {
			Wait = new WebDriverWait(webdriver, 300);
			Wait.until(ExpectedConditions.elementToBeClickable(By.id(xpathMap.get("LobbyfiveReels"))));
			boolean newGame = webdriver.findElements(By.id(xpathMap.get("LobbyNewGamesId"))).size() > 0;
			if (newGame) {
				webdriver.findElement(By.id(xpathMap.get("LobbyNewGamesId"))).click();
				waitForPageToBeReady();
				Thread.sleep(1000);
				boolean game = webdriver.findElements(By.id(appName)).size() > 0;
				if (game) {
					webdriver.findElement(By.id(appName)).click();
				} else {
					System.out.println(appName + " not available in the lobby");
				}

			} else {
				webdriver.findElement(By.id(xpathMap.get("LobbyfiveReels"))).click();
				waitForPageToBeReady();
				Thread.sleep(1000);
				boolean game = webdriver.findElements(By.id(appName)).size() > 0;
				if (game) {
					webdriver.findElement(By.id(appName)).click();
				} else {
					System.out.println(appName + " not available in the lobby");
				}
			}
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("loginPopUp"))));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return GameDesktopName;
	}

	// ********************************Exception
	// Handler********************************************************//
	public void evalException(Exception ex) throws Exception {
		// System.out.println("*************In Exception Handling
		// Class*************"+ex.getClass());
		// ex.printStackTrace();
		// repo1.details_append( "Execution Interrupted because of exception" , "" , "",
		// "Interrupted");
		System.out.println("value of ex is " + ex);
		String exClass = ex.getClass().toString();
		// ex.printStackTrace();
		if (exClass.contains("StaleElementReferenceException")) {
			// System.out.println("Identified specific exception "+exClass);
			// System.out.println();
			repo1.detailsAppend("Execution Interrupted because of StaleElementReferenceException", "", "",
					"Interrupted");
			webdriver.close();
		} else if (exClass.contains("NoSuchElementException")) {
			String gameplay = funGamePlayError();

			if (gameplay != null) {
				repo1.detailsAppend(gameplay + " error occurred", "", "", "Interrupted");
				// System.out.println("Identified specific exception "+exClass);
			} else {
				repo1.detailsAppend("NoSuchElementException because of Element not found", "", "", "Interrupted");
			}
			webdriver.close();
		} else if (exClass.contains("InvalidElementStateException")) {
			repo1.detailsAppend("Execution Interrupted because of InvalidElementStateException", "", "", "Interrupted");
			// System.out.println("Identified specific exception "+exClass);
			webdriver.close();
		} else if (exClass.contains("ElementNotVisibleException")) {
			repo1.detailsAppend("Execution Interrupted because of ElementNotVisibleException", "", "", "Interrupted");
			// System.out.println("Identified specific exception "+exClass);
			webdriver.close();
		} else if (exClass.contains("ErrorInResponseException")) {
			repo1.detailsAppend("Execution Interrupted because of ErrorInResponseException", "", "", "Interrupted");
			// System.out.println("Identified specific exception "+exClass);
		} else if (exClass.contains("InvalidSwitchToTargetException")) {
			repo1.detailsAppend("Execution Interrupted because of InvalidSwitchToTargetException", "", "",
					"Interrupted");
			// System.out.println("Identified specific exception "+exClass);
		} else if (exClass.contains("NoSuchFrameException")) {
			repo1.detailsAppend("Execution Interrupted because of NoSuchFrameException", "", "", "Interrupted");
			// System.out.println("Identified specific exception "+exClass);
		} else if (exClass.contains("NoSuchWindowException")) {
			repo1.detailsAppend("Execution Interrupted because of NoSuchWindowException", "", "", "Interrupted");
			// System.out.println("Identified specific exception "+exClass);
		} else if (exClass.contains("NoSuchAttributeException")) {
			repo1.detailsAppend("Execution Interrupted because of NoSuchAttributeException", "", "", "Interrupted");
			// System.out.println("Identified specific exception "+exClass);
		} else if (exClass.contains("NoAlertPresentException")) {
			repo1.detailsAppend("Execution Interrupted because of NoAlertPresentException", "", "", "Interrupted");
			// System.out.println("Identified specific exception "+exClass);
		} else if (exClass.contains("ElementNotSelectableException")) {
			repo1.detailsAppend("Execution Interrupted because of ElementNotSelectableException", "", "",
					"Interrupted");
			// System.out.println("Identified specific exception "+exClass);
		} else if (exClass.contains("InvalidCookieDomainException")) {
			repo1.detailsAppend("Execution Interrupted because of InvalidCookieDomainException", "", "", "Interrupted");
			// System.out.println("Identified specific exception "+exClass);
		} else if (exClass.contains("UnableToSetCookieException")) {
			repo1.detailsAppend("Execution Interrupted because of UnableToSetCookieException", "", "", "Interrupted");
			// System.out.println("Identified specific exception "+exClass);
		} else if (exClass.contains("RemoteDriverServerException")) {
			repo1.detailsAppend("Execution Interrupted because of RemoteDriverServerException", "", "", "Interrupted");
			// System.out.println("Identified specific exception "+exClass);
		} else if (exClass.contains("TimeoutException")) {

			repo1.detailsAppend("Execution Interrupted because of TimeoutException", "", "", "Interrupted");
			// System.out.println("Identified specific exception "+exClass);
		} else if (exClass.contains("MoveTargetOutOfBoundsException")) {
			repo1.detailsAppend("Execution Interrupted because of MoveTargetOutOfBoundsException", "", "",
					"Interrupted");
			// System.out.println("Identified specific exception "+exClass);
		} else if (exClass.contains("UnexpectedTagNameException")) {
			repo1.detailsAppend("Execution Interrupted because of UnexpectedTagNameException", "", "", "Interrupted");
			// System.out.println("Identified specific exception "+exClass);
		} else if (exClass.contains("InvalidSelectorException")) {
			repo1.detailsAppend("Execution Interrupted because of InvalidSelectorException", "", "", "Interrupted");
			// System.out.println("Identified specific exception "+exClass);
		} else if (exClass.contains("ImeNotAvailableException")) {
			repo1.detailsAppend("Execution Interrupted because of ImeNotAvailableException", "", "", "Interrupted");
			// System.out.println("Identified specific exception "+exClass);
		} else if (exClass.contains("ImeActivationFailedException")) {
			repo1.detailsAppend("Execution Interrupted because of ImeActivationFailedException", "", "", "Interrupted");
			// System.out.println("Identified specific exception "+exClass);
		} else if (exClass.contains("UnhandledAlertException")) {
			repo1.detailsAppend("Execution Interrupted because of Unhandled message ALERT", "", "", "Interrupted");
			Thread.sleep(3000);
			// System.out.println("Identified specific exception "+exClass);

			// Alert alert = null;
			// try {
			// //System.out.println("Alert found"+((TargetLocator)
			// driver).alert().getText());
			// alert = driver.switchTo().alert();
			// } catch (Exception e) {
			// e.printStackTrace();
			// System.out.println("Alert found"+alert.getText());
			//
			//
			// }
			// // Get the text of the alert or prompt
			//
			// //System.out.println("Alert found");
			//
			//
			// // And acknowledge the alert (equivalent to clicking "OK")
			// alert.accept();
		} else if (exClass.contains("NullPointerException")) {

			String gameplay = funGamePlayError();

			if (gameplay != null) {
				repo1.detailsAppend(gameplay + " error occurred", "", "", "Interrupted");
				// System.out.println("Identified specific exception "+exClass);
			} else {
				repo1.detailsAppend("NoSuchElementException because of Element not found", "", "", "Interrupted");
			}

		}
	}

	/*
	 * Date: 29/05/2018 Author:Havish Jain Description: To open menu container
	 * Parameter: NA
	 */
	public boolean menuOpen() {
		return false;

	}

	/*
	 * Date: 29/05/2018 Author:Havish Jain Description: To Close menu container
	 * Parameter: NA
	 */
	public boolean menuClose() {
		return false;

	}

	/**
	 * Date:25-7-2018 Name:Premlata Mishra Description: This method is to verify
	 * error is coming or not
	 * 
	 * @throws Exception
	 */
	public boolean func_SwipeRight() {
		return false;
	}

	/**
	 * Date: 07-01-2018 Author:Laxmikanth Kodam Description: verify New Feature
	 * Dialogue is appearing on the screen
	 * 
	 * @return
	 * @throws InterruptedException
	 */

	public void newFeature() {

		try {
			webdriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			boolean test = webdriver.findElements(By.id("countinue_txt")).size() > 0;

			if (test) {
				nativeClickByID(xpathMap.get("M_NewFeatureScreen"));
				// func_Click(XpathMap.get("OneDesign_NewFeature_ClickToContinue"));
			}

			else {

				System.out.println("Continue Button  is not displaying");
			}
		} catch (Exception e) {

			e.getMessage();
		}
	}

	/* Click on quick spin toggle button */
	public boolean clickOnQuickSpin() {
		Wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathMap.get("OneDesignQuickSpin"))));
		// func_Click(XpathMap.get("OneDesignQuickSpin"));

		return true;
	}

	public boolean betDecrease() {

		return true;
	}

	public String FS_summaryScreenClick() {

		return null;
	}

	/**
	 * *Author:Premlata This method is used to wait till the free spin summary
	 * screen won't come
	 * 
	 * @throws InterruptedException
	 */

	public boolean isElementPresent(String locator) {
		boolean isPresent = false;
		try {
			webdriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			if (webdriver.findElements(By.xpath(locator)).size() > 0) {
				log.debug("Error is coming while refreshing the game");
				isPresent = true;
			}
			webdriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		} catch (Exception e) {
			log.error("error in isElementPresent", e);
		}
		return isPresent;
	}

	public void capturePaytableScreenshot(Mobile_HTML_Report report, String language) throws Exception {

	}

	public boolean paytableOpen(Mobile_HTML_Report report, String CurrencyName) throws Exception {
		return true;

	}

	public void winHistoryClose() throws Exception {

	}

	/**
	 * *Author:Premlata This method is used to click on win history button
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public boolean winHistoryClick() throws Exception {
		return false;
	}

	public boolean FSSceneLoading() {
		return true;
	}

	public boolean waitSummaryScreen() {
		return true;
	}

	/**
	 * This method is used to logout ,if any error occure while launching the game
	 * Author: Premlata Mishra
	 * 
	 * @return true
	 */
	public void errorLogout() {
		try {
			webdriver.findElement(By.xpath(xpathMap.get("spain_LobbyOK"))).click();
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("five_Reel_slot"))));
			funcClick(xpathMap.get("navigation_MyAccount"));
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("logout"))));
			funcClick(xpathMap.get("logout"));
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("Login"))));
			funcClick(xpathMap.get("closeButtonLogin"));
		} catch (Exception e) {
			log.error("error in logout", e);
		}
	}

	/**
	 * This method is used to login with random string generator Author: Havish Jain
	 * 
	 * @return true
	 */
	public long Random_LoginBaseScene(String userName) throws Exception {
		long loadingTime = 0;

		try {
			webdriver.findElement(By.xpath(xpathMap.get("userName"))).clear();
			String password = xpathMap.get("Password");
			webdriver.findElement(By.xpath(xpathMap.get("userName"))).sendKeys(userName);
			webdriver.findElement(By.xpath(xpathMap.get("password"))).clear();
			webdriver.findElement(By.xpath(xpathMap.get("password"))).sendKeys(password);
			// Thread.sleep(2000);
			webdriver.findElement((By.xpath(xpathMap.get("Login")))).click();
			log.debug("Clicked on login button after entering usename and password");
			long start = System.currentTimeMillis();
			new WebDriverWait(webdriver, 500).until(ExpectedConditions.or(
					ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("clock_ID"))),
					ExpectedConditions.visibilityOfElementLocated(
							By.xpath(xpathMap.get("OneDesign_NewFeature_ClickToContinue")))));

			long finish = System.currentTimeMillis();
			long totalTime = finish - start;
			loadingTime = totalTime / 1000;
			log.debug("Total Time for Game load in Seconds is: " + loadingTime);
			// newFeature(); //Full Screen overlay will appear first in mobile
			// func_FullScreen();

		} catch (NoSuchElementException e) {
			e.printStackTrace();
			log.debug("error while login");
			throw new Exception(e);
		}
		return loadingTime;
	}

	public String entryScreen_Wait(String clickToContinue) {
		String wait = "";
		return wait;
	}

	public String clickToContinue() {
		String FreeSpin = "";
		return FreeSpin;

	}

	public void FS_continue() {

	}

	public String waitUntilSessionLoss() {
		return null;
	}

	public String waitUntilSessionReminder(Mobile_HTML_Report tc10) {
		Wait = new WebDriverWait(webdriver, 300);

		String header = null;
		try {

			boolean stopbutton = isStopButtonAvailable();
			if (!stopbutton) {
				tc10.detailsAppend("Verify the Stop button is not available in Spain Regulatory Markets.",
						"Stop Button should not display.", "Stop Button is not gettting displayed in game", "Pass");
				System.out.println("Waiting for SessionReminder Continue overlay  and checking for stop button");
				log.debug("Waiting for SessionReminder Continue overlay  and checking for stop button");
			} else {
				System.out.println("Waiting for SessionReminder Continue overlay  and checking for stop button");
				log.debug("Waiting for SessionReminder Continue overlay  and checking for stop button");
				tc10.detailsAppend("Verify the Stop button is not available in Spain Regulatory Markets.",
						"Stop Button should not display.", " Stop Button is gettting displayed in game", "Fail");

			}

			Wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath(xpathMap.get("spain_SessionReminderContinue"))));
			Thread.sleep(1000);
			header = webdriver.findElement(By.xpath(xpathMap.get("spain_SessionReminderContinue"))).getText();
		} catch (Exception e) {

			e.printStackTrace();
		}
		return header;
	}

	/*
	 * Close Session loss popup for spain
	 */

	public void closeSessionLossPopup(Mobile_HTML_Report report) {
		try {
			report.detailsAppend("  Loss Limit Summary overlay ", "check if Loss Limit Summary overlay appear",
					"check if Loss Limit Summary overlay appear", "PASS");
			funcClick(xpathMap.get("spain_lossLimitDialogueOK"));
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("spain_CloseBtn"))));
			report.detailsAppend("Close ", "Loss Limit Summary overlay Close clicked ",
					"Loss Limit Summary overlay Close clicked", "PASS");
			funcClick(xpathMap.get("spain_CloseBtn"));
			// Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("five_Reel_slotID"))));
			report.detailsAppend("Close naviagtion ",
					"User should redirect to lobby after clicking on close button when loss limit is reached",
					"User is redirected to lobby", "Pass");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean FSSceneLoading(int bonusSelection) {
		return false;
	}

	public void acceptAlert() {

	}

	public String clickBonusSelection(int i) {
		return null;
	}

	public void coolingOffPeriod(Mobile_HTML_Report tc10) {
		try {
			nativeClickByID(xpathMap.get("five_Reel_slotID"));

			JavascriptExecutor js = ((JavascriptExecutor) webdriver);
			js.executeScript("arguments[0].scrollIntoView(true);", webdriver.findElement(By.id(GameName)));

			js.executeScript("arguments[0].click();", webdriver.findElement(By.id(GameName)));

			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("spain_CloseBtn"))));
			tc10.detailsAppend("To check if Cooling Off Period Overlay appear on launching same game",
					"Cooling Off Period Overlay should appear on launching same game",
					"Cooling Off Period Overlay appears on launching same game", "Pass");
			funcClick(xpathMap.get("spain_CloseBtn"));
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("five_Reel_slotID"))));
			tc10.detailsAppend(
					"To verify user navigates to lobby after clicking on close button when cooling off period is running",
					"User should redirect to lobby after clicking on close button when cooling off period is running",
					"User is redirected to lobby", "Pass");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void spainContinueSession() {
		// Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("spain_SessionReminderContinue"))));
		webdriver.findElement(By.xpath(xpathMap.get("spain_SessionReminderContinue"))).click();
		System.out.println("Clicked on Continue  ");
		log.debug("Clicked on Continue ");
	}

	public void coolingOffPeriodNewGame(String gamename, Mobile_HTML_Report tc10) {
		try {
			nativeClickByID(xpathMap.get("five_Reel_slotID"));

			JavascriptExecutor js = ((JavascriptExecutor) webdriver);
			js.executeScript("arguments[0].scrollIntoView(true);", webdriver.findElement(By.id(gamename)));
			js.executeScript("arguments[0].click();", webdriver.findElement(By.id(gamename)));

			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("spain_CloseBtn"))));
			tc10.detailsAppend("To check if Cooling Off Period Overlay appear on launching another game",
					"Cooling Off Period Overlay should appear on launching another game",
					"Cooling Off Period Overlay appears on launching another game", "Pass");
			funcClick(xpathMap.get("spain_CloseBtn"));
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("five_Reel_slotID"))));
			tc10.detailsAppend(
					"To verify user navigates to lobby after clicking on close button when cooling off period is running",
					"User should redirect to lobby after clicking on close button when cooling off period is running",
					"User is redirected to lobby", "Pass");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void waitUntilTimeLimitSession(Mobile_HTML_Report tc10) {
		try {
			for (int i = 0; i <= 5; i++) {
				spainContinueSession();
				boolean b = webdriver.findElements(By.xpath(xpathMap.get("spain_CloseBtn"))).size() > 0;
				if (b) {
					break;
				}
			}
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("spain_CloseBtn"))));
			tc10.detailsAppend("To check if Time Limit Summary overlay appear when Time limit is reached",
					"Time Limit Summary overlay should appear", "Time Limit summary overlay appears", "Pass");
			funcClick(xpathMap.get("spain_CloseBtn"));
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("five_Reel_slotID"))));
			tc10.detailsAppend(
					"To verify user navigates to lobby after clicking on close button when Time limit is reached",
					"User should redirect to lobby after clicking on close button when Time limit is reached",
					"User is redirected to lobby", "Pass");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void relaunchGame() throws InterruptedException {
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("five_Reel_slotID"))));
		nativeClickByID(xpathMap.get("five_Reel_slotID"));

		JavascriptExecutor js = ((JavascriptExecutor) webdriver);
		js.executeScript("arguments[0].scrollIntoView(true);", webdriver.findElement(By.id(GameName)));

		js.executeScript("arguments[0].click();", webdriver.findElement(By.id(GameName)));

		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("spain_SlotGameOverlay"))));
	}

	public boolean verifyQuickSpin() {
		return false;
	}

	public String getCurrencySymbol() {
		return null;
	}

	public String getCurrentBet() {
		return null;
	}

	public boolean betCurrencySymbol(String currencySymbol) {
		return false;
	}

	public boolean betSettingCurrencySymbol(String currencySymbol, Mobile_HTML_Report report, String currencyName)
			throws Exception {
		return false;
	}

	public boolean waitforWinAmount(String currencyFormat, Mobile_HTML_Report currencyReport, String currencyName) {
		return false;
	}

	public String Func_logout_OD() throws Exception {
		String loginTitle = null;
		try {
			nativeClickByID(xpathMap.get("M_OneDesign_HomeIconID"));

			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("five_Reel_slot"))));

			loginTitle = func_GetText(xpathMap.get("five_Reel_slot"));

			if (webdriver.getPageSource().contains("Arthur")) {
				Native_Click(xpathMap.get("Profile"));
				// func_Click("//*[@id='myaccount']");
				log.debug("Clicked on profile");

			} else {
				nativeClickByID(xpathMap.get("M_navigation_MyAccountID"));
				log.debug("Clicked on My Account");

			}
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("logout"))));
			loginTitle = func_GetText(xpathMap.get("logout"));
			// func_Click(XpathMap.get("logout"));
			JavascriptExecutor js = ((JavascriptExecutor) webdriver);
			js.executeScript("arguments[0].scrollIntoView(true);",
					webdriver.findElement(By.id(xpathMap.get("logout_ID"))));

			js.executeScript("arguments[0].click();", webdriver.findElement(By.id(xpathMap.get("logout_ID"))));
			// Native_ClickByID(XpathMap.get("logout_ID"));
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("Login"))));
			// func_Click(XpathMap.get("closeButtonLogin"));
			nativeClickByID(xpathMap.get("closeButtonLoginID"));
		} catch (WebDriverException e) {
			e.getMessage();
			throw new Exception(e);
		}
		return loginTitle;
	}

	public boolean open_Bet() {
		return false;
	}

	public void verifyStopLanguage(Mobile_HTML_Report tc01, String language) {

	}

	public void FS_Start() {

	}

	public void waitForSpinButton() {

	}

	public void jackpotSummary(Mobile_HTML_Report report, String language) {

	}

	public String func_String_Operation(String value) {
		String str = value;
		String str1 = str.substring(1);
		return str1;
	}

	public boolean func_SwipeDown() {
		try {
			webdriver.context("NATIVE_APP");
			Dimension size1 = webdriver.manage().window().getSize();
			int startx = size1.width / 2;
			String startyString = xpathMap.get("startY");
			String endyString = xpathMap.get("endY");
			double startyDouble = Double.parseDouble(startyString);
			double endyDouble = Double.parseDouble(endyString);
			int starty = (int) (size1.height * startyDouble);
			int endy = (int) (size1.height * endyDouble);
			TouchAction action = new TouchAction(webdriver);
			action.press(PointOption.point(startx, starty)).moveTo(PointOption.point(startx, endy)).release().perform();
			// action.press(startx, starty).moveTo(startx, endy).release().perform();
			webdriver.context("CHROMIUM");
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			return false;
		}
		return true;
	}

	/*
	 * public void assignFreeGames(Mobile_HTML_Report tc01, String userName) { try{
	 * webdriver.findElement(By.xpath(xpathMap.get("FreeGamesUsername"))).clear();
	 * webdriver.findElement(By.xpath(xpathMap.get("FreeGamesUsername"))).sendKeys(
	 * username); Thread.sleep(3000);
	 * webdriver.findElement(By.xpath("//*[@class='ui-menu-item-wrapper']")).click()
	 * ; webdriver.findElement(By.xpath(xpathMap.get("FreeGamesMultipleCheckBox"))).
	 * click(); Thread.sleep(1000);
	 * webdriver.findElement(By.xpath(xpathMap.get("FreeGamesMultipleCount"))).clear
	 * (); webdriver.findElement(By.xpath(xpathMap.get("FreeGamesMultipleCount"))).
	 * sendKeys("20");
	 * webdriver.findElement(By.xpath(xpathMap.get("FreeGamesSearchBox"))).sendKeys(
	 * "football"); Thread.sleep(3000);
	 * webdriver.findElement(By.xpath(xpathMap.get("FreeGameOffer1"))).click();
	 * webdriver.findElement(By.xpath(xpathMap.get("FreeGameOffer2"))).click();
	 * webdriver.findElement(By.xpath(xpathMap.get("FreeGamesAssignOffer"))).click()
	 * ; tc01.details_append("Verify Free Games Offer assigned successfully",
	 * "Free Games offer should be assigned",
	 * "Free Games Offer assigned successfully", "Pass"); Thread.sleep(5000); }
	 * catch(Exception e){ e.printStackTrace(); }
	 * 
	 * }
	 */

	public boolean freeGamesExpriyScreen() {
		return false;
	}

	public String freeGamesContinueExpiry() {
		return null;
	}

	public boolean freeGamesEntryScreen() {
		return false;
	}

	public boolean freeGameEntryInfo() {
		return false;
	}

	public boolean clickPlayNow() {
		return false;
	}

	public String freeGamesResumescreen() {
		return null;
	}

	public boolean freeGameResumeInfo() {
		return false;
	}

	public boolean resumeScreenDiscardClick() {
		return false;
	}

	public boolean confirmDiscardOffer() {
		return false;
	}

	public void getConsoleValue() {
		Coordinates coordinateObj = new Coordinates();
		JavascriptExecutor js = ((JavascriptExecutor) webdriver);
	}

	/**
	 * *Author:Pramlata Mishra This method is used to get element and convert the
	 * parameter type to long
	 * 
	 * @throws InterruptedException
	 */
	public void typeCasting(String cx, Coordinates coordinateObj) {
		JavascriptExecutor js = ((JavascriptExecutor) webdriver);
		try {
			Long sx = (Long) js.executeScript(cx);
			coordinateObj.setX(sx);
			log.debug(+sx);
		} catch (Exception e) {
			Double sdx = (Double) js.executeScript(cx);
			Long sx = sdx.longValue();
			coordinateObj.setX(sx);
			log.debug(+sx);
		}
	}

	public double typeCasting(Object object, AppiumDriver<WebElement> driver) {
		int value = 0;
		JavascriptExecutor js = ((JavascriptExecutor) driver);
		try {
			if (object instanceof Long)
				value = ((Long) object).intValue();
			else if (object instanceof Double)
				value = ((Double) object).intValue();

		} catch (Exception e) {
			e.getMessage();
		}
		return value;
	}

	public void clickAtCoordinates(String cx, String cy) {
		try {
			JavascriptExecutor js = ((JavascriptExecutor) webdriver);
			System.out.println(js.executeScript(cx));
			System.out.println(js.executeScript(cy));
			int dx = 0, dy = 0;
			try {
				Long sx = (Long) js.executeScript(cx);
				dx = sx.intValue();
			} catch (Exception e) {
				Double sx = (Double) js.executeScript(cx);
				dx = sx.intValue();
			}
			try {
				Long sy = (Long) js.executeScript(cy);
				dy = sy.intValue();
			} catch (Exception e) {
				Double sy = (Double) js.executeScript(cy);
				dy = sy.intValue();
			}
			/*
			 * Double sx = (Double) js.executeScript(cx); Double sy = (Double)
			 * js.executeScript(cy);
			 * 
			 * double dx = sx.doubleValue(); double dy = sy.doubleValue();
			 * 
			 * System.out.println("width:" +dx ); System.out.println("Height:" +dy);
			 */

			if (dx == 0) {
				dx = 1;
			}

			if (dy == 0) {
				dy = 1;
			}
			/*
			 * dx = dx * (size1.width) / 1280; dy = dy * (size1.height) / 720;
			 */

			System.out.println("width:" + dx);
			System.out.println("Height:" + dy);

			/*
			 * x = (int)dx; y = (int)dy;
			 */

			Thread.sleep(100);
			Actions act = new Actions(webdriver);
			WebElement ele1 = webdriver.findElement(By.id("gameCanvas"));
			act.moveToElement(ele1, dx, dy).click().build().perform();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * *Author:Pramlata Mishra This method is used to click at the element
	 * 
	 * @throws InterruptedException
	 */
	public void clickAtCoordinates(Long sx, Long sy) {
		try {
			// JavascriptExecutor js = ((JavascriptExecutor)webdriver);
			// Dimension d=webdriver.manage().window().getSize();
			int dx = 0, dy = 0;
			try {
				dx = sx.intValue();
			} catch (Exception e) {
				dx = sx.intValue();
			}
			try {
				dy = sy.intValue();
			} catch (Exception e) {
				dy = sy.intValue();
			}
			Thread.sleep(100);
			Actions act = new Actions(webdriver);
			WebElement ele1 = webdriver.findElement(By.id("gameCanvas"));
			/*
			 * for(int i=0;i<=100;i++) {
			 */

			act.moveToElement(ele1, dx, dy).click().build().perform();

			// dx=dy+5;dy=dy+10;
			// }

		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * *Author:Pramlata Mishra This method is used to get element text form game
	 * console
	 * 
	 * @throws InterruptedException
	 */
	public String GetConsoleText(String text) {
		String consoleText = null;
		try {		
			JavascriptExecutor js = (webdriver);	   
			consoleText = (String) js.executeScript(text);
			 log.debug("Text Read from Console="+consoleText);
		} catch (Exception e) {
			e.getMessage();
		}
		return consoleText;
	}

	/**
	 * Date:07/12/2017 Author:premlata Mishra This method is to split the string
	 * accprding to given string or symbol
	 * 
	 * @return true
	 */
	public String[] splitString(String val, String toreplace) {
		String splitedval[] = null;
		try {
			String valnew = val.replace(toreplace, "/");
			splitedval = valnew.split("/");
		} catch (Exception e) {
			e.getMessage();
		}
		return splitedval;
	}

	public ArrayList<Map<String, Long>> getConsoleListMap(String text) {
		ArrayList<Map<String, Long>> list = null;
		try {
			JavascriptExecutor js = ((JavascriptExecutor) webdriver);
			list = (ArrayList<Map<String, Long>>) js.executeScript(text);
		} catch (Exception e) {
			e.getMessage();
		}
		return list;
	}

	/*
	 * Date: 13/11/2017 Author: Ashish Kshatriya Description: This function is used
	 * for get Webelement text Parameter: By locator
	 */
	public String func_GetText(String locator) {
		/*try {
			WebElement ele = webdriver.findElement(By.xpath(locator));
			return ele.getText();

		} catch (NoSuchElementException e) {
			return null;
		}*/
		return null;
	}

	/**
	 * *Author:Premlata This method is used to tap on the toggle button
	 * 
	 * @throws InterruptedException
	 */

	public void clickWithWebElement(WebElement webViewElement, int Xcoordinate_AddValue) {
		String s = null;
		try {
			if (webdriver instanceof AndroidDriver) {
				webdriver.context("CHROMIUM");
			} else if (webdriver instanceof IOSDriver) {
				Set<String> contexts = webdriver.getContextHandles();
				for (String context : contexts) {
					if (context.startsWith("WEBVIEW")) {
						log.debug("context going to set in IOS is:" + context);
						webdriver.context(context);
					}
				}
			}
			double urlHeight = 0.0;
			JavascriptExecutor javaScriptExe = (JavascriptExecutor) webdriver;
			
			int webViewInnerWidth = ((Long) javaScriptExe.executeScript("return window.innerWidth || document.body.clientWidth")).intValue();
			//System.out.println("webViewInnerWidth "+webViewInnerWidth);
			int webViewOuterWidth = ((Long) javaScriptExe.executeScript("return window.outerWidth || document.body.clientWidth")).intValue();
			//System.out.println("webViewInnerWidth "+webViewInnerWidth);
			
			int webViewInnerHeight = ((Long) javaScriptExe.executeScript("return window.innerHeight || document.body.clientHeight")).intValue();
			//System.out.println("webViewInnerHeight "+webViewInnerHeight);
			int webViewOuterHeight = ((Long) javaScriptExe.executeScript("return window.outerHeight || document.body.clientHeight")).intValue();
			//System.out.println("webViewOuterHeight "+webViewOuterHeight);
			
			int webViewOffsetHeight = ((Long) javaScriptExe.executeScript("return window.offsetHeight || document.body.clientHeight")).intValue();
			//System.out.println("webViewOffsetHeight "+webViewOffsetHeight);
			
			int webViewBottomHeight = webViewOffsetHeight-webViewInnerHeight;
			//System.out.println("webViewBottomHeight "+webViewBottomHeight);
			
			Dimension elementSize = webViewElement.getSize();
			//System.out.println("elementSize "+elementSize);
			
			int webViewElementCoX = webViewElement.getLocation().getX() + (elementSize.width / 2);
			//System.out.println("webViewElementCoX "+webViewElementCoX);
			
			int webViewElementCoY = webViewElement.getLocation().getY() + (elementSize.height / 2);
			//System.out.println("webViewElementCoY "+webViewElementCoY);

			//double connectedDeviceWidth = typeCasting(javaScriptExe.executeScript("return window.screen.width * window.devicePixelRatio"), webdriver);
			//double connectedDeviceHeight = typeCasting(javaScriptExe.executeScript("return window.screen.height * window.devicePixelRatio"), webdriver);

			//System.out.println("connectedDeviceWidth "+connectedDeviceWidth);
			//System.out.println("connectedDeviceHeight "+connectedDeviceHeight);
			String curContext = webdriver.getContext();
			//webdriver.context("NATIVE_APP");
			
			urlHeight = (double) webViewOuterHeight - (double) webViewInnerHeight;
			//System.out.println("url height "+urlHeight);
			
			// urlHeight=0.0;
			double relativeScreenViewHeight = webViewOuterHeight - urlHeight;
			//System.out.println("relativeScreenViewHeight "+relativeScreenViewHeight);
			
			double nativeViewEleX = (double) (((double) webViewElementCoX / (double) webViewInnerWidth)* webViewOuterWidth);
			//System.out.println("nativeViewEleX "+nativeViewEleX);
			
			double nativeViewEleY = (double) (((double) webViewElementCoY / (double) webViewOuterHeight)* relativeScreenViewHeight);
			System.out.println("nativeViewEleY "+nativeViewEleY);
			
			tapOnCoordinates((nativeViewEleX + Xcoordinate_AddValue), ((nativeViewEleY + urlHeight + 1)));
			webdriver.context(curContext);
			
			/*
			 * if(xpathMap.get("serviceUrl").equalsIgnoreCase("yes")) urlHeight = (double)
			 * screenHeight * (0.12); else
			 * if(xpathMap.get("serviceUrl").equalsIgnoreCase("no")) urlHeight = 0.0; else
			 * urlHeight = (double) screenHeight * (0.22);
			 */
			// double nativeViewEleY = (double) (((double) webViewElementCoY / (double)
						// webViewHeight) );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*public void clickWithWebElement(WebElement webViewElement, int Xcoordinate_AddValue) {
		String s = null;

		try {
			double urlHeight = 0.0;
			JavascriptExecutor javaScriptExe = (JavascriptExecutor) webdriver;
			int webViewWidth = ((Long) javaScriptExe
					.executeScript("return window.innerWidth || document.body.clientWidth")).intValue();
			int webViewHeight = ((Long) javaScriptExe
					.executeScript("return window.innerHeight || document.body.clientHeight")).intValue();
			Dimension elementSize = webViewElement.getSize();
			int webViewElementCoX = webViewElement.getLocation().getX() + (elementSize.width / 2);
			int webViewElementCoY = webViewElement.getLocation().getY() + (elementSize.height / 2);
			webdriver.context("NATIVE_APP");
			double screenWidth = webdriver.manage().window().getSize().getWidth();
			double screenHeight = webdriver.manage().window().getSize().getHeight();
			if (xpathMap.get("serviceUrl").equalsIgnoreCase("yes")) {
				urlHeight = (double) screenHeight * (0.12);
			} else if (xpathMap.get("serviceUrl").equalsIgnoreCase("no")) {
				urlHeight = 0.0;
			} else {
				
			}
			urlHeight = (double) screenHeight * (0.22);
			double relativeScreenViewHeight = screenHeight - urlHeight;
			double nativeViewEleX = (double) (((double) webViewElementCoX / (double) webViewWidth) * screenWidth);
			double nativeViewEleY = (double) (((double) webViewElementCoY / (double) webViewHeight)
					* relativeScreenViewHeight);
			tapOnCoordinates(nativeViewEleX + Xcoordinate_AddValue, (nativeViewEleY + urlHeight + 1));
			webdriver.context("CHROMIUM");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	public void tapOnCoordinates(final double x, final double y) {
		// new TouchAction(webdriver).tap((int)x, (int)y).perform();
		// int X=(int) Math.round(x); // it will round off the values
		// int Y=(int) Math.round(y);
		System.out.println("X cor - " + x + "," + " Y cor - " + y);
		System.out.println(x);
		System.out.println(y);
		TouchAction action = new TouchAction(webdriver);
		
		action.tap(PointOption.point((int) x, (int) y)).perform();
		//action.press(PointOption.point((int) x, (int) y)).release().perform();

		// action.tap(PointOption.point(X, Y)).perform();

	}

	/**
	 * Date:07/12/2017 Author:Premlata Mishra only declaration of component store
	 * method
	 * 
	 * @return true
	 */
	public void refresh() {
		try {
			String currentUrl = webdriver.getCurrentUrl();
			webdriver.navigate().to(currentUrl);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	/**
	 * Date:07/12/2017 Author:Premlata Mishra only declaration of component store
	 * method
	 * 
	 * @return true
	 */
	public String gamelogo() {
		return null;
	}

	public String quickSpinStatus() {
		String opacityvalue = null;
		return opacityvalue;
	}

	public void SettingsToBasescen() throws InterruptedException {

	}

	public boolean verifySpin_Stop() {
		boolean spin = false;
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("Spin_Button"))));
			Native_Click(xpathMap.get("Spin_Button"));
			spin = webdriver.findElement(By.id(xpathMap.get("Stop_button_ID"))).isDisplayed();
		} catch (Exception e) {
			spin = false;
			e.getMessage();
		}
		return spin;
	}

	/**
	 * Date: 04/06/2018 Author: Havish Jain Description: This function is used for
	 * touch event in mobile devices Parameter: By locator
	 */
	public boolean Native_Click(String locator) {
		boolean present = true;
		try {
			webdriver.context("NATIVE_APP");
			MobileElement el4 = (MobileElement) webdriver.findElement(By.xpath(locator));
			el4.click();
			webdriver.context("CHROMIUM");
		} catch (NoSuchElementException e) {
			System.out.println(e.getMessage());
		}
		return present;
	}

	/**
	 * Date:07/12/2017 Author:premlata mishra This method is to wait till reels got
	 * land
	 * 
	 * @return true
	 * @throws InterruptedException
	 */
	public void waitTillStop() throws InterruptedException {
		try {
			webdriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			boolean test = webdriver.findElements(By.xpath("//*[@id='respin-footer-4']")).size() > 0;
			if (test) {
				while (webdriver.findElement(By.xpath("//*[@id='respin-footer-4']")).getText().isEmpty()) {
					Thread.sleep(500);
				}
			} else {
				Wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathMap.get("spinButtonBox"))));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void winClick() {
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("OneDesign_Spin_Text"))));
	}

	/**
	 * *Author:Premlata This method is used to tap on quick spin
	 * 
	 * @throws InterruptedException
	 */
	public boolean tap_quickSpin() throws Exception {
		boolean flag = true;
		try {
			new WebDriverWait(webdriver, 100)
					.until(ExpectedConditions.visibilityOf(webdriver.findElement(By.id("inGameClock"))));
			WebElement e = webdriver.findElement(By.xpath(xpathMap.get("quickSpin_Toggle")));
			clickWithWebElement(e, 0);
			return flag;
		} catch (Exception e) {
			e.getStackTrace();
			return flag = false;
		}

	}

	/**
	 * Date: 30/05/2018 Autohr: Havish Jain Description: This function used to
	 * handle the error
	 * 
	 * @return Title
	 */

	public void error_Handler(Mobile_HTML_Report report) {

		String error = xpathMap.get("Error");
		for (int i = 1; i <= Constant.REFRESH_RETRY_COUNT; i++) {
			if (isElementPresent(error)) {
				try {
					report.detailsAppend("Error is present", "Error is present", "Error is present", "Interrupted");
				} catch (Exception e) {

					e.printStackTrace();
				}
				webdriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
				webdriver.navigate().refresh();
			} else {
				break;
			}
		}
	}

	/**
	 * *Author:Premlata This method is used to slide the coin size slider
	 * 
	 * @throws InterruptedException
	 */
	public boolean moveCoinSizeSlider() throws InterruptedException {
		// func_Click(XpathMap.get("OneDesignbetbutton"));
		WebElement CoinSizeSlider = webdriver.findElement(By.xpath(xpathMap.get("OneDesignCoinSizeSlider")));
		Thread.sleep(3000);
		Actions action = new Actions(webdriver);
		action.dragAndDropBy(CoinSizeSlider, 127, 0).build().perform();
		return true;

	}

	public void Coinselectorclose() {

	}
/**
 * method is for paytable close
 * @throws InterruptedException
 */
	public void paytableClose() throws InterruptedException
	{

	}

	public boolean bigWin_Wait() {
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("OD_BIgWin"))));
		return true;
	}

	public String Slider_TotalBetamnt() {
		return null;
	}

	public String refreshWait() {
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("OneDesign_Spin_Text"))));
		// newFeature();
		return null;
	}

	/**
	 * Date:07/12/2017 Author:Premlata Mishra actually not necessery in component
	 * store just declaration needed
	 * 
	 * @return true
	 */
	public boolean betIncrease() {
		return true;
	}

	/**
	 * Date:12/08/2021 This method used to enable address bar
	 * 
	 * @return void
	 * @throws InterruptedException
	 */
	public void clickOnAddressBar() {
		Wait = new WebDriverWait(webdriver, 50);
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("clock_ID"))));
			if (osPlatform.equalsIgnoreCase("Android")) {
				String context = webdriver.getContext();
				webdriver.context("NATIVE_APP");
				Dimension size1 = webdriver.manage().window().getSize();
				int startx = size1.width / 2;
				int starty = size1.height / 2;
				TouchAction action = new TouchAction(webdriver);
				action.press(PointOption.point(0, 0)).release().perform();
				webdriver.context(context);
			} else {// tapping at down on the screen for ios 15 and above version
				Thread.sleep(2000);
				Dimension size = webdriver.manage().window().getSize();
				int anchor = (int) (size.width * 0.5);
				int startPoint = (int) (size.height * 0.2);
				int endPoint = (int) (size.height * 0.95);
				new TouchAction(webdriver).tap(PointOption.point(10, endPoint)).perform();
				Thread.sleep(3000);
				/*
				 * TouchAction touchAction = new TouchAction(webdriver);
				 * touchAction.tap(PointOption.point(10, 110)) .perform();
				 */
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public String FS_RefreshWait() {
		return null;
	}

	public String summaryScreen_Wait() {
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("clock_ID"))));
		return "";
	}

	public boolean customeverifyimage(String button) throws InterruptedException {
		boolean isMatch = false;
		return isMatch;
	}

	/**
	 * This method is used to verify the Base Game Logo
	 * 
	 * @param imagepath
	 * @return
	 * @throws InterruptedException
	 */
	public boolean verifySpin() {
		boolean spin = false;
		return spin;
	}

	public String freeGamesDiscardExistingOffer() {
		return null;
	}

	public void clickNextOffer() {

	}

	public void clickBaseSceneDiscard() {

	}

	public boolean open_Autoplay() {
		// TODO Auto-generated method stub
		return false;
	}

	public void close_Autoplay() {
		// TODO Auto-generated method stub
	}

	/**
	 * Author: Havish Jain This method is used to get data from the har and get the
	 * URL of assets.
	 * 
	 * @param proxy
	 * @return ArrayList
	 */
	public ArrayList<String> cacheBustingCDN(BrowserMobProxyServer proxy2) {
		List<String> al = new ArrayList<String>();
		Har nhar;
		HarLog hardata;
		List<HarEntry> entries;
		Iterator<HarEntry> itr;
		waitForPageToBeReady();
		nhar = proxy2.getHar();

		hardata = nhar.getLog();
		entries = hardata.getEntries();
		itr = entries.iterator();

		while (itr.hasNext()) {
			HarEntry entry = itr.next();
			String requestUrl = entry.getRequest().getUrl().toString();
			al.add(requestUrl);
		}
		return (ArrayList<String>) al;
	}

	/**
	 * Author: Havish Jain This method is used read Manifest file from the server
	 * and save the json data in a list.
	 * 
	 * @param manifest
	 *            file
	 * @return ArrayList
	 */
	public ArrayList<String> readManifestFile(String manifestFile) throws ParseException, IOException {
		List<String> al = new ArrayList<String>();
		FileReader reader = new FileReader(manifestFile);

		String str = null;
		try (BufferedReader br = new BufferedReader(reader)) {
			String st = new String();
			while (true) {
				str = new String(st);
				st = br.readLine();
				if (st == null) {
					break;
				}
			}
		}

		String[] words = str.split("=");

		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(words[1]);

		Set set = jsonObject.keySet();
		for (Object s : set) {
			JSONObject jsonObject3 = (JSONObject) jsonObject.get(s);
			String resourcePath = jsonObject3.get("path").toString();
			if (!resourcePath.contains("960x640") && !resourcePath.contains("1024x768"))
				al.add(jsonObject3.get("path").toString());
		}
		return (ArrayList<String>) al;
	}

	public void setNameSpace() {
	}

	/*
	 * Function overloading check page navigation
	 * 
	 * @input: Report,page tital
	 * 
	 * @return: void
	 */

	public void checkPageNavigation(Mobile_HTML_Report report, String pagetital) {
		try {

			// Below code is for if responsible gaming link is opening in new tab
			String mainwindow = webdriver.getWindowHandle();
			Set<String> s1 = webdriver.getWindowHandles();
			if (s1.size() > 1) {
				Iterator<String> i1 = s1.iterator();
				while (i1.hasNext()) {
					String ChildWindow = i1.next();

					if (mainwindow.equalsIgnoreCase(ChildWindow)) {
						String ChildWindow1 = i1.next();
						webdriver.switchTo().window(ChildWindow1);
					}

					String tital = webdriver.getTitle();
					log.debug(tital);
					if (tital.equalsIgnoreCase(pagetital)) {
						report.detailsAppend("verify the " + pagetital + " link ",
								" Should nevigate to" + pagetital + " link", "Navigate to " + pagetital + " link",
								"pass");
						log.debug("Page navigated succesfully");
						webdriver.close();// closing new window
						webdriver.switchTo().window(mainwindow);
						waitForSpinButton();

					} else {
						report.detailsAppend("verify the" + pagetital + " link ",
								" Should nevigate to " + pagetital + "link",
								"Does not Navigate to " + pagetital + " link", "fail");
					}
				}
			}
			// To check is responsible gaming link is open in same tab
			else {
				String tital = webdriver.getTitle();
				log.debug(tital);
				if (tital.equalsIgnoreCase(pagetital)) {
					report.detailsAppend("verify the" + pagetital + " link ",
							" Should nevigate to" + pagetital + " link", "Navigate to" + pagetital + " link", "pass");
					log.debug("Page navigated succesfully");
					webdriver.navigate().back();
					webdriver.navigate().refresh();
					// JavascriptExecutor js = (JavascriptExecutor) webdriver;
					// js.executeScript("window.history.go(-1)");
					waitForSpinButton();

				} else {
					report.detailsAppend("verify the " + pagetital + " link ",
							" Should nevigate to" + pagetital + "link", "Does not Navigate to " + pagetital + " link",
							"fail");
					webdriver.navigate().back();
					webdriver.navigate().refresh();
				}
			}

		} catch (Exception e) {
			log.error("error in navigation of page");
		}
	}

	public boolean isStopButtonAvailable() {
		return false;
	}

	/*
	 * To get the numaric text from the console
	 */
	public long getConsoleNumeric(String text) {
		long consoleNumeric = 0;
		try {
			JavascriptExecutor js = ((JavascriptExecutor) webdriver);
			consoleNumeric = (Long) js.executeScript(text);
		} catch (Exception e) {
			e.getMessage();
		}
		return consoleNumeric;
	}

	public boolean verifyAutoplayConsoleOptions(Mobile_HTML_Report report) {

		return false;
	}

	public boolean verifyAutoplayPanelOptions(Mobile_HTML_Report report) {

		return false;
	}

	/*
	 * Date: 25/04/2019 Description:To verify Auto play on refreshing Parameter: NA
	 * 
	 * @return boolean
	 */
	public boolean isAutoplayOnAfterRefresh() {
		boolean onrefresh;
		try {
			webdriver.findElement(By.xpath(xpathMap.get("Start_Autoplay"))).click();
			Thread.sleep(3000);
			webdriver.navigate().refresh();
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("spin_button"))));
			log.debug("On refresh previous autoplay session has not resume");
		} catch (Exception e) {
			log.error("On refresh previous autoplay session has resume");
			return onrefresh = false;
		}

		return onrefresh = true;
	}

	public boolean isAutoplayPauseOnFocusChange(Mobile_HTML_Report sanityreport) {

		return false;
	}

	public boolean isAutoplayStoppedOnMenuClick() {
		return false;
	}

	public boolean verifyALLBetValues(Mobile_HTML_Report report) {

		return false;
	}

	public boolean verifyAllQuickBets(Mobile_HTML_Report report) {

		return false;
	}

	public boolean verifyCurrencyFormat(String currencyFormat) {
		// TODO Auto-generated method stub
		return false;
	}

	public void verifyPaytableCurrency(Mobile_HTML_Report currencyReport, String currencyName) {
		// TODO Auto-generated method stub

	}

	public boolean isBetChangedOnRefresh() {

		return true;
	}

	public boolean validateMiniPaytable(String bet, Mobile_HTML_Report sanityreport) {

		return false;
	}

	public void openBetPanel() {
	}

	public String setTheNextLowBet() {

		return null;
	}

	public void verifyMenuOptionNavigations(Mobile_HTML_Report report) {
	}

	public boolean verifyResponsibleGamingNavigation(Mobile_HTML_Report denmark) {
		return false;
	}

	public boolean verifyHelpNavigation(Mobile_HTML_Report denmark) {
		return false;
	}

	public void BackTogamefromnavigation(Mobile_HTML_Report report) {
	}

	public boolean verifysettingsOptions(Mobile_HTML_Report report) throws InterruptedException {
		return false;
	}

	public boolean verifypaytablenavigation(Mobile_HTML_Report report) {
		return false;
	}

	public boolean verifyhelpenavigation(Mobile_HTML_Report report) {
		return false;
	}

	public void paytablenavigationClose() {
	}

	public boolean verifyresponsiblegamingenavigation(Mobile_HTML_Report report) {
		return false;
	}

	public boolean verifyplaychecknavigation(Mobile_HTML_Report report) {
		boolean ret = false;
		return ret;
	}

	public boolean verifyloyaltynavigation(Mobile_HTML_Report report) {
		return false;
	}

	public boolean verifycashChecknavigation(Mobile_HTML_Report report) {
		return false;
	}

	public boolean verifylobbynavigation() {
		return false;
	}

	public boolean CheckNavigateGameToBanking(Mobile_HTML_Report report) {
		return false;

	}

	public String Verifystoryoptioninpaytable(Mobile_HTML_Report report, String languageCode) {
		return languageCode;
	}

	public boolean waitForbigwin() {
		return false;
	}

	public void miniPaytableScreeShots(Mobile_HTML_Report currencyReport, String currencyName) {

	}

	public String getMinimumBet() {

		return null;
	}

	public String getCurrentCredits() {
		return null;
	}

	public boolean freeSpinWinSummaryCurrencyFormat(String currencyFormat) {
		return false;
	}

	public boolean reSpinOverlayCurrencyFormat(String currencyFormat) {
		return false;
	}

	public void clickOnReSpinOverlay() {

	}

	public boolean loadGame(String url) {
		boolean isGameLaunch = false;
		try {
			Wait = new WebDriverWait(webdriver, 120);
			webdriver.navigate().to(url);
			System.out.println(url);
			if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame"))
					|| (Constant.YES.equalsIgnoreCase(xpathMap.get("continueBtnOnGameLoad")))) {
				//log.debug("Waiting for clock to be visible");
				//Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("clock"))));
				isGameLaunch = true;
				log.debug(" clock is visible");

				webdriver.getContext();
				
				if("Yes".equalsIgnoreCase(xpathMap.get("CntBtnNoXpath"))) 
				{	
					       threadSleep(2000);
				        elementWait("return "+xpathMap.get("CntBtnNoXpathSatus"), true);
				        
				        isGameLaunch = true;
				       
		      	}	else
				{
			
				Wait.until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath(xpathMap.get("OneDesign_NewFeature_ClickToContinue"))));
				isGameLaunch = true;
				
				}
				
				
				
			} else {
				log.debug("Waiting for clock to be visible");
				Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("clock"))));
				isGameLaunch = true;
				log.debug(" clock is visible");
			}
			log.debug("game loaded ");
		} catch (WebDriverException e) {
			log.debug("Exception occur in loadgame()");
			log.error(e.getMessage());
		}
		return isGameLaunch;
	}

	public void verifySpinBtnState(Mobile_HTML_Report sanityreport) {

	}

	public void Payoutvarificationforallbet(Mobile_HTML_Report sanityreport) {
		// TODO Auto-generated method stub

	}

	/**
	 * * Date: 29/05/2018 Author: Premlata Mishra Description: This function is used
	 * to verify paytable payouts and it's values Parameter:
	 */
	public double verifyPaytable_Payouts(String xmlpayout, double payline, double bet, String scatter) {
		double verifypayout = 0.0, intPayout, verifypayoutnew = 0.0;
		try {
			// double gamepayout_Double=gamepayout(xpath);
			intPayout = Integer.parseInt(xmlpayout);// xmlpayout
			if (scatter.equalsIgnoreCase("yes")) {
				verifypayout = (bet * intPayout);
				verifypayoutnew = round(verifypayout);
				return verifypayoutnew;
			} else {
				verifypayout = (bet * intPayout) / payline;
				verifypayoutnew = round(verifypayout);
				return verifypayoutnew;
			}
		} catch (Exception e) {
			log.error("error in verifying payout", e);
		}
		return verifypayout;
	}

	public double round(double number) {
		DecimalFormat dnf = new DecimalFormat("#.##");
		double roundednumber = new Double(dnf.format(number));
		return roundednumber;
	}

	public boolean isPaytableAvailable() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean checkAvilability(String string) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean verifyBigWinCurrencyFormat(String currencyFormat, Mobile_HTML_Report currencyReport,
			String currencyName) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setDefaultBet() {
		// TODO Auto-generated method stub

	}

	/*
	 * Function to decode the url. Input- String encoded url Output- String decoded
	 * url
	 * 
	 * @author:- Snehal Gaikwad
	 */
	public String decode(String url) {
		try {
			String prevURL = "";
			String decodeURL = url;
			while (!prevURL.equals(decodeURL)) {
				prevURL = decodeURL;
				decodeURL = URLDecoder.decode(decodeURL, "UTF-8");
			}
			log.info("Decoded Url=" + decodeURL);
			return decodeURL;
		} catch (UnsupportedEncodingException e) {
			return "Issue while decoding" + e.getMessage();
		}
	}

	public void validateMenuInBigWin(Mobile_HTML_Report report) {
		// declaring in super class
	}

	public void validatePaytableNavigationInBigWin(Mobile_HTML_Report report) {
		// declaring in super class

	}

	public void bigWinQuickSpinOnOffValidation(Mobile_HTML_Report report) {
		// declaring in super class

	}

	public void bigwinwithAutoplay(Mobile_HTML_Report report) {
		// declaring in super class

	}

	public void bigWinWithOrientationChange(Mobile_HTML_Report report) {
		// declaring in super class

	}

	public void bigWinWithSpin(Mobile_HTML_Report report) {
		// declaring in super class
	}

	public boolean elementWait(String element, boolean value) {
		// declaring in super class
		return false;
	}

	public Map<String, Long> getConsoleLongMap(String text) {
		Map<String, Long> map = null;
		JavascriptExecutor js = ((JavascriptExecutor) webdriver);
		map = (Map<String, Long>) js.executeScript(text);
		return map;
	}

	public List<Long> getConsoleLongList(String text) {
		List<Long> list = null;

		JavascriptExecutor js = ((JavascriptExecutor) webdriver);
		list = (List<Long>) js.executeScript(text);
		return list;
	}

	public void verifyJackPotBonuswithScreenShots(Mobile_HTML_Report report, String languageCode) {
		// declare in parent class

	}

	public void threadSleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			log.debug("Exception while thread sleep", e);
			Thread.currentThread().interrupt();

		}
	}

	public String convertToMonthWeekssFormat(long seconds) {

		String convertedStr = null;
		long month = seconds / (4 * 7 * 24 * 3600);
		seconds = seconds % (4 * 7 * 24 * 3600);

		long week = seconds / (7 * 24 * 3600);
		seconds = seconds % (7 * 24 * 3600);

		long day = seconds / (24 * 3600);
		seconds = seconds % (24 * 3600);

		long hour = seconds / 3600;
		seconds %= 3600;

		long minutes = seconds / 60;
		convertedStr = new String(
				month + "m" + " " + week + "w" + " " + day + "d" + " " + hour + "h" + " " + minutes + "m");

		return convertedStr;

	}

	public void setMaxBetPanel() {
		// TODO Auto-generated method stub

	}

	public void funvFullScreenOff() {
		// TODO Auto-generated method stub

	}

	public ArrayList<String> getConsoleList(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getBetAmtString() {
		return null;
	}

	public boolean quickSpinOff() {
		return false;
	}

	public boolean quickSpinclickWith() {
		return false;

	}

	public boolean quickSpinOn() {
		return false;
	}

	public boolean openTotalBetBoolean() {
		return false;
	}

	public boolean verifypaytable() {
		return false;
	}

	public void waitFor(String string) {
		// declaration in parent class
	}

	public boolean drawCollectBaseGame(Mobile_HTML_Report report, String languageCode) throws InterruptedException {
		// declaration in parent class
		return false;
	}

	public void doubleToCollect(Mobile_HTML_Report sanityReport) throws Exception {
		// declaration in parent class
	}

	public boolean verifyPaytablePresent() {
		return false;
	}

	public double getBetAmtDouble() {
		return 0;
	}

	public boolean dealClick() throws InterruptedException {
		return false;
	}

	public boolean drawClick() throws InterruptedException {
		return false;
	}

	public void doubleToGambleReached(Mobile_HTML_Report languageReport, String languageCode)
			throws InterruptedException {
		// declaration in parent class
	}

	public void paytableClickVideoPoker(Mobile_HTML_Report languageReport, String languageCode)
			throws InterruptedException {
		// declaration in parent class

	}

	public void clickWithWebElement1(WebElement webViewElement, int Xcoordinate_AddValue) {
		String s = null;
		try {
			double urlHeight = 0.0;
			String currentcontext = webdriver.getContext();
			JavascriptExecutor javaScriptExe = (JavascriptExecutor) webdriver;
			int webViewWidth = ((Long) javaScriptExe
					.executeScript("return window.innerWidth || document.body.clientWidth")).intValue();
			int webViewHeight = ((Long) javaScriptExe
					.executeScript("return window.innerHeight || document.body.clientHeight")).intValue();
			Dimension elementSize = webViewElement.getSize();
			int webViewElementCoX = webViewElement.getLocation().getX() + (elementSize.width / 2);
			int webViewElementCoY = webViewElement.getLocation().getY() + (elementSize.height / 2);

			webdriver.context("NATIVE_APP");
			double screenWidth = webdriver.manage().window().getSize().getWidth();
			double screenHeight = webdriver.manage().window().getSize().getHeight();

			urlHeight = screenHeight - webViewHeight;
			double relativeScreenViewHeight = screenHeight - urlHeight;
			double nativeViewEleX = (double) (((double) webViewElementCoX / (double) webViewWidth) * screenWidth);
			double nativeViewEleY = (double) (((double) webViewElementCoY / (double) webViewHeight)
					* relativeScreenViewHeight);
			tapOnCoordinates(nativeViewEleX + Xcoordinate_AddValue, (nativeViewEleY + urlHeight + 1));
			webdriver.context(currentcontext);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setSoundFalgActive(boolean b) {

	}

	public void setQuickSpinOn() {

	}

	public void startAutoPlay() {

	}

	public void clickFregamesPlay() {

	}

	/**
	 * This method crate freegames using free game api and assign to user
	 * 
	 * @param userName
	 * @param defaultNumberOfGames
	 * @param offerExpirationUtcDate
	 * @param balanceTypeId
	 * @param mid
	 * @param cid
	 * @param noOffers
	 *            optional parameter
	 */

	public boolean addFreeGameToUserInBluemesa(String userName, int defaultNumberOfGames, String offerExpirationUtcDate,
			String balanceTypeId, int mid, int cid, @Optional("1") int noOfOffers) {
		RestAPILibrary apiobj = new RestAPILibrary();
		CommonUtil util = new CommonUtil();
		boolean isFreeGameAssigned = false;
		FreeGameOfferResponse freeGameOfferRes = apiobj.addFreeGameOffer(defaultNumberOfGames,
				util.randomFreegameOfferName(), offerExpirationUtcDate, balanceTypeId, mid, cid);
		if (freeGameOfferRes != null) {
			// assign multiple offer
			for (int OfferCnt = 1; OfferCnt <= noOfOffers; OfferCnt++) {
				isFreeGameAssigned = apiobj.assignFreeGameOfferByOfferID(userName, freeGameOfferRes.getOfferId(),
						freeGameOfferRes.getOfferAvailableFromUtcDate());
			}
		}
		return isFreeGameAssigned;
	}

	/**
	 * This method create and assign free game offer to user in axiom
	 * 
	 * @param userName
	 * @param defaultNumberOfGames
	 * @param offerExpirationUtcDate
	 * @param balanceTypeId
	 * @param mid
	 * @param cid
	 * @param noOfOffers
	 *            optional parameter
	 */

	public boolean addFreeGameToUserInAxiom(String userName, int defaultNoOfFreeGames, String offerExpirationUtcDate,
			String balanceTypeID, int mid, int cid, @Optional("1") int noOfOffers) {
		RestAPILibrary apiobj = new RestAPILibrary();
		CommonUtil util = new CommonUtil();
		boolean isFreeGameAssigned = false;
		for (int OfferCnt = 1; OfferCnt <= noOfOffers; OfferCnt++) {
			isFreeGameAssigned = apiobj.createFreeGameInAxiom(userName, util.randomFreegameOfferName(),
					defaultNoOfFreeGames, offerExpirationUtcDate, balanceTypeID, mid, cid);
		}
		return isFreeGameAssigned;
	}

	/**
	 * Below code unlock the bonus by clicking on bonus selction iteam depending on
	 * the NoOfBonusSelection count
	 */
	public void unlockBonus(Mobile_HTML_Report language) {
		// decleration in parent class

	}

	public String replaceParamInHook(String selectBonus, Map<String, String> paramMap) {
		return null;
	}

	public boolean elementWait(String string, boolean b, int i) {
		return false;
	}

	public boolean isCreditDeducted(String currentCreditAmount, String currentBet) {
		return false;
	}

	public boolean isPlayerWon() {
		return false;
	}

	public boolean isWinAddedToCredit(String currentCreditAmount, String currentBet) {
		return false;
	}

	public void validateCreditForWinLoss(String currentBet, Mobile_HTML_Report language, String languageCode) {

	}

	public boolean waitForWinDisplay() {
		return false;
	}

	public void paytableOpenScroll(Mobile_HTML_Report report, String language) throws Exception {

	}

	public void waitFreeSpinEntry(Mobile_HTML_Report language, String languageDescription, String languageCode) {

	}

	public void func_FullScreen(Mobile_HTML_Report language) {

	}

	public void freeSpinWithAddressBar(Mobile_HTML_Report language, String languageDescription, String languageCode) {

	}

	/**
	 * To verify bonus game win currency format
	 * 
	 * @param currencyFormat
	 * @return
	 */
	public boolean bonusWinCurrFormat(String currencyFormat) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * To click on the continue button after bonus game completion
	 * 
	 * @return
	 */
	public String clickContinueInBonusGame() {
		String FreeSpin = "";
		return FreeSpin;
		// TODO Auto-generated method stub

	}

	/**
	 * To select the bonus books in bonus game(hotInk)
	 */
	public boolean bonusSelection(String currencyFormat, int iteamCnt) {
		// TODO Auto-generated method stub
		return false;

	}

	/**
	 * To validate currency amount with the currency format in excel sheet
	 */
	public boolean currencyFormatValidator(String curencyAmount, String currencyFormat) {
		return false;
	}

	/**
	 * To verify piggybank bonus in bust the bank
	 * 
	 * @return
	 */
	public boolean piggyBankBonus(String currencyFormat) {
		// TODO Auto-generated method stub
		return false;
	}

	public String getOsPlatform() {
		return osPlatform;
	}

	public void setOsPlatform(String osPlatform) {
		this.osPlatform = osPlatform;
	}

	/**
	 * @return the osVersion
	 */
	public double getOsVersion() {
		double dOsVersion = 0;
		if (osVersion != null && !osVersion.equals("")) {
			if (osVersion.indexOf('.', osVersion.indexOf('.') + 1) != -1) {
				osVersion = osVersion.substring(0, osVersion.indexOf('.'));
				log.debug("osVersion" + osVersion);
			}
			dOsVersion = Double.parseDouble(osVersion);
		}
		return dOsVersion;
	}

	/**
	 * @param osVersion
	 *            the osVersion to set
	 */
	public void setOsVersion(String osVersion) {

		this.osVersion = osVersion;

	}

	/**
	 * This method returns boolean value based on fullscreen overlay visibility
	 * 
	 * @return
	 */

	public boolean isFullScreenOverlayVisible() {
		boolean isVisible = false;
		WebDriverWait expWait = new WebDriverWait(webdriver, 5);

		int size = webdriver.findElements(By.xpath(xpathMap.get("fullScreenOverlay"))).size();

		if (size != 0) {
			isVisible = true;
		}

		return isVisible;
	}

	// verify Session Reminder
	public boolean verifySessionReminderPresent(Mobile_HTML_Report report) {
		return false;
	}

	/**
	 * This method is used To click on continue button in session reminder
	 */
	public void selectContinueSession() {
		// TODO Auto-generated method stub

	}

	/**
	 * This method is used to check whether Topbar is present or not
	 * 
	 * @return
	 */
	public boolean isTopBarVisible() {

		boolean isTopBarPresentinGame = false;
		try {
			isTopBarPresentinGame = webdriver.findElement(By.xpath(xpathMap.get("isTopbarPresent"))).isDisplayed();
			// System.out.println(isTopBarPresentinGame);
			if (isTopBarPresentinGame) {
				log.debug("Topbar is visible");
				System.out.println("Topbar is visible");
				return isTopBarPresentinGame;
			}
		} catch (Exception e) {
			log.error("Not able to verify Topbar", e);
		}

		return isTopBarPresentinGame;
	}

	/**
	 * This method is used to click on the Autoplay win/loss limit dialog box
	 */
	public void clickOnPrimaryBtn() {

	}

	/**
	 * Below methods are for Pokerstar GTR Teast cases
	 */

	public boolean openAutoplay() {
		return false;
	}

	/**
	 * getWinLimitValues returns the list of Loss Limit Values
	 * 
	 * @return
	 */

	public List<WebElement> getLossLimitValues() {
		List<WebElement> winLimitList = null;
		try {
			Select selection = new Select(webdriver.findElement(By.xpath((xpathMap.get("lossLimit")))));
			winLimitList = selection.getOptions();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return winLimitList;
	}

	public boolean checkLossLimitReachedForBet(String lossLimit, int counter) {
		return false;
	}

	public boolean checkWinLimitReachedForBet(String winLimit, int counter) {
		return false;
	}
	/*
	 * get Win limit values using hooks Used for screch games
	 */

	/**
	 * getWinLimitValues returns the list of Win Limit Values
	 * 
	 * @return
	 */

	public List<WebElement> getWinLimitValues() {
		List<WebElement> winLimitList = null;
		try {
			WebElement webElement = webdriver.findElement(By.xpath((xpathMap.get("winLimit"))));
			Select selection = new Select(webElement);
			winLimitList = selection.getOptions();

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return winLimitList;
	}

	/*
	 * To verify Safe bonus bonus in bust the bank
	 * 
	 * @return
	 */
	public boolean verifySafeBonusCurrency(String currencyFormat) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Verify the Italy Game paly Feature / Verify that game has scroll bar feature
	 */

	public void italyScrollBarCickAmount(Mobile_HTML_Report italy, String amount) 
	{
		try {

			WebElement slider = webdriver.findElement(By.id(xpathMap.get("userInput")));// select scroll bar
			System.out.println("Verify that game has Game play feature");
			log.debug("Verify that  game has Game play feature ");
			italy.detailsAppend(" Verify that  game has Game play feature ", " Game play feature selected",
					" Game play feature selected", "PASS");
			Thread.sleep(3000);

			JavascriptExecutor js = (JavascriptExecutor) webdriver;
			js.executeScript("javascript:document.getElementById(\"" + xpathMap.get("userInput") + "\").value=" + amount + ";");// (Value took from Excel)
			Thread.sleep(3000);
			System.out.println("Slider value1 for credits is : " + slider.getAttribute("value"));// value is not updated
																									// in the game 90 on
																									// the gaming screen
			log.debug("Slider value1 for credits is : " + slider.getAttribute("value"));

			if (osPlatform.equalsIgnoreCase("Android")) {
				slider.sendKeys(Keys.RIGHT);
			} else {
				slider.sendKeys(Keys.TAB);
				// slider.click();

			}

			Thread.sleep(3000);

			System.out.println("Slider value2 for credits is : " + slider.getAttribute("value"));// value updated in the
																									// game 90.01
			log.debug("Slider value2 for credits is : " + slider.getAttribute("value"));
			selectedamout = slider.getAttribute("value");

			italy.detailsAppend(" Game Play  scroll Bar  ", " Amount Slided ", " Amount Slided", "PASS");
			Thread.sleep(2000);

			webdriver.findElement(By.xpath(xpathMap.get("Italy_Submit"))).click();
			System.out.println("Clicked on Submit");
			log.debug("Clicked on Submit");
			italy.detailsAppend(" Submit Button ", " Clicked on Submit Button ", "Clicked on Submit Button ", "PASS");
			Thread.sleep(3000);

			// webdriver.findElement(By.xpath(xpathMap.get("Italy_Play"))).click();
			MobileElement italyplay = (MobileElement) webdriver.findElement(By.xpath(xpathMap.get("Italy_Play")));
			webdriver.executeScript("arguments[0].click();", italyplay);
			italy.detailsAppend("  Play Button ", " Clicked on play Button ", " Clicked on play Button ", "PASS");
			System.out.println("Clicked on Play");
			log.debug("Clicked on Play");
			Thread.sleep(3000);

			/*
			 * webdriver.findElement(By.xpath(xpathMap.get("Italy_Continue"))).click();
			 * italy.detailsAppend(" Verify that  game is clicked on Continue Button ",
			 * " clicked on Continue Button"," clicked on Continue Button ", "Pass" );
			 * System.out.println("Clicked on Continue Button ");
			 */

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		System.out.println("selected amount is " + selectedamout);
		log.debug("selected amount is " + selectedamout);
	}
	/**
	 * method is used to check if it is displayed or not 
	 * @param locator
	 * @return
	 */
	public boolean isDisplayed(String locator)
	{
		boolean islocatorVisible = false;
	try 
	{
		islocatorVisible = webdriver.findElement(By.xpath(xpathMap.get(locator))).isDisplayed();
		if (islocatorVisible)
		{
			log.debug("Topbar is visible");
			 islocatorVisible = true;
		}
	} 
	catch (Exception e) 
	{
		log.error("Not able to verify Topbar", e);
	}
	return islocatorVisible;
	}

	/**
	 * This method is used to check whether Topbar is present or not
	 * 
	 * @return
	 */
	public boolean topBarVisibilitycheck()
	{
		boolean ret = false;
		String TopBar = "TopBar";
		try
		{
		isDisplayed(TopBar);
		ret = true;
		}
		catch (Exception e) 
		{
			log.error("Not able to verify Topbar", e);
		}
		return ret;
	}
/**
 * method is for to check if it is Displayed or not  and to get the Text 	
 */
	public String isDisplayedAndGetText(String locator)
	{
		String getText = null;
		try 
		{
			boolean isloactorVisible = webdriver.findElement(By.xpath(xpathMap.get(locator))).isDisplayed();
			if (isloactorVisible) 
			{
				getText = webdriver.findElement(By.xpath(xpathMap.get(locator))).getText(); 
				log.debug( "Text  is "+getText);
				//System.out.println(getText);	
		    } 
			else 
			{
				log.debug("Locator from Menu is not Visible");
			}
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
		}
		return getText;
	}

	/**
	 * Compare Game Name from Top Bar
	 */

	public String gameNameFromTopBar(Mobile_HTML_Report report) 
	{
		String GameName = "GameName_From _TopBar" ;
		String gameNamefromExcel = xpathMap.get("GameName");
		String gamenametext = isDisplayedAndGetText(GameName);
		try
		{
			if (gamenametext.equals(gameNamefromExcel)) // Expected VS Actual Name .
			{
			log.debug("Game name is same");
			} 
			else 
			{
			log.debug("Game name is different");
			}
		} 
		catch (Exception e)
		{
			log.error("Not able to verify Game name", e);
		}
		return GameName;

	}

	/**
	 * verify clock on topbar
	 * 
	 * @return
	 */
	public String clockFromTopBar() 
	{
		String clock = null;
		try 
		{
			boolean isClockPresent = webdriver.findElement(By.id(xpathMap.get("clock_ID"))).isDisplayed();
			if (isClockPresent)
			{
				log.debug("clock is visible");
				clock = webdriver.findElement(By.id(xpathMap.get("clock_ID"))).getText();
				log.debug("Time is :" + clock);
			}
			else 
			{
				log.debug("closck on thetop bar is not visible");
			}
		}
		catch (Exception e) 
		{
			log.error("Not able to verify clock", e);
		}
		return clock;
	}

	/*
	 * Verify and get currency values
	 */
	public String italygetCurrentCredits()
	{
		String balance = null;
		String consoleBalance = null;

		if (!GameName.contains("Scratch")) {
			if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame")))// force console
			{
				balance = "return " + xpathMap.get("Balancetext");
			} else
				balance = "return " + xpathMap.get("Balancetext");
		} else {
			balance = "return " + xpathMap.get("InfoBarBalanceTxt");

		}
		consoleBalance = GetConsoleText(balance);
		System.out.println("Console Balanace is " + consoleBalance);

		String consolebalance = consoleBalance.replace("CREDITS: € ", " ").replace(",", ".");

		System.out.println("Console Balanace is" + consolebalance);
		return consolebalance;

	}

	/**
	 * This method is to check whether the Credit amount is same selected one or not
	 */

	public String italyCreditAmountComparission() {

		String gameconsolecredit = "";

		try {

			String creditamount = italygetCurrentCredits(); // get Console credit from italygetCurrentCredits method
			creditamount = creditamount.replace(" ", "");// replace space with null(from italygetCurrentCredits method)

			double dblSelectAmount = Double.parseDouble(selectedamout);
			double dblCreditAmount = Double.parseDouble(creditamount);

			// we get selected amount from click amount method
			if (dblCreditAmount == dblSelectAmount) // Expected VS Actual Name .
			{
				log.debug("Take away screen game credit is selected as" + selectedamout);
				log.debug("Credit Amounts are Same ");
			} 
			else 
			{
				log.debug("Take away screen game credit is selected as " + selectedamout);
				log.debug("Credit Amounts are different  ");
			}
		} catch (Exception e)
		{
			log.error("Not able to verify Game name", e);
		}
		return gameconsolecredit;

	}

	/**
	 * This method checks whether quickspin available or not in Italy Market
	 * 
	 * @return
	 */

	public boolean italy_IsQuickspinAvailable() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * This method checks whether quickspin available or not in Italy Market
	 * 
	 * @return
	 */

	public boolean italy_isStopButtonAvailable() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * To verify Responsible gaming is present on topbat in uk market
	 * 
	 * @return
	 */
	public boolean verifyResponsibleGamingOnTopbar() {
		boolean isResponsibleGamingAvailable = false;

		try {
			List<WebElement> elementList = webdriver.findElements(By.id(xpathMap.get("clickOnMenuOnTopbar"))); // Search
																												// on
																												// the
																												// page.
			if (elementList.size() > 0) {
				// webdriver.findElement(By.id(xpathMap.get("clickOnMenuOnTopbar"))).click(); //
				// If size is > Zero then it will click on session continue button

				nativeClickByID(xpathMap.get("clickOnMenuOnTopbar"));
				System.out.println("Element found for Responsible Gaming on the Top Bar ");
				log.debug("Element found for Responsible Gaming on the Top Bar  ");

			} else {
				System.out.print(" Element not found for Responsible Gaming on the Top Bar ");
				log.debug(" Element not found for Responsible Gaming on the Top Bar ");
			}

			String isResponsibleGaming = webdriver.findElement(By.xpath(xpathMap.get("isResponsibleGamingAvailable")))
					.getText();
			// System.out.println(isResponsibleGamingAvailable);
			if (isResponsibleGaming.equals("Responsible Gaming")) {
				log.debug("Responsible Gaming name is Same");
				System.out.println("Responsible Gaming name is Same");
				isResponsibleGamingAvailable = true;
			} else {
				log.error("Responsible Gaming name is not  Same");
				System.out.println("Responsible Gaming name is not  Same");
				isResponsibleGamingAvailable = false;
			}
		} catch (Exception e) {
			log.error("Not able to verify Responsible Gaming Availability", e);
		}
		return isResponsibleGamingAvailable;
	}

	/**
	 * To verify Help is present on topbar in uk market or not
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public boolean verifyHelpOnTopbar() throws InterruptedException {
		boolean isHelpAvailable = false;
		try {

			List<WebElement> elementList = webdriver.findElements(By.id(xpathMap.get("clickOnMenuOnTopbar"))); // Search
																												// on
																												// the
																												// page.

			if (elementList.size() > 0) {
				// webdriver.findElement(By.id(xpathMap.get("clickOnMenuOnTopbar"))).click(); //
				// If size is > Zero then it will click on session continue button

				nativeClickByID(xpathMap.get("clickOnMenuOnTopbar")); // Nativate to click on
				System.out.println("Element found for Help on the Top Bar  ");
				log.debug("Element found for Help on the Top Bar ");

				// IOSElement btn = (IOSElement)new WebDriverWait(webdriver ,
				// 30).until(ExpectedConditions.elementToBeClickable(MobileBy.id("clickOnMenuOnTopbar")));
				// btn.click();

			} else {
				log.debug(" Element not found   for Help on the Top Bar ");
				System.out.print(" Element not found for Help on the Top Bar ");
			}

			String isHelp = webdriver.findElement(By.xpath(xpathMap.get("isHelpAvailable"))).getText();
			System.out.println(isHelp);

			if (isHelp.equals("Help")) {
				log.debug("Help  name is Same");
				System.out.println("Helpname is Same");
				isHelpAvailable = true;
			} else {
				log.error("Help is not available");
				System.out.println("Help is not available");
				isHelpAvailable = false;
			}
		} catch (Exception e) {
			log.error("Not able to verify Help Availability", e);
		}
		return isHelpAvailable;
	}

	public boolean fillStartSessionLossForm(String LossLimit, Mobile_HTML_Report report, String languageCode) {
		Wait = new WebDriverWait(webdriver, 90);
		boolean ret = false;
		try {
			// Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("spain_TimeLimit"))));

			List<WebElement> elementList = webdriver.findElements(By.xpath(xpathMap.get("spain_TimeLimit"))); 
			
			if (elementList.size() > 0)
			{

				WebElement timelimit = webdriver.findElement(By.xpath(xpathMap.get("spain_TimeLimit")));
				timelimit.click();
				report.detailsAppendFolder(" Spain Limit ", "Spain Limit is Present ", "Spain Limit is Present ","PASS", languageCode);
				Select dropdown1 = new Select(timelimit);
				dropdown1.selectByIndex(2);
				Thread.sleep(2000);
				report.detailsAppendFolder(" Spain Limit ", "Spain Limit is Selected ", "Spain Limit is Selected ","PASS", languageCode);
				System.out.println("Time Limit Selected ");
				log.debug("Time Limit Selected ");
			} 
			else 
			{
				System.out.println("Time Limit not  Selected ");
				log.debug("Time Limit not  Selected ");
				report.detailsAppendFolder(" Spain Limit ", "Spain Limit is Selected ", "Spain Limit is Selected ","FAIL", languageCode);
			}

			WebElement reminderpreiod = webdriver.findElement(By.xpath(xpathMap.get("spain_ReminderPeriod")));
			reminderpreiod.click();
			report.detailsAppendFolder("Spain Reminder Period ", "Spain Reminder Period is Present ","Spain Reminder Period is Present ", "PASS", languageCode);
			Select dropdown1 = new Select(reminderpreiod);
			dropdown1.selectByIndex(1);
			Thread.sleep(2000);
			report.detailsAppendFolder(" Spain Reminder Period ", "Spain Limit is Selected ",
					"Spain Limit is Selected ", "PASS", languageCode);
			System.out.println("Reminder Period selected d");

			WebElement lossvalue = webdriver.findElement(By.xpath(xpathMap.get("spain_LossLimit")));
			lossvalue.sendKeys(LossLimit);
			report.detailsAppendFolder(" Spain LossLimit ", "Spain LossLimit is Selected ","Spain LossLimit is Selected ", "PASS", languageCode);
			Thread.sleep(2000);
			System.out.println("Loss Limit is  " + LossLimit);
			Thread.sleep(2000);

			WebElement takeaway = webdriver.findElement(By.xpath(xpathMap.get("takwAway")));

			if (osPlatform.equalsIgnoreCase("Android")) 
			{
				takeaway.click(); // just Click for IOS

			}
			JavascriptExecutor js = (JavascriptExecutor) webdriver;
			js.executeScript("arguments[0].scrollIntoView();", takeaway); // scroll the window
			takeaway.click(); // just Click for Andriod

			report.detailsAppendFolder(" Spain Reminder Period ", " Spain Reminder Period is Present ",
					"Spain Reminder Period is Present ", "PASS", languageCode);
			Select dropdown3 = new Select(takeaway);
			dropdown3.selectByIndex(1);
			report.detailsAppendFolder("Spain  Reminder Period ", "Spain Reminder Period is Selected ",
					"Spain Reminder Period is Selected ", "PASS", languageCode);
			Thread.sleep(1000);
			// reportSpain.detailsAppendFolder("Verify Loss Limit ", "Loss Limit is Present
			// ", "Loss Limit is Present ", "PASS",languageCode);
			System.out.println(" Clicked  Take a break from slot");

			webdriver.findElement(By.xpath(xpathMap.get("spain_SetLimits"))).click();
			Thread.sleep(1000);
			System.out.println("Set Limits Clicked  successfully");
			report.detailsAppendFolder("Spain Set limit  ", "Spain Set Limit is clicked ", "Spain Set Limit is clicked",
					"PASS", languageCode);

			// funcFullScreen();
			ret = true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * This method checks whether quickspin available or not
	 * 
	 * @return
	 */
	public boolean isQuickspinAvailable() {
		return false;
	}

	public boolean waitUntilSessionReminder(String languageCode) {
		Wait = new WebDriverWait(webdriver, 300);

		boolean header = false;
		try {
			log.debug("Waiting for SessionReminder Continue overlay");

			Wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath(xpathMap.get("spain_SessionReminderContinue"))));
			System.out.println("Slot Game Limit Reminder is Present ");
			log.debug("Slot Game Limit Reminder is Present ");
			Thread.sleep(1000);
			header = true;
		} catch (Exception e) {

			e.printStackTrace();
		}
		return header;
	}

	public boolean waitUntilSessionLoss(String lossLimit) {
		return false;
	}

	public void closeSessionLossPopup(Mobile_HTML_Report reportSpain, String languageCode) {
		try {

			Thread.sleep(3000);

			WebElement close = webdriver.findElement(By.xpath(xpathMap.get("spain_CloseBtn")));
			close.click();
			Thread.sleep(3000);

			System.out.println("Cliked on close");
			log.debug(" Cliked on close");
			// reportSpain.detailsAppendFolder("To check if Loss Limit Summary overlay
			// appear", "Loss Limit Summary overlay should appear", "Loss Limit summary
			// overlay appears", "Pass",languageCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method checks whether stop available or not in Italy Market
	 * 
	 * @return
	 */
	public boolean isStopButtonAvailablity() {
		return false;
	}

	/*
	 * public void spainStartNewSession(Mobile_HTML_Report reportSpain,String
	 * languageCode) { try { boolean test =
	 * webdriver.findElements(By.xpath(xpathMap.get("spain_StartNewSession"))).size(
	 * ) > 0;
	 * 
	 * if (test) { //reportSpain.
	 * detailsAppendFolder("Verify that Start New Sessiosn Overlay appears before the game loads "
	 * ,"Start New Sessiosn Overlay should appear before the game loads"
	 * ,"Start New Sessiosn Overlay is appearing before the game loads",
	 * "Pass",languageCode); funcClick(xpathMap.get("spain_StartNewSession"));
	 * Thread.sleep(3000);
	 * 
	 * } else { System.out.println("Slot Game limit overlay appears"); } } catch
	 * (Exception e) { e.getMessage(); } }
	 */
	public boolean spainStartNewSession(Mobile_HTML_Report reportSpain, String languageCode) {
		boolean strtnewsession = false;
		try {

			// If the session reminder present It will click on continue
			List<WebElement> strtsession = webdriver.findElements(By.xpath(xpathMap.get("spain_StartNewSession")));

			// boolean ele =
			// Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("SessionContinue")))).isDisplayed();
			if (strtsession.size() > 0) {
				webdriver.findElement(By.xpath(xpathMap.get("spain_StartNewSession"))).click(); // If size is > Zero
																								// then it will click on
																								// session continue
																								// button
				log.debug("Start New seesion clicked ");
				System.out.print("Start New seesion clicked ");
				reportSpain.detailsAppendFolder("Verify Start new session present on the screen  ",
						"Start new session is present", "Start new session is present ", "PASS", languageCode);
				strtnewsession = true;
			}

			else {
				log.debug("Start New seesion is not present and unable to click ");
				System.out.print("Start New seesion is not present and unable to click ");
				reportSpain.detailsAppendFolder("Verify Start new session present on the screen  ",
						"Start new session is not present", "Start new session is not present ", "FAIL", languageCode);
			}
		}

		catch (Exception e) {
			log.error("Not able to verify session reminder status", e);
			System.out.print("Session reminder not found");
			strtnewsession = false;
		}
		return strtnewsession;
	}

	public boolean spain_checkStartNewSession(Mobile_HTML_Report reportSpain, String languageCode) {
		boolean strtnewsession = false;
		try {

			// If the session reminder present It will click on continue
			List<WebElement> strtsession = webdriver.findElements(By.xpath(xpathMap.get("spain_StartNewSession")));

			// boolean ele =
			// Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("SessionContinue")))).isDisplayed();
			if (strtsession.size() > 0) {
				System.out.print("Start New seesion is present ");
				strtnewsession = true;
			}

			else {
				log.debug("Start New seesion is not  present ");
				System.out.print("Start New seesion is not  present ");
				strtnewsession = false;
			}
		}

		catch (Exception e) {
			log.error("Not able to verify session reminder status", e);
			System.out.print("Session reminder not found");
			strtnewsession = false;
		}
		return strtnewsession;
	}

	/**
	 * To open Help
	 */
	public boolean openHelpFromTopbar(Mobile_HTML_Report report) {
		return false;
	}

	/**
	 * To open PlayerProtection from Topbar
	 */
	public boolean openPlayerProtectionFromTopbar(Mobile_HTML_Report report) {
		return false;
	}

	/**
	 * To verify Player Protection is present on topbat in uk market
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public boolean Verify_PlayerProtection(Mobile_HTML_Report report) throws InterruptedException {
		MobileElement element = (MobileElement) webdriver.findElement(By.xpath(xpathMap.get("clickOnMenuOnTopbar")));
		webdriver.executeScript("arguments[0].click();", element);
		report.detailsAppend("  Menu  ", "Menu Clicked ", "Menu Clicked", "PASS");
		// System.out.println("PlayerProtection is same");
		boolean playerprotection = false;

		try {
			String plyrprotection = webdriver.findElement(By.xpath(xpathMap.get("Denmark_PlayerProtection"))).getText();
			System.out.println(plyrprotection);
			if (plyrprotection.equals("Player Protection") || plyrprotection.equals("Spillerbeskyttelse")) {
				log.debug("PlayerProtection  is same");
				System.out.println("PlayerProtection  is same");
				playerprotection = true;
			} else {
				log.error("PlayerProtection is not  same");
				System.out.println("PlayerProtection is not  same");
				playerprotection = false;
			}
		} catch (Exception e) {
			report.detailsAppend(" Verify Menu  ", "Menu isn't Clicked ", "Menu  isn't Clicked", "FAIL");
			log.error("Not able to verify Player Protection Availability", e);
		}
		return playerprotection;

	}

	

	/**
	 * To verify Help from Menu
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public boolean Verify_Help(Mobile_HTML_Report report) throws InterruptedException {
		MobileElement element = (MobileElement) webdriver.findElement(By.xpath(xpathMap.get("clickOnMenuOnTopbar")));
		webdriver.executeScript("arguments[0].click();", element);
		report.detailsAppend("  Menu  ", "Menu Clicked ", "Menu Clicked", "PASS");
		boolean isHelpAvailable = false;
		try {
			String isHelp = webdriver.findElement(By.xpath(xpathMap.get("Italy_Help"))).getText();
			System.out.println(isHelp);

			if (isHelp.equals("Help")) {
				log.debug("Help  name is Same");
				System.out.println("Helpname is Same");
				isHelpAvailable = true;
			} else {
				log.error("Help is not available");
				System.out.println("Help is not available");
				isHelpAvailable = false;
			}
		} catch (Exception e) {
			log.error("Not able to verify Help Availability", e);
		}
		return isHelpAvailable;
	}

	/**
	 * To verify Responsiblegaming from Menu
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public boolean Verify_Responsiblegaming(Mobile_HTML_Report report) throws InterruptedException {
		MobileElement element = (MobileElement) webdriver.findElement(By.xpath(xpathMap.get("clickOnMenuOnTopbar")));
		webdriver.executeScript("arguments[0].click();", element);
		report.detailsAppend("  Menu  ", "Menu Clicked ", "Menu Clicked", "PASS");
		boolean isResponsiblegaming = false;
		try {
			String responsibleGaming = webdriver.findElement(By.xpath(xpathMap.get("Italy_ResponsibleGaming")))
					.getText();
			System.out.println(responsibleGaming);

			if (responsibleGaming.equals("Responsible Gaming")) {
				log.debug("Responsiblegaming  name is Same");
				System.out.println("Responsiblegaming is Same");
				isResponsiblegaming = true;
			} else {
				log.error("Responsiblegaming is not available");
				System.out.println("Responsiblegaming is not available");
				isResponsiblegaming = false;
			}
		} catch (Exception e) {
			log.error("Not able to verify Responsiblegaming Availability", e);
		}
		return isResponsiblegaming;
	}

	// reelspin duration
	public long reelSpinDuration() {
		return 0;

	}

	public boolean openResponsiblegamingFromTopbar(Mobile_HTML_Report report) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean currency_check(Mobile_HTML_Report report) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * Continue Button
	 */
	public void Continue() {
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("ClickonContinue"))));
		webdriver.findElement(By.id(xpathMap.get("ClickonContinue"))).click();
		log.debug("Clicked on Continue Button ");
		System.out.println("Clicked on Continue Button ");

	}

	/**
	 * This method is used to check whether session reminder is present or not
	 * 
	 * @return
	 */
	public boolean verifySessionReminderPresentDenmark(Mobile_HTML_Report report) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean verifyTopBarVisible() {
		// TODO Auto-generated method stub
		return false;
	}

	// game name on top bar
	public String gameNameOnTopBarForDenmark(Mobile_HTML_Report report) {
		String getGameName = null;
		try {
			boolean isGameNamePresent = webdriver.findElement(By.xpath(xpathMap.get("Denmark_GameName"))).isDisplayed();
			// System.out.println(isGameNamePresent);
			if (isGameNamePresent) {
				log.debug("Gamename is visible");
				System.out.println("Gamename is visible");

				getGameName = webdriver.findElement(By.xpath(xpathMap.get("Denmark_GameName"))).getText();

				System.out.println(getGameName);
				log.debug(getGameName);
				if (getGameName.equals("Reel Thunder")) {
					System.out.println("Game name is same");
				} else {
					System.out.println("Game name is different");
				}

			} else {
				System.out.println("Game name is not visible on top bar");
			}
		} catch (Exception e) {
			log.error("Not able to verify Game name", e);
		}
		return getGameName;

	}

	public String clockOnTheTopBar() {
		String clock = null;

		try {
			boolean isClockPresent = webdriver.findElement(By.xpath(xpathMap.get("Denmark_ClockonTheTopBar")))
					.isDisplayed();
			// System.out.println(isClockPresent);
			if (isClockPresent) {
				log.debug("clock is visible");
				System.out.println("clock is visible");
				clock = webdriver.findElement(By.xpath(xpathMap.get("Denmark_ClockonTheTopBar"))).getText();
				System.out.println("Time is :" + clock);
				log.debug("Time is :" + clock);

			} else {
				System.out.println("closck on thetop bar is not visible");
			}
		} catch (Exception e) {
			log.error("Not able to verify clock", e);
		}
		return clock;

	}

	public void refresh(Mobile_HTML_Report report) {
		

	}

	/**
	 * Verify Session Reminder is Present or not
	 */
	public boolean sessionReminderPresent(Mobile_HTML_Report report)
	{
		return false;
	}

	public boolean closeSymbol(Mobile_HTML_Report report) {
		
		return false;
	}

	/*
	 * To verify Bonus Reminder
	 */

	public void verifyBonusReminder(Mobile_HTML_Report report) {
		
	}

	/**
	 * To verify gamename on topbar
	 */
	public void gameNameOnTopBar(Mobile_HTML_Report report) {
		String getGameName = null;
		try {
			boolean isGameNamePresent = webdriver.findElement(By.xpath(xpathMap.get("isGamename"))).isDisplayed();
			System.out.println(isGameNamePresent);
			if (isGameNamePresent) {
				log.debug("Gamename is visible");
				System.out.println("Gamename is visible");
				getGameName = webdriver.findElement(By.xpath(xpathMap.get("isGamename"))).getText();
				System.out.println(getGameName);
				log.debug(getGameName);
				String name = xpathMap.get(("TopbarGameName"));
				System.out.println(name);
				Thread.sleep(2000);
				if (getGameName.equals(name)) {
					System.out.println("Game name is same");
					report.detailsAppend("Verify Gamename must be is displayed on Topbar",
							"Gamename should be is displayed on Topbar",
							"Gamename is displayed on Topbar and it is correct " + getGameName, "pass");
				} else {
					System.out.println("Game name is different");
					report.detailsAppend("Gamename must be is displayed on Topbar",
							"Gamename should be is displayed on Topbar",
							"Gamename is displayed on Topbar and in correct " + getGameName, "fail");
				}

			}
		} catch (Exception e) {
			log.error("Not able to verify Game name", e);
			System.out.println("Game name is not visible on top bar");
			report.detailsAppend("Gamename must be is displayed on Topbar", "Gamename should be is displayed on Topbar",
					"Gamename is not displayed on Topbar", "fail");

		}

	}

	/**
	 * To verify bet value on topbar
	 */
	public void betOnTopBar(Mobile_HTML_Report report) {
		String getBetValue = null;
		try {
			boolean isBet = webdriver.findElement(By.xpath(xpathMap.get("isBetOnTopbarVisible"))).isDisplayed();
			if (isBet) {
				log.debug("Bet on Top is visible");
				System.out.println("Bet on Top bar is visible");
				getBetValue = webdriver.findElement(By.xpath(xpathMap.get("isBetOnTopbarVisible"))).getText();
				System.out.println(getBetValue);
				String betValue = GetConsoleText("return " + xpathMap.get("BetButtonLabel"));
				System.out.println(betValue);
				Thread.sleep(2000);
				if (getBetValue.equalsIgnoreCase(betValue)) {
					System.out.println("Bet values are same");
					report.detailsAppend("Verify Bet value must be is displayed on Topbar",
							"Bet value should be is displayed on Topbar",
							"Bet value is displayed on Topbar and is correct " + getBetValue, "pass");
				} else {
					System.out.println("Bet values are different");
					report.detailsAppend("Verify Bet value must be is displayed on Topbar",
							"Bet value should be is displayed on Topbar",
							"Bet value is displayed on Topbar and incorrect " + getBetValue, "fail");

				}
			}
		}

		catch (Exception e) {
			log.error("Not able to verify bet", e);
			System.out.println("Bet value is not visible on top bar");
			report.detailsAppend("Bet value must be is displayed on Topbar",
					"Bet value should be is displayed on Topbar", "Bet value is not displayed on Topbar", "fail");

		}
	}

	/**
	 * To verify clock on topbar
	 */
	public void clockOnTopBar(Mobile_HTML_Report report) {
		String clock = null;

		try {
			boolean isClockPresent = webdriver.findElement(By.xpath(xpathMap.get("isClockVisible"))).isDisplayed();
			Thread.sleep(2000);
			if (isClockPresent) {
				log.debug("clock is visible");
				System.out.println("clock is visible");
				clock = webdriver.findElement(By.xpath(xpathMap.get("isClockVisible"))).getText();
				System.out.println(clock);
				report.detailsAppend("Verify Clock must be is displayed on Topbar",
						"Clock should be is displayed on Topbar", "Clock is displayed on Topbar", "pass");
			}
		}

		catch (Exception e) {
			log.error("Not able to verify clock", e);
			System.out.println("clock is not visible on top bar");
			report.detailsAppend("Clock must be is displayed on Topbar", "Clock should be is displayed on Topbar",
					"Clock is not displayed on Topbar", "fail");

		}
	}

	/**
	 * To verfiy net postion
	 * 
	 * @return
	 */
	public boolean verifyNetPosition(Mobile_HTML_Report reportUK) {
		return false;
	}

	/**
	 * To verify Menu navigations from topbar in uk
	 * 
	 * @return
	 */
	public void verifyMenuOptionNavigationsForUK(Mobile_HTML_Report report) {
	}

	/**
	 * To verify Responsible Gaming is present on topbar and navigate to it
	 * 
	 * @return
	 */
	public void verifyResposibleGamingOnTopbar(Mobile_HTML_Report report) {
	}

	/**
	 * To verify Help is present on topbar and navigate to it
	 * 
	 * @return
	 */
	public void verifyHelpOnTopbar(Mobile_HTML_Report report) {
	}

	/**
	 * To verify Transaction History is present on topbar and navigate to it
	 * 
	 * @return
	 */
	public void verifyTransactionHistoryOnTopbar(Mobile_HTML_Report report) {
	}

	/**
	 * To verify Game History is present on topbar and navigate to it
	 * 
	 * @return
	 */
	public void verifyGameHistoryOnTopbar(Mobile_HTML_Report report) {
	}

	/**
	 * To verify Player Protection is present on topbar and navigate to it
	 * 
	 * @return
	 */
	public void verifyPlayerProtectionOnTopbar(Mobile_HTML_Report report) {
	}

	/**
	 * To click on Menu on topbar
	 * 
	 * @return
	 */
	public boolean clickOnMenuOnTopbar() {
		boolean ret = false;
		try {
			webdriver.findElement(By.xpath(xpathMap.get("clickOnMenuOnTopbar"))).click();
			log.debug("Clicked on menu button on topbar to open");
			ret = true;
		} catch (Exception e) {
			log.error("Error in opening menu", e);
		}
		return ret;
	}

	/**
	 * Date: 04/06/2018 Author: Havish Jain Description: This function is used for
	 * touch event in mobile devices Parameter: By locator
	 */

	public boolean Native_ClickByXpath(String locator) {
		boolean present = false;
		try 
		{
			//String context = webdriver.getContext();
      		Wait.until(ExpectedConditions.elementToBeClickable(By.xpath(locator)));
			WebElement element =  webdriver.findElement(By.xpath(locator));

			if (element != null) 
			{
				if (osPlatform.equalsIgnoreCase("Android")) 
				{
					clickWithWebElementAndroid(webdriver, element, 0);
					System.out.println("Element Found and Clicked");log.debug("Element Found and Clicked");
				}
				else
				{
                    System.out.println("Element   Found");
					log.debug("Element Found");
					clickWithWebElement(element, 0);
				}

				present = true;
			}
			//webdriver.context(context);
		} 
		catch (NoSuchElementException e) 
		{
			System.out.println(e.getMessage());
			System.out.println("Unable to Navigate to the clicked Page");
			log.debug("Unable to Navigate to the clicked Page");
			present = false;
		}
		return present;
	}

	/*
	 * Verify Italy Currency
	 */
	public boolean italyCurrencyCheck(Mobile_HTML_Report report) {
		return false;

	}

	/*
	 * Verify Italy Currency
	 */
	public boolean spainCurrencyCheck(Mobile_HTML_Report report) {
		return false;

	}

	/**
	 * Verify the player protrection Icon From Top bar
	 */
	public boolean playerProtectionIcon(Mobile_HTML_Report report) 
	{
		return false;
		
		
	}
	/**
	 * method is for click , navigate and back to game screen for Italy Market using Xpath (For Android & IOS click action is same)
	 */
	public boolean italyClickAndNavigate(Mobile_HTML_Report report , String locator) 
	{
		return false;

	}

	/**
	 * Verify Italy check page navigation for Menu check
	 */
	public void italyCheckPageNavigation(Mobile_HTML_Report report, String gameurl) 
	{
		
	}

	/**
	 * Verify ResponsibleGaming From TopBar
	 */
	public boolean italyResponsibleGamingFromMenu(Mobile_HTML_Report report) throws InterruptedException {

		MobileElement element = (MobileElement) webdriver.findElement(By.xpath(xpathMap.get("clickOnMenuOnTopbar")));
		webdriver.executeScript("arguments[0].click();", element);
		Thread.sleep(3000);
		report.detailsAppend(" Verify Menu  ", "Menu Clicked ", "Menu Clicked", "PASS");
		boolean isResponsiblegaming = false;
		try {
			String responsibleGaming = webdriver.findElement(By.xpath(xpathMap.get("Italy_ResponsibleGaming")))
					.getText();
			System.out.println(responsibleGaming);

			if (responsibleGaming.equals("Responsible Gaming") || responsibleGaming.equals("Gioco Responsabile")) {
				log.debug("Responsiblegaming  name is Same");
				System.out.println("Responsiblegaming is Same");
				isResponsiblegaming = true;
			} else {
				log.error("Responsiblegaming is not available");
				System.out.println("Responsiblegaming is not available");
				isResponsiblegaming = false;
			}
		} catch (Exception e) {
			log.error("Not able to verify Responsiblegaming Availability", e);
		}
		return isResponsiblegaming;

	}

	/*
	 * Click and navigate through Responsible gaming from Menu
	 */
	public boolean italyOpenResponsibleGamingFromMenu(Mobile_HTML_Report report) {
		boolean ret = false;
		Wait = new WebDriverWait(webdriver, 500);
		try {
			String gameurl = webdriver.getCurrentUrl();
			if (osPlatform.equalsIgnoreCase("Android")) {
				WebElement element1 = webdriver.findElement(By.xpath(xpathMap.get("Italy_ResponsibleGaming")));
				element1.click();
				Thread.sleep(2000);
				report.detailsAppend("ResponsibleGaming from Menu ", "ResponsibleGaming Clicked  ",
						"ResponsibleGaming Clicked", "PASS");
				Thread.sleep(3000);
				log.debug("clicked on Responsible");
				System.out.println("clicked on Responsible");
				Thread.sleep(3000);
				funcFullScreen();

			} else {
				MobileElement element = (MobileElement) webdriver
						.findElement(By.xpath(xpathMap.get("Italy_ResponsibleGaming")));
				webdriver.executeScript("arguments[0].click();", element);
				Thread.sleep(2000);
				report.detailsAppend("ResponsibleGaming from Menu", "ResponsibleGaming Clicked ",
						"ResponsibleGaming Clicked", "PASS");
				italyCheckPageNavigation(report, gameurl);
				Thread.sleep(3000);
				log.debug("clicked on Responsible");
				System.out.println("clicked on ResponsibleGameing and Back to base Game ");
				Thread.sleep(3000);
				Thread.sleep(2000);

			}
			ret = true;

		} catch (Exception e) {
			report.detailsAppend("ResponsibleGaming from Menu", "ResponsibleGaming Clicked ",
					"ResponsibleGaming Clicked", "FAIL");
			log.error("Error in navigetion to ResponsibleGameing  page ", e);
		}
		return ret;
	}

	/*
	 * Check Italy Player protection from Menu
	 */
	public boolean italyPlayerProtectionFromMenu(Mobile_HTML_Report report) throws InterruptedException {
		MobileElement element = (MobileElement) webdriver.findElement(By.xpath(xpathMap.get("clickOnMenuOnTopbar")));
		webdriver.executeScript("arguments[0].click();", element);
		Thread.sleep(3000);
		boolean playerprotection = false;
		try {
			String plyrprotection = webdriver.findElement(By.xpath(xpathMap.get("Italy_PlayerProtection"))).getText();
			System.out.println(plyrprotection);
			if (plyrprotection.equals("Player Protection") || plyrprotection.equals("Tutela del giocatore")) {
				log.debug("PlayerProtection  is same");
				System.out.println("PlayerProtection  is same");
				playerprotection = true;
			} else {
				log.error("PlayerProtection is not  same");
				System.out.println("PlayerProtection is not  same");
				playerprotection = false;
			}
		} catch (Exception e) {
			report.detailsAppend(" Verify Menu  ", "Menu isn't Clicked ", "Menu  isn't Clicked", "FAIL");
			log.error("Not able to verify Player Protection Availability", e);
		}
		return playerprotection;
	}

	/*
	 * Italy Responsible gaming name comparisiions and visibility check
	 */
	public boolean italyOpenPlayerProtectionFromMenu(Mobile_HTML_Report report) {
		boolean ret = false;
		Wait = new WebDriverWait(webdriver, 500);
		try {
			String gameurl = webdriver.getCurrentUrl();
			MobileElement element = (MobileElement) webdriver
					.findElement(By.xpath(xpathMap.get("Italy_PlayerProtection")));
			webdriver.executeScript("arguments[0].click();", element);
			Thread.sleep(4000);
			report.detailsAppend("  PlayerProtection from Menu ", "PlayerProtection Clicked ",
					"PlayerProtection Clicked", "PASS");
			Thread.sleep(2000);
			log.debug("clicked on Player Protection");
			System.out.println("clicked on Player Protection");

			italyCheckPageNavigation(report, gameurl);
			System.out.println("Page navigated to Back to Game ");
			log.debug("Page navigated to Back to Game");
			ret = true;
		} catch (Exception e) {
			log.error("Error in navigetion to help page ", e);
			report.detailsAppend("PlayerProtection from Menu  ", "PlayerProtection isn't  Clicked ",
					"PlayerProtection isn't Clicked", "FAIL");
		}
		return ret;
	}

	/**
	 * Italy Help from menu text comparison and visibility check
	 */
	public boolean italyHelpFromTopBarMenu(Mobile_HTML_Report report) throws InterruptedException 
	{
		return false;
		
	}

	/*
	 * open help from menu for italy market
	 */

	public boolean italyOpenHelpFromMenu(Mobile_HTML_Report report) {

		boolean ret = false;
		Wait = new WebDriverWait(webdriver, 90);
		try {
			String gameurl = webdriver.getCurrentUrl();

			MobileElement element = (MobileElement) webdriver.findElement(By.xpath(xpathMap.get("Italy_Help")));
			webdriver.executeScript("arguments[0].click();", element);
			// report.detailsAppend(" Help from Menu ", "Menu Clicked ","Menu Clicked","PASS");
			Thread.sleep(2000);
			log.debug("clicked on Help");
			System.out.println("clicked on Help");
			if (osPlatform.equalsIgnoreCase("Android")) {
				italyAndriodHelp_CheckPageNavigation(report, gameurl); // Checkpage Navigation

			} else {

				italyCheckPageNavigation(report, gameurl);
			}

			log.debug(" Back to the Game Screen from Help");
			System.out.println(" Back to the Game Screen from Help");
			Thread.sleep(2000);

			ret = true;
		} catch (Exception e) {
			report.detailsAppend(" Help from Menu  ", "Menu isn't Clicked ", "Menu isn't Clicked", "FAIL");
			log.error("Error in navigetion to help page ", e);
		}
		return ret;

	}

	/**
	 * This is for italy Andriod Help from Menu to PageNavigation
	 */
	public void italyAndriodHelp_CheckPageNavigation(Mobile_HTML_Report report, String gameurl) 
	{

	}

	/**
	 * verify QuickSpin Availablity
	 * 
	 */
	public boolean verifyQuickSpinAvailablity()
	{
		boolean isQuickSpin = false;
		try {

			boolean isQuickspinVisible = getConsoleBooleanText("return " + xpathMap.get("QuickSpinBtnAvailable"));
			if (isQuickspinVisible)
			{
				System.out.print("Quickspin is available");
				log.debug("Quickspin is available");
				isQuickSpin =true;
			}
			else
			{
				System.out.print("Quickspin is not available");
				log.error("Quickspin is not available");
				isQuickSpin =false;
			}
		} catch (Exception e) 
		{
			log.error("Not able to verify quickspin Availability", e);
		}
		return isQuickSpin;
	}

	/**
	 * verify StopButton Availability
	 */
	public boolean verifyStopButtonAvailablity()
	{
		boolean isstopbutton = true;
		try 
		{
	    clickAtButton("return " + xpathMap.get("ClickSpinBtn"));
			// To verify the stop button
			String stopbutton = GetConsoleText("return " + xpathMap.get("SpinBtnCurrState"));

			if (stopbutton.equalsIgnoreCase("activeSecondary") || !(stopbutton.equalsIgnoreCase("disabled")))
			{
				// As current state of button is ActiveSecondary
				log.debug("Stop button is available");
				System.out.println("Stop button is available");
				isstopbutton = true;
			} 
			else 
			{
				log.error("Stop button is not available");
				System.out.println("Stop button is not available");
				isstopbutton = false;
			}
		} 
		catch (Exception e) 
		{
			log.debug(e.getMessage());
		}

		return isstopbutton;
	}


	/**
	 * Verify Reel Spin Duration 
	 */
	public long reelSpinDuratioN(Mobile_HTML_Report report)
	{
		long avgspinduration = 0;
		return avgspinduration;
	}

	/*
	 * Verify denmark Currency Check
	 * 
	 */
	public boolean denmarkCurrencyCheck(Mobile_HTML_Report report) {

		return false;
	}

	/**
	 * Denmark Help comparison from Menu, click and navigation
	 */

	public boolean denmarkHelpFromTopBarMenu(Mobile_HTML_Report report) 
	{
		return false;			
	}
	/**
	 * spain Help comparission from Menu
	 */

	public boolean spainHelpFromTopBarMenu(Mobile_HTML_Report report) 
	{
		return false;			
	}
	

	/**
	 * Denmark PlayerProtection comparison from Menu, click and navigation
	 */
	public boolean denmarkPlayerProtectionFromMenu(Mobile_HTML_Report report)
	{
		return false;
				
	}


	/*
	 * Verify cHeckPageNavigation - It is for the games which doesn't / does have
	 * continue button on Loading screen
	 */
	public void cHeckPageNavigation(Mobile_HTML_Report report, String gameurl) {
		try {
			String mainwindow = webdriver.getWindowHandle();

			Set<String> s1 = webdriver.getWindowHandles();
			if (s1.size() > 1) {
				Iterator<String> i1 = s1.iterator();
				while (i1.hasNext()) {
					String ChildWindow = i1.next();
					if (mainwindow.equalsIgnoreCase(ChildWindow)) 
					{
						// ChildWindow=i1.next();
						report.detailsAppend("verify the Navigation screen shot ", " Navigation page screen shot","Navigation page screenshot ", "PASS");
						Thread.sleep(1000);
						webdriver.switchTo().window(ChildWindow);
						Thread.sleep(2000);
						String url = webdriver.getCurrentUrl();
						log.debug("Reload URL is :: " + url);
						if (!url.equalsIgnoreCase(gameurl)) 
						{
							// pass condition for navigation
							report.detailsAppend("verify the Navigation screen shot ", " Navigation page screen shot","Navigation page screenshot ", "PASS");
							log.debug("Page navigated succesfully");
							//System.out.println("Page navigated succesfully");
							webdriver.close();
						} else {
							log.debug("Now On game page");
						}
					}
				}
				webdriver.switchTo().window(mainwindow);
			} else {
				String url = webdriver.getCurrentUrl();
				//System.out.println("Reloaded URL is :: " + url);
				log.debug("Reloaded URL is :: " + url);
				if (!url.equalsIgnoreCase(gameurl)) {
					// pass condition for navigation
					Thread.sleep(2000);
					report.detailsAppend("verify the Navigation screen shot", " Navigation page screen shot","Navigation page screenshot ", "PASS");
					// Thread.sleep(2000);
					//System.out.println("Page navigated succesfully");
					log.debug("Page navigated succesfully");
					webdriver.navigate().to(gameurl);
					Thread.sleep(2000);
					waitForSpinButton();
					if ((Constant.YES.equalsIgnoreCase(xpathMap.get("continueBtnOnGameLoad"))))
					{
						// Continue();
						newFeature();
					}

				}
				else
				{
					report.detailsAppend("verify the Navigation screen shot", " Navigation page screen shot","Navigation page screenshot ", "PASS");
					webdriver.navigate().to(gameurl);
					log.debug(gameurl);
					Thread.sleep(2000);
					// System.out.println(gameurl);
					waitForSpinButton();

					log.debug("Now On game page");
				}
			}
			// webdriver.navigate().to(gameurl);
		} catch (Exception e)
		{
			log.error("error in navigation of page");
		}
	}

	/**
	 * Spain cooing of period
	 */
	public void spainCoolingOffPeriod(Mobile_HTML_Report report) {
		// TODO Auto-generated method stub

	}

	/**
	 * To verify Help text link on top bar
	 */
	public boolean helpTextLink(Mobile_HTML_Report report)
	{
		return false;
		
	}

	/**
	 * verify HelpText link for spain
	 */
	public void verifyHelpTextlink(Mobile_HTML_Report report)
	{
		boolean isHelpTextNaviagted = false;
		try {
			String gameurl = webdriver.getCurrentUrl();

			MobileElement element = (MobileElement) webdriver.findElement(By.xpath(xpathMap.get("ishelpsymbol_Icon")));
			webdriver.executeScript("arguments[0].click();", element);
			report.detailsAppend("Help Text ", "Help Text is clicked", "Help Text is clicked", "PASS");
			System.out.println("Clicked on Help Icon");
			Thread.sleep(3000);
			// italy_Checkpagenavigation(report, gameurl);
			cHeckPageNavigation(report, gameurl); // Checkpage Navigation
			isHelpTextNaviagted = true;
			Thread.sleep(2000);
			if (isHelpTextNaviagted) {
				System.out.println("Game navigated from Help to Game screen");
				log.debug("Help  text link navigation verified succesfully");
				funcFullScreen();
				newFeature();
				report.detailsAppend(" Navigation", "Navigation from  Help screen to Game",
						"Navigation from  Help screen to Game", "PASS");
			} else {
				report.detailsAppend(" Navigation", "Navigation from  Help screen to Game",
						"Navigation from  Help screen to Game", "FAIL");
			}

		} catch (Exception e) {
			log.error("Error in checking help page ", e);
		}

	}

	/*
	 * verify MenuOption Navigations ForSpain
	 */
	public void verifyMenuOptionNavigationsForSpain(Mobile_HTML_Report report) {
		try {
			// To verify help
			verifyHelpOnTopbar(report);
			Thread.sleep(2000);
		} catch (Exception e) {
			log.error("Error in checking help navigation from top bar ", e);

		}

	}

	
/**
*  help text comparison from Menu, click and navigation
*/
	public boolean helpIcon(Mobile_HTML_Report report)
	{
		return false;

	}

	/**
	 * To verify Session Duration on topbar in sweden market
	 */

	public String sessionDurationFromTopBar() {
		String sessionduration = null;
		try {
			boolean issessionduration = webdriver.findElement(By.xpath(xpathMap.get("Swden-SessionDuation_OnTopBar")))
					.isDisplayed();
			// System.out.println(isClockPresent);
			if (issessionduration) {
				log.debug("sessionDuration is visible");
				sessionduration = webdriver.findElement(By.xpath(xpathMap.get("Swden-SessionDuation_OnTopBar"))).getText();
				log.debug("Session Duration is :" + sessionduration);

			} else {
				log.debug("sessionDuration on thetop bar is not visible");
			}
		} catch (Exception e) {
			log.error("Not able to verify sessionDuration", e);
		}
		return sessionduration;

	}

	/*
	 * //To Compare consolebalance on the Top Bar
	 */
	public String creditComparissionfromFromTopBar() {
		String balance = null;
		String topbarconsolebalance = null;

		if (!GameName.contains("Scratch")) {
			if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame")))// force console
			{
				balance = "return " + xpathMap.get("Balancetext");
			} else
				balance = "return " + xpathMap.get("InfoBarBalanceTxt");
		}

		String consoleBalance = GetConsoleText(balance);
        log.debug("Console Credits are " + consoleBalance);
		String bal1 = consoleBalance.replace("CREDITS: ", "");
		String bal2 = consoleBalance.replace("KREDITER: ", "");
		// System.out.println("Console Credits are "+bal); System.out.println("Console
		// Credits are "+bal1);

		// now get the Top bar console balance
		try {

			boolean istopbarconsolebalance = webdriver
					.findElement(By.xpath(xpathMap.get("Sweden_ConosoleBalance_From_TopBar"))).isDisplayed();
			// System.out.println(isClockPresent);
			if (istopbarconsolebalance) {
				log.debug("Balance from the Top Bar is visible");
				topbarconsolebalance = webdriver.findElement(By.xpath(xpathMap.get("Sweden_ConosoleBalance_From_TopBar"))).getText();
				log.debug("Balance from Top Bar is  :" + topbarconsolebalance);
				String topbarBal = topbarconsolebalance.replace("Balance: ", "");
				String topbarBa2 = topbarconsolebalance.replace("Saldo: ", "");
				// System.out.println("TopBar Balance is:"+topbarBal);System.out.println("TopBar
				// Balance is:"+topbarBa2);

				if (bal1.equals(topbarBal) || bal2.equals(topbarBa2)) {
					log.debug("Credits are Same");
				} else {
					log.debug("Credits are Different");
				}

			} else {
				log.debug("Balance on thetop bar is not visible");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return consoleBalance;
	}

	/**
	 * betComparissionFromTopBar
	 */
	public String betComparissionFromTopBar() {
		String Bet = null;
		String Betfromtopbar = null;

		if (!GameName.contains("Scratch")) {
			if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame")))// force console
			{
				Bet = "return " + xpathMap.get("InfobarBettext");
			} else
				Bet = "return " + xpathMap.get("BetBalanceTxt");
		}

		String consoleBet = GetConsoleText(Bet);
        log.debug("Console Bet is " + consoleBet);
		String betconsole = consoleBet.replace("BET: ", "");
		String betconsole1 = consoleBet.replace("INSATS: ", "");
		// System.out.println("Console Bet is "+betconsole); System.out.println("Console
		// Bet is "+betconsole1);

		try {
			boolean isBetfromtopbar = webdriver.findElement(By.xpath(xpathMap.get("Swden_Bet_From_TopBar"))).isDisplayed();
			// System.out.println(isClockPresent);
			if (isBetfromtopbar) {
				log.debug("Bet on Top Bar is visible ");
				Betfromtopbar = webdriver.findElement(By.xpath(xpathMap.get("Swden_Bet_From_TopBar"))).getText();
				log.debug("Bet from Top Bar is " + Betfromtopbar);
				String TopBarBet = Betfromtopbar.replace("Bet: ", "");
				String TopBarBet1 = Betfromtopbar.replace("Insats: ", "");
				// System.out.println("Console Bet is "+TopBarBet); System.out.println("Console
				// Bet is "+TopBarBet1);

				if (betconsole.equals(TopBarBet) || betconsole1.equals(TopBarBet1)) {
					log.debug("Bet Values are Same");
				}

				else {
					log.debug("Bet Values are Different");
				}

			} else {
				log.debug("Bet on thetop bar is not visible");
			}
		} catch (Exception e) {
			log.error("Not able to verify Bet", e);
		}
		return Bet;

	}

	/*
	 * /Verify Sweden Seesion Reminder
	 */
	public boolean swedenSessionReminderAvailability(Mobile_HTML_Report report) {

		String SessionReminderPresent = null;
		try {
			boolean isSessionReminderPresent = webdriver
					.findElement(By.xpath(xpathMap.get("Sweden_SessionReminder_Continue"))).isDisplayed();
			if (isSessionReminderPresent) {
				log.debug("clock is visible");
				System.out.println("clock is visible");
				MobileElement element = (MobileElement) webdriver
						.findElement(By.xpath(xpathMap.get("Sweden_SessionReminder_Continue")));
				webdriver.executeScript("arguments[0].click();", element);

			} else {
				System.out.println("SessionReminder on thetop bar is not visible");
			}
		} catch (Exception e) {
			log.error("Not able to verify SessionReminder", e);
		}
		return SessionReminderPresent != null;
	}

	/**
	 * method is for click , navigate and back to game screen using Xpath (For Android & IOS click action is different)
	 */
	public boolean clickAndNavigate(Mobile_HTML_Report report , String locator) 
	{
		return false;
	}
	/**
	 * method is for click , navigate and back to game screen using Xpath (For Android & IOS click action is same)
	 */
	public boolean clickandNavigate(Mobile_HTML_Report report , String locator) 
	{
		return false;
	}
	
	/**
	 * Sweden three log Symbols from Top Bar
	 */

	public boolean swedenLogosFromTopBar(Mobile_HTML_Report report )
	{
		return false;

	}

	/**
	 * Click and get the Text 
	 */
	public String clickAndGetText(Mobile_HTML_Report report, String menuclick ,String locator) 
	{
		return null;
		
		
	}
	/**
	 * Sweden Help text comparison from Menu , click and Navigate 
	 * @throws InterruptedException 
	 */
	public boolean swedenTopBarMenu(Mobile_HTML_Report report) throws InterruptedException 
	{
		return false;
		
	}
	

	/**
	 * Sewden Currency Checks
	 */
	public boolean swedenCurrencyCheck(Mobile_HTML_Report report) 
	{
		return false;

	}

	

	// verify win or loss for Stop Button
	public Double winORLossForStopButton(Mobile_HTML_Report report)
	{
		
		return null;
	}

	// verify win or loss for Reel Spin Duration
	public Double winORLossForReelSpinDuration(Mobile_HTML_Report report) 
	{
		
		return null;
	}

	// verify session Reminder Win or Loss
	public void sessionReminderWinOrLoss(Mobile_HTML_Report sweden, Double valueAfterSpin1, Double valueAfterSpin2)
	{
	
	}

	/**
	 * verify session reminder is visible or not
	 * 
	 */
	public boolean isSessionReminderPresent() 
	{
		return false;
	}

	/**
	 * Verify Germany Currency Format
	 */
	public boolean germanyCurrencyCheck(Mobile_HTML_Report report)
	{
		
		return false;
	}

	/**
	 * verify Autoplay Availabilty
	 */
	public boolean verifyAutoplayAvailabilty() 
	{
		return false;
	}

	
/**
* germay help text comparison from Menu, click and navigation
*/
	public boolean germanyHelpFromTopBarMenu(Mobile_HTML_Report report) throws InterruptedException 
	{
		return false;
	}

	
	/**
	* germay ResponsibleGaming text comparison from Menu, click and navigation
	*/
	public boolean germanyResponsibleGamingFromTopBarMenu(Mobile_HTML_Report report) throws InterruptedException 
	{
			return false;
	}

	
/**
* germay GameHistory text comparison from Menu, click and navigation
*/
public boolean germanyGameHistoryFromTopBarMenu(Mobile_HTML_Report report) throws InterruptedException
{
	return false;
}	
	
/**
* Malta GameHistory text comparison from Menu, click and navigation
*/
public boolean maltaGameHistoryFromTopBarMenu(Mobile_HTML_Report report) throws InterruptedException
{
	return false;
}

/**
* Malta help text comparison from Menu, click and navigation
*/
public boolean maltaHelpFromTopBarMenu(Mobile_HTML_Report report) throws InterruptedException 
{
	return false;
}

/**
 ** Malta ResponsibleGaming text comparison from Menu, click and navigation
*/
public boolean maltaResponsibleGamingFromTopBarMenu(Mobile_HTML_Report report) throws InterruptedException 
{
	return false;
}

/**
 * Verify malta Currency Format
 */
public boolean maltaCurrencyCheck(Mobile_HTML_Report report)
{
	
	return false;
}
/**
 *  verify & Compare portugal Currency
 */
public boolean portugalCurrencyCheck(Mobile_HTML_Report report) 
{
	return false;
}
/**
 *Verifies Portugal  help text comparison , click and its navigation from menu
 */
public boolean portugalHelpFromTopBarMenu(Mobile_HTML_Report report) throws InterruptedException
{
	return false;

}
/**
 *Verifies Portugal GameHistory text comparison , click and its navigation from menu
 */
public boolean portugalGameHistoryFromTopBarMenu(Mobile_HTML_Report report) throws InterruptedException
{
	return false;
}
/**
 *Verifies Romania  help text comparison , click and its navigation from menu
 */
public boolean romaniaHelpFromTopBarMenu(Mobile_HTML_Report report) throws InterruptedException
{
	return false;

}
/**
 *Verifies Romania GameHistory text comparison , click and its navigation from menu
 */
public boolean romaniaGameHistoryFromTopBarMenu(Mobile_HTML_Report report) throws InterruptedException
{
	return false;
}
/**
 *  verify & Compare romania Currency
 */
public boolean romaniaCurrencyCheck(Mobile_HTML_Report report) 
{
	return false;
}

	public boolean swedenHelpFromMenu1(Mobile_HTML_Report sweden) throws InterruptedException
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean check(Mobile_HTML_Report sweden,String amount) throws InterruptedException
	{
		// TODO Auto-generated method stub
		return false;
	}
	/**
	 * Verifies the Currency Format - using String method 
	 */

	public boolean verifyRegularExpression(Mobile_HTML_Report report, String regExp, String method)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * verifies mini bet
	 * @return
	 */
	public String moveCoinSizeSliderToMinBet(Mobile_HTML_Report report,String CurrencyName) 
	{
		return null;
	}
	/**
	 * Verifies the current credits
	 * 
	 */
	public String getCurrentCredit(Mobile_HTML_Report report,String CurrencyName) 
	{
		return null;
		
	}
	
	/**
	 * Verifies the current Bet
	 * 
	 */
	public String getCurrentBetAmt(Mobile_HTML_Report report,String CurrencyName) 
	{
		return null;
		
	}
	/**
	 * method is used to scroll the paytable
	 * @param lvcReport
	 * @return
	 */
		public boolean paytableScroll(Mobile_HTML_Report report,String CurrencyName)
		{
			// TODO Auto-generated method stub
			return false;
		}
		
		
		/**
		 * Verifies the Currency Format - using String method 
		 */

		public boolean verifyRegularExpressionUsingArrays(Mobile_HTML_Report report, String regExp, String[] method)
		{
			// TODO Auto-generated method stub
			return false;
		}
		/**
		 * method is used to validate the Paytable Values 
		 * @return
		 */
		public String[] validatePayoutsFromPaytable(Mobile_HTML_Report report, String CurrencyName) //String[] array
		{
			return null;
			
		}
		/**
		 * method is to get the text using by ID .
		 */
		public String func_click(String locator) 
		{
			return null;
		}
		/**
		 * Verifies the Autoplay
		 * 
		 */

		public boolean isAutoplayAvailable() 
		{
			// TODO Auto-generated method stub
			return false;
		}
		/**
		 * Verifies the current Win
		 * 
		 */
		public String getCurrentWinAmt(Mobile_HTML_Report report,String CurrencyName) 
		{
			return null;
			
		}
		
		/**
		 * Verifies the Scatter Win
		 * 
		 */
		public String getScatterAmt(Mobile_HTML_Report report,String CurrencyName) 
		{
			return null;
			
		}
		/**
		 * Check Availablity of an Element
		 * @param string
		 * @return
		 */
		public boolean checkAvilabilityofElement(String string) {
			// TODO Auto-generated method stub
			return false;
		}
		/**
		 * Verifies the Big Win
		 * 
		 */
		public String verifyBigWin(Mobile_HTML_Report report,String CurrencyName) 
		{
			return null;
			
		}
		/**
		 * Verify Bonus Feature by clicking and get text 
		 * @param report
		 * @return
		 */

		public String[] bonusFeatureClickandGetText(Mobile_HTML_Report report,String CurrencyName) 
		{
		
			return null;
		}
		/**
		 * method verifies the bonus summary screen and get Text	
		 */
		public String bonusSummaryScreen(Mobile_HTML_Report report,String CurrencyName)
		{
			return null;
		}
		/**
		 * Get the text by using attribute
		 * @param report
		 * @param locator
		 * @return
		 */
		public String func_GetTextbyAttribute(Mobile_HTML_Report report ,String locator,String CurrencyName) 
		{
			return null;
		}
		/**
		 * Verifies the current tOtal wins in FS
		 * 
		 */

		public String getCurrentTotalWinINFS(Mobile_HTML_Report report ,String CurrencyName) 
		{
			// TODO Auto-generated method stub
			return null;
		}
		/**
		 * method verifies the bonus summary screen and get Text	
		 */
		public String freeSpinsSummaryScreen(Mobile_HTML_Report report ,String CurrencyName)
		{
			// TODO Auto-generated method stub
			return null;
		}
		/**
		 * This method retuns max bet value
		 */
			public String moveCoinSizeSliderToMaxBet(Mobile_HTML_Report report,String CurrencyName)
			{
				return null;
			}

	public boolean verifyWinAmtCurrencyFormat(String currencyFormat, Mobile_HTML_Report currencyReport) {
		// TODO Auto-generated method stub
		return false;
	}

	public void jackpotSummaryWinCurrFormat(String currencyFormat, Mobile_HTML_Report currencyReport,
			String currencyName) {

	}


	public void payoutverificationforBetLVC(Mobile_HTML_Report currencyReport, String regExpression,String currencyName) {
		// TODO Auto-generated method stub
		
	}

	public void freeGameSummaryWinCurrFormat(String currencyFormat, Mobile_HTML_Report currencyReport,
			String currencyName) {
		// TODO Auto-generated method stub
	}

	public boolean assignFreeGames(String userName,String offerExpirationUtcDate,int mid, int cid,int languageCnt,int defaultNoOfFreeGames) {
		// TODO Auto-generated method stub
		return false;
	}

	public String getWinAmtFromFreegames() {
		return GameName;
		// TODO Auto-generated method stub
	}

	public boolean verifyCurrencyFormatForCredits(String regExpression) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean betCurrencySymbolForLVC(String regExpression) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean verifyWinAmtCurrencyFormat(String regExpression) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean verifyBigWinCurrencyFormatForLVC(String regExpression, Mobile_HTML_Report currencyReport,
			String currencyName) {
		// TODO Auto-generated method stub
		return false;
	}

	public void jackpotSummaryWinCurrFormatForLVC(String regExpression, Mobile_HTML_Report currencyReport,
			String currencyName) {
		// TODO Auto-generated method stub
		
	}

	public void freeSpinSummaryWinCurrFormatForLVC(String regExpression, Mobile_HTML_Report currencyReport,
			String currencyName) {
		// TODO Auto-generated method stub
		
	}

	public void freeGameSummaryWinCurrFormatForLVC(String regExpression, Mobile_HTML_Report currencyReport,
			String currencyName) {
		// TODO Auto-generated method stub
		
	}

	public boolean verifyWinAmtCurrencyFormatForLVC(String regExpression) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setMinBet() {
		try {

		} catch (Exception e) {
			e.getMessage();
		}
	}
	/**
	 * method is used to click by using xpath
	 * @param locator
	 * @return
	 */
	public boolean TapbyXpath(String locator) 
	{
		try
		{
		Thread.sleep(2000);
		WebElement ele=webdriver.findElement(By.xpath(xpathMap.get(locator)));
		clickWithWebElementAndroid(webdriver, ele, 0);
		log.debug("Clicked on spin button");
		//need to uncomment for other game Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("Spin_Button_ID"))));
		}
		catch (Exception e) {
		log.error("rror while clicking on spin button", e);
		}
		return true;
		}
	/**
	 * method is used to click by using ID
	 * @param locator
	 * @return
	 */
	public boolean TapbyID(String locator) 
	{
		try
		{
		Thread.sleep(2000);
		WebElement we = webdriver.findElement(By.id(locator));
		clickWithWebElement(we, 0);
		log.debug("Clicked on spin button");
		//need to uncomment for other game Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("Spin_Button_ID"))));
		}
		catch (Exception e) {
		log.error("rror while clicking on spin button", e);
		}
		return true;
		}
	
	
	
	public void clickWithWebElement(AppiumDriver<WebElement> webdriver, WebElement webViewElement,
			int Xcoordinate_AddValue) {
		String s = null;
		try {
			double urlHeight = 0.0;
			JavascriptExecutor javaScriptExe = (JavascriptExecutor) webdriver;
			int webViewInnerWidth = ((Long) javaScriptExe
					.executeScript("return window.innerWidth || document.body.clientWidth")).intValue();
			System.out.println("webViewInnerWidth " + webViewInnerWidth);
			int webViewOuterWidth = ((Long) javaScriptExe
					.executeScript("return window.outerWidth || document.body.clientWidth")).intValue();
			System.out.println("webViewInnerWidth " + webViewInnerWidth);
			int webViewInnerHeight = ((Long) javaScriptExe
					.executeScript("return window.innerHeight || document.body.clientHeight")).intValue();
			System.out.println("webViewInnerHeight " + webViewInnerHeight);
			int webViewOuterHeight = ((Long) javaScriptExe
					.executeScript("return window.outerHeight || document.body.clientHeight")).intValue();
			System.out.println("webViewOuterHeight " + webViewOuterHeight);
			int webViewOffsetHeight = ((Long) javaScriptExe
					.executeScript("return window.offsetHeight || document.body.clientHeight")).intValue();
			System.out.println("webViewOffsetHeight " + webViewOffsetHeight);
			int webViewBottomHeight = webViewOffsetHeight - webViewInnerHeight;
			System.out.println("webViewBottomHeight " + webViewBottomHeight);
			Dimension elementSize = webViewElement.getSize();
			System.out.println("elementSize " + elementSize);
			int webViewElementCoX = webViewElement.getLocation().getX() + (elementSize.width / 2);
			System.out.println("webViewElementCoX " + webViewElementCoX);
			int webViewElementCoY = webViewElement.getLocation().getY() + (elementSize.height / 2);
			System.out.println("webViewElementCoY " + webViewElementCoY); // double connectedDeviceWidth =
																			// typeCasting(javaScriptExe.executeScript("return
																			// window.screen.width *
																			// window.devicePixelRatio"), webdriver);
			// double connectedDeviceHeight =
			// typeCasting(javaScriptExe.executeScript("return window.screen.height *
			// window.devicePixelRatio"), webdriver);
			// //System.out.println("connectedDeviceWidth "+connectedDeviceWidth);
			// System.out.println("connectedDeviceHeight "+connectedDeviceHeight);
			String curContext = webdriver.getContext();
			webdriver.context("NATIVE_APP");
			urlHeight = (double) webViewOuterHeight - (double) webViewInnerHeight;
			System.out.println("url height " + urlHeight);
			// urlHeight=0.0;
			double relativeScreenViewHeight = webViewOuterHeight - urlHeight;
			System.out.println("relativeScreenViewHeight " + relativeScreenViewHeight);
			double nativeViewEleX = (double) (((double) webViewElementCoX / (double) webViewInnerWidth)
					* webViewOuterWidth);
			System.out.println("nativeViewEleX " + nativeViewEleX);
			double nativeViewEleY = (double) (((double) webViewElementCoY / (double) webViewOuterHeight)
					* relativeScreenViewHeight);
			System.out.println("nativeViewEleY " + nativeViewEleY);
			tapOnCoordinates(webdriver, (nativeViewEleX + Xcoordinate_AddValue), ((nativeViewEleY + urlHeight + 1)));
			webdriver.context(curContext);
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
	
	public void tapOnCoordinates(AppiumDriver<WebElement> webdriver, final double x, final double y) {
		// new TouchAction(webdriver).tap((int)x, (int)y).perform();
		// int X=(int) Math.round(x); // it will round off the values
		// int Y=(int) Math.round(y);
		System.out.println("X cor - " + x + "," + " Y cor - " + y);
		System.out.println(x);
		System.out.println(y);
		TouchAction action = new TouchAction(webdriver);

		action.press(PointOption.point((int) x, (int) y)).release().perform();

		// action.tap(PointOption.point(X, Y)).perform();

	}
	
/**
 * Click with Web Element using the co-ordinates	
 * @param webdriver
 * @param webViewElement
 * @param Xcoordinate_AddValue
 */
	public void clickWithWebElementAndroid (AppiumDriver<WebElement> webdriver,WebElement webViewElement,int Xcoordinate_AddValue)
	{
		String s=null;
		try 
		{
			double urlHeight=0.0;
			JavascriptExecutor javaScriptExe = (JavascriptExecutor) webdriver;			  	
			int webViewWidth = ((Long) javaScriptExe.executeScript("return window.innerWidth || document.body.clientWidth")).intValue();
			int webViewHeight = ((Long) javaScriptExe.executeScript("return window.innerHeight || document.body.clientHeight")).intValue();
			Dimension elementSize = webViewElement.getSize();			  		
			int webViewElementCoX = webViewElement.getLocation().getX() + (elementSize.width / 2);
			int webViewElementCoY = webViewElement.getLocation().getY() + (elementSize.height / 2);		

			double   connectedDeviceWidth = typeCasting(javaScriptExe.executeScript("return window.screen.width * window.devicePixelRatio"),webdriver); 
			double   connectedDeviceHeight = typeCasting(javaScriptExe.executeScript("return window.screen.height * window.devicePixelRatio"),webdriver);

			String curContext=webdriver.getContext();
			webdriver.context("NATIVE_APP");
			//urlHeight = (double) connectedDeviceHeight * (0.12);
			/*if(xpathMap.get("serviceUrl").equalsIgnoreCase("yes"))
				urlHeight = (double) screenHeight * (0.12);
			else if(xpathMap.get("serviceUrl").equalsIgnoreCase("no"))
				urlHeight = 0.0;
			else
				urlHeight = (double) screenHeight * (0.22);	*/
				//urlHeight=0.0;		
				double relativeScreenViewHeight = connectedDeviceHeight - urlHeight;	
				double nativeViewEleX = (double) (((double) webViewElementCoX / (double) webViewWidth) * connectedDeviceWidth);
				//double nativeViewEleY = (double) (((double) webViewElementCoY / (double) webViewHeight) );			  		
				double nativeViewEleY = (double) (((double) webViewElementCoY / (double) webViewHeight)* relativeScreenViewHeight);
				tapOnCoordinates(webdriver,nativeViewEleX+Xcoordinate_AddValue, (nativeViewEleY + urlHeight + 1));	
				webdriver.context(curContext);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//method is used to click on the webelements
	public void clickWithTheWebElementAndriod(AppiumDriver<WebElement> webdriver,WebElement webViewElement,int Xcoordinate_AddValue) 
	{
		String s = null;
		try 
		{
			
			JavascriptExecutor javaScriptExe = (JavascriptExecutor) webdriver;			  	
			int webViewWidth = ((Long) javaScriptExe.executeScript("return window.innerWidth || document.body.clientWidth")).intValue();
			int webViewHeight = ((Long) javaScriptExe.executeScript("return window.innerHeight || document.body.clientHeight")).intValue();
			Dimension elementSize = webViewElement.getSize();			  		
			int webViewElementCoX = webViewElement.getLocation().getX() + (elementSize.width / 2);
			int webViewElementCoY = webViewElement.getLocation().getY() + (elementSize.height / 2);		

			double   connectedDeviceWidth = typeCasting(javaScriptExe.executeScript("return window.screen.width * window.devicePixelRatio"),webdriver); 
			double   connectedDeviceHeight = typeCasting(javaScriptExe.executeScript("return window.screen.height * window.devicePixelRatio"),webdriver);

			System.out.println("webViewHeight "+webViewHeight);
			System.out.println("connectedDeviceHeight "+connectedDeviceHeight);
			
			
			
			int webViewInnerHeight = ((Long) javaScriptExe
					.executeScript("return window.innerHeight || document.body.clientHeight")).intValue();
			System.out.println("webViewInnerHeight " + webViewInnerHeight);
			int webViewOuterHeight = ((Long) javaScriptExe
					.executeScript("return window.outerHeight || document.body.clientHeight")).intValue();
			System.out.println("webViewOuterHeight " + webViewOuterHeight);
			int webViewOffsetHeight = ((Long) javaScriptExe
					.executeScript("return window.offsetHeight || document.body.clientHeight")).intValue();
			System.out.println("webViewOffsetHeight " + webViewOffsetHeight);
			int webViewBottomHeight = webViewOffsetHeight - webViewInnerHeight;
			System.out.println("webViewBottomHeight " + webViewBottomHeight);
			
			AndroidDriver<WebElement> androidDriver =(AndroidDriver<WebElement>) webdriver;
			
			
			long statusBarHeight=(Long)androidDriver.getCapabilities().getCapability("statBarHeight");
			System.out.println("statusBarHeight "+statusBarHeight);
			
			double pixelRatio =0 ;
			Object pixelRatioObject = androidDriver.getCapabilities().getCapability("pixelRatio");
			
			if(pixelRatioObject instanceof Long ){
				 pixelRatio=(Long)androidDriver.getCapabilities().getCapability("pixelRatio");
				
			}else if (pixelRatioObject instanceof Double ) {
				 pixelRatio=(Double)androidDriver.getCapabilities().getCapability("pixelRatio");
			}
			
			
			/***************************************/
			double navigationBarHeight=0;
			double urlHeight=0.0;
					
			
			
			double webViewOffsetHeightInPixel= webViewOffsetHeight*pixelRatio;
			
			// to check the game in fullscreen
			double hightDifference = connectedDeviceHeight - webViewOffsetHeightInPixel;
			
			
			boolean isGameInFullScreenMode = (hightDifference < 2 && hightDifference > -2 );
			System.out.println(" isGameInFullScreenMode ::"+isGameInFullScreenMode);
			
			if(!isGameInFullScreenMode)
			{
				if(webViewOffsetHeight!=0) 
				{
				// Calculate the navigation Bar height and urlHeigt
				navigationBarHeight = connectedDeviceHeight - (webViewOffsetHeightInPixel + statusBarHeight);

				System.out.println("navigationBarHeight " + navigationBarHeight);
				urlHeight = ((webViewOffsetHeight - webViewHeight) * pixelRatio) + statusBarHeight;
				System.out.println(" urlHeight and  statusBarHeight " + urlHeight);
				}
				else 
				{
					navigationBarHeight=0;
					urlHeight = connectedDeviceHeight-((webViewHeight * pixelRatio));
				}
			}
			
			/***************************************/
			
			
			String curContext=webdriver.getContext();
			webdriver.context("NATIVE_APP");
			
			
			/*if(xpathMap.get("serviceUrl").equalsIgnoreCase("yes"))
				urlHeight = (double) screenHeight * (0.12);
			else if(xpathMap.get("serviceUrl").equalsIgnoreCase("no"))
				urlHeight = 0.0;
			else
				urlHeight = (double) screenHeight * (0.22);	*/
				//urlHeight=0.0;		
				double relativeScreenViewHeight = connectedDeviceHeight - urlHeight-navigationBarHeight;	
				double nativeViewEleX = (double) (((double) webViewElementCoX / (double) webViewWidth) * connectedDeviceWidth);
				//double nativeViewEleY = (double) (((double) webViewElementCoY / (double) webViewHeight) );			  		
				double nativeViewEleY = (double) (((double) webViewElementCoY / (double) webViewHeight)* relativeScreenViewHeight);
				tapOnCoordinates(webdriver,nativeViewEleX+Xcoordinate_AddValue, (nativeViewEleY + urlHeight + 1));	
				webdriver.context(curContext);
		} catch (Exception e)
		{
			e.getStackTrace();
		}
	}
	
	
	
	
	//To get the Bet pannel values 
		public String[] verifyBetPannelValues(Mobile_HTML_Report report,String CurrencyName) 
		{
			// TODO Auto-generated method stub
			return null;
		}
		

		//method is to click the WebElements using Xpath for Component Store Games	
			public boolean NativeClickByXpath_CS(String locator ) 
			{
				boolean present = false;
				try 
				{
					if (osPlatform.equalsIgnoreCase("Android")) 
					{
						
						Wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathMap.get(locator))));
						WebElement element =  webdriver.findElement(By.xpath(xpathMap.get(locator)));
						//clickWithWebElementAndroid(webdriver, element, 0);
						clickWithTheWebElementAndriod(webdriver, element, 0);
						System.out.println("Element Found and Clicked");log.debug("Element Found and Clicked");
						}
				
		      		else 
					{
		      			Wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathMap.get(locator))));
		      			/*MobileElement ele = (MobileElement) webdriver.findElement(By.xpath(xpathMap.get(locator)));
		      			ele.click();*/
		      			//webdriver.executeScript("arguments[0].click();", ele);
		      			
		      		Thread.sleep(3000);
		      	     WebElement ele1 =  webdriver.findElement(By.xpath(xpathMap.get(locator)));
		      	     System.out.println(xpathMap.get(locator));
		    	       ele1.click();
		      			//clickWithWebElement(ele,0);
		      			/*int elementWidth = ele.getSize().getWidth();System.out.println("Element width: "+ elementWidth);int elementHeight = element.getSize().getHeight();System.out.println("Element width: "+ elementHeight);
		      			Actions actions = new Actions(webdriver);
		      			actions.moveToElement(webdriver.findElement(By.tagName("body")), 0, 0);
		      			actions.moveByOffset(xCoordinate, yCoordinate).click().build().perform();*/
		                System.out.println("Element Found and Clicked");
						log.debug("Element Found and Clicked");
						
							
					}

						present = true;
					//webdriver.context(context);
				} 
				catch (Exception e) 
				{
					System.out.println(e.getMessage());
					System.out.println("Unable to click");
					log.debug("Unable to click");
					present = false;
				}
				return present;
			}
			//method is to click the WebElements using id for Component Store Games	
			public boolean NativeClickByID_CS(String locator )
			{
				boolean present = false;
				try 
				{
					if (osPlatform.equalsIgnoreCase("Android")) 
					{
						
						Wait.until(ExpectedConditions.elementToBeClickable(By.id(xpathMap.get(locator))));
						WebElement element =  webdriver.findElement(By.id(xpathMap.get(locator)));
						//clickWithWebElementAndroid(webdriver, element, 0);
						clickWithTheWebElementAndriod(webdriver, element, 0);
						System.out.println("Element Found and Clicked");log.debug("Element Found and Clicked");
						
						}
			
					
		      		else 
					{
		      			//MobileElement ele = (MobileElement) webdriver.findElement(By.id(xpathMap.get(locator)));
		      			WebElement ele =  webdriver.findElement(By.id(xpathMap.get(locator)));
		      			//webdriver.executeScript("arguments[0].click();", ele);
		      			ele.click();
		      			Thread.sleep(3000);
		                System.out.println("element Found and Clicked");
						log.debug("Element Found and Clicked");
						
							
					}

						present = true;
					//webdriver.context(context);
				} 
				catch (NoSuchElementException | InterruptedException e) 
				{
					System.out.println(e.getMessage());
					System.out.println("Unable to Navigate to the clicked Page");
					log.debug("Unable to Navigate to the clicked Page");
					present = false;
				}
				return present;
			}

			/*
			 * Method for payout verification for all bets
			 
			public void PayoutValidationforBetLVC(Mobile_HTML_Report report,String regExpression,String currencyName);
			{

			}*/
	
			public void freeGameInfoCurrencyFormat(String regExpression, Mobile_HTML_Report currencyReport,
					String currencyName) {

				// TODO Auto-generated method stub
				
			}
			

			
			/*
			 * verifies the RegularExpression
			 */
			public boolean verifyRegularExpression(String curencyAmount, String regExp)
			{
				return false;
				
			}
			

			public void validateMiniPaytableForLVC(Mobile_HTML_Report currencyReport,
					String regExpression,String currencyName) {
				// TODO Auto-generated method stub
				
			}
			public boolean currencyFormatValidatorForLVC(String curencyAmount, String regExpression) {
				return false;

			}
			public void closeOverlayForLVC() 
			{
				try 
				{
				if (osPlatform.equalsIgnoreCase("iOS")) 
				{
					if(webdriver.getOrientation().equals(ScreenOrientation.PORTRAIT))
					{
						TouchAction touchAction = new TouchAction(webdriver);
						touchAction.tap(PointOption.point(40, 120)).perform();
					}
					else
					{
						TouchAction touchAction = new TouchAction(webdriver);
						touchAction.tap(PointOption.point(50, 80)).perform();
					}
				} else 
				{// For Andriod mobile
					
					Actions act = new Actions(webdriver);
					act.moveByOffset(20, 80).click().build().perform();
					act.moveByOffset(-20, -80).build().perform();
				}
				}
				 catch (Exception e) 
				{
						log.error("error while closing overlay ", e);
						log.error(e.getMessage());
				}
			}
			
			public void isFreeGamesVisible(String url) {
				// TODO Auto-generated method stub
				
			}
			
			public boolean assignFreeGames21(String userName,String offerExpirationUtcDate,int mid, int cid,int languageCnt,int defaultNoOfFreeGames)
			{
				//assign free games to above created user
				boolean isFreeGameAssigned=true;
				try 
				{
					String balanceTypeId=xpathMap.get("BalanceTypeID");
					Double dblBalanceTypeID=Double.parseDouble(balanceTypeId);
					balanceTypeId=""+dblBalanceTypeID.intValue()+"";

					//Assign free games offers to user
					if(TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
					{
						isFreeGameAssigned=addFreeGameToUserInBluemesa( userName,defaultNoOfFreeGames , offerExpirationUtcDate, balanceTypeId, mid, cid,languageCnt*2);
					}
					else
					{
						isFreeGameAssigned=addFreeGameToUserInAxiom(userName,defaultNoOfFreeGames,offerExpirationUtcDate,balanceTypeId,mid,cid,languageCnt*2);
					}
				}
				catch (Exception e)
				{
				log.error(e.getMessage(), e);
				}
				return isFreeGameAssigned;
			}
			//@Override
			public void setNameSpace2()
			{
			int nameSpaceCount= Integer.parseInt(TestPropReader.getInstance().getProperty("ForceNameSpaceCount"));



			for(int count=0;count<nameSpaceCount;count++){
			String nameSpace= TestPropReader.getInstance().getProperty("ForceNameSpace"+count);



			String namspaceResponce= null;
			namspaceResponce=GetConsoleText("return "+nameSpace+".currentScene");
			if(namspaceResponce!=null&&!"".equals(namspaceResponce)){
			log.debug("NameSpace for this game :: "+nameSpace);
			GameName=nameSpace;
			log.debug("namsspace for game="+nameSpace);
			break;
			}
			}



			}

			//@Override
			public boolean clickPlayNow2()
			{
			Wait=new WebDriverWait(webdriver,500);

			boolean b = false;
			isAutoplayAvailable();
			try {
			/*JavascriptExecutor js = ((JavascriptExecutor)webdriver);
			Coordinates coordinateObj=new Coordinates();
			String align="return "+forceNamespace+".getControlById('FreeGamesComponent').views.freeGamesOfferView.Buttons.playNowButton.buttonData.text.layoutStyles.desktop.alignment";
			typeCasting("return "+forceNamespace+".getControlById('FreeGamesOffersView.playNowButton').x",coordinateObj);
			coordinateObj.setX(coordinateObj.getSx());
			typeCasting("return "+forceNamespace+".getControlById('FreeGamesOffersView.playNowButton').y",coordinateObj );
			coordinateObj.setY(coordinateObj.getSx());
			typeCasting("return "+forceNamespace+".getControlById('FreeGamesOffersView.playNowButton').height",coordinateObj);
			coordinateObj.setHeight(coordinateObj.getSx());
			typeCasting("return "+forceNamespace+".getControlById('FreeGamesOffersView.playNowButton').width",coordinateObj);
			coordinateObj.setWidth(coordinateObj.getSx());
			coordinateObj.setAlign((js.executeScript(align)).toString());
			getComponentCenterCoordinates(coordinateObj);
			clickAtCoordinates(coordinateObj.getCenterX(), coordinateObj.getCenterY());*/



			//clickAtButton("return "+xpathMap.get("FreeGamesPlayNow"));
			Thread.sleep(2000);
			b = true;
			}
			catch (Exception e) {
			e.printStackTrace();
			}
			return b;
			}
			/**
			 * method verifies the entry screen of Free Games
			 * @param report
			 * @param currencyName
			 * @return
			 */
				public boolean freeGamesEntryScreen(Mobile_HTML_Report report,String currencyName)
				{
					return false;
					
				}
				/**
				 * method is for free game Information Icon
				 */
				public String freeGameEntryInfo(Mobile_HTML_Report report,String currencyName,String locator1 ,String Locator2) 
				{
					return null;
				}
				/**
				 * method is to refresh in Free Games
				 */
				public boolean freeGameOnRefresh(Mobile_HTML_Report report,String currencyName) 
				{
					return false;
				}
				/**
				 * Verifies the Big Win on refresh Base Game
				 * 
				 */
				public String verifyBigWinOnRefresh(Mobile_HTML_Report report,String CurrencyName) 
				{
					return null;
					
				}
				/**
				 * method is to Back to BaseGame in Free Games
				 */
				public String freeGameBackToBaseGame(Mobile_HTML_Report report,String currencyName) 
				{
					return null;
				}
				//Method is to get the text by using ID 
				public String func_GetTextID(String locator)
				{
					return null;
				
				}
				/**
				 * Verifies Paytable open
				 * 
				 */
				public boolean paytableVarification(Mobile_HTML_Report report,String CurrencyName) 
				{
					return false;
				
				}		
				/**
				 * Verifies the Paytable text validation 
				 * 
				 */
				public String textValidationForPaytableBranding(Mobile_HTML_Report report,String CurrencyName) 
				{
					return null;
					
				}
				/**
				 * Verifies Paytable payout varification for 3 reel game
				 * 
				 */
				public String[]  singlePaytablePayouts(Mobile_HTML_Report report,String CurrencyName) 
				{
					return null;
					
				}
				/**
				 * method is to 
				 * @param report
				 * @param currencyName
				 * @return
				 */
				public boolean topBarMenuButtonIcons(Mobile_HTML_Report report, String currencyName) 
				{
					return false;
					
				}
				/**
				 * Verify Clock from Top Bar
				 *
				 */
					public boolean clockFromTopBar(Mobile_HTML_Report report)
				{
					return false;
					}

				public boolean clockFromTopBar(Mobile_HTML_Report lvcReport, String currencyName) 
				{
					// TODO Auto-generated method stub
					return false;
				}
				/**
				 * Click menu Buttons
				 * @param currencyName 
				 * @return
				 * @throws InterruptedException
				 */
				public boolean menuButtonS(Mobile_HTML_Report report, String currencyName) 
				{
					return false;
					
				}
				
				/*
				 * set Max bet using scroll bar 
				 */
				public String setMaxBetUsingScrollBar(Mobile_HTML_Report report , String CurrencyName ) 
				{
					return null;
				}
				/*
				 * set min bet using scroll bar 
				 */
				public String setMinBetUsingScrollBar(Mobile_HTML_Report lvcReport, String currencyName)
				{
					// TODO Auto-generated method stub
					return null;
				}
				/**
				 * method is for to scroll five times
				 * @param report
				 * @param CurrencyName
				 * @return
				 */
					public boolean paytableScrollOfFive(Mobile_HTML_Report report, String CurrencyName) 
					{
						return false;
						
					}
					/**
					 * method is for to scroll seven times
					 * @param report
					 * @param CurrencyName
					 * @return
					 */
					public boolean paytableScrollOfSeven(Mobile_HTML_Report report, String CurrencyName) 
					{
						return false;
					}
					/**
					 * method is used to validate the Paytable Values 
					 * @return
					 */
					public boolean validatePayoutsFromPaytable(Mobile_HTML_Report report,String CurrencyName,String regExpr) //String[] array
					{
						return false;
						
					}
					/**
					 * Method for payout verification for all bets
					 */

					public void PayoutValidationforBetLVC(Mobile_HTML_Report report,String regExpr,String currencyName) 
					{
					
					}
					/**
					 * Verifies the Big Win on refresh
					 * 
					 */
					public String verifyBigWinRefreshOnFreeSpins(Mobile_HTML_Report report,String CurrencyName) 
					{
						return null;
						
					}
					
		public void closeOverlayForGamePannelsLVC() 
		{
			try
			{
				 if(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame")))
				 {
					boolean flag = getConsoleBooleanText("return "+xpathMap.get("isSpinBackBtnVisible"));
					if(flag)
					{
						clickAtButton("return "+xpathMap.get("ClickSpinBackBtn"));	
						log.debug("Clicked close totalBet button");
					}
				 }
				 else
				 {
					 closeOverlayForLVC();
					log.debug("Clicked on screen to close overlay");
				 }
			}
			catch (Exception e) 
			{
				log.error("error in closing overlay", e);
				log.error(e);
			}
		}

		public void isBonusMultiplierVisible() {
			// TODO Auto-generated method stub
			
		}

		public boolean isFreeSpinTriggered() {
			// TODO Auto-generated method stub
			return false;
		}
		
		public void fullScreenOverOnTopofContinue()
		{
			try
			{
				if (osPlatform.equalsIgnoreCase("Android")) 
				{
					newFeature();
				}
			}catch (Exception e) 
			{
				log.error("error", e);
				log.error(e);
			}
			
		}
		public boolean loadGameForLVC(String url) {
			boolean isGameLaunch = false;
			try {
				Wait = new WebDriverWait(webdriver, 120);
				webdriver.navigate().to(url);
				System.out.println(url);
				if ((Constant.YES.equalsIgnoreCase(xpathMap.get("continueBtnOnGameLoad")))) 
				{
					
					if("Yes".equalsIgnoreCase(xpathMap.get("CntBtnNoXpath"))) 
					{	
						log.debug("Waiting for clock to be visible");
						Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("clock"))));
						isGameLaunch = true;
						log.debug(" clock is visible");
			      	}	else
					{
				
					Wait.until(ExpectedConditions
							.visibilityOfElementLocated(By.xpath(xpathMap.get("OneDesign_NewFeature_ClickToContinue"))));
					isGameLaunch = true;
					
					}
					
					
					
				} else {
					log.debug("Waiting for clock to be visible");
					Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("clock"))));
					isGameLaunch = true;
					log.debug(" clock is visible");
				}
				log.debug("game loaded ");
			} catch (WebDriverException e) {
				log.debug("Exception occur in loadgame()");
				log.error(e.getMessage());
			}
			return isGameLaunch;
		}	
		

		//TS64283
		public void autoPlay_with_QSUpdated(Mobile_HTML_Report report)
        {
			
		}
		
		//TS64283
		public void capturePaytableScreenshotWithLandscape(Mobile_HTML_Report report, String language) {
		
		}
		//TS64283
		public String VerifystoryoptioninpaytableWithLandscape(Mobile_HTML_Report report, String languageCode){
			String story=null;
			return story;
	}
		//TS64283
		public boolean UnlockAllFreeSpin() {
        return false;
		}
		public void closeOverlay(int x, int y)
	    {
	    
	    }
		
		 public boolean isElementVisible(String element) {
			return false;
			 
		 }
		 
		 public void clickOnButtonUsingCoordinates(String X, String Y) 
		 {
		 
		 }
		 public boolean elementVisible_Xpath(String Xpath) {
			return false;
			 
		 }
		 
		/**
		 * for playnext ui sanity
		 * @param report
		 * @return
		 */
		 public boolean paytableScroll(Mobile_HTML_Report report)
				{
					// TODO Auto-generated method stub
					return false;
				}
		 public void validateCoinSizeSlider(Mobile_HTML_Report sanityReport,String sliderPoint,String betValue,String currencyName)
			{
			 
			}
		 public void validateCoinsPerLineSlider(Mobile_HTML_Report report,String sliderPoint,String betValue,String CurrencyName)
			{
			 
			}
		 public void validateLinesSlider(Mobile_HTML_Report report,String sliderPoint,String betValue,String CurrencyName)
			{
			 
			}
		 public boolean verifySliderValue(String sliderPoint,String betValue)
			{
				return false;
			 
			}
		 public boolean setMaxbetPlayNext()
			{
				return false;
			 
			}
		 public void quickSpinOnBaseGame(Mobile_HTML_Report report)
			{
			 
			}
		 public void validateNFDButton(Mobile_HTML_Report report,String CurrencyName)
			{
			 
			}
		 public boolean verifyPaytableScroll(Mobile_HTML_Report report,String CurrencyName)
			{
				return false;
				
			}
		 public void verifyMenuOptions(Mobile_HTML_Report report,String CurrencyName)
			{
			 
			}
		 public boolean paytableScrollOfNine(Mobile_HTML_Report report,String CurrencyName) {
			return false;
			 
		 }
		
		 public boolean waitForContinueButton()
			{
				return false;
			 
			}

		public boolean openAutoplayPanel() {
			return false;
			// TODO Auto-generated method stub
			
		}
		public void verifyBetSliders(Mobile_HTML_Report report,ImageLibrary imageLibrary,String CurrencyName)
		{
			
		}
		
		public boolean openBetPanelOnBaseScene()
		{
			return false;
			
		}


		public String entryScreen_Wait_Upadated_FreeSpin(String freeSpinEntryScreen) {
		
			return null;
		}


		
		public void validateLossLimitSliderAutoplay(Mobile_HTML_Report report,String sliderPoint,String Value,String CurrencyName)
		{	
			
		}
		
		public void validateWinLimitSliderAutoplay(Mobile_HTML_Report report,String sliderPoint,String Value,String CurrencyName)
		{
			
		}
		public void validateTotalBetSliderAutoplay(Mobile_HTML_Report report,String sliderPoint,String Value,String CurrencyName)
		{						
		
		}
		
		public void validateSpinsSliderAutoplay(Mobile_HTML_Report report,String sliderPoint,String Value,String CurrencyName)
		{
		
		}

		public void stopAutoPlay() 
		{
			
		}
		public boolean openMenuPanel()
		{
			return false;
			
		}
		public String verifyBonusWin(Mobile_HTML_Report report, String CurrencyName) {
			return null;
		
		}
		public void verifyBetValuesAppended(boolean isBetChangedIntheConsole, String quickBetVal, Mobile_HTML_Report report,String currencyName) {
			
		}
		public boolean isBetChangedIntheConsole(String betValue) {
			return false;
			
		}
		public void verifyQuickbetOptionsRegExp(Mobile_HTML_Report report, String currencyName ,String regExpr,String regExprNoSymbol,String isoCode) {
			
		}
		public void verifyquickbetsWithNoSymbol(Mobile_HTML_Report report, String currencyName ,String regExprNoSymbol) {
			
		}
		public String verifyFSCurrentWinAmt(Mobile_HTML_Report report, String CurrencyName) {
			return null;
			
		}
		public String verifyFreeSpinBigWin(Mobile_HTML_Report report, String CurrencyName) {
			return null;
			
		}
		
		public boolean waitForElement(String hook)
	    {
			return false;  
			
	    }
		public boolean verifyGridPayouts(Mobile_HTML_Report report, String regExp,String CurrencyName,String isoCode) {
			return false;
			
		}
		public String[] paytablePayoutsOfScatter(Mobile_HTML_Report report, String CurrencyName) // String[] array
		{
			return null;
			
		}
		public String[] paytablePayoutsOfScatterWild(Mobile_HTML_Report report, String CurrencyName) // String[] array
		{
			return null;
		
		}
		
		public void paytableOpen() 
			{

			}
		 public boolean isElementVisibleUsingHook(String elementHook) {
			return false;
		 
		 }
		 public String funcGetText(String locator) 
		 {
			return locator;
		 
		 }
		 
		 public void closeUsingCoordinates()
			{
				
			}
		 
		 public boolean scrollUsingElement(String element)
		 {
			 return false;
		 }
		 public boolean verifyRegularExpressionPlayNext(Mobile_HTML_Report report, String regExp, String method,String isoCode) {
			 return false;
		 }
		 
		 public String verifyBonusWinInFS(Mobile_HTML_Report report, String CurrencyName) {
			return null;
			 
		 }
		 

			/*
			 * Method to click FG info btn and return FGinfo
			 */
			public String freeGameEntryInfo(ImageLibrary imageLibrary, String fgInfotxt)  
			{	
				return null;
			}

			
			public boolean clickOnEntryScreenDiscard(ImageLibrary imageLibrary)
			{
				return false;
				
			}
			public boolean verifySettingsOptions(Mobile_HTML_Report report,String CurrencyName) {
				return false;
			}
			public void verifyHelpOnTopbar(Mobile_HTML_Report report,String CurrencyName) {
			}
			
			public void checkpagenavigation(Mobile_HTML_Report report, String gameurl,String CurrencyName)
			{
				
			}
			
			
			
			public void verifyquickbetOptionsRegExp(Mobile_HTML_Report report, String currencyName ,String regExpr,String regExprNoSymbol,String isoCode ) 
			{
				
			}
			public void clickOnContinue() {
				
			}
			public boolean clickOnPlayLater(ImageLibrary imageLibrary) {

				return false;

			}
			public boolean confirmDiscardOffer(ImageLibrary imageLibrary) 
			{
				return false;
			}
			public boolean clickPlayNow(ImageLibrary imageLibrary) 
			{
				return false;
			}
			public boolean spinclick(ImageLibrary imageLibrary)
			{
				return true;
			}
			public void verifyAutoplayOptions(Mobile_HTML_Report report, String languageCurrency) {
				// TODO Auto-generated method stub
				
			}
			 /**
			 * This method is used to verify big win with image
			 * 
			 * @author pb61055
			 */
			public String verifyBigWinImg(Mobile_HTML_Report report,ImageLibrary imageLibrary,String CurrencyName) {
				return CurrencyName;
			
			}
			/**
			 * This method is used to current win with image
			 * 
			 * @author pb61055
			 */
			public String getCurrentWinAmtImg(Mobile_HTML_Report report,ImageLibrary imageLibrary,String CurrencyName) {
				return CurrencyName;
				
			}
			
			/**
			 * This method is used to set mobile context as chrominum/web view
			 * @author pb61055
			 */
			public void setChromiumWebViewContext()
			{
				try
				{
					if (webdriver instanceof AndroidDriver) 
					{
						webdriver.context("CHROMIUM");
					}
					else if (webdriver instanceof IOSDriver) 
					{
					  Set<String> contexts = webdriver.getContextHandles();
						for (String context : contexts) {
							if (context.startsWith("WEBVIEW")) 
							{
								log.debug("context going to set in IOS is:" + context);
								webdriver.context(context);
							}
						}
					}
					Thread.sleep(2000);
				}
				catch (Exception e) 
				{
					log.error(e.getMessage(), e);
				}
			}
			
			 /**
			    * This method is used to click on Base scene continue/NFD buttin
			    * @author pb61055
			    * @param report
			    * @param imageLibrary
			    */
			   public void clickOnBaseSceneContinueButton(Mobile_HTML_Report report,ImageLibrary imageLibrary,String currencyName)
			   {
				   
			   }
			   /**
				 * This method is used to refresh game
				 * @author pb61055
				 * @param report
				 * @param imageLibrary
				 * @param currencyName
				 */
				public void refreshGame(Mobile_HTML_Report report,ImageLibrary imageLibrary,String currencyName) 
				{
					
				}
				/**
				 * This method is used to verify quick spin
				 * @author pb61055
				 * @param report
				 * @param imageLibrary
				 * @param currencyName
				 */
				public void verifyQuickSpin(Mobile_HTML_Report report,ImageLibrary imageLibrary,String currencyName)
				{
					
				}
				/**
				 * This method is used to open Autoplay
				 * @author pb61055
				 * @param report
				 * @param imageLibrary
				 * @param currencyName
				 * @return
				 */
				public boolean openAutoplayPanel(Mobile_HTML_Report report,ImageLibrary imageLibrary,String currencyName) 
				{
					return false;
					
				}
				/**
				 * This method is used to validate Autoplay panel
				 * @author pb61055
				 * @param report
				 * @param imageLibrary
				 * @param currencyName
				 */
				public void verifyAutoplayPanel(Mobile_HTML_Report report,ImageLibrary imageLibrary,String currencyName)
				{
					
				}
				/**
				 * This method is used to click on start autoplay
				 * @author pb61055
				 * @param report
				 * @param imageLibrary
				 * @param currencyName
				 */
				public void startAutoplay(Mobile_HTML_Report report,ImageLibrary imageLibrary,String currencyName)
				{
					
				}
				/**
				 * This method is used to stop autoplay spins
				 * @author pb61055
				 * @param report
				 * @param imageLibrary
				 * @param currencyName
				 */
				public void stopAutoplay(Mobile_HTML_Report report,ImageLibrary imageLibrary,String currencyName)
				{
					
				}
				/**
				 * This method is used to open bet panel
				 * @author pb61055
				 * @param report
				 * @param imageLibrary
				 * @param currencyName
				 * @return
				 */
				public boolean openBetPanel(Mobile_HTML_Report report,ImageLibrary imageLibrary,String currencyName) 
				{
					return false;
					
				}
				/**
				 * This method is used to validate bet panel
				 * @author pb61055
				 * @param report
				 * @param imageLibrary
				 * @param currencyName
				 */
				public void verifyBetPanel(Mobile_HTML_Report report,ImageLibrary imageLibrary,String currencyName)
				{
					
				}

				/**
				 * This method is used to validate set max bet
				 * @author pb61055
				 * @param report
				 * @param imageLibrary
				 * @param currencyName
				 */
				public void setMaxBet(Mobile_HTML_Report report,ImageLibrary imageLibrary,String currencyName)
				{
					
				}
				/**
				 * This method is used to open Menu panel
				 * @author pb61055
				 * @param report
				 * @param imageLibrary
				 * @param currencyName
				 * @return
				 */
				public boolean openMenuPanel(Mobile_HTML_Report report,ImageLibrary imageLibrary,String currencyName) 
				{
					return false;	
					
				}
				/**
				 * This method is used to close settings/Paytable/Autoplay/bet using close button
				 * @author pb61055
				 * @param imageLibrary
				 * @return
				 */
				public boolean closeButton(ImageLibrary imageLibrary) 
				{
					return false;
					
				}
				/**
				 * This method is is used to verify paytable and paytable scroll
				 * @author pb61055
				 * @param report
				 * @param imageLibrary
				 * @param currencyName
				 */
				public void verifyPaytable(Mobile_HTML_Report report,ImageLibrary imageLibrary,String currencyName)
				{
					
				}
				/**
				 * This method is used to validate Settings panel
				 * @author pb61055
				 * @param report
				 * @param imageLibrary
				 * @param currencyName
				 */
				public void verifySettingsPanel(Mobile_HTML_Report report,ImageLibrary imageLibrary,String currencyName)
				{
					
				}
				/**
				 * *This method is used to open paytable
				 * @author pb61055
				 * @param report
				 * @param imageLibrary
				 * @param currencyName
				 * @return
				 */
				public boolean openPaytable(Mobile_HTML_Report report,ImageLibrary imageLibrary,String currencyName) 
				{
					return false;
					
				}
				/**
				 * This method is used to close paytable
				 * @author pb61055
				 * @param imageLibrary
				 * @return
				 */
				public boolean closePaytable(ImageLibrary imageLibrary) 
				{
					return false;
					
				}
				/**
				 * This method is used to open settings
				 * @author pb61055
				 * @param report
				 * @param imageLibrary
				 * @param currencyName
				 * @return
				 */
				public boolean openSettingsPanel(Mobile_HTML_Report report,ImageLibrary imageLibrary,String currencyName) 
				{
					return false;
					
				}
				
				public void verifyHelpNavigation(Mobile_HTML_Report report,ImageLibrary imageLibrary,String currencyName)
				{
					
				}
				  /**
				    * This method is used for verifying currency format for credits
				    * @author pb61055
				    * @param report
				    * @param currencyName
				    * @param regExpr
				    */
				   
				   public void verifyCreditsCurrencyFormat(Mobile_HTML_Report report, String regExpr)
				   {
					   
				   }
				   
				   /**
				    * This method is used for verifying currency format for credits
				    * @author pb61055
				    * @param report
				    * @param currencyName
				    * @param regExpr
				    */
				   
				   public void verifyBetCurrencyFormat(Mobile_HTML_Report report, String regExpr)
				   {
					   
				   }
				   
				   /**
					 * This method is used to validate paytable payout currency format
					 * @author pb61055
					 * @param report
					 * @param imageLibrary
					 * @param currencyName
					 * @param regExpr
					 */
					public void validatePaytablePayoutsCurrencyFormat(Mobile_HTML_Report report,ImageLibrary imageLibrary,String currencyName,String regExpr,String isoCode)
					{
						
					}
					
	/**
	 * This method is used to validate normal win amount
	 * @author pb61055
	 * @param report
	 * @param imageLibrary
	 * @param currencyName
	 * @param regExpr
	 */
	public void verifyNormalWinCurrencyFormat(Mobile_HTML_Report report,ImageLibrary imageLibrary,String currencyName,String regExpr,String isoCode)
	{
		
	}
	/**
	 * This method is used to validate big win amount
	 * @author pb61055
	 * @param report
	 * @param imageLibrary
	 * @param currencyName
	 * @param regExpr
	 */
	public void verifyBigWinCurrencyFormat(Mobile_HTML_Report report,ImageLibrary imageLibrary,String currencyName,String regExpr,String isoCode)
	{	
		
	}
	/**
	 * This method is used to validate Bonus/Amazing win amount
	 * @author pb61055
	 * @param report
	 * @param imageLibrary
	 * @param currencyName
	 * @param regExpr
	 */
	public void verifyBonusWinCurrencyFormat(Mobile_HTML_Report report,ImageLibrary imageLibrary,String currencyName,String regExpr,String isoCode)
	{
		
	}
	/**
	 * This method is used to verify Freespins currency format
	 * @author pb61055
	 * @param report
	 * @param imageLibrary
	 * @param currencyName
	 * @param regExpr
	 */
	public void verifyFreeSpinsCurrencyFormat(Mobile_HTML_Report report,ImageLibrary imageLibrary,String currencyName,String regExpr,String isoCode,String regExprNoSymbol)
	{
		
	}
	/**
	 * This method is used to verify Free spins summary currency format
	 * @author pb61055
	 * @param report
	 * @param imageLibrary
	 * @param currencyName
	 * @param regExpr
	 */
	public void verifyFreeSpinsSummaryCurrencyFormat(Mobile_HTML_Report report,ImageLibrary imageLibrary,String currencyName)
	{
	
	}
	/**
	 * This method is used to verify Free games entry screen and free games info text currency format
	 * @author pb61055
	 * @param report
	 * @param imageLibrary
	 * @param currencyName
	 * @param regExpr
	 */
	public void verifyFreeGamesEntryScreen(Mobile_HTML_Report report,ImageLibrary imageLibrary,String currencyName,String regExpr,String isoCode)
	{
		
	}
	/**
	 * This method is used to verify and click on free games play later button
	 * @author pb61055
	 * @param report
	 * @param imageLibrary
	 * @param currencyName
	 */
	public void freeGamesPlayLater(Mobile_HTML_Report report,ImageLibrary imageLibrary,String currencyName)
	{
		
	}
	/**
	 * This method is used to verify and click on free games play now button
	 * @author pb61055
	 * @param report
	 * @param imageLibrary
	 * @param currencyName
	 */
	public void freeGamesPlayNow(Mobile_HTML_Report report,ImageLibrary imageLibrary,String currencyName)
	{
		
	}
	/**
	 * This method is used to verify delete button on FG's entry screen
	 * @author pb61055
	 * @param report
	 * @param imageLibrary
	 * @param currencyName
	 */
	public void freeGamesDeleteEntryScreen(Mobile_HTML_Report report,ImageLibrary imageLibrary,String currencyName)
	{
		
	}
	/**
	 * This method is used for free games delete on base game screen
	 * @author pb61055
	 * @param report
	 * @param imageLibrary
	 * @param currencyName
	 * @param regExpr
	 */
	public void freeGamesDeleteBaseGameScreen(Mobile_HTML_Report report,ImageLibrary imageLibrary,String currencyName)
	{
		
	}
	/**
	 * This method is used verify FG Summary screen currency format
	 * @author pb61055
	 * @param report
	 * @param imageLibrary
	 * @param currencyName
	 * @param regExpr
	 */
	public void verifyFGSummaryScreen(Mobile_HTML_Report report,ImageLibrary imageLibrary,String currencyName,String regExpr,String isoCode)
	{
		
	}
}
