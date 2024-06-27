package Modules.Regression.TestScript;
import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import com.zensar.automation.framework.report.Mobile_HTML_Report;

import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile_CS;
import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile_CS_Renovate;
import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile_Force;
import io.appium.java_client.android.AndroidDriver;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 * This script verifies the Skybet check points.
 * @author Premlata
 *
 */
public class Mobile_Regression_RegulatoryMarket_Skybet  
{
	public AndroidDriver<WebElement> webdriver;
	public BrowserMobProxyServer proxy;
	public static Logger log = Logger.getLogger(Desktop_Regression_RegulatoryMarket_Skybet.class.getName());
	public String DeviceName;
	String classname= this.getClass().getSimpleName();
	String applicationName;
	public String mstrTC_Name,mstrTC_Desc,mstrModuleName;
	public String obj=null;
	public String GameDesktopName;
	public String gametitleexcel;
	public String GameTitle;
	public int paylinesverification;
	public boolean GameHelp;
	public String GameverifyTime;
	public String path=System.getProperty("user.dir");
	public  String imagepath=path+File.separator+"src"+File.separator+"Modules"+File.separator+"Regression"+File.separator+"TestData"+File.separator+"Images"+File.separator+"BaseScene"+ File.separator;
	public int verifycredit;
	public String SpinVisible;
	public int oldbalance;
	public int newbalance;
	public String testcaseName;
	public String userName;
	public String amount;
	public double amount1;
	public String password;
	public String filePath;
	public String startTime;
	BigDecimal totalWinTopbar;
	String balanceTopbar;
	public int mintDetailCount;
	public int mintSubStepNo;
	public int mintPassed;
	public int mintFailed;
	public int mintWarnings;
	public int mintStepNo;
	public String Status;
	String stake=null;
	float totalWinFloat;
	float totalWinFloat_new;
	String paid=null;
	float totalWinTopbarFloat;
	String freebets=null;
	String balanceTopbr=null;
	String dataFromHar=null;
	String balance=null;
	String balancedemo=null;
	String loyaltyBalance=null;
	String totalWin;
	//String balanceTopbar=null;
	String loyaltyBalanceTopbar=null;
	String betTopbar=null;
	//String totalWinTopbar;
	public String deviceLockKey;
	public String clientID="40300";
	public String CreditsValue;
	public String Framework;
	public String GameName;
	//public String regulatoryMarketImagePath = Constant.path + File.separator + "Modules" + File.separator + "Regression" + File.separator + "TestData" + File.separator + "Images" + File.separator + "Regulatory_Market_feature" + File.separator;

	String xmlFilePath= System.getProperty("user.dir")+"\\src\\Modules\\Regression\\TestData\\GameData\\"+classname+".testdata";
	File src= new File(xmlFilePath);      		
	File destFile = new File("\\\\10.247.224.22\\C$\\MGS_TestData\\"+classname+".testdata");
	public void script() throws Exception{

		Mobile_HTML_Report Tc16=new Mobile_HTML_Report(webdriver,DeviceName,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,GameName);
		
		CFNLibrary_Mobile cfnlib=null; 
		if(Framework.equalsIgnoreCase("PN")){
			cfnlib=new CFNLibrary_Mobile(webdriver,proxy,Tc16,GameName);
		}
		else if(Framework.equalsIgnoreCase("Force")){
			cfnlib=new CFNLibrary_Mobile_Force(webdriver, proxy, Tc16, GameName);
		}
		else if(Framework.equalsIgnoreCase("CS_Renovate")){
			cfnlib=new CFNLibrary_Mobile_CS_Renovate(webdriver, proxy, Tc16, GameName);
		}
		else{
			cfnlib=new CFNLibrary_Mobile_CS(webdriver,proxy,Tc16,GameName);
		}

		try{


			//-------------------Getting webdriver of proper test site---------------------//
			// Step 1
			//open lobby and verify lucky twins name
			Tc16.detailsAppend("Verify BaseScene features", "Base Scene Features should be displayed", "", "");
			if(webdriver!=null)
			{		
				String Skybet_DirectURL=cfnlib.xpathMap.get("Skybet_DirectURL");
				cfnlib.funcNavigateDirectURL(Skybet_DirectURL);
				//Navigate to the settings page
				cfnlib.settingsOpen();
				// Checking quick spin button is present on the screen or not with the help of Image comparison.
				boolean quickspin=cfnlib.verifyQuickSpin(imagepath);   
				if(!quickspin)
				{
					Tc16.detailsAppend("Verify the Quick Spin option is not available in UK Regulatory Markets.  ", "Quick Spin  Option should not display .", "Quick Spin  Option is not gettting displayed in "+applicationName+"", "pass");
				}
				else
				{
					Tc16.detailsAppend("Verify the Quick Spin option is not available in UK Regulatory Markets.", "Quick Spin  Option should not display . ", " Quick Spin  Option is gettting displayed in "+applicationName+" ", "Fail");
				}
				cfnlib.settingsBack();
				
				/*Verify that the stop button  is not available in skybet Regulatory Markets.*/
				boolean verify_Stop=cfnlib.verifyStop(imagepath+"Mobile_Stop.png");
				if(!verify_Stop)
				{
					Tc16.detailsAppend("Verify the stop is not available in Skybet Regulatory Markets.", "stop button should not display . ", " stop button is not gettting displayed."+" ", "pass");
				}
				else
				{
					Tc16.detailsAppend("Verify the stop is not available in Skybet Regulatory Markets.", "stop button should not display . ", " stop button is not  gettting displayed."+" ", "fail");
				}
				Thread.sleep(10000);
				String win1 = cfnlib.getWinAmt();
				DecimalFormat format = new DecimalFormat();
		        format.setParseBigDecimal(true);
		        BigDecimal win = (BigDecimal) format.parse(win1);
		        		        
				System.out.println("win "+win);
				String betValue=cfnlib.getBetAmt();
				System.out.println("betvalue "+betValue);
				String credits = cfnlib.verifyCreditsValue();
				//BigDecimal credits = (BigDecimal) format.parse(credits1);
				System.out.println("credits "+credits);

				String topbarData=cfnlib.verifyToggleTopbar();
				if(topbarData!=null){
					System.out.println("inside topbar if control");
					Tc16.detailsAppend("Click on the toggle topbar","Topbar should open","Topbar opened successfully", "Pass");
					stake = topbarData.split("!")[0];
					System.out.println("stakeTopbar="+stake);
					paid=topbarData.split("!")[1];
					System.out.println("paidTopbar="+paid);
					balanceTopbr=topbarData.split("!")[2];
					System.out.println("balanceTopbr="+balanceTopbr);

					//Deleting all the unwanted characters from the string
					try {
						String balanceTopbar1 = cfnlib.func_String_Operation(balanceTopbr);
						balanceTopbar = balanceTopbar1.replace(",", "");
						String winTopbar = cfnlib.func_String_Operation(paid);
						totalWinTopbar = (BigDecimal) format.parse(winTopbar);
						betTopbar = stake.replaceAll("[$,]", "");
						System.out.println(balanceTopbar);
					}catch(Exception e) {
						System.out.println(e);
					}

					System.out.println("in topbar : " + betTopbar + "\t" + totalWinTopbar + "\t" + balanceTopbar);
								}
				else
				{

					Tc16.detailsAppend("Click on the toggle topbar","Topbar should open","Cannot open the topbar", "fail");
				}
			
				//Comparing the data of information bar and the topbar. They should be in sync.

				//Comparing credit balance
			Tc16.detailsAppend("Compare and ensure that the values in the topbar and in the information bar are in sync","Topbar and information bars data should be in sync", "", "");
				System.out.println("The balance before comparison : " + balanceTopbar + "\t" + credits);


				if(balanceTopbar.equalsIgnoreCase(credits))
				{
					Tc16.detailsAppend("Verify the Players Balance inside the Top Bar","The balance should be displayed in the topbar and it should be sync with the info bar balance","Credit Balance at both the places is same", "Pass");
					System.out.println("Credit Matches 'if' ");
				}
				else
				{
					Tc16.detailsAppend("Verify the Players Balance inside the Top Bar","The balance should be displayed in the topbar and it should be in sync with the info bar balance","Credit Balance at the both the places is not the same", "Fail");
					System.out.println("Credit Matches 'else' ");
				}
				//Comparing the bet amount

				if(betTopbar.equalsIgnoreCase(betValue))
				{
					Tc16.detailsAppend("Verify the Players bet inside the Top Bar","The games default bet should be displayed in the topbar","Bet displayed in the topbar is correct", "Pass");
					System.out.println("inside betTopbar 'if' ");
				}
				else
				{
					Tc16.detailsAppend("Verify the Players bet inside the Top Bar","The games default bet should be displayed in the topbar","Bet displayed in the topbar is incorrect", "Fail");
					System.out.println("inside betTopbar 'else' ");
				}
				//Comparing winning amount
				if(win.compareTo(totalWinTopbar) == 0)
				{
					Tc16.detailsAppend("Verify the players win amount displayed inside the topbar ","The win amount should be displayed in the topbar and it should be in sync with the info bar win amount","Win amount at the both the places is same", "Pass");
					System.out.println("inside Winning amount 'if' ");
				}
				else
				{
					Tc16.detailsAppend("Verify the players win amount displayed inside the topbar ","The win amount should be displayed in the topbar and it should be in sync with the info bar win amount","Win amount at the both the places is not same", "Fail");
					System.out.println("inside Winning amount 'else' ");
				}

				cfnlib.nativeClickByID(cfnlib.xpathMap.get("Toggle_TopBar_ID"));
				
			
				
				//	}//class
				//}//for loop			    
			}//if loop
		}//TRY
		//-------------------Handling the exception---------------------//
		catch (Exception e) {
			/*ExceptionHandler eHandle=new ExceptionHandler(e,webdriver,Tc16);
			eHandle.evalException();*/
			log.error(e.getMessage(),e);
			cfnlib.evalException(e);
		}
		finally
		{
			//Thread.sleep(5000);


			Tc16.endReport();
			if(destFile.delete()){
				System.out.println(destFile.getName() + " is deleted!");

			}else{
				System.out.println("Delete operation is failed.");
			}
			webdriver.close();
			webdriver.quit();
			proxy.abort();
			Thread.sleep(3000);
		}	
	}//method
}