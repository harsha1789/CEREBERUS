package com.zensar.automation.library;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.zensar.automation.framework.api.ImageAPI;
import com.zensar.automation.framework.api.ImageDesktopAPI;
import com.zensar.automation.framework.api.ImageMobileAPI;
import com.zensar.automation.framework.library.ImageDetailsReader;
import com.zensar.automation.framework.library.LanguageReader;
import com.zensar.automation.framework.model.Image;
import com.zensar.automation.framework.model.OrientationImageDetails;
import com.zensar.automation.framework.report.Mobile_HTML_Report;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public class ImageLibrary 
{

	WebDriver dr = null;
	String gameName = null;
	public String assetsPath = null;
	public String extentScreenShotPath=null;
	Map<String, String> gameAssets = null;
	ImageAPI  imgapi = null;
	public WebDriver webdriver;

	OrientationImageDetails portraitDetails = new OrientationImageDetails();
	OrientationImageDetails landscapeDetails = new OrientationImageDetails();
	OrientationImageDetails desktopDetails = new OrientationImageDetails();

	Map<String, Image> portraitImageMap = new HashMap<String, Image>();
	Map<String, Image> landscapeImageMap = new HashMap<String, Image>();
	Map<String, Image> desktopImageMap = new HashMap<String, Image>();
	String screenOrientation;
	ImageDetailsReader imageDetailsReader ;
	String deviceType =null;
	public String getScreenOrientation() {
		return screenOrientation;
	}

	public void setScreenOrientation(String screenOrientation) {
		this.screenOrientation = screenOrientation;
	}

	public ImageLibrary(WebDriver dr, String gameName,String deviceType) 
	{

		this.dr = dr;
		this.gameName = gameName;
		this.deviceType=deviceType;
		assetsPath = "D:\\ImageComparision\\" + gameName + "\\";
		String path=System.getProperty("user.dir");
		extentScreenShotPath=path+"\\output\\"+gameName+deviceType+(new CommonUtil()).createTimeStampStr();
		imageDetailsReader = new ImageDetailsReader();
		if(deviceType.equals("Mobile")){
			
			AppiumDriver<WebElement> driver= null;
			if (dr instanceof AndroidDriver)
			{
				driver = (AndroidDriver)dr;
			} else if (dr instanceof IOSDriver) {
				 driver = (IOSDriver)dr;
			}
			
			screenOrientation = driver.getOrientation().toString().toLowerCase();
			portraitDetails=imageDetailsReader.readImageDetails(ScreenOrientation.PORTRAIT, gameName);
			portraitImageMap=portraitDetails.getImagesMap();
			landscapeDetails=imageDetailsReader.readImageDetails(ScreenOrientation.LANDSCAPE, gameName);
			landscapeImageMap=landscapeDetails.getImagesMap();
			imgapi = new ImageMobileAPI(driver, gameName,portraitDetails.getDimension(),landscapeDetails.getDimension());
		}
		if(deviceType.equals("Desktop")){
			desktopDetails=imageDetailsReader.readImageDetails("Desktop", gameName);
			desktopImageMap=desktopDetails.getImagesMap();
			screenOrientation="desktop";
			imgapi = new ImageDesktopAPI(dr, gameName,desktopDetails.getDimension());
		}
	}

	public String getText(String Imageid){

		Image imageDetails = getImageDetails(screenOrientation, Imageid);

		String imageText = imgapi.getText(imageDetails);

		return imageText;

	}

	public String getTextByCoordinates(String Imageid,String language){

		Image imageDetails = getImageDetails(screenOrientation, Imageid);

		String imageText = imgapi.getTextByCoordinates(imageDetails,language);

		return imageText;

	}

	public String getCreditInfo() {

		//ScreenOrientation screenOrientation = dr.getOrientation();
		/*Image creditImageDetails = getImageDetails(screenOrientation, "Credits");

		String creditText = imgapi.getText(creditImageDetails);*/

		String creditText = getText("Credits");

		return creditText;
	}

	public boolean clickSpin() throws InterruptedException, IOException, URISyntaxException {
		boolean status = false;

		Image spinImageDetails = getImageDetails(screenOrientation, "Spin");
		status = imgapi.click(spinImageDetails);

		return status;
	}

	public boolean clickFreeGameInfo()  {
		boolean status = false;

		Image spinImageDetails = getImageDetails(screenOrientation, "FreeGamesInfo");
		status = imgapi.click(spinImageDetails);

		return status;
	}

	public boolean clickFreeGamePlayNow()  {
		boolean status = false;

		Image spinImageDetails = getImageDetails(screenOrientation, "FreeGamesPlayNow");
		status = imgapi.click(spinImageDetails);

		return status;
	}

	public boolean clickFreeGameMenu()  {
		boolean status = false;

		Image spinImageDetails = getImageDetails(screenOrientation, "FreeGamesMenu");
		status = imgapi.click(spinImageDetails);

		return status;
	}

	public boolean clickFreeGamePlayLater()   {
		boolean status = false;

		Image spinImageDetails = getImageDetails(screenOrientation, "FreeGamesPlayLater");
		status = imgapi.click(spinImageDetails);

		return status;
	}

	public boolean clickFreeGameDiscard()  {
		boolean status = false;

		Image discardImageDetails = getImageDetails(screenOrientation, "FreeGamesDiscard");
		status = imgapi.click(discardImageDetails);

		return status;
	}

	public String getFreeGamesAbout() {

		/*Image freeGamesCongratulations = getImageDetails(screenOrientation, "FreeGamesAbout");

		String CongratulationText = imgapi.getText(freeGamesCongratulations);*/

		String aboutFreeGames = getText("FreeGamesAbout");

		return aboutFreeGames;
	}

	public String getFreeGamesCongratulationsInfo() {


		String freegameCongratulationText= getText("FreeGamesCongratulations"); 

		return freegameCongratulationText;
	}

	public boolean isImagesColliding() {

		Image firstImage = getImageDetails(screenOrientation, "FreeGamesMenu");

		Image secondImage = getImageDetails(screenOrientation, "FreeGamesInfo");

		boolean isImagesColliding = imgapi.isImagesCollide(firstImage, secondImage);

		return isImagesColliding;
	}



	public String getFreeGamesCongratulationsInfo(String language) {

		/*Image freeGamesCongratulations = getImageDetails(screenOrientation, "FreeGamesCongratulations");

		String CongratulationText = imgapi.getTextByCoordinates(freeGamesCongratulations,language);*/

		String congratulationText = getTextByCoordinates("FreeGamesCongratulations",language);

		return congratulationText;
	}

	public String getFreeGamesPlayLaterInfo(String language) {


		String playLaterText = getTextByCoordinates("FreeGamesPlayLaterText",language);

		return playLaterText;
	}

	public String getFreeGamesPlayNowInfo(String language) {


		String freeGamesPlayNow = getTextByCoordinates("FreeGamesPlayNowText",language);

		return freeGamesPlayNow;
	}

	public String getFreeGamesInfo() {

		Image freeGamesinfo= getImageDetails(screenOrientation, "FreeGamesInfo");

		String freeGamesinfoText = imgapi.getText(freeGamesinfo);

		return freeGamesinfoText;
	}

	public String getFreeGamesPlayLaterInfo() {

		String playLaterText = getText("FreeGamesPlayLaterText");

		return playLaterText;

	}

	public String getFreeGamesPlayNowInfo() {

		String freeGamesPlayNowText = getText("FreeGamesPlayNowText");

		return freeGamesPlayNowText;
	}

	public boolean clickMenu() throws InterruptedException, IOException, URISyntaxException {
		boolean status = false;


		Image menuImageDetails = getImageDetails(screenOrientation, "Menu");
		status = imgapi.click(menuImageDetails);

		return status;
	}

	public String getBetInfo() {

		Image creditImageDetails = getImageDetails(screenOrientation, "Bet");

		String creditText = imgapi.getText(creditImageDetails);

		return creditText;
	}

	public boolean waitTillSpinBtnVisible() {
		boolean status = false;

		Image spinImageDetails = getImageDetails(screenOrientation, "Spin");
		status = imgapi.waitTill(spinImageDetails);

		return status;
	}

	public boolean waitTillFreeGamesVisible() {
		boolean status = false;

		Image spinImageDetails = getImageDetails(screenOrientation, "FreeGamesCongratulations");
		status = imgapi.waitTill(spinImageDetails);

		return status;
	}

	public String getImageAsEncodedString(String path) {
		String imgEncodeString = null;
		File refImgFile = new File(path);

		try {
			if (refImgFile.exists()) {
				imgEncodeString = Base64.getEncoder().encodeToString(Files.readAllBytes(refImgFile.toPath()));
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		return imgEncodeString;
	}

	public Image getImageDetails(String orientation, String imageId) {
		Image imageDetails = null;
		if (orientation.equalsIgnoreCase(ScreenOrientation.PORTRAIT.toString())) {
			imageDetails = portraitImageMap.get(imageId);
		} else if (orientation.equalsIgnoreCase(ScreenOrientation.LANDSCAPE.toString())) {
			imageDetails = landscapeImageMap.get(imageId);
		}else if ("desktop".equalsIgnoreCase(orientation)) {
			imageDetails = desktopImageMap.get(imageId);
		}

		return imageDetails;
	}

	public Map<String, Image> readImageDetails(JsonArray jsonArray, String orientation) {

		Map<String, Image> imageMap = new HashMap<String, Image>();

		for (JsonElement jsonElement : jsonArray) {
			Image image = new Image();
			String id = jsonElement.getAsJsonObject().get("id").getAsString();
			String name = jsonElement.getAsJsonObject().get("name").getAsString();
			int xMinus = jsonElement.getAsJsonObject().get("xMinus").getAsInt();
			int yMinus = jsonElement.getAsJsonObject().get("yMinus").getAsInt();
			int width = jsonElement.getAsJsonObject().get("width").getAsInt();
			int height = jsonElement.getAsJsonObject().get("height").getAsInt();
			String imageAsEncodedString = getImageAsEncodedString(assetsPath + orientation.toString().toLowerCase() + "\\"+name);

			image.setId(id);
			image.setName(name);
			image.setxMinus(xMinus);
			image.setyMinus(yMinus);
			image.setHeight(height);
			image.setWidth(width);
			image.setImageAsEncodedString(imageAsEncodedString);

			imageMap.put(id, image);

		}
		return imageMap;
	}

	public void readImageDetails(ScreenOrientation orientation, String gamename) {

		try {
			JsonParser parser = new JsonParser();
			Object obj = parser.parse(new FileReader(assetsPath + "Dimension.json"));
			JsonObject jsonObject = (JsonObject) obj;
			if (orientation.toString().equalsIgnoreCase("Portrait")) {
				JsonObject portraitJson = jsonObject.getAsJsonObject("Portrait");
				JsonObject portraitDimensionJson = portraitJson.getAsJsonObject("Dimension");
				int x = portraitDimensionJson.get("x").getAsInt();
				int y = portraitDimensionJson.get("y").getAsInt();
				JsonArray portraitImagesJsonArray = portraitJson.getAsJsonArray("Images");
				portraitImageMap = readImageDetails(portraitImagesJsonArray, orientation.toString());
				portraitDetails.getDimension().setX(x);
				portraitDetails.getDimension().setY(y);

				portraitDetails.setImagesMap(portraitImageMap);

			} else if (orientation.toString().equalsIgnoreCase("Landscape")) {
				JsonObject landscapeJson = jsonObject.getAsJsonObject("Landscape");
				JsonObject portraitDimensionJson = landscapeJson.getAsJsonObject("Dimension");

				int x = portraitDimensionJson.get("x").getAsInt();
				int y = portraitDimensionJson.get("y").getAsInt();
				JsonArray portraitImagesJsonArray = landscapeJson.getAsJsonArray("Images");
				landscapeImageMap = readImageDetails(portraitImagesJsonArray, orientation.toString());

				landscapeDetails.getDimension().setX(x);
				landscapeDetails.getDimension().setY(y);
				landscapeDetails.setImagesMap(landscapeImageMap);

			}

		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}


	public boolean isImagesCollide(String gameName, String sceneName,ExtentTest report) {

		boolean isImagesColliding=false,result=false;
		try{
			HashMap<String,ArrayList<String>> allSceneImagesCollideDetails=new ImageDetailsReader().readOverlappingCheckDetails(gameName);

			ArrayList<String> SceneImagesCollideDetailsList=allSceneImagesCollideDetails.get(sceneName);
			ArrayList<String> SceneImagesCollideDetailsList2=allSceneImagesCollideDetails.get(sceneName);

			for(String imageId :SceneImagesCollideDetailsList){
				for(String imageId2 :SceneImagesCollideDetailsList2){

					if(!imageId.equalsIgnoreCase(imageId2)){

						Image firstImage = getImageDetails(screenOrientation, imageId);

						Image secondImage = getImageDetails(screenOrientation, imageId2);
						System.out.println("Checking "+ firstImage.getId() +" with "+ secondImage.getId() );
						isImagesColliding = imgapi.isImagesCollide(firstImage, secondImage);
						if(isImagesColliding)
						{
							result=true;
							report.log(Status.FAIL, firstImage.getId() +" is colliding with " +secondImage.getId());
						}
					}

				}

			}

		}catch(Exception e)
		{	isImagesColliding=true;
		e.printStackTrace();
		}
		return result;
	}


	public boolean isImagesInside(String gameName, String sceneName,ExtentTest report) {

		boolean isImagesInside=false,result=true;
		try{
			HashMap<String,ArrayList<String[]>> allSceneImagesCollideDetails=new ImageDetailsReader().readInsideCheckDetails(gameName);

			ArrayList<String[]> SceneImagesCollideDetailsList=allSceneImagesCollideDetails.get(sceneName);

			for(String imageId[] :SceneImagesCollideDetailsList){

				Image innerImage = getImageDetails(screenOrientation, imageId[0]);

				Image outerImage = getImageDetails(screenOrientation, imageId[1]);
				System.out.println("Checking inner image"+ innerImage.getId() +" with outer image "+ outerImage.getId() );
				isImagesInside = imgapi.isImageInside(innerImage, outerImage);	
				if(!isImagesInside)
				{
					result=false;
					report.log(Status.FAIL, innerImage.getId()+" is not inside "+outerImage.getId());
				}
			}
		}catch(Exception e)
		{
			isImagesInside=false;
			e.printStackTrace();
		}

		return result;
	}

	/*Date:23/7/2020
	 * This method is alredy in Mobile HTML report class copy here temporary
	 * */
	public boolean details_append_folderOnlyScreeshot( WebDriver webdriver,String screenShotName) 
	{
		boolean isScreenshotTaken=false;
		//Captures screenshot for calling step
		File scrFile = ((TakesScreenshot)webdriver).getScreenshotAs(OutputType.FILE);
		// Now you can do whatever you need to do with it, for example copy somewhere
		String screenShotPath=extentScreenShotPath+"\\"+getScreenOrientation()+"\\"+ screenShotName+".jpg";;
		File destFile =new File(screenShotPath);

		try {
			FileUtils.copyFile(scrFile, destFile);
			isScreenshotTaken=true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		scrFile.delete();
		scrFile=null;
		destFile=null;
		return isScreenshotTaken;
	}

	/*Date:23/7/2020
	 * Desc: returns the free games count from free game Base scene
	 * */
	public String getFreeGamesCount() {

		Image freegamesCount = getImageDetails(screenOrientation, "FreeGamesOfferCount");

		String freeGamesCountText = imgapi.getText(freegamesCount);

		return freeGamesCountText;
	}
	/*Click on Quick Spin
	 * */
	public boolean clickQuickSpin() throws InterruptedException, IOException, URISyntaxException {
		boolean status = false;

		Image quickImageDetails = getImageDetails(screenOrientation, "QuickSpin");
		status = imgapi.click(quickImageDetails);

		return status;
	}
	/*Click on Autoplay button
	 * */
	public boolean clickStartAutoplay() throws InterruptedException, IOException, URISyntaxException {
		boolean status = false;

		Image autoplayImageDetails = getImageDetails(screenOrientation, "Autoplay");
		status = imgapi.click(autoplayImageDetails);
		Thread.sleep(1000);
		if(status)
		{
			Image startautoplayImageDetails = getImageDetails(screenOrientation, "StartAutoplay");
			status = imgapi.click(startautoplayImageDetails);
		}

		return status;
	}
	/*Click Free game Next offer
	 *  */
	public boolean clickFreeGameNextOffer() throws InterruptedException, IOException, URISyntaxException {
		boolean status = false;

		Image quickImageDetails = getImageDetails(screenOrientation, "FreeGameNextOffer");
		status = imgapi.click(quickImageDetails);

		return status;
	}
	/*Click Free gamediscard
	 */
	public boolean clickFreeGameDiscardSymbol() throws InterruptedException, IOException, URISyntaxException {
		boolean status = false;

		Image quickImageDetails = getImageDetails(screenOrientation, "FreeGame");
		status = imgapi.click(quickImageDetails);

		return status;
	}
	/*Function to wait till Discard offer screen visible
	 * */
	public boolean waitTillDiscardOfferVisible() {
		boolean status = false;

		Image discardOfferDetails = getImageDetails(screenOrientation, "FreeGameDiscardOffer");
		status = imgapi.waitTill(discardOfferDetails);

		return status;
	}
	/*Click Free game Keep it button
	 */
	public boolean clickFreeGameKeepItButton() throws InterruptedException, IOException, URISyntaxException {
		boolean status = false;

		Image keepItImageDetails = getImageDetails(screenOrientation, "FreeGameKeepIt");
		status = imgapi.click(keepItImageDetails);

		return status;
	}
	/*Click Free game Discard text button on dicard scene
	 */
	public boolean clickFreeGameDiscardOfferTextButton() throws InterruptedException, IOException, URISyntaxException {
		boolean status = false;

		Image discardImageDetails = getImageDetails(screenOrientation, "FreeGameDiscardTextButton");
		status = imgapi.click(discardImageDetails);

		return status;
	}
	/*Function to wait till Expiry screen visible
	 * */
	public boolean waitTillExpirySceneVisible() {
		boolean status = false;

		Image expiryOfferDetails = getImageDetails(screenOrientation, "FreeGameContinueButton");
		status = imgapi.waitTill(expiryOfferDetails);

		return status;
	}
	/*Function to wait till resume play screen visible
	 * */
	public boolean waitTillResumeSceneVisible() {
		boolean status = false;

		Image resumePlayDetails = getImageDetails(screenOrientation, "FreeGameResumePlayButton");
		status = imgapi.waitTill(resumePlayDetails);

		return status;
	}
	/*Function to wait till resume play screen visible
	 * */
	public boolean clickResumePlayButton() {
		boolean status = false;

		Image resumePlayButton = getImageDetails(screenOrientation, "FreeGameResumePlayButton");
		status = imgapi.click(resumePlayButton);

		return status;
	}

	/*Verify free game offer assing correct or not
	 * */

	public boolean validateFreeGameOffer(int defaultNoOFFreeGames)
	{
		boolean result=false;
		String offerassingText=getFreeGamesAbout();
		if(offerassingText!=null && offerassingText.trim().equals("You Have "+defaultNoOFFreeGames+" Free Games"))
		{
			result=true;
		}	
		return result;
	}
	/*Date:29/07/2020
	 * Verify free game cash Balance text correct or not in free game info
	 * @author: Snehal
	 * */

	public boolean validateAwardCashbalanceText(WebDriver webdriver,ExtentTest report)
	{
		boolean result=false;

		String jsonStr=getJsonString("en","fgOffersWinningsAddedToCashBalance");
		String offerassingText=getText("CashBalanceText");
		if(offerassingText!=null && jsonStr!=null &&offerassingText.trim().equalsIgnoreCase(jsonStr))
		{
			boolean res=details_append_folderOnlyScreeshot(webdriver,"validateAwardCashbalanceText");
			if(res)
			{
				try {
					report.log(Status.PASS, "Free game offers that award Cash balance show correct offer information on load",MediaEntityBuilder.createScreenCaptureFromPath(extentScreenShotPath+"\\validateAwardCashbalanceText.jpg").build() );
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				report.log(Status.PASS, "Free game offers that award Cash balance show correct offer information on load");
			}

			result =true;
		}
		else
		{
			try {
				boolean res=details_append_folderOnlyScreeshot(webdriver,"validateAwardCashbalanceText");
				report.log(Status.FAIL, "Expected String is not match with recived String \n Expected="+jsonStr+"\t Recived String= "+offerassingText,MediaEntityBuilder.createScreenCaptureFromPath(extentScreenShotPath+"\\validateAwardCashbalanceText.jpg").build() );
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return result;
	}

	/*Date:29/07/2020
	 * Verify free game Bonus Balance text correct or not in free game info
	 * @author :Snehal
	 * */

	public boolean validateAwardBonusbalanceText(String languageCode)
	{
		String textInScreen=null;

		textInScreen=getText("BonusBalanceText").trim();

		if(textInScreen.equals("Winnings added to Bonus balance"))
			return true;
		else
			return false;
	}

	public boolean validateTextInScene(String gameName, String sceneName,String languageCode,Mobile_HTML_Report languageReport) {
		boolean isAnytextValidationFailed=false;
		try{
			HashMap<String,ArrayList<String[]>> allSceneLangDetails=new ImageDetailsReader().readLanguageheckDetails(gameName);

			ArrayList<String[]> SceneLangDetailsList=allSceneLangDetails.get(sceneName);

			HashMap<String,String> languageDetailMap=new LanguageReader().getLanguageDetails(languageCode);


			for(String imageId[] :SceneLangDetailsList){
				String textInScreen=null;
				if(languageCode.equals("en"))
				{
					textInScreen=getText(imageId[0]).trim();
				}
				else{
					textInScreen = getTextByCoordinates(imageId[0],languageCode).trim();
				}
				String langKey= imageId[1];
				String textInLangJson = languageDetailMap.get(langKey).trim();
				System.out.println("String in scene :: " + textInScreen );
				System.out.println("String in Json :: " + textInLangJson);

				if(textInScreen.equalsIgnoreCase(textInLangJson)){
					//ToDo :: Add to report
					System.out.println("Validation Success for :: "+  langKey);
					languageReport.detailsAppendFolder("verify Localisation of free game entry screenin language", "verify localasation of text ","Text translated", "Pass", langKey);

				}else{
					languageReport.detailsAppendFolder("verify Localisation of free game entry screenin language", "verify localasation of text","Text is not translated", "Fail", langKey);
					System.out.println("Validation failed for :: "+  langKey);
					isAnytextValidationFailed=true;
				}

			}
		}catch(Exception e)
		{
			System.out.println(e.getMessage());
		}

		return isAnytextValidationFailed;
	}



	public boolean validateTextInScene(String gameName, String sceneName,String languageCode,ExtentTest report) {
		boolean isAnytextValidationFailed=false;
		try{
			HashMap<String,ArrayList<String[]>> allSceneLangDetails=new ImageDetailsReader().readLanguageheckDetails(gameName);

			ArrayList<String[]> SceneLangDetailsList=allSceneLangDetails.get(sceneName);

			HashMap<String,String> languageDetailMap=new LanguageReader().getLanguageDetails(languageCode);


			for(String imageId[] :SceneLangDetailsList){
				String textInScreen=null;
				if(languageCode.equals("en"))
				{
					textInScreen=getText(imageId[0]).trim();
				}
				else{
					textInScreen = getTextByCoordinates(imageId[0],languageCode).replaceAll("\n", " ").trim();
				}
				String langKey= imageId[1];
				String textInLangJson = languageDetailMap.get(langKey).trim();
				System.out.println("String in scene :: " + textInScreen );
				System.out.println("String in Json :: " + textInLangJson);

				if(textInScreen.equalsIgnoreCase(textInLangJson)){

					System.out.println("Validation Success for :: "+  langKey);
					report.pass("Expected Text for  "+imageId[0]+  " is <span style='color:green;'>"+textInLangJson+" </span> match with recevied string  <span style='color:green;'>"+textInScreen+" </span> in language "+languageCode );
					//report.log(Status.PASS, "Expected Text for  "+imageId[0]+  " is <span style='font-weight:bold;'>"+textInLangJson+" </span> match with recevied string <span style='color:green;'>"+textInScreen+" </span>in language "+languageCode );

				}else{
					System.out.println("Validation failed for :: "+  langKey);
					report.fail("Expected Text for  "+imageId[0]+  " is <span style='color:red;'>"+textInLangJson+" </span> not match with recevied string  <span style='color:red;'>"+textInScreen+" </span> in language "+languageCode );
					//report.log(Status.FAIL, "Expected Text for  "+imageId[0]+  " is <span style='font-weight:bold;'>"+textInLangJson+" </span> not match with recevied string  <span style='color:red;'>"+textInScreen+" </span>in language "+languageCode );
					isAnytextValidationFailed=true;
				}

			}
		}catch(Exception e)
		{	isAnytextValidationFailed=true;
		System.out.println(e.getMessage());
		}

		return isAnytextValidationFailed;
	}

	/*Dsc:returns free game offer id*/

	public String getFreeGameOfferID()
	{
		String freegameofferid=getText("FreeGameOfferID").trim();

		return freegameofferid;

	}

	public boolean waitTillNFDButtonVisible()
	{
		boolean status = false;
		Image NFDDetails = getImageDetails(screenOrientation, "NFDButton");
		status = imgapi.waitTill(NFDDetails);
		return status;

	}

	public boolean waitTillBtnVisible(String bt)
	{
		boolean status = false;
		Image BtnImageDetails = getImageDetails(screenOrientation, bt);
		status = imgapi.waitTill(BtnImageDetails);
		return status;
	}


	/*====================================================================================
	 * JOlly's changes stared
	 * */

	//Table Functions 

	public boolean PlacedonEven()  {
		boolean status = false;

		Image EvenImageDetails = getImageDetails(screenOrientation, "Even");
		status = imgapi.click(EvenImageDetails);

		return status;
	}

	public boolean PlacedonOdd()  {
		boolean status = false;

		Image OddImageDetails = getImageDetails(screenOrientation, "Odd");
		status = imgapi.click(OddImageDetails);

		return status;
	}

	public boolean Favourites()  {
		boolean status = false;

		Image FavouriteDetails = getImageDetails(screenOrientation, "Favourite");
		status = imgapi.click(FavouriteDetails);

		return status;
	}

	public boolean DoubleX2()  {
		boolean status = false;

		Image DoubleDetails = getImageDetails(screenOrientation, "DoubleX2");
		status = imgapi.click(DoubleDetails);

		return status;
	}

	public boolean Undo()  {
		boolean status = false;

		Image UndoDetails = getImageDetails(screenOrientation, "Undo");
		status = imgapi.click(UndoDetails);

		return status;
	}

	public boolean Clear()  {
		boolean status = false;

		Image ClearDetails = getImageDetails(screenOrientation, "Clear");
		status = imgapi.click(ClearDetails);

		return status;
	}

	//Jolly Shringi

	public boolean waitTillSummaryScreenVisible() {
		boolean status = false;
		Image SummaryScreenDetails = getImageDetails(screenOrientation, "BackToGame");
		status = imgapi.waitTill(SummaryScreenDetails);

		return status;
	}
	//Jolly Shringi

	public boolean WaitTillBaseScene()
	{
		boolean status = false;
		waitTillSpinBtnVisible();
		if(getBetInfo()!="FREE")
		{
			status = true;
		}           
		return status;
	}

	//Jolly Shringi

	public boolean BaseSceneWinVisibility()
	{
		boolean status = false;
		Image BaseGameWinAmountDetails = getImageDetails(screenOrientation, "BaseGameWinAmount");
		status = imgapi.isImageAppears(BaseGameWinAmountDetails);           
		return status;
	}
	//Jolly Shringi

	public boolean ValidateFreeGameSummaryWinAmt()
	{
		boolean status = false;
		Image FreeGameSummaryWinDetails = getImageDetails(screenOrientation, "CurrencySymbol");
		status = imgapi.isImageAppears(FreeGameSummaryWinDetails);          
		return status;
	}
	//Jolly Shringi

	public boolean GameLogoVisible()
	{
		boolean status = false;
		Image FreeGameLogoDetails = getImageDetails(screenOrientation, "FreeGameSummaryLogo");
		status = imgapi.isImageAppears(FreeGameLogoDetails);           
		return status;
	}
	//Jolly Shringi

	public boolean FreeGameSummaryWinAmt()
	{
		boolean status = false;
		Image FreeGameSummaryWinDetails = getImageDetails(screenOrientation, "FreeGameSummaryYouWonText");
		status = imgapi.isImageAppears(FreeGameSummaryWinDetails);           
		return status;
	}

	//Jolly Shringi
	public boolean FreeGameSummaryNextOffer()
	{
		boolean status = false;
		Image FreeGameSummaryNextOffer = getImageDetails(screenOrientation, "FreeGameNextOffer");
		status = imgapi.isImageAppears(FreeGameSummaryNextOffer);           
		return status;
	}

	//Jolly shringi
	public boolean waitTillPracticePlayVisible() {
		boolean status = false;

		Image PracticePlayDetails = getImageDetails(screenOrientation, "PracticePlay");
		status = imgapi.waitTill(PracticePlayDetails);

		return status;
	}

	/*Abhishek's changes ======================================================================================
	 * 
	 * */
	public boolean clickFreeGameBetButton() throws InterruptedException, IOException, URISyntaxException
	{
		boolean status = false;
		Image BetButton = getImageDetails(screenOrientation, "FreeGameConsoleBet");
		status = imgapi.click(BetButton);

		return status;

	}

	/*To check image is appering on scene 
	 * */
	public boolean isImageAppears(String image) throws InterruptedException {
		boolean status = false;
		Image imageDisplay = getImageDetails(screenOrientation, image);
		Thread.sleep(5000);
		status = imgapi.isImageAppears(imageDisplay);

		return status;
	}
	/*To click on image pass as parameter
	 * */
	public boolean click(String image) throws InterruptedException {
		boolean status = false;

		Image imageDisplay = getImageDetails(screenOrientation, image);
		Thread.sleep(5000);
		status = imgapi.click(imageDisplay);
		Thread.sleep(5000);
		return status;
	}


	//canvas clcik
	public boolean Canvasclick(String image) {
		boolean status = false;

		Image imageDisplay = getImageDetails(screenOrientation, image);
	   status = imgapi.canvasclick(imageDisplay);

		return status;
	}
	
	
		
	/*Create time stamp
	 * */
	public String createTimeStampStr()  {
		String timeStamp=null;
		try{
			Calendar mycalendar = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_hhmmss");
			timeStamp = formatter.format(mycalendar.getTime());

		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return timeStamp;
	}


	/*Description:Function will return the Json string  from inputed language json file.
	 * Input: language code, language key
	 * Output: Json string
	 * Author: Snehal
	 * */

	public String getJsonString(String languageCode,String languageKey)
	{
		String jsonStr=null;
		try{
			HashMap<String,String> languageDetailMap=new LanguageReader().getLanguageDetails(languageCode);
			jsonStr = languageDetailMap.get(languageKey).trim();
		}catch(Exception e)
		{
			System.out.println("Exception in reading json string"+e.getMessage());
		}
		return jsonStr;
	}


	//Created by Abhishek

	public boolean waitTillImageVisible(String image)
	{
		boolean status = false;

		Image imageDisplay = getImageDetails(screenOrientation, image);
		status = imgapi.waitTill(imageDisplay);

		return status;
	}

	public void paytableScroll(String image) {
		JavascriptExecutor js = (JavascriptExecutor) webdriver;
		Image imageDisplay = getImageDetails(screenOrientation, image);
		js.executeScript("arguments[0].scrollIntoView(true);", imageDisplay);
	}

	public boolean clickAndDrag(String image, String coordinates ) {
		boolean status = false;

		Image imageDisplay = getImageDetails(screenOrientation, image);
		Image imageDisplay1 = getImageDetails(screenOrientation, coordinates);
		status = imgapi.clickAndDrag(imageDisplay, imageDisplay1 );

		return status;
	}

	public boolean clickAt(String image) {
		boolean status = false;

		Image imageDisplay = getImageDetails(screenOrientation, image);
		status = imgapi.clickAt(imageDisplay);

		return status;
	}

	public String getTextCreditAmount(String Imageid){

		Image imageDetails = getImageDetails(screenOrientation, Imageid);

		String imageText = imgapi.getTextCreditAmount(imageDetails);

		return imageText;

	}
	
	
	
	

}
