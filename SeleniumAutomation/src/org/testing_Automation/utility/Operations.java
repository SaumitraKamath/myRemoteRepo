package org.testing_Automation.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.Trade_Automation.driver.GENERIC_TESTSOL_XPATH_CONSTANTS;
import org.Trade_Automation.functionlib.ExcelUtilsN;
import org.Trade_Automation.functionlib.Utilities;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Operations
{
	private static org.apache.log4j.Logger log = Logger.getLogger(Operations.class);
	// private static WebDriver driver = (WebDriver)
	// JPMC_XPATH_Constants.Driver_KEY;
	// ADD
	/**
	 * @param sOperation
	 * @param driver
	 * @param daoObject 
	 * @throws Exception
	 */
	public static void calling(String sOperation,  WebDriver driver, InputOutputDaoObject daoObject,ArrayList<String> arrdata)
	        throws Exception
	{
		// if ("Add".equalsIgnoreCase(sOperation) &&
		// "Registration".equalsIgnoreCase(getMenuEntityName))
		// {
		try {
			String menuEntityName = daoObject.getMenuEntityName();
			String testCaseId= daoObject.getTestCaseId();
			
			getRefId(sOperation, menuEntityName, testCaseId, driver,daoObject,arrdata);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.fatal("Exception in calling");
			throw e;
			//e.printStackTrace();
		}
		// }

	}

	/**
	 * @param sOperation
	 * @param menuEntityName
	 * @param testCaseId
	 * @param driver
	 * @param daoObject 
	 * @throws Exception
	 */
	public static void getRefId(String sOperation, String menuEntityName, String testCaseId, WebDriver driver, InputOutputDaoObject daoObject,ArrayList<String> arrdata)
	        throws Exception
	{
		try {
			log.debug("++++++++++++++in getRefId+++++++++++++++ ");
			Thread.sleep(5000);
			Set<String> wind = driver.getWindowHandles();
			log.debug("size   " + wind.size());
			ArrayList<String> arr = new ArrayList<String>();
			for (String win : wind)
			{
				arr.add(win);
				//log.debug("added>>>>>>" + win);
			}
			driver.switchTo().window(arr.get(1));
			driver.switchTo().defaultContent();
			driver.switchTo().frame("newIndex");
			String screenshotFlag=arrdata.get(3);
			//Added to take screenshot of the reference no generated
			if("Y".equals(screenshotFlag)){
				log.debug("inside actionbutton screenshot getRefId");
				Utility.takeScreenshot(driver, daoObject,arrdata);			
			}
			
			String ReffId = driver.findElement(By.xpath("/html/body/b/pre")).getText();
			String RefId = ReffId.trim();
			writexls(RefId, testCaseId);
			log.debug("ref Id-->" + RefId);
			driver.switchTo().defaultContent();
			driver.switchTo().frame("BUTTONFRAME");
			driver.findElement(By.xpath("//span[@class='Close']")).click();

			Set<String> winds = driver.getWindowHandles();
			log.debug("size   " + winds.size());
			for (String win : winds)
			{
				driver.switchTo().window(win);
			}
			// driver.navigate().refresh();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.fatal("Exception in getRefId");
			throw e;
			//e.printStackTrace();
		}

	}

	/**
	 * @param refid
	 * @param testCaseId
	 * @throws Exception
	 */
	public static void writexls(String refid, String testCaseId) throws Exception
	{
		HSSFWorkbook wb = null;
		HSSFSheet sheet;
		FileInputStream inFile = null;
		HashMap<String, String> data = new HashMap<String, String>();
		String configpath = System.getProperty("user.dir") + "\\Config\\config.properties";
		String resultfilepath = Utilities.getProp("RESULTFILEPATH", configpath, System.getProperty("user.dir"));
		FileOutputStream outFile = null;
		String[] header1 = { "TestCaseID", "Status" };
		try{
			inFile = new FileInputStream(resultfilepath);
			wb = new HSSFWorkbook(inFile);
	
			sheet = wb.getSheet("Report");
	
			outFile = new FileOutputStream(new File(resultfilepath));
	
			ExcelUtilsN.setExcelWSheet1(wb, "Report");
	
			log.debug("resultfilepath" + resultfilepath);
	
			String[] resultdata1 = { testCaseId, refid };
			//log.debug("header1-->" + header1.length + "header1-->" + header1.toString());
			ExcelUtilsN.preparePreManualResultSheet(sheet, header1, resultdata1, "PASS", "");

			wb.write(outFile);
		}catch(Exception e){
			//e.printStackTrace();
			log.fatal("Exception: ",e);
		}finally{
			if(inFile!=null)
				inFile.close();
			if(outFile!=null)
				outFile.close();
			if(wb!=null)
				wb.close();
		}

	}
}
