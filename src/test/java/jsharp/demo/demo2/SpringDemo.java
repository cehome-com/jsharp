package jsharp.demo.demo2;

import java.sql.SQLException;
import java.util.List;

import jsharp.demo.Document;
import jsharp.demo.DocumentDao;
import jsharp.spring.SpringTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;


@ContextConfiguration(locations = { "/jsharp/demo/demo2/spring.xml"})
public class SpringDemo extends SpringTest {
	
	@Autowired
	DocumentDao documentDao;
	
	@Test
	public  void search() throws SQLException{
		 
		List<Document> docs=documentDao.queryList(" from  t_document where {id} >? and id<? ", 1,100);
		System.out.println(docs.size()); 
	}
}
