package com.zensar.automation.framework.api;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
//import com.sun.xml.internal.ws.server.provider.ProviderArgumentsBuilder;
import com.zensar.automation.framework.library.PropertyReader;
import com.zensar.automation.framework.model.DeviceInfo;
import com.zensar.automation.framework.model.DeviceList;
import com.zensar.automation.framework.model.STFService;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * This class provides the capability to connect or disconnect device.
 */
public class DeviceApi {
	Logger log = Logger.getLogger(DeviceApi.class.getName());
    private OkHttpClient client;
    private JsonParser jsonParser;
    private STFService stfService;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
   
	private volatile String deviceProvider;
	private volatile String providerUrl;

    public DeviceApi(STFService stfService) {
        this.jsonParser = new JsonParser();
        this.stfService = stfService;
        log.info("stf service request info="+stfService);
        String TAG = "STF ---- LOG --- : ";
        
        /* Trust All Certificates */
        final TrustManager[] trustManagers = new TrustManager[]{new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] x509Certificates = new X509Certificate[0];
                return x509Certificates;
            }

            @Override
            public void checkServerTrusted(final X509Certificate[] chain,
                                           final String authType) throws CertificateException {
                System.out.println(TAG + ": authType: " + String.valueOf(authType));
            }

            @Override
            public void checkClientTrusted(final X509Certificate[] chain,
                                           final String authType) throws CertificateException {
                System.out.println(TAG + ": authType: " + String.valueOf(authType));
            }
        }};

        X509TrustManager x509TrustManager = new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] x509Certificates = new X509Certificate[0];
                return x509Certificates;
            }

            @Override
            public void checkServerTrusted(final X509Certificate[] chain,
                                           final String authType) throws CertificateException {
                System.out.println(TAG + ": authType: " + String.valueOf(authType));
            }

            @Override
            public void checkClientTrusted(final X509Certificate[] chain,
                                           final String authType) throws CertificateException {
                System.out.println(TAG + ": authType: " + String.valueOf(authType));
            }
        };
        
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        try {
            final String PROTOCOL = "SSL";
            SSLContext sslContext = SSLContext.getInstance(PROTOCOL);
            KeyManager[] keyManagers = null;
            SecureRandom secureRandom = new SecureRandom();
            sslContext.init(keyManagers, trustManagers, secureRandom);
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            okHttpClientBuilder.sslSocketFactory(sslSocketFactory, x509TrustManager);
        } catch (Exception e) {
           log.error( e.getMessage(),e);
        }

        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                System.out.println(TAG + ": hostname: " + String.valueOf(hostname));
                log.info(TAG + ": hostname: " + String.valueOf(hostname));
                    return true;
              
            }
        };
        
        okHttpClientBuilder.hostnameVerifier(hostnameVerifier);
        this.client = okHttpClientBuilder.build();
    }

    public boolean connectDevice(String deviceSerial) {
    	log.info("Before Connecting to device");
    	log.info("Token="+stfService.getAuthToken());
    	log.info("Device ID="+deviceSerial);
        Request request = new Request.Builder()
                .addHeader("Authorization", "Bearer " + stfService.getAuthToken())
                .url(stfService.getStfUrl() + "devices/" + deviceSerial)
                .build();
                Response response;
        log.info("Request URL:"+request.toString());
        try {
            response = client.newCall(request).execute();
            JsonObject jsonObject = jsonParser.parse(response.body().string()).getAsJsonObject();

            if (!isDeviceFound(jsonObject)) {
                return false;
            }

			System.out.println("\nSerial:   " + deviceSerial + "\nResponse Details: " + jsonObject);
			log.info("\nSerial:   " + deviceSerial + "\nResponse Details: " + jsonObject);
            JsonObject deviceObject = jsonObject.getAsJsonObject("device");
            boolean present = deviceObject.get("present").getAsBoolean();
            boolean ready = deviceObject.get("ready").getAsBoolean();
            boolean using = deviceObject.get("using").getAsBoolean();
            JsonElement ownerElement = deviceObject.get("owner");
            boolean owner = !(ownerElement instanceof JsonNull);

            if (!present || !ready || using || owner) {
                log.error("Device is in use");
                return false;
            }

			providerUrl = deviceObject.getAsJsonObject("display").get("url").getAsString();
			deviceProvider = providerUrl.substring(providerUrl.indexOf("/d/"),providerUrl.indexOf(deviceSerial));
			deviceProvider = deviceProvider.replace("/d/","/appium/");
			log.info("Provider name of the device: "+deviceProvider);
			
            return addDeviceToUser(deviceSerial);
            
            
        } catch (IOException e) {
            throw new IllegalArgumentException("STF service is unreachable", e);
        }
    }
	
	public String getDeviceProvider()
	{
		return deviceProvider;
	}

	public String getProviderUrl(String deviceSerial,String osPlatform)
	{
			System.out.println("Waiting for proxy connection");
			log.debug("Waiting for proxy connection");
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e1) {
				log.error("Error throwing while waiting for proxy connection, can be ignored");
				return null;
				
			}
		
			log.info("Before Connecting to device");
			log.info("Token="+stfService.getAuthToken());
			log.info("Device ID="+deviceSerial);
		    Request request = new Request.Builder()
		            .addHeader("Authorization", "Bearer " + stfService.getAuthToken())
		            .url(stfService.getStfUrl() + "devices/" + deviceSerial)
		            .build();
		            Response response;
		    log.info("Request URL:"+request.toString());
		    try {
		    	response = client.newCall(request).execute();
		    	JsonObject jsonObject = jsonParser.parse(response.body().string()).getAsJsonObject();

		    	JsonObject deviceObject = jsonObject.getAsJsonObject("device");

		    	System.out.println("REspose b4 getting Appium URL::"+jsonObject);
		    	
		    	String remmoteConnectUrl=deviceObject.getAsJsonPrimitive("remoteConnectUrl").getAsString();
		    	
		    	System.out.println("remmoteConnectUrl::"+remmoteConnectUrl);
		    	
		    	providerUrl =remmoteConnectUrl;
		    		    	

		    	//http://10.120.87.248/appium/pune1/wd/hub
		    	//http://10.120.87.248/appium/pune2/wd/hub
		    	//http://10.120.87.250:4723/wd/hub
		    	log.info("Provider Url of the device: "+providerUrl);

		    	
		    } catch (IOException e) {
		    	providerUrl=null;
		        throw new IllegalArgumentException("STF service is unreachable", e);
		    }catch (Exception e) {
		    	providerUrl=null;
		    	log.error("Device might not be available or checked out by someone else",e);
		    }
		    return providerUrl;
	}
    private boolean isDeviceFound(JsonObject jsonObject) {
        if (!jsonObject.get("success").getAsBoolean()) {
            log.error("Device not found");
            return false;
        }
        return true;
    }
    private boolean addDeviceToUser(String deviceSerial) {
		return false;
    	//return addDeviceToUser(deviceSerial, 36000000);
    	
    }
    
    private boolean addDeviceToUser(String deviceSerial, int timeout) {
        RequestBody requestBody = RequestBody.create(JSON, "{\"serial\": \"" + deviceSerial + "\", \"timeout\": "+timeout+"}");
        Request request = new Request.Builder()
                .addHeader("Authorization", "Bearer " + stfService.getAuthToken())
                .url(stfService.getStfUrl() + "user/devices")
                .post(requestBody)
                .build();
        Response response;
        try {
            response = client.newCall(request).execute();
            JsonObject jsonObject = jsonParser.parse(response.body().string()).getAsJsonObject();

            if (!isDeviceFound(jsonObject)) {
                return false;
            }

            log.info("The device <" + deviceSerial + "> is locked successfully");
            return true;
        } catch (IOException e) {
            throw new IllegalArgumentException("STF service is unreachable", e);
        }
    }

    public boolean releaseDevice(String deviceSerial) {
        Request request = new Request.Builder()
                .addHeader("Authorization", "Bearer " + stfService.getAuthToken())
                .url(stfService.getStfUrl() + "user/devices/" + deviceSerial)
                .delete()
                .build();
        Response response;
        try {
            response = client.newCall(request).execute();
            JsonObject jsonObject = jsonParser.parse(response.body().string()).getAsJsonObject();

            if (!isDeviceFound(jsonObject)) {
                return false;
            }

            log.info("The device <" + deviceSerial + "> is released successfully");
            return true;
        } catch (IOException e) {
            throw new IllegalArgumentException("STF service is unreachable", e);
        }
    }
    
    public boolean isDeviceConnected(String deviceSerial) {
    	 boolean owner=false;
    	log.info("Before Connecting to device");
    	log.info("Token="+stfService.getAuthToken());
    	log.info("Device ID="+deviceSerial);
        Request request = new Request.Builder()
                .addHeader("Authorization", "Bearer " + stfService.getAuthToken())
                .url(stfService.getStfUrl() + "devices/" + deviceSerial)
                .build();
                Response response;
        log.info("Request URL:"+request.toString());
        try {
            response = client.newCall(request).execute();
            JsonObject jsonObject = jsonParser.parse(response.body().string()).getAsJsonObject();

            System.out.println("\nSerial:   " + deviceSerial + "\nResponse Details: " + jsonObject);
			log.info("\nSerial:   " + deviceSerial + "\nResponse Details: " + jsonObject);
			
            if (!isDeviceFound(jsonObject)) {
                return false;
            }

			
            JsonObject deviceObject = jsonObject.getAsJsonObject("device");
            
            JsonElement ownerElement = deviceObject.get("owner");
            log.info("Current Owner :"+ownerElement);
            
            owner = !(ownerElement instanceof JsonNull);

			log.info("Provider name of the device: "+deviceProvider);
           
        }catch(Exception e)
        {
        	log.error(e.getMessage(),e);
        }
        return owner;
        }
    
    /*Description: This method read the all device list of given STF_SERVICE_URL
	 * @return:device list of provided STF service
	 * */
	
	public DeviceList getAllDevices()
	{
		DeviceList deviceList=null;
		OkHttpClient okHttpClientclient = this.client;
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		log.debug("getAllDevices():STFURL ="+stfService.getStfUrl());
		log.debug("getAllDevices():Access Token"+stfService.getAuthToken());
		if (okHttpClientclient != null) {
			Request request = new Request.Builder().url(stfService.getStfUrl() + "devices/")
					.addHeader("Authorization", "Bearer " + stfService.getAuthToken())
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
				
				log.error("JsonMappingException:"+e.getCause()+"/n"+e.getMessage(),e);
			} catch (IOException e) {
				
			log.error("IOException:"+e.getCause());
			}catch (Exception e) {
				log.error("Exception occur in getAllDevices(): ", e);
			}
			
		}
		return deviceList;
	}
	
	/*Description: This method read the all device list which are in ready state
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
				try{
					String [] notes=deviceInfo.getNotes().split("_");
					if(notes[1]!=null)
					{
						deviceInfo.setMarketName(notes[1]);
					}

				}catch(Exception exception)
				{
					deviceInfo.setMarketName("");
				}
				readyDeviceList.add(deviceInfo);
			}
		}
		return readyDeviceList;
		
		
	}
	
}
