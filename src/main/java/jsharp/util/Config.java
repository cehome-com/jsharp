package jsharp.util;
import java.io.*;
import java.util.*;

import jsharp.util.loader.ResourceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



//import com.myarch.reloader.*;

/*
 * 配置信息读取类（从 config.properties读取项目的配置信息）
 * 
 * */

public  class Config
{
	final Logger logger = LoggerFactory.getLogger(Config.class);  
	protected Properties p = null;

	protected String[] configFiles = null;
	
	private  String encoding="UTF-8";
	
	private static String configPath=null;

	public Config() 
	{
		 
	}
	 

	public  Config(String classPath,String encoding)
	{
		loadConfig(new String[] { classPath },encoding);
	}
	
	public void loadConfig(String[] classPaths,String encoding)
	{
		this.configFiles = classPaths;
		this.encoding=encoding;
		load();
	}
	
	public   Config(String[] classPaths,String encoding)
	{
		loadConfig(classPaths,encoding);
		 
	}

	/**
	 *  config config1 config2
	 * @param count  搜索索引数量，如 classPath=config , count=3 ,则会继续找config-1,config-2
	 */
	public   Config(String prefix,String suffix, int count,String encoding)
	{
		String[] ss=new String[count];
		ss[0]=prefix+suffix;
		for(int i=1;i<count;i++)
		{
			ss[i]=prefix+"-"+i+suffix;
		}
		  
		loadConfig(ss,encoding);
	}
	
	public Properties getProperties(){
		return p;
	}
	
	/**
	 * 
	 * @param name
	 * @return Not null string
	 */
	public String getStr(String name)
	{
		 
		String s = p.getProperty(name);
		if(s==null)return "";
		return s;
	}
	

	/**
	 * 
	 * @param name
	 * @return  property (can be null)
	 */
	public String getString(String name)
	{
		 
		String s = p.getProperty(name);
		
		return s;
	}
	
	public String getString(String name, String def)
	{
		 
		String s = p.getProperty(name);
		if (s == null) logger.warn("set property to default:" + name + "=" + def);
		if (s == null || s.trim().length() == 0) s = def;
		return s;
	}

	public synchronized String load()
	{
		try
		{

			p = new Properties();
			boolean found=false;
			for (String configFile : configFiles)
			{
				java.net.URL url =null;
				if (configFile.startsWith("classpath:") )
				{
				     String s=	configFile.substring("classpath:".length()).trim();
				     if(s.length()>0) url =ResourceLoader.getResource(s );// "com/trs/tool/global.properties");
				    
				}
				else 
					{
					  if(configFile.length()>0)	
						  {
						      File f=new File(configFile);
						      if( f.exists())  url=f.toURL();//java.net.URL(configFile);//
						  }
					}
				// System.out.println("config.properties="+url);
				if (url == null)
				{
					
					continue;
				}
				 
				
				found=true;
				
				String urlPath=  java.net.URLDecoder.decode(url.getPath(), "UTF-8");	
				logger.info("load {} from {} ", configFile, urlPath);
				
				//if(configPath==null)
				//{
					
					//configPath=new File(temp).getParentFile().getCanonicalPath();
				//}
				

				Properties p1 = new Properties();
				// if(resourceClassName.length()>0)
				// ((BaseResource.class)Class.forName("")).clear();

				if(Convert.toStr(encoding).trim().length()==0)
					p1.load(new InputStreamReader( url.openStream()));
				else
				p1.load(new InputStreamReader( url.openStream(),encoding));

				p.putAll(p1);

			}
			if(configFiles==null|| configFiles.length==0) logger.error("config files is empty ");
			else if(!found)logger.warn( "can not find "+this.getConfigFileNames());
			
			

			//doLoad();

		}
		catch (Exception e)
		{
			// e.printStackTrace();
			logger.error("Exception from  Config.reload():" , e);
			return "" + e;
		}
		return "";

	}
	
	public String getConfigFileNames()
	{
		return Convert.arrayToString(configFiles, ",", "");
	}
	
	
	

	//protected abstract void doLoad();

	 
	/**
	 * 返回配置文件所在的路径（类的根路径）
	 */
	/*public static String getConfigPath()
	{
		return configPath;
	}*/

}