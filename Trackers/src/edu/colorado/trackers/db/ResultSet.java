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
	
	public void close() {
		cursor.close();
	}

	int getCount() {
		return cursor.getCount();
	}
	
	public String getString(int i) {
		return cursor.getString(i);
	}
	
	public Integer getInt(int i) {
		return cursor.getInt(i);
	}
	
	public Double getDouble(int i) {
		return cursor.getDouble(i);
	}

	boolean isNull(int i) {
		return cursor.isNull(i);
	}
	
	public boolean moveToNext() {
		return cursor.moveToNext();
	}
}