package jsharp.support;

import java.sql.ResultSet;
import java.sql.SQLException;
import jsharp.support.BeanSupport;
import org.apache.commons.dbutils.ResultSetHandler;

public class BeanClassHandler<T> implements ResultSetHandler<T>
{
	Class<T> c;
	public BeanClassHandler(Class<T> c){
		this.c=c;
	}
	
  public T handle(ResultSet rs)  throws SQLException
  {
    return rs.next() ? BeanSupport.toBean(rs, c) : null;
  }
}