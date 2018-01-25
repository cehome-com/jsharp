package jsharp.support;
/**
 * 字段的属性信息
 * @author ma
 *
 */
public class ColumnAnn
{

	private String name;
	private boolean insertable=true;
	private boolean updatable=true;
	private boolean transient_=false;
	private boolean identitied=false;
	private boolean lob=false;
	private boolean blob=false;
	private boolean clob=false;
	
	private boolean unique=false;
	private boolean nullable=true;
	private String columnDefinition=null;
	private String defaultValue=null;
	
	
	private int length=255;
	private int precision=0;
	private int scale=0;
	
	public boolean isTransient_()
	{
		return transient_;
	}
	public void setTransient_(boolean transient_)
	{
		this.transient_ = transient_;
	}
	public boolean isUnique()
	{
		return unique;
	}
	public void setUnique(boolean unique)
	{
		this.unique = unique;
	}
	public boolean isNullable()
	{
		return nullable;
	}
	public void setNullable(boolean nullable)
	{
		this.nullable = nullable;
	}
	public String getColumnDefinition()
	{
		return columnDefinition;
	}
	public void setColumnDefinition(String columnDefinition)
	{
		this.columnDefinition = columnDefinition;
	}
	public int getLength()
	{
		return length;
	}
	public void setLength(int length)
	{
		this.length = length;
	}
	public int getPrecision()
	{
		return precision;
	}
	public void setPrecision(int precision)
	{
		this.precision = precision;
	}
	public int getScale()
	{
		return scale;
	}
	public void setScale(int scale)
	{
		this.scale = scale;
	}
	public boolean isBlob()
	{
		return blob;
	}
	public void setBlob(boolean blob)
	{
		this.blob = blob;
	}
	public boolean isClob()
	{
		return clob;
	}
	public void setClob(boolean clob)
	{
		this.clob = clob;
	}
	public boolean isLob()
	{
		return lob;
	}
	public void setLob(boolean lob)
	{
		this.lob = lob;
	}
	public boolean isIdentitied()
	{
		return identitied;
	}
	public void setIdentitied(boolean identitied)
	{
		this.identitied = identitied;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public boolean isInsertable()
	{
		return insertable;
	}
	public void setInsertable(boolean insertable)
	{
		this.insertable = insertable;
	}
	public boolean isUpdatable()
	{
		return updatable;
	}
	public void setUpdatable(boolean updatable)
	{
		this.updatable = updatable;
	}
	public boolean isTransient()
	{
		return transient_;
	}
	public void setTransient(boolean transient_)
	{
		this.transient_ = transient_;
	}
	
	public String getDefaultValue()
	{
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue)
	{
		this.defaultValue = defaultValue;
	}
	
}
