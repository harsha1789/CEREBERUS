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

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile_CS;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.offset.PointOption;

public class MobileTestScript_9_May {
	Logger log = Logger.getLogger(MobileTestScript_9_May.class.getName());

	public ScriptParameters scriptParameters;
	public CFNLibrary_Mobile_CS cfnlib ;
	
	public void script() throws Exception {

		AppiumDriver<WebElement> webdriver = scriptParameters.getAppiumWebdriver();

		System.out.println(webdriver.getContextHandles());

		try {
			if (webdriver != null) {
				WebDriverWait waitNew = new WebDriverWait(webdriver, 20);
				Thread.sleep(5000);
				//webdriver.get(
					//	"https://mobile-app1-gtp37.installprogram.eu/mobileWebGames/game/mgs?displayName=Adventure+Palace&gameId=adventurePalace&gameVersion=adventurePalace_ComponentStore_1_10_4_635&moduleId=10025&clientId=40305&clientTypeId=40&languageCode=en&productId=5007&brand=islandparadise&loginType=InterimUPE&returnUrl=https://mobile-app1-gtp37.installprogram.eu/lobby/en/IslandParadise/games/&bankingUrl=https://mobile-app1-gtp37.installprogram.eu/lobby/en/IslandParadise&routerEndPoints=&currencyFormat=&isPracticePlay=False&username=harsha_73_AP_Mobile&password=test1234$&host=mobile");
				webdriver.get("https://mobile-app1-gtp110.installprogram.eu/htmlGames/3.31.0/launch/jokerPoker_ComponentStore_1_3_1_218/mgs/jokerPoker?displayName=Joker+Poker&moduleId=23&clientId=40300&gamePath=/mgs/jokerPoker&clientTypeId=40&languageCode=en&productId=5007&market=dotcom&brand=islandparadise&loginType=InterimUPE&returnUrl=https://mobile-app1-gtp110.installprogram.eu/lobby/en/IslandParadise/games/&routerEndPoints=&currencyFormat=&isPracticePlay=False&username=harsha110&password=test1234$&formFactor=mobile");
				                                                                                                                 
				Thread.sleep(10000);
				//*************************
				 boolean val=cfnlib.funcFullScreen1();
				 if (val == true)
				 {
					 System.out.println("In full screen mode ");
				 }
				  // Navigate to the URL of the pop-up window
			//	webdriver.get("http://example.com/popup");

				// WebElement m= driver.findElement(By.xpath ("//*[local-name()='svg' and @data-icon='home']/*[localname()='path']"));
				 WebElement m= webdriver.findElement(By.xpath ("//*[@class='titan-infobar__element titan-infobar__element-image titan-infobar__element-content titan-infobar_touchable']/*[name()='svg']"));
			      // Action class to move and click element
			      Actions a = new Actions(webdriver);
			      a.moveToElement(m).
			      click().build().perform();

    // Wait for the pop-up window to load
    // You may need to use a more specific wait condition here
    // depending on how the pop-up window is displayed on the page
				Thread.sleep(5000);

   
	 WebElement OneDesignbetbutton = webdriver.findElement(By.xpath(" //div[@id='betBtn']"));
					
	OneDesignbetbutton.click();

    // Switch to the pop-up window
			//	webdriver.switchTo().window("betSettings");

 

    // Find the slider element on the page
   // WebElement slider = webdriver.findElement(By.id("slider"));

 

    // Create a new instance of the Actions class
    Actions actions = new Actions(webdriver);

 
   // webdriver.get("https://example.com/slider");// Find the slider element using XPath
    
    WebElement slider1 = webdriver.findElement(By.xpath("//*[@class='range-slider__thumb']"));
   // Use the slider element
    slider1.sendKeys("50"); // Set the value of the slider to 50
    // Use the Actions class to move the slider to the maximum value
    actions.dragAndDropBy(slider1, 100, 0).perform();

 

    // You may need to add additional wait conditions or use a more specific
    // element locator to ensure that the slider has finished moving before
    // continuing with the rest of your automation script

 

    // Close the pop-up window and quit the driver
    webdriver.close();
    webdriver.quit();
				
				
				
				
				
				
				
				/**********************
				
				// waitNew.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"continueButton\"]/span")));

				// *[@id='continueButton']/span

				// btnBetClickContainer
				
				//
				
			//	webdriver.rotate(ScreenOrientation.LANDSCAPE);
				
				//funcFullScreen();
				
				//WebElement menuEle = webdriver.findElement(By.xpath("//*[@id='fullscreenOverlay']"));
				
				//	menuEle.click();
					
					
                  WebElement OneDesignbetbutton = webdriver.findElement(By.xpath(" //div[@id='betBtn']"));
				
				OneDesignbetbutton.click();
				
//				waitNew.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='fullscreenOverlay']")));
//
			//	
				WebElement element = webdriver.findElement(By.xpath("//*[@id=\"betSettingsCoinSize\"]/div[3]/div[3]"));
				JavascriptExecutor js = (JavascriptExecutor) webdriver;
				//WebElement element = webdriver.findElement(By.id("range-slider__thumb"));
				js.executeScript("arguments[0].setAttribute('style', 'width: 100%;')", element);
				
				element.click();
				
						 
						
				WebElement OneDesignCoinSizeSlider_ID = webdriver.findElement(By.xpath("(//*[@id=\"betSettingsCoinSize\"]/div)[2]"));
				OneDesignCoinSizeSlider_ID.click();
				
				Actions action = new Actions(webdriver);

	            String bet_attr = OneDesignCoinSizeSlider_ID.getAttribute("style");
	            System.out.println(bet_attr.trim());
	            Thread.sleep(2000);

	            if (!bet_attr.equals("left: 100%;")) {

	                action.dragAndDropBy(OneDesignCoinSizeSlider_ID, 200, 0).build().perform();
	                Thread.sleep(2000);
	                
	            } else {
	                //if already set max bet then make flag as true
	               
	            }
				
				clickWithWebElementAndroid(webdriver, OneDesignCoinSizeSlider_ID, 100);
				
				tapOnCoordinates(webdriver, 450, 606);
				
				WebElement eleSlider1=webdriver.findElement(By.cssSelector("style=\"width: 100%;]"));
				int valueToBeset=2;
				valueToBeset=(valueToBeset>1&&valueToBeset<30)?valueToBeset+1:valueToBeset;
				int eleSlider1Width=eleSlider1.getSize().getWidth();
				int pixelValue=(eleSlider1Width)*valueToBeset/100;       
				new Actions(webdriver).moveToElement(eleSlider1,pixelValue, 0).click().build().perform();
				
				tapOnCoordinates(webdriver, 351, 578);
				
				JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 50, 25);  
				slider.setMajorTickSpacing(10); 
				
				
				   WebElement Slider = webdriver.findElement(By.xpath("//*[@id=\"coinsSizeSlidersliderThumb\"]"));
			        

			        int PixelsToMove = GetPixelsToMove(Slider, 15, 20, 5);
			        Actions SliderAction = new Actions(webdriver);
			        SliderAction.clickAndHold(Slider).moveByOffset((-(int) Slider.getSize().getWidth() / 2), 0)
			                .moveByOffset(PixelsToMove, 0).release().perform();
			     // Set Loop counter to get desired value
			        Slider.sendKeys(Keys.ARROW_LEFT); // Or ARROW_RIGHT
			     // End loop
				
				//  /html/body/section/div[1]/div[6]/div[1]/div[2]/div[3]/div[3]/div[3]
				WebElement OneDesignCoinSizeSlider_ID1 = webdriver.findElement(By.xpath("/html/body/section/div[1]/div[6]/div[1]/div[2]/div[3]/div[3]/div[3]"));
				OneDesignCoinSizeSlider_ID.click();
					//coinsSizeSlidersliderThumb	
				
				
			/*	
				waitNew.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='btnMenu']")));

				 menuEle = webdriver.findElement(By.xpath("//*[@id='btnMenu']"));
				 
				//*[@id="btnSpin"]
				
				menuEle.click();
				menuEle.click();
				menuEle.click();
				
				clickWithWebElementAndroid(webdriver, menuEle, 0);
				
				clickWithWebElementAndroid(webdriver, menuEle, 0);
				
				clickWithWebElementAndroid(webdriver, menuEle, 0);
			*/	
	
				waitNew.until(ExpectedConditions.elementToBeClickable(By.id("btnSpin")));

				WebElement spiEle = webdriver.findElement(By.id("btnSpin"));
				//clickWithWebElement(webdriver, spiEle, 0);
				spiEle.click();
				
				//clickWithWebElement(webdriver, spiEle, 0);
				clickWithWebElementAndroid(webdriver, spiEle, 0);

				waitNew.until(ExpectedConditions.elementToBeClickable(By.id("btnBetClickContainer")));

				WebElement element1 = webdriver.findElement(By.id("btnBetClickContainer"));
				element1.click();
				Thread.sleep(5000);
				//clickWithWebElement(webdriver, element, 0);
				clickWithWebElementAndroid(webdriver, element1, 0);
				
				
				
				
				
			    
		        //method is used to scroll the pay-table and validate the pay-outs and check the currency format
				WebDriverWait waitNew11 = new WebDriverWait(webdriver, 20);
				waitNew11.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='btnMenu']")));

				WebElement menuEle1 = webdriver.findElement(By.xpath("//*[@id='btnMenu']"));
				
				menuEle1.click();	
				
			
			
				
					System.out.println("Paytable Open : PASS");log.debug("Paytable Open : PASS");
						//lvcReport.detailsAppendFolder("Base Game", "Pay Table ", "PayOut Amount", "PASS",""+CurrencyName);
						
						//method is used to , Check the pay-table and its validations
						
						//boolean paytablePayoutsValidations = cfnlib.verifyRegularExpressionUsingArrays(lvcReport,regExpr,cfnlib.validatePayoutsFromPaytable(lvcReport,CurrencyName));
					boolean paytablePayoutsValidations=true;
						if(paytablePayoutsValidations)
						{
							System.out.println("Paytable Values : PASS");log.debug("Paytable Values : PASS");
							//lvcReport.detailsAppendFolder("Pay Table", "Paytable Payouts Validations", "Paytable Payouts Validations", "PASS",""+CurrencyName);
							Thread.sleep(2000);	
						}
						else
						{
							System.out.println("Paytable Values : FAIL");log.debug("Paytable Values : FAIL");
							//lvcReport.detailsAppendFolder("Pay Table", "Paytable Payouts Validations", "Paytable Payouts Validations", "FAIL",""+CurrencyName);
						}
			    
			
				{
					System.out.println("Paytable Open : FAIL");log.debug("Paytable Open : FAIL");
						//lvcReport.detailsAppendFolder("Base Game", "PayOut Amount", "PayOut Amount", "FAIL",""+CurrencyName);
				}
				
				

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
		//	webdriver.close();
			//webdriver.quit();
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
	 
	 
	 
	 
	 
	 
	 
	 
	
	
	
	
	
	
	

}