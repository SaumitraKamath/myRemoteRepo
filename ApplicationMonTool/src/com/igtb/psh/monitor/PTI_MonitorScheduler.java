package com.igtb.psh.monitor;

import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

//import com.polaris.ipsh.exception.IPSHException;

public class PTI_MonitorScheduler implements ServletContextListener
{

    private static PTI_AlertLogger mPtLog;
    static
    {
	// System.out.println("Inside PTI_MonitorScheduler");
	mPtLog = PTI_AlertLogger.getInstance(PTI_MonitorScheduler.class.getName());
    }

    public void execute() throws Exception
    {

	PTI_FileMonitor lPTI_FileMonitorObj = null;
	// PTI_BusinessStateMonitor lPTI_BusinessStateMonitorObj = null;
	PTI_ResourceMonitor lPTI_ResourceMonitorObj = null;
	// PTI_EODJobsMonitor lPTI_EODJobsMonitorObj = null;
	try
	{

	    mPtLog.debug("Schedular is being invoked for time slot::" + new java.util.Date());

	    lPTI_FileMonitorObj = new PTI_FilesMonitor();
	    lPTI_FileMonitorObj.fileMonitoring();

	    // lPTI_BusinessStateMonitorObj = new PTI_BusinessStateMonitor();
	    // lPTI_BusinessStateMonitorObj.businessStateMonitoring();
	    // lPTI_BusinessStateMonitorObj.monitorTxnProcessing();
	    lPTI_ResourceMonitorObj = new PTI_ResourceMonitor();
	    lPTI_ResourceMonitorObj.resourceMonitoring();
	    //
	    // lPTI_EODJobsMonitorObj = new PTI_EODJobsMonitor();
	    // lPTI_EODJobsMonitorObj.mEODMonitoring();

	} catch (Exception e)
	{

	    mPtLog.fatal("MON_ERR_SCHEDULER - Exception in  execute method PTI_MonitorScheduler ", e);
	    e.printStackTrace();
	    // throw new IPSHException(e);
	}
    }

    public void initializeScheduler()
    {
	long period = 1000 * PTI_MonitorConstants.SCH_FREQ_IN_SEC;
	Timer timer = new Timer();
	PTI_MonitoringTimer tps = new PTI_MonitoringTimer(timer);
	timer.schedule(tps, 0, period); // the time specified in millisecond.
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0)
    {
	// TODO Auto-generated method stub
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0)
    {
	// System.out.println("Container started...");
	initializeScheduler();
    }

}