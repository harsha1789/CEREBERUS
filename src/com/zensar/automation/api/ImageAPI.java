package com.zensar.automation.api;

import org.openqa.selenium.support.ui.WebDriverWait;

import com.zensar.automation.framework.model.Image;

public interface ImageAPI {

	public String getText(Image image);
	public String getTextByCoordinates(Image image,String language) ;
	public boolean click(Image image) ;
	public boolean waitTill(Image image);
	public boolean isImagesCollide(Image firstImage, Image secondImage);
	public boolean isImageInside(Image innerImage, Image outerImage);
	public boolean isImageAppears(Image innerImage);
	public boolean clickAndDrag(Image imageDisplay, Image imageDisplay1);
	public boolean clickAt(Image imageDisplay);
	public boolean doubleClick(Image image) ;
	//try
   
    //try
    public boolean canvasclick(Image image) ;


}