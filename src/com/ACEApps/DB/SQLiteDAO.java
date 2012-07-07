package com.ACEApps.DB;

import java.sql.SQLException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDAO {
	
	//Global column names
	public static final String COL_ID = "_id";
	public static final String COL_DATE = "date";
	public static final String COL_DESC = "description";
	
	private static final String DATABASE_NAME = "Partyinviter_db";
	private static final String DATABASE_TABLE = "PartyInviter_table";
	private static final int DATABASE_VERSION = 1;
	
	private DbHelper pi_dbHelper;
	private final Context pi_context;
	private SQLiteDatabase pi_database;
	
	private static class DbHelper extends SQLiteOpenHelper{

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" +
					COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					COL_DATE + " STRING NOT NULL, " +
					COL_DESC + " STRING NOT NULL);"					
					);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}		
	}
	
	public SQLiteDAO(Context _context){
		pi_context = _context;
	}
	
	public SQLiteDAO open() throws SQLException{
		pi_dbHelper = new DbHelper(pi_context);
		pi_database = pi_dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		pi_dbHelper.close();
	}

	public long create(String date, String description) {
		ContentValues cv = new ContentValues();
		cv.put(COL_DATE, date);
		cv.put(COL_DESC, description);
		return pi_database.insert(DATABASE_TABLE, null, cv);
	}
	
	public String getData(){
		String[] columns = new String[]{ COL_ID, COL_DATE, COL_DESC}; 
		Cursor c = pi_database.query(DATABASE_TABLE, columns, null, null, null, null, null);
		String result = "";
		
		int iRow = c.getColumnIndex(COL_ID);
		int iDate = c.getColumnIndex(COL_DATE);
		int iDesc = c.getColumnIndex(COL_DESC);
		
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result = result + c.getString(iRow) + " " + c.getString(iDate) + " " + c.getString(iDesc) + "\n";			
		}		
		return result;		
	}
}