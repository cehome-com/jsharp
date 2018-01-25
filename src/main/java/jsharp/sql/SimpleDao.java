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
 * ��session ����һ���ķ�װ����ָ���ı������ɾ�� ���߼򵥲�ѯ
 * 
 * SimpleDao �õ��˵�sessionΪsessionFactory.getCurrentSession()���������û��@Transactional
 * ע�⣬ ����ִ����Ϻ�����ֶ��ر�session SimpleDao.getSessionFactory().close();
 * 
 * ȱʡ���캯����ȡwebsharp�����е�һ��SessionFactory��������ڣ���
 * ��spring���������xml������SessionFactory�����ע��spring��SessionFactory����ǰ��
 * 
 * @author ma
 * 
 * @param <T>
 */
public class SimpleDao<T> extends AbstractDao<T>  {
 
	private TableDao<T> tableDao;
	
	public TableDao<T> getTableDao(){
		return tableDao;
	}
	
	/**
	 * ���ڼ̳�ʱʹ��
	 */
	protected SimpleDao() {
		super();
		tableDao =new TableDao<T>();

	}

    @Autowired
    public void setSessionFactory(final SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
        tableDao.setSessionFactory(sessionFactory);
    }


    /**
	 * ���ڼ̳�ʱʹ��
	 */
	protected SimpleDao(final SessionFactory sessionFactory) {
		super(sessionFactory);
		tableDao =new TableDao<T>(sessionFactory);
	}

	public SimpleDao(final SessionFactory sessionFactory, final Class<T> entityClass) {
		super(sessionFactory,entityClass);
		tableDao =new TableDao<T>(sessionFactory,entityClass);
	}
  
	public int delete(final T entity, String... keys) {

		return getSessionFactory().delete(null, entity,keys);
	}
 
	public int deleteById(Object id) {

		return getSessionFactory().deleteById(null, id, entityClass);
	}
	
 
	public  int deleteByProps(Object... nameAndValues){
		return getSessionFactory().deleteByProps(null,entityClass,nameAndValues);
	}
	
   public int deleteByWhere( String where, Object... params) {
			return getSessionFactory().deleteByWhere(null, entityClass,where, params);
		}
	 
	public T get(Object id) {
		return (T) getSessionFactory().get(null, id, entityClass);
	}
	
	 
	public List<T> getAll(String orderBy) {
			return  getSessionFactory().getAll(null, orderBy, entityClass);
	}
	
	
	public int insert(final T entity) {

		return getSessionFactory().insert(null, entity);
	}
	public boolean load( T entity) {
		return getSessionFactory().reload(null, entity);
	}
	public List<T> queryListByEntity(String orderBy, T entityParam) {
		return getSessionFactory().queryListByEntity(null, orderBy, entityParam);
	}
	public List<T> queryListByProps(String orderBy, Object... nameAndValues) {

		return getSessionFactory().queryListByProps(null, orderBy, entityClass, nameAndValues);
	}
	
	public T queryOne(String sql, Object... params) {
		return getSessionFactory().queryOne(sql, entityClass, params);
	}
	
	public T queryOneByProps(String orderBy, Object... nameAndValues) {

		return (T) getSessionFactory().queryOneByProps(null, orderBy, entityClass, nameAndValues);
	}
	public T queryOneByEntity(String orderBy, T entityParam) {

		return (T) getSessionFactory().queryOneByEntity(null, orderBy, entityParam);
	}
	
	public  DataValue queryValueByWhere(String field,String where,String orderBy,Object... params){
		return getSessionFactory().queryValueByWhere(null, field, where, orderBy, entityClass, params);
	}
		
	public DataValue queryValueByProps(String field, String orderBy, Object... nameAndValues) {

		return getSessionFactory().queryValueByProps(null, field, orderBy, entityClass, nameAndValues);
	}
	
	public DataValue queryValueByEntity(String field, String orderBy, T entityParam) {

		return getSessionFactory().queryValueByEntity(null, field, orderBy, entityParam);
	}
	
	public List<T> queryPageByProps(String orderBy, int page, int size, Object... nameAndValues) {
		return getSessionFactory().queryPageByProps(null, orderBy, page, size, entityClass, nameAndValues);
	}
	public List<T> queryPageByEntity(String orderBy, int page, int size, T entityParam) {
		return getSessionFactory().queryPageByEntity(null, orderBy, page, size, entityParam);
	}
	
	public boolean reload(T entity) {
		return getSessionFactory().reload(null, entity);
	}
	
	public int save(final T entity, boolean queryBeforeSave) {

		return getSessionFactory().save(null, entity, queryBeforeSave);
	}
	public int save(T entity, String... keys) {
		return getSessionFactory().save(null, entity, keys);
	}
	public int update(final T entity, String... keys) {

		return getSessionFactory().update(null, entity, keys);
	}
	public int updateByWhere(T entity, String where, Object... params) {
		return getSessionFactory().updateByWhere(null, entity, where, params);
	}
	
	
	public int updateByProps(Object entity, Object... nameAndValues) {
		return getSessionFactory().updateByProps(null, entity, nameAndValues);
	}
	 
}
