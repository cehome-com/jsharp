package jsharp.support;

import java.sql.ResultSet;
import java.sql.SQLException;
import jsharp.support.BeanSupport;
import org.apache.commons.dbutils.ResultSetHandler;

public class BeanObjectHandler implements ResultSetHandler<Object>
{
	Object c;
	public BeanObjectHandler(Object c){
		this.c=c;
	}
	
  public Object handle(ResultSet rs)  throws SQLException
  {
    return rs.next() ? BeanSupport.toBean(rs, c) : null;
  }
}