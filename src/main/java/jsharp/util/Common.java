package jsharp.util;
import java.io.*;
import java.util.*;
import java.text.MessageFormat;
import java.lang.reflect.*;
public class Common
{

	
	public static void scriptAlert(Writer w, String s)throws IOException
	{
		
	   w.write( "<script>alert('"+s+"');</script>\r\n");
	
	}
	
	public static String format(String strFormat,Object[] args)
	{
		MessageFormat fmt = new MessageFormat(strFormat);
		 
		return fmt.format(args);
	}
	
	public static String format(String strFormat,Object arg1 )
	{
		MessageFormat fmt = new MessageFormat(strFormat);
		Object[] args=new Object[1];
		args[0]=arg1;
	    return fmt.format(args);
	}
	public static String format(String strFormat,Object arg1,Object arg2 )
	{
		MessageFormat fmt = new MessageFormat(strFormat);
		Object[] args=new Object[2];
		args[0]=arg1;
		args[1]=arg2;
	 
		return fmt.format(args);
	}
	public static String format(String strFormat,Object arg1,Object arg2,Object arg3)
	{
		MessageFormat fmt = new MessageFormat(strFormat);
		Object[] args=new Object[3];
		args[0]=arg1;
		args[1]=arg2;
		args[2]=arg3;
		return fmt.format(args);
	}
	public static String format(String strFormat,Object arg1,Object arg2,Object arg3,Object arg4)
	{
		MessageFormat fmt = new MessageFormat(strFormat);
		Object[] args=new Object[4];
		args[0]=arg1;
		args[1]=arg2;
		args[2]=arg3;
		args[3]=arg4;
		return fmt.format(args);
	}
	
	public static String encodeUTF8(String s) throws Exception
	{
		if(s==null) s="";
		return java.net.URLEncoder.encode(s,"UTF-8");
	}
	
	public static Locale getLocale(javax.servlet.http.HttpSession session )
	{ 
		String strLanguage=Convert.toStr(session.getAttribute("Language"));
		if(strLanguage.length()==0 || strLanguage.equals("_")) return null;
		int n=strLanguage.indexOf('_');
		if (n==-1)  return new Locale(strLanguage);
		String s1=strLanguage.substring(0,n);
		String s2=strLanguage.substring(n+1);
		return new Locale(s1,s2);
		
	}
	public static ResourceBundle getBundle(javax.servlet.http.HttpSession session )
	{
		return ResourceBundle.getBundle("com.trs.Resource.resource",getLocale(session));
	}
	
	public static void reloadBundles() throws Exception
    {
        
            clearMap(ResourceBundle.class, null, "cacheList");
 
            // now, for the true and utter hack, if we're running in tomcat, clear
            // it's class loader resource cache as well.
            clearTomcatCache();
   
    }
 
 
	private static void clearTomcatCache() throws Exception
    {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        // no need for compilation here.
        Class cl = loader.getClass();
 
        
            if ("org.apache.catalina.loader.WebappClassLoader".equals(cl.getName()))
            {
                clearMap(cl, loader, "resourceEntries");
            }
            else
            {
                throw new Exception("class loader " + cl.getName() + " is not tomcat loader.");
            }
        
    }

 
	private static void clearMap(Class cl, Object obj, String name) 
        throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException,
        InvocationTargetException
    {    
        java.lang.reflect.Field field = cl.getDeclaredField(name);
        field.setAccessible(true);
 
        Object cache = field.get(obj);
        Class ccl = cache.getClass();
        Method sizeMethod = ccl.getMethod("size", null);
        //log.info(name + ": size before clear: " + sizeMethod.invoke(cache, null));
        Method clearMethod = ccl.getMethod("clear", null);
        clearMethod.invoke(cache, null);
        //log.info(name + ": size after clear: " + sizeMethod.invoke(cache, null));
    }
	
	
	 
	public static void closeObject(Object o)
	{
		try
		{
			
				o.getClass().getMethod("close", null).invoke(o,null);
		}
		catch(Exception e)
		{
			
		}
	}
	
	public static void closeObjects(Object... os)
	{
		for(Object o :os)closeObject(o);
	}
   
	
	public static Locale toLocale(String str)
	{
		if (str == null ||str.length()==0)
			return null;
		int len = str.length();
		if (len != 2 && len != 5 && len < 7)
			throw new IllegalArgumentException("Invalid locale format: " + str);
		char ch0 = str.charAt(0);
		char ch1 = str.charAt(1);
		if (ch0 < 'a' || ch0 > 'z' || ch1 < 'a' || ch1 > 'z')
			throw new IllegalArgumentException("Invalid locale format: " + str);
		if (len == 2)
			return new Locale(str, "");
		if (str.charAt(2) != '_')
			throw new IllegalArgumentException("Invalid locale format: " + str);
		char ch3 = str.charAt(3);
		if (ch3 == '_')
			return new Locale(str.substring(0, 2), "", str.substring(4));
		char ch4 = str.charAt(4);
		if (ch3 < 'A' || ch3 > 'Z' || ch4 < 'A' || ch4 > 'Z')
			throw new IllegalArgumentException("Invalid locale format: " + str);
		if (len == 5)
			return new Locale(str.substring(0, 2), str.substring(3, 5));
		if (str.charAt(5) != '_')
			throw new IllegalArgumentException("Invalid locale format: " + str);
		else
			return new Locale(str.substring(0, 2), str.substring(3, 5), str.substring(6));
	}

}
