package org.Trade_Automation.functionlib;

import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Properties;

import org.Trade_Automation.driver.Action_Keywords;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Utilities
{
	WebDriver	         driver;
	public static String	configpath	= System.getProperty("user.dir") + "\\Config\\config.properties";

	public Utilities(WebDriver driver)
	{
		this.driver = driver;
	}

	public final static String	dbURL	 = "jdbc:oracle:thin:@10.10.9.44:1521:SIR13954";
	public final static String	userName	= "CIBC_PHASE2_DEV";
	public final static String	password	= "CIBC_AUTO_TEST";

	public static String	   latestwindow;

	/**
	 * This method gets configuration data.
	 * 
	 * @param key
	 * @param propFilePath
	 * @return property values
	 * @throws IOException
	 */
	public static String getProp(String key, String propFilePath) throws IOException
	{
		if (key.contains("PATH"))
		{
			return getProp(key, propFilePath, System.getProperty("user.dir"));

		}
		String propval=null;;
		FileReader reader = null;
		try {
			String propfilename = propFilePath;
			reader=new FileReader(propfilename);
			Properties prop = new Properties();
			prop.load(reader);
			propval = prop.getProperty(key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(reader!=null)
				reader.close();
		}
		// System.out.println("In GetProp: "+string);
		return propval;
	}

	/**
	 * This method is overloaded form of getProp(String key,String propfilepath)
	 * and gets configuration data.
	 * 
	 * @param key
	 * @param propfilepath
	 * @param user_workspace
	 * @return path for given resource as key.
	 * @throws IOException
	 */
	public static String getProp(String key, String propfilepath, String user_workspace) throws IOException
	{

		String propfilename = propfilepath;
		String propval=null;
		FileReader reader = null;
		try {
			reader = new FileReader(propfilename);
			Properties prop = new Properties();
			prop.load(reader);
			propval = prop.getProperty(key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(reader!=null)
				reader.close();
		}
		return user_workspace + propval;
	}

	/**
	 * This method flushes and clear current buffer.
	 * 
	 * @throws IOException
	 */

	public boolean TestExecute(String TCID, int noOfTestConfig) throws IOException, Exception
	{
		boolean executeFlag = false;
		ExcelUtilsN.setExcelFile(getProp("FRAMEWORKFILEPATH", configpath), "TestConfig");
		for (int iRow = 1; iRow <= noOfTestConfig; iRow++)
		{
			if (ExcelUtilsN.getCellData(iRow, 0).equals(TCID))
			{
				executeFlag = "Y".equalsIgnoreCase(ExcelUtilsN.getCellData(iRow, 1));
			}

		}
		return executeFlag;
	}

	public static void gotoFrameByName(String frameName)
	{
		continueSearch = true;
		Action_Keywords.driver.switchTo().defaultContent();
		gotoFrame(frameName);
	}

	private static boolean	continueSearch	= true;

	private static void gotoFrame(String frameName)
	{
		try
		{
			JavascriptExecutor jsExecutor = (JavascriptExecutor) Action_Keywords.driver;
			String currentFrame = (String) jsExecutor.executeScript("return self.name");
			System.out.println("Entering gotoFrame and currentFrame = " + currentFrame);
			if ((null != frameName) && (!"".equals(frameName)))
			{
				if (continueSearch)
				{
					if (!frameName.equals(currentFrame))
					{
						ArrayList<WebElement> frames = (ArrayList<WebElement>) Action_Keywords.driver.findElements(By
						        .tagName("frame"));
						// System.out.println("frame : " + frames.size());
						if (frames.size() == 0)
						{
							Action_Keywords.driver.switchTo().parentFrame();
							return;
						}

						for (int i = 0; i < frames.size() && continueSearch; i++)
						{

							// getAllChildFrames();
							currentFrame = frames.get(i).getAttribute("name");

							if (frameName.equals(currentFrame))
							{
								Action_Keywords.driver.switchTo().frame(currentFrame);
								continueSearch = false;
								return;
							}
							if (continueSearch)
							{
								System.out.println("Moving to :" + currentFrame);
								Action_Keywords.driver.switchTo().frame(currentFrame);
								gotoFrame(frameName);
							}
							else
								break;
							try
							{
								Thread.sleep(200);
							}
							catch (InterruptedException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						currentFrame = frameName;
					}
					else
					{
						Action_Keywords.driver.switchTo().frame(currentFrame);
					}
				}
				return;
			}
			currentFrame = "";
			Action_Keywords.driver.switchTo().defaultContent();
		}
		catch (Exception ex)
		{
			System.out.println("Exception---" + ex);
		}
	}

	/**
	 * Provide frame names in order of hierarchy to reach a frame.
	 * 
	 * @param route
	 */

	public static ArrayList<WebElement> getAllChilds(String tag)
	{
		ArrayList<WebElement> eleList = (ArrayList<WebElement>) Action_Keywords.driver.findElements(By.tagName(tag));
		for (WebElement x : eleList)
		{
			System.out.println("<" + tag + " type = " + x.getAttribute("type") + "class = " + x.getAttribute("class")
			        + " id = " + x.getAttribute("id") + " name = " + x.getAttribute("name") + " title = "
			        + x.getAttribute("title") + ">");
		}
		return eleList;
	}

	public static String removeZeros(String inputValue)
	{
		String output = inputValue;
		long longValue = 0;
		try
		{
			DecimalFormat df2 = new DecimalFormat("##########");
			longValue = Math.round(Double.valueOf(inputValue));
			if (Double.valueOf(inputValue) > Double.valueOf(longValue)
			        || Double.valueOf(inputValue) < Double.valueOf(longValue))
			{
				output = df2.format(inputValue);
			}
			else if (inputValue != null && inputValue.indexOf(".") == -1)
			{
				output = inputValue;
			}
			else
			{
				output = df2.format(longValue);
			}
		}
		catch (Exception e)
		{
		}
		return output;
	}

}
