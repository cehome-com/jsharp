package jsharp.sql;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;

import jsharp.util.DataValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * like SimpelDao, but invoke method with an additional table name as a parameter
 * 
 * @author ma
 *
 */
public class TableDao<T> extends AbstractDao<T>  {

 
 
	/**
	 * 用于继承时使用
	 */
	protected TableDao() {
		super();
	}

	/**
	 * 用于继承时使用
	 * @param sessionFactory
	 */
	protected TableDao(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public TableDao(final SessionFactory sessionFactory, final Class<T> entityClass) {
		super(sessionFactory,entityClass);
	}

	 
	 
	public int deleteByWhere(String tableName, String where, Object... params) {
		return getSessionFactory().deleteByWhere(tableName, where, params);
	}
 
	public int delete(String tableName, final T entity, String... keys) {

		return getSessionFactory().delete(tableName, entity, keys);
	}

 
	public int deleteById(String table, Object id) {

		return getSessionFactory().deleteById(table, id, entityClass);
	}
	 
	public  int deleteByProps(String table,Object... nameAndValues){
		return getSessionFactory().deleteByProps(table,entityClass,nameAndValues);
	}

	 
	public T get(String tableName, Object id) {
		return (T) getSessionFactory().get(tableName, id, entityClass);
	}

	 
	public int insert(String tableName, final T entity) {

		return getSessionFactory().insert(tableName, entity);
	}
 
	public boolean load(String tableName, T entity) {
		return getSessionFactory().reload(tableName, entity);
	}
	
	
 
	public List<T> queryListByEntity(String table, String orderBy, T entityParam) {
		return getSessionFactory().queryListByEntity(table, orderBy, entityParam);
	}

	 
	public List<T> queryListByProps(String table, String orderBy, Object... nameAndValues) {

		return getSessionFactory().queryListByProps(table, orderBy, entityClass, nameAndValues);
	}

	 
	public T queryOneByProps(String table, String orderBy, Object... nameAndValues) {

		return (T) getSessionFactory().queryOneByProps(table, orderBy, entityClass, nameAndValues);
	}

 
	public T queryOneByEntity(String table, String orderBy, T entityParam) {

		return (T) getSessionFactory().queryOneByEntity(table, orderBy, entityParam);
	}
 
	public DataValue queryValueByProps(String table, String field, String orderBy, Object... nameAndValues) {

		return getSessionFactory().queryValueByProps(table, field, orderBy, entityClass, nameAndValues);
	}
 
	public DataValue queryValueByEntity(String table, String field, String orderBy, T entityParam) {

		return getSessionFactory().queryValueByEntity(table, field, orderBy, entityParam);
	}
 
	public List<T> queryPageByProps(String table, String orderBy, int page, int size, Object... nameAndValues) {
		return getSessionFactory().queryPageByProps(table, orderBy, page, size, entityClass, nameAndValues);
	}

	 
	public List<T> queryPageByEntity(String table, String orderBy, int page, int size, T entityParam) {
		return getSessionFactory().queryPageByEntity(table, orderBy, page, size, entityParam);
	}
 
	public int save(String tableName, final T entity, boolean queryBeforeSave) {

		return getSessionFactory().save(tableName, entity, queryBeforeSave);
	}

	 
	public int save(String table, T entity, String... keys) {
		return getSessionFactory().save(table, entity, keys);
	}
 
	public int update(String table, final T entity, String... keys) {

		return getSessionFactory().update(table, entity, keys);
	}

	 
	public int updateByWhere(String table, T entity, String where, Object... params) {
		return getSessionFactory().updateByWhere(table, entity, where, params);
	}

	 

	public int updateByProps(String table, Object entity, Object... nameAndValues) {
		return getSessionFactory().updateByProps(table, entity, nameAndValues);
	}
 
	
}
