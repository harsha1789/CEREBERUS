package Modules.Regression.TestScript;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.DataBaseFunctions;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import net.lightbody.bmp.BrowserMobProxyServer;


/**
 * This script traverse and capture all the screen shots related to free games.
 * It reads the test data excel sheet for configured languages.
 * @author AK47374
 *
 */
public class Mobile_Regression_Language_Verification_FreeSpinsCS {
	public static Logger log = Logger.getLogger(Mobile_Regression_Language_Verification_FreeSpinsCS.class.getName());
	public ScriptParameters scriptParameters;
	
	public void script() throws Exception{

		String mstrTCName=scriptParameters.getMstrTCName();
		String mstrTCDesc=scriptParameters.getMstrTCDesc();
		String mstrModuleName=scriptParameters.getMstrModuleName();
		BrowserMobProxyServer proxy=scriptParameters.getProxy();
		String startTime=scriptParameters.getStartTime();
		String filePath=scriptParameters.getFilePath();
		AppiumDriver<WebElement> webdriver=scriptParameters.getAppiumWebdriver();
		String deviceName=scriptParameters.getDeviceName();
		String userName=scriptParameters.getUserName();
		String framework=scriptParameters.getFramework();
		String gameName=scriptParameters.getGameName();


		String status=null;
		int mintDetailCount=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		String urlNew=null;
		String languageDescription;
		String languageCode;
		ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();
		Mobile_HTML_Report language=new Mobile_HTML_Report(webdriver,deviceName,filePath,startTime,mstrTCName,mstrTCDesc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,status,gameName);

		log.info("Framework"+framework);
		MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, language, gameName);
		
		CommonUtil util = new CommonUtil();
		userName=util.createRandomUser();
		cfnlib.setOsPlatform(scriptParameters.getOsPlatform());
		int mid=Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int  cid=Integer.parseInt(TestPropReader.getInstance().getProperty("CIDMobile"));
		List<String> copiedFiles=new ArrayList<>();
		RestAPILibrary apiobj=new RestAPILibrary();



		
		try{
			// Step 1
			if(webdriver!=null)
			{		
				Map<String, String> rowData2 = null;
				Map<String, String> rowData3 = null;

				String strFileName="./"+gameName+"/TestData/"+this.getClass().getSimpleName()+".testdata";
				File testDataFile=new File(strFileName);
				userName=util.createRandomUser();
				userName=util.randomStringgenerator();
				boolean istestDataCopied = 	util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles);
				if(istestDataCopied)
				{
				String url = cfnlib.xpathMap.get("ApplicationURL");
				String launchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
				System.out.println(launchURl);
				webdriver.navigate().to(launchURl);
				log.debug("navigated to url ");
				Thread.sleep(20000);
				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
			
							
					//Set Max bet
					Thread.sleep(1000);
					cfnlib.waitForSpinButton();
					//cfnlib.funcFullScreen();
				//	cfnlib.func_FullScreen(language);
				
					
					cfnlib.waitForSpinButton();				
					
					
					String freeSpinEntryScreenID = cfnlib.xpathMap.get("freeSpinEntryScreenID");
					// Reading the language code into the list map
					List<Map> list= util.readLangList();
					int rowCount2 =list.size();
					log.debug("Total number of Languages configured"+rowCount2);
					if(cfnlib.xpathMap.get("featureScreenPresnt").equalsIgnoreCase("yes"))
						cfnlib.newFeature();	
					
					for(int j=1;j<rowCount2;j++){
						
						//Step 2: To get the languages in MAP and load the language specific url
						rowData2 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j);
						languageDescription = rowData2.get(Constant.LANGUAGE).trim();
						languageCode = rowData2.get(Constant.LANG_CODE).trim();
						System.out.println("Language Code:"+languageCode);
						
						
						language.detailsAppendFolder("Verify Game in Language " +languageCode+" ", " Application window should be displayed in " +languageDescription+"", "", "", languageCode);
						
						if(j!=1)
						{
							cfnlib.func_FullScreen(language);
						}
						/*cfnlib.spinclick(); 
						Thread.sleep(1500);
						new WebDriverWait(webdriver, 150).until(ExpectedConditions.visibilityOfElementLocated(By.id(freeSpinEntryScreenID)));
		
						]language.detailsAppendFolder("Verify that the application should display Free Entry Summary in " + languageCode + " ","Free Spin Summary should display in " + languageDescription + " ","Free Spin Summary displays in portrait mode " + languageDescription + " ", "Pass", languageCode);
						cfnlib.funcLandscape();
						language.detailsAppendFolder("Verify that the application should display Free Entry Summary in " + languageCode + " ","Free Spin Summary should display in " + languageDescription + " ","Free Spin Summary displays in portrait mode " + languageDescription + " ", "Pass", languageCode);
						cfnlib.funcPortrait();*/
						cfnlib.freeSpinWithAddressBar(language,languageDescription,languageCode);				
						
						// for updating language in URL and then Refresh
						if (j + 1 != rowCount2){
							rowData3 = excelpoolmanager.readExcelByRow(Constant.TESTDATA_EXCEL_PATH, "LanguageCodes", j+1);
							languageDescription = rowData3.get("Language").toString();
							String languageCode2 = rowData3.get("Language Code").toString().trim();

							String currentUrl = webdriver.getCurrentUrl();
							if(currentUrl.contains("LanguageCode"))
								urlNew= currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+languageCode2);
							else if(currentUrl.contains("languagecode"))
								urlNew = currentUrl.replaceAll("languagecode="+languageCode, "languagecode="+languageCode2);

							
							log.debug(urlNew);
							webdriver.navigate().to(urlNew);
							String error=cfnlib.xpathMap.get("Error");
							
							if(cfnlib.isElementPresent(error))
							{
								language.detailsAppendFolder("Verify the Language code is " +languageCode2+" ", " Application window should be displayed in " +languageDescription+"", "", "", languageCode2);
								language.detailsAppendFolder("Verify that any error is coming","error must not come","error is coming", "fail", languageCode2);
								if (j + 2 != rowCount2){
									rowData3 = excelpoolmanager.readExcelByRow(Constant.TESTDATA_EXCEL_PATH, "LanguageCodes", j+2);
									String languageCode3 = rowData3.get("Language Code").toString().trim();
									currentUrl = webdriver.getCurrentUrl();
									urlNew = currentUrl.replaceAll("LanguageCode="+languageCode2, "LanguageCode="+languageCode3);
									webdriver.navigate().to(urlNew);
								}
								j++;
							}
						}
					}
				
				}
				
				
			}
		}
		//-------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error(e.getMessage(),e);
			cfnlib.evalException(e);
		}
		finally
		{
			language.endReport();
			if(!copiedFiles.isEmpty())
			{
				if(TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
					util.deleteBluemesaTestDataFiles(mid,cid,copiedFiles);
				else
					apiobj.deleteAxiomTestDataFiles(copiedFiles);
			}
			webdriver.close();
			webdriver.quit();
			//proxy.abort();
			Thread.sleep(3000);
		}	
	}
}