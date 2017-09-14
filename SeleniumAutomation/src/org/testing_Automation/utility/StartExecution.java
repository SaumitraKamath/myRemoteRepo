package org.testing_Automation.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.Trade_Automation.driver.GENERIC_TESTSOL_XPATH_CONSTANTS;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import atu.testrecorder.ATUTestRecorder;

public class StartExecution
{

	private static WebDriver	driver	= null;
	static ATUTestRecorder	 recorder;
	private static org.apache.log4j.Logger log = Logger.getLogger(StartExecution.class);

	public StartExecution(HashMap<String, Object> mapIni)
	{
		this.driver = (WebDriver) mapIni.get(GENERIC_TESTSOL_XPATH_CONSTANTS.Driver_KEY);
	}

	public StartExecution()
	{
		// TODO Auto-generated constructor stub
	}

	public WebElement getElementByxPath(String sEntityName)
	{
		WebElement webObj = null;

		try
		{
			//log.debug("00000000");
			//sEntityName = ".//*[@id='" + sEntityName + "']";
			log.debug("sEntityName =>" + sEntityName);
			webObj = driver.findElement(By.xpath(sEntityName));
			//log.debug("3333333333");
			log.debug("id"+webObj.getAttribute("id"));

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

	public WebElement getElementByName(String sEntityName)
	{
		WebElement webObj = null;

		try
		{

			log.debug("sEntityName=" + sEntityName);
			webObj = driver.findElement(By.name(sEntityName));
			log.debug("Element found...!!!");

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

	public WebElement getElementByName1(String sEntityName)
	{
		WebElement webObj = null;

		try
		{

			//log.debug("inside the elementbyname1");
			webObj = driver.findElement(By.name(sEntityName));
			//log.debug("Element found...!!!");

		}
		catch (org.openqa.selenium.NoSuchElementException e)
		{
			// TODO: handle exception
			// e.printStackTrace();
			return null;
		}
		catch (org.openqa.selenium.NoSuchWindowException exe)
		{
			return null;
		}
		return webObj;
	}

	public WebElement findElement(String findMethod, String sEntityName) throws Exception
	{
		WebElement webObj = null;
		try
		{
			log.debug("Inside findElement Entityname: "+sEntityName);
			if (findMethod.equalsIgnoreCase("ByName"))
			{
				webObj = getElementByName(sEntityName);
			}
			else if (findMethod.equalsIgnoreCase("Id"))
			{
				webObj = getElementById(sEntityName);
			}
			else
			{
				webObj = getElementByxPath(sEntityName);
			}
			// log.debug(exeObj);

			while (webObj == null)
			{
				Utility.wait(driver, 300);
				webObj = findElement(findMethod, sEntityName);
			}

		}
		catch (org.openqa.selenium.NoSuchFrameException exe)
		{
			return null;
		}
		catch (Exception e)
		{
			// TODO: handle exception
			log.fatal("Exception in findElement");
			throw e;
			//e.printStackTrace();
		}
		return webObj;
	}

	public WebElement findElement1(String findMethod, String sEntityName)
	{

		WebElement webObj = null;
		try
		{
		//	log.debug("Inside findElement");
			if (findMethod.equalsIgnoreCase("ByName"))
			{
				webObj = getElementByName1(sEntityName);
			}
			else if (findMethod.equalsIgnoreCase("Id"))
			{
				webObj = getElementById(sEntityName);
			}
			else
			{
				webObj = getElementByxPath(sEntityName);
			}

			while (webObj == null)
			{
				Utility.wait(driver, 300);
				webObj = findElement(findMethod, sEntityName);
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

	private WebElement getElementById(String sEntityName)
	{
		WebElement webObj = null;

		try
		{

			log.debug("**************getElementById ***************sEntityName " + sEntityName);
			driver.switchTo().defaultContent();
			driver.switchTo().frame("SearchFrame");

			
			//log.debug(driver.getCurrentUrl());

			webObj = driver.findElement(By.id(sEntityName));

		}
		catch (org.openqa.selenium.NoSuchElementException e)
		{
			// TODO: handle exception
			// e.printStackTrace();
			return null;
		}
		catch (org.openqa.selenium.NoSuchWindowException exe)
		{
			return null;
		}
		return webObj;
	}

	public void switchToFrame(String sFrameName)
	{
		log.debug("Moving to frame:" + sFrameName);
		try
		{
			driver.switchTo().defaultContent();
			driver.switchTo().frame("MenuFrame");
		}
		catch (org.openqa.selenium.NoSuchFrameException exe)
		{
			//exe.printStackTrace();
			log.fatal("Exception: ",exe);
			System.exit(0);
		}
		log.debug("DONE Moving to frame:" + sFrameName);
	}

	public void click(WebElement element, int iTime) throws InterruptedException
	{
		element.click();
		Utility.wait(driver, 400);
	}

	public void populateTxt(WebElement element, String sInputValues) throws InterruptedException
	{
		//log.debug("Putting values" + element.getAttribute("id"));
		element.click();
		element.clear();
		element.sendKeys(sInputValues);
		//Added to tab out after entering values 
		element.sendKeys(Keys.TAB);
		// Thread.sleep(iTime);
	}
	
	
	public void selectValue(WebElement element, String sInputValues) throws InterruptedException
	{
		log.debug(" In selectValue sInputValues:::: " + element);
		Select dropdown = new Select(element);

		dropdown.selectByVisibleText(sInputValues);

		/*
		 * List <WebElement>l2 = dropdown.getOptions(); for(WebElement web :l2
		 * ){
		 * 
		 * if(web.getText().equalsIgnoreCase(sInputValues)) {
		 * dropdown.selectByVisibleText(sInputValues); break; }else{
		 * Utility.wait(driver, 300);
		 * 
		 * }
		 * 
		 * }
		 */
		log.debug("Value selected");
	}

	public void getLinks(String sMenuName, int iSleepCount)
	{
		try
		{
			log.debug("sMenuName: " + sMenuName);
			List<WebElement> links = driver.findElements(By.tagName("a"));

			log.debug("Links size: " + links.size());
			for (WebElement myElement : links)
			{
				String link = myElement.getText();
				log.debug("Text: " + link);
				if (link.trim().equalsIgnoreCase(sMenuName))
				{
					log.debug("Menu found ");
					myElement.click();
					break;
				}

			}
			Utility.wait(driver, iSleepCount);
			//log.debug("After wait");
		}
		catch (Exception e)
		{
			log.fatal("Exception: " , e);
		}
	}

	public void getImages(String sMenuName, int isSleepCount)
	{
		try
		{
			log.debug("Name of the Menu is ::" + sMenuName);
			
			List<WebElement> links = driver.findElements(By.tagName("img"));
			log.debug("Size of Image ArrayList is ::" + links.size());
			for (WebElement myElement : links)
			{
				String link = myElement.getText();
				log.debug(link);
				if (link.trim().equalsIgnoreCase(sMenuName))
				{
					log.debug("Menu found ");
					myElement.click();
					break;
				}
				Utility.wait(driver, isSleepCount);
			}
		}
		catch (Exception e)
		{
			log.fatal("Exception: " , e);
		}
	}

	public ArrayList<String> formatRecord(String sKey, String sSplitChar) throws Exception
	{
		ArrayList<String> arrKey = null;
		try
		{
			arrKey = new ArrayList<String>(Arrays.asList((sKey.split(sSplitChar))));
		}
		catch (Exception exe)
		{
			log.fatal("Exception in formatRecord");
			throw exe;
			//exe.printStackTrace();
		}
		return arrKey;
	}

	public void getMenuButton(String TagName, String sAttributeName, String sMenuName, int iSleepCount)
	{
		try
		{
			log.debug("Entring in getMenuButton method");
			List<WebElement> links = driver.findElements(By.tagName(TagName));
			//log.debug("Size of the arrayList is ::" + links.size());

			for (WebElement myElement : links)
			{

				String link = myElement.getAttribute(sAttributeName);

				log.debug("ME =" + link);

				if (link.toUpperCase().contains(sMenuName.toUpperCase()))
				{
					log.debug(sMenuName + " found ");
					myElement.click();
					break;
				}
				else
				{
					log.debug("Not found");
				}
			}
			Utility.wait(driver, iSleepCount);

		}
		catch (Exception e)
		{
			log.fatal("Exception: " , e);
		}
		//log.debug("final out ");
	}

	public void getAccPickList(String TagName, String sAttributeName, String sMenuName, int iSleepCount,
	        String fieldValue)
	{
		try
		{
			//log.debug("Entring in getMenuButton method");
			List<WebElement> links = driver.findElements(By.tagName(TagName));

			ArrayList<WebElement> accList = new ArrayList<WebElement>();
			for (WebElement myElement : links)
			{

				String link = myElement.getAttribute(sAttributeName);

				if (link.toUpperCase().contains(sMenuName.toUpperCase()))
				{
					log.debug(sMenuName + " found ");

					accList.add(myElement);

				}
				else
				{
					log.debug("Not found");
				}
				log.debug("Size of accList is ::" + accList.size());
			}
			if (fieldValue.equalsIgnoreCase("$$InstructionDetails_Cod_bracctsrcacct"))
			{
				accList.get(0).click();
			}
			else if (fieldValue.equalsIgnoreCase("$$InstructionDetails_Cod_braccttgtacct"))
			{
				accList.get(1).click();
			}
			Utility.wait(driver, iSleepCount);
			// Thread.sleep(iSleepCount);
		}
		catch (Exception e)
		{
			log.fatal("Exception: " , e);
		}
		//log.debug("final out ");
	}

	public static void getMenuButtonCust(String TagName, String sAttributeName, int iSleepCount)
	{
		try
		{
			log.debug("Entring in getMenuButtonCust method");
			List<WebElement> links = driver.findElements(By.tagName(TagName));
			log.debug("Printing size of List ::" + links.size());
			WebElement myElement = links.get(0);
			myElement.click();

			Utility.wait(driver, iSleepCount);

		}
		catch (Exception e)
		{
			log.fatal("Exception: " , e);
		}
	//	log.debug("final out ");
	}

	public static void waitForWindow(WebDriver driver, int iWindowCountime) throws InterruptedException
	{
		// wait until number of window handles become 2 or until 6 seconds are
		// completed.
		log.debug("driver.getWindowHandles().size() :: " + driver.getWindowHandles().size());
		log.debug("required size() :: " + iWindowCountime);
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
			//log.debug("timecount =" + timecount);
		}
		while (driver.getWindowHandles().size() != iWindowCountime);

	}

	public static void switchToModalDialog(WebDriver driver, String parent)
	{
		// Switch to Modal dialog
		if (driver.getWindowHandles().size() == 2)
		{
			for (String window : driver.getWindowHandles())
			{
				if (!window.equals(parent))
				{

					driver.switchTo().window(window);
					if(log.isDebugEnabled()){
						log.debug(driver.getTitle());	
						log.debug("Modal dialog found");
					}
					break;
				}
			}
		}
	}

	public WebElement getMenuButtonNew(String TagName, String sAttributeName, String sMenuName, int iSleepCount)
	{
		WebElement myElementObj = null;
		try
		{
			//log.debug("Finding button...");
			List<WebElement> links = driver.findElements(By.tagName(TagName));
			log.debug("Size of List is ::" + links.size());

			for (WebElement myElement : links)
			{
				String link = myElement.getAttribute(sAttributeName);
				//log.debug("Attribute name is ::" + link);
				if (link.toUpperCase().contains(sMenuName.toUpperCase()))
				{
					log.debug(sMenuName + " found ");
					myElementObj = myElement;
					break;
				}
			}

		}
		catch (Exception e)
		{
			log.fatal("Exception: " , e);
		}
		//log.debug("final out ");
		return myElementObj;
	}


	public static void waitForWindow(WebDriver driver) throws InterruptedException
	{
		// wait until number of window handles become 2 or until 6 seconds are
		// completed.
		int timecount = 1;
		do
		{
			driver.getWindowHandles();
			Thread.sleep(200);
			timecount++;
			if (timecount > 30)
			{
				break;
			}
		}
		while (driver.getWindowHandles().size() != 2);

	}


	public String getResponseMessage()
	{
		String strMessage = null;

		List<WebElement> fleetDetails = driver.findElements(By.tagName("td"));

		for (WebElement detail : fleetDetails)
		{
			strMessage = detail.getText();
			//log.debug("Message ::" + strMessage);
			if (detail.getText().contains("successfully"))
			{
				break;
			}

		}
		return strMessage;
	}

	public String getListDetailsTab(String strData)
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

	public static void switchToModalDialog(WebDriver driver, String parent, int iWindowSize)
	{
		// Switch to Modal dialog
		if (driver.getWindowHandles().size() == iWindowSize)
		{
			/*
			 * for (String window : driver.getWindowHandles()) {
			 * log.debug(window); }
			 */

			for (String window : driver.getWindowHandles())
			{

				log.debug(window);

				if (!window.equals(parent))
				{
					driver.switchTo().window(window);
					if(log.isDebugEnabled())
						log.debug(driver.getTitle()+" Modal dialog found");

					//log.debug("Modal dialog found");
					break;
				}
			}
		}
	}

	public static void switchToModalDialog(WebDriver driver, StringBuffer parent, int iWindowSize)
	{
		log.debug("Swiching to new window!!" + parent.toString());
		try
		{

			String currentWindow = driver.getWindowHandle();
			log.debug("currentWindow: " + currentWindow);

			Set<String> availableWindows = driver.getWindowHandles();
			log.debug("availableWindows: " + availableWindows.size());
			if (!availableWindows.isEmpty())
			{
				for (String windowId : availableWindows)
				{
					log.debug("windowId: " + windowId);
					log.debug("windowId getTitle: " + driver.switchTo().window(windowId).getTitle());
					if (driver.switchTo().window(windowId).getTitle().equals(parent.toString()))
					{
						log.debug(driver.getTitle() + " Finallty ");
						break;
					}
					else
					{
						driver.switchTo().window(currentWindow);
						log.debug(driver.getTitle() + "In ELSE ");
					}
				}
			}

		}
		catch (Exception e)
		{
			log.fatal("Exception in switchToModalDialog->",e);
			//e.printStackTrace();
		}

	}

	public static void JustForTest(WebDriver driver, String sKeyName)
	{

		List<WebElement> links = driver.findElements(By.tagName("a"));

		for (WebElement myElement : links)
		{
			String link = myElement.getAttribute("onclick");
			//log.debug("Value == == = = " + link);
			if (link.trim().contains(sKeyName))
			{
				//log.debug("Menu found " + sKeyName);
				myElement.click();
				break;
			}

		}

	}

}
