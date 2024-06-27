package Modules.Regression.TestScript;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop_CS;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop_CS_Renovate;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop_Force;
import net.lightbody.bmp.BrowserMobProxyServer;

public class LanguageTranslationTest {
	Logger log = Logger.getLogger(Desktop_Regression_Language_Verification_BaseScene.class.getName());
	public ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();
	public String path=System.getProperty("user.dir");
	public  WebDriver webdriver;		
	public BrowserMobProxyServer proxy;
	public String classname= this.getClass().getSimpleName();
	public String mstrTC_Name,mstrTC_Desc,mstrModuleName;
	public String gametitleexcel;
	public String filePath;
	public String startTime;
	public String userName;
	public int mintDetailCount;
	public int mintSubStepNo;
	public int mintPassed;
	public int mintFailed;
	public int mintWarnings;
	public int mintStepNo;
	public String Status;
	public String Browsername;
	public String Framework;
	public String GameName;
	public String languageDescription;
	public String languageCode;

	public void script() throws Exception{
		Desktop_HTML_Report language = new Desktop_HTML_Report(webdriver,Browsername,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,GameName);
		CFNLibrary_Desktop cfnlib=null;
		if(Framework.equalsIgnoreCase("PN")){
			cfnlib=new CFNLibrary_Desktop(webdriver,proxy,language,GameName);
		}else if(Framework.equalsIgnoreCase("Force")){
			cfnlib=new CFNLibrary_Desktop_Force(webdriver,proxy,language,GameName);
		}else if(Framework.equalsIgnoreCase("CS_Renovate")){
			cfnlib=new CFNLibrary_Desktop_CS_Renovate(webdriver,proxy,language,GameName);
		}else{
			cfnlib=new CFNLibrary_Desktop_CS(webdriver,proxy,language,GameName);
		}
		try{
			
			if(webdriver!=null)
			{	
				int rowcount;
				Map<String, String> rowData2 = null;
				Map<String, String> rowData3 = null;
				String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");
				String url = cfnlib.XpathMap.get("ApplicationURL");
				
				HashMap<String,ArrayList<String>> translationmap = cfnlib.readTranslations();
				
				String obj = cfnlib.func_navigate(url);
				log.debug("opening webiste");
				if(obj!=null){
						language.detailsAppend("Open Browser and Enter Lobby URL in address bar and click Enter", "Island Paradise Lobby should open", "Island Paradise lobby is Open", "Pass");
				}
				else {
						language.detailsAppend("Open browser and Enter Lobby URL in address bar and click Enter", "Island Paradise lobby should Open", "Island paradise is not displayed", "Fail");
				}
				
				if(Framework.equalsIgnoreCase("Force")){
					cfnlib.setNameSpace();
					}
				
				// Read the language code
				int rowCount2 = excelpoolmanager.rowCount(testDataExcelPath, "LanguageCodes");
				for(int j=1;j<rowCount2;j++){
					
					rowData2 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",  j);
					languageDescription = rowData2.get("Language").toString();
					languageCode = rowData2.get("Language Code").toString().trim();
					
					cfnlib.newFeature();
					cfnlib.waitForSpinButton();
					// Iterating for text to be verify
					ExcelDataPoolManager textcomparisionexcel= new ExcelDataPoolManager(TestPropReader.getInstance().getProperty("TEXT_COMPARISION_EXCEL_PATH"));
					Map<String, String> rowData = null;
					// Read the row count in sheet "Text"
					
				
					rowcount = textcomparisionexcel.rowCount(TestPropReader.getInstance().getProperty("TEXT_COMPARISION_EXCEL_PATH"),"Text");
					
					
					for(int i=1;i<rowcount;i++)
					{
						rowData = textcomparisionexcel.readExcelByRow(TestPropReader.getInstance().getProperty("TEXT_COMPARISION_EXCEL_PATH"),"Text",i);
					
						
						cfnlib.verifyTextTranslation(rowData.get("Hook"),rowData.get("Text"),languageCode,translationmap,language);
						
						
					}
			
					// Updating next language		
					if (j + 1 != rowCount2){
						rowData3 = excelpoolmanager.readExcelByRow(testDataExcelPath, "LanguageCodes",  j+1);
						String languageCode2 = rowData3.get("Language Code").toString().trim();

						String currentUrl = webdriver.getCurrentUrl();
						String url_new = currentUrl.replaceAll("LanguageCode="+languageCode, "LanguageCode="+languageCode2);
						webdriver.navigate().to(url_new);
						
					}

				
			}//for loop closing
		}// if
		}catch (Exception e) {
			log.error(e.getMessage(),e);
			
		}
		//-------------------Closing the connections---------------//
		finally
		{
			language.endReport();
			webdriver.close();
			webdriver.quit();
			proxy.abort();
			Thread.sleep(1000);	   
		}	
	}
}// class body
