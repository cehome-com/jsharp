package jsharp.support;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

import jsharp.util.DataMap;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.RowProcessor;

public class DataRowProcessor extends BasicRowProcessor{
	
	 public static final RowProcessor instance =new DataRowProcessor();
	
	 @Override
	 public Map<String, Object> toMap(ResultSet rs) throws SQLException {
	        Map<String, Object> result = new DataMap();
	        ResultSetMetaData rsmd = rs.getMetaData();
	        int cols = rsmd.getColumnCount();

	        for (int i = 1; i <= cols; i++) {
	        	if(rsmd.getColumnType(i)==java.sql.Types.CLOB)
	        		result.put(rsmd.getColumnLabel(i), rs.getString(i));
	        	else
	        		result.put(rsmd.getColumnLabel(i), rs.getObject(i));
	        }

	        return result;
	    }

}
