package Modules.Regression.FunctionLibrary;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
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

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.FindFailed;
//import org.sikuli.script.Pattern;
//import org.sikuli.script.Region;
import org.sikuli.script.Screen;
import org.testng.annotations.Optional;

//import com.sun.glass.events.KeyEvent;
import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.driver.DesktopDriver;
import com.zensar.automation.framework.model.Coordinates;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
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
import io.appium.java_client.MobileElement;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.offset.PointOption;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarLog;

public class CFNLibrary_Desktop extends DesktopDriver {
	long Avgquickspinduration = 0;
	long Avgautoplayduration = 0;
	String amgWinAmt;

	public Logger log = Logger.getLogger(CFNLibrary_Desktop.class.getName());
	public WebElement ReminderPeriod;
	public WebElement Losslimit;
	public String Titlenew;
	public String newGames;
	public String Flag;
	public boolean visible;
	public boolean isScreenshotTaken;
	public boolean result;
	public String spinButton;
	public String message1;
	public String ret;
	public String para1;
	public String para2;
	public String timeLimitSessionReminder;
	public String sessionReminderCurrent;
	public String endSessionButton;
	public String continueButton;
	public String timeLimit;
	public String lossLimitSessionReminder;
	public String sessionBalanceSessionReminder;
	public String timeElapsedsessionreminder;
	public String sessionBalance;
	private double userwidth;
	private double userheight;
	private double userElementX;
	String osPlatform;
	private int Ty;
	private int Tx;
	public String selectedamout = " ";
	private int userElementY;
	String balance = null;
	String loyaltyBalance = null;
	String totalWin = null;
	int totalWinNew = 0;
	double totalWin1 = 0;
	int initialbalance1 = 0;
	String numline = null;
	int totalnumline = 0;
	int bal = 0;
	int previousBalance = 0;
	WebElement time = null;
	int newBalance = 0;
	int freegameremaining = 0;
	int freegamecompleted = 0;
	Properties OR = new Properties();
	WebDriverWait Wait;
	String GameDesktopName;
	Har nhar;
	HarLog hardata;
	List<HarEntry> entries;
	Iterator<HarEntry> itr;
	public WebDriver webdriver;
	public BrowserMobProxyServer proxy;
	public Desktop_HTML_Report repo1;
	public String ex;
	String jsResult1;
	public double minbet = 0.01;
	String jsResultFinal1;
	public String commonLanguageContent;
	public String responseLanguageContent;
	public String userName;
	public static final String UTF8_BOM = "\uFEFF";
	public String GameName;
	public Map<String, String> XpathMap;
	Util clickAt = new Util();
	

	public CFNLibrary_Desktop(WebDriver webdriver1, BrowserMobProxyServer browserproxy1, Desktop_HTML_Report report,
			String gameName) throws IOException {
		log.info("In Super class contructor...");
		webdriver = webdriver1;
		repo1 = report;
		Wait = new WebDriverWait(webdriver, 10);

		proxy = browserproxy1;
		try {
			this.GameName = gameName.trim();
			log.info("game name: " + GameName);
			String newGameurl = null;
			String testDataExcelPath = TestPropReader.getInstance().getProperty("TestData_Excel_Path");

			ExcelDataPoolManager excelpoolmanager = new ExcelDataPoolManager(testDataExcelPath);
			log.info("Reading test data from path: " + testDataExcelPath);

			Map<String, String> rowData1 = null;
			int rowcount1 = excelpoolmanager.rowCount(testDataExcelPath, GameName);
			XpathMap = new HashMap<>();
			for (int i = 1; i < rowcount1; i++) {
				rowData1 = excelpoolmanager.readExcelByRow(testDataExcelPath, GameName, i);
				String element = rowData1.get("Element").trim();
				String xpath = rowData1.get("Xpath").trim();
				XpathMap.put(element, xpath);
			}
			if (TestPropReader.getInstance().getProperty("GameDesktopUrl") != null) {

				String gameurl = TestPropReader.getInstance().getProperty("GameDesktopUrl");

				log.debug("Before encoding string is=" + gameurl);

				java.util.regex.Pattern p = Pattern.compile("\\\\u0026");
				Matcher m = p.matcher(gameurl);

				if (m.find()) {
					newGameurl = m.replaceAll("&");
				}
				log.info("Decoded Url=" + newGameurl);
				XpathMap.put("ApplicationURL", newGameurl);

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void maxiMizeBrowser() {
		// Maximize the window
		webdriver.manage().window().maximize();
	}

	/* game play error handling */
	public String Fun_gameplayerror() {
		String gameplay = null;
		try {

			gameplay = webdriver.findElement(By.xpath(XpathMap.get("Error"))).getText();
			// webdriver.findElement(By.xpath(XpathMap.get("spain_LobbyOK"))).click();

		} catch (Throwable t1) {
			t1.getMessage();
		}
		return gameplay;
	}

	public void wait_Bonus() {
		// Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("Bonus"))));
	}

	/*
	 * Author: Havish Jain Description: This function is used for get attribute data
	 * from the Har Parameter: String nodeValue ,String attribute1, String
	 * attribute2,String attribute3,String gameTitle
	 */
	public void verifyGameAnalytics() throws InterruptedException {
		try {
			for (LogEntry entry : webdriver.manage().logs().get(LogType.PERFORMANCE)) {
				if (entry.getMessage().contains("Network.dataReceived")) {
					Matcher dataLengthMatcher = java.util.regex.Pattern.compile("encodedDataLength\":(.*?),")
							.matcher(entry.getMessage());
					if (dataLengthMatcher.find()) {
						System.out.println(entry.getMessage());
						System.out.println(dataLengthMatcher.group());
						System.out.println();
					}
					// Do whatever you want with the data here.
				}
			}
			/*
			 * LogEntries logEntries = webdriver.manage().logs().get(LogType.BROWSER); if
			 * (!logEntries.getAll().isEmpty()){ System.out.println(
			 * "Console output from browser:" ); for (LogEntry logEntry : logEntries) {
			 * if((logEntry.getMessage()).contains("TestBackendConnector"))
			 * System.out.println("JS: " + logEntry.getLevel() + ": " +
			 * logEntry.getMessage()); } }
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Date: 26/6/2019 Author: Sneha Jawarkar Description: GTR Reelspin
	 */
	public boolean clickspintostop() {
		return false;
	}

	public void Reelspin_duration_during_win_loss() throws InterruptedException {
	}

	/*
	 * Date: 03/04/2017 Author: Ashish Kshatriya Description: This function used for
	 * click on Settings link in bargur menu Parameter: NA
	 */
	public boolean navigateSettings(String od) throws Exception {
		clickAt.clickAt(OR.getProperty("menubuttonPixels"), webdriver, "//*[@id='gameCanvas']");
		Thread.sleep(1000);
		clickAt.clickAt(OR.getProperty("settings"), webdriver, "//*[@id='gameCanvas']");
		Thread.sleep(1000);
		return true;
	}

	/*
	 * Date: 03/04/2017 Author: Ashish Kshatriya Description: This function used for
	 * take to Game in application Parameter: image path
	 */
	public boolean takeToGame(String amount) throws InterruptedException {
		webdriver.findElement(By.id(OR.getProperty("UserInput"))).sendKeys(amount);
		webdriver.findElement(By.id(OR.getProperty("Submit"))).click();
		Thread.sleep(3000);
		webdriver.findElement(By.id(OR.getProperty("Submit"))).click();
		return true;
	}

	/*
	 * Date: 24/04/2019 Description:To verify maximum count of spin. *Parameter: NA
	 * 
	 * @return boolean
	 */
	public boolean max_spin_chk() {
		boolean max_spin;
		try {
			Thread.sleep(5000);
			webdriver.findElement(By.id(XpathMap.get("AutoplayID"))).click();
			WebElement SizeSlider = webdriver.findElement(By.id(XpathMap.get("SpinSizeSlider_ID")));
			// String value1
			// =webdriver.findElement(By.id(XpathMap.get("SpinCount_ID"))).getText();
			Actions action = new Actions(webdriver);
			action.dragAndDropBy(SizeSlider, 600, 0).build().perform();
			log.debug("drag and drop performed");

			String value2 = webdriver.findElement(By.id(XpathMap.get("SpinCount_ID"))).getText();

			if (value2.equals("100")) {
				return max_spin = true;
			} else {
				return max_spin = false;
			}

		} catch (Exception e) {
			log.error("Session not over after autoplay spin", e);
			return false;
		}
	}

	/*
	 * Date: 04/05/2017 Author: Anish Description: This function Func_Login use to
	 * perform login operation Commented By :Kanchan Parameter: applicationName and
	 * url
	 */
	public String Func_Login(String userName, String password) {
		String Title = null;
		try {
			webdriver.findElement(By.xpath(OR.getProperty("GameUserName"))).clear();
			webdriver.findElement(By.xpath(OR.getProperty("GameUserName"))).sendKeys(userName);
			webdriver.findElement(By.xpath(OR.getProperty("GamePassword"))).clear();
			webdriver.findElement(By.xpath(OR.getProperty("GamePassword"))).sendKeys(password);
			webdriver.findElement(By.xpath(OR.getProperty("GameLogin"))).click();
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(OR.getProperty("canvasxPath"))));
			Title = webdriver.getTitle();
			System.out.println("The Title is " + Title);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Title;
	}

	public void errorLogout() {
		// Commenting below code as we are using direct url to launch the game
		/*
		 * try { Wait = new WebDriverWait(webdriver,30); boolean test =
		 * webdriver.findElements(By.xpath(XpathMap.get("spain_LobbyOK"))).size() > 0;
		 * 
		 * if (test) { func_Click(XpathMap.get("spain_LobbyOK")); Thread.sleep(2000); }
		 * 
		 * 
		 * Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.
		 * get("five_Reel_slot")))); func_Click(XpathMap.get("navigation_MyAccount"));
		 * Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.
		 * get("logout")))); func_Click(XpathMap.get("logout"));
		 * Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.
		 * get("Login")))); func_Click(XpathMap.get("closeButtonLogin")); } catch
		 * (Exception e) { log.error("error in logout", e); }
		 */
	}

	/*
	 * Date: 04/05/2017 Author: Anish Description: This function Func_Login use to
	 * perform login operation Parameter: username andpassword Commented By :Kanchan
	 */
	public String Func_LoginBaseScene(String username, String password) {
		Wait = new WebDriverWait(webdriver, 500);
		String Title = null;
		try {

			webdriver.findElement(By.xpath(XpathMap.get("userName"))).clear();
			webdriver.findElement(By.xpath(XpathMap.get("userName"))).sendKeys(username);
			webdriver.findElement(By.xpath(XpathMap.get("password"))).clear();
			webdriver.findElement(By.xpath(XpathMap.get("password"))).sendKeys(password);

			Thread.sleep(3000);
			webdriver.findElement((By.xpath(XpathMap.get("Login")))).click();

			log.debug("Clicked on Login button and waiting for game to load");
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("clock"))));
			// newFeature();
			// summaryScreen_Wait();
			log.debug("Game has loaded successfully ");
			Title = webdriver.getTitle();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Title;
	}

	public void newFeature() {
		try {
			webdriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			boolean test = webdriver.findElements(By.xpath(XpathMap.get("OneDesign_NewFeature_ClickToContinue")))
					.size() > 0;
			if (test) {
				func_Click(XpathMap.get("OneDesign_NewFeature_ClickToContinue"));
				Thread.sleep(2000);
			}

			else {

				System.out.println("Continue Button is not Displaying");
			}
		} catch (Exception e) {

		} finally {
			webdriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		}
	}

	public long Random_LoginBaseScene(String userName) throws Exception {
		long loadingTime = 0;
		try {
			webdriver.findElement(By.xpath(XpathMap.get("userName"))).clear();
			String password = XpathMap.get("Password");
			webdriver.findElement(By.xpath(XpathMap.get("userName"))).sendKeys(userName);
			webdriver.findElement(By.xpath(XpathMap.get("password"))).clear();
			webdriver.findElement(By.xpath(XpathMap.get("password"))).sendKeys(password);
			webdriver.findElement((By.xpath(XpathMap.get("Login")))).click();
			log.debug(
					"Clicked on login button after entering usename and password and waiting for in game clock to visible");
			long start = System.currentTimeMillis();
			new WebDriverWait(webdriver, 120)
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("clock"))));

			long finish = System.currentTimeMillis();
			long totalTime = finish - start;
			loadingTime = totalTime / 1000;
			log.debug("Total Time for Game load in Seconds is: " + loadingTime);

		} catch (NoSuchElementException e) {
			e.printStackTrace();
			log.debug("error while login");
			throw new Exception(e);
		}
		return loadingTime;
	}

	/*
	 * loading the game with direct url
	 */
	public boolean loadGame(String url) {
		boolean isGameLaunch = false;

		Wait = new WebDriverWait(webdriver, 120);

		try {
			webdriver.navigate().to(url);
			if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(XpathMap.get("TypeOfGame"))
					|| (Constant.YES.equalsIgnoreCase(XpathMap.get("continueBtnOnGameLoad")))) {
				Wait.until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath(XpathMap.get("OneDesign_NewFeature_ClickToContinue"))));
				isGameLaunch = true;

			} else {
				log.debug("Waiting for clock to be visible");
				Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("clock"))));
				log.debug(" clock is visible");
				isGameLaunch = true;
			}
			log.debug("game loaded  ");
		} catch (WebDriverException e) {
			log.debug("Exception occur in loadgame()");
			log.error(e.getMessage());
		}
		return isGameLaunch;
	}
	
	


	/* Sneha Jawarkar: Wait for Spin button */
	public boolean waitForSpinButtonstop() {
		return false;

	}

	public void quickspin_with_Basegame_freespin() throws InterruptedException {
	}

	/**
	 * This method is used to wait till Base scene loads Author: Sneha Jawarkar
	 */
	public boolean IsBaseScene() {
		return false;
	}

	/**
	 * Date: 26/6/2019 Author: Sneha Jawarkar Description: GTR Reelspin
	 * 
	 * @throws Exception
	 */
	public void Reelspin_in_All() throws Exception {
	}

	/**
	 * * Date: 14/05/2019 Author: Sneha Jawarkar. Description: This function is used
	 * in GTR_freegame Parameter: play letter
	 */
	public void ClickBaseSceneDiscardButton() {
	}

	/**
	 * * Date: 17/05/2019 Author: Sneha Jawarkar. Description: This function is used
	 * in GTR_freegame Parameter: resume play button
	 */
	public boolean ClickFreegameResumePlayButton() {
		boolean b = false;
		return b;
	}

	/**
	 * This method is used to wait till FS scene loads Author: Sneha Jawarkar
	 */
	public boolean FreeSpinEntryScene() {
		return false;
	}

	/*
	 * Date: 16/05/2019 Author:Sneha Jawarkar Description: Freegame_GTR Parameter:
	 * NA
	 */
	public boolean CompleteFreeGameOffer_freespin(int freegamescount) {
		return false;
	}

	/**
	 * Date: 21/6/2019 Author: Sneha Jawarkar Description: GTR Reelspin
	 * 
	 * @throws InterruptedException
	 */
	public void ReelSpin_differecne_Duration_Normalspin_quickspin() throws InterruptedException {
	}

	public long Reelspin_speed_During_quickspin() throws InterruptedException {
		return Avgquickspinduration;

	}

	public long averageReelSpinDuration() throws InterruptedException {
		return 0;
	}

	/**
	 * Date: 21/6/2019 Author: Sneha Jawarkar Description: GTR Reelspin
	 * 
	 * @throws InterruptedException
	 */
	public long Reelspin_speed_During_Autoplay() {
		long autoplayloadingTime = 0;
		return Avgautoplayduration;

	}

	/*
	 * Date: 16/05/2019 Author:Sneha Jawarkar Description: Freegame_GTR Parameter:
	 * NA
	 */
	public boolean CompleteFreeGameOffer(int freegamescount) {
		return false;

	}

	/**
	 * * Date: 14/05/2019 Author: Sneha Jawarkar. Description: This function is used
	 * in GTR_freegame Parameter: play letter
	 */
	public boolean clickOnPlayLater() {
		boolean b = false;
		return b;

	}

	/**
	 * Date:15/5/19 Author:Sneha Jawarkar GTR_Freegame purpose
	 */

	public void Backtogame_centerclick() {
	}

	/**
	 * Date: 29/05/2018 Author: Havish Jain Description: This function is used to
	 * refresh the page and will take screenshot of splash screen before navigating
	 * to Base Scene. Parameter:
	 */
	public String splashScreen(Desktop_HTML_Report report, String language) {
		Wait = new WebDriverWait(webdriver, 120);

		try {
			webdriver.navigate().refresh();
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("preLoaderBackground"))));
			report.detailsAppend("verify the Splash Screen", "Splash Screen should display",
					"Splash screen is displaying", "pass");
			Thread.sleep(2000);

			log.debug("Refreshed the game and taken screenshot of splash screen");
			if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(XpathMap.get("TypeOfGame"))
					|| (Constant.YES.equalsIgnoreCase(XpathMap.get("continueBtnOnGameLoad")))) {
				Wait.until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath(XpathMap.get("OneDesign_NewFeature_ClickToContinue"))));
			} else {
				log.debug("Waiting for clock to be visible");
				Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("clock"))));
				log.debug("Waiting for clock is visible");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Date: 30/05/2018 Autohr: Havish Jain Description: This function used to
	 * launch game with Practice Play
	 * 
	 * @return Title
	 */
	public String func_LoginPracticePlay() {
		Wait = new WebDriverWait(webdriver, 500);
		String Title = null;
		try {
			func_Click(XpathMap.get("practice_Play"));
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("clock"))));
			Title = webdriver.getTitle();
			newFeature();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Title;
	}

	public double round(double number) {
		DecimalFormat dnf = new DecimalFormat("#.##");
		double roundednumber = new Double(dnf.format(number));
		return roundednumber;
	}

	/*
	 * Date: 04/05/2017 Author: Anish Description: This function Func_Login use to
	 * perform login operation Parameter: username andpassword Commented By :Kanchan
	 */
	public String Func_LoginBaseScene_Spain(String username, String password) {
		Wait = new WebDriverWait(webdriver, 500);
		String Title = null;
		try {
			webdriver.findElement(By.xpath(OR.getProperty("GameUserName"))).clear();
			webdriver.findElement(By.xpath(OR.getProperty("GameUserName"))).sendKeys(username);
			webdriver.findElement(By.xpath(OR.getProperty("GamePassword"))).clear();
			webdriver.findElement(By.xpath(OR.getProperty("GamePassword"))).sendKeys(password);
			webdriver.findElement(By.xpath(OR.getProperty("GameLogin"))).click();
			Thread.sleep(10000);
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(OR.getProperty("canvasxPath"))));
			Title = webdriver.getTitle();
			System.out.println("The Title is " + Title);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Title;
	}

	/*
	 * Date: 04/04/2017 Author: Anish Gadgil Description: Function Navigate url for
	 * Language Test scripts Parameter: applicationName and urlNavigate
	 */
	public String func_navigate(String urlNavigate) {
		Wait = new WebDriverWait(webdriver, 200);
		try {
			webdriver.navigate().to(urlNavigate);

			log.debug("navigated the url and  waiting for five_Reel_slot to be visible");
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("five_Reel_slot"))));
			// Thread.sleep(8000);

			if (webdriver.getPageSource().contains("Arthur")) {
				log.debug("Inside Microgaming Skin fuctionality");
				Actions actions = new Actions(webdriver);
				WebElement elementLocator = webdriver.findElement(By.xpath(XpathMap.get("GameName_Arthur")));
				actions.doubleClick(elementLocator).perform();
				Thread.sleep(2000);
				/*
				 * if
				 * (webdriver.findElement(By.xpath(XpathMap.get("PlayNow_Arthur"))).isDisplayed(
				 * )) {
				 * webdriver.findElement(By.xpath(XpathMap.get("GameName_Arthur"))).click(); }
				 */

			} else {
				log.debug("Inside Mint Skin block");
				GameDesktopName = webdriver.findElement(By.id(GameName)).getText();
				webdriver.findElement(By.id(GameName)).click();

			}
			log.debug("Clicked on game and waiting for Login button to visible");
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("Login"))));
		} catch (Exception e) {
			log.error("error in func_navigate", e);
		}
		return GameDesktopName;
	}
	/*
	 * public String func_navigate(String urlNavigate ){ Wait=new
	 * WebDriverWait(webdriver,500); try { webdriver.navigate().to(urlNavigate);
	 * 
	 * log.debug(
	 * "navigated the url and  waiting for five_Reel_slot to be visible");
	 * Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
	 * XpathMap.get("five_Reel_slot")))); Thread.sleep(4000);
	 * System.out.println("Url  "+webdriver.getCurrentUrl());
	 * //System.out.println("Canvaaxpath"+OR.getProperty("canvasxPath"));
	 * GameDesktopName=webdriver.findElement(By.id("GameName")).getText();
	 * //Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
	 * XpathMap.get("GameNamenew"))));
	 * //GameDesktopName=webdriver.findElement(By.xpath(XpathMap.get(
	 * "GameNamenew"))).getText();
	 * 
	 * System.out.println(GameDesktopName);
	 * webdriver.findElement(By.id(XpathMap.get("GameName"))).click();
	 * //webdriver.findElement(By.xpath(XpathMap.get("GameNamenew"))).click();
	 * //webdriver.findElement(By.id("breakDaBankAgain")).click(); log.debug(
	 * "Clicked on game and waiting for Login button to visible");
	 * Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
	 * XpathMap.get("Login")))); } catch(Exception e){ log.error(
	 * "error in func_navigate", e); } return GameDesktopName; }
	 */

	public String func_navigate1(String urlNavigate) {
		Wait = new WebDriverWait(webdriver, 500);
		try {
			webdriver.navigate().to(urlNavigate);

			log.debug("navigated the url and  waiting for five_Reel_slot to be visible");
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("five_Reel_slot"))));
			Thread.sleep(4000);

			FluentWait<WebDriver> wait1 = new FluentWait<WebDriver>(webdriver).withTimeout(Duration.ofMinutes(2))
					.pollingEvery(Duration.ofSeconds(5)).ignoring(NoSuchElementException.class);

			// wait1.until(arg0)
			{

			}

			GameDesktopName = webdriver.findElement(By.xpath(XpathMap.get("GameName1"))).getText();
			webdriver.findElement(By.xpath(XpathMap.get("GameName1"))).click();
			log.debug("Clicked on game and waiting for Login button to visible");
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("Login"))));
		} catch (Exception e) {
			log.error("error in func_navigate", e);
		}
		return GameDesktopName;
	}

	/*
	 * Date: 5/05/2019 Description:To verify Autoplay after free spin Parameter: NA
	 * 
	 * @return boolean
	 */

	public boolean is_autoplay_with_freespin(Desktop_HTML_Report gtr_autoplay) {
		try {
			webdriver.findElement(By.id(XpathMap.get("AutoplayID"))).click();
			webdriver.findElement(By.xpath(XpathMap.get("Start_Autoplay"))).click();

			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("spin_button"))));
			log.debug("Free spin over");
		} catch (Exception e) {
			log.error("Free Spins are not completed", e);
		}
		return true;
	}

	/*
	 * Date: 22/04/2019 Description:To verify Autoplay is avialable or not in game
	 * Parameter: NA
	 * 
	 * @return boolean
	 */
	public boolean ISAutoplayAvailable() {
		boolean autoplay;
		try {
			autoplay = webdriver.findElement(By.id(XpathMap.get("AutoplayID"))).isDisplayed();
		} catch (Exception e) {
			log.error("Autoplay id button is not visible", e);
			autoplay = false;
		}
		return autoplay;
	}
	/*
	 * Date: 17/05/2019 Author:Snehal Gaikwad Description: To set bet to maximum
	 * Parameter: NA
	 */

	public String setMaxBet() {
		String betAmount = null;
		try {
			String bet = "return mgs.mobile.casino.slotbuilder.v1.automation.getServiceById('BetService').setMaxBet()";
			getConsoleText(bet);

			betAmount = getConsoleText(
					"return mgs.mobile.casino.slotbuilder.v1.automation.getControlById('BetPanelComponent').Text.betSizeValueText._text");
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
	public boolean is_Loss_Limit_Lower() {
		boolean flag = false;
		try {
			webdriver.findElement(By.id(XpathMap.get("Cancel_Autoplay"))).click();
			Thread.sleep(2000);
			setMaxBet();
			getConsoleText(
					"return mgs.mobile.casino.slotbuilder.v1.automation.getControlById('AutoplayOptionsComponent').onButtonClicked('autoplayOpenMobileButton')");
			webdriver.findElement(By.id(XpathMap.get("Start_Autoplay"))).click();
			log.debug("Loss limit is less than current bet error message display");
			flag = true;
		} catch (Exception e) {
			log.error("Loss limit is less than current bet error message not display");
			flag = false;
		}
		return flag;
	}
	/*
	 * Date: 20/05/2019 Author:Snehal Gaikwad Description: To Check whether the Loss
	 * Limit is reach or not Parameter: NA
	 */

	public boolean is_Loss_Limit_Reached(Desktop_HTML_Report report) {
		boolean flag = false;
		try {
			webdriver.findElement(By.id(XpathMap.get("Cancel_Autoplay"))).click();
			getConsoleText(
					"return mgs.mobile.casino.slotbuilder.v1.automation.getServiceById('BetService').betModel.setBetValue(5000)");
			Thread.sleep(2000);
			getConsoleText(
					"return mgs.mobile.casino.slotbuilder.v1.automation.getControlById('AutoplayOptionsComponent').onButtonClicked('autoplayOpenMobileButton')");
			WebElement losslimit = webdriver.findElement(By.id(XpathMap.get("lossLimit")));
			Select limits = new Select(losslimit);
			limits.selectByValue("10000");
			Thread.sleep(2000);
			webdriver.findElement(By.id(XpathMap.get("Start_Autoplay"))).click();
			while (true) {
				if (!GetConsoleBooleanText(
						"return mgs.mobile.casino.slotbuilder.v1.automation.getServiceById('AutoplayService').canAutoplayContinue().autoPlayContinue")) {
					Thread.sleep(2000);
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

	public boolean is_Win_Limit_Reached(Desktop_HTML_Report gtr_autoplay) {
		boolean flag = false;
		try {
			webdriver.findElement(By.id(XpathMap.get("Cancel_Autoplay"))).click();
			getConsoleText(
					"return mgs.mobile.casino.slotbuilder.v1.automation.getServiceById('BetService').betModel.setBetValue(2500)");
			Thread.sleep(2000);
			getConsoleText(
					"return mgs.mobile.casino.slotbuilder.v1.automation.getControlById('AutoplayOptionsComponent').onButtonClicked('autoplayOpenMobileButton')");
			WebElement losslimit = webdriver.findElement(By.xpath(XpathMap.get("Loss_Limit")));
			Select limits = new Select(losslimit);
			limits.selectByValue("10000");

			webdriver.findElement(By.id(XpathMap.get("Start_Autoplay"))).click();
			while (true) {
				if (GetConsoleBooleanText(
						"return mgs.mobile.casino.slotbuilder.v1.automation.getServiceById('AutoplayService').canAutoplayContinue().autoPlayContinue") == false) {
					Thread.sleep(2000);
					clickAtButton(
							"return mgs.mobile.casino.slotbuilder.v1.automation.getControlById('DialogComponent').onButtonClicked('primaryOnlyButton')");
					log.debug("Clicked on Loss Limit reached dailog  ");
					flag = true;
					break;
				}
				continue;
			}
		} catch (Exception e) {
			log.error("Loss limit is not reached");
			flag = false;

		}
		return flag;
	}

	/*
	 * Date: 22/04/2019 Description:To verify Autoplay with quick spin on Parameter:
	 * NA
	 * 
	 * @return boolean
	 */
	public void autoPlay_with_QS_On(Desktop_HTML_Report report) {
		boolean qS_Test = false;
		try {
			Thread.sleep(5000);

			WebElement qsoffele = webdriver.findElement(By.xpath(XpathMap.get("QuickSpin_Off")));
			boolean qsoff = qsoffele.isDisplayed();

			if (qsoff) {
				qsoffele.click();

				boolean qson = webdriver.findElement(By.xpath(XpathMap.get("QuickSpin_On"))).isDisplayed();
				if (qson) {
					webdriver.findElement(By.id(XpathMap.get("AutoplayID"))).click();
					qS_Test = true;
					log.debug("Clicked on autoplay");
				} else {
					qS_Test = false;
				}

			}
		} catch (Exception e)

		{
			log.error("Autoplay is  not clickable with Quick Spin on", e);
		}

	}
	/*
	 * Date: 22/04/2019 Description:To verify Autoplay spin selection Parameter: NA
	 * 
	 * @return boolean
	 */

	public boolean autoplay_spin_selection() {
		boolean spin_autoplay;
		try {
			// webdriver.findElement(By.id(XpathMap.get("AutoplayID"))).click();
			WebElement CoinSizeSlider = webdriver.findElement(By.id(XpathMap.get("SpinSizeSlider_ID")));
			String value1 = webdriver.findElement(By.id(XpathMap.get("SpinCount_ID"))).getText();
			Actions action = new Actions(webdriver);
			action.dragAndDropBy(CoinSizeSlider, 200, 0).build().perform();
			log.debug("drag and drop performed");

			String value2 = webdriver.findElement(By.id(XpathMap.get("SpinCount_ID"))).getText();

			if (value1.equals(value2)) {
				return spin_autoplay = false;
			} else {
				return spin_autoplay = true;
			}

		} catch (Exception e) {
			log.error("Spin count not getting change.", e);
			return spin_autoplay = true;

		}

	}

	/*
	 * Date: 24/04/2019 Description:To verify Autoplay spin session stop Parameter:
	 * NA
	 * 
	 * @return boolean
	 */
	public boolean is_autoplay_session_end() {

		boolean spin_session;

		try {

			WebElement SizeSlider = webdriver.findElement(By.id(XpathMap.get("SpinSizeSlider_ID")));
			// String value1
			// =webdriver.findElement(By.id(XpathMap.get("SpinCount_ID"))).getText();

			Actions action = new Actions(webdriver);
			action.dragAndDropBy(SizeSlider, -500, 0).build().perform();

			webdriver.findElement(By.xpath(XpathMap.get("Start_Autoplay"))).click();

			log.debug("Clicked Auto paly..");
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(XpathMap.get("Autoplay_active"))));
			log.debug("Autoplay is Active Now");

		} catch (Exception e) {
			log.error("Session not over after autoplay spin", e);
			return spin_session = false;
		}
		return spin_session = true;
	}

	public void clickAtButton(String button) {
		JavascriptExecutor js = ((JavascriptExecutor) webdriver);
		js.executeScript(button);
	}

	public void is_autoplay_setting(Desktop_HTML_Report report) {
		try {
		} catch (Exception e) {
		}

	}

	/*
	 * 
	 * */
	public boolean IsAutoplayAllMode() {
		boolean flag = ISAutoplayAvailable();
		if (flag) {
			func_LoginPracticePlay();
			ISAutoplayAvailable();
		}
		return flag;
	}

	/*
	 * Date: 03/04/2017 Author: Kanchan Badhiye Description: verifyTime
	 * functionality for verifying time Parameter: NA
	 */
	public String verifyTime() throws InterruptedException {
		Wait = new WebDriverWait(webdriver, 120);
		String Time = null;
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(OR.getProperty("canvasxPath"))));
			time = webdriver.findElement(By.xpath(OR.getProperty("GameTime")));
			Time = time.getText();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Time;
	}

	/**
	 * Date: 25/10/2017 Author: Kamal Kumar Vishwakarma Description: This function
	 * is used for get Data from the Common Language File provided by Derivco
	 */
	public void getCommonLanguageFile(String language) throws IOException {

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
	 * Date: 04/05/2017 Author:Kanchan Badhiye Description: verify_Win_Amount use to
	 * verify TotalWin Parameter: nodeValue, attributes
	 */
	public double verify_Win_Amount(String nodeValue, String attribute1, String attribute2, String attribute3)
			throws InterruptedException {
		Wait = new WebDriverWait(webdriver, 180);
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(OR.getProperty("canvasxPath"))));
			clickAt.clickAt(OR.getProperty("spinButtonPixels"), webdriver, "//*[@id='gameCanvas']");
			Thread.sleep(15000);
			String DataFromHar = clickAt.getData(nodeValue, attribute1, attribute2, attribute3, proxy);
			balance = DataFromHar.split(",")[0];
			loyaltyBalance = DataFromHar.split(",")[1];
			totalWin = DataFromHar.split(",")[2];
			totalWin1 = Double.parseDouble(totalWin);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Thread.sleep(2000);
		return totalWin1;
	}

	/*
	 * Date: 04/05/2017 Author:Kanchan Badhiye Description:
	 * verify_Win_Amount_QuickSpin use to verify win amount for quick spin
	 * Parameter: nodeValue, attributes
	 */
	public int verify_Win_Amount_QuickSpin(String nodeValue, String attribute1, String attribute2, String attribute3)
			throws InterruptedException {
		Wait = new WebDriverWait(webdriver, 120);
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(OR.getProperty("canvasxPath"))));
			clickAt.clickAt(OR.getProperty("menubuttonPixels"), webdriver, "//*[@id='gameCanvas']");
			Thread.sleep(500);
			// Clicking on setting
			clickAt.clickAt(OR.getProperty("settings"), webdriver, "//*[@id='gameCanvas']");
			Thread.sleep(500);
			// Clicking on Quick Spin
			clickAt.clickAt(OR.getProperty("quickspin"), webdriver, "//*[@id='gameCanvas']");
			Thread.sleep(500);
			// Clickin on Back to Game Button
			clickAt.clickAt(OR.getProperty("settingsbackbutton"), webdriver, "//*[@id='gameCanvas']");
			Thread.sleep(500);
			// Clicking on extended Menu Button
			clickAt.clickAt(OR.getProperty("menubuttonextended"), webdriver, "//*[@id='gameCanvas']");
			Thread.sleep(500);
			// Clicking on Spin button
			clickAt.clickAt(OR.getProperty("spinButtonPixels"), webdriver, "//*[@id='gameCanvas']");
			Thread.sleep(500);
			String DataFromHar = clickAt.getData(nodeValue, attribute1, attribute2, attribute3, proxy);
			balance = DataFromHar.split(",")[0];
			loyaltyBalance = DataFromHar.split(",")[1];
			totalWin = DataFromHar.split(",")[2];
			totalWinNew = Integer.parseInt(totalWin);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Thread.sleep(5000);
		return totalWinNew;
	}

	public boolean menubutton() throws InterruptedException {
		try {
			clickAt.clickAt(OR.getProperty("bargurMenu"), webdriver, "//*[@id='gameCanvas']");
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Thread.sleep(4000);
		return true;
	}

	/*
	 * Date: 04/04/2017 Author: Anish Gadgil Description: This function will open
	 * the settings page. Parameter: No parameters required.
	 */
	public boolean verifySettings() throws InterruptedException {
		boolean image = webdriver.findElements(By.id(OR.getProperty("canvasxPath"))).size() > 0;
		try {
			if (image) {
				clickAt.clickAt(OR.getProperty("bargurMenu"), webdriver, "//*[@id='gameCanvas']");
				Thread.sleep(500);
				clickAt.clickAt(OR.getProperty("settings"), webdriver, "//*[@id='gameCanvas']");
				Thread.sleep(500);
			} else {
			}
		} catch (ElementNotVisibleException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/*
	 * Date: 18/04/2017 Author: Ashish Kshatriya Description: This function will
	 * expand the bargur menu. Parameter: No parameters required.
	 */
	public boolean verifyExpandBargurMenu() throws InterruptedException {
		boolean image = webdriver.findElements(By.id(OR.getProperty("canvasxPath"))).size() > 0;
		try {
			if (image) {
				clickAt.clickAt(OR.getProperty("bargurMenu"), webdriver, "//*[@id='gameCanvas']");
				Thread.sleep(500);
			} else {
			}
		} catch (ElementNotVisibleException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/*
	 * Date: 18/04/2017 Author: Ashish Kshatriya Description: This function will
	 * closed the bargur menu. Parameter: No parameters required.
	 */
	public boolean verifyClosedBargurMenu() throws InterruptedException {
		boolean image = webdriver.findElements(By.id(OR.getProperty("canvasxPath"))).size() > 0;
		try {
			if (image) {
				clickAt.clickAt(OR.getProperty("closeHamBurgurMenu"), webdriver, "//*[@id='gameCanvas']");
				Thread.sleep(500);
			} else {
			}
		} catch (ElementNotVisibleException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/*
	 * Date: 04/05/2017 Author:Kanchan Badhiye Description: verifySpinBeforeClick()
	 * use to verify balance amount Parameter: nodeValue, attributes
	 */
	public String verifySpinBeforeClick() throws InterruptedException {

		// Wait=new WebDriverWait(webdriver,120);
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(OR.getProperty("canvasxPath"))));
			Thread.sleep(10000);
			String outputData = clickAt.getData("Player", "balance", "", "", proxy);
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
	 * Date: 25/04/2019 Description:To verify Auto play on refreshing Parameter: NA
	 * 
	 * @return boolean
	 */
	public boolean isAutoplayOnAfterRefresh() {
		boolean onrefresh;
		try {
			webdriver.findElement(By.xpath(XpathMap.get("Start_Autoplay"))).click();
			Thread.sleep(3000);
			webdriver.navigate().refresh();
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("spin_button"))));
			log.debug("On refresh previous autoplay session has not resume");
		} catch (Exception e) {
			log.error("On refresh previous autoplay session has resume");
			return onrefresh = false;
		}

		return onrefresh = true;
	}

	/*
	 * Date: 04/05/2017 Author:Kanchan Badhiye Description: paylinesExist use to
	 * verify paylines Parameter: nodeValue, attributes
	 */
	public int paylinesExist() throws InterruptedException {
		try {
			// clickAt.clickAt(OR.getProperty("spinButtonPixels"),webdriver,"");
			String DataFromHar = clickAt.getData("VisArea", "numPaylines", "", "", proxy);
			numline = DataFromHar.split(",")[0];
			System.out.println(numline + ":" + DataFromHar);
			totalnumline = Integer.parseInt(numline);
			System.out.println("The total paylines are " + totalnumline);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Thread.sleep(2000);
		return totalnumline;
	}

	/**
	 * Date:07/12/2017 Author:premlata mishra actually not necessery in component
	 * store just declaration needed
	 * 
	 * @return true
	 */
	public void waitTillStop() throws InterruptedException {

	}

	public String symbol_Value(String locator) {
		String value = Get_Amount(locator);
		return value;
	}

	/**
	 * Date:07/12/2017 Author:premlata Mishra This method is to get any element
	 * value
	 * 
	 * @return true
	 */
	public String Get_Amount(String locator) {
		/*
		 * Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
		 * locator))); String Value=func_GetText(locator); String
		 * Value1=func_String_Operation(Value);
		 */
		return null;
	}

	public String GetWinAmt() {
		return null;
	}

	/*
	 * Date: 04/05/2017 Author:Kanchan Badhiye Description: verifycredit use to
	 * verify credit amounts Parameter: nodeValue, attributes
	 */
	public String verifyCredit() {

		Wait = new WebDriverWait(webdriver, 120);
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(OR.getProperty("canvasxPath"))));
			clickAt.clickAt(OR.getProperty("spinButtonPixels"), webdriver, "//*[@id='gameCanvas']");
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
	 * Date: 04/05/2017 Author:Kanchan Badhiye Description:
	 * validate_credits_firstSpin use to verify credit amounts afer first spin
	 * Parameter: nodeValue, attributes
	 */
	public String validate_credits_firstSpin() throws InterruptedException {
		Wait = new WebDriverWait(webdriver, 120);
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(OR.getProperty("canvasxPath"))));
			clickAt.clickAt(OR.getProperty("spinButtonPixels"), webdriver, "//*[@id='gameCanvas']");
			Thread.sleep(5000);
			String DataFromHar = clickAt.getData("Player", "balance", "", "", proxy);
			balance = DataFromHar.split(",")[0];
			// previousBalance=Integer.parseInt(balance);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Thread.sleep(2000);
		return balance;
	}

	/*
	 * Date: 04/05/2017 Author:Kanchan Badhiye Description:
	 * validate_credits_firstSpin use to verify credit amounts afer first spin
	 * Parameter: nodeValue, attributes
	 */
	public int validate_credits_firstSpinCS() throws InterruptedException {
		Wait = new WebDriverWait(webdriver, 120);
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(OR.getProperty("canvasxPath"))));
			clickAt.clickAt(OR.getProperty("spinButtonPixels"), webdriver, "//*[@id='gameCanvas']");
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
	 * Date: 04/05/2017 Author:Kanchan Badhiye Description:
	 * validate_credits_firstSpin use to verify credit amounts afer next spin
	 * Parameter: nodeValue, attributes
	 */
	public String validate_credits_nextSpin() throws InterruptedException {
		Wait = new WebDriverWait(webdriver, 120);
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(OR.getProperty("canvasxPath"))));
			clickAt.clickAt(OR.getProperty("spinButtonPixels"), webdriver, "//*[@id='gameCanvas']");
			Thread.sleep(5000);
			String DataFromHar = clickAt.getData("Player", "balance", "", "", proxy);
			balance = DataFromHar.split(",")[0];
			// newBalance = Integer.parseInt(balance);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Thread.sleep(2000);
		return balance;
	}

	/*
	 * Date: 30/03/2017 Author:Anish Gadgil Description:This function is used to
	 * logout from the game. Parameter: No parameters are required
	 */
	public String Func_logout(String onedesign) {
		String loginTitle = null;
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(OR.getProperty("canvasxPath"))));
			// clickAt.clickAt(OR.getProperty("quickspin"),webdriver,"");
			// Thread.sleep(5000);
			// clickAt.clickAt(OR.getProperty("bargurMenu"),webdriver,"");//
			// Clicking on hamburger menu
			// clickAt.clickAt(OR.getProperty("bargurMenu"),webdriver,""); //
			// Clicking on hamburger menu
			clickAt.clickAt(OR.getProperty("bargurMenu"), webdriver, "//*[@id='gameCanvas']"); // Clicking
																								// on
																								// hamburger
																								// menu

			Thread.sleep(1000);
			clickAt.clickAt(OR.getProperty("gameLink"), webdriver, "//*[@id='gameCanvas']"); // Clicking
																								// on
																								// Games
																								// menu
			Thread.sleep(15000);
			waitForPageToBeReady();
			// Wait.until(ExpectedConditions.elementToBeClickable(By.id(OR.getProperty("accountsLobbyID"))));
			Wait.until(ExpectedConditions.elementToBeClickable(By.id("navigation_myaccount")));

			Thread.sleep(2000);
			webdriver.findElement(By.id("navigation_myaccount")).click(); // Clickin
																			// on
																			// Accounts
			Thread.sleep(1000);
			Wait.until(ExpectedConditions.elementToBeClickable(By.id(OR.getProperty("logoutbuttonId"))));
			Thread.sleep(1000);
			webdriver.findElement(By.id(OR.getProperty("logoutbuttonId"))).click(); // Clicking
																					// on
																					// log
																					// out
																					// button
			// Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(OR.getProperty("LoginTitleid"))));
			Wait.until(ExpectedConditions.elementToBeClickable(By.id(OR.getProperty("closeButtonid"))));
			Thread.sleep(1000);
			loginTitle = webdriver.findElement(By.id(OR.getProperty("LoginTitleid"))).getText(); // Clicking
																									// on
																									// log
																									// out
																									// button
			webdriver.findElement(By.id(OR.getProperty("closeButtonid"))).click(); // Clicking
																					// on
																					// log
																					// out
																					// button
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginTitle;
	}

	/*
	 * Date: 03/04/2017 Author: Kanchan Badhiye Description:
	 * freegamebalnceverifybeforespin() use for calculation of balance for free
	 * games Parameter: NA
	 */
	public int freegamebalnceverifybeforespin(String nodeValue, String attribute1, String attribute2, String attribute3)
			throws Exception {
		try {
			clickAt.clickAt(OR.getProperty("freeGamesPlayNow"), webdriver, "//*[@id='gameCanvas']");
			Thread.sleep(1000);
			String DataFromHar = clickAt.getData(nodeValue, attribute1, attribute2, attribute3, proxy);
			balance = DataFromHar.split(",")[0];
			initialbalance1 = Integer.parseInt(balance);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Thread.sleep(4000);
		return initialbalance1;
	}

	public int verify_balance_QuickSpin(String nodeValue, String attribute1, String attribute2, String attribute3)
			throws Exception {
		try {
			clickAt.clickAt(OR.getProperty("spinButton"), webdriver, "//*[@id='gameCanvas']");
			Thread.sleep(1000);
			String DataFromHar = clickAt.getData(nodeValue, attribute1, attribute2, attribute3, proxy);
			balance = DataFromHar.split(",")[0];
			initialbalance1 = Integer.parseInt(balance);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Thread.sleep(4000);
		return initialbalance1;
	}

	/*
	 * Date: 03/04/2017 Author: Kanchan Badhiye Description:
	 * freegamebalnceverifyafterspin() use for calculation of balance for free games
	 * Parameter: NA
	 */
	public int freegamebalnceverifyafterspin(String nodeValue, String attribute1, String attribute2, String attribute3)
			throws InterruptedException {
		try {
			clickAt.clickAt(OR.getProperty("spinButtonPixels"), webdriver, "//*[@id='gameCanvas']");
			Thread.sleep(1000);
			String DataFromHar = clickAt.getData(nodeValue, attribute1, attribute2, attribute3, proxy);
			balance = DataFromHar.split(",")[0];
			totalWinNew = Integer.parseInt(balance);
			return totalWinNew;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Thread.sleep(4000);
		return totalWinNew;
	}

	/*
	 * Date: 03/04/2017 Author: Kanchan Badhiye Description: refreshscreen() use for
	 * refreshing current url Parameter: NA
	 */

	public String clickAt(String userdata) {
		String[] coordinates1 = userdata.split(",");
		userwidth = Integer.parseInt(coordinates1[0]);
		userheight = Integer.parseInt(coordinates1[1]);
		userElementX = Integer.parseInt(coordinates1[2]);
		userElementY = Integer.parseInt(coordinates1[3]);
		Actions act = new Actions(webdriver);
		WebElement ele1 = webdriver.findElement(By.xpath("//*[@id='gameCanvas']"));
		double deviceHeight = ele1.getSize().height;
		double deviceWidth = ele1.getSize().width;
		Tx = (int) ((deviceWidth / userwidth) * userElementX);
		Ty = (int) ((deviceHeight / userheight) * userElementY);
		System.out.println("TX value:" + Tx + "Ty value:" + Ty);
		act.moveToElement(ele1, Tx, Ty).click().build().perform();
		return Tx + "," + Ty;
	}

	public boolean customeclickimage(String imagepath) {
		Screen S = new Screen();
		org.sikuli.script.Pattern image = new org.sikuli.script.Pattern(imagepath);
		System.out.println("get image" + image);
		try {
			S.click(image, 10);
		} catch (FindFailed e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public String entryScreen_Wait(String clickToContinue) {
		String wait = "";
		return wait;
	}

	public String clickToContinue() {
		String FreeSpin = "";
		return FreeSpin;
	}

	public String FS_Credits() {
		// func_Click(XpathMap.get("FreeSpin_ClickToContinue"));
		String FS_Credits = "";
		return FS_Credits;
	}

	public String summaryScreen_Wait() {
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("clock_ID"))));
		return "";

	}

	public void FS_continue() {
		webdriver.findElement(By.className("labelFS")).click();
	}

	public String FS_RefreshWait() {
		// Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("FreeSpin_Credits"))));
		String wait = "";
		return wait;
	}

	public void jackpotSceneWait() {
	}

	public boolean elementWait(String element, String currentSceneText) {

		while (true) {
			String currentScene = getConsoleText(element);
			if (currentScene != null && currentScene.equalsIgnoreCase(currentSceneText)) {
				return true;
			}
		}
	}

	
	public boolean spinclick() throws InterruptedException {
		try {
			ClickByCoordinates("return " + XpathMap.get("SpinBtnCoordinatex"), "return " + XpathMap.get("SpinBtnCoordinatey"));
			Thread.sleep(10000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Thread.sleep(4000);
		return true;
	}

	public String capturePaytableScreenshot(Desktop_HTML_Report report, String languageCode) throws Exception {
		String paytable = "";
		return paytable;
	}

	public void paytableClose() {
		/*
		 * webdriver.findElement(By.xpath(XpathMap.get("PaytableBack"))).click() ;
		 * Wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XpathMap.
		 * get("OneDesignMenuClose"))));
		 * webdriver.findElement(By.xpath(XpathMap.get("OneDesignMenuClose"))). click();
		 */
	}

	public void bigWin_Wait() {
		// Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("OD_BIgWin"))));
	}

	public boolean Helpclick() throws InterruptedException {
		try {
			clickAt.clickAt(OR.getProperty("spinButtonPixels"), webdriver, "");
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();// Close_Popup
		}
		Thread.sleep(4000);
		return true;
	}

	/*
	 * Date: 04/04/2017 Author: Kanchan Badhiye Description: spinclick() click on
	 * spin once
	 */
	public boolean multiplespinclick() {
		try {
			int gameremaining = 0;
			int i = 0;
			gameremaining = games_remainingforfreegame("CurrentGame", "gamesRemaining", "", "");
			// while(gameremaining!=0)
			System.out.println("inside multiplespinclick" + gameremaining);
			System.out.println("games remaining" + gameremaining);
			for (i = gameremaining; i > 0; i--) {
				clickAt.clickAt(OR.getProperty("spinButtonPixels"), webdriver, "");
				Thread.sleep(5000);
				System.out.println("spin button clicked" + i);
				gameremaining = games_remainingforfreegame("CurrentGame", "gamesRemaining", "", "");
				if (gameremaining == 0) {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/*
	 * Date: 03/04/2017 Author: Kanchan Badhiye Description:
	 * games_remainingforfreegame() use for calculation of game remaining for free
	 * games Parameter: nodeValue,and attribute values
	 */
	public int games_remainingforfreegame(String nodeValue, String attribute1, String attribute2, String attribute3)
			throws InterruptedException {
		Wait = new WebDriverWait(webdriver, 120);
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(OR.getProperty("canvasxPath"))));
			clickAt.clickAt(OR.getProperty("spinButtonPixels"), webdriver, "");
			Thread.sleep(1000);
			String DatafromHar = clickAt.getData(nodeValue, attribute1, attribute2, attribute3, proxy);
			String gamesremainig = DatafromHar.split(",")[0];
			freegameremaining = Integer.parseInt(gamesremainig);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Thread.sleep(4000);
		return freegameremaining;
	}

	/*
	 * Date: 03/04/2017 Author: Kanchan Badhiye Description:
	 * games_completeforfreegame() use for calculation of game complete for free
	 * games Parameter: nodeValue,and attribute values
	 */
	public int games_completeforfreegame(String nodeValue, String attribute1, String attribute2, String attribute3)
			throws InterruptedException {
		Wait = new WebDriverWait(webdriver, 120);
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(OR.getProperty("canvasxPath"))));
			String DatafromHar = clickAt.getData(nodeValue, attribute1, attribute2, attribute3, proxy);
			String gamescompleted = DatafromHar.split(",")[0];
			freegamecompleted = Integer.parseInt(gamescompleted);
			System.out.println("games gamescompleted" + gamescompleted);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return freegamecompleted;
	}

	/*
	 * Date: 03/04/2017 Author: Kanchan Badhiye Description: offerid_forfreegame()
	 * use for retriving offer id games Parameter: nodeValue,and attribute values
	 */
	public int offerid_forfreegame(String nodeValue, String attribute1, String attribute2, String attribute3)
			throws InterruptedException {
		Wait = new WebDriverWait(webdriver, 120);
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(OR.getProperty("canvasxPath"))));
			clickAt.clickAt(OR.getProperty("spinButtonPixels"), webdriver, "//*[@id='gameCanvas']");
			Thread.sleep(1000);
			String DatafromHar = clickAt.getData(nodeValue, attribute1, attribute2, attribute3, proxy);
			String gamescompleted = DatafromHar.split(",")[0];
			freegamecompleted = Integer.parseInt(gamescompleted);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Thread.sleep(4000);
		return freegamecompleted;
	}

	/*
	 * Date: 03/04/2017 Author: Ashish Kshatriya Description: Go in PayTable screen
	 * from the settings Parameter: N/A
	 */
	public boolean verifyPayTable() throws InterruptedException {
		boolean image = webdriver.findElements(By.id(OR.getProperty("canvasxPath"))).size() > 0;
		try {
			if (image) {
				clickAt.clickAt(OR.getProperty("bargurMenu"), webdriver, "//*[@id='gameCanvas']");
				Thread.sleep(500);
				clickAt.clickAt(OR.getProperty("payTableMenu"), webdriver, "//*[@id='gameCanvas']");
				Thread.sleep(500);
			} else {
			}
		} catch (ElementNotVisibleException e) {
			e.printStackTrace();
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
			// boolean image =
			// webdriver.findElements(By.id(OR.getProperty("canvasxPath"))).size()
			// > 0;
			clickAt.clickAt(OR.getProperty("SelectCredits"), webdriver, "//*[@id='gameCanvas']");
			Thread.sleep(300);
			clickAt.clickAt(OR.getProperty("SelectCoins"), webdriver, "//*[@id='gameCanvas']");
			Thread.sleep(500);
			clickAt.clickAt(OR.getProperty("CoinSize"), webdriver, "//*[@id='gameCanvas']");
			Thread.sleep(500);
			for (int i = 0; i < minCoinSize; i++) {
				clickAt.clickAt(OR.getProperty("CoinMin"), webdriver, "//*[@id='gameCanvas']");
				Thread.sleep(500);
			}
			clickAt.clickAt(OR.getProperty("CoinSize"), webdriver, "//*[@id='gameCanvas']");
			Thread.sleep(500);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/*
	 * Date: 04/05/2017 Author:Kanchan Badhiye Description: settingback()use to
	 * click on back to setting screen Parameter: NA
	 */
	public boolean settingsBack() {
		return true;
	}

	/*
	 * Date: 29/05/2018 Author:Havish Jain Description: To open menu container
	 * Parameter: NA
	 */
	public boolean menuOpen() {
		boolean ret = false;
		try {
			// Clicking on hamburger menu
			String sX = "return Game.instance.__componentMap.BaseScene.__componentMap.menuShiftContainer.properties.x";
			String sY = "return Game.instance.__componentMap.BaseScene.__componentMap.menuShiftContainer.properties.globalY";

			clickAtCoordinates(sX, sY);
			Thread.sleep(1000);
			ret = true;
		} catch (InterruptedException e) {
			log.error("Error while opening menu", e);
			Thread.currentThread().interrupt();
		}
		return ret;
	}

	/*
	 * Date: 29/05/2018 Author:Havish Jain Description: To Close menu container
	 * Parameter: NA
	 */
	public boolean menuClose() {
		return false;
	}

	/*
	 * Date: 18/04/2017 Author: Ashish Kshatriya Description: This function is used
	 * for click on Base Scene Bet Parameter:N/A
	 */
	public boolean baseSceneBet() throws Exception {
		try {
			// boolean image =
			// webdriver.findElements(By.id(OR.getProperty("canvasxPath"))).size()
			// > 0;
			clickAt.clickAt(OR.getProperty("BetBaseScene"), webdriver, "//*[@id='gameCanvas']");
			Thread.sleep(500);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/*
	 * Date: 03/04/2017 Author: Ashish Kshatriya Description: PayTable slide
	 * naviagtion in PayTable screen from settings Parameter: N/A
	 */
	public boolean verifyPayTableNavigation() throws InterruptedException {

		try {
			clickAt.clickAt(OR.getProperty("navigateSlidePayTable"), webdriver, "//*[@id='gameCanvas']");
			Thread.sleep(500);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/*
	 * Date: 03/04/2017 Author: Ashish Kshatriya Description: Close Bargur Menu
	 * Parameter: N/A
	 */
	public boolean closeMenu() throws InterruptedException {
		// boolean image =
		// webdriver.findElements(By.id(OR.getProperty("canvasxPath"))).size() >
		// 0;
		try {

			clickAt.clickAt(OR.getProperty("backFrompayTableMenu"), webdriver, "//*[@id='gameCanvas']");
			Thread.sleep(500);
			clickAt.clickAt(OR.getProperty("closeHamBurgurMenu"), webdriver, "//*[@id='gameCanvas']");
			Thread.sleep(500);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/*
	 * Date: 03/04/2017 Author: Ashish Kshatriya Description: Login with Set game
	 * limit for Regulatory Market Spain Country Parameter: Username, Password,
	 * Application Name
	 */
	public String Func_Login_RegulatoryMarket_Spain(String username, String password) {
		Wait = new WebDriverWait(webdriver, 50);
		String Title = null;
		try {
			webdriver.findElement(By.xpath(XpathMap.get("userName"))).clear();
			webdriver.findElement(By.xpath(XpathMap.get("userName"))).sendKeys(username);
			webdriver.findElement(By.xpath(XpathMap.get("password"))).clear();
			webdriver.findElement(By.xpath(XpathMap.get("password"))).sendKeys(password);

			webdriver.findElement((By.xpath(XpathMap.get("Login")))).click();
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("spain_LobbyOK"))));
			webdriver.findElement((By.xpath(XpathMap.get("spain_LobbyOK")))).click();

			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("spain_SlotGameOverlay"))));
			Title = webdriver.getTitle();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Title;

	}

	// Creating random player in uk

	public String Func_Login_RegulatoryMarket_UK(CommonUtil gcfnlib, String password) {
		// Uncomment the database require

		// DataBaseFunctions createplayer1=new
		// DataBaseFunctions("zensardev","Casino","10.247.208.54","1402","5118");
		DataBaseFunctions createplayer1 = new DataBaseFunctions("zensardev2", "Casino", "10.247.208.107", "1402",
				"5118");
		// DataBaseFunctions createplayer1=new
		// DataBaseFunctions("zensarqa","Casino","10.247.208.62","1402","5118");
		// DataBaseFunctions createplayer1=new
		// DataBaseFunctions("zensarqa2","Casino","10.247.208.113","1402","5118");
		userName = gcfnlib.randomStringgenerator();
		log.debug("The New username is ==" + userName);
		createplayer1.createUser(userName, "0", 0);

		String url = XpathMap.get("UK_url");

		func_navigate(url);
		String GameTitle = Func_LoginBaseScene(userName, password);

		return GameTitle;
	}

	/*
	 * Date: 17/05/2019 Author:Snehal Gaikwad Description: To Check the bet
	 * selection on autoplay bet setting Parameter: NA
	 */

	public boolean autoplaybetselection() {

		boolean betselection = false;
		try {
			getConsoleText(
					"return mgs.mobile.casino.slotbuilder.v1.automation.getControlById('AutoplayOptionsComponent').onButtonClicked('autoplayOpenMobileButton')");
			WebElement coinsbet = webdriver.findElement(By.xpath(XpathMap.get("Coins_Bet")));
			Select coins = new Select(coinsbet);
			coins.selectByValue("6");
			webdriver.findElement(By.id(XpathMap.get("Start_Autoplay"))).click();
			Thread.sleep(2000);
			betselection = true;
		} catch (Exception e) {
			log.error("Error in Autoplay Bet Selection");
			betselection = false;

		}
		return betselection;
	}

	/*
	 * Date: 06/04/2017 Author: Ashish Kshatriya Description:Overlay popup for Set
	 * Session Limit for regulatory market Spain Parameter: N/A
	 */
	/*
	 * Date: 06/04/2017 Author: Ashish Kshatriya Description:Overlay popup for Set
	 * Session Limit for regulatory market Spain Parameter: N/A
	 */
	public boolean slotGameLimitsOverlay() throws InterruptedException {
		try {
			boolean SetSessionLimits = webdriver.findElements(By.xpath(XpathMap.get("slotGameLimitsOverlay")))
					.size() > 0;
			if (SetSessionLimits)
				return true;
			else
				return false;
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
			boolean SlotGameSession = webdriver.findElements(By.xpath(OR.getProperty("SlotGamesession1"))).size() > 0;
			try {
				if (SlotGameSession)
					;
				String slotGame = webdriver.findElement(By.xpath(OR.getProperty("SlotGamesession1"))).getText()
						.toString().trim();
				ar[0] = slotGame;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			boolean TotalWagered = webdriver.findElements(By.xpath(OR.getProperty("TotalWagered"))).size() > 0;
			try {
				if (TotalWagered)
					;
				String totalWagered = webdriver.findElement(By.xpath(OR.getProperty("TotalWagered"))).getText()
						.toString().trim();
				ar[1] = totalWagered;
			} catch (NoSuchElementException e) {
				e.printStackTrace();
			}
			boolean TotalPayouts = webdriver.findElements(By.xpath(OR.getProperty("TotalPayouts"))).size() > 0;
			try {
				if (TotalPayouts)
					;
				String totalPayout = webdriver.findElement(By.xpath(OR.getProperty("TotalPayouts"))).getText()
						.toString().trim();
				ar[2] = totalPayout;
			} catch (NoSuchElementException e) {
				e.printStackTrace();
			}
			boolean TotalBalance = webdriver.findElements(By.xpath(OR.getProperty("TotalBalance"))).size() > 0;
			try {
				if (TotalBalance)
					;
				String totalBalance = webdriver.findElement(By.xpath(OR.getProperty("TotalBalance"))).getText()
						.toString().trim();
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
	 * Date: 06/04/2017 Author: Ashish Kshatriya Description:Verify
	 * SlotGameSession,TotalWagered,TotalPayouts & TotalBalance for regulatory
	 * market Spain Parameter: N/A
	 */
	public boolean lossLimitPopupOverLay() throws InterruptedException {
		try {
			boolean popup = webdriver.findElements(By.id(OR.getProperty("LossLimitOverPopup"))).size() > 0;
			while (!popup) {
				clickAt.clickAt(OR.getProperty("spinButtonPixels"), webdriver, "//*[@id='gameCanvas']");
				Thread.sleep(3000);
				boolean popup1 = webdriver.findElements(By.id(OR.getProperty("LossLimitOverPopup"))).size() > 0;
				if (popup1) {
					break;
				}
			}
			boolean ok = webdriver.findElements(By.id(OR.getProperty("LossLimitOverOk"))).size() > 0;
			if (ok) {
				webdriver.findElement(By.id(OR.getProperty("LossLimitOverOk"))).click();
			} else {
			}
			boolean close = webdriver.findElements(By.id(OR.getProperty("LossLimitClose"))).size() > 0;
			if (close) {
				webdriver.findElement(By.id(OR.getProperty("LossLimitClose"))).click();
			} else {
			}
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/*
	 * Date: 03/04/2017 Author: Ashish Kshatriya Description: Set Session Limit for
	 * regulatory market Spain Parameter: LossLimit
	 */
	public String setLimit(String losslimit) throws InterruptedException {
		Wait = new WebDriverWait(webdriver, 120);
		try {
			boolean SetSessionLimits = webdriver.findElements(By.id(OR.getProperty("SetSessionLimitsForm"))).size() > 0;
			if (SetSessionLimits) {
				WebElement setTimeLimit = webdriver.findElement(By.id(OR.getProperty("SelectTimeLimit")));
				selectValue(setTimeLimit, OR.getProperty("TimeLimitdata"));
				Thread.sleep(1000);
				WebElement setReminderPeriod = webdriver.findElement(By.id(OR.getProperty("SelectReminderPeriod")));
				selectValue(setReminderPeriod, OR.getProperty("ReminderPeriod"));
				Thread.sleep(1000);
				WebElement setLossLimit = webdriver.findElement(By.id(OR.getProperty("SelectLossLimit")));
				// setLossLimit.sendKeys(OR.getProperty("LossLimit"));
				setLossLimit.sendKeys(losslimit);
				Thread.sleep(1000);
				/*
				 * WebElement setPreventFutureSlotGame=webdriver.findElement(By.id(OR.
				 * getProperty("SelectFutureSlotGame"))); selectValue(setPreventFutureSlotGame,
				 * OR.getProperty("FutureSlotGame"));
				 */
				webdriver.findElement(By.id(OR.getProperty("SetLimitButton"))).click();
				waitForPageToBeReady();
				Thread.sleep(500);
				boolean confirmgame = webdriver.findElements(By.id(OR.getProperty("ConfirmLaunchGame"))).size() > 0;
				if (confirmgame) {
					webdriver.findElement(By.id(OR.getProperty("ConfirmLaunchGame"))).click();
					waitForPageToBeReady();
				} else {
				}
				webdriver.findElement(By.id(OR.getProperty("canvasxPath")));
				Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(OR.getProperty("canvasxPath"))));

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
	 * is used for free spin occur in RMS. Parameter: NA
	 */
	public String verifySpinOccur() throws InterruptedException {
		String title = null;
		try {
			title = webdriver.getTitle();
			clickAt.clickAt(OR.getProperty("spinButtonPixels"), webdriver, "//*[@id='gameCanvas']");
			Thread.sleep(4000);
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}
		return title;

	}

	/*
	 * Date: 06/04/2017 Author: Author: Ashish Kshatriya Description: This function
	 * is used for Verify that user can dismiss the overlay and continue to play
	 * another game in RMS. Parameter: NA
	 */
	public String exitGame() throws InterruptedException {
		String title = null;
		Wait = new WebDriverWait(webdriver, 120);
		try {
			webdriver.navigate().refresh();
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(OR.getProperty("canvasxPath"))));
			Thread.sleep(1000);
			clickAt.clickAt(OR.getProperty("bargurMenu"), webdriver, "//*[@id='gameCanvas']"); // Clicking
																								// on
																								// hamburger
																								// menu
			Thread.sleep(1000);
			clickAt.clickAt(OR.getProperty("gamesMenu"), webdriver, "//*[@id='gameCanvas']"); // Clicking
																								// on
																								// Games
																								// menu
			waitForPageToBeReady();
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.getProperty("accountsLobbyID"))));
			title = webdriver.getTitle();
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}
		return title;
	}

	/*
	 * Date: 06/04/2017 Author: Author: Ashish Kshatriya Description: This function
	 * is used for take screenshots in application. Parameter: WebDriver driver
	 */
	public String createScreenshot(WebDriver webdriver) throws InterruptedException {
		UUID uuid = UUID.randomUUID();
		// String screenshotsFolder = "";
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
			if (webdriver.findElement(By.id(OR.getProperty("errorNotificationPopup"))).isDisplayed()) {
				getMessage = webdriver.findElement(By.id(OR.getProperty("errorNotificationPopupBody"))).getText();
			} else {
			}
		} catch (NoSuchElementException e) {
			// getTest().log(LogStatus.ERROR, "Error has been found on
			// screen"+getTest().addScreenCapture(createScreenshot(webdriver,"")));
		}
		return getMessage;
	}

	/*
	 * Date: 03/04/2017 Author: Anish Gadgil Description: Verifying the multiplier
	 * for Currency Parameter: nodeValue, attribute1, attribute2, attribute3
	 */
	public double currencymultiplier(String nodeValue, String attribute1, String attribute2, String attribute3)
			throws InterruptedException {
		double multiplier = 0;
		try {
			Thread.sleep(1000);
			clickAt.clickAt(OR.getProperty("spinButtonPixels"), webdriver, "//*[@id='gameCanvas']");
			Thread.sleep(3000);
			String DataFromHar = clickAt.getData(nodeValue, attribute1, attribute2, attribute3, proxy);
			String numchips = DataFromHar.split(",")[0];
			multiplier = Integer.parseInt(numchips);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Thread.sleep(5000);
		return multiplier;
	}

	/*
	 * Date:07/04/2017 Author: Anish Gadgil Description:This function will verify
	 * the starting screen of Free Games in partial automation Parameter:no
	 * parameters
	 */
	public boolean verify_FreeGames_Language_entryScreen_infoBar() throws InterruptedException {
		boolean image = webdriver.findElements(By.id(OR.getProperty("canvasxPath"))).size() > 0;
		try {
			if (image) {
				Thread.sleep(2000);
				clickAt.clickAt(OR.getProperty("infoBar"), webdriver, "//*[@id='gameCanvas']");
				Thread.sleep(1000);
			} else {
			}
		} catch (ElementNotVisibleException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/*
	 * Date:07/04/2017 Author: Anish Gadgil Description:This function will verify
	 * the starting screen of Free Games in partial automation Parameter:no
	 * parameters
	 */
	public boolean verify_FreeGames_Language_entryScreen_discardButton() throws InterruptedException {
		boolean image = webdriver.findElements(By.id(OR.getProperty("canvasxPath"))).size() > 0;
		try {
			if (image) {
				Thread.sleep(2000);
				clickAt.clickAt(OR.getProperty("discardbutton"), webdriver, "//*[@id='gameCanvas']");
				Thread.sleep(1000);
			} else {
			}
		} catch (ElementNotVisibleException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/*
	 * Date:10/04/2017 Author: Anish Gadgil Description:This function will take the
	 * game to base game from Discard Screen Parameter:no parameters
	 */
	public boolean discardScreentoBaseGame() throws InterruptedException {
		boolean image = webdriver.findElements(By.id(OR.getProperty("canvasxPath"))).size() > 0;
		try {
			if (image) {
				clickAt.clickAt(OR.getProperty("discardButtonOnScreen"), webdriver, "//*[@id='gameCanvas']");
				Thread.sleep(1000);
			} else {
			}
		} catch (ElementNotVisibleException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/*
	 * Date:10/04/2017 Author: Laxmikanth Kodam Description:This function will Click
	 * on Play now button of Free games and then click on spin and will refresh the
	 * game. Parameter:no parameters
	 */
	public boolean verify_ResumeScreenonrefresh_FreeGames() throws InterruptedException {
		Wait = new WebDriverWait(webdriver, 120);
		boolean image = webdriver.findElements(By.id(OR.getProperty("canvasxPath"))).size() > 0;
		try {
			if (image) {
				clickAt.clickAt(OR.getProperty("freeGamesPlayNow"), webdriver, "//*[@id='gameCanvas']");
				Thread.sleep(1000);
				clickAt.clickAt(OR.getProperty("spinButtonPixels"), webdriver, "//*[@id='gameCanvas']");
				Thread.sleep(4000);
				webdriver.navigate().refresh();
				Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(OR.getProperty("canvasxPath"))));
			} else {
			}
		} catch (ElementNotVisibleException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// *********************************************************************************************
	/*
	 * Date:19/08/17 Author: Anish Gadgil Description:This function is used to click
	 * anywhere on the screen. Parameters: No Parameters
	 */
	public boolean clickAnywhere() throws InterruptedException {

		clickAt.clickAt(OR.getProperty("menubuttonextended"), webdriver, "//*[@id='gameCanvas']");
		Thread.sleep(2000);
		return true;
	}

	// ************************************************************************************************

	// ****************************************************************************************

	/*
	 * Date: 04/05/2017 Author:Kanchan Badhiye Description: vclickonsetting use to
	 * click on setting screen Parameter: NA
	 */

	public boolean clickonsetting() throws InterruptedException {

		// Thread.sleep(5000);
		try {
			clickAt.clickAt(OR.getProperty("menubuttonPixels"), webdriver, "//*[@id='gameCanvas']");
			Thread.sleep(1000);
			clickAt.clickAt(OR.getProperty("settings"), webdriver, "//*[@id='gameCanvas']");
			Thread.sleep(1000);
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	// ****************************************************************************************
	/*
	 * Date:10/04/2017 Author: Anish Gadgil Description:This function will take the
	 * game from resume screen to Base Game. Parameter:no parameters
	 */
	public boolean resumeScreentoBaseGame() throws InterruptedException {
		boolean image = webdriver.findElements(By.id(OR.getProperty("canvasxPath"))).size() > 0;
		try {
			if (image) {
				clickAt.clickAt(OR.getProperty("Resumescreenbutton"), webdriver, "//*[@id='gameCanvas']");
				Thread.sleep(1000);
			} else {
			}
		} catch (ElementNotVisibleException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/*
	 * Date:10/04/2017 Author: Anish Gadgil Description:This function will take the
	 * game from summary screen to Base Game. Parameter:no parameters
	 */
	public boolean summaryScreentoBaseGame() throws InterruptedException {
		boolean image = webdriver.findElements(By.id(OR.getProperty("canvasxPath"))).size() > 0;
		try {
			if (image) {
				clickAt.clickAt(OR.getProperty("backToGameSummaryScreen"), webdriver, "//*[@id='gameCanvas']");
				Thread.sleep(1000);
			} else {
			}
		} catch (ElementNotVisibleException e) {
			e.printStackTrace();
			return false;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/* Method is for component store */
	public boolean func_Click(String locator) {
		boolean present;
		try {
			WebElement ele = webdriver.findElement(By.xpath(locator));

			if (ele != null) {
				Wait.until(ExpectedConditions.elementToBeClickable(By.xpath(locator)));
				ele.click();
			}
			present = true;
		} catch (NoSuchElementException e) {
			present = false;
		}
		return present;
	}

	public void waitForPageToBeReady() {
		JavascriptExecutor js = (JavascriptExecutor) webdriver;
		for (int i = 0; i < 400; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				log.error("error in page to be ready", e);
				Thread.currentThread().interrupt();
			}
			if (js.executeScript("return document.readyState").toString().equals("complete")) {
				break;
			}
		}
	}

	// ********************************Exception
	// Handler********************************************************//
	public void evalException() throws Exception {
		// System.out.println("*************In Exception Handling
		// Class*************"+ex.getClass());
		// ex.printStackTrace();
		// repo1.details_append( "Execution Interrupted because of exception" ,
		// "" , "", "Interrupted");

		String exClass = ex.getClass().toString();
		// ex.printStackTrace();
		if (exClass.contains("StaleElementReferenceException")) {
			// System.out.println("Identified specific exception "+exClass);
			// System.out.println();
			repo1.detailsAppend("Execution Interrupted because of StaleElementReferenceException", "", "",
					"Interrupted");
		} else if (exClass.contains("NoSuchElementException")) {
			repo1.detailsAppend("Execution Interrupted because of NoSuchElementException", "", "", "Interrupted");
			// System.out.println("Identified specific exception "+exClass);
		} else if (exClass.contains("InvalidElementStateException")) {
			repo1.detailsAppend("Execution Interrupted because of InvalidElementStateException", "", "", "Interrupted");
			// System.out.println("Identified specific exception "+exClass);
		} else if (exClass.contains("ElementNotVisibleException")) {
			repo1.detailsAppend("Execution Interrupted because of ElementNotVisibleException", "", "", "Interrupted");
			// System.out.println("Identified specific exception "+exClass);
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
			//
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
		}
	}

	/**
	 * This method is used to verify the Base Game Logo
	 * 
	 * @param imagepath
	 * @return
	 */

	public boolean QuickSpinclick() {

		/* func_Click(XpathMap.get("OneDesignQuickSpin")); */

		return true;
	}

	public void winClick() {

	}

	public boolean customeverifyimage(String imagepath) throws InterruptedException {
		Wait.until(ExpectedConditions.visibilityOf(webdriver.findElement(By.id("inGameClock"))));// wait
																									// untill
																									// the
																									// base
																									// game
																									// screen
																									// won't
																									// come
		Screen S = new Screen();
		boolean newimage;
		org.sikuli.script.Pattern image = new org.sikuli.script.Pattern(imagepath);
		System.out.println("get image : " + image);
		try {
			// S.click(S.wait(image, 10));
			// Region r=S.find(image);
			org.sikuli.script.Region r = S.exists(image, 5);
			if (r != null) {
				System.out.println("image found");
				newimage = true;
			} else {
				System.out.println("image not found");
				newimage = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return newimage;
	}
	/*
	 * Date: 03/04/2017 Author: Author: Dhairaya Gautam Description: This function
	 * is used for navigating for Denmark lobby Parameter: NA
	 */

	public String Func_navigate_reg(String applicationName, String url) {
		try {
			webdriver.get(url);
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(OR.getProperty("LobbyfiveReels"))));
			webdriver.findElement(By.id(OR.getProperty("LobbyfiveReels"))).click();
			Thread.sleep(4000);
			GameDesktopName = webdriver.findElement(By.id(applicationName)).getText();
			webdriver.findElement(By.id(applicationName)).click();
			// wait.until(ExpectedConditions.elementToBeClickable(By.id(OR.getProperty("LobbyfiveReels"))));
			/*
			 * JavascriptExecutor js = (JavascriptExecutor) webdriver;
			 * js.executeScript("arguments[0].scrollIntoView(true);",webdriver.
			 * findElement(By.xpath(OR.getProperty("LobbyfiveReels")))); Thread.sleep(4000);
			 */
			/*
			 * js.executeScript("arguments[0].scrollIntoView(true);",webdriver.
			 * findElement(By.id(applicationName))); Thread.sleep(1000); //
			 * Wait.until(ExpectedConditions.elementToBeClickable(By.id( applicationName)));
			 * GameDesktopName = webdriver.findElement(By.id(applicationName)).getText();
			 * System.out.println("application name " + GameDesktopName);
			 * webdriver.findElement(By.id(applicationName)).click();
			 */
			// change it
			// wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.getProperty("loginPopUp"))));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return GameDesktopName;
	}

	public boolean Func_LoginBaseScene_Gibraltar(String username, String password) {
		String Title = null;
		try {
			Thread.sleep(3000);
			webdriver.findElement(By.xpath(OR.getProperty("GameUserName"))).clear();
			Thread.sleep(500);
			webdriver.findElement(By.xpath(OR.getProperty("GameUserName"))).sendKeys(username);
			webdriver.findElement(By.xpath(OR.getProperty("GamePassword"))).clear();
			webdriver.findElement(By.xpath(OR.getProperty("GamePassword"))).sendKeys(password);
			webdriver.findElement(By.xpath(OR.getProperty("GameLogin"))).click();

			Thread.sleep(25000);
			try {
				boolean regulaoryLastLogin = webdriver
						.findElements(By.xpath(OR.getProperty("regulatoryLastLoginPopup"))).size() > 0;
				if (regulaoryLastLogin) {
					webdriver.findElement(By.xpath(OR.getProperty("GameLogin"))).click();
					Thread.sleep(9000);
					// webdriver.findElement(By.xpath(OR.getProperty("regulatoryClose"))).click();

				}
			} catch (Exception e) {
				System.err.println("Error : " + e);
			}
			Thread.sleep(15000);
			// wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inGameClock")));
			Thread.sleep(10000);
			Title = webdriver.getTitle();
			System.out.println("The Title is " + Title);
			/*
			 * String taketogame=webdriver.findElement(By.id("header")).getText();
			 * System.out.println(taketogame);
			 * webdriver.findElement(By.id("userInput")).sendKeys(amount);
			 * webdriver.findElement(By.id("submit")).click(); Thread.sleep(6000);
			 * webdriver.findElement(By.id("submit")).click(); Thread.sleep(30000);
			 * clickAt(OR.getProperty("newfeatures")); WebElement ele1 =
			 * webdriver.findElement(By.xpath("//*[@id='gameCanvas']")); ele1 =
			 * wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath
			 * (OR.getProperty("canvasxPath")))); Title = webdriver.getTitle();
			 * System.out.println("The Title is " + Title);
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public void verifyInGameMenuIcons(Desktop_HTML_Report sanitysuit) {
		Wait = new WebDriverWait(webdriver, 50);
		try {

			inGameMenuClick(sanitysuit, XpathMap.get("help_Image"), "Help");
			inGameMenuClick(sanitysuit, XpathMap.get("responsible_Image"), "Responsible Gaming");
			inGameMenuClick(sanitysuit, XpathMap.get("playcheck"), "Playcheck");
			inGameMenuClick(sanitysuit, XpathMap.get("cashcheck"), "Cashcheck");
			inGameMenuClick(sanitysuit, XpathMap.get("loyalty"), "Loyalty");
			/*
			 * String parentWindow= webdriver.getWindowHandle();
			 * 
			 * Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath
			 * (XpathMap.get("help_Image"))));
			 * webdriver.findElement(By.xpath(XpathMap.get("help_Image"))).click ();
			 * 
			 * for (String handle : webdriver.getWindowHandles()) {
			 * webdriver.switchTo().window(handle); } sanitysuit.details_append(
			 * "Verify that the "+XpathMap.get("help_Image")+
			 * " link should open in new window",
			 * "On clicking help link new window should open", "New window is open",
			 * "pass"); webdriver.close();
			 * 
			 * webdriver.switchTo().window(parentWindow);
			 */
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public void inGameMenuClick(Desktop_HTML_Report sanitysuit, String locator, String iconName) {
		try {
			String parentWindow = webdriver.getWindowHandle();
			if (webdriver.findElement(By.xpath(locator)).isDisplayed()) {
				webdriver.findElement(By.xpath(locator)).click();

				for (String handle : webdriver.getWindowHandles()) {
					webdriver.switchTo().window(handle);
				}
				sanitysuit.detailsAppend("Verify that the " + iconName + " link should open in new window",
						"On clicking " + iconName + " link new window should open", "New window is open", "pass");
				webdriver.close();
				webdriver.switchTo().window(parentWindow);
			}
		} catch (Exception e) {
			e.getMessage();
		}
	}

	/**
	 * Date: 10/1/2018 Autohr: Laxmikanth Kodam Description: This function used to
	 * verify the help icon
	 *
	 */

	public void checkPopUp() {

	}

	/**
	 * Date: 10/1/2018 Autohr: Laxmikanth Kodam Description: This function used for
	 * verifying the help icon
	 * 
	 * @return
	 */

	public boolean verifyHelp() {

		boolean ret = false;
		boolean test = webdriver.findElement(By.id("inGameHelpLink")).isDisplayed();
		if (test) {
			ret = true;
		}
		return ret;
	}

	/**
	 * Date: 10/1/2018 Autohr: Laxmikanth Kodam Description: This function used for
	 * clicking help icon
	 * 
	 * @return
	 */
	public String clickHelp() {

		String GoogleTitle = null;

		return GoogleTitle;
	}

	/**
	 * Date: 03/04/2017 Author: Robin Dulhani Description: Returns the title of the
	 * web page. Parameter: Reference of WebDriver.
	 */

	public String getPageTitle(WebDriver w) {
		String Title = null;
		try {

			Title = w.getTitle();
		} catch (Exception e) {
			System.err.println("Error : " + e);

		}

		return Title;

	}

	/*
	 * Date: 03/04/2017 Author: Author: Dhairaya Gautam Description: This function
	 * used for page load Parameter: NA
	 */
	public String taketoGame() {
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.getProperty("canvasxPath"))));
		clickAt(OR.getProperty("newfeatures"));

		return null;
	}

	/*
	 * Date: 03/04/2017 Author: Author: Dhairaya Gautam Description: This function
	 * used for page load Parameter: NA
	 */
	public boolean Func_LoginBaseScene_Italy(String username, String password) {
		Wait = new WebDriverWait(webdriver, 500);
		String Title = null;
		boolean present = false;

		try {
			webdriver.findElement(By.xpath(XpathMap.get("userName"))).clear();
			webdriver.findElement(By.xpath(XpathMap.get("userName"))).sendKeys(username);
			webdriver.findElement(By.xpath(XpathMap.get("password"))).clear();
			webdriver.findElement(By.xpath(XpathMap.get("password"))).sendKeys(password);

			webdriver.findElement((By.xpath(XpathMap.get("Login")))).click();
			// func_Click(XpathMap.get("practice_Play"));
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("italyHeader"))));
			Title = webdriver.getTitle();
			if (Title != null) {

				present = true;
			} else {
				present = false;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return present;
	}

	// function for check game play req
	/*
	 * Date: 03/04/2017 Author: Author: Dhairaya Gautam Description: This function
	 * used for page load Parameter: NA
	 */
	public String isGamePlay() {
		String s = null;
		try {
			WebElement ele1 = Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='header']")));
			ele1 = webdriver.findElement(By.xpath("//*[@id='header']"));
			s = ele1.getText();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * Date: 14/11/2017 Author:Premlata Mishra Description: This function is used
	 * verify incomplete game dialogue Parameter:String applicationName,String
	 * urlNavigate
	 */
	public String incomplete_GameName(String gamename) {
		String incomplete_GameName = null;
		try {
			Wait = new WebDriverWait(webdriver, 40);
			// Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("five_Reel_slot"))));
			JavascriptExecutor js = ((JavascriptExecutor) webdriver);
			js.executeScript("arguments[0].scrollIntoView(true);", webdriver.findElement(By.id(gamename)));

			js.executeScript("arguments[0].click();", webdriver.findElement(By.id(gamename)));
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("defaultOverlayContent")));
			incomplete_GameName = webdriver.findElement(By.id("defaultOverlayContent")).getText();
		} catch (Exception e) {
			incomplete_GameName = null;
		}
		System.out.println("incompletegamegame" + incomplete_GameName);
		return incomplete_GameName;
	}

	/*
	 * Date: 03/04/2017 Author: Author: Dhairaya Gautam Description: This function
	 * used for page load Parameter: NA
	 */
	public void clickamount(Desktop_HTML_Report italy, String amount) {

		try {
			// System.out.println(s);
			Thread.sleep(1000);
			webdriver.findElement(By.id("userInput")).sendKeys(amount);
			Thread.sleep(1000);
			webdriver.findElement(By.id("submit")).click();
			Thread.sleep(3000);
			webdriver.findElement(By.id("submit")).click();
			// Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("clock_ID"))));
			// Thread.sleep(30000);
			// value=true;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * private CharSequence valueOf(double amount) { return null; }
	 */

	/*
	 * Date: 03/04/2017 Author: Author: Anish Description: This function used for
	 * page load
	 */
	public String verifyCreditsValue() {
		return null;
	}
	/*
	 * Date: 03/04/2017 Author: Author: Dhairaya Gautam Description: This function
	 * used for page load Parameter: NA
	 */

	public boolean clickok() throws InterruptedException {
		boolean close = false;
		close = webdriver.findElements(By.xpath(OR.getProperty("LossLimitOverOk"))).size() > 0;
		if (close) {
			webdriver.findElement(By.xpath(OR.getProperty("LossLimitOverOk"))).click();
			close = true;
		} else {
		}
		return close;
	}

	/*
	 * Date: 03/04/2017 Author: Author: Dhairaya Gautam Description: This function
	 * used for page load Parameter: NA
	 */
	public String Func_logout_spain() {
		String loginTitle = null;
		try {
			/*
			 * waitForPageToBeReady(); Thread.sleep(00);
			 * wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath
			 * ("//*[@id='gameCanvas']")));
			 * 
			 * clickAt(OR.getProperty("bargurMenu")); // Clicking on hamburger menu
			 * Thread.sleep(1000); clickAt(OR.getProperty("gamesMenu")); // Clicking on
			 * Games menu
			 * 
			 * //WebElement ele1 = webdriver.findElement(By.xpath("//*[@id='gameCanvas']"));
			 * Thread.sleep(10000); waitForPageToBeReady();
			 */
			// WebElement ele1 =
			// webdriver.findElement(By.xpath("//*[@id='gameCanvas']"));
			Wait.until(ExpectedConditions.elementToBeClickable(By.id(OR.getProperty("accountsLobbyID")))).click();
			;
			Thread.sleep(2000);

			webdriver.findElement(By.id(OR.getProperty("logoutbuttonId"))).click(); // Clicking
																					// on
																					// log
																					// out
																					// button
			Thread.sleep(1000);
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(OR.getProperty("LoginTitleid"))));
			loginTitle = webdriver.findElement(By.id(OR.getProperty("LoginTitleid"))).getText(); // Clicking
																									// on
																									// log
																									// out
																									// button
			Thread.sleep(1000);
			webdriver.findElement(By.id(OR.getProperty("closeButtonid"))).click(); // Clicking
																					// on
																					// log
																					// out
																					// button
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginTitle;
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
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.getProperty("canvasxPath"))));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Date: 03/04/2017 Author: Author: Dhairaya Gautam Description: This function
	 * used for page load Parameter: NA
	 */
	public boolean navigateSettingsUK() throws Exception {
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.getProperty("canvasxPath"))));
		// clickAt(OR.getProperty("newFeatures"));
		Thread.sleep(1000);
		clickAt(OR.getProperty("bargurMenu"));
		Thread.sleep(500);
		clickAt(OR.getProperty("settingsUK"));
		Thread.sleep(500);
		return true;
	}

	/**
	 * Date: 03/04/2017 Author: Kamal Kumar Vishwakarma Description: Parameter:
	 */
	public double[] getAttributes(String nodeValue, String attribute1, String attribute2, String attribute3) {

		double[] data = new double[4];
		try {
			waitForPageToBeReady();
			Thread.sleep(1000);
			clickAt(OR.getProperty("spinButtonPixels"));
			Thread.sleep(15000);
			String DataFromHar = clickAt.getData(nodeValue, attribute1, attribute2, attribute3, proxy);
			balance = DataFromHar.split(",")[0];
			loyaltyBalance = DataFromHar.split(",")[1];
			totalWin = DataFromHar.split(",")[2];
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

	/**
	 * Date: 28/12/2017 Author:Laxmikanth Kodam Description: Parameter:
	 */

	public double[] getAttributesSpin(String nodeValue, String attribute1, String attribute2, String attribute3) {

		double[] data = new double[4];
		try {
			waitForPageToBeReady();
			Thread.sleep(1000);
			clickAt(OR.getProperty("spinButtonPixels"));
			Thread.sleep(15000);
			String DataFromHar = clickAt.getData(nodeValue, attribute1, attribute2, attribute3, proxy);
			balance = DataFromHar.split(",")[0];
			loyaltyBalance = DataFromHar.split(",")[1];
			totalWin = DataFromHar.split(",")[2];
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
	 * Date: 11/04/2017 Author:Robin Dulhani Description: This function verifies the
	 * working of the TOGGLE TOPBAR. Parameter: imagepath
	 */
	String topbarData;

	public String verifyToggleTopbar() {

		try {
			webdriver.switchTo().frame("commonUIIFrame");
			Thread.sleep(1000);
			webdriver.findElement(By.xpath("//*[@id='toggleTopbarButton']")).click();
			Thread.sleep(5000);
			String stake = webdriver.findElement(By.id("stake")).getText() + "!";
			String paid = webdriver.findElement(By.id("paid")).getText() + "!";
			webdriver.findElement(By.id("balance")).getText();
			String balance = webdriver.findElement(By.id("balance")).getText() + "!";
			String freebets = webdriver.findElement(By.id("freebets")).getText() + "!";
			System.out.println("stake= " + stake);
			System.out.println("paid= " + paid);
			System.out.println("balance= " + balance);
			System.out.println("freebets= " + freebets);
			topbarData = stake + paid + balance + freebets;
			System.out.println(topbarData);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return topbarData;
	}

	public boolean verifyQuickSpin() throws InterruptedException {
		boolean newimage = false;
		try {
			// newimage=VerifyImage(imagepath);

		} catch (Exception e) {

			e.printStackTrace();
		}
		return newimage;
	}
	/*
	 * Date: 03/04/2017 Author: Author: Dhairaya Gautam Description: This function
	 * used for page load Parameter: NA
	 * 
	 * public boolean verifyQuickSpin(String imagepath) throws InterruptedException
	 * { Screen S = new Screen(); Match m = S.exists(imagepath); Pattern image = new
	 * Pattern(imagepath); System.out.println("get image" + image); try {
	 * Thread.sleep(5000);
	 * 
	 * if(S.exists(image)!=null){
	 * 
	 * System.out.println("Quick spin exists"); closeMenu(); return false; } else {
	 * closeMenu(); return true;
	 * 
	 * 
	 * }
	 * 
	 * 
	 * } catch (Exception e) { e.printStackTrace(); return false; }
	 * 
	 * }
	 */

	/**
	 * Date: 01/01/2017 Autohr: Laxmikanth Kodam Description:Verify the UK topbar
	 * stake by changing the coin size in settings
	 * 
	 * @return Title
	 * @throws InterruptedException
	 */

	public String[] verifyStakeWithTopBar() {

		return null;

	}

	/**
	 * @throws InterruptedException
	 * 
	 */

	public boolean verifyGameFlag() throws InterruptedException {
		Screen S = new Screen();

		org.sikuli.script.Pattern image = new org.sikuli.script.Pattern();

		boolean present = false;
		try {
			org.sikuli.script.Region r = S.exists(image);
			if (r != null) {

				present = true;
			} else {

				present = false;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return present;

	}

	public boolean verifySpin(String image) throws InterruptedException {

		return true;
	}

	/**
	 * Date: 01/01/2017 Autohr: Dhairaya Gautam Description:Verify the
	 * verifyResponsiblegamingLink in Denmark
	 * 
	 * @return Title
	 * @throws InterruptedException
	 */
	public boolean verifyResponsibleLink_Denmark() throws InterruptedException {

		try {

			webdriver.findElement(By.xpath("//*[@id='responsibleGaming']")).click();

			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("liSession")));

			String title = webdriver.getTitle();

			if (title != null) {
				System.out.println("Responsible gaming link is present");
			} else {
				System.out.println("Responsible gaming link is not present");

			}
			Thread.sleep(2000);

			Thread.sleep(2000);
			// closeMenu();
			/*
			 * clickAt(OR.getProperty("responsiblegmaingclick")); Thread.sleep(15000);
			 * if(webdriver.getTitle().equalsIgnoreCase( "Responsible Gaming")){
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
	 * Date: 07/04/2017 Author: Dhairaya Gautam Description: Method to verify
	 * responsible gaming link present in Denmark regulatory markets Parameter:image
	 * path
	 *
	 */
	public boolean verifyResponsibleLink(String imagepath) throws InterruptedException {
		Screen S = new Screen();
		org.sikuli.script.Pattern image = new org.sikuli.script.Pattern(imagepath);
		System.out.println("get image" + image);

		try {
			System.out.println("Responsible gaming link checking");
			Thread.sleep(2000);
			S.exists(image);
			Thread.sleep(2000);
			// closeMenu();
			/*
			 * clickAt(OR.getProperty("responsiblegmaingclick")); Thread.sleep(15000);
			 * if(webdriver.getTitle().equalsIgnoreCase( "Responsible Gaming")){
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

	public boolean verifyResponsibleLink_working(String imagepath, Desktop_HTML_Report report)
			throws InterruptedException {
		Screen S = new Screen();
		org.sikuli.script.Pattern image = new org.sikuli.script.Pattern(imagepath);
		System.out.println("get image" + image);

		try {
			System.out.println("Responsible gaming link checking");
			Thread.sleep(2000);
			S.exists(image);
			Thread.sleep(2000);
			closeMenu();
			/*
			 * clickAt(OR.getProperty("responsiblegmaingclick")); Thread.sleep(15000);
			 * if(webdriver.getTitle().equalsIgnoreCase( "Responsible Gaming")){
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
	 * int itemSize = elementCount.size();{
	 * 
	 * }
	 */

	public boolean verifydenmarkResponsibleLink(String imagepath) throws InterruptedException {
		Screen S = new Screen();
		org.sikuli.script.Pattern image = new org.sikuli.script.Pattern(imagepath);
		System.out.println("get image" + image);

		try {
			System.out.println("Responsible gaming link checking");
			Thread.sleep(2000);
			S.exists(image);
			Thread.sleep(2000);
			closeMenu();
			/*
			 * clickAt(OR.getProperty("responsiblegmaingclick")); Thread.sleep(15000);
			 * if(webdriver.getTitle().equalsIgnoreCase( "Responsible Gaming")){
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
	public String Func_LoginBaseScene_Denmark(String username, String password) {
		String Title = null;
		try {
			Thread.sleep(1000);
			webdriver.findElement(By.xpath(OR.getProperty("GameUserName"))).clear();
			Thread.sleep(500);
			webdriver.findElement(By.xpath(OR.getProperty("GameUserName"))).sendKeys(username);
			Thread.sleep(500);
			webdriver.findElement(By.xpath(OR.getProperty("GamePassword"))).clear();
			Thread.sleep(500);
			webdriver.findElement(By.xpath(OR.getProperty("GamePassword"))).sendKeys(password);
			Thread.sleep(500);
			webdriver.findElement(By.xpath(OR.getProperty("GameLogin"))).click();
			// Thread.sleep(10000);
			/*
			 * try { boolean regulaoryLastLogin =
			 * webdriver.findElements(By.xpath(OR.getProperty(
			 * "regulatoryLastLoginPopup"))).size() > 0; if (regulaoryLastLogin) {
			 * webdriver.findElement(By.xpath(OR.getProperty("GameLogin"))). click();
			 * Thread.sleep(3000); } } catch (Exception e) { System.err.println("Error : " +
			 * e); } webdriver.findElement(By.xpath(OR.getProperty("regulatoryClose"))
			 * ).click();
			 */

			// Thread.sleep(30000);
			// clickAt(OR.getProperty("newfeatures"));
			// WebElement ele1 =
			// webdriver.findElement(By.xpath("//*[@id='gameCanvas']"));
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("txtWin")));
			Thread.sleep(500);
			// clickAt(OR.getProperty("newfeatures"));
			Title = webdriver.getTitle();
			System.out.println("The Title is " + Title);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Title;
	}

	public String PreventFuture() throws Exception {
		String PreventFuture1 = null;

		WebElement PreventFuture = webdriver.findElement(By.xpath(OR.getProperty("PreventFuture")));

		PreventFuture1 = PreventFuture.getText();

		return PreventFuture1;
	}

	/* code for verifying slottitle */
	public String verifyslottitle() throws Exception {
		String slotgametitle1 = null;

		WebElement slotgametitle = webdriver.findElement(By.xpath(OR.getProperty("SlotGameLimit")));
		slotgametitle1 = slotgametitle.getText();

		return slotgametitle1;
	}

	public String verifysetlimitbutton() throws Exception {
		String setlimit1 = null;

		WebElement setlimit = webdriver.findElement(By.xpath(OR.getProperty("setlimitbutton")));
		setlimit1 = setlimit.getText();
		setlimit.click();

		return setlimit1;
	}

	public boolean verifyhyperlink() throws Exception {
		boolean ret = false;

		if (webdriver.findElement(By.xpath(OR.getProperty("backtolobby"))).isDisplayed()) {
			System.out.println("enabled");
			ret = true;
		}

		return ret;
	}

	/* verify time limit blank */
	public boolean verifytimelimitblank() throws Exception {

		boolean ret = false;

		WebElement timelimitinputbox = webdriver.findElement(By.xpath(OR.getProperty("timeLimitInput")));
		String textInsideInputBox = timelimitinputbox.getAttribute("value");

		// Check whether input field is blank
		if (textInsideInputBox.isEmpty()) {
			System.out.println("Input field is empty");
			ret = true;
		}

		return ret;
	}

	/* verify reminder period blank */
	public boolean verifyreminderperiodblank() throws Exception {
		boolean ret = false;

		WebElement timelimitinputbox = webdriver.findElement(By.xpath(OR.getProperty("reminderLimitInput")));
		String textInsideInputBox = timelimitinputbox.getAttribute("value");

		// Check whether input field is blank
		if (textInsideInputBox.isEmpty()) {
			System.out.println("Input field is empty");
			ret = true;
		}

		return ret;
	}

	/* verify time loss limit blank */
	public boolean losslimitblank() throws Exception {
		boolean ret = true;

		WebElement timelimitinputbox = webdriver.findElement(By.xpath(OR.getProperty("lossLimitInput")));
		String textInsideInputBox = timelimitinputbox.getAttribute("value");

		// Check whether input field is blank
		if (textInsideInputBox.isEmpty()) {
			System.out.println("Input field is empty");
			ret = true;
		}

		return ret;
	}

	/**
	 * Date:10-1-2018 Name:Laxmikanth Kodam Description: this function is used to
	 * language verification on base scene
	 * 
	 * @return lobbytitle
	 */

	public boolean baseSceneLanguage() {

		return true;
	}

	/**
	 * Date:10-1-2018 Name:Laxmikanth Kodam Description: this function is used to
	 * language verification inside paytable
	 */

	public boolean paytableLanguage() {

		return true;
	}

	/**
	 * Date:10-1-2018 Name:Laxmikanth Kodam Description: this function is used to
	 * language verification inside settings
	 */

	public boolean settingsLanguage() {

		return true;
	}

	/**
	 * Date:10-1-2018 Name:Laxmikanth Kodam Description: this function is used to
	 * language verification inside hamburger menu
	 * 
	 * @return true
	 */

	public boolean menuLinksLanguage() {

		return true;
	}

	/* verify time limit blank */
	public boolean verifyfutureprventblank() throws Exception {
		boolean ret = false;

		WebElement timelimitinputbox = webdriver.findElement(By.xpath(OR.getProperty("preventfutureslotgamefor")));
		String textInsideInputBox = timelimitinputbox.getAttribute("value");

		// Check whether input field is blank
		if (textInsideInputBox.isEmpty()) {
			System.out.println("Input field is empty");
			ret = true;
		}

		return ret;
	}

	/**
	 * Date:07/12/2017 Author:Laxmikanth Kodam This method is used for verifying the
	 * status of the quick spin
	 * 
	 * @return true
	 */

	/**
	 * Date:07/12/2017 Author:Laxmikanth Kodam This method is actually not necessery
	 * in component store just declaration needed
	 * 
	 * @return true
	 */

	public String verifyBalanceBetBeforeSpin() {

		return null;
	}

	/**
	 * Date: 13/11/2017 Author: Ashish Kshatriya Description: This function is used
	 * for get Webelement text Parameter: By locator
	 */
	public String func_GetText(String locator) 
	{
		return null;
		
	}

	/**
	 * Date:22/11/2017 Author Lamxikanth Kodam Common added for logout() This method
	 * is common logout function for the component store
	 * 
	 * @return
	 * @throws Exception
	 */
	public String Func_logout_OD() throws Exception {
		String loginTitle = null;
		try {
			func_Click(XpathMap.get("OneDesign_HomeIcon"));// Clicking on X button

			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("five_Reel_slot"))));
			if (webdriver.getPageSource().contains("Arthur")) {
				func_Click(XpathMap.get("Profile"));
				// func_Click("//*[@id='myaccount']");
				log.debug("Clicked on profile");

			} else {
				func_Click(XpathMap.get("navigation_MyAccount"));
				log.debug("Clicked on My Account");

			}
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("logout"))));
			loginTitle = func_GetText(XpathMap.get("logout"));
			func_Click(XpathMap.get("logout"));
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("Login"))));
			Thread.sleep(2000);
			func_Click(XpathMap.get("closeButtonLogin"));
		} catch (WebDriverException e) {
			e.getMessage();
			throw new Exception(e);
		}
		return loginTitle;
	}

	// C720751(5005-5006-5011) Selectable options :Ensure dropdown box appear
	// for Time limit box

	public boolean verifytimedropdown() throws Exception {
		boolean ret = false;

		WebElement timedropdown = webdriver.findElement(By.xpath(OR.getProperty("timeLimitInput")));
		timedropdown.click();
		Thread.sleep(500);
		ret = true;

		return ret;
	}

	// C720751(5005-5006-5011) Selectable options :Ensure dropdown box appear
	// for reminder limit box
	public boolean verifyreminderdropdown() throws Exception {
		boolean ret1 = false;

		WebElement reminderdropdown = webdriver.findElement(By.xpath(OR.getProperty("reminderLimitInput")));
		reminderdropdown.click();
		Thread.sleep(500);
		ret1 = true;

		return ret1;
	}

	// Selectable option Ensure slotreminder dropdown appear
	public boolean verifypreventfutureslotreminder() throws Exception {
		boolean ret = false;

		WebElement preventfuture = webdriver.findElement(By.xpath(OR.getProperty("preventfutureslotgamefor")));
		preventfuture.click();
		ret = true;

		return ret;
	}

	// Ensure there are time duration options available for selection in the
	// different time units (eg, 1 hour, 1 minute).

	public String verifytimedurationoption(String dropdownValue) throws Exception {
		String selectoption = null;
		try {
			WebElement timedropdown = webdriver.findElement(By.xpath(OR.getProperty("timeLimitInput")));
			// timedropdown.clear();
			// timedropdown.click();
			Thread.sleep(500);
			// List<WebElement> optionsoftimelimit =
			// webdriver.findElements(By.xpath(OR.getProperty("timelimitoptions")));
			Select optionsoftimelimit = new Select(timedropdown);
			System.out.println("select options" + optionsoftimelimit);
			optionsoftimelimit.selectByVisibleText(dropdownValue);
			// optionsoftimelimit.selectByIndex(2);
			selectoption = optionsoftimelimit.getFirstSelectedOption().getText();
			System.out.println("option " + optionsoftimelimit);

		} catch (Exception e) {
			e.getMessage();
		}

		return selectoption;
	}

	// Ensure there are time duration options available for selection in the
	// different time units (eg, 1 hour, 1 minute).

	public String verifytimedurationoption() throws Exception {
		String selectoption = null;

		WebElement timedropdown = webdriver.findElement(By.xpath(OR.getProperty("timeLimitInput")));
		// timedropdown.clear();
		// timedropdown.click();
		Thread.sleep(500);
		// List<WebElement> optionsoftimelimit =
		// webdriver.findElements(By.xpath(OR.getProperty("timelimitoptions")));
		Select optionsoftimelimit = new Select(timedropdown);
		System.out.println("select options" + optionsoftimelimit);
		optionsoftimelimit.selectByIndex(1);
		// optionsoftimelimit.selectByIndex(2);
		selectoption = optionsoftimelimit.getFirstSelectedOption().getText();
		System.out.println("option " + optionsoftimelimit);

		return selectoption;
	}

	// Ensure there are time duration options available for selection in the
	// different time units (eg, 1 hour, 1 minute).

	public String verifytimedurationoption1() throws Exception {
		String selectoption = null;

		WebElement timedropdown = webdriver.findElement(By.xpath(OR.getProperty("timeLimitInput")));
		// timedropdown.clear();
		// timedropdown.click();
		Thread.sleep(500);
		// List<WebElement> optionsoftimelimit =
		// webdriver.findElements(By.xpath(OR.getProperty("timelimitoptions")));
		Select optionsoftimelimit = new Select(timedropdown);
		System.out.println("select options" + optionsoftimelimit);
		optionsoftimelimit.selectByIndex(1);
		// optionsoftimelimit.selectByIndex(2);
		selectoption = optionsoftimelimit.getFirstSelectedOption().getText();
		System.out.println("option " + optionsoftimelimit);

		return selectoption;
	}

	public String verifyreminderperiodoption(String dropdownValue) throws Exception {
		String selectoption = null;
		try {
			WebElement reminderdropdown = webdriver.findElement(By.xpath(OR.getProperty("reminderLimitInput")));
			// reminderdropdown.clear();
			// reminderdropdown.click();
			Thread.sleep(500);
			// List<WebElement> optionsoftimelimit =
			// webdriver.findElements(By.xpath(OR.getProperty("timelimitoptions")));
			Select optionsoftimelimit = new Select(reminderdropdown);
			System.out.println("select options" + optionsoftimelimit);
			optionsoftimelimit.selectByVisibleText(dropdownValue);
			// optionsoftimelimit.selectByIndex(2);
			selectoption = optionsoftimelimit.getFirstSelectedOption().getText();
			System.out.println("option " + optionsoftimelimit);

		} catch (Exception e) {

		}

		return selectoption;
	}

	/*
	 * Date: 06/04/2017 Author: Author: Ashish Kshatriya Description: This function
	 * is used for take screenshots in application. Parameter: WebDriver driver
	 */
	public String createScreenshot(WebDriver webdriver, String deviceName) throws InterruptedException {
		// UUID uuid = UUID.randomUUID();

		/*
		 * screenshotsFolder = "ImageScreenshot//Mobile//"; File dir = new
		 * File(screenshotsFolder); dir.mkdirs();
		 */
		String imageLocation = Constant.OUTPUTIMAGEFOLDER;
		File scrFile = ((TakesScreenshot) webdriver).getScreenshotAs(OutputType.FILE);
		Thread.sleep(1000);
		try {
			FileUtils.copyFile(scrFile, new File(imageLocation + deviceName + "_Actual" + ".jpg"));
		} catch (IOException e) {
			System.out.println("Error while generating screenshot:\n" + e.toString());
		}
		return imageLocation + deviceName + "_Actual" + ".jpg";
	}

	/*
	 * Date: 10/04/2017 Author: Author: Ashish Kshatriya Description: This function
	 * is used for close save password popup in chrome browser Parameter: NA
	 */
	/*
	 * public WebElement closeSavePasswordPopup() { WebElement closepopup=null; try{
	 * webdriver.context("NATIVE_APP");
	 * //Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.
	 * getProperty("closeSavePassword")))); boolean
	 * pop=webdriver.findElements(By.xpath(OR.getProperty("closeSavePassword")))
	 * .size()>0; if(pop){ closepopup=webdriver.findElement(By.xpath(OR.getProperty(
	 * "closeSavePassword"))); closepopup.click(); webdriver.context("CHROMIUM"); }
	 * else { webdriver.context("CHROMIUM"); } }catch(Exception e) {
	 * e.printStackTrace(); } return closepopup; }
	 */

	public String[] sessionToSlotsummary() {
		String[] sessionSlotSummary = new String[6];
		Wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath(OR.getProperty("endSessionButtonSessionReminder"))));
		webdriver.findElement(By.xpath(OR.getProperty("endSessionButtonSessionReminder"))).click();

		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.getProperty("SlotGameSummaryHeader"))));
		String slotGameSummaryHeader = webdriver.findElement(By.xpath(OR.getProperty("SlotGameSummaryHeader")))
				.getText().trim();
		System.out.println(slotGameSummaryHeader);
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.getProperty("TotalAmountWagered"))));
		String totalAmountWagered = webdriver.findElement(By.xpath(OR.getProperty("TotalAmountWagered"))).getText()
				.trim();
		System.out.println("Total Amount Wagered :" + totalAmountWagered);

		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.getProperty("TotalAmountWon"))));
		String totalAmountWon = webdriver.findElement(By.xpath(OR.getProperty("TotalAmountWon"))).getText().trim();
		System.out.println("Total Amount Won: " + totalAmountWon);

		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.getProperty("SessionBalance"))));
		String sessionBalance1 = webdriver.findElement(By.xpath(OR.getProperty("SessionBalance"))).getText().trim();
		System.out.println("session: " + sessionBalance1);
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.getProperty("SummarySlot"))));
		String playMoretext = webdriver.findElement(By.xpath(OR.getProperty("SummarySlot"))).getText().trim();
		System.out.println("PlayMore: " + playMoretext);
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.getProperty("CloseButton"))));
		String closeButton = webdriver.findElement(By.xpath(OR.getProperty("CloseButton"))).getText().trim();
		System.out.println("CloseButton: " + closeButton);
		sessionSlotSummary[0] = slotGameSummaryHeader;
		sessionSlotSummary[1] = totalAmountWagered;
		sessionSlotSummary[2] = totalAmountWon;
		sessionSlotSummary[3] = sessionBalance1;
		sessionSlotSummary[4] = playMoretext;
		sessionSlotSummary[5] = closeButton;

		System.out.println("SessionSummary: " + sessionSlotSummary);
		return sessionSlotSummary;
	}

	/*
	 * Author : Anand Bhayre Description: To fill Spain Start Session Form Param:
	 * Time Limit, Reminder Period, Loss Limit and prevent Future Slot Game for Play
	 * Return: Boolean value
	 */

	public boolean fillSatrtSessionForm(String TimeLimit, String ReminderPeriod, String LossLimit,
			String PreventFuture) {
		boolean ret = false;
		Wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(OR.getProperty("timeLimitInput"))));
		Select sel = new Select(webdriver.findElement(By.xpath(OR.getProperty("timeLimitInput"))));
		sel.selectByVisibleText(TimeLimit);
		sel = new Select(webdriver.findElement(By.xpath(OR.getProperty("reminderLimitInput"))));
		sel.selectByVisibleText(ReminderPeriod);
		webdriver.findElement(By.xpath(OR.getProperty("lossLimitInput"))).sendKeys(LossLimit);
		sel = new Select(webdriver.findElement(By.xpath(OR.getProperty("preventfutureslotgamefor"))));
		sel.selectByVisibleText(PreventFuture);

		webdriver.findElement(By.xpath(OR.getProperty("setlimitbutton"))).click();

		Wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(OR.getProperty("okButton"))));
		webdriver.findElement(By.xpath(OR.getProperty("okButton"))).click();
		// closeSavePasswordPopup();
		ret = isElementExist("newFeature", 120);

		return ret;

	}

	/*
	 * public boolean fillSatrtSessionForm1(String TimeLimit, String ReminderPeriod,
	 * String LossLimit, String PreventFuture) { boolean ret=false;
	 * Wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(
	 * OR.getProperty("timeLimitInput")))); Select sel= new
	 * Select(webdriver.findElement(By.xpath(OR.getProperty("timeLimitInput")))) ;
	 * sel.selectByVisibleText(TimeLimit); sel= new
	 * Select(webdriver.findElement(By.xpath(OR.getProperty("reminderLimitInput"
	 * )))); sel.selectByVisibleText(ReminderPeriod);
	 * webdriver.findElement(By.xpath(OR.getProperty("lossLimitInput"))).
	 * sendKeys(LossLimit); sel= new
	 * Select(webdriver.findElement(By.xpath(OR.getProperty(
	 * "preventfutureslotgamefor")))); sel.selectByVisibleText(PreventFuture);
	 * 
	 * //webdriver.findElement(By.xpath(OR.getProperty("setlimitbutton"))).click ();
	 * 
	 * Wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(
	 * OR.getProperty("setlimitbutton"))));
	 * //webdriver.findElement(By.xpath(OR.getProperty("okButton"))).click();
	 * closeSavePasswordPopup(); ret=isElementExist("setlimitbutton", 120);
	 * 
	 * return ret;
	 * 
	 * }
	 */
	/*
	 * Date: 10/04/2017 Author: Author: Ashish Kshatriya Description: This function
	 * is used for close save password popup in chrome browser Parameter: NA
	 * 
	 * public WebElement closeSavePasswordPopup() { WebElement closepopup=null; try{
	 * webdriver.context("NATIVE_APP");
	 * //Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.
	 * getProperty("closeSavePassword")))); boolean
	 * pop=webdriver.findElements(By.xpath(OR.getProperty("closeSavePassword")))
	 * .size()>0; if(pop){ closepopup=webdriver.findElement(By.xpath(OR.getProperty(
	 * "closeSavePassword"))); closepopup.click(); webdriver.context("CHROMIUM"); }
	 * else { webdriver.context("CHROMIUM"); } }catch(Exception e) {
	 * e.printStackTrace(); } return closepopup; }
	 */
	/*
	 * Author: Anand Description: To test element is present or not Param: Key of
	 * element's xpath from OR Return: Boolean value
	 */

	public boolean isElementExist(String keyName, int timeInSeconds) {
		boolean ret = false;
		try {
			webdriver.manage().timeouts().implicitlyWait(timeInSeconds, TimeUnit.SECONDS);
			// WebDriverWait wait= new WebDriverWait(webdriver,timeInSeconds);
			// wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.getProperty(keyName))));
			// webdriver.findElements( By.id("...") ).size() != 0;
			if (webdriver.findElements(By.xpath(OR.getProperty(keyName))).size() > 0) {
				ret = true;
			}
		} catch (Exception e) {
			webdriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		}
		return ret;
	}

	/*
	 * Author: Anand Bhayre Description: Click on given element Param: Element Xpath
	 * Key -Target -Expected Return: boolean value
	 */
	public boolean clickOnElement(String targetElementKey, String expectedElementKey) {
		Wait.until(ExpectedConditions.elementToBeClickable(By.xpath(OR.getProperty(targetElementKey)))).click();
		return isElementExist(expectedElementKey, 60);

	}

	/* verify the quick spin toggle */
	public String quickSpinStatus() {
		return null;
	}

	/**
	 * Author:Sneha Jawarkar This method is used for verifying the status of the
	 * quick spin
	 * 
	 * @return true
	 */
	public boolean quickSpinStatus_verify() {
		return false;
	}

	/* Click on Menu close back arrow */
	public boolean MenuBackArrow() {
		/*
		 * webdriver.findElement(By.xpath(XpathMap.get("OneDesignMenuClose"))). click();
		 */
		return true;
	}

	/* Click on setting close back arrow */
	public boolean SettingBackArrow() {
		/*
		 * webdriver.findElement(By.xpath(XpathMap.get("OneDesignSettingclose"))
		 * ).click();
		 */

		return true;
	}

	public String clickOnSetLimitWithoutinput() {
		try {

			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.getProperty("setlimitbutton"))));
			WebElement setlimt = webdriver.findElement(By.xpath(OR.getProperty("setlimitbutton")));
			setlimt.click();

			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.getProperty("messagetimeLimit"))));
			message1 = webdriver.findElement(By.xpath(OR.getProperty("messagetimeLimit"))).getText();
		}

		catch (Exception e) {

			e.printStackTrace();
		}
		return message1;

	}

	public String EndSession() {

		return null;

	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String[] baseHeaderValues() {

		return null;
	}

	/**
	 * 
	 */

	public String clickOnClose() {

		return null;

	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */

	public String sendLossLimitData() throws Exception {
		return null;
	}

	public boolean fillStartSessionLossForm(String LossLimit, Desktop_HTML_Report reportSpain) {
		Wait = new WebDriverWait(webdriver, 500);
		boolean ret = false;
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("spain_TimeLimit"))));

			Select sel = new Select(webdriver.findElement(By.xpath(XpathMap.get("spain_TimeLimit"))));
			sel.selectByIndex(1);
			Thread.sleep(2000);
			reportSpain.detailsAppend("Select Time limit in Session overlay", "Time limit should be selected",
					"Time limit is selected", "Pass");

			sel = new Select(webdriver.findElement(By.xpath(XpathMap.get("spain_ReminderPeriod"))));
			sel.selectByIndex(1);
			Thread.sleep(1000);
			reportSpain.detailsAppend("Select Reminder period in Session overlay", "Reminder period should be selected",
					"Reminder period is selected", "Pass");

			webdriver.findElement(By.xpath(XpathMap.get("spain_LossLimit"))).sendKeys(LossLimit);
			Thread.sleep(2000);
			reportSpain.detailsAppend("Fill loss limit in Session overlay", "Loss limit should be filled",
					"Losslimit is filed", "Pass");
			Thread.sleep(2000);

			sel = new Select(webdriver.findElement(By.xpath(XpathMap.get("spainSelectCoolingOffPeriod"))));
			sel.selectByIndex(1);
			reportSpain.detailsAppend("Select Cooling off period", "Cooling off periodshould be selected",
					"Cooling off period is selected", "Pass");
			Thread.sleep(2000);

			webdriver.findElement(By.xpath(XpathMap.get("spain_SetLimits"))).click();
			Thread.sleep(1000);
			ret = true;

		} catch (Exception e) {

			e.printStackTrace();
		}
		return ret;
	}

	public String waitUntilSessionLoss() {

		String title = null;
		try {
			for (int i = 0; i <= 10; i++) {
				Wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id(XpathMap.get("Spin_Button_ID"))));
				spinclick();
				Thread.sleep(3000);
				boolean b = webdriver.findElements(By.xpath(XpathMap.get("spain_lossLimitDialogueOK"))).size() > 0;
				if (b) {
					System.out.println("Loss Limit is reached");
					title = webdriver.findElement(By.xpath(XpathMap.get("spain_lossLimitDialogueOK"))).getText();
					break;
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return title;

	}

	public boolean waitUntilSessionReminder() {
		WebDriverWait Wait = new WebDriverWait(webdriver, 500);
		boolean header = false;
		try {

			log.debug("Waiting for SessionReminder Continue overlay");
			System.out.println("Waiting for SessionReminder overlay");
			Wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath(XpathMap.get("spain_SessionReminderContinue"))));
			header = true;

		} catch (Exception e) {

			log.error("Error in waiting for session reminder ", e);
		}
		return header;

	}

	public void closeSessionLossPopup() {
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("spain_CloseBtn"))));
			webdriver.findElement(By.xpath(XpathMap.get("spain_CloseBtn"))).click();
		} catch (Exception e) {
			log.error("Error in checking in close session loss pop up ", e);
		}
	}

	public void coolingOffPeriod(Desktop_HTML_Report tc10) {
		try {
			func_Click(XpathMap.get("five_Reel_slot"));

			JavascriptExecutor js = ((JavascriptExecutor) webdriver);
			js.executeScript("arguments[0].scrollIntoView(true);", webdriver.findElement(By.id(GameName)));

			js.executeScript("arguments[0].click();", webdriver.findElement(By.id(GameName)));

			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("spain_CloseBtn"))));
			tc10.detailsAppend("To check if Cooling Off Period Overlay appear on launching same game",
					"Cooling Off Period Overlay should appear on launching same game",
					"Cooling Off Period Overlay appears on launching same game", "Pass");
			func_Click(XpathMap.get("spain_CloseBtn"));
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("five_Reel_slotID"))));
			webdriver.findElement(By.id(XpathMap.get("five_Reel_slotID"))).click();
			;
			tc10.detailsAppend(
					"To verify user navigates to lobby after clicking on close button when cooling off period is running",
					"User should redirect to lobby after clicking on close button when cooling off period is running",
					"User is redirected to lobby", "Pass");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void spainContinueSession() {
		try {
			webdriver.findElement(By.xpath(XpathMap.get("spain_SessionReminderContinue"))).click();
			System.out.println("clicked on continue in session reminder");
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public void coolingOffPeriodNewGame(String gamename, Desktop_HTML_Report tc10) {
		try {

			JavascriptExecutor js = ((JavascriptExecutor) webdriver);
			js.executeScript("arguments[0].scrollIntoView(true);", webdriver.findElement(By.id(gamename)));

			js.executeScript("arguments[0].click();", webdriver.findElement(By.id(gamename)));

			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("spain_CloseBtn"))));
			tc10.detailsAppend("To check if Cooling Off Period Overlay appear on launching another game",
					"Cooling Off Period Overlay should appear on launching another game",
					"Cooling Off Period Overlay appears on launching another game", "Pass");
			func_Click(XpathMap.get("spain_CloseBtn"));
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("five_Reel_slotID"))));
			tc10.detailsAppend(
					"To verify user navigates to lobby after clicking on close button when cooling off period is running",
					"User should redirect to lobby after clicking on close button when cooling off period is running",
					"User is redirected to lobby", "Pass");

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void waitUntilTimeLimitSession(Desktop_HTML_Report tc10) {
		try {

			for (int i = 0; i <= 5; i++) {
				spainContinueSession();
				boolean b = webdriver.findElements(By.xpath(XpathMap.get("spain_CloseBtn"))).size() > 0;
				if (b) {
					break;
				}
			}
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("spain_CloseBtn"))));
			tc10.detailsAppend("To check if Time Limit Summary overlay appear when Time limit is reached",
					"Time Limit Summary overlay should appear", "Time Limit summary overlay appears", "Pass");
			func_Click(XpathMap.get("spain_CloseBtn"));
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("five_Reel_slotID"))));
			tc10.detailsAppend(
					"To verify user navigates to lobby after clicking on close button when Time limit is reached",
					"User should redirect to lobby after clicking on close button when Time limit is reached",
					"User is redirected to lobby", "Pass");
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public void relaunchGame() {

		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("five_Reel_slotID"))));
		func_Click(XpathMap.get("five_Reel_slot"));

		JavascriptExecutor js = ((JavascriptExecutor) webdriver);
		js.executeScript("arguments[0].scrollIntoView(true);", webdriver.findElement(By.id(GameName)));

		js.executeScript("arguments[0].click();", webdriver.findElement(By.id(GameName)));

		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("spain_SlotGameOverlay"))));
	}

	/***
	 * Date: 10/1/2018 Autohr: Laxmikanth Kodam Description: This function used for
	 * navigating to Gibraltar url
	 * 
	 * @return true
	 */
	public String func_navigate_DirectURL(String urlNavigate) {
		Wait = new WebDriverWait(webdriver, 500);
		String title = null;
		try {

			// urlNavigate=XpathMap.get("app_url_Gibraltar_Desktop");
			webdriver.navigate().to(urlNavigate);

			newFeature();
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("clock"))));

			title = webdriver.getTitle();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return title;
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String clickForScreen(String ac, String aa) {

		return null;
	}

	public String verifyreminderperiodoption() throws Exception {
		String selectoption = null;
		WebElement reminderdropdown = webdriver.findElement(By.xpath(OR.getProperty("reminderLimitInput")));
		// reminderdropdown.clear();
		// reminderdropdown.click();
		Thread.sleep(500);
		// List<WebElement> optionsoftimelimit =
		// webdriver.findElements(By.xpath(OR.getProperty("timelimitoptions")));
		Select optionsoftimelimit = new Select(reminderdropdown);
		System.out.println("select options" + optionsoftimelimit);
		optionsoftimelimit.selectByIndex(1);
		// optionsoftimelimit.selectByIndex(2);
		selectoption = optionsoftimelimit.getFirstSelectedOption().getText();
		System.out.println("option " + optionsoftimelimit);

		return selectoption;
	}

	public String verifyfuturepreventoption(String dropdownVaue) throws Exception {
		String selectoption = null;
		try {
			WebElement timedropdown = webdriver.findElement(By.xpath(OR.getProperty("preventfutureslotgamefor")));
			// timedropdown.clear();
			// timedropdown.click();
			Thread.sleep(500);
			// List<WebElement> optionsoftimelimit =
			// webdriver.findElements(By.xpath(OR.getProperty("timelimitoptions")));
			Select optionsoftimelimit = new Select(timedropdown);
			System.out.println("select options" + optionsoftimelimit);
			optionsoftimelimit.selectByVisibleText(dropdownVaue);
			// optionsoftimelimit.selectByIndex(2);
			selectoption = optionsoftimelimit.getFirstSelectedOption().getText();
			System.out.println("option " + optionsoftimelimit);

		} catch (Exception e) {
			e.getMessage();
		}

		return selectoption;
	}

	public void spainStartNewSession(Desktop_HTML_Report reportSpain) {
		WebDriverWait wait = new WebDriverWait(webdriver, 10);
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("spain_StartNewSession"))));
			boolean test = webdriver.findElements(By.xpath(XpathMap.get("spain_StartNewSession"))).size() > 0;
			if (test) {
				reportSpain.detailsAppend("Verify that Start New Sessiosn Overlay appears before the game loads ",
						"Start New Sessiosn Overlay should appear before the game loads",
						"Start New Sessiosn Overlay is appearing before the game loads", "Pass");
				func_Click(XpathMap.get("spain_StartNewSession"));
			} else {
				System.out.println("Slot Game limit overlay appears");
			}
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public String verifyfuturepreventoption() throws Exception {
		String selectoption = null;

		WebElement timedropdown = webdriver.findElement(By.xpath(OR.getProperty("preventfutureslotgamefor")));
		// timedropdown.clear();
		// timedropdown.click();
		Thread.sleep(500);
		// List<WebElement> optionsoftimelimit =
		// webdriver.findElements(By.xpath(OR.getProperty("timelimitoptions")));
		Select optionsoftimelimit = new Select(timedropdown);
		System.out.println("select options" + optionsoftimelimit);
		optionsoftimelimit.selectByIndex(1);
		// optionsoftimelimit.selectByIndex(2);
		selectoption = optionsoftimelimit.getFirstSelectedOption().getText();
		System.out.println("option " + optionsoftimelimit);

		return selectoption;
	}

	/* verify message when reminder input selected */
	public String verifyremindermessage() {
		String reminder = null;

		try {
			verifyreminderperiodoption();

			WebElement message = webdriver.findElement(By.xpath(OR.getProperty("inputboxselectmessage")));
			reminder = message.getText();

		} catch (Exception e) {

		}
		return reminder;
	}
	/* verify message when input box is loss limit */

	public String verifylosslimitmessage() {
		String lossslimit = null;

		try {
			WebElement losslimitinputbox = webdriver.findElement(By.xpath(OR.getProperty("lossLimitInput")));
			losslimitinputbox.click();

			WebElement message = webdriver.findElement(By.xpath(OR.getProperty("inputboxselectmessage")));
			lossslimit = message.getText();
		} catch (Exception e) {

		}
		return lossslimit;
	}

	/* verify message when input box is loss limit */

	public String preventfuturemessage() {
		String preventfuture = null;
		try {
			verifyfuturepreventoption();

			WebElement message = webdriver.findElement(By.xpath(OR.getProperty("inputboxselectmessage")));
			preventfuture = message.getText();

		} catch (Exception e) {

		}
		return preventfuture;
	}

	/* verify the loss limit error validation message */
	public boolean isElementPresent(String locator) {
		boolean isPresent = false;
		try {
			webdriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			if (webdriver.findElements(By.xpath(locator)).size() > 0) {
				log.debug("error is coming while loading language");
				isPresent = true;
			}
			webdriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		} catch (Exception e) {
			log.error("exception while searching for loading error ", e);
		}
		return isPresent;
	}

	/* Verify the last Login Pop up */

	public boolean lastLoginPopup() {

		WebElement lastlogin = webdriver.findElement(By.xpath(OR.getProperty("lastLoginPopup")));
		lastlogin.click();

		return true;
	}

	public String verifySessionReminderLimit() {
		// Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.getProperty("limitSessionReminder"))));
		timeLimitSessionReminder = webdriver.findElement(By.xpath(OR.getProperty("limitSessionReminder"))).getText();
		return timeLimitSessionReminder;
	}

	public String getText(String elementKeyName) {
		return webdriver.findElement(By.xpath(elementKeyName)).getText();
	}

	public String verifySessionReminderCurrent() {

		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.getProperty("currentSessionReminder"))));
		sessionReminderCurrent = webdriver.findElement(By.xpath(OR.getProperty("currentSessionReminder"))).getText();

		return sessionReminderCurrent;

	}

	public String endSessionButtonSessionReminder() {

		Wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath(OR.getProperty("endSessionButtonSessionReminder"))));
		endSessionButton = webdriver.findElement(By.xpath(OR.getProperty("endSessionButtonSessionReminder"))).getText();

		return endSessionButton;

	}

	public String continueButtonSessionReminder() {

		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.getProperty("continueSessionReminder"))));
		continueButton = webdriver.findElement(By.xpath(OR.getProperty("continueSessionReminder"))).getText();

		return continueButton;
	}

	public String timeLimit() {

		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.getProperty("timeLimitSessionReminder"))));
		timeLimit = webdriver.findElement(By.xpath(OR.getProperty("timeLimitSessionReminder"))).getText();

		return timeLimit;

	}

	public String lossLimitSessionReminder() {

		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.getProperty("lossLimitSessionReminder"))));
		lossLimitSessionReminder = webdriver.findElement(By.xpath(OR.getProperty("lossLimitSessionReminder")))
				.getText();

		return lossLimitSessionReminder;

	}

	public String sesionBalanceSessionReminder() {

		Wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath(OR.getProperty("sessionBalanceSessionReminder"))));
		sessionBalanceSessionReminder = webdriver.findElement(By.xpath(OR.getProperty("sessionBalanceSessionReminder")))
				.getText();

		return sessionBalanceSessionReminder;

	}

	public String timeElaspeseSessionReminder() {

		Wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.getProperty("timeElapsedSessionReminder"))));
		timeElapsedsessionreminder = webdriver.findElement(By.xpath(OR.getProperty("timeElapsedSessionReminder")))
				.getText();

		return timeElapsedsessionreminder;

	}

	public String sessionBalanceValueSessionReminder() {

		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.getProperty("timeLimitSessionReminder"))));
		sessionBalance = webdriver.findElement(By.xpath(OR.getProperty("timeLimitSessionReminder"))).getText();

		return sessionBalance;

	}

	public String lossLimitValueSessionreminder() {

		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.getProperty("losslimitValue"))));
		sessionBalance = webdriver.findElement(By.xpath(OR.getProperty("losslimitValue"))).getText();

		return sessionBalance;

	}

	public String logout_spainform() throws Exception {
		String loginTitle = null;
		try {

			Wait.until(ExpectedConditions.elementToBeClickable(By.id(OR.getProperty("accountsLobbyID"))));
			Thread.sleep(2000);
			webdriver.findElement(By.id(OR.getProperty("accountsLobbyID"))).click(); // Clickin
																						// on
																						// Accounts
			Thread.sleep(1000);
			Wait.until(ExpectedConditions.elementToBeClickable(By.id(OR.getProperty("logoutbuttonId"))));
			Thread.sleep(1000);
			webdriver.findElement(By.id(OR.getProperty("logoutbuttonId"))).click(); // Clicking
																					// on
																					// log
																					// out
																					// button
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(OR.getProperty("LoginTitleid"))));
			Thread.sleep(1000);
			loginTitle = webdriver.findElement(By.id(OR.getProperty("LoginTitleid"))).getText(); // Clicking
																									// on
																									// log
																									// out
																									// button
			webdriver.findElement(By.id(OR.getProperty("closeButtonid"))).click(); // Clicking
																					// on
																					// log
																					// out
																					// button
			Thread.sleep(1000);

			/*
			 * Wait.until(ExpectedConditions.elementToBeClickable(By.id(OR.
			 * getProperty("accountsLobbyID")))); Thread.sleep(2000);
			 * Wait.until(ExpectedConditions.elementToBeClickable(By.id(OR.
			 * getProperty("closeButtonid"))));
			 * webdriver.findElement(By.id(OR.getProperty("closeButtonid"))). click();
			 * //Wait.until(ExpectedConditions.elementToBeClickable(By.id(OR.
			 * getProperty("accountsLobbyID")))); // Thread.sleep(2000);
			 * logout=webdriver.getTitle(); //title Spain
			 */

		}

		catch (Exception e) {
			e.printStackTrace();
			evalException(e);
		}
		return loginTitle;
	}

	public String verifybacktolobby() throws Exception {
		String backtolobbytitle = null;
		try {
			// click on hyperlink
			WebElement backtolobby1 = webdriver.findElement(By.xpath(OR.getProperty("backtolobby")));
			backtolobby1.click();
			Thread.sleep(2000);
			// Back To Lobby
			backtolobbytitle = webdriver.getTitle();
		} catch (Exception e) {
			evalException(e);
		}
		return backtolobbytitle;
	}

	/*
	 * 
	 */
	public String waitUntilSession() {

		return null;

	}

	/* verify Time limit error message */

	public String verifyTimeLimitError(String losslimit) {
		String error = null;

		try {

			// select time reminder
			verifyreminderperiodoption();
			// select future prevent
			verifyfuturepreventoption();
			// enter loss limit value
			WebElement losslimitinputbox = webdriver.findElement(By.xpath(OR.getProperty("lossLimitInput")));
			losslimitinputbox.clear();
			losslimitinputbox.sendKeys(losslimit);
			// click on set limit button
			WebElement setlimt = webdriver.findElement(By.xpath(OR.getProperty("setlimitbutton")));
			setlimt.click();
			WebElement message = webdriver.findElement(By.xpath(OR.getProperty("inputboxselectmessage")));
			error = message.getText();

		} catch (Exception e) {

		}
		return error;
	}

	public void clickIcon(Desktop_HTML_Report report) throws Exception {

	}

	/*
	 * this method is of no use only decleration is here,method is for component
	 * store
	 */
	public String Slider_TotalBetamnt() {
		return null;
	}

	public String BaseGame_TotalBetmnt() {

		return null;
	}

	/*
	 * this method is of no use only decleration is here,method is for component
	 * store
	 */
	public void Coinselectorclose() throws InterruptedException {
	}

	/*
	 * this method is of no use only decleration is here,method is for component
	 * store
	 */
	public void moveCoinSizeSlider() throws InterruptedException {
	}
	/* verify Reminder period error message */

	public String verifyremindererror(String losslimit) {
		String error = null;
		String losslimit1 = null;
		try {
			// select time limit
			losslimit1 = losslimit;
			verifytimedurationoption();
			// enter loss limit
			WebElement losslimitinputbox = webdriver.findElement(By.xpath(OR.getProperty("lossLimitInput")));
			losslimitinputbox.clear();
			losslimitinputbox.sendKeys(losslimit1);
			// select prevent future
			verifyfuturepreventoption();
			// click on set limit button
			WebElement setlimt = webdriver.findElement(By.xpath(OR.getProperty("setlimitbutton")));
			setlimt.click();
			// get error message
			WebElement message = webdriver.findElement(By.xpath(OR.getProperty("inputboxselectmessage")));
			error = message.getText();
		} catch (Exception e) {

		}
		return error;

	}

	/* verify loss limit blank error message */

	public String verifylosslimiterror(String losslimitdata) {
		String losslimiterror = null;

		String losslimitdata1 = losslimitdata;
		double losslimitdata2;
		String losslimit = null;
		int losslimitdataint = 0;
		try { // select time duration
			verifytimedurationoption();
			// select reminder period
			verifyreminderperiodoption();
			// select prevent future
			verifyfuturepreventoption();
			losslimitdata2 = Double.parseDouble(losslimitdata1);
			// losslimitdata3=losslimitdata2.toString();
			losslimitdataint = (int) losslimitdata2;
			double losslimitdatadouble = (double) losslimitdata2;
			WebElement losslimitinputbox = webdriver.findElement(By.xpath(OR.getProperty("lossLimitInput")));
			losslimitinputbox.clear();
			if (losslimitdataint == 0) {
				losslimit = String.valueOf(losslimitdataint).toString();
				losslimitinputbox.sendKeys(losslimit);
			}

			else if (losslimitdata2 > 1000) {

				losslimit = String.valueOf(losslimitdatadouble).toString();
				losslimitinputbox.clear();
				losslimitinputbox.sendKeys(losslimit);

			}
			// click on setlimit
			WebElement setlimt = webdriver.findElement(By.xpath(OR.getProperty("setlimitbutton")));
			setlimt.click();

			// get error message
			WebElement message = webdriver.findElement(By.xpath(OR.getProperty("inputboxselectmessage")));
			losslimiterror = message.getText();

		} catch (Exception e) {

		}
		return losslimiterror;
	}

	// verify refresh function

	public String refreshpage() throws Exception {
		// refresh page
		String refresh = null;

		verifytimedurationoption();
		verifyreminderperiodoption();
		WebElement losslimitinputbox = webdriver.findElement(By.xpath(OR.getProperty("lossLimitInput")));
		losslimitinputbox.sendKeys("100");
		// refresh page
		webdriver.navigate().refresh();
		Thread.sleep(1000);
		Wait.until(ExpectedConditions.elementToBeClickable(By.xpath(OR.getProperty("backtolobby"))));
		WebElement backtolobby = webdriver.findElement(By.xpath(OR.getProperty("backtolobby")));
		refresh = backtolobby.getText();

		return refresh;

	}

	public String FS_summaryScreenClick() {
		String wait = "";
		return wait;
	}

	public String Func_navigate_again(String appName) {
		try {
			Wait = new WebDriverWait(webdriver, 300);
			Wait.until(ExpectedConditions.elementToBeClickable(By.id(OR.getProperty("LobbyfiveReels"))));
			boolean newGame = webdriver.findElements(By.id(OR.getProperty("LobbyNewGamesId"))).size() > 0;
			if (newGame) {
				webdriver.findElement(By.id(OR.getProperty("LobbyNewGamesId"))).click();
				waitForPageToBeReady();
				Thread.sleep(1000);
				boolean game = webdriver.findElements(By.id(appName)).size() > 0;
				if (game) {
					webdriver.findElement(By.id(appName)).click();
				} else {
					System.out.println(appName + " not available in the lobby");
				}

			} else {
				webdriver.findElement(By.id(OR.getProperty("LobbyfiveReels"))).click();
				waitForPageToBeReady();
				Thread.sleep(1000);
				boolean game = webdriver.findElements(By.id(appName)).size() > 0;
				if (game) {
					webdriver.findElement(By.id(appName)).click();
				} else {
					System.out.println(appName + " not available in the lobby");
				}
			}
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.getProperty("loginPopUp"))));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return GameDesktopName;
	}

	public void newTabOpen() {
		try {
			String windowhandle = webdriver.getWindowHandle();
			webdriver.switchTo().window(windowhandle);
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_T);
			robot.keyRelease(KeyEvent.VK_T);
			robot.keyRelease(KeyEvent.VK_CONTROL);

			// To check open windows
			ArrayList<String> tabs = new ArrayList<String>(webdriver.getWindowHandles());
			System.out.println(tabs.size());
			String window = tabs.get(1);
			webdriver.switchTo().window(window);
			webdriver.get("https://outlook.office.com");
			Thread.sleep(10000);
			webdriver.switchTo().window(tabs.get(0));
			Thread.sleep(5000);
			// closeOverlay();
			log.debug("Switch to default tab");
		} catch (Exception e) {
			log.error("Error on opening new tab");
		}
	}

	/*
	 * Date: 25/04/2019 Description:To verify autoplay must stop when focus being
	 * removed in UK . Parameter: NA
	 * 
	 * @return boolean
	 */

	public boolean Autoplay_focus_removed_UK() {
		boolean focus;
		try {
			clickAtButton("return " + XpathMap.get("ClickAutoPlayMoreOptionsBtn"));

			Thread.sleep(2000);
			webdriver.findElement(By.id(XpathMap.get("Start_Autoplay"))).click();
			Thread.sleep(3000);
			newTabOpen();

			if (GetConsoleBooleanText("return " + XpathMap.get("isAutoPlayActive")) == false)
				focus = true;
			else {
				focus = false;
				log.debug("Autoplay staus=" + GetConsoleBooleanText("return " + XpathMap.get("isAutoPlayActive")));
			}

			/*
			 * String windowhandle=webdriver.getWindowHandle();
			 * webdriver.switchTo().window(windowhandle); Robot robot= new Robot();
			 * robot.keyPress(KeyEvent.VK_CONTROL); robot.keyPress(KeyEvent.VK_T);
			 * robot.keyRelease(KeyEvent.VK_T); robot.keyRelease(KeyEvent.VK_CONTROL);
			 * 
			 * //To check open windows ArrayList<String> tabs=new
			 * ArrayList<String>(webdriver.getWindowHandles());
			 * System.out.println(tabs.size()); String window=tabs.get(1);
			 * webdriver.switchTo().window(window);
			 * webdriver.get("https://outlook.office.com"); Thread.sleep(10000);
			 * webdriver.switchTo().window(tabs.get(0)); Thread.sleep(5000);
			 * //closeOverlay(); log.debug("Switch to default tab");
			 */

			// driver.switchTo().defaultContent();

		} catch (Exception e) {
			log.error("Error on opening new tab", e);
			focus = false;
		}
		return focus;
	}

	/*
	 * Date: 25/04/2019 Description:To verify autoplay must stop when focus being
	 * removed. Parameter: NA
	 * 
	 * @return boolean
	 */
	public boolean Autoplay_focus_removed() {

		boolean focus;
		try {
			webdriver.findElement(By.id(XpathMap.get("AutoplayID"))).click();
			webdriver.findElement(By.xpath(XpathMap.get("Start_Autoplay"))).click();

			String windowhandle = webdriver.getWindowHandle();
			webdriver.switchTo().window(windowhandle);
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_T);
			robot.keyRelease(KeyEvent.VK_T);
			robot.keyRelease(KeyEvent.VK_CONTROL);

			// To check open windows
			ArrayList<String> tabs = new ArrayList<String>(webdriver.getWindowHandles());
			System.out.println(tabs.size());
			String window = tabs.get(1);
			webdriver.switchTo().window(window);
			webdriver.get("https://outlook.office.com");
			Thread.sleep(2000);
			webdriver.switchTo().window(tabs.get(0));
			log.debug("Switch to default tab");
			// driver.switchTo().defaultContent();

		} catch (Exception e) {
			log.error("Focus not get changed");
			log.error(e.getMessage());
			return focus = false;
		}
		// webdriver.getWindowHandle();
		return focus = true;

	}

	public boolean settingsOpen() throws InterruptedException {
		return true;
	}

	public boolean spinclickFSTrigger() throws InterruptedException {
		return true;
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

	public void resizeBrowser(int a, int b) {
		Dimension d = new Dimension(800, 480);
		// Resize current window to the set dimension
		webdriver.manage().window().setSize(d);
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
	 * Date:07/12/2017 Author:premlata Mishra This method is actually decleared no
	 * definition
	 * 
	 * @return true
	 */
	public String refreshWait() {
		return null;
	}

	public void SettingsToBasescen() throws InterruptedException {

	}

	public boolean betDecrease() throws InterruptedException {
		return true;
	}

	/**
	 * Date:07/12/2017 Author:Laxmikanth Kodam This method is to change the bet
	 * 
	 * @return true
	 */
	public double GetBetAmt() {
		return 0.0;
	}

	// ********************************Exception
	// Handler********************************************************//
	public void evalException(Exception ex) throws Exception {
		System.out.println("value of ex is " + ex);
		String exClass = ex.getClass().toString();
		if (exClass.contains("StaleElementReferenceException")) {
			repo1.detailsAppend("Execution Interrupted because of StaleElementReferenceException", "", "",
					"Interrupted");
		} else if (exClass.contains("NoSuchElementException")) {
			String gameplay = Fun_gameplayerror();
			if (gameplay != null) {
				errorLogout();
				repo1.detailsAppend(gameplay + " error occurred", "", "", "Interrupted");
			} else {
				repo1.detailsAppend("NoSuchElementException because of Element not found", "", "", "Interrupted");
			}
		} else if (exClass.contains("TimeoutException")) {
			String gameplay = Fun_gameplayerror();
			if (gameplay != null) {
				repo1.detailsAppend("General" + " error occurred", "", "", "Interrupted");
				errorLogout();

			} else {
				repo1.detailsAppend("TimeoutException because of Element not found", "", "", "Interrupted");
			}
		}

		else if (exClass.contains("InvalidElementStateException")) {
			repo1.detailsAppend("Execution Interrupted because of InvalidElementStateException", "", "", "Interrupted");
		} else if (exClass.contains("ElementNotVisibleException")) {
			repo1.detailsAppend("Execution Interrupted because of ElementNotVisibleException", "", "", "Interrupted");
		} else if (exClass.contains("ErrorInResponseException")) {
			repo1.detailsAppend("Execution Interrupted because of ErrorInResponseException", "", "", "Interrupted");
		} else if (exClass.contains("InvalidSwitchToTargetException")) {
			repo1.detailsAppend("Execution Interrupted because of InvalidSwitchToTargetException", "", "",
					"Interrupted");
		} else if (exClass.contains("NoSuchFrameException")) {
			repo1.detailsAppend("Execution Interrupted because of NoSuchFrameException", "", "", "Interrupted");
		} else if (exClass.contains("NoSuchWindowException")) {
			repo1.detailsAppend("Execution Interrupted because of NoSuchWindowException", "", "", "Interrupted");
		} else if (exClass.contains("NoSuchAttributeException")) {
			repo1.detailsAppend("Execution Interrupted because of NoSuchAttributeException", "", "", "Interrupted");
		} else if (exClass.contains("NoAlertPresentException")) {
			repo1.detailsAppend("Execution Interrupted because of NoAlertPresentException", "", "", "Interrupted");
		} else if (exClass.contains("ElementNotSelectableException")) {
			repo1.detailsAppend("Execution Interrupted because of ElementNotSelectableException", "", "",
					"Interrupted");
		} else if (exClass.contains("InvalidCookieDomainException")) {
			repo1.detailsAppend("Execution Interrupted because of InvalidCookieDomainException", "", "", "Interrupted");
		} else if (exClass.contains("UnableToSetCookieException")) {
			repo1.detailsAppend("Execution Interrupted because of UnableToSetCookieException", "", "", "Interrupted");
		} else if (exClass.contains("RemoteDriverServerException")) {
			repo1.detailsAppend("Execution Interrupted because of RemoteDriverServerException", "", "", "Interrupted");
		} else if (exClass.contains("TimeoutException")) {
			repo1.detailsAppend("Execution Interrupted because of TimeoutException", "", "", "Interrupted");
		} else if (exClass.contains("MoveTargetOutOfBoundsException")) {
			repo1.detailsAppend("Execution Interrupted because of MoveTargetOutOfBoundsException", "", "",
					"Interrupted");
		} else if (exClass.contains("UnexpectedTagNameException")) {
			repo1.detailsAppend("Execution Interrupted because of UnexpectedTagNameException", "", "", "Interrupted");
		} else if (exClass.contains("InvalidSelectorException")) {
			repo1.detailsAppend("Execution Interrupted because of InvalidSelectorException", "", "", "Interrupted");
		} else if (exClass.contains("ImeNotAvailableException")) {
			repo1.detailsAppend("Execution Interrupted because of ImeNotAvailableException", "", "", "Interrupted");
		} else if (exClass.contains("ImeActivationFailedException")) {
			repo1.detailsAppend("Execution Interrupted because of ImeActivationFailedException", "", "", "Interrupted");
		} else if (exClass.contains("UnhandledAlertException")) {
			repo1.detailsAppend("Execution Interrupted because of Unhandled message ALERT", "", "", "Interrupted");
			Thread.sleep(3000);
		} else if (exClass.contains("UnreachableBrowserException")) {
			func_Click(XpathMap.get("OneDesign_HomeIcon"));
			// String gameplay = Fun_gameplayerror();
			// if (gameplay != null) {
			errorLogout();
			repo1.detailsAppend("Execution Interrupted because of UnreachableBrowserException", "", "", "Interrupted");
			// } else {
			// repo1.details_append("NoSuchElementException because of Element not found",
			// "", "", "Interrupted");
			// }

		} else if (exClass.contains("WebDriverException")) {
			repo1.detailsAppend("Execution was Interrupted   due to BROWSER_TIMEOUT WebDriverException ", "", "",
					"Interrupted");
		} else if (exClass.contains("NullPointerException")) {
			String gameplay = Fun_gameplayerror();
			if (gameplay != null) {
				repo1.detailsAppend(gameplay + " error occurred", "", "", "Interrupted");
			} else {
				repo1.detailsAppend("NoSuchElementException because of Element not found", "", "", "Interrupted");
			}
		}

	}

	public String Func_LoginNew(String userName, String password, String gameName) {
		return null;
	}

	public boolean open_TotalBet() {
		boolean b = false;
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("clock"))));

			String BetX = "return Game.instance.__componentMap.BaseScene.__componentMap.desktopConsole.__componentMap.betBox.__componentMap.betLabel.properties.globalX";
			String BetY = "return Game.instance.__componentMap.BaseScene.__componentMap.desktopConsole.__componentMap.betBox.__componentMap.betLabel.properties.globalY";

			clickAtCoordinates(BetX, BetY);
			b = true;
		} catch (Exception e) {
			e.getMessage();
		}
		return b;
	}

	public void close_TotalBet() throws InterruptedException {

	}

	public void clickPlayForReal() {

	}

	public String Func_LoginPracticePlay2() {
		return Flag;
	}

	public boolean FSSceneLoading() {
		return true;
	}

	public void waitSummaryScreen() throws InterruptedException {

	}

	public String clickBonusSelection(int i) {
		return null;
	}

	public boolean waitforWinAmount(String currencyFormat, Desktop_HTML_Report currency, String currencyName) {
		return true;
	}

	public String getCurrencySymbol() {
		return null;
	}

	public boolean betCurrencySymbol(String currency) {
		return false;
	}

	public String getCurrentBet() {
		return null;
	}

	public void verifyStopLanguage(Desktop_HTML_Report language, String languageCode) {
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("clock"))));
			Thread.sleep(4000);

			String SpinX = "return Game.instance.__componentMap.BaseScene.__componentMap.desktopConsole.__componentMap.spinButton.properties.globalX";
			String SpinY = "return Game.instance.__componentMap.BaseScene.__componentMap.desktopConsole.__componentMap.spinButton.properties.globalY";

			clickAtCoordinates(SpinX, SpinY);
			Thread.sleep(1500);
			language.detailsAppend("verify the Stop button translation",
					"Stop button should translate as per respective language", "Stop button is displaying", "Pass");
		} catch (Exception e) {
			e.getMessage();
		}
	}

	/**
	 * This method is used to stop is avalable or not Author: Premlata Mishra
	 * 
	 * @return true
	 */
	public boolean VerifyStop(String imagepath) throws InterruptedException {
		boolean newimage;
		spinclick();
		Screen S = new Screen();

		org.sikuli.script.Pattern image = new org.sikuli.script.Pattern(imagepath);
		System.out.println("get image : " + image);
		try {
			org.sikuli.script.Region r = S.exists(image, 5);
			if (r != null) {
				System.out.println("image found");
				newimage = true;
			} else {
				System.out.println("image not found");
				newimage = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return newimage;
	}

	public String verifyFlag() {
		return null;
	}

	public void FS_Start() {

	}

	public void waitForSpinButton() {

	}

	public void jackpotSummary(Desktop_HTML_Report language, String languageCode) {

	}

	public String paytableSwipe(Desktop_HTML_Report language) {
		String paytable = "";
		try {
		} catch (Exception e) {
		}
		return paytable;
	}

	public boolean winHistoryClick() throws Exception {
		log.debug("Clicked on win history button");
		return true;
	}

	public void winHistoryClose() throws Exception {

	}

	/**
	 * ate:22/11/2017 Author:Laxmikanth Kodam This method used to remove the $
	 * symbol from credits balance
	 * 
	 * @return
	 */
	public String func_String_Operation(String value) {
		String str = value;
		String str1 = str.substring(1);
		return str1;
	}

	public void clickAtCoordinates(String cx, String cy) {
		try {
			JavascriptExecutor js = ((JavascriptExecutor) webdriver);

			// System.out.println(js.executeScript(cx));
			// System.out.println(js.executeScript(cy));
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
			if (dx == 0) {
				dx = 1;
			}
			if (dy == 0) {
				dy = 1;
			}

			// System.out.println("width:" +dx );
			// System.out.println("Height:" + dy);

			Thread.sleep(1000);
			Actions act = new Actions(webdriver);

			WebElement ele1 = webdriver.findElement(By.id("gameCanvas"));

			act.moveToElement(ele1, dx, dy).click().build().perform();

		} catch (Exception e) {
			log.error("exception on clicking", e);
			;
		}
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

	public boolean freeGamesExpriyScreen() {
		return false;
	}

	public String freeGamesContinueExpiry() {
		return null;
	}

	public void assignFreeGames(Desktop_HTML_Report language, String username) {
		try {
			webdriver.findElement(By.xpath(XpathMap.get("FreeGamesUsername"))).clear();
			webdriver.findElement(By.xpath(XpathMap.get("FreeGamesUsername"))).sendKeys(username);
			Thread.sleep(3000);
			webdriver.findElement(By.xpath("//*[@class='ui-menu-item-wrapper']")).click();
			webdriver.findElement(By.xpath(XpathMap.get("FreeGamesMultipleCheckBox"))).click();
			Thread.sleep(1000);
			webdriver.findElement(By.xpath(XpathMap.get("FreeGamesMultipleCount"))).clear();
			webdriver.findElement(By.xpath(XpathMap.get("FreeGamesMultipleCount"))).sendKeys("20");
			webdriver.findElement(By.xpath(XpathMap.get("FreeGamesSearchBox"))).sendKeys(GameName);
			Thread.sleep(3000);
			webdriver.findElement(By.xpath(XpathMap.get("FreeGameOffer1"))).click();
			webdriver.findElement(By.xpath(XpathMap.get("FreeGameOffer2"))).click();
			webdriver.findElement(By.xpath(XpathMap.get("FreeGamesAssignOffer"))).click();
			language.detailsAppend("Verify Free Games Offer assigned successfully",
					"Free Games offer should be assigned", "Free Games Offer assigned successfully", "Pass");
			Thread.sleep(5000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close_Autoplay() throws InterruptedException {

	}

	public String getConsoleText(String text) {
		String consoleText = null;
		try {
			JavascriptExecutor js = ((JavascriptExecutor) webdriver);
			consoleText = (String) js.executeScript(text);
			log.debug("Text read from console= " + consoleText);
		} catch (JavascriptException e) {
			log.error("Exception executing javascript, please check hook for component" + e.getMessage());
		}
		return consoleText;
	}

	public ArrayList<String> getConsoleList(String text) {
		ArrayList<String> list = null;
		try {
			JavascriptExecutor js = ((JavascriptExecutor) webdriver);
			list = (ArrayList<String>) js.executeScript(text);
		} catch (Exception e) {
			e.getMessage();
		}
		return list;
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

	public Map<String, String> getConsoleMap(String text) {
		Map<String, String> map = null;
		try {
			JavascriptExecutor js = ((JavascriptExecutor) webdriver);
			map = (Map<String, String>) js.executeScript(text);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			System.out.println(e.getMessage());
		}
		return map;
	}

	public Map<String, Long> getConsoleLongMap(String text) {
		Map<String, Long> map = null;
		try {
			JavascriptExecutor js = ((JavascriptExecutor) webdriver);
			map = (Map<String, Long>) js.executeScript(text);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			System.out.println(e.getMessage());
		}
		return map;
	}

	public Map<String, String> getConsoleStringMap(String text) {
		Map<String, String> map = null;
		try {
			JavascriptExecutor js = ((JavascriptExecutor) webdriver);
			map = (Map<String, String>) js.executeScript(text);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			System.out.println(e.getMessage());
		}
		return map;
	}

	/**
	 * *Author:Pramlata Mishra This method is used to get element text form game
	 * console
	 * 
	 * @throws InterruptedException
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

	public boolean GetConsoleBooleanText(String hook) {
		boolean consoleText = true;
		JavascriptExecutor js = ((JavascriptExecutor) webdriver);
		try {
			consoleText = (Boolean) js.executeScript(hook);
		} catch (JavascriptException e) {
			//log.error("Please check hook");
			//log.error(e.getMessage());
			consoleText = false;
		} catch (Exception e) {
			log.error(e.getMessage());
			consoleText = false;
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

	public void acceptAlert() {

	}

	/**
	 * * Date: 29/05/2018 Author: Premlata Mishra Description: This function is used
	 * to set the bet to minimum Parameter:
	 */
	public void setMinBet() {
		try {

		} catch (Exception e) {
			e.getMessage();
		}
	}

	/**
	 * Date:10-1-2018 Name:Premlata Mishra Description: this function is open
	 * paytable page
	 * 
	 * @throws Exception
	 */
	public void openPaytable() {
		try {
			func_Click(XpathMap.get("OneDesign_Hamburger"));
			Wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XpathMap.get("OneDesign_Paytable"))));
			func_Click(XpathMap.get("OneDesign_Paytable"));
			Thread.sleep(2000);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	/**
	 * * Date: 29/05/2018 Author: Premlata Mishra Description: This function is used
	 * to fetch payout from paytable
	 * 
	 * Parameter:
	 */
	public double gamepayout(String xpath) {
		double gamepayout_Double = 0.0;
		return gamepayout_Double;
	}

	public double gamepayout(ArrayList<String> symbolData, String paytablePayout) {
		double gamepayout_Double = 0.0;
		return gamepayout_Double;
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

	

	/**
	 * Date: 30/05/2018 Autohr: Havish Jain Description: This function used to
	 * handle the error
	 * 
	 * 
	 * 
	 */
	public void error_Handler(Desktop_HTML_Report report) {

		String error = XpathMap.get("Error");
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

	public Map<String, Integer> creditBubble() throws Exception {
		Map<String, Integer> userValue = new HashMap<>();
		int creditbubbleBalstr;
		int bonusInBubbleValdouble;
		int totalcreditValdouble = 0;
		try {
			Thread.sleep(1000);
			// Actions act = new Actions(webdriver);
			// act.moveByOffset(200, 200).click().build().perform();
			// clickAtCoordinates("return
			// theForce.game.automation.getControlById('BalanceComponent').Text.balanceText.world.x","return
			// theForce.game.automation.getControlById('BalanceComponent').Text.balanceText.world.y");
			// func_Click(XpathMap.get("Creditbubble"));
			Thread.sleep(1000);
			if (webdriver.findElement(By.xpath(XpathMap.get("Creditbubble"))).isDisplayed()) {
				creditbubbleBalstr = Integer.parseInt(verifycreditBubble(XpathMap.get("creditBubble_Balance")));

				bonusInBubbleValdouble = Integer.parseInt(verifycreditBubble(XpathMap.get("creditBubble_Bonus")));

				bonusInBubbleValdouble = Integer.parseInt(verifycreditBubble(XpathMap.get("CreditBubble_TB")));

				userValue.put("Balance", creditbubbleBalstr);
				userValue.put("Bonus", bonusInBubbleValdouble);
				userValue.put("TotalCredit", totalcreditValdouble);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return userValue;
	}

	public String verifycreditBubble(String locator) {
		String betValue = getText(locator);
		String bet = betValue.replaceAll("[^0-9]", "");
		return bet;
	}

	public void typeCasting(String cx, Coordinates coordinateObj) {
		JavascriptExecutor js = ((JavascriptExecutor) webdriver);
		try {
			Long sx = (Long) js.executeScript(cx);
			coordinateObj.setX(sx);
		} catch (Exception e) {
			Double sdx = (Double) js.executeScript(cx);
			Long sx = sdx.longValue();
			coordinateObj.setX(sx);
		}
	}

	public void clickAtCoordinates(Long sx, Long sy) {
		try {
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
			Thread.sleep(1000);
			Actions act = new Actions(webdriver);
			WebElement ele1 = webdriver.findElement(By.id("gameCanvas"));
			act.moveToElement(ele1, dx, dy).click().build().perform();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clickNextOffer() {
	}

	public String freeGamesDiscardExistingOffer() {
		return null;
	}

	public void clickBaseSceneDiscard() {
	}

	public boolean FSSceneLoading(int bonusSelection) {
		return true;
	}

	public String removeUTF8BOM(String s) {
		if (s.startsWith(UTF8_BOM)) {
			s = s.substring(1);
		}
		return s;
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
			String resourcePath = new String();
			JSONObject jsonObject3 = (JSONObject) jsonObject.get(s);
			resourcePath = jsonObject3.get("path").toString();

			// need to uncomment for pn game resourcePath =
			// (String)jsonObject.get(s);

			if (!resourcePath.contains("960x640") && !resourcePath.contains("1136x640"))
				al.add(resourcePath);
		}
		return (ArrayList<String>) al;
	}

	/*
	 * Below methods are for Pokerstar GTR Teast cases
	 */

	public String pokerStar_game_load(String url) {
		String gamename = XpathMap.get("RedirectGame");

		Wait = new WebDriverWait(webdriver, 500);
		try {
			webdriver.navigate().to(url);

			log.debug("navigated the url and  Poker Star Lobby  to be visible");
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("Config_Button"))));
			Thread.sleep(4000);
			System.out.println("Url  " + webdriver.getCurrentUrl());
			log.info("Nevigate to Poker star Lobby");

			String game_Xpth = "//span[contains(text(),'%s')]";
			String fullXpath = String.format(game_Xpth, gamename);
			System.out.println("Xpath" + fullXpath);
			webdriver.findElement(By.xpath(fullXpath)).click();
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("Dice_icon"))));
			log.debug("Clicked on game and waiting for game base scene");

		} catch (Exception e) {
			log.error("error in func_navigate", e);
		}
		return gamename;

	}

	/*
	 * Below methods are for Pokerstar GTR Teast cases
	 */

	public boolean openAutoplay() {
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
			WebElement webElement = webdriver.findElement(By.xpath((XpathMap.get("winLimit"))));
			Select selection = new Select(webElement);
			winLimitList = selection.getOptions();

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return winLimitList;
	}

	/**
	 * getWinLimitValues returns the list of Loss Limit Values
	 * 
	 * @return
	 */

	public List<WebElement> getLossLimitValues() {
		List<WebElement> winLimitList = null;
		try {
			Select selection = new Select(webdriver.findElement(By.xpath((XpathMap.get("lossLimit")))));
			winLimitList = selection.getOptions();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return winLimitList;
	}

	public boolean checkWinLimitReachedForBet(String winLimit, int counter) {
		return false;
	}

	public boolean checkLossLimitReachedForBet(String lossLimit, int counter) {
		return false;
	}

	public void setQuickSpinOff() {

	}

	public void setQuickSpinOn() {

	}

	public boolean isBetButtonAccessible() {
		return false;
	}

	public boolean isMaxButtonAccessible() {
		return false;
	}

	public boolean isAutoplayStoppedOnMenuClick() {
		return false;
	}

	public boolean isBetChangedIntheConsole(String betValue) {
		return false;
	}

	public String getCurrentCredits() {

		return null;
	}

	public String getCurrentCreditsforRegMarkets() {

		return null;
	}

	public boolean isCreditDeducted(String creditValue, String betValue) {
		return false;
	}

	public boolean isWinAddedToCredit(String creditValue, String betValue) {
		return false;
	}

	public boolean isPlayerWon() {

		return false;
	}

	public long getNumberOfQuickbets() {

		return 0;
	}

	public boolean isBetChangedOnRefresh() {

		return true;
	}

	public void openBetPanel() {
	}

	public String setTheNextLowBet() {

		return null;
	}

	public String selectQuickBet(int quickBet) {

		return null;
	}

	public boolean validateMiniPaytable(String bet, Desktop_HTML_Report language) {

		return false;
	}

	public boolean isAutoplayPauseOnFocusChange(Desktop_HTML_Report language) {

		return false;
	}

	public boolean verifyAutoplayConsoleOptions(Desktop_HTML_Report report) {

		return false;
	}

	public boolean verifyAutoplayPanelOptions(Desktop_HTML_Report report) {

		return false;
	}

	public boolean verifyALLBetValues(Desktop_HTML_Report report) {

		return false;
	}

	public boolean verifyAllQuickBets(Desktop_HTML_Report report) {

		return false;
	}

	public boolean CheckNavigateGameToBanking(Desktop_HTML_Report report) {
		return false;

	}

	public boolean waitForWinDisplay() {
		return false;
	}

	public void BackTogamefromnavigation(Desktop_HTML_Report report) {
	}

	public boolean verifysettingsOptions(Desktop_HTML_Report report) throws InterruptedException {
		return false;
	}

	public boolean verifypaytablenavigation(Desktop_HTML_Report report) {
		return false;
	}

	public boolean verifyhelpenavigation(Desktop_HTML_Report report) {
		return false;
	}

	public void paytablenavigationClose() {
	}

	public boolean verifyresponsiblegamingenavigation(Desktop_HTML_Report report) {
		return false;
	}

	public boolean verifyplaychecknavigation(Desktop_HTML_Report report) {
		boolean ret = false;
		return ret;
	}

	public boolean verifyloyaltynavigation(Desktop_HTML_Report report) {
		return false;
	}

	public boolean verifycashChecknavigation(Desktop_HTML_Report report) {
		return false;
	}

	public boolean verifylobbynavigation() {
		return false;
	}

	public void setNameSpace() {
	}

	public void verifyMenuOptionNavigations(Desktop_HTML_Report report) {
	}

	/*
	 * Function overloading check page navigation
	 * 
	 * @input: Report,page tital
	 * 
	 * @return: void
	 */

	public void checkPageNavigation(Desktop_HTML_Report report, String pagetital) {
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
								" Should navigate to" + pagetital + " link", "Navigate to " + pagetital + " link",
								"pass");
						log.debug("Page navigated succesfully");
						System.out.println("Page navigated succesfully");
						webdriver.close();// closing new window
						webdriver.switchTo().window(mainwindow);
						waitForSpinButton();

					} else {
						report.detailsAppend("verify the" + pagetital + " link ",
								" Should nevigate to " + pagetital + "link",
								"Does not Navigate to " + pagetital + " link", "fail");
						webdriver.switchTo().window(mainwindow);
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
					System.out.println("Page navigated succesfully");
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
		// TODO Auto-generated method stub
		return false;
	}

	public boolean verifyResponsibleGamingNavigation(Desktop_HTML_Report denmark) {
		return false;
	}

	public boolean verifyHelpNavigation(Desktop_HTML_Report denmark) {
		return false;
	}

	public boolean betSettingCurrencySymbol(String currencySymbol, Desktop_HTML_Report currency, String currencyName)
			throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean verifyCurrencyFormat(String currencyFormat) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean freeSpinSummaryWinCurrFormat(String currencyFormat) {
		// TODO Auto-generated method stub
		return true;
	}

	public void verifyPaytableCurrency(Desktop_HTML_Report currencyReport, String currencyName) {
		// TODO Auto-generated method stub

	}


	/**
	 * This method checks whether quickspin available or not
	 * 
	 * @return
	 */
	public boolean isQuickspinAvailable() {
		return false;
	}

	/**
	 * This method is used to click on the Autoplay win/loss limit dialog box
	 */
	public void clickOnPrimaryBtn() {

	}

	/**
	 * This method is used to check the status of spin button in base scene
	 * 
	 * @return
	 */
	public boolean verifySpinStatus() {
		return false;
	}

	/**
	 * To verify bonus game win currency format
	 * 
	 * @param currencyFormat
	 * @return
	 */
	public boolean bonusWinCurrFormat(String currencySymbol) {
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
	 * To verify piggybank bonus and safe bonus in bust the bank
	 * 
	 * @return
	 */
	public boolean piggyBankBonus(String currencyFormat) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * To verify Safe bonus bonus in bust the bank
	 * 
	 * @return
	 */
	public boolean verifySafeBonusCurrency(String currencyFormat) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * This method is used to check whether session reminder is present or not
	 * 
	 * @return
	 */
	public boolean verifySessionReminderPresent(Desktop_HTML_Report report) {
		return true;
	}

	

	/*
	 * Italy TopBar
	 */

	public String italy_GameNameOnTopBar(Desktop_HTML_Report italy) {
		String getGameName = null;
		try {
			boolean isGameNamePresent = webdriver.findElement(By.xpath(XpathMap.get("Italy_TopbarGamename")))
					.isDisplayed();
			// System.out.println(isGameNamePresent);
			if (isGameNamePresent) {
				log.debug("Gamename is visible");
				getGameName = webdriver.findElement(By.xpath(XpathMap.get("Italy_TopbarGamename"))).getText(); // Expected
																												// Name
				System.out.println("Gamename is visible as  " + getGameName);
				log.debug("Gamename is visible as  " + getGameName);

				String name = XpathMap.get(("Italy_GamenameonTopBar")); // Actual Name
				System.out.println("Actual Game name is  " + name);
				if (getGameName.equals(name)) // Expected VS Actual Name .
				{
					System.out.println("Game name is same");
					Thread.sleep(2000);
					italy.detailsAppend("Expected VS Actual Game Names are same ", "Game Names are Same",
							"Game Names are Same", "pass");
				} else {
					System.out.println("Game name is different");
					italy.detailsAppend("Expected VS Actual Game Names are same ", "Game Names are different",
							"Game Names are different", "fail");
				}

			} else {
				System.out.println("Game name is not visible on top bar");
			}
		} catch (Exception e) {
			log.error("Not able to verify Game name", e);
		}
		return getGameName;
	}

	

	/**
	 * This method checks whether stop available or not in Italy Market
	 * 
	 * @return
	 */
	public boolean verifyStopButtonAvailablity() 
	{
		return false;
	}

	/*
	 * Continue Button
	 */
	public void Continue() throws InterruptedException {
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("ClickonContinue"))));
		webdriver.findElement(By.id(XpathMap.get("ClickonContinue"))).click();
		log.debug("Clicked on Continue Button ");
		System.out.println("Clicked on Continue Button ");

	}
	/**
	 * method is used to check if it is displayed or not 
	 * @param locator
	 * @return
	 */
	public boolean isDisplayed(String locator)
	{
		return false;
		
	}
	/**
	 * method is used to check if it is displayed or not 
	 * @param locator
	 * @return
	 * @author rk61073
	 */
	public boolean isDisplayed(Desktop_HTML_Report report,String locator,String CurrencyName)
	{
		return false;
	}

	/**
	 * This method is used to check whether Topbar is present or not
	 */
	public boolean verifyTopBarVisible() 
	{
		return false;
	}
	

	/**
	 * This method is used for verify whether quick spin is present or not
	 * 
	 * @return true
	 */
	public boolean verifyQuickspinAvailablity() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean verifyStopButtonAvailablity1() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * To open Responsiblegaming from Topbar
	 */
	public boolean openResponsiblegamingFromTopbar(Desktop_HTML_Report report) {
		return false;
	}

	/**
	 * close from Top Bar
	 */
	public boolean close(Desktop_HTML_Report report) {
		return false;
	}

	/**
	 * This method is used to check whether session reminder is present or not
	 * 
	 * @return
	 */
	public boolean verifySessionReminderPresent() {
		return true;
	}

	/**
	 * This method is used To click on continue button in session reminder
	 */
	public void selectContinueSession() {
		// TODO Auto-generated method stub

	}

	/**
	 * To verify Player Protection is present on topbat in uk market
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public boolean Verify_PlayerProtection(Desktop_HTML_Report report) throws InterruptedException {
		webdriver.findElement(By.xpath(XpathMap.get("clickOnMenuOnTopbar"))).click();
		Thread.sleep(3000);
		// report.detailsAppend(" Verify Menu ", "Menu Clicked ","Menu Clicked",
		// "PASS");
		boolean playerprotection = false;
		try {
			// String plyrprotection
			// =webdriver.findElement(By.xpath(XpathMap.get("Italy_PlayerProtection"))).getText();
			String plyrprotection = webdriver.findElement(By.xpath(XpathMap.get("Denmark_PlayerProtection"))).getText();
			System.out.println("Player Protection is displayed as" + plyrprotection);
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
	public boolean Verify_Help(Desktop_HTML_Report report) throws InterruptedException {
		webdriver.findElement(By.xpath(XpathMap.get("clickOnMenuOnTopbar"))).click();
		Thread.sleep(3000);
		// report.detailsAppend(" Verify Menu ", "Menu Clicked ","Menu Clicked",
		// "PASS");
		boolean isHelpAvailable = false;
		try {
			String isHelp = webdriver.findElement(By.xpath(XpathMap.get("Italy_Help"))).getText();
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
	public boolean Verify_Responsiblegaming(Desktop_HTML_Report report) throws InterruptedException {
		webdriver.findElement(By.xpath(XpathMap.get("clickOnMenuOnTopbar"))).click();
		Thread.sleep(3000);
		report.detailsAppend(" Verify Menu  ", "Menu Clicked ", "Menu Clicked", "PASS");
		boolean isResponsiblegaming = false;
		try {
			String responsibleGaming = webdriver.findElement(By.xpath(XpathMap.get("Italy_ResponsibleGaming")))
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
 /**
 *Verifies  player protection icon and its navigation
 */
	public boolean playerProtectionIcon(Desktop_HTML_Report report) 
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean currency_Check(Desktop_HTML_Report report) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * To fill the start session form in spain market
	 * 
	 * @return
	 */
	public void fillStartSessionForm(String lossLimitForSpain, Desktop_HTML_Report reportSpain) {
		Wait = new WebDriverWait(webdriver, 500);
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("spain_TimeLimit"))));
			Select sel = new Select(webdriver.findElement(By.xpath(XpathMap.get("spain_TimeLimit"))));
			sel.selectByIndex(1);
			Thread.sleep(1000);
			sel = new Select(webdriver.findElement(By.xpath(XpathMap.get("spain_ReminderPeriod"))));
			sel.selectByIndex(1);
			Thread.sleep(1000);
			webdriver.findElement(By.xpath(XpathMap.get("spain_LossLimit"))).sendKeys(lossLimitForSpain);
			Thread.sleep(1000);
			reportSpain.detailsAppend("Fill all the valid limits in Session overlay",
					"Fill all the valid limits in Session overlay",
					"Fill all the valid limits in Session overlay display", "Pass");
			webdriver.findElement(By.xpath(XpathMap.get("spain_SetLimits"))).click();
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * To validate balance is updated correctly
	 */
	public boolean isBalanceUpdated(String payoutAfterStop, String wagersAfterStop) {
		return false;
	}

	/**
	 * To validate win is added to payouts correctly
	 */
	public boolean isWinAddedToPayout(String payoutb4Spin, double dblPayoutAfterStop) {
		return false;
	}

	/**
	 * To validate bet is added to wagers correctly
	 */
	public boolean isBetAddedToWagers(double dblPayoutAfterStop, double dblWagersAfterStop, double dblBetValue) {
		return false;
	}

	
	/**
	 * To click on Menu on topbar
	 * 
	 * @return
	 */
	public boolean clickOnMenuOnTopbar() {
		boolean ret = false;
		try {
			webdriver.findElement(By.xpath(XpathMap.get("clickOnMenuOnTopbar"))).click();
			log.debug("Clicked on menu button on topbar to open");
			ret = true;
		} catch (Exception e) {
			log.error("Error in opening menu", e);

		}
		return ret;
	}

	/**
	 * To verify Menu navigations from topbar in uk
	 * 
	 * @return
	 */
	public void verifyMenuOptionNavigationsForUK(Desktop_HTML_Report report) {
	}

	/**
	 * To verify Responsible Gaming is present on topbar and navigate to it
	 * 
	 * @return
	 */
	public void verifyResposibleGamingOnTopbar(Desktop_HTML_Report report) {
	}

	/**
	 * To verify Help is present on topbar and navigate to it
	 * 
	 * @return
	 */
	public void verifyHelpOnTopbar(Desktop_HTML_Report report) {
	}

	/**
	 * To verify Transaction History is present on topbar and navigate to it
	 * 
	 * @return
	 */
	public void verifyTransactionHistoryOnTopbar(Desktop_HTML_Report report) {
	}

	/**
	 * To verify Game History is present on topbar and navigate to it
	 * 
	 * @return
	 */
	public void verifyGameHistoryOnTopbar(Desktop_HTML_Report report) {
	}

	/**
	 * To verify Player Protection is present on topbar and navigate to it
	 * 
	 * @return
	 */
	public void verifyPlayerProtectionOnTopbar(Desktop_HTML_Report report) {
	}

	/**
	 * To verify Help text link on top bar
	 */
	public void verifyHelpTextlink(Desktop_HTML_Report report) 
	{
		// TODO Auto-generated method stub
	}

	/**
	 * To verfiy net postion
	 * 
	 * @return
	 */
	public boolean verifyNetPosition(Desktop_HTML_Report report) {
		return false;
	}

	/**
	 * To verify Menu navigations from topbar in spain
	 * 
	 * @return
	 */
	public void verifyMenuOptionNavigationsForSpain(Desktop_HTML_Report report) {
	}

	/**
	 * To close cooling off period
	 */
	public void closeCoolingOffPeriod(Desktop_HTML_Report report) {
	}

	/**
	 * To set the spain cooling Off period
	 * 
	 * @param report
	 */
	public void spainCoolingOffPeriod(Desktop_HTML_Report report) {
	}/*
		 * To verify Bonus Reminder
		 */

	public void verifyBonusReminder(Desktop_HTML_Report report) {
		// TODO Auto-generated method stub
	}

	/**
	 * To validate balance, payouts and wagers updated correctly and verify slot
	 * loss limit overlay
	 * 
	 * @pb61055
	 */
	public boolean waitUntilSessionLoss(String lossLimit, Desktop_HTML_Report reportSpain) {
		return false;
	}

	

	/**
	 *  method is for denmark PlayerProtection text comparission , click and its navigation from menu 
	 */
	public boolean denmarkPlayerProtectionFromMenu(Desktop_HTML_Report report) throws InterruptedException
	{
		return false;		
	}

	/*
	 * Verify Italy Help
	 */
	public boolean Verify_ItalyHelp(Desktop_HTML_Report report) throws InterruptedException {
		webdriver.findElement(By.xpath(XpathMap.get("clickOnMenuOnTopbar"))).click();
		Thread.sleep(3000);
		boolean isHelpAvailable = false;
		try {
			String isHelp = webdriver.findElement(By.xpath(XpathMap.get("Italy_Help"))).getText();
			System.out.println("" + isHelp);

			if (isHelp.equals("Help") || isHelp.equals("Guida")) {
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
	 *  method is for italy PlayerProtection text comparission , click and its navigation from menu 
	 */
	public boolean italyPlayerProtectionFromTopBarMenu(Desktop_HTML_Report report) throws InterruptedException
	{
		return false;
		
	}

	/**
	 *  method is for italy ResponsibleGaming text comparission , click and its navigation from menu 
	 */
	public boolean italyResponsibleGamingFromMenu(Desktop_HTML_Report report) throws InterruptedException
	{
		return false;
	}

	/**
	 * To verify help navigation in menu for spain
	 * 
	 * @pb61055
	 */
	public void verifyHelpOnTopbarSpain(Desktop_HTML_Report report) {
	}

	/**
	 * To verify currency format for spain
	 * 
	 * @param currencyFormat
	 * @return
	 */
	public boolean verifyCurrencyFormatForSpain(String currencyFormat) {
		return false;
	}

	/**
	 * This method is used to get currency symbol for spain .
	 * 
	 * @throws InterruptedException
	 */
	public String getCurrencySymbolForSpain() {
		return Flag;
	}

	/**
	 * To verify Session reminder is visible or not
	 */

	public boolean isSessionReminderPresent(Desktop_HTML_Report report) 
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 *  Game Name from Top Bar
	 * @param report
	 * @return
	 */
	public String gameNameFromTopBar(Desktop_HTML_Report report) 
	{
		return null;
	}


	/**
	 * method is for to check if it is Displayed or not  and to get the Text 	
	 */
		public String isDisplayedAndGetText(String locator)
		{
			return null;
		
		}
/**
 * Verify Clock from Top Bar
 *
 */
	public boolean clockFromTopBar(Desktop_HTML_Report report)
{
	return false;
	}
	/**
	 * To verify Help text link on top bar
	 */
	public boolean helpTextLink(Desktop_HTML_Report report)
	{
		return false;
	}
	/**
	 * method is for click , navigate and back to game screen using Xpath (For Android & IOS click action is different)
	 */
	public boolean clickAndNavigate(Desktop_HTML_Report report , String locator) 
	{
		return false;
	}
	
	/**
	 * Click and get the Text 
	 */
	
	/**
	 * Denamrk Currency Check
	 */

	public boolean denmarkCurrencyCheck(Desktop_HTML_Report report) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * This method is used to find the reel spin duration for a single spin
	 */
	public long reelSpinDuratioN(Desktop_HTML_Report report)
	{
		// TODO Auto-generated method stub
		return 0;
	}
	/**
	 * This method is used to scroll the , scroll bar and select amount for italy market 
	 */
	public void italyScrollBarAmount(Desktop_HTML_Report report, String amount) 
	{
		// TODO Auto-generated method stub

	}

	public String italygetCurrentCredits()
	{
		String balance = null;
		String consoleBalance = null;

		if (!GameName.contains("Scratch")) 
		{
			if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(XpathMap.get("TypeOfGame")))
			{
				balance = "return " + XpathMap.get("Balancetext");
			} else
				balance = "return " + XpathMap.get("Balancetext");
		} 
		else 
		{
			balance = "return " + XpathMap.get("InfoBarBalanceTxt");

		}

		consoleBalance = getConsoleText(balance);
		System.out.println("Actual Consloe Balance is " + consoleBalance);
		consoleBalance = getConsoleText(balance).replaceAll("[a-zA-Z]", "").replace(": � ", " ");
		System.out.println("Now, the Console Balanace is " + consoleBalance);
		return consoleBalance;

	}
/**
 * italy credit comparission 
 */
	public boolean italyCreditAmountComparission() 
	{
		boolean isVaildCreditAmount = false;
		try 
		{
			String creditamount = italygetCurrentCredits(); // get Console credit from italygetCurrentCredits method

			creditamount = creditamount.replace(" ", "");// replace space with null(from italygetCurrentCredits method)

			selectedamout = selectedamout.replace(".", ","); // replace . with ,(from italyscrollbarclickamount method)

			// we get selected amount from click amount method
			if (creditamount.equals(selectedamout)) // Expected VS Actual Name .
			{
				isVaildCreditAmount = true;
				System.out.println("Take away screen game credit is selected as " + selectedamout);
				System.out.println("Credit Amounts are SAME ");
			} else {
				System.out.println("Credit Amounts are DIFFERENT  ");
			}
		} catch (Exception e) {
			log.error("Not able to verify Game name", e);
		}
		return isVaildCreditAmount;

	}

	public boolean italyCurrencyCheck(Desktop_HTML_Report report) {
		// TODO Auto-generated method stub
		return false;
	}

	
/**
 * method is for italy help text comparission , click and its navigation from menu 
 * @param report
 * @return
 * @throws InterruptedException
 */
	public boolean italyHelpFromTopBarMenu(Desktop_HTML_Report report) throws InterruptedException 
	{
		return false;
	}


	
	/**
	 * This method is used to check whether Topbar is present or not
	 *
	 * @return
	 */
	public boolean isTopBarVisible() {

		boolean isTopBarPresentinGame = false;
		try {
			isTopBarPresentinGame = webdriver.findElement(By.xpath(XpathMap.get("isTopbarPresent"))).isDisplayed();
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
	 * To verify gamename on topbar
	 */
	public void gameNameOnTopBar(Desktop_HTML_Report report) {
		String getGameName = null;
		try {
			boolean isGameNamePresent = webdriver.findElement(By.xpath(XpathMap.get("isGamename"))).isDisplayed();
			System.out.println(isGameNamePresent);
			if (isGameNamePresent) {
				log.debug("Gamename is visible");
				System.out.println("Gamename is visible");
				getGameName = webdriver.findElement(By.xpath(XpathMap.get("isGamename"))).getText();
				System.out.println(getGameName);
				log.debug(getGameName);
				String name = XpathMap.get(("TopbarGameName"));
				System.out.println(name);
				Thread.sleep(2000);
				if (getGameName.equals(name)) {
					System.out.println("Game name is same");
					report.detailsAppend("Verify Gamename must be is displayed on Topbar",
							"Gamename should be is displayed on Topbar",
							"Gamename is displayed on Topbar " + getGameName, "pass");
				} else {
					System.out.println("Game name is different");
					report.detailsAppend("Gamename must be is displayed on Topbar",
							"Gamename should be is displayed on Topbar",
							"Gamename is displayed on Topbar and incorrect " + getGameName, "fail");
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
	public void betOnTopBar(Desktop_HTML_Report report) {
		String getBetValue = null;
		try {
			boolean isBet = webdriver.findElement(By.xpath(XpathMap.get("isBetOnTopbarVisible"))).isDisplayed();
			if (isBet) {
				log.debug("Bet on Top is visible");
				System.out.println("Bet on Top bar is visible");
				getBetValue = webdriver.findElement(By.xpath(XpathMap.get("isBetOnTopbarVisible"))).getText();
				System.out.println(getBetValue);
				String betValue = getConsoleText("return " + XpathMap.get("BetTextValue"));
				System.out.println(betValue);
				Thread.sleep(2000);
				if (getBetValue.equalsIgnoreCase(betValue)) {
					System.out.println("Bet values are same");
					report.detailsAppend("Verify Bet value must be is displayed on Topbar",
							"Bet value should be is displayed on Topbar",
							"Bet value is displayed on Topbar and correct " + getBetValue, "pass");
				} else {
					System.out.println("Bet values are different");
					report.detailsAppend("Verify Bet value must be is displayed on Topbar",
							"Bet value should be is displayed on Topbar",
							"Bet value is displayed on Topbar and incorrect " + getBetValue, "pass");

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
	public void clockOnTopBar(Desktop_HTML_Report report) {
		String clock = null;
		try {
			boolean isClockPresent = webdriver.findElement(By.xpath(XpathMap.get("isClockVisible"))).isDisplayed();
			Thread.sleep(2000);
			if (isClockPresent) {
				log.debug("clock is visible");
				System.out.println("clock is visible");
				clock = webdriver.findElement(By.xpath(XpathMap.get("isClockVisible"))).getText();
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
	 * This method is used to find the reel spin duration for a single spin
	 */
	public long reelSpinDuration() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * To verify session duration on top bar
	 * 
	 * @param report
	 */
	public void verifySessionDurationOnTopBar(Desktop_HTML_Report report) {
		String sessionDuration = null;
		try {
			boolean isSessionDurationPresent = webdriver.findElement(By.xpath(XpathMap.get("isSessionDuration")))
					.isDisplayed();
			Thread.sleep(2000);
			if (isSessionDurationPresent) {
				log.debug("Session Duration is visible");
				System.out.println("Session Duration is visible");
				sessionDuration = webdriver.findElement(By.xpath(XpathMap.get("isSessionDuration"))).getText();
				System.out.println(sessionDuration);
				report.detailsAppend("Verify Session Duration must be is displayed on Topbar",
						"Session Duration should be is displayed on Topbar", "Session Duration is displayed on Topbar",
						"pass");
			}
		}

		catch (Exception e) {
			log.error("Not able to verify Session Duration", e);
			System.out.println("Session Duration is not visible on top bar");
			report.detailsAppend("Session Duration must be is displayed on Topbar",
					"Session Duration should be is displayed on Topbar", "Session Duration is not displayed on Topbar",
					"fail");
		}
	}

	/**
	 * To verify session duration on top bar
	 * 
	 * @param report
	 */
	public void verifyBalanceOnTopBar(Desktop_HTML_Report report) {
		String getBalanceValue = null;
		try {
			boolean isBalance = webdriver.findElement(By.xpath(XpathMap.get("balanceOnTopBar"))).isDisplayed();
			if (isBalance) {
				log.debug("Balance on Top is visible");
				System.out.println("Balance on Top bar is visible");
				getBalanceValue = webdriver.findElement(By.xpath(XpathMap.get("balanceOnTopBar"))).getText();
				String balanceValue = getBalanceValue.replaceAll("Balance: ", "");
				balanceValue = balanceValue.replaceAll("Saldo: ", "");
				System.out.println(balanceValue);
				String consoleCredit = getCurrentCredits().replaceAll("Credits: ", "");
				consoleCredit = consoleCredit.replace("credits: ", "");
				consoleCredit = consoleCredit.replace("CREDITS: ", "");
				consoleCredit = consoleCredit.replace("KREDITER: ", "");
				System.out.println(consoleCredit);
				Thread.sleep(2000);
				if (balanceValue.equalsIgnoreCase(consoleCredit)) {
					System.out.println("Balance is same as credits");
					report.detailsAppend("Verify Balance value must be is displayed on Topbar",
							"Balance value should be is displayed on Topbar",
							"Balance value is displayed on Topbar and correct " + getBalanceValue, "pass");
				} else {
					System.out.println("Balance is not same as credits");
					report.detailsAppend("Verify Balance value must be is displayed on Topbar",
							"Balance value should be is displayed on Topbar",
							"Balance value is displayed on Topbar and incorrect " + getBalanceValue, "pass");

				}
			}
		} catch (Exception e) {
			log.error("Not able to verify Balance", e);
			System.out.println("Balance value is not visible on top bar");
			report.detailsAppend("Balance value must be is displayed on Topbar",
					"Balance value should be is displayed on Topbar", "Balance value is not displayed on Topbar",
					"fail");

		}
	}

	/**
	 * To verify menu options navigations in sweden
	 * 
	 * @param report
	 */
	public void verifyMenuOptionNavigationsForSweden(Desktop_HTML_Report report) {
		// TODO Auto-generated method stub

	}

	/**
	 * To verify win or loss in session reminder
	 * 
	 * @param report
	 */

	public void verifySessionReminderWinAndLoss(Desktop_HTML_Report report, Double valueAfterSpin1,
			Double valueAfterSpin2) {
		// TODO Auto-generated method stub

	}

	public Double verifyWinOrLossForStopButton(Desktop_HTML_Report report) {
		return null;
	}

	public Double verifyWinOrLossForReelSpinDuration(Desktop_HTML_Report report) {
		return null;
	}

	/*
	 * /Sweden Spelpaus Symbols from Top Bar
	 */
	public void swedenRegMarketLogosFromTopBar(Desktop_HTML_Report report) {
	}

	/**
	 * Sweden Self Test Symbol from Top Bar
	 */
	public void swedenSelfTestLogoOnTopBar(Desktop_HTML_Report report) {

	}

	/**
	 * Sweden Responsible gaming Symbols from Top Bar
	 */
	public void swedenResponsibleGamingLogoOnTopBar(Desktop_HTML_Report report) {

	}

	/**
	 * Sweden Self exclusion Symbol from Top Bar
	 */

	public void swedenSelfExlusionLogoOnTopBar(Desktop_HTML_Report report) {

	}

	/**
	 * This method is used to get currency symbol for sweden .
	 * 
	 * @throws InterruptedException
	 */
	public String getCurrencySymbolForSweden() {
		return null;

	}

	/**
	 * To verify currency format for spain
	 * 
	 * @param currencyFormat
	 * @return
	 */
	public boolean verifyCurrencyFormatForSweden(String currencyFormat) {
		return false;
	}

	/**
	 * To verify Session reminder is visible or not
	 */
	public boolean sessionReminderPresent() {
		boolean isSessionReminderVisible = false;
		WebDriverWait Wait = new WebDriverWait(webdriver, 500);
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("sessionReminderVisible"))));
				List<WebElement> elementList = webdriver.findElements(By.xpath(XpathMap.get("sessionReminderVisible"))); 
			if (elementList.size() > 0) {
				log.debug("Session reminder found");
				System.out.println("Session reminder found");
				isSessionReminderVisible = true;
			}
		} catch (Exception e) {
			log.error("Not able to verify session reminder status", e);
			System.out.println("Session reminder not found");
			isSessionReminderVisible = false;
		}
		return isSessionReminderVisible; // if it return false then there is no session reminder present on the screen
	}

	/**
	 * To verify help navigation in menu for sweden
	 * 
	 * @pb61055
	 */
	public void verifyHelpOnTopbarSweden(Desktop_HTML_Report report) {

	}
	

	/**
	 *Verifies Germany  help text comparison , click and its navigation from menu
	 */
	public boolean germanyHelpFromTopBarMenu(Desktop_HTML_Report report) throws InterruptedException
	{
		return false;

	}
	/**
	 *Verifies Germany  ResponsibleGaming text comparison , click and its navigation from menu
	 */
	public boolean germanyResponsibleGamingFromTopBarMenu(Desktop_HTML_Report report) throws InterruptedException
	{
		return false;

	}

	/**
	 *Verifies Germany GameHistory text comparison , click and its navigation from menu
	 */
	public boolean germanyGameHistoryFromTopBarMenu(Desktop_HTML_Report report) throws InterruptedException
	{
		return false;
	}

	
	

	/**
	 *  verify & Compare Germany Currency
	 */
	public boolean germanyCurrencyCheck(Desktop_HTML_Report report) 
	{
		return false;
	}

	/**
	 *  Verify is coin size changed for Germany
	 */
	public boolean isCoinSizeChanged(Desktop_HTML_Report report) 
	{
			return false;
	}

	/**
	 *  verify if Autoplay is available or not
	 * @return
	 */
	public boolean verifyAutoplayAvailabilty()
	{
		return false;

	}
	
	/**
	 *Verifies malta  help text comparison , click and its navigation from menu
	 */
	public boolean maltaHelpFromTopBarMenu(Desktop_HTML_Report report) throws InterruptedException
	{
		return false;

	}
	/**
	 *Verifies malta  ResponsibleGaming text comparison , click and its navigation from menu
	 */
	public boolean maltaResponsibleGamingFromTopBarMenu(Desktop_HTML_Report report) throws InterruptedException
	{
		return false;

	}

	/**
	 *Verifies malta GameHistory text comparison , click and its navigation from menu
	 */
	public boolean maltaGameHistoryFromTopBarMenu(Desktop_HTML_Report report) throws InterruptedException
	{
		return false;
	}
	
	/**
	 *Verifies Portugal  help text comparison , click and its navigation from menu
	 */
	public boolean portugalHelpFromTopBarMenu(Desktop_HTML_Report report) throws InterruptedException
	{
		return false;

	}
	/**
	 *Verifies Portugal GameHistory text comparison , click and its navigation from menu
	 */
	public boolean portugalGameHistoryFromTopBarMenu(Desktop_HTML_Report report) throws InterruptedException
	{
		return false;
	}
	
/**
 * This method is for to update Germany bet settings in Axiom and in Bluemesa
 * @return 
 */

	public boolean germanyBetSettings(String userName, String mid, String cid)
	{
		return false;
		
	}

	/**
	 *  verify & Compare malta Currency
	 */
	public boolean maltaCurrencyCheck(Desktop_HTML_Report report) 
	{
		return false;
	}
	/**
	 *  verify & Compare portugal Currency
	 */
	public boolean portugalCurrencyCheck(Desktop_HTML_Report report) 
	{
		return false;
	}
	
	/**
	 *Verifies Romania  help text comparison , click and its navigation from menu
	 */
	public boolean romaniaHelpFromTopBarMenu(Desktop_HTML_Report report) throws InterruptedException
	{
		return false;

	}
	/**
	 *Verifies Romania GameHistory text comparison , click and its navigation from menu
	 */
	public boolean romaniaGameHistoryFromTopBarMenu(Desktop_HTML_Report report) throws InterruptedException
	{
		return false;
	}
	/**
	 *  verify & Compare romania Currency
	 */
	public boolean romaniaCurrencyCheck(Desktop_HTML_Report report) 
	{
		return false;
	}
	
	/**
	 * Verifies the current credits
	 * 
	 */
	public String getCurrentCredit(Desktop_HTML_Report report) 
	{
		return null;
		
	}
	/**
	 * Verifies the current Bet
	 * 
	 */
	public String getCurrentBetAmt(Desktop_HTML_Report report) 
	{
		return null;
		
	}
	
	/**
	 * Verifies the current Win
	 * 
	 */
	public String getCurrentWinAmt(Desktop_HTML_Report report,String CurrencyName ) 
	{
		return null;
		
	}
	/**
	 * Verifies the Bet
	 * 
	 */
	public boolean verifyBetSettings(Desktop_HTML_Report report) 
	{
		return false;
		
	}
	/**
	 * Verifies the Big Win
	 * 
	 */
	public String verifyBigWin(Desktop_HTML_Report report,String CurrencyName) 
	{
		return null;
		
	}
	/**
	 * Verifies the current Win
	 * 
	 */
	public boolean paytableOpen(Desktop_HTML_Report report,String CurrencyName) 
	{
		return false;
		
	}
	
	/**
	 * verifies mini bet
	 * @return
	 */
	public String moveCoinSizeSliderToMinBet(Desktop_HTML_Report report , String CurrencyName ) 
	{
		return null;
	}
	

	
	public void Scrolling(String ele)
	{
	JavascriptExecutor js = (JavascriptExecutor) webdriver;
	WebElement ele1 = webdriver.findElement(By.xpath(ele));
	js.executeScript("arguments[0].scrollIntoView(true);",ele1);
	
	}
	
	/**
	 * method is used to validate the Paytable Values 
	 * @return
	 */
	public boolean validatePayoutsFromPaytable(Desktop_HTML_Report report,String CurrencyName,String regExpr) //String[] array
	{
		return false;
		
	}
	
	/**
	 * method is to get the text using by ID .
	 */
	public String func_click(String locator) 
	{
		return null;
	}
	public String func_GetText_BYID(String locator) 
	{
		return null;
		
	}
	/**
	 * Verifies the Currency Format - using String method 
	 */


	
		
	/**
	 * Verifies the Currency Format - using String method 
	 */


	public boolean regExp(Desktop_HTML_Report report, String regExp, String method)
	{
		return false;
	}

	
	/**
	 * method verifies the bonus summary screen and get Text	
	 */
	public String bonusSummaryScreen(Desktop_HTML_Report report, String CurrencyName)
	{
		return null;
	}
	
	/**
	 * Verify Bonus Feature by clicking and get text 
	 * @param report
	 * @return
	 */

	public String[] bonusFeatureClickandGetText(Desktop_HTML_Report report, String CurrencyName) 
	{
	
		return null;
	}
	/**
	 * Verifies the current tOtal wins in FS
	 * 
	 */

	public String getCurrentTotalWinINFS(Desktop_HTML_Report report) 
	{
		// TODO Auto-generated method stub
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
	 * Get the text by using attribute
	 * @param report
	 * @param locator
	 * @return
	 */
	public String func_GetTextbyAttribute(Desktop_HTML_Report report ,String locator, String CurrencyName) 
	{
		return null;
	}
	/**
	 * method verifies the bonus summary screen and get Text	
	 */
	public String freeSpinsSummaryScreen(Desktop_HTML_Report report, String CurrencyName)
	{
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * method verifies the bonus summary screen and get Text	
	 */
	public String paytableScrollAndValidatePayouts(Desktop_HTML_Report report, String language)
	{
		// TODO Auto-generated method stub
		return null;
	}
	public boolean verifyWinAmtCurrencyFormat(String currencyFormat, Desktop_HTML_Report currencyReport) 
	{
		// TODO Auto-generated method stub
		return false;
	}

	public void jackpotSummaryWinCurrFormat(String currencyFormat, Desktop_HTML_Report currencyReport,
			String currencyName) {

	}

	public void payoutverificationforBetLVC(Desktop_HTML_Report currencyReport, String regExpression,String currencyName) {
		// TODO Auto-generated method stub
		
	}

	public void freeGameSummaryWinCurrFormat(String currencyFormat, Desktop_HTML_Report currencyReport,
			String currencyName) {
		// TODO Auto-generated method stub
	}

	public boolean assignFreeGames(String userName,String offerExpirationUtcDate,int mid, int cid,int languageCnt,int defaultNoOfFreeGames) {
		// TODO Auto-generated method stub
		return false;
	}

	public String getWinAmtFromFreegames() 
	{
		return Flag;
		
	}
	
	 public boolean isElementVisible(String element)
	  {
		return visible;
	  
	  }
	 public void ClickByCoordinates(String cx, String cy)
	  {
		// TODO Auto-generated method stub
	  }
/**
 * method is used to scroll the paytable
 * @param lvcReport
 * @return
 */
	public boolean paytableScroll(Desktop_HTML_Report report, String CurrencyName)
	{
		// TODO Auto-generated method stub
		return false;
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

	public boolean verifyBigWinCurrencyFormatForLVC(String regExpression, Desktop_HTML_Report currencyReport,
			String currencyName) {
		// TODO Auto-generated method stub
		return false;
	}

	public void jackpotSummaryWinCurrFormatForLVC(String regExpression, Desktop_HTML_Report currencyReport,
			String currencyName) {
		// TODO Auto-generated method stub
		
	}

	public void freeSpinSummaryWinCurrFormatForLVC(String regExpression, Desktop_HTML_Report currencyReport,
			String currencyName) {
		// TODO Auto-generated method stub
		
	}

	public void freeGameSummaryWinCurrFormatForLVC(String regExpression, Desktop_HTML_Report currencyReport,
			String currencyName) {
		// TODO Auto-generated method stub
		
	}

	public boolean verifyWinAmtCurrencyFormatForLVC(String regExpression) {
		// TODO Auto-generated method stub
		return false;
	}

	//To get the Bet pannel values 
	public String[] verifyBetPannelValues(Desktop_HTML_Report report,String CurrencyName) 
	{
		// TODO Auto-generated method stub
		return null;
	}


	public void freeGameInfoCurrencyFormat(String regExpression, Desktop_HTML_Report currencyReport,
			String currencyName) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * set min bet using scroll bar 
	 */
	public String setMinBetUsingScrollBar(Desktop_HTML_Report report , String CurrencyName ) 
	{
		return null;
	}
	/*
	 * set Max bet using scroll bar 
	 */
	public String setMaxBetUsingScrollBar(Desktop_HTML_Report report , String CurrencyName ) 
	{
		return null;
	}
	
	/*
	 * verifies the RegularExpression
	 */
	public boolean verifyRegularExpression(String curencyAmount, String regExp)
	{
		return false;
		
	}
	/*
	 * verifies the BigWin Text
	 */
	public String verifyWinText(Desktop_HTML_Report report,String CurrencyName,String locator)
	{
		return null;
	
	}
	
	/**
	 * Verifies Paytable open
	 * 
	 */
	public boolean paytableVarification(Desktop_HTML_Report report,String CurrencyName) 
	{
		return false;
	
	}
	/**
	 * Click menu Buttons
	 * @param currencyName 
	 * @return
	 * @throws InterruptedException
	 */
	public boolean menuButtonsforFiveReelGames(Desktop_HTML_Report report, String currencyName) 
	{
		return false;
		
	}
	/**
	 * Click menu Buttons
	 * @param currencyName 
	 * @return
	 * @throws InterruptedException
	 */
	public boolean menuButtonsforThreeReelGame(Desktop_HTML_Report report, String currencyName) 
	{
		return false;
		
	}
	/**
	 * method is to 
	 * @param report
	 * @param currencyName
	 * @return
	 */
	public boolean topBarMenuButtonIcons(Desktop_HTML_Report report, String currencyName) 
	{
		return false;
		
	}
	/**
	 * Verify Clock from Top Bar
	 *
	 */
		public boolean clockFromTopBar(Desktop_HTML_Report report,String currencyName)
	{
		return false;
		}
		/**
		 * Verifies the Big Win on refresh Base Game
		 * 
		 */
		public String verifyBigWinOnRefresh(Desktop_HTML_Report report,String CurrencyName) 
		{
			return null;
			
		}
		/**
		 * Verifies the Paytable text validation 
		 * 
		 */
		public String textValidationForPaytableBranding(Desktop_HTML_Report report,String CurrencyName) 
		{
			return null;
			
		}
		/**
		 * Verifies the Big Win on refresh
		 * 
		 */
		public String verifyBigWinRefreshOnFreeSpins(Desktop_HTML_Report report,String CurrencyName) 
		{
			return null;
			
		}
		/**
		 * method is used to validate the Paytable Values 
		 * @return
		 */
		public String[] paytablePayoutsOfSeven(Desktop_HTML_Report report,String CurrencyName) //String[] array
		{
			return null;
		}
		
		/**
		 * method is used to validate the Paytable Values 
		 * @return
		 */
		public String[] paytablePayoutsOfFour(Desktop_HTML_Report report,String CurrencyName) //String[] array
		{
			return null;
		}
		/**
		 * method verifies the entry screen of Free Games
		 * @param report
		 * @param currencyName
		 * @return
		 */
			public boolean freeGamesEntryScreen(Desktop_HTML_Report report,String currencyName)
			{
				return false;
				
			}
			/**
			 * method is for free game Information Icon
			 */
			public String freeGameEntryInfo(Desktop_HTML_Report report,String currencyName,String locator1 ,String Locator2) 
			{
				return null;
			}
			
			/**
			 * method is to Back to BaseGame in Free Games
			 */
			public String freeGameBackToBaseGame(Desktop_HTML_Report report,String currencyName) 
			{
				return null;
			}
		


	public void validateMiniPaytableForLVC(Desktop_HTML_Report currencyReport,
			String regExpression,String currencyName) {
		// TODO Auto-generated method stub
		
	}

	public boolean currencyFormatValidatorForLVC(String curencyAmount, String regExpression) {
		return false;

	}

	public void isFreeGamesVisible(String url) 
	{
		// TODO Auto-generated method stub
		
	}

	/*
	 * Method for payout verification for all bets
	 */
	public void PayoutvarificationforBetLVC(Desktop_HTML_Report lvcReport, String regExpr, String currencyName)
	{
		// TODO Auto-generated method stub
		
	}
	/**
	 * Verifies Paytable payout varification for 3 reel game
	 * 
	 */
	public String[]  singlePaytablePayouts(Desktop_HTML_Report report,String CurrencyName) 
	{
		return null;
		
	}
	/**
	 * method is for to scroll five times
	 * @param report
	 * @param CurrencyName
	 * @return
	 */
		public boolean paytableScrollOfFive(Desktop_HTML_Report report, String CurrencyName) 
		{
			return false;
		
		}
		/**
		 * method is for to scroll seven times
		 * @param report
		 * @param CurrencyName
		 * @return
		 */
		public boolean paytableScrollOfSeven(Desktop_HTML_Report report, String CurrencyName) 
		{
			return false;
		}

		public void RefreshGame(String Element) {
			
		}
		
		public void navigate_back(Desktop_HTML_Report report, String languageCurrency) throws InterruptedException {
			
		}
		
		public boolean elementVisible_Xpath(String Xpath) {
			return visible;
		}
		/**
		 * Click menu Buttons
		 * @param currencyName 
		 * @return
		 * @throws InterruptedException
		 */
		public boolean menuButtons(Desktop_HTML_Report report, String currencyName) 
		{
			return false;
			
		}
		
		public void loadGame1(String url)
		{
			
		}

		/**
		 * Verifies the Big Win on refresh Base Game
		 * 
		 */
		public String verifyBigWinOnRefreshInFreeGames(Desktop_HTML_Report report,String CurrencyName) 
		{
			return null;
			
		}
		public void isBonusMultiplierVisible() 
		{
			// TODO Auto-generated method stub
			
		}
	/*
		 * loading the game with direct url
		 */
		public boolean loadGameForLVC(String url)
		{
			boolean isGameLaunch = false;

			Wait = new WebDriverWait(webdriver, 120);

			try {
				webdriver.navigate().to(url);
				if ((Constant.YES.equalsIgnoreCase(XpathMap.get("continueBtnOnGameLoad"))))
				{
					Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("OneDesign_NewFeature_ClickToContinue"))));
					isGameLaunch = true;

				} else {
					log.debug("Waiting for clock to be visible");
					Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("clock"))));
					log.debug(" clock is visible");
					isGameLaunch = true;
				}
				log.debug("game loaded  ");
			} catch (WebDriverException e) {
				log.debug("Exception occur in loadgame()");
				log.error(e.getMessage());
			}
			return isGameLaunch;
		}
		
		public boolean details_append_folderOnlyScreeshot( WebDriver webdriver,String screenShotName) 
		{
			
			return isScreenshotTaken;
		}
		public void verifyquickbet(Desktop_HTML_Report report, String CurrencyName ,String regExpr) {
		
		}
		public String verifyAmgWin(Desktop_HTML_Report report, String CurrencyName) {
			return amgWinAmt;
			
		}
		
		public boolean verifyGridPayouts(Desktop_HTML_Report report, String regExp,String CurrencyName) 
		{
			return false;
					
		}
		
		public boolean waitForElement(String hook)
		{
			return result;
		}
		public void ClickByCoordinatesWithAdjust(String cx, String cy, int x, int y) {
			
		}
		public String freeGameEntryInfo(Desktop_HTML_Report report,String currencyName,String infoBtn,String infoBtnx,String infoBtny, String infoTxt) 
		{
			return null;
		}
		public String freeGameEntryInfo(Desktop_HTML_Report report,String currencyName,String fgInfoBTNx, String fgInfoBTNy, String fgInfotxt )  
		{
			return null;
		}
		public String GetConsoleText(String text)
		{
			return null;
		}
		
		public String freeGameOnRefresh(Desktop_HTML_Report report,String currencyName,String isResumeButtonVisible, String infoBtn, String infoBtnx,String infoBtny, String infoTxt, ImageLibrary imageLibrary) 
		{
			return null;
		}
		public String[] payOuts(Desktop_HTML_Report report, String CurrencyName){
			return null;
		}
		
		public boolean validatePayouts(Desktop_HTML_Report report, String CurrencyName, String regExpr) 																												
		{
			return false;
		}
		
		public void SpinUntilFGSummary(String FGSummaryBackToGameBtn,ImageLibrary imageLibrary)
		{
			
		}
		
		public String getCurrentFSWinAmt(Desktop_HTML_Report report,String FSwinamtVisible,String FSwinAmt,String CurrencyName ) 
		{
			return null;
		}

		/**
		 * Verifies the FreeSpine Big Win
		 * 
		 */
		public String verifyFreeSpinBigWin(Desktop_HTML_Report report, String CurrencyName) {
			return null;
		
		}
		
		/**
		 * Verifies the current Win amt
		 * 
		 */
		public String verifyFSCurrentWinAmt(Desktop_HTML_Report report, String CurrencyName) 
		{
			return null;
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
				System.out.println("webViewElementCoY " + webViewElementCoY); 
				IOSDriver<WebElement> iosDriver =(IOSDriver<WebElement>) webdriver;
				System.out.println(iosDriver);
				// double connectedDeviceWidth =
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

		public void tapOnCoordinates(PerformsTouchActions string, final double x, final double y) {
			// new TouchAction(webdriver).tap((int)x, (int)y).perform();
			// int X=(int) Math.round(x); // it will round off the values
			// int Y=(int) Math.round(y);
			System.out.println("X cor - " + x + "," + " Y cor - " + y);
			System.out.println(x);
			System.out.println(y);
			TouchAction action = new TouchAction(string);

			action.press(PointOption.point((int) x, (int) y)).release().perform();

			// action.tap(PointOption.point(X, Y)).perform();

		}

		

		
		public void clickWithWebElementAndroid (AppiumDriver<WebElement> webdriver,WebElement webViewElement,int Xcoordinate_AddValue) {
			String s=null;
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
					// Calculate the navigation Bar height and urlHeigt
					navigationBarHeight = connectedDeviceHeight - (webViewOffsetHeightInPixel + statusBarHeight);

					System.out.println("navigationBarHeight " + navigationBarHeight);
					urlHeight = ((webViewOffsetHeight - webViewHeight) * pixelRatio) + statusBarHeight;
					System.out.println(" urlHeight and  statusBarHeight " + urlHeight);
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
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		
		
		 	public static String getImageAsEncodedString(String path) {
			String imgEncodeString = null;
			File refImgFile = new File(path);

			try {
				if (refImgFile.exists()) {
					imgEncodeString = Base64.getEncoder().encodeToString(Files.readAllBytes(refImgFile.toPath()));
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}

			return imgEncodeString;
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
		
		
		
		
		public boolean verifyRegularExpressionUsingArrays(Mobile_HTML_Report report, String regExp, String[] method)
		{
			String[] Text = method;
			boolean regexp= false;
			try
			{
				Thread.sleep(2000);
			for(int i=0;i<Text.length;i++)
			{
				System.out.println("Text value on ui is "+Text[i]);
			if (Text[i].matches(regExp))
			{
				log.debug("Compared with Reg Expression .Currency is same");
				regexp = true; 
			}Thread.sleep(2000);
			}}
			catch (Exception e) 
			{
				log.error(e.getMessage(), e);
			}	
			return regexp ;		
	}
		
		
		 public static int GetPixelsToMove(WebElement Slider, double Amount, double SliderMax, double SliderMin) {
		        int pixels = 0;
		        int tempPixels = Slider.getSize().getWidth();
		        System.out.println(tempPixels);
		        tempPixels = (int)(tempPixels / (SliderMax - SliderMin));
		        System.out.println(tempPixels);
		        tempPixels = (int) (tempPixels * (Amount - SliderMin));
		        System.out.println(tempPixels);
		        pixels = tempPixels;
		        return pixels;
		    }

		public String getCurrentWinAmt(Desktop_HTML_Report lvcReport, String currencyName, String string,
				String string2) {
			// TODO Auto-generated method stub
			return null;
		}

		public String verifyBigWin(Desktop_HTML_Report lvcReport, String regExpr, String currentWinAmt) {
			// TODO Auto-generated method stub
			return null;
		}
		 
		
		public boolean open_TotalBetupdated(Desktop_HTML_Report report, String languageCode) 
		{
			return false;
			
		}
		public void autoPlay_with_QSUpdated(Desktop_HTML_Report language, String languageCode) 
		{
			
		}
		public String capturePaytableScreenshot_Resize(Desktop_HTML_Report report, String languageCode) 
		 {
			return null;
		 }

	
		public boolean UnlockAllFreeSpin() 
		 {
			return false;
			
		 }
		public boolean clickMenuOnTopbar() 
		{
	        return false;
	    }

		public String verifyFGBigWin(Desktop_HTML_Report lvcReport, String string, String string2) {
			// TODO Auto-generated method stub
			return null;
		}
		public void verifyBetValuesAppended(boolean isBetChangedIntheConsole, String quickBetVal, Desktop_HTML_Report report,String CurrencyName) {
		
		}
		public void miniPaytableScreeShots(Desktop_HTML_Report currencyReport, String currencyName) {
			// TODO Auto-generated method stub
			
		}

		public boolean reSpinOverlayCurrencyFormat(String currencyFormat) {
			// TODO Auto-generated method stub
			return false;
		}

		public HashMap readTranslations(){
			HashMap<String,ArrayList<String>> map = new HashMap<String,ArrayList<String>>();
			return map;
		}


		public Map<String , String> getHooks() {
			
			 Map<String , String> result=null;
			 return result;
		}


		public void verifyTextTranslation(String hook,String text,String languageCode,HashMap<String,ArrayList<String>> translationmap,Desktop_HTML_Report report)
		{
		}

		public String Verifystoryoptioninpaytable(Desktop_HTML_Report report, String languageCode) 
		{
			return languageCode;
		}
		public boolean waitForBigWin()
		{
			return false;
		}

		public  void Payoutvarificationforallbet(Desktop_HTML_Report language) {
			// TODO Auto-generated method stub
			
		}
		public String getMinimumBet()
		{
			
			return null;
		}

		public void verifySpinBtnState(Desktop_HTML_Report language) {
			// TODO Auto-generated method stub
			
		}

		public void autoplayPresets(Desktop_HTML_Report report) {
			// TODO Auto-generated method stub
			
		}

		public boolean verifyBigWinCurrencyFormat(String currencyFormat, Desktop_HTML_Report currencyReport, String currencyName) {
			return false;
		}

		public boolean waitForBigWin(String bigwintexttobevisible) {
			return false;
		}

		public void setDefaultBet() {
			
		}
		public boolean isPaytableAvailable() {
			return false;
		}

		public boolean checkAvilability(String string) {
			
			return false;
		}

		public void validateMenuInBigWin(Desktop_HTML_Report report) {
			// declare in parent class
		}
		public void validatePaytableNavigationInBigWin(Desktop_HTML_Report report)
		{
			// declare in parent class
		}
		public void bigWinQuickSpinOnOffValidation(Desktop_HTML_Report report)
		{
			// declare in parent class
		}
		public void bigwinwithAutoplay(Desktop_HTML_Report report)
		{// declare in parent class
			}
		public void bigWinOnMinimize(Desktop_HTML_Report report){
			// declare in parent class
		}
		public void bigWinResizeValidation(Desktop_HTML_Report report,int x, int y)
		{// declare in parent class
			}
		public void  bigWinTabValidation(Desktop_HTML_Report report){
			// declare in parent class
		}

		public void bigWinWithSpin(Desktop_HTML_Report report) {
			// declare in parent class
			
		}

		public boolean elementWait(String element, boolean b) {
			return b;
			// declare in parent class
			
		}

		public void verifyJackPotBonuswithScreenShots(Desktop_HTML_Report report,String languageCode) {
			// declare in parent class
			
		}

		public void threadSleep(int time){
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				log.debug("Exception while thread sleep",e);
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
						month + "m" + " " + week + "w" + " " + day + "d" + " " + hour + "h" + " " + minutes+ "m");

				return convertedStr;

			}

			public void refresh() {
				//Declear in parent class
			}

			public Map<String, String> getAllTheModalHeaders() {
				return null;
			}

			public String checkIfAnyModelHeaderOccured(Map<String, String> modalHeadersMap) {
				return null;
			}

			public Map<String, String> getAllTheModalFooter() {
				return null;
			}

			public boolean isFreeSpinTriggered() {
				return false;
			}
			
		public void waitFor(String Locator) {
			// declear in parent Library	
			}

		public boolean drawCollectBaseGame(Desktop_HTML_Report report, String languageCode) throws InterruptedException, Exception {
			// TODO Auto-generated method stub
			
			//HashMap<String, Object> value = new HashMap<String, Object>();
			return false;
			
		}

		public boolean drawClick() throws InterruptedException {
			return false;
			// TODO Auto-generated method stub
			
		}

		public void doubleToCollect(Desktop_HTML_Report report) throws InterruptedException, Exception {
			//return false;
			// TODO Auto-generated method stub
			//return false;
		}

		public boolean verifyPaytablePresent() {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean quickSpinOn() {
			// TODO Auto-generated method stub
			return false;
		}

		public void quickSpinClick() {
			// TODO Auto-generated method stub
			
		}

		public boolean quickSpinOff() {
			// TODO Auto-generated method stub
			return false;
		}

		public String getAttributeXpath(String string, String string2) {
			// TODO Auto-generated method stub
			return null;
		}

		public boolean waitForWin() {
			// TODO Auto-generated method stub
			return false;
		}

		public String GetBetAmtString() {
			// TODO Auto-generated method stub
			return null;
		}

		public boolean dealClick() throws InterruptedException {
			return false;
			// TODO Auto-generated method stub
			
		}

		public void doubleToGambleReached(Desktop_HTML_Report languageReport, String languageCode) throws InterruptedException, Exception {
			// TODO Auto-generated method stub
			
		}

		public void paytableClickVideoPoker(Desktop_HTML_Report languageReport, String languageCode) throws Exception {
			// TODO Auto-generated method stub
			
		}

		public boolean quickSpinDisabledForDenmark() {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean clockDisplayForDenmark() {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean helpLinkDisplayForDenmark() {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean linkToPlayerProtection() {
			// TODO Auto-generated method stub
			return false;
		}

		public void setSoundFalgActive(boolean b) {
			
		}

		public void startAutoPlay() {
			
		}

		public void clickFregamesPlay() {
			
		}

		public boolean addFreeGameToUserInBluemesa(String userName2, int defaultNoOfFreeGames, String offerExpirationUtcDate,
				String balanceTypeID, int mid, int cid,@Optional("1")  int ofOfOffers ) {
					return false;
			
		}

	/*	public boolean addFreeGameToUserInAxiom(String randomUserName, int defaultNoOfFreeGames, String offerExpirationUtcDate,
				String balanceTypeID, int mid, int cid,@Optional("1")int ofOfOffers ) {
			return false;
		}
*/
		public boolean addFreeGameToUserInAxiom(String randomUserName, int defaultNoOfFreeGames,
				String offerExpirationUtcDate, String balanceTypeID, int mid, int cid, @Optional("1") int noOfOffers) {
			RestAPILibrary apiobj = new RestAPILibrary();
			CommonUtil util = new CommonUtil();
			boolean isFreeGameAssigned = false;
			for (int OfferCnt = 1; OfferCnt <= noOfOffers; OfferCnt++) {
				isFreeGameAssigned = apiobj.createFreeGameInAxiom(randomUserName, util.randomFreegameOfferName(),
						defaultNoOfFreeGames, offerExpirationUtcDate, balanceTypeID, mid, cid);

			}
			return isFreeGameAssigned;
		}
		
		public String moveCoinSizeSliderToMaxBet() {
			return null;
			
		}

		public void unlockBonus(Desktop_HTML_Report language) {
			
		}

		public String replaceParamInHook(String selectBonus, Map<String, String> paramMap) {
			return null;
		}

		public boolean isGameNameVisibleInCurrentScene() {
			return false;
		}

		public boolean elementWait(String string, boolean b, int i) {
		return false;	
		}

		public void paytableclosefunction(String locator) {

			Wait = new WebDriverWait(webdriver, 60);
			try {
				log.debug("Waiting For paytable close");
				Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(locator))).click();;
			} catch (Exception e) {
				log.error("error while paytable close :" + locator, e);
			}
			
		}

		public void moveCoinSizeSliderToMinBet() {
			Wait = new WebDriverWait(webdriver, 5000);
			try {
				func_Click(XpathMap.get("OneDesignbetbutton"));
				WebElement coinSizeSlider = webdriver.findElement(By.xpath(XpathMap.get("OneDesignCoinSizeSlider_ID")));
				Thread.sleep(3000);
				Actions action = new Actions(webdriver);
				action.dragAndDropBy(coinSizeSlider,-150, 0).build().perform();

			} catch (Exception e) {
				log.debug(e.getMessage());
			}
			
		}

		public boolean doubleToGambleReachedFunctionality(Desktop_HTML_Report sanityReport) {
			// TODO Auto-generated method stub
			return false;
			
		}

		public void doubleToGambleReached(Desktop_HTML_Report sanityReport) {
			// TODO Auto-generated method stub
			
		}
		/**
		 * method is used to perform click action 
		 * @param locator
		 * @return
		 * @author rk61073
		 */
		public boolean fun_Click(String locator) 
		{
			return false;
		}
		/**
		 * method is used to navigate 
		 * @param report
		 * @param gameurl
		 * @author rk61073
		 */
		public void checkpagenavigation(Desktop_HTML_Report report, String gameurl,String CurrencyName) 
		{

		}
		/**
		 * method is used to navigate 
		 * @param report
		 * @param gameurl
		 * @author rk61073
		 */
		public void checkpagenavigation(Desktop_HTML_Report report, String gameurl) 
		{

		}
		/**
		 * validated the hambugermenu options 
		 * @author rk61073
		 */
		public void hamburgerMenu(Desktop_HTML_Report report,String CurrencyName)
		{	
		}
		/**
		 * validated the hambugermenu options 
		 * @author rk61073
		 */
		public void menu(Desktop_HTML_Report report)
		{	
		}
		/**
		 * method is for paytable 
		 * @author rk61073
		 */
		public boolean paytable(Desktop_HTML_Report lvcReport, String currencyName) 
		{
			// TODO Auto-generated method stub
			return false;
		}
		/**
		 * method is for clock 
		 * @author rk61073
		 */
		public String clockfromTopBar(Desktop_HTML_Report lvcReport, String currencyName) 
		{
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * 
		 * @param locator
		 * @return
		 * @author rk61073
		 */
		public String fun_GetText(String locator) 
		{
			return null;
			
		}

		/**
		 * method is for click & get text 
		 * @param report
		 * @param menuclick
		 * @param locator
		 * @return
		 * @author rk61073
		 */
		public String clickAndGetText(Desktop_HTML_Report report, String menuclick ,String locator) 
		{
			return null;
		}
		/**method is for click and navigate 
		 * @param report
		 * @param locator
		 * @return
		 * @author rk61073
		 */
		public boolean clickAndNavigate(Desktop_HTML_Report report , String locator,String currencyName) 
		{
			return false;

		}
		/**
		 * method is to validate help from top bar & navigates to help
		 * @param report
		 * @return
		 * @throws InterruptedException
		 * @author rk61073
		 */
		public boolean helpFromTopBarMenu(Desktop_HTML_Report report,String currencyName) throws InterruptedException
		{
			return false;
			
		}
		/**
		 * Verifies the Currency Format -  using String method
		 * @param isoCode
		 * @param regExp
		 * @param method
		 * @return
		 * @author rk61073
		 */
		public boolean verifyRegularExpression(Desktop_HTML_Report report, String regExp, String method)
		{
			return false;

		}
		/**
		 * method is to enable & disable the quick deal button 
		 * @param report
		 * @param method
		 * @return
		 *  @author rk61073
		 */
		public boolean isQuickDealButtonAvailable(Desktop_HTML_Report report, String currencyName)
		{
			return false;
		}
		/**
		 * method is to validate the total bet 
		 * @param report
		 * @param regExpr
		 * @param currencyName
		 * @return
		 * @author rk61073
		 */
		public boolean setMinBet(Desktop_HTML_Report report, String currencyName)
		{
			return false;

		}
		/**
		 * method is to validate the total bet 
		 * @param report
		 * @param regExpr
		 * @param currencyName
		 * @return
		 * @author rk61073
		 */
		public boolean setMaxBet(Desktop_HTML_Report report, String currencyName)
		{
			return false;

		}
		public void closeOverlay() {}
		/**method is to close the overlay
		 * @author rk61073
		 */
		public void closetheOverlay(int x ,int y) 
		{
		}
		/**
		 * method is used to click  the x ele& y co-ordinates using actions class
		 * @param x
		 * @param y
		 * @author rk61073
		 */
		public void tapOnCoordinates(String locator, int x, int y)
		{

		}
		/**
		 * method is used to click  the x ele& y co-ordinates using actions class
		 * @param x
		 * @param y
		 * @author rk61073
		 */
		public String tapOnCoordinate(String locator, int x, int y)
		{
			return null;

		}
		/**
		 * method is to get the paytable payouts
		 * @param report
		 * @param CurrencyName
		 * @return
		 * @author rk61073
		 */
		public String[] paytablePayoutsbyChangingTheCoinSizes(Desktop_HTML_Report report, String CurrencyName) 
		{
			return null;
		}
		/**
		 * Verifies the Currency Format using arrays
		 * @param report
		 * @param regExp
		 * @param method
		 * @return
		 * @author rk61073
		 */
		public boolean verifyRegularExpressionUsingArrays(Desktop_HTML_Report report, String regExp, String[] method)
		{
			return false;
		}


		/**
		 * method is to get the double the amount
		 * @param report
		 * @param CurrencyName
		 * @return
		 * @author rk61073
		 */
		public void doubleTheAmountToDouble(Desktop_HTML_Report report,String regExpr, String currencyName)
		{
			//return false;

		}
		/**
		 * method is to validate totalwin amaount 
		 * @param report
		 * @param regExpr
		 * @param currencyName
		 * @return
		 * @author rk61073
		 */
		public boolean totalWin(Desktop_HTML_Report report,String regExpr, String currencyName)
		{
			return false;
			
		}
		/**
		 * method is to check the availability if an element 
		 * @param string
		 * @return
		 * @author rk61073
		 */
		public boolean checkAvilabilityofElement(String element) 
		{	
			return false;
		}

		/**
		 * method is to get the deal ,draw & Validate Total Win  amount
		 * @param report
		 * @param CurrencyName
		 * @return
		 * @author rk61073
		 */
		public boolean clickOnDealDrawAndValidateTotalWin(Desktop_HTML_Report report,String regExpr, String currencyName)
		{
			return false;

		}

		/**
		 * method is to close the settings
		 * @param report
		 * @param CurrencyName
		 * @return
		 * @author rk61073
		 */
		public boolean closeSettings(Desktop_HTML_Report report,String CurrencyName)
		{
			return false;


		}
		/**
		 * method is to close the settings
		 * @param report
		 * @param CurrencyName
		 * @return
		 * @author rk61073
		 */
		public boolean creditValidationAndCollectTheAmt(Desktop_HTML_Report report, String languageCode)
		{
			return false;
			
		}
		/**
		 * method is used to perform click action 
		 * @param locator
		 * @return
		 * @author rk61073
		 */
		public boolean fun_ClickByID(String locator) 
		{
			return false;
		}
		/**
		 * 
		 * @param report
		 * @param currencyName
		 * @return
		 */
		public boolean openBet(Desktop_HTML_Report report, String currencyName)
		{
			return false;

		}
		/**
		 * 
		 * @param report
		 * @param language
		 * @return
		 * @author rk61073
		 */
		public boolean refresh(Desktop_HTML_Report report, String language)
		{
			return false;
			
		}
		/**
		 * method is to get the paytable payouts
		 * @param report
		 * @param CurrencyName
		 * @return
		 * @author rk61073
		 */
		public boolean paytablePayoutsByChangingTheCoinSizes(Desktop_HTML_Report report,String regExpr,String CurrencyName) 
		{
			return false;
		}
		/**
		 * 
		 * @param locator
		 * @return
		 * @author rk61073
		 */
		public boolean func_Click_BYID(String locator)
		{
			return false;
			
		}
		/**
		 * this method changes the coinsize by clicking on decrement button on bet  & validate the text from  coinsize from total bet & coin size 
		 * @return
		 * @author rk61073
		 */
		public boolean betPlusButtonValidation( Desktop_HTML_Report  report, String CurrencyName )
		{
			return false;
		}
		/**
		 * this method changes the coinsize by clicking on left and right buttons & validate the text from  coinsize from total bet & coin size 
		 * @return
		 * @author rk61073
		 */
		public boolean betMinusButtonValidation( Desktop_HTML_Report  report, String CurrencyName )
		{
			return false;
		}
		/**
		 * this method changes the coinsize by clicking on left  buttons & validate the text from coinsfromtotalbet & coins 
		 * @return
		 * @author rk61073
		 */
		public boolean coinsMinusButtonValidation(Desktop_HTML_Report  report, String CurrencyName )
		{
			return false;
		}
		/**
		 * this method changes the coinsize by clicking on right buttons & validate the text from coinsfromtotalbet & coins 
		 * @return
		 * @author rk61073
		 */
		public boolean coinsPlusButtonValidation(Desktop_HTML_Report  report, String CurrencyName )
		{
			return false;
		}
		/**
		 * this method clicks on betplusone button & validate the text from  CoinText & CoinTextFromTotalBet
		 * @return
		 * @author rk61073
		 */
		public boolean betPlusOneButton()
		{
			return false;
		}
		/**
		 * validates if the bet increment & decrement buttons available & enabled or not 
		 * @return
		 * @author rk61073
		 */
		public boolean isBetPlusAndMinusButtonsAvailableAndEnabled(Desktop_HTML_Report report,String CurrencyName)
		{
			return false;
			
		}
		/**
		 * validates if the coins increment & decrement buttons available & enabled or not 
		 * @return
		 * @author rk61073
		 */
		public boolean isCoinsPlusAndMinusButtonsAvailableAndEnabled(Desktop_HTML_Report report,String CurrencyName)
		{
		return false;
		}
		/**
		 * validates if the bet plus button  available & enabled or not 
		 * @return
		 * @author rk61073
		 */
		public boolean isBetPlusOneButtonAvailableAndEnabled(Desktop_HTML_Report report,String CurrencyName)
		{
			return false;	
		}
		/**
		 * validates if the bet max button  available & enabled or not 
		 * @return
		 * @author rk61073
		 */
		public boolean isBetMaxButtonAvailableAndEnabled(Desktop_HTML_Report report,String CurrencyName)
		{
			return false;	
		}
		/**
		 * 
		 * @return
		 */
		public boolean isBetMaxButtonAvailableAndDisabled(Desktop_HTML_Report  report, String CurrencyName)
		{
			return false;	
		}
		/**
		 * 
		 * @return
		 */
		public boolean isBetPlusOneButtonAvailableAndDisabled(Desktop_HTML_Report  report, String CurrencyName)
		{
			return false;	
		}
		/**
		 * 
		 * @return
		 */
		public boolean isCoinsPlusAndMinusButtonsAvailableAndDisabled(Desktop_HTML_Report  report, String CurrencyName)
		{
			return false;	
		}
		/**
		 * 
		 * @return
		 */
		public boolean isBetPlusAndMinusButtonsAvailableAndDisabled(Desktop_HTML_Report  report, String CurrencyName)
		{
			return false;	
		}


		/**
		 * method validate the payouts by clicking on coins decrement / minus button 
		 * @return
		 * @author rk61073
		 */
		public boolean validateThePayoutsByClickingOnCoinsDecrementButtons()
		{
			return false;
		}

		/**
		 * method validate the payouts and coins updation from the base & bet panel by clicking on BetPlusOneButton
		 * @return
		 * @author rk61073
		 */
		public boolean betPlusOneButtonValidation(Desktop_HTML_Report  report, String CurrencyName)
		{
			return false;
		}
		/**
		 * method validate the change in the Coins but not in coinsize by clicking on paytable for 5 times in loop 
		 * @return
		 * @author rk61073
		 */
		public boolean payoutsValidationByChangingCoins( Desktop_HTML_Report  report, String CurrencyName)
		{
			return false;
		}
		/**
		 * method validate the change in Coins in Base and in BetPanel by Clicking on BetMax Button 
		 * @return
		 * @author rk61073
		 */
		public void betMaxButtonValidation(Desktop_HTML_Report  report, String CurrencyName)
		{
			
		}
		/**
		 * 
		 * @return
		 * @author rk61073
		 */
		public boolean coinsUpdationInBaseAndInBetPanel()
		{
			return false;	
		}

		/**
		 * method is for no win 
		 * @return
		 * @author rk61073
		 */
		public boolean noWin(Desktop_HTML_Report  report, String CurrencyName)
		{
			return false;	
		}
		/**
		 * 
		 * @return
		 * @author rk61073
		 */
		public boolean isQuickDealButtonAvailableAndEnabled(Desktop_HTML_Report  report, String CurrencyName)
		{
			return false;	
		}
		/**
		 * 
		 * @return
		 * @author rk61073
		 */
		public boolean isQuickDealButtonAvailableAndDisabled(Desktop_HTML_Report  report, String CurrencyName)
		{
			return false;	
		}
		/**
		 * 
		 * @param report
		 * @param language
		 * @return
		 * @author rk61073
		 */
		public boolean refreshTheGame(Desktop_HTML_Report report, String language)
		{
			return false;	
		}
		/**
		 * method is to validate totalwin amaount 
		 * @param report
		 * @param currencyName
		 * @return
		 * @author rk61073
		 */
		public boolean totalWin(Desktop_HTML_Report report, String currencyName)
		{
			return false;	
		}
		/**
		 * method is to get the deal ,draw & Validate Total Win  amount
		 * @param report
		 * @param CurrencyName
		 * @return
		 * @author rk61073
		 */
		public boolean clickOnDealDrawAndValidateTotalWin(Desktop_HTML_Report report, String currencyName)
		{
			return false;
		}
		/**
		 * method is to get the double the amount
		 * @param report
		 * @param CurrencyName
		 * @return
		 * @author rk61073
		 */
		public void doubleTheAmountToDouble(Desktop_HTML_Report report, String currencyName)
		{
			
		}
		/**
		 * method is to get the double the amount
		 * @param report
		 * @param CurrencyName
		 * @return
		 * @author rk61073
		 */
		public boolean isDoubleTOButtonAndCollectButtonAvailableAndEnabled(Desktop_HTML_Report report, String currencyName)
		{
			return false;
		}
		/**
		 * method is to get the paytable payouts
		 * @param report
		 * @param CurrencyName
		 * @return
		 * @author rk61073
		 */
		public boolean paytablePayoutsByChangingTheCoinSizes(Desktop_HTML_Report report,String CurrencyName) 
		{
			return false;
		}
		/**
		 * 
		 * @param report
		 * @param currencyName
		 * @return
		 * @author rk61073
		 */

		public boolean newConsoleButtonsAreEnabledOnRefresh(Desktop_HTML_Report report, String currencyName)
		{return false;}

		/**
		 * method is to validate the buttons are disabled are not 
		 * @param report
		 * @param currencyName
		 * @return
		 * @author rk61073
		 */

		public boolean newConsoleButtonsAreDisabled(Desktop_HTML_Report report, String currencyName)
		{return false;}

		/**
		 * method is to validate the buttons are enabled are not 
		 * @param report
		 * @param currencyName
		 * @return
		 * @author rk61073
		 */

		public boolean newConsoleButtonsAreEnabled(Desktop_HTML_Report report, String currencyName)
		{return false;}

		/**
		 * method validates the coins text in base and in bet panel 
		 * @return
		 */
		public boolean totalBetInBaseAndInBetPanel()
		{return false;}


		/**
		 * method is to validate the status ok button 
		 * @param report
		 * @param currencyName
		 * @return
		 * @author rk61073
		 */

		public boolean verifyStatsOkButton(Desktop_HTML_Report report, String currencyName)
		{return false;}

		/**
		 * method is to validate the status reset button 
		 * @param report
		 * @param currencyName
		 * @return
		 * @author rk61073
		 */

		public boolean  verifyStatsResetButton(Desktop_HTML_Report report, String currencyName)
		{return false;}
		/**
		 * method is to validate the buttons are disabled are not 
		 * @param report
		 * @param currencyName
		 * @return
		 * @author rk61073
		 */

		public boolean newConsoleButtonsAreEnabledOnBacktoBaseGameFromDoubleToScreen(Desktop_HTML_Report report, String currencyName)
		{return false;}
		/**
		 * method is to slide to  max bet
		 * @param report
		 * @param regExpr
		 * @param currencyName
		 * @return
		 * @author rk61073
		 */
		public boolean setMaxBetAndValidate(Desktop_HTML_Report report, String currencyName)
		{return false;}

		/**
		 * method is to slide to  MinBet
		 * @param report
		 * @param currencyName
		 * @return
		 * @author rk61073
		 */
		public boolean setMinBetAndValidate(Desktop_HTML_Report report, String currencyName)
		{return false;}
		/**
		 * 
		 * @return
		 * @author rk61073
		 */
		public boolean slideTheCoinSliderToMax()
		{return false;}
		/**
		 * 
		 * @return
		 * @author rk61073
		 */
		public boolean slideTheCoinsToMax()
		{return false;}
		/**
		 * method validates the held startegy 
		 * @return
		 * @author rk61073
		 */
		public void verifyHeldCardsAndClick()
		{
		}
		/**
		 * validated the hambugermenu options 
		 * @author rk61073
		 */
		public void hamburgerMenuWithResize(Desktop_HTML_Report report,String currencyName)
		{
			
		}
		/**
		 * 
		 * @return
		 * @throws InterruptedException
		 * @author rk61073
		 */
		public boolean dealBtn() throws InterruptedException
		{return false;}
		/**
		 * 
		 * @return
		 * @throws InterruptedException
		 * @author rk61073
		 */
		public boolean drawBtn() throws InterruptedException 
		{return false;}
		/**
		 * 
		 * @return
		 * @throws InterruptedException
		 * @author rk61073
		 */
		public boolean noWin() throws InterruptedException 
		{return false;}
		/**
		 * 
		 * @return
		 * @throws InterruptedException
		 * @author rk61073
		 */
		public boolean collectBtn() throws InterruptedException 
		{return false;}
		/**
		 * 
		 * @return
		 * @throws InterruptedException
		 * @author rk61073
		 */
		public boolean payTableClose() throws InterruptedException 
		{return false;}
		/**
		 * 
		 * @return
		 * @throws InterruptedException
		 * @author rk61073
		 */
		public boolean doubleBtn() throws InterruptedException 
		{return false;}
		/**
		 * 
		 * @author rk61073
		 */
		public void waitForDealButton() 
		{}
		/**
		 * validate gamble limit reached for lvc
		 * @author rk61073
		 */
		public void gambleLimitReachedForLVC(Desktop_HTML_Report report,String regExpr, String currencyName)
		{}
		/**
		 * validate ststs for lvc 
		 * @param report
		 * @param regExprNoSymbol
		 * @param CurrencyName
		 */
		public void validateStatsForLVC(Desktop_HTML_Report report,String regExprNoSymbol,  String CurrencyName) 
		{}
		/**
		 * 
		 * @param report
		 * @param language
		 * @return
		 * @author rk61073
		 */
		public boolean gameLogo(Desktop_HTML_Report report, String language)
		{return false;}
		/**
		 * 
		 * @return
		 * @throws InterruptedException
		 * @author rk61073
		 */
		public boolean drawButton() throws InterruptedException 
		{return false;}
		
		/**		 
		 * @author VC66297
		 */
		public String getCurrentWinAmt1(Desktop_HTML_Report report, String CurrencyName) {
			return null;
		}
		public String verifyBigWin(Desktop_HTML_Report report, String CurrencyName,String SpinBtnCoordinatex ,String SpinBtnCoordinatey) {
			return null;
		}
			
		public String verifyAmgWin(Desktop_HTML_Report report, String CurrencyName,String SpinBtnCoordinatex ,String SpinBtnCoordinatey) {
			return null;
		}
			
		public String verifyFGBigWin(Desktop_HTML_Report report,ImageLibrary imageLibrary) {
			return null;
		}
		public String getFGCurrentWinAmt(Desktop_HTML_Report report,String SpinBtnCoordinatex ,String SpinBtnCoordinatey) {
			return null;
		}
			
		public String verifyFreeSpinBonusWin(Desktop_HTML_Report report, String CurrencyName) {
			return null;
		}
		
		
		public String freeGameEntryInfo(Desktop_HTML_Report report,String currencyName,ImageLibrary imageLibrary, String fgInfotxt ) throws InterruptedException  
		{
			return null;
		}
	    
	    public void verifyquickbetImg(Desktop_HTML_Report report, String languageCurrency, String regExpr,
				ImageLibrary imageLibrary) {		
			
		}

		public String verifyBigWinImg(Desktop_HTML_Report report, ImageLibrary imageLibrary) {
					return null;
		}

		public String verifyAmgWinImg(Desktop_HTML_Report report, ImageLibrary imageLibrary) {		
			return null;
		}
		/**
		 * This method is used to compare two strings
		 * @author pb61055
		 * @param expectedText
		 * @param actualText
		 * @return
		 */
		public boolean compareText(String expectedText,String actualText,String hasHook) 
		{
			return false;
		
		}

		public void navigate_back() throws InterruptedException {
			// TODO Auto-generated method stub
			
		}

		
		
		
}

	
	