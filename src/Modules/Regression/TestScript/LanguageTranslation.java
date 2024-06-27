package Modules.Regression.TestScript;

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

public class LanguageTranslation {

	
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
		public String Text;
		public String Hook;

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
				// Step 1 
				if(webdriver!=null)
				{		
					Map<String, String> rowData2 = null;
					
					//Reading text along with hooks from excel
					int rowCount2 = excelpoolmanager.rowCount(TestPropReader.getInstance().getProperty("TEXT_COMPARISION_EXCEL_PATH"), "Text");
					for(int j=1;j<rowCount2;j++){
						//Step 2: To get the languages in MAP and load the language specific url
						rowData2 = excelpoolmanager.readExcelByRow(TestPropReader.getInstance().getProperty("TEXT_COMPARISION_EXCEL_PATH"), "Text",  j);
						Text = rowData2.get("Text").toString();
						Hook = rowData2.get("Hook").toString().trim();
						
						System.out.println("Text:"+Text+"\nHook:"+Hook);
					}
		

					
					
					
					
					
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
}
