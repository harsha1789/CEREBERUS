package Modules.Regression.TestScript;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;

import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.TestPropReader;
import org.apache.log4j.Logger;
import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import net.lightbody.bmp.BrowserMobProxyServer;
/**
 * This Script is for Low Value Currency - Base Game, Free Spins
 * ===========================================================================
 * 1.Base Game
 * ===========
 * 1.1.Credits 
 * 1.2.Bet
 * 1.3.Autoplay
 * 1.4.Bet Pannel (Min , Max & All Bet Values)
 * 1.5.PayTable , Pay-Outs Validation & Branding Validation  
 * 1.6.Win 
 * 1.7.Big Win 
 * 1.8.Bonus Game 
 * 1.9 Menu Items Validation & their navigations
 * 1.10 Top Icons Navigations
 * 1.11 Clock Validation 
 * 1.12 Refresh on Win (In Base Game , Free Spins)
 * 
 * 2.Free Spins
 * =============
 * 2.1.Credits
 * 2.2.Total Win
 * 2.3.Big Win -1st Spin
 * 2.4.Normal Win - 2nd Spin
 * 2.5.Big Win - 3rd Spin
 * 2.5.Summary Screen Total Win Validation
 *  
 *  
 * TestData 
 * ==============
 * 1.Base Game
 * 1.1 Big Win
 * 1.2 Normal Win
 * 1.3 Big Win
 * 1.4 Bonus
 * 2.Free Spins
 * 2.1 Big Win - 1st Spin 
 * 2.2 Normal Win -2nd Spin
 * 2.3 Big Win -3rd Spin
 *
 * 
 * @author rk61073
 *
 */

public class  Mobile_Regression_LVC_CS_BaseScene
{
		public ScriptParameters scriptParameters;
		Logger log = Logger.getLogger(Mobile_Regression_LVC_CS_BaseScene.class.getName());
		public void script() throws Exception 
		{
			
			String mstrTC_Name=scriptParameters.getMstrTCName();
			String mstrTC_Desc=scriptParameters.getMstrTCDesc();
			String mstrModuleName=scriptParameters.getMstrModuleName();
			BrowserMobProxyServer proxy=scriptParameters.getProxy();
			String startTime=scriptParameters.getStartTime();
			String filePath=scriptParameters.getFilePath();
			AppiumDriver<WebElement> webdriver=scriptParameters.getAppiumWebdriver();
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
			CommonUtil util = new CommonUtil();
			RestAPILibrary apiObj = new RestAPILibrary();
			cfnlib.setOsPlatform(scriptParameters.getOsPlatform()); //To get OSPlatform 
			
			List<String> copiedFiles=new ArrayList<>();
			int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
			System.out.println("MID : " +mid);log.debug("MID : " +mid);
			int cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDMobile"));
			System.out.println("CID : " +cid);log.debug("CID : " +cid);
	       
			try 
			{
				
				// Step 1 
				if(webdriver!=null)
				{	
					//Implement code for test data copy depending on the env
					String strFileName=TestPropReader.getInstance().getProperty("MobileCurrencyTestDataPath");
					File testDataFile=new File(strFileName);
					
					
					List<Map<String, String>> currencyList= util.readCurrList();// mapping
				
					
					for (Map<String, String> currencyMap:currencyList) 
					{
					userName=util.randomStringgenerator();
						
					//Step 2: To get the languages in MAP and load the language specific url
						
					String currencyID = currencyMap.get(Constant.ID).trim();
					String isoCode = currencyMap.get(Constant.ISOCODE).trim();
					System.out.println(isoCode);
					String CurrencyName = currencyMap.get(Constant.ISONAME).trim();
					String languageCurrency = currencyMap.get(Constant.LANGUAGECURRENCY).trim();
					String regExpr=currencyMap.get(Constant.REGEXPRESSION).trim();
					String regExprNoSymbol=currencyMap.get(Constant.REGEXPRESSION_NOSYMBOL).trim();
					
					
					if(	util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles,currencyID,isoCode))
	                System.out.println("Username : "+userName); System.out.println("CurrencyName : "+CurrencyName);
				
	                if( util.migrateUser(userName))
	                {
	                 log.debug("User Migration : PASS");
	                 System.out.println("User Migration : PASS");

	                 String balance="700000000000";
	                 Thread.sleep(60000);
	                 
	                 if(util.updateUserBalance(userName,balance))
	                 {
	                 log.debug("Balance Updated as "+balance );
	                 System.out.println("Balance Updated as "+balance );
					
					String url = cfnlib.xpathMap.get("ApplicationURL");
					String launchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
					//launchURl = launchURl.replaceAll("\\blanguageCode=.*?(&|$)", "languageCode="+languageCurrency+"$1");
					System.out.println(launchURl); log.info("url = " +launchURl);
					
						
					if(	cfnlib.loadGame(launchURl))
					{					
						lvcReport.detailsAppendFolder("Currency Name is "+CurrencyName,"Currency ID is "+currencyID,"ISO Code is "+isoCode,"PASS",CurrencyName);
						
						
						//Verify Spin Button 
						cfnlib.waitForSpinButton();
						Thread.sleep(2000);	
						
						cfnlib.funcFullScreen();
						Thread.sleep(10000);
						
						// Check continue button exists and clicks
						if((Constant.YES.equalsIgnoreCase(cfnlib.xpathMap.get("continueBtnOnGameLoad"))))
						{
							cfnlib.newFeature();
						}
							
						
		for(int orientation=0;orientation<2;orientation++)
			{
			     if(orientation==0 && !webdriver.getOrientation().equals(ScreenOrientation.PORTRAIT))
			{
				boolean portrait= cfnlib.funcPortrait();
			if(portrait)
			{
			     log.debug("Able to rotate portrait");
			     System.out.println("Able to rotate portrait");
			}
			}
				  else if(orientation==1 && !webdriver.getOrientation().equals(ScreenOrientation.LANDSCAPE))
			{
			       boolean landscape=cfnlib.funcLandscape();
			if(landscape)
			{
					log.debug("Able to rotate landscape");
					System.out.println("Able to rotate landscape");
			}
		}
			
	 if(TestPropReader.getInstance().getProperty("GameCategory").equalsIgnoreCase("5ReelSlots"))
	{
		 
		 //Verify the Autoplay 
			boolean autoplay = cfnlib.isAutoplayAvailable();
			if(autoplay)
			{
				System.out.println("Autoplay : PASS");log.debug("Autoplay : PASS");
				lvcReport.detailsAppendFolder("Autoplay", "Autoplay Pannel ", "Autoplay Pannel", "PASS",CurrencyName);	
				Thread.sleep(2000);
				cfnlib.closeOverlay();
			}
			else
			{
				System.out.println("Autoplay : FAIL");log.debug("Autoplay : FAIL");
				lvcReport.detailsAppendFolder("Autoplay", "Autoplay Pannel", "Autoplay Pannel", "FAIL",""+CurrencyName);
			}Thread.sleep(2000);
			
			
							
			// method is used to verify Min and Max Bet && verifies Currency Format 
			   for(int i=1;i<3;i++)
				{			
				if(i==1)
				{
					System.out.println("**************MIN BET*******************");	log.debug("**************MIN BET*******************");			
			       boolean minBetFormatVerification =cfnlib.verifyRegularExpression(lvcReport,regExpr,cfnlib.moveCoinSizeSliderToMinBet(lvcReport,CurrencyName));
					if(minBetFormatVerification)
					{
						System.out.println("Set Min Bet : PASS");log.debug("Set Min Bet : PASS");
					}
					else
					{
						System.out.println("Set Min Bet : FAIL");log.debug("Set Min Bet : FAIL");
						lvcReport.detailsAppendFolder("Base Game", "Minimum Bet", "Minimum Bet", "FAIL",""+CurrencyName);
					}
											
				}//closing if for Min Bet 	
			
				else
				{
					System.out.println("**************MAX BET*******************");log.debug("**************MAX BET*******************");
				//for Max Bet 	
				boolean maxBetFormatVerification =	cfnlib.verifyRegularExpression(lvcReport,regExpr,cfnlib.moveCoinSizeSliderToMaxBet(lvcReport,CurrencyName));
				if(maxBetFormatVerification)
				{
					System.out.println("Set Max Bet : PASS");log.debug("Set Max Bet : PASS");
				}
				else
				{
					System.out.println("Set Max Bet : FAIL");log.debug("Set Max Bet : FAIL");
					lvcReport.detailsAppendFolder("Base Game", "Maximum Bet", "Maximum Bet", "FAIL",""+CurrencyName);
				}}	
			
						
		 	// Getts the Credit Amt && verifies Currency Format	and check the currency format	
			    
			    boolean credits = cfnlib.verifyRegularExpression(lvcReport,regExpr,cfnlib.getCurrentCredit(lvcReport,CurrencyName));
			    if(credits)
			    {
			    	System.out.println("Base Game Credit Value : PASS");log.debug("Base Game Credit Value : PASS");
					lvcReport.detailsAppendFolder("Base Game", "Credit Amount", "Credit Amount", "PASS",CurrencyName);
				}
				else
				{
					System.out.println("Base Game Credit Value : FAIL");log.debug("Base Game Credit Value : FAIL");
					lvcReport.detailsAppendFolder("Base Game", "Credit Amount", "Credit Amount", "FAIL",CurrencyName);
				}
			
				
				// Getts the Bet Amt && verifies Currency Format and check the currency format		
				
				boolean betAmt = cfnlib.verifyRegularExpression(lvcReport,regExpr,cfnlib.getCurrentBetAmt(lvcReport,CurrencyName));
				if(betAmt)
				{
					System.out.println("Base Game Bet Value : PASS");log.debug("Base Game Bet Value : PASS");
					lvcReport.detailsAppendFolder("Base Game", "Bet Amount", "Bet Amount", "PASS",CurrencyName);
				}
				else
				{
					System.out.println("Base Game Bet Value : FAIL");log.debug("Base Game Bet Value : FAIL");
					lvcReport.detailsAppendFolder("Base Game", "Bet Amount", "Bet Amount", "FAIL",CurrencyName);
				}
					
				//method is used to scroll the pay-table and validate the pay-outs and check the currency format	
				boolean scrollPaytable = cfnlib.paytableScroll(lvcReport,CurrencyName);
				if(scrollPaytable)
				{
					System.out.println("Paytable Open : PASS");log.debug("Paytable Open : PASS");
					
					boolean payouts =	cfnlib.validatePayoutsFromPaytable(lvcReport,CurrencyName,regExpr);
					if(payouts)
					{
	            	//cfnlib.PayoutValidationforBetLVC(lvcReport,regExpr,CurrencyName);
	            	lvcReport.detailsAppendFolder("Base Game", "Pay Table ", "PayOut Amount", "PASS",""+CurrencyName);
					}
					else
					{
						lvcReport.detailsAppendFolder("Base Game", "Pay Table ", "PayOut Amount", "FAIL",""+CurrencyName);
					}
					Thread.sleep(2000);cfnlib.func_click("PaytableClose");Thread.sleep(2000);
					System.out.println("Paytable Closed : PASS");log.debug("Paytable Closed : PASS");
			    }
				else
				{
					System.out.println("Paytable Open : FAIL");log.debug("Paytable Open : FAIL");
					lvcReport.detailsAppendFolder("Base Game", "PayOut Amount", "PayOut Amount", "FAIL",""+CurrencyName);
				}
				
				//method is used to Spin the Spin Buttton  (Big Win)
				cfnlib.spinclick();Thread.sleep(2000);
				
					
	            //method is used to Spins to get Big win and check the currency format
				
				boolean bigWinFormatVerification =	cfnlib.verifyRegularExpression(lvcReport,regExpr,cfnlib.verifyBigWin(lvcReport,CurrencyName));
				if(bigWinFormatVerification)
				{
					System.out.println("Base Game BigWin Value : PASS");log.debug("Base Game BigWin Value : PASS");
					lvcReport.detailsAppendFolder("Base Game", "Big Win Amt", "Big Win Amt", "PASS",CurrencyName);
				}
				else
				{
					System.out.println("Base Game BigWin Value : FAIL");log.debug("Base Game BigWin Value : FAIL");
					lvcReport.detailsAppendFolder("Base Game", " Big Win Amt", "Big Win Amt", "FAIL",CurrencyName);
				}Thread.sleep(2000); 
				
				//method is used to Spin the Spin Buttton  (Normal Win)
				cfnlib.spinclick();Thread.sleep(3000); 
				
				
				//method is used  to get the Win amt and check the currency format	
				boolean winFormatVerification =	cfnlib.verifyRegularExpression(lvcReport,regExpr,cfnlib.getCurrentWinAmt(lvcReport,CurrencyName));
				if(winFormatVerification)
				{
					System.out.println("Base Game Win Value : PASS");log.debug("Base Game Win Value : PASS");
					lvcReport.detailsAppendFolder("Base Game", "Win Amt", "Win Amt", "PASS",CurrencyName);
				}
				else
				{
					System.out.println("Base Game Win Value : FAIL");log.debug("Base Game Win Value : FAIL");
					lvcReport.detailsAppendFolder("Base Game", "Win Amt", "Win Amt", "FAIL",CurrencyName);
				}Thread.sleep(1000);
			
				
				//method is used to Spin the Spin Buttton  
				cfnlib.spinclick();Thread.sleep(3000); 
				
					
				if(TestPropReader.getInstance().getProperty("IsBonusAvailable").equalsIgnoreCase("yes"))
				{
				//Base Game Bonus 
				if (cfnlib.checkAvilabilityofElement("BonusFeatureImage"))
				{
					Thread.sleep(8000);
				 lvcReport.detailsAppendFolder("Base Game", " Bonus Feature is Available", " Bonus Feature is Available", "PASS",""+CurrencyName);
				 System.out.println("Bonus Feature is Available");log.debug("Bonus Feature is Available");
				 
				  //method is used to get the click , get the bonus text,verifies bonus summary screen in base game and check the currency format
				 boolean bonus = cfnlib.verifyRegularExpressionUsingArrays(lvcReport,regExprNoSymbol,cfnlib.bonusFeatureClickandGetText(lvcReport,CurrencyName));
				  System.out.println("Bonus Game : PASS");log.debug("Bonus Game : PASS");
				  cfnlib.spinclick();
				  Thread.sleep(2000);
				}
				else
				{
					 lvcReport.detailsAppendFolder("Base Game", " Bonus Feature is Not Available", " Bonus Feature is Not Available", "FAIL",CurrencyName);
					 System.out.println("Bonus Game : FAIL");log.debug("Bonus Game : FAIL");
				}}
				else
				{
					System.out.println("Bonus is not Available");log.debug("Bonus is not Available");
					 lvcReport.detailsAppendFolder("Base Game", " Bonus Feature is not  Available", " Bonus Feature is not Available", "FAIL ",""+CurrencyName);
				}
				
				
				// Free Spins Summary Screen - adding it in if loop and check availability 
				if(TestPropReader.getInstance().getProperty("IsFreeSpinAvailable").equalsIgnoreCase("yes"))
				{
				
				if(cfnlib.checkAvilabilityofElement("FreeSpins"))
				{
					
					lvcReport.detailsAppendFolder("Free Spins", "Free Spins is Available", "Free Spins is Available", "PASS",CurrencyName);
					// Free spins - 13 Spin 
					
					//1st Spins to get Big win in FS
					//method is used to get the current big win and check the currency format
					boolean bigWinFormatVerificationINFS =	cfnlib.verifyRegularExpression(lvcReport,regExpr,cfnlib.verifyBigWin(lvcReport,CurrencyName));
					if(bigWinFormatVerificationINFS)
					{
						System.out.println("Free Spins BigWin Value : PASS");log.debug("Free Spins BigWin Value : PASS");
						lvcReport.detailsAppendFolder("Free Spins", "Big Win Amt", ""+bigWinFormatVerificationINFS, "PASS",CurrencyName);
					}
					else
					{
						System.out.println("Free Spins BigWin Value : FAIL");log.debug("Free Spins BigWin Value : FAIL");
						lvcReport.detailsAppendFolder("Free Spins", "Big Win Amt", ""+bigWinFormatVerificationINFS, "FAIL",CurrencyName);
					}
					
					//2nd Spins to get Win in FS 
					//method is used to get the current win and check the currency format
					boolean winFormatVerificationINFS =	cfnlib.verifyRegularExpression(lvcReport,regExpr,cfnlib.getCurrentWinAmt(lvcReport,CurrencyName));
					if(winFormatVerificationINFS)
					{
						System.out.println("Free Games Win Value : PASS");log.debug("Free Games Win Value : PASS");
						lvcReport.detailsAppendFolder("Free Spins", "Win Amt", ""+winFormatVerificationINFS, "PASS",CurrencyName);
					}
					else
					{
						System.out.println("Free Games Win Value : FAIL");log.debug("Free Games Win Value : FAIL");
						lvcReport.detailsAppendFolder("Free Spins", "Win Amt", ""+winFormatVerificationINFS, "FAIL",CurrencyName);
					}Thread.sleep(3000);
			
					//3rd Spins to get Big win in FS
					//method is used to get the current big win and check the currency format
					boolean bigWinOnFSRefresh =	cfnlib.verifyRegularExpression(lvcReport,regExpr,cfnlib.verifyBigWinRefreshOnFreeSpins(lvcReport,CurrencyName));
					if(bigWinOnFSRefresh)
					{
						System.out.println("Free Spins BigWin Value on Refresh : PASS");log.debug("Free Spins BigWin Value on Refresh : PASS");
						lvcReport.detailsAppendFolder("Free Spins", "Big Win Amt on Refresh", ""+bigWinOnFSRefresh, "PASS",CurrencyName);
					}
					else
					{
						System.out.println("Free Spins BigWin Value : FAIL");log.debug("Free Spins BigWin Value : FAIL");
						lvcReport.detailsAppendFolder("Free Spins", "Big Win Amt", ""+bigWinFormatVerificationINFS, "FAIL",CurrencyName);
					}
					
					
					// Getts the Credit Amt && verifies Currency Format	
					//method is used to get the current credit and check the currency format
				    boolean bonusCredits = cfnlib.verifyRegularExpression(lvcReport,regExpr,cfnlib.getCurrentCredit(lvcReport,CurrencyName));
				    if(bonusCredits)
				    {
				    	System.out.println("Free Game Credit Value : PASS");log.debug("Free Game Credit Value : PASS");
				    	lvcReport.detailsAppendFolder("Free Spins", "Credit Amt", ""+bonusCredits, "PASS",CurrencyName);
					}
					else
					{
						System.out.println("Free Game Credit Value : FAIL");log.debug("Free Game Credit Value : FAIL");
						lvcReport.detailsAppendFolder("Free Spins", "Credit Amt",  ""+bonusCredits, "FAIL",CurrencyName);
					}
				
					// Getts the Bet Amt && verifies Currency Format	getCurrentBetAmt
					//method is used to get the current totalwin and check the currency format
					boolean bonusBetAmt = cfnlib.verifyRegularExpression(lvcReport,regExpr,cfnlib.getCurrentTotalWinINFS(lvcReport,CurrencyName));
					if(bonusBetAmt)
					{
						System.out.println("Free Game Win Value : PASS");log.debug("Free Game Win Value : PASS");
						lvcReport.detailsAppendFolder("Free Spins", "Total Win Amt",""+ bonusBetAmt, "PASS",CurrencyName);
					}
					else
					{
						System.out.println("Free Game Win Value : FAIL");log.debug("Free Game Win Value : FAIL");
						lvcReport.detailsAppendFolder("Free Spins", "Total Win Amt", ""+ bonusBetAmt, "FAIL",CurrencyName);
					}
					
					//method is used to verify free spins summary screen  and check the currency format
					if (cfnlib.checkAvilabilityofElement("FSSummaryScreen"))
					{
					boolean fsSummaryScreen =cfnlib.verifyRegularExpression(lvcReport,regExprNoSymbol,cfnlib.freeSpinsSummaryScreen(lvcReport,CurrencyName));
					if(fsSummaryScreen)
					{
						System.out.println("Free Spins Summary Screen : PASS");log.debug("Free Spins Summary Screen : PASS");
						lvcReport.detailsAppendFolder("Free Spins", " Summary Screen", " Summary Screen", "PASS",CurrencyName);
					}
					else
					{
						System.out.println("Free Spins Summary Screen : FAIL");log.debug("Free Spins Summary Screen : FAIL");
						lvcReport.detailsAppendFolder("Free Spins", " Summary Screen", " Summary Screen", "FAIL",CurrencyName);
					}}//Closing FS Summary Screen 
			    }//Closing FS  
				
				}//closing Freespins Flag
				
				else
				{
					System.out.println("Free Spins Availablity : FAIL");log.debug("Free Spins Availablity : FAIL");
					lvcReport.detailsAppendFolder("Free Spins", "Free Spins is Available", "Free Spins is Available", "FAIL",""+CurrencyName);
				}
				}//closing for loop if loop of Bet Settings of Min and Max 
	}//closing if loop of GameCategory
	 else	 
	 {
			// for 3 reel Games
			
		//menu buttons and navigations
		 
			boolean menu =	cfnlib.menuButtonS(lvcReport,CurrencyName);
			if(menu)
			{
				System.out.println("Menu : PASS");log.debug("Menu : PASS");
				lvcReport.detailsAppendFolder("Menu", "Menu Pannel ", "Menu Pannel", "PASS",CurrencyName);	
				Thread.sleep(2000);
			}
			else
			{
				System.out.println("Menu : FAIL");log.debug("Menu : FAIL");
				lvcReport.detailsAppendFolder("Menu", "Menu Pannel", "Menu Pannel", "FAIL",""+CurrencyName);
			}Thread.sleep(2000);
			
			
			// clock availablity 
			boolean isClockAvailable = cfnlib.clockFromTopBar(lvcReport,CurrencyName);
			if(isClockAvailable)
			{
				System.out.println("Clock : PASS");log.debug("Clock : PASS");
				lvcReport.detailsAppendFolder("Clock", "Clock Pannel ", "Clock Pannel", "PASS",CurrencyName);	
				Thread.sleep(2000);
			}
			else
			{
				System.out.println("Clock : FAIL");log.debug("Clock : FAIL");
				lvcReport.detailsAppendFolder("Clock", "Clock Pannel", "Clock Pannel", "FAIL",""+CurrencyName);
			}Thread.sleep(2000);
			
			//topbar icons validation
			boolean topBarMenu = cfnlib.topBarMenuButtonIcons(lvcReport,CurrencyName);
			if(topBarMenu)
			{
				System.out.println("TopBarMenu : PASS");log.debug("TopBarMenu : PASS");
				lvcReport.detailsAppendFolder("TopBarMenu", "TopBarMenu Pannel ", "TopBarMenu Pannel", "PASS",CurrencyName);	
				Thread.sleep(2000);
			}
			else
			{
				System.out.println("TopBarMenu : FAIL");log.debug("TopBarMenu : FAIL");
				lvcReport.detailsAppendFolder("TopBarMenu", "TopBarMenu Pannel", "TopBarMenu Pannel", "FAIL",""+CurrencyName);
			}Thread.sleep(2000);
			
			
			for(int i=1;i<3;i++)
			{			
			if(i==1)
			{
				System.out.println("**************MIN BET*******************");	log.debug("**************MIN BET*******************");		
				
				boolean minBetFormatVerification =cfnlib.verifyRegularExpression(lvcReport,regExpr,cfnlib.setMinBetUsingScrollBar(lvcReport,CurrencyName));
				if(minBetFormatVerification)
				{
					System.out.println("Set Min Bet : PASS");log.debug("Set Min Bet : PASS");
					//lvcReport.detailsAppendFolder("Base Game", "Minimum Bet", "Minimum Bet", "PASS",""+CurrencyName)
				}
				else
				{
					System.out.println("Set Min Bet : FAIL");log.debug("Set Min Bet : FAIL");
					lvcReport.detailsAppendFolder("Base Game", "Minimum Bet", "Minimum Bet", "FAIL",""+CurrencyName);
				}
										
			}//closing if for Min Bet 	
			
			else
			{
				System.out.println("**************MAX BET*******************");log.debug("**************MAX BET*******************");
			//for Max Bet 	
			boolean maxBetFormatVerification =	cfnlib.verifyRegularExpression(lvcReport,regExpr,cfnlib.setMaxBetUsingScrollBar(lvcReport,CurrencyName));
			if(maxBetFormatVerification)
			{
				System.out.println("Set Max Bet : PASS");log.debug("Set Max Bet : PASS");
				
				//lvcReport.detailsAppendFolder("Base Game", "Maximum Bet", "Maximum Bet", "PASS",""+CurrencyName);
			}
			else
			{
				System.out.println("Set Max Bet : FAIL");log.debug("Set Max Bet : FAIL");
				lvcReport.detailsAppendFolder("Base Game", "Maximum Bet", "Maximum Bet", "FAIL",""+CurrencyName);
			}}	
			
			
			///method is used to scroll the pay-table and validate the pay-outs and check the currency format	
			boolean paytable = cfnlib.paytableVarification(lvcReport,CurrencyName);
			if(paytable)
			{
				System.out.println("Paytable Open : PASS");log.debug("Paytable Open : PASS");
				boolean paytablePayouts = cfnlib.verifyRegularExpressionUsingArrays(lvcReport,regExpr,cfnlib.singlePaytablePayouts(lvcReport,CurrencyName));
				if(paytablePayouts)
				{
					System.out.println("Paytable Payouts : PASS");log.debug("Paytable Payouts : PASS");
				}
				else
				{
					System.out.println("Paytable Payouts : FAIL");log.debug("Paytable Payouts : FAIL");
				}
				
		    }
			else
			{
				System.out.println("Paytable Open : FAIL");log.debug("Paytable Open : FAIL");
				lvcReport.detailsAppendFolder("Base Game", "PayOut Amount", "PayOut Amount", "FAIL",""+CurrencyName);
			}
			
					
	 	// Getts the Credit Amt && verifies Currency Format	and check the currency format	
		    
		    boolean credits = cfnlib.verifyRegularExpression(lvcReport,regExpr,cfnlib.func_GetText("Creditvalue"));
		    if(credits)
		    {
		    	System.out.println("Base Game Credit Value : PASS");log.debug("Base Game Credit Value : PASS");
				lvcReport.detailsAppendFolder("Base Game", "Credit Amount", ""+credits, "PASS",""+CurrencyName);
			}
			else
			{
				System.out.println("Base Game Credit Value : FAIL");log.debug("Base Game Credit Value : FAIL");
				lvcReport.detailsAppendFolder("Base Game", "Credit Amount", ""+credits, "FAIL",""+CurrencyName);
			}
		
			
			// Getts the Bet Amt && verifies Currency Format and check the currency format		
			
			boolean betAmt = cfnlib.verifyRegularExpression(lvcReport,regExpr,cfnlib.func_GetText("BetAmt"));
			if(betAmt)
			{
				System.out.println("Base Game Bet Value : PASS");log.debug("Base Game Bet Value : PASS");
				lvcReport.detailsAppendFolder("Base Game", "Bet Amount", ""+betAmt, "PASS",""+CurrencyName);
			}
			else
			{
				System.out.println("Base Game Bet Value : FAIL");log.debug("Base Game Bet Value : FAIL");
				lvcReport.detailsAppendFolder("Base Game", "Bet Amount", ""+betAmt, "FAIL",""+CurrencyName);
			}
			
		
			//method is used to Spin the Spin Buttton  (Normal Win)
			cfnlib.spinclick();Thread.sleep(3000);
        //method is used to Spins to get Big win and check the currency format
			
			boolean bigWinFormatVerification =	cfnlib.verifyRegularExpression(lvcReport,regExpr,cfnlib.verifyBigWin(lvcReport,CurrencyName));
			if(bigWinFormatVerification)
			{
				System.out.println("Base Game BigWin Value : PASS");log.debug("Base Game BigWin Value : PASS");
				lvcReport.detailsAppendFolder("Base Game", "Big Win Amt", ""+bigWinFormatVerification, "PASS",""+CurrencyName);
			}
			else
			{
				System.out.println("Base Game BigWin Value : FAIL");log.debug("Base Game BigWin Value : FAIL");
				lvcReport.detailsAppendFolder("Base Game", " Big Win Amt", ""+bigWinFormatVerification, "FAIL",""+CurrencyName);
			}Thread.sleep(5000);
			
			
			
			//method is used to Spin the Spin Buttton  ( Win)
			cfnlib.spinclick();Thread.sleep(5000); 
			
        
			//method is used  to get the Win amt and check the currency format	
			boolean winFormatVerification =	cfnlib.verifyRegularExpression(lvcReport,regExpr,cfnlib.getCurrentWinAmt(lvcReport,CurrencyName));
			if(winFormatVerification)
			{
				System.out.println("Base Game Win Value : PASS");log.debug("Base Game Win Value : PASS");
				lvcReport.detailsAppendFolder("Base Game", "Win Amt", ""+winFormatVerification, "PASS",""+CurrencyName);
			}
			else
			{
				System.out.println("Base Game Win Value : FAIL");log.debug("Base Game Win Value : FAIL");
				lvcReport.detailsAppendFolder("Base Game", "Win Amt", ""+winFormatVerification, "FAIL",""+CurrencyName);
			}Thread.sleep(1000);
			
			//method is used to Spin the Spin Buttton  (BIG Win)
			cfnlib.spinclick();Thread.sleep(5000); 
			
			boolean refreshOnWin =	cfnlib.verifyRegularExpression(lvcReport,regExpr,cfnlib.verifyBigWinOnRefresh(lvcReport,CurrencyName));
			if(refreshOnWin)
			{
				System.out.println("Base Game BigWin Value on Refresh : PASS");log.debug("Base Game BigWin Value on Refresh : PASS");
				lvcReport.detailsAppendFolder("Base Game", "Big Win Amt on Refresh", ""+refreshOnWin, "PASS",""+CurrencyName);
			}
			else
			{
				System.out.println("Base Game BigWin Value on Refresh : PASS");log.debug("Base Game BigWin Value on Refresh : PASS");
				lvcReport.detailsAppendFolder("Base Game", " Big Win Amt", ""+bigWinFormatVerification, "FAIL",""+CurrencyName);
			}Thread.sleep(3000); 
				
			
			}	//closing for loop for 3reel GameCategory  
			
		
	 }//closing for else for 3reel GameCategory 
    }//closing for loop for landscape and portrait
		System.out.println("            ");
					}	//closing if loop load game 
					else
					{
						System.out.println("Unable to load the game");
						lvcReport.detailsAppendFolder("Launched Currency Name is "+CurrencyName,"Launched Currency ID is"+currencyID,"Launched ISO Code is "+isoCode,"FAIL",CurrencyName);
					 }
					}//Closing user migration 
	                else
					{
						System.out.println("Unable do user migartion");
						lvcReport.detailsAppendFolder("Launched Currency Name is "+CurrencyName,"Launched Currency ID is"+currencyID,"Launched ISO Code is "+isoCode,"FAIL",CurrencyName);
					 }
	   			}//closing Balance Updation  
	                else
					{
						System.out.println("Unable to update the balance");
						lvcReport.detailsAppendFolder("Launched Currency Name is "+CurrencyName,"Launched Currency ID is"+currencyID,"Launched ISO Code is "+isoCode,"FAIL",CurrencyName);
					 }
			}//closing for loop of currency mapping 
			
			}//closing if loop of webdriver
		else
		{
			System.out.println("Unable to load the game");
			lvcReport.detailsAppend("Load Game ", "Unable to Load the game", "Unable to Load the game", "FAIL");
		 }
}//closing try block 
			// -------------------Handling the exception---------------------//

			catch (Exception e) 
			{
				//log.error(e.getMessage(), e);
				cfnlib.evalException(e);
			}

			finally
			{
				lvcReport.endReport();
				if(!copiedFiles.isEmpty())
				{
					if(TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
							util.deleteBluemesaTestDataFiles(mid,cid,copiedFiles);
					else
						apiObj.deleteAxiomTestDataFiles(copiedFiles);
				}
				webdriver.close();
				webdriver.quit();
				//proxy.abort();
				Thread.sleep(1000);
			}
		}

	}
