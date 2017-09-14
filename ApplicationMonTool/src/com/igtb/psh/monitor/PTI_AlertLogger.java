package com.igtb.psh.monitor;

import java.io.InputStream;
import java.util.Properties;

import com.polaris.intellect.logger.Level;
import com.polaris.intellect.logger.Logger;
import com.polaris.intellect.logger.PropertyConfigurator;
//import com.polaris.payments.common.PT_Constants;

public class PTI_AlertLogger {


	/**
	 * The reference for Logger class
	 */

	private Logger m_logger = null;
	private static final String m_l_class_name = PTI_AlertLogger.class.getName();

	static {
		try {
			PropertyConfigurator.configure(loadFile()); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Properties loadFile()
	{
		Properties l_propINI;
		InputStream lInputStream;
		try {
			ClassLoader lClassLoader = Class.forName("com.igtb.psh.monitor.PTI_AlertLogger").getClassLoader();
			 
			String sFilename = "iflow-logger.properties";
			l_propINI = new Properties();
			lInputStream = lClassLoader.getResourceAsStream(sFilename);
			if (lInputStream == null) {
				throw new Exception("Custom Logging not configured");
			}

			l_propINI.load(lInputStream);
			return l_propINI;
		} catch (Exception p_e) 
		{
		    	new PTI_AlertLogger("PTI_AlertLogger").m_logger.fatal("Issue in loading properties"+p_e.getMessage(),p_e);
			return null;
		}
	}
 
	/**
	 * This constructor takes the module name as parameters, all users will make
	 * an instance of this class by passing their module name, this constructor
	 * in tern calls Log4j's getLogger method py passing the passed parameter,
	 * so as to make Logger instance for particular module,
	 */

	public static PTI_AlertLogger getInstance(String pClassName) {
		return new PTI_AlertLogger(pClassName);
	}

	public PTI_AlertLogger(String pClassName) {
		m_logger = Logger.getLogger(pClassName);
	}

	public final boolean isDebugEnabled() {
		return m_logger.isDebugEnabled();
	}
	
	public final boolean isErrorEnabled() {
		return m_logger.isDebugEnabled();
	}

	/**
	 * Log a message object with the {@link Level#DEBUG DEBUG} level.
	 * 
	 * @param debugMessage
	 *            Object - the message object to log.
	 */

	public final void debug(Object p_debugMessage) {
		m_logger.log(m_l_class_name, Level.DEBUG, p_debugMessage, null); 
	}

	/**
	 * Log a message object with the {@link Level#DEBUG DEBUG} level.
	 * 
	 * @param debugMessage
	 *            Object - the message object to log.
	 * @param exception
	 *            Throwable - to have stack trace of exception
	 */

	public final void debug(Object p_debugMessage, Throwable p_exception) {
		m_logger.log(m_l_class_name, Level.DEBUG, p_debugMessage, p_exception); 
	}

	/**
	 * Log a message object with the {@link Level#INFO INFO} level.
	 * 
	 * @param infoMessage
	 *            Object - the message object to log.
	 */

	public final void info(Object p_infoMessage) {
		m_logger.log(m_l_class_name, Level.INFO, p_infoMessage, null); 
	}

	/**
	 * Log a message object with the {@link Level#INFO INFO} level.
	 * 
	 * @param infoMessage
	 *            Object - the message object to log.
	 * @param exception
	 *            Throwable - the exception object to have stack trace.
	 */

	public final void info(Object p_infoMessage, Throwable p_exception) {
		m_logger.log(m_l_class_name, Level.INFO, p_infoMessage, p_exception); 
	}

	/**
	 * Log a message object with the {@link Level#WARN WARN} level.
	 * 
	 * @param warnMessage
	 *            Object - the message object to log.
	 */

	public final void warn(Object p_warnMessage) {
		m_logger.log(m_l_class_name, Level.WARN, p_warnMessage, null);
	}

	/**
	 * Log a message object with the {@link Level#WARN WARN} level.
	 * 
	 * @param warnMessage
	 *            Object - the message object to log.
	 * @param exception
	 *            Throwable - the exception object to have stack trace.
	 */

	public final void warn(Object p_warnMessage, Throwable p_exception) {
		m_logger.log(m_l_class_name, Level.WARN, p_warnMessage, p_exception);
	}

	/**
	 * Log a message object with the {@link Level#ERROR ERROR} level.
	 * 
	 * @param errorMessage
	 *            Object - the message object to log.
	 */

	public final void error(String p_errorMessage) {
		m_logger.log(m_l_class_name, Level.ERROR, p_errorMessage, null); 
	}

	/**
	 * Log a message object with the {@link Level#ERROR ERROR} level.
	 * 
	 * @param errorMessage
	 *            Object - the message object to log.
	 * @param exception
	 *            Throwable - the exception object to have stack trace.
	 */

	public final void error(String p_errorMessage, Throwable p_exception) {
		m_logger.log(m_l_class_name, Level.ERROR, p_errorMessage, p_exception);
	}

	/**
	 * Log a message object with the {@link Level#FATAL FATAL} level.
	 * 
	 * @param fatalMessage
	 *            Object - the message object to log.
	 */

	public final void fatal(Object p_fatalMessage) {
		m_logger.log(m_l_class_name, Level.FATAL, p_fatalMessage, null);
	}

	/**
	 * Log a message object with the {@link Level#FATAL FATAL} level.
	 * 
	 * @param fatalMessage
	 *            Object - the message object to log.
	 * @param exception
	 *            Throwable - the exception object to have stack trace.
	 */

	public final void fatal(Object p_fatalMessage, Throwable p_exception) {
		m_logger.log(m_l_class_name, Level.FATAL, p_fatalMessage, p_exception);
	}

	/*
	 * public static void reset(){ try {
	 * PropertyConfigurator.configure(loadFile()); //1.5 } catch(Exception e) {
	 * e.printStackTrace(); } }
	 */


}
