package edu.colorado.trackers.shoppinglist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ShoppingListDB extends SQLiteOpenHelper {
	
	private static final int version = 1;

	public ShoppingListDB(Context context) {
		super(context, "shoppinglist.db", null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE shopping_list_items " +
				"(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"name TEXT NOT NULL, " +
				"price DOUBLE NOT NULL, " +
				"quantity INTEGER NOT NULL);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS shopping_list_items");
		onCreate(db);
	}

}
