package jsharp.sql;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.*;

import jsharp.util.EntityUtils;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.dbutils.ResultSetHandler;

import jsharp.sql.id.IdGeneration;
import jsharp.sql.id.SequenceIdGeneration;
import jsharp.support.BeanAnn;
import jsharp.support.BeanClassHandler;
import jsharp.support.BeanListHandler;
import jsharp.support.BeanObjectHandler;
import jsharp.support.BeanPageHandler;
import jsharp.support.BeanSupport;
import jsharp.support.ColumnAnn;
import jsharp.support.EntityMethodInterceptor;
import jsharp.support.SqlSupport;
import jsharp.util.DataMap;
import jsharp.util.DataValue;

public class ObjectSessionFactory extends AbstractSessionFactory {

	private static int indexOfWhitespace(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) <= 32) return i;
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jsharp.sql.SessionFactory#delete(java.lang.String, java.lang.Object)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see jsharp.sql.q#delete(java.lang.String, java.lang.Object)
	 */
	public int delete(String table, Object entity,String... keys) {

		BeanAnn ba = BeanAnn.getBeanAnn(entity.getClass());
		if (table == null) table = ba.getTable();
		if(keys==null|| keys.length==0){
			return deleteByWhere(table,ba.getIdName() + " = ? ", ba.getIdValue(entity)); 
		}else{
			Object[] nameAndValues =getNameAndValues(entity,keys);  
			Object[] wheres= getWhereCondition(ba,nameAndValues);
			return deleteByWhere(table, (String)wheres[0], (Object[])wheres[1]);
		}

	}
	
	public int deleteByProps(String table,Class entityClass, Object... nameAndValues){
		BeanAnn ba = BeanAnn.getBeanAnn(entityClass);
		if (table == null) {
            table = ba.getTable();
        }
		 Object[] wheres= getWhereCondition(ba,nameAndValues);
		 return deleteByWhere(table, (String)wheres[0], (Object[])wheres[1]);
		
	}

	public int deleteById(String table, Object id, Class entityClass) {
		BeanAnn ba = BeanAnn.getBeanAnn(entityClass);
		if (table == null) table = ba.getTable();
		return deleteByWhere(table, ba.getIdName() + " = ? ", id);

	}

    public int deleteByWhere(String table, Class entityClass, String where, Object... params) {
        BeanAnn ba = BeanAnn.getBeanAnn(entityClass);
        if (table == null){
            table = ba.getTable();
        }
        where=SqlSupport.replacePropsWithColumns(entityClass, where);
        return deleteByWhere(table,where,params);

    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see jsharp.sql.q#find(java.lang.String, java.lang.String,
	 * java.lang.Class, java.lang.Object)
	 */
	public <T> List queryListByProps(String table, String orderBy, Class<T> entityClass, Object... nameAndValues)

	{

		List params = new ArrayList();
		String sql = buildSqlByProps(table, null, orderBy, entityClass, params, nameAndValues);
		return queryList(sql, entityClass, params.toArray());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jsharp.sql.q#find(java.lang.String, java.lang.String, int, int,
	 * java.lang.Class, java.lang.Object)
	 */
	public <T> List queryPageByProps(String table, String orderBy, int page, int size, Class<T> entityClass, Object... nameAndValues) {
		List params = new ArrayList();
		String sql = buildSqlByProps(table, null, orderBy, entityClass, params, nameAndValues);
		return queryList(page, size, sql, entityClass, params.toArray());
	}

	public <T> List<T> queryPageByEntity(String table, String orderBy, int page, int size, T entity) {
		List params = parseParams(table, entity);
		return queryPageByProps(table, orderBy, page, size, (Class<T>) entity.getClass(), params.toArray());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jsharp.sql.q#find(java.lang.String, java.lang.String, T)
	 */
	public <T> List<T> queryListByEntity(String table, String orderBy, T entity) {

		List params = parseParams(table, entity);
		return queryListByProps(table, orderBy, (Class<T>) entity.getClass(), params.toArray());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jsharp.sql.q#findOne(java.lang.String, java.lang.String,
	 * java.lang.Class, java.lang.Object)
	 */
	public <T> T queryOneByProps(String table, String orderBy, Class<T> entityClass, Object... nameAndValues)

	{

		List params = new ArrayList();
		String sql = buildSqlByProps(table, null, orderBy, entityClass, params, nameAndValues);
		return queryOne(sql, entityClass, params.toArray());
	}

	public <T> T queryOneByEntity(String table, String orderBy, T entity) {
		List params = parseParams(table, entity);
		return queryOneByProps(table, orderBy, (Class<T>) entity.getClass(), params.toArray());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jsharp.sql.SessionFactory#get(java.lang.Class, java.lang.String,
	 * java.lang.Object)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see jsharp.sql.q#get(java.lang.String, java.lang.Object,
	 * java.lang.Class)
	 */
	public <T> T get(String table, Object id, Class<T> entityClass) {

		BeanAnn ba = BeanAnn.getBeanAnn(entityClass);
		if (table == null) table = ba.getTable();

		String sql = "select * from " + table + " where " + ba.getIdName() + " = ? ";

		return queryOne(sql, entityClass, id);
	}

	protected GenerationType getDefaultGenerationType() {

		return null;
	}

	public int insert(String table, Object entity) {
		BeanAnn ba = BeanAnn.getBeanAnn(entity.getClass());
		boolean empty= isEmptyId(entity);
		String[] ignoreProps = empty?new String[]{ba.getIdProperty().getName()}:null;
		return  insert(table,entity,empty,ignoreProps);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see jsharp.sql.SessionFactory#insert(java.lang.String, java.lang.Object)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see jsharp.sql.q#insert(java.lang.String, java.lang.Object)
	 */
	private int insert(String table, Object entity, boolean useIdGen,String[] ignoreProps) {

		try {
			BeanAnn ba = BeanAnn.getBeanAnn(entity.getClass());
			if (table == null) table = ba.getTable();

			DataMap list = BeanSupport.toMap(table, entity, ba, true,ignoreProps);
			// gv.strategy()

            Object idValue = null;
            if(useIdGen) {
                PropertyDescriptor pd = ba.getIdProperty();
                if (pd != null) {
                    Field field = BeanSupport.getField(entity.getClass(), pd.getName());
                    GeneratedValue gv = field.getAnnotation(GeneratedValue.class);
                    if (gv == null) gv = pd.getReadMethod().getAnnotation(GeneratedValue.class);
                    if (gv != null) {
                        String idColumnName = ba.getIdName();
                        // idColumnNames = new String[] { idColumnName };
                        // �Զ�����ʵ�� . gv.strategy()û��=null��ȱʡΪAUTO
                        if (gv.strategy() == GenerationType.AUTO && gv.generator() != null && gv.generator().startsWith("class:")) {
                            // if (gv.generator() != null && gv.generator().length()
                            // > 0)
                            {
                                IdGeneration idGen = (IdGeneration) Class.forName(gv.generator().substring(6)).newInstance();
                                idValue = idGen.gen(this, table, null, null);
                                list.put(idColumnName, idValue);
                            }
                        } else
                        // ����ʵ��
                        {

                            GenerationType gt = gv.strategy() == GenerationType.AUTO ? this.getDefaultGenerationType() : gv.strategy();
                            if (gt == GenerationType.IDENTITY) {

                            } else if (gt == GenerationType.SEQUENCE) {
                                String genName = gv.generator();
                                idValue = new SequenceIdGeneration().gen(this, table, genName, null);
                                list.put(idColumnName, idValue);
                            }

                        }
                    }
                }
            }

			if (list.size() == 0) throw new JSException("No fields  to insert or update.");
			long[] res = insertAndGetKey(table, list);
			if (res[0] > 0) {
				if (res[1] > 0)
					ba.setIdValue(entity, res[1]);
				else if (idValue != null) ba.setIdValue(entity, idValue);
			}
			return (int) res[0];

		} catch (Exception e) {
			throw JSException.wrap(e);
		} finally {

		}

	}

	protected String buildSqlByProps(String table, String fields, String orderBy, Class entityClass, List resultParams, Object... params) {
		BeanAnn ba = BeanAnn.getBeanAnn(entityClass);
		if (table == null) table = ba.getTable();
		String where = "";
		if (params != null && params.length > 0) {

			for (int i = 0; i < params.length; i += 2) {
				if (where.length() > 0) where += " and ";
				where += ba.getColumnNameByProperty((String) params[i]) + " = ? ";
				resultParams.add(params[i + 1]);
			}
		}

		String sql = "select " + (fields == null ? "*" : fields) + " from " + table; // +
																						// " where "
																						// +
		// ba.getIdName() +
		// " = ? ";
		if (where.length() > 0) sql += " where " + where;

		String order = "";
		if (orderBy != null && orderBy.length() > 0) {
			String[] orders = orderBy.split(",");
			for (String s : orders) {
				s = s.trim();
				if (order.length() > 0) order += " , ";
				int n = indexOfWhitespace(s);
				if (n == -1)
					order += ba.getColumnNameByProperty(s) + " ";
				else
					order += ba.getColumnNameByProperty(s.substring(0, n)) + s.substring(n);

			}

		}
		if (order.length() > 0) sql += " order by " + order;

		return sql;
	}

	/**
	 * params from  dyn entity
	 * @param table
	 * @param entity
	 * @return
	 */
	private List parseParams(String table, Object entity) {

		BeanAnn ba = BeanAnn.getBeanAnn(entity.getClass());
		new ArrayList<Object>();

		try {
			Set<String> nameSet =null;
			if(entity instanceof  Entity){
				nameSet=((Entity)entity).returnDynFNameSet();
				if(nameSet==null)  throw new SQLException("no parameters. maybe not a valid Entity");
			}else{
				EntityMethodInterceptor mi= EntityUtils.getEntityMethodInterceptor(entity);
				if(mi!=null){
					nameSet=mi.getProperties();
				}else{
					throw new SQLException("entity (query parameters) must be cglib object");
				}
			}

			//if (!Enhancer.isEnhanced(entity.getClass())) throw new SQLException("entity (query parameters) must be cglib object");
			//EntityMethodInterceptor mi = (EntityMethodInterceptor) entity.getClass().getDeclaredMethod("getCallback", new Class[] { Integer.TYPE }).invoke(entity, new Object[] { new Integer(0) });

			//Set<String> nameSet = ((EntityMethodInterceptor) mi).getProperties();
			List params = new ArrayList();
			Map<String, ColumnAnn> columnMap = ba.getColumnMap();
			for (String name : nameSet) {
				columnMap.get(name);
				params.add(name);
				params.add(PropertyUtils.getProperty(entity, name));
			}
			return params;

		} catch (Exception e) {
			throw JSException.wrap(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jsharp.sql.SessionFactory#query(java.lang.Class, java.lang.String,
	 * java.lang.Object)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see jsharp.sql.q#query(java.lang.String, java.lang.Class,
	 * java.lang.Object)
	 */
	public <T> List<T> queryList(String sql, Class<T> entityClass, Object... params) {

		BeanListHandler<T> h = new BeanListHandler<T>(entityClass);
		return queryEntities(sql, h, entityClass, params);

	}

	protected <T> T queryEntities(String sql, ResultSetHandler<T> rsh, Class<?> entityClass, Object... params) {

		sql = SqlSupport.fixSql(SqlSupport.replacePropsWithColumns(entityClass, sql), entityClass);
		return query(sql, rsh, params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jsharp.sql.q#query(java.lang.String, int, int, java.lang.Class,
	 * java.lang.Object)
	 */
	public <T> List<T> queryList(String sql, int page, int size, Class<T> entityClass, Object... params) {

		BeanPageHandler<T> h = new BeanPageHandler<T>(page, size, entityClass);
		return queryEntities(sql, h, entityClass, params);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jsharp.sql.SessionFactory#queryOne(java.lang.Class,
	 * java.lang.String, java.lang.Object)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see jsharp.sql.q#queryOne(java.lang.String, java.lang.Class,
	 * java.lang.Object)
	 */
	public <T> T queryOne(String sql, final Class<T> entityClass, Object... params) {
		sql = SqlSupport.fixSql(SqlSupport.replacePropsWithColumns(entityClass, sql), entityClass);

		return (T) query(sql, new BeanClassHandler(entityClass) , params);

	}

	public <T> DataValue queryValue(String sql, final Class<T> entityClass, Object... params) {
		sql = SqlSupport.fixSql(SqlSupport.replacePropsWithColumns(entityClass, sql), entityClass);
		DataValue dv = queryValue(sql, params);
		return dv;
	}
	
	public  <T> DataValue queryValueByWhere(String table, String field,String where,String orderBy, final Class<T> entityClass,Object... params){
		BeanAnn ba = BeanAnn.getBeanAnn(entityClass);
		String columnName = ba.getColumnNameByProperty(field);
		if (columnName != null) field = columnName;	
		if (table == null) table = ba.getTable();
		String sql="select "+field+" from "+table;
		if(where !=null && where.trim().length()>0) sql+=" where "+where;
		if(orderBy!=null && orderBy.trim().length()>0) sql+=" order by "+orderBy;
		return this.queryValue(sql,entityClass,params);
	
	}
	public <T> DataValue queryValueByProps(String table, String field, String orderBy, Class<T> entityClass, Object... nameAndValues) {
		List params = new ArrayList();
		BeanAnn ba = BeanAnn.getBeanAnn(entityClass);
		String columnName = ba.getColumnNameByProperty(field);
		if (columnName != null) field = columnName;
		String sql = buildSqlByProps(table, field, orderBy, entityClass, params, nameAndValues);
		return queryValue(sql, entityClass, params.toArray());
	}

	public <T> DataValue queryValueByEntity(String table, String field, String orderBy, T entity) {
		List params = parseParams(table, entity);
		BeanAnn ba = BeanAnn.getBeanAnn(entity.getClass());
		String columnName = ba.getColumnNameByProperty(field);
		if (columnName != null) field = columnName;
		return queryValueByProps(table, field, orderBy, (Class<T>) entity.getClass(), params.toArray());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jsharp.sql.q#reload(java.lang.String, java.lang.Object)
	 */
	public boolean reload(String table, final Object entity) {
		BeanAnn ba = BeanAnn.getBeanAnn(entity.getClass());

		if (table == null) table = ba.getTable();
		String sql = "select * from " + table + " where " + ba.getIdName() + " = ? ";
		Object result = query(sql, new BeanObjectHandler (entity), ba.getIdValue(entity));
		return result != null;

	}

	private Object[] getNameAndValues( Object entity, String... keys){
		Object[] params = new Object[keys.length * 2];
		int i = 0;
		try {
			for (String key : keys) {
				params[i++] = key;
				Object value=PropertyUtils.getProperty(entity, key);
				if(value==null) throw new JSException("value of property '"+key+"' is null");
				params[i++] = value;
			}
			
			return params;

		} catch (Exception e) {
			throw JSException.wrap(e);
		}

	}

	public int save(String table, Object entity, String... keys) {
			if(keys.length==0){
				 return save(table,entity,false);
			}else{
				Object[] params =getNameAndValues(entity,keys);
				if (queryValueByProps(table, "count(*)", null, entity.getClass(), params).getInt(0) == 0) {
					return this.insert(table, entity);
				} else {
					return this.updateByProps(table, entity, params);
				}
			}
	}

    /**
     * null or  not set dyn prop or  string="" or num="0"
     * @param entity
     * @return
     */
	private boolean isEmptyId(Object entity){
		BeanAnn ba = BeanAnn.getBeanAnn(entity.getClass());
		Object idValue = ba.getIdValue(entity);
		boolean empty = false;
		if (idValue == null)
			empty = true;
		else {
			PropertyDescriptor pd=ba.getIdProperty();
			if(pd==null){  // no id found
				empty=true;
			}else {//if( ! EntityUtils.hasDynProperty(entity,pd.getName())) {
				Class propType = ba.getIdProperty().getPropertyType();
				if (propType != null) {
					if (propType.equals(String.class)) {
						if (idValue == null || idValue.toString().length() == 0) empty = true;
					} else if (propType.isPrimitive() || propType.equals(Integer.class) || propType.equals(Long.class) || propType.equals(Short.class) || propType.equals(Byte.class)) {
						String s = idValue.toString();
						if (s == null || s.length() == 0 || s.equals("0")) empty = true;
					}
				}
			}

		}
		return empty;

	}
 
	public int save(String tableName, Object entity, boolean queryBeforeSave) {
		BeanAnn ba = BeanAnn.getBeanAnn(entity.getClass());
		try {
			Object idValue = ba.getIdValue(entity);
			boolean empty = isEmptyId(entity);
			if (empty)
				return insert(tableName, entity,empty,new String[]{ba.getIdProperty().getName()});
			else if (queryBeforeSave) {
				Object e = this.get(tableName, idValue, entity.getClass());
				if (e == null)
					return insert(tableName, entity,empty,new String[]{ba.getIdProperty().getName()});
				else
					return update(tableName, entity);

			} else {
				return update(tableName, entity);
			}

		} catch (Exception e) {
			throw JSException.wrap(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jsharp.sql.SessionFactory#update(java.lang.String, java.lang.Object)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see jsharp.sql.q#update(java.lang.String, java.lang.Object)
	 */
	public int update(String table, Object entity,String... keys) {
		
		if(keys==null|| keys.length==0){
			BeanAnn ba = BeanAnn.getBeanAnn(entity.getClass());
			return updateByWhere(table, entity, new String[]{ba.getIdName()},ba.getIdName() + " = ? ", ba.getIdValue(entity));}
		else{
			Object[] nameAndValues =getNameAndValues(entity,keys);
			return this.updateByProps(table, entity, nameAndValues);
		}
	}

	
	
	/**
	 * create cblib object which can do dynamic update
	 */
	public <T> T createObject(Class<T> c) {
		return BeanSupport.createEntity(c);
	}

	private Object[] getWhereCondition(BeanAnn ba,Object... nameAndValues){
		List<Object> values = new ArrayList<Object>();
		List<String> names = new ArrayList<String>();
		String where = "";
		if (nameAndValues != null && nameAndValues.length > 0) {

			for (int i = 0; i < nameAndValues.length; i += 2) {
				if (where.length() > 0) where += " and ";
				where += ba.getColumnNameByProperty((String) nameAndValues[i]) + " = ? ";
				names.add(nameAndValues[i].toString());
				values.add(nameAndValues[i + 1]);
			}
		}
		return new Object[]{where,values.toArray(),names.toArray(new String[0])};
	}
	
	public int updateByProps(String table, Object entity, Object... nameAndValues) {
		BeanAnn ba = BeanAnn.getBeanAnn(entity.getClass());
		Object[] wheres= getWhereCondition(ba,nameAndValues);
		return updateByWhere(table, entity,null,(String)wheres[0], (Object[])wheres[1]);
	}
	
	public int updateByWhere(String table, Object entity, String where, Object... params) {
		return updateByWhere(table,entity,null,where,params);	
	}

	/**
	 * 
	 * @param table
	 * @param entity
	 * @param ignoreProps  when update(entity), keys of where condition, need't update .
	 * @param where
	 * @param params
	 * @return
	 */
	protected int updateByWhere(String table, Object entity,String[] ignoreProps, String where, Object... params) {
		BeanAnn ba = BeanAnn.getBeanAnn(entity.getClass());
		if (table == null) table = ba.getTable();
		try {
			DataMap list = BeanSupport.toMap(table, entity, ba, false,ignoreProps);
			where = SqlSupport.replacePropsWithColumns(entity.getClass(), where);
			int ret = update(table, list, where, params);
			// clearSqlValues(entity);
			return ret;
		} catch (SQLException e) {
			throw JSException.wrap(e);
		}
	}

	public int updateBySQL(String sql, Class entityClass, Object... params) {
		BeanAnn ba = BeanAnn.getBeanAnn(entityClass);
		//update table set a=?,b=? where c=?
		sql = SqlSupport.replacePropsWithColumns(entityClass, sql).trim();
		String low=sql.toLowerCase();
		if(!low.startsWith("update")){
			String table=ba.getTable();
			String prefix=" update "+table+" ";
			if(!low.startsWith("set")) prefix+=" set ";
			sql=prefix+sql;
		}
		return updateBySQL(sql, params);
	}

	@Override
	public <T> List<T> getAll(String table, String orderBy, Class<T> entityClass) {
		return this.queryListByProps(table, orderBy, entityClass);
	}
}
