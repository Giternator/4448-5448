package edu.colorado.trackers.db;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 *  This is a wrapper class around SQLLite selector functionality
 *  Example: SELECT col1, col2...coln from table where (conditon) order by (column)
 * @author Vikas
 *
 */

public class Selector extends DMLQuery {
	private SQLiteDatabase db;
	private String tableName;
	private String orderByColumn = new String();
	private String cols = new String("*");
	private Cursor cur;
	
    public Selector(SQLiteDatabase database, String table) {
		tableName = table;
		db = database;
	}

    public void addColumns(String columns[]) {
    	if (columns.length == 0) {
    		return;
    	}
    	StringBuilder columnQuery = new StringBuilder();
    	for(String col: columns) {
    		columnQuery.append(col).append(',');
    	}
    	columnQuery.deleteCharAt(columnQuery.length() -1);
    	cols = columnQuery.toString();
    }
    
    public void orderBy(String colName) {
    	orderByColumn = colName;
    }

    @TargetApi(9)
	protected String buildQuery() {
    	StringBuffer sql = new StringBuffer("SELECT ");
        sql.append(cols).append(" from ");
    	sql.append(tableName);	
    	if (!whereClause.isEmpty()) {
    		sql.append(" where ").append(whereClause);
    	}
    	if (!orderByColumn.isEmpty()) {
    		sql.append(" order by ").append(orderByColumn);
    	}	
    	System.out.println("SQL = " + sql.toString());  	
    	return sql.toString();
    }
    
    public int execute() {
    	String sql = buildQuery();
    	cur = db.rawQuery(sql, whereArgs);
    	return cur.getCount();
    }
    
    
    public ResultSet getResultSet() {
    	return new ResultSet(cur);
    }
  
}