package com.zensar.automation.executors;
import org.apache.log4j.Logger;

import com.zensar.automation.framework.library.UnSupportedBrowserException;
import com.zensar.automation.framework.library.WebDriverManagerNode;
import com.zensar.automation.framework.utils.Constant;
/*
 * This class will launch Chrome node at local host on port 5555
 * Hub and port are configurable 
 * */
public class WebDriverChromeNode {
	
	
	    public static void main(String[] args)  {
	    	Logger log = Logger.getLogger(WebDriverChromeNode.class.getName());
	    	WebDriverManagerNode node=new WebDriverManagerNode();
	    	try {
				node.launchNode(Constant.CHROME, "http://localhost:4444/grid/register", "5555");
			} catch (UnSupportedBrowserException e) {
				log.error(e.getMessage(),e);
			}
	       
	    }
	
}
