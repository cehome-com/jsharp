package jsharp.sql;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Transient;

import jsharp.support.BeanSupport;
import jsharp.support.EntityMethodInterceptor;
import net.sf.cglib.proxy.Enhancer;

public class Entity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, String> m = null;

	//public Set<String> DYNAMIC_FIELDS=null;
	public static String DYN_F_NAME_SET="dynFNameSet";
	private Set<String> dynFNameSet;
	//private EntityMethodInterceptor  entityMethodInterceptor=null;
	
	public Entity(){
		//entityMethodInterceptor=EntityMethodInterceptor.getEntityMethodInterceptor(this);
		
	}

	/**
	 * 利用sql语句值（函数等）给实体类的属性赋值，如 setSqlValue("myTime","sysdate")
	 * 调用此方法后，则实体类属性本身的set方法将不起作用。
	 * 
	 * @param property
	 * @param sqlValue
	 */
	public void setSqlValue(String property, String sqlValue) {
		if (m == null) m = new HashMap<String, String>();
		m.put(property, sqlValue);
		//-- 移除实体值
		if(dynFNameSet!=null && dynFNameSet.contains(property)){
			m.remove(property);
		}
		

	}

	@Transient
	public Set<String> returnDynFNameSet() {
		return dynFNameSet;
	}

	public void replaceDynFNameSet(Set<String> dynFNameSet) {
		this.dynFNameSet = dynFNameSet;
	}


	@Transient
	public String findSqlValue(String property) {
		if (m != null) return m.get(property);
		return null;
	}

	public void clearSqlValues() {
		if (m != null) m.clear();
	}

	public boolean hasSqlValue() {

		return m != null && m.size() > 0;
	}

	public String removeSqlValue(String property) {
		if(m==null) return null;
		return  m.remove(property);
	}
	
	public Map<String, String> sqlValueMap(){
		return m;
	}

	@Transient
	public static <T> T createEntity(Class<T> c) {
		return   BeanSupport.createEntity(c);
	}
}
