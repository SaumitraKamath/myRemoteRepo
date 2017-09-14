package org.testing_Automation.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import org.Trade_Automation.driver.Action_Keywords;
import org.Trade_Automation.driver.GENERIC_TESTSOL_XPATH_CONSTANTS;
import org.Trade_Automation.functionlib.Utilities;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * A dirty simple program that reads an Excel file.
 * 
 * @author www.codejava.net
 * 
 */
/**
 * @author sapna.jain
 *
 */
public class Utility
{

	public static WebDriver	     driver;
	public static WebDriver	     driver1	= null;

	private static WebDriverWait	wait	= null;
	private static String	     configpath	= System.getProperty("user.dir") + "\\Config\\config.properties";
	private static org.apache.log4j.Logger log = Logger.getLogger(Utility.class);

	/**
	 * @param driver
	 * @param excelFilePath
	 * @param mapIni
	 * @throws IOException
	 */
	public void readBook(WebDriver driver, String excelFilePath, HashMap mapIni) throws IOException
	{
		
		log.debug("In readbook");
		FileInputStream inputStream=null;
		Workbook workbook = null;
		try{
			inputStream = new FileInputStream(new File(excelFilePath));

			workbook= new HSSFWorkbook(inputStream);

			readOperationSheet(driver, workbook, mapIni);

		}finally{
			if(workbook!=null)
				workbook.close();
			if(inputStream!=null)
		 		inputStream.close();
		}

	}

	
	/**
	 * @param sEntityName
	 * @param sOperation
	 * @return
	 * @throws Exception 
	 */
	public HashMap<String, ArrayList> populateMaintainData(String sEntityName, String sOperation) throws Exception
	{

		log.debug("sOpearion" + sOperation);
		log.debug("sEntityName" + sEntityName);

		if (sOperation.equalsIgnoreCase("Update"))
		{
			sOperation = "Add";
		}
		else if (sOperation.equalsIgnoreCase("Select"))
		{
			return null;
		}

		StringBuilder sQuery = new StringBuilder().append(" select listagg(keyName, ', ') within group (order by seq_num) key_name,listagg(typeofattribute, ', ') within GROUP (ORDER BY seq_num) type,MIN(TabName ||'~' ||operation_mode) tab_name,listagg(action_button, ', ') within group (order by seq_num) action_button,BEFORE_AC_BU_FRAME,AFTER_AC_BU_FRAME,listagg(elementtype, ', ') within group (order by seq_num) Elementtype from service_fix_details having");
		sQuery.append( " TABNAME = ? and operation_mode = ?").append(" GROUP BY TabName,operation_mode,BEFORE_AC_BU_FRAME,AFTER_AC_BU_FRAME ");

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet res = null;
		HashMap<String, ArrayList> hashMap = new HashMap<String, ArrayList>();
		try
		{
			conn = getConnection();
			log.debug(sQuery.toString());
			pst = conn.prepareStatement(sQuery.toString());
			pst.setString(1, sEntityName);
			pst.setString(2, sOperation);
			

			res = pst.executeQuery();

			ArrayList<String> arNew = new ArrayList<String>();
			while (res.next())
			{
				arNew.add(res.getString("key_name"));
				arNew.add(res.getString("type"));
				arNew.add(res.getString("action_button"));
				arNew.add(res.getString("BEFORE_AC_BU_FRAME"));
				arNew.add(res.getString("AFTER_AC_BU_FRAME"));
				arNew.add(res.getString("Elementtype"));
				hashMap.put(res.getString("tab_name"), arNew);
			}

		}
		catch (Exception exe)
		{
			log.fatal("populateMaintainData Exception"+exe);
			//exe.printStackTrace();
			throw exe;
		}finally{
			try {
				if(res!=null)
					res.close();
				if(pst!=null)
					pst.close();
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				
				log.fatal("populateMaintainData SQLException"+e);
				throw new Exception(e);
			}
		}
		log.debug("Key Size ::" + hashMap.size());
		return hashMap;
	}

	/**
	 * @return
	 */
	public Connection getConnection()
	{
		java.sql.Connection conn = null;
		try
		{
			//log.debug("Taking Connection");
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// conn =
			// DriverManager.getConnection(GENERIC_TESTSOL_XPATH_CONSTANTS.DB_IP_ADDRESS,
			// GENERIC_TESTSOL_XPATH_CONSTANTS.DB_USER_NAME,
			// GENERIC_TESTSOL_XPATH_CONSTANTS.DB_PASSWORD);
			conn = DriverManager.getConnection(Utilities.getProp(GENERIC_TESTSOL_XPATH_CONSTANTS.DB_IP_ADDRESS,
			        GENERIC_TESTSOL_XPATH_CONSTANTS.CONFIGPATH), Utilities.getProp(
			        GENERIC_TESTSOL_XPATH_CONSTANTS.DB_USER_NAME, GENERIC_TESTSOL_XPATH_CONSTANTS.CONFIGPATH),
			        Utilities.getProp(GENERIC_TESTSOL_XPATH_CONSTANTS.DB_PASSWORD,
			                GENERIC_TESTSOL_XPATH_CONSTANTS.CONFIGPATH));
			//log.debug("Connection taken= " + conn);
		}
		catch (Exception exe)
		{
			//exe.printStackTrace();
			log.fatal("Exception: ",exe);

		}
		return conn;
	}

	/**
	 * @param driver
	 * @param workbook
	 * @param mapIni
	 */
	public void readOperationSheet(WebDriver driver, Workbook workbook, HashMap mapIni)
	{
		log.debug("In readOperationSheet");
	
		try
		{
			Sheet firstSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = firstSheet.iterator();
			while (iterator.hasNext())
			{
				ArrayList<InputOutputDaoObject> arrEntityDao = new ArrayList<InputOutputDaoObject>();
				InputOutputDaoObject daoObject = new InputOutputDaoObject();

				Row nextRow = iterator.next();
				log.debug("First Sheet row Num=" + nextRow.getRowNum());
				if (nextRow.getRowNum() == 0)
				{
					// log.debug("readOperationSheet Header Data");
					continue;
				}
				Iterator<Cell> cellIterator = nextRow.cellIterator();
				StringBuffer buffString = new StringBuffer();
				int iCount = 0;
				//while loop on first test sheet reading the first navigation sheet and putting the values in dao
				while (cellIterator.hasNext())
				{

					Cell cell = cellIterator.next();
					log.debug("Cell count" + iCount + "::Cell data" + cell.getStringCellValue());
					switch (iCount)
					{
						case 0:
							daoObject.setTestCaseId(cell.getStringCellValue());
							break;
						case 1:
							daoObject.setMenuEntityName(cell.getStringCellValue());
							break;
						case 2:
							if (!"".equalsIgnoreCase(cell.getStringCellValue().trim()))
							{
								ArrayList<String> entityNameData = new ArrayList<String>();
								entityNameData = formatRecord(cell.getStringCellValue(),
								        GENERIC_TESTSOL_XPATH_CONSTANTS.navigationSeperator);
								log.debug("entityNameData=" + entityNameData.toString());
								daoObject.setNavigationEntityName(entityNameData);
								log.debug("setNavigationEntityName=" + daoObject.getNavigationEntityname());
								daoObject.setMainEntityName(getEntityname(entityNameData));
								log.debug("setMainEntityName=" + daoObject.getMainEntityName());
								readDataSheet(workbook, daoObject);
							}

							break;
						case 3:
							daoObject.setChildEntityName(cell.getStringCellValue());
							break;
						case 4:
							daoObject.setUserName(cell.getStringCellValue());
							break;
						case 5:
							daoObject.setOperation(cell.getStringCellValue());
							break;
						default:
							break;
					}

					iCount++;
				}

				log.debug("Data Object ::" + daoObject);
				arrEntityDao.add(daoObject);

				executeOperation(driver, arrEntityDao, mapIni);

			}

		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	/**
	 * @param workbook
	 * @param daoObject
	 */
	public void readDataSheet(Workbook workbook, InputOutputDaoObject daoObject)
	{
		log.debug("In readDataSheet");
		try
		{
			Sheet firstSheet = workbook.getSheet(daoObject.getMenuEntityName());
			log.debug("readData firstSheet=" + firstSheet.getSheetName());
			Iterator<Row> iterator = firstSheet.iterator();
			log.debug("readData firstSheet=" + firstSheet.getSheetName());
			while (iterator.hasNext())
			{
				Row nextRow = iterator.next();
				log.debug("readDataSheet rowNum=" + nextRow.getRowNum());
				if (nextRow.getRowNum() == 0)
				{
					log.debug("readDataSheet Header Data");
					continue;
				}

				if (!isTestIdRow(nextRow, daoObject.getTestCaseId()))
				{
					log.debug("false isTestIdRow=" + daoObject.getTestCaseId());
					continue;
				}

				Iterator<Cell> cellIterator = nextRow.cellIterator();

				cellIterator.hasNext();
				StringBuffer buffData = new StringBuffer();
				
				for (int cn = 0; cn < nextRow.getLastCellNum(); cn++)
				{
					
					Cell cell = nextRow.getCell(cn, Row.CREATE_NULL_AS_BLANK);
					switch (cell.getCellType())
					{
						case Cell.CELL_TYPE_STRING:
							buffData.append(cell.getStringCellValue()).append("#");							
							break;
						case Cell.CELL_TYPE_BOOLEAN:
							buffData.append(cell.getBooleanCellValue()).append("#");
							break;
						case Cell.CELL_TYPE_NUMERIC:
							buffData.append(cell.getNumericCellValue()).append("#");							
							break;
						default:
							buffData.append("#");							
							break;
					}

				}

				if (buffData.length() > 0)
				{
					log.debug("buffData == > " + buffData);
					daoObject.addObject(buffData.substring(0, (buffData.length() - 1)));
				}
			}
		}
		catch (Exception e)
		{

			// TODO: handle exception
		}
	}

	/**
	 * @param nextRow
	 * @param sTestCaseId
	 * @return
	 */
	boolean isTestIdRow(Row nextRow, String sTestCaseId)
	{
		log.debug("Inside isTestIdRow : nextRow=" + nextRow + ":sTestCaseId=" + sTestCaseId);
		Iterator<Cell> cellIterator = nextRow.cellIterator();
		boolean isTestIdFound = false;
		if (cellIterator.hasNext())
		{
			Cell cell = cellIterator.next();
			log.debug("cell.getStringCellValue()" + cell.getStringCellValue());
			if (sTestCaseId.equalsIgnoreCase(cell.getStringCellValue()))
			{
				isTestIdFound = true;
			}
			else
			{
				isTestIdFound = false;
			}
		}
		log.debug("Inside isTestIdRow isTestIdFound=" + isTestIdFound);
		return isTestIdFound;
	}

	/**
	 * @param driver
	 * @param findMethod
	 * @param sEntityName
	 * @return
	 */
	public static WebElement findElement(WebDriver driver, String findMethod, String sEntityName)
	{
		WebElement webObj = null;
		try
		{

			if (findMethod.equalsIgnoreCase("ByName"))
			{
				webObj = Utility.getElementByName(driver, sEntityName);
			}
			else
			{
				webObj = Utility.getElementByxPath(driver, sEntityName);
			}

			while (webObj == null)
			{
				Thread.sleep(3000);
				webObj = Utility.findElement(driver, findMethod, sEntityName);
			}

		}
		catch (org.openqa.selenium.NoSuchFrameException exe)
		{
			return null;
		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		return webObj;
	}

	/**
	 * @param driver
	 * @param sEntityName
	 * @return
	 */
	public static WebElement getElementByName(WebDriver driver, String sEntityName)
	{
		WebElement webObj = null;

		try
		{
			webObj = driver.findElement(By.name(sEntityName));
		}
		catch (org.openqa.selenium.NoSuchElementException e)
		{
			return null;
		}
		catch (org.openqa.selenium.NoSuchWindowException exe)
		{
			return null;
		}
		return webObj;
	}

	/**
	 * @param driver
	 * @param sEntityName
	 * @return
	 */
	public static WebElement getElementByxPath(WebDriver driver, String sEntityName)
	{
		WebElement webObj = null;
		try
		{
			sEntityName = ".//*[@id='" + sEntityName + "']";
			log.debug("sEntityName =>" + sEntityName);
			webObj = driver.findElement(By.xpath(sEntityName));
		}
		catch (org.openqa.selenium.NoSuchElementException e)
		{
			// e.printStackTrace();
			return null;
		}
		catch (org.openqa.selenium.NoSuchWindowException exe)
		{
			return null;
		}
		return webObj;
	}

	/**
	 * @param element
	 * @param sInputValues
	 * @throws InterruptedException
	 */
	public static void selectValue(WebElement element, String sInputValues) throws InterruptedException
	{
		log.debug("MMEEMEMEM:::: " + element);
		Select dropdown = new Select(element);
		dropdown.selectByVisibleText(sInputValues);
	}

	/**
	 * @param element
	 * @param iTime
	 * @throws InterruptedException
	 */
	public static void click(WebElement element, int iTime) throws InterruptedException
	{
		element.click();
		Thread.sleep(iTime);
	}

	/**
	 * @param sUserName
	 * @param driver
	 * @return
	 * @throws InterruptedException
	 */
	public static boolean login(String sUserName, WebDriver driver) throws InterruptedException
	{

		//log.debug("login select value ");
		Thread.sleep(3000);
		driver.manage().window().maximize();
		Utility.selectValue(Utility.findElement(driver, "ByName", "ArmorTicket"), sUserName);
		Utility.click(Utility.findElement(driver, "ByName", "Login"), 3000);
		return true;
	}

	/**
	 * @param driver
	 * @param daoObject
	 * @throws InterruptedException
	 */
	public static void getMainFramePage(WebDriver driver, InputOutputDaoObject daoObject) throws InterruptedException
	{

		//log.debug("MMMMMMMMMMMMMMMMM");
		Thread.sleep(3000);
		driver.switchTo().defaultContent();
		Utility.switchToFrame(driver, "TopLevel");
		Utility.getLinks(driver, daoObject.getMenuEntityName(), 3000);

		driver.switchTo().defaultContent();

		// Thread.sleep(20000);
		Thread.sleep(20000);
		Utility.switchToFrame(driver, "MenuFrame");
		Utility.getLinks(driver, daoObject.getMainEntityName(), 3000);
		Thread.sleep(20000);

		Utility.getLinks(driver, daoObject.getChildEntityName(), 3000);

		driver.switchTo().defaultContent();
		Utility.switchToFrame(driver, "SearchFrame");//
		Thread.sleep(3000);

	}

	/**
	 * @param driver
	 * @param sFrameName
	 */
	public static void switchToFrame(WebDriver driver, String sFrameName)
	{
		try
		{
			driver.switchTo().frame(sFrameName);
		}
		catch (org.openqa.selenium.NoSuchFrameException exe)
		{
			exe.printStackTrace();
		}

	}

	/**
	 * @param driver
	 * @param sMenuName
	 * @param iSleepCount
	 */
	public static void getLinks(WebDriver driver, String sMenuName, int iSleepCount)
	{
		try
		{
			log.debug(sMenuName);
			List<WebElement> links = driver.findElements(By.tagName("a"));
			//log.debug(links.size());
			for (WebElement myElement : links)
			{
				String link = myElement.getText();
				//log.debug(link);
				if (link.trim().equalsIgnoreCase(sMenuName))
				{
					log.debug("Menu found ");
					myElement.click();
					break;
				}

			}
			Thread.sleep(iSleepCount);
		}
		catch (Exception e)
		{
			log.fatal("Exception: " , e);
		}
	}

	/**
	 * @param driver
	 * @param searchKey
	 */
	public static void searchRecord(WebDriver driver, String searchKey)
	{

	}

	/**
	 * @param sKey
	 * @param sSplitChar
	 * @return
	 */
	public static ArrayList<String> formatRecord(String sKey, String sSplitChar)
	{
		ArrayList<String> arrKey = null;
		if(log.isDebugEnabled()){
			log.debug("sKey=" + sKey);
			log.debug("sSplitChar=" + sSplitChar);
		}
		if (sKey != null && sKey.length() > 0)
		{
			try
			{
				arrKey = new ArrayList<String>(Arrays.asList((sKey.split(sSplitChar))));
			}
			catch (Exception exe)
			{
				//exe.printStackTrace();
				log.fatal("Exception: ",exe);
			}
		}
		log.debug("arrKey=" + arrKey.toString());
		return arrKey;
	}

	/**
	 * @param arrKey
	 * @return
	 */
	public static String getEntityname(ArrayList<String> arrKey)
	{
		String entityName = "";
		if (arrKey != null && !arrKey.isEmpty())
		{
			entityName = arrKey.get(arrKey.size() - 1);
		}
		log.debug("Last entityName" + entityName);
		return entityName;
	}

	/**
	 * @param element
	 * @param sInputValues
	 * @throws InterruptedException
	 */
	public static void populateTxt(WebElement element, String sInputValues) throws InterruptedException
	{
		element.clear();
		element.sendKeys(sInputValues);

	}

	/**
	 * @param driver
	 * @param TagName
	 * @param sAttributeName
	 * @param sMenuName
	 * @param iSleepCount
	 */
	public static void getMenuButton(WebDriver driver, String TagName, String sAttributeName, String sMenuName,
	        int iSleepCount)
	{
		try
		{
			List<WebElement> links = driver.findElements(By.tagName(TagName));
			for (WebElement myElement : links)
			{

				String link = myElement.getAttribute(sAttributeName);

				if (link.toUpperCase().contains(sMenuName.toUpperCase()))
				{
					log.debug(sMenuName + " found ");
					myElement.click();
					break;
				}
			}
			Thread.sleep(iSleepCount);

		}
		catch (Exception e)
		{
			log.fatal("Exception: " , e);
		}
		//log.debug("final out ");
	}

	/**
	 * @param driver
	 * @throws InterruptedException
	 */
	public static void waitForWindow(WebDriver driver) throws InterruptedException
	{

		int timecount = 1;
		do
		{
			driver.getWindowHandles();
			Thread.sleep(2000);
			timecount++;
			if (timecount > 30)
			{
				break;
			}
			log.debug(driver.getTitle());
		}
		while (driver.getWindowHandles().size() != 2);

	}

	/**
	 * @param driver
	 * @param parent
	 */
	public static void switchToModalDialog(WebDriver driver, String parent)
	{

		if (driver.getWindowHandles().size() == 2)
		{
			for (String window : driver.getWindowHandles())
			{
				if (!window.equals(parent))
				{

					driver.switchTo().window(window);
					log.debug(driver.getTitle());

					log.debug("Modal dialog found");
					break;
				}
			}
		}
	}

	/**
	 * @param driver
	 * @param strData
	 * @return
	 */
	public static String getListDetailsTab(WebDriver driver, String strData)
	{
		String strMessage = null;

		List<WebElement> fleetDetails = driver.findElements(By.tagName("td"));

		for (WebElement detail : fleetDetails)
		{
			strMessage = detail.getText();
			log.debug("Message ::" + strMessage);
			if (detail.getText().contains(strData))
			{
				log.debug("Data found");
				detail.click();
				break;
			}

		}
		return strMessage;
	}

	/**
	 * @param driver
	 * @param arrDataObject
	 * @param strKeyValue
	 * @return
	 */
	public static boolean selectRecord(WebDriver driver, ArrayList<String> arrDataObject, String strKeyValue)
	{
		boolean sRecordSelected = false;
		int iCount = 0;
		for (String sData : arrDataObject)
		{
			iCount++;
			if (iCount > 2 && sData != null && sData.length() > 1)
			{
				log.debug("sData = ==  > " + sData);
				Utility.getListDetailsTab(driver, sData);
				break;
			}
		}

		return sRecordSelected;
	}

	/**
	 * @param driver
	 * @param arrDataObject
	 * @param arrKeyName
	 * @return
	 * @throws InterruptedException
	 */
	public static boolean recordModify(WebDriver driver, ArrayList<String> arrDataObject, ArrayList<String> arrKeyName)
	        throws InterruptedException
	{
		if(log.isDebugEnabled()){
			log.debug("arrDataObject >" + arrDataObject);
			log.debug("arrKeyName >" + arrKeyName);
		}
		boolean sRecordSelected = false;
		int iCount = 0;

		arrDataObject = new ArrayList<String>(arrDataObject.subList(2, arrDataObject.size() - 1));
		log.debug("After mofidy arrDataObject >" + arrDataObject);
		for (String sData : arrDataObject)
		{
			if (sData != null && sData.length() > 0)
			{
				String sKeyName = arrKeyName.get(iCount).trim();
				log.debug(sKeyName + "=" + sData);

				populateTxt(findElement(driver, "xPath", sKeyName), sData);
			}
			iCount++;
		}
		sRecordSelected = true;

		return sRecordSelected;
	}

	/**
	 * @param driver
	 * @param iSec
	 */
	static public void wait(WebDriver driver, int iSec)
	{
		driver.manage().timeouts().implicitlyWait(iSec, TimeUnit.SECONDS);
	}

	/**
	 * @param Key
	 * @param arrdata
	 * @param type
	 * @param entype
	 * @param sactype
	 * @return
	 * @throws Exception 
	 */
	static public LinkedHashMap<String, String> formatHashData(ArrayList<String> Key, ArrayList<String> arrdata,
	        ArrayList<String> type, ArrayList<String> entype, ArrayList<String> sactype) throws Exception
	{
		LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
		if(log.isDebugEnabled()){
			log.debug("Key =>" + Key);
			
			log.debug("arrdata =>" + arrdata);
		}
		
		try
		{
			int xlsTotalValueCount=Integer.parseInt(Utilities.getProp(GENERIC_TESTSOL_XPATH_CONSTANTS.MADNATORY_CONFIG_CELL_COUNT,GENERIC_TESTSOL_XPATH_CONSTANTS.CONFIGPATH));//
			//int xlsTotalValueCount = 5;
			int xlsMandatoryConfigDataCount = xlsTotalValueCount;

			for (int i = 0; i < Key.size(); i++)
			{
				/*if (arrdata.get(Count).trim() == null)
				{
					//log.debug("Since arraydata null skipping for :" + Key.get(i));
				}
				else
				{*/
				//log.debug("XXXX"+ (arrdata.size()-tmp)+"i: "+i);
					if((arrdata.size()-xlsMandatoryConfigDataCount)>i){
						log.debug("Putting data for : " + Key.get(i) + " Count: "+xlsTotalValueCount+" Value: "+arrdata.get(xlsTotalValueCount) );
						//	log.debug("Putting data for : " + entype.get(i) + " Count: "+Count+" Value: "+arrdata.get(Count) );
						//	log.debug("Putting data for : " + sactype.get(i) + " Count: "+Count+" Value: "+arrdata.get(Count) );
						data.put(Key.get(i) + "~" + type.get(i) + "~" + entype.get(i) + "~" + sactype.get(i),
					        arrdata.get(xlsTotalValueCount));
					}else{
						log.debug("In else part");
						data.put(Key.get(i) + "~" + type.get(i) + "~" + entype.get(i) + "~" + sactype.get(i),
						        null);
					}
				//}

				xlsTotalValueCount++;
			}
		}
		catch (Exception exe)
		{
		//	exe.printStackTrace();
			log.fatal("Excpetion: ",exe);
			throw exe;
		}
		return data;
	}

	// Capture entire page screenshot and then store it to destination drive
	/**
	 * @param driver
	 * @param FilePath
	 * @param daoObject 
	 * @return
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static String takeScreenshot(WebDriver driver,  InputOutputDaoObject daoObject,ArrayList<String> arrdata) throws InterruptedException, IOException
	{
		String filePath = Utilities.getProp("SCREENSHOTSPATH", configpath, System.getProperty("user.dir"));
		//String filePath  = GENERIC_TESTSOL_XPATH_CONSTANTS.sceenshotsPath;
		String testCaseId = daoObject.getTestCaseId();		
		String operation = daoObject.getOperation();
		String soperation=arrdata.get(2);
		String mainEntityName= daoObject.getMainEntityName();		
		log.debug("In takeScreenShot TestcaseId: "+testCaseId +" operation: " +soperation+" mainEntityName: "+mainEntityName);		
		DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH-mm-ss");
		Date date = new Date();
		filePath = filePath + testCaseId+"_"+mainEntityName+"_"+soperation+"_"+dateFormat.format(date) + ".jpg";
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenshot, new File(filePath));
		filePath = filePath.replace("//", "/");
		log.debug("Screenshot is captured and stored in path:" + filePath);
		return filePath;
		
	}

	/**
	 * @param testCaseId
	 * @param result
	 * @param screenshotPath
	 * @throws Exception
	 */
	public static void updateExcelSheet(String testCaseId, String result, String screenshotPath) throws Exception
	{
		log.debug("Going to Update Result for TestcaseId=" + testCaseId + " with result as:" + result);
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		Workbook workbook = null;
		String testingFilePath = Utilities.getProp("TESTINGFILEPATH", configpath, System.getProperty("user.dir"));
		try
		{
			inputStream = new FileInputStream(new File(testingFilePath));
			workbook = new HSSFWorkbook(inputStream);
			Sheet firstSheet = workbook.getSheetAt(0);
			for (Row row : firstSheet)
			{
				for (Cell cell : row)
				{

					if (cell.getRichStringCellValue().getString().trim().equals(testCaseId))
					{
						log.debug("Found row to update>>>");
						Cell testCaseCell = row.getCell(6);
						testCaseCell.setCellValue(result);
						Cell screenshotPathCell = row.getCell(7);
						HSSFHyperlink file_link = new HSSFHyperlink(HSSFHyperlink.LINK_FILE);
						file_link.setAddress(screenshotPath);
						screenshotPathCell.setCellValue("Click to Open Screenshot");
						screenshotPathCell.setHyperlink(file_link);
					}

				}
			}
			inputStream.close();

			outputStream = new FileOutputStream(new File(testingFilePath));
			workbook.write(outputStream);
			outputStream.close();
		}
		catch (Exception e)
		{
			log.fatal("Exception in updateResult(): ",e);
			//e.printStackTrace();
		}
		finally
		{
			if(workbook!=null)
				workbook.close();
			if (inputStream != null)
				inputStream.close();
			if (outputStream != null)
				outputStream.close();			
		}
		log.debug("Finished Updating Result for TestcaseId=" + testCaseId + " with result as:" + result);
	}

	/**
	 * @param dao
	 * @param resultValue
	 * @throws IOException
	 */
	public static void updateExcelSheet(InputOutputDaoObject dao, String resultValue,ArrayList<String> arrdata) throws IOException
	{

		log.debug("Going to Update Request id for TestcaseId=" + dao.getTestCaseId() + " with request id as:"
		        + resultValue);
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		Workbook workbook = null;
		String testingFilePath = Utilities.getProp("TESTINGFILEPATH", configpath, System.getProperty("user.dir"));
		try
		{
			log.debug("dao.getMainEntityName()"+dao.getMenuEntityName());
			inputStream = new FileInputStream(new File(testingFilePath));
			workbook = new HSSFWorkbook(inputStream);
			Sheet sheetName = workbook.getSheet(dao.getMenuEntityName());
			Sheet navigationSheet = workbook.getSheetAt(0);
			log.debug("navigationSheet: "+navigationSheet.getSheetName());
			String tabdataname=arrdata.get(1);
			String operation=arrdata.get(2);
			log.debug("tabdataname "+tabdataname+" operation "+operation);
			log.debug("Sheet Updating=" + sheetName.getSheetName() + "test id=" + dao.getTestCaseId());
			for (Row row : sheetName)
			{ //log.debug("inside for loop of row"+sheetName);

				for (Cell cell : row)
				{//log.debug("inside for loop of cell");
					Cell testCaseCell1 = row.getCell(0);
					Cell testCaseCell2 = row.getCell(1);
					Cell testCaseCell3 = row.getCell(2);
					log.info("testCaseCell1: "+testCaseCell1+" testCaseCell2: "+testCaseCell2+" testCaseCell3: "+testCaseCell3);
					if(testCaseCell1!=null){
					if (testCaseCell1.getStringCellValue().trim().equals(dao.getTestCaseId()) && testCaseCell2.getStringCellValue().trim().equals(tabdataname) && testCaseCell3.getStringCellValue().trim().equals(operation))
					{
						CellStyle style = sheetName.getWorkbook().createCellStyle();
						Cell cell1=row.createCell(4);
						style.setFillPattern(CellStyle.SOLID_FOREGROUND);
						if(GENERIC_TESTSOL_XPATH_CONSTANTS.PASS.equals(resultValue)){
						style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
						} else if (GENERIC_TESTSOL_XPATH_CONSTANTS.FAIL.equals(resultValue)) {
						style.setFillForegroundColor(IndexedColors.RED.getIndex());
						}
						cell1.setCellStyle(style);
						cell1.setCellValue(resultValue);
//						Cell resultCell = row.getCell(4);
//						resultCell.setCellValue(resultValue);
						log.debug("Done Setting the request id");
					
						/*if (tabCell.getRichStringCellValue().getString().trim().equals(dao.getMenuEntityName())
						        && operationCell.getRichStringCellValue().getString().trim().equals("Fill"))
						{
							resultCell.setCellValue("PASS");
							log.debug("Done Setting the request id");
						}*/

					}
				}
				}

			}
			
			
			for (Row row : navigationSheet)
			{ //log.debug("inside for loop of row"+sheetName);

				for (Cell cell : row)
				{log.debug("inside for loop of cell for main sheets");
					Cell testCaseCell1 = row.getCell(0);
					Cell testCaseCell2 = row.getCell(1);
					//Cell testCaseCell3 = row.getCell(2);
					log.info("testCaseCell1: "+testCaseCell1+" testCaseCell2: "+testCaseCell2);
					if(testCaseCell1!=null){
					if (testCaseCell1.getStringCellValue().trim().equals(dao.getTestCaseId()) && testCaseCell2.getStringCellValue().trim().equals(dao.getMenuEntityName()) )
					{
						//Cell tabCell = row.getCell(1);
						//Cell operationCell = row.getCell(2);
						///if(row!=null){
						CellStyle style = sheetName.getWorkbook().createCellStyle();
						Cell cell1=row.createCell(5);
						style.setFillPattern(CellStyle.SOLID_FOREGROUND);
						if(GENERIC_TESTSOL_XPATH_CONSTANTS.PASS.equals(resultValue)){
						style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
						} else if (GENERIC_TESTSOL_XPATH_CONSTANTS.FAIL.equals(resultValue)) {
						style.setFillForegroundColor(IndexedColors.RED.getIndex());
						}
						cell1.setCellStyle(style);
						cell1.setCellValue(resultValue);
						//row.createCell(5).setCellValue(resultValue);
//						Cell resultCell = row.getCell(5);
//						resultCell.setCellValue(resultValue);
						///}
						log.debug("Done Setting the request id");
						
						/*if (tabCell.getRichStringCellValue().getString().trim().equals(dao.getMenuEntityName())
						        && operationCell.getRichStringCellValue().getString().trim().equals("Fill"))
						{
							resultCell.setCellValue("PASS");
							log.debug("Done Setting the request id");
						}*/

					}
					}
				}

			}
			inputStream.close();

			outputStream = new FileOutputStream(new File(testingFilePath));
			workbook.write(outputStream);
			outputStream.close();
		}
		catch (Exception e)
		{
			log.fatal("Exception in updateExelSheet(): ",e);
			//e.printStackTrace();
		}
		finally
		{
			if(workbook!=null)
				workbook.close();
			if (inputStream != null)
				inputStream.close();
			if (outputStream != null)
				outputStream.close();			
		}
		if(log.isDebugEnabled())
			log.debug("Finished Updating request id for TestcaseId=" + dao.getTestCaseId()
		        + " with request id as:" + resultValue);

	}


	
	/**
	 * @param driver
	 * @param arrEntityDao
	 * @param mapIni
	 */
	public void executeOperation(WebDriver driver, ArrayList<InputOutputDaoObject> arrEntityDao, HashMap mapIni)
	{
		log.debug("In executeOperation ---------------------------------------");
		System.out.println("executeOperation ---------------------------------");
		EventFactory ef = new EventFactory();
		log.debug("After EventFactory");
		log.debug("In executeOperation Title " + driver.getTitle());
		WebDriver webdriver1 = (WebDriver) mapIni.get(GENERIC_TESTSOL_XPATH_CONSTANTS.Driver_KEY);
		try
		{
			StartExecution exe = new StartExecution(mapIni);
			log.debug("After StartExecution: " + arrEntityDao.size());
			String linkPrefix = "//*[text()='";
			String linkSuffix = "']";
			// iterating on test case wise
			for (InputOutputDaoObject daoObject : arrEntityDao)
			{

				driver.navigate().refresh();
				log.debug("executeOperation daoObject=" + daoObject.toString());
				EventHandler event1 = null;
				
				StringTokenizer menutokenizer = new StringTokenizer(daoObject.getMainEntityName(), ",");
				log.debug("menutokenizer: "+menutokenizer);
				while (menutokenizer.hasMoreTokens())
				{
					String Link = linkPrefix + menutokenizer.nextToken() + linkSuffix;
					log.debug("Link:"+Link);

					Action_Keywords.clickOnMenu("", "", Link, "", "", mapIni, "");
				}

				log.debug("Now starting with the operations..");

				Utility.wait(driver, 5000);
				driver.switchTo().defaultContent();
				Utility.wait(driver, 300);
				log.debug("After switching to SearchFrame");

				log.debug("DataDao Final:: " + daoObject.getDataDao().toString());
				driver.switchTo().defaultContent();
				driver.switchTo().frame("SearchFrame");
//iterating second sheet which is on operation
				
				for (Object strData : daoObject.getDataDao())
				{
					log.debug("strData: " + strData);

					ArrayList<String> arrdata = exe.formatRecord(strData.toString(), "#");
					log.debug("arrdata is :: " + arrdata);
					String sActionButton = null;
					String sBeforeFrame = null;
					String sAfterFrame = null;
					String entityType = null;
					if (arrdata.isEmpty())
					{

						continue;
					}
						

					try {
						String sOperation = arrdata.get(2);
						String screenshotFlag=arrdata.get(3);
						System.out.println("screen shot "+screenshotFlag);
						HashMap<String, ArrayList> keyRecord = new HashMap();
						boolean isDbHitRequired = true;

						log.debug("before populateMaintainData call " + arrdata.get(1).toString()
						        + "and operation" + sOperation);

						if (isDbHitRequired)
						{
							keyRecord = populateMaintainData(arrdata.get(1).toString(), sOperation);
						}

						log.debug(keyRecord + " keyRecord.size()" + keyRecord.size());
						log.debug(daoObject.getMainEntityName() + "~" + sOperation);
						if (keyRecord != null && keyRecord.size() > 0)
						{

							ArrayList arKeyRecord = keyRecord.get(arrdata.get(1).toString() + "~" + sOperation);
					
							log.debug("Printig arKeyRecord ::" + arKeyRecord);
							String sKeyType = (String) arKeyRecord.get(0);
							log.debug("Printing sKeyType is ::" + sKeyType);
							String sAttributeType = (String) arKeyRecord.get(1);
							log.debug("Printing sAttributeType is ::" + sAttributeType);
							sActionButton = (String) arKeyRecord.get(2);
							log.debug("sActionButton ::" + sActionButton);
							sBeforeFrame = (String) arKeyRecord.get(3);
							log.debug("sBeforeFrame ::" + sBeforeFrame);
							sAfterFrame = (String) arKeyRecord.get(4);
							log.debug("sAfterFrame ::" + sAfterFrame);
							entityType = (String) arKeyRecord.get(5);
							log.debug("entityType ::" + entityType);
							ArrayList<String> entype = exe.formatRecord(entityType, ",");
							ArrayList<String> Key = exe.formatRecord(sKeyType, ",");
							ArrayList<String> sactype = exe.formatRecord(sActionButton, ",");
					
							ArrayList<String> type = exe.formatRecord(sAttributeType, ",");
							LinkedHashMap<String, String> hsmMappingData = Utility.formatHashData(Key, arrdata, type,
							        entype, sactype);
							log.debug("hsmMappingData is ::" + hsmMappingData);
							Iterator it = hsmMappingData.entrySet().iterator();
							while (it.hasNext())
							{
								Map.Entry pair = (Map.Entry) it.next();
								if (pair.getValue() != null && pair.getValue().toString().trim().length() > 0)
								{
									log.debug("Now inside HM loop ---------------" + pair.getKey().toString() + " = "  + pair.getValue().toString());
									ArrayList<String> separator = exe.formatRecord(pair.getKey().toString(), "~");
									

									if (separator.get(1).toString().contains(GENERIC_TESTSOL_XPATH_CONSTANTS.RADIO_BUTTON))
									{
										
										List oCheckBox = driver.findElements(By.name(separator.get(0).trim()));
										log.debug("after radio button 2: " + separator.get(0));

										int iSize = oCheckBox.size();
										

										for (int i = 0; i < iSize; i++)
										{
//										

											String sValue = ((WebElement) oCheckBox.get(i)).getAttribute("value");
											
											log.debug("Radio button -------------"+sValue);

											if (sValue.equalsIgnoreCase(pair.getValue().toString().trim()))
											{
												log.debug("inside if loop radio button" + pair.getValue().toString().trim());

												((WebElement) oCheckBox.get(i)).click();

												break;

											}
											Thread.sleep(2000);

										}
										

									}
									else if (separator.get(1).toString()
									        .contains(GENERIC_TESTSOL_XPATH_CONSTANTS.CHECK_BOX))
									{
										
										List oCheckBox = driver.findElements(By.name(separator.get(0).trim()));
										log.debug("after checkbox button" + separator.get(0));

										int iSize = oCheckBox.size();
										log.debug("size of list" + iSize);

										for (int i = 0; i < iSize; i++)
										{
											

											String sValue = ((WebElement) oCheckBox.get(i)).getAttribute("value");

											if (sValue.equalsIgnoreCase(pair.getValue().toString().trim()))
											{
												log.debug("inside if loop radio button"
												        + pair.getValue().toString().trim());

												((WebElement) oCheckBox.get(i)).click();

												break;

											}
											Thread.sleep(2000);

										}
										

									}
									else if (separator.get(1).toString()
									        .contains(GENERIC_TESTSOL_XPATH_CONSTANTS.DROP_DOWN))
									{

										log.debug("Dropdown event ..Key is :: " + separator.get(0)
										        + " And value is :: " + pair.getValue().toString() + "entype "
										        + separator.get(2));
										exe.selectValue(exe.findElement(separator.get(2).trim(), separator.get(0).trim()),
										        pair.getValue().toString().trim());

									}
									else if (separator.get(1).toString().contains(GENERIC_TESTSOL_XPATH_CONSTANTS.DIV))
									{

										log.debug("div event ..Key is :: " + separator.get(0)
										        + " And value is :: " + pair.getValue().toString() + "entype "
										        + separator.get(2));
										String xpathNavigation = separator.get(3);
										
										StringTokenizer xpathtokenizer = new StringTokenizer(xpathNavigation, "!");
										while (xpathtokenizer.hasMoreTokens())
										{
											
											String tokenpath = xpathtokenizer.nextToken();

											log.debug("tokenpath" + tokenpath);
											WebElement xpathClick = driver.findElement(By.xpath(tokenpath));
											
											log.debug("xpathClick: " + xpathClick);
//										

											xpathClick.click();

											log.debug("clicked on ele--------------->>>>>>>>>>>>>>>>>");
											Thread.sleep(5000);

										}

									}
									else if (separator.get(1).toString()
									        .contains(GENERIC_TESTSOL_XPATH_CONSTANTS.HYPER_LINK))
									{

										log.debug("HyperLink event ..Key is :: " + separator.get(0)
										        + " And value is :: " + pair.getValue().toString() + "entype "
										        + separator.get(2));
										String xpathNavigation = separator.get(3);
										log.debug("xpathNavigation" + xpathNavigation);
										StringTokenizer xhypertokenizer = new StringTokenizer(xpathNavigation, "!");
										while (xhypertokenizer.hasMoreTokens())
										{
											

											WebElement xphyperClick = driver.findElement(By.xpath(xhypertokenizer
											        .nextToken()));
											
											Thread.sleep(5000);
											xphyperClick.click();
										}
										
										Thread.sleep(3000);
										if("Y".equals(screenshotFlag)){
											log.debug("inside hyperlink screenshot capture");
											takeScreenshot(driver, daoObject,arrdata);										
										}

									}
									else if (separator.get(1).toString()
									        .contains(GENERIC_TESTSOL_XPATH_CONSTANTS.OPEN_WINDOW))
									{
										
										Thread.sleep(5000);
										Nfocusonwindow(driver, separator.get(3).trim());
										log.debug("driver title" + driver.getTitle());
										if("Y".equals(screenshotFlag)){
											log.debug("inside openwindow screenshot capture");
											takeScreenshot(driver, daoObject,arrdata);										
										}

									}
									else if (separator.get(1).toString()
									        .contains(GENERIC_TESTSOL_XPATH_CONSTANTS.CLICK_SUB_TAB))
									{
										
										String clickSubTabNavigation = separator.get(3).trim();
										log.debug("clickSubTabNavigation" + clickSubTabNavigation);
										clickSubTab(driver, "", "", clickSubTabNavigation);
										Thread.sleep(2000);
									}
									else if (separator.get(1).toString()
									        .contains(GENERIC_TESTSOL_XPATH_CONSTANTS.ACTION_BUTTON))
									{

										log.debug("button event ..Key is :: " + separator.get(0)
										        + " And value is :: " + pair.getValue().toString() + "entype "
										        + separator.get(2));
										String clickButtonName = separator.get(0).trim();
										log.debug("clickButtonName" + clickButtonName);
										String clickButtonNpath = separator.get(3).trim();
										log.debug("clickButtonNpath" + clickButtonNpath);

										if (StringUtils.isNotEmpty(sBeforeFrame))
										{
											driver.switchTo().defaultContent();
											StringTokenizer betokenizer = new StringTokenizer(sBeforeFrame, "*");
											while (betokenizer.hasMoreTokens())
											{

												driver.switchTo().frame(betokenizer.nextToken());
											}
										}

										WebElement tabname = driver.findElement(By.xpath(clickButtonNpath));
										JavascriptExecutor js = (JavascriptExecutor) driver;
					                    js.executeScript("arguments[0].click()", tabname);
										//tabname.click();
										log.debug("after click of button");
										if (StringUtils.isNotEmpty(sAfterFrame))
										{
											driver.switchTo().defaultContent();
											StringTokenizer aftokenizer = new StringTokenizer(sAfterFrame, "*");
											while (aftokenizer.hasMoreTokens())
											{

												driver.switchTo().frame(aftokenizer.nextToken());
											}
										}
										Thread.sleep(3000);
										if("Y".equals(screenshotFlag)){
											log.debug("inside actionbutton screenshot capture");
											takeScreenshot(driver, daoObject,arrdata);										
										}

									}
									else if (separator.get(1).toString()
									        .contains(GENERIC_TESTSOL_XPATH_CONSTANTS.CUSTOMIZED_OPERATION))
									{
										Thread.sleep(5000);
										log.debug("inside customized operation");
										Operations.calling(sOperation,  driver,daoObject,arrdata);
										Thread.sleep(2000);
									}
									else if (separator.get(1).toString()
									        .contains(GENERIC_TESTSOL_XPATH_CONSTANTS.TEXTBOX))
									{
										log.debug("text box event ..Key is :: " + separator.get(0)
								        + " And value is :: " + pair.getValue().toString() + "entype "
								        + separator.get(2));

										log.debug("window is ::" + driver.getCurrentUrl());
										log.debug("entitytype" + separator.get(2).toString());
										Utility.wait(driver, 300);
										String xlsValue = pair.getValue().toString().trim();
										//log.debug("XLS Value: "+xlsValue);
										//if(xlsValue!=null & !"".equalsIgnoreCase(xlsValue)){
											exe.populateTxt(exe.findElement(separator.get(2).trim(), separator.get(0).trim()),
												xlsValue);
										//}
										Thread.sleep(2000);
												
									}
									
									else
									{
										log.debug("text box event ..Key is :: " + separator.get(0)
										        + " And value is :: " + pair.getValue().toString() + "entype "
										        + separator.get(2));

										log.debug("window is ::" + driver.getCurrentUrl());
										log.debug("entitytype" + separator.get(2).toString());
										Utility.wait(driver, 300);
										String xlsValue = pair.getValue().toString().trim();
										//log.debug("XLS Value: "+xlsValue);
										//if(xlsValue!=null & !"".equalsIgnoreCase(xlsValue)){
											exe.populateTxt(exe.findElement(separator.get(2).trim(), separator.get(0).trim()),
												xlsValue);
										//}
										Thread.sleep(2000);
									}

								}
							}

						}

						log.debug("Operation name is ::" + sOperation);
						updateExcelSheet(daoObject,GENERIC_TESTSOL_XPATH_CONSTANTS.PASS,arrdata);

						
					}
						catch (Exception e) {
							// TODO Auto-generated catch block
							log.info("inner for loop catch exception");
							updateExcelSheet(daoObject,GENERIC_TESTSOL_XPATH_CONSTANTS.FAIL,arrdata);
							log.fatal("inner for loop catch exception fatal");
							
							
							break;
						}
				} 

			}

		}
		catch (Exception e)
		{
			log.fatal("Exception in executeOperation()", e);
			//e.printStackTrace();

		}

	}

	/**
	 * @param driver
	 * @param tcid
	 * @param tsid
	 * @param tdid
	 * @throws InterruptedException
	 */
		public static void clickSubTab(WebDriver driver, String tcid, String tsid, String tdid) throws Exception
		{
			try {
				log.debug("+++++++++Entering clickSubTab : " + tdid + "++++++++++++++++++++++++");

				// driver.switchTo().defaultContent();
				// driver.switchTo().frame("newIndex");
				// String a = "Terms & Conditions";
				// System.out.println("after switch");
				// WebElement xpathClick = driver.findElement(By.xpath("//*[text()='" +
				// tdid + "'] "));
				// System.out.println("xpathClick: " + xpathClick);
				// xpathClick.click();
				// WebElement tabname = driver.findElement(By.xpath("//*[(text()='" +
				// tdid + "')] "));
				if (tdid.contains("Key Info"))
				{
					log.debug("inside key info");
					WebElement kib = driver.findElement(By.xpath("//div[@id='tabpane3']/table/tbody/tr/td[1]"));
					log.debug(kib.getText());
					JavascriptExecutor jee = (JavascriptExecutor) driver;
					jee.executeScript("arguments[0].click();", kib);
				}
				else
				{
					log.debug("else key info");
					WebElement tabname = driver.findElement(By.xpath("//td[text()='" + tdid + "'] "));
					JavascriptExecutor je = (JavascriptExecutor) driver;
					je.executeScript("arguments[0].click();", tabname);
				}

				log.debug("+++++++++++++exit clickSubTab+++++++++++++++++++");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.fatal("Exception in clickSubTab");
				throw e;
				//e.printStackTrace();
			}
		}

	/**
	 * @param driver
	 * @param title
	 */
	private static void Nfocusonwindow(WebDriver driver, String title) throws Exception
	{

		try
		{

			//log.debug("+++++++++++++++Entering Nfocusonwindow+++++++++++++ ");

			log.debug("searching for :-->" + title);
			Set<String> window = driver.getWindowHandles();
			log.debug("total no of windows:-->" + window.size());
			Iterator<String> itr = window.iterator();
			while (itr.hasNext())
			{
				//log.debug("I am here 1");
				driver.switchTo().window(itr.next());

				//log.debug("I am here 2  " + driver.getTitle());
				if (title.equalsIgnoreCase(driver.getTitle()))
				{
					//log.debug("I am here 3");
					break;
				}

			}

			log.debug("You are on right window" + driver.getTitle());
		}
		catch (Exception ex)
		{
			try
			{
				throw ex;
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				log.fatal("Exception in Nfocusonwindow");
				throw e;
				//e.printStackTrace();
			}
		}
	}

	/**
	 * @param driver
	 * @param URL
	 * @return
	 */
	public static WebDriver winworkspace(WebDriver driver, String URL)
	{
		WebDriver wind = null;
		ArrayList<String> newTab = new ArrayList<String>(driver.getWindowHandles());
		for (int k = 0; k < newTab.size(); k++)
		{
			log.debug("before switch to window win6" + newTab.get(k));

			String title = driver.switchTo().window(newTab.get(k)).getTitle();
			log.debug("title>=" + title);
			log.debug("After switch to window win6");
			String wind1 = wind.getCurrentUrl();
			log.debug("Number of windows inside winworkspace :" + newTab.size());
			log.debug("wind1=" + wind1);
			if (wind1.equals(URL + "WorkspacePicklist.jsp"))
			{
				//log.debug("in workspace jsp");
				driver.switchTo().window(newTab.get(k));
				break;
			}

		}

		return wind;
	}

	/**
	 * @param driver
	 */
	public static void waitForPageLoaded(WebDriver driver)
	{
		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>()
		{
			@Override
			public Boolean apply(WebDriver driver)
			{

				return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString()
				        .equals("complete");
			}
		};
		log.debug("out");
		try
		{
			Thread.sleep(1000);
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(expectation);
		}
		catch (Throwable error)
		{

		}

	}
	
	
}