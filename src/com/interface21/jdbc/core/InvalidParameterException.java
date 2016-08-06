package com.interface21.jdbc.core;

import com.interface21.dao.InvalidDataAccessApiUsageException;

/**
 * <P>Exception thrown when a unauthorised method from a ResultSet instance is
 * invoked. Mostly thrown in the unauthorised ReadOnlyResultSet methods in the 
 * JdbcTemplate</P>
 * 
 * @author Yann Caroff
 */
public class InvalidParameterException extends InvalidDataAccessApiUsageException {
   
  private final String param;
  private final String value;

  /**
   * Constructor for InvalidParameterException.
   * @param param
   * @param value
   */
  public InvalidParameterException(String param, String value) {
    super("Parameter [" + param + "] holds an invalid value [" + value + "]");
    this.param = param;
    this.value = value;
  }
  
  /**
   * Returns the exception parameter. Should represent a ResultSet.
   * @return the parameter
   */
  public String getParameter() {
    return param;
  }

  /**
   * Returns the value held by the parameter. Containts any invalid value such
   * as 'null'.
   * @return the value
   */
  public String getValue() {
    return value;
  }
}