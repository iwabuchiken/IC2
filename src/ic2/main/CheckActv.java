package ic2.main;

import java.util.ArrayList;
import java.util.List;

import ic2.adapters.ItemListAdapter;
import ic2.adapters.MainListAdapter;
import ic2.items.CL;
import ic2.items.Item;
import ic2.listeners.Custom_OI_LCL;
import ic2.listeners.button.BO_CL;
import ic2.listeners.button.BO_TL;
import ic2.main.R;
import ic2.tasks.Task_GetYomi;
import ic2.tasks.Task_SaveItems;
import ic2.utils.CONS;
import ic2.utils.DBUtils;
import ic2.utils.Methods;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CheckActv extends ListActivity {

	/*********************************
	 * List-related
	 *********************************/
	public static ItemListAdapter ilAdp;
	
	public static List<Item> iList;

	public static CL clList;
	
	public static ListView checkactv_lv;

	static long list_id;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		/********************************
		 * 1. set_up_1
		 * 2. Set listeners
		 * 
		 * 3. Initialise vars
		 ********************************/
		/*********************************
		 * 1. Set up
		 *********************************/
		super.onCreate(savedInstanceState);

		set_up_1();

		/*********************************
		 * 3. Initialise vars
		 *********************************/
		iList = new ArrayList<Item>();

		/*********************************
		 * 2. Set listeners
		 *********************************/
		set_listeners();
		
		set_up_2_show_list();
		
		/*********************************
		 * 3. Initialise vars
		 *********************************/
		checkactv_lv = this.getListView();
		
//		iList = new ArrayList<Item>();
		
	}//public void onCreate(Bundle savedInstanceState)

	/**********************************************
	 * <Ret>
	 * 	false	=> iList == null
	 **********************************************/
	private boolean set_up_2_show_list() {
		/*********************************
		 * 1. Get item list
		 * 	1. Query
		 * 	2. Build list
		 * 	3. Sort list
		 * 
		 * 2. Setup adapter
		 * 
		 * 3. Set adapter
		 * 
		 * 4. Return
		 *********************************/
//		/*********************************
//		 * 1.1. Query
//		 *********************************/
//		DBUtils dbu = new DBUtils(this, MainActv.dbName);
//		
//		SQLiteDatabase rdb = dbu.getReadableDatabase();

//		Cursor c = Methods.select_all_from_table(this, rdb, MainActv.tableName_items);
//		
//		if (c.getCount() < 1) {
//			
//			// debug
//			Toast.makeText(this, "No data yet", Toast.LENGTH_SHORT).show();
//			
//			/********************************
//			 * 3. Close db
//			 ********************************/
//			rdb.close();
//
//			return;
//		}//if (c.getCount() < 1)
		
		/*********************************
		 * 1.2. Build list
		 *********************************/
		iList = Methods.get_item_list_from_check_list(this, clList.getDb_id());

		// Log
		if (iList == null) {

			Log.d("CheckActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "iList => Null");
			
			// debug
			Toast.makeText(this,
					"Query exception, or, no items " +
					"for this check list, yet",
					Toast.LENGTH_SHORT).show();
			
//			/********************************
//			 * 3. Close db
//			 ********************************/
//			rdb.close();

			return false;

		} else {//if (iList == null)
			
			// Log
			Log.d("CheckActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "iList.size(): " + iList.size());
			
		}//if (iList == null)
		
//			Log.d("CheckActv.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "iList.size(): " + iList.size());
		
		/*********************************
		 * 1.3. Sort list
		 *********************************/
		boolean res = Methods.sort_item_list_by_serial_num(this);
		
		// Log
		Log.d("CheckActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "res: " + res);
		
		
		/*********************************
		 * 2. Setup adapter
		 *********************************/
		ilAdp = new ItemListAdapter(
				this,
				R.layout.list_row_item_list,
				iList
				);

		/*********************************
		 * 3. Set adapter
		 *********************************/
		setListAdapter(ilAdp);
		
//		////////////////////////////////
//
//		// set: title
//
//		////////////////////////////////
//		String title = this.getClass().getName()
//						+ "("
//						+ iList.size()
//						+ ")"
//						;
//		this.setTitle(title);
		
		////////////////////////////////

		// title

		////////////////////////////////
		TextView tv_title = (TextView) findViewById(R.id.actv_check_tv);

		String title = clList.getName()
						+ " ("
						+ iList.size()
						+ ")"
						;
		
		if (title.equals("")) {
			
			tv_title.setText("No list name data");
			
		} else {
			
			tv_title.setText(title);
			
		}

		
		/*********************************
		 * 4. Return
		 *********************************/
		return true;

	}//private void set_up_2_show_list()


	private void set_listeners() {
		/*********************************
		 * 1. Button => "Add"
		 * 2. Long click
		 * 
		 * 3. Button => "T"
		 * 4. Button => "B"
		 *********************************/
		/*********************************
		 * 1. Button => "Add"
		 *********************************/
		Button bt_add = (Button) findViewById(R.id.actv_check_bt_add);
		
		bt_add.setTag(Methods.ButtonTags.actv_check_bt_add);
		
		bt_add.setOnTouchListener(new BO_TL(this));
		bt_add.setOnClickListener(new BO_CL(this));

		/*********************************
		 * 2. Long click
		 *********************************/
		ListView lv = this.getListView();
		
		lv.setTag(Methods.ListTags.actv_check_lv);
		
		lv.setOnItemLongClickListener(new Custom_OI_LCL(this));
		
		/*********************************
		 * 3. Button => "T"
		 *********************************/
		Button bt_top = (Button) findViewById(R.id.actv_check_bt_top);
		
		bt_top.setTag(Methods.ButtonTags.actv_check_bt_top);
		
		bt_top.setOnTouchListener(new BO_TL(this));
		bt_top.setOnClickListener(new BO_CL(this));
		
		/*********************************
		 * 4. Button => "B"
		 *********************************/
		Button bt_bottom = (Button) findViewById(R.id.actv_check_bt_bottom);
		
		bt_bottom.setTag(Methods.ButtonTags.actv_check_bt_bottom);
		
		bt_bottom.setOnTouchListener(new BO_TL(this));
		bt_bottom.setOnClickListener(new BO_CL(this));
		
	}//private void set_listeners()

	private void set_up_1() {
		/********************************
		 * 1. Set up
		 * 	1. Basics
		 * 	2. Initialise vars
		 * 2. Get intent values
		 * 
		 * 3. Get list object
		 * 
		 * 4. Set title
		 * 
		 * 5. Get item list
		 * 
		 * 6. Close db
		 * 
		 ********************************/
		
		setContentView(R.layout.actv_check);

		this.setTitle(this.getClass().getName());
		
		/*********************************
		 * 1.2. Initialise vars
		 *********************************/
//		iList = new ArrayList<Item>();
		
		
		/*********************************
		 * 2. Get intent values
		 *********************************/
		Intent i = this.getIntent();
		
		list_id = i.getLongExtra(MainActv.intent_list_id, -1);
		
		if (list_id == -1) {
			
			// debug
			Toast.makeText(this, "No intent value", Toast.LENGTH_SHORT).show();
			
			return;
			
		}//if (list_id == -1)
		
		// debug
//		Toast.makeText(this, "list_id=" + list_id, Toast.LENGTH_SHORT).show();
		
		/*********************************
		 * 3. Get list object
		 *********************************/
		clList = Methods.get_clList_from_db_id(this, list_id);
		
		if (clList == null) {
			
			// Log
			String msg_Log = "clList => null";
			Log.d("CheckActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", msg_Log);
			
			// debug
			String msg_Toast = "Check list => can't build: id = " + list_id;
			Toast.makeText(this, msg_Toast, Toast.LENGTH_SHORT).show();
			
			return;
			
		}
		
		// Log
		Log.d("CheckActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "clList.getName(): " + clList.getName());
		
		/*********************************
		 * 4. Set title
		 *********************************/
		TextView tv_title = (TextView) findViewById(R.id.actv_check_tv);

		String title = clList.getName();
		
		if (title.equals("")) {
			
			tv_title.setText("No list name data");
			
		} else {
			
			tv_title.setText(title);
			
		}
		
		
	}//private void set_up_1()
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.menu_actv_check, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onDestroy() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		boolean result;
		
		switch (item.getItemId()) {
		
		case R.id.opt_menu_actv_check_clear_status://---------------
			
			opt_menu_actv_check_clear_status();
			
			break;// case R.id.opt_menu_actv_check_clear_status

		case R.id.opt_menu_actv_check_sort_list_by_status://------------
			
			boolean res = Methods.sort_item_list_by_status(this);
			
			ilAdp.notifyDataSetChanged();
			
			break;// case R.id.opt_menu_actv_check_sort_list_by_status
			
		case R.id.opt_menu_actv_check_save_status_data://------------
			
//			temp_add_column_status_to_table_items();
			
			opt_menu_actv_check_save_status_data();
			
//			result = Methods.update_item_all_status(this, MainActv.dbName, MainActv.tableName_items);
//			
//			if (result == true) {
//				
//				// debug
//				Toast.makeText(this, "Status saved", Toast.LENGTH_SHORT).show();
//				
//			} else {//if (result == true)
//				
//				// debug
//				Toast.makeText(this, "Save status => Error occurred (See log)", Toast.LENGTH_SHORT).show();
//				
//			}//if (result == true)
			
			break;// case R.id.opt_menu_actv_check_save_status_data

		case R.id.opt_menu_actv_check_reset_serial_num:
			
			opt_menu_actv_check_reset_serial_num();
			
			break;

		}//switch (item.getItemId())

		return super.onOptionsItemSelected(item);
		
	}//public boolean onOptionsItemSelected(MenuItem item)

	private void opt_menu_actv_check_reset_serial_num() {
		/*********************************
		 * memo
		 *********************************/
		for (int i = 0; i < CheckActv.iList.size(); i++) {
			
			CheckActv.iList.get(i).setSerial_num(i + 1);
			
		}//for (int i = 0; i < CheckActv.iList.size(); i++)
		
		CheckActv.ilAdp.notifyDataSetChanged();
		
		// debug
		Toast.makeText(this, "Reset => Done", Toast.LENGTH_SHORT).show();
		
	}//private void opt_menu_actv_check_reset_serial_num()

	private void opt_menu_actv_check_save_status_data() {
		
		Task_SaveItems task = new Task_SaveItems(this);
		
		task.execute("Start");
		
//		boolean result = Methods.update_item_all_status(
//									this, 
//									CONS.DB.dbName,
//									CONS.DB.tname_items);
//		
//		if (result == true) {
//			
//			// debug
//			Toast.makeText(this, "Status saved", Toast.LENGTH_SHORT).show();
//			
//		} else {//if (result == true)
//			
//			// debug
//			Toast.makeText(this, "Save status => Error occurred (See log)", Toast.LENGTH_SHORT).show();
//			
//		}//if (result == true)
		
	}//private void opt_menu_actv_check_save_status_data()

	private void temp_add_column_status_to_table_items() {
		// REF=> http://stackoverflow.com/questions/8291673/how-to-add-new-column-to-android-sqlite-database
		/*********************************
		 * memo
		 *********************************/
		Methods.add_column_to_table(this, 
						CONS.DB.dbName, CONS.DB.tname_items, "status", "INTEGER");
		
//		DBUtils dbu = new DBUtils(actv, dbName);
//		
//		SQLiteDatabase wdb = dbu.getWritableDatabase();

		
		
	}//private void temp_add_column_status_to_table_items()

	private void opt_menu_actv_check_clear_status() {
		// Log
		Log.d("CheckActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "iList.size(): " + iList.size());
		
		for (Item i : CheckActv.iList) {
			
			i.setStatus(0);
			
		}
		
		//debug
		String temp = "";
		
		for (Item i : CheckActv.iList) {
			
			temp += "[" + i.getDb_id() + " => " + "status=" + i.getStatus() + "]";
			
		}
		
		ilAdp.notifyDataSetChanged();
		
	}//private void opt_menu_actv_check_clear_status()

	@Override
	protected void onPause() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onStop();
	}

	@Override
	protected void onListItemClick(
					ListView l, View v, int position, long id) {
		/*********************************
		 * 1. Get item
		 * 
		 * 2. Change status
		 * 
		 *********************************/
		Item item = (Item) l.getItemAtPosition(position);

		Methods.change_item_status(this, item);
		
		super.onListItemClick(l, v, position, id);
		
	}//protected void onListItemClick(ListView l, View v, int position, long id)

}//public class CheckActv extends ListActivity
