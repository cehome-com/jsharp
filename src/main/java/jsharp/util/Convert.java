package jsharp.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ArrayUtils;

public class Convert
{
	/**
	 * 
	 * @param s
	 * @return not null string
	 */
	public static String toStr(String s)
	{
		 return toString(s,"");
	   
	}
	
	/**
	 * 
	 * @param obj
	 * @return not null string
	 */
	public static String toStr(Object obj)
	{
		 return toString(obj,"");
	   
	}
	
	public static String toString(Object obj,String def)
	{
		 
		if(obj==null) return def;// || obj instanceof DBNull ) return "";
		else return obj.toString();
	   
	}
	

	public static int toInt(Object o)
	{
		if(o instanceof String) return  Integer.parseInt((String)o); 
		if(o instanceof Number)   return ((Number)o).intValue();
		return Integer.parseInt((String)o.toString());
	} 
	
	public static int toInt(Object o,int nDefault)
	{
		if(o==null) return nDefault;
		try
		{
			
		  return toInt(o);
			 
		}
		catch(Exception e)
		{
			return nDefault;
		}
	}
	
	public static long toLong(Object o)
	{
		if(o instanceof String) return  Long.parseLong((String)o); 
		if(o instanceof Number)   return ((Number)o).longValue();
		return Long.parseLong((String)o.toString());
	}
	public static long toLong(Object o,long nDefault)
	{
		if(o==null) return nDefault;
		try
		{
			return toLong(o);
		  
		}
		catch(Exception e)
		{
			return nDefault;
		}
	}
	
	public static float toFloat(Object o,float nDefault)
	{
		if(o==null) return nDefault;
		try
		{
			if(o instanceof String) return  Float.parseFloat((String)o); 
			if(o instanceof Number)   return ((Number)o).floatValue();
			return Float.parseFloat((String)o.toString());
		  
			 
		}
		catch(Exception e)
		{
			return nDefault;
		}
	}
	
	public static Double toDouble(Object o,Double nDefault)
	{
		if(o==null) return nDefault;
		try
		{
			if(o instanceof String) return  Double.parseDouble((String)o); 
			if(o instanceof Number)   return ((Number)o).doubleValue();
			return Double.parseDouble((String)o.toString());
		  
			 
		}
		catch(Exception e)
		{
			return nDefault;
		}
	}
	
	
	public static <T>Set<T> toSet(T... objects)
	{
		Set<T> set=new HashSet<T>(objects==null?1:objects.length);
		if(objects!=null) 
		for(T o:objects) set.add(o);
		return set;
	}
	
	
	
	/*public static Integer toInteger(String s,int nDefault)
	{
	   int n=toInt(s,nDefault);
	   return new Integer(n);
	}*/
	
	
	/**
	 * Convert an array to string , such as {'aa','bb'} to 'aa','bb'
	 * @param array
	 * @param separator
	 * @param quote
	 * @return
	 */
	public static String arrayToString(Object[] array,String separator,String quote)
	{
		if(array==null || array.length==0) return "";
		if(quote==null) quote="";
		String s="";
		for(int nIndex=0;nIndex<array.length;nIndex++)
		{
			if(s.length()>0) s=s+separator;
			if( array[nIndex]!=null)
			s+=quote+ array[nIndex]+quote;
		}
		 return s;
	}
	
	public static String arrayToString(boolean[] array,String separator,String quote){
		return arrayToString( ArrayUtils.toObject(array),separator,quote);
	}
	public static String arrayToString(byte[] array,String separator,String quote){
		return arrayToString( ArrayUtils.toObject(array),separator,quote);
	}
	public static String arrayToString(short[] array,String separator,String quote){
		return arrayToString( ArrayUtils.toObject(array),separator,quote);
	}
	public static String arrayToString(int[] array,String separator,String quote){
		return arrayToString( ArrayUtils.toObject(array),separator,quote);
	}
	public static String arrayToString(long[] array,String separator,String quote){
		return arrayToString( ArrayUtils.toObject(array),separator,quote);
	}
	public static String arrayToString(float[] array,String separator,String quote){
		return arrayToString( ArrayUtils.toObject(array),separator,quote);
	}
	public static String arrayToString(double[] array,String separator,String quote){
		return arrayToString( ArrayUtils.toObject(array),separator,quote);
	}
	public static String arrayToString(char[] array,String separator,String quote){
		return arrayToString( ArrayUtils.toObject(array),separator,quote);
	}
	
	
	public static String toString(List array,String separator,String quote)
	{
		if(array==null || array.size()==0) return "";
		if(quote==null) quote="";
		String s="";
		for(int nIndex=0;nIndex<array.size();nIndex++)
		{
			if(s.length()>0) s=s+separator;
			if( array.get(nIndex)!=null)
			s+=quote+ array.get(nIndex)+quote;
		}
		 return s;
	}
	
	public static Vector toVector(Object[] objects)
	{
		 Vector v=new Vector();
		 if(objects!=null)
		 for(int i=0;i<objects.length;i++) v.add(objects[i]);
		 return v;
	}
	public static Vector toVector(String s,String separator)
	{
		String[] ss=s.split(separator);
		 return toVector(ss);
	}
	
	 
	
	public static String[] toArray(String s,int step)
	{
		if(s==null || s.length()==0) return null;
		int len=(s.length()-1) /step+1;
		String[] ss=new String[len];
		for(int i=0;i<len-1;i++)
		{
			ss[i]=s.substring(i*step,step);
		}
		ss[len-1]=s.substring((len-1)*step);
		return ss;
		
	}
	
	
	/**
	 * 锟窖讹拷态锟斤拷锟斤拷锟斤拷锟阶狶ist锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷List锟斤拷锟斤拷转锟斤拷
	 * @param objects
	 * @return not null List
	 */
	public static  List  toList(Object... objects )
	{   
		Object[] array=objects;
		if(objects==null) return new ArrayList();
		if(objects.length==1 && objects[0]==null) return new ArrayList();
		if(objects.length==1 && (objects[0] instanceof List) ) return (List)objects[0] ;
		if(objects.length==1 && (objects[0].getClass().isArray()) ){
			Object o=objects[0];
			if(o instanceof int[]){
				array=ArrayUtils.toObject((int[])o);
			}else if(o instanceof byte[]){
				array=ArrayUtils.toObject((byte[])o);
			}else if(o instanceof boolean[]){
				array=ArrayUtils.toObject((boolean[])o);
			}else if(o instanceof long[]){
				array=ArrayUtils.toObject((long[])o);
			}else if(o instanceof float[]){
				array=ArrayUtils.toObject((float[])o);
			}else if(o instanceof double[]){
				array=ArrayUtils.toObject((double[])o);
			}else if(o instanceof char[]){
				array=ArrayUtils.toObject((char[])o);
			}else if(o instanceof short[]){
				array=ArrayUtils.toObject((short[])o);
			}
			
		}
		List<Object> list=new ArrayList<Object>(array.length);
		for(int i=0;i<array.length;i++) list.add(array[i]);
		return list;
	}
	
	
	public static Date toDate(String d,String format) throws ParseException{
		return new SimpleDateFormat(format).parse(d);
	}
	
	
	public static Map toMap(List list,String keyPropName){
		if(list==null) return null;
		Map m=new HashMap();
		try{
		for(Object o :list){
			if(o!=null)
			m.put( PropertyUtils.getProperty(o, keyPropName), o);
		}
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		return m;
	}
	
	public static String toMD5(final String s){
	        try {
	            byte[] btInput = s.getBytes();
	            MessageDigest mdInst = MessageDigest.getInstance("MD5");
	            mdInst.update(btInput);
	            byte[] md = mdInst.digest();
	            StringBuffer sb = new StringBuffer();
	            for (byte element : md) {
	                int val = element & 0xff;
	                if (val < 16) {
	                    sb.append("0");
	                }
	                sb.append(Integer.toHexString(val));
	            }
	            return sb.toString();
	        } catch (Exception e) {
	            return null;
	        }
	     
	}
	
	public static String toString(Throwable e){
		StringWriter w=new  StringWriter();
		e.printStackTrace(new PrintWriter(w) );
		return w.toString();
	}
	
	/**
	 * time to seconds
	 * @param s
	 * @return
	 */
	public static int toSeconds(String s) {
		char c = Character.toLowerCase(s.charAt(s.length() - 1));
		String s2 = s.substring(0, s.length() - 1);
		int t = s2.length() > 0 ? Integer.parseInt(s2) : 0;
		if (c == 's')
			return t;
		else if (c == 'm')
			return t * 60;
		else if (c == 'h')
			return t * 3600;
		else if (c == 'd')
			return t * 3600 * 24;
		else
			return Integer.parseInt(s);
	}

	private static  long GB= 1024*1024*1024;
	private static  long EB= GB*GB;
	private static  long NB= GB*GB*GB;
	public static long toByteUnit(String s) {
		char c = Character.toLowerCase(s.charAt(s.length() - 1));
		String s2 = s.substring(0, s.length() - 1);
		long t = s2.length() > 0 ? Long.parseLong(s2) : 0;
		if (c == 'b'|| (c>='0'&&c<='9'))
			return t;
		else if (c == 'k')
			return t * 1024;
		else if (c == 'm')
			return t * 1024*1024;
		else if (c == 'g')
			return t * GB;
		else if (c == 't')
			return t * GB*1024;
		else if (c == 'p')
			return t * GB*1024*1024;
		else if (c == 'e')
			return t * EB;
		else if (c == 'z')
			return t * EB*1024;
		else if (c == 'y')
			return t * EB*1024*1024;
		else if (c == 'n')
			return t * NB;
		else if (c == 'd')
			return NB * 1024;
		else
			return Long.parseLong(s);
	}
	 
	
	public static void main(String[] args){
		//Convert.toString(lotteryTypeIds, ",", null)+")"; 
			//String a[]={"a","b","c","d","e"};
			int a[] ={2,4};
			String separator = ",";
			String padding = null;
			String str = Convert.arrayToString(a, separator, padding);
			System.out.println(str);
			
	} 
}
