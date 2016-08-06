package com.interface21.jndi.mock;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.InitialContextFactoryBuilder;
import javax.naming.spi.NamingManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Mock JNDI naming context builder.
 *
 * <p>Mainly targetted at test environments, but also usable for standalone
 * applications. Typically used for binding a JDBC DataSource to a well-known JNDI
 * location, to be able to use J2EE data access code outside of a J2EE container.
 *
 * <p>There are various choices for mock DataSource implementations:
 * - SingleConnectionDataSource (using the same Connection for all getConnection calls);
 * - DriverManagerDataSource (creating a new Connection on each getConnection call);
 * - Apache's Jakarta Commons DBCP offers BasicDataSource (a real pool).
 *
 * @author Juergen Hoeller
 * @see #init
 * @see #bind
 * @see MockContext
 * @see com.interface21.jdbc.datasource.SingleConnectionDataSource
 * @see com.interface21.jdbc.datasource.DriverManagerDataSource
 */
public class MockInitialContextFactoryBuilder implements InitialContextFactoryBuilder {

	private static Log logger = LogFactory.getLog(MockInitialContextFactoryBuilder.class);

	private static Hashtable boundObjects = new Hashtable();

	/**
	 * Initialize the context builder by registering it with the JNDI NamingManager.
	 * @throws NamingException if there's already a naming context builder
	 * registered with the JNDI NamingManager
	 */
	public static void init() throws NamingException {
		NamingManager.setInitialContextFactoryBuilder(new MockInitialContextFactoryBuilder());
	}

	/**
	 * Bind the given object under the given name, for all naming contexts
	 * that this context builder will generate.
	 * @param name the JNDI name of the object (e.g. "java:comp/env/jdbc/myds")
	 * @param obj the object to bind (e.g. a DataSource implementation)
	 */
	public static void bind(String name, Object obj) {
		logger.info("Static JNDI binding: '" + name + "'='" + obj + "'");
		boundObjects.put(name, obj);
	}


	public MockInitialContextFactoryBuilder() {
		super();
	}
	
	public InitialContextFactory createInitialContextFactory(Hashtable props) throws NamingException {
		return new MockInitialContextFactory();
	}


	private class MockInitialContextFactory implements InitialContextFactory {
		
		public MockInitialContextFactory() {
		}
		
		public Context getInitialContext(Hashtable newProps) throws NamingException {
			// no newProps support, to be able to share the main Hashtable instance
			return new MockContext(boundObjects);
		}
	}

}
