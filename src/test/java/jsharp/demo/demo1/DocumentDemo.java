package jsharp.demo.demo1;


import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import jsharp.demo.DemoUtils;
import jsharp.demo.Document;
import jsharp.demo.DocumentDao;
import jsharp.sql.ObjectSessionFactory;
import jsharp.sql.SessionFactory;

import org.junit.Test;

public class DocumentDemo {

	static DataSource ds=DemoUtils.getDataSource();
	static SessionFactory factory=new ObjectSessionFactory() ; 
	static DocumentDao dao=new DocumentDao();
	static{
		  factory.setDataSource(ds);
		  dao.setSessionFactory(factory);
        dao.getTableDao().setSessionFactory(factory);
	}

    @Test
    public void create() throws SQLException {

        factory.updateBySQL("create table t_document(id bigint  NOT NULL AUTO_INCREMENT,title varchar(100),content text, sys_time datetime, PRIMARY KEY(id)) ");

    }


    @Test
    public void del() throws SQLException {

       // dao.getSessionFactory().updateBySQL("delete from lottery_issue where issue_id=?",1554804797);
        //dao.updateBySQL(
       System.out.println( dao.getTableDao().deleteByWhere("lottery_issue","issue_id=?",1554804797));

    }


    /**
	 * insert data
	 * @throws SQLException
	 */
	@Test
	public void insert() throws SQLException {
		Document doc=new Document();
		doc.setId(1);
		doc.setTitle("title");
		doc.setContent("Hello world!");
		doc.setSystime(new Date());
		dao.insert(doc); 
	}
	
	/**
	 * update where id=1
	 * @throws SQLException
	 */
	@Test
	public void update() throws SQLException {
		Document doc=new Document();
		doc.setId(1);
		doc.setTitle("title");
		doc.setContent("update Hello world!");
		doc.setSystime(new Date());
		dao.update(doc); 
	}
	
	@Test
	public void updateBySQL() throws SQLException {
		Document doc=new Document();
		doc.setTitle("title");
		doc.setContent("update Hello world2!");
		doc.setSystime(new Date());
		dao.updateByWhere(doc,"id=? ", 1); 
	}
	
	/**
	 * insert or update  
	 * @throws SQLException
	 */
	@Test
	public void save() throws SQLException {
		Document doc=new Document();
		doc.setId(1);
		doc.setTitle("title");
		doc.setContent("save Hello world!");
		doc.setSystime(new Date());
		dao.save(doc,true);
		
	}
	
	@Test
	public void saveWithAutoId() throws SQLException {
		Document doc=new Document();
		doc.setTitle("title");
		doc.setContent("Hello world!");
		doc.setSystime(new Date());
		dao.save(doc,true); 
		System.out.println(doc.getId());
	}
	
	@Test
	public void partSave() throws SQLException {
		Document doc= dao.createObject();//关键是这里，不能用new
		doc.setId(1);
		doc.setTitle("title");
		dao.save(doc,true);// 实际只更新了title字段
	}
	
	
	@Test
	public void search1() throws SQLException {
		List<Document> docs=dao.queryListByProps(null, "id",10,"tilte","Hello");
	}
	
	@Test
	public void search2() throws SQLException {
		
		Document param= dao.createObject();//必须是动态创建
		param.setId(10);
		param.setTitle("Hello");
		List<Document> docs=dao.queryListByEntity(null, param);
	}
	
	@Test
	public void search3() throws SQLException {
	 
		List<Document> docs=dao.queryList(" from  t_document where {id} >? and id<? ", 1,100);
		System.out.println(docs.get(0).getContent());
	}

}
