package Modules.Regression.FunctionLibrary;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.zensar.automation.framework.report.Desktop_HTML_Report;

import net.lightbody.bmp.BrowserMobProxyServer;

public class CFNLibrary_Desktop_CS_Renovate extends CFNLibrary_Desktop_CS {
	
	public Logger log = Logger.getLogger(CFNLibrary_Desktop_CS_Renovate.class.getName());

	public CFNLibrary_Desktop_CS_Renovate(WebDriver webdriver, BrowserMobProxyServer browserproxy1, Desktop_HTML_Report report, String gameName)
			throws IOException {
		super(webdriver, browserproxy1, report,gameName);
		this.GameName=gameName.trim();
		
	}

	/**
	 * *Author:Havish Jain
	 * This method is used to click on Bet button in Project renovate
	 * @throws InterruptedException 
	 */
	public boolean open_TotalBet() {
		boolean b = false;
		try{
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("clock"))));
			Wait.until(ExpectedConditions.elementToBeClickable(By.id(XpathMap.get("betTextID"))));
			webdriver.findElement(By.id(XpathMap.get("betTextID"))).click();
			log.debug("clicked on total bet button");
			Thread.sleep(1000);
			b = true;
		}
		catch(Exception e)
		{
			log.error("error in open_TotalBet method", e);
		}
		return b;		
	}

	//To close bet page	
	public void close_TotalBet() throws InterruptedException
	{	
		open_TotalBet();
		log.debug("Clicked to close totalBet overlay");
	}
	public void wait_Bonus()
	{
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("Bonus"))));
	}

	/*	*//**
	 * This method is used to stop is available or not
	 * Author: Premlata Mishra
	 * @return true
	 *//*
		public boolean VerifyStop(String imagepath)
		{
			boolean test=false;
			try{
				func_Click(XpathMap.get("Spin_Button"));
				Thread.sleep(1000);
				test=webdriver.findElement(By.xpath(XpathMap.get("Stop_Text"))).isDisplayed();
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
	  */
	/*
	 * Date: 29/05/2018
	 * Author:Havish Jain  
	 * Description: To Open menu container  
	 * Parameter: NA
	 */	
	public boolean menuOpen(){

		return false;
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


		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("OneDesign_Settings"))));
		//Clicking on settings link		
		func_Click(XpathMap.get("OneDesign_Settings"));
		log.debug("Clicked on settings button to open");
		return true;

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
			Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathMap.get("Renovate_SettingsBack"))));
			webdriver.findElement(By.xpath(XpathMap.get("Renovate_SettingsBack"))).click();
			log.debug("Clicked on screen to close settings overlay");
			Thread.sleep(1000);

		}
		catch(Exception e)
		{
			log.error("error in closing settings",e);

		}
		return true;
	}
	/**
	 * Date:10-1-2018
	 * Name:Premlata Mishra
	 * Description: This method is to verify error is coming or not 
	 * @throws Exception 
	 */
	public  boolean isElementPresent( String locator)
	{
		boolean isPresent=false;
		try
		{
			webdriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			Wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(XpathMap.get("clock"))));
			if( webdriver.findElements(By.xpath(locator)).size() > 0)
			{
				isPresent=true;
			}
		}
		catch(Exception e)
		{
			 log.error("Exception while checking element", e);
		}
		return isPresent;
	}
	/**
	 * Date:10-1-2018
	 * Name:Premlata Mishra
	 * Description: this function is used to Scroll the page and to take the screenshot
	 * @throws Exception 
	 */

	public String paytableOpen(Desktop_HTML_Report report, String classname, String languageCode) 
	{
		String paytable = "";
		try
		{
			//func_Click(XpathMap.get("OneDesign_Hamburger"));
			Wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XpathMap.get("OneDesign_Paytable"))));
			func_Click(XpathMap.get("OneDesign_Paytable"));
			log.debug("Clicked on paytable icon to open ");
			Thread.sleep(3500);
			report.detailsAppend("verify the paytable screen shot", " paytable first page screen shot", "paytable first page screenshot ", "pass");
			paytable = "paytable";
			JavascriptExecutor js = (JavascriptExecutor) webdriver;

			WebElement ele1 = webdriver.findElement(By.xpath(XpathMap.get("OneDesignpaytable1")));
			js.executeScript("arguments[0].scrollIntoView(true);",ele1);
			Thread.sleep(1000);
			report.detailsAppend("verify the paytable screen shot", " paytable Second page screen shot", "paytable Second page screenshot ", "pass");

			boolean test=webdriver.findElements(By.xpath(XpathMap.get("OneDesignpaytable2"))).size()>0;
			if(test)
			{
				WebElement ele3=webdriver.findElement(By.xpath(XpathMap.get("OneDesignpaytable2")));
				js.executeScript("arguments[0].scrollIntoView(true);", ele3);
				Thread.sleep(1000);
				report.detailsAppend("verify the paytable screen shot", " paytable Third page screen shot", "paytable Third page screenshot ", "pass");
				boolean test1=webdriver.findElements(By.xpath(XpathMap.get("OneDesignpaytable3"))).size()>0;
				if(test1){
					WebElement ele4= webdriver.findElement(By.xpath(XpathMap.get("OneDesignpaytable3")));
					js.executeScript("arguments[0].scrollIntoView(true);", ele4);
					Thread.sleep(1000);
					report.detailsAppend("verify the paytable screen shot", " paytable fourth page screen shot", "paytable fourth page screenshot ", "pass");

					/*	boolean test2=webdriver.findElements(By.xpath(XpathMap.get("OneDesignpaytable4"))).size()>0;
				if(test2)
				{
					WebElement ele5=webdriver.findElement(By.xpath(XpathMap.get("OneDesignpaytable4")));
					js.executeScript("arguments[0].scrollIntoView(true);", ele5);
					//js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
					Thread.sleep(1000);
					report.details_append("verify the paytable screen shot", " paytable Fifth page screen shot", "paytable Fifth page screenshot ", "pass");	
				}*/
				}
			}
			Thread.sleep(1000);
		}
		catch (Exception e) {
			log.error("error in opening paytable",e);
		}
		return paytable;
	}

	public void paytableClose() 
	{
		func_Click(XpathMap.get("PaytableBack"));
		log.debug("Closed the paytable page");
		Wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XpathMap.get("OneDesign_Spin_Text"))));

	}

	/*	*//**
	 * This method is used to wait till FS scene loads
	 * Author: Havish Jain
	 * @return true
	 *//*

	/*public boolean FSSceneLoading()
	{

		//wait for Free spin scene to refresh
		Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(XpathMap.get("FSRemainingValueID"))));
		return true;
	}*/

	public void func_FullScreen(){

	}

	/*	*//**
	 * This method is used to Click on Start Free Spin button in FS
	 * Author: Havish Jain
	 * @return true
	 */
	public void FS_Start() {

		webdriver.findElement(By.id(XpathMap.get("StartFSID"))).click();
		log.debug("Clicked on start button");
	}

	/*	*//**
	 * This method is used to Click on Continue Free Spin button in FS
	 * Author: Havish Jain
	 * @return true
	 */
	public void FS_continue()
	{
		webdriver.findElement(By.id(XpathMap.get("ContinueFSID"))).click();
		log.debug("Clicked on free spin continue button");
	}

	/**
	 * *Author:Havish Jain
	 * This method is used to verify currency symbol in bet setting screen 
	 * @throws InterruptedException 
	 */
	public boolean betSettingCurrencySymbol(String currency) {
		String BetAmount = webdriver.findElement(By.id(XpathMap.get("betAmount"))).getText();
		if(BetAmount.indexOf(currency)>=0)
		{
			return true;
		}
		if(currency==null || currency.equals(""))
		{
			return true;
		}
		return false;
	}
}
