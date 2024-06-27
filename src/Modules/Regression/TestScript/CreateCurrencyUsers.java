package Modules.Regression.TestScript;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.zensar.automation.framework.library.UnableToLoadPropertiesException;
import com.zensar.automation.library.DataBaseFunctions;
import com.zensar.automation.library.TestPropReader;
import com.zensar.automation.model.Currency;

/**
 * This is the class which creates 1 to 5 users for all the currencies mentioned currencies sheet.
 * Environment ID's-
 *  Dev - 1257
	QA - 2668
	QA 2 - 1658
	Dev 2 - 1657
 */

public class CreateCurrencyUsers {

	// Give exact path of excel sheet
	public static String path = System.getProperty("user.dir");
	Connection conn = null;
	static String gameName;
	public static Logger log = Logger.getLogger(CreateCurrencyUsers.class.getName());

	public static void main(String[] args) throws IOException {
		
		String gameName=null;
		
		String strEnvID=null;
		int envID=0;
		
		try{
			gameName=args[0].trim();
			
			strEnvID=args[1].trim();
			envID=Integer.parseInt(strEnvID);
			
		
		
		System.setProperty("logfilename",gameName+"/Selenium");
		TestPropReader.getInstance().loadAllProperties(gameName, envID);
		CreateCurrencyUsers createCurrencyUsers = new CreateCurrencyUsers();
		createCurrencyUsers.procceCurrency();
		}catch(ArrayIndexOutOfBoundsException e){
			
			System.out.println("Mandatory parameter game Name, environmentID  is missing");
			e.printStackTrace();
		}catch(NumberFormatException e){
			System.out.println("environmentID is missing");
			e.printStackTrace();
		}catch(UnableToLoadPropertiesException e)
		{
			System.out.println("unable to load test property file");
		}
	}
	
	public void procceCurrency() throws IOException  {

		
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
				
				//creating user in to the database with new username
				CreateUser(userName, CurrencyID, 0,TestPropReader.getInstance().getProperty("serverID").toString());
				System.out.println("The New username is ==" + userName+" created");
				}
		
		
		closeConnection();
	}
	
	public void closeConnection() {
		if (conn != null) {
			try {
				conn.close();
				log.debug("Connection closed");
			} catch (SQLException e) {
				try {
					conn.close();
				} catch (SQLException e1) {
					log.error("Exception while closing the connection",e1);
				}
			}
		}
	}

	/*public void getDatabaseConnection(){
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

			//STEP 3: Open a connection
			//System.out.println("Connecting to database...");
			String USER = TestPropReader.getInstance().getProperty("DBUser");
			String PASS = TestPropReader.getInstance().getProperty("DBpass");
			//String URL = "jdbc:sqlserver://"+serverIp+"\\inst2;EncryptionMethod=noEncryption";
			String URL= "jdbc:sqlserver://"+serverIp+"\\inst2;EncryptionMethod=noEncryption;DatabaseName="+dataBaseName;

			//String URL="jdbc:sqlserver://"+serverIp+":"+port+";DatabaseName="+dataBaseName;
			log.info("URl for data base"+URL);
			//conn = DriverManager.
			conn = DriverManager.getConnection(URL,USER,PASS);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	*/
	
	
	
	public Connection getConnection() {

		if (conn == null) {

			try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				conn = DriverManager.getConnection("jdbc:sqlserver://"
						+TestPropReader.getInstance().getProperty("serverIp")
						+":"+TestPropReader.getInstance().getProperty("port")
						+";DatabaseName="+TestPropReader.getInstance().getProperty("dataBaseName"), 
						TestPropReader.getInstance().getProperty("DBUser"),
						TestPropReader.getInstance().getProperty("DBpass"));  
				
			} catch (ClassNotFoundException e) {
				log.error("ClassNotFoundException",e);
			} catch (SQLException e) {
				log.error("Exception while getting the connection",e);
			}
		}

		return conn;

	}

	public void CreateUser(String username, String currencyType, int usertype, String serverID) {

		Statement stmt = null;

		try {

			getConnection();
			
			stmt = conn.createStatement();

			String query = "IF NOT EXISTS(SELECT UserID FROM dbo.tb_UserAccount WHERE LoginName = '" + username + "')"
					+ "BEGIN " + "DECLARE " + " @rServerID int" + ", @rLoginName char(20)" + ", @rPassword char(20)"
					+ ", @rBalance int" + ", @rIdentifierOut char(36)" + ", @rUserType int" + ", @iCount int"
					+ ", @cLoginName char(20)" + ", @MachineIdentifier int" + ", @Currency int" +

					" exec sp_RegisterNewUser @ServerID = " + serverID + ", " + "@Currency = " + currencyType + ","
					+ "@UserType =" + usertype + ", " + "@LoginName = '" + username + "'," + "@Password ='test', "
					+ "@Email ='cartman@southpark.com', " + "@FN ='Eric', " + "@LN ='Cartman',"
					+ " @WrkTel ='5698659', " + "@HomeTel ='4464654', " + "@Fax ='4646666',"
					+ "@Addr1 ='25 South Park Street', " + " @Addr2 ='South Park', " + "@City ='South Park',"
					+ "@Country ='USA', " + "@Province ='Colarado', " + "@Zip ='1234'," + "@IDNumber ='6901205121085', "
					+ "@Occupation ='Scholar'," + "@Sex ='M', " + "@DOB ='1969/01/20', " + "@Alias ='Fat boy',"
					+ "@IdentifierIn =null," + "@EventID =29, " + "@ModuleID =24, " + "@ChangeAmt =100000,"
					+ "@CreditLimit =0," + "@MachineIdentifier=0," + "@HDIdentifier =null," + "@Creator = 1, "
					+ "@IdentifierOut =@rIdentifierOut output" + " END";

			stmt.executeUpdate(query);
			
			

		} catch (SQLException se) {
			log.error("SQLException",se);
		} catch (Exception e) {
			log.error("Exception",e);
		} finally {
			
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
				log.error("SQLException while closing statement",se2);
			}
		} 
		
	}
}
