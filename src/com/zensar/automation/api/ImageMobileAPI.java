package com.zensar.automation.api;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.zensar.automation.framework.library.PropertyReader;
import com.zensar.automation.framework.library.ZAFOCR;
import com.zensar.automation.framework.model.Dimension;
import com.zensar.automation.framework.model.Image;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.Setting;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public class ImageMobileAPI implements ImageAPI{

	Logger log = Logger.getLogger(ImageMobileAPI.class.getName());
	AppiumDriver driver = null;
	String gameName = null;
	String assetsPath = null;
	Map<String, String> gameAssets = null;
	int connectedDeviceWidth;
	int connectedDeviceHeight;
	Dimension configuredPortraiatDimension=null;
	Dimension configuredLandscapeDimension=null;
	public ImageMobileAPI(AppiumDriver driver, String gameName, Dimension configuredPortraiatDimension,Dimension configuredLandscapeDimension ) {
		this.driver = driver;
		this.gameName = gameName;
		this.configuredPortraiatDimension=configuredPortraiatDimension;
		this.configuredLandscapeDimension=configuredLandscapeDimension;
		assetsPath = PropertyReader.getInstance().getProperty("assetsPath") + gameName + "\\";
		String deviceOrientation = driver.getOrientation().toString().toLowerCase();
		JavascriptExecutor js = ((JavascriptExecutor)driver);
		//dr.manage().window().fullscreen();
		//log.debug("browser Width ::"+dr.manage().window().getSize().getWidth());
		//log.debug("browser Height ::"+dr.manage().window().getSize().getHeight());
		
		if (driver instanceof AndroidDriver) {
			driver.context("CHROMIUM");
		} else if (driver instanceof IOSDriver) {
			Set<String> contexts=driver.getContextHandles();
			for(String context:contexts) {
				if(context.startsWith("WEBVIEW")){
					log.debug("context going to set in IOS is:"+context);
					driver.context(context);
				}
			}
			
		}
		
		if (deviceOrientation.equals("portrait")) {
		   connectedDeviceWidth = typeCasting( js.executeScript("return window.screen.width * window.devicePixelRatio")); 
		   connectedDeviceHeight = typeCasting( js.executeScript("return window.screen.height * window.devicePixelRatio"));
		} else if (deviceOrientation.equals("landscape")) {
			 connectedDeviceWidth = typeCasting( js.executeScript("return window.screen.width * window.devicePixelRatio"));
			 connectedDeviceHeight  = typeCasting( js.executeScript("return window.screen.height * window.devicePixelRatio"));
		}
        
		 log.debug("Connected device screenWidth="+connectedDeviceWidth+"\nConnected device scrrenHeight="+connectedDeviceHeight);
       
        driver.context("NATIVE_APP");
		if (driver instanceof AndroidDriver) {
			((AndroidDriver) driver).setSetting(Setting.IMAGE_MATCH_THRESHOLD, 0.6);
		} else if (driver instanceof IOSDriver) {
			((IOSDriver) driver).setSetting(Setting.IMAGE_MATCH_THRESHOLD, 0.6);
		}
       // driver.setSetting(Setting.IMAGE_MATCH_THRESHOLD, 0.6);
        
		if (deviceOrientation.equals("portrait")) {
			if (configuredPortraiatDimension.getY() != connectedDeviceHeight) {
				float scaleFactor2 = (float)connectedDeviceHeight  / configuredPortraiatDimension.getY();
				log.debug(scaleFactor2);
				
				
				if (driver instanceof AndroidDriver) {
					((AndroidDriver) driver).setSetting(Setting.FIX_IMAGE_TEMPLATE_SCALE, true);
					((AndroidDriver) driver).setSetting(Setting.DEFAULT_IMAGE_TEMPLATE_SCALE,scaleFactor2);
				} else if (driver instanceof IOSDriver) {
					((IOSDriver) driver).setSetting(Setting.FIX_IMAGE_TEMPLATE_SCALE, true);
					((IOSDriver) driver).setSetting(Setting.DEFAULT_IMAGE_TEMPLATE_SCALE,scaleFactor2);
				}
				 
				
			}
		} else if (deviceOrientation.equals("landscape")) {
			if (configuredLandscapeDimension.getY() != connectedDeviceHeight) {
				float scaleFactor = 1.5f;
				float scaleFactor2 =(float)connectedDeviceHeight  / configuredLandscapeDimension.getY();
				
				log.debug((float)connectedDeviceWidth  / configuredLandscapeDimension.getX());
				log.debug(scaleFactor2);
				log.debug(scaleFactor);
				//;
				if (driver instanceof AndroidDriver) {
					((AndroidDriver) driver).setSetting(Setting.FIX_IMAGE_TEMPLATE_SCALE, true);
					((AndroidDriver) driver).setSetting(Setting.DEFAULT_IMAGE_TEMPLATE_SCALE,scaleFactor2);
				} else if (driver instanceof IOSDriver) {
					((IOSDriver) driver).setSetting(Setting.FIX_IMAGE_TEMPLATE_SCALE, true);
					((IOSDriver) driver).setSetting(Setting.DEFAULT_IMAGE_TEMPLATE_SCALE,scaleFactor2);
				}
			}
		}
		
		//log.debug("DEFAULT_IMAGE_TEMPLATE_SCALE ::"+ dr.getSettings().get(Setting.DEFAULT_IMAGE_TEMPLATE_SCALE));
		
	}

	
	public ImageMobileAPI() {
		// TODO Auto-generated constructor stub
	}
	public String getText(Image image) {

		String respString = null;
		try {
			log.debug("Context"+driver.getContext());	
			WebElement ele = driver.findElementByImage(image.getImageAsEncodedString());
			log.debug(image.getName()+" is  found");
			log.debug(ele.getRect().getPoint());
			File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			
			File DestFile2 = new File(assetsPath+driver.getOrientation().toString().toLowerCase()+".png");
			FileHandler.copy(screenshot, DestFile2);
			
			BufferedImage fullImg = ImageIO.read(screenshot);

			// Get the location of element on the page
			Point point = ele.getLocation();
			// Get width and height of the element
			int eleWidth = ele.getSize().getWidth();
			int eleHeight = ele.getSize().getHeight();

			//log.debug("Point X and Y :: " + point.getX() + ", " + point.getY());
			//log.debug("eleWidth and eleHeight :: " + eleWidth + ", " + eleHeight);
			//log.debug("Before Resize :: "+image.getWidth());
			image=rescaleImage(image);
			//log.debug("After Resize :: "+image.getWidth());
			
			int leftTopX=point.getX() - image.getxMinus();
			leftTopX = leftTopX<0?0:leftTopX;
			
			int leftTopY=point.getY()- image.getyMinus();
			leftTopY = leftTopY<0?0:leftTopY;
			
			int width = eleWidth + image.getWidth();
			width=(leftTopX+width>=connectedDeviceWidth)?((connectedDeviceWidth-1)-leftTopX):width;
			//log.debug();
			
			int height = eleHeight + image.getHeight();
			height=(leftTopY+height>=connectedDeviceHeight)?((connectedDeviceHeight-1)-leftTopY):height;
			//log.debug(image.getName()+"Collecting coorinates :: ("+leftTopX+","+leftTopY+") and ("+width+","+height+")");
			// Crop the entire page screenshot to get only element screenshot
			image.setSubImageX(leftTopX);
			image.setSubImageY(leftTopY);
			image.setSubImageWidth(width);
			image.setSubImageHeight(height);
			
			BufferedImage eleCredit = fullImg.getSubimage(leftTopX,leftTopY,width,height);
			
			

			ZAFOCR zafocr = new ZAFOCR();
			respString = zafocr.getText(eleCredit);
			//log.debug(respString);

			// Below code is to store the text image and keeping this for
			// testing purpose can be removed later
			/*
			BufferedImage eleScreenshot4 = fullImg.getSubimage(image.getSubImageX(),image.getSubImageY(),image.getSubImageWidth(),image.getSubImageHeight());			
			File screenshotLocation4 = new File(assetsPath + image.getName()+"Output.png");
			ImageIO.write(eleScreenshot4, "png", screenshotLocation4);*/

			/*File screenshot4 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			BufferedImage eleScreenshot4 = fullImg.getSubimage(leftTopX,leftTopY,width,height);			
			ImageIO.write(eleScreenshot4, "png", screenshot4);
			File screenshotLocation4 = new File(assetsPath + image.getName()+"Output.png");
			FileUtils.copyFile(screenshot4, screenshotLocation4);*/
		}catch(NoSuchElementException e)
		{
			log.debug(image.getName()+" is not found");
			//e.printStackTrace();
		
		}catch (WebDriverException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}

		return respString;
	}
	
	
	public String getTextByCoordinates(Image image,String language) {


		String respString = null;
		if(language == null){
			language="en";
		}
		try {
					

			File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			
			File DestFile2 = new File(assetsPath+driver.getOrientation().toString().toLowerCase()+".png");
			FileHandler.copy(screenshot, DestFile2);
			
			BufferedImage fullImg = ImageIO.read(screenshot);
			
			BufferedImage eleCredit = fullImg.getSubimage(image.getSubImageX(),image.getSubImageY(),image.getSubImageWidth(),image.getSubImageHeight());
			
		//	File screenshot4 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			/*BufferedImage eleScreenshot4 = fullImg.getSubimage(image.getSubImageX(),image.getSubImageY(),image.getSubImageWidth(),image.getSubImageHeight());			
			
			File screenshotLocation4 = new File(assetsPath + image.getName()+"_"+language+".png");
			ImageIO.write(eleScreenshot4, "png", screenshotLocation4);
*/
			ZAFOCR zafocr = new ZAFOCR();
			respString = zafocr.getText(eleCredit,language);
			//log.debug(respString);

			// Below code is to store the text image and keeping this for
			// testing purpose can be removed later
			//Thread.sleep(2000);
			/*File screenshot4 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			BufferedImage eleScreenshot4 = fullImg.getSubimage(image.getSubImageX(),image.getSubImageY(),image.getSubImageWidth(),image.getSubImageHeight());			
			
			File screenshotLocation4 = new File(assetsPath + image.getName()+"_"+language+".png");
			ImageIO.write(eleScreenshot4, "png", screenshotLocation4);*/
			//FileUtils.copyFile(screenshot4, screenshotLocation4);
		} catch (WebDriverException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return respString;
	
		
		
	}
	
	

	public boolean click(Image image) {
		boolean status = false;

		try {
			WebDriverWait wait = new WebDriverWait(driver, 30);

			/*By imageElement = MobileBy.image(image.getImageAsEncodedString());

			wait.until(ExpectedConditions.presenceOfElementLocated(imageElement)).click();*/
			
			WebElement ele = driver.findElementByImage(image.getImageAsEncodedString());
			log.debug(image.getName()+" is  found going to click..");
			ele.click();
			
			image.setSubImageX(ele.getRect().x);
			image.setSubImageY(ele.getRect().y);
			image.setSubImageHeight(ele.getRect().height);
			image.setSubImageWidth(ele.getRect().width);
			
			//Thread.sleep(5000);
			status = true;
		}catch(NoSuchElementException e)
		{
			log.debug(image.getName()+" is  not found");
		}
		catch (Exception e) {
			log.debug(e.getMessage());
			e.printStackTrace();
		}

		return status;
	}
	
	
	public boolean clickByCoordinates(Image image) {
		boolean status = false;

		try {
			
			int imageCenterXcordinate= image.getSubImageX()+(image.getSubImageWidth()/2);
			int imageCenterYcordinate= image.getSubImageY()+(image.getSubImageHeight()/2);
			Actions actions= new Actions(driver);
			actions.moveToElement(driver.findElement(By.tagName("body")), 0,0);
			actions.moveByOffset(imageCenterXcordinate, imageCenterYcordinate).click().perform();
			//Thread.sleep(5000);
			status = true;
		} catch (Exception e) {
			log.debug(e.getMessage());
			e.printStackTrace();
		}

		return status;
	}

	public boolean waitTill(Image image) {
		boolean status = false;

		try {
			WebDriverWait wait = new WebDriverWait(driver, 60);
			

			By imageElement = MobileBy.image(image.getImageAsEncodedString());
			wait.until(ExpectedConditions.presenceOfElementLocated(imageElement));
		//	wait.until(ExpectedConditions.presenceOfElementLocated(imageElement)).click();
			//Thread.sleep(5000);
			//File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			//File DestFile2 = new File("D:\\ImageComparision\\" + gameName + "\\"+driver.getOrientation().toString().toLowerCase()+".png");
			//FileHandler.copy(screenshot, DestFile2);
			status = true;
		} catch (Exception e) {
			log.debug(image.getName()+" is not found");
			log.debug(e.getMessage());
			//e.printStackTrace();
		}

		return status;
	}
	
	
	
	
	Image rescaleImage(Image image) {

		try {
			image.setxMinus(resizeValueByWidth(image.getxMinus()));
			image.setyMinus(resizeValueByHeight(image.getyMinus()));
			image.setHeight(resizeValueByWidth(image.getHeight()));
			image.setWidth(resizeValueByHeight(image.getWidth()));
		} catch (Exception e) {
			log.debug(e.getMessage());
			e.printStackTrace();
		}
		return image;
	}
	
	int resizeValueByWidth(int value) {
		if(driver.getOrientation().toString().toLowerCase().equals("portrait")){
		value = (int) (value * ((float) connectedDeviceWidth / (float) configuredPortraiatDimension.getX()));
		}else if(driver.getOrientation().toString().toLowerCase().equals("landscape")){
			value = (int) (value * ((float) connectedDeviceWidth / (float) configuredLandscapeDimension.getX()));
			}
		return value;
	}
	 
	int resizeValueByHeight(int value) {
		
		if(driver.getOrientation().toString().toLowerCase().equals("portrait")){
		value = (int) (value * ((float) connectedDeviceHeight / (float) configuredPortraiatDimension.getY()));
		}else if(driver.getOrientation().toString().toLowerCase().equals("landscape")){
			value = (int) (value * ((float) connectedDeviceHeight / (float) configuredLandscapeDimension.getY()));
		}
		return value;
	}
	/*
	 * Check image collide are not
	 * if image collide it retuns true
	 * otherwise return false
	 * */

	public boolean isImagesCollide(Image firstImage, Image secondImage) {
		
		firstImage=getSubImageDetails(firstImage);
		secondImage=getSubImageDetails(secondImage);
		
		boolean isImagesCollide=false;
		if(!((firstImage.getSubImageX() == firstImage.getSubImageY())&&(firstImage.getSubImageHeight()==firstImage.getSubImageWidth())&&(firstImage.getSubImageHeight()==0) 
				&&	(secondImage.getSubImageX() == secondImage.getSubImageY())&&(secondImage.getSubImageHeight()==secondImage.getSubImageWidth())&&(secondImage.getSubImageHeight()==0)))
		{
			log.debug("Image details found going to verify "+firstImage.getName() +" and "+ secondImage.getName());
			int firstImageleft = firstImage.getSubImageX();
			int firstImageTop = firstImage.getSubImageY();
			int firstImageRight  = firstImage.getSubImageX()+firstImage.getSubImageWidth();
			int firstImageBottom = firstImage.getSubImageY()+firstImage.getSubImageHeight();
			
			int secondImageleft = secondImage.getSubImageX();
			int secondImageTop = secondImage.getSubImageY();
			int secondImageRight  = secondImage.getSubImageX()+secondImage.getSubImageWidth();
			int secondImageBottom = secondImage.getSubImageY()+secondImage.getSubImageHeight();
			
			isImagesCollide = !( firstImageTop > secondImageBottom || firstImageRight < secondImageleft || firstImageBottom < secondImageTop ||  firstImageleft > secondImageRight );
		}
		else{
			log.debug("Image details not found during in isImagesCollide() "+firstImage.getName() +" and "+ secondImage.getName());
		}
		return isImagesCollide;
	}

	//@Override
	public boolean isImageInside(Image innerImage, Image outerImage) {
		
		boolean isImageInside=false;
		
		innerImage=getSubImageDetails(innerImage);
		outerImage=getSubImageDetails(outerImage);
		if(!((innerImage.getSubImageX() == innerImage.getSubImageY())&&(innerImage.getSubImageHeight()==innerImage.getSubImageWidth())&&(innerImage.getSubImageHeight()==0) 
				&&	(outerImage.getSubImageX() == outerImage.getSubImageY())&&(outerImage.getSubImageHeight()==outerImage.getSubImageWidth())&&(outerImage.getSubImageHeight()==0)))
		{
			log.debug("Image details found going to verify "+innerImage.getName() +" and "+ outerImage.getName());
			int innerImageleft = innerImage.getSubImageX();
			int innerImageTop = innerImage.getSubImageY();
			int innerImageRight  = innerImage.getSubImageX()+innerImage.getSubImageWidth();
			int innerImageBottom = innerImage.getSubImageY()+innerImage.getSubImageHeight();
			
			int outerImageleft = outerImage.getSubImageX();
			int outerImageTop = outerImage.getSubImageY();
			int outerImageRight  = outerImage.getSubImageX()+outerImage.getSubImageWidth();
			int outerImageBottom = outerImage.getSubImageY()+outerImage.getSubImageHeight();
			
			isImageInside= (
				      ((outerImageTop <= innerImageTop) && (innerImageTop <= outerImageBottom)) &&
				      ((outerImageTop <= innerImageBottom) && (innerImageBottom <= outerImageBottom)) &&
				      ((outerImageleft <= innerImageleft) && (innerImageleft <= outerImageRight)) &&
				      ((outerImageleft <= innerImageRight) && (innerImageRight <= outerImageRight))
				    );
		}
		else
		{
			log.debug("Image details not found during in isImageInside() "+innerImage.getName() +" and "+ outerImage.getName());
		}
		return isImageInside;
	}

	public Image getSubImageDetails(Image image) {

		
		if( (image.getSubImageX() == image.getSubImageY())&&(image.getSubImageHeight()==image.getSubImageWidth())&&(image.getSubImageHeight()==0)){
			log.debug("Sub Image details not present, Hence reading ");
		try {
					
			WebElement ele = driver.findElementByImage(image.getImageAsEncodedString());
			log.debug(image.getName()+" is  found");
			log.debug(ele.getRect().getPoint());
			/*File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			
			File DestFile2 = new File(assetsPath+driver.getOrientation().toString().toLowerCase()+".png");
			FileHandler.copy(screenshot, DestFile2);
			
			BufferedImage fullImg = ImageIO.read(screenshot);
			 */
			// Get the location of element on the page
			Point point = ele.getLocation();
			// Get width and height of the element
			int eleWidth = ele.getSize().getWidth();
			int eleHeight = ele.getSize().getHeight();

			//log.debug("Point X and Y :: " + point.getX() + ", " + point.getY());
			//log.debug("eleWidth and eleHeight :: " + eleWidth + ", " + eleHeight);
			//log.debug("Before Resize :: "+image.getWidth());
			image=rescaleImage(image);
			//log.debug("After Resize :: "+image.getWidth());
			
			int leftTopX=point.getX() - image.getxMinus();
			leftTopX = leftTopX<0?0:leftTopX;
			
			int leftTopY=point.getY()- image.getyMinus();
			leftTopY = leftTopY<0?0:leftTopY;
			
			int width = eleWidth + image.getWidth();
			width=(leftTopX+width>=connectedDeviceWidth)?((connectedDeviceWidth-1)-leftTopX):width;
			//log.debug();
			
			int height = eleHeight + image.getHeight();
			height=(leftTopY+height>=connectedDeviceHeight)?((connectedDeviceHeight-1)-leftTopY):height;
			//log.debug(image.getName()+"Collecting coorinates :: ("+leftTopX+","+leftTopY+") and ("+width+","+height+")");
			// Crop the entire page screenshot to get only element screenshot
			image.setSubImageX(leftTopX);
			image.setSubImageY(leftTopY);
			image.setSubImageWidth(width);
			image.setSubImageHeight(height);
			
			
			
		}catch(NoSuchElementException e)
		{
			log.debug(image.getName()+" is not found");
		}
		catch (WebDriverException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}

		return image;
	}
	public int typeCasting(Object object)
	{
		int value=0;
		JavascriptExecutor js = ((JavascriptExecutor)driver);
		try{
	    	 if(object instanceof Long)
	    		 value = ((Long) object).intValue();
	    	 else if(object instanceof Double)
	    		 value = ((Double) object).intValue(); 

	     }catch(Exception e)
	     {
	    	 e.getMessage();
	     }
		return value;
	}
	
	//@Override
	public boolean isImageAppears(Image image) {
		
		boolean isImageAppears = false;
		
		try {
			WebDriverWait wait = new WebDriverWait(driver, 3);
			By imageElement = MobileBy.image(image.getImageAsEncodedString());
			if(wait.until(ExpectedConditions.presenceOfElementLocated(imageElement))!=null)
			{
				isImageAppears=true;
			}
		} catch (Exception e) {
			log.debug("Exception thrown image not found");
			e.printStackTrace();
		}
		
		return isImageAppears;
	}


	//@Override
	public boolean clickAndDrag(Image imageDisplay, Image imageDisplay1) {
		// TODO Auto-generated method stub
		return false;
	}


	//@Override
	public boolean clickAt(Image imageDisplay) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * This method is for double click on particular image.
	 * 
	 */
	
	public boolean doubleClick(Image image) {
		boolean status = false;

		try {
			
			WebElement ele = driver.findElementByImage(image.getImageAsEncodedString());
			log.debug(image.getName()+" is  found going to double click..");
			Actions actions = new Actions(driver);
			actions.doubleClick(ele).perform();
			
			log.debug("Double click performed");	
			//Thread.sleep(5000);
			status = true;
		}catch(NoSuchElementException e)
		{
			log.debug(image.getName()+" is  not found");
		}
		catch (Exception e) {
			log.debug(e.getMessage());
			e.printStackTrace();
		}

		return status;
	}
	
	
	//this method clicks using canvas and working for millioaire and bonanza game
    public boolean canvasclick(Image image) {
        boolean status = false;



       try {
            WebDriverWait wait = new WebDriverWait(driver, 30);



           /*By imageElement = MobileBy.image(image.getImageAsEncodedString());



           wait.until(ExpectedConditions.presenceOfElementLocated(imageElement)).click();*/
            
            WebElement ele = driver.findElementByImage(image.getImageAsEncodedString());
            log.debug(image.getName()+" is  found going to click..");
            ele.click();
            
            image.setSubImageX(ele.getRect().x);
            image.setSubImageY(ele.getRect().y);
            image.setSubImageHeight(ele.getRect().height);
            image.setSubImageWidth(ele.getRect().width);
            
            //Thread.sleep(5000);
            status = true;
        }catch(NoSuchElementException e)
        {
            log.debug(image.getName()+" is  not found");
        }
        catch (Exception e) {
            log.debug(e.getMessage());
            e.printStackTrace();
        }



       return status;
    }


	

}
