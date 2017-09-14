package com.igtb.psh.monitor;

/***************************************************************************
 /* Copyright ©  2016 Intellect Design Arena Ltd. All rights reserved      */
/*                                                                      */
/************************************************************************/
/*  Application  : PSH Monitoring Engine                             */
/*  Module Name  : Common Services               	     	*/
/*                                                                      */
/*  File Name    : PTI_AlertEngineimpl.java                             */
/*                                                                      */
/*  Description  : Writes all the monitoring alerts into log
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

import java.util.HashMap;

public class PTI_AlertEngineImpl extends PTI_AlertEngineFactory
{
    private static PTI_AlertLogger mPtLog;
//    private static boolean isDebugEnabled;

    static
    {
	mPtLog = PTI_AlertLogger.getInstance(PTI_AlertEngineImpl.class.getName());
//	isDebugEnabled = mPtLog.isDebugEnabled();
    }

    public void mWriteLog(HashMap<String,String> pResultMap)
    {// (HashMap lMapErrorCodes){

	// mPtLog.fatal("Inside mWriteLog...");

	String lErrorCode = null;
	try
	{
	    lErrorCode = (String) pResultMap.get(PTI_MonitorConstants.ERROR_CODE);
	    if (PTI_MonitorConstants.MON_ERR_FILE_EXISTS.equals(lErrorCode))
	    {
		mPtLog.fatal("ErrorCode-" + lErrorCode + " File is not getting picked up for last 5 minutes -> " + pResultMap.get(PTI_MonitorConstants.FILE_NAME));
	    }

	    else if (PTI_MonitorConstants.MON_ERR_JMS_QUEUE.equals(lErrorCode))
	    {
		mPtLog.fatal("lErrorCode-" + lErrorCode + " There are messages pending in queue -> " + pResultMap.get(PTI_MonitorConstants.MON_ERR_JMS_QUEUE));
	    } 
	    
	    else if (PTI_MonitorConstants.MON_ERR_JDBC_CON_MAX_LIMIT.equals(lErrorCode))
	    {
		mPtLog.fatal("lErrorCode..." + lErrorCode + " Maximum Connections availabe has been reached in Connection pool ->" + pResultMap.get(PTI_MonitorConstants.MON_ERR_JDBC_CON));
	    }
	    /*
	     * if
	     * (PTI_MonitorConstants.MON_ERR_SCA_FILE_EXISTS.equals(lErrorCode))
	     * { mPtLog.fatal("lErrorCode..." + lErrorCode +
	     * " File is not getting picked up for last 5 minutes .." +
	     * pResultMap.get(PTI_MonitorConstants.FILE_NAME)); } if
	     * (PTI_MonitorConstants.MON_ERR_IWS_FILE_EXISTS.equals(lErrorCode))
	     * { mPtLog.fatal("lErrorCode..." + lErrorCode +
	     * " File is not getting picked up for last 5 minutes .." +
	     * pResultMap.get(PTI_MonitorConstants.FILE_NAME)); } if
	     * (PTI_MonitorConstants.MON_ERR_NACKFILE.equals(lErrorCode)) {
	     * mPtLog.fatal("lErrorCode..." + lErrorCode +
	     * " NACK is not generated for the files .." + pResultMap
	     * .get(PTI_MonitorConstants.NACK_ERR_FILE_NAME)); } if
	     * (PTI_MonitorConstants.MON_ERR_TECHNACK_FILE.equals(lErrorCode)) {
	     * mPtLog.fatal("lErrorCode..." + lErrorCode +
	     * " Technical Nack is not generated for the file ..." +
	     * pResultMap.get(PTI_MonitorConstants.FILE_NAME_TNACK)); } if
	     * (PTI_MonitorConstants.MON_ERR_ACK_FILE.equals(lErrorCode)) {
	     * mPtLog.fatal("lErrorCode..." + lErrorCode +
	     * " ACK is not generated for the file..." +
	     * pResultMap.get(PTI_MonitorConstants.FILE_NAME_ACK)); } if
	     * (PTI_MonitorConstants.MON_ERR_PSR_FILE.equals(lErrorCode)) {
	     * mPtLog.fatal("lErrorCode..." + lErrorCode +
	     * " PSR is not generated for the files .." +
	     * pResultMap.get(PTI_MonitorConstants.FILE_NAME_PSR)); } if
	     * (PTI_MonitorConstants.MON_ERR_TXNRECON.equals(lErrorCode)) {
	     * mPtLog.fatal("lErrorCode..." + lErrorCode +
	     * " Actual txn count does not match transaction table count..." +
	     * pResultMap .get(PTI_MonitorConstants.FILE_NAME_BUSI_MONITOR)); }
	     * if (PTI_MonitorConstants.MON_ERR_JMS_QUEUE.equals(lErrorCode)) {
	     * mPtLog.fatal("lErrorCode..." + lErrorCode +
	     * " There are messages pending in queue.."); } if
	     * (PTI_MonitorConstants.MON_ERR_JDBC_CON_MAX_LIMIT
	     * .equals(lErrorCode)) { mPtLog.fatal("lErrorCode..." + lErrorCode
	     * +
	     * " Maximum Connections availabe has been reached in Connection pool..."
	     * ); } if(PTI_MonitorConstants.MON_ERR_EOD_LIST.equals(lErrorCode)
	     * || PTI_MonitorConstants.MON_ERR_EOD_EXEC.equals(lErrorCode)){
	     * mPtLog.fatal("lErrorCode..." +
	     * lErrorCode+" EOD JOB has not been executed for the day"); }
	     * if(PTI_MonitorConstants.MON_WARN_TXN_STUCK.equals(lErrorCode) ){
	     * mPtLog.fatal("Warning..." +
	     * lErrorCode+" "+pResultMap.get(PTI_MonitorConstants
	     * .WORKITEMID).toString()+
	     * " Payments got stuck in "+pResultMap.get(
	     * PTI_MonitorConstants.TXN_STATUS)); }
	     * if(PTI_MonitorConstants.MON_ERR_TXN_STUCK.equals(lErrorCode) ){
	     * mPtLog.fatal("Error..." +
	     * lErrorCode+" "+pResultMap.get(PTI_MonitorConstants
	     * .WORKITEMID).toString()+
	     * " Payments got stuck in "+pResultMap.get(
	     * PTI_MonitorConstants.TXN_STATUS)); }
	     */
	} catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public boolean mWriteEmail()
    {

	return true;
    }

    public boolean mWriteDashboard()
    {
	return true;
    }
}
