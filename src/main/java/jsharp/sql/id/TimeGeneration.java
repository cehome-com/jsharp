package jsharp.sql.id;

import java.sql.SQLException;

import jsharp.sql.SessionFactory;

 
public class   TimeGeneration implements IdGeneration
{
	
	 
	public  Object gen(SessionFactory session,String table, String genName,  java.util.Map params) throws  SQLException
	{
		return System.nanoTime();
			
		
	}
	 

}
