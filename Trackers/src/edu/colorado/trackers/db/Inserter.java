package edu.colorado.trackers.db;

import java.util.Iterator;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * This is a wrapper class around SQLLite inserter functionality
 * @author Vikas
 *
 */

public class Inserter extends DMLQuery {
	private SQLiteDatabase db;
	private String tableName;

	private ContentValues contentVals = new ContentValues();
	Inserter(SQLiteDatabase database, String table) {
		db = database;
		tableName = table;
	}

    
	@TargetApi(11)
	protected String buildQuery() {
    	StringBuffer sql = new StringBuffer("INSERT into ");
    	sql.append(tableName).append("(");
    	Iterator<String> i = contentVals.keySet().iterator();
    	StringBuffer values = new StringBuffer();
    	String s, val;
    	while(i.hasNext()) {
    		s = i.next();
    		val = contentVals.getAsString(s);
    	    sql.append(s).append(",");
    			values.append("'").append(val).append("'").append(",");
    	}
    	// remove extra , at the end.
    	if(!(contentVals.size() == 0)) {
    		sql.deleteCharAt(sql.length() -1);
    		values.deleteCharAt(values.length() - 1);
    	}
    	
    	sql.append(") values(").append(values.toString()).append(")");
    	
    	if (!whereClause.isEmpty()) {
    		sql.append(" where ").append(whereClause);
    	}
    	
    	System.out.println("SQL = " + sql.toString());  	
    	return sql.toString();
    }
	
    public void columnNameValues(ContentValues con)
    {
    	contentVals = con;
    }
    
	@TargetApi(9)
	public int execute() {
		long ret = 0;
		if (whereClause.isEmpty()) {
		    ret = db.insert(tableName, null, contentVals);
		}
		else {
			Cursor cur = db.rawQuery(buildQuery(), whereArgs);
			ret = cur.getCount();
			cur.close();
		}
		return (int)ret;
	}
	
}
	