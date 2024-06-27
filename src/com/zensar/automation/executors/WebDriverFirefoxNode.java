package com.zensar.automation.executors;
import org.apache.log4j.Logger;

import com.zensar.automation.framework.library.UnSupportedBrowserException;
import com.zensar.automation.framework.library.WebDriverManagerNode;
import com.zensar.automation.framework.utils.Constant;
/**
 * This class will launch the Firefox node at specified port in local
 * default port 5558 
 * */
public class WebDriverFirefoxNode {

	    public static void main(String[] args)  {
	    	Logger log = Logger.getLogger(WebDriverChromeNode.class.getName());
	    	WebDriverManagerNode node=new WebDriverManagerNode();
	    	try {
				node.launchNode(Constant.FIREFOX, "http://localhost:4444/grid/register", "5558");
			} catch (UnSupportedBrowserException e) {
				log.error(e.getMessage(),e);
			}
	        
	    }
	
}
