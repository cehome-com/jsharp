package jsharp.test;

import jsharp.spring.SpringTest;
import jsharp.support.SqlSupport;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/jsharp/test/spring.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Test1 extends SpringTest {

	@Autowired
	DocumentDao documentDao;

	public Test1(){

	}

	@Test
	public  void test1Create() throws SQLException{
		Connection conn=documentDao.getSessionFactory().getConnection();
		Statement stmt=conn.createStatement();
		stmt.execute("DROP table t_document if exists");
		String sql=SqlSupport.sqlCreateString("t_document", Document.class);
		//Connection conn=documentDao.getSessionFactory().getConnection();
		System.out.println(sql);
		//sql=sql.replace("auto_increment","generated always as identity");
		//sql="create table t_docment0 (  id bigint PRIMARY KEY auto_increment   not null  )";
		//sql="create table t_docment (browse_count integer default 0 not null , id bigint PRIMARY KEY  auto_increment not null  ," +
		//		" systime1 datetime, title varchar(255), content varchar(255) ) \n";
		conn.createStatement().execute(sql);
		conn.commit();
		conn.close();

	}

	@Test
	public void test2Insert() throws SQLException {
		Document doc=documentDao.createObject();
		//doc.setId(1);
		doc.setTitle("title");
		doc.setContent("Hello world!");
		doc.setCreateTime(new Date());
		documentDao.insert(doc);
		System.out.println(doc.getId());
	}
	@Test
	public void test3get() throws SQLException {
		try {
			Document doc= documentDao.get(1);
			Assert.assertNotNull(doc);
			//System.out.println(doc.getId());
		}catch (Exception e){
			ExceptionUtils.getRootCause(e).printStackTrace();;

		}


	}
	//@Test
	public void test() throws Exception {


	}
}
