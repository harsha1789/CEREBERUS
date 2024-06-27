package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;
/**
 * 
 * 
 * This Script is for Low Value Currency - Bonus
 * ===========================================================================
 * 1.Bonus 
 * ===========
 * 1.1.Set Bet (Max & Min)
 * 1.2.Bonus
 *  
 *  
 * TestData 
 * ==============
 * 1.Bonus (Depending on the game)
 * 
 * 
 * @author rk61073
 *
 */

public class Desktop_Regression_LVC_CS_Bonus
{
	Logger log = Logger.getLogger(Desktop_Regression_LVC_CS_Bonus.class.getName()); 
	public ScriptParameters scriptParameters;
	public void script() throws Exception
	{

		String mstrTCName=scriptParameters.getMstrTCName();
		String mstrTCDesc=scriptParameters.getMstrTCDesc();
		String mstrModuleName=scriptParameters.getMstrModuleName();
		WebDriver webdriver=scriptParameters.getDriver();
		BrowserMobProxyServer proxy=scriptParameters.getProxy();
		String startTime=scriptParameters.getStartTime();
		String filePath=scriptParameters.getFilePath();
		String userName=scriptParameters.getUserName();
		String browserName=scriptParameters.getBrowserName();
		String framework=scriptParameters.getFramework();
		String gameName=scriptParameters.getGameName();
		String status1=null;
		int mintDetailCount=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;	
			
		
		Desktop_HTML_Report lvcReport = new Desktop_HTML_Report(webdriver,browserName,filePath,startTime,mstrTCName,mstrTCDesc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,status1,gameName);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,lvcReport, gameName);
		CommonUtil util = new CommonUtil();
		RestAPILibrary apiObj = new RestAPILibrary();
		
		List<String> copiedFiles=new ArrayList<>();
		int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		System.out.println("MID : " +mid);log.debug("MID : " +mid);
		int cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDDesktop"));
		System.out.println("CID : " +cid);log.debug("CID : " +cid);
     	
		try
		{
		
			// Step 1 
			if(webdriver!=null)
			{	
				//Implement code for test data copy depending on the env
				String strFileName=TestPropReader.getInstance().getProperty("BonusTestDataPath");
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

                String balance=cfnlib.XpathMap.get("BalanceUpdationForLVC");
                Thread.sleep(60000);
                
                if(util.updateUserBalance(userName,balance))
                {
                log.debug("Balance Updated as "+balance );
                System.out.println("Balance Updated as "+balance );
                
                 
                String url = cfnlib.XpathMap.get("ApplicationURL"); 
				String launchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1" );
				//launchURl = launchURl.replaceAll("\\blanguageCode=.*?(&|$)", "languageCode="+languageCurrency+"$1");
				System.out.println(launchURl); log.info("url = " +launchURl);
			
				
				if(	cfnlib.loadGame(launchURl))
				{
					lvcReport.detailsAppendNoScreenshot("CURRENCY NAME :: "+CurrencyName,"CURRENCY ID :: "+currencyID,"ISO CODE :: "+isoCode,"PASS");
					
					if(framework.equalsIgnoreCase("CS"))
					{
						cfnlib.setNameSpace();
					}
					Thread.sleep(2000);
					
					// Check continue button exists and clicks
					if((Constant.YES.equalsIgnoreCase(cfnlib.XpathMap.get("continueBtnOnGameLoad"))))
					{
						cfnlib.newFeature();
					}
					
					Thread.sleep(3000);
					
					//Verify Spin Button 
					cfnlib.waitForSpinButton();
					Thread.sleep(3000);	
					
										
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
					lvcReport.detailsAppendFolder("Bet Settings ", "Minium Bet", "Minium Bet", "PASS",""+CurrencyName);
					
				}
				else
				{
					System.out.println("Set Min Bet : FAIL");log.debug("Set Min Bet : FAIL");
					lvcReport.detailsAppendFolder("Bet Settings ", "Minium Bet", "Minium Bet", "FAIL",""+CurrencyName);
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
				lvcReport.detailsAppendFolder("Bet Settings ", "Maximum Bet", "Minium Bet", "PASS",""+CurrencyName);
			}
			else
			{
				System.out.println("Set Max Bet : FAIL");log.debug("Set Max Bet : FAIL");
				lvcReport.detailsAppendFolder("Base Game", "Maximum Bet", "Maximum Bet", "FAIL",""+CurrencyName);
			}}	//closing if for Max Bet 	
			
    
			//method is used to Spin the Spin Button 
			cfnlib.spinclick();
			Thread.sleep(2000);
				
			if(TestPropReader.getInstance().getProperty("IsBonusAvailable").equalsIgnoreCase("yes"))
			{
				Thread.sleep(8000);
			//Base Game Bonus 
			if (cfnlib.checkAvilabilityofElement("BonusFeatureImage"))
			{
			 lvcReport.detailsAppendFolder("Base Game", " Bonus Feature is Available", " Bonus Feature is Available", "PASS",""+CurrencyName);
			 System.out.println("Bonus Feature is Available");log.debug("Bonus Feature is Available");
			 
			 
			 if(gameName.contains("Halloweenies") && cfnlib.XpathMap.get("Bonus").equalsIgnoreCase("yes"))
			 {
				   //method is used to get the click , get the bonus text,verifies bonus summary screen in base game and check the currency format
					boolean bonus = cfnlib.verifyRegularExpressionUsingArrays(lvcReport,regExprNoSymbol,cfnlib.bonusFeatureClickandGetText(lvcReport,CurrencyName));
					if (bonus)
					{
					System.out.println("Bonus Game : PASS");log.debug("Bonus Game : PASS");
					Thread.sleep(3000);
					lvcReport.detailsAppendFolder("Bonus Game", " Bonus Feature","Bonus Feature", "PASS", CurrencyName);
					}
					else 
					{
					System.out.println("Bonus Game : FAIL");log.debug("Bonus Game : FAIL");
					Thread.sleep(3000);
					lvcReport.detailsAppendFolder("Bonus Game", " Bonus Feature","Bonus Feature", "FAIL", CurrencyName);
					}
				 
			 }
			 else if(gameName.contains("LuckyKoi") && cfnlib.XpathMap.get("Bonus").equalsIgnoreCase("yes"))
			 {
				 cfnlib.tapOnCoordinates("FreeSpins",843, 433);
				 
				 /*boolean bonus = cfnlib.verifyRegularExpression(lvcReport,regExprNoSymbol,cfnlib.bonusfeatureClickOnCoOrdinatesandGetText(lvcReport,CurrencyName));
					if (bonus)
					{
					System.out.println("Bonus Game : PASS");log.debug("Bonus Game : PASS");
					Thread.sleep(3000);
					lvcReport.detailsAppendFolder("Bonus Game", " Bonus Feature","Bonus Feature", "PASS", CurrencyName);
					}
					else 
					{
					System.out.println("Bonus Game : FAIL");log.debug("Bonus Game : FAIL");
					Thread.sleep(3000);
					lvcReport.detailsAppendFolder("Bonus Game", " Bonus Feature","Bonus Feature", "FAIL", CurrencyName);
					}	*/ 
			 }
			 else
			 {
				System.out.println("There is no Bonus Feature Available") ;log.debug("There is no Bonus Feature Available");
			 }	
			}//closing if loop of checkAvilabilityofElement 
			else
			{
				 lvcReport.detailsAppendFolder("Base Game", " Bonus Feature is not Available", " Bonus Feature is not Available", "FAIL",CurrencyName);
				 System.out.println("Bonus Entry Screen : FAIL");log.debug("Bonus Entry Screen : FAIL");
			}
			  }//closing the bonus available
			else
			{
				System.out.println("Bonus is not Available");log.debug("Bonus is not Available");
				 lvcReport.detailsAppendFolder("Base Game", " Bonus Feature is not  Available", " Bonus Feature is not Available", "FAIL ",""+CurrencyName);
			}
		}//closing for loop if loop of Bet Settings of Min and Max 
			System.out.println("            ");		
				}	//closing if loop load game 
				else
				{
					System.out.println("Unable to load the game");log.debug("Unable to load the game");
					lvcReport.detailsAppendFolder("Launched Currency Name is "+CurrencyName,"Launched Currency ID is"+currencyID,"Launched ISO Code is "+isoCode,"FAIL",CurrencyName);
				 }
				
               }//closing Balance Updation
                else
				{
					System.out.println("Unable to update the balance");log.debug("Unable to update the balance");
					lvcReport.detailsAppendFolder("Unable to update the balance","Launched Currency ID is"+currencyID,"Launched ISO Code is "+isoCode,"FAIL",CurrencyName);
				 }
   			} //Closing user migration  
               else
				{
					System.out.println("Unable do user migartion");log.debug("Unable do user migartion");
					lvcReport.detailsAppendFolder("Unable do user migartion","Launched Currency ID is"+currencyID,"Launched ISO Code is "+isoCode,"FAIL",CurrencyName);
				 }
   				}//closing for loop of currency mapping 
				
   			}//closing if loop of webdriver
			else
			{
				System.out.println("Unable to load the game");
				lvcReport.detailsAppend("Load Game ", "Unable to Load the game", "Unable to Load the game", "FAIL");
			 }
					
					}//closing try block 
		
		//-------------------Handling the exception---------------------//
		catch (Exception e) 
		{
			log.error(e.getMessage(),e);
			
		}
		//-------------------Closing the connections---------------//
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
		}//closing finally block	
	
}
	
		
	

}
