package ic2.utils;



import ic2.adapters.ItemListAdapter;
import ic2.items.CL;
import ic2.items.Item;
import ic2.listeners.dialog.DB_CL;
import ic2.listeners.dialog.DB_TL;
import ic2.listeners.dialog.DialogListener;
import ic2.listeners.dialog.DOI_CL;
import ic2.main.CheckActv;
import ic2.main.MainActv;
import ic2.main.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.os.Looper;

import org.apache.commons.lang.StringUtils;

public class Methods {

	static int counter;		// Used => sortFileList()

	static Activity actv_methods;
	
	/****************************************
	 * Enums
	 ****************************************/
	public static enum DialogButtonTags {
		// Generics
		dlg_generic_dismiss, dlg_generic_dismiss_second_dialog, dlg_generic_dismiss_third_dialog,

		// dlg_register_genre.xml
		dlg_register_genre_bt_ok,

		// dlg_register_list.xml
		dlg_register_list_bt_ok,
		
		// dlg_register_item.xml
		dlg_rgstr_item_bt_ok,

		// dlg_checkactv_change_serial_num_btn_ok.xml
		dlg_checkactv_change_serial_num_btn_ok,
		
		// dlg_checkactv_edit_item_text_btn_ok.xml
		dlg_checkactv_edit_item_text_btn_ok,
		
		// dlg_Edit_List_Title
		dlg_edit_list_title_btn_ok,
		
		// dlg_change_genre
		dlg_change_genre_btn_ok,
		
		// dlg_edit_cl.xml
		dlg_edit_cl_btn_ok, dlg_edit_cl_btn_cancel,
		
		
	}//public static enum DialogButtonTags
	
	public static enum DialogItemTags {
		// dlg_register.xml
		dlg_register_lv,
		
		// dlg_checkactv_long_click.xml
		dlg_checkactv_long_click_lv,
		
		// dlg_filter_by_genre.xml
		DLG_FILTER_BY_GENRE_LV,
		
		// dlg_main_actv_long_click.xml
		DLG_MAINACTV_LONGCLICK_LV,
		
		dlg_db_admin_lv,
		
		dlg_sort_list_lv,

	}//public static enum DialogItemTags
	
	
	public static enum ButtonTags {
		// MainActivity.java
		ib_up,
		
		// DBAdminActivity.java
		db_manager_activity_create_table, db_manager_activity_drop_table, 
		db_manager_activity_register_patterns,
		
		// thumb_activity.xml
		thumb_activity_ib_back, thumb_activity_ib_bottom, thumb_activity_ib_top,
		
		// image_activity.xml
		image_activity_back,
		
		// TIListAdapter.java
		tilist_cb,

		// actv_check.xml
		actv_check_bt_add, actv_check_bt_top, actv_check_bt_bottom,
		
		
	}//public static enum ButtonTags
	
	public static enum ItemTags {
		
		// MainActivity.java
		dir_list,
		
		// ThumbnailActivity.java
		dir_list_thumb_actv,
		
		// Methods.java
		dir_list_move_files,
		
		// TIListAdapter.java
		tilist_checkbox,
		
		
	}//public static enum ItemTags

	public static enum MoveMode {
		// TIListAdapter.java
		ON, OFF,
		
	}//public static enum MoveMode

	public static enum PrefenceLabels {
		
		CURRENT_PATH,
		
		thumb_actv,
		
		chosen_list_item,
		
	}//public static enum PrefenceLabels

	public static enum ListTags {
		// MainActivity.java
		actv_main_lv,

		// CheckActv.java
		actv_check_lv,

	}//public static enum ListTags

	public static enum LongClickTags {
		// MainActivity.java
		main_activity_list,
		
		
	}//public static enum LongClickTags

	
	/****************************************
	 * Vars
	 ****************************************/
	public static final int vibLength_click = 35;

	static int tempRecordNum = 20;

	/****************************************
	 * Methods
	 ****************************************/
	public static void sortFileList(File[] files) {
		// REF=> http://android-coding.blogspot.jp/2011/10/sort-file-list-in-order-by-implementing.html
		/*----------------------------
		 * 1. Prep => Comparator
		 * 2. Sort
			----------------------------*/
		/*----------------------------
		 * 1. Prep => Comparator
			----------------------------*/
		Comparator<? super File> filecomparator = new Comparator<File>(){
			
			public int compare(File file1, File file2) {
				/*----------------------------
				 * 1. Prep => Directory
				 * 2. Calculate
				 * 3. Return
					----------------------------*/
				
				int pad1=0;
				int pad2=0;
				
				if(file1.isDirectory())pad1=-65536;
				if(file2.isDirectory())pad2=-65536;
				
				int res = pad2-pad1+file1.getName().compareToIgnoreCase(file2.getName());
				
				return res;
			} 
		 };//Comparator<? super File> filecomparator = new Comparator<File>()
		 
		/*----------------------------
		 * 2. Sort
			----------------------------*/
		Arrays.sort(files, filecomparator);

	}//public static void sortFileList(File[] files)

	public static boolean sort_list_CLList(Activity actv, List<CL> cLList) {
		/*********************************
		 * 1. Comaparator
		 * 2. Sort
		 *********************************/
		actv_methods = actv;
		
		/*********************************
		 * 1. Comaparator
		 *********************************/
		Comparator<CL> comp = new Comparator<CL>(){

			public int compare(CL c1, CL c2) {
				/*********************************
				 * 1. Get genre name
				 * 2. Null?
				 * 
				 * 3. Genre names => Not equal?
				 * 4. Genre names => Equal?
				 *********************************/
				String c1_genre_name = 
						Methods.get_genre_name_from_genre_id(actv_methods, c1.getGenre_id());
				
				String c2_genre_name = 
						Methods.get_genre_name_from_genre_id(actv_methods, c2.getGenre_id());
				
				/*********************************
				 * 2. Null?
				 *********************************/
				if (c1_genre_name == null || c2_genre_name == null) {
					
					return 0;
					
				}
				
				/*********************************
				 * 3. Genre names => Not equal?
				 *********************************/
				if (!c1_genre_name.equals(c2_genre_name)) {
					
					return c1_genre_name.compareTo(c2_genre_name);
					
				} else {//if (condition)
					/*********************************
					 * 4. Genre names => Equal?
					 * 		=> Then sort by "created_at"
					 *********************************/
					
					return (int)(c1.getCreated_at() - c2.getCreated_at());
					
				}//if (condition)
				
			}//public int compare(CL i1, CL i2)
			
		};//Comparator<CL> comp
		
		/*********************************
		 * 2. Sort
		 *********************************/
		Collections.sort(cLList, comp);
		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "Sort done: " + cLList.toString());
		
		return true;
	}//public static boolean sort_list_CLList(Activity actv, List<CL> cLList)

	public static boolean sort_item_list_by_status(Activity actv) {
		/*********************************
		 * 1. Comaparator
		 * 2. Sort
		 *********************************/
		actv_methods = actv;
		
		/*********************************
		 * 1. Comaparator
		 *********************************/
		Comparator<Item> comp = new Comparator<Item>(){

			public int compare(Item i1, Item i2) {
				/*********************************
				 * 1. Get genre name
				 * 2. Null?
				 * 
				 * 3. Genre names => Not equal?
				 * 4. Genre names => Equal?
				 *********************************/
				int i1_status = i1.getStatus();
				int i2_status = i2.getStatus();
				
				return i1_status - i2_status;
//				return -(i1_status - i2_status);
				
			}//public int compare(Item i1, Item i2)
			
		};//Comparator<CL> comp
		
		/*********************************
		 * 2. Sort
		 *********************************/
		Collections.sort(CheckActv.iList, comp);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Sort done: " + CheckActv.iList.toString());
		
		return true;
		
	}//public static boolean sort_item_list_by_status(Activity actv)

	public static boolean sort_item_list_by_serial_num(Activity actv) {
		/*********************************
		 * 1. Comaparator
		 * 2. Sort
		 *********************************/
		actv_methods = actv;
		
		/*********************************
		 * 1. Comaparator
		 *********************************/
		Comparator<Item> comp = new Comparator<Item>(){

			public int compare(Item i1, Item i2) {
				/*********************************
				 * 1. Get genre name
				 * 2. Null?
				 * 
				 * 3. Genre names => Not equal?
				 * 4. Genre names => Equal?
				 *********************************/
				int i1_status = i1.getSerial_num();
				int i2_status = i2.getSerial_num();
				
				return i1_status - i2_status;
//				return -(i1_status - i2_status);
				
			}//public int compare(Item i1, Item i2)
			
		};//Comparator<CL> comp
		
		/*********************************
		 * 2. Sort
		 *********************************/
		Collections.sort(CheckActv.iList, comp);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "Sort done: " + CheckActv.iList.toString());
				+ "]", "Sort done: ");
		
		return true;
		
	}//public static boolean sort_item_list_by_serial_num(Activity actv)

	/****************************************
	 *
	 * 
	 * <Caller> 1. Methods.enterDir()
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static boolean update_prefs_currentPath(Activity actv, String newPath) {
		
//		SharedPreferences prefs = 
//				actv.getSharedPreferences(MainActv.prefs_current_path, MainActv.MODE_PRIVATE);
//
//		/*----------------------------
//		 * 2. Get editor
//			----------------------------*/
//		SharedPreferences.Editor editor = prefs.edit();
//
//		/*----------------------------
//		 * 3. Set value
//			----------------------------*/
//		editor.putString(MainActv.prefs_current_path, newPath);
//		
//		try {
//			editor.commit();
//			
//			return true;
//			
//		} catch (Exception e) {
//			
//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Excption: " + e.toString());
//			
//			return false;
//		}
		return false;
		
	}//public static boolean update_prefs_current_path(Activity actv, Strin newPath)

	/****************************************
	 *
	 * 
	 * <Caller> 1. Methods.enterDir()
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static boolean clear_prefs_currentPath(Activity actv, String newPath) {
		
//		SharedPreferences prefs = 
//				actv.getSharedPreferences(MainActv.prefs_current_path, MainActv.MODE_PRIVATE);
//
//		/*----------------------------
//		 * 2. Get editor
//			----------------------------*/
//		SharedPreferences.Editor editor = prefs.edit();
//
//		/*----------------------------
//		 * 3. Clear
//			----------------------------*/
//		try {
//			
//			editor.clear();
//			editor.commit();
//			
//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Prefs cleared");
//			
//			return true;
//			
//		} catch (Exception e) {
//			
//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Excption: " + e.toString());
//			
//			return false;
//		}

		return false;
		
	}//public static boolean clear_prefs_current_path(Activity actv, Strin newPath)

	
	/****************************************
	 *
	 * 
	 * <Caller> 1. Methods.enterDir()
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static String get_currentPath_from_prefs(Activity actv) {
		
//		SharedPreferences prefs = 
//				actv.getSharedPreferences(MainActv.prefs_current_path, MainActv.MODE_PRIVATE);
//
//		return prefs.getString(MainActv.prefs_current_path, null);

		return null;
		
	}//public static String get_currentPath_from_prefs(Activity actv)

	public static void confirm_quit(Activity actv, int keyCode) {
		
		// TODO �ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ黷ｽ�ｿｽ�ｿｽ�ｿｽ\�ｿｽb�ｿｽh�ｿｽE�ｿｽX�ｿｽ^�ｿｽu
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			
			AlertDialog.Builder dialog=new AlertDialog.Builder(actv);
			
	        dialog.setTitle("Confirmation");
	        dialog.setMessage("Quit the app?");
	        
	        dialog.setPositiveButton("Yes",new DialogListener(actv, dialog, 0));
	        dialog.setNegativeButton("No",new DialogListener(actv, dialog, 1));
	        
	        dialog.create();
	        dialog.show();
			
		}//if (keyCode==KeyEvent.KEYCODE_BACK)
		
	}//public static void confirm_quit(Activity actv, int keyCode)

	public static List<String> getTableList(Activity actv) {
		DBUtils dbu = new DBUtils(actv, CONS.DB.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		//=> source: http://stackoverflow.com/questions/4681744/android-get-list-of-tables : "Just had to do the same. This seems to work:"
		String q = "SELECT name FROM " + "sqlite_master"+
						" WHERE type = 'table' ORDER BY name";
		
		Cursor c = null;
		try {
			c = rdb.rawQuery(q, null);
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "c.getCount(): " + c.getCount());

		} catch (Exception e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
		}
		
		// Table names list
		List<String> tableList = new ArrayList<String>();
		
		// Log
		if (c != null) {
			c.moveToFirst();
			
			for (int i = 0; i < c.getCount(); i++) {
				//
				tableList.add(c.getString(0));
				
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "c.getString(0): " + c.getString(0));
				
				
				// Next
				c.moveToNext();
				
			}//for (int i = 0; i < c.getCount(); i++)

		} else {//if (c != null)
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "c => null");
		}//if (c != null)

//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "c.getCount(): " + c.getCount());
//		
		rdb.close();
		
		return tableList;
		
//		return null;
	}//public static List<String> getTableList()

	public static String[] get_column_list(Activity actv, String dbName, String tableName) {
		/*********************************
		 * 1. Set up db
		 * 2. Cursor null?
		 * 3. Get names
		 * 
		 * 4. Close db
		 * 5. Return
		 *********************************/
		DBUtils dbu = new DBUtils(actv, dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		//=> source: http://stackoverflow.com/questions/4681744/android-get-list-of-tables : "Just had to do the same. This seems to work:"
		String q = "SELECT * FROM " + tableName;
		
		/*********************************
		 * 2. Cursor null?
		 *********************************/
		Cursor c = null;
		
		try {
			c = rdb.rawQuery(q, null);
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "c.getCount(): " + c.getCount());

		} catch (Exception e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
			rdb.close();
			
			return null;
		}
		
		/*********************************
		 * 3. Get names
		 *********************************/
		String[] column_names = c.getColumnNames();
		
		/*********************************
		 * 4. Close db
		 *********************************/
		rdb.close();
		
		/*********************************
		 * 5. Return
		 *********************************/
		return column_names;
		
//		return null;
	}//public static String[] get_column_list(Activity actv, String tableName)

	/****************************************
	 *		insertDataIntoDB()
	 * 
	 * <Caller> 
	 * 1. private static boolean refreshMainDB_3_insert_data(Activity actv, Cursor c)
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	private static int insertDataIntoDB(Activity actv, String tableName, Cursor c) {
		/*----------------------------
		 * Steps
		 * 0. Set up db
		 * 1. Move to first
		 * 2. Set variables
		 * 3. Obtain data
		 * 4. Insert data
		 * 5. Close db
		 * 6. Return => counter
			----------------------------*/
//		/*----------------------------
//		 * 0. Set up db
//			----------------------------*/
//		DBUtils dbu = new DBUtils(actv, CONS.DB.dbName);
//		
//		SQLiteDatabase wdb = dbu.getWritableDatabase();
//		
//		/*----------------------------
//		 * 1. Move to first
//			----------------------------*/
//		c.moveToFirst();
//
//		/*----------------------------
//		 * 2. Set variables
//			----------------------------*/
//		int counter = 0;
//		int counter_failed = 0;
//		
//		/*----------------------------
//		 * 3. Obtain data
//			----------------------------*/
//		for (int i = 0; i < c.getCount(); i++) {
//
//			String[] values = {
//					String.valueOf(c.getLong(0)),
//					c.getString(1),
//					c.getString(2),
//					String.valueOf(c.getLong(3)),
//					String.valueOf(c.getLong(4))
//			};
//
//			/*----------------------------
//			 * 4. Insert data
//			 * 		1. Insert data to tableName
//			 * 		2. Record result
//			 * 		3. Insert data to backupTableName
//			 * 		4. Record result
//				----------------------------*/
//			boolean blResult = 
//						dbu.insertData(wdb, tableName, DBUtils.cols_for_insert_data, values);
//				
//			if (blResult == false) {
//				// Log
//				Log.d("Methods.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "i => " + i + "/" + "c.getLong(0) => " + c.getLong(0));
//				
//				counter_failed += 1;
//				
//			} else {//if (blResult == false)
//				counter += 1;
//			}
//
//			//
//			c.moveToNext();
//			
//			if (i % 100 == 0) {
//				// Log
//				Log.d("Methods.java" + "["
//						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//						+ "]", "Done up to: " + i);
//				
//			}//if (i % 100 == 0)
//			
//		}//for (int i = 0; i < c.getCount(); i++)
//		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "All data inserted: " + counter);
//		
//		/*----------------------------
//		 * 5. Close db
//			----------------------------*/
//		wdb.close();
//		
//		/*----------------------------
//		 * 6. Return => counter
//			----------------------------*/
//		//debug
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "counter_failed(sum): " + counter_failed);
//		
//		return counter;

		return -1;
		
	}//private static int insertDataIntoDB(Activity actv, Cursor c)

	private static boolean insertDataIntoDB(
			Activity actv, String tableName, String[] types, String[] values) {
//		/*----------------------------
//		* Steps
//		* 1. Set up db
//		* 2. Insert data
//		* 3. Show message
//		* 4. Close db
//		----------------------------*/
//		/*----------------------------
//		* 1. Set up db
//		----------------------------*/
//		DBUtils dbu = new DBUtils(actv, CONS.DB.dbName);
//		
//		SQLiteDatabase wdb = dbu.getWritableDatabase();
//		
//		/*----------------------------
//		* 2. Insert data
//		----------------------------*/
//		boolean result = dbu.insertData(wdb, tableName, types, values);
//		
//		/*----------------------------
//		* 3. Show message
//		----------------------------*/
//		if (result == true) {
//		
//			// debug
//			Toast.makeText(actv, "Data stored", Toast.LENGTH_SHORT).show();
//			
//			/*----------------------------
//			* 4. Close db
//			----------------------------*/
//			wdb.close();
//			
//			return true;
//			
//		} else {//if (result == true)
//		
//			// debug
//			Toast.makeText(actv, "Store data => Failed", 200).show();
//			
//			/*----------------------------
//			* 4. Close db
//			----------------------------*/
//			wdb.close();
//			
//			return false;
//		
//		}//if (result == true)
//		
//		/*----------------------------
//		* 4. Close db
//		----------------------------*/

		return false;
		
	}//private static boolean insertDataIntoDB()

	public static boolean set_pref(Activity actv, String pref_name, String value) {
//		SharedPreferences prefs = 
//				actv.getSharedPreferences(pref_name, MainActv.MODE_PRIVATE);
//
//		/*----------------------------
//		 * 2. Get editor
//			----------------------------*/
//		SharedPreferences.Editor editor = prefs.edit();
//
//		/*----------------------------
//		 * 3. Set value
//			----------------------------*/
//		editor.putString(pref_name, value);
//		
//		try {
//			editor.commit();
//			
//			return true;
//			
//		} catch (Exception e) {
//			
//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Excption: " + e.toString());
//			
//			return false;
//		}

		return false;
	}//public static boolean set_pref(String pref_name, String value)

	public static String get_pref(Activity actv, String pref_name, String defValue) {
//		SharedPreferences prefs = 
//				actv.getSharedPreferences(pref_name, MainActv.MODE_PRIVATE);
//
//		/*----------------------------
//		 * Return
//			----------------------------*/
//		return prefs.getString(pref_name, defValue);
		
		return null;

	}//public static boolean set_pref(String pref_name, String value)

	public static boolean set_pref(Activity actv, String pref_name, int value) {
//		SharedPreferences prefs = 
//				actv.getSharedPreferences(pref_name, MainActv.MODE_PRIVATE);
//
//		/*----------------------------
//		 * 2. Get editor
//			----------------------------*/
//		SharedPreferences.Editor editor = prefs.edit();
//
//		/*----------------------------
//		 * 3. Set value
//			----------------------------*/
//		editor.putInt(pref_name, value);
//		
//		try {
//			editor.commit();
//			
//			return true;
//			
//		} catch (Exception e) {
//			
//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Excption: " + e.toString());
//			
//			return false;
//		}

		return false;
	}//public static boolean set_pref(String pref_name, String value)

	public static int get_pref(Activity actv, String pref_name, int defValue) {
//		SharedPreferences prefs = 
//				actv.getSharedPreferences(pref_name, MainActv.MODE_PRIVATE);
//
//		/*----------------------------
//		 * Return
//			----------------------------*/
//		return prefs.getInt(pref_name, defValue);

		return -1;
	}//public static boolean set_pref(String pref_name, String value)

	public static Dialog dlg_template_cancel(Activity actv, int layoutId, int titleStringId,
			int cancelButtonId, Tags.DialogButtonTags cancelTag) {
		/*----------------------------
		* Steps
		* 1. Set up
		* 2. Add listeners => OnTouch
		* 3. Add listeners => OnClick
		----------------------------*/
		
		// 
		Dialog dlg = new Dialog(actv);
		
		//
		dlg.setContentView(layoutId);
		
		// Title
		dlg.setTitle(titleStringId);
		
		/*----------------------------
		* 2. Add listeners => OnTouch
		----------------------------*/
		//
		Button btn_cancel = (Button) dlg.findViewById(cancelButtonId);
		
		//
		btn_cancel.setTag(cancelTag);
		
		//
		btn_cancel.setOnTouchListener(new DB_TL(actv, dlg));
		
		/*----------------------------
		* 3. Add listeners => OnClick
		----------------------------*/
		//
		btn_cancel.setOnClickListener(new DB_CL(actv, dlg));
		
		//
		//dlg.show();
		
		return dlg;
	
	}//public static Dialog dlg_template_okCancel()
	
	public static Dialog
	dlg_template_cancel
	(Activity actv, int layoutId, String titleStringId,
			int cancelButtonId, Tags.DialogButtonTags cancelTag) {
		/*----------------------------
		 * Steps
		 * 1. Set up
		 * 2. Add listeners => OnTouch
		 * 3. Add listeners => OnClick
		----------------------------*/
		
		// 
		Dialog dlg = new Dialog(actv);
		
		//
		dlg.setContentView(layoutId);
		
		// Title
		dlg.setTitle(titleStringId);
		
		/*----------------------------
		 * 2. Add listeners => OnTouch
		----------------------------*/
		//
		Button btn_cancel = (Button) dlg.findViewById(cancelButtonId);
		
		//
		btn_cancel.setTag(cancelTag);
		
		//
		btn_cancel.setOnTouchListener(new DB_TL(actv, dlg));
		
		/*----------------------------
		 * 3. Add listeners => OnClick
		----------------------------*/
		//
		btn_cancel.setOnClickListener(new DB_CL(actv, dlg));
		
		//
		//dlg.show();
		
		return dlg;
		
	}//dlg_template_cancel

	public static Dialog dlg_template_okCancel(Activity actv, int layoutId, int titleStringId,
			int okButtonId, int cancelButtonId, Tags.DialogButtonTags dlgRgstrItemBtOk, ic2.utils.Tags.DialogButtonTags dlgGenericDismiss) {
		/*----------------------------
		* Steps
		* 1. Set up
		* 2. Add listeners => OnTouch
		* 3. Add listeners => OnClick
		----------------------------*/
		
		// 
		Dialog dlg = new Dialog(actv);
		
		//
		dlg.setContentView(layoutId);
		
		// Title
		dlg.setTitle(titleStringId);
		
		/*----------------------------
		* 2. Add listeners => OnTouch
		----------------------------*/
		//
		Button btn_ok = (Button) dlg.findViewById(okButtonId);
		Button btn_cancel = (Button) dlg.findViewById(cancelButtonId);
		
		//
		btn_ok.setTag(dlgRgstrItemBtOk);
		btn_cancel.setTag(dlgGenericDismiss);
		
		//
		btn_ok.setOnTouchListener(new DB_TL(actv, dlg));
		btn_cancel.setOnTouchListener(new DB_TL(actv, dlg));
		
		/*----------------------------
		* 3. Add listeners => OnClick
		----------------------------*/
		//
		btn_ok.setOnClickListener(new DB_CL(actv, dlg));
		btn_cancel.setOnClickListener(new DB_CL(actv, dlg));
		
		//
		//dlg.show();
		
		return dlg;
	
	}//public static Dialog dlg_template_okCancel()

	public static long getMillSeconds(int year, int month, int date) {
		// Calendar
		Calendar cal = Calendar.getInstance();
		
		// Set time
		cal.set(year, month, date);
		
		// Date
		Date d = cal.getTime();
		
		return d.getTime();
		
	}//private long getMillSeconds(int year, int month, int date)

	/****************************************
	 *	getMillSeconds_now()
	 * 
	 * <Caller> 
	 * 1. ButtonOnClickListener # case main_bt_start
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static long getMillSeconds_now() {
		
		Calendar cal = Calendar.getInstance();
		
		return cal.getTime().getTime();
		
	}//private long getMillSeconds_now(int year, int month, int date)

	public static String get_TimeLabel(long millSec) {
		
		 SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd_HHmmss");
		 
		return sdf1.format(new Date(millSec));
		
	}//public static String get_TimeLabel(long millSec)

	public static String  convert_millSeconds2digitsLabel(long millSeconds) {
		/*----------------------------
		 * Steps
		 * 1. Prepare variables
		 * 2. Build a string
		 * 3. Return
			----------------------------*/
		/*----------------------------
		 * 1. Prepare variables
			----------------------------*/
		int seconds = (int)(millSeconds / 1000);
		
		int minutes = seconds / 60;
		
		int digit_seconds = seconds % 60;
		
		/*----------------------------
		 * 2. Build a string
			----------------------------*/
		StringBuilder sb = new StringBuilder();
		
		if (String.valueOf(minutes).length() < 2) {
			
			sb.append("0");
			
		}//if (String.valueOf(minutes).length() < 2)
		
		sb.append(String.valueOf(minutes));
		sb.append(":");

		if (String.valueOf(digit_seconds).length() < 2) {
			
			sb.append("0");
			
		}//if (String.valueOf(digit_seconds).length() < 2)

		sb.append(String.valueOf(digit_seconds));
		
		/*----------------------------
		 * 3. Return
			----------------------------*/
		return sb.toString();
		
	}//public static void  convert_millSeconds2digitsLabel()

	public static String convert_millSec_to_DateLabel(long millSec) {
		
		 SimpleDateFormat sdf1 = new SimpleDateFormat("yy/MM/dd");
		 
		return sdf1.format(new Date(millSec));
		
	}//public static String get_TimeLabel(long millSec)

	public static HashMap<String, Integer> convert_to_histogram(String[] data) {
		/*----------------------------
		 * 1. Get hash map
		 * 2. Return
			----------------------------*/
		/*----------------------------
		 * 1. Get hash map
			----------------------------*/
		HashMap<String, Integer> hm = new HashMap<String, Integer>();
		
		for (String item : data) {
			
			if (hm.get(item) == null) {
				
				hm.put(item, Integer.valueOf(1));
				
			} else {//if (hm.get(ary) == null)
				
				int val = hm.get(item);
				
				val += 1;
				
				hm.put(item, val);
				
			}//if (hm.get(ary) == null)
			
		}//for (String item : data)

		/*----------------------------
		 * 2. Return
			----------------------------*/
		return hm;
		
	}//public static HashMap<String, Integer> convert_to_histogram(String[] data)

	public static void dlg_register(Activity actv) {
		/*----------------------------
		 * Steps
		 * 1. Get a dialog
		 * 2. List view
		 * 3. Set listener => list
		 * 9. Show dialog
			----------------------------*/
		 
		Dialog dlg = dlg_template_cancel(actv, 
				R.layout.dlg_register, R.string.generic_tv_register,
				R.id.dlg_register_btn_cancel,
				Tags.DialogButtonTags.dlg_generic_dismiss);
		
		/*----------------------------
		 * 2. List view
		 * 	1. Get view
		 * 	1-2. Set tag to view
		 * 
		 * 	2. Prepare list data
		 * 	3. Prepare adapter
		 * 	4. Set adapter
			----------------------------*/
		ListView lv = (ListView) dlg.findViewById(R.id.dlg_register_lv);
		
		lv.setTag(Methods.DialogItemTags.dlg_register_lv);
		
		/*----------------------------
		 * 2.2. Prepare list data
			----------------------------*/
		List<String> registerItems = new ArrayList<String>();
		
		registerItems.add(actv.getString(R.string.main_menu_register_list));
		registerItems.add(actv.getString(R.string.main_menu_register_genre));
		
		ArrayAdapter<String> adp = new ArrayAdapter<String>(
		
				actv,
				android.R.layout.simple_list_item_1,
				registerItems
		);
		
		/*----------------------------
		 * 2.4. Set adapter
			----------------------------*/
		lv.setAdapter(adp);
		
		
		
		/*----------------------------
		 * 3. Set listener => list
			----------------------------*/
		lv.setOnItemClickListener(
						new DOI_CL(
								actv, 
								dlg));
		
		/*----------------------------
		 * 9. Show dialog
			----------------------------*/
		dlg.show();
		
	}//public static void dlg_register(Activity actv)

	public static void dlg_register_genre(Activity actv, Dialog dlg) {
		/*----------------------------
		 * Steps
		 * 1. Set up
		 * 2. Add listeners => OnTouch
		 * 3. Add listeners => OnClick
			----------------------------*/
		
		// 
		Dialog dlg2 = new Dialog(actv);
		
		//
		dlg2.setContentView(R.layout.dlg_register_genre);
		
		// Title
		dlg2.setTitle(R.string.dlg_register_genre_title);
		
		/*----------------------------
		 * 2. Add listeners => OnTouch
			----------------------------*/
		//
		Button btn_ok = 
			(Button) dlg2.findViewById(R.id.dlg_register_genre_btn_ok);
		Button btn_cancel = (Button) dlg2.findViewById(R.id.dlg_register_genre_btn_cancel);
		
		//
		btn_ok.setTag(Tags.DialogButtonTags.dlg_register_genre_bt_ok);
		btn_cancel.setTag(Tags.DialogButtonTags.DLG_GENERIC_DISMISS_SECOND_DIALOG);
		
		//
		btn_ok.setOnTouchListener(new DB_TL(actv, dlg2));
		btn_cancel.setOnTouchListener(new DB_TL(actv, dlg2));
		
		/*----------------------------
		 * 3. Add listeners => OnClick
			----------------------------*/
		//
		btn_ok.setOnClickListener(
					new DB_CL(actv, dlg, dlg2));
		btn_cancel.setOnClickListener(
					new DB_CL(actv, dlg, dlg2));
		
		//
		dlg2.show();
		
	}//public static void dlg_register_genre(Activity actv, Dialog dlg)

	public static void dlg_register_list(Activity actv, Dialog dlg) {
		/*----------------------------
		 * Steps
		 * 1. Set up
		 * 2. Add listeners => OnTouch
		 * 3. Add listeners => OnClick
		 * 
		 * 4. Set spinner
			----------------------------*/
		
		// 
		Dialog dlg2 = new Dialog(actv);
		
		//
		dlg2.setContentView(R.layout.dlg_register_list);
		
		// Title
		dlg2.setTitle(R.string.dlg_register_list_title);
		
		/*----------------------------
		 * 2. Add listeners => OnTouch
			----------------------------*/
		//
		Button btn_ok = 
			(Button) dlg2.findViewById(R.id.dlg_register_list_btn_ok);
		Button btn_cancel = (Button) dlg2.findViewById(R.id.dlg_register_list_btn_cancel);
		
		//
		btn_ok.setTag(Tags.DialogButtonTags.dlg_register_list_bt_ok);
		btn_cancel.setTag(Tags.DialogButtonTags.DLG_GENERIC_DISMISS_SECOND_DIALOG);
		
		//
		btn_ok.setOnTouchListener(new DB_TL(actv, dlg2));
		btn_cancel.setOnTouchListener(new DB_TL(actv, dlg2));
		
		/*----------------------------
		 * 3. Add listeners => OnClick
			----------------------------*/
		//
		btn_ok.setOnClickListener(
					new DB_CL(actv, dlg, dlg2));
		btn_cancel.setOnClickListener(
					new DB_CL(actv, dlg, dlg2));
		
		/*********************************
		 * 4. Set spinner
		 * 	1. List
		 * 	2. Adapter
		 * 	3. Set adapter
		 *********************************/
		Spinner sp = (Spinner) dlg2.findViewById(R.id.dlg_register_list_sp);
		
		List<String> genreList = Methods.get_genre_list(actv);

		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "genreList.size(): " + genreList.size());

		/*********************************
		 * 4.2. Adapter
		 *********************************/
		ArrayAdapter<String> adp = new ArrayAdapter<String>(
	              actv, android.R.layout.simple_spinner_item, genreList);
		
		adp.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
		
		/*********************************
		 * 4.3. Set adapter
		 *********************************/
		sp.setAdapter(adp);
		
		//
		dlg2.show();
		
	}//public static void dlg_register_list(Activity actv, Dialog dlg)

	public static void dlg_register_item(Activity actv) {
		/*----------------------------
		 * Steps
		 * 1. Get a dialog
		 * 2. List view
		 * 3. Set listener => list
		 * 9. Show dialog
			----------------------------*/
		 
		Dialog dlg = dlg_template_okCancel(
						actv, R.layout.dlg_register_item,
						R.string.dlg_rgstr_item_title,
						
						R.id.dlg_rgstr_item_btn_ok,
						R.id.dlg_rgstr_item_btn_cancel,
						
						Tags.DialogButtonTags.dlg_rgstr_item_bt_ok,
						Tags.DialogButtonTags.dlg_generic_dismiss
						);
		
		/*----------------------------
		 * 2. List view
		 * 	1. Get view
		 * 	1-2. Set tag to view
		 * 
		 * 	2. Prepare list data
		 * 	3. Prepare adapter
		 * 	4. Set adapter
			----------------------------*/
		/*----------------------------
		 * 9. Show dialog
			----------------------------*/
		dlg.show();
		
	}//public static void dlg_register_item(Activity actv)

	static List<String> get_genre_list(Activity actv) {
		/*********************************
		 * 1. db
		 * 2. Query
		 * 2-2. If no entry => Return
		 * 
		 * 3. Build list
		 * 
		 *********************************/
		
		DBUtils dbu = new DBUtils(actv, CONS.DB.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		String sql = "SELECT * FROM " + MainActv.tableName_genres;
		
		Cursor c = null;
		
		try {
			
			c = rdb.rawQuery(sql, null);
			
		} catch (Exception e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
			rdb.close();
			
			return null;
		}
		
		/*********************************
		 * 2-2. If no entry => Return
		 *********************************/
		if (c.getCount() < 1) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "c.getCount() < 1");
			
			rdb.close();
			
			return null;
			
		}//if (c.getCount() < 1)
		
		
		/*********************************
		 * 3. Build list
		 *********************************/
		c.moveToFirst();
		
		List<String> genreList = new ArrayList<String>();
		
		for (int i = 0; i < c.getCount(); i++) {
			
			genreList.add(c.getString(3));
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "c.getString(3): " + c.getString(3));
			
			c.moveToNext();
			
		}//for (int i = 0; i < c.getCount(); i++)
		
		rdb.close();
		
		return genreList;
		
	}//private static List<String> get_genre_list(Activity actv)

	public static void register_genre(Activity actv, Dialog dlg, Dialog dlg2) {
		/*********************************
		 * 1. Get text
		 * 2. db
		 * 
		 * 3. Insert data
		 * 
		 * 9. Close db
		 * 
		 *********************************/
		EditText et = (EditText) dlg2.findViewById(R.id.dlg_register_genre_et);
		
		String genre_name = et.getText().toString();
		
		
		DBUtils dbu = new DBUtils(actv, CONS.DB.dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		

		boolean res = dbu.insertData_genre(wdb, genre_name);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "res: " + res + "(" + genre_name + ")");
		
		/*********************************
		 * 9. Close db
		 *********************************/
		wdb.close();
		
	}//public static void register_genre(Activity actv, Dialog dlg, Dialog dlg2)

	public static void register_list(Activity actv, Dialog dlg, Dialog dlg2) {
		/*********************************
		 * 1. Get text
		 * 2. db
		 * 
		 * 2-2. Get genre id
		 * 
		 * 3. Insert data
		 * 
		 * 9. Close db
		 * 10. Dismiss dialogues
		 * 
		 * 11. Notify adapter
		 * 
		 *********************************/
		/*********************************
		 * 2-2. Get genre id
		 *********************************/
		Spinner sp = (Spinner) dlg2.findViewById(R.id.dlg_register_list_sp);
		
		String genre_name = (String) sp.getSelectedItem();

		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "genre_name: " + genre_name);
		
		int genre_id = Methods.get_genre_id_from_genre_name(actv, genre_name);
		
		if (genre_id < 0) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "genre_id: " + genre_id);
			
			// debug
			Toast.makeText(actv, "�ｿｽW�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ謫ｾ�ｿｽﾅゑｿｽ�ｿｽﾜゑｿｽ�ｿｽ�ｿｽ", Toast.LENGTH_SHORT).show();
			
		}//if (genre_id < 0)
		
		/*********************************
		 * 1. Get text
		 *********************************/
		EditText et = (EditText) dlg2.findViewById(R.id.dlg_register_list_et);
		
		String list_name = et.getText().toString();
		
		/*********************************
		 * 2. db
		 *********************************/
		DBUtils dbu = new DBUtils(actv, CONS.DB.dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		/*********************************
		 * 3. Insert data
		 *********************************/
		boolean res = dbu.insertData_list(wdb, list_name, genre_id);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "res: " + res + "(" + list_name + ")");
		
		/*********************************
		 * 9. Close db
		 *********************************/
		wdb.close();
		
		/*********************************
		 * 10. Dismiss dialogues
		 *********************************/
		dlg2.dismiss();
		dlg.dismiss();
		
		/*********************************
		 * 11. Notify adapter
		 *********************************/
		
		
	}//public static void register_list(Activity actv, Dialog dlg, Dialog dlg2)

	public static void register_item(Activity actv, Dialog dlg) {
		/*********************************
		 * 1. Get text
		 * 2. Get serial number
		 * 
		 * 3. Setup db
		 * 
		 * 4. Insert data
		 * 
		 * 9. Close db
		 * 
		 * 10. Dismiss dialog
		 * 
		 *********************************/
		EditText et_text = (EditText) dlg.findViewById(R.id.dlg_rgstr_item_et_text);
		
		String text = et_text.getText().toString();
		
		/*********************************
		 * 2. Get serial number
		 *********************************/
		EditText et_order = (EditText) dlg.findViewById(R.id.dlg_rgstr_item_et_order);
		
		int serial_num;
		
		if (et_order.getText().toString().equals("")) {
			
			serial_num = 
					Methods.get_num_of_entries_items(actv, CheckActv.clList.getDb_id()) + 1;
			
		} else {//if (serial_num.equals(""))
			
			serial_num = Integer.parseInt(et_order.getText().toString());
			
		}//if (serial_num.equals(""))
		
		/*********************************
		 * 3. Setup db
		 *********************************/
		DBUtils dbu = new DBUtils(actv, CONS.DB.dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		/*********************************
		 * 4. Insert data
		 *********************************/
		Object[] data = {text, serial_num, CheckActv.clList.getDb_id()};
		boolean res = dbu.insertData_item(wdb, data);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "res: " + res + "(" + (String) text + ")");
		
		/*********************************
		 * 9. Close db
		 *********************************/
		wdb.close();
		
		/*********************************
		 * 10. Dismiss dialog
		 *********************************/
		if (res == true) {

			dlg.dismiss();
			
			// debug
			Toast.makeText(actv, "Data stored", Toast.LENGTH_SHORT).show();

			Methods.refresh_item_list(actv);
			
			return;
			
		} else {//if (res == true)
			
			// debug
			Toast.makeText(actv, "Store data => Failed", 200).show();

			return;
			
		}//if (res == true)
		
		
		
	}//public static void register_item(Activity actv, Dialog dlg)

	/**********************************************
	 * <Return>
	 * -1	=> Exception in querying
	 * >=0	=> Num of entries
	 * @param list_id 
	 **********************************************/
	private static int get_num_of_entries_items(Activity actv, long list_id) {
		/*********************************
		 * memo
		 *********************************/
		DBUtils dbu = new DBUtils(actv, CONS.DB.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		//=> source: http://stackoverflow.com/questions/4681744/android-get-list-of-tables : "Just had to do the same. This seems to work:"
		String q = "SELECT * FROM " + CONS.DB.tname_items +
				" WHERE " + CONS.DB.col_name_Items[2] + "='" + list_id + "'";;
		
		Cursor c = null;
		
		try {
			c = rdb.rawQuery(q, null);
			
			int num_of_entries = c.getCount();
			
			rdb.close();
			
			return num_of_entries;
			
		} catch (Exception e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
			rdb.close();
			
			return -1;
			
		}
		
//		return 0;
	}//private static int get_num_of_entries_items(Activity actv)

	/**********************************************
	 * get_genre_id_from_genre_name(Activity actv, String genre_name)
	 * 
	 * <Return>
	 * -1	=> Error, exception
	 * -2	=> No entry
	 **********************************************/
	public static int get_genre_id_from_genre_name(Activity actv, String genre_name) {
		/*********************************
		 * 1. db
		 * 2. Query
		 *
		 * 3. Get data
		 * 4. Close db
		 * 
		 * 5. Return
		 *********************************/
		DBUtils dbu = new DBUtils(actv, CONS.DB.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();
		
		/*********************************
		 * 2. Query
		 *********************************/
		String sql = "SELECT * FROM " + MainActv.tableName_genres + 
					" WHERE name='" + genre_name + "'";
		
		Cursor c = null;
		
		try {
			
			c = rdb.rawQuery(sql, null);
			
			actv.startManagingCursor(c);
			
		} catch (Exception e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
			rdb.close();
			
			return -1;
		}
		
		/*********************************
		 * 2-2. If no entry => Return
		 *********************************/
		if (c.getCount() < 1) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "c.getCount() < 1");
			
			rdb.close();
			
			return -2;
			
		}//if (c.getCount() < 1)
		
		
		/*********************************
		 * 3. Get data
		 *********************************/
		c.moveToFirst();
		
		int genre_id = (int) c.getLong(0);
		
		/*********************************
		 * 4. Close db
		 *********************************/
		rdb.close();
		
		/*********************************
		 * 5. Return
		 *********************************/
		return genre_id;
		
	}//private static int get_genre_id_from_genre_name(String genre_name)


	/**********************************************
	 * get_genre_name_from_genre_id(Activity actv, int genre_id)
	 * 
	 * <Return>
	 * 	null	=> Cursor == null
	 * 			=> c.getCount() < 1
	 **********************************************/
	public static String get_genre_name_from_genre_id(Activity actv, int genre_id) {
		/*********************************
		 * 1. db
		 * 2. Query
		 *
		 * 3. Get data
		 * 4. Close db
		 * 
		 * 5. Return
		 *********************************/
		DBUtils dbu = new DBUtils(actv, CONS.DB.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();
		
		/*********************************
		 * 2. Query
		 *********************************/
		String sql = "SELECT * FROM " + MainActv.tableName_genres + 
					" WHERE " + android.provider.BaseColumns._ID + "='" + genre_id + "'";
		
		Cursor c = null;
		
		try {
			
			c = rdb.rawQuery(sql, null);
			
			actv.startManagingCursor(c);
			
		} catch (Exception e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
			rdb.close();
			
			return null;
		}
		
		/*********************************
		 * 2-2. If no entry => Return
		 *********************************/
		if (c.getCount() < 1) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "c.getCount() < 1");
			
			rdb.close();
			
			return null;
			
		}//if (c.getCount() < 1)
		
		
		/*********************************
		 * 3. Get data
		 *********************************/
		c.moveToFirst();
		
		String genre_name = (String) c.getString(3);
		
		/*********************************
		 * 4. Close db
		 *********************************/
		rdb.close();
		
		/*********************************
		 * 5. Return
		 *********************************/
		return genre_name;
		
	}//public static String get_genre_name_from_genre_id(int genre_id)

	
	public static CL get_clList_from_db_id(Activity actv, long list_id) {
		/*********************************
		 * 1. db
		 * 2. Query
		 *
		 * 3. Get data
		 * 4. Close db
		 * 
		 * 5. Return
		 *********************************/
		DBUtils dbu = new DBUtils(actv, CONS.DB.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();
		
		/*********************************
		 * 2. Query
		 *********************************/
		String sql = "SELECT * FROM " + CONS.DB.tname_Check_Lists + 
					" WHERE " + android.provider.BaseColumns._ID + "='" + list_id + "'";
		
		Cursor c = null;
		
		try {
			
			c = rdb.rawQuery(sql, null);
			
			actv.startManagingCursor(c);
			
		} catch (Exception e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
			rdb.close();
			
			return null;
		}
		
		/*********************************
		 * 2-2. If no entry => Return
		 *********************************/
		if (c.getCount() < 1) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "c.getCount() < 1");
			
			rdb.close();
			
			return null;
			
		}//if (c.getCount() < 1)
		
		
		/*********************************
		 * 3. Get data
		 *********************************/
		c.moveToFirst();
		
//		String genre_name = (String) c.getString(3);
		
		CL clList = new CL(
				c.getString(3),
				c.getInt(4),
				
				c.getLong(0),
				c.getLong(1),
				c.getLong(2)
				);
		
		/*********************************
		 * 4. Close db
		 *********************************/
		rdb.close();
		
		/*********************************
		 * 5. Return
		 *********************************/
		return clList;
		
	}//public static CL get_clList_from_db_id(Activity actv, long list_id)

	public static Cursor select_all_from_table(Activity actv, SQLiteDatabase rdb, String tableName) {
		/*********************************
		 * 
		 *********************************/
		/********************************
		 * 2. Query
		 ********************************/
		String sql = "SELECT * FROM " + tableName;
		
		Cursor c = null;
		
		try {
			
			c = rdb.rawQuery(sql, null);
			
			actv.startManagingCursor(c);

			return c;
			
		} catch (Exception e) {
			// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
			rdb.close();
			
			return null;
		}
		
	}//public static select_all_from_table(Activity actv, String tableName)

	/**********************************************
	 * <Return>
	 * 	null	=> Query exception
	 * 			=> No entry
	 **********************************************/
	public static List<Item> get_item_list_from_check_list(
					Activity actv, long list_id) {
		/*********************************
		 * 
		 *********************************/
		/*********************************
		 * 1. db
		 * 2. Query
		 *
		 * 3. Get data
		 * 4. Close db
		 * 
		 * 5. Return
		 *********************************/
		DBUtils dbu = new DBUtils(actv, CONS.DB.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();
		
		/*********************************
		 * 2. Query
		 *********************************/
		String sql = "SELECT * FROM " + CONS.DB.tname_items + 
//					" WHERE " + CONS.DB.cols_items[5] + "='" + list_id + "'";
				" WHERE " + CONS.DB.col_name_Items[2] + "='" + list_id + "'";

		Cursor c = null;
		
		try {
			
			c = rdb.rawQuery(sql, null);
			
			actv.startManagingCursor(c);
			
		} catch (Exception e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
			rdb.close();
			
			return null;
		}
		
		/*********************************
		 * 2-2. If no entry => Return
		 *********************************/
		if (c.getCount() < 1) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "c.getCount() < 1");
			
			rdb.close();
			
			return null;
			
		}//if (c.getCount() < 1)
		
		
		/*********************************
		 * 3. Get data
		 *********************************/
		c.moveToFirst();

		List<Item> iList = new ArrayList<Item>();
		
		for (int i = 0; i < c.getCount(); i++) {
			
			iList.add(new Item(
					c.getString(3),
					c.getInt(4),
					c.getLong(5),
					
					c.getInt(6),	// status
					
					c.getLong(0),
					c.getLong(1),
					c.getLong(2)
					));
			
			c.moveToNext();
			
		}//for (int i = 0; i < c.getCount(); i++)
		
		/*********************************
		 * 4. Close db
		 *********************************/
		rdb.close();
		
		/*********************************
		 * 5. Return
		 *********************************/
		return iList;
		
	}//public static List<Item> get_item_list_from_check_list

	private static boolean refresh_item_list(Activity actv) {
		/*********************************
		 * 1. Build list
		 * 
		 * 2. Notify adapter
		 * 
		 * 3. Return
		 *********************************/
		/*********************************
		 * 1.2. Build list
		 *********************************/
		if (CheckActv.iList != null) {
		
			CheckActv.iList.clear();
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "CheckActv.iList => Cleared");
			
		} else {//if (CheckActv.iList != null)
			
			CheckActv.iList = new ArrayList<Item>();
			
		}//if (CheckActv.iList != null)
		
		CheckActv.iList.addAll(
			Methods.get_item_list_from_check_list(
					actv, 
					CheckActv.clList.getDb_id()));

		// Log
		if (CheckActv.iList == null) {

			Log.d("CheckActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "CheckActv.iList => Null");
			
			// debug
			Toast.makeText(actv,
					"Query exception, or, no items " +
					"for this check list, yet",
					Toast.LENGTH_SHORT).show();

			return false;

		} else {//if (CheckActv.iList == null)
			
			// Log
			Log.d("CheckActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "CheckActv.iList.size(): " + CheckActv.iList.size());
			
		}//if (CheckActv.iList == null)
		
		/*********************************
		 * 2. Notify adapter
		 *********************************/
		CheckActv.ilAdp = new ItemListAdapter(
				actv,
				R.layout.list_row_item_list,
//				CheckActv.iList
				CheckActv.iList
				);
		
		((ListActivity) actv).setListAdapter(CheckActv.ilAdp);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Adapter => Re-set");
		
//		CheckActv.ilAdp.notifyDataSetChanged();
//
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "CheckActv.ilAdp => Notified");
		
		/*********************************
		 * 3. Return
		 *********************************/
		return true;

	}//private void refresh_item_list()

	public static void change_item_status(Activity actv, Item item) {
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "item.getStatus(): " + item.getStatus());
		
		int status = item.getStatus();
		
		if (status == 5) {
			
			item.setStatus(0);
			
		} else {//if (status == 5)
		
			item.setStatus(status + 1);
			
		}//if (status == 5)
		
		CheckActv.ilAdp.notifyDataSetChanged();

		
	}//public static void change_item_status(Activity actv, Item item)s

	
	public static void
	dlg_checkactv_long_click
	(Activity actv, int item_position) {
		/*********************************
		 * 1. Dialog
		 * 2. List view
		 * 3. Show dialog
		 *********************************/
		Dialog dlg = dlg_template_cancel(actv, 
				R.layout.dlg_checkactv_long_click,
				R.string.dlg_checkactv_long_click_title,
				
				R.id.dlg_checkactv_long_click_bt_cancel,
				Tags.DialogButtonTags.dlg_generic_dismiss);

		/*----------------------------
		 * 2. List view
		 * 	1. Get view
		 * 	1-2. Set tag to view
		 * 
		 * 	2. Prepare list data
		 * 	3. Prepare adapter
		 * 	4. Set adapter
			----------------------------*/
		ListView lv = (ListView) dlg.findViewById(R.id.dlg_checkactv_long_click_lv);
		
		/*----------------------------
		 * 2.2. Prepare list data
			----------------------------*/
		List<String> long_click_items = new ArrayList<String>();
		
		long_click_items.add(actv.getString(
				R.string.dlg_checkactv_long_click_lv_edit));
		
		long_click_items.add(actv.getString(
				R.string.dlg_checkactv_long_click_lv_change_serial_num));
		
		ArrayAdapter<String> adp = new ArrayAdapter<String>(
		
				actv,
				android.R.layout.simple_list_item_1,
				long_click_items
		);
		
		/*----------------------------
		 * 2.4. Set adapter
			----------------------------*/
		lv.setAdapter(adp);
		
		/*----------------------------
		 * 3. Set listener => list
			----------------------------*/
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "item_position: " + item_position);
		
		lv.setTag(Methods.DialogItemTags.dlg_checkactv_long_click_lv);
		
		lv.setOnItemClickListener(
						new DOI_CL(
								actv, 
								dlg, item_position));
		
		/*********************************
		 * 3. Show dialog
		 *********************************/
		dlg.show();
		
	}//public static void dlg_checkactv_long_click(Activity actv)
	
	public static void
	dlg_checkactv_long_click
	(Activity actv, int item_position, Item item) {
		/*********************************
		 * 1. Dialog
		 * 2. List view
		 * 3. Show dialog
		 *********************************/
		String dlgTitle = null;
		
		if (item.getText() != null) {

			int textLen = item.getText().length();
			
			if (textLen < CONS.Admin.Miscs.DialogTitleLength) {
				
				dlgTitle = item.getText();
				
			} else {//if (textLen < 6)
				
				dlgTitle = item.getText().substring(
								0,
								CONS.Admin.Miscs.DialogTitleLength);
				
			}//if (textLen < 6)
			
					
		} else {//if (item.getText() != null)
			
			dlgTitle = actv.getString(R.string.dlg_checkactv_long_click_title);
			
		}//if (item.getText() != null)
		
		
		
		Dialog dlg = dlg_template_cancel(actv, 
				R.layout.dlg_checkactv_long_click,
//				R.string.dlg_checkactv_long_click_title,
				dlgTitle,
				
				R.id.dlg_checkactv_long_click_bt_cancel,
				Tags.DialogButtonTags.dlg_generic_dismiss);
		
		/*----------------------------
		 * 2. List view
		 * 	1. Get view
		 * 	1-2. Set tag to view
		 * 
		 * 	2. Prepare list data
		 * 	3. Prepare adapter
		 * 	4. Set adapter
			----------------------------*/
		ListView lv = (ListView) dlg.findViewById(R.id.dlg_checkactv_long_click_lv);
		
		/*----------------------------
		 * 2.2. Prepare list data
			----------------------------*/
		List<String> long_click_items = new ArrayList<String>();
		
		long_click_items.add(actv.getString(
				R.string.dlg_checkactv_long_click_lv_edit));
		
		long_click_items.add(actv.getString(
				R.string.dlg_checkactv_long_click_lv_change_serial_num));

		long_click_items.add(actv.getString(
				R.string.dlg_checkactv_long_click_lv_delete_item));

		ArrayAdapter<String> adp = new ArrayAdapter<String>(
				
				actv,
				android.R.layout.simple_list_item_1,
				long_click_items
				);
		
		/*----------------------------
		 * 2.4. Set adapter
			----------------------------*/
		lv.setAdapter(adp);
		
		/*----------------------------
		 * 3. Set listener => list
			----------------------------*/
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "item_position: " + item_position);
		
		lv.setTag(Methods.DialogItemTags.dlg_checkactv_long_click_lv);
		
		lv.setOnItemClickListener(
				new DOI_CL(
						actv, 
						dlg, item_position,
						item));
		
		/*********************************
		 * 3. Show dialog
		 *********************************/
		dlg.show();
		
	}//dlg_checkactv_long_click

	public static void
	dlg_MainActv_LongClick
	(Activity actv, int pos_InAdapter, CL cl) {
//		(Activity actv, int item_position, long check_list_id, CL check_list) {
		/*********************************
		 * 1. Dialog
		 * 2. List view
		 * 3. Show dialog
		 *********************************/
		// Log
		String msg_Log = "pos_InAdapter = " + pos_InAdapter;
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", msg_Log);
		
		
		
		Dialog dlg = dlg_template_cancel(actv, 
				R.layout.dlg_main_actv_long_click, 
				R.string.dlg_main_actv_long_click_title,
				
				R.id.dlg_main_actv_long_click_bt_cancel,
				Tags.DialogButtonTags.dlg_generic_dismiss);

		//debug
		CL list = (CL) CONS.MainActv.lvMain.getItemAtPosition(pos_InAdapter);
		
		dlg.setTitle(
				actv.getString(R.string.dlg_main_actv_long_click_title)
				+ " : "
				+ list.getName());
		
		// Log
		msg_Log = "list.getName() => " + list.getName();
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", msg_Log);
		
		
		/*----------------------------
		 * 2. List view
		 * 	1. Get view
		 * 	1-2. Set tag to view
		 * 
		 * 	2. Prepare list data
		 * 	3. Prepare adapter
		 * 	4. Set adapter
			----------------------------*/
		ListView lv = (ListView) dlg.findViewById(R.id.dlg_main_actv_long_click_lv);
		
//		lv.setTag(Methods.DialogItemTags.dlg_main_actv_long_click_lv);
		lv.setTag(Methods.DialogItemTags.DLG_MAINACTV_LONGCLICK_LV);
		
		/*----------------------------
		 * 2.2. Prepare list data
			----------------------------*/
		List<String> long_click_items = new ArrayList<String>();
		
		// Add items to the list
		long_click_items.add(
					actv.getString(
							R.string.dlg_main_actv_long_click_lv_clear_item_status));
		
		long_click_items.add(actv.getString(
						R.string.dlg_main_actv_long_click_lv_delete_list));
		
//		long_click_items.add(actv.getString(
//				R.string.dlg_main_actv_long_click_lv_edit_title));
//		
//		long_click_items.add(actv.getString(
//				R.string.dlg_main_actv_long_click_lv_change_genre));
		
		long_click_items.add(actv.getString(
				R.string.dlg_main_actv_long_click_lv_edit_cl));
		
		long_click_items.add(actv.getString(
				R.string.dlg_main_actv_long_click_lv_dup_cl));
		
		// Setup: Adapter
		ArrayAdapter<String> adp = new ArrayAdapter<String>(
		
				actv,
				android.R.layout.simple_list_item_1,
				long_click_items
		);
		
		/*----------------------------
		 * 2.4. Set adapter
			----------------------------*/
		lv.setAdapter(adp);
		
		/*----------------------------
		 * 3. Set listener => list
			----------------------------*/
		
		lv.setOnItemClickListener(
						new DOI_CL(
								actv, 
								dlg, pos_InAdapter, cl));
//		dlg, position_InLV, check_list_id, check_list));
		
		/*********************************
		 * 3. Show dialog
		 *********************************/
		dlg.show();
		
	}//public static void dlg_main_actv_long_click(Activity actv)

	public static void dlg_checkactv_long_click_lv_change_serial_num(
			Activity actv, Dialog dlg, int item_position) {
		/*********************************
		 * memo
		 *********************************/
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "item_position: " + item_position);

		
		Dialog dlg2 = new Dialog(actv);
		
		//
		dlg2.setContentView(R.layout.dlg_checkactv_change_serial_num);
		
		// Title
		dlg2.setTitle(R.string.dlg_checkactv_change_serial_num_title);

		/*********************************
		 * Get views
		 *********************************/
		//
		Button btn_ok = 
			(Button) dlg2.findViewById(R.id.dlg_checkactv_change_serial_num_btn_ok);
		
		Button btn_cancel = 
				(Button) dlg2.findViewById(R.id.dlg_checkactv_change_serial_num_btn_cancel);
		
		/*********************************
		 * Set tags
		 *********************************/
		//
		btn_ok.setTag(
				Tags.DialogButtonTags.dlg_checkactv_change_serial_num_btn_ok);
		
		btn_cancel.setTag(
				Tags.DialogButtonTags.DLG_GENERIC_DISMISS_SECOND_DIALOG);
		
		/*----------------------------
		* 2. Add listeners => OnTouch
		----------------------------*/
		//
		btn_ok.setOnTouchListener(new DB_TL(actv, dlg));
		btn_cancel.setOnTouchListener(new DB_TL(actv, dlg));
		
		/*----------------------------
		* 3. Add listeners => OnClick
		----------------------------*/
		//
		btn_ok.setOnClickListener(new DB_CL(actv, dlg, dlg2, item_position));
		btn_cancel.setOnClickListener(
					new DB_CL(actv, dlg, dlg2));
		

//		Dialog dlg2 = dlg_template_okCancel(
//				actv, R.layout.dlg_checkactv_change_serial_num,
//				R.string.dlg_checkactv_change_serial_num_title,
//				
//				R.id.dlg_checkactv_change_serial_num_btn_ok,
//				R.id.dlg_checkactv_change_serial_num_btn_cancel,
//
//				Methods.DialogButtonTags.dlg_checkactv_change_serial_num_btn_ok,
//				Methods.DialogButtonTags.dlg_generic_dismiss_second_dialog
//				);


		dlg2.show();

		
	}//public static void dlg_checkactv_long_click_lv_change_serial_num

	public static boolean is_numeric(String num_string) {
		
//		return num_string.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+ ");
		return num_string.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+");

	}//public static boolean is_numeric(String num_string)

	public static void checkactv_change_order(Activity actv, Dialog dlg,
			Dialog dlg2, int item_position) {
		/*********************************
		 * 1. Get new position
		 * 2. Change order
		 * 
		 * 3. Sort list
		 * 4. Notify adapter
		 * 
		 * 5. Dismiss dialogues
		 * 
		 * 6. Update db
		 *********************************/
		
		checkactv_change_order_1_change_order(actv, dlg, dlg2, item_position);
		
		checkactv_change_order_2_update_data(actv, dlg, dlg2, item_position);
		
//		/*********************************
//		 * 1. Get new position
//		 *********************************/
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "item_position: " + item_position);
//		
//		EditText et = (EditText) dlg2.findViewById(R.id.dlg_checkactv_change_serial_num_et_new);
//		
////		// debug
////		Toast.makeText(actv, et.getText().toString(), Toast.LENGTH_SHORT).show();
//		
//		int new_position = Integer.parseInt(et.getText().toString());
//		
//		/*********************************
//		 * 2. Change order
//		 *********************************/
//		if (new_position >= item_position) {
//			
//			int i;
//			
//			// Items before the target
////			for (i = item_position + 1; i <= new_position; i++) {
//			for (i = item_position + 1; i < new_position; i++) {
//				
//				CheckActv.iList.get(i).setSerial_num(
//								CheckActv.iList.get(i).getSerial_num() - 1);
//				
//			}//for (int i = item_position + 1; i < new_position; i++)
//			
//			// Target
//			CheckActv.iList.get(item_position).setSerial_num(new_position);
//			
//			// Increment
////			i += 1;
//			
////			// Items before the target
////			for (; i < CheckActv.iList.size(); i++) {
////				
////				CheckActv.iList.get(i).setSerial_num(
////								CheckActv.iList.get(i).getSerial_num() + 1);
////				
////			}//for (int i = item_position + 1; i < new_position; i++)
//			
//		} else if (new_position < item_position) {//if (new_position >= item_position)
//
//			int i;
//			
//			for (i = new_position ; i < item_position; i++) {
//				
//				CheckActv.iList.get(i).setSerial_num(
//								CheckActv.iList.get(i).getSerial_num() + 1);
//				
//			}//for (int i = item_position + 1; i < new_position; i++)
//			
//			// Target
////			CheckActv.iList.get(item_position).setSerial_num(new_position);
//			CheckActv.iList.get(item_position).setSerial_num(new_position + 1);
//			
//		} else {//if (new_position >= item_position)
//			
//			
//			
//		}//if (new_position >= item_position)
//		
//		/*********************************
//		 * 3. Sort list
//		 *********************************/
//		sort_item_list_by_serial_num(actv);
//		
//		/*********************************
//		 * 4. Notify adapter
//		 *********************************/
//		CheckActv.ilAdp.notifyDataSetChanged();
//		
//		/*********************************
//		 * 5. Dismiss dialogues
//		 *********************************/
//		dlg.dismiss();
//		dlg2.dismiss();
		
	}//public static void checkactv_change_order()

	public static void checkactv_change_order_1_change_order(Activity actv, Dialog dlg,
			Dialog dlg2, int item_position) {
		/*********************************
		 * 1. Get new position
		 * 2. Change order
		 * 
		 * 3. Sort list
		 * 4. Notify adapter
		 * 
		 * 5. Dismiss dialogues
		 * 
		 * 6. Update db
		 *********************************/
		
		/*********************************
		 * 1. Get new position
		 *********************************/
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "item_position: " + item_position);
		
		EditText et = (EditText) dlg2.findViewById(R.id.dlg_checkactv_change_serial_num_et_new);
		
//		// debug
//		Toast.makeText(actv, et.getText().toString(), Toast.LENGTH_SHORT).show();
		
		int new_position = Integer.parseInt(et.getText().toString());
		
		/*********************************
		 * 2. Change order
		 *********************************/
		if (new_position >= item_position) {
			
			int i;
			
			// Items before the target
			for (i = item_position + 1; i <= new_position; i++) {
//			for (i = item_position + 1; i < new_position; i++) {
				
				CheckActv.iList.get(i).setSerial_num(
								CheckActv.iList.get(i).getSerial_num() - 1);
				
			}//for (int i = item_position + 1; i < new_position; i++)
			
			// Target
//			CheckActv.iList.get(item_position).setSerial_num(new_position);
			CheckActv.iList.get(item_position).setSerial_num(new_position + 1);
			
			// Increment
//			i += 1;
			
//			// Items before the target
//			for (; i < CheckActv.iList.size(); i++) {
//				
//				CheckActv.iList.get(i).setSerial_num(
//								CheckActv.iList.get(i).getSerial_num() + 1);
//				
//			}//for (int i = item_position + 1; i < new_position; i++)
			
		} else if (new_position < item_position) {//if (new_position >= item_position)

			int i;
			
			for (i = new_position ; i < item_position; i++) {
				
				CheckActv.iList.get(i).setSerial_num(
								CheckActv.iList.get(i).getSerial_num() + 1);
				
			}//for (int i = item_position + 1; i < new_position; i++)
			
			// Target
//			CheckActv.iList.get(item_position).setSerial_num(new_position);
			CheckActv.iList.get(item_position).setSerial_num(new_position + 1);
			
		} else {//if (new_position >= item_position)
			
			
			
		}//if (new_position >= item_position)
		
		/*********************************
		 * 3. Sort list
		 *********************************/
		sort_item_list_by_serial_num(actv);
		
		/*********************************
		 * 4. Notify adapter
		 *********************************/
		CheckActv.ilAdp.notifyDataSetChanged();
		
		/*********************************
		 * 5. Dismiss dialogues
		 *********************************/
		dlg.dismiss();
		dlg2.dismiss();
		
	}//public static void checkactv_change_order()

	public static void checkactv_change_order_2_update_data(Activity actv, Dialog dlg,
			Dialog dlg2, int item_position) {
		/*********************************
		 * 6. Update db
		 *********************************/

		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "checkactv_change_order_2_update_data => Starts");
		/*********************************
		 * 6. Update db
		 *********************************/
		HashMap<Long, Integer> data = new HashMap<Long, Integer>();
		
		for (int i = 0; i < CheckActv.iList.size(); i++) {
			
			data.put(CheckActv.iList.get(i).getDb_id(), CheckActv.iList.get(i).getSerial_num());
			
		}//for (int i = 0; i < CheckActv.iList.size(); i++)
		
		boolean res = DBUtils.updateData_items(
							actv, CONS.DB.dbName, CONS.DB.tname_items, data);
		
		
	}//public static void checkactv_change_order()

	public static void db_backup(Activity actv) {
		/*----------------------------
		 * 1. Prep => File names
		 * 2. Prep => Files
		 * 2-2. Folder exists?
		 * 3. Copy
			----------------------------*/
		String time_label = Methods.get_TimeLabel(Methods.getMillSeconds_now());
		
		String db_src = StringUtils.join(
							new String[]{
								CONS.DB.dirPath_db, 
								CONS.DB.dbName}, 
						File.separator);
		
		String db_dst = StringUtils.join(
							new String[]{
								CONS.DB.dirPath_db_backup, 
								CONS.DB.fileName_db_backup_trunk}, 
						File.separator);
		
		db_dst = db_dst + "_" + time_label + CONS.DB.fileName_db_backup_ext;
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "db_src: " + db_src + " * " + "db_dst: " + db_dst);
		
		/*----------------------------
		 * 2. Prep => Files
			----------------------------*/
		File src = new File(db_src);
		File dst = new File(db_dst);
		
		/*----------------------------
		 * 2-2. Folder exists?
			----------------------------*/
		File db_backup = new File(CONS.DB.dirPath_db_backup);
		
		if (!db_backup.exists()) {
			
			try {
				db_backup.mkdir();
				
				// Log
				Log.d("Methods.java" + "["
						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
						+ "]", "Folder created: " + db_backup.getAbsolutePath());
			} catch (Exception e) {
				
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Create folder => Failed");
				
				return;
				
			}
			
		} else {//if (!db_backup.exists())
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Folder exists: ");
			
		}//if (!db_backup.exists())
		
		/*----------------------------
		 * 3. Copy
			----------------------------*/
		try {
			FileChannel iChannel = new FileInputStream(src).getChannel();
			FileChannel oChannel = new FileOutputStream(dst).getChannel();
			iChannel.transferTo(0, iChannel.size(), oChannel);
			iChannel.close();
			oChannel.close();
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "File copied");
			
			// debug
			Toast.makeText(actv, "DB backup => Done", 3000).show();
			
		} catch (FileNotFoundException e) {
			// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception: " + e.toString());
			
		} catch (IOException e) {
			// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception: " + e.toString());
		}//try

		
	}//public static void db_backup(Activity actv, Dialog dlg, String item)
	
	public static void dlg_checkactv_long_click_lv_edit_item_text(
			Activity actv, Dialog dlg, int item_position) {
		/*********************************
		 * 1. Dialog setup
		 * 2. Get views
		 * 3. Set tags
		 * 
		 * 3-2. Set current text
		 * 
		 * 4. Add listeners => OnTouch
		 * 5. Add listeners => OnClick
		 * 
		 * 6. Show dialog
		 *********************************/
		
		Dialog dlg2 = new Dialog(actv);
		
		//
		dlg2.setContentView(R.layout.dlg_checkactv_edit_item_text);
		
		// Title
		dlg2.setTitle(R.string.dlg_checkactv_edit_item_text_title);

		/*********************************
		 * 2. Get views
		 *********************************/
		//
		Button btn_ok = 
			(Button) dlg2.findViewById(R.id.dlg_checkactv_edit_item_text_btn_ok);
		
		Button btn_cancel = 
				(Button) dlg2.findViewById(R.id.dlg_checkactv_edit_item_text_btn_cancel);
		
		/*********************************
		 * 3. Set tags
		 *********************************/
		//
		btn_ok.setTag(
				Tags.DialogButtonTags.dlg_checkactv_edit_item_text_btn_ok);
		
		btn_cancel.setTag(
				Tags.DialogButtonTags.DLG_GENERIC_DISMISS_SECOND_DIALOG);
		
		/*********************************
		 * 3-2. Set current text
		 *********************************/
		EditText et = (EditText) dlg2.findViewById(R.id.dlg_checkactv_edit_item_text_et);
		
		String text;
		
		if (CheckActv.iList.get(item_position) != null && 
				CheckActv.iList.get(item_position).getText() != null) {
			
			text = CheckActv.iList.get(item_position).getText().toString();
			
		} else {
			
			text = "";
			
		}
		
//		String text = CheckActv.iList.get(item_position).getText().toString();
		
		et.setText(text);
		
//		et.setSelection(0);
		
		et.setSelection(text.length());
		
		/*********************************
		 * 4. Add listeners => OnTouch
		 *********************************/
		//
		btn_ok.setOnTouchListener(new DB_TL(actv, dlg));
		btn_cancel.setOnTouchListener(new DB_TL(actv, dlg));
		
		/*********************************
		 * 5. Add listeners => OnClick
		 *********************************/
		//
		btn_ok.setOnClickListener(new DB_CL(actv, dlg, dlg2, item_position));
		btn_cancel.setOnClickListener(
					new DB_CL(actv, dlg, dlg2));
		
		/*********************************
		 * 6. Show dialog
		 *********************************/
		dlg2.show();
		
	}//public static void dlg_checkactv_long_click_lv_edit_item_text

	public static void update_item_text(Activity actv, Dialog dlg, Dialog dlg2, int item_position) {
		/*********************************
		 * 1. Get => New text
		 * 2. Get => Item id
		 * 3. Update data
		 *********************************/
		/*********************************
		 * 1. Get => New text
		 *********************************/
		EditText et = (EditText) dlg2.findViewById(R.id.dlg_checkactv_edit_item_text_et);

		String new_text = "";
		
		if (et != null && et.getText() != null) {
			
			new_text = et.getText().toString();
			
		}
		
		/*********************************
		 * 2. Get => Item id
		 *********************************/
		long item_id;
		
		if (CheckActv.iList != null && CheckActv.iList.get(item_position) != null) {
			
			item_id = CheckActv.iList.get(item_position).getDb_id();
			
		} else {
			
			// debug
			Toast.makeText(actv, 
					"Error: Something wrong with CheckActv.iList", 
					Toast.LENGTH_LONG).show();
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Error: Something wrong with CheckActv.iList");
			
			return;
		}
		
//		long item_id = CheckActv.iList.get(item_position).getDb_id();
		
		/*********************************
		 * 3. Update data
		 *********************************/
		boolean res = DBUtils.updateData_items_text(
									actv, CONS.DB.dbName, 
									CONS.DB.tname_items, 
									item_id, new_text);
		
		if (res == true) {
			
			// debug
			Toast.makeText(actv, "Text updated", Toast.LENGTH_SHORT).show();
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Text updated");
			
			dlg.dismiss();
			dlg2.dismiss();
			
			/*********************************
			 * Refresh item list
			 *********************************/
			Methods.refresh_item_list(actv);
			
			return;
			
		} else {//if (res == true)

			// debug
			Toast.makeText(actv, "Text update => Failed", Toast.LENGTH_SHORT).show();
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Text update => Failed");
			
			return;
			
		}//if (res == true)
		
		
	}//public static void update_item_text(Activity actv, Dialog dlg, Dialog dlg2)

	
	public static boolean
	update_item_all_status
	(Activity actv, String dbName, String tableName) {
		/*********************************
		 * 1. Set up db
		 * 
		 *********************************/
//		boolean res = true;
		
		boolean res = 
				DBUtils.updateData_Items_Status_All(actv, CheckActv.iList);
		
//		for (Item item : CheckActv.iList) {
//			
//			boolean local_res = DBUtils.updateData_items_status(
//						actv, dbName, tableName, 
//						item.getDb_id(), item.getStatus());
//			
//			if (local_res == false) {
//				
//				res = false;
//			}
//		}//for (Item item : CheckActv.iList)
		
		// Log
		String msg_Log = "Update item: res => " + res;
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", msg_Log);
		
		return res;
		
	}//public static boolean update_item_all_status()

	/**********************************************
	 * <Return>
	 * 	false	=> Column exists
	 **********************************************/
	public static boolean
	add_column_to_table
	(Activity actv,
			String dbName, String tableName,
			String column_name, String data_type) {
		/*********************************
		 * 1. Column already exists?
		 * 2. db setup
		 * 
		 * 3. Build sql
		 * 4. Exec sql
		 *********************************/
		/*********************************
		 * 1. Column already exists?
		 *********************************/
		String[] cols = Methods.get_column_list(actv, dbName, tableName);
		
		//debug
		for (String col_name : cols) {

			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "col: " + col_name);
			
		}//for (String col_name : cols)

		
		for (String col_name : cols) {
			
			if (col_name.equals(column_name)) {
				
				// debug
				Toast.makeText(actv, "Column exists: " + column_name, Toast.LENGTH_SHORT).show();
				
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Column exists: " + column_name);
				
				return false;
				
			}
			
		}//for (String col_name : cols)
		
		// debug
		Toast.makeText(actv, "Column doesn't exist: " + column_name, Toast.LENGTH_SHORT).show();
		
		/*********************************
		 * 2. db setup
		 *********************************/
		DBUtils dbu = new DBUtils(actv, dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		/*********************************
		 * 3. Build sql
		 *********************************/
		String sql = "ALTER TABLE " + tableName + 
					" ADD COLUMN " + column_name + 
					" " + data_type;
		
		/*********************************
		 * 4. Exec sql
		 *********************************/
		try {
//			db.execSQL(sql);
			wdb.execSQL(sql);
			
			// Log
			Log.d(actv.getClass().getName() + 
					"["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Column added => " + column_name);

			wdb.close();
			
			return true;
			
		} catch (SQLException e) {
			// Log
			Log.d(actv.getClass().getName() + 
					"[" + Thread.currentThread().getStackTrace()[2].getLineNumber() + "]", 
					"Exception => " + e.toString());
			
			wdb.close();
			
			return false;
		}//try


		
	}//public static boolean add_column_to_table()

	public static boolean write_log(
						Activity actv, 
						String text, 
						String file_name, String line_num) {
		
		
		return false;
		
	}//public static boolean write_log

	
	public static void dlg_filter_by_genre(Activity actv) {
		/*********************************
		 * 1. Genre data exist?
		 * 2. Set up dialog
		 * 
		 * 3. Get view
		 * 4. Prep list
		 * 
		 * 5. Set up adapter
		 *********************************/
		List<String> genre_list = Methods.get_all_data_genres(actv);

		// All data
		genre_list.add(actv.getString(R.string.generic_label_all));
		
//		if (genre_list == null) {
//			
//			// debug
//			Toast.makeText(actv, "No genre data", Toast.LENGTH_SHORT).show();
//			
//			return;
//			
//		}
		
		/*********************************
		 * 2. Set up dialog
		 *********************************/
		Dialog dlg = dlg_template_cancel(actv, 
				R.layout.dlg_filter_by_genre, R.string.dlg_filter_by_genre_title,
				R.id.dlg_filter_by_genre_btn_cancel,
				Tags.DialogButtonTags.dlg_generic_dismiss);

		/*********************************
		 * 3. Get view
		 *********************************/
		ListView lv = (ListView) dlg.findViewById(R.id.dlg_filter_by_genre_lv);
		
//		lv.setTag(Methods.DialogItemTags.dlg_filter_by_genre_lv);
		lv.setTag(Methods.DialogItemTags.DLG_FILTER_BY_GENRE_LV);
		
		/*********************************
		 * 5. Set up adapter
		 *********************************/
		ArrayAdapter<String> adp = new ArrayAdapter<String>(
		
				actv,
				android.R.layout.simple_list_item_1,
				genre_list
		);
		
		/*----------------------------
		 * 2.4. Set adapter
			----------------------------*/
		lv.setAdapter(adp);

		/*----------------------------
		 * 3. Set listener => list
			----------------------------*/
		lv.setOnItemClickListener(
						new DOI_CL(
								actv, 
								dlg));
		
		/*----------------------------
		 * 9. Show dialog
			----------------------------*/
		dlg.show();
		
		
	}//public static void dlg_filter_by_genre(Activity actv)

	private static List<String> get_all_data_genres(Activity actv) {
		/*********************************
		 * 1. Set up db
		 * 2. Query
		 * 
		 * 3. Build list
		 * 4. Return value
		 * 
		 *********************************/
		DBUtils dbu = new DBUtils(actv, CONS.DB.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		//=> source: http://stackoverflow.com/questions/4681744/android-get-list-of-tables : "Just had to do the same. This seems to work:"
		String q = "SELECT * FROM " + MainActv.tableName_genres;
		
		Cursor c = null;
		try {
			
			c = rdb.rawQuery(q, null);
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "c.getCount(): " + c.getCount());

		} catch (Exception e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
			rdb.close();
			
			return null;
		}
		
		/*********************************
		 * 3. Build list
		 *********************************/
		c.moveToFirst();
		
//		String[] genres = new String[c.getCount()];
		
		List<String> genre_list = new ArrayList<String>();
		
		for (int i = 0; i < c.getCount(); i++) {
			
//			genres[i] = c.getString(3);
			genre_list.add(c.getString(3));
			
			c.moveToNext();
			
		}//for (int i = 0; i < c.getCount(); i++)
		
		
		/*********************************
		 * Close db
		 *********************************/
		rdb.close();
		
		/*********************************
		 * Sort list
		 *********************************/
		Collections.sort(genre_list);
		
		/*********************************
		 * 4. Return value
		 *********************************/
		return genre_list;
		
	}//private static List<String> get_all_data_genres(Activity actv)

	
	public static void 
	clear_Items_AllToZero
	(Activity actv, long pos_InAdapter, Dialog dlg) {
		/*********************************
		 * 1. Update data
		 * 2. If successfull
		 * 	1. Dismiss dialog
		 * 	2. Refresh list 
		 *********************************/
		boolean res = DBUtils.update_items_all_to_zero(
					actv, CONS.DB.dbName, CONS.DB.tname_items, pos_InAdapter);
		
		if (res == true) {
			
			/*********************************
			 * 2.1. Dismiss dialog
			 *********************************/
			dlg.dismiss();
			
//			Methods.refresh_list_check_list(actv);
			CONS.MainActv.mlAdp.notifyDataSetChanged();
			
			
		}//if (res == true)
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "res=" + res);
		
		
		
	}//public static void clear_items_all_to_zero(Activity actv, long check_list_id)

	private static void refresh_list_check_list(Activity actv) {
		/********************************
		 * 1. Set up db
		 * 2. Query
		 * 
		 * 3. Close db
		 * 
		 * 4. Build list
		 * 4-2. Sort list
		 * 
		 * 5. Set list to adapter
		 * 
		 * 6. Set adapter to view
		 ********************************/
		DBUtils dbu = new DBUtils(actv, CONS.DB.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();
		
		/********************************
		 * 2. Query
		 ********************************/
		String sql = "SELECT * FROM " + CONS.DB.tname_Check_Lists;
		
		Cursor c = null;
		
		try {
			
			c = rdb.rawQuery(sql, null);
			
			actv.startManagingCursor(c);
			
		} catch (Exception e) {
			// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
			rdb.close();
			
			return;
		}
		
		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "c.getCount() => " + c.getCount());

		
		/********************************
		 * 4. Build list
		 ********************************/
		if (c.getCount() < 1) {
			
			// debug
			Toast.makeText(actv, "No data yet", Toast.LENGTH_SHORT).show();
			
			/********************************
			 * 3. Close db
			 ********************************/
			rdb.close();

			return;
		}//if (c.getCount() < 1)

		c.moveToNext();
		
//		MainActv.CLList = new ArrayList<CL>();
		CONS.MainActv.CLList.clear();

		for (int i = 0; i < c.getCount(); i++) {
			
			CONS.MainActv.CLList.add(new CL(
					c.getString(3),
					c.getInt(4),
					
					c.getLong(0),
					c.getLong(1),
					c.getLong(2)
					));
			
			c.moveToNext();
			
		}//for (int i = 0; i < c.getCount(); i++)

		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "CONS.MainActv.CLList.size(): " + CONS.MainActv.CLList.size());
		
		rdb.close();
		
		/*********************************
		 * 4-2. Sort list
		 *********************************/
		boolean res = Methods.sort_list_CLList(actv, CONS.MainActv.CLList);
		
		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "res: " + res);
		
		/********************************
		 * 5. Set list to adapter
		 ********************************/
		CONS.MainActv.mlAdp.notifyDataSetChanged();
		
//		mlAdp = new MainListAdapter(
//				this,
//				R.layout.list_row_main,
//				CLList
//				);
		
		/********************************
		 * 6. Set adapter to view
		 ********************************/
//		setListAdapter(mlAdp);

		
	}//private static void refresh_list_check_list(Activity actv)

	public static boolean restore_db(Activity actv, String dbName,
			String src, String dst) {
		/*********************************
		 * 1. Setup db
		 * 2. Setup: File paths
		 * 3. Setup: File objects
		 * 4. Copy file
		 * 
		 *********************************/
		// Setup db
		DBUtils dbu = new DBUtils(actv, dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
	
		wdb.close();
	
		/*********************************
		 * 2. Setup: File paths
		 *********************************/
	
		/*********************************
		 * 3. Setup: File objects
		 *********************************/
		File f_src = new File(src);
		File f_dst = new File(dst);
	
		/*********************************
		 * 4. Copy file
		 *********************************/
		try {
			FileChannel iChannel = new FileInputStream(src).getChannel();
			FileChannel oChannel = new FileOutputStream(dst).getChannel();
			iChannel.transferTo(0, iChannel.size(), oChannel);
			iChannel.close();
			oChannel.close();
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]",
					"File copied from: " + src
					+ "/ to: " + dst);
			
			// If the method is not in the context of a thread,
			//	then, show a message
			if (Looper.myLooper() == Looper.getMainLooper()) {
				
				// debug
				Toast.makeText(actv, "DB restoration => Done", Toast.LENGTH_LONG).show();
				
			} else {//if (condition)
	
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "DB restoration => Done");
				
			}//if (condition)
			
			
			return true;
	
		} catch (FileNotFoundException e) {
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception: " + e.toString());
			
			return false;
			
		} catch (IOException e) {
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception: " + e.toString());
			
			return false;
			
		}//try
		
	}//private boolean restore_db()

	public static void
	delete_list
	(Activity actv, 
			long check_list_id, Dialog dlg, 
			CL check_list) {
		
		boolean res = DBUtils.delete_list(actv, check_list_id, check_list);
		
		if (res == true) {
			
			/*********************************
			 * 2.1. Dismiss dialog
			 *********************************/
			dlg.dismiss();
			
			Methods.refresh_list_check_list(actv);
			
			
		}//if (res == true)
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "res=" + res);

	}//delete_list(Activity actv, long check_list_id, Dialog dlg)

	/*********************************
	 * List<String> get_tableNames(Activity actv)
	 * @return	(1) List&lt;String&gt;<br/>
	 * 			(2) null	=> Cursor size less than 1
	 *********************************/
	public static
	List<String> get_tableNames(Activity actv) {
		/*********************************
		 * Setup: Db
		 *********************************/
		DBUtils dbu = new DBUtils(actv, CONS.DB.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();
		
		/*********************************
		 * Query
		 *********************************/
		String sql =
				"SELECT * FROM sqlite_master WHERE type='table'";
		
		Cursor c = rdb.rawQuery(sql, null);
		
		if (c.getCount() < 1) {
			
			// Log
			Log.d("["
					+ "Methods.java : "
					+ +Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + " : "
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "c.getCount() < 1");
			
			rdb.close();
			
			return null;
			
		}//if (cursor.getCount() < 1)

		/*********************************
		 * Get: Count
		 *********************************/
		List<String> tableNames = new ArrayList<String>();
		
		while(c.moveToNext()) {
			
//			tableNames.add(c.getString(0));	//=> Column '0' --> 'type'
			tableNames.add(c.getString(1));
			
		}
		
		/*********************************
		 * Return
		 *********************************/
		rdb.close();
		
		return tableNames;
		
	}//List<String> get_tableNames(Activity actv)

	public static void restore_db(Activity actv) {
		// TODO Auto-generated method stub

		String src_dir = CONS.DB.dirPath_db_backup;
		
		File f_dir = new File(src_dir);
		
		File[] src_dir_files = f_dir.listFiles();
		
		// If no files in the src dir, quit the method
		if (src_dir_files.length < 1) {
			
			// Log
			Log.d("DialogOnItemClickListener.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "No files in the dir: " + src_dir);
			
			return;
			
		}//if (src_dir_files.length == condition)

		File f_src_latest = src_dir_files[0];
		
		
		for (File file : src_dir_files) {
			
			if (f_src_latest.lastModified() < file.lastModified()) {
						
				f_src_latest = file;
				
			}//if (variable == condition)
			
		}//for (File file : src_dir_files)
		
		// Show the path of the latest file
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "f_src_latest=" + f_src_latest.getAbsolutePath());
		
		/*********************************
		 * Restore file
		 *********************************/
		String src = f_src_latest.getAbsolutePath();

		
		String dst = StringUtils.join(
						new String[]{
								CONS.DB.dirPath_db,
								CONS.DB.dbName},
						File.separator);

		// Log
		Log.d("DialogOnItemClickListener.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ ":"
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]",
				"src=" + src
				+ "|"
				+ "dst=" + dst);

		boolean res = Methods.restore_db(actv, CONS.DB.dbName, src, dst);

	}//public static void restore_db()

	public static int backupDb(Activity actv,
			String dbName, String dirPathBk) {
		/*----------------------------
		* 1. Prep => File names
		* 2. Prep => Files
		* 2-2. Folder exists?
		* 3. Copy
		----------------------------*/
		//String time_label = Methods.get_TimeLabel(Methods.getMillSeconds_now());
		String timeLabel = Methods.getTimeLabel(Methods.getMillSeconds_now());
		
		String db_src = StringUtils.join(
					new String[]{
							CONS.DB.dirPath_db,
							dbName},
					File.separator);
		
		String db_dst = StringUtils.join(
					new String[]{
							dirPathBk,
							CONS.DB.fileName_db_backup_trunk},
					File.separator);
		
		db_dst = db_dst + "_" + timeLabel + CONS.DB.fileName_db_backup_ext;
		
		// Log
		Log.d("Methods.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "db_src: " + db_src + " * " + "db_dst: " + db_dst);
		
		/*----------------------------
		* 2. Prep => Files
		----------------------------*/
		File src = new File(db_src);
		File dst = new File(db_dst);
		
		/*********************************
		* DB file exists?
		*********************************/
		File f = new File(db_src);
		
		if (f.exists()) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "DB file exists=" + f.getAbsolutePath());
		} else {//if (f.exists())
		
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "File doesn't exist=");
			
			return CONS.RetVal.DB_DOESNT_EXIST;
		
		}//if (f.exists())
		
		/*----------------------------
		* 2-2. Folder exists?
		----------------------------*/
		File db_backup = new File(dirPathBk);
		
		if (!db_backup.exists()) {
		
		try {
			db_backup.mkdir();
		
			// Log
			Log.d("Methods.java"
					+ "["
					+ Thread.currentThread().getStackTrace()[2]
							.getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2]
							.getMethodName() + "]",
					"Folder created: " + db_backup.getAbsolutePath());
			
		} catch (Exception e) {
			
			// Log
			Log.d("Methods.java"
					+ "["
					+ Thread.currentThread().getStackTrace()[2]
							.getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2]
							.getMethodName() + "]",
					"Create folder => Failed");
			
			return CONS.RetVal.DB_CANT_CREATE_FOLDER;
			
		}
		
		} else {//if (!db_backup.exists())
		
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "Folder exists: " + db_backup.getAbsolutePath());
		
		}//if (!db_backup.exists())
		
		/*----------------------------
		* 3. Copy
		----------------------------*/
		try {
			FileChannel iChannel = new FileInputStream(src).getChannel();
			FileChannel oChannel = new FileOutputStream(dst).getChannel();
			iChannel.transferTo(0, iChannel.size(), oChannel);
			iChannel.close();
			oChannel.close();
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "File copied");
			
			return CONS.RetVal.DB_BACKUP_SUCCESSFUL;
		
		} catch (FileNotFoundException e) {
		
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "Exception: " + e.toString());
			
			return CONS.RetVal.DB_FILE_COPY_EXCEPTION;
		
		} catch (IOException e) {
		
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "Exception: " + e.toString());
			
			return CONS.RetVal.DB_FILE_COPY_EXCEPTION;
		
		}//try
	
	}//public static void backupDb()

	public static String getTimeLabel(long millSec) {
		
		 SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd_HHmmss");
		 
		return sdf1.format(new Date(millSec));
		
	}//public static String get_TimeLabel(long millSec)

	public static int
	getArrayIndex
	(String[] targetArray, String targetString) {
		int index = -1;
		
		for (int i = 0; i < targetArray.length; i++) {
			
			if (targetArray[i].equals(targetString)) {
				
				index = i;
				
				break;
				
			}//if (targetArray[i] == )
			
		}//for (int i = 0; i < targetArray.length; i++)
		
		return index;
	}//public static int getArrayIndex(String[] targetArray, String targetString)

	public static String convert_Kana2Gana(String s) {
		StringBuffer sb = new StringBuffer(s);
		
//		for (int i = 0; i < sb.length(); i++) {
//		
//			char c = sb.charAt(i);
//			
//			if (c >= '繧｡' && c <= '繝ｳ') {
//				
//				sb.setCharAt(i, (char)(c - '繧｡' + '縺�'));
//				
//			} else if (c == '繝ｵ') {
//				
//				sb.setCharAt(i, '縺�');
//				
//			} else if (c == '繝ｶ') {
//				
//				sb.setCharAt(i, '縺�');
//
//			} else if (c == '繝ｴ') {
//
//				sb.setCharAt(i, '縺�');
//
//				i++;
//			}
//			
//		}//for (int i = 0; i < sb.length(); i++)
		
		return sb.toString(); 
		
	}//public static String convert_Kana2Gana(String s)

	public static String
	get_ElapsedTime(long t_Start, long t_End) {

		long l_elapsedTime = t_End - t_Start;
		
		/*********************************
		 * mill & seconds
		 *********************************/
		long mill = l_elapsedTime % 1000;
		
		long seconds = l_elapsedTime / 1000;
		
		/*********************************
		 * min
		 *********************************/
		long min = seconds / 60;
		
		seconds = seconds % 60;
		
		/*********************************
		 * hour
		 *********************************/
		long hour = min / 60;
		
		min = min % 60;
		
		/*********************************
		 * Build: Label
		 *********************************/
		String label = StringUtils.join(
							new String[]{
								String.valueOf(hour),
								String.valueOf(min),
								String.valueOf(seconds)
							},
							
							Methods.Lib.separatorColon);
		
		label += "." + String.valueOf(mill);
		
		return label;
		
	}//get_ElapsedTime(long t_Start, long t_End)

	static class Lib {
		
		public static final String separatorColon	= ":";
		
	}

	/*********************************
	 * @param item_position => Integer number in CheckActv.iList
	 * @param dlg1 => By long click on an item in CheckActv.iList
	 * @param item => Item class
	 * @return void
	 * @see Overload => Exists
	 *********************************/
	public static void
	dlg_checkactv_long_click_lv_delete_item
	(Activity actv, Dialog dlg1,
			int item_position, Item item) {
		// Delete item from: db
		
		// Update serial num of each item
		// Ending:
		//		- Close db
		//		- Dismiss dlg1
		
		/*********************************
		 * Setup: Db
		 *********************************/
		DBUtils dbu = new DBUtils(actv, CONS.DB.dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		/*********************************
		 * Validate: Item exists?
		 *********************************/
		boolean result = DBUtils.isInTable(
				actv,
				wdb,
				CONS.DB.tableNames.items.toString(),
				android.provider.BaseColumns._ID,
				item.getDb_id());
		
		if (result == false) {		// Result is false ==> Meaning the target data doesn't exist
		//							in db; Hence, not executing delete op

			// Log
			String log_msg = 
					"Item list doesn't exist in db: "
					+ String.valueOf(item.getDb_id());

			Log.d("["
					+ "Methods.java : "
					+ +Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + " : "
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", log_msg);
			
			return;
		
		} else {//if (result == false)
		
			// Log
			Log.d("Methods.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", 
			"Item list exists in db(id="
			+ String.valueOf(item.getDb_id()) + ")");
		
		}//if (result == false)

		/*********************************
		 * Delete: Items
		 *********************************/
		String sql = 
				"DELETE FROM "
				+ CONS.DB.tableNames.items.toString()
				+ " WHERE "
				+ android.provider.BaseColumns._ID + " = '"
				+ String.valueOf(item.getDb_id()) + "'";
		
		// Log
		Log.d("DBUtils.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "sql=" + sql);
		
		try {
			
			wdb.execSQL(sql);
			
			// debug
			Toast.makeText(actv, "Items deleted", Toast.LENGTH_LONG).show();
			
			// Log
			String log_msg = "Items deleted: "
							+ String.valueOf(item.getDb_id());

			Log.d("["
					+ "Methods.java : "
					+ +Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + " : "
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", log_msg);
			
		} catch (SQLException e) {
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
			// debug
			String toa_msg = "Deletion failed for "
							+ String.valueOf(item.getDb_id())
							+ "(" + e.toString() + ")";
			
			Toast.makeText(actv, toa_msg, Toast.LENGTH_LONG).show();
			
		}//try

		/*********************************
		 * Delete item from: iList
		 *********************************/
		CheckActv.iList.remove(item_position);
		
		/*********************************
		 * Notify the adapter
		 *********************************/
		CheckActv.ilAdp.notifyDataSetChanged();
		
		/*********************************
		 * Update serial num of each item
		 *********************************/
		boolean res = Methods_ic.update_ItemsList(CheckActv.iList);
		
		/*********************************
		 * Ending operations
		 *********************************/
		wdb.close();
		
		dlg1.dismiss();
		
	}//dlg_checkactv_long_click_lv_delte_item

	public static void
	copy_External_DB(Activity actv) {
		// TODO Auto-generated method stub
		
//		String fPath_DB_Src = "/data/data/ic.main/databases"
//							+ "/"
//							+ "ic.db";

		////////////////////////////////

		// File: src

		////////////////////////////////
		String src_dir = "/mnt/sdcard-ext" + "/IC_backup";
		
		File f_dir = new File(src_dir);
		
		File[] src_dir_files = f_dir.listFiles();
		
		// If no files in the src dir, quit the method
		if (src_dir_files.length < 1) {
			
			// Log
			Log.d("DialogOnItemClickListener.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "No files in the dir: " + src_dir);
			
			return;
			
		}//if (src_dir_files.length == condition)

		File f_src_latest = src_dir_files[0];
		
		
		for (File file : src_dir_files) {
			
			if (f_src_latest.lastModified() < file.lastModified()) {
						
				f_src_latest = file;
				
			}//if (variable == condition)
			
		}//for (File file : src_dir_files)
		
		// Show the path of the latest file
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "f_src_latest=" + f_src_latest.getAbsolutePath());
		
		String src = f_src_latest.getAbsolutePath();

		////////////////////////////////

		// File: dst

		////////////////////////////////
		String dst = StringUtils.join(
				new String[]{
						CONS.DB.dirPath_db,
						CONS.DB.dbName},
				File.separator);		

		////////////////////////////////

		// Restore

		////////////////////////////////
//		// Log
//		String msg_Log = "src = " + src
//						+ " *** "
//						+ "dst = " + dst;
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", msg_Log);
		
		boolean res = Methods.restore_db(actv, 
									CONS.DB.dbName, 
									src, 
									dst);
		
		// Log
		String msg_Log = "res = " + res;
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", msg_Log);
		
		
		
	}//copy_External_DB(Activity actv)

	public static void 
	edit_List_Title
	(Activity actv, 
		Dialog dlg1, Dialog dlg2, int item_position) {
		// TODO Auto-generated method stub
		
		CL list = (CL) CONS.MainActv.lvMain.getItemAtPosition(item_position);
		
		// Log
		String msg_Log = "list.getName() => " + list.getName();
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", msg_Log);
		
		////////////////////////////////

		// get: new title

		////////////////////////////////
//		dlg_checkactv_edit_item_text
		EditText et_Title = 
				(EditText) dlg2.findViewById(R.id.dlg_checkactv_edit_item_text_et);
		
		////////////////////////////////

		// update: DB

		////////////////////////////////
//		cols_check_lists
//		"name",	"genre_id", "yomi"	// 0,1,2

		boolean res = DBUtils.updateData_CheckList(actv, 
								list.getDb_id(), CONS.DB.cols_check_lists[0], 
								et_Title.getText().toString());
		
		if (res == false) {
			
			// debug
			String msg_Toast = "Update => can't be done";
			Toast.makeText(actv, msg_Toast, Toast.LENGTH_SHORT).show();
			
			return;
			
		}
		
		////////////////////////////////

		// update: listview

		////////////////////////////////
		list.setName(et_Title.getText().toString());
		
		// Log
		msg_Log = "New title => set";
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", msg_Log);
		
		////////////////////////////////

		// notify

		////////////////////////////////
		CONS.MainActv.mlAdp.notifyDataSetChanged();
		
		////////////////////////////////

		// close: dialogues

		////////////////////////////////
		dlg2.dismiss();
		dlg1.dismiss();
		
	}//edit_List_Title

	public static void
	change_Genre
	(Activity actv, Dialog dlg1, Dialog dlg2,
			int item_position) {
		// TODO Auto-generated method stub
		
		Spinner sp = (Spinner) dlg2.findViewById(R.id.dlg_change_genre_sp);
		
		String genre_name = (String) sp.getSelectedItem();

		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "genre_name: " + genre_name);
		
		int genre_id = Methods.get_genre_id_from_genre_name(actv, genre_name);
		
		if (genre_id < 0) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "genre_id: " + genre_id);
			
			// debug
			Toast.makeText(actv, "�ｿｽW�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ謫ｾ�ｿｽﾅゑｿｽ�ｿｽﾜゑｿｽ�ｿｽ�ｿｽ", Toast.LENGTH_SHORT).show();
			
		} else {//if (genre_id < 0)
			
			// Log
			String msg_Log = "genre_id = " + genre_id;
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", msg_Log);
			
		}//if (genre_id < 0)
		
		////////////////////////////////

		// update: DB

		////////////////////////////////
		CL list = (CL) CONS.MainActv.lvMain.getItemAtPosition(item_position);
		
//		cols_check_lists
//		"name",	"genre_id", "yomi"	// 0,1,2
		boolean res = DBUtils.updateData_CheckList__GenreID(
										actv, 
										list.getDb_id(), 
										genre_id);
		
		if (res == false) {
			
			// debug
			String msg_Toast = "Update genr id => Can't be done";
			Toast.makeText(actv, msg_Toast, Toast.LENGTH_SHORT).show();
			
			return;
			
		}
		
		////////////////////////////////

		// update: list

		////////////////////////////////
		list.setGenre_id(genre_id);
		
		CONS.MainActv.mlAdp.notifyDataSetChanged();
		
		// Log
		String msg_Log = "CONS.MainActv.mlAdp => notified";
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", msg_Log);
		
		////////////////////////////////

		// dialogues: close

		////////////////////////////////
		dlg2.dismiss();
		dlg1.dismiss();
		
		
		
	}//change_Genre

	public static void 
	edit_CL
	(Activity actv, 
			Dialog dlg1, Dialog dlg2,
			int item_position) {
		// TODO Auto-generated method stub
	
		////////////////////////////////

		// Get: spinner

		////////////////////////////////
		Spinner sp = (Spinner) dlg2.findViewById(R.id.dlg_edit_cl_sp_genre);
		
		String genre_name = (String) sp.getSelectedItem();

		int genre_id = Methods.get_genre_id_from_genre_name(actv, genre_name);
		
		if (genre_id < 0) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "genre_id: " + genre_id);
			
			// debug
			Toast.makeText(actv, "�ｿｽW�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ謫ｾ�ｿｽﾅゑｿｽ�ｿｽﾜゑｿｽ�ｿｽ�ｿｽ", Toast.LENGTH_SHORT).show();
			
		} else {//if (genre_id < 0)
			
//			// Log
//			String msg_Log = "genre_id = " + genre_id;
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", msg_Log);
			
		}//if (genre_id < 0)
		
		////////////////////////////////

		// title

		////////////////////////////////
		EditText et_Title = 
				(EditText) dlg2.findViewById(R.id.dlg_edit_cl_et_title);
		
		////////////////////////////////

		// yomi

		////////////////////////////////
		EditText et_Yomi = 
				(EditText) dlg2.findViewById(R.id.dlg_edit_cl_et_yomi);
		

		////////////////////////////////

		// check list

		////////////////////////////////
		CL list = (CL) CONS.MainActv.lvMain.getItemAtPosition(item_position);
		
		////////////////////////////////

		// update: values

		////////////////////////////////
		list.setYomi(et_Yomi.getText().toString());
		list.setName(et_Title.getText().toString());
		list.setGenre_id(genre_id);
		
		// Log
		String msg_Log = "list.getYomi() => " + list.getYomi();
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", msg_Log);
		
		////////////////////////////////

		// update: DB

		//////////////////////////////////		cols_check_lists
//		"name",	"genre_id", "yomi"	// 0,1,2
		boolean res = DBUtils.updateData_CheckList__All(
										actv, 
										list
//										list.getDb_id()
										);
//		boolean res = DBUtils.updateData_CheckList__GenreID(
//				actv, 
//				list.getDb_id(), 
//				genre_id);
		
		if (res == false) {
			
			// debug
			String msg_Toast = "Update check list => Can't be done";
			Toast.makeText(actv, msg_Toast, Toast.LENGTH_SHORT).show();
			
			return;
			
		}
		
		////////////////////////////////

		// update: list

		////////////////////////////////
//		list.setGenre_id(genre_id);
		
		CONS.MainActv.mlAdp.notifyDataSetChanged();
		
		// Log
		msg_Log = "CONS.MainActv.mlAdp => notified";
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", msg_Log);
		
		////////////////////////////////

		// dialogues: close

		////////////////////////////////
		dlg2.dismiss();
		dlg1.dismiss();

		
		
	}//edit_CL

	public static void 
	dup_CL
	(Activity actv, 
			Dialog dlg1, Dialog dlg2,
			int item_position) {
		// TODO Auto-generated method stub
		
		////////////////////////////////

		// get: cl

		////////////////////////////////
		CL cl = (CL) CONS.MainActv.lvMain.getItemAtPosition(item_position);
		
//		_debug_D_2_V_1_1(item_position);
		
		////////////////////////////////

		// get: items

		////////////////////////////////
		String msg_Log;
		
		List<Item> items = DBUtils.get_Items_from_CL(actv, cl.getDb_id());
		
		// Log
		if (items == null) {
			
			// Log
			msg_Log = "items => null";
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", msg_Log);
			
		} else {

			msg_Log = "items.size() = " + items.size();
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", msg_Log);
			
		}
		
		////////////////////////////////

		// create: check list

		////////////////////////////////
		DBUtils dbu = new DBUtils(actv, CONS.DB.dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		String listName_new = cl.getName() + "(2)";
		
		// Log
		msg_Log = "cl.getYomi() => " + cl.getYomi();
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", msg_Log);
		
		
		boolean res = dbu.insertData_list(
							wdb, 
							listName_new, 
							cl.getGenre_id(),
							cl.getYomi() + "(2)");
//		boolean res = dbu.insertData_list(wdb, listName_new, cl.getGenre_id());
		
		if (res == false) {
			
			// Log
			msg_Log = "can't create check list: " + listName_new;
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", msg_Log);
			
			// debug
			Toast.makeText(actv, msg_Log, Toast.LENGTH_SHORT).show();
			
			return;
			
		}
		
		wdb.close();
		
		////////////////////////////////

		// get: newly created check list

		////////////////////////////////
		CL cl_New = DBUtils.get_CL_from_Name(actv, listName_new);
		
		// Log
		msg_Log = "cl.getDb_id() = " + cl.getDb_id()
					+ " / "
					+ "cl_New.getDb_id() = "
					+ cl_New.getDb_id()
				;
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", msg_Log);
		
		////////////////////////////////

		// create: items

		////////////////////////////////
		wdb = dbu.getWritableDatabase();
		
		int tmp_Count = 0;
		
		for (Item item : items) {

			Object[] data = {
					
					item.getText(), 
					item.getSerial_num(),
//					item.getList_id(),
					cl_New.getDb_id()
//					CheckActv.clList.getDb_id()
			};
			
			res = dbu.insertData_item(wdb, data);
			
			if (res == true) {
				
				tmp_Count ++;
				
			}
			
		}
		
		// Log
		msg_Log = "items => inserted: " + tmp_Count;
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", msg_Log);
		
		wdb.close();
		
		////////////////////////////////

		// rebuild: check list in MainActv

		////////////////////////////////
		
		
		////////////////////////////////

		// notify: adapter

		////////////////////////////////
		
		////////////////////////////////

		// dismiss dialogues

		////////////////////////////////
		dlg2.dismiss();
		dlg1.dismiss();
		
		////////////////////////////////

		// report

		////////////////////////////////
		// debug
		String msg_Toast = "Check list => duplicated";
		Toast.makeText(actv, msg_Toast, Toast.LENGTH_SHORT).show();
		
		
	}//dup_CL

	private static void _debug_D_2_V_1_1(int item_position) {
		// TODO Auto-generated method stub
		
		// Log
		String msg_Log;
		
		msg_Log = "item_position = " + item_position;
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", msg_Log);
		
		
		for (int i = 0; i < CONS.MainActv.lvMain.getChildCount(); i++) {
			
			CL cl = (CL) CONS.MainActv.lvMain.getItemAtPosition(i);
			
			// Log
			msg_Log = "message" + cl.getName()
							+ " / "
							+ "id = " + i
							;
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", msg_Log);
			
			
		}
		
	}

	public static void 
	delete_list
	(Activity actv, 
			Dialog dlg1, Dialog dlg2,
			int pos_InAdapter) {
		// TODO Auto-generated method stub
		////////////////////////////////

		// get: check list

		////////////////////////////////
		CL cl = (CL) CONS.MainActv.mlAdp.getItem(pos_InAdapter);
		
		////////////////////////////////

		// delete: DB

		////////////////////////////////
		boolean res = DBUtils.delete_list(actv, cl.getDb_id(), cl);
		
		/******************************
			validate
		 ******************************/
		if (res == false) {
			
			// Log
			String msg_Log = "Check list => can't be removed from DB";
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", msg_Log);
			
			// debug
			Toast.makeText(actv, msg_Log, Toast.LENGTH_SHORT).show();
			
			return;
			
		}
		
		////////////////////////////////

		// delete: from: adapter

		////////////////////////////////
		CONS.MainActv.mlAdp.remove(cl);
		
		CONS.MainActv.mlAdp.notifyDataSetChanged();
			
//			Methods.refresh_list_check_list(actv);
		
//		////////////////////////////////
//
//		// delete: items
//
//		////////////////////////////////
//		int res_i = DBUtils.delete_Items(actv, cl.getDb_id());
//
//		// debug
//		String msg_Toast = res_i + " items => deleted";
//		Toast.makeText(actv, msg_Toast, Toast.LENGTH_SHORT).show();
		
		////////////////////////////////

		// dialog: dismiss

		////////////////////////////////
		dlg2.dismiss();
		dlg1.dismiss();
			
			
	}//delete_list

}//public class Methods

