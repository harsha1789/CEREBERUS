package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 * This script traverse and capture all the screen shots related to free Spins
 * For All Feactures. It reads the test data excel sheet for configured languages.
 * 
 * @author TS64283
 */

public class Mobile_Regression_Language_Verification_Compatibility_All_FreeSpin {

	Logger log = Logger.getLogger(Mobile_Regression_Language_Verification_Compatibility_All_FreeSpin.class.getName());

	public ScriptParameters scriptParameters;

	public void script() throws Exception {

		String mstrTC_Name = scriptParameters.getMstrTCName();
		String mstrTC_Desc = scriptParameters.getMstrTCDesc();
		String mstrModuleName = scriptParameters.getMstrModuleName();
		BrowserMobProxyServer proxy = scriptParameters.getProxy();
		String startTime = scriptParameters.getStartTime();
		String filePath = scriptParameters.getFilePath();
		AppiumDriver<WebElement> webdriver = scriptParameters.getAppiumWebdriver();
		String DeviceName = scriptParameters.getDeviceName();
		String userName = scriptParameters.getUserName();
		String framework = scriptParameters.getFramework();
		// String deviceLockKey=scriptParameters.getDeviceLockKey();
		String gameName = scriptParameters.getGameName();
		// int checkedOutDeviceNum=scriptParameters.getCheckedOutDeviceNum();
		String osPlatform = scriptParameters.getOsPlatform();
		String osVersion = scriptParameters.getOsVersion();

		String Status = null;
		int mintDetailCount = 0;
		// int mintSubStepNo=0;
		int mintPassed = 0;
		int mintFailed = 0;
		int mintWarnings = 0;
		int mintStepNo = 0;
		String urlNew = null;

		ExcelDataPoolManager excelpoolmanager = new ExcelDataPoolManager();
		Mobile_HTML_Report language = new Mobile_HTML_Report(webdriver, DeviceName, filePath, startTime, mstrTC_Name,
				mstrTC_Desc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings, mintStepNo, Status,
				gameName);
		log.info("Framework" + framework);
		MobileFunctionLibraryFactory mobileFunctionLibraryFactory = new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib = mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,
				language, gameName);
		WebDriverWait wait;
		CommonUtil util = new CommonUtil();
		int mid = Integer.parseInt(TestPropReader.getInstance().getProperty("MID"));
		int cid = Integer.parseInt(TestPropReader.getInstance().getProperty("CIDMobile"));
		List<String> copiedFiles = new ArrayList<>();
		RestAPILibrary apiobj = new RestAPILibrary();
		cfnlib.setOsPlatform(osPlatform);
		cfnlib.setOsVersion(osVersion);

		try {

			if (webdriver != null) {

				Map<String, String> rowData2 = null;
				Map<String, String> rowData3 = null;
				wait = new WebDriverWait(webdriver, 60);

				// If you want run whole script in LANDSCAPE mode then add Orientation =
				// LANDSCAPE in testdata file
				if ("LANDSCAPE".equalsIgnoreCase(cfnlib.xpathMap.get("Orientation"))) {
					String context = webdriver.getContext();
					webdriver.context("NATIVE_APP");
					webdriver.rotate(ScreenOrientation.LANDSCAPE);
					webdriver.context(context);
				}

				String testDataExcelPath = TestPropReader.getInstance().getProperty("TestData_Excel_Path");

				String strFileName = TestPropReader.getInstance().getProperty("MobileFreeSpinsAllFeacturePath");

				File testDataFile = new File(strFileName);

				userName = util.randomStringgenerator();

				if (util.copyFilesToTestServer(mid, cid, testDataFile, userName, copiedFiles)) {

					log.debug("Test data is copy in test Server for Username for all freespin =" + userName);

					String url = cfnlib.xpathMap.get("ApplicationURL");

					String LaunchURl = url.replaceFirst("\\busername=.*?(&|$)", "username=" + userName + "$1");

					log.debug("url = " + LaunchURl);

					cfnlib.loadGame(LaunchURl);

					cfnlib.threadSleep(8000);

					if (framework.equalsIgnoreCase("Force")) {
						cfnlib.setNameSpace();
					}

					int rowCount2 = excelpoolmanager.rowCount(testDataExcelPath, "LanguageCodes");

					for (int j = 1; j < rowCount2; j++) {

						log.debug("Total number of Languages configured" + rowCount2);

						// Step 2: To get the languages in MAP and load the language specific url

						rowData2 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j);
						String languageDescription = rowData2.get("Language").toString();
						String languageCode = rowData2.get("Language Code").toString().trim();

						if ("Yes".equalsIgnoreCase(cfnlib.xpathMap.get("CntBtnNoXpath"))) {

							cfnlib.elementWait("return " + cfnlib.xpathMap.get("CntBtnNoXpathSatus"), true);
							cfnlib.funcFullScreen();
							Thread.sleep(2000);
							cfnlib.closeOverlay();

						}

						cfnlib.waitForSpinButton();

						cfnlib.spinclick();

						Thread.sleep(2000);

						// cfnlib.elementWait("return " + cfnlib.xpathMap.get("FreeScrinVisible"),
						// true);

						boolean UnlockStatus = cfnlib.UnlockAllFreeSpin();

						if (UnlockStatus) {

							log.info("Screenshot for  ALL Freespin Feature Unlocking Status");

							language.detailsAppend(
									"Verify Different Languages on Free spin entry screen All Feature is Unlocked",
									"Free spin entry screen should display All Feature Unlocked as per respective language",
									"", "");

							language.detailsAppendFolder(
									"Verify that the application should display Free spin entry screen status  All Feature Unlocked in "
											+ languageCode + " ",
									"Free spin entry screen should display in " + languageDescription
											+ "With All Feature Unlocked" + " ",
									"Free spin entry screen displays in " + languageDescription + " "
											+ "With All Feature Unlocked ",
									"Pass", languageCode);

							cfnlib.funcLandscape();

							Thread.sleep(1000);

							language.detailsAppendFolder(
									"Verify that the application should display Free spin entry screen status All Feature Unlocked in Landscape Mode "
											+ languageCode + " ",
									"Free spin entry screen should display in " + languageDescription
											+ "With All Feature Unlocked in Lanscape Mode" + " ",
									"Free spin entry screen displays in " + languageDescription + " "
											+ "With All Feature Unlocked ",
									"Pass", languageCode);

							cfnlib.funcPortrait();

							Thread.sleep(2000);

							log.info("Screenshot done  ALL Freespin Feature Unlocking Status check");

							for (int i = 1; i <= 4; i++) {
								String FreeSpinEntryScreen;
								String b2;

								language.detailsAppend("Verify the Different Languages in Free Spin For ",
										"Free Spin window should display as per respective language", "", "");

								FreeSpinEntryScreen = cfnlib.xpathMap.get("FreeSpinEntryScreen");
								b2 = cfnlib.entryScreen_Wait_Upadated_FreeSpin(FreeSpinEntryScreen);

								if (b2.equalsIgnoreCase("freeSpin")) {

									cfnlib.clickBonusSelection(i);
									log.info("Freespin continue button is clicked and awaiting for spin Button");
								} else {

									System.out.println("FreeSpinEntry Scrren Not displaying");
									log.error("FreeSpinEntry Scrren Not displaying");

								}

								Thread.sleep(1000);

								cfnlib.FSSceneLoading();

								webdriver.navigate().refresh();

								Thread.sleep(3000);

								cfnlib.elementWait("return " + cfnlib.xpathMap.get("CntBtnNoXpathSatus"), true);

								Thread.sleep(1000);

								cfnlib.funcFullScreen();

								Thread.sleep(2000);

								cfnlib.closeOverlay();

								cfnlib.FSSceneLoading();

								language.detailsAppendFolder(
										"Verify that the application should display Free Spin scene in " + languageCode
												+ " " + "For " + i + " " + " Feature ",
										"Free Spin scene should display in " + languageDescription + " " + "For " + i
												+ " " + " Feature ",
										"Free Spin scene displays in " + languageDescription + " " + "For " + +i + " "
												+ " Feature ",
										"Pass", languageCode);

								cfnlib.funcLandscape();

								Thread.sleep(1000);

								language.detailsAppendFolder(
										"Verify that the application should display Free Spin scene in Landscape Mode"
												+ languageCode + " " + "For " + i + " " + " Feature ",
										"Free Spin scene should display in " + languageDescription + " " + "For " + i
												+ " " + " Feature ",
										"Free Spin scene displays in " + languageDescription + " " + "For " + i + " "
												+ " Feature ",
										"Pass", languageCode);

								cfnlib.funcPortrait();

								language.detailsAppend("Verify the Different Languages in Free Spin Summary Screen",
										"Free Spin Summary Screen should display as per respective language", "", "");

								log.info("Freespin continue button is about to click");
								cfnlib.FS_continue();
								log.info("Freespin continue button is clicked and awaiting for Summary Screen");

								Thread.sleep(1000);

								if (i > 1) {

									Thread.sleep(i * 2500);
								}

								cfnlib.waitSummaryScreen();

								language.detailsAppendFolder(
										"Verify that the application should display Free Spin Summary in "
												+ languageCode + " " + "For " + i + " " + " Feature ",
										"Free Spin Summary should display in " + languageDescription + " " + "For " + i
												+ " " + " Feature ",
										"Free Spin Summary displays in " + languageDescription + " " + "For " + i + " "
												+ " Feature ",
										"Pass", languageCode);

								cfnlib.funcLandscape();

								webdriver.navigate().refresh();

								Thread.sleep(3000);

								cfnlib.elementWait("return " + cfnlib.xpathMap.get("CntBtnNoXpathSatus"), true);

								Thread.sleep(2000);

								cfnlib.closeOverlay();

								cfnlib.waitSummaryScreen();

								language.detailsAppendFolder(
										"Verify that the application should display Free Spin Summary in "
												+ languageCode + " " + "For " + i + " " + " Feature ",
										"Free Spin Summary should display in " + languageDescription + " " + "For " + i
												+ " " + " Feature ",
										"Free Spin Summary displays in " + languageDescription + " " + "For " + i + " "
												+ " Feature ",
										"Pass", languageCode);

								cfnlib.funcPortrait();

								Thread.sleep(3000);

								cfnlib.funcFullScreen();

								cfnlib.waitForSpinButton();

								if (i <= 3) {
									cfnlib.spinclick();

									Thread.sleep(6000);

								}
							}
						} else {

							System.out.println("Feacture is not unlocked");

						}
						// Language Change logic:: for updating language in URL and then Refresh

						if (j + 1 != rowCount2) {
							rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes", j + 1);
							languageDescription = rowData3.get("Language").trim();
							String languageCode2 = rowData3.get("Language Code").trim();

							String currentUrl = webdriver.getCurrentUrl();
							if (currentUrl.contains("LanguageCode"))
								urlNew = currentUrl.replaceAll("LanguageCode=" + languageCode,
										"LanguageCode=" + languageCode2);
							else if (currentUrl.contains("languagecode"))
								urlNew = currentUrl.replaceAll("languagecode=" + languageCode,
										"languagecode=" + languageCode2);
							else if (currentUrl.contains("languageCode"))
								urlNew = currentUrl.replaceAll("languageCode=" + languageCode,
										"languageCode=" + languageCode2);

							log.info("Privious Language Code in Url= " + languageCode + "\nNext Language code= "
									+ languageCode2 + "\nNew Url after replacing language code:" + urlNew);
							cfnlib.loadGame(urlNew);
							String error = cfnlib.xpathMap.get("Error");

							if (cfnlib.isElementPresent(error)) {
								language.detailsAppendFolder("Verify the Language code is " + languageCode2 + " ",
										" Application window should be displayed in " + languageDescription + "", "",
										"", languageCode2);
								language.detailsAppendFolder("Verify that any error is coming",
										"General error should not display", "General Error is Diplay", "fail",
										languageCode2);

								if (j + 2 != rowCount2) {
									rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",
											j + 2);
									String languageCode3 = rowData3.get("Language Code").toString().trim();
									currentUrl = webdriver.getCurrentUrl();
									if (currentUrl.contains("LanguageCode"))
										urlNew = currentUrl.replaceAll("LanguageCode=" + languageCode2,
												"LanguageCode=" + languageCode3);
									else if (currentUrl.contains("languagecode"))
										urlNew = currentUrl.replaceAll("languagecode=" + languageCode2,
												"languagecode=" + languageCode3);
									else if (currentUrl.contains("languageCode"))
										urlNew = currentUrl.replaceAll("languageCode=" + languageCode2,
												"languageCode=" + languageCode3);

									cfnlib.loadGame(urlNew);
								}

								j++;
							}
						}
					}
				} else {
					log.debug("Unable to copy FreeSpin test data file on the environment hence skipping execution");
					language.detailsAppend("", "",
							"Unable to copy FreeSpin test data on server,hence skipping futher execution", "fail");
				}
			}
			// -------------------Handling the exception---------------------//
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (!copiedFiles.isEmpty()) {
				if (TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
					util.deleteBluemesaTestDataFiles(mid, cid, copiedFiles);
				else
					apiobj.deleteAxiomTestDataFiles(copiedFiles);
			}

			language.endReport();
			webdriver.close();
			webdriver.quit();
			// proxy.abort();
			Thread.sleep(3000);
		}
	}

}
