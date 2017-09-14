package com.igtb.psh.monitor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class PTI_MonitorConstants
{

    static Properties mMonitorConst;
    private static PTI_AlertLogger mPtLog;
    // private static boolean isDebugEnabled;
    public static ArrayList<String> FILE_PATH = new ArrayList<String>();

    public static ArrayList<String> QUEUE_LIST = new ArrayList<String>();

    public static ArrayList<String> RESOURCE_LIST = new ArrayList<String>();

    public static String FILE_NAME;
    public static String MONITOR_TYPE;
    public static String IRM_ERR_001;
    public static String INIT_CONT_FACTORY;
    public static String LOGS_PATH;
    public static String PROVIDER_URL;
    public static String HOSTNAME;
    public static long PORT;
    public static String CONSOLE_UNAME;
    public static String CONSOLE_PASS;
    public static String FILE_MONITOR;
    public static String FILE_MONITOR_NACK;
    public static String FILE_MONITOR_ACK;
    public static String FILE_MONITOR_TECH_NACK;
    public static String FILE_MONITOR_PSR;
    public static String FILE_NAME_NACK;
    public static String FILE_NAME_TNACK;
    public static String FILE_NAME_ACK;
    public static String FILE_NAME_PSR;

    public static String BUSINESS_STATE_MONITOR;
    public static String FILE_NAME_BUSI_MONITOR;

    public static String ERROR_CODE = "ERROR_CODE";

    public static String MON_ERR_FILE_EXISTS = "MON_ERR_FILE_EXISTS";

    public static int MINUTES_INTERVAL ;

    public static String MON_ERR_FTT_FILE_EXISTS = "MON_ERR_FTT_FILE_EXISTS";

    public static String GET_NACK_QRY;
    public static String GET_TECH_NACK_QRY;
    public static String GET_ACK_QRY;
    public static String GET_PSR_QRY;
    public static String GET_ACT_TXN_CNT_QRY;
    public static String MON_ERR_TXNRECON = "MON_ERR_TXNRECON";
    public static String NACK_ERR_FILE_NAME = "NACK_ERR_FILE_NAME";
    public static String MON_ERR_JMS_QUEUE = "MON_ERR_JMS_QUEUE";
    public static String MON_ERR_JDBC_CON_MAX_LIMIT = "MON_ERR_JDBC_CON_MAX_LIMIT";
    public static String MON_ERR_JDBC_CON = "MON_ERR_JDBC_CON"; 
    public static String GET_EOD_JOB_LIST_QRY;
    public static String GET_EOD_JOB_EXEC_QRY;
    public static int GET_EOD_SLA_HR;
    public static int GET_EOD_SLA_MIN;
    public static String MON_ERR_EOD_LIST = "MON_ERR_EOD_LIST";
    public static String MON_ERR_EOD_EXEC = "MON_ERR_EOD_EXEC";
    public static String GET_JMS_QUEUE_QRY;
    public static String GET_JDBC_SRC_QRY;
    public static int SCH_FREQ_IN_SEC;
    public static String RESP_FILE_PATH;
    public static String MON_ERR_RESP_FILE_EXISTS = "MON_ERR_RESP_FILE_EXISTS";
    public static final int MINUTES_INTERVAL_RESP_FILE = 60;
    public static final List<String> SCHEDULED_MONITORING_PROPERTIES = new ArrayList<String>();
    public static String GET_TXN_STATUS_QRY;
    public static String MON_WARN_TXN_STUCK = "MON_WARN_TXN_STUCK";
    public static String MON_ERR_TXN_STUCK = "MON_ERR_TXN_STUCK";
    public static String TXN_STATUS = "TXN_STATUS";
    public static String WORKITEMID = "WORKITEMID";
    static
    {
	mPtLog = PTI_AlertLogger.getInstance(PTI_MonitorConstants.class.getName());
	// isDebugEnabled = mPtLog.isDebugEnabled();

	mMonitorConst = new Properties();
	try
	{
	    mMonitorConst = PTI_MonitoringPropertiesReader.getProperties("PTI_MonitorConstants.properties");

	} catch (Exception e)
	{

	    mPtLog.fatal("MON_ERR_SCH_PROP - Error Fetching the File PTI_MonitorConstants.properties ", e);
	    e.printStackTrace();
	}

	System.out.println("File loaded successfully");

	for (String key : mMonitorConst.stringPropertyNames())
	{
	    if (key.endsWith("_PATH"))
	    {
		// System.out.println("Here"+key);
		FILE_PATH.add(mMonitorConst.getProperty(key));
	    } else if (key.endsWith("_QUEUE"))
	    {
		QUEUE_LIST.add(mMonitorConst.getProperty(key));
	    } else if (key.endsWith("_RESOURCE"))
	    {
		RESOURCE_LIST.add(mMonitorConst.getProperty(key));
	    }
	}

	FILE_NAME = mMonitorConst.getProperty("FILE_NAME");

	MONITOR_TYPE = mMonitorConst.getProperty("MONITOR_TYPE");

	IRM_ERR_001 = mMonitorConst.getProperty("IRM_ERR_001");

	INIT_CONT_FACTORY = mMonitorConst.getProperty("INIT_CONT_FACTORY");
	LOGS_PATH = mMonitorConst.getProperty("LOGS_LOCATION");

	PROVIDER_URL = mMonitorConst.getProperty("PROVIDER_URL");
	// HOSTNAME = mMonitorConst.getProperty("HOSTNAME");
	// PORT = Long.parseLong(mMonitorConst.getProperty("PORT"));
	CONSOLE_UNAME = mMonitorConst.getProperty("CONSOLE_UNAME");
	CONSOLE_PASS = mMonitorConst.getProperty("CONSOLE_PASS");

	FILE_MONITOR = mMonitorConst.getProperty("FILE_MONITOR");

	// FILE_MONITOR_NACK = mMonitorConst.getProperty("FILE_MONITOR_NACK");
	// FILE_MONITOR_ACK = mMonitorConst.getProperty("FILE_MONITOR_ACK");
	// FILE_MONITOR_TECH_NACK =
	// mMonitorConst.getProperty("FILE_MONITOR_TECH_NACK");
	// FILE_MONITOR_PSR = mMonitorConst.getProperty("FILE_MONITOR_PSR");
	// FILE_NAME_NACK = mMonitorConst.getProperty("FILE_NAME_NACK");
	// FILE_NAME_TNACK = mMonitorConst.getProperty("FILE_NAME_TNACK");
	// FILE_NAME_ACK = mMonitorConst.getProperty("FILE_NAME_ACK");
	// FILE_NAME_PSR = mMonitorConst.getProperty("FILE_NAME_PSR");
	// BUSINESS_STATE_MONITOR =
	// mMonitorConst.getProperty("BUSINESS_STATE_MONITOR");
	// FILE_NAME_BUSI_MONITOR =
	// mMonitorConst.getProperty("FILE_NAME_BUSI_MONITOR");
	// GET_TECH_NACK_QRY = mMonitorConst.getProperty("GET_TECH_NACK_QRY");
	// GET_NACK_QRY = mMonitorConst.getProperty("GET_NACK_QRY");
	// GET_ACK_QRY = mMonitorConst.getProperty("GET_ACK_QRY");
	// GET_PSR_QRY = mMonitorConst.getProperty("GET_PSR_QRY");
	// GET_ACT_TXN_CNT_QRY =
	// mMonitorConst.getProperty("GET_ACT_TXN_CNT_QRY");
	// GET_EOD_JOB_LIST_QRY =
	// mMonitorConst.getProperty("GET_EOD_JOB_LIST_QRY");
	// GET_EOD_JOB_EXEC_QRY =
	// mMonitorConst.getProperty("GET_EOD_JOB_EXEC_QRY");
	// GET_EOD_SLA_HR =
	// Integer.parseInt(mMonitorConst.getProperty("GET_EOD_SLA_HR"));
	// GET_EOD_SLA_MIN =
	// Integer.parseInt(mMonitorConst.getProperty("GET_EOD_SLA_MIN"));
	// GET_JMS_QUEUE_QRY = mMonitorConst.getProperty("GET_JMS_QUEUE_QRY");
	// GET_JDBC_SRC_QRY = (mMonitorConst.getProperty("GET_JDBC_SRC_QRY"));
	SCH_FREQ_IN_SEC = Integer.parseInt(mMonitorConst.getProperty("SCH_FREQ_IN_SEC"));
	MINUTES_INTERVAL = Integer.parseInt(mMonitorConst.getProperty("MINUTES_INTERVAL"));
	// RESP_FILE_PATH = mMonitorConst.getProperty("RESP_FILE_PATH");
	// GET_TXN_STATUS_QRY = mMonitorConst.getProperty("GET_TXN_STATUS_QRY");
    }
}
