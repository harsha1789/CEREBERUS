package com.zensar.automation.api;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.io.Files;
import com.zensar.automation.framework.library.PropertyReader;
import com.zensar.automation.framework.library.ZAFOCR;
import com.zensar.automation.framework.model.Dimension;
import com.zensar.automation.framework.model.Image;
import com.zensar.automation.framework.model.ImageElement;



public class ImageDesktopAPI implements ImageAPI {

	
	Logger log = Logger.getLogger(ImageDesktopAPI.class.getName());
	WebDriver driver = null;
	String gameName = null;
//	String assetsPath = null;
	Map<String, String> gameAssets = null;
    int connectedDesktopWidth;
	int connectedDesktopHeight;
	Dimension configureddesktopDimension=null;
	public ImageDesktopAPI(WebDriver driver, String gameName, Dimension configureddesktopDimension ) {
		log.debug("In side ImageDesktopAPI Contructor" );
		this.driver = driver;
		this.gameName = gameName;
		this.configureddesktopDimension=configureddesktopDimension;
		
		//log.debug("configureddesktopDimension X ::"+configureddesktopDimension.x);
		//log.debug("configureddesktopDimension Y ::"+configureddesktopDimension.y);
		
		

		/* JavascriptExecutor js = ((JavascriptExecutor)driver);
		 connectedDesktopWidth = ((Long) js.executeScript("return window.screen.width * window.devicePixelRatio")).intValue(); 
		 connectedDesktopHeight = ((Long) js.executeScript("return window.screen.height * window.devicePixelRatio")).intValue();*/
		
		 //log.debug(" screenWidth="+connectedDesktopWidth+"\n scrrenHeight="+connectedDesktopHeight);
		log.debug("Opencv dll path"+PropertyReader.getInstance().getProperty("OpenCVDLL") );
		 System.load(PropertyReader.getInstance().getProperty("OpenCVDLL"));
		 log.debug("Welcome to OpenCV " + Core.VERSION);
       
		 //Write the resize the code here
		 
       /* dr.context("NATIVE_APP");
        dr.setSetting(Setting.IMAGE_MATCH_THRESHOLD, 0.4);
        
		if (deviceOrientation.equals("portrait")) {
			if (configuredPortraiatDimension.getY() != connectedDeviceHeight) {
				float scaleFactor = (float)connectedDeviceWidth  / configuredPortraiatDimension.getX();
				float scaleFactor2 = (float)connectedDeviceHeight  / configuredPortraiatDimension.getY();
				log.debug(scaleFactor2);
				 scaleFactor = 1.42f;
				 
				dr.setSetting(Setting.FIX_IMAGE_TEMPLATE_SCALE, true);
				dr.setSetting(Setting.DEFAULT_IMAGE_TEMPLATE_SCALE,scaleFactor2);
			}
		} else if (deviceOrientation.equals("landscape")) {
			if (configuredLandscapeDimension.getY() != connectedDeviceHeight) {
				float scaleFactor = 1.5f;
				float scaleFactor2 =(float)connectedDeviceHeight  / configuredLandscapeDimension.getY();
				
				log.debug((float)connectedDeviceWidth  / configuredLandscapeDimension.getX());
				log.debug(scaleFactor2);
				log.debug(scaleFactor);
				//;
				dr.setSetting(Setting.FIX_IMAGE_TEMPLATE_SCALE, true);
				dr.setSetting(Setting.DEFAULT_IMAGE_TEMPLATE_SCALE,scaleFactor2);
			}
		}*/
		
		//log.debug("DEFAULT_IMAGE_TEMPLATE_SCALE ::"+ dr.getSettings().get(Setting.DEFAULT_IMAGE_TEMPLATE_SCALE));
		
	}

	public String getText(Image image) {

		String respString = null;
		try {
					
			ImageElement ele = findElementByImage(driver, image);

			File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			
			String inFile = PropertyReader.getInstance().getProperty("assetsPath")+"FullImage.png";
			File destFile2 = new File(inFile);
			FileHandler.copy(screenshot, destFile2);
			
			
			/*
			Mat img = Imgcodecs.imread(inFile);
			int currentBrowserWidth = img.cols();
			int currentBrowserHeight = img.rows();
			
			Imgproc.resize(img, img, new Size(),(double)configureddesktopDimension.x/currentBrowserWidth,(double)configureddesktopDimension.y/currentBrowserHeight, Imgproc.INTER_CUBIC );
			
			Imgcodecs.imwrite("D:\\Test\\FullImage.png",img);
			
			Thread.sleep(2000);*/
			
			BufferedImage fullImg = ImageIO.read(new File(inFile));
			int connectedDesktopWidth=fullImg.getWidth();
			int connectedDesktopHeight=fullImg.getHeight();
			
			//log.debug("Current Screenshot Width: "+connectedDesktopWidth);
			//log.debug("Current Screenshot Height: "+connectedDesktopHeight);
			// Get width and height of the element
			int eleWidth = ele.getImageX();
			int eleHeight = ele.getImageY();

			//log.debug("Point X and Y :: " + eleWidth + ", " + eleHeight);
			//log.debug("eleWidth and eleHeight :: " + eleWidth + ", " + eleHeight);

			//rescale this afterwards
			//log.debug("Before Resize :: "+image.getWidth());
			//image=rescaleImage(image);
			//log.debug("After Resize :: "+image.getWidth());
			int leftTopX=ele.getImageX() - image.getxMinus();
			leftTopX = leftTopX<0?0:leftTopX;
			
			int leftTopY=ele.getImageY()- image.getyMinus();
			leftTopY = leftTopY<0?0:leftTopY;
			
			int width = eleWidth + image.getWidth();
			width=(leftTopX+width>=connectedDesktopWidth)?((connectedDesktopWidth-1)-leftTopX):width;
			//log.debug();
			
			int height = eleHeight + image.getHeight();
			height=(leftTopY+height>=connectedDesktopHeight)?((connectedDesktopHeight-1)-leftTopY):height;
			log.debug(image.getName()+"Collecting coorinates :: ("+leftTopX+","+leftTopY+") and ("+width+","+height+")");
			// Crop the entire page screenshot to get only element screenshot
			BufferedImage eleCredit = fullImg.getSubimage(leftTopX,leftTopY,width,height);
			
			ZAFOCR zafocr = new ZAFOCR();
			respString = zafocr.getText(eleCredit);
			log.debug(respString);

			// Below code is to store the text image and keeping this for
			// testing purpose can be removed later

			/*File screenshot4 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			BufferedImage eleScreenshot4 = fullImg.getSubimage(leftTopX,leftTopY,width,height);			
			ImageIO.write(eleScreenshot4, "png", screenshot4);
			File screenshotLocation4 = new File("D:\\Test\\" + image.getName()+"Output123.png");
			FileUtils.copyFile(screenshot4, screenshotLocation4);*/
			
		} catch (WebDriverException webDriverException) {
			log.error(webDriverException.getMessage(),webDriverException);
		} catch (IOException ioException) {
			log.error(ioException.getMessage(),ioException);
		}catch (Exception exception) {
			log.error(exception.getMessage(),exception);
		}

		return respString;
	}

	public boolean click(Image image) {
		boolean status = false;

		try {
			
			ImageElement ele = findElementByImage(driver, image);
			int imageCenterXcordinate= ele.getImageX()+(ele.getFindImageWidth()/2);
			int imageCenterYcordinate= ele.getImageY()+(ele.getFindImageHeight()/2);			
			log.debug("X: "+imageCenterXcordinate);
			log.debug("Y: "+imageCenterYcordinate);
			Actions actions= new Actions(driver);
			long bodyHeight = driver.findElement(By.tagName("body")).getRect().getHeight();
			long bodyWidth = driver.findElement(By.tagName("body")).getRect().getWidth();	
			int topLeftX = (int) bodyWidth/2;
			int topLeftY = (int) bodyHeight/2;
			actions.moveToElement(driver.findElement(By.tagName("body")), 0,0);
			actions.moveToElement(driver.findElement(By.tagName("body")), -topLeftX,-topLeftY);
			log.debug("topLeftX: "+topLeftX+" topLeftY: "+topLeftY);
			actions.moveByOffset(imageCenterXcordinate, imageCenterYcordinate).click().perform();	
			
			Thread.sleep(5000);
			status = true;
			
		} catch (Exception exception) {
			log.error("Exception while click : ",exception);
		}

		return status;
	}

	public boolean waitTill(Image image) {
		boolean status = false;

		
			long currentTime = System.currentTimeMillis();
			long defaultWaitTime=30000;
			long waitTillTime=currentTime+defaultWaitTime;
			while(currentTime<waitTillTime){
			ImageElement ele;
			try {
				ele = findElementByImage(driver, image);
				if(ele!=null){
					break;
				}
			} catch (Exception exception) {
				log.error(exception.getMessage(),exception);
			}
			currentTime = System.currentTimeMillis();
			}
			status = true;
			
			
		return status;
	}
	
	
	 Image rescaleImage(Image image) {

		try {
			image.setxMinus(resizeValueByWidth(image.getxMinus()));
			image.setyMinus(resizeValueByHeight(image.getyMinus()));
			image.setHeight(resizeValueByWidth(image.getHeight()));
			image.setWidth(resizeValueByHeight(image.getWidth()));
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return image;
	}
	
	int resizeValueByWidth(int value) {
		value = (int) (value * ((float) connectedDesktopWidth / (float) configureddesktopDimension.getX()));
		return value;
	}
	 
	int resizeValueByHeight(int value) {
		
		value = (int) (value * ((float) connectedDesktopHeight / (float) configureddesktopDimension.getY()));
		return value;
	}
	
	public ImageElement findElementByImage(WebDriver driver,Image image) throws IOException{
		
		ImageElement imageElement =null;
		int matchMethod = Imgproc.TM_CCOEFF_NORMED;
		log.debug("\nRunning Template Matching");

		long currentTime = System.currentTimeMillis();
		long defaultWaitTime=45000;
		long waitTillTime=currentTime+defaultWaitTime;
		while(currentTime<waitTillTime){
			
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String inFile = PropertyReader.getInstance().getProperty("assetsPath")+"FullImage.png";
		log.debug("inFile NAme ::"+inFile);
		File destFile2 = new File(inFile);
		FileHandler.copy(screenshot, destFile2);
		
		
		//log.debug("browser Width ::"+driver.manage().window().getSize().getWidth());
		//log.debug("browser Height ::"+driver.manage().window().getSize().getHeight());
		
		/*File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scrFile, new File("D:\\Test\\FullImage1.png"));*/
		
		String templateFileName = PropertyReader.getInstance().getProperty("assetsPath")+"Template.png";
		log.debug("templateFileName NAme ::"+templateFileName);
		File templateFile = new File(templateFileName);
		Files.write(image.getImageAsEncodedString().getBytes("UTF-8"), templateFile);

		Mat img =null;
		Mat templ=null;
		
		//BufferedImage fullImg = ImageIO.read(screenshot);
		try {
			log.debug("Going to read images ");
			 img = Imgcodecs.imread(inFile);
			 templ = Imgcodecs.imread(image.getImageFullPath());
		} catch (Exception e) {
			System.out.println("Exception occured ::");
			log.error(e.getMessage(),e);
		}
		
		int currentBrowserWidth = img.cols();
		int currentBrowserHeight = img.rows();
		
		//log.debug("currentBrowserWidth ::  "+ currentBrowserWidth);
		//log.debug("currentBrowserHeight :: "+ currentBrowserHeight);
		
		int interpolation;
		
		if (currentBrowserWidth > configureddesktopDimension.getX() && currentBrowserHeight > configureddesktopDimension.getY())
	       interpolation = Imgproc.INTER_CUBIC; //enlarge image
	    else if (currentBrowserWidth < configureddesktopDimension.getX() && currentBrowserHeight < configureddesktopDimension.getY())
	        interpolation = Imgproc.INTER_AREA; //shrink image
	    else
	        interpolation = Imgproc.INTER_LINEAR; 
		
		log.debug("interpolation"+interpolation);
		//Mat resizeImg=null;
		Imgproc.resize(templ, templ, new Size(),(double)currentBrowserWidth/configureddesktopDimension.getX(),(double)currentBrowserHeight/configureddesktopDimension.getY(), Imgproc.INTER_AREA );
		// / Create the result matrix
		int result_cols = img.cols() - templ.cols() + 1;
		int result_rows = img.rows() - templ.rows() + 1;
		Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);
		log.debug("Going to match tempalte");
		// / Do the Matching and Normalize
		Imgproc.matchTemplate(img, templ, result, matchMethod);
		log.debug("Afetr match tempalte");
		//log.debug("Core.NORM_MINMAX ::" + Core.NORM_MINMAX);
		//log.debug("CvType.CV_32FC1::" + CvType.CV_32FC1);
		//log.debug("Imgproc.TM_CCOEFF:" + Imgproc.TM_CCOEFF);
		// Imgproc.TM_CCOEFF
		//Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());
		log.debug("Setting Threshold");
		Imgproc.threshold(result, result, 0.5, 1.0, Imgproc.THRESH_TOZERO);
		// / Localizing the best match with minMaxLoc
		MinMaxLocResult mmr = Core.minMaxLoc(result);

		org.opencv.core.Point matchLoc;
		if (matchMethod == Imgproc.TM_SQDIFF || matchMethod == Imgproc.TM_SQDIFF_NORMED) {
			matchLoc = mmr.minLoc;
		} else {
			matchLoc = mmr.maxLoc;
		}
		
		log.debug("mmr.maxVal ::"+mmr.maxVal);
		imageElement =new ImageElement();
		
		imageElement.setImageX((int)matchLoc.x);
		imageElement.setImageY((int)matchLoc.y);
		
		imageElement.setFindImageWidth(templ.cols());
		imageElement.setFindImageHeight(templ.rows());
		
		if(mmr.maxVal>0.5){
			break;
		}else{
			log.debug("Template not found");
		}
		
		currentTime = System.currentTimeMillis();
		
		}
		
		
		// / Show me what you got
		/*Imgproc.rectangle(img, matchLoc, new org.opencv.core.Point(matchLoc.x + templ.cols()+100, matchLoc.y + templ.rows()+0),
				new Scalar(0, 255, 0));
		int xCoordinate=(int) matchLoc.x+((int)templ.cols()/2);
		log.debug("xCoordinate :: "+xCoordinate);
		int yCoordinate=(int) matchLoc.y+((int)templ.rows()/2);
		log.debug("yCoordinate :: "+yCoordinate);
		
		BufferedImage eleScreenshot4 = fullImg.getSubimage((int) matchLoc.x-7, (int) matchLoc.y-7, templ.cols()+100, templ.rows());
		Actions actions= new Actions(driver);
		actions.moveToElement(driver.findElement(By.tagName("body")), 0,0);
		actions.moveByOffset(xCoordinate, yCoordinate).click().perform();
		
		ZAFOCR zafocr = new ZAFOCR();
		
		log.debug(zafocr.getText(eleScreenshot4));
		
		ImageIO.write(eleScreenshot4, "png", screenshot);
		File screenshotLocation4 = new File("D:\\Test\\Output_Sub1.png");
		FileUtils.copyFile(screenshot, screenshotLocation4);
		// Save the visualized detection.
		//log.debug("Writing " + outFile);
		//Imgcodecs.imwrite(outFile, img);
	*/	
		return imageElement;
	}

	//@Override
	public String getTextByCoordinates(Image image, String language) {
		// TODO Auto-generated method stub
		return null;
	}

	//@Override
	public boolean isImagesCollide(Image firstImage, Image secondImage) {
		// TODO Auto-generated method stub
		return false;
	}

	//@Override
	public boolean isImageInside(Image innerImage, Image outerImage) {
		// TODO Auto-generated method stub
		return false;
	}

	//@Override
	public boolean isImageAppears(Image image) {

		boolean isImageAppears = false;

		try {
			WebDriverWait wait = new WebDriverWait(driver, 3);
			ImageElement ele = findElementByImage(driver, image);
			
			if(ele!=null)
			{
				isImageAppears=true;
			}
		} catch (Exception exception) {
			log.error("Error in isImageAppears",exception);
		}

		return isImageAppears;
	}
	
	
	public boolean clickAt(Image image) {
		boolean status = false;

		try {

			ImageElement ele = findElementByImage(driver, image);
			int imageCenterXcordinate= ele.getImageX()+(ele.getFindImageWidth()/2);
			int imageCenterYcordinate= ele.getImageY()+(ele.getFindImageHeight()/2);			
			log.debug("X: "+imageCenterXcordinate);
			log.debug("Y: "+imageCenterYcordinate);
			Actions actions= new Actions(driver);
			long bodyHeight = driver.findElement(By.tagName("body")).getRect().getHeight();
			long bodyWidth = driver.findElement(By.tagName("body")).getRect().getWidth();
			if(bodyHeight>663)
			{
				bodyHeight=663;
			}
			int topLeftX = (int) bodyWidth/2;
			int topLeftY = (int) bodyHeight/2;			
			actions.moveToElement(driver.findElement(By.tagName("body")), 0,0);
			actions.moveToElement(driver.findElement(By.tagName("body")), -topLeftX,-topLeftY);
			log.debug("topLeftX: "+topLeftX+" topLeftY: "+topLeftY);
			actions.moveByOffset(imageCenterXcordinate, imageCenterYcordinate).click().perform();	
			
			Thread.sleep(5000);
			status = true;
		} catch (Exception exception) {
			log.error("Error in isImageAppears",exception);
		}

		return status;
	}
	
	
	
	
	public boolean clickAndDrag(Image image, Image image1) {
		boolean status = false;

		try {

			ImageElement ele = findElementByImage(driver, image);
			ImageElement ele1 = findElementByImage(driver, image1);
			int imageCenterXcordinate= ele.getImageX()+(ele.getFindImageWidth()/2);
			int imageCenterYcordinate= ele.getImageY()+(ele.getFindImageHeight()/2);
			int imageCenterXcordinate1= ele.getImageX()+(ele1.getFindImageWidth()/2);
			int imageCenterYcordinate1= ele.getImageY()+(ele1.getFindImageHeight()/2);
			Actions actions= new Actions(driver);
			
			long bodyHeight = driver.findElement(By.tagName("body")).getRect().getHeight();
			long bodyWidth = driver.findElement(By.tagName("body")).getRect().getWidth();	
			int topLeftX = (int) bodyWidth/2;
			int topLeftY = (int) bodyHeight/2;			
			log.debug("topLeftX: "+topLeftX+" topLeftY: "+topLeftY);
			actions.moveToElement(driver.findElement(By.tagName("body")), 0,0);
			actions.moveToElement(driver.findElement(By.tagName("body")), -topLeftX,-topLeftY);				
			//actions.moveByOffset(imageCenterXcordinate, imageCenterYcordinate).click().perform();
			actions.moveByOffset(imageCenterXcordinate, imageCenterYcordinate).clickAndHold().perform();
			actions.moveToElement(driver.findElement(By.tagName("body")), 0,0);
			actions.moveToElement(driver.findElement(By.tagName("body")), -topLeftX,-topLeftY);
			actions.moveByOffset(imageCenterXcordinate1, imageCenterYcordinate1).release().perform();
			
			
			Thread.sleep(5000);
			status = true;
		} catch (Exception exception) {
			log.error("Error in isImageAppears",exception);
		}

		return status;
	}
	
	
	public boolean doubleClick(Image image) {
		boolean status = false;

		try {
			
			ImageElement ele = findElementByImage(driver, image);
			int imageCenterXcordinate= ele.getImageX()+(ele.getFindImageWidth()/2);
			int imageCenterYcordinate= ele.getImageY()+(ele.getFindImageHeight()/2);			
			log.debug("X: "+imageCenterXcordinate);
			log.debug("Y: "+imageCenterYcordinate);
			Actions actions= new Actions(driver);
			long bodyHeight = driver.findElement(By.tagName("body")).getRect().getHeight();
			long bodyWidth = driver.findElement(By.tagName("body")).getRect().getWidth();	
			int topLeftX = (int) bodyWidth/2;
			int topLeftY = (int) bodyHeight/2;
			actions.moveToElement(driver.findElement(By.tagName("body")), 0,0);
			actions.moveToElement(driver.findElement(By.tagName("body")), -topLeftX,-topLeftY);
			log.debug("topLeftX: "+topLeftX+" topLeftY: "+topLeftY);
			actions.moveByOffset(imageCenterXcordinate, imageCenterYcordinate).doubleClick().perform();	
			
			Thread.sleep(5000);
			status = true;
			
		} catch (Exception exception) {
			log.error("Exception while click : ",exception);
		}

		return status;
	}

	//this method clicks using canvas and working for millioaire and bonanza game
    public boolean canvasclick(Image image) {
        boolean status = false;



      /* try {
            WebDriverWait wait = new WebDriverWait(driver, 30);



           By imageElement = MobileBy.image(image.getImageAsEncodedString());



           wait.until(ExpectedConditions.presenceOfElementLocated(imageElement)).click();
            
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

*/

       return status;
    }
	
	}
