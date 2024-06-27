package Modules.Regression.FunctionLibrary;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.interactions.Actions;

import org.openqa.selenium.support.ui.ExpectedConditions;

import org.openqa.selenium.support.ui.WebDriverWait;

import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.utils.Constant;

import com.zensar.automation.framework.utils.Util;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.ImageLibrary;
import com.zensar.automation.library.TestPropReader;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.offset.PointOption;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarLog;

public class CFNLibrary_Desktop_PlayNext extends CFNLibrary_Desktop {
	public String extentScreenShotPath = null;
	long Avgquickspinduration = 0;
	long Avgautoplayduration = 0;
	WebDriverWait Wait;
	String playNextNamespace = null;

	public Logger log = Logger.getLogger(CFNLibrary_Desktop_PlayNext.class.getName());

	Util clickAt = new Util();

	public CFNLibrary_Desktop_PlayNext(WebDriver webdriver, BrowserMobProxyServer browserproxy,
			Desktop_HTML_Report report, String gameName) throws IOException {
		super(webdriver, browserproxy, report, gameName);
		this.GameName = gameName.trim();

		log.info("Functionlibrary object created with test data");
	}

	public void maxiMizeBrowser() {
		// Maximize the window
		webdriver.manage().window().maximize();
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

	public void setNameSpace() {
		int nameSpaceCount = Integer.parseInt(TestPropReader.getInstance().getProperty("PlayNextNamespaceCount"));

		for (int count = 0; count < nameSpaceCount; count++) {
			String nameSpace = TestPropReader.getInstance().getProperty("PlayNextNamespace" + count);

			String namspaceResponce = null;
			namspaceResponce = getConsoleText("return " + nameSpace + ".currentScene");
			if (namspaceResponce != null && !"".equals(namspaceResponce)) {
				log.debug("NameSpace for this game :: " + nameSpace);
				playNextNamespace = nameSpace;
				log.debug("namsspace for game=" + nameSpace);
				break;
			}
		}

	}

	public String getConsoleText(String text) {
		String consoleText = null;
		try {
			JavascriptExecutor js = ((JavascriptExecutor) webdriver);
			consoleText = (String) js.executeScript(text);
		} catch (Exception e) {
			e.getMessage();
		}
		return consoleText;
	}

	

	public String func_GetText(String locator) {
		try {
			String ele = getConsoleText("return " + XpathMap.get(locator));
			System.out.println("" + ele);
			log.debug(ele);
			return ele;

		} catch (NoSuchElementException e) {
			return null;
		}
	}
	
	
		public String func_GetText1(String locator) 
		{
			try 
			{
				WebElement ele = webdriver.findElement(By.xpath(XpathMap.get(locator)));
				System.out.println(""+ele.getText());log.debug(ele.getText());
				return ele.getText();
				
			} 
			catch (NoSuchElementException e)
			{
				return null;
			}
		}
	
	public String Func_GetText(String locator) 
	{
		String ele="";
		try 
		{
			ele = webdriver.findElement(By.xpath(XpathMap.get(locator))).getText();
			System.out.println(ele);log.debug(ele);
		}					
		catch (NoSuchElementException e)
		{
			System.out.println(e);
		}
		return ele;
	}

	/*
	 * Compare the Regular expression using String
	 */
	public boolean verifyRegularExpression(String curencyAmount, String regExp) {
		boolean isRegExp = false;
		try {
			log.debug("curencyAmount: " + curencyAmount);
			if (curencyAmount.matches(regExp)) {
				isRegExp = true;
				// System.out.println("Currency format is correct");
				log.debug("Currency format is correct");
			} else {
				isRegExp = false;
				// System.out.println("Currency format is incorrect");
				log.debug("Currency format is incorrect");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return isRegExp;

	}

	public boolean isElementVisible(String element) {

		boolean visible = false;
		try {
			String elements = "return " + XpathMap.get(element);
			log.debug("Checking element visibility");
			visible = GetConsoleBooleanText(elements);
		} catch (Exception e) {
			log.debug(e.getMessage(), e);
		}

		return visible;
	}

	public void ClickByCoordinates(String cx, String cy) {
		Wait = new WebDriverWait(webdriver, 50);
		// boolean visible = false;
		int dx = 0, dy = 0, dw = 0, dh = 0;
		JavascriptExecutor js = ((JavascriptExecutor) webdriver);

		System.out.println(js.executeScript(cx));
		System.out.println(js.executeScript(cy));
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

		long bodyHeight = webdriver.findElement(By.id("viewporter")).getRect().getHeight();
		long bodyWidth = webdriver.findElement(By.id("viewporter")).getRect().getWidth();
		int topLeftX = (int) bodyWidth / 2;
		int topLeftY = (int) bodyHeight / 2;
		Actions actions = new Actions(webdriver);
		actions.moveToElement(webdriver.findElement(By.id("viewporter")), 0, 0);
		actions.moveToElement(webdriver.findElement(By.id("viewporter")), -topLeftX, -topLeftY);
		log.debug("topLeftX: " + topLeftX + " topLeftY: " + topLeftY);
		actions.moveByOffset(dx, dy).click().perform();	
	
	}



	public boolean details_append_folderOnlyScreeshot(WebDriver webdriver, String screenShotName) {
		boolean isScreenshotTaken = false;
		// Captures screenshot for calling step
		File scrFile = ((TakesScreenshot) webdriver).getScreenshotAs(OutputType.FILE);
		// Now you can do whatever you need to do with it, for example copy somewhere
		String screenShotPath = extentScreenShotPath + "\\" + screenShotName + ".jpg";
		File destFile = new File(screenShotPath);

		try {
			FileUtils.copyFile(scrFile, destFile);
			isScreenshotTaken = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		scrFile.delete();
		scrFile = null;
		destFile = null;
		return isScreenshotTaken;
	}

	public void waitForSpinButton() {
		Wait = new WebDriverWait(webdriver, 3000);
		try {
			log.debug("Waiting for base scene to load");
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("isSpinBtnVisible"))));

		} catch (Exception e) {
			log.error("error while waiting for base scene to come", e);
		}
	}

	/**
	 * Verifies the Autoplay
	 * 
	 */

	public boolean isAutoplayAvailable() {
		boolean isAutoplayAvailable = false;
		String autoplay = "isAutoplayMenuBtnVisible";
		try {
			func_Click(autoplay);
			System.out.println("Autoplay Opened");
			log.debug("Autoplay Opened");
			isAutoplayAvailable = true;
			Thread.sleep(2000);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return isAutoplayAvailable;

	}

	public void resizeBrowser(int a, int b) {
		Dimension d = new Dimension(a, b);
		// Resize current window to the set dimension
		webdriver.manage().window().setSize(d);
	
	}

	public boolean elementVisible_Xpath(String Xpath) {
		boolean visible = false;
		if (webdriver.findElement(By.xpath(Xpath)).isDisplayed()) {
		
			visible = true;
		}
		return visible;

	}

	// navigate back to base game from different window and verify
	public void navigate_back() throws InterruptedException {
		Thread.sleep(1000);
		String parentWindow = webdriver.getWindowHandle();
		Set<String> handles = webdriver.getWindowHandles();
		for (String windowHandle : handles) {
			if (!windowHandle.equals(parentWindow)) {
				webdriver.switchTo().window(windowHandle);
				// Perform your operation here for new window
				Thread.sleep(6000);
				String title = webdriver.getTitle();
				log.debug(getPageTitle(webdriver));
				details_append_folderOnlyScreeshot(webdriver, title);

				Thread.sleep(2000);
				webdriver.close(); // closing child window
				webdriver.switchTo().window(parentWindow);
				log.debug("NAvigated to base game");

			}
		}
	}

	public void RefreshGame(String Element) {
		webdriver.navigate().refresh();
		if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(XpathMap.get("TypeOfGame"))) {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get(Element))));
		} else {
			log.debug("Waiting for clock to be visible");
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get(Element))));
			log.debug("Waiting for clock is visible");
		}
		log.debug("game Refresh  ");
	}

	


	public boolean paytableScroll(Desktop_HTML_Report report, String currencyName) {
		boolean paytableScroll = false;
		try {
			if (XpathMap.get("paytableScrollOfFive").equalsIgnoreCase("yes")) {
				paytableScroll = paytableScrollOfFive(report, currencyName);
				return paytableScroll;
			} else if (XpathMap.get("paytableScrollOfNine").equalsIgnoreCase("yes")) {
				paytableScroll = paytableScrollOfNine(report, currencyName);
				return paytableScroll;
			} else {
				System.out.println("Check the Paytable Scroll");
				log.debug("Check the Paytable Scroll");
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return paytableScroll;
	}

	

	public void verifyquickbet(Desktop_HTML_Report report, String currencyName ,String regExpr) {
		String betVal ;
		// check if max bet button is visible to know whether bet panel is open
		try
		{	
	    boolean isMaxBet = isElementVisible("isMaxBetVisible");
		if (isMaxBet) {
		
			report.detailsAppend("Verify Max Bet is visible "," Max Bet is visibled", "Max Bet is visible ","Pass"+ currencyName);									
			ClickByCoordinates("return " + XpathMap.get("MaxBetCoordinatex"),"return " + XpathMap.get("MaxBetCoordinatey"));
			report.detailsAppend("Verify Max Bet is Clicked "," Max Bet is Clicked", "Max Bet isClicked ","Pass"+ currencyName);									
			boolean betAmt = verifyRegularExpression(report, regExpr, getConsoleText("return "+ XpathMap.get("BetTextValue")));
			openBetPanel();		
		}
			boolean bet1 = isElementVisible("isQuickBet1");
			if(bet1) {
				betVal = getConsoleText("return " + XpathMap.get("QuickBet1Value"));	
				report.detailsAppend("Verify Bet1 is visible "," Bet 1 is visibled", "Bet 1 is visible ","Pass"+ currencyName);									
				ClickByCoordinates("return " + XpathMap.get("QuickBet1Coordinatex"),"return " + XpathMap.get("QuickBet1Coordinatey"));
				report.detailsAppend("Verify Bet 1 is Clicked "," Bet 1 is Clicked", "Bet 1 isClicked ","Pass" + currencyName);							
				
				boolean isBetChangedIntheConsole = isBetChangedIntheConsole(betVal);
				verifyBetValuesAppended(isBetChangedIntheConsole ,betVal ,report);
				openBetPanel();	
				
				
			}
			boolean bet2 = isElementVisible("isQuickBet2");			
            if(bet2) {
            	betVal = getConsoleText("return " + XpathMap.get("QuickBet2Value"));	
            	report.detailsAppend("Verify Bet2 is visible "," Bet 2 is visibled", "Bet 2 is visible ","Pass"+ currencyName);									
				ClickByCoordinates("return " + XpathMap.get("QuickBet2Coordinatex"),"return " + XpathMap.get("QuickBet2Coordinatey"));
				report.detailsAppend("Verify Bet 2 is Clicked "," Bet 2 is Clicked", "Bet2 isClicked ","Pass" + currencyName);
				
				boolean isBetChangedIntheConsole = isBetChangedIntheConsole(betVal);
				verifyBetValuesAppended(isBetChangedIntheConsole ,betVal ,report);			
				openBetPanel();			
			}
        	boolean bet3 = isElementVisible("isQuickBet3");
            if(bet3) {
            	betVal = getConsoleText("return " + XpathMap.get("QuickBet3Value"));			
            	report.detailsAppend("Verify Bet3 is visible "," Bet 3 is visibled", "Bet 3 is visible ","Pass"+ currencyName);									
				ClickByCoordinates("return " + XpathMap.get("QuickBet3Coordinatex"),"return " + XpathMap.get("QuickBet3Coordinatey"));
				report.detailsAppend("Verify Bet 3 is Clicked "," Bet 3 is Clicked", "Bet 3 isClicked ","Pass" + currencyName);	
				
				boolean isBetChangedIntheConsole = isBetChangedIntheConsole(betVal);
				verifyBetValuesAppended(isBetChangedIntheConsole ,betVal ,report);				
				openBetPanel();			
				
			}
            if (XpathMap.get("CoinSizeSliderPresent").equalsIgnoreCase("Yes")) {
				betVal =getConsoleText("return " + XpathMap.get("BetMenuBetValue")); 				
				System.out.println("bet menu value " +betVal);

				Thread.sleep(2000);
				clickAtButton("return " + XpathMap.get("CoinSizeSliderSet"));
				Thread.sleep(2000);

				String betVal1 = getConsoleText("return " + XpathMap.get("BetMenuBetValue"));	

				if (betVal.equalsIgnoreCase(betVal1) != true) {
					details_append_folderOnlyScreeshot(webdriver,"CoinSizeSliderWorking");

					report.detailsAppend("Base Game quick button"," Coin size slider is working ", "Coin size slider is working ","Pass" + currencyName);
				} else {
					details_append_folderOnlyScreeshot(webdriver,"CoinSizeSliderNtWorking");
					report.detailsAppend("Base Game quick button"," Coin size slider is not working ","Coin size slider is not working ", "Fail" + currencyName);
				}
			}
        	boolean bet4 = isElementVisible("isQuickBet4");
            if(bet4) {
            	betVal = getConsoleText("return " + XpathMap.get("QuickBet4Value"));	
            	report.detailsAppend("Verify Bet4 is visible "," Bet 4 is visibled", "Bet 4 is visible ","Pass" + currencyName);									
				ClickByCoordinates("return " + XpathMap.get("QuickBet4Coordinatex"),"return " + XpathMap.get("QuickBet4Coordinatey"));
				report.detailsAppend("Verify Bet 4 is Clicked "," Bet 4 is Clicked", "Bet 4 isClicked ","Pass" + currencyName);	
				
				boolean isBetChangedIntheConsole = isBetChangedIntheConsole(betVal);
				verifyBetValuesAppended(isBetChangedIntheConsole ,betVal ,report);				
				//openBetPanel();			
				
			}					

			
		
		
		} catch (Exception e) 
        {
	        log.error(e.getMessage(), e);
        }
	}

	public void verifyBetValuesAppended(boolean isBetChangedIntheConsole, String quickBetVal, Desktop_HTML_Report report) {
		try {
			if (isBetChangedIntheConsole) {
				report.detailsAppend("verify that is Bet Changed In the Console for quick bet  =" + quickBetVal,
						"Is Bet Changed In the Console for quick bet  =" + quickBetVal, "Bet Changed In the Console",
						"Pass");
				log.debug("isCreditDeducted :: Pass for quick bet value =" + quickBetVal);
			} else {
				report.detailsAppend("verify that is Bet Changed In the Console for quick bet  =" + quickBetVal,
						"Is Bet Changed In the Console for quick bet  =" + quickBetVal,
						"Bet not Changed In the Console", "Fail");
				log.debug("isCreditDeducted :: Fail for quick bet value =" + quickBetVal);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

	}

	public boolean isBetChangedIntheConsole(String betValue) {
		String consoleBet = null;
		String bet = null;
		String bet1 = null;
		try {
			if (!GameName.contains("Scratch")) {
				log.debug("Bet value selected from game before = " + betValue);
				consoleBet = getConsoleText("return " + XpathMap.get("BetTextValue"));
				log.debug("Bet Refelecting on console after bet chnage from quickbet : " + consoleBet);
				bet1 = consoleBet.replaceAll("[a-zA-Z:]", "").trim();			
				bet = bet1.replaceFirst(":", "").trim();
			} // below else for Scratch game
			else {
				log.debug("Bet value selected from scrach game = " + betValue);
				consoleBet = getConsoleText("return " + XpathMap.get("BetTextValue"));
				// String bet = consoleBet.replaceAll("[a-zA-Z]", "").trim();
				bet1 = consoleBet.replaceAll("[a-zA-Z]", "").trim();
				bet = bet1.replaceFirst(":", "").trim();
				log.debug("Bet Refelecting on console after bet chnage from quickbet : " + consoleBet);
				System.out.println("Bet Refelecting on console after bet change from quickbet : " + consoleBet);
			}
		} catch (JavascriptException exception) {
			log.error("Exception occur while executing hook,Please verify thre hook of given component"
					+ exception.getMessage());
		}
		if (bet.contains(betValue)) {
			log.debug("selected bet " + betValue + " reflecting properly on console " + bet);
			return true;
		} else {
			log.debug("selected bet " + betValue + " Not reflecting properly on console " + bet);
			return false;
		}			

	}
	

	public boolean paytableScrollOfFive(Desktop_HTML_Report report, String currencyName) {
		
		String winBoosterXpath = "WinBooster";		
		String mysterySymbolXpath = "MysterySymbol";
		String WildXpath = "Wild";	
		String PaytableGridXpath = "PaytableGrid";
		String PaylineXpath = "Payline";		
		boolean test = false;
		try {
		
		
			report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "Pass", "" + currencyName);

			test = webdriver.findElements(By.xpath(XpathMap.get(winBoosterXpath))).size() > 0;
			if (test) {
				JavascriptExecutor js = (JavascriptExecutor) webdriver;
				WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(winBoosterXpath)));
				js.executeScript("arguments[0].scrollIntoView(true);", ele1);
				report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "Pass", "" + currencyName);
				Thread.sleep(3000);
				test = true;
			}

			test = webdriver.findElements(By.xpath(XpathMap.get(mysterySymbolXpath))).size() > 0;
			if (test) {
				JavascriptExecutor js = (JavascriptExecutor) webdriver;
				WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(mysterySymbolXpath)));
				js.executeScript("arguments[0].scrollIntoView(true);", ele1);
				report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "Pass", "" + currencyName);
				Thread.sleep(3000);
				test = true;
			}
			test = webdriver.findElements(By.xpath(XpathMap.get(WildXpath))).size() > 0;
		
			if (test) {
				JavascriptExecutor js = (JavascriptExecutor) webdriver;
				WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(WildXpath)));
				js.executeScript("arguments[0].scrollIntoView(true);", ele1);
				report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "Pass", "" + currencyName);
				Thread.sleep(3000);
				test = true;
			}

			test = webdriver.findElements(By.xpath(XpathMap.get(PaytableGridXpath))).size() > 0;
			if (test) {
				JavascriptExecutor js = (JavascriptExecutor) webdriver;
				WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(PaytableGridXpath)));
				js.executeScript("arguments[0].scrollIntoView(true);", ele1);
				report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "Pass", "" + currencyName);
				Thread.sleep(4000);
				test = true;
			}

			test = webdriver.findElements(By.xpath(XpathMap.get(PaylineXpath))).size() > 0;
			if (test) {
				JavascriptExecutor js = (JavascriptExecutor) webdriver;
				WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(PaylineXpath)));
				js.executeScript("arguments[0].scrollIntoView(true);", ele1);
				report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "Pass", "" + currencyName);
				Thread.sleep(3000);
				test = true;
			}
			Thread.sleep(2000);
			// method is for validating the payatable Branding
			textValidationForPaytableBranding(report, currencyName);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return test;
	}
	
	/**
	 * Verifies the Paytable text validation 
	 * 
	 */
	public String textValidationForPaytableBranding(Desktop_HTML_Report report,String currencyName) 
	{

		String PaytableBranding = null;
		Wait = new WebDriverWait(webdriver, 6000);
		try
		{
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("Payways"))));	
			boolean txt = webdriver.findElement(By.xpath(XpathMap.get("Payways"))).isDisplayed();
		if(txt)	
		{
			WebElement ele = webdriver.findElement(By.xpath(XpathMap.get("Payways")));
			PaytableBranding=ele.getText();
			System.out.println("actual  : " +PaytableBranding);
			if(PaytableBranding.equalsIgnoreCase(XpathMap.get("PaywaysTxt")))
			{
				System.out.println("Powered By MicroGaming Text : Pass");log.debug("Powered By MicroGaming Text : Pass");
				report.detailsAppendFolder("Paytable Branding ", "Branding Text ", ""+PaytableBranding, "Pass",""+currencyName);

			}
			else
			{
				System.out.println("Powered By MicroGaming Text : Fail");log.debug("Powered By MicroGaming Text : Fail");
				report.detailsAppendFolder("Paytable Branding ", "Branding Text ", ""+PaytableBranding, "Fail",""+currencyName);

			}}
		
		else
		{
			System.out.println("Powered By MicroGaming Text : Fail");
			report.detailsAppendFolder("Paytable", "Branding is not available ", ""+PaytableBranding, "Fail",""+currencyName);
		}
			
		}
		catch (Exception e) 
		{
			log.error(e.getMessage(), e);
		}
		return PaytableBranding;
		
		
	}

	/**
	 * method is for to scroll seven times
	 * 
	 * @param report
	 * @param currencyName
	 * @return
	 */
	public boolean paytableScrollOfNine(Desktop_HTML_Report report, String currencyName) {
		String winUpto = "WinUpTo"; 
		String wildXpath = "Wild";		
		String freeSpine = "FreeSpine";	
		String scatterXpath = "Scatter";	
		String symbolGridXpath = "PaytableGrid";
		String symbolGridXpath5 = "PaytableGrid5";	
		String paylines = "Payline";	
		String scroll1="scroll1";
		String scroll2="scroll2";
		String scroll3="scroll3";
		String scroll4="scroll4";
		String scroll5="scroll5";
		String scroll6="scroll6";
		String scroll7="scroll7";
		String scroll8="scroll8";
		
		boolean test = false;
		try {
						
			test = webdriver.findElements(By.xpath(XpathMap.get(winUpto))).size() > 0;
			if (test) {
				JavascriptExecutor js = (JavascriptExecutor) webdriver;
				WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(winUpto)));
				js.executeScript("arguments[0].scrollIntoView(true);", ele1);
				report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "Pass", "" + currencyName);
				Thread.sleep(1000);
				test = true;
			}

			test = webdriver.findElements(By.xpath(XpathMap.get(wildXpath))).size() > 0;
			if (test) {
				JavascriptExecutor js = (JavascriptExecutor) webdriver;
				WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(wildXpath)));
				js.executeScript("arguments[0].scrollIntoView(true);", ele1);
				report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "Pass", "" + currencyName);
				Thread.sleep(1000);
				test = true;
			}

			test = webdriver.findElements(By.xpath(XpathMap.get(freeSpine))).size() > 0;
			if (test) {
				JavascriptExecutor js = (JavascriptExecutor) webdriver;
				WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(freeSpine)));
				js.executeScript("arguments[0].scrollIntoView(true);", ele1);
				report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "Pass", "" + currencyName);
				Thread.sleep(1000);
				test = true;
			}
			test = webdriver.findElements(By.xpath(XpathMap.get(scatterXpath))).size() > 0;
			if (test) {
				JavascriptExecutor js = (JavascriptExecutor) webdriver;
				WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(scatterXpath)));
				js.executeScript("arguments[0].scrollIntoView(true);", ele1);
				report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "Pass", "" + currencyName);
				Thread.sleep(1000);
				test = true;
			}

			test = webdriver.findElements(By.xpath(XpathMap.get(symbolGridXpath))).size() > 0;
			if (test) {
				JavascriptExecutor js = (JavascriptExecutor) webdriver;
				WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(symbolGridXpath)));
				js.executeScript("arguments[0].scrollIntoView(true);", ele1);
				report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "Pass", "" + currencyName);
				Thread.sleep(1000);
				test = true;
			}

			test = webdriver.findElements(By.xpath(XpathMap.get(symbolGridXpath5))).size() > 0;
			if (test) {
				JavascriptExecutor js = (JavascriptExecutor) webdriver;
				WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(symbolGridXpath5)));
				js.executeScript("arguments[0].scrollIntoView(true);", ele1);
				report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "Pass", "" + currencyName);
				Thread.sleep(1000);
				test = true;
			}
			test = webdriver.findElements(By.xpath(XpathMap.get(paylines))).size() > 0;
			if (test) {
				JavascriptExecutor js = (JavascriptExecutor) webdriver;
				WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(paylines)));
				js.executeScript("arguments[0].scrollIntoView(true);", ele1);
				report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "Pass", "" + currencyName);
				Thread.sleep(1000);
				test = true;
			}
		
			test = webdriver.findElements(By.xpath(XpathMap.get(scroll1))).size() > 0;
			if (test) {
				JavascriptExecutor js = (JavascriptExecutor) webdriver;
				WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(scroll1)));
				js.executeScript("arguments[0].scrollIntoView(true);", ele1);
				report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "Pass", "" + currencyName);
				Thread.sleep(1000);
				test = true;
			}
			test = webdriver.findElements(By.xpath(XpathMap.get(scroll2))).size() > 0;
			if (test) {
				JavascriptExecutor js = (JavascriptExecutor) webdriver;
				WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(scroll2)));
				js.executeScript("arguments[0].scrollIntoView(true);", ele1);
				report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "Pass", "" + currencyName);
				Thread.sleep(1000);
				test = true;
			}
			test = webdriver.findElements(By.xpath(XpathMap.get(scroll3))).size() > 0;
			if (test) {
				JavascriptExecutor js = (JavascriptExecutor) webdriver;
				WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(scroll3)));
				js.executeScript("arguments[0].scrollIntoView(true);", ele1);
				report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "Pass", "" + currencyName);
				Thread.sleep(1000);
				test = true;
			}
			test = webdriver.findElements(By.xpath(XpathMap.get(scroll4))).size() > 0;
			if (test) {
				JavascriptExecutor js = (JavascriptExecutor) webdriver;
				WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(scroll4)));
				js.executeScript("arguments[0].scrollIntoView(true);", ele1);
				report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "Pass", "" + currencyName);
				Thread.sleep(1000);
				test = true;
			}
			test = webdriver.findElements(By.xpath(XpathMap.get(scroll5))).size() > 0;
			if (test) {
				JavascriptExecutor js = (JavascriptExecutor) webdriver;
				WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(scroll5)));
				js.executeScript("arguments[0].scrollIntoView(true);", ele1);
				report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "Pass", "" + currencyName);
				Thread.sleep(1000);
				test = true;
			}
			test = webdriver.findElements(By.xpath(XpathMap.get(scroll6))).size() > 0;
			if (test) {
				JavascriptExecutor js = (JavascriptExecutor) webdriver;
				WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(scroll6)));
				js.executeScript("arguments[0].scrollIntoView(true);", ele1);
				report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "Pass", "" + currencyName);
				Thread.sleep(1000);
				test = true;
			}
			test = webdriver.findElements(By.xpath(XpathMap.get(scroll7))).size() > 0;
			if (test) {
				JavascriptExecutor js = (JavascriptExecutor) webdriver;
				WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(scroll7)));
				js.executeScript("arguments[0].scrollIntoView(true);", ele1);
				report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "Pass", "" + currencyName);
				Thread.sleep(1000);
				test = true;
			}
		/*	test = webdriver.findElements(By.xpath(XpathMap.get(scroll8))).size() > 0;
			if (test) {
				JavascriptExecutor js = (JavascriptExecutor) webdriver;
				WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(scroll8)));
				js.executeScript("arguments[0].scrollIntoView(true);", ele1);
				report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "Pass", "" + currencyName);
				Thread.sleep(1000);
				test = true;
			}*/

			
	
			Thread.sleep(3000);		
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return test;
	}

	/**
	 * method is used to validate the Paytable Values
	 * 
	 * @return
	 */
	public boolean validatePayoutsFromPaytable(Desktop_HTML_Report report, String currencyName, String regExpr) 																												
	{
		boolean payoutvalues = false;
		try {
			if (XpathMap.get("paytableScrollOfNine").equalsIgnoreCase("yes")) {				
				payoutvalues = verifyRegularExpressionUsingArrays(report, regExpr,paytablePayoutsOfScatter(report, currencyName));
				return payoutvalues;
			}else if (XpathMap.get("paytableScrollOfFive").equalsIgnoreCase("yes")) {				
				payoutvalues = verifyRegularExpressionUsingArrays(report, regExpr,paytablePayoutsOfScatterWild(report, currencyName));
				return payoutvalues;
			} 
			else {
				System.out.println("Verify Paytable Payouts");
				log.debug("Verify Paytable Payouts");
			}
			Thread.sleep(2000);			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return payoutvalues;
	}

	/**
	 * method is used to validate the Paytable Values
	 * 
	 * @return
	 */
	public String[] paytablePayoutsOfScatter(Desktop_HTML_Report report, String currencyName) // String[] array
	{
		String symbols[] = { "Scatter5", "Scatter4", "Scatter3","Scatter2"};
		//String symbols[] = { "Scatter5"};
		
		try {
			System.out.println("Paytable Validation for  Scatter ");
			log.debug("Paytable Validation for Scatter ");
			for (int i = 0; i < symbols.length; i++) {
				symbols[i] = func_GetText1(symbols[i]);
				System.out.println(symbols[i]);
				log.debug(symbols[i]);
			}
			Thread.sleep(3000);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return symbols;
	}

	
	/**
	 * method is used to validate the Paytable Values
	 * 
	 * @return
	 */
	public String[] paytablePayoutsOfScatterWild(Desktop_HTML_Report report, String currencyName) // String[] array
	{
		String symbols[] = { "Scatter5", "Scatter4", "Scatter3","Scatter2","Wild5","Wild4","Wild3","Wild2"};
		try {
			System.out.println("Paytable Validation for  Scatter ");
			log.debug("Paytable Validation for Scatter ");
			for (int i = 0; i < symbols.length; i++) {
				symbols[i] = func_GetText1(symbols[i]);
				System.out.println(symbols[i]);
				log.debug(symbols[i]);
			}
			Thread.sleep(3000);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return symbols;
	}
	/**
	 * Verifies the Currency Format - using String method
	 */

	public boolean verifyRegularExpressionUsingArrays(Desktop_HTML_Report report, String regExp, String[] method) {
		String[] Text = method;
		boolean regexp = false;
		try {
			Thread.sleep(2000);
			for (int i = 0; i < Text.length; i++) {
				if (Text[i].matches(regExp)) {

					log.debug("Compared with Reg Expression .Currency is same");
					regexp = true;
				} else {

					log.debug("Compared with Reg Expression .Currency is different");
					regexp = false;
				}
				Thread.sleep(2000);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return regexp;
	}

	/**
	 * Check Availablity of an Element
	 * 
	 * @param string
	 * @return
	 */
	public boolean checkAvilabilityofElement(String hooksofcomponent) {
		boolean isComponentAvilable = true;
		Wait = new WebDriverWait(webdriver, 10000);
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get(hooksofcomponent))));
			WebElement ele = webdriver.findElement(By.xpath(XpathMap.get(hooksofcomponent)));
			ele.isDisplayed();
			if (ele != null) {
				isComponentAvilable = true;
			}
		} catch (Exception e) {
			// if component in the game not avilable in it while give an exception
			isComponentAvilable = false;
		}
		return isComponentAvilable;
	}

	/**
	 * method verifies the bonus summary screen and get Text
	 */
	public String freeSpinsSummaryScreen(Desktop_HTML_Report report, String currencyName) {
		String fsSummaryScreen = null;
		Wait = new WebDriverWait(webdriver, 350);
		try {
			fsSummaryScreen = func_GetTextbyAttribute(report, "FSSummaryScreenWinAmt", currencyName);
			report.detailsAppendFolderOnlyScreenShot(currencyName);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return fsSummaryScreen;
	}

	/**
	 * this methods gets an attribute values
	 */
	public String func_GetTextbyAttribute(Desktop_HTML_Report report, String locator, String currencyName) {
		try {
			WebElement ele = webdriver.findElement(By.xpath(XpathMap.get(locator)));
			ele.getAttribute("textvalue");
			System.out.println("Text is : " + ele.getAttribute("textvalue"));
			log.debug("Amount is :" + ele.getAttribute("textvalue"));
			// report.detailsAppendFolder("Text", " Win Amt ",
			// ""+ele.getAttribute("textvalue"), "Pass",currencyName);
			return ele.getText();

		} catch (NoSuchElementException e) {
			return null;
		}
	}

	public void openBetPanel() {

		// Opening bet panel
		ClickByCoordinates("return " + XpathMap.get("BetMenuCoordinatex"),	"return " + XpathMap.get("BetMenuCoordinatey"));

	}

	public void clickAtButton(String button) {
		try {
			JavascriptExecutor js = ((JavascriptExecutor) webdriver);
			js.executeScript(button);
		} catch (JavascriptException e) {
			log.error("EXeception while executon ", e);
			log.error(e.getCause());
		}
	}

	/**
	 * Verifies the Currency Format - using String method
	 */

	public boolean verifyRegularExpression(Desktop_HTML_Report report, String regExp, String method) {
		String Text = method;
		boolean regexp = false;
		try {
			Thread.sleep(2000);
			String Text1=Text.replaceAll("[a-zA-Z:]", "").trim();
			//System.out.println(Text1);
			//Text1=Text1.replaceAll(" ", "").trim();	
			if (Text1.matches(regExp)) {
				log.debug("Compared with Reg Expression.Currency is in correct format: "+Text1);
				regexp = true;
			}
			Thread.sleep(2000);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return regexp;
	}
	
	
	public boolean verifyGridPayouts(Desktop_HTML_Report report, String regExp,String currencyName) {		

		boolean result=false;
		int trueCount=0;
		try
		{
			String gridSize=XpathMap.get("gridCount");	
			Double count=Double.parseDouble(gridSize);
			int gridCount=count.intValue();		
			for(int i=1; i<=gridCount; i++) 
			{			
				String gridEle ="GridPay"+i+"";		
				String gridValue = func_GetText1(gridEle);
				boolean gridVl = verifyRegularExpression(report, regExp, gridValue);
				
				if(gridVl) 
				{
					trueCount++;
				}
			}
			
			if(trueCount==gridCount)
			{
				result=true;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
		return result;
		
	
	}
	
	   public boolean waitForElement(String hook)
	    {    
	        boolean result=false;
	        long startTime = System.currentTimeMillis();
	        try{
	            log.debug("Waiting for Element before next click");
	            while(true)
	            {
	            	Boolean ele = GetConsoleBooleanText("return "+XpathMap.get(hook));
	                if(ele)
	                {
	                    log.info(hook+" value= "+ele);
	                    result=true;        
	                    System.out.println(hook+" value= "+ele);
	                    break;
	                }
	                else
	                {
	                    long currentime = System.currentTimeMillis();
	                    if(((currentime - startTime) / 1000) > 180)
	                    {
	                    log.info("No value present after 3 mins= "+ele);
	                    System.out.println("No value present after 3 mins "+ele);
	                    result=false;
	                    break;            
	                    }
	               }
	                
	            }        
	        }
	        catch(Exception e)
	        {
	            log.error("error while waiting for hook ",e);
	        }
	        return result;
	    }
		
		public String getCurrentFSWinAmt(Desktop_HTML_Report report,String FSwinamtVisible,String FSwinAmt,String currencyName ) 
		{
			String FSWinAmt = null;
			
			try
			{	
		    report.detailsAppendFolderOnlyScreenShot(currencyName);
		   waitForElement(FSwinamtVisible);
		   boolean isFSWinAmt =	GetConsoleBooleanText(FSwinamtVisible);
			if(isFSWinAmt)
			{	
			FSWinAmt = func_GetText(FSwinAmt);
			System.out.println(" Win Amount is "+FSWinAmt);
			log.debug(" Win Amount is "+FSWinAmt);
			}
			else
			{
				System.out.println("There is no Win ");
				log.debug("There is no Win ");
			}}
			catch (Exception e) 
			{
				log.error(e.getMessage(), e);
			}
			return FSWinAmt;
		}
		
		public void SpinUntilFGSummary(String FGSummaryBackToGameBtn, ImageLibrary imageLibrary)
		{
			try
			{			
				//while(isElementVisible(FGSummaryBackToGameBtn)==false)
					for(int i=0;i<4;i++)
				{
					webdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
					imageLibrary.click("Spin");
					Thread.sleep(20000);
				}
				//}
			}
			catch(Exception e)
			{
				log.error(e.getMessage(), e);
			}
		}
		
		public boolean validatePayouts(Desktop_HTML_Report report, String currencyName, String regExpr) 																												
		{
			boolean payoutvalues = false;
			try {
				if (XpathMap.get("PaytablePayoutsofNine").equalsIgnoreCase("yes")) {
					
					payoutvalues = verifyRegularExpressionUsingArrays(report, regExpr,payOuts(report, currencyName));
					return payoutvalues;
				} else {
					System.out.println("Verify Paytable Payouts");
					log.debug("Verify Paytable Payouts");
				}
				Thread.sleep(2000);			
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			return payoutvalues;
		}
		
		public String[] payOuts(Desktop_HTML_Report report, String currencyName){
			String symbols[] = { "payout1", "payout2", "payout3", "payout4", "payout5", "payout6", "payout7"};				
			try {
				for (int i = 0; i < symbols.length; i++) {
					String value= Func_GetText(symbols[i]);
					//trim until the currency symbol symbol 
					int index=value.lastIndexOf(" ");
					System.out.println("Information Text is at the Index of space is "+index);log.debug("Information Text is at the Index of space is "+index);
					if(index>0)
					{
						value=value.substring(index+1,value.length());					
						symbols[i]=value.trim();
						System.out.println("Info Text "+symbols[i]);log.debug("payout value "+symbols[i]);						
					}
					
					log.debug("Payout1 "+symbols[i]);
				}
				Thread.sleep(3000);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			return symbols;
		}
		
		public String freeGameOnRefresh(Desktop_HTML_Report report,String currencyName,String isResumeButtonVisible, String infoBtn, String infoBtnx,String infoBtny, String infoTxt, ImageLibrary imageLibrary ) 
		{
			String onRefresh = null;
			try 
			{
				report.detailsAppendFolder("Free Game", "Refresh", "Refresh", "Pass",currencyName);
				boolean isResumeBtnVisible = waitForElement(isResumeButtonVisible);
				if(isResumeBtnVisible) 
				{
					report.detailsAppendFolder("Free Games on Refresh", "Resume Button", "Resume Button", "Pass",currencyName); 
					Thread.sleep(5000);
					//validate the Entry Screen Info Text 	
					onRefresh= freeGameEntryInfo(report,currencyName,imageLibrary,"FGinfoTxt");
					
					//onRefresh =	freeGameEntryInfo(report,currencyName, infoBtn,"return "+XpathMap.get(infoBtnx),"return "+XpathMap.get(infoBtny),"return "+XpathMap.get(infoTxt));
					//Click on Resume Button on Refresh 
					//func_Click("ResumeButton");
					
					
			    } 
				else 
				{
					log.debug("Free Games Resume Button Text is : - Fail");
					System.out.println("Free Games Resume Button Text is : - Fail");
				}
			}
			catch (Exception e)
			{
				log.error(e.getMessage(), e);
			}
			return onRefresh ;
		}	
		
		// get console Text
		public String GetConsoleText(String text)
		{
			String consoleText = null;
			try {
				JavascriptExecutor js = ((JavascriptExecutor) webdriver);
				consoleText = (String) js.executeScript(text);
				log.debug("Text Read from Console="+consoleText);
				System.out.println("Text Read from Console="+consoleText);
				} catch (Exception e) 
				{
					e.getMessage();
				}
				return consoleText;
		}
		/*
		 * Method to click FG info btn and return FGinfo
		 */
		public String freeGameEntryInfo(Desktop_HTML_Report report,String currencyName,ImageLibrary imageLibrary, String fgInfotxt )  
		{
			//ClickByCoordinates("return " +XpathMap.get(fgInfoBTNx), "return " +XpathMap.get(fgInfoBTNy));
			imageLibrary.click("FGInfoIcon");
			String infoText="";
			String text=GetConsoleText("return " +XpathMap.get(fgInfotxt));
			System.out.println(text);
			log.debug(text);
			//trim until the @ symbol 
			int index=text.lastIndexOf("@");
			if(index>0)
			{
				text=text.substring(index+1,text.length());					
				infoText=text.trim();
				System.out.println(infoText);log.debug(infoText);
				//report.detailsAppendFolder("Free Games","Info Text","Info Text", "Pass", currencyName);
			}	
			return infoText;
		}
		
		/**
		 * method is for free game Information Icon
		 */
			public String freeGameEntryInfo(Desktop_HTML_Report report,String currencyName,String infoBtn,String infoBtnx,String infoBtny, String infoTxt) 
			{
				String infoText = "";
				try
				{
				
					ClickByCoordinates(infoBtnx, infoBtny);
					Thread.sleep(2000);
					report.detailsAppendFolder("Free Games","Info Button Click ","Info Button Click ", "Pass", currencyName);
					Thread.sleep(2000);
					String text = getConsoleText(infoTxt);
					log.debug(text);
					//trim until the @ symbol 
					int index=text.lastIndexOf("@");
					if(index>0)
					{
						text=text.substring(index+1,text.length());					
						infoText=text.trim();
						System.out.println(infoText);log.debug(infoText);
						//report.detailsAppendFolder("Free Games","Info Text","Info Text", "Pass", currencyName);
					}	
					
				} catch (Exception e) 
				{
					e.printStackTrace();
				}
				return infoText;
			}
			
			public void ClickByCoordinatesWithAdjust(String cx, String cy, int x, int y) {
				Wait = new WebDriverWait(webdriver, 50);
				// boolean visible = false;
				int dx = 0, dy = 0, dw = 0, dh = 0;
				JavascriptExecutor js = ((JavascriptExecutor) webdriver);

				System.out.println(js.executeScript(cx));
				System.out.println(js.executeScript(cy));
				try {
					Long sx = (Long) js.executeScript(cx);
					dx = sx.intValue()+(x);
				} catch (Exception e) {
					Double sx = (Double) js.executeScript(cx);
					dx = sx.intValue()+(y);
				}
				try {
					Long sy = (Long) js.executeScript(cy);
					dy = sy.intValue()+(x);
				} catch (Exception e) {
					Double sy = (Double) js.executeScript(cy);
					dy = sy.intValue()+(y);
				}

				if (dx == 0) {
					dx = 1;

				}
				if (dy == 0) {
					dy = 1;
				}

				long bodyHeight = webdriver.findElement(By.id("gameCanvas")).getRect().getHeight();
				long bodyWidth = webdriver.findElement(By.id("gameCanvas")).getRect().getWidth();
				int topLeftX = (int) bodyWidth / 2;
				int topLeftY = (int) bodyHeight / 2;
				Actions actions = new Actions(webdriver);
				actions.moveToElement(webdriver.findElement(By.id("gameCanvas")), 0, 0);
				actions.moveToElement(webdriver.findElement(By.id("gameCanvas")), -topLeftX, -topLeftY);
				log.debug("topLeftX: " + topLeftX + " topLeftY: " + topLeftY);
				System.out.println(dx + "  "+dy);
				actions.moveByOffset(dx, dy).click().perform();		

			}
			
			/**
			 * Verifies the FreeSpine Big Win
			 * 
			 */
			public String verifyFreeSpinBigWin(Desktop_HTML_Report report, String currencyName) {
				String bigWinAmt = null;
				Wait = new WebDriverWait(webdriver, 3000);

				try {
					boolean isBigWin = isElementVisible("FSBigWin");
					if (isBigWin) {
						Thread.sleep(8000);				
						bigWinAmt = getConsoleText("return " + XpathMap.get("FSBigWinValue"));
						System.out.println("FreeSpine Big Win Amount is " + bigWinAmt);
						log.debug(" Big Win Amount is " + bigWinAmt);
						report.detailsAppendFolderOnlyScreenShot(currencyName);

					} else {
						System.out.println("There is no Big Win ");
						log.debug("There is no Big Win");
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
				return bigWinAmt;

			}
	
			/**
			 * Verifies the current Win amt
			 * 
			 */
			public String verifyFSCurrentWinAmt(Desktop_HTML_Report report, String currencyName) {
				String winAmt = null;
				Wait = new WebDriverWait(webdriver, 250);
				try {
					report.detailsAppendFolderOnlyScreenShot(currencyName);

					boolean isWinAmt = isElementVisible("isFSNormalWin");
					if (isWinAmt) {
						winAmt = getConsoleText("return " + XpathMap.get("FSNormalWinValue"));
						System.out.println("FreeSpin current Win Amount is " + winAmt);
						log.debug(" Win Amount is " + winAmt);
					} else {
						System.out.println("There is no Win ");
						log.debug("There is no Win ");
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
				return winAmt;
			}
			
			/**
			 * method is to verify the Free Games Assignment 
			 */
			public boolean assignFreeGames(String userName,String offerExpirationUtcDate,int mid, int cid,int noOfOffers,int defaultNoOfFreeGames) 
			{
				//assign free games to above created user
				boolean isFreeGameAssigned=false;
				try {
				String balanceTypeId=XpathMap.get("BalanceTypeID");
				Double dblBalanceTypeID=Double.parseDouble(balanceTypeId);
				balanceTypeId=""+dblBalanceTypeID.intValue()+"";
				log.debug(balanceTypeId);
				//Assign free games offers to user 
				if(TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
				{

					isFreeGameAssigned=addFreeGameToUserInBluemesa( userName,defaultNoOfFreeGames ,  offerExpirationUtcDate,  balanceTypeId,  mid, cid,noOfOffers);
				}
				else
				{
					isFreeGameAssigned=addFreeGameToUserInAxiom(userName,defaultNoOfFreeGames,offerExpirationUtcDate,balanceTypeId,mid,cid,noOfOffers);
				}
				}catch (Exception e)
				{
					log.error(e.getMessage(), e);
				}
				return isFreeGameAssigned;
			}
			
			
			/// FG Bigwin
			
			/**
			 * Verifies Free Game the Big Win
			 * 
			 */	
				
			public String verifyFGBigWin(Desktop_HTML_Report report,ImageLibrary imageLibrary) {
				String bigWinAmt = null;
				Wait = new WebDriverWait(webdriver, 30000);

				try {
					bigWinAmt = getConsoleText("return " + XpathMap.get("TotalWinAmt"));
					System.out.println(" Win Amount is " + bigWinAmt);
					//log.debug(" Win Amount is " + bigWinAmt);
					
					/*boolean isBigWin = isElementVisible("isBigWinPresent");
					if (isBigWin) {
						Thread.sleep(8000);				
						bigWinAmt = getConsoleText("return " + XpathMap.get("BigWinAmt"));
						System.out.println("FreeGame Big Win Amount is " + bigWinAmt);
						log.debug("Free Game Big Win Amount is " + bigWinAmt);
						while(bigWinAmt.equals(""))
						{					
							imageLibrary.click("Spin");	
							Thread.sleep(7000);
							bigWinAmt = getConsoleText("return " + XpathMap.get("TotalWinAmt"));
						}
					//	report.detailsAppendFolderOnlyScreenShot(currencyName);

					} else {
						System.out.println("There is no Big Win ");
						log.debug("There is no Big Win");
					}*/
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
				return bigWinAmt;

			}
			
			/**
			 * Verifies the Free Game current Win amt
			 * 
			 */
			public String getFGCurrentWinAmt(Desktop_HTML_Report report,String SpinBtnCoordinatex ,String SpinBtnCoordinatey) {
				String winAmt = null;
				Wait = new WebDriverWait(webdriver, 250);
				try {
						winAmt = getConsoleText("return " + XpathMap.get("winAmt"));
						while(winAmt.equals(""))
						{					
							ClickByCoordinates("return "+XpathMap.get(SpinBtnCoordinatex),"return "+XpathMap.get(SpinBtnCoordinatey));					
							Thread.sleep(2000);
							winAmt = getConsoleText("return " + XpathMap.get("winAmt"));
						}
						System.out.println(" Win Amount is " + winAmt);
						log.debug(" Win Amount is " + winAmt);
				
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
				return winAmt;
			}
			
			/**
			 * Verifies the FreeSpine Bonus WIn
			 * 
			 */
			public String verifyFreeSpinBonusWin(Desktop_HTML_Report report, String currencyName) {
				String bigWinAmt = null;
				Wait = new WebDriverWait(webdriver, 3000);

				try {
					boolean isBigWin = isElementVisible("FSBigWin");
					if (isBigWin) {
						Thread.sleep(8000);				
						bigWinAmt = getConsoleText("return " + XpathMap.get("FSBonusWin"));
						System.out.println("FreeSpine Bonus Win Amount is " + bigWinAmt);
						log.debug(" Bonus win Amount is " + bigWinAmt);
						report.detailsAppendFolderOnlyScreenShot(currencyName);

					} else {
						System.out.println("There is no bonus Win ");
						log.debug("There is no Bonus Win");
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
				return bigWinAmt;

			}
			
			/**
			 * Verifies the current Win amt
			 * VC66297
			 * 
			 */
			public String getCurrentWinAmt(Desktop_HTML_Report report, String currencyName) {
				String winAmt = null;
				Wait = new WebDriverWait(webdriver, 250);
				try {
					report.detailsAppendFolderOnlyScreenShot(currencyName);
					winAmt = getConsoleText("return " + XpathMap.get("TotalWinAmt"));
					while(winAmt.equals(""))
					{					
											
						Thread.sleep(2000);
						winAmt = getConsoleText("return " + XpathMap.get("TotalWinAmt"));
						
					}
					
					
					/*boolean isWinAmt = isElementVisible("isTotalWinAmt");
					if (isWinAmt) {
						winAmt = getConsoleText("return " + XpathMap.get("TotalWinAmt"));
						System.out.println(" Win Amount is " + winAmt);
						log.debug(" Win Amount is " + winAmt);
					} else {
						System.out.println("There is no Win ");
						log.debug("There is no Win ");
					}*/
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
				return winAmt;
			}
			
			/**
			 * Verifies the Big Win
			 * VC66297
			 */
			public String verifyBigWin(Desktop_HTML_Report report, String currencyName) {
				String bigWinAmt = null;
				Wait = new WebDriverWait(webdriver, 30000);

				try {
					boolean isBigWin = isElementVisible("BigWin");
					if (isBigWin) {
						Thread.sleep(8000);				
						bigWinAmt = getConsoleText("return " + XpathMap.get("BigWinValue"));
						System.out.println("Big Win Amount is " + bigWinAmt);
						log.debug(" Big Win Amount is " + bigWinAmt);
						report.detailsAppendFolderOnlyScreenShot(currencyName);

					} else {
						System.out.println("There is no Big Win ");
						log.debug("There is no Big Win");
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
				return bigWinAmt;

			}
			/**
			 * Verifies the Amazing win
			 * VC66297
			 * 
			 */

			public String verifyAmgWin(Desktop_HTML_Report report, String currencyName) {
				String amgWinAmt = null;
				Wait = new WebDriverWait(webdriver, 30000);

				try {
					boolean isAmgWin = isElementVisible("isAmgWinAmt");
					if (isAmgWin) {
						Thread.sleep(8000);			
						amgWinAmt = getConsoleText("return " + XpathMap.get("AmgWinAmt"));
						System.out.println("Amzing Win Amount is " + amgWinAmt);
						log.debug(" Big Win Amount is " + amgWinAmt);
						report.detailsAppendFolderOnlyScreenShot(currencyName);

					} else {
						System.out.println("There is no Big Win ");
						log.debug("There is no Big Win");
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
				return amgWinAmt;

			}
			

			/**
			 * Verifies the current Win amt for workaround
			 * VC66297
			 */
			public String getCurrentWinAmt1(Desktop_HTML_Report report, String currencyName) {
				String winAmt = null;
				Wait = new WebDriverWait(webdriver, 250);
				try {
					report.detailsAppendFolderOnlyScreenShot(currencyName);
					winAmt = getConsoleText("return " + XpathMap.get("TotalWinAmt"));
					System.out.println(" Win Amount is " + winAmt);
					log.debug(" Win Amount is " + winAmt);
					
					/*boolean isWinAmt = isElementVisible("isTotalWinAmt");
					if (isWinAmt) {
						
						winAmt = getConsoleText("return " + XpathMap.get("TotalWinAmt"));
						while(winAmt.equals(""))
						{					
												
							Thread.sleep(2000);
							winAmt = getConsoleText("return " + XpathMap.get("TotalWinAmt"));
						}
						System.out.println(" Win Amount is " + winAmt);
						log.debug(" Win Amount is " + winAmt);
					} else {
						System.out.println("There is no Win ");
						log.debug("There is no Win ");
					}*/
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
				return winAmt;
			}	
		
			/**
			 * Verifies the Big Win for workaround
			 * VC66297
			 */
			public String verifyBigWin(Desktop_HTML_Report report, String currencyName,String SpinBtnCoordinatex ,String SpinBtnCoordinatey) {
				String bigWinAmt = null;
				Wait = new WebDriverWait(webdriver, 30000);

				try {
					boolean isBigWin = isElementVisible("BigWin");
					if (isBigWin) {
						Thread.sleep(8000);				
						bigWinAmt = getConsoleText("return " + XpathMap.get("BigWinValue"));
						System.out.println("Big Win Amount is " + bigWinAmt);
						log.debug(" Big Win Amount is " + bigWinAmt);
						while(bigWinAmt.equals(""))
						{					
							ClickByCoordinates("return "+XpathMap.get(SpinBtnCoordinatex),"return "+XpathMap.get(SpinBtnCoordinatey));					
							Thread.sleep(7000);
							bigWinAmt = getConsoleText("return " + XpathMap.get("TotalWinAmt"));
						}
					//	report.detailsAppendFolderOnlyScreenShot(currencyName);

					} else {
						System.out.println("There is no Big Win ");
						log.debug("There is no Big Win");
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
				return bigWinAmt;

			}
			
			/**
			 * Verifies the Amazing for workaround
			 * VC66297
			 */
			
			public String verifyAmgWin(Desktop_HTML_Report report, String currencyName,String SpinBtnCoordinatex ,String SpinBtnCoordinatey) {
				String amgWinAmt = null;
				Wait = new WebDriverWait(webdriver, 30000);

				try {
					boolean isAmgWin = isElementVisible("isAmgWinAmt");
					if (isAmgWin) {
						Thread.sleep(8000);			
						amgWinAmt = getConsoleText("return " + XpathMap.get("AmgWinAmt"));
						while(amgWinAmt.equals(""))
						{					
							ClickByCoordinates("return "+XpathMap.get(SpinBtnCoordinatex),"return "+XpathMap.get(SpinBtnCoordinatey));					
							Thread.sleep(7000);
							amgWinAmt = getConsoleText("return " + XpathMap.get("AmgWinAmt"));
						}
						System.out.println("Amzing Win Amount is " + amgWinAmt);
						log.debug(" Big Win Amount is " + amgWinAmt);
						report.detailsAppendFolderOnlyScreenShot(currencyName);

					} else {
						System.out.println("There is no Big Win ");
						log.debug("There is no Big Win");
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
				return amgWinAmt;

			}
			
			// navigate back to base game from different window and verify
		    public void navigate_back(Desktop_HTML_Report report, String currencyName) throws InterruptedException {
		        Thread.sleep(1000);
		        String parentWindow = webdriver.getWindowHandle();
		        Set<String> handles = webdriver.getWindowHandles();
		        for (String windowHandle : handles) {
		            if (!windowHandle.equals(parentWindow)) {
		                webdriver.switchTo().window(windowHandle);
		                // Perform your operation here for new window
		                Thread.sleep(30000);
		                String title = webdriver.getTitle();
		                log.debug(getPageTitle(webdriver));
		                report.detailsAppendFolder("Verify if help navigation ", "Help should navigated", "Help is navigated", "Pass",currencyName);



		               Thread.sleep(2000);
		                webdriver.close(); // closing child window
		                webdriver.switchTo().window(parentWindow);
		                log.debug("NAvigated to base game");



		           }
		        }
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
				boolean val=true;
				String consoleText=null;
				try {
					Thread.sleep(2000);
					if (XpathMap.get(hasHook).equalsIgnoreCase("Yes")) 
					{
						String text = "return " + XpathMap.get(actualText);
						consoleText = getConsoleText(text);
					} 
					else
					{
						consoleText=fun_GetText(actualText);
					}
					
					System.out.println("Expected text -- " + expectedText);
					log.debug("Expected text -- " + expectedText);
					System.out.println("Actual text-- " + consoleText);
					log.debug("Actual text-- " + consoleText);
					
					if (consoleText.equals(expectedText)) 
					{
						val = true;
						log.debug(consoleText+" string is correct");
					}	
			} catch (Exception e) 
			{
				log.debug("unable to compare",e);
				e.getMessage();
			}
				return val;
			}
			
			public void verifyquickbetImg(Desktop_HTML_Report report, String currencyName ,String regExpr, ImageLibrary imageLibrary) {
				String betVal ;
				// check if max bet button is visible to know whether bet panel is open
				try
				{	
			    boolean isMaxBet = imageLibrary.isImageAppears("BetMax");
			    if(isMaxBet) {
					report.detailsAppendFolder("Verify Max Bet is visible "," Max Bet is visibled", "Max Bet is visible ","Pass",currencyName);		
					Thread.sleep(3000);	
					imageLibrary.click("BetMax");
					Thread.sleep(3000);	
					report.detailsAppendFolder("Verify Max Bet is Clicked "," Max Bet is Clicked", "Max Bet isClicked ","Pass",currencyName);									
					boolean betAmt = verifyRegularExpression(report, regExpr, getConsoleText("return "+ XpathMap.get("BetTextValue")));
						
				
				}
/*				Thread.sleep(3000);
				
					boolean bet1 = isElementVisible("isQuickBet1");
					if(bet1) {
						betVal = getConsoleText("return " + XpathMap.get("QuickBet1Value"));	
						//report.detailsAppendFolder("Verify Bet1 is visible "," Bet 1 is visibled", "Bet 1 is visible ","Pass", currencyName);									
						ClickByCoordinates("return " + XpathMap.get("QuickBet1Coordinatex"),"return " + XpathMap.get("QuickBet1Coordinatey"));
						Thread.sleep(1000);
						report.detailsAppendFolder("Verify Bet 1 is Clicked "," Bet 1 is Clicked", "Bet 1 isClicked ","Pass" , currencyName);							
						
						boolean isBetChangedIntheConsole = isBetChangedIntheConsole(betVal);
						verifyBetValuesAppended(isBetChangedIntheConsole ,betVal ,report,currencyName);
						imageLibrary.click("BetButton");
						Thread.sleep(2000);
						
						
					}
					boolean bet2 = isElementVisible("isQuickBet2");			
		            if(bet2) {
		            	betVal = getConsoleText("return " + XpathMap.get("QuickBet2Value"));	
		            	//report.detailsAppendFolder("Verify Bet2 is visible "," Bet 2 is visibled", "Bet 2 is visible ","Pass", currencyName);									
						ClickByCoordinates("return " + XpathMap.get("QuickBet2Coordinatex"),"return " + XpathMap.get("QuickBet2Coordinatey"));
						Thread.sleep(1000);
						report.detailsAppendFolder("Verify Bet 2 is Clicked "," Bet 2 is Clicked", "Bet2 isClicked ","Pass" , currencyName);
						
						boolean isBetChangedIntheConsole = isBetChangedIntheConsole(betVal);
						verifyBetValuesAppended(isBetChangedIntheConsole ,betVal ,report,currencyName);			
						imageLibrary.click("BetButton");
						Thread.sleep(2000);		
					}
		        	boolean bet3 = isElementVisible("isQuickBet3");
		            if(bet3) {
		            	betVal = getConsoleText("return " + XpathMap.get("QuickBet3Value"));			
		            	//report.detailsAppendFolder("Verify Bet3 is visible "," Bet 3 is visibled", "Bet 3 is visible ","Pass", currencyName);									
						ClickByCoordinates("return " + XpathMap.get("QuickBet3Coordinatex"),"return " + XpathMap.get("QuickBet3Coordinatey"));
						Thread.sleep(1000);
						report.detailsAppendFolder("Verify Bet 3 is Clicked "," Bet 3 is Clicked", "Bet 3 isClicked ","Pass" , currencyName);	
						
						boolean isBetChangedIntheConsole = isBetChangedIntheConsole(betVal);
						verifyBetValuesAppended(isBetChangedIntheConsole ,betVal ,report,currencyName);				
						imageLibrary.click("BetButton");
						Thread.sleep(2000);			
						
					}
		        	boolean bet4 = isElementVisible("isQuickBet4");
		            if(bet4) {
		            	betVal = getConsoleText("return " + XpathMap.get("QuickBet4Value"));	
		            	//report.detailsAppendFolder("Verify Bet4 is visible "," Bet 4 is visible", "Bet 4 is visible ","Pass" , currencyName);									
						ClickByCoordinates("return " + XpathMap.get("QuickBet4Coordinatex"),"return " + XpathMap.get("QuickBet4Coordinatey"));
						Thread.sleep(1000);
						report.detailsAppendFolder("Verify Bet 4 is Clicked "," Bet 4 is Clicked", "Bet 4 isClicked ","Pass" , currencyName);	
						
						boolean isBetChangedIntheConsole = isBetChangedIntheConsole(betVal);
						verifyBetValuesAppended(isBetChangedIntheConsole ,betVal ,report,currencyName);				
						imageLibrary.click("BetButton");
						Thread.sleep(2000);			
						
					}	
		            
		            boolean bet5 = isElementVisible("isQuickBet5");
		            if(bet5) {
		            	betVal = getConsoleText("return " + XpathMap.get("QuickBet5Value"));	
		            	//.detailsAppendFolder("Verify Bet 5 is visible "," Bet 5 is visibled", "Bet 5 is visible ","Pass" , currencyName);									
						ClickByCoordinates("return " + XpathMap.get("QuickBet5Coordinatex"),"return " + XpathMap.get("QuickBet5Coordinatey"));
						Thread.sleep(1000);
						report.detailsAppendFolder("Verify Bet 5 is Clicked "," Bet 5 is Clicked", "Bet 5 is Clicked ","Pass" , currencyName);	
						
						boolean isBetChangedIntheConsole = isBetChangedIntheConsole(betVal);
						verifyBetValuesAppended(isBetChangedIntheConsole ,betVal ,report,currencyName);				
						imageLibrary.click("BetButton");
						Thread.sleep(2000);			
						
					}					

		            boolean bet6 = isElementVisible("isQuickBet6");
		            if(bet6) {
		            	betVal = getConsoleText("return " + XpathMap.get("QuickBet6Value"));	
		            	//report.detailsAppendFolder("Verify Bet 6 is visible "," Bet 6 is visibled", "Bet 6 is visible ","Pass" , currencyName);									
						ClickByCoordinates("return " + XpathMap.get("QuickBet6Coordinatex"),"return " + XpathMap.get("QuickBet6Coordinatey"));
						Thread.sleep(1000);
						report.detailsAppendFolder("Verify Bet 6 is Clicked "," Bet 6 is Clicked", "Bet 6 is Clicked ","Pass" , currencyName);	
						
						boolean isBetChangedIntheConsole = isBetChangedIntheConsole(betVal);
						verifyBetValuesAppended(isBetChangedIntheConsole ,betVal ,report,currencyName);									
						Thread.sleep(2000);			
						
					}					
*/				
				} catch (Exception e) 
		        {
			        log.error(e.getMessage(), e);
		        }
			
		}
			
			/*	**
	            * Verifies Free Game the Big Win
	            *
	            */    
	               
	           public String verifyBigWin(Desktop_HTML_Report report,ImageLibrary imageLibrary) {
	               String bigWinAmt = null;
	               
	              try {
	            	  bigWinAmt = getConsoleText("return " + XpathMap.get("BigWinAmt"));
	            	  while(bigWinAmt.equals(""))
                      {                    
                          imageLibrary.click("Spin");    
                          Thread.sleep(10000);
                          bigWinAmt = getConsoleText("return " + XpathMap.get("BigWinAmt"));
                         
                      }
	            	  
	            	  
	            	  
	                   /*boolean isBigWin = isElementVisible("isBigWinPresent");
	                   if (isBigWin) {
	                       Thread.sleep(8000);                
	                      
	                       System.out.println("Base Game Big Win Amount is " + bigWinAmt);
	                       log.debug("Base Game Big Win Amount is " + bigWinAmt);
	                       while(bigWinAmt.equals(""))
	                       {                    
	                           imageLibrary.click("Spin");    
	                           Thread.sleep(7000);
	                           bigWinAmt = getConsoleText("return " + XpathMap.get("TotalWinAmt"));
	                       }*/
	                   //    report.detailsAppendFolderOnlyScreenShot(currencyName);
	                  /*} else {
	                       System.out.println("There is no Big Win ");
	                       log.debug("There is no Big Win");
	                   }*/
	               } catch (Exception e) {
	                   log.error(e.getMessage(), e);
	               }
	               return bigWinAmt;
	          }
				
	           public String verifyAmgWinImg(Desktop_HTML_Report report, ImageLibrary imageLibrary) {
	       		String amgWinAmt = null;
	       		Wait = new WebDriverWait(webdriver, 30000);

	       		try {
	       			amgWinAmt = getConsoleText("return " + XpathMap.get("AmgWinAmt"));
       				System.out.println("Amzing Win Amount is " + amgWinAmt);
       				log.debug(" Amzing Win Amount is " + amgWinAmt);
	       			boolean isAmgWin = isElementVisible("isAmgWinAmt");
	       			if (isAmgWin) {	
	       				
	       			  while(amgWinAmt.equals(""))
	                  {                    
	                      imageLibrary.click("Spin");    
	                      Thread.sleep(7000);
	                      amgWinAmt = getConsoleText("return " + XpathMap.get("TotalWinAmt"));
	                  }
	       				//report.detailsAppendFolderOnlyScreenShot(currencyName);

	       			} else {
	       				System.out.println("There is no Amg Win ");
	       				log.debug("There is no Amg Win");
	       			}
	       		} catch (Exception e) {
	       			log.error(e.getMessage(), e);
	       		}
	       		return amgWinAmt;

	       	}
	           
	           public void checkpagenavigation(Desktop_HTML_Report report, String currencyName,String gameurl) {
	       		try {
	       			String mainwindow = webdriver.getWindowHandle();
	       			Set<String> s1 = webdriver.getWindowHandles();
	       			if (s1.size() > 1) {
	       				Iterator<String> i1 = s1.iterator();
	       				while (i1.hasNext()) {
	       					String ChildWindow = i1.next();
	       					if (mainwindow.equalsIgnoreCase(ChildWindow)) {
	       						//report.detailsAppend("verify the Navigation screen shot", " Navigation page screen shot", "Navigation page screenshot ", "Pass");
	       						ChildWindow = i1.next();
	       						webdriver.switchTo().window(ChildWindow);
	       						String url = webdriver.getCurrentUrl();
	       						log.debug("Navigation URL is :: " + url);
	       						log.debug("Navigation URL is :: " + url);
	       						if (!url.equalsIgnoreCase(gameurl)) {
	       							// Pass condition for navigation
	       							Thread.sleep(15000);
	       							report.detailsAppendFolder("verify the Navigation screen shot", " Navigation page screen shot",
	       									"Navigation page screenshot ", "Pass",currencyName);
	       							log.debug("Page navigated succesfully");
	       							log.debug("Page navigated succesfully");
	       							webdriver.close();
	       						} else {
	       							//System.out.println("Now On game page");
	       							log.debug("Now On game page");
	       						}
	       					}
	       				}
	       				webdriver.switchTo().window(mainwindow);
	       			} else {
	       				String url = webdriver.getCurrentUrl();
	       				//System.out.println("Navigation URL is ::  " + url);
	       				log.debug("Navigation URL is ::  " + url);
	       				if (!url.equalsIgnoreCase(gameurl)) {
	       					// Pass condition for navigation
	       					Thread.sleep(8000);
	       					report.detailsAppendFolder("verify the Navigation screen shot", " Navigation page screen shot",
	       							"Navigation page screenshot ", "Pass",currencyName);
	       					log.debug("Page navigated succesfully");

	       					webdriver.navigate().to(gameurl);
	       					Thread.sleep(8000);
	       				} else {
	       					webdriver.navigate().to(gameurl);
	       					Thread.sleep(8000);
	       					//waitForSpinButton();
	       					//System.out.println("Now On game page");
	       					log.debug("Now On game page");
	       				}
	       			}

	       		} catch (Exception e) {
	       			log.error("error in navigation of page");
	       		}
	       	}
	           
	           public void closeOverlay() {
	       		try {
	       			Actions act = new Actions(webdriver);
	       			act.moveToElement(webdriver.findElement(By.id("canvasContainer")), 100, 200).click().build().perform();
	       			//act.moveByOffset(100, 200).click().build().perform();
	       			//act.moveByOffset(100, -200).build().perform();

	       		} catch (Exception e) {
	       			e.getMessage();
	       		}
	       	}
	           /**
				 * To  isFreeSpinsVisible with imag
				 * @author pb61055
				 */   
	           public boolean isFreeSpinsVisible(ImageLibrary imageLibrary) 
				{
					boolean isFreeSpinsVisible=false;
						
						try {imageLibrary.click("Spin");
						Thread.sleep(12000);							
							boolean isVisible = GetConsoleBooleanText("return " + XpathMap.get("isFreeSpinsVisible"));
							while(isVisible!=true)
							{					
													
								imageLibrary.click("Spin");
								Thread.sleep(12000);
								
							}
							isFreeSpinsVisible=true;
						
						} catch (Exception e) {
							log.error(e.getMessage(), e);
						}
						
				
					return isFreeSpinsVisible;
					} 
	           
	           /**
				 * To  getCurrentWinAmtImg
				 * @author pb61055
				 */
	           public String getCurrentWinAmtImg(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName) {
					String winAmt = null;
					Wait = new WebDriverWait(webdriver, 250);
					int count=0;

					try {
						
						winAmt = getConsoleText("return " + XpathMap.get("TotalWinAmt"));
						System.out.println(" Win Amount is " + winAmt);
						log.debug(" Win Amount is " + winAmt);
						
		            	 /* while(winAmt.equals(""))
	                     {            
		            		
	                         imageLibrary.click("Spin");    
	                         Thread.sleep(10000);
	                         
	                         
	                         winAmt = getConsoleText("return " + XpathMap.get("TotalWinAmt"));
	                         report.detailsAppendFolderOnlyScreenShot(currencyName);
	                         count++;
	                         if(count==3)
	                         {
	                        	 break;
	                        	
	                         }
	                     }
		            	  */
					
					} catch (Exception e) {
						log.error(e.getMessage(), e);
					}
					return winAmt;

				}
	           /**
				 * To verify BigWin with Img
				 * @author pb61055
				 */

				   public String verifyBigWinImg(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName) {
						String bigWinAmt = null;
						Wait = new WebDriverWait(webdriver, 30000);
						int count=0;

						try {
							
						
							 bigWinAmt = getConsoleText("return " + XpathMap.get("BigWinAmt"));
							
							System.out.println("Big Win Amount is " + bigWinAmt);
							log.debug(" Big Win Amount is " + bigWinAmt);
							/*
			            	  while(bigWinAmt.equals(""))
		                     {            
			            		 
		                         imageLibrary.click("Spin");    
		                         Thread.sleep(10000);
		                         
		                         
		                         bigWinAmt = getConsoleText("return " + XpathMap.get("BigWinAmt"));
		                         report.detailsAppendFolderOnlyScreenShot(currencyName);
		                         count++;
		                         if(count==3)
		                         {
		                        	 break;
		                        	
		                         }
		                     }
			            	  */
						
						} catch (Exception e) {
							log.error(e.getMessage(), e);
						}
						return bigWinAmt;

					}
				   
				   /**
					 * This method is used to verify playcheck using image or xpath
					 * 
					 * @author pb61055
					 */
				   public void verifyPlayCheckNavigation(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName) 
				   {
					   String gameurl = webdriver.getCurrentUrl();

					   try 
					   {
						   func_Click(XpathMap.get("HelpMenu"));
						   
						   Thread.sleep(1000);
						   if (imageLibrary.isImageAppears("PlayCheck")) 
						   {
							   Thread.sleep(1000);
							   report.detailsAppendNoScreenshot("Verify Play Check is visible", "Play Check should be visible", "Play Check is visible", "Pass");
							   imageLibrary.click("PlayCheck");
							   Thread.sleep(2000);
							   checkpagenavigation(report,currencyName,gameurl);
						   }
						   else if(elementVisible_Xpath(XpathMap.get("PlayCheck")))
						   {
							   report.detailsAppendNoScreenshot("Verify Play Check is visible", "Play Check should be visible", "Play Check is visible", "Pass");
							   func_Click(XpathMap.get("PlayCheck"));
							   Thread.sleep(2000);
							   checkpagenavigation(report,currencyName,gameurl);
						   }
						   else
						   {
							   report.detailsAppendNoScreenshot("Verify Play Check is visible", "Play Check should be visible", "Play Check is not visible", "Fail");							
						   }
					   } catch (Exception e) 
					   {
						   log.error(e.getMessage(), e);   	
					   }

				   }
				  
				   /**
					 * This method is used to verify cash check using image or xpath
					 * 
					 * @author pb61055
					 */
				   public void verifyCashCheckNavigation(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName) 
				   {
					   String gameurl = webdriver.getCurrentUrl();

					   try 
					   {
						   func_Click(XpathMap.get("HelpMenu"));
						   Thread.sleep(1000);
						   if (imageLibrary.isImageAppears("CashCheck")) 
						   {
							   Thread.sleep(1000);
							   report.detailsAppendNoScreenshot("Verify Cash Check is visible", "Cash Check should be visible", "Cash Check is visible", "Pass");
								
							   imageLibrary.click("CashCheck");
							   Thread.sleep(2000);
							   checkpagenavigation(report,currencyName,gameurl);
						   }
						   else if(elementVisible_Xpath(XpathMap.get("CashCheck")))
						   {
							   report.detailsAppendNoScreenshot("Verify Cash Check is visible", "Cash Check should be visible", "Cash Check is visible", "Pass");
								
							   func_Click(XpathMap.get("CashCheck"));
							   Thread.sleep(2000);
							   checkpagenavigation(report,currencyName,gameurl);
						   }
						   else
						   {
							   report.detailsAppendNoScreenshot("Verify Cash Check is visible", "Cash Check should be visible", "Cash Check is not visible", "Fail");
						   }

					   } catch (Exception e) 
					   {
						    log.error(e.getMessage(), e);
					   }

				   }
				   
				   /**
					 * This method is used to verify Help using image or xpath
					 * 
					 * @author pb61055
					 */
				   public void verifyHelpNavigation(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName,String Market) 
				   {
					   String gameurl = webdriver.getCurrentUrl();
					   String hook = null;
					   try 
					   {
						   if(Market.equalsIgnoreCase("uk"))
							   hook="Help";
						   else
							   hook="dotComHelp";
						   
						   func_Click(XpathMap.get("HelpMenu"));
						   Thread.sleep(1000);
						   if (imageLibrary.isImageAppears("Help")) 
						   {
							   //Thread.sleep(1000);
							   report.detailsAppendNoScreenshot("Verify Help is visible", "Help should be visible", "Help is visible", "Pass");
								
							   imageLibrary.click("Help");
							   Thread.sleep(2000);
							   checkpagenavigation(report,currencyName,gameurl);
						   }
						   else if(elementVisible_Xpath(XpathMap.get(hook)))
						   {
							   report.detailsAppendNoScreenshot("Verify Help is visible", "Help should be visible", "Help is visible", "Pass");
								
							   func_Click(XpathMap.get("ukHelp"));
							   Thread.sleep(2000);
							   checkpagenavigation(report,currencyName,gameurl);
						   }
						   else
						   {
							   report.detailsAppendNoScreenshot("Verify Help is visible", "Help should be visible", "Help is not visible", "Fail");
								
						   }

					   } catch (Exception e) {
						   
						   log.error(e.getMessage(), e);
					   }

				   }
				   
				   /**
					 * This method is used to verify Player protection using image or xpath
					 * 
					 * @author pb61055
					 */
				   public void verifyPlayerProtectionNavigation(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName) 
				   {
					   String gameurl = webdriver.getCurrentUrl();

					   try 
					   {
						   func_Click(XpathMap.get("HelpMenu"));
						   Thread.sleep(1000);
						   if (imageLibrary.isImageAppears("PlayerProtection")) 
						   {
							   Thread.sleep(1000);
							   report.detailsAppendNoScreenshot("Verify Player protection is visible", "Player protection should be visible", "Player protection is visible", "Pass");
								
							   imageLibrary.click("PlayerProtection");
							   Thread.sleep(2000);
							   checkpagenavigation(report,currencyName,gameurl);
						   }
						   else if(elementVisible_Xpath(XpathMap.get("PlayerProtection")))
						   {
							   report.detailsAppendNoScreenshot("Verify Player protection is visible", "Player protection should be visible", "Player protection is visible", "Pass");
								
							   func_Click(XpathMap.get("PlayerProtection"));
							   Thread.sleep(2000);
							   checkpagenavigation(report,currencyName,gameurl);
						   }
						   else
						   {
							   report.detailsAppendNoScreenshot("Verify Player protection is visible", "Player protection should be visible", "Player protection is not visible", "Fail");
								   
						   }

					   } catch (Exception e) 
					   {
						   log.error(e.getMessage(), e);
					   }

				   }
				 
				   
				   /**
					 * To click on continue button in session reminder
					 * @author pb61055
					 */

					public void selectContinueSession() 
					{
						try {
						webdriver.findElement(By.xpath(XpathMap.get("SessionContinue"))).click();
						log.debug("clicked on continue in session reminder dialog box");
						}
						catch(Exception e)
						{
							log.info("Unable to click on session continue ",e);
							log.error(e.getMessage(), e);
						}
					}
					
					 /**
					 * To verify if credit deducted or not
					 * @author pb61055
					 */
					
					public boolean isCreditDeducted(String creditB4Spin, String betValue) {

						String creditAfterSpin = getConsoleText("return " + XpathMap.get("CreditValue"));

						double dblcreditAfterSpin = Double.parseDouble(creditAfterSpin.replaceAll("[^0-9]", ""));
						double dblcreditB4Spin = Double.parseDouble(creditB4Spin.replaceAll("[^0-9]", ""));
						double dblbetValue = Double.parseDouble(betValue.replaceAll("[^0-9]", ""));

						if ((dblcreditB4Spin - dblbetValue) == dblcreditAfterSpin) {
							System.out.println("Intial Credit is = " + dblcreditB4Spin + " and selected bet is = " + dblbetValue);
							System.out.println("Credit after spin is = " + dblcreditAfterSpin);
							log.debug("Cradit after spin with calculation is " + dblcreditB4Spin + "-" + dblbetValue + " = "
									+ dblcreditAfterSpin);
							return true;
						}
						return false;
					}
					
					 /**
					 * To verify if Bonus funds notification is visible
					 * @author pb61055
					 */
					public boolean isBonusFundsNotificationVisible()
					{
						boolean isVisible = false;
						WebDriverWait Wait = new WebDriverWait(webdriver, 20);
						try {
							Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("isBonusFundsNotificationVisible"))));
							if (elementVisible_Xpath(XpathMap.get("isBonusFundsNotificationVisible"))) {
								isVisible = true;
							}
						} catch (Exception e) {
							log.error(e.getMessage(), e);
						}
						return isVisible;
					}
					
					
					 
					   /**
						 * This method is used to verify bonusFundsNotification terms and condition
						 * 
						 * @author pb61055
						 */
					   public void verifyBonusFundsNotificationNavigation(Desktop_HTML_Report report,String currencyName) 
					   {
						   String gameurl = webdriver.getCurrentUrl();

						   try 
						   {
							   Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("bonusFundsNotificationTandC"))));
							   if(elementVisible_Xpath(XpathMap.get("bonusFundsNotificationTandC")))
							   {
								   report.detailsAppendNoScreenshot("Verify Bonus Funds notification Terms and Conditions and its navigation", "Bonus Funds notification Terms and Conditions should be visible", "Bonus Funds notification Terms and Conditions is visible", "Pass");
									
								   func_Click(XpathMap.get("bonusFundsNotificationTandC"));
								   Thread.sleep(2000);
								   checkpagenavigation(report,currencyName,gameurl);
							   }
							   else
							   {
								   report.detailsAppendNoScreenshot("Verify Bonus Funds notification Terms and Conditions and its navigation", "Bonus Funds notification Terms and Conditions should be visible", "Bonus Funds notification Terms and Conditions is not visible", "Fail");
							   }
						   } catch (Exception e) 
						   {
							   log.error(e.getMessage(), e);   	
						   }

					   }
					   
					    /* * To wait until session reminder
						 * @author sp68146
						 */
					   public boolean waitUntilSessionReminder(){
					   Boolean sessionReminderScreen=false;
					   try {					   		
						   		Wait = new WebDriverWait(webdriver, 120);
						   		Wait.until(ExpectedConditions
									.visibilityOfElementLocated(By.xpath(XpathMap.get("SessionReminderContinueBTN"))));
						   		if(webdriver.findElement(By.xpath(XpathMap.get("SessionReminderContinueBTN"))).isDisplayed()){
						   			sessionReminderScreen=true; 
						   		}
						   
					   		}catch (Exception e) {
			       			log.error("error in navigation of page");
					   		}
					   return sessionReminderScreen;				   			  
				   	}
					 	  
					   
					   /**
					    * This method is used to click on Base scene continue/NFD buttin
					    * @author pb61055
					    * @param report
					    * @param imageLibrary
					    */
					   public void clickOnBaseSceneContinueButton(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName)
					   {
						   try
						   {
							   imageLibrary.waitTillImageVisible("NFDButton");
							   if(imageLibrary.isImageAppears("NFDButton"))
								{
										//System.out.println("NFD button is visible");log.debug("NFD button is visible");									
										Thread.sleep(2000);
										imageLibrary.click("NFDButton");
										Thread.sleep(3000);
								
									}
									else
									{
										//System.out.println("NFD button is not visible");log.debug("NFD button is not visible");
										report.detailsAppendFolder("Verify Continue button is visible ", "Continue buttion is visible", "Continue button is not visible", "Fail", currencyName);
									}
						   }
						   catch (Exception e) {
							   log.error(e.getMessage(), e);
						   	}
					   }
					   
					   /**
					    * This method is used for verifying currency format for credits
					    * @author pb61055
					    * @param report
					    * @param currencyName
					    * @param regExpr
					    */
					   
					   public void verifyCreditsCurrencyFormat(Desktop_HTML_Report report, String regExpr)
					   {
						   boolean credits = verifyRegularExpression(report, regExpr,getConsoleText("return " + XpathMap.get("CreditValue")));

						   if (credits)
						   {
										
							   report.detailsAppendNoScreenshot("Verify the currency format for credit ","Credit should display in correct currency format " ,"Credit is displaying in correct currency format ", "Pass");																
							} else 
							{
								report.detailsAppendNoScreenshot("Verify the currency format for credit ","Credit should display in correct currency format " ,"Credit is displaying in incorrect currency format ", "Fail");
							}
							
					   }
					   
					   /**
					    * This method is used for verifying currency format for credits
					    * @author pb61055
					    * @param report
					    * @param currencyName
					    * @param regExpr
					    */
					   
					   public void verifyBetCurrencyFormat(Desktop_HTML_Report report, String regExpr)
					   {
						   boolean bet = verifyRegularExpression(report, regExpr,getConsoleText("return " + XpathMap.get("BetTextValue")));

						   if (bet)
						   {
										
							   report.detailsAppendNoScreenshot("Verify the currency format for bet ","Bet should display in correct currency format " ,"Bet is displaying in correct currency format ", "Pass");																
							} else 
							{
								report.detailsAppendNoScreenshot("Verify the currency format for bet ","Bet should display in correct currency format " ,"Bet is displaying in incorrect currency format ", "Fail");
							}
							
					   }
					   
					   
					   /**
					    * This method is used for click on spin and check reel landing
					    * @author pb61055
					    * @param report
					    * @param imageLibrary
					    * @param currencyName
					    */
					   public void checkSpinReelLanding(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName)
					   {
						   try
						   {
							
						   if(imageLibrary.isImageAppears("Spin"))
							{
								imageLibrary.click("Spin");
								Thread.sleep(1750);
								report.detailsAppendFolder("Verify Spin reel landing ", "Reels should not land at the same time", "Reels did not land at the same time", "Pass",currencyName);
							}
						   }
						   catch (Exception e) {
							   log.error(e.getMessage(), e);
						   	}
					   }
					   
					   
					   /**
						 * This method is used for click on spin and check spin status
						 * @author pb61055
						 * @param report
						 * @param imageLibrary
						 * @param currencyName
						 * @param checkSpinStopBaseSceneUsingCoordinates
						 */
					   public void checkSpinStop(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName,String checkSpinStopUsingCoordinates)
					   {
						   try
						   {
							
						   if(imageLibrary.isImageAppears("Spin"))
							{

								imageLibrary.click("Spin");
								Thread.sleep(1750);
								report.detailsAppendFolder("Verify Spin button status ", "Spin button should not have stop button", "Spin button does not have stop button", "Pass",currencyName);
								Thread.sleep(3000);
								
								if(checkSpinStopUsingCoordinates.equalsIgnoreCase("yes"))
								{
									imageLibrary.click("Spin");
									Thread.sleep(1000);
									ClickByCoordinates("return " + XpathMap.get("clickOnReelX"), "return " + XpathMap.get("clickOnReelY"));
								
									report.detailsAppendFolder("Verify  clicking or tapping on the reels does not act as a Spin/Stop or Slam stop by stopping the reels spin ", "Spin button should not resolve", "Spin button does not resolve", "Pass",currencyName);
								}
							}
						   
						   
						   }catch (Exception e) {
							   log.error(e.getMessage(), e);
						   	}
					
					   }
					   
					   /**
						 * This method is used to verify Game name under Base scene, Menu, Paytable and settings
						 * 
						 * @author pb61055
						 */
					   public void verifyGameNameThroughOutGameBaseScene(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName) 
					   {
						   try
						   {
							
							gameNameOnTopBar(report);																	
							if(imageLibrary.isImageAppears("Menu"))
							{
								imageLibrary.click("Menu");
								Thread.sleep(2000);	
								report.detailsAppendFolder("Menu Open ", "Menu should be open", "Menu is Opened", "Pass",currencyName);
								if(imageLibrary.isImageAppears("Paytable"))
								{
									//click on PayTable
									imageLibrary.click("Paytable");
									Thread.sleep(2000);																
									if(elementVisible_Xpath(XpathMap.get("PaytableClose")))
									{
										report.detailsAppendFolder("PayTable Open ", "PayTable should be opened", "PayTable is opened", "Pass",currencyName);
										Thread.sleep(2000);
										gameNameOnTopBar(report);										
										
									func_Click(XpathMap.get("PaytableClose"));
									Thread.sleep(2000);
									
								}
								}
							}								
							try {

								// click on menu if settings are under menu
								if ("Yes".equalsIgnoreCase(XpathMap.get("settingsUnderMenu"))) {
									if (imageLibrary.isImageAppears("Menu")) {

										imageLibrary.click("Menu");
										Thread.sleep(2000);
									}
								}
								if (imageLibrary.isImageAppears("Settings")) {
									// click on menu
									imageLibrary.click("Settings");
									Thread.sleep(2000);
									report.detailsAppendFolder("Settings Open ", "Settings should be open", "Settings is Opened", "Pass",currencyName);
									gameNameOnTopBar(report);
									imageLibrary.click("MenuClose");
								}

							} catch (Exception e) {
								e.getMessage();
							}
						   }catch (Exception e) {
							   log.error(e.getMessage(), e);
						   		}
						}
					   
					   /**
						 * This method is used to verify Game name under Base scene, Menu, Paytable and settings
						 * 
						 * @author pb61055
						 */
					public void verifyClockThroughOutGameBaseScen(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName) 
					{
						   try
						   {//verifying clock on top bar
								clockOnTopBar(report);
								
								if(imageLibrary.isImageAppears("Menu"))
								{
									imageLibrary.click("Menu");
									Thread.sleep(2000);	
									if(imageLibrary.isImageAppears("Paytable"))
									{
										//click on PayTable
										imageLibrary.click("Paytable");
										Thread.sleep(2000);																
										if(elementVisible_Xpath(XpathMap.get("PaytableClose")))
										{
											report.detailsAppendFolder("PayTable open", "PayTable should be opened", "PayTable is opened", "Pass",currencyName);
											Thread.sleep(2000);
											clockOnTopBar(report);									
											
											func_Click(XpathMap.get("PaytableClose"));
											Thread.sleep(2000);
										
									}
									}
								}								
								try {

									// click on menu if settings are under menu
									if ("Yes".equalsIgnoreCase(XpathMap.get("settingsUnderMenu"))) {
										if (imageLibrary.isImageAppears("Menu")) {

											imageLibrary.click("Menu");
											Thread.sleep(2000);
										}
									}
									if (imageLibrary.isImageAppears("Settings")) {
										// click on menu
										imageLibrary.click("Settings");
										Thread.sleep(2000);
										report.detailsAppendFolder("Settings Open ", "Settings should be open", "Settings is Opened", "Pass",currencyName);
										clockOnTopBar(report);
										imageLibrary.click("MenuClose");
									}

								} catch (Exception e) {
									e.getMessage();
								}
								Thread.sleep(2000);
								if(imageLibrary.isImageAppears("BetButton"))						
								{								
									//click on bet menu
									System.out.println("Bet button is visible");log.debug("Bet button is visible");
									Thread.sleep(2000);
									imageLibrary.click("BetButton");
									report.detailsAppendFolder("Bet menu", "Bet menu", "Bet menu is opened", "Pass",currencyName);
									clockOnTopBar(report);
									imageLibrary.click("MenuClose");
								}
								
						   }catch (Exception e) {
							   log.error(e.getMessage(), e);
						   		}
						}
					
					
					/**
					 * *This method is used to open paytable
					 * @author pb61055
					 * @param report
					 * @param imageLibrary
					 * @param currencyName
					 * @return
					 */
					public boolean openPaytable(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName) 
					{
						boolean isPaytbaleOpen=false;
					
						try
						{
							if(imageLibrary.isImageAppears("Menu"))
							{
								imageLibrary.click("Menu");
								Thread.sleep(2000);	
								
								if(imageLibrary.isImageAppears("Paytable"))
								{
									//click on PayTable
									imageLibrary.click("Paytable");
									Thread.sleep(2000);	
									report.detailsAppendFolder("PayTable open", "PayTable should be opened", "PayTable is opened", "Pass",currencyName);
									isPaytbaleOpen=true;
								}
						
							}
						}catch (Exception e) {
							   log.error(e.getMessage(), e);
						   		}
						return isPaytbaleOpen;
					}
					
					/**
					 * This method is used to close paytable
					 * @author pb61055
					 * @param imageLibrary
					 * @return
					 */
					public boolean closePaytable() 
					{
						boolean isPaytbaleClose=false;
					
						try
						{
							if(elementVisible_Xpath(XpathMap.get("PaytableClose")))
							{	
								func_Click(XpathMap.get("PaytableClose"));
								Thread.sleep(2000);
								isPaytbaleClose=true;
								
							}
						}catch (Exception e) {
							   log.error(e.getMessage(), e);
						   		}
						return isPaytbaleClose;
					}
					   
					/**
					 * This method is used to open settings
					 * @author pb61055
					 * @param report
					 * @param imageLibrary
					 * @param currencyName
					 * @return
					 */
					public boolean openSettingsPanel(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName) 
					{
						boolean isSettingsOpen=false;
					

						try {

							// click on menu if settings are under menu
							if ("Yes".equalsIgnoreCase(XpathMap.get("settingsUnderMenu"))) {
								if (imageLibrary.isImageAppears("Menu")) {

									imageLibrary.click("Menu");
								}
							}
							if (imageLibrary.isImageAppears("Settings")) 
							{
								// click on menu
								imageLibrary.click("Settings");
								report.detailsAppendFolder("Settings Open ", "Settings should be open", "Settings is Opened", "Pass",currencyName);
								isSettingsOpen=true;
							}
							Thread.sleep(2000);
						} catch (Exception e) {
							e.getMessage();
						}
							
						return isSettingsOpen;
					}  
			
					/**
					 * This method is used to close settings/Paytable/Autoplay/bet using close button
					 * @author pb61055
					 * @param imageLibrary
					 * @return
					 */
					public boolean closeButton(ImageLibrary imageLibrary) 
					{
						boolean isClose=false;
					
						try
						{
							if(imageLibrary.isImageAppears("MenuClose"))
							{	
								imageLibrary.click("MenuClose");
								Thread.sleep(2000);
								isClose=true;
								
							}
						}catch (Exception e) {
							   log.error(e.getMessage(), e);
						   		}
						return isClose;
					}
					
					/**
					 * This method is used to open Autoplay
					 * @author pb61055
					 * @param report
					 * @param imageLibrary
					 * @param currencyName
					 * @return
					 */
					public boolean openAutoplayPanel(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName) 
					{
						boolean isAutoplayOpen=false;
	
						try {

							if(imageLibrary.isImageAppears("Autoplay")) 
							{
								//click on autoplay button						
								imageLibrary.click("Autoplay");
								Thread.sleep(3000);	
								if(imageLibrary.isImageAppears("Autoplay10x"))
								{
									report.detailsAppendFolder("Autoplay menu", "Autoplay menu", "Autoplay menu is opened", "Pass",currencyName);
									isAutoplayOpen=true;
								}
							}
						} catch (Exception e) {
							e.getMessage();
						}
							
						return isAutoplayOpen;
					}  
					/**
					 * This method is used to open bet panel
					 * @author pb61055
					 * @param report
					 * @param imageLibrary
					 * @param currencyName
					 * @return
					 */
					public boolean openBetPanel(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName) 
					{
						boolean isBetOpen=false;
	
						try {
							if(imageLibrary.isImageAppears("BetButton"))						
							{								
								
								imageLibrary.click("BetButton");
								Thread.sleep(2000);
								report.detailsAppendFolder("Bet menu", "Bet menu", "Bet menu is opened", "Pass",currencyName);
								isBetOpen=true;
							}
						} catch (Exception e) {
							e.getMessage();
						}
							
						return isBetOpen;
					} 

					
					/**
					 * To validate session reminder testcases on base scene
					 * @author pb61055 
					 * @param report
					 * @param userName
					 * @param imageLibrary
					 * @param currencyName
					 * @param SessionReminderUserInteraction
					 * @param SessionReminderContinue
					 * @param SessionReminderExitGame
					 */
					public void validateSessionReminderBaseScene(Desktop_HTML_Report report,String userName,ImageLibrary imageLibrary,String currencyName,String SessionReminderUserInteraction,String SessionReminderContinue,String SessionReminderExitGame) 
					{
						boolean isVisible=false;
						try 
						{
							CommonUtil util = new CommonUtil();						
							String gameurl = webdriver.getCurrentUrl();
							
							String periodValue= XpathMap.get("sessionReminderDurationInSec");
							String resetPeriodValue=XpathMap.get("resetSessionReminderValue");
						
							//assigning session reminder
							boolean sessionReminderAssigned=util.setSessionReminderOnAxiom(userName,periodValue);
							loadGame(gameurl);Thread.sleep(5000);
							/*if ("Yes".equalsIgnoreCase(XpathMap.get("NFD"))) {
								clickOnBaseSceneContinueButton(report, imageLibrary);
							}*/
							log.debug("Session reminder is set for "+periodValue+" secs");
										
							
							if (sessionReminderAssigned && sessionReminderPresent())
							{
								report.detailsAppendFolder("Verify Session Reminder must be present","Session Reminder must be present", "Session Reminder is present","Pass", currencyName);
									
								if(SessionReminderUserInteraction.equalsIgnoreCase("yes"))
								{
									//clicking else where on the screen
									closeOverlay();
									report.detailsAppendFolder("Verify when Session Reminder is present and player should not be able to interact with any part of the game","Player should not not be able to interact with any part of the game", "Unable to interact with the game","Pass",currencyName);								
									Thread.sleep(2000);
								}
								
								//Ensure that the continue button takes you back to the game
								if(SessionReminderContinue.equalsIgnoreCase("yes"))
								{
									selectContinueSession();
									Thread.sleep(2000);
									if("Yes".equalsIgnoreCase(XpathMap.get("NFD"))){
										if(imageLibrary.isImageAppears("NFDButton"))
											isVisible=true;
									}else{
										if(imageLibrary.isImageAppears("Spin"))
											isVisible=true;
									}
									if(isVisible)
									{
										report.detailsAppendFolder("Verify Session Reminder dialog box is closed and game is visible",	" Session Reminder dialog box is closed and game should be visible"," Session Reminder dialog box is closed and game is visible","Pass",currencyName);
										Thread.sleep(2000);
									}
									else
									{
										report.detailsAppendFolder("Verify Session Reminder dialog box is closed and game is visible",	" Session Reminder dialog box is closed and game should be visible"," Session Reminder dialog box is closed and game is not visible","Fail",currencyName);	
									}
								}
							} else {
								report.detailsAppendFolder("Verify Session Reminder must be present","Session Reminder must be present","Session Reminder is not present", "Fail",currencyName);
								log.debug("Session reminder is not visible on screen");
								Thread.sleep(2000);
							}

							//Ensure that the log out button takes you to the lobby
							if(SessionReminderExitGame.equalsIgnoreCase("yes"))
							{							
								//assigning session reminder
								sessionReminderAssigned=util.setSessionReminderOnAxiom(userName,periodValue);
								loadGameAndClickContinueBtn(gameurl, report, imageLibrary, currencyName);
								
								if (sessionReminderAssigned && sessionReminderPresent())
								{
									func_Click(XpathMap.get("ExitGameSessrionReminder"));
									Thread.sleep(12000);
									report.detailsAppendNoScreenshot("Verify Session Reminder log out/Exit game button","Log out button shoul take user to the lobby","Log out button takes user to the lobby", "Pass");
									
									checkpagenavigation(report,currencyName,gameurl);		
									if ("Yes".equalsIgnoreCase(XpathMap.get("NFD"))) {
										clickOnBaseSceneContinueButton(report, imageLibrary,currencyName);
									}
								}
							}
							else {
								report.detailsAppendFolder("Verify Session Reminder must be present to verify exit game scenario","Session Reminder must be present","Session Reminder is not present", "Fail",currencyName);
							}
									
								
							//reset to higher duarion for session reminder
							boolean resetSessionReminderDUration=util.setSessionReminderOnAxiom(userName,resetPeriodValue);
							if (resetSessionReminderDUration) {
								log.debug("Session reminder has been reset to 10hr in order to avoid pop up");
							} else {
								log.debug("Unable to reset Session reminder");
							}							
							// relaunching the game to set the above reset session reminder duration to higher value
							loadGameAndClickContinueBtn(gameurl, report, imageLibrary, currencyName);
								
								
											
					} catch (Exception e) {
						e.getMessage();
						log.error(e);
					}
					}			
					
					
					/**
					 * To validate  bonus funds notification testcases
					 * @author pb61055
					 * @param report
					 * @param userName
					 * @param imageLibrary
					 * @param currencyName
					 * @param closeBtnEnabledCMA
					 */
					public void validateBonusFundsNotificationBaseScene(Desktop_HTML_Report report,String userName,ImageLibrary imageLibrary,String currencyName,String closeBtnEnabledCMA) 
					{
						try 
						{
							if(closeBtnEnabledCMA.equalsIgnoreCase("yes"))
							{
								CommonUtil util = new CommonUtil();
								String gameurl = webdriver.getCurrentUrl();
								
								if(util.enableBonusFundsNotification(userName))
								{
									//Relaunching the game to verify Bonus funds notification
									loadGameAndClickContinueBtn(gameurl, report, imageLibrary, currencyName);
									
									//Ensure the Bonus Funds notification appears
									if(isBonusFundsNotificationVisible())
									{
										report.detailsAppendFolder("Verify if Bonus funds notification is visible ","Bonus funds notification should display" ,"Bonus funds notification is displayed", "Pass",currencyName);		
									}
									else
									{
										report.detailsAppendFolder("Verify if Bonus funds notification is visible ","Bonus funds notification should display" ,"Bonus funds notification is not displayed", "Fail",currencyName);
									}
								
								
									//Dismiss the notification by clicking the close button
									if(isBonusFundsNotificationVisible())
									{

										func_Click(XpathMap.get("closeBonusFundsNotification"));
										if (!(isBonusFundsNotificationVisible())) 
										{
											report.detailsAppendFolder("Verify Bonus funds notification can be dismissed by clicking on close button","Bonus funds notification should dismiss by clicking on close button","Bonus funds notification is dismissed by clicking the close button","Pass", currencyName);
											report.detailsAppendFolder("Verify Bonus funds notification can be dismissed after 1 second of gameplay","Bonus funds notification should dismiss after 1 second of gameplay","Bonus funds notification is dismissed after 1 second of gameplay","Pass", currencyName);
										} else 
										{
											report.detailsAppendFolder("Verify Bonus funds notification can be dismissed by clicking on lcose button","Bonus funds notification should dismiss by clicking on close button","Bonus funds notification is dismissed by clicking the close button","Fail", currencyName);
											report.detailsAppendFolder("Verify Bonus funds notification can be dismissed after 1 second of gameplay","Bonus funds notification should dismiss after 1 second of gameplay","Bonus funds notification is not dismissed after 1 second of gameplay","Fail", currencyName);
										}
									}
								
							
									//After dismissing the Notification, ensure you can spin with no errors thrown
									if(imageLibrary.isImageAppears("Spin"))
									{
										Thread.sleep(2000);
										imageLibrary.click("Spin");
										Thread.sleep(2000);
										report.detailsAppendFolder("Verify After dismissing the Notification, ensure you can spin with no errors thrown","No error should display","No errors found","Pass", currencyName);
									}
							
							
									//Clicking the hyperlink on the Notification
									//Refresh the game
									webdriver.navigate().refresh();
									Thread.sleep(10000);
									report.detailsAppendFolder("Verify Bonus funds notification is visible after refresh","Bonus funds notification should be visible after refresh","Bonus funds notification is visible after refresh","Pass", currencyName);
									
									verifyBonusFundsNotificationNavigation(report, currencyName);
									
									loadGameAndClickContinueBtn(gameurl, report, imageLibrary, currencyName);
									
									Thread.sleep(2000);
									if(imageLibrary.isImageAppears("Spin"))
									{
										report.detailsAppendFolder("Verify player can navigate back to the game after navigating to Terms and Conditions","Base scene should be visible","Base scene is visible","Pass", currencyName);
									}					
									else
									{
										report.detailsAppend("Unable to assign Bonus funds notification / CMA to player hence skipping the test cases","","","");
									}	
								}
							}
									
						} catch (Exception e) {
						e.getMessage();
						log.error(e);
					}
				
					}		
					
					/**
					 * This method is used to verify Value adds navigation
					 * @author pb61055
					 * @param report
					 * @param imageLibrary
					 * @param currencyName
					 * @param PlayerProtectionNavigation
					 * @param CashCheckNavigation
					 * @param HelpNavigation
					 * @param PlayCheckNavigation
					 */
					public void verifyValueAddsNavigation(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName,
							String PlayerProtectionNavigation,String CashCheckNavigation,String HelpNavigation,String PlayCheckNavigation, String Market) 
					{
						try
						{
							String gameurl = webdriver.getCurrentUrl();
							if (func_Click(XpathMap.get("HelpMenu"))) {
								Thread.sleep(1000);
								report.detailsAppendFolder("Verify if menu on top bar is open",
										"Menu on top bar should be open", "Menu on top bar is open", "Pass",
										currencyName);
								closeOverlay();
								Thread.sleep(1000);
								
								loadGameAndClickContinueBtn(gameurl, report, imageLibrary, currencyName);
								
								if (HelpNavigation.equalsIgnoreCase("yes")) {
									verifyHelpNavigation(report, imageLibrary, currencyName,Market);
									Thread.sleep(2000);
								}
								
								if (CashCheckNavigation.equalsIgnoreCase("yes")) {
									verifyCashCheckNavigation(report, imageLibrary, currencyName);
									Thread.sleep(2000);
								}

								if (PlayCheckNavigation.equalsIgnoreCase("yes")) {
									verifyPlayCheckNavigation(report, imageLibrary, currencyName);
									Thread.sleep(5000);
								}
								
								if (PlayerProtectionNavigation.equalsIgnoreCase("yes")) {
									verifyPlayerProtectionNavigation(report, imageLibrary, currencyName);
									Thread.sleep(2000);
								}


								if ("Yes".equalsIgnoreCase(XpathMap.get("NFD"))) {
									clickOnBaseSceneContinueButton(report, imageLibrary,currencyName);
								}
						}
							
						} catch (Exception e) {
							e.getMessage();
							log.error(e);
						}
					}
						
					/**
					 * This is used to verify banking option navigation from Hamburger menu
					 * @author pb61055
					 * @param report
					 * @param imageLibrary
					 * @param currencyName
					 */
					public void verifyBankingNavigation(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName) 
					{
						try 
						{
							String gameurl = webdriver.getCurrentUrl();
						
							if(imageLibrary.isImageAppears("Banking"))
							{
								report.detailsAppendNoScreenshot("Verify Banking option in Hamburger menu", "Banking option should be visible", "Banking option is visible", "Pass");
								Thread.sleep(2000);
								imageLibrary.click("Banking");
								Thread.sleep(8000);
								checkpagenavigation(report,currencyName,gameurl);
							}else
							{
								report.detailsAppendNoScreenshot("verify Banking opetion in Hamburger menu", "Banking option should be visible", "Banking option is not visible", "Fail");
							}
							
						} catch (Exception e) {
							e.getMessage();
							log.error(e);
						}
					}
					
				
					
					/**
					 * This method is used for making depoit via banking option and verifying credit after deposit
					 * @author pb61055
					 * @param report
					 * @param userName
					 * @param balance
					 * @param currencyName
					 * @param imageLibrary
					 */
					 public boolean creditDepositUsingBanking(Desktop_HTML_Report report,String userName,String balance,String currencyName,ImageLibrary imageLibrary,String currencySymbol) 
					  {
						   String newCreditValue= null;
						   Wait = new WebDriverWait(webdriver, 180);
						   boolean isUpdated = false;
						   try 
						   {
							  CommonUtil util = new CommonUtil();
							  String gameurl = webdriver.getCurrentUrl();
								
							  String newBalance=XpathMap.get("newBalance");
							  util.updateUserBalance(userName,newBalance);
						
							  loadGameAndClickContinueBtn(gameurl, report, imageLibrary, currencyName);
							  
							  
							  newCreditValue=getConsoleText("return " + XpathMap.get("CreditValue"));
							  
							  String value=newCreditValue.replaceAll("[^0-9.-]", "").trim();
							  System.out.println("value "+value);
							 
							  if(value.equals("0.00"))
							  {
							  report.detailsAppendFolder("Verify credit value ","Credit amount should be 0" ,
									  "Credit amount is 0", "Pass",currencyName);
						
							  }
							  else
							  {
								  report.detailsAppendFolder("Verify credit value ","Credit amount should be 0" ,
										  "Credit amount is not 0", "Fail",currencyName);
								 
							  }
							  
								
							  if(imageLibrary.isImageAppears("Spin"))
								{
									imageLibrary.click("Spin");
									Thread.sleep(3000);
									
									 if(elementVisible_Xpath(XpathMap.get("insufficientBalanceOverlay")))
										{
										   report.detailsAppendFolder("Verify if the Insufficient balance dialog box appears", " Insufficient balance dialog box should appears", " Insufficient balance dialog box is visible", "Pass", currencyName);
								
										   webdriver.findElement(By.xpath(XpathMap.get("bankingYes"))).click();
										   Thread.sleep(5000);
										   
										   if(elementVisible_Xpath(XpathMap.get("isBankingVisible")))
										   {
											   report.detailsAppendFolder("Verify if the Banking is open", "Banking should be open", "Banking is open", "Pass", currencyName);
												
											   webdriver.navigate().refresh();
											   Thread.sleep(10000);
												log.debug ("Actual user: "+ userName+"Entered user: "+userName);
												webdriver.findElement(By.xpath(XpathMap.get("bankingLoginName"))).sendKeys(userName);
												Thread.sleep(10000);
												webdriver.findElement(By.xpath(XpathMap.get("bankingBalance"))).sendKeys(balance);
												Thread.sleep(2000);
												Wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XpathMap.get("bankingSubmit"))));
												
												report.detailsAppendFolder("Verify if values entered correclty", "Values should display", "Values are visible", "Pass", currencyName);
												
												try
												{
													webdriver.findElement(By.xpath(XpathMap.get("bankingSubmit"))).click();
												}catch (Exception e) {
													report.detailsAppendFolder("Unable to click on submit button ","Unable to click on submit button " ,
															"Unable to click on submit button ", "Fail",currencyName);
													log.error(e.getMessage(), e);
												}
												
												
												loadGameAndClickContinueBtn(gameurl, report, imageLibrary, currencyName);
												  
												String updatedCreditValue=getConsoleText("return " + XpathMap.get("CreditValue"));
												String credit=updatedCreditValue.replaceAll("[^0-9.-]", "").trim();
												if(credit.equals(balance))
												{
													report.detailsAppendFolder("Verify credit value ","Credit amount should be updated using banking deposit" ,
															"Credit amount is updated using banking deposit", "Pass",currencyName);
													isUpdated = true;
												}
												else
												{
													report.detailsAppendFolder("Verify credit value ","Credit amount should be updated using banking deposit" ,
															"Credit amount is not updated using banking deposit", "Fail",currencyName);
												}
												
											}
											else
											{
												report.detailsAppendFolder("Verify if the Banking is open", "Banking should be visible", "Banking is not visible", "Fail", currencyName);
											}
										}
									   else
									   {
										   report.detailsAppendFolder("Verify if the Insufficient balance dialog box appears", " Insufficient balance dialog box should appears", " Insufficient balance dialog box is not visible", "Fail", currencyName);
											
									   }
								}
						   } catch (Exception e) 
						   {
							   log.error(e.getMessage(), e);
						   }
						   return isUpdated;
					   }
					 

					   /**
					    * This method is used to check if net posotion is available or not
					    * @author pb61055
					    * @return
					    */
						public boolean isNetProfitAvailable() 
						{
							boolean isNetProfitAvailable = false;
							try {
								isNetProfitAvailable = webdriver.findElement(By.xpath(XpathMap.get("isNetPosition"))).isDisplayed();
							}catch (Exception e) {
								 log.error(e.getMessage(), e);						
								}		
							return isNetProfitAvailable;
						}
						
						/**
						 * This method is used to verify netposition test cases on base scene
						 * Test Data
						 * 1. no win
						 * 2. normal win
						 * 3. Big win						 * 
						 * @author pb61055
						 * @param report
						 * @param imageLibrary
						 * @param currencyName
						 * @param regExpr
						 * @param NetPositionLaunch
						 * @param NetPositionRefresh
						 * @param NetPositionNormalWin
						 * @param NetPositionBigWin
						 */
						public void verifyNetPositionBaseScene(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName,String regExpr,
								String NetPositionLaunch,String NetPositionRefresh,String NetPositionWinLoss,String NetPositionBigWin,
								String NetPositionRefreshWin,String NetPositionRefreshLoss)
						{
							String gameurl = webdriver.getCurrentUrl();
							String netPositionValueLaunch = null;
							String netPositionValueRefresh = null;
														
							try {
								loadGameAndClickContinueBtn(gameurl, report, imageLibrary, currencyName);				

								//The game session net position calculates correctly - (win and loss test data should be Passed)
								if(NetPositionWinLoss.equalsIgnoreCase("yes"))
								{
									verifyNetPositionValue(report,imageLibrary,currencyName, "NoWin");
									verifyNetPositionValue(report,imageLibrary,currencyName, "NormalWin");
								}
									
								//The game session net position calculates correctly after a Big Win
								if(NetPositionBigWin.equalsIgnoreCase("yes"))
								{
									verifyNetPositionValue(report,imageLibrary,currencyName, "BigWin");								
								}
									
								
								//The game session net position displays on the Titan/V info bar
								//The net position resets on each launch of the game
								if(NetPositionLaunch.equalsIgnoreCase("yes"))
								{	
									loadGameAndClickContinueBtn(gameurl, report, imageLibrary, currencyName);								
									if(isNetProfitAvailable())
									{
										if(imageLibrary.isImageAppears("Spin"))
										{
											imageLibrary.click("Spin");
											Thread.sleep(3000);
											imageLibrary.click("Spin");
											Thread.sleep(5000);
											imageLibrary.waitTillImageVisible("Spin");
											report.detailsAppendFolder("Verify net position before re-launching the game again", "Net position before re-launching the game ", "Net position before re-launching the game", "Pass",currencyName);
											
											loadGameAndClickContinueBtn(gameurl, report, imageLibrary, currencyName);
											report.detailsAppendFolder("Verify net position after re-launching the game ", "Net position after re-launching the game ", "Net position after re-launching the game" , "Pass",currencyName);
											
											netPositionValueLaunch=webdriver.findElement(By.xpath(XpathMap.get("isNetPosition"))).getText();
											boolean isFormatCorrect = verifyRegularExpression(report, regExpr,netPositionValueLaunch);
											if(isFormatCorrect)
											{
												
												report.detailsAppendNoScreenshot("Verify net position is in correct currency format", "Net position should be in correct currency format", "Net position is in correct currency format", "Pass");
											}
											else
											{
												report.detailsAppendFolder("Verify net position is in correct currency format", "Net position should be in correct currency format", "Net position is not in correct currency format", "Fail",currencyName);														
											}
										
											
											netPositionValueLaunch=netPositionValueLaunch.replaceAll("[^0-9.-]", "").trim();
											if(netPositionValueLaunch.equals("0.00"))
											{
												report.detailsAppendNoScreenshot("Verify net position is 0.00 when launched", "Net position should be 0.00", "Net position is 0.00", "Pass");
											}
											else
											{
												report.detailsAppendFolder("Verify net position is 0.00 when launched", "Net position should be 0.00", "Net position is not 0.00", "Fail",currencyName);														
											}
											
										}
									}
								}
								
								//The net position resets when you refresh/reload the game 
								if(NetPositionRefresh.equalsIgnoreCase("yes"))
								{
									if(isNetProfitAvailable())
									{
										if(imageLibrary.isImageAppears("Spin"))
										{
											imageLibrary.click("Spin");
											Thread.sleep(3000);
											imageLibrary.click("Spin");
											Thread.sleep(5000);
											imageLibrary.waitTillImageVisible("Spin");
											report.detailsAppendFolder("Verify net position before refreshing the game ", "Net position before refreshing the game ", "Net position before refreshing the game ", "Pass",currencyName);
											
											checkNetPositionAfterRefresh(report, imageLibrary, currencyName);
										}
									}
										
								}
									
								
								//Game does not carry previous spin info after a refresh - Win Scenario
								if(NetPositionRefreshWin.equalsIgnoreCase("yes"))
								{
									loadGameAndClickContinueBtn(gameurl, report, imageLibrary, currencyName);
									
									netPositionValueLaunch=netPositionValueLaunch.replaceAll("[^0-9.-]", "").trim();
									if(netPositionValueLaunch.equals("0.00"))
									{
										report.detailsAppendFolder("Verify net position is 0.00 when launched for win refresh scenario", "Net position should be 0.00", "Net position is 0.00", "Pass",currencyName);
									}
									else
									{
										report.detailsAppendFolder("Verify net position is 0.00 when launched for win refresh scenario", "Net position should be 0.00", "Net position is not 0.00", "Fail",currencyName);														
									}
									// As first spin is set to no win, spinning here to get normal win for next spin as per testdata
									if(imageLibrary.isImageAppears("Spin"))
									{
										imageLibrary.click("Spin");
										Thread.sleep(3000);
										imageLibrary.waitTillImageVisible("Spin");
									}
									verifyNetPositionValue(report,imageLibrary,currencyName, "NormalWin");
									
									refreshGameAndClickContinueBtn(report, imageLibrary, currencyName);
									
									netPositionValueLaunch=netPositionValueLaunch.replaceAll("[^0-9.-]", "").trim();
									if(netPositionValueRefresh.equals("0.00"))
									{
										report.detailsAppendNoScreenshot("Verify net position is 0.00 when refreshed after win for win refresh scenario", "Net position should be 0.00", "Net position is 0.00", "Pass");
									}
									else
									{
										report.detailsAppendFolder("Verify net position is 0.00 when refreshed after win for win refresh scenario", "Net position should be 0.00", "Net position is not 0.00", "Fail",currencyName);														
									}
									// As first spin is set to no win, spinning here to get normal win for next spin as per testdata
									if(imageLibrary.isImageAppears("Spin"))
									{
										imageLibrary.click("Spin");
										Thread.sleep(3000);
										imageLibrary.waitTillImageVisible("Spin");
									}
									verifyNetPositionValue(report,imageLibrary,currencyName, "NormalWin");
									
								}
								
								
								//Game does not carry previous spin info after a refresh - Loss Scenario
								if(NetPositionRefreshLoss.equalsIgnoreCase("yes"))
								{
									loadGameAndClickContinueBtn(gameurl, report, imageLibrary, currencyName);
									
									netPositionValueLaunch=netPositionValueLaunch.replaceAll("[^0-9.-]", "").trim();
									if(netPositionValueLaunch.equals("0.00"))
									{
										report.detailsAppendFolder("Verify net position is 0.00 when launched for loss refresh scenario", "Net position should be 0.00", "Net position is 0.00", "Pass",currencyName);
									}
									else
									{
										report.detailsAppendFolder("Verify net position is 0.00 when launched for loss refresh scenario", "Net position should be 0.00", "Net position is not 0.00", "Fail",currencyName);														
									}
									verifyNetPositionValue(report,imageLibrary,currencyName, "NoWin");
									
									refreshGameAndClickContinueBtn(report, imageLibrary, currencyName);
									
									netPositionValueLaunch=netPositionValueLaunch.replaceAll("[^0-9.-]", "").trim();
									if(netPositionValueRefresh.equals("0.00"))
									{
										report.detailsAppendNoScreenshot("Verify net position is 0.00 when refreshed after loss for loss refresh scenario", "Net position should be 0.00", "Net position is 0.00", "Pass");
									}
									else
									{
										report.detailsAppendFolder("Verify net position is 0.00 when refreshed after loss for loss refresh scenario", "Net position should be 0.00", "Net position is not 0.00", "Fail",currencyName);														
									}
									
									
									if(imageLibrary.isImageAppears("Spin"))
									{
										imageLibrary.click("Spin");
										Thread.sleep(5000);
										imageLibrary.waitTillImageVisible("Spin");
									}
									if(imageLibrary.isImageAppears("Spin"))
									{
										imageLibrary.click("Spin");
										Thread.sleep(12000);
										imageLibrary.waitTillImageVisible("Spin");
									}
									
									verifyNetPositionValue(report,imageLibrary,currencyName, "NoWin");
									
								}
								
								
								
								

							} catch (Exception e) {
								 log.error(e.getMessage(), e);						
								}
									
						
						}
						
						/**
						 * This method is used to validate if player has won any win
						 * @author pb61055
						 * @param report
						 * @param imageLibrary
						 * @param currencyName
						 * @param winType
						 * @return
						 */
						public boolean isPlayerWon(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName, String winType)
						{
							String winAmount = null;
							boolean isplayerWon = false;
							try {
								winAmount= playerWinAmount( report, imageLibrary, currencyName,  winType);
								if (winAmount != null && !"".equals(winAmount)) 
								{
									isplayerWon = true;
								}								
							}catch (Exception e) {
								 log.error(e.getMessage(), e);						
								}
							return isplayerWon;
						}
						/**
						 * This method is used to validate if player has won any win
						 * @author pb61055
						 * @param report
						 * @param imageLibrary
						 * @param currencyName
						 * @param winType
						 * @return
						 */
						public boolean isPlayerWon(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName)
						{							
							String winAmount = null;
							boolean isplayerWon = false;
							try {
								winAmount=getCurrentWinAmtImg(report, imageLibrary, currencyName);	
								if (winAmount != null && !"".equals(winAmount)) 
								{
									isplayerWon = true;
								}								
							}catch (Exception e) {
								 log.error(e.getMessage(), e);						
								}
							return isplayerWon;
						}
						
						/**
						 * This method is used to get Player win amount by Passing win type
						 * @author pb61055
						 * @param report
						 * @param imageLibrary
						 * @param currencyName
						 * @param winType
						 * @return
						 */
						public String playerWinAmount(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName, String winType)
						{
							String winAmount = null;
							try {
								if(winType.equalsIgnoreCase("NormalWin"))
									winAmount=getCurrentWinAmtImg(report, imageLibrary, currencyName);	
								else if(winType.equalsIgnoreCase("BigWin"))
									winAmount=verifyBigWinImg(report, imageLibrary, currencyName);	
								else if(winType.equalsIgnoreCase("BonusWin"))
									winAmount=verifyAmgWinImg(report, imageLibrary);
								else
									log.debug("no win");
															
							}catch (Exception e) {
								 log.error(e.getMessage(), e);						
								}
							return winAmount;
						}
						
						
						/**
						 * This method is used to calculate net postion value and verify if win amount is updated correctly
						 * @author pb61055
						 * @param report
						 * @param imageLibrary
						 * @param currencyName
						 * @param winType
						 */
						public void verifyNetPositionValue(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName, String winType)
						{
							boolean isNetPositionUpdated = false;
							boolean isPlayerWon = false;
							String winAmount = null;
							try {								
									String netPositionBeforeSpin = webdriver.findElement(By.xpath(XpathMap.get("isNetPosition"))).getText();
									double dblNetPositionBeforeSpin = Double.parseDouble(netPositionBeforeSpin.replaceAll("[^0-9.-]", "").replace(".", ""));
								
									if(imageLibrary.isImageAppears("Spin"))
									{
										imageLibrary.click("Spin");
										Thread.sleep(5000);	
										closeOverlay();
									}
									if(winType.equalsIgnoreCase("NormalWin"))
									{
										Thread.sleep(5000);
									}
									else if (winType.equalsIgnoreCase("BigWin")||winType.equalsIgnoreCase("BonusWin"))
									{
										Thread.sleep(12000);
									}
									else
										log.debug("no extra sleep time");
									
									imageLibrary.waitTillImageVisible("Spin");
										
									Thread.sleep(2000);
									
									String netPositionAfterSpin = webdriver.findElement(By.xpath(XpathMap.get("isNetPosition"))).getText();
									double dblNetPositionAfterSpin = Double.parseDouble(netPositionAfterSpin.replaceAll("[^0-9.-]", "").replace(".", ""));

									String betValue = getConsoleText("return " + XpathMap.get("BetTextValue"));
									double dblBetValue = Double.parseDouble(betValue.replaceAll("[^0-9]", ""));
									
									if(winType.isEmpty())
										isPlayerWon(report,imageLibrary,currencyName);
									else
										isPlayerWon = isPlayerWon(report,imageLibrary,currencyName, winType);
									
									if (isPlayerWon) 
									{
										winAmount = playerWinAmount( report, imageLibrary, currencyName,  winType);
										double dblWinAmount = Double.parseDouble(winAmount.replaceAll("[^0-9]", ""));
										try

										{
											double dblBSandBetValue = dblNetPositionBeforeSpin - dblBetValue;
											if ((dblBSandBetValue + dblWinAmount) == dblNetPositionAfterSpin) {
												System.out.println("Win is added to net position successfully");
												isNetPositionUpdated = true;
											} else {
												System.out.println("Win is not added to net position successfully");
												isNetPositionUpdated = false;
											}
										} catch (Exception e) {
											log.error(e);
										}
										
									} else {
										if ((dblNetPositionBeforeSpin - dblBetValue) == (dblNetPositionAfterSpin)) {
											System.out.println("There is no win, net position is updated successfully");
											isNetPositionUpdated = true;
										} else {
											System.out.println("There is no win, net position is not updated successfully");
											isNetPositionUpdated = false;
										}
									}
									if(isNetPositionUpdated)
									{
									report.detailsAppendFolder("verify Net position updates for "+winType,
											"Net position should update ", "Net position is updating ",
											"Pass",currencyName);
									log.debug("Net Position before spin " + netPositionBeforeSpin + " Net psotion after spin "
											+ netPositionAfterSpin);
									}
									else {
										report.detailsAppendFolder("verify Net position updates for "+winType,
												"Net position should update ", "Net position is not updating ",
												"Fail",currencyName);
										log.debug("Net Position before spin " + netPositionBeforeSpin + " Net psotion after spin "
												+ netPositionAfterSpin);
									}
								
							}catch (Exception e) {
								 log.error(e.getMessage(), e);						
								}
						}
			public void verifyBetSliders(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName)
			{
				try
				{
					if(imageLibrary.isImageAppears("BetMax"))
					{
						report.detailsAppendFolder("Bet menu", "Bet menu", "Bet menu is opened", "Pass",currencyName);
					
						// Check Coin Size Slider
						if(XpathMap.get("CoinSizeSliderPresent").equalsIgnoreCase("Yes"))
						{
							validateCoinSizeSlider(report,"CoinSizeSliderSet","BetMenuBetValue",currencyName);
							Thread.sleep(1000);
						}						
						
						
						// Check Coins per line slider
						if(XpathMap.get("CoinsPerLineSliderPresent").equalsIgnoreCase("Yes"))
						{
							validateCoinsPerLineSlider(report,"CoinsPerLineSliderSet","BetMenuBetValue",currencyName);
							Thread.sleep(1000);
						}
						
						
						//check lines slider
						if(XpathMap.get("LinesSliderPresent").equalsIgnoreCase("Yes"))
						{
							validateLinesSlider(report,"LinesSliderSet","BetMenuBetValue",currencyName);
							Thread.sleep(1000);
						}
					}
					
						}catch (Exception e) 
				{
					log.debug("unable to verify bet options");
					log.error(e.getMessage(), e);
				}
			}
			/**
			 * This method is used to slide the coin size slider and validate bet amount to verify its working or not
			 * @author pb61055
			 * @param report
			 */
			public void validateCoinSizeSlider(Desktop_HTML_Report report,String sliderPoint,String betValue,String currencyName)
			{
				try
				{
					boolean ableToSlide=verifySliderValue(sliderPoint,betValue);
					if(ableToSlide)
					{
						report.detailsAppendFolder("Verify if able to change coin size slider value", "Coin size slider value should change", "Coin size slider value is changed", "Pass",currencyName);
						log.debug("Coin size slider is working");
					}
					else
					{
						report.detailsAppendFolder("Verify if able to change coin size slider value", "Coin size slider value should change", "Unable to change coin cize clider value", "Fail",currencyName);
						log.debug("Coin size slider is not working");
					}				
				}catch (Exception e) 
				{
					log.debug("unable to verify coin size slider");
					log.error(e.getMessage(), e);
				}
			}
			/**
			 * This method is used to slide the coins per line slider and validate bet amount to verify its working or not
			 * @author pb61055
			 * @param report
			 */
			public void validateCoinsPerLineSlider(Desktop_HTML_Report report,String sliderPoint,String betValue,String currencyName)
			{
				try
				{	
					boolean ableToSlide=verifySliderValue(sliderPoint,betValue);			
					if(ableToSlide)
					{									
						report.detailsAppendFolder("Verify if able to change coins per line slider value", "Verify if able to change coins per line slider value", "Coins per line slider value is changed", "Pass",currencyName);
						log.debug("Coins Per Line slider is working");
					}
					else
					{
						report.detailsAppendFolder("Verify if able to change coins per line slider value", "Verify if able to change coins per line slider value", "Unable to change coins per line slider value", "Fail",currencyName);
						log.debug("Coins Per Line slider is not working");
					}							
					
				}catch (Exception e) 
				{
				log.debug("unable to verify coins per line slider");
				log.error(e.getMessage(), e);
			}
		}
			/**
			 * This method is used to slide the lines slider and validate bet amount to verify its working or not
			 * @author pb61055
			 * @param report
			 */
			public void validateLinesSlider(Desktop_HTML_Report report,String sliderPoint,String betValue,String currencyName)
			{
				try
				{
					boolean ableToSlide=verifySliderValue(sliderPoint,betValue);		
													
					if(ableToSlide)
					{
						report.detailsAppendFolder( "Verify if able to change line slider value", "Line slider value should change", "Line slider value is changed", "Pass",currencyName);
						log.debug("Line slider is working");
					}
					else
					{
						report.detailsAppendFolder( "Verify if able to change line slider value", "Line slider value should change", "Unable to change line slider value", "Fail",currencyName);
						log.debug("Line slider is not working");
					}									
			
				}catch (Exception e) 
				{
					log.debug("unable to verify line slider");
					log.error(e.getMessage(), e);
				}
			}
			
			/**
			 * This method is used to get slider value
			 * @author pb61055
			 * @param sliderPoint
			 * @param value
			 * @return
			 */
			public boolean verifySliderValue(String sliderPoint,String value)
			{
				boolean isSliderMoved =false;
				try
				{
					String  valueBefore=GetConsoleText("return "+XpathMap.get(value));			
					log.debug("Value before sliding: "+valueBefore);
					Thread.sleep(1000);
					clickAtButton(XpathMap.get(sliderPoint));
					Thread.sleep(1000);								
					String  valueAfter=GetConsoleText("return "+XpathMap.get(value));
					log.debug("Value after sliding: "+valueAfter);				
					if(valueBefore.equalsIgnoreCase(valueAfter) != true)
					{
						isSliderMoved =true;
						log.debug("slider is moved");
					}
				}catch (Exception e) 
				{
					log.debug("unable to move slider");
					log.error(e.getMessage(), e);
				}
				return isSliderMoved;
			}

			/**
			 * This method is used to refresh game
			 * @author pb61055
			 * @param report
			 * @param imageLibrary
			 * @param currencyName
			 */
			public void refreshGameAndClickContinueBtn(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName) 
			{
				try
				{
					//String gameurl = webdriver.getCurrentUrl();
					webdriver.navigate().refresh();
					log.debug("game Refresh  ");
					Thread.sleep(10000);
					if ("Yes".equalsIgnoreCase(XpathMap.get("NFD"))) {
						clickOnBaseSceneContinueButton(report, imageLibrary,currencyName);
					}
					Thread.sleep(2000);
					report.detailsAppendFolder("Verify Refresh", "After Refresh", "Ater Refresh", "Pass",currencyName);
				}catch (Exception e) 
				{
					log.error(e.getMessage(), e);
				}
			}
			
			/**
			 * This method is used to validate bet panel
			 * @author pb61055
			 * @param report
			 * @param imageLibrary
			 * @param currencyName
			 */
			public void verifyBetPanel(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName)
			{
				try
				{
					if(openBetPanel(report,imageLibrary,currencyName))
					{
						refreshGameAndClickContinueBtn( report, imageLibrary, currencyName);
					}
					
					if(openBetPanel(report,imageLibrary,currencyName))
					{
						verifyBetSliders(report, imageLibrary, currencyName);
						
						setMaxBet(report, imageLibrary, currencyName);
					}						
					
				} 
				catch (Exception e) 
				{
					log.debug("unable to bet panel");
					log.error(e.getMessage(), e);
				}
				
			}
			

			/**
			 * This method is used to validate set max bet
			 * @author pb61055
			 * @param report
			 * @param imageLibrary
			 * @param currencyName
			 */
			public void setMaxBet(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName)
			{
				try
				{
					if(imageLibrary.isImageAppears("BetMax"))
					{
						Thread.sleep(2000);
						imageLibrary.click("BetMax");
						Thread.sleep(2000);
						if(imageLibrary.isImageAppears("Spin"))
						{									
							report.detailsAppendFolder("Max Bet", "Click on Max Button", "Base Scene is visible", "Pass",currencyName );
						}
						else
						{
							report.detailsAppendFolder("Max Bet", "Click on Max Button", "Base Scene is visible not visible", "Fail",currencyName );
						}
					}
					
					
				} 
				catch (Exception e) 
				{
					log.debug("unable to bet panel");
					log.error(e.getMessage(), e);
				}
				
			}
			/**
			 * This method is used to verify quick spin
			 * @author pb61055
			 * @param report
			 * @param imageLibrary
			 * @param currencyName
			 */
			public void verifyQuickSpin(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName)
			{
				try
				{
					if(imageLibrary.isImageAppears("QuickSpinOn"))
						{
							//click on bet menu
							Thread.sleep(2000);
							imageLibrary.click("QuickSpinOn");
							Thread.sleep(2000);							
							if(imageLibrary.isImageAppears("QuickSpinOff"))
							{
								report.detailsAppendFolder("Quick spin Buttom ", "Quick Spin button should be clicked", "Quick Spin button is clicked", "Pass",currencyName);
							}
							else{
								report.detailsAppendFolder("Quick spin Buttom ", "Quick Spin button should be clicked", "unable to click on quick spin", "Fail",currencyName);
							}								
						}
						else {
							report.detailsAppendFolder("Quick spin Buttom ", "Quick Spin button should be available", "quick spin button is not visible", "Fail",currencyName);				
						}
						
					
				}catch (Exception e) 
				{
					log.error(e.getMessage(), e);
				}
			}
			
			/**
			 * This method is used to open Menu panel
			 * @author pb61055
			 * @param report
			 * @param imageLibrary
			 * @param currencyName
			 * @return
			 */
			public boolean openMenuPanel(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName) 
			{			
				boolean isMenuOpen=false;

				try {
					if(imageLibrary.isImageAppears("Menu"))						
					{								
						Thread.sleep(2000);
						imageLibrary.click("Menu");
						Thread.sleep(2000);
						report.detailsAppendFolder("Menu Open ", "Menu should open", "Menu is Opened", "Pass",currencyName);
						isMenuOpen=true;
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
					
				return isMenuOpen;
			} 
			
			/**
			 * This method is is used to verify paytable and paytable scroll
			 * @author pb61055
			 * @param report
			 * @param imageLibrary
			 * @param currencyName
			 */
			public void verifyPaytable(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName)
			{
				try
				{
					if(openPaytable(report,imageLibrary,currencyName))
					{
						refreshGameAndClickContinueBtn( report, imageLibrary, currencyName);
					}
					
					if(openPaytable(report,imageLibrary,currencyName))
					{
						if(closePaytable())
						{
							boolean scrollPaytable = paytableScroll(report, currencyName);
							if (scrollPaytable) 										
									report.detailsAppendFolder("PayTable ", "PayTable Scroll ", "PayTable Scroll", "Pass",currencyName);
							else
								report.detailsAppendFolder("PayTable ", "PayTable Scroll ", "PayTable Scroll", "Fail", currencyName);
								
							closePaytable();
							Thread.sleep(2000);
								if(imageLibrary.isImageAppears("Spin"))
									report.detailsAppendNoScreenshot("Verify Paytable Closed", "Paytable should be Closed", "Base Scene is visible after paytable closed", "Pass" );
								else
									report.detailsAppendFolder("Verify Paytable Closed", "Paytable should be Closed", "Paytable is not closed", "Fail", currencyName );
						}
						else
						{
							report.detailsAppendFolder("PayTable ", "PayTable should opened", "PayTable is not opened", "Fail",currencyName);
						}
					}
														
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
			
			/**
			 * This method is used to validate Settings panel
			 * @author pb61055
			 * @param report
			 * @param imageLibrary
			 * @param currencyName
			 */
			public void verifySettingsPanel(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName)
			{
				try
				{
					if(openSettingsPanel(report,imageLibrary,currencyName))
					{
						refreshGameAndClickContinueBtn( report, imageLibrary, currencyName);
					}
					
					if(openSettingsPanel(report,imageLibrary,currencyName))
					{
						if("Yes".equalsIgnoreCase(XpathMap.get("settingsUnderMenu"))) 
							closeButton(imageLibrary);
						else 
							imageLibrary.click("SettingsClose");							
						Thread.sleep(2000);	
						
						if(imageLibrary.isImageAppears("Spin"))
						{
							report.detailsAppendNoScreenshot("Base Scene after Settings closed ", "Base Scene after Settings closed", "We are on Base Scene after Settings closed", "Pass");	
						}
						else
						{
							report.detailsAppendFolder("Base Scene after Settings closed ", "Base Scene after Settings closed", "We are not on Base Scene after Settings closed", "Fail", currencyName);
						}		
					}
						
				} 
				catch (Exception e) 
				{
					log.error(e.getMessage(), e);
				}
				
			}		
			
			/**
			 * This method is used to validate Autoplay panel
			 * @author pb61055
			 * @param report
			 * @param imageLibrary
			 * @param currencyName
			 */
			public void verifyAutoplayPanel(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName)
			{
				try
				{
					if(openAutoplayPanel(report,imageLibrary,currencyName))
					{
						refreshGameAndClickContinueBtn( report, imageLibrary, currencyName);
					}
					
					if(openAutoplayPanel(report,imageLibrary,currencyName))
					{
						verifyAutoplayOptions(report,currencyName);
						
						startAutoplay(report,imageLibrary,currencyName);
						
						stopAutoplay(report,imageLibrary,currencyName);						
					}
					
	
				
				} 
				catch (Exception e) 
				{
					log.error(e.getMessage(), e);
				}
				
			}	
			/**
			 * This methid is used to verify autoplay options
			 * @author pb61055
			 * @param report
			 * @param currencyName
			 */
			public void verifyAutoplayOptions(Desktop_HTML_Report report,String currencyName)
			{
				try
				{
					if(XpathMap.get("SpinSliderPresent").equalsIgnoreCase("Yes"))
					{
						
						validateSpinsSliderAutoplay(report, "SpinSliderSet","SpinSliderValue",currencyName);
						Thread.sleep(2000);
					}
					
					if(XpathMap.get("TotalBetSliderPresent").equalsIgnoreCase("Yes"))
					{
						validateTotalBetSliderAutoplay(report, "TotalBetSliderSet","TotalBetSliderValue",currencyName);
						Thread.sleep(2000);		
					}
									
					if(XpathMap.get("WinLimitSliderPresent").equalsIgnoreCase("Yes"))
					{
						validateWinLimitSliderAutoplay(report, "WinLimitSliderSet","WinLimitSliderValue",currencyName);
						Thread.sleep(2000);		
					}
															
					if(XpathMap.get("LossLimitSliderPresent").equalsIgnoreCase("Yes"))
					{
						validateLossLimitSliderAutoplay(report, "LossLimitSliderSet","LossLimitSliderValue",currencyName);
						Thread.sleep(2000);
					}
					
					
					
			}catch (Exception e) 
				{
				log.debug("unable to verify autoplay options");
				log.error(e.getMessage(),e);
				
			}
			}
			
			/**
			 * This method is used to verify spin slider in autoplay
			 * @author pb61055
			 * @param report
			 * @param sliderPoint
			 * @param Value
			 * @param currencyName
			 */
			public void validateSpinsSliderAutoplay(Desktop_HTML_Report report,String sliderPoint,String Value,String currencyName)
			{
				try
				{
					boolean ableToSlide=verifySliderValue(sliderPoint,Value);
					if(ableToSlide)
					{
						report.detailsAppendFolder("Verify autoplay spins slider", "Autoplay spins slider should work", "Autoplay spins slider is working", "Pass",currencyName);
						log.debug("Autoplay spins slider is working");
					}
					else
					{
						report.detailsAppendFolder("Verify autoplay spins slider", "Autoplay spins slider should work", "Autoplay spins slider is not working", "Fail",currencyName);
						log.debug("Autoplay spins slider is not working");
					}
					
				}catch (Exception e) 
				{
					log.debug("unable to verify autoplay spins slider");
					log.error(e.getMessage(), e);
				}
			}
			
			/**
			 * This method is used to verify Total bet slider in autoplay
			 * @author pb61055
			 * @param report
			 * @param sliderPoint
			 * @param Value
			 * @param currencyName
			 */
			public void validateTotalBetSliderAutoplay(Desktop_HTML_Report report,String sliderPoint,String Value,String currencyName)
			{						
				try
				{
					boolean ableToSlide=verifySliderValue(sliderPoint,Value);
					if(ableToSlide)
					{
						report.detailsAppendFolder("Verify autoplay total bet slider", "Autoplay total bet slider should work", "Autoplay total bet slider is working", "Pass",currencyName);
						log.debug("Autoplay total bet slider is working");
					}
					else
					{
						report.detailsAppendFolder("Verify autoplay total bet slider", "Autoplay total bet slider should work", "Autoplay total bet slider is not working", "Fail",currencyName);
						log.debug("Autoplay total bet slider is not working");
					}
					
				}catch (Exception e) 
				{
					log.debug("unable to verify autoplay total bet slider");
					log.error(e.getMessage(), e);
				}
			}
			
			/**
			 * This method is used to verify Win limit in autoplay
			 * @author pb61055
			 * @param report
			 * @param sliderPoint
			 * @param Value
			 * @param currencyName
			 */
			public void validateWinLimitSliderAutoplay(Desktop_HTML_Report report,String sliderPoint,String Value,String currencyName)
			{						
				try
				{
					boolean ableToSlide=verifySliderValue(sliderPoint,Value);
					if(ableToSlide)
					{
						report.detailsAppendFolder("Verify autoplay win limit slider", "Autoplay win limit slider should work", "Autoplay win limit slider is working", "Pass",currencyName);
						log.debug("Autoplay win limit slider is working");
					}
					else
					{
						report.detailsAppendFolder("Verify autoplay win limit slider", "Autoplay win limit slider should work", "Autoplay win limit slider is not working", "Fail",currencyName);
						log.debug("Autoplay win limit slider is not working");
					}
					
				}catch (Exception e) 
				{
					log.debug("unable to verify autoplay win limit slider");
					log.error(e.getMessage(), e);
				}
			}
			
			/**
			 * This method is used to verify Loss limit in Autoplay
			 * @author pb61055
			 * @param report
			 * @param sliderPoint
			 * @param Value
			 * @param currencyName
			 */
			public void validateLossLimitSliderAutoplay(Desktop_HTML_Report report,String sliderPoint,String Value,String currencyName)
			{						
				try
				{
					boolean ableToSlide=verifySliderValue(sliderPoint,Value);
					if(ableToSlide)
					{
						report.detailsAppendFolder("Verify autoplay loss limit slider", "Autoplay loss limit slider Slider should work", "Autoplay loss limit slider Slider is working", "Pass",currencyName);
						log.debug("Autoplay loss limit slider is working");
					}
					else
					{
						report.detailsAppendFolder("Verify autoplay loss limit slider", "Autoplay loss limit slider Slider should work", "Autoplay loss limit slider Slider is not working", "Fail",currencyName);
						log.debug("Autoplay loss limit slider is not working");
					}
					
				}catch (Exception e) 
				{
					log.debug("unable to verify autoplay loss limit slider");
					log.error(e.getMessage(), e);
				}
			}
			
			/**
			 * This method is used to click on start autoplay
			 * @author pb61055
			 * @param report
			 * @param imageLibrary
			 * @param currencyName
			 */
			public void startAutoplay(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName)
			{
				try
				{
					if(imageLibrary.isImageAppears("AutoplayStart"))
					{
						imageLibrary.click("AutoplayStart");
						Thread.sleep(5000);
						
						if(imageLibrary.isImageAppears("AutoplayStop"))
							report.detailsAppendFolder("Autoplay", "Autoplay should work", "Autoplay is working", "Pass",currencyName);
						else
							report.detailsAppendFolder("Autoplay", "Autoplay should work", "Autoplay is not working", "Fail",currencyName);
					}
					else 
					{
						report.detailsAppendFolder("Autoplay", "Start Autoplay Button visibility", "Start Autoplay Button is not Visible", "Fail",currencyName);
					}	
				}	
				catch(Exception e)
				{
					log.error(e.getMessage(),e);
				}
			}
			
			/**
			 * This method is used to stop autoplay spins
			 * @author pb61055
			 * @param report
			 * @param imageLibrary
			 * @param currencyName
			 */
			public void stopAutoplay(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName)
			{
				try
				{
					if(imageLibrary.isImageAppears("AutoplayStop"))
					{
						imageLibrary.click("AutoplayStop");
						Thread.sleep(3000);
						if(imageLibrary.isImageAppears("Spin"))	
							report.detailsAppendFolder("Autoplay", "Autoplay Stop should work", "Autoplay is stopped", "Pass",currencyName);
						else
							report.detailsAppendFolder("Autoplay", "Autoplay Stop should work", "Autoplay is did not stop", "Fail",currencyName);
					}
					else 
					{
						report.detailsAppendFolder("Autoplay", "Stop Autoplay Button visibility", "Start Autoplay Button is not Visible", "Fail",currencyName);
					}	
				}	
				catch(Exception e)
				{
					log.error(e.getMessage(),e);
				}
			}
			
			/**
			 * This method is used to validate paytable payout currency format
			 * @author pb61055
			 * @param report
			 * @param imageLibrary
			 * @param currencyName
			 * @param regExpr
			 */
			public void validatePaytablePayoutsCurrencyFormat(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName,String regExpr)
			{
				try
				{
					openPaytable(report,imageLibrary,currencyName);
					if(closePaytable())
					{
						boolean scrollPaytable = paytableScroll(report, currencyName);
						if (scrollPaytable) 										
								report.detailsAppendFolder("PayTable ", "PayTable Scroll ", "PayTable Scroll", "Pass",currencyName);
						else
							report.detailsAppendFolder("PayTable ", "PayTable Scroll ", "PayTable Scroll", "Fail", currencyName);
							
						boolean scatterAndWildPayouts = validatePayoutsFromPaytable(report,currencyName, regExpr);
						boolean symbolPayouts=verifyGridPayouts(report,regExpr,currencyName);
						if (scatterAndWildPayouts&&symbolPayouts) 
						{
							report.detailsAppendNoScreenshot(
									"Verify Paytable payout currency format for selected bet value with the game currency format",
									"Paytable payout verification with the game currency format ",
									"Paytbale payout verification  with the game currency format is done and is correct",
									"Pass");
							log.debug("Paytable currency format: Pass");
						
						} else {
							report.detailsAppendNoScreenshot(
									"Verify Paytable payout currency format for selected bet value with the game currency format",
									"Paytable payout verification with the game currency format ",
									"Paytable payout verification with the game currency format is done but Failed coz some formats are not matched",
									"Fail");
							log.debug("Paytable currency format: Fail");
						}
						
						closePaytable();
					}
					
				}catch(Exception e)
				{
					log.error(e.getMessage(),e);
				}
			}

			/**
			 * This method is used to check Progressive Bars
			 * @author pb61055
			 * @param report
			 * @param imageLibrary
			 * @param currencyName
			 */
			public void verifyProgressiveBar(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName,String regExpr)
			{
				try 
				{
					if(XpathMap.get("isProgressiveBarAvailable").equalsIgnoreCase("Yes")) 
					{
						report.detailsAppend("Following are the Progressive bar test cases", "Verify Progressive bar", "", "");			
					if (isElementVisible("isProgressiveBar1")) {								
						Thread.sleep(2000);
						
						// check if spin is successful,take screenshot
						report.detailsAppendFolder("verify Progressive Bar1",
								" Progressive Bar1", " Progressive Bar1 visible", "Pass",currencyName);
						boolean grandValue = verifyRegularExpression(report, regExpr,
								getConsoleText("return " + XpathMap.get("ProgressiveBar1Value")));
						if (grandValue) {									
							report.detailsAppendFolder("verify Progressive Bar1 Value", " Progressive Bar1 Value",
									"Progressive Bar1 Value", "Pass",currencyName);
						} else {								
							report.detailsAppendFolder("verify Progressive Bar1 value", " Progressive Bar1 Value",
									"Progressive Bar1 Value", "Fail",currencyName);
						}										
					} else {
					
						report.detailsAppendFolder("verify Progressive Bar 1",
								"Progressive Bar 1 ", "Progressive  Bar1 not visible", "Fail", currencyName);
					}							
					
					if (isElementVisible("isProgressiveBar2")) {								
						Thread.sleep(2000);
						
						// check if spin is successful,take screenshot
						report.detailsAppendFolder("verify verify Progressive Bar 2 ",
								" Progressive Bar2", " Progressive Bar2 is visible", "Pass",currencyName);
						boolean grandValue = verifyRegularExpression(report, regExpr,
								getConsoleText("return " + XpathMap.get("ProgressiveBar2Value")));
						if (grandValue) {									
							report.detailsAppendFolder("verify Progressive Bar2 Value", " Progressive Bar2 value",
									"Progressive Bar2 Value", "Pass",currencyName);
						} else {									
							report.detailsAppendFolder("verify Progressive Bar2 Value", " Progressive Bar2 Value",
									"Progressive Bar2 Value", "Fail",currencyName);
						}		
						
					} else {
					
						report.detailsAppendFolder("verify Progressive Bar2","Progressive Bar2", "Progressive Bar2 not visible", "Fail",currencyName);

					}
					
					if (isElementVisible("isProgressiveBar3")) {								
						Thread.sleep(2000);
						
						// check if spin is successful,take screenshot
						report.detailsAppendFolder("verify Progressive Bar3",
								"Progressive Bar3", "Progressive Bar3 is visible", "Pass",currencyName);
						boolean grandValue = verifyRegularExpression(report, regExpr,
								getConsoleText("return " + XpathMap.get("ProgressiveBar3Value")));
						if (grandValue) {
						
							report.detailsAppendFolder("verifyProgressive Bar3 Value", "verify Progressive Bar3 Value",
									"verify Progressive Bar3 Value", "Pass",currencyName);
						} else {								
							report.detailsAppendFolder("verify verify Progressive Bar3 value", " verify Progressive Bar3 Value",
									"verify Progressive Bar3 Value", "Fail",currencyName);
						}		
						
					} else {
					
						report.detailsAppendFolder("verify verify Progressive Bar3","verify Progressive Bar3 ", "verify Progressive Bar3 not visible", "Fail", currencyName);	
					}
				} 
				}catch (Exception e) 
				{
					log.error(e.getMessage(), e);
				}					
				
			}
			
			/**
			 * This method is used to validate normal win amount
			 * @author pb61055
			 * @param report
			 * @param imageLibrary
			 * @param currencyName
			 * @param regExpr
			 */
			public void verifyNormalWinCurrencyFormat(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName,String regExpr)
			{
				// method is used to Spin the Spin Button (Normal Win)
				try {
					if (imageLibrary.isImageAppears("Spin")) {
						Thread.sleep(2000);
						// click on spin button
						imageLibrary.click("Spin");
						Thread.sleep(5000);
						// check if spin is successful,take screenshot
						//report.detailsAppendFolder("verify BaseGame normal win","Spin Button is working", "Spin Button is working", "Pass",currencyName);
						// method is used to get the Win amount and check the currency format
						
						/*boolean winFormatVerification = cfnlib.verifyRegularExpression(report, regExpr,
								cfnlib.getCurrentWinAmt(report, currencyName));*/
						
						
						boolean winFormatVerification = verifyRegularExpression(report, regExpr,
								getCurrentWinAmtImg(report,imageLibrary, currencyName));
						
						
						if (winFormatVerification) {
							System.out.println("Base Game Win Value : Pass");
							log.debug("Base Game Win Value : Pass");
							report.detailsAppendFolder("verify Base Game win amt", " Win Amt",
									"Win Amt", "Pass",currencyName);
						} else {
							System.out.println("Base Game Win Value : Fail");
							log.debug("Base Game Win Value : Fail");
							report.detailsAppendFolder("verify Base Game win amt", " Win Amt",
									"Win Amt", "Fail",currencyName);
						}
					} else {
						
						report.detailsAppendFolder("verify BaseGame normal win",
								"Spin Button is not working", "Spin Button is not working", "Fail",
								"" + currencyName);
					}
					Thread.sleep(5000);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
			
			/**
			 * This method is used to validate big win amount
			 * @author pb61055
			 * @param report
			 * @param imageLibrary
			 * @param currencyName
			 * @param regExpr
			 */
			public void verifyBigWinCurrencyFormat(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName,String regExpr)
			{
				// method is used to Spin the Spin Button (Big Win)
				try {
					if (imageLibrary.isImageAppears("Spin")) {
						imageLibrary.click("Spin");
						Thread.sleep(5000);
						// Verifies the Big Win currency format
						/*boolean bigWinFormatVerification = cfnlib.verifyRegularExpression(report,
								regExpr, cfnlib.verifyBigWinImg(report, imageLibrary));*/
						
						
						boolean bigWinFormatVerification = verifyRegularExpression(report,
								regExpr, verifyBigWinImg(report, imageLibrary,currencyName));
						
						
						if (bigWinFormatVerification) {
							System.out.println("Base Game BigWin Value : Pass");
							log.debug("Base Game BigWin Value : Pass");
							report.detailsAppendFolder("verify Base Game big win", "Big Win Amt",
									"Big Win Amt", "Pass",currencyName);
						} else {
							System.out.println("Base Game BigWin Value : Fail");
							log.debug("Base Game BigWin Value : Fail");
							report.detailsAppendFolder("verify Base Game big win", " Big Win Amt",
									"Big Win Amt", "Fail",currencyName);
						}
					}
					Thread.sleep(10000);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}

			}
			
			/**
			 * This method is used to validate Bonus/Amazing win amount
			 * @author pb61055
			 * @param report
			 * @param imageLibrary
			 * @param currencyName
			 * @param regExpr
			 */
			public void verifyBonusWinCurrencyFormat(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName,String regExpr)
			{
				try {
					if (imageLibrary.isImageAppears("Spin")) {
					
						// click on spin button
						imageLibrary.click("Spin");
						Thread.sleep(8000);
						// method is used to get the Win amt and check the currency format

						boolean winVerification = verifyRegularExpression(report, regExpr,
								verifyAmgWinImg(report, imageLibrary));
						if (winVerification) {
							System.out.println("Base Game Bonus win Value : Pass");
							log.debug("Base Game Win Value : Pass");
							report.detailsAppendFolder("verify Base Game Bonus win amt", "Bonus Win Amt",
									"Bonus Win Amt", "Pass",currencyName);
						} else {
							System.out.println("Base Game Bonus Win Value : Fail");
							log.debug("Base Game Win Value : Fail");
							report.detailsAppendFolder("verify Base Game Bonus win amt", " Win Amt",
									"Bonus Win Amt", "Fail",currencyName);
						}
					
					} else {
					
								report.detailsAppendFolder("verify BaseGame Amazing win",
								"Spin Button is not working", "Spin Button is not working", "Fail",
								currencyName);
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
			
			/**
			 * This method is used to verify Freespins currency format
			 * @author pb61055
			 * @param report
			 * @param imageLibrary
			 * @param currencyName
			 * @param regExpr
			 */
			public void verifyFreeSpinsCurrencyFormat(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName,String regExpr)
			{
				try
				{
					// click on spin button
					imageLibrary.click("Spin");			
					
					if(XpathMap.get("tapToContinueFreespinsAvailable").equalsIgnoreCase("Yes"))
					{
						//cfnlib.waitForElement("tapToContinueFreespins");
						if(XpathMap.get("isFSHaveMultipleOptions").equalsIgnoreCase("Yes"))
						{
							imageLibrary.click("tapToFSOption1");
							Thread.sleep(3000);
						}
						imageLibrary.click("tapToContinueFreespins");
					}
												
					Thread.sleep(10000);							
					// Free spins  1St Spins to get correct Win
					boolean winFormatVerificationINFS = verifyRegularExpression(report,regExpr, verifyFSCurrentWinAmt(report, currencyName));
					if (winFormatVerificationINFS) {
						System.out.println("Free Games Win Value : Pass");
						log.debug("Free Games Win Value : Pass");
						report.detailsAppendFolder("Free Spins", "Win Amt", "Win Amt", "Pass",currencyName);
					} else {
						System.out.println("Free Games Win Value : Fail");
						log.debug("Free Games Win Value : Fail");
						report.detailsAppendFolder("Free Spins", "Win Amt", "Win Amt", "Fail",currencyName);
					}					
					//Thread.sleep(10000);
					// method is used to get the current big win and check the currency format
					boolean bigWinFormatVerificationINFS = verifyRegularExpression(report,regExpr, verifyFreeSpinBigWin(report, currencyName));
					if (bigWinFormatVerificationINFS) {
						System.out.println("Free Spins BigWin Value : Pass");
						log.debug("Free Spins BigWin Value : Pass");
						report.detailsAppendFolder("Free Spins", "Big Win Amt", "Big Win Amt","Pass",currencyName);
					} else {
						System.out.println("Free Spins BigWin Value : Fail");
						log.debug("Free Spins BigWin Value : Fail");
						report.detailsAppendFolder("Free Spins", "Big Win Amt", "Big Win Amt","Fail",currencyName);
					}
					
					Thread.sleep(15000);
				/*	// method is used to get the current Amazing win and check the currency format
					  String bonus =cfnlib.func_GetText("FSBonusWin");
	                  System.out.println("Free Spins bonus win value : Pass" +bonus);
					boolean amzWinFormatVerificationINFS = cfnlib.verifyRegularExpression(report,regExpr, cfnlib.func_GetText("FSBonusWin"));
					if (amzWinFormatVerificationINFS) {
						System.out.println("Free Spins Amazing Value : Pass");
						log.debug("Free Spins Amazing Value : Pass");
						report.detailsAppendFolder("Free Spins", "Amazing Win Amt", "Amazing Win Amt","Pass",currencyName);
					} else {
						System.out.println("Free Spins BigWin Value : Fail");
						log.debug("Free Spins BigWin Value : Fail");
						report.detailsAppendFolder("Free Spins", "Amazing Win Amt", "Amazing Win Amt","Fail",currencyName);
					}*/
					String cred =func_GetText("FSBalanceText");
					System.out.println("Credit Value : "+cred);
						// method is used to get the current credit and check the currency format
						boolean bonusCredits = verifyRegularExpression(report, regExpr,
								func_GetText("FSBalanceText"));
						if (bonusCredits) {
							
							report.detailsAppendFolder("Free Spins", "Credit Amt", "Credit Amt", "Pass",currencyName);
						} else {
							
							report.detailsAppendFolder("Free Spins", "Credit Amt", "Credit Amt", "Fail",currencyName);
						}
					
					// method is used to get the current total win and check the currency format
					String totalFs =func_GetText("totalWinInFS");
                    System.out.println("Free Spins Total win value : Pass" +totalFs);
					boolean bonusBetAmt = verifyRegularExpression(report, regExpr,
							func_GetText("totalWinInFS"));
					if (bonusBetAmt) {
						System.out.println("Free Spins total Win Value : Pass");
						log.debug("Free Spins Win Value : Pass");
						report.detailsAppendFolder("Free Spins", "Total Win Amt", "Total Win Amt","Pass",currencyName);
					} else {
						System.out.println("Free Spins Win Value : Fail");
						log.debug("Free Spins Win Value : Fail");
						report.detailsAppendFolder("Free Spins", "Total Win Amt", "Total Win Amt","Fail",currencyName);
					}
				
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			}
			
			/**
			 * This method is used to verify Free spins summary currency format
			 * @author pb61055
			 * @param report
			 * @param imageLibrary
			 * @param currencyName
			 * @param regExpr
			 */
			public void verifyFreeSpinsSummaryCurrencyFormat(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName,String regExpr)
			{
				try
				{
					if(waitTillFreespinSummary(report,imageLibrary,currencyName)) 
					{
						boolean fsSummaryScreen = verifyRegularExpression(report,
								regExpr,func_GetText("FSSummaryScreenWinAmt"));
						if (fsSummaryScreen) {
							System.out.println("Free Spins Summary Screen : Pass");
							log.debug("Free Spins Summary Screen : Pass");
							report.detailsAppendFolder("Free Spins", " Summary Screen"," Summary Screen", "Pass",currencyName);
						} else {
							System.out.println("Free Spins Summary Screen : Fail");
							log.debug("Free Spins Summary Screen : Fail");
							report.detailsAppendFolder("Free Spins", " Summary Screen"," Summary Screen", "Fail",currencyName);
						}
					} 
				}catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
			
			/**
			 * This method is used to close freespins
			 * @author pb61055
			 * @param report
			 * @param imageLibrary
			 * @param currencyName
			 */
			public void closeFreeSpins(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName)
			{
				try
				{
					if (imageLibrary.isImageAppears("FSSummaryContinueBtn")) {
						Thread.sleep(2000);
						// click on spin button
						imageLibrary.click("FSSummaryContinueBtn");
						Thread.sleep(3000);
						if (imageLibrary.isImageAppears("Spin")) {		
							report.detailsAppendFolder("Verify FS summary screen click", "FS summary screen click"," FS summary screen clicked", "Pass",currencyName);
						} else {
							System.out.println("Free Spins Summary Screen : Fail");
							log.debug("Free Spins Summary Screen : Fail");
							report.detailsAppendFolder("Verify FS summary screen click", "FS summary screen click"," FS summary screen not clicked","Fail",currencyName);
						}
					} // Closing Summary Screen
					
				}catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
			
			/**
			 * This method is used to verify Free games entry screen and free games info text currency format
			 * @author pb61055
			 * @param report
			 * @param imageLibrary
			 * @param currencyName
			 * @param regExpr
			 */
			public void verifyFreeGamesEntryScreen(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName,String regExpr)
			{
				try
				{
					if(imageLibrary.isImageAppears("FreeGamesPlayLater"))
					{	
						report.detailsAppendFolder("Verify Game launched ", "Game should be launched and FG entry screen is visible", "Free Games Entry screen is visible", "Pass",currencyName);
					}
					else
					{
						report.detailsAppendFolder("Verify Game launchaed ", "Game should be launched and FG entry screen is visible", "Free Games Entry screen is not visible", "Fail",currencyName);
					}	
					Thread.sleep(3000);
					
					boolean isfreeGameEntryInfoVisible = verifyRegularExpression(report,regExpr,freeGameEntryInfo(report,regExpr,imageLibrary,"FreeGamesInfoText"));
					if (isfreeGameEntryInfoVisible)
					{
						System.out.println("Free Games Entry Screen Info Icon Text Validation : Pass");
						log.debug("Free Games Entry Screen Info Icon Text Validation : Pass");
						report.detailsAppendFolder("Free Games"," Entry Screen Info Text Validation"," Entry Screen Info Text Validation", "Pass",currencyName);
					}
					else
					{
						System.out.println("Free Games Entry Screen Info Icon Text Validation : Fail");
						log.debug("Free Games Entry Screen Info Icon Text Validation : Fail");
						report.detailsAppendFolder("Free Games"," Entry Screen Info Text Validation","Free Games Entry Screen Info", "Fail",currencyName);
					}
					Thread.sleep(2000);
				}catch (Exception e) {
					log.error(e.getMessage(), e);
				}
				
			}
			
			/**
			 * This method is used to verify and click on free games play later button
			 * @author pb61055
			 * @param report
			 * @param imageLibrary
			 * @param currencyName
			 */
			public void freeGamesPlayLater(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName)
			{
				try
				{
					if(imageLibrary.isImageAppears("FreeGamesPlayLater"))
					{
						report.detailsAppendFolder("Verify FG Play Later button visible", "Play Later button Should be visible", "FG Play Later button is visible", "Pass",currencyName);
						Thread.sleep(2000);
						imageLibrary.click("FreeGamesPlayLater");					
						Thread.sleep(2000);
					}
					else
					{
						report.detailsAppendFolder("Verify FG Play Later button visible", "Play Later button Should be visible", "FG Play Later button is not visible", "Fail",currencyName);
					}
				}catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
			
			/**
			 * This method is used to verify and click on free games play now button
			 * @author pb61055
			 * @param report
			 * @param imageLibrary
			 * @param currencyName
			 */
			public void freeGamesPlayNow(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName)
			{
				try
				{
					if(imageLibrary.isImageAppears("FreeGamesPlayNow"))
					{
						report.detailsAppendFolder("Verify FG Play Now button visible", "Play Now button Should be visible", "FG Play Now button is visible", "Pass",currencyName);
						Thread.sleep(2000);
						imageLibrary.click("FreeGamesPlayNow");					
						Thread.sleep(2000);
					}
					else
					{
						report.detailsAppendFolder("Verify FG Play Now button visible", "Play Now button Should be visible", "FG Play Now button is not visible", "Fail",currencyName);
						
					}
				}catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}

			/**
			 * This method is used to verify delete button on FG's entry screen
			 * @author pb61055
			 * @param report
			 * @param imageLibrary
			 * @param currencyName
			 */
			public void freeGamesDeleteEntryScreen(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName)
			{
				try
				{
					if(imageLibrary.isImageAppears("FreeGamesPlayNow"))
					{	
						report.detailsAppendFolder("Verify Game launchaed after refresh to check Discard FG Offer", "Game should be launched", "Game is launched", "Pass",currencyName);										
						if(imageLibrary.isImageAppears("FGDelete"))	
						{
							report.detailsAppendFolder("Verify FG Discard offer Button visible", "FG Discard offer Button should be visible", "FG Discard offer Button is visible", "Pass",currencyName);
							imageLibrary.click("FGDelete");
							
							Thread.sleep(1000);											
							if(imageLibrary.isImageAppears("FGDiscard"))	
							{	
								report.detailsAppendFolder("Verify FG Discard page visible", "FG Discard page should be visible", "Discard page is visible", "Pass",currencyName);
								imageLibrary.click("FGDiscard");								
								Thread.sleep(5000);
							}
							else
							{
								report.detailsAppendFolder("VerifyFG Discard page visible ", "FG Discard page should be visible", "Discard page is not visible", "Fail",currencyName);
							}												
						}
						else
						{
							report.detailsAppendFolder("Verify FG Discard offer Button visible", "FG Discard offer Button should be visible", "FG Delete offer Button is not visible", "Fail",currencyName);
						}										
					}
					else
					{
						report.detailsAppendFolder("Verify Game launchaed after refresh to check Discard FG Offer ", "Game should be launched", "Game is not launched", "Fail",currencyName);
					}
				}catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
			
			/**
			 * This method is used verify FG big win amount
			 * @author pb61055
			 * @param report
			 * @param imageLibrary
			 * @param currencyName
			 * @param regExpr
			 */
			public void verifyFGBigWinCurrencyFormat(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName,String regExpr)
			{
				try {													
					// method is used to Spin the Spin Button (Big Win)
					try {
						if(imageLibrary.isImageAppears("Spin")) {
							Thread.sleep(3000);
							imageLibrary.click("Spin");
							Thread.sleep(20000);
							// Verifies the Big Win currency format
							boolean bigWinFormatVerification = verifyRegularExpression(report, 
									regExpr, verifyFGBigWin(report,imageLibrary));
							
							
							if (bigWinFormatVerification) {
								System.out.println("Base Game BigWin Value : Pass");
								log.debug("Base Game BigWin Value : Pass");
								report.detailsAppendFolder("verify Base Game big win", "Big Win Amt",
										"Big Win Amt", "Pass",currencyName);
							} else {
								System.out.println("Base Game BigWin Value : Fail");
								log.debug("Base Game BigWin Value : Fail");
								report.detailsAppendFolder("verify Base Game big win", " Big Win Amt",
										"Big Win Amt", "Fail",currencyName);
							}

						}
					} catch (Exception e) {
						log.error(e.getMessage(), e);
					}
					
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
			
			/**
			 * This method is used verify FG Resume button
			 * @author pb61055
			 * @param report
			 * @param imageLibrary
			 * @param currencyName
			 * @param regExpr
			 */
			public void verifyFGResumeScreen(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName,String regExpr)
			{
				try
				{
					 if(imageLibrary.isImageAppears("FGResumePlay"))
                     {
                     
                             report.detailsAppendFolder("Free Game", "On Refresh FG Info Text Validation", "On Refresh FG Info Text Validation ", "Pass",currencyName);                                        
                     }
                     else
                     {
                             report.detailsAppendFolder("Free Game", "On Refresh FG Info Text Validation", "On Refresh FG Info Text Validation ", "Fail",currencyName);
                                                               
                     }
					 Thread.sleep(2000);
					
					
					
				/*	boolean freeGamesOnRefresh =verifyRegularExpression(report,regExpr,freeGameOnRefresh(report,currencyName,"isFGResumeBtnVisible","isFGResumeInfoBtnVisible","FGResumeInfoBtnx","FGResumeInfoBtny","FGinfoTxt",imageLibrary));
					if(freeGamesOnRefresh )
					{
						report.detailsAppendFolder("Free Game", "On Refresh FG Info Text Validation", "On Refresh FG Info Text Validation ", "Pass",currencyName);					
					}
					else
					{
						report.detailsAppendFolder("Free Game", "On Refresh FG Info Text Validation", "On Refresh FG Info Text Validation ", "Fail",currencyName);
					}
					Thread.sleep(2000);		*/
					
					                                                

					 imageLibrary.click("FGResumePlay");			 
					 
					 if ("Yes".equalsIgnoreCase(XpathMap.get("NFD"))) {
							clickOnBaseSceneContinueButton(report, imageLibrary,currencyName);
						}
							
				
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
			
			/**
			 * This method is used verify FG Summary screen currency format
			 * @author pb61055
			 * @param report
			 * @param imageLibrary
			 * @param currencyName
			 * @param regExpr
			 */
			public void verifyFGSummaryScreen(Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName,String regExpr)
			{
				try
				{
					SpinUntilFGSummary("FGSummaryBackToGameBtn", imageLibrary);
					boolean FGSummaryWonAmount = verifyRegularExpression(report,regExpr,getConsoleText("return "+XpathMap.get("FGSummaryWonAmount")));
					if(FGSummaryWonAmount)
					{
						log.debug("Free Game Summary won Amount : Pass");
						report.detailsAppendFolder("Free Game ", "credit Summary won Amount", "credit Summary won Amount", "Pass",currencyName);
					}
					else
					{
						log.debug("Free  Game Summary won Amount : Fail");
						report.detailsAppendFolder("Free Game ", "credit Summary won Amount", "credit Summary won Amount", "Fail",currencyName);
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
			
			/**
			 * This method is used to check FG offer Screen
			 * @author sp68146
			 * @param report
			 * @param imageLibrary
			 * @param languageCode
			 * @throws InterruptedException 
			 */
			public boolean fGAssignment(Desktop_HTML_Report report,ImageLibrary imageLibrary,String languageCode) throws InterruptedException {
				boolean isFgAssigned=false;
				try {
					Thread.sleep(60000);
					webdriver.navigate().refresh();
					Thread.sleep(7000);
					
					if(imageLibrary.isImageAppears("FreeGamesPlayNow"))
					{
						Thread.sleep(2000);
						report.detailsAppendFolder("Verify FG Screen visibility", "ensure Play now, Play Later, Delete icon are displayed","Landed on FG offer screen","Pass", languageCode);
						isFgAssigned=true;
						Thread.sleep(2000);				
					}
					else {
						report.detailsAppendFolder("Verify FG Screen visibility", "ensure Play now, Play Later, Delete icon are displayed","Not landed on FG offer screen","Fail", languageCode);
						
					}
					
					}
					catch (Exception e) {
						e.getMessage();
						log.error(e);
					}
				return isFgAssigned;
			}
			
			
			public void fGPlayLater(Desktop_HTML_Report report,ImageLibrary imageLibrary,String languageCode) throws InterruptedException {
				try {
					imageLibrary.click("FreeGamesPlayLater");
					Thread.sleep(2000);
					if(XpathMap.get("NFD").equalsIgnoreCase("Yes"))
					{
						
						//check for visiblity of nfd button and take screenshot
						Thread.sleep(3000);
						if(imageLibrary.isImageAppears("NFDButton"))
						{							
							imageLibrary.click("NFDButton");
							Thread.sleep(3000);
							report.detailsAppendFolder("Verify if able to click on continue after clicking on Play Later option",
									"Should be able to click on continue button", "Able to click on continue button",
									"Pass", languageCode);														
				  
							if(imageLibrary.isImageAppears("Spin"))
							{
								System.out.println("Base Scene after FG Play Later");log.debug("Base Scene after FG Play Later");
								report.detailsAppendFolder("Verify Base Scene after FG Play Later", "Base should be visible", "Base Scene is visible", "Pass",languageCode);
							}
							else
							{
								System.out.println("Base Scene after FG Play Later is not visible");log.debug("Base Scene after FG Play Later is not visible");
								report.detailsAppendFolder("Verify Base Scene after FG Play Later", "Base should be visible", "Base Scene is not visible", "Fail", languageCode);
							}
						}
						else
						{
							report.detailsAppendFolder("Verify Continue button is visible after FG Play Later ", "Continue buttion should be visible", "Continue button is not visible", "Fail", languageCode);
						}
					}
					else
					{							
						if(imageLibrary.isImageAppears("Spin"))
						{
							System.out.println("Base Scene after FG Play Later");log.debug("Base Scene after FG Play Later");
							report.detailsAppendFolder("Verify Base Scene after FG Play Later", "Base is visible", "Free Game Scene is visible", "Pass", languageCode);
						}
						else
						{
							System.out.println("Base Scene after FG Play Later is not visible");log.debug("Base Scene after FG Play Later is not visible");
							report.detailsAppendFolder("Verify Base Scene after FG Play Later", "Base is visible", "Free Game Scene is not visible", "Fail", languageCode);
						}																									
					}
				}
				catch (Exception e) {
					e.getMessage();
					log.error(e);
				}	}
				
			public void FGOfferDelete(Desktop_HTML_Report report,ImageLibrary imageLibrary,String languageCode) throws InterruptedException {
				
				try {
					RefreshGame("clock");
					Thread.sleep(15000);									
					if(imageLibrary.isImageAppears("FreeGamesPlayNow"))
					{	
						report.detailsAppendFolder("Verify Game launchaed after refresh to check Discard FG Offer", "Game should be launched", "Game is launched", "Pass",languageCode);										
						if(imageLibrary.isImageAppears("FGDelete"))	
						{
							report.detailsAppendFolder("Verify FG Discard offer Button visible", "FG Discard offer Button should be visible", "FG Discard offer Button is visible", "Pass",languageCode);
							imageLibrary.click("FGDelete");
							
							Thread.sleep(1000);											
							if(imageLibrary.isImageAppears("FGDiscard"))	
							{	
								report.detailsAppendFolder("Verify FG Discard page visible", "FG Discard page should be visible", "Discard page is visible", "Pass",languageCode);
								imageLibrary.click("FGDiscard");
								
								Thread.sleep(1000);
								
								if(XpathMap.get("NFD").equalsIgnoreCase("Yes"))
								{
								
									clickOnBaseSceneContinueButton(report,imageLibrary, languageCode);
								}
								
								else
								{				
									Thread.sleep(2000);
							
									if(imageLibrary.isImageAppears("Spin"))
									{
										System.out.println("Base Scene after FG Play Later");log.debug("Base Scene after FG Play Later");
										report.detailsAppendFolder("Verify Base Scene after discarding FG", "Base is visible", "Free Game Scene is visible", "Pass",languageCode);
									}
									else
									{
										System.out.println("Base Scene after FG Play Later is not visible");log.debug("Base Scene after FG Play Later is not visible");
										report.detailsAppendFolder("Verify Base Scene after discarding FG", "Base is visible", "Free Game Scene is not visible", "Fail",languageCode);
									}																									
								}	
								
							}	
							else
							{
								report.detailsAppendFolder("VerifyFG Discard page visible ", "FG Discard page should be visible", "Discard page is not visible", "Fail",languageCode);
							}												
						}
						else
						{
							report.detailsAppendFolder("Verify FG Discard offer Button visible", "FG Discard offer Button should be visible", "FG Delete offer Button is not visible", "Fail",languageCode);
						}										
					}
					else
					{
						report.detailsAppendFolder("Verify Game launchaed after refresh to check Discard FG Offer ", "Game should be launched", "Game is not launched", "Fail",languageCode);
					}
					
				}catch(Exception e){
					e.getMessage();
					log.error(e);
					}					
			}
			
			public void FGOfferAcceptAndResumeFG(Desktop_HTML_Report report,ImageLibrary imageLibrary,String languageCode, String FGOfferResume, String FGSummaryScreen) throws InterruptedException {
				try {
				Thread.sleep(30000);
				webdriver.navigate().refresh();
				if (imageLibrary.isImageAppears("FreeGamesPlayNow")) {
					imageLibrary.click("FreeGamesPlayNow");
					Thread.sleep(3000);
					if (XpathMap.get("NFDHasHook").equalsIgnoreCase("Yes")) {
						clickOnBaseSceneContinueButton(report, imageLibrary, languageCode);
					}
					if (imageLibrary.isImageAppears("Spin")) {
						System.out.println("Free Game Scene is visible");
						log.debug("Free Game Scene is visible");
						report.detailsAppendFolder("Verify Free Game Scene", "Free Game Scene is visible",
								"Free Game Scene is visible", "Pass", languageCode);
						String creditBeforeSpin = getConsoleText("return " + XpathMap.get("CreditValue"));
						double dblNetPositionBeforeSpin = Double.parseDouble(creditBeforeSpin.replaceAll("[^0-9.-]", "").replace(".", ""));
						System.out.println("Credit value/Net Position before FG spin " + creditBeforeSpin);
						imageLibrary.click("Spin");
						Thread.sleep(4000);
						String creditAfterSpin = getConsoleText("return " + XpathMap.get("CreditValue"));
						double dblNetPositionAfterSpin = Double.parseDouble(creditAfterSpin.replaceAll("[^0-9.-]", "").replace(".", ""));
						if (dblNetPositionBeforeSpin<=dblNetPositionAfterSpin) {
							report.detailsAppendFolder("verify FG Net position ", "Credit value/Net position should not be deducted in FG",
									"Net Position is not deducted ", "pass",languageCode);
						} else {
							report.detailsAppendFolder("verify FG Net position ", "Credit value/Net position should not be deducted in FG",
									"Net Position is deducted ", "fail",languageCode);
						}
						Thread.sleep(2000);
						// check if spin is successful,take screenshot
						report.detailsAppendNoScreenshot("verify FG Spin 1", "Spin Button is working", "Spin Button is working",
								"Pass");

						System.out.println("Net Position after FG spin " + creditAfterSpin);
						imageLibrary.click("Spin");
						Thread.sleep(5000);
						// check if spin is successful,take screenshot
						report.detailsAppendFolder("verify FG Spin 2", "Spin Button is working", "Spin Button is working",
								"Pass",languageCode);

						//// *************************Resume FG scenario********************
						if (FGOfferResume.equalsIgnoreCase("Yes")) {
							webdriver.navigate().refresh();
							Thread.sleep(10000);
							if (imageLibrary.isImageAppears("FGResumePlay")) {
								report.detailsAppendFolder("Verify Free Game Resume Button",
										"Free Game Resume button should be visible", "Free Game Resume button is visible",
										"Pass",languageCode);
								if (FGSummaryScreen.equalsIgnoreCase("Yes")) {

									imageLibrary.click("FGResumePlay");
									Thread.sleep(3000);
									if (XpathMap.get("NFDHasHook").equalsIgnoreCase("Yes")) {
										clickOnBaseSceneContinueButton(report, imageLibrary, creditAfterSpin);
										Thread.sleep(3000);
									}
									imageLibrary.click("Spin");
									Thread.sleep(7000);
									// check if spin is successful,take screenshot
									report.detailsAppendFolder("verify FG Spin 3", "Spin Button is working",
											"Spin Button is working", "Pass", languageCode);
									if (imageLibrary.isImageAppears("FGSummaryBackToGameBtn")) {

										report.detailsAppendFolder("verify FG Summary Screen",
												" should be landed on FG summary screen", "landed on FG summary screen",
												"Pass", languageCode);
									}
								}
							} else {
								report.detailsAppendFolder("Verify Free Game Resume Button",
										"Free Game Resume button should be visible",
										"Free Game Resume button is not visible", "Fail", languageCode);

							}
						}
					} else {
						System.out.println("Free Game Scene is not visible");
						log.debug("Free Game Scene is not visible");
						report.detailsAppendFolder("Verify Free Game Scene", "Free Game Scene is visible",
								"Free Game Scene is not visible", "Fail",languageCode);
					}

				}
			} catch (Exception e) {
				e.getMessage();
				log.error(e);
			}

		}
			/**
			 * This method is used to launch the game and click on NFD button
			 * @author pb61055
			 * @param url
			 * @param report
			 * @param imageLibrary
			 * @param currencyName
			 * @return
			 */
			public boolean loadGameAndClickContinueBtn(String url,Desktop_HTML_Report report,ImageLibrary imageLibrary,String currencyName) 
			{
				boolean isGameLaunch = false;
				Wait = new WebDriverWait(webdriver, 120);
				try {			
					webdriver.navigate().to(url);
					Thread.sleep(8000);
					if ("Yes".equalsIgnoreCase(XpathMap.get("NFD")))
					{
						clickOnBaseSceneContinueButton(report, imageLibrary,currencyName);
					}	
					Thread.sleep(2000);
					log.debug("game loaded  ");
				
				}catch (Exception e) {
					 log.error(e.getMessage(), e);	
					 log.debug("Exception occur in loadgame()");
					}
				return isGameLaunch;
			}
			
			public void SelectFreeSpinOption(Desktop_HTML_Report report, ImageLibrary imageLibrary) {
				try 
				{
				//imageLibrary.waitTillImageVisible("FSOption1");
				if(imageLibrary.isImageAppears("FSOption1")) {
					imageLibrary.click("FSOption1");
					}
				}
				catch(Exception e) {
					e.getMessage();
					log.error(e);
				}
				
			}
			
			public void checkFSRefresh(Desktop_HTML_Report report, ImageLibrary imageLibrary,String languageCode) throws InterruptedException {
				try {
				webdriver.navigate().refresh();
				Thread.sleep(8000);
				imageLibrary.waitTillImageVisible("FSRefreshContinueBTN");
				if(imageLibrary.isImageAppears("FSRefreshContinueBTN")) {
					report.detailsAppendFolder("FS continue button after refresh", "FS continue button should be displayed after refresh", "Free Spin Scene is visible after refresh", "Pass",languageCode);
					imageLibrary.click("FSRefreshContinueBTN");
					Thread.sleep(3000);
					report.detailsAppendFolder("Verify Free Spin refresh", "Free Spin Scene should be visible after refresh", "Free Spin Scene is visible", "Pass",languageCode);
				}
				}
				catch(Exception e) {
					e.getMessage();
					log.error(e);
				}}
			
			public void validateSessionReminderFSScene(Desktop_HTML_Report report,String userName,ImageLibrary imageLibrary,String languageCode,String SessionReminderUserInteraction,String SessionReminderContinue,String SessionReminderExitGame) 
			{
				try 
				{
					CommonUtil util = new CommonUtil();						
					String gameurl = webdriver.getCurrentUrl();
					
					String periodValue= XpathMap.get("sessionReminderDurationInSec");
					String resetPeriodValue=XpathMap.get("resetSessionReminderValue");
				
					//assigning session reminder
					boolean sessionReminderAssigned=util.setSessionReminderOnAxiom(userName,periodValue);
					loadGame(gameurl);Thread.sleep(5000);
					/*if ("Yes".equalsIgnoreCase(XpathMap.get("NFD"))) {
						clickOnBaseSceneContinueButton(report, imageLibrary);
					}*/
					log.debug("Session reminder is set for "+periodValue+" secs");
								
					
					if (sessionReminderAssigned && sessionReminderPresent())
					{
						report.detailsAppendFolder("Verify Session Reminder must be present","Session Reminder must be present", "Session Reminder is present","Pass", languageCode);
															
						//Ensure that the continue button takes you back to the game
						if(SessionReminderContinue.equalsIgnoreCase("yes"))
						{
							selectContinueSession();
							if(imageLibrary.isImageAppears("FSRefreshContinueBTN"))
							{
								imageLibrary.click("FSRefreshContinueBTN");
								Thread.sleep(2000);
								report.detailsAppendFolder("Verify Session Reminder dialog box is closed and game is visible",	" Session Reminder dialog box is closed and game should be visible"," Session Reminder dialog box is closed and game is visible","Pass",languageCode);
								Thread.sleep(2000);
							}
							else
							{
								report.detailsAppendFolder("Verify Session Reminder dialog box is closed and game is visible",	" Session Reminder dialog box is closed and game should be visible"," Session Reminder dialog box is closed and game is not visible","Fail",languageCode);	
							}									
						}
					} else {
						report.detailsAppendFolder("Verify Session Reminder must be present","Session Reminder must be present","Session Reminder is not present", "Fail",languageCode);
						log.debug("Session reminder is not visible on screen");
						Thread.sleep(2000);
					}
						
					//reset to higher duarion for session reminder
					boolean resetSessionReminderDUration=util.setSessionReminderOnAxiom(userName,resetPeriodValue);
					if (resetSessionReminderDUration) {
						log.debug("Session reminder has been reset to 10hr in order to avoid pop up");
					} else {
						log.debug("Unable to reset Session reminder");
					}							
					// relaunching the game to set the above reset session reminder duration to higher value
					loadGame(gameurl);
					Thread.sleep(5000);
					closeOverlay();
					/*imageLibrary.waitTillImageVisible("FSRefreshContinueBTN");
					if(imageLibrary.isImageAppears("FSRefreshContinueBTN")) {
						imageLibrary.click("FSRefreshContinueBTN");
						
					}*/
																	
				} catch (Exception e) {
				e.getMessage();
				log.error(e);
			}
		}			
			
			//*************************** Practice Play *****************
			public void practicePlay(Desktop_HTML_Report report, ImageLibrary imageLibrary, String languageCode){
				try {
					String urlNew;
					String currentUrl = webdriver.getCurrentUrl();
					urlNew = currentUrl.replaceAll("isPracticePlay=False", "isPracticePlay=True");
					loadGame(urlNew);
					
					if ("Yes".equalsIgnoreCase(XpathMap.get("NFD"))) {
						
						imageLibrary.waitTillImageVisible("NFDButton");
						clickOnBaseSceneContinueButton(report, imageLibrary,languageCode);
						report.detailsAppendFolder("Verify Practice Play",	" Practice play should be visible"," Practice Play is visible","Pass",languageCode);
						if(imageLibrary.isImageAppears("Spin")) {
							imageLibrary.click("Spin");
							report.detailsAppendFolder("Verify Practice Play first Spin",	" Practice play first Spin"," Practice play first Spin ","Pass",languageCode);
							
							imageLibrary.waitTillImageVisible("Spin");
							imageLibrary.click("Spin");
							Thread.sleep(1000);
							report.detailsAppendFolder("Verify Practice Play second Spin",	" Practice play second Spin"," Practice play second Spin ","Pass",languageCode);
							
							imageLibrary.waitTillImageVisible("Spin");
							imageLibrary.click("Spin");
							Thread.sleep(1000);
							report.detailsAppendFolder("Verify Practice Play third Spin",	" Practice play third Spin"," Practice play third Spin ","Pass",languageCode);
							
						}								 								
					}
					// Load Game in real i.e. isPracticePlay=False
					loadGame(currentUrl);
					System.out.println("Game loaded in real mode from practice play");
					log.info("Game loaded in real mode from practice play");
					if ("Yes".equalsIgnoreCase(XpathMap.get("NFD"))) {
						
						imageLibrary.waitTillImageVisible("NFDButton");
						clickOnBaseSceneContinueButton(report, imageLibrary,languageCode);
						report.detailsAppendFolder("Verify Game after Practice play",	"Game should be out of Pratice Play ","Game is out of Pratice Play ","Pass",languageCode);
					}
					
				}catch(Exception e) {
					e.getMessage();
					log.error(e);
				}
			}
			
			
			public void spinCases(Desktop_HTML_Report report, ImageLibrary imageLibrary, String languageCode){
				try {
					//added for test data related 
					String gameurl = webdriver.getCurrentUrl();
					loadGameAndClickContinueBtn(gameurl, report, imageLibrary, languageCode);
					
					if(imageLibrary.isImageAppears("Spin")) {
						imageLibrary.click("Spin");
						report.detailsAppendFolder("Verify Spins ",	" Spin 1"," Spin 1 Should be done","Pass",languageCode);
							
						imageLibrary.waitTillImageVisible("Spin");
						imageLibrary.click("Spin");
						Thread.sleep(1000);
						report.detailsAppendFolder("Verify Spins ",	" Spin 2"," Spin 2 Should be done","Pass",languageCode);
							
						imageLibrary.waitTillImageVisible("Spin");
						imageLibrary.click("Spin");
						Thread.sleep(1000);
						report.detailsAppendFolder("Verify Spins ",	" Spin 3"," Spin 3 Should be done","Pass",languageCode);;
						imageLibrary.waitTillImageVisible("Spin");
						}								 								
					}
				catch(Exception e) {
					e.getMessage();
					log.error(e);
				}
			}
			
			public void loadGamewithCredits(Desktop_HTML_Report report, ImageLibrary imageLibrary, String languageCode){
				try {
					//added for test data related 
					String gameurl = webdriver.getCurrentUrl();
					loadGameAndClickContinueBtn(gameurl, report, imageLibrary, languageCode);
					
					if(imageLibrary.isImageAppears("Spin")) {
						imageLibrary.click("Spin");
						report.detailsAppendFolder("Verify Spins with updated balance ",	" Spin 1"," Spin 1 Should be done","Pass",languageCode);
							
						imageLibrary.waitTillImageVisible("Spin");
						imageLibrary.click("Spin");
						Thread.sleep(1000);
						report.detailsAppendFolder("Verify Spins with updated balance ",	" Spin 2"," Spin 2 Should be done","Pass",languageCode);
							
						imageLibrary.waitTillImageVisible("Spin");
						imageLibrary.click("Spin");
						Thread.sleep(1000);
						report.detailsAppendFolder("Verify Spins with updated balance ",	" Spin 3"," Spin 3 Should be done","Pass",languageCode);
							
						}								 								
					}
				catch(Exception e) {
					e.getMessage();
					log.error(e);
				}
			}
			
	public void loadGamewithoutCredits(Desktop_HTML_Report report, ImageLibrary imageLibrary, String languageCode,
			String userName, String balance) {
		try {
			CommonUtil util = new CommonUtil();

			String newBalance = XpathMap.get("newBalance");
			util.updateUserBalance(userName, newBalance);

			Thread.sleep(3000);
			// added for test data related
			String gameurl = webdriver.getCurrentUrl();
			loadGameAndClickContinueBtn(gameurl, report, imageLibrary, languageCode);
			if (imageLibrary.isImageAppears("Spin")) {
				imageLibrary.click("Spin");
				Thread.sleep(3000);
				if (elementVisible_Xpath(XpathMap.get("insufficientBalanceOverlay"))) {
					report.detailsAppendFolder("Verify Spins without Balance ",
							" Insufficient Balance pop up should be displayed ",
							"Insufficient Balance pop up is displayed", "Pass", languageCode);
				} else {
					report.detailsAppendFolder("Verify Spins without Balance ",
							" Insufficient Balance pop up should be displayed ",
							"Insufficient Balance pop up is not displayed", "Fail", languageCode);
				}

			}
			util.updateUserBalance(userName, balance);
			loadGameAndClickContinueBtn(gameurl, report, imageLibrary, languageCode);
		} catch (Exception e) {
			e.getMessage();
			log.error(e);
		}
	}

			/**
			 * This method used to spin until free spins are triggered
			 * @author pb61055
			 * @param imageLibrary
			 */
			public void spin(ImageLibrary imageLibrary)
			{
				try 
				{
					imageLibrary.click("Spin");
					/*if(imageLibrary.isImageAppears("Spin")) 
					{
						imageLibrary.click("Spin");
					}*/
					Thread.sleep(12000);

				}catch (Exception e) {
					 log.error(e.getMessage(), e);						
					}	
			}
			
			/**
			 * This method is used to verify free spins entry screen
			 * @author pb61055
			 * @param report
			 * @param imageLibrary
			 * @param languageCode
			 */
			public void freeSpinsEntryScreen(Desktop_HTML_Report report,ImageLibrary imageLibrary,String languageCode,String FreeSpinTransitionBaseGameToFeature)
			{
				try
				{
					report.detailsAppendFolder("Verify Free Spins Entry screen", "Free spin entry screen should be visible", "Free spin entry screen is visible", "Pass",languageCode);
					
					if("Yes".equalsIgnoreCase(XpathMap.get("isFSHaveMultipleOptions"))) 
					{
						
						SelectFreeSpinOption(report, imageLibrary);
						Thread.sleep(2000);
						//Free Spins - Transition from Base Game to Feature
						if(FreeSpinTransitionBaseGameToFeature.equalsIgnoreCase("yes"))
						{
							report.detailsAppendFolder("Verify Free Spins Transition from Base Game to Feature", "Player should be able to click on Free spin option and enter free spins", "Player is able to click on Free spin option and enter free spins", "Pass",languageCode);
						}
					}
					Thread.sleep(5000);
					imageLibrary.waitTillImageVisible("tapToContinueFreespins");
					
					if(imageLibrary.isImageAppears("tapToContinueFreespins")) 
					{					
						imageLibrary.click("tapToContinueFreespins");
						report.detailsAppendFolder("Free Spins", "Should be landed on Free Spins scene", "Landed on Free Spins Scene", "Pass",languageCode);
						Thread.sleep(7000);
						report.detailsAppendFolder("Free Spins", "Free Spins scene", "Free Spins scene", "Pass",languageCode);
					}
					
				}catch (Exception e) {
					 log.error(e.getMessage(), e);						
				}
			}
			
			  /**
			    * This method is used for check reel landing in FS
			    * @author pb61055
			    * @param report
			    * @param currencyName
			    */
			   public void checkSpinReelLandingFeature(Desktop_HTML_Report report,String currencyName)
			   {
				   try
				   {
						report.detailsAppendFolder("Verify Spin reel landing", "Reels should not land at the same time", "Reels did not land at the same time", "Pass",currencyName);
				   }
				   catch (Exception e) {
					   log.error(e.getMessage(), e);
				   	}
			   }
			   
			   
			   /**
			    * This method is used to validate Bonus session reminder
			    * @author pb61055
			    * @param report
			    * @param userName
			    * @param imageLibrary
			    * @param languageCode
			    * @param SessionReminderUserInteraction
			    * @param SessionReminderContinue
			    * @param SessionReminderExitGame
			    */
			   public void validateSessionReminderBonus(Desktop_HTML_Report report,String userName,ImageLibrary imageLibrary,String languageCode,String SessionReminderUserInteraction,String SessionReminderContinue,String SessionReminderExitGame) 
				{
					try 
					{
						CommonUtil util = new CommonUtil();						
						String gameurl = webdriver.getCurrentUrl();
						
						String periodValue= XpathMap.get("sessionReminderDurationInSec");
						String resetPeriodValue=XpathMap.get("resetSessionReminderValue");
					
						//assigning session reminder
						boolean sessionReminderAssigned=util.setSessionReminderOnAxiom(userName,periodValue);
						loadGame(gameurl);Thread.sleep(5000);
						
						log.debug("Session reminder is set for "+periodValue+" secs");
									
						
						if (sessionReminderAssigned && sessionReminderPresent())
						{
							report.detailsAppendFolder("Verify Session Reminder must be present","Session Reminder must be present", "Session Reminder is present","Pass", languageCode);
																
							//Ensure that the continue button takes you back to the game
							if(SessionReminderContinue.equalsIgnoreCase("yes"))
							{
								selectContinueSession();
								if(imageLibrary.isImageAppears("BonusSpin"))
								{
									if("Yes".equalsIgnoreCase(XpathMap.get("BonusRefreshHasContinueBtn")))
									{
										imageLibrary.isImageAppears("BonusRefreshContinue");
									}
									report.detailsAppendFolder("Verify Session Reminder dialog box is closed and game is visible",	" Session Reminder dialog box is closed and game should be visible"," Session Reminder dialog box is closed and game is visible","Pass",languageCode);
									Thread.sleep(2000);
								}
								else
								{
									report.detailsAppendFolder("Verify Session Reminder dialog box is closed and game is visible",	" Session Reminder dialog box is closed and game should be visible"," Session Reminder dialog box is closed and game is not visible","Fail",languageCode);	
								}									
							}
						} else {
							report.detailsAppendFolder("Verify Session Reminder must be present","Session Reminder must be present","Session Reminder is not present", "Fail",languageCode);
							log.debug("Session reminder is not visible on screen");
							Thread.sleep(2000);
						}
							
						//reset to higher duarion for session reminder
						boolean resetSessionReminderDUration=util.setSessionReminderOnAxiom(userName,resetPeriodValue);
						if (resetSessionReminderDUration) {
							log.debug("Session reminder has been reset to 10hr in order to avoid pop up");
						} else {
							log.debug("Unable to reset Session reminder");
						}							
						// relaunching the game to set the above reset session reminder duration to higher value
					
						loadGame(gameurl);
						Thread.sleep(5000);
						if("Yes".equalsIgnoreCase(XpathMap.get("BonusRefreshHasContinueBtn")))
						{
							imageLibrary.isImageAppears("BonusRefreshContinue");
						}
						
					}   catch (Exception e) {
							   log.error(e.getMessage(), e);
						   	}
			}
			
	/**
	 * This method is used to wait till Free spins continue button is visible
	 * @author pb61055
	 * @param report
	 * @param imageLibrary
	 * @param languageCode
	 * @return
	 */
	public boolean waitTillFreespinSummary(Desktop_HTML_Report report,ImageLibrary imageLibrary,String languageCode)
	{
		boolean isFSSummaryVisible=false;
		try
		{
			if ("Yes".equalsIgnoreCase(XpathMap.get("isFSSummaryHookAvailable")))
			{
				if (waitForElement("FSSummaryScreen")) 
					isFSSummaryVisible = true;
			}
			else
			{
				if (imageLibrary.waitTillImageVisible("FSSummaryContinueBtn")) 
					isFSSummaryVisible = true;
			}
			Thread.sleep(5000);
			if(isFSSummaryVisible)
				report.detailsAppendFolder("Verify if Free spins summary screen is visible", "Free spins Summary Screen should be visible","Free spins Summary Screen is visible", "Pass",languageCode);
			else
				report.detailsAppendFolder("Verify if Free spins summary screen is visible", "Free spins Summary Screen should be visible","Free spins Summary Screen is not visible", "Fail",languageCode);
			
			
		}catch (Exception e) {
			   log.error(e.getMessage(), e);
		   	}
		return isFSSummaryVisible;
	}
	
	/**
	 * This method is used to verify Free Spins- Transition from Feature to Base Game and refresh during transition
	 * @author pb61055
	 * @param report
	 * @param imageLibrary
	 * @param languageCode
	 */
	public void freeSpinsToBaseGameTransitions(Desktop_HTML_Report report,ImageLibrary imageLibrary,String languageCode,String FreeSpinTransitionFeatureToBaseGame,String FreeSpinTransitionFeatureToBaseGameRefresh)
	{
		try
		{
			//Free Spins - Transition from Feature to Base Game
			if(FreeSpinTransitionFeatureToBaseGame.equalsIgnoreCase("yes"))
			{
				if(waitTillFreespinSummary(report,imageLibrary,languageCode))
				{
					closeFreeSpins(report, imageLibrary, languageCode);
					Thread.sleep(3000);
					report.detailsAppendNoScreenshot("Verify Free Spins - Transition from Feature to Base Game", "Free Spins - Transition from Feature to Base Game","Free Spins - Transition from Feature to Base Game", "Pass");
				}
				else
					report.detailsAppendNoScreenshot("Verify Free Spins - Transition from Feature to Base Game", "Free Spins - Transition from Feature to Base Game","Free Spins - Transition from Feature to Base Game", "Fail");
			}
			
			//Free Spins - Refresh - Transition from Feature to Base Game
			if(FreeSpinTransitionFeatureToBaseGameRefresh.equalsIgnoreCase("yes"))
			{
				webdriver.navigate().refresh();
				Thread.sleep(8000);
				if(waitTillFreespinSummary(report,imageLibrary,languageCode))
				{
					closeFreeSpins(report, imageLibrary, languageCode);
					report.detailsAppendNoScreenshot("Verify Free Spins - Refresh - Transition from Feature to Base Game", "Free Spins - Refresh - Transition from Feature to Base Game","Free Spins - Refresh - Transition from Feature to Base Game", "Pass");
				}
				else
					report.detailsAppendNoScreenshot("Verify Free Spins - Refresh - Transition from Feature to Base Game", "Free Spins - Refresh - Transition from Feature to Base Game","Free Spins - Refresh - Transition from Feature to Base Game", "Fail");
			}
			
			
		}catch (Exception e) {
			   log.error(e.getMessage(), e);
		   	}
	}
	

	public void netPositioninFS(Desktop_HTML_Report report, String languageCode) throws InterruptedException {
		try {
			String creditBefore = getConsoleText("return " + XpathMap.get("CreditValue"));
			double dblNetPositionBefore = Double.parseDouble(creditBefore.replaceAll("[^0-9.-]", "").replace(".", ""));

			System.out.println("Net Position before Spins in FS " + creditBefore);
			Thread.sleep(5000);
			String creditAfter = getConsoleText("return " + XpathMap.get("CreditValue"));
			double dblNetPositionAfter = Double.parseDouble(creditAfter.replaceAll("[^0-9.-]", "").replace(".", ""));
			
			if (dblNetPositionBefore<=dblNetPositionAfter) {
				report.detailsAppendFolder("verify FS Net position ", "Net position should not be deducted in FS",
						"Net Position is not deducted ", "pass", languageCode);
			} else {
				report.detailsAppendFolder("verify FS Net position ", "Net position should not be deducted in FS",
						"Net Position is deducted ", "fail",languageCode);
			}
		} catch (Exception e) {
			e.getMessage();
			log.error(e);
		}
	}
	
	public void sessionDurationOntopBar(Desktop_HTML_Report report, String languageCode) throws InterruptedException {
		try {
			webdriver.navigate().refresh();
			Thread.sleep(25000);
			if (isElementPresent(XpathMap.get("sessionDurationDisplay"))) {
				report.detailsAppendFolder("check Session duration",
						" Session duration should be 10 sec or more than 10 sec", "check Session duration", "Pass",
						languageCode);
				webdriver.navigate().refresh();
				Thread.sleep(10000);
				report.detailsAppendFolder("check Session duration reset", " Session duration should be reset",
						"check Session duration", "Pass", languageCode);
			} else {
				report.detailsAppendFolder("check Session duration", " Session duration should be present on infobar",
						"Session Duration is not present on info bar", "Fail", languageCode);
			}
		} catch (Exception e) {
			e.getMessage();
			log.error(e);
		}
	}
	
	public boolean AcceptfgOffer(Desktop_HTML_Report report, ImageLibrary imageLibrary, String languageCode) {
		boolean fgOfferAccepted= false;
		try {			
			webdriver.navigate().refresh();
			Thread.sleep(7000);
			if (imageLibrary.isImageAppears("FreeGamesPlayNow")) {
				imageLibrary.click("FreeGamesPlayNow");
				Thread.sleep(3000);
				if (XpathMap.get("NFDHasHook").equalsIgnoreCase("Yes")) {
					clickOnBaseSceneContinueButton(report, imageLibrary, languageCode);
				}
				if (imageLibrary.isImageAppears("Spin")) {
					System.out.println("Free Game Scene is visible");
					log.debug("Free Game Scene is visible");
					report.detailsAppendFolder("Verify Free Game Scene", "Free Game Scene is visible",
							"Free Game Scene is visible", "Pass",languageCode );
					fgOfferAccepted=true;
				}
			}
		}catch (Exception e) {
			e.getMessage();
			log.error(e);
		}
		return fgOfferAccepted;
	}
	
	public void spinUntilImageAppears(Desktop_HTML_Report report, ImageLibrary imageLibrary, String image)
	{
		try
		{
			while(!(imageLibrary.isImageAppears(image)))
			{
				webdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				imageLibrary.click("Spin");
				Thread.sleep(8000);
				if(imageLibrary.isImageAppears(image))
				{
					break;
				}
			}		
		} catch (Exception e) {
			   log.error(e.getMessage(), e);
		   	}
	}
	public void freeSpinsInFG(Desktop_HTML_Report report, ImageLibrary imageLibrary, String languageCode) {
		try {
			if(fGAssignment(report,imageLibrary,languageCode)) {	
				
				if(AcceptfgOffer(report, imageLibrary, languageCode)) {
				// trigger free spins
				imageLibrary.click("Spin");
				if(XpathMap.get("tapToContinueFreespinsAvailable").equalsIgnoreCase("Yes"))
				{
					//cfnlib.waitForElement("tapToContinueFreespins");
					if(XpathMap.get("isFSHaveMultipleOptions").equalsIgnoreCase("Yes"))
					{
						imageLibrary.click("tapToFSOption1");
						Thread.sleep(3000);
					}
					imageLibrary.click("tapToContinueFreespins");
				}
											
				Thread.sleep(10000);
				// 1st SS for FS in FG
				report.detailsAppend("Verify free spins in Free Game", "Net Position should not change during free spins in Free Game",
						"Net Position should not change during free spins in Free Game", "PASS",languageCode );
				Thread.sleep(5000);
				// 2nd SS for FS in FG
				report.detailsAppend("Verify free spins in Free Game", "Net Position should not change during free spins in Free Game",
						"Net Position should not change during free spins in Free Game", "PASS",languageCode );
		
				if(waitTillFreespinSummary(report,imageLibrary,languageCode)) {					
						closeFreeSpins(report, imageLibrary, languageCode);
						Thread.sleep(3000);
						report.detailsAppend("Verify Free Spins - Transition from Feature to FG", "Free Spins - Transition from Feature to FG","Free Spins - Transition from Feature to FG", "Pass", languageCode);
					}
					else {
						report.detailsAppend("Verify Free Spins - Transition from Feature to FG", "Free Spins - Transition from Feature to FG","Free Spins - Transition from Feature to FG", "Fail", languageCode);
					}
				 FGOfferDelete(report, imageLibrary,languageCode);
				}
				else {
					report.detailsAppendFolder("Verify Free Game Accept Offer", "Free Game offer should be accepted",
							"Free Game offer is not accepted", "Fail",languageCode );
				}
			}
			
		}catch (Exception e) {
			e.getMessage();
			log.error(e);
		}
				
	}
	
	/**
	 * This method is used to verify Link and Win Transitions
	 * @author pb61055
	 * @param report
	 * @param imageLibrary
	 * @param languageCode
	 * @param BonusTransitionBaseGameToFeature
	 * @param BonusTransitionBaseGameToFeatureRefresh
	 */
	public void verifyBonusTransitionBaseToBonus(Desktop_HTML_Report report,ImageLibrary imageLibrary,String languageCode,String BonusTransitionBaseGameToFeature,
			String BonusTransitionBaseGameToFeatureRefresh)
	{
		try
		{
			// Bonus - Transition from Feature to Base Game
			if (BonusTransitionBaseGameToFeature.equalsIgnoreCase("yes")) {
				
				if (elementVisible_Xpath(XpathMap.get("isBonusVisible"))) {
					report.detailsAppendFolder("Verify Bonus - Transition from Base Game to Feature",
							"Bonus - Transition from Base Game to Feature",
							"Bonus - Transition from Base Game to Feature", "Pass", languageCode);
				}
			}

			// Bonus - Refresh - Transition from Feature to Base Game
			if (BonusTransitionBaseGameToFeatureRefresh.equalsIgnoreCase("yes")) {
				webdriver.navigate().refresh();
				Thread.sleep(8000);
				if (elementVisible_Xpath(XpathMap.get("isBonusVisible"))) {
					report.detailsAppendFolder("Verify Bonus - Transition from Base Game to Feature",
							"Bonus - Transition from Base Game to Feature",
							"Bonus - Transition from Base Game to Feature", "Pass", languageCode);
				}
			}

		}
		 catch (Exception e) {
			   log.error(e.getMessage(), e);
		   	}
	}
	
	/**
	 * This method is used to verify Link and Win Transitions
	 * @author pb61055
	 * @param report
	 * @param imageLibrary
	 * @param languageCode
	 * @param BonusTransitionFeatureToBaseGame
	 * @param BonusTransitionFeatureToBaseGameRefresh
	 */
	public void verifyBonusTransitionBonusToBase(Desktop_HTML_Report report,ImageLibrary imageLibrary,String languageCode,String BonusTransitionFeatureToBaseGame,String BonusTransitionFeatureToBaseGameRefresh)
	{
		try
		{
			//Bonus - Transition from Feature to Base Game
			if(BonusTransitionFeatureToBaseGame.equalsIgnoreCase("yes"))
			{
				if(elementVisible_Xpath(XpathMap.get("bonusFeatureCongratulations")))
				{
					imageLibrary.click("BonusContinue");
					Thread.sleep(3000);
					report.detailsAppendNoScreenshot("Verify Bonus - Transition from Feature to Base Game", "Bonus - Transition from Feature to Base Game","Bonus - Transition from Feature to Base Game", "Pass");
				}
				else
					report.detailsAppendNoScreenshot("Verify Bonus - Transition from Feature to Base Game", "Bonus - Transition from Feature to Base Game","Bonus - Transition from Feature to Base Game", "Fail");
			}
			
			
			//Bonus - Refresh - Transition from Feature to Base Game
			if(BonusTransitionFeatureToBaseGameRefresh.equalsIgnoreCase("yes"))
			{
				webdriver.navigate().refresh();
				Thread.sleep(8000);
				if(elementVisible_Xpath(XpathMap.get("bonusFeatureCongratulations")))
				{
					imageLibrary.click("BonusContinue");
					Thread.sleep(3000);
					report.detailsAppendNoScreenshot("Verify Bonus - Refresh - Transition from Feature to Base Game", "Bonus - Refresh - Transition from Feature to Base Game","Bonus - Refresh - Transition from Feature to Base Game", "Pass");
				}
				else
					report.detailsAppendNoScreenshot("Verify Bonus - Refresh - Transition from Feature to Base Game", "Bonus - Refresh - Transition from Feature to Base Game","Bonus - Refresh - Transition from Feature to Base Game", "Fail");
			}
			
			//Game does not carry previous spin info after a refresh - Bonus
			checkNetPositionAfterRefresh(report, imageLibrary, languageCode);
			verifyNetPositionValue(report, imageLibrary, languageCode, "NormalWin");
			
		}
		 catch (Exception e) {
			   log.error(e.getMessage(), e);
		   	}
	}
	
	/**
	 * This method is used to verify link and win feature
	 * @author pb61055
	 * @param report
	 * @param imageLibrary
	 * @param languageCode
	 */
	public void verifyLinkAndWinFeature(Desktop_HTML_Report report,ImageLibrary imageLibrary,String languageCode)
	{
		try
		{
			if(imageLibrary.isImageAppears("BonusSpin"))
			{
				spinUntilImageAppears(report, imageLibrary, "BonusComplete");
			}
			if(elementVisible_Xpath(XpathMap.get("bonusFeatureCongratulations")))
			{
				report.detailsAppendFolder("Verify Bonus is completed", "Bonus Feature should be complete",
						"Bonus Feature is completed", "Pass",languageCode );
			}
			else if(imageLibrary.isImageAppears("BonusContinue"))
			{
				report.detailsAppendFolder("Verify Bonus is completed", "Bonus Feature should be complete",
						"Bonus Feature is completed", "Pass",languageCode );
			}
			else
			{
				report.detailsAppendFolder("Verify Bonus is completed", "Bonus Feature should be complete",
						"Bonus Feature is not completed", "Fail",languageCode );
			}
			
		} catch (Exception e) {
			   log.error(e.getMessage(), e);
		   	}
	
	}
	
	/**
	 * This method is used to verify Net Position in Bonus
	 * @author pb61055
	 * @param report
	 * @param imageLibrary
	 * @param languageCode
	 * @param winType
	 */
	public void verifyNetPositionBonus(Desktop_HTML_Report report,ImageLibrary imageLibrary,String languageCode,String winType,double dblNetPositionBeforeBonus)
	{
		try
		{
			boolean isNetPositionUpdated = false;
			boolean isPlayerWon = false;
			String winAmount = null;

			String betValue = getConsoleText("return " + XpathMap.get("BetTextValue"));
			double dblBetValue = Double.parseDouble(betValue.replaceAll("[^0-9]", ""));

			String netPositionAfterBonus = webdriver.findElement(By.xpath(XpathMap.get("isNetPosition"))).getText();
			double dblNetPositionAfterBonus = Double
					.parseDouble(netPositionAfterBonus.replaceAll("[^0-9.-]", "").replace(".", ""));

			if (winType.isEmpty())
				isPlayerWon(report, imageLibrary, languageCode);
			else
				isPlayerWon = isPlayerWon(report, imageLibrary, languageCode, winType);

			if (isPlayerWon) {
				winAmount = playerWinAmount(report, imageLibrary, languageCode, winType);
				double dblWinAmount = Double.parseDouble(winAmount.replaceAll("[^0-9]", ""));
				try

				{
					double dblBSandBetValue = dblNetPositionBeforeBonus - dblBetValue;
					if ((dblBSandBetValue + dblWinAmount) == dblNetPositionAfterBonus) {
						System.out.println("Win is added to net position successfully");
						isNetPositionUpdated = true;
					} else {
						System.out.println("Win is not added to net position successfully");
						isNetPositionUpdated = false;
					}
				} catch (Exception e) {
					log.error(e);
				}

			} else {
				if ((dblNetPositionBeforeBonus - dblBetValue) == (dblNetPositionAfterBonus)) {
					System.out.println("There is no win, net position is updated successfully");
					isNetPositionUpdated = true;
				} else {
					System.out.println("There is no win, net position is not updated successfully");
					isNetPositionUpdated = false;
				}
			}
			if (isNetPositionUpdated) {
				report.detailsAppend("verify Net position updates for " + winType, "Net position should update ",
						"Net position is updating ", "Pass");
				log.debug("Net Position before spin " + dblNetPositionBeforeBonus + " Net psotion after spin "
						+ netPositionAfterBonus);
			} else {
				report.detailsAppend("verify Net position updates for " + winType, "Net position should update ",
						"Net position is not updating ", "Fail");
				log.debug("Net Position before spin " + dblNetPositionBeforeBonus + " Net psotion after spin "
						+ netPositionAfterBonus);
			}
		} catch (Exception e) {
			   log.error(e.getMessage(), e);
		   	}
	}

	/**
	 * This method is used to select bonus option
	 * @author pb61055
	 * @param imageLibrary
	 */
	public void selectBonusOption(ImageLibrary imageLibrary) 
	{
		try 
		{
		if(imageLibrary.isImageAppears("BonusOption1")) {
			imageLibrary.click("BonusOption1");
			}
		}
		 catch (Exception e) {
			   log.error(e.getMessage(), e);
		   	}
	}
	
	/**
	 * This method is used t trigger bonus
	 * @author pb61055
	 * @param imageLibrary
	 */
	public boolean triggerBonus(ImageLibrary imageLibrary)
	{
		boolean isBonusTriggered=false;
		try
		{			
			int retryCount=5;
			int count=0;
			while(count<retryCount)
			{
				if(elementVisible_Xpath(XpathMap.get("isBonusVisible"))) 
				{
					isBonusTriggered=true;
					break;
				}
				
				if (imageLibrary.isImageAppears("Spin")) {
					imageLibrary.click("Spin");
					
				}
				count++;
			}
				Thread.sleep(5000);
		}
		 catch (Exception e) {
			   log.error(e.getMessage(), e);
		   	}
		return isBonusTriggered;
	}
	
	/**
	 * This method is used to get net poition before any feature is triggered
	 * @author pb61055
	 * @return
	 */
	public double getNetPositionDblValue()
	{
		double dblNetPosition = 0;
		try
		{
			String netPosition = webdriver.findElement(By.xpath(XpathMap.get("isNetPosition"))).getText();
			dblNetPosition = Double.parseDouble(netPosition.replaceAll("[^0-9.-]", "").replace(".", ""));

		}
		 catch (Exception e) {
			   log.error(e.getMessage(), e);
		   	}
		return dblNetPosition;
	}
	
	/**
	 * This method is used to check net position value after refresh
	 * @author pb61055
	 * @param report
	 * @param languageCode
	 */
	public void checkNetPositionAfterRefresh(Desktop_HTML_Report report, ImageLibrary imageLibrary, String languageCode)
	{
		try {
			refreshGameAndClickContinueBtn(report, imageLibrary, languageCode);

			String netPositionValueRefresh=webdriver.findElement(By.xpath(XpathMap.get("isNetPosition"))).getText();
			netPositionValueRefresh=netPositionValueRefresh.replaceAll("[^0-9.-]", "").trim();
			if(netPositionValueRefresh.equals("0.00"))
			{
				report.detailsAppendNoScreenshot("Verify net position is 0.00 when refreshed", "Net position should be 0.00", "Net position is 0.00", "Pass");
			}
			else
			{
				report.detailsAppendFolder("Verify net position is 0.00 when refreshed", "Net position should be 0.00", "Net position is not 0.00", "Fail",languageCode);														
			}
		}
		catch (Exception e) {
			   log.error(e.getMessage(), e);
		   	}
	}
	
	/**
	 * This method is used to verify Bonus in Free games
	 * @author pb61055
	 * @param report
	 * @param imageLibrary
	 * @param languageCode
	 */
	public void bonusInFreeGames(Desktop_HTML_Report report, ImageLibrary imageLibrary, String languageCode)
	{
		try
		{
			verifyNetPositionValue(report, imageLibrary, languageCode, "NormalWin");
			verifyNetPositionValue(report, imageLibrary, languageCode, "NoWin");
			double dblNetPositionBeforeBonus = getNetPositionDblValue();
			verifyNetPositionBonus(report, imageLibrary, languageCode,XpathMap.get("BonusWinType"), dblNetPositionBeforeBonus);
			verifyNetPositionValue(report, imageLibrary, languageCode, "NormalWin");
			verifyNetPositionValue(report, imageLibrary, languageCode, "NoWin");
		}
		catch (Exception e) {
			   log.error(e.getMessage(), e);
		   	}
	}
	
}
