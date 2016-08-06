
package com.interface21.aop.interceptor;

import org.aopalliance.MethodInterceptor;
import org.aopalliance.MethodInvocation;

/**
 * Interceptor that throws UnsupportedOperationException
 * on any invocation. Useful during development.
 * 
 * ALSO MOCK INTERCEPTOR: can test
 * 
 * @author Rod Johnson
 */
public class MockObjectInterceptor implements MethodInterceptor {
	
	// CREATE AN EASY MOCK
	
	// How to know when to verify?

	/**
	 * @see Interceptor#invoke(Invocation)
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable {
		String s = invocation.getMethod().getName();
		throw new UnsupportedOperationException("StubInterceptor: '" + s + "' not implemented");
		
	}

}
