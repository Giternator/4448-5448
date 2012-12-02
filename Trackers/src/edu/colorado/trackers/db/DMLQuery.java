package edu.colorado.trackers.db;

/** 
 * This is abstract base class for classes impementing DML Queries
 * (Inserter, Selector, Deleter, Updater
 * @author Vikas
 *
 */

abstract class DMLQuery {
	
	protected String whereClause = new String();
	protected String whereArgs[] = null;
	
    public void where(String wh, String[] args) {
    	whereClause = wh;
    	whereArgs = args;
    }	
    
    public void where(String wh) {
    	whereClause = wh;
    }	    
    
    abstract public int execute();
    
}
