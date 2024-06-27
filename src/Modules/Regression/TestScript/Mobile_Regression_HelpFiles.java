package Modules.Regression.TestScript;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.TestPropReader;
import com.zensar.automation.model.HelpFileCount;

import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import net.lightbody.bmp.BrowserMobProxyServer;

/*This Script verify the help file of game.
 * It covers following -
 * 1] checks  the total link present in market specific language.
 * 2] check the total paragraphs present in the text
 * 3] check the total bullet points in text
 * 4] check for table present in text
 * 
 * */

public class Mobile_Regression_HelpFiles {
	Logger log = Logger.getLogger(Mobile_Regression_HelpFiles.class.getName());
	public ScriptParameters scriptParameters;
	
	
	public void script() throws Exception {
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
		

		int totalLinksPerLang = 0;
		int skipLInkforOncashAndAsia = 100;

		Mobile_HTML_Report helpFilesReport = new Mobile_HTML_Report(webdriver, DeviceName, filePath, startTime,
				mstrTC_Name, mstrTC_Desc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings,
				mintStepNo, Status, gameName);
		String default_dotcom_link = null;
		MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, helpFilesReport, gameName);
		
		
		try {
			// Step 1
			if (webdriver != null) {
				
				

				Map<String, String> currentRow = null;
				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				ExcelDataPoolManager excelpoolmanager = new ExcelDataPoolManager(testDataExcelPath);
				String moduleId = excelpoolmanager.getCellData("HelpFileMobile", 1, 8);
				log.debug(String.valueOf((int) Double.parseDouble(moduleId)));
				String clientId = excelpoolmanager.getCellData("HelpFileMobile", 2, 8);
				String orientation = excelpoolmanager.getCellData("HelpFileMobile", 3, 8);
				log.debug(String.valueOf((int) Double.parseDouble(clientId)));
				int totalRows = excelpoolmanager.rowCount(testDataExcelPath, "HelpFileMobile");
				List<HelpFileCount> enHelpFileList = new ArrayList<>();
				
				// Executing in specific orientation
				if("landscape".equalsIgnoreCase(orientation)){
					webdriver.context("NATIVE_APP");
					webdriver.rotate(ScreenOrientation.LANDSCAPE);
					webdriver.context("CHROMIUM");
				}
				//prosseing each line from excel 
				for (int processingRow = 1; processingRow < totalRows; processingRow++) {

					//long startTime = System.currentTimeMillis();
					webdriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
					int linkCount = 1;
					int elementCount=0;
					log.debug("++++++++++++++++++++++++++++++++Processing " + processingRow + "+++++++++");
					currentRow = excelpoolmanager.readExcelByRow(testDataExcelPath, "HelpFileMobile",
							processingRow);
					String clientType = currentRow.get("clientType").toString();
					String market = currentRow.get("Market").toString();
					String helpSystemId = currentRow.get("helpSystemId").toString().trim();
					String languageCode = currentRow.get("Language Code").toString().trim();
					String run = currentRow.get("Run").toString().trim();
					String link = currentRow.get("Link").toString().trim();
					try {
						if ("DotCom".equalsIgnoreCase(market) && "en".equalsIgnoreCase(languageCode)) {
							default_dotcom_link = link;
						}
						//checking "Yes" falg read from excel
						if ("yes".equalsIgnoreCase(run)) {

							log.debug("Executing ::" + market + languageCode);

							if ((link != null || "".equalsIgnoreCase(link)) && !"en".equalsIgnoreCase(languageCode)
									&& "DotCom".equalsIgnoreCase(market)) {
								link = default_dotcom_link;
							}
							
							//replacing language in link
							link = link.replaceAll("#Language#", languageCode);
							link = link.replaceAll("midcid", moduleId.trim() + clientId.trim());
							webdriver.get(link);

							// webdriver.get("https://mobilewebserver9-mobiletesting14.installprogram.eu/Help/MobileHelp/en/Default.htm?cshid=1043050301");

							WebDriverWait wait = new WebDriverWait(webdriver, 50);
							try {
								Thread.sleep(10000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							//log.debug(webdriver.getPageSource());

							webdriver.switchTo().defaultContent();
							webdriver.switchTo().frame(webdriver.findElement(By.xpath("//iframe[@id='topic']")));
							Thread.sleep(5000);
							//log.debug(webdriver.getPageSource());
							// log.debug(driver.getPageSource());
							
							// fetching current link present in text
							List<WebElement> list = webdriver.findElements(By
									.xpath("//div/ul[@class='nocontent menu _Skins_Responsive_menu mc-component']/li"));

							if (processingRow == 1) {
								totalLinksPerLang = list.size();
							}

							//validating links 
							if (totalLinksPerLang == list.size()) {
								helpFilesReport.detailsAppendNoScreenshot(
										"Verifying links for Language:" + market + "-" + languageCode + "::Link::"
												+ linkCount,
										"All Number of side Links ::" + totalLinksPerLang,
										"All the number of elements matching with Number of side Links ::"
												+ list.size(),
										"PASS");
								log.debug(languageCode + "-" + linkCount + " is Pass");
							} else if (("Asia".equalsIgnoreCase(market) || "OnCash".equalsIgnoreCase(market))
									&& ((totalLinksPerLang - 1) == list.size())) {
								helpFilesReport.detailsAppendNoScreenshot(
										"Verifying links for Language:" + market + "-" + languageCode + "::Link::"
												+ linkCount,
										"All Number of side Links ::" + (totalLinksPerLang - 1),
										"All the number of elements matching with Number of side Links ::"
												+ list.size(),
										"PASS");
							} else {
								//System.out.println(webdriver.getPageSource());
								helpFilesReport.detailsAppendNoScreenshot(
										"Verifying links for Language:" + market + "-" + languageCode + "::Link::"
												+ linkCount,
										"All Number of side Links ::" + totalLinksPerLang,
										"All the number of elements are not matching with Number of side Links ::"
												+ list.size(),
										"Fail");
								log.debug(languageCode + "-" + linkCount + " is Fail");
							}
							// accessing next link
							for (int count = 1; count <= list.size(); count++) {
								try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									
									e.printStackTrace();
								}
								WebElement webElement = webdriver.findElement(By
										.xpath("//div/ul[@class='nocontent menu _Skins_Responsive_menu mc-component']/li["
												+ count + "]"));
								log.debug("Going to click on " + webElement.getTagName());
								log.debug("Going to click on " + webElement.getText());
								webElement.click();
								try {
									Thread.sleep(5000);
								} catch (InterruptedException e) {
									
									e.printStackTrace();
								}

								webdriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
								List<WebElement> liList = webdriver.findElements(By.xpath("//div[@class='responsive-topic']/div[1]//li"));
								log.debug("List size"+liList.size());
					            List<WebElement> divList = webdriver.findElements(By.xpath("//div[@class='responsive-topic']/div[1]//div"));
								List<WebElement> tableList = webdriver.findElements(By.xpath("//div[@class='responsive-topic']/div[1]//table"));
								List<WebElement> pList = webdriver.findElements(By.xpath("//div[@class='responsive-topic']/div[1]//p"));
								
								log.debug("liList"+liList.size());
								log.debug("divList"+divList.size());
								log.debug("tableList"+tableList.size());
								log.debug("pList"+pList.size());
								int rows =0;
								int columns=0;
								for(WebElement table : tableList){
									List<WebElement> trList = table.findElements(By.tagName("tr"));
									for(WebElement row : trList){
										rows++;
										List<WebElement> tdList = row.findElements(By.tagName("td"));
										for(WebElement column : tdList){
											if(!"".equalsIgnoreCase(column.getText().trim())){
												columns++;
											}
										}
									}
								}
								// Storing the total text information in enlink object
								if(processingRow==1){
								HelpFileCount enlink= new HelpFileCount();
								enlink.setDivCount(divList.size());
								enlink.setLiCount(liList.size());
								enlink.setpCount(pList.size());
								enlink.setTableCount(tableList.size());
								enlink.setTrCount(rows);
								enlink.setTdCount(columns);
								enHelpFileList.add(enlink);
								//log.debug("en-"+elementCount+":"+webdriver.getPageSource());
								}else{
									// Accesing nth object from enhelpFilelist
									HelpFileCount firstLink=	enHelpFileList.get(elementCount);
									
									
									boolean listaus=false;
									boolean divstaus=false;
									boolean pstaus=false;
									boolean tablestaus=false;
									boolean trstaus=false;
									boolean tdstaus=false;
									
									//validating each  tag present in nth link with en link
									if(firstLink.getLiCount()==liList.size()){
										listaus=true;
									}
									if(firstLink.getDivCount()==divList.size()){
										divstaus=true;
									}
									if(firstLink.getpCount()==pList.size()){
										pstaus=true;
									}
									if(firstLink.getTableCount()==tableList.size()){
										tablestaus=true;
									}
									if(firstLink.getTrCount()==rows){
										trstaus=true;
									}
									if(firstLink.getTdCount()==columns){
										tdstaus=true;
									}
									
									
									if (listaus&&divstaus&&pstaus&&tablestaus&&trstaus&&tdstaus){
										helpFilesReport.detailsAppendNoScreenshot(
												"Verifying tags for Language:" + languageCode + "::Link::" + linkCount,
												"All the number elements must match english Divs::"
														+ firstLink.getDivCount() + "  Li Count::" + firstLink.getLiCount()
														+ "  Para Count:: " + firstLink.getpCount() + " Table Count::"
														+ firstLink.getTableCount() + "  Table Row Count:: "
														+ firstLink.getTrCount() + " Table column Count::"
														+ firstLink.getTdCount()+"Number of side Links ::"+totalLinksPerLang ,
												"All the number of elements matching with english Divs::"
														+ divList.size() + "  Li Count::" + liList.size()
														+ "  Para Count:: " + pList.size() + " Table Count::"
														+ tableList.size() + "  Table Row Count:: "
														+ rows + " Table column Count::"
														+ columns, "PASS");
										log.debug(languageCode+"-"+linkCount+" is Pass");
									}else{
										//System.out.println(webdriver.getPageSource());
										helpFilesReport.detailsAppendNoScreenshot(
												"Verifying tags for Language:" + languageCode + "::Link::" + linkCount,
												"All the number elements must match english Divs::"
														+ firstLink.getDivCount() + "  Li Count::" + firstLink.getLiCount()
														+ "  Para Count:: " + firstLink.getpCount() + " Table Count::"
														+ firstLink.getTableCount() + "  Table Row Count:: "
														+ firstLink.getTrCount() + " Table column Count::"
														+ firstLink.getTdCount()+"Number of side Links ::"+totalLinksPerLang,
												"All the number of elements are not matching with english Divs::"
														+ divList.size() + "  Li Count::" + liList.size()
														+ "  Para Count:: " + pList.size() + " Table Count::"
														+ tableList.size() + "  Table Row Count:: "
														+ rows + " Table column Count::"
														+ columns, "FAIL");
										log.debug(languageCode+"-"+linkCount+" is Fail");
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
								helpFilesReport.detailsAppendFolderOnlyScreenshot("Verifying links for :" + market +"-"+languageCode,
										"click on clink",
										"link clicked ", "Pass", market+"-"+languageCode);

								if (b2) {

									long noofScroll = scrollHeight / clientHeight;

									for (int count2 = 1; count2 <= noofScroll; count2++) {
										try {
											Thread.sleep(2000);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										javascript.executeScript("window.scrollBy(0," + clientHeight * count2 + ")");
										helpFilesReport.detailsAppendFolderOnlyScreenshot("Verifying links for :" + market +"-"+languageCode,
												"click on clink",
												"link clicked ", "Pass", market+"-"+languageCode);
										try {
											Thread.sleep(2000);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
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
		}
		// -------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			helpFilesReport.endReport();
			webdriver.close();
			webdriver.quit();
			proxy.abort();
			Thread.sleep(3000);
		}
	}
}