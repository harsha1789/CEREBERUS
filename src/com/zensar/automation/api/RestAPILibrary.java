package com.zensar.automation.api;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.swing.LayoutFocusTraversalPolicy;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
//import com.sun.net.ssl.internal.ssl.X509ExtendedTrustManager;
import com.zensar.automation.framework.model.DeviceInfo;
import com.zensar.automation.framework.model.DeviceList;
import com.zensar.automation.library.DataBaseFunctions;
import com.zensar.automation.library.TestPropReader;
import com.zensar.automation.model.AllBlueMesaEnvironments;
import com.zensar.automation.model.EnvList;
import com.zensar.automation.model.FreeGameOfferResponse;
import com.zensar.automation.model.GetAllBlueMesaEnvironmentsResponse;
import com.zensar.automation.model.InstalledGameMetaData;
import com.zensar.automation.model.PatchDataResponse;
import com.zensar.automation.model.Preset;
import com.zensar.automation.model.PresetResponse;
import com.zensar.automation.model.UserMetaData;
import com.zensar.automation.model.VirtualMachines;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
/**
 * This class provide the implimentation for  Rest API used in framwork,
 * such as createURL,getEnvironmentsDetails,getDeviceList etc.
 * @author sg56207
 *
 */
public class RestAPILibrary {

	Logger log = Logger.getLogger(RestAPILibrary.class.getName());
	
	/**
	 * Description: This method read presets for given MID and CID and return first signed off version patch id
	 * @input: Int MID, Int CID
	 * @return : Int 
	 * */
	public PresetResponse getPresets(int mid, int cid)
	{
		ObjectMapper mapper=new ObjectMapper();
		PresetResponse presetResponse=null;
		try{

			OkHttpClient okHttpClientclient = getHTTPClient();
			Request request = new Request.Builder()
					.url(TestPropReader.getInstance().getProperty("ETCHostURL")+"/GetPresets?moduleID="+mid+"&clientID="+cid+"&")
					.method("GET", null)
					.addHeader("Accept-Language", "en-US,en;q=0.9")
					.addHeader("Content-Type", "application/json; charset=utf-8")
					.addHeader("Host", TestPropReader.getInstance().getProperty("ETCHost"))
					.addHeader("Origin", TestPropReader.getInstance().getProperty("ETCHostURL"))
					.addHeader("Referer", TestPropReader.getInstance().getProperty("ETCReferer"))
					.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36")
					.build();
			Response response = okHttpClientclient.newCall(request).execute();

			if(response.isSuccessful())
			{
				log.info("getPresets()  success and response= "+response ); 

				String strPresetResponse = response.body().string();
				log.info("getPresets()  success and preset response= "+strPresetResponse +" as expected"); 

				//Add condition to check whether it is latest or not 
				Preset[] presets=mapper.readValue(strPresetResponse,Preset[].class);
				presetResponse=new PresetResponse();
				presetResponse.setPresets(presets);

			}



		}catch(Exception e)
		{
			log.error("Exception occur in getPresets(int MID, int CID) ",e);
			log.error(e.getMessage());
		}


		return presetResponse;

	}

	/*Description: This method read the patch data for given patch id and check the status of the request
	 * @Input:Int enviromentID, Int patchID, String username
	 * @return:String   response message
	 * */

	public PatchDataResponse getpatchdata(int environmentID ,int patchid,String username)
	{

		Response response=null;
		PatchDataResponse patchDataResponse=null;
		ObjectMapper mapper=new ObjectMapper();
		try{


			OkHttpClient okHttpClientclient = getHTTPClient();
			MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
			RequestBody body = RequestBody.create(mediaType, "");
			Request request = new Request.Builder()
					.url(TestPropReader.getInstance().getProperty("ETCHostURL")+"/readPatchData?environmentID="+environmentID+"&patchID="+patchid+"&Username="+username)
					.method("POST", body)
					.addHeader("Accept-Language", "en-US,en;q=0.9")
					.addHeader("Content-Type", "application/json; charset=utf-8")
					.addHeader("Host", TestPropReader.getInstance().getProperty("ETCHost"))
					.addHeader("Origin", TestPropReader.getInstance().getProperty("ETCHostURL"))
					.addHeader("Referer", TestPropReader.getInstance().getProperty("ETCReferer"))
					.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36")
					.addHeader("Content-Length", "0")
					.build();
			response = okHttpClientclient.newCall(request).execute();	

			if(response.isSuccessful())
			{
				String responsenew = response.body().string();
				patchDataResponse =new PatchDataResponse();
				patchDataResponse=mapper.readValue(responsenew,PatchDataResponse.class);
			}
			else
			{	
				log.info("Request fail with response :"+ response.message());

			}


		}catch(Exception e)
		{
			log.error("Exception occur in getpatchdata(int MID, int patchid,int CID) ",e);
			log.error(e.getMessage());
		}


		return patchDataResponse;

	}
	/*Description: install preset on given environment
	 * @return : response message
	 * */
	public String  installPreset(int environmentid,int mid,int cid,String username,String database,String architecture)
	{

		String installPresetResponse=null;
		try{


			OkHttpClient okHttpClientclient = getHTTPClient();

			MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
			RequestBody body = RequestBody.create(mediaType, "");
			Request request = new Request.Builder()  // add paramete arcitecture
					.url(TestPropReader.getInstance().getProperty("ETCHostURL")+"/installPreset?environmentID="+environmentid+"&architecture=x86&moduleID="+mid+"&clientID="+cid+"&userName="+username+"&siteTeamName=Top%20Games&database="+database)
					.method("POST", body)
					.addHeader("Accept-Language", "en-US,en;q=0.9")
					.addHeader("Content-Type", "application/json; charset=utf-8")
					.addHeader("Host", TestPropReader.getInstance().getProperty("ETCHost"))
					.addHeader("Origin", TestPropReader.getInstance().getProperty("ETCHostURL"))
					.addHeader("Referer", TestPropReader.getInstance().getProperty("ETCReferer"))
					.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36")
					.addHeader("Content-Length", "0")
					.build();
			Response response = okHttpClientclient.newCall(request).execute();	

			installPresetResponse = response.body().string();

			log.debug("Response of installPreset: "+installPresetResponse);	


		}catch(Exception e)
		{
			log.error("Exception occur in installPreset() ",e);
			log.error(e.getMessage());
		} 
		return installPresetResponse;

	}


	public String createURL( int environmentid,String language,String host,String gamename,String playername,String serverid ,String lobbyname, String gameVersion)
	{
		String createURLResponse=null;
		String strUrl=null;
		String password=TestPropReader.getInstance().getProperty("UserPassword");
		try{
			OkHttpClient okHttpClientclient = getHTTPClient();
			MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
			RequestBody body = RequestBody.create(mediaType, "");
			Request request = new Request.Builder()
					.url(TestPropReader.getInstance().getProperty("ETCHostURL")+"/gameURL?environmentID="+environmentid+"&mobilewebgames=MobileWebGames&language="+language+"&host="+host+"&gameName="+gamename+"&launchtype=1&userName="+playername+"&password="+password+"&serverID="+serverid+"&lobbyName="+lobbyname+"&canisterEnv=Yes&hostLocation=Localhost&siteTeamName=Top%20Games&DiscoOption=true&framework=V&Version=0&LoginMethod=false&Brand=IslandParadise"+"&gameVersion="+gameVersion)
					.method("POST", body)
					.addHeader("Accept-Language", "en-US,en;q=0.9")
					.addHeader("Content-Type", "application/json; charset=utf-8")
					.addHeader("Host", TestPropReader.getInstance().getProperty("ETCHost"))
					.addHeader("Origin", TestPropReader.getInstance().getProperty("ETCHostURL"))
					.addHeader("Referer", TestPropReader.getInstance().getProperty("ETCReferer"))
					.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36")
					.addHeader("Content-Length", "0")
					.build();
			Response response = okHttpClientclient.newCall(request).execute();
			if(response.code()==200)
			{
				log.info("createUrl request success");
				createURLResponse =response.body().string();
				strUrl=createURLResponse.substring(1,createURLResponse.length()-1);

			}
			else
			{
				log.error("createURl request fail with message:");
				log.error(response.message());
			}
		}catch(Exception e)
		{
			log.error("Exception occur in createURL() ",e);
			log.error(e.getMessage());
		}


		return strUrl;
	}


	public String createTitanURL( int environmentid,String language,String host,String gameName,String playerName,String serverId ,String lobbyName, String gameVersion)
	{
		String envName=TestPropReader.getInstance().getProperty("EnvironmentName");
		log.debug("createTitanURL request received");
		String createURLResponse=null;
		String strUrl=null;
		try{
			String titanVersion=TestPropReader.getInstance().getProperty("TitanVersion");
			if(titanVersion==null||"".equalsIgnoreCase(titanVersion))
			{
				RestAPILibrary apiobj = new RestAPILibrary();
				titanVersion=apiobj.getLatestTitanVersionBlueMesa(environmentid);
			}
			OkHttpClient client = getHTTPClient();
			MediaType mediaType = MediaType.parse("text/plain");
			RequestBody body = RequestBody.create(mediaType, "");
			Request request = new Request.Builder().url(TestPropReader.getInstance().getProperty("ETCHostURL")+"/gameURL?environmentID="
					+ environmentid + "&mobilewebgames=" + titanVersion + "&language=" + language + "&host=" + host
					+ "&gameName=" + gameName + "&launchtype=1&userName=" + playerName + "&password=test&serverID="
					+ serverId + "&lobbyName=" + lobbyName
					+ "&canisterEnv=Yes&hostLocation=Localhost&siteTeamName=ThirdParty&framework=Titan&Version=0&LoginMethod=false&Brand=IslandParadise&gameVersion="
					+ gameVersion).method("POST", body).build();

			log.info("createTitanURL request body"+request.body().toString());
			Response response = client.newCall(request).execute();

			if(response.code()==200)
			{
				log.info("createTitanURL request success");
				createURLResponse =response.body().string();
				strUrl=createURLResponse.substring(1,createURLResponse.length()-1)+"&site="+envName;

			}
			else
			{
				log.error("createTitanURL request fail with message:");
				log.error(response.message());
			}
		}catch(Exception e)
		{
			log.error("Exception occur in createTitanURL() ",e);
			log.error(e.getMessage());
		}


		return strUrl;
	}


	public boolean isGameLaunch(int mid,int cid,String playername) 
	{

		String sessionid=null;
		String usertoken=null;
		boolean isGameLaunch=false;
		try{
			//creating random correlation id
			UUID xCorrelationId=UUID.randomUUID();
			String randomXCorrelationId=xCorrelationId.toString();
			log.info("Corrlation id="+randomXCorrelationId);
			String strServerID=TestPropReader.getInstance().getProperty("serverID");
			String host = null;
			String env=TestPropReader.getInstance().getProperty("EnvironmentName").toLowerCase();
			String executionEnv=TestPropReader.getInstance().getProperty("ExecutionEnv");
			String password=null;
			switch(env)
			{
			case "bluemesa" :host="http://"+TestPropReader.getInstance().getProperty("RabbitMQ");
			password=TestPropReader.getInstance().getProperty("UserPassword");
			break;
			case "axiom"	:host=TestPropReader.getInstance().getProperty("AxiomHost")+"-"+executionEnv+".installprogram.eu";
			password=TestPropReader.getInstance().getProperty("AxiomUserPassword");
			break;
			}
			OkHttpClient client1 = getHTTPClient();
			MediaType mediaType1 = MediaType.parse("application/json");
			RequestBody body1 = RequestBody.create(mediaType1, "{\n  \"environment\": {    \"clientTypeId\": 5,    \"languageCode\": \"en\"\n  },\n  \"userName\": \""+playername+"\",\n  \"password\": \""+password+"\",\n  \"sessionProductId\":"+strServerID+"\n\n}\n\n");
			Request request1 = new Request.Builder()
					.url(host+"/casino/user/public/v1/accounts/login/real")
					.method("POST", body1)
					.addHeader("X-CorrelationId", randomXCorrelationId)
					.addHeader("X-Forwarded-For", UUID.randomUUID().toString())
					.addHeader("X-Clienttypeid", "40")
					.addHeader("Content-Type", "application/json")
					.build();
			Response response1 = client1.newCall(request1).execute();
			log.info("Response of token request for client id:"+cid+" = "+response1);

			if(response1.code()==200)
			{
				JsonParser parse = new JsonParser();
				JsonObject jobj = (JsonObject) parse.parse(response1.body().string());

				sessionid=jobj.getAsJsonObject("account").getAsJsonObject("core").get("userIdentifier").getAsString();
				log.info("Session id="+sessionid);
				usertoken=jobj.getAsJsonObject("tokens").get("userToken").getAsString();
				log.info("Usertoken"+usertoken);


				OkHttpClient okHttpClientclient =  getHTTPClient();
				MediaType mediaType = MediaType.parse("application/json,application/json");
				RequestBody body = RequestBody.create(mediaType, TestPropReader.getInstance().getProperty("RefreshPacketRequestBody"));

				Request request = new Request.Builder()
						.url(host+"/casino/play/public/v1/games/module/"+mid+"/client/"+cid+"/play")
						.method("POST", body)
						.addHeader("Authorization", "Bearer "+usertoken)
						.addHeader("X-correlationid", UUID.randomUUID().toString())
						.addHeader("X-Route-ProductId", strServerID)
						.addHeader("X-Route-ModuleId", Integer.toString(mid))
						.addHeader("X-Route-Clientid", Integer.toString(cid))
						.addHeader("X-ClientTypeId", "40")
						.addHeader("Content-Type", "application/json")
						.build();
				Response response = okHttpClientclient.newCall(request).execute();


				if(response.code()==200)
				{

					String strResponse=response.body().string();
					boolean res=strResponse.contains("payload");
					if(res)
					{
						log.info("Refresh packet response for cilent id "+cid+strResponse);
						isGameLaunch=true;
					}
					else
					{
						log.info("Refresh packet response code 200 but not containg required response"+strResponse);
					}

				}
				else
				{
					log.info("Refresh packet fail:"+response.code());
					log.info("Request Body :"+body.toString());
					log.info("Response body :"+response.body().string());

				}
			}else
			{
				log.info("login packet  fail:"+response1.code());

				log.info(response1.message());
			}
		}catch(Exception e)
		{
			log.error("Exception occur in isGameLaunch() during health check for client : "+cid,e);
			log.error(e.getMessage());
		}
		return isGameLaunch;
	}
	///////////////////BlueMesaEnvironment Meta data API call//////////////////////

	/*Description: This method map the environment name with environment id
	 * @Input:String EnvName
	 * @return:Int enviromentID
	 * */
	public int getEnvironmentId(String envName)
	{

		Response response=null;
		GetAllBlueMesaEnvironmentsResponse envMetaData=null;
		ObjectMapper mapper=new ObjectMapper();
		int envID=-1;
		try{

			OkHttpClient okHttpClientclient = getHTTPClient();
			Request request = new Request.Builder()
					.url(TestPropReader.getInstance().getProperty("ETCGetAllBlueMesaEnvironmentUrl"))
					.method("GET", null)
					.build();
			response = okHttpClientclient.newCall(request).execute();

			if(response.isSuccessful())
			{
				String responsenew = response.body().string();
				envMetaData=mapper.readValue(responsenew,GetAllBlueMesaEnvironmentsResponse.class);

				//validation

				log.debug("response="+envMetaData.getSuccess());
				List<AllBlueMesaEnvironments> allEnvList=envMetaData.getAllBlueMesaEnvironments();

				for (AllBlueMesaEnvironments allBlueMesaEnvironments : allEnvList) {

					if(allBlueMesaEnvironments.getEnvironmentName().toLowerCase().startsWith(envName.toLowerCase()))
					{
						envID= Integer.valueOf(allBlueMesaEnvironments.getId());
						break;
					}
				}

				log.info("Request success with response :"+ responsenew);
			}
			else
			{	
				log.info("Request fail with response :"+ response.message());

			}
		}catch(Exception e)
		{
			log.error("Exception occur in mapEnvNameToEnvId(String envName) ",e);
			log.error(e.getMessage());
		}
		return envID;
	}


	/*Description: This method read the all virtual machine meta data of inputed environment id
	 * @Input:Int enviromentID
	 * @return:virtual machines details list
	 * */
	public EnvList getBlueMesaEnvMetaData(int environmentID)
	{

		Response response=null;
		EnvList envlist=null;
		ObjectMapper mapper=new ObjectMapper();
		try{

			OkHttpClient okHttpClientclient = getHTTPClient();
			Request request = new Request.Builder()
					.url(TestPropReader.getInstance().getProperty("ETCBlueMesaGetUrl")+environmentID)
					.method("GET", null)
					.build();
			response = okHttpClientclient.newCall(request).execute();

			if(response.isSuccessful())
			{
				String responsenew = response.body().string();
				envlist =new EnvList();
				envlist=mapper.readValue(responsenew,EnvList.class);

				///checking

				System.out.println("response="+envlist.getSuccess());
				System.out.println("Environment ID ="+envlist.getDataObject().environmentId);
				VirtualMachines vm = envlist.getDataObject().getVirtualMachines().get(1);
				System.out.println("Virtual machines extenal IP address="+vm.getExternalIpAddress());

				log.info("Request success with response :"+ responsenew);
			}
			else
			{	
				log.info("Request fail with response :"+ response.message());

			}
		}catch(Exception e)
		{
			log.error("Exception occur in getBlueMesaEnvMetaData(int environmentID) ",e);
			log.error(e.getMessage());
		}
		return envlist;
	}

	/**
	 * Description: This method read the all device list of given STF_SERVICE_URL
	 * @return:device list of provided STF service
	 * */

	public DeviceList getAllDevices()
	{
		DeviceList deviceList=null;
		OkHttpClient okHttpClientclient = getHTTPClient();
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		if (okHttpClientclient != null) {
			Request request = new Request.Builder().url(TestPropReader.getInstance().getProperty("STF_SERVICE_URL")+"devices")
					.addHeader("Authorization", "Bearer " + TestPropReader.getInstance().getProperty("ACCESS_TOKEN"))
					.build();
			String responseJson = null;
			try {
				Response response = okHttpClientclient.newCall(request).execute();

				if (response.code()==200) {
					responseJson = response.body().string();
					log.info("Reading All devices from zenreplica to Device List..");
					deviceList= mapper.readValue(responseJson, DeviceList.class);
					log.info("Device List reading completed..");
				}
				else
				{
					log.error("request not success "+response);
					log.error(response.code());
				}
			} catch (JsonParseException e) {

				log.error("JsonParseException:"+e.getCause());
			} catch (JsonMappingException e) {

				log.error("JsonMappingException:"+e.getCause());
			} catch (IOException e) {

				log.error("IOException:"+e.getCause());
			}catch (Exception e) {
				log.error("Exception occur in getAllDevices(): ", e);
			}

		}
		return deviceList;
	}

	/**
	 * Description: This method read the all device list which are in ready state
	 * @return:device list of ready devices
	 * */

	public List<DeviceInfo>  getAllReadyDevices()
	{
		DeviceList deviceList=null;
		List<DeviceInfo> readyDeviceList= new ArrayList<>();
		deviceList=getAllDevices();
		List<DeviceInfo> deviceInfoList=  deviceList.getDevices();
		for(DeviceInfo deviceInfo : deviceInfoList)
		{
			if(deviceInfo.ready )
			{
				readyDeviceList.add(deviceInfo);
			}
		}
		return readyDeviceList;


	}

	/**
	 * Description: This method create the user in axiom lobby
	 * @input :String userName
	 * @boolean : return success if user created successfully ow return false
	 * @return boolean
	 * */

	public boolean createUserInAxiom( String userName,String... currencyIsoCode)
	{
		boolean isUsrCreated=false;
		String responseStatus=null;
		Response response=null;
		String strISOCode;
		String password=TestPropReader.getInstance().getProperty("AxiomUserPassword");
		try{
			String envName=TestPropReader.getInstance().getProperty("ExecutionEnv");

			if(currencyIsoCode.length==0)
			{
				strISOCode="USD";
			}
			else
			{
				strISOCode=currencyIsoCode[0];
			}
			log.debug("Currency ISO Code:"+strISOCode);
			OkHttpClient okHttpClientclient = getHTTPClient();
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, "{\"Username\":\""+userName+"\",\"Password\":\""+password+"\",\"MarketTypeId\":0,\"ServerId\":5007,\"UserTypeId\":0,\"currencyIsoCode\":\""+strISOCode+"\",\"Country\":\"South Africa\",\"NumberOfAccounts\":1}");
			
			if (okHttpClientclient != null) {
				Request request = new Request.Builder()
						.url(TestPropReader.getInstance().getProperty("AxiomEnvRefererUrl")+"-"+envName+".installprogram.eu"+"/UserAccounts")
						.method("POST", body)
						.addHeader("Content-Type", "application/json")
						.build();
				log.info("createUser request body"+request.body().toString());
				response = okHttpClientclient.newCall(request).execute();
				
				//System.out.println(response.isSuccessful());
				//System.out.println( response.code());
				log.info("Response status "+response.isSuccessful());
				log.info("Response code "+ response.code());
				
				if(response.isSuccessful() && response.code()==200)
				{
					JsonParser parse = new JsonParser();
					JsonObject jobj = (JsonObject) parse.parse(response.body().string());

					responseStatus=jobj.get("dataObject").toString();
					if(responseStatus.equalsIgnoreCase("true"))
						isUsrCreated=true;
					log.info("User created on axiom environment successfully");
				}
				else if(response.code()==400){
					JsonParser parse = new JsonParser();
					JsonObject jobj = (JsonObject) parse.parse(response.body().string());

					responseStatus=jobj.get("customMessage").toString();
					if(responseStatus.contains("UserAccount already taken"))
						log.info("User on axiom environment already created");
				}
				else
				{
					log.error("createUser on axiom environment request fail with message:");
					log.error(response.message());
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			log.error("Exception occur in createUser() on axiom ",e);
			log.error(e.getMessage());
		}


		return isUsrCreated;
	}

	/**
	 * 	
	 * @param userName
	 * @return
	 */
	public String getUserIdFromAxiom( String userName)
	{
		String userId=null;
		String responseStatus=null;
		Response response=null;
		UserMetaData userInfo=new UserMetaData();
		try{
			String envName=TestPropReader.getInstance().getProperty("ExecutionEnv");
			OkHttpClient okHttpClientclient = getHTTPClient();
			if (okHttpClientclient != null) {
				Request request = new Request.Builder()
						.url(TestPropReader.getInstance().getProperty("AxiomEnvRefererUrl")+"-"+envName+".installprogram.eu"+"/UserAccounts/UserAccount?loginName="+userName)
						.method("GET", null)
						.build();
				response = okHttpClientclient.newCall(request).execute();

				if(response.isSuccessful() && response.code()==200)
				{
					JsonParser parse = new JsonParser();
					JsonObject jobj = (JsonObject) parse.parse(response.body().string());

					userId=jobj.get("userId").getAsString();

					log.info("getUserIDFromAxiom request pass:UserID="+userId);
				}

				else
				{
					log.error("getUserIDFromAxiom request  fail with message:");
					log.error(response.message());
				}
			}
		}catch(Exception e)
		{
			log.error("Exception occur in getUserIDFromAxiom()  ",e);
			log.error(e.getMessage());
		}

		return userId;
	}






	/**
	 * Description: This method list the installed games in axiom lobby
	 * @return InstalledGameMetaData
	 * */

	public InstalledGameMetaData getInstalledGameMetaData()
	{
		String displayName=null;
		Response response=null;
		ObjectMapper mapper = new ObjectMapper();
		OkHttpClient okHttpClient = getHTTPClient();
		InstalledGameMetaData avilableGamesList = new InstalledGameMetaData();
		try{

			String envName=TestPropReader.getInstance().getProperty("ExecutionEnv");
			if (okHttpClient != null) {
				Request request = new Request.Builder()
						.url(TestPropReader.getInstance().getProperty("AxiomEnvRefererUrl")+"-"+envName+".installprogram.eu"+"/Games/InstalledGameRecords")
						.method("GET", null)
						.build();
				response = okHttpClient.newCall(request).execute();

				if(response.isSuccessful() && response.code()==200)
				{
					String responseJson = response.body().string();


					avilableGamesList=mapper.readValue(responseJson,InstalledGameMetaData.class);

					log.info("getInstalledGameMetaData() got success code 200");
				}
				else
				{
					log.error("getInstalledGameMetaData() request fail with message:");
					log.error(response.message());
				}
			}
		}catch(Exception e)
		{
			log.error("Exception occur in createUser() on axiom ",e);
			log.error(e.getMessage());
		}


		return avilableGamesList;
	}


	/**
	 * This method will create the game url for axiom lobby
	 * @param userName
	 * @param gameDisplayName
	 * @param gameVersion
	 * @return
	 */
	public String axiomCreateURL(String userName,String gameVersion,int moduleId, int clientId, String host)
	{
		String strUrl=null;
		String url = null;
		String  frameWorkVersion=null;
		String password=TestPropReader.getInstance().getProperty("AxiomUserPassword");
		RestAPILibrary apiobj = new RestAPILibrary();
		try{
			String frameWorkType =TestPropReader.getInstance().getProperty("FrameWorkType");
			if(frameWorkType.equalsIgnoreCase("Titan"))
			{
				frameWorkType="titan";
				frameWorkVersion=TestPropReader.getInstance().getProperty("TitanVersion");
				if(frameWorkVersion==null||"".equalsIgnoreCase(frameWorkVersion))
				{
					frameWorkVersion=apiobj.getLatestTitanVersionAxiom();
				}
			}
			else
			{
				frameWorkType="v";
				frameWorkVersion="";
			}
			String envName=TestPropReader.getInstance().getProperty("ExecutionEnv");
			OkHttpClient okHttpClient = getHTTPClient();
			Request request = new Request.Builder()
					.url(TestPropReader.getInstance().getProperty("AxiomEnvRefererUrl")+"-"+envName+".installprogram.eu"+"/launch/GameUrl?"
							+ "gameProvider=null"
							+ "&username="+userName
							+ "&password="+password
							+ "&lobbyName=DotCom"
							+ "&moduleId="+moduleId
							+ "&clientId="+clientId
							+ "&gameVersion="+gameVersion
							+ "&languageCode=en"
							+ "&host="+host
							//+ "&disco=true"
							+ "&iframe=false"
							+ "&framework="+frameWorkType
							+ "&frameworkVersion="+frameWorkVersion)
					/*+"&"
							+ "_=1610022507604")*/
					.method("GET", null)
					.build();
			Response response = okHttpClient.newCall(request).execute();
			if(response.code()==200)
			{
				JsonParser parse = new JsonParser();
				JsonObject jobj = (JsonObject) parse.parse(response.body().string());

				strUrl=jobj.get("dataObject").toString();
				url=strUrl.substring(1,strUrl.length()-1);
				//check with QAleaads why we need this addition
				url=url+"&site=bluemesa";
			}
			else
			{
				log.error("createURl request fail with message:");
				log.error(response.message());
			}
		}catch(Exception e)
		{
			log.error("Exception occur in createURL() ",e);
			log.error(e.getMessage());
		}


		return url;
	}


	/**
	 * Description: Method to copy test data on axiom environment
	 * @input testdatafileName
	 * @return boolean
	 * */

	public boolean copyTestData(String testdataFileName,String tempTestdataFile)
	{
		boolean isTestDataCopied=false;
		String responseStatus=null;

		Response response=null;

		try{
			String envName=TestPropReader.getInstance().getProperty("ExecutionEnv");
			OkHttpClient okHttpClientclient = getHTTPClient();
			RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
					.addFormDataPart("testDataFormFile",tempTestdataFile,
							RequestBody.create(MediaType.parse("application/octet-stream"),
									new File(testdataFileName)))
					.build();
			Request request = new Request.Builder()
					.url(TestPropReader.getInstance().getProperty("AxiomEnvRefererUrl")+"-"+envName+".installprogram.eu"+"/Upload/TestDataFile")
					.method("POST", body)
					.build();
			response = okHttpClientclient.newCall(request).execute();

			if(response.isSuccessful() && response.code()==200)
			{
				JsonParser parse = new JsonParser();
				JsonObject jobj = (JsonObject) parse.parse(response.body().string());

				responseStatus=jobj.get("dataObject").toString();
				if(responseStatus.equalsIgnoreCase("true"))
					isTestDataCopied=true;
				log.info("Testdat peaste on axiom environment successfully");
			}
			else
			{
				log.error("Copy testdata on axiom environment request fail with message:");
				log.error(response.message());
			}

		}catch(Exception e)
		{
			log.error("Exception occur in TestDtaCopy() on axiom ",e);
			log.error(e.getMessage());
		}


		return isTestDataCopied;
	}

	/**
	 *  This function will upload the game preset zip fle on axiom environment.
	 * 
	 * @param gamePresetsZipfile
	 * @param gameProvider
	 * @param architecture
	 * @return the boolean flag depending upon the response
	 */

	public boolean uploadGamePresetZip(String gamePresetsZipfile,String gameProvider,String architecture )
	{
		boolean isfileUploaded=false;
		String responseStatus=null;
		String tempZipFileName="preset.zip";
		Response response=null;
		try{
			String envName=TestPropReader.getInstance().getProperty("ExecutionEnv");
			OkHttpClient okHttpClientclient = getHTTPClient();
			

			RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("presetFormFile",
					gamePresetsZipfile,
					RequestBody.create(MediaType.parse("application/octet-stream"), new File(gamePresetsZipfile)))
					.build();
			Request request = new Request.Builder()
					.url(TestPropReader.getInstance().getProperty("AxiomEnvRefererUrl")+"-"+envName+".installprogram.eu"+"/Upload/GamePresets?architecture="+architecture+"&isProgressive=false")
					.method("POST", body)
					.build();
			response = okHttpClientclient.newCall(request).execute();

			log.debug("response code::"+response.code());
			System.out.println("response code::"+response.code());
			
			if(response.isSuccessful() && response.code()==200)
			{
				JsonParser parse = new JsonParser();
				JsonObject jobj = (JsonObject) parse.parse(response.body().string());

				responseStatus=jobj.get("dataObject").toString();
				if(responseStatus.equalsIgnoreCase("true"))
					isfileUploaded=true;
				log.info("Game preset zip file uploaded sucessfully on axiom environment successfully");
			}
			else
			{
				log.error("Game preset zip file  on axiom environment request fail with message:");
				log.error(response.body().string());
			}

		}catch(Exception e)
		{
			log.error("Exception occur in uploadGamePresetZip() on axiom ",e);
			log.error(e.getMessage());
		}


		return isfileUploaded;
	}


	/**
	 * This function will apply Filter type (e.g. AdvancedSlotsFilter) for given module and clinet Id on axiom environment.
	 * @param moduleId
	 * @param clientId
	 * @param filterType
	 * @return   boolean flag depending upon the response
	 */
	public boolean applyFilterMap(String moduleId,String clientId,String filterType)
	{
		boolean isFilterMapApply=false;
		String responseStatus=null;
		Response response=null;
		try{
			String envName=TestPropReader.getInstance().getProperty("ExecutionEnv");	
			OkHttpClient okHttpClient = getHTTPClient();
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, "{\"ModuleId\":"+moduleId+",\"ClientId\":"+clientId+",\"FilterType\":\""+filterType+"\"}");
			Request request = new Request.Builder()
					.url(TestPropReader.getInstance().getProperty("AxiomEnvRefererUrl")+"-"+envName+".installprogram.eu"+"/GameSettings/FilterMapType")
					.method("PATCH", body)
					.addHeader("Content-Type", "application/json")
					.build();
			response = okHttpClient.newCall(request).execute();
			if(response.code()==200)
			{
				JsonParser parse = new JsonParser();
				JsonObject jobj = (JsonObject) parse.parse(response.body().string());

				responseStatus=jobj.get("dataObject").toString();
				if(responseStatus.equalsIgnoreCase("true"))
					isFilterMapApply=true;
				log.info("Filter map applyed sucessfully on axiom environment ");

			}
			else
			{
				log.error("applyFilterMap request fail with message:");
				log.error(response.message());
			}
		}catch(Exception e)
		{
			log.error("Exception occur in applyFilterMap() ",e);
			log.error(e.getMessage());
		}

		return isFilterMapApply;
	}

	/**
	 * This function will apply database settings for given module and clinet Id ,game provider(e.g. mgs), and game category(e.g. 5ReelSlots) on axiom environment.
	 * @param moduleId
	 * @param clientId
	 * @param gameProvider
	 * @param gameCategory
	 * @return boolean flag depending upon the response
	 */

	public boolean applyDatabaseSettings(String moduleId,String clientId,String gameProvider,String gameCategory)
	{
		boolean isDatabaseSettingApply=false;
		String responseStatus=null;

		Response response=null;
		try{
			String envName=TestPropReader.getInstance().getProperty("ExecutionEnv");
			OkHttpClient okHttpClient = getHTTPClient();
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, "{\"ModuleId\":"+moduleId+",\"ClientId\":"+clientId+",\"GameProvider\":\""+gameProvider+"\",\"GameCategory\":\""+gameCategory+"\"}");
			Request request = new Request.Builder()
					.url(TestPropReader.getInstance().getProperty("AxiomEnvRefererUrl")+"-"+envName+".installprogram.eu"+"/GameSettings/MobileGameDatabaseSettings")
					.method("PATCH", body)
					.addHeader("Content-Type", "application/json")
					.build();
			response = okHttpClient.newCall(request).execute();
			if(response.code()==200)
			{
				JsonParser parse = new JsonParser();
				JsonObject jobj = (JsonObject) parse.parse(response.body().string());

				responseStatus=jobj.get("dataObject").toString();
				if(responseStatus.equalsIgnoreCase("true"))
					isDatabaseSettingApply=true;
				log.info("Game Databse settings applyed sucessfully on axiom environment ");

			}
			else
			{
				log.error("applyDatabaseSettings request fail with message:");
				log.error(response.message());
			}
		}catch(Exception e)
		{
			log.error("Exception occur in applyDatabaseSettings() ",e);
			log.error(e.getMessage());
		}

		return isDatabaseSettingApply;
	}


	public OkHttpClient getHTTPClient(){

		OkHttpClient client = null;
		/* Trust All Certificates */
		final TrustManager[] trustManagers = new TrustManager[]{new X509ExtendedTrustManager () {
			//@Override
			public X509Certificate[] getAcceptedIssuers() {
				X509Certificate[] x509Certificates = new X509Certificate[0];
				return x509Certificates;
			}

			@Override
			public void checkServerTrusted(final X509Certificate[] chain,
					final String authType) throws CertificateException {
				log.debug( ":authType: " + authType);
			}

			@Override
			public void checkClientTrusted(final X509Certificate[] chain,
					final String authType) throws CertificateException {
				log.debug( ": authType:" + authType);
			}

			//@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType, String arg2, String arg3)
					throws CertificateException {
				// Do nothing
			}

			//@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType, String arg2, String arg3)
					throws CertificateException {
				// Do nothing
			}

			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket)
					throws CertificateException {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket)
					throws CertificateException {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine)
					throws CertificateException {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine)
					throws CertificateException {
				// TODO Auto-generated method stub
				
			}


		}};

		X509ExtendedTrustManager  x509TrustManager = new X509ExtendedTrustManager () {
			@Override
			public X509Certificate[] getAcceptedIssuers() {
				X509Certificate[] x509Certificates = new X509Certificate[0];
				return x509Certificates;
			}

			@Override
			public void checkServerTrusted(final X509Certificate[] chain,
					final String authType) throws CertificateException {
				log.debug( ": authType: " + authType);
			}

			@Override
			public void checkClientTrusted(final X509Certificate[] chain,
					final String authType) throws CertificateException {
				log.debug( ": authType: " + authType);
			}

			//@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType, String arg2, String arg3)
					throws CertificateException {
				// Do nothing
			}

			//@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType, String arg2, String arg3)
					throws CertificateException {
				// Do nothing
			}

			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket)
					throws CertificateException {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket)
					throws CertificateException {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine)
					throws CertificateException {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine)
					throws CertificateException {
				// TODO Auto-generated method stub
				
			}
		};

		OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
		try {
			okHttpClientBuilder.readTimeout(5, TimeUnit.MINUTES);
			final String PROTOCOL = "SSL";
			SSLContext sslContext = SSLContext.getInstance(PROTOCOL);
			KeyManager[] keyManagers = null;
			SecureRandom secureRandom = new SecureRandom();
			sslContext.init(keyManagers, trustManagers, secureRandom);
			SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
			okHttpClientBuilder.sslSocketFactory(sslSocketFactory, x509TrustManager);
		} catch (Exception e) {
			log.error("Exception in  getHTTPClient()",e);
		}

		HostnameVerifier hostnameVerifier = new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				log.debug( ": hostname: " + hostname);
				return true;
			}
		};
		okHttpClientBuilder.hostnameVerifier(hostnameVerifier);
		client = okHttpClientBuilder.build();

		return client;
	}

	public String  getAssignFreeGameToken()
	{
		Response response=null;
		String accessTokenResponse=null;
		try{


			OkHttpClient client = getHTTPClient();

			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, "{\r\n  \"apiKey\": \"e21ff5d7-893a-4198-a8c7-2e836aaedea4\"\r\n}");
			Request request = new Request.Builder()
					.url("http://"+TestPropReader.getInstance().getProperty("WebServer4")+"/system/operatorsecurity/v1/operatortokens")
					.method("POST", body)
					.addHeader("Content-Type", "application/json")
					.build();
			response = client.newCall(request).execute();

			if(response.code()==200)
			{
				JsonParser parse = new JsonParser();
				JsonObject jobj = (JsonObject) parse.parse(response.body().string());


				accessTokenResponse=jobj.get("AccessToken").getAsString();
				log.info("Free Game offer Access token"+accessTokenResponse);
			}
			else
			{
				log.error("Response from getAssignFreeGameToken = "+response.message());
			}


		}catch(Exception e)
		{
			log.error("Exception occur in getAssignFreeGameToken() ",e);
			log.error(e.getMessage());
		}finally {
			if (response != null && response.body() != null) {
				response.body().close();
			}
		}
		return accessTokenResponse;
	}

	public FreeGameOfferResponse  addFreeGameOffer(int defaultNumberOfGames,String offerName , String offerExpirationUtcDate, String balanceTypeId, int mid,int Cid)
	{

		String accessTokenResponse=null;
		String offerAvailableFromUtcDate=null;
		FreeGameOfferResponse freeGameresponse=null;
		Response response=null;
		try{


			OkHttpClient client = getHTTPClient();
			accessTokenResponse=getAssignFreeGameToken();
			// Set offerAvailableFromUtcDate to day before current date 
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss'.000Z'");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			offerAvailableFromUtcDate=formatter.format(cal.getTime());


			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, "{\r\n    \"idempotencyId\": \""+UUID.randomUUID().toString()+"\",\r\n    \"defaultNumberOfGames\": "+defaultNumberOfGames+",\r\n    \"offerName\" : \""+offerName+"\",\r\n    \"offerAvailableFromUtcDate\" : \""+offerAvailableFromUtcDate+"\",\r\n    \"offerExpirationUtcDate\" : \""+offerExpirationUtcDate+"\",\r\n    \"balanceTypeId\" : "+balanceTypeId+",\r\n    \"durationAvailableAfterAwarded\": {\r\n    \t\"length\": 6,\r\n    \t\"timeUnitId\": 6\r\n    },\r\n    \"games\": \r\n    [\r\n        \r\n        {\r\n            \"moduleId\" : "+mid+",\r\n            \"clientId\" : "+Cid+",\r\n            \"nearestCostPerBet\": 0.09\r\n        }\r\n          \r\n          \r\n        \r\n    ]\r\n}");
			Request request = new Request.Builder()
					.url("http://"+TestPropReader.getInstance().getProperty("WebServer4")+"/casino/freeGames/v1/offers/nearestCost/product/5001")
					.method("POST", body)
					.addHeader("Content-Type", "application/json")
					.addHeader("Authorization", "Bearer "+accessTokenResponse)
					.build();
			response = client.newCall(request).execute();
			if(response.code()==200)
			{
				freeGameresponse=new FreeGameOfferResponse();
				JsonParser parse = new JsonParser();
				JsonObject jobj = (JsonObject) parse.parse(response.body().string());

				JsonArray gamesArray=jobj.getAsJsonArray("games");
				freeGameresponse.setOfferId(jobj.get("offerId").getAsString());
				JsonElement ele=gamesArray.get(0);
				JsonObject obj=ele.getAsJsonObject();
				freeGameresponse.setCostPerBet(obj.get("costPerBet").getAsString());
				freeGameresponse.setOfferName(jobj.get("offerName").getAsString());
				freeGameresponse.setOfferAvailableFromUtcDate(offerAvailableFromUtcDate);
				freeGameresponse.setDefaultNumberOfGames(defaultNumberOfGames);
				log.info("Free Game offer ID assined from API call"+freeGameresponse.getOfferId());
			}
			else
			{
				log.error(" Response from addFreeGameOffer = "+response.message());
			}

		}catch(Exception e)
		{
			log.error("Exception occur in addFreeGameOffer() ",e);
			log.error(e.getMessage());
		}finally {
			if (response != null && response.body() != null) {
				response.body().close();
			}
		}
		return freeGameresponse;

	}
	//Assing free game offer ID to user id
	public boolean assignFreeGameOfferByOfferID(String userName, String offerID,String offerAvailableFromUtcDate)
	{
		boolean	isfreeGameAssigned=false;
		Response response=null;
		try{


			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd,' 'HH:mm:ss,'+00:00'");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
			offerAvailableFromUtcDate=formatter.format(new Date());


			DataBaseFunctions dbfunction = new DataBaseFunctions(TestPropReader.getInstance().getProperty("ServerName"),
					TestPropReader.getInstance().getProperty("dataBaseName"),
					TestPropReader.getInstance().getProperty("serverIp"), TestPropReader.getInstance().getProperty("port"),
					TestPropReader.getInstance().getProperty("serverID"));

			String userId=dbfunction.getUserID(userName);
			String accessTokenResponse=getAssignFreeGameToken();
			OkHttpClient client = getHTTPClient();

			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, "{\n\t\"idempotencyId\":\""+UUID.randomUUID().toString()+"\",\n\t\"offerAvailableFromUtcDate\": \""+offerAvailableFromUtcDate+"\"\n}");
			Request request = new Request.Builder()
					.url("http://"+TestPropReader.getInstance().getProperty("WebServer4")+"/casino/freeGames/v1/offers/product/5001/user/"+userId+"/offer/"+offerID)
					.method("POST", body)
					.addHeader("Content-Type", "application/json")
					.addHeader("Authorization", "Bearer "+accessTokenResponse)
					.build();
			response = client.newCall(request).execute();
			if(response.code()==200)
			{
				isfreeGameAssigned=true;
				log.info("Free Game offer ID assined ...");
			}
			else
			{
				log.error("Respone for assignFreeGameOfferByOfferID ="+response.message());
			}

		}catch(Exception e)
		{
			log.error("Exception occur in assignFreeGameOfferByOfferID() ",e);
			log.error(e.getMessage());
		} finally {
			if (response != null && response.body() != null) {
				response.body().close();
			}
		}
		return isfreeGameAssigned;
	}


	/**
	 * This method will create free game in axiom environment
	 * @param userName
	 * @param freeGameName
	 * @param defaultNoOfFreeGames
	 * @param offerExpirationUtcDate
	 * @param balanceTypeID
	 * @param mid
	 * @param cid
	 * @return
	 */
/*	public boolean createFreeGameInAxiom(String userName,String freeGameName,int defaultNoOfFreeGames, String offerExpirationUtcDate,String balanceTypeID,int mid,int cid)
	{
		Response response=null;
		String responseStatus=null;
		boolean isFreeGameAssign=false;
		try{
			String userID=getUserIdFromAxiom(userName);
			String envName=TestPropReader.getInstance().getProperty("ExecutionEnv");
			OkHttpClient client = getHTTPClient();

			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, "{\"UserId\":"+userID+","
					+ "\r\n\"FreeGameOfferByNearestCostRequest\":\r\n "
					+ "		   {\"DefaultNumberOfGames\":"+defaultNoOfFreeGames+",\r\n"
					+ "    \"OfferName\":\""+freeGameName+"\",\r\n    \r\n    "
					+ "\"OfferAvailableFromUtcDate\":null,\r\n    "
					+ "\"OfferExpirationUtcDate\":\""+offerExpirationUtcDate+"\",\r\n    "
					+ "\"BalanceTypeId\":"+balanceTypeID+",\r\n        "
					+ "\"DurationAvailableAfterAwarded\":\r\n            "
					+ "{\"Length\":5,\r\n            "
					+ "\"TimeUnitId\":5\r\n            },\r\n            "
					+ "\"Games\":[\r\n                "
					+ "{\"NearestCostPerBet\":0.5,\r\n                "
					+ "\"ModuleId\":"+mid+",\r\n                "
					+ "\"ClientId\":"+cid+"\r\n                }]"
					+ "\r\n    }\r\n}");
			Request request = new Request.Builder()
					.url(TestPropReader.getInstance().getProperty("AxiomEnvRefererUrl")+"-"+envName+".installprogram.eu"+"/FreeGames/FreeGamesOffer")
					.method("POST", body)
					.addHeader("Content-Type", "application/json")
					.build();

			response = client.newCall(request).execute();

			if(response.code()==200)
			{
				JsonParser parse = new JsonParser();
				JsonObject jobj = (JsonObject) parse.parse(response.body().string());

				responseStatus=jobj.get("customMessage").toString();
				if(responseStatus.contains("SUCCESS"))
				{
					log.info("Free game assign to user");
					isFreeGameAssign=true;
				}
			}
			else
			{
				log.error("Response from createFreeGameInAxiom = "+response.message());
			}


		}catch(Exception e)
		{
			log.error("Exception occur in createFreeGameInAxiom ",e);
			log.error(e.getMessage());
		}finally {
			if (response != null && response.body() != null) {
				response.body().close();
			}
		}
		return isFreeGameAssign;
	}
*/
	/**
	 * This method will create free game in axiom environment
	 * @param userName
	 * @param freeGameName
	 * @param defaultNoOfFreeGames
	 * @param offerExpirationUtcDate
	 * @param balanceTypeID
	 * @param mid
	 * @param cid
	 * @return
	 */
	public boolean createFreeGameInAxiom(String userName,String freeGameName,int defaultNoOfFreeGames, String offerExpirationUtcDate,String balanceTypeID,int mid,int cid)
	{
		Response response=null;
		String responseStatus=null;
		boolean isFreeGameAssign=false;
		try{
			String userID=getUserIdFromAxiom(userName);
			String envName=TestPropReader.getInstance().getProperty("ExecutionEnv");
			OkHttpClient client = getHTTPClient();

			MediaType mediaType = MediaType.parse("application/json");
		/*	RequestBody body = RequestBody.create(mediaType, "{\"UserId\":"+userID+","
					+ "\r\n\"FreeGameOfferByNearestCostRequest\":\r\n "
					+ "		   {\"DefaultNumberOfGames\":"+defaultNoOfFreeGames+",\r\n"
					+ "    \"OfferName\":\""+freeGameName+"\",\r\n    \r\n    "
					+ "\"OfferAvailableFromUtcDate\":null,\r\n    "
					+ "\"OfferExpirationUtcDate\":\""+offerExpirationUtcDate+"\",\r\n    "
					+ "\"BalanceTypeId\":"+balanceTypeID+",\r\n        "
					+ "\"DurationAvailableAfterAwarded\":\r\n            "
					+ "{\"Length\":5,\r\n            "
					+ "\"TimeUnitId\":5\r\n            },\r\n            "
					+ "\"Games\":[\r\n                "
					+ "{\"NearestCostPerBet\":0.5,\r\n                "
					+ "\"ModuleId\":"+mid+",\r\n                "
					+ "\"ClientId\":"+cid+"\r\n                }]"
					+ "\r\n    }\r\n}");
			Request request = new Request.Builder()
					.url(TestPropReader.getInstance().getProperty("AxiomEnvRefererUrl")+"-"+envName+".installprogram.eu"+"/FreeGames/FreeGamesOffer")
					.method("POST", body)
					.addHeader("Content-Type", "application/json")
					.build();

			response = client.newCall(request).execute();
*/

	String body2  = "{\"UserId\":"+userID+",\"FreeGameOfferByNearestCostRequest\":{\"DefaultNumberOfGames\":"+defaultNoOfFreeGames+",\"OfferName\":\"\",\"DefaultDisplayLine1\":\"Display Line 1\",\"DefaultDisplayLine2\":\"Display Line 2\",\"OfferAvailableFromUtcDate\":null,\"OfferExpirationUtcDate\":\""+"2039-02-19T23:59:00.000Z"+"\",\"BalanceTypeId\":0,\"DurationAvailableAfterAwarded\":{\"Length\":7,\"TimeUnitId\":4},\"Games\":[{\"NearestCostPerBet\":0.1,\"ModuleId\":"+mid+",\"ClientId\":"+cid+"}]}}";
			RequestBody body = RequestBody.create(mediaType, body2);
			System.out.println("body2: " + body2);
			
			
			Request request = new Request.Builder()
					.url(TestPropReader.getInstance().getProperty("AxiomEnvRefererUrl")+"-"+envName+".installprogram.eu"+"/FreeGames/FreeGamesOffer")
					.method("POST", body)
					.addHeader("Content-Type", "application/json")
					.build();

			response = client.newCall(request).execute();

			System.out.println("----------------------------------------------");
			System.out.println(response.body().string());
			System.out.println("----------------------------------------------");

			if(response.code()==200)
			{
//				JsonParser parse = new JsonParser();
//				JsonObject jobj = (JsonObject) parse.parse(response.body().string());
//				responseStatus=jobj.get("customMessage").toString();
//				if(responseStatus.contains("SUCCESS"))
//				{
					log.info("Free game assign to user");
					isFreeGameAssign=true;
//				}
			}
			else
			{
				log.error("Response from createFreeGameInAxiom = "+response.message());
			}


		}catch(Exception e)
		{
			log.error("Exception occur in createFreeGameInAxiom ",e);
			log.error(e.getMessage());
		}finally {
			if (response != null && response.body() != null) {
				response.body().close();
			}
		}
		return isFreeGameAssign;
	}



	/**
	 * 	
	 * @param userName
	 * @return
	 */
	public UserMetaData getUserMetaDataFromAxiom( String userName)
	{
		Response response=null;
		ObjectMapper mapper=new ObjectMapper();
		UserMetaData userInfo=new UserMetaData();
		try{
			String envName=TestPropReader.getInstance().getProperty("ExecutionEnv");
			OkHttpClient okHttpClientclient = getHTTPClient();
			if (okHttpClientclient != null) {
				Request request = new Request.Builder()
						//.url(TestPropReader.getInstance().getProperty("AxiomEnvRefererUrl")+"-"+envName+".installprogram.eu"+"/api/userId?name="+userName)
						//adding below as api call get change...test above once and then remove 
						.url(TestPropReader.getInstance().getProperty("AxiomEnvRefererUrl")+"-"+envName+".installprogram.eu"+"/UserAccounts/UserAccount?loginName="+userName)
						.method("GET", null)
						.build();
				response = okHttpClientclient.newCall(request).execute();

				if(response.isSuccessful() && response.code()==200)
				{
					String responseJson = response.body().string();
					userInfo=mapper.readValue(responseJson,UserMetaData.class);
					log.info("User metadata store for UserNmae="+userName);
				}
				else
				{
					log.error("getUserMetadatFromAxiom request  fail with message:");
					log.error(response.message());
				}

			}
		}catch(Exception e)
		{
			log.error("Exception occur in getUserIDFromAxiom()  ",e);
			log.error(e.getMessage());
		}

		return userInfo;
	}
	/**
	 * This Rest call will update the user balance in axiom environment
	 * @param userName
	 * @param balance
	 * @return
	 */
	public boolean updateBalanceUsingAPI(String userName,double balance)
	{
		boolean isBalanceUpdated=false;
		Response response=null;
		String responseStatus=null;
		UserMetaData userInfo=new UserMetaData();
		try{
			String envName=TestPropReader.getInstance().getProperty("ExecutionEnv");
			OkHttpClient okHttpClientclient = getHTTPClient();
			
			//Fetching user's meta data
			
			userInfo=getUserMetaDataFromAxiom(userName);
			String userId=userInfo.getUserID();
			int serverId=userInfo.getServerID();
			int productId=userInfo.getProductId();
			if (okHttpClientclient != null) {
				MediaType mediaType = MediaType.parse("application/json");
				RequestBody body = RequestBody.create(mediaType, "{\"UserId\":"+userId+",\"Amount\":"+balance+",\"ServerId\":"+productId+"}");
				Request request = new Request.Builder()
						.url(TestPropReader.getInstance().getProperty("AxiomEnvRefererUrl")+"-"+envName+".installprogram.eu/UserAccounts/Manage/Balance")
						.method("PATCH", body)
						.addHeader("Content-Type", "application/json")
						.build();
				response = okHttpClientclient.newCall(request).execute();
				//System.out.println(response);
				
				if(response.isSuccessful() && response.code()==200)
				{
					JsonParser parse = new JsonParser();
					JsonObject jobj = (JsonObject) parse.parse(response.body().string());

					responseStatus=jobj.get("dataObject").toString();
					if(responseStatus.equalsIgnoreCase("true"))
						isBalanceUpdated=true;
					log.info("User balance updated with Amount:"+balance);
				}

				else
				{
					log.error("updateBalanceInAxiom request  fail with message:");
					log.error(response.message());
				}
			}
		}catch(Exception e)
		{
			log.error("Exception occur in getUserIDFromAxiom()  ",e);
			log.error(e.getMessage());
		}

		return isBalanceUpdated;
	}


	/**
	 * Description: Method to delete copied test data file from axiom environment
	 * @input testdatafileName
	 * @return boolean
	 * */

	public boolean deleteAxiomTestDataFile(String testdataFileName)
	{
		boolean isTestDataDeleted=false;
		String responseStatus=null;

		Response response=null;

		try{
			String envName=TestPropReader.getInstance().getProperty("ExecutionEnv");
			OkHttpClient okHttpClientclient = getHTTPClient();
			if (okHttpClientclient != null) {
				MediaType mediaType = MediaType.parse("text/plain");
				RequestBody body = RequestBody.create(mediaType, "");
				Request request = new Request.Builder()
						.url(TestPropReader.getInstance().getProperty("AxiomEnvRefererUrl")+"-"+envName+".installprogram.eu/Manage/Content/File?filePath=C:\\MGS_Testdata\\"+testdataFileName)
						.method("DELETE", body)
						.build();
				response = okHttpClientclient.newCall(request).execute();

				if(response.isSuccessful() && response.code()==200)
				{
					JsonParser parse = new JsonParser();
					JsonObject jobj = (JsonObject) parse.parse(response.body().string());

					responseStatus=jobj.get("customMessage").toString();
					if(responseStatus.contains("File Deleted"))
					{
						log.info(testdataFileName+"  deleted from Environment "+envName);
						isTestDataDeleted=true;
					}
				}
				else
				{
					log.error("delete testdata on axiom environment request fail with message:");
					log.error(response.message());
				}
			}
		}catch(Exception e)
		{
			log.error("Exception occur in deleteAxiomTestDataFile() on axiom ",e);
			log.error(e.getMessage());
		}


		return isTestDataDeleted;
	}

	/**
	 * This method will delete the list of copied test files from axiom environment
	 * @param copiedFiles
	 * @return
	 */

	public boolean deleteAxiomTestDataFiles(List<String> copiedFiles)
	{
		boolean isTestDataDeleted=false;
		String responseStatus=null;
		int counter=0;
		Response response=null;

		try{
			String envName=TestPropReader.getInstance().getProperty("ExecutionEnv");
			OkHttpClient okHttpClientclient = getHTTPClient();
			if (okHttpClientclient != null) {
				MediaType mediaType = MediaType.parse("text/plain");
				RequestBody body = RequestBody.create(mediaType, "");

				while(counter<copiedFiles.size())
				{
					Request request = new Request.Builder()
							.url(TestPropReader.getInstance().getProperty("AxiomEnvRefererUrl")+"-"+envName+".installprogram.eu/Manage/Content/File?filePath=C:\\MGS_Testdata\\"+copiedFiles.get(counter))
							.method("DELETE", body)
							.build();
					response = okHttpClientclient.newCall(request).execute();

					if(response.isSuccessful() && response.code()==200)
					{
						JsonParser parse = new JsonParser();
						JsonObject jobj = (JsonObject) parse.parse(response.body().string());

						responseStatus=jobj.get("customMessage").toString();
						if(responseStatus.contains("File Deleted"))
						{
							log.info(copiedFiles.get(counter)+"  deleted from Environment "+envName);
							isTestDataDeleted=true;
						}
					}
					else
					{
						log.error("delete testdata on axiom environment request fail with message:");
						log.error(response.message());
					}
					counter++;
				}
				}
		}catch(Exception e)
		{
			log.error("Exception occur in deleteAxiomTestDataFile() on axiom ",e);
			log.error(e.getMessage());
		}


		return isTestDataDeleted;
	}

	public String getLatestTitanVersionBlueMesa(int envId)
	{

		Response response=null;
		GetAllBlueMesaEnvironmentsResponse envMetaData=null;
		String latestVesion = null;
		int entryNum=0;
		
		try{

			OkHttpClient client = getHTTPClient();
					 
					Request request = new Request.Builder()
					  .url(TestPropReader.getInstance().getProperty("ETCHostURL")+"/getAvailableTitanFrameworks?environmentID="+envId)
					  .method("GET", null)
					  .build();
			 response = client.newCall(request).execute();
			if(response.isSuccessful())
			{
				String responsenew = response.body().string();
				JsonParser parse = new JsonParser();
				JsonObject jsonObject = (JsonObject) parse.parse(responsenew);
				System.out.println(jsonObject.size());
				System.out.println(jsonObject.entrySet());
				Set<java.util.Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
				   for(Map.Entry<String,JsonElement> entry : entrySet){
					   int key= Integer.parseInt(entry.getKey());
					   if(key >= entryNum) {
						   JsonElement jsonElement=entry.getValue();
						   latestVesion=jsonElement.getAsString();
					   }
				   }
				log.info("Request success with response :"+ responsenew);
			}
			else
			{	
				log.info("Request fail with response :"+ response.message());

			}
		}catch(Exception e)
		{
			log.error("Exception occur in mapEnvNameToEnvId(String envName) ",e);
			log.error(e.getMessage());
		}
		return latestVesion;
	}


	public String getLatestTitanVersionAxiom()
	{

		Response response=null;
		String latestVesion = null;
		long longLatestver=0;
		
		try{

			OkHttpClient client = getHTTPClient();
					Request request = new Request.Builder()
					  .url("https://axiomcore-app1-gtp110.installprogram.eu/MobileSettings/TitanVersions")
					  .method("GET", null)
					  .build();
			response = client.newCall(request).execute();
			
			if(response.isSuccessful())
			{
				String responsenew = response.body().string();
				JsonParser parse = new JsonParser();
				JsonObject jsonObject = (JsonObject) parse.parse(responsenew);
				JsonArray titanVersionArray = (JsonArray) jsonObject.get("dataObject");
				
				   for(int count=0;count<titanVersionArray.size();count++){
					   JsonElement titanVersionElement = (JsonElement)titanVersionArray.get(count);
					   JsonObject titanVersionObject = titanVersionElement.getAsJsonObject();
					   JsonElement appVersionEle=(JsonElement)titanVersionObject.get("appVersion");
					   
					   
					   JsonElement isHealthyEle=(JsonElement)titanVersionObject.get("isHealthy");
					   boolean isHealthy=isHealthyEle.getAsBoolean();
					   
					   if(isHealthy) {
						   String appVersion=appVersionEle.getAsString();
						   long appVersionLong=Long.parseLong(appVersion.replace(".", ""));
						   
						   if(appVersionLong>=longLatestver) {
							   latestVesion=appVersion;
							   longLatestver=appVersionLong;
						   }
					   }
				   }			
				log.info("Request success with response :"+ responsenew);
			}
			else
			{	
				log.info("Request fail with response :"+ response.message());

			}
		}catch(Exception e)
		{
			log.error("Exception occur in mapEnvNameToEnvId(String envName) ",e);
			log.error(e.getMessage());
		}
		return latestVesion;
	}

/**
 * This method is used to crete the regulatory market users
 * @param userName
 * @param regMarket
 * @param envId
 * @param productId
 * @return
 */
	public boolean createRegMarketUser(String userName, String regMarket,int envId,int productId ) {
		boolean isUserCretd=false; 
		
		try {
			
			String etcHost=TestPropReader.getInstance().getProperty("ETCHostURL");
			OkHttpClient client = getHTTPClient();
			
					MediaType mediaType = MediaType.parse("application/json");
					RequestBody body = RequestBody.create(mediaType, "environmentID="+envId+"&registeredProductId="+productId+"&username="+userName+"&password=test1234$&siteTeamName=Game%20Changers");
					Request request = new Request.Builder()
					  .url(etcHost+"/"+regMarket+"?environmentID="+envId+"&registeredProductId="+productId+"&username="+userName+"&password=test1234$&siteTeam")
					  .method("POST", body)
					  .addHeader("Content-Type", "application/json")
					  .build();
					Response response = client.newCall(request).execute();
					System.out.println(response);
					log.debug(response);
					if(response.isSuccessful())
					{
						isUserCretd=true;
					}
					
		} 
		catch (IOException e) 
		{
			log.error("Exception occur ehile creating the user for reg market ",e);
		}
				
				
				
		return isUserCretd;
	}
	/**
	 * method will create the random user in  in Axiom Environment
	 * @param userName
	 * @param currencyIsoCode
	 * @param country
	 * @param marketTypeID
	 * @param serverID
	 * @return
	 */
	public boolean createRegMarketUserInAxiom( String userName,String currencyIsoCode,String regMarket ,int marketTypeID, int productId  )
	{
		boolean isUsrCreated=false;
		String responseStatus=null;
		Response response=null;
		String password=TestPropReader.getInstance().getProperty("AxiomUserPassword");
		try{
			String envName=TestPropReader.getInstance().getProperty("ExecutionEnv");

			OkHttpClient okHttpClientclient = getHTTPClient();
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, "{\"Username\":\""+userName+"\","
					+ "\"Password\":\""+password+"\","
					+ "\"MarketTypeId\":"+marketTypeID+","
					+ "\"ServerId\":"+productId+","
					+ "\"UserTypeId\":0,"
					+ "\"currencyIsoCode\":\""+currencyIsoCode+"\","
					+ "\"Country\":\""+regMarket+"\","
					+ "\"NumberOfAccounts\":1}");
			if (okHttpClientclient != null) {
				Request request = new Request.Builder()
						.url(TestPropReader.getInstance().getProperty("AxiomEnvRefererUrl")+"-"+envName+".installprogram.eu"+"/UserAccounts")
						.method("POST", body)
						.addHeader("content-type", "application/json")
						.build();
				log.info("createUser request body"+request.body().toString());
				response = okHttpClientclient.newCall(request).execute();
				
				System.out.println(response.isSuccessful());
				System.out.println( response.code());

				if(response.isSuccessful() && response.code()==200)
				{
					JsonParser parse = new JsonParser();
					JsonObject jobj = (JsonObject) parse.parse(response.body().string());

					responseStatus=jobj.get("dataObject").toString();
					if(responseStatus.equalsIgnoreCase("true"))
						isUsrCreated=true;
					log.info("User created on axiom environment successfully");
				}
				else if(response.code()==400){
					JsonParser parse = new JsonParser();
					JsonObject jobj = (JsonObject) parse.parse(response.body().string());

					responseStatus=jobj.get("customMessage").toString();
					if(responseStatus.contains("UserAccount already taken"))
						log.info("User on axiom environment already created");
				}
				else
				{
					log.error("createUser on axiom environment request fail with message:");
					log.error(response.message());
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			log.error("Exception occur in createUser() on axiom ",e);
			log.error(e.getMessage());
		}


		return isUsrCreated;
	}
	
	/**
	 * This method will create the game url for axiom lobby
	 * @param userName
	 * @param gameDisplayName
	 * @param gameVersion
	 * @return
	 */
	public String quickFireAxiomCreateURL(String userName,String gameVersion,int moduleId, int clientId,String lobbyname, String host)
	{
		String strUrl=null;
		String url = null;
		String  frameWorkVersion=null;
		String password=TestPropReader.getInstance().getProperty("AxiomUserPassword");
		RestAPILibrary apiobj = new RestAPILibrary();
		try{
			String frameWorkType =TestPropReader.getInstance().getProperty("FrameWorkType");
			if(frameWorkType.equalsIgnoreCase("Titan"))
			{
				frameWorkType="titan";
				frameWorkVersion=TestPropReader.getInstance().getProperty("TitanVersion");
				if(frameWorkVersion==null||"".equalsIgnoreCase(frameWorkVersion))
				{
					frameWorkVersion=apiobj.getLatestTitanVersionAxiom();
				}
			}
			else
			{
				frameWorkType="v";
				frameWorkVersion="";
			}
			String envName=TestPropReader.getInstance().getProperty("ExecutionEnv");
			OkHttpClient okHttpClient = getHTTPClient();
			Request request = new Request.Builder()
					.url(TestPropReader.getInstance().getProperty("AxiomEnvRefererUrl")+"-"+envName+".installprogram.eu"+"/launch/GameUrl?"
							+ "gameProvider=null"
							+ "&username="+""
							+ "&password="+password
							+ "&lobbyName="+lobbyname
							+ "&moduleId="+moduleId
							+ "&clientId="+clientId
							+ "&gameVersion="+gameVersion
							+ "&languageCode=en"
							+ "&host="+host
							+ "&iframe=false"
							+ "&framework="+frameWorkType
							+ "&frameworkVersion="+frameWorkVersion)
					.method("GET", null)
					.build();
			Response response = okHttpClient.newCall(request).execute();
			if(response.code()==200)
			{
				JsonParser parse = new JsonParser();
				JsonObject jobj = (JsonObject) parse.parse(response.body().string());

				strUrl=jobj.get("dataObject").toString();
				url=strUrl.substring(1,strUrl.length()-1);
				//check with QAleaads why we need this addition
				url=url+"&site=bluemesa";
			}
			else
			{
				log.error("createURl request fail with message:");
				log.error(response.message());
			}
		}catch(Exception e)
		{
			log.error("Exception occur in createURL() ",e);
			log.error(e.getMessage());
		}


		return url;
	}
	/**
	 * This Rest call will update the user balance in axiom environment
	 * @param userName
	 * @param balance
	 * @return
	 */
	public boolean updateBalanceUsingAPI(String userName,String balance)
	{
		boolean isBalanceUpdated=false;
		Response response=null;
		String responseStatus=null;
		UserMetaData userInfo=new UserMetaData();
		int retryCount = 10;
		int count=0;
		
		try{
			String envName=TestPropReader.getInstance().getProperty("ExecutionEnv");
			OkHttpClient okHttpClientclient = getHTTPClient();
			
			//Fetching user's meta data			
			userInfo=getUserMetaDataFromAxiom(userName);
			String userId=userInfo.getUserID();
			int productId=userInfo.getProductId();
			
			if (okHttpClientclient != null) {
				MediaType mediaType = MediaType.parse("application/json");
				RequestBody body = RequestBody.create(mediaType, "{\"UserId\":"+userId+",\"Amount\":"+balance+",\"ServerId\":"+productId+"}");
				
				while(count<retryCount) 
				{
				Request request = new Request.Builder()
						.url(TestPropReader.getInstance().getProperty("AxiomEnvRefererUrl")+"-"+envName+".installprogram.eu/UserAccounts/Manage/Balance")
						.method("PATCH", body)
						.addHeader("Content-Type", "application/json")
						.build();
				response = okHttpClientclient.newCall(request).execute();
				System.out.println("response code for update balance :: "+response.code());
				log.debug("response code::"+response.code());
				
				if(response.isSuccessful() && response.code()==200)
				{
					JsonParser parse = new JsonParser();
					JsonObject jobj = (JsonObject) parse.parse(response.body().string());

					responseStatus=jobj.get("dataObject").toString();
					if(responseStatus.equalsIgnoreCase("true"))
						isBalanceUpdated=true;
					log.info("User balance updated with Amount:"+balance);
					break;
				}
				else
				{
					log.error("updateBalanceInAxiom request  fail with message:");
					log.error(response.message());
					response.body().close();
					Thread.sleep(10000);
				}
				count++;
				}
			}
		}catch(Exception e)
		{
			log.error("Exception occur in getUserIDFromAxiom()  ",e);
			log.error(e.getMessage());
		}
		
		
		return isBalanceUpdated;
	}
	

}

