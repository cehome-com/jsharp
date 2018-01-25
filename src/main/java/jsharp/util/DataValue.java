package jsharp.util;

public class DataValue {

	public static int TYPE_DEFAULT = -1;
	public static int TYPE_TEXT = 0; // 把value直接加入,不设置？参数
	public static int TYPE_CLOB = 1;
	public static int TYPE_BLOB = 2;

	Object value;
	int type;
	int length;

	public DataValue() {

	}

	public DataValue(Object value) {

		this.value = value;
	}

	public DataValue(Object value, int type) {

		this.value = value;
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * 
	 * @return not null string
	 */
	public String getStr() {
		return Convert.toStr(value);
	}

	public int getInt(int defaultValue) {
		return Convert.toInt(value, defaultValue);
	}

	public long getLong(long defaultValue) {
		return Convert.toLong(value, defaultValue);
	}

	public float getFloat(float defaultValue) {
		return Convert.toFloat(value, defaultValue);
	}

	public double getDouble(double defaultValue) {
		return Convert.toDouble(value, defaultValue);
	}
}
