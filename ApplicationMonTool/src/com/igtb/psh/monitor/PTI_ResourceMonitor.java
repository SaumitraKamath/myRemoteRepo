/***************************************************************************
/* Copyright ©  2016 Intellect Design Arena Ltd. All rights reserved      */
/*                                                                      */
/************************************************************************/
/*  Application  : PSH Monitoring Engine                             */
/*  Module Name  : Common Services               	     	*/
/*                                                                      */
/*  File Name    : PTI_ResourceMonitor.java                             */
/*                                                                      */
/*  Description  : Invokes Resource Monitoring Helper
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.jms.JMSException;
import javax.naming.NamingException;

import com.igtb.psh.monitor.PTI_AlertEngineFactory;
import com.igtb.psh.monitor.PTI_AlertEngineImpl;


public class PTI_ResourceMonitor {
	
	private static PTI_AlertLogger mPtLog;
	static {
		mPtLog = PTI_AlertLogger.getInstance(PTI_ResourceMonitor.class.getName());
	}

	/**
	 * @param args
	 * @throws JMSException 
	 * @throws NamingException 
	 */
	
	public void resourceMonitoring() throws NamingException, JMSException{
		
//		mPtLog.debug("Inside resourceMonitoring..");
		try {
			
		PTI_ResourceMonitorHelper rmh = new PTI_ResourceMonitorHelper();
		rmh.queueMonitoring() ;
		rmh.dataSourceMonitoring();
		
		}
		catch (Exception e) {
			
			mPtLog.fatal("Exception in resourceMonitoring..",e);
		}
		
		
	}
	
	}