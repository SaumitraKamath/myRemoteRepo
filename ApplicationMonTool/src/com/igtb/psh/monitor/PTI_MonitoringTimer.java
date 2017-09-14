package com.igtb.psh.monitor;
/***************************************************************************
/* Copyright ©  2016 Intellect Design Arena Ltd. All rights reserved      */
/*                                                                      */
/************************************************************************/
/*  Application  : PSH Monitoring Engine                             */
/*  Module Name  : Common Services               	     	*/
/*                                                                      */
/*  File Name    : PTI_MonitoringTimer.java                             */
/*                                                                      */
/*  Description  : Performs scheduler activities
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
import java.util.Timer;
import java.util.TimerTask;

public class PTI_MonitoringTimer extends TimerTask 
{
	private static PTI_AlertLogger mPtLog;
	Timer timer;
	PTI_MonitorScheduler lPTI_MonitorSchedulerObj = new PTI_MonitorScheduler();
	
	static 
	{
		mPtLog = PTI_AlertLogger.getInstance(PTI_MonitoringTimer.class.getName());
	}
	
	
	public PTI_MonitoringTimer(Timer timer) 
	{
		this.timer = timer;
	}

	public void perFormScheduleOpn() {
		try {
			lPTI_MonitorSchedulerObj.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		mPtLog.debug("After Calling perFormScheduleOpn....");
	}

	@Override
	public void run() 
	{
		perFormScheduleOpn();
	}

}
