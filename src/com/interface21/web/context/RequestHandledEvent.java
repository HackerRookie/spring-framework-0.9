/**
 * Generic framework code included with 
 * <a href="http://www.amazon.com/exec/obidos/tg/detail/-/1861007841/">Expert One-On-One J2EE Design and Development</a>
 * by Rod Johnson (Wrox, 2002). 
 * This code is free to use and modify. However, please
 * acknowledge the source and include the above URL in each
 * class using or derived from this code. 
 * Please contact <a href="mailto:rod.johnson@interface21.com">rod.johnson@interface21.com</a>
 * for commercial support.
 */

package com.interface21.web.context;

import com.interface21.context.ApplicationEvent;

/**
 * Event raised when a request is handled by our web framework.
 * @author  Rod Johnson
 * @since January 17, 2001
 */
public class RequestHandledEvent extends ApplicationEvent {


	//---------------------------------------------------------------------
	// Instance data
	//---------------------------------------------------------------------
    private String  url;
    private long    timeMillis;
    private String  ip;
	
	/** Usually GET or POST */
	private String	method;
	
	/** Name of the servlet that handled this request is available */
	private String servletName;
	
    private Throwable   failureCause;
	

	//---------------------------------------------------------------------
	// Constructors
	//---------------------------------------------------------------------
    /** Creates a new PageViewEvent */
	public RequestHandledEvent(Object source, String url, long timeMillis, String ip, String method, String servletName) {
        super(source);
        this.url = url;
        this.timeMillis = timeMillis;
        this.ip = ip;
		this.method = method;
		this.servletName = servletName;
	}
    
    public RequestHandledEvent(Object source, String url, long timeMillis, String ip, String method, String servletName, Throwable t) {
       this(source, url, timeMillis, ip, method, servletName);
       failureCause = t;
	}
    
    // TIME TAKEN FOR ITHGER EV£ENT - separatoe????

	//---------------------------------------------------------------------
	// Methods from XXXX interface
	//---------------------------------------------------------------------
    public String getURL() {
        return url;
    }
    
    public long getTimeMillis() {
        return timeMillis;
    }
    
    public String getIP() {
        return ip;
    }
    
    public boolean wasFailure() {
        return failureCause != null;
    }
    
    public Throwable getFailureCause() {
        return failureCause;
    }
	
	public String getMethod() {
		return method;
	}
	
	public String getServletName() {
		return servletName;
	}
    
    public String toString() {
        StringBuffer sb = new StringBuffer("RequestHandledEvent: url=[" + getURL() + "] time=" + getTimeMillis() + "ms");
		sb.append(" ip=" + getIP() + " method='" + getMethod() + "' servletName='" + getServletName() + "'");
		sb.append(" client=" + getIP() + " ");
		sb.append(" status=");
        sb.append(!wasFailure() ? "OK" : "failed: "+ getFailureCause());
        return sb.toString();
    }

}	// class RequestHandledEvent
