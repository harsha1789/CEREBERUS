package com.zensar.automation.library;

import java.io.File;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.zensar.automation.framework.utils.Util;
import com.zensar.automation.model.Currency;




/*This class will paste  test data for all the currency users into Casino server
 * 
 * To run this script steps are
 * paste the freespin testdata at Desktop_Regression_CurrencySymbol.testdata file
 * change the path TestEnv.properties file (TestDataFileName=BreakAway_96.veyrongame)
 * To paste data for desktop pass Desktop as argument while running as a java application.
 * 
 * */

public class CreateTestData {

	
	public static void main(String[] args)
	{
		String destFile;
		Logger log=Logger.getLogger(CreateTestData.class.getName());
		log.debug("test");
		String xmlFilePath=null;
		String gameName=null;
		String mid=null,cid=null;
		String strEnvID=null;
		int envID=0;
		try{
			gameName=args[1].trim();
			
			strEnvID=args[2].trim();
			envID=Integer.parseInt(strEnvID);
			mid=args[3].trim();
			cid=args[4].trim();
		}catch(ArrayIndexOutOfBoundsException e){
			
			log.debug("Mandatory parameter game Name, environmentID, Module id or client id  is missing");
			log.error(e.getMessage(),e);
		}catch(NumberFormatException e){
			log.debug("environmentID is missing");
			log.error(e.getMessage(),e);
		}
			
		if(gameName != null && envID !=0)
		{
		try{
			System.setProperty("logfilename",gameName+"/Selenium");
			TestPropReader.getInstance().loadAllProperties(gameName, envID);
			// condition to check the for which client need to paste the test data
			 if("Mobile".equalsIgnoreCase((args.length)!=0 ? args[0]: ""))
			 {
				 xmlFilePath = TestPropReader.getInstance().getProperty("MobileCurrencyTestDataPath");
				 
			 }
			 else
			 {
				 xmlFilePath = TestPropReader.getInstance().getProperty("CurrencyTestDataPath");
				 
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
		//Fetching all currencies from data base
		ArrayList<Currency> currencyList=dbobject.getCurrencyData();
		
		int currencysize=currencyList.size();
		if(currencysize==0)
		{
				log.error("Error While reading the currencies from datbase");
		}
		// Read ISO name from database as use as User name 
		
		for(Currency currency :currencyList)
		{
			
			
			String CurrencyID = currency.getCurrencyID();
			gameName=TestPropReader.getInstance().getProperty("GameName");
			String capGameName = gameName.replaceAll("[a-z0-9]", "").trim();
			String userName = "Zen" + capGameName + "_" + currency.getIsoCode().trim();
			log.debug("The New username is ==" + userName);
			
			/*String userName = "Zen_"+currency.getIsoCode().trim();
			log.debug("The New username is ==" + userName);*/
			
			//creating user in to the database with new username
			dbobject.createUser(userName, CurrencyID, 0);
			
			// Read the test data file 
	
			destFile = "smb://" + TestPropReader.getInstance().getProperty("Casinoas1IP").trim()
				+ "/c$/MGS_TestData/" + TestPropReader.getInstance().getProperty("TestDataFileName").trim()+"_"+mid+"_"+cid+"_"+userName + ".testdata";

			// --------Update the Xml File of test Data with currency user name----------//
			gcfnlib.changePlayerName(userName, xmlFilePath);

				// -----Copy the test  Data to The CasinoAs1 Server-----//
				gcfnlib.copyFolder(sourceFile, destFile);
				System.out.println("created test data for : "+ userName);
			}
			
		
		log.debug("Test data creation is compeleted .... ");
		
	}catch(Exception e)
		{
		log.error(e.getMessage(), e);
		}
	}
		else
		{			System.out.println("Excution skipped as mandatory parameter missing, plase provide game name and env ID  parametes");

			}
	}
}
