package jsharp.sql;
/**
 * 
 * @author wanma
 * @creation 2014锟斤拷6锟斤拷8锟斤拷
 */
public class SqlValue {
	
	private String value;

	public SqlValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String toString(){
		return value;
	}

}
