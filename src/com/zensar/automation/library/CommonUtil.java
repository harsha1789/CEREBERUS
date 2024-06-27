package com.zensar.automation.library;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.Base64.Decoder;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.zensar.automation.api.RestAPILibrary;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.framework.utils.Util;
import com.zensar.automation.model.Currency;
import com.zensar.automation.model.UserMetaData;

public class CommonUtil extends  Util{



	Logger log = Logger.getLogger(CommonUtil.class.getName());
	/**
	 * This method copy testdata file from Testdata folder to server o specified environment
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
				apiobj.copyTestData( file.getPath(),fileName);
				copiedFiles.add(fileName);
				isTestDataCopied=true;
			}catch(Exception e)
			{
				isTestDataCopied=false;
				log.error("Exception while copying test data file",e);
			}
		} 

		return isTestDataCopied;
	}


	/**
	 * This method copy testdat for currency user on specified environment
	 * @param mid
	 * @param cid
	 * @param file
	 * @param userName
	 * @param copiedFiles
	 * @param currencyID
	 * @return
	 */
	public boolean copyFilesToTestServer(int mid,int cid,File file, String userName,List<String> copiedFiles, String currencyID,String... currencyCode ) {
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
				dbobject.createUser(userName, currencyID, 0);

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
			boolean  isUserCreated = apiobj.createUserInAxiom(userName,currencyCode);
				gcfnlib.changePlayerName(userName, file.getPath());
				apiobj.copyTestData(file.getPath(),fileName);
				copiedFiles.add(fileName);
				isTestDataCopied=true;
			}catch(Exception e)
			{
				isTestDataCopied=false;
				log.error("Exception while copying test data file",e);
			}
		} 

		return isTestDataCopied;
	}


	/**
	 * method will crate the user in database
	 * @param userName
	 * @return
	 */
	public boolean createUser(String userName)
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
	 * This function generate the random String
	 * @return String
	 */
	public String randomStringgenerator() {

		char[] chars = "abcdefghijklmnopqrstuvwxyz123456789".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 7; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		String output = sb.toString();
		log.debug("Random String has created for username");
		return "Zen_" + output;
	}


	/**
	 * method will create the random user in specified environment
	 * @return String
	 */

	public String createRandomUser()
	{
		String randomUserName=null;
		String envName=TestPropReader.getInstance().getProperty("EnvironmentName");
		RestAPILibrary apiObj = new RestAPILibrary();
		try{
			randomUserName=randomStringgenerator();
			//condition to create user on configured environments
			if(envName==null || envName.equalsIgnoreCase("Bluemesa"))
				createUser(randomUserName);
			else
				apiObj.createUserInAxiom(randomUserName);
		}catch(Exception e)
		{
			log.error("Fail to create user",e);

		}
		return randomUserName;
	}
	/**
	 * This function create rand string as free game offer name
	 * @return
	 */
	public String randomFreegameOfferName() {

		char[] chars = "123456789".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 9; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		String randomFreegameOfferName = sb.toString();
		log.debug("Random String has created for FreeGameOffer");
		return  "Auto"+randomFreegameOfferName;
	}

	/**
	 * This method gives current time in "yyyy-MM-dd' 'HH:mm:ss'.000Z'" format
	 * @return
	 */
	public String getCurrentTimeWithDateAndTime()  {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss'.000Z'");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, +1);
		return formatter.format(cal.getTime());
	}

	/**
	 * Read the currency list from excel sheet
	 * @param start
	 * @param end
	 * @return list of currency object
	 */
	public List<Currency> getCurrencyListFromExel(int start, int end)
	{
		// creating list of currency
		ArrayList<Currency> currencylist=new ArrayList<>();

		ExcelDataPoolManager excelpoolmanager = new ExcelDataPoolManager();
		String testDataExcelPath=TestPropReader.getInstance().getProperty("TestData_Excel_Path");

		int totalRows;
		try {
			totalRows = excelpoolmanager.rowCount(testDataExcelPath, "CurrencyList");

			for(int count=1;count<totalRows;count++ )
			{
				Map<String, String> currentRow = null;
				currentRow	= excelpoolmanager.readExcelByRow(testDataExcelPath, "CurrencyList",count);
				
				if(count>=start&& count<=end){
					Currency currency=new Currency();
					currency.setCurrencyFormat(currentRow.get("DisplayFormat").trim());
					int id=(int)Double.parseDouble(currentRow.get(Constant.ID));
					currency.setCurrencyID(String.valueOf(id));
					//removing special charcter read from excel cell
					String strISOCode=currentRow.get(Constant.ISOCODE).trim().replace("\u00A0", "");
					currency.setIsoCode(strISOCode);
					String strISOName=currentRow.get(Constant.ISONAME).trim().replace("\u00A0", "");
					currency.setIsoName(strISOName);
					
					currency.setStatus("Open");
					if(currentRow.get("Multiplier").trim()==null)
						currency.setCurrencyMultiplier("1");
					else
					{
						String strMultiplier=currentRow.get("Multiplier").trim().replaceAll("[^0-9.]", "");
						if(strMultiplier.trim().equals(""))
							currency.setCurrencyMultiplier("1");
						else
						{
							int intMultiplier=(int)Double.parseDouble(strMultiplier);
							currency.setCurrencyMultiplier(String.valueOf(intMultiplier));
						}
					}
					currencylist.add(currency);

				}
			}
		} catch (IOException ioexception) {
			log.error(ioexception.getMessage(),ioexception);
		}
		catch(Exception exception)
		{
			log.error(exception.getMessage(),exception);
		}
		return currencylist;
	}

	/**
	 * This method update the user balance depending upon the execution environment
	 * @param userName
	 * @param balance
	 * @return
	 */
	public boolean updateUserBalance(String userName,double balance)
	{
		boolean isBalnceUpdated=false;
		String envName=TestPropReader.getInstance().getProperty("EnvironmentName");
		RestAPILibrary apiObj = new RestAPILibrary();
		if(envName==null || envName.equalsIgnoreCase("Bluemesa"))
		{
			DataBaseFunctions dbobject = new DataBaseFunctions(TestPropReader.getInstance().getProperty("ServerName"),
					TestPropReader.getInstance().getProperty("dataBaseName"),
					TestPropReader.getInstance().getProperty("serverIp"), TestPropReader.getInstance().getProperty("port"),
					TestPropReader.getInstance().getProperty("serverID"));

			dbobject.updateBalanceUsingDatabase(userName, balance);

		}
		else
			apiObj.updateBalanceUsingAPI(userName,balance);
		return isBalnceUpdated;
	}

	/**
	 * Delete the test data file from test server
	 * @param mid
	 * @param cid
	 * @param cidDesktop
	 */
	public void deleteBluemesaTestDataFiles(int mid, int cid){
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
	public void deleteBluemesaTestDataFiles(int mid,int cid,List<String> copiedFiles){
		Util glbCfnLib = new Util();

		String folderPath= "smb://" + TestPropReader.getInstance().getProperty("Casinoas1IP").trim()+ "/c$/MGS_TestData/";
		glbCfnLib.deleteFiles(folderPath,copiedFiles);
	}
	
	/**
	 * method will create the random user for Reg Markets
	 * @return String
	 */

	public String randomUserCreationForRegMarkets(String userName, String regMarket,int envId,int productId,String currencyIsoCode ,int marketTypeID)
	{
		String randomUserName=null;
		RestAPILibrary apiobj = new RestAPILibrary();
		try{
			if( TestPropReader.getInstance().getProperty("EnvironmentName")==null ||
					TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
			{
			TestPropReader testPropReader = TestPropReader.getInstance();
			testPropReader.setProperty("ETCHostURL","https://etcapi.mgsops.net");
			System.out.println("LAtest Version :: "+apiobj.createRegMarketUser( userName,  regMarket, envId, productId));
			}
			else
			{
				apiobj.createRegMarketUserInAxiom ( userName,currencyIsoCode,regMarket,marketTypeID,productId );
			}
		}
		catch(Exception e)
		{
			log.error("Fail to create user",e);
		}
		return randomUserName;
	}
	
	/**
	 * This method copy testdata file from Testdata folder to server o specified environment for Reg markets
	 * @param mid
	 * @param cid
	 * @param file
	 * @param userName
	 * @param copiedFiles
	 * @return
	 */
	public boolean copyFilesToTestServerForRegMarket(int mid,int cid,File file, String userName, String regMarket,int envId,int productId,String currencyIsoCode ,int marketTypeID,List<String> copiedFiles) {
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
				TestPropReader testPropReader= TestPropReader.getInstance();
				testPropReader.setProperty("ETCHostURL","https://etcapi.mgsops.net");
				apiobj.createRegMarketUser( userName,  regMarket, envId, productId);

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
		else if(TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Axiom"))
		{
			try
			{
				apiobj.createRegMarketUserInAxiom ( userName,currencyIsoCode,regMarket,marketTypeID,productId );
				gcfnlib.changePlayerName(userName, file.getPath());
				apiobj.copyTestData( file.getPath(),fileName);
				copiedFiles.add(fileName);
				isTestDataCopied=true;
			}
			catch(Exception e)
			{
				isTestDataCopied=false;
				log.error("Exception while copying test data file",e);
			}
		} 

		return isTestDataCopied;
	}
	
	/**
	 * This function generate the random String for Quick Fire Games
	 * @return String
	 */
	public String quickFireRandomStringgenerator()
	{

		char[] chars = "0123456789".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 2; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		String output = sb.toString();
		log.debug("Random String has created for username");
		return "token" + output;
	}
	
	/**
	 * method will create the random user for Reg Markets
	 * @return String
	 */

	public String quickfireGamesCreateURL(String userName, String gameVersion,int moduleId,int clientId,String lobbyname,String host )
	{
		String randomUserName=null;
		RestAPILibrary apiobj = new RestAPILibrary();
		try{
			if( TestPropReader.getInstance().getProperty("EnvironmentName")==null ||TestPropReader.getInstance().getProperty("EnvironmentName").equalsIgnoreCase("Bluemesa"))
			{
			TestPropReader testPropReader = TestPropReader.getInstance();
			testPropReader.setProperty("ETCHostURL","https://etcapi.mgsops.net");
			//System.out.println("LAtest Version :: "+apiobj.createRegMarketUser( userName,  regMarket, envId, productId));
			}
			else
			{
				apiobj.quickFireAxiomCreateURL( userName,gameVersion,moduleId,clientId,lobbyname,host);	
			}
		}
		catch(Exception e)
		{
			log.error("Fail to create user",e);
		}
		return randomUserName;
	}
	
	
	/**
	 * method will crate the user in database
	 * @param userName
	 * @return
	 */
	public boolean migrateUser(String userName)
	{
		
		UserMetaData userInfo=new UserMetaData();
		
		RestAPILibrary apiobj = new RestAPILibrary();
		
		userInfo=apiobj.getUserMetaDataFromAxiom(userName);
		String userId=userInfo.getUserID();

		
		String sqlQuery="USE casino\r\n" + 
				"GO\r\n" + 
				"\r\n" + 
				"PRINT N'Turning OFF SystemSettingID = 12 a.k.a. VPB - Enable for Sports Books';\r\n" + 
				"update top (1) tb_SystemSetting set systemsettingintvalue =0 where SystemSettingID = 12;    \r\n" + 
				"\r\n" + 
				"GO\r\n" + 
				"\r\n" + 
				"DECLARE @UserID int = UserIDNum \r\n" + 
				"\r\n" + 
				"DECLARE @external BIT = (select dbo.fn_IsExternalPlay (NULL, @UserID))\r\n" + 
				"\r\n" + 
				"IF (@external = 0)\r\n" + 
				"BEGIN\r\n" + 
				"  EXEC Balance.pr_MigrateUserToNoBonus @UserID = @UserID -- int\r\n" + 
				"END\r\n" + 
				"ELSE\r\n" + 
				"BEGIN\r\n" + 
				"  EXEC Balance.pr_MigrateUserToExternalBalance @UserID = @UserID -- int\r\n" + 
				"END";
		try{
		String newSqlQuery=	sqlQuery.replaceAll("UserIDNum", userId);
		
		String zipFilePath= "./"+userId+".zip";
		
		File f = new File(zipFilePath);
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(f));
		ZipEntry e = new ZipEntry("query.sql");
		out.putNextEntry(e);

		byte[] data = newSqlQuery.toString().getBytes();
		out.write(data, 0, data.length);
		out.closeEntry();

		out.close();
		
		apiobj.uploadGamePresetZip(zipFilePath, null, "x64");
		
		//System.out.println(newSqlQuery);
		}catch(Exception e)
		{
			
			log.error("Fail to migrate user",e);
			return false;
		}
		return true;
	}
	/**
	 * @Overload
	 * This method update the user balance depending upon the execution environment
	 * @param userName
	 * @param balance
	 * @return
	 */
	public boolean updateUserBalance(String userName,String balance)
	{
		boolean isBalnceUpdated=false;
		String envName=TestPropReader.getInstance().getProperty("EnvironmentName");
		RestAPILibrary apiObj = new RestAPILibrary();
		if(envName==null || envName.equalsIgnoreCase("Bluemesa"))
		{
			DataBaseFunctions dbobject = new DataBaseFunctions(TestPropReader.getInstance().getProperty("ServerName"),
					TestPropReader.getInstance().getProperty("dataBaseName"),
					TestPropReader.getInstance().getProperty("serverIp"), TestPropReader.getInstance().getProperty("port"),
					TestPropReader.getInstance().getProperty("serverID"));

			dbobject.updateBalanceUsingDatabase(userName, balance);

		}
		else
			isBalnceUpdated=apiObj.updateBalanceUsingAPI(userName,balance);
				
		return isBalnceUpdated;
	}

}
