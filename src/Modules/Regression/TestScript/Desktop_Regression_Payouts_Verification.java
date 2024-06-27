package Modules.Regression.TestScript;

import java.util.ArrayList;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
//import org.skyscreamer.jsonassert.JSONCompareResult;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;

public class Desktop_Regression_Payouts_Verification {

	/**
	 * For each bet amount, This script validate all the symbol pay out values in the paytable.
	 * @author Premlata 
	 */
	Logger log = Logger.getLogger(Desktop_Regression_Payouts_Verification.class.getName());
	
	public ScriptParameters scriptParameters;
	

	public void script() throws Exception{
		
		String Xpath=null;;
		String payline;
		String paytablePayout;
		String scatter;
		String scatterCount;
		//int xmlsno=1;
		int length;
		double gamePayout;
		double scatterCount_Int;
		double betCount;
		String xmlFileName;
		int index;
		 String gameLanguage = null;
		// JSONCompareResult languageResult;
		 String mstrTC_Name=scriptParameters.getMstrTCName();
			String mstrTC_Desc=scriptParameters.getMstrTCDesc();
			String mstrModuleName=scriptParameters.getMstrModuleName();
			WebDriver webdriver=scriptParameters.getDriver();
			BrowserMobProxyServer proxy=scriptParameters.getProxy();
			String startTime=scriptParameters.getStartTime();
			String filePath=scriptParameters.getFilePath();
			String userName=scriptParameters.getUserName();
			String browsername=scriptParameters.getBrowserName();
			String framework=scriptParameters.getFramework();
			String gameName=scriptParameters.getGameName();
			String languageDescription=null;
			String languageCode=null;
			String Status=null;
			int mintDetailCount=0;
			//int mintSubStepNo=0;
			int mintPassed=0;
			int mintFailed=0;
			int mintWarnings=0;
			int mintStepNo=0;
			
			ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();
		Desktop_HTML_Report payout = new Desktop_HTML_Report(webdriver,browsername,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,gameName);
		CommonUtil util=new CommonUtil();
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,payout, gameName);


		try{
			

			// Step 1 

			if(webdriver!=null)
			{		
				Map<String, String> rowData2 = null;
				String xmlFilePath = TestPropReader.getInstance().getProperty("XMLFilePath");
				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				String password = Constant.PASSWORD;
				length=util.xmlLength(xmlFilePath,"WinningCombinations");
				ArrayList<String> symbolData=util.getXMLDataInArray("Symbol","name", xmlFilePath);
				
				String url = cfnlib.XpathMap.get("ApplicationURL");
				
				//If it is ways game check paylinecost in refresh packet response or
				//it is line games check paylinecost in game xml
				payline=cfnlib.XpathMap.get("payline");//this code will fetch the data from sheet
				double paylineNumber=Double.parseDouble(payline);
				
				int payoutCount=length;
				/*This code fetch the scatter count from sheet and parse into integer*/	
				scatterCount=cfnlib.XpathMap.get("ScatterCount");
				scatterCount_Int=Double.parseDouble(scatterCount);
				
				/*This code is to fetch betCount from sheet and convert it to double*/
				betCount=Double.parseDouble(cfnlib.XpathMap.get("betCount"));
				
				
				String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
				System.out.println("url = " +LaunchURl);
				cfnlib.loadGame(LaunchURl);
				/*String obj = cfnlib.func_navigate(url);
				if(obj!=null)
				{
					payout.details_append("Open Browser and Enter Lobby URL in address bar and click Enter", "Island Paradise Lobby should open", "Island Paradise lobby is Open", "Pass");
				}
				else 
				{
					payout.details_append("Open browser and Enter Lobby URL in address bar and click Enter", "Island Paradise lobby should Open", "Island paradise is not displayed", "Fail");
				}
				// Capture Screen shot for BaseScene after login
				String obj1= cfnlib.Func_LoginBaseScene(userName, constant.Password);
				if(obj1 != null){
					payout.details_append("Verify that the application should display Game Logo and game name in Base Scene in Practice Play", "Game logo and game name with base scene should display", "Game logo and game name with base scene displays", "Pass");
				}
				else {
					payout.details_append("Verify that the application should Logo and game name in Base scene in Practice Play", "Game logo and game name with base scene should display", "Game logo and game name with base scene is not display", "Fail");
				}*/
				
				if(framework.equalsIgnoreCase("Force")){
					cfnlib.setNameSpace();
					}
				
				cfnlib.newFeature();
				/*This method is to set the bet value minimum*/
				cfnlib.setMaxBet();
				for(int i=1;i<=betCount;i++)
				{
					//setting index at the staring
					index=0;
					//Get the bet amount
					double bet=cfnlib.GetBetAmt();
					System.out.println("Processing for Bet :: "+bet);
					payout.detailsAppend("verify the payouts after changing bet and bet value  :"+bet, "Paytable payout must be correct after changing bet","", "");
					cfnlib.capturePaytableScreenshot(payout, "");
					// Below logic verifies all the values in paytable for this bet 
					for(int j=0;j<payoutCount;j++)
					{
						
						//Step 2: To get the game payout in MAP from sheet 
						if(framework.equalsIgnoreCase("CS"))
						{
						rowData2 = excelpoolmanager.readExcelByRow(testDataExcelPath, "PaytablePayout",  j);
						String Element = rowData2.get("Element").toString();
						Xpath = rowData2.get("Xpath").toString().trim();
						System.out.println(Xpath);
						}
						paytablePayout=util.readXML("WinCombination", "numSymbolsRequired", "symbols","payouts", xmlFilePath,length,index);
						System.out.println("paytablePayout:"+paytablePayout);
						if(paytablePayout.contains("Scatter")|| paytablePayout.contains("FreeSpins"))
						{
							scatter="yes";
						}
						else
						{
							scatter="no";
						}
						String[] xmlData=paytablePayout.split(",");
						String xmlpayout=xmlData[2];
						System.out.println(xmlpayout);
						if(framework.equalsIgnoreCase("CS"))
						{
						gamePayout=cfnlib.gamepayout(Xpath);//it will fetch game payout for cs game
						}
						else
						{
							gamePayout=cfnlib.gamepayout(symbolData,paytablePayout);//it will fetch game payout for Force game
						}
						double result= cfnlib.verifyPaytable_Payouts(xmlpayout,paylineNumber,bet,scatter);
						
						System.out.println(result);
						if(gamePayout==result)
						{
							payout.detailsAppendNoScreenshot("Verify that the payout "+ xmlData[1] + " coming in paytable is correct ", "Paytable payout must be correct","bet value :"+bet+" xml base value :"+xmlpayout+" result from formula :"+result+" symbol name : "+xmlData[1]+"game payout : " +gamePayout+ " is correct", "pass");
						}
						else
						{
							payout.detailsAppendNoScreenshot("Verify that the payout "+ xmlData[1] + " coming in paytable is incorrect ", "Paytable payout must be correct","bet value :"+bet+" xml base value :"+xmlpayout+" result from formula :"+result+" symbol name :"+xmlData[1]+"game payout : " +gamePayout+ " is incorrect", "fail");	
						}
						length--;
						index++;
					}
					// Closes the paytable
					cfnlib.paytableClose();
					// Change the bet to the next bet
					cfnlib.betIncrease();
					length=util.xmlLength(xmlFilePath,"WinningCombinations");
					//xmlsno++;
				}
				//Step 5: Logout From the Game
				/*String logout= cfnlib.Func_logout_OD();
				Thread.sleep(3000);
				if(logout.trim()!=null)
				{
					payout.details_append("Verify that user has successfully logged out ", "User should be successfully logged out", "User is Successfully logged out", "pass");
				}
				else 
				{
					payout.details_append("Verify that user has successfully logged out ", "User should be successfully logged out", "User is not Successfully logged out", "fail");
				}*/



			}
		}
		//-------------------Handling the exception---------------------//
		catch (Exception e) {
			//System.out.println(e.getMessage());
			e.printStackTrace();
			cfnlib.evalException(e);

		}
		//-------------------Closing the connections---------------//
		finally
		{
			payout.endReport();
			webdriver.close();
			webdriver.quit();
			proxy.abort();
			Thread.sleep(1000);	   
		}	
	}
}



