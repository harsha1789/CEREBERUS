package Modules.Regression.FunctionLibrary;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

//import com.sun.glass.events.KeyEvent;
import com.zensar.automation.framework.model.Coordinates;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.framework.utils.Util;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.TestPropReader;
import com.zensar.automation.model.Symbol;
import com.zensar.automation.model.WinCombination;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.offset.PointOption;
import net.lightbody.bmp.BrowserMobProxyServer;

public class CFNLibrary_Mobile_Force extends CFNLibrary_Mobile {
	boolean isFreeSpinTriggered = false;
	String forceNamespace = null;

	Logger log = Logger.getLogger(CFNLibrary_Mobile_Force.class.getName());

	public CFNLibrary_Mobile_Force(AppiumDriver<WebElement> webdriver, BrowserMobProxyServer proxy,
			Mobile_HTML_Report tc06, String gameName) throws IOException {
		super(webdriver, proxy, tc06, gameName);

		this.webdriver = webdriver;
		this.proxy = proxy;
		repo1 = tc06;
		// webdriver.manage().timeouts().implicitlyWait(5000, TimeUnit.SECONDS);
		Wait = new WebDriverWait(webdriver, 5000);

	}

	@Override
	public void verifyStopLanguage(Mobile_HTML_Report language, String languageCode) {
		try {
			clickAtButton("return " + xpathMap.get("ClickSpinBtn"));
			language.detailsAppendFolder("verify the Stop button translation",
					"Stop button should translate as per respective language", "Stop button is displaying", "Pass",
					languageCode);
			log.debug("clicked  on spin button and took the screenshot");
			log.debug("Waiting for Spin button to visible");

			elementWait("return " + xpathMap.get("SpinBtnCurrState"));
		} catch (Exception e) {
			log.error("error in verifyStop()", e);
		}
	}

	/*
	 * Date: 07/01/2019 Author:Sneha Jawarkar Description: Freegame_GTR Parameter:
	 * NA
	 */
	@Override
	public void quickspinwithBasegameFreespin() throws InterruptedException {
		// QuickSpinclick();//quick spin may not available in mobile
		spinclick();
		Thread.sleep(30000);
		try {
			if (freeSpinEntryScene()) {
				boolean b = verifyQuickSpinStatus();
				if (b)
					log.debug("quickspin is applicable during freespins");
				else
					log.debug("quickspin is not applicable during freespins");
			}
			waitForSpinButton();
			if (isBaseScene()) {
				spinclick();
				waitForSpinButtonstop();
				boolean b = verifyQuickSpinStatus();
				if (b)
					log.debug("quickspin is applicable during Normalspin");
				else
					log.debug("quickspin is not applicable during Normalspins");
			}
			log.debug("verify quickspin_with_Basegame_freespin");
		} catch (Exception e) {
			log.error("Error to verify quickspin_with_Basegame_freespin");
		}
	}

	@Override
	public void close_Autoplay() {
		try {
			if (!GameName.contains("Scratch")) {
				if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame"))) {
					boolean flag = getConsoleBooleanText("return " + xpathMap.get("isSpinBackBtnVisible"));
					if (flag) {
						clickAtButton("return " + xpathMap.get("ClickSpinBackBtn"));
						log.debug("Clicked close autoplay button");
					}
				} else {
					closeOverlay();
				}
			} else {
				settingsBack();
				log.debug("Closed the Autoplay setting");
			}

		} catch (Exception e) {
			e.getMessage();
		}
	}

	/* Sneha Jawarkar: Wait for Spin button */
	@Override
	public boolean waitForSpinButtonstop() {
		try {
			log.debug("Waiting for spinbutton active ");
			while (true) {
				elementWait("return " + xpathMap.get("SpinBtnCurrState"), "active");

				return true;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Date: 21/6/2019 Author: Sneha Jawarkar Description: GTR Reelspin
	 * 
	 * @throws InterruptedException
	 */
	@Override
	public long reelspinSpeedDuringAutoplay() {
		long autoplayloadingTime = 0;
		long autoplaysum = 0;
		boolean b = ISAutoplayAvailable();
		if (b) {
			clickAtButton("return " + forceNamespace
					+ ".getControlById('AutoplayOptionsComponent').onButtonClicked('autoplayMoreOptionsButton')");
			// set Autoplay at 10
			clickAtButton("return " + forceNamespace
					+ ".getControlById('AutoplayPanelComponent').numberSpinsSliderEventReceived({'eventName': 'CLICK', 'value': '0' })");
			// start autoplay
			long start = System.currentTimeMillis();
			clickAtButton("return " + forceNamespace
					+ ".getControlById('AutoplayPanelComponent').onButtonClicked('autoplayStartButton')");
			waitForSpinButtonstop();
			long finish = System.currentTimeMillis();
			long totalTime = finish - start;
			autoplayloadingTime = totalTime / 1000;
			autoplaysum = autoplaysum + autoplayloadingTime;
			log.debug("Calculation for the quickspin duration is running properly");
			log.debug("Total quickspin duraion in seconds for the 10 Autoplay spins = " + autoplaysum + "Sec");

		} else {
			log.error("Error during quick spin average time calculation");
		}
		return autoplaysum;

	}

	/**
	 * Author:Sneha Jawarkar This method is used for verifying the status of the
	 * quick spin
	 * 
	 * @return true
	 */
	public void quickspinOff() {
		try {
			String qsCurrectState = GetConsoleText("return " + xpathMap.get("QuickSpinBtnState"));
			if (qsCurrectState.equalsIgnoreCase("active")) {
				clickAtButton("return " + xpathMap.get("QuickSpinBtn"));
				String qsCurrectStateOff = GetConsoleText("return " + xpathMap.get("QuickSpinBtnState"));
				if (qsCurrectStateOff.equalsIgnoreCase("activeSecondary"))
					log.debug("Quickspin is off Succesfully ");
			} else {
				log.error("Quickspin is not Active ");
			}
		} catch (Exception e) {
			log.error("Quickspin Not able to off", e);
		}
	}

	/**
	 * Date: 21/6/2019 Author: Sneha Jawarkar Description: GTR Reelspin
	 * 
	 * @throws InterruptedException
	 */
	public long reelSpinSpeedDuringQuickspin() throws InterruptedException {
		long Avgquickspinduration = 0;
		long quickspinloadingTime = 0;
		long quickspinsum = 0;

		
		boolean b = clickOnQuickSpin();
		if(b){
			for(int i=0; i<5;i++)
			{

				long start = System.currentTimeMillis();
				spinclick();
				waitForSpinButtonstop();
				long finish = System.currentTimeMillis();
				long totalTime = finish - start;
				quickspinloadingTime = totalTime / 1000;
				quickspinsum = quickspinsum + quickspinloadingTime;
				log.debug("Calculation for the quickspin duration is running properly");

			}
			log.debug("Total quickspin duraion in seconds for the 5 spins = " + quickspinsum + "Sec");
			Avgquickspinduration = quickspinsum / 5;
			log.debug("Average time in seconds between 5 quickspins is = " + Avgquickspinduration + " Sec");
		} else {
			log.error("Error during quick spin average time calculation");
		}
		quickspinOff();
		return Avgquickspinduration;
	}

	/**
	 * Date: 26/6/2019 Author: Sneha Jawarkar Description: GTR Reelspin
	 * 
	 * @throws Exception
	 */
	public void reelspinInAll() throws Exception {
		log.debug("Reelspin in Normal_Started");
		reelSpinSpeedDurationNormal();
		log.debug("Reelspin in Normal_End");
		log.debug("Reelspin with quickspin Started");
		reelSpinSpeedDuringQuickspin();
		log.debug("Reelspin with quickspin End");
		log.debug("Reelspin with Autoplay Started");
		reelspinSpeedDuringAutoplay();
		log.debug("Reelspin with Autoplay End");

	}

	/**
	 * Date: 21/6/2019 Author: Sneha Jawarkar Description: GTR Reelspin
	 * 
	 * @throws InterruptedException
	 */
	@Override
	public void reelSpinDifferecneDurationNormalspinQuickspin() throws InterruptedException {
		long Avgquickspinduration = 0;
		long Avgnormalspinduration = 0;
		reelSpinSpeedDuringQuickspin();

		int normalspinduration = (int) Avgnormalspinduration;
		int quickspinduration = (int) Avgquickspinduration;
		if (normalspinduration > quickspinduration) {
			log.debug("Normalspin duration is gretter than quickspin");
		} else {
			log.error("Quick spin duration is gretter than NormalSpin");
		}
	}

	/**
	 * Date: 21/6/2019 Author: Sneha Jawarkar Description: GTR Reelspin
	 * 
	 * @throws InterruptedException
	 */
	@Override
	public long reelSpinSpeedDurationNormal() throws InterruptedException {
		long normalloadingTime = 0;
		long normalsum = 0;
		long avgNormalSpinDuration = 0;

		for (int i = 0; i < 5; i++) {
			long start = System.currentTimeMillis();
			spinclick();
			waitForSpinButtonstop();
			long finish = System.currentTimeMillis();
			long totalTime = finish - start;
			normalloadingTime = totalTime / 1000;
			normalsum = normalsum + normalloadingTime;
			log.debug("Calculation for the spin duration is running properly");
		}
		log.debug("error while spin duration");
		avgNormalSpinDuration = normalsum / 5;
		return avgNormalSpinDuration;
	}

	/*
	 * Date: 16/05/2019 Author:Sneha Jawarkar Description: Freegame_GTR Parameter:
	 * NA
	 */
	@Override
	public void reelspinDurationDuringWinLoss() throws InterruptedException {// spin with nowin/loss
		long lossstart = System.currentTimeMillis();
		spinclick();
		waitForSpinButtonstop();
		long lossfinish = System.currentTimeMillis();
		long losstotalTime = lossfinish - lossstart;
		// spin with win
		long winstart = System.currentTimeMillis();
		spinclick();
		waitForSpinButtonstop();
		long winfinish = System.currentTimeMillis();
		spinclick();
		waitForSpinButtonstop();
	}

	/**
	 * Author:Sneha Jawarkar This method is used for verifying the status of the
	 * quick spin
	 * 
	 * @return true
	 */
	public boolean verifyQuickSpinStatus() {
		try {
			String quickSpincurrState = GetConsoleText("return " + xpathMap.get("QuickSpinBtnState"));

			if (quickSpincurrState.equalsIgnoreCase("active")) {
				log.debug("Quickspin is Active");
				return true;
			} else {
				log.error(" Quickspin is not Active");
				return false;
			}
		} catch (Exception e) {
			log.error("Not able to verify quickspin status", e);
		}
		return false;
	}

	/**
	 * This method is used to wait till Base scene loads Author: Sneha Jawarkar
	 */
	public boolean isBaseScene() {
		try {
			Thread.sleep(10000);
			String currentScene = GetConsoleText("return " + xpathMap.get("currentScene"));
			if (currentScene.equalsIgnoreCase("SLOT") || currentScene.equalsIgnoreCase("BASE"))
				log.debug("Basescene is available");
			return true;

		} catch (Exception e) {
			log.error("error while waiting for Base scene", e);
			return false;
		}
	}

	/**
	 * This method is used to wait till FS scene loads Author: Sneha Jawarkar
	 */
	@Override
	public boolean freeSpinEntryScene() {
		try {
			log.debug("After refreshing,waiting for free spin's scene to come");
			Thread.sleep(10000);
			String currentScene = GetConsoleText("return " + xpathMap.get("currentScene"));
			if (currentScene.equalsIgnoreCase("FREESPINS"))
				return true;

		} catch (Exception e) {
			log.error("error while waiting for free spin scene", e);
			return false;
		}
		return false;
	}

	/*
	 * Date: 16/05/2019 Author:Sneha Jawarkar Description: Freegame_GTR Parameter:
	 * NA
	 */
	@Override
	public boolean completeFreeGameOfferFreespin(int freegamescount) {
		try {
			for (int i = 0; i < freegamescount; i++) {
				spinclick();
				Thread.sleep(10000);
				if (freeSpinEntryScene()) {
					Thread.sleep(10000);
					webdriver.navigate().refresh();
					Thread.sleep(10000);
					clickFreegameResumePlayButton();
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
	 * Date: 16/05/2019 Author:Sneha Jawarkar Description: Freegame_GTR Parameter:
	 * NA
	 */
	@Override
	public boolean completeFreeGameOffer(int freegamescount) {
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
	 * * Date: 14/05/2019 Author: Sneha Jawarkar. Description: This function is used
	 * in GTR_freegame Parameter: play letter
	 */
	@Override
	public boolean clickOnPlayLater() {
		Wait = new WebDriverWait(webdriver, 500);
		boolean b = false;
		try {
			JavascriptExecutor js = (webdriver);
			Coordinates coordinateObj = new Coordinates();
			String align = "return " + xpathMap.get("PlayLaterButtonAlignment");
			typeCasting("return " + forceNamespace + ".getControlById('FreeGamesOffersView.playLaterButton').x",
					coordinateObj);
			coordinateObj.setX(coordinateObj.getX());
			typeCasting("return " + forceNamespace + ".getControlById('FreeGamesOffersView.playLaterButton').y",
					coordinateObj);
			coordinateObj.setY(coordinateObj.getX());
			typeCasting("return " + forceNamespace + ".getControlById('FreeGamesOffersView.playLaterButton').height",
					coordinateObj);
			coordinateObj.setHeight(coordinateObj.getX());
			typeCasting("return " + forceNamespace + ".getControlById('FreeGamesOffersView.playLaterButton').width",
					coordinateObj);
			coordinateObj.setWidth(coordinateObj.getX());
			coordinateObj.setAlign((js.executeScript(align)).toString());
			getComponentCenterCoordinates(coordinateObj);
			clickAtCoordinates(coordinateObj.getX(), coordinateObj.getY());
			Thread.sleep(100);
			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;

	}

	/**
	 * Date:15/5/19 Author:Sneha Jawarkar GTR_Freegame purpose
	 */

	@Override
	public void backtogameCenterclick() {
		try {
			clickAtButton(
					"return " + forceNamespace + ".getControlById('FreeGamesCompleteView.backToGameCenterButton')");
			log.debug("Clicked at back to game");
		} catch (Exception e) {
			log.error("Can not clicked on Back to Button", e);
		}
	}

	/**
	 * * Date: 17/05/2019 Author: Sneha Jawarkar. Description: This function is used
	 * in GTR_freegame Parameter: resume play button
	 */
	@Override
	public boolean clickFreegameResumePlayButton() {
		boolean b = false;
		try {
			JavascriptExecutor js = (webdriver);
			Coordinates coordinateObj = new Coordinates();
			String align = "return " + forceNamespace
					+ ".getControlById('FreeGamesComponent').views.freeGamesResumeView.Buttons.resumeButton.label.currentStyle.alignment";
			typeCasting("return " + forceNamespace + ".getControlById('FreeGamesResumeView.resumeButton').x",
					coordinateObj);
			coordinateObj.setX(coordinateObj.getX());
			typeCasting("return " + forceNamespace + ".getControlById('FreeGamesResumeView.resumeButton').y",
					coordinateObj);
			coordinateObj.setY(coordinateObj.getX());
			typeCasting("return " + forceNamespace + ".getControlById('FreeGamesResumeView.resumeButton').height",
					coordinateObj);
			coordinateObj.setHeight(coordinateObj.getX());
			typeCasting("return " + forceNamespace + ".getControlById('FreeGamesResumeView.resumeButton').width",
					coordinateObj);
			coordinateObj.setWidth(coordinateObj.getX());
			coordinateObj.setAlign((js.executeScript(align)).toString());
			getComponentCenterCoordinates(coordinateObj);
			clickAtCoordinates(coordinateObj.getCenterX(), coordinateObj.getCenterY());
			Thread.sleep(2000);
			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	/**
	 * * Date: 14/05/2019 Author: Sneha Jawarkar. Description: This function is used
	 * in GTR_freegame Parameter: play letter
	 */
	@Override
	public void clickBaseSceneDiscardButton() {
		try {
			clickAtButton("return " + xpathMap.get("BaseSceneDiscardBtn"));
			System.out.println("Clicked on basescene Discard Button");
		} catch (Exception e) {
			System.out.println("Can not Clicked on Basescene Discard Button");
		}
	}

	/**
	 * Date: 21/6/2019 Author: Sneha Jawarkar Description: GTR Reelspin
	 * 
	 * @throws InterruptedException
	 */
	@Override
	public long reelSpinspeedDuration() throws InterruptedException {
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

	public void getComponentCenterCoordinates(Coordinates coordinateObj) {
		try {
			long centerX = 0, centerY = 0;
			long middleHeight = coordinateObj.getHeight() / 2;
			long middleWidth = coordinateObj.getWidth() / 2;
			long x = coordinateObj.getX();
			long y = coordinateObj.getY();
			String align = coordinateObj.getAlign();
			System.out.println("x " + x);
			System.out.println("y " + y);
			if (align.equals("BOTTOM_RIGHT")) {
				centerX = x - middleWidth;
				centerY = y - middleHeight;
			} else if (align.equals("BOTTOM_LEFT")) {
				centerX = x + middleWidth;
				centerY = y - middleHeight;
			} else if (align.equals("BOTTOM_CENTER")) {
				centerX = x;
				centerY = y - middleHeight;
			} else if (align.equals("TOP_LEFT")) {
				centerX = x + middleWidth;
				centerY = y + middleHeight;
			} else if (align.equals("TOP_CENTER")) {
				centerX = x - middleWidth;
				centerY = y - middleHeight;
			}

			else if (align.equals("LEFT_CENTER")) {
				centerX = x - middleWidth;
				centerY = y - middleHeight;
			} else if (align.equals("RIGHT_CENTER")) {
				centerX = x - middleWidth;
				centerY = y - middleHeight;
			} else if (align.equals("CENTER")) {
				centerX = x;
				centerY = y;
			}

			coordinateObj.setCenterX(centerX);
			coordinateObj.setCenterY(centerY);
			System.out.println("centerx " + centerX);
			System.out.println("centerY " + centerY);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Override
	public void clickAtButton(String button) {
		JavascriptExecutor js = (webdriver);
		js.executeScript(button);
	}
	/*
	 * @Override public void newFeature() { try { //
	 * webdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	 * if(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame"))||
	 * (Constant.YES.equalsIgnoreCase(xpathMap.get("continueBtnOnGameLoad")))){ //
	 * Click on new feature screen with xpath for the game likes immortal romance
	 * Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.
	 * get("OneDesign_NewFeature_ClickToContinue"))));
	 * webdriver.findElement(By.xpath(xpathMap.get(
	 * "OneDesign_NewFeature_ClickToContinue"))).click();
	 * log.debug("Clicked on continue button present on new feature screen"); }
	 * 
	 * // click on new feature screen with hooks for games like dragon dance else{
	 * Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.
	 * get("clock")))); clickAtButton("return "+forceNamespace+
	 * ".getControlById('BaseIntroComponent').onButtonClicked('baseIntroTapToContinueButton')"
	 * ); log.debug("Clicked on intro screen of game"); }
	 * 
	 * } catch(Exception e) {
	 * log.info("exception in new feature,Seems no feature screen present",e); } }
	 */

	@Override
	public boolean open_Autoplay() {
		boolean autoplay = false;
		try {
			if (!GameName.contains("Scratch")) {
				if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame"))) {
					boolean autoplayflag = getConsoleBooleanText("return " + xpathMap.get("isAutoPlayBtnVisible"));
					if (autoplayflag) {
						clickAtButton("return " + xpathMap.get("ClickAutoPlayBtn"));
						log.debug("Clicked close autoplay button");
					}
				} else {
					// For altimate console
					if (Constant.FORCE_ULTIMATE_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame"))) {

						clickAtButton("return " + xpathMap.get("ClickAutoPlayBtn"));
					} else {
						// for other games
						clickAtButton("return " + xpathMap.get("ClickAutoplayOpenMobileBtn"));

						clickAtButton("return " + xpathMap.get("ClickAutoPlayMoreOptionsBtn"));
					}
					log.debug("Clicked on Autoplay button");
				}
				Thread.sleep(2000);
				autoplay = true;
			} else {
				clickAtButton("return " + xpathMap.get("ClickAutoplayOpenMobileBtn"));

				clickAtButton("return " + xpathMap.get("ClickAutoPlayMoreOptionsBtn"));

				log.debug("Clicked on Autoplay button");
			}
		} catch (Exception e) {
			log.error("error in open_Autoplay()", e);
			// autoplay=false;
		}
		return autoplay;

	}

	@Override
	public boolean openTotalBet() {
		boolean isBetOpen = false;
		try {
			clickAtButton("return " + xpathMap.get("ClickBetIconBtn"));
			log.debug("Clicked on TotalBet button");
			isBetOpen = true;
			Thread.sleep(2000);
		} catch (Exception e) {
			log.error("error in open_TotalBet()", e);
		}
		return isBetOpen;
	}

	/*
	 * Date: 15/05/2019 Author:Snehal Gaikwad Description: This method is to Check
	 * whether autoplay is available Parameter: NA
	 */

	@Override
	public boolean ISAutoplayAvailable() {
		boolean autoplay = false;
		try {
			GetConsoleText("return " + xpathMap.get("ClickAutoplayOpenMobileBtn"));
			autoplay = true;
		} catch (Exception e) {
			log.error("Autoplay id button is not visible", e);
			autoplay = false;
		}
		return autoplay;
	}

	/*
	 * Date: 15/05/2019 Description:To verify Autoplay with quick spin on Parameter:
	 * NA
	 * 
	 * @return boolean
	 */

	@Override
	public boolean autoPlayWithQSOn() {

		boolean qS_Test = false;
		try {
			clickAtButton("return " + xpathMap.get("QuickSpinBtn"));
			Thread.sleep(100);
			String QS_currectState = GetConsoleText("return " + xpathMap.get("QuickSpinBtnState"));
			if (QS_currectState.equalsIgnoreCase("active")) {
				clickAtButton("return " + xpathMap.get("ClickAutoPlayMoreOptionsBtn"));
				qS_Test = true;
				log.debug("Clicked on autoplay");
			} else {
				qS_Test = false;
			}

		} catch (Exception e)

		{
			log.error("Autoplay is  not clickable with Quick Spin on", e);
		}
		return qS_Test;

	}
	/*
	 * Date: 15/05/2019 Description:To check Autoplay after free spin Parameter: NA
	 * 
	 * @return boolean
	 */

	@Override
	public boolean isAutoplayWithFreespin() {
		boolean freeSpin = false;
		try {
			clickAtButton("return " + xpathMap.get("AutoPlayStartBtn"));
			Thread.sleep(13000);
			waitForSpinButton();
			log.debug("Free spin over");
			freeSpin = true;
		} catch (Exception e) {
			log.error("Free Spins are not completed");
		}
		return freeSpin;
	}

	/*
	 * Date: 15/05/2019 name:Snehal Gaikwad Description:To verify Autoplay spin
	 * selection Parameter: NA
	 * 
	 * @return boolean
	 */

	@Override
	public boolean autoplaySpinSelection() {
		boolean spin_autoplay = false;
		try {
			Thread.sleep(1000);
			clickAtButton("return " + xpathMap.get("ClickAutoPlayMoreOptionsBtn"));// hooks to open autoplay
			log.debug("drag and drop performed");

			clickAtButton("return " + forceNamespace
					+ ".getControlById('AutoplayPanelComponent').numberSpinsSliderEventReceived({'eventName': 'CLICK', 'value': '1' })");// hooks
																																			// to
																																			// change
																																			// set
																																			// spin
																																			// till
																																			// max
																																			// from
																																			// autoplay
																																			// spin
																																			// slider
			Thread.sleep(1000);
			clickAtButton("return " + forceNamespace
					+ ".getControlById('AutoplayPanelComponent').numberSpinsSliderEventReceived({'eventName': 'CLICK', 'value': '0.5' })");
			Thread.sleep(1000);
			clickAtButton("return " + forceNamespace
					+ ".getControlById('AutoplayPanelComponent').numberSpinsSliderEventReceived({'eventName': 'CLICK', 'value': '1' })");
			Thread.sleep(1000);
			clickAtButton("return " + forceNamespace
					+ ".getControlById('AutoplayPanelComponent').numberSpinsSliderEventReceived({'eventName': 'CLICK', 'value': '0' })");
			spin_autoplay = true;
		} catch (Exception e) {
			log.error("Spin count not getting change.", e);
			return spin_autoplay = false;

		}
		return spin_autoplay;

	}

	/*
	 * Date: 15/05/2019 Author: Snehal Gaikwad Description:To verify Autoplay spin
	 * session stop Parameter: NA
	 * 
	 * @return boolean
	 */

	@Override
	public boolean isAutoplaySessionEnd() {

		boolean spin_session = false;

		try {

			log.debug("Clicked Auto paly..");
			clickAtButton("return " + xpathMap.get("AutoPlayStartBtn"));
			elementWait("return " + xpathMap.get("BetIconCurrState"), "active");
			log.debug("Autoplay is Active Now");
			spin_session = true;

		} catch (Exception e) {
			log.error("Session not over after autoplay spin", e);
			return spin_session = false;
		}
		return spin_session;
	}

	/*
	 * Date: 15/05/2019 Author: Snehal Gaikwad Description:To verify maximum spin
	 * Parameter: NA
	 * 
	 * @return boolean
	 */
	@Override
	public boolean maxSpinChk() {
		try {
			clickAtButton("return " + xpathMap.get("ClickAutoPlayMoreOptionsBtn"));
			clickAtButton("return " + forceNamespace
					+ ".getControlById('AutoplayPanelComponent').numberSpinsSliderEventReceived({'eventName': 'CLICK', 'value': '1' })");
			return true;
		} catch (Exception e) {
			log.error("Session not over after autoplay spin", e);
			return false;
		}
	}

	/*
	 * Date: 15/05/2019 Author: Snehal Gaikwad Description:To verify Auto play on
	 * refreshing Parameter: NA
	 * 
	 * @return boolean
	 */
	@Override
	public boolean autoplayOnRefresh() {
		try {
			clickAtButton("return " + xpathMap.get("AutoPlayStartBtn"));
			// details_append(" verify On refresh, the previous AutoPlay session must not
			// resume."," On refresh, the previous AutoPlay session must not resume.","The
			// previous AutoPlay session has not resume", "pass");

			Thread.sleep(2000);
			webdriver.navigate().refresh();
			waitForSpinButton();
			funcFullScreen();
			newFeature();
			log.debug("On refresh previous autoplay session has not resume");
		} catch (Exception e) {
			log.error("On refresh previous autoplay session has resume");
			return false;
		}

		return true;
	}

	/*
	 * Date: 15/05/2019 Author: Snehal Gaikwad. Description:To verify autoplay must
	 * stop when focus being removed. Parameter: NA
	 * 
	 * @return boolean
	 */
	@Override
	public boolean autoplayFocusRemoved() {

		try {
			clickAtButton("return " + xpathMap.get("ClickAutoPlayMoreOptionsBtn"));
			Thread.sleep(1000);
			clickAtButton("return " + xpathMap.get("AutoPlayStartBtn"));
			webdriver.runAppInBackground(Duration.ofSeconds(30));

			// webdriver.findElement(By.id(XpathMap.get("AutoplayID"))).click();
			// webdriver.findElement(By.id(XpathMap.get("Start_Autoplay_ID"))).click();

			// webdriver.launchApp();
		}

		catch (Exception e) {
			log.error("Focus not get changed");
			log.error(e.getMessage());
			return false;
		}

		return true;

	}

	/*
	 * Date: 29/05/2018 Author:Premlata Mishra Description: This method is to wait
	 * till the element get enable Parameter: NA
	 */
	public void elementWait(String element) {

		while (true) {
			String currentScene = GetConsoleText(element);
			if (currentScene != null && currentScene.equalsIgnoreCase("active")) {
				break;
			}
		}
	}

	@Override
	public boolean waitforWinAmount(String currencyFormat, Mobile_HTML_Report currencyReport, String currencyName) {
		boolean result = false;
		String strwinexp = null;
		long startTime = System.currentTimeMillis();
		String winamt = null;
		try {
			// Thread.sleep(6000);
			log.debug("Function-> waitforWinAmount()");
			ArrayList<String> textlist = getConsoleList(
					"return theForce.game.automation.getControlById('TieredBigWinComponent').Text");
			if ("Yes".equalsIgnoreCase(xpathMap.get("BigWinlayers"))) {
				for (int i = 1; i <= 3; i++) {
					Thread.sleep(10000);
					waitForbigwin();
					log.debug("Bigwinlayer captured" + i);
					System.out.println("Bigwinlayer captured" + i);

					while (true) {
						if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame"))) {
							winamt = getConsoleText("return " + xpathMap.get("WinMobileText"));
							log.info("verifyBigWinCurrencyFormat():waiting for win  to occur");
						} else
							winamt = getConsoleText("return " + xpathMap.get("BigWinCountUpText"));
						if (winamt != null) {
							log.info(" win occur ");
							// fetching win from panel
							String consolewinnew = winamt.replaceAll("win: ", "");
							String winregexp = createregexp(consolewinnew, currencyFormat);
							if (winregexp.contains("$"))
								strwinexp = winregexp.replace("$", "\\$");
							else
								strwinexp = winregexp;
							Pattern.compile(strwinexp);
							String winexp = strwinexp.replace("#", "\\d");

							log.debug(
									"verifyBigWinCurrencyFormat():Win Exp= " + winexp + "consolewin= " + consolewinnew);

							if (Pattern.matches(winexp, consolewinnew)) {
								result = true;
								currencyReport.detailsAppendFolderOnlyScreeshot(currencyName);
								log.debug("curency format is correct");

							} else {
								result = false;
								currencyReport.detailsAppendFolderOnlyScreeshot(currencyName);
								log.debug("curency format is incorrect");
							}

							break;
						} else {
							long currentime = System.currentTimeMillis();
							if (((currentime - startTime) / 1000) > 120) {
								System.out.println("verifyBigWinCurrencyFormat():No win occur  in 2");
								result = false;
								break;
							}
						}

					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.error("Big win not occur.please  use bigwin test data");

		}

		return result;
	}

	/**
	 * *Author:Premlata This method is used to click on win history button
	 * 
	 * @throws InterruptedException
	 */
	@Override
	public boolean winHistoryClick() throws Exception {
		try {
			clickAtButton("return " + xpathMap.get("ClickWinHistoryExpander"));
			Thread.sleep(1000);
			log.debug("Clicked on win history button");
			return true;
		} catch (Exception e) {
			log.error("error on clikcing win history button", e);
		}
		return false;
	}

	/**
	 * *Author:Havish This method is used to wait till the free spin summary screen
	 * won't come
	 */
	@Override
	public void waitSummaryScreen() {
		Wait = new WebDriverWait(webdriver, 100);

		try {
			long startTime = System.currentTimeMillis();
			log.info("Waiting for Summary Screen to come");
			for (int i = 1; i >= 1; i++) {
				String currentScene = GetConsoleText("return " + xpathMap.get("currentScene"));
				if (currentScene != null && (currentScene.contains("FREESPINS_COMPLETE"))) {
					log.debug("Desire current scene reached , breaking while condition ");
					Thread.sleep(1000);
					break;
				}

				long currentime = System.currentTimeMillis();
				// Break if wait is more than 30 secs
				if (((currentime - startTime) / 1000) > 240) {
					log.debug("break as wait is more than 240 seconds");
					break;
				}
			}
			log.debug("Waiting for base screen to come");
			// Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("clock_ID"))));
		} catch (Exception e) {
			log.error("error while waiting for summary screen");
		}
	}

	@Override
	public String clickBonusSelection(int freeSpinCount) {
		String FS_Credits = null;
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("clock_ID"))));
			Thread.sleep(500);
			if (freeSpinCount == 1) {
				// clicking by hooks
				clickAtButton("return " + xpathMap.get("FirstFSSelection"));
			} else if (freeSpinCount == 2) {
				// clicking by hooks
				// clickAtButton("return
				// "+forceNamespace+".getControlById('BonusSelectionComponent').Sprites.fsselectionTroyActive.events.onInputUp.dispatch("+forceNamespace+".getControlById('BonusSelectionComponent').Sprites.fsselectionTroyActive)");
				clickAtButton("return " + xpathMap.get("SecondFSSelection"));
			} else if (freeSpinCount == 3) {
				// clicking by hooks
				// clickAtButton("return
				// "+forceNamespace+".getControlById('BonusSelectionComponent').Sprites.fsselectionMichaelActive.events.onInputUp.dispatch("+forceNamespace+".getControlById('BonusSelectionComponent').Sprites.fsselectionMichaelActive)");
				clickAtButton("return " + xpathMap.get("ThirdFSSelection"));
			} else if (freeSpinCount == 4) {
				// clicking by hooks
				// clickAtButton("return
				// "+forceNamespace+".getControlById('BonusSelectionComponent').Sprites.fsselectionSarahActive.events.onInputUp.dispatch("+forceNamespace+".getControlById('BonusSelectionComponent').Sprites.fsselectionSarahActive)");
				clickAtButton("return " + xpathMap.get("FourthFSSelection"));
			}
			Thread.sleep(1000);
		} catch (Exception e) {
			e.getMessage();
		}
		return FS_Credits;
	}

	public void bonusClick(String bonusComponent, String align) {
		try {
			Coordinates coordinateObj = new Coordinates();
			typeCasting(bonusComponent + ".x", coordinateObj);
			coordinateObj.setX(coordinateObj.getX());
			typeCasting(bonusComponent + ".y", coordinateObj);
			coordinateObj.setY(coordinateObj.getX());
			typeCasting(bonusComponent + ".height", coordinateObj);
			coordinateObj.setHeight(coordinateObj.getX());
			typeCasting(bonusComponent + ".width", coordinateObj);
			coordinateObj.setWidth(coordinateObj.getX());
			coordinateObj.setAlign(align);
			getComponentCenterCoordinates(coordinateObj);
			clickAtCoordinates(coordinateObj.getX() / 2/*-coordinateObj.getWidth()/2*/, coordinateObj.getY() / 2);
			// targerian
			// coordinateObj.getX()+50;coordinateObj.getY()-coordinateObj.getHeight()/2;
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public void FSSceneWait(String checkCurrentScene, String currentSceneText) {
		while (true) {
			String currentScene = GetConsoleText(checkCurrentScene);
			if (currentScene != null && currentScene.equalsIgnoreCase(currentSceneText)) {
				break;
			}
		}
	}

	@Override
	public boolean FSSceneLoading(int bonusSelection) {
		try {
			if (bonusSelection == 1) {
				FSSceneWait("return " + xpathMap.get("currentScene"), "FREESPIN_BARATHEON");
			}
			if (bonusSelection == 2) {
				FSSceneWait("return " + xpathMap.get("currentScene"), "FREESPIN_LANNISTER");
			}
			if (bonusSelection == 3) {
				FSSceneWait("return " + xpathMap.get("currentScene"), "FREESPIN_STARK");
			}
			if (bonusSelection == 4) {
				FSSceneWait("return " + xpathMap.get("currentScene"), "FREESPIN_TARGARYEN");
			}
		} catch (Exception e) {
			e.getMessage();
		}
		return true;
	}

	/**
	 * This method is used to check for Free Spin Entry Screen Author: Havish Jain
	 * 
	 * @return true
	 */
	@Override
	public String entryScreen_Wait(String entry_Screen) {
		long startTime = System.currentTimeMillis();
		String wait = "FS";
		try {
			if (entry_Screen.equalsIgnoreCase("yes")) {
				log.debug("Waiting for free spin entry screen to come");
				while (true) {
					String currentScene = GetConsoleText("return " + xpathMap.get("currentScene"));
					if (("BONUS_SELECTION".equalsIgnoreCase(currentScene))
							|| "FREESPINS_INTRO".equalsIgnoreCase(currentScene) || "SLOT".equalsIgnoreCase(currentScene)
							|| "PLAYING_ACTIVE_BONUS".equalsIgnoreCase(currentScene)) {
						wait = "freeSpin";
						// Thread.sleep(3000);
						break;
					}

					long currentime = System.currentTimeMillis();
					// Break if wait is more than 30 secs
					if (((currentime - startTime) / 1000) > 30) {
						log.debug("break after 30 secs");
						break;
					}
				}
			} else
				log.debug("Free Spin Entry Screen is not avalable");
		} catch (Exception e) {
			log.error(e);
		}
		return wait;
	}

	/**
	 * This method is used to wait till FS scene loads Author: Havish Jain
	 * 
	 * @return true
	 */
	@Override
	public boolean FSSceneLoading() {
		try {
			long startTime = System.currentTimeMillis();
			log.debug("waiting for free scene to come");
			while (true) {
				String currentScene = GetConsoleText("return " + xpathMap.get("currentScene"));
				if (currentScene != null && (currentScene.contains("FREESPIN"))) {
					Thread.sleep(2000);
					break;
				}
				long currentime = System.currentTimeMillis();
				// Break if wait is more than 30 secs
				if (((currentime - startTime) / 1000) > 30) {
					log.debug("break after 30 seconds");
					break;
				}
			}
		} catch (Exception e) {
			log.error("error while waiting for free spin scene", e);
		}
		return true;
	}

	/* Havish Jain: Wait for Spin button */
	@Override
	public void waitForSpinButton() {
		try {
			long startTime = System.currentTimeMillis();
			log.debug("Waiting for spin button to visible");
			while (true) {
				String currentScene = getConsoleText("return " + xpathMap.get("currentScene"));
				if (!GameName.contains("Scratch")) {
					if (currentScene != null && currentScene.equalsIgnoreCase("SLOT")) {
						log.debug("Spin button is visible");
						break;
					} else if (currentScene != null && currentScene.contains("FREESPINS_COMPLETE")) {
						if (getConsoleBooleanText("return " + xpathMap.get("IsFS_SummaryContinueBtnVisible"))) {// As in
																												// some
																												// of
																												// the
																												// game
																												// back
																												// to
																												// game
																												// is
																												// present
							clickAtButton("return " + xpathMap.get("ClickToFreeSpinSummaryBackToGameButton"));
						} else {
							Thread.sleep(4000);
						}
						funcFullScreen();
					}
				} else {
					if (currentScene != null && currentScene.equalsIgnoreCase("BASE")) {
						log.debug("Spin button is visible");
						break;
					}
				}
				long currentime = System.currentTimeMillis();
				// Break if wait is more than 30 secs
				if (((currentime - startTime) / 1000) > 30) {
					log.debug("Spin button is not visible, break after 30 sec");
					break;
				}
			}

		} catch (Exception e) {
			log.error("error while waiting for spin button", e);
		}
	}

	@Override
	public void FS_continue() {
		try {
			clickAtButton("return " + xpathMap.get("ClickStartFSBtn"));
			log.debug("Clicked on free spin continue button");
			Thread.sleep(2000);
		}

		catch (Exception e) {
			log.error("error while clicking on free spin continue button", e);
		}
	}

	@Override
	/**
	 * This method is used to click on free spin enrtry screen
	 */
	public String clickToContinue() {

		String ret = null;
		WebDriverWait wait = new WebDriverWait(webdriver, 60);
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("clock"))));
			elementWait("return " + xpathMap.get("IsFS_EntryContinueBtnVisible"), true);
			boolean flag = getConsoleBooleanText("return " + xpathMap.get("IsFS_EntryContinueBtnVisible"));
			if (flag) {
				System.out.println("Going to click on continue btn");
				Thread.sleep(4000);
				// if there is no hook for the continue button, then closeOverlay will be called
				if (xpathMap.get("Click_FS_EntryContinueBtn") != null) {
					clickAtButton("return " + xpathMap.get("Click_FS_EntryContinueBtn"));
				} else {
					closeOverlay();
				}
				System.out.println(" clicked on continue btn");

				log.debug("Clicked free spin entry screen continue button to start Free spins");
				ret = "Clicked";
			} else {
				log.debug("No  continue button available in free spins entry screen");
				ret = "NotClicked";
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return ret;
	}

	@Override
	public boolean open_Bet() {
		boolean isBetOpen = false;
		try {
			/*
			 * String BetX= "return "+forceNamespace+
			 * ".getControlById('BetComponent').Buttons.betTextDesktopButton.centerX" ;
			 * String BetY= "return "+forceNamespace+
			 * ".getControlById('BetComponent').Buttons.betTextDesktopButton.centerY" ;
			 * Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.
			 * get("clock")))); clickAtCoordinates(BetX, BetY);
			 */
			if (!GameName.contains("Scratch")) {
				new WebDriverWait(webdriver, 500).until(ExpectedConditions.or(
						ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("clock_ID"))),
						ExpectedConditions.visibilityOfElementLocated(
								By.xpath(xpathMap.get("OneDesign_NewFeature_ClickToContinue")))));
			} else {
				Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("clock"))));
			}
			clickAtButton("return " + xpathMap.get("ClickBetIconBtn"));
			isBetOpen = true;
			log.debug("Clicked on TotalBet button");
			Thread.sleep(2000);
		} catch (Exception e) {
			isBetOpen = false;
			log.error("Error in opening bet", e);
		}
		return isBetOpen;
	}

	/*
	 * Date: 29/05/2018 Author:Havish Jain Description: To Open menu container
	 * Parameter: NA
	 */
	@Override
	public void closeTotalBet() throws InterruptedException {
		try {
			if (!GameName.contains("Scratch")) {
				closeOverlay();
				boolean flag = getConsoleBooleanText("return " + xpathMap.get("isSpinBackBtnVisible"));
				if (flag) {
					clickAtButton("return " + xpathMap.get("ClickSpinBackBtn"));
					log.debug("Clicked close totalBet button");
				}
			} else {
				closeOverlay();
			}
			log.debug("Clicked on screen to close totalBet overlay");
			Thread.sleep(100);
		} catch (Exception e) {
			log.error("error in close_TotalBet()", e);
			log.error(e);
		}
	}

	@Override
	public boolean menuOpen() {
		boolean ret = false;
		try {
			clickAtButton("return " + xpathMap.get("ClickHamburgerMenuBtn"));
			log.debug("Clicked on menu button");
			ret = true;
			Thread.sleep(1000);
		} catch (Exception e) {
			ret = false;
			log.debug("error in menuOpen", e);
		}
		return ret;
	}

	/*
	 * Date: 29/05/2018 Author:Havish Jain Description: To Close menu container
	 * Parameter: NA
	 */
	@Override
	public boolean menuClose() {
		try {
			boolean test = false;
			try {
				if (!GameName.contains("Scratch")) {
					boolean flag = getConsoleBooleanText("return " + xpathMap.get("isSpinBackBtnVisible"));
					if (flag) {
						clickAtButton("return " + xpathMap.get("ClickSpinBackBtn"));
						log.debug("Clicked close menu button");
					} else {
						clickAtButton("return " + xpathMap.get("ClickHamburgerMenuBtn"));
						test = true;
					}
				} else {
					Thread.sleep(1000);
					clickAtButton("return " + xpathMap.get("ClickHamburgerMenuBtn"));
					// clickAtCoordinates(dx, dy);
					log.debug("Clicked to close menu Overlay");
				}
				test = true;
			} catch (Exception e) {
				log.debug("error in menuClose()", e);
			}
			return test;
		} catch (Exception e) {
			log.error("exception in menu", e);
		}
		return true;
	}

	/**
	 * Date:07/12/2017 Author:Premlata Mishra This method is usedto open the
	 * settings
	 * 
	 * @return true
	 * @throws InterruptedException
	 */
	@Override
	public boolean settingsOpen() throws InterruptedException {
		Wait = new WebDriverWait(webdriver, 50);
		boolean test = false;
		try {
			if (!GameName.contains("Scratch")) {
				if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame"))) {
					boolean flag = getConsoleBooleanText("return " + xpathMap.get("isMenuSettingsBtnVisible"));
					if (flag) {
						clickAtButton("return " + xpathMap.get("ClickHamburgerMenuBtn"));
						clickAtButton("return " + xpathMap.get("MenuSettingBtn"));
						log.debug("Clicked on setting button in menu");
					}
				} else {
					clickAtButton("return " + xpathMap.get("MenuOptionsBtn"));
					log.debug("Clicked on settings button");
				}
				test = true;
			} else {
				clickAtButton("return " + xpathMap.get("MenuOptionsBtn"));
				log.debug("Clicked on settings button");
			}
			test = true;
		}

		catch (Exception e) {
			log.error("error in settignsOpen()", e);
		}
		return test;
	}

	/*
	 * public String splashScreen(Mobile_HTML_Report report,String language){
	 * 
	 * Wait=new WebDriverWait(webdriver,500); try { webdriver.navigate().refresh();
	 * Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get(
	 * "preLoaderBackgroundID")))); Thread.sleep(3000);
	 * report.details_append_folder("verify the Splash Screen",
	 * "Splash Screen should display", "Splash screen is displaying",
	 * "pass",language ); log.debug("Splash screen screenshot has taken");
	 * Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get(
	 * "clock_ID")))); //newFeature(); } catch (Exception e) {
	 * log.error("exception in splshScreen method", e); } return null;
	 * 
	 * }
	 */
	/**
	 * Date:07/12/2017 Author:Laxmikanth Kodam This method is actually not necessery
	 * in component store just declaration needed
	 * 
	 * @return true
	 */
	@Override
	public boolean settingsBack() {
		boolean test = false;
		long dx = 10, dy = 100;
		try {
			if (!GameName.contains("Scratch")) {
				if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame"))) {
					boolean flag = getConsoleBooleanText("return " + xpathMap.get("isHamburgerMenuBtnVisible"));
					if (flag) {
						clickAtButton("return " + xpathMap.get("ClickHamburgerMenuBtn"));
						clickAtButton("return " + xpathMap.get("ClickSpinBackBtn"));
						log.debug("Clicked on setting button in menu");
						test = true;
					}
				} else {
					closeOverlay();
					log.debug("Clicked to close settings overlay");
					test = true;
				}
			} else {
				Thread.sleep(1000);
				clickAtCoordinates(dx, dy);
				test = true;
				log.debug("Clicked to close settings overlay");
			}

		} catch (Exception e) {
			log.error("error in settingsBack()", e);
		}
		return test;
	}

	/**
	 * Date:07/12/2017 Author:premlata Mishra This method is to get bet ammount
	 * 
	 * @return true
	 */
	@Override
	public String getBetAmt() {
		String betVal = null;
		try {
			String bet = "return " + xpathMap.get("BetButtonLabel");
			String consoleBet = GetConsoleText(bet);
			String consoleBetnew = consoleBet.replaceAll("[a-zA-Z$]", "");
			betVal = consoleBetnew.substring(2);
		} catch (Exception e) {
			e.getMessage();
		}
		return betVal;
	}

	public String GetBetAmt1() {
		String Betamtnew = null;
		try {
			String Betamt = "return " + xpathMap.get("BetButtonLabel");
			String Betamt1 = GetConsoleText(Betamt);

			if (!Betamt1.isEmpty()) {
				String splitedWin[] = splitString(Betamt1, ".");
				Betamtnew = splitedWin[0].replaceAll("[^0-9]", "");
				Thread.sleep(100);
			} else
				Betamtnew = "0.0";

			System.out.println("Bet amount " + Betamt1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Betamtnew;

	}

	/**
	 * Date:07/12/2017 Author:premlata mishra This method is to get win amount
	 * 
	 * @return true
	 */
	@Override
	public String getWinAmt() {
		String Winamtnew = null;
		try {
			String Winamt = "return " + xpathMap.get("WinMobileText");
			String Winamt1 = GetConsoleText(Winamt);

			if (!Winamt1.isEmpty()) {
				String splitedWin[] = splitString(Winamt1, ".");
				Winamtnew = splitedWin[0].replaceAll("[^0-9]", "");
				Thread.sleep(100);
			} else
				Winamtnew = "0.0";

			System.out.println("win amount " + Winamt1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Winamtnew;
	}

	/**
	 * Date:25/06/2018 Author:Premlata Mishra This method is used to verify Quick
	 * spin is available or not
	 * 
	 * @return true
	 * @throws InterruptedException
	 */
	@Override
	public boolean verifyQuickSpin() {
		boolean test = false;
		try {
			// To verify the quick spin button in setting panel
			// String qucikSpin="return
			// mgs.mobile.casino.slotbuilder.v1.automation.getControlById('GameSettingsComponent').Buttons.quickSpinButton.enable";
			boolean quickSpinTxt = getConsoleBooleanText("return " + xpathMap.get("SettingsQuickSpin"));
			// To verify the quick spin button on base scene component
			boolean quickspinbasescene = getConsoleBooleanText("return " + xpathMap.get("isQuickSpinBtnVisible"));
			// condition is to check quick spin button is not available
			if (quickSpinTxt == true && quickspinbasescene == true) {
				// As quick spin button is present in setting as well as base scene component
				test = true;
			} else {
				test = false;
			}
		} catch (Exception e) {
			return test = false;
		}
		return test;
	}

	/**
	 * Date:10-1-2018 Name:Havish Jain Description: this function is used to Scroll
	 * the page using cordinates and to take the screenshot
	 * 
	 * @throws Exception
	 */
	@Override
	public void capturePaytableScreenshot(Mobile_HTML_Report report, String language) {
		try {
			JavascriptExecutor js = (webdriver);
			Coordinates coordinateObj = new Coordinates();
			clickAtButton("return " + xpathMap.get("MenuPaytableBtn"));
			log.debug("Clicked on paytable button");
			Thread.sleep(2000);
			/*
			 * Adding the Jackpot info table screen shots for game having bolt on features
			 */
			if (checkAvilability(xpathMap.get("IsJackpotInfoPaytableComponentVisible"))) {
				captureJackpotInfoPaytable(report, language);
				clickAtButton("return " + xpathMap.get("CloseJackpotInfoPaytableComponent"));
			}
			// report.detailsAppendFolder("verify the paytable screen shot", " paytable
			// first page screen shot", "paytable first page screenshot ", "pass",
			// language);
			typeCasting("return " + xpathMap.get("PaytableScrollHeight"), coordinateObj);
			coordinateObj.setPaytableFullHeight2(coordinateObj.getX());
			typeCasting("return " + xpathMap.get("PaytableScroll_h"), coordinateObj);
			coordinateObj.setPaytableHeight2(coordinateObj.getX());
			// height adjustment because of text missing out
			int paytablHeight2 = ((int) coordinateObj.getPaytableHeight2()) - 160;
			int scroll = (int) (coordinateObj.getPaytableFullHeight2() / paytablHeight2);

			System.out.println("Total Screenshot count " + scroll);

			report.detailsAppendFolder("verify the paytable screen shot ", " paytable next page screen shot ",
					"paytable next page screenshot ", "pass", language);
			for (int i = 1; i <= scroll + 1; i++) {
				threadSleep(1000);
				js.executeScript("return " + forceNamespace
						+ ".getControlById('PaytableComponent').paytableScroller.scrollTo(0, -" + paytablHeight2 * i
						+ ")");
				threadSleep(1000);
				report.detailsAppendFolder("verify the paytable screen shot ", " paytable next page screen shot ",
						"paytable next page screenshot ", "pass", language);
			}
		} catch (Exception e) {
			log.error("error in paytableOpenScroll()", e);
		}
	}

	/**
	 * Date:10-1-2018 Name:Sneha Jawarkar Description: this function is used to
	 * Scroll the story page and to take the screenshot
	 * 
	 * @throws Exception
	 */
	public void scrollstory(Mobile_HTML_Report report, String languageCode) {
		try {
			JavascriptExecutor js = (webdriver);
			Coordinates coordinateObj = new Coordinates();
			typeCasting("return " + xpathMap.get("PaytableStoryScrollHeight"), coordinateObj);
			coordinateObj.setPaytableFullHeight2(coordinateObj.getX());
			typeCasting("return " + xpathMap.get("PaytableStoryScroll_h"), coordinateObj);
			coordinateObj.setPaytableHeight2(coordinateObj.getX());
			int scroll = (int) (coordinateObj.getPaytableFullHeight2() / coordinateObj.getPaytableHeight2());

			for (int i = 1; i <= scroll; i++) {
				js.executeScript("return " + forceNamespace
						+ ".getControlById('PaytableStoryComponent').storyScroll.scrollTo(0,-"
						+ coordinateObj.getPaytableHeight2() * i + ")");
				Thread.sleep(1500);
				report.detailsAppendFolder("verify the story screen shot", " story next page screen shot",
						"story next page screenshot ", "pass", languageCode);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			log.error("Error while scrolling stroy");
		}
	}

	public void closestory() {
		Wait = new WebDriverWait(webdriver, 50);
		clickAtButton("return " + forceNamespace
				+ ".getControlById('PaytableStoryComponent').onButtonClicked('storyGameInfoButton')");
		log.debug("Stroy clode using Game ingot button present in story");

	}

	/**
	 * Date:11-11-2019 Name:Sneha Jawarkar Description: this function is used to
	 * verify stroy option in paytable the page and to take the screenshot
	 * 
	 * @throws Exception
	 */
	@Override
	public String Verifystoryoptioninpaytable(Mobile_HTML_Report report, String languageCode) {
		Wait = new WebDriverWait(webdriver, 50);
		String story = null;
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("clock_ID"))));
			clickAtButton("return " + xpathMap.get("MenuPaytableBtn"));
			log.debug("Clicked on paytable icon to open ");
			if (checkAvilability(xpathMap.get("IsJackpotInfoPaytableComponentVisible"))) {
				captureJackpotInfoPaytable(report, languageCode);
				clickAtButton("return " + xpathMap.get("CloseJackpotInfoPaytableComponent"));
			}
			Thread.sleep(1500);
			// open story for Amber take screen shot and then close
			clickAtButton("return " + xpathMap.get("PaytableStoryAmberBtn"));
			report.detailsAppendFolder("verify the Amberstory screen shot", " Amberstory first page screen shot",
					"Amberstory first page screenshot ", "pass", languageCode);
			// scrollstory(report,languageCode);
			closestory();
			log.debug("Stroy capture for Amber");
			Thread.sleep(3000);

			// open story for Troy take screen shot and then close
			clickAtButton("return " + xpathMap.get("PaytableStoryTroyBtn"));
			report.detailsAppendFolder("verify the Troystory screen shot", " Troystory first page screen shot",
					"Troystory first page screenshot ", "pass", languageCode);
			// scrollstory(report,languageCode);
			closestory();
			log.debug("Stroy capture for Troy");
			Thread.sleep(3000);

			// open story for Michael take screen shot and then close
			clickAtButton("return " + xpathMap.get("PaytableStoryMichaelBtn"));
			report.detailsAppendFolder("verify the Michaelstory screen shot", " Michaelstory first page screen shot",
					"Michaelstory first page screenshot ", "pass", languageCode);
			// scrollstory(report,languageCode);
			closestory();
			log.debug("Stroy capture for Michael");
			// open story for Sarah take screen shot and then close
			clickAtButton("return " + xpathMap.get("PaytableStorySarahBtn"));
			report.detailsAppendFolder("verify the Sarahstory screen shot", " Sarahstory first page screen shot",
					"Sarahstory first page screenshot ", "pass", languageCode);
			// scrollstory(report,languageCode);
			closestory();
			log.debug("Stroy capture for Sarah");
			story = "story1";
		} catch (Exception e) {
			log.error("error in opening story", e);
		}
		return story;
	}

	/**
	 * *Author:Pramlata Mishra This method is used to verify currency symbol in bet
	 * setting screen
	 * 
	 * @throws InterruptedException
	 */
	@Override
	public boolean betSettingCurrencySymbol(String currencyFormat, Mobile_HTML_Report report, String CurrencyName)
			throws Exception {
		boolean isCurrentSymbolPresent = false;
		String strBetExp = null;
		String bet = "return " + xpathMap.get("BetSizeText");
		String panelbet = GetConsoleText(bet);
		// check whether the currency symbol in bet panel

		String betregexp = createregexp(panelbet, currencyFormat);
		if (betregexp.contains("$"))
			strBetExp = betregexp.replace("$", "\\$");
		else
			strBetExp = betregexp;
		Pattern.compile(strBetExp);
		String betexp = strBetExp.replace("#", "\\d");

		if (Pattern.matches(betexp, panelbet)) {
			isCurrentSymbolPresent = true;
		} else {
			isCurrentSymbolPresent = false;
		}

		int totalNoOfQuickBets = (int) getNumberOfQuickbets();
		// ArrayList<String> quickBets= getConsoleList("return
		// "+forceNamespace+".getControlById('BetPanelComponent').getQuickBetValue()");
		// int totalNoOfQuickBets = quickBets.size();
		for (int quickBet = 0; quickBet < totalNoOfQuickBets; quickBet++) {
			String strQuickBet = "return " + forceNamespace
					+ ".getControlById('BetPanelComponent').Buttons.quickBetButton" + quickBet + ".label._text";

			String quickbet = GetConsoleText(strQuickBet);
			String quickbetbetregexp = createregexp(quickbet, currencyFormat);
			if (quickbetbetregexp.contains("$"))
				strBetExp = quickbetbetregexp.replace("$", "\\$");
			else
				strBetExp = quickbetbetregexp;
			Pattern.compile(strBetExp);
			String quickbetexp = strBetExp.replace("#", "\\d");

			byte ptext[] = strQuickBet.getBytes("UTF-8");
			strQuickBet = new String(ptext, "UTF-8");

			log.debug("Quick Bet= " + quickBet + " quick bet exp= " + quickbetexp);
			if (Pattern.matches(quickbetexp, quickbet)) {
				isCurrentSymbolPresent = true;
				log.debug("quick bet format result=" + isCurrentSymbolPresent);
				// report.detailsAppendFolder("Verify the currency format in
				// "+strQuickBet+"Quick bet", "Quick bet Currency display in correct format",
				// "Quick bet Currency should display in correct format", "pass",CurrencyName);
			} else {
				// report.detailsAppendFolder("Verify the currency format in
				// "+strQuickBet+"Quick bet", "Quick bet Currency display in correct format",
				// "Quick bet Currency does not display in correct format",
				// "Fail",CurrencyName);
				isCurrentSymbolPresent = false;
				log.debug("quick bet format result=" + isCurrentSymbolPresent);
			}
		}

		return isCurrentSymbolPresent;
	}

	/**
	 * *Author:Pramlata Mishra This method is used to verify multiplier
	 * 
	 * @throws InterruptedException
	 */
	@Override
	public String getCurrentBet() {
		String betnew = null;
		String bet = null;
		try {
			log.debug("reading bet text form base scene");
			if (!GameName.contains("Scratch")) {
				if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame"))) {
					bet = "return " + xpathMap.get("BetTextValue");

				} else {
					bet = "return " + xpathMap.get("BetButtonLabel");

				}
			} else {
				bet = "return " + xpathMap.get("InfobarBettext");

			}
			log.debug("read bet text form base scene");
			String consoleBet = getConsoleText(bet);
			betnew = consoleBet.replaceAll("[^0-9.,]", "");

			while (betnew.startsWith(".")) {

				betnew = betnew.substring(1);
				log.debug("betvalue: " + betnew);
			}

			while (betnew.endsWith(".")) {

				betnew = betnew.substring(0, betnew.length() - 1);
				log.debug("betvalue: " + betnew);
			}

			log.debug("New bet text after replacement=" + betnew);
		} catch (Exception e) {
			log.error("Error in verifying multiplier", e);
		}
		return betnew;
	}

	/**
	 * *Author:Premlata Mishra This method is used to get currency symbol .
	 * 
	 * @throws InterruptedException
	 */
	@Override
	public String getCurrencySymbol() {
		String currencySymbol = null;
		String currencySymbolnew = null;
		try {
			String consoleBalance = getCurrentCredits();
			log.debug(consoleBalance);
			// currencySymbol = consoleBalance.replaceAll("[[0-9][a-zA-Z],.\\s]", "");
			currencySymbol = consoleBalance.replaceAll("[[0-9]:,.\\s]", "");
			currencySymbolnew = currencySymbol.replace("credits", "");
			currencySymbolnew = currencySymbolnew.replace("Credits", "");
			currencySymbolnew = currencySymbolnew.replace("CREDITS", "");
		} catch (Exception e) {
			log.error("error in getting currency symbol", e);
		}
		return currencySymbolnew;
	}

	/*
	 * Date: 22/02/2019 Author:Premlata Description:this method is to wait for
	 * jackpot scene Parameter: NA
	 */
	@Override
	public void jackpotSceneWait() {
		elementWait("return " + xpathMap.get("currentScene"), "SLOT");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Description : Element wait till it is visible and returns boolean value
	 * Overload of public void elementWait(String element,String currentSceneText)
	 * 
	 */
	@Override
	public boolean elementWait(String element, String currentSceneText) {
		long startTime = System.currentTimeMillis();
		while (true) {
			String currentScene = GetConsoleText(element);
			if (currentScene != null && currentScene.equalsIgnoreCase(currentSceneText)) {
				return true;
			}
			long currentime = System.currentTimeMillis();
			// Break if wait is more than 30 secs
			if (((currentime - startTime) / 1000) > 60) {
				log.debug("Element  is not visible, break after 30 sec");
				return false;
			}
		}
	}

	@Override
	public boolean elementWait(String element, boolean value) {
		boolean isElementVisible = false;
		long startTime = System.currentTimeMillis();
		while (true) {
			boolean isVisible = getConsoleBooleanText(element);
			// System.out.println("element :: "+element +"and value :: "+isVisible);
			if (isVisible == value) {
				isElementVisible = true;
				break;
			}
			long currentime = System.currentTimeMillis();
			// Break if wait is more than 30 secs
			if (((currentime - startTime) / 1000) > 60) {
				log.debug("Element  is not visible, break after 30 sec");
				break;
			}
			threadSleep(300);
		}
		return isElementVisible;
	}

	/*
	 * Date: 29/05/2018 Author:Havish Jain Description: To click on spin button
	 * Parameter: NA
	 */
	@Override
	public boolean spinclick() throws InterruptedException {
		Wait = new WebDriverWait(webdriver, 500);
		try {
			// Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("clock_ID"))));
			if (!GameName.contains("Scratch")) {
				clickAtButton("return " + xpathMap.get("ClickSpinBtn"));
			} else {
				clickAtButton("return  " + forceNamespace
						+ ".getControlById('ScratchButtonComponent').onButtonClicked('scratchButton')");

				elementWait(
						"return " + forceNamespace
								+ ".getControlById('ScratchButtonComponent').Buttons.scratchButton.currentState",
						"activeSecondary");

				clickAtButton("return  " + forceNamespace
						+ ".getControlById('ScratchButtonComponent').onButtonClicked('scratchButton')");
			}
			log.debug("Clicked on spin button");
		} catch (Exception e) {
			log.error("error while clicking on spin button", e);
		}
		return true;
	}

	/**
	 * Date: 14/12/2017 Autohr: Laxmikanth Kodam Description: This function used to
	 * take to the game
	 * 
	 * @return null
	 */

	@Override
	public String verifyCreditsValue() {
		String val = null;
		// DecimalFormat format = new DecimalFormat();
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("clock_ID"))));
			String balance = "return " + xpathMap.get("Balancetext");
			String consoleBalance = GetConsoleText(balance).replace(".", "/");

			String splited_balance[] = consoleBalance.split("/");
			if (splited_balance[1].equals("00")) {
				val = splited_balance[0].replaceAll("[^0-9]", "");// this is for comparison of italy script
			} else {
				consoleBalance = GetConsoleText(balance);
				val = consoleBalance.replaceAll("[a-zA-Z]", "").replace(",", "").substring(2);
			}
		} catch (Exception e) {
			e.getMessage();
		}
		return val;
	}

	/**
	 * Date: 04/06/2018 Author: Havish Jain Description: This function is used for
	 * touch event in mobile devices Parameter: By locator
	 */

	@Override
	public void gameToLobby() {
		try {
			closeTotalBet();
			String lobbyX = "return " + forceNamespace
					+ ".getControlById('MenuIconPanelComponent.lobbyButton').centerX";
			String lobbyY = "return " + forceNamespace
					+ ".getControlById('MenuIconPanelComponent.lobbyButton').centerY";
			clickAtCoordinates(lobbyX, lobbyY);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	/**
	 * Date:22/11/2017 Author Lamxikanth Kodam Common added for logout() This method
	 * is common logout function for the component store
	 * 
	 * @return
	 */
	@Override
	public String Func_logout_OD() {
		String loginTitle = null;
		try {
			clickAtButton("return " + forceNamespace
					+ ".getControlById('MenuIconPanelComponent').onButtonClicked('lobbyButton')");
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("five_Reel_slot"))));
			loginTitle = func_GetText(xpathMap.get("five_Reel_slot"));
			nativeClickByID(xpathMap.get("M_navigation_MyAccountID"));
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("logout"))));
			loginTitle = func_GetText(xpathMap.get("logout"));
			JavascriptExecutor js = (webdriver);
			js.executeScript("arguments[0].scrollIntoView(true);",
					webdriver.findElement(By.id(xpathMap.get("logout_ID"))));
			js.executeScript("arguments[0].click();", webdriver.findElement(By.id(xpathMap.get("logout_ID"))));
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("Login"))));
			Thread.sleep(3000);
			nativeClickByID(xpathMap.get("closeButtonLoginID"));

		} catch (Exception e) {
			e.getMessage();
		}
		return loginTitle;
	}

	/**
	 * Author : Havish Jain Description: To wait until loss limit is reached Param:
	 * Null Return: String value
	 */
	@Override
	public String waitUntilSessionLoss() {
		String title = null;
		try {
			for (int i = 0; i <= 10; i++) {
				// Wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id(XpathMap.get("M_SpinButton_ID"))));
				spinclick();
				Thread.sleep(3000);
				boolean b = webdriver.findElements(By.xpath(xpathMap.get("spain_lossLimitDialogueOK"))).size() > 0;
				if (b) {
					System.out.println("Loss Limit is reached");
					title = webdriver.findElement(By.xpath(xpathMap.get("spain_lossLimitDialogueOK"))).getText();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return title;
	}

	/*
	 * Author:Havish This method is used discard any existing offer which is not
	 * finished previously
	 * 
	 * @throws InterruptedException
	 */
	@Override
	public String freeGamesDiscardExistingOffer() {
		Wait = new WebDriverWait(webdriver, 100);
		String Text = null;
		try {
			if (!GameName.contains("Scratch")) {
				new WebDriverWait(webdriver, 500).until(ExpectedConditions.or(
						ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("clock_ID"))),
						ExpectedConditions.visibilityOfElementLocated(
								By.xpath(xpathMap.get("OneDesign_NewFeature_ClickToContinue")))));
			} else {
				Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("clock"))));
			}
			Text = GetConsoleText(
					"return " + forceNamespace + ".getControlById('FreeGamesComponent').viewHistory.pop()");
			if (Text.equalsIgnoreCase("freeGamesResumeView")) {
				resumeScreenDiscardClick();
				confirmDiscardOffer();
				clickNextOffer();
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return Text;
	}

	/*
	 * Author:Havish This method is used to wait till the freeGamesContinueExpiry
	 * screen and then click on continue button
	 * 
	 * @throws InterruptedException
	 */
	@Override
	public String freeGamesContinueExpiry() {
		Wait = new WebDriverWait(webdriver, 100);
		String Text = null;
		try {
			if (!GameName.contains("Scratch")) {
				new WebDriverWait(webdriver, 500).until(ExpectedConditions.or(
						ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("clock_ID"))),
						ExpectedConditions.visibilityOfElementLocated(
								By.xpath(xpathMap.get("OneDesign_NewFeature_ClickToContinue")))));
			} else {
				Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("clock"))));
			}
			Thread.sleep(100);
			String scene = GetConsoleText("return " + xpathMap.get("FGViewHistoryPop"));
			if (scene.equalsIgnoreCase("freeGamesExpiredView")) {
				Text = GetConsoleText("return " + xpathMap.get("FreeGamesExpiredViewContinueBtn"));
				if (Text.equalsIgnoreCase("continueButton")) {
					clickAtButton("return " + xpathMap.get("ClickFGExpiredViewContinueBtn"));
					Thread.sleep(100);
					clickNextOffer();
				}
			}
			/*
			 * JavascriptExecutor js = ((JavascriptExecutor)webdriver); Coordinates
			 * coordinateObj=new Coordinates(); String align="return "+forceNamespace+
			 * ".getControlById('FreeGamesComponent').views.freeGamesExpiredView.Buttons.continueButton.buttonData.text.layoutStyles.desktop.alignment";
			 * typeCasting("return "+forceNamespace+
			 * ".getControlById('FreeGamesExpiredView.continueButton').x",coordinateObj);
			 * coordinateObj.setX(coordinateObj.getSx());
			 * typeCasting("return "+forceNamespace+
			 * ".getControlById('FreeGamesExpiredView.continueButton').y",coordinateObj );
			 * coordinateObj.setY(coordinateObj.getSx());
			 * typeCasting("return "+forceNamespace+
			 * ".getControlById('FreeGamesExpiredView.continueButton').height",coordinateObj
			 * ); coordinateObj.setHeight(coordinateObj.getSx());
			 * typeCasting("return "+forceNamespace+
			 * ".getControlById('FreeGamesExpiredView.continueButton').width",coordinateObj)
			 * ; coordinateObj.setWidth(coordinateObj.getSx());
			 * coordinateObj.setAlign((js.executeScript(align)).toString());
			 * getComponentCenterCoordinates(coordinateObj);
			 * clickAtCoordinates(coordinateObj.getCenterX(), coordinateObj.getCenterY());
			 * clickNextOffer();
			 */
			// }
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return Text;
	}

	/*
	 * Author:Havish This method is used to click on Next Offer button
	 * 
	 * @throws InterruptedException
	 */
	@Override
	public void clickNextOffer() {
		try {
			/*
			 * JavascriptExecutor js = ((JavascriptExecutor)webdriver); String Text =
			 * GetConsoleText("return "+forceNamespace+
			 * ".getControlById('FreeGamesComponent').viewHistory.pop()");
			 * if(Text.equalsIgnoreCase("freeGamesCompleteView")){ Coordinates
			 * coordinateObj=new Coordinates(); String align="return "+forceNamespace+
			 * ".getControlById('FreeGamesComponent').views.freeGamesCompleteView.Buttons.nextOfferButton.buttonData.text.layoutStyles.desktop.alignment";
			 * typeCasting("return "+forceNamespace+
			 * ".getControlById('FreeGamesCompleteView.nextOfferButton').x",coordinateObj);
			 * coordinateObj.setX(coordinateObj.getSx());
			 * typeCasting("return "+forceNamespace+
			 * ".getControlById('FreeGamesCompleteView.nextOfferButton').y",coordinateObj );
			 * coordinateObj.setY(coordinateObj.getSx());
			 * typeCasting("return "+forceNamespace+
			 * ".getControlById('FreeGamesCompleteView.nextOfferButton').height",
			 * coordinateObj); coordinateObj.setHeight(coordinateObj.getSx());
			 * typeCasting("return "+forceNamespace+
			 * ".getControlById('FreeGamesCompleteView.nextOfferButton').width",
			 * coordinateObj); coordinateObj.setWidth(coordinateObj.getSx());
			 * coordinateObj.setAlign((js.executeScript(align)).toString());
			 * getComponentCenterCoordinates(coordinateObj);
			 * clickAtCoordinates(coordinateObj.getCenterX(), coordinateObj.getCenterY()); }
			 */
			String Text = GetConsoleText("return " + xpathMap.get("FGCompleteNextOfferBtn"));
			if (Text.equalsIgnoreCase("nextOfferButton")) {
				clickAtButton("return " + xpathMap.get("ClickFGCompleteNextOfferBtn"));
			}

		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	/*
	 * Author:Havish This method is used to wait till the freeGames Entry screen
	 * 
	 * @throws InterruptedException
	 */
	@Override
	public boolean freeGamesEntryScreen() {
		boolean isFreeGameAssing = false;
		try {
			elementWait("return " + xpathMap.get("isFGOfferPlayNowBtn"), true);
			Thread.sleep(2000);
			isFreeGameAssing = getConsoleBooleanText("return " + xpathMap.get("isFGOfferPlayNowBtn"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return isFreeGameAssing;
	}

	/*
	 * Author:Havish This method is used to click on info icon on Free Games Enrty
	 * Screen
	 * 
	 * @throws InterruptedException
	 */
	@Override
	public boolean freeGameEntryInfo() {
		boolean b = false;
		try {
			clickAtButton("return " + xpathMap.get("ClickFGOfferViewInfoBtn"));
			Thread.sleep(2000);
			/*
			 * Coordinates coordinateObj=new Coordinates(); String
			 * align="return "+forceNamespace+
			 * ".getControlById('FreeGamesComponent').views.freeGamesOfferView.Buttons.infoButton.label.currentStyle.alignment";
			 * typeCasting("return "+forceNamespace+
			 * ".getControlById('FreeGamesOffersView.infoButton').x",coordinateObj);
			 * coordinateObj.setX(coordinateObj.getSx());
			 * typeCasting("return "+forceNamespace+
			 * ".getControlById('FreeGamesOffersView.infoButton').y",coordinateObj );
			 * coordinateObj.setY(coordinateObj.getSx());
			 * typeCasting("return "+forceNamespace+
			 * ".getControlById('FreeGamesOffersView.infoButton').height",coordinateObj);
			 * coordinateObj.setHeight(coordinateObj.getSx());
			 * typeCasting("return "+forceNamespace+
			 * ".getControlById('FreeGamesOffersView.infoButton').width",coordinateObj);
			 * coordinateObj.setWidth(coordinateObj.getSx());
			 * coordinateObj.setAlign((js.executeScript(align)).toString());
			 * getComponentCenterCoordinates(coordinateObj);
			 * clickAtCoordinates(coordinateObj.getCenterX(), coordinateObj.getCenterY());
			 * Thread.sleep(2000);
			 */
			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	/*
	 * Author:Havish This method is to click on PlayNow button
	 * 
	 * @throws InterruptedException
	 */
	@Override
	public boolean clickPlayNow() {
		Wait = new WebDriverWait(webdriver, 500);
		boolean b = false;
		try {
			/*
			 * JavascriptExecutor js = ((JavascriptExecutor)webdriver); Coordinates
			 * coordinateObj=new Coordinates(); String align="return "+forceNamespace+
			 * ".getControlById('FreeGamesComponent').views.freeGamesOfferView.Buttons.playNowButton.buttonData.text.layoutStyles.desktop.alignment";
			 * typeCasting("return "+forceNamespace+
			 * ".getControlById('FreeGamesOffersView.playNowButton').x",coordinateObj);
			 * coordinateObj.setX(coordinateObj.getSx());
			 * typeCasting("return "+forceNamespace+
			 * ".getControlById('FreeGamesOffersView.playNowButton').y",coordinateObj );
			 * coordinateObj.setY(coordinateObj.getSx());
			 * typeCasting("return "+forceNamespace+
			 * ".getControlById('FreeGamesOffersView.playNowButton').height",coordinateObj);
			 * coordinateObj.setHeight(coordinateObj.getSx());
			 * typeCasting("return "+forceNamespace+
			 * ".getControlById('FreeGamesOffersView.playNowButton').width",coordinateObj);
			 * coordinateObj.setWidth(coordinateObj.getSx());
			 * coordinateObj.setAlign((js.executeScript(align)).toString());
			 * getComponentCenterCoordinates(coordinateObj);
			 * clickAtCoordinates(coordinateObj.getCenterX(), coordinateObj.getCenterY());
			 */

			clickAtButton("return " + xpathMap.get("ClickFGOfferViewPlayNowBtn"));
			Thread.sleep(2000);
			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	/*
	 * Author:Havish This method is used to wait till the freeGames resume screen
	 * 
	 * @throws InterruptedException
	 */
	@Override
	public String freeGamesResumescreen() {
		Wait = new WebDriverWait(webdriver, 50);
		String str = null;
		try {
			if (!GameName.contains("Scratch")) {
				new WebDriverWait(webdriver, 500).until(ExpectedConditions.or(
						ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("clock_ID"))),
						ExpectedConditions.visibilityOfElementLocated(
								By.xpath(xpathMap.get("OneDesign_NewFeature_ClickToContinue")))));
			} else {
				Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("clock"))));
			}
			String Text = GetConsoleText("return " + xpathMap.get("FGViewHistoryPop"));
			if (Text.equalsIgnoreCase("freeGamesResumeView")) {
				str = "ResumePlay";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	/*
	 * Author:Havish This method is used to click on free games resume info
	 * 
	 * @throws InterruptedException
	 */
	@Override
	public boolean freeGameResumeInfo() {
		boolean b = false;
		try {

			clickAtButton("return " + xpathMap.get("ClickFGResumeViewInfoBtn"));
			Thread.sleep(2000);
			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	/*
	 * Author:Havish This method is used to click on discard icon on free game
	 * resume screen
	 * 
	 * @throws InterruptedException
	 */
	@Override
	public boolean resumeScreenDiscardClick() {
		boolean b = false;
		try {

			clickAtButton("return " + xpathMap.get("ClickFGResumeViewDeleteBtn"));
			Thread.sleep(2000);
			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	/*
	 * Author:Havish This method is used for confirming discard offer
	 * 
	 * @throws InterruptedException
	 */
	@Override
	public boolean confirmDiscardOffer() {
		boolean b = false;
		try {

			clickAtButton("return " + xpathMap.get("ClickFGDeleteOfferViewDiscardBtn"));
			Thread.sleep(3000);
			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	/*
	 * Author:Havish This method is used to wait for Expiry screen
	 * 
	 * @throws InterruptedException
	 */
	@Override
	public boolean freeGamesExpriyScreen() {
		Wait = new WebDriverWait(webdriver, 50);
		boolean b = false;
		try {
			if (!GameName.contains("Scratch")) {
				new WebDriverWait(webdriver, 500).until(ExpectedConditions.or(
						ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("clock_ID"))),
						ExpectedConditions.visibilityOfElementLocated(
								By.xpath(xpathMap.get("OneDesign_NewFeature_ClickToContinue")))));
			} else {
				Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("clock"))));
			}
			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	/*
	 * Author:Havish This method is used click on Discard button in base scene
	 * during Free Games
	 */
	@Override
	public void clickBaseSceneDiscard() {
		try {

			clickAtButton("return " + xpathMap.get("BaseSceneDiscardBtn"));
			Thread.sleep(2000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setNameSpace() {
		int nameSpaceCount = Integer.parseInt(TestPropReader.getInstance().getProperty("ForceNameSpaceCount"));

		for (int count = 0; count < nameSpaceCount; count++) {
			String nameSpace = TestPropReader.getInstance().getProperty("ForceNameSpace" + count);

			String namspaceResponce = null;
			namspaceResponce = getConsoleText("return " + nameSpace + ".currentScene");
			if (namspaceResponce != null && !"".equals(namspaceResponce)) {
				log.debug("NameSpace for this game :: " + nameSpace);
				forceNamespace = nameSpace;
				log.debug("namsspace for game=" + nameSpace);
				break;
			}
		}

	}

	/*
	 * Author: Snehal Gaikwad Description: This method is used to check responsible
	 * gaimg navigation
	 * 
	 * @input: Mobile report
	 * 
	 * @return: Boolean
	 */
	@Override
	public boolean verifyResponsibleGamingNavigation(Mobile_HTML_Report report) {
		boolean ret = false;
		Wait = new WebDriverWait(webdriver, 500);
		try {
			menuOpen();
			clickAtButton("return " + xpathMap.get("MenuResponsibleGamingBtn"));
			log.debug("Clicked on ResponsibleGameing button to open in menu");
			Thread.sleep(1000);
			String tital = "Responsible Gaming";
			checkPageNavigation(report, tital);
			log.debug("Game navigated to ResponsibleGameing verified");
			ret = true;
		} catch (Exception e) {
			log.error("Error in navigetion to ResponsibleGameing  page ", e);
		}
		return ret;
	}

	/*
	 * Author: Snehal Gaikwad Description: This method is used to check help
	 * navigation
	 * 
	 * @input: Mobile report
	 * 
	 * @return: Boolean
	 */
	@Override
	public boolean verifyHelpNavigation(Mobile_HTML_Report report) {
		boolean ret = false;
		Wait = new WebDriverWait(webdriver, 500);
		try {
			menuOpen();
			clickAtButton("return " + xpathMap.get("MenuHelpBtn"));
			log.debug("Clicked on help button  in menu");
			Thread.sleep(1000);
			String tital = "Casino Help";
			checkPageNavigation(report, tital);
			log.debug("Game navigated to help link  verified");
			ret = true;
		} catch (Exception e) {
			log.error("Error in navigetion to help link  page ", e);
		}
		return ret;
	}

	/*
	 * Author: Snehal Gaikwad Description: This method is used to check stop is
	 * available or not
	 * 
	 * @input: No
	 * 
	 * @return: Boolean
	 */
	@Override
	public boolean isStopButtonAvailable() {
		boolean isstopbutton = true;
		try {

			clickAtButton("return " + xpathMap.get("ClickSpinBtn"));
			// To verify the stop button
			String stopbutton = GetConsoleText("return " + xpathMap.get("SpinBtnCurrState"));

			if (stopbutton.equalsIgnoreCase("activeSecondary") || !(stopbutton.equalsIgnoreCase("disabled"))) {
				// As current state of button is ActiveSecondary
				isstopbutton = true;
				// System.out.println("Stop button is available ");
				log.debug("Stop button is available");
			} else {
				// System.out.println("Stop button is not available ");
				log.debug("Stop button is not available");
				isstopbutton = false;
			}
		} catch (Exception e) {
			log.debug(e.getMessage());
		}

		return isstopbutton;
	}

	@Override
	public String getCurrentCredits() {
		String balance = null;

		/*
		 * if (!GameName.contains("Scratch")) { balance =
		 * "return "+xpathMap.get("Balancetext"); } else { balance =
		 * "return "+xpathMap.get("InfoBarBalanceTxt"); }
		 */

		if (!GameName.contains("Scratch")) {
			if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame")))// force console
			{
				balance = "return " + xpathMap.get("Balancetext");
			} else
				balance = "return " + xpathMap.get("Balancetext");
		} else {
			balance = "return " + xpathMap.get("InfoBarBalanceTxt");

		}
		String consoleBalance = GetConsoleText(balance);
		log.debug("Credits from Console is " + consoleBalance);
		return consoleBalance;
	}

	/**
	 * *Author:Pramlata Mishra This method is used to get element text form game
	 * console
	 * 
	 * @throws InterruptedException
	 */
	public ArrayList<String> getConsoleList(String text) {
		ArrayList<String> list = null;
		try {
			JavascriptExecutor js = (webdriver);
			list = (ArrayList<String>) js.executeScript(text);
		} catch (Exception e) {
			e.getMessage();
		}
		return list;
	}

	public String getBetFromConsole() {
		String bet;
		if (!GameName.contains("Scratch")) {
			bet = "return " + xpathMap.get("BetButtonLabel");
		} else {
			bet = "return " + xpathMap.get("InfobarBettext");
		}

		String consoleBet = GetConsoleText(bet);
		return consoleBet;
	}

	@Override
	public boolean betCurrencySymbol(String currencyFormat) {
		String bet = null;
		String strBetExp = null;
		boolean isBetInCurrencyFormat = false;

		try {

			log.debug("Function-> betCurrencySymbol()");
			log.debug("Reading Bet Text from base scene");
			if (!GameName.contains("Scratch")) {
				if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame"))) {
					bet = "return " + xpathMap.get("BetTextValue");
				} else
					bet = "return " + xpathMap.get("BetButtonLabel");
			} else {
				bet = "return " + xpathMap.get("InfobarBettext");
			}
			String consoleBet = getConsoleText(bet);

			Thread.sleep(100);

			String consoleBetnew = consoleBet.replaceAll("bet: ", "");
			consoleBetnew = consoleBetnew.replaceAll("BET: ", "");
			consoleBetnew = consoleBetnew.replaceAll("Bet: ", "");
			String betregexp = createregexp(consoleBetnew, currencyFormat);
			if (betregexp.contains("$"))
				strBetExp = betregexp.replace("$", "\\$");
			else
				strBetExp = betregexp;
			Pattern.compile(strBetExp);
			String betexp = strBetExp.replace("#", "\\d");

			if (Pattern.matches(betexp, consoleBetnew)) {
				isBetInCurrencyFormat = true;
			} else {
				isBetInCurrencyFormat = false;
			}

			log.debug("Fetching bet currency symbol" + "/n bet currency symbol is::" + consoleBet);
		} catch (Exception e) {
			log.error("Error in Fetching currency symbol", e);
		}

		return isBetInCurrencyFormat;

	}

	public long getNumberOfQuickbets() {

		long quickBetsCount = 0;

		if (!GameName.contains("Scratch")) {
			quickBetsCount = getConsoleNumeric("return " + xpathMap.get("QuickBetOptions"));
		} else {
			quickBetsCount = getConsoleNumeric("return " + xpathMap.get("NoOfQucikBets"));
		}

		return quickBetsCount;
	}

	public int countAutoPlayTillStop() {

		Set<String> hash_Set = new HashSet<String>();
		setFreeSpinTriggered(false);
		String currentScene;
		log.debug("is it a Scratch game :: " + GameName.contains("Scratch"));
		try {

			while (true) {

				if (!GameName.contains("Scratch")) {
					String autoplayValue = GetConsoleText("return " + xpathMap.get("AutoPlayStopBtnLabel"));
					hash_Set.add(autoplayValue);
				} else {
					String autoplayValue = GetConsoleText("return " + xpathMap.get("AutoPlayCounterText"));
					hash_Set.add(autoplayValue);

				}
				if (!GameName.contains("Scratch")) {
					currentScene = GetConsoleText("return " + xpathMap.get("SpinBtnCurrState"));
					if (currentScene != null && currentScene.equalsIgnoreCase("active")) {
						break;
					}
				} else {
					boolean isAutoplayActive = getConsoleBooleanText("return " + xpathMap.get("isAutoPlayActive"));
					if (!isAutoplayActive) {
						break;
					}
				}

				String freeSpinScene = GetConsoleText("return " + xpathMap.get("currentScene"));
				if (freeSpinScene.equalsIgnoreCase("FREESPINS")) {
					setFreeSpinTriggered(true);
				}

			}

		} catch (Exception e) {
			log.error("Exception in countAutoPlayTillStop method", e);
		}
		System.out.println("Autoply count=" + hash_Set);
		return hash_Set.size();
	}

	public boolean isFreeSpinTriggered() {
		boolean isFreeSpinTriggered = false;
		long startTime = System.currentTimeMillis();
		while (true) {
			String currentScene = getConsoleText("return " + xpathMap.get("currentScene"));
			if (("BONUS_SELECTION".equalsIgnoreCase(currentScene))
					|| "FREESPINS_INTRO".equalsIgnoreCase(currentScene)) {
				isFreeSpinTriggered = true;
				System.out.println("Free spin triggerd");
				log.debug("Free spin triggerd");
				break;
			}

			long currentime = System.currentTimeMillis();
			// Break if wait is more than 30 secs
			if (((currentime - startTime) / 1000) > 60) {
				isFreeSpinTriggered = false;
				log.debug("break after 30 secs");
				break;
			}
		}

		return isFreeSpinTriggered;
	}

	public void setFreeSpinTriggered(boolean freeSpinTriggered) {

		isFreeSpinTriggered = freeSpinTriggered;
	}

	@Override
	public boolean verifyAutoplayConsoleOptions(Mobile_HTML_Report report) {

		try {
			int noofautoplayoptions;
			/*
			 * depending upon calling class set the autoplay count to play
			 */
			if ((Class.forName(Thread.currentThread().getStackTrace()[2].getClassName())).toString()
					.contains("Modules.Regression.TestScript.Mobile_Regression_BaseScene")) {
				noofautoplayoptions = (int) getConsoleNumeric("return " + xpathMap.get("AutoplaySpinNoArrayLength"))
						- 1;
			} else
				noofautoplayoptions = 1;

			// this counter = number of console options available in autoplay console.
			for (int counter = 1; counter <= noofautoplayoptions; counter++) {
				log.debug("Autoplay for ::" + counter);
				clickAtButton("return " + xpathMap.get("ClickAutoplayOpenMobileBtn"));
				Thread.sleep(1000);
				clickAtButton("return " + forceNamespace
						+ ".getControlById('AutoplayOptionsComponent').onButtonClicked('autoplayQuickOptionButton"
						+ counter + "')");
				Thread.sleep(1000);

				report.detailsAppend("verify the AutoplayConsoleOptions screen shot for option = " + counter,
						" AutoplayConsoleOptions page screen shot for option = " + counter,
						"AutoplayConsoleOptions page screenshotfor option = " + counter, "pass");

				long autoplaySelectedCount = getConsoleNumeric(" return " + forceNamespace
						+ ".getControlById('AutoplayOptionsComponent').Buttons.autoplayQuickOptionButton" + counter
						+ ".autoPlaySpinNumber");
				clickAtButton("return " + forceNamespace
						+ ".getControlById('AutoplayButtonComponent').onButtonClicked('autoplayQuickStopButton')");
				Thread.sleep(1000);

				log.debug("Awaiting Autoplay to comlete for counter ::" + counter);
				int autplayActualCount = countAutoPlayTillStop();
				Thread.sleep(2000);
				if (autoplaySelectedCount == autplayActualCount) {
					report.detailsAppend(
							"verify the Autoplay Selected Count is matched with Autoplay actul count for option = "
									+ autoplaySelectedCount,
							"  Autoplay Selected Count is matched with Autoplay actul count for option = "
									+ autoplaySelectedCount,
							" Autoplay Selected Count is matched with Autoplay actul count for option = "
									+ autoplaySelectedCount,
							"pass");
					log.debug("Auto play Successfully completed for counter ::" + autoplaySelectedCount);
				} else {
					if (isFreeSpinTriggered) {
						report.detailsAppend(
								"verify that is the Freespin triggered during Autoplay so Autoplay must be intercepted for counter ::"
										+ autoplaySelectedCount,
								" The Freespin triggered during Autoplay so Autoplay is intercepted for counter ::"
										+ autoplaySelectedCount,
								"The Freespin triggered during Autoplay so Autoplay is intercepted for counter ::"
										+ autoplaySelectedCount,
								"pass");
						log.debug("Free Spin Triggered in Auto play and Auto play interupted");
					} else {
						report.detailsAppend(
								"Autoply count  match", "Autoplay count does not match, autoplaySelectedCount="
										+ autoplaySelectedCount + "autplayActualCount=" + autplayActualCount,
								"", "fail");
						log.debug("Free Spin Triggered in Auto play and Auto play not interupted so Failed Scenario");
					}
				}
				log.debug("autoplaySelectedCount :: " + autoplaySelectedCount);
				log.debug("autplayActualCount :: " + autplayActualCount);
			}
			return true;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			try {
				report.detailsAppendNoScreenshot("verify the AutoplayConsoleOptions  ", "  ",
						"Exception occur while AutoplayConsoleOptions  ", "fail");
			} catch (Exception e1) {
				log.error(e1.getStackTrace());
			}

			return false;
		}
	}

	public boolean isBetButtonAccessible() {

		try {
			String currentState = GetConsoleText("return " + xpathMap.get("BetIconCurrState"));
			if (currentState.equalsIgnoreCase("Active")) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isMaxButtonAccessible() {
		try {
			String currentState;

			if (!GameName.contains("Scratch")) {

				currentState = GetConsoleText("return " + xpathMap.get("MaxButnCurrState"));
			} else {
				currentState = GetConsoleText("return " + xpathMap.get("MaxButnTextCurrState"));
			}
			if (currentState.equalsIgnoreCase("Active")) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Author:Sneha Jawarkar This method is used for verifying the status of the
	 * quick spin and set the status to On.
	 * 
	 * @return void
	 */
	public void setQuickSpinOn() {
		try {
			if (!GameName.contains("Scratch")) {
				String QS_currectState = GetConsoleText("return " + xpathMap.get("QuickSpinBtnState"));
				if (!QS_currectState.equalsIgnoreCase("active")) {
					clickAtButton("return " + xpathMap.get("QuickSpinBtn"));
				}
			} else {
				String QS_currectState = GetConsoleText("return " + xpathMap.get("TurboBtnCurrState"));
				if (!QS_currectState.equalsIgnoreCase("active")) {
					clickAtButton("return " + xpathMap.get("ClickTurboBtn"));
				}

			}
		} catch (Exception e) {
			log.error("Quickspin Not able to off.", e);
		}
	}

	public void waitTillScratchAutoplayStop() {

		while (true) {
			boolean isAutoplay = getConsoleBooleanText("return " + xpathMap.get("isAutoPlayActive"));
			if (!isAutoplay) {
				break;
			}
		}
	}

	/**
	 * Author:Sneha Jawarkar This method is used for verifying the status of the
	 * quick spin and set the status to off.
	 * 
	 * @return void
	 */
	public void setQuickSpinOff() {
		try {
			if (!GameName.contains("Scratch")) {
				String QS_currectState = GetConsoleText("return " + xpathMap.get("QuickSpinBtnState"));
				if (QS_currectState.equalsIgnoreCase("active")) {
					clickAtButton("return " + xpathMap.get("QuickSpinBtn"));
					String QS_currectState_off = GetConsoleText("return " + xpathMap.get("QuickSpinBtnState"));
					if (QS_currectState_off.equalsIgnoreCase("activeSecondary"))
						log.debug("Quickspin is off Succesfully");
					log.debug("Quickspin is off Succesfully");
				} else {
					log.error("Quickspin is not Active");
				}
			} else {

				String QS_currectState = GetConsoleText("return " + xpathMap.get("TurboBtnCurrState"));
				if (QS_currectState.equalsIgnoreCase("active")) {
					clickAtButton("return " + xpathMap.get("ClickTurboBtn"));
					String QS_currectState_off = GetConsoleText("return " + xpathMap.get("TurboBtnCurrState"));
					if (QS_currectState_off.equalsIgnoreCase("activeSecondary"))
						log.debug("Quickspin is off Succesfully");
				} else {
					log.error("Quickspin is not Active");
				}

			}
		} catch (Exception e) {
			log.error("Quickspin Not able to off.", e);
		}
	}

	@Override
	public boolean verifyAutoplayPanelOptions(Mobile_HTML_Report report) {

		try {
			if (Constant.FORCE_ULTIMATE_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame"))
					|| Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame"))) {
				verifyOneDesignAutoplayPanelOptions(report);
			} else {
				int noofautoplayoptions;

				if ((Class.forName(Thread.currentThread().getStackTrace()[2].getClassName())).toString()
						.contains("Modules.Regression.TestScript.Mobile_Regression_BaseScene")) {
					noofautoplayoptions = (int) getConsoleNumeric("return " + xpathMap.get("AutoplaySpinNoArrayLength"))
							- 1;
				} else
					noofautoplayoptions = 1;

				// This counter = number of panel options available in autoplay panel.
				for (int counter = 1; counter <= noofautoplayoptions; counter++) {
					log.debug("Autoplay for ::" + counter);
					clickAtButton("return " + xpathMap.get("ClickAutoplayOpenMobileBtn"));
					Thread.sleep(1000);
					clickAtButton("return " + xpathMap.get("ClickAutoPlayMoreOptionsBtn"));
					Thread.sleep(1000);
					// Screen shot for Autoplay panel option

					report.detailsAppend("verify the Autoplay panel option screen shot for = " + counter,
							" Autoplay panel option page screen shot for = " + counter,
							"Autoplay panel option page screenshot  for = " + counter, "pass");

					clickAtButton("return " + forceNamespace
							+ ".getControlById('AutoplayPanelComponent').numberSpinsSliderEventReceived({'eventName': 'CLICK', 'value': '"
							+ (counter * 0.25) + "' })");
					clickAtButton("return " + xpathMap.get("AutoPlayStartBtn"));
					Thread.sleep(2000);
					log.debug("Awaiting Autoplay to comlete for counter ::" + counter);

					// is bet Available.. Bet must not be accessible during auto
					// play

					boolean isBetButtonActive = isBetButtonAccessible(); // Must not be accessible during auto play

					if (isBetButtonActive) {
						report.detailsAppend("verify that is BetButton Active during Autoplay",
								" Is BetButton Active during Autoplay", " BetButton Active during Autoplay ", "fail");
						log.debug("isBetButtonActive Active:: FAIL");
					} else {
						report.detailsAppend("verify that is BetButton Active during Autoplay",
								" Is BetButton Active during Autoplay", "BetButton is not  Active during Autoplay ",
								"pass");
						log.debug("isBetButtonActive not Active:: PASS");
					}

					boolean isMaxButtonActive = isMaxButtonAccessible(); // Must not be accessible during auto play
					if (isMaxButtonActive) {
						report.detailsAppend("verify that is MaxButton Accessible during Autoplay",
								" Is MaxButton Accessible during Autoplay", "MaxButton Accessible during Autoplay ",
								"fail");
						log.debug("isMaxButtonActive Active:: FAIL");
					} else {
						report.detailsAppend("verify that is MaxButton Accessible during Autoplay",
								" Is MaxButton Accessible during Autoplay",
								"MaxButton is not Accessible during Autoplay", "pass");
						log.debug("isMaxButtonActive not Active:: PASS");
					}
					setQuickSpinOn();
					report.detailsAppend("verify the QuickSpin On screen shot", " QuickSpinOn page screen shot",
							"QuickSpinOn option page screenshot ", "pass");
					Thread.sleep(2000);

					setQuickSpinOff();
					report.detailsAppend("verify the QuickSpinOff screen shot", " QuickSpinOff page screen shot",
							"QuickSpinOff option page screenshot ", "pass");
					Thread.sleep(1000);

					waitTillAutoplayComplete();

					Thread.sleep(1000);
				}
			}
		} catch (InterruptedException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			try {
				report.detailsAppendNoScreenshot("verify Autoplay panel option", " ",
						"Exception occur while verifying autoplay panel option", "fail");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		return false;
	}

	/*
	 * Date: 25/04/2019 Description:To verify Auto play on refreshing Parameter: NA
	 * 
	 * @return boolean
	 */
	@Override
	public boolean isAutoplayOnAfterRefresh() {
		boolean onrefresh = true;
		try {
			clickAtButton("return " + xpathMap.get("AutoPlayStartBtn"));
			// details_append(" verify On refresh, the previous AutoPlay session must not
			// resume."," On refresh, the previous AutoPlay session must not resume.","The
			// previous AutoPlay session has not resume", "pass");

			Thread.sleep(1000);
			webdriver.navigate().refresh();
			Thread.sleep(3000);
			// to remove hand gesture
			funcFullScreen();
			waitForSpinButton();
			onrefresh = getConsoleBooleanText("return " + xpathMap.get("isAutoPlayActive"));
			// Check Auto play Status and then return the response
			log.debug("On refresh previous autoplay session has not resume");
		} catch (Exception e) {
			log.error("On refresh previous autoplay session has resume");
			return onrefresh = true;
		}

		return onrefresh;
	}

	public int getAutoPlayCount() {
		int count = 0;
		String strCount = null;
		if (!GameName.contains("Scratch")) {
			strCount = GetConsoleText("return " + xpathMap.get("AutoPlayStopBtnLabel"));
		} else {
			strCount = GetConsoleText("return " + xpathMap.get("AutoPlayCounterText"));
		}
		if (strCount != null && !"".equals(strCount.trim())) {
			count = Integer.parseInt(strCount);
		}
		return count;
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

	@Override
	public boolean isAutoplayPauseOnFocusChange(Mobile_HTML_Report report) {
		boolean isAutoplayPauseOnFocusChange = false;
		try {

			clickAtButton("return " + xpathMap.get("AutoPlayStartBtn"));

			/*
			 * clickAtButton( "return "+forceNamespace+
			 * ".getControlById('AutoplayOptionsComponent').onButtonClicked('autoplayMoreOptionsButton')"
			 * ); Thread.sleep(2000);
			 * webdriver.findElement(By.id(XpathMap.get("Start_Autoplay"))).click();
			 */
			Thread.sleep(3000);

			// Get the Auto play pending Count before focus Change
			int countb4FocusChange = getAutoPlayCount();

			report.detailsAppend("Verify autoplay count before focus changed",
					"Autoplay count before focus changed is = " + countb4FocusChange,
					"Autoplay count before focus changed is = " + countb4FocusChange, "Pass");

			webdriver.runAppInBackground(Duration.ofSeconds(30));

			// newTabOpen();
			// Get the Auto play pending Count after focus Change
			int countAfetrFocusChange = getAutoPlayCount();
			// Check the count difference and send the response

			if ((countb4FocusChange) <= countAfetrFocusChange) {
				isAutoplayPauseOnFocusChange = true;
			}
			log.debug("Auto play countb4FocusChange :: " + countb4FocusChange);
			log.debug("Auto play countAfetrFocusChange :: " + countAfetrFocusChange);

		} catch (Exception e) {
			log.error("Error on opening new tab", e);
		}
		return isAutoplayPauseOnFocusChange;
	}

	@Override
	public boolean isAutoplayStoppedOnMenuClick() {

		boolean isAutoplayStoppedOnMenuClick = true;

		// Start Auto Play
		clickAtButton("return " + xpathMap.get("AutoPlayStartBtn"));

		try {
			// wait for some time
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			log.error(e.getMessage(), e);
		}

		clickAtButton("return " + xpathMap.get("ClickHamburgerMenuBtn"));

		isAutoplayStoppedOnMenuClick = getConsoleBooleanText("return " + xpathMap.get("isAutoPlayActive"));
		// as on success isAutoplayActive() returns false
		return (!isAutoplayStoppedOnMenuClick);
	}

	public boolean isCreditDeducted(String creditB4Spin, String betValue) {

		String creditAfetrSpin = getCurrentCredits();

		double dblcreditAfetrSpin = Double.parseDouble(creditAfetrSpin.replaceAll("[^0-9]", ""));
		double dblcreditB4Spin = Double.parseDouble(creditB4Spin.replaceAll("[^0-9]", ""));
		double dblbetValue = Double.parseDouble(betValue.replaceAll("[^0-9]", ""));

		if ((dblcreditB4Spin - dblbetValue) == dblcreditAfetrSpin) {
			System.out.println("Intial Cradit is = " + dblcreditB4Spin + " and selected bet is = " + dblbetValue);
			System.out.println("Cridit after spin is = " + dblcreditAfetrSpin);
			log.debug("Cradit after spin with calculation is " + dblcreditB4Spin + "-" + dblbetValue + " = "
					+ dblcreditAfetrSpin);
			return true;
		}
		return false;
	}

	public void verifyALLScratchBetValues(Mobile_HTML_Report report) {
		log.debug("Verifing All scatch Bet Values");
		boolean isCreditDeducted = false;
		boolean isWinAddedToCredit = false;
		String consoleBet = null;
		Util util = new Util();

		int numofbet = 0;

		int length = util.xmlLength("./" + GameName + "/Config/" + GameName + ".xml", "Results");
		if (length != 0) {
			// Read the XML Values
			System.out.println("length" + length);
			ArrayList<String> symbolData = util.getXMLDataInArray("Symbol", "name",
					"./" + GameName + "/Config/" + GameName + ".xml");

			HashMap<String, Symbol> symbolmMap = new HashMap<>();

			int totalNoOfSymbols = symbolData.size();

			for (int count = 0; count < totalNoOfSymbols; count++) {
				String[] xmlData = symbolData.get(count).split(",");
				String symbolName = xmlData[0];
				symbolmMap.put(symbolName, new Symbol(symbolName));
			}

			for (int count = 0; count < length; count++) {
				String strResult = util.readXML("Result", "numSymbolsRequired", "symbol", "payout",
						"./" + GameName + "/Config/" + GameName + ".xml", length + 2, count);
				String[] xmlData = strResult.split(",");
				String strSymbolName = xmlData[1];
				int intPayout = Integer.parseInt(xmlData[2].trim());
				Symbol symbol = symbolmMap.get(strSymbolName);

				symbol.setPayout(intPayout);
				symbolmMap.put(strSymbolName, symbol);
			}

			// logout from previous player and log in with new player having win at every
			// spin
			try {

				System.out.println("Logouted from privios player and trying to login with new player");
				log.debug("Logout from privios player and trying to login with new player");

				String url = xpathMap.get("ApplicationURL");
				String LaunchURl = url + xpathMap.get("WinPlayerUsername");
				System.out.println("log in with new win player : " + xpathMap.get("WinPlayerUsername"));
				loadGame(LaunchURl);
				funcFullScreen();
				log.debug("login with new player having win at each spin");
			} catch (Exception e1) {
				log.error(e1.getMessage(), e1);
				e1.printStackTrace();
			}

			// First get All the bet values
			ArrayList<String> betList = getConsoleList("return " + xpathMap.get("BetOptions"));
			// For Eaach bet

			try {
				if ((Class.forName(Thread.currentThread().getStackTrace()[2].getClassName())).toString()
						.contains("Modules.Regression.TestScript.Mobile_Regression_BaseScene"))
					numofbet = betList.size();
				else
					numofbet = 1;
			} catch (ClassNotFoundException e1) {

				e1.printStackTrace();
				log.error(e1.getException());
			}
			for (int counter = 0; counter < numofbet; counter++) {
				int c = 0;
				int w = 0;
				// Open the Bet PAnel
				openBetPanel();

				// Set the bet VAlue
				try {
					Thread.sleep(2000);
					GetConsoleText("return " + forceNamespace + ".getControlById('BetPanelComponent').updateBetValue("
							+ betList.get(counter) + ")");
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// Close the Bet Panel
				try {
					closeTotalBet();
				} catch (InterruptedException e1) {
					log.error(e1.getMessage(), e1);
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String consolebet = getConsoleText("return " + xpathMap.get("BetSizeText"));
				boolean isBetChangedIntheConsole = isBetChangedIntheConsole(
						getConsoleText("return " + xpathMap.get("BetSizeText")));
				try {
					if (isBetChangedIntheConsole) {
						report.detailsAppend("verify that is Bet Changed In the Console for = " + consolebet,
								" Is Bet Changed In the Console for = " + consolebet,
								"Bet Changed In the Console for = " + consolebet, "pass");
						log.debug("Bet Changed In the Console for = " + consolebet);
					} else {
						report.detailsAppend("verify that is Bet Changed In the Console for = " + consolebet,
								"Is Bet Changed In the Console for = " + consolebet,
								"Bet not Changed In the Console for = " + consolebet, "fail");
						log.debug("Bet not Changed In the Console  for = " + consolebet);

					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					e.printStackTrace();
				}

				for (int scratchCount = 0; scratchCount < totalNoOfSymbols; scratchCount++) {
					String creditB4Scratch = GetConsoleText("return " + xpathMap.get("InfoBarBalanceTxt"));
					log.debug("credit before scrach is = " + creditB4Scratch);
					clickAtButton("return  " + xpathMap.get("ClickscratchButton"));

					consoleBet = GetConsoleText("return " + xpathMap.get("InfobarBettext"));

					isCreditDeducted = isCreditDeducted(creditB4Scratch, consoleBet);
					try {
						if (isCreditDeducted) {
							c++;
							// report.details_append("verify that is Credit Deducted for = "+consoleBet, "
							// Is Credit Deducted for = "+consoleBet, "Credit Deducted for = "+consoleBet,
							// "pass");
							log.debug("Credit Deducted for" + consoleBet);
						} else {
							// report.details_append("verify that is Credit Deducted for = "+consoleBet, "Is
							// Credit Deducted for = "+consoleBet, "Credit not Deducted for = "+consoleBet,
							// "fail");
							log.debug("Credit not Deducted for" + consoleBet);
						}
					} catch (Exception e) {
						log.error(e.getMessage(), e);
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					elementWait("return " + xpathMap.get("ScratchBtnCurrState"), "activeSecondary");

					clickAtButton("return  " + xpathMap.get("ClickscratchButton"));

					ArrayList<String> symbolList = getConsoleList("return " + xpathMap.get("GridGridSelection"));

					Map<String, Integer> hm = new HashMap<String, Integer>();

					String strWinSymbol = null;
					for (String symbol : symbolList) {
						Integer j = hm.get(symbol);
						hm.put(symbol, (j == null) ? 1 : j + 1);
						if (hm.get(symbol) == 3) {
							strWinSymbol = symbol;
						}
					}

					if (strWinSymbol != null) {
						if (strWinSymbol != null) {

							log.debug("Verifying Win value");

							Symbol winSymbol = symbolmMap.get(strWinSymbol);

							int winPayout = winSymbol.getPayout();
							waitForWinDisplay();
							String winValue = getConsoleText("return " + xpathMap.get("WinText"));
							// if player won any ammount
							checkWinValue(winValue, winPayout, consoleBet, report);
							isWinAddedToCredit = isWinAddedToCredit(creditB4Scratch, consoleBet);
							try {
								if (isWinAddedToCredit) {
									w++;
									// report.details_append("verify that is Win Added To Credit for
									// "+strWinSymbol+" And "+bet, " Is Win Added To Credit for "+strWinSymbol+" And
									// "+bet, "Win Added To Credit for "+strWinSymbol+" And "+bet, "pass");
									log.debug("Win added to Credit for the bet : " + consoleBet);
								} else {
									// report.details_append("verify that is Win Added To Credit for
									// "+strWinSymbol+" And "+bet, "Is Win Added To Credit for "+strWinSymbol+" And
									// "+bet, "Win not Added To Credit for "+strWinSymbol+" And "+bet, "fail");
									log.debug("Win is not added to Credit for the bet : " + consoleBet);
								}
							} catch (Exception e) {
								log.error(e.getMessage(), e);
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}
					// strWinSymbol = null;
				}
				try {
					// common report to verify isCreditDeducted for the bet
					if (c != 0) {
						report.detailsAppendNoScreenshot(
								"Verify Credits are deducting properly after spin for the bet :" + consoleBet,
								"Credits are deducting properly after spin for the bet : " + consoleBet,
								"Credits are deducting properly after spin for the bet :" + consoleBet, "pass");
					} else {
						report.detailsAppendNoScreenshot(
								"Verify Credits are deducting properly after spin for the bet :" + consoleBet,
								"Credits are not deducting properly after spin for the bet : " + consoleBet,
								"Credits are not deducting properly after spin for the bet :" + consoleBet, "fail");
					}
					// report to verify isWinAddedToCredit for the bet
					if (w != 0) {
						report.detailsAppendNoScreenshot(
								"Verify Win ammount updating properly after spin for the bet :" + consoleBet,
								"Win ammount updatingproperly after spin for the bet : " + consoleBet,
								"Win ammount updating properly after spin for the bet :" + consoleBet, "pass");
					} else {
						report.detailsAppendNoScreenshot(
								"Verify Win ammount updating properly after spin for the bet :" + consoleBet,
								"Win ammount updating properly after spin for the bet : " + consoleBet,
								"Win ammount not updating properly after spin for the bet :" + consoleBet, "fail");
					}
				} catch (Exception e) {
					log.error("error while taking report for the win and cridit for the bet");
					log.error(e.getMessage(), e);

				}
			}
		} else {
			log.info("game.xml not found. paste game.xml file in root folder");
			try {
				report.detailsAppendNoScreenshot("Verify all scratch bet values", " ", "game xml  file not found",
						"fail");
			} catch (Exception e) {

				e.printStackTrace();
			}

		}
	}

	@Override
	public String setTheNextLowBet() {
		String betAmount = null;
		try {
			// Decrease the bet value
			clickAtButton("return " + xpathMap.get("DecreaseBetValue"));
			// Get the Bet Value
			betAmount = GetConsoleText("return " + xpathMap.get("BetSizeText"));

		} catch (Exception e) {
			log.error("Exception while decreasing the bet value ::" + e.getMessage(), e);
		}

		return betAmount;
	}

	@Override
	public void openBetPanel() {

		clickAtButton("return " + xpathMap.get("ClickBetIconBtn"));

	}
	/*
	 * Date: 17/05/2019 Author:Snehal Gaikwad Description: To set bet to maximum
	 * Parameter: NA
	 */

	@Override
	public String setMaxBet() {
		String betAmount = null;
		String bet = null;
		int length = 0;
		try {
			if (!GameName.contains("Scratch"))
				bet = "return " + xpathMap.get("SetMaxBet");
			else {
				length = (int) getConsoleNumeric("return " + xpathMap.get("NoOfQucikBets"));
				selectQuickBet(length - 1);
			}
			GetConsoleText(bet);

			betAmount = GetConsoleText("return " + xpathMap.get("BetSizeText"));
		} catch (Exception e) {
			log.error("Exception while setting Max value", e);
			return null;

		}

		return betAmount;
	}

	public boolean isWinAddedToCredit(String creditB4Spin, String betValue) {

		boolean isWinAddedToCredit = false;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String creditAfetrStop = getCurrentCredits();
		String winAmount = null;
		if (!GameName.contains("Scratch")) {
			winAmount = GetConsoleText("return " + xpathMap.get("WinMobileText"));
			log.info("Win Amount: " + winAmount);
		} else {
			winAmount = GetConsoleText("return " + xpathMap.get("WinText"));
			log.info("Win Amount: " + winAmount);
		}

		double dblCreditAfetrStop = Double.parseDouble(creditAfetrStop.replaceAll("[^0-9]", ""));
		double dblCreditB4Spin = Double.parseDouble(creditB4Spin.replaceAll("[^0-9]", ""));
		double dblBetValue = Double.parseDouble(betValue.replaceAll("[^0-9]", ""));
		double dblWinAmount = Double.parseDouble(winAmount.replaceAll("[^0-9]", ""));

		if (((dblCreditB4Spin - dblBetValue) + dblWinAmount) == dblCreditAfetrStop) {
			log.debug("win added to credit succesfully");
			isWinAddedToCredit = true;
		} else {
			log.debug("Win is not added to Credit.Credit calculated: "
					+ ((dblCreditB4Spin - dblBetValue) + dblWinAmount) + "and credit after spin" + dblCreditAfetrStop);
		}

		return isWinAddedToCredit;
	}

	/*
	 * verify if player won or not
	 */
	public boolean isPlayerWon() {
		String winAmount = null;

		boolean isplayerWon = false;
		if (!GameName.contains("Scratch")) {
			winAmount = getConsoleText("return " + xpathMap.get("WinMobileText"));
		} else {
			winAmount = getConsoleText("return " + xpathMap.get("WinText"));
		}

		if (winAmount != null && !"".equals(winAmount) && !"Win: ".equals(winAmount)) {
			isplayerWon = true;
		}

		return isplayerWon;
	}

	public boolean isBetChangedIntheConsole(String betValue) {
		String consoleBet = null;
		String bet = null;
		String bet1 = null;
		if (!GameName.contains("Scratch")) {
			System.out.println("Bet value selected from game = " + betValue);
			consoleBet = getConsoleText("return " + xpathMap.get("BetButtonLabel"));
			bet1 = consoleBet.replaceAll("[a-zA-Z]", "").trim();
			bet = bet1.replaceFirst(":", "").trim();
		} // below else for Scratch game
		else {
			System.out.println("Bet value selected from scrach game = " + betValue);
			consoleBet = getConsoleText("return " + xpathMap.get("BetButtonLabel"));
			// String bet = consoleBet.replaceAll("[a-zA-Z]", "").trim();
			bet1 = consoleBet.replaceAll("[a-zA-Z]", "").trim();
			bet = bet1.replaceFirst(":", "").trim();

			log.debug("Bet Refelecting on console after bet chnage from quickbet : " + consoleBet);
			System.out.println("Bet Refelecting on console after bet change from quickbet : " + consoleBet);
		}
		if (betValue.trim().equalsIgnoreCase(bet)) {
			System.out.println("selected bet " + betValue + " reflecting properly on console " + bet);
			return true;
		} else {
			System.out.println("selected bet " + betValue + " Not reflecting properly on console " + bet);
			return false;
		}

	}

	public boolean waitForWinDisplay() {
		boolean isWinDisplay = false;
		try {
			long startTime = System.currentTimeMillis();
			log.debug("Waiting for win text display to come after completion of spin");
			while (true) {
				boolean flag = getConsoleBooleanText("return " + xpathMap.get("isWinTextVisible"));
				log.debug("is win text Visible=" + flag);
				String flagtext = Boolean.toString(flag);

				if (flagtext != null && flagtext.equalsIgnoreCase("true")) {
					isWinDisplay = true;
					break;
				}
				long currentime = System.currentTimeMillis();
				// Break if wait is more than 80 secs
				if (((currentime - startTime) / 1000) > 120) {
					log.debug("Win is not visible, break after 120 sec");
					break;
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return isWinDisplay;
	}

	public void checkWinValue(String winValue, int winPayout, String betValue, Mobile_HTML_Report report) {

		try {
			double dblwinValue = Double.parseDouble(winValue.replaceAll("[^0-9]", ""));
			double dblbetValue = Double.parseDouble(betValue.replaceAll("[^0-9]", ""));

			if (dblwinValue == dblbetValue * winPayout) {
				report.detailsAppend(
						"verify that check Win Value Matched with bet value multiplied by it's payout is ="
								+ dblwinValue,
						"check Win Value Mached with bet value multiplied by it's payout is =" + dblwinValue,
						"Win Value Matched with bet value multiplied by it's payout is =" + dblwinValue, "Pass");
				log.debug("PASS::check Win Value Mached with bet value multiplied by it's payout is =" + dblwinValue);
			} else {
				report.detailsAppend(
						"verify that check Win Value Matched with bet value multiplied by it's payout is ="
								+ dblwinValue,
						"check Win Value Mached with bet value multiplied by it's payout is =" + dblwinValue,
						"Win Value not Matched with bet value multiplied by it's payout is =" + dblwinValue, "fail");
				log.debug(
						"FAIl::check Win Value not Mached with bet value multiplied by it's payout is =" + dblwinValue);
			}
		} catch (NumberFormatException e) {
			log.error(e.getMessage(), e);
			log.error("NumberFormatException FAIl");
		} catch (Exception e1) {
			log.error(e1.getMessage(), e1);
			log.error("NumberFormatException FAIl");
		}

	}

	public void verifyallScratchQuickBets(Mobile_HTML_Report report) {

		long totalNoOfQuickBets = 0;
		try {
			if ((Class.forName(Thread.currentThread().getStackTrace()[2].getClassName())).toString()
					.contains("Modules.Regression.TestScript.Mobile_Regression_BaseScene"))
				totalNoOfQuickBets = getNumberOfQuickbets();
			else
				totalNoOfQuickBets = 1;
		} catch (ClassNotFoundException e1) {

			e1.printStackTrace();
		}

		for (int quickBet = 0; quickBet < totalNoOfQuickBets; quickBet++) {
			int c = 0;
			int w = 0;
			String quickBetVal = selectQuickBet(quickBet);

			boolean isBetChangedIntheConsole = isBetChangedIntheConsole(quickBetVal);
			try {
				if (isBetChangedIntheConsole) {
					report.detailsAppend(
							"verify that is Bet Changed In the Console during scratch quick bets for =" + quickBetVal,
							"verify that is Bet Changed In the Console during scratch quick bets",
							"Bet Changed In the Console during scratch quick bets", "Pass");
					log.debug("PASS");
				} else {
					report.detailsAppend(
							"verify that is Bet Changed In the Console during scratch quick bets for =" + quickBetVal,
							"verify that is Bet Changed In the Console during scratch quick bets",
							"Bet not Changed In the Console during scratch quick bets", "Fail");
					log.debug("FAIL");
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}

			for (int scratchCounter = 0; scratchCounter < 10; scratchCounter++) {
				log.debug("scratchCounter :: " + scratchCounter);

				String currentCreditAmount = getCurrentCredits();
				String creditB4Scratch = GetConsoleText("return " + xpathMap.get("InfoBarBalanceTxt"));
				log.debug("creditB4Scratch" + creditB4Scratch);

				clickAtButton("return  " + xpathMap.get("ClickscratchButton"));
				log.debug("Cliscked at scratch");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					log.error(e.getMessage(), e);

					e.printStackTrace();
				}
				String consoleBet = GetConsoleText("return " + xpathMap.get("InfobarBettext"));
				log.debug("consoleBet :: " + consoleBet);
				boolean isCreditDeducted = isCreditDeducted(creditB4Scratch, quickBetVal);
				try {
					if (isCreditDeducted) {
						c++;
						// report.details_append("verify that is Credit Deducted during scratch quick
						// bets for =" +consoleBet, "verify that is Credit Deducted during scratch quick
						// bets", "Credit Deducted during scratch quick bets", "Pass");
						log.debug("Credit Deducted during scratch quick bets for =" + quickBetVal);
					} else {
						// report.details_append("verify that is Credit Deducted during scratch quick
						// bets for =" +consoleBet, "verify that is Credit Deducted during scratch quick
						// bets", "Credit not Deducted during scratch quick bets", "Fail");
						log.debug("Credit not Deducted during scratch quick bets for =" + quickBetVal);
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				log.debug("isCreditDeducted" + isCreditDeducted);

				while (true) {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						log.error(e.getMessage(), e);
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					String currentScene = GetConsoleText("return " + xpathMap.get("ScratchBtnCurrState"));
					System.out.println(currentScene);
					if (currentScene != null && currentScene.equalsIgnoreCase("activeSecondary")) {
						log.debug("wating for scrach button");

						break;
					}
				}

				log.debug("ScratchButtonComponent waiting here activeSecondary state");
				clickAtButton("return  " + xpathMap.get("ClickscratchButton"));

				waitForWinDisplay();

				boolean isPlayerWon = isPlayerWon();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (isPlayerWon) {

					boolean isWinAddedToCredit = isWinAddedToCredit(currentCreditAmount, quickBetVal);
					try {
						if (isWinAddedToCredit) {
							w++;
							// report.details_append("verify that is Win Added To Credit during scratch
							// quick bets for = "+currentCreditAmount+" And "+quickBetVal, "verify that is
							// Win Added To Credit during scratch quick bets", "Win Added To Credit",
							// "Pass");
							log.debug("isWinAddedToCredit :: PASS");
						} else {
							// report.details_append("verify that is Win Added To Credit during scratch
							// quick bets"+currentCreditAmount+" And "+quickBetVal, "verify that is Win
							// Added To Credit during scratch quick bets", "Win Added To Credit", "Fail");
							log.debug("isWinAddedToCredit :: Fail");
						}
					} catch (Exception e) {
						log.error(e.getMessage(), e);
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					break;
				}
			}

			try {
				// common report to verify isCreditDeducted for the bet
				if (c != 0) {
					report.detailsAppendNoScreenshot(
							"Verify Credits are deducting properly after spin for the quickbet :" + quickBetVal,
							"Credits are deducting properly after spin for the quickbet : " + quickBetVal,
							"Credits are deducting properly after spin for the quickbet :" + quickBetVal, "pass");
				} else {
					report.detailsAppendNoScreenshot(
							"Verify Credits are deducting properly after spin for the quickbet :" + quickBetVal,
							"Credits are not deducting properly after spin for the quickbet : " + quickBetVal,
							"Credits are not deducting properly after spin for the quickbet :" + quickBetVal, "fail");
				}
				// report to verify isWinAddedToCredit for the bet
				if (w != 0) {
					report.detailsAppendNoScreenshot(
							"Verify Win ammount updating properly after spin for the quickbet :" + quickBetVal,
							"Win ammount updatingproperly after spin for the quickbet : " + quickBetVal,
							"Win ammount updating properly after spin for the quickbet :" + quickBetVal, "pass");
				} else {
					report.detailsAppendNoScreenshot(
							"Verify Win ammount updating properly after spin for the quickbet :" + quickBetVal,
							"Win ammount updating properly after spin for the quickbet : " + quickBetVal,
							"Win ammount not updating properly after spin for the quickbet :" + quickBetVal, "fail");
				}
			} catch (Exception e) {
				log.error("error while taking report for the win and cridit for the bet");
				log.error(e.getMessage(), e);
				try {
					report.detailsAppendNoScreenshot("Verify all quick bet", " ",
							"Exception occur while verifying quick bet", "fail");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

		}

	}

	@Override
	public boolean verifyALLBetValues(Mobile_HTML_Report report) {

		boolean isBetVerified = false;
		String minimumbet = null;

		if (!GameName.contains("Scratch")) {
			int creditCnt = 0;
			int winCnt = 0;

			String currentBet;
			try {
				openBetPanel();
				Thread.sleep(1000);
				log.debug("Bet panael is open");
				log.debug("Setting max bet");
				// report.details_append("verify that bet panel is opened ", " Bet panel is
				// opened with screen shot", "Bet panel is opened with screen shot", "pass");
				currentBet = setMaxBet();
				Thread.sleep(1000);
				closeTotalBet();
				if ("yes".equalsIgnoreCase(xpathMap.get("IsRespinFeature"))
						&& checkAvilability(xpathMap.get("MaxBetDailoagVisible"))) {
					clickAtButton("return  " + xpathMap.get("MaxBetDailoag"));

				}

				// report.details_append("verify that bet panel is opened with max bet", " Bet
				// panel is opened with screen shot", "Bet panel is opened with screen shot",
				// "pass");
				Thread.sleep(1000);
				// closeTotalBet();

				minimumbet = getMinimumBet();
				String betValue = currentBet;

				do {
					currentBet = betValue;
					boolean isBetChangedIntheConsole = isBetChangedIntheConsole(currentBet);
					if (isBetChangedIntheConsole) {
						report.detailsAppend(
								"verify that is Bet Changed In the Console with screen shot for=" + betValue,
								" Is Bet Changed In the Console with screen shot",
								"Bet Changed In the Console with screen shot", "pass");
						log.debug("Bet Changed In the Console");
					} else {
						report.detailsAppend(
								"verify that is Bet Changed In the Console with screen shotfor=" + betValue,
								"Is Bet Changed In the Console with screen shot",
								"Bet not Changed In the Console with screen shot", "Fail");
						log.debug("Bet not Changed In the Console");
					}
					for (int spinCounter = 0; spinCounter < 10; spinCounter++) {
						String currentCreditAmount = getCurrentCredits();
						// Spin Here
						spinclick();
						// Thread.sleep(1000);
						boolean isCreditDeducted = isCreditDeducted(currentCreditAmount, currentBet);

						if (isCreditDeducted) {
							creditCnt++;
							log.debug("Credit Deducted for" + currentBet);
						} else {
							log.debug("Credit not Deducted for" + currentBet);
						}

						// lets wait till the win comes
						waitForSpinButtonstop();
						Thread.sleep(8000);
						boolean isPlayerWon = isPlayerWon();
						if (isPlayerWon) {

							boolean isWinAddedToCredit = isWinAddedToCredit(currentCreditAmount, betValue);

							if (isWinAddedToCredit) {
								winCnt++;
								log.debug("Win added to Credit for the bet : " + betValue);
							} else {
								log.debug("Win is not added to Credit for the bet : " + betValue);
							}

							break;
						} else if (spinCounter == 9) {
							try {
								report.detailsAppendNoScreenshot(
										"Verify Win ammount get update after spin for the bet :" + betValue,
										"Win ammount updating properly after spin for the quickbet : " + betValue,
										"There is no Win occur even after 10 spins , hence the following Test case of Win Amount Updation is failed."
												+ betValue,
										"fail");
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					}

					try {

						// report to verify isWinAddedToCredit for the bet
						if (winCnt != 0) {
							report.detailsAppendNoScreenshot(
									"Verify Win amount updating properly after spin for the bet :" + betValue,
									"Win ammount updatingproperly after spin for the bet : " + betValue,
									"Win ammount updating properly after spin for the bet :" + betValue, "pass");
						} else {
							report.detailsAppendNoScreenshot(
									"Verify Win ammount updating properly after spin for the bet :" + betValue,
									"Win ammount updating properly after spin for the bet : " + betValue,
									"Win amount not updating properly after spin for the bet :" + betValue, "fail");
						}
						// common report to verify isCreditDeducted for the bet
						if (creditCnt != 0) {
							report.detailsAppendNoScreenshot(
									"Verify Credits are deducting properly after spin for the bet :" + betValue,
									"Credits are deducting properly after spin for the bet : " + betValue,
									"Credits are deducting properly after spin for the bet :" + betValue, "pass");
						} else {
							report.detailsAppendNoScreenshot(
									"Verify Credits are deducting properly after spin for the bet :" + betValue,
									"Credits are not deducting properly after spin for the bet : " + betValue,
									"Credits are not deducting properly after spin for the bet :" + betValue, "fail");
						}
					} catch (Exception e) {
						log.error("error while taking report for the win and cridit for the bet");
						log.error(e.getMessage(), e);
						report.detailsAppendNoScreenshot("Verify Credits and bet after spin", " ",
								"Exception occur while verifying credit and bet ", "Fail");

					}

					if ((Class.forName(Thread.currentThread().getStackTrace()[2].getClassName())).toString()
							.contains("Modules.Regression.TestScript.Mobile_Regression_BaseScene")) {
						openBetPanel();
						Thread.sleep(1000);
						betValue = setTheNextLowBet();
						report.detailsAppend("verify that Next low level bet is selected for=" + betValue,
								" Next low level bet is selected", "Next low level bet is selected", "pass");
						Thread.sleep(2000);
						closeTotalBet();
					} else {
						break;
					}

				} while (!betValue.equalsIgnoreCase(minimumbet));
			} catch (Exception e) {

				e.printStackTrace();
				try {
					report.detailsAppendNoScreenshot("verify all bet values", " ",
							"Exception occur while verifying all bet values", "fail");
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}

		} else {
			verifyALLScratchBetValues(report);
		}

		return isBetVerified;
	}

	public String selectQuickBet(int quickBet) {

		String betAmount = null;
		try {
			openBetPanel();
			Thread.sleep(2000);
			clickAtButton("return " + forceNamespace
					+ ".getControlById('BetPanelComponent').onButtonClicked('quickBetButton" + quickBet + "')");

			if ("yes".equalsIgnoreCase(xpathMap.get("IsRespinFeature"))
					&& checkAvilability(xpathMap.get("MaxBetDailoagVisible"))) {
				clickAtButton("return  " + xpathMap.get("MaxBetDailoag"));

			}

			Thread.sleep(1000);
			betAmount = GetConsoleText("return " + xpathMap.get("BetSizeText"));
			closeTotalBet();
		} catch (InterruptedException e) {
			log.error("Exception while changing the Bet", e);
		}
		return betAmount;
	}

	@Override
	public boolean verifyAllQuickBets(Mobile_HTML_Report report) {

		long totalNoOfQuickBets = 1;

		try {
			if ((Class.forName(Thread.currentThread().getStackTrace()[2].getClassName())).toString()
					.contains("Modules.Regression.TestScript.Mobile_Regression_BaseScene"))
				totalNoOfQuickBets = getNumberOfQuickbets();
			else
				totalNoOfQuickBets = 1;
		} catch (ClassNotFoundException e1) {

			e1.printStackTrace();
		}
		if (!GameName.contains("Scratch")) {
			try {
				for (int quickBet = 0; quickBet < totalNoOfQuickBets; quickBet++) {
					int c = 0;
					int w = 0;
					String quickBetVal = selectQuickBet(quickBet);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					/*
					 * if("yes".equalsIgnoreCase(xpathMap.get("IsRespinFeature"))&&
					 * checkAvilability(xpathMap.get("MaxBetDailoagVisible"))) {
					 * clickAtButton("return  "+xpathMap.get("MaxBetDailoag"));
					 * 
					 * }
					 */

					boolean isBetChangedIntheConsole = isBetChangedIntheConsole(quickBetVal);
					try {
						if (isBetChangedIntheConsole) {
							report.detailsAppend(
									"verify that is Bet Changed In the Console for quick bet value =" + quickBetVal,
									"Is Bet Changed In the Console for quick bet value =" + quickBetVal,
									"Bet Changed In the Console", "Pass");
							log.debug("isCreditDeducted :: PASS for quick bet value =" + quickBetVal);
						} else {
							report.detailsAppend(
									"verify that is Bet Changed In the Console for quick bet value =" + quickBetVal,
									"Is Bet Changed In the Console for quick bet value =" + quickBetVal,
									"Bet not Changed In the Console", "fail");
							log.debug("isCreditDeducted :: FAil for quick bet value =" + quickBetVal);
						}
					} catch (Exception e) {
						log.error(e.getMessage(), e);
					}

					for (int spinCounter = 0; spinCounter < 10; spinCounter++) {
						String currentCreditAmount = getCurrentCredits();
						// Spin Here
						try {
							spinclick();
							// Thread.sleep(1000);
						} catch (InterruptedException e) {
							log.error(e.getMessage(), e);

							e.printStackTrace();
						}

						boolean isCreditDeducted = isCreditDeducted(currentCreditAmount, quickBetVal);
						try {
							if (isCreditDeducted) {
								c++;
								log.debug("isCreditDeducted :: PASS for quick bet value =" + quickBetVal);
							} else {
								log.debug("isCreditDeducted :: FAil for quick bet value =" + quickBetVal);
							}

						} catch (Exception e) {
							log.error(e.getMessage(), e);
						}

						// lets wait till the win comes
						waitForSpinButtonstop();
						Thread.sleep(6000);
						// waitForWinDisplay();
						boolean isPlayerWon = isPlayerWon();
						if (isPlayerWon) {
							boolean isWinAddedToCredit = isWinAddedToCredit(currentCreditAmount, quickBetVal);
							try {
								if (isWinAddedToCredit) {
									w++;
									log.debug("isWinAddedToCredit :: PASS for quick bet value =" + quickBetVal);
								} else {
									log.debug("isWinAddedToCredit :: FAil for quick bet value =" + quickBetVal);
								}
							} catch (Exception e) {
								log.error(e.getMessage(), e);
							}
							break;
						} else if (spinCounter == 9) {
							try {
								report.detailsAppendNoScreenshot(
										"Verify Win ammount get update after spin for the quickbet :" + quickBetVal,
										"Win ammount updating properly after spin for the quickbet : " + quickBetVal,
										"There is no WIn even after 10 spins" + quickBetVal, "fail");
							} catch (Exception e) {

								log.error("Exception in screen shot", e);
							}

						}
					}

					try {
						// report to verify isWinAddedToCredit for the bet
						if (w != 0) {
							report.detailsAppendNoScreenshot(
									"Verify Win ammount get update  after spin for the quickbet :" + quickBetVal,
									"Win ammount updatingproperly after spin for the quickbet : " + quickBetVal,
									"Win ammount updating  after spin for  quickbet :" + quickBetVal, "pass");
						} else {
							report.detailsAppendNoScreenshot(
									"Verify Win ammount get update after spin for the quickbet :" + quickBetVal,
									"Win ammount updating properly after spin for the quickbet : " + quickBetVal,
									"Win ammount not updating after spin for  quickbet :" + quickBetVal, "fail");
						}
						// common report to verify isCreditDeducted for the bet
						if (c != 0) {
							report.detailsAppendNoScreenshot(
									"Verify Credits get deducting after spin for the quickbet :" + quickBetVal,
									"Credits are deducting properly after spin for the quickbet : " + quickBetVal,
									"Credits are deducting properly after spin for the quickbet :" + quickBetVal,
									"pass");
						} else {
							report.detailsAppendNoScreenshot(
									"Verify Credits are deducting after spin for the quickbet :" + quickBetVal,
									"Credits are  deducting properly after spin for the quickbet : " + quickBetVal,
									"Credits are not deducting properly after spin for the quickbet :" + quickBetVal,
									"fail");
						}

					} catch (Exception e) {
						log.error("error while taking report for the win and cridit for the bet");
						log.error(e.getMessage(), e);

					}

				}
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		} else {
			verifyallScratchQuickBets(report);
		}

		return false;
	}

	/*
	 * Method to vericfy the currency format in credit
	 */

	@Override
	public boolean verifyCurrencyFormat(String currencyFormat) {
		boolean result = true;
		String strBetExp = null;
		String regexp = null;
		try {
			// Read console credits
			String consoleCredit = getCurrentCredits().replaceAll("Credits: ", "");
			consoleCredit = consoleCredit.replace("credits: ", "");
			consoleCredit = consoleCredit.replace("CREDITS: ", "");
			String betregexp = createregexp(consoleCredit, currencyFormat);

			if (currencyFormat.contains("$"))
				strBetExp = betregexp.replace("$", "\\$");
			else
				strBetExp = betregexp;
			Pattern.compile(strBetExp);
			regexp = strBetExp.replace("#", "\\d");

			if (Pattern.matches(regexp, consoleCredit)) {
				result = true;
			} else {
				result = false;
			}

		} catch (Exception e) {
			log.error(e);
		}

		return result;
	}

	// Method to count the special symbol in currency format
	public int specialSymbolCount(String currencyFormat) {
		int count = 0;

		Pattern pattern = Pattern.compile("#");
		Matcher matcher = pattern.matcher(currencyFormat);
		while (matcher.find()) {
			count++;
		}

		return count;
	}

	/*
	 * Method is use for create the regular expression
	 */
	public String createregexp(String betnew, String currencyFormat) {
		try {

			int count = specialSymbolCount(currencyFormat);
			betnew = betnew.replaceAll("[^0-9]", "");
			if (count > betnew.length()) {
				int lenght = count - betnew.length();
				for (int i = 0; i < lenght; i++) {
					for (int j = 0; j < currencyFormat.length(); j++) {
						if (currencyFormat.charAt(j) == '#') {
							StringBuilder sb = new StringBuilder();
							sb.append(currencyFormat);
							if (currencyFormat.charAt(j + 1) == ',' || currencyFormat.charAt(j + 1) == '.'
									|| currencyFormat.charAt(j + 1) == ' ' || currencyFormat.charAt(j + 1) == 160) {
								sb.deleteCharAt(j + 1);
							}
							sb.deleteCharAt(j);
							currencyFormat = sb.toString();
							break;
						}
					}
				}

			}
		} catch (Exception e) {
			log.error(e);
		}
		return currencyFormat;
	}

	/*
	 * Method to check the currency format in free spin
	 */
	@Override
	public boolean freeSpinWinSummaryCurrencyFormat(String currencyFormat) {
		String freespinwin = null;
		String strfreespinwinExp = null;
		boolean isWinInCurrencyFormat = false;
		try {
			if (!GameName.contains("Scratch")) {
				freespinwin = "return " + xpathMap.get("FSSumaryAmount");
			} else {
				// need to update the hook for Scratch game
				freespinwin = "return " + xpathMap.get("InfobarBettext");
			}
			String freespinwinnew = GetConsoleText(freespinwin);

			// To validate currency Amount format
			isWinInCurrencyFormat = currencyFormatValidator(freespinwinnew, currencyFormat);

			/*
			 * String freespinregexp=createregexp(freespinwinnew,currencyFormat);
			 * if(freespinregexp.contains("$"))
			 * strfreespinwinExp=freespinregexp.replace("$", "\\$"); else
			 * strfreespinwinExp=freespinregexp; Pattern.compile(strfreespinwinExp); String
			 * winexp=strfreespinwinExp.replace("#", "\\d");
			 * 
			 * 
			 * if(Pattern.matches(winexp,freespinwinnew)) { isWinInCurrencyFormat=true; }
			 * else{ isWinInCurrencyFormat=false; }
			 */

			log.debug("Fetching bet currency symbol" + "/n bet currency symbol is::" + freespinwin);
		} catch (Exception e) {
			log.error("Error in Fetching currency symbol", e);
		}
		return isWinInCurrencyFormat;

	}

	/*
	 * This method is use to verify the paytable currency with minimum bet and with
	 * maximum bet value
	 */
	@Override
	public void verifyPaytableCurrency(Mobile_HTML_Report currencyReport, String currencyName) {

		try {
			// set bet value to minimum and take screen shot of paytable to check the
			// alignment
			setMinBet();
			capturePaytableScreenshot(currencyReport, currencyName);
			paytableClose();
			setMaxBet();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				log.debug(e.getMessage(), e);
			}
			if ("yes".equalsIgnoreCase(xpathMap.get("IsRespinFeature"))
					&& checkAvilability(xpathMap.get("MaxBetDailoagVisible"))) {
				clickAtButton("return  " + xpathMap.get("MaxBetDailoag"));

			}
			capturePaytableScreenshot(currencyReport, currencyName);
			paytableClose();
		} catch (WebDriverException e) {
			log.debug(e);
		}

	}

	/**
	 * * Date: 29/05/2018 Author: Premlata Mishra Description: This function is used
	 * to set the bet to minimum Parameter:
	 */
	public void setMinBet() {
		try {
			// String bet ="return
			// "+forceNamespace+".getServiceById('BetService').betModel.setMaxBet()";
			String bet = "return " + xpathMap.get("SetMinBet");
			GetConsoleText(bet);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	/*
	 * Method to close the paytable
	 */
	@Override
	public void paytableClose() {
		Wait = new WebDriverWait(webdriver, 100);
		try {
			clickAtButton("return " + xpathMap.get("PaytableCloseBtn"));
			log.debug("Closed the paytable page");
		} catch (Exception e) {
			log.debug("error in closing paytable");
		}
	}

	@Override
	public boolean isBetChangedOnRefresh() {

		boolean isBetChanged = true;
		try {
			String currentBet = GetConsoleText("return " + xpathMap.get("BetSizeText"));

			setMaxBet();

			if ("yes".equalsIgnoreCase(xpathMap.get("IsRespinFeature"))
					&& checkAvilability(xpathMap.get("MaxBetDailoagVisible"))) {
				clickAtButton("return  " + xpathMap.get("MaxBetDailoag"));

			}

			GetConsoleText("return " + xpathMap.get("BetSizeText"));

			webdriver.navigate().refresh();
			if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame"))
					|| (Constant.YES.equalsIgnoreCase(xpathMap.get("continueBtnOnGameLoad")))) {
				newFeature();
				Thread.sleep(1000);
				funcFullScreen();
			} else {
				// to remove hand gesture
				funcFullScreen();
				Thread.sleep(1000);
				newFeature();
			}

			String betAferRefresh = GetConsoleText("return " + xpathMap.get("BetSizeText"));

			if (currentBet.equalsIgnoreCase(betAferRefresh)) {
				isBetChanged = false;
			}

		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return isBetChanged;
	}

	@Override
	public boolean validateMiniPaytable(String betValue, Mobile_HTML_Report report) {

		long symbolNumber;
		String strPaylineCost = xpathMap.get("DefaultPayLineCost");// this code will fetch the data from sheet
		double payLineCost = Double.parseDouble(strPaylineCost);
		double dblBetValue = Double.parseDouble(betValue.replaceAll("[^0-9]", ""));

		Util util = new Util();
		int length = util.xmlLength("./" + GameName + "/Config/" + GameName + ".xml", "WinningCombinations");

		ArrayList<String> symbolData = util.getXMLDataInArray("Symbol", "name",
				"./" + GameName + "/Config/" + GameName + ".xml");

		ArrayList<String> symbolArray = new ArrayList<>();
		ArrayList<String> proceedSymbolArray = new ArrayList<>();

		HashMap<String, Symbol> symbolmMap = new HashMap<>();

		int totalNoOfSymbols = symbolData.size();
		log.debug("totalNoOfSymbols: " + totalNoOfSymbols);

		for (int count = 0; count < totalNoOfSymbols; count++) {
			String[] xmlData = symbolData.get(count).split(",");
			String symbolName = xmlData[0];
			symbolArray.add(count, symbolName);
			proceedSymbolArray.add(count, symbolName);
			symbolmMap.put(symbolName, new Symbol(symbolName));
		}

		for (int count = 0; count < length; count++) {
			String strWinCombination = util.readXML("WinCombination", "numSymbolsRequired", "symbols", "payouts",
					"./" + GameName + "/Config/" + GameName + ".xml", length + 2, count);
			if (strWinCombination != null) {
				WinCombination winCombination = new WinCombination();

				String[] xmlData = strWinCombination.split(",");

				winCombination.setNumSymbolsRequired(Integer.parseInt(xmlData[0].trim()));
				winCombination.setSymbols(xmlData[1]);
				winCombination.setPayout(Integer.parseInt(xmlData[2].trim()));

				symbolmMap.get(xmlData[1]).addWinComb(winCombination);
			}
		}

		proceedSymbolArray.remove("FS_Scatter");
		proceedSymbolArray.remove("WildMagic");
		log.debug("Total  Symbols::" + proceedSymbolArray);
		do {
			for (int column = 0; column <= 4; column++) {
				for (int row = 1; row <= 3; row++) {

					Map<String, String> paramMap = new HashMap<>();
					paramMap.put("param1", Integer.toString(column));

					paramMap.put("param2", Integer.toString(row));

					String symbolNumberHook = xpathMap.get("getSymbolNumber");
					String newHook = replaceParamInHook(symbolNumberHook, paramMap);
					symbolNumber = getConsoleNumeric("return " + newHook);

					String symbolName = symbolArray.get((int) symbolNumber);
					if (proceedSymbolArray.contains(symbolName)) {
						String symbolClickHook = xpathMap.get("clickOnSymbol");
						newHook = replaceParamInHook(symbolClickHook, paramMap);
						clickAtButton("return " + newHook);

						try {
							Thread.sleep(2000);
							report.detailsAppendFolderOnlyScreeshot("Minipaytable-" + betValue);
						} catch (Exception e1) {
							log.error("validateMiniPaytable screenshot", e1);
						}

						for (int count = 2; count < 6; count++) {
							String payoutValue = xpathMap.get("getPayoutValue");
							log.debug("payoutValue " + payoutValue);
							paramMap.put("param1", Integer.toString(count));
							newHook = replaceParamInHook(payoutValue, paramMap);
							log.debug("newhook " + newHook);
							String paytableValue = getConsoleText("return " + newHook);
							log.debug("paytable value " + paytableValue);

							ArrayList<WinCombination> symWinCombs = symbolmMap.get(symbolName).getWinCombList();
							int payout = 0;
							double calcPayout = 0;
							for (WinCombination winCombination : symWinCombs) {
								if (winCombination.getNumSymbolsRequired() == count) {
									payout = winCombination.getPayout();
								}
							}

							if (payout != 0 && "".equalsIgnoreCase(paytableValue)) {
								log.debug("Fail for Symbol" + symbolName + " and count ::" + count);
								log.debug("symbolName " + symbolName + "  payout " + payout + "  paytableValue "
										+ paytableValue);
							} else if (payout == 0 && !"".equalsIgnoreCase(paytableValue)) {
								log.debug("Fail for Symbol" + symbolName + " and count ::" + count);
								log.debug("symbolName " + symbolName + "  payout " + payout + "  paytableValue "
										+ paytableValue);
							} else if (payout == 0 && "".equalsIgnoreCase(paytableValue)) {
								log.debug("Fail There is no paayout for Symbol" + symbolName + " and count ::" + count);
								log.debug("symbolName " + symbolName + "  payout " + payout + "  paytableValue "
										+ paytableValue);
							} else {
								double dblpaytableValue = Double.parseDouble(paytableValue.replaceAll("[^0-9]", ""));

								if (symbolName != null && (symbolName.contains("scatter")
										|| symbolName.contains("Scatter") || symbolName.contains("FreeSpin"))) {
									calcPayout = (dblBetValue * payout);
								} else {

									calcPayout = (dblBetValue * payout) / payLineCost;
								}

								try {
									if (dblpaytableValue == calcPayout) {

										report.detailsAppendNoScreenshot(
												"Minipaytable:: Verify the payout for symbol" + symbolName,
												"Payout from paytable and xml should be equal, payout must be ::"
														+ calcPayout,
												"Payout from paytable and xml is equal for symbol " + symbolName
														+ " is correct which is " + dblpaytableValue,
												"pass");

										log.debug("PASS for Symbol" + symbolName + " and count ::" + count);
										log.debug("symbolName " + symbolName + "payout " + payout + "paytableValue "
												+ paytableValue);
									} else {

										report.detailsAppendNoScreenshot(
												"Minipaytable:: Verify the payout for symbol" + symbolName,
												"Payout from paytable and xml should be equal, payout must be ::"
														+ calcPayout,
												"Payout from paytable and xml is not equal for symbol " + symbolName
														+ " is not correct which is " + dblpaytableValue,
												"Fail");

									}
								} catch (Exception e) {

									log.error(e);
								}
							}

						}
						proceedSymbolArray.remove(symbolName);
						log.debug("Remaining Symbols::" + proceedSymbolArray);

					}

				}
			}
			if (proceedSymbolArray.size() > 0) {
				try {
					spinclick();
				} catch (InterruptedException e) {
					log.error("validateMiniPaytable", e);
				}
				waitForSpinButtonstop();
				threadSleep(7000);
			}

		} while (proceedSymbolArray.size() > 0);
		return false;
	}

	@Override
	public void verifyMenuOptionNavigations(Mobile_HTML_Report report) {
		try {
			boolean menuOpen = menuOpen();
			if (menuOpen) {
				report.detailsAppend("Verify that Navigation of menu link", "Navigation of menu link",
						"Navigation of menu link done", "pass");
			} else {
				report.detailsAppend("Verify that Navigation of menu link", "Navigation of menu link",
						"Navigation of menu link not done", "fail");
			}
			// verify navigating to banking page.
			/*
			 * boolean banking = CheckNavigateGameToBanking(report); if(banking){
			 * log.debug("Banking navigation verified succesfully"); } else {
			 * report.details_append("Verify that Navigation on banking screen",
			 * "Navigation on banking screen", "Navigation on banking screen not Done",
			 * "fail"); }
			 */

			// newFeature();

			// navigation from game to setting page
			verifysettingsOptions(report);
			settingsBack();

			// navigation from game to paytable
			if (!GameName.contains("Scratch")) {
				verifypaytablenavigation(report);
			} else {
				log.debug("Paytable is not present in the game seprately");
			}
			if (!Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame"))) {
				// verify help page navigation from game
				boolean help = verifyhelpenavigation(report);
				if (help) {
					log.debug("Help navigation verified succesfully");
				} else {
					report.detailsAppend("Verify that Navigation on Help screen", "Navigation on Help screen",
							"Navigation on Help screen not Done", "fail");
				}
				// verify navigation to responsible gaming from game menu
				boolean respgame = verifyresponsiblegamingenavigation(report);
				if (respgame) {
					log.debug("Responsible gameing navigation verified succesfully");
				} else {
					report.detailsAppend("Verify that Navigation on ResponsibleGaming screen ",
							"Navigation on ResponsibleGaming screen", "Navigation on ResponsibleGaming screen not Done",
							"fail");
				}

				// verify navigation to playcheck gaming from game menu
				boolean playcheck = verifyplaychecknavigation(report);
				if (playcheck) {
					log.debug("Navigation on playcheck succesfully");
				} else {
					report.detailsAppend("Verify that Navigation on playcheck", "Navigation on playcheck",
							"Navigation on playcheck not Done", "fail");
				}

				// verify navigation to cashCheckButton gaming from game menu
				boolean cashCheckButton = verifycashChecknavigation(report);
				if (cashCheckButton) {
					log.debug("Navigated to Cashcheck page succesfully");
				} else {
					report.detailsAppend("Verify that Navigation on cashCheck", "Navigation on cashCheck",
							"Navigation on cashCheck done", "fail");
				}
				//// verify navigation to loyaltyButton gaming from game menu
				boolean loyaltyButton = verifyloyaltynavigation(report);
				if (loyaltyButton) {
					log.debug("Navigated to Loyalty page succesfully");
				} else {
					report.detailsAppend("Verify that Navigation on loyalty", "Navigation on loyalty",
							"Navigation on loyalty is not Done", "fail");
				}
			}
			// verify navigation to lobby gaming from game menu
			/*
			 * boolean lobbyButton = verifylobbynavigation(); if(lobbyButton){
			 * report.details_append("Verify that Navigation on lobby screen",
			 * "Navigation on lobby screen","Navigation on lobby screen Done","pass"); }
			 * else { report.details_append("Verify that Navigation on lobby screen",
			 * "Navigation on lobby screen","Navigation on lobby screen not Done","fail"); }
			 */
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				report.detailsAppendNoScreenshot("Verify Menu options", "",
						"Exception occur while verifying menu options", "fail");
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}
	}

	/*
	 * To verify the help gaming links
	 * 
	 * public boolean verifyHelpNavigation(Mobile_HTML_Report report) { boolean
	 * ret=false; Wait=new WebDriverWait(webdriver,500); try { menuOpen();
	 * Thread.sleep(1000); clickAtButton("return "+forceNamespace+
	 * ".getControlById('MenuIconPanelComponent').onButtonClicked('helpButton')");
	 * log.debug("Clicked on help button  in menu"); Thread.sleep(2000); String
	 * tital="Casino Help"; checkPageNavigation(report,tital);
	 * log.debug("Game navigated to help link  verified"); ret = true; } catch
	 * (Exception e) { log.error("Error in navigetion to help link  page ", e); }
	 * return ret; }
	 */
	public boolean CheckNavigateGameToBanking(Mobile_HTML_Report report, String classname) {
		Wait = new WebDriverWait(webdriver, 20);
		boolean ret = false;
		try {
			String gameurl = webdriver.getCurrentUrl();
			boolean flag = getConsoleBooleanText("return " + xpathMap.get("isMenuDepositBtnVisible"));
			if (flag) {
				clickAtButton("return " + xpathMap.get("MenuDepositBtn"));
				log.debug("Clicked on banking button to open in menu");
				long start = System.currentTimeMillis();
				String navigetedurl = webdriver.getCurrentUrl();
				Thread.sleep(3000);
				long finish = System.currentTimeMillis();
				long totalTime = finish - start;
				double loadingtime = totalTime / 1000;
				log.debug("Game navigeted to banking page in " + loadingtime + " Seconds");
				// verify that is navigated to banking page or not.
				if (navigetedurl.contains("Banking") && loadingtime < 30.00) {
					Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='button']")));
					log.debug("Game navigated to banking page.");
					// report Pass if banking navigation done
					report.detailsAppend("verify the Navigation screen shot to the banking page",
							" Navigation page screen shot for the banking page",
							"Navigation page screenshot for the banking page ", "pass");
					Thread.sleep(2000);
					WebElement ele = webdriver.findElement(By.xpath("//*[@class='button']"));
					ele.click();
					log.debug("cliked on back to game from navigation");
					webdriver.navigate().to(gameurl);
					waitForSpinButton();
					// Pass if back to game page successfully from banking navigation
					report.detailsAppend("verify that banking page navigated successfully back to the game",
							" navigated successfully back to the game", "navigated successfully back to the game",
							"pass");
					ret = true;
				} else {
					log.debug("Game has taken more 30 secondos to navigate on banking page");
					// fail due to navigation not done in 30 seconds
					report.detailsAppend("verify the Navigation screen shot to the banking page",
							" Navigation page for the banking page not has fail due to not navigated in 30 seconds",
							"Navigation page for the banking page not has fail due to not navigated in 30 seconds ",
							"fail");
					Thread.sleep(2000);
					WebElement ele = webdriver.findElement(By.xpath("//*[@class='button']"));
					ele.click();
					log.debug("cliked on back to game from navigation");
					webdriver.navigate().to(gameurl);
					waitForSpinButton();
					// Pass if back to game page successfully from banking navigation
					report.detailsAppend("verify that banking page navigated successfully back to the game",
							" navigated successfully back to the game", "navigated successfully back to the game",
							"pass");
					return ret;
				}
			} else {
				log.debug("Banking page option is not Available in game");
				return ret;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.error("Error in navigetion to banking page ", e);
		}
		return ret;
	}

	public void checkpagenavigation(Mobile_HTML_Report report, String gameurl) {
		try {
			String mainwindow = webdriver.getWindowHandle();
			Set<String> s1 = webdriver.getWindowHandles();
			if (s1.size() > 1) {

				Iterator<String> i1 = s1.iterator();
				while (i1.hasNext()) {
					String ChildWindow = i1.next();

					if (mainwindow.equalsIgnoreCase(ChildWindow)) {
						System.out.println("Page navigated succesfully");
						if (osPlatform.equalsIgnoreCase("Android")) {
							ChildWindow = i1.next();
						}
						// Thread.sleep(2000);
						webdriver.switchTo().window(ChildWindow);
						String url = webdriver.getCurrentUrl();
						System.out.println("Navigation URL1 is :: " + url);
						log.debug("Navigation URL is :: " + url);
						if (!url.equalsIgnoreCase(gameurl)) {
							// pass condition for navigation
							report.detailsAppend("verify the Navigation screen shot", " Navigation page screen shot",
									"Navigation page screenshot ", "PASS");
							System.out.println("Page navigated succesfully");
							log.debug("Page navigated succesfully");
							webdriver.close();
						} else {
							System.out.println("Now On game page");
							log.debug("Now On game page");
							funcFullScreen();
						}
					}
				}
				webdriver.switchTo().window(mainwindow);
			} else {
				String url = webdriver.getCurrentUrl();
				System.out.println("Navigation URL is ::  " + url);
				log.debug("Navigation URL is ::  " + url);
				if (!url.equalsIgnoreCase(gameurl)) {
					// pass condition for navigation
					report.detailsAppend("verify the Navigation screen shot", " Navigation page screen shot",
							"Navigation page screenshot ", "PASS");
					System.out.println("Page navigated succesfully");

					webdriver.navigate().to(gameurl);
					waitForSpinButton();
					newFeature();
				} else {
					report.detailsAppend("verify the Navigation screen shot", " Navigation page screen shot",
							"Navigation page screenshot ", "PASS");
					webdriver.navigate().to(gameurl);
					waitForSpinButton();
					System.out.println("Now On game page");
					log.debug("Now On game page");
				}
			}

		} catch (Exception e) {
			log.error("error in navigation of page");
		}
	}

	@Override
	public boolean verifyhelpenavigation(Mobile_HTML_Report report) {
		boolean ret = false;
		Wait = new WebDriverWait(webdriver, 500);
		try {
			String gameurl = webdriver.getCurrentUrl();
			menuOpen();
			boolean flag = getConsoleBooleanText("return " + xpathMap.get("isMenuHelpBtnVisible"));
			if (flag) {
				clickAtButton("return " + xpathMap.get("MenuHelpBtn"));
				log.debug("Clicked on Help button to open in menu");
				Thread.sleep(3000);
				// Pass
				checkpagenavigation(report, gameurl);
				System.out.println("Game navigated to Help");
				ret = true;
			} else {
				log.debug("Help navigation option is not available in menu options");
				return ret;
			}

			loadGame(gameurl);
			if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame"))
					|| (Constant.YES.equalsIgnoreCase(xpathMap.get("continueBtnOnGameLoad")))) {
				newFeature();
				Thread.sleep(1000);
				funcFullScreen();
			} else {
				// to remove hand gesture
				funcFullScreen();
				Thread.sleep(1000);
				newFeature();
			}
			waitForSpinButton();
		} catch (Exception e) {
			log.error("Error in navigetion to help page ", e);
		}
		return ret;
	}

	@Override
	public boolean verifyresponsiblegamingenavigation(Mobile_HTML_Report report) {
		boolean ret = false;
		Wait = new WebDriverWait(webdriver, 500);
		try {
			String gameurl = webdriver.getCurrentUrl();
			menuOpen();
			boolean flag = getConsoleBooleanText("return " + xpathMap.get("isMenuResponsibleBtnVisible"));
			if (flag) {
				clickAtButton("return " + xpathMap.get("MenuResponsibleGamingBtn"));
				log.debug("Clicked on ResponsibleGameing button to open in menu");
				Thread.sleep(3000);
				// Pass
				checkpagenavigation(report, gameurl);
				System.out.println("Game navigated to ResponsibleGameing verified");
				ret = true;
			} else {
				log.debug("Responsible Gaming navigation option is not available in menu options");
				return ret;
			}
			webdriver.navigate().to(gameurl);
			waitForSpinButton();
		} catch (Exception e) {
			log.error("Error in navigetion to ResponsibleGameing  page ", e);
		}
		return ret;
	}

	@Override
	public boolean verifyplaychecknavigation(Mobile_HTML_Report report) {
		boolean ret = false;
		Wait = new WebDriverWait(webdriver, 500);
		try {
			String gameurl = webdriver.getCurrentUrl();
			menuOpen();
			boolean flag = getConsoleBooleanText("return " + xpathMap.get("isMenuplayCheckBtnVisible"));
			if (flag) {
				clickAtButton("return " + xpathMap.get("MenuPlayCheckBtn"));
				log.debug("Clicked on Playcheck button to open in menu");
				Thread.sleep(3000);
				// Pass
				checkpagenavigation(report, gameurl);
				System.out.println("Game navigated to palycheck verified");
				ret = true;
			} else {
				log.debug("playchecknavigation option is not available in menu options");
				return ret;
			}
			webdriver.navigate().to(gameurl);
			funcFullScreen();
			newFeature();
			waitForSpinButton();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.error("Error in navigetion to palycheck  page ", e);
		}
		return ret;
	}

	@Override
	public boolean verifycashChecknavigation(Mobile_HTML_Report report) {
		boolean ret = false;
		Wait = new WebDriverWait(webdriver, 500);
		try {
			String gameurl = webdriver.getCurrentUrl();
			menuOpen();
			boolean flag = getConsoleBooleanText("return " + xpathMap.get("isMenuCashCheckBtnVisible"));
			if (flag) {
				clickAtButton("return " + xpathMap.get("MenuCashCheckBtn"));
				log.debug("Clicked on cashCheck button to open in menu");
				Thread.sleep(5000);
				// Pass
				checkpagenavigation(report, gameurl);
				System.out.println("Game navigated to cashCheck verified");
				ret = true;
			} else {
				log.debug("cashChecknavigation option is not available in menu options");
				return ret;
			}
			webdriver.navigate().to(gameurl);
			funcFullScreen();
			newFeature();
			waitForSpinButton();
		} catch (Exception e) {
			log.error("Error in navigetion to cashCheck  page ", e);
		}
		return ret;
	}

	/**
	 * Open the settings tab and verify the options in it Date:07/12/2017
	 * Author:Sneha Jawarkar This method is verifysettingsOptions
	 */
	@Override
	public boolean verifysettingsOptions(Mobile_HTML_Report report) throws InterruptedException {
		boolean test = false;
		Wait = new WebDriverWait(webdriver, 50);
		try {
			// menuOpen();
			boolean flag = getConsoleBooleanText("return " + xpathMap.get("isMenuOptionBtnVisible"));
			if (flag) {
				clickAtButton("return " + xpathMap.get("SettingOpenBtn"));
				log.debug("Clicked on settings button to open and verify");
				// pass sceenshot for the setting to be opened successfully
				report.detailsAppend("verify the Navigation screen shot for setting options from game menu",
						" Navigation page screen shot for setting options from game menu",
						"Navigation page screenshot for setting options from game menu ", "pass");
				boolean soundpresent = getConsoleBooleanText("return " + xpathMap.get("isMenuSoundBtnVisible"));
				if (soundpresent) {
					boolean soundflag = getConsoleBooleanText("return " + xpathMap.get("isSettingsSoundBtnActive"));
					if (soundflag) {
						// sceenshot for sound button with on state
						report.detailsAppend("verify the sound button is on in setting options ",
								"The sound button is on in setting option ", "The sound button is on in setting option",
								"pass");
					} else {
						// verify setting off State
						report.detailsAppend("verify the sound button is off setting option",
								" verify the sound button is off setting optionu",
								"verify the sound button is off setting option", "pass");
						clickAtButton("return " + xpathMap.get("ClickSettingsSoundBtn"));
						// sceenshot for sound button with on state
						report.detailsAppend("verify the sound button is on in setting options ",
								"The sound button is on in setting option ", "The sound button is on in setting option",
								"pass");
					}
				} else {
					log.info("sound  toggele button is not avilable in menu setting");
				}
				// Pass for Quickspin to be verify
				boolean quickspinpresent = getConsoleBooleanText(
						"return " + xpathMap.get("isSettingsQuickSpinBtnVisible"));
				if (quickspinpresent) {
					boolean quickspinflag = getConsoleBooleanText(
							"return " + xpathMap.get("isSettingsQuickSpinBtnActive"));
					if (quickspinflag) {
						// sceenshot for sound button with on state
						report.detailsAppend("verify the Quickspin button is on in setting options ",
								"The Quickspin button is on in setting option ",
								"The Quickspin button is on in setting option", "pass");
					} else {
						// verify quickspin
						report.detailsAppend("verify the Quickspin button is off setting option",
								" verify the Quickspin button is off setting optionu",
								"verify the Quickspin button is off setting option", "pass");
						clickAtButton("return " + forceNamespace
								+ ".getControlById('GameSettingsComponent').Buttons.quickSpinButton.onClickDown()");
						// sceenshot for sound button with on state
						report.detailsAppend("verify the Quickspin button is on in setting options ",
								"The Quickspin button is on in setting option ",
								"The Quickspin button is on in setting option", "pass");
					}

				} else {
					log.info("Quick spin toggele button is not avilable in menu setting");
				}
				// Pass for Turbo to be verify
				boolean Turbopresent = getConsoleBooleanText("return " + xpathMap.get("isSettingsturboBtnVisible"));
				if (Turbopresent) {
					boolean Turboflag = getConsoleBooleanText("return " + xpathMap.get("isSettingsturboBtnActive"));
					if (Turboflag) {
						// sceenshot for sound button with on state
						report.detailsAppend("verify the Turbo button is on in setting options ",
								"The Turbo button is on in setting option ", "The Turbo button is on in setting option",
								"pass");
					} else {
						// verify setting off State
						report.detailsAppend("verify the Turbo button is off setting option",
								" verify the Turbo button is off setting optionu",
								"verify the Turbo button is off setting option", "pass");
						clickAtButton("return " + forceNamespace
								+ ".getControlById('GameSettingsComponent').Buttons.turboButton.onClickDown()");
						// sceenshot for sound button with on state
						report.detailsAppend("verify the Turbo button is on in setting options ",
								"The Turbo button is on in setting option ", "The Turbo button is on in setting option",
								"pass");
					}
				} else {
					log.info("Turbo toggle button is not available in menu  setting ");
				}

				// Pass for ShowMiniPaytables to be verify
				boolean ShowMiniPaytablespresent = getConsoleBooleanText(
						"return " + xpathMap.get("isSettingsMiniPaytableBtnActive"));
				if (ShowMiniPaytablespresent) {
					boolean Turboflag = getConsoleBooleanText("return " + xpathMap.get("isSettingsPaytableBtnActive"));
					if (Turboflag) {
						// sceenshot for sound button with on state
						report.detailsAppend("verify the ShowMiniPaytables button is on in setting options ",
								"The ShowMiniPaytables button is on in setting option ",
								"The ShowMiniPaytables button is on in setting option", "pass");
					} else {
						// verify setting off State
						report.detailsAppend("verify the ShowMiniPaytables button is off setting option",
								" verify the ShowMiniPaytables button is off setting optionu",
								"verify the ShowMiniPaytables button is off setting option", "pass");
						clickAtButton("return " + xpathMap.get("ClickSettingsMiniPaytableBtn"));
						// sceenshot for sound button with on state
						report.detailsAppend("verify the ShowMiniPaytables button is on in setting options ",
								"The ShowMiniPaytables button is on in setting option ",
								"The ShowMiniPaytables button is on in setting option", "pass");
					}
				} else {
					log.info("Show minitable toggel button is not available in menu setting");
				}
				log.debug("Setting to open succesfully");
				test = true;
			} else {
				log.debug("Setting option is not available in game menu");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.error("Error in opening setting button", e);
			try {
				report.detailsAppend("verify  setting options ", " ", "Exception occur while verifying setting option",
						"Fail");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		return test;
	}

	/**
	 * Date:07/12/2017 Author:Sneha Jawarkar This method is verifypaytablenavigation
	 */
	@Override
	public void paytablenavigationClose() {
		try {
			clickAtButton("return " + xpathMap.get("PaytableCloseBtn"));
			log.debug("Closed the paytable page");
		} catch (Exception e) {
			log.debug("error in closing paytable");
		}
	}

	@Override
	public boolean verifypaytablenavigation(Mobile_HTML_Report report) {
		boolean paytable = false;
		try {
			boolean flag = getConsoleBooleanText("return " + xpathMap.get("isMenuPaytableBtnVisible"));
			if (flag) {
				clickAtButton("return " + xpathMap.get("PaytableOpenBtn"));
				log.debug("Clicked on paytable icon to open ");
				Thread.sleep(2000);
				String paytablestatus = "return " + xpathMap.get("isPaytableVisible");
				boolean status = getConsoleBooleanText(paytablestatus);
				System.out.println("paytable open = " + status);
				if (status) {
					System.out.println("Game navigated to paytable page succesfully");
					// pass
					report.detailsAppend("Verify that Navigation on paytable screen is done ",
							"Language in paytable Screen should be done", "Language inside paytable screens is done",
							"pass");
					paytablenavigationClose();
					paytable = true;
				} else {
					log.debug("Game is not navigated to paytable");
					// fail
					report.detailsAppend("Verify that Navigation on paytable screen is done ",
							"Language in paytable Screen should be done",
							"Language inside paytable screens is not done", "fail");

				}
			} else {
				log.debug("Paytable option is not available in menu options");
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.error("error in opening paytable", e);
		}
		return paytable;
	}

	@Override
	public boolean verifyloyaltynavigation(Mobile_HTML_Report report) {
		boolean ret = false;
		Wait = new WebDriverWait(webdriver, 500);
		try {
			String gameurl = webdriver.getCurrentUrl();
			menuOpen();
			boolean flag = getConsoleBooleanText("return " + xpathMap.get("isMenuLoyalityBtnVisible"));
			if (flag) {
				clickAtButton("return " + xpathMap.get("MenuLoyaltyBtn"));
				log.debug("Clicked on loyalty button to open in menu");
				Thread.sleep(3000);
				// Pass
				report.detailsAppend("verify the Navigation screen shot for loyalty",
						" Navigation page screen shot for loyalty", "Navigation page screenshot for loyalty", "pass");
				checkpagenavigation(report, gameurl);
				funcFullScreen();
				System.out.println("Game navigated to loyalty verified");
				ret = true;
			} else {
				log.debug("loyalty option is not available in menu options");
			}
		} catch (Exception e) {
			log.error("Error in navigetion to loyalty  page ", e);
		}
		return ret;
	}

	@Override
	public boolean verifylobbynavigation() {
		boolean ret = false;
		Wait = new WebDriverWait(webdriver, 500);
		try {
			menuOpen();
			boolean flag = getConsoleBooleanText("return " + xpathMap.get("isMenulobbyBtnVisible"));
			if (flag) {
				String currenturl = webdriver.getCurrentUrl();
				System.out.println("Game url is =" + currenturl);

				clickAtButton("return " + xpathMap.get("MenuLobbyBtn"));
				log.debug("Clicked on lobby button to open in menu");
				Thread.sleep(3000);
				// verify lobby page
				log.debug("navigated the url and  waiting for five_Reel_slot to be visible");
				Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("five_Reel_slot"))));
				System.out.println("navigated the lobby from game succesfully");
				ret = true;
			} else {
				log.debug("Lobby navigation option is not available in menu options");
				return ret;
			}
		} catch (Exception e) {
			log.error("Error in navigetion to lobby  page ", e);
		}
		return ret;
	}

	/*
	 * Sneha Jawarkar: Wait for big win layers screen
	 * 
	 */
	@Override
	public boolean waitForbigwin() {
		boolean result = false;
		long startTime = System.currentTimeMillis();
		try {
			log.debug("Waiting for bigwinlayers to come after complition of spin");
			Thread.sleep(1000);
			while (true) {

				// Boolean currentSceneflag = GetConsoleBooleanText("return
				// "+forceNamespace+".getControlById('TieredBigWinComponent').Animations.bigWinAnimation.visible");
				Boolean currentSceneflag = getConsoleBooleanText("return " + xpathMap.get("isTieredBigWinTextVisible"));
				System.out.println("Bigwin popup present = " + currentSceneflag);
				if (currentSceneflag) {
					System.out.println("Bigwin popup present = " + currentSceneflag);
					result = true;
					Thread.sleep(1000);
					break;
				} else {
					long currentime = System.currentTimeMillis();
					if (((currentime - startTime) / 1000) > 30) {
						System.out.println("No big win present after 30 seconds= " + currentSceneflag);
						result = false;
						break;
					}

				}
			}

		} catch (Exception e) {
			log.error("error while waiting for big win layers", e);
		}

		return result;
	}

	@Override
	public void miniPaytableScreeShots(Mobile_HTML_Report currencyReport, String currencyName) {

		String strGameName = null;
		int startindex = 0;
		long symbolNumber;
		if (GameName.contains("Desktop")) {
			java.util.regex.Pattern str = java.util.regex.Pattern.compile("Desktop");

			Matcher substing = str.matcher(GameName);

			while (substing.find()) {
				startindex = substing.start();
			}
			strGameName = GameName.substring(0, startindex);
			log.debug("newgamename=" + strGameName);
		} else {
			strGameName = GameName;
		}
		Util util = new Util();
		int length = util.xmlLength("./" + GameName + "/Config/" + GameName + ".xml", "WinningCombinations");

		ArrayList<String> symbolData = util.getXMLDataInArray("Symbol", "name",
				"./" + GameName + "/Config/" + GameName + ".xml");

		ArrayList<String> symbolArray = new ArrayList<>();
		ArrayList<String> proceedSymbolArray = new ArrayList<>();

		HashMap<String, Symbol> symbolmMap = new HashMap<String, Symbol>();

		int totalNoOfSymbols = symbolData.size();

		try {

			for (int count = 0; count < totalNoOfSymbols; count++) {
				String[] xmlData = symbolData.get(count).split(",");
				String symbolName = xmlData[0];
				symbolArray.add(count, symbolName);
				proceedSymbolArray.add(count, symbolName);
				symbolmMap.put(symbolName, new Symbol(symbolName));
			}

			for (int count = 0; count < length; count++) {
				String strWinCombination = util.readXML("WinCombination", "numSymbolsRequired", "symbols", "payouts",
						"./" + GameName + "/Config/" + GameName + ".xml", length + 2, count);
				if (strWinCombination != null) {
					WinCombination winCombination = new WinCombination();

					String[] xmlData = strWinCombination.split(",");

					winCombination.setNumSymbolsRequired(Integer.parseInt(xmlData[0].trim()));
					winCombination.setSymbols(xmlData[1]);
					winCombination.setPayout(Integer.parseInt(xmlData[2].trim()));

					symbolmMap.get(xmlData[1]).addWinComb(winCombination);
				}
			}
			Thread.sleep(500);

			proceedSymbolArray.remove("FS_Scatter");
			proceedSymbolArray.remove("WildMagic");

			do {

				String currentScene = GetConsoleText("return " + xpathMap.get("currentScene"));
				System.out.println("currentScene :: " + currentScene);
				if (currentScene != null
						&& ((!currentScene.equalsIgnoreCase("SLOT")) || (!currentScene.equalsIgnoreCase("BASE")))) {
					break;
				}

				for (int column = 0; column <= 4; column++) {
					for (int row = 1; row <= 3; row++) {
						Map<String, String> paramMap = new HashMap<String, String>();
						paramMap.put("param1", Integer.toString(column));
						paramMap.put("param2", Integer.toString(row));

						String symbolNumberHook = xpathMap.get("getSymbolNumber");
						String newHook = replaceParamInHook(symbolNumberHook, paramMap);
						symbolNumber = getConsoleNumeric("return " + newHook);

						/*
						 * if("RollingReels".equalsIgnoreCase(xpathMap.get("ReelsType"))) { symbolNumber
						 * = getConsoleNumeric( "return "+forceNamespace+
						 * ".getControlById('ReelsWithRollingReelsComponent').reelsArray[" + column +
						 * "].symbolArray[" + row + "].symbolNumber");
						 * 
						 * } else { symbolNumber = getConsoleNumeric(
						 * "return "+forceNamespace+".getControlById('ReelComponent').reelsArray[" +
						 * column + "].symbolArray[" + row + "].symbolNumber"); }
						 */
						String symbolName = symbolArray.get((int) symbolNumber);
						if (proceedSymbolArray.contains(symbolName)) {
							String symbolClickHook = xpathMap.get("clickOnSymbol");
							newHook = replaceParamInHook(symbolClickHook, paramMap);
							clickAtButton("return " + newHook);
							/*
							 * if("RollingReels".equalsIgnoreCase(xpathMap.get("ReelsType"))) {
							 * clickAtButton( "return "+forceNamespace+
							 * ".getControlById('ReelsWithRollingReelsComponent').reelsArray[" + column +
							 * "].symbolArray[" + row +
							 * "].symbolClickEvent.dispatch({ symbolNumber: "+forceNamespace+
							 * ".getControlById('ReelsWithRollingReelsComponent').reelsArray[" + column +
							 * "].symbolArray[" + row +
							 * "].symbolNumber, symbolReelSetPositionId: "+forceNamespace+
							 * ".getControlById('ReelsWithRollingReelsComponent').reelsArray[" + column +
							 * "].symbolArray[" + row + "].symbolReelSetPositionId})");
							 * 
							 * } else { clickAtButton(
							 * "return "+forceNamespace+".getControlById('ReelComponent').reelsArray[" +
							 * column + "].symbolArray[" + row +
							 * "].symbolClickEvent.dispatch({ symbolNumber: "+forceNamespace+
							 * ".getControlById('ReelComponent').reelsArray[" + column + "].symbolArray[" +
							 * row + "].symbolNumber, symbolReelSetPositionId: "+forceNamespace+
							 * ".getControlById('ReelComponent').reelsArray[" + column + "].symbolArray[" +
							 * row + "].symbolReelSetPositionId})"); }
							 */

							currencyReport.detailsAppendFolderOnlyScreeshot(currencyName);

							proceedSymbolArray.remove(symbolName);
							log.debug("Remaining Symbols::" + proceedSymbolArray);

						}

					}
				}
				if (proceedSymbolArray.size() > 0) {
					try {
						spinclick();
					} catch (InterruptedException e) {
						log.error("validateMiniPaytable", e);
					}
					waitForSpinButtonstop();
				}
				Thread.sleep(5000);

			} while (proceedSymbolArray.size() > 0);
		} catch (Exception e) {
			log.error(e.getMessage());
		}

	}

	@Override
	public String getMinimumBet() {
		String minimumbet = GetConsoleText("return " + xpathMap.get("getMinimumBet"));
		return minimumbet;
	}

	/*
	 * This method is use to verify the currency format in respin reel
	 */
	@Override
	public boolean reSpinOverlayCurrencyFormat(String currencyFormat) {

		String respinbet = null;
		String strrespinbetexp = null;
		boolean isrespinbetincurrencyformat = false;
		try {
			closeOverlay();
			Thread.sleep(3000);
			clickAtButton("return " + xpathMap.get("RespinMouseClick"));

			if (!GameName.contains("Scratch")) {
				respinbet = "return " + forceNamespace + ".getControlById('RespinComponent').Text.respinValue0._text";
			} else {
				// need to update the hook for Scratch game
				respinbet = "return " + forceNamespace + ".getControlById('RespinComponent').Text.respinValue0._text";
			}
			String respinbetnew = GetConsoleText(respinbet);

			String respinregexp = createregexp(respinbetnew, currencyFormat);
			if (respinregexp.contains("$"))
				strrespinbetexp = respinregexp.replace("$", "\\$");
			else
				strrespinbetexp = respinregexp;
			Pattern.compile(strrespinbetexp);
			String respinbetexp = strrespinbetexp.replace("#", "\\d");

			if (Pattern.matches(respinbetexp, respinbetnew)) {
				isrespinbetincurrencyformat = true;
			} else {
				isrespinbetincurrencyformat = false;
			}

			log.debug("Fetching bet currency symbol" + "/n bet currency symbol is::" + respinbet);
		} catch (Exception e) {
			log.error("Error in Fetching currency symbol", e);
		}
		return isrespinbetincurrencyformat;
	}

	/*
	 * This method is used to click on first respin
	 */
	@Override
	public void clickOnReSpinOverlay() {

		try {
			closeOverlay();
			Thread.sleep(3000);
			// to click on the bet dialog when bet is changed in respin games
			clickAtButton("return " + xpathMap.get("ClickOnBetDialogNo"));

		} catch (Exception e) {
			log.error("Error while clicking on respin Overlay", e);
		}

	}

	public void setSoundFalgActive(boolean soundFlagActive) {
		menuOpen();
		boolean flag = getConsoleBooleanText("return " + xpathMap.get("isMenuOptionBtnVisible"));
		if (flag) {
			clickAtButton("return " + xpathMap.get("SettingOpenBtn"));
			log.debug("Clicked on settings button to open and verify");
			boolean soundpresent = getConsoleBooleanText("return " + xpathMap.get("isMenuSoundBtnVisible"));
			if (soundpresent) {
				boolean soundflag = getConsoleBooleanText("return " + xpathMap.get("isSettingsSoundBtnActive"));

				if (soundflag != soundFlagActive) {
					clickAtButton("return " + xpathMap.get("ClickSettingsSoundBtn"));
				}

			}
		}
		closeOverlay();
	}

	public void startAutoPlay() {

		clickAtButton("return " + xpathMap.get("AutoPlayStartBtn"));

	}

	public void clickFregamesPlay() {

		String view = getConsoleText("return " + xpathMap.get("FGViewHistoryLength"));

		try {
			if ("freeGamesOfferView".equalsIgnoreCase(view)) {
				clickPlayNow();
			} else if ("freeGamesResumeView".equalsIgnoreCase(view)) {
				clickResumePlay();
			}
		} catch (Exception e) {
			System.out.println("Exception in clickFregamesPlay");
			log.error("Exception in clickFregamesPlay", e);
		}
	}

	/**
	 * *Author:Pramlata Mishra This method is used to get element text form game
	 * console
	 * 
	 * @throws InterruptedException
	 */
	public String getConsoleText(String text) {
		String consoleText = null;
		try {
			JavascriptExecutor js = (webdriver);
			consoleText = (String) js.executeScript(text);
		} catch (Exception e) {
			e.getMessage();
		}
		return consoleText;
	}

	public boolean clickResumePlay() {

		boolean isResumeButtonClicked = false;
		try {
			clickAtButton("return " + xpathMap.get("ClickFGResumeViewResumeBtn"));
			isResumeButtonClicked = true;
		} catch (Exception e) {
			isResumeButtonClicked = false;
			log.error(e.getMessage(), e);
			// e.printStackTrace();
		}
		return isResumeButtonClicked;
	}

	/*
	 * Name:verifySpinBtnState(report) Description:verify the spin button status
	 * with quick spin on and off
	 * 
	 * return:void
	 */
	public void verifySpinBtnState(Mobile_HTML_Report report)
    {
        try{
            if(!Constant.FORCE_ULTIMATE_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame"))&& !(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame")))){
                // click on autoplay mobile button
                clickAtButton("return "+xpathMap.get("ClickAutoplayOpenMobileBtn"));
            }
            if(getConsoleBooleanText("return "+xpathMap.get("isQuickSpinBtnVisible")))
            {



               if("active".equalsIgnoreCase(getConsoleText("return "+xpathMap.get("QuickSpinBtnState"))))
                {
                    clickOnQuickSpin();
                    Thread.sleep(1000);
                }



               //checks the status of spin button when quick spin is off
                if("activeSecondary".equalsIgnoreCase(getConsoleText("return "+xpathMap.get("QuickSpinBtnState"))))
                {
                    if(!GameName.contains("Scratch"))
                    {
                        //click on spin button
                        spinclick();
                        if("activeSecondary".equalsIgnoreCase(getConsoleText("return "+xpathMap.get("SpinBtnCurrState"))))
                            report.detailsAppend("verify the spin button status when quick spin is off", " Spin button should convert to STOP", "Spin button converted to STOP ", "pass");
                        else
                            report.detailsAppend("verify the spin button status when quick spin is off", " Spin button should convert to STOP", "Spin button not converted to STOP ", "fail");
                        //assertEquals(getConsoleText("retuns "+XpathMap.get("SpinBtnCurrState")), "activeSecondary");
                    }
                    // below code block for scratch games
                    else
                    {
                        clickAtButton("return  "+xpathMap.get("ClickscratchButton"));
                        elementWait("return "+xpathMap.get("ScratchBtnCurrState"), "activeSecondary");



                       if("activeSecondary".equalsIgnoreCase(getConsoleText("return "+xpathMap.get("ScratchBtnCurrState"))))
                            report.detailsAppend("verify the spin button status when quick spin is off", " Spin button should convert to STOP", "Spin button converted to STOP ", "pass");
                        else
                            report.detailsAppend("verify the spin button status when quick spin is off", " Spin button should convert to STOP", "Spin button not converted to STOP ", "fail");



                       clickAtButton("return  "+xpathMap.get("ClickscratchButton"));
                        elementWait("return "+xpathMap.get("ScratchBtnCurrState"), "active");
                    }
                }
                if(!Constant.FORCE_ULTIMATE_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame"))&& !(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame")))){
                    //check the status of spin button when quick spin is on
                    clickAtButton("return "+xpathMap.get("ClickAutoplayOpenMobileBtn"));
                }
                /*    //check the status of spin button when quick spin is on
                clickAtButton("return "+XpathMap.get("ClickAutoplayOpenMobileBtn"));*/
                Thread.sleep(2000);
                waitForSpinButtonstop();
                waitForSpinButton();
                clickOnQuickSpin();
                Thread.sleep(1000);
                //click on spin button
                if(!GameName.contains("Scratch"))
                {
                    spinclick();
                    Thread.sleep(300);
                    if("disabled".equalsIgnoreCase(getConsoleText("return "+xpathMap.get("SpinBtnCurrState"))))
                        report.detailsAppend("verify the spin button status when quick spin is on", " Spin button should not convert to STOP", "Spin button not converted to STOP ", "pass");
                    else
                        report.detailsAppend("verify the spin button status when quick spin is on", " Spin button should not convert to STOP", "Spin button converted to STOP ", "fail");



               }
                else
                {
                    clickAtButton("return  "+xpathMap.get("ClickscratchButton"));
                    if("disabled".equalsIgnoreCase(getConsoleText("return "+xpathMap.get("SpinBtnCurrState"))))
                        report.detailsAppend("verify the spin button status when quick spin is on", " Spin button should not convert to STOP", "Spin button not converted to STOP ", "pass");
                    else
                        report.detailsAppend("verify the spin button status when quick spin is on", " Spin button should not convert to STOP", "Spin button converted to STOP ", "fail");
                }
                clickOnQuickSpin();
            }
            else
            {
                log.debug("Quick spin button is not present");
                report.detailsAppend("verify the spin button status when quick spin is off", " quick spin button must be present", "quick spin button is not present ", "fail");
            }
        }catch(Exception e)
        {
            log.error(e.getMessage(), e);
            try {
                report.detailsAppendNoScreenshot("verify the spin button status ", " ", "Exception occur while verifying spin button satus with quick spin on and off ", "fail");
            } catch (Exception e1) {
                e1.printStackTrace();
            }



       }
    }
	/*
	 * Method for payout verification for all bets
	 */
	@Override
	public void Payoutvarificationforallbet(Mobile_HTML_Report report) {

		System.currentTimeMillis();
		Util util = new Util();
		int length = 0;
		double gamePayout;
		int index = 0;
		String paytablePayout;
		String scatter;
		long totalNoOfQuickBets;

		// Read xml for the game

		String xmlFilePath = "./" + GameName + "/Config/" + GameName + ".xml";
		length = util.xmlLength(xmlFilePath, "WinningCombinations");
		ArrayList<String> symbolData = util.getXMLDataInArray("Symbol", "name", xmlFilePath);

		String strPaylineCost = xpathMap.get("DefaultPayLineCost");// this code will fetch the data from sheet
		double paylinecost = Double.parseDouble(strPaylineCost);

		ArrayList<String> winCombinationList = new ArrayList<String>();

		for (int count = 0; count < length; count++) {
			String strWinCombination = util.readXML("WinCombination", "numSymbolsRequired", "symbols", "payouts",
					"./" + GameName + "/Config/" + GameName + ".xml", length + 2, count);
			if (strWinCombination != null) {
				winCombinationList.add(strWinCombination);
			}
		}

		int winCombinationSize = winCombinationList.size();

		try {
			setMaxBet();
			/*
			 * if("yes".equalsIgnoreCase(XpathMap.get("IsRespinFeature"))) {
			 * clickAtButton("return  "+XpathMap.get("MaxBetDailoag"));
			 * 
			 * }
			 */
			if ((Class.forName(Thread.currentThread().getStackTrace()[2].getClassName())).toString()
					.contains("Modules.Regression.TestScript.Mobile_Regression_BaseScene")) {

				totalNoOfQuickBets = getNumberOfQuickbets();
			} else
				totalNoOfQuickBets = 1;
			for (int i = 1; i <= totalNoOfQuickBets; i++) {
				boolean[] resultflag = new boolean[winCombinationSize];
				// length=gcfnlib.xmlLength(xmlFilePath,"WinningCombinations");
				// setting index at the staring
				index = 0;
				// Get the bet amount
				String bet1 = getBetAmt();
				double bet = Double.parseDouble(bet1);
				System.out.println(bet);
				// System.out.println("Payout varification in progress for the selected bet
				// :"+bet);
				report.detailsAppendNoScreenshot("Payout varification in progress for the selected bet :" + bet,
						"Payout varification in progress for the selected bet :" + bet,
						"Payout varification in progress for the selected bet :" + bet, "Pass");
				// report.details_append("verify the payouts after changing bet and bet value
				// :"+bet, "Paytable payout must be correct after changing bet","", "");

				capturePaytableScreenshot(report, "Bet-" + bet);

				// Below logic verifies all the values in paytable for this bet
				for (int j = 0; j < winCombinationSize; j++) {

					// paytablePayout=gcfnlib.readXML("WinCombination", "numSymbolsRequired",
					// "symbols","payouts", xmlFilePath,length,index);
					paytablePayout = winCombinationList.get(index);
					if (paytablePayout.contains("Scatter") || paytablePayout.contains("FreeSpin")
							|| paytablePayout.contains("scatter")) {
						scatter = "yes";
					} else {
						scatter = "no";
					}
					String[] xmlData = paytablePayout.split(",");
					String xmlpayout = xmlData[2];
					// System.out.println(xmlpayout);

					gamePayout = gamepayout(symbolData, paytablePayout);// it will fetch game payout for Force game

					double result = verifyPaytable_Payouts(xmlpayout, paylinecost, bet, scatter);
					// System.out.println("paytablePayout ::" +paytablePayout );
					// System.out.println("gamePayout ::"+gamePayout);
					// System.out.println("result ::"+result);

					// System.out.println(result);
					if (gamePayout == result) {
						Arrays.fill(resultflag, true);
						// System.out.println("Paytable payout is correct for the bet value :"+bet+" xml
						// base value :"+xmlpayout+" result from formula :"+result+" symbol name :
						// "+xmlData[1]+"game payout : " +gamePayout+ " is correct");
						log.debug("Paytable payout is correct for the bet value :" + bet + " xml base value :"
								+ xmlpayout + " result from formula :" + result + " symbol name : " + xmlData[1]
								+ "game payout : " + gamePayout + " is correct");
						// report.details_append_NoScreenshot("Verify that the payout "+ xmlData[1] + "
						// coming in paytable is correct ", "Paytable payout must be correct","bet value
						// :"+bet+" xml base value :"+xmlpayout+" result from formula :"+result+" symbol
						// name : "+xmlData[1]+"game payout : " +gamePayout+ " is correct", "pass");
					} else {
						Arrays.fill(resultflag, false);
						log.debug("Paytable payout is not correct for the bet value :" + bet + " xml base value :"
								+ xmlpayout + " result from formula :" + result + " symbol name : " + xmlData[1]
								+ "game payout : " + gamePayout + " is incorrect");

						// System.out.println("Paytable payout is not correct for the bet value :"+bet+"
						// xml base value :"+xmlpayout+" result from formula :"+result+" symbol name :
						// "+xmlData[1]+"game payout : " +gamePayout+ " is correct");

						// report.details_append_NoScreenshot("Verify that the payout "+ xmlData[1] + "
						// coming in paytable is incorrect ", "Paytable payout must be correct","bet
						// value :"+bet+" xml base value :"+xmlpayout+" result from formula :"+result+"
						// symbol name :"+xmlData[1]+"game payout : " +gamePayout+ " is incorrect",
						// "fail");
					}
					length--;
					index++;
				}

				System.currentTimeMillis();

				// common result for the one selected bet;

				boolean isAllTrue = !Arrays.toString(resultflag).contains("false");
				if (isAllTrue) {
					report.detailsAppendNoScreenshot(
							"verify Payout verification for the bet : " + bet + "with the values of paytable in game",
							"Payout verification for the bet : " + bet + "with the values of paytable in game ",
							"Payout verification for the bet : " + bet + "with the values of paytable in game is done",
							"pass");

				} else {
					report.detailsAppendNoScreenshot(
							"verify Payout verification for the bet : " + bet + "with the values of paytable in game",
							"Payout verification for the bet : " + bet + "with the values of paytable in game ",
							"Payout verification for the bet : " + bet
									+ "with the values of paytable in game is done but failed coz some values are not matched",
							"fail");

				}
				// Closes the paytable
				paytableClose();
				// Change the bet to the next bet
				betIncrease();

				// xmlsno++;
			}
		} catch (Exception e) {
			log.error(e.getStackTrace());
		}

		System.currentTimeMillis();
	}

	public double gamepayout(ArrayList<String> symbolData, String paytablePayout) {
		double gamepayout_Double = 0.0;
		int symbolIndex = 0;
		try {
			String wild = xpathMap.get("wildSymbol");
			String[] xmlData = paytablePayout.split(",");
			String line = xmlData[0];
			String symbol = xmlData[1];
			for (int i = 0; i < symbolData.size(); i++) {
				if (symbolData.get(i).contains(symbol)) {
					if (wild.equalsIgnoreCase("no")) {
						symbolIndex = i - 1;
					} else
						symbolIndex = i;
					break;
				}

			}
			String payout = "return " + forceNamespace + ".getControlById('PaytableComponent').paytableSymbolArray["
					+ symbolIndex + "].Text.line" + line + "._text";
			System.out.println("return " + forceNamespace + ".getControlById('PaytableComponent').paytableSymbolArray["
					+ symbolIndex + "].Text.line" + line + "._text");
			log.debug("return " + forceNamespace + ".getControlById('PaytableComponent').paytableSymbolArray["
					+ symbolIndex + "].Text.line" + line + "._text");
			String consolePayout = getConsoleText(payout);
			String consolePayoutnew = consolePayout.replaceAll("[^0-9]", "");
			gamepayout_Double = Double.parseDouble((consolePayoutnew));

		} catch (Exception e) {
			e.getMessage();
		}
		return gamepayout_Double / 100;
	}

	
	public boolean clickOnQuickSpin()
	{
		boolean qSTest=false;
		try
		{
			clickAtButton("return "+xpathMap.get("QuickSpinBtn"));

			Thread.sleep(3000);
			String qsCurrectState = getConsoleText("return " + xpathMap.get("QuickSpinBtnState"));
			if (qsCurrectState.equalsIgnoreCase("active")) {
				qSTest = true;
				log.debug("Clicked on quickspin");
			} else {
				qSTest = false;
				log.error("error in quickspin click");
			}
		} catch (Exception e) {
			log.error("qucikspin is  not clickable with Quick Spin on", e);
		}
		return qSTest;

	}

	public boolean verifyOneDesignAutoplayPanelOptions(Mobile_HTML_Report report) {

		try {
			// This counter = number of panel options available in autoplay panel.
			for (int counter = 0; counter < 1; counter++) {
				log.debug("Autoplay for ::" + counter);
				clickAtButton("return " + xpathMap.get("ClickAutoPlayBtn"));
				Thread.sleep(1000);
				// clickAtButton("return "+xpathMap.get("AutoplayOptionButton0"));
				Thread.sleep(1000);
				clickAtButton("return " + xpathMap.get("AutoplayOptionButton1"));
				Thread.sleep(1000);
				clickAtButton("return " + xpathMap.get("AutoplayOptionButton0"));
				Thread.sleep(1000);
				// new added to see is the selected option is same depling at console
				String autoplaySelectedCountfrompanel = getConsoleText("return " + xpathMap.get("AutoPlayNumberLabel"));
				log.debug("autoplaySelectedCount from Autoplay Panel option :: " + autoplaySelectedCountfrompanel);
				clickAtButton("return " + xpathMap.get("AutoPlayStartBtn"));
				report.detailsAppendNoScreenshot(
						"Verify that selected autoplay count from Autoply panel option must be played in game",
						"Selected autoplay count from Autoply panel option must be played in game",
						"AutoplaySelectedCount from Autoply panel option :: " + autoplaySelectedCountfrompanel, "Pass");
				Thread.sleep(2000);
				// is bet Available.. Bet must not be accessible during autoplay

				boolean isBetButtonActive = isBetButtonAccessible(); // Must not be accessible during auto play
				if (isBetButtonActive) {
					// need screenshot
					report.detailsAppend("verify that is BetButton Active during Autoplay",
							" Is BetButton Active during Autoplay", " BetButton Active during Autoplay ", "fail");

				} else {
					report.detailsAppend("verify that is BetButton Active during Autoplay",
							" Is BetButton Active during Autoplay", "BetButton is not  Active during Autoplay ",
							"pass");
				}

				boolean isMaxButtonActive = isMaxButtonAccessible(); // Must not be accessible during auto play
				if (isMaxButtonActive) {
					report.detailsAppend("verify that is MaxButton Accessible during Autoplay",
							" Is MaxButton Accessible during Autoplay", "MaxButton Accessible during Autoplay ",
							"fail");
				} else {
					report.detailsAppend("verify that is MaxButton Accessible during Autoplay",
							" Is MaxButton Accessible during Autoplay", "MaxButton is not Accessible during Autoplay",
							"pass");
				}
				setQuickSpinOn();
				report.detailsAppend("verify the QuickSpin On screen shot", " QuickSpinOn page screen shot",
						"QuickSpinOn option page screenshot ", "pass");
				Thread.sleep(2000);
				// Take screen shot
				setQuickSpinOff();
				report.detailsAppend("verify the QuickSpinOff screen shot", " QuickSpinOff page screen shot",
						"QuickSpinOff option page screenshot ", "pass");
				Thread.sleep(1000);

				waitTillAutoplayComplete();

				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return false;
	}

	@Override
	public void setDefaultBet() {
		int defaultbet;
		try {
			if (!GameName.contains("Scratch")) {
				String strpaylinecost = xpathMap.get("DefaultPayLineCost");
				int paylinecost = (int) Double.parseDouble(strpaylinecost);
				String strnumOfchips = xpathMap.get("DefaultNumOfChips");
				int numOfchips = (int) Double.parseDouble(strnumOfchips);
				String strchipSize = xpathMap.get("DefaultChipSize");
				int chipSize = (int) Double.parseDouble(strchipSize);
				defaultbet = paylinecost * chipSize * numOfchips;
			}
			// For scratch games
			else {
				defaultbet = Integer.parseInt(getConsoleText("return " + xpathMap.get("getDefaultBet")));
			}
			String setBetValue = xpathMap.get("SetBetValue") + "(" + defaultbet + ")";
			getConsoleText("return " + setBetValue);
		} catch (Exception e) {
			log.error("Exception occur:", e);
		}
	}

	/*
	 * Method Name: checkAvilability(String hooksofcomponent) Description: This
	 * method check the availability of component Input parameter: String hook of
	 * component to be check. output : boolean variable , component is available or
	 * not in game. Author : Snehal Gaikwad.
	 */
	@Override
	public boolean checkAvilability(String hooksofcomponent) {
		boolean isComponentAvilable = true;

		try {

			isComponentAvilable = getConsoleBooleanText("return " + hooksofcomponent);
		} catch (Exception e) {

			isComponentAvilable = false;
		}
		return isComponentAvilable;
	}

	/*
	 * Method Name: isPaytableAvilable() Description: This method check the
	 * availability of paytable component in game Input parameter: output : boolean
	 * variable , component is available or not in game. Author : Snehal Gaikwad.
	 */

	@Override
	public boolean isPaytableAvailable() {
		boolean ispaytableavilable = true;

		try {
			// if paytable availble it will return true
			ispaytableavilable = getConsoleBooleanText("return " + xpathMap.get("isMenuPaytableBtnVisible"));
		} catch (Exception e) {
			// if paytable in the game not avilable in it while give an exception
			ispaytableavilable = false;
		}
		return ispaytableavilable;
	}

	/*
	 * Method Name: isSettingAvilable() Description: This method check the
	 * availability of setting component in game Input parameter: output : boolean
	 * variable , component is available or not in game. Author : Snehal Gaikwad.
	 */
	public boolean isSettingAvailable() {
		boolean isSettingAvilable = true;

		try {
			// if paytable availble it will return true
			isSettingAvilable = getConsoleBooleanText("return " + xpathMap.get("isMenuSettingsBtnVisible"));
		} catch (Exception e) {
			// if paytable in the game not avilable in it while give an exception
			isSettingAvilable = false;
		}
		return isSettingAvilable;
	}

	/*
	 * Method Name:waitTillAutoplayComplete() Description: wait till the Autoplay
	 * available. check the current sate of bet component. return : boolean
	 * variable.
	 */

	public boolean waitTillAutoplayComplete() {
		boolean isAutoplayComplete = false;
		try {
			log.debug("waiting for Autoplay to complete....");
			elementWait("return " + xpathMap.get("BetIconCurrState"), "active");
			isAutoplayComplete = true;
			log.debug(" Autoplay complete as bet button is active");

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return isAutoplayComplete;
	}

	// Method to wait till win occurs and verify the currency format in win
	@Override
	public boolean verifyBigWinCurrencyFormat(String currencyFormat, Mobile_HTML_Report currencyReport,
			String currencyName) {
		boolean result = false;
		String strwinexp = null;
		long startTime = System.currentTimeMillis();
		String winamt = null;
		try {
			// Thread.sleep(1000);
			log.debug("Function-> waitforWinAmount()");
			if ("Yes".equalsIgnoreCase(xpathMap.get("BigWinlayers"))) {
				for (int i = 1; i <= 3; i++) {
					Thread.sleep(2000);
					waitForbigwin();
					log.debug("Bigwinlayer captured" + i);
					System.out.println("Bigwinlayer captured" + i);

					while (true) {
						if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame"))) {
							winamt = getConsoleText("return " + xpathMap.get("WinMobileText"));
							log.info("verifyBigWinCurrencyFormat():waiting for win  to occur");
						} else
							winamt = getConsoleText("return " + xpathMap.get("BigWinCountUpText"));
						if (winamt != null) {
							log.info(" win occur ");
							// fetching win from panel
							String consolewinnew = winamt.replace("win: ", "");
							String winregexp = createregexp(consolewinnew, currencyFormat);
							if (winregexp.contains("$"))
								strwinexp = winregexp.replace("$", "\\$");
							else
								strwinexp = winregexp;
							Pattern.compile(strwinexp);
							String winexp = strwinexp.replace("#", "\\d");

							log.debug(
									"verifyBigWinCurrencyFormat():Win Exp= " + winexp + "consolewin= " + consolewinnew);

							if (Pattern.matches(winexp, consolewinnew)) {
								result = true;
								currencyReport.detailsAppendFolderOnlyScreeshot(currencyName);
							} else {
								result = false;
								currencyReport.detailsAppendFolderOnlyScreeshot(currencyName);
							}
							break;
						} else {
							long currentime = System.currentTimeMillis();
							if (((currentime - startTime) / 1000) > 120) {
								log.debug("verifyBigWinCurrencyFormat():No win occur  in 2 mint.");
								result = false;
								break;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.error("Big win not occur.please  use bigwin test data");

		}
		return result;
	}

	@Override
	public void validateMenuInBigWin(Mobile_HTML_Report report) {
		try {
			spinclick();
			waitForbigwin();
			if (menuOpen()) {
				Thread.sleep(2000);
				menuClose();
				if (getConsoleBooleanText("return " + xpathMap.get("isWinComponentVisible"))) {
					report.detailsAppend(" Verify Bigwin with Hamburger menu ",
							" Menu Panel should open and Big Win should be summarized  ",
							"Menu Panel  open and Big Win is summarized ", "PASS");
				} else {
					report.detailsAppend("Verify Bigwin with Hamburger menu ",
							"Menu Panel should open and Big Win should be summarized  ",
							"Menu Panel open and Big Win is not summarized", "FAIL");
				}
			} else {
				report.detailsAppend("Verify Bigwin with Hamburger menu ",
						"Menu Panel should open and Big Win should be summarized  ", "unable to open Menu Panel ",
						"FAIL");
			}

		} catch (JavascriptException exception) {
			log.error(" Exception occur while executing hook,Please verify thre hook of given component"
					+ exception.getMessage());
		} catch (Exception exception) {
			log.error("Exception occur" + exception.getMessage());
		}

	}

	@Override
	public void validatePaytableNavigationInBigWin(Mobile_HTML_Report report) {
		try {
			spinclick();
			waitForbigwin();
			if (menuOpen()) {
				if (checkAvilability(xpathMap.get("isMenuPaytableBtnVisible"))) {
					clickAtButton(xpathMap.get("PaytableOpenBtn"));
					Thread.sleep(2000);
					paytableClose();
					if (getConsoleBooleanText("return " + xpathMap.get("isWinComponentVisible"))) {
						report.detailsAppend(" Verify Bigwin with Paytable navigation ",
								"Big Win should be summarized after paytable navegation ",
								"Paytable is open and Big Win is summarized ", "PASS");
					} else {
						report.detailsAppend("Verify Bigwin with Paytable navigation ",
								" Big Win should be summarized after paytable navegation ",
								"Paytable is open and Big Win is not summarized ", "FAIL");
					}
				} else {
					report.detailsAppend("Verify Bigwin with Paytable navigation ",
							" Big Win should be summarized after paytable navegation ",
							"Paytable is not visible in Bigwin ", "FAIL");

				}
			}

		} catch (JavascriptException exception) {
			log.error("Exception occur while executing hook,Please verify thre hook of given component"
					+ exception.getMessage());
		} catch (Exception exception) {
			log.error(exception.getMessage());
		}
	}


	public void bigWinQuickSpinOnOffValidation(Mobile_HTML_Report report)
    {
        try{
            //put quick spin off if previously on
            if("active".equalsIgnoreCase(getConsoleText("return "+xpathMap.get("QuickSpinBtnState"))))
            {
                clickOnQuickSpin();
                Thread.sleep(1000);
            }
            //Check big win status when quick spin is off
            if("activeSecondary".equalsIgnoreCase(getConsoleText("return "+xpathMap.get("QuickSpinBtnState"))))
            {
                spinclick();
                waitForbigwin();
                Thread.sleep(1000);
                if(!getConsoleBooleanText("return "+ xpathMap.get("isWinComponentVisible")))
                {
                    report.detailsAppend("Verify Bigwin with QuickSpin Off ","Big Win animation should play completely ","Big Win animation  play completely ", "PASS");
                }
                else
                {
                    report.detailsAppend("Verify Bigwin with QuickSpin Off ","Big Win animation should play completely ","Big Win summarized ", "FAIL");



               }




                //Check big win status when quick spin is on



               clickOnQuickSpin();
                if("active".equalsIgnoreCase(getConsoleText("return "+xpathMap.get("QuickSpinBtnState"))))
                {
                    spinclick();
                    waitForbigwin();
                    Thread.sleep(1000);
                    if(!getConsoleBooleanText("return "+ xpathMap.get("isWinComponentVisible")))
                    {
                        report.detailsAppend("Verify Bigwin with QuickSpin On"," Big Win animation should play completely ","Big Win animation  play completely ", "PASS");
                    }
                    else
                    {
                        report.detailsAppend("Verify Bigwin with QuickSpin On "," Big Win animation should play completely ","Big Win summarized ", "FAIL");



                   }
                    //make quick spin off
                    clickOnQuickSpin();
                }
                else
                {
                    report.detailsAppend("Verify Bigwin with QuickSpin On "," Big Win animation should play completely ","Quick spin is not on ", "FAIL");
                }
            }else
            {
                report.detailsAppend("Verify Bigwin with QuickSpin off "," Big Win animation should play completely ","Quick spin is not off ", "FAIL");
            }



       }catch(JavascriptException exception)
        {
            log.error("Exception occur while executing hook,Please verify thre hook of given component"+exception.getMessage());
        }
        catch(Exception exception)
        {
            log.error(exception.getMessage());
        }
    }
		@Override
	    public void bigwinwithAutoplay(Mobile_HTML_Report report)
	    {
	        try{
	            startAutoPlay();
	            //put quick spin off if previously on
	            if("active".equalsIgnoreCase(getConsoleText("return "+xpathMap.get("QuickSpinBtnState"))))
	            {
	                clickOnQuickSpin();
	                Thread.sleep(1000);
	            }
	            //check quick spin is off
	            if("activeSecondary".equalsIgnoreCase(getConsoleText("return "+xpathMap.get("QuickSpinBtnState"))))
	            {
	                if(!getConsoleBooleanText("return "+ xpathMap.get("isWinComponentVisible")))
	                {
	                    report.detailsAppend("Verify Bigwin with Autoplay"," When Quick spin is off,big win should play completely ","Big Win animation  play completely ", "PASS");
	                }
	                else
	                {
	                    report.detailsAppend("Verify Bigwin with Autoplay"," When Quick spin is off,big win should play completely ","Big Win summarized ", "FAIL");



	               }
	                waitForWinDisplay();
	                //validate when quick spin is on in autoplay
	                clickOnQuickSpin();
	                if(getConsoleBooleanText("return "+ xpathMap.get("isWinComponentVisible")))
	                {
	                    report.detailsAppend("Verify Bigwin with Autoplay  ","When Quick spin is on,big win should summarized ","Big Win summarized ", "PASS");
	                }
	                else
	                {
	                    report.detailsAppend("Verify Bigwin with Autoplay ","When Quick spin is on,big win should summarized ","Big Win not summarized ", "FAIL");



	               }
	            }
	            waitTillAutoplayComplete();
	        }catch(JavascriptException exception)
	        {
	            log.error("Exception occur while executing hook,Please verify thre hook of given component"+exception.getMessage());
	        }
	        catch(Exception exception)
	        {
	            log.error(exception.getMessage());
	        }
	    }
	@Override
	public void bigWinWithOrientationChange(Mobile_HTML_Report report) {
		try {
			spinclick();
			waitForbigwin();
			if (webdriver.getOrientation().compareTo(ScreenOrientation.PORTRAIT) == 0) {
				webdriver.context("NATIVE_APP");
				webdriver.rotate(ScreenOrientation.LANDSCAPE);
				webdriver.context("CHROMIUM");
			} else if (webdriver.getOrientation().compareTo(ScreenOrientation.LANDSCAPE) == 0) {
				webdriver.context("NATIVE_APP");
				webdriver.rotate(ScreenOrientation.PORTRAIT);
				webdriver.context("CHROMIUM");
			}

			Thread.sleep(2000);

			if (getConsoleBooleanText("return " + xpathMap.get("isWinComponentVisible"))) {
				report.detailsAppend("Verify Bigwin with Autoplay  ",
						" When Quick spin is on,big win should summarized ", "Big Win summarized ", "PASS");
			} else {
				report.detailsAppend("Verify Bigwin with Autoplay ",
						" When Quick spin is on,big win should summarized ", "Big Win not summarized ", "FAIL");

			}

		} catch (JavascriptException exception) {
			log.error("Exception occur while executing hook,Please verify thre hook of given component"
					+ exception.getMessage());
		} catch (Exception exception) {
			log.error(exception.getMessage());
		}
	}

	@Override
	public void bigWinWithSpin(Mobile_HTML_Report report) {
		try {
			spinclick();
			waitForbigwin();
			if (!getConsoleBooleanText("return " + xpathMap.get("isWinComponentVisible"))) {
				report.detailsAppend("Verify Bigwin with spin button click  ",
						" On Spin click ,big win should play complatly ", "Big Win played complatly ", "PASS");
			} else {
				report.detailsAppend("Verify Bigwin with spin button click",
						" On Spin click ,big win should play complatly ",
						"Big Win not  played complatly,get summarized ", "FAIL");
			}
			waitForWinDisplay();
		} catch (JavascriptException exception) {
			log.error("Exception occur while executing hook,Please verify thre hook of given component"
					+ exception.getMessage());
		} catch (Exception exception) {
			log.error(exception.getMessage());
		}
	}

	@Override
	public void verifyJackPotBonuswithScreenShots(Mobile_HTML_Report report, String languageCode) {

		String jackpotTrophyCloseHook = xpathMap.get("jackpotHistoryClose");
		try {
			String jackpotTrophyClickHook = xpathMap.get("jackpotHistoryTrophyClick");

			JavascriptExecutor js = ((JavascriptExecutor) webdriver);

			clickAtButton("return  " + jackpotTrophyClickHook);

			threadSleep(5000);

			// jackpotHistoryPageHeight
			// jackpotHistoryVisibleHeight
			String[] jackpotHistoryDurationArray = new String[] { "1week", "2weeks", "1month", "6months", "1year",
					"2years" };
			double jackpotHistoryPageHeight = 0;

			if (js.executeScript("return " + xpathMap.get("jackpotHistoryPageHeight")).getClass().getName()
					.endsWith("Long")) {
				long longJackpotHistoryPageHeight = (Long) js
						.executeScript("return " + xpathMap.get("jackpotHistoryPageHeight"));
				jackpotHistoryPageHeight = (double) longJackpotHistoryPageHeight;
			} else {
				jackpotHistoryPageHeight = (double) js
						.executeScript("return " + xpathMap.get("jackpotHistoryPageHeight"));
			}

			double jackpotHistoryVisibleHeight = 0;
			if (js.executeScript("return " + xpathMap.get("jackpotHistoryVisibleHeight")).getClass().getName()
					.endsWith("Long")) {
				long longJackpotHistoryVisibleHeight = (Long) js
						.executeScript("return " + xpathMap.get("jackpotHistoryVisibleHeight"));
				jackpotHistoryVisibleHeight = (double) longJackpotHistoryVisibleHeight;
			} else {
				jackpotHistoryVisibleHeight = (double) js
						.executeScript("return " + xpathMap.get("jackpotHistoryVisibleHeight"));
			}

			System.out.println("jackpotHistoryPageHeight : " + jackpotHistoryPageHeight);
			System.out.println("jackpotHistoryVisibleHeight : " + jackpotHistoryVisibleHeight);
			int scroll = (int) (jackpotHistoryPageHeight / jackpotHistoryVisibleHeight);
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("param1", Double.toString(jackpotHistoryPageHeight));

			String Hook = xpathMap.get("scrollProgressiveWinHistoryWidgetwithHight");
			String newHook = replaceParamInHook(Hook, paramMap);

			for (int counter1 = 0; counter1 <= 10; counter1++) {

				int index = 0;
				if (counter1 >= 2) {
					index = 2;

				} else {
					index = counter1;
				}

				js.executeScript("return " + newHook);
				paramMap.put("param1", Integer.toString(index));

				String clickOnJackpotDurationHook = xpathMap.get("ClickOnJackpotDuration");
				String durationClick = replaceParamInHook(clickOnJackpotDurationHook, paramMap);

				clickAtButton("return " + durationClick);

				// Start verifying calculations

				String jackpotHistoryDuration = (getConsoleText(
						"return window.theForce.game.automation.getControlById('ProgressiveWinHistoryWidgetComponent').tabText["
								+ index + "].text")).replaceAll("\\s+", "");

				System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++=");
				System.out.println(
						"==================Process jackpotHistoryDuration ::" + jackpotHistoryDurationArray[counter1]);
				System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++=");

				double megaValFromModel = 0.0;
				double majorValFromModel = 0.0;
				double minorValFromModel = 0.0;
				double miniValFromModel = 0.0;

				try {
					List<Long> jackpotModelList = getConsoleLongList(
							"return window.theForce.game.automation.getControlById('ProgressiveWinHistoryWidgetComponent').models.ProgressiveModel.getJackpotAmounts()");

					megaValFromModel = jackpotModelList.get(0);
					majorValFromModel = jackpotModelList.get(1);
					minorValFromModel = jackpotModelList.get(2);
					miniValFromModel = jackpotModelList.get(3);
				} catch (ClassCastException e) {
					Map<String, Long> jackpotBackendMap = getConsoleLongMap(
							"return window.theForce.game.automation.getControlById('ProgressiveWinHistoryWidgetComponent').models.ProgressiveModel.getJackpotAmounts()");

					megaValFromModel = jackpotBackendMap.get("0");
					majorValFromModel = jackpotBackendMap.get("1");
					minorValFromModel = jackpotBackendMap.get("2");
					miniValFromModel = jackpotBackendMap.get("3");
				}
				String[] durationArray = { xpathMap.get("getWinSummary_1week"), xpathMap.get("getWinSummary_2week"),
						xpathMap.get("getWinSummary_1month"), xpathMap.get("getWinSummary_6month"),
						xpathMap.get("getWinSummary_1year"), xpathMap.get("getWinSummary_2year")

				};

				for (int jacpotType = 0; jacpotType < 4; jacpotType++) {
					// read from the view
					/*
					 * double jackpotWin =Double.parseDouble((
					 * getConsoleText("return window.theForce.game.automation.getControlById('ProgressiveWinHistoryWidgetComponent').jackpotGroupsArray["
					 * +jacpotType+"].getChildAt(9).text")).replaceAll("[^0-9]", "")); double
					 * highestWinInView =Double.parseDouble((
					 * getConsoleText("return window.theForce.game.automation.getControlById('ProgressiveWinHistoryWidgetComponent').jackpotGroupsArray["
					 * +jacpotType+"].getChildAt(10).text")).replaceAll("[^0-9]", "")); double
					 * totalWinInView = Double.parseDouble((
					 * getConsoleText("return window.theForce.game.automation.getControlById('ProgressiveWinHistoryWidgetComponent').jackpotGroupsArray["
					 * +jacpotType+"].getChildAt(11).text")).replaceAll("[^0-9]", "")); double
					 * noOfWinInView = Double.parseDouble((
					 * getConsoleText("return window.theForce.game.automation.getControlById('ProgressiveWinHistoryWidgetComponent').jackpotGroupsArray["
					 * +jacpotType+"].getChildAt(12).text")).replaceAll("[^0-9]", "")); double
					 * lastWinInView = Double.parseDouble((
					 * getConsoleText("return window.theForce.game.automation.getControlById('ProgressiveWinHistoryWidgetComponent').jackpotGroupsArray["
					 * +jacpotType+"].getChildAt(13).text")).replaceAll("[^0-9]", "")); double
					 * averageWinInView = Double.parseDouble((
					 * getConsoleText("return window.theForce.game.automation.getControlById('ProgressiveWinHistoryWidgetComponent').jackpotGroupsArray["
					 * +jacpotType+"].getChildAt(14).text")).replaceAll("[^0-9]", "")); String
					 * avgWinTimeInView =
					 * getConsoleText("return window.theForce.game.automation.getControlById('ProgressiveWinHistoryWidgetComponent').jackpotGroupsArray["
					 * +jacpotType+"].getChildAt(15).text");
					 */
					paramMap.put("param1", Integer.toString(jacpotType));
					// read from the view
					String hook = xpathMap.get("getjackpotWin");
					newHook = replaceParamInHook(hook, paramMap);
					double jackpotWin = Double
							.parseDouble((getConsoleText("return " + newHook)).replaceAll("[^0-9]", ""));

					hook = xpathMap.get("gethighestWin");
					newHook = replaceParamInHook(hook, paramMap);
					String strhighestWinInView = getConsoleText("return " + newHook).replaceAll("[^0-9]", "");
					double highestWinInView = Double
							.parseDouble("".equals(strhighestWinInView) ? "0" : strhighestWinInView);

					hook = xpathMap.get("gettotalWin");
					newHook = replaceParamInHook(hook, paramMap);
					String strTotalWinInView = getConsoleText("return " + newHook).replaceAll("[^0-9]", "");
					double totalWinInView = Double.parseDouble("".equals(strTotalWinInView) ? "0" : strTotalWinInView);

					hook = xpathMap.get("getnoOfWin");
					newHook = replaceParamInHook(hook, paramMap);
					String strNoOfWinInView = getConsoleText("return " + newHook).replaceAll("[^0-9]", "");
					double noOfWinInView = Double.parseDouble("".equals(strNoOfWinInView) ? "0" : strNoOfWinInView);

					hook = xpathMap.get("getlastWin");
					newHook = replaceParamInHook(hook, paramMap);
					String strLastWinInView = (getConsoleText("return " + newHook)).replaceAll("[^0-9]", "");
					double lastWinInView = Double.parseDouble("".equals(strLastWinInView) ? "0" : strLastWinInView);

					hook = xpathMap.get("getaverageWin");
					newHook = replaceParamInHook(hook, paramMap);
					String strAverageWinInView = getConsoleText("return " + newHook).replaceAll("[^0-9]", "");
					double averageWinInView = Double
							.parseDouble("".equals(strAverageWinInView) ? "0" : strAverageWinInView);

					hook = xpathMap.get("getavgWinTime");
					newHook = replaceParamInHook(hook, paramMap);
					String strAvgWinTimeInView = getConsoleText("return " + newHook);
					String avgWinTimeInView = "-".equals(strAvgWinTimeInView) ? "0" : strAvgWinTimeInView;

					if (jacpotType == 0) {

						if (jackpotWin == megaValFromModel) {
							report.detailsAppendNoScreenshot(
									"<b>Verify the Mega win value for " + jackpotHistoryDurationArray[counter1]
											+ "</b>",
									"Expecrted value is :: " + megaValFromModel, "Actual Value is :: " + jackpotWin,
									"pass");
						} else {
							report.detailsAppendNoScreenshot(
									"<b>Verify the Mega win value for " + jackpotHistoryDurationArray[counter1]
											+ "</b>",
									"Expecrted value is :: " + megaValFromModel, "Actual Value is :: " + jackpotWin,
									"Fail");
						}

					} else if (jacpotType == 1) {
						if (jackpotWin == majorValFromModel) {
							report.detailsAppendNoScreenshot(
									"<b>Verify the Major win value for " + jackpotHistoryDurationArray[counter1]
											+ "</b>",
									"Expecrted value is :: " + majorValFromModel, "Actual Value is :: " + jackpotWin,
									"pass");
						} else {
							report.detailsAppendNoScreenshot(
									"<b>Verify the Major win value for " + jackpotHistoryDurationArray[counter1]
											+ "</b>",
									"Expecrted value is :: " + majorValFromModel, "Actual Value is :: " + jackpotWin,
									"Fail");
						}

					} else if (jacpotType == 2) {
						if (jackpotWin == minorValFromModel) {
							report.detailsAppendNoScreenshot(
									"<b>Verify the Minor win value for " + jackpotHistoryDurationArray[counter1]
											+ "</b>",
									"Expecrted value is :: " + minorValFromModel, "Actual Value is :: " + jackpotWin,
									"pass");
						} else {
							report.detailsAppendNoScreenshot(
									"<b>Verify the Minor win value for " + jackpotHistoryDurationArray[counter1]
											+ "</b>",
									"Expecrted value is :: " + minorValFromModel, "Actual Value is :: " + jackpotWin,
									"Fail");
						}
					} else if (jacpotType == 3) {
						if (jackpotWin == miniValFromModel) {
							report.detailsAppendNoScreenshot(
									"<b>Verify the Mini win value for " + jackpotHistoryDurationArray[counter1]
											+ "</b>",
									"Expecrted value is :: " + miniValFromModel, "Actual Value is :: " + jackpotWin,
									"pass");
						} else {
							report.detailsAppendNoScreenshot(
									"<b>Verify the Mini win value for " + jackpotHistoryDurationArray[counter1]
											+ "</b>",
									"Expecrted value is :: " + miniValFromModel, "Actual Value is :: " + jackpotWin,
									"Fail");
						}
					}
					// read from Model
					Map<String, Long> map = getConsoleLongMap(
							"return " + durationArray[counter1] + "[" + jacpotType + "]");

					if (map == null) {
						if (highestWinInView == 0.0) {
							report.detailsAppendNoScreenshot(
									"Highest win value verification for " + jackpotHistoryDurationArray[counter1],
									"Expecrted value is :: " + 0.0, "Actual Value is :: " + highestWinInView, "pass");
						} else {
							report.detailsAppendNoScreenshot(
									"Highest win value verification for " + jackpotHistoryDurationArray[counter1],
									"Expecrted value is :: " + 0.0, "Actual Value is :: " + highestWinInView, "Fail");
						}
						if (totalWinInView == 0.0) {
							report.detailsAppendNoScreenshot(
									"Total win value verification for " + jackpotHistoryDurationArray[counter1],
									"Expecrted value is :: " + 0.0, "Actual Value is :: " + totalWinInView, "pass");
						} else {
							report.detailsAppendNoScreenshot(
									"Total win value verification for " + jackpotHistoryDurationArray[counter1],
									"Expecrted value is :: " + 0.0, "Actual Value is :: " + totalWinInView, "Fail");
						}
						if (noOfWinInView == 0) {
							report.detailsAppendNoScreenshot(
									"Number of wins verification for " + jackpotHistoryDurationArray[counter1],
									"Expecrted value is :: " + 0.0, "Actual Value is :: " + noOfWinInView, "pass");
						} else {
							report.detailsAppendNoScreenshot(
									"Number of wins verification for " + jackpotHistoryDurationArray[counter1],
									"Expecrted value is :: " + 0.0, "Actual Value is :: " + noOfWinInView, "Fail");
						}
						if (lastWinInView == 0.0) {
							report.detailsAppendNoScreenshot(
									"Last win value verification for " + jackpotHistoryDurationArray[counter1],
									"Expecrted value is :: " + 0.0, "Actual Value is :: " + lastWinInView, "pass");
						} else {
							report.detailsAppendNoScreenshot(
									"Last win value verification for " + jackpotHistoryDurationArray[counter1],
									"Expecrted value is :: " + 0.0, "Actual Value is :: " + lastWinInView, "Fail");
						}
						if (averageWinInView == 0.0) {
							report.detailsAppendNoScreenshot(
									"Average win value verification for " + jackpotHistoryDurationArray[counter1],
									"Expecrted value is :: " + 0.0, "Actual Value is :: " + averageWinInView, "pass");
						} else {
							report.detailsAppendNoScreenshot(
									"Average win value verification for " + jackpotHistoryDurationArray[counter1],
									"Expecrted value is :: " + 0.0, "Actual Value is :: " + averageWinInView, "Fail");
						}
						if (avgWinTimeInView.equalsIgnoreCase("0")) {
							report.detailsAppendNoScreenshot(
									"Average win time value verification for " + jackpotHistoryDurationArray[counter1],
									"Expecrted value is :: 0", "Actual Value is :: " + avgWinTimeInView, "pass");
						} else {
							report.detailsAppendNoScreenshot(
									"Average win time value verification for " + jackpotHistoryDurationArray[counter1],
									"Expecrted value is :: 0", "Actual Value is :: " + avgWinTimeInView, "Fail");
						}
					} else {
						System.out.println(map);
						double maxWinAmount = map.get("maxWinAmount");
						double totalWinAmount = map.get("totalWinAmount");
						double winCount = map.get("winCount");
						double lastJackpotNumber = map.get("lastJackpotNumber");
						double avgWinAmount = map.get("avgWinAmount");
						long avgWinTime = map.get("avgWinTime");

						double lastWinAmt = getLastWinAmount(lastJackpotNumber);

						if (highestWinInView == maxWinAmount) {
							report.detailsAppendNoScreenshot(
									"Highest win value verification for " + jackpotHistoryDurationArray[counter1],
									"Expecrted value is :: " + maxWinAmount, "Actual Value is :: " + highestWinInView,
									"pass");
						} else {
							report.detailsAppendNoScreenshot(
									"Highest win value verification for " + jackpotHistoryDurationArray[counter1],
									"Expecrted value is :: " + maxWinAmount, "Actual Value is :: " + highestWinInView,
									"Fail");
						}
						if (totalWinInView == totalWinAmount) {
							report.detailsAppendNoScreenshot(
									"Total win value verification for " + jackpotHistoryDurationArray[counter1],
									"Expecrted value is :: " + totalWinAmount, "Actual Value is :: " + totalWinInView,
									"pass");
						} else {
							report.detailsAppendNoScreenshot(
									"Total win value verification for " + jackpotHistoryDurationArray[counter1],
									"Expecrted value is :: " + totalWinAmount, "Actual Value is :: " + totalWinInView,
									"Fail");
						}
						if (noOfWinInView == winCount) {
							report.detailsAppendNoScreenshot(
									"Number of wins verification for " + jackpotHistoryDurationArray[counter1],
									"Expecrted value is :: " + winCount, "Actual Value is :: " + noOfWinInView, "pass");
						} else {
							report.detailsAppendNoScreenshot(
									"Number of wins verification for " + jackpotHistoryDurationArray[counter1],
									"Expecrted value is :: " + winCount, "Actual Value is :: " + noOfWinInView, "Fail");
						}
						if (lastWinInView == lastWinAmt) {
							report.detailsAppendNoScreenshot(
									"Last win value verification for " + jackpotHistoryDurationArray[counter1],
									"Expecrted value is :: " + lastWinAmt, "Actual Value is :: " + lastWinInView,
									"pass");
						} else {
							report.detailsAppendNoScreenshot(
									"Last win value verification for " + jackpotHistoryDurationArray[counter1],
									"Expecrted value is :: " + lastWinAmt, "Actual Value is :: " + lastWinInView,
									"Fail");
						}
						if (averageWinInView == avgWinAmount) {
							report.detailsAppendNoScreenshot(
									"Average win value verification for " + jackpotHistoryDurationArray[counter1],
									"Expecrted value is :: " + avgWinAmount, "Actual Value is :: " + averageWinInView,
									"pass");
						} else {
							report.detailsAppendNoScreenshot(
									"Average win value verification for " + jackpotHistoryDurationArray[counter1],
									"Expecrted value is :: " + avgWinAmount, "Actual Value is :: " + averageWinInView,
									"Fail");
						}

						String strAvgWinTime = convertToMonthWeekssFormat(avgWinTime);
						if (avgWinTime == 0.0 && avgWinTimeInView.equalsIgnoreCase("0")) {
							report.detailsAppendNoScreenshot(
									"Average win time value verification for " + jackpotHistoryDurationArray[counter1],
									"Expecrted value is :: " + avgWinTime, "Actual Value is :: " + avgWinTimeInView,
									"pass");
						} else if (avgWinTimeInView.equalsIgnoreCase(strAvgWinTime)) {
							report.detailsAppendNoScreenshot(
									"Average win time value verification for " + jackpotHistoryDurationArray[counter1],
									"Expecrted value is :: " + strAvgWinTime, "Actual Value is :: " + avgWinTimeInView,
									"pass");
						} else {
							report.detailsAppendNoScreenshot(
									"Average win time value verification for " + jackpotHistoryDurationArray[counter1],
									"Expecrted value is :: " + strAvgWinTime, "Actual Value is :: " + avgWinTimeInView,
									"Fail");
						}

					}
				}

				for (int counter2 = 0; counter2 <= scroll + 1; counter2++) {

					report.detailsAppendFolder("verify the ProgressiveWinHistoryWidget screen shot",
							" ProgressiveWinHistoryWidget screen shot", "ProgressiveWinHistoryWidget screenshot ",
							"pass", languageCode);
					paramMap.put("param1", Double.toString(jackpotHistoryPageHeight));
					paramMap.put("param2", Integer.toString(counter2));

					String hook = xpathMap.get("scrollProgressiveWinHistoryWidget");
					newHook = replaceParamInHook(hook, paramMap);
					js.executeScript("return " + newHook);

					log.debug("Scrolling the ProgressiveWinHistoryWidget and taking screenshots");

				}

				if (!getConsoleBooleanText("return " + xpathMap.get("isJackpotHistoryRightArrowVisible"))) {
					break;
				} else if (index == 2) {
					clickAtButton("return " + xpathMap.get("ClickOnJackpotHistoryRightArrow"));
				}
			}

			log.debug("closing ProgressiveWinHistoryWidget");
			clickAtButton("return  " + jackpotTrophyCloseHook);
		} catch (Exception e) {
			log.error("Error While taking ProgressiveWinHistoryWidget screenshots", e);
			clickAtButton("return  " + jackpotTrophyCloseHook);
		}
	}

	double getLastWinAmount(double lastJackpotNumber) {

		double lastwinAmt = 0.0;
		List<Map<String, Long>> lastWinlist = getConsoleListMap("return " + xpathMap.get("getRecentWin"));

		for (Map<String, Long> map : lastWinlist) {
			long jackpotNumber = map.get("jackpotNumber");
			if (jackpotNumber == lastJackpotNumber) {
				lastwinAmt = map.get("winAmount");
				break;
			}
		}

		return lastwinAmt;
	}

	/*
	 * Author:Snehal Gaikwad Method Deescription: create dynamic hook by passing
	 * parameter as placeholder in hook Input:String Hook read from testdata.xls
	 * Map<String ,String> of parameterName and value to be replace Output:String
	 */
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

	public void refresh() {
		try {
			WebDriverWait wait = new WebDriverWait(webdriver, 60);

			webdriver.navigate().refresh();

			if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame"))
					|| (Constant.YES.equalsIgnoreCase(xpathMap.get("continueBtnOnGameLoad")))) {
				wait.until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath(xpathMap.get("OneDesign_NewFeature_ClickToContinue"))));
				webdriver.findElement(By.xpath(xpathMap.get("OneDesign_NewFeature_ClickToContinue"))).click();
				Thread.sleep(1000);
			} else {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("clock"))));
				Thread.sleep(2000);
				waitForSpinButton();
				// clickAtButton("return "+xpathMap.get("BaseIntroContinueBtn"));
			}

		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public void captureJackpotInfoPaytable(Mobile_HTML_Report report, String language) {
		try {
			JavascriptExecutor js = (webdriver);
			Coordinates coordinateObj = new Coordinates();
			Thread.sleep(2000);
			typeCasting("return " + xpathMap.get("JackpotInfoScrollHeight"), coordinateObj);
			coordinateObj.setPaytableFullHeight2(coordinateObj.getX());
			typeCasting("return " + xpathMap.get("JackpotInfoScroll_h"), coordinateObj);
			coordinateObj.setPaytableHeight2(coordinateObj.getX());
			int scroll = (int) (coordinateObj.getPaytableFullHeight2() / coordinateObj.getPaytableHeight2());

			for (int i = 1; i <= scroll + 1; i++) {
				js.executeScript("return " + forceNamespace
						+ ".getControlById('JackpotInfoPaytableComponent').storyScroll.scrollTo(0,-"
						+ coordinateObj.getPaytableHeight2() * i + ")");
				log.debug("Scrolling the jackpot info page and taking screenshots");

				report.detailsAppendFolderOnlyScreeshot(language);
			}
		} catch (Exception e) {
			log.error("error in captureJackpotInfoPaytable()", e);
		}
	}

	public void unlockBonus(Mobile_HTML_Report report) {

		String strbonusCount = xpathMap.get("NoOfBonusSelection");
		int bonusCount = (int) Double.parseDouble(strbonusCount);

		String selectBonus = xpathMap.get("SelectBonus");
		Map<String, String> paramMap = new HashMap<String, String>();
		ExcelDataPoolManager excelpoolmanager = new ExcelDataPoolManager();
		String testDataExcelPath = TestPropReader.getInstance().getProperty("TestData_Excel_Path");
		CommonUtil util = new CommonUtil();
		Map<String, String> rowData2 = null;
		Map<String, String> rowData3 = null;
		String languageDescription = null;
		String languageCode = null;
		String urlNew = null;
		// Reading the language code in to list map
		@SuppressWarnings("rawtypes")
		List<Map> list = util.readLangList();

		try {
			int rowCount2 = excelpoolmanager.rowCount(testDataExcelPath, "LanguageCodes");
			log.debug("Total number of Languages configured" + rowCount2);

			for (int iteamCnt = 0; iteamCnt < bonusCount; iteamCnt++) {
				paramMap.put("param1", Integer.toString(iteamCnt));
				String newSelectBonusHook = replaceParamInHook(selectBonus, paramMap);

				String clickOnBonus = "return " + newSelectBonusHook;
				clickAtButton(clickOnBonus);
				for (int j = 1; j < rowCount2; j++) {
					rowData2 = list.get(j);
					languageDescription = rowData2.get(Constant.LANGUAGE).trim();
					languageCode = rowData2.get(Constant.LANG_CODE).trim();
					log.debug("Waiting after language change,to bonus selction scene to come");
					elementWait("return " + xpathMap.get("currentScene"), "PLAYING_ACTIVE_BONUS");
					log.debug("Wait over");
					Thread.sleep(2000);
					report.detailsAppendFolder(
							"Verify Language translations on Bonus feature in  " + languageCode + " ",
							"Bonus scene should display in " + languageDescription + " ",
							"Bonus scene displays in " + languageDescription + " ,verify screenshot", "Pass",
							languageCode);
					if (j + 1 != rowCount2) {
						log.debug("Going for next lang");
						rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j + 1);
						String nextLanguage = rowData3.get(Constant.LANG_CODE).trim();
						log.debug("read next lang from excel" + nextLanguage);
						String currentUrl = webdriver.getCurrentUrl();

						if (currentUrl.contains("LanguageCode"))
							urlNew = currentUrl.replaceAll("LanguageCode=" + languageCode,
									"LanguageCode=" + nextLanguage);
						else if (currentUrl.contains("languagecode"))
							urlNew = currentUrl.replaceAll("languagecode=" + languageCode,
									"languagecode=" + nextLanguage);

						loadGame(urlNew);
						if ((Constant.YES.equalsIgnoreCase(xpathMap.get("continueBtnOnGameLoad")))) {
							newFeature();
						}
						log.debug("Load game with lang ::" + nextLanguage);
					} else {
						log.debug("Going to load eng lang");
						Thread.sleep(2000);
						rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", 1);
						String nextLanguage = rowData3.get(Constant.LANG_CODE).trim();
						log.debug("read next lang from excel" + nextLanguage);
						String currentUrl = webdriver.getCurrentUrl();
						if (currentUrl.contains("LanguageCode"))
							urlNew = currentUrl.replaceAll("LanguageCode=" + languageCode,
									"LanguageCode=" + nextLanguage);
						else if (currentUrl.contains("languagecode"))
							urlNew = currentUrl.replaceAll("languagecode=" + languageCode,
									"languagecode=" + nextLanguage);

						loadGame(urlNew);
						if ((Constant.YES.equalsIgnoreCase(xpathMap.get("continueBtnOnGameLoad")))) {
							newFeature();
						}
						log.debug("Load game with lang ::" + nextLanguage);
					}
				}
				Thread.sleep(2000);
			}

			report.detailsAppend("Unlock Bonus", "on selection of bonus,bonus should unlock",
					"bonus  unlock,verify screenshot", "pass");
		} catch (InterruptedException exception) {
			log.debug(exception.getMessage(), exception);
		}

		catch (IOException e) {
			log.error("Exception occur while reading  excel file", e);
		}

	}

	/*
	 * Description : This method will wait for specific amount of time for
	 * visibility of element Overload of public void elementWait(String
	 * element,boolean visibility,int waitTimeInSec)
	 * 
	 */
	public boolean elementWait(String element, boolean value, int waitTimeInSec) {
		long startTime = System.currentTimeMillis();
		while (true) {
			boolean isVisible = getConsoleBooleanText(element);
			if (isVisible == value) {
				return true;
			}
			long currentime = System.currentTimeMillis();
			// Break if wait is more than given secs
			if (((currentime - startTime) / 1000) > waitTimeInSec) {
				log.debug("Element  is not visible, break after " + waitTimeInSec + " sec");
				return false;
			}
			threadSleep(500);
		}
	}

	public void validateCreditForWinLoss(String currentBet, Mobile_HTML_Report report, String folderName) {
		try {
			// collect credit before spin
			String currentCreditAmount = getCurrentCredits();
			spinclick();

			boolean isCreditDeducted = isCreditDeducted(currentCreditAmount, currentBet);
			if (isCreditDeducted) {
				if (folderName != null) {
					report.detailsAppendFolder("validateCreditForWinLoss ",
							"On spin click credit balance should get updated", "Credit balance is updated", "pass",
							folderName);
				} else {
					report.detailsAppend("validateCreditForWinLoss ", "On spin click credit balance should get updated",
							"Credit balance is updated", "pass");

				}
			} else {
				if (folderName != null) {
					report.detailsAppendFolder("validateCreditForWinLoss ",
							"On spin click credit balance should get updated", "Credit balance is not updated", "Fail",
							folderName);
				} else {
					report.detailsAppend("validateCreditForWinLoss ", "On spin click credit balance should get updated",
							"Credit balance is not updated", "Fail");

				}
			}
			waitForWinDisplay();
			waitForSpinButtonstop();

			Thread.sleep(1000);
			boolean isPlayerWon = isPlayerWon();
			if (isPlayerWon) {
				boolean isWinAddedToCredit = isWinAddedToCredit(currentCreditAmount, currentBet);

				if (isWinAddedToCredit) {
					if (folderName != null) {
						report.detailsAppendFolder("validateCreditForWinLoss ", "On Win ,credit should updated",
								"on Win ,Credit balance is updated", "pass", folderName);
					} else {
						report.detailsAppend("validateCreditForWinLoss ", "On Win ,credit should updated",
								"On win Credit balance is updated", "pass");

					}
				} else {
					if (folderName != null) {
						report.detailsAppendFolder("validateCreditForWinLoss ", "On Win ,credit should updated",
								"Credit balance is not updated", "Fail", folderName);
					} else {
						report.detailsAppend("validateCreditForWinLoss ", "On Win ,credit should updated",
								"Credit balance is not updated", "Fail");

					}
				}
			} else {
				if (folderName != null) {
					report.detailsAppendFolder("validateCreditForWinLoss ", "After spin ,there is no win occur", "",
							"Fail", folderName);
				} else {
					report.detailsAppend("validateCreditForWinLoss ", "AfterSpin ,there is no win occur", "", "Fail");

				}
			}

		} catch (Exception exception) {
			log.error(exception.getMessage(), exception);
		}
	}

	/**
	 * To verify bonus game win currency format
	 */

	public boolean bonusWinCurrFormat(String currencyFormat) {
		String bonuswin = null;
		boolean isWinInCurrencyFormat = false;
		try {
			log.debug("Function -> bonuswinCurrencyFormat()");
			if (!GameName.contains("Scratch")) {
				bonuswin = "return " + xpathMap.get("BonusWinAmount");
			} else {
				// need to update the hook for Scratch game
				bonuswin = "return " + xpathMap.get("InfobarBettext");
			}
			String bonuswinnew = getConsoleText(bonuswin);
			System.out.println("Bonus win text from console " + bonuswinnew);
			// To validate currency Amount format
			isWinInCurrencyFormat = currencyFormatValidator(bonuswinnew, currencyFormat);

			log.debug("Fetching Bonus win currency symbol" + "/n Bonus win currency symbol is::" + bonuswin);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return isWinInCurrencyFormat;

	}

	/**
	 * To click on the continue button after bonus game completion
	 */
	public String clickContinueInBonusGame() {

		String ret = null;
		WebDriverWait wait = new WebDriverWait(webdriver, 5);
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("clock"))));
			elementWait("return " + xpathMap.get("IsBonusContinueBtnVisible"), true);
			boolean flag = getConsoleBooleanText("return " + xpathMap.get("IsBonusContinueBtnVisible"));
			if (flag) {
				Thread.sleep(4000);
				clickAtButton("return  " + xpathMap.get("continueBtnBonus"));
				System.out.println("Clicked Bonus game free spin entry screen continue button to start Free spins");
				log.debug("Clicked Bonus game free spin entry screen continue button to start Free spins");
				ret = "Clicked";
			} else {
				log.debug("No  continue button available in Bonus game free spins entry screen");
				ret = "NotClicked";
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return ret;
	}

	/**
	 * To select the bonus books in bonus game(hotInk)
	 */
	public boolean bonusSelection(String currencyFormat, int iteamCnt) {
		boolean isBonusInCurrencyFormat = false;
		try {
			String bonusSelect = xpathMap.get("BonusSelection");
			Map<String, String> paramMap = new HashMap<String, String>();

			paramMap.put("param1", Integer.toString(iteamCnt));
			String newBonusBook = "return " + replaceParamInHook(bonusSelect, paramMap);
			Thread.sleep(3000);
			String bonusBookWin = getConsoleText(newBonusBook);
			System.out.println("Bonus win text from console " + bonusBookWin);

			isBonusInCurrencyFormat = currencyFormatValidator(bonusBookWin, currencyFormat);
			log.debug("Fetching Bonus win currency symbol" + "/n Bonus win currency symbol is:: " + bonusBookWin);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return isBonusInCurrencyFormat;
	}

	/**
	 * To validate currency amount with the currency format in excel sheet
	 */
	public boolean currencyFormatValidator(String curencyAmount, String currencyFormat) {
		boolean isCurrencyFormat = false;
		String strCurrencyExp = null;
		try {
			String currencyregexp = createregexp(curencyAmount, currencyFormat);
			if (currencyregexp.contains("$"))
				strCurrencyExp = currencyregexp.replace("$", "\\$");
			else
				strCurrencyExp = currencyregexp;
			Pattern pattren = Pattern.compile(strCurrencyExp);
			String currencyexp = strCurrencyExp.replace("#", "\\d");

			if (Pattern.matches(currencyexp, curencyAmount)) {
				isCurrencyFormat = true;
			} else {
				isCurrencyFormat = false;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return isCurrencyFormat;

	}

	/**
	 * To verify piggybank bonus in bust the bank
	 * 
	 * @return
	 */
	public boolean piggyBankBonus(String currencyFormat) {
		String bonuswin = null;
		boolean isBonusInCurrencyFormat = false;
		try {
			log.debug("Function -> piggybonuswinCurrencyFormat()");
			if (!GameName.contains("Scratch")) {
				bonuswin = "return " + xpathMap.get("PiggyBonusAmount");
			}
			String bonuswinnew = getConsoleText(bonuswin);
			System.out.println("Bonus win text from the console to check currency format " + bonuswinnew);

			isBonusInCurrencyFormat = currencyFormatValidator(bonuswinnew, currencyFormat);
			log.debug("Fetching Bonus win currency symbol" + "/n Bonus win currency symbol is::" + bonuswinnew);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return isBonusInCurrencyFormat;
	}

	/**
	 * To verify Safe bonus bonus in bust the bank
	 * 
	 * @return
	 */
	public boolean verifySafeBonusCurrency(String currencyFormat) {
		String bonuswin = null;
		boolean isBonusInCurrencyFormat = false;
		try {
			log.debug("Function -> safebonuswinCurrencyFormat()");
			if (!GameName.contains("Scratch")) {
				bonuswin = "return " + xpathMap.get("SafeBonusWinAmount");
			}
			String bonuswinnew = getConsoleText(bonuswin);
			System.out.println("Safe Bonus win text from the console to check currency format " + bonuswinnew);

			isBonusInCurrencyFormat = currencyFormatValidator(bonuswinnew, currencyFormat);
			log.debug("Fetching Safe Bonus win currency symbol" + "/n Bonus win currency symbol is::" + bonuswinnew);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return isBonusInCurrencyFormat;
	}

	/**
	 * This method is used to check whether Topbar is present or not
	 * 
	 * @return
	 */
	public boolean verifyTopBarVisible() {

		boolean isTopBarPresentinGame = false;
		try {
			isTopBarPresentinGame = webdriver.findElement(By.xpath(xpathMap.get("Denmark_TopBar"))).isDisplayed();
			// System.out.println(isTopBarPresentinGame);
			if (isTopBarPresentinGame) {
				log.debug("Topbar is visible");
				System.out.println("Topbar is visible");
				isTopBarPresentinGame = true;
			}
		} catch (Exception e) {
			log.error("Not able to verify Topbar", e);
		}

		return isTopBarPresentinGame;
	}

	/**
	 * This method is used for verifying whether quick spin is present or not in the
	 * Italy Market
	 * 
	 * @return true
	 */

	public boolean italy_IsQuickspinAvailable() {
		try {

			WebElement clock = webdriver.findElement(By.id(xpathMap.get("clock_ID"))); // Clock on the Top Screen
			clock.getText();
			System.out.println("Clock is visible");
			System.out.println("Time is " + clock.getText()); // Get the clock text

			boolean isQuickspinVisible = getConsoleBooleanText("return " + xpathMap.get("Quickspin "));
			// System.out.println(isQuickspinVisible);
			if (isQuickspinVisible) {
				log.debug("Quickspin is available");
				System.out.println("Quickspin is available");
				return true;
			} else {
				log.error("Quickspin is not available");
				System.out.println("Quickspin is not available");
				return false;
			}
		} catch (Exception e) {
			log.error("Not able to verify quickspin Availability", e);
		}
		return true;
	}

	/**
	 * This method is used for verifying whether stop button is present or not in
	 * the Italy Market
	 * 
	 * @return true
	 */
	public boolean italy_isStopButtonAvailable() {
		boolean isstopbutton = true;
		try {

			clickAtButton("return " + xpathMap.get("ClickSpinBtn")); // Click on spin button
			// To verify the stop button
			String stopbutton = getConsoleText("return " + xpathMap.get("SpinBtnCurrState"));

			if (stopbutton.equalsIgnoreCase("activeSecondary") || !(stopbutton.equalsIgnoreCase("disabled"))) {
				// As current state of button is ActiveSecondary
				isstopbutton = true;
				log.debug("Stop button is available");
				System.out.println("Stop button is available");
			} else {
				isstopbutton = false;
				log.debug("Stop button is not available");
				System.out.println("Stop button is not available");
			}
		} catch (Exception e) {
			log.debug(e.getMessage());
			log.debug("Exception, Stop button is not available");
			System.out.println("Exception, Stop button is not available");
		}

		return isstopbutton;
	}

	/**
	 * This method is used To click on continue button in session reminder
	 */
	public void selectContinueSession() {
		try {
			webdriver.findElement(By.xpath(xpathMap.get("SessionContinue"))).click();
			log.debug("clicked on continue in session reminder dialog box");
		} catch (Exception e) {
			log.debug("Unable to click on session continue ", e);
		}
	}

	/**
	 * verify if session reminder is visible or not
	 * 
	 */

	public boolean verifySessionReminderPresent(Mobile_HTML_Report report) {
		boolean isSessionReminderVisible = false;
		WebDriverWait Wait = new WebDriverWait(webdriver, 500);
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("SessionReminder"))));// If
																													// the
																													// session
																													// reminder
																													// present
																													// It
																													// will
																													// click
																													// on
																													// continue
			List<WebElement> elementList = webdriver.findElements(By.xpath(xpathMap.get("SessionReminder"))); // Search
																												// on
																												// the
																												// page.

			if (elementList.size() > 0) {
				log.debug("Session reminder found");
				report.detailsAppend("Verify Session Reminder ", "Session Reminder is visible",
						"Session Reminder is visible", "PASS");
				isSessionReminderVisible = true;
			}
		} catch (Exception e) {
			log.error("Not able to verify session reminder status", e);
			isSessionReminderVisible = false;
		}
		return isSessionReminderVisible; // if it return false then there is no session reminder present on the screen
	}

	public boolean isQuickspinAvailable() {
		try {
			boolean isQuickspinVisible = getConsoleBooleanText("return " + xpathMap.get("QuickSpinBtnAvailable"));
			if (isQuickspinVisible) {
				log.debug("Quickspin is available");
				System.out.println("Quickspin is available");
				return true;
			} else {
				log.error("Quickspin is not available");
				System.out.println("Quickspin is not available");
				return false;
			}
		} catch (Exception e) {
			log.error("Not able to verify quickspin Availability", e);
		}
		return false;
	}

	/*
	 * Verify Stop Button
	 */
	public boolean isStopButtonAvailablity() {
		boolean isstopbutton = false;
		try {

			clickAtButton("return " + xpathMap.get("ClickSpinBtn")); // Click on spin button
			// To verify the stop button
			String stopbutton = getConsoleText("return " + xpathMap.get("SpinBtnCurrState"));

			if (stopbutton.equalsIgnoreCase("activeSecondary") || !(stopbutton.equalsIgnoreCase("disabled"))) {

				// As current state of button is ActiveSecondary
				isstopbutton = true;
				log.debug("Stop button is available");
				System.out.println("Stop button is available");
			} else {
				isstopbutton = false;
				log.debug("Stop button is not available");
				System.out.println("Stop button is not available");
			}
		} catch (Exception e) {
			log.debug(e.getMessage());
			log.debug("Exception, Stop button is not available");
			System.out.println("Exception, Stop button is not available");
		}

		return isstopbutton;
	}

	/**
	 * To validate bet is added to wagers correctly
	 */
	public boolean isBetAddedToWagers(String wagersB4Spin, double dblWagersAfterStop, double dblBetValue) {
		boolean isBetAddedToWagers = false;

		double dblWagersB4Spin = Double.parseDouble(wagersB4Spin.replaceAll("[^0-9]", ""));
		if ((dblWagersB4Spin + dblBetValue) == dblWagersAfterStop) {
			log.debug("wagers updated succesfully");
			isBetAddedToWagers = true;
		} else {
			log.debug("wagers is not added");
		}

		return isBetAddedToWagers;
	}

	/**
	 * To validate win is added to payouts correctly
	 */
	public boolean isWinAddedToPayout(String payoutb4Spin, double dblPayoutAfterStop) {
		boolean isWinAddedToPayout = false;

		String winAmount = null;
		if (!GameName.contains("Scratch")) {
			winAmount = getConsoleText("return " + xpathMap.get("WinMobileText"));
		} else {
			winAmount = getConsoleText("return " + xpathMap.get("WinText"));
		}
		log.debug("win amount: " + winAmount);
		double dblPayoutB4Spin = Double.parseDouble(payoutb4Spin.replaceAll("[^0-9]", ""));
		double dblWinAmount = Double.parseDouble(winAmount.replaceAll("[^0-9]", ""));

		if ((dblPayoutB4Spin + dblWinAmount) == dblPayoutAfterStop) {
			log.debug("win added to payout succesfully");
			isWinAddedToPayout = true;
		}
		return isWinAddedToPayout;
	}

	/**
	 * To validate balance is updated correctly
	 */
	public boolean isBalanceUpdated(double dblPayoutAfterStop, double dblWagersAfterStop, double dblBetValue) {
		boolean isBalanceUpdated = false;

		String BalanceAfterStop = webdriver.findElement(By.xpath(xpathMap.get("spainBalance"))).getText();
		double dblBalanceAfterStop = Double.parseDouble(BalanceAfterStop.replaceAll("[^0-9.-]", ""));
		log.debug("Balance after spin stop: " + BalanceAfterStop);

		if (dblBalanceAfterStop == (dblPayoutAfterStop - dblWagersAfterStop)) {
			log.debug("Balance updated successfully");
			isBalanceUpdated = true;
		} else {
			log.debug("Balance not updated successfully");
			isBalanceUpdated = false;

		}
		return isBalanceUpdated;
	}

	/**
	 * To validate balance, payouts and wagers updated correctly and verify slot
	 * loss limit overlay
	 */
	public boolean waitUntilSessionLoss(String lossLimit) {
		boolean title = false;
		String betValue = getConsoleText("return " + xpathMap.get("BetSizeText"));

		double dblLossLimit = Double.parseDouble(lossLimit.replaceAll("[^0-9]", ""));
		double dblBetValue = Double.parseDouble(betValue.replaceAll("[^0-9]", ""));
		try {
			for (int i = 0; i <= 5; i++) {
				String payoutb4Spin = webdriver.findElement(By.xpath(xpathMap.get("spainPayouts"))).getText();
				String wagersB4Spin = webdriver.findElement(By.xpath(xpathMap.get("spainWagers"))).getText();
				String balanceB4Spin = webdriver.findElement(By.xpath(xpathMap.get("spainBalance"))).getText();

				double dblBalanceB4Spin = Double.parseDouble(balanceB4Spin.replaceAll("[^0-9.-]", ""));
				log.debug("wagers before spin: " + wagersB4Spin + " payouts before spin: " + payoutb4Spin
						+ " balance before spin: " + balanceB4Spin + " for spin: " + i);
				if (((-dblBetValue) + (dblBalanceB4Spin)) <= (-dblLossLimit)) {
					waitForSpinButton();
					Thread.sleep(2000);
					spinclick();
					Thread.sleep(3000);
					webdriver.findElements(By.xpath(xpathMap.get("spain_lossLimitDialogueOK")));
					log.debug("Loss Limit is reached");
					title = true;
					break;
				} else {
					waitForSpinButton();
					Thread.sleep(2000);
					spinclick();
					Thread.sleep(4000);

					String wagersAfterStop = webdriver.findElement(By.xpath(xpathMap.get("spainWagers"))).getText();

					String payoutAfterStop = webdriver.findElement(By.xpath(xpathMap.get("spainPayouts"))).getText();

					double dblWagersAfterStop = Double.parseDouble(wagersAfterStop.replaceAll("[^0-9]", ""));
					double dblPayoutAfterStop = Double.parseDouble(payoutAfterStop.replaceAll("[^0-9]", ""));

					log.debug("wagers after spin stop: " + wagersAfterStop + " payouts after spin stop: "
							+ payoutAfterStop + " for spin: " + i);

					boolean isBetAdded = isBetAddedToWagers(wagersB4Spin, dblWagersAfterStop, dblBetValue);//
					if (isBetAdded) {
						System.out.println("Bet is added to wagers");
					} else {
						System.out.println("Bet is not added to wagers");
					}
					boolean isPlayerWon = isPlayerWon();//
					if (isPlayerWon) {
						boolean isWinAddedToPayout = isWinAddedToPayout(payoutb4Spin, dblPayoutAfterStop);//
						try {
							if (isWinAddedToPayout) {
								isBalanceUpdated(dblPayoutAfterStop, dblWagersAfterStop, dblBetValue);

							} else {
								log.debug("win is not added to payout");
							}
						} catch (Exception e) {
							log.error(e.getMessage(), e);
						}
					} else {
						isBalanceUpdated(dblPayoutAfterStop, dblWagersAfterStop, dblBetValue);
						Thread.sleep(2000);
					}

				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return title;
	}

	/**
	 * To open Responsible Gaming from topbar
	 * 
	 * @return
	 */
	public boolean openResponsibleGamingFromTopbar(Mobile_HTML_Report report) {
		boolean ret = false;
		Wait = new WebDriverWait(webdriver, 500);
		try {
			String gameurl = webdriver.getCurrentUrl();
			webdriver.findElement(By.xpath(xpathMap.get("isResponsibleGamingAvailable"))).click();

			Thread.sleep(3000);
			log.debug("clicked on Responsible");
			System.out.println("clicked on ResponsibleGameing ");
			Thread.sleep(3000);
			checkpagenavigation(report, gameurl); // Page Navigation
			log.debug("Back to the Game Screen from ResponsibleGameing");
			System.out.println("Back to the Game Screen from ResponsibleGameing");
			Thread.sleep(6000);
			ret = true;
		} catch (Exception e) {
			log.error("Error in navigetion to ResponsibleGameing  page ", e);
		}
		return ret;

	}

	/**
	 * To open Help from topbar
	 */
	public boolean openHelpFromTopbar(Mobile_HTML_Report report) {
		boolean ret = false;
		Wait = new WebDriverWait(webdriver, 500);
		try {
			String gameurl = webdriver.getCurrentUrl();
			MobileElement element = (MobileElement) webdriver.findElement(By.xpath(xpathMap.get("Denmark_Help")));
			webdriver.executeScript("arguments[0].click();", element);
			report.detailsAppend("Verify Top Bar Help Text ", "Help is clicked", "Help is clicked", "PASS");
			Thread.sleep(5000);
			log.debug("clicked on Help");
			System.out.println("clicked on Help ");
			Thread.sleep(2000);
			checkpagenavigation(report, gameurl); // Checkpage Navigation
			Thread.sleep(2000);
			log.debug("Back to the Game Screen from Help");
			System.out.println("Back to the Game Screen from Help");
			ret = true;
		} catch (Exception e) {
			log.error("Error in navigetion to help page ", e);
			report.detailsAppend("Verify Top Bar Help Text ", "Help is not  displayed", "Help is not displayed",
					"FAIL");
		}
		return ret;
	}

	/**
	 * To open PlayerProtection from Topbar
	 */
	public boolean openPlayerProtectionFromTopbar(Mobile_HTML_Report report) {
		boolean ret = false;
		Wait = new WebDriverWait(webdriver, 900);
		try {
			String gameurl = webdriver.getCurrentUrl();

			MobileElement element = (MobileElement) webdriver
					.findElement(By.xpath(xpathMap.get("Denmark_PlayerProtection")));
			webdriver.executeScript("arguments[0].click();", element);
			report.detailsAppend(" Verify PlayerProtection  ", "PlayerProtection Clicked ", "PlayerProtection Clicked",
					"PASS");
			Thread.sleep(4000);
			log.debug("clicked on Player Protection");
			System.out.println("clicked on Player Protection");
			Thread.sleep(2000);

			checkpagenavigation(report, gameurl);
			System.out.println("Page navigated to Back to Game ");
			log.debug("Page navigated to Back to Game");
			ret = true;
		} catch (Exception e) {
			log.error("Error in navigetion to help page ", e);
			report.detailsAppend(" Verify PlayerProtection  ", "PlayerProtection isn't  Clicked ",
					"PlayerProtection isn't Clicked", "FAIL");
		}
		return ret;
	}

	/**
	 * This method is used to find the reel spin duration for a single spin
	 */
	public long reelSpinDuration() {
		long reelSpinTime = 0;
		try {
			Thread.sleep(1000);
			spinclick();
			long start = System.currentTimeMillis();
			System.out.println(start);
			waitForSpinButtonstop();
			long finish = System.currentTimeMillis();
			System.out.println(finish);
			reelSpinTime = finish - start;
			System.out.println(reelSpinTime);
			log.debug(reelSpinTime);
		}

		catch (Exception e) {
			log.error("Not able to verify reelspin time", e);
		}
		return reelSpinTime;
	}

	/*
	 * Italy Page check Navigation for Menu
	 */
	public void italy_Checkpagenavigation(Mobile_HTML_Report report, String gameurl) {
		try {
			String mainwindow = webdriver.getWindowHandle();

			Set<String> s1 = webdriver.getWindowHandles();
			if (s1.size() > 1) {
				Iterator<String> i1 = s1.iterator();
				while (i1.hasNext()) {
					String ChildWindow = i1.next();
					if (mainwindow.equalsIgnoreCase(ChildWindow)) {
						// ChildWindow=i1.next();
						webdriver.switchTo().window(ChildWindow);
						String url = webdriver.getCurrentUrl();
						System.out.println("Reload URL is :: " + url);
						log.debug("Reload URL is :: " + url);
						if (!url.equalsIgnoreCase(gameurl)) {
							// pass condition for navigation
							// report.detailsAppend("verify the Navigation screen shot ", " Navigation page
							// screen shot", "Navigation page screenshot ", "PASS");
							log.debug("Page navigated succesfully");
							System.out.println("Page navigated succesfully");
							webdriver.close();
						} else {
							log.debug("Now On game page");
						}
					}
				}
				webdriver.switchTo().window(mainwindow);
			} else {
				String url = webdriver.getCurrentUrl();
				System.out.println("Reloaded URL is :: " + url);
				log.debug("Reloaded URL is :: " + url);
				if (!url.equalsIgnoreCase(gameurl)) {
					// pass condition for navigation
					// report.detailsAppend("verify the Navigation screen shot", " Navigation page
					// screen shot", "Navigation page screenshot ", "PASS");
					System.out.println("Page navigated succesfully");
					log.debug("Page navigated succesfully");
					webdriver.navigate().to(gameurl);

					// Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("userInput"))));
					// //Gameplay Dialog Box
					// String amount1= xpathMap.get("Italy_amount"); //Italy amount from Excel
					// italy_ScrollBarCickAmount(report, amount1); // method for Scroll Bar

					newFeature();
				} else {
					webdriver.navigate().to(gameurl);
					waitForSpinButton();
					log.debug("Now On game page");
				}
			}
			// webdriver.navigate().to(gameurl);
		} catch (Exception e) {
			log.error("error in navigation of page");
		}
	}

	public boolean currency_check(Mobile_HTML_Report report) {
		String balance = null;
		String consoleBalance = null;
		boolean credit;

		if (!GameName.contains("Scratch")) {
			if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame")))// force console
			{
				balance = "return " + xpathMap.get("Balancetext");
			} else
				balance = "return " + xpathMap.get("Balancetext");
		} else {
			balance = "return " + xpathMap.get("InfcaseoBarBalanceTxt");
		}

		consoleBalance = GetConsoleText(balance);
		System.out.println("Console Balanace is " + consoleBalance);

		String str = consoleBalance;
		System.out.println("String: " + str);
		// int index = str.indexOf("€");
		// System.out.printf("Curreny '€' is at index %d\n", index);

		int index = str.indexOf("kr");
		System.out.printf("Curreny 'kr' is at index : %d\n", index);

		/*
		 * String consolebalance = consoleBalance.replace("CREDITS: ","" );
		 * 
		 * System.out.println("Console Balanace is"+consolebalance);
		 * 
		 * char currencySymbolIndex= consolebalance.charAt(0); //
		 * System.out.println("Currency symbol from console "+currencySymbolIndex);
		 * 
		 * String currencySymbol = Character.toString(currencySymbolIndex);
		 * System.out.println("Currency symbol from console"+currencySymbol);
		 * 
		 * String currenysymbolfromexcel = XpathMap.get("ItalySymbol");
		 * System.out.println("Currency symbol from Excel"+currenysymbolfromexcel);
		 */
		if (index != -1) {
			System.out.println("YES, The Currency is Same ");
			credit = true;
		} else {
			System.out.println("NO, The currency isn't Same");
			credit = false;
		}
		return credit;

	}

	// refresh the page
	public void refresh(Mobile_HTML_Report report) {
		try {
			WebDriverWait wait = new WebDriverWait(webdriver, 30);

			webdriver.navigate().refresh();
			Thread.sleep(4000);
			report.detailsAppend("Verfiy Refresh", "Refreshed Suceessfuly", "Refreshed Suceessfuly", "PASS");
			if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame"))
					|| (Constant.YES.equalsIgnoreCase(xpathMap.get("continueBtnOnGameLoad")))) {
				wait.until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath(xpathMap.get("OneDesign_NewFeature_ClickToContinue"))));
				webdriver.findElement(By.xpath(xpathMap.get("OneDesign_NewFeature_ClickToContinue"))).click();
				report.detailsAppend("Verfiy Continue", "Refreshed and clicked on Continue",
						"Refreshed and clicked on Continue", "PASS");

			} else {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("clock"))));
				Thread.sleep(1000);
				// clickAtButton("return "+XpathMap.get("BaseIntroContinueBtn"));
			}

		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	/*
	 * Close for Denmark on Topbar
	 */
	public boolean closeSymbol(Mobile_HTML_Report report) {

		boolean isclose = false;
		try {

			// If the session reminder present It will click on continue
			List<WebElement> elementList = webdriver
					.findElements(By.xpath(xpathMap.get("Denmark_CloseOnTheTopBarMenu")));

			if (elementList.size() > 0) {
				MobileElement element = (MobileElement) webdriver
						.findElement(By.xpath(xpathMap.get("Denmark_CloseOnTheTopBarMenu")));
				webdriver.executeScript("arguments[0].click();", element);

				report.detailsAppend("Verify close ", "close is Clicked", "close is Clicked", "PASS");
				log.debug("close is Clicked \n");
				System.out.print("close is Clicked");
				isclose = true;
			}

			else {
				log.debug("close isn't Clicked");
				System.out.print("close isn't Clicked");
				report.detailsAppend("Verify close ", "close isn't Clicked", "close isn't Clicked", "FAIL");
			}
		}

		catch (Exception e) {
			log.error("Not able to verify Close status", e);
			System.out.print("Close not found");
			isclose = false;
		}
		return isclose;
	}

	/**
	 * This method is used for verifying whether quick spin is present or not
	 * 
	 * @return true
	 */

	public boolean verifyQuickspinAvailablity() {
		boolean isQuickSpin = false;
		try {

			boolean isQuickspinVisible = getConsoleBooleanText("return " + xpathMap.get("QuickSpinBtnAvailable"));
			// System.out.println(isQuickspinVisible);
			if (isQuickspinVisible) {
				log.debug("Quickspin is available");
				System.out.println(" Quickspin is available");
				return true;
			} else {
				log.error("Quickspin is not available");
				System.out.println("Quickspin is not available");
				return false;
			}
		} catch (Exception e) {
			log.error("Not able to verify quickspin Availability", e);
		}
		return isQuickSpin;
	}

	/**
	 * Verify Session Reminder is Present or not
	 */
	public boolean sessionReminderPresent(Mobile_HTML_Report report) {
		boolean isSessionReminderVisible = false;
		WebDriverWait Wait = new WebDriverWait(webdriver, 250);
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("SessionReminder"))));
			List<WebElement> elementList = webdriver.findElements(By.xpath(xpathMap.get("SessionReminder")));
			if (elementList.size() > 0) {
				log.debug("Session Reminder found");
				report.detailsAppend("Verify Session Reminder ", "Session Reminder is Present",
						"Session Reminder is Present", "PASS");
				MobileElement element = (MobileElement) webdriver
						.findElement(By.xpath(xpathMap.get("SessionReminder")));
				webdriver.executeScript("arguments[0].click();", element);
				Thread.sleep(2000);
				log.debug("Session reminder clicked");
				isSessionReminderVisible = true;
			} else {
				log.debug("Session reminder not  found");
				report.detailsAppend("Verify Session Reminder ", "Session Reminder is not Clicked",
						"Session Reminder is not Clicked", "FAIL");
			}
		} catch (Exception e) {
			log.error("Not able to verify session reminder status", e);
			isSessionReminderVisible = false;
		}
		return isSessionReminderVisible; // if it return false then there is no session reminder present on the screen
	}

	/*
	 * To verify Bonus Reminder
	 */
	public void verifyBonusReminder(Mobile_HTML_Report report) {
		boolean isTearmsAndConditionsNaviagted = false;
		String isBonusReminderAvailable = webdriver.findElement(By.xpath(xpathMap.get("isBonusReminder"))).getText();
		try {
			System.out.println(isBonusReminderAvailable);
			if (isBonusReminderAvailable.equals("Bonus Reminder")) {
				report.detailsAppend("Verify Bonus Reminder must be is available",
						"Bonus Reminder Should be is availabler", "Bonus Reminder is available", "pass");
				String gameurl = webdriver.getCurrentUrl();
				webdriver.findElement(By.xpath(xpathMap.get("clickOnTermsAndConditions"))).click();
				log.debug("clicked on Terms and conditions in Bonus Reminder");
				Thread.sleep(3000);
				checkpagenavigation(report, gameurl);
				System.out.println("Game navigated to Terms and Conditions");
				isTearmsAndConditionsNaviagted = true;
				Thread.sleep(2000);
				if (isTearmsAndConditionsNaviagted) {
					log.debug("Terms and Conditions navigation verified succesfully");
					// report.detailsAppend("Verify the Navigation to Help screen", "Navigation to
					// Help screen", "Navigation to Help screen is Done", "pass");
				} else {
					report.detailsAppend("Verify that Navigation to Terms and Conditions",
							"Navigation to Terms and Conditions screen",
							"Navigation to Terms and Conditions screen not Done", "fail");
				}
			} else {

			}

		}

		catch (Exception e) {
			log.error("Error in checking Bonus Reminder ", e);

		}
	}

	/**
	 * To find the net profit in uk market
	 * 
	 * @pb61055
	 */
	public boolean verifyNetPosition(Mobile_HTML_Report report) {
		boolean isNetPositionUpdated = false;
		String isNetProfitAvailable = webdriver.findElement(By.xpath(xpathMap.get("isNetPosition"))).getText();
		try {
			if (isNetProfitAvailable.contains("Net position")) {
				report.detailsAppend("Verify net position on topbar",
						"Net position should be visible and updated correctly",
						"Net position is visible and updated correctly", "pass");

				for (int i = 0; i < 2; i++) {
					String netPositionBeforeSpin = webdriver.findElement(By.xpath(xpathMap.get("isNetPosition")))
							.getText();
					double dblNetPositionBeforeSpin = Double
							.parseDouble(netPositionBeforeSpin.replaceAll("[^0-9.-]", "").replace(".", ""));

					spinclick();
					waitForSpinButton();
					Thread.sleep(10000);

					String netPositionAfterSpin = webdriver.findElement(By.xpath(xpathMap.get("isNetPosition")))
							.getText();
					double dblNetPositionAfterSpin = Double
							.parseDouble(netPositionAfterSpin.replaceAll("[^0-9.-]", "").replace(".", ""));

					String betValue = getConsoleText("return " + xpathMap.get("BetSizeText"));
					double dblBetValue = Double.parseDouble(betValue.replaceAll("[^0-9]", ""));

					Thread.sleep(2000);

					boolean isPlayerWon = isPlayerWon();
					if (isPlayerWon) {
						String winAmount = null;
						if (!GameName.contains("Scratch")) {
							winAmount = getConsoleText("return " + xpathMap.get("WinMobileText"));
						} else {
							winAmount = getConsoleText("return " + xpathMap.get("WinText"));
						}
						double dblWinAmount = Double.parseDouble(winAmount.replaceAll("[^0-9]", ""));

						try {
							double dblBSandBetValue = dblNetPositionBeforeSpin - dblBetValue;
							if ((dblBSandBetValue + dblWinAmount) == dblNetPositionAfterSpin) {
								System.out.println("Win is added to net position successfully");
								isNetPositionUpdated = true;
							} else {
								System.out.println("Win is not added to net position successfully");
								isNetPositionUpdated = false;
							}
						} catch (Exception e) {
							log.error("Error in checking help page ", e);
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
					if (isNetPositionUpdated) {
						report.detailsAppend("verify Net position updates after each spin ",
								"Net position should update after every spin ",
								"Net position is updating after every spin", "Pass");
						log.debug("Net Position before spin " + netPositionBeforeSpin + " Net psotion after spin "
								+ netPositionAfterSpin);
					} else {
						report.detailsAppend("verify Net position updates after each spin ",
								"Net position should update after every spin ",
								"Net position is not updating after every spin", "Fail");
						log.debug("Net Position before spin " + netPositionBeforeSpin + " Net psotion after spin "
								+ netPositionAfterSpin);

					}
				} // for loop
			} // if block
		} // try block
		catch (Exception e) {
			log.error("Error in checking net profit ", e);
			report.detailsAppend("Verify net position on topbar",
					"Net position should be visible and updated correctly", "Net position is not visible", "fail");
			System.out.println("Net profit is not present");
		}
		return isNetPositionUpdated;
	}

	/**
	 * To verify Help is present on topbar and navigate to it
	 * 
	 * @return
	 */
	public void verifyHelpOnTopbar(Mobile_HTML_Report report) {
		// Native_ClickByXpath(xpathMap.get("clickOnMenuOnTopbar"));
		MobileElement element1 = (MobileElement) webdriver.findElement(By.xpath(xpathMap.get("clickOnMenuOnTopbar")));
		webdriver.executeScript("arguments[0].click();", element1);
		/*
		 * if(osPlatform.equalsIgnoreCase("ios")) { MobileElement element =
		 * (MobileElement)
		 * webdriver.findElement(By.xpath(xpathMap.get("clickOnMenuOnTopbar")));
		 * webdriver.executeScript("arguments[0].click();", element); } else {
		 * Native_ClickByXpath(xpathMap.get("clickOnMenuOnTopbar")); }
		 */
		boolean isHelpNaviagted = false;
		try {
			String isHelpAvailable = webdriver.findElement(By.xpath(xpathMap.get("isHelpAvailable"))).getText();
			System.out.println(isHelpAvailable);
			if (isHelpAvailable.equals("Help") || (isHelpAvailable.equals("Ayuda"))) {
				// report.detailsAppend("Menu", "Menu on Topbar", "Menu on Topbar", "PASS");
				String gameurl = webdriver.getCurrentUrl();
				MobileElement element = (MobileElement) webdriver
						.findElement(By.xpath(xpathMap.get("isHelpAvailable")));
				webdriver.executeScript("arguments[0].click();", element);
				report.detailsAppend("Verify Help must be is displayed on Topbar",
						"Help should be is displayed on Topbar", "Help is displayed on Topbar", "pass");

				log.debug("clicked on help on topbar");
				Thread.sleep(3000);
				// italy_Checkpagenavigation(report, gameurl);funcFullScreen();
				checkpagenavigation(report, gameurl);
				funcFullScreen();
				System.out.println("Game navigated to Help");
				isHelpNaviagted = true;
				Thread.sleep(2000);
				if (isHelpNaviagted) {
					System.out.println("Navigated from Memu Help to Game Screen");
					log.debug("Help navigation verified succesfully");
					// report.detailsAppend(" Navigation ", "Navigation from Help screen",
					// "Navigation from Help screen is Done", "PASS");
				} else {
					report.detailsAppend(" Navigation ", "Navigation from Help screen",
							"Navigation from Help screen is Done", "FAIL");
				}
			} else {
				report.detailsAppend(" Navigation ", "Navigation from Help screen",
						"Navigation from Help screen is Done", "FAIL");
				log.error("Help option is not available");
			}
		} catch (Exception e) {
			log.error("Error in checking help page ", e);
		}
	}

	/*
	 * To click on Menu on topbar
	 * 
	 * @return
	 */
	private boolean clickOnMenuOnTopbar(Mobile_HTML_Report report) {
		boolean ret = false;
		try {
			MobileElement element = (MobileElement) webdriver
					.findElement(By.xpath(xpathMap.get("clickOnMenuOnTopbar")));
			webdriver.executeScript("arguments[0].click();", element);
			report.detailsAppend("Menu", "Menu on Topbar", "Menu on Topbar", "PASS");
			log.debug("Clicked on menu button on topbar to open");

			ret = true;
		} catch (Exception e) {
			report.detailsAppend("Menu", "Menu on Topbar", "Menu on Topbar", "FAIL");
			log.error("Error in opening menu", e);
		}
		return ret;

	}

	/**
	 * To verify Responsible gaming is present on topbar and navigate to it
	 * 
	 * @return
	 */
	public void verifyResposibleGamingOnTopbar(Mobile_HTML_Report report) {
		Native_ClickByXpath(xpathMap.get("clickOnMenuOnTopbar"));

		boolean isRGNaviagted = false;
		try {
			String isRGAvailable = webdriver.findElement(By.xpath(xpathMap.get("isResponsibleGamingAvailable")))
					.getText();
			System.out.println(isRGAvailable);
			if (isRGAvailable.equals("Responsible Gaming")) {
				report.detailsAppend("Verify Responsible gaming must be is displayed on Topbar",
						"Responsible gaming should be is displayed on Topbar",
						"Responsible gaming is displayed on Topbar", "pass");
				String gameurl = webdriver.getCurrentUrl();
				webdriver.findElement(By.xpath(xpathMap.get("isResponsibleGamingAvailable"))).click();
				log.debug("clicked on Responsible gaming on topbar");
				Thread.sleep(3000);
				checkpagenavigation(report, gameurl);
				System.out.println("Game navigated to Responsible gaming");
				isRGNaviagted = true;
				if (isRGNaviagted) {
					log.debug("Responsible gaming navigation verified succesfully");
					// report.detailsAppend("Verify the Navigation to Responsible gaming screen",
					// "Navigation to Responsible gaming screen", "Navigation to Responsible gaming
					// screen is Done", "pass");
				} else {
					report.detailsAppend("Verify that Navigation to Responsible gaming screen",
							"Navigation to Responsible gaming screen",
							"Navigation to Responsible gaming screen not Done", "fail");
				}
			}
		} catch (Exception e) {
			log.error("Error in checking Responsible gaming page ", e);
			report.detailsAppend("Responsible gaming must be is displayed on Topbar",
					"Responsible gaming should be is displayed on Topbar",
					"Responsible gaming is not displayed on Topbar", "fail");
			System.out.println("Responsible gaming option is not available");
		}

	}

	/**
	 * To verify Transaction History is present on topbar and navigate to it
	 * 
	 * @return
	 */
	public void verifyTransactionHistoryOnTopbar(Mobile_HTML_Report report) {
		Native_ClickByXpath(xpathMap.get("clickOnMenuOnTopbar"));
		// MobileElement element = (MobileElement)
		// webdriver.findElement(By.xpath(xpathMap.get("clickOnMenuOnTopbar")));
		// webdriver.executeScript("arguments[0].click();", element);
		/*
		 * if(osPlatform.equalsIgnoreCase("ios")) { MobileElement element =
		 * (MobileElement)
		 * webdriver.findElement(By.xpath(xpathMap.get("clickOnMenuOnTopbar")));
		 * webdriver.executeScript("arguments[0].click();", element); } else {
		 * Native_ClickByXpath(xpathMap.get("clickOnMenuOnTopbar")); }
		 */
		boolean isTHNaviagted = false;
		try {
			String isTHAvailable = webdriver.findElement(By.xpath(xpathMap.get("isTransactionHistoryAvailable")))
					.getText();
			System.out.println(isTHAvailable);
			if (isTHAvailable.equals("Transaction History")) {
				report.detailsAppend("Verify Transaction History must be is displayed on Topbar",
						"Transaction History should be is displayed on Topbar",
						"Transaction History is displayed on Topbar", "pass");
				String gameurl = webdriver.getCurrentUrl();
				webdriver.findElement(By.xpath(xpathMap.get("isTransactionHistoryAvailable"))).click();
				log.debug("clicked on Transaction History on topbar");
				Thread.sleep(3000);
				checkpagenavigation(report, gameurl);
				System.out.println("Game navigated to Transaction History");
				isTHNaviagted = true;
				if (isTHNaviagted) {
					log.debug("Transaction History navigation verified succesfully");
					// report.detailsAppend("Verify the Navigation to Transaction History screen",
					// "Navigation to Transaction History screen", "Navigation to Transaction
					// History screen is Done", "pass");
				} else {
					report.detailsAppend("Verify that Navigation to Transaction History screen",
							"Navigation to Transaction History screen",
							"Navigation to Transaction History screen not Done", "fail");
				}
			}
		} catch (Exception e) {
			log.error("Error in checking Transaction History page ", e);
			report.detailsAppend("Transaction History must be is displayed on Topbar",
					"Transaction History should be is displayed on Topbar",
					"Transaction History is not displayed on Topbar", "fail");
			System.out.println("Transaction History option is not available");
		}

	}

	/**
	 * To verify Game History is present on topbar and navigate to it
	 * 
	 * @return
	 */
	public void verifyGameHistoryOnTopbar(Mobile_HTML_Report report) {
		Native_ClickByXpath(xpathMap.get("clickOnMenuOnTopbar"));
		// MobileElement element = (MobileElement)
		// webdriver.findElement(By.xpath(xpathMap.get("clickOnMenuOnTopbar")));
		// webdriver.executeScript("arguments[0].click();", element);
		/*
		 * if(osPlatform.equalsIgnoreCase("ios")) { MobileElement element =
		 * (MobileElement)
		 * webdriver.findElement(By.xpath(xpathMap.get("clickOnMenuOnTopbar")));
		 * webdriver.executeScript("arguments[0].click();", element); } else {
		 * Native_ClickByXpath(xpathMap.get("clickOnMenuOnTopbar")); }
		 */
		boolean isGHNaviagted = false;
		try {
			String isGHAvailable = webdriver.findElement(By.xpath(xpathMap.get("isGameHistoryAvailable"))).getText();
			System.out.println(isGHAvailable);
			if (isGHAvailable.equals("Game History")) {
				report.detailsAppend("Verify Game History must be is displayed on Topbar",
						"Game History should be is displayed on Topbar", "Game History is displayed on Topbar", "pass");
				String gameurl = webdriver.getCurrentUrl();
				webdriver.findElement(By.xpath(xpathMap.get("isGameHistoryAvailable"))).click();
				if (osPlatform.equalsIgnoreCase("ios")) {
					try {
						webdriver.switchTo().alert().accept();

					} catch (NoAlertPresentException e) {
						log.debug("no alert present", e);
					}
				}

				log.debug("clicked on Game History on topbar");
				Thread.sleep(3000);
				checkpagenavigation(report, gameurl);

				System.out.println("Game navigated to Game History");
				isGHNaviagted = true;
				if (isGHNaviagted) {
					log.debug("Game History navigation verified succesfully");
					// report.detailsAppend("Verify the Navigation to Game History screen",
					// "Navigation to Game History screen", "Navigation to Game History screen is
					// Done", "pass");
				} else {
					report.detailsAppend("Verify that Navigation to Game History screen",
							"Navigation to Game History screen", "Navigation to Game History screen not Done", "fail");
				}
			}
		} catch (Exception e) {
			log.error("Error in checking Game History page ", e);
			report.detailsAppend("Game History must be is displayed on Topbar",
					"Game History should be is displayed on Topbar", "Game History is not displayed on Topbar", "fail");
			System.out.println("Game History option is not available");
		}

	}

	/**
	 * To verify Player Protection is present on topbar and navigate to it
	 * 
	 * @return
	 */
	public void verifyPlayerProtectionOnTopbar(Mobile_HTML_Report report) {
		Native_ClickByXpath(xpathMap.get("clickOnMenuOnTopbar"));
		// MobileElement element = (MobileElement)
		// webdriver.findElement(By.xpath(xpathMap.get("clickOnMenuOnTopbar")));
		// webdriver.executeScript("arguments[0].click();", element);
		/*
		 * if(osPlatform.equalsIgnoreCase("ios")) { MobileElement element =
		 * (MobileElement)
		 * webdriver.findElement(By.xpath(xpathMap.get("clickOnMenuOnTopbar")));
		 * webdriver.executeScript("arguments[0].click();", element); } else {
		 * Native_ClickByXpath(xpathMap.get("clickOnMenuOnTopbar")); }
		 */
		boolean isPPNaviagted = false;
		try {
			String isPHAvailable = webdriver.findElement(By.xpath(xpathMap.get("isPlayerProtectionAvailable")))
					.getText();
			System.out.println(isPHAvailable);
			if (isPHAvailable.equals("Player Protection")) {
				report.detailsAppend("Verify Player Protection must be is displayed on Topbar",
						"Player Protection should be is displayed on Topbar",
						"Player Protection is displayed on Topbar", "pass");
				String gameurl = webdriver.getCurrentUrl();
				webdriver.findElement(By.xpath(xpathMap.get("isPlayerProtectionAvailable"))).click();
				log.debug("clicked on Player Protection on topbar");
				Thread.sleep(3000);
				checkpagenavigation(report, gameurl);
				System.out.println("Game navigated to Player Protection");
				isPPNaviagted = true;
				if (isPPNaviagted) {
					log.debug("Player Protection navigation verified succesfully");
					// report.detailsAppend("Verify the Navigation to Game History screen",
					// "Navigation to Game History screen", "Navigation to Game History screen is
					// Done", "pass");
				} else {
					report.detailsAppend("Verify that Navigation to Player Protection screen",
							"Navigation to Player Protection screen", "Navigation to Player Protection screen not Done",
							"fail");
				}
			}
		} catch (Exception e) {
			log.error("Error in checking Player Protection page ", e);
			report.detailsAppend("Player Protection must be is displayed on Topbar",
					"Player Protection should be is displayed on Topbar",
					"Player Protection is not displayed on Topbar", "fail");
			System.out.println("Player Protection option is not available");

		}

	}

	/**
	 * To verify Menu navigations from topbar in uk
	 * 
	 * @return
	 */
	public void verifyMenuOptionNavigationsForUK(Mobile_HTML_Report report) {
		try {
			try {
				// To verify help
				verifyHelpOnTopbar(report);
				Thread.sleep(2000);

				boolean isBRVisible1 = webdriver.findElement(By.xpath(xpathMap.get("isBonusReminderPresent")))
						.isDisplayed();
				if (isBRVisible1) {
					MobileElement element = (MobileElement) webdriver
							.findElement(By.xpath(xpathMap.get("ClickOnCloseBonusReminder")));
					webdriver.executeScript("arguments[0].click();", element);
					/*
					 * if(osPlatform.equalsIgnoreCase("ios")) { MobileElement element =
					 * (MobileElement)
					 * webdriver.findElement(By.xpath(xpathMap.get("ClickOnCloseBonusReminder")));
					 * webdriver.executeScript("arguments[0].click();", element); } else {
					 * Native_ClickByXpath(xpathMap.get("ClickOnCloseBonusReminder")); }
					 */
					System.out.println("Bonus Reminder is present after refresh");
				}

			} catch (Exception e) {
				log.error("Error in checking Help page ", e);
			}

			Thread.sleep(2000);

			try {
				// To verify Game history
				verifyGameHistoryOnTopbar(report);
				Thread.sleep(2000);
				funcFullScreen();

				boolean isBRVisible = webdriver.findElement(By.xpath(xpathMap.get("isBonusReminderPresent")))
						.isDisplayed();
				if (isBRVisible) {
					MobileElement element = (MobileElement) webdriver
							.findElement(By.xpath(xpathMap.get("ClickOnCloseBonusReminder")));
					webdriver.executeScript("arguments[0].click();", element);
					/*
					 * if(osPlatform.equalsIgnoreCase("ios")) { MobileElement element =
					 * (MobileElement)
					 * webdriver.findElement(By.xpath(xpathMap.get("ClickOnCloseBonusReminder")));
					 * webdriver.executeScript("arguments[0].click();", element); } else {
					 * Native_ClickByXpath(xpathMap.get("ClickOnCloseBonusReminder")); }
					 */
					System.out.println("Bonus Reminder is present after refresh");
				}
			} catch (Exception e) {
				log.error("Error in checking Game history page ", e);
			}

			try {

				String currentContext = webdriver.getContext();
				System.out.println("current contex " + currentContext);

				// To verify Player protection
				verifyPlayerProtectionOnTopbar(report);
				Thread.sleep(2000);
				webdriver.context(currentContext);
				funcFullScreen();

			} catch (Exception e) {
				log.error("Error in checking Player protection page ", e);
			}

			try {
				String currentContext = webdriver.getContext();
				System.out.println("current contex " + currentContext);
				// To verify Transaction history
				verifyTransactionHistoryOnTopbar(report);
				Thread.sleep(2000);
				webdriver.context(currentContext);
				funcFullScreen();

			}

			catch (Exception e) {
				log.error("Error in checking Transaction history page ", e);
			}

		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				report.detailsAppendNoScreenshot("verifyMenuOptionNavigationsForUK", "",
						"Exception while verifyMenuOptionNavigationsForUK", "fail");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	/*
	 * To check if win limit is reached in autoplay for uk
	 */
	public boolean checkWinLimitReachedForBet(String winLimit, int counter) {
		boolean winLimitReached = false;
		long startTime = System.currentTimeMillis();
		try {
			System.out.println(counter);

			setMaxBet();
			openAutoplay();

			WebElement webElement = webdriver.findElement(By.xpath(xpathMap.get("winLimit")));

			Select selection = new Select(webElement);
			// selection.selectByVisibleText(winLimit);
			selection.selectByIndex(counter);
			WebElement webElement1 = webdriver.findElement(By.xpath(xpathMap.get("lossLimit")));
			Select selection1 = new Select(webElement1);
			// selection1.selectByVisibleText(winLimit);
			selection1.selectByIndex(5);
			webdriver.findElement(By.xpath(xpathMap.get("Start_Autoplay"))).click();

			// String betValue = getConsoleText("return "+XpathMap.get("BetValue"));

			// Can I get Autplay count here
			while (true) {
				if ((getConsoleBooleanText("return " + xpathMap.get("isAutoplayDialogVisible")))) {

					Thread.sleep(3000);

					winLimitReached = true;
					break;
				} else {
					long currentime = System.currentTimeMillis();
					if (((currentime - startTime) / 1000) > 120) {
						System.out.println("Win limit not reached after 2 mint");
						winLimitReached = false;
						break;
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getCause());
		}
		return winLimitReached;

	}

	/**
	 * To click on the Autoplay limit dialog box
	 */
	public void clickOnPrimaryBtn() {
		clickAtButton("return " + xpathMap.get("ClickPrimaryBtn"));
		log.debug("Autoplay dialog is closed");
	}

	/*
	 * To check if loss limit is reached in autoplay for uk
	 */
	public boolean checkLossLimitReachedForBet(String winLimit, int counter) {
		boolean lossLimitReached = false;
		long startTime = System.currentTimeMillis();
		try {
			setMaxBet();
			openAutoplay();
			System.out.println(counter);

			WebElement webElement = webdriver.findElement(By.xpath(xpathMap.get("lossLimit")));
			Select selection = new Select(webElement);
			selection.selectByIndex(counter);
			webdriver.findElement(By.xpath(xpathMap.get("Start_Autoplay"))).click();

			/*
			 * if(counter==0) { System.out.println("Current balance exceeds loss limit"); }
			 */
			// String betValue = getConsoleText("return "+XpathMap.get("BetValue"));
			// Can I get Autplay count here
			while (true) {
				if ((getConsoleBooleanText("return " + xpathMap.get("isAutoplayDialogVisible")))) {

					Thread.sleep(3000);
					lossLimitReached = true;
					break;
				} else {
					long currentime = System.currentTimeMillis();
					if (((currentime - startTime) / 1000) > 120) {
						System.out.println("Win limit not reached after 2 mint");
						lossLimitReached = false;
						break;
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getCause());
		}

		return lossLimitReached;
	}

	/**
	 * Verify Reel Spin Duration
	 */
	public long reelSpinDuratioN(Mobile_HTML_Report report) {
		long reelSpinTime = 0;
		long quickspinloadingTime = 0;
		try {
			spinclick();
			long start = System.currentTimeMillis();
			log.debug("Start time for Reel spin duration in milliseconds is " + start);

			waitForSpinButtonstop(); // Wait till the stop button
			long finish = System.currentTimeMillis();
			log.debug("Finsish time for Reel spin duration in milliseconds is " + finish);

			long totaltime = finish - start;
			// quickspinloadingTime = totaltime / 1000;
			quickspinloadingTime = totaltime;

			log.debug("Total Reel spin time in MilliSeconds is (Finish- Start =)" + quickspinloadingTime);

		}

		catch (Exception e) {
			report.detailsAppend("Verify Reel Spin ", "Reel Spin Duration", "Reel Spin Duration", "FAIL");
			log.error("Not able to verify reelspin time", e);
		}
		return quickspinloadingTime;
	}

	/*
	 * Spain Cooling of period
	 */
	public void spainCoolingOffPeriod(Mobile_HTML_Report report) {
		boolean isSpainCoolingOffPeriodPresent = false;
		try {

			Wait.until(
					ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("setSpainCoolingOffPeriod"))));
			isSpainCoolingOffPeriodPresent = true;
			if (isSpainCoolingOffPeriodPresent) {
				report.detailsAppend(" cooling off period ", "Cooling off period overlay ",
						"Cooling off period overlay ", "PASS");
				MobileElement element = (MobileElement) webdriver
						.findElement(By.xpath(xpathMap.get("setSpainCoolingOffPeriod")));
				webdriver.executeScript("arguments[0].click();", element);

			} else {
				report.detailsAppend("Verify cooling off period overlay",
						"Cooling off period overlay should be present", "Cooling off period overlay is not present",
						"fail");
			}

		} catch (Exception e) {

			log.error("Error in setting cooling off period ", e);
		}

	}

	// verify win or loss for Stop Button
	public Double winORLossForStopButton(Mobile_HTML_Report report) {
		Double value = null;
		try {
			String currentCreditAmount = getCurrentCredits();
			String betValue = getConsoleText("return " + xpathMap.get("BetBalanceTxt"));
			double dblbetValue = Double.parseDouble(betValue.replaceAll("[^0-9]", ""));
			System.out.println(dblbetValue);

			// Verify the stop button availability
			boolean stopbutton = isStopButtonAvailable();
			if (!stopbutton) {
				// report.detailsAppend("Verify the Stop button is not available in Spain
				// Regulatory Markets.", "Stop Button should not display.", "Stop Button is not
				// gettting displayed in game", "Pass");

			} else {
				// report.detailsAppend("Verify the Stop button is not available in Spain
				// Regulatory Markets.", "Stop Button should not display.", " Stop Button is
				// gettting displayed in game", "Fail");

			}
			Thread.sleep(4000);
			waitForSpinButtonstop(); //

			boolean isCreditDeducted = isCreditDeducted(currentCreditAmount, betValue);
			if (isCreditDeducted) {
				System.out.println("There is no win");
				value = (-dblbetValue);
			} else {
				System.out.println("There is win");
				if (isPlayerWon()) {
					String winAmount = null;
					if (!GameName.contains("Scratch")) {
						winAmount = getConsoleText("return " + xpathMap.get("WinMobileText"));
					} else {
						winAmount = getConsoleText("return " + xpathMap.get("WinText"));
					}

					double dblWinAmount = Double.parseDouble(winAmount.replaceAll("[^0-9]", ""));
					boolean isWinAddedToCredit = isWinAddedToCredit(currentCreditAmount, betValue);

					if (isWinAddedToCredit) {
						log.debug("Win added to Credit for the bet : " + betValue);
						value = dblWinAmount;
					} else {
						log.debug("Win is not added to Credit for the bet : " + betValue);
						value = dblWinAmount;
					}
				}
			}
		} catch (Exception e) {
			log.error("Not able to verify win or loss for stop spin", e);
		}
		return value;
	}

	// verify win or loss for Reel Spin Duration
	public Double winORLossForReelSpinDuration(Mobile_HTML_Report report) {
		Double value = null;
		try {
			String currentCreditAmount = getCurrentCredits();
			String betValue = getConsoleText("return " + xpathMap.get("BetTextValue"));
			double dblbetValue = Double.parseDouble(betValue.replaceAll("[^0-9]", ""));

			// verify reel spin duration
			long isReelspin = reelSpinDuration();
			if (isReelspin > 3000) {
				if (isReelspin < 4000) {
					// report.detailsAppend("Verify Reel spin duration for sweden is greater than 3
					// and less than 4 seconds", "Reel spin duration for sweden should be greater
					// than 3 and less than 4 seconds " , "Reel spin duration for sweden is correct,
					// "+isReelspin+" milliseconds", "pass");

				}
			} else {
				// report.detailsAppend("Verify Reel spin duration for sweden is greater than 3
				// and less than 4 seconds", "Reel spin duration for sweden should be greater
				// than 3 and less than 4 seconds " , "Reel spin duration for sweden is
				// incorrect, "+isReelspin+" milliseconds", "fail");
			}
			Thread.sleep(4000);
			waitForSpinButtonstop();
			boolean isCreditDeducted = isCreditDeducted(currentCreditAmount, betValue);
			if (isCreditDeducted) {
				System.out.println("There is no win");
				value = (-dblbetValue);
			} else {
				System.out.println("There is win");
				if (isPlayerWon()) {
					String winAmount = null;
					if (!GameName.contains("Scratch")) {
						winAmount = getConsoleText("return " + xpathMap.get("WinMobileText"));
					} else {
						winAmount = getConsoleText("return " + xpathMap.get("WinText"));
					}

					double dblWinAmount = Double.parseDouble(winAmount.replaceAll("[^0-9]", ""));
					boolean isWinAddedToCredit = isWinAddedToCredit(currentCreditAmount, betValue);

					if (isWinAddedToCredit) {
						log.debug("Win added to Credit for the bet : " + betValue);
						value = dblWinAmount;
					} else {
						log.debug("Win is not added to Credit for the bet : " + betValue);
						value = dblWinAmount;
					}
				}
			}
		} catch (Exception e) {
			log.error("Not able to verify win or loss for reel spin duration", e);
		}
		return value;
	}

	// verify session Reminder Win or Loss
	public void sessionReminderWinOrLoss(Mobile_HTML_Report report, Double valueAfterSpin1, Double valueAfterSpin2) {

		try {
			String getWinFromSR = webdriver.findElement(By.xpath(xpathMap.get("sessionReminderWin"))).getText();
			String getLossFromSR = webdriver.findElement(By.xpath(xpathMap.get("sessionReminderLoss"))).getText();
			String betValue = getConsoleText("return " + xpathMap.get("BetTextValue"));

			double dblbetValue = Double.parseDouble(betValue.replaceAll("[^0-9]", ""));
			double dblWinFromSR = Double.parseDouble(getWinFromSR.replaceAll("[^0-9]", ""));
			double dblLossFromSR = Double.parseDouble(getLossFromSR.replaceAll("[^0-9]", ""));
			double winValue = 0;
			double lossValue = 0;

			if ((valueAfterSpin1) > 0 && (valueAfterSpin2 > 0)) {
				double value1 = valueAfterSpin1 - dblbetValue;
				double value2 = valueAfterSpin2 - dblbetValue;
				winValue = value1 + value2;

			} else if ((valueAfterSpin1) > 0 && !(valueAfterSpin2 > 0)) {
				double value = valueAfterSpin1 - dblbetValue;
				winValue = value;
				lossValue = valueAfterSpin2;

			} else if (!(valueAfterSpin1 > 0) && (valueAfterSpin2 > 0)) {
				double value = valueAfterSpin2 - dblbetValue;
				winValue = value;
				lossValue = valueAfterSpin1;
			} else {
				lossValue = valueAfterSpin1 + valueAfterSpin2;
			}

			if (winValue == dblWinFromSR) {
				System.out.println("Win is correct" + winValue);
				report.detailsAppend("Verify win is correct", "win should be correct", "win is correct", "pass");
			} else {
				System.out.println("Win is incorrect" + winValue);
				report.detailsAppend("Verify win is correct", "win should be correct", "win is incorrect", "fail");
			}
			if (lossValue == (-dblLossFromSR)) {
				System.out.println("Loss is correct" + lossValue);
				report.detailsAppend("Verify Loss is correct", "Loss should be correct", "Loss is correct", "pass");
			} else {
				System.out.println("loss is incorrect" + lossValue);
				report.detailsAppend("Verify Loss is correct", "Loss should be correct", "Loss is incorrect", "fail");
			}

		} catch (Exception e) {
			log.error("Not able to verify win or loss values", e);
			System.out.println("Not able to verify win or loss values in session reminder");
			report.detailsAppend("Verify Win and Loss is correct in session reminder",
					" Win and Loss should be correct in session reminder",
					" Win and Loss is incorrect in session reminder", "fail");
		}

	}

	// open PayTable from Menu
	public boolean paytableOpen(Mobile_HTML_Report report) throws Exception {
		Wait = new WebDriverWait(webdriver, 50);
		boolean test = false;
		try {
			if (!GameName.contains("Scratch")) {
				if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame"))) {
					boolean flag = getConsoleBooleanText("return " + xpathMap.get("isMenuSettingsBtnVisible"));
					if (flag) {
						clickAtButton("return " + xpathMap.get("ClickHamburgerMenuBtn"));
						clickAtButton("return " + xpathMap.get("isMenuPaytableBtnVisible"));
						log.debug("Clicked on setting button in menu");
					}
				} else {
					clickAtButton("return " + xpathMap.get("MenuPaytableBtn"));
					log.debug("Clicked on settings button");
				}
				test = true;
			} else {
				clickAtButton("return " + xpathMap.get("MenuPaytableBtn"));
				log.debug("Clicked on settings button");
			}
			test = true;
		}

		catch (Exception e) {
			log.error("error in settignsOpen()", e);
		}
		return test;
	}

	/*
	 * Verify Italy Currency
	 */
	public boolean italyCurrencyCheck(Mobile_HTML_Report report) {
		// String currencyFormat = "€ #.###,##";
		String currencyFormat = xpathMap.get("ItalyCurrency");
		boolean credit = false;
		boolean result = true;
		String strBetExp = null;
		String regexp = null;
		try {

			String consoleCredit = getCurrentCredits();
			log.debug("Credit in base scene=" + consoleCredit);

			String str = consoleCredit;
			int index = str.indexOf("€");

			if (index != -1) {
				System.out.printf("CURRENCY Symbol is at index %d\n", index);
				report.detailsAppend(" Currency ", "Currency idex is at  " + index, "Currency idex is at  " + index,
						"PASS");
				credit = true;

				log.debug("Function-> verifyCurrencyFormat() ");
				// Read console credits
				// String consoleCredit = getCurrentCredits().replaceAll("Credits: ", "");
				consoleCredit = consoleCredit.replace("credits: ", "");
				consoleCredit = consoleCredit.replace("CREDITS: ", "");
				consoleCredit = consoleCredit.replace("CREDITI: ", "");

				String betregexp = createregexp(consoleCredit, currencyFormat);
				if (currencyFormat.contains("$"))
					strBetExp = betregexp.replace("$", "\\$");
				else
					strBetExp = betregexp;
				Pattern pattren = Pattern.compile(strBetExp);
				System.out.println("Currency is  " + pattren);
				report.detailsAppend(" Currency  ", " Currency is " + pattren, "Currency is " + pattren, "PASS");
				regexp = strBetExp.replace("#", "\\d");

				if (Pattern.matches(regexp, consoleCredit)) {
					result = true;
				} else {
					result = false;
				}

				return result;

			} else {
				System.out.println("No, The Currency is Different ");
				report.detailsAppend(" Currency ", "Currency idex is at  " + index, "Currency idex is at  " + index,
						"FAIL");
				credit = false;
			}
		} // Close for Try Block
		catch (Exception e) {
			log.error(e.getMessage(), e);

		}
		return credit;
	}

	/**
	 * Verify denmark Currency Check
	 * 
	 */
	public boolean denmarkCurrencyCheck(Mobile_HTML_Report report) {
		boolean credit = false;
		boolean result = true;
		String strBetExp = null;
		String regexp = null;
		// String currencyFormat="kr #.###,##";
		String currencyFormat = xpathMap.get("DenmarkCurrency");

		try {

			String consoleCredit = getCurrentCredits();
			log.debug("Credit in base scene=" + consoleCredit);

			String str = consoleCredit;
			int index = str.indexOf("kr");

			if (index != -1) {
				System.out.printf("CURRENCY Symbol is at index %d\n", index);
				report.detailsAppend(" Currency ", "Currency idex is at  " + index, "Currency idex is at  " + index,
						"PASS");
				credit = true;

				log.debug("Function-> verifyCurrencyFormat() ");

				consoleCredit = consoleCredit.replace("credits: ", "");
				consoleCredit = consoleCredit.replace("CREDITS: ", "");
				consoleCredit = consoleCredit.replace("JETONER: ", "");

				String betregexp = createregexp(consoleCredit, currencyFormat);
				if (currencyFormat.contains("$"))
					strBetExp = betregexp.replace("$", "\\$");
				else
					strBetExp = betregexp;
				Pattern pattren = Pattern.compile(strBetExp);
				System.out.println("Currency is  " + pattren);
				report.detailsAppend(" Currency  ", " Currency is " + pattren, "Currency is " + pattren, "PASS");
				regexp = strBetExp.replace("#", "\\d");

				if (Pattern.matches(regexp, consoleCredit)) {
					result = true;
				} else {
					result = false;
				}

				return result;

			} else {
				System.out.println("No, The Currency is Different ");
				report.detailsAppend(" Currency ", "Currency idex is at  " + index, "Currency idex is at  " + index,
						"FAIL");
				credit = false;
			}
		} // Close for Try Block
		catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return credit;

	}

	/*
	 * Verify spain Currency
	 */
	public boolean spainCurrencyCheck(Mobile_HTML_Report report) {
		boolean credit = false;
		boolean result = true;
		String strBetExp = null;
		String regexp = null;
		// String currencyFormat="€ #.###,##";
		String currencyFormat = xpathMap.get("SpainCurrency");
		try {

			String consoleCredit = getCurrentCredits();
			log.debug("Credit in base scene=" + consoleCredit);

			String str = consoleCredit;
			int index = str.indexOf("€");

			if (index != -1) {
				System.out.printf("CURRENCY Symbol is at index %d\n", index);
				report.detailsAppend(" Currency ", "Currency idex is at  " + index, "Currency idex is at  " + index,
						"PASS");
				credit = true;

				log.debug("Function-> verifyCurrencyFormat() ");

				consoleCredit = consoleCredit.replace("credits: ", "");
				consoleCredit = consoleCredit.replace("CREDITS: ", "");
				consoleCredit = consoleCredit.replace("CRÉDITOS: ", "");

				String betregexp = createregexp(consoleCredit, currencyFormat);
				if (currencyFormat.contains("$"))
					strBetExp = betregexp.replace("$", "\\$");
				else
					strBetExp = betregexp;
				Pattern pattren = Pattern.compile(strBetExp);
				System.out.println("Currency is  " + pattren);
				report.detailsAppend(" Currency  ", " Currency is " + pattren, "Currency is " + pattren, "PASS");
				regexp = strBetExp.replace("#", "\\d");

				if (Pattern.matches(regexp, consoleCredit)) {
					result = true;
				} else {
					result = false;
				}

				return result;

			} else {
				System.out.println("No, The Currency is Different ");
				report.detailsAppend(" Currency ", "Currency idex is at  " + index, "Currency idex is at  " + index,
						"FAIL");
				credit = false;
			}
		} // Close for Try Block
		catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return credit;
	}

	/**
	 * Sewden Currency Checks
	 */
	public boolean swedenCurrencyCheck(Mobile_HTML_Report report) {
		boolean credit = false;
		boolean result = true;
		String currencyFormat = xpathMap.get("SwedishKrona");
		String strBetExp = null;
		String regexp = null;
		try {

			String consoleCredit = getCurrentCredits();
			log.debug("Credit in base scene=" + consoleCredit);

			String str = consoleCredit;
			int index = str.indexOf("kr");

			if (index != -1) {
				// System.out.printf("CURRENCY Symbol is at index %d\n", index);
				report.detailsAppend(" Currency ", "Currency index is at  " + index, "Currency index is at  " + index,
						"PASS");
				credit = true;

				log.debug("Function-> verifyCurrencyFormat() ");
				// verify Currency Format

				consoleCredit = consoleCredit.replace("credits: ", "");

				consoleCredit = consoleCredit.replace("CREDITS: ", "");

				consoleCredit = consoleCredit.replace("KREDITER: ", "");

				// System.out.println("currency Format is "+currencyFormat);

				String betregexp = createregexp(consoleCredit, currencyFormat);
				if (currencyFormat.contains("$"))
					strBetExp = betregexp.replace("$", "\\$");

				else
					strBetExp = betregexp;
				Pattern pattren = Pattern.compile(strBetExp);
				log.debug("Currency is  " + pattren);
				report.detailsAppend("  Currency Check  ", " Currency is " + pattren, "Currency is " + pattren, "PASS");
				regexp = strBetExp.replace("#", "\\d");

				if (Pattern.matches(regexp, consoleCredit)) {
					result = true;
				} else {
					result = false;
				}

				return result;

			} else {
				log.debug("No, The Currency is Different ");
				report.detailsAppend(" Currency ", "Currency idex is at  " + index, "Currency idex is at  " + index,
						"FAIL");
				credit = false;
			}
		} // Close for Try Block
		catch (Exception e) {
			log.error(e.getMessage(), e);

		}
		return credit;
	}

	/**
	 * method is for click , navigate and back to game screen using Xpath (For
	 * Android & IOS click action is different)
	 */
	public boolean clickandNavigate(Mobile_HTML_Report report, String locator) {
		boolean ele = false;
		String gameurl = webdriver.getCurrentUrl();
		try {
			boolean isElementVisisble = webdriver.findElement(By.xpath(xpathMap.get(locator))).isDisplayed();
			// String isElementVisisble1 =
			// webdriver.findElement(By.xpath(xpathMap.get(locator))).getText();
			// System.out.println(isElementVisisble1);

			if (isElementVisisble) {

				if (osPlatform.equalsIgnoreCase("Android")) {
					MobileElement elementClick = (MobileElement) webdriver.findElement(By.xpath(xpathMap.get(locator)));
					webdriver.executeScript("arguments[0].click();", elementClick);
				} else {
					WebElement elementClick = webdriver.findElement(By.xpath(xpathMap.get(locator)));
					elementClick.click();

					MobileElement elementClick1 = (MobileElement) webdriver
							.findElement(By.xpath(xpathMap.get(locator)));
					webdriver.executeScript("arguments[0].click();", elementClick1);

					WebDriverWait wt = new WebDriverWait(webdriver, 5);
					wt.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathMap.get(locator))));

					int getX = elementClick.getLocation().getX();
					System.out.println("x Aixs " + getX);
					int getY = elementClick.getLocation().getY();
					System.out.println("y Aixs " + getY);

					TouchAction action = new TouchAction(webdriver);
					// new TouchAction(webdriver).press(PointOption.point(getX,
					// getY)).release().perform();
					action.press(PointOption.point(250, 22)).release().perform();
					Actions actions = new Actions(webdriver);
					actions.moveByOffset(getX, getY).click().build().perform();
				}
				log.debug("Clicked on Element");
				Thread.sleep(3000);
				checkpagenavigation(report, gameurl); // Page Navigation
				log.debug("Navigation is done & now on Game Screen");
				newFeature();
				funcFullScreen();
				Thread.sleep(1000);
			} else {
				isElementVisisble = false;
			}
		} catch (Exception e) {
			log.error("Not able to verify SelfExlusion logos from Top Bar", e);
		}

		return ele;
	}

	/**
	 * method is for click , navigate and back to game screen using Xpath (For
	 * Android & IOS click action is same)
	 */
	public boolean clickAndNavigate(Mobile_HTML_Report report, String locator) {
		boolean ele = false;
		String gameurl = webdriver.getCurrentUrl();
		try {
			boolean isElementVisisble = webdriver.findElement(By.xpath(xpathMap.get(locator))).isDisplayed();

			if (isElementVisisble) {
				MobileElement elementClick = (MobileElement) webdriver.findElement(By.xpath(xpathMap.get(locator)));
				webdriver.executeScript("arguments[0].click();", elementClick);
				// report.detailsAppend(" Click Menu option ", "Menu option Clicked ", "Menu
				// option Clicked", "PASS");
				Thread.sleep(6000);
				// checkpagenavigation(report, gameurl);
				cHeckPageNavigation(report, gameurl);
				Thread.sleep(3000);
				// newFeature();
				funcFullScreen();
				Thread.sleep(1000);
				ele = true;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return ele;
	}

	/**
	 * Verify thee player protrection Icon From Top bar
	 */
	public boolean playerProtectionIcon(Mobile_HTML_Report report) {
		boolean ret = false;
		String playerProtectionIcon = "Italy_ResponsibleGaming_Icon";
		try {
			italyClickAndNavigate(report, playerProtectionIcon);
			ret = true;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return ret;

	}

	/**
	 * Italy Help from menu text comparison and visibility check
	 */
	public boolean italyHelpFromTopBarMenu(Mobile_HTML_Report report) throws InterruptedException {
		String menuclick = "clickOnMenuOnTopbar";
		String locator = "Italy_Help";
		String Text = clickAndGetText(report, menuclick, locator);
		boolean text = false;
		String allHelpText[] = { "Help", "Guida" };
		try {
			for (int i = 0; i < allHelpText.length; i++) {
				if (Text.equals(allHelpText[i])) {
					log.debug("Help Text from Menu is same");
					text = true;
				}
			}
			Thread.sleep(2000);
			italyClickAndNavigate(report, locator);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return text;
	}

	/**
	 * To verify Help text link on top bar
	 */
	public boolean helpTextLink(Mobile_HTML_Report report) {
		String help = "ishelpTextLinkAvailable";
		String helptext = isDisplayedAndGetText(help);
		boolean text = false;
		String allHelpText[] = { "Help", "Hilfe", "Hjälp", "Ayuda", "Hjælp" };
		try {
			for (int i = 0; i < allHelpText.length; i++)
				if (helptext.equals(allHelpText[i])) {
					log.debug("Help Text from Menu is same");
					text = true;
				}
			clickAndNavigate(report, help);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return text;
	}

	/**
	 * This method is for italy IOS PageNavigation
	 */
	public void italyCheckPageNavigation(Mobile_HTML_Report report, String gameurl) {
		try {
			String mainwindow = webdriver.getWindowHandle();

			Set<String> s1 = webdriver.getWindowHandles();
			if (s1.size() > 1) {
				Iterator<String> i1 = s1.iterator();
				while (i1.hasNext()) {
					String ChildWindow = i1.next();
					if (mainwindow.equalsIgnoreCase(ChildWindow)) {
						// ChildWindow=i1.next();
						webdriver.switchTo().window(ChildWindow);
						String url = webdriver.getCurrentUrl();
						System.out.println("Reload URL is :: " + url);
						log.debug("Reload URL is :: " + url);
						if (!url.equalsIgnoreCase(gameurl)) {
							// pass condition for navigation
							/*
							 * report.detailsAppend("verify the Navigation screen shot ",
							 * " Navigation page screen shot", "Navigation page screenshot ", "PASS");
							 */
							log.debug("Page navigated succesfully");
							System.out.println("Page navigated succesfully");
							webdriver.close();
						} else {
							log.debug("Now On game page");
						}
					}
				}
				webdriver.switchTo().window(mainwindow);
			} else {
				String url = webdriver.getCurrentUrl();
				System.out.println("Reloaded URL is :: " + url);
				log.debug("Reloaded URL is :: " + url);
				if (!url.equalsIgnoreCase(gameurl)) {
					// pass condition for navigation
					/*
					 * report.detailsAppend("verify the Navigation screen shot",
					 * " Navigation page screen shot", "Navigation page screenshot ", "PASS");
					 */
					System.out.println("Page navigated succesfully");
					log.debug("Page navigated succesfully");
					webdriver.navigate().to(gameurl);
					// Gameplay Dialog Box
					Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("userInput"))));

					// Italy amount from Excel
					String amount1 = xpathMap.get("Italy_amount");
					// method for Scroll Bar
					italyScrollBarCickAmount(report, amount1);
					// This is for Continue Button on Loading Screen
					if ((Constant.YES.equalsIgnoreCase(xpathMap.get("continueBtnOnGameLoad")))) {
						newFeature();
					}
				} else {
					webdriver.navigate().to(gameurl);
					/*
					 * report.detailsAppend("verify the Navigation screen shot",
					 * " Navigation page screen shot", "Navigation page screenshot ", "PASS");
					 */
					Thread.sleep(2000);
					// Gameplay Dialog Box
					Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("userInput"))));
					// Italy amount from Excel
					String amount1 = xpathMap.get("Italy_amount");
					// method for Scroll Bar
					italyScrollBarCickAmount(report, amount1);
					// This is for Continue Button on Loading Screen
					if ((Constant.YES.equalsIgnoreCase(xpathMap.get("continueBtnOnGameLoad")))) {
						newFeature();
					}
					// funcFullScreen();
					waitForSpinButton();
					log.debug("Now On game page");
				}
			}
			// webdriver.navigate().to(gameurl);
		} catch (Exception e) {
			log.error("error in navigation of page");
		}
	}

	/**
	 * This method is for italy Andriod PageNavigation
	 */
	public void italyAndriodHelp_CheckPageNavigation(Mobile_HTML_Report report, String gameurl) {

		try {
			String mainwindow = webdriver.getWindowHandle();

			Set<String> s1 = webdriver.getWindowHandles();
			if (s1.size() > 1) {
				Iterator<String> i1 = s1.iterator();
				while (i1.hasNext()) {
					String ChildWindow = i1.next();
					if (mainwindow.equalsIgnoreCase(ChildWindow)) {
						// ChildWindow=i1.next();
						// report.detailsAppend("verify the Navigation screen shot ", " Navigation page
						// screen shot", "Navigation page screenshot ", "PASS");
						Thread.sleep(2000);
						webdriver.switchTo().window(ChildWindow);
						Thread.sleep(2000);
						String url = webdriver.getCurrentUrl();
						System.out.println("Reload URL is :: " + url);
						log.debug("Reload URL is :: " + url);
						if (!url.equalsIgnoreCase(gameurl)) {
							// pass condition for navigation
							// report.detailsAppend("verify the Navigation screen shot ", " Navigation page
							// screen shot", "Navigation page screenshot ", "PASS");
							log.debug("Page navigated succesfully");
							System.out.println("Page navigated succesfully");
							webdriver.close();
						} else {
							log.debug("Now On game page");
						}
					}
				}
				webdriver.switchTo().window(mainwindow);
			} else {
				String url = webdriver.getCurrentUrl();
				System.out.println("Reloaded URL is :: " + url);
				log.debug("Reloaded URL is :: " + url);
				if (!url.equalsIgnoreCase(gameurl)) {
					// pass condition for navigation
					// report.detailsAppend("verify the Navigation screen shot", " Navigation page
					// screen shot", "Navigation page screenshot ", "PASS");
					System.out.println("Page navigated succesfully");
					log.debug("Page navigated succesfully");
					webdriver.navigate().to(gameurl);
					report.detailsAppend("verify the Navigation screen shot", " Navigation page screen shot",
							"Navigation page screenshot ", "PASS");
					waitForSpinButton();
					// This is for Continue Button on Loading Screen
					if ((Constant.YES.equalsIgnoreCase(xpathMap.get("continueBtnOnGameLoad")))) {
						newFeature();
					}
				} else {
					webdriver.navigate().to(gameurl);
					Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("userInput")))); // Gameplay
																													// Dialog
																													// Box
					String amount1 = xpathMap.get("Italy_amount"); // Italy amount from Excel
					italyScrollBarCickAmount(report, amount1); // method for Scroll Bar
					funcFullScreen();// This is for Continue Button on Loading Screen
					if ((Constant.YES.equalsIgnoreCase(xpathMap.get("continueBtnOnGameLoad")))) {
						newFeature();
						funcFullScreen();
					}
					log.debug("Now On game page");
				}
			}
			// webdriver.navigate().to(gameurl);
		} catch (Exception e) {
			log.error("error in navigation of page");
		}

	}

	/**
	 * method is for click , navigate and back to game screen for Italy Market using
	 * Xpath (For Android & IOS click action is same)
	 */
	public boolean italyClickAndNavigate(Mobile_HTML_Report report, String locator) {
		boolean ele = false;
		String gameurl = webdriver.getCurrentUrl();
		try {
			boolean isElementVisisble = webdriver.findElement(By.xpath(xpathMap.get(locator))).isDisplayed();

			if (isElementVisisble) {
				MobileElement elementClick = (MobileElement) webdriver.findElement(By.xpath(xpathMap.get(locator)));
				webdriver.executeScript("arguments[0].click();", elementClick);
				// report.detailsAppend(" Click Menu ", "Menu Clicked ", "Menu Clicked",
				// "PASS");
				Thread.sleep(3000);
				italyCheckPageNavigation(report, gameurl);
				if (osPlatform.equalsIgnoreCase("Android")) {
					italyAndriodHelp_CheckPageNavigation(report, gameurl); // Checkpage Navigation
					// italyCheckPageNavigation(report, gameurl);
				} else {

					italyCheckPageNavigation(report, gameurl);
				}
				// cHeckPageNavigation(report, gameurl);
				// newFeature();
				funcFullScreen();
				Thread.sleep(1000);
				ele = true;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return ele;
	}

	/**
	 * Sweden three log Symbols from Top Bar
	 */

	public boolean swedenLogosFromTopBar(Mobile_HTML_Report report) {
		String loactor1 = "Sweden_spelpaus_From_TobBar";
		String loactor2 = "Sweden_Spelgranser_From_TobBar";
		String loactor3 = "Sweden_sjalvtest_From_TobBar";
		boolean logo = false;
		try {
			clickAndNavigate(report, loactor1);
			clickAndNavigate(report, loactor2);
			clickandNavigate(report, loactor3);
			logo = true;
		} catch (Exception e) {
			log.error("Error", e);
		}
		return logo;

	}

	/**
	 * help text comparison from Menu, click and navigation
	 */
	public boolean helpIcon(Mobile_HTML_Report report) {
		boolean ret = false;
		String help = "Help_Icon_From_TopBar";
		try {
			clickAndNavigate(report, help);
			ret = true;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return ret;
	}

	/**
	 * verify if session reminder is visible or not
	 * 
	 */

	public boolean isSessionReminderPresent() {
		boolean isSessionReminderVisible = false;
		WebDriverWait Wait = new WebDriverWait(webdriver, 500);
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("sessionReminderVisible"))));

			String sessionReminder = webdriver.findElement(By.xpath(xpathMap.get("sessionReminderVisible"))).getText();

			if (sessionReminder.equals("Session Reminder")) {
				System.out.println("Session Reminder is present");
				log.debug("Session Reminder is present");
				isSessionReminderVisible = true;
			}
		} catch (Exception e) {
			log.error("Not able to verify session reminder status", e);
		}
		return isSessionReminderVisible;
	}

	/**
	 * to click on continue in splash screen
	 */
	public void newFeature() {
		try {
			// webdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame"))
					|| (Constant.YES.equalsIgnoreCase(xpathMap.get("continueBtnOnGameLoad")))) {
				// Click on new feature screen with xpath for the game likes immortal romance
				Wait.until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath(xpathMap.get("OneDesign_NewFeature_ClickToContinue"))));
				webdriver.findElement(By.xpath(xpathMap.get("OneDesign_NewFeature_ClickToContinue"))).click();
				log.debug("Clicked on continue button present on new feature screen");
			}
			// click on new feature screen with hooks for games like dragon dance
			else {
				Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("clock"))));
				// clickAtButton("return " + forceNamespace+
				// ".getControlById('BaseIntroComponent').onButtonClicked('baseIntroTapToContinueButton')");
				clickAtButton("return " + xpathMap.get("BaseIntroContinueBtn"));
				log.debug("Clicked on intro screen of game");
			}

		} catch (Exception e) {
			log.info("exception in new feature,Seems no feature screen present", e);
		}
	}

	/**
	 * Verify Germany Currency Format
	 */
	public boolean germanyCurrencyCheck(Mobile_HTML_Report report) {
		// String currencyFormat = "€ #.###,##";
		String currencyFormat = xpathMap.get("GermanyCurrency");
		boolean credit = false;
		boolean result = true;
		String strBetExp = null;
		String regexp = null;
		try {
			String consoleCredit = getCurrentCredits();
			log.debug("Credit in base scene=" + consoleCredit);
			String str = consoleCredit;
			int index = str.indexOf("€");

			if (index != -1) {
				// log.debug("CURRENCY Symbol is at index %d\n", index);
				report.detailsAppend(" Currency ", "Currency idex is at  " + index, "Currency idex is at  " + index,
						"PASS");
				credit = true;

				log.debug("Function-> verifyCurrencyFormat() ");
				// Read console credits

				consoleCredit = consoleCredit.replace("credits: ", "");
				consoleCredit = consoleCredit.replace("CREDITS: ", "");

				String betregexp = createregexp(consoleCredit, currencyFormat);
				if (currencyFormat.contains("$"))
					strBetExp = betregexp.replace("$", "\\$");
				else
					strBetExp = betregexp;
				Pattern pattren = Pattern.compile(strBetExp);
				log.debug("Currency is  " + pattren);
				report.detailsAppend(" Currency  ", " Currency is " + pattren, "Currency is " + pattren, "PASS");
				regexp = strBetExp.replace("#", "\\d");

				if (Pattern.matches(regexp, consoleCredit)) {
					result = true;
				} else {
					result = false;
				}

				return result;
			} else {
				log.debug("No, The Currency is Different ");
				report.detailsAppend(" Currency ", "Currency idex is at  " + index, "Currency idex is at  " + index,
						"FAIL");
				credit = false;
			}
		} // Close for Try Block
		catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return credit;

	}

	/**
	 * verify Autoplay Availabilty
	 */
	public boolean verifyAutoplayAvailabilty() {
		boolean isAutoplay = false;
		try {
			boolean isAutoplayVisible = getConsoleBooleanText("return " + xpathMap.get("isAutoplayBtnAvailable"));
			if (isAutoplayVisible) {
				log.debug("Autoplay is available");
				return true;
			} else {
				log.error("Autoplay is not available");
				return false;
			}
		} catch (Exception e) {
			log.error("Not able to verify Autoplay Availability", e);
		}
		return isAutoplay;

	}

	/**
	 * germay help text comparison from Menu, click and navigation
	 */
	public boolean germanyHelpFromTopBarMenu(Mobile_HTML_Report report) throws InterruptedException {
		String menuclick = "clickOnMenuOnTopbar";
		String locator = "HelpFromMenu";
		String Text = clickAndGetText(report, menuclick, locator);
		boolean text = false;
		String allHelpText[] = { "Help", "Hilfe" };
		try {
			for (int i = 0; i < allHelpText.length; i++) {
				if (Text.equals(allHelpText[i])) {
					log.debug("Help Text from Menu is same");
					text = true;
				}
			}
			Thread.sleep(2000);
			clickAndNavigate(report, locator);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return text;
	}

	/**
	 * germay ResponsibleGaming text comparison from Menu, click and navigation
	 */
	public boolean germanyResponsibleGamingFromTopBarMenu(Mobile_HTML_Report report) throws InterruptedException {
		String menuclick = "clickOnMenuOnTopbar";
		String locator = "ResponsibleGamingFromMenu";
		String Text = clickAndGetText(report, menuclick, locator);
		boolean text = false;
		String allResponsibleText[] = { "Responsible Gaming", "Verantwortungsbewusst spielen" };
		try {
			for (int i = 0; i < allResponsibleText.length; i++) {
				if (Text.equals(allResponsibleText[i])) {
					log.debug("Responsible Gaming Text from Menu is same");
					text = true;
				}
			}
			Thread.sleep(2000);
			clickAndNavigate(report, locator);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return text;

	}

	/**
	 * germay GameHistory text comparison from Menu, click and navigation
	 */
	public boolean germanyGameHistoryFromTopBarMenu(Mobile_HTML_Report report) throws InterruptedException {
		String menuclick = "clickOnMenuOnTopbar";
		String locator = "GameHistoryFromMenu";
		String Text = clickAndGetText(report, menuclick, locator);
		boolean text = false;
		String allGameHistoryText[] = { "Game History", "Spielverlauf" };
		try {
			for (int i = 0; i < allGameHistoryText.length; i++) {
				if (Text.equals(allGameHistoryText[i])) {
					log.debug("Game History Text from Menu is same");
					text = true;
				}
			}
			Thread.sleep(2000);
			clickAndNavigate(report, locator);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return text;

	}

	/**
	 * Click & get the text
	 */

	public String clickAndGetText(Mobile_HTML_Report report, String menuclick, String locator) {
		String getText = null;
		MobileElement clickMenu = (MobileElement) webdriver.findElement(By.xpath(xpathMap.get(menuclick)));
		webdriver.executeScript("arguments[0].click();", clickMenu);
		report.detailsAppend(" Click Menu ", " Clicked Menu ", " Clicked Menu", "PASS");
		try {
			boolean isloactorVisible = webdriver.findElement(By.xpath(xpathMap.get(locator))).isDisplayed();
			if (isloactorVisible) {
				getText = webdriver.findElement(By.xpath(xpathMap.get(locator))).getText();
				log.debug("Text is " + getText);
				// System.out.println("Text from Menu is "+getText);
			} else {
				log.debug("Locator from Menu is not Visible");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return getText;
	}

	/**
	 * Sweden Help text comparison from Menu , click and Navigate
	 * 
	 * @throws InterruptedException
	 */
	public boolean swedenTopBarMenu(Mobile_HTML_Report report) throws InterruptedException {
		String menuclick = "clickOnMenuOnTopbar";
		String locator = "Denmark_Help";
		String Text = clickAndGetText(report, menuclick, locator);
		boolean text = false;
		String allHelpText[] = { "Help", "Hjälp" };
		try {
			for (int i = 0; i < allHelpText.length; i++) {
				if (Text.equals(allHelpText[i])) {
					log.debug("Help Text from Menu is same");
					text = true;
				}
			}
			Thread.sleep(2000);
			clickAndNavigate(report, locator);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return text;
	}

	/**
	 * Denmark Help comparission from Menu
	 */

	public boolean spainHelpFromTopBarMenu(Mobile_HTML_Report report) {
		String menuclick = "clickOnMenuOnTopbar";
		String locator = "spain_Help";
		String Text = clickAndGetText(report, menuclick, locator);
		boolean text = false;
		String allHelpText[] = { "Help", "Ayuda" };
		try {
			for (int i = 0; i < allHelpText.length; i++) {
				if (Text.equals(allHelpText[i])) {
					log.debug("Help Text from Menu is same");
					text = true;
				}
			}
			Thread.sleep(2000);
			clickAndNavigate(report, locator);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return text;

	}

	/**
	 * Denmark Help comparison from Menu, click and navigation
	 */

	public boolean denmarkHelpFromTopBarMenu(Mobile_HTML_Report report) {
		String menuclick = "clickOnMenuOnTopbar";
		String locator = "Denmark_Help";
		String Text = clickAndGetText(report, menuclick, locator);
		boolean text = false;
		String allHelpText[] = { "Help", "Hjælp" };
		try {
			for (int i = 0; i < allHelpText.length; i++) {
				if (Text.equals(allHelpText[i])) {
					log.debug("Help Text from Menu is same");
					text = true;
				}
			}
			Thread.sleep(2000);
			clickAndNavigate(report, locator);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return text;

	}

	/**
	 * Denmark PlayerProtection comparison from Menu, click and navigation
	 */
	public boolean denmarkPlayerProtectionFromMenu(Mobile_HTML_Report report) {
		String menuclick = "clickOnMenuOnTopbar";
		String locator = "Denmark_PlayerProtection";
		String Text = clickAndGetText(report, menuclick, locator);
		boolean text = false;
		String allPlyProtectionText[] = { "Player Protection", "Spillerbeskyttelse" };
		try {
			for (int i = 0; i < allPlyProtectionText.length; i++) {
				if (Text.equals(allPlyProtectionText[i])) {
					log.debug("PlayerProtection Text  from Menu is same");
					text = true;
				}
			}
			Thread.sleep(2000);
			clickAndNavigate(report, locator);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return text;
	}

	public boolean verifyWinAmtCurrencyFormat(String currencyFormat, Mobile_HTML_Report currencyReport) {
		boolean result = false;
		long startTime = System.currentTimeMillis();
		String winamt = null;
		try {
			Thread.sleep(2000);
			log.debug("Function-> verifyWinCurrencyFormat");

			if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame"))) {
				winamt = getConsoleText("return " + xpathMap.get("WinMobileText"));
				log.info("verifyBigWinCurrencyFormat():waiting for win  to occur");
			} else
				winamt = getConsoleText("return " + xpathMap.get("WinDesktopLabel"));

			if (winamt != null) {
				log.info(" win occur ");
				// fetching win from panel
				String consolewinnew = winamt.toLowerCase().replaceAll("win: ", "");
				result = currencyFormatValidator(consolewinnew, currencyFormat);
			} else {
				long currentime = System.currentTimeMillis();
				if (((currentime - startTime) / 1000) > 120) {
					System.out.println("verifyWinCurrencyFormat():No win occur  in 2");
					result = false;
				}
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.error("Win not occur, please  use win test data");

		}

		return result;
	}

	public void jackpotSummaryWinCurrFormat(String currencyFormat, Mobile_HTML_Report currencyReport,
			String currencyName) {
		String jackpotWin = null;

		boolean isWinInCurrencyFormat = false;
		try {
			log.debug("Function -> jackpotSummaryWinCurrencyFormat()");
			isWinInCurrencyFormat = verifyWinAmtCurrencyFormat(currencyFormat, currencyReport);
			log.debug("Fetching Jackpot summary currency symbol " + "/n Jackpot summary currency symbol is:: "
					+ jackpotWin);
			if (isWinInCurrencyFormat) {
				currencyReport.detailsAppendFolder("Verify jackpot currency when win occurs ",
						"Jackpot win should display with correct currency format and and currency symbol ",
						"Jackpot win displaying with correct currency format and and currency symbol ", "Pass",
						currencyName);

			} else {
				currencyReport.detailsAppendFolder("Verify jackpot currency in win occurs ",
						"Jackpot win should display with correct currency format and and currency symbol ",
						"Jackpot win is not  displaying with correct currency format and and currency symbol ", "Fail",
						currencyName);
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public boolean assignFreeGames(String userName, String offerExpirationUtcDate, int mid, int cid, int noOfOffers,
			int defaultNoOfFreeGames) {
		// assign free games to above created user
		boolean isFreeGameAssigned = false;
		try {
			String balanceTypeId = xpathMap.get("BalanceTypeID");
			Double dblBalanceTypeID = Double.parseDouble(balanceTypeId);
			balanceTypeId = "" + dblBalanceTypeID.intValue() + "";

			// Assign free games offers to user
			if (TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa")) {

				isFreeGameAssigned = addFreeGameToUserInBluemesa(userName, defaultNoOfFreeGames, offerExpirationUtcDate,
						balanceTypeId, mid, cid, noOfOffers);
			} else {
				isFreeGameAssigned = addFreeGameToUserInAxiom(userName, defaultNoOfFreeGames, offerExpirationUtcDate,
						balanceTypeId, mid, cid, noOfOffers);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return isFreeGameAssigned;
	}

	public void freeGameSummaryWinCurrFormat(String currencyFormat, Mobile_HTML_Report currencyReport,
			String currencyName) {
		String freeGamesWin = null;
		boolean isWinInCurrencyFormat = false;
		try {
			log.debug("Function -> freeSpinWinCurrencyFormat()");
			if (!GameName.contains("Scratch")) {
				freeGamesWin = "return " + xpathMap.get("freeGamesSummaryAmt");
			}
			String freegameswinnew = getConsoleText(freeGamesWin);

			// To validate currency Amount format
			isWinInCurrencyFormat = currencyFormatValidator(freegameswinnew, currencyFormat);
			log.debug("Fetching Free games summary currency symbol " + "/n free games summary currency symbol is:: "
					+ freeGamesWin);

			if (isWinInCurrencyFormat) {
				currencyReport.detailsAppendFolder("Verify freegames summary currency when win occurs ",
						"freegames summary win should display with correct currency format and and currency symbol ",
						"freegames summary win displaying with correct currency format and and currency symbol ",
						"Pass", currencyName);

			} else {
				currencyReport.detailsAppendFolder("Verify freegames summary currency in win occurs ",
						"freegames summary win should display with correct currency format and and currency symbol ",
						"freegames summary win is not  displaying with correct currency format and and currency symbol ",
						"Fail", currencyName);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public String getWinAmtFromFreegames() {
		String Winamtnew = null;
		try {
			String Winamt = "return " + xpathMap.get("freeGamesSummaryAmt");
			Winamtnew = getConsoleText(Winamt);
			log.debug("win amount " + Winamtnew);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Winamtnew;
	}

	public String gamepayoutForPaytable(ArrayList<String> symbolData, String paytablePayout) {
		String consolePayout = null;
		int symbolIndex = 0;
		try {
			String wild = xpathMap.get("wildSymbol");
			String[] xmlData = paytablePayout.split(",");
			String line = xmlData[0];
			String symbol = xmlData[1];
			Map<String, String> paramMap = new HashMap<String, String>();

			for (int i = 0; i < symbolData.size(); i++) {
				if (symbolData.get(i).contains(symbol)) {
					if (wild.equalsIgnoreCase("no")) {
						symbolIndex = i - 1;
					} else
						symbolIndex = i;
					break;
				}

			}
			paramMap.put("param1", Integer.toString(symbolIndex));
			paramMap.put("param2", line);

			String paytablePayoutValueHook = xpathMap.get("getPaytablePayoutValue");
			String newHook = replaceParamInHook(paytablePayoutValueHook, paramMap);
			String payout = "return " + newHook;
			consolePayout = getConsoleText(payout);
		} catch (Exception e) {
			e.getMessage();
		}
		return consolePayout;
	}

	/*
	 * Method for payout verification for all bets
	 */
	public void payoutverificationforBetLVC(Mobile_HTML_Report report, String regExpression, String currencyName) {
		Util util = new Util();
		int length = 0;
		String gamePayout;
		int index = 0;
		String paytablePayout;
		String scatter;
		int isAllTrue = 0;
		String xmlFilePath;

		// Read xml for the game
		if ("yes".equalsIgnoreCase(xpathMap.get("PaytableSymbleAlignmentDifferent"))) {
			// Read xml for the game
			xmlFilePath = "./" + GameName + "/Config/" + "PaytableSymbols" + ".xml";
		} else {
			xmlFilePath = "./" + GameName + "/Config/" + GameName + ".xml";
		}

		length = util.xmlLength(xmlFilePath, "WinningCombinations");
		ArrayList<String> symbolData = util.getXMLDataInArray("Symbol", "name", xmlFilePath);

		ArrayList<String> winCombinationList = new ArrayList<String>();

		for (int count = 0; count < length; count++) {
			String strWinCombination = util.readXML("WinCombination", "numSymbolsRequired", "symbols", "payouts",
					"./" + GameName + "/Config/" + GameName + ".xml", length + 2, count);
			if (strWinCombination != null) {
				winCombinationList.add(strWinCombination);
			}
		}

		int winCombinationSize = winCombinationList.size();

		try {
			// setting index at the staring
			index = 0;
			// Get the bet amount
			double bet = GetBetAmt();

			report.detailsAppendNoScreenshot(
					"Paytable Payout currency format verification in progress for the selected bet ",
					"Paytable Payout currency format verification in progress for the selected bet ",
					"Paytable Payout currency format verification in progress for the selected bet ", "Pass");

			capturePaytableScreenshot(report, currencyName);

			paytablePayout = winCombinationList.get(index);

			for (int j = 0; j < winCombinationSize; j++) {

				paytablePayout = winCombinationList.get(index);
				if (paytablePayout.contains("Scatter") || paytablePayout.contains("FreeSpin")) {
					scatter = "yes";
				} else {
					scatter = "no";
				}
				String[] xmlData = paytablePayout.split(",");

				gamePayout = gamepayoutForPaytable(symbolData, paytablePayout);// it will fetch game payout for Force
																				// game

				boolean result = currencyFormatValidatorForLVC(gamePayout, regExpression);

				if (result) {
					isAllTrue++;
					log.debug("Paytable payout is correct for the bet value :" + bet
							+ " currency format validation is :" + result + " symbol name : " + xmlData[1]
							+ " game payout : " + gamePayout + " is correct");

				} else {

					log.debug("Paytable payout is not correct for the bet value :" + bet
							+ " currency format validation is :" + result + " symbol name : " + xmlData[1]
							+ " game payout : " + gamePayout + " is incorrect");

				}

				length--;
				index++;
			}

			if (isAllTrue == winCombinationSize) {
				report.detailsAppendNoScreenshot(
						"verify Paytable payout currency format for selected bet value with the game currency format",
						"Paytable payout verification with the game currency format ",
						"Paytbale payout verification  with the game currency format is done", "pass");

			} else {
				report.detailsAppendNoScreenshot(
						"verify Paytable payout currency format for selected bet value with the game currency format",
						"Paytable payout verification with the game currency format ",
						"Paytable payout verification with the game currency format is done but failed coz some formats are not matched",
						"fail");

			}
			// Closes the paytable
			paytableClose();

		} catch (Exception e) {
			e.printStackTrace();
			try {
				report.detailsAppendNoScreenshot("verify Payout verification for the bet ", " ",
						"Exception ocuur while verifying payout for bet", "Fail");
			} catch (Exception e1) {
				// log.error(e1.getStackTrace());
			}

		}
	}

	public boolean currencyFormatValidatorForLVC(String curencyAmount, String regExpression) {
		boolean isCurrencyFormat = false;
		try {

			log.debug("Function-> currencyFormatValidatorForLVC()");
			log.debug("curencyAmount: " + curencyAmount);
			if (curencyAmount.matches(regExpression)) {
				isCurrencyFormat = true;
				log.debug("Currency format is correct");
			} else {
				isCurrencyFormat = false;
				log.debug("Currency format is incorrect");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return isCurrencyFormat;

	}
	/*
	 * Method to verify currency format in credit
	 */

	public boolean verifyCurrencyFormatForCredits(String regExpression) {
		boolean result = true;
		String strBetExp = null;
		String regexp = null;
		try {
			log.debug("Function-> verifyCurrencyFormat() ");
			// Read console credits
			String consoleCredit = getCurrentCredits().replaceAll("Credits: ", "");
			consoleCredit = consoleCredit.replace("credits: ", "");
			consoleCredit = consoleCredit.replace("CREDITS: ", "");
			result = currencyFormatValidatorForLVC(consoleCredit, regExpression);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return result;
	}

	public boolean betCurrencySymbolForLVC(String regExpression) {
		String bet = null;
		boolean isBetInCurrencyFormat = false;

		try {
			log.debug("Function-> betCurrencySymbol()");
			log.debug("Reading Bet Text from base scene");
			if (!GameName.contains("Scratch")) {
				if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame"))) {
					bet = "return " + xpathMap.get("BetTextValue");
				} else
					bet = "return " + xpathMap.get("BetButtonLabel");
			} else {
				bet = "return " + xpathMap.get("InfobarBettext");
			}

			// String consoleBet=getConsoleText(bet).toLowerCase();
			String consoleBet = getConsoleText(bet);
			log.debug(" Bet Text from base scene=" + consoleBet);
			Thread.sleep(100);

			// String consoleBetnew=consoleBet.toLowerCase().replaceAll("bet: ", "");
			String consoleBetnew = consoleBet.replaceAll("bet: ", "");
			consoleBetnew = consoleBetnew.replaceAll("BET: ", "");
			consoleBetnew = consoleBetnew.replaceAll("Bet: ", "");
			isBetInCurrencyFormat = currencyFormatValidatorForLVC(consoleBetnew, regExpression);

			log.debug(" Bet currency symbol is::" + consoleBet);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return isBetInCurrencyFormat;

	}

	public boolean verifyWinAmtCurrencyFormatForLVC(String regExpression) {
		boolean result = false;
		long startTime = System.currentTimeMillis();
		String winamt = null;
		try {
			Thread.sleep(3000);
			log.debug("Function-> verifyWinCurrencyFormat");

			if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame"))) {
				closeOverlayForLVC();
				Thread.sleep(1000);
				winamt = getConsoleText("return " + xpathMap.get("WinMobileText"));
			} else
				winamt = getConsoleText("return " + xpathMap.get("WinDesktopLabel"));

			if (winamt != null) {
				log.debug(" win occur " + winamt);
				// fetching win from panel
				String consolewinnew = winamt.replaceAll("win: ", "");
				consolewinnew = consolewinnew.replaceAll("WIN: ", "");
				consolewinnew = consolewinnew.replaceAll("Win: ", "");
				consolewinnew = consolewinnew.replaceAll("Win ", "");
				result = currencyFormatValidatorForLVC(consolewinnew, regExpression);
			} else {
				long currentime = System.currentTimeMillis();
				if (((currentime - startTime) / 1000) > 120) {
					System.out.println("verifyWinCurrencyFormat():No win occur  in 2");
					result = false;
				}
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.error("Win not occur, please  use win test data");

		}

		return result;
	}

	// Method to wait till win occurs and verify the currency format in win
	public boolean verifyBigWinCurrencyFormatForLVC(String regExpression, Mobile_HTML_Report currencyReport,
			String currencyName) {
		boolean regMatch = false;
		boolean result = false;

		long startTime = System.currentTimeMillis();
		String winamt = null;
		try {
			// Thread.sleep(2000);
			log.debug("Function-> verifyBigWinCurrencyFormat");

			if ("Yes".equalsIgnoreCase(xpathMap.get("BigWinlayers"))) {
				for (int i = 1; i <= 3; i++) {
					Thread.sleep(4000);

					waitForbigwin();

					log.debug("Bigwinlayer captured" + i);
					System.out.println("Bigwinlayer captured" + i);

					while (true) {
						if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame"))) {
							winamt = getConsoleText("return " + xpathMap.get("WinMobileText"));
							log.info("verifyBigWinCurrencyFormat():waiting for win  to occur");
						} else
							winamt = getConsoleText("return " + xpathMap.get("BigWinCountUpText"));
						if (winamt != null) {
							log.info(" win occur ");
							// fetching win from panel
							String consolewinnew = winamt.replaceAll("win: ", "");
							regMatch = currencyFormatValidatorForLVC(consolewinnew, regExpression);

							if (regMatch) {
								result = true;
								currencyReport.detailsAppendFolderOnlyScreeshot(currencyName);

							} else {
								result = false;
								currencyReport.detailsAppendFolderOnlyScreeshot(currencyName);
							}

							break;
						} else {
							long currentime = System.currentTimeMillis();
							if (((currentime - startTime) / 1000) > 120) {
								System.out.println("verifyBigWinCurrencyFormat():No win occur  in 2");
								result = false;
								break;
							}
						}

					}
				}
			} else {
				Thread.sleep(8000);
				winamt = getConsoleText("return " + xpathMap.get("BigWinCountUpText"));
				if (winamt != null) {
					log.info(" win occur ");
					// fetching win from panel
					String consolewinnew = winamt.replaceAll("win: ", "");
					consolewinnew = consolewinnew.replaceAll("WIN: ", "");
					consolewinnew = consolewinnew.replaceAll("Win: ", "");
					consolewinnew = consolewinnew.replaceAll("Win ", "");
					regMatch = currencyFormatValidatorForLVC(consolewinnew, regExpression);
					if (regMatch) {
						result = true;

					} else {
						result = false;
					}
				} else {
					log.debug("no win occur");
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.error("Big win not occur.please  use bigwin test data");

		}

		return result;
	}

	public void jackpotSummaryWinCurrFormatForLVC(String regExpression, Mobile_HTML_Report currencyReport,
			String currencyName) {
		String jackpotWin = null;
		boolean isWinInCurrencyFormat = false;
		try {
			log.debug("Function -> jackpotSummaryWinCurrencyFormat()");

			jackpotWin = "return " + xpathMap.get("bonusWinAmount");

			String bonusWinnew = getConsoleText(jackpotWin);
			// To validate currency Amount format
			isWinInCurrencyFormat = currencyFormatValidatorForLVC(bonusWinnew, regExpression);

			log.debug("Fetching Jackpot summary currency symbol " + "/n Jackpot summary currency symbol is:: "
					+ jackpotWin);
			if (isWinInCurrencyFormat) {
				currencyReport.detailsAppendFolder("Verify jackpot currency when win occurs ",
						"Jackpot win should display with correct currency format and and currency symbol ",
						"Jackpot win displaying with correct currency format and and currency symbol ", "Pass",
						currencyName);

			} else {
				currencyReport.detailsAppendFolder("Verify jackpot currency in win occurs ",
						"Jackpot win should display with correct currency format and and currency symbol ",
						"Jackpot win is not  displaying with correct currency format and and currency symbol ", "Fail",
						currencyName);
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/*
	 * Method to check the currency format in free spin summary
	 */
	public void freeSpinSummaryWinCurrFormatForLVC(String regExpression, Mobile_HTML_Report currencyReport,
			String currencyName) {
		String freespinwin = null;
		boolean isWinInCurrencyFormat = false;
		try {
			log.debug("Function -> freeSpinWinCurrencyFormat()");
			if (!GameName.contains("Scratch")) {
				freespinwin = "return " + xpathMap.get("FSSumaryAmount");
			} else {
				// need to update the hook for Scratch game
				freespinwin = "return " + xpathMap.get("InfobarBettext");
			}
			String freespinwinnew = getConsoleText(freespinwin);
			// To validate currency Amount format
			isWinInCurrencyFormat = currencyFormatValidatorForLVC(freespinwinnew, regExpression);

			if (isWinInCurrencyFormat) {
				currencyReport.detailsAppendFolder("Verify freespins summary currency when win occurs ",
						"freespins summary win should display with correct currency format and and currency symbol ",
						"freespins summary win displaying with correct currency format and and currency symbol ",
						"Pass", currencyName);

			} else {
				currencyReport.detailsAppendFolder("Verify freespins summary currency in win occurs ",
						"freespins summary win should display with correct currency format and and currency symbol ",
						"freespins summary win is not  displaying with correct currency format and and currency symbol ",
						"Fail", currencyName);
			}

			// log.debug("Fetching Free spin summary currency symbol" + "/n Free spin
			// summary currency symbol is::"
			// + freespinwin);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

	}

	public void freeGameSummaryWinCurrFormatForLVC(String regExpression, Mobile_HTML_Report currencyReport,
			String currencyName) {
		String freeGamesWin = null;
		boolean isWinInCurrencyFormat = false;
		try {
			log.debug("Function -> freeSpinWinCurrencyFormat()");
			if (!GameName.contains("Scratch")) {
				freeGamesWin = "return " + xpathMap.get("freeGamesSummaryAmt");
			}
			String freegameswinnew = getConsoleText(freeGamesWin);

			// To validate currency Amount format
			isWinInCurrencyFormat = currencyFormatValidatorForLVC(freegameswinnew, regExpression);
			log.debug("Fetching Free games summary win " + freeGamesWin);

			if (isWinInCurrencyFormat) {
				currencyReport.detailsAppendFolder("Verify freegames summary currency when win occurs ",
						"freegames summary win should display with correct currency format and and currency symbol ",
						"freegames summary win displaying with correct currency format and and currency symbol ",
						"Pass", currencyName);

			} else {
				currencyReport.detailsAppendFolder("Verify freegames summary currency in win occurs ",
						"freegames summary win should display with correct currency format and and currency symbol ",
						"freegames summary win is not  displaying with correct currency format and and currency symbol ",
						"Fail", currencyName);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public String getCurrentBetForLVC() {
		String consoleBet = null;
		String bet = null;
		try {
			log.debug("reading bet text form base scene");
			if (!GameName.contains("Scratch")) {
				if (Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(xpathMap.get("TypeOfGame"))) {
					bet = "return " + xpathMap.get("BetTextValue");

				} else {
					bet = "return " + xpathMap.get("BetButtonLabel");

				}
			} else {
				bet = "return " + xpathMap.get("InfobarBettext");

			}
			log.debug("read bet text form base scene");
			consoleBet = getConsoleText(bet);
		} catch (Exception e) {
			log.error("Error in verifying multiplier", e);
		}
		return consoleBet;
	}

	public double GetBetAmt() {
		String consolePayoutnew = null;
		double consolePayoutnew1 = 0.0;
		try {
			String bet = "return " + xpathMap.get("BetSizeText");
			String consoleBet = getConsoleText(bet);
			consolePayoutnew = consoleBet.replaceAll("[^0-9]", "");
			consolePayoutnew1 = Double.parseDouble((consolePayoutnew));
		} catch (Exception e) {
			e.getMessage();
		}
		return consolePayoutnew1 / 100;
	}

	/**
	 * Malta GameHistory text comparison from Menu, click and navigation
	 */
	public boolean maltaGameHistoryFromTopBarMenu(Mobile_HTML_Report report) throws InterruptedException {
		String menuclick = "clickOnMenuOnTopbar";
		String locator = "GameHistoryFromMenu";
		String Text = clickAndGetText(report, menuclick, locator);
		boolean text = false;
		try {
			if (Text.equals("Game History")) {
				log.debug("Game History Text from Menu is same");
				text = true;
			}
			Thread.sleep(2000);
			clickAndNavigate(report, locator);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return text;
	}

	/**
	 * Malta help text comparison from Menu, click and navigation
	 */
	public boolean maltaHelpFromTopBarMenu(Mobile_HTML_Report report) throws InterruptedException {
		String menuclick = "clickOnMenuOnTopbar";
		String locator = "HelpFromMenu";
		String Text = clickAndGetText(report, menuclick, locator);
		boolean text = false;
		try {
			if (Text.equals("Help")) {
				log.debug("Help Text from Menu is same");
				text = true;
			}
			Thread.sleep(2000);
			clickAndNavigate(report, locator);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return text;
	}

	/**
	 ** Malta ResponsibleGaming text comparison from Menu, click and navigation
	 */
	public boolean maltaResponsibleGamingFromTopBarMenu(Mobile_HTML_Report report) throws InterruptedException {
		String menuclick = "clickOnMenuOnTopbar";
		String locator = "ResponsibleGamingFromMenu";
		String Text = clickAndGetText(report, menuclick, locator);
		boolean text = false;
		try {
			if (Text.equals("Responsible Gaming")) {
				log.debug("Responsible Gaming Text from Menu is same");
				text = true;
			}
			Thread.sleep(2000);
			clickAndNavigate(report, locator);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return text;

	}

	/**
	 * Verify malta Currency Format
	 */
	public boolean maltaCurrencyCheck(Mobile_HTML_Report report) {
		// String currencyFormat = "€ #.###,##";
		String currencyFormat = xpathMap.get("Malta_Currency_BritishPound");
		boolean credit = false;
		boolean result = true;
		String strBetExp = null;
		String regexp = null;
		try {
			String consoleCredit = getCurrentCredits();
			log.debug("Credit in base scene=" + consoleCredit);
			String str = consoleCredit;
			int index = str.indexOf("£");

			if (index != -1) {
				// log.debug("CURRENCY Symbol is at index %d\n", index);
				report.detailsAppend(" Currency ", "Currency idex is at  " + index, "Currency idex is at  " + index,
						"PASS");
				credit = true;

				log.debug("Function-> verifyCurrencyFormat() ");
				// Read console credits

				consoleCredit = consoleCredit.replace("credits: ", "");
				consoleCredit = consoleCredit.replace("CREDITS: ", "");

				String betregexp = createregexp(consoleCredit, currencyFormat);
				if (currencyFormat.contains("$"))
					strBetExp = betregexp.replace("$", "\\$");
				else
					strBetExp = betregexp;
				Pattern pattren = Pattern.compile(strBetExp);
				log.debug("Currency is  " + pattren);
				report.detailsAppend(" Currency  ", " Currency is " + pattren, "Currency is " + pattren, "PASS");
				regexp = strBetExp.replace("#", "\\d");

				if (Pattern.matches(regexp, consoleCredit)) {
					result = true;
				} else {
					result = false;
				}

				return result;
			} else {
				log.debug("No, The Currency is Different ");
				report.detailsAppend(" Currency ", "Currency idex is at  " + index, "Currency idex is at  " + index,
						"FAIL");
				credit = false;
			}
		} // Close for Try Block
		catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return credit;

	}

	/**
	 * verify & Compare portugal Currency
	 */
	public boolean portugalCurrencyCheck(Mobile_HTML_Report report) {
		// String currencyFormat ="$#,###.##";
		String currencyFormat = xpathMap.get("Portugalu_Currency");
		boolean credit = false;
		boolean result = true;
		String strBetExp = null;
		String regexp = null;
		try {
			String consoleCredit = getCurrentCredits();
			log.debug("Credit in base scene=" + consoleCredit);
			String str = consoleCredit;
			int index = str.indexOf("€");

			if (index != -1) {
				// log.debug("CURRENCY Symbol is at index %d\n", index);
				report.detailsAppend(" Currency Index ", "Currency idex is at  " + index,
						"Currency idex is at  " + index, "PASS");
				credit = true;

				log.debug("Function-> verifyCurrencyFormat() ");
				// Read console credits

				consoleCredit = consoleCredit.replace("credits: ", "");
				consoleCredit = consoleCredit.replace("CREDITS: ", "");
				consoleCredit = consoleCredit.replace("CRÉDITOS: ", "");

				String betregexp = createregexp(consoleCredit, currencyFormat);
				if (currencyFormat.contains("$"))
					strBetExp = betregexp.replace("$", "\\$");
				else
					strBetExp = betregexp;
				Pattern pattren = Pattern.compile(strBetExp);
				log.debug("Currency is  " + pattren);
				report.detailsAppend(" Currency Format  ", " Currency is " + pattren, "Currency is " + pattren, "PASS");
				regexp = strBetExp.replace("#", "\\d");

				if (Pattern.matches(regexp, consoleCredit)) {
					result = true;
				} else {
					result = false;
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);

		}
		return credit;
	}

	/**
	 * Verifies Portugal help text comparison , click and its navigation from menu
	 */
	public boolean portugalHelpFromTopBarMenu(Mobile_HTML_Report report) throws InterruptedException {
		String menuclick = "clickOnMenuOnTopbar";
		String locator = "HelpFromMenu";
		String Text = clickAndGetText(report, menuclick, locator);
		boolean text = false;
		String allHelpText[] = { "Help", "Ajuda" };
		try {
			for (int i = 0; i < allHelpText.length; i++) {
				if (Text.equals(allHelpText[i])) {
					log.debug("Help Text from Menu is same");
					text = true;
				}
			}
			Thread.sleep(2000);
			clickAndNavigate(report, locator);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return text;
	}

	/**
	 * Verifies Portugal GameHistory text comparison , click and its navigation from
	 * menu
	 */
	public boolean portugalGameHistoryFromTopBarMenu(Mobile_HTML_Report report) throws InterruptedException {
		String menuclick = "clickOnMenuOnTopbar";
		String locator = "GameHistoryFromMenu";
		String Text = clickAndGetText(report, menuclick, locator);
		boolean text = false;
		String allGameHistoryText[] = { "Game History", "Historial do jogo" };
		try {
			for (int i = 0; i < allGameHistoryText.length; i++) {
				if (Text.equals(allGameHistoryText[i])) {
					log.debug("Game History Text from Menu is same");
					text = true;
				}
			}
			Thread.sleep(2000);
			clickAndNavigate(report, locator);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return text;

	}

	/**
	 * Verifies Romania help text comparison , click and its navigation from menu
	 */
	public boolean romaniaHelpFromTopBarMenu(Mobile_HTML_Report report) throws InterruptedException {
		String menuclick = "clickOnMenuOnTopbar";
		String locator = "HelpFromMenu";
		String Text = clickAndGetText(report, menuclick, locator);
		boolean text = false;
		String allHelpText[] = { "Help", "Ajutor" };
		try {
			for (int i = 0; i < allHelpText.length; i++) {
				if (Text.equals(allHelpText[i])) {
					log.debug("Help Text from Menu is same");
					text = true;
				}
			}
			Thread.sleep(2000);
			clickAndNavigate(report, locator);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return text;
	}

	/**
	 * Verifies Romania GameHistory text comparison , click and its navigation from
	 * menu
	 */
	public boolean romaniaGameHistoryFromTopBarMenu(Mobile_HTML_Report report) throws InterruptedException {
		String menuclick = "clickOnMenuOnTopbar";
		String locator = "GameHistoryFromMenu";
		String Text = clickAndGetText(report, menuclick, locator);
		boolean text = false;
		String allGameHistoryText[] = { "Game History", "Istoric joc" };
		try {
			for (int i = 0; i < allGameHistoryText.length; i++) {
				if (Text.equals(allGameHistoryText[i])) {
					log.debug("Game History Text from Menu is same");
					text = true;
				}
			}
			Thread.sleep(2000);
			clickAndNavigate(report, locator);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return text;

	}

	/**
	 * verify & Compare romania Currency
	 */
	public boolean romaniaCurrencyCheck(Mobile_HTML_Report report) {
		String currencyFormat = xpathMap.get("Romania_Currency");
		boolean credit = false;
		boolean result = true;
		String strBetExp = null;
		String regexp = null;
		try {
			String consoleCredit = getCurrentCredits();
			log.debug("Credit in base scene=" + consoleCredit);
			String str = consoleCredit;
			int index = str.indexOf("£");

			if (index != -1) {
				// log.debug("CURRENCY Symbol is at index %d\n", index);
				report.detailsAppend(" Currency ", "Currency idex is at  " + index, "Currency idex is at  " + index,
						"PASS");
				credit = true;

				log.debug("Function-> verifyCurrencyFormat() ");
				// Read console credits

				consoleCredit = consoleCredit.replace("credits: ", "");
				consoleCredit = consoleCredit.replace("CREDITS: ", "");
				consoleCredit = consoleCredit.replace("CREDITE: ", "");

				String betregexp = createregexp(consoleCredit, currencyFormat);
				if (currencyFormat.contains("$"))
					strBetExp = betregexp.replace("$", "\\$");
				else
					strBetExp = betregexp;
				Pattern pattren = Pattern.compile(strBetExp);
				log.debug("Currency is  " + pattren);
				report.detailsAppend(" Currency  ", " Currency is " + pattren, "Currency is " + pattren, "PASS");
				regexp = strBetExp.replace("#", "\\d");

				if (Pattern.matches(regexp, consoleCredit)) {
					result = true;
				} else {
					result = false;
				}

				return result;
			} else {
				log.debug("No, The Currency is Different ");
				report.detailsAppend(" Currency ", "Currency idex is at  " + index, "Currency idex is at  " + index,
						"FAIL");
				credit = false;
			}
		} // Close for Try Block
		catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return credit;

	}

	public boolean check(Mobile_HTML_Report sweden, String amount) throws InterruptedException {

		WebElement bet1 = webdriver.findElement(By.xpath(xpathMap.get("betpoker")));
		bet1.click();

		WebElement elementClick = webdriver.findElement(By.xpath(xpathMap.get("pokerhook")));

		/*
		 * webdriver.executeScript("arguments[0].click();", elementClick);
		 * JavascriptExecutor js = (JavascriptExecutor) webdriver;
		 * js.executeScript("arguments[0].scrollIntoView();", elementClick);
		 * 
		 * 
		 * 
		 * 
		 * JavascriptExecutor js = (JavascriptExecutor) webdriver;
		 * js.executeScript("javascript:document.getElementById(\"" +
		 * xpathMap.get("userInput") + "\").value=" + amount + ";");// (Value took from
		 * Excel) Thread.sleep(3000);
		 * System.out.println("Slider value1 for credits is : " +
		 * bet1.getAttribute("value"));// value is not updated // in the game 90 on //
		 * the gaming screen
		 * 
		 * if (osPlatform.equalsIgnoreCase("Android")) { bet1.sendKeys(Keys.RIGHT); }
		 * else { bet1.sendKeys(Keys.TAB); // slider.click();
		 * 
		 * }
		 * 
		 * System.out.println("Slider value2 for credits is : " +
		 * bet1.getAttribute("value"));// value updated in the // game 90.01
		 * log.debug("Slider value2 for credits is : " + bet1.getAttribute("value"));
		 * selectedamout = bet1.getAttribute("value");
		 */

		JavascriptExecutor js = (JavascriptExecutor) webdriver;
		js.executeScript("windows.scrollBy(0,100)");

		Actions actions = new Actions(webdriver);
		int getX = elementClick.getLocation().getX();
		System.out.println("x Aixs " + getX);
		int getY = elementClick.getLocation().getY();
		System.out.println("y Aixs " + getY);
		actions.moveToElement(elementClick, getX, getY).click().build().perform();

		Thread.sleep(3000);

		return false;
	}

	public void freeGameInfoCurrencyFormat(String regExpression, Mobile_HTML_Report currencyReport,
			String currencyName) {
		boolean isValueInCurrencyFormat = false;
		String freeGamesInfo = null;
		String infoCurrency = null;
		String freegameInfoCurrency = null;

		try {
			log.debug("Function -> freeGameInfoBetCurrencyFormat()");

			freeGamesInfo = "return " + xpathMap.get("freeGameInfoBetText");
			String freegamesInfoFormat = getConsoleText(freeGamesInfo);
			int index = freegamesInfoFormat.lastIndexOf("@");
			System.out.println(index);
			if (index > 0) {
				freegameInfoCurrency = freegamesInfoFormat.substring(index + 1, freegamesInfoFormat.length());
				infoCurrency = freegameInfoCurrency.trim();
				System.out.println(freegameInfoCurrency.trim());
			}
			// To validate currency Amount format
			isValueInCurrencyFormat = currencyFormatValidatorForLVC(infoCurrency, regExpression);
			log.debug("Fetching Free games summary currency symbol " + "/n free games summary currency symbol is:: "
					+ infoCurrency);

			if (isValueInCurrencyFormat) {
				currencyReport.detailsAppendFolder("Verify freegames info currency ",
						"freegames info currency should display with correct currency format and and currency symbol ",
						"freegames info currency displaying with correct currency format and and currency symbol ",
						"Pass", currencyName);

			} else {
				currencyReport.detailsAppendFolder("Verify freegames info currency ",
						"freegames info currency should display with correct currency format and and currency symbol ",
						"freegames info currency displaying with incorrect currency format and and currency symbol ",
						"Fail", currencyName);
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	public void validateMiniPaytableForLVC(Mobile_HTML_Report report, String regExpression, String currencyName) {

		int isAllTrue = 0;
		long symbolNumber;

		Util util = new Util();
		int length = util.xmlLength("./" + GameName + "/Config/" + GameName + ".xml", "WinningCombinations");

		ArrayList<String> symbolData = util.getXMLDataInArray("Symbol", "name",
				"./" + GameName + "/Config/" + GameName + ".xml");

		ArrayList<String> symbolArray = new ArrayList<>();
		ArrayList<String> proceedSymbolArray = new ArrayList<>();

		HashMap<String, Symbol> symbolmMap = new HashMap<>();

		int totalNoOfSymbols = symbolData.size();
		log.debug("totalNoOfSymbols: " + totalNoOfSymbols);

		for (int count = 0; count < totalNoOfSymbols; count++) {
			String[] xmlData = symbolData.get(count).split(",");
			String symbolName = xmlData[0];
			symbolArray.add(count, symbolName);
			proceedSymbolArray.add(count, symbolName);
			symbolmMap.put(symbolName, new Symbol(symbolName));
		}
		for (int count = 0; count < length; count++) {
			String strWinCombination = util.readXML("WinCombination", "numSymbolsRequired", "symbols", "payouts",
					"./" + GameName + "/Config/" + GameName + ".xml", length + 2, count);
			if (strWinCombination != null) {
				WinCombination winCombination = new WinCombination();

				String[] xmlData = strWinCombination.split(",");

				winCombination.setNumSymbolsRequired(Integer.parseInt(xmlData[0].trim()));
				winCombination.setSymbols(xmlData[1]);
				winCombination.setPayout(Integer.parseInt(xmlData[2].trim()));

				symbolmMap.get(xmlData[1]).addWinComb(winCombination);
			}
		}

		int winCombinationSize = length;

		proceedSymbolArray.remove("FS_Scatter");
		proceedSymbolArray.remove("WildMagic");
		proceedSymbolArray.remove("WildChest");
		// proceedSymbolArray.remove("Wild");
		proceedSymbolArray.remove("PiggyBank");
		proceedSymbolArray.remove("Safe");
		log.debug("Total  Symbols::" + proceedSymbolArray);
		double bet = GetBetAmt();

		do {
			for (int column = 0; column <= 4; column++) {
				for (int row = 1; row <= 3; row++) {

					Map<String, String> paramMap = new HashMap<String, String>();
					paramMap.put("param1", Integer.toString(column));
					paramMap.put("param2", Integer.toString(row));

					String symbolNumberHook = xpathMap.get("getSymbolNumber");
					String newHook = replaceParamInHook(symbolNumberHook, paramMap);
					symbolNumber = getConsoleNumeric("return " + newHook);
					// System.out.println("symbolNumber "+symbolNumber);

					String symbolName = symbolArray.get((int) symbolNumber);
					// System.out.println("symbolName "+symbolName);
					// System.out.println("symbolArray "+symbolArray);

					if (proceedSymbolArray.contains(symbolName)) {
						String symbolClickHook = xpathMap.get("clickOnSymbol");
						newHook = replaceParamInHook(symbolClickHook, paramMap);
						clickAtButton("return " + newHook);

						try {
							threadSleep(2000);
							report.detailsAppendFolderOnlyScreeshot(currencyName);

						} catch (Exception e1) {
							log.error(e1.getStackTrace());
							log.error("error while taking screenshot ", e1);
							log.error(e1.getMessage());
						}

						// Fetching the payout values from clicked symbol
						for (int count = 2; count < 6; count++) {

							String payoutValue = xpathMap.get("getPayoutValue");
							paramMap.put("param1", Integer.toString(count));
							newHook = replaceParamInHook(payoutValue, paramMap);
							String paytableValue = getConsoleText("return " + newHook);

							ArrayList<WinCombination> symWinCombs = symbolmMap.get(symbolName).getWinCombList();

							int payout = 0;
							for (WinCombination winCombination : symWinCombs) {
								if (winCombination.getNumSymbolsRequired() == count) {
									payout = winCombination.getPayout();
								}
							}

							if (payout != 0 && "".equalsIgnoreCase(paytableValue)) {
								log.debug("Fail for Symbol" + symbolName + " and count ::" + count);
							} else if (payout == 0 && !"".equalsIgnoreCase(paytableValue)) {
								log.debug("Fail for Symbol" + symbolName + " and count ::" + count);
							} else if (payout == 0 && "".equalsIgnoreCase(paytableValue)) {
								log.debug("Fail There is no payout for Symbol" + symbolName + " and count ::" + count);
							} else {

								try {
									boolean result = currencyFormatValidatorForLVC(paytableValue, regExpression);
									Thread.sleep(500);

									if (result) {
										isAllTrue++;
										log.debug("Minipaytable payout is correct for bet value :" + bet
												+ " currency format validation is :" + result
												+ " for Minipaytable payout for symbol" + symbolName);

										log.debug("PASS for Symbol" + symbolName + " and count ::" + count);

									} else {

										log.debug("Minipaytable payout is incorrect for bet value :" + bet
												+ " currency format validation is :" + result
												+ " for Minipaytable payout for symbol" + symbolName);

									}

								} catch (Exception e) {

									log.error(e.getStackTrace());
									log.error("error while verifying currency format for mini paytable ", e);
									log.error(e.getMessage());
								}
							}

						}

						proceedSymbolArray.remove(symbolName);
						// System.out.println(proceedSymbolArray.remove(symbolName));
						log.debug("Remaining Symbols::" + proceedSymbolArray);
						// System.out.println("Remaining Symbols::" + proceedSymbolArray);

					}

				}
			}
			if (proceedSymbolArray.size() > 0) {
				try {
					spinclick();
				} catch (InterruptedException e) {
					log.error("validateMiniPaytableForLVC", e);
					log.error(e.getMessage());
				}
				waitForSpinButtonstop();
				try {
					Thread.sleep(7000);
				} catch (InterruptedException e) {
					log.error(e.getStackTrace());
					log.error("error for Thread.sleep ", e);
					log.error(e.getMessage());
				}
			}

		} while (proceedSymbolArray.size() > 0);

		if (isAllTrue == winCombinationSize) {
			report.detailsAppendNoScreenshot(
					"verify MiniPaytable payout currency format for the selected bet value with the game currency format",
					"MiniPaytable payout verification  ith the game currency format ",
					"MiniPaytable payout verification with the game currency format is done", "pass");

		} else {
			report.detailsAppendNoScreenshot(
					"verify MiniPaytable payout verification for the bet : " + bet + " with the game currency format",
					"MiniPaytable payout verification with the game currency format ",
					"MiniPaytable payout verification with the game currency format is done but failed coz some formats are not matched",
					"fail");

		}
	}

	public void isFreeGamesVisible(String url) {
		long startTime = System.currentTimeMillis();
		try {
			while (true) {
				boolean currentSceneflag = getConsoleBooleanText("return " + xpathMap.get("isFGVisible"));

				System.out.println("free games visible " + currentSceneflag);
				if (currentSceneflag) {
					log.info("free games visible " + currentSceneflag);
					Thread.sleep(1000);
					break;
				} else {
					long currentime = System.currentTimeMillis();
					if (((currentime - startTime) / 1000) > 5) {
						Thread.sleep(5000);

						System.out.println("No free games reflect on screen , refresh ");
						log.info("No free games reflect on screen , refresh ");

						loadGame(url);
					}
				}
			}

		} catch (Exception e) {
			log.error("error while waiting for free games screen ", e);
			log.error(e.getMessage());
		}
	}

	public void isBonusMultiplierVisible() {
		try {
			while (true) {
				elementWait("return " + xpathMap.get("isBonusReelVisible"), true);
				boolean isBonusVisible = getConsoleBooleanText("return " + xpathMap.get("isBonusReelVisible"));

				if (isBonusVisible) {
					log.info("Bonus is visible " + isBonusVisible);
					System.out.println("Bonus is visible " + isBonusVisible);
					break;
				} else {
					log.info("Bonus is not visible, wait " + isBonusVisible);
				}
			}

		} catch (Exception e) {
			log.error("error while waiting for Bonus ", e);
			log.error(e.getMessage());
		}
	}

	// TS64283 for autoplay
	public void autoPlay_with_QSUpdated(Mobile_HTML_Report report) {
		try {
			String QS_currectStateFirst = getConsoleText("return " + xpathMap.get("QuickSpinBtnState"));

			Thread.sleep(1000);

			if (QS_currectStateFirst.equalsIgnoreCase("activeSecondary")) {
				clickAtButton("return " + xpathMap.get("QuickSpinBtn"));
			}

			Thread.sleep(1000);

			String QS_currectState = getConsoleText("return " + xpathMap.get("QuickSpinBtnState"));

			if (QS_currectState.equalsIgnoreCase("active")) {

				clickAtButton("return " + xpathMap.get("AutoPlayStartBtn"));

				if (getConsoleBooleanText("return " + xpathMap.get("isAutoPlayActive")))

					report.detailsAppend("verify that AutoPlay with QuickSpin is on and AutoPlay should be started.",
							"AutoPlay with QuickSpin is  must be available with Autoplay start.",
							"AutoPlay with QuickSpin is available and autoplay is started", "pass");
				else
					report.detailsAppend("verify that AutoPlay with QuickSpin is on and AutoPlay should be started.",
							"AutoPlay with QuickSpin is  must be available with Autoplay start.",
							"AutoPlay with QuickSpin is NOT available and autoplay is NOT started", "Fail");

				threadSleep(7000);

				report.detailsAppend("verify that AutoPlay count must be available.",
						"AutoPlay with count must be available.", "AutoPlay with count is available", "pass");

				clickAtButton("return " + xpathMap.get("AutoPlayStop"));

				threadSleep(1000);
				if (getConsoleBooleanText("return " + xpathMap.get("isAutoPlayActive")) == false) {

					report.detailsAppend("verify that AutoPlay count is NOT displaying ",
							"verify that AutoPlay count is not displayed .",
							"AutoPlay Feacture has stopped and count is not available", "pass");

				} else if (getConsoleBooleanText("return " + xpathMap.get("isAutoPlayActive"))) {
					report.detailsAppend("verify that AutoPlay count is NOT displaying ",
							"verify that AutoPlay count is not displayed .",
							"AutoPlay Feacture has not stopped and count is  available", "fail");
				}
				// while(true){
				// boolean isAutoplay = getConsoleBooleanText("return
				// "+xpathMap.get("isAutoPlayActive"));
				// if(!isAutoplay){
				// break;
				// }
				// }
				log.debug("Clicked on autoplay");
			} else {
				log.debug("quick spin button not active");

				report.detailsAppend("verify that AutoPlay with QuickSpin on must be available.",
						"AutoPlay with QuickSpin on must be available.", "Quick Spin is not active", "Fail");

			}
			setQuickSpinOff();
		} catch (Exception e)

		{
			log.error("Error in autoPlay_with_QSUpdated", e);
		}
	}

	// TS64283 capturePaytableScreenshot With Landscape Mode
	public void capturePaytableScreenshotWithLandscape(Mobile_HTML_Report report, String language) {
		try {
			JavascriptExecutor js = (webdriver);
			Coordinates coordinateObj = new Coordinates();
			clickAtButton("return " + xpathMap.get("MenuPaytableBtn"));
			log.debug("Clicked on paytable button");
			Thread.sleep(2000);
			/*
			 * Adding the Jackpot info table screen shots for game having bolt on features
			 */
			if (checkAvilability(xpathMap.get("IsJackpotInfoPaytableComponentVisible"))) {
				captureJackpotInfoPaytable(report, language);
				clickAtButton("return " + xpathMap.get("CloseJackpotInfoPaytableComponent"));
			}
			// report.detailsAppendFolder("verify the paytable screen shot", " paytable
			// first page screen shot", "paytable first page screenshot ", "pass",
			// language);
			typeCasting("return " + xpathMap.get("PaytableScrollHeight"), coordinateObj);
			coordinateObj.setPaytableFullHeight2(coordinateObj.getX());
			typeCasting("return " + xpathMap.get("PaytableScroll_h"), coordinateObj);
			coordinateObj.setPaytableHeight2(coordinateObj.getX());
			// height adjustment because of text missing out
			int paytablHeight2 = ((int) coordinateObj.getPaytableHeight2()) - 70;
			int scroll = (int) (coordinateObj.getPaytableFullHeight2() / paytablHeight2);

			System.out.println("Total Screenshot count " + scroll);

			report.detailsAppendFolder("verify the paytable screen shot in landscape Mode ",
					" paytable next page screen shot in landscape Mode ",
					"paytable next page screenshot in landscape Mode  ", "pass", language);
			for (int i = 1; i <= scroll + 1; i++) {
				threadSleep(1000);
				js.executeScript("return " + forceNamespace
						+ ".getControlById('PaytableComponent').paytableScroller.scrollTo(0, -" + paytablHeight2 * i
						+ ")");
				threadSleep(1000);
				report.detailsAppendFolder("verify the paytable screen shot in landscape Mode ",
						" paytable next page screen shot in landscape Mode ",
						"paytable next page screenshot in landscape Mode ", "pass", language);
			}
		} catch (Exception e) {
			log.error("error in paytableOpenScroll()", e);
		}
	}

	// ts64283
	public String VerifystoryoptioninpaytableWithLandscape(Mobile_HTML_Report report, String languageCode) {
		Wait = new WebDriverWait(webdriver, 60);
		String story = null;
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("clock_ID"))));
			clickAtButton("return " + xpathMap.get("MenuPaytableBtn"));
			log.debug("Clicked on paytable icon to open ");
			if (checkAvilability(xpathMap.get("IsJackpotInfoPaytableComponentVisible"))) {
				captureJackpotInfoPaytable(report, languageCode);
				clickAtButton("return " + xpathMap.get("CloseJackpotInfoPaytableComponent"));
			}
			Thread.sleep(1500);
			// open story for Amber take screen shot and then close
			clickAtButton("return " + xpathMap.get("PaytableStoryAmberBtn"));
			report.detailsAppendFolder("verify the Amberstory screen shot in Landscape mode",
					" Amberstory first page screen shot in Landscape mode ",
					"Amberstory first page screenshot in Landscape mode ", "pass", languageCode);
			// scrollstory(report,languageCode);
			closestory();
			log.debug("Stroy capture for Amber in Landscape mode");
			Thread.sleep(3000);

			// open story for Troy take screen shot and then close
			clickAtButton("return " + xpathMap.get("PaytableStoryTroyBtn"));
			report.detailsAppendFolder("verify the Troystory screen shot in Landscape mode ",
					" Troystory first page screen shot in Landscape mode ",
					"Troystory first page screenshot in Landscape mode  ", "pass", languageCode);
			// scrollstory(report,languageCode);
			closestory();
			log.debug("Stroy capture for Troy in Landscape mode ");
			Thread.sleep(3000);

			// open story for Michael take screen shot and then close
			clickAtButton("return " + xpathMap.get("PaytableStoryMichaelBtn"));
			report.detailsAppendFolder("verify the Michaelstory screen shot in Landscape mode ",
					" Michaelstory first page screen shot in Landscape mode ",
					"Michaelstory first page screenshot in Landscape mode ", "pass", languageCode);
			// scrollstory(report,languageCode);
			closestory();
			log.debug("Stroy capture for Michael in Landscape mode ");
			// open story for Sarah take screen shot and then close
			clickAtButton("return " + xpathMap.get("PaytableStorySarahBtn"));
			report.detailsAppendFolder("verify the Sarahstory screen shot in Landscape mode ",
					" Sarahstory first page screen shot in Landscape mode ",
					"Sarahstory first page screenshot in Landscape mode  ", "pass", languageCode);
			// scrollstory(report,languageCode);
			closestory();
			log.debug("Stroy capture for Sarah in Landscape mode  ");
			story = "story1";
		} catch (Exception e) {
			log.error("error in opening story", e);
		}
		return story;
	}

	// TS64283 unlock All FreeSpin
	public boolean UnlockAllFreeSpin() {

		boolean UnlockAllFreeSpin = false;

		try {

			int i = 1;

			String FreeSpinEntryScreen;
			String b2 ;
			FreeSpinEntryScreen	= xpathMap.get("FreeSpinEntryScreen");
			b2= entryScreen_Wait_Upadated_FreeSpin(FreeSpinEntryScreen);

			while (getConsoleBooleanText("return " + xpathMap.get("FourthSFreeSpinVisible")) == false && i < 20) {

				log.info("UnlockAllFreeSpin Method is Running start");
					Thread.sleep(2000);
					if (b2.equalsIgnoreCase("freeSpin"))
					{

						clickBonusSelection(1);
						log.info("Freespin continue button is clicked and awaiting for spin Button");
					}else {
						
						System.out.println("FreeSpinEntry Scrren Not displaying");
						log.error("FreeSpinEntry Scrren Not displaying");
								
					}

				

				Thread.sleep(2000);

				FSSceneLoading();

				Thread.sleep(5000);

				waitSummaryScreen();

				Thread.sleep(3000);

				waitForSpinButton();

				spinclick();

				Thread.sleep(5000);

			 FreeSpinEntryScreen = xpathMap.get("FreeSpinEntryScreen");
			 b2 = entryScreen_Wait_Upadated_FreeSpin(FreeSpinEntryScreen);

				i++;

			}

			return UnlockAllFreeSpin = true;

		}

		catch (Exception e) {
			log.error("error in Freespin Unlocking", e);
		}

		return UnlockAllFreeSpin;

	}

	// TS64283 EntryScreenWait only for BonusSelection
	public String entryScreen_Wait_Upadated_FreeSpin(String entry_Screen) {
		long startTime = System.currentTimeMillis();
		String wait = "FS";
		try {
			if (entry_Screen.equalsIgnoreCase("yes")) {
				log.debug("Waiting for free spin entry screen to come");
				while (true) {
					String currentScene = GetConsoleText("return " + xpathMap.get("currentScene"));
					if (("BONUS_SELECTION".equalsIgnoreCase(currentScene))
							|| "FREESPINS_INTRO".equalsIgnoreCase(currentScene)
							|| "PLAYING_ACTIVE_BONUS".equalsIgnoreCase(currentScene)) {
						wait = "freeSpin";
						// Thread.sleep(3000);
						break;
					}

					long currentime = System.currentTimeMillis();
					// Break if wait is more than 30 secs
					if (((currentime - startTime) / 1000) > 30) {
						log.debug("break after 30 secs");
						break;
					}
				}
			} else
				log.debug("Free Spin Entry Screen is not avalable");
		} catch (Exception e) {
			log.error(e);
		}
		return wait;
	}

	
	public String clickBonusSelection_Updated(int freeSpinCount) {
		String FS_Credits = null;
		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("clock_ID"))));
			Thread.sleep(500);
			if (freeSpinCount == 1) {
				// clicking by hooks
				clickAtButton("return " + xpathMap.get("FirstFSSelectionS"));
			} else if (freeSpinCount == 2) {
				// clicking by hooks
				// clickAtButton("return
				// "+forceNamespace+".getControlById('BonusSelectionComponent').Sprites.fsselectionTroyActive.events.onInputUp.dispatch("+forceNamespace+".getControlById('BonusSelectionComponent').Sprites.fsselectionTroyActive)");
				clickAtButton("return " + xpathMap.get("SecondFSSelectionS"));
			} else if (freeSpinCount == 3) {
				// clicking by hooks
				// clickAtButton("return
				// "+forceNamespace+".getControlById('BonusSelectionComponent').Sprites.fsselectionMichaelActive.events.onInputUp.dispatch("+forceNamespace+".getControlById('BonusSelectionComponent').Sprites.fsselectionMichaelActive)");
				clickAtButton("return " + xpathMap.get("ThirdFSSelectionS"));
			} else if (freeSpinCount == 4) {
				// clicking by hooks
				// clickAtButton("return
				// "+forceNamespace+".getControlById('BonusSelectionComponent').Sprites.fsselectionSarahActive.events.onInputUp.dispatch("+forceNamespace+".getControlById('BonusSelectionComponent').Sprites.fsselectionSarahActive)");
				clickAtButton("return " + xpathMap.get("FourthFSSelectionS"));
			}
			Thread.sleep(1000);
		} catch (Exception e) {
			e.getMessage();
		}
		return FS_Credits;
	}
}