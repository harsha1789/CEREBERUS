package Modules.Regression.TestScript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.TestPropReader;
import com.zensar.automation.model.HelpFileCount;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;

/*This Script verify the help file of game.
 * It covers following -
 * 1] checks  the total link present in market specific language.
 * 2] check the total paragraphs present in the text
 * 3] check the total bullet points in text
 * 4] check for table present in text
 * 
 * */

public class Desktop_HelpFiles_Remote {
	Logger log = Logger.getLogger(Desktop_HelpFiles_Remote.class.getName());
	public ScriptParameters scriptParameters;
	
	
	public void script() throws Exception {
		String mstrTCName=scriptParameters.getMstrTCName();
		String mstrTCDesc=scriptParameters.getMstrTCDesc();
		String mstrModuleName=scriptParameters.getMstrModuleName();
		WebDriver webdriver=scriptParameters.getDriver();
		BrowserMobProxyServer proxy=scriptParameters.getProxy();
		String startTime1=scriptParameters.getStartTime();
		String filePath=scriptParameters.getFilePath();
		String browserName=scriptParameters.getBrowserName();
		String framework=scriptParameters.getFramework();
		String gameName=scriptParameters.getGameName();
		String status=null;
		String defaultDotcomLink = null;
		String sheetName="HelpFileRemote";
		
		int mintDetailCount=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		

		int totalLinksPerLang = 0;

		Desktop_HTML_Report helpFilesReport = new Desktop_HTML_Report(webdriver, browserName, filePath, startTime1,
				mstrTCName, mstrTCDesc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings,
				mintStepNo, status, gameName);

		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, helpFilesReport, gameName);

		
		
		try {
			// Step 1
			if (webdriver != null) {
				
				

				Map<String, String> currentRow = null;
				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				ExcelDataPoolManager excelpoolmanager = new ExcelDataPoolManager(testDataExcelPath);
				String moduleId = excelpoolmanager.getCellData(sheetName, 1, 8);
				log.debug(String.valueOf((int) Double.parseDouble(moduleId)));
				String clientId = excelpoolmanager.getCellData(sheetName, 2, 8);
				log.debug(String.valueOf((int) Double.parseDouble(clientId)));
				int totalRows = excelpoolmanager.rowCount(testDataExcelPath, sheetName);
				List<HelpFileCount> enHelpFileList = new ArrayList<>();
				
				
				//prosseing each line from excel 
				for (int processingRow = 1; processingRow < totalRows; processingRow++) {

					//long startTime = System.currentTimeMillis();
					webdriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
					int linkCount = 0;
					int elementCount=0;
					log.debug("++++++++++++++++++++++++++++++++Processing " + processingRow + "+++++++++");
					currentRow = excelpoolmanager.readExcelByRow(testDataExcelPath, sheetName,
							processingRow);
					String clientType = currentRow.get("clientType").toString();
					String market = currentRow.get("Market").toString();
					String helpSystemId = currentRow.get("helpSystemId").toString().trim();
					String languageCode = currentRow.get("Language Code").toString().trim();
					String run = currentRow.get("Run").toString().trim();
					String link = currentRow.get("Link").toString().trim();
					try {
						if ("DotCom".equalsIgnoreCase(market) && "en".equalsIgnoreCase(languageCode)) {
							defaultDotcomLink = link;
						}
						//checking "Yes" falg read from excel
						if ("yes".equalsIgnoreCase(run)) {
							


							log.debug("Executing ::" + market + languageCode);

						
							
							//replacing language in link
							//link = link.replaceAll("#Language#", languageCode);
							//link = link.replaceAll("midcid", moduleId.trim() + clientId.trim());
							
							//String folderPath="\\\\DERIW50N0028\\HelpFiles\\desktop\\en\\ImmortalRomanceWowPot_1231_en";
							File folder = new File(link);
							String fileName=null;
							
							
							//final File[] fileEntry = folder.listFiles();
							
							 for (final File fileEntry : folder.listFiles()) {
							        if (fileEntry.isFile()) {
							fileName=fileEntry.getName();
							webdriver.get(link+"\\"+fileName);
							
							

							// webdriver.get("https://mobilewebserver9-mobiletesting14.installprogram.eu/Help/MobileHelp/en/Default.htm?cshid=1043050301");

							WebDriverWait wait = new WebDriverWait(webdriver, 50);
							cfnlib.threadSleep(2000);
							
							 List<WebElement> liList0 = webdriver.findElements(By.xpath("//li"));
								List<WebElement> divList0 = webdriver.findElements(By.xpath("//div"));
								List<WebElement> pList0 = webdriver.findElements(By.xpath("//p"));
								List<WebElement> tableList0 = webdriver.findElements(By.xpath("//table"));
								
								int rows0 =0;
								int columns0=0;
								for(WebElement table : tableList0){
									List<WebElement> trList = table.findElements(By.tagName("tr"));
									for(WebElement row : trList){
										rows0++;
										List<WebElement> tdList = row.findElements(By.tagName("td"));
										for(WebElement column : tdList){
											if(!"".equalsIgnoreCase(column.getText().trim())){
												columns0++;
											}
										}
									}
								}
								
								
								if(processingRow==1){
								HelpFileCount firstLink= new HelpFileCount();
								firstLink.setDivCount(divList0.size());
								firstLink.setLiCount(liList0.size());
								firstLink.setpCount(pList0.size());
								firstLink.setTableCount(tableList0.size());
								firstLink.setTrCount(rows0);
								firstLink.setTdCount(columns0);
								enHelpFileList.add(firstLink);
								}else{
									HelpFileCount firstLink=	enHelpFileList.get(linkCount);
									boolean listaus=false;
									boolean divstaus=false;
									boolean pstaus=false;
									boolean tablestaus=false;
									boolean trstaus=false;
									boolean tdstaus=false;
									
									if(firstLink.getLiCount()==liList0.size()){
										listaus=true;
									}
									if(firstLink.getDivCount()==divList0.size()){
										divstaus=true;
									}
									if(firstLink.getpCount()==pList0.size()){
										pstaus=true;
									}
									if(firstLink.getTableCount()==tableList0.size()){
										tablestaus=true;
									}
									if(firstLink.getTrCount()==rows0){
										trstaus=true;
									}
									if(firstLink.getTdCount()==columns0){
										tdstaus=true;
									}
									
									if (listaus&&divstaus&&pstaus&&tablestaus&&trstaus&&tdstaus){
										helpFilesReport.detailsAppendNoScreenshot(
												"Verifying tags for Language :" + market+"-"+languageCode + ":: Link::" + linkCount,
												"All the number elements must match english Divs ::"
														+ firstLink.getDivCount() + "  Li Count ::" + firstLink.getLiCount()
														+ "  Para Count :: " + firstLink.getpCount() + " Table Count ::"
														+ firstLink.getTableCount() + "  Table Row Count :: "
														+ firstLink.getTrCount() + " Table column Count ::"
														+ firstLink.getTdCount(),
												"All the number of elements matching with english Divs::"
														+ divList0.size() + "  Li Count::" + liList0.size()
														+ "  Para Count:: " + pList0.size() + " Table Count::"
														+ tableList0.size() + "  Table Row Count:: "
														+ rows0 + " Table column Count::"
														+ columns0, "PASS");
									}else{
										helpFilesReport.detailsAppendNoScreenshot(
												"Verifying tags for Language:" + market+"-"+languageCode + "::Link::" + linkCount,
												"All the number elements must match english Divs::"
														+ firstLink.getDivCount() + "  Li Count::" + firstLink.getLiCount()
														+ "  Para Count:: " + firstLink.getpCount() + " Table Count::"
														+ firstLink.getTableCount() + "  Table Row Count:: "
														+ firstLink.getTrCount() + " Table column Count::"
														+ firstLink.getTdCount(),
												"All the number of elements are not matching with english Divs::"
														+ divList0.size() + "  Li Count::" + liList0.size()
														+ "  Para Count:: " + pList0.size() + " Table Count::"
														+ tableList0.size() + "  Table Row Count:: "
														+ rows0 + " Table column Count::"
														+ columns0, "FAIL");
									}
									
								}
							//increment the link count 
								elementCount++;
								linkCount++;
								
								// Check If vertical scroll Is present or not.
								JavascriptExecutor javascript = (JavascriptExecutor) webdriver;
								
								long scrollHeight = (Long) javascript
										.executeScript("return document.documentElement.scrollHeight");
								long clientHeight = (Long) javascript
										.executeScript("return document.documentElement.clientHeight;");
								Boolean b2 = (Boolean) javascript.executeScript(
										"return document.documentElement.scrollHeight>document.documentElement.clientHeight;");
								helpFilesReport.detailsAppendFolderOnlyScreenShot(market+"-"+languageCode);

								if (b2) {

									long noofScroll = scrollHeight / clientHeight;

									for (int count2 = 1; count2 <= noofScroll; count2++) {
										cfnlib.threadSleep(2000);
										javascript.executeScript("window.scrollBy(0," + clientHeight * count2 + ")");
										helpFilesReport.detailsAppendFolderOnlyScreenShot( market+"-"+languageCode);
										cfnlib.threadSleep(2000);
									}

								}
							        }
							 }
						}
						
					} catch (Exception e) {
						helpFilesReport.detailsAppendFolder(
								"Processing Failed" + market +"-"+languageCode,
								"Processing Failed",
								"Processing Failed" + market +"-"+languageCode, "Fail",
								market+"-"+languageCode);
						log.error("Execption while Processing Language" + languageCode,e);
					}
				
			
				}
			}
		// -------------------Handling the exception---------------------//
		}catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			helpFilesReport.endReport();
			webdriver.close();
			webdriver.quit();
			//proxy.abort();
			Thread.sleep(1000);
		}
	}
}