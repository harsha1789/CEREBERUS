package Modules.Regression.TestScript;

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
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Mobile_HTML_Report;

import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.offset.PointOption;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 * This is a test script to test the device and browser connection
 * 
 * @author Premlata
 */

public class MobileTestScript
{
	
	Logger log = Logger.getLogger(MobileTestScript.class.getName());

	public ScriptParameters scriptParameters;

	public void script() throws Exception {

		AppiumDriver<WebElement> webdriver = scriptParameters.getAppiumWebdriver();

		System.out.println(webdriver.getContextHandles());
		
		
		
		
		String mstrTC_Name=scriptParameters.getMstrTCName();
		String mstrTC_Desc=scriptParameters.getMstrTCDesc();
		String mstrModuleName=scriptParameters.getMstrModuleName();
		BrowserMobProxyServer proxy=scriptParameters.getProxy();
		String startTime=scriptParameters.getStartTime();
		String filePath=scriptParameters.getFilePath();
	
		String userName=scriptParameters.getUserName();
		String DeviceName=scriptParameters.getDeviceName();
		String framework=scriptParameters.getFramework();
		String gameName=scriptParameters.getGameName();
		String language = "Paytable";
		String Status=null;
		int mintDetailCount=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		
		
		Mobile_HTML_Report lvcReport =new Mobile_HTML_Report(webdriver,DeviceName,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,gameName);
		MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,lvcReport, gameName);
		cfnlib.setOsPlatform(scriptParameters.getOsPlatform()); //To get OSPlatform 
		
		try {
			if (webdriver != null) {
				WebDriverWait waitNew = new WebDriverWait(webdriver, 20);
				Thread.sleep(5000);
				webdriver.get(
						"https://mobile-app1-gtp73.installprogram.eu/mobileWebGames/game/mgs/10_10_5_8061?displayName=Halloweenies&gameId=halloweenies&gameVersion=halloweenies_ComponentStore_1_4_8_398&moduleId=12531&clientId=40300&clientTypeId=40&languageCode=en&productId=5007&brand=islandparadise&loginType=InterimUPE&returnUrl=https://mobile-app1-gtp73.installprogram.eu/lobby/en/IslandParadise/games/&bankingUrl=https://mobile-app1-gtp73.installprogram.eu/lobby/en/IslandParadise&routerEndPoints=&currencyFormat=&isPracticePlay=False&username=Zen_zn3238g&password=test1234$&host=mobile&site=Bluemesa");
				Thread.sleep(10000);
				
				// waitNew.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"continueButton\"]/span")));

				// *[@id='continueButton']/span

				// btnBetClickContainer
				
				//
				
				//webdriver.rotate(ScreenOrientation.LANDSCAPE);
				/*MobileElement ele = (MobileElement) webdriver.findElement(By.xpath("//*[@id='btnMenu']"));
      			//WebElement ele =  webdriver.findElement(By.xpath(xpathMap.get(locator)));
      			webdriver.executeScript("arguments[0].click();", ele);
      			ele.click();
      			Thread.sleep(3000);
      	WebElement ele1 =  webdriver.findElement(By.xpath("//*[@id='btnMenu']"));
    	ele1.click();*/
				
				
				waitNew.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='btnAutoplay']")));
      			WebElement autoplayEle = webdriver.findElement(By.xpath("//*[@id='btnAutoplay']"));
      			autoplayEle.click();
      			autoplayEle.click();
      			
				/*boolean autoplay = cfnlib.isAutoplayAvailable();
				if (autoplay)
				{
					System.out.println("Autoplay : ppASS");
					log.debug("Autoplay : PfASS");
					cfnlib.closeOverlay();
				} 
				else 
				{
					System.out.println("Autoplay : FAIL");
					log.debug("Autoplay : FAIL");
				}
				*/
				

      			
      			waitNew.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='btnBetClickContainer']")));
      			WebElement betEle = webdriver.findElement(By.xpath("//div[@id='btnBetClickContainer']"));
      			betEle.click();
      			betEle.click();
      	
      			
				waitNew.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='btnMenu']")));
				WebElement menuEle = webdriver.findElement(By.xpath("//*[@id='btnMenu']"));
				menuEle.click();
				
				
				waitNew.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='navigationPaytables']")));
				WebElement paytableEle = webdriver.findElement(By.xpath("//*[@id='navigationPaytables']"));
				paytableEle.click();
				
				waitNew.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='backButton']")));
				WebElement paytableBackEle = webdriver.findElement(By.xpath("//*[@id='backButton']"));
				paytableBackEle.click();
      			
      			
      			waitNew.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='btnMenu']")));
      			MobileElement ele = (MobileElement) webdriver.findElement(By.xpath("//*[@id='btnMenu']"));
				webdriver.executeScript("arguments[0].click();", ele);
      			ele.click();
      			
      			
      			boolean autoplay1 = cfnlib.isAutoplayAvailable();
   			 if(autoplay1)
   			{
   				 System.out.println("Autoplay : PASS");log.debug("Autoplay : PASS"); 
   				 cfnlib.closeOverlay();
   			}
   			else
   			{
   				System.out.println("Autoplay : FAIL");log.debug("Autoplay : FAIL");
   			       
   			}Thread.sleep(2000);
   			
   							
      			
				
				
				
				
				
				
				
				
				
				
				
				
			/*	
				clickWithWebElementAndroid(webdriver, menuEle, 0);
				
				clickWithWebElementAndroid(webdriver, menuEle, 0);


				waitNew.until(ExpectedConditions.elementToBeClickable(By.id("btnSpin")));

				WebElement spiEle = webdriver.findElement(By.id("btnSpin"));
				//clickWithWebElement(webdriver, spiEle, 0);
				spiEle.click();
				
				//clickWithWebElement(webdriver, spiEle, 0);
				clickWithWebElementAndroid(webdriver, spiEle, 0);

				waitNew.until(ExpectedConditions.elementToBeClickable(By.id("btnBetClickContainer")));

				WebElement element = webdriver.findElement(By.id("btnBetClickContainer"));
				element.click();
				Thread.sleep(5000);
				//clickWithWebElement(webdriver, element, 0);
				clickWithWebElementAndroid(webdriver, element, 0);*/

				/*
				 * webdriver.findElement(By.xpath("//*[@id=\"continueButton\"]/span")).click();
				 * 
				 * webdriver.findElement(By.xpath("//*[@id=\"continueButton\"]/span")).click();
				 * //webdriver.findElement(By.xpath("//*[@id=\"preloaderBase\"]")).click();
				 * Thread.sleep(5000);
				 * 
				 * 
				 * boolean isBRVisible = webdriver.findElement(By.xpath(
				 * "//*[@class=\"titan-simple-notification__container\"]/div")).isDisplayed();
				 * if (isBRVisible) {
				 * //Native_ClickByXpath(xpathMap.get("ClickOnCloseBonusReminder"));
				 * System.out.println("Bonus Reminder is present after refresh");
				 * webdriver.findElement(By.xpath(
				 * "//*[@class=\"titan-simple-notification__close-button-container\"]")).click()
				 * ; webdriver.findElement(By.xpath(
				 * "//*[@class=\"titan-simple-notification__container\"]/div/div/header/div[2]")
				 * ).click();
				 * 
				 * WebElement webElement=webdriver.findElement(By.xpath(
				 * "//*[@class=\"titan-simple-notification__close-button-container\"]"));
				 * 
				 * clickWithWebElement(webdriver,webElement,0); System.out.println(); }
				 */
				// webdriver.findElement(By.xpath("//*[@id='continueButton']/span")).click();
				// waitNew.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='titan-full-screen-overlay
				// titan__titan-full-screen-overlay']")));
				/*
				 * //*[@id="continueButton"]/span Wait<WebDriver> wait1 = new
				 * FluentWait<WebDriver>(webdriver) .withTimeout(Duration.ofSeconds(20))
				 * .pollingEvery(Duration.ofSeconds(5)); WebElement foo = wait1.until(new
				 * Function<WebDriver, WebElement>() { public WebElement apply(WebDriver driver)
				 * { return driver.findElement(By.
				 * xpath("//div[@class='titan-full-screen-overlay titan__titan-full-screen-overlay']"
				 * )); } });
				 * 
				 * System.out.println(foo);
				 */
				// System.out.println(webdriver.getContext());
				// System.out.println(webdriver.findElements(By.xpath("//div[@class='titan-full-screen-overlay
				// titan__titan-full-screen-overlay']")).size());

				// System.out.println(webdriver.findElement(By.xpath("//div[@class='titan-full-screen-overlay
				// titan__titan-full-screen-overlay']")).isDisplayed());
				// wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"continueButton\"]/span")));

				// wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"preloaderBase456\"]")));
				// webdriver.findElement(By.id("continueButton")).click();

				/*
				 * String context = webdriver.getContext(); webdriver.context("NATIVE_APP");
				 * File scrFile = webdriver.getScreenshotAs(OutputType.FILE); String
				 * immortalEncodedString = getImageAsEncodedString("D:\\Test\\Spin.png");
				 * WebElement webElement=webdriver.findElementByImage(immortalEncodedString);
				 * System.out.println("Coordinates :  "+webElement.getRect().getX() +
				 * ", "+webElement.getRect().getY()); Thread.sleep(5000);
				 * webdriver.context(context);
				 */
			}

			// -------------------Handling the exception---------------------//
		} catch (Exception e) {
			// webdriver.findElement(By.id("continueButton")).click();
			e.printStackTrace();
			log.error(e.getMessage(), e);
		} finally {
			webdriver.close();
			webdriver.quit();
		}
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
/*	public boolean isAutoplayAvailable() 
	{	
		boolean isAutoplayAvailable = false;
		try
		{
			NativeClickByXpath_CS("Autoplay");
			//NativeClickByID_CS("AutoplayID","Autoplay_LandscapeMode");
			isAutoplayAvailable= true;
		
		}
		catch (Exception e) 
		{
			log.error(e.getMessage(), e);
		}
		return isAutoplayAvailable;
		
	}
	public boolean NativeClickByXpath_CS(String locator ) 
	{
		boolean present = false;
		try 
		{
			AppiumDriver<WebElement> webdriver = scriptParameters.getAppiumWebdriver();
      			Wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='btnAutoplay']")));
      			MobileElement ele = (MobileElement) webdriver.findElement(By.xpath(//*[@id="btnAutoplay"]));
      			//WebElement ele =  webdriver.findElement(By.xpath(xpathMap.get(locator)));
      			webdriver.executeScript("arguments[0].click();", ele);
      			ele.click();
      			Thread.sleep(3000);
      	WebElement ele1 =  webdriver.findElement(By.xpath(xpathMap.get(locator)));
    	ele1.click();
      			//clickWithWebElement(ele,0);
      			int elementWidth = ele.getSize().getWidth();System.out.println("Element width: "+ elementWidth);int elementHeight = element.getSize().getHeight();System.out.println("Element width: "+ elementHeight);
      			Actions actions = new Actions(webdriver);
      			actions.moveToElement(webdriver.findElement(By.tagName("body")), 0, 0);
      			actions.moveByOffset(xCoordinate, yCoordinate).click().build().perform();
                System.out.println("Element Found and Clicked");
				log.debug("Element Found and Clicked");
				
		
				present = true;
			//webdriver.context(context);
		} 
		catch (NoSuchElementException | InterruptedException e) 
		{
			System.out.println(e.getMessage());
			System.out.println("Unable to click");
			log.debug("Unable to click");
			present = false;
		}
		return present;
	}
*/
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

}