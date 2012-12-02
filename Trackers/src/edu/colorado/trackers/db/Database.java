package edu.colorado.trackers.db;

import android.content.Context;

/**
 * This is the class exposed to user and so, any public 
 * methods added in this class should be first go through a 
 * design review.
 * This class should be used to get a inserter, a deleter, 
 * an updater or a selector object.
 * @author Vikas
 *
 */

public class Database {

	// database implementation object.
	private SQLiteDBImpl dbImpl;

	/**
	 * Constructor
	 * @param context   Context object. 
	 * @param dbName    Name of the database 
	 */
	public Database(Context con, String dbName) {
		dbImpl = new SQLiteDBImpl(con, dbName);
	}
	
	/** 
	 * Checks if a table exists in the database
	 * @param tableName	Name of the table
	 * @return
	 */
	public boolean tableExists(String tableName) {
		return dbImpl.tableExists(tableName);
	}

	/**
	 * creates a table in the database
	 * @param tableName	Name of the table to create.
	 * @param cols		Columms to create (example "INTEGER PRIMARY KEY AUTOINCREMENT, 
	 * 												profile TEXT NOT NULL") 
	 */
	public void createTable(String tableName, String cols)	{
		dbImpl.createTable(tableName, cols);
	}
	
	/**
	 * Drops table from a database
	 * @param tableName	Name of the table to drop
	 */
	public void dropTable(String tableName) {
		dbImpl.dropTable(tableName);
	}
	
	/**
	 * Returns a Selector object which can be used to 
	 * select rows from the database.
	 * @param tableName	Name of the table from which to select.
	 * @return
	 */
	public Selector selector(String tableName) {
		return dbImpl.selector(tableName);
	}

	/**
	 * Returns an Inserter object which can be used to
	 * insert rows into database
	 * @param tableName
	 * @return
	 */
	public Inserter inserter(String tableName) {
		return dbImpl.inserter(tableName);
		
	}
	
	/**
	 * Returns a Deleter object which can be used to delete 
	 * rows from a table.
	 * @param table
	 * @return
	 */
	public Deleter deleter(String table) {
		return dbImpl.deleter(table);
	}

	/**
	 * Closes the database connection. Should be called when 
	 * database access is no longer required.
	 */
	public void close() {
		dbImpl.close();
	}
	
}