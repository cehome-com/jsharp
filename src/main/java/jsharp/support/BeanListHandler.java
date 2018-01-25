package jsharp.support;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jsharp.util.DataMap;

import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.AbstractListHandler;
import org.apache.commons.dbutils.handlers.ArrayHandler;

/**
 * <code>ResultSetHandler</code> implementation that converts a
 * <code>ResultSet</code> into a <code>List</code> of <code>Map</code>s.
 * This class is thread safe.
 *
 * @see org.apache.commons.dbutils.ResultSetHandler
 */
public class BeanListHandler<T> implements ResultSetHandler<List<T>> {
	 Class<T> c;
	 public BeanListHandler(Class<T> c){
		 this.c=c;
	 }
	 public List<T> handle(ResultSet rs) throws SQLException {
	        List<T> rows = new ArrayList<T>();
	        while (rs.next()) {
	           rows.add(handleRow(rs));
	        }
	        return rows;
	 }
	 
	 protected T handleRow(ResultSet rs) throws SQLException {
		 return BeanSupport.toBean(rs, c);
	 }
    
}