/***************************************************************************
/* Copyright ©  2016 Intellect Design Arena Ltd. All rights reserved      */
/*                                                                      */
/************************************************************************/
/*  Application  : PSH Monitoring Engine                             */
/*  Module Name  : Common Services               	     	*/
/*                                                                      */
/*  File Name    : PTI_CIBCFilesMonior.java                             */
/*                                                                      */
/*  Description  : Monitors IWS,SCA,FTT channels incoming files
 /*                 	                                                */
/*  Author       : Lakshman Damuluri     */
/************************************************************************/
/* Version Control Block                                              	*/
/* ~~~~~~~ ~~~~~~~ ~~~~~                                              	*/
/*                                                                    	*/
/*    Date        	Version     Author                    Ref.                 Description                 */
/*    ====        	=======     ======                    ====                 ===========             
 /*********************************************************************************

 17-June-2016     1.0   		Lakshman Damuluri                  New File

 /*********************************************************************************/
package com.igtb.psh.monitor;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

//import com.polaris.payments.common.util.PT_Logger;

public class PTI_FilesMonitor extends PTI_FileMonitor
{

//    HashMap<String,String> mResultMap = null;
    private static PTI_AlertLogger mPtLog;
    static
    {
	mPtLog = PTI_AlertLogger.getInstance(PTI_FilesMonitor.class.getName());
    }

    public boolean mCheckFilePickup(File[] listFiles, String lDirectory, int pMinInterval) throws Exception
    {
	Date modifiedDate = null;
	boolean lChkFilePickFlag = true;
//	List<String> lResultList = null;
	PTI_AlertEngineImpl lPtiAlertImpl = new PTI_AlertEngineImpl();
	HashMap<String,String> mResultMap = new HashMap<String,String>();
	try
	{
	    Date currentDate = new Date();
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(currentDate);
	    cal.add(Calendar.MINUTE, -pMinInterval);

	    Date beforeDate = cal.getTime();
//	    Date lRespProcSchedTime = cal.getTime();

	    for (File listFile : listFiles)
	    {
		mResultMap = new HashMap<String,String>();
		if (listFile.isFile())
		{
		    modifiedDate = new Date(listFile.lastModified());

//		    mPtLog.debug("modifiedDate..." + modifiedDate);
//		    mPtLog.debug("alertDate..." + beforeDate);

		    if (modifiedDate != null && modifiedDate.before(beforeDate))
		    {
//			mPtLog.debug("Files Still Exist..." + listFile.getName());
			mResultMap.put(PTI_MonitorConstants.FILE_NAME, listFile.getName());
			mResultMap.put(PTI_MonitorConstants.MONITOR_TYPE, PTI_MonitorConstants.FILE_MONITOR);
			// if (lDirectory.equals(PTI_MonitorConstants.FTT_PATH))
			mResultMap.put(PTI_MonitorConstants.ERROR_CODE, PTI_MonitorConstants.MON_ERR_FILE_EXISTS);
			lChkFilePickFlag = false;
			// Pass error code to the map...
		    }
		}
		if (!mResultMap.isEmpty())
		    lPtiAlertImpl.mWriteLog(mResultMap);
	    }
	} catch (Exception e)
	{
	    mPtLog.fatal("Exception in mCheckFilePickup..", e);
	    e.printStackTrace();
	    // e.printStackTrace();
	}
	return lChkFilePickFlag;

    }
}
