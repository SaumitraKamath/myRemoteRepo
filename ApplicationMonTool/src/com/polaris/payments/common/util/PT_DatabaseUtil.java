package com.polaris.payments.common.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.igtb.psh.monitor.PTI_AlertLogger;


public class PT_DatabaseUtil
{
    private static PTI_AlertLogger logger;
	static {
	    logger = PTI_AlertLogger.getInstance(PT_DatabaseUtil.class.getName());
	}
    
	public static String DATASOURCE = null;

	static DataSource dataSource = null;

	public static Properties PROPERTIES = null;

	private static Map<String, Object> propertiesCache = new Hashtable<String, Object>();

	static {
		try {
			PROPERTIES = getProperties("apiutil.properties");

			String ds = (String) PROPERTIES.get("DataSource");
			if (ds != null) {
				DATASOURCE = ds;
			}
		} catch (Exception e) {
			if(logger.isErrorEnabled()) {
			logger.error(
					"Static Block of OLMConnection Utility while Loading the Properties :: "
							+ e.getMessage(), e);
			}
		}
	}

	public static Properties getProperties(String propFileName)
			throws Exception {
		Properties props = (Properties) propertiesCache.get(propFileName);
		if (props == null) {
			synchronized (propertiesCache) {
				props = loadProperties(propFileName);
				propertiesCache.put(propFileName, props);
			}
		}
		return props;
	}

	public static Properties loadProperties(String propFileName)
			throws Exception {
		Properties props = new Properties();
		InputStream is;
		synchronized (PT_DatabaseUtil.class) {
			is = PT_DatabaseUtil.class.getClassLoader().getResourceAsStream(propFileName);
		}

		try {
			props.load(is);
		} finally {
			try {
				is.close();
			} finally {
				if(logger.isErrorEnabled()) {
					logger.error("Static Block of OLMConnection Utility while Loading the Properties :: ");
				}
			}
		}
		return props;
	}

	public static Connection mGetConnection() throws NamingException,
			SQLException {
		Context ctx = new InitialContext();
		Connection conn = getConnection(ctx, DATASOURCE);
		ctx.close();
		return conn;
	}


	private static Connection getConnection(Context ctx,
			final String dataSourceName) {
		try {
			if (dataSource == null) {
				dataSource = (DataSource) ctx.lookup(dataSourceName);
			}
			return dataSource.getConnection();
		} catch (NamingException e) {
			if(logger.isErrorEnabled()) {
				logger.error("close:: " + e.getMessage(), e);
			}
			return null;
		} catch (SQLException e) {
			if(logger.isErrorEnabled()) {
				logger.error("close:: " + e.getMessage(), e);
			}
			return null;
		}
	}

	public static void close(Connection conn) {

		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			if(logger.isErrorEnabled()) {
				logger.error("close:: " + e.getMessage(), e);
			}
		}
	}

	public static void close(PreparedStatement pstmt) {

		try {
			if (pstmt != null) {
				pstmt.close();
			}
		} catch (SQLException e) {
			if(logger.isErrorEnabled()) {
				logger.error("close:: " + e.getMessage(), e);
			}
		}
	}

	public static void close(Statement stmt) {

		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (SQLException e) {
			if(logger.isErrorEnabled()) {
				logger.error("close:: " + e.getMessage(), e);
			}
		}
	}

	public static void close(ResultSet rSet) {

		try {
			if (rSet != null) {
				rSet.close();
			}
		} catch (SQLException e) {
			if(logger.isErrorEnabled()) {
				logger.error("close:: " + e.getMessage(), e);
			}
		}
	}
}
