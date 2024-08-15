package com.zhang.jdbc.datasource;

import com.zhang.util.ClassUtils;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;


public class DriverManagerDataSource extends AbstractDriverBasedDataSource {

	/**
	 * Constructor for bean-style configuration.
	 */
	public DriverManagerDataSource() {
	}

	/**
	 * Create a new DriverManagerDataSource with the given JDBC URL,
	 * not specifying a username or password for JDBC access.
	 * @param url the JDBC URL to use for accessing the DriverManager
	 * @see java.sql.DriverManager#getConnection(String)
	 */
	public DriverManagerDataSource(String url) {
		setUrl(url);
	}

	/**
	 * Create a new DriverManagerDataSource with the given standard
	 * DriverManager parameters.
	 * @param url the JDBC URL to use for accessing the DriverManager
	 * @param username the JDBC username to use for accessing the DriverManager
	 * @param password the JDBC password to use for accessing the DriverManager
	 * @see java.sql.DriverManager#getConnection(String, String, String)
	 */
	public DriverManagerDataSource(String url, String username, String password) {
		setUrl(url);
		setUsername(username);
		setPassword(password);
	}

	/**
	 * Create a new DriverManagerDataSource with the given JDBC URL,
	 * not specifying a username or password for JDBC access.
	 * @param url the JDBC URL to use for accessing the DriverManager
	 * @param conProps the JDBC connection properties
	 * @see java.sql.DriverManager#getConnection(String)
	 */
	public DriverManagerDataSource(String url, Properties conProps) {
		setUrl(url);
		setConnectionProperties(conProps);
	}


	public void setDriverClassName(String driverClassName) {
		String driverClassNameToUse = driverClassName.trim();
		try {
			Class.forName(driverClassNameToUse, true, ClassUtils.getDefaultClassLoader());
		}
		catch (ClassNotFoundException ex) {
			throw new IllegalStateException("Could not load JDBC driver class [" + driverClassNameToUse + "]", ex);
		}
	}


	@Override
	protected Connection getConnectionFromDriver(Properties props) throws SQLException {
		String url = getUrl();
		return getConnectionFromDriverManager(url, props);
	}


	protected Connection getConnectionFromDriverManager(String url, Properties props) throws SQLException {
		return DriverManager.getConnection(url, props);
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {

	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {

	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return 0;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}
}
