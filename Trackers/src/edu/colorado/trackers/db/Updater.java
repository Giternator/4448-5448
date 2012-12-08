package edu.colorado.trackers.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 *  * This is a wrapper class around SQLLite updater functionality.
 * @author Vikas
 *
 */


public class Updater extends DMLQuery {
	private SQLiteDatabase db;
	private String tableName;
	private ContentValues contentVals = new ContentValues();
	
	Updater(SQLiteDatabase database, String table) {
		db = database;
		tableName = table;
	}
	
	public void columnNameValues(ContentValues con) {
    	contentVals = con;
    }
    
	public int execute() {
		int ret = 0;
		if (whereArgs.length != 0) {		
			ret = db.update(tableName, contentVals, whereClause, whereArgs);
		}
		else  {
			ret = db.update(tableName, contentVals, whereClause, null);
		}
		return ret;
	}
	
}
	