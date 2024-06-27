package com.zensar.automation.library;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.zensar.automation.model.Currency;




/*This class will paste  test data for all the currency users into Casino server
 * 
 * To run this script steps are
 * paste the freespin testdata at Desktop_Regression_CurrencySymbol.testdata file
 * change the path TestEnv.properties file (TestDataFileName=BreakAway_96.veyrongame_10206_50300_)
 * To paste data for desktop pass Desktop as argument while running as a java application.
 * 
 * */

public class CreateUserInDB {

	
	public static void main(String[] args)
	{
		Logger log=Logger.getLogger(CreateUserInDB.class.getName());
		log.debug("test");
		String gameName=null;
		String strEnvID=null;
		int envID=0;
		try{
			gameName=args[0].trim();
			
			strEnvID=args[1].trim();
			envID=Integer.parseInt(strEnvID);
			
		}catch(ArrayIndexOutOfBoundsException e){
			
			log.debug("Mandatory parameter game Name, environmentID, is missing");
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
			
			
			String currencyID = currency.getCurrencyID();
			gameName=TestPropReader.getInstance().getProperty("GameName");
			String capGameName = gameName.replaceAll("[a-z0-9]", "").trim();
			String userName = "Zen" + capGameName + "_" + currency.getIsoCode().trim();
			log.debug("The New username is ==" + userName);
			
			//creating user in to the database with new username
			dbobject.createUser(userName, currencyID, 0);
			}
		
		log.debug("Test data creation is compeleted .... ");
		
	}catch(Exception e)
		{
			log.error(e.getMessage(),e);
		}
	}
		else
		{			System.out.println("Excution skipped as mandatory parameter missing, plase provide game name and env ID  parametes");

			}
	}
}
