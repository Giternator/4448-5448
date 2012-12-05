package edu.colorado.trackers.db;

import android.database.Cursor;

/**
 * This class is a wrapper for SQLLite Cursor used for 
 * navigating a result set from select query.
 * @author Vikas
 *
 */

public class ResultSet {

	private Cursor cursor;

	ResultSet(Cursor cur) {
		cursor = cur;
	}
	
	void close() {
		cursor.close();
	}

	int getCount() {
		return cursor.getCount();
	}
	
	String getString(int i) {
		return cursor.getString(i);
	}

	boolean isNull(int i) {
		return cursor.isNull(i);
	}
	
	boolean moveToNext() {
		return cursor.moveToNext();
	}
}