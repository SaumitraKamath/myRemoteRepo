/***************************************************************************
/* Copyright ©  2016 Intellect Design Arena Ltd. All rights reserved      */
/*                                                                      */
/************************************************************************/
/*  Application  : PSH Monitoring Engine                             */
/*  Module Name  : Common Services               	     	*/
/*                                                                      */
/*  File Name    : PTI_BusinessStateMonitor.java                             */
/*                                                                      */
/*  Description  : Monitors all Txn are debulked in a payment files
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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.polaris.payments.common.util.PT_DatabaseUtil;

public class PTI_BusinessStateMonitor
{

    private static PTI_AlertLogger mPtLog;
    static
    {
	mPtLog = PTI_AlertLogger.getInstance(PTI_BusinessStateMonitor.class.getName());
    }

    HashMap mResultMap = new HashMap();
    java.util.Calendar cal = Calendar.getInstance();
    java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime());
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
    String mDate = sdf.format(sqlDate);
    PTI_AlertEngineImpl lPtiAlertImpl = PTI_AlertEngineFactory.mWriteLog();

    public void businessStateMonitoring() throws Exception
    {

	mPtLog.debug("Inside businessStateMonitoring...");

	Connection con = null;
	PreparedStatement lPrepStmt = null;
	ResultSet lRst = null;
	Map<BigDecimal, PTI_FileDetBean> lFileDetMap = null;
	String fileCountQuery = null;
	try
	{
	    fileCountQuery = PTI_MonitorConstants.GET_ACT_TXN_CNT_QRY;
	    con = PT_DatabaseUtil.mGetConnection();
	    // con = getConnection();

	    lPrepStmt = con.prepareStatement(fileCountQuery);
	    lPrepStmt.setString(1, mDate);
	    lRst = lPrepStmt.executeQuery(); // result set is cartesian product
					     // of FPO and TPO
	    lFileDetMap = new HashMap<BigDecimal, PTI_FileDetBean>();
	    PTI_FileDetBean bean = null;
	    List<BigDecimal> txnWidList = null;
	    while (lRst.next())
	    {
		if (!lFileDetMap.containsKey(lRst.getBigDecimal("FILE_WID")))
		{
		    bean = new PTI_FileDetBean();
		    txnWidList = new ArrayList<BigDecimal>();
		    bean.setWorkitemId(lRst.getBigDecimal("FILE_WID"));
		    bean.setActualTxnCount(lRst.getInt("ACTUAL_COUNT"));
		    bean.setFileName(lRst.getString("FILENAME"));
		    txnWidList.add(lRst.getBigDecimal("TXN_WORKITEMID"));
		    lFileDetMap.put(lRst.getBigDecimal("FILE_WID"), bean);
		} else
		{
		    txnWidList.add(lRst.getBigDecimal("TXN_WORKITEMID"));
		}
		bean.setTxnWidList(txnWidList);
	    }

	    mPtLog.debug("lFileDetMap.." + lFileDetMap);

	    for (Map.Entry<BigDecimal, PTI_FileDetBean> entry : lFileDetMap.entrySet())
	    {
		PTI_FileDetBean value = entry.getValue();

		mPtLog.debug("value.getTxnWidList()..." + value.getTxnWidList());

		if (value != null && value.getTxnWidList() != null)
		{
		    if (value.getActualTxnCount() != value.getTxnWidList().size())
		    {
			mPtLog.debug("Actual txn count does not match transaction table count : " + value.getFileName() + " " + "| Actual no.of txn's: " + value.getActualTxnCount() + " | Transactions debulked: " + value.getTxnWidList().size());
			mResultMap.put(PTI_MonitorConstants.FILE_NAME_BUSI_MONITOR, PTI_MonitorConstants.BUSINESS_STATE_MONITOR);
			mResultMap.put(PTI_MonitorConstants.ERROR_CODE, PTI_MonitorConstants.MON_ERR_TXNRECON);
		    }
		}
		if (!mResultMap.isEmpty())
		    lPtiAlertImpl.mWriteLog(mResultMap);
	    }
	} catch (Exception e)
	{
	    mPtLog.fatal("Exception in businessStateMonitoring", e);
	    e.printStackTrace();
	} finally
	{
	    if (con != null)
		con.close();
	    if (lRst != null)
		lRst.close();
	    if (lPrepStmt != null)
		lPrepStmt.close();
	}
    }

    public void monitorTxnProcessing() throws Exception
    {

	mPtLog.debug("Inside monitorTxnProcessing...");

	Connection con = null;
	PreparedStatement lPrepStmt = null;
	ResultSet lRst = null;
	String lGetTxnsQuery = null;
	HashMap lTxnDtlMap = null;
	List lWoritemidList = new ArrayList();
	Date lMakerDateTime = null;
	List lFinalDataList = null;
	Map lResultMap = new HashMap();
	Date lCurrentDateTime = new Date();
	int lDiff = 0;
	int lWaringMaxTime = 0;
	int lErrMaxTime = 0;
	String lTxnStatus = null;
	try
	{

	    lGetTxnsQuery = PTI_MonitorConstants.GET_TXN_STATUS_QRY;
	    con = PT_DatabaseUtil.mGetConnection();
	    lPrepStmt = con.prepareStatement(lGetTxnsQuery);
	    lRst = lPrepStmt.executeQuery();
	    lFinalDataList = new ArrayList();
	    while (lRst.next())
	    {
		lTxnDtlMap = new HashMap();
		lTxnDtlMap.put("WORKITEMID", lRst.getString(1));
		lTxnDtlMap.put("TXN_STATUS", lRst.getString(2));
		lTxnDtlMap.put("MAKER_DATE", lRst.getTimestamp(3));
		lTxnDtlMap.put("WARN_MAX_TIME_IN_SEC", lRst.getInt(4));
		lTxnDtlMap.put("ERR_MAX_TIME_IN_SEC", lRst.getInt(5));

		lFinalDataList.add(lTxnDtlMap);
	    }

	    mPtLog.debug("lFinalDataList..." + lFinalDataList);

	    for (int i = 0; i < lFinalDataList.size(); i++)
	    {

		lResultMap = (Map) lFinalDataList.get(i);
		lMakerDateTime = (Timestamp) lResultMap.get("MAKER_DATE");

		lWaringMaxTime = (Integer) lResultMap.get("WARN_MAX_TIME_IN_SEC");
		lErrMaxTime = (Integer) lResultMap.get("ERR_MAX_TIME_IN_SEC");

		mPtLog.debug(" lWaringMaxTime.." + lWaringMaxTime + " :: lErrMaxTime.. " + lErrMaxTime);

		mPtLog.debug("lMakerDateTime is:" + lMakerDateTime + ":: currentDateTimeString..." + lCurrentDateTime);

		lDiff = minutesDiff(lMakerDateTime, lCurrentDateTime);

		mPtLog.debug(" Diff.." + lDiff);

		// mPtLog.debug(" lWoritemidList.."+lWoritemidList.toString());

		if (lDiff > lWaringMaxTime)
		{
		    lWoritemidList.add(lResultMap.get("WORKITEMID"));
		    lTxnStatus = (String) lResultMap.get("TXN_STATUS");
		    mResultMap.put(PTI_MonitorConstants.ERROR_CODE, PTI_MonitorConstants.MON_WARN_TXN_STUCK);
		    mResultMap.put(PTI_MonitorConstants.TXN_STATUS, lTxnStatus);
		}
		if (lDiff > lErrMaxTime)
		{
		    lWoritemidList.add(lResultMap.get("WORKITEMID"));
		    lTxnStatus = (String) lResultMap.get("TXN_STATUS");
		    mResultMap.put(PTI_MonitorConstants.ERROR_CODE, PTI_MonitorConstants.MON_ERR_TXN_STUCK);
		    mResultMap.put(PTI_MonitorConstants.TXN_STATUS, lTxnStatus);

		}
	    }

	    mResultMap.put("WORKITEMID", lWoritemidList);

	    // mPtLog.debug(lWoritemidList.toString()+
	    // "(s) are stuck in "+lTxnStatus);

	    if (!mResultMap.isEmpty())
		lPtiAlertImpl.mWriteLog(mResultMap);

	    // get the current date

	} catch (Exception e)
	{
	    mPtLog.debug("Exception in monitorTxnProcessing.. ", e);
	    e.printStackTrace();
	} finally
	{
	    if (con != null)
		con.close();
	    if (lRst != null)
		lRst.close();
	    if (lPrepStmt != null)
		lPrepStmt.close();
	}
    }

    public static int minutesDiff(Date earlierDate, Date laterDate)
    {
	if (earlierDate == null || laterDate == null)
	    return 0;

	return (int) ((laterDate.getTime() / 60000) - (earlierDate.getTime() / 60000));
    }

}