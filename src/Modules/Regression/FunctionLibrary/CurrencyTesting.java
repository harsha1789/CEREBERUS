
package Modules.Regression.FunctionLibrary;





import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CurrencyTesting {
	
	 public static void main(String[] args)
	 { 
		 
		 System.setProperty ("webdriver.chrome.driver","D:\\Automation\\ZAF_JAR\\Project_Jars\\chromedriver.exe" );
		 WebDriver driver=new ChromeDriver();

		 JavascriptExecutor js = ((JavascriptExecutor)driver);
		 
		 WebDriverWait wait= new WebDriverWait(driver, 50);
		 int bonuscount=1;
		try{ 
		 String Username="Zen_USD";
	
		 String Url="https://mobilewebserver9-zensarqa2.installprogram.eu/MobileWebGames/game/mgs/9_15_0_7290?moduleID=10145&clientID=50300&gameName=immortalRomanceDesktop&gameTitle=Immortal%20Romance&LanguageCode=en&clientTypeID=40&casinoID=5007&lobbyName=IslandParadise&loginType=InterimUPE&lobbyURL=https://mobilewebserver9-ZensarQA2.installprogram.eu/Lobby/en/IslandParadise/Games/3ReelSlots&xmanEndPoints=https://mobilewebserver9-ZensarQA2.installprogram.eu/XMan/x.x&bankingURL=https://mobilewebserver9-ZensarQA2.installprogram.eu/Lobby/en/IslandParadise/Banking&routerEndPoints=&isPracticePlay=false&username="+Username+"&password=test&host=Desktop&apicommsenabled=false&launchtoken=&version=";

		
		driver.navigate().to(Url);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='continueButton continueButtonFadeIn']")));
		 
		 //new feature click
		 driver.findElement(By.xpath("//*[@class='continueButton continueButtonFadeIn']")).click();
		 Thread.sleep(6000);
		 
			js.executeScript(("return mgs.mobile.casino.slotbuilder.v1.automation.getControlById('SpinButtonComponent').onButtonClicked('spinButton')"));
		
			while(bonuscount<5)
			{
				clickBonusSelection(bonuscount, driver);
				
				
				while(true){
					String currentScene = (String) js.executeScript("return mgs.mobile.casino.slotbuilder.v1.automation.currentScene");
					if(currentScene!=null && (currentScene.equalsIgnoreCase("FREESPINS_COMPLETE")|| currentScene.equalsIgnoreCase("FREESPINS_COMPLETE_AMBER")|| currentScene.equalsIgnoreCase("FREESPINS_COMPLETE_TROY")||  currentScene.equalsIgnoreCase("FREESPINS_COMPLETE_MICHAEL")|| currentScene.equalsIgnoreCase("FREESPINS_COMPLETE_SARAH")) ){
						break;
					}
				
				}
			
			}
			/*while(true){
				String currentScene = (String) js.executeScript("return mgs.mobile.casino.slotbuilder.v1.automation.currentScene");
				if(currentScene!=null && (currentScene.equalsIgnoreCase("FREESPINS_COMPLETE")|| currentScene.equalsIgnoreCase("FREESPINS_COMPLETE_AMBER")|| currentScene.equalsIgnoreCase("FREESPINS_COMPLETE_TROY")||  currentScene.equalsIgnoreCase("FREESPINS_COMPLETE_MICHAEL")|| currentScene.equalsIgnoreCase("FREESPINS_COMPLETE_SARAH")) ){
					break;
				}
				
			}*/
			
			js.executeScript(("return mgs.mobile.casino.slotbuilder.v1.automation.getControlById('SpinButtonComponent').onButtonClicked('spinButton')"));

				
		
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
			driver.close();
			driver.quit();
		}
	
	 }

	 public String getConsoleText(String text, WebDriver driver) {
			String consoleText = null;
			try {
				JavascriptExecutor js = ((JavascriptExecutor) driver);
				consoleText = (String) js.executeScript(text);
			} catch (Exception e) {
				e.getMessage();
			}
			return consoleText;
		}

	 public static  void clickAtButton(String button,WebDriver driver)
		{
			JavascriptExecutor js = ((JavascriptExecutor)driver);
			js.executeScript(button);
		}
	 
	 public  static void clickBonusSelection(int bonusCount,WebDriver driver)
		{
		
			try
			{

				 WebDriverWait wait= new WebDriverWait(driver, 50);
				 wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inGameClock")));
				
				if(bonusCount==1)
				{
					//clicking by hooks
					clickAtButton("return mgs.mobile.casino.slotbuilder.v1.automation.getControlById('BonusSelectionComponent').Sprites.fsselectionAmberActive.events.onInputUp.dispatch(mgs.mobile.casino.slotbuilder.v1.automation.getControlById('BonusSelectionComponent').Sprites.fsselectionAmberActive)",driver);
					}
				else if(bonusCount==2)
				{
					//clicking by hooks
					clickAtButton("return mgs.mobile.casino.slotbuilder.v1.automation.getControlById('BonusSelectionComponent').Sprites.fsselectionTroyActive.events.onInputUp.dispatch(mgs.mobile.casino.slotbuilder.v1.automation.getControlById('BonusSelectionComponent').Sprites.fsselectionTroyActive)",driver);

					}
				else if(bonusCount==3)
				{
					//clicking by hooks
					clickAtButton("return mgs.mobile.casino.slotbuilder.v1.automation.getControlById('BonusSelectionComponent').Sprites.fsselectionMichaelActive.events.onInputUp.dispatch(mgs.mobile.casino.slotbuilder.v1.automation.getControlById('BonusSelectionComponent').Sprites.fsselectionMichaelActive)",driver);

					}
				else if(bonusCount==4)
				{
					//clicking by hooks
					clickAtButton("return mgs.mobile.casino.slotbuilder.v1.automation.getControlById('BonusSelectionComponent').Sprites.fsselectionSarahActive.events.onInputUp.dispatch(mgs.mobile.casino.slotbuilder.v1.automation.getControlById('BonusSelectionComponent').Sprites.fsselectionSarahActive)",driver);

					}
				Thread.sleep(1000);
			}
			catch(Exception e)
			{
				e.getMessage();
			}
			
}
	 
	
	 
	 
}