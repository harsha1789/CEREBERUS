package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 * Checks if the game has implemented Cache busting by verifying the assets downloaded against the GameAssets.js file entries.
 * It verifies for each language.
 * @author Havish
 *
 */


public class Desktop_Regression_CDNCacheBusting {
	Logger log = Logger.getLogger(Desktop_Regression_CDNCacheBusting.class.getName());
	public ScriptParameters scriptParameters;
	

	public void script() throws Exception {
		String mstrTC_Name=scriptParameters.getMstrTCName();
		String mstrTC_Desc=scriptParameters.getMstrTCDesc();
		String mstrModuleName=scriptParameters.getMstrModuleName();
		WebDriver webdriver=scriptParameters.getDriver();
		BrowserMobProxyServer proxy=scriptParameters.getProxy();
		String startTime=scriptParameters.getStartTime();
		String filePath=scriptParameters.getFilePath();
		String userName=scriptParameters.getUserName();
		String browserName=scriptParameters.getBrowserName();
		String framework=scriptParameters.getFramework();
		String gameName=scriptParameters.getGameName();
		String languageDescription=null;
		String languageCode=null;
		String status=null;
		String gameTitle;
		
		String password;
		
		int mintDetailCount=0;
		//int mintSubStepNo=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		
		File manifestFolder;
		String manifestFile=null;
		ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();
		Desktop_HTML_Report basescene = new Desktop_HTML_Report(webdriver, browserName, filePath, startTime,
				mstrTC_Name, mstrTC_Desc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings,
				mintStepNo, status, gameName);
		
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, basescene, gameName);

		try {
			if (webdriver != null) {
				password = Constant.PASSWORD;
				String url = cfnlib.XpathMap.get("ApplicationURL");
				String obj = cfnlib.func_navigate(url);
				if (obj != null) {
					basescene.detailsAppend("Open browser and Enter game URL in address bar and click Enter",
							"Island Paradise Lobby should be displayed", "Island Paradise lobby is displayed", "Pass");
				} else {
					basescene.detailsAppend("Open browser and Enter Game URL in address bar and click Enter",
							"Island Paradise lobby should be displayed", "Island paradise is not displayed", "Fail");
				}

				basescene.detailsAppend("Verify user is able to login to the Game " + gameName + "",
						"User should be able to Login " + gameName + "", "", "");

				log.debug("The New username is == " + userName);
				gameTitle = cfnlib.Func_LoginBaseScene(userName, password);
				if (gameTitle.trim() != null) {
					basescene.detailsAppend(
							"Check that user is able to login with username and password and  Title verified ",
							"User should be logged in successfully and " + gameName + " Game should be launched ",
							"Logged in succesfully and " + gameName + " is launched. ", "Pass");
				} else {
					basescene.detailsAppend("Check that user is not able to login with username and password",
							"User should be logged in successfully and " + gameName + " Game should be launched ",
							"Logged in succesfully and " + gameName + " is not launched. ", "Fail");
				}

				manifestFolder = new File("\\\\" + TestPropReader.getInstance().getProperty("Casinoas1IP")
						+ "\\m$\\mgsiis\\Games\\mgs\\" + gameName + "\\manifests\\");

				if (!manifestFolder.isDirectory()) {
					basescene.detailsAppendNoScreenshot("To Check that resource is pulling from CDN",
							"All resources should pull from CDN", "Resource is not pulling from CDN", "Fail");
					throw new IllegalArgumentException(manifestFolder + " is no directory.");
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
				for (int j = 1; j < rowCount2; j++) {
					// Step 2: To get the languages in MAP and load the language
					// specific url
					rowData2 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j);
					 languageDescription = rowData2.get("Language").trim();
					languageCode = rowData2.get("Language Code").trim();
					basescene.detailsAppendFolder("Verify Game in Language " + languageCode + " ",
							" Application window should be displayed in " + languageDescription + "", "", "",
							languageCode);

					String fileName = "gameAssetManifest-" + languageCode + "-";
					String fileName1 = "gameAssetManifest-1024x768-" + languageCode + "-";
					String fileFullName = null;
					for (int n = 0; n < fileList.size(); n++) {
						if (fileList.get(n).contains(fileName) || fileList.get(n).contains(fileName1)) {
							fileFullName = fileList.get(n);
							break;
						}
					}

					manifestFile = manifestFolder + "\\" + fileFullName;
					ArrayList<String> resources = cfnlib.readManifestFile(manifestFile);

					cfnlib.taketoGame();

					// webdriver.manage().timeouts().implicitlyWait(60,
					// TimeUnit.SECONDS);
					Thread.sleep(20000);
					ArrayList<String> response = cfnlib.cacheBustingCDN(proxy);

					Map<String, String> found = new HashMap<String, String>();
					Map<String, String> notFound = new HashMap<String, String>();
					Map<String, String> notLoadingFromGcontent = new HashMap<String, String>();

					int resourcesSize = resources.size();
					int responseSize = response.size();

					log.debug("resource size: " + resourcesSize);
					log.debug("response size: " + responseSize);
					for (int i = 0; i < resourcesSize; i++) {
						String strResource = resources.get(i);
						for (int k = 0; k < responseSize; k++) {
							String strResponse = response.get(k);
							if (strResponse.contains(strResource)) {
								found.put(strResource, strResponse);
								if (!strResponse.contains("gcontent.eu")) {
									log.debug("Resource is not pulling from CDN" + strResource);
									basescene.detailsAppendNoScreenshot("To Check that resource is pulling from CDN",
											"All resources should pull from CDN",
											"Resource is not pulling from CDN: " + strResource + "", "Fail");
									notLoadingFromGcontent.put(strResource, strResponse);
								}
								break;
							}
							if (k == responseSize - 1) {
								log.debug("Resource not found in game but available in Manifests File " + strResource);
								// basescene.details_append_NoScreenshot("To
								// Check that all required resources are loading
								// in game", "All resources mentioned in
								// Manifest file should load in game", "Resource
								// not found in game but available in Manifests
								// File: " +strResource+"", "Fail");
								notFound.put(strResource, strResponse);
							}
						}
					}
					basescene.detailsAppendNoScreenshot("To Check total number of resources available for loading",
							"All resources mentioned in Manifest file for the respective resolutoin should load in game",
							"Total Number of resource after ignoring assests of other resoultion: "
									+ (found.size() + notFound.size()) + "",
							"Pass");
					basescene.detailsAppendNoScreenshot("To Check total number of resources found",
							"All resources mentioned in Manifest file should load in game",
							"Resources found in Game: " + found.size() + "", "Pass");
					basescene.detailsAppendNoScreenshot("To Check total number of resources not found",
							"All resources mentioned in Manifest file should load in game",
							"Resources not found in Game: " + notFound.size() + "", "Fail");
					if (notLoadingFromGcontent.size() == 0)
						basescene.detailsAppendNoScreenshot(
								"To Check total number of resources not loading from Gcontent.eu",
								"All resources should load from gcontent.eu",
								"All Resources loaded in game are loading from Gcontent.eu", "Pass");
					else
						basescene.detailsAppendNoScreenshot(
								"To Check total number of resources not loading from Gcontent.eu",
								"All resources should load from gcontent.eu",
								"Resources loaded in game but not loading from Gcontent.eu: "
										+ notLoadingFromGcontent.size() + "",
								"Fail");
					log.debug("Resource found in Game: " + found.size());
					log.debug("Resource not found in Game: " + notFound.size());

					// for updating language in URL and then Refresh
					if (j + 1 != rowCount2) {
						//String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
						rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",
								j + 1);
						String languageCode2 = rowData3.get("Language Code").trim();

						String currentUrl = webdriver.getCurrentUrl();
						String url_new = currentUrl.replaceAll("LanguageCode=" + languageCode,
								"LanguageCode=" + languageCode2);
						webdriver.navigate().to(url_new);
						String error = cfnlib.XpathMap.get("Error");
						if (cfnlib.isElementPresent(error)) {
							basescene.detailsAppendFolder("Verify the Language code is " + languageCode2 + " ",
									" Application window should be displayed in " + languageDescription + "", "", "",
									languageCode2);
							basescene.detailsAppendFolder("Verify that any error is coming", "error must not come",
									"error is coming", "fail", languageCode2);
							if (j + 2 != rowCount2) {
								rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath,
										"LanguageCodes", j + 2);
								String languageCode3 = rowData3.get("Language Code").toString().trim();

								currentUrl = webdriver.getCurrentUrl();
								url_new = currentUrl.replaceAll("LanguageCode=" + languageCode2,
										"LanguageCode=" + languageCode3);
								webdriver.navigate().to(url_new);
							}
							j++;
						}
					}
				}
			}
		}
		// -------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			basescene.endReport();
			webdriver.close();
			webdriver.quit();
			proxy.abort();
			Thread.sleep(1000);
		}
	}
}
