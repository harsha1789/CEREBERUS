package com.zensar.automation.executors;
import org.apache.log4j.Logger;

import com.zensar.automation.framework.library.UnSupportedBrowserException;
import com.zensar.automation.framework.library.WebDriverManagerNode;
import com.zensar.automation.framework.utils.Constant;
/*This class launch the IE node at port 5556 in local
 * */
public class WebDriverIENode {

	    public static void main(String[] args)  {
	    	Logger log = Logger.getLogger(WebDriverChromeNode.class.getName());
	    	WebDriverManagerNode node=new WebDriverManagerNode();
	    	try {
				node.launchNode(Constant.INTERNETEXPLORER, "http://localhost:4444/grid/register", "5556");
			} catch (UnSupportedBrowserException e) {
				log.error(e.getMessage(),e);
			}
	       
	    }
	
}
