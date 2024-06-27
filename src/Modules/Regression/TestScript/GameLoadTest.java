package Modules.Regression.TestScript;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.ImmutableMap;
import com.zensar.automation.model.GameLoad;


/**
 * @author AK47374
 *
 */
public class GameLoadTest {

	public static void main(String[] args) {
		String path = System.getProperty("user.dir");
		System.out.println("Working Directory = " + path);
			
		Scanner sc= new Scanner(System.in);
		
		System.out.print("Enter Game URL :"); 
		String url=sc.nextLine();
		
		/*System.out.print("Enter a network Throtling type(Slow3G/Fast3G): "); 
		String networkThrottling=sc.nextLine();*/  
		
		System.out.print("Enter chrome driver exe path :"); 	
		String exePath=sc.nextLine();
		
		System.out.print("Enter a waitType type(clock/Continue button): "); 
		String gameWait=sc.nextLine(); 
		
		boolean isClockGame = gameWait.equalsIgnoreCase("clock");
		String continueBtnXpath=null;
		if(!isClockGame) {
			System.out.print("Enter the xpath of Continue button): "); 
			 continueBtnXpath=sc.nextLine(); 
		}
		
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
			
		System.out.print("Enter a Number of time you want to run : "); 	
		int noOfTests=sc.nextInt();
		
		
		
		System.setProperty("webdriver.chrome.driver",exePath);	//"D:\\ChromeDriver86\\chromedriver.exe"
		
		
		
		int overallGameLoadSize=0;
		int overallGameRequestCnt=0;
		int overallnonGameReqCount=0;
		long overallGameloadTime=0;
		int j=0;
		//List<Double> list = new ArrayList<Double>(); 
		List<GameLoad> gameLoadList =new ArrayList<>();		
		
		
		for(int count=0 ;count<noOfTests ;count++){	
		int flag=0;						
		long gameLoadSize=0;
		int gameRequestCnt=0;
		int nonGameReqCount=0;		
		
		DesiredCapabilities cap = DesiredCapabilities.chrome();

		LoggingPreferences logPrefs = new LoggingPreferences();
		logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
		cap.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);

		cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		cap.setPlatform(org.openqa.selenium.Platform.WINDOWS);

		ChromeOptions options = new ChromeOptions();
		options.addArguments("disable-infobars");
		//options.addArguments("--disable-web-security");
		options.addArguments("--start-maximized");
		options.addArguments("--ignore-certificate-errors");
		Map<String, Object> prefs = new HashMap<>();
		prefs.put("credentials_enable_service", false);
		prefs.put("profile.password_manager_enabled", false);
		options.setExperimentalOption("prefs", prefs);
		options.setExperimentalOption("useAutomationExtension", false);
		options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
		options.addArguments("--start-maximized");
		options.setCapability( "goog:loggingPrefs", logPrefs );
		cap.setCapability(ChromeOptions.CAPABILITY, options);
		
		WebDriver driver = new ChromeDriver(cap);
		Map map = new HashMap();
		
		
		map.put("offline", false);
    	map.put("latency", 15);
    	map.put("download_throughput",10000000);
    	map.put("upload_throughput", 10000000);
		
		/*if(networkThrottling.equalsIgnoreCase("Slow3G")){
    	map.put("offline", false);
    	map.put("latency", 2000);
    	map.put("download_throughput",500 * 1024 / 8 * .8);
    	map.put("upload_throughput", 500 * 1024 / 8 * .8);
    	
		}else if(networkThrottling.equalsIgnoreCase("Fast3G")){
			map.put("offline", false);
	    	map.put("latency", 150 * 3.75);
	    	map.put("download_throughput",1.6 * 1024 * 1024 / 8 * .9);
	    	map.put("upload_throughput", 750 * 1024 / 8 * .9);
		}*/
				
		System.out.println("Download speed :: "+map.get("download_throughput"));
    	System.out.println("upload speed :: "+map.get("upload_throughput"));
		try {

			CommandExecutor executor = ((ChromeDriver) driver).getCommandExecutor();

			Response response = executor.execute(new Command(((ChromeDriver) driver).getSessionId(),
					"setNetworkConditions", ImmutableMap.of("network_conditions", ImmutableMap.copyOf(map))));
		} catch (IOException e) {

			Throwable cause = e.getCause();
			String message = e.getLocalizedMessage();
			e.printStackTrace();
		}
		//String url="https://mobilewebserver9-zensarqa.installprogram.eu/MobileWebGames/game/mgs/10_2_5_7840?moduleID=15056&clientID=50300&gameName=megaMoolahGoddessDesktop&gameTitle=Mega%20Moolah%20Goddess&LanguageCode=en&clientTypeID=40&casinoID=5007&lobbyName=IslandParadise&loginType=InterimUPE&lobbyURL=https://mobilewebserver9-ZensarQA.installprogram.eu/Lobby/en/IslandParadise/Games/3ReelSlots&xmanEndPoints=https://mobilewebserver9-ZensarQA.installprogram.eu/XMan/x.x&bankingURL=https://mobilewebserver9-ZensarQA.installprogram.eu/Lobby/en/IslandParadise/Banking&routerEndPoints=&isPracticePlay=false&username=player901&password=test&host=Desktop&apicommsenabled=true&launchtoken=&version=&gameversion=megaMoolahGoddessDesktop_TheForce_2_0_0_944";
		double startWalltime=0;
		double endWalltime=0;
		double startTimeStamp=0;
		double endTimeStamp=0;
	   // System.out.println("Time in milliseconds: " + timeMilli);
		System.out.println("Navigate to " + url);
		WebDriverWait wait = new WebDriverWait(driver,300);
		driver.manage().deleteAllCookies();	
		wait.pollingEvery(Duration.ofMillis(100));
		driver.navigate().to(url);	
		if(!isClockGame){
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(continueBtnXpath)));
			endWalltime=System.currentTimeMillis();
		}else{
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='inGameClock']")));
		}
		//	
		Date date1 = new Date();
		long timeMilli1 = date1.getTime();
	    System.out.println("Time in milliseconds: " + timeMilli1);
	    if(isClockGame)//networkThrottling.equalsIgnoreCase("Slow3G"))
	    {
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	    }/*else if(!isClockGame&&networkThrottling.equalsIgnoreCase("Slow3g")){
	    	try {
				Thread.sleep(3000);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
	    }*/
	    
	    
		// then ask for all the performance logs from this request
		// one of them will contain the Network.responseReceived method
		// and we shall find the "last recorded url" response
		LogEntries logs = driver.manage().logs().get("performance");
		
		driver.quit();
		int status = -1;

		System.out.println("\nList of log entries:\n");

		FileOutputStream fos=null;
		FileOutputStream fosLength=null;
		FileOutputStream fosReq=null;
		
		try {
			File harFile = new File(path+"\\_"+count+".txt");
			fos = new FileOutputStream(harFile);
			
			
			File harFileLength = new File(path+"\\_"+count+"_Legth.txt");
			fosLength = new FileOutputStream(harFileLength);
			
			File reqFile = new File(path+"\\_"+count+"_reqFile.txt");
			fosReq = new FileOutputStream(reqFile);
			
			String radarJsRequestId=null;

			for (Iterator<LogEntry> it = logs.iterator(); it.hasNext();) {
				LogEntry entry = it.next();

				try {
					JSONObject json = new JSONObject(entry.getMessage());

					//System.out.println(json.toString());
					fos.write("\n".getBytes());//
					fos.write(json.toString().getBytes());
					JSONObject message = json.getJSONObject("message");
					String method = message.getString("method");

					if (method != null && "Network.responseReceived".equals(method)) {
																					
						JSONObject params = message.getJSONObject("params");						
						String requestId= params.getString("requestId");
						JSONObject response = params.getJSONObject("response");
						String messageUrl = response.getString("url");
						status= response.getInt("status");
						JSONObject headers = response.getJSONObject("headers");																	
						long contentLengthL=0;
						if(headers.has("Content-Length")){
							contentLengthL = headers.getLong("Content-Length");
						}
						long encodedDataLengthL=0;
						
						if(response.has("encodedDataLength")){
							encodedDataLengthL = response.getLong("encodedDataLength");
						}
						
						
						int index = messageUrl.lastIndexOf("/");
						
						if(index>0){
							messageUrl = messageUrl.substring(index+1,messageUrl.length());
						}
						fos.write(( "encodedDataLengthL : "+encodedDataLengthL +" Header Content-Length"+contentLengthL +" Resource ::"+ messageUrl + "  Status : " + status ).getBytes() );
						fos.write("\n".getBytes());
						
						fosLength.write(("Request Id : "+requestId + "encodedDataLengthL : "+encodedDataLengthL +" Header Content-Length"+contentLengthL +" Resource ::"+ messageUrl + "  Status : " + status ).getBytes() );
						fosLength.write("\n".getBytes());
						
						if(isClockGame&&messageUrl.endsWith("radar.js")&&status==200){
							radarJsRequestId=requestId;
							endWalltime=((Double)response.getDouble("responseTime")).doubleValue();
						}
						endTimeStamp= ((Double)params.getDouble("timestamp")).doubleValue();
						
					}else if (method != null && "Network.loadingFinished".equals(method)) {
						JSONObject params = message.getJSONObject("params");
						String requestId= params.getString("requestId");
						long encodedDataLengthL=0;
						
						if(params.has("encodedDataLength")){
							encodedDataLengthL = params.getLong("encodedDataLength");
						}
						gameLoadSize+=encodedDataLengthL;
						fosLength.write(("Request Id : "+requestId + "encodedDataLengthL : "+encodedDataLengthL  ).getBytes() );
						fosLength.write("\n".getBytes());	
						if(isClockGame&&requestId.equalsIgnoreCase(radarJsRequestId)){
							endTimeStamp= ((Double)params.getDouble("timestamp")).doubleValue();
							break;
						}
						endTimeStamp= ((Double)params.getDouble("timestamp")).doubleValue();
					}
					else if(method != null && "Network.requestWillBeSent".equals(method))							
					{
						flag++;						
						JSONObject params = message.getJSONObject("params");
						//list.add(params.getDouble("wallTime"));
						JSONObject request = params.getJSONObject("request");
						String messageUrl = request.getString("url");
						if(messageUrl.equalsIgnoreCase(url)){
							startTimeStamp= ((Double)params.getDouble("timestamp")).doubleValue();
							startWalltime=((Double)params.getDouble("wallTime")).doubleValue();
							
							gameLoadSize=0;
							gameRequestCnt=0;
							nonGameReqCount=0;
							System.out.println("URL matched");
						}
						fosReq.write(("Request Url :"+messageUrl).getBytes());
						fosReq.write("\n".getBytes());	
						if(messageUrl.contains("mgs")||messageUrl.contains("MGS")||messageUrl.contains("Mgs")){
							gameRequestCnt++;
						}else{
							nonGameReqCount++;
						}						
					}					
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		} catch (FileNotFoundException e1) {
			driver.quit();
			e1.printStackTrace();
		}finally{
			driver.quit();
			/*System.out.println(" Start wall time in milli secs ::"+startWalltime);
			System.out.println(" End wall time in milli secs   ::"+endWalltime);
			//System.out.println(" Total load time               ::"+(endWalltime-startWalltime));
			
			DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z"); 
	        Date startTime = new Date(Math.round(startWalltime)); 
	        Date endTime = new Date(Math.round(endWalltime)); 
	        
	       // System.out.println(" Start wall time in       secs ::"+startTime);
	       // System.out.println(" End wall time in       secs   ::"+endTime);
*/	        
			//long gameLoadTime = Math.round(endWalltime-startWalltime);
	        System.out.println("startTimeStamp :: "+startTimeStamp);
	        System.out.println("endTimeStamp :: "+endTimeStamp);
	        long gameLoadTime = Math.round(endTimeStamp-startTimeStamp);
	        System.out.println("gameLoadTime :: "+gameLoadTime);
			GameLoad gameLoad= new GameLoad();
		
			gameLoad.setGameLoadSize(gameLoadSize);
			gameLoad.setGameLoadTime(gameLoadTime);
			gameLoad.setGameRequestCount(gameRequestCnt);
			gameLoad.setNonGameRequestCount(nonGameReqCount);
			gameLoad.setTotalRequestCount(gameRequestCnt+nonGameReqCount);
			
			gameLoadList.add(gameLoad);
			
			
			
			/*Double first = list.get(0); 
			Double last = list.get(list.size() - 1);
			Double WallTimeDiff = (last-first);*/
						
			System.out.println("Overall Game Load Size ::" + gameLoadSize+" bytes");
			System.out.println("Overall Game Load Size ::" + ((float)gameLoadSize/(1024*1024)) + " MB");
			//System.out.println("Overall Game Load Time ::" + WallTimeDiff);
			
			overallGameLoadSize+=gameLoadSize;
			overallGameRequestCnt+=gameRequestCnt;
			overallnonGameReqCount+=nonGameReqCount;
			overallGameloadTime+=gameLoadTime;
			
			//System.out.println("total flag request :: " +flag);
			System.out.println("Game request count ::" + gameRequestCnt);
			System.out.println("NonGame request count ::"+nonGameReqCount);
			System.out.println("Total count ::" + (gameRequestCnt+nonGameReqCount));
																	
				if(fos!=null){
				try {									
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
				
				if(fosLength!=null){
					try {
						fosLength.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
			}		
				
				if(fosReq!=null){
					try {
						fosReq.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
			}		
		}
		}	
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Results");
		
		Row row1 = sheet.createRow(0);
		Cell cell6 = row1.createCell(0);	
		Cell cell7 = row1.createCell(1);
		Cell cell8 = row1.createCell(2);
		Cell cell9 = row1.createCell(3);
		Cell cell10 = row1.createCell(4);
		Cell cell11 = row1.createCell(5);
		cell6.setCellValue("Take");
		cell7.setCellValue("Overall Game Load Size");
		cell8.setCellValue("Overall Game Load time");
		cell9.setCellValue("Game request count");
		cell10.setCellValue("NonGame request count");
		cell11.setCellValue("Total count");
		int i=0;
		for(GameLoad gameLoad: gameLoadList){
			Row row = sheet.createRow(i+1);
			Cell cell = row.createCell(0);		
			Cell cell1 = row.createCell(1);
			Cell cell2 = row.createCell(2);
			Cell cell3 = row.createCell(3);
			Cell cell4 = row.createCell(4);
			Cell cell5 = row.createCell(5);
			System.out.println("\n");
			cell.setCellValue("Take"+(i+1));
			cell1.setCellValue(((double)gameLoad.getGameLoadSize()/(1024*1024))+ " MB");
			cell2.setCellValue(((double)gameLoad.getGameLoadTime())+ " Sec");
			cell3.setCellValue(gameLoad.getGameRequestCount());
			cell4.setCellValue(gameLoad.getNonGameRequestCount());
			cell5.setCellValue(gameLoad.getTotalRequestCount());
			i++;
			j=i+2;
		}
		Row row2 = sheet.createRow(j);
		Cell cell12 = row2.createCell(0);	
		Cell cell13 = row2.createCell(1);
		Cell cell14 = row2.createCell(2);
		Cell cell15 = row2.createCell(3);
		Cell cell16 = row2.createCell(4);
		cell12.setCellValue("Average Game size");
		cell13.setCellValue("Average NonGame request count");
		cell14.setCellValue("Average Game request count");
		cell15.setCellValue("Average Total count");
		cell16.setCellValue("Average Load time");
		Row row = sheet.createRow(j+1);
		Cell cell = row.createCell(0);		
		Cell cell1 = row.createCell(1);
		Cell cell2 = row.createCell(2);
		Cell cell3 = row.createCell(3);
		Cell cell4 = row.createCell(4);
		cell.setCellValue(((double)overallGameLoadSize/(noOfTests*1024*1024))+ " MB");
		cell1.setCellValue((double)overallnonGameReqCount/noOfTests);
		cell2.setCellValue((double)overallGameRequestCnt/noOfTests);
		cell3.setCellValue((double)(overallGameRequestCnt+overallnonGameReqCount)/noOfTests);	
		cell4.setCellValue(((double)(overallGameloadTime)/noOfTests)+ " Secs");													
		 //Iterate over data and write to sheet
		try {
	        FileOutputStream out = 
	        new FileOutputStream(new File(path+"\\"+timeStamp+".xls"));
	        workbook.write(out);
	        out.close();		        
}
	    catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
}
}
	
