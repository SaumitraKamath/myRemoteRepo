package com.igtb.psh.monitor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import weblogic.jms.extensions.JMSRuntimeHelper;
import weblogic.jndi.Environment;
import weblogic.management.MBeanHome;
import weblogic.management.runtime.JDBCConnectionPoolRuntimeMBean;
import weblogic.management.runtime.JMSDestinationRuntimeMBean;

@SuppressWarnings("deprecation")
public class PTI_ResourceMonitorHelper
{

    private static PTI_AlertLogger mPtLog;
    static
    {
	mPtLog = PTI_AlertLogger.getInstance(PTI_ResourceMonitorHelper.class.getName());
    }

    

    // Defines the JNDI context factory.
    private PTI_AlertEngineImpl lPtiAlertImpl = new PTI_AlertEngineImpl();

    // @SuppressWarnings({ "deprecation"})
    public HashMap<String, Integer> dataSourceMonitoring() throws Exception
    {

//	mPtLog.debug("Inside dataSourceMonitoring..");

	// DataSource datasource;
	HashMap<String, String> mResultMap = null;
	HashMap<String, Integer> dataSourceHashMap = new HashMap<String, Integer>();
	// int numMsgs = 0;
	ResultSet lRst = null;
	PreparedStatement lPrepStmt = null;
	// String lGetJDBCResourcesQry = null;
	InitialContext ctx = null;
	Connection lConn = null;
	ArrayList<String> lResourceList = PTI_MonitorConstants.RESOURCE_LIST;
	String lDataSourceName = null;
	// get the initial context
	// @SuppressWarnings("deprecation")
	JDBCConnectionPoolRuntimeMBean dataSourceBean = null;
	try
	{

	    /*
	     * lGetJDBCResourcesQry = PTI_MonitorConstants.GET_JDBC_SRC_QRY;
	     * lConn = PT_DatabaseUtil.mGetConnection(); lResourceList = new
	     * ArrayList<String>(); lPrepStmt =
	     * lConn.prepareStatement(lGetJDBCResourcesQry); lRst =
	     * lPrepStmt.executeQuery(); while (lRst.next()) {
	     * lResourceList.add(lRst.getString(1));
	     * 
	     * }
	     */

	    // mPtLog.debug("lResourceList..." + lResourceList.toString());

	    Properties lEnv = new Properties();
	    lEnv.put(Context.PROVIDER_URL, PTI_MonitorConstants.PROVIDER_URL);
	    lEnv.put(Context.SECURITY_PRINCIPAL, PTI_MonitorConstants.CONSOLE_UNAME);
	    lEnv.put(Context.SECURITY_CREDENTIALS, PTI_MonitorConstants.CONSOLE_PASS);
	    lEnv.put(Context.INITIAL_CONTEXT_FACTORY, PTI_MonitorConstants.INIT_CONT_FACTORY);
	    ctx = new InitialContext(lEnv);

	    for (int i = 0; i < lResourceList.size(); i++)
	    {
		mResultMap = new HashMap<String, String>();
		lDataSourceName = (String) lResourceList.get(i);
		MBeanHome home = getMBeanHome();
		dataSourceBean = (JDBCConnectionPoolRuntimeMBean) home.getRuntimeMBean(lDataSourceName, "JDBCConnectionPoolRuntime");
		mPtLog.debug("lDataSourceName : " + lDataSourceName);
		mPtLog.debug("Leaked conn count : " + dataSourceBean.getLeakedConnectionCount());
		mPtLog.debug("Active conn High count : " + dataSourceBean.getActiveConnectionsHighCount());
		mPtLog.debug("Active conn Current count : " + dataSourceBean.getActiveConnectionsCurrentCount());
		mPtLog.debug("Active conn Average count : " + dataSourceBean.getActiveConnectionsAverageCount());
		mPtLog.debug("ConnectionDelayTime : " + dataSourceBean.getConnectionDelayTime());
		mPtLog.debug("MaxCapacity : " + dataSourceBean.getMaxCapacity());
		mPtLog.debug("ConnectionsTotalCount : " + dataSourceBean.getConnectionsTotalCount());
		mPtLog.debug("FailuresToReconnectCount : " + dataSourceBean.getFailuresToReconnectCount());
		mPtLog.debug("CurrCapacity : " + dataSourceBean.getCurrCapacity());

		if (dataSourceBean.getConnectionsTotalCount() >= dataSourceBean.getMaxCapacity())
		{

		    // mPtLog.fatal("Maximum Connections availabe has been reached in Connection pool.."
		    // + lDataSourceName);

		    mResultMap.put(PTI_MonitorConstants.ERROR_CODE, PTI_MonitorConstants.MON_ERR_JDBC_CON_MAX_LIMIT);
		    mResultMap.put(PTI_MonitorConstants.MON_ERR_JDBC_CON, lDataSourceName);
//		    lPtiAlertImpl.mWriteLog(mResultMap);
		}
		else if (dataSourceBean.getLeakedConnectionCount() > 0)
		{

		    mResultMap.put(PTI_MonitorConstants.ERROR_CODE, PTI_MonitorConstants.MON_ERR_JDBC_CON_MAX_LIMIT);
		    mResultMap.put(PTI_MonitorConstants.MON_ERR_JDBC_CON, lDataSourceName);
		}

		    if (!mResultMap.isEmpty())
			lPtiAlertImpl.mWriteLog(mResultMap);
	    }
	} catch (Exception e)
	{
	    // TODO: handle exception
	    mPtLog.fatal("Exception in dataSourceMonitoring for ", e);
	    e.printStackTrace();
	} finally
	{
	    if (ctx != null)
		ctx.close();
	    if (lPrepStmt != null)
		lPrepStmt.close();
	    if (lRst != null)
		lRst.close();
	    if (lConn != null)
		lConn.close();
	}

	return dataSourceHashMap;
    }

    public void queueMonitoring() throws NamingException, JMSException, Exception
    {
	// JMXConnector connector;
	// MBeanServerConnection connection;
	ArrayList<String> lQueueList = null;
	HashMap<String, String> mResultMap = null;
	InitialContext ctx = null;
	Destination queue = null;
	JMSDestinationRuntimeMBean destMBean = null;
	// String lGetJMSQueuesQry = null;
	Connection lConn = null;
	ResultSet lRst = null;
	PreparedStatement lPrepStmt = null;
	try
	{
	    lQueueList = PTI_MonitorConstants.QUEUE_LIST;
	    /*
	     * lGetJMSQueuesQry = PTI_MonitorConstants.GET_JMS_QUEUE_QRY; lConn
	     * = PT_DatabaseUtil.mGetConnection(); lPrepStmt =
	     * lConn.prepareStatement(lGetJMSQueuesQry); lRst =
	     * lPrepStmt.executeQuery(); while(lRst.next()){
	     * lQueueList.add(lRst.getString(1)); }
	     */

	    // mPtLog.debug("lQueueList..."+lQueueList.toString());

	    Properties lEnv = new Properties();
	    lEnv.put(Context.PROVIDER_URL, PTI_MonitorConstants.PROVIDER_URL);
	    lEnv.put(Context.SECURITY_PRINCIPAL, PTI_MonitorConstants.CONSOLE_UNAME);
	    lEnv.put(Context.SECURITY_CREDENTIALS, PTI_MonitorConstants.CONSOLE_PASS);
	    lEnv.put(Context.INITIAL_CONTEXT_FACTORY, PTI_MonitorConstants.INIT_CONT_FACTORY);
	    ctx = new InitialContext(lEnv);
	    for (int i = 0; i < lQueueList.size(); i++)
	    {
		mResultMap = new HashMap<String, String>();
		queue = (Destination) ctx.lookup(lQueueList.get(i).toString());
		destMBean = JMSRuntimeHelper.getJMSDestinationRuntimeMBean(ctx, queue);

		mPtLog.debug("QueueName :: " + lQueueList.get(i).toString());
		mPtLog.debug("Messages Currnet Count :: " + destMBean.getMessagesCurrentCount());
		mPtLog.debug("MessageHighCount :: " + destMBean.getMessagesHighCount());
		mPtLog.debug("MessagePendingCount :: " + destMBean.getMessagesPendingCount());
		mPtLog.debug("MessagesReceivedCount :: " + destMBean.getMessagesReceivedCount());
		mPtLog.debug("BytesCurrentCount :: " + destMBean.getBytesCurrentCount() + " BytesHighCount:: " + destMBean.getBytesHighCount());
		mPtLog.debug("BytesReceivedCount :: " + destMBean.getBytesReceivedCount() + " BytesThresholdTime :: " + destMBean.getBytesThresholdTime());

		if (destMBean.getBytesThresholdTime() >= 5)
		{

		    // mPtLog.fatal("There are messages pending in queue .." +queue);
		    mResultMap.put(PTI_MonitorConstants.ERROR_CODE, PTI_MonitorConstants.MON_ERR_JMS_QUEUE);
		    mResultMap.put(PTI_MonitorConstants.MON_ERR_JMS_QUEUE, lQueueList.get(i).toString());
		}
		else if (destMBean.getMessagesPendingCount() > 0)
		{

		    // mPtLog.fatal("There are messages pending in queue .." +queue);
		    mResultMap.put(PTI_MonitorConstants.ERROR_CODE, PTI_MonitorConstants.MON_ERR_JMS_QUEUE);
		    mResultMap.put(PTI_MonitorConstants.MON_ERR_JMS_QUEUE, lQueueList.get(i).toString());
		}
		    if (!mResultMap.isEmpty())
			lPtiAlertImpl.mWriteLog(mResultMap);
	    }


	} catch (Exception e)
	{

	    mPtLog.fatal("Exception in queueMonitoring..", e);
	    e.printStackTrace();
	} finally
	{
	    if (ctx != null)
		ctx.close();
	    if (lPrepStmt != null)
		lPrepStmt.close();
	    if (lRst != null)
		lRst.close();
	    if (lConn != null)
		lConn.close();
	}
    }

//    @SuppressWarnings("deprecation")
    private MBeanHome getMBeanHome()
    {
	// URL to the serve whose JDBC activity we are tracing
	String url = PTI_MonitorConstants.PROVIDER_URL;
	String username = PTI_MonitorConstants.CONSOLE_UNAME;
	String password = PTI_MonitorConstants.CONSOLE_PASS;
	// The MBeanHome will allow us to
	// retrieve the MBeans related to JDBC statement tracing
	MBeanHome home = null;

	try
	{ // We'll need the environment so that we can //retrieve the initial
	  // context
	    Environment env = new Environment();
	    env.setProviderUrl(url);
	    env.setSecurityPrincipal(username);
	    env.setSecurityCredentials(password);
	    Context ctx = env.getInitialContext();
	    // Retrieving the MBeanHome interface for the server with //the url
	    // t3://localhost:7001
	    home = (MBeanHome) ctx.lookup(MBeanHome.LOCAL_JNDI_NAME);
	} catch (NamingException ne)
	{
	    mPtLog.debug("Error getting MBeanHome " + ne);
	}
	return home;
    }

}
