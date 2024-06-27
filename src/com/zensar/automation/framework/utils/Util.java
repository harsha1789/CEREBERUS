package com.zensar.automation.framework.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.zensar.automation.framework.library.PropertyReader;
import com.zensar.automation.framework.model.DeviceInfo;
import com.zensar.automation.framework.model.Display;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarLog;

public class Util {

	
	public static final String UTF8_BOM = "\uFEFF";
	Logger log = Logger.getLogger(Util.class.getName());
	
	/**
	 * Date: 03/04/2017 Author: Kanchan Badhiye Description: clickAt() use for
	 * getting user data and click on particular button Parameter:
	 * @param nodeValue
	 * @param attribute1
	 * @param xmlData
	 * @return arrayList
	 */
	public ArrayList<String> getXMLDataInArray(String nodeValue, String attribute1, String xmlData)
	{
		List<String> a1=new ArrayList<>();
		try
		{
			File inputFile = new File(xmlData );
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			/*dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); 
			dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ""); 
*/
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			TransformerFactory tf = TransformerFactory.newInstance();
			/*tf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); 
			tf.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, ""); 
*/
			javax.xml.transform.Transformer transformer;
			transformer = tf.newTransformer();
			StringWriter writer = new StringWriter();
			//transform document to string
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			String xmlString = writer.getBuffer().toString(); 
			int length=xmlLength(xmlData,"SymbolSet");
			for(int i=0;i<length;i++)
			{
				String dataList=parseXMLByIndex(nodeValue,attribute1,"", "", xmlString,i);
				a1.add(dataList);
			}
			Thread.sleep(10);
		}
		catch(Exception e)
		{
			log.error(e);
		}

		return (ArrayList<String>) a1;
	}
	/**
	 * This method will click at the point specify in parameter
	 * @param userdata
	 * @param webdriver
	 * @param canVasXpath
	 * @return String
	 */
	public String clickAt(String userdata, WebDriver webdriver, String canVasXpath) {
		int x;
		int y;
		int userElementY;
		double userwidth;
		double userheight;
		double userElementX;

		String[] coordinates1 = userdata.split(",");
		userwidth = Integer.parseInt(coordinates1[0]);
		userheight = Integer.parseInt(coordinates1[1]);
		userElementX = Integer.parseInt(coordinates1[2]);
		userElementY = Integer.parseInt(coordinates1[3]);
		Actions act = new Actions(webdriver);
		WebElement ele1 = webdriver.findElement(By.xpath(canVasXpath));
		double deviceHeight = ele1.getSize().height;
		double deviceWidth = ele1.getSize().width;
		x = (int) ((deviceWidth / userwidth) * userElementX);
		y = (int) ((deviceHeight / userheight) * userElementY);
		act.moveToElement(ele1, x, y).click().build().perform();
		return x + "," + y;
	}
	/**
	 * This methode will read the xml file 
	 * @param nodeValue
	 * @param attribute1
	 * @param attribute2
	 * @param attribute3
	 * @param xmlData
	 * @param length
	 * @param index
	 * @return string
	 */
	public String readXML(String nodeValue, String attribute1, String attribute2, String attribute3, String xmlData,int length,int index)
	{
		try
		{
			File inputFile = new File(xmlData );
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			/*dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); 
			dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ""); */
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			TransformerFactory tf = TransformerFactory.newInstance();
			/*tf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); 
			tf.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");*/ 
			javax.xml.transform.Transformer transformer;
			transformer = tf.newTransformer();
			StringWriter writer = new StringWriter();
			//transform document to string
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			String xmlString = writer.getBuffer().toString(); 
			if(length>=0)
			{
				return parseXMLByIndex(nodeValue,attribute1,attribute2, attribute3, xmlString,index); 
			}
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}
	/**
	 * This methode will return the XML file lenght
	 * @param xmlData
	 * @param nodeVAlue
	 * @return int
	 */
	public int xmlLength(String xmlData,String nodeVAlue)
	{
		int length=0;
		int lengthnew=0;
		try
		{
			File inputFile = new File(xmlData );
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			/*dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); 
			dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ""); 
			*/
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			TransformerFactory tf = TransformerFactory.newInstance();
			/*tf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); 
			tf.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");*/ 
			javax.xml.transform.Transformer transformer;

			transformer = tf.newTransformer();
			StringWriter writer = new StringWriter();
			//transform document to string
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			NodeList nList = doc.getElementsByTagName(nodeVAlue);
			Node nNode = nList.item(0);

			NodeList childnodes=nNode.getChildNodes();
			length= nNode.getChildNodes().getLength();

			for(int i=0;i<length-1;i++)
			{
				if(childnodes.item(i).hasAttributes())
					lengthnew++;
			}
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
		return lengthnew;
	}

	/**
	 * Description: This
	 * function is used for filter Data from the Har
	 * @param nodeValue
	 * @param attribute1
	 * @param attribute2
	 * @param attribute3
	 * @param proxy
	 * @return String
	 */
	public String getData(String nodeValue, String attribute1, String attribute2, String attribute3,
			BrowserMobProxyServer proxy) {
		Har nhar;
		HarLog hardata;
		List<HarEntry> entries;
		Iterator<HarEntry> itr;
		
		nhar = proxy.getHar();
		hardata = nhar.getLog();
		entries = hardata.getEntries();
		itr = entries.iterator();
		String data = "";
		log.debug(itr);
		while (itr.hasNext()) {
			try {
				HarEntry entry = itr.next();
				String requestUrl = entry.getRequest().getUrl();
				if (requestUrl.contains("x.x?")) {
					String reloadData = entry.getResponse().getContent().getText();
					log.debug("Response Data is==" + reloadData);
					if (reloadData.contains(nodeValue)) {
						data = parseXML(nodeValue, attribute1, attribute2, attribute3, reloadData);
					}
				}
			} catch (Exception e) {
				log.error(e.getStackTrace());
			}
		}
		return data;
	}

	/**
	 *  This function is used for get Data from the Har
	 * @param nodeValue
	 * @param attribute1
	 * @param attribute2
	 * @param attribute3
	 * @param xmlData
	 * @return String
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public String parseXML(String nodeValue, String attribute1, String attribute2, String attribute3, String xmlData)
			throws SAXException, IOException, ParserConfigurationException {
		String dataList = "";
		Node nNode;
		NodeList nList;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		/*dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); 
		dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ""); 
		*/
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(xmlData));
		Document doc = dBuilder.parse(is);
		doc.getDocumentElement().normalize();
		log.debug("Root element :" + doc.getDocumentElement().getNodeName());
		nList = doc.getElementsByTagName(nodeValue);
		nNode = nList.item(0);
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) nNode;
			dataList += eElement.getAttribute(attribute1) + ",";
			dataList += eElement.getAttribute(attribute2) + ",";
			dataList += eElement.getAttribute(attribute3);
		}
		return dataList;
	}
	
	/**
	 * this function is used for get Data from the Har by index
	 * @param nodeValue
	 * @param attribute1
	 * @param attribute2
	 * @param attribute3
	 * @param xmlData
	 * @param index
	 * @return String
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public String parseXMLByIndex(String nodeValue, String attribute1, String attribute2, String attribute3, String xmlData,int index)
			throws SAXException, IOException, ParserConfigurationException {
		String dataList = "";
		Node nNode;
		NodeList nList;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		/*dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); 
		dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ""); */
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(xmlData));
		Document doc = dBuilder.parse(is);
		doc.getDocumentElement().normalize();
		nList = doc.getElementsByTagName(nodeValue);
		nNode = nList.item(index);
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) nNode;
			dataList += eElement.getAttribute(attribute1) + ",";
			dataList += eElement.getAttribute(attribute2) + ",";
			dataList += eElement.getAttribute(attribute3);
		}
		return dataList;
	}
	/**
	 * Date: 25/10/2017 Author: Kamal Kumar Vishwakarma Description: This
	 * function is used for removing BOM data from the file
	 */

	public String removeUTF8BOM(String s) {
		if (s.startsWith(UTF8_BOM)) {
			s = s.substring(1);
		}
		return s;
	}

	/**
	 * Date: 25/10/2017 Author: Kamal Kumar Vishwakarma Description: This
	 * function is used for get the language.json file from the Build
	 */

	public String getResponseLanguageFile(BrowserMobProxyServer proxy) {
		Har nhar;
		nhar = proxy.getHar();
		HarLog hardata;
		List<HarEntry> entries;
		Iterator<HarEntry> itr;
		String responseLanguageContent = null;
		
		hardata = nhar.getLog();
		entries = hardata.getEntries();
		itr = entries.iterator();

		while(itr.hasNext()) {
			HarEntry entry = itr.next();
			String requestUrl = entry.getRequest().getUrl();

			if(requestUrl.matches("(.*)resources/language(.*).json") || requestUrl.contains("rpt.cedexis.com") ) {
				responseLanguageContent = removeUTF8BOM(entry.getResponse().getContent().getText());
			}
		}

		return responseLanguageContent;
	}  

	
	
	/**
	 * This function copy  Test Data to CasinoAS1
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	public void copyFolder(File src, File dest) throws IOException {

		if (src.isDirectory()) {
			if (!dest.exists()) {
				dest.mkdir();
			}

			String[] files = src.list();
			for (String file : files) {

				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				copyFolder(srcFile, destFile);
			}
		} else {
			try(InputStream in = new FileInputStream(src)){
				try(OutputStream out = new FileOutputStream(dest)){
					byte[] buffer = new byte[1024];

					int length;
					while ((length = in.read(buffer)) > 0) {
						out.write(buffer, 0, length);
					}
				}
			}
		}
		log.debug("copying the source xml file to "+dest);
	}

	/**
	 * This method read the languages configured in language sheet 
	 * @return
	 */
	public List<Map> readLangList(){

		List<Map> langList=new ArrayList<>();
		ExcelDataPoolManager excelpoolmanager = new ExcelDataPoolManager();
		Map<String, String> currentRow = null;
		try {
			String testDataExcelPath=PropertyReader.getInstance().getProperty("TestData_Excel_Path");
			int totalRows = excelpoolmanager.rowCount(testDataExcelPath, Constant.LANG_XL_SHEET_NAME);
			for(int count =0 ;count<totalRows;count++){
				currentRow	= excelpoolmanager.readExcelByRow(testDataExcelPath, Constant.LANG_XL_SHEET_NAME,count);
				String languageDescription = currentRow.get(Constant.LANGUAGE).trim();
				String languageCode = currentRow.get(Constant.LANG_CODE).trim();
				Map<String,String> langrow=new HashMap<>();
				langrow.put("Language",languageDescription);
				langrow.put("Language Code",languageCode);
				langList.add(langrow);
			}
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		}
		return langList;
	}


	/**
	 * Method read the supported device list from device list sheet
	 * @return
	 */

	public List<DeviceInfo> readDeviceList(){

		List<DeviceInfo> deviceList=new ArrayList<>();
		ExcelDataPoolManager excelpoolmanager = new ExcelDataPoolManager();
		Map<String, String> currentRow = null;
		try {
			String testDataExcelPath=PropertyReader.getInstance().getProperty("TestData_Excel_Path");
			int totalRows = excelpoolmanager.rowCount(testDataExcelPath, "DeviceList");
			for(int count =1 ;count<totalRows;count++){
				currentRow	= excelpoolmanager.readExcelByRow(testDataExcelPath, "DeviceList",count);
				String osPlatform=currentRow.get("OSPlatform").trim();
				String devicename = currentRow.get("Device").trim();
				String size = currentRow.get("Display Size").trim();
				String size1=size.replace("pixels","" ).trim();
				String[] sizenew=size1.split("x");
				DeviceInfo device=new DeviceInfo();
				Display display= new Display();
				display.setWidth(Integer.parseInt(sizenew[0].trim()));
				display.setheight(Integer.parseInt(sizenew[1].trim()));
				//
				//device.setModel(devicename);
				//
				device.setMarketName(devicename);
				device.setDisplay(display);
				device.setOsPlatform(osPlatform);
				deviceList.add(device);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		log.info("Total no of devices in xls = "+deviceList.size());
		return deviceList;
	}

	/**
	 * This method copy file to server 
	 * @param src
	 * @param destpath
	 * @throws IOException
	 */
	public void copyFolder(File src, String destpath) throws IOException {
		ByteArrayInputStream inputStream = null;
		SmbFileOutputStream sfos= null;
		try {
			final byte[] buffer =new byte[10 * 8024];
			

			byte[] data =FileUtils.readFileToByteArray(src);
			inputStream = new ByteArrayInputStream(data);
			Decoder decoder=Base64.getDecoder();

			String domain=new String(decoder.decode(PropertyReader.getInstance().getProperty("Domain")));
			String domainUserName=new String(decoder.decode(PropertyReader.getInstance().getProperty("DomainUserName")));
			String domainPassword=new String(decoder.decode(PropertyReader.getInstance().getProperty("DomainPassword")));


			NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(domain, domainUserName, domainPassword);
			SmbFile remoteFile = new SmbFile(destpath, auth);
			sfos= new SmbFileOutputStream(remoteFile);
			int count =0;
			while((count = inputStream.read(buffer)) >0){
				sfos.write(buffer,0,count);
			}

			sfos.flush();
			inputStream.close();

		} catch (Exception e) {
			log.error("Exception while copying the file",e);
		}finally {
			if(sfos!=null)
				sfos.close();
		}
	}

	/**
	 * This method deletethe file from server
	 * @param filePath
	 * @throws IOException
	 */
	public void deleteFile(String filePath) throws IOException {

		try {
			Decoder decoder=Base64.getDecoder();
			String domain=new String(decoder.decode(PropertyReader.getInstance().getProperty("Domain")));
			String domainUserName=new String(decoder.decode(PropertyReader.getInstance().getProperty("DomainUserName")));
			String domainPassword=new String(decoder.decode(PropertyReader.getInstance().getProperty("DomainPassword")));


			NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(domain, domainUserName, domainPassword);

			SmbFile remoteFile = new SmbFile(filePath,auth);
			if (remoteFile.isFile() && remoteFile.exists()) {
				remoteFile.delete();
				log.info("file is removed"+ remoteFile);
			}

		} catch (Exception e) {
			log.error("Exception while deleting the file",e);
		}
	}


	/**
	 * This method update the player name in xml file
	 * @param playerName
	 * @param xmlFilePath
	 */
	public void changePlayerName(String playerName, String xmlFilePath) {
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			/*documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); 
			documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
			*/
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(xmlFilePath);

			Node playerInfo = document.getElementsByTagName("Key").item(0);

			// update login Name
			NamedNodeMap attribute = playerInfo.getAttributes();
			Node nodeAttr = attribute.getNamedItem("loginName");
			nodeAttr.setTextContent(playerName);

			// append a new node to the first employee
			// write the DOM object to the file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			/*transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); 
			transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");*/
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(document);

			StreamResult streamResult = new StreamResult(new File(xmlFilePath));
			transformer.transform(domSource, streamResult);
			log.debug("Changing the player name in xml with username created randomly");
		} catch (ParserConfigurationException pce) {
			log.error(pce.getMessage(),pce);
		} catch (TransformerException tfe) {
			log.error(tfe.getMessage(),tfe);
		} catch (IOException ioe) {
			log.error(ioe.getMessage(),ioe);
		} catch (SAXException sae) {
			log.error(sae.getMessage(),sae);
		}
	}

	/**
	 * THis method delete file having the name in wildcard format specified
	 * @param folderPath
	 * @param wildCard
	 */
	public void deleteFiles(String folderPath, String wildCard) {

		try {
			Decoder decoder=Base64.getDecoder();
			String domain=new String(decoder.decode(PropertyReader.getInstance().getProperty("Domain")));
			String domainUserName=new String(decoder.decode(PropertyReader.getInstance().getProperty("DomainUserName")));
			String domainPassword=new String(decoder.decode(PropertyReader.getInstance().getProperty("DomainPassword")));
			NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(domain, domainUserName, domainPassword);

			SmbFile remoteFolder = new SmbFile(folderPath, auth);
			if (remoteFolder.exists() && remoteFolder.isDirectory()) {
				SmbFile[] smbfiles = remoteFolder.listFiles(wildCard);
				for (SmbFile smbfile : smbfiles) {
					if (smbfile.isFile() && smbfile.exists()) {
						log.info("file is going to delete" + smbfile);
						smbfile.delete();
					}
				}
			}

		} catch (Exception e) {
			log.error("Exception while deleting the files", e);
		}
	}

	/**
	 * this method delete the list of copied files
	 * @param folderPath
	 * @param copiedFiles
	 */
	public void deleteFiles(String folderPath, List<String> copiedFiles) {
		try {

			String gameName=PropertyReader.getInstance().getProperty("GameName");
			String fileName=null;
			Decoder decoder=Base64.getDecoder();
			String domain=new String(decoder.decode(PropertyReader.getInstance().getProperty("Domain")));
			String domainUserName=new String(decoder.decode(PropertyReader.getInstance().getProperty("DomainUserName")));
			String domainPassword=new String(decoder.decode(PropertyReader.getInstance().getProperty("DomainPassword")));
			NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(domain, domainUserName, domainPassword);
			SmbFile remoteFolder = new SmbFile(folderPath, auth);

			if (remoteFolder.exists() && remoteFolder.isDirectory()) {
				SmbFile[] smbfiles = remoteFolder.listFiles();
				for (SmbFile smbfile : smbfiles) {
					fileName=smbfile.getCanonicalPath();
					if(fileName.contains(gameName))
					{
						String filetobedelete=fileName.substring(smbfile.getCanonicalPath().indexOf(gameName));	
						if (smbfile.isFile() && smbfile.exists()&& copiedFiles.contains(filetobedelete)&& copiedFiles.size()!=0) 
						{
							log.info("file is going to delete" + smbfile);
							smbfile.delete();
						}
					}
				}

			}

		} catch (Exception e) {
			log.error("Exception while deleting the files", e);
		}
	}
	

	

	/**
	 * Method will create the folder at specified path
	 * @param folderPath
	 * @return
	 */
	public boolean createFolder(String folderPath)
	{
		boolean isFolderCreated=false;
		File file = new File(folderPath);
		if (!file.isDirectory()) {
			if (file.mkdir()) {
				log.debug("Directory is created!");
				isFolderCreated= true;
			} else {
				log.debug("Failed to create directory!");
				isFolderCreated= false;
			}
		}else
		{
			log.info("Directory already exists: "+ folderPath);
			isFolderCreated=true;
		}
		return  isFolderCreated;
	}

	/**
	 * This function read the liat of supported currency list for progressive game from SupportedCurrenciesList sheet
	 * @return list 
	 */

	public     ArrayList<String>  readSupportedCurrenciesList(){
		ArrayList<String> values = new ArrayList<>();
		ExcelDataPoolManager excelpoolmanager = new ExcelDataPoolManager();
		Map<String, String> currentRow = null;
		Map<String,String> currRow=new HashMap<>();
		try {
			String testDataExcelPath=PropertyReader.getInstance().getProperty("TestData_Excel_Path");
			int totalRows = excelpoolmanager.rowCount(testDataExcelPath, Constant.CURRENCY_XL_SHEET_NAME);
			for(int count =1 ;count<totalRows;count++){
				currentRow    = excelpoolmanager.readExcelByRow(testDataExcelPath, Constant.CURRENCY_XL_SHEET_NAME,count);
				String curID = currentRow.get("ID").toString().trim().replace("\u00A0", "");
				String curISOCode = currentRow.get("ISOCode").trim().replace("\u00A0", "");
				currRow.put("ID",curID.trim());
				currRow.put("ISOCode",StringUtils.trim(curISOCode));
				values.addAll(currRow.values());
			}
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		}

		return values;
	}

	

	/**
	 * Create  System time stamp
	 * @return String
	 */

	public String createTimeStampStr()  {
		Calendar mycalendar = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_hhmmss");

		return formatter.format(mycalendar.getTime());
	}

	/**
	 * This method read currency list from sheet
	 * @return
	 */
	public List<Map<String,String>> readCurrList(){

		List<Map<String,String>> curList=new ArrayList<>();
		ExcelDataPoolManager excelpoolmanager = new ExcelDataPoolManager();
		Map<String, String> currentRow = null;
		try {
			String testDataExcelPath=PropertyReader.getInstance().getProperty("TestData_Excel_Path");
			int totalRows = excelpoolmanager.rowCount(testDataExcelPath, Constant.CURRENCY_XL_SHEET_NAME);
			for(int count =1 ;count<totalRows;count++){
				currentRow	= excelpoolmanager.readExcelByRow(testDataExcelPath, Constant.CURRENCY_XL_SHEET_NAME,count);
				String id = currentRow.get(Constant.ID).trim();
				String isoCode = currentRow.get(Constant.ISOCODE).trim();
				String isoName = currentRow.get(Constant.ISONAME).trim();
				String languageCurrency = currentRow.get(Constant.LANGUAGECURRENCY).trim();
				String currencyFormat=currentRow.get(Constant.DISPALYFORMAT).trim();
				String regExpression=currentRow.get(Constant.REGEXPRESSION).trim();
				String regExpressionNoSymbol=null;
				if(currentRow.get(Constant.REGEXPRESSION_NOSYMBOL)!=null) {
				 regExpressionNoSymbol=currentRow.get(Constant.REGEXPRESSION_NOSYMBOL).trim();
				}
				Map<String,String> currow=new HashMap<>();
				currow.put(Constant.ID,id);
				currow.put(Constant.ISOCODE,isoCode);
				currow.put(Constant.ISONAME,isoName);
				currow.put(Constant.LANGUAGECURRENCY,languageCurrency);
				currow.put(Constant.DISPALYFORMAT,currencyFormat);
				currow.put(Constant.REGEXPRESSION,regExpression);
				currow.put(Constant.REGEXPRESSION_NOSYMBOL,regExpressionNoSymbol);
				curList.add(currow);
			}
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		}
		return curList;
	}

	
	
	 /*
	  * Method Description: this method returns the current system date
	  * Return : String
	  * */
	public   static String getCurrentDate()
	{
		//Create a Calendar Objec
		Calendar calendar=Calendar.getInstance(TimeZone.getDefault());
		int todaysDate=calendar.get(Calendar.DATE);
		return Integer.toString(todaysDate);
	}
	
	
	
	/**
	 * This method read Language Translations list from excel sheet
	 * @return
	 */
	public List<Map<String, String>> readLangTransList() {

		List<Map<String, String>> langList = new ArrayList<>();
		ExcelDataPoolManager excelpoolmanager = new ExcelDataPoolManager();
		Map<String, String> currentRow = null;
		try {
			String testDataExcelPath = PropertyReader.getInstance().getProperty("TestData_Excel_Path");
			int totalRows = excelpoolmanager.rowCount(testDataExcelPath, Constant.LANG_XL_SHEET_NAME);
			for (int count = 1; count < totalRows; count++) {
				currentRow = excelpoolmanager.readExcelByRow(testDataExcelPath, Constant.LANG_XL_SHEET_NAME, count);
				String languageDescription = currentRow.get(Constant.LANGUAGE).trim();
				String languageCode = currentRow.get(Constant.LANG_CODE).trim();

				String Home = null;
				if (currentRow.get(Constant.Home) != null) {
					Home = currentRow.get(Constant.Home).trim();
				}
				String Banking = null;
				if (currentRow.get(Constant.Banking) != null) {
					Banking = currentRow.get(Constant.Banking).trim();
				}
				String Settings = null;
				if (currentRow.get(Constant.Settings) != null) {
					Settings = currentRow.get(Constant.Settings).trim();
				}
				String Sounds = null;
				if (currentRow.get(Constant.Sounds) != null) {
					Sounds = currentRow.get(Constant.Sounds).trim();
				}
				String Double = null;
				if (currentRow.get(Constant.Double) != null) {
					Double = currentRow.get(Constant.Double).trim();
				}
				String QuickDeal = null;
				if (currentRow.get(Constant.QuickDeal) != null) {
					QuickDeal = currentRow.get(Constant.QuickDeal).trim();
				}
				String PoweredByMicrogaming = null;
				if (currentRow.get(Constant.PoweredByMicrogaming) != null) {
					PoweredByMicrogaming = currentRow.get(Constant.PoweredByMicrogaming).trim();
				}
				String CoinsInPaytable = null;
				if (currentRow.get(Constant.CoinsInPaytable) != null) {
					CoinsInPaytable = currentRow.get(Constant.CoinsInPaytable).trim();
				}
				String Credits = null;
				if (currentRow.get(Constant.Credits) != null) {
					Credits = currentRow.get(Constant.Credits).trim();
				}
				String BetPlus1 = null;
				if (currentRow.get(Constant.BetPlus1) != null) {
					BetPlus1 = currentRow.get(Constant.BetPlus1).trim();
				}
				String BetMax = null;
				if (currentRow.get(Constant.BetMax) != null) {
					BetMax = currentRow.get(Constant.BetMax).trim();
				}
				String Deal = null;
				if (currentRow.get(Constant.Deal) != null) {
					Deal = currentRow.get(Constant.Deal).trim();
				}
				String Held = null;
				if (currentRow.get(Constant.Held) != null) {
					Held = currentRow.get(Constant.Held).trim();
				}
				String Draw = null;
				if (currentRow.get(Constant.Draw) != null) {
					Draw = currentRow.get(Constant.Draw).trim();
				}
				String Collect = null;
				if (currentRow.get(Constant.Collect) != null) {
					Collect = currentRow.get(Constant.Collect).trim();
				}
				String Bet = null;
				if (currentRow.get(Constant.Bet) != null) {
					Bet = currentRow.get(Constant.Bet).trim();
				}
				String TotalBet = null;
				if (currentRow.get(Constant.TotalBet) != null) {
					TotalBet = currentRow.get(Constant.TotalBet).trim();
				}
				String CoinsInBet = null;
				if (currentRow.get(Constant.CoinsInBet) != null) {
					CoinsInBet = currentRow.get(Constant.CoinsInBet).trim();
				}
				String CoinSize = null;
				if (currentRow.get(Constant.CoinSize) != null) {
					CoinSize = currentRow.get(Constant.CoinSize).trim();
				}
				String DoubleTo = null;
				if (currentRow.get(Constant.DoubleTo) != null) {
					DoubleTo = currentRow.get(Constant.DoubleTo).trim();
				}
				String PickCard = null;
				if (currentRow.get(Constant.PickCard) != null) {
					PickCard = currentRow.get(Constant.PickCard).trim();
				}
				String Congratulations = null;
				if (currentRow.get(Constant.Congratulations) != null) {
					Congratulations = currentRow.get(Constant.Congratulations).trim();
				}
				String YouWin = null;
				if (currentRow.get(Constant.YouWin) != null) {
					YouWin = currentRow.get(Constant.YouWin).trim();
				}
				String DoubleLimitReached = null;
				if (currentRow.get(Constant.DoubleLimitReached) != null) {
					DoubleLimitReached = currentRow.get(Constant.DoubleLimitReached).trim();
				}
				
				String regExpression = null;
				if (currentRow.get(Constant.REGEXPRESSION) != null) {
					regExpression = currentRow.get(Constant.REGEXPRESSION).trim();
				}

				Map<String, String> langrow = new HashMap<>();
				langrow.put("Language", languageDescription);
				langrow.put("Language Code", languageCode);
				langrow.put("Home", Home);
				langrow.put("Banking", Banking);
				langrow.put("Settings", Settings);
				langrow.put("Sounds", Sounds);
				langrow.put("Double", Double);
				langrow.put("QuickDeal", QuickDeal);
				langrow.put("PoweredByMicrogaming", PoweredByMicrogaming);
				langrow.put("CoinsInPaytable", CoinsInPaytable);
				langrow.put("Credits", Credits);
				langrow.put("BetPlus1", BetPlus1);
				langrow.put("BetMax", BetMax);
				langrow.put("Deal", Deal);
				langrow.put("Held", Held);
				langrow.put("Draw", Draw);
				langrow.put("Collect", Collect);
				langrow.put("Bet", Bet);
				langrow.put("TotalBet", TotalBet);
				langrow.put("CoinsInBet", CoinsInBet);
				langrow.put("CoinSize", CoinSize);
				langrow.put("DoubleTo", DoubleTo);
				langrow.put("PickCard", PickCard);
				langrow.put("Congratulations", Congratulations);
				langrow.put("YouWin", YouWin);
				langrow.put("DoubleLimitReached", DoubleLimitReached);
				langrow.put("RegExpression", regExpression);
				langList.add(langrow);
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return langList;
	}
	
	/**
	 * This method reads the regulated markets list
	 * @return
	 */
	public List<Map<String,String>> readMarketList()
	{
		List<Map<String,String>> marketList=new ArrayList<>();
		ExcelDataPoolManager excelpoolmanager = new ExcelDataPoolManager();
		Map<String, String> currentRow = null;
		try {
			String testDataExcelPath=PropertyReader.getInstance().getProperty("TestData_Excel_Path");
			int totalRows = excelpoolmanager.rowCount(testDataExcelPath, Constant.MARKET_XL_SHEET_NAME);
			for(int count =1 ;count<totalRows;count++)
			{
				currentRow	= excelpoolmanager.readExcelByRow(testDataExcelPath,Constant.MARKET_XL_SHEET_NAME,count);// coloum count (such as 7) eg: A,B,...
				String countryName =currentRow.get(Constant.COUNTRY_NAME).trim();// getting the market name( value to the key) from above row map
				String productId =currentRow.get(Constant.PRODUCT_ID).trim();
				String currencyIsoCode =currentRow.get(Constant.ISOCODE).trim();
				String marketTypeId =currentRow.get(Constant.MARKET_TYPEID).trim();
				String balance =currentRow.get(Constant.BALANCE).trim();
				
				String regExpression=null;
				if(currentRow.get(Constant.REGEXPRESSION)!=null) 
				{
				 regExpression=currentRow.get(Constant.REGEXPRESSION).trim();
				}
								
				
				String market=null;
				if(currentRow.get(Constant.MARKET)!=null) 
				{
					market=currentRow.get(Constant.MARKET).trim();
				}
				
				String brand=null;
				if(currentRow.get(Constant.BRAND)!=null) 
				{
					brand=currentRow.get(Constant.BRAND).trim();
				}
				
				String retrunUrlParam=null;
				if(currentRow.get(Constant.RETURN_URL_PARAMETER)!=null) 
				{
					retrunUrlParam=currentRow.get(Constant.RETURN_URL_PARAMETER).trim();
				}
				
				String marketUrl=null;
				if(currentRow.get(Constant.MARKET_URL)!=null) 
				{
					marketUrl=currentRow.get(Constant.MARKET_URL).trim();
				}
				
				String runFlag=null;
				if(currentRow.get(Constant.runFlag)!=null) 
				{
					runFlag=currentRow.get(Constant.runFlag).trim();
				}
				
				String Lang1=null;
				if(currentRow.get(Constant.Lang1)!=null) 
				{
					Lang1=currentRow.get(Constant.Lang1).trim();
				}
				String Lang2=null;
				if(currentRow.get(Constant.Lang2)!=null) 
				{
					Lang2=currentRow.get(Constant.Lang2).trim();
				}
				String LangCode1=null;
				if(currentRow.get(Constant.LangCode1)!=null) 
				{
					LangCode1=currentRow.get(Constant.LangCode1).trim();
				}
				String LangCode2=null;
				if(currentRow.get(Constant.LangCode2)!=null) 
				{
					LangCode2=currentRow.get(Constant.LangCode2).trim();
				}
				
				String LanguageCount=null;
				if(currentRow.get(Constant.LanguageCount)!=null) 
				{
					LanguageCount=currentRow.get(Constant.LanguageCount).trim();
				}			
				
				// Base game
				String BaseGameFeatures=null;
				if(currentRow.get(Constant.BaseGameFeatures)!=null) 
				{
					BaseGameFeatures=currentRow.get(Constant.BaseGameFeatures).trim();
				}
				
				//************* INFO bar
				String InfoBarFeatures=null;
				if(currentRow.get(Constant.InfoBarFeatures)!=null) 
				{
					InfoBarFeatures=currentRow.get(Constant.InfoBarFeatures).trim();
				}
				
				//************* Top bar
				String TopbarFeatures=null;
				if(currentRow.get(Constant.TopbarFeatures)!=null) 
				{
					TopbarFeatures=currentRow.get(Constant.TopbarFeatures).trim();
				}
				
				String gameClock=null;
				if(currentRow.get(Constant.gameClock)!=null) 
				{
					gameClock=currentRow.get(Constant.gameClock).trim();
				}				

				String GameName=null;
				if(currentRow.get(Constant.GameName)!=null) 
				{
					GameName=currentRow.get(Constant.GameName).trim();
				}
				//********************** SPIN
				String SpinFeatures=null;
				if(currentRow.get(Constant.SpinFeatures)!=null) 
				{
					SpinFeatures=currentRow.get(Constant.SpinFeatures).trim();
				}
				
				String SpinReelLanding=null;
				if(currentRow.get(Constant.SpinReelLanding)!=null) 
				{
					SpinReelLanding=currentRow.get(Constant.SpinReelLanding).trim();
				}
				
				String checkSpinStop=null;
				if(currentRow.get(Constant.checkSpinStop)!=null) 
				{
					checkSpinStop=currentRow.get(Constant.checkSpinStop).trim();
				}		
				
				String checkSpinStopUsingCoordinates=null;
				if(currentRow.get(Constant.checkSpinStopUsingCoordinates)!=null) 
				{
					checkSpinStopUsingCoordinates=currentRow.get(Constant.checkSpinStopUsingCoordinates).trim();
				}
				
				//******************* QUICK SPIN
				String QuickSpinFeatures=null;
				if(currentRow.get(Constant.QuickSpinFeatures)!=null) 
				{
					QuickSpinFeatures=currentRow.get(Constant.QuickSpinFeatures).trim();
				}
				
				//****************** VALUE ADDS
				String ValueAddsNavigation=null;
				if(currentRow.get(Constant.ValueAddsNavigation)!=null) 
				{
					ValueAddsNavigation=currentRow.get(Constant.ValueAddsNavigation).trim();
				}
				String PlayerProtectionNavigation=null;
				if(currentRow.get(Constant.PlayerProtectionNavigation)!=null) 
				{
					PlayerProtectionNavigation=currentRow.get(Constant.PlayerProtectionNavigation).trim();
				}
				
				String CashCheckNavigation=null;
				if(currentRow.get(Constant.CashCheckNavigation)!=null) 
				{
					CashCheckNavigation=currentRow.get(Constant.CashCheckNavigation).trim();
				}
								
				String HelpNavigation=null;
				if(currentRow.get(Constant.HelpNavigation)!=null) 
				{
					HelpNavigation=currentRow.get(Constant.HelpNavigation).trim();
				}
				
				String PlayCheckNavigation=null;
				if(currentRow.get(Constant.PlayCheckNavigation)!=null) 
				{
					PlayCheckNavigation=currentRow.get(Constant.PlayCheckNavigation).trim();
				}
								
				//************* AUTOPLAY************
				String AutoplayPanel=null;
				if(currentRow.get(Constant.AutoplayPanel)!=null) 
				{
					AutoplayPanel=currentRow.get(Constant.AutoplayPanel).trim();
				}
				
				//************* BetPanel************
				String BetPanel=null;
				if(currentRow.get(Constant.BetPanel)!=null) 
				{
					BetPanel=currentRow.get(Constant.BetPanel).trim();
				}
				//************* MenuPanel************
				String MenuPanel=null;
				if(currentRow.get(Constant.MenuPanel)!=null) 
				{
					MenuPanel=currentRow.get(Constant.MenuPanel).trim();
				}
				//************* PaytableFeatures************
				String PaytableFeatures=null;
				if(currentRow.get(Constant.PaytableFeatures)!=null) 
				{
					PaytableFeatures=currentRow.get(Constant.PaytableFeatures).trim();
				}
				
				//************* SettingsPanel************
				String SettingsPanel=null;
				if(currentRow.get(Constant.SettingsPanel)!=null) 
				{
					SettingsPanel=currentRow.get(Constant.SettingsPanel).trim();
				}
			
				
				//Session Reminder
				String SessionReminder=null;
				if(currentRow.get(Constant.SessionReminder)!=null) 
				{
					SessionReminder=currentRow.get(Constant.SessionReminder).trim();
				}
				
				String SessionReminderUserInteraction=null;
				if(currentRow.get(Constant.SessionReminderUserInteraction)!=null) 
				{
					SessionReminderUserInteraction=currentRow.get(Constant.SessionReminderUserInteraction).trim();
				}
				
				String SessionReminderContinue=null;
				if(currentRow.get(Constant.SessionReminderContinue)!=null) 
				{
					SessionReminderContinue=currentRow.get(Constant.SessionReminderContinue).trim();
				}
				
				String SessionReminderExitGame=null;
				if(currentRow.get(Constant.SessionReminderExitGame)!=null) 
				{
					SessionReminderExitGame=currentRow.get(Constant.SessionReminderExitGame).trim();
				}
				
				//CMA
				String BounsFundsNotification=null;
				if(currentRow.get(Constant.BounsFundsNotification)!=null) 
				{
					BounsFundsNotification=currentRow.get(Constant.BounsFundsNotification).trim();
				}
				
				String closeBtnEnabledCMA=null;
				if(currentRow.get(Constant.closeBtnEnabledCMA)!=null) 
				{
					closeBtnEnabledCMA=currentRow.get(Constant.closeBtnEnabledCMA).trim();
				}
				
				//Banking
				String BankingNavigation=null;
				if(currentRow.get(Constant.BankingNavigation)!=null) 
				{
					BankingNavigation=currentRow.get(Constant.BankingNavigation).trim();
				}
				
				String BalanceUpdateUsingBanking=null;
				if(currentRow.get(Constant.BalanceUpdateUsingBanking)!=null) 
				{
					BalanceUpdateUsingBanking=currentRow.get(Constant.BalanceUpdateUsingBanking).trim();
				}
				
				//Net position
				String NetPositionBaseScene=null;
				if(currentRow.get(Constant.NetPositionBaseScene)!=null) 
				{
					NetPositionBaseScene=currentRow.get(Constant.NetPositionBaseScene).trim();
				}
				
				String NetPositionLaunch=null;
				if(currentRow.get(Constant.NetPositionLaunch)!=null) 
				{
					NetPositionLaunch=currentRow.get(Constant.NetPositionLaunch).trim();
				}
				
				String NetPositionRefresh=null;
				if(currentRow.get(Constant.NetPositionRefresh)!=null) 
				{
					NetPositionRefresh=currentRow.get(Constant.NetPositionRefresh).trim();
				}
				
				String NetPositionWinLoss=null;
				if(currentRow.get(Constant.NetPositionWinLoss)!=null) 
				{
					NetPositionWinLoss=currentRow.get(Constant.NetPositionWinLoss).trim();
				}
				
				String NetPositionBigWin=null;
				if(currentRow.get(Constant.NetPositionBigWin)!=null) 
				{
					NetPositionBigWin=currentRow.get(Constant.NetPositionBigWin).trim();
				}
				
				String NetPositionFreeSpin=null;
				if(currentRow.get(Constant.NetPositionFreeSpin)!=null) 
				{
					NetPositionFreeSpin=currentRow.get(Constant.NetPositionFreeSpin).trim();
				}
				
				String NetPositionBonusFeature=null;
				if(currentRow.get(Constant.NetPositionBonusFeature)!=null) 
				{
					NetPositionBonusFeature=currentRow.get(Constant.NetPositionBonusFeature).trim();
				}
				
				String NetPositionRefreshWin=null;
				if(currentRow.get(Constant.NetPositionRefreshWin)!=null) 
				{
					NetPositionRefreshWin=currentRow.get(Constant.NetPositionRefreshWin).trim();
				}
				
				String NetPositionRefreshLoss=null;
				if(currentRow.get(Constant.NetPositionRefreshLoss)!=null) 
				{
					NetPositionRefreshLoss=currentRow.get(Constant.NetPositionRefreshLoss).trim();
				}
				String PracticePlay=null;
				if(currentRow.get(Constant.PracticePlay)!=null) 
				{
					PracticePlay=currentRow.get(Constant.PracticePlay).trim();
				}
				
				String SessionDurationReset=null;
				if(currentRow.get(Constant.SessionDurationReset)!=null) 
				{
					SessionDurationReset=currentRow.get(Constant.SessionDurationReset).trim();
				}
				
				// Wager RTS
				String WagerRTS=null;
				if(currentRow.get(Constant.WagerRTS)!=null) 
				{
					WagerRTS=currentRow.get(Constant.WagerRTS).trim();
				}
				
				String SpinScenarios=null;
				if(currentRow.get(Constant.SpinScenarios)!=null) 
				{
					SpinScenarios=currentRow.get(Constant.SpinScenarios).trim();
				}  
				
				String LoadGameWithCredits=null;
				if(currentRow.get(Constant.LoadGameWithCredits)!=null) 
				{
					LoadGameWithCredits=currentRow.get(Constant.LoadGameWithCredits).trim();
				}
				
				String LoadGameWithoutCredits=null;
				if(currentRow.get(Constant.LoadGameWithoutCredits)!=null) 
				{
					LoadGameWithoutCredits=currentRow.get(Constant.LoadGameWithoutCredits).trim();
				}
				
				// Free Games
				String FreeGameFeatures=null;
				if(currentRow.get(Constant.FreeGameFeatures)!=null) 
				{
					FreeGameFeatures=currentRow.get(Constant.FreeGameFeatures).trim();
				}
				
				String FGOfferAccept=null;
				if(currentRow.get(Constant.FGOfferAccept)!=null) 
				{
					FGOfferAccept=currentRow.get(Constant.FGOfferAccept).trim();
				}
				
				String FGPlayLater=null;
				if(currentRow.get(Constant.FGPlayLater)!=null) 
				{
					FGPlayLater=currentRow.get(Constant.FGPlayLater).trim();
				}
				
				String FGOfferResume=null;
				if(currentRow.get(Constant.FGOfferResume)!=null) 
				{
					FGOfferResume=currentRow.get(Constant.FGOfferResume).trim();
				}
				
				String FGOfferDelete=null;
				if(currentRow.get(Constant.FGOfferDelete)!=null) 
				{
					FGOfferDelete=currentRow.get(Constant.FGOfferDelete).trim();
				}
				
				String FGSummaryScreen=null;
				if(currentRow.get(Constant.FGSummaryScreen)!=null) 
				{
					FGSummaryScreen=currentRow.get(Constant.FGSummaryScreen).trim();
				}
				
				String FreeSpinsInFG=null;
				if(currentRow.get(Constant.FreeSpinsInFG)!=null) 
				{
					FreeSpinsInFG=currentRow.get(Constant.FreeSpinsInFG).trim();
				}

				// Free Spin
				String FreeSpinFeatures=null;
				if(currentRow.get(Constant.FreeSpinFeatures)!=null) 
				{
					FreeSpinFeatures=currentRow.get(Constant.FreeSpinFeatures).trim();
				}
				
				String FSRefresh=null;
				if(currentRow.get(Constant.FSRefresh)!=null) 
				{
					FSRefresh=currentRow.get(Constant.FSRefresh).trim();
				}
				
				String FSSessionReminder=null;
				if(currentRow.get(Constant.FSSessionReminder)!=null) 
				{
					FSSessionReminder=currentRow.get(Constant.FSSessionReminder).trim();
				}
				
				String FreeSpinReelLanding=null;
				if(currentRow.get(Constant.FreeSpinReelLanding)!=null) 
				{
					FreeSpinReelLanding=currentRow.get(Constant.FreeSpinReelLanding).trim();
				}
				
				String FreeSpinTransitionBaseGameToFeature=null;
				if(currentRow.get(Constant.FreeSpinTransitionBaseGameToFeature)!=null) 
				{
					FreeSpinTransitionBaseGameToFeature=currentRow.get(Constant.FreeSpinTransitionBaseGameToFeature).trim();
				}
				
				String FreeSpinTransitionFeatureToBaseGame=null;
				if(currentRow.get(Constant.FreeSpinTransitionFeatureToBaseGame)!=null) 
				{
					FreeSpinTransitionFeatureToBaseGame=currentRow.get(Constant.FreeSpinTransitionFeatureToBaseGame).trim();
				}
				
				String FreeSpinTransitionFeatureToBaseGameRefresh=null;
				if(currentRow.get(Constant.FreeSpinTransitionFeatureToBaseGameRefresh)!=null) 
				{
					FreeSpinTransitionFeatureToBaseGameRefresh=currentRow.get(Constant.FreeSpinTransitionFeatureToBaseGameRefresh).trim();
				}
				
				String NetPositionInFS=null;
				if(currentRow.get(Constant.NetPositionInFS)!=null) 
				{
					NetPositionInFS=currentRow.get(Constant.NetPositionInFS).trim();
				}
			
				//Bonus
				String BonusFeatures=null;
				if(currentRow.get(Constant.BonusFeatures)!=null) 
				{
					BonusFeatures=currentRow.get(Constant.BonusFeatures).trim();
				}
				
				String BonusTransitionBaseGameToFeature=null;
				if(currentRow.get(Constant.BonusTransitionBaseGameToFeature)!=null) 
				{
					BonusTransitionBaseGameToFeature=currentRow.get(Constant.BonusTransitionBaseGameToFeature).trim();
				}
				
				String BonusTransitionFeatureToBaseGame=null;
				if(currentRow.get(Constant.BonusTransitionFeatureToBaseGame)!=null) 
				{
					BonusTransitionFeatureToBaseGame=currentRow.get(Constant.BonusTransitionFeatureToBaseGame).trim();
				}
				
				String BonusTransitionBaseGameToFeatureRefresh=null;
				if(currentRow.get(Constant.BonusTransitionBaseGameToFeatureRefresh)!=null) 
				{
					BonusTransitionBaseGameToFeatureRefresh=currentRow.get(Constant.BonusTransitionBaseGameToFeatureRefresh).trim();
				}
				
				String BonusTransitionFeatureToBaseGameRefresh=null;
				if(currentRow.get(Constant.BonusTransitionFeatureToBaseGameRefresh)!=null) 
				{
					BonusTransitionFeatureToBaseGameRefresh=currentRow.get(Constant.BonusTransitionFeatureToBaseGameRefresh).trim();
				}
							
				String BonusSessionReminder=null;
				if(currentRow.get(Constant.BonusSessionReminder)!=null) 
				{
					BonusSessionReminder=currentRow.get(Constant.BonusSessionReminder).trim();
				}
				
				String BonusNetPosition=null;
				if(currentRow.get(Constant.BonusNetPosition)!=null) 
				{
					BonusNetPosition=currentRow.get(Constant.BonusNetPosition).trim();
				}
				
				String BonusSelection=null;
				if(currentRow.get(Constant.BonusSelection)!=null) 
				{
					BonusSelection=currentRow.get(Constant.BonusSelection).trim();
				}
			
				String BonusReelLanding=null;
				if(currentRow.get(Constant.BonusReelLanding)!=null) 
				{
					BonusReelLanding=currentRow.get(Constant.BonusReelLanding).trim();
				}
				
				String BonusRefresh=null;
				if(currentRow.get(Constant.BonusRefresh)!=null) 
				{
					BonusRefresh=currentRow.get(Constant.BonusRefresh).trim();
				}
				
				String BonusInFreeGames=null;
				if(currentRow.get(Constant.BonusInFreeGames)!=null) 
				{
					BonusInFreeGames=currentRow.get(Constant.BonusInFreeGames).trim();
				}
				
				//Link and WIn
				String LinkAndWin=null;
				if(currentRow.get(Constant.LinkAndWin)!=null) 
				{
					LinkAndWin=currentRow.get(Constant.LinkAndWin).trim();
				}
				
				Map<String,String> marketRow=new HashMap<>();
				
			
				marketRow.put("ProductId", productId);
				marketRow.put("RegMarketName", countryName);
				marketRow.put("CurrencyIsoCode", currencyIsoCode);
				marketRow.put("MarketTypeId", marketTypeId);
				marketRow.put("Balance", balance);
				marketRow.put("RegExpression", regExpression);
				marketRow.put("market", market);
				marketRow.put("brand", brand);
				marketRow.put("retrunUrlParam", retrunUrlParam);		
				marketRow.put("marketUrl", marketUrl);
				marketRow.put("runFlag", runFlag);
				marketRow.put("Lang1", Lang1);
				marketRow.put("Lang2", Lang2);		
				marketRow.put("LangCode1", LangCode1);
				marketRow.put("LangCode2", LangCode2);
				marketRow.put("LanguageCount", LanguageCount);

				//Base Game
				marketRow.put("BaseGameFeatures",BaseGameFeatures);
				
				//info bar
				marketRow.put("InfoBarFeatures",InfoBarFeatures );
				
				// top bar
				marketRow.put("TopbarFeatures", TopbarFeatures);
				marketRow.put("gameClock", gameClock);
				marketRow.put("GameName", GameName);
				
				//Spin
				marketRow.put("SpinFeatures",SpinFeatures );	
				marketRow.put("SpinReelLanding", SpinReelLanding);		
				marketRow.put("checkSpinStop", checkSpinStop);
				marketRow.put("checkSpinStopUsingCoordinates", checkSpinStopUsingCoordinates);
				
				//QUick spin
				marketRow.put("QuickSpinFeatures", QuickSpinFeatures);
				
				// Value Adds
				marketRow.put("ValueAddsNavigation", ValueAddsNavigation);
				marketRow.put("PlayerProtectionNavigation", PlayerProtectionNavigation);
				marketRow.put("CashCheckNavigation", CashCheckNavigation);		
				marketRow.put("HelpNavigation", HelpNavigation);
				marketRow.put("PlayCheckNavigation", PlayCheckNavigation);
			
				// BetPanel
				marketRow.put("BetPanel", BetPanel);
				
				// MenuPanel
				marketRow.put("MenuPanel", MenuPanel);
				
				// PaytableFeatures
				marketRow.put("PaytableFeatures", PaytableFeatures);
				
				// SettingsPanel
				marketRow.put("SettingsPanel", SettingsPanel);
				
				// Autoplay
				marketRow.put("AutoplayPanel", AutoplayPanel);
				
				// Session Reminder
				marketRow.put("SessionReminder", SessionReminder);
				marketRow.put("SessionReminderUserInteraction", SessionReminderUserInteraction);
				marketRow.put("SessionReminderContinue", SessionReminderContinue);
				marketRow.put("SessionReminderExitGame", SessionReminderExitGame);
				
				// CMA
				marketRow.put("BounsFundsNotification", BounsFundsNotification);
				marketRow.put("closeBtnEnabledCMA", closeBtnEnabledCMA);
				
				// Banking
				marketRow.put("BankingNavigation", BankingNavigation);
				marketRow.put("BalanceUpdateUsingBanking", BalanceUpdateUsingBanking);
				
				// Net Position
				marketRow.put("NetPositionBaseScene", NetPositionBaseScene);		
				marketRow.put("NetPositionLaunch", NetPositionLaunch);
				marketRow.put("NetPositionRefresh", NetPositionRefresh);
				marketRow.put("NetPositionWinLoss", NetPositionWinLoss);
				marketRow.put("NetPositionBigWin", NetPositionBigWin);		
				marketRow.put("NetPositionFreeSpin", NetPositionFreeSpin);
				marketRow.put("NetPositionBonusFeature", NetPositionBonusFeature);
				marketRow.put("NetPositionRefreshWin", NetPositionRefreshWin);
				marketRow.put("NetPositionRefreshLoss", NetPositionRefreshLoss);
				
				// Session Duration
				marketRow.put("SessionDurationReset", SessionDurationReset);
				
				//Practice play
				marketRow.put("PracticePlay", PracticePlay);
				
				//Wager RTS				
				marketRow.put("WagerRTS", WagerRTS);
				marketRow.put("SpinScenarios", SpinScenarios);
				marketRow.put("LoadGameWithCredits", LoadGameWithCredits);
				marketRow.put("LoadGameWithoutCredits", LoadGameWithoutCredits);
							
				//Free Games
				marketRow.put("FGOfferAccept", FGOfferAccept);
				marketRow.put("FreeGameFeatures", FreeGameFeatures);
				marketRow.put("FGOfferResume", FGOfferResume);
				marketRow.put("FGOfferDelete", FGOfferDelete);
				marketRow.put("FGPlayLater", FGPlayLater);
				marketRow.put("FGSummaryScreen", FGSummaryScreen);
				marketRow.put("FreeSpinsInFG", FreeSpinsInFG);
				
				//Free spins
				marketRow.put("FreeSpinFeatures", FreeSpinFeatures);
				marketRow.put("FSRefresh", FSRefresh);
				marketRow.put("FSSessionReminder", FSSessionReminder);
				marketRow.put("FreeSpinReelLanding", FreeSpinReelLanding);
				marketRow.put("FreeSpinTransitionBaseGameToFeature", FreeSpinTransitionBaseGameToFeature);
				marketRow.put("FreeSpinTransitionFeatureToBaseGame", FreeSpinTransitionFeatureToBaseGame);
				marketRow.put("FreeSpinTransitionFeatureToBaseGameRefresh", FreeSpinTransitionFeatureToBaseGameRefresh);
				marketRow.put("NetPositionInFS", NetPositionInFS);
				
				//Bonus				
				marketRow.put("BonusFeatures", BonusFeatures);
				marketRow.put("BonusTransitionBaseGameToFeature", BonusTransitionBaseGameToFeature);
				marketRow.put("BonusTransitionFeatureToBaseGame", BonusTransitionFeatureToBaseGame);
				marketRow.put("BonusTransitionBaseGameToFeatureRefresh", BonusTransitionBaseGameToFeatureRefresh);
				marketRow.put("BonusTransitionFeatureToBaseGameRefresh", BonusTransitionFeatureToBaseGameRefresh);
				marketRow.put("BonusSessionReminder", BonusSessionReminder);
				marketRow.put("BonusNetPosition", BonusNetPosition);
				marketRow.put("BonusSelection", BonusSelection);
				marketRow.put("BonusReelLanding", BonusReelLanding);
				marketRow.put("BonusRefresh", BonusRefresh);
				marketRow.put("BonusInFreeGames", BonusInFreeGames);
				
				//Link and Win
				marketRow.put("LinkAndWin", LinkAndWin);
				
				marketList.add(marketRow);
			}
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		}
		return marketList;
	}
}
