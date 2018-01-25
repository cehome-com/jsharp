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
 * 对session 做进一步的封装，对指定的表进行增删改 或者简单查询
 * 
 * SimpleDao 用到了的session为sessionFactory.getCurrentSession()，所以如果没有@Transactional
 * 注解， 方法执行完毕后必须手动关闭session SimpleDao.getSessionFactory().close();
 * 
 * 缺省构造函数会取websharp配置中第一个SessionFactory（如果存在），
 * 在spring环境中如果xml配置了SessionFactory，则会注入spring的SessionFactory覆盖前者
 * 
 * @author ma
 * 
 * @param <T>
 */
public class AbstractDao<T>  {

	/**
	 * 通过反射,获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class. eg. public UserDao
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
	 * 用于继承时使用
	 */
	protected AbstractDao() {

		this.entityClass = getSuperClassGenricType(getClass());
        if(entityClass!=null)
	    this.tableName= BeanAnn.getBeanAnn(entityClass).getTable();
    }

	/**
	 * 用于继承时使用
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
