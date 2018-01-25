package jsharp.support;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.*;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Id;
import javax.persistence.Transient;

import jsharp.sql.anno.Column;
import jsharp.sql.anno.ColumnUnderscore;
import jsharp.sql.anno.Table;

import org.apache.commons.beanutils.BeanUtils;

import net.sf.cglib.proxy.Enhancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

/**
 * 获取实体类的注解信息
 * 
 * @author ma
 * 
 */
/**
 * @author ma
 *
 */
public class BeanAnn
{
	private  static Logger logger= LoggerFactory.getLogger(BeanAnn.class);
	private static Map<Class,BeanAnn> beanAnns=new  HashMap<Class,BeanAnn> ();;
	
	public static BeanAnn getBeanAnn(Class entityClass)
	{
		//如果是通过cblib 代理实现的子类，需要回到父类
		if(Enhancer.isEnhanced(entityClass)) entityClass=entityClass.getSuperclass();
		BeanAnn ba=beanAnns.get(entityClass);
		if(ba==null)
		{
			synchronized(beanAnns)
			{
				ba=beanAnns.get(entityClass);
				if(ba==null)
				{
					ba=new BeanAnn(entityClass);
					beanAnns.put(entityClass, ba);
				}
			}
		}
		
		return ba;
	}
	

	private String table = null;
	private String idName=null;
	private PropertyDescriptor idProperty=null;
	private Map<String, ColumnAnn> columnMap = new HashMap<String, ColumnAnn> ();
	private java.util.Set<String> transientColumnSet = new java.util.HashSet<String> ();
	private java.util.Set<String> lobColumnSet= new java.util.HashSet<String> ();
	private PropertyDescriptor[] properties =null;
	private Map<String,PropertyDescriptor> propertyMap=new HashMap<String, PropertyDescriptor> ();
	private Class entityClass=null; 
	
	
	
	public java.util.Set<String> getLobColumnSet() {
		return lobColumnSet;
	}

	public void setLobColumnSet(java.util.Set<String> lobColumnSet) {
		this.lobColumnSet = lobColumnSet;
	}

	/**
	 * 不进行入库处理的字段
	 * @return
	 */
	public java.util.Set<String> getTransientColumnSet() {
		return transientColumnSet;
	}

	public void setTransientColumnSet(java.util.Set<String> transientColumnSet) {
		this.transientColumnSet = transientColumnSet;
	}

	/**
	 *  db field name for id
	 * @return
     */
	public String getIdName()
	{
		return idName;
	}

	/**
	 * 设置ID字段名称
	 * @param idName
	 */
	public void setIdName(String idName)
	{
		this.idName = idName;
	}



	
	 

	public PropertyDescriptor getIdProperty()
	{
		return idProperty;
	}

	public void setIdProperty(PropertyDescriptor idProperty)
	{
		this.idProperty = idProperty;
	}

	 

	
	/**
	 * 实体类的表名注解
	 * @return
	 */
	public String getTable()
	{
		return table;
	}

	public void setTable(String table)
	{
		this.table = table;
	}

	/**
	 * readMethod名跟数据库字段名映射表
	 * @return
	 */
	public Map<String, ColumnAnn> getColumnMap()
	{
		return columnMap;
	}

	public void setColumnMap(Map<String, ColumnAnn> columnMap)
	{
		this.columnMap = columnMap;
	}
	
 
	
   public void setIdValue(Object entity,Object value)throws IllegalAccessException, IllegalArgumentException,
   InvocationTargetException
   {
	   if(idProperty!=null)  BeanUtils.setProperty(entity, idProperty.getName(), value);//  IdProperty.getWriteMethod().invoke(entity, value);
   }
   
   public Object getIdValue(Object entity) 
   { try
   		{
	   		if(idProperty==null) throw new IllegalArgumentException("@Id not exists");
		   return idProperty.getReadMethod().invoke(entity);
   		}
   		catch(Exception e)
   		{
   			throw new jsharp.sql.JSException(e);
   		}
	   
   }
   
   private  <T extends Annotation> T findFieldAnnotation(Class<T> clazz, String fieldName,Class<T> annotationClass ) { 
       T annotation = null; 
       try { 
           Field field = clazz.getDeclaredField(fieldName); 
           if (field != null) { 
               annotation = field.getAnnotation(annotationClass); 
           } 
       } catch (SecurityException e) { 
           //e.printStackTrace(); 
       } catch (NoSuchFieldException e) { 
          // e.printStackTrace(); 
       } 
       return annotation; 
   } 

   /**
    * 获取字段或方法上面的指定类型的注解；如果两者都有则报错。
    * @param <T>
    * @param c
    * @param method
    * @param fieldName
    * @param annotationClass
    * @return
    */
   private  <T extends Annotation> T getMethodorFieldAnn(Class<T> c,  Method method, String fieldName,Class<T> annotationClass )
   {
	   
	   T t1 =method==null?null: method.getAnnotation(annotationClass);	
	    T t2=findFieldAnnotation(c,fieldName ,annotationClass);
	    if(t1!=null && t2!=null) throw new RuntimeException("Can not define Annotation both on filed and method!");
		return t1==null?t2:t1; 
   }
  
	public BeanAnn(Class c)
	{
		if (Enhancer.isEnhanced(c)) c=c.getSuperclass();
		this.entityClass=c;
		Table t = (Table) c.getAnnotation(Table.class);
		if (t != null && t.name()!=null && t.name().length()>0) table = t.name();
		ColumnUnderscore columnUnderscore=(ColumnUnderscore)c.getAnnotation(ColumnUnderscore.class);
		boolean columnUnderscoreSupport=(t!=null && t.columnUnderscoreSupport()) || (columnUnderscore!=null ) ;
		//System.out.println(table); 
		
		//Field[] fields=c.getDeclaredFields();
		
	 
		PropertyDescriptor[] pds= propertyDescriptors(c);
		for (PropertyDescriptor pd : pds)
		{ 
			String name=pd.getName();
			if (name.equals("class")) continue;
			Method method = pd.getReadMethod();
			Field field =BeanSupport.getField(c, name);
			if(field==null) {
				Transient trans=method.getAnnotation(Transient.class);
				if(trans==null)
				throw  new jsharp.sql.JSException("can not find field :"+name);
				else continue;
			}
			propertyMap.put(name, pd);
			//String methodName = method.getName();
			//--@Column
			//Column column1 = method.getAnnotation(Column.class);	
			//Column column2=findFieldAnnotation(c,pd.getName(),Column.class);
			//if(column1!=null && column2!=null) throw new RuntimeException("Can not define Annotation both on filed and method!");
			ColumnAnn fa=new ColumnAnn();
			columnMap.put(pd.getName(), fa);
			
			if(getMethodorFieldAnn(c,method, name  ,javax.persistence.Column.class)!=null)
			{
				throw new jsharp.sql.JSException("please use 'websharp.persistence.Column' instead of 'javax.persistence.Column' for "+c);
			}
			
			Column column=getMethodorFieldAnn(c,method, name  ,Column.class);
			if (column != null)
			{
				 
				//columnMap.put(methodName, name);
				fa.setName(column.name()==null||column.name().isEmpty()?
						(columnUnderscoreSupport?this.camelCaseToUnderscore(name): name):column.name());
				fa.setInsertable(column.insertable());
				fa.setUpdatable(column.updatable());
				fa.setNullable(column.nullable());
				fa.setLength(column.length());
				fa.setPrecision(column.precision());
				fa.setScale(column.scale());
				fa.setDefaultValue(column.defaultValue());
				fa.setColumnDefinition(column.columnDefinition());
			   

				//System.out.println(name);
			}
			else fa.setName(columnUnderscoreSupport?this.camelCaseToUnderscore(name): name);

			//-- @Id注解
			Id id=field.getAnnotation(Id.class);
			if(id==null && method!=null) id= method.getAnnotation(Id.class);
			if(id!=null)
			{
				fa.setIdentitied(true);
				setIdProperty(pd);
				setIdName(fa.getName());
				
				
			}
			
			//--@Transient  
			Transient  trans=field.getAnnotation(Transient.class);
			if(trans==null)  trans=method.getAnnotation(Transient.class);
			if(trans!=null) fa.setTransient(true);        //transientColumnSet.add(methodName);
			
			//--@Lob
			Lob lob=field.getAnnotation(Lob.class);
			if(lob==null) lob=method.getAnnotation(Lob.class);
			if(lob!=null) 
				{
					fa.setLob(true);//  this.lobColumnSet.add(methodName);
					if(pd.getPropertyType().equals(String.class)) fa.setClob(true);
					else fa.setBlob(true);
				}
			
			

		}
		
		properties=propertyMap.values().toArray(new PropertyDescriptor[0]);

		/*Method[] methods = c.getDeclaredMethods();
		for (Method method : methods)
		{
			String methodName = method.getName();
			// if(methodName.startsWith("get"))
			// methodName=methodName.substring(3);
			// else if(methodName.startsWith("is"))
			// methodName=methodName.substring(2);
			// else continue; reload

		}*/

	}
	
	//--  cameTo came_to
	private String camelCaseToUnderscore(String s){
		if(s==null)return s;
		StringBuilder sb=new StringBuilder(s.length());
		for(int i=0;i<s.length();i++){
			char c=s.charAt(i);
			if(java.lang.Character.isUpperCase(c)){
				sb.append("_").append(Character.toLowerCase(c));
			}else{
				sb.append(c);
			}
		}
		return sb.toString();
	}

	
	public PropertyDescriptor[] getProperties()
	{
		return properties;
	}
	
	public Class getEntityClass()
	{
		return entityClass;
	}
	
	
	private PropertyDescriptor[] propertyDescriptors(Class c)  
	{
		// Introspector caches BeanInfo classes for better performance
		BeanInfo beanInfo = null;
		try
		{
			beanInfo = Introspector.getBeanInfo(c);

		}
		catch (IntrospectionException e)
		{
			throw new RuntimeException("Bean introspection failed: " + e.getMessage());
		}

		return beanInfo.getPropertyDescriptors();

	}
	 
	public String getColumnNameByProperty(String name)
	{
		ColumnAnn fa=columnMap.get(name);
		if(fa!=null) return fa.getName();
		logger.warn("no column mapping found for "+name);
		return name;
		 
		
	} 

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		// TODO Auto-generated method stub

		//BeanAnn ba = new BeanAnn(Bean.class);
		
		/*BeanInfo beanInfo = Introspector.getBeanInfo(Bean.class);
		
		PropertyDescriptor  p=beanInfo.getPropertyDescriptors()[0];
		System.out.println( p.getName());
		*/
	}

}
