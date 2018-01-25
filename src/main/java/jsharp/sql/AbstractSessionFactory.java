package jsharp.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import jsharp.support.DataMapListHandler;
import jsharp.support.DataMapPageHandler;
import jsharp.support.DataRowProcessor;
import jsharp.util.DataMap;
import jsharp.util.DataValue;

import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSessionFactory implements SessionFactory {

	protected static final Logger logger = LoggerFactory.getLogger(AbstractSessionFactory.class);
	protected JSQueryRunner queryRunner;

	public AbstractSessionFactory() {
		 
	}

	public void setDataSource(DataSource ds) {
		queryRunner = new JSQueryRunner(ds,this);
	}

	public void beginTrans() {
		try {
			queryRunner.beginTrans();
		} catch (Exception e) {
			throw JSException.wrap(e);
		}

	}

	public void commitTrans() {

		try {
			queryRunner.commitTrans();
		} catch (Exception e) {
			throw JSException.wrap(e);
		}

	}

	public void rollbakTrans() {
		queryRunner.rollbakTrans();

	}

	public void openSession() {

		try {
			queryRunner.openSession();
		} catch (Exception e) {
			throw JSException.wrap(e);
		}
	}

	public void closeSession() {

		queryRunner.closeSession();
	}

	public DataSource getDataSource() {
		return queryRunner.getDataSource();
	}

	public Connection getConnection() {
		try {
			return this.getDataSource().getConnection();
		} catch (SQLException e) {
			throw JSException.wrap(e);
		}
	}

	 
	public List<DataMap> queryList(String sql, Object... params) {

		DataMapListHandler h = new DataMapListHandler(DataRowProcessor.instance);
		return query(sql, h, params);

	}

	public <T> T query(String sql, ResultSetHandler<T> rsh, Object... params) {
		try {
			return queryRunner.query(sql, rsh, fixParams(params));
		} catch (SQLException e) {
			throw JSException.wrap(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jsharp.sql.ddddd#query(int, int, java.lang.String, java.lang.Object)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see jsharp.sql.qq#query(int, int, java.lang.String, java.lang.Object)
	 */
	public List<DataMap> queryList(int page, int size, String sql, Object... params) {

		DataMapPageHandler h = new DataMapPageHandler(page, size);
		return query(sql, h, params);

	}
	
	public List<DataMap> queryPage(int page, int size, String sql, Object... params) {

		DataMapPageHandler h = new DataMapPageHandler(page, size);
		return query(sql, h, params);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jsharp.sql.ddddd#queryOne(java.lang.String, java.lang.Object)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see jsharp.sql.qq#queryOne(java.lang.String, java.lang.Object)
	 */
	public DataMap queryOne(String sql, Object... params) {

		MapHandler h = new MapHandler(DataRowProcessor.instance);
		return (DataMap) query(sql, h, params);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jsharp.sql.ddddd#queryValue(java.lang.String, java.lang.Object)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see jsharp.sql.qq#queryValue(java.lang.String, java.lang.Object)
	 */
	public DataValue queryValue(String sql, Object... params) {

		Map<String, Object> map = queryOne(sql, params);
		if (map != null && map.values().iterator().hasNext()) {
			return new DataValue(map.values().iterator().next());
		}

		return null;

	}

	 
	public int update(String tableName, DataMap map, String where, Object... params) {
		if (map == null || map.size() == 0) return 0;
		String sql = "update " + tableName + " set ";
		String s = "";
		ArrayList<Object> arrayList = new ArrayList<Object>();
		for (Map.Entry<String, Object> e : map.entrySet()) {

			String key = e.getKey();
			Object value = e.getValue();
			if (s != "") s += ",";
			if ((value instanceof SqlValue) || map.getType(key) == DataValue.TYPE_TEXT) {
				s += key + "=" + value;
			} else {

				arrayList.add(value);
				s += key + "=?";
			}
		}

		sql += s;
		where = (where == null) ? "" : where.trim();
		if (where.toLowerCase().startsWith("where")) where = where.substring(5);
		if (where.trim().length() > 0) sql += " where " + where;
		if (params != null) {
			for (Object param : params) {
				arrayList.add(param);
			}
		}
		
		try {
			return queryRunner.update(sql, fixParams(arrayList));
		} catch (SQLException e1) {
			throw JSException.wrap(e1);
		}

	}

	public int insert(String tableName, DataMap map) {
		try {
			return (int) queryRunner.insert(tableName, map, false)[0];
		} catch (SQLException e) {
			throw JSException.wrap(e);
		}
	}

	public long[] insertAndGetKey(String tableName, DataMap map) {
		try {
			return queryRunner.insert(tableName, map, true);
		} catch (SQLException e) {
			throw JSException.wrap(e);
		}
	}

	public int deleteByWhere(String tableName, String where, Object... params) {
		String deleteSql = " delete from " + tableName;
		if (where != null && where.length() > 0) deleteSql += " where " + where;
		return this.updateBySQL(deleteSql, fixParams(params));

	}
	
	public int updateBySQL(String sql, Object... params) {
		try {
			return queryRunner.update(sql,fixParams(params));
		} catch (SQLException e) {
			throw JSException.wrap(e);
		}
	}
	
	protected Object[] fixParams(Object... params){
		if(params!=null && params.length==1 && params[0] instanceof java.util.Collection){
			Collection<Object> c=(Collection<Object>) params[0];
			return c.toArray(new Object[0]);
		}else return params;
	}

}
