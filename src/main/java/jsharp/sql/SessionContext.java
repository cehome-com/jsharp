package jsharp.sql;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map; //import javax.transaction.Synchronization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link CurrentConnectionContext} impl which scopes the notion of current
 * Connection by the current thread of execution. Unlike the JTA counterpart,
 * threads do not give us a nice hook to perform any type of cleanup making it
 * questionable for this impl to actually generate Connection instances. In the
 * interest of usability, it was decided to have this default impl actually
 * generate a Connection upon first request and then clean it up after the
 * {@link org.hibernate.Transaction} associated with that Connection is
 * committed/rolled-back. In order for ensuring that happens, the Connections
 * generated here are unusable until after {@link Connection#beginTransaction()}
 * has been called. If <tt>close()</tt> is called on a Connection managed by this
 * class, it will be automatically unbound.
 * <p/>
 * Additionally, the static {@link #bind} and {@link #unbind} methods are
 * provided to allow application code to explicitly control opening and closing
 * of these Connections. This, with some from of interception, is the preferred
 * approach. It also allows easy framework integration and one possible approach
 * for implementing long-Connections.
 * <p/>
 * The {@link #buildOrObtainConnection}, {@link #isAutoCloseEnabled},
 * {@link #isAutoFlushEnabled}, {@link #getConnectionReleaseMode}, and
 * {@link #buildCleanupSynch} methods are all provided to allow easy subclassing
 * (for long-running Connection scenarios, for example).
 * 
 * @author Steve Ebersole
 */
public class SessionContext
{

	private static final Logger log = LoggerFactory.getLogger(SessionContext.class);

	/**
	 * A ThreadLocal maintaining current Connections for the given execution
	 * thread. The actual ThreadLocal variable is a java.util.Map to account for
	 * the possibility for multiple SessionFactorys being used during execution
	 * of the given thread.
	 */
	private static final ThreadLocal context = new ThreadLocal();

	protected final SessionFactory factory;

	public SessionContext(SessionFactory factory)
	{
		this.factory = factory;
	}

	/**
	 * {@inheritDoc}
	 */
	public final Connection currentConnection() throws Exception
	{
		Connection current = existingConnection(factory);
		if (current == null)
		{
			current = buildOrObtainConnection();
			// register a cleanup synch
			// current.getTransaction().registerSynchronization(
			// buildCleanupSynch() );
			// wrap the Connection in the transaction-protection proxy

			// then bind it
			doBind(current, factory);
		}
		/*
		 * else if(current.getConnection()==null ||
		 * current.getConnection().isClosed()) { current =
		 * buildOrObtainConnection(); doBind( current, factory ); } current =
		 * buildOrObtainConnection(); doBind( current, factory );
		 */
		return current;
	}

	/**
	 * Getter for property 'factory'.
	 * 
	 * @return Value for property 'factory'.
	 */
	protected SessionFactory getFactory()
	{
		return factory;
	}

	/**
	 * Strictly provided for subclassing purposes; specifically to allow
	 * long-Connection support.
	 * <p/>
	 * This implementation always just opens a new Connection.
	 * 
	 * @return the built or (re)obtained Connection.
	 */
	protected Connection buildOrObtainConnection() throws Exception
	{
		return factory.getConnection();
	}

	/**
	 * Unassociate a previously bound Connection from the current thread of
	 * execution.
	 * 
	 * @return The Connection which was unbound.
	 */
	public static Connection unbind(SessionFactory factory)
	{
		return doUnbind(factory, true);
	}

	public static Connection existingConnection(SessionFactory factory)
	{
		Map ConnectionMap = ConnectionMap();
		if (ConnectionMap == null)
		{
			return null;
		}
		else
		{
			return (Connection) ConnectionMap.get(factory);
		}
	}

	protected static Map ConnectionMap()
	{
		return (Map) context.get();
	}

	private static void doBind(Connection connection, SessionFactory factory)
	{
		Map connectionMap = ConnectionMap();
		if (connectionMap == null)
		{
			connectionMap = new HashMap();
			context.set(connectionMap);
		}
		connectionMap.put(factory, connection);
	}

	private static Connection doUnbind(SessionFactory factory, boolean releaseMapIfEmpty)
	{
		Map connectionMap = ConnectionMap();
		Connection connection = null;
		if (connectionMap != null)
		{
			connection = (Connection) connectionMap.remove(factory);
			if (releaseMapIfEmpty && connectionMap.isEmpty())
			{
				context.set(null);
			}
		}
		return connection;
	}

}
