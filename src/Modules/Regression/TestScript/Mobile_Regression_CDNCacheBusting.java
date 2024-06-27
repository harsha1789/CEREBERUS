package Modules.Regression.TestScript;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 * Checks if the game has implemented Cache busting by verifying the assets downloaded against the GameAssets.js file entries.
 * It verifies for each language.
 * @author Havish
 *
 */
public class Mobile_Regression_CDNCacheBusting {
	Logger log = Logger.getLogger(Desktop_Regression_CDNCacheBusting.class.getName());
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
		ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();
		Mobile_HTML_Report tc01=new Mobile_HTML_Report(webdriver,DeviceName,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,gameName);

		MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, tc01, gameName);
		
		
		try{
			// Step 1
			if(webdriver!=null)
			{	

				String url=cfnlib.xpathMap.get("ApplicationURL");
				String obj=cfnlib.funcNavigate(url);
				if(obj!=null)
				{
					tc01.detailsAppend("Open Browser and Enter Lobby URL in address bar and click Enter", "Island Paradise Lobby should open", "Island Paradise lobby is Open", "Pass");
				}
				else
				{
					tc01.detailsAppend("Open browser and Enter Game URL in address bar and click Enter", "Island Paradise lobby should be displayed", "Island paradise is not displayed", "Fail");
				}
				// Step s1.2 /*Login to application and verify the game title*/

				tc01.detailsAppend("Verify features of the game", "Game should work properly", "", "");
				String password = Constant.PASSWORD;
				log.debug("The New username is =="+ userName);

				String GameTitle= cfnlib.loginToBaseScene(userName, password);		
				if(GameTitle!=null)
				{
					tc01.detailsAppend("Verify that the application should display Game Logo and game name in Base Scene in Practice Play", "Game logo and game name with base scene should display", "Game logo and game name with base scene displays", "Pass");
				}
				else {
					tc01.detailsAppend("Verify that the application should Logo and game name in Base scene in Practice Play", "Game logo and game name with base scene should display", "Game logo and game name with base scene is not display", "Fail");
				}
				
				//ToDo : Fix it here and desktop
				//ZensarQA
				//manifestFolder = new File("\\\\10.247.224.125\\m$\\mgsiis\\Games\\mgs\\"+GameName+"\\manifests\\");
				//ZensarQA2
				File manifestFolder = new File("\\\\10.247.225.15\\m$\\mgsiis\\Games\\mgs\\"+gameName+"\\manifests\\");
				//Zensardev2
				//manifestFolder = new File("\\\\10.247.224.236\\m$\\mgsiis\\Games\\mgs\\"+GameName+"\\manifests\\");
				//Zensardev
				//manifestFolder = new File("\\\\10.247.224.97\\m$\\mgsiis\\Games\\mgs\\"+GameName+"\\manifests\\");

				if(!manifestFolder.isDirectory()){
					tc01.detailsAppendNoScreenshot("To Check that resource is pulling from CDN", "All resources should pull from CDN", "Resource is not pulling from CDN", "Fail");
					throw new IllegalArgumentException(manifestFolder+" is no directory.");
				}
				List<String> fileList = new ArrayList<String>();
				File[] listOfFiles = manifestFolder.listFiles();

				for (File file : listOfFiles) {
					if (file.isFile()) {
						fileList.add(file.getName());
					}
				}

				Map<String, String> rowData2 = null;
				Map<String, String> rowData3 = null;
				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				int rowCount2 = excelpoolmanager.rowCount(testDataExcelPath, "LanguageCodes");
				for(int j=1;j<rowCount2;j++){
					//Step 2: To get the languages in MAP and load the language specific url
					rowData2 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",  j);
					String languageDescription = rowData2.get("Language").toString();
					String languageCode = rowData2.get("Language Code").toString().trim();
					tc01.detailsAppendFolder("Verify Game in Language " +languageCode+" ", " Application window should be displayed in " +languageDescription+"", "", "", languageCode);

					String fileName = "gameAssetManifest-"+languageCode+"-"; 
					String fileName1 = "gameAssetManifest-1136x640-"+languageCode+"-"; 

					String fileFullName = null;
					for(int n=0;n<fileList.size();n++){
						if(fileList.get(n).contains(fileName) || fileList.get(n).contains(fileName1)){
							fileFullName = fileList.get(n);
							break;
						}
					}

					String manifestFile = manifestFolder+"\\"+fileFullName;
					ArrayList<String> resources= cfnlib.readManifestFile(manifestFile);

					cfnlib.summaryScreen_Wait();
					//cfnlib.func_FullScreen();	
					Thread.sleep(40000);
					ArrayList<String> response = cfnlib.cacheBustingCDN(proxy);

					Map<String, String> found = new HashMap<String, String>();
					Map<String, String> notFound = new HashMap<String, String>();
					Map<String, String> notLoadingFromGcontent = new HashMap<String, String>();

					int resourcesSize = resources.size();
					int responseSize = response.size();

					log.debug("resource size" +resourcesSize);
					log.debug("response size" +responseSize);
					for(int i =0; i<resourcesSize; i++)
					{
						String strResource = resources.get(i);
						for(int k=0; k<responseSize; k++)
						{
							String strResponse = response.get(k);
							if(strResponse.contains(strResource)){
								found.put(strResource,strResponse);
								if(!strResponse.contains("gcontent.eu")){
									log.debug("Resource is not pulling from CDN" +strResource);
									tc01.detailsAppendNoScreenshot("To Check that resource is pulling from CDN", "All resources should pull from CDN", "Resource is not pulling from CDN: " +strResource+"", "Fail");
									notLoadingFromGcontent.put(strResource,strResponse);
								}
								break;
							}
							if(k==responseSize-1){
								log.debug("Resource not found in game but available in Manifests File " +strResource);
								//Tc01.details_append_NoScreenshot("To Check that all required resources are loading in game", "All resources mentioned in Manifest file should load in game", "Resource not found in game but available in Manifests File: " +strResource+"", "Fail");
								notFound.put(strResource,strResponse);
							}
						}
					}
					tc01.detailsAppendNoScreenshot("To Check total number of resources available for loading", "All resources mentioned in Manifest file for the respective resolutoin should load in game", "Total Number of resource after ignoring assests of other resoultion: " +(found.size()+notFound.size())+"", "Pass");
					tc01.detailsAppendNoScreenshot("To Check total number of resources found", "All resources mentioned in Manifest file should load in game", "Resources found in Game: " +found.size()+"", "Pass");
					tc01.detailsAppendNoScreenshot("To Check total number of resources not found", "All resources mentioned in Manifest file should load in game", "Resources not found in Game: " +notFound.size()+"", "Fail");
					if(notLoadingFromGcontent.size()==0)
						tc01.detailsAppendNoScreenshot("To Check total number of resources not loading from Gcontent.eu", "All resources should load from gcontent.eu", "All Resources loaded in game are loading from Gcontent.eu", "Pass");					
					else
						tc01.detailsAppendNoScreenshot("To Check total number of resources not loading from Gcontent.eu", "All resources should load from gcontent.eu", "Resources loaded in game but not loading from Gcontent.eu: " +notLoadingFromGcontent.size()+"", "Fail");
					log.debug("Resource found in Game" +found.size());
					log.debug("Resource not found in Game" +notFound.size());	
					//for updating language in URL and then Refresh 
					if (j + 1 != rowCount2){
						rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",  j+1);
						languageDescription = rowData3.get("Language").toString();
						String languageCode2 = rowData3.get("Language Code").toString().trim();

						String currentUrl = webdriver.getCurrentUrl();
						String url_new = currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+languageCode2);
						webdriver.navigate().to(url_new);
						String error=cfnlib.xpathMap.get("Error");
						if(cfnlib.isElementPresent(error))
						{
							tc01.detailsAppendFolder("Verify the Language code is " +languageCode2+" ", " Application window should be displayed in " +languageDescription+"", "", "", languageCode2);
							tc01.detailsAppendFolder("Verify that any error is coming","error must not come","error is coming", "fail", languageCode2);
							if (j + 2 != rowCount2){
								rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",  j+2);
								String languageCode3 = rowData3.get("Language Code").toString().trim();

								currentUrl = webdriver.getCurrentUrl();
								url_new = currentUrl.replaceAll("LanguageCode="+languageCode2, "LanguageCode="+languageCode3);
								webdriver.navigate().to(url_new);
							}
							j++;
						}
					}
				}
			}
		}
		//-------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		finally
		{
			tc01.endReport();
			webdriver.close();
			webdriver.quit();
			proxy.abort();
			Thread.sleep(1000);
		}	
	}
}