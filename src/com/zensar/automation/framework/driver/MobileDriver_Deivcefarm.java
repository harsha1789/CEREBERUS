package com.zensar.automation.framework.driver;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;
import com.zensar.automation.framework.api.DeviceApi;
import com.zensar.automation.framework.library.Global;
import com.zensar.automation.framework.library.PropertyReader;
import com.zensar.automation.framework.model.STFService;
import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.proxy.CaptureType;

/**
 * This is  the driver class for mobile , it reads the configured test cases from excel sheet,
 * creates driver object based on browser, device farm and calls the test case using reflection.
 * 
 */
@Test
public class MobileDriver_Deivcefarm{
	Logger log = Logger.getLogger(MobileDriver.class.getName());
	ThreadLocal<WebDriver> webDriver=new ThreadLocal<>();
	BrowserMobProxyServer browserproxy;
	String deviceName;
	AppiumDriver<WebElement> driver;
	DateFormat mObjDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	String filePath;
	String startTime; 
	int mintDetailCount;
	int mintSubStepNo;
	int mintPassed;
	int mintFailed;
	int mintWarnings;
	int mintStepNo;
	String mstrTestCase;
	String msrtTCDesc;
	String msrtModuleName;
	String status;
	String time;
	String username;
	public static Map<String,String> repo= new HashMap<>();
	String mstrGame;
	String deviceIPPort;
	String gameName;
	String deviceSerial;
	// Commented out the below code while testing authentication at hub
	//private  DeviceApi deviceApi;
	//ATUTestRecorder recorder; 
	String browserName;
	String osPlatform;




	///////////////********* Mobile Driver*******************/////////////
	@Parameters({ "DeviceId","IpAddress", "Port","OSVersion","Proxy","Username","ZenReplica","DeviceName","Browser","OSPlatform","GameName","CheckedOutDeviceNum"})
	public  void test(String deviceid, String ipAddress, String port,String osVersion, int proxy, String username, String zenReplica, String deviceName,String browser, String osPlatform,String gameName,@Optional("1") int checkedOutDeviceNum) throws IOException, InterruptedException,IllegalAccessException
	{
		log.info("Automation Execution stared for mobile....");
	//	String seleniumGridUrl = null; 
		String seleniumGridUrl="http://username:AccessKey@hub-cloud.browserstack.com/wd/hub";
		this.deviceSerial=deviceid;
		this.gameName=gameName;
		this.browserName=browser;
		this.osPlatform=osPlatform;
		//AppiumDriver<WebElement> driver = null;
		
		try{

			ExcelDataPoolManager oExcelFile = new ExcelDataPoolManager(); 
			boolean isDeviceCheckedOut = false;
			// Depending on zenReplica flag connect to  zenReplica device farm or Sauce lab device farm devices
	/*		if (zenReplica.equalsIgnoreCase(Constant.TRUE)) 
			{// Commented out the below code while testing authentication at hub
			
			// isDeviceCheckedOut = checkOutDevice(zenReplica, ipAddress,osPlatform);
			
					seleniumGridUrl= PropertyReader.getInstance().getProperty("SELENIUM_GRID_PATH");
			}else {
				seleniumGridUrl = PropertyReader.getInstance().getProperty("SELENIUM_LOCAL_GRID_PATH");
			}
			System.out.println("Selenium Grid Url :: "+ seleniumGridUrl);
			log.info("Selenium Grid Url :: "+ seleniumGridUrl);
	*/		
			
			Global obj=new Global(gameName);
			//------------- Declaration Of Global Variable in Config File-----------
			obj.cfnRootPath();
			String suitExcelPath= PropertyReader.getInstance().getProperty("Suit_Excel_Path");
			String regressionExcelPath=PropertyReader.getInstance().getProperty("Regression_Excel_Path");
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()); 
			repo.put(deviceName, timeStamp);
			int rowcount =oExcelFile.rowCount(suitExcelPath,Constant.CONFIGFILE);
			Map<String, String> rowData =null;
			
			for ( int moduleCounter=1; moduleCounter<rowcount; moduleCounter++)
			{
				//---------- Reading Excel Module Suit File On the Basis of Run Flag -------------
				rowData = oExcelFile.readExcelByRow(suitExcelPath,Constant.CONFIGFILE, moduleCounter);
				String mstrRunFlag=rowData.get("RunStatus");
				if(mstrRunFlag.equalsIgnoreCase("run"))
				{
					obj.setGstrModuleName(rowData.get("ModuleName"));
					obj.cfnModuleRootPath();
					obj.setgCalenderCalStart(Calendar.getInstance());
					obj.setGstrStartTime(mObjDateFormat.format(obj.getgCalenderCalStart().getTime()));

					//****************************************************************//
					int testcaseCount =oExcelFile.rowCount(regressionExcelPath,Constant.CONFIGFILE);

					for (int testCaseCounter=1; testCaseCounter<testcaseCount; testCaseCounter++)
					{
						//---------- Reading Excel Suit File On the Basis of Run Flag -------------
						rowData = oExcelFile.readExcelByRow(regressionExcelPath,Constant.CONFIGFILE, testCaseCounter);
						String testcaseRunFlag=rowData.get("RunFlag").trim();
						String  mstrPlatform=rowData.get("Platform").trim();
						String mstrFramework=rowData.get("Framework").trim();

						mstrGame=rowData.get("GameName").trim();
						mstrTestCase=rowData.get("TestCase").trim();

						if (testcaseRunFlag.equalsIgnoreCase("run") && mstrPlatform.equals("Mobile"))
						{
							log.info("Run flag found true...");
							msrtTCDesc =rowData.get("TestCaseDescription");
							//Launching the Browser

							this.deviceName=deviceName;
							this.username=username;
							org.openqa.selenium.Proxy seleniumProxy=null;
							if("yes".equalsIgnoreCase(PropertyReader.getInstance().getProperty("enableReadHar")))
							{
								browserproxy= new BrowserMobProxyServer();

								if(!(browserproxy.isStarted()))
								{  		
									browserproxy.setTrustAllServers(true); 
									Set<CaptureType> captureTypes = new HashSet<>();
									captureTypes.add(CaptureType.REQUEST_CONTENT);
									captureTypes.add(CaptureType.RESPONSE_CONTENT);
									browserproxy.enableHarCaptureTypes(captureTypes);
									if(proxy!=0)
									{
										browserproxy.start(proxy);
									}
									else
									{
										browserproxy.start();
									}
									Thread.sleep(2000);
								}	 
								browserproxy.setHarCaptureTypes(CaptureType.getAllContentCaptureTypes());
								browserproxy.newHar();
								InetAddress address=browserproxy.getClientBindAddress();
								seleniumProxy=ClientUtil.createSeleniumProxy(browserproxy);
								log.debug(address.toString());
							}

							DesiredCapabilities capabilities = new DesiredCapabilities();
							if(browser.equalsIgnoreCase("chrome")|| browser.equalsIgnoreCase("safari"))
							{
								if (browser.equalsIgnoreCase("chrome") && osPlatform.equalsIgnoreCase("android"))
								{
									//capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceid);
									//capabilities.setCapability("udid", deviceid);
									//capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, osVersion);
									capabilities.setCapability(CapabilityType.PLATFORM_NAME, "Android");
									//capabilities.setCapability(CapabilityType.BROWSER_NAME, "Chrome");
									capabilities.setCapability("chromedriverExecutableDir",	"/root/chromedrivers/");
									
									capabilities.acceptInsecureCerts();
									capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UIAutomator2");
									capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "600000");
									capabilities.setCapability("appium:chromeOptions",ImmutableMap.of("w3c", false));
									
									//capabilities.setCapability("devicefarm:apiEndpoint", PropertyReader.getInstance().getProperty("STF_SERVICE_URL"));
						//			capabilities.setCapability("devicefarm:authToken",PropertyReader.getInstance().getProperty("ACCESS_TOKEN"));
									capabilities.setCapability("devicefarm:deviceTimeout",PropertyReader.getInstance().getProperty("DEVICE_TIMEOUT"));
									/* the following is  commented 
									capabilities.setCapability("uiautomator2ServerInstallTimeout", 200000);
									ChromeOptions chromeOptions = new ChromeOptions();
									chromeOptions.addArguments("--enable-automatic-password-saving");
									chromeOptions.setAcceptInsecureCerts(true);
									capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
									 */
									
						
									capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Galaxy S20");
									capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "chrome");
									capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "11");
									
									//This is a workaround for appium 1.17
									int randomWait = (int) Math.floor(Math.random() * (30000 - 10000 + 1)) + 10000;  
									log.debug("randomWait :: "+randomWait);
									Thread.sleep(randomWait);
									
									
									driver = new AndroidDriver<>(new URL(seleniumGridUrl), capabilities); 
									
								
									
									log.info("Driver object for mobile...");
								}else if(browser.equalsIgnoreCase("safari") && osPlatform.equalsIgnoreCase("iOS"))
								{
									if(zenReplica.equalsIgnoreCase(Constant.TRUE)) {
										
										Thread.sleep(3000);
										System.out.println("Appium Service Address : - "+ seleniumGridUrl);
										//Set the desire capabilities for browser

										capabilities.setCapability("deviceName", deviceName);
										capabilities.setCapability("platformName", "iOS");
										capabilities.setCapability("browserName", "safari");
										capabilities.setCapability("platformVersion", osVersion);
										capabilities.setCapability("udid", deviceid);
										capabilities.setCapability("appium:newCommandTimeout", 600000);
										//capabilities.setCapability(CapabilityType.BROWSER_NAME, "Safari");
										capabilities.setCapability("appium:nativeWebTap",true);
										capabilities.setCapability("appium:autoWebview",true);
										//showXcodeLog: true and showIOSLog: true
										capabilities.setCapability("appium:showXcodeLog",true);
										capabilities.setCapability("appium:showIOSLog",true);
										//capabilities.setCapability("devicefarm:apiEndpoint", PropertyReader.getInstance().getProperty("STF_SERVICE_URL"));
										capabilities.setCapability("devicefarm:authToken",PropertyReader.getInstance().getProperty("ACCESS_TOKEN"));
										capabilities.setCapability("devicefarm:deviceTimeout",PropertyReader.getInstance().getProperty("DEVICE_TIMEOUT"));
										int randomWait = (int) Math.floor(Math.random() * (30000 - 10000 + 1)) + 10000;  
										log.debug("randomWait :: "+randomWait);
										Thread.sleep(randomWait);
										
										
									    //create driver object by passing the remote/local appium url
										
									    driver = new IOSDriver<>(new URL(seleniumGridUrl), capabilities); 

										
									}else {
									
									//below are the capabilities set for ios device to run on sauce labs
									//set here your test object api key
									capabilities.setCapability("testobject_api_key", "CA4FB67971A14D1D9A11204ABC0B00F2");
									capabilities.setCapability("deviceName",deviceid);
									capabilities.setCapability("browserName", "Safari");
									capabilities.setCapability("newCommandTimeout", 90000);
									capabilities.setCapability("testobject_suite_name","Zaf Automation");

									driver = new IOSDriver<>(new URL(seleniumGridUrl), capabilities);
									}
								}

								Thread.sleep(3000);
								driver.manage().deleteAllCookies();
								//driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
								driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
								Thread.sleep(1000);
								//Converting Test case name to ClassName Format
								String s="Modules."+obj.getGstrModuleName()+".TestScript.";

								String mstrClassName =s.concat(mstrTestCase);
								log.debug("Test case to run :"+mstrClassName);

								//Method Name to call From respective class
								String mstrFunctionName="script";
								//---------------Getting TestCase information from TestCaseReader Class

								msrtModuleName=obj.getGstrModuleName();

								filePath=obj.getGstrResultPath();
								startTime=obj.getGstrStartTime();
								log.debug(startTime);

								ScriptParameters scriptParameters=new ScriptParameters();

								 Class<?> c = Class.forName(mstrClassName);
								Object scriptClassObj = c.newInstance();

								scriptParameters.setMstrTCName(mstrTestCase);
								scriptParameters.setMstrTCDesc(msrtTCDesc);
								scriptParameters.setMstrModuleName(msrtModuleName);
								scriptParameters.setProxy(browserproxy);
								scriptParameters.setStartTime(startTime);
								scriptParameters.setFilePath(filePath);
								scriptParameters.setAppiumWebdriver(driver);
								scriptParameters.setDeviceName(deviceName);
								scriptParameters.setUserName(username);
								scriptParameters.setFramework(mstrFramework);
								scriptParameters.setGameName(mstrGame);
								scriptParameters.setCheckedOutDeviceNum(checkedOutDeviceNum);
								scriptParameters.setBrowserName(browserName);
								scriptParameters.setOsPlatform(osPlatform);
								scriptParameters.setOsVersion(osVersion);

								log.debug("Script Parameters Configured");
								Field scriptParametersObj=c.getDeclaredField("scriptParameters"); 
								scriptParametersObj.set(scriptClassObj,scriptParameters);
								Method m = c.getDeclaredMethod(mstrFunctionName);
								log.info("Calling invoke method");
								m.invoke(scriptClassObj);
								log.info("Excution started for: "+ mstrClassName);
							}
							else{
								log.info("Current browser automation is not supported.");
							}
						}
						else{
							log.info("About to release device as no script is marked as run in Suit.xml");
						}
					}
				}else{
					log.info("About to release device as no script is marked as run in Suit.xml");
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error("Exception in Mobile Driver", e);
		} 
		finally
		{
			// Commented out the below code while testing authentication at hub
			//deviceApi.releaseDevice(deviceid);
		}
	}


	
	@AfterTest
	public void closeBrowser() throws Exception
	{ 
		try{
			Mobile_HTML_Report h=new Mobile_HTML_Report(driver,deviceName,filePath,startTime,mstrTestCase,msrtTCDesc,msrtModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,status,mstrGame);
			h.buildAutomationSummary();
			//String mstrAutoSummPath=filePath+repo.get(DeviceName)+"_"+DeviceName+"_"+"Automation_summary"+".html";
			//String cmds[] = new String[] {"cmd", "/c",mstrAutoSummPath}; 
			//Runtime.getRuntime().exec(cmds); 
			// Commented out the below code while testing authentication at hub
			//deviceApi.releaseDevice(this.deviceSerial);
		}
		catch(Exception e){
			// Commented out the below code while testing authentication at hub
			//deviceApi.releaseDevice(this.deviceSerial);
		}


	}
	@AfterSuite
	public void afterSuite()
	{
		try{
			
			webDriver.remove();
			log.info("Device has Released" +this.deviceSerial);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}

	protected String getStackTrace(Throwable t)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		return sw.toString();
	}

	/*	private void createAppiumService() {
		this.service = AppiumDriverLocalService.buildDefaultService();
		this.service.start();
	}*/

	/*public void initialiseSTFService() throws MalformedURLException, URISyntaxException{
		String accessToken =PropertyReader.getInstance().getProperty("ACCESS_TOKEN"); 
		String stfServiceURL =PropertyReader.getInstance().getProperty("STF_SERVICE_URL");
		STFService stfService = new STFService(stfServiceURL,	accessToken);
		deviceApi = new DeviceApi(stfService);
	}*/

	public String getDeviceIPPort() {
		return deviceIPPort;
	}

	// Commented out the below code while testing authentication at hub
	
/*
public boolean checkOutDevice(String zenReplica,String ipAddress,String osPlatform) {
		
		boolean isDeviceCheckedOut=false;
		
		try {
						
			initialiseSTFService();
			 
			log.info("Starting STFService...."+deviceApi);
			boolean isDeviceConnected=deviceApi.isDeviceConnected(this.deviceSerial);
			log.info("Is device connected:"+isDeviceConnected);
			boolean isDeviceAssigned = deviceApi.connectDevice(this.deviceSerial);
			//Check whether it is already checked out
			if(!isDeviceConnected && !isDeviceAssigned)
			{
				log.error("Device is not available, possible errors : Device not available or Access Token might be invalid ");
				return false;
				
			}else {
				isDeviceCheckedOut=true;	
			}
			Thread.sleep(2000);
			
		} catch (URISyntaxException uriSyntaxException) {
			log.error(uriSyntaxException.getMessage(),uriSyntaxException);
		}catch (MalformedURLException malformedURLException) {
			log.error(malformedURLException.getMessage(),malformedURLException);
		}catch(InterruptedException interruptedException)
		{
			log.error(interruptedException.getMessage(),interruptedException);
		}
	
	
	return isDeviceCheckedOut;

	}*/
	
}