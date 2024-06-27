package com.zensar.automation.framework.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.log4j.//logger;
import org.json.simple.JSONObject;

import com.zensar.automation.framework.model.Response;


public class RestWebserviceCaller {

	public static StringBuilder stringBuilder;
	
	public static Integer parentStatusCode;
	public static HttpClient http;
	public static HttpPost post;
	public static HttpPut put;
	public static HttpGet get;
	public static HttpDelete delete;
	public static HttpResponse httpRes;
	public static BufferedReader rd;
	public static BufferedReader bufferReader;
			

	static {
		http = new DefaultHttpClient();
		
	}

	// Get Service Call
	public static Response getWebserviceCall(String urlStr) throws IOException {
		get = new HttpGet(urlStr);
		try {
			httpRes = http.execute(get);
		} catch (NoHttpResponseException e) {
			
			return new Response(0, stringBuilder.toString());
		}
		parentStatusCode = httpRes.getStatusLine().getStatusCode();
		
		if (parentStatusCode != 200) {
			//return new Response(parentStatusCode, httpRes.toString());
		}
		try{
		bufferReader = new BufferedReader(new InputStreamReader(httpRes.getEntity().getContent()));
		stringBuilder = new StringBuilder();
        String line;
		while ((line = bufferReader.readLine()) != null) {
			stringBuilder.append(line);
		}
		bufferReader.close();
		return new Response(parentStatusCode, stringBuilder.toString());
		}catch(NullPointerException e)
		{
			String s=httpRes.toString();
			//http.getConnectionManager().shutdown();
			return new Response(parentStatusCode, s);
		}
	}

	//POST web service call
	public static Response postWebserviceCal(String urlStr, JSONObject inputJsonObject)
			throws Exception {

		post = new HttpPost(urlStr);
        
		post.setEntity(new StringEntity("" + inputJsonObject, ContentType.create("application/json")));
		
		try {
			httpRes = http.execute(post);
		} catch (NoHttpResponseException e) {
			
			return new Response(0, stringBuilder.toString());
		}
		
		parentStatusCode = httpRes.getStatusLine().getStatusCode();
		
		if (parentStatusCode != 200) {
			//return new Response(parentStatusCode, httpRes.toString());
			
		}
		try{
		bufferReader = new BufferedReader(new InputStreamReader(httpRes.getEntity().getContent()));
		stringBuilder = new StringBuilder();
        String line;
		while ((line = bufferReader.readLine()) != null) {
			stringBuilder.append(line);
		}
		bufferReader.close();
		return new Response(parentStatusCode, stringBuilder.toString());
		}catch(NullPointerException e)
		{
			String s=httpRes.toString();
			//http.getConnectionManager().shutdown();
			return new Response(parentStatusCode, s);
		}
	}

	//PUT webservice call
	public static Response putWebserviceCal(String urlStr, JSONObject inputJsonObject)
			throws Exception {

		
		put = new HttpPut(urlStr);
		

		put.setEntity(new StringEntity("" + inputJsonObject, ContentType.create("application/json")));
		
		try {
			httpRes = http.execute(put);
		} catch (NoHttpResponseException e) {
			
			return new Response(0, stringBuilder.toString());
		}
		parentStatusCode = httpRes.getStatusLine().getStatusCode();
		
		if (parentStatusCode != 200) {
			//return new Response(parentStatusCode, httpRes.toString());
		}
		try{
		bufferReader = new BufferedReader(new InputStreamReader(httpRes.getEntity().getContent()));
		stringBuilder = new StringBuilder();
        String line;
		while ((line = bufferReader.readLine()) != null) {
			stringBuilder.append(line);
		}
		bufferReader.close();
		return new Response(parentStatusCode, stringBuilder.toString());
		}catch(NullPointerException e)
		{
			String s=httpRes.toString();
			//http.getConnectionManager().shutdown();
			return new Response(parentStatusCode, s);
		}
	}
	
		// Deleet Service Call
		public static Response deleteWebserviceCall(String urlStr) throws IOException {
			
			delete = new HttpDelete(urlStr);
			//logger.info("The GET serive call ");

			try {
				httpRes = http.execute(delete);
			} catch (NoHttpResponseException e) {
				//logger.error(" The target server failed to respond ");
				return new Response(0, stringBuilder.toString());
			}
			parentStatusCode = httpRes.getStatusLine().getStatusCode();
			//logger.info("the parentStatus code is : " + parentStatusCode);
			if (parentStatusCode != 200) {

				//return new Response(parentStatusCode, httpRes.toString());
			}
			
			try{
			bufferReader = new BufferedReader(new InputStreamReader(httpRes.getEntity().getContent()));
			stringBuilder = new StringBuilder();
	        String line;
			while ((line = bufferReader.readLine()) != null) {
				stringBuilder.append(line);
			}
			bufferReader.close();
			return new Response(parentStatusCode, stringBuilder.toString());
			}catch(NullPointerException e)
			{
				String s=httpRes.toString();
				//http.getConnectionManager().shutdown();
				System.out.println("Catch");
				return new Response(parentStatusCode, s);
			}
			
		}


}
