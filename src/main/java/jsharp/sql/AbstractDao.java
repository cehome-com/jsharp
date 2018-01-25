package jsharp.sql;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;

import jsharp.support.BeanAnn;
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
public class AbstractDao<T>  {

	/**
	 * ͨ������,���Class�����������ĸ���ķ��Ͳ���������. ���޷��ҵ�, ����Object.class. eg. public UserDao
	 * extends HibernateDao<User>
	 * 
	 * @param clazz
	 *            The class to introspect
	 * @return the first generic declaration, or Object.class if cannot be
	 *         determined
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getSuperClassGenricType(final Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	public static Class getSuperClassGenricType(final Class clazz, final int index) {
		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			// logger.warn(clazz.getSimpleName() +
			// "'s superclass not ParameterizedType");
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			// logger.warn("Index: " + index + ", Size of " +
			// clazz.getSimpleName() + "'s Parameterized Type: " +
			// params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			// logger.warn(clazz.getSimpleName() +
			// " not set the actual class on superclass generic parameter");
			return Object.class;
		}

		return (Class) params[index];
	}

	protected SessionFactory sessionFactory;

	protected Class<T> entityClass;
    protected String tableName;

	final Logger logger = LoggerFactory.getLogger(getClass());
 

	/**
	 * ���ڼ̳�ʱʹ��
	 */
	protected AbstractDao() {

		this.entityClass = getSuperClassGenricType(getClass());
        if(entityClass!=null)
	    this.tableName= BeanAnn.getBeanAnn(entityClass).getTable();
    }

	/**
	 * ���ڼ̳�ʱʹ��
	 */
	protected AbstractDao(final SessionFactory sessionFactory) {
		this.entityClass = getSuperClassGenricType(getClass());
		this.sessionFactory = sessionFactory;
	}

	public AbstractDao(final SessionFactory sessionFactory, final Class<T> entityClass) {
		this.sessionFactory = sessionFactory;
		this.entityClass = entityClass;
	}

	 
	public T createObject() {
		return this.getSessionFactory().createObject(entityClass);
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	@Autowired
	public void setSessionFactory(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public List<T> queryList(String sql, Object... params) {
		return getSessionFactory().queryList(sql, entityClass, params);
	}
	public DataValue queryValue(String sql, Object... params) {
		return getSessionFactory().queryValue(sql, entityClass, params);
	}
	public List<T> queryPage(String sql, int page, int size, Object... params) {
		return getSessionFactory().queryList(sql, page, size, entityClass, params);
	}
	public int updateBySQL(String sql, Object... params) {
		return getSessionFactory().updateBySQL(sql, entityClass, params);
	}

    public String getTableName() {
        return tableName;
    }
}
