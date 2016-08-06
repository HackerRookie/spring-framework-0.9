
package com.interface21.util;


/**
 * Utility class for diagnostic purposes to analyze the
 * ClassLoader hierarchy for any object.
 * @author  Rod Johnson
 * @since 02 April 2001
 */
public class ClassLoaderAnalyzer {

    //---------------------------------------------------------------------
    // Public methods
    //---------------------------------------------------------------------
    /** Show the class loader hierarchy for this class
     * @param obj object to analyze loader hierarchy for
     * @param role a description of the role of this class in the application
     * (e.g., "servlet" or "EJB reference")
     * @param delim line break
     * @param tabText text to use to set tabs
     * @return a String showing the class loader hierarchy for this class
     */
    public static String showClassLoaderHierarchy(Object obj, String role, String delim, String tabText) {
        String s = "object of " + obj.getClass() + ": role is " + role + delim;
        return s + showClassLoaderHierarchy(obj.getClass().getClassLoader(), delim, tabText, 0);
    }

     /** Show the class loader hierarchy for this class
      * @param cl class loader to analyze hierarchy for
      * @param delim line break
      * @param tabText text to use to set tabs
      * @param indent nesting level (from 0) of this loader; used in pretty printing
      * @return a String showing the class loader hierarchy for this class
      */
    public static String showClassLoaderHierarchy(ClassLoader cl, String delim, String tabText, int indent) {
        if (cl == null) {
        	String s = "null classloader " + delim;
        	ClassLoader ctxcl = Thread.currentThread().getContextClassLoader();
            s += "Context class loader=" + ctxcl + " hc=" + ctxcl.hashCode();
            return s;
        }
        String s = "";//"ClassLoader: ";
        for (int i = 0; i < indent; i++)
            s += tabText;
        s += cl + " hc=" + cl.hashCode() + delim;
        ClassLoader parent = cl.getParent();
        return s + showClassLoaderHierarchy(parent, delim, tabText, indent + 1);
    }
    
    
//    public static void getClassLoaderHierarchy(ClassLoader cl, List l) {
//        if (cl == null)
//            l.add( "null classloader");
//        //s += cl + " hc=" + cl.hashCode() + delim;
//        ClassLoader parent = cl.getParent();
//       l.append(parent);
//    }

    public static void main(String[] args) {
        System.out.println(showClassLoaderHierarchy(new String(), "new String", "<br>", "-"));
    }

}	// class ClassLoaderAnalyzer

