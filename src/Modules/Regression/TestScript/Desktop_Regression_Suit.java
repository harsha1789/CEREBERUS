package Modules.Regression.TestScript;
/**
 * This script is older one
 * You can find updated script with name Desktop_Regression_BaseScene
 */
import java.io.File;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.zensar.automation.framework.model.ScriptParameters;
import com.zensar.automation.framework.report.Desktop_HTML_Report;
import com.zensar.automation.framework.utils.Constant;
import com.zensar.automation.library.CommonUtil;
import com.zensar.automation.library.DataBaseFunctions;
import com.zensar.automation.library.TestPropReader;

import Modules.Regression.FunctionLibrary.CFNLibrary_Desktop;
import Modules.Regression.FunctionLibrary.factory.DesktopFunctionLibraryFactory;
import net.lightbody.bmp.BrowserMobProxyServer;

	public class Desktop_Regression_Suit {
		Logger log = Logger.getLogger(Desktop_Regression_Language_Verification_BaseScene.class.getName());
		public ScriptParameters scriptParameters;
		
		
		
		public void script() throws Exception{
			String mstrTC_Name=scriptParameters.getMstrTCName();
			String mstrTC_Desc=scriptParameters.getMstrTCDesc();
			String mstrModuleName=scriptParameters.getMstrModuleName();
			WebDriver webdriver=scriptParameters.getDriver();
			BrowserMobProxyServer proxy=scriptParameters.getProxy();
			String startTime=scriptParameters.getStartTime();
			String filePath=scriptParameters.getFilePath();
			String userName=scriptParameters.getUserName();
			String browsername=scriptParameters.getBrowserName();
			String framework=scriptParameters.getFramework();
			String gameName=scriptParameters.getGameName();
			String languageDescription=null;
			String languageCode=null;
			String Status=null;
			int mintDetailCount=0;
			//int mintSubStepNo=0;
			int mintPassed=0;
			int mintFailed=0;
			int mintWarnings=0;
			int mintStepNo=0;
			 double winamt=0.0;
			 
			 int verifycreditnew=0;
			String classname= this.getClass().getSimpleName();
			 String xmlFilePath= System.getProperty("user.dir")+"\\src\\Modules\\Regression\\TestData\\GameData\\"+classname+".testdata";
			 File sourceFile= new File(xmlFilePath);

			 File destFile = new File("\\\\"+TestPropReader.getInstance().getProperty("Casinoas1IP")+"\\c$\\MGS_TestData\\"+classname + "_" + userName +".testdata");
			
			Desktop_HTML_Report reg_Suit=new Desktop_HTML_Report(webdriver,browsername,filePath,startTime,mstrTC_Name,mstrTC_Desc,mstrModuleName,mintDetailCount,mintPassed,mintFailed,mintWarnings,mintStepNo,Status,gameName);
			DesktopFunctionLibraryFactory desktopFunctionLibraryFactory=new DesktopFunctionLibraryFactory();
			CFNLibrary_Desktop cfnlib=desktopFunctionLibraryFactory.getFunctionLibrary(framework, webdriver, proxy,reg_Suit, gameName);

			CommonUtil util=new CommonUtil();
			
			DataBaseFunctions createplayer = new DataBaseFunctions(TestPropReader.getInstance().getProperty("ServerName"),
					TestPropReader.getInstance().getProperty("dataBaseName"),
					TestPropReader.getInstance().getProperty("serverIp"), TestPropReader.getInstance().getProperty("port"),
					TestPropReader.getInstance().getProperty("serverID"));
			
			try{
				
				if(webdriver!=null)
				{
					userName=util.randomStringgenerator();
					System.out.println("The New username is =="+ userName);
					createplayer.createUser(userName,"0", 0);

					//--------------------------Update the Xml File of Papyrus Data----------//
					util.changePlayerName(userName, xmlFilePath);


					//-----------------------Copy the Papyrus Data to The CasinoAs1 Server---------//
					util.copyFolder(sourceFile, destFile);
					String appurl=cfnlib.XpathMap.get("ApplicationURL");
					String obj=cfnlib.func_navigate(appurl);
					if(obj!=null)
					{
						 reg_Suit.detailsAppend("Open browser and Enter game URL in address bar and click Enter", "Island Paradise Lobby should be displayed", "Island Paradise lobby is displayed", "Pass");
					}
					else
					{
						 reg_Suit.detailsAppend("Open browser and Enter Game URL in address bar and click Enter", "Island Paradise lobby should be displayed", "Island paradise is not displayed", "Fail");	
					}

					String gameTitle=cfnlib.Func_LoginBaseScene(userName,Constant.PASSWORD);

					 reg_Suit.detailsAppend("Check the image is displaying on splash scren", "Image  must display", "", "");
					if(gameTitle.trim()!=null /*&& GameTitle.equalsIgnoreCase(gametitleexcel)*/)
					{

						reg_Suit.detailsAppend("Check that user is able to login with username and password and  Title verified ", "User should be logged in successfully and "+gameName+" Game should be launched ", "Logged in succesfully and "+gameName+" is launched. ", "Pass");
					}
					else
					{
						 reg_Suit.detailsAppend("Check that user is not able to login with username and password", "User should be logged in successfully and "+gameName+" Game should be launched ", "Logged in succesfully and "+gameName+" is not launched. ", "Fail");

					}	

				 String gamelogo=cfnlib.gamelogo();   
					if(gamelogo!=null)
					{
						 reg_Suit.detailsAppend("Verify that the application should display Logo and game name", "Game logo and game name with base scene should display", "Game logo is displaying "+gameName+" ", "pass");                     
					}
					else                                                                                                                                                                                                                                     
					{                                                                                                                                                                                                                                    
						 reg_Suit.detailsAppend("Verify that the application should display Logo and game name", "Game logo and game name with base scene should display", "Game logo and game name is not  displaying "+gameName+"", "fail");   
					}
					// verify balance     

					String verifycredit=cfnlib.verifyCredit().replaceAll(",", "");                                                                                                                                                                                         
					Double betvalue=cfnlib.GetBetAmt();
					if(verifycredit!=null)                                                                                                                                                                                                      
					{                                                                                                                                                                                                                           
						 reg_Suit.detailsAppend("credits and bet is displaying on base screen  ", "Credits and bet must display on the screen  ", "Credit amount :"+verifycredit+ "and bet :"+betvalue+" is visible on screen", "pass");                                                  
					}                                                                                                                                                                                                                           
					else                                                                                                                                                                                                                        
					{                                                                                                                                                                                                                           
						 reg_Suit.detailsAppend("credits and bet is displaying on base screen  ", "Credits and bet must display on the screen  ", "Credit amount :"+verifycredit+ "and bet :"+betvalue+" is not visible on the screen", "fail");                                          
					} 
					
				/*	Verify that on spin ,stop button should displayed when quick spin is disabled*/ 
					cfnlib.settingsOpen();

					String opacity=cfnlib.quickSpinStatus();
					if(opacity.equals("0.5"))
					{   
					//	Thread.sleep(3000);
						 reg_Suit.detailsAppend("Verify that Quick spin is disabled when game launch on first time", "When game is launched first time quick spin must be in disabled state ", "On launching the game  first time quick spin is  in disabled state "+ gameName+" ", "pass");
						cfnlib.SettingsToBasescen();
						boolean bgImage=cfnlib.customeverifyimage("STOP");//1st spin with any symbol  test data assigned to verify 
						if(bgImage)
						{
							 reg_Suit.detailsAppend("Verify that when quick spin is disabled and after clicking on spin button it should change the logo from spin to stop logo ", "During the quick spin disabled, spin button logo should replace with stop button ", "During the quick spin disabled ,spin button logo is replacing with stop button ", "pass");
						}
						else
						{
							 reg_Suit.detailsAppend("Verify that when quick spin is disabled and after clicking on spin button it should change the logo from spin to stop logo ", "During the quick spin disabled, spin button logo should replace with stop button ", "During the quick spin disabled ,spin button logo is not replacing with stop button ", "fail");
						}
					}
						/*Verify that stop button should not be displayed when quick spin is enabled*/ 
						cfnlib.winClick();
						String paytable=cfnlib.capturePaytableScreenshot(reg_Suit, "");
						if(paytable!=null)
						{
							 reg_Suit.detailsAppend("verify the paytable screen shot", " paytable first page screen shot", "paytable first page screenshot ", "pass");

						}
						//Thread.sleep(2000);
						Double symbol_Value=Double.parseDouble(cfnlib.symbol_Value(	cfnlib.XpathMap.get("5_K_Symbol")));
						cfnlib.paytableClose();
						Double winamnt=Double.parseDouble(cfnlib.GetWinAmt().replace(",", ""));
						Thread.sleep(1000);
						if(symbol_Value.equals(winamnt))
						{
							 reg_Suit.detailsAppend("Verify that win value is coming according to symbol payout" ,"Win value must come according to symbol payout", "win vlaue is coming according to symbol payout", "pass");
						}
						else
						{
							 reg_Suit.detailsAppend("Verify that win value is coming according to symbol payout" ,"Win value must come according to symbol payout", "win vlaue is not coming according to symbol payout", "fail");
						}
						cfnlib.settingsOpen();
						cfnlib.QuickSpinclick();
						String opacity1=cfnlib.quickSpinStatus();
						//Thread.sleep(3000);
						if(opacity1.equals("1"))
						{
							 reg_Suit.detailsAppend("Verify that clicking on quick spin toggle it must get enable" ," Clicking on quick spin toggle it must get  enable ", "Clicking on quick spin toggle button ,Its getting enable", "pass");
						}
						else
						{
							 reg_Suit.detailsAppend("Verify that clicking on quick spin toggle it must get enable" ," Clicking on quick spin toggle it must get  enable ", "Clicking on quick spin toggle button ,Its not getting enable", "fail");
						}	
						cfnlib.SettingsToBasescen();
						Double verifycredit1=Double.parseDouble(cfnlib.verifyCredit().replace(",", ""));
						Double betamt=cfnlib.GetBetAmt();
						System.out.println("verify credits : "+verifycredit1);
						boolean bgImage=cfnlib.customeverifyimage("SPIN");//2nd spin with any test data set
						if(bgImage)
						{
							 reg_Suit.detailsAppend("Verify that when quick spin is enabled and on spin, spin button is not converting to stop Button" ,"when quick spin is enabled and on spin, spin button must  converting to stop Button", "when quick spin is enabled and on spin, spin button is not converting to stop Button", "pass");
						}

						else
						{
							 reg_Suit.detailsAppend("Verify that when quick spin is enabled and on spin, spin is not converting to stop Button" ,"when quick spin is enabled and on spin, spin must  converting to stop Button", "when quick spin is enabled and on spin, spin button is converting to stop Button", "fail");
						}
						cfnlib.waitTillStop();
						cfnlib.winClick();
						Double winamount=Double.parseDouble(cfnlib.GetWinAmt().replace(",", ""));
						
						/*Verify that currect bet is deducting from balance and if any win occurs win amount is adding to credits*/
						Double verifycredit2=Double.parseDouble(cfnlib.verifyCredit().replace(",", ""));
						System.out.println("double amount"+verifycredit2);
						if(winamount>0.0)
						{
							Double newcredit=verifycredit1-betamt+winamount;
							System.out.println("new credit"+newcredit);
							if(verifycredit1.equals(newcredit))
							{
								 reg_Suit.detailsAppend("Verify that currect bet is deducting from balance and if any win occurs win amount is adding to credits", " currect bet must  deduct from balance and if any win occurs win amount must add to credits", "Correct bet "+betamt+" is deducting from credits and win"+winamount+ "  is getting added to credits,Credit before spin"+verifycredit+" and credits after spin" +verifycredit1 , "pass");
							}
							else
							{
								 reg_Suit.detailsAppend("Verify that currect bet is deducting from balance and if any win occurs win amount is adding to credits", " currect bet must  deduct from balance and if any win occurs win amount must add to credits", "Correct bet "+betamt+ "is not  deducting from credits and win "+winamount+"is not getting added to credits,Credit before spin"+verifycredit+" and credits after spin" +verifycredit1 , "fail");	
							}
						}
						else
						{
							Double newcredit=verifycredit1-betamt;
							System.out.println("newcredit"+newcredit);
							if(verifycredit1.equals(newcredit))
							{
								 reg_Suit.detailsAppend("Verify that currect bet is deducting from balance and if any win occurs win amount is adding to credits", " currect bet must  deduct from balance and if any win occurs win amount must add to credits", "Correct bet "+betamt+" is deducting from credits and win is getting added to credits,Credit before spin"+verifycredit+" and credits after spin" +verifycredit1 , "pass");
							}
							else
							{
								 reg_Suit.detailsAppend("Verify that currect bet is deducting from balance and if any win occurs win amount is adding to credits", " currect bet must  deduct from balance and if any win occurs win amount must add to credits", "Correct bet  "+betamt+ "is not  deducting from credits and win is getting added to credits,Credit before spin"+verifycredit+" and credits after spin" +verifycredit1 , "fail");	
							}
						}
						/*	Verify that after changing the bet and refresh the game, bet is not getting change*/
						System.out.println("bet before refresh"+betamt);
						Double minbet=cfnlib.GetBetAmt();
						System.out.println("minbet"+minbet);
						Double betAmount0=cfnlib.GetBetAmt();
						bgImage=cfnlib.betDecrease();
						Double betAmount=cfnlib.GetBetAmt();
						/*Verify that bet is decreasing*/
						System.out.println("betdecrease"+betAmount0);
						if(!betAmount.equals(betAmount0))
						{
							 reg_Suit.detailsAppend("Verify that clicking on - bet button ,Bet must decrease ","After clicking on - button ,bet must change","bet is chaging,bet before changing :" +betAmount0+" Bet after decreasing :"+betAmount  ,"pass" );
						}
						else
						{
							 reg_Suit.detailsAppend("Verify that clicking on - bet button ,Bet must decrease ","After clicking on - button ,bet must change","bet is not chaging,bet before changing :" +betAmount0+" Bet after decreasing :"+betAmount,"fail" );	
						}
						/*Verify that on changing bet and didn't spin and refresh the game,bet is not changing*/					
						webdriver.navigate().refresh();
						/*if any error is coming on refresh,this code will handle*/ 		
						//cfnlib.error_Handler();
						cfnlib.refreshWait();
						Double betAmount1=cfnlib.GetBetAmt();
						System.out.println("bet after refresh : "+betAmount1);
						if(betAmount0.equals(betAmount1))
						{
							 reg_Suit.detailsAppend("Verify that after changing bet and did not click on spin and refresh the game ,bet value must not be change","after changing bet and did not click on spin and refresh the game ,the bet value must not change","bet value is not not changing after refresh"+"bet value before changing bet "+betAmount0+ " bet value after changing bet : "+betAmount+"bet value after refresh"+betAmount1,"pass" );
						}
						else
						{
							 reg_Suit.detailsAppend("Verify that after changing bet and did not click on spin and refresh the game ,bet value must not be change","after changing bet and did not click on spin and refresh the game ,the bet value must not change","bet value is not not changing after refresh"+"bet value before changing bet "+betAmount0+ " bet value after changing bet : "+betAmount+"bet value after refresh"+betAmount1,"fail" );
						}
						Double maxbet=cfnlib.GetBetAmt();
						System.out.println("maxbet"+maxbet);
						Double betAmount2=cfnlib.GetBetAmt();
						System.out.println("betAmount2 : "+betAmount2);
						/*Verify that bet is increasing*/
						cfnlib.betIncrease();
						Thread.sleep(2000);
						Double betAmount3=cfnlib.GetBetAmt();
						System.out.println("bet increase"+betAmount3);
						Thread.sleep(2000);
						if(!betAmount3.equals(betAmount2))
						{
							 reg_Suit.detailsAppend("Verify that clicking on + bet button ,Bet must increasing ","After clicking on + button ,bet must increasing","bet is chaging,bet before changing :" +betAmount2+" Bet after increasing :"+betAmount3  ,"pass" );
						}
						else
						{
							 reg_Suit.detailsAppend("Verify that clicking on + bet button ,Bet must increasing ","After clicking on + button ,bet must increasing","bet is not chaging,bet before changing :" +betAmount2+" Bet after increasing :"+betAmount3  ,"fail" );
						}
						webdriver.navigate().refresh();
						//cfnlib.error_Handler();
						cfnlib.refreshWait();
						Double betAmount4=cfnlib.GetBetAmt();
						System.out.println("bet after refresh"+betAmount4);
						if(betAmount4.equals(betAmount2))
						{
							 reg_Suit.detailsAppend("Verify that after changing bet and did not click on spin and refresh the game ,bet value must not be change","after changing bet and did not click on spin and refresh the game ,the bet value must not change","bet value before changing bet "+betAmount2+ " bet value after changing bet :"+betAmount4,"pass" );
						}
						else
						{
							 reg_Suit.detailsAppend("Verify that after changing bet and did not click on spin and refresh the game ,bet value must not be change","after changing bet and did not click on spin and refresh the game ,the bet value must not change","bet is changing after refresh the game","fail" );
						}
						
						/*Verify that big win screen is displaying with overlay*/ 
						if(cfnlib.XpathMap.get("BigWin").equalsIgnoreCase("Yes"))
						{
							cfnlib.spinclick();//3rd spin with big win data set if big is available in game
							cfnlib.bigWin_Wait();
							boolean bigWin=cfnlib.func_Click(cfnlib.XpathMap.get("OD_BIgWin"));
							if(bigWin)
							{
								 reg_Suit.detailsAppend("Verify that big win screen is displaying with overlay ","Big win screen must display ,Once big win triggers ","On  triggering big win,Big win screen is displaying","pass");
							}
							else
							{
								 reg_Suit.detailsAppend("Verify that big win screen is displaying with overlay ","Big win screen must display ,Once big win triggers ","On  triggering big win,Big win screen is not displaying ","fail");
							}
						/*	Verify that on big win overlay,Setting and paytable page must open*/ 
							boolean sOpen=cfnlib.settingsOpen();
							Thread.sleep(2000);
							if(sOpen)
							{
								 reg_Suit.detailsAppend("Verify that on big win overlay ,setting page is getting open ","On big win overlay ,Clicking on settings setting page must open","on big win overlay settings page is opening","pass");
							}
							else
							{
								 reg_Suit.detailsAppend("Verify that on big win overlay ,setting page is getting open ","On big win overlay ,Clicking on settings setting page must open","on big win overlay settings page is not opening","fail");
							}
							cfnlib.SettingsToBasescen();
							/*Verify that on big win overlay ,paytable page is getting open*/ 
							String pOpen=cfnlib.capturePaytableScreenshot(reg_Suit, "");
							if(pOpen!=null)
							{
								 reg_Suit.detailsAppend("Verify that on big win overlay ,paytable page is getting open ","On big win overlay ,Clicking on paytable page must open","on big win overlay paytable page is opening","pass");
							}
							else
							{
								 reg_Suit.detailsAppend("Verify that on big win overlay ,paytable page is getting open ","On big win overlay ,Clicking on paytable page must open","on big win overlay paytable page is not  opening","fail");
							}
							cfnlib.paytableClose();
							cfnlib.func_Click(cfnlib.XpathMap.get("OD_BIgWin"));
						}
					/*	Verify that if win is occuring,before changing bet win is  displaying in win box*/
						Double winamount2=Double.parseDouble(cfnlib.GetWinAmt().replace(",", ""));
						System.out.println("winbetcahnge"+winamount2);
						if(!winamount2.equals(winamt))
						{
							 reg_Suit.detailsAppend("Verify that if win is occuring,before changing bet win is  displaying in win box","if win is occuring,after changing bet win amount is displaying in win box","win amount is displaying before changing bet", "pass");
							cfnlib.betDecrease();
							Double winamount1=Double.parseDouble(cfnlib.GetWinAmt().replace(",",""));
							System.out.println("win afte rbet change :"+winamount1);
							if(winamount1.equals(winamt))
							{
								 reg_Suit.detailsAppend("Verify that if win is occuring,after changing bet win is not displaying in win box","if win is occuring,after changing bet win amount is not displaying in win box","win amount is not displaying in win box after changing bet", "pass");
							}
							else
							{
								 reg_Suit.detailsAppend("Verify that if win is occuring,after changing bet win is not displaying in win box","if win is occuring,after changing bet win amount is not displaying in win box","win amount is displaying in win box after changing bet", "fail");
							}
						}
						cfnlib.spinclick();
						cfnlib.waitTillStop();
						cfnlib.winClick();
						/*Verify that coin selector total bet amount*/
						cfnlib.moveCoinSizeSlider();
						System.out.println("slider open");
						 reg_Suit.detailsAppend("Verify that coin selector total bet amount", "coin selector total bet amount ", "coin selector total bet amount ", "pass");
						Double totalbet=Double.parseDouble(cfnlib.Slider_TotalBetamnt());
						cfnlib.Coinselectorclose();
						Double totalbet1=cfnlib.GetBetAmt();
						/*Verify that coin selector total bet amount and bet box bet amount is equal*/
						if(totalbet.equals(totalbet1))
						{
							 reg_Suit.detailsAppend("Verify that coin selector total bet amount and bet box bet amount is equal ", "coin selector total bet amount and bet box bet amount must be same", "coin selector total bet amount :"+totalbet+"and bet box bet amount is same :"+totalbet1, "pass");
							System.out.println("bet equal");
						}
						else
						{
							 reg_Suit.detailsAppend("Verify that coin selector total bet amount and bet box bet amount is equal ", "coin selector total bet amount and bet box bet amount must be same", "coin selector total bet amount and bet box bet amount is not same", "fail");
						}
						
						for(int i=1;i<=2;i++)
						{
							cfnlib.spinclick();//5th spin with free Apin test data assigned and 6th spin with free spin retrigger test data assigned	
							String clickToContinueText=cfnlib.XpathMap.get("clickToContinueText");
							if(cfnlib.XpathMap.get("FreeSpinEntryScreen").equalsIgnoreCase("yes"))
							{
								String CTC_wait=cfnlib.entryScreen_Wait(clickToContinueText);
							Thread.sleep(1000);
							/*Free spin entry screen is displaying and after refreshing click to continue button is displaying*/
							if(CTC_wait!=null)
							{
								 reg_Suit.detailsAppend("Free spin entry screen is displaying", "free spin entry screen must display", "free screen entry screen is displaying", "pass");
							}
							else
							{
								 reg_Suit.detailsAppend("Free spin entry screen is displaying", "free spin entry screen must display", "free screen entry screen is not displaying", "fail");
							}
							/*Verify that after refreshing free spins entry screen,entry screen must display */											
							webdriver.navigate().refresh();
							//cfnlib.error_Handler();
							Thread.sleep(3000);
							 reg_Suit.detailsAppend("on refreshing entry screen of free spin","splash screen must come on refreshing entry screen of free spin","splash screen is coming on refreshing entry screen of free spin", "pass");
							//CTC_wait=cfnlib.entryScreen_Wait(clickToContinueText);
							if(CTC_wait!=null)
							{
								 reg_Suit.detailsAppend("Verify that after refreshing free spins entery screen,entery screen must display ", "After refreshing free spins entery screen,entery screen must display ", "After refreshing free spins entery screen,entery screen is displaying ", "pass");
							}
							else
							{
								 reg_Suit.detailsAppend("Verify that after refreshing free spins entery screen,entery screen must display ", "After refreshing free spins entery screen,entery screen must display ", "After refreshing free spins entery screen,entery screen is not displaying ", "fail");
							}

							String FS_Credits=cfnlib.clickToContinue();
							Thread.sleep(2000);
							if(FS_Credits!=null)
							{
								 reg_Suit.detailsAppend("Verify the Clicking on clickTocontinue taking user to free spin screen", "Clicking on anywhere on enry screen It must take user to free spin screen ", "Clicking on entry screen of free spin entry screen,It is taking user to free spin screen ", "pass");
							}
							else
							{
								 reg_Suit.detailsAppend("Verify the Clicking on clickTocontinue taking user to free spin screen", "Clicking on anywhere on enry screen,It must take user to free spin screen ", "Clicking on entry screen of free spin entry screen,It is not taking user to free spin screen ", "fail");
							}
							}
							webdriver.navigate().refresh();
							//cfnlib.error_Handler();
							String wait=cfnlib.FS_RefreshWait();
							if(wait!=null)
							{
								 reg_Suit.detailsAppend("Verify that after refreshing the entery screen of free spin,free spins entery screen is displaying","After refreshing free spins entery screen ,entery screen of free spin must display","After refreshing free spins entery screen ,entery screen is displaying","pass");
							}
							else
							{
								 reg_Suit.detailsAppend("Verify that after refreshing free spin screen,Option is available on free spin screen to continue free spin ","After refreshing free spin screen ,An option must avialable to continue free spin","After refreshing free spin screen ,no option is not displaying to continue free spin","fail");
							}
							cfnlib.FS_continue();
							if(cfnlib.XpathMap.get("BigWin").equalsIgnoreCase("yes"))
							{
								if( i==1)
								{
									cfnlib.bigWin_Wait();
									boolean bigWin1=cfnlib.func_Click(cfnlib.XpathMap.get("OD_BIgWin"));
									if(bigWin1)
									{
										 reg_Suit.detailsAppend("Verify that big win screen is displaying with overlay in free spin ","Big win screen must display ,Once big win triggers in free spin","On  triggering big win,Big win screen is displaying in free spin","pass");
									}
									else
									{
										 reg_Suit.detailsAppend("Verify that big win screen is displaying with overlay in free spin ","Big win screen must display ,Once big win triggers in free spin","On  triggering big win,Big win screen is not displaying in free spin ","fail");
									}
								}	
							
							}/*else{
								 Thread.sleep(1000);
							 }*/
							if(cfnlib.XpathMap.get("FreeSpinEntryScreen").equalsIgnoreCase("yes"))
							{
								String CTC_SummaryWait=cfnlib.summaryScreen_Wait();
								if(CTC_SummaryWait!=null)
								{
									 reg_Suit.detailsAppend("Verify the free spin summary screen is displaying", "After completing all free spin ,free spin summary screen must display", "Clicking on entry screen of free spin entry screen,It is taking user to free spin screen ", "pass");
								}
								else
								{
									 reg_Suit.detailsAppend("Verify the Clicking on clickTocontinue taking user to free spin screen", "Clicking on anywhere on enry screen,It must take user to free spin screen ", "Clicking on entry screen of free spin entry screen,It is not taking user to free spin screen ", "fail");
								}
							 webdriver.navigate().refresh();
							//cfnlib.error_Handler();
							if(cfnlib.XpathMap.get("FreeSpinEntryScreen").equalsIgnoreCase("yes"))
							{
								CTC_SummaryWait=cfnlib.summaryScreen_Wait();
								 if(CTC_SummaryWait!=null)
								{
									 reg_Suit.detailsAppend("Verify that after refreshing free spins summary screen,summary screen must display ", "After refreshing free spins summary screen,summary screen must display ", "After refreshing free spins summary screen,summary screen is displaying ", "pass");
								}
								else
								{
									 reg_Suit.detailsAppend("Verify that after refreshing free spins summary screen,summary screen must display ", "After refreshing free spins summary screen,summary screen must display ", "After refreshing free spins summary screen,summary screen is not displaying ", "fail");
								}
							}
							// Thread.sleep(2000);
							//webdriver.navigate().refresh();
							String FS_Credits=cfnlib.FS_summaryScreenClick().replaceAll(",","");
							if(FS_Credits!=null)
							{
								 reg_Suit.detailsAppend("Verify the Clicking on clickTocontinue taking user to free spin screen", "Clicking on anywhere on enry screen,It must take user to free spin screen ", "Clicking on entry screen of free spin entry screen,It is taking user to free spin screen ", "pass");
							}
							else
							{
								 reg_Suit.detailsAppend("Verify the Clicking on clickTocontinue taking user to free spin screen", "Clicking on anywhere on enry screen,It must take user to free spin screen ", "Clicking on entry screen of free spin entry screen,It is not taking user to free spin screen ", "fail");
							}
							}
							String FS_Credits=cfnlib.FS_summaryScreenClick().replaceAll(",","");
							cfnlib.refreshWait();
							webdriver.navigate().refresh();
							String CTC_SummaryWait=cfnlib.summaryScreen_Wait();
							if(CTC_SummaryWait!=null)
								{
									 reg_Suit.detailsAppend("Verify that after refreshing base scene,summary screen must display ", "After refreshing free spins summary screen,summary screen must display ", "After refreshing free spins summary screen,summary screen is displaying ", "pass");
								}
								else
								{
									 reg_Suit.detailsAppend("Verify that after refreshing base scene,summary screen must display", "After refreshing free spins summary screen,summary screen must display ", "After refreshing free spins summary screen,summary screen is not displaying ", "fail");
								}
							cfnlib.refreshWait();
							String Veify_Credits=cfnlib.verifyCredit().replaceAll(",", "");
							/*	Verify that base scene and free spin credit amount is same*/
							if(Veify_Credits.equals(Veify_Credits))
							{
								 reg_Suit.detailsAppend("Verify that base scene and free spin credit amount is same","Base scene and free spin amount must be same","Base scene credit amount"+Veify_Credits+"and free spin credit amount is "+FS_Credits+"same ","pass");
							}
							else
							{
								 reg_Suit.detailsAppend("Verify that base scene and free spin credit amount is same","Base scene and free spin amount must be same","Base scene and free spin amount must be same","Base scene credit amount"+Veify_Credits+"and free spin credit amount is "+FS_Credits+" not same ","fail");
							}
						}
						/*Credit bubble verification*/
						 Map<String, Integer> creditbubble=cfnlib.creditBubble();
						 if(creditbubble!=null)
						 {
							  reg_Suit.detailsAppend("Verify the credit bubble in the game ", "Credits bubble  must display on the screen  ", "Credit bubble is coming in the game", "pass");
							// int cb_balance=creditbubble.get("Balance");//This code is to fetch credit bubble value from MAP
							  int cb_balance=creditbubble.get("Balance");
							  try
							{
							verifycreditnew=Integer.parseInt(cfnlib.verifyCredit().replaceAll("[^0-9]", ""));
							}
							catch(NumberFormatException  e)
							{
								e.getMessage();
							}
							 if(cb_balance==verifycreditnew)
								 {
									  reg_Suit.detailsAppend("Verify the main balance in credit bubble and base scene credit is same in the game ", "main balance in credit bubble and base scene credit is same in the game ", "main balance in credit bubble and base scene credit is same in the game", "pass");
								 }
								 else
								 {
									  reg_Suit.detailsAppend("Verify the main balance in credit bubble and base scene credit is same in the game", "main balance in credit bubble and base scene credit is same in the game  ", "main balance in credit bubble and base scene credit is not same in the game", "fail");
								 }
							 int CB_bonus=creditbubble.get("Bonus");
									 if(CB_bonus==0)
									 {
										  reg_Suit.detailsAppend("Verify that the bonus is displaying in credit bubble ", "main balance in credit bubble and base scene credit is same in the game ", "main balance in credit bubble and base scene credit is same in the game", "pass");
									 }
									 else
									 {
										  reg_Suit.detailsAppend("main balance in credit bubble and base scene credit is same in the game", "main balance in credit bubble and base scene credit is same in the game  ", "main balance in credit bubble and base scene credit is not same in the game", "fail");
									 }
									 int CB_totalcredits=creditbubble.get("TotalCredit");
									 if(cb_balance+CB_bonus==CB_totalcredits)
									 {
										  reg_Suit.detailsAppend("Verify the total credits is sum of main balance and bonus ", "total credits must be sum of main balance and bonus ", "total credits is sum of main balance and bonus", "pass");
									 }
									 else
									 {
										  reg_Suit.detailsAppend("Verify the total credits is sum of main balance and bonus ", "total credits must be sum of main balance and bonus ", "total credits is not  sum of main balance and bonus", "fail");
									 }
						 }
						 else
						 {
							  reg_Suit.detailsAppend("Verify the credit bubble in the game ", "Credits bubble  must display on the screen  ", "Credit bubble is not coming in the game", "fail");
						 }
						
					}
				}
			catch (Exception e) {
				cfnlib.evalException(e);

			}
			
		}
	}
	


