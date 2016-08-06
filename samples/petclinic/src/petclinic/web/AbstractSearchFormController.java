/*
 * AbstractSearchFormController.java
 *
 */

package petclinic.web;

import com.interface21.validation.BindException;
import com.interface21.web.servlet.ModelAndView;
import com.interface21.web.servlet.mvc.SimpleFormController;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

/**
 *  Form controller that should be subclassed to provide a search capability.
 *  In addition to the "formView" and "successView" properties supplied by
 *  <code>SimpleFormController</code>, it adds a "selectView" property.
 *  Subclasses must provide an implementation of the abstract "search" method
 *  that will return a <code>List</code>.
 *
 *  <p>If there are no validation errors, the view that is displayed will depend
 *  upon the number of elements in the returned <code>List</code>:
 *  0 elements displays "formView",
 *  exactly 1 element diplays "successView",
 *  more than 1 element displays "selectView".
 * 
 *  @author  Ken Krebs
 */
public abstract class AbstractSearchFormController extends SimpleFormController {
    
    /** Holds value of property selectView. */
    private String selectView;
    
    /** 
     * Create a new instance of AbstractSearchFormController.
     * <p>Subclasses should set the following properties, either in the
	 * constructor or via a BeanFactory: formView, successView, selectView.
     * @see com.interface21.web.servlet.mvc.SimpleFormController
	 * @see #setFormView
	 * @see #setSuccessView
	 * @see #setSelectView
     */
    public AbstractSearchFormController() {
        super();
    }
    
    /**
     * Set the name of the view that should be used for selection display.
     */
    public final void setSelectView(String selectView) {
        this.selectView = selectView;
    }
    
    /**
     * Return the name of the view that should be used for selection display.
     */
    protected final String getSelectView() {
        return this.selectView;
    }
    
    /**
     * This implementation processes the results of the "search" method,
     * displaying the correct view depending upon the contents of the 
     * returned <code>List</code>. This should not normally need to be overridden.
	 * @param request current servlet request
	 * @param response current servlet response
	 * @param command form object with request parameters bound onto it
	 * @param errors binder without errors (subclass can add errors if it wants to)
	 * @return the prepared model and view
	 * @throws ServletException in case of invalid state or arguments
	 * @throws IOException in case of I/O errors
	 * @see #onSubmit(Object)
	 * @see #showForm
     */
    protected ModelAndView onSubmit(HttpServletRequest request,	HttpServletResponse response,
    Object command,	BindException errors) throws ServletException, IOException {
        List results = search(command);
        if(results.size() < 1) {
            errors.reject("error.notFound", null, "not found");
            return showForm(request, response, errors);
        }
        if(results.size() > 1) {
            if (getSelectView() == null)
                throw new ServletException("selectView isn't set");
            return new ModelAndView(getSelectView(), "selections", results);
        }
        return onSubmit(results.get(0));
    }
    
	/**
	 * Template callback method that subclasses must implement to perform the search.
	 * @param command object with embedded search information
	 * @return the results in <code>List</code> form
	 */
    protected abstract List search(Object command);
    
}
