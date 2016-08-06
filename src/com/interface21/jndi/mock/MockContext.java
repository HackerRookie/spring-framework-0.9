
package com.interface21.jndi.mock;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameNotFoundException;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.interface21.util.StringUtils;

/**
 * Mock JNDI naming context.
 * Mainly targetted at test environments, but also usable for standalone applications.
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @see MockInitialContextFactoryBuilder
 */
public class MockContext implements Context {

	private static final Log logger = LogFactory.getLog(MockContext.class);

	private Hashtable boundObjects;

	public MockContext() {
		boundObjects = new Hashtable();
	}
	
	public MockContext(Hashtable ht) {
		boundObjects = ht;
	}

	public Object lookup(Name arg0) throws NamingException {
		return null;
	}

	public Object lookup(String name) throws NamingException {
		logger.info("Mock JNDI lookup: '" + name + "'");
		Object found = boundObjects.get(name);
		if (found == null) {
			throw new NameNotFoundException("Name '" + name + "' not bound: " + boundObjects.size() + " bindings -- [" +
			                                StringUtils.collectionToDelimitedString(boundObjects.keySet(), ",") + "]");
		}
		return found;
	}

	public void bind(Name arg0, Object arg1) throws NamingException {
	}

	public void bind(String name, Object obj) throws NamingException {
		logger.info("Mock JNDI binding: '" + name + "' = [" + obj + "]");
		boundObjects.put(name, obj);
	}

	public void rebind(Name arg0, Object arg1) throws NamingException {
	}

	public void rebind(String arg0, Object arg1) throws NamingException {
		boundObjects.put(arg0, arg1);
	}

	public void unbind(Name arg0) throws NamingException {
	}

	public void unbind(String arg0) throws NamingException {
		boundObjects.remove(arg0);
	}

	public void rename(Name arg0, Name arg1) throws NamingException {
	}

	public void rename(String arg0, String arg1) throws NamingException {
		Object obj = boundObjects.remove(arg0);
		boundObjects.put(obj, arg1);
	}

	public NamingEnumeration list(Name arg0) throws NamingException {
		return null;
	}

	public NamingEnumeration list(String arg0) throws NamingException {
		return null;
	}

	public NamingEnumeration listBindings(Name arg0) throws NamingException {
		return null;
	}

	public NamingEnumeration listBindings(String root) throws NamingException {
		logger.debug("Listing bindings under [" + root + "]");
		return new NamingEnumerationImpl(root);
	}

	public void destroySubcontext(Name arg0) throws NamingException {
	}

	public void destroySubcontext(String arg0) throws NamingException {
	}

	public Context createSubcontext(Name arg0) throws NamingException {
		return null;
	}

	public Context createSubcontext(String arg0) throws NamingException {
		return null;
	}

	public Object lookupLink(Name arg0) throws NamingException {
		return null;
	}

	public Object lookupLink(String arg0) throws NamingException {
		return null;
	}

	public NameParser getNameParser(Name arg0) throws NamingException {
		return null;
	}

	public NameParser getNameParser(String arg0) throws NamingException {
		return null;
	}

	public Name composeName(Name arg0, Name arg1) throws NamingException {
		return null;
	}

	public String composeName(String arg0, String arg1) throws NamingException {
		return null;
	}

	public Object addToEnvironment(String arg0, Object arg1) throws NamingException {
		return null;
	}

	public Object removeFromEnvironment(String arg0) throws NamingException {
		return null;
	}

	public Hashtable getEnvironment() throws NamingException {
		return null;
	}

	public void close() throws NamingException {
	}

	public String getNameInNamespace() throws NamingException {
		return null;
	}
	
	
	/**
	 * Bit of a hack, but the aim was to do the least possible
	 * to get this working.
	 */
	private class NamingEnumerationImpl implements NamingEnumeration {
		
		private Iterator itr;
		
		public NamingEnumerationImpl(String root) {
			Iterator i = boundObjects.keySet().iterator();
			List l = new LinkedList();
			// Create list
			while (i.hasNext()) {
				String name = (String) i.next();
				 if (name.indexOf(root) != -1) {
				 	 String strippedName = name.substring(root.length() + 1);
				 	 Binding b = new Binding(strippedName, boundObjects.get(name));
					 l.add(b);
				 }
			}
			this.itr = l.iterator();
		}
		
		public void close() throws NamingException {
		}

		public boolean hasMore() throws NamingException {
			return itr.hasNext();
		}

		public Object next() throws NamingException {
			return (Binding) itr.next();
		}

		public boolean hasMoreElements() {
			return itr.hasNext();
		}

		public Object nextElement() {
			return (Binding) itr.next();
		}
	}

}
