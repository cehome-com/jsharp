package jsharp.sql;

import java.sql.Connection;
import java.util.List;

import javax.sql.DataSource;

import jsharp.util.DataMap;
import jsharp.util.DataValue;

public interface SessionFactory {
	
	/* (non-Javadoc)
	 * @see jsharp.sql.ddddd#beginTrans()
	 */
	public abstract void beginTrans() ;

	/* (non-Javadoc)
	 * @see jsharp.sql.ddddd#closeSession()
	 */
	public abstract void closeSession();

	/* (non-Javadoc)
	 * @see jsharp.sql.ddddd#commitTrans()
	 */
	public abstract void commitTrans() ;

	public abstract <T> T createObject(Class<T> c);
	/*
	 * (non-Javadoc)
	 * 
	 * @see jsharp.sql.SessionFactory#delete(java.lang.String, java.lang.Object)
	 */
	public abstract int delete(String table, Object entity,String... keys)  ;

	public abstract int deleteById(String table,Object id,Class entityClass) ;
	public abstract int deleteByProps(String table,Class entityClass, Object... nameAndValues);

	/* (non-Javadoc)
	 * @see jsharp.sql.ddddd#delete(java.lang.String, java.lang.String, java.lang.Object)
	 */
	public abstract int deleteByWhere(String table, String where, Object... params)  ;
    public abstract int deleteByWhere(String table, Class entityClass, String where, Object... params);
	/*
	 * (non-Javadoc)
	 * 
	 * @see jsharp.sql.SessionFactory#get(java.lang.Class, java.lang.String,
	 * java.lang.Object)
	 */
	public abstract <T> T get(String table, Object id, Class<T> entityClass)  ;
	
	public abstract <T> List<T> getAll(String table, String orderBy,Class<T> entityClass)  ;
 
	/* (non-Javadoc)
	 * @see jsharp.sql.ddddd#getConnection()
	 */
	public abstract Connection getConnection()  ;

	/* (non-Javadoc)
	 * @see jsharp.sql.ddddd#getDataSource()
	 */
	public abstract DataSource getDataSource();

	/* (non-Javadoc)
	 * @see jsharp.sql.ddddd#insert(java.lang.String, java.util.Map)
	 */
	public abstract int insert(String table, DataMap  map)  ;

	/* (non-Javadoc)
	 * @see jsharp.sql.ddddd#insert(java.lang.String, java.util.Map, boolean)
	 */
	public abstract long[] insertAndGetKey(String table, DataMap  map)  ;

	/*
	 * (non-Javadoc)
	 * 
	 * @see jsharp.sql.SessionFactory#insert(java.lang.String, java.lang.Object)
	 */
	public abstract int insert(String table, Object entity)  ;

	/* (non-Javadoc)
	 * @see jsharp.sql.ddddd#openSession()
	 */
	public abstract void openSession() ;

	/* (non-Javadoc)
	 * @see jsharp.sql.ddddd#query(int, int, java.lang.String, java.lang.Object)
	 */
	public abstract List<DataMap> queryList(int page, int size, String sql, Object... params)  ;

	/*
	 * (non-Javadoc)
	 * 
	 * @see jsharp.sql.SessionFactory#query(java.lang.Class, java.lang.String,
	 * java.lang.Object)
	 */
	public abstract <T> List<T> queryList(String sql, Class<T> entityClass, Object... params)  ;

	@Deprecated 
	/**
	 * replace with queryPage
	 * @param sql
	 * @param page
	 * @param size
	 * @param entityClass
	 * @param params
	 * @return
	 */
	public abstract <T> List<T> queryList(String sql, int page, int size, Class<T> entityClass, Object... params)  ;
	
	
	/* (non-Javadoc)
	 * @see jsharp.sql.ddddd#query(java.lang.String, java.lang.Object)
	 */
	public abstract List<DataMap> queryList(String sql, Object... params)  ;

	public abstract <T> List<T> queryListByEntity(String table, String orderBy, T entityParam)  ;

	public abstract <T> List<T> queryListByProps(String table, String orderBy, Class<T> entityClass, Object... nameAndValues)  ;

	/*
	 * (non-Javadoc)
	 * 
	 * @see jsharp.sql.SessionFactory#queryOne(java.lang.Class,
	 * java.lang.String, java.lang.Object)
	 */
	public abstract <T> T queryOne(String sql, Class<T> entityClass, Object... params)  ;

	/* (non-Javadoc)
	 * @see jsharp.sql.ddddd#queryOne(java.lang.String, java.lang.Object)
	 */
	public abstract DataMap queryOne(String sql, Object... params)  ;
	public abstract <T> T queryOneByEntity(String table, String orderBy,T entity)  ;

	public abstract <T> T queryOneByProps(String table, String orderBy, Class<T> entityClass, Object... nameAndValues)  ;
	public List<DataMap> queryPage(int page, int size, String sql, Object... params);
	public abstract <T> List<T> queryPageByProps(String table, String orderBy, int page, int size, Class<T> entityClass, Object... nameAndValues)  ;
	public abstract <T> List<T> queryPageByEntity(String table, String orderBy, int page, int size, T entity)  ;
	public abstract <T> DataValue queryValue(String sql, final Class<T> entityClass,Object... params)  ;
	public abstract <T> DataValue queryValueByWhere(String table, String field,String where,String orderBy, final Class<T> entityClass,Object... params)  ;
	public abstract <T> DataValue queryValueByProps(String table, String field,String orderBy, Class<T> entityClass,Object... nameAndValues)  ;
	public abstract <T> DataValue queryValueByEntity(String table, String field,String orderBy,T entity)  ;
	public abstract DataValue queryValue(String sql, Object... params)  ;
	

	public abstract boolean reload(String table, Object entity)  ;

	/* (non-Javadoc)
	 * @see jsharp.sql.ddddd#rollbakTrans()
	 */
	public abstract void rollbakTrans();

	 
	public abstract int save(String table, Object entity,boolean queryBeforeSave)  ;
	/**
	 * first, search by keys ,then decide insert or update.
	 * @param table
	 * @param entity
	 * @param keys  entity's property names
	 * @return
	 */
	public int save(String table, Object entity, String... keys) ;

	/* (non-Javadoc)
	 * @see jsharp.sql.ddddd#setDataSource(javax.sql.DataSource)
	 */
	public abstract void setDataSource(DataSource ds);

	/* (non-Javadoc)
	 * @see jsharp.sql.ddddd#update(java.lang.String, java.util.Map, java.lang.String, java.lang.Object)
	 */
	public abstract int update(String table, DataMap map, String where, Object... params)  ;
	
	/**
	 * update by id 
	 * @param table
	 * @param entity
	 * @return
	 * @ 
	 */
	public abstract int update(String table, Object entity,String... keys)  ;
	/**
	 * update by where
	 * @param table
	 * @param entity
	 * @param where
	 * @param params
	 * @return
	 * @ 
	 */
	public abstract int updateByWhere(String table, Object entity, String where, Object... params)  ;
	public abstract int updateBySQL(String sql,Object... params)  ;
	public abstract int updateBySQL(String sql,Class entityClass,Object... params)  ;
	
	/**
	 * update by name and values
	 * @param table
	 * @param entity
	 * @param nameAndValues
	 * @return
	 * @ 
	 */
	
	public abstract int updateByProps(String table, Object entity,Object... nameAndValues)  ;

}