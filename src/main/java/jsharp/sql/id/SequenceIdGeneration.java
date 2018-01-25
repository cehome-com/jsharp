package jsharp.sql.id;

import java.sql.SQLException;

import jsharp.sql.SessionFactory;

 
public class   SequenceIdGeneration implements IdGeneration
{
	
	 
	public  Object gen(SessionFactory session,String table, String genName,  java.util.Map params) throws  SQLException
	{
		//Executor executor=null;
				java.sql.ResultSet rs=null;
					if(genName==null || genName.length()==0)
					{
						
						genName="sequence_websharp";
					}
					//executor=session.queryValue( "select "+genName+".nextval from dual");
					//rs=executor.query(null); 
					 
					rs.next();
					return rs.getObject(1);
		 
		
	}
	 

}
