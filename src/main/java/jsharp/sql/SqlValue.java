package jsharp.sql;
/**
 * 
 * @author wanma
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
