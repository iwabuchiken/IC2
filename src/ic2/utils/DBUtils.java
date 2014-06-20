package ic2.utils;




import ic2.items.CL;
import ic2.items.Item;
import ic2.items.Word;
import ic2.main.MainActv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
//import android.view
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/****************************************
 * Copy & pasted from => C:\WORKS\WORKSPACES_ANDROID\ShoppingList\src\shoppinglist\main\DBManager.java
 ****************************************/
public class DBUtils extends SQLiteOpenHelper{

	/*****************************************************************
	 * Class fields
	 *****************************************************************/
	/*----------------------------
	 * 1. DB-related
	 * 2. Table names, folder names
	 * 3. Activity, context
	 * 4. Columns, types
		----------------------------*/
	/*----------------------------
	 * 1. DB-related
		----------------------------*/
	// Database
	SQLiteDatabase db = null;
	
	 // DB name
//	final static String dbName = "CM.db";
	
	/*----------------------------
	 * 2. Table names, folder names
		----------------------------*/
	// Table names
	public final static String mainTableName = "main_table";
	
	public static String currentTableName = null;
	
	public static String baseDirName = "";
	
	/*----------------------------
	 * 3. Activity, context
		----------------------------*/
	// Activity
	Activity activity;
	
	//
	Context context;
	
	/*----------------------------
	 * 4. Columns, types
		----------------------------*/
	// Main table
	public static final String[] cols_main_table = {
		"file_name", "file_path", 
		"duration", 
		"date_added", "date_modified",
		"file_info", "memos",
		"located_at"
	};
	
	public static final String[] types_main_table = {
		"TEXT", "TEXT", 
//		"TEXT UNIQUE", "TEXT",
		"INTEGER", 
		"INTEGER", "INTEGER",
		"TEXT", "TEXT",
		"TEXT"
	};
	
	
	/*****************************************************************
	 * Constructor
	 *****************************************************************/
	public DBUtils(Context context, String dbName) {
		super(context, dbName, null, 1);
		
		// Initialize activity
		this.activity = (Activity) context;
		
		this.context = context;
		
//		this.dbName = dbName;
		
	}//public DBUtils(Context context)

//	public DBUtils() {
//		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
//	}

	/*******************************************************
	 * Methods
	 *******************************************************/
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		
	}//public void onCreate(SQLiteDatabase db)

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		
	}

	/****************************************
	 * createTable_generic()
	 * 
	 * <Caller> 
	 * 1. 
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public boolean createTable(
					SQLiteDatabase db, String tableName, String[] columns, String[] types) {
		/*----------------------------
		 * Steps
		 * 1. Table exists?
		 * 2. Build sql
		 * 3. Exec sql
			----------------------------*/
		
		//
//		if (!tableExists(db, tableName)) {
		if (tableExists(db, tableName)) {
			// Log
			Log.d("DBManager.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table exists => " + tableName);
			
			return false;
		}//if (!tableExists(SQLiteDatabase db, String tableName))
		
		/*----------------------------
		 * 2. Build sql
			----------------------------*/
		//
		StringBuilder sb = new StringBuilder();
		
		sb.append("CREATE TABLE " + tableName + " (");
		sb.append(android.provider.BaseColumns._ID +
							" INTEGER PRIMARY KEY AUTOINCREMENT, ");
		
		// created_at, modified_at
		sb.append("created_at INTEGER, modified_at INTEGER, ");
		
		int i = 0;
		for (i = 0; i < columns.length - 1; i++) {
			sb.append(columns[i] + " " + types[i] + ", ");
		}//for (int i = 0; i < columns.length - 1; i++)
		
		sb.append(columns[i] + " " + types[i]);
		
		sb.append(");");
		
		// Log
		Log.d("DBManager.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "sql => " + sb.toString());
		
		/*----------------------------
		 * 3. Exec sql
			----------------------------*/
		//
		try {
//			db.execSQL(sql);
			db.execSQL(sb.toString());
			
			// Log
			Log.d(this.getClass().getName() + 
					"["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table created => " + tableName);
			
			
			return true;
		} catch (SQLException e) {
			// Log
			Log.d(this.getClass().getName() + 
					"[" + Thread.currentThread().getStackTrace()[2].getLineNumber() + "]", 
					"Exception => " + e.toString());
			
			return false;
		}//try
		
	}//public boolean createTable(SQLiteDatabase db, String tableName)

	public boolean tableExists(SQLiteDatabase db, String tableName) {
		// The table exists?
		Cursor cursor = db.rawQuery(
									"SELECT * FROM sqlite_master WHERE tbl_name = '" + 
									tableName + "'", null);
		
		((Activity) context).startManagingCursor(cursor);
//		actv.startManagingCursor(cursor);
		
		// Judge
		if (cursor.getCount() > 0) {
			return true;
		} else {//if (cursor.getCount() > 0)
			return false;
		}//if (cursor.getCount() > 0)
	}//public boolean tableExists(String tableName)

	public boolean dropTable(Activity actv, SQLiteDatabase db, String tableName) {
		/*------------------------------
		 * The table exists?
		 *------------------------------*/
		// The table exists?
		boolean tempBool = tableExists(db, tableName);
		
		if (tempBool == true) {
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table exists: " + tableName);
			
//			Methods.recordLog(actv, 
//					"DBUtils.java", 
//					"Table exists: " + tableName);
			
		} else {//if (tempBool == true)
			// Log
			Log.e("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table doesn't exist: " + tableName);

//			Methods.recordLog(actv, 
//					"DBUtils.java", 
//					"Table doesn't exist: " + tableName);

			return false;
		}

		/*------------------------------
		 * Drop the table
		 *------------------------------*/
		// Define the sql
        String sql 
             = "DROP TABLE " + tableName;
        
        // Execute
        try {
			db.execSQL(sql);
			
			// Vacuum
			db.execSQL("VACUUM");
			
			// Log
			Log.d("DBManager.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "The table dropped => " + tableName);
			
			// Return
			return true;
			
		} catch (SQLException e) {
			// TODO �����������ꂽ catch �u���b�N
			// Log
			Log.e("DBManager.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "DROP TABLE => failed (table=" + tableName + "): " + e.toString());
			
			// debug
			Toast.makeText(actv, 
						"DROP TABLE => failed(table=" + tableName, 
						3000).show();
			
			// Return
			return false;
		}//try

	}//public boolean dropTable(String tableName) 

	public boolean insertData(SQLiteDatabase db, String tableName, 
												String[] columnNames, String[] values) {
		
////		String sql = "SELECT * FROM TABLE " + DBUtils.table_name_memo_patterns;
//		String sql = "SELECT * FROM " + DBUtils.table_name_memo_patterns;
//		
//		Cursor c = db.rawQuery(sql, null);
//		
//		
//		
//		// Log
//		Log.d("DBUtils.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "c.getCount() => " + c.getCount() + " / " +
//				"c.getColumnCount() => " + c.getColumnCount());
//		
//		c.close();
		
		
		/*----------------------------
		* 1. Insert data
		----------------------------*/
		try {
			// Start transaction
			db.beginTransaction();
			
			// ContentValues
			ContentValues val = new ContentValues();
			
			// Put values
			for (int i = 0; i < columnNames.length; i++) {
				val.put(columnNames[i], values[i]);
			}//for (int i = 0; i < columnNames.length; i++)
			
			// Insert data
			db.insert(tableName, null, val);
			
			// Set as successful
			db.setTransactionSuccessful();
			
			// End transaction
			db.endTransaction();
			
			// Log
//			Log.d("DBUtils.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "Data inserted => " + "(" + columnNames[0] + " => " + values[0] + 
//				" / " + columnNames[3] + " => " + values[3] + ")");
			
			return true;
		} catch (Exception e) {
			// Log
			Log.e("DBUtils.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Exception! => " + e.toString());
			
			return false;
		}//try
		
//		//debug
//		return false;
		
	}//public insertData(String tableName, String[] columnNames, String[] values)

	public boolean insertData(SQLiteDatabase db, String tableName, 
											String[] columnNames, long[] values) {
		/*----------------------------
		* 1. Insert data
		----------------------------*/
		try {
			// Start transaction
			db.beginTransaction();
			
			// ContentValues
			ContentValues val = new ContentValues();
			
			// Put values
			for (int i = 0; i < columnNames.length; i++) {
				val.put(columnNames[i], values[i]);
			}//for (int i = 0; i < columnNames.length; i++)
			
			// Insert data
			db.insert(tableName, null, val);
			
			// Set as successful
			db.setTransactionSuccessful();
			
			// End transaction
			db.endTransaction();
			
			// Log
			Log.d("DBUtils.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "Data inserted => " + "(" + columnNames[0] + " => " + values[0] + "), and others");
			
			return true;
		} catch (Exception e) {
			// Log
			Log.e("DBUtils.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "Exception! => " + e.toString());
			
			return false;
		}//try
	}//public insertData(String tableName, String[] columnNames, String[] values)

	public boolean insertData(SQLiteDatabase db, String tableName, 
			String[] columnNames, Object[] values) {
		/*----------------------------
		* 1. Insert data
		----------------------------*/
		try {
			// Start transaction
			db.beginTransaction();
			
			// ContentValues
			ContentValues val = new ContentValues();
			
			// Put values
			val.put(columnNames[0], (String) values[0]);		// file_name
			val.put(columnNames[1], (String) values[1]);		// file_path
			
			val.put(columnNames[2], (Long) values[2]);		// duration
			
			val.put(columnNames[3], (Long) values[3]);		// date_added
			val.put(columnNames[4], (Long) values[4]);		// date_modified
			
			val.put(columnNames[5], (String) values[5]);		// file_info
			val.put(columnNames[6], (String) values[6]);		// memos
			
			val.put(columnNames[7], (String) values[7]);		// located_at
			
//			for (int i = 0; i < columnNames.length; i++) {
//			val.put(columnNames[i], values[i]);
//			}//for (int i = 0; i < columnNames.length; i++)
			
			// Insert data
			db.insert(tableName, null, val);
			
			// Set as successful
			db.setTransactionSuccessful();
			
			// End transaction
			db.endTransaction();
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Transaction => Ends");
			
			
			return true;
		
		} catch (Exception e) {
			// Log
			Log.e("DBUtils.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "Exception! => " + e.toString());
			
			return false;
		}//try
		
		////debug
		//return false;
		
	}//public insertData(String tableName, String[] columnNames, String[] values)

	public boolean insertData_genre(SQLiteDatabase wdb, String genre_name) {
		/*----------------------------
		* 1. Insert data
		----------------------------*/
		long created_at = Methods.getMillSeconds_now();
		long modified_at = Methods.getMillSeconds_now();

		try {
			// Start transaction
			wdb.beginTransaction();
			
			// ContentValues
			ContentValues val = new ContentValues();
			
			// Put values
			val.put("created_at", created_at);		// file_name

			val.put("modified_at", modified_at);		// date_added
			val.put(MainActv.cols_genres[0], genre_name);		// date_modified
			
			// Insert data
			wdb.insert(MainActv.tableName_genres, null, val);
			
			// Set as successful
			wdb.setTransactionSuccessful();
			
			// End transaction
			wdb.endTransaction();
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Transaction => Ends");
			
			
			return true;
		
		} catch (Exception e) {
			// Log
			Log.e("DBUtils.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "Exception! => " + e.toString());
			
			return false;
		}//try
		
		////debug
		//return false;
		
	}//public boolean insertData_genre

	public boolean 
	insertData_list
	(SQLiteDatabase wdb, String list_name, int genre_id) {
		/*----------------------------
		* 1. Insert data
		----------------------------*/
		long created_at = Methods.getMillSeconds_now();
		long modified_at = Methods.getMillSeconds_now();

		try {
			// Start transaction
			wdb.beginTransaction();
			
			// ContentValues
			ContentValues val = new ContentValues();
			
			// Put values
			val.put("created_at", created_at);		// 
			val.put("modified_at", modified_at);		//
			
			val.put(CONS.DB.cols_check_lists[0], list_name);		// 
			val.put(CONS.DB.cols_check_lists[1], genre_id);		//
			
			// Insert data
			wdb.insert(CONS.DB.tname_Check_Lists, null, val);
			
			// Set as successful
			wdb.setTransactionSuccessful();
			
			// End transaction
			wdb.endTransaction();
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Transaction => Ends");
			
			
			return true;
		
		} catch (Exception e) {
			// Log
			Log.e("DBUtils.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "Exception! => " + e.toString());
			
			return false;
		}//try
		
		////debug
		//return false;
		
	}//public boolean insertData_list
	
	public boolean 
	insertData_list
	(SQLiteDatabase wdb, String list_name, int genre_id, String yomi) {
		/*----------------------------
		 * 1. Insert data
		----------------------------*/
		long created_at = Methods.getMillSeconds_now();
		long modified_at = Methods.getMillSeconds_now();
		
		try {
			// Start transaction
			wdb.beginTransaction();
			
			// ContentValues
			ContentValues val = new ContentValues();
			
//			"name",	"genre_id", "yomi"	// 0,1,2
			
			// Put values
			val.put("created_at", created_at);		// 
			val.put("modified_at", modified_at);		//
			
			val.put(CONS.DB.cols_check_lists[0], list_name);		// 
			val.put(CONS.DB.cols_check_lists[1], genre_id);		//
			val.put(CONS.DB.cols_check_lists[2], yomi);		//
			
			// Insert data
			wdb.insert(CONS.DB.tname_Check_Lists, null, val);
			
			// Set as successful
			wdb.setTransactionSuccessful();
			
			// End transaction
			wdb.endTransaction();
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Transaction => Ends");
			
			
			return true;
			
		} catch (Exception e) {
			// Log
			Log.e("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception! => " + e.toString());
			
			return false;
		}//try
		
		////debug
		//return false;
		
	}//public boolean insertData_list

	public boolean insertData_item(SQLiteDatabase wdb, Object[] data) {
		/*----------------------------
		* 1. Insert data
		----------------------------*/
		long created_at = Methods.getMillSeconds_now();
		long modified_at = Methods.getMillSeconds_now();

//		"text", "serial_num",	"list_id"
		
		try {
			// Start transaction
			wdb.beginTransaction();
			
			// ContentValues
			ContentValues val = new ContentValues();
			
			// Put values
			val.put("created_at", created_at);		// 
			val.put("modified_at", modified_at);		//
			
			val.put(CONS.DB.col_name_Items[0], (String) data[0]);	// 
			val.put(CONS.DB.col_name_Items[1], (Integer) data[1]);	//
			val.put(CONS.DB.col_name_Items[2], (Long) data[2]);	//
			
			// Insert data
			wdb.insert(CONS.DB.tname_items, null, val);
			
			// Set as successful
			wdb.setTransactionSuccessful();
			
			// End transaction
			wdb.endTransaction();
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Transaction => Ends");
			
			
			return true;
		
		} catch (Exception e) {
			// Log
			Log.e("DBUtils.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "Exception! => " + e.toString());
			
			return false;
		}//try
		
		////debug
		//return false;
		
	}//public boolean insertData_item
	
	public boolean updateData(SQLiteDatabase wdb, String tableName) {
		/*----------------------------
		* 1. Insert data
		----------------------------*/
		try {
			// Start transaction
//			wdb.beginTransaction();
			
			// ContentValues
			ContentValues val = new ContentValues();
			
			// Put values
//			val.put(DBUtils.cols_main_table[0], fi.getFile_name());		// file_name
			
//			for (int i = 0; i < columnNames.length; i++) {
//			val.put(columnNames[i], values[i]);
//			}//for (int i = 0; i < columnNames.length; i++)
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Update => Done");
			
			return true;
		
		} catch (Exception e) {
			// Log
			Log.e("DBUtils.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "Exception! => " + e.toString());
			
			return false;
		}//try
		
		////debug
		//return false;
		
	}//public insertData(String tableName, String[] columnNames, String[] values)

	public boolean deleteData(Activity actv, SQLiteDatabase db, String tableName, long file_id) {
		/*----------------------------
		 * Steps
		 * 1. Item exists in db?
		 * 2. If yes, delete it
			----------------------------*/
		/*----------------------------
		 * 1. Item exists in db?
			----------------------------*/
		boolean result = isInDB_long(db, tableName, file_id);
		
		if (result == false) {		// Result is false ==> Meaning the target data doesn't exist
											//							in db; Hence, not executing delete op
			
			// debug
			Toast.makeText(actv, 
					"Data doesn't exist in db: " + String.valueOf(file_id), 
					2000).show();
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", 
					"Data doesn't exist in db => Delete the data (file_id = " + String.valueOf(file_id) + ")");
			
			return false;
			
		}//if (result == false)
		
		
		String sql = 
						"DELETE FROM " + tableName + 
						" WHERE file_id = '" + String.valueOf(file_id) + "'";
		
		try {
			db.execSQL(sql);
			
//			// Log
//			Log.d("DBUtils.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Data deleted => file id = " + String.valueOf(file_id));
			
			
			return true;
			
		} catch (SQLException e) {
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
			return false;
			
		}//try
		
	}//public boolean deleteData(SQLiteDatabase db, String tableName, long file_id)

	/****************************************
	 *
	 * 
	 * <Caller> 
	 * 1. deleteData(Activity actv, SQLiteDatabase db, String tableName, long file_id)
	 * 
	 * <Desc> 
	 * 1. REF=> http://stackoverflow.com/questions/3369888/android-sqlite-insert-unique
	 * 
	 * <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public boolean isInDB_long(SQLiteDatabase db, String tableName, long file_id) {
		
		String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE file_id = '" +
						String.valueOf(file_id) + "'";
		
		long result = DatabaseUtils.longForQuery(db, sql, null);
		
		// Log
		Log.d("DBUtils.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "result => " + String.valueOf(result));
		
		if (result > 0) {

			return true;
			
		} else {//if (result > 0)
			
			return false;
			
		}//if (result > 0)
		
//		return false;
		
	}//public boolean isInDB_long(SQLiteDatabase db, String tableName, long file_id)

	public static boolean
	isInTable
	(Activity actv, SQLiteDatabase db,
			String tableName, String colName, String value) {
		
		String sql = "SELECT * FROM " + tableName + " WHERE " + colName + " = '" + value + "'";
		
		Cursor c = db.rawQuery(sql, null);
		
		actv.startManagingCursor(c);
		
		return c.getCount() > 0 ? true : false;
		
	}//public static boolean isInTable
	
	public static boolean
	isInTable
	(Activity actv, SQLiteDatabase db,
			String tableName, String colName, long value) {
		
		String sql =
				"SELECT * FROM " + tableName
				+ " WHERE " + colName + " = '" + value + "'";
		
		//debug
		// Log
		Log.d("[" + "DBUtils.java : "
				+ +Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ " : "
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]", "db.getPath()=" + db.getPath());
		
		Cursor c = db.rawQuery(sql, null);
		
		actv.startManagingCursor(c);
		
		return c.getCount() > 0 ? true : false;
		
	}//public static boolean isInTable

	/****************************************
	 *
	 * 
	 * <Caller> 
	 * 1. Methods.updateData(Activity actv, String tableName, FileItem fi)
	 * 
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public boolean updateData_memos(SQLiteDatabase wdb, String tableName) {
		/*----------------------------
		* Steps
		* 1. 
		----------------------------*/
//		String sql = "UPDATE " + tableName + " SET " + 
//			
//			"file_name='" + fi.getFile_name() + "', " +
//			"file_path='" + fi.getFile_path() + "', " +
//			
//			
//			" WHERE file_name = '" + fi.getFile_name() + "'";

		try {
		
//			wdb.execSQL(sql);
			
//			// Log
//			Log.d("DBUtils.java" + "["
//			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//			+ "]", "sql => Done: " + sql);
			
//			Methods.toastAndLog(actv, "Data updated", 2000);
			
			return true;
		
		
		} catch (SQLException e) {
			// Log
//			Log.d("DBUtils.java" + "["
//			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//			+ "]", "Exception => " + e.toString() + " / " + "sql: " + sql);
			
			return false;
		}
		
	}//public void updateData_memos

	public static boolean updateData_items(Activity actv, String dbName,
			String tableName, HashMap<Long,Integer> data) {
		/*********************************
		 * 1. Set up db
		 * 2. Exec query
		 * 
		 * 3. Close db
		 * 
		 * 4. Return
		 *********************************/
		DBUtils dbu = new DBUtils(actv, dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		Set<Long> keys = data.keySet();
		
//		StringBuilder sb = new StringBuilder();
//		
//		sb.append("UPDATE " + tableName + " SET ");
		
		for (Long k : keys) {
			// Sql
			StringBuilder sb = new StringBuilder();
			
			sb.append("UPDATE " + tableName + " SET ");
			
			sb.append(CONS.DB.col_name_Items[1] + "='" + data.get(k) + "'");
			
			sb.append(" WHERE " + android.provider.BaseColumns._ID + "='" + k + "'");
			
			String sql = sb.toString();
			
			// Exec
			try {
				
				wdb.execSQL(sql);
				
				// Log
				Log.d("DBUtils.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "sql => Done: " + sql);
				
		//		Methods.toastAndLog(actv, "Data updated", 2000);
				
//							return true;
			
			
			} catch (SQLException e) {

				Log.e("DBUtils.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Exception => " + e.toString() + " / " + "sql: " + sql);
			
			}

		}//for (Long k : keys)
		
		/*********************************
		 * 3. Close db
		 *********************************/
		wdb.close();
		
		/*********************************
		 * 4. Return
		 *********************************/
		return true;
		
		
//		for (int i = 0; i < data.size(); i++) {
//			
//			sb.append(CONS.DB.cols_items[1] + "='" + )
//			
////			sb.append(android.provider.BaseColumns._ID + "='")
//			
//		}//for (int i = 0; i < data.size(); i++)
		
//			"file_name='" + fi.getFile_name() + "', " +
//			"file_path='" + fi.getFile_path() + "', " +
//			
//			
//			" WHERE file_name = '" + fi.getFile_name() + "'";
	
//		try {
//		
//	//		wdb.execSQL(sql);
//			
//	//		// Log
//	//		Log.d("DBUtils.java" + "["
//	//		+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//	//		+ "]", "sql => Done: " + sql);
//			
//	//		Methods.toastAndLog(actv, "Data updated", 2000);
//			
//			return true;
//		
//		
//		} catch (SQLException e) {
//			// Log
//	//		Log.d("DBUtils.java" + "["
//	//		+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//	//		+ "]", "Exception => " + e.toString() + " / " + "sql: " + sql);
//			
//			return false;
//		}
		
	}//public static boolean updateData_items()

	public static boolean updateData_items_text(Activity actv, String dbName,
			String tableName, long item_id, String new_text) {
		/*********************************
		 * 1. Set up db
		 * 1-2. Build sql
		 * 
		 * 2. Exec query
		 * 
		 * 3. Close db
		 * 
		 * 4. Return
		 *********************************/
		DBUtils dbu = new DBUtils(actv, dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		/*********************************
		 * 1-2. Build sql
		 *********************************/
		// Sql
		StringBuilder sb = new StringBuilder();
		
		sb.append("UPDATE " + tableName + " SET ");
		
		sb.append(CONS.DB.col_name_Items[0] + "='" + new_text + "'");
		
		sb.append(" WHERE " + android.provider.BaseColumns._ID + "='" + item_id + "'");
		
		String sql = sb.toString();
		
		// Exec
		try {
			
			wdb.execSQL(sql);
			
			// Log
			Log.d("DBUtils.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "sql => Done: " + sql);
			
		} catch (SQLException e) {

			Log.e("DBUtils.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "Exception => " + e.toString() + " / " + "sql: " + sql);
			
			wdb.close();
			
			return false;
		
		}
		
		/*********************************
		 * 3. Close db
		 *********************************/
		wdb.close();
		
		/*********************************
		 * 4. Return
		 *********************************/
		return true;
		
	}//public static boolean updateData_items_text()

	
	public static boolean
	updateData_items_status
	(Activity actv, String dbName,
			String tableName, long db_id, int status_num) {
		/*********************************
		 * 1. Set up db
		 * 1-2. Build sql
		 * 
		 * 2. Exec query
		 * 
		 * 3. Close db
		 * 
		 * 4. Return
		 *********************************/
		DBUtils dbu = new DBUtils(actv, dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		/*********************************
		 * 1-2. Build sql
		 *********************************/
		// Sql
		StringBuilder sb = new StringBuilder();
		
		sb.append("UPDATE " + tableName + " SET ");
		
		sb.append(CONS.DB.col_name_Items[3] + "='" + status_num + "'");
		
		sb.append(" WHERE " + android.provider.BaseColumns._ID + "='" + db_id + "'");
		
		String sql = sb.toString();
		
		// Exec
		try {
			
			wdb.execSQL(sql);
			
//			// Log
//			Log.d("DBUtils.java" + "["
//			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//			+ "]", "sql => Done: " + sql);
			
		} catch (SQLException e) {

			Log.e("DBUtils.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "Exception => " + e.toString() + " / " + "sql: " + sql);
			
			wdb.close();
			
			return false;
		
		}
		
		
		return true;
		
	}//public boolean updateData_items_status()
	
	public static boolean
	updateData_Items_Status_All
	(Activity actv, List<Item> iList) {
		/*********************************
		 * 1. Set up db
		 * 1-2. Build sql
		 * 
		 * 2. Exec query
		 * 
		 * 3. Close db
		 * 
		 * 4. Return
		 *********************************/
		DBUtils dbu = new DBUtils(actv, CONS.DB.dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		/*********************************
		 * 1-2. Build sql
		 *********************************/
		// Sql
		StringBuilder sb = new StringBuilder();
		
		for (Item item : iList) {
			
			sb.append("UPDATE " + CONS.DB.tname_items + " SET ");
			
			sb.append(CONS.DB.col_name_Items[3] + "='" + item.getStatus() + "'");
			
			sb.append(" WHERE "
						+ android.provider.BaseColumns._ID + "='" 
						+ item.getDb_id() + "'");
			
			try {
				
				wdb.execSQL(sb.toString());
				
			} catch (SQLException e) {
				
				StringBuilder tmp = new StringBuilder();
				
				tmp.append("DBUtils.java");
				tmp.append("[");
				tmp.append(Thread.currentThread().getStackTrace()[2].getLineNumber());
				tmp.append("]");
				
				Log.e(tmp.toString(), 
						"Exception => " 
						+ e.toString() 
						+ " / " + "sql: " + sb.toString());
				
//				wdb.close();
				
//				return false;
				
			} finally {
				
				sb.delete(0, sb.length());
				
			}
			
		}//for (Item item : iList)
		
		wdb.close();
		
		
//		sb.append("UPDATE " + tableName + " SET ");
		
//		sb.append(CONS.DB.cols_items[3] + "='" + status_num + "'");
		
//		sb.append(" WHERE " + android.provider.BaseColumns._ID + "='" + db_id + "'");
		
//		String sql = sb.toString();
		
//		// Exec
//		try {
//			
//			wdb.execSQL(sql);
//			
////			// Log
////			Log.d("DBUtils.java" + "["
////			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
////			+ "]", "sql => Done: " + sql);
//			
//		} catch (SQLException e) {
//			
//			Log.e("DBUtils.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Exception => " + e.toString() + " / " + "sql: " + sql);
//			
//			wdb.close();
//			
//			return false;
//			
//		}
		
		
		return true;
		
	}//public boolean updateData_items_status()

	public static boolean update_items_all_to_zero(Activity actv, String dbName,
			String tableName, long check_list_id) {
		/*********************************
		 * memo
		 *********************************/
		DBUtils dbu = new DBUtils(actv, dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		StringBuilder sb = new StringBuilder();
		
		sb.append("UPDATE " + tableName + " SET ");
		
		sb.append(CONS.DB.col_name_Items[3] + "='" + 0 + "'");
		
		sb.append(" WHERE " + CONS.DB.col_name_Items[2] + "='" + check_list_id + "'");
		
		String sql = sb.toString();
		
		// Exec
		try {
			
			wdb.execSQL(sql);
			
			// Log
			Log.d("DBUtils.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "sql => Done: " + sql);
			
			wdb.close();
			
			return true;
			
		} catch (SQLException e) {

			Log.e("DBUtils.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "Exception => " + e.toString() + " / " + "sql: " + sql);
			
			wdb.close();
			
			return false;
			
		}//try
		
	}//public static boolean update_items_all_to_zero()

	/*********************************
	 * delete_list
	 * @param check_list 
	 * @return	true<br/>
	 * 			false => (1)The check list doesn't exist in the db, or<br/>
	 * 					(2) Deletion failed: Check list, or<br/>
	 * 					(3) Deletion failed: Items
	 *********************************/
	public static boolean
	delete_list(Activity actv, long check_list_id, CL check_list) {
		
		/*********************************
		 * Setup: Db
		 *********************************/
		DBUtils dbu = new DBUtils(actv, CONS.DB.dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		//debug
		
		
		/*********************************
		 * Delete: Check list
		 *********************************/
		boolean result = delete_list__1_check_list(actv, wdb, check_list_id);
		
		if (result == false) {
			
			wdb.close();
			
			return false;
			
		}//if (result == false)
		
		/*********************************
		 * Delete: Items
		 *********************************/
		result = delete_list__2_items(actv, wdb, check_list_id);

		if (result == false) {
			
			wdb.close();
			
			return false;
			
		}//if (result == false)
		
		wdb.close();
		
		/*********************************
		 * Delete: Item in CLList
		 *********************************/
		result = CONS.MainActv.CLList.remove(check_list);
		
		if (result == false) {
			
			// Log
			Log.d("["
					+ "DBUtils.java : "
					+ +Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + " : "
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "Remove from CLList => Failed");
			
			return false;
			
		}//if (result == false)
		
		/*********************************
		 * Notify the adapter
		 *********************************/
		CONS.MainActv.mlAdp.notifyDataSetChanged();
		
		/*********************************
		 * Return
		 *********************************/
		return true;
		
	}//delete_list(Activity actv, long check_list_id)

	/*********************************
	 * delete_list__1_items
	 * @return	true	=> Items deleted<br/>
	 * 			false	=> SQL exception
	 *********************************/
	private static boolean
	delete_list__2_items
	(Activity actv,SQLiteDatabase wdb, long check_list_id) {

		/*********************************
		 * Delete: Items
		 *********************************/
		String sql = 
				"DELETE FROM " + CONS.DB.tname_items + 
				" WHERE " + CONS.DB.col_name_Items[2] + " = '"
				+ String.valueOf(check_list_id) + "'";
		
		// Log
		Log.d("DBUtils.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "sql=" + sql);
		
		try {
			
			wdb.execSQL(sql);
			
			// debug
			Toast.makeText(actv, "Items deleted", Toast.LENGTH_LONG).show();
			
			// Log
			Log.d("["
					+ "DBUtils.java : "
					+ +Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + " : "
							+ Thread.currentThread().getStackTrace()[2].getMethodName()
							+ "]", "Items deleted: " + String.valueOf(check_list_id));
			
			return true;
			
		} catch (SQLException e) {
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
			return false;
			
		}//try

	}//delete_list__1_items

	/*********************************
	 * delete_list__1_check_list
	 * @return	true => List deleted<br/>
	 * 			false => (1)List doesn't exist, or<br/>
	 * 					(2)Deletion failed
	 *********************************/
	private static boolean
	delete_list__1_check_list
	(Activity actv, SQLiteDatabase wdb, long check_list_id) {
		/*********************************
		 * Validate: Check list exists?
		 *********************************/
		boolean result = DBUtils.isInTable(
				actv,
				wdb,
				CONS.DB.tname_Check_Lists,
				android.provider.BaseColumns._ID,
				check_list_id);
		
		if (result == false) {		// Result is false ==> Meaning the target data doesn't exist
		//							in db; Hence, not executing delete op
			
			// debug
			Toast.makeText(actv, 
					"Check list doesn't exist in db: " + String.valueOf(check_list_id), 
					Toast.LENGTH_LONG).show();
			
			// Log
			Log.d("DBUtils.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", 
				"Check list doesn't exist in db: " + String.valueOf(check_list_id));
			
			return false;
		
		} else {//if (result == false)
		
		// Log
		Log.d("DBUtils.java" + "["
		+ Thread.currentThread().getStackTrace()[2].getLineNumber()
		+ "]", 
		"Check list exists in db(id=" + String.valueOf(check_list_id) + ")");
		
		}//if (result == false)
		
		/*********************************
		* Delete: Check list
		*********************************/
		String sql = 
			"DELETE FROM " + CONS.DB.tname_Check_Lists + 
			" WHERE " + android.provider.BaseColumns._ID + " = '"
			+ String.valueOf(check_list_id) + "'";
		
		// Log
		Log.d("DBUtils.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "sql=" + sql);
		
		try {
			
			wdb.execSQL(sql);
			
			// debug
			Toast.makeText(actv, "List deleted", Toast.LENGTH_LONG).show();
			
			// Log
			Log.d("["
					+ "DBUtils.java : "
					+ +Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + " : "
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "List deleted: " + String.valueOf(check_list_id));
			
			return true;
		
		} catch (SQLException e) {
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
			return false;
		
		}//try
		
	}//delete_list__1_check_list

	public int updateData_CheckList
	(Activity actv, SQLiteDatabase wdb,
		String tnameChecklists,
		long dbId, String colYomi, String yomi) {
		
		/*----------------------------
		* Steps
		* 1. 
		----------------------------*/
		String sql = "UPDATE " + tnameChecklists
					+ " SET "
					+ colYomi + "='"
					+ yomi + "'"
//					+ " WHERE file_id = '" + dbId + "'";
					+ " WHERE "
					+ android.provider.BaseColumns._ID + " = '"
					+ dbId + "'";
		
		try {
			
			wdb.execSQL(sql);
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "sql => Done: " + sql);
			
			//Methods.toastAndLog(actv, "Data updated", 2000);
			
			return CONS.RetVal.DB_UPDATE_SUCCESSFUL;
			
			
		} catch (SQLException e) {

			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]",
					"Exception => " + e.toString() + " / " + "sql: " + sql);
			
			return CONS.RetVal.EXCEPTION_SQL;
			
		}//try

		
	}//public int updateData_CheckList
	
	public static boolean updateData_CheckList
	(Activity actv,
			long dbId, String col_Name, String col_value) {

		DBUtils dbu = new DBUtils(actv, CONS.DB.dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
//		cols_check_lists
//		"name",	"genre_id", "yomi"	// 0,1,2
		
		String sql = "UPDATE " + CONS.DB.tname_Check_Lists
				+ " SET "
				+ col_Name + "='"
				+ col_value + "'"
//					+ " WHERE file_id = '" + dbId + "'";
				+ " WHERE "
				+ android.provider.BaseColumns._ID + " = '"
				+ dbId + "'";
		
		try {
			
			wdb.execSQL(sql);
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "sql => Done: " + sql);
			
			//Methods.toastAndLog(actv, "Data updated", 2000);
			wdb.close();
			
			return true;
			
			
		} catch (SQLException e) {
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]",
					"Exception => " + e.toString() + " / " + "sql: " + sql);
			
			wdb.close();
			
			return false;
			
		}//try
		
		
	}//public int updateData_CheckList

	public static boolean 
	updateData_CheckList__GenreID
	(Activity actv, long db_id, int genre_id) {
		// TODO Auto-generated method stub
		
		DBUtils dbu = new DBUtils(actv, CONS.DB.dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
//		cols_check_lists
//		"name",	"genre_id", "yomi"	// 0,1,2
		
		String sql = "UPDATE " + CONS.DB.tname_Check_Lists
				+ " SET "
				+ CONS.DB.cols_check_lists[1] + "='"
				+ genre_id + "'"
//					+ " WHERE file_id = '" + dbId + "'";
				+ " WHERE "
				+ android.provider.BaseColumns._ID + " = '"
				+ db_id + "'";
		
		try {
			
			wdb.execSQL(sql);
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "sql => Done: " + sql);
			
			//Methods.toastAndLog(actv, "Data updated", 2000);
			
			wdb.close();
			
			return true;
			
			
		} catch (SQLException e) {
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]",
					"Exception => " + e.toString() + " / " + "sql: " + sql);
			
			wdb.close();
			
			return false;
			
		}//try
		
		
	}//updateData_CheckList__GenreID

	public static boolean 
	updateData_CheckList__All
	(Activity actv, CL list) {
		// TODO Auto-generated method stub
		////////////////////////////////

		// check list

		////////////////////////////////
//		CL cl = Methods.get_clList_from_db_id(actv, list.getDb_id());
		
		if (list == null) {
			
			// Log
			String msg_Log = "the parameter check list => null";
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", msg_Log);
			
			return false;
			
		}
		
		////////////////////////////////

		// sql

		////////////////////////////////
		String sql = _build_Sql_CL_Full(actv, list);
		
		// Log
		String msg_Log = "sql = " + sql;
		Log.d("DBUtils.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", msg_Log);
		
		
//		String sql = "UPDATE " + CONS.DB.tname_Check_Lists
//				+ " SET "
//				+ CONS.DB.cols_check_lists[1] + "='"
//				+ genre_id + "'"
////					+ " WHERE file_id = '" + dbId + "'";
//				+ " WHERE "
//				+ android.provider.BaseColumns._ID + " = '"
//				+ db_id + "'";

		////////////////////////////////

		// db

		////////////////////////////////
		DBUtils dbu = new DBUtils(actv, CONS.DB.dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		try {
			
			wdb.execSQL(sql);
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "sql => Done: " + sql);
			
			//Methods.toastAndLog(actv, "Data updated", 2000);
			
			wdb.close();
			
			return true;
			
			
		} catch (SQLException e) {
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]",
					"Exception => " + e.toString() + " / " + "sql: " + sql);
			
			wdb.close();
			
			return false;
			
		}//try
		
	}//updateData_CheckList__All

	private static String
	_build_Sql_CL_Full
	(Activity actv, CL cl) {
		// TODO Auto-generated method stub
//		"name",	"genre_id", "yomi"	// 0,1,2
		StringBuilder sb= new StringBuilder();
		
		sb.append("UPDATE ");
		sb.append(CONS.DB.tname_Check_Lists);
		sb.append(" SET ");
		
		//REF "," http://stackoverflow.com/questions/808418/sqliite-update-two-columns answered Apr 30 '09 at 18:36
		sb.append(CONS.DB.cols_check_lists[1]);
		sb.append(" = '");
		sb.append(cl.getGenre_id());
		sb.append("' , ");
		
		sb.append(CONS.DB.cols_check_lists[0]);
		sb.append(" = '");
		sb.append(cl.getName());
		sb.append("' , ");
		
		sb.append(CONS.DB.cols_check_lists[2]);
		sb.append(" = '");
		sb.append(cl.getYomi());
		sb.append("'");
		
		
		sb.append(" WHERE ");
		sb.append(android.provider.BaseColumns._ID);
		sb.append(" = ");
		sb.append(cl.getDb_id());

		
		return sb.toString();
		
	}//_build_Sql_CL_Full

	public static List<Item> 
	get_Items_from_CL
	(Activity actv, long cl_DbId) {
		// TODO Auto-generated method stub
		
		DBUtils dbu = new DBUtils(actv, CONS.DB.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();
		
//		String tname	= CONS.DB.tname_Check_Lists;
//		String fields[]	= CONS.DB.cols_check_lists_FULL;
//		String where	= 
//					CONS.DB.cols_check_lists_FULL[
//					     Methods.getArrayIndex(
//					    		 CONS.DB.cols_check_lists_FULL,
//					    		 "yomi")]
//					+ " is null";
////		+ " = "
////		+ "?";
//		
//		String args[]	= new String[]{
//				
////				"null"
////				null
//				""
//		};
		
		//
//		String sql = "SELECT * FROM " + CONS.DB.tname_Check_Lists;
		
//		"text", "serial_num",			// 0,1
//		"list_id", "status"				// 2,3

		String where = CONS.DB.col_name_Items[2]
						+ " = ?";
//		+ " = "
//		+ cl_DbId;
		
		String[] params = new String[]{
				
				String.valueOf(cl_DbId)
				
		};
		
		Cursor c = null;
		
		try {
			
//			c = rdb.rawQuery(sql, null);
			c = rdb.query(
						CONS.DB.tname_items, 
						CONS.DB.col_name_Items_full, 
						where, 
						params, 
//						null, 
						null, null, null);
//			c = rdb.query(tname, fields, where, args, null, null, null);
		
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "c.getCount()=" + c.getCount());
			
		} catch (Exception e) {
			// Log
			Log.e("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
			rdb.close();
			
			return null;
		}
	
		/*********************************
		 * Query => No records?
		 *********************************/
		if (c.getCount() < 1) {
			
			// Log
			Log.d("["
					+ "DBUtils.java : "
					+ +Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + " : "
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "Query => No records");
			
			return null;
			
		}
		
		/*********************************
		 * Get names
		 *********************************/
//		List<String> itemNames = new ArrayList<String>();
//		
//		List<Long> itemIds = new ArrayList<Long>();
		
		List<Item> itemList = new ArrayList<Item>();
		
		c.moveToFirst();
		
//		int numOfSamples = CONS.DB.GetYomi_ChunkNum;
//		int numOfSamples = 5;
//		int numOfSamples = 10;
		
		/***************************************
		 * Counter: Count 1 each time when a new entry 
		 * 				is made into wordList
		 ***************************************/
		int counter = 0;
//		int numOfSamples = c.getCount();

//		"INTEGER", 				// 0
//		"INTEGER", "INTEGER",	// 1,2
//		
//		"TEXT", "INTEGER",		// 3,4
//		"INTEGER", "INTEGER"	// 5,6
		for (int i = 0; i < c.getCount(); i++) {

			Item item = new Item.Builder()
						.setDb_id(c.getLong(0))
						.setCreated_at(c.getLong(1))
						.setModified_at(c.getLong(2))
						.setText(c.getString(3))
						.setSerial_num(c.getInt(4))
						.setList_id(c.getInt(5))
						.setStatus(c.getInt(6))
						.build();
			
			itemList.add(item);
			
			/*********************************
			 * Next row in the cursor
			 *********************************/
			c.moveToNext();
			
		}//for (int i = 0; i < 10; i++)
		
		
//		// Log
//		Log.d("DBUtils.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ ":"
//				+ Thread.currentThread().getStackTrace()[2].getMethodName()
//				+ "]", "itemList.size()=" + itemList.size());
		
		rdb.close();		
		
		return itemList;
		
	}//get_Items_from_CL

	public static CL 
	get_CL_from_Name
	(Activity actv, String listName_new) {
		// TODO Auto-generated method stub
		
		DBUtils dbu = new DBUtils(actv, CONS.DB.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();
		
//		public static String[] cols_check_lists_FULL = {
//			
//			android.provider.BaseColumns._ID,	// 0
//			"created_at", "modified_at",		// 1,2
//			"name",	"genre_id", "yomi"			// 3,4,5
//		};

		
		String where = CONS.DB.cols_check_lists_FULL[3]
				+ " = ?";
		//+ " = "
		//+ cl_DbId;
		
		String[] params = new String[]{
				
				listName_new
				
		};
		
		Cursor c = null;
		
		try {
			
		//	c = rdb.rawQuery(sql, null);
			c = rdb.query(
						CONS.DB.tname_Check_Lists, 
						CONS.DB.cols_check_lists_FULL, 
						where, 
						params, 
		//				null, 
						null, null, null);
		//	c = rdb.query(tname, fields, where, args, null, null, null);
		
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "c.getCount()=" + c.getCount());
			
		} catch (Exception e) {
			// Log
			Log.e("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
			rdb.close();
			
			return null;
		}
		
		/*********************************
		 * Query => No records?
		 *********************************/
		if (c.getCount() < 1) {
			
			// Log
			Log.d("["
					+ "DBUtils.java : "
					+ +Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + " : "
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "Query => No records");
			
			return null;
			
		}
		
		/*********************************
		 * Get names
		 *********************************/
		c.moveToFirst();
		
		/***************************************
		 * Counter: Count 1 each time when a new entry 
		 * 				is made into wordList
		 ***************************************/
//		public static String[] cols_check_lists_FULL = {
//		
//		android.provider.BaseColumns._ID,	// 0
//		"created_at", "modified_at",		// 1,2
//		"name",	"genre_id", "yomi"			// 3,4,5
//	};
		
		CL cl = new CL.Builder()
				.setDb_id(c.getLong(0))
				.setCreated_at(c.getLong(1))
				.setModified_at(c.getLong(2))
				.setName(c.getString(3))
				.setGenre_id(c.getInt(4))
				.setYomi(c.getString(5))
				.build();
		
		
		
		//// Log
		//Log.d("DBUtils.java" + "["
		//		+ Thread.currentThread().getStackTrace()[2].getLineNumber()
		//		+ ":"
		//		+ Thread.currentThread().getStackTrace()[2].getMethodName()
		//		+ "]", "itemList.size()=" + itemList.size());
		
		rdb.close();	
		
		return cl;
		
	}//get_CL_from_Name

}//public class DBUtils

