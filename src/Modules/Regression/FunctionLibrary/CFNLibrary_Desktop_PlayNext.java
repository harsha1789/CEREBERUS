package Modules.Regression.FunctionLibrary;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

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

import com.zensar.automation.framework.utils.Constant;

import com.zensar.automation.framework.utils.Util;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.ImageLibrary;
import com.zensar.automation.library.TestPropReader;

import io.appium.java_client.TouchAction;
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

	


	public boolean paytableScroll(Desktop_HTML_Report report, String CurrencyName) {
		boolean paytableScroll = false;
		try {
			if (XpathMap.get("paytableScrollOfFive").equalsIgnoreCase("yes")) {
				paytableScroll = paytableScrollOfFive(report, CurrencyName);
				return paytableScroll;
			} else if (XpathMap.get("paytableScrollOfNine").equalsIgnoreCase("yes")) {
				paytableScroll = paytableScrollOfNine(report, CurrencyName);
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

	

	public void verifyquickbet(Desktop_HTML_Report report, String CurrencyName ,String regExpr) {
		String betVal ;
		// check if max bet button is visible to know whether bet panel is open
		try
		{	
	    boolean isMaxBet = isElementVisible("isMaxBetVisible");
		if (isMaxBet) {
		
			report.detailsAppend("Verify Max Bet is visible "," Max Bet is visibled", "Max Bet is visible ","Pass"+ CurrencyName);									
			ClickByCoordinates("return " + XpathMap.get("MaxBetCoordinatex"),"return " + XpathMap.get("MaxBetCoordinatey"));
			report.detailsAppend("Verify Max Bet is Clicked "," Max Bet is Clicked", "Max Bet isClicked ","Pass"+ CurrencyName);									
			boolean betAmt = verifyRegularExpression(report, regExpr, getConsoleText("return "+ XpathMap.get("BetTextValue")));
			openBetPanel();		
		}
			boolean bet1 = isElementVisible("isQuickBet1");
			if(bet1) {
				betVal = getConsoleText("return " + XpathMap.get("QuickBet1Value"));	
				report.detailsAppend("Verify Bet1 is visible "," Bet 1 is visibled", "Bet 1 is visible ","Pass"+ CurrencyName);									
				ClickByCoordinates("return " + XpathMap.get("QuickBet1Coordinatex"),"return " + XpathMap.get("QuickBet1Coordinatey"));
				report.detailsAppend("Verify Bet 1 is Clicked "," Bet 1 is Clicked", "Bet 1 isClicked ","Pass" + CurrencyName);							
				
				boolean isBetChangedIntheConsole = isBetChangedIntheConsole(betVal);
				verifyBetValuesAppended(isBetChangedIntheConsole ,betVal ,report);
				openBetPanel();	
				
				
			}
			boolean bet2 = isElementVisible("isQuickBet2");			
            if(bet2) {
            	betVal = getConsoleText("return " + XpathMap.get("QuickBet2Value"));	
            	report.detailsAppend("Verify Bet2 is visible "," Bet 2 is visibled", "Bet 2 is visible ","Pass"+ CurrencyName);									
				ClickByCoordinates("return " + XpathMap.get("QuickBet2Coordinatex"),"return " + XpathMap.get("QuickBet2Coordinatey"));
				report.detailsAppend("Verify Bet 2 is Clicked "," Bet 2 is Clicked", "Bet2 isClicked ","Pass" + CurrencyName);
				
				boolean isBetChangedIntheConsole = isBetChangedIntheConsole(betVal);
				verifyBetValuesAppended(isBetChangedIntheConsole ,betVal ,report);			
				openBetPanel();			
			}
        	boolean bet3 = isElementVisible("isQuickBet3");
            if(bet3) {
            	betVal = getConsoleText("return " + XpathMap.get("QuickBet3Value"));			
            	report.detailsAppend("Verify Bet3 is visible "," Bet 3 is visibled", "Bet 3 is visible ","Pass"+ CurrencyName);									
				ClickByCoordinates("return " + XpathMap.get("QuickBet3Coordinatex"),"return " + XpathMap.get("QuickBet3Coordinatey"));
				report.detailsAppend("Verify Bet 3 is Clicked "," Bet 3 is Clicked", "Bet 3 isClicked ","Pass" + CurrencyName);	
				
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

					report.detailsAppend("Base Game quick button"," Coin size slider is working ", "Coin size slider is working ","Pass" + CurrencyName);
				} else {
					details_append_folderOnlyScreeshot(webdriver,"CoinSizeSliderNtWorking");
					report.detailsAppend("Base Game quick button"," Coin size slider is not working ","Coin size slider is not working ", "FAIL" + CurrencyName);
				}
			}
        	boolean bet4 = isElementVisible("isQuickBet4");
            if(bet4) {
            	betVal = getConsoleText("return " + XpathMap.get("QuickBet4Value"));	
            	report.detailsAppend("Verify Bet4 is visible "," Bet 4 is visibled", "Bet 4 is visible ","Pass" + CurrencyName);									
				ClickByCoordinates("return " + XpathMap.get("QuickBet4Coordinatex"),"return " + XpathMap.get("QuickBet4Coordinatey"));
				report.detailsAppend("Verify Bet 4 is Clicked "," Bet 4 is Clicked", "Bet 4 isClicked ","Pass" + CurrencyName);	
				
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
				log.debug("isCreditDeducted :: PASS for quick bet value =" + quickBetVal);
			} else {
				report.detailsAppend("verify that is Bet Changed In the Console for quick bet  =" + quickBetVal,
						"Is Bet Changed In the Console for quick bet  =" + quickBetVal,
						"Bet not Changed In the Console", "fail");
				log.debug("isCreditDeducted :: FAil for quick bet value =" + quickBetVal);
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
	

	public boolean paytableScrollOfFive(Desktop_HTML_Report report, String CurrencyName) {
		
		String winBoosterXpath = "WinBooster";		
		String mysterySymbolXpath = "MysterySymbol";
		String WildXpath = "Wild";	
		String PaytableGridXpath = "PaytableGrid";
		String PaylineXpath = "Payline";		
		boolean test = false;
		try {
		
		
			report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "PASS", "" + CurrencyName);

			test = webdriver.findElements(By.xpath(XpathMap.get(winBoosterXpath))).size() > 0;
			if (test) {
				JavascriptExecutor js = (JavascriptExecutor) webdriver;
				WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(winBoosterXpath)));
				js.executeScript("arguments[0].scrollIntoView(true);", ele1);
				report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "PASS", "" + CurrencyName);
				Thread.sleep(3000);
				test = true;
			}

			test = webdriver.findElements(By.xpath(XpathMap.get(mysterySymbolXpath))).size() > 0;
			if (test) {
				JavascriptExecutor js = (JavascriptExecutor) webdriver;
				WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(mysterySymbolXpath)));
				js.executeScript("arguments[0].scrollIntoView(true);", ele1);
				report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "PASS", "" + CurrencyName);
				Thread.sleep(3000);
				test = true;
			}
			test = webdriver.findElements(By.xpath(XpathMap.get(WildXpath))).size() > 0;
		
			if (test) {
				JavascriptExecutor js = (JavascriptExecutor) webdriver;
				WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(WildXpath)));
				js.executeScript("arguments[0].scrollIntoView(true);", ele1);
				report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "PASS", "" + CurrencyName);
				Thread.sleep(3000);
				test = true;
			}

			test = webdriver.findElements(By.xpath(XpathMap.get(PaytableGridXpath))).size() > 0;
			if (test) {
				JavascriptExecutor js = (JavascriptExecutor) webdriver;
				WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(PaytableGridXpath)));
				js.executeScript("arguments[0].scrollIntoView(true);", ele1);
				report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "PASS", "" + CurrencyName);
				Thread.sleep(4000);
				test = true;
			}

			test = webdriver.findElements(By.xpath(XpathMap.get(PaylineXpath))).size() > 0;
			if (test) {
				JavascriptExecutor js = (JavascriptExecutor) webdriver;
				WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(PaylineXpath)));
				js.executeScript("arguments[0].scrollIntoView(true);", ele1);
				report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "PASS", "" + CurrencyName);
				Thread.sleep(3000);
				test = true;
			}
			Thread.sleep(2000);
			// method is for validating the payatable Branding
			textValidationForPaytableBranding(report, CurrencyName);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return test;
	}
	
	/**
	 * Verifies the Paytable text validation 
	 * 
	 */
	public String textValidationForPaytableBranding(Desktop_HTML_Report report,String CurrencyName) 
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
				System.out.println("Powered By MicroGaming Text : PASS");log.debug("Powered By MicroGaming Text : PASS");
				report.detailsAppendFolder("Paytable Branding ", "Branding Text ", ""+PaytableBranding, "PASS",""+CurrencyName);

			}
			else
			{
				System.out.println("Powered By MicroGaming Text : FAIL");log.debug("Powered By MicroGaming Text : FAIL");
				report.detailsAppendFolder("Paytable Branding ", "Branding Text ", ""+PaytableBranding, "FAIL",""+CurrencyName);

			}}
		
		else
		{
			System.out.println("Powered By MicroGaming Text : FAIL");
			report.detailsAppendFolder("Paytable", "Branding is not available ", ""+PaytableBranding, "FAIL",""+CurrencyName);
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
	 * @param CurrencyName
	 * @return
	 */
	public boolean paytableScrollOfNine(Desktop_HTML_Report report, String CurrencyName) {
		String winUpto = "WinUpTo"; 
		String wildXpath = "Wild";
		String scatterXpath = "Scatter";	
		String freeSpine = "FreeSpine";		
		String symbolGridXpath = "PaytableGrid";
		String symbolGridXpath5 = "PaytableGrid5";	
		String paylines = "Payline";	
		boolean test = false;
		try {
						
			test = webdriver.findElements(By.xpath(XpathMap.get(winUpto))).size() > 0;
			if (test) {
				JavascriptExecutor js = (JavascriptExecutor) webdriver;
				WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(winUpto)));
				js.executeScript("arguments[0].scrollIntoView(true);", ele1);
				report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "PASS", "" + CurrencyName);
				Thread.sleep(3000);
				test = true;
			}

			test = webdriver.findElements(By.xpath(XpathMap.get(wildXpath))).size() > 0;
			if (test) {
				JavascriptExecutor js = (JavascriptExecutor) webdriver;
				WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(wildXpath)));
				js.executeScript("arguments[0].scrollIntoView(true);", ele1);
				report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "PASS", "" + CurrencyName);
				Thread.sleep(3000);
				test = true;
			}

			test = webdriver.findElements(By.xpath(XpathMap.get(freeSpine))).size() > 0;
			if (test) {
				JavascriptExecutor js = (JavascriptExecutor) webdriver;
				WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(freeSpine)));
				js.executeScript("arguments[0].scrollIntoView(true);", ele1);
				report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "PASS", "" + CurrencyName);
				Thread.sleep(4000);
				test = true;
			}
			test = webdriver.findElements(By.xpath(XpathMap.get(scatterXpath))).size() > 0;
			if (test) {
				JavascriptExecutor js = (JavascriptExecutor) webdriver;
				WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(scatterXpath)));
				js.executeScript("arguments[0].scrollIntoView(true);", ele1);
				report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "PASS", "" + CurrencyName);
				Thread.sleep(4000);
				test = true;
			}

			test = webdriver.findElements(By.xpath(XpathMap.get(symbolGridXpath))).size() > 0;
			if (test) {
				JavascriptExecutor js = (JavascriptExecutor) webdriver;
				WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(symbolGridXpath)));
				js.executeScript("arguments[0].scrollIntoView(true);", ele1);
				report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "PASS", "" + CurrencyName);
				Thread.sleep(4000);
				test = true;
			}

			test = webdriver.findElements(By.xpath(XpathMap.get(symbolGridXpath5))).size() > 0;
			if (test) {
				JavascriptExecutor js = (JavascriptExecutor) webdriver;
				WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(symbolGridXpath5)));
				js.executeScript("arguments[0].scrollIntoView(true);", ele1);
				report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "PASS", "" + CurrencyName);
				Thread.sleep(4000);
				test = true;
			}
			test = webdriver.findElements(By.xpath(XpathMap.get(paylines))).size() > 0;
			if (test) {
				JavascriptExecutor js = (JavascriptExecutor) webdriver;
				WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(paylines)));
				js.executeScript("arguments[0].scrollIntoView(true);", ele1);
				report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "PASS", "" + CurrencyName);
				Thread.sleep(4000);
				test = true;
			}
		

			
	
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
	public boolean validatePayoutsFromPaytable(Desktop_HTML_Report report, String CurrencyName, String regExpr) 																												
	{
		boolean payoutvalues = false;
		try {
			if (XpathMap.get("paytableScrollOfNine").equalsIgnoreCase("yes")) {				
				payoutvalues = verifyRegularExpressionUsingArrays(report, regExpr,paytablePayoutsOfScatter(report, CurrencyName));
				return payoutvalues;
			}else if (XpathMap.get("paytableScrollOfFive").equalsIgnoreCase("yes")) {				
				payoutvalues = verifyRegularExpressionUsingArrays(report, regExpr,paytablePayoutsOfScatterWild(report, CurrencyName));
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
	public String[] paytablePayoutsOfScatter(Desktop_HTML_Report report, String CurrencyName) // String[] array
	{
		String symbols[] = { "Scatter5", "Scatter4", "Scatter3"};

		
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
	public String[] paytablePayoutsOfScatterWild(Desktop_HTML_Report report, String CurrencyName) // String[] array
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
	public String freeSpinsSummaryScreen(Desktop_HTML_Report report, String CurrencyName) {
		String fsSummaryScreen = null;
		Wait = new WebDriverWait(webdriver, 350);
		try {
			fsSummaryScreen = func_GetTextbyAttribute(report, "FSSummaryScreenWinAmt", CurrencyName);
			report.detailsAppendFolderOnlyScreenShot(CurrencyName);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return fsSummaryScreen;
	}

	/**
	 * this methods gets an attribute values
	 */
	public String func_GetTextbyAttribute(Desktop_HTML_Report report, String locator, String CurrencyName) {
		try {
			WebElement ele = webdriver.findElement(By.xpath(XpathMap.get(locator)));
			ele.getAttribute("textvalue");
			System.out.println("Text is : " + ele.getAttribute("textvalue"));
			log.debug("Amount is :" + ele.getAttribute("textvalue"));
			// report.detailsAppendFolder("Text", " Win Amt ",
			// ""+ele.getAttribute("textvalue"), "PASS",CurrencyName);
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

	public boolean verifyRegularExpression(Desktop_HTML_Report isoCode, String regExp, String method) {
		String Text = method;
		boolean regexp = false;
		try {
			Thread.sleep(2000);
			String Text1=Text.replaceAll("[a-zA-Z:]", "").trim();
			Text1=Text1.replaceAll(" ", "").trim();	
			if (Text1.matches(regExp)) {
				log.debug("Compared with Reg Expression .Currency is same");
				regexp = true;
			}
			Thread.sleep(2000);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return regexp;
	}
	
	
	public boolean verifyGridPayouts(Desktop_HTML_Report report, String regExp,String CurrencyName) {		

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
	                    if(((currentime-startTime)/500)> 30)
	                    {
	                    log.info("No value present after 30 seconds= "+ele);
	                    System.out.println("No value present after 60 seconds= "+ele);
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
		
		public String getCurrentFSWinAmt(Desktop_HTML_Report report,String FSwinamtVisible,String FSwinAmt,String CurrencyName ) 
		{
			String FSWinAmt = null;
			
			try
			{	
		    report.detailsAppendFolderOnlyScreenShot(CurrencyName);
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
				while(isElementVisible(FGSummaryBackToGameBtn)==false)
				{					
					imageLibrary.click("Spin");
					Thread.sleep(7000);
				}
			}
			catch(Exception e)
			{
				log.error(e.getMessage(), e);
			}
		}
		
		public boolean validatePayouts(Desktop_HTML_Report report, String CurrencyName, String regExpr) 																												
		{
			boolean payoutvalues = false;
			try {
				if (XpathMap.get("PaytablePayoutsofNine").equalsIgnoreCase("yes")) {
					
					payoutvalues = verifyRegularExpressionUsingArrays(report, regExpr,payOuts(report, CurrencyName));
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
		
		public String[] payOuts(Desktop_HTML_Report report, String CurrencyName){
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
		
		public String freeGameOnRefresh(Desktop_HTML_Report report,String currencyName,String isResumeButtonVisible, String infoBtn, String infoBtnx,String infoBtny, String infoTxt) 
		{
			String onRefresh = "";
			try 
			{
				report.detailsAppendFolder("Free Game", "Refresh", "Refresh", "PASS",currencyName);
				boolean isResumeBtnVisible = waitForElement(isResumeButtonVisible);
				if(isResumeBtnVisible) 
				{
					report.detailsAppendFolder("Free Games on Refresh", "Resume Button", "Resume Button", "PASS",currencyName); 
					//validate the Entry Screen Info Text 	
					onRefresh =	freeGameEntryInfo(report,currencyName, infoBtn,"return "+XpathMap.get(infoBtnx),"return "+XpathMap.get(infoBtny),"return "+XpathMap.get(infoTxt));
					//Click on Resume Button on Refresh 
					//func_Click("ResumeButton");
					return onRefresh;
					
			    } 
				else 
				{
					log.debug("Free Games Resume Button Text is : - FAIL");
					System.out.println("Free Games Resume Button Text is : - FAIL");
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
				//report.detailsAppendFolder("Free Games","Info Text","Info Text", "PASS", currencyName);
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
					report.detailsAppendFolder("Free Games","Info Button Click ","Info Button Click ", "PASS", currencyName);
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
						//report.detailsAppendFolder("Free Games","Info Text","Info Text", "PASS", currencyName);
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
			public String verifyFreeSpinBigWin(Desktop_HTML_Report report, String CurrencyName) {
				String bigWinAmt = null;
				Wait = new WebDriverWait(webdriver, 3000);

				try {
					boolean isBigWin = isElementVisible("FSBigWin");
					if (isBigWin) {
						Thread.sleep(8000);				
						bigWinAmt = getConsoleText("return " + XpathMap.get("FSBigWinValue"));
						System.out.println("FreeSpine Big Win Amount is " + bigWinAmt);
						log.debug(" Big Win Amount is " + bigWinAmt);
						report.detailsAppendFolderOnlyScreenShot(CurrencyName);

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
			public String verifyFSCurrentWinAmt(Desktop_HTML_Report report, String CurrencyName) {
				String winAmt = null;
				Wait = new WebDriverWait(webdriver, 250);
				try {
					report.detailsAppendFolderOnlyScreenShot(CurrencyName);

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
					boolean isBigWin = isElementVisible("isBigWinPresent");
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
					//	report.detailsAppendFolderOnlyScreenShot(CurrencyName);

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
			public String verifyFreeSpinBonusWin(Desktop_HTML_Report report, String CurrencyName) {
				String bigWinAmt = null;
				Wait = new WebDriverWait(webdriver, 3000);

				try {
					boolean isBigWin = isElementVisible("FSBigWin");
					if (isBigWin) {
						Thread.sleep(8000);				
						bigWinAmt = getConsoleText("return " + XpathMap.get("FSBonusWin"));
						System.out.println("FreeSpine Bonus Win Amount is " + bigWinAmt);
						log.debug(" Bonus win Amount is " + bigWinAmt);
						report.detailsAppendFolderOnlyScreenShot(CurrencyName);

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
			public String getCurrentWinAmt(Desktop_HTML_Report report, String CurrencyName) {
				String winAmt = null;
				Wait = new WebDriverWait(webdriver, 250);
				try {
					report.detailsAppendFolderOnlyScreenShot(CurrencyName);

					boolean isWinAmt = isElementVisible("isTotalWinAmt");
					if (isWinAmt) {
						winAmt = getConsoleText("return " + XpathMap.get("TotalWinAmt"));
						System.out.println(" Win Amount is " + winAmt);
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
			 * Verifies the Big Win
			 * VC66297
			 */
			public String verifyBigWin(Desktop_HTML_Report report, String CurrencyName) {
				String bigWinAmt = null;
				Wait = new WebDriverWait(webdriver, 30000);

				try {
					boolean isBigWin = isElementVisible("BigWin");
					if (isBigWin) {
						Thread.sleep(8000);				
						bigWinAmt = getConsoleText("return " + XpathMap.get("BigWinValue"));
						System.out.println("Big Win Amount is " + bigWinAmt);
						log.debug(" Big Win Amount is " + bigWinAmt);
						report.detailsAppendFolderOnlyScreenShot(CurrencyName);

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

			public String verifyAmgWin(Desktop_HTML_Report report, String CurrencyName) {
				String amgWinAmt = null;
				Wait = new WebDriverWait(webdriver, 30000);

				try {
					boolean isAmgWin = isElementVisible("isAmgWinAmt");
					if (isAmgWin) {
						Thread.sleep(8000);			
						amgWinAmt = getConsoleText("return " + XpathMap.get("AmgWinAmt"));
						System.out.println("Amzing Win Amount is " + amgWinAmt);
						log.debug(" Big Win Amount is " + amgWinAmt);
						report.detailsAppendFolderOnlyScreenShot(CurrencyName);

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
			public String getCurrentWinAmt1(Desktop_HTML_Report report, String CurrencyName) {
				String winAmt = null;
				Wait = new WebDriverWait(webdriver, 250);
				try {
					report.detailsAppendFolderOnlyScreenShot(CurrencyName);

					boolean isWinAmt = isElementVisible("isTotalWinAmt");
					if (isWinAmt) {
						
						winAmt = getConsoleText("return " + XpathMap.get("TotalWinAmt"));
						/*while(winAmt.equals(""))
						{					
												
							Thread.sleep(2000);
							winAmt = getConsoleText("return " + XpathMap.get("TotalWinAmt"));
						}*/
						System.out.println(" Win Amount is " + winAmt);
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
			 * Verifies the Big Win for workaround
			 * VC66297
			 */
			public String verifyBigWin(Desktop_HTML_Report report, String CurrencyName,String SpinBtnCoordinatex ,String SpinBtnCoordinatey) {
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
					//	report.detailsAppendFolderOnlyScreenShot(CurrencyName);

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
			
			public String verifyAmgWin(Desktop_HTML_Report report, String CurrencyName,String SpinBtnCoordinatex ,String SpinBtnCoordinatey) {
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
						report.detailsAppendFolderOnlyScreenShot(CurrencyName);

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
		    public void navigate_back(Desktop_HTML_Report report, String languageCurrency) throws InterruptedException {
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
		                report.detailsAppendFolder("Verify if help navigation ", "Help should navigated", "Help is navigated", "PASS",languageCurrency);



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
			
			public void verifyquickbetImg(Desktop_HTML_Report report, String CurrencyName ,String regExpr, ImageLibrary imageLibrary) {
				String betVal ;
				// check if max bet button is visible to know whether bet panel is open
				try
				{	
			    boolean isMaxBet = imageLibrary.isImageAppears("BetMax");
			    if(isMaxBet) {
					report.detailsAppendFolder("Verify Max Bet is visible "," Max Bet is visibled", "Max Bet is visible ","Pass",CurrencyName);									
					imageLibrary.click("BetMax");
					report.detailsAppendFolder("Verify Max Bet is Clicked "," Max Bet is Clicked", "Max Bet isClicked ","Pass",CurrencyName);									
					boolean betAmt = verifyRegularExpression(report, regExpr, getConsoleText("return "+ XpathMap.get("BetTextValue")));
						
				
				}
/*				Thread.sleep(3000);
				
					boolean bet1 = isElementVisible("isQuickBet1");
					if(bet1) {
						betVal = getConsoleText("return " + XpathMap.get("QuickBet1Value"));	
						//report.detailsAppendFolder("Verify Bet1 is visible "," Bet 1 is visibled", "Bet 1 is visible ","Pass", CurrencyName);									
						ClickByCoordinates("return " + XpathMap.get("QuickBet1Coordinatex"),"return " + XpathMap.get("QuickBet1Coordinatey"));
						Thread.sleep(1000);
						report.detailsAppendFolder("Verify Bet 1 is Clicked "," Bet 1 is Clicked", "Bet 1 isClicked ","Pass" , CurrencyName);							
						
						boolean isBetChangedIntheConsole = isBetChangedIntheConsole(betVal);
						verifyBetValuesAppended(isBetChangedIntheConsole ,betVal ,report,CurrencyName);
						imageLibrary.click("BetButton");
						Thread.sleep(2000);
						
						
					}
					boolean bet2 = isElementVisible("isQuickBet2");			
		            if(bet2) {
		            	betVal = getConsoleText("return " + XpathMap.get("QuickBet2Value"));	
		            	//report.detailsAppendFolder("Verify Bet2 is visible "," Bet 2 is visibled", "Bet 2 is visible ","Pass", CurrencyName);									
						ClickByCoordinates("return " + XpathMap.get("QuickBet2Coordinatex"),"return " + XpathMap.get("QuickBet2Coordinatey"));
						Thread.sleep(1000);
						report.detailsAppendFolder("Verify Bet 2 is Clicked "," Bet 2 is Clicked", "Bet2 isClicked ","Pass" , CurrencyName);
						
						boolean isBetChangedIntheConsole = isBetChangedIntheConsole(betVal);
						verifyBetValuesAppended(isBetChangedIntheConsole ,betVal ,report,CurrencyName);			
						imageLibrary.click("BetButton");
						Thread.sleep(2000);		
					}
		        	boolean bet3 = isElementVisible("isQuickBet3");
		            if(bet3) {
		            	betVal = getConsoleText("return " + XpathMap.get("QuickBet3Value"));			
		            	//report.detailsAppendFolder("Verify Bet3 is visible "," Bet 3 is visibled", "Bet 3 is visible ","Pass", CurrencyName);									
						ClickByCoordinates("return " + XpathMap.get("QuickBet3Coordinatex"),"return " + XpathMap.get("QuickBet3Coordinatey"));
						Thread.sleep(1000);
						report.detailsAppendFolder("Verify Bet 3 is Clicked "," Bet 3 is Clicked", "Bet 3 isClicked ","Pass" , CurrencyName);	
						
						boolean isBetChangedIntheConsole = isBetChangedIntheConsole(betVal);
						verifyBetValuesAppended(isBetChangedIntheConsole ,betVal ,report,CurrencyName);				
						imageLibrary.click("BetButton");
						Thread.sleep(2000);			
						
					}
		        	boolean bet4 = isElementVisible("isQuickBet4");
		            if(bet4) {
		            	betVal = getConsoleText("return " + XpathMap.get("QuickBet4Value"));	
		            	//report.detailsAppendFolder("Verify Bet4 is visible "," Bet 4 is visible", "Bet 4 is visible ","Pass" , CurrencyName);									
						ClickByCoordinates("return " + XpathMap.get("QuickBet4Coordinatex"),"return " + XpathMap.get("QuickBet4Coordinatey"));
						Thread.sleep(1000);
						report.detailsAppendFolder("Verify Bet 4 is Clicked "," Bet 4 is Clicked", "Bet 4 isClicked ","Pass" , CurrencyName);	
						
						boolean isBetChangedIntheConsole = isBetChangedIntheConsole(betVal);
						verifyBetValuesAppended(isBetChangedIntheConsole ,betVal ,report,CurrencyName);				
						imageLibrary.click("BetButton");
						Thread.sleep(2000);			
						
					}	
		            
		            boolean bet5 = isElementVisible("isQuickBet5");
		            if(bet5) {
		            	betVal = getConsoleText("return " + XpathMap.get("QuickBet5Value"));	
		            	//.detailsAppendFolder("Verify Bet 5 is visible "," Bet 5 is visibled", "Bet 5 is visible ","Pass" , CurrencyName);									
						ClickByCoordinates("return " + XpathMap.get("QuickBet5Coordinatex"),"return " + XpathMap.get("QuickBet5Coordinatey"));
						Thread.sleep(1000);
						report.detailsAppendFolder("Verify Bet 5 is Clicked "," Bet 5 is Clicked", "Bet 5 is Clicked ","Pass" , CurrencyName);	
						
						boolean isBetChangedIntheConsole = isBetChangedIntheConsole(betVal);
						verifyBetValuesAppended(isBetChangedIntheConsole ,betVal ,report,CurrencyName);				
						imageLibrary.click("BetButton");
						Thread.sleep(2000);			
						
					}					

		            boolean bet6 = isElementVisible("isQuickBet6");
		            if(bet6) {
		            	betVal = getConsoleText("return " + XpathMap.get("QuickBet6Value"));	
		            	//report.detailsAppendFolder("Verify Bet 6 is visible "," Bet 6 is visibled", "Bet 6 is visible ","Pass" , CurrencyName);									
						ClickByCoordinates("return " + XpathMap.get("QuickBet6Coordinatex"),"return " + XpathMap.get("QuickBet6Coordinatey"));
						Thread.sleep(1000);
						report.detailsAppendFolder("Verify Bet 6 is Clicked "," Bet 6 is Clicked", "Bet 6 is Clicked ","Pass" , CurrencyName);	
						
						boolean isBetChangedIntheConsole = isBetChangedIntheConsole(betVal);
						verifyBetValuesAppended(isBetChangedIntheConsole ,betVal ,report,CurrencyName);									
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
	               
	           public String verifyBigWinImg(Desktop_HTML_Report report,ImageLibrary imageLibrary) {
	               String bigWinAmt = null;
	               
	              try {
	                   boolean isBigWin = isElementVisible("isBigWinPresent");
	                   if (isBigWin) {
	                       Thread.sleep(8000);                
	                       bigWinAmt = getConsoleText("return " + XpathMap.get("BigWinAmt"));
	                       System.out.println("Base Game Big Win Amount is " + bigWinAmt);
	                       log.debug("Base Game Big Win Amount is " + bigWinAmt);
	                       while(bigWinAmt.equals(""))
	                       {                    
	                           imageLibrary.click("Spin");    
	                           Thread.sleep(7000);
	                           bigWinAmt = getConsoleText("return " + XpathMap.get("TotalWinAmt"));
	                       }
	                   //    report.detailsAppendFolderOnlyScreenShot(CurrencyName);
	                  } else {
	                       System.out.println("There is no Big Win ");
	                       log.debug("There is no Big Win");
	                   }
	               } catch (Exception e) {
	                   log.error(e.getMessage(), e);
	               }
	               return bigWinAmt;
	          }
				
	           public String verifyAmgWinImg(Desktop_HTML_Report report, ImageLibrary imageLibrary) {
	       		String amgWinAmt = null;
	       		Wait = new WebDriverWait(webdriver, 30000);

	       		try {
	       			boolean isAmgWin = isElementVisible("isAmgWinAmt");
	       			if (isAmgWin) {	
	       				amgWinAmt = getConsoleText("return " + XpathMap.get("AmgWinAmt"));
	       				System.out.println("Amzing Win Amount is " + amgWinAmt);
	       				log.debug(" Big Win Amount is " + amgWinAmt);
	       			  while(amgWinAmt.equals(""))
	                  {                    
	                      imageLibrary.click("Spin");    
	                      Thread.sleep(7000);
	                      amgWinAmt = getConsoleText("return " + XpathMap.get("TotalWinAmt"));
	                  }
	       				//report.detailsAppendFolderOnlyScreenShot(CurrencyName);

	       			} else {
	       				System.out.println("There is no Amg Win ");
	       				log.debug("There is no Amg Win");
	       			}
	       		} catch (Exception e) {
	       			log.error(e.getMessage(), e);
	       		}
	       		return amgWinAmt;

	       	}
}
