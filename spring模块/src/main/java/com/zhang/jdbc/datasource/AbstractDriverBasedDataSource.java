package com.zhang.jdbc.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;


import javax.sql.DataSource;


public abstract class AbstractDriverBasedDataSource implements DataSource {


	private String url;

	private String username;

	private String password;


	private String catalog;

	private String schema;

	private Properties connectionProperties;


	public void setUrl( String url) {
		this.url = (url != null ? url.trim() : null);
	}


	public String getUrl() {
		return this.url;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getUsername() {
		return this.username;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getPassword() {
		return this.password;
	}


	public void setCatalog( String catalog) {
		this.catalog = catalog;
	}


	public String getCatalog() {
		return this.catalog;
	}

	public void setSchema( String schema) {
		this.schema = schema;
	}


	public String getSchema() {
		return this.schema;
	}


	public void setConnectionProperties( Properties connectionProperties) {
		this.connectionProperties = connectionProperties;
	}


	public Properties getConnectionProperties() {
		return this.connectionProperties;
	}



	@Override
	public Connection getConnection() throws SQLException {
		return getConnectionFromDriver(getUsername(), getPassword());
	}


	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return getConnectionFromDriver(username, password);
	}



	protected Connection getConnectionFromDriver(String username, String password) throws SQLException {
		Properties mergedProps = new Properties();
		Properties connProps = getConnectionProperties();
		if (connProps != null) {
			mergedProps.putAll(connProps);
		}
		if (username != null) {
			mergedProps.setProperty("user", username);
		}
		if (password != null) {
			mergedProps.setProperty("password", password);
		}

		Connection con = getConnectionFromDriver(mergedProps);
		if (this.catalog != null) {
			con.setCatalog(this.catalog);
		}
		if (this.schema != null) {
			con.setSchema(this.schema);
		}
		return con;
	}

	protected abstract Connection getConnectionFromDriver(Properties props) throws SQLException;

}
