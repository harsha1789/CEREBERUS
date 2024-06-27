package Modules.Regression.TestScript;
import javax.swing.*;  
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.DeviceRotation;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.ClickAction;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.zensar.automation.framework.library.ZAFOCR;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Mobile_HTML_Report;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.offset.PointOption;
import java.io.File;
import net.sourceforge.tess4j.*;


public class ReadTextFromImage {
	Logger log = Logger.getLogger(ReadTextFromImage.class.getName());

	public ScriptParameters scriptParameters;

	public void script() throws Exception {

		AppiumDriver<WebElement> webdriver = scriptParameters.getAppiumWebdriver();

		System.out.println(webdriver.getContextHandles());
	
			WebDriverWait waitNew = new WebDriverWait(webdriver, 20);
			Thread.sleep(5000);
			//webdriver.get
				//	"https://mobile-app1-gtp37.installprogram.eu/mobileWebGames/game/mgs?displayName=Adventure+Palace&gameId=adventurePalace&gameVersion=adventurePalace_ComponentStore_1_10_4_635&moduleId=10025&clientId=40305&clientTypeId=40&languageCode=en&productId=5007&brand=islandparadise&loginType=InterimUPE&returnUrl=https://mobile-app1-gtp37.installprogram.eu/lobby/en/IslandParadise/games/&bankingUrl=https://mobile-app1-gtp37.installprogram.eu/lobby/en/IslandParadise&routerEndPoints=&currencyFormat=&isPracticePlay=False&username=harsha_73_AP_Mobile&password=test1234$&host=mobile");
			//webdriver.get("https://mobile-app1-gtp110.installprogram.eu/htmlGames/3.31.0/launch/jokerPoker_ComponentStore_1_3_1_218/mgs/jokerPoker?displayName=Joker+Poker&moduleId=23&clientId=40300&gamePath=/mgs/jokerPoker&clientTypeId=40&languageCode=en&productId=5007&market=dotcom&brand=islandparadise&loginType=InterimUPE&returnUrl=https://mobile-app1-gtp110.installprogram.eu/lobby/en/IslandParadise/games/&routerEndPoints=&currencyFormat=&isPracticePlay=False&username=harsha110&password=test1234$&formFactor=mobile");
			webdriver.navigate().to("https://mobile-app1-gtp83.installprogram.eu/htmlGames/3.33.0/launch/wildCatch_PlayNext_1_2_1_722/mgs/wildCatch?displayName=Wild+Catch&moduleId=10763&clientId=40300&gamePath=&clientTypeId=40&languageCode=en&productId=5007&market=dotcom&brand=islandparadise&loginType=InterimUPE&returnUrl=https://mobile-app1-gtp83.installprogram.eu/lobby/en/IslandParadise/games/&routerEndPoints=&currencyFormat=&isPracticePlay=False&username=Zen_sdb8pqm&password=test1234$&formFactor=mobile");
			Thread.sleep(10000);
			
			File imageFile = new File("D:\\ImageComparision\\WildCatch\\Images\\Desktop\\Credits.png");
			Tesseract tesseract = new Tesseract();	
			tesseract.setLanguage("eng");
			tesseract.setPageSegMode(3);
			tesseract.setOcrEngineMode(0);
			ZAFOCR ocr =new ZAFOCR();
			 String result1 = ocr.getText(imageFile);
				 String result = tesseract.doOCR(imageFile);
				 System.out.println(result);
			
			//*************************
			
		}
	}

	 
	 
	