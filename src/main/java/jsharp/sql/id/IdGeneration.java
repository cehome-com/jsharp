package jsharp.sql.id;

import java.sql.SQLException;

import jsharp.sql.SessionFactory;

public interface   IdGeneration
{
	
	 
	Object gen(SessionFactory session,String table, String genName,  java.util.Map params)throws SQLException;
	 

}