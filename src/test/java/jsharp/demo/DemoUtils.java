package jsharp.demo;

import javax.sql.DataSource;

import jsharp.sql.SimpleDataSource;

public class DemoUtils {

	public static DataSource getDataSource() {
		SimpleDataSource ds = new SimpleDataSource();
		ds.setDriver("com.mysql.jdbc.Driver");
		ds.setUrl("jdbc:mysql://10.218.249.98:3316/lottery_ptdj");
		ds.setUser("caipiao");
		ds.setPassword("caipiao");
		return ds;
	}
}
