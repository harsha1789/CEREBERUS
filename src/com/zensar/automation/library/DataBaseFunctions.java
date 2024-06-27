package com.zensar.automation.library;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.model.Currency;
/**
 *This class impliments casino database realated function 
 *such as get Databse connection,craete user in databse  
 *
 */

public class DataBaseFunctions {

	Logger log=Logger.getLogger(DataBaseFunctions.class.getName());

	String serverName;
	String dataBaseName;
	String serverIp;
	String serverID;
	String port;


	public DataBaseFunctions(String serverName,String dataBaseName,String serverIp,String port,String serverID){

		this.serverName=serverName;
		this.dataBaseName=dataBaseName;
		this.serverIp=serverIp;
		this.port=port;
		this.serverID=serverID;
	}

	/**
	 * Method create the database connection with configured environment
	 */
	public Connection getDatabaseConnection(){
		Connection con =null;
		try{
			//STEP 3: Open a connection
			String dbUser = TestPropReader.getInstance().getProperty("DBUser");
			String dbPassword = TestPropReader.getInstance().getProperty("DBpass");
			String url= "jdbc:sqlserver://"+serverIp+"\\inst2;EncryptionMethod=noEncryption;DatabaseName="+dataBaseName;
			log.info("URl for data base"+url);
			con= DriverManager.getConnection(url,dbUser,dbPassword);
		} catch (SQLException e) {
			log.error(e.getMessage(),e);
		}

		return con;
	}

	/**
	 * This method will create user in databse
	 * @param username
	 * @param currency
	 * @param usertype
	 */
	public void createUser(String username, String currency, int usertype,String... strServerIDParam) {

		//STEP 4: Execute a query
		log.debug("Creating statement...");
		String strServerID=null;
		if(strServerIDParam==null || strServerIDParam.length==0) 
		{
			strServerID=serverID;
		}
		else
		{
			strServerID=strServerIDParam[0];
		}

		try(Connection conn= getDatabaseConnection();
				Statement stmt = conn.createStatement();){
			String query=
					"IF NOT EXISTS(SELECT UserID FROM dbo.tb_UserAccount WHERE LoginName = '"+username+"')"+
							"BEGIN "+
							"DECLARE "+ 
							" @rServerID int"+
							", @rLoginName char(20)"+
							", @rPassword char(20)"+
							", @rBalance int"+
							", @rIdentifierOut char(36)"+
							", @rUserType int"+
							", @iCount int"+
							", @cLoginName char(20)"+
							", @MachineIdentifier int"+
							", @Currency int"+ 

" exec sp_RegisterNewUser @ServerID = "+strServerID+", "+
"@Currency = "+currency+","+
"@UserType ="+usertype+", "+
"@LoginName = '"+username+"',"+
"@Password ='test', "+
"@Email ='cartman@southpark.com', "+
"@FN ='Eric', "+
"@LN ='Cartman',"+
" @WrkTel ='5698659', "+
"@HomeTel ='4464654', "+
"@Fax ='4646666',"+
"@Addr1 ='25 South Park Street', "+
" @Addr2 ='South Park', "+
"@City ='South Park',"+
"@Country ='USA', "+
"@Province ='Colarado', "+
"@Zip ='1234',"+
"@IDNumber ='6901205121085', "+ 
"@Occupation ='Scholar',"+
"@Sex ='M', "+
"@DOB ='1969/01/20', "+
"@Alias ='Fat boy',"+
"@IdentifierIn =null,"+
"@EventID =29, "+
"@ModuleID =24, "+
"@ChangeAmt =100000,"+
"@CreditLimit =0,"+
"@MachineIdentifier=0,"+
"@HDIdentifier =null,"+
"@Creator = 1, "+
"@IdentifierOut =@rIdentifierOut output"+
" END";

			stmt.executeUpdate(query);
			log.debug("Player inserted");
		}catch(SQLException se){
			log.error("error while creating user", se);
		}catch(Exception e){
			e.printStackTrace();
			log.error("error while creating user", e);
		}//end try
	}//end main



	public int assignFreeGameWithExpiry(String username, String gameName)
	{
		int gamesRewarded = 0;
		String newSAdate=null;
		String userID = null;
		String offerId = null;



		//STEP 4: Execute a query
		try(Connection conn= getDatabaseConnection();
				Statement stmt = conn.createStatement())
		{
			String dbQuery="Select * from tb_UserAccount where LoginName = '"+username+"'";

			try(ResultSet rs= stmt.executeQuery(dbQuery))
			{
				while(rs.next())
				{
					userID = rs.getString("UserID");
				}
			}
			String freeGameOfferQuery="select * from dbo.tb_FreeGameOffer where Description like '%"+gameName+"%'";
			try(ResultSet rs1= stmt.executeQuery(freeGameOfferQuery))
			{
				while(rs1.next())
				{
					offerId = rs1.getString("OfferId");		


					break;
				}
			}
			String getOfferQuery="select * from dbo.tb_FreeGameOffer where Description like '%"+gameName+"%'";
			try(ResultSet rs2=stmt.executeQuery(getOfferQuery))
			{
				while(rs2.next())
				{
					gamesRewarded = rs2.getInt("NrGamesToAward");
					java.sql.Date startDate= rs2.getDate("StartDate");
					log.debug("StarDate from db= "+startDate);
					Time starttime = rs2.getTime("StartDate");
					log.debug( "StartTime from db= "+starttime);
					java.sql.Date endDate= rs2.getDate("EndDate");
					log.debug("EndDate from db= "+endDate);
					Time endtime = rs2.getTime("EndDate");
					log.debug( "EndTime from db= "+endtime);
					log.debug("One Offer has="+gamesRewarded);
					break;
				}
			}

			try(PreparedStatement pstmt = conn.prepareStatement("UPDATE dbo.tb_FreeGameOffer SET EndDate=? WHERE OfferId=?");) 
			{ pstmt.setString(1,newSAdate);
			pstmt.setString(2,offerId);
			int rs = pstmt.executeUpdate(); 
			log.debug("update exicuted succesfully");
			}

			Thread.sleep(50000);
			String query=
					"DECLARE "+ 
							"@Now DateTime"+
							" SET "+
							"@Now = GETDATE()"+
							" exec dbo.pr_AddUserFreeGame @OfferID = '"+offerId+"' , "+
							"@UserID = '"+userID+"',"+
							"@UserOfferStartDate =  @Now";

			for(int i=1;i<=5; i++)
				stmt.executeUpdate(query);
			Thread.sleep(30000);
			log.debug("Free Game Offer is assigned to User Account");
		}catch(SQLException sqlException) {
			log.debug(sqlException.getMessage(),sqlException);
		} catch (InterruptedException e) {
			log.debug(e.getMessage(),e);
		}

		return gamesRewarded;
	}


	public int GTR_assignFreeGamenew(String username, String gameName){
		int games_rewarded = 0;
		try{
			String userID = null;
			String offerId = null;

			//Step 1 Establish a Connection from the data base

			//STEP 4: Execute a query
			try(
					Connection conn =getDatabaseConnection(); 
					Statement stmt = conn.createStatement()){
				String dbQuery="Select * from tb_UserAccount where LoginName = '"+username+"'";

				try(ResultSet rs= stmt.executeQuery(dbQuery)){
					while(rs.next())
					{
						log.debug(rs.getString("UserID"));
						userID = rs.getString("UserID");
					}
				}
				String getOfferQuery="select * from dbo.tb_FreeGameOffer where Description like '%"+gameName+"%'";
				try(ResultSet rs1= stmt.executeQuery(getOfferQuery)){
					while(rs1.next())
					{
						log.debug(rs1.getString("OfferId"));
						offerId = rs1.getString("OfferId");
						//String gameToAward=rs1.getString("NrGamesToAward");
						break;
					}
					String getRewardsQuery="select * from dbo.tb_FreeGameOffer where Description like '%"+gameName+"%'";
					try(ResultSet rs2=stmt.executeQuery(getRewardsQuery))
					{
						while(rs2.next())
						{
							games_rewarded = rs2.getInt("NrGamesToAward");
							log.debug("One Offer has="+games_rewarded);
							break;
						}
					}
				}

				String query=
						"DECLARE "+ 
								"@Now DateTime"+
								" SET "+
								"@Now = GETDATE()"+
								" exec dbo.pr_AddUserFreeGame @OfferID = '"+offerId+"' , "+
								"@UserID = '"+userID+"',"+
								"@UserOfferStartDate =  @Now";

				for(int i=1;i<=5; i++)
					stmt.executeUpdate(query);
				//log.debug("Free Game Offer is assigned to User Account");
			}
		}catch(SQLException se){
			//Handle errors for JDBC
			//log.error("Error while assigning free games", se);
		}
		return games_rewarded;
	}


	public void assignFreeGame (String username, String gameName){

		int startindex=0;
		String newgamename=null;
		if(gameName.contains("Desktop"))
		{	
			//String Gamename=gameName.replace("Desktop", "");
			Pattern  str=Pattern.compile("Desktop");

			Matcher  substing=str.matcher(gameName);

			while(substing.find())
			{
				startindex=substing.start();
			}
			gameName=gameName.substring(0, startindex);
			log.debug("newgamename="+gameName);
		}

		//String Gamename=gameName.replace("Desktop", "");
		String userID = null;
		String offerId = null;
		//Step 1 Establish a Connection from the data base

		//STEP 4: Execute a query
		try(	Connection conn= getDatabaseConnection();
				Statement stmt = conn.createStatement()){
			String getUserQuery="Select * from tb_UserAccount where LoginName = '"+username+"'";

			try(ResultSet rs= stmt.executeQuery(getUserQuery)){
				while(rs.next())
				{
					userID = rs.getString("UserID");
					log.debug("User ID="+userID);
				}
			}
			String getFGOfferQuery="select * from dbo.tb_FreeGameOffer where Description like '%"+gameName+"%'";
			try(ResultSet rs1= stmt.executeQuery(getFGOfferQuery)){
				while(rs1.next())
				{
					String description =  rs1.getString("Description");
					if(description.equalsIgnoreCase(gameName+"DEF")){
						offerId = rs1.getString("OfferId");
						log.debug("OfferID Assing="+offerId);
						break;
					}
				}
			}

			String query=
					"DECLARE "+ 
							"@Now DateTime"+
							" SET "+
							"@Now = GETDATE()"+
							" exec dbo.pr_AddUserFreeGame @OfferID = '"+offerId+"' , "+
							"@UserID = '"+userID+"',"+
							"@UserOfferStartDate =  @Now";
			//assing free games as  2 offres per language
			for(int i=1;i<=95; i++)
				stmt.executeUpdate(query);
			log.debug("Free Game Offer is assigned to User Account");

		}catch(SQLException se){
			log.error("Error while assigning free games !!"+se.getMessage(), se);
		}catch(Exception e){
			log.error("Error while assigning free games ", e);
		}
	}

	public void updateBalance(String userName) {
		String currentUserID = null;
		// Step 1 Establish a Connection from the data base
		try (Connection conn = getDatabaseConnection(); Statement stmt = conn.createStatement();) {
			String getUserNameQuery = "Select * from tb_UserAccount where LoginName = '" + userName + "'";

			try (ResultSet rs = stmt.executeQuery(getUserNameQuery)) {

				while (rs.next()) {
					currentUserID = rs.getString("UserID");
				}

				stmt.executeUpdate("Update tb_usertoken set balance = 100000 where UserID = '" + currentUserID + "'");
			}

		} catch (SQLException se) {
			log.error(se.getMessage(), se);
		}
	}


	public void updateBalanceUsingDatabase(String userName, double balance) {

		String currentUserID = null;
		// Step 1 Establish a Connection from the data base
		try (Connection conn = getDatabaseConnection(); Statement stmt = conn.createStatement()) {
			String getUserNameQuery = "Select * from tb_UserAccount where LoginName = '" + userName + "'";

			try (ResultSet rs = stmt.executeQuery(getUserNameQuery)) {

				while (rs.next()) {
					currentUserID = rs.getString("UserID");
				}

				stmt.executeUpdate(
						"Update tb_usertoken set balance = " + balance + " where UserID = '" + currentUserID + "'");
			}

		} catch (SQLException se) {
			// Handle errors for JDBC
			log.error(se.getMessage(),se);
		}
	}



	/*Name: getCurrencyData
	 * parameter :no parameter
	 * return :ArrayList of currency object
	 * */

	public ArrayList<Currency> getCurrencyData()
	{
		// creating list of currency
		ArrayList<Currency> currencylist=new ArrayList<>();
		// Establish the connection
		try(Connection conn =getDatabaseConnection();
				Statement stmt=conn.createStatement())
		{
			// Reading the required value from the data base
			String dbQuery="Select tbc.ID,tbc.ISOCode,tbc.ISOName,tbc.DisplayFormat,tbs.TemplateName Multiplier from  tb_Currency tbc left outer join  tb_CurrencyTemplate tct on tbc.ID  = tct.CurrencyID left outer join tb_SettingTemplate tbs  on tct.TemplateID= tbs.TemplateID ";
			try(ResultSet rs=stmt.executeQuery(dbQuery))
			{
				while(rs.next())
				{
					Currency currency=new Currency();
					currency.setCurrencyFormat(rs.getString("DisplayFormat"));
					currency.setCurrencyID(rs.getString("ID"));
					currency.setIsoCode(rs.getString("ISOCode"));
					currency.setIsoName(rs.getString("ISOName"));
					currency.setStatus("Open");
					if(rs.getString("Multiplier")==null)
						currency.setCurrencyMultiplier("1");
					else
						currency.setCurrencyMultiplier(rs.getString("Multiplier").replaceAll("[^0-9]", ""));
					currencylist.add(currency);

				}
			}

		}catch(SQLException se){
			//Handle errors for JDBC
			log.error("Error while assigning free games", se);
		}

		return currencylist;
	}




	


	public ArrayList<Currency> getCurrencyData(int start, int end)
	{
		// creating list of currency
		ArrayList<Currency> currencylist=new ArrayList<>();
		int count=0;
		// Establish the connection

		//execute the query
		try(	Connection conn = getDatabaseConnection();
				Statement stmt=conn.createStatement())
		{
			// Reading the required value from the data base
			String query="Select tbc.ID,tbc.ISOCode,tbc.ISOName,tbc.DisplayFormat,tbs.TemplateName Multiplier from  tb_Currency tbc left outer join  tb_CurrencyTemplate tct on tbc.ID  = tct.CurrencyID left outer join tb_SettingTemplate tbs  on tct.TemplateID= tbs.TemplateID ";
			try(ResultSet rs=stmt.executeQuery(query))
			{
				while(rs.next())
				{

					if(count>=start&& count<=end){
						Currency currency=new Currency();
						currency.setCurrencyFormat(rs.getString("DisplayFormat"));
						currency.setCurrencyID(rs.getString("ID"));
						currency.setIsoCode(rs.getString("ISOCode"));
						currency.setIsoName(rs.getString("ISOName"));
						currency.setStatus("Open");
						if(rs.getString("Multiplier")==null)
							currency.setCurrencyMultiplier("1");
						else
							currency.setCurrencyMultiplier(rs.getString("Multiplier").replaceAll("[^0-9]", ""));
						currencylist.add(currency);

					}count++;
				}
			}
		}catch(SQLException se){
			log.error("Error while assigning free games", se);
		}
		return currencylist;
	}


	/*Get UserID of given user name
	 * */

	public String getUserID (String username){
		String userID = null;
		try(	Connection conn = getDatabaseConnection();
				Statement stmt=conn.createStatement())
		{
			//STEP 4: Execute a query
			String getUserIDQuery="Select * from tb_UserAccount where LoginName = '"+username+"'";

			try(ResultSet rs= stmt.executeQuery(getUserIDQuery)){
				while(rs.next())
				{
					userID = rs.getString("UserID");
				}
			}
		}
		catch(Exception e){
			//Handle errors for Class.forName
			log.error(e.getStackTrace());
		}
		return userID;
	}
	/**
	 * @overload
	 * updating the balance
	 * @param userName
	 * @param balance
	 */
	
	public void updateBalanceUsingDatabase(String userName, String balance) {

		String currentUserID = null;
		// Step 1 Establish a Connection from the data base
		try (Connection conn = getDatabaseConnection(); Statement stmt = conn.createStatement()) {
			String getUserNameQuery = "Select * from tb_UserAccount where LoginName = '" + userName + "'";

			try (ResultSet rs = stmt.executeQuery(getUserNameQuery)) {

				while (rs.next()) {
					currentUserID = rs.getString("UserID");
				}

				stmt.executeUpdate(
						"Update tb_usertoken set balance = " + balance + " where UserID = '" + currentUserID + "'");
			}

		} catch (SQLException se) {
			// Handle errors for JDBC
			log.error(se.getMessage(),se);
		}
	}
}
