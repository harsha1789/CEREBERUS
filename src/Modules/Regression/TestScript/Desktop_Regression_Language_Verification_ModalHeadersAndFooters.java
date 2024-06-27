package Modules.Regression.TestScript;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;


 /**This Script need test data for modal header and footer
 * This Script traverse through all the Modal Headers and take the screen shot of the all the modal headers and footers.
 * It reads the test data excel sheet for configured languages.
 * 	//Step 1 : Read all the modal headers from test data
 * 	//Step 2 : call Spin Method
	//Step 3 : Check if any of the modal header occurred
	//Step 4 : if any of the modal header occurred,take the screen shot and then reduce the count
	//Step 5 : check if any modal header screen shot is pending
	//Step 6 : Repeat the step 2 if any of the headers left
						
 * */

public class Desktop_Regression_Language_Verification_ModalHeadersAndFooters {
	Logger log = Logger.getLogger(Desktop_Regression_Language_Verification_ModalHeadersAndFooters.class.getName());
	public ScriptParameters scriptParameters;
	public void script() throws Exception {
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
		String languageDescription=null;
		String languageCode=null;
		String status=null;
		int mintDetailCount=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		String urlNew=null;
		ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();
		Desktop_HTML_Report headFooterReport = new Desktop_HTML_Report(webdriver, browserName, filePath, startTime, mstrTCName,
				mstrTCDesc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings, mintStepNo, status,
				gameName);
		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,headFooterReport, gameName);


		CommonUtil util = new CommonUtil();
		

		try {
			if (webdriver != null) 
			{
				Map<String, String> rowData2 = null;
				Map<String, String> rowData3 = null;
				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				WebDriverWait wait=new WebDriverWait(webdriver,60);

				String url = cfnlib.XpathMap.get("ApplicationURL");
				String launchURl = url.replaceFirst("\\busername=.*?(&|$)", "username="+userName+"$1");
				log.info("url = " +launchURl);
				cfnlib.loadGame(launchURl);
				log.debug("navigated to url ");

				if(framework.equalsIgnoreCase("Force")){
					cfnlib.setNameSpace();
				}
				
				int rowCount2 = excelpoolmanager.rowCount(testDataExcelPath, "LanguageCodes");
				// Reading the language code in to list map
				
				List<Map> list= util.readLangList();

				log.debug("Total number of Languages configured="+rowCount2);
				headFooterReport.detailsAppend("Verify the modal Header and footer in game", "", "", "");
				for (int j = 1; j < rowCount2; j++) {
					cfnlib.newFeature();
					Thread.sleep(1000);
					cfnlib.waitForSpinButton();
					Thread.sleep(1000);
					//Step 2: To get the languages in MAP and load the language specific url
					rowData2 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",  j);
					languageDescription = rowData2.get("Language").toString();
					languageCode = rowData2.get("Language Code").toString().trim();
					System.out.println("Before new feature");
					//cfnlib.newFeature();
					
					
					// Capture screenshot for Win History Component.Win history is available in Forcegame only not in cs games
					if((!gameName.contains("Scratch"))&& framework.equalsIgnoreCase("Force"))
					{
				    
						Map<String, String> modalHeadersMap = cfnlib.getAllTheModalHeaders();
						
						while(modalHeadersMap.size()>0){
							
							cfnlib.spinclick();					
							Thread.sleep(2000);
							cfnlib.waitForSpinButtonstop();
							Thread.sleep(2000);
							String headerKey= cfnlib.checkIfAnyModelHeaderOccured(modalHeadersMap);
							if(headerKey!=null){
								headFooterReport.detailsAppendFolder("ModalHeader with "+headerKey, "Modal Header Screen should be display", "Modal Header is displayed in " +languageDescription+ " language", "Pass", languageCode);
								modalHeadersMap.remove(headerKey);
							}
							
						}
						Map<String, String> modalFooterMap = cfnlib.getAllTheModalFooter();
						System.out.println(modalFooterMap.size());
						Thread.sleep(3000);
						if(TestPropReader.getInstance().getProperty("IsFreeSpinAvailable").equalsIgnoreCase("yes"))
						{
							String FreeSpinEntryScreen = cfnlib.XpathMap.get("FreeSpinEntryScreen");
							do{
								cfnlib.spinclick();
								Thread.sleep(2000);
							}while(!(cfnlib.isFreeSpinTriggered()));
							headFooterReport.detailsAppendFolderOnlyScreenShot(languageCode);
			
							String b2 = cfnlib.entryScreen_Wait(FreeSpinEntryScreen);
							log.info("FreeSpinEntryScreen response"+b2);
							if("yes".equals(cfnlib.XpathMap.get("isFreeSpinSelectionAvailable")))
							{
								String strfreeSpinSelectionCount=cfnlib.XpathMap.get("NoOfFreeSpinBonusSelection");
								int freeSpinSelectionCount = (int) Double.parseDouble(strfreeSpinSelectionCount);

								if(Constant.STORMCRAFT_CONSOLE.equalsIgnoreCase(cfnlib.XpathMap.get("TypeOfGame")))
								{
									wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cfnlib.XpathMap.get("OneDesign_NewFeature_ClickToContinue"))));
									cfnlib.newFeature();
								}
								cfnlib.clickBonusSelection(freeSpinSelectionCount);
							}
							else{
								if( TestPropReader.getInstance().getProperty("IsProgressiveGame").equalsIgnoreCase("Yes"))
								{
									
									cfnlib.clickToContinue();
									Thread.sleep(3000);
								}
								else{
									cfnlib.clickToContinue();
								}
							}
							
							while(modalFooterMap.size()>0){
							
							String headerKey= cfnlib.checkIfAnyModelHeaderOccured(modalFooterMap);
							if(headerKey!=null){
								Thread.sleep(3000);
								headFooterReport.detailsAppendFolder("Modal Footer with "+headerKey, "Modal Footer Screen should be display", "Modal Footer is displayed in " +languageDescription+ " language", "Pass", languageCode);
								System.out.println(" Now removing ::" + headerKey);
								modalFooterMap.remove(headerKey);
							}
							
						}
							if(modalFooterMap.size()==0 && "FREESPINS".equalsIgnoreCase(cfnlib.getConsoleText("return "+cfnlib.XpathMap.get("currentScene"))))
							{
								cfnlib.waitSummaryScreen();
								headFooterReport.detailsAppendFolderOnlyScreenShot(languageCode);

							}
						}
					}
					Thread.sleep(2000);
					
					if (j + 1 != rowCount2) {
						rowData3=list.get(j + 1);
						String nextLanguage = rowData3.get(Constant.LANG_CODE).trim();
						String currentUrl = webdriver.getCurrentUrl();
						if(currentUrl.contains("LanguageCode"))
							urlNew= currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+nextLanguage);
						else if(currentUrl.contains("languagecode"))
							urlNew = currentUrl.replaceAll("languagecode="+languageCode, "languagecode="+nextLanguage);
						
						cfnlib.loadGame(urlNew);
						
					}

				}

			}
		}
		// -------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error(e.getMessage(),e);
			cfnlib.evalException(e);
		}
		// -------------------Closing the connections---------------//
		finally {
			headFooterReport.endReport();
			webdriver.close();
			webdriver.quit();
			Thread.sleep(1000);
		}
	}


}
