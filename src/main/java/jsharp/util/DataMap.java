package jsharp.util;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;


/**
 * {@link LinkedHashMap} variant that stores String keys in a case-insensitive
 * manner, for example for key-based access in a results table.
 *
 * <p>Preserves the original order as well as the original casing of keys,
 * while allowing for contains, get and remove calls with any case of key.
 *
 * <p>Does <i>not</i> support <code>null</code> keys.
 *
 * @author Juergen Hoeller
 * @since 3.0
 */
public class DataMap extends LinkedHashMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Map<String, String> caseInsensitiveKeys;
	private  Map<String, Integer> dataTypes=null;

	private final Locale locale;


	/**
	 * Create a new LinkedCaseInsensitiveMap for the default Locale.
	 * @see java.lang.String#toLowerCase()
	 */
	public DataMap() {
		this(null);
	}

	/**
	 * Create a new LinkedCaseInsensitiveMap that stores lower-case keys
	 * according to the given Locale.
	 * @param locale the Locale to use for lower-case conversion
	 * @see java.lang.String#toLowerCase(java.util.Locale)
	 */
	public DataMap(Locale locale) {
		super();
		this.caseInsensitiveKeys = new HashMap<String, String>();
		this.locale = (locale != null ? locale : Locale.getDefault());
	}

	/**
	 * Create a new LinkedCaseInsensitiveMap that wraps a {@link LinkedHashMap}
	 * with the given initial capacity and stores lower-case keys according
	 * to the default Locale.
	 * @param initialCapacity the initial capacity
	 * @see java.lang.String#toLowerCase()
	 */
	public DataMap(int initialCapacity) {
		this(initialCapacity, null);
	}

	/**
	 * Create a new LinkedCaseInsensitiveMap that wraps a {@link LinkedHashMap}
	 * with the given initial capacity and stores lower-case keys according
	 * to the given Locale.
	 * @param initialCapacity the initial capacity
	 * @param locale the Locale to use for lower-case conversion
	 * @see java.lang.String#toLowerCase(java.util.Locale)
	 */
	public DataMap(int initialCapacity, Locale locale) {
		super(initialCapacity);
		this.caseInsensitiveKeys = new HashMap<String, String>(initialCapacity);
		this.locale = (locale != null ? locale : Locale.getDefault());
	}


	@Override
	public Object put(String key, Object value) {
		this.caseInsensitiveKeys.put(convertKey(key), key);
		if(dataTypes!=null)dataTypes.remove(key);
		return super.put(key, value);
	}
	
	public Object put(String key, Object value,int type) {
		if(dataTypes==null) dataTypes=new  HashMap<String,Integer>();
		dataTypes.put(convertKey(key), type);
		this.caseInsensitiveKeys.put(convertKey(key), key);
		return super.put(key, value);
	}
	
	

	@Override
	public boolean containsKey(Object key) {
		return (key instanceof String && this.caseInsensitiveKeys.containsKey(convertKey((String) key)));
	}

	@Override
	public Object get(Object key) {
		if (key instanceof String) {
			return super.get(this.caseInsensitiveKeys.get(convertKey((String) key)));
		}
		else {
			return null;
		}
	}
	public int getType(Object key) {
		if(dataTypes==null)return -1;
		if (key instanceof String) {
			Integer n= dataTypes.get(convertKey((String) key));
			return n==null?-1:n;
		}
		else {
			return -1;
		}
	}

	@Override
	public Object remove(Object key) {
		if (key instanceof String ) {
			return super.remove(this.caseInsensitiveKeys.remove(convertKey((String) key)));
		}
		else {
			return null;
		}
	}

	@Override
	public void clear() {
		this.caseInsensitiveKeys.clear();
		super.clear();
	}


	/**
	 * Convert the given key to a case-insensitive key.
	 * <p>The default implementation converts the key
	 * to lower-case according to this Map's Locale.
	 * @param key the user-specified key
	 * @return the key to use for storing
	 * @see java.lang.String#toLowerCase(java.util.Locale)
	 */
	protected String convertKey(String key) {
		return key.toLowerCase(this.locale);
	}
	
	public String getString(Object key)
	{
		Object o=get(key);
		if(o==null) return null ;
		return o.toString();
	}
	
	public String getStr(Object key)
	{
		return Convert.toStr(get(key));
	}
	
	public int getInt(Object key)
	{
		 return Convert.toInt(this.get(key));
	}
	
	
	public int getInt(Object key,int def)
	{
		 return Convert.toInt(this.get(key), def);
	}
	
	public long getLong(Object key)
	{
	
		 return Convert.toLong(this.get(key));
	}
	
	public long getLong(Object key,long def)
	{
	
		 return Convert.toLong(this.get(key), def);
	}
	
	public static float getFloat(Object key,float def){
		 return Convert.toFloat(key,def);
	}
	
	public static double getDouble(Object key,double def){
		 return Convert.toDouble(key,def);
	}
	
	
}
