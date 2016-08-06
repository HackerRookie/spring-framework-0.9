/*
 * The Spring Framework is published under the terms
 * of the Apache Software License.
 */

package com.interface21.ejb.support;

import javax.ejb.EnterpriseBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/** 
 * Superclass for all EJBs.
 * Provides logging support.
 * As javax.ejb.EnterpriseBean is a tag interface, there
 * are no EJB methods to implement.
 * <br>Subclasses will often want to create an object of type
 * JndiEnvironmentBeanFactory, to provide a BeanFactory view
 * of their JNDI environment variables. However, as they may
 * also choose to use another BeanFactory strategy (or not require
 * a bean factory) this class no longer creates a BeanFactory.
 * @see JndiEnvironmentBeanFactory
 * @author Rod Johnson
 * @version $Id: AbstractEnterpriseBean.java,v 1.3 2003/05/28 16:39:13 jhoeller Exp $
 */
public abstract class AbstractEnterpriseBean implements EnterpriseBean {
	
	/**
	 * Logger, available to subclasses
	 */
	protected final Log logger = LogFactory.getLog(getClass());

}
