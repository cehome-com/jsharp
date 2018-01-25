package jsharp.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import jsharp.support.DataMapListHandler;
import jsharp.support.DataMapPageHandler;
import jsharp.support.DataRowProcessor;
import jsharp.util.DataMap;
import jsharp.util.DataValue;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSQueryRunner extends QueryRunner {

	protected static final Logger logger = LoggerFactory.getLogger(JSQueryRunner.class);
	//protected DataSource ds;
	SessionFactory sessionFactory;
	private final transient SessionContext currentConnectionContext;
	public JSQueryRunner(DataSource ds,SessionFactory sessionFactory){
		super(ds,true);
		this.sessionFactory=sessionFactory;
		currentConnectionContext = new SessionContext(sessionFactory);
	}
	/*public SessionFactory(DataSource ds) {
		super(ds, true);
		currentConnectionContext = new SessionContext(this);
	}*/

	/**
	 * begin context connection and set autoCommit=false
	 * @throws Exception
     */
	public void beginTrans() throws Exception {

		currentConnectionContext.currentConnection().setAutoCommit(false);

	}

	/* (non-Javadoc)
	 * @see jsharp.sql.ddddd#commitTrans()
	 */
	/* (non-Javadoc)
	 * @see jsharp.sql.qq#commitTrans()
	 */
	public void commitTrans() throws Exception {

		currentConnectionContext.currentConnection().commit();
		closeSession();

	}

	/* (non-Javadoc)
	 * @see jsharp.sql.ddddd#rollbakTrans()
	 */
	/* (non-Javadoc)
	 * @see jsharp.sql.qq#rollbakTrans()
	 */
	public void rollbakTrans() {
		try {
			currentConnectionContext.currentConnection().rollback();
		} catch (Exception e) {
			// System.out.println(e);
		} finally {
			closeSession();
		}

	}

	/**
	 * init a context connection with autoCommit=true (if exists  then do nothing)
	 * @throws Exception
     */
	public void openSession() throws Exception {

		currentConnectionContext.currentConnection().setAutoCommit(true);
	}

	/**
	 * close context connection
	 */
	public void closeSession() {

		Connection currentSession = currentConnectionContext.existingConnection(sessionFactory);
		if (currentSession != null) {
			try {
				currentSession.close();
			} catch (Exception e) {
			}

			currentConnectionContext.unbind(sessionFactory);
		}
	}

	
	  /* (non-Javadoc)
	 * @see jsharp.sql.ddddd#getDataSource()
	 */
	/* (non-Javadoc)
	 * @see jsharp.sql.qq#getDataSource()
	 */
	 
	/**
	 * @throws SQLException 
	 * 
	 */
	protected Connection prepareConnection() throws SQLException   {
		if (this.getDataSource() == null) {
			throw new SQLException("QueryRunner requires a DataSource to be " + "invoked in this way, or a Connection should be passed in");
		}
		//如果执行过openSession操作，则conn存在，则多次利用,否则
		Connection conn = currentConnectionContext.existingConnection(sessionFactory);
		if (conn == null) conn = getConnection();
		return conn;
	}
	  protected void close(Connection conn) throws SQLException   {
		  
		  Connection contextConn = currentConnectionContext.existingConnection(sessionFactory);
		  if(contextConn==null){
			  if(!conn.getAutoCommit())conn.commit(); //
			  DbUtils.close(conn);
		  }
	    }
	
 
	public Connection getConnection() throws SQLException   {
		return this.getDataSource().getConnection();
	}
	
	public long[] insert(String tableName, DataMap map,boolean returnKey) throws SQLException   {
		if (map == null || map.size() == 0) return new long[]{0,0};
		String sql = "insert into " + tableName + "({0}) values({1})";
		String s1 = "";
		String s2 = "";
		ArrayList<Object> arrayList = new ArrayList<Object>();
		for (Map.Entry<String, Object> e : map.entrySet()) {
			if (s1 != "") {
				s1 += ",";
				s2 += ",";
			}
			s1 += e.getKey();
			if ((e.getValue() instanceof SqlValue) || map.getType(e.getKey()) == DataValue.TYPE_TEXT) {
				s2 += e.getValue();
			}else{
				arrayList.add(e.getValue());
				s2 += "?";
			}
		}

		MessageFormat fmt = new MessageFormat(sql);
		Object[] args = { s1, s2 };
		sql = fmt.format(args);
		if(returnKey){
		
			return insertAndReturnKey( true,   sql, arrayList.toArray(new Object[] {}));
		}
		else{
			return new long[]{this.update(sql, arrayList.toArray(new Object[] {})),0};
		}
 
		 
	}
	 private long[] insertAndReturnKey( boolean closeConn, String sql, Object... params) throws SQLException   {
		  
	        PreparedStatement stmt = null;
	        ResultSet rs=null;
	    	Connection conn=null;
	        try {
	        	conn=this.prepareConnection();
	            stmt = conn.prepareStatement( sql,Statement.RETURN_GENERATED_KEYS);
	            this.fillStatement(stmt, params);
	            int rows= stmt.executeUpdate();
	            rs = stmt.getGeneratedKeys();                                  // 获取自增主键！
	            if (rs.next()) return new long[]{rows, rs.getLong(1)};
	            else return  new long[]{rows,0};
	             
	        } catch (SQLException e) {
	            this.rethrow(e, sql, params);

	        } finally {
	        	DbUtils.closeQuietly(rs);
	        	DbUtils.closeQuietly(stmt);
	            if (closeConn) {
	                close(conn);
	            }
	        }

	        return new long[]{0,0};
	    }
	
 


}
