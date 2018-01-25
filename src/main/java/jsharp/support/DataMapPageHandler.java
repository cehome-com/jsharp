package jsharp.support;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jsharp.util.DataMap;

import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.MapListHandler;

public class DataMapPageHandler  extends DataMapListHandler {
	private int page;
	private int size;
	
	 
	
	public DataMapPageHandler(int page,int size){
		super(DataRowProcessor.instance);
		this.page=page;
		this.size=size;
	}
	
	
	 public List<DataMap> handle(ResultSet rs) throws SQLException {
		 List<DataMap>  rows = new ArrayList<DataMap>();
	        int n=1;
	        int posi=(page-1)*size;
	        if(posi==0 ||rs.absolute(posi))
	        while (rs.next()) {
	            rows.add(this.handleRow(rs));
	            if(n++==size) break;
	        }
	        return rows;
	    }

}
