package Modules.Regression.TestScript;
/**
 * This script is older one 
 * you can find updated script with name Mobile_Regression_BaseScene 
 */
import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Mobile_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.DataBaseFunctions;

import Modules.Regression.FunctionLibrary.CFNLibrary_Mobile;
import Modules.Regression.FunctionLibrary.factory.MobileFunctionLibraryFactory;
import io.appium.java_client.AppiumDriver;
import net.lightbody.bmp.BrowserMobProxyServer;
public class Mobile_Regression_Suit {
	public static Logger log = Logger.getLogger(Mobile_Regression_Suit.class.getName());
	public ScriptParameters scriptParameters;
	
	
	//-------------------Main script defination---------------------//
	public void script() throws Exception {
		String mstrTC_Name=scriptParameters.getMstrTCName();
		String mstrTC_Desc=scriptParameters.getMstrTCDesc();
		String mstrModuleName=scriptParameters.getMstrModuleName();
		BrowserMobProxyServer proxy=scriptParameters.getProxy();
		String startTime=scriptParameters.getStartTime();
		String filePath=scriptParameters.getFilePath();
		
		//AndroidDriver<WebElement> webdriver=scriptParameters.getWebdriver();
		AppiumDriver<WebElement> webdriver=scriptParameters.getAppiumWebdriver();
		
		String DeviceName=scriptParameters.getDeviceName();
		String userName=scriptParameters.getUserName();
		String framework=scriptParameters.getFramework();
		//String deviceLockKey=scriptParameters.getDeviceLockKey();
		String gameName=scriptParameters.getGameName();
		//int checkedOutDeviceNum=scriptParameters.getCheckedOutDeviceNum();
		
		
		String Status=null;
		int mintDetailCount=0;
		//int mintSubStepNo=0;
		int mintPassed=0;
		int mintFailed=0;
		int mintWarnings=0;
		int mintStepNo=0;
		
		String classname= this.getClass().getSimpleName();

		
		String xmlFilePath = System.getProperty("user.dir") + "\\src\\Modules\\Regression\\TestData\\GameData\\"
				+ classname + ".testdata";
		File sourceFile = new File(xmlFilePath);
		
		File destFile = new File("\\\\10.247.224.110\\c$\\MGS_TestData\\"+classname+".testdata");

		webdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Mobile_HTML_Report reg_Suit=new Mobile_HTML_Report(webdriver,DeviceName,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,gameName);
		CommonUtil util=new CommonUtil();

		//DataBaseFunctions createplayer=new DataBaseFunctions("zensarqa2","Casino","10.247.208.113","1402","5001");
		DataBaseFunctions createplayer=new DataBaseFunctions("zensarqa","Casino","10.247.208.62","1402","5001");
		MobileFunctionLibraryFactory mobileFunctionLibraryFactory=new MobileFunctionLibraryFactory();
		CFNLibrary_Mobile cfnlib=mobileFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy, reg_Suit, gameName);
		
		
		WebDriverWait	wait = new WebDriverWait(webdriver, 50);
		try{
			//-------------------Getting webdriver of proper test site---------------------//
			// Step 1
			if(webdriver!=null)
			{		
				userName=util.randomStringgenerator();
				System.out.println("The New username is =="+ userName);
					createplayer.createUser(userName,"0", 0);

				//--------------------------Update the Xml File of Papyrus Data----------//
					util.changePlayerName(userName, xmlFilePath);

				
				//-----------------------Copy the Papyrus Data to The CasinoAs1 Server---------//
					util.copyFolder(sourceFile, destFile);
				String appurl=cfnlib.xpathMap.get("ApplicationURL");
				String obj=cfnlib.funcNavigate(appurl);
				if(obj!=null)
				{
					 //need to uncomment  reg_Suit.details_append("Open browser and Enter game URL in address bar and click Enter", "Island Paradise Lobby should be displayed", "Island Paradise lobby is displayed", "Pass");
				}
				else
				{
					 //need to uncomment  reg_Suit.details_append("Open browser and Enter Game URL in address bar and click Enter", "Island Paradise lobby should be displayed", "Island paradise is not displayed", "Fail");	
				}

				String GameTitle=cfnlib.loginToBaseScene(userName,Constant.PASSWORD);
				 //need to uncomment  reg_Suit.details_append("Check the image is displaying on splash scren", "Image  must display", "", "");
				if(GameTitle.trim()!=null)
				{
					 //need to uncomment  reg_Suit.details_append("Check that user is able to login with username and password and  Title verified ", "User should be logged in successfully and "+GameName+" Game should be launched ", "Logged in succesfully and "+GameName+" is launched. ", "Pass");
				}
				else
				{
					 //need to uncomment  reg_Suit.details_append("Check that user is not able to login with username and password", "User should be logged in successfully and "+GameName+" Game should be launched ", "Logged in succesfully and "+GameName+" is not launched. ", "Fail");
				}
				String gamelogo=cfnlib.gamelogo();
				cfnlib.refresh();
				if(gamelogo!=null)
				{
						 //need to uncomment  reg_Suit.details_append("Verify that the application should display Logo and game name", "Game logo and game name with base scene should display", "Game logo is displaying "+GameName+" ", "pass");                     
				}
				else                                                                                                                                                                                                                                     
				{                                                                                                                                                                                                                                    
							 //need to uncomment  reg_Suit.details_append("Verify that the application should display Logo and game name", "Game logo and game name with base scene should display", "Game logo and game name is not  displaying "+GameName+"", "fail");   
				}
				// verify balance     
				String verifycredit=cfnlib.verifyCredit();                                                                                                                                                                                         
				if(verifycredit!=null)                                                                                                                                                                                                      
				{                                                                                                                                                                                                                           
						 //need to uncomment  reg_Suit.details_append("credits and bet is displaying on base screen  ", "Credits and bet must display on the screen  ", "Credit amount :"+verifycredit+ "and bet :"+betvalue+" is visible on screen", "pass");                                                  
				}                                                                                                                                                                                                                           
				else                                                                                                                                                                                                                        
				{                                                                                                                                                                                                                           
					  //need to uncomment  reg_Suit.details_append("credits and bet is displaying on base screen  ", "Credits and bet must display on the screen  ", "Credit amount :"+verifycredit+ "and bet :"+betvalue+" is not visible on the screen", "fail");                                          
				}                                                                                                                                                                                                                           
				/*	Verify that on spin ,stop button should displayed when quick spin is disabled */
				cfnlib.settingsOpen();
				String opacity=cfnlib.quickSpinStatus();
				if(opacity.equals("0.5"))
				{   
					Thread.sleep(3000);
						 //need to uncomment  reg_Suit.details_append("Verify that Quick spin is disabled when game launch on first time", "When game is launched first time quick spin must be in disabled state ", "On launching the game  first time quick spin is  in disabled state "+ GameName+" ", "pass");
					cfnlib.SettingsToBasescen();
					boolean bgImage=cfnlib.verifySpin_Stop();
					if(bgImage)
					{
								 //need to uncomment  reg_Suit.details_append("Verify that when quick spin is disabled and after clicking on spin button it should change the logo from spin to stop logo ", "During the quick spin disabled, spin button logo should replace with stop button ", "During the quick spin disabled ,spin button logo is replacing with stop button ", "pass");
					}

					else
					{
							 //need to uncomment  reg_Suit.details_append("Verify that when quick spin is disabled and after clicking on spin button it should change the logo from spin to stop logo ", "During the quick spin disabled, spin button logo should replace with stop button ", "During the quick spin disabled ,spin button logo is not replacing with stop button ", "fail");
					}
					cfnlib.waitTillStop();
				}
				cfnlib.winClick();
				cfnlib.settingsOpen();
				 cfnlib.tap_quickSpin();
				String opacity1=cfnlib.quickSpinStatus();
				if(opacity1.equals("1"))
				{
						 //need to uncomment  reg_Suit.details_append("Verify that clicking on quick spin toggle it must get enable" ," Clicking on quick spin toggle it must get  enable ", "Clicking on quick spin toggle button ,Its getting enable", "pass");
				}
				else
				{
						 //need to uncomment  reg_Suit.details_append("Verify that clicking on quick spin toggle it must get enable" ," Clicking on quick spin toggle it must get  enable ", "Clicking on quick spin toggle button ,Its not getting enable", "fail");
				}	
				cfnlib.SettingsToBasescen();
				Double verifycredit1=Double.parseDouble(cfnlib.verifyCredit().replace(",", ""));
				//Need to check aslo function GetBetAmt1() while debugging
				Double betamt=Double.parseDouble(cfnlib.getBetAmt().replace(",", ""));
				System.out.println("verify credits : "+verifycredit1);
				boolean bgImage=cfnlib.verifySpin_Stop();
				if(!bgImage)
				{
						 //need to uncomment  reg_Suit.details_append("Verify that when quick spin is enabled and on spin, spin button is not converting to stop Button" ,"when quick spin is enabled and on spin, spin button must  converting to stop Button", "when quick spin is enabled and on spin, spin button is not converting to stop Button", "pass");
				}

				else
				{
						 //need to uncomment  reg_Suit.details_append("Verify that when quick spin is enabled and on spin, spin is not converting to stop Button" ,"when quick spin is enabled and on spin, spin must  converting to stop Button", "when quick spin is enabled and on spin, spin button is converting to stop Button", "fail");
				}
				cfnlib.waitTillStop();
				cfnlib.winClick();
				Double winamount=Double.parseDouble(cfnlib.getWinAmt().replace(",", ""));
				Double verifycredit2=Double.parseDouble(cfnlib.verifyCredit().replace(",", ""));
				System.out.println("double amount"+verifycredit2);
				if(winamount>0.0)
				{
					Double newcredit=verifycredit1-betamt+winamount;
					System.out.println("new credit"+newcredit);
					if(verifycredit1.equals(newcredit))
					{
								 //need to uncomment  reg_Suit.details_append("Verify that correct bet is deducting from balance and if any win occurs win amount is adding to credits", " currect bet must  deduct from balance and if any win occurs win amount must add to credits", "Correct bet "+betamt+" is deducting from credits and win"+winamount+ "  is getting added to credits,Credit before spin"+verifycredit+" and credits after spin" +verifycredit1 , "pass");
					}
					else
					{
							 //need to uncomment  reg_Suit.details_append("Verify that correct bet is deducting from balance and if any win occurs win amount is adding to credits", " currect bet must  deduct from balance and if any win occurs win amount must add to credits", "Correct bet "+betamt+ "is not  deducting from credits and win "+winamount+"is not getting added to credits,Credit before spin"+verifycredit+" and credits after spin" +verifycredit1 , "fail");	
					}
				}

				else
				{
					Double newcredit=verifycredit1-betamt;
					System.out.println("newcredit"+newcredit);
					if(verifycredit1.equals(newcredit))
					{
							 //need to uncomment  reg_Suit.details_append("Verify that correct bet is deducting from balance and if any win occurs win amount is adding to credits", " currect bet must  deduct from balance and if any win occurs win amount must add to credits", "Correct bet "+betamt+" is deducting from credits and win is getting added to credits,Credit before spin"+verifycredit+" and credits after spin" +verifycredit1 , "pass");
					}
					else
					{
							 //need to uncomment  reg_Suit.details_append("Verify that correct bet is deducting from balance and if any win occurs win amount is adding to credits", " currect bet must  deduct from balance and if any win occurs win amount must add to credits", "Correct bet  "+betamt+ "is not  deducting from credits and win is getting added to credits,Credit before spin"+verifycredit+" and credits after spin" +verifycredit1 , "fail");	
					}
				}
				/*	Verify that after changing the bet and refresh the game, bet is not getting change*/
				    Double betAmount2=Double.parseDouble(cfnlib.getBetAmt().replace(",", "").replace(" ", ""));
				    System.out.println("betAmount2 : "+betAmount2);
			     	cfnlib.moveCoinSizeSlider();
				    Thread.sleep(2000);
				   Double betAmount3=Double.parseDouble(cfnlib.Slider_TotalBetamnt());
				//need to delete	Double betAmount3=Double.parseDouble(cfnlib.GetBetAmt().replace(",", "").replace(" ", ""));
				    System.out.println("bet increase"+betAmount3);
				   Thread.sleep(2000);
				   cfnlib.Coinselectorclose();
				  
				  // cfnlib.enableAdressbar();
				webdriver.navigate().refresh();
			//	cfnlib.error_Handler();
				cfnlib.refreshWait();
				Double betAmount4=Double.parseDouble(cfnlib.getBetAmt().replace(",", ""));
				System.out.println("bet after refresh"+betAmount4);
				if(betAmount4.equals(betAmount2))
				{
						 //need to uncomment  reg_Suit.details_append("Verify that after changing bet and did not click on spin and refresh the game ,bet value must not be change","after changing bet and did not click on spin and refresh the game ,the bet value must not change","bet value before changing bet "+betAmount2+ " bet value after changing bet :"+betAmount4,"pass" );
				}
				else
				{
						 //need to uncomment  reg_Suit.details_append("Verify that after changing bet and did not click on spin and refresh the game ,bet value must not be change","after changing bet and did not click on spin and refresh the game ,the bet value must not change","bet is changing after refresh the game","fail" );
				}
				cfnlib.spinclick();
				/*Verify that big win screen is displaying with overlay */
					if(cfnlib.xpathMap.get("BigWin").equalsIgnoreCase("Yes"))
					{
					boolean bigWin=	cfnlib.bigWin_Wait();
						if(bigWin)
						{
									 //need to uncomment  reg_Suit.details_append("Verify that big win screen is displaying with overlay ","Big win screen must display ,Once big win triggers ","On  triggering big win,Big win screen is displaying","pass");
						}
						else
						{
									 //need to uncomment  reg_Suit.details_append("Verify that big win screen is displaying with overlay ","Big win screen must display ,Once big win triggers ","On  triggering big win,Big win screen is not displaying ","fail");
						}
					/*	Verify that on big win overlay,Setting and paytable page must open*/ 
						boolean sOpen=cfnlib.settingsOpen();
						Thread.sleep(2000);
						if(sOpen)
						{
								 //need to uncomment  reg_Suit.details_append("Verify that on big win overlay ,setting page is getting open ","On big win overlay ,Clicking on settings setting page must open","on big win overlay settings page is opening","pass");
						}
						else
						{
								 //need to uncomment  reg_Suit.details_append("Verify that on big win overlay ,setting page is getting open ","On big win overlay ,Clicking on settings setting page must open","on big win overlay settings page is not opening","fail");
						}
						cfnlib.SettingsToBasescen();
						/*Verify that on big win overlay ,paytable page is getting open */
 						boolean pOpen=cfnlib.paytableOpen(reg_Suit,"");

						if(pOpen)
						{
								 //need to uncomment  reg_Suit.details_append("Verify that on big win overlay ,paytable page is getting open ","On big win overlay ,Clicking on paytable page must open","on big win overlay paytable page is opening","pass");
						}
						else
						{
								 //need to uncomment  reg_Suit.details_append("Verify that on big win overlay ,paytable page is getting open ","On big win overlay ,Clicking on paytable page must open","on big win overlay paytable page is not  opening","fail");
						}
						cfnlib.paytableClose();
						cfnlib.nativeClickByID(cfnlib.xpathMap.get("OD_BIgWin_ID"));
					}
					/*Verify that if win is occuring,after changing bet win is  displaying in win box*/
					Double winamount2=Double.parseDouble(cfnlib.getWinAmt().replace(",", ""));
					System.out.println("winbetcahnge"+winamount2);
					if(!winamount2.equals(winamount))
					{
						//need to uncomment  reg_Suit.details_append("Verify that if win is occuring,before changing bet win is  displaying in win box","if win is occuring,after changing bet win amount is displaying in win box","win amount is displaying before changing bet", "pass");
						cfnlib.moveCoinSizeSlider();
						cfnlib.Coinselectorclose();
						Double winamount1=Double.parseDouble(cfnlib.getWinAmt().replace(",",""));
						System.out.println("win after bet change :"+winamount1);
						if(winamount1.equals(winamount))
						{
									 //need to uncomment  reg_Suit.details_append("Verify that if win is occuring,after changing bet win is not displaying in win box","if win is occuring,after changing bet win amount is not displaying in win box","win amount is not displaying in win box after changing bet", "pass");
						}
						else
						{
									 //need to uncomment  reg_Suit.details_append("Verify that if win is occuring,after changing bet win is not displaying in win box","if win is occuring,after changing bet win amount is not displaying in win box","win amount is displaying in win box after changing bet", "fail");
						}
					}
					cfnlib.spinclick();
					cfnlib.waitTillStop();
					cfnlib.winClick();
					/*Verify that coin selector total bet amount*/
					cfnlib.moveCoinSizeSlider();
					System.out.println("slider open");
					//need to uncomment  reg_Suit.details_append("Verify that coin selector total bet amount", "coin selector total bet amount ", "coin selector total bet amount ", "pass");
					Double totalbet=Double.parseDouble(cfnlib.Slider_TotalBetamnt());
					cfnlib.Coinselectorclose();
					Double totalbet1=Double.parseDouble(cfnlib.getBetAmt().replace(",", ""));
					/*Verify that coin selector total bet amount and bet box bet amount is equal*/
					if(totalbet.equals(totalbet1))
					{
							 //need to uncomment  reg_Suit.details_append("Verify that coin selector total bet amount and bet box bet amount is equal ", "coin selector total bet amount and bet box bet amount must be same", "coin selector total bet amount :"+totalbet+"and bet box bet amount is same :"+totalbet1, "pass");
						System.out.println("bet equal");
					}
					else
					{
							 //need to uncomment  reg_Suit.details_append("Verify that coin selector total bet amount and bet box bet amount is equal ", "coin selector total bet amount and bet box bet amount must be same", "coin selector total bet amount :"+totalbet+" and bet box bet amount is not same  :"+totalbet1, "fail");
					}
				for(int i=1;i<=2;i++)
				{
						cfnlib.spinclick();	//5th and 6th spin with free spin assigned 
						String clickToContinueText=cfnlib.xpathMap.get("clickToContinueText");
						if(cfnlib.xpathMap.get("FreeSpinEntryScreen").equalsIgnoreCase("yes"))
						{
							/*Free spin entry screen is displaying and after refreshing click to continue button is displaying*/
							String CTC_wait=cfnlib.entryScreen_Wait(clickToContinueText);
							if(CTC_wait!=null)
							{
										 //need to uncomment  reg_Suit.details_append("Free spin entry screen is displaying", "free spin entry screen must display", "free screen entry screen is displaying", "pass");
							}
							else
							{
										 //need to uncomment  reg_Suit.details_append("Free spin entry screen is displaying", "free spin entry screen must display", "free screen entry screen is not displaying", "fail");
							}
						}
						/*Verify that after refreshing free spins entry screen,entry screen must display 	*/	
						cfnlib.clickOnAddressBar();
						webdriver.navigate().refresh();
						String wait1=cfnlib.FS_RefreshWait();
							 //need to uncomment  reg_Suit.details_append("on refreshing entry screen of free spin","splash screen must come on refreshing entry screen of free spin","splash screen is coming on refreshing entry screen of free spin", "pass");
						Thread.sleep(2000);
						if(wait1!=null)
						{
								 //need to uncomment  reg_Suit.details_append("Verify that after refreshing the free spin screen ,Continue button is coming","After refreshing free spins screen,Continue buttton must come","After refreshing free spins screen,Continue button is coming","pass");
						}
						else
						{
							  //need to uncomment  reg_Suit.details_append("Verify that after refreshing the free spin screen ,Continue button is coming","After refreshing free spins screen,Continue buttton must come","After refreshing free spins screen,Continue button is not coming","pass");
						}
						cfnlib.FS_continue();
						if(cfnlib.xpathMap.get("BigWin").equalsIgnoreCase("yes"))
						{
							if( i==1)
							{
								boolean bigWin1=cfnlib.bigWin_Wait();
									cfnlib.nativeClickByID(cfnlib.xpathMap.get("OD_BIgWin_ID"));
								if(bigWin1)
								{
									  //need to uncomment  reg_Suit.details_append("Verify that big win screen is displaying with overlay in free spin ","Big win screen must display ,Once big win triggers in free spin","On  triggering big win,Big win screen is displaying in free spin","pass");
								}
								else
								{
									  //need to uncomment  reg_Suit.details_append("Verify that big win screen is displaying with overlay in free spin ","Big win screen must display ,Once big win triggers in free spin","On  triggering big win,Big win screen is not displaying in free spin ","fail");
								}
								//cfnlib.Native_ClickByID(cfnlib.XpathMap.get("OD_BIgWin_ID"));
							}	
						}
					if(cfnlib.xpathMap.get("FreeSpinEntryScreen").equalsIgnoreCase("yes")){
						String CTC_SummaryWait=cfnlib.summaryScreen_Wait();
						if(CTC_SummaryWait!=null)
						{
							  //need to uncomment  reg_Suit.details_append("Verify the free spin summary screen is displaying", "After completing all free spin ,free spin summary screen must display", "Clicking on entry screen of free spin entry screen,It is taking user to free spin screen ", "pass");
						}
						else
						{
							  //need to uncomment  reg_Suit.details_append("Verify the Clicking on clickTocontinue taking user to free spin screen", "Clicking on anywhere on enry screen,It must take user to free spin screen ", "Clicking on entry screen of free spin entry screen,It is not taking user to free spin screen ", "fail");
						}
					}
					cfnlib.waitTillStop();
					webdriver.navigate().refresh();
				//	cfnlib.error_Handler();
					cfnlib.waitSummaryScreen();
					cfnlib.summaryScreen_Wait();
					reg_Suit.detailsAppend("Verify the Clicking on clickTocontinue taking user to free spin screen", "Clicking on anywhere on enry screen,It must take user to free spin screen ", "Clicking on entry screen of free spin entry screen,It is taking user to free spin screen ", "pass");
				    //need to uncomment  reg_Suit.details_append("on refreshing summary screen of free spin","splash screen must come on refreshing summary screen of free spin","splash screen is coming on refreshing summary screen of free spin", "pass");
					if(cfnlib.xpathMap.get("FreeSpinEntryScreen").equalsIgnoreCase("yes")){
						String CTC_SummaryWait=cfnlib.summaryScreen_Wait();
						if(CTC_SummaryWait!=null)
						{
							  //need to uncomment  reg_Suit.details_append("Verify that after refreshing free spins summary screen,summary screen must display ", "After refreshing free spins summary screen,summary screen must display ", "After refreshing free spins summary screen,summary screen is displaying ", "pass");
						}
						else
						{
							  //need to uncomment  reg_Suit.details_append("Verify that after refreshing free spins summary screen,summary screen must display ", "After refreshing free spins summary screen,summary screen must display ", "After refreshing free spins summary screen,summary screen is not displaying ", "fail");
						}
					}
					String Veify_Credits=cfnlib.verifyCredit().replaceAll(",", "");
					/*	Verify that base scene and free spin credit amount is same*/
					if(Veify_Credits.equals(Veify_Credits))
					{
						  //need to uncomment  reg_Suit.details_append("Verify that base scene and free spin credit amount is same","Base scene and free spin amount must be same","Base scene credit amount"+Veify_Credits+"and free spin credit amount is "+FS_Credits+"same ","pass");
					}
					else
					{
						  //need to uncomment  reg_Suit.details_append("Verify that base scene and free spin credit amount is same","Base scene and free spin amount must be same","Base scene and free spin amount must be same","Base scene credit amount"+Veify_Credits+"and free spin credit amount is "+FS_Credits+" not same ","fail");
					}
				}
				String logout= cfnlib.Func_logout_OD();
				System.out.println("The logout title is "+logout);
				if(logout.trim()!=null&&logout.equalsIgnoreCase("Log Out"))
				{
					  //need to uncomment  reg_Suit.details_append("Verify that user has successfully logged out ", "User should be successfully logged out", "User is Successfully logged out", "pass");
				}
				else 
				{
					  //need to uncomment  reg_Suit.details_append("Verify that user has successfully logged out ", "User should be successfully logged out", "User is not Successfully logged out", "fail");
				}
			}
		}//TRY
		//-------------------Handling the exception---------------------//
		catch (Exception e) {
			//	ExceptionHandler eHandle=new ExceptionHandler(e,webdriver,reg_Suit);
			e.printStackTrace();
		}
		finally{
			//Global.browserproxy.abort();
			//---------- Closing the report----------//
			reg_Suit.endReport();
			//---------- Closing the webdriver ----------//
			webdriver.close();
			webdriver.quit();
			//Global.appiumService.stop();
		}
	}


}
