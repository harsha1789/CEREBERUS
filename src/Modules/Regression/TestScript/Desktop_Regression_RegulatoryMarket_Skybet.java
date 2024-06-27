package Modules.Regression.TestScript;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop_CS;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop_CS_Renovate;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop_Force;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 * This script verifies the Skybet check points.
 * @author Premlata
 *
 */
public class Desktop_Regression_RegulatoryMarket_Skybet {

	
	Logger log = Logger.getLogger(Desktop_Regression_RegulatoryMarket_Skybet.class.getName());
	public ScriptParameters scriptParameters;
	
	public void script() throws Exception{
		String mstrTC_Name=scriptParameters.getMstrTCName();
		String mstrTC_Desc=scriptParameters.getMstrTCDesc();
		String mstrModuleName=scriptParameters.getMstrModuleName();
		WebDriver webdriver=scriptParameters.getDriver();
		BrowserMobProxyServer proxy=scriptParameters.getProxy();
		String startTime=scriptParameters.getStartTime();
		String filePath=scriptParameters.getFilePath();
		String Browsername=scriptParameters.getBrowserName();
		String Framework=scriptParameters.getFramework();
		String GameName=scriptParameters.getGameName();
	
		String Status=null;
		int mintDetailCount=0;
		//int mintSubStepNo=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		Double betTopbar=0.0;
		String balanceTopbar=null;;
		String applicationName = GameName;
		
		String classname = this.getClass().getSimpleName();
		String xmlFilePath= System.getProperty("user.dir")+"\\src\\Modules\\Regression\\TestData\\GameData\\"+classname+".testdata";

		Desktop_HTML_Report Tc10 = new Desktop_HTML_Report(webdriver, Browsername, filePath, startTime, mstrTC_Name,mstrTC_Desc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings, mintStepNo, Status,GameName);
		CFNLibrary_Desktop cfnlib = null;

		if (Framework.equalsIgnoreCase("PN")) {
			cfnlib = new CFNLibrary_Desktop(webdriver, proxy, Tc10,GameName);
		} 
		else if(Framework.equalsIgnoreCase("Force")){
			cfnlib=new CFNLibrary_Desktop_Force(webdriver,proxy,Tc10,GameName);
		}
		else if(Framework.equalsIgnoreCase("CS_Renovate")){
			cfnlib=new CFNLibrary_Desktop_CS_Renovate(webdriver,proxy,Tc10,GameName);
		}else {
			cfnlib = new CFNLibrary_Desktop_CS(webdriver, proxy, Tc10, GameName);
		}
		

		try {
			
			//-------------------Getting webdriver of proper test site---------------------//
			// Step 1 open lobby and verify lucky twins name
			Tc10.detailsAppend("Verify BaseScene features", "Base Scene Features should be displayed", "", "");
			if(webdriver!=null)
			{		BigDecimal totalWinTopbar=null;
				String Skybet_DirectURL=cfnlib.XpathMap.get("Skybet_DirectURL");
				cfnlib.func_navigate_DirectURL(Skybet_DirectURL);
				//Navigate to the settings page
				cfnlib.settingsOpen();
				// Checking quick spin button is present on the screen or not with the help of Image comparison.
				boolean quickspin=cfnlib.verifyQuickSpin();   
				if(!quickspin)
				{
					Tc10.detailsAppend("Verify the Quick Spin option is not available in Skybet Regulatory Markets.  ", "Quick Spin Option should not display.", "Quick Spin Option is not gettting displayed in"+applicationName+"", "pass");
				}
				else
				{
					Tc10.detailsAppend("Verify the Quick Spin option is not available in Skybet Regulatory Markets.", "Quick Spin Option should not display.", " Quick Spin Option is gettting displayed in"+applicationName+" ", "fail");
				}
				cfnlib.settingsBack();
			//	Verify the stop is not available in Skybet.
				boolean verify_Stop=cfnlib.isStopButtonAvailable();
				if(!verify_Stop)
				{
					Tc10.detailsAppend("Verify the stop is not available in Skybet Regulatory Markets.", "stop button should not display . ", " stop button is not gettting displayed."+" ", "pass");
				}
				else
				{
					Tc10.detailsAppend("Verify the stop is not available in Skybet Regulatory Markets.", "stop button should not display . ", " stop button is not  gettting displayed."+" ", "fail");
				}
			
				Thread.sleep(10000);
				String win1 = cfnlib.GetWinAmt();
				DecimalFormat format = new DecimalFormat();
		        format.setParseBigDecimal(true);
		        BigDecimal win = (BigDecimal) format.parse(win1);
		        		        
				System.out.println("win "+win);
				double betValue=cfnlib.GetBetAmt();
				System.out.println("betvalue "+betValue);
				String credits1 = cfnlib.verifyCreditsValue();

				String topbarData=cfnlib.verifyToggleTopbar();
				if(topbarData!=null){
					System.out.println("inside topbar if control");
					Tc10.detailsAppend("Click on the toggle topbar","Topbar should open","Topbar opened successfully", "Pass");
					String stake = topbarData.split("!")[0];
					System.out.println("stakeTopbar="+stake);
					String paid=topbarData.split("!")[1];
					System.out.println("paidTopbar="+paid);
					String balanceTopbr=topbarData.split("!")[2];
					System.out.println("balanceTopbr="+balanceTopbr);

					//Deleting all the unwanted characters from the string
					try {
						String balanceTopbar1 = cfnlib.func_String_Operation(balanceTopbr);
						balanceTopbar = balanceTopbar1.replace(",", "");
						String winTopbar = cfnlib.func_String_Operation(paid);
						totalWinTopbar = (BigDecimal) format.parse(winTopbar);
						 betTopbar = Double.parseDouble(stake.replaceAll("[$,]", ""));
						System.out.println(balanceTopbar);
					}catch(Exception e) {
						System.out.println(e);
					}

					System.out.println("in topbar : " + betTopbar + "\t" + totalWinTopbar + "\t" + balanceTopbar);
				}
				else
				{
					Tc10.detailsAppend("Click on the toggle topbar","Topbar should open","Cannot open the topbar", "fail");
				}
			
				//Comparing the data of information bar and the topbar. They should be in sync.
				//Comparing credit balance
			Tc10.detailsAppend("Compare and ensure that the values in the topbar and in the information bar are in sync","Topbar and information bars data should be in sync", "", "");
				System.out.println("The balance before com : " + balanceTopbar + "\t" + credits1);

				if(balanceTopbar.equalsIgnoreCase(credits1))
				{
					Tc10.detailsAppend("Verify the Players Balance inside the Top Bar","The balance should be displayed in the topbar and it should be sync with the info bar balance","Credit Balance at both the places is same", "Pass");
					System.out.println("Credit Matches 'if' ");
				}
				else
				{
					Tc10.detailsAppend("Verify the Players Balance inside the Top Bar","The balance should be displayed in the topbar and it should be in sync with the info bar balance","Credit Balance at the both the places is not the same", "Fail");
					System.out.println("Credit Matches 'else' ");
				}
				//Comparing the bet amount

				if(betTopbar==betValue)
				{
					Tc10.detailsAppend("Verify the Players bet inside the Top Bar","The games default bet should be displayed in the topbar","Bet displayed in the topbar is correct", "Pass");
					System.out.println("inside betTopbar 'if' ");
				}
				else
				{
					Tc10.detailsAppend("Verify the Players bet inside the Top Bar","The games default bet should be displayed in the topbar","Bet displayed in the topbar is incorrect", "Fail");
					System.out.println("inside betTopbar 'else' ");
				}
				//Comparing winning amount
				if(win.compareTo(totalWinTopbar) == 0)
				{
					Tc10.detailsAppend("Verify the players win amount displayed inside the topbar ","The win amount should be displayed in the topbar and it should be in sync with the info bar win amount","Win amount at the both the places is same", "Pass");
					System.out.println("inside Winning amount 'if' ");
				}
				else
				{
					Tc10.detailsAppend("Verify the players win amount displayed inside the topbar ","The win amount should be displayed in the topbar and it should be in sync with the info bar win amount","Win amount at the both the places is not same", "Fail");
					System.out.println("inside Winning amount 'else' ");
				}

				//close top bar
				webdriver.findElement(By.id(cfnlib.XpathMap.get("Toggle_TopBar_ID"))).click();
				
			
				//	}//class
				//}//for loop			    
			}//if loop
		}//TRY
		//-------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error(e.getMessage(),e);
			cfnlib.evalException(e);
		}
		// -------------------Closing the connections---------------//
		finally {
			Tc10.endReport();
			webdriver.close();
			webdriver.quit();
			proxy.abort();
			Thread.sleep(1000);
		}
	}
}
