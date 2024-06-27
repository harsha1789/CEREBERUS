package Modules.Regression.TestScript;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.TestPropReader;
import com.zensar.automation.model.HelpFileCount;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 * This Script traverse all the help file pages and take the screen shot. It
 * reads the test data excel sheet for configured languages.
 * 
 * @author sg56207
 */

public class Desktop_Regression_HelpFIle_Verification2 {

	Logger log = Logger.getLogger( Desktop_Regression_HelpFIle_Verification.class.getName());
	public ScriptParameters scriptParameters;
	
	public void script() throws Exception {
		String mstrTC_Name=scriptParameters.getMstrTCName();
		String mstrTC_Desc=scriptParameters.getMstrTCDesc();
		String mstrModuleName=scriptParameters.getMstrModuleName();
		WebDriver webdriver=scriptParameters.getDriver();
		BrowserMobProxyServer proxy=scriptParameters.getProxy();
		String startTime1=scriptParameters.getStartTime();
		String filePath=scriptParameters.getFilePath();
		String Browsername=scriptParameters.getBrowserName();
		String framework=scriptParameters.getFramework();
		String gameName=scriptParameters.getGameName();
		String languageCode=null;
		String Status=null;
		int mintDetailCount=0;
		//int mintSubStepNo=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		int totalLinksPerLang=0;
		int skipLInkforOncashAndAsia=100;
		Desktop_HTML_Report helpFilesReport = new Desktop_HTML_Report(webdriver, Browsername, filePath, startTime1,
				mstrTC_Name, mstrTC_Desc, mstrModuleName, mintDetailCount, mintPassed, mintFailed, mintWarnings,
				mintStepNo, Status, gameName);

		DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
		CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, helpFilesReport, gameName);

	    WebDriverWait wait = new WebDriverWait(webdriver,30);
	    JavascriptExecutor  js =(JavascriptExecutor)webdriver ;
		try {
			
			if (webdriver != null) {
				
				Map<String, String> currentRow = null;
				int startindex=0;
				String strGameName=null;
				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				ExcelDataPoolManager excelpoolmanager = new ExcelDataPoolManager(testDataExcelPath);
				String moduleId = excelpoolmanager.getCellData("HelpFile", 1, 7);
				String clientId = excelpoolmanager.getCellData("HelpFile", 2, 7);
				String variant= excelpoolmanager.getCellData("HelpFile", 3, 7);
				String url=excelpoolmanager.getCellData("HelpFile", 4, 7);
				int totalRows = excelpoolmanager.rowCount(testDataExcelPath, "HelpFile");
				List<HelpFileCount> enHelpFileList = new ArrayList<>();
				List<WebElement> list1=new ArrayList<> ();
				if(gameName.contains("Desktop"))
				{	
					java.util.regex.Pattern  str=java.util.regex.Pattern.compile("Desktop");

					Matcher  substing=str.matcher(gameName);

					while(substing.find())
					{
						startindex=substing.start();
					}
					strGameName=gameName.substring(0, startindex);
					log.debug("newgamename="+strGameName);
				}
				
				helpFilesReport.detailsAppend("Verify game help files", "", "", "Pass");
				for (int processingRow = 1; processingRow < totalRows; processingRow++) {
					webdriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
					int linkCount=0;
					//System.out.println("++++++++++++++++++++++++++++++++Processing "+processingRow+"+++++++++");
					currentRow = excelpoolmanager.readExcelByRow(testDataExcelPath, "HelpFile", processingRow);
					String clientType = currentRow.get("clientType").toString();
					String market = currentRow.get("Market").toString();
					String helpSystemId = currentRow.get("helpSystemId").toString().trim();
					languageCode = currentRow.get("Language Code").toString().trim();
					String run = currentRow.get("Run").toString().trim();

					
					if("yes".equalsIgnoreCase(run)){
					try {
						webdriver.get(url);
						sleep(1000);

						webdriver.findElement(By.xpath("//input[@name='clientId']")).clear();

						webdriver.findElement(By.xpath("//input[@name='moduleId']")).clear();

						webdriver.findElement(By.xpath("//input[@name='clientType']")).clear();

						webdriver.findElement(By.xpath("//input[@name='helpSystemId']")).clear();

						webdriver.findElement(By.xpath("//input[@name='ul']")).clear();

						

						webdriver.findElement(By.xpath("//input[@name='clientId']")).sendKeys(String.valueOf((int) Double.parseDouble(clientId)));

						webdriver.findElement(By.xpath("//input[@name='moduleId']")).sendKeys(String.valueOf((int) Double.parseDouble(moduleId)));

						webdriver.findElement(By.xpath("//input[@name='clientType']")).sendKeys(String.valueOf((int) Double.parseDouble(clientType)));

						webdriver.findElement(By.xpath("//input[@name='helpSystemId']")).sendKeys(String.valueOf((int) Double.parseDouble(helpSystemId)));

						webdriver.findElement(By.xpath("//input[@name='ul']")).sendKeys(languageCode);

						sleep(2000);

						helpFilesReport.detailsAppendFolderOnlyScreenShot(market+"-"+languageCode);
						webdriver.findElement(By.xpath("//input[@name='submitButton']")).click();
						//wait.until(ExpectedConditions.elementToBeClickable(webdriver.findElement(By.xpath("//div[@id='ToggleNavigationButton']"))));
						Thread.sleep(15000);
						
						webdriver.switchTo().defaultContent();
						wait .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath("//frameset/frameset/frame[@name='navigation']")));
						webdriver.switchTo().frame("toc");
						
						//waiting till game help links to load and click
				        wait.until(ExpectedConditions.visibilityOfElementLocated((By.xpath("//a[contains(@href,'_About' )and contains(@href,'"+strGameName+"')]/../../parent::div"))));
				        webdriver.findElement(By.xpath("//a[contains(@href,'_About' )and contains(@href,'"+strGameName+"')]/../../parent::div")).click();
				        sleep(3000);
				        // selecting appropriate game variant provided as input ,if not then redicted to default variant of game
						switch(variant)
						{
							case "V90":webdriver.findElement(By.xpath("//a[contains(@href,'_"+variant+"/"+strGameName+"_About')]/../preceding-sibling::a")).click();
					        			wait.until(ExpectedConditions.elementToBeClickable((By.xpath("//a[contains(@href,'_"+variant+"/"+strGameName+"_About')]")))).click();
					        			break;
							case "V92":webdriver.findElement(By.xpath("//a[contains(@href,'_"+variant+"/"+strGameName+"_About')]/../preceding-sibling::a")).click();
				        				wait.until(ExpectedConditions.elementToBeClickable((By.xpath("//a[contains(@href,'_"+variant+"/"+strGameName+"_About')]")))).click();
				        				break;
							case "V94":webdriver.findElement(By.xpath("//a[contains(@href,'_"+variant+"/"+strGameName+"_About')]/../preceding-sibling::a")).click();
	        							wait.until(ExpectedConditions.elementToBeClickable((By.xpath("//a[contains(@href,'_"+variant+"/"+strGameName+"_About')]")))).click();
	        							break;
	        				default:	List<WebElement> variantList=webdriver.findElements(By.xpath("//a[contains(@href,'_About' )and contains(@href,'"+strGameName+"')]/../preceding-sibling::a"));
	        					
	        							if(!(variantList.get(0).isEnabled()))
	        								variantList.get(0).click();
	        							
	        							wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//a[contains(@href,'_About' )and contains(@href,'"+strGameName+"')]"))).get(0).click();
	        							//if(!(webdriver.findElement(By.xpath("//a[contains(@href,'"+strGameName+"/"+strGameName+"_About')]/../preceding-sibling::a")).isEnabled()))
	        							//webdriver.findElement(By.xpath("//a[contains(@href,'"+strGameName+"/"+strGameName+"_About')]/../preceding-sibling::a")).click();
										//wait.until(ExpectedConditions.elementToBeClickable((By.xpath("//a[contains(@href,'"+strGameName+"/"+strGameName+"_About')]")))).click();
										break;

						}
						
						webdriver.switchTo().defaultContent();
						webdriver.switchTo().frame(
								webdriver.findElement(By.xpath("//frameset/frameset/frame[@name='navigation']")));
						webdriver.switchTo().frame("toc");
						
						 webdriver.switchTo().defaultContent();
						 wait .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath("//frameset/frameset/frame[@name='body']")));
					     Thread.sleep(2000);   
						 List<WebElement> anchorList0 = webdriver.findElements(By.tagName("a"));
					        System.out.println("Anchor list Size in first Link"+anchorList0.size());
					        
					        for(WebElement anchorElement : anchorList0){
					        	Thread.sleep(1500);
					        	anchorElement.click();
					        }
					        
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
											"Verifying tags for Language:" + market+"-"+languageCode + "::Link::" + linkCount,
											"All the number elements must match english Divs::"
													+ firstLink.getDivCount() + "  Li Count::" + firstLink.getLiCount()
													+ "  Para Count:: " + firstLink.getpCount() + " Table Count::"
													+ firstLink.getTableCount() + "  Table Row Count:: "
													+ firstLink.getTrCount() + " Table column Count::"
													+ firstLink.getTdCount(),
											"All the number of elements matching with english Divs::"
													+ divList0.size() + "  Li Count::" + liList0.size()
													+ "  Para Count:: " + pList0.size() + " Table Count::"
													+ tableList0.size() + "  Table Row Count:: "
													+ rows0 + " Table column Count::"
													+ columns0, "PASS");
									//System.out.println(languageCode+"-"+linkCount+" is Pass");
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
									//System.out.println(languageCode+"-"+linkCount+" is Fail");
								}
								
							}
					        webdriver.switchTo().defaultContent();
					        wait .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath("//frameset/frameset/frame[@name='navigation']")));
					        webdriver.switchTo().frame("toc");
					        linkCount++;
					    
					        //navigating to each sub link available in  game help menu 
					    if(variant.equalsIgnoreCase("V90")|| variant.equalsIgnoreCase("V92")|| variant.equalsIgnoreCase("V94"))   
					    	list1 = webdriver.findElements(By.xpath("//a[contains(@href,'_"+variant+"/"+strGameName+"_About')]/../following-sibling::div"));
					    else
							 list1 = webdriver.findElements(By.xpath("(//a[contains(@href,'_About' )and contains(@href,'"+strGameName+"')])[1]/../following-sibling::div"));

						if(processingRow==1){
						
						totalLinksPerLang = list1.size();
						}
						int count1 = 0;
						helpFilesReport.detailsAppendFolderOnlyScreenShot(market+"-"+languageCode);
					   sleep(2000);
						if (totalLinksPerLang==list1.size()){
							helpFilesReport.detailsAppendNoScreenshot(
									"Verifying links for Language:" + market +"-"+languageCode + "::Link::" + linkCount,
									"All Number of side Links ::"+totalLinksPerLang ,
									"All the number of elements matching with Number of side Links ::"+list1.size(), "PASS");
							//System.out.println(languageCode+"-"+linkCount+" is Pass");
							} else if (("Asia".equalsIgnoreCase(market) || "OnCash".equalsIgnoreCase(market))
									&& ((totalLinksPerLang - 1) == list1.size())) {
								helpFilesReport.detailsAppendNoScreenshot(
										"Verifying links for Language:" + market +"-"+languageCode + "::Link::" + linkCount,
										"All Number of side Links ::"+(totalLinksPerLang-1) ,
										"All the number of elements matching with Number of side Links ::"+list1.size(), "PASS");
						}else{
							//System.out.println(webdriver.getPageSource());
							helpFilesReport.detailsAppendNoScreenshot(
									"Verifying links for Language:" + market +"-"+languageCode + "::Link::" + linkCount,
									"All Number of side Links ::"+totalLinksPerLang ,
									"All the number of elements are not matching with Number of side Links ::"+list1.size(), "Fail");
							//System.out.println(languageCode+"-"+linkCount+" is Fail");
						}
						
						for (WebElement webElement : list1) {
							/*//System.out.println("--------------------------------------------------" + count1++
									+ "--------------------------------------------------------------------");
							//System.out.println("Going to click on " + webElement.getTagName());
							//System.out.println("Going to click on " + webElement.getText());*/
							
							String elemntText = webElement.getText();
							if(processingRow==1&&elemntText.contains("RTP")){
								skipLInkforOncashAndAsia=linkCount;
							}
							
							if (("Asia".equalsIgnoreCase(market) || "OnCash".equalsIgnoreCase(market))
									&& (skipLInkforOncashAndAsia==linkCount)) {
								linkCount++;
								continue;
							}
							
							//System.out.println(webElement.findElement(By.tagName("a")).getText());
							webElement.findElement(By.tagName("a")).click();
							Thread.sleep(2000);
							webdriver.switchTo().defaultContent();
							wait .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath("//frameset/frameset/frame[@name='body']")));
							Thread.sleep(2000);
							 List<WebElement> anchorList = webdriver.findElements(By.tagName("a"));
						        System.out.println("Anchor list Size"+anchorList.size());
						        
						        if(anchorList.size()>0){
						        	takeScreenshot( helpFilesReport,  market,  languageCode);
						        }
						        
						        for(WebElement anchorElement : anchorList){
						        	Thread.sleep(1500);
						        	anchorElement.click();
						        }
						        if(anchorList.size()>0){
						        Thread.sleep(1500);
						        scrollToTop( helpFilesReport,  market,  languageCode);
						        }
						    
							
							List<WebElement> liList = webdriver.findElements(By.xpath("//li"));
							//System.out.println("List size"+liList.size());
				            List<WebElement> divList = webdriver.findElements(By.xpath("//div"));
							List<WebElement> tableList = webdriver.findElements(By.xpath("//table"));
							List<WebElement> pList = webdriver.findElements(By.xpath("//p"));
							
							//System.out.println("liList"+liList.size());
							//System.out.println("divList"+divList.size());
							//System.out.println("tableList"+tableList.size());
							//System.out.println("pList"+pList.size());
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
							
							if(processingRow==1){
							HelpFileCount link= new HelpFileCount();
							link.setDivCount(divList.size());
							link.setLiCount(liList.size());
							link.setpCount(pList.size());
							link.setTableCount(tableList.size());
							link.setTrCount(rows);
							link.setTdCount(columns);
							enHelpFileList.add(link);
							System.out.println("en-"+linkCount);
							log.debug("en-"+linkCount);
							}else{
								System.out.println(languageCode+"-"+linkCount);
								HelpFileCount firstLink=enHelpFileList.get(linkCount);
								boolean listaus=false;
								boolean divstaus=false;
								boolean pstaus=false;
								boolean tablestaus=false;
								boolean trstaus=false;
								boolean tdstaus=false;
								
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
													+ columns+"Number of side Links ::"+list1.size(), "PASS");
									//System.out.println(languageCode+"-"+linkCount+" is Pass");
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
													+ columns+"Number of side Links ::"+list1.size(), "FAIL");
									//System.out.println(languageCode+"-"+linkCount+" is Fail");
								}
								
							
								
							}
									
							linkCount++;

							JavascriptExecutor javascript = (JavascriptExecutor) webdriver;
							// Check If vertical scroll Is present or not.
							long scrollHeight = (Long) javascript
									.executeScript("return document.documentElement.scrollHeight");
							long clientHeight = (Long) javascript
									.executeScript("return document.documentElement.clientHeight;");
							Boolean b2 = (Boolean) javascript.executeScript(
									"return document.documentElement.scrollHeight>(document.documentElement.clientHeight);");

							/*//System.out.println("vertical scroll::" + b2);
							//System.out.println("scrollHeight::" + scrollHeight);
							//System.out.println("clientHeight::" + clientHeight);*/

							// Incase of scroll
							if (b2) {

								long noofScroll = scrollHeight / clientHeight;

								for (int count = 1; count <= noofScroll + 1; count++) {
									sleep(2000);
									helpFilesReport.detailsAppendFolderOnlyScreenShot(market+"-"+languageCode);
									javascript.executeScript("window.scrollBy(0," + clientHeight * count + ")");
								}

							} else {
								helpFilesReport.detailsAppendFolderOnlyScreenShot(market+"-"+languageCode);
							}
							//Switching back to menu navigation frame
							Thread.sleep(2000);
							webdriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
							webdriver.switchTo().defaultContent();
							wait .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath("//frameset/frameset/frame[@name='navigation']")));
							wait .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("toc"));
						}
						
					} catch (Exception e) {
						log.error("Exception Language",e);
						helpFilesReport.detailsAppendFolder("Exception while processing Language " + languageCode,
								"Exception while processing Language " + languageCode,
								"Should not throw exception while processing ::" + languageCode + "          "
										+ languageCode + " language",
								"FAIL", market+"-"+languageCode);
					}
					}
					else{
						//System.out.println("Skipped :: "+market+languageCode);
					}
					
					long endTime =System.currentTimeMillis();
					
					//System.out.println("Total time taken to process "+market+languageCode+" :: "+ (endTime-startTime)/1000 + "secs");
				}
			}

		}

		// -------------------Handling the exception---------------------//
		catch (Exception e) {
			log.error(e.getMessage(), e);
			cfnlib.evalException(e);
		}
		// -------------------Closing the connections---------------//
		finally {
			helpFilesReport.endReport();
			webdriver.close();
			webdriver.quit();
			proxy.abort();
			Thread.sleep(1000);
		}

	}

	public void sleep(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			log.error(e.getStackTrace());
		}
	}
	
	public void takeScreenshot(Desktop_HTML_Report helpFilesReport, String market, String languageCode){
		try {
			WebDriver webdriver=scriptParameters.getDriver();
			JavascriptExecutor javascript = (JavascriptExecutor) webdriver;
			// Check If vertical scroll Is present or not.
			long scrollHeight = (Long) javascript
					.executeScript("return document.documentElement.scrollHeight");
			long clientHeight = (Long) javascript
					.executeScript("return document.documentElement.clientHeight;");
			Boolean b2 = (Boolean) javascript.executeScript(
					"return document.documentElement.scrollHeight>(document.documentElement.clientHeight);");

			/*//System.out.println("vertical scroll::" + b2);
			//System.out.println("scrollHeight::" + scrollHeight);
			//System.out.println("clientHeight::" + clientHeight);*/

			// Incase of scroll
			if (b2) {

				long noofScroll = scrollHeight / clientHeight;

				for (int count = 1; count <= noofScroll + 1; count++) {
					sleep(3000);
					helpFilesReport.detailsAppendFolderOnlyScreenShot(market+"-"+languageCode);
					javascript.executeScript("window.scrollBy(0, " + clientHeight * count + ")");
				}

			} else {
				helpFilesReport.detailsAppendFolderOnlyScreenShot(market+"-"+languageCode);
			}
		} catch (Exception e) {
			log.error("Exception while Taking screenshot ");
		}
	}
	
	public void scrollToTop(Desktop_HTML_Report helpFilesReport, String market, String languageCode){
		try {
			WebDriver webdriver=scriptParameters.getDriver();
			JavascriptExecutor javascript = (JavascriptExecutor) webdriver;
			//javascript.executeScript("window.scrollBy(0,0)");
			// Check If vertical scroll Is present or not.
			long scrollHeight = (Long) javascript
					.executeScript("return document.documentElement.scrollHeight");
			long clientHeight = (Long) javascript
					.executeScript("return document.documentElement.clientHeight;");
			Boolean b2 = (Boolean) javascript.executeScript(
					"return document.documentElement.scrollHeight>(document.documentElement.clientHeight);");

		
			if (b2) {

				long noofScroll = scrollHeight / clientHeight;

				for (int count = 1; count <= noofScroll + 1; count++) {
					sleep(3000);
				javascript.executeScript("window.scrollBy(0, -" + clientHeight * count + ")");
				}

			} 
		} catch (Exception e) {
			log.error("Exception while Scrolling back to top ",e);
		}
	}
}
