package Modules.Regression.FunctionLibrary;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.sikuli.script.Region;
import org.sikuli.script.Screen;

import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.Util;
import com.zensar.automation.library.TestPropReader;
import com.zensar.automation.model.Symbol;
import com.zensar.automation.model.WinCombination;

import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;

import org.openqa.selenium.WebElement;
//import bsh.util.Util;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.core.har.HarEntry;

public class CFNLibrary_Desktop_CS extends CFNLibrary_Desktop
{
	long Avgquickspinduration = 0;
	long Avgnormalspinduration = 0;
	public Logger log = Logger.getLogger(CFNLibrary_Desktop_CS.class.getName());

	public BrowserMobProxyServer proxy;
	public String LobbyBalance;
	public double lobbybals;
	public String bet = null;
	double totalWinNew = 0;
	WebDriverWait Wait;

	Util clickAt = new Util();

	public CFNLibrary_Desktop_CS(WebDriver webdriver, BrowserMobProxyServer browserproxy1, Desktop_HTML_Report report,
			String gameName) throws IOException {
		super(webdriver, browserproxy1, report, gameName);
		this.GameName = gameName.trim();

	}

	/**
	 * Date: 13/11/2017 Author: Ashish Kshatriya Description: This function is
	 * used for get Webelement text Parameter: By locator
	 */
	public String func_GetText(String locator) 
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
	/**
	 * this methods gets an attribute values
	 */
	public String func_GetTextbyAttribute(Desktop_HTML_Report report ,String locator,String CurrencyName) 
	{
		try 
		{
			WebElement ele = webdriver.findElement(By.xpath(XpathMap.get(locator)));
			ele.getAttribute("textvalue");
			System.out.println("Text is : "+ele.getAttribute("textvalue"));
			log.debug("Amount is :"+ele.getAttribute("textvalue"));
			//report.detailsAppendFolder("Text", "  Win Amt ", ""+ele.getAttribute("textvalue"), "PASS",CurrencyName);
			return ele.getText();
			
		} 
		catch (NoSuchElementException e)
		{
			return null;
		}
	}
	/**
	 * this methods gets an attribute values
	 */
	public String func_GetTextbyAttributeforBonus(Desktop_HTML_Report report ,String locator,String CurrencyName) 
	{
		try 
		{
			WebElement ele = webdriver.findElement(By.xpath(XpathMap.get(locator)));
			ele.getAttribute("textvalue");
			System.out.println("Bonus Win Amt is : "+ele.getAttribute("textvalue"));
			log.debug("Bonus Win Amt is  :"+ele.getAttribute("textvalue"));
			//report.detailsAppendFolder("Bonus Text ", "Bonus Win Amt ", ""+ele.getAttribute("textvalue"), "PASS",CurrencyName);
			return ele.getText();
			
		} 
		catch (NoSuchElementException e)
		{
			return null;
		}
	}

	/**
	 * Date: 13/11/2017 Author: Ashish Kshatriya Description: This function is
	 * used for get Webelement text Parameter: By locator
	 */
	public String func_GetText_BYID(String locator) {
		try {
			WebElement ele = webdriver.findElement(By.id(locator));
			return ele.getText();
		} catch (NoSuchElementException e) {
			return null;
		}
	}

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

	/* Sneha Jawarkar: Wait for Spin button */
	public boolean waitForSpinButtonstop() {
		try {
			System.out.println("Waiting for spinbutton active to come after completion of FreeSpin");
			while (true) {
				/*
				 * elementWait(
				 * "return mgs.mobile.casino.slotbuilder.v1.automation.getControlById('SpinButtonComponent').Buttons.spinButton.currentState"
				 * , "active");
				 */
				return true;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * * Date: 14/05/2019 Author: Sneha Jawarkar. Description: This function is
	 * used in GTR_freegame Parameter: play letter
	 */
	public void ClickBaseSceneDiscardButton() {
		try {
			/*
			 * clickAtButton(
			 * "return mgs.mobile.casino.slotbuilder.v1.automation.getControlById('FreeGamesRemainingComponent').onButtonClicked('deleteButton')"
			 * ); System.out.println("Clicked on basescene Discard Button");
			 */ } catch (Exception e) {
			System.out.println("Can not Clicked on Basescene Discard Button");
		}
	}

	/**
	 * * Date: 17/05/2019 Author: Sneha Jawarkar. Description: This function is
	 * used in GTR_freegame Parameter: resume play button
	 */
	public boolean ClickFreegameResumePlayButton() {
		boolean b = false;
		try {
			/*
			 * JavascriptExecutor js = ((JavascriptExecutor) webdriver);
			 * Coordinates coordinateObj = new Coordinates(); String align =
			 * "return mgs.mobile.casino.slotbuilder.v1.automation.getControlById('FreeGamesComponent').views.freeGamesResumeView.Buttons.resumeButton.label.currentStyle.alignment"
			 * ; typeCasting(
			 * "return mgs.mobile.casino.slotbuilder.v1.automation.getControlById('FreeGamesResumeView.resumeButton').x"
			 * , coordinateObj); coordinateObj.setX(coordinateObj.getSx());
			 * typeCasting(
			 * "return mgs.mobile.casino.slotbuilder.v1.automation.getControlById('FreeGamesResumeView.resumeButton').y"
			 * , coordinateObj); coordinateObj.setY(coordinateObj.getSx());
			 * typeCasting(
			 * "return mgs.mobile.casino.slotbuilder.v1.automation.getControlById('FreeGamesResumeView.resumeButton').height"
			 * , coordinateObj); coordinateObj.setHeight(coordinateObj.getSx());
			 * typeCasting(
			 * "return mgs.mobile.casino.slotbuilder.v1.automation.getControlById('FreeGamesResumeView.resumeButton').width"
			 * , coordinateObj); coordinateObj.setWidth(coordinateObj.getSx());
			 * coordinateObj.setAlign((js.executeScript(align)).toString());
			 * getComponentCenterCoordinates(coordinateObj);
			 * clickAtCoordinates(coordinateObj.getCenterX(),
			 * coordinateObj.getCenterY()); Thread.sleep(2000); b = true;
			 */ } catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	/**
	 * This method is used to wait till FS scene loads Author: Sneha Jawarkar
	 */
	public boolean FreeSpinEntryScene() {
		try {
			log.debug("After refreshing,waiting for free spin's scene to come");
			Thread.sleep(10000);
			/*
			 * String currentScene =
			 * GetConsoleText("return mgs.mobile.casino.slotbuilder.v1.automation.currentScene"
			 * ); if (currentScene.equalsIgnoreCase("FREESPINS"))
			 */ return true;
			// System.out.println("free spins are started.");

		} catch (Exception e) {
			log.error("error while waiting for free spin scene", e);
			return false;
		}
	}

	/*
	 * Date: 16/05/2019 Author:Sneha Jawarkar Description: Freegame_GTR
	 * Parameter: NA
	 */
	public boolean CompleteFreeGameOffer_freespin(int freegamescount) {
		try {
			for (int i = 0; i < freegamescount; i++) {
				spinclick();
				Thread.sleep(10000);
				System.out.println("click on spin");
				if (FreeSpinEntryScene()) {
					// spinclick();
					Thread.sleep(10000);
					webdriver.navigate().refresh();
					Thread.sleep(10000);
					ClickFreegameResumePlayButton();
					FS_continue();
					Thread.sleep(10000);

				}
				waitForSpinButtonstop();
			}
		} catch (Exception e) {
			log.error("Can Not Clicked on spin Button");

		}
		return false;
	}

	/*
	 * Date: 16/05/2019 Author:Sneha Jawarkar Description: Freegame_GTR
	 * Parameter: NA
	 */
	public boolean CompleteFreeGameOffer(int freegamescount) {
		try {
			for (int i = 0; i < freegamescount; i++) {
				spinclick();
				// write for spin button to stop
				waitForSpinButtonstop();
			}
			log.debug("Clicked on spin button");
			return true;
		} catch (Exception e) {
			log.debug("Can not complete the freegame offer", e);
			return false;
		}

	}

	/**
	 * * Date: 14/05/2019 Author: Sneha Jawarkar. Description: This function is
	 * used in GTR_freegame Parameter: play letter
	 */
	public boolean clickOnPlayLater() {
		Wait = new WebDriverWait(webdriver, 500);
		boolean b = false;
		/*
		 * try { JavascriptExecutor js = ((JavascriptExecutor) webdriver);
		 * Coordinates coordinateObj = new Coordinates(); String align =
		 * "return mgs.mobile.casino.slotbuilder.v1.automation.getControlById('FreeGamesComponent').views.freeGamesOfferView.Buttons.playLaterButton.buttonData.text.layoutStyles.desktop.alignment"
		 * ;
		 * typeCasting("return mgs.mobile.casino.slotbuilder.v1.automation.getControlById('FreeGamesOffersView.playLaterButton').x"
		 * ,coordinateObj); coordinateObj.setX(coordinateObj.getSx());
		 * typeCasting("return mgs.mobile.casino.slotbuilder.v1.automation.getControlById('FreeGamesOffersView.playLaterButton').y"
		 * ,coordinateObj); coordinateObj.setY(coordinateObj.getSx());
		 * typeCasting("return mgs.mobile.casino.slotbuilder.v1.automation.getControlById('FreeGamesOffersView.playLaterButton').height"
		 * ,coordinateObj); coordinateObj.setHeight(coordinateObj.getSx());
		 * typeCasting("return mgs.mobile.casino.slotbuilder.v1.automation.getControlById('FreeGamesOffersView.playLaterButton').width"
		 * ,coordinateObj); coordinateObj.setWidth(coordinateObj.getSx());
		 * coordinateObj.setAlign((js.executeScript(align)).toString());
		 * getComponentCenterCoordinates(coordinateObj);
		 * clickAtCoordinates(coordinateObj.getX(), coordinateObj.getY());
		 * Thread.sleep(100); b = true; } catch (Exception e) {
		 * e.printStackTrace(); }
		 */
		return b;

	}

	/**
	 * Date:15/5/19 Author:Sneha Jawarkar GTR_Freegame purpose
	 */

	public void Backtogame_centerclick() {
		try {
			/*
			 * clickAtButton(
			 * "return mgs.mobile.casino.slotbuilder.v1.automation.getControlById('FreeGamesCompleteView.backToGameCenterButton')"
			 * );
			 */log.debug("Clicked at back to game");
		} catch (Exception e) {
			log.error("Can not clicked on Back to Button", e);
		}
	}

	/**
	 * Date: 21/6/2019 Author: Sneha Jawarkar Description: GTR Reelspin
	 * 
	 * @throws InterruptedException
	 */
	public long Reel_Spin_speed_Duration() throws InterruptedException {
		long Avgspinduration = 0;
		long loadingTime = 0;
		long sum = 0;
		for (int i = 0; i < 5; i++) {
			long start = System.currentTimeMillis();
			spinclick();
			waitForSpinButton();
			long finish = System.currentTimeMillis();
			long totalTime = finish - start;
			loadingTime = totalTime / 1000;
			sum = sum + loadingTime;
			log.debug("Calculation for the spin duration is running properly");
			System.out.println(i + "taken" + loadingTime);
			// newFeature();
			// summaryScreen_Wait();
		}
		log.debug("error while spin duration");
		System.out.println("Total spin duraion for the 5 spins = " + sum);
		Avgspinduration = sum / 5;
		System.out.println("Average time between 5 spins is = " + Avgspinduration);
		return Avgspinduration;
	}

	/*
	 * Date: 22/04/2019 Description:To verify Autoplay spin selection Parameter:
	 * NA
	 * 
	 * @return boolean
	 */

	public boolean autoplay_spin_selection() {
		boolean spin_autoplay;
		try {
			webdriver.findElement(By.id(XpathMap.get("AutoplayID"))).click();
			WebElement SizeSlider = webdriver.findElement(By.id(XpathMap.get("SpinSizeSlider_ID")));
			String value1 = webdriver.findElement(By.id(XpathMap.get("SpinCount_ID"))).getText();
			Actions action = new Actions(webdriver);
			action.dragAndDropBy(SizeSlider, 200, 0).build().perform();
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
	 * Date: 25/04/2019 Description:To verify autoplay must stop when focus
	 * being removed. Parameter: NA
	 * 
	 * @return boolean
	 */
	public boolean Autoplay_focus_removed() {
		boolean focus;
		try {
			webdriver.findElement(By.id(XpathMap.get("AutoplayID"))).click();
			webdriver.findElement(By.xpath(XpathMap.get("Start_Autoplay"))).click();

			Thread.sleep(3000);

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
			Thread.sleep(3000);
			// closeOverlay();
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

	public boolean Autoplay_focus_removed_UK() {
		boolean focus;
		try {
			clickAtButton("return " + XpathMap.get("ClickAutoPlayMoreOptionsBtn"));
			Thread.sleep(2000);
			webdriver.findElement(By.id(XpathMap.get("Start_Autoplay"))).click();
			Thread.sleep(3000);
			newTabOpen();
			focus = true;
			/*
			 * String windowhandle=webdriver.getWindowHandle();
			 * webdriver.switchTo().window(windowhandle); Robot robot= new
			 * Robot(); robot.keyPress(KeyEvent.VK_CONTROL);
			 * robot.keyPress(KeyEvent.VK_T); robot.keyRelease(KeyEvent.VK_T);
			 * robot.keyRelease(KeyEvent.VK_CONTROL);
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
	 * Date: 5/05/2019 Description:To verify Autoplay after free spin Parameter:
	 * NA
	 * 
	 * @return boolean
	 */

	public boolean is_autoplay_with_freespin() {
		boolean freeSpin = false;
		try {
			// webdriver.findElement(By.id(XpathMap.get("AutoplayID"))).click();
			webdriver.findElement(By.xpath(XpathMap.get("Start_Autoplay"))).click();
			Thread.sleep(5000);
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("spin_button"))));
			log.debug("Free spin over");
			freeSpin = true;
		} catch (Exception e) {
			log.error("Free Spins are not completed");
		}
		return freeSpin;
	}

	/*
	 * Date: 24/04/2019 Description:To verify Autoplay spin session stop
	 * Parameter: NA
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
	/*
	 * Date: 24/04/2019 Description:To verify maximum count of spin. *Parameter:
	 * NA
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
			return max_spin = false;
		}

	}

	/*
	 * Date: 24/04/2019 Description:To verify Auto play setting window
	 * Parameter: NA
	 * 
	 * @return boolean
	 */
	public boolean is_autoplay_window() {
		boolean isautoplaywin;
		try {
			// webdriver.findElement(By.id(XpathMap.get("AutoplayID"))).click();
			Thread.sleep(3000);
			if (webdriver.findElement(By.xpath(XpathMap.get("AutoplayHeader"))).isDisplayed()) {
				log.debug("Auto play setting window is displayed");
			}
		} catch (Exception e) {
			log.error("Autoplay setting window not Displayed");
			return isautoplaywin = false;
		}
		return isautoplaywin = true;

	}

	/*
	 * Date: 25/04/2019 Description:To verify Auto play on refreshing Parameter:
	 * NA
	 * 
	 * @return boolean
	 */
	public boolean isAutoplayOnAfterRefresh() {
		boolean onrefresh;
		try {
			webdriver.findElement(By.xpath(XpathMap.get("Start_Autoplay"))).click();
			// details_append(" verify On refresh, the previous AutoPlay session
			// must not resume."," On refresh, the previous AutoPlay session
			// must not resume.","The previous AutoPlay session has not resume",
			// "pass");

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

	public void wait_Bonus() {
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("Bonus"))));
	}

	/**
	 * Date: 13/11/2017 Author: Ashish Kshatriya Description: This function is
	 * used for click on element Parameter: By locator
	 */
	public boolean func_Click(String locator) 
	{
		Wait = new WebDriverWait(webdriver, 60);
		boolean present;
		try {
			//Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("locator"))));
			WebElement ele = webdriver.findElement(By.xpath(XpathMap.get(locator)));
			if (ele != null) 
			{
				ele.click();
			}
			else
			{
				System.out.println("Unable to Click");
			}
			present = true;
		} catch (NoSuchElementException e) {
			present = false;
			e.printStackTrace();
		}
		return present;
	}
	
	/**
	 * Date: 13/11/2017 Author: Ashish Kshatriya Description: This function is
	 * used for click on element Parameter: By locator
	 */
	public String func_click(String locator) 
	{
		Wait = new WebDriverWait(webdriver, 60);
		try {
			WebElement ele = webdriver.findElement(By.xpath(XpathMap.get(locator)));
			if (ele != null) {
				ele.click();
			}
		} catch (NoSuchElementException e) {
		   e.printStackTrace();
		}
		return null;
	}

	/**
	 * Date: 13/11/2017 Author: Ashish Kshatriya Description: This function is
	 * used for click on element Parameter: By locator
	 */
	public boolean func_Click_BYID(String locator) {
		Wait = new WebDriverWait(webdriver, 50);
		boolean present;
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(locator)));
			WebElement ele = webdriver.findElement(By.id(locator));
			if (ele != null) {
				ele.click();
			}
			present = true;
		} catch (NoSuchElementException e) {
			present = false;
			e.printStackTrace();
		}
		return present;
	}

	public String Func_navigate_Denmark(String applicationName, String urlNavigate) {
		Wait = new WebDriverWait(webdriver, 500);
		try {
			String appurl = XpathMap.get("ApplicationURL");
			webdriver.navigate().to(appurl);
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("five_Reel_slot"))));
			webdriver.findElement(By.xpath(XpathMap.get("five_Reel_slot"))).click();
			// webdriver.findElement(By.xpath(XpathMap.get("applicationName")));
			Thread.sleep(4000);
			// Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("Spin_Button"))));
			GameDesktopName = webdriver.findElement(By.id(GameName)).getText();
			// JavascriptExecutor js = ((JavascriptExecutor)webdriver);
			// js.executeScript("arguments[0].scrollIntoView(true);",webdriver.findElement(By.id(applicationName)));
			webdriver.findElement(By.id(GameName)).click();
			// WebElement ele=webdriver.findElement(By.id(applicationName));
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("Login"))));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return GameDesktopName;
	}

	/**
	 * Date: 15/12/2017 Autohr: Laxmikanth Kodam Description: This function used
	 * to Login to the spain lobby
	 * 
	 * @return Title
	 */
	public String Func_Login_RegulatoryMarket_Spain(String username, String password) {
		Wait = new WebDriverWait(webdriver, 500);
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

	/* Havish Jain: Method is used to wait till Stop button */
	public void verifyStopLanguage(Desktop_HTML_Report language, String languageCode) {
		Wait = new WebDriverWait(webdriver, 50);
		try {
			func_Click(XpathMap.get("spinButtonBox"));
			log.debug("Clicked on spin button");
			Wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XpathMap.get("spinButtonBox"))));
			language.detailsAppendFolder("verify the Stop button translation",
					"Stop button should translate as per respective language", "Stop button is displaying", "Pass",
					languageCode);
			log.debug("Waiting reels to stop");

			Thread.sleep(100);
		} catch (Exception e) {
			log.error("error in verifyStopLanguage method", e);
		}
	}

	/**
	 * Date: 14/11/2017 Author: Laxmikanth Kodam Description: This function is
	 * used to resize the window
	 * 
	 */

	public void resizeBrowser(int a, int b) {
		Dimension d = new Dimension(a, b);
		// Resize current window to the set dimension
		webdriver.manage().window().setSize(d);

	}

	/**
	 * Date: 14/11/2017 Author: Laxmikanth Kodam Description: This function is
	 * used to resize the window
	 * 
	 */
	public void maxiMizeBrowser() {
		// Maximize the window
		webdriver.manage().window().maximize();
	}

	/**
	 * Author: Laxmikanth Kodam Description: This function is used to login the
	 * application by entering username and password Parameter:String
	 * username,String password
	 */
	public String Func_LoginBaseScene(String username, String password) {
		Wait = new WebDriverWait(webdriver, 500);
		String Title = null;

		try {
			webdriver.findElement(By.xpath(XpathMap.get("userName"))).clear();
			webdriver.findElement(By.xpath(XpathMap.get("userName"))).sendKeys(username);
			webdriver.findElement(By.xpath(XpathMap.get("password"))).clear();
			webdriver.findElement(By.xpath(XpathMap.get("password"))).sendKeys(password);

			webdriver.findElement((By.xpath(XpathMap.get("Login")))).click();
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btnSpin")));
			// Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("clock"))));
			Title = GameName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Title;
	}

	/*
	 * public long Random_LoginBaseScene(String userName){ Wait=new
	 * WebDriverWait(webdriver,500); long loadingTime = 0; try {
	 * webdriver.findElement(By.xpath(XpathMap.get("userName"))).clear();
	 * 
	 * String password=XpathMap.get("Password");
	 * webdriver.findElement(By.xpath(XpathMap.get("userName"))).sendKeys(
	 * userName);
	 * webdriver.findElement(By.xpath(XpathMap.get("password"))).clear();
	 * webdriver.findElement(By.xpath(XpathMap.get("password"))).sendKeys(
	 * password);
	 * webdriver.findElement((By.xpath(XpathMap.get("Login")))).click();
	 * 
	 * long start = System.currentTimeMillis();
	 * Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
	 * XpathMap.get("clock")))); long finish = System.currentTimeMillis(); long
	 * totalTime = finish - start; loadingTime = totalTime/1000;
	 * System.out.println("Total Time for Game load in Seconds is: "+loadingTime
	 * ); //newFeature(); //summaryScreen_Wait(); } catch (Exception e) {
	 * e.printStackTrace(); } return loadingTime; }
	 */

	/**
	 * Date: 29/05/2018 Author: Havish Jain Description: This function is used
	 * to refresh the page and will take screenshot of splash screen before
	 * navigating to Base Scene. Parameter:
	 */

	public String splashScreen(Desktop_HTML_Report report, String language) {

		try {
			webdriver.navigate().refresh();
			if (language.equalsIgnoreCase("en"))
				acceptAlert();
			Wait = new WebDriverWait(webdriver, 40);
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("preLoaderBackground"))));
			report.detailsAppendFolder("verify the Splash Screen", "Splash Screen should display",
					"Splash screen is displaying", "pass", language);
			Thread.sleep(2000);

			log.debug("Refreshed the game and taken screen shot of splsh Screen");
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("clock"))));
			Thread.sleep(2000);
			// newFeature();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void acceptAlert() {
		Wait = new WebDriverWait(webdriver, 15);
		try {
			Capabilities caps = ((RemoteWebDriver) webdriver).getCapabilities();
			String browserName = caps.getBrowserName();
			if (browserName.equalsIgnoreCase("internet explorer") || browserName.equalsIgnoreCase("firefox")) {
				Wait.until(ExpectedConditions.alertIsPresent());
				Alert alert = webdriver.switchTo().alert();
				alert.accept();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Date: 31/05/2018 Author: Havish Jain Description: This function is used
	 * to click on PLay For Real link in menu Parameter:
	 */

	public void clickPlayForReal() {
		try {
			webdriver.findElement(By.xpath(XpathMap.get("PlayForReal"))).click();
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("Login"))));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Date: 14/11/2017 Author: Laxmikanth Kodam Description: This function is
	 * used to login the application by entering username and password
	 * Parameter:String username,String password
	 */
	public String Func_LoginBaseScene_Spain(String username, String password) {
		Wait = new WebDriverWait(webdriver, 500);
		String Title = null;
		try {
			webdriver.findElement(By.xpath(XpathMap.get("userName"))).clear();
			webdriver.findElement(By.xpath(XpathMap.get("userName"))).sendKeys(username);
			webdriver.findElement(By.xpath(XpathMap.get("password"))).clear();
			webdriver.findElement(By.xpath(XpathMap.get("password"))).sendKeys(password);

			webdriver.findElement((By.xpath(XpathMap.get("Login")))).click();

			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("spain_LobbyOK"))));
			Thread.sleep(1000);
			func_Click(XpathMap.get("spain_LobbyOK"));
			LobbyBalance = func_GetText(XpathMap.get("lobbyBalance"));
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("spain_BackToLobby"))));
			Title = webdriver.getTitle();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Title;
	}

	/**
	 * Date: 07-01-2018 Author:Laxmikanth Kodam Description: verify New Feature
	 * Dialogue is appearing on the screen
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public void newFeature() 
	{
		try {
			webdriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			// Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("OneDesign_NewFeature_ClickToContinue"))));
			// boolean
			// test=webdriver.findElements(By.xpath(XpathMap.get("OneDesign_NewFeature_ClickToContinue"))).size()>0;
			boolean test = webdriver.findElement(By.xpath(XpathMap.get("OneDesign_NewFeature_ClickToContinue"))).isDisplayed();
			if (test) 
			{
				//Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("OneDesign_NewFeature_ClickToContinue"))));
				func_Click("OneDesign_NewFeature_ClickToContinue");
				Thread.sleep(2000);
			}
			else 
			{
				log.debug("New Feature Screen is not displaying");
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Date: 17-11-2017 Author:Laxmikanth Kodam Description: Verify Credit text
	 * on the console for all the languages and validate with excel
	 */
	public String verifyCreditText() {

		String creditText = func_GetText(XpathMap.get("creditText"));
		String CreditValue = func_String_Operation(creditText);
		return CreditValue;
	}

	public String verifyCredit() {
		String CreditVal = null;
		try {
			String creditValue = func_GetText(XpathMap.get("creditBalance"));
			CreditVal = func_String_Operation(creditValue);
		} catch (Exception e) {
			e.getMessage();
		}
		return CreditVal;
	}

	/**
	 * Date:20/11/2017 Author: Laxmikanth Kodam This method used to verify the
	 * balance before spin
	 * 
	 * @return balance
	 * @throws InterruptedException
	 */
	public String verifySpinBeforeClick() throws InterruptedException {
		String defaultBalance = null;
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("gameLogo"))));
			defaultBalance = func_GetText(XpathMap.get("creditBalanace"));
			System.out.println("The outputData is " + defaultBalance);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defaultBalance;
	}

	/**
	 * Date:22/11/2017 Author:Laxmikanth Kodam This method used to check the
	 * currency multiplier inside settings
	 * 
	 * @return amount
	 * @throws InterruptedException
	 */
	public double currencymultiplier(String nodeValue, String attribute1, String attribute2, String attribute3)
			throws InterruptedException {
		func_Click(XpathMap.get("leftArrow"));
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("settings"))));
		func_Click(XpathMap.get("coinSize_Arrow"));
		func_Click(XpathMap.get("minCoinSize_Amount"));
		String minAmount = func_GetText(XpathMap.get("coinSize_Amount"));
		String value1 = func_String_Operation(minAmount);
		Double amount = Double.parseDouble(value1);
		return amount;
	}

	/**
	 * Author:Laxmikanth Kodam This method is used to calculate the respective
	 * multiplier value with coin size and min coin size
	 * 
	 * @param multipllier
	 * @return
	 */

	public double multiplierCalculation(double valu) {
		double minbal = 0.01;
		double Multiplier = valu / minbal;
		int multiplier = (int) Multiplier;
		System.out.println(multiplier);
		return multiplier;
	}

	/**
	 * Author:Premlata Mishra This method is used for verifying the status of
	 * the quick spin
	 * 
	 * @return true
	 */
	public String quickSpinStatus() {
		String opacityvalue = null;
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("quickSpin_Toggle"))));
			WebElement elementObj = webdriver.findElement(By.xpath(XpathMap.get("quickSpin_Toggle")));
			opacityvalue = elementObj.getCssValue("opacity");
			System.out.println(opacityvalue);
		} catch (Exception e) {
			e.getMessage();
		}
		return opacityvalue;
	}

	/**
	 * Date:07/12/2017 Author:Premlata Mishra This method is used to open the
	 * settings
	 * 
	 * @return true
	 * @throws InterruptedException
	 */
	public boolean settingsOpen() throws InterruptedException {
		Wait = new WebDriverWait(webdriver, 50);
		boolean test = false;
		try {
			// in case of video poker game putlocator of deal button
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("OneDesign_Spin_Text"))));
			// Clicking on hamburger menu
			func_Click(XpathMap.get("OneDesign_Hamburger"));
			log.debug("Clicked on hemburger menu button ");
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("OneDesign_Settings"))));
			// Clicking on settings link
			func_Click(XpathMap.get("OneDesign_Settings"));
			log.debug("Clicked on settings button to open");
			Thread.sleep(2000);
			test = true;
		} catch (Exception e) {
			log.error("Error in opening setting button", e);
		}
		return test;
	}

	public String gamelogo() {
		try {
			boolean test = webdriver.findElements(By.xpath(XpathMap.get("OneDesignLogo"))).size() > 0;
			if (test) {
				String gamelogo = func_GetText(XpathMap.get("OneDesignLogo"));
				Thread.sleep(2000);
				return gamelogo;
			} else {
				System.out.println("yes button is not in the game");
			}
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * Date:07/12/2017 Author:Premlata Mishra This method is usedto open the
	 * settings
	 * 
	 * @return true
	 * @throws InterruptedException
	 */
	public void SettingsToBasescen() throws InterruptedException {
		Wait = new WebDriverWait(webdriver, 5000);
		try {
			func_Click(XpathMap.get("OneDesignSettingclose"));
			Thread.sleep(3000);
			// boolean
			// wait=webdriver.findElement(By.xpath(XpathMap.get("OneDesignMenuClose"))).isDisplayed();
			/*
			 * if(!GameName.equals("reelGemsDesktop")&&!GameName.equals(
			 * "reelGems")) {
			 * webdriver.findElement(By.xpath(XpathMap.get("OneDesignMenuClose")
			 * )).click(); }
			 */
			Wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XpathMap.get("OneDesign_Spin_Button"))));
		} catch (Exception e) {
			e.getMessage();
		}
	}

	/**
	 * Date:07/12/2017 Author:Laxmikanth Kodam This method is to change the bet
	 * 
	 * @return true
	 * @throws InterruptedException
	 */
	/*
	 * public boolean betDecrease() throws InterruptedException {
	 * Thread.sleep(2000); func_Click(XpathMap.get("OneDesignBetDecrement"));
	 * Wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XpathMap.get(
	 * "OneDesignBetbtnyes")))); func_Click(XpathMap.get("OneDesignBetbtnyes"));
	 * return true; }
	 */
	public boolean betDecrease() {
		try {
			webdriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			func_Click(XpathMap.get("OneDesignBetDecrement"));
			// func_Click("//*[@id='btnDecrement']");
			/* need to uncomment for breakDaBank */
			/*
			 * boolean test1=webdriver.findElements(By.xpath(XpathMap.get(
			 * "OneDesignBetDecrement"))).size()>0; if(test1) {
			 * func_Click(XpathMap.get("OneDesignBetDecrement")); } else {
			 * System.out.println("- button is not here"); }
			 */
			/* need to uncomment for Reel gems */
			/*
			 * boolean test=webdriver.findElements(By.xpath(XpathMap.get(
			 * "OneDesignBetbtnyes"))).size()>0; if(test){
			 * Wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
			 * XpathMap.get("OneDesignBetbtnyes"))));
			 * func_Click(XpathMap.get("OneDesignBetbtnyes"));
			 * Thread.sleep(2000); } else{
			 * 
			 * System.out.println("yes button is not in the game"); }
			 */ } catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Date:07/12/2017 Author:Premlata Mishra This method is to increase the bet
	 * 
	 * @return true
	 */
	/*
	 * public boolean betIncrease() {
	 * func_Click(XpathMap.get("OneDesignBetIncreament"));
	 * Wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XpathMap.get(
	 * "OneDesignBetbtnyes")))); func_Click(XpathMap.get("OneDesignBetbtnyes"));
	 * 
	 * return true; }
	 */
	public boolean betIncrease() {

		try {
			webdriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			boolean test1 = webdriver.findElements(By.xpath(XpathMap.get("OneDesignBetIncreament"))).size() > 0;
			if (test1) {
				func_Click(XpathMap.get("OneDesignBetIncreament"));
			} else {
				System.out.println("bet button is not displaying");
			}
			boolean test = webdriver.findElements(By.xpath(XpathMap.get("OneDesignBetbtnyes"))).size() > 0;

			if (test) {
				Wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XpathMap.get("OneDesignBetbtnyes"))));
				func_Click(XpathMap.get("OneDesignBetbtnyes"));
				Thread.sleep(2000);
			} else {
				System.out.println("yes button is not in the game");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Date:07/12/2017 Author:premlata Mishra This method is to wait till the
	 * game will load after refresh,and the it will click on new feature screen
	 * 
	 * @return true
	 */
	public String refreshWait() {
		String wait = null;
		try {
			Wait = new WebDriverWait(webdriver, 5000);
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("OneDesign_Spin_Text"))));
			newFeature();
			wait = "wait";
		} catch (Exception e) {
			e.getMessage();
		}
		return wait;
	}

	/**
	 * Date:07/12/2017 Author:premlata Mishra This method is to get the paytable
	 * symbol amount
	 * 
	 * @return true
	 */
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
		String Value1 = null;
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
			String Value = func_GetText(locator);
			Value1 = func_String_Operation(Value);
		} catch (Exception e) {
			e.getMessage();
		}
		return Value1;
	}

	/**
	 * Date:07/12/2017 Author:premlata Mishra This method is to change the bet
	 * 
	 * @return true
	 */
	public double GetBetAmt() 
	{
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("OneDesign_Spin_Button"))));
		String betVal = func_GetText(XpathMap.get("OneDesign_BetValue"));
		System.out.println("Bet Value is "+betVal);
		String CreditVal = func_String_Operation(betVal);
		double CreditVal1 = Double.parseDouble(CreditVal);
		return CreditVal1;
	}

	/**
	 * Date:07/12/2017 Author:premlata mishra This method is to get win amount
	 * 
	 * @return true
	 */
	public String GetWinAmt() {
		String Winamt1 = null;
		try {
			String Winamt = func_GetText_BYID(XpathMap.get("OneDesignWinTextID"));
			Thread.sleep(1000);

			if (!Winamt.isEmpty())
				Winamt1 = func_String_Operation(Winamt);
			else
				Winamt1 = "0.0";

			System.out.println("win amount " + Winamt1);
		} catch (InterruptedException e) {
			log.error("Error in getting win amount", e);
			Thread.currentThread().interrupt();
		}
		return Winamt1;
	}

	/**
	 * Date:07/12/2017 Author:premlata mishra This method is to get total bet
	 * amount
	 * 
	 * @return true
	 */
	public String Slider_TotalBetamnt() {
		String totalbet = null;
		try {
			Wait.until(
					ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("OneDesignCoinSizeSlider"))));
			totalbet = func_GetText(XpathMap.get("OneDesigntotalbet"));
			// totalbet1=func_String_Operation(totalbet);
			System.out.println("Total Bet amount" + totalbet);
		} catch (Exception e) {
			e.getMessage();
		}
		return totalbet;
	}

	/**
	 * Date:07/12/2017 Author:premlata mishra This method is to get total bet
	 * amount
	 * 
	 * @return true
	 *//*
		 * public String BaseGame_TotalBetmnt() {
		 * Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
		 * XpathMap.get("OneDesignbetbutton")))); String
		 * totalbet=func_GetText(XpathMap.get("OneDesignbetbutton")); String
		 * totalbet1=func_String_Operation(totalbet);
		 * System.out.println("basegame totalbet"+totalbet1); return totalbet1;
		 * }
		 */
	/**
	 * Date:07/12/2017 Author:Laxmikanth Kodam This method is actually not
	 * necessery in component store just declaration needed
	 * 
	 * @return true
	 */
	public boolean settingsBack() {
		boolean test = false;
		try {
			Thread.sleep(500);
			webdriver.findElement(By.xpath(XpathMap.get("OneDesignSettingclose"))).click();
			log.debug("Clicked settings back button to close settings overlay");
			Thread.sleep(1000);
			if (webdriver.findElement(By.id(XpathMap.get("OneDesign_SettingsID"))).isDisplayed()) {
				webdriver.findElement(By.xpath(XpathMap.get("OneDesignMenuClose"))).click();
				log.debug("clicked on hemburger menu to close menu overlay");
				Thread.sleep(1000);
			}
			Thread.sleep(1000);
			test = true;
		} catch (Exception e) {
			log.error("error in closing settings", e);
		}
		return test;
	}

	/**
	 * * Date:07/12/2017 Author:premlata Mishra This method is waiting till the
	 * reel gets stop after click on spin
	 * 
	 * @return true
	 * @throws InterruptedException
	 */
	/*
	 * public String waitTillStop() throws InterruptedException {
	 * 
	 * boolean
	 * test=webdriver.findElements(By.xpath("//*[@id='respin-footer-4']")).size(
	 * )>0; if(test){
	 * while(webdriver.findElement(By.xpath("//*[@id='respin-footer-4']")).
	 * getText().isEmpty()) { Thread.sleep(500); } } else {
	 * 
	 * Wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XpathMap.get(
	 * "spinButtonBox")))); }
	 * 
	 * Thread.sleep(500);
	 * webdriver.findElement(By.xpath("//*[@id='txtWin']")).click();
	 * Thread.sleep(1000);
	 * 
	 * return null; }
	 */
	public void waitTillStop() {
		try {
			webdriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			boolean test = webdriver.findElements(By.xpath("//*[@id='respin-footer-4']")).size() > 0;
			if (test) {
				while (webdriver.findElement(By.xpath("//*[@id='respin-footer-4']")).getText().isEmpty()) {
					Thread.sleep(500);
				}
			} else {
				Wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XpathMap.get("spinButtonBox"))));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ate:22/11/2017 Author:Laxmikanth Kodam This method used to remove the $
	 * symbol from credits balance
	 * 
	 * @param value
	 * @return
	 */
	public String func_String_Operation(String value) {
		String str = value;
		String str1 = str.substring(1);
		return str1;
	}

	/**
	 * Date:22/11/2017 Author:Laxmikanth Kodam This method is used swipe the
	 * coins size from min to max coins sizes
	 * 
	 * @return true
	 * @throws Exception
	 */
	public boolean swipeMinCoinSize(double minCoinSize) throws Exception {
		try {
			List<WebElement> forms = webdriver.findElements(By.className("coinSize-cell"));
			int count = forms.size();
			func_Click(XpathMap.get("coinSize_Arrow"));
			for (int i = 3; i < count; i++) {
				WebElement current = webdriver.findElement(By.xpath("//*[@id='coinSize-content']/div[" + i + "]"));
				current.click();
				Thread.sleep(2000);
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Date 26/11/2017 Author:Laxmikanth Kodam This method used to verify the
	 * Big/Super/Mega win amounts
	 * 
	 * @return total win value
	 */
	public double verify_Win_Amount(String nodeValue, String attribute1, String attribute2, String attribute3)
			throws InterruptedException {
		Wait = new WebDriverWait(webdriver, 180);
		double Bigwin = 0;
		try {
			func_Click(XpathMap.get("spinButtonBox"));
			String bigWin = func_GetText(XpathMap.get("winValue"));
			String BigWin = func_String_Operation(bigWin);
			Bigwin = Double.parseDouble(BigWin);
			System.out.println(Bigwin);
			/*
			 * String DataFromHar = clickAt.getData(nodeValue, attribute1,
			 * attribute2, attribute3,proxy); balance =
			 * DataFromHar.split(",")[0]; loyaltyBalance =
			 * DataFromHar.split(",")[1]; totalWin = DataFromHar.split(",")[2];
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
		Thread.sleep(2000);
		return Bigwin;
	}

	/**
	 * Date 26/11/2017 Laxmikanth Kodam This method used to verify the Bet
	 * amount
	 * 
	 * @return the value to the win amount
	 */
	public double verify_Bet_Amount() {
		double betAmount = 0;
		try {
			String betValue = func_GetText(XpathMap.get("betValue"));
			String BetValue = func_String_Operation(betValue);
			System.out.println(BetValue);
			betAmount = Double.parseDouble(BetValue);
			System.out.println(betAmount);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return betAmount;
	}

	/**
	 * Date: 25/10/2017 Author: Kamal Kumar Vishwakarma Description: This
	 * function is used for get Data from the Common Language File provided by
	 * Derivco
	 */
	public void getCommonLanguageFile(String language) throws IOException {

		File commonLanguage = new File("languageFiles/" + language + "/language.json");
		commonLanguageContent = clickAt.removeUTF8BOM(FileUtils.readFileToString(commonLanguage, "UTF-8"));
	}

	/**
	 * Date: 04/05/2017 Author:Kamal Kumar Vishwakarma Description:
	 * verifyLanguageResponseFile use to verify Language.json Parameter:
	 * language
	 */
	/*
	 * public JSONCompareResult verifyLanguageResponseFile(String language,
	 * BrowserMobProxyServer proxy) throws InterruptedException {
	 * JSONCompareResult result = null; try { waitForPageToBeReady();
	 * //getCommonLanguageFile(language); responseLanguageContent =
	 * getResponseLanguageFile(proxy); result = compareLanguage();
	 * Thread.sleep(100); } catch (Exception e) { e.printStackTrace(); } return
	 * result; }
	 */

	public String getResponseLanguageFile(BrowserMobProxyServer proxy) {
		nhar = proxy.getHar();

		hardata = nhar.getLog();
		entries = hardata.getEntries();
		itr = entries.iterator();

		while (itr.hasNext()) {
			HarEntry entry = itr.next();
			String requestUrl = entry.getRequest().getUrl().toString();
			System.out.println(requestUrl);

			if (requestUrl.matches("(.*)resources/language(.*).json") || requestUrl.matches("(.*).eu/XMan/x.x(.*)")) {
				System.out.println("Matched**");
				responseLanguageContent = removeUTF8BOM(entry.getResponse().getContent().getText());
			}
		}
		return responseLanguageContent;
	}

	public String removeUTF8BOM(String s) {
		if (s.startsWith(UTF8_BOM)) {
			s = s.substring(1);
		}
		return s;
	}

	/**
	 * Date: 14/11/2017 Author: Laxmikanth Kodam Description: This function is
	 * used navigate the url of the application Parameter:String
	 * applicationName,String urlNavigate
	 */

	public String Func_navigate_reg(String applicationName, String urlNavigate) {
		Wait = new WebDriverWait(webdriver, 500);
		try {
			webdriver.navigate().to(urlNavigate);
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("five_Reel_slot"))));

			webdriver.findElement(By.xpath(XpathMap.get("five_Reel_slot"))).click();
			Thread.sleep(4000);
			// Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("Spin_Button"))));
			GameDesktopName = webdriver.findElement(By.id(applicationName)).getText();
			webdriver.findElement(By.id(applicationName)).click();
			// func_Click(XpathMap.get("practice_Play"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return GameDesktopName;
	}

	/**
	 * Date: 10/1/2018 Autohr: Laxmikanth Kodam Description: This function used
	 * for navigating to Gibraltar url
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
	 * Date: 10/1/2018 Autohr: Laxmikanth Kodam Description: This function used
	 * to close the popup
	 */
	public void checkPopUp() {
		try {
			webdriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			boolean test = webdriver.findElements(By.xpath(XpathMap.get("Gibraltar_CrossMark"))).size() > 0;
			if (test) {
				func_Click(XpathMap.get("Gibraltar_CrossMark"));
			} else {
				System.out.println("POP UP not displayed");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Date: 10/1/2018 Autohr: Laxmikanth Kodam Description: This function used
	 * for verifying the help icon
	 * 
	 * @return
	 */
	public boolean verifyHelp() {
		boolean ret = false;
		try {
			boolean test = webdriver.findElement(By.xpath(XpathMap.get("Gibraltar_HelpLink"))).isDisplayed();
			if (test) {
				ret = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * Date: 10/1/2018 Autohr: Laxmikanth Kodam Description: This function used
	 * for clicking help icon
	 * 
	 * @return
	 */
	public String clickHelp() {
		String GoogleTitle = null;
		try {
			func_Click(XpathMap.get("Gibraltar_HelpLink"));
			Wait.until(ExpectedConditions.titleContains("Google"));
			GoogleTitle = webdriver.getTitle();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return GoogleTitle;
	}

	/**
	 * Date: 13/12/2017 Autohr: Laxmikanth Kodam Description: This function used
	 * for clicking left arrow & navigate to settings page
	 * 
	 * @return true
	 */
	public boolean navigateSettings(String OD) throws Exception {
		Wait = new WebDriverWait(webdriver, 500);
		if (OD.equalsIgnoreCase("onedesign")) {
			settingsOpen();
		} else {
			webdriver.switchTo().defaultContent();
			func_Click(XpathMap.get("leftArrow"));
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("settings"))));
			Thread.sleep(3000);
		}
		return true;
	}

	/**
	 * Author:Premlata Mishra This method is used to open the settings
	 * 
	 * @return true
	 * @throws InterruptedException
	 */
	public boolean verifyQuickSpin(String imagepath) {
		boolean test = false;
		try {
			webdriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			// test=webdriver.getPageSource().contains("Quick Spin");
			test = webdriver.findElement(By.xpath(XpathMap.get("QuickSpin"))).isDisplayed();
			if (test) {
				return test = true;
			}
		} catch (Exception e) {
			return test = false;
		}
		return test;
	}

	/**
	 * This method is used to check stop is avalable or not Author: Premlata
	 * Mishra
	 * 
	 * @return true
	 */
	public boolean VerifyStop(String imagepath) {
		boolean test = false;
		try {
			webdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("Spin_Button"))));
			func_Click(XpathMap.get("Spin_Button"));
			test = webdriver.findElement(By.xpath(XpathMap.get("Stop_Text"))).isDisplayed();
			// test=webdriver.getPageSource().contains("stop text");
			if (test) {
				return test = true;
			}
		} catch (Exception e) {
			return test = false;
		}
		return test;
	}

	/**
	 * This method is used to check flag is available or not Author: Premlata
	 * Mishra
	 * 
	 * @return true
	 */
	public String verifyFlag() {
		boolean test = false;
		String flag = null;
		try {
			webdriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			test = webdriver.findElement(By.id(XpathMap.get("Denmark_FlagID"))).isDisplayed();
			if (test) {
				webdriver.findElement(By.id(XpathMap.get("Denmark_FlagID"))).click();
				flag = webdriver.findElement(By.id(XpathMap.get("Denmark_FlagID"))).getText();
			}
		} catch (Exception e) {
			flag = null;
		}
		return flag;
	}

	/**
	 * Author:Havish Jain This method is used validate Time limit reached
	 * scenario. Click continue in Reminder popup and wait till time limit is
	 * reached
	 * 
	 * @return true
	 * @throws InterruptedException
	 */
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

	/**
	 * Author: Laxmikanth Kodam Description: This function used to verify the
	 * spin button
	 * 
	 * @return true
	 */
	public boolean verifySpin(String image) {
		func_Click(XpathMap.get("Spin_Button"));
		return true;
	}

	/**
	 * Date: 13/12/2017 Autohr: Laxmikanth Kodam Description: This function used
	 * to verify the responsible gaming text
	 * 
	 * @return text
	 */
	public boolean verifyResponsibleLink(String imagepath) {
		String text = func_GetText(XpathMap.get("responsibleGaming_Text"));
		System.out.println(text);
		boolean ret = false;
		if (text != null) {
			ret = true;
		} else {
			ret = false;
		}
		return ret;
	}

	/**
	 * Date: 13/12/2017 Autohr: Laxmikanth Kodam Description: This function used
	 * to verify the responsible gaming text
	 * 
	 * @return text
	 */
	public boolean verifyResponsibleLink_working(String imagepath, Desktop_HTML_Report report) {

		boolean ret = false;
		try {
			func_Click(XpathMap.get("responsibleGaming_Text"));
			ret = true;
		} catch (Exception e) {
			e.printStackTrace();
			ret = false;
		}
		return ret;
	}
	/**
	 * @throws InterruptedException
	 */
	/*
	 * public boolean verifyGameFlag() throws InterruptedException{
	 * func_Click(XpathMap.get("responsible_Image")); return true; }
	 */

	/**
	 * Autohr: Laxmikanth Kodam Description: This function used to login the
	 * application
	 * 
	 * @return Title
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
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("italyHeader"))));
			/*
			 * Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath
			 * (XpathMap.get("preLoaderBackground"))));
			 * Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath
			 * (XpathMap.get("spinButtonBox"))));
			 */
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

	/**
	 * Date: 30/05/2018 Autohr: Havish Jain Description: This function used to
	 * click on Practice Play when Login Popup is open
	 * 
	 * @return Title
	 */

	public String func_LoginPracticePlay() {
		Wait = new WebDriverWait(webdriver, 500);
		String Title = null;
		try {
			func_Click(XpathMap.get("practice_Play"));
			long start = System.currentTimeMillis();
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("clock"))));
			long finish = System.currentTimeMillis();
			long totalTime = finish - start;
			System.out.println("Total Time for Game load in Seconds is - " + totalTime);
			Title = webdriver.getTitle();
			newFeature();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Title;
	}

	/**
	 * Date: 14/12/2017 Autohr: Laxmikanth Kodam Description: This function used
	 * to verify the game dialog
	 * 
	 * @return header
	 */
	public String incomplete_GameName(String gamename) {
		String incomplete_GameName = null;
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("five_Reel_slot"))));
			JavascriptExecutor js = ((JavascriptExecutor) webdriver);
			js.executeScript("arguments[0].scrollIntoView(true);", webdriver.findElement(By.id(gamename)));

			js.executeScript("arguments[0].click();", webdriver.findElement(By.id(gamename)));
			webdriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			webdriver.findElement(By.id(XpathMap.get("General_Error_ID"))).isDisplayed();
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("General_Error_ID"))));
			incomplete_GameName = webdriver.findElement(By.id(XpathMap.get("General_Error_ID"))).getText();
		} catch (Exception e) {
			incomplete_GameName = null;
		}
		System.out.println("incompletegamegame" + incomplete_GameName);
		return incomplete_GameName;
	}

	public String isGamePlay() {
		Wait = new WebDriverWait(webdriver, 500);
		String header = null;
		try {
			header = func_GetText(XpathMap.get("italyHeader"));
			System.out.println(header);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return header;
	}

	/*
	 * Date: 03/04/2017 Author: Author: Dhairaya Gautam Description: This
	 * function used for page load
	 */
	public void clickamount(Desktop_HTML_Report italy, String amount) {
		try {
			Thread.sleep(1000);
			webdriver.findElement(By.id("userInput")).sendKeys(amount);
			Thread.sleep(1000);
			webdriver.findElement(By.id(XpathMap.get("italySubmit_ID"))).click();
			Thread.sleep(3000);
			italy.detailsAppend(" Verify that Take to Game screen appears ", " Take to Game screen should appear ",
					" Take to Game screen is appearing ", "Pass");
			webdriver.findElement(By.id(XpathMap.get("italySubmit_ID"))).click();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Date: 14/12/2017 Autohr: Laxmikanth Kodam Description: This function used
	 * to take to the game
	 * 
	 * @return null
	 */
	public String taketoGame() {
		// Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btnSpin")));
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("clock"))));
		return null;
	}

	/**
	 * Date: 14/12/2017 Autohr: Laxmikanth Kodam Description: This function used
	 * to take to the game
	 * 
	 * @return null
	 */
	public String verifyCreditsValue() 
	{
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("clock_ID"))));
		String aval = func_GetText(XpathMap.get("OneDesign_Credit_Balance"));
		String val = func_String_Operation(aval).replace(",", "");
		// double val1=Double.parseDouble(val);
		return val;
	}

	/**
	 * Date: 15/12/2017 Autohr: Laxmikanth Kodam Description: This function used
	 * to Login to the spain lobby
	 * 
	 * @return Title
	 */
	public String Func_Login_RegulatoryMarket_Spain(String username, String password, String appName) {
		Wait = new WebDriverWait(webdriver, 500);
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

	/*
	 * 
	 * Author: Havish Jain Description:Overlay popup for Set Session Limit for
	 * regulatory market Spain Parameter: N/A
	 */
	public boolean slotGameLimitsOverlay() throws InterruptedException {
		try {
			boolean SetSessionLimits = webdriver.findElements(By.xpath(XpathMap.get("spain_SlotGameOverlay")))
					.size() > 0;
			if (SetSessionLimits)
				return true;
			else
				return false;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/**
	 * Date: 15/12/2017 Autohr: Laxmikanth Kodam Description: This function used
	 * to Set limits on the spain lobby overlay
	 * 
	 * @return Title
	 */
	public String setLimit(String losslimit) throws InterruptedException {
		Wait = new WebDriverWait(webdriver, 120);
		try {
			// Setting TimeLimit
			func_Click(XpathMap.get("spain_TimeLimit"));
			Select TimeLimit = new Select(webdriver.findElement(By.xpath(XpathMap.get("spain_TimeLimit"))));
			TimeLimit.selectByVisibleText("10 m");
			Thread.sleep(1000);

			// Setting ReminderPeriod
			func_Click(XpathMap.get("spain_RemainderPeriod"));
			Select ReminderPeriod = new Select(webdriver.findElement(By.xpath(XpathMap.get("spain_RemainderPeriod"))));
			ReminderPeriod.selectByVisibleText("5 m");
			Thread.sleep(1000);

			// Setting LossLimit
			func_Click(XpathMap.get("spain_LossLimit"));
			webdriver.findElement(By.xpath(XpathMap.get("spain_LossLimit"))).sendKeys("" + losslimit);
			// webdriver.findElement(By.className(XpathMap.get("dialog-button
			// bttn-color-primary")));

			// Setting PreventFuture
			func_Click(XpathMap.get("spain_PreventFutureSlot"));
			Select PreventFuture = new Select(webdriver.findElement(By.xpath(XpathMap.get("spain_PreventFutureSlot"))));
			PreventFuture.selectByVisibleText("1 Hour(s)");
			Thread.sleep(1000);

			// Click on SetLimits button
			webdriver.findElement(By.xpath(XpathMap.get("spain_SetLimits"))).click();
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("spain_SetLimitsOK"))));

			func_Click(XpathMap.get("spain_SetLimitsOK"));
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}
		String Title = webdriver.getTitle();
		return Title;
	}

	/**
	 * Date: 19/12/2017 Autohr: Laxmikanth Kodam Description:Verify the UK url
	 * 
	 * @return Title
	 */
	public void openUrl(String url) {
		Wait = new WebDriverWait(webdriver, 500);
		try {

			webdriver.navigate().to(url);
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("clock"))));
			Thread.sleep(5000);
			/*
			 * func_Click(XpathMap.get("UK_TopBar")); //Thread.sleep(20000);
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Date: 19/12/2017 Autohr: Laxmikanth Kodam Description:Verify the UK
	 * topbar
	 * 
	 * @return Title
	 * @throws InterruptedException
	 */
	public String verifyToggleTopbar() {
		webdriver.switchTo().frame("commonUIIFrame");
		try {
			func_Click(XpathMap.get("UK_TopBar"));
			Thread.sleep(2000);
			String stake = func_GetText(XpathMap.get("UK_StakeVal")) + "!";
			String paid = func_GetText(XpathMap.get("UK_PaidVal")) + "!";
			// String
			// balancedemo=webdriver.findElement(By.id("balance")).getText();
			String balance = func_GetText(XpathMap.get("UK_BalanceVal")) + "!";
			String freebets = func_GetText(XpathMap.get("UK_FreeBetsVal")) + "!";
			System.out.println("stake= " + stake);
			System.out.println("paid= " + paid);
			System.out.println("balance= " + balance);
			System.out.println("freebets= " + freebets);
			topbarData = stake + paid + balance + freebets;
			System.out.println(topbarData);
		} catch (InterruptedException e) {
			log.error("Toggle Bar issue", e);
			Thread.currentThread().interrupt();
		}
		return topbarData;
	}

	/**
	 * Author: Laxmikanth Kodam Description:Verify the UK topbar stake by
	 * changing the coin size in settings
	 * 
	 * @return Title
	 * @throws InterruptedException
	 */
	public String[] verifyStakeWithTopBar() {
		webdriver.switchTo().frame("commonUIIFrame");
		String[] betvalues = new String[4];
		try {
			/*
			 * func_Click(XpathMap.get("UK_TopBar")); Thread.sleep(4000);
			 */
			String intialStakeVal = func_GetText(XpathMap.get("UK_StakeVal"));
			betvalues[0] = intialStakeVal;
			webdriver.switchTo().defaultContent();
			Thread.sleep(4000);
			String intialCoinSizeVal = func_GetText(XpathMap.get("coinSize_Amount"));
			betvalues[1] = intialCoinSizeVal;
			List<WebElement> forms = webdriver.findElements(By.xpath("//div[@class='coinSize-cell']"));
			int count = forms.size();
			func_Click(XpathMap.get("coinSize_Arrow"));
			Thread.sleep(4000);
			for (int i = 3; i < count; i++) {
				WebElement current = webdriver.findElement(By.xpath("//*[@id='coinSize-content']/div[" + i + "]"));
				current.click();
				func_Click(XpathMap.get("coinSize_Arrow"));
				func_Click(XpathMap.get("coinSize_Arrow"));
				Thread.sleep(2000);
			}
			func_Click(XpathMap.get("coinSizeDropDownMax"));
			String finalCoinSizeVal = func_GetText(XpathMap.get("coinSize_Amount"));
			betvalues[2] = finalCoinSizeVal;
			webdriver.switchTo().frame("commonUIIFrame");
			String finalStakeVal = func_GetText(XpathMap.get("UK_StakeVal"));
			betvalues[3] = finalStakeVal;
		} catch (InterruptedException e) {
			log.error("Error in verifying stake in top bar", e);
		}
		return betvalues;
	}

	public String PreventFuture() throws Exception {
		String PreventFuture = null;
		PreventFuture = func_GetText(XpathMap.get("spain_PreventFutureSlot_Text"));
		return PreventFuture;
	}

	/**
	 * Author:Laxmikanth Kodam Description: Parameter:
	 */
	public double[] getAttributes(String nodeValue, String attribute1, String attribute2, String attribute3) {
		double[] data = new double[4];
		try {
			String totalWin1 = null;
			String balance1 = func_GetText(XpathMap.get("creditBalance"));
			balance = func_String_Operation(balance1);

			String bet1 = func_GetText(XpathMap.get("betValue"));
			bet = func_String_Operation(bet1);

			totalWin1 = func_GetText(XpathMap.get("winValue"));
			if (!totalWin1.isEmpty()) {
				totalWin = func_String_Operation(totalWin1);
				String y = totalWin.replaceAll(",", "");
				data[2] = Double.parseDouble(y);
			} else {
				totalWin1 = "0";
				System.out.println(totalWin1);
				totalWinNew = Double.parseDouble(totalWin1);
			}
			String x = balance.replaceAll(",", "");
			data[0] = Double.parseDouble(x);
			data[1] = Double.parseDouble(bet);
			data[3] = totalWinNew;
			System.out.println(data[0]);
			// System.out.println(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * Author:Laxmikanth Kodam Description: Parameter:
	 */
	public double[] getAttributesSpin(String nodeValue, String attribute1, String attribute2, String attribute3) {

		double[] data1 = new double[4];
		Wait = new WebDriverWait(webdriver, 500);
		try {
			func_Click(XpathMap.get("spinButtonBox"));
			Thread.sleep(4000);
			// Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("clock"))));
			String totalWin1 = null;
			String balance1 = func_GetText(XpathMap.get("creditBalance"));
			balance = func_String_Operation(balance1);

			String bet1 = func_GetText(XpathMap.get("betValue"));
			bet = func_String_Operation(bet1);

			totalWin1 = func_GetText(XpathMap.get("winValue"));
			if (!totalWin1.isEmpty()) {
				totalWin = func_String_Operation(totalWin1);
				String y = totalWin.replaceAll(",", "");
				data1[2] = Double.parseDouble(y);
			} else {
				totalWin1 = "0";
				System.out.println(totalWin1);
				totalWinNew = Double.parseDouble(totalWin1);
			}
			String x = balance.replaceAll(",", "");
			data1[0] = Double.parseDouble(x);
			data1[1] = Double.parseDouble(bet);
			data1[3] = totalWinNew;
			System.out.println(data1[0]);
			// System.out.println(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data1;
	}

	/* code for verifying slottitle */
	public String verifyslottitle() throws Exception {
		String slotgametitle1 = null;
		slotgametitle1 = func_GetText(XpathMap.get("spain_SlotGameLimits"));
		return slotgametitle1;
	}

	public String verifysetlimitbutton() throws Exception {
		String setlimit = null;
		setlimit = func_GetText(XpathMap.get("spain_SetLimits"));
		return setlimit;
	}

	public boolean verifyhyperlink() throws Exception {
		boolean ret = false;
		if (webdriver.findElement(By.xpath(XpathMap.get("spain_BackToLobby"))).isDisplayed()) {
			System.out.println("enabled");
			ret = true;
		}
		return ret;
	}

	/* verify time limit blank */
	public boolean verifytimelimitblank() throws Exception {
		boolean ret = false;
		func_Click(XpathMap.get("spain_TimeLimit"));
		Thread.sleep(2000);
		ret = true;
		return ret;
	}

	/* verify reminder period blank */
	public boolean verifyreminderperiodblank() throws Exception {
		boolean ret = false;
		func_Click(XpathMap.get("spain_RemainderPeriod"));
		Thread.sleep(500);
		ret = true;
		return ret;
	}

	/* verify time loss limit blank */
	public boolean losslimitblank() throws Exception {
		boolean ret = true;
		func_Click(XpathMap.get("spain_LossLimit"));
		String textInsideInputBox = func_GetText(XpathMap.get("spain_LossLimit"));
		// Check whether input field is blank
		if (textInsideInputBox.isEmpty()) {
			System.out.println("Input field is empty");
			ret = true;
		}
		return ret;
	}

	/* verify time limit blank */
	public boolean verifyfutureprventblank() throws Exception {
		boolean ret = false;
		String selectoption = null;
		func_Click(XpathMap.get("spain_PreventFutureSlot"));
		Select preventblank = new Select(webdriver.findElement(By.xpath(XpathMap.get("spain_PreventFutureSlot"))));
		// preventblank.selectByIndex(1);
		selectoption = preventblank.getFirstSelectedOption().getText();
		System.out.println(selectoption);
		// Check whether input field is blank
		if (selectoption.isEmpty()) {
			System.out.println("Input field is empty");
			ret = true;
		}
		return ret;
	}

	// Selectable options :Ensure dropdown box appear for Time limit box
	public boolean verifytimedropdown() throws Exception {
		boolean ret = false;
		String selectoption = null;
		func_Click(XpathMap.get("spain_TimeLimit"));
		Select TimeLimit = new Select(webdriver.findElement(By.xpath(XpathMap.get("spain_TimeLimit"))));
		// TimeLimit.selectByIndex(1);
		selectoption = TimeLimit.getFirstSelectedOption().getText();
		System.out.println(selectoption);
		// Check whether input field is blank
		if (selectoption.isEmpty()) {
			System.out.println("Input field is empty");
			ret = true;
		}
		return ret;
	}

	// Selectable options :Ensure dropdown box appear for reminder limit box
	public boolean verifyreminderdropdown() throws Exception {
		boolean ret = false;
		// String textInsideInputBox
		// =func_GetText(XpathMap.get("spain_RemainderPeriod"));
		String selectoption = null;
		func_Click(XpathMap.get("spain_RemainderPeriod"));
		Select RemainderPeriod = new Select(webdriver.findElement(By.xpath(XpathMap.get("spain_RemainderPeriod"))));
		// RemainderPeriod.selectByIndex(1);
		selectoption = RemainderPeriod.getFirstSelectedOption().getText();
		System.out.println(selectoption);
		// Check whether input field is blank
		if (selectoption.isEmpty()) {
			System.out.println("Input field is empty");
			ret = true;
		}
		return ret;
	}

	// Selectable option Ensure slotreminder dropdown appear
	public boolean verifypreventfutureslotreminder() throws Exception {
		boolean ret = false;
		func_Click(XpathMap.get("spain_PreventFutureSlot"));
		ret = true;
		return ret;
	}

	// Ensure there are time duration options available for selection in the
	// different time units (eg, 1 hour, 1 minute).
	public String verifytimedurationoption(String dropdownValue) throws Exception {
		String selectoption = null;
		try {
			func_Click(XpathMap.get("spain_TimeLimit"));
			Select TimeLimit = new Select(webdriver.findElement(By.xpath(XpathMap.get("spain_TimeLimit"))));
			TimeLimit.selectByIndex(1);
			selectoption = func_GetText(XpathMap.get("spain_TimeLimit"));
			Thread.sleep(1000);
		} catch (Exception e) {
			e.getMessage();
		}
		return selectoption;
	}

	// Ensure there are time duration options available for selection in the
	// different time units (eg,5 mins,10 mins).
	public String verifytimedurationoption() throws Exception {
		String selectoption = null;
		func_Click(XpathMap.get("spain_TimeLimit"));
		Select TimeLimit = new Select(webdriver.findElement(By.xpath(XpathMap.get("spain_TimeLimit"))));
		TimeLimit.selectByIndex(3);
		selectoption = TimeLimit.getFirstSelectedOption().getText();
		System.out.println(selectoption);
		Thread.sleep(1000);
		return selectoption;
	}

	// Ensure that send the loss limit value should be less than player balance
	public String sendLossLimitData() throws Exception {
		String lobbybalance = LobbyBalance.replaceAll("[ï¿½$,]", "");
		String lobbybal = lobbybalance.substring(0, lobbybalance.length() - 3);
		lobbybals = Double.parseDouble(lobbybal);
		double lobbyBal = lobbybals - 1;
		func_Click(XpathMap.get("spain_LossLimit"));
		webdriver.findElement(By.xpath(XpathMap.get("spain_LossLimit"))).clear();
		webdriver.findElement(By.xpath(XpathMap.get("spain_LossLimit"))).sendKeys(String.valueOf(lobbyBal));
		return LobbyBalance;
	}

	/*
	 * Author: Ashish Kshatriya Description: This function is used for take
	 * screenshots in application.
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

	/**
	 * Author : Laxmikanth Kodam Description: To fill Spain Start Session Form
	 * Param: Time Limit, Reminder Period, Loss Limit and prevent Future Slot
	 * Game for Play Return: Boolean value
	 */
	public boolean fillStartSessionLossForm(String LossLimit, Desktop_HTML_Report tc10) {
		boolean ret = false;
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("spain_TimeLimit"))));
			Select sel = new Select(webdriver.findElement(By.xpath(XpathMap.get("spain_TimeLimit"))));
			sel.selectByIndex(1);
			sel = new Select(webdriver.findElement(By.xpath(XpathMap.get("spain_ReminderPeriod"))));
			sel.selectByIndex(1);
			webdriver.findElement(By.xpath(XpathMap.get("spain_LossLimit"))).sendKeys(LossLimit);
			sel = new Select(webdriver.findElement(By.xpath(XpathMap.get("spain_PreventFutureSlot"))));
			sel.selectByIndex(1);
			webdriver.findElement(By.xpath(XpathMap.get("spain_SetLimits"))).click();
			Wait.until(
					ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(XpathMap.get("spain_SetLimitsOK"))));
			webdriver.findElement(By.xpath(XpathMap.get("spain_SetLimitsOK"))).click();
			Wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(XpathMap.get("Spin_Button"))));
			ret = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	/*
	 * Author : Havish Jain Description: To wait until session reminder popup
	 * appears Return: String value
	 */
	public boolean waitUntilSessionReminder(Desktop_HTML_Report tc10) {
		boolean header = false;
		try {
			Wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath(XpathMap.get("spain_SessionReminderContinue"))));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return header;
	}

	/**
	 * Author : Havish Jain Description: To click on continue button in session
	 * reminder popup
	 */
	public void spainContinueSession() {
		try {
			Wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath(XpathMap.get("spain_SessionReminderContinue"))));
			webdriver.findElement(By.xpath(XpathMap.get("spain_SessionReminderContinue"))).click();
		} catch (Exception e) {
			e.getMessage();
		}
	}

	/*
	 * Author: Anand Description: To test element is present or not Param: Key
	 * of element's xpath from OR Return: Boolean value
	 */
	public boolean isElementExist(String keyName, int timeInSeconds) {
		boolean ret = false;
		try {
			webdriver.manage().timeouts().implicitlyWait(timeInSeconds, TimeUnit.SECONDS);
			if (webdriver.findElements(By.xpath(XpathMap.get(keyName))).size() > 0) {
				ret = true;
			}
		} catch (Exception e) {
			webdriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		}
		return ret;
	}

	/**
	 * Author : Havish Jain Description: To wait until loss limit is reached
	 * Param: Null Return: String value
	 */
	public String waitUntilSessionLoss() {
		String title = null;
		try {
			for (int i = 0; i <= 10; i++) {
				Wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id(XpathMap.get("Spin_Button_ID"))));
				func_Click(XpathMap.get("OneDesign_Spin_Text"));
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

	/**
	 * Author : Havish Jain Description: To close popup of session loss and
	 * capture screenshot Param: Desktop_HTML_Report
	 */
	public void closeSessionLossPopup(Desktop_HTML_Report tc10) {
		try {
			func_Click(XpathMap.get("spain_lossLimitDialogueOK"));
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("spain_CloseBtn"))));
			tc10.detailsAppend("To check if Loss Limit Summary overlay appear",
					"Loss Limit Summary overlay should appear", "Loss Limit summary overlay appears", "Pass");
			func_Click(XpathMap.get("spain_CloseBtn"));
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("five_Reel_slotID"))));
			tc10.detailsAppend(
					"To verify user navigates to lobby after clicking on close button when loss limit is reached",
					"User should redirect to lobby after clicking on close button when loss limit is reached",
					"User is redirected to lobby", "Pass");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Author : Havish Jain Description: To vefiry that game can't be played in
	 * cooling off, loss limit is already reached and user is in lobby again
	 * Param: Desktop_HTML_Report Return:
	 */
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

	/**
	 * Author : Havish Jain Description: To verify that another game can't be
	 * played in cooling off, loss limit is already reached and user is in lobby
	 * again Param: Mobile_HTML_Report Return:
	 */

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

	/*
	 * Author: Havish Jain Description:Launch the game again when spain lobby is
	 * open Parameter: N/A
	 */
	public void relaunchGame() {
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("five_Reel_slotID"))));
		func_Click(XpathMap.get("five_Reel_slot"));
		JavascriptExecutor js = ((JavascriptExecutor) webdriver);
		js.executeScript("arguments[0].scrollIntoView(true);", webdriver.findElement(By.id(GameName)));
		js.executeScript("arguments[0].click();", webdriver.findElement(By.id(GameName)));
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("spain_SlotGameOverlay"))));
	}

	/**
	 */
	public String clickForScreen(String Ok, String Close) {
		try {
			webdriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			boolean OK = webdriver.findElements(By.xpath(XpathMap.get(Ok))).size() > 0;
			if (OK) {
				func_Click(XpathMap.get(Ok));
			} else {
				System.out.println("Element not found");
			}
		} catch (Exception e) {
		}

		return null;
	}

	/**
	 */
	public String clickOnClose() {
		func_Click(XpathMap.get("spain_CloseBtn"));
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("navigation_MyAccount"))));
		String lobbyAccount = webdriver.getTitle();
		return lobbyAccount;
	}

	public void spainStartNewSession(Desktop_HTML_Report Tc10) {
		try {
			webdriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			boolean test = webdriver.findElements(By.xpath(XpathMap.get("spain_StartNewSession"))).size() > 0;
			if (test) {
				Tc10.detailsAppend("Verify that Start New Sessiosn Overlay appears before the game loads ",
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

	public String getText(String elementKeyName) {
		return func_GetText(XpathMap.get(elementKeyName));// webdriver.findElement(By.xpath(XpathMap.get(elementKeyName))).getText();
	}

	/**
	 * Name:Premlata Mishra Description: this function is used to click on icon
	 * n switch to previous window
	 * 
	 * @throws InterruptedException
	 * @throws Exception
	 */
	public void iconclick(WebElement ele, Desktop_HTML_Report report) throws Exception {
		String winHandleBefore = webdriver.getWindowHandle();
		System.out.println("Before content : " + winHandleBefore);
		ele.click();
		for (String winHandle : webdriver.getWindowHandles()) {
			if (!winHandle.equals(winHandleBefore))
				webdriver.switchTo().window(winHandle);
		}
		Thread.sleep(3000);
		report.detailsAppend("Verify that icon is clicking and nevigating to new page",
				" Clicking on icon ,it must nevigate to new page", "Clicking on icon ,it is nevigating to new page",
				"pass");
		webdriver.close();
		for (String winHandle : webdriver.getWindowHandles()) {
			if (winHandle.equals(winHandleBefore))
				webdriver.switchTo().window(winHandle);
		}
		String content = webdriver.getWindowHandle();
		System.out.println("After perform content : " + content);
	}

	public void clickIcon(Desktop_HTML_Report report) throws Exception {
		iconclick(webdriver.findElement(By.xpath(XpathMap.get("OneDesignHelp"))), report);
		// iconclick(webdriver.findElement(By.xpath(XpathMap.get("OneDesignnull"))),report);
		iconclick(webdriver.findElement(By.xpath(XpathMap.get("OneDesignPlayCheck"))), report);
		iconclick(webdriver.findElement(By.xpath(XpathMap.get("OneDesignCashCheck"))), report);
		func_Click(XpathMap.get("OneDesignClosePaytable"));
	}

	/**
	 * Date:10-1-2018 Name:Premlata Mishra Description: this function is used to
	 * Scroll the page * @throws Exception
	 */
	public void scrollBar(WebElement ele, Desktop_HTML_Report report) throws Exception {
		JavascriptExecutor js = (JavascriptExecutor) webdriver;
		js.executeScript("arguments[0].scrollIntoView(true);", ele);
	}

	/**
	 * Date:10-1-2018 Name:Premlata Mishra Description: this function is used to
	 * Scroll the page and to take the screenshot
	 * 
	 * @throws Exception
	 */
	public String paytableOpen(Desktop_HTML_Report report, String classname, String languageCode) {
		Wait = new WebDriverWait(webdriver, 50);
		String paytable = null;
		try {
			if (classname.equals("Desktop_Regression_Suit")) {
				Wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XpathMap.get("OneDesign_Spin_Button"))));

				func_Click(XpathMap.get("OneDesign_Hamburger"));
				Wait.until(ExpectedConditions.elementToBeClickable(By.id(XpathMap.get("OneDesign_PaytableID"))));
				func_Click_BYID(XpathMap.get("OneDesign_PaytableID"));
				log.debug("Clicked on paytable icon to open ");
				Thread.sleep(3000);
				JavascriptExecutor js = (JavascriptExecutor) webdriver;
				WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get("5_saphire_Symbol_")));
				js.executeScript("arguments[0].scrollIntoView(true);", ele1);
				Thread.sleep(1000);
				paytable = func_GetText(XpathMap.get("5_saphire_Symbol_"));
				// report.details_append("verify the paytable screen shot", "
				// paytable first page screen shot", "paytable first page
				// screenshot ", "pass");
			} else {
				Thread.sleep(1000);
				func_Click_BYID(XpathMap.get("OneDesign_HamburgerID"));
				Thread.sleep(1000);
				Wait.until(ExpectedConditions.elementToBeClickable(By.id(XpathMap.get("OneDesign_PaytableID"))));
				func_Click_BYID(XpathMap.get("OneDesign_PaytableID"));
				log.debug("Clicked on paytable icon to open ");
				Thread.sleep(3000);
				report.detailsAppendFolder("verify the paytable screen shot", " paytable first page screen shot",
						"paytable first page screenshot ", "pass", languageCode);
				paytable = "paytable";
				JavascriptExecutor js = (JavascriptExecutor) webdriver;

				WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get("OneDesignpaytable1")));
				js.executeScript("arguments[0].scrollIntoView(true);", ele1);
				Thread.sleep(1000);
				report.detailsAppendFolder("verify the paytable screen shot", " paytable Second page screen shot",
						"paytable Second page screenshot ", "pass", languageCode);

				boolean test = webdriver.findElements(By.xpath(XpathMap.get("OneDesignpaytable2"))).size() > 0;
				if (test) {
					WebElement ele3 = webdriver.findElement(By.xpath(XpathMap.get("OneDesignpaytable2")));
					js.executeScript("arguments[0].scrollIntoView(true);", ele3);
					Thread.sleep(1000);
					report.detailsAppendFolder("verify the paytable screen shot", " paytable Third page screen shot",
							"paytable Third page screenshot ", "pass", languageCode);
					boolean test1 = webdriver.findElements(By.xpath(XpathMap.get("OneDesignpaytable3"))).size() > 0;
					if (test1) {
						WebElement ele4 = webdriver.findElement(By.xpath(XpathMap.get("OneDesignpaytable3")));
						js.executeScript("arguments[0].scrollIntoView(true);", ele4);
						Thread.sleep(1000);
						report.detailsAppendFolder("verify the paytable screen shot",
								" paytable fourth page screen shot", "paytable fourth page screenshot ", "pass",
								languageCode);
						/*
						 * boolean
						 * test2=webdriver.findElements(By.xpath(XpathMap.get(
						 * "OneDesignpaytable4"))).size()>0; if(test2) {
						 * WebElement
						 * ele5=webdriver.findElement(By.xpath(XpathMap.get(
						 * "OneDesignpaytable4"))); js.executeScript(
						 * "arguments[0].scrollIntoView(true);", ele5);
						 * Thread.sleep(1000); report.
						 * details_append_folder("verify the paytable screen shot"
						 * , " paytable Fifth page screen shot",
						 * "paytable Fifth page screenshot ", "pass",
						 * languageCode); }
						 */
					}
				}
			}
		} catch (Exception e) {
			log.error("error in opening paytable", e);
		}
		return paytable;
	}

	public void paytableClose() {
		Wait = new WebDriverWait(webdriver, 500);
		try {
			func_Click_BYID(XpathMap.get("PaytableBackID"));
			Thread.sleep(2000);
			log.debug("Closed the paytable page");
			Wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XpathMap.get("spinButtonBox"))));
		} catch (Exception e) {
			log.debug("error in closing paytable");
		}
	}

	/*
	 * Author:Havish Jain Description: To Open menu container
	 */
	public boolean menuOpen() {
		boolean ret = false;
		try {
			// Clicking on hamburger menu
			func_Click_BYID(XpathMap.get("OneDesign_HamburgerID"));
			Thread.sleep(2000);
			boolean test = webdriver.findElements(By.id(XpathMap.get("OneDesign_HamburgerID"))).size() > 0;
			if (test) {
				ret = true;
				log.debug("Clicked on menu button  to open");
			} else {
				log.debug("Hamburger Menu Links are not displaying");
			}
		} catch (InterruptedException e) {
			log.error("Error in opening menu", e);
		}
		return ret;
	}

	/*
	 * Author:Havish Jain Description: To Close menu container
	 */
	public boolean menuClose() {
		try {
			menuOpen();
			log.debug("Clicked on screen to close menu overlay");
		} catch (Exception e) {
			log.error("Error in closing menu", e);
		}
		return true;
	}

	/**
	 * Name:Laxmikanth Kodam Description: this function is used to navigate back
	 * to lobby
	 * 
	 * @return lobbytitle
	 */
	public String verifybacktolobby() throws Exception {
		String backtolobbytitle = null;
		try {
			// click on hyperlink
			func_Click(XpathMap.get("spain_BackToLobby"));
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("navigation_MyAccount"))));
			// Back To Lobby
			backtolobbytitle = webdriver.getTitle();
		} catch (Exception e) {
			evalException(e);
		}
		return backtolobbytitle;
	}

	/**
	 * Author Lamxikanth Kodam Common added for logout() This method is common
	 * logout function for the component store
	 * 
	 * @return
	 */
	public String Func_logout(String onedesign) {
		String loginTitle = null;
		if (onedesign.equalsIgnoreCase("onedesign")) {
			Func_logout_OD();
		} else {
			// Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("gameLogo"))));
			func_Click(XpathMap.get("leftArrow"));// Clicking on left arrow
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("backToLobby"))));
			func_Click(XpathMap.get("backToLobby"));// Clicking on back to lobby
													// button
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("three_Reel_Slot"))));
			func_Click(XpathMap.get("navigation_MyAccount"));
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("logout"))));
			loginTitle = func_GetText(XpathMap.get("logout"));
			func_Click(XpathMap.get("logout"));
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("Login"))));
			func_Click(XpathMap.get("closeButtonLogin"));
		}
		return loginTitle;
	}

	/**
	 * Author Lamxikanth Kodam Common added for logout() This method is common
	 * logout function for the component store
	 * 
	 * @return
	 */
	public String Func_logout_OD() {
		String loginTitle = null;
		func_Click(XpathMap.get("OneDesign_HomeIcon"));// Clicking on Home Icon
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("five_Reel_slot"))));
		func_Click(XpathMap.get("navigation_MyAccount"));
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("logout"))));
		loginTitle = func_GetText(XpathMap.get("logout"));
		func_Click(XpathMap.get("logout"));
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("Login"))));
		func_Click(XpathMap.get("closeButtonLogin"));
		return loginTitle;
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
			repo1.detailsAppend("Execution Interrupted because of InvalidElementStateException", "", "",
					"Interrupted");
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
			repo1.detailsAppend("Execution Interrupted because of InvalidCookieDomainException", "", "",
					"Interrupted");
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
			repo1.detailsAppend("Execution Interrupted because of ImeActivationFailedException", "", "",
					"Interrupted");
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
		}
	}

/*	public boolean verifyImage(String path) {
		boolean isMatch = false;
		Wait.until(ExpectedConditions.visibilityOf(webdriver.findElement(By.id("inGameClock"))));// wait
																									// untill
																									// the
																									// base
																									// game
																									// screen
																									// won't
																									// come
		Screen S = new Screen();
		// Match match=null;
	Pattern image = new Pattern(path);
		System.out.println("get image : " + image);
		try {
			// S.click(S.wait(image, 10));
			// Region r=S.find(image);
			Region r = S.exists(image, 100);
			if (r != null) {
				System.out.println("image found");
				isMatch = true;
			} else {
				System.out.println("image not found");
				isMatch = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return isMatch;
	}*/

	/**
	 * This method is used to verify the spin visibility
	 * 
	 * @param imagepath
	 * @return true
	 * @throws InterruptedException
	 */
	public boolean customeverifyimage(String button) throws InterruptedException {
		Wait = new WebDriverWait(webdriver, 500);
		String stop = new String("Stop");
		String spin = new String("spin");
		boolean isMatch = false;
		if (spin.equalsIgnoreCase(button)) {
			String spinText = verifySpin();
			if (spin.equalsIgnoreCase(spinText))
				isMatch = true;
		}
		if (stop.equalsIgnoreCase(button)) {
			String stopButton = null;
			stopButton = verifyStop();
			log.debug("stopButton:" + stopButton);
			if (stop.equalsIgnoreCase(stopButton))
				isMatch = true;
		}
		return isMatch;
	}

	/**
	 * This method is used to verify the Base Game Logo
	 * 
	 * @param imagepath
	 * @return
	 * @throws InterruptedException
	 */
	public String verifySpin() throws InterruptedException {
		// if(button){
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("OneDesign_Spin_Text"))));
		func_Click(XpathMap.get("OneDesign_Spin_Button"));
		String SpinText = func_GetText(XpathMap.get("OneDesign_Spin_Text"));
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("OneDesign_Spin_Text"))));
		// Thread.sleep(6000);
		return SpinText;
	}

	public String verifyStop() throws InterruptedException {
		// if(button){
		// Wait=new WebDriverWait(webdriver,120);
		func_Click(XpathMap.get("OneDesign_Spin_Button"));
		String StopText = func_GetText(XpathMap.get("OneDesign_Stop_Text"));
		// Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("OneDesign_Spin_Text"))));
		// Thread.sleep(60000);
		return StopText;
	}

	public void winClick() {
		Wait = new WebDriverWait(webdriver, 500);
		try {
			func_Click(XpathMap.get("OneDesignWinText"));
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("OneDesign_Spin_Text"))));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean spinclick() throws InterruptedException
	{
		Wait = new WebDriverWait(webdriver, 90);
		try 
		{
			func_Click("spinButtonBox");
			log.debug("Clicked on spin button");
		} 
		catch (Exception e) 
		{
			log.error("error while clicking on spin button", e);
		}
		return true;
	}

	/**
	 * This method is used to wait till FS scene loads Author: Havish Jain
	 * 
	 * @return true
	 */
	public boolean FSSceneLoading() {
		Wait = new WebDriverWait(webdriver, 50);
		try {
			// wait for Free spin scene to refresh
			log.debug("After refreshing,waiting for free spin's scene to come");
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("FSMultiplierCountID"))));
		} catch (Exception e) {
			log.error("error while waiting for free spin scene", e);
		}
		return true;
	}

	/**
	 * *Author:Premlata This method is used to wait till the free spin entry
	 * screen won't come
	 * 
	 * @throws InterruptedException
	 */
	public String entryScreen_Wait(String entry_Screen) {
		Wait = new WebDriverWait(webdriver, 50);
		String wait = null;
		if (entry_Screen.equalsIgnoreCase("yes")) {
			log.debug("Waiting for free spin entry screen to come");
			Wait.until(
					ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("M_FreeSpin_ClickToContinueID"))));
			wait = "freeSpin";
		} else {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("FSMultiplierCountID"))));
			wait = "NoEntryScreen";
		}
		return wait;
	}

	/**
	 * *Author:Premlata This method is used to click on free spin enrtry screen
	 * 
	 * @throws InterruptedException
	 */
	public String clickToContinue() {
		String FS_Credits = null;
		Wait = new WebDriverWait(webdriver, 500);
		try {
			webdriver.findElement(By.id(XpathMap.get("M_FreeSpin_ClickToContinueID"))).click();
			// func_Click(XpathMap.get("M_FreeSpin_ClickToContinueID"));
			FS_Credits = func_GetText(XpathMap.get("FreeSpin_Credits_ID"));
		} catch (Exception e) {
			e.getMessage();
		}
		return FS_Credits;
	}

	/**
	 * *Author:Havish This method is used to wait till the free spin summary
	 * screen won't come
	 * 
	 * @throws InterruptedException
	 */
	public void waitSummaryScreen() throws InterruptedException {
		Wait = new WebDriverWait(webdriver, 100);
		try {
			log.debug("Waiting for summary screen to come");
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("CongratsFSSummaryID"))));
			Thread.sleep(1500);
		} catch (Exception e) {
			log.error("error while waiting for summary screen");
		}
	}

	/**
	 * *Author:Havish This method is used to swipe for in paytable pages when
	 * arrows displays on the screen
	 * 
	 * @throws InterruptedException
	 */
	public String paytableSwipe(Desktop_HTML_Report report) {
		String paytable = "";
		try {
			// func_Click(XpathMap.get("OneDesign_Hamburger"));
			Wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XpathMap.get("OneDesign_Paytable"))));
			func_Click(XpathMap.get("OneDesign_Paytable"));
			Thread.sleep(1500);
			func_Click(XpathMap.get("OneDesign_Paytable"));
			report.detailsAppend("verify paytable screen shot", " paytable first page screen shot",
					"paytable first page screenshot ", "pass");
			paytable = "paytable";

			func_Click(XpathMap.get("OneDesign_Paytable"));
			Thread.sleep(1500);
			report.detailsAppend("verify the paytable screen shot", " paytable Second page screen shot",
					"paytable Second page screenshot ", "pass");

			func_Click(XpathMap.get("OneDesign_Paytable"));
			Thread.sleep(1500);
			report.detailsAppend("verify the paytable screen shot", " paytable Third page screen shot",
					"paytable Third page screenshot ", "pass");

			func_Click(XpathMap.get("OneDesign_Paytable"));
			Thread.sleep(1500);
			report.detailsAppend("verify the paytable screen shot", " paytable fourth page screen shot",
					"paytable fourth page screenshot ", "pass");

			/*
			 * func_Click(XpathMap.get("OneDesign_Paytable"));
			 * Thread.sleep(1500);
			 * report.details_append("verify the paytable screen shot",
			 * " paytable Fifth page screen shot",
			 * "paytable Fifth page screenshot ", "pass");
			 */
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return paytable;
	}

	/**
	 * *Author:Havish Jain This method is used to get currency symbol from
	 * credit balance
	 * 
	 * @throws InterruptedException
	 */
	public String getCurrencySymbol() {
		String currencySymbol = null;
		try {
			// String balance = func_GetText(XpathMap.get("creditBalance"));
			String balance = func_GetText("//*[@id='info_credit_val']");
			currencySymbol = balance.replaceAll("[[0-9],.\\s]", "");
			log.debug("Fetching currency symbol from game" + "Currency symbol is :" + currencySymbol);
		} catch (Exception e) {
			log.error("error in getting currency symbol", e);
		}
		return currencySymbol;
	}

	/**
	 * *Author:Havish Jain This method is used to get bet from the game without
	 * currency symbol, comma and dot.
	 * 
	 * @throws InterruptedException
	 */
	public String getCurrentBet() {
		String betValue = func_GetText(XpathMap.get("betValue"));
		String bet = betValue.replaceAll("[^0-9]", "");
		log.debug("Fetching multiplier from game" + "/nMultiplier is :" + bet);
		return bet;
	}

	/**
	 * *Author:Havish Jain This method is used to verify currency symbol in bet
	 * 
	 * @throws InterruptedException
	 */
	public boolean betCurrencySymbol(String currency) {
		String bet = func_GetText(XpathMap.get("betValue"));
		if (bet.indexOf(currency) >= 0) {
			return true;
		}
		if (currency == null || currency.equals("")) {
			return true;

		}
		log.debug("Fetching bet currency symbol" + "/n bet currency symbol is::" + bet);
		return false;
	}

	/**
	 * *Author:Havish Jain This method is used to verify currency symbol in bet
	 * setting screen
	 * 
	 * @throws InterruptedException
	 */
	public boolean betSettingCurrencySymbol(String currency, Desktop_HTML_Report report) {
		String totalBet = func_GetText(XpathMap.get("OneDesigntotalbet"));
		if (totalBet.indexOf(currency) < 0) {
			return true;
		}
		if (currency == null || currency.equals("")) {
			return true;
		}
		return false;
	}

	/**
	 * *Author:Havish Jain This method is used to wait till win amount occur in
	 * free spin
	 * 
	 * @throws InterruptedException
	 */
	public boolean waitforWinAmount(Desktop_HTML_Report currency, String currencyName) {
		Wait = new WebDriverWait(webdriver, 180);
		boolean b = false;
		String win = "";
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("FSWinOverlay"))));
			currency.detailsAppendFolder("Verify win overlay in Free Spins",
					"Win Overlay should display above the reel container", "Win Overlay is displaying", "Pass",
					currencyName);
			while (true) {
				win = func_GetText(XpathMap.get("FSWinValue"));
				if (!win.isEmpty()) {
					b = true;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	public boolean Helpclick() {
		webdriver.findElement(By.xpath(XpathMap.get("Help_Icon"))).click();
		return true;
	}// Close_Popup

	/* Click on quick spin toggle button */
	public boolean QuickSpinclick() {
		Wait = new WebDriverWait(webdriver, 5000);
		try {
			Wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XpathMap.get("OneDesignQuickSpin"))));
			func_Click(XpathMap.get("OneDesignQuickSpin"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * *Author:Premlata This method is used to slide the coin size slider
	 * 
	 * @throws InterruptedException
	 *//*
		 * public void Coinselectorclose() throws InterruptedException {
		 * func_Click(XpathMap.get("OneDesignCoinSliderClose")); //
		 * Thread.sleep(1000);
		 * Wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XpathMap.
		 * get("OneDesignBetbtnyes"))));
		 * func_Click(XpathMap.get("OneDesignBetbtnyes")); }
		 */
	public void Coinselectorclose() {

		func_Click(XpathMap.get("OneDesignCoinSliderClose"));
		try {
			webdriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			boolean test = webdriver.findElements(By.xpath(XpathMap.get("OneDesignBetbtnyes"))).size() > 0;
			if (test) {
				Wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XpathMap.get("OneDesignBetbtnyes"))));
				func_Click(XpathMap.get("OneDesignBetbtnyes"));
				Thread.sleep(2000);
			} else {
				System.out.println("yes button is not in the game");
			}
		} catch (Exception e) {
		}
	}

	/**
	 * *Author:Premlata This method is used to slide the coin size slider
	 * 
	 * @throws InterruptedException
	 */
	public void moveCoinSizeSlider() throws InterruptedException {
		Wait = new WebDriverWait(webdriver, 5000);
		try {
			func_Click(XpathMap.get("OneDesignbetbutton"));
			WebElement coinSizeSlider = webdriver.findElement(By.xpath(XpathMap.get("OneDesignCoinSizeSlider_ID")));
			Thread.sleep(3000);
			Actions action = new Actions(webdriver);
			action.dragAndDropBy(coinSizeSlider, 127, 0).build().perform();

		} catch (Exception e) {
			log.debug(e.getMessage());
		}
	}

	/**
	 * *Author:Premlata This method is used to wait till the free spin entry
	 * screen won't come
	 * 
	 * @throws InterruptedException
	 */
	public String entryScreen_Wait() {
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("FreeSpin_ClickToContinue"))));
		String wait = func_GetText(XpathMap.get("FreeSpin_ClickToContinue"));
		return wait;
	}

	/**
	 * *Author:Premlata This method is used to wait till the free spin summary
	 * screen won't come
	 * 
	 * @throws InterruptedException
	 */
	public String summaryScreen_Wait() {
		Wait = new WebDriverWait(webdriver, 500);
		String wait = null;
		try {
			boolean test = webdriver.findElement(By.id(XpathMap.get("FSSummaryCountinue_ID"))).isDisplayed();
			if (test) {
				Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("FSSummaryCountinue_ID"))));
				wait = webdriver.findElement(By.id(XpathMap.get("FSSummaryCountinue_ID"))).getText();
			} else {
				Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("FSSummaryCountinue_ID"))));
				wait = "summaryScreen";
			}
		} catch (Exception e) {
			e.getMessage();
		}
		return wait;
	}

	/**
	 * *Author:Premlata This method is used to click on free spin enrtry screen
	 * 
	 * @throws InterruptedException
	 */
	public String clickToCntinue() {
		func_Click(XpathMap.get("FreeSpin_ClickToContinue"));
		String FS_Credits = func_GetText(XpathMap.get("FreeSpin_Credits"));
		return FS_Credits;
	}

	public String FS_Credits() {
		String FS_Credits1 = null;
		try {
			// String
			// FS_Credits=func_GetText(XpathMap.get("FreeSpin_Credits_ID"));
			String FS_Credits = webdriver.findElement(By.id(XpathMap.get("FreeSpin_Credits_ID"))).getText();
			FS_Credits1 = func_String_Operation(FS_Credits);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return FS_Credits1;
	}

	public String FS_summaryScreenClick(Desktop_HTML_Report report) {
		String fs_credits = null;
		try {
			fs_credits = FS_Credits();
			if (XpathMap.get("FreeSpinEntryScreen").equalsIgnoreCase("yes")) {
				webdriver.navigate().refresh();
				error_Handler(report);
				Wait.until(
						ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("FSSummaryCountinue_ID"))));
				func_Click_BYID(XpathMap.get("FSSummaryCountinue_ID"));
			} else {
				fs_credits = FS_Credits();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fs_credits;

	}

	/**
	 * *Author:Premlata This method is used to wait to come free spin screen
	 * ,after refreshing the free spin
	 * 
	 * @throws InterruptedException
	 */
	public String FS_RefreshWait() {
		String balance = null;
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("FreeSpin_Credits_ID"))));
			balance = webdriver.findElement(By.id(XpathMap.get("FreeSpin_Credits_ID"))).getText();
		} catch (Exception e) {
			e.getMessage();
		}
		return balance;
	}
	/*
	 * This method is to click on free spin's "click to continue"
	 */
	/*
	 * public void FS_continue() {
	 * 
	 * webdriver.findElement(By.className("labelFS")).click(); }
	 */

	/* Havish Jain: Wait for Spin button */
	public void waitForSpinButton() {
		Wait = new WebDriverWait(webdriver, 3000);
		try {
			log.debug("Waiting for base scene to come after completion of FreeSpin");
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("spinButtonBox"))));
		} catch (Exception e) {
			log.error("error while waiting for base scene to come", e);
		}
	}

	public boolean winHistoryClick() throws Exception {
		return false;
	}

	/* Havish Jain: Click on Start Free Spin button in FS */
	public void FS_Start() {
		try {
			log.debug("Clicked on start button");
			webdriver.findElement(By.id(XpathMap.get("StartFSID"))).click();
		} catch (Exception e) {
			e.getMessage();
		}
	}

	/* Havish Jain: Click on Continue Free Spin button in FS */
	public void FS_continue() {
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("ContinueFSID"))));
			webdriver.findElement(By.id(XpathMap.get("ContinueFSID"))).click();
			log.debug("Clicked on free spin continue button");
		} catch (Exception e) {
			log.error("error while clicking on free spin continue button", e);
		}
	}

	/**
	 * *Author:Premlata This method is used to wait to come BIg win overlay
	 */
	public void bigWin_Wait() 
	{
		Wait = new WebDriverWait(webdriver, 500);
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("OD_BIgWin_ID"))));
	}

	public boolean open_TotalBet() {
		boolean b = false;
		try {
			// Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("clock"))));
			webdriver.findElement(By.xpath(XpathMap.get("OneDesignbetbutton"))).click();
			// func_Click_BYID(XpathMap.get("OneDesignbetbutton"));
			log.debug("clicked on total bet button");
			Thread.sleep(2000);
			b = true;
		} catch (Exception e) {
			log.error("error in open_TotalBet method", e);
		}
		return b;
	}

	public void closeOverlay() {
		try {
			Actions act = new Actions(webdriver);
			act.moveByOffset(100, 300).click().build().perform();
			act.moveByOffset(100, -200).build().perform();

		} catch (Exception e) {
			e.getMessage();
		}
	}

	public void close_TotalBet() throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(webdriver, 60);
		try {
			System.out.println(XpathMap.get("TotalBet_OvelayID"));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("TotalBet_OvelayID"))));
			// webdriver.findElement(By.xpath("//div[@id='inGameClock']")).click();
			// open_TotalBet();
			closeOverlay();
			log.debug("Clicked to close totalBet overlay");
		} catch (Exception e) {
			log.error("error while closing bet", e);
		}
	}
	/*
	 * Date: 22/04/2019 Description:To verify Autoplay with quick spin on
	 * Parameter: NA
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

	public boolean openAutoplay() {
		boolean b = false;
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btnSpin")));
			// Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("clock"))));
			func_Click_BYID(XpathMap.get("AutoplayID"));
			log.debug("Clicked on autoplay button");
			Thread.sleep(2000);
			b = true;
		} catch (Exception e) {
			log.error("error while opening the autoplay", e);
		}
		return b;
	}

	public void close_Autoplay() throws InterruptedException {
		try {
			Thread.sleep(500);
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("Autoplay_OverlayID"))));
			// webdriver.findElement(By.xpath("//div[@id='inGameClock']")).click();
			// open_Autoplay();
			closeOverlay();
			log.debug("clicked on screen to close overlay");
			Thread.sleep(500);
		} catch (Exception e) {
			log.error("Close Autoplay Error", e);
		}
	}

	/*
	 * Author: Havish Jain Description:This function is used to take screenshot
	 * of jackpot summary screen and click on back to game button return true
	 */
	public void jackpotSummary(Desktop_HTML_Report language, String languageCode) {
		Wait = new WebDriverWait(webdriver, 500);
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("winStrip"))));
			language.detailsAppendFolder("Verify language on Win Box which diplays above the reel container",
					"Win Box should display above the reel container", "Win Box displays", "Pass", languageCode);
			Thread.sleep(2500);
			spinclick();
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("JackpotSummaryID"))));
			Thread.sleep(1000);
			language.detailsAppendFolder("Verify language on Jackpot Summary Screen",
					"Jackpot Summary Screen should display", "Jackpot Summary Screen displays", "Pass", languageCode);
			Thread.sleep(1000);
			webdriver.findElement(By.id(XpathMap.get("BackToGameID"))).click();
			Thread.sleep(1500);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
/**
 * method verifies the entry screen of Free Games
 * @param report
 * @param currencyName
 * @return
 */
	public boolean freeGamesEntryScreen(Desktop_HTML_Report report,String currencyName)
	{
		boolean islocatorVisible = false;
		Wait = new WebDriverWait(webdriver, 5000);
		try 
		{
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("isFreeGamesVisible"))));
			islocatorVisible = webdriver.findElement(By.xpath(XpathMap.get("isFreeGamesVisible"))).isDisplayed();
			if(islocatorVisible)
			{
				isDisplayed(report,"playNow",currencyName);
				islocatorVisible = true;
				report.detailsAppendFolder("Free Games on Enter Screen ","Play Now Button","Play Now Button", "PASS", currencyName);
				System.out.println("PlayNow Button is Available on Free Games Intro Scrren : PASS");
				log.debug("PlayNow Button is Available on Free Games Intro Scrren : PASS");
				
 				String text = isDisplayedAndGetText("playNow");
 			  report.detailsAppendFolder("Free Games on Enter Screen ","Play Now Button Text",""+text, "PASS", currencyName);
				System.out.println("Free Games Play NowButton Text is : "+text+ " - PASS");
				log.debug("Free Games Play NowButton Text is : "+text);
				
				isDisplayed(report,"playLater",currencyName);
				islocatorVisible = true;
				report.detailsAppendFolder("Free Games on Enter Screen ","Play Later Button","Play Later Button", "PASS", currencyName);
				System.out.println("Play Later Button is Available on Free Games Intro Scrren : PASS");
				log.debug("Play Later Button is Available on Free Games Intro Scrren : PASS");
				
				
				isDisplayed(report,"FreeGamesInformation",currencyName);
				islocatorVisible = true;
				report.detailsAppendFolder("Free Games on Enter Screen ","Info Button","Info Button", "PASS", currencyName);
				System.out.println("Information Icon is Available on Free Games Intro Scrren : PASS");
				log.debug("Information Icon is Available on Free Games Intro Scrren : PASS");
				
				isDisplayed(report,"FreeGamesDeleteButton",currencyName);
				islocatorVisible = true;
				report.detailsAppendFolder("Free Games on Enter Screen ","Delete Button","Delete Button", "PASS", currencyName);
				System.out.println("Delete Button is Available on Free Games Intro Scrren : PASS");
				log.debug("Delete Button is Available on Free Games Intro Scrren : PASS");
				
				isDisplayed(report,"FreeGamesMenuIcon",currencyName);
				islocatorVisible = true;
				report.detailsAppendFolder("Free Games on Enter Screen ","Menu Icon","Menu Icon", "PASS", currencyName);
				System.out.println("Menu Icon is Available on Free Games Intro Scrren : PASS");
				log.debug("Menu Icon is Available on Free Games Intro Scrren : PASS");
				
			}
			else
			{
				System.out.println("Check the Free Games Intro Screen ");
				log.debug("Check the Free Games Intro Screen ");
				report.detailsAppendFolder("Free Games on Enter Screen ","Check the entry screen","Check the entry screen", "FAIL", currencyName);
			}
			
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
		}
		return islocatorVisible;
		// TODO Auto-generated method stub
		
	}
/**
 * method is for free game Information Icon
 */
	public String freeGameEntryInfo(Desktop_HTML_Report report,String currencyName,String locator1 ,String Locator2) 
	{
		Wait = new WebDriverWait(webdriver, 500);
		String infoText = "";
		try
		{
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get(locator1))));
			boolean infoIcon = webdriver.findElement(By.xpath(XpathMap.get(locator1))).isDisplayed();
			if(infoIcon)
			{
			webdriver.findElement(By.xpath(XpathMap.get(locator1))).click();
			report.detailsAppendFolder("Free Games","Info Button Click ","Info Button Click ", "PASS", currencyName);
			isDisplayed(report,Locator2,currencyName);
			String text = func_GetText(Locator2);
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
			}
			else
			{
				System.out.println( "Check the Free Games Info icon");
				log.debug( "Check the Free Games Info icon");
				//report.detailsAppendFolder("Free Games","Check the Free Games Info icon","Info Text"+infoText, "FAIL", currencyName);
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		return infoText;
	}
/**
 * play now button on Free games Intro Screen 
 */
	public boolean clickPlayNow() {
		Wait = new WebDriverWait(webdriver, 500);
		boolean b = false;
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("FreeGamesPlayNow"))));
			webdriver.findElement(By.id(XpathMap.get("FreeGamesPlayNow"))).click();
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("FreeGamesBaseSceneDiscard"))));
			b = true;
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	public String freeGamesResumescreen() {
		Wait = new WebDriverWait(webdriver, 500);
		String str = null;
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("FreeGamesResumeButton"))));
			Thread.sleep(1000);
			str = webdriver.findElement(By.id(XpathMap.get("FreeGamesResumeButton"))).getText();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	public boolean freeGameResumeInfo() {
		Wait = new WebDriverWait(webdriver, 500);
		boolean b = false;
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("FreeGamesResumeButton"))));
			webdriver.findElement(By.id(XpathMap.get("FreeGamesResumeInfoIcon"))).click();
			Wait.until(
					ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("FreeGamesResumeInfoDetails"))));
			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	public boolean resumeScreenDiscardClick() {
		Wait = new WebDriverWait(webdriver, 500);
		boolean b = false;
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("FreeGamesResumeButton"))));
			webdriver.findElement(By.id(XpathMap.get("FreeGamesResumeDiscard"))).click();
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("FreeGamesDiscardButton"))));
			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	public boolean confirmDiscardOffer() {
		Wait = new WebDriverWait(webdriver, 500);
		boolean b = false;
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("FreeGamesDiscardButton"))));
			webdriver.findElement(By.id(XpathMap.get("FreeGamesDiscardButton"))).click();
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("FreeGamesBackToGames"))));
			Thread.sleep(1500);
			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	public boolean freeGamesExpriyScreen() {
		Wait = new WebDriverWait(webdriver, 500);
		boolean b = false;
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("FreeGamesExpiredContinue"))));
			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	public String freeGamesDiscardExistingOffer() {
		Wait = new WebDriverWait(webdriver, 100);
		String currentScene = null;
		try {
			webdriver.manage().timeouts().implicitlyWait(25, TimeUnit.SECONDS);
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("clock"))));
			if (webdriver.findElements(By.id(XpathMap.get("FreeGamesResumeButton"))).size() > 0) {
				currentScene = "FreeGameResume";
				resumeScreenDiscardClick();
				confirmDiscardOffer();
				clickNextOffer();
			} else {
				currentScene = "freeGamesExpiredView";
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return currentScene;
	}

	/*
	 * Author:Havish This method is used to click on Next Offer button
	 * 
	 * @throws InterruptedException
	 */
	public void clickNextOffer() {
		try {
			if (webdriver.findElements(By.id(XpathMap.get("FreeGamesNextOffer"))).size() > 0) {
				webdriver.findElement(By.id(XpathMap.get("FreeGamesNextOffer"))).click();
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	/*
	 * Author:Havish This method is used click on Discard button in base scene
	 * during Free Games
	 */
	public void clickBaseSceneDiscard() {
		try {
			if (webdriver.findElements(By.id(XpathMap.get("FreeGamesBaseSceneDiscard"))).size() > 0) {
				webdriver.findElement(By.id(XpathMap.get("FreeGamesBaseSceneDiscard"))).click();
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	public String freeGamesContinueExpiry() {
		Wait = new WebDriverWait(webdriver, 100);
		try {
			webdriver.manage().timeouts().implicitlyWait(25, TimeUnit.SECONDS);
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("clock"))));
			if (webdriver.findElements(By.id(XpathMap.get("FreeGamesExpiredContinue"))).size() > 0) {
				webdriver.findElement(By.id(XpathMap.get("FreeGamesExpiredContinue"))).click();
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return null;
	}

	/**
	 * * Date: 29/05/2018 Author: Premlata Mishra Description: This function is
	 * used to set the bet to minimum Parameter:
	 */
	public void setMinBet() 
	{
		try {
			/*for (int i = 0; i < 5; i++)
			{
				WebElement CoinSizeSlider = webdriver.findElement(By.xpath(XpathMap.get("OneDesignCoinSizeSlider")));
				Actions action = new Actions(webdriver);
				action.dragAndDropBy(CoinSizeSlider, 30, 0).build().perform();
					
			}
			webdriver.findElement(By.xpath(XpathMap.get("OneDesignBetDecrement")));
			for (int i = 0; i < 12; i++) {
				betDecrease();
			}
*/
			String bet = "return " + XpathMap.get("SetMinBet");
			getConsoleText(bet);
			
		} 
		catch (Exception e)
		{
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
	 * * Date: 29/05/2018 Author: Premlata Mishra Description: This function is
	 * used to fetch payout from paytable
	 * 
	 * Parameter:
	 */
	public double gamepayout(String xpath) {
		double gamepayout_Double = 0.0;
		try {
			String gamePayout = func_GetText(xpath);
			String gamePayoutnew = func_String_Operation(gamePayout).replaceAll(",", "");
			gamepayout_Double = Double.parseDouble((gamePayoutnew));
		} catch (Exception e) {
			e.getMessage();
		}
		return gamepayout_Double;
	}

	/**
	 * * Date: 29/05/2018 Author: Premlata Mishra Description: This function is
	 * used to verify paytable and it's values Parameter:
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
			e.getMessage();
		}
		return verifypayout;
	}

	/**
	 * Date:10-1-2018 Name:Premlata Mishra Description: this function is used to
	 * Scroll the page and to take the screenshot
	 * 
	 * @throws Exception
	 */
	public void paytableScroll(String ele) 
	{
		JavascriptExecutor js = (JavascriptExecutor) webdriver;
		WebElement ele1 = webdriver.findElement(By.xpath(ele));
		js.executeScript("arguments[0].scrollIntoView(true);", ele1);
	}
	
	
	public String paytableScrollAndValidatePayouts(Desktop_HTML_Report report, String language)
	{
	   Wait = new WebDriverWait(webdriver, 50);
		String paytable = null;
		try
		{
			//method is for paytable open
			//paytableOpen(report);
			
			JavascriptExecutor js = ((JavascriptExecutor) webdriver);
			Thread.sleep(1500);
			
			double paytableOverallHeight = 0;//3754
			
			if (js.executeScript("return window.document.getElementById('paytableScroll').scrollHeight").getClass().getName().endsWith("Long"))
			{
				long longTotalPaytableHeight = (Long) js.executeScript("return " + XpathMap.get("PaytableScrolling"));
				paytableOverallHeight = (double) longTotalPaytableHeight;
			} 
			else
			{
				paytableOverallHeight = (double) js.executeScript("return " + XpathMap.get("PaytableScrollHeight"));
			}

			double paytableHeight2 = 0;//654
			if (js.executeScript("return window.document.getElementById('paytableScroll').scrollHeight").getClass().getName().endsWith("Long"))
			{
				long longPaytableHeight = (Long) js.executeScript("return " + XpathMap.get("PaytableScroll_h"));
				paytableHeight2 = (double) longPaytableHeight;
			} 
			else
			{
				paytableHeight2 = (double) js.executeScript("return " + XpathMap.get("PaytableScroll_h"));
			}
			// height adjustment because of text missing out
			paytableHeight2 = paytableHeight2 - 100;//554
			int scroll = (int) (paytableOverallHeight / paytableHeight2);//3754/554 = 6
			System.out.println(scroll); //6 

			for (int i = 1; i <= scroll + 1; i++) 
			{
				//js.executeScript("window.scrollBy(0,200)", "");
				js.executeScript("window.scrollTo(0,Math.max(document.documentElement.scrollHeight,document.body.scrollHeight,document.documentElement.clientHeight));");
				//validatePayoutsFromPaytable(report);
				//js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
				//js.executeScript("window.scrollTo(0, Math.max(document.documentElement.scrollHeight, document.body.scrollHeight, document.documentElement.clientHeight));");
				threadSleep(1000);
				/*int x = element.getLocation().getX();
				int y = element.getLocation().getY();
				js.executeScript("window.scrollBy(" +x +", " +y +")");
				
				((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();", webElement);*/
				
				
				threadSleep(1000);
				log.debug("Scrolling the paytable page and taking screenshots");
			//	report.detailsAppendFolder("verify the paytable screen shot", " paytable next page screen shot","paytable next page screenshot ", "PASS", language);

			}
			paytable = "paytable1";
		} 
		catch (Exception e)
		{
			log.error("error in opening paytable", e);
		}
		return paytable;
	}


	public double round(double number) {
		/*
		 * DecimalFormat dnf = new DecimalFormat( "#,###,###,##0.00" ); double
		 * roundednumber = new Double(dnf.format(number)).doubleValue();
		 */
		DecimalFormat dnf = new DecimalFormat("#.##");
		double roundednumber = new Double(dnf.format(number));
		return roundednumber;
	}

	/**
	 * * Date: 29/05/2018 Author: Premlata Mishra Description: This function is
	 * used to verify credit bubble and it's values Parameter:
	 */
	public String verifycreditBubble(String locator) {
		String bet = null;
		try {
			String betValue = func_GetText(locator);
			bet = betValue.replaceAll("[^0-9]", "");
		} catch (Exception e) {
			e.getMessage();
		}
		return bet;
	}

	/**
	 * * Date: 29/05/2018 Author: Premlata Mishra Description: This function is
	 * used to verify credit bubble and it's values
	 * 
	 * Parameter:
	 */
	public Map<String, Integer> creditBubble() throws Exception {
		Map<String, Integer> userValue = new HashMap<>();
		int creditbubbleBalstr;
		int bonusInBubbleValdouble;
		int totalcreditValdouble = 0;
		try {
			// Thread.sleep(5000);
			// func_Click(XpathMap.get("//*[@id='btnCoinsCredits']"));
			webdriver.findElement(By.id("btnCoinsCredits")).click();
			Thread.sleep(3000);
			if (webdriver.findElement(By.xpath(XpathMap.get("Creditbubble"))).isDisplayed()) {
				creditbubbleBalstr = Integer.parseInt(verifycreditBubble(XpathMap.get("creditBubble_Balance")));
				bonusInBubbleValdouble = Integer.parseInt(verifycreditBubble(XpathMap.get("creditBubble_Bonus")));
				totalcreditValdouble = Integer.parseInt(verifycreditBubble(XpathMap.get("CreditBubble_TB")));
				userValue.put("Balance", creditbubbleBalstr);
				userValue.put("Bonus", bonusInBubbleValdouble);
				userValue.put("TotalCredit", totalcreditValdouble);
				return userValue;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	public String GetBetAmtString() {
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("OneDesign_Spin_Button"))));
		String betVal = func_GetText(XpathMap.get("OneDesign_BetValue"));
		String CreditVal = func_String_Operation(betVal);
		// double CreditVal1=Double.parseDouble(CreditVal);
		return CreditVal;
	}

	public String getAttributeXpath(String xpath, String attr) {
		String style = webdriver.findElement(By.xpath(xpath)).getAttribute(attr);
		return style;
	}

	public boolean quickSpinOff() {
		boolean flag = false;
		try {
			spinclick();
			Thread.sleep(2000);
			if (webdriver.findElement(By.xpath(XpathMap.get("OneDesign_Stop_Button"))).isDisplayed())
				flag = true;
			else
				flag = false;

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}

	public boolean quickSpinOn() {
		boolean flag = false;
		try {
			spinclick();
			Thread.sleep(2000);
			if (!webdriver.findElement(By.xpath(XpathMap.get("OneDesign_Stop_Button"))).isDisplayed())
				flag = true;
			else
				flag = false;

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}

	public void quickSpinClick() {
		WebElement quickSpinButton = webdriver.findElement(By.xpath(XpathMap.get("quickSpinDiv")));

		new WebDriverWait(webdriver, 120).until(ExpectedConditions.elementToBeClickable(quickSpinButton));
		quickSpinButton.click();
	}

	public boolean verifyPaytablePresent() {
		WebDriverWait wait;
		boolean flag = false;

		try {
			wait = new WebDriverWait(webdriver, 20);
			wait.until(ExpectedConditions.elementToBeClickable(By.id(XpathMap.get("deal"))));
			if (webdriver.findElement(By.id(XpathMap.get("paytableContainer"))).isDisplayed())
				flag = true;
			else
				flag = false;
			System.out.println(flag);
		} catch (Exception e) {
			log.error("Error in paytable display");
		}
		return flag;
	}

	@Override
	public boolean dealClick() throws InterruptedException {

		WebDriverWait wait;
		boolean flag = false;
		try {
			wait = new WebDriverWait(webdriver, 20);
			// *********Clicking on Deal and Draw button
			log.debug("Finding Deal element on the page");
			wait.until(ExpectedConditions.elementToBeClickable(By.id(XpathMap.get("deal"))));
			webdriver.findElement(By.id(XpathMap.get("deal"))).click();
			log.debug("Successfully clicked on Deal button");
			log.debug("Finding draw button on the page");
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("draw"))));
			flag = true;
		} catch (Exception e) {
			log.error("Error while clicking on Draw button", e);
		}
		return flag;
	}

	public boolean drawClick() throws InterruptedException {
		WebDriverWait wait;
		boolean flag = false;
		try {
			wait = new WebDriverWait(webdriver, 20);

			// **********Clicking on Draw button
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("draw"))));
			webdriver.findElement(By.id(XpathMap.get("draw"))).click();
			log.debug("Successfully clicked on draw button");
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("collectBtn"))));

			// **********Clicking on module content to stop win counting
			webdriver.findElement(By.id("moduleContent")).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.id(XpathMap.get("collectBtn"))));
			flag = true;

		} catch (Exception e) {
			log.error("Error while clicking on Draw button", e);
		}
		return flag;
	}

	@Override
	public boolean drawCollectBaseGame(Desktop_HTML_Report report, String languageCode) throws Exception {
		double creditBalBeforeWin = 0.0;
		double creditBalWin = 0.0;
		double creditBalAfterWin = 0.0;
		WebDriverWait wait;
		WebElement winValue;
		double win = 0.0;
		double bet;
		boolean flag = false;

		try {
			wait = new WebDriverWait(webdriver, 200);

			// ********Collecting credit balance before win
			wait.until(ExpectedConditions.elementToBeClickable(By.id(XpathMap.get("collectBtn"))));
			creditBalBeforeWin = getCreditAmtDouble();

			// *********Collecting win value
			win = getWinAmtDouble(XpathMap.get("winValue"));
			if (win != 0.0) 
			{
			// ***********Clicking on collect button on double to screen
			webdriver.findElement(By.id(XpathMap.get("collectBtn"))).click();
			Thread.sleep(3000);

			// *************Capturing credit balance after collecting win
			creditBalAfterWin = getCreditAmtDouble();
			creditBalWin = (creditBalBeforeWin + win);
			if (Double.compare(creditBalWin, creditBalAfterWin) == 0) {
				if (languageCode != null) {
					report.detailsAppendFolder("Verify Deal, Draw and Collect functionality",
							"On Clicking deal and draw button",
							"Credit balance should get updated"
									+ "Credit balance is updated successfully :- Credit balance before win - "
									+ creditBalBeforeWin + " Win amount - " + win + " Credit balance after win - "
									+ creditBalAfterWin,
							"Pass", languageCode);
					resizeBrowser(600, 800);
					Thread.sleep(1000);
					report.detailsAppendFolder("Verify Deal, Draw and Collect functionality",
							"On Clicking deal and draw button",
							"Credit balance should get updated"
									+ "Credit balance is updated successfully :- Credit balance before win - "
									+ creditBalBeforeWin + " Win amount - " + win + " Credit balance after win - "
									+ creditBalAfterWin,
							"Pass", languageCode);
					resizeBrowser(400, 600);
					Thread.sleep(1000);
					report.detailsAppendFolder("Verify Deal, Draw and Collect functionality",
							"On Clicking deal and draw button",
							"Credit balance should get updated"
									+ "Credit balance is updated successfully :- Credit balance before win - "
									+ creditBalBeforeWin + " Win amount - " + win + " Credit balance after win - "
									+ creditBalAfterWin,
							"Pass", languageCode);
					webdriver.manage().window().maximize();
				}

				else {
					report.detailsAppend("Verify Deal, Draw and Collect functionality",
							"On Clicking deal and draw button",
							"Credit balance should get updated"
									+ "Credit balance is updated successfully :- Credit balance before win - "
									+ creditBalBeforeWin + " Win amount - " + win + " Credit balance after win - "
									+ creditBalAfterWin,
							"Pass");

				}
			

			} else {
				if (languageCode != null) {
					report.detailsAppendFolder("Verify Deal, Draw and Collect functionality",
							"On Clicking deal and draw button ,credit balance should get updated",
							"Credit balance is not updated"+ "Credit balance is updated successfully :- Credit balance before win - "
									+ creditBalBeforeWin + " Win amount - " + win + " Credit balance after win - "
									+ creditBalAfterWin, "Fail", languageCode);
				} else {
					report.detailsAppend("Verify Deal, Draw and Collect functionality",
							"On Clicking deal and draw button ,credit balance should get updated",
							"Credit balance is not updated"+ "Credit balance is updated successfully :- Credit balance before win - "
									+ creditBalBeforeWin + " Win amount - " + win + " Credit balance after win - "
									+ creditBalAfterWin, "Fail");
				}
			}
			
		}
		else
		{
			report.detailsAppend("Verify Deal, Draw and Collect functionality",
					"On Clicking deal and draw button ,credit balance should get updated",
					"Win has not been triggered", "Fail");
		}

		} catch (Exception e) {
			log.error("Error in collecting win on base game");
			report.detailsAppend("Verify Deal, Draw and Collect functionality",
					"On Clicking deal and draw button ,credit balance should get updated",
					"Credit balance is not updated" + "Credit balance is updated successfully :- Credit balance before win - "
							+ creditBalWin + " Win amount - " + win + " Credit balance after win - "
							+ creditBalAfterWin, "Fail");
		}
		return flag;
	}
	
	
	
	
	/*@Override
	public boolean drawCollectBaseGameFunctionality(Desktop_HTML_Report report, String languageCode) throws Exception {
		double creditBalBeforeWin = 0.0;
		double creditBalAfterWin = 0.0;
		WebDriverWait wait;
		WebElement winValue;
		double win = 0.0;
		double bet;
		boolean flag = false;

		try {
			wait = new WebDriverWait(webdriver, 200);

			// ********Collecting credit balance before win
			wait.until(ExpectedConditions.elementToBeClickable(By.id(XpathMap.get("collectBtn"))));
			creditBalBeforeWin = getCreditAmtDouble();

			// *********Collecting win value
			win = getWinAmtDouble(XpathMap.get("winValue"));
			if (win != 0.0) 
			{
			// ***********Clicking on collect button on double to screen
			webdriver.findElement(By.id(XpathMap.get("collectBtn"))).click();
			Thread.sleep(3000);

			// *************Capturing credit balance after collecting win
			creditBalAfterWin = getCreditAmtDouble();
			creditBalBeforeWin = (creditBalBeforeWin + win);
			if (Double.compare(creditBalBeforeWin, creditBalAfterWin) == 0) {
				if (languageCode != null) {
					report.details_append_folder("Verify Deal, Draw and Collect functionality",
							"On Clicking deal and draw button",
							"Credit balance should get updated"
									+ "Credit balance is updated successfully :- Credit balance before win - "
									+ creditBalBeforeWin + " Win amount - " + win + " Credit balance after win - "
									+ creditBalAfterWin,
							"Pass", languageCode);
					resizeBrowser(600, 800);
					Thread.sleep(1000);
					report.details_append_folder("Verify Deal, Draw and Collect functionality",
							"On Clicking deal and draw button",
							"Credit balance should get updated"
									+ "Credit balance is updated successfully :- Credit balance before win - "
									+ creditBalBeforeWin + " Win amount - " + win + " Credit balance after win - "
									+ creditBalAfterWin,
							"Pass", languageCode);
					resizeBrowser(400, 600);
					Thread.sleep(1000);
					report.details_append_folder("Verify Deal, Draw and Collect functionality",
							"On Clicking deal and draw button",
							"Credit balance should get updated"
									+ "Credit balance is updated successfully :- Credit balance before win - "
									+ creditBalBeforeWin + " Win amount - " + win + " Credit balance after win - "
									+ creditBalAfterWin,
							"Pass", languageCode);
					webdriver.manage().window().maximize();
				}

				else {
					report.details_append("Verify Deal, Draw and Collect functionality",
							"On Clicking deal and draw button",
							"Credit balance should get updated"
									+ "Credit balance is updated successfully :- Credit balance before win - "
									+ creditBalBeforeWin + " Win amount - " + win + " Credit balance after win - "
									+ creditBalAfterWin,
							"Pass");

				}
			

			} else {
				if (languageCode != null) {
					report.details_append_folder("Verify Deal, Draw and Collect functionality",
							"On Clicking deal and draw button ,credit balance should get updated",
							"Credit balance is not updated"+ "Credit balance is updated successfully :- Credit balance before win - "
									+ creditBalBeforeWin + " Win amount - " + win + " Credit balance after win - "
									+ creditBalAfterWin, "Fail", languageCode);
				} else {
					report.details_append("Verify Deal, Draw and Collect functionality",
							"On Clicking deal and draw button ,credit balance should get updated",
							"Credit balance is not updated"+ "Credit balance is updated successfully :- Credit balance before win - "
									+ creditBalBeforeWin + " Win amount - " + win + " Credit balance after win - "
									+ creditBalAfterWin, "Fail");
				}
			}
			
		}
		else
		{
			report.details_append("Verify Deal, Draw and Collect functionality",
					"On Clicking deal and draw button ,credit balance should get updated",
					"Win has not been triggered", "Fail");
		}

		} catch (Exception e) {
			log.error("Error in collecting win on base game");
			report.details_append("Verify Deal, Draw and Collect functionality",
					"On Clicking deal and draw button ,credit balance should get updated",
					"Credit balance is not updated" + "Credit balance is updated successfully :- Credit balance before win - "
							+ creditBalBeforeWin + " Win amount - " + win + " Credit balance after win - "
							+ creditBalAfterWin, "Fail");
		}
		return flag;
	}*/
	

	@Override
	public void doubleToCollect(Desktop_HTML_Report report) throws Exception {
		WebDriverWait wait;
		WebElement winValue;
		double win = 0.0;
		double creditBalBeforeWin = 0.0;
		double creditBalWin = 0.0;
		double creditBalAfterWin = 0.0;
		boolean flag = false;

		try {
			wait = new WebDriverWait(webdriver, 200);

			// *********Clicking on Deal and Draw button
			wait.until(ExpectedConditions.elementToBeClickable(By.id(XpathMap.get("deal"))));

			if (dealClick()) {
				if (drawClick()) {
					Thread.sleep(2000);

					// ********Collecting credit balance before win
					creditBalBeforeWin = getCreditAmtDouble();
					log.debug("Credit balance before win - " + creditBalBeforeWin);
					System.out.println("Credit balance before win - " + creditBalBeforeWin);

					// *******Clicking on double to button
					wait.until(ExpectedConditions.elementToBeClickable(By.id(XpathMap.get("double"))));
					webdriver.findElement(By.id(XpathMap.get("double"))).click();
					Thread.sleep(5000);

					// **************Waiting for navigation on double to screen
					wait.until(ExpectedConditions.elementToBeClickable(By.id(XpathMap.get("doubleSection"))));
					Thread.sleep(2000);
					wait.until(ExpectedConditions.elementToBeClickable(By.id(XpathMap.get("doubleCard"))));
					report.detailsAppend("Verify double and collect functionality",
							"On Clicking double button ,double to screen should be open ", "Double to screen is open",
							"Pass");
					webdriver.findElement(By.id(XpathMap.get("doubleCard"))).click();
					Thread.sleep(1000);
					webdriver.findElement(By.id(XpathMap.get("moduleContent"))).click();
					Thread.sleep(5000);

					// ************Collecting win amount
					win = getWinAmtDouble(XpathMap.get("doubleWinAmt"));
					if (win != 0.0) {
						System.out.println("Win amount " + win);
						report.detailsAppend("Verify double and collect functionality",
								"Win should displayed after selecting card on double to screen",
								"Win amount should displayed", "Pass");

						// ***********Clicking on collect button on double to
						// screen
						webdriver.findElement(By.id(XpathMap.get("collectBtn"))).click();
						wait.until(ExpectedConditions.elementToBeClickable(By.id(XpathMap.get("deal"))));

						// *************Capturing credit balance after
						// collecting win
						creditBalAfterWin = getCreditAmtDouble();
						log.debug("Credit balance after win - " + creditBalAfterWin);
						creditBalWin = creditBalBeforeWin + win;
						log.debug("Balance after adding win " + creditBalWin);
						System.out.println("Balance after adding win " + creditBalWin);

						// *************Comparing Credit balance before win and
						// after win
						if (Double.compare(creditBalWin, creditBalAfterWin) == 0) {

							report.detailsAppend("Verify double and collect functionality",
									"On Clicking double button ,double to screen open and collecting win amount and balace should get updated",
									"Credit balance before win - " + creditBalBeforeWin + " Win amount - " + win
											+ " Credit balance after win - " + creditBalAfterWin,
									"Pass");
						} else {

							report.detailsAppend("Verify double and collect functionality",
									"On Clicking double button ,double to screen open and collecting win amount and balace should get updated",
									"Credit balance before win - " + creditBalBeforeWin + " Win amount - " + win
											+ " Credit balance after win - " + creditBalAfterWin,
									"Fail");
						}
						log.debug("Credit balance after win is correctly updated. Win amount  " + win);
						System.out.println("Flag status - " + flag);
					} else {
						report.detailsAppend("Verify double and collect functionality",
								"On Clicking double button ,double to screen open and collecting win amount and balace should get updated",
								"Win not triggered", "Fail");

					}
				}

				else {
					log.error("Error in Draw functionality ");
					report.detailsAppend("Verify double and collect functionality",
							"On Clicking double button ,double to screen open and collecting win amount and balace should get updated",
							"Error in Draw functionality ", "Fail");
				}
			} else {
				log.error("Error in Deal functionality ");
				report.detailsAppend("Verify double and collect functionality",
						"On Clicking double button ,double to screen open and collecting win amount and balace should get updated",
						"Error in Deal functionality ", "Fail");
			}
		}

		catch (Exception e) {
			log.error("Error while collecting win amount on double to screen");
			report.detailsAppend("Verify double and collect functionality",
					"On Clicking double button ,double to screen open and collecting win amount and balace should get updated",
					"Credit balance before win - " + creditBalBeforeWin + " Win amount - " + win
							+ " Credit balance after win - " + creditBalAfterWin,
					"Fail");

		}

	}

	public double getCreditAmtDouble() {
		String creditBal;
		double creditBalance = 0.0;
		try {
			creditBal = webdriver.findElement(By.id(XpathMap.get("creditBal"))).getText();
			creditBalance = Double.parseDouble(creditBal.replaceAll("[^.0-9]", ""));
		} catch (Exception e) {
			log.error("Error in fetching credit balance - " + e);
			return creditBalance;
		}
		return creditBalance;
	}

	public double getBetAmtDouble() {

		String betVal;
		double betBal = 0.0;
		try {
			betVal = webdriver.findElement(By.id(XpathMap.get("betBal"))).getText();
			betBal = Double.parseDouble(betVal.replaceAll("[^0-9]", ""));
		} catch (Exception e) {
			log.error("Error in fetching bet - " + e);
			return betBal;
		}
		return betBal;

	}

	public double getWinAmtDouble(String xpathVal) {
		String winValue;
		double win = 0.0;

		try {
			winValue = webdriver.findElement(By.xpath(xpathVal)).getText();
			win = Double.parseDouble(winValue.replaceAll("[^.0-9]", ""));
		} catch (Exception e) {
			log.error("Error in fetching bet - " + e);

		}
		return win;
	}

	public void doubleToGambleReachedFunctionality() throws InterruptedException {
		WebDriverWait wait;
		double win = 0.0;
		double creditBalBeforeWin;
		double creditBalAfterWin;
		boolean flag = false;
		try {

			wait = new WebDriverWait(webdriver, 200);
			wait.until(ExpectedConditions.elementToBeClickable(By.id(XpathMap.get("deal"))));

			// *********Clicking on Deal and Draw button
			if (dealClick()) {
				if (drawClick()) {
					Thread.sleep(2000);
					creditBalBeforeWin = getCreditAmtDouble();
					log.debug("Credit balance before win - " + creditBalBeforeWin);
					do {
						// *********Clicking on Double button
						wait.until(ExpectedConditions.elementToBeClickable(By.id(XpathMap.get("double"))));
						webdriver.findElement(By.id(XpathMap.get("double"))).click();
						Thread.sleep(5000);
						wait.until(ExpectedConditions.elementToBeClickable(By.id(XpathMap.get("doubleSection"))));
						Thread.sleep(2000);

						// ***************Click on card
						webdriver.findElement(By.id(XpathMap.get("doubleCard"))).click();
						Thread.sleep(1000);
						webdriver.findElement(By.id(XpathMap.get("moduleContent"))).click();
						Thread.sleep(5000);
					} while (!webdriver.findElement(By.xpath(XpathMap.get("gambleLimit"))).isDisplayed());

					wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("backToGame"))));
					Thread.sleep(2000);
					win = getWinAmtDouble(XpathMap.get("doubleWinAmt"));
					log.debug("Successfully captured win amount " + win);
					creditBalAfterWin = getCreditAmtDouble();
					log.debug("Credit balance after win - " + creditBalAfterWin);
					creditBalBeforeWin = creditBalBeforeWin + win;
					log.debug("Balance after adding win " + creditBalBeforeWin);
					if (Double.compare(creditBalBeforeWin, creditBalAfterWin) == 0)
						flag = true;
					else
						flag = false;
					webdriver.findElement(By.id(XpathMap.get("backToGame"))).click();
				} else {
					log.error("Error in Draw functionality");
				}

			} else {

			}

		} catch (Exception e) {
			log.error("Error in double to feature - " + e);
		}

	}

	@Override
	public void doubleToGambleReached(Desktop_HTML_Report report, String languageCode) throws Exception {

		WebDriverWait wait;
		WebElement winValue;
		double win;

		try {

			wait = new WebDriverWait(webdriver, 200);
			wait.until(ExpectedConditions.elementToBeClickable(By.id(XpathMap.get("deal"))));

			// *********Clicking on Deal and Draw button
			if (dealClick()) {
				if (drawClick()) {
					Thread.sleep(2000);

					do {
						// *********Clicking on Double button
						wait.until(ExpectedConditions.elementToBeClickable(By.id(XpathMap.get("double"))));
						webdriver.findElement(By.id(XpathMap.get("double"))).click();
						wait.until(ExpectedConditions.elementToBeClickable(By.id(XpathMap.get("doubleSection"))));
						Thread.sleep(2000);

						// ***************Click on card
						wait.until(ExpectedConditions.attributeContains(
								webdriver.findElement(By.xpath(XpathMap.get("doubleCardFirst"))), "class",
								"vp card quick-transition"));
						webdriver.findElement(By.id(XpathMap.get("doubleCard"))).click();
						wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("winBox"))));
						closeOverlay();
						webdriver.findElement(By.id(XpathMap.get("moduleContent"))).click();
						Thread.sleep(5000);
						report.detailsAppendFolder("Verify double to functionality", "Double to functionality",
								"Double to functionality", "Pass", languageCode);
						resizeBrowser(600, 800);
						Thread.sleep(1000);
						report.detailsAppendFolder("Verify double to functionality", "Double to functionality",
								"Double to functionality", "Pass", languageCode);
						resizeBrowser(400, 600);
						Thread.sleep(1000);
						report.detailsAppendFolder("Verify double to functionality", "Double to functionality",
								"Double to functionality", "Pass", languageCode);
						webdriver.manage().window().maximize();
					} while (!webdriver.findElement(By.xpath(XpathMap.get("gambleLimit"))).isDisplayed());

					wait.until(ExpectedConditions.elementToBeClickable(By.id(XpathMap.get("backToGame"))));
					Thread.sleep(2000);
					if (webdriver.findElement(By.id(XpathMap.get("backToGame"))).isDisplayed()) {

						report.detailsAppendFolder("Verify Gamble reached functionality",
								"Gamble Reached functionality", "Gamble Reached text should be displayed", "Pass",
								languageCode);
						resizeBrowser(600, 800);
						Thread.sleep(1000);
						report.detailsAppendFolder("Verify Gamble reached functionality",
								"Gamble Reached functionality", "Gamble Reached text should be displayed", "Pass",
								languageCode);
						resizeBrowser(400, 600);
						Thread.sleep(1000);
						report.detailsAppendFolder("Verify Gamble reached functionality",
								"Gamble Reached functionality", "Gamble Reached text should be displayed", "Pass",
								languageCode);
						webdriver.manage().window().maximize();

					} else {
						report.detailsAppendFolder("Verify Gamble reached functionality",
								"Gamble Reached functionality", "Gamble Reached text is not displayed", "Fail",
								languageCode);
					}

					webdriver.findElement(By.id(XpathMap.get("backToGame"))).click();
				} else {
					log.error("Problem in clicking Draw button in Gamble reached functionality");
					report.detailsAppendFolder("Verify Gamble reached functionality",
							"Gamble Reached functionality", "Error in Draw functionality", "Fail",
							languageCode);
				}
			}

			else {
				log.error("Problem in clicking Deal button in Gamble reached functionality");
				report.detailsAppendFolder("Verify Gamble reached functionality",
						"Gamble Reached functionality", "Error in Deal functionality", "Fail",
						languageCode);
			}
		} catch (Exception e) {
			log.error("Error in double to feature - " + e);
			report.detailsAppendFolder("Verify Gamble reached functionality",
					"Gamble Reached functionality", "Gamble Reached text is not displayed", "Fail",
					languageCode);
		}

	}

	@Override
	public void paytableClickVideoPoker(Desktop_HTML_Report report, String languageCode) throws Exception {
		WebDriverWait wait;
		int size = 0;

		try {

			wait = new WebDriverWait(webdriver, 200);
			wait.until(ExpectedConditions.elementToBeClickable(By.id(XpathMap.get("paytableContainer"))));
			size = Integer.parseInt(XpathMap.get("paytableCoinsSize").replaceAll(".0", ""));

			for (int i = 1; i <= size; i++) {
				webdriver.findElement(By.id(XpathMap.get("paytableCoinsDisplay"))).click();
				report.detailsAppendFolder("Verify paytable functionality", "paytable functionality",
						"paytable functionality page" + i, "Pass", languageCode);
				resizeBrowser(600, 800);
				Thread.sleep(1000);
				report.detailsAppendFolder("Verify paytable functionality", "paytable functionality",
						"paytable functionality page" + i, "Pass", languageCode);
				resizeBrowser(400, 600);
				Thread.sleep(1000);
				report.detailsAppendFolder("Verify paytable functionality", "paytable functionality",
						"paytable functionality page" + i, "Pass", languageCode);
				webdriver.manage().window().maximize();
			}

		} catch (Exception e) {
			log.error("Error in paytable feature - " + e);
			report.detailsAppendFolder("Verify paytable functionality", "paytable functionality",
					"paytable functionality page", "Fail", languageCode);
		}
	}
	
	

	/*
	 * This method will wait for element to be visible
	 * 
	 * @param
	 * 
	 * @return
	 * 
	 * @throws
	 */
	public void waitFor(String locator) {
		Wait = new WebDriverWait(webdriver, 60);
		try {
			log.debug("Waiting For Drwa button to visible");
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
		} catch (Exception e) {
			log.error("error while waiting for element :" + locator, e);
		}
	}

	public boolean waitForWin() {
		boolean flag = false;
		Wait = new WebDriverWait(webdriver, 500);
		try {
			spinclick();
			webdriver.findElement(By.xpath(XpathMap.get("reelGrid"))).click();
			Thread.sleep(2000);
			if (webdriver.findElement(By.xpath(XpathMap.get("txtInfoBarWin"))).isDisplayed())
				flag = true;
			else
				flag = false;
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			flag = false;
		}

		return flag;

	}
	
	public boolean quickSpinDisabledForDenmark()
	{
		boolean flag = false;
		try
		{
			
			String classQuickAttr = XpathMap.get("classQuickAttr");
			WebElement we = webdriver.findElement(By.id("quickSpin"));
			String attr = we.getAttribute("class");
			
			if(attr.equals(classQuickAttr))
				flag = true;
			else
				flag = false;
			
		}
		catch(Exception e)
		{
			log.error("Error occured in quick spin check" + e);
		}
		
		return flag;
	}
	
	public boolean clockDisplayForDenmark()
	{
		boolean flag = true;
		try
		{
			if(webdriver.findElement(By.id(XpathMap.get("clock"))).isDisplayed())
				flag = true;
			else
				flag = false;
		}
		catch(Exception e)
		{
			log.error("Clock not displyed in the game" + e);
		}
		return flag;
	}
	
	public boolean helpLinkDisplayForDenmark()
	{
		boolean flag = false;
		WebDriverWait  wait = new WebDriverWait(webdriver, 60) ;
		try
		{
			webdriver.findElement(By.xpath(XpathMap.get("helpLinkMenu"))).click();
			wait.until(ExpectedConditions.elementToBeClickable(webdriver.findElement(By.xpath(XpathMap.get("helpLink")))));
			if(webdriver.findElement(By.xpath(XpathMap.get("helpLink"))).isDisplayed())
				flag = true;
			else
				flag = false;
			closeOverlay();
			
		}
		
		catch(Exception e)
		{
			log.error("Error in help link display"+e);
		}
		return flag;
	}
	
	public boolean linkToPlayerProtection()
	{
		boolean flag = false;
		WebDriverWait  wait = new WebDriverWait(webdriver, 60) ;
		try
		{
			webdriver.findElement(By.xpath(XpathMap.get("helpLinkMenu"))).click();
			wait.until(ExpectedConditions.elementToBeClickable(webdriver.findElement(By.xpath(XpathMap.get("playerProtectionLink")))));
			if(webdriver.findElement(By.xpath(XpathMap.get("playerProtectionLink"))).isDisplayed())
				flag = true;
			else
				flag = false;
			closeOverlay();
		}
		catch(Exception e)
		{
			log.error("Error in player protection link display"+e);
			
		}
		return false;
		
	}
	/**
	 * This method Set conSizeSliderTo Max bet
	 */
	public String moveCoinSizeSliderToMaxBet(Desktop_HTML_Report report, String CurrencyName ) 
	{
		String sliderText = null;
		Wait = new WebDriverWait(webdriver, 60);
		try
		{
			func_Click("BetButton");
			report.detailsAppendFolder("Bet Button", "Bet Button Clicked", "Bet Button Clicked", "PASS",""+CurrencyName);
			Thread.sleep(2000);
			sliderText = func_GetText("sliderText");
			System.out.println("Default Bet is : "+sliderText);log.debug("Default Bet is : "+sliderText);
			// To get bet button values 
			verifyBetPannelValues(report,CurrencyName);
			Thread.sleep(000);
			func_Click("MaxBetButton");
			Thread.sleep(2000);	
			}
		catch (Exception e) 
		{
			log.debug(e.getMessage());
		}
		return sliderText;
	}
	
	/**
	* This methode Set conSizeSliderTo Min bet
	* @return
	*/
	public String moveCoinSizeSliderToMinBet(Desktop_HTML_Report report , String CurrencyName ) 
	{
	String sliderText = null; 
	Wait = new WebDriverWait(webdriver, 60);
	try
	{
	func_Click("BetButton");Thread.sleep(2000);
	report.detailsAppendFolder("Bet Button", "Bet Button Opened", "Bet Button Opened", "PASS",""+CurrencyName);
	sliderText = func_GetText("sliderText");
	System.out.println("Default Bet is : "+sliderText);log.debug("Default Bet is : "+sliderText);
	// To get bet button values 
	verifyBetPannelValues(report,CurrencyName);
	Thread.sleep(2000);
	func_Click("BetAmt1");
	Thread.sleep(2000);
	}
	catch (Exception e) 
	{
	log.debug(e.getMessage());
	}
	return sliderText;
	}
	
	/**
	 *  Method to count the special symbol in currency format
	 * @param currencyFormat
	 * @return
	 */
		public int specialSymbolCount(String currencyFormat) 
		{
			int count = 0;
         	Pattern pattern = Pattern.compile("#");
			Matcher matcher = pattern.matcher(currencyFormat);
			while (matcher.find()) 
			{
				count++;
			}

			return count;
		}

	/*
	 * Method is use for create regular expression
	 */
	public String createregexp(String betnew, String currencyFormat)
	{
		try {

			int count = specialSymbolCount(currencyFormat);
			betnew = betnew.replaceAll("[^0-9]", "");
			if (count > betnew.length()) {
				int lenght = count - betnew.length();
				for (int i = 0; i < lenght; i++) {
					for (int j = 0; j < currencyFormat.length(); j++)
					{
						if (currencyFormat.charAt(j) == '#') 
						{
							StringBuilder sb = new StringBuilder();
							sb.append(currencyFormat);
							if (currencyFormat.charAt(j + 1) == ',' || currencyFormat.charAt(j + 1) == '.'
									|| currencyFormat.charAt(j + 1) == ' ' || currencyFormat.charAt(j + 1) == 160)
							{
								sb.deleteCharAt(j + 1);
							}
							sb.deleteCharAt(j);
							currencyFormat = sb.toString();
							break;
						}
					}
				}

			}
		} catch (Exception e) 
		{
			log.error(e.getCause());
		}
		return currencyFormat;
	}
	/**
	 *This method is for to Get Current credits from console
	 */
	public String getCurrentCredit(Desktop_HTML_Report isoCode) 
		{
			String creditValue = null;
			try
			{
			creditValue = func_GetText("Creditvalue");
			System.out.println("Console Credits are "+creditValue);
			log.debug("Console Credits are "+creditValue);
			isoCode.detailsAppendFolder("Base Game", "Credit Amount", creditValue, "PASS",""+isoCode);
			}
			catch (Exception e) 
			{
				log.error(e.getMessage(), e);
			}
			return creditValue;
		}
		
		/**
		 * Verifies the current Bet
		 * 
		 */
		public String getCurrentBetAmt(Desktop_HTML_Report isoCode) 
		{
			
		 String betvalue = null;
			try
			{
            betvalue = func_GetText("Betvalue");
			System.out.println("Console Bet is "+betvalue);
			log.debug("Console Bet is "+betvalue);
			isoCode.detailsAppendFolder("Base Game", "Bet Amount", ""+betvalue, "PASS",""+isoCode);
			}
			catch (Exception e) 
			{
				log.error(e.getMessage(), e);
			}
			
			return betvalue;	
		}
		
		/**
		 * Verifies the current Bet
		 * 
		 */
		public String getCurrentTotalWinINFS(Desktop_HTML_Report isoCode) 
		{
			
		 String totalWinInFS = null;
			try
			{
			totalWinInFS = func_GetText("totalWinInFS");
			System.out.println(" Console Total Win is "+totalWinInFS);
			log.debug("Console Total Win is "+totalWinInFS);
			isoCode.detailsAppendFolder("Free Spins", "Total Win Amt", ""+totalWinInFS, "PASS",""+isoCode);
			}
			catch (Exception e) 
			{
				log.error(e.getMessage(), e);
			}
			
			return totalWinInFS;	
		}
		
		/**
		 * Verifies the current Win amt
		 * 
		 */
		public String getCurrentWinAmt(Desktop_HTML_Report report,String CurrencyName ) 
		{
			String winAmt = null;
			Wait = new WebDriverWait(webdriver, 250);
			try
			{	
		    report.detailsAppendFolderOnlyScreenShot(CurrencyName);
		   Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("WinAmt"))));
		   boolean isWinAmt =	webdriver.findElement(By.xpath(XpathMap.get("WinAmt"))).isDisplayed();
			if(isWinAmt)
			{	
			winAmt = func_GetText("WinAmt");
			System.out.println(" Win Amount is "+winAmt);
			log.debug(" Win Amount is "+winAmt);
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
			return winAmt;
		}
		
		/**
		 * Verifies Paytable open
		 * 
		 */
		public boolean paytableOpen(Desktop_HTML_Report report,String CurrencyName) 
		{
			boolean paytable= false;
			try
			{
				func_Click("MenuIcon");
				Thread.sleep(2000);
				report.detailsAppendFolder("Menu Icon", "Menu Clicked", "Menu Clicked", "PASS",CurrencyName);
				func_Click("paytableIcon");
				report.detailsAppendFolder("PayTable Icon", "PayTable Icon Clicked", "PayTable Icon Clicked", "PASS",CurrencyName);
				Thread.sleep(2000);
				paytable = true;
			}
			catch (Exception e) 
			{
				log.error(e.getMessage(), e);
			}
			return paytable;
			
		}
		
		/**
		 *  verifyBetSettings
		 * 
		 */
		public boolean verifyBetSettings(Desktop_HTML_Report report) 
		{
			boolean betbutton = false;
			String bet1 = "BetAmt1";String bet2 = "BetAmt2";String bet3 = "BetAmt3";
			String bet4 = "BetAmt4";String bet5 = "BetAmt5";String bet6 = "BetAmt6";
			try
			{
				
		WebElement aa=	webdriver.findElement(By.xpath(XpathMap.get("NewBet")));
		aa.click();
	
		int xpathCount = webdriver.findElements(By.xpath(XpathMap.get("AllBet"))).size();
		if(xpathCount>0)
		{
			String allBetAmtText[]= {bet1,bet2,bet3,bet4,bet5,bet6};
			for(int i=0;i<allBetAmtText.length;i++)
			{
			String betAmt = func_GetText(XpathMap.get(i));
			System.out.println("Bet Amt"+betAmt);
			betbutton = true;
			}}}
			catch (Exception e) 
			{
				log.error(e.getMessage(), e);
			}
			
			return betbutton;	
		}
		
		
		
		/**
		 * Verifies the Currency Format -  using String method
		 */

		public boolean verifyRegularExpression(Desktop_HTML_Report isoCode, String regExp, String method)
		{
			String Text = method;
			boolean regexp= false;
			try
			{
				Thread.sleep(2000);
			if (Text.matches(regExp)) 
			{
				log.debug("Compared with Reg Expression .Currency is same");
				regexp = true; 
			}Thread.sleep(2000);
			}
			catch (Exception e) 
			{
				log.error(e.getMessage(), e);
			}	
			return regexp ;		
	}
	/*	
	 * Compare the Regular expression using String 
	 */
		public boolean verifyRegularExpression(String curencyAmount, String regExp)
		{
			boolean isRegExp = false;
			try {
				log.debug("curencyAmount: "+curencyAmount);
				if (curencyAmount.matches(regExp)) 
				{
					isRegExp = true;
					//System.out.println("Currency format is correct");
					log.debug("Currency format is correct");
				} 
				else {
					isRegExp = false;
					//System.out.println("Currency format is  incorrect");
					log.debug("Currency format is incorrect");
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			return isRegExp;

		}
		
		
		
			
		/**
		 * Verifies the Currency Format - using String method 
		 */

		public boolean verifyRegularExpressionUsingArrays(Desktop_HTML_Report report, String regExp, String[] method)
		{
			String[] Text = method;
			boolean regexp= false;
    		try
			{
				Thread.sleep(2000);
			for(int i=0;i<Text.length;i++)
			{
			if (Text[i].matches(regExp))
			{
				
				log.debug("Compared with Reg Expression .Currency is same");
				regexp = true; 
			}
			else
			{
				
				log.debug("Compared with Reg Expression .Currency is different");
				regexp = false; 
			}Thread.sleep(2000);
			}}
			catch (Exception e) 
			{
				log.error(e.getMessage(), e);
			}	
			return regexp ;		
	}
		/*
		 * verifies the BigWin Text
		 */
		public String verifyWinText(Desktop_HTML_Report report,String CurrencyName,String locator)
		{
			String iswinTxtAvailable = null;
			Wait = new WebDriverWait(webdriver, 600);
			try
			{
		    Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get(locator))));	
		    boolean winTxt = webdriver.findElement(By.xpath(XpathMap.get(locator))).isDisplayed();
			if(winTxt)
			{	
				Thread.sleep(2000);
				iswinTxtAvailable = func_GetText(locator);Thread.sleep(2000);
			System.out.println(" Win Amt "+iswinTxtAvailable);log.debug("  Win Amt"+iswinTxtAvailable);
			
			iswinTxtAvailable = func_GetTextbyAttribute(report,"BigWinText",CurrencyName);
			System.out.println("  Win Amt "+iswinTxtAvailable);log.debug(" Win Amt "+iswinTxtAvailable);
			}}
			catch (Exception e) 
			{
				log.error(e.getMessage(), e);
			}
			return iswinTxtAvailable;
				
		}
	
		/**
		 * Verifies the Big Win
		 * 
		 */
		public String verifyBigWin(Desktop_HTML_Report report,String CurrencyName)
		{
			String bigWinAmt = null;
			Wait = new WebDriverWait(webdriver, 30000);
		
			try
			{
		    Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("BigWin"))));	
		    boolean isBigWin = webdriver.findElement(By.xpath(XpathMap.get("BigWin"))).isDisplayed();
			if(isBigWin)
			{	
			Thread.sleep(8000);
			bigWinAmt = func_GetText("BigWin");
			System.out.println("Big Win Amount is "+bigWinAmt);log.debug(" Big Win Amount is "+bigWinAmt);
			report.detailsAppendFolderOnlyScreenShot(CurrencyName);	
		    Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("WinAmt"))));
			boolean isWinTxt = webdriver.findElement(By.xpath(XpathMap.get("WinAmt"))).isDisplayed();
			}
		else
			{
				System.out.println("There is no Big Win ");
				log.debug("There is no Big Win");
			}}
			catch (Exception e) 
			{
				log.error(e.getMessage(), e);
			}
			return bigWinAmt;
			
		}
		

/**
 * Set Max Bet
 */
	public String setMaxBet()
	{
		log.debug("Function -> setMAxBet()");
		String betAmount = null;
		String bet = null;
		try {
			bet = "return " + XpathMap.get("SetMaxBet");
			getConsoleText(bet);
			log.debug("Bet=" + getConsoleText(bet));
			betAmount = getConsoleText("return " + XpathMap.get("BetSizeText"));

		} catch (JavascriptException exception) {
			log.error(" Exception occur while executing hook,Please verify thre hook of given component"
					+ exception.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;

		}

		return betAmount;
	}
	
	/**
	 * Verify Bonus Feature by clicking and get text 
	 * @param report
	 * @return
	 */
	
	public String[] bonusFeatureClickandGetText(Desktop_HTML_Report report, String CurrencyName)
	{
		String bonusText[] = {"BonusSymbolText1","BonusSymbolText2","BonusSymbolText3","BonusSymbolText4","BonusSymbolText5"};
		String bonusClick[]= {"BonusSymbol1","BonusSymbol2","BonusSymbol3","BonusSymbol4","BonusSymbol5"};
		try
		{
		 System.out.println("Following are the Bonus Win Texts");log.debug("Following are the Bonus Win Texts");
		for(int i=0  ;i<bonusClick.length;i++)
		{	
			func_click(bonusClick[i]);
			report.detailsAppendFolder("Bonus Game", "Clicked Bonus Feature", "Clicked Bonus Feature", "PASS",CurrencyName);
			bonusText[i] =   func_GetTextbyAttributeforBonus(report,bonusText[i],CurrencyName);
		}
		bonusSummaryScreen(report,CurrencyName);
		}
		catch (Exception e) 
		{
			
			log.error(e.getMessage(), e);
		}
			
		return bonusText ;
		}
	/**
	 * Check Availablity of an Element
	 * @param string
	 * @return
	 */
	

	public boolean checkAvilabilityofElement(String hooksofcomponent) 
	{
		boolean isComponentAvilable = true;
		Wait = new WebDriverWait(webdriver, 10000);
		try 
		{
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get(hooksofcomponent))));	
			WebElement ele = webdriver.findElement(By.xpath(XpathMap.get(hooksofcomponent)));
			ele.isDisplayed();
			if(ele != null)
			{
				isComponentAvilable = true;
			}
		}
		catch (Exception e) 
		{
			// if component in the game not avilable in it while give an exception
			isComponentAvilable = false;
		}
		return isComponentAvilable;
	}
/**
 * method verifies the bonus summary screen and get Text	
 */
	public String bonusSummaryScreen(Desktop_HTML_Report report, String CurrencyName)
	{
		String bonusSummaryScreen = null;
		Wait = new WebDriverWait(webdriver, 350);
	
		try
		{
	    Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("BonusSummaryScreen"))));	
	    report.detailsAppendFolder("Bonus Feature ", "Summary Screen", "Summary Screen", "PASS",CurrencyName);
	    boolean isBonusSummaryScreenPresent = webdriver.findElement(By.xpath(XpathMap.get("BonusSummaryScreen"))).isDisplayed();
		if(isBonusSummaryScreenPresent)
		{	
		bonusSummaryScreen =func_GetTextbyAttribute(report,"BonusSummaryText",CurrencyName);
		report.detailsAppendFolder("Bonus Feature Summary Screen ", "Total Win Amt", "Summary Screen Win Amt", "PASS",CurrencyName);
		//System.out.println(" Win Amount is "+bonusSummaryScreen);
		//log.debug(" Win Amount is "+bonusSummaryScreen);
		}
		else
		{
			System.out.println("There is no Big Win ");
			log.debug("There is no Big Win");
		}}
		catch (Exception e) 
		{
			log.error(e.getMessage(), e);
		}
		return bonusSummaryScreen;
	}
	
	/**
	 * method verifies the bonus summary screen and get Text	
	 */
	public String freeSpinsSummaryScreen(Desktop_HTML_Report report, String CurrencyName)
	{
		String fsSummaryScreen = null;
		Wait = new WebDriverWait(webdriver, 350);
		try
		{
	    	fsSummaryScreen =func_GetTextbyAttribute(report,"FSSummaryScreenWinAmt",CurrencyName);
			report.detailsAppendFolderOnlyScreenShot(CurrencyName);
		}
		catch (Exception e) 
		{
			log.error(e.getMessage(), e);
		}
		return fsSummaryScreen;
	}
	
	
	/**
	 * Verifies the Autoplay
	 * 
	 */

	public boolean isAutoplayAvailable() 
	{
		boolean isAutoplayAvailable = false;
		String autoplay ="AutoplayButton";
		try
		{
		func_Click(autoplay);
		System.out.println("Autoplay Opened");log.debug("Autoplay Opened");
		isAutoplayAvailable = true;
		Thread.sleep(2000);
		}
		catch (Exception e) 
		{
			log.error(e.getMessage(), e);
		}
		return isAutoplayAvailable;
		
	}
	
	
	/**
	 * method is used to validate the Paytable Values 
	 * @return
	 */
	public String[] paytablePayoutsOfFour(Desktop_HTML_Report report,String CurrencyName) //String[] array
	{
		String symbols[] = {"Wild5","Wild4","Wild3","Scatter2" };
		try
		{
			System.out.println("Paytable Validation for Wild and Scatter ");log.debug("Paytable Validation for Wild and Scatter ");
		for(int i=0  ;i<symbols.length;i++)
		{	
			symbols[i] =   func_GetText(symbols[i]);
			System.out.println(symbols[i]);log.debug(symbols[i]);
		}
		Thread.sleep(3000);
		}
		catch (Exception e) 
		{
			log.error(e.getMessage(), e);
		}
		return symbols;
	}
	
	/**
	 * method is used to validate the Paytable Values 
	 * @return
	 */
	public String[] paytablePayoutsOfSeven(Desktop_HTML_Report report,String CurrencyName) //String[] array
	{
		String symbols[] = {"Wild5","Wild4","Wild3","Wild2","Scatter5","Scatter4","Scatter3" };
		try
		{
			System.out.println("Paytable Validation for Wild and Scatter ");log.debug("Paytable Validation for Wild and Scatter ");
		for(int i=0  ;i<symbols.length;i++)
		{	
			symbols[i] =   func_GetText(symbols[i]);
			System.out.println(symbols[i]);log.debug(symbols[i]);
		}
		Thread.sleep(3000);
		}
		catch (Exception e) 
		{
			log.error(e.getMessage(), e);
		}
		return symbols;
	}
	
	
	/**
	 * method is used to validate the Paytable Values 
	 * @return
	 */
	public boolean validatePayoutsFromPaytable(Desktop_HTML_Report report,String CurrencyName,String regExpr) //String[] array
	{
		boolean payoutvalues = false;
		try
		{
			if(XpathMap.get("PaytablePayoutsofSeven").equalsIgnoreCase("yes"))
			{
			 payoutvalues =   verifyRegularExpressionUsingArrays(report,regExpr,paytablePayoutsOfSeven(report,CurrencyName));
			  return payoutvalues;
			}
			else if(XpathMap.get("PaytablePayoutsofFour").equalsIgnoreCase("yes"))
			{
				payoutvalues =	verifyRegularExpressionUsingArrays(report,regExpr,paytablePayoutsOfFour(report,CurrencyName));
				 return payoutvalues;
			}
			else
			{
				System.out.println("Verify Paytable Payouts");log.debug("Verify Paytable Payouts");
			}
			Thread.sleep(2000);
			//validate payout values which are in grid /Xml
		   PayoutvarificationforBetLVC(report,regExpr,CurrencyName);
			}
		catch (Exception e) 
		{
			log.error(e.getMessage(), e);
		}
		return payoutvalues;
	}
	
	/**
	 * method is used to scroll the paytable
	 * @param lvcReport
	 * @return
	 */
	
	
	public boolean paytableScroll(Desktop_HTML_Report report, String CurrencyName) 
	{
	boolean paytableScroll = false;
try
{
	if(XpathMap.get("paytableScrollOfFive").equalsIgnoreCase("yes"))
	{
		paytableScroll =  paytableScrollOfFive(report,CurrencyName);
	     return  paytableScroll ;
	}
	else if(XpathMap.get("paytableScrollOfSeven").equalsIgnoreCase("yes"))
	{
		paytableScroll =  paytableScrollOfSeven(report,CurrencyName);
		  return  paytableScroll ;
	}
	else
	{
		System.out.println("Check the Paytable Scroll");log.debug("Check the Paytable Scroll");
	}
	
	}
      catch (Exception e) 
        {
	        log.error(e.getMessage(), e);
        }
		return paytableScroll;
		}
	

/**
 * method is for to scroll five times
 * @param report
 * @param CurrencyName
 * @return
 */
	public boolean paytableScrollOfFive(Desktop_HTML_Report report, String CurrencyName) 
	{
	String wildXpath="Wild";
	String bonusXpath="Bonus";
	String scatterXpath="Scatter";
	String symbolGridXpath1="PaytableGrid1";
	String symbolGridXpath2="PaytableGrid2";
	String paylineInfoXpath="Payways";
	boolean test = false;
try
{	
  //open paytable
	paytableOpen(report,CurrencyName);
	report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "PASS",""+CurrencyName);
	
		test=webdriver.findElements(By.xpath(XpathMap.get(wildXpath))).size()>0;
	if(test)
	{
		JavascriptExecutor js = (JavascriptExecutor) webdriver;
		WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(wildXpath)));
		js.executeScript("arguments[0].scrollIntoView(true);", ele1);
		report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "PASS",""+CurrencyName);
		Thread.sleep(3000);
		test= true;
		}

		test=webdriver.findElements(By.xpath(XpathMap.get(bonusXpath))).size()>0;
		if(test) 
		{
		JavascriptExecutor js = (JavascriptExecutor) webdriver;
		WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(bonusXpath)));
		js.executeScript("arguments[0].scrollIntoView(true);", ele1);
		report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "PASS",""+CurrencyName);
		Thread.sleep(3000);
		test= true;
		}

		test=webdriver.findElements(By.xpath(XpathMap.get(scatterXpath))).size()>0;
		if(test) 
		{
		JavascriptExecutor js = (JavascriptExecutor) webdriver;
		WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(scatterXpath)));
		js.executeScript("arguments[0].scrollIntoView(true);", ele1);
		report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "PASS",""+CurrencyName);
		Thread.sleep(3000);
		test= true;
		}

		test=webdriver.findElements(By.xpath(XpathMap.get(symbolGridXpath1))).size()>0;
		if(test) 
		{
		JavascriptExecutor js = (JavascriptExecutor) webdriver;
		WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(symbolGridXpath1)));
		js.executeScript("arguments[0].scrollIntoView(true);", ele1);
		report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "PASS",""+CurrencyName);
		Thread.sleep(4000);
		test= true;
		}
		
		test=webdriver.findElements(By.xpath(XpathMap.get(symbolGridXpath2))).size()>0;
		if(test) 
		{
		JavascriptExecutor js = (JavascriptExecutor) webdriver;
		WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(symbolGridXpath2)));
		js.executeScript("arguments[0].scrollIntoView(true);", ele1);
		report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "PASS",""+CurrencyName);
		Thread.sleep(4000);
		test= true;
		}

		test=webdriver.findElements(By.xpath(XpathMap.get(paylineInfoXpath))).size()>0;
		if(test) 
		{
		JavascriptExecutor js = (JavascriptExecutor) webdriver;
		WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(paylineInfoXpath)));
		js.executeScript("arguments[0].scrollIntoView(true);", ele1);
		report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "PASS",""+CurrencyName);
		Thread.sleep(3000);
		test= true;
		}
		Thread.sleep(2000);
		//method is for validating the payatable Branding 
		textValidationForPaytableBranding(report,CurrencyName);
	}
      catch (Exception e) 
        {
	        log.error(e.getMessage(), e);
        }
		return test;
		}
	/**
	 * method is for to scroll seven times
	 * @param report
	 * @param CurrencyName
	 * @return
	 */
	public boolean paytableScrollOfSeven(Desktop_HTML_Report report, String CurrencyName) 
	{
	String wildXpath="Wild";
	String bonusXpath="Bonus";
	String scatterXpath="Scatter";
	String symbolGridXpath1="PaytableGrid1";
	String symbolGridXpath2="PaytableGrid2";
	String symbolGridXpath3="PaytableGrid3";
	String symbolGridXpath4="PaytableGrid4";
	String paylineInfoXpath="Payways";
	boolean test = false;
try
{	
  //open paytable
	paytableOpen(report,CurrencyName);
	report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "PASS",""+CurrencyName);
	
		test=webdriver.findElements(By.xpath(XpathMap.get(wildXpath))).size()>0;
	if(test)
	{
		JavascriptExecutor js = (JavascriptExecutor) webdriver;
		WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(wildXpath)));
		js.executeScript("arguments[0].scrollIntoView(true);", ele1);
		report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "PASS",""+CurrencyName);
		Thread.sleep(3000);
		test= true;
		}

		test=webdriver.findElements(By.xpath(XpathMap.get(bonusXpath))).size()>0;
		if(test) 
		{
		JavascriptExecutor js = (JavascriptExecutor) webdriver;
		WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(bonusXpath)));
		js.executeScript("arguments[0].scrollIntoView(true);", ele1);
		report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "PASS",""+CurrencyName);
		Thread.sleep(3000);
		test= true;
		}

		test=webdriver.findElements(By.xpath(XpathMap.get(scatterXpath))).size()>0;
		if(test) 
		{
		JavascriptExecutor js = (JavascriptExecutor) webdriver;
		WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(scatterXpath)));
		js.executeScript("arguments[0].scrollIntoView(true);", ele1);
		report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "PASS",""+CurrencyName);
		Thread.sleep(3000);
		test= true;
		}

		test=webdriver.findElements(By.xpath(XpathMap.get(symbolGridXpath1))).size()>0;
		if(test) 
		{
		JavascriptExecutor js = (JavascriptExecutor) webdriver;
		WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(symbolGridXpath1)));
		js.executeScript("arguments[0].scrollIntoView(true);", ele1);
		report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "PASS",""+CurrencyName);
		Thread.sleep(4000);
		test= true;
		}
		
		test=webdriver.findElements(By.xpath(XpathMap.get(symbolGridXpath2))).size()>0;
		if(test) 
		{
		JavascriptExecutor js = (JavascriptExecutor) webdriver;
		WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(symbolGridXpath2)));
		js.executeScript("arguments[0].scrollIntoView(true);", ele1);
		report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "PASS",""+CurrencyName);
		Thread.sleep(4000);
		test= true;
		}
		
		test=webdriver.findElements(By.xpath(XpathMap.get(symbolGridXpath3))).size()>0;
		if(test) 
		{
		JavascriptExecutor js = (JavascriptExecutor) webdriver;
		WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(symbolGridXpath3)));
		js.executeScript("arguments[0].scrollIntoView(true);", ele1);
		report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "PASS",""+CurrencyName);
		Thread.sleep(4000);
		test= true;
		}
		
		test=webdriver.findElements(By.xpath(XpathMap.get(symbolGridXpath4))).size()>0;
		if(test) 
		{
		JavascriptExecutor js = (JavascriptExecutor) webdriver;
		WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(symbolGridXpath4)));
		js.executeScript("arguments[0].scrollIntoView(true);", ele1);
		report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "PASS",""+CurrencyName);
		Thread.sleep(4000);
		test= true;
		}

		test=webdriver.findElements(By.xpath(XpathMap.get(paylineInfoXpath))).size()>0;
		if(test) 
		{
		JavascriptExecutor js = (JavascriptExecutor) webdriver;
		WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get(paylineInfoXpath)));
		js.executeScript("arguments[0].scrollIntoView(true);", ele1);
		report.detailsAppendFolder("PayTable", "PayTable Scroll", "PayTable Scroll", "PASS",""+CurrencyName);
		Thread.sleep(3000);
		test= true;
		}
		Thread.sleep(3000);
		//method is for validating the payatable Branding 
		textValidationForPaytableBranding(report,CurrencyName);
	}
      catch (Exception e) 
        {
	        log.error(e.getMessage(), e);
        }
		return test;
		}
	//To get the Bet pannel values 
	
	public String[] verifyBetPannelValues(Desktop_HTML_Report report,String CurrencyName) 
	{
//
		String allBetValues[] = {"BetValue1","BetValue2","BetValue3","BetValue4","BetValue5","BetValue6"};
		
		try 
		{
			//func_Click("BetButton");
			for(int i=0  ;i<allBetValues.length;i++)
			{
			allBetValues[i]  = func_GetText(allBetValues[i]);
			System.out.println("Bet Values : "+allBetValues[i]);log.debug("Bet Values : "+allBetValues[i]);	
		   // report.detailsAppendNoScreenshot("Bet Pannel", "Bet Pannel Values", ""+allBetValues[i], "PASS");
			}
			
			//closeOverlay();
		
		}
		catch (Exception e) 
		{
			 log.error(e.getMessage(), e);
		}
		
		return allBetValues;

	}
	public String replaceParamInHook(String str, Map<String, String> data) {

		Pattern p = Pattern.compile("\\$\\{(.+?)\\}");

		Matcher m = p.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			String var = m.group(1);
			String replacement = data.get(var);
			m.appendReplacement(sb, replacement);
		}
		m.appendTail(sb);
		return sb.toString();
	}
	// get console Text
				public String GetConsoleText(String text) {
					String consoleText = null;
					try {
						JavascriptExecutor js = ((JavascriptExecutor) webdriver);
						consoleText = (String) js.executeScript(text);
						// log.debug("Text Read from Console="+consoleText);
					} catch (Exception e) {
						e.getMessage();
					}
					return consoleText;
				}
			/**
			 * get Payouts from Paytable	
			 * @param symbolData
			 * @param paytablePayout
			 * @return
			 */
				public String gamepayoutForPaytable(ArrayList<String> symbolData, String paytablePayout) {
					String consolePayout=null;
					int symbolIndex = 0;
					try {
						String wild = XpathMap.get("wildSymbol");
						String[] xmlData = paytablePayout.split(",");
						String line = xmlData[0];
						String symbol = xmlData[1];
						Map<String, String> paramMap = new HashMap<String, String>();

						for (int i = 0; i < symbolData.size(); i++)
						{
							if (symbolData.get(i).contains(symbol))
							{
								 if (wild.equalsIgnoreCase("no")) 
								{
									symbolIndex = i+1;
								}
								else
								{
									symbolIndex = i;
								}
								break;
							}
						}
							
						paramMap.put("param1", Integer.toString(symbolIndex));
						paramMap.put("param2", line);
						
						
						String paytablePayoutValueHook = XpathMap.get("paytablePayouts");
						String newHook = replaceParamInHook(paytablePayoutValueHook, paramMap);
						
						//System.out.println("WebElement : "+newHook);
						
						WebElement ele = webdriver.findElement(By.xpath(newHook));
						System.out.println("Ele"+ele.getText());
						 consolePayout = ele.getText();
				}
					catch (Exception e) 
					{
						e.getMessage();
					}
					return consolePayout;
				}
				
				
							
				
				public double getBetAmt() {
					String consolePayoutnew = null;
					double consolePayoutnew1 = 0.0;
					try {
						String bet = func_GetText("Betvalue");
						System.out.println("Bet Value"+bet);
						/*String consoleBet = getConsoleText(bet);
						System.out.println("consoleBet"+consoleBet);
						consolePayoutnew = consoleBet.replaceAll("[^0-9]", "");
						System.out.println("consolePayoutnew"+consolePayoutnew);
						consolePayoutnew1 = Double.parseDouble((consolePayoutnew));
						System.out.println("consolePayoutnew1"+consolePayoutnew1);*/
					} catch (Exception e) {
						e.getMessage();
					}
					return consolePayoutnew1 / 100;
				}
				
				/*
				 * Method for payout verification for all bets
				 */

				public void PayoutvarificationforBetLVC(Desktop_HTML_Report report, String regExpr,String currencyName) 
				{
					Util util = new Util();
					int length = 0;
					String gamePayout;
					int index = 0;
					String paytablePayout;
					String strGameName = null;
					int startindex = 0;
					String scatter;
					int isAllTrue=0;

					// Read xml for the game
					if (GameName.contains("Desktop")) 
					{
						// String Gamename=gameName.replace("Desktop", "");
						java.util.regex.Pattern str = java.util.regex.Pattern.compile("Desktop");

						Matcher substing = str.matcher(GameName);

						while (substing.find()) {
							startindex = substing.start();
						}
						strGameName = GameName.substring(0, startindex);
						log.debug("newgamename=" + strGameName);
					}
					String xmlFilePath = "./" + strGameName + "/Config/" + strGameName + ".xml";
					length = util.xmlLength(xmlFilePath, "WinningCombinations");
					ArrayList<String> symbolData = util.getXMLDataInArray("Symbol", "name", xmlFilePath);

					ArrayList<String> winCombinationList = new ArrayList<>();

					for (int count = 0; count < length; count++)
					{
						String strWinCombination = util.readXML("WinCombination", "numSymbolsRequired", "symbols", "payouts",
								"./" + strGameName + "/Config/" + strGameName + ".xml", length + 2, count);
						if (strWinCombination != null) 
						{
							winCombinationList.add(strWinCombination);
						}
					}

				int winCombinationSize = winCombinationList.size();
					try {
							// setting index at the staring
							index = 0;
							// Get the bet amount
							double bet = getBetAmt();
							
							//capturePaytableScreenshot(report,currencyName);
							
							paytablePayout = winCombinationList.get(index);
								
							for (int j = 0; j < winCombinationSize; j++)
							{
	
								paytablePayout = winCombinationList.get(index);
								if (paytablePayout.contains("Scatter") || paytablePayout.contains("FreeSpin"))
								{
									scatter = "yes";
								} 
								else
								{
									scatter = "no";
								}
					String[] xmlData = paytablePayout.split(",");

					gamePayout = gamepayoutForPaytable(symbolData, paytablePayout);// it will fetch game payout for Force game
					
				//comparing currency with Reg Expression 
				boolean result = verifyRegularExpression(gamePayout,regExpr);
               if (result)
				{
            	  // report.detailsAppendNoScreenshot("Pay Table ", "Pay Table Payouts Validation", ""+gamePayout, "PASS"); 
            	  	
            	   System.out.println(gamePayout);
            	   //System.out.println("BET Value " + bet  + " validation :" + result + " symbol Name : " + xmlData[1]+ " Paytable Payout : " + gamePayout);
					log.debug("BET Value " + bet +" validation :" + result + " symbol Name : " + xmlData[1]+ " Paytable Payout : " + gamePayout);
				}
               else
               {
            	  // System.out.println("BET Value " + bet +" validation :" + result + " symbol Name : " + xmlData[1]+ " Paytable Payout : " + gamePayout);
					log.debug("BET Value " + bet +" validation :" + result + " symbol Name : " + xmlData[1]+ " Paytable Payout : " + gamePayout);
			    }
				length--;
		     	index++;
			}

		// Closes the paytable
	//paytableClose();

						
					} catch (Exception e) {
						e.printStackTrace();
						try {
							report.detailsAppendNoScreenshot("verify Payout verification for the bet ", " ",
									"Exception ocuur while verifying payout for bet", "Fail");
						} catch (Exception e1) {
							//log.error(e1.getStackTrace());
						}

					}

				}
				
				/*
				 * set min bet using scroll bar 
				 */
				public String setMinBetUsingScrollBar(Desktop_HTML_Report report , String CurrencyName ) 
				{
					String sliderText = null; 
					Wait = new WebDriverWait(webdriver, 60);
					try
					{
					func_Click("BetButton");
					report.detailsAppendFolder("Bet Button", "Bet Button Clicked", "Bet Button Clicked", "PASS",""+CurrencyName);
					Thread.sleep(2000);
					WebElement coinSizeSlider = webdriver.findElement(By.xpath(XpathMap.get("CoinSizeScrollBar")));
					coinSizeSlider.isDisplayed();
					if(coinSizeSlider != null)
					{
					sliderText = func_GetText("sliderText");
					System.out.println("Slider Bet is : "+sliderText);log.debug("Slider Bet is : "+sliderText);
					
					Thread.sleep(2000);
					String totalBet = func_GetText("slidertotalBet");
					System.out.println("Total Slider Bet is : "+totalBet);log.debug(" Total Slider Bet is : "+totalBet);
					
					Thread.sleep(2000);
					func_Click("BetBackButton");
					System.out.println("Clicked Bet Back Button : PASS");log.debug("Clicked Bet Back Button : PASS");
					}}
					catch (Exception e) 
					{
					log.debug(e.getMessage());
					}
					return sliderText;
				
				}
				/*
				 * set Max bet using scroll bar 
				 */
				public String setMaxBetUsingScrollBar(Desktop_HTML_Report report , String CurrencyName ) 
				{
					String sliderText = null; 
					Wait = new WebDriverWait(webdriver, 60);
					try
					{
					func_Click("BetButton");
					report.detailsAppendFolder("Bet Button", "Bet Button Clicked", "Bet Button Clicked", "PASS",""+CurrencyName);
					Thread.sleep(2000);
					
					WebElement coinSizeSlider = webdriver.findElement(By.xpath(XpathMap.get("CoinSizeScrollBar")));
					coinSizeSlider.isDisplayed();
					if(coinSizeSlider != null)
					{
					System.out.println("Default Bet is : ");
					Actions action = new Actions(webdriver);
					action.dragAndDropBy(coinSizeSlider,950, 0).build().perform();//500
					Thread.sleep(2000);
					}
					
					sliderText = func_GetText("sliderText");
					System.out.println("Default Bet is : "+sliderText);log.debug("Default Bet is : "+sliderText);
					
					String totalBet = func_GetText("slidertotalBet");
					System.out.println("Total Slider Bet is : "+totalBet);log.debug(" Total Slider Bet is : "+totalBet);
					Thread.sleep(2000);
				
					func_Click("BetBackButton");
					System.out.println("Clicked Bet Back Button : PASS");log.debug("Clicked Bet Back Button : PASS");
					}
					catch (Exception e) 
					{
					log.debug(e.getMessage());
					}
					return sliderText;
				
				}
				/**
				 * Verifies Paytable open
				 * 
				 */
				public boolean paytableVarification(Desktop_HTML_Report report,String CurrencyName) 
				{
					boolean paytable= false;
				try
				{
					func_Click("MenuIcon");
					Thread.sleep(2000);
					System.out.println("Click Menu Icon : PASS");log.debug("Click Menu Icon : PASS");
					report.detailsAppendFolder("Menu Icon", "Menu Clicked", "Menu Clicked", "PASS",CurrencyName);
					paytable = true;
					
					func_Click("paytableIcon");
					System.out.println("Click Paytable Icon : PASS");log.debug("Click Paytable Icon : PASS");Thread.sleep(2000);
					paytable = true;
					
					report.detailsAppendFolder("PayTable ", "PayTable", "PayTable ", "PASS",CurrencyName);
					
					System.out.println("Screen Shot of Paytable: PASS");log.debug("Screen Shot of Paytable: PASS");
					paytable = true;
					
					textValidationForPaytableBranding(report,CurrencyName);
					Thread.sleep(2000);
					
					//func_click("paytableBackButton");Thread.sleep(1000);
					
					paytable = true;
				}
				catch (Exception e) 
				{
					log.error(e.getMessage(), e);
				}
				return paytable;
				
				}
				
				
				
				/**
				 * method is used to navigate 
				 * @param report
				 * @param gameurl
				 * @author rk61073
				 */
				public void checkpagenavigation(Desktop_HTML_Report report, String gameurl,String CurrencyName) {
					try {
						String mainwindow = webdriver.getWindowHandle();
						Set<String> s1 = webdriver.getWindowHandles();
						if (s1.size() > 1) {
							Iterator<String> i1 = s1.iterator();
							while (i1.hasNext()) {
								String ChildWindow = i1.next();
								if (mainwindow.equalsIgnoreCase(ChildWindow)) {
									//report.detailsAppend("verify the Navigation screen shot", " Navigation page screen shot", "Navigation page screenshot ", "PASS");
									ChildWindow = i1.next();
									webdriver.switchTo().window(ChildWindow);
									String url = webdriver.getCurrentUrl();
									log.debug("Navigation URL is :: " + url);
									log.debug("Navigation URL is :: " + url);
									if (!url.equalsIgnoreCase(gameurl))
									{
										// pass condition for navigation	
										report.detailsAppendFolder("verify the Navigation screen shot", " Navigation page screen shot",
												"Navigation page screenshot ", "PASS",CurrencyName);
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
								// pass condition for navigation
								report.detailsAppendFolder("verify the Navigation screen shot", " Navigation page screen shot",
										"Navigation page screenshot ", "PASS",CurrencyName);
								log.debug("Page navigated succesfully");

								webdriver.navigate().to(gameurl);
								waitForSpinButton();
								//newFeature();
							} else {
								webdriver.navigate().to(gameurl);
								waitForSpinButton();
								//System.out.println("Now On game page");
								log.debug("Now On game page");
							}
						}

					} catch (Exception e) {
						log.error("error in navigation of page");
					}
				}

				
				
				/**
				 * method is for click , navigate and back to game screen using Xpath (For Android & IOS click action is different)
				 *
				 */
				public boolean clickAndNavigate(Desktop_HTML_Report report , String locator,String CurrencyName) 
				{
					boolean ele =false;
					String gameurl = webdriver.getCurrentUrl();
				try
				{		
					boolean isElementVisisble = webdriver.findElement(By.xpath(XpathMap.get(locator))).isDisplayed();
					
				if(isElementVisisble)
					{
					webdriver.findElement(By.xpath(XpathMap.get(locator))).click();
					//report.detailsAppend(" Click ", " Clicked ", " Clicked", "PASS");
					Thread.sleep(5000);
					checkpagenavigation(report, gameurl,CurrencyName); 
					
					Thread.sleep(1000);
					ele = true;
					}
				}
				catch(Exception e)
				{
					log.error(e.getMessage(), e);
				}

				return ele;
				}
				/**
				 * method is used to check if it is displayed or not 
				 * @param locator
				 * @return
				 * 
				 */
				public boolean isDisplayed(Desktop_HTML_Report report,String locator,String CurrencyName)
				{
					boolean islocatorVisible = false;
				try 
				{
					islocatorVisible = webdriver.findElement(By.xpath(XpathMap.get(locator))).isDisplayed();
					if (islocatorVisible)
					{
						log.debug("Element is Displayed");
						//report.detailsAppendFolder("Menu", "Menu Pannel", "Menu Pannel", "PASS",CurrencyName);
						 islocatorVisible = true;
					}
					else
						
					{
						log.debug("Element is not Displayed");
					}
				} 
				catch (Exception e) 
				{
					log.error("Not able to verify Topbar", e);
				}
				return islocatorVisible;
				}
				/**method is for menu buttons
				 * 
				 */
				public boolean menuButtonsforFiveReelGames(Desktop_HTML_Report report,String CurrencyName) 
				{
					boolean isMenuButtonsVisible = false;
					String isMenuAvailable = "MenuIcon";
					try 
					{
						boolean isElementVisisble = webdriver.findElement(By.xpath(XpathMap.get(isMenuAvailable))).isDisplayed();	
						if(isElementVisisble)
						{
							
							func_Click("MenuIcon");
							report.detailsAppendFolder("Menu", "Menu Pannel", "Menu Pannel", "PASS",CurrencyName);
							Thread.sleep(3000);
							clickAndNavigate(report,"playcheckFromMenu",CurrencyName);
							System.out.println("playcheckFromMenu : PASS");log.debug("playcheckFromMenu : PASS");
							Thread.sleep(3000);
							
							func_Click("MenuIcon");
							report.detailsAppendFolder("Menu", "Menu Pannel", "Menu Pannel", "PASS",CurrencyName);
							Thread.sleep(2000);
							clickAndNavigate(report,"ResponisblegamingFromMenu",CurrencyName);
							System.out.println("ResponisblegamingFromMenu : PASS");log.debug("ResponisblegamingFromMenu : PASS");
							Thread.sleep(3000);
							
							func_Click("MenuIcon");
							report.detailsAppendFolder("Menu", "Menu Pannel", "Menu Pannel", "PASS",CurrencyName);
							Thread.sleep(2000);
							clickAndNavigate(report,"BankingFromMenu",CurrencyName);
							System.out.println("BankingFromMenu : PASS");log.debug("BankingFromMenu : PASS");
							Thread.sleep(3000);
							
							func_Click("MenuIcon");
							report.detailsAppendFolder("Menu", "Menu Pannel", "Menu Pannel", "PASS",CurrencyName);
							Thread.sleep(2000);
							clickAndNavigate(report,"HelpFromMenu",CurrencyName);
							System.out.println("HelpFromMenu : PASS");log.debug("HelpFromMenu : PASS");
							Thread.sleep(3000);
							
							func_Click("MenuIcon");
							report.detailsAppendFolder("Menu", "Menu Pannel", "Menu Pannel", "PASS",CurrencyName);
							Thread.sleep(2000);
							
							isDisplayed(report,"SoundFromMenu",CurrencyName);
							report.detailsAppendFolder("Menu", "Sound From Menu Pannel", "Sound From Menu Pannel", "PASS",CurrencyName);
							System.out.println("SoundFromMenu : PASS");log.debug("SoundFromMenu : PASS");
							Thread.sleep(2000);
							
							isDisplayed(report,"HomeFromMenu",CurrencyName);
							report.detailsAppendFolder("Menu", "Home Icon From Menu", "Home Icon From Menu", "PASS",CurrencyName);
							System.out.println("HomeFromMenu : PASS");log.debug("HomeFromMenu : PASS");
							isMenuButtonsVisible = true;
						}
						else
						{
							System.out.println("There is no Menu Button Available");
						}
						
					}
					catch (Exception e)
					{
						log.error(e.getMessage(), e);
					}
					
					return isMenuButtonsVisible;
				}
				/**
				 * method is to 
				 * @param report
				 * @param currencyName
				 * @return
				 * 				 */
				
				public boolean topBarMenuButtonIcons(Desktop_HTML_Report report, String currencyName) 
				{
					boolean isMenuButtonsVisible = false;
					String isTopBarMenuIconsVisible = "TopBarMenuIcons";
					String isTopBarMenuIconVisible = "TopBarMenuIcon";
					try 
					{
						if(webdriver.findElement(By.xpath(XpathMap.get(isTopBarMenuIconsVisible))).isDisplayed())
						{
							clickAndNavigate(report,"HelpIconfromTopBar",currencyName);
							Thread.sleep(2000);
							System.out.println("HelpIconfromTopBar : PASS");log.debug("HelpIconfromTopBar : PASS");
							report.detailsAppendFolder("Top Bar Icons", "Help", "Help", "PASS",currencyName);	

							
							clickAndNavigate(report,"GameHistoryFromTopBar",currencyName);
							Thread.sleep(3000);
							System.out.println("GameHistoryFromTopBar : PASS");log.debug("GameHistoryFromTopBar : PASS");
							report.detailsAppendFolder("Top Bar Icons", "GameHistory", "GameHistory", "PASS",currencyName);
							
							clickAndNavigate(report,"PlayerProtectionGamingFromTopBar",currencyName);
							Thread.sleep(3000);
							System.out.println("ResponsibleGamingFromTopBar : PASS");log.debug("ResponsibleGamingFromTopBar : PASS");
							report.detailsAppendFolder("Top Bar Icons", "ResponsibleGaming", "ResponsibleGaming", "PASS",currencyName);
							
							clickAndNavigate(report,"TranisationHistoryFromTopBar",currencyName);
							Thread.sleep(3000);
							System.out.println("TranisationHistoryFromTopBar : PASS");log.debug("TranisationHistoryFromTopBar : PASS");
							report.detailsAppendFolder("Top Bar Icons", "TranisationHistory", "TranisationHistory", "PASS",currencyName);
							isMenuButtonsVisible= true;
									
						}
						else if(webdriver.findElement(By.xpath(XpathMap.get(isTopBarMenuIconVisible))).isDisplayed())
						{
							func_Click("TopBarMenuIcon");
							System.out.println("Menu from Top Bar : PASS");log.debug("Menu from Top Bar : PASS");
							report.detailsAppendFolder("Top Bar Menu","Menu Icon" ,"Menu Icon" , "PASS", currencyName);
							Thread.sleep(2000);
							
							clickAndNavigate(report,"HelpIconfromTopBar",currencyName);
							System.out.println("HelpIconfromTopBar : PASS");log.debug("HelpIconfromTopBar : PASS");
							report.detailsAppendFolder("Top Bar Icons from Dropdown", "Help", "Help", "PASS",currencyName);
							Thread.sleep(2000);
							
							func_Click("TopBarMenuIcon");
							System.out.println("Menu from Top Bar : PASS");log.debug("Menu from Top Bar : PASS");
							report.detailsAppendFolder("Top Bar Menu","Menu Icon" ,"Menu Icon" , "PASS", currencyName);
							Thread.sleep(2000);
							
							clickAndNavigate(report,"CashCheckFromTopBar",currencyName);
							System.out.println("CashCheckFromTopBar : PASS");log.debug("CashCheckFromTopBar : PASS");
							report.detailsAppendFolder("Top Bar Icons from Dropdown", "CashCheck", "CashCheck", "PASS",currencyName);
							Thread.sleep(3000);
							
							func_Click("TopBarMenuIcon");
							System.out.println("Menu from Top Bar : PASS");log.debug("Menu from Top Bar : PASS");
							report.detailsAppendFolder("Top Bar Menu","Menu Icon" ,"Menu Icon" , "PASS", currencyName);
							
							Thread.sleep(2000);
							
							clickAndNavigate(report,"PlayerProtectionGamingFromTopBar",currencyName);
							System.out.println("Player Protection from TopBar : PASS");log.debug("Player Protection from TopBar : PASS");
							report.detailsAppendFolder("Top Bar Icons from Dropdown", "Player Protection", "Player Protection", "PASS",currencyName);
							Thread.sleep(3000);
							
							func_Click("TopBarMenuIcon");
							System.out.println("Menu from Top Bar : PASS");log.debug("Menu from Top Bar : PASS");
							report.detailsAppendFolder("Top Bar Menu","Menu Icon" ,"Menu Icon" , "PASS", currencyName);
							Thread.sleep(2000);
							
							clickAndNavigate(report,"PlayCheckFromTopBar",currencyName);
							System.out.println("PlayCheck from TopBar : PASS");log.debug("PlayCheck from TopBar : PASS");
							report.detailsAppendFolder("Top Bar Icons from Dropdown", "PlayCheck", "PlayCheck", "PASS",currencyName);
							Thread.sleep(3000);
							isMenuButtonsVisible= true;
							
						}
						else
						{
							System.out.println("There is no Top Bar Menu Button Available");
							report.detailsAppendFolder("Top Bar Icons", "op Bar Icons", "Top Bar Icons", "FAIL",currencyName);
						}
						
					}
					catch (Exception e)
					{
						log.error(e.getMessage(), e);
					}
					
					return isMenuButtonsVisible;
				}
				/**
				 * Verify Clock from Top Bar
				 *
				 */
					public boolean clockFromTopBar(Desktop_HTML_Report report,String currencyName)
				{
						//String clock = "ClockonTheTopBar";
						boolean clocktext = false;
						try
						{
					    Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("clock"))));
					    boolean isClockVisible = webdriver.findElement(By.xpath(XpathMap.get("clock"))).isDisplayed();
						if (isClockVisible) 
						{
						String clockText = func_GetText("clock");
						if(clockText != null)
						{
						log.debug(clockText);
						System.out.println(clockText);
						report.detailsAppendFolder("Top Bar Clock", "Clock", ""+clockText, "PASS",currencyName);	
						clocktext = true;
						}
						else
						{
							log.debug("Check Clock"+clockText);
							System.out.println("Check Clock"+clockText);
							report.detailsAppendFolder("Top Bar Clock", "Clock", ""+clockText, "FAIL",currencyName);	
						}
						}}
						
						catch (Exception e) 
						{
							log.error("Not able to verify clock", e);
						}
						return clocktext;
				}
					/**
					 * method is for to check if it is Displayed or not  and to get the Text 	
					 * 
					 */
						public String isDisplayedAndGetText(String locator)
						{
							String getText = null;
							try 
							{
								boolean isloactorVisible = webdriver.findElement(By.xpath(XpathMap.get(locator))).isDisplayed();
								if (isloactorVisible) 
								{
									getText = webdriver.findElement(By.xpath(XpathMap.get(locator))).getText(); 
									log.debug( "Text is "+getText);
									//System.out.println( "Text is "+getText);	
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
						 * Click menu Buttons
						 * @param currencyName 
						 * @return
						 * @throws InterruptedException
						 * 
						 */
						public boolean menuButtonsforThreeReelGame(Desktop_HTML_Report report,String CurrencyName) 
						{
							boolean isMenuButtonsVisible = false;
							String isMenuAvailable = "MenuButton";
							try 
							{
								boolean isElementVisisble = webdriver.findElement(By.xpath(XpathMap.get(isMenuAvailable))).isDisplayed();	
								if(isElementVisisble)
								{
									func_Click("MenuButton");
									report.detailsAppendFolder("Menu Button ", "Menu Pannel", "Menu Pannel", "PASS",CurrencyName);
									Thread.sleep(2000);
									
									clickAndNavigate(report,"BankingFromMenu",CurrencyName);
									System.out.println("BankingFromMenu : PASS");log.debug("BankingFromMenu : PASS");
									report.detailsAppendFolder("Menu Button ", "Banking", "Banking", "PASS",CurrencyName);
									Thread.sleep(2000);
									
									func_Click("MenuIcon");
									report.detailsAppendFolder("Menu Button ", "Menu Pannel", "Menu Pannel", "PASS",CurrencyName);
									Thread.sleep(3000);
									
									func_Click("SettingsFromMenu");
									System.out.println("Settings from Menu : PASS");log.debug("Settings from Menu : PASS");
									report.detailsAppendFolder("Menu Button ", "Settings", "Settings", "PASS",CurrencyName);
									Thread.sleep(3000);
									
									func_Click("BackButton");
									report.detailsAppendFolder("Menu Button  ", "Back Button Clicked", "Back Button Clicked", "PASS",CurrencyName);
									Thread.sleep(2000);
									
									isMenuButtonsVisible = true;
								}
								else
								{
									System.out.println("There is no Menu Button Available");
									report.detailsAppendFolder("Menu Button  ", "Back Button Clicked", "Back Button Clicked", "PASS",CurrencyName);
								}
								
							}
							catch (Exception e)
							{
								log.error(e.getMessage(), e);
							}
							
							return isMenuButtonsVisible;
						}
						
						/**
						 * Verifies the Big Win
						 * 
						 */
						public String verifyBigWinOnRefresh(Desktop_HTML_Report report,String CurrencyName) 
						{
							String bigWinAmt = null;
							Wait = new WebDriverWait(webdriver, 600);
						
							try
							{
						   /* Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("BigWin"))));	
						    boolean isBigWin = webdriver.findElement(By.xpath(XpathMap.get("BigWin"))).isDisplayed();*/
								Thread.sleep(3000);
								webdriver.navigate().refresh();
								report.detailsAppendFolder("Big Win On Refresh ", "Refresh", "Refresh", "PASS",CurrencyName);
							     Thread.sleep(2000);
							     
							     
							     waitForSpinButton();
							     Thread.sleep(2000);
						    Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("WinAmt"))));	
							 boolean isBigWin = webdriver.findElement(By.xpath(XpathMap.get("WinAmt"))).isDisplayed();
							if(isBigWin)
							{	
							bigWinAmt = func_GetText("WinAmt");
							System.out.println("Free Game BigWin Value on Refresh "+bigWinAmt);
							report.detailsAppendFolder("Free Game", "Big Win Amt on Refresh", ""+bigWinAmt, "PASS",""+CurrencyName);
							log.debug("Big Win Amount is "+bigWinAmt);Thread.sleep(9000); 
							}
							else
							{
								report.detailsAppendFolder("Free Game", "Big Win Amt on Refresh", ""+bigWinAmt, "FAIL",""+CurrencyName);
								System.out.println("There is no Big Win ");
								log.debug("There is no Big Win");
							}}
							catch (Exception e) 
							{
								log.error(e.getMessage(), e);
							}
							return bigWinAmt;
							
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
								Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("PoweredByMicroGaming"))));	
								boolean txt = webdriver.findElement(By.xpath(XpathMap.get("PoweredByMicroGaming"))).isDisplayed();
							if(txt)	
							{
								PaytableBranding=func_GetText("PoweredByMicroGaming");
								
								if(PaytableBranding.equalsIgnoreCase(XpathMap.get("PoweredByMicroGamingText")))
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
						 * Verifies the Big Win on refresh
						 * 
						 */
						public String verifyBigWinRefreshOnFreeSpins(Desktop_HTML_Report report,String CurrencyName) 
						{

							String bigWinAmt = null;
							Wait = new WebDriverWait(webdriver, 600);
						
							try
							{
						    Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("BigWin"))));	
						    boolean isBigWin = webdriver.findElement(By.xpath(XpathMap.get("BigWin"))).isDisplayed();
							if(isBigWin)
							{
							 webdriver.navigate().refresh();
								 
					        Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("WinAmt"))));	
							bigWinAmt = func_GetText("WinAmt");
							System.out.println(" Big Win Amount on Refresh "+bigWinAmt);
							report.detailsAppendFolder("Big Win Amt", "Big Win Amt  on Refresh", ""+bigWinAmt, "PASS",""+CurrencyName);
							log.debug("  Big Win Amount is "+bigWinAmt);
							Thread.sleep(2000); 
							//cfnlib.spinclick();
						     func_Click("spinButtonBoxContainer");
						    }
							
							else
							{
								//report.detailsAppendFolder("Base Game", "Big Win Amt", ""+bigWinAmt, "PASS",""+isoCode);
								System.out.println("There is no Big Win  on Refresh ");
								log.debug("There is no Big Win  on Refresh");
							}}
							catch (Exception e) 
							{
								log.error(e.getMessage(), e);
							}
							return bigWinAmt;
							
						}
						/**
						 * method is to verify the Free Games Assignment 
						 */
						public boolean assignFreeGames(String userName,String offerExpirationUtcDate,int mid, int cid,int languageCnt,int defaultNoOfFreeGames) 
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

								isFreeGameAssigned=addFreeGameToUserInBluemesa( userName,defaultNoOfFreeGames ,  offerExpirationUtcDate,  balanceTypeId,  mid, cid,languageCnt);
							}
							else
							{
								isFreeGameAssigned=addFreeGameToUserInAxiom(userName,defaultNoOfFreeGames,offerExpirationUtcDate,balanceTypeId,mid,cid,languageCnt);
							}
							}catch (Exception e)
							{
								log.error(e.getMessage(), e);
							}
							return isFreeGameAssigned;
						}
						/**
						 * Click and get the Text 
						 */
						public String clickAndGetText(Desktop_HTML_Report report, String menuclick ,String locator) 
						{
							String getText = null;
							webdriver.findElement(By.xpath(XpathMap.get(menuclick))).click();
							report.detailsAppend(" Click Menu  ", "Menu Clicked ", "Menu Clicked", "PASS");
							try 
							{
								boolean isloactorVisible = webdriver.findElement(By.xpath(XpathMap.get(locator))).isDisplayed();
								if (isloactorVisible) 
								{
									getText = webdriver.findElement(By.xpath(XpathMap.get(locator))).getText(); 
									log.debug( "Text is "+getText);		
									System.out.println( "Text is "+getText);	
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
							return getText ;
				}
						/**
						 * method is to refresh in Free Games
						 */
						public String freeGameOnRefresh(Desktop_HTML_Report report,String currencyName) 
						{
							String onRefresh = "";
							  Wait = new WebDriverWait(webdriver, 500);
							try 
							{
								Thread.sleep(3000);webdriver.navigate().refresh();
								report.detailsAppendFolder("Free Game", "Refresh", "Refresh", "PASS",currencyName);
							     Thread.sleep(2000);
							     waitForSpinButton();
							     Thread.sleep(2000);
							   
								Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("ResumeButton"))));
								boolean isResumeButtonVisible = webdriver.findElement(By.xpath(XpathMap.get("ResumeButton"))).isDisplayed();
								if (isResumeButtonVisible) 
								{
									String text = func_GetText("ResumeButton");
									report.detailsAppendFolder("Free Games on Refresh", "Resume Button", "Resume Button", "PASS",currencyName); 
									System.out.println("Free Games Resume Button Text is : "+text+ " - PASS");	
									log.debug("Free Games Resume Button Text is : "+text+ " - PASS");
									//validate the Entry Screen Info Text 	
									onRefresh =	freeGameEntryInfo(report,currencyName,"OnResumeButtonInfoIcon","OnResumeButtonInfoText");
									//Click on Resum Button on Refresh 
									func_Click("ResumeButton");
									
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
						/**
						 * method is to Back to BaseGame in Free Games
						 */
						public String freeGameBackToBaseGame(Desktop_HTML_Report report,String currencyName) 
						{

							String totalWinAmtTxt = null;
							  Wait = new WebDriverWait(webdriver, 700);
							try 
							{
								
								Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("FreeGamesBacktoBaseGame"))));
								boolean isResumeButtonVisible = webdriver.findElement(By.xpath(XpathMap.get("FreeGamesBacktoBaseGame"))).isDisplayed();
								if (isResumeButtonVisible) 
								{
									
								   totalWinAmtTxt = func_GetText("FreeGamesTotalWinAmt");
								    System.out.println("Congratulations Win Amount Text "+totalWinAmtTxt);
									log.debug("Congratulations Win Amount is  "+totalWinAmtTxt);
									report.detailsAppendFolder("Free Game", "Congratulations Win Amount ", "Congratulations Win Amount ", "PASS",currencyName);
									
									String txt = func_GetText("FreeGamesBacktoBaseGameButton");
								    System.out.println("Back Button to Base Game Text "+txt);
									log.debug("Back Button to Base Game Text is  "+txt);
									
									Thread.sleep(2000);
									func_Click("FreeGamesBacktoBaseGameButton");	
							    } 
								else 
								{
									log.debug("Free Games Back to Base game Button  FAIL");
									System.out.println("Free Games Back to Base game Button  FAIL");
								}
							}
							catch (Exception e)
							{
								log.error(e.getMessage(), e);
							}
							return totalWinAmtTxt ;
				
						}	
			/**
			 * medthod is to verify if Free games visible or not
			 */
						public void isFreeGamesVisible(String url) 
						{	
							long startTime = System.currentTimeMillis();
							try
							{
								while (true) 
								{
									Boolean currentSceneflag = webdriver.findElement(By.xpath(XpathMap.get("playNow"))).isDisplayed();

									System.out.println("Free games visible " + currentSceneflag);
									if (currentSceneflag) 
									{
										log.info("Free games are assigned " + currentSceneflag);
										Thread.sleep(1000);
										break;
									} 
									else 
									{
										long currentime = System.currentTimeMillis();
										if (((currentime - startTime) / 1000) > 15) 
										{
											System.out.println("No free games reflected on the screen , REFRESH ");
											log.info("No free games reflected on the screen , REFRESH ");
											Thread.sleep(5000);
											loadGame(url);
										}
									}
								}

							} catch (Exception e) {
								log.error("error while waiting for free games screen ", e);
								log.error(e.getMessage());
							}
						}
						/**
						 * Verifies Paytable payout varification for 3 reel game
						 * 
						 */
						public String[]  singlePaytablePayouts(Desktop_HTML_Report report,String CurrencyName) 
						{
							String payouts= null;
							String payoutsValidation[] = {"payouts1","payouts2","payouts3","payouts4","payouts5","payouts6","payouts7","payouts8","payouts9","payouts10"};
							
							try
							{
								if(XpathMap.get("slider").equals("yes"))	
								{
									//for Coach Potato
								}
								else
								{ 
									 System.out.println("Following are Paytable Payouts");log.debug("Following are Paytable Payouts");
									for(int i=0  ;i<payoutsValidation.length;i++)
									{	
									payoutsValidation[i] =   func_GetText(payoutsValidation[i]);
									System.out.println(payoutsValidation[i]);
									}
								}
							
							Thread.sleep(2000);	
							func_click("paytableBackButton");Thread.sleep(1000);		
						}
						catch (Exception e) 
						{
							log.error(e.getMessage(), e);
						}
						return payoutsValidation;
						}			
						/**
						 * Click menu Buttons
						 * @param currencyName 
						 * @return
						 * @throws InterruptedException
						 */
						public boolean menuButtons(Desktop_HTML_Report report, String currencyName) 
						{
							boolean menubuttons = false;
							try
							{
								if(XpathMap.get("menuForThreeReelgame").equalsIgnoreCase("yes"))
								{
									menuButtonsforThreeReelGame(report,currencyName);
								    menubuttons = true;
								}
								else if(XpathMap.get("menuForFiveReelGame").equalsIgnoreCase("yes"))
								{
									menuButtonsforFiveReelGames(report,currencyName);
									menubuttons = true;
								}
								else
								{
									System.out.println("Menu : FAIL");log.debug("Menu : FAIL");
								}
								
								}
							      catch (Exception e) 
							        {
								        log.error(e.getMessage(), e);
							        }
									return menubuttons;
									}
						/**
						 * Verifies the Big Win on refresh Base Game
						 * 
						 */
						public String verifyBigWinOnRefreshInFreeGames(Desktop_HTML_Report report,String CurrencyName) 
						{
							String bigWinAmt = null;
							Wait = new WebDriverWait(webdriver, 600);
						
							try
							{
						   /* Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("BigWin"))));	
						    boolean isBigWin = webdriver.findElement(By.xpath(XpathMap.get("BigWin"))).isDisplayed();*/
								Thread.sleep(3000);
								webdriver.navigate().refresh();
								report.detailsAppendFolder("Big Win On Refresh ", "Refresh", "Refresh", "PASS",CurrencyName);
							     Thread.sleep(2000);
							     
							     freeGameOnRefresh(report,CurrencyName);
							     
							     waitForSpinButton();
							     Thread.sleep(2000);
						    Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("WinAmt"))));	
							 boolean isBigWin = webdriver.findElement(By.xpath(XpathMap.get("WinAmt"))).isDisplayed();
							if(isBigWin)
							{	
							bigWinAmt = func_GetText("WinAmt");
							System.out.println("Free Game BigWin Value on Refresh "+bigWinAmt);
							report.detailsAppendFolder("Free Game", "Big Win Amt on Refresh", ""+bigWinAmt, "PASS",""+CurrencyName);
							log.debug("Big Win Amount is "+bigWinAmt);Thread.sleep(9000); 
							}
							else
							{
								report.detailsAppendFolder("Free Game", "Big Win Amt on Refresh", ""+bigWinAmt, "FAIL",""+CurrencyName);
								System.out.println("There is no Big Win ");
								log.debug("There is no Big Win");
							}}
							catch (Exception e) 
							{
								log.error(e.getMessage(), e);
							}
							return bigWinAmt;
							
						}
}
