import java.io.File;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.library.UnableToLoadPropertiesException;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.library.TestPropReader;


/*@Author:-sg56207
 * This class install the game on axiom environment which includes
 * 1] Presets file upload
 * 2] Apply filter Map
 * 3] Apply Database setting
 * Input Parameters: Game Name ,Module Id ,Desktop Client Id, Mobile Client Id
 * */

public class GameInstallationOnAXIOM {

	public static void main(String[] args) {

		RestAPILibrary apiobj=new RestAPILibrary();
		ExtentHtmlReporter htmlReporter;
		ExtentReports extent=null;
		ExtentTest report=null,report1=null;


		if(args.length!=0)
		{

			try{
				String gameName=args[0].trim();
				String moduleId=args[1].trim();
				String mobClientId=args[2].trim();
				String deskClientId=args[3].trim();
				String reportFolder="./"+gameName+"/Reports/";
				// initialize the HtmlReporter  	
				htmlReporter= new ExtentHtmlReporter(reportFolder+"/GameInstallationStatusReport.html");

				//initialize ExtentReports and attach the HtmlReporter
				extent= new ExtentReports();
				extent.attachReporter(htmlReporter);


				htmlReporter.config().setChartVisibilityOnOpen(true);
				htmlReporter.config().setDocumentTitle("Game Installation summary");
				htmlReporter.config().setReportName("Axiom - "+gameName+" Game Installation summary ");
				htmlReporter.config().setTheme(Theme.STANDARD);
				htmlReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
				report=extent.createTest("Desktop varitent Installtion Status");
				report1=extent.createTest("Mobile varitent Installtion Status");


				TestPropReader.getInstance().loadDefaultProperties();
				TestPropReader.getInstance().loadGameProperties(gameName);
				String desktopPresets=System.getProperty("user.dir")+File.separator+gameName+Constant.PRESETSZIPPATH+TestPropReader.getInstance().getProperty("DesktopPresetZipfile");
				String mobilePresets=System.getProperty("user.dir")+File.separator+gameName+Constant.PRESETSZIPPATH+TestPropReader.getInstance().getProperty("MobilePresetZipfile");
				String filterType=TestPropReader.getInstance().getProperty("FilterType");
				String gameProvider=TestPropReader.getInstance().getProperty("GameProvider");
				String gameCategory=TestPropReader.getInstance().getProperty("GameCategory");
				String architecture=TestPropReader.getInstance().getProperty("Architecture");


				boolean isupload=apiobj.uploadGamePresetZip(desktopPresets,gameProvider,architecture);
				if(isupload)
				{
					report.log(Status.PASS, MarkupHelper.createLabel(" Presets Uploaded:PASS ",  ExtentColor.GREEN));

					if(apiobj.applyFilterMap(moduleId, deskClientId, filterType))
					{
						report.log(Status.PASS, MarkupHelper.createLabel("Apply FilterMap:PASS ",  ExtentColor.GREEN));
						if(apiobj.applyDatabaseSettings(moduleId, deskClientId, gameProvider, gameCategory))
							report.log(Status.PASS, MarkupHelper.createLabel("Apply dataBase Settings:PASS ",  ExtentColor.GREEN));
						else
							report.log(Status.FAIL, MarkupHelper.createLabel("Apply dataBase Settings:FAIL ",  ExtentColor.RED));

					}else
					{
						report.log(Status.FAIL, MarkupHelper.createLabel("Apply FilterMap:FAIL ",  ExtentColor.RED));

					}
				}else
				{
					report.log(Status.FAIL, MarkupHelper.createLabel(" Presets Uploaded:FAIL ",  ExtentColor.RED));

				}
				if(apiobj.uploadGamePresetZip(mobilePresets,gameProvider,architecture))
				{	
					report1.log(Status.PASS, MarkupHelper.createLabel(" Presets Uploaded:PASS ",  ExtentColor.GREEN));

					if(apiobj.applyFilterMap(moduleId, mobClientId, filterType))
					{
						report1.log(Status.PASS, MarkupHelper.createLabel(" Apply FilterMap:PASS ",  ExtentColor.GREEN));

						if(apiobj.applyDatabaseSettings(moduleId, mobClientId, gameProvider, gameCategory))
							report1.log(Status.PASS, MarkupHelper.createLabel("Apply dataBase Settings:PASS ",  ExtentColor.GREEN));
						else
							report1.log(Status.FAIL, MarkupHelper.createLabel("Apply dataBase Settings:FAIL ",  ExtentColor.RED));

					}else{
						report1.log(Status.FAIL, MarkupHelper.createLabel("Apply FilterMap:FAIL ",  ExtentColor.RED));
					}
				}else
				{
					report1.log(Status.FAIL, MarkupHelper.createLabel(" Presets Uploaded:FAIL ",  ExtentColor.RED));

				}
			}catch(UnableToLoadPropertiesException e)
			{
				report.error("Unable to load test property file\n"+e.getMessage());
			}catch (Exception e)
			{
				if(report!=null)
					report.error(e.getMessage());
			}finally
			{
				try{
					if(extent!=null)
						extent.flush();
					
				}catch(Exception e)
				{
					if(report!=null )
						report.error("Exception occur while extent object close"+e.getMessage());
					
				}
			}
		}else
		{
			System.out.println("Excution skipped as mandatory parameter missing, plase provide game name ,Module ID ,MobileclientID and DesktopClientID  parametes");
		}
	}
}
