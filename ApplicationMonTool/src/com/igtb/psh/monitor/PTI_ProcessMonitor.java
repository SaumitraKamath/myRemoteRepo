package com.igtb.psh.monitor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;


public class PTI_ProcessMonitor {
	
	private static PTI_AlertLogger mPtLog;
	static {
		mPtLog = PTI_AlertLogger.getInstance(PTI_ProcessMonitor.class.getName());
	}
	HashMap mResultMap = new HashMap();

	 PTI_AlertEngineImpl lPtiAlertImpl=PTI_AlertEngineFactory.mWriteLog();
	public void processMonitoring() throws Exception{
		
		 Runtime rt = Runtime.getRuntime();///apps/wldomains/wls-psh-1/logs/ipshlogs/
         String[] cmd = { PTI_MonitorConstants.LOGS_PATH, "-c", "grep 'IPSHException' ipsh-scheduler.log|wc -l" };
         Process proc = rt.exec(cmd);
         BufferedReader is = new BufferedReader(new InputStreamReader(proc.getInputStream()));
         String line;
         while ((line = is.readLine()) == null) {
              mPtLog.debug("There is problem in Schedulers");
             }
	        	mResultMap.put(PTI_MonitorConstants.MONITOR_TYPE,"ProcessMonitor");
	        	lPtiAlertImpl.mWriteLog(mResultMap);
         }
	
}
