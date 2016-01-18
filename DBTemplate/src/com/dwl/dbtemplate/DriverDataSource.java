package com.dwl.dbtemplate;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.dwl.dbhelp.exception.DbHelpException;

public class DriverDataSource implements DataSource {
	private String url;
	private String username;
	private String userpass;

	private int maxConnection;
	
	public DriverDataSource(String driverClass, String url,String uname,String upass) {

		try {
			Class.forName(driverClass);
			this.url = url;		
			this.username=uname;
			this.userpass=upass;
			Connection conn = getConnection();
			this.maxConnection = conn.getMetaData().getMaxConnections();
		} catch (Exception e) {
			throw new DbHelpException("instance DricerDataSource failed", e);
		}
	}
	public Connection getConnection(){
		try {
			return DriverManager.getConnection(url, username, userpass);
		} catch (SQLException e) {
			throw new DbHelpException("could not find connection.",e);
		}
	}

	public int getLoginTimeout() {
		return DriverManager.getLoginTimeout();
	}

	public PrintWriter getLogWriter() {
		return DriverManager.getLogWriter();
	}

	public void setLoginTimeout(int timeout) {
		DriverManager.setLoginTimeout(timeout);
	}

	public void setLogWriter(PrintWriter writer) {
		DriverManager.setLogWriter(writer);
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
