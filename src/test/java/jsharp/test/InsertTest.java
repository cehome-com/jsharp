package jsharp.test;

import jsharp.spring.SpringTest;
import jsharp.support.SqlSupport;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

@ContextConfiguration(locations = { "/jsharp/test/spring.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InsertTest extends SpringTest implements InitializingBean {

	@Autowired
	DocumentDao documentDao;

	public InsertTest(){

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Connection conn=documentDao.getSessionFactory().getConnection();
		Statement stmt=conn.createStatement();
		stmt.execute("DROP table t_document if exists");
		String sql=SqlSupport.sqlCreateString("t_document", Document.class);
		//System.out.println(sql);
		conn.createStatement().execute(sql);
		stmt.close();
		conn.close();
	}



	@Test
	public void test1() throws SQLException {
		Document doc=documentDao.createObject();
		//doc.setId(1);
		doc.setTitle("title");
		doc.setContent("Hello world!");
		doc.setCreateTime(new Date());
		documentDao.insert(doc);
		Assert.assertEquals(1,doc.getId());

	}

	@Test
	public void test2() throws SQLException {
		int id=99;
		Document doc=documentDao.createObject();
		doc.setId(id);
		doc.setTitle("title");
		doc.setContent("Hello world!");
		doc.setCreateTime(new Date());
		documentDao.insert(doc);
		Assert.assertEquals(id,doc.getId());
		Assert.assertNotNull(documentDao.get(id));


	}

	@Test
	public void test3() throws SQLException {
		Document doc=new Document();
		doc.setTitle("title");
		doc.setContent("Hello world!");
		doc.setCreateTime(new Date());
		documentDao.insert(doc);
		Assert.assertEquals(1,doc.getId());

	}

	@Test
	public void test4() throws SQLException {
		int id=99;
		Document doc=new Document();
		doc.setId(id);
		doc.setTitle("title");
		doc.setContent("Hello world!");
		doc.setCreateTime(new Date());
		documentDao.insert(doc);
		Assert.assertEquals(id,doc.getId());
		Assert.assertNotNull(documentDao.get(id));
	}

	@Test
	public void test5() throws SQLException {
		System.out.println("id=0 or empty , will be ignore");
		int id=0;
		Document doc=documentDao.createObject();
		doc.setId(id);
		doc.setTitle("title");
		doc.setContent("Hello world!");
		doc.setCreateTime(new Date());
		documentDao.insert(doc);
		Assert.assertNotSame(id,doc.getId());
		Assert.assertNull(documentDao.get(id));
	}

	@Test
	public void test6() throws SQLException {
		int id=0;
		Document doc=new Document();
		doc.setId(id);
		doc.setTitle("title");
		doc.setContent("Hello world!");
		doc.setCreateTime(new Date());
		documentDao.insert(doc);
		Assert.assertEquals(1,doc.getId());
		Assert.assertNull(documentDao.get(id));
	}

}
