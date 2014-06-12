package ic2.listeners;


import ic2.items.CL;
import ic2.items.Item;
import ic2.utils.Methods;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class Custom_OI_LCL implements OnItemLongClickListener {
//	public class CustomOnItemLongClickListener implements OnItemLongClickListener {

	Activity actv;
	static Vibrator vib;

	//
	ArrayAdapter<String> dirListAdapter;	// Used in => case dir_list_move_files
	
	//
	Dialog dlg;	// Used in => case dir_list_move_files
	
	//
	List<String> fileNameList;	// Used in => case dir_list_move_files
	
	/****************************************
	 * Constructor
	 ****************************************/
	public Custom_OI_LCL(Activity actv) {
		// 
		this.actv = actv;
		vib = (Vibrator) actv.getSystemService(actv.VIBRATOR_SERVICE);
	}

	/*----------------------------
	 * Used in => case dir_list_move_files
		----------------------------*/
	public Custom_OI_LCL(Activity actv,
			Dialog dlg, ArrayAdapter<String> dirListAdapter, List<String> fileNameList) {
		// 
		this.actv = actv;
		vib = (Vibrator) actv.getSystemService(actv.VIBRATOR_SERVICE);
		this.dlg = dlg;
		this.fileNameList = fileNameList;
		this.dirListAdapter = dirListAdapter;
		
	}//public CustomOnItemLongClickListener

	/****************************************
	 * Methods
	 ****************************************/
//	@Override
	public boolean onItemLongClick(
										AdapterView<?> parent, View v,
										int position, long id) {
		/*----------------------------
		 * Steps
		 * 1. Get item
		 * 2. Get tag
		 * 3. Vibrate
		 * 
		 * 4. Is the tag null?
		 * 
		 * 5. If no, the switch
			----------------------------*/
		
		//
		Methods.ListTags tag = (Methods.ListTags) parent.getTag();
		
//		// Log
//		Log.d("CustomOnItemLongClickListener.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "Tag => " + tag.name());

		//
//		vib.vibrate(400);
		vib.vibrate(40);
		
		switch (tag) {
		
		case actv_check_lv://-----------------------------------------
			
			Item item = (Item) parent.getItemAtPosition(position);
			
			Methods.dlg_checkactv_long_click(actv, position, item);
//			Methods.dlg_checkactv_long_click(actv, position);
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "position: " + position);

//			// debug
//			Toast.makeText(actv, item.getText(), Toast.LENGTH_SHORT).show(); 
			
			break;// case actv_check_lv

		case actv_main_lv://-----------------------------------------
			
			boolean result = case_actv_main_lv(parent, position);
			
			if (result == false) {
				
				return false;
				
			}//if (result == false)
			
//			CL check_list = (CL) parent.getItemAtPosition(position);
//			
//			if (check_list == null) {
//				
//				// debug
//				Toast.makeText(actv, "Check list is null", Toast.LENGTH_SHORT).show();
//				
//				return false;
//				
//			}//if (check_list == null)
//			
//			long check_list_id = check_list.getDb_id();
//			
//			
//			Methods.dlg_main_actv_long_click(actv, position, check_list_id);
//			
//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "position: " + position);
//			
//			// Log
//			Log.d("CustomOnItemLongClickListener.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "check_list.getName()=" + check_list.getName());

			break;// case actv_main_lv

		}//switch (tag)
		
		
//		return false;
		return true;
		
	}//public boolean onItemLongClick()

	private boolean
	case_actv_main_lv(AdapterView<?> parent, int position) {
		// TODO Auto-generated method stub
		CL check_list = (CL) parent.getItemAtPosition(position);
		
//		// Log
//		String msg_Log = "check_list.getName() => " + check_list.getName();
//		Log.d("Custom_OI_LCL.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", msg_Log);
//		
//		// Log
//		msg_Log = "parent => " + parent.getClass().getName();
//		Log.d("Custom_OI_LCL.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", msg_Log);
		
		if (check_list == null) {
			
			// debug
			Toast.makeText(actv, "Check list is null", Toast.LENGTH_SHORT).show();
			
			return false;
			
		}//if (check_list == null)
		
		long check_list_id = check_list.getDb_id();
		
		Methods.dlg_main_actv_long_click(actv, position, check_list_id, check_list);
		
		return true;
		
	}//case_actv_main_lv(AdapterView<?> parent, int position)

}//public class CustomOnItemLongClickListener implements OnItemLongClickListener
