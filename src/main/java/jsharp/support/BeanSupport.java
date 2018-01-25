package jsharp.support;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jsharp.sql.Entity;
import jsharp.util.DataMap;
import jsharp.util.DataValue;
import jsharp.util.EntityUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BeanSupport {

	final static Logger logger = LoggerFactory.getLogger(BeanSupport.class);
	private static final Map primitiveDefaults = new HashMap();
	protected static final int PROPERTY_NOT_FOUND = -1;

	static
	{
		primitiveDefaults.put(Integer.TYPE, new Integer(0));
		primitiveDefaults.put(Short.TYPE, new Short((short) 0));
		primitiveDefaults.put(Byte.TYPE, new Byte((byte) 0));
		primitiveDefaults.put(Float.TYPE, new Float(0));
		primitiveDefaults.put(Double.TYPE, new Double(0));
		primitiveDefaults.put(Long.TYPE, new Long(0));
		primitiveDefaults.put(Boolean.TYPE, Boolean.FALSE);
		primitiveDefaults.put(Character.TYPE, new Character('\u0000'));
	}
	public static DataMap toMap(String table, Object entity, BeanAnn ba, boolean inserted,String[] ignoreProps) throws SQLException
	{
		java.util.Set<String> nameSet = null;
		DataMap dataMap = new DataMap();
		//logger.debug("entity to FieldList - " + entity);
		try
		{


			if(entity instanceof  Entity){
				nameSet=((Entity)entity).returnDynFNameSet();
				//if(nameSet==null)  throw new SQLException("no parameters. may be not a valid Entity");
			}else{
				EntityMethodInterceptor mi= EntityUtils.getEntityMethodInterceptor(entity);
				if(mi!=null){
					nameSet=mi.getProperties();
				}
			}

			// 用到cblib, 获取需要动态更新的字段集
			/*if (Enhancer.isEnhanced(entity.getClass()))// .getName().indexOf("$$EnhancerByCGLIB$$")>0)
			{

				EntityMethodInterceptor mi = (EntityMethodInterceptor) entity.getClass()
						.getDeclaredMethod("getCallback", new Class[] { Integer.TYPE })
						.invoke(entity, new Object[] { new Integer(0) });

				nameSet = ((EntityMethodInterceptor) mi).getProperties();
			}*/

			Entity sqlEntity = null;

			if (entity instanceof Entity && ((Entity) entity).hasSqlValue())
				sqlEntity = (Entity) entity;

			PropertyDescriptor[] props = ba.getProperties();
			// BeanAnn ba=new BeanAnn(entity.getClass());
			Map<String, ColumnAnn> columnMap = ba.getColumnMap();
			for (int i = 0; i < props.length; i++)
			{
				PropertyDescriptor prop = props[i];
				String name = props[i].getName();
				if (name.equals("class"))
					continue;

				ColumnAnn fa = columnMap.get(name);

				// id 字段忽略
				//if (fa.isIdentitied())
				//	continue;
				
				// if(ba.getIdProperty()!=null &&
				// ba.getIdProperty().getName().equals(name)) continue;

				// 忽略的字段
				// if(ba.getTransientColumnSet().contains(readMethod.getName()))
				// continue;
				if (fa.isTransient())
					continue;  
				if(ignoreProps!=null && arrayIndexOf(ignoreProps, name))continue;  
				if (inserted)
				{
					if (!fa.isInsertable())
						continue;
				}
				else
				{ 
					if ((!fa.isUpdatable()))//||(fa.isIdentitied() && ignoreId))
						continue;
				}

				// 如果是包含sql语句值则直接认为是设置为sql语句文本
				if (sqlEntity != null && sqlEntity.findSqlValue(name) != null)
				{
					dataMap.put(fa.getName(), sqlEntity.findSqlValue(name), DataValue.TYPE_TEXT);
					continue;
				}

				// 动态插入或更新，只更新变化的字段
				if (nameSet != null && !nameSet.contains(name))
					continue;

				Method readMethod = prop.getReadMethod();

				Object value = readMethod.invoke(entity);

				// String columnName=columnMap.get(readMethod.getName());
				// if(columnName==null)columnName=name;
				// 处理大对象（如果有@Lob ，返回String类型的为clob，否则为blob
				// if( ba.getLobColumnSet().contains(readMethod.getName()))
				if (fa.isLob())
				{
					if (readMethod.getReturnType() == String.class)
						dataMap.put(fa.getName(), value, DataValue.TYPE_CLOB);
					else
						dataMap.put(fa.getName(), value, DataValue.TYPE_BLOB);
				}
				else
				{
					// 部分数据库不支持java.util.Date，所以需要转换
					if (value != null && value.getClass() == java.util.Date.class)
						value = new java.sql.Timestamp(((java.util.Date) value).getTime());

					dataMap.put(fa.getName(), value);
				}

			}

			return dataMap;
		}
		catch (Exception e)
		{
			logger.error("", e);
			throw new SQLException(e);
		}

	}
	
	public static <T>T toBean(ResultSet rs, Class<T> entityClass) throws SQLException
	{
		 
		//PropertyDescriptor[] props = propertyDescriptors(entityClass);

		ResultSetMetaData rsmd = rs.getMetaData();
		Set<String> colunmNameSet = new HashSet<String>();
		for (int i = 1; i <= rsmd.getColumnCount(); i++)
		{
			colunmNameSet.add(rsmd.getColumnLabel(i).toLowerCase());
		}

		BeanAnn ba = BeanAnn.getBeanAnn(entityClass);
		return (T)createBean(rs, colunmNameSet, null,ba);
	}


	public static Object toBean(ResultSet rs, Object entity) throws SQLException
	{
		Class entityClass = entity.getClass();
		//PropertyDescriptor[] props = propertyDescriptors(entityClass);
		ResultSetMetaData rsmd = rs.getMetaData();
		Set<String> colunmNameSet = new HashSet<String>();
		for (int i = 1; i <= rsmd.getColumnCount(); i++)
		{
			colunmNameSet.add(rsmd.getColumnLabel(i).toLowerCase());
		}

		BeanAnn ba = BeanAnn.getBeanAnn(entityClass);
		return createBean(rs, colunmNameSet, entity,ba);
	}

	 /*
	 *  database==> bean
	 * */
	private static Object createBean( ResultSet rs, Set columnNameSet, Object entity, BeanAnn ba) throws SQLException
	{
		try
		{


			Object bean = entity == null?EntityUtils.create(ba.getEntityClass()):entity;
			EntityMethodInterceptor emi = getEntityMethodInterceptor(bean);

			if (emi != null) {
                emi.setEnabled(false);
            }

			PropertyDescriptor[] properties = ba.getProperties();

			Map<String, ColumnAnn> columnMap = ba.getColumnMap(); // redmethodName
																	// => column
																	// name

			for (PropertyDescriptor p : properties)
			{

				String name = p.getName();
				if (name.equals("class"))
					continue;
				ColumnAnn fa = columnMap.get(name);
				// 忽略的字段
				// if(ba.getTransientColumnSet().contains(readMethod.getName()))
				// continue;
				if (fa.isTransient())
					continue;

				// Method readMethod=p.getReadMethod();

				// 获取属性对应的字段映射
				// String columnName=columnMap.get(readMethod.getName());
				// if(columnName==null)columnName=name;
				// 如果数据库中没有此字段则忽略
				if (!columnNameSet.contains(fa.getName().toLowerCase()))
					continue;
				//
				Object value = rs.getObject(fa.getName());//todo3  session.getDialect().getObject(rs, fa.getName());

				if (value == null)
				{
					Class propType = p.getPropertyType();
					if (propType != null && propType.isPrimitive())
					{
						value = primitiveDefaults.get(propType);
					}
				}

				//1. 如果value==null ，如果是基本类型，BeanUtils.setProperty
				// 会设置缺省值，如果是类则BeanUtils.setProperty会报错
				//2.必须用EntityUtils，因为里面有个初始化convert
				if (value != null)
					EntityUtils.setProperty(bean, name, value);
				// 如果value==null， 必须如下调用才能不报错
				else
					p.getWriteMethod().invoke(bean, new Object[] { null });
				// else p.getWriteMethod().invoke(bean, value);//
				// BeanUtils.setProperty(bean, columnName, value)

			}

			if (emi != null)
				emi.setEnabled(true);

			return bean;

		}
		catch (Exception ex)
		{
			throw new SQLException(ex);
		}

	}
	
	public static EntityMethodInterceptor getEntityMethodInterceptor(Object entity) throws Exception
	{
		if (entity.getClass().getName().indexOf("$$EnhancerByCGLIB$$") > 0)
		{

			return (EntityMethodInterceptor) entity.getClass()
					.getDeclaredMethod("getCallback", new Class[] { Integer.TYPE })
					.invoke(entity, new Object[] { new Integer(0) });

		}
		return null;
	}
	
	private static PropertyDescriptor[] propertyDescriptors(Class c) throws SQLException
	{
		// Introspector caches BeanInfo classes for better performance
		BeanInfo beanInfo = null;
		try
		{
			beanInfo = Introspector.getBeanInfo(c);

		}
		catch (IntrospectionException e)
		{
			throw new SQLException("Bean introspection failed: " + e.getMessage());
		}

		return beanInfo.getPropertyDescriptors();

	}


	public static <T> T createEntity(Class<T> c)
	{
		return EntityUtils.create(c);
	}
	
	private static Field getField0(Class c, String name){
		Field[] fs=c.getDeclaredFields();
		for(Field f:fs){
			if(f.getName().equals(name)) return f;
		}
		return null;
	}
	public static Field getField(Class clasz, String name){
		Field  f=getField0(clasz,name);
		if(f!=null) return f;
		 Class<?>[] interfaces =clasz.getInterfaces();
	        for (int i = 0; i < interfaces.length; i++) {
	            Class<?> c = interfaces[i];
	            f=getField(c,name);
	    		if(f!=null) return f;
	        }
	        // Direct superclass, recursively
	        if (!clasz.isInterface()) {
	            Class<?> c = clasz.getSuperclass();
	            if(c!=null) f=getField(c,name);
	    		if(f!=null) return f;
	        }
	        return null;
		
	}

	private static boolean arrayIndexOf(String[] array, String s) {
		if (array == null || array.length == 0)
			return false;
		if (s == null) {
			for (String a : array) {
				if (a == null)
					return true;
			}
		} else {
			for (String a : array) {
				if(s.equalsIgnoreCase(a))return true;
			}
		}
		return false;
	}
	
}
