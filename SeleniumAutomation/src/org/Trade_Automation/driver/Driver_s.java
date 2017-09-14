package org.Trade_Automation.driver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.Trade_Automation.functionlib.ExcelUtilsN;
import org.Trade_Automation.functionlib.Utilities;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.testing_Automation.utility.Utility;
import org.testng.annotations.Test;

@Test
public class Driver_s
{

	private static org.apache.log4j.Logger log = Logger
			.getLogger(Driver_s.class);
	private static String	                     errorDesc	= "NA";

	private static String	                     configpath	= System.getProperty("user.dir")
	                                                                + "\\Config\\config.properties";
	public static WebDriver	                     driver;

	private final static HashMap<String, Object>	mapIni	= new HashMap<String, Object>();

	@Test
	public static void run() throws Exception
	{
		log.debug("In run");
		FileOutputStream outFile = null;
		FileInputStream inputStream = null;
		ArrayList<String> KeyInfo = null;
		ArrayList<String> Partiestab = null;
		ArrayList<String> TermsandCondition = null;
		ArrayList<String> ChargesSettlement = null;
		String sTestCaseID = null;
		String sTestDescription = null;
		String sTestStatus = null;
		int colint;
		String updatefilepath = null;
		updatefilepath = Utilities.getProp("UPDATEFILEPATH", configpath, System.getProperty("user.dir"));
		System.out.println("updatefilepath" + updatefilepath);
		outFile = new FileOutputStream(new File(updatefilepath));
//		HSSFWorkbook workbook1 = new HSSFWorkbook();
//		HSSFSheet sheet = workbook1.createSheet("Report");

//		ExcelUtilsN.setExcelWSheet1(workbook1, "Report");
//		String[] header = { "TestCaseID", "Description", "Status", "Error Description" };

		String sPath = Utilities.getProp("DATAFILEPATH", configpath, System.getProperty("user.dir"));
		String testingFilePath = Utilities.getProp("TESTINGFILEPATH", configpath, System.getProperty("user.dir"));

		inputStream = new FileInputStream(sPath);

		HSSFWorkbook workbook = new HSSFWorkbook(inputStream);

		try
		{
			Action_Keywords.openBrowser(mapIni);
			Action_Keywords.navigate(mapIni);
			Action_Keywords.login("", "", "", "LOGIN", mapIni);
			driver = (WebDriver) mapIni.get(GENERIC_TESTSOL_XPATH_CONSTANTS.Driver_KEY);
			System.out.println("driver: " + driver);
			Utility util = new Utility();
			if (driver != null)
				// util.readBook(driver, JPMC_XPATH_Constants.testingFilePath,
				// mapIni);
				util.readBook(driver, testingFilePath, mapIni);
			else
				System.out.println("Unable to get the driver");

		}
		catch (Exception e)
		{
			//e.printStackTrace();
			//System.out.println("In exception");
			//e.printStackTrace();
			log.fatal("Exception: ",e);
		}
		finally
		{
			//workbook1.write(outFile);
			if(workbook!=null)
				workbook.close();
			Action_Keywords.driver.quit();
			outFile.flush();
			if(outFile!=null)
				outFile.close();
			if(inputStream!=null)
				inputStream.close();

		}

	}

	public static String setTestStatuses(Map<String, String> statusMap)
	{
		String sTestStatus;
		if ("true".equals(statusMap.get("result")))
		{
			sTestStatus = "PASS";
			errorDesc = statusMap.get("Error Message");

		}
		else if ("skipped".equals(statusMap.get("result")))
		{
			sTestStatus = "SKIPPED";
			errorDesc = statusMap.get("Error Message");

		}

		else
		{
			sTestStatus = "FAIL";
			errorDesc = statusMap.get("Error Message");

		}
		return sTestStatus;
	}

}
