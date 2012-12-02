package edu.colorado.trackers.db;

import android.database.sqlite.SQLiteDatabase;

/**
 *  * This is a wrapper class around SQLLite deleter functionality
 * @author Vikas
 *
 */

public class Deleter extends DMLQuery {
	private SQLiteDatabase db;
	private String tableName;

	Deleter(SQLiteDatabase database, String table) {
		db = database;
		tableName = table;
	}
	
	public int execute() {
		int ret = 0;
		if (whereArgs.length == 0) {
			ret = db.delete(tableName, whereClause, null);
		}
		else {
			ret = db.delete(tableName, whereClause, whereArgs);
		}
		return ret;
	}
	
}