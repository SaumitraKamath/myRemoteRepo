/***************************************************************************
 /* Copyright ©  2016 Intellect Design Arena Ltd. All rights reserved      */
/*                                                                      */
/************************************************************************/
/*  Application  : PSH Monitoring Engine                             */
/*  Module Name  : Common Services               	     	*/
/*                                                                      */
/*  File Name    : PTI_EODJobsMonitor.java                             */
/*                                                                      */
/*  Description  : Monitors EOD jobs execution
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

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.polaris.payments.common.util.PT_DatabaseUtil;
//import com.polaris.payments.common.util.PT_Logger;
//import com.sun.org.apache.bcel.internal.generic.LALOAD;

public class PTI_EODJobsMonitor
{

    private static PTI_AlertLogger mPtLog;
    static
    {
	mPtLog = PTI_AlertLogger.getInstance(PTI_ResponseFileMonitor.class.getName());
    }

    HashMap mResultMap = new HashMap();

    PTI_AlertEngineImpl lPtiAlertImpl = new PTI_AlertEngineImpl();

    public void mEODMonitoring() throws Exception
    {

	mPtLog.fatal("mEODMonitoring..");

	Connection lConn = null;
	PreparedStatement lPrepStmt = null;
	ResultSet lRst = null;
	Map<BigDecimal, PTI_FileDetBean> lFileDetMap = null;
	String lEODListQry = null;
	String lEODExecQry = null;
	boolean lEODListFlag;
	boolean lEODExecFlag;
	int lHours;
	int lMinutes;
	int lCount = 0;
	try
	{
	    lHours = PTI_MonitorConstants.GET_EOD_SLA_HR;
	    lMinutes = PTI_MonitorConstants.GET_EOD_SLA_MIN;
	    java.util.Calendar cal = Calendar.getInstance();

	    mPtLog.debug("lHours..." + lHours + "  lMinutes::; " + lMinutes + " cal.getTime().getHours()" + cal.getTime().getHours() + " Minutes sys " + cal.getTime().getMinutes());

	    if (cal.getTime().getHours() > lHours && cal.getTime().getMinutes() > lMinutes)
	    {

		mPtLog.debug("EOD Time reached..");

		lConn = PT_DatabaseUtil.mGetConnection();
		lEODListQry = PTI_MonitorConstants.GET_EOD_JOB_LIST_QRY;
		lPrepStmt = lConn.prepareStatement(lEODListQry);
		lRst = lPrepStmt.executeQuery();
		while (lRst.next())
		{
		    lCount = lRst.getInt(1);

		    mPtLog.debug("Count ..." + lCount);

		    if (lCount == 0)
		    {
			lEODListFlag = true;
			mResultMap.put(PTI_MonitorConstants.ERROR_CODE, PTI_MonitorConstants.MON_ERR_EOD_LIST);
		    }
		}
		lPrepStmt = null;
		lRst = null;
		lEODExecQry = PTI_MonitorConstants.GET_EOD_JOB_EXEC_QRY;
		lPrepStmt = lConn.prepareStatement(lEODExecQry);
		lRst = lPrepStmt.executeQuery();
		while (lRst.next())
		{
		    lCount = lRst.getInt(1);

		    mPtLog.debug("Count ..." + lCount);

		    if (lCount == 0)
		    {
			lEODExecFlag = true;
			mResultMap.put(PTI_MonitorConstants.ERROR_CODE, PTI_MonitorConstants.MON_ERR_EOD_EXEC);
		    }
		}
		if (!mResultMap.isEmpty())
		    lPtiAlertImpl.mWriteLog(mResultMap);
	    }

	} catch (Exception e)
	{

	    mPtLog.fatal("Exception in mEODMonitoring..", e);
	    e.printStackTrace();

	} finally
	{
	    if (lConn != null)
		lConn.close();
	    if (lRst != null)
		lRst.close();
	    if (lPrepStmt != null)
		lPrepStmt.close();
	}

    }
}
