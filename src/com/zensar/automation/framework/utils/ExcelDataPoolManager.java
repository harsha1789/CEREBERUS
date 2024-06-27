package com.zensar.automation.framework.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;



//-------------ExcelDataPoolManager class---------------//
/**
 * Excel Data pool manager class reads the suit file in .xls format and decides which module to be run
 * and which scripts to be run.
 * @author 
 */
public class ExcelDataPoolManager {

	Logger log = Logger.getLogger(ExcelDataPoolManager.class.getName());
	String fileName;
	Set<Entry<String,String>> set = null ;

	//------------Constructor ExcelDataPoolManager ----------------//
	public ExcelDataPoolManager() {
		super();
	}


	public ExcelDataPoolManager(String filepath) 
	{
		this.fileName=filepath;
	}

	/**
	 * This function reads the specified sheet in Excel file provided by the arguments
	 * @param FilePath Path of the suit file
	 * @param sSheetName Name of the sheet to be read
	 * @return List
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 * @throws EncryptedDocumentException 
	 * @throws Exception 
	 */
	//------------readExcel() function----------------//

	public List<Map<String,String>> readExcel(String xlsFilePath , String sSheetName) 
	{
		Iterator<Row> mitrRows;
		Iterator<Cell> mitrCells;
		Row mhssfrowRow = null;
		List<Map<String,String>> mlistData;
		mlistData = new ArrayList<>();
		int noOfRows=0;
		try(Workbook workbook= WorkbookFactory.create(new FileInputStream(xlsFilePath))){
			workbook.setMissingCellPolicy(MissingCellPolicy.RETURN_BLANK_AS_NULL);
			Sheet msheetSheet=workbook.getSheet(sSheetName);

			mhssfrowRow = null;
			mitrRows = msheetSheet.rowIterator();
			noOfRows = msheetSheet.getPhysicalNumberOfRows();
			Map<String,String> hm = null;

			//------------Read no of modules to run----------------//
			for (int roeCnt = 1; (roeCnt < noOfRows) && (mitrRows.hasNext()); roeCnt++)
			{
				hm = new HashMap<>(); 
				mhssfrowRow =  mitrRows.next();
				mitrCells = mhssfrowRow.cellIterator();
				int columnCnt=mhssfrowRow.getPhysicalNumberOfCells();
				for( int cellCnt=0 ;(cellCnt<columnCnt)&&(mitrCells.hasNext()) ; cellCnt++) 
				{
					mitrCells.next();
					if(roeCnt>0)
					{
						hm.put(getValue(msheetSheet.getRow(0).getCell(cellCnt)), getValue(msheetSheet.getRow(roeCnt).getCell(cellCnt)));
					}
				}
				//------------Get a set of the entries----------------// 
				mlistData.add(hm); 

			}
		}catch(InvalidFormatException invalidFormatException) {
			log.error("While reading excel sheet:: "+invalidFormatException.getMessage(),invalidFormatException);
		}catch(IOException ioException) {
			log.error("IOException While reading excel sheet:: "+ioException.getMessage(),ioException);
		}
		//------------Return from function----------------//
		return(mlistData);
	}


	/**
	 * This function reads the Excel file By row
	 * @param FilePath Path of the suit file
	 * @param sSheetName Name of the sheet to be read
	 * @param iRow Total no of rows
	 * @return Map
	 * @throws IOException 
	 */
	//------------readExcelByRow() function----------------//

	public Map<String,String> readExcelByRow(String xlsFilePath , String sSheetName , int iRow) throws IOException {


		Map<String,String> hm = null;
		Iterator<Row> mitrRows;
		Iterator<Cell> mitrCells;
		Row mhssfrowRow = null;
		int noOfRows=0;

		//------------Declare Excel Sheet Variables----------------// 
		try(Workbook workbook= WorkbookFactory.create(new FileInputStream(xlsFilePath))){
			workbook.setMissingCellPolicy(MissingCellPolicy.RETURN_BLANK_AS_NULL);
			Sheet msheetSheet=workbook.getSheet(sSheetName);

			mitrRows = msheetSheet.rowIterator();
			noOfRows = msheetSheet.getPhysicalNumberOfRows();
			int cnt = 1;

			//------------Count total no of scripts in a Module----------------// 
			for (; (cnt < noOfRows) && (mitrRows.hasNext()); cnt++) {
				if( cnt == iRow ) {
					break;
				}
			}

			hm = new HashMap<>(); 
			mhssfrowRow =  mitrRows.next();
			mitrCells = mhssfrowRow.cellIterator();
			int cellCnt=0 ;
			for( ;mitrCells.hasNext() ; cellCnt++) {
				mitrCells.next();
				hm.put( getValue(msheetSheet.getRow(0).getCell(cellCnt)) , getValue(msheetSheet.getRow(iRow).getCell(cellCnt , Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)));
			}

		}catch(InvalidFormatException invalidFormatException) {
			log.error("While reading excel sheet By row:: "+invalidFormatException.getMessage(),invalidFormatException);
		}catch(IOException ioException) {
			log.error("IOException While reading excel sheet by row:: "+ioException.getMessage(),ioException);
		}
		//------------Return from function----------------//
		return(hm);
	}


	/**
	 * This function shows the data of the Specified row 
	 * @param hm 
	 * @return void
	 */

	//------------showRowData() function----------------//
	public void showRowData(Map<String,String> hm){
		set = hm.entrySet();
		Iterator<Entry<String, String>>  iteam = set.iterator();
		//------------Display elements----------------//
		while(iteam.hasNext()) {
			Map.Entry<String, String> me = iteam.next();
			log.info(me.getKey() );
		} 
	}


	/**
	 * This function shows the data of the excel specified by the List
	 * @param sheetData List of mapping 
	 * @return boolean
	 */
	//------------showExelData() function----------------//
	public boolean showExelData(List<Map<String,String>> sheetData) {
		Map<String,String> cell = null;
		for (int cnt = 0; cnt < sheetData.size(); cnt++) {
			cell =  sheetData.get(cnt);
			log.info( cell.get("Username") + " " + cell.get("Password"));
		}
		return(true);
	}

	/**
	 * Method will return the cell value depending upon the formate of the cell
	 * else return blank String
	 * @param cell
	 * @return String
	 */

	public String getValue(Cell cell) {
		String value=null;
		if(cell==null)
		{
			return "";
		}
		else{
			switch (cell.getCellTypeEnum()) {
			case BOOLEAN:
				value=Boolean.toString(cell.getBooleanCellValue());
				break;
			case STRING:
				value=cell.getRichStringCellValue().getString();
				break;
			case NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					value=(cell.getDateCellValue().toString());
				} else {
					value=Double.toString(cell.getNumericCellValue());
				}
				break;
			case FORMULA:
				value=cell.getCellFormula();
				break;
			case BLANK:
				value="";
				break;
			default:
				value="";
			}
		}
		return value;
	}
	/**
	 * This function will return the number of rows present in Sheet
	 * @param Sheet name ,of which reading the no of row
	 * @return Integer , no of rows
	 *Author: Anand Bhayre
	 */
	public int getrowcount(String sheetName) {
		int row=0;

		try( Workbook workbook = WorkbookFactory.create(new FileInputStream(fileName))){

			workbook.setMissingCellPolicy(MissingCellPolicy.RETURN_BLANK_AS_NULL);
			int totalsheets= workbook.getNumberOfSheets();

			for(int i=0;i<totalsheets; i++){
				Sheet sheet=workbook.getSheetAt(i);
				if(sheet.getSheetName().equals(sheetName)){
					row=sheet.getLastRowNum()+1;
					break;
				}
			}

		}catch(IOException  | InvalidFormatException exception)
		{
			log.error("Exception occur:"+exception.getMessage());
		}
		return row;
	}


	/**
	 * This function will return the number of columns present in Sheet
	 * @param Steing SheetName
	 * @return Integer , no of columns
	 *	Author: Anand Bhayre
	 */
	public int getColumncount(String sheetName) {

		int columncount=0;     
		try(Workbook workbook = WorkbookFactory.create(new FileInputStream(fileName))){
			workbook.setMissingCellPolicy(MissingCellPolicy.RETURN_BLANK_AS_NULL);
			int totalsheets= workbook.getNumberOfSheets();    

			for(int cnt=0; cnt<totalsheets; cnt++){

				Sheet sheet=workbook.getSheetAt(cnt);
				if(sheet.getSheetName().equals(sheetName)){
					Row row = sheet.getRow(0);
					columncount = row.getLastCellNum();
					break;
				}
			}
		}catch(IOException | InvalidFormatException exception)
		{
			log.error(exception.getStackTrace());
		}
		return columncount;

	}

	/**
	 * This function will return the cell data
	 * @param sheetName
	 * @param testcaseName
	 * @param columnname
	 * @return String ,cell data
	 * @throws IOException
	 */

	public String getCellData(String sheetName, String testcaseName, String columnname) throws IOException
	{
		int row = 0;
		try(Workbook workbook = WorkbookFactory.create(new FileInputStream(fileName))){
			workbook.setMissingCellPolicy(MissingCellPolicy.RETURN_BLANK_AS_NULL);
			int totalsheets= workbook.getNumberOfSheets();
			int sheetnum=0;

			for(int i=0; i<totalsheets; i++){
				Sheet sheet=workbook.getSheetAt(i);
				if(sheet.getSheetName().equals(sheetName))
				{
					sheetnum=i;
					break;
				}
			}
			row=getCellRowNumber(sheetnum+1, testcaseName);
		}catch(IOException |InvalidFormatException exception)
		{
			log.error(exception.getMessage());
		}
		return getCellData(sheetName, row, columnname);
	}

	/**
	 * This function will return the cell row number
	 * @param sheetNum
	 * @param cellText
	 * @return Integer ,cell row number
	 * @throws IOException
	 */
	public int getCellRowNumber(int sheetNum, String cellText) {

		int cellnum=0;
		try(Workbook workbook = WorkbookFactory.create(new FileInputStream(fileName))){
			workbook.setMissingCellPolicy(MissingCellPolicy.RETURN_BLANK_AS_NULL);
			Sheet sheet=workbook.getSheetAt(sheetNum-1);
			Row row = sheet.getRow(0);

			int totalrow= sheet.getLastRowNum();
			int totalcolumn = row.getLastCellNum();

			for(int rowCnt =0; rowCnt<=totalrow; rowCnt++){
				for(int columnCnt=0; columnCnt<=totalcolumn-1;columnCnt++){
					Row row1=sheet.getRow(rowCnt);
					Cell cell = row1.getCell(columnCnt);
					if(cell!=null)
					{
						String celldata=cell.getStringCellValue();
						if(celldata.equals(cellText)){
							cellnum=rowCnt+1;
							break;
						}
					}
				}
			}
		}
		catch(IOException | InvalidFormatException ioException)
		{
			log.error(ioException.getMessage());
		}
		return cellnum;

	}

	/**
	 * This will return the cell data
	 * @param sheetName
	 * @param rowNum
	 * @param colNum
	 * @return
	 * @throws IOException
	 */

	public String getCellData(String sheetName, int rowNum, int colNum) {


		String celldata=null;
		try(Workbook workbook = WorkbookFactory.create(new FileInputStream(fileName))){
			workbook.setMissingCellPolicy(MissingCellPolicy.RETURN_BLANK_AS_NULL);
			int totalsheets= workbook.getNumberOfSheets();

			for(int icnt=0; icnt<totalsheets; icnt++){
				Sheet sheet=workbook.getSheetAt(icnt);
				if(sheet.getSheetName().equals(sheetName)){
					Row row = sheet.getRow(rowNum-1);
					Cell cell= row.getCell(colNum-1);
					if(cell!=null){
						try {
							celldata= cell.getStringCellValue();
						} catch (Exception e) {
							celldata=String.valueOf(cell.getNumericCellValue());
						}
						break;
					}
					else
					{
						celldata="";
						break;
					}
				}
			}
		}catch (IOException |InvalidFormatException ioException)
		{
			log.error(ioException.getMessage());
		}
		return celldata;
	}


	/**
	 * This will return the cell data
	 * @param sheetName
	 * @param rowNum
	 * @param colName
	 * @return String 
	 * @throws IOException
	 */
	public String getCellData(String sheetName, int rowNum, String colName) throws IOException{

		String celldata=null;
		int colNum=0;
		String value=null;
		try(Workbook workbook = WorkbookFactory.create(new FileInputStream(fileName))){

			workbook.setMissingCellPolicy(MissingCellPolicy.RETURN_BLANK_AS_NULL);
			int totalsheets= workbook.getNumberOfSheets();

			for(int i=0; i<totalsheets; i++){
				Sheet sheet=workbook.getSheetAt(i);
				if(sheet.getSheetName().equals(sheetName)){
					Row row = sheet.getRow(rowNum-1);

					//to find the col name
					Row row1 = sheet.getRow(0);

					for(int j=0; j<row1.getLastCellNum(); j++){
						Cell cell1= row1.getCell(j);
						if(cell1.getStringCellValue().equals(colName)){
							colNum=cell1.getColumnIndex();
						}

					}
					Cell cell= row.getCell(colNum);
					if(cell==null)
						return "";

					else{
						switch (cell.getCellTypeEnum()) {
						case BOOLEAN:
							value=Boolean.toString(cell.getBooleanCellValue());
							break;
						case STRING:
							value=cell.getRichStringCellValue().getString();
							break;
						case NUMERIC:
							if (DateUtil.isCellDateFormatted(cell)) {
								value=(cell.getDateCellValue().toString());
							} else {
								value=Double.toString(cell.getNumericCellValue());
							}
							break;
						case FORMULA:
							value=cell.getCellFormula();
							break;
						case BLANK:
							value="";
							break;
						default:
							value="";
						}
						return value;

					}
				}
			}
		}catch(IOException | InvalidFormatException ioException)
		{
			log.error(ioException.getMessage());
		}
		return celldata;

	}

	/**
	 * This function gives the exact row count in the excel file
	 * @param xlsFilePath Path of the suit file
	 * @param sSheetName Name of the sheet
	 * @return int
	 * @throws IOException
	 */
	//------------rowCount() function----------------//
	public int rowCount(String xlsFilePath , String sSheetName) throws IOException 
	{
		int noOfRows = 0;
		try(Workbook workbook = WorkbookFactory.create(new FileInputStream(xlsFilePath))){
			workbook.setMissingCellPolicy(MissingCellPolicy.RETURN_BLANK_AS_NULL);
			Sheet msheetSheet=workbook.getSheet(sSheetName);

			noOfRows = msheetSheet.getPhysicalNumberOfRows();
		}catch(Exception exception)
		{
			log.error(exception.getMessage());
		}
		//------------Return rowCount from function----------------//

		return noOfRows;
	} 	
}
