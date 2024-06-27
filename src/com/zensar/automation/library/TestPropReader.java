package com.zensar.automation.library;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.library.PropertyReader;
import com.zensar.automation.framework.library.UnableToLoadPropertiesException;
import com.zensar.automation.model.EnvList;
import com.zensar.automation.model.VirtualMachines;
	//default constructor
	//load default properties(String token)
	//load game specific propertie ie.test env reding(String gamename)
	//load env specific properties(String envname)
	//load all properties (Token, envname,gameName)
public class TestPropReader extends PropertyReader{

	
	Logger logger = Logger.getLogger(TestPropReader.class.getName());
	static TestPropReader testInstance =null;
	
	private TestPropReader()
	{
		super();
	}

	public static TestPropReader getInstance(){
		if (testInstance == null) {
			testInstance = new TestPropReader();
			
		}
		return testInstance;
	}
	@Override
	// Get and set method to access the properties 
	public String getProperty(String key) {
		return prop.getProperty(key);
	}
	@Override
	public void setProperty(String key, String value) {

		prop.setProperty(key, value);
	}

	
	/*Load BlueMesa environment related properties example Casinoas1IP,serverIp etc. and return boolean
	 * @param String  BlueMesa environment id
	 * */
	public boolean loadBlueMesaEnvProperties(int envID) throws UnableToLoadPropertiesException
	{
		boolean isBlueMesaEnvPropLoaded=false;
		RestAPILibrary restAPILibrary = new RestAPILibrary();

		try{
			logger.info("Loading BlueMesa Environment properties inprogess...");
			if(envID !=0){
				logger.debug("Loading BlueMesa  properties for Environment id "+envID);
				EnvList envList= restAPILibrary.getBlueMesaEnvMetaData(envID);

				List<VirtualMachines> vmList = envList.getDataObject().getVirtualMachines();

				for (VirtualMachines virtualMachines: vmList){
					if(virtualMachines.getName().equalsIgnoreCase(getProperty("GamingDB"))){
						setProperty("serverIp", virtualMachines.getExternalIpAddress());

					}else if(virtualMachines.getName().equalsIgnoreCase("CasinoAS1")){
						setProperty("Casinoas1IP", virtualMachines.getExternalIpAddress());
					}
					else if(virtualMachines.getName().equalsIgnoreCase("RabbitMQ")){
						setProperty("RabbitMQ", virtualMachines.getExternalIpAddress());
					}else if(virtualMachines.getName().equalsIgnoreCase("WebServer4")){
						setProperty("WebServer4", virtualMachines.getExternalIpAddress());
					}


				}
				isBlueMesaEnvPropLoaded=true;
				logger.info("loading completed for BlueMesa Environment properties");
			}
		}
		catch (Exception e) {
			logger.error("Exception occur in Testpropreader :",e);
			throw new UnableToLoadPropertiesException(e.getMessage(),e);

		}

		return isBlueMesaEnvPropLoaded;
	}

	/*Load  properties example return boolean
	 * @param string Game Name
	 * @param String  BlueMesa environment id
	 * 
	 * */

	public boolean loadAllProperties(String gameName,String strEnvName) throws UnableToLoadPropertiesException
	{
		boolean isPropLoaded=false;
		try{
			RestAPILibrary restAPILibrary = new RestAPILibrary();
			logger.info("Loading all properties in progress..");
			
			isPropLoaded=PropertyReader.getInstance().loadDefaultProperties();
			if(!isPropLoaded)
			{
				logger.debug("Default properties not loaded");
				return isPropLoaded;
			}
			isPropLoaded=PropertyReader.getInstance().loadGameProperties(gameName);
			if(!isPropLoaded)
			{
				logger.debug("Game Test environment properties not loaded");
				return isPropLoaded;
			}
			PropertyReader.getInstance().getProp().forEach((key, value) -> setProperty((String)key,(String)value));
			//following are the changes done for run the automation on axiom lobby 
			
			if( TestPropReader.getInstance().getProperty("EnvironmentName")==null ||
				TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
			{
				int envid=restAPILibrary.getEnvironmentId(strEnvName);
				setProperty("EnvironmentID", Integer.toString(envid));
				
				isPropLoaded=loadBlueMesaEnvProperties(envid);
				if(!isPropLoaded)
				{
					logger.debug("fail to load bluemesa properties ");
					return isPropLoaded;
				}
			}
			setProperty("ExecutionEnv", strEnvName);
		}
		catch (Exception e) {
			logger.error("Exception occur in Testpropreader:",e);
			throw new UnableToLoadPropertiesException(e.getMessage(),e);

		}
		return isPropLoaded;
	}
	
	
	public boolean loadAllProperties(String gameName,int envId) throws UnableToLoadPropertiesException
	{
		boolean isPropLoaded=false;
		try{
			logger.info("Loading all properties in progress..");
			
			
			PropertyReader.getInstance().loadDefaultProperties();
			PropertyReader.getInstance().loadGameProperties(gameName);
			PropertyReader.getInstance().getProp().forEach((key, value) -> setProperty((String)key,(String)value));
			if( TestPropReader.getInstance().getProperty("EnvironmentName")==null ||
					TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
			{
				loadBlueMesaEnvProperties(envId);
				setProperty("EnvironmentID", Integer.toString(envId));
			}
			
			isPropLoaded=true;
			
		}
		catch (Exception e) {
			logger.error("Exception occur in Testpropreader:",e);
			throw new UnableToLoadPropertiesException(e.getMessage(),e);

		}
		return isPropLoaded;
	}
	
	
	
	
}//end of class
