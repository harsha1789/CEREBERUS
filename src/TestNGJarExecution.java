
import java.util.List;
import org.apache.log4j.Logger;
import org.testng.TestNG;
import com.beust.jcommander.internal.Lists;

import com.zensar.automation.framework.library.UnableToLoadPropertiesException;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.library.TestPropReader;

/**
 * Run the TestNG suite file from jar 
 * runtimre parametres require as
 * 1]Platform (Desktop/Mobile) 
 * 2]Game Name
 * 2]EnvironmentName   
 * */
public class TestNGJarExecution {
	

	public static void main(String[] args) {
		// Declaration and initializaton of local varibles
		TestNG testng = new TestNG();
		List<String> suites = Lists.newArrayList();
		String gameName=null;
		String strEnvName=null;
		String gameVersion = args.length>=4?args[3]:"Default";

		try{
			
			//Storing arguments in local varibles
			gameName=args[1].trim();
			strEnvName=args[2].trim();
			System.setProperty("logfilename",gameName+"/Selenium");
			if(!gameVersion.equalsIgnoreCase("Default"))
			{
				if(!(strEnvName.contains("GTP")||strEnvName.contains("gtp")))
				{

				if(gameVersion.contains("-alpha")){
					gameVersion= gameVersion.replace("-alpha", "");
				} else if (gameVersion.contains("-rc")){
					gameVersion= gameVersion.replace("-rc", "");
				}  else if (gameVersion.contains("-hf")){
					gameVersion= gameVersion.replace("-hf", "");
				} 
				}
				else
				{
					gameVersion=gameVersion.replaceAll("[^.0-9]", "");
				}

			}
		}catch(ArrayIndexOutOfBoundsException e){

			System.out.println("Mandatory parameter game Name and environmentName is missing");
		}
		// creating logger 
		Logger log = Logger.getLogger(TestNGJarExecution.class.getName());

		if(gameName != null && strEnvName !=null)
		{
			try{
				
				//Load initial test propeties
				boolean isTestPropertiesLoaded=TestPropReader.getInstance().loadAllProperties(gameName, strEnvName);
				if(isTestPropertiesLoaded)
				{
					TestPropReader.getInstance().setProperty("gameVersion", gameVersion);
				//run the correct testng suite file depending upone the platform
				if( args.length==0 || args[0] == null || args[0].equalsIgnoreCase(Constant.DESKTOP) ){
					suites.add("./"+gameName+"/Config/Desktop_Suite.xml");
				} else if (args[0].equalsIgnoreCase(Constant.MOBILE)){
					suites.add("./"+gameName+"/Config/Mobile_Suite.xml");	
				}

				testng.setTestSuites(suites);
				//run the TestNGsuite 
				testng.run();
				}
				else
				{
					log.error("Unable to load test property file, hence skipping the execution\n");	
				}
			}catch(UnableToLoadPropertiesException e)
			{
				log.error("Unable to load test property file\n"+e.getStackTrace());
			}
		}else
		{
			log.info("Excution skipped as mandatory parameter missing, please provide game name and env ID  parameters");
		}
	}
}
