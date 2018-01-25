package jsharp.sql;
import java.sql.SQLException; 

 
public class JSException extends java.lang.RuntimeException
{

	private void setPropties(SQLException e)
	{
		this.vendorCode = e.getErrorCode();

		this.SQLState = e.getSQLState();
	}
	
	public static JSException wrap(Throwable e)
	{
		if(e instanceof JSException) return (JSException)e;
		return new JSException(e);
	}

	public JSException(Throwable cause)  
	{
		super(cause.getMessage(),cause);
		if (cause instanceof SQLException)
			setPropties((SQLException) cause);

	}
 
	public JSException(String reason, Throwable cause)
	{
		super(reason, cause);

		if (cause instanceof SQLException)
			setPropties((SQLException) cause);

	}

	 
	public JSException(String reason)
	{
		super(reason);
	}
	public JSException(String reason, String sqlState, Throwable cause)
	{
		super(reason, cause);

		this.SQLState = sqlState;
		this.vendorCode = 0;

	}

	 
	public JSException(String reason, String sqlState, int vendorCode, Throwable cause)
	{
		super(reason, cause);

		this.SQLState = sqlState;
		this.vendorCode = vendorCode;

	}

	/**
	 * Retrieves the SQLState for this <code>WebsharpException</code> object.
	 * 
	 * @return the SQLState value
	 */
	public String getSQLState()
	{
		return (SQLState);
	}

	/**
	 * Retrieves the vendor-specific exception code for this
	 * <code>WebsharpException</code> object.
	 * 
	 * @return the vendor's error code
	 */
	public int getErrorCode()
	{
		return (vendorCode);
	}

	/**
	 * @serial
	 */
	private String SQLState;

	/**
	 * @serial
	 */
	private int vendorCode;

	 

	private static final long serialVersionUID = 2135244094396331484L;
}
