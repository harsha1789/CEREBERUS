package com.zensar.automation.executors;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.testng.TestNG;

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
import com.zensar.automation.framework.utils.Util;
import com.zensar.automation.library.ConfigGenerator;
import com.zensar.automation.library.DataBaseFunctions;
import com.zensar.automation.library.TestPropReader;
import com.zensar.automation.model.Currency;
import com.zensar.automation.model.GameMetaData;
import com.zensar.automation.model.InstalledGameMetaData;
import com.zensar.automation.model.PatchDataResponse;
import com.zensar.automation.model.Preset;
import com.zensar.automation.model.PresetResponse;
/**
 * This class is the entry point for CICD execution of  games
 * it will process the parameter ,do the pre execution steps and execute the test script accordingly
 * @author sg56207
 *
 */
public class ExecutionIntiator {

	static	Logger log = Logger.getLogger(ExecutionIntiator.class.getName());


	@SuppressWarnings("unused")
	public static void main(String[] args) {
		//builds a new report using the html template 
		ExtentHtmlReporter htmlReporter;
		ExtentReports extent=null;
		ExtentTest report=null;
		try {

			if (args.length >= 8) {
				String strMID= args[0];
				String strCIDMobile = args[1];
				String strCIDDesktop = args[2];
				String strGameName = args[3];
				String strEnvName = args[4];
				String strToken=args[5];
				String strPatchID= args.length>=7?args[6]:"0";
				String gameVersion = args.length>=8?args[7]:"Default";
				
				
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
				//initialising folder to store report and logs
				String reportFolder="./"+strGameName+"/Reports/"+strToken;
				Util globalcfnLib=new Util();
				if(globalcfnLib.createFolder(reportFolder))
				{
					log.info("folder to store report and logs created successfully");
				}
				else
					log.info("folder to store report and logs not created ");
				// initialize the HtmlReporter  	
				htmlReporter= new ExtentHtmlReporter(reportFolder+"/ExecutionSummaryReport.html");

				//initialize ExtentReports and attach the HtmlReporter
				extent= new ExtentReports();
				extent.attachReporter(htmlReporter);


				htmlReporter.config().setChartVisibilityOnOpen(true);
				htmlReporter.config().setDocumentTitle("Execution summary");
				htmlReporter.config().setReportName("Test Script Execution summary");

				htmlReporter.config().setTheme(Theme.STANDARD);
				htmlReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");

				report=extent.createTest("CICD Execution summary");


				log.info("Input 1: Cid=" + strCIDMobile);
				log.info("Input 2: Mid=" + strMID);
				log.info("Input 3: Cid Desktop=" + strCIDDesktop);
				log.info("Input 4: game Name=" + strGameName);
				log.info("Input 5: envName=" + strEnvName);
				log.info("Input 6: patchID=" + strPatchID);
				log.info("Input 7: Token=" + strToken);
				log.info("Input 8: GameVersion=" + gameVersion);
				
				ExecutionIntiator exeinitobj = new ExecutionIntiator();
				ConfigGenerator configGenerator = new ConfigGenerator();
				RestAPILibrary apiObj = new RestAPILibrary();
				InstalledGameMetaData gameMetaData;
				String envType;
				boolean isDesktopUserCreated = false;
				boolean isMobileUserCreated = false;
				
				System.setProperty("Token",strToken);
				
				if(strEnvName!=null && strEnvName.toUpperCase().startsWith("GTP")) {
					envType="axiom";
					TestPropReader.getInstance().setProperty("EnvironmentName",envType);
				}else {
					envType="bluemesa";
					TestPropReader.getInstance().setProperty("EnvironmentName",envType);
				}
				TestPropReader.getInstance().loadAllProperties(strGameName, strEnvName);
				TestPropReader.getInstance().setProperty("MID", strMID);
				TestPropReader.getInstance().setProperty("CIDDesktop", strCIDDesktop);
				TestPropReader.getInstance().setProperty("CIDMobile", strCIDMobile);
				
				int cidMobile=Integer.parseInt(strCIDMobile);
				int mid=Integer.parseInt(strMID);
				int cidDesktop=Integer.parseInt(strCIDDesktop);
				int patchId= Integer.parseInt(strPatchID.trim());
				int envid = 0;
				String mobileUrl=null;
				String desktopUrl=null;
				
				if(envType.equalsIgnoreCase("bluemesa")) {
					envid=Integer.parseInt(TestPropReader.getInstance().getProperty("EnvironmentID"));
				}
				
				/*Remove the below after testing
				 * if( TestPropReader.getInstance().getProperty("EnvironmentName") == null ||
					TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
					{
					envid=Integer.parseInt(TestPropReader.getInstance().getProperty("EnvironmentID"));
					}
				if(TestPropReader.getInstance().getProperty("EnvironmentName")!=null)
				{
					lobbyName=TestPropReader.getInstance().getProperty("EnvironmentName").toLowerCase();
				}
				else
				{
					lobbyName="bluemesa";
					TestPropReader.getInstance().setProperty("EnvironmentName","bluemesa");
				}*/
				
				
				
			
				log.info("Installing game in progress.....");

				
				/*int randomwait=Integer.parseInt(TestPropReader.getInstance().getProperty("MaxRandomWaitTime"));
				int waitTime=new Random().nextInt(randomwait)*1000;
				log.info("Random waiting for :"+waitTime);
				//Thread.sleep(waitTime);
*/
				boolean isGameInstalled=false;
				String strGameInstalled = TestPropReader.getInstance().getProperty("isGameInstalled");
				log.info("Is game already Installed :: "+strGameInstalled);
				if(strGameInstalled != null && Constant.FALSE.equalsIgnoreCase(strGameInstalled))
				{
				log.info("Going to install and setup the game"+strGameInstalled);
				String etcUserID= TestPropReader.getInstance().getProperty("EtcUserId");
				isGameInstalled = exeinitobj.installGame(mid, cidMobile, envid,etcUserID,patchId);
				log.info("Mobile game installation status:"+isGameInstalled);
				}
				else{
					log.info("Skipping installation and setup"+strGameInstalled);
					isGameInstalled=true;
				}
				
				//commented below code as currently not supporting on desktop 
				//	boolean isDesktopGameInstall= exeinitobj.installGame(mid, cidDesktop, envid, etcUserID,patchId);

				//	log.info("Desktop game installation status:"+isDesktopGameInstall);


				if (isGameInstalled) {
					report.log(Status.PASS, MarkupHelper.createLabel(" Game Installtion status:PASS ",  ExtentColor.GREEN));
					log.info("Game Installed Success.........");
					log.info("Copying test data on casinoAS1...");
					List<String> copiedFiles=new ArrayList<>();
					//int threshold=Integer.parseInt(TestPropReader.getInstance().getProperty("MaxThreshold"));
					//int waitTime=120000+(new Random().nextInt(7))*10000;
					//log.debug("Wait time after installantionand test data "+waitTime);
					//boolean isTestdatacopied=exeinitobj.copyTestDataFiles(mid,cidDesktop,cidMobile,strGameName,copiedFiles);
					//Waiting for lobby to load test data
					//Thread.sleep(420000);
					boolean isTestdatacopied=true;
					log.info("Test data copy status:"+isTestdatacopied);
					if(isTestdatacopied)
					{
						report.log(Status.PASS, MarkupHelper.createLabel(" Test data copy status:PASS ",  ExtentColor.GREEN));

						String desktopUSer="Zen_CICD_Desktop";
						String mobileUser="Zen_CICD_Mobile";
						
						
						switch(envType)
						{
							case "bluemesa":isDesktopUserCreated=exeinitobj.createUser(desktopUSer);
												break;
							case "axiom"	:isDesktopUserCreated=true;
											 apiObj.createUserInAxiom(desktopUSer);
												break;
							default			: break;
						}
						
						
						if(isDesktopUserCreated)
						{
							report.log(Status.PASS, MarkupHelper.createLabel(" Create user for desktop status:PASS ",  ExtentColor.GREEN));

							TestPropReader.getInstance().setProperty("DesktopUser", desktopUSer);
						}
						else
						{
							report.log(Status.FAIL, MarkupHelper.createLabel(" Create user for desktop status:FAIL ",  ExtentColor.RED));
						}
						switch(envType)
						{
							case "bluemesa":isMobileUserCreated=exeinitobj.createUser(mobileUser);
												break;
							case "axiom"	:isMobileUserCreated=true;
											 apiObj.createUserInAxiom(mobileUser);
												break;
									default: break;
						}
						if(isMobileUserCreated)
						{
							report.log(Status.PASS, MarkupHelper.createLabel(" Create user for mobile status:PASS ",  ExtentColor.GREEN));
							TestPropReader.getInstance().setProperty("MobileUser", mobileUser);
						}
						else
						{
							report.log(Status.FAIL, MarkupHelper.createLabel(" Create user for mobile status:FAIL ",  ExtentColor.RED));
						}
						
						
						if(isMobileUserCreated)
						{
							// Create game URL
							switch(envType)
							{
								case "bluemesa": mobileUrl= exeinitobj.getGameUrl(envid, "en", Constant.MOBILE, strGameName, mobileUser,TestPropReader.getInstance().getProperty("serverID"),"IslandParadise",gameVersion);
												 desktopUrl= exeinitobj.getGameUrl(envid, "en", Constant.DESKTOP, strGameName+"Desktop", desktopUSer,TestPropReader.getInstance().getProperty("serverID"),"IslandParadise",gameVersion);
													break;
								case "axiom"	:gameMetaData=apiObj.getInstalledGameMetaData();
												//String gameDisplayNameMob=exeinitobj.getGameDisplyName(gameMetaData,mid,cidMobile);
												//String gameDisplayNameDesk=exeinitobj.getGameDisplyName(gameMetaData, mid, cidDesktop);
												 mobileUrl=apiObj.axiomCreateURL(mobileUser, gameVersion,mid,cidMobile,"mobile");
								                 desktopUrl=apiObj.axiomCreateURL(desktopUSer, gameVersion,mid, cidDesktop,"desktop");
												break;
										default: break;
							}
							
							

							if (mobileUrl != null && desktopUrl!=null ) {
								report.log(Status.PASS, MarkupHelper.createLabel(" Create URL status:PASS ",  ExtentColor.GREEN));

								TestPropReader.getInstance().setProperty("GameDesktopUrl",desktopUrl) ;
								TestPropReader.getInstance().setProperty("GameMobileUrl", mobileUrl);

								//boolean isMobileGameAvailable=true;
								boolean isMobileGameAvailable = exeinitobj.verifyGameAvailability(mid, cidMobile, mobileUser);
								log.info("isMobileGameAvailable status:"+isMobileGameAvailable);
								//commenting for testing on multiple devices , uncomment if require
								/*boolean isDesktopGameAvailable=exeinitobj.verifyGameAvailability(mid, cidDesktop, desktopUSer);
								log.info("isDesktopGameAvailable status:"+isDesktopGameAvailable);
*/	
								boolean isDesktopGameAvailable=true;
								// get Available device list from zen repliica and build
								// mobile xml configuration and Desktop xml configuration
								if(isMobileGameAvailable && isDesktopGameAvailable)
								{
									report.log(Status.PASS, MarkupHelper.createLabel(" game available status:PASS ",  ExtentColor.GREEN));

									boolean isDevicesCheckedOut=configGenerator.mobileConfigGenerator();
									configGenerator.desktopConfigGenerator();

									// object of testNG class
									TestNG runner = new TestNG();
									// create a list of testng xml files
									List<String> suitfile = new ArrayList<>();
									//Commented for CICD on IS server ,please make thred pool size 2 for both suite to run
									//suitfile.add(TestPropReader.getInstance().getProperty("DesktopTestNGPath"));
									if(isDevicesCheckedOut)
									{
										report.log(Status.PASS, MarkupHelper.createLabel(" Device checkedout status:PASS ",  ExtentColor.GREEN));
										suitfile.add(TestPropReader.getInstance().getProperty("MobileTestNGPath"));
										runner.setSuiteThreadPoolSize(1);
									}
									else
										report.log(Status.FAIL, MarkupHelper.createLabel(" Device checkedout status:FAILED ",  ExtentColor.RED));
									// set xml file for execution
									runner.setTestSuites(suitfile);
									runner.run();
									log.info("Execution started for Mobile and Desktop ....");
									
									/*Commenting below code as testing with axiom environment
									 * //Deleting test data files from casino 
									log.info("Deleting test data files");
									exeinitobj.deleteTestDataFiles(mid,cidMobile,cidDesktop,copiedFiles);*/
								}
								else
								{
									report.log(Status.FAIL, MarkupHelper.createLabel(" Game available status:FAILED ",  ExtentColor.RED));

								}
							}
							else
							{
								report.log(Status.FAIL, MarkupHelper.createLabel(" Create URL status:FAILED ",  ExtentColor.RED));

							}
						}else{
							report.log(Status.FAIL, MarkupHelper.createLabel(" Unable to create User ",  ExtentColor.RED));

						}
					}
					else
					{
						report.log(Status.FAIL, MarkupHelper.createLabel(" Test data copy status:FAILED ",  ExtentColor.RED));
						log.error("Fail to copy test data file on casinoAS1..");
					}
				} 
				else{
					report.log(Status.FAIL, MarkupHelper.createLabel(" Game Installtion status:FAILED ",  ExtentColor.RED));

					log.error("Fail to instal game...");
				}
			}

			else {
				log.info(
						"oops...!Arguments missing ,Plase provide the valide Input as cid ,mid,cid for desktop,gameName and environment ID");
			}

		}catch(UnableToLoadPropertiesException e)
		{

			report.log(Status.FAIL, MarkupHelper.createLabel(" Fail to load Test property ",  ExtentColor.RED));

			log.info("Error in creating instance of Test.property ");

		} 
		catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		finally
		{
			try{
				if(extent!=null)
					extent.flush();
				
			}catch(Exception e)
			{
				log.error("Exception occur while extent object close",e);
				
			}
		}
	}

	/**
	 * Method to get the Game Name display in axiom lobby
	 * @param gameMetaData
	 * @param mid
	 * @param cid
	 * @return String
	 */

	public  String getGameDisplyName(InstalledGameMetaData gameMetaData, int mid, int cid ) {
		
		String strmid=String.valueOf(mid);
		String strcid=String.valueOf(cid);
		String gameDisplayName=null;
		List<GameMetaData> installGameList=gameMetaData.getDataObject();
		for(GameMetaData installGame : installGameList)
		{
			if(installGame.moduleId.equalsIgnoreCase(strmid)&& installGame.clientId.equalsIgnoreCase(strcid))
			{
				gameDisplayName=installGame.internalName;
				break;
			}
		}
		return gameDisplayName;
	}
	/**
	 * Description: This function read the preset of given mid and cid it will
	 * try to install preset for maximum 3 times in case of failure .
	 * 
	 * @return: response of getPresets api
	 */
	public PresetResponse getPresets(int mid, int cid) {
		int repeatcnt = 0;
		RestAPILibrary apiobj = new RestAPILibrary();
		PresetResponse presetResponse = null;
		try {
			do {
				presetResponse = apiobj.getPresets(mid, cid);
				if (presetResponse == null && repeatcnt < 3) {
					log.info("getPresets failed . Repeate count="+repeatcnt);
					Thread.sleep(60000);
					repeatcnt++;
				}

			} while (repeatcnt == 3);
		} catch (Exception e) {
			log.error("Exception in getPresets", e);
		}
		return presetResponse;
	}
	/**
	 * Description: read the patchdata for given patch id. it will try to
	 * install preset for maximum 3 times in case of failure .
	 * 
	 * @return : response of getpatchdata api
	 */

	public PatchDataResponse getPatchData(int mid, int cid, int envid, int patchid, String username) {
		int repeatcnt = 0;
		PatchDataResponse patchdata = null;
		RestAPILibrary apiobj = new RestAPILibrary();
		try {
			do {
				patchdata = apiobj.getpatchdata(envid, patchid, username);
				if (patchdata == null && repeatcnt < 3) {
					log.info("getpatchdata failed . Repeate count="+repeatcnt);
					PresetResponse presetResponse = getPresets(mid, cid);
					patchid = getLatestSignedoffPatchid(presetResponse);
					Thread.sleep(60000);

					repeatcnt++;
				}

			} while (repeatcnt == 3);
		} catch (Exception e) {
			log.error("Exception in getPresets", e);
		}
		return patchdata;
	}

	/**
	 * Description: this function will install the preset it will try to install
	 * preset for maximum 3 times in case of failure .
	 * 
	 * @return:response of installPreset api
	 */
	public String installPreset(int envid, int mid, int cid, String username, String database, String architecture) {
		String response = null;
		int patchid;
		PatchDataResponse patchdata=null;
		RestAPILibrary apiobj = new RestAPILibrary();
		int repeatcnt = 0;
		try {
			do {

				response = apiobj.installPreset(envid, mid, cid, username, database, architecture);
				if (!"null".equalsIgnoreCase(response) && repeatcnt < 3) {
					repeatcnt++;
					log.info("Preset installation failed . Repeate count="+repeatcnt);
					Thread.sleep(60000);
					PresetResponse presetResponse = getPresets(mid, cid);
					patchid = getLatestSignedoffPatchid(presetResponse);

					patchdata = getPatchData(mid, cid, envid, patchid, username);
					if(patchdata!=null)
						architecture = patchdata.getArchitecture();
				}
				else{
					break;
				}
			} while (repeatcnt < 3 );
		} catch (Exception e) {
			log.error("Exception in installPreset", e);
		}
		return response;
	}

	/**
	 * Description:Read the LatestSigned off patchid
	 * 
	 * @input: preset res
	 * @return: int patchid: signedoff patch id
	 */
	public int getLatestSignedoffPatchid(PresetResponse presetResponse) {
		int latestpatchid;
		Preset[] presets = presetResponse.getPresets();
		latestpatchid=presets[0].getPatchID();
		log.info("finding latest  signedoff patchid");
		for (Preset preset : presets) {

			if (preset.getPatchStatus().equalsIgnoreCase("Signed Off") && preset.getPatchID() > latestpatchid)
			{
				latestpatchid = preset.getPatchID();
			}
		}
		log.info("latest patch ID:"+latestpatchid);
		return latestpatchid;
	}


	/**
	 * Description: This method checks whether the passed patcId is valid or not.
	 * basically, it checks passed patchId is relevant to actual mid and cid or not.
	 * 
	 * @input: PresetResponse presetResponse,int patchId
	 * @return: boolean
	 */
	public boolean isValidPatchid(PresetResponse presetResponse,int patchId) {
		log.debug("on ENtry of isValidPatchid method ");
		boolean validPatchId=false;
		if(presetResponse!=null){
			Preset[] presets = presetResponse.getPresets();
			for (Preset preset : presets) {

				if (patchId==preset.getPatchID())
				{
					validPatchId=true;
					break;
				}
			}
		}
		log.debug("is a vailid PatchId :: "+ validPatchId);
		return validPatchId;
	}


	/**
	 * Description: this function download the presets,read singed off
	 * patchdata, download the patch ,and install the patch
	 * @input: int mid, int cid
	 * @return boolean isSetupCompleted
	 * 
	 */
	public boolean installGame(int mid, int cid, int envid, String username,int paramPatchID) {


		int latestSignOffPatchId = 0;
		boolean isSetupCompleted = false;
		PresetResponse presetResponse = null;
		PatchDataResponse patchdata = null;


		try {

			presetResponse = getPresets(mid, cid);

			if (presetResponse != null) {

				latestSignOffPatchId = getLatestSignedoffPatchid(presetResponse);
				log.info("latest signed off patchid found. " + latestSignOffPatchId);
				log.info("reading patchdata for patchid" + latestSignOffPatchId);

				int patchId;
				if(paramPatchID == 0){
					log.info("Patch ID parameter is not passed hence considering latest signoff patchId :: " + latestSignOffPatchId+" to install");
					patchId=latestSignOffPatchId;					
				}else if(!isValidPatchid(presetResponse, paramPatchID)){
					log.info("Patch ID parameter which is :: "+paramPatchID+" not the valid one. hence installing with latest signedoff patch id:: " + latestSignOffPatchId);
					patchId=latestSignOffPatchId;	
				}else if(paramPatchID != latestSignOffPatchId){
					log.info("Patch ID parameter which is"+paramPatchID+" not the lateset signedoff :: " + latestSignOffPatchId+". However installing with parameterized patchId");
					patchId=paramPatchID;					
				}else 
				{
					log.info("Patch ID parameter which is :: "+paramPatchID+" is also the lateset signedoff :: " + latestSignOffPatchId+". Hence installing it. ");
					patchId=paramPatchID;
				}

				patchdata = getPatchData(mid, cid, envid, latestSignOffPatchId, username);

				if (patchdata != null) {

					String architecture = patchdata.getArchitecture();

					log.info("read patchdata successfully and getting game architecture version=" + architecture);

					log.info("Installing presets...");
					String result = installPreset(envid, mid, cid, username, TestPropReader.getInstance().getProperty("GamingDB"), architecture);
					if ("null".equalsIgnoreCase(result)) {
						log.info("Preset installed..." + result);
						isSetupCompleted = true;
					} else {
						log.info("Response for preset install=" + result);
						isSetupCompleted = false;
					}

				} else {

					log.info("patch data response is null");
					isSetupCompleted = false;
				}

			} // if loop
			else {
				log.info("No response fron preset Api after maximum no of tries.");
				isSetupCompleted = false;
			}
		} catch (Exception e) {
			log.error("Exception occur installGame() ", e);
		}
		return isSetupCompleted;

	}

	/**
	 * Description: This function create game url
	 * 
	 * @input: int envid: Environment id on which enviornment want to create url
	 * String language:language of game String host: Mobile/Desktop host String
	 * gameName: game name String playerName: player name String serverid:
	 * serverid String lobbyName: lobby name
	 * 
	 * @return : String gameurl.
	 */
	public String getGameUrl(int envid, String language, String host, String gameName, String playerName,
			String serverid, String lobbyName,String gameVersion) {
		String url = null;
		int repeatcnt = 0;
		RestAPILibrary apiobj = new RestAPILibrary();
		try {
			do {
				
				String frameWorkType =TestPropReader.getInstance().getProperty("FrameWorkType");
				if(frameWorkType==null||frameWorkType.equalsIgnoreCase("V")||frameWorkType.equals("")){
					url = apiobj.createURL(envid, language, host, gameName, playerName, serverid, lobbyName,gameVersion);
				}else if(frameWorkType.equalsIgnoreCase("Titan")){
					gameVersion= gameVersion.replace(".", "_");
					url = apiobj.createTitanURL(envid, language, host, gameName, playerName, serverid, lobbyName,gameVersion);
				}
				
				if (url == null) {
					repeatcnt++;
					Thread.sleep(10000);
				} else {
					Pattern p = Pattern.compile("\\\\u0026");
					Matcher m = p.matcher(url);

					if (m.find()) {
						url = m.replaceAll("&");
					}
					log.info("Game Url for host in getGameUrl method : for host "+host+": "+url);
					break;

				}
			} while (repeatcnt < 3);
			if (repeatcnt == 3) {
				log.info("Error in getting url,tried for maximum time...");
			}
		} catch (Exception e) {
			log.error("Exception occur in getGameUrl", e);
		}

		return url;
	}

	/**
	 * Description: This function check the game avilability .
	 * 
	 * @input: url,mid,cid,playername
	 * 
	 * @return: boolean isGameAvilable.
	 */
	public boolean verifyGameAvailability(int mid, int cid, String palyerName) {
		RestAPILibrary apiobj = new RestAPILibrary();
		int repeatCnt=0;
		boolean isGameAvilable=false;
		try{
			do {
				boolean status = apiobj.isGameLaunch(mid, cid, palyerName);

				if (!status)
				{
					repeatCnt++;
					Thread.sleep(10000);
				} else {
					isGameAvilable = true;
					break;

				}
			} while (repeatCnt < 3);


		}catch(Exception e)
		{
			log.error("Exception in Verify game availability",e);
		}

		return isGameAvilable;
	}
	
	/**
	 * THis method used to copy testdata files 
	 * @param mid
	 * @param cidDesktop
	 * @param cidMobile
	 * @param gameName
	 * @param copiedFiles
	 * @return
	 */
	public boolean copyTestDataFiles(int mid,  int cidDesktop,int cidMobile, String gameName,List<String> copiedFiles) {

	/*	boolean isTestDataCopied = copyCurrencyTestData(mid,cidDesktop,"Desktop", gameName,copiedFiles);
		if (!isTestDataCopied) {
			return false;
		}
			isTestDataCopied = copyCurrencyTestData(mid,cidMobile,"Mobile", gameName,copiedFiles);
		if (!isTestDataCopied) {
			return false;
		}*/
		boolean copyTestFiles = false;
		int maxThreshold=Integer.parseInt(TestPropReader.getInstance().getProperty("MaxThreshold"));

		File folder = new File("./" + gameName + "/TestData");

		File[] files = folder.listFiles();

		for (File file : files) {

			if (file.getName().contains("BigWin")&& file.getName().contains("Desktop")) {
				String capGameName = gameName.replaceAll("[a-z0-9]", "").trim();
				String userName = "BigWin_" + capGameName;
				copyFilesToTestServer(mid,cidDesktop,file, userName,copiedFiles);
			}else if (file.getName().contains("BigWin")&& file.getName().contains("Mobile")) {
				String capGameName = gameName.replaceAll("[a-z0-9]", "").trim();
				String userName = "BigWin_" + capGameName;
				copyFilesToTestServer(mid,cidMobile,file, userName,copiedFiles,maxThreshold);
			} 
			else if (file.getName().contains("FreeSpin")&& file.getName().contains("Desktop")) {
				String capGameName = gameName.replaceAll("[a-z0-9]", "").trim();
				String userName = "FreeSpin_" + capGameName;
				copyFilesToTestServer(mid,cidDesktop,file, userName,copiedFiles);
			}else if (file.getName().contains("FreeSpin")&& file.getName().contains("Mobile")) {
				String capGameName = gameName.replaceAll("[a-z0-9]", "").trim();
				String userName = "FreeSpin_" + capGameName;
				copyFilesToTestServer(mid,cidMobile,file, userName,copiedFiles,maxThreshold);
			} 
			else if (file.getName().contains("FreeGames")&&file.getName().contains("Desktop")) {
				String capGameName = gameName.replaceAll("[a-z0-9]", "").trim();
				String userName = "FreeGames_" + capGameName;
				copyFilesToTestServer(mid,cidDesktop,file, userName,copiedFiles);
			} else if(file.getName().contains("FreeGames")&&file.getName().contains("Mobile")) {
				String capGameName = gameName.replaceAll("[a-z0-9]", "").trim();
				String userName = "FreeGames_" + capGameName;
				copyFilesToTestServer(mid,cidDesktop,file, userName,copiedFiles,maxThreshold);
			}

		}

		return true;
	}

	/**
	 * This method copy the list of test data files to test server
	 * @param mid
	 * @param cid
	 * @param file
	 * @param userName
	 * @param copiedFiles
	 * @return
	 */
	public boolean copyFilesToTestServer(int mid,int cid,File file, String userName,List<String> copiedFiles) {
		boolean isTestDataCopied=false;
		RestAPILibrary apiobj= new RestAPILibrary();
		// creating the object of Global function Library
		Util gcfnlib = new Util();
		String fileName=TestPropReader.getInstance().getProperty("TestDataFileName").trim()+"_"+mid+"_"+cid+"_"
				+ userName + ".testdata";
		
		
		if( TestPropReader.getInstance().getProperty("EnvironmentName")==null ||
				TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
		{
			try{
				log.info("ServerName:"+ TestPropReader.getInstance().getProperty("ServerName"));
				log.info("dataBaseName:"+ TestPropReader.getInstance().getProperty("dataBaseName"));
				log.info("serverIp:"+ TestPropReader.getInstance().getProperty("serverIp"));
				log.info("Port:"+ TestPropReader.getInstance().getProperty("port"));
				log.info("serverID:"+ TestPropReader.getInstance().getProperty("serverID"));
				// Creating database object name as "dbobject"
				DataBaseFunctions dbobject = new DataBaseFunctions(TestPropReader.getInstance().getProperty("ServerName"),
						TestPropReader.getInstance().getProperty("dataBaseName"),
						TestPropReader.getInstance().getProperty("serverIp"), TestPropReader.getInstance().getProperty("port"),
						TestPropReader.getInstance().getProperty("serverID"));
				dbobject.createUser(userName, "0", 0);

				String destFile = "smb://" + TestPropReader.getInstance().getProperty("Casinoas1IP").trim()
						+ "/c$/MGS_TestData/" + fileName;

				// --------Update the Xml File of test Data with currency user
				// name----------//

				gcfnlib.changePlayerName(userName, file.getPath());
				// -----Copy the test Data to The CasinoAs1 Server-----//
				gcfnlib.copyFolder(file, destFile);
				copiedFiles.add(fileName);
				isTestDataCopied=true;

			}	catch (IOException e) {
				isTestDataCopied=false;
				log.error("Exception while copying test data file ",e);
			}
		}
		// To copy TestData  on axiom lobby
		else if(TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Axiom")){
			try{
				apiobj.createUserInAxiom(userName);
				gcfnlib.changePlayerName(userName, file.getPath());
				apiobj.copyTestData(file.getPath(),fileName);
				copiedFiles.add(fileName);
				isTestDataCopied=true;
			}catch(Exception e)
			{
				isTestDataCopied=false;
				log.error(" Exception while copying test data file",e);
			}
		} 

		return isTestDataCopied;

	}

	/**
	 * Description : Overload of copyFilesToTestServer() with maxThreshold
	 * Copy the test data file to Server with  maxThreshold no of times.
	 * with username as username+count up to threshold value
	 * */
	public boolean copyFilesToTestServer(int mid,int cid,File file, String userName,List<String> copiedFiles,int maxThreshold) 
	{
		boolean isTestDataCopied=false;
		// creating the object of Global function Library
		Util gcfnlib = new Util();
		RestAPILibrary apiobj= new RestAPILibrary();
		DataBaseFunctions dbobject = null;
		
		if( TestPropReader.getInstance().getProperty("EnvironmentName")==null ||
			TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
		{
			log.info("ServerName:"+ TestPropReader.getInstance().getProperty("ServerName"));
			log.info("dataBaseName:"+ TestPropReader.getInstance().getProperty("dataBaseName"));
			log.info("serverIp:"+ TestPropReader.getInstance().getProperty("serverIp"));
			log.info("Port:"+ TestPropReader.getInstance().getProperty("port"));
			log.info("serverID:"+ TestPropReader.getInstance().getProperty("serverID"));
			// Creating database object name as "dbobject"
			dbobject = new DataBaseFunctions(TestPropReader.getInstance().getProperty("ServerName"),
					TestPropReader.getInstance().getProperty("dataBaseName"),
					TestPropReader.getInstance().getProperty("serverIp"), TestPropReader.getInstance().getProperty("port"),
					TestPropReader.getInstance().getProperty("serverID"));
		}
		for(int count =1 ;count <=maxThreshold;count++)
		{
			String newUserName=userName+count;
			String fileName=TestPropReader.getInstance().getProperty("TestDataFileName").trim()+"_"+mid+"_"+cid+"_"
					+ newUserName + ".testdata";


			if( TestPropReader.getInstance().getProperty("EnvironmentName")==null ||
				TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
			{
				dbobject.createUser(newUserName, "0", 0);
				String destFile = "smb://" + TestPropReader.getInstance().getProperty("Casinoas1IP").trim()
						+ "/c$/MGS_TestData/" + fileName;

				// --------Update the Xml File of test Data with currency user name----------//
				try {
					gcfnlib.changePlayerName(newUserName, file.getPath());
					// -----Copy the test Data to The CasinoAs1 Server-----//
					gcfnlib.copyFolder(file, destFile);
					copiedFiles.add(fileName);
					isTestDataCopied=true;
				} catch (IOException e) {
					isTestDataCopied=false;
					log.error("Exception while copying test data file",e);
				}
			}
			// To copy TestData  on axiom lobby
			else if(TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Axiom")){
				try{
					apiobj.createUserInAxiom(newUserName);
					gcfnlib.changePlayerName(newUserName, file.getPath());
					apiobj.copyTestData( file.getPath(),fileName);
					copiedFiles.add(fileName);
					isTestDataCopied=true;
				}catch(Exception e)
				{
					isTestDataCopied=false;
					log.error("Exception while copying test data file",e);
				}
			} 
		}
		return isTestDataCopied;

	}

	/**
	 * This methos copy the test data files for currency players
	 * @param mid
	 * @param cid
	 * @param device
	 * @param gameName
	 * @param copiedFiles
	 * @return
	 */
	public boolean copyCurrencyTestData(int mid,int cid,String device, String gameName,List<String> copiedFiles) {

		boolean isTestDataCopied = false;

		try {
			String destFile;
			int threshold=Integer.parseInt(TestPropReader.getInstance().getProperty("MaxThreshold"));
			log.info("copying currency test data for cid and mid:");
			String xmlFilePath = null;

			// condition to check the for which client need to paste the test
			// data
			if ("Mobile".equalsIgnoreCase(device)) {
				isTestDataCopied=copyMobileCurrencyTestDataWithThreshold(mid, cid, device, gameName, copiedFiles, threshold);

			} 
			else {
				xmlFilePath = TestPropReader.getInstance().getProperty("CurrencyTestDataPath");

				File sourceFile = new File(xmlFilePath);

				// creating the object of Global function Library
				Util gcfnlib = new Util();

				// Creating database object name as "dbobject"
				DataBaseFunctions dbobject = new DataBaseFunctions(TestPropReader.getInstance().getProperty("ServerName"),
						TestPropReader.getInstance().getProperty("dataBaseName"),
						TestPropReader.getInstance().getProperty("serverIp"),
						TestPropReader.getInstance().getProperty("port"),
						TestPropReader.getInstance().getProperty("serverID"));
				// Fetching all currencies from data base
				ArrayList<Currency> currencyList = dbobject.getCurrencyData();

				int currencysize = currencyList.size();
				if (currencysize == 0) {
					log.error("Error While reading the currencies from datbase");
				}
				// Read ISO name from database as use as User name

				for (Currency currency : currencyList) {

					String currencyID = currency.getCurrencyID();
					String capGameName = gameName.replaceAll("[a-z0-9]", "").trim();
					String userName = "Zen" + capGameName + "_" + currency.getIsoCode().trim();
					log.debug("The New username is ==" + userName);

					// creating user in to the database with new username
					dbobject.createUser(userName, currencyID, 0);

					// Read the test data file
					String fileName= TestPropReader.getInstance().getProperty("TestDataFileName").trim()+"_"+mid+"_"+cid+"_"
							+ userName + ".testdata";
					destFile = "smb://" + TestPropReader.getInstance().getProperty("Casinoas1IP").trim()
							+ "/c$/MGS_TestData/" + fileName;

					// --------Update the Xml File of test Data with currency user
					// name----------//
					gcfnlib.changePlayerName(userName, xmlFilePath);

					// -----Copy the test Data to The CasinoAs1 Server-----//
					gcfnlib.copyFolder(sourceFile, destFile);
					copiedFiles.add(fileName);
					log.info("created test data for : " + userName);
				}

				isTestDataCopied = true;
				log.debug("Test data creation is compeleted .... ");
				log.info("Test data creation is completed ....");
			}//else
		} catch (IOException e) {
			isTestDataCopied = false;
			log.error(e.getMessage(),e);
		}

		return isTestDataCopied;
	}

	/**
	 * overload of function copyMobileCurrencyTestData with max threshold
	 * @param mid
	 * @param cid
	 * @param device
	 * @param gameName
	 * @param copiedFiles
	 * @param threshold
	 * @return
	 */
	public boolean copyMobileCurrencyTestDataWithThreshold(int mid,int cid,String device, String gameName,List<String> copiedFiles,int threshold) {

		boolean isTestDataCopied = false;

		try {
			String destFile;
			log.info("copying currency test data for cid and mid:"+mid+" "+cid);
			String xmlFilePath = null;

			// condition to check the for which client need to paste the test
			// data
			if ("Mobile".equalsIgnoreCase(device)) {
				xmlFilePath = TestPropReader.getInstance().getProperty("MobileCurrencyTestDataPath");

			} 
			File sourceFile = new File(xmlFilePath);

			// creating the object of Global function Library
			Util gcfnlib = new Util();

			// Creating database object name as "dbobject"
			DataBaseFunctions dbobject = new DataBaseFunctions(TestPropReader.getInstance().getProperty("ServerName"),
					TestPropReader.getInstance().getProperty("dataBaseName"),
					TestPropReader.getInstance().getProperty("serverIp"),
					TestPropReader.getInstance().getProperty("port"),
					TestPropReader.getInstance().getProperty("serverID"));
			// Fetching all currencies from data base
			ArrayList<Currency> currencyList = dbobject.getCurrencyData();

			int currencysize = currencyList.size();
			if (currencysize == 0) {
				log.error("Error While reading the currencies from datbase");
			}
			// Read ISO name from database as use as User name
			for(int i=1;i<=threshold;i++)
			{
				for (Currency currency : currencyList) {

					String currencyID = currency.getCurrencyID();
					String capGameName = gameName.replaceAll("[a-z0-9]", "").trim();
					String userName = "Zen" + capGameName +i+ "_" + currency.getIsoCode().trim();
					log.debug("The New username is ==" + userName);

					// creating user in to the database with new username
					dbobject.createUser(userName, currencyID, 0);

					// Read the test data file
					String fileName= TestPropReader.getInstance().getProperty("TestDataFileName").trim()+"_"+mid+"_"+cid+"_"
							+ userName + ".testdata";
					destFile = "smb://" + TestPropReader.getInstance().getProperty("Casinoas1IP").trim()
							+ "/c$/MGS_TestData/" + fileName;

					// --------Update the Xml File of test Data with currency user
					// name----------//
					gcfnlib.changePlayerName(userName, xmlFilePath);

					// -----Copy the test Data to The CasinoAs1 Server-----//
					gcfnlib.copyFolder(sourceFile, destFile);
					copiedFiles.add(fileName);
					log.debug("created test data for : " + userName);
				}
			}
			isTestDataCopied = true;
			log.debug("Test data creation is compeleted .... ");

		} catch (IOException e) {
			isTestDataCopied = false;
			log.error(e.getStackTrace());
		}

		return isTestDataCopied;
	}


/**
 * Create user in database
 * @param userName
 * @return
 */

	boolean createUser(String userName)
	{
		try{
			DataBaseFunctions dbobject = new DataBaseFunctions(TestPropReader.getInstance().getProperty("ServerName"),
					TestPropReader.getInstance().getProperty("dataBaseName"),
					TestPropReader.getInstance().getProperty("serverIp"), TestPropReader.getInstance().getProperty("port"),
					TestPropReader.getInstance().getProperty("serverID"));
			dbobject.createUser(userName, "0", 0);
		}catch(Exception e)
		{
			log.error("Fail to create user",e);
			return false;
		}
		return true;
	}
	/**
	 * Delete the test data file from test server
	 * @param mid
	 * @param cid
	 * @param cidDesktop
	 */
	public void deleteTestDataFiles(int mid, int cid,int cidDesktop){
		Util glbCfnLib = new Util();

		String folderPath= "smb://" + TestPropReader.getInstance().getProperty("Casinoas1IP").trim()+ "/c$/MGS_TestData/";

		String wildCard= TestPropReader.getInstance().getProperty("TestDataFileName").trim()+"_"+mid+"_"+cid+"_";

		glbCfnLib.deleteFiles(folderPath,wildCard);
	}
	/**
	 *  Delete the test data files from server
	 * @param mid
	 * @param cid
	 * @param cidDesktop
	 * @param copiedFiles
	 */
	public void deleteTestDataFiles(int mid, int cid,int cidDesktop,List<String> copiedFiles){
		Util glbCfnLib = new Util();

		String folderPath= "smb://" + TestPropReader.getInstance().getProperty("Casinoas1IP").trim()+ "/c$/MGS_TestData/";
		glbCfnLib.deleteFiles(folderPath,copiedFiles);
	}
	
	
}
