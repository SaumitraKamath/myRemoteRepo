package com.igtb.psh.monitor;


import java.util.Properties;
public class PTI_MonitoringPropertiesReader {
	private static PTI_AlertLogger mPtLog;
	static {
		mPtLog = PTI_AlertLogger.getInstance(PTI_MonitoringPropertiesReader.class.getName());
	}
	public PTI_MonitoringPropertiesReader()
	{
	}

	public static Properties getProperties(String fileName)throws Exception
	{
		Properties prop = new Properties();
		try
		{
			ClassLoader clasLoader = Class.forName("com.igtb.psh.monitor.PTI_MonitoringPropertiesReader").getClassLoader();
			prop.load(clasLoader.getResourceAsStream(fileName));
		}
		catch(Exception e)
		{
			mPtLog.fatal("MON_ERR_SCH_PROP - Can not find  PTI_MonitorConstants.properties file ",e);
			e.printStackTrace();
		}
		return prop;
	}

}