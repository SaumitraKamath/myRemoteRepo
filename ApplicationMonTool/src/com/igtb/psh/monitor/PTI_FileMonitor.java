package com.igtb.psh.monitor;

import java.io.File;
import java.sql.Connection;

//import com.polaris.payments.common.util.PT_Logger;

public abstract class PTI_FileMonitor
{

    private static PTI_AlertLogger mPtLog;
    static
    {
	mPtLog = PTI_AlertLogger.getInstance(PTI_FileMonitor.class.getName());
    }

    public void fileMonitoring() throws Exception
    {

	// mPtLog.debug("Inside fileMonitoring..");

	Connection lConn = null;

	try
	{
	    int i = 0;
	    for (String path : PTI_MonitorConstants.FILE_PATH)
	    {
		File lFTTdirectory = new File(path); // ("/data/psh/Dev_LandingZone/incoming/payment-files/FTT/incoming/");

		mPtLog.debug("lFTTdirectory " + i + ":: " + lFTTdirectory);

		PTI_FilesMonitor cfm = new PTI_FilesMonitor();
		// Call scheduled monitoring here

		if (lFTTdirectory.exists())
		{

		    File[] listFiles = lFTTdirectory.listFiles();

		    mPtLog.debug("Files in directory..." + listFiles.toString());

		    cfm.mCheckFilePickup(listFiles, path, PTI_MonitorConstants.MINUTES_INTERVAL);

		}
		i++;
	    }
	    /*
	     * lConn = PT_DatabaseUtil.mGetConnection(); PTI_ResponseFileMonitor
	     * rfm = new PTI_ResponseFileMonitor();
	     * 
	     * ArrayList<String> genParams = new ArrayList<String>();
	     * 
	     * SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
	     * String strDate = formatter.format(new Date());
	     * 
	     * genParams.add(strDate); genParams.add(strDate);
	     * 
	     * rfm.mCheckDBState(lConn, genParams,
	     * "SELECT 1,2,3 FROM DUAL where ?=?", "1000");
	     */

	} catch (Exception e)
	{

	    mPtLog.debug("Exception in fileMonitoring..", e);
	    e.printStackTrace();
	} finally
	{
	    if (lConn != null)
		lConn.close();
	}
    }

}