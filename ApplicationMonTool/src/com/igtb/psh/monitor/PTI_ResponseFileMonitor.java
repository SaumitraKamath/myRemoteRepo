/***************************************************************************
/* Copyright ©  2016 Intellect Design Arena Ltd. All rights reserved      */
/*                                                                      */
/************************************************************************/
/*  Application  : PSH Monitoring Engine                             */
/*  Module Name  : Common Services               	     	*/
/*                                                                      */
/*  File Name    : PTI_ResponseFileMonitor.java                             */
/*                                                                      */
/*  Description  : Monitors all response file ACK,NACK,TechNACK,PSR
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.polaris.payments.common.util.PT_DatabaseUtil;

public class PTI_ResponseFileMonitor {

	private static PTI_AlertLogger mPtLog;
	static {
		mPtLog = PTI_AlertLogger.getInstance(PTI_ResponseFileMonitor.class
				.getName());
	}
	java.util.Calendar cal = Calendar.getInstance();
	java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime());
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
	String mDate = sdf.format(sqlDate);

	HashMap mResultMap = null;

	 PTI_AlertEngineImpl lPtiAlertImpl= new PTI_AlertEngineImpl();

/*	public boolean mCheckNackGenerated(Connection con, Date pDate)
			throws Exception {

		mPtLog.debug("Inside mCheckNackGenerated..");

		ResultSet lRst = null;
		PreparedStatement lPrepStmtNack = null;
		String lGetNACKQuery = null;
		List lWorkItemIdList = null;
		List lFilenamelist = null;
		boolean lNackFlag = true;

		try {

			lGetNACKQuery = PTI_MonitorConstants.GET_NACK_QRY;
			lPrepStmtNack = con.prepareStatement(lGetNACKQuery);
			lPrepStmtNack.setString(1, mDate);
			lRst = lPrepStmtNack.executeQuery();
			lWorkItemIdList = new ArrayList();
			lFilenamelist = new ArrayList();
			mResultMap =  new HashMap();
			while (lRst.next()) {
				lWorkItemIdList.add(lRst.getBigDecimal(1));
				lFilenamelist.add(lRst.getString(2));

				mResultMap.put(PTI_MonitorConstants.NACK_ERR_FILE_NAME,lRst.getString(2));
				mResultMap.put(PTI_MonitorConstants.ERROR_CODE,PTI_MonitorConstants.MON_ERR_NACKFILE);
				// MON_ERR_NACKFILE
				//mPtLog.debug("NACK is not generated for the files  lFilenamelist.toString() + " and workitemid is "+ lWorkItemIdList.toString());
				lNackFlag = false;

			}
			mPtLog.debug("lWorkItemIdList.." + lWorkItemIdList.toString()
						+ " :: lFilenamelist.." + lFilenamelist);
			if (!mResultMap.isEmpty()) {
				// MON_ERR_NACKFILE - add to result map....
				
				lPtiAlertImpl.mWriteLog(mResultMap);
			}

		} catch (Exception e) {
			mPtLog.fatal("Exception in mCheckFilePickup..", e);
			e.printStackTrace();
		} finally {
			if (lPrepStmtNack != null)
				lPrepStmtNack.close();
			if (lRst != null)
				lRst.close();
		}
		return lNackFlag;
	}

	public boolean mCheckTechNackGenerated(Connection con, Date pDate)
			throws Exception {

		mPtLog.debug("Inside mCheckTechNackGenerated..");

		ResultSet lRSet = null;
		PreparedStatement lPreStmtTechNack = null;
		boolean lTechNackFlag = true;
		Process proc = null;
		String lGetTechNackQuery = null;
		mResultMap =  new HashMap();
		try {

			lGetTechNackQuery = PTI_MonitorConstants.GET_TECH_NACK_QRY;
			lPreStmtTechNack = con.prepareStatement(lGetTechNackQuery);
			lPreStmtTechNack.setString(1, mDate);
			lRSet = lPreStmtTechNack.executeQuery();
			String[] cmd1 = { PTI_MonitorConstants.LOGS_PATH, "-c",
					"grep 'Technical NACK' payments.log|wc -l" };
			// move to property
			Runtime rt = null;
			while (lRSet != null) {

				// check the log file
				rt = Runtime.getRuntime();

				proc = rt.exec(cmd1);
				BufferedReader br1 = new BufferedReader(new InputStreamReader(
						proc.getInputStream()));
				String line1;
				while (((line1 = br1.readLine()) != null)) {
					mPtLog.debug("Technical Nack is not generated for the file workitemid "+lRSet.getBigDecimal(0));
					lTechNackFlag = false;
					mResultMap.put(PTI_MonitorConstants.FILE_NAME_TNACK,
							lRSet.getString(1));
					
					mResultMap.put(PTI_MonitorConstants.ERROR_CODE,PTI_MonitorConstants.MON_ERR_TECHNACK_FILE);
					// MON_ERR_TECHNACK_FILE
				}
				if(!mResultMap.isEmpty())
					lPtiAlertImpl.mWriteLog(mResultMap);
			}
		} catch (Exception e) {
			mPtLog.fatal("Exception in mCheckTechNackGenerated..", e);
			e.printStackTrace();
		} finally {
			if (lPreStmtTechNack != null)
				lPreStmtTechNack.close();
			if (lRSet != null)
				lRSet.close();
		}
		return lTechNackFlag;
	}

	public boolean mCheckAckGenerated(Connection con, Date pDate)
			throws Exception {

		mPtLog.debug("Inside mCheckAckGenerated..");

		ResultSet lRst = null;
		PreparedStatement lPrepStmt = null;
		String lGetACKQry = null;
		List lWorkItemIdList = null;
		List lFilenamelist = null;
		boolean lAckFlag = true;
		mResultMap =  new HashMap();
		try {

			lGetACKQry = PTI_MonitorConstants.GET_ACK_QRY;
			lPrepStmt = con.prepareStatement(lGetACKQry);
			lPrepStmt.setString(1, mDate);
			lRst = lPrepStmt.executeQuery();
			lWorkItemIdList = new ArrayList();
			lFilenamelist = new ArrayList();

			while (lRst.next()) {
				lWorkItemIdList.add(lRst.getBigDecimal(1));
				lFilenamelist.add(lRst.getString(2));
				mResultMap.put(PTI_MonitorConstants.FILE_NAME_ACK,lRst.getString(2));
				mResultMap.put(PTI_MonitorConstants.ERROR_CODE,
						PTI_MonitorConstants.MON_ERR_ACK_FILE);
				lAckFlag = false;

			}
				mPtLog.debug("lWorkItemIdList.." + lWorkItemIdList.toString()
						+ " :: lFilenamelist.." + lFilenamelist);

				mPtLog.debug("ACK is not generated for the files "
						+ lFilenamelist.toString() + " and workitemid is "
						+ lWorkItemIdList.toString());
			if (!mResultMap.isEmpty())
				lPtiAlertImpl.mWriteLog(mResultMap);

		} catch (Exception e) {
			mPtLog.fatal("Exception in mCheckFilePickup..", e);
			e.printStackTrace();
		} finally {
			if (lPrepStmt != null)
				lPrepStmt.close();
			if (lRst != null)
				lRst.close();
		}
		return lAckFlag;
	}

	public boolean mCheckPSRGenerated(Connection pConn, Date pDate)
			throws Exception {

		mPtLog.debug("Inside mCheckPSRGenerated..");

		ResultSet lRst = null;
		PreparedStatement lPrepStmt = null;
		String lGetPSRQry = null;
		List lWorkItemIdList = null;
		List lFilenamelist = null;
		boolean lPSRFlag = true;
		mResultMap =  new HashMap();
		try {

			lGetPSRQry = PTI_MonitorConstants.GET_PSR_QRY;
			lPrepStmt = pConn.prepareStatement(lGetPSRQry);
			lPrepStmt.setString(1, mDate);
			lRst = lPrepStmt.executeQuery();
			lWorkItemIdList = new ArrayList();
			lFilenamelist = new ArrayList();
			while (lRst.next()) {
				lWorkItemIdList.add(lRst.getBigDecimal(1));
				lFilenamelist.add(lRst.getString(2));
				mResultMap.put(PTI_MonitorConstants.FILE_NAME_PSR,lRst.getString(2));
				mResultMap.put(PTI_MonitorConstants.ERROR_CODE,PTI_MonitorConstants.MON_ERR_PSR_FILE);
				// MON_PSR_FILE_NOTEXISTS
			}
			mPtLog.debug("lWorkItemIdList.." + lWorkItemIdList.toString()
					+ " :: lFilenamelist.." + lFilenamelist);
			if (!mResultMap.isEmpty())
				lPtiAlertImpl.mWriteLog(mResultMap);

		} catch (Exception e) {
			mPtLog.fatal("Exception in mCheckPSRGenerated..", e);
			e.printStackTrace();

		} finally {
			if (lPrepStmt != null)
				lPrepStmt.close();
			if (lRst != null)
				lRst.close();
		}

		return lPSRFlag;
	}
*/
	 
	 public boolean mCheckDBState(Connection con, ArrayList<String> genParams, String query,String error_Code ) throws Exception {

		mPtLog.debug("Inside mCheckNackGenerated..");

		ResultSet lRst = null;
		PreparedStatement lPrepStmtNack = null;
	
		List lWorkItemIdList = null;
		List lFilenamelist = null;
		boolean status = true;

		try {
			lWorkItemIdList = new ArrayList();
			lFilenamelist = new ArrayList();
			mResultMap =  new HashMap();
			
			lPrepStmtNack = con.prepareStatement(query);
			for (int i=1;i<=genParams.size();i++)
			{
			    String tempData =genParams.get(i); 
        			if (tempData instanceof java.lang.String)
        			{
        			    lPrepStmtNack.setString(i, tempData);
        			} 
        			else 
        			{
        			    throw new Exception("Invalid Data type");
        			}     
			}
			
			lRst = lPrepStmtNack.executeQuery();

			while (lRst.next()) 
			{
				lWorkItemIdList.add(lRst.getBigDecimal(1));
				lFilenamelist.add(lRst.getString(2));

				mResultMap.put(PTI_MonitorConstants.FILE_NAME,lRst.getString(2));
				mResultMap.put(PTI_MonitorConstants.ERROR_CODE,error_Code);
				status = false;
			}
			
			mPtLog.debug("lWorkItemIdList.." + lWorkItemIdList.toString() + " :: lFilenamelist.." + lFilenamelist);
			
			if (!mResultMap.isEmpty()) 
			{
				lPtiAlertImpl.mWriteLog(mResultMap);
			}

		} finally {
		    PT_DatabaseUtil.close(lRst);
		    PT_DatabaseUtil.close(lPrepStmtNack);
		}
		return status;
	}
}