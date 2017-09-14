package com.igtb.psh.monitor;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StartupServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    private static PTI_AlertLogger logger;
    // PTI_MonitorScheduler lPTI_MonitorSchedulerObj = new
    // PTI_MonitorScheduler();

    static
    {
	logger = PTI_AlertLogger.getInstance(StartupServlet.class.getName());

	// System.out.println("Inside PTI_MonitoringTimer... ");
    }

    /**
     * Initialise method of the servlet. In this method, the DST Timer Service
     * is instantiated and using the timer service, the DST adjustments are
     * scheduled.
     */
    public void init()
    {
	if (logger.isDebugEnabled())
	    logger.debug("Entering init() of StartupServlet");

	long period = 1000 * PTI_MonitorConstants.SCH_FREQ_IN_SEC;
	Timer timerObj = new Timer();
	TimerTask task = new PTI_MonitoringTimer(timerObj);
	timerObj.schedule(task, 0, period);

	if (logger.isDebugEnabled())
	    logger.debug("Leaving init() of StartupServlet");
    }

    /**
     * Service method of the servlet. In this method, the DST Timer Service is
     * instantiated and using the timer service, the DST adjustments are
     * scheduled.
     */
    public void service(HttpServletRequest req, HttpServletResponse res)
    {
	if (logger.isDebugEnabled())
	    logger.debug("Entering service() StartupServlet.");

	if (logger.isDebugEnabled())
	    logger.debug("Leaving service() of StartupServlet.");
    }
}