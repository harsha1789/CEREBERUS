package Modules.Regression.TestScript;
import java.io.File;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.framework.utils.ExcelDataPoolManager;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop_CS;
import net.lightbody.bmp.BrowserMobProxyServer;

public class Desktop_Sanity_TestSuit
{
	public ExcelDataPoolManager excelpoolmanager= new ExcelDataPoolManager();
	public String path=System.getProperty("user.dir");
	public WebDriver webdriver;		
	public BrowserMobProxyServer proxy;
	public String classname= this.getClass().getSimpleName();
	public String mstrTC_Name,mstrTC_Desc,mstrModuleName;
	public String obj=null;
	public boolean error;
	WebDriverWait Wait;
	public boolean bgImage;
	public String gametitleexcel;
	public String testcaseName;
	public String filePath;
	public String startTime;
	public String opacityValue;
	public int mintDetailCount;
	public int mintSubStepNo;
	public double credit=0.0;
	public int mintPassed;
	public int mintFailed;
	public int mintWarnings;
	public int mintStepNo;
	public String Status;
	public String Browsername;
	public String GameDesktopName;
	public String GameTitle;
	public int paylinesverification;
	public boolean GameHelp;
	public WebElement GameverifyTime;
	public int totalWin;
	Map<String, String> XpathMap;
	public String verifycredit;
	public String SpinVisible;
	public String oldbalance;
	public String newbalance;
	public String url;
	public String userName;
	public String password;
	public String applicationName;
	public String Framework;
	public String GameName;
	public double minbet1=0.25;
	public boolean isCSgame ;
	public String image;
	public  String imagepath=path+File.separator+"src"+File.separator+"Modules"+File.separator+"Regression"+File.separator+"TestData"+File.separator+"Images"+File.separator+"BaseScene";
	public String xmlFilePath= System.getProperty("user.dir")+"\\src\\Modules\\Regression\\TestData\\GameData\\"+classname+".testdata";
	public File sourceFile= new File(xmlFilePath);
    public File destFile = new File("\\\\"+TestPropReader.getInstance().getProperty("Casinoas1IP")+"\\c$\\MGS_TestData\\"+classname + "_" + userName +".testdata");
	
	public double winamt=0.0;
	public double maxbet1=2.50;
	/**
	 * @throws Exception
	 */
	public void script() throws Exception{
		Desktop_HTML_Report sanitysuit=new Desktop_HTML_Report(webdriver,Browsername,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,GameName);
		CFNLibrary_Desktop cfnlib=null;

		if(Framework.equalsIgnoreCase("PN")){
			cfnlib=new CFNLibrary_Desktop(webdriver,proxy,sanitysuit, GameName);

		}else{
			cfnlib=new CFNLibrary_Desktop_CS(webdriver,proxy,sanitysuit,GameName);
			isCSgame=true;
		}

		try{
			

			//-------------------Getting webdriver of proper test site---------------------//
			// Step 1
			//open lobby and verify lucky twins name

			if(webdriver!=null)
			{	

				String appurl=cfnlib.XpathMap.get("ApplicationURL");
				obj=cfnlib.func_navigate(appurl);

				if(obj!=null)
				{
					sanitysuit.detailsAppend("Open browser and Enter game URL in address bar and click Enter", "Island Paradise Lobby should be displayed", "Island Paradise lobby is displayed", "Pass");
				}
				else
				{
					sanitysuit.detailsAppend("Open browser and Enter Game URL in address bar and click Enter", "Island Paradise lobby should be displayed", "Island paradise is not displayed", "Fail");	
				}
				String playername=cfnlib.XpathMap.get("playername");
				GameTitle=cfnlib.Func_LoginBaseScene(playername,Constant.PASSWORD);
				if(GameTitle.trim()!=null)
				{
					System.out.println("game title from excel "+gametitleexcel);
					sanitysuit.detailsAppend("Check that user is able to login with username and password and  Title verified ", "User should be logged in successfully and "+GameName+" Game should be launched ", "Logged in succesfully and "+GameName+" is launched. ", "Pass");
				}
				else
				{
					sanitysuit.detailsAppend("Check that user is not able to login with username and password", "User should be logged in successfully and "+GameName+" Game should be launched ", "Logged in succesfully and "+GameName+" is not launched. ", "Fail");

				}	
				Thread.sleep(500);
				if(isCSgame){                                                                                                                                                                                                                           
					String gamelogo=cfnlib.gamelogo();   
					if(gamelogo!=null)
					{
						sanitysuit.detailsAppend("Verify that the application should display Logo and game name", "Game logo and game name with base scene should display", "Game logo is displaying "+GameName+" ", "pass");                     
					}
					else                                                                                                                                                                                                                                     
					{                                                                                                                                                                                                                                    
						sanitysuit.detailsAppend("Verify that the application should display Logo and game name", "Game logo and game name with base scene should display", "Game logo and game name is not  displaying "+GameName+"", "fail");   
					} 
				}                                                                                                                                                                                                                                   
				else  
				{
					bgImage=cfnlib.customeverifyimage(imagepath+File.separator+"logo4.png");                                                                                                                                                                
					if(bgImage)                                                                                                                                                                                                                             
					{                                                                                                                                                                                                                                    
						sanitysuit.detailsAppend("Verify that the application should display Logo and game name", "Game logo and game name with base scene should display", "Game logo is displaying "+GameName+" ", "pass");                     
					}                                                                                                                                                                                                                                    
					else                                                                                                                                                                                                                                     
					{                                                                                                                                                                                                                                    
						sanitysuit.detailsAppend("Verify that the application should display Logo and game name", "Game logo and game name with base scene should display", "Game logo and game name is not  displaying "+GameName+"", "fail");   
					}
				}
				
				cfnlib.verifyInGameMenuIcons(sanitysuit);
				// verify balance     
				verifycredit=cfnlib.verifyCredit();                                                                                                                                                                                         
				Double betvalue=cfnlib.GetBetAmt();
				if(verifycredit!=null)                                                                                                                                                                                                      
				{                                                                                                                                                                                                                           
					sanitysuit.detailsAppend("credits and bet is displaying on base screen  ", "Credits and bet must display on the screen  ", "Credit amount :"+verifycredit+ "and bet :"+betvalue+" is visible on screen", "pass");                                                  
				}                                                                                                                                                                                                                           
				else                                                                                                                                                                                                                        
				{                                                                                                                                                                                                                           
					sanitysuit.detailsAppend("credits and bet is displaying on base screen  ", "Credits and bet must display on the screen  ", "Credit amount :"+verifycredit+ "and bet :"+betvalue+" is not visible on the screen", "fail");                                          
				}                                                                                                                                                                                                                           
				/*Verify that on spin ,stop button should displayed when quick spin is disabled*/ 
				cfnlib.settingsOpen();
				String opacity=cfnlib.quickSpinStatus();
				if(opacity.equals("0.5"))
				{   
					Thread.sleep(3000);
					sanitysuit.detailsAppend("Verify that Quick spin is disabled when game launch on first time", "When game is launched first time quick spin must be in disabled state as default", "On launching the game  first time quick spin is  in disabled state as default"+GameName+" ", "pass");
					cfnlib.SettingsToBasescen();
					if(isCSgame){
						bgImage=cfnlib.customeverifyimage("STOP");
					}
					else
					{
						cfnlib.spinclick();	
						bgImage=cfnlib.customeverifyimage(imagepath+File.separator+"Desktop_Stop.png");
					}
					if(bgImage)
					{
						sanitysuit.detailsAppend("Verify that when quick spin is disabled and after clicking on spin button it should change the logo from spin to stop logo ", "During the quick spin disabled, spin button logo should replace with stop button ", "During the quick spin disabled ,spin button logo is replacing with stop button ", "pass");
					}
					else
					{
						sanitysuit.detailsAppend("Verify that when quick spin is disabled and after clicking on spin button it should change the logo from spin to stop logo ", "During the quick spin disabled, spin button logo should replace with stop button ", "During the quick spin disabled ,spin button logo is not replacing with stop button ", "fail");
					}
				}
				/*Verify the stop button should not be displayed when quick spin is enabled */
				cfnlib.winClick();
				cfnlib.settingsOpen();
				cfnlib.QuickSpinclick();
				String opacity1=cfnlib.quickSpinStatus();
				Thread.sleep(3000);
				if(opacity1.equals("1"))
				{
					sanitysuit.detailsAppend("Verify that clicking on quick spin toggle it must get enable" ," Clicking on quick spin toggle it must get  enable ", "Clicking on quick spin toggle button ,Its getting enable", "pass");
				}
				else
				{
					sanitysuit.detailsAppend("Verify that clicking on quick spin toggle it must get enable" ," Clicking on quick spin toggle it must get  enable ", "Clicking on quick spin toggle button ,Its not getting enable", "fail");
				}	
				cfnlib.SettingsToBasescen();

				Double verifycredit=Double.parseDouble(cfnlib.verifyCredit().replace(",", ""));
				Double betamt=cfnlib.GetBetAmt();
				System.out.println("verify credits : "+verifycredit);
				if(isCSgame){
					bgImage=cfnlib.customeverifyimage("SPIN");
				}
				else
				{
					cfnlib.spinclick();	
					bgImage=cfnlib.customeverifyimage(imagepath+File.separator+"Desktop_Stop.png");
				}
				if(bgImage)
				{
					sanitysuit.detailsAppend("Verify that when quick spin is enabled and on spin, spin button is not converting to stop Button" ,"when quick spin is enabled and on spin, spin button must  converti to stop Button", "when quick spin is enabled and on spin, spin button is not converting to stop Button", "pass");
				}
				else
				{
					sanitysuit.detailsAppend("Verify that when quick spin is enabled and on spin, spin is not converting to stop Button" ,"when quick spin is enabled and on spin, spin must  converti to stop Button", "when quick spin is enabled and on spin, spin button is converting to stop Button", "fail");
				}
				cfnlib.waitTillStop();
				cfnlib.winClick();
				Thread.sleep(3000);
				Double verifycredit1=Double.parseDouble(cfnlib.verifyCredit().replace(",", ""));
				System.out.println("double amount: "+verifycredit1);
				Double winamount=Double.parseDouble(cfnlib.GetWinAmt().replace(",", ""));
				if(winamount>0.0)
				{
					Double newcredit=cfnlib.round(verifycredit-betamt+winamount);					
					System.out.println("new credit: "+newcredit);
					if(verifycredit1.equals(newcredit))
					{
						sanitysuit.detailsAppend("Verify that current bet is deducting from balance and if any win occurs win amount is adding to credits", " current bet must  deduct from balance and if any win occurs win amount must add to credits", "Correct bet "+betamt+" is deducting from credits and win"+winamount+ "  is getting added to credits,Credit before spin"+verifycredit+" and credits after spin" +verifycredit1 , "pass");
					}
					else
					{
						sanitysuit.detailsAppend("Verify that current bet is deducting from balance and if any win occurs win amount is adding to credits", " current bet must  deduct from balance and if any win occurs win amount must add to credits", "Correct bet "+betamt+ "is not  deducting from credits and win "+winamount+"is not getting added to credits,Credit before spin"+verifycredit+" and credits after spin" +verifycredit1 , "fail");	}
				}
				else
				{
					Double newcredit=cfnlib.round(verifycredit-betamt);
					System.out.println("new credit: "+newcredit);
					if(verifycredit1.equals(newcredit))
					{
						sanitysuit.detailsAppend("Verify that current bet is deducting from balance and if any win occurs win amount is adding to credits", " current bet must  deduct from balance and if any win occurs win amount must add to credits", "Correct bet "+betamt+" is deducting from credits and win is getting added to credits,Credit before spin"+verifycredit+" and credits after spin" +verifycredit1 , "pass");
					}
					else
					{
						sanitysuit.detailsAppend("Verify that current bet is deducting from balance and if any win occurs win amount is adding to credits", " current bet must  deduct from balance and if any win occurs win amount must add to credits", "Correct bet  "+betamt+ "is not  deducting from credits and win is getting added to credits,Credit before spin"+verifycredit+" and credits after spin" +verifycredit1 , "fail");	
					}
				}
				/*	Verify that after changing the bet and refresh the game, bet is not getting change*/
				System.out.println("bet before refresh: "+betamt);
				Double minbet=cfnlib.GetBetAmt();
				System.out.println("minbet: "+minbet);
				if(minbet.equals(minbet1))
				{
					cfnlib.betIncrease();
					Thread.sleep(1000);
					Double betAmount0=cfnlib.GetBetAmt();
					cfnlib.spinclick();
					cfnlib.waitTillStop();
					bgImage=cfnlib.betDecrease();
					Double betAmount=cfnlib.GetBetAmt();
					/*Verify that bet is decreasing*/
					System.out.println("bet decrease: "+betAmount0);
					if(!betAmount.equals(betAmount0))
					{
						sanitysuit.detailsAppend("Verify that clicking on - bet button ,Bet must decrease ","After clicking on - button ,bet must change","bet is chaging,bet before changing :" +betAmount0+" Bet after decresing :"+betAmount ,"pass" );
					}
					else
					{
						sanitysuit.detailsAppend("Verify that clicking on - bet button ,Bet must decrease ","After clicking on - button ,bet must change","bet is not chaging,bet before changing :" +betAmount0+" Bet after decresing :"+betAmount ,"fail" );	
					}
					/*	Verify that on changing bet and didn't spin and refresh the game,bet is not changing								webdriver.navigate().refresh();
					 */								
					cfnlib.refreshWait();
					 Double betAmount1=cfnlib.GetBetAmt();
					 System.out.println("bet after refresh : "+betAmount1);
					 Thread.sleep(2000);
					 if(betAmount0.equals(betAmount1))
					 {
						 sanitysuit.detailsAppend("Verify that after changing bet and did not click on spin and refresh the game ,bet value must not be change","after changing bet and did not click on spin and refresh the game ,the bet value must not change","bet value is not not chanign after refresh"+"bet value before changing bet "+betAmount0+ " bet value after changing bet : "+betAmount0+"bet value after refresh"+betAmount1,"pass" );
					 }
					 else
					 {
						 sanitysuit.detailsAppend("Verify that after changing bet and did not click on spin and refresh the game ,bet value must not be change","after changing bet and did not click on spin and refresh the game ,the bet value must not change","bet value is not not chanign after refresh"+"bet value before changing bet "+betAmount0+ " bet value after changing bet : "+betAmount0+"bet value after refresh"+betAmount1,"fail" );
					 }
				}
				else
				{
					Double betAmount0=cfnlib.GetBetAmt();
					bgImage=cfnlib.betDecrease();
					Double betAmount=cfnlib.GetBetAmt();
					/*Verify that bet is decreasing*/
					System.out.println("betdercreas"+betAmount0);
					if(!betAmount.equals(betAmount0))
					{
						sanitysuit.detailsAppend("Verify that clicking on - bet button ,Bet must decrease ","After clicking on - button ,bet must change","bet is chaging,bet before changing :" +betAmount+" Bet after decresing :"+betAmount0  ,"pass" );
					}
					else
					{
						sanitysuit.detailsAppend("Verify that clicking on - bet button ,Bet must decrease ","After clicking on - button ,bet must change","bet is not chaging,bet before changing :" +betAmount+" Bet after decresing :"+betAmount0  ,"fail" );	
					}
					/*Verify that on changing bet and didn't spin and refresh the game,bet is not changing*/
					webdriver.navigate().refresh();
					cfnlib.refreshWait();
					Double betAmount1=cfnlib.GetBetAmt();
					System.out.println("bet after refresh : "+betAmount1);
					Thread.sleep(2000);
					if(betAmount0.equals(betAmount1))
					{
						sanitysuit.detailsAppend("Verify that after changing bet and did not click on spin and refresh the game ,bet value must not be change","after changing bet and did not click on spin and refresh the game ,the bet value must not change","bet value is not not chanign after refresh"+"bet value before changing bet "+betAmount0+ " bet value after changing bet : "+betAmount+"bet value after refresh"+betAmount1,"pass" );
					}
					else
					{
						sanitysuit.detailsAppend("Verify that after changing bet and did not click on spin and refresh the game ,bet value must not be change","after changing bet and did not click on spin and refresh the game ,the bet value must not change","bet value is not not chanign after refresh"+"bet value before changing bet "+betAmount0+ " bet value after changing bet : "+betAmount+"bet value after refresh"+betAmount1,"fail" );

					}
				}
				Double maxbet=cfnlib.GetBetAmt();
				if(maxbet.equals(maxbet1))
				{
					cfnlib.betDecrease();
					Thread.sleep(1000);
					Double betAmount2=cfnlib.GetBetAmt();
					System.out.println("betAmount2 : "+betAmount2);
					cfnlib.spinclick();
					cfnlib.waitTillStop();
					Thread.sleep(3000);
					/*	Verify that bet is increasing*/
					cfnlib.betIncrease();
					Thread.sleep(2000);
					Double betAmount3=cfnlib.GetBetAmt();
					System.out.println("bet increase: "+betAmount3);
					Thread.sleep(2000);
					if(!betAmount2.equals(betAmount3))
					{
						sanitysuit.detailsAppend("Verify that clicking on + bet button ,Bet must increasing ","After clicking on + button ,bet must increasing","bet is chaging,bet before changing :" +betAmount2+" Bet after increasing :"+betAmount3  ,"pass" );
					}
					else
					{
						sanitysuit.detailsAppend("Verify that clicking on + bet button ,Bet must increasing ","After clicking on + button ,bet must increasing","bet is not chaging,bet before changing :" +betAmount2+" Bet after increasing :"+betAmount3  ,"fail" );
					}
					webdriver.navigate().refresh();
					cfnlib.refreshWait();
					Double betAmount4=cfnlib.GetBetAmt();
					System.out.println("bet after refresh: "+betAmount4);
					if(betAmount4.equals(betAmount2))
					{
						sanitysuit.detailsAppend("Verify that after changing bet and did not click on spin and refresh the game ,bet value must not be change","after changing bet and did not click on spin and refresh the game ,the bet value must not change","bet value before changing bet "+betAmount2+ " bet value after changing bet "+betAmount4,"pass" );
					}
					else
					{
						sanitysuit.detailsAppend("Verify that after changing bet and did not click on spin and refresh the game ,bet value must not be change","after changing bet and did not click on spin and refresh the game ,the bet value must not change","bet is changing after refresh the game","fail" );
					}
				}
				else
				{
					Double betAmount2=cfnlib.GetBetAmt();
					System.out.println("betAmount2 : "+betAmount2);
					Thread.sleep(3000);
					/*Verify that bet is increasing*/
					cfnlib.betIncrease();
					Thread.sleep(2000);
					Double betAmount3=cfnlib.GetBetAmt();
					System.out.println("bet increase"+betAmount3);
					Thread.sleep(2000);
					if(!betAmount2.equals(betAmount3))
					{
						sanitysuit.detailsAppend("Verify that clicking on + bet button ,Bet must increasing ","After clicking on + button ,bet must increasing","bet is chaging,bet before changing :" +betAmount2+" Bet after increasing :"+betAmount3  ,"pass" );
					}
					else
					{
						sanitysuit.detailsAppend("Verify that clicking on + bet button ,Bet must increasing ","After clicking on + button ,bet must increasing","bet is not chaging,bet before changing :" +betAmount2+" Bet after increasing :"+betAmount3  ,"fail" );
					}
					webdriver.navigate().refresh();
					cfnlib.refreshWait();
					Double betAmount4=cfnlib.GetBetAmt();
					System.out.println("bet after refresh"+betAmount4);
					if(betAmount4.equals(betAmount2))
					{
						sanitysuit.detailsAppend("Verify that after changing bet and did not click on spin and refresh the game ,bet value must not be change","after changing bet and did not click on spin and refresh the game ,the bet value must not change","bet value before changing bet "+betAmount2+ " bet value after changing bet :"+betAmount4,"pass" );
					}
					else
					{
						sanitysuit.detailsAppend("Verify that after changing bet and did not click on spin and refresh the game ,bet value must not be change","after changing bet and did not click on spin and refresh the game ,the bet value must not change","bet is changing after refresh the game","fail" );
					}

				}
				cfnlib.spinclick();
				Thread.sleep(3000);
				Double winamount2=Double.parseDouble(cfnlib.GetWinAmt().replace(",", ""));
				System.out.println("winbetcahnge: "+winamount2);
				if(!winamount2.equals(winamt))
				{
					sanitysuit.detailsAppend("Verify that if win is occuring,before changing bet win is  displaying in win box","if win is occuring,after changing bet win amount is displaying in win box","win amount is displaying before changing bet", "pass");
					cfnlib.betDecrease();
					Double winamount1=Double.parseDouble(cfnlib.GetWinAmt().replace(",",""));
					System.out.println("win afte rbet change :"+winamount1);
					if(winamount1.equals(winamt))
					{
						sanitysuit.detailsAppend("Verify that if win is occuring,after changing bet win is not displaying in win box","if win is occuring,after changing bet win amount is not displaying in win box","win amount is not displaying in win box after changing bet", "pass");
					}
					else
					{
						sanitysuit.detailsAppend("Verify that if win is occuring,after changing bet win is not displaying in win box","if win is occuring,after changing bet win amount is not displaying in win box","win amount is displaying in win box after changing bet", "fail");
					}
				}
				Thread.sleep(3000);
				cfnlib.spinclick();
				cfnlib.waitTillStop();
				cfnlib.winClick();
				cfnlib.moveCoinSizeSlider();
				System.out.println("slider open");
				Thread.sleep(3000);
				sanitysuit.detailsAppend("Verify that coin selector total bet amount", "coin selector total bet amount ", "coin selector total bet amount ", "pass");
				Thread.sleep(3000);
				Double totalbet=Double.parseDouble(cfnlib.Slider_TotalBetamnt());
				Thread.sleep(3000);
				cfnlib.Coinselectorclose();
				Double totalbet1=cfnlib.GetBetAmt();
				System.out.println("totalbet1"+cfnlib.GetBetAmt());
				if(totalbet.equals(totalbet1))
				{
					sanitysuit.detailsAppend("Verify that coin selector total bet amount and bet box bet amount is equal ", "coin selector total bet amount and bet box bet amount must be same", "coin selector total bet amount :"+totalbet+"and bet box bet amount is same :"+totalbet1, "pass");
					System.out.println("bet equal");
				}
				else
				{
					sanitysuit.detailsAppend("Verify that coin selector total bet amount and bet box bet amount is equal ", "coin selector total bet amount and bet box bet amount must be same", "coin selector total bet amount and bet box bet amount is not same", "fail");
				}

				Thread.sleep(3000);
				System.out.println("paytable open");
				cfnlib.capturePaytableScreenshot(sanitysuit, "");

				Thread.sleep(500);
				/*cfnlib.clickIcon(sanitysuit);
							sanitysuit.details_append("Verify that Logout functionality ", "User should able to Logout sucessfully", "", "");*/
				String logout= cfnlib.Func_logout_OD();
				System.out.println("The logout title is "+logout);
				if(logout.trim()!=null&&logout.equalsIgnoreCase("Log Out"))
				{
					sanitysuit.detailsAppend("Verify that user has successfully logged out ", "User should be successfully logged out", "User is Successfully logged out", "pass");
				}
				else 
				{
					sanitysuit.detailsAppend("Verify that user has successfully logged out ", "User should be successfully logged out", "User is not Successfully logged out", "fail");
				}
			}
			/*	Handling logout from game 
						sanitysuit.details_append("Verify that Logout functionality ", "User should able to Logout sucessfully", "", "");
						String logout= cfnlib.Func_logout_OD();
						System.out.println("The logout title is "+logout);
						if(logout.trim()!=null&&logout.equalsIgnoreCase("Log Out"))
						{
							sanitysuit.details_append("Verify that user has successfully logged out ", "User should be successfully logged out", "User is Successfully logged out", "pass");
						}
						else 
						{
							sanitysuit.details_append("Verify that user has successfully logged out ", "User should be successfully logged out", "User is not Successfully logged out", "fail");
						}*/
			//}
			//}
		}
		//-------------------Handling the exception---------------------//
		catch (Exception e) {
			cfnlib.evalException(e);
		}
		finally
		{
			sanitysuit.endReport();
			webdriver.close();
			webdriver.quit();
			proxy.abort();
			Thread.sleep(1000);	   
		}	
	}
}




