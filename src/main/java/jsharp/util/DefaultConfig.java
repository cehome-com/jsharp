package jsharp.util;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

 
 
 

//import com.myarch.reloader.*;

/*
 * 缺省配置信息读取类，用于静态方法直接读取
 * 首先从websharp.properties websharp1.properties websharp2.properties
 * */
/**
 *缺省配置信息读取类，用于静态方法直接读取
 * 首先从websharp.properties websharp-1.properties websharp-2.properties（后者存在相同属性则会覆盖前者） 三个文件中读取
 * 项目实际配置文件名称， config-files属性表示正式环境的配置文件名称，可以时多个，如 config-files=config1,config2
 * 如果是本地测试，可以配置测试配置文件config-test-files，同时要指定一个测试环境判断的类和方法（config-test-class,config-test-method）
 * 用于判断是否在加载完config-files后是否继续加载config-test-files,如：
 * 
sample:
config-files=classpath\:config.properties;classpath\:config-1.properties
config-test-files=classpath\:config-2.properties
config-test-class=websharp.util.EnvUtils
config-test-method=isWindows
 * @author ma
 *
 */
public class DefaultConfig   {

	private static Logger logger = LoggerFactory.getLogger(DefaultConfig.class);

	protected static Config instance = null; 
	
	
	public static String WS_DATASOURCES="ws-datasources";
	
	//-- 定义时间格式（实体类转换时用到），每种格式都可以多个，用逗号隔开
	//缺省日期和时间格式
	public static String WS_CONVERTER_DATETIME_DEFAULT="ws-converter-datetime-default";
	//java.util.date java.sql.date
	public static String WS_CONVERTER_DATETIME_DATE="ws-converter-datetime-date";
	//java.sql.time
	public static String WS_CONVERTER_DATETIME_TIME="ws-converter-datetime-time";
	//java.sql.timestamp
	public static String WS_CONVERTER_DATETIME_TIMESTAMP="ws-converter-datetime-timestamp";
	//转换错误是否抛异常。  
	public static String WS_CONVERTER_EXCEPTION_THROW="ws-convert-exception-throw";
	
	//转换错误是否抛异常。  
	public   static  String WS_MULTIPART_ENCODING="ws-multipart-encoding";
	public   static  String WS_MULTIPART_MAX_FILE_SIZE="ws-multipart-max-file-size";
	public   static  String WS_MULTIPART_MAX_UPLOAD_SIZE="ws-multipart-max-upload-size";
	public   static  String WS_MULTIPART_MEMORY_SIZE="ws-multipart-memory-size";
	public   static  String WS_MULTIPART_TEMP_DIR="ws-multipart-temp-dir"; 
	 

	static {
		
		 
		Config websharpConfig=new Config("classpath:websharp", ".properties",3,null);  
		
		//-- 如果定义了encoding，则按照encoding重新加载属性文件
		String encoding=websharpConfig.getStr("config-encoding").trim();
		if(encoding.length()>0)
			websharpConfig=new Config("classpath:websharp", ".properties",3,encoding);  
			
		
		 
		//ResourceBundle rb=ResourceBundle.getBundle("websharp" ,Locale.ROOT);
		String files=websharpConfig.getStr("config-files");
		ArrayList<String> list=new ArrayList<String>();
		String[] ss=files.split("[,; ]+");
		for(String s: ss)
		{
			s=s.trim();
			if(s.length()>0) list.add(s);
		}
		
		String className= websharpConfig.getStr("config-test-class");
		String methodName= websharpConfig.getStr("config-test-method");
		String testFiles=websharpConfig.getStr("config-test-files");
		if(className.length()==0  && methodName.length()==0&& testFiles.length()==0)
		{
			
		}
		else if(className.length()>0  && methodName.length()>0&& testFiles.length()>0)
		{
			try
			{
				Object o=Class.forName(className).newInstance();
				boolean isTestEnv= (Boolean)o.getClass().getMethod(methodName, null).invoke(o, null);
				if( isTestEnv)
				{
					  
					 ss=testFiles.split("[,; ]+");
						for(String s: ss)
						{
							s=s.trim();
							if(s.length()>0) list.add(s);
						}
				}
			}
			catch(Throwable e)
			{
				logger.error("can not load test config file.",e);
			}
		}
		else
		{
			logger.warn("config-test-class,config-test-method,config-test-files are not properly configured, ignore test config.");
		}
		
		instance = new Config(list.toArray(new String[0]),encoding);
		 
	}

	public static String getStr(String name)
	{
		  
		return instance.getStr(name);
	}
	

	/**
	 * 
	 * @param name
	 * @return  property (can be null)
	 */
	public  static String getString(String name)
	{
		return instance.getString(name);
	}
	
	public static String getString(String name, String def)
	{
		 
		return instance.getString(name,def);
	}
	
	public static long getLong(String name, long def)
	{
		return Convert.toLong(getString(name),def);
	}
	public static int getInt(String name, int def)
	{
		return Convert.toInt(getString(name),def);
	}
	
	public static Properties getProperties() {
		return instance.p;
	}
	
	public static String getConfigFileNames()
	{
		return instance.getConfigFileNames( );
	}

}