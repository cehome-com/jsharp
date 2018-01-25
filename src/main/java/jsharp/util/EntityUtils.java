package jsharp.util;

import java.util.*;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;

import jsharp.sql.Entity;
import jsharp.support.EntityMethodInterceptor;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.CalendarConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.DateTimeConverter;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.beanutils.converters.SqlTimeConverter;
import org.apache.commons.beanutils.converters.SqlTimestampConverter;
//import org.springframework.beans.propertyeditors.CustomDateEditor;
//import org.springframework.validation.BindException;
//import org.springframework.web.bind.ServletRequestDataBinder;




import net.sf.cglib.proxy.Enhancer;

public class EntityUtils
{

	static
	{

		registerDateConverter();
	}

	/**
	 * @param args
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public static void main(String[] args) throws IllegalAccessException, InvocationTargetException
	{
		//EntityUtils e=new EntityUtils();
		//BeanUtils.setProperty(e, "a", 3);
		String s = "";
		String st = "";
		ArrayList list1 = new ArrayList();
		list1.add(s);
		list1.add(st);
		list1.add(s);
		System.out.println(list1.getClass());
		ArrayList list2 = (ArrayList) create(ArrayList.class);
		ArrayList list3 = (ArrayList) create(ArrayList.class);
		System.out.println(list2.getClass());
		System.out.println(list2.getClass().getSuperclass());
		System.out.println(list1.getClass() == list2.getClass().getSuperclass());
		System.out.println(list3.getClass() == list2.getClass());

		System.out.println(list1.hashCode() + " " + list2.hashCode());

		System.out.println(Enhancer.isEnhanced(list1.getClass()) + " " + Enhancer.isEnhanced(list2.getClass()) + " "
				+ Enhancer.isEnhanced(list3.getClass()) + " ");

	}

	private static void registerDateConverter()
	{
		//如果不配置，缺省是要抛异常的
		boolean throwE =!( "0".equals(DefaultConfig.getStr(DefaultConfig.WS_CONVERTER_EXCEPTION_THROW))
				|| "false".equals(DefaultConfig.getStr(DefaultConfig.WS_CONVERTER_EXCEPTION_THROW)));
		throwE=false;
		String[] dateTimeFormats = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "HH:mm:ss","yyyy.MM.dd","yyyy/MM/dd","yyyy-MM-dd HH:mm:ss.SSS"};
		String formats = DefaultConfig.getStr(DefaultConfig.WS_CONVERTER_DATETIME_DEFAULT).trim();
		if (formats.length() > 0)
			dateTimeFormats = formats.split(",");

		formats = DefaultConfig.getStr(DefaultConfig.WS_CONVERTER_DATETIME_DATE).trim();
		String[] dateFormats = formats.length() > 0 ? formats.split(",") : dateTimeFormats;

		formats = DefaultConfig.getStr(DefaultConfig.WS_CONVERTER_DATETIME_TIME).trim();
		String[] timeFormats = formats.length() > 0 ? formats.split(",") : dateTimeFormats;

		formats = DefaultConfig.getStr(DefaultConfig.WS_CONVERTER_DATETIME_TIMESTAMP).trim();
		String[] timestampFormats = formats.length() > 0 ? formats.split(",") : dateTimeFormats;

		DateTimeConverter dc = null;

		dc = throwE ? new CalendarConverter() : new CalendarConverter(null); // 转换错误则缺省为null
		dc.setUseLocaleFormat(true);
		dc.setPatterns(dateTimeFormats);
		ConvertUtils.register(dc, java.util.Calendar.class);

		dc = throwE ? new DateConverter() : new DateConverter(null); // 转换错误则缺省为null
		dc.setUseLocaleFormat(true);
		dc.setPatterns(dateFormats);
		ConvertUtils.register(dc, java.util.Date.class);

		dc = throwE ? new SqlDateConverter() : new SqlDateConverter(null); // 转换错误则缺省为null
		dc.setUseLocaleFormat(true);
		dc.setPatterns(dateFormats);
		ConvertUtils.register(dc, java.sql.Date.class);

		dc = throwE ? new SqlTimeConverter() : new SqlTimeConverter(null);
		dc.setUseLocaleFormat(true);
		dc.setPatterns(timeFormats);
		ConvertUtils.register(dc, java.sql.Time.class);

		dc = throwE ? new SqlTimestampConverter() : new SqlTimestampConverter(null);
		dc.setUseLocaleFormat(true);
		dc.setPatterns(timestampFormats);
		ConvertUtils.register(dc, java.sql.Timestamp.class);

	}

	public static boolean isCglib(Class clazz){
		return Enhancer.isEnhanced(clazz);
	}

	public static boolean hasDynProperty(Object entity,String property)   {
		EntityMethodInterceptor emi= null;
		try {
			emi = getEntityMethodInterceptor (entity);
			return (emi!=null && emi.getProperties().contains(property));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public static EntityMethodInterceptor getEntityMethodInterceptor(Object entity) throws Exception
	{
		//if (entity.getClass().getName().indexOf("$$EnhancerByCGLIB$$") > 0)
		if (isCglib(entity.getClass()))
		{

			return (EntityMethodInterceptor) entity.getClass()
					.getDeclaredMethod("getCallback", new Class[] { Integer.TYPE })
					.invoke(entity, new Object[] { new Integer(0) });

		}
		return null;
	}

	public static Object clone(Object entity)
	{
		try
		{
			Enhancer e = new Enhancer();
			e.setSuperclass(entity.getClass());
			EntityMethodInterceptor emi = new EntityMethodInterceptor();
			emi.setEnabled(false);
			e.setCallback(emi);
			Object dest = e.create();
			if(dest instanceof Entity){
				((Entity)dest).replaceDynFNameSet(emi.getProperties());
			}

			// org.apache.commons.beanutils.PropertyUtils.copyProperties(a2,a1);
			// 不同属性类型报错
			// org.apache.commons.beanutils.BeanUtils.copyProperties(a2,a1);
			// 智能装换类型
			org.apache.commons.beanutils.PropertyUtils.copyProperties(dest, entity);
			emi.setEnabled(true);
			return dest;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}

	}

    public static void copyExclude(Object from, Object to,String... excludeProperties)
    {
        copyExclude(from,to,true,excludeProperties);
    }
	/**
	 * 
	 * @param from
	 *            source
	 * @param to
	 *            dest
	 * @param excludeProperties
	 *            排除的属性名称
	 */
	public static void copyExclude(Object from, Object to, boolean useFromProperties,String... excludeProperties)
	{
        Object po=useFromProperties?from:to;
		Set<String> set = Convert.toSet(excludeProperties);
		set.add("class");
		if (Enhancer.isEnhanced(po.getClass()))
		{	set.add("callback");set.add("callbacks");}
		PropertyDescriptor[] ps = PropertyUtils.getPropertyDescriptors(po);
		try
		{
			for (PropertyDescriptor p : ps)
			{
				String name = p.getName();
				if (!set.contains(name))
				{
					Object value=PropertyUtils.getProperty(from,name);
					if(value==null)
						BeanUtils.setProperty(	to, name,null);
					else
					//--BeanUtils 会自动类型转换，PropertyUtils不会
					BeanUtils.setProperty(to, name, value);
				}
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @param o1
	 * @param o2
	 * @param includeProperties
	 *            包含的属性名称
	 */
	public static void copyInclude(Object o1, Object o2, String... includeProperties)
	{
		Set<String> set = Convert.toSet(includeProperties);
		PropertyDescriptor[] ps = PropertyUtils.getPropertyDescriptors(o1);
		try
		{ 
			for (PropertyDescriptor p : ps)
			{
				String name = p.getName();
				if (set.contains(name))
				{
					 
					//BeanUtils.setProperty(o2, name, BeanUtils.getProperty(o2, name));
					
					Object value=PropertyUtils.getProperty(o1,name);
					if(value==null)
						BeanUtils.setProperty(	o2, name,null);
					else
					//--BeanUtils 会自动类型转换，PropertyUtils不会
						BeanUtils.setProperty(o2, name, value);
				}
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * 忽略错误
	 * 
	 * @param <T>
	 * @param c
	 * @param properties
	 * @return
	 */
	public static <T> T create(Class<T> c, Map properties)
	{
		T o = create(c);
		try
		{

			BeanUtils.populate(o, properties);

		}
		catch (Exception e)
		{
			// e.printStackTrace();
			throw new RuntimeException(e);
		}
		return o;

	}
	
	public static <T> T create(Class<T> c, ServletRequest request)
	{
		return create(c,request.getParameterMap());
	}
	
	/**
	 * 创建实体并且只仅把includeParams包含的request属性值赋给实体。
	 * @param c
	 * @param request
	 * @param includeProperties 如：name ,  *name表示非empty才包含
	 * @return
	 */
	public static <T> T createInclude(Class<T> c, ServletRequest request,String... includeProperties)
	{
		T o = create(c);
		try
		{

			for (String name : includeProperties)
			{
				boolean doEmpty=true;
				if(name.startsWith("*")){
					name=name.substring(1);
					doEmpty=false;
				}
				if(request.getParameter(name)!=null){
					if(doEmpty || request.getParameter(name).length()>0 )
					BeanUtils.setProperty(o, name, request.getParameter(name));
				}
			}

		}
		catch (Exception e)
		{
			// e.printStackTrace();
			throw new RuntimeException(e);
		}
		return o;
	}
	
	/**
	 * 创建实体并且把request属性值赋予实体，但excludeParams除外。
	 * @param c
	 * @param request
	 * @param excludeProperties 如 name   *name 表示仅empty时候才排除
	 * @return
	 */
	public static <T> T createExclude(Class<T> c, ServletRequest request,String... excludeProperties)
	{
		T o = create(c);
		try
		{
			Set<String> set = Convert.toSet(excludeProperties);
			Enumeration<String> names= request.getParameterNames();
			while(names.hasMoreElements())
			{
				String name=names.nextElement();
				if(set.contains(name)) continue;
				if(set.contains("*"+name)&&request.getParameter(name).length()==0) continue;
				BeanUtils.setProperty(o, name, request.getParameter(name));
			}
			 

		}
		catch (Exception e)
		{
			 
			throw new RuntimeException(e);
		}
		return o;
	}





	public static <T> T create(Class<T> c)
	{
		Enhancer e = new Enhancer();
		e.setSuperclass(c);
		EntityMethodInterceptor emi=new EntityMethodInterceptor();
		e.setCallback(emi);
		Object o = e.create();
		if(o instanceof Entity){
			Entity entity=((Entity)o);
			entity.replaceDynFNameSet(emi.getProperties());
		}

		return (T) o;
	}
	
	public static void setProperty(Object bean, String name, Object value)
	        throws IllegalAccessException, InvocationTargetException {

		 BeanUtils.setProperty(bean, name, value);
	    }
	
 

}
