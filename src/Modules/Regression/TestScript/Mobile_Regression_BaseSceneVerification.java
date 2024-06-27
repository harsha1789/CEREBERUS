package Modules.Regression.TestScript;
import java.io.File;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import com.zensar.automation.framework.library.AkazeImageFinder;
import com.zensar.automation.framework.library.TestdroidImageRecognition;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import net.lightbody.bmp.BrowserMobProxyServer;

public class Mobile_Regression_BaseSceneVerification {
	
	Logger log = Logger.getLogger(Mobile_Regression_BaseSceneVerification.class.getName());
	public ScriptParameters scriptParameters;
	
	public void script() throws Exception{

		

		String mstrTC_Name=scriptParameters.getMstrTCName();
		String mstrTC_Desc=scriptParameters.getMstrTCDesc();
		String mstrModuleName=scriptParameters.getMstrModuleName();
		BrowserMobProxyServer proxy=scriptParameters.getProxy();
		String startTime=scriptParameters.getStartTime();
		String filePath=scriptParameters.getFilePath();
		
		//AndroidDriver<WebElement> webdriver=scriptParameters.getWebdriver();
		AppiumDriver<WebElement> webdriver=scriptParameters.getAppiumWebdriver();
		
		String DeviceName=scriptParameters.getDeviceName();
		String userName=scriptParameters.getUserName();
		String framework=scriptParameters.getFramework();
		//String deviceLockKey=scriptParameters.getDeviceLockKey();
		String gameName=scriptParameters.getGameName();
		//int checkedOutDeviceNum=scriptParameters.getCheckedOutDeviceNum();
		
		
		String Status=null;
		int mintDetailCount=0;
		//int mintSubStepNo=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		
		String xmlFilePath= TestPropReader.getInstance().getProperty("CurrencyTestDataPath");
		File src= new File(xmlFilePath);  
		String classname= this.getClass().getSimpleName();
		File destFile = new File("\\\\10.247.224.22\\C$\\MGS_TestData\\"+classname+".testdata");
		Mobile_HTML_Report tc01=new Mobile_HTML_Report(webdriver,DeviceName,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,gameName);
		ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();
		

		MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, tc01, gameName);
		
		
		try{

			// DataBaseFunctions createplayer= new DataBaseFunctions(DeviceName, DeviceName, DeviceName, DeviceName, DeviceName, DeviceName);
			//CreateUserInDB createplayer = new CreateUserInDB(); 
			



			//-------------------Getting webdriver of proper test site---------------------//
			// Step 1
			//open lobby and verify lucky twins name
			tc01.detailsAppend("Verify BaseScene features", "Base Scene Features should be displayed", "", "");
			if(webdriver!=null)
			{		
				Map<String, String> rowData1 = null;
				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				int rowcount1 =excelpoolmanager.rowCount(testDataExcelPath,"Test_Data");
				for(int i=1;i<rowcount1;i++){
					rowData1 = excelpoolmanager.readExcelByRow(testDataExcelPath,"Test_Data" ,i);
					String testcaseName1=rowData1.get("TestCaseName").toString().trim();
					String url=Constant.URL;
					String testcaseName=rowData1.get("TestCaseName").toString().trim();

					/*cfnlib.changePlayerName(userName, clientID, xmlFilePath);
				cfnlib.copyFolder(src, destFile);*/

					//userName = constant.username;
					String password=Constant.PASSWORD;
					String gametitleexcel=gameName;

					if (testcaseName1.equalsIgnoreCase(classname)){
						String obj=cfnlib.funcNavigate(url);

						if(obj!=null)
						{
							tc01.detailsAppend("Open browser and Enter game URL in address bar and click Enter", "Island Paradise Lobby should be displayed", "Island Paradise lobby is displayed", "Pass");
						}
						else
						{
							tc01.detailsAppend("Open browser and Enter Game URL in address bar and click Enter", "Island Paradise lobby should be displayed", "Island paradise is not displayed", "Fail");

						}


						// Step s1.2
						/*Login to application and verify the game title*/
						tc01.detailsAppend("Verify user is able to login to the Game "+gameName+"", "User should be able to Login "+gameName+"", "", "");


						/*userName=gcfnlib.randomStringgenerator();
							System.out.println("The New usename is =="+ userName);
							createplayer.createUser(userName, 0, 0);
							gcfnlib.changePlayerName(userName, constant.clientID, xmlFilePath);
							gcfnlib.copyFolder(src, destFile);*/
						//userName=DeviceName;
						//userName=DeviceName;

						String GameTitle=cfnlib.loginToBaseScene(userName,password);
					
						if(GameTitle!=null)
						{
							tc01.detailsAppend("Check that user is able to login with username and password and Title verified ", "User should be logged in successfully and Game should be launched ", "Logged in succesfully and Game is launched", "Pass");
						}
						else
						{
							tc01.detailsAppend("Check that user is not able to login with username and password", "User should be logged in successfully and "+GameTitle+" Game should be launched ", "Logged in succesfully and "+gameName+" is not launched. ", "Fail");

						}
						//to remove hand gesture
						cfnlib.funcFullScreen();			
						
												
						//time verification 
						tc01.detailsAppend("Verify that Time is getting displayed on the screen of the game "+GameTitle+"", "Time should be displayed on the screen", "", "");
						String GameverifyTime=cfnlib.verifyTime();

						if(GameverifyTime!=null)
						{
							tc01.detailsAppend(" Verify Time visible on base screen  ", "User should able to view time ", "Time is getting displayed on basescene of the game "+GameTitle+"", "pass");
						}
						else
						{
							tc01.detailsAppend("Verify Time is not visible on base screen", "User should not able to  view time on base screen ", " Time is not getting displayed on base scene of the game "+GameTitle+" ", "Fail");	
						}


						// verify spin visibility 
						tc01.detailsAppend("Verify the visibility of Spin Button on the screen", "Spin button should be displayed on the screen", "", "");
						String SpinVisible= cfnlib.verifySpinBeforeClick("Player","balance","","");

						if(SpinVisible!=null)
						{
							tc01.detailsAppend("Verify Spin button is visible on screen ", "User should able to view spin button ", "Spin button is displayed on basescene of the game "+gameName+" ", "pass");
						}

						else
						{
							tc01.detailsAppend("Verify spin button is visible on screen", "User should able to view spin button ", "Spin button is not being displayed on basescene of the game "+gameName+"", "fail");
						}

						// paylines verification 

						tc01.detailsAppend("Verify that the  paylines text exists on the screen ", "Psaylines should be visible on the screen", "", "");  
						int paylinesverification=cfnlib.paylinesExist();
						if(paylinesverification!=0)
						{
							tc01.detailsAppend("paylines  visible on base screen  ", "user should able to view paylines  ", +paylinesverification+ " Paylines is  visible on basescene", "pass");
						}
						else 
						{
							tc01.detailsAppend("paylines not visible on base screen  ", "user not  able to view paylines  ", "Paylines is not visible on basescene", "fail");
						}

						// verify balance 
						tc01.detailsAppend("Verify that credit is displayed on the screen", "Credits should be displayed on the screen", "", "");
						int verifycredit=Integer.parseInt(cfnlib.verifyCredit());
						if(verifycredit!=0)
						{
							tc01.detailsAppend("credit amount visible on base screen  ", "user should able credit amount  ", "Credit amount "+verifycredit+" is visible on screen", "pass");
						}
						else
						{
							tc01.detailsAppend("credit amount not visible on base screen  ", "user not  able to credit amount  ", "Credit amount "+verifycredit+"is not visible on screen", "fail");
						}

						//  Validating balance
						tc01.detailsAppend("Verify that balance is changing after every spin", "Balance should change after spin", "", "");
						int oldbalance=cfnlib.validate_credits_firstSpin();

						int newbalance=cfnlib.validate_credits_nextSpin();
						if(oldbalance!=newbalance){
							tc01.detailsAppend("Verify that balance changes after every spin", "Balance after spin  and before spin should not be equal", "Balance after spin "+newbalance+" and before spin "+oldbalance+" is not equal", "pass");
						}
						else
						{
							tc01.detailsAppend("Verify that balance changes after every spin", "Balance after spin and before spin should not be equal", "Balance after spin and before spin is equal", "fail");
						}

						Thread.sleep(3000);
						TestdroidImageRecognition findImage= new TestdroidImageRecognition(webdriver);
						findImage.findImageOnScreen("Logo");
						if(findImage!=null)
						{
							tc01.detailsAppend("Verify that Logo display correctly ", "Logo should display correctly.", "Logo has been displayed correctly", "pass");  
						}

						String logout= cfnlib.funcLogout();
						System.out.println("The logout title is "+logout);
						if(logout.trim()!=null&&logout.equalsIgnoreCase("Login"))
						{
							tc01.detailsAppend("Verify that user has successfully logged out ", "User should be successfully logged out", "User is Successfully logged out", "pass");
						}
						else 
						{
							tc01.detailsAppend("Verify that user has successfully logged out ", "User should be successfully logged out", "User is not Successfully logged out", "fail");
						}
					}
				}			    
			}
		}//TRY
		//-------------------Handling the exception---------------------//
		catch (Exception e) {
			/*ExceptionHandler eHandle=new ExceptionHandler(e,webdriver,Tc01);
			eHandle.evalException();*/
		e.printStackTrace();
		}
		finally
		{
			//Thread.sleep(5000);


			tc01.endReport();
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
	}
}