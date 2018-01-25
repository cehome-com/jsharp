package jsharp.test;

import javax.sql.DataSource;

import jsharp.sql.SimpleDataSource;
import org.junit.Test;

import java.sql.SQLException;

public class DemoUtils {

	static {
        try {
			//Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static DataSource getDataSource() {
		SimpleDataSource ds = new SimpleDataSource();
		ds.setDriver("org.apache.derby.jdbc.EmbeddedDriver");
		ds.setUrl("jdbc:derby:memory:myDB;create=true");
		//ds.setUser("caipiao");
		//ds.setPassword("caipiao");
		return ds;
	}

	@Test
	public void test() throws SQLException {
		System.out.println(getDataSource().getConnection().getMetaData().getURL());
	}
}
