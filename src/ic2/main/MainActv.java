/*********************************
 * IC=ItemChecker
 * Date=20120914_205758
 *********************************/
package ic2.main;

import java.io.File;
import java.util.List;

import ic2.adapters.MainListAdapter;
import ic2.items.CL;
import ic2.listeners.Custom_OI_LCL;
import ic2.main.R;
import ic2.utils.CONS;
import ic2.utils.DBUtils;
import ic2.utils.Methods;
import ic2.utils.Methods_dlg;
import ic2.utils.Methods_ic;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

// Apache



public class MainActv extends ListActivity {

	/********************************
	 * Lists
	 ********************************/
//	public static List<CL> CLList;
	public static MainListAdapter mlAdp;
	
	/********************************
	 * DB
	 ********************************/

	// genres
	public static String tableName_genres = "genres";
	
	public static String[] cols_genres =		{"name"};
	
	public static String[] col_types_genres = 	{"TEXT"};

	
	/*********************************
	 * Intents
	 *********************************/
	public static String intent_list_id = "list_id";
	
	
	/*********************************
	 * Others
	 *********************************/
	public static Activity mainActv;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	/********************************
		 * 1. Set up
		 * 2. Create tables
		 * 
		 * 3. Show list
		 * 4. Initialise vars
		 * 
		 * 5. Set listeners
		 ********************************/
    	
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        this.setTitle(this.getClass().getName());
    
        //debug
//        do_debugs();
//        create_tables();
        
        /*********************************
		 * 4. Initialise vars
		 *********************************/
        mainActv = this;
        
        /*********************************
		 * 5. Set listeners
		 *********************************/
        _onCreate__SetListeners();
//		ListView lv = this.getListView();
		
//		CONS.MainActv.lvMain = this.getListView();
//		
////		lv.setTag(Methods.ItemTags.dir_list);
//		CONS.MainActv.lvMain.setTag(Methods.ListTags.actv_main_lv);
//		
//		CONS.MainActv.lvMain
//					.setOnItemLongClickListener(new Custom_OI_LCL(this));
        
		/*********************************
		 * Re-install the app
		 *********************************/
//		_debug_D_20_reinstall_app();
        
        do_debug();
		
    }//public void onCreate(Bundle savedInstanceState)

    private void do_debug() {
		// TODO Auto-generated method stub
		_debug_D_12_V_1_0();
		
	}

	private void _debug_D_12_V_1_0() {
		// TODO Auto-generated method stub
		File f = new File(CONS.DB.dirPath_db);
		
		String[] list = f.list();
		
		for (String name : list) {
			
			// Log
			String msg_Log = "name = " + name;
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", msg_Log);
			
		}
		
		
	}

	private void _onCreate__SetListeners() {
		// TODO Auto-generated method stub
    	
		CONS.MainActv.lvMain = this.getListView();
		
//		lv.setTag(Methods.ItemTags.dir_list);
		CONS.MainActv.lvMain.setTag(Methods.ListTags.actv_main_lv);
		
		CONS.MainActv.lvMain
					.setOnItemLongClickListener(new Custom_OI_LCL(this));

	}

	private void do_debugs() {
    	
//    	_debug_SEG_5_v_0_1();
    	
//    	_debug_D_25_v_3_0_1();
    	
//    	_debug_D_25_v_2_0();
    	
//		debug_D_24_v_1_0();
	}//private void do_debugs()

	private void 
	_onStart_ShowList() {
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
		DBUtils dbu = new DBUtils(this, CONS.DB.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		/*********************************
		 * Get: Preference
		 *********************************/
		SharedPreferences prefs = this
				.getSharedPreferences(
						CONS.Prefs.pname_IC,
						Context.MODE_PRIVATE);
		
		int pref_GenreId = prefs.getInt(
				CONS.Prefs.pkey_GenreId,

				CONS.Prefs.pkey_GenreId_IntValue);
		
		// Log
		Log.d("[" + "MainActv.java : "
				+ +Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "pref_GenreId = " + pref_GenreId);
		
		/********************************
		 * 2. Query
		 ********************************/
		String sql;
		
		if (pref_GenreId == -1 || pref_GenreId == 0) {
//			if (pref_GenreId == -1) {
			
			sql = "SELECT * FROM " + CONS.DB.tname_Check_Lists;
			
		} else {//if (savedPosition == -1)
			
					sql = "SELECT * FROM " + CONS.DB.tname_Check_Lists
					+ " WHERE " + CONS.DB.cols_check_lists[1] + "="
					+ pref_GenreId;
			
		}//if (savedPosition == -1)
		
		
		Cursor c = null;
		
		try {
			
			c = rdb.rawQuery(sql, null);
			
			startManagingCursor(c);
			
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
			Toast.makeText(this, "No data yet", Toast.LENGTH_SHORT).show();
			
			/********************************
			 * 3. Close db
			 ********************************/
			rdb.close();

			return;
		}//if (c.getCount() < 1)

		c.moveToNext();

		CONS.MainActv.CLList = Methods_ic.build_CL(this, c);
//		CLList = Methods_ic.build_CL(this, c);
		
		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "CONS.MainActv.CLList.size(): " + CONS.MainActv.CLList.size());
		
		rdb.close();
		
		/*********************************
		 * 4-2. Sort list: Default => by "yomi"
		 *********************************/
		// Log
		Log.d("[" + "MainActv.java : "
				+ +Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ " : "
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]", "Sorting list ...");
		
		String sortType = prefs.getString(
							this.getString(R.string.pref_key_sort_type),
							"NOUPDATE");

		TypedArray selectedValues = this.getResources()
		        	.obtainTypedArray(R.array.sort_types_values);
		
		// Log
		String log_msg = "sortType="
						+ sortType
						+ "/"
						+ "selectedValues.getString(0)="
						+ selectedValues.getString(0);

		Log.d("[" + "MainActv.java : "
				+ +Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ " : "
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]", log_msg);
		
		// created_at
		if (sortType.equals(selectedValues.getString(0))) {
			
			CONS.Admin.sortType = CONS.Admin.SortTypes.SortBy_CreatedAt;
	
		// item_name
		} else if (sortType.equals(selectedValues.getString(1))){//if (selectedValues.equals(o))
			
			CONS.Admin.sortType = CONS.Admin.SortTypes.SortBy_Yomi;
			
		} else {//if (selectedValues.equals(o))
			
			CONS.Admin.sortType = CONS.Admin.SortTypes.SortBy_Yomi;
			
		}//if (selectedValues.equals(o))
		
		boolean res = Methods_ic.sort_CheckList(
						this, CONS.Admin.sortType);
//					this,CONS.Admin.SortTypes.SortBy_Yomi);
		
//		boolean res = Methods.sort_list_CLList(this, CLList);
		
		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "res: " + res);
		
		/********************************
		 * 5. Set list to adapter
		 ********************************/
		CONS.MainActv.mlAdp = new MainListAdapter(
				this,
				R.layout.list_row_main,
				CONS.MainActv.CLList
				);
		
		/********************************
		 * 6. Set adapter to view
		 ********************************/
		setListAdapter(CONS.MainActv.mlAdp);
		
		//debug
//		do_debugs();
		
	}//_onStart_ShowList()

	@Override
	protected void onListItemClick(
					ListView l, View v, int position, long id) {
		/*********************************
		 * 1. Get item
		 * 2. Set up for intent
		 * 
		 * 3. Start
		 *********************************/
		////////////////////////////////

		// set: pref

		////////////////////////////////
		Methods.set_Pref_Int(
						this, 
						CONS.Prefs.pname_IC, 
						CONS.Prefs.pkey_LastVisiblePosition_MainActv, 
						l.getLastVisiblePosition());
		
		// Log
		String msg_Log = "Pref.lastvisible: set to => " 
						+ l.getLastVisiblePosition();
		
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", msg_Log);
		
		// reset default child count
		if (l.getChildCount() > 0) {
			
			CONS.Admin.dflt_ChildCount = l.getChildCount() - 1;
//			CONS.Admin.dflt_ChildCount = l.getChildCount();
			
			// Log
			msg_Log = "CONS.Admin.dflt_ChildCount = " 
							+ CONS.Admin.dflt_ChildCount;
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", msg_Log);
			
					
		}
		
		////////////////////////////////

		// get: CL

		////////////////////////////////
		CL clList = (CL) l.getItemAtPosition(position);
		
		/*********************************
		 * 2. Set up for intent
		 *********************************/
		Intent i = new Intent();
		
		i.setClass(this, CheckActv.class);
		
		i.putExtra(MainActv.intent_list_id, clList.getDb_id());
		
		/*********************************
		 * 3. Start
		 *********************************/
		startActivity(i);
		
		// debug
//		Toast.makeText(this, clList.getName(), Toast.LENGTH_SHORT).show();
//		Toast.makeText(this, "list_id=" + clList.getDb_id(), Toast.LENGTH_SHORT).show();
		
		super.onListItemClick(l, v, position, id);
		
	}//protected void onListItemClick(ListView l, View v, int position, long id)

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.main_menu, menu);

		return super.onCreateOptionsMenu(menu);
	}//public boolean onCreateOptionsMenu(Menu menu)

	@Override
	protected void onDestroy() {
		// TODO �ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ黷ｽ�ｿｽ�ｿｽ�ｿｽ\�ｿｽb�ｿｽh�ｿｽE�ｿｽX�ｿｽ^�ｿｽu
		super.onDestroy();

		/*********************************
		 * Reset: Preference
		 *********************************/
		SharedPreferences prefs = this
						.getSharedPreferences(
							CONS.Prefs.pname_IC,
							Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = prefs.edit();
		
		editor.putInt(
					CONS.Prefs.pkey_GenreId,
					CONS.Prefs.pkey_GenreId_IntValue);
		
		editor.commit();
		
		// Log
		Log.d("[" + "MainActv.java : "
			+ +Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]",
			"Prefs: Genre id => Reset to  " + CONS.Prefs.pkey_GenreId_IntValue);
		
		/*********************************
		 * Fields
		 *********************************/
		CONS.MainActv.CLList = null;
		
		// Log
		Log.d("[" + "MainActv.java : "
				+ +Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ " : "
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]", "CONS.MainActv.CLList => null");
		
		
	}//protected void onDestroy()

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		
		case R.id.main_opt_menu_register://---------------
			
			Methods.dlg_register(this);
			
			break;// case R.id.opt_menu_main_actv_register
			
		case R.id.main_opt_menu_db://---------------
			
			Methods_dlg.dlg_db_activity(this);
//			Methods.db_backup(this);
			
			break;// case R.id.main_opt_menu_backup_db

		case R.id.main_opt_menu_filter_by_genre://---------------
			
			Methods.dlg_filter_by_genre(this);
			
			break;// case R.id.main_opt_menu_filter_by_genre

		case R.id.main_opt_menu_sort_list://---------------
			
			Methods_dlg.dlg_SortList(this);
			
			break;// case R.id.main_opt_menu_filter_by_genre
			
		case R.id.main_opt_menu_see_log://---------------
			
			_case_main_opt_menu_see_log();
			
			break;// case R.id.main_opt_menu_filter_by_genre
			
		case R.id.main_opt_menu_settings://---------------
			
			_case_main_opt_menu_settings();
			
			break;// case R.id.main_opt_menu_filter_by_genre
			
		}//switch (item.getItemId())

		return super.onOptionsItemSelected(item);
	}//public boolean onOptionsItemSelected(MenuItem item)

	private void
	_case_main_opt_menu_settings() {
		// TODO Auto-generated method stub
		Intent i = new Intent();
		
		i.setClass(this, SettingsActv.class);
		
		i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		
		/*********************************
		 * 3. Start
		 *********************************/
		startActivity(i);

	}//_case_main_opt_menu_settings()

	private void
	_case_main_opt_menu_see_log() {
		// TODO Auto-generated method stub
		Intent i = new Intent();
		
		i.setClass(this, LogActv.class);
		
		/*********************************
		 * 3. Start
		 *********************************/
		startActivity(i);

	}//_case_main_opt_menu_see_log()

	@Override
	protected void onPause() {
		// TODO �ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ黷ｽ�ｿｽ�ｿｽ�ｿｽ\�ｿｽb�ｿｽh�ｿｽE�ｿｽX�ｿｽ^�ｿｽu
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO �ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ黷ｽ�ｿｽ�ｿｽ�ｿｽ\�ｿｽb�ｿｽh�ｿｽE�ｿｽX�ｿｽ^�ｿｽu
		super.onResume();
	}

	@Override
	protected void onStart() {
		/*********************************
		 * 1. Show list
		 * 
//		 * 1. Refresh list view;
		 *********************************/
//		/*********************************
//		 * 1. Show list
//		 *********************************/
		_onStart_ShowList();
		
		_onStart_SetSelection();
		
		super.onStart();
		
	}//protected void onStart()

	private void 
	_onStart_SetSelection() {
		// TODO Auto-generated method stub
		////////////////////////////////

		// set: selection

		////////////////////////////////
		int last_Position = Methods.get_Pref_Int(
						this, 
						CONS.Prefs.pname_IC, 
						CONS.Prefs.pkey_LastVisiblePosition_MainActv, 
						CONS.Prefs.dflt_IntExtra_value);
		
		// Log
		String msg_Log = "last_Position = " + last_Position;
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", msg_Log);
		
		
		if (last_Position != CONS.Prefs.dflt_IntExtra_value) {
			
			ListView lv = this.getListView();
			
			int num = last_Position - CONS.Admin.dflt_ChildCount;
			
			if (num < 0) num = 0;
			
			lv.setSelection(num);
			
		}

	}//_onStart_SetSelection

	@Override
	protected void onStop() {
		// TODO �ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ黷ｽ�ｿｽ�ｿｽ�ｿｽ\�ｿｽb�ｿｽh�ｿｽE�ｿｽX�ｿｽ^�ｿｽu
		super.onStop();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		Methods.confirm_quit(this, keyCode);
		
		return super.onKeyDown(keyCode, event);
		
	}//public boolean onKeyDown(int keyCode, KeyEvent event)
    
}//public class MainActv extends Activity
