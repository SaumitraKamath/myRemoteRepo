package org.Trade_Automation.driver;

public interface GENERIC_TESTSOL_XPATH_CONSTANTS
{
	public static final String	CONFIGPATH	               = System.getProperty("user.dir")
	                                                               + "\\Config\\config.properties";
	public static final String	ORPATH	                   = System.getProperty("user.dir")
	                                                               + "\\Object_Repository\\OR.properties";
	public static final String	USER_ACTION_SUMMARYPTH	   = System.getProperty("user.dir")
	                                                               + "\\Resources\\USER_ACTION_SUMMARY.txt";
	public static final String	HITDETAILSTATUSVIEWPTH	   = System.getProperty("user.dir")
	                                                               + "\\Resources\\HIT_DETAIL_STATUS_VIEW.txt";
	public static final String	HITSUMMARYSTATUSVIEWPTH	   = System.getProperty("user.dir")
	                                                               + "\\Resources\\HIT_SUMMARY_STATUS_VIEW.txt";
	public static final String	SCRHITDETAILSTATUSVIEWPTH	= System.getProperty("user.dir")
	                                                               + "\\Resources\\SCR_HIT_DETAIL_STATUS_VIEW.txt";
	public static final String	SCRHITSUMMARYSTATUSVIEWPTH	= System.getProperty("user.dir")
	                                                               + "\\Resources\\SCR_HIT_SUMMARY_STATUS_VIEW.txt";
	public static final String	alertIDpth	               = System.getProperty("user.dir")
	                                                               + "\\Resources\\ALERT_ID.txt";
	public static final String	CHECKPATH	               = System.getProperty("user.dir")
	                                                               + "\\Resources\\CHECK_FLAG.txt";
	public static final String	CLOSEPATH	               = System.getProperty("user.dir")
	                                                               + "\\Resources\\CLOSE_FLAG.txt";
	public static final String	DRIVER	                   = "DRIVER";
	public static final String	DRIVERPATH	               = "DRIVERPATH";
	public static final String	LOGLEVELATT	               = "LOGLEVELATT";
	public static final String	LOGLEVEL	               = "LOGLEVEL";
	public static final String	LOGFILEATT	               = "LOGFILEATT";
	public static final String	LOGFILE	                   = "LOGFILE";
	public static final String	pageLoadStrategy_key	   = "pageLoadStrategy";
	public static final String	pageLoadStrategy_val	   = "eager";

	public static final String	clickLinkByXPath_XPath1	   = "//*[contains(text(),'";
	public static final String	clickLinkByXPath_XPath3	   = "')]";

	public static final String	MenuFrame_Val	           = "MenuFrame";
	public static final String	SearchFrame_Val	           = "SearchFrame";
	public static final String	Register_Val	           = "IndexRegister";
	public static final String	ScreenHeaderxpath1	       = "//*[@id='pagehdr']/tbody/tr[";
	public static final String	ScreenHeaderxpath2	       = "]/td[";
	public static final String	ScreenHeaderxpath3	       = "]";
	public static final String	ScreenResultxpath4	       = "//*[@id='result_tbl']/tbody/tr[";
	public static final String	ScreenResultxpath5	       = "]/td[";
	public static final String	ScreenResultxpath6	       = "]";

	// Login page
	public static final String	UserName_label	           = "ArmorTicket";

	// Workspace Picklist
	public static final String	Workspace_window	       = "Workspace Picklist";

	// BPS page
	public static final String	TabFrame_Val	           = "TabFrame";

	// File Inquiry-Search criteria
	public static final String	fileName_ID	               = "S1_F9";
	public static final String	fileToDate_ID	           = "S1_F11";
	public static final String	fileFromDate_ID	           = "S1_F12";
	public static final String	fileSearch_ID	           = "srchButton";

	// Physical File Details
	public static final String	logicalFile_ID	           = "linkV14_R54";

	// file Inquiry Result
	public static final String	tableXPathResult_Xpath	   = "S1_F9";

	public static final String	tableXPathResult	       = "//table[@id='result_tbl']";
	public static final String	tableXpath1	               = "//*[@id='pagehdr']/tbody/tr[1]/td";

	// Logical File Details
	public static final String	logicalFileDetails_winhdl	= "Logical File Details";
	public static final String	tranCount_ID	           = "linkV14_R53";
	public static final String	batchCount_ID	           = "linkV13_R22";

	// JPMC LC Registration

	public static final String	Product_type	           = "cmbPkField2";
	public static final String	Operation	               = "cmbPkField3";
	public static final String	Initiator_CIF_No	       = "//*[@id='basicinfo']/tbody/tr[2]/td[2]/input[@id='txtBICICIFNo']";
	public static final String	LC_Amount	               = "txtBILCDLCAmountCurrency";
	public static final String	LC_Amount1	               = "txtBILCDLCAmount";
	public static final String	Additional_Amount	       = "txtBILCDAdditionalAmounts";
	public static final String	Expiry_Date	               = "txtBILCNExpiryDate";
	public static final String	Expiry_Place	           = "txtBILCNExpiryPlace";
	public static final String	Product_Sub_Type	       = "dropProdSubType";

	// JPMC Processing Queue - Data Entry - LC - Import LC - Parties

	public static final String	Applicant	               = "btnSearch1";
	public static final String	Beneficiary	               = "btnSearch2";
	public static final String	Advising_Bank	           = "btnSearch3";
	public static final String	Reg_Buttons	               = "RegButtons";
	public static final String	navigationSeperator	       = ">";
	//public static String	   sceenshotsPath	           = "D://WinTrust//Selenium//Screenshots//TestData_ScreenShot_";
	// public static final String testingFilePath =
	// "D://WinTrust//Selenium//Latest Test Data Sheets//Latest Test Data Sheets//TestData_Trade_ActualDemo.xls";
	public static final Object	Driver_KEY	               = "Driver";
	public static final String	DB_IP_ADDRESS	           = "DBIPADDRESS";
	public static final String	DB_USER_NAME	           = "DBUSERNAME";
	public static final String	DB_PASSWORD	               = "DBPASSWORD";
	public static final String	RADIO_BUTTON	           = "RadioButton";
	public static final String	CHECK_BOX	               = "CheckBox";
	public static final String	DROP_DOWN	               = "Dropdown";
	public static final String	DIV	                       = "div";
	public static final String	HYPER_LINK	               = "HyperLink";
	public static final String	OPEN_WINDOW	               = "OpenWindow";
	public static final String	CLICK_SUB_TAB	           = "clickSubTab";
	public static final String	ACTION_BUTTON	           = "button";
	public static final String	CUSTOMIZED_OPERATION	   = "CustomizedOperation";
	public static final String TEXTBOX = "TextBox";
	public static final String PASS = "PASS";
	public static final String FAIL = "FAIL";
	public static final String MADNATORY_CONFIG_CELL_COUNT = "MADNATORY_CONFIG_CELL_COUNT";

}
