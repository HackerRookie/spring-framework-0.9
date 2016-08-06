package com.interface21.transaction.datasource;

import com.interface21.jdbc.datasource.ConnectionHolder;

/**
 * DataSource transaction object, representing a ConnectionHolder.
 * Used as transaction object by DataSourceTransactionManager.
 *
 * @author Juergen Hoeller
 * @since 02.05.2003
 * @see com.interface21.transaction.datasource.DataSourceTransactionManager
 * @see com.interface21.jdbc.datasource.ConnectionHolder
 */
public class DataSourceTransactionObject {

	private final ConnectionHolder connectionHolder;

	private Integer previousIsolationLevel;

	protected DataSourceTransactionObject(ConnectionHolder connectionHolder) {
		this.connectionHolder = connectionHolder;
	}

	public ConnectionHolder getConnectionHolder() {
		return connectionHolder;
	}

	protected void setPreviousIsolationLevel(Integer previousIsolationLevel) {
		this.previousIsolationLevel = previousIsolationLevel;
	}

	public Integer getPreviousIsolationLevel() {
		return previousIsolationLevel;
	}

}
