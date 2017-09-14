package org.Trade_Automation.driver;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.Trade_Automation.functionlib.Utilities;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Action_Keywords
{

	public static WebDriver	     driver;
	private static Utilities	 util;
	public static String	     clientfileid;
	public static String	     physicalfile;
	public static String	     batchrefno;
	public static String	     clientpaymentid;
	public static Boolean	     nextone	             = true;
	public static String	     query	                 = "select RATIONALE_LEVEL from tb_citialerts_data where workitemid = ? ";
	private static String	     checkFlag	             = "Y";
	private static String	     URL	                 = "http://10.10.7.83:20051/BPS/";
	private static StopWatch	 stopWatch	             = new StopWatch();
	private static WebDriverWait	wait	             = null;
	public static long	         filterTransactionIDtime	= 0L;
	private static final String	 ORPATH	                 = System.getProperty("user.dir") + "\\Object_Repository\\OR.properties";
	private static org.apache.log4j.Logger log = Logger.getLogger(Action_Keywords.class);

	public static boolean clickLink(String sTestCaseID, String sTestStepID, String line, String AlertID, String sUser,
	        HashMap c) throws InterruptedException
	{

		boolean bFndElement = false;

		try
		{

			log.debug("Inside clickLink - " + line);

			driver.switchTo().defaultContent();
			driver.switchTo().frame("SearchFrame");
			Thread.sleep(1000);

			//log.debug("clicked on link and id is: " + line);

			List<WebElement> element_a = driver.findElements(By
			        .xpath("//*[@class='x-grid3-cell-inner x-grid3-col-4']/a"));
			for (WebElement ele : element_a)
			{
				//log.debug("found element " + ele.getText());
				//log.debug("test data: " + line);
				if (ele.getText().equalsIgnoreCase(line))
				{
					// Thread.sleep(1000);
					bFndElement = true;
					ele.click();
					log.debug("clicked on link and id is: " + line);
					break;
				}
			}

		}
		catch (Exception e)
		{
			return bFndElement;

		}

		c.put("Error Message", "Click on Workitem Successful");
		c.put("result", "true");
		return bFndElement;

	}

	public static String checkPageIsReady()
	{

		JavascriptExecutor js = (JavascriptExecutor) driver;
		String ispageloaded = "false";

		// Initially bellow given if condition will check ready state of page.
		if (js.executeScript("return document.readyState").toString().equals("complete"))
		{
			ispageloaded = "true";
			// log.debug("Page Is loaded."+ispageloaded);

		}

		else
		{

			// This loop will rotate for 25 times to check If page Is ready
			// after every 1 second.
			// You can replace your value with 25 If you wants to Increase or
			// decrease wait time.
			for (int i = 0; i < 25; i++)
			{
				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e)
				{
				}
				// To check page ready state.
				if (js.executeScript("return document.readyState").toString().equals("complete"))
				{
					ispageloaded = "true";
					break;
				}
			}
		}
		// log.debug("ispageloaded flag: "+ispageloaded);
		if (ispageloaded.equalsIgnoreCase("false"))
		{
			IOUtilities.writetextdata(GENERIC_TESTSOL_XPATH_CONSTANTS.CHECKPATH, "N", false);
		}
		return ispageloaded;
	}

	public static String URLLookUp(String tcid, String tsid, String tdid, String title)
	{
		String title1 = driver.getTitle();
		String flag = "N";
		String clubbedURL = null;
		while (flag.equalsIgnoreCase("N"))
		{
			if (title1.equals(title))
			{
				clubbedURL = URL + title + ".jsp";
				// log.debug("I am in selectwindowByTitle : URLLookUp()");
				flag = "Y";
			}
		}
		return clubbedURL;

	}

	/**
	 * First method used to open IE browser It also initiate the driver ,update
	 * the execution time in result sheet using start and stopWatch
	 */

	/**
	 * @param c
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> openBrowser(HashMap c) throws Exception
	{
		log.debug("###### Entering openBrowser ######");
		Long openBrowserTime;
		openBrowserTime = 0L;
		checkFlag = IOUtilities.readtextdata(GENERIC_TESTSOL_XPATH_CONSTANTS.CHECKPATH);

		if (checkFlag.equalsIgnoreCase("Y"))
		{
			
			try
			{
				if (!stopWatch.isStarted())
				{
					stopWatch.reset();
					stopWatch.start();
				}

				// Initialize the Driver
				System.setProperty(Utilities.getProp(GENERIC_TESTSOL_XPATH_CONSTANTS.DRIVER,
				        GENERIC_TESTSOL_XPATH_CONSTANTS.CONFIGPATH), Utilities.getProp(
				        GENERIC_TESTSOL_XPATH_CONSTANTS.DRIVERPATH, GENERIC_TESTSOL_XPATH_CONSTANTS.CONFIGPATH));
				System.setProperty(Utilities.getProp(GENERIC_TESTSOL_XPATH_CONSTANTS.LOGLEVELATT,
				        GENERIC_TESTSOL_XPATH_CONSTANTS.CONFIGPATH), Utilities.getProp(
				        GENERIC_TESTSOL_XPATH_CONSTANTS.LOGLEVEL, GENERIC_TESTSOL_XPATH_CONSTANTS.CONFIGPATH));
				System.setProperty(Utilities.getProp(GENERIC_TESTSOL_XPATH_CONSTANTS.LOGFILEATT,
				        GENERIC_TESTSOL_XPATH_CONSTANTS.CONFIGPATH), Utilities.getProp(
				        GENERIC_TESTSOL_XPATH_CONSTANTS.LOGFILE, GENERIC_TESTSOL_XPATH_CONSTANTS.CONFIGPATH));
				// Set the DesiredCapabilities for IE browser
				Map<String, String> capabilities = new HashMap<String, String>();
				capabilities.put(GENERIC_TESTSOL_XPATH_CONSTANTS.pageLoadStrategy_key,
				        GENERIC_TESTSOL_XPATH_CONSTANTS.pageLoadStrategy_val);
				driver = new InternetExplorerDriver(new DesiredCapabilities(capabilities));
				if (driver == null)
				{
					throw new Exception("Driver could not be initiated");
				}
				// Initialize the Util for the driver
				util = new Utilities(driver);
				stopWatch.stop();
				openBrowserTime = stopWatch.getTime() / 1000;
				stopWatch.reset();

				// Putting success message in the HashMap for the Excel
				c.put("Error Message", "Open Browser Successful");
				c.put("result", "true");
				c.put("executionTime", openBrowserTime);
				c.put(GENERIC_TESTSOL_XPATH_CONSTANTS.Driver_KEY, driver);
				log.debug("Driver in open browser: " + driver);
			}
			catch (Exception ex)
			{
				c.put("Error Message", "openBrowser Method Unsuccessfull||" + ex.getMessage());
				c.put("result", "false");
				c.put("executionTime", openBrowserTime);
				throw ex;
			}
		}
		else
		{
			
			c.put("Error Message", "Previous Test Failed");
			c.put("result", "skipped");
			c.put("executionTime", openBrowserTime);

		}

		log.debug("###### Exiting openBrowser ######");
		return c;

	}

	/**
	 * navigate method used to navigate to PSH URL
	 * 
	 * @throws Exception
	 */
	/**
	 * @param c
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> navigate(HashMap c) throws Exception
	{
		log.debug("###### Entering Navigate ######");
		Long navigateTime;
		navigateTime = 0L;
		checkFlag = IOUtilities.readtextdata(GENERIC_TESTSOL_XPATH_CONSTANTS.CHECKPATH);

		if (checkFlag.equalsIgnoreCase("Y"))
		{
			try
			{
				stopWatch.reset();
				stopWatch.start();
				String URL = Utilities.getProp("URL", GENERIC_TESTSOL_XPATH_CONSTANTS.CONFIGPATH);
				// driver.manage().timeouts().implicitlyWait(10,
				// TimeUnit.SECONDS);
				// driver.manage().deleteAllCookies();
				// driver.manage().window().maximize();
				driver.navigate().to(URL);
				stopWatch.stop();
				navigateTime = stopWatch.getTime() / 1000;
				stopWatch.reset();

				if (driver.findElement(By.id(GENERIC_TESTSOL_XPATH_CONSTANTS.UserName_label)).getText()
				        .contains("User Name"))
				{
					c.put("Error Message", "Navigate Successful");
					c.put("result", "true");
					c.put("executionTime", navigateTime);
				}

			}

			catch (Exception e)
			{
				c.put("Error Message", "Navigate Unsuccessful");
				c.put("result", "false");
				c.put("executionTime", navigateTime);
				IOUtilities.writetextdata(GENERIC_TESTSOL_XPATH_CONSTANTS.CHECKPATH, "N", false);
				throw e;
			}
		}

		else
		{
			c.put("Error Message", "Previous Test Failed");
			c.put("result", "skipped");
			c.put("executionTime", navigateTime);

		}
		log.debug("###### Exiting Navigate ######");
		return c;
	}

	// //////////////////////////////////JPMC Login///////////////////////////

	/**
	 * login method used to Enter Checker or Maker Credential in PSH
	 */
	/**
	 * @param tcid
	 * @param tsid
	 * @param tdid
	 * @param sUser
	 * @param c
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> login(String tcid, String tsid, String tdid, String sUser, HashMap c)
	        throws Exception
	{
		log.debug("###### Entering Login ######");
		Long loginTime;
		loginTime = 0L;
		checkFlag = IOUtilities.readtextdata(GENERIC_TESTSOL_XPATH_CONSTANTS.CHECKPATH);
		if (checkFlag.equalsIgnoreCase("Y"))
		{

			try
			{
				stopWatch.start();
				driver.getWindowHandle();
				driver.findElement(By.xpath(Utilities.getProp("USERNAME", GENERIC_TESTSOL_XPATH_CONSTANTS.ORPATH)))
				        .clear();
				driver.findElement(By.xpath(Utilities.getProp("USERNAME", GENERIC_TESTSOL_XPATH_CONSTANTS.ORPATH)))
				        .sendKeys(Utilities.getProp(sUser, GENERIC_TESTSOL_XPATH_CONSTANTS.CONFIGPATH));
				new Select(driver.findElement(By.xpath(Utilities.getProp("APPLICATION",
				        GENERIC_TESTSOL_XPATH_CONSTANTS.ORPATH)))).selectByVisibleText(Utilities.getProp("APPLICATION",
				        GENERIC_TESTSOL_XPATH_CONSTANTS.CONFIGPATH));

				driver.findElement(By.xpath(Utilities.getProp("LOGINBUTTON", GENERIC_TESTSOL_XPATH_CONSTANTS.ORPATH)))
				        .click();
				log.debug("in login title = " + driver.getTitle());
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				// if
				// ("http://10.10.7.50:20014/servlet/DisplayControllerServlet?".equals(driver.getURL))
				Set<String> win = driver.getWindowHandles();
				log.debug("size  " + win.size());
				ArrayList<String> arr = new ArrayList<String>();

				for (String wind : win)
				{
					//log.debug("wind: " + wind);

					arr.add(wind);

				}

				driver.switchTo().window(arr.get(1));
				log.debug("In login: " + driver.getTitle() + "  |2.  " + driver.getCurrentUrl());

				stopWatch.stop();
				loginTime = stopWatch.getTime() / 1000;
				stopWatch.reset();
				c.put("Error Message", "Login Successful");
				c.put("result", "true");
				c.put("executionTime", loginTime);

			}
			catch (Exception e)
			{
				c.put("Error Message", "Login Page unavailable|| " + e.getMessage());
				c.put("result", "false");
				c.put("executionTime", loginTime);
				e.printStackTrace();
				IOUtilities.writetextdata(GENERIC_TESTSOL_XPATH_CONSTANTS.CHECKPATH, "N", false);

			}
		}
		else
		{
			c.put("Error Message", "Previous Test Failed");
			c.put("result", "skipped");
			c.put("executionTime", loginTime);

		}

		log.debug("###### Exiting Login ######");
		return c;
	}

	/**
	 * clickOnMenu method used to click on any of the Menus
	 */

	public static Map<String, String> clickOnMenu(String testcase, String testStep, String testData,
	        String clientFileId, String clientPaymentId, HashMap c, String sActionType) throws Exception
	{
		log.debug("###### Entering clickOnMenu ######");
		WebElement clickable = null;
		Long clickOnMenuTime;
		clickOnMenuTime = 0L;
		checkFlag = IOUtilities.readtextdata(GENERIC_TESTSOL_XPATH_CONSTANTS.CHECKPATH);
		if (checkFlag.equalsIgnoreCase("Y"))
		{
			stopWatch.reset();
			stopWatch.start();
			try
			{
				Thread.sleep(1000);
				driver.switchTo().defaultContent();

				driver.switchTo().frame("MenuFrame");
				wait = new WebDriverWait(driver, 20);
				clickable = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(testData)));
				clickable.click();
				stopWatch.stop();
				clickOnMenuTime = stopWatch.getTime() / 1000;
				stopWatch.reset();
				c.put("Error Message", "Click on menu Successful");
				c.put("executionTime", clickOnMenuTime);
				c.put("result", "true");
			}
			catch (Exception ex)
			{
				c.put("Error Message", "clickOnMenu |" + ex.getMessage());
				c.put("executionTime", clickOnMenuTime);
				c.put("result", "false");
				throw ex;
			}
		}
		else
		{
			c.put("Error Message", "Previous Test Failed");
			c.put("result", "skipped");
			c.put("executionTime", clickOnMenuTime);

		}
		log.debug("###### Exiting clickOnMenu ######");
		return c;
	}

	/**
	 * advsearch method used to Enter file name in test box ,also enter from and
	 * To data in File Enquiry
	 * 
	 * @throws Exception
	 */

	public static Map<String, String> LC_Registration(String tcid, String tsid, String testData, HashMap c)

	throws Exception
	{
		log.debug("###### Entering LC_Registration ######");

		WebElement elementInput = null;
		Long LC_RegistrationTime;
		LC_RegistrationTime = 0L;
		String Product_type = "Import LC";
		String Operation = "ISSUES";
		checkFlag = IOUtilities.readtextdata(GENERIC_TESTSOL_XPATH_CONSTANTS.CHECKPATH);
		if (checkFlag.equalsIgnoreCase("Y"))
		{
			stopWatch.reset();
			stopWatch.start();
			try
			{

				driver.switchTo().defaultContent();
				driver.switchTo().frame(GENERIC_TESTSOL_XPATH_CONSTANTS.SearchFrame_Val);
				wait = new WebDriverWait(driver, 20);

				elementInput = wait.until(ExpectedConditions.presenceOfElementLocated(By
				        .id(GENERIC_TESTSOL_XPATH_CONSTANTS.Product_type)));

				elementInput.sendKeys(Product_type);
				elementInput = null;

				WebElement dropdwn = driver.findElement(By.name("cmbPkField3"));
				Select sel = new Select(dropdwn);
				sel.selectByValue(Operation);

				driver.findElement(
				        By.xpath(Utilities.getProp("CREATEITEMBUTTON", GENERIC_TESTSOL_XPATH_CONSTANTS.ORPATH)))
				        .click();
				log.debug(driver.getTitle());

				stopWatch.stop();
				LC_RegistrationTime = stopWatch.getTime() / 1000;
				stopWatch.reset();

				c.put("Error Message", "Click on Advance Search Successful");
				c.put("executionTime", LC_RegistrationTime);
				c.put("result", "true");
			}
			catch (Exception ex)
			{
				c.put("Error Message", "LC_Registration |" + ex.getMessage());
				c.put("result", "false");
				c.put("executionTime", LC_RegistrationTime);
				throw ex;
			}
		}
		else
		{
			c.put("Error Message", "Previous Test Failed");
			c.put("result", "skipped");
			c.put("executionTime", LC_RegistrationTime);

		}
		log.debug("###### Exiting LC_Registration ######");
		return c;

	}

	public static String Registration(ArrayList<String> keyInfotab, String tsid, String testData, HashMap c)

	throws Exception
	{
		log.debug("###### Entering Registration ######");

		WebElement elementInput = null;
		Long RegistrationTime;
		RegistrationTime = 0L;
		String Initiator_CIF_No = keyInfotab.get(1);
		String LC_Amount = keyInfotab.get(5);
		String LC_Amount1 = keyInfotab.get(6);
		String Additional_Amount = keyInfotab.get(7);
		String Expiry_Date = keyInfotab.get(17);
		String Expiry_Place = keyInfotab.get(18);
		String dropProdSubType = keyInfotab.get(20);
		String RefId = null;
		checkFlag = IOUtilities.readtextdata(GENERIC_TESTSOL_XPATH_CONSTANTS.CHECKPATH);
		if (checkFlag.equalsIgnoreCase("Y"))
		{
			stopWatch.reset();
			stopWatch.start();
			try
			{

				driver.switchTo().defaultContent();
				driver.switchTo().frame(GENERIC_TESTSOL_XPATH_CONSTANTS.SearchFrame_Val);
				Utilities.gotoFrameByName(GENERIC_TESTSOL_XPATH_CONSTANTS.Register_Val);
				wait = new WebDriverWait(driver, 20);

				elementInput = wait.until(ExpectedConditions.presenceOfElementLocated(By
				        .xpath(GENERIC_TESTSOL_XPATH_CONSTANTS.Initiator_CIF_No)));
				elementInput.clear();
				elementInput.sendKeys(Initiator_CIF_No);
				elementInput = null;

				elementInput = wait.until(ExpectedConditions.presenceOfElementLocated(By
				        .name(GENERIC_TESTSOL_XPATH_CONSTANTS.LC_Amount)));
				elementInput.clear();
				elementInput.sendKeys(LC_Amount);
				elementInput = null;
				Thread.sleep(1000);
				elementInput = wait.until(ExpectedConditions.presenceOfElementLocated(By
				        .name(GENERIC_TESTSOL_XPATH_CONSTANTS.LC_Amount1)));
				elementInput.clear();
				elementInput.sendKeys(LC_Amount1);
				elementInput = null;

				elementInput = wait.until(ExpectedConditions.presenceOfElementLocated(By
				        .name(GENERIC_TESTSOL_XPATH_CONSTANTS.Additional_Amount)));
				elementInput.clear();
				elementInput.sendKeys(Additional_Amount);
				elementInput = null;

				elementInput = wait.until(ExpectedConditions.presenceOfElementLocated(By
				        .name(GENERIC_TESTSOL_XPATH_CONSTANTS.Expiry_Date)));
				elementInput.clear();
				elementInput.sendKeys("10.06.2019");// Expiry_Date
				elementInput = null;

				elementInput = wait.until(ExpectedConditions.presenceOfElementLocated(By
				        .name(GENERIC_TESTSOL_XPATH_CONSTANTS.Expiry_Place)));
				elementInput.clear();
				elementInput.sendKeys(Expiry_Place);
				elementInput = null;

				WebElement dropdwn = driver.findElement(By.name("dropProdSubType"));
				Select sel = new Select(dropdwn);
				sel.selectByVisibleText(dropProdSubType);

				// Click on Create Item Button
				Utilities.gotoFrameByName(GENERIC_TESTSOL_XPATH_CONSTANTS.Reg_Buttons);

				driver.findElement(By.xpath(Utilities.getProp("SUBMITBUTTON", GENERIC_TESTSOL_XPATH_CONSTANTS.ORPATH)))
				        .click();
				Thread.sleep(4000);
				log.debug(driver.getTitle() + "......url1...." + driver.getCurrentUrl());
				Set<String> wind = driver.getWindowHandles();
				log.debug("size   " + wind.size());
				ArrayList<String> arr = new ArrayList<String>();
				for (String win : wind)
				{
					arr.add(win);
		//			log.debug("added>>>>>>" + win);

				}

				driver.switchTo().window(arr.get(1));
				driver.switchTo().defaultContent();
				driver.switchTo().frame("newIndex");
				String focus = driver.findElement(By.xpath("/html/body/pre")).getText();
				log.debug(focus);
				String ReffId = driver.findElement(By.xpath("/html/body/b/pre")).getText();
				RefId = ReffId.trim();
				log.debug(RefId);
				driver.switchTo().defaultContent();
				driver.switchTo().frame("BUTTONFRAME");
				driver.findElement(By.xpath("//span[@class='Close']")).click();
				Set<String> winds = driver.getWindowHandles();
				log.debug("size   " + winds.size());
				for (String win : winds)
				{
					driver.switchTo().window(win);
				}
				driver.navigate().refresh();
				// driver.close();
				stopWatch.stop();
				RegistrationTime = stopWatch.getTime() / 1000;
				stopWatch.reset();

				/*
				 * Subtest(); iText = InputBox("rid") iRow =
				 * InputBox("Which row?")
				 * 
				 * Cells(iRow, 2).Value = iText End Sub
				 */
				c.put("Error Message", "Click on Advance Search Successful");
				c.put("executionTime", RegistrationTime);
				c.put("result", "true");
			}
			catch (Exception ex)
			{
				c.put("Error Message", "Registration |" + ex.getMessage());
				c.put("result", "false");
				c.put("executionTime", RegistrationTime);
				throw ex;
			}
		}
		else
		{
			c.put("Error Message", "Previous Test Failed");
			c.put("result", "skipped");
			c.put("executionTime", RegistrationTime);

		}
		log.debug("###### Exiting Registration ######");
		return RefId;

	}

	public static void clickLinks(String tcid, String tsid, String testData, HashMap c) throws Exception
	{

		Nfocusonwindow("TRADE");
		driver.switchTo().defaultContent();
		driver.switchTo().frame("newIndex");
		JavascriptExecutor je = (JavascriptExecutor) driver;
		WebElement Asset_Liability = driver.findElement(By.xpath("//*[text()='Asset & Liability']"));
		je.executeScript("arguments[0].scrollIntoView(true);", Asset_Liability);
		Asset_Liability.click();
		log.debug("clicked on Asset_Liability");
		Thread.sleep(2000);
		Nfocusonwindow("Asset Liability Screen");
		driver.findElement(By.xpath("//span[@class='ALSave']")).click();
		log.debug("clicked on save");
		Thread.sleep(2000);
		Set<String> win = driver.getWindowHandles();
		log.debug("size  " + win.size());

		for (String wind : win)
		{
			driver.switchTo().window(wind);
			if ("TRADE".equalsIgnoreCase(driver.getTitle()))
				break;
		}

		driver.switchTo().defaultContent();
		driver.switchTo().frame("newIndex");
		WebElement Credit_Details = driver.findElement(By.xpath("//*[text()='Credit Line Details']"));
		Credit_Details.click();
		log.debug("clicked on Credit_Details");
		Thread.sleep(2000);
		Set<String> win1 = driver.getWindowHandles();
		log.debug("Credit_Details win size:  " + win1.size());
		for (String win2 : win1)
		{
			driver.switchTo().window(win2);
			log.debug(driver.getTitle() + "....url3...." + driver.getCurrentUrl());

			List<WebElement> heading = driver.findElements(By.tagName("td"));
			for (WebElement head : heading)
			{

				if ("Credit Line System".equalsIgnoreCase(head.getText()))
				{
					log.debug("we r on right wind-->" + head.getText());
					break;
				}
			}

		}

		Thread.sleep(2000);
		WebElement elementInput = driver.findElement(By.xpath("//span[@class='OK']"));
		elementInput.click();

		log.debug("clicked on OK");
		Thread.sleep(2000);
		Set<String> win2 = driver.getWindowHandles();
		log.debug("size  " + win2.size());

		for (String winds : win2)
		{
			driver.switchTo().window(winds);
			if ("Alert".equalsIgnoreCase(driver.getTitle()))
				break;
		}
		driver.findElement(By.xpath("//a[@class='linkblue']")).click();

		Thread.sleep(2000);
		Set<String> winnn = driver.getWindowHandles();
		log.debug("size  " + winnn.size());

		for (String winds : winnn)
		{
			driver.switchTo().window(winds);
			if ("TRADE".equalsIgnoreCase(driver.getTitle()))
				break;
		}
	}

	/* filterTransactionID method used to filter workitem id */
	public static Map<String, String> filterTransactionID(String sTestCaseID, String sTestStepID, String sTestDataID,
	        String sUser, String sAlertID, String errorDesc, HashMap c, int testSequence, Connection conn)
	        throws Exception

	{
		log.debug("++++++++++Entering filterTransactionID+++++++++++++++++++");
		log.debug("Title>>   " + driver.getTitle() + "    url>>   " + driver.getCurrentUrl());
		log.debug("nitin PSH_Work_Item_Id is : " + sTestDataID);
		// String alertfilepath =
		// "D:\\workspace\\CIBC_Automation_May31\\Resources\\alerts.txt";
		filterTransactionIDtime = 0L;
		stopWatch.reset();
		stopWatch.start();
		try
		{
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.switchTo().defaultContent();

			String pageLoaded = checkPageIsReady();
			log.debug("PageLoaded: " + pageLoaded);
			if (pageLoaded.equalsIgnoreCase("true"))
			{
				driver.switchTo().frame("SearchFrame");
			}

			WebElement searchtable = driver.findElement(By.xpath(Utilities.getProp("WRKTEXTBOX", ORPATH)));
			searchtable.click();
			Thread.sleep(1000);
			WebElement searchmenu = driver.findElement(By.xpath(Utilities.getProp("WRKMENULINK", ORPATH)));
			searchmenu.click();
			Thread.sleep(1000);
			List<WebElement> filtermenu = driver.findElements(By.xpath("//*[@class='x-menu-item x-menu-item-arrow']"));
			log.debug("Arrow: " + filtermenu.size());
			WebElement lastitem = filtermenu.get(filtermenu.size() - 1);

			lastitem.click();

			List<WebElement> filtermenu1 = driver.findElements(By
			        .xpath("//*[@class='x-menu-item x-menu-check-item filteroption-undefined']"));

			WebElement item = filtermenu1.get(0);
			log.debug("filtermenu1: " + item.getText());

			item.click();

			WebElement filtermenu3 = driver.findElement(By.xpath("//*[@id='ext-gen41']"));
			filtermenu3.sendKeys(sTestDataID);

			filtermenu3.sendKeys(Keys.ENTER);

			log.debug("End filtering");
			clickLink(sTestCaseID, sTestStepID, sTestDataID, "", sUser, c);
			log.debug("Title>>   " + driver.getTitle() + "    url>>   " + driver.getCurrentUrl());
			Thread.sleep(2000);
		}
		catch (Exception e)
		{

			c.put("Error Message", "Exception occurred");
			c.put("result", "false");
			throw e;

		}

		log.debug("Picking the result-input");
		c.put("Error Message", "Filter Process completed");
		c.put("result", "true");
		log.debug("Picking the result-exit");
		stopWatch.stop();
		filterTransactionIDtime = stopWatch.getTime();
		c.put("executionTime", filterTransactionIDtime);
		return c;

	}

	public static void selectDD(String id, String value)
	{

		log.debug("+++++++++++++++Entering selectDD+++++++++++++++++");
		WebElement ProdSubtype = driver.findElement(By.xpath("//select[@id='" + id + "']"));

		Select sel = new Select(ProdSubtype);
		sel.selectByVisibleText(value);
		log.debug("+++++++++++++++exit selectDD+++++++++++++++++");
	}

	public static void keyInfotab(ArrayList<String> keyInfotab, String sTestStepID, String sTestDataID, HashMap c)
	        throws Exception
	{
		String Cash_Margin = keyInfotab.get(15);
		String ProdSubType = keyInfotab.get(20);
		String Applicable_Rules = keyInfotab.get(21);
		String Applicable_Rules_for = keyInfotab.get(22);
		String DescriptionAppRules = keyInfotab.get(23);
		String Revolvingchkkbox = keyInfotab.get(24);
		String revcountval = keyInfotab.get(25);
		String ValueBasedRB = keyInfotab.get(26);
		String TimeBasedRB = keyInfotab.get(27);
		String revolvingFq = keyInfotab.get(28);
		String revolvingFqday = keyInfotab.get(29);
		String EvergreenLCckb = keyInfotab.get(30);
		String noofdaysbeforeexp = keyInfotab.get(31);
		String Transferableckb = keyInfotab.get(34);
		String trflvlval = keyInfotab.get(35);
		String splcls = keyInfotab.get(36);
		String advamtval = keyInfotab.get(37);
		String drwamtval = null;

		log.debug("+++++++++++++entering KeyInfo+++++++++++++++++++");

		// Set<String> win = driver.getWindowHandles();
		// log.debug("size  " + win.size());
		// ArrayList<String> arr = new ArrayList();
		//
		// for (String wind : win)
		// {
		// arr.add(wind);
		// }
		//
		// driver.switchTo().window(arr.get(1));
		Nfocusonwindow("TRADE");
		driver.switchTo().defaultContent();
		driver.switchTo().frame("newIndex");

		try
		{
			selectDD("dropProdSub", ProdSubType);
			selectDD("cmbBILCNLCSubjectTo", Applicable_Rules);

			WebElement CashMarg = driver.findElement(By.xpath("//input[@id='txtBILCNCashMargin']"));
			CashMarg.clear();
			CashMarg.sendKeys(Cash_Margin);

			if ("Other".equalsIgnoreCase(Applicable_Rules))
			{
				WebElement DescriptionApp = driver.findElement(By.xpath("//input[@id='txtDescApplRules']"));
				DescriptionApp.sendKeys(DescriptionAppRules);
			}

			// selectDD("cmbBILCNLCApplRulesMT740", Applicable_Rules_for);

			// REVOLVING CHECK BOX //first check box
			Thread.sleep(2000);
			if ("Y".equalsIgnoreCase(Revolvingchkkbox))
			{
				

				WebElement ValueBased = driver.findElement(By.xpath("//input[@id='chkBILCNRevolving']"));
				ValueBased.click();
				
				// VALUE BASED RADIO BUTTON
				if ("Y".equalsIgnoreCase(ValueBasedRB))
				{
					WebElement ValueBasedRBb = driver.findElement(By.xpath("//input[@id='chkBILCNTimeValueBased2']"));
					ValueBasedRBb.click();
					// input revolving count vulue
					WebElement revcount = driver.findElement(By.xpath("//input[@name='txtRevolvingDone']"));
					revcount.sendKeys(revcountval);
					revcount.sendKeys(Keys.TAB);

				}
				// time based radio button
				else if ("Y".equalsIgnoreCase(TimeBasedRB))
				{
					WebElement TimeBasedRBs = driver.findElement(By.xpath("//input[@id='chkBILCNTimeValueBased1']"));
					TimeBasedRBs.click();

					if ("Fixed".equalsIgnoreCase(revolvingFq))
					{
						selectDD("txtRevolvingFrequency", "Fixed");
						WebElement rfdays = driver.findElement(By.xpath("//input[@id='txtBILCNNoOfDays']"));
						rfdays.sendKeys(revolvingFqday);
					}
					else
					{
						try
						{
							WebDriverWait wait = new WebDriverWait(driver, 20);
							wait.until(ExpectedConditions.alertIsPresent());
							log.debug(driver.switchTo().alert().getText());
							driver.switchTo().alert().accept();
							log.debug("alert accepted");
						}
						catch (Exception e)
						{

						}
					}
				}
			}

			// Transferable check box //second check box
			if ("Y".equalsIgnoreCase(Transferableckb))
			{
				
				JavascriptExecutor je = (JavascriptExecutor) driver;
				WebElement Transferable = driver.findElement(By.xpath("//input[@id='chkBILCNTransferable']"));
				je.executeScript("arguments[0].scrollIntoView(true);", Transferable);

				Transferable.click();
				WebElement trflvl = driver.findElement(By.xpath("//input[@id='txtBITDTransferLevel']"));
				trflvl.sendKeys(trflvlval);// /////////

			}

			// Evergreen LC check box //third check box
			if ("Y".equalsIgnoreCase(EvergreenLCckb))
			{
				log.debug("third check box");
				WebElement EvergreenLC = driver.findElement(By.xpath("//input[@id='chkEvergreenLc']"));
				EvergreenLC.click();
				WebElement nodaybfrexp = driver.findElement(By.xpath("//input[@id='txtNoOfDaysBExpiry']"));
				nodaybfrexp.sendKeys(noofdaysbeforeexp);// ///////////

			}
			if ("Green Clause".equalsIgnoreCase(splcls))
			{
				driver.switchTo().defaultContent();
				driver.switchTo().frame("newIndex");
				WebElement grnrb = driver.findElement(By.xpath("//input[@value='G']"));
				grnrb.click();
				JavascriptExecutor je = (JavascriptExecutor) driver;
				WebElement advamt = driver.findElement(By.xpath("//input[@id='txtDrawingAmount']"));
				je.executeScript("arguments[0].scrollIntoView(true);", advamt);
				advamt.sendKeys(advamtval);// /////////////
			}
			else if ("Red Clause".equalsIgnoreCase(splcls))
			{
				// driver.switchTo().defaultContent();
				// driver.switchTo().frame("newIndex");
				WebElement grnrb = driver.findElement(By.xpath("//input[@value='R']"));
				grnrb.click();
				JavascriptExecutor je = (JavascriptExecutor) driver;
				WebElement advamt = driver.findElement(By.xpath("//input[@id='txtDrawingAmount']"));
				je.executeScript("arguments[0].scrollIntoView(true);", advamt);
				advamt.sendKeys(advamtval);// /////////////
			}
		}
		catch (Exception ex)
		{
			c.put("Error Message", "keyInfotab |" + ex.getMessage());
			c.put("result", "false");

			throw ex;
		}
		log.debug("+++++++++++++exit KeyInfo+++++++++++++++++++");
	}

	public static HashMap<String, String> actionmethod(ArrayList<String> keyInfotab, ArrayList<String> Partiestab,
	        ArrayList<String> TermsandCondition, ArrayList<String> ChargesSettlement, HashMap c)
	{
		log.debug("+++++++++++++entering actionmethod+++++++++++++++++++");
		Connection conn = null;
		try

		{

			// Registration

			clickOnMenu("", "", "//*[text()='Registration']", "", "", c, "");
			clickOnMenu("", "", "//*[text()='LC']", "", "", c, "");
			log.debug("firdst screen url " + driver.getCurrentUrl() + "   ::title::  " + driver.getTitle());
			LC_Registration("", "", "", c);
			// try
			// {
			// resultStatus = LC_Registration("", "", "", c);
			//
			// if (resultStatus.get("result").equalsIgnoreCase("true"))
			// {
			// c.put("Error Message", "Open Successful");
			// c.put("result", "true");
			// }
			// }
			// catch (Exception e)
			// {
			// c.put("Error Message", "Open UnSuccessful");
			// c.put("result", "false");
			// e.printStackTrace();
			// }

			String refid = Registration(keyInfotab, "", "", c);

			// Data Entry

			clickOnMenu("", "", "//*[text()='Processing Queue']", "", "", c, "");
			clickOnMenu("", "", "//*[text()='Data Entry']", "", "", c, "");
			clickOnMenu("", "", "//*[text()='LC']", "", "", c, "");
			clickOnMenu("", "", "//*[text()='Import LC']", "", "", c, "");
			filterTransactionID("", "", refid, "", "", "", c, 123, conn);
			Nfocusonwindow("TRADE");
			keyInfotab(keyInfotab, "", "", c);
			clickSubTab("", "", "Parties", c);
			Partiestab(Partiestab, "", "", c);
			clickSubTab("", "", "Terms & Conditions", c);
			TermsandCondition(TermsandCondition, "", "", c);
			clickSubTab("", "", "Charges & Settlement", c);
			Charges_Settlement(ChargesSettlement, "", "", c);
			clickSubTab("", "", "Correspondence List", c);
			WebElement kib = driver.findElement(By.xpath("//div[@id='tabpane3']/table/tbody/tr/td[1]"));
			log.debug(kib.getText());
			JavascriptExecutor jee = (JavascriptExecutor) driver;
			jee.executeScript("arguments[0].click();", kib);
			clickLinks("", "", "", c);

			nclickButtons("Submit");

			Thread.sleep(20000);
			Set<String> w1 = driver.getWindowHandles();
			log.debug("size  " + w1.size());
			ArrayList<String> arr1 = new ArrayList();

			for (String w2 : w1)
			{
				driver.switchTo().window(w2);
				if ("Alert".equalsIgnoreCase(driver.getTitle()))
					break;
			}
			driver.findElement(By.xpath("//a[@class='linkblue']")).click();

			// focus and refresh

			Thread.sleep(2000);
			Set<String> winds = driver.getWindowHandles();
			log.debug("size-->" + winds.size());
			for (String win : winds)
			{
				driver.switchTo().window(win);
			}
			driver.navigate().refresh();

			// Authorization

			Thread.sleep(2000);
			clickOnMenu("", "", "//*[text()='Processing Queue']", "", "", c, "");
			clickOnMenu("", "", "//*[text()='Authorization']", "", "", c, "");
			clickOnMenu("", "", "//*[text()='LC']", "", "", c, "");
			clickOnMenu("", "", "//*[text()='Import LC']", "", "", c, "");
			filterTransactionID("", "", refid, "", "", "", c, 123, conn);
			Nfocusonwindow("TRADE");
			clickSubTab("", "", "Correspondence List", c);
			nclickButtons("Authorize");

			Thread.sleep(20000);
			Set<String> n2 = driver.getWindowHandles();
			log.debug("size  " + n2.size());

			for (String nds : n2)
			{
				driver.switchTo().window(nds);
				if ("Alert".equalsIgnoreCase(driver.getTitle()))
					break;
			}
			WebElement Clk = driver.findElement(By.xpath("//a[@class='linkblue']"));
			Clk.click();

			
			Set<String> windsas = driver.getWindowHandles();
			log.debug("size after authorize-->" + windsas.size());
			for (String winaa : windsas)
			{
				driver.switchTo().window(winaa);
			}

			driver.navigate().refresh();

			c.put("Error Message", "Open Successful");
			c.put("result", "true");
			// c.put("executionTime", openBrowserTime);

		}
		catch (Exception e)
		{
			c.put("Error Message", "Open UnSuccessful");
			c.put("result", "false");
			// c.put("executionTime", openBrowserTime);
			//e.printStackTrace();
			log.fatal("Exception: ",e);
		}
		log.debug("+++++++++++++exit actionmethod+++++++++++++++++++");
		return c;
	}

	public static void clickSubTab(String tcid, String tsid, String tdid, HashMap c) throws InterruptedException
	{
		log.debug("+++++++++Entering clickSubTab : " + tdid + "++++++++++++++++++++++++");

		driver.switchTo().defaultContent();
		driver.switchTo().frame("newIndex");

		WebElement tabname = driver.findElement(By.xpath("//*[(text()='" + tdid + "')] "));
		JavascriptExecutor je = (JavascriptExecutor) driver;
		je.executeScript("arguments[0].click();", tabname);

		// List<WebElement> tabname =
		// driver.findElements(By.xpath("//div[@id='tabpane3']/table/tbody/tr/td"));
		// for (WebElement tab : tabname)
		// {
		// String txt = tab.getText();
		// if (tdid.equalsIgnoreCase(txt))
		// {
		// JavascriptExecutor je = (JavascriptExecutor) driver;
		// je.executeScript("arguments[0].click();", tab);
		// }
		// }

		log.debug("+++++++++++++exit clickSubTab+++++++++++++++++++");
	}

	public static void Partiestab(ArrayList<String> Partiestab, String sTestStepID, String sTestDataID, HashMap c)
	        throws InterruptedException
	{
		log.debug("+++++++++++++entering Partiestab+++++++++++++++++++");
		String Applicant = Partiestab.get(0);
		String Beneficiary = Partiestab.get(1);
		String AdvisingBank = Partiestab.get(2);
		String AdviseThroughBank = Partiestab.get(3);
		String ConfirmingBank = Partiestab.get(4);
		String presentingbank = Partiestab.get(5);
		String reimbursingbank = Partiestab.get(6);
		String ultimate = Partiestab.get(7);
		try
		{

			if (!"NA".equalsIgnoreCase(Beneficiary))
			{
				driver.findElement(By.id("btnSearch2")).click();
				Thread.sleep(2000);
				getdatas(Beneficiary);
			}
			Thread.sleep(1000);
			if (!"NA".equalsIgnoreCase(AdvisingBank))
			{
				driver.findElement(By.id("btnSearch3")).click();
				Thread.sleep(2000);
				getdata(AdvisingBank);
			}
			Thread.sleep(1000);
			if (!"NA".equalsIgnoreCase(AdviseThroughBank))
			{
				driver.findElement(By.id("btnSearch4")).click();
				Thread.sleep(2000);
				getdata(AdviseThroughBank);
			}
			Thread.sleep(1000);
			if (!"NA".equalsIgnoreCase(ConfirmingBank))
			{
				driver.findElement(By.id("btnSearch5")).click();
				Thread.sleep(2000);
				getdata(ConfirmingBank);
			}
			Thread.sleep(1000);
			if (!"NA".equalsIgnoreCase(presentingbank))
			{
				driver.findElement(By.id("btnSearch6")).click();
				Thread.sleep(2000);
				getdata(presentingbank);
			}
			Thread.sleep(1000);
			if (!"NA".equalsIgnoreCase(reimbursingbank))
			{
				driver.findElement(By.id("btnSearch7")).click();
				Thread.sleep(2000);
				getdata(reimbursingbank);
			}
			Thread.sleep(1000);
			if (!"NA".equalsIgnoreCase(ultimate))
			{
				driver.findElement(By.id("btnSearch8")).click();
				Thread.sleep(2000);
				getdata(ultimate);
			}
		}
		catch (Exception ex)
		{
			c.put("Error Message", "Partiestab |" + ex.getMessage());
			c.put("result", "false");

			throw ex;
		}

		log.debug("+++++++++++++exit Partiestab+++++++++++++++++++");

	}

	public static void getdata(String partyid)
	{
		log.debug("+++++++++++++entering getdata+++++++++++++++++++");
		log.debug(driver.getTitle() + "....url...." + driver.getCurrentUrl());

		Set<String> win = driver.getWindowHandles();
		log.debug("size  " + win.size());
		for (String winds : win)
		{
			driver.switchTo().window(winds);
			log.debug(driver.getTitle() + "....url...." + driver.getCurrentUrl());
			if ("Party Search Window".equalsIgnoreCase(driver.getTitle()))
			{
				driver.findElement(By.xpath("//input[@id='partyId']")).sendKeys(partyid);
				driver.findElement(By.xpath("//span[@class='Submit']")).click();
				driver.findElement(By.xpath("//span[@id='PartySearchResultTrs']/table/tbody/tr[1]/td[1]/a")).click();
			}
		}
		Nfocusonwindow("TRADE");
		driver.switchTo().defaultContent();
		driver.switchTo().frame("newIndex");
		log.debug("+++++++++++++exit getdata+++++++++++++++++++");
	}

	public static void getdatas(String partyid)
	{
		log.debug("+++++++++++++entering getdata+++++++++++++++++++");
		log.debug(driver.getTitle() + "....url...." + driver.getCurrentUrl());

		Set<String> win = driver.getWindowHandles();
		log.debug("size  " + win.size());
		for (String winds : win)
		{
			driver.switchTo().window(winds);
			log.debug(driver.getTitle() + "....url...." + driver.getCurrentUrl());
			if ("Party Search Window".equalsIgnoreCase(driver.getTitle()))
			{
				driver.findElement(By.xpath("//input[@id='partyId']")).sendKeys(partyid);
				driver.findElement(By.xpath("//span[@class='Submit']")).click();
				WebDriverWait wait = new WebDriverWait(driver, 10);
				wait.until(
				        ExpectedConditions.visibilityOfElementLocated(By
				                .xpath("//span[@id='PartySearchResultTrs']/table/tbody/tr[1]/td[1]/a"))).click();
			}
		}
		Nfocusonwindow("TRADE");
		driver.switchTo().defaultContent();
		driver.switchTo().frame("newIndex");
		log.debug("+++++++++++++exit getdata+++++++++++++++++++");
	}

	private static void Nfocusonwindow(String title)
	{

		try
		{

			log.debug("+++++++++++++++Entering Nfocusonwindow+++++++++++++ ");
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			log.debug("current window:-->" + driver.getTitle());
			if (!title.equalsIgnoreCase(driver.getTitle()))
			{
				log.debug("searching for :-->" + title);
				Set<String> window = driver.getWindowHandles();
				log.debug("total no of windows:-->" + window.size());
				Iterator<String> itr = window.iterator();
				while (itr.hasNext())
				{
					log.debug("I am here 1");
					driver.switchTo().window(itr.next());

					log.debug("I am here 2  " + driver.getTitle());
					if (title.equalsIgnoreCase(driver.getTitle()))
					{
						log.debug("I am here 3");
						break;
					}

				}

			}
			else
			{
				log.debug("You are on right window");
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
				e.printStackTrace();
			}
		}
	}

	public static void TermsandCondition(ArrayList<String> TermsandCondition, String sTestStepID, String sTestDataID,
	        HashMap c) throws Exception
	{

		log.debug("+++++++++++++entering Terms And Conditions+++++++++++++++++++");

		String Available_With = TermsandCondition.get(0);// "Issuing Bank";
		String Available_By = TermsandCondition.get(1); // /"BY DEF PAYMENT";
		String Restricted_Bank_Name = TermsandCondition.get(2); // "Intellect Office Goregaon";
		String Confirm_Instructions = TermsandCondition.get(3); // "MAY ADD";
		String Reimb_Exp_On_Account = TermsandCondition.get(4);// "CLM";
		String Period_for_Present = TermsandCondition.get(5);// "2 Years";
		String SelectMethdIssue = TermsandCondition.get(6);// "Telecommunication/SWIFT";
		String BankChgPayBy = TermsandCondition.get(7);// "Applicant Pays all charges";
		String ChargeDetails = TermsandCondition.get(9);// null;
		String ConfrmChargePayBy = TermsandCondition.get(10);// "Applicant pays confirmation charges";
		String Debit_Account = TermsandCondition.get(11);// null;
		String Charges_Debit_Account = TermsandCondition.get(12);// null;
		String Drafts_Drawn = TermsandCondition.get(13); // "BEN";
		String Drafts_At = TermsandCondition.get(14);// "MUMBAI";
		String PartialShipment = TermsandCondition.get(15);// "Yes";
		String Place_of_Receipt = TermsandCondition.get(16);// "Mumbai";
		String ShipToDetails = TermsandCondition.get(17); // "Pune";
		String ShipViaDetails = TermsandCondition.get(18); // "Nasik";
		String Incoter = TermsandCondition.get(19); // "DDP";
		String Incoter1 = "Y";
		String DocRcvd = TermsandCondition.get(20);// "N";
		String TransShipment = TermsandCondition.get(21);// "No";
		String LatestShipment = "30.05.2019";// TermsandCondition.get(22);
		String Airport_of_Destination = TermsandCondition.get(24);// "Satara";
		String Commodity = TermsandCondition.get(23);// "OK";
		String Shipment = TermsandCondition.get(26);// "OK";
		String Goods = TermsandCondition.get(27);// "OK";
		String Documents = TermsandCondition.get(28);// "OK";
		String Conditions = TermsandCondition.get(29);// "OK";
		String Additional = TermsandCondition.get(30);// "OK";
		String ReimBank = TermsandCondition.get(31);// "OK";
		String NegotiatingBank = TermsandCondition.get(32);// "OK";

		log.debug("+++++++++++++entering Terms And Conditions+++++++++++++++++++");

		Set<String> win = driver.getWindowHandles();
		log.debug("size  " + win.size());
		ArrayList<String> arr = new ArrayList();

		for (String wind : win)
		{
			arr.add(wind);
		}
		Nfocusonwindow("TRADE");
		driver.switchTo().defaultContent();
		driver.switchTo().frame("newIndex");

		try
		{
			/********* Available With *******/
			/***** Restricted to Bank Name andAddress ***********/

			if (Available_With != null && "Other".equalsIgnoreCase(Available_With))
			{
				WebElement available = driver.findElement(By.xpath("//select[@name='cmbTCAvailableWith']"));
				available.sendKeys(Available_With);
				WebElement Negotiable = driver
				        .findElement(By.xpath("//textarea[@name='txtTCRestrictedNegotiableDet']"));
				Negotiable.sendKeys(Restricted_Bank_Name);

			}
			if (Available_With != null && !"Other".equalsIgnoreCase(Available_With))
			{
				WebElement available = driver.findElement(By.xpath("//select[@name='cmbTCAvailableWith']"));
				available.sendKeys(Available_With);
			}

			/********** Available By ************/

			WebElement cmbTCAvailab = driver.findElement(By.xpath("//select[@name='cmbTCAvailableBy']"));
			cmbTCAvailab.sendKeys(Available_By);

			/******** Confirm Instructions **********************/

			WebElement Instructions = driver.findElement(By.xpath("//select[@name='cmbTCConfirmInstructions']"));
			Instructions.sendKeys(Confirm_Instructions);

			/************** Reimb. Exp Of Account Of **************/

			WebElement cmbTCBear = driver.findElement(By.xpath("//select[@name='cmbTCBearReimExpense']"));
			cmbTCBear.sendKeys(Reimb_Exp_On_Account);

			/****************************** Period for Presentations *******************/

			WebElement txtTCNoof = driver.findElement(By.xpath("//textarea[@name='txtTCNoofPresentation']"));
			txtTCNoof.sendKeys(Period_for_Present);

			/************* Method of Issue ***********************/

			WebElement cmbSelect = driver.findElement(By.xpath("//select[@name='cmbSelectMethdIssue']"));
			cmbSelect.sendKeys(SelectMethdIssue);

			/************* Bank Charges Payable By ***********************/

			/*
			 * WebElement BankChg =
			 * driver.findElement(By.xpath("//select[@name='cmbSelectBankChgPayBy']"
			 * )); BankChg.sendKeys(BankChgPayBy); BankChg.sendKeys(Keys.TAB);
			 */

			/************************* Charges Information *****************/

			WebElement ChargeDet = driver.findElement(By.xpath("//textarea[@name='txtChargeDetails']"));
			ChargeDet.sendKeys(ChargeDetails);

			/************** Applicant pays confirmation charges *******************/

			WebElement txtConfrm = driver.findElement(By.xpath("//select[@name='txtConfrmChargePayBy']"));
			txtConfrm.sendKeys(ConfrmChargePayBy);

			/******************** Debit Account Number **********************/

			/*
			 * WebElement DebitAcctNum =
			 * driver.findElement(By.xpath("//select[@name='txtDebitAcctNum']"
			 * )); DebitAcctNum.sendKeys(Debit_Account);
			 */

			/******************** Charges Debit Account Number *******************************/

			/*
			 * WebElement ChgDebitAcct =
			 * driver.findElement(By.xpath("//select[@name='txtChgDebitAcctNum']"
			 * )); ChgDebitAcct.sendKeys(Charges_Debit_Account);
			 */

			/************************* Drafts Drawn On *************************/

			WebElement DraftsDrawnBox = driver.findElement(By.xpath("//textarea[@name='txtDraftsDrawnBox']"));
			DraftsDrawnBox.sendKeys(Drafts_Drawn);

			/****************** Drafts At ************************/

			WebElement txaTCSPP = driver.findElement(By.xpath("//textarea[@name='txaTCSPPaymentSchedule']"));
			txaTCSPP.sendKeys(Drafts_At);

			/************* Partial Shipment ************/

			WebElement cmbTCSDPartia = driver.findElement(By.xpath("//select[@name='cmbTCSDPartialShipment']"));
			cmbTCSDPartia.sendKeys(PartialShipment);

			/**************** Place of Taking in Charge/Place of Receipt ****************/

			WebElement ShipFromD = driver.findElement(By.xpath("//input[@name='txtTCSDShipFromDetails']"));
			ShipFromD.sendKeys(Place_of_Receipt);

			/************ Place of Final Destination/Transportation/Delivery *****************/

			WebElement CSDShipTo = driver.findElement(By.xpath("//input[@name='txtTCSDShipToDetails']"));
			CSDShipTo.sendKeys(ShipToDetails);

			/*********************** Port Of Loading/Airport of Departure *************/

			WebElement ipViaDetai = driver.findElement(By.xpath("//input[@name='txtTCSDShipViaDetails']"));
			ipViaDetai.sendKeys(ShipViaDetails);

			/**************
			 * Incoterms ************** Insurance Document Received By Applicant
			 **************/

			if (Incoter != null && ("DDA".equalsIgnoreCase(Incoter) || "DAP".equalsIgnoreCase(Incoter)))
			{
				WebElement selecti = driver.findElement(By.xpath("//select[@name='selectinco']"));
				selecti.sendKeys(Incoter);
			}
			if (Incoter != null && !("DDA".equalsIgnoreCase(Incoter) || "DAP".equalsIgnoreCase(Incoter)))
			{
				WebElement selecti = driver.findElement(By.xpath("//select[@name='selectinco']"));
				selecti.sendKeys(Incoter);

				if ("Y".equalsIgnoreCase(Incoter1))
				{
					WebElement chkInc = driver.findElement(By.xpath("//input[@name='chkIncDocRcvd']"));
					chkInc.click();
				}
			}

			/*************** Trans Shipment ************************/

			WebElement cmbTCSDTransS = driver.findElement(By.xpath("//select[@name='cmbTCSDTransShipment']"));
			cmbTCSDTransS.sendKeys(TransShipment);

			/*************************** Latest Shipment Date ***********************/

			WebElement CSDLatestShi = driver.findElement(By.xpath("//input[@name='txtTCSDLatestShipmentDate']"));
			CSDLatestShi.sendKeys(LatestShipment);

			/****************** Commodity **********************/

			WebElement Commod = driver.findElement(By.xpath("//input[@name='txtCommodity']"));
			Commod.sendKeys(Commodity);

			/***************** Commodity **********************/

			/*********************** Port of Discharge/Airport of Destination ******************/

			WebElement tVoyageD = driver.findElement(By.xpath("//input[@name='txtVoyageDetails']"));
			tVoyageD.sendKeys(Airport_of_Destination);
			// ////////////////////////////LINKS//////////////////////

			// -----------------------------Shipment
			// Period--------------------------------
			if ("NA".equalsIgnoreCase(LatestShipment))
			{
				WebElement Shipment_Period = driver.findElement(By.xpath("//a[text()='Shipment Period']"));
				Shipment_Period.click();
				Thread.sleep(3000);

				Set<String> win8 = driver.getWindowHandles();
				log.debug("size  " + win8.size());
				ArrayList<String> arr8 = new ArrayList();

				for (String wind : win8)
				{
					log.debug("loop");
					driver.switchTo().window(wind);
					log.debug("title==" + driver.getTitle());
					if ("Shipment Period".equalsIgnoreCase(driver.getTitle()))
					{
						log.debug("i am on " + driver.getTitle());
						break;
					}
				}
				WebElement Shipmenttxtara = driver.findElement(By.xpath("//textarea[@name='txaCondition']"));
				Shipmenttxtara.sendKeys(Shipment);
				driver.findElement(By.xpath("//span[@class='OK']")).click();
				log.debug("clicked on ok");

				Set<String> win9 = driver.getWindowHandles();
				log.debug("size  " + win9.size());
				ArrayList<String> arr9 = new ArrayList();
				for (String wind : win9)
				{
					log.debug("loop");
					driver.switchTo().window(wind);
					log.debug("title==" + driver.getTitle());
					if ("TRADE".equalsIgnoreCase(driver.getTitle()))
					{
						log.debug("i am on " + driver.getTitle());
						break;
					}
				}

				driver.switchTo().defaultContent();
				driver.switchTo().frame("newIndex");
			}
			// -------------------Description of Goods and/or
			// Services-----------------------------
			WebElement DescriptionofGoods = driver.findElement(By
			        .xpath("//a[text()='Description of Goods and/or Services']"));
			DescriptionofGoods.click();
			Thread.sleep(3000);

			Set<String> win10 = driver.getWindowHandles();
			log.debug("size  " + win10.size());
			ArrayList<String> arr10 = new ArrayList();

			for (String wind : win10)
			{
				log.debug("loop");
				driver.switchTo().window(wind);
				log.debug("title==" + driver.getTitle());
				if ("Goods Description".equalsIgnoreCase(driver.getTitle()))
				{
					log.debug("i am on " + driver.getTitle());
					break;
				}
			}
			WebElement goodstxtarea = driver.findElement(By.xpath("//textarea[@name='txaCondition']"));
			goodstxtarea.sendKeys(Goods);
			driver.findElement(By.xpath("//span[@class='OK']")).click();
			log.debug("clicked on ok");

			Set<String> win11 = driver.getWindowHandles();
			log.debug("size  " + win11.size());
			ArrayList<String> arr11 = new ArrayList();
			for (String wind : win11)
			{
				log.debug("loop");
				driver.switchTo().window(wind);
				log.debug("title==" + driver.getTitle());
				if ("TRADE".equalsIgnoreCase(driver.getTitle()))
				{
					log.debug("i am on " + driver.getTitle());
					break;
				}
			}

			driver.switchTo().defaultContent();
			driver.switchTo().frame("newIndex");
			// -------------------Documents
			// Required-------------------------------
			WebElement DocumentsRequired = driver.findElement(By.xpath("//a[text()='Documents Required']"));
			DocumentsRequired.click();
			Thread.sleep(3000);

			Set<String> win12 = driver.getWindowHandles();
			log.debug("size  " + win.size());
			ArrayList<String> arr12 = new ArrayList();

			for (String wind : win12)
			{
				log.debug("loop");
				driver.switchTo().window(wind);
				log.debug("title==" + driver.getTitle() + "  URL==" + driver.getCurrentUrl());
				if ("Documents Required".equalsIgnoreCase(driver.getTitle()))
				{
					log.debug("i am on " + driver.getTitle());
					break;
				}
			}
			WebElement Documentstextarea = driver.findElement(By.xpath("//textarea[@name='txaCondition']"));
			Documentstextarea.sendKeys(Documents);
			driver.findElement(By.xpath("//span[@class='OK']")).click();
			log.debug("clicked on ok");

			Set<String> win13 = driver.getWindowHandles();
			log.debug("size  " + win.size());
			ArrayList<String> arr13 = new ArrayList();
			for (String wind : win13)
			{
				log.debug("loop");
				driver.switchTo().window(wind);
				log.debug("title==" + driver.getTitle());
				if ("TRADE".equalsIgnoreCase(driver.getTitle()))
				{
					log.debug("i am on " + driver.getTitle());
					break;
				}
			}
			driver.switchTo().defaultContent();
			driver.switchTo().frame("newIndex");
			// --------------------------------Additional
			// Conditions--------------------------------------
			Thread.sleep(3000);
			WebElement Additional_Conditions = driver.findElement(By.xpath("//a[text()='Additional Conditions']"));
			Additional_Conditions.click();
			Thread.sleep(3000);

			Set<String> win14 = driver.getWindowHandles();
			log.debug("size  " + win14.size());
			ArrayList<String> arr14 = new ArrayList();

			for (String wind : win14)
			{
				log.debug("loop");
				driver.switchTo().window(wind);
				log.debug("title==" + driver.getTitle());
				if ("Additional Condition".equalsIgnoreCase(driver.getTitle()))
				{
					log.debug("i am on " + driver.getTitle());
					break;
				}
			}
			WebElement Additionaltxtarea = driver.findElement(By.xpath("//textarea[@name='txaCondition']"));
			Additionaltxtarea.sendKeys(Conditions);
			driver.findElement(By.xpath("//span[@class='OK']")).click();
			log.debug("clicked on ok");

			Set<String> win15 = driver.getWindowHandles();
			log.debug("size  " + win15.size());
			ArrayList<String> arr15 = new ArrayList();
			for (String wind : win15)
			{
				log.debug("loop");
				driver.switchTo().window(wind);
				log.debug("title==" + driver.getTitle());
				if ("TRADE".equalsIgnoreCase(driver.getTitle()))
				{
					log.debug("i am on " + driver.getTitle());
					break;
				}
			}

			driver.switchTo().defaultContent();
			driver.switchTo().frame("newIndex");

			// -------------------------Additional
			// Charges----------------------------------

			WebElement Additional_Charges = driver.findElement(By.xpath("//a[text()='Additional Charges']"));
			Additional_Charges.click();
			Thread.sleep(3000);

			Set<String> win16 = driver.getWindowHandles();
			log.debug("size  " + win16.size());
			ArrayList<String> arr16 = new ArrayList();

			for (String wind : win16)
			{
				log.debug("loop");
				driver.switchTo().window(wind);
				log.debug("title==" + driver.getTitle());
				if ("Additional Charges".equalsIgnoreCase(driver.getTitle()))
				{
					log.debug("i am on " + driver.getTitle());
					break;
				}
			}
			WebElement chargestxtarea = driver.findElement(By.xpath("//textarea[@name='txaCondition']"));
			chargestxtarea.sendKeys(Additional);
			driver.findElement(By.xpath("//span[@class='OK']")).click();
			log.debug("clicked on ok");

			Set<String> win17 = driver.getWindowHandles();
			log.debug("size  " + win17.size());
			ArrayList<String> arr17 = new ArrayList();
			for (String wind : win17)
			{
				log.debug("loop");
				driver.switchTo().window(wind);
				log.debug("title==" + driver.getTitle());
				if ("TRADE".equalsIgnoreCase(driver.getTitle()))
				{
					log.debug("i am on " + driver.getTitle());
					break;
				}
			}

			driver.switchTo().defaultContent();
			driver.switchTo().frame("newIndex");

			WebElement Instr_To_Reim_Bank = driver.findElement(By.xpath("//a[text()='Instr To Reim Bank']"));
			Instr_To_Reim_Bank.click();
			Thread.sleep(3000);

			Set<String> win18 = driver.getWindowHandles();
			log.debug("size  " + win18.size());
			ArrayList<String> arr18 = new ArrayList();

			for (String wind : win18)
			{
				log.debug("loop");
				driver.switchTo().window(wind);
				log.debug("title==" + driver.getTitle());
				if ("Instruction To Reim. Bank".equalsIgnoreCase(driver.getTitle()))
				{
					log.debug("i am on " + driver.getTitle());
					break;
				}
			}
			WebElement Itrbtxtarea = driver.findElement(By.xpath("//textarea[@name='txaCondition']"));
			Itrbtxtarea.sendKeys(ReimBank);
			driver.findElement(By.xpath("//span[@class='OK']")).click();
			log.debug("clicked on ok");

			Set<String> win19 = driver.getWindowHandles();
			log.debug("size  " + win19.size());
			ArrayList<String> arr19 = new ArrayList();
			for (String wind : win19)
			{
				log.debug("loop");
				driver.switchTo().window(wind);
				log.debug("title==" + driver.getTitle());
				if ("TRADE".equalsIgnoreCase(driver.getTitle()))
				{
					log.debug("i am on " + driver.getTitle());
					break;
				}
			}

			driver.switchTo().defaultContent();
			driver.switchTo().frame("newIndex");

			// -------------------------------Instruction To Negotiating
			// Bank------------------------

			WebElement Instruction_To_Negotiating_Bank = driver.findElement(By
			        .xpath("//a[text()='Instruction To Negotiating Bank']"));
			Instruction_To_Negotiating_Bank.click();
			Thread.sleep(3000);

			Set<String> win20 = driver.getWindowHandles();
			log.debug("size  " + win20.size());
			ArrayList<String> arr20 = new ArrayList();

			for (String wind : win20)
			{
				log.debug("loop");
				driver.switchTo().window(wind);
				log.debug("title==" + driver.getTitle());
				if ("Instruction To Nego Bank".equalsIgnoreCase(driver.getTitle()))
				{
					log.debug("i am on " + driver.getTitle());
					break;
				}
			}
			WebElement nbTxtarea = driver.findElement(By.xpath("//textarea[@name='txaCondition']"));
			nbTxtarea.sendKeys(NegotiatingBank);
			driver.findElement(By.xpath("//span[@class='OK']")).click();
			log.debug("clicked on ok");

			Set<String> win21 = driver.getWindowHandles();
			log.debug("size  " + win21.size());
			ArrayList<String> arr21 = new ArrayList();
			for (String wind : win21)
			{
				log.debug("loop");
				driver.switchTo().window(wind);
				log.debug("title==" + driver.getTitle());
				if ("TRADE".equalsIgnoreCase(driver.getTitle()))
				{
					log.debug("i am on " + driver.getTitle());
					break;
				}
			}

			driver.switchTo().defaultContent();
			driver.switchTo().frame("newIndex");

			// ////////////////////////////LINKS//////////////////////
		}
		catch (Exception ex)
		{

			c.put("Error Message", "TermsandCondition |" + ex.getMessage());
			c.put("result", "false");

			throw ex;
		}
		log.debug("+++++++++++++exit Terms And Conditions+++++++++++++++++++");

	}

	public static void nclickButtons(String Value) throws InterruptedException
	{
		WebDriverWait wait = new WebDriverWait(driver, 10);
		log.debug("++++entering clickButtons++++");
		log.debug("tsid: " + Value);
		try
		{
			driver.switchTo().defaultContent();
			driver.switchTo().frame("BUTTONFRAME");
			Thread.sleep(1000);

			List<WebElement> butons = driver.findElements(By.xpath("//span[text()='" + Value + "']"));
			Iterator<WebElement> itr = butons.iterator();
			log.debug("list size: " + butons.size());
			while (itr.hasNext())
			{
				WebElement btn = itr.next();
				int loc = btn.getLocation().getX();
				log.debug("Locatin cordi :" + loc);
				if (loc > 0)
				{

					JavascriptExecutor js = (JavascriptExecutor) driver;
					js.executeScript("arguments[0].click()", btn);
					log.debug("clicked on :" + btn.getText());
				}

			}

		}
		catch (Exception e)
		{
			throw e;
		}
		log.debug("++++entering clickButtons++++");
	}

	public static Map<String, String> Charges_Settlement(ArrayList<String> Charges_Settlement, String sTestStepID,
	        String sTestDataID, HashMap c) throws InterruptedException
	{

		String new_txtCharge = null;
		String new_txtCharge1 = null;
		String Overrid = Charges_Settlement.get(0); // "Y";
		String checkbox1 = Charges_Settlement.get(1); // "Y";
		String Overrid1 = Charges_Settlement.get(2); // "Y";
		String checkbox2 = Charges_Settlement.get(3); // "Y";
		String Overrid2 = Charges_Settlement.get(4); // "Y";
		String checkbox3 = Charges_Settlement.get(5); // "Y";
		String Overrid3 = Charges_Settlement.get(6); // "Y";
		String checkbox4 = Charges_Settlement.get(7); // "Y";

		log.debug("+++++++++++++entering Charges And Settlement+++++++++++++++++++");

		Set<String> win = driver.getWindowHandles();
		log.debug("size  " + win.size());
		ArrayList<String> arr = new ArrayList();
		try
		{
			for (String wind : win)
			{
				arr.add(wind);
			}

			Nfocusonwindow("TRADE");
			driver.switchTo().defaultContent();
			driver.switchTo().frame("newIndex");

			/*
			 * driver.switchTo().window(arr.get(1));
			 * driver.switchTo().defaultContent();
			 * driver.switchTo().frame("newIndex");
			 */

			if (Overrid != null && "Y".equalsIgnoreCase(Overrid))
			{
				WebElement new_fntOverr = driver.findElement(By.xpath("//input[@name='new_fntOverride1']"));
				new_fntOverr.click();
				WebElement Negotiable = driver.findElement(By.xpath("//input[@name='new_txtChargeAmt1']"));
				// Negotiable.clear();
				Negotiable.sendKeys(new_txtCharge);
			}

			if (checkbox1 != null && "Y".equalsIgnoreCase(checkbox1))
			{
				WebElement new_check = driver.findElement(By.xpath("//input[@name='new_checkbox1']"));
				new_check.click();
			}
			if (Overrid1 != null && "Y".equalsIgnoreCase(Overrid1))
			{
				WebElement new_fntOve = driver.findElement(By.xpath("//input[@name='new_fntOverride2']"));
				new_fntOve.click();
				WebElement Negotia = driver.findElement(By.xpath("//input[@name='new_txtChargeAmt2']"));
				// Negotia.clear();
				Negotia.sendKeys(new_txtCharge1);
			}
			if (checkbox2 != null && "Y".equalsIgnoreCase(checkbox2))
			{
				WebElement new_che = driver.findElement(By.xpath("//input[@name='new_checkbox2']"));
				new_che.click();
			}

			/*
			 * if (Overrid2 != null && "Y".equalsIgnoreCase(Overrid2)) {
			 * WebElement new_fntOve =
			 * driver.findElement(By.xpath("//input[@name='new_fntOverride3']"
			 * )); new_fntOve.click(); WebElement Negotia =
			 * driver.findElement(By
			 * .xpath("//input[@name='new_txtChargeAmt2']")); //
			 * Negotia.clear(); Negotia.sendKeys(new_txtCharge1); } if
			 * (checkbox3 != null && "Y".equalsIgnoreCase(checkbox3)) {
			 * WebElement new_che =
			 * driver.findElement(By.xpath("//input[@name='new_checkbox3']"));
			 * new_che.click(); }
			 * 
			 * if (Overrid3 != null && "Y".equalsIgnoreCase(Overrid3)) {
			 * WebElement new_fntOve =
			 * driver.findElement(By.xpath("//input[@name='new_fntOverride4']"
			 * )); new_fntOve.click(); WebElement Negotia =
			 * driver.findElement(By
			 * .xpath("//input[@name='new_txtChargeAmt2']")); //
			 * Negotia.clear(); Negotia.sendKeys(new_txtCharge1); } if
			 * (checkbox4 != null && "Y".equalsIgnoreCase(checkbox4)) {
			 * WebElement new_che =
			 * driver.findElement(By.xpath("//input[@name='new_checkbox4']"));
			 * new_che.click(); }
			 */
			log.debug("+++++++++++++exit Charges And Settlement+++++++++++++++++++");
			return c;

		}
		catch (Exception ex)
		{
			c.put("Error Message", "Charges_Settlement |" + ex.getMessage());
			c.put("result", "false");

			throw ex;
		}

	}

}