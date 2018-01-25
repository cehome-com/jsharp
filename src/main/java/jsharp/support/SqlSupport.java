package jsharp.support;

import com.mysql.jdbc.StringUtils;
import jsharp.sql.JSException;
import jsharp.util.Convert;

import java.beans.PropertyDescriptor;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;

public class SqlSupport {

	private final static TypeNames typeNames = new TypeNames();
	private static String identityColumnString="auto_increment not null ";
	static {

		//-- mysql support
		registerColumnType( Types.BIT, "bit" );
		registerColumnType( Types.BIGINT, "bigint" );
		registerColumnType( Types.SMALLINT, "smallint" );
		registerColumnType( Types.TINYINT, "tinyint" );
		registerColumnType( Types.INTEGER, "integer" );
		registerColumnType( Types.CHAR, "char(1)" );
		registerColumnType( Types.FLOAT, "float" );
		registerColumnType( Types.FLOAT, "decimal($p,$s)" );
		registerColumnType( Types.DOUBLE, "double precision" );
		registerColumnType( Types.DOUBLE, "decimal($p,$s)" );
		registerColumnType( Types.DATE, "date" );
		registerColumnType( Types.TIME, "time" );
		//registerColumnType( Types.TIMESTAMP, "datetime" );
		registerColumnType( Types.TIMESTAMP, "datetime" );
		registerColumnType( Types.VARBINARY, "longblob" );
		registerColumnType( Types.VARBINARY, 16777215, "mediumblob" );
		registerColumnType( Types.VARBINARY, 65535, "blob" );
		registerColumnType( Types.VARBINARY, 255, "tinyblob" );
		registerColumnType( Types.LONGVARBINARY, "longblob" );
		registerColumnType( Types.LONGVARBINARY, 16777215, "mediumblob" );
		registerColumnType( Types.NUMERIC, "decimal($p,$s)" );
		registerColumnType( Types.BLOB, "longblob" );
		registerColumnType( Types.CLOB, "longtext" );

		registerColumnType( Types.VARCHAR, "longtext" );
		registerColumnType( Types.VARCHAR, 255, "varchar($l)" );
		registerColumnType( Types.LONGVARCHAR, "longtext" );

	}
	private static void registerColumnType(int code, int capacity, String name) {
		typeNames.put( code, capacity, name );
	}
	private static void registerColumnType(int code, String name) {
		typeNames.put( code, name );
	}
	private static String getTypeName(int code, int length, int precision, int scale) throws JSException {
		String result = typeNames.get( code, length, precision, scale );
		if ( result == null ) {
			throw new JSException(
					"No type mapping for java.sql.Types code: " +
							code +
							", length: " +
							length
			);
		}
		return result;
	}


	public static SQLException getSQLException(Exception e) {
		if (e instanceof SQLException) return (SQLException) e;
		return new SQLException(e);

	}

	/**
	 * 把语句中的用大括号包含起来的属性（如：{name}）替换成数据库字段，如果找不到字段则缺省用name。
	 * 
	 * 如果是字符串内容值应该忽略，目前没有忽略，如 x='ddd{dddd}xx'里面的大括号，将来可能需要加上？
	 * 
	 * @param entityClass
	 * @param where
	 * @return
	 */
	public static String replacePropsWithColumns(Class entityClass, String where) {
		// Map 不进行处理
		if (where==null || Map.class.isAssignableFrom(entityClass.getClass())) return where;

		BeanAnn ba = BeanAnn.getBeanAnn(entityClass);
		int n = 0;
		int p = -1;
		// {name}=xxx and {value}= ?
		String result = "";
		while (n < where.length()) {
			char c = where.charAt(n);
			if (c == '{') {
				if (n == 0 || where.charAt(n - 1) != '$') // ${} 认为是函数，忽略
					p = n;
			} else if (c == '}') {
				if (p != -1) {
					String name = where.substring(p + 1, n);
					result += ba.getColumnNameByProperty(name);
					p = -1;
				}

			} else if (p == -1) {
				result += c;
			}
			n++;

		}
		return result;

	}

	/**
	 * select * from aaa where a=b group by a order by aa
	 * 
	 * @param sql
	 * @return
	 */
	public static  String fixSql(String sql, Class entityClass) {
		if(sql==null) sql="";
		String sql2 =sql.length()==0?"":sql.trim().toLowerCase();
		if (sql2.startsWith("select ")) {
			return sql;
		} else if (sql2.startsWith("from ")) {
			return "select * " + sql;
		} else {
			if (entityClass == null) return sql;
			BeanAnn ba = BeanAnn.getBeanAnn(entityClass);
			String table = ba.getTable();
			if (table == null) return sql;
			if (sql2.length()==0|| sql2.startsWith("where ") || sql2.startsWith("order ") || sql2.startsWith("group ") || sql2.startsWith("limit ")) {
				return "select * from " + table + " " + sql;
			} else {
				return "select * from " + table + " where " + sql;
			}
		}
	}

	public static String sqlCreateString(String table, Class entityClass)
	{

		BeanAnn ba = BeanAnn.getBeanAnn(entityClass);



		StringBuilder buf = new StringBuilder("create table").append(' ')

				.append(StringUtils.isNullOrEmpty(table) ? ba.getTable() : table).append(" (");

		// creat table aa(id long ,a varchar(20))
		PropertyDescriptor[] props = ba.getProperties();
		Map<String, ColumnAnn> columnMap = ba.getColumnMap();
		int n=0;
		for (int i = 0; i < props.length; i++)
		{
			PropertyDescriptor prop = props[i];
			String name = props[i].getName();
			if (name.equals("class"))
				continue;
			ColumnAnn fa = columnMap.get(name);
			if (fa.isTransient())
				continue;
			if(n>0) buf.append(", ");
			n++;

			int sqlType = Types.NULL;
			if (fa.isClob())
				sqlType = java.sql.Types.CLOB;
			else if (fa.isBlob())
				sqlType = Types.BLOB;
			else
				sqlType = javaTypeToSqlType(prop.getPropertyType());

			String typeName = getTypeName(sqlType, fa.getLength(), fa.getPrecision(), fa.getScale());

			buf.append(fa.getName()).append(' ').append(typeName);
			if (fa.isIdentitied() )
			{
				buf.append(" PRIMARY KEY ");

				buf.append(' ').append(identityColumnString).append(' ');
			}


			if (Convert.toStr( fa.getDefaultValue()).trim().length()>0)
			{
				buf.append(" default ").append(fa.getDefaultValue());
			}

			if (!fa.isNullable())
			{
				buf.append(" not null ");
			}

			if (fa.isUnique())
			{
				buf.append(" unique");
			}

			/*if (Convert.toString( fa.getComment()).trim().length()>0)
			{
				buf.append(" comment ").append( "'"+fa.getComment()+"'");
			}*/


		}

		//if (ba.getIdProperty() != null)
		//{
		//	buf.append(" PRIMARY KEY ( " + ba.getIdName() + " ) ");
		//}

		buf.append(" ) ");
		if(n==0) return "";
		return buf.toString();
	}

	/**
	 * Java 类型 JDBC 类型 String VARCHAR 或 LONGVARCHAR java.math.BigDecimal NUMERIC
	 * boolean BIT byte TINYINT short SMALLINT int INTEGER long BIGINT float
	 * REAL double DOUBLE byte[] VARBINARY 或 LONGVARBINARY java.sql.Date DATE
	 * java.sql.Time TIME java.sql.Timestamp TIMESTAMP
	 *
	 * @param type
	 * @return
	 */
	public static int javaTypeToSqlType(Class type)
	{

		if (String.class.equals(type))
			return Types.VARCHAR;// LONGVARCHAR
		if (Integer.TYPE.equals(type) || Integer.class.equals(type))
			return Types.INTEGER;
		if (java.math.BigDecimal.class.equals(type))
			return Types.NUMERIC;
		if (Boolean.TYPE.equals(type) || Boolean.class.equals(type))
			return Types.BIT;
		if (Byte.TYPE.equals(type) || Byte.class.equals(type))
			return Types.TINYINT;
		if (Short.TYPE.equals(type) || Short.class.equals(type))
			return Types.SMALLINT;
		if (Long.TYPE.equals(type) || Long.class.equals(type))
			return Types.BIGINT;
		if (Float.TYPE.equals(type) || Float.class.equals(type))
			return Types.REAL;
		if (Double.TYPE.equals(type) || Double.class.equals(type))
			return Types.DOUBLE;
		if (Byte[].class.equals(type) || byte[].class.equals(type))
			return Types.VARBINARY;
		if (java.util.Date.class.equals(type))
			return Types.TIMESTAMP;
		if (java.sql.Time.class.equals(type))
			return Types.TIME;
		if (java.sql.Timestamp.class.equals(type))
			return Types.TIMESTAMP;

		return Types.NULL;

	}
}
