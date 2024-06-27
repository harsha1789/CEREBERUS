package com.zensar.automation.library;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.zensar.automation.framework.api.DeviceApi;
import com.zensar.automation.framework.library.PropertyReader;
import com.zensar.automation.framework.model.DeviceInfo;
import com.zensar.automation.framework.model.STFService;
import com.zensar.automation.framework.utils.Constant;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;

/**
 * This is the file to generate the testng configuration files for Mobile and Desktop 
 * @author ak47374
 *
 */
public class ConfigGenerator {
	
	private static Configuration cfg = null;
	Logger log = Logger.getLogger(ConfigGenerator.class.getName());
	static{		
	
		cfg = new Configuration(new Version("2.3.0"));
	}
	 
	public boolean mobileConfigGenerator() {
		
		boolean isDeviceAvailable=false;
		try {
			List<DeviceInfo> androidFinalDeviceList=getFinalDeviceList(Constant.ANDROID);
			List<DeviceInfo> iOSFinalDeviceList=getFinalDeviceList(Constant.IOS);
			List<DeviceInfo> finalDeviceList = new ArrayList<>();
			finalDeviceList.addAll(androidFinalDeviceList);
			finalDeviceList.addAll(iOSFinalDeviceList);
			log.info("Final Device List="+ finalDeviceList);
			if(!finalDeviceList.isEmpty())
			{
			isDeviceAvailable=true;
			String gameName=TestPropReader.getInstance().getProperty("GameName"); 
			// Load template
			Template template = cfg.getTemplate("./"+gameName+"/Config/TestNGMobileXMLTemplate.ftl");
			log.info("Read the mobile xml template");
			// Create data for template
			Map<String, Object> templateData = new HashMap<>();
			

			templateData.put("DeviceList", finalDeviceList);
			templateData.put("gameName", gameName);

			// Write output on console example 1
			StringWriter out = new StringWriter();
			template.process(templateData, out);
			log.debug("Generated from template ::"+out.getBuffer().toString());
			out.flush();
			
			String mobileTestNGPath ="./"+gameName+"/Config/MobileTestNG.xml";
			// Write data to the file
			Writer file = new FileWriter(new File(mobileTestNGPath));
			template.process(templateData, file);
			file.flush();
			file.close();
			TestPropReader.getInstance().setProperty("MobileTestNGPath", mobileTestNGPath);
			log.info("Mobile Test Ng file path="+mobileTestNGPath);
			}
			else
			{
				log.info("No device is available for execution");
			}
		} catch (Exception e) {
			isDeviceAvailable=false;
			log.error(e.getMessage(),e);
		}
		return isDeviceAvailable;
	}
	
	public void desktopConfigGenerator() {
		

		try {

			String gameName=TestPropReader.getInstance().getProperty("GameName");
			
			// Load template
			Template template = cfg.getTemplate("./"+gameName+"/Config/TestNGDesktopXMLTemplate.ftl");
			log.info("Read the Desktop xml template");
			// Create data for template
			Map<String, Object> templateData = new HashMap<>();
			 
			templateData.put("gameName", gameName+"Desktop");

			// Write output on console example 1
			StringWriter out = new StringWriter();
			template.process(templateData, out);
			log.info(out.getBuffer().toString());
			out.flush();
			
			String desktopTestNGPath="./"+gameName+"/Config/DesktopTestNG.xml";
			// Write data to the file
			Writer file = new FileWriter(new File(desktopTestNGPath));
			template.process(templateData, file);
			file.flush();
			file.close();
			TestPropReader.getInstance().setProperty("DesktopTestNGPath", desktopTestNGPath);
			log.info("Mobile Test Ng file path="+desktopTestNGPath);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} 
	}
	
	
	
	public  List<DeviceInfo> getFinalDeviceList(String osPlatform)
	{
		List<DeviceInfo> execDeviceList= new ArrayList<>();
		 
		int maxThreshould=0;
		int count=0;
		TestPropReader.getInstance().setProperty("STF_SERVICE_URL",PropertyReader.getInstance().getProperty("STF_SERVICE_URL"));
		TestPropReader.getInstance().setProperty("ACCESS_TOKEN",PropertyReader.getInstance().getProperty("ACCESS_TOKEN"));
		
		if(osPlatform.equalsIgnoreCase(Constant.ANDROID)) {
			maxThreshould=Integer.parseInt(TestPropReader.getInstance().getProperty("AndroidDevicesMaxThreshold"));
		}else if (osPlatform.equalsIgnoreCase(Constant.IOS)) {
			maxThreshould=Integer.parseInt(TestPropReader.getInstance().getProperty("iOSDevicesMaxThreshold"));
		}
		
		if(maxThreshould == 0) {
			return execDeviceList;
		}
		try{
			CommonUtil commonUtil = new CommonUtil();
			String accessToken =TestPropReader.getInstance().getProperty("ACCESS_TOKEN") ;
			STFService stfService = new STFService(TestPropReader.getInstance().getProperty("STF_SERVICE_URL"),	accessToken);
			DeviceApi deviceApi = new DeviceApi(stfService);
			
			//get all ready device from zen replica
			List<DeviceInfo> deviceInfoList=deviceApi.getAllReadyDevices();
			
			log.info("Device read from zen replica="+deviceInfoList.size());
		
			// Reading the device list from xls sheet in to list map
			List<DeviceInfo> deviceCheckList= commonUtil.readDeviceList();
			log.info("Device Read from testdata  device list:"+deviceCheckList.size());
			
			
			
			
			for(DeviceInfo device: deviceCheckList)
			{
			//Check the OSplatform from excel device list
			if(device.getOsPlatform().equalsIgnoreCase(osPlatform)) {
				log.debug("From the testdata sheet :Device Name"+device.getMarketName());
				log.debug("From the testdata sheet : Height "+ device.display.height);
				log.debug("From the testdata sheet : Width "+ device.display.width);

				for(DeviceInfo deviceInfo : deviceInfoList ){
				if(deviceInfo==null)
					break;
				else	
				{	
					
					log.debug("From the Device farm :Device Name"+deviceInfo.marketName);
					log.debug("From the Device farm : Height "+ deviceInfo.display.height);
					log.debug("From the Device farm : Width "+ deviceInfo.display.width);

					
					if((device.getMarketName().equalsIgnoreCase(deviceInfo.marketName)) && (deviceInfo.display!=null && deviceInfo.display.height==device.display.height && deviceInfo.display.width==device.display.width ) )
					{	
						log.info(device.getMarketName()+" is avilable in device farm");
							if(deviceInfo.present && deviceInfo.ready && deviceInfo.owner==null && count< maxThreshould )
							{
							log.info(device.getMarketName()+" is avilable  for check out");
								boolean isDevicePresentExecList=false;
								for (DeviceInfo execDevice : execDeviceList ){
									if( execDevice.display.height==device.display.height && execDevice.display.width==device.display.width)
									{
										log.info(device.getMarketName()+" another device with same dimentions is already present");

										isDevicePresentExecList=true;
										
										}							
								}
									
								if(!isDevicePresentExecList){
									
									if (deviceApi.connectDevice(deviceInfo.getSerial()))
									{
										execDeviceList.add(deviceInfo);
										count++;
										deviceInfo.setCheckedOutDeviceNum(count);
										deviceInfo.setTestName("Regression Suite for Device "+deviceInfo.marketName);
										String randomUserName=commonUtil.createRandomUser();
										deviceInfo.setUsername(randomUserName);
										deviceInfo.setOsPlatform(osPlatform);
										if(osPlatform.equalsIgnoreCase(Constant.ANDROID)) {
											deviceInfo.setBrowserName(Constant.CHROME);
										}
										else
										{
											deviceInfo.setBrowserName(Constant.SAFARI);
										}
										log.info(device.getMarketName()+" is added to final device list");
										
									}
									else
									{
										log.info("Not able to connect to device "+deviceInfo.getSerial()+" Current status :"+deviceInfo.getReady());
									}
								
								}
								
							}
							else if(count>=maxThreshould)
							{
								break;
							}
						
					}
					
				}
			
			}
			}//End of os platform Check
			if(count>=maxThreshould)
			{
				break;
			}
			}	
			}catch(Exception e)
			{
				log.error("Exception in getFinalDeviceList():", e);
			}
			log.info("Final Device List count="+execDeviceList.size());
			
		return execDeviceList;
		}//end of methode
	
	
	
}//end of class
