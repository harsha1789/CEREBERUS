package Modules.Regression.FunctionLibrary;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.zensar.automation.framework.report.Mobile_HTML_Report;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
//import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.offset.PointOption;
import net.lightbody.bmp.BrowserMobProxyServer;
public class CFNLibrary_Mobile_CS_Renovate extends CFNLibrary_Mobile_CS {

	public CFNLibrary_Mobile_CS_Renovate(AppiumDriver<WebElement> webdriver, BrowserMobProxyServer proxy,
			Mobile_HTML_Report tc06, String gameName) throws IOException {
		super(webdriver, proxy, tc06, gameName);

		this.webdriver=webdriver;
		this.proxy=proxy;
		repo1= tc06;
		//webdriver.manage().timeouts().implicitlyWait(5000, TimeUnit.SECONDS);
		Wait = new WebDriverWait(webdriver, 5000);
		this.GameName=gameName;
		
	}
	/**
	 * *Author:Havish Jain
	 * This method is used to click on Bet button in Project renovate
	 * @throws InterruptedException 
	 */


	public boolean openTotalBet()
	{
	
			Wait.until(ExpectedConditions.elementToBeClickable(By.id(xpathMap.get("betTextID"))));
			log.debug("Clicked on TotalBet button");
			return true;

	}


	//To close bet page	
	public void closeTotalBet() throws InterruptedException
	{	
		openTotalBet();	
		log.debug("Closed the total bet overlay");	
	}

	/*
	 * Date: 29/05/2018
	 * Author:Havish Jain  
	 * Description: To Open menu container  
	 * Parameter: NA
	 */	
	public boolean menuOpen(){

		return false;
	}
	public void waitBonus()
	{
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("M_Bonus"))));
	}
	/*
	 * Date: 29/05/2018
	 * Author:Havish Jain  
	 * Description: To Close menu container  
	 * Parameter: NA
	 */	
	public boolean menuClose()
	{

		return false;

	}

	/**
	 * Date:07/12/2017
	 * Author:Premlata Mishra
	 * This method is used to open the settings
	 * @return true
	 * @throws InterruptedException 
	 */
	public boolean settingsOpen() throws InterruptedException
	{	

		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("OneDesign_SettingsID"))));
			//Native_Click(XpathMap.get("OneDesign_Settings"));
			nativeClickByID(xpathMap.get("OneDesign_SettingsID"));
			log.debug("Clicked on settigns button");
		}
		catch (Exception e) {
			log.error("error in settingsOpen()", e);
		}

		return true;
	}

	public boolean funcFullScreen(){
		return false;
	}


	/**
	 * Date:07/12/2017
	 * Author:Laxmikanth Kodam
	 * This method is actually not necessery in component store just declaration needed 
	 * @return true
	 */
	public boolean settingsBack()
	{

		try {
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("OneDesign_SettingsHeaderID"))));
			nativeClickByID(xpathMap.get("OneDesign_SettingsID"));		
			log.debug("Clicked on settigns back button to close settigns ");
			Thread.sleep(1000);					
		}

		catch(Exception e)
		{
			log.error("error in settignsBack()", e);

		}
		return true;
	}

	/**
	 * Date:05-06-2018
	 * Name:Havish Jain
	 * Description: this function is used to Scroll the page vertically and to take the screenshot
	 * @throws Exception 
	 */
	public boolean func_SwipeDown(){
		try {
			webdriver.context("NATIVE_APP"); 
			Dimension size1 = webdriver.manage().window().getSize(); 
			int startx = size1.width / 2;
			int starty = (int) (size1.height * 0.265); 
			int endy = (int) (size1.height* 0.20);
			TouchAction action = new TouchAction(webdriver);
			Point p=new Point(startx, starty);
			//action.ppress(startx, starty).moveTo(startx, endy).release().perform();
			io.appium.java_client.TouchAction action1 = new TouchAction(webdriver);
			action.press(PointOption.point(startx, starty)).moveTo(PointOption.point(startx, endy)).release().perform();
			//new TouchAction(webdriver).press(point(startx, starty));
			webdriver.context("CHROMIUM");
			Thread.sleep(500);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			return false;
		}
		return true;

	} 

	/**
	 * Date:10-1-2018
	 * Name:Havish Jain
	 * Description: this function is used to Scroll the page using cordinates and to take the screenshot
	 * @throws Exception 
	 */

	public void capturePaytableScreenshot(Mobile_HTML_Report report, String language) 
	{
		try
		{
			//Clicking on paytable link
			webdriver.context("NATIVE_APP");
			MobileElement el5 = (MobileElement) webdriver.findElement(By.id(xpathMap.get("OneDesign_PaytableID")));
			el5.click();
			log.debug("Clicked on paytable buttons");
			webdriver.context("CHROMIUM");
			// Thread.sleep(3000);
			report.detailsAppend("verify the paytable screen shot", " paytable first page screen shot", "paytable first page screenshot ", "pass");

			for(int i=1; i<=5; i++){
				func_SwipeDown();
				Thread.sleep(2000);
				report.detailsAppend("verify the paytable screen shot", " paytable next screen shot", "paytable next page screenshot ", "pass");
			}

			webdriver.context("NATIVE_APP"); 
			Dimension size1 = webdriver.manage().window().getSize(); 
			int startx = size1.width / 2;
			int starty = (int) (size1.height * 0.70); 
			int endy = (int) (size1.height* 0.20);
			/*TouchAction action = new TouchAction(webdriver);
                action.press(startx, starty).moveTo(startx, endy).release().perform();*/
			io.appium.java_client.TouchAction action1 = new TouchAction(webdriver);
			action1.press(PointOption.point(startx, starty)).moveTo(PointOption.point(startx, endy)).release().perform();
			webdriver.context("CHROMIUM");
			Thread.sleep(1000);
			report.detailsAppend("verify the paytable screen shot", " paytable next screen shot", "paytable next page screenshot ", "pass");

			nativeClickByID(xpathMap.get("PaytableBackID"));
			Thread.sleep(1000);
		}
		catch (Exception e) {
			log.error("error in paytableOpenScroll()",e);
		}
	}

	/**
	 * This method is used to stop is avalable or not
	 * Author: Premlata Mishra
	 * @return true
	 */
	public boolean verifyStop(String imagepath)
	{
		boolean test=false;
		try{
			webdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			nativeClickByID(xpathMap.get("Spin_Button_ID"));
			test=webdriver.findElement(By.xpath(xpathMap.get("Stop_Text"))).isDisplayed();
			if(test)
			{
				return test =true;
			}
		}
		catch(Exception e)
		{
			return test=false;
		}
		return test;
	}

	public void FS_continue()
	{
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpathMap.get("ContinueFSID"))));	 
		nativeClickByID(xpathMap.get("ContinueFSID"));
	}
}
