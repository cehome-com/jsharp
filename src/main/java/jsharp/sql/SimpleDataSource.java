package jsharp.sql;

import java.io.PrintWriter;
import java.sql.*;
import java.util.logging.Logger;
import javax.sql.DataSource;

public class SimpleDataSource implements DataSource {

	public SimpleDataSource() {
		driver = "";
		url = "";
		password = "";
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	public void setLogWriter(PrintWriter printwriter) throws SQLException {
	}

	public void setLoginTimeout(int i) throws SQLException {
	}

	public int getLoginTimeout() throws SQLException {
		return 0;
	}

	public Object unwrap(Class iface) throws SQLException {
		return null;
	}

	public boolean isWrapperFor(Class iface) throws SQLException {
		return false;
	}

	public Connection getConnection() throws SQLException {
		try {
			Class.forName(driver);
			return DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		}
	}

	public Connection getConnection(String username, String password) throws SQLException {
		try {
			Class.forName(driver);
			return DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		}
	}

	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}

	private String driver;
	private String url;
	private String user;
	private String password;
}
