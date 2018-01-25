package jsharp.support;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class BeanPageHandler<T> extends BeanListHandler<T> {

	private int page;
	private int size;
	public BeanPageHandler(int page,int size,Class<T> c) {
		super(c);
		this.page=page;
		this.size=size;
	}

	 public List<T> handle(ResultSet rs) throws SQLException {
		 List<T>  rows = new ArrayList<T>();
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
