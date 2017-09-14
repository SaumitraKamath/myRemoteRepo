package org.Trade_Automation.functionlib;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;

public class ExcelUtilsN
{

	private static HSSFSheet	                       excelWSheet;
	private static HSSFWorkbook	                       excelWBook;

	private static HSSFCell	                           Cell;
	private static HashMap<String, ArrayList<Integer>>	testCaseRowMap	= new HashMap<String, ArrayList<Integer>>();
	static
	{
		FileInputStream excelFile =null;
		try
		{
			excelFile= new FileInputStream(Utilities.getProp("DATAFILEPATH", Utilities.configpath));
			excelWBook = new HSSFWorkbook(excelFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}finally{
			if(excelFile!=null)
				try {
					excelFile.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	/**
	 * 
	 */
	public static void populateHMap()
	{
		boolean firstTime = true;
		String currentCase = "";
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for (int i = 1; i <= excelWSheet.getLastRowNum(); i++)
		{
			if (firstTime)
			{
				temp.add(i);
				currentCase = excelWSheet.getRow(i).getCell(0).getStringCellValue();
				firstTime = false;
			}
			else if (currentCase.equals(excelWSheet.getRow(i).getCell(0).getStringCellValue()))
			{
				temp.add(i);
			}
			else
			{
				testCaseRowMap.put(currentCase, temp);
				currentCase = excelWSheet.getRow(i).getCell(0).getStringCellValue();
				temp = new ArrayList<Integer>();
				temp.add(i);
			}

		}
		testCaseRowMap.put(currentCase, temp);
	}

	/**
	 * @param excelWBook
	 * @param sheetName
	 */
	public static void setExcelWSheet1(HSSFWorkbook excelWBook, String sheetName)
	{
		excelWSheet = excelWBook.getSheet(sheetName);

	}

	/**
	 * @param sheetName
	 */
	public static void setExcelWSheet(String sheetName)
	{
		excelWSheet = excelWBook.getSheet(sheetName);

	}

	/**
	 * @return
	 */
	public static HSSFSheet getExcelWSheet()
	{
		return excelWSheet;
	}

	/**
	 * This method is to set the File path and to open the Excel file Pass Excel
	 * Path and SheetName as Arguments to this method
	 * 
	 * @param Path
	 * @param SheetName
	 * @throws Exception
	 */
	public static void setExcelFile(String Path, String SheetName) throws Exception
	{
		// FileInputStream ExcelFile = new FileInputStream(Path);
		FileInputStream excelFile = null;
		try{
		excelFile = new FileInputStream(Path);
		excelWBook = new HSSFWorkbook(excelFile);
		excelWSheet = excelWBook.getSheet(SheetName);
		}catch(Exception e){
			e.printStackTrace();
		} finally{
			if(excelFile!=null)
				excelFile.close();
				
		}
			
		
	}

	/**
	 * @param Path
	 * @param SheetName
	 * @throws Exception
	 */
	public static void setReportFile(String Path, String SheetName) throws Exception
	{
		// FileInputStream ExcelFile = new FileInputStream(Path);
		FileInputStream excelFile = null;
		try{
			excelFile=new FileInputStream(Path);
			excelWBook = new HSSFWorkbook(excelFile);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(excelFile!=null)
				excelFile.close();
			
		}
		
		
	}

	// This method is to read the test data from the Excel cell
	// In this we are passing parameters/arguments as Row Num and Col Num
	/**
	 * @param RowNum
	 * @param ColNum
	 * @return
	 * @throws Exception
	 */
	public static String getCellData(int RowNum, int ColNum) throws Exception
	{

		Cell = excelWSheet.getRow(RowNum).getCell(ColNum);

		String CellData = Cell.toString();

		try
		{
			CellData = Utilities.removeZeros(CellData);
		}
		catch (Exception e)
		{
		}
		return CellData;

	}

	/**
	 * @param irow
	 * @param value
	 * @param outFile
	 * @param workbook
	 * @throws Exception
	 */
	public static void setCellData(int irow, String value, FileOutputStream outFile, HSSFWorkbook workbook)
	        throws Exception
	{

		HSSFSheet sheet = workbook.getSheetAt(0);

		HSSFRow row = null;
		HSSFCell cell = null;

		// Update the value of cell
		System.out.println("irow inside setCellData: " + irow);

		row = sheet.createRow(irow);

		// System.out.println("Cell Value: "+sheet.getRow(irow).getCell(23));

		// cell.setCellValue(value);
		sheet.getRow(irow).createCell(irow);

		sheet.getRow(irow).getCell(0).setCellValue(value);
		System.out.println("Cell Value after: " + sheet.getRow(irow).getCell(0));

		System.out.println("Before writing output");
		workbook.write(outFile);

	}

	/**
	 * @param SheetName
	 * @return
	 * @throws IOException
	 */
	public static int getLastRowNum(String SheetName) throws IOException
	{
		String currentSheet = getExcelWSheet().getSheetName();
		int lastRowNum = 0;
		setExcelWSheet(SheetName);
		lastRowNum = excelWSheet.getLastRowNum();

		setExcelWSheet(currentSheet);
		return lastRowNum;
	}

	/**
	 * @param SheetName
	 * @return
	 * @throws Exception
	 */
	public static int getCol(String SheetName) throws Exception
	{
		// FileInputStream ExcelFile = new FileInputStream(Path);
		// excelWBook = new HSSFWorkbook(ExcelFile);
		excelWSheet = excelWBook.getSheet(SheetName);
		// System.out.println("Last Row"+excelWSheet.getLastRowNum());
		return excelWSheet.getRow(0).getLastCellNum();
	}

	/**
	 * @param Path
	 * @param SheetName
	 * @return
	 * @throws Exception
	 */
	public static int getCols(String Path, String SheetName) throws Exception
	{
		// FileInputStream ExcelFile = new FileInputStream(Path);
		// excelWBook = new HSSFWorkbook(ExcelFile);
		excelWSheet = excelWBook.getSheet(SheetName);
		// System.out.println("Last Row"+excelWSheet.getLastRowNum());
		return excelWSheet.getRow(0).getLastCellNum();
	}

	/**
	 * @param testConfigToExecute
	 * @throws Exception
	 */
	public static void getTestConfigToExecute(ArrayList<String> testConfigToExecute) throws Exception
	{
		String currentSheet = getExcelWSheet().getSheetName();
		setExcelWSheet("TestConfig");
		int noOfTestConfig = getExcelWSheet().getLastRowNum();
		for (int iRow = 1; iRow <= noOfTestConfig; iRow++)
		{
			if ("Y".equalsIgnoreCase(getCellData(iRow, 1)))
			{
				testConfigToExecute.add(getCellData(iRow, 0));
			}

		}
		setExcelWSheet(currentSheet);
	}

	/**
	 * @param tempTCID
	 * @return
	 */
	public static int getFirstTestStep(String tempTCID)
	{
		int row = 1;
		row = testCaseRowMap.get(tempTCID).get(0);
		return row;
	}

	/**
	 * @param tempTCID
	 * @return
	 */
	public static int getLastTestStep(String tempTCID)
	{
		int i = testCaseRowMap.get(tempTCID).size();
		i = testCaseRowMap.get(tempTCID).get(i - 1);
		return i;
	}

	/**
	 * @param xlsFile
	 * @param alert_header
	 * @return
	 * @throws IOException
	 */
	public static int getLastRowNum(String xlsFile, String alert_header) throws IOException
	{
		FileInputStream excelFile = null;
		HSSFWorkbook workbook=null;
		HSSFSheet sheet = null;
		try{
			excelFile= new FileInputStream(xlsFile);
			workbook = new HSSFWorkbook(excelFile);
			sheet = workbook.createSheet("Report");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(workbook!=null)
				workbook.close();
			if(excelFile!=null)
				excelFile.close();
			
		}

		return sheet.getLastRowNum();
	}

	/**
	 * @param dbQueue
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static String lookupData(String dbQueue) throws IOException, Exception
	{
		String uiQueue = "";
		String currentSheet = getExcelWSheet().getSheetName();
		setExcelWSheet("Queue_Lookup");
		for (int i = 0; i < excelWSheet.getLastRowNum(); i++)
		{
			if ((ExcelUtilsN.getCellData(i, 0).equalsIgnoreCase(dbQueue)))
			{
				uiQueue = ExcelUtilsN.getCellData(i, 1);
				System.out.println("Fetched data: " + uiQueue);
			}
		}
		setExcelWSheet(currentSheet);
		return uiQueue;

	}

	private static int	   rowCount	       = 0, colCount = 0;
	private static int	   rowCounts	   = 0, colCounts = 0;
	private static boolean	headerCreated	= false;
	private static boolean	headerCreatedd	= false;

	/**
	 * @param sheet
	 * @param header
	 * @param resultdata1
	 * @param sTestStatus
	 */
	public static void prepareTestResultSheet(HSSFSheet sheet, String[] header, String[] resultdata1, String sTestStatus)
	{

		if (!headerCreated)
		{
			Row row = sheet.createRow(rowCount++);
			for (String a : header)
			{
				Cell cell = row.createCell(colCount++);
				cell.setCellValue(a);
				System.out.println("Set cell is :" + cell.getStringCellValue());
			}
			headerCreated = true;
		}

		HashMap<String, Short> colorMap = new HashMap<String, Short>();
		colorMap.put("PASS", IndexedColors.GREEN.getIndex());
		colorMap.put("FAIL", IndexedColors.RED.getIndex());
		colorMap.put("SKIPPED", IndexedColors.INDIGO.getIndex());
		colorMap.put("Not Executed", IndexedColors.YELLOW.getIndex());
		colCount = 0;
		CellStyle style = sheet.getWorkbook().createCellStyle();
		System.out.println("Sheet name is :" + sheet.getSheetName());
		Row row1 = sheet.createRow(rowCount++);
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setFillForegroundColor(colorMap.get(sTestStatus));

		for (String cellValue : resultdata1)
		{
			Cell cell1 = row1.createCell(colCount++);
			if (cellValue.equals(sTestStatus))
			{
				cell1.setCellStyle(style);
				cell1.setCellValue(cellValue);
			}
			else
			{
				cell1.setCellValue(cellValue);
			}

		}
	}

	/**
	 * @param sheet
	 * @param header
	 * @param resultdata1
	 * @param sTestStatus
	 * @param sUser
	 * @throws IOException
	 */
	public static void preparePreManualResultSheet(HSSFSheet sheet, String[] header, String[] resultdata1,
	        String sTestStatus, String sUser) throws IOException
	{

		System.out.println("nitin header-->>" + header.toString() + " size-->" + header.length + "  headerCreatedd-->"
		        + headerCreatedd);

		if (!headerCreatedd)
		{
			Row row = sheet.createRow(rowCounts++);
			for (String a : header)
			{
				System.out.println(a);
				Cell cell = row.createCell(colCounts++);
				cell.setCellValue(a);
				System.out.println("Set cell is -->" + cell.getStringCellValue() + "rowCounts-1->" + rowCounts
				        + "colCounts-1->" + colCounts);
			}
			headerCreatedd = true;
		}

		HashMap<String, Short> colorMap = new HashMap<String, Short>();
		colorMap.put("PASS", IndexedColors.GREEN.getIndex());
		colorMap.put("FAIL", IndexedColors.RED.getIndex());
		colorMap.put("SKIPPED", IndexedColors.INDIGO.getIndex());
		colorMap.put("Not Executed", IndexedColors.YELLOW.getIndex());
		colCounts = 0;
		CellStyle style = sheet.getWorkbook().createCellStyle();
		System.out.println("Sheet name is :***********	" + sheet.getSheetName());
		Row row1 = sheet.createRow(rowCounts++);
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setFillForegroundColor(colorMap.get(sTestStatus));

		for (String cellValue : resultdata1)
		{
			Cell cell1 = row1.createCell(colCounts++);
			System.out.println("colCounts-2->" + colCounts);
			if (cellValue.equals(sTestStatus))
			{
				cell1.setCellStyle(style);
				cell1.setCellValue(cellValue);
			}
			else
			{
				cell1.setCellValue(cellValue);
			}

		}

	}
}