
package com.interface21.core;

/**
 * Interface to be implemented by exceptions that have
 * a root cause.
 * This enables exceptions that don't share a common superclass
 * to expose a root cause consistently.
 * This will no longer be necessary in Java 1.4,
 * although it won't be incompatible.
 * @author  Rod Johnson
 * @version $Id: HasRootCause.java,v 1.1.1.1 2003/02/11 08:10:18 johnsonr Exp $
 */
public interface HasRootCause {
	
	/** 
	 * Return the root cause of this exception
	 * @return the root cause of this exception,
	 * or null if there was no root cause
	 */
	Throwable getRootCause();

}

