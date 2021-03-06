package ic2.listeners.dialog;

import ic2.items.CL;
import ic2.main.R;
import ic2.utils.Methods;
import ic2.utils.Tags;
import android.app.Activity;
import android.app.Dialog;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class DB_CL implements OnClickListener {
	/*----------------------------
	 * Fields
		----------------------------*/
	//
	Activity actv;
	Dialog dlg;
	Dialog dlg2;		//=> Used in dlg_input_empty_btn_XXX

	int pos_InList;
	//
	Vibrator vib;
	
	// Used in => Methods.dlg_addMemo(Activity actv, long file_id, String tableName)
	long file_id;
	String tableName;
	
	public DB_CL(Activity actv, Dialog dlg) {
		//
		this.actv = actv;
		this.dlg = dlg;
		
		//
		vib = (Vibrator) actv.getSystemService(actv.VIBRATOR_SERVICE);
	}

	public DB_CL(Activity actv, Dialog dlg1,
			Dialog dlg2) {
		//
		this.actv = actv;
		this.dlg = dlg1;
		this.dlg2 = dlg2;
		
		//
		vib = (Vibrator) actv.getSystemService(actv.VIBRATOR_SERVICE);
	}

	public DB_CL(Activity actv, Dialog dlg, long file_id, String tableName) {
		// 
		this.actv = actv;
		this.dlg = dlg;
		
		this.tableName = tableName;
		
		this.file_id = file_id;
		
		vib = (Vibrator) actv.getSystemService(actv.VIBRATOR_SERVICE);
		
	}//public DialogButtonOnClickListener(Activity actv, Dialog dlg, long file_id, String tableName)

	public DB_CL(Activity actv, Dialog dlg,
			Dialog dlg2, int pos_InList) {
		
		this.actv = actv;
		this.dlg = dlg;
		this.dlg2 = dlg2;
		
		this.pos_InList = pos_InList;
		
		//
		vib = (Vibrator) actv.getSystemService(actv.VIBRATOR_SERVICE);

	}

	//	@Override
	public void onClick(View v) {
		//
		Tags.DialogButtonTags tag_name = (Tags.DialogButtonTags) v.getTag();
//		Methods.DialogButtonTags tag_name = (Methods.DialogButtonTags) v.getTag();

		//
		switch (tag_name) {
		
		case dlg_generic_dismiss://------------------------------------------------
			
			vib.vibrate(Methods.vibLength_click);
			
			dlg.dismiss();
			
			break;
			
		case DLG_GENERIC_DISMISS_SECOND_DIALOG://------------------------------------------------
			
			vib.vibrate(Methods.vibLength_click);
			
			dlg2.dismiss();
			
			break;// case dlg_generic_dismiss_second_dialog

		case dlg_register_genre_bt_ok://------------------------------------------------
			
			register_genre_bt_ok();
			
			break;// case dlg_register_genre_bt_ok
			
		case dlg_register_list_bt_ok://------------------------------------------------
			
			register_list_bt_ok();
			
			break;// case dlg_register_list_bt_ok

		case dlg_rgstr_item_bt_ok://------------------------------------------------
			
			register_item_bt_ok();
			
			break;// case dlg_rgstr_item_bt_ok

		case dlg_checkactv_change_serial_num_btn_ok://-----------------------------
			
			// Log
			Log.d("DialogButtonOnClickListener.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "item_position: " + pos_InList);

			
			dlg_checkactv_change_serial_num_btn_ok();
			
			break;// case dlg_checkactv_change_serial_num_btn_ok
			
		case dlg_checkactv_edit_item_text_btn_ok://---------------------------------
			
			Methods.update_item_text(actv, dlg, dlg2, pos_InList);
			
			break;
			
		case dlg_edit_list_title_btn_ok://---------------------------------
			
			Methods.edit_List_Title(actv, dlg, dlg2, pos_InList);
			
			break;
			
		case dlg_change_genre_btn_ok://---------------------------------
			
			Methods.change_Genre(actv, dlg, dlg2, pos_InList);
			
			break;
			
		case dlg_edit_cl_btn_ok://---------------------------------
			
			Methods.edit_CL(actv, dlg, dlg2, pos_InList);
			
			break;
			
		case DLG_CONF_DUPLIST_OK://---------------------------------
			
			Methods.dup_CL(actv, dlg, dlg2, pos_InList);
			
			break;
			
		case DLG_CONF_DELETE_CL_OK://---------------------------------
			
			int pos_InAdapter = pos_InList;
			
			case_Dlg_Conf_Delete_CL_Ok(pos_InAdapter);
			
//			Methods.dup_CL(actv, dlg, dlg2, pos_InAdapter);
			
			break;
			
		default: // ----------------------------------------------------
			break;
		}//switch (tag_name)
	}

	private void 
	case_Dlg_Conf_Delete_CL_Ok
	(int pos_InAdapter) {
		// TODO Auto-generated method stub
		
		Methods.delete_list(actv, dlg, dlg2, pos_InAdapter);
		
	}

	private void dlg_checkactv_change_serial_num_btn_ok() {
		/*********************************
		 * 1. Get view
		 * 2. Null?
		 * 3. Input empty?
		 * 
		 * 4. Numeric?
		 * 5. Change order
		 * 
		 *********************************/
		EditText et = (EditText) dlg2.findViewById(R.id.dlg_checkactv_change_serial_num_et_new);
		
		/*********************************
		 * 2. Null?
		 *********************************/
		if (et == null) {
			
			Toast.makeText(actv, 
			"EditText => Null!", 
			Toast.LENGTH_SHORT).show();
	
			return;
			
		}
		
		/*********************************
		 * 3. Input empty?
		 *********************************/
		String new_num = et.getText().toString();
		
		if (new_num.equals("")) {
			
			Toast.makeText(actv, 
			"No input!", 
			Toast.LENGTH_SHORT).show();
	
			return;
			
		}
		
		/*********************************
		 * 4. Numeric?
		 *********************************/
		if (!Methods.is_numeric(new_num)) {
			
			Toast.makeText(actv, 
			"Not a number",
			Toast.LENGTH_SHORT).show();
	
			return;
		}
		
//		} else {
//
//			Toast.makeText(actv, 
//			"It's a number", 
//			Toast.LENGTH_SHORT).show();
//
//		}
		
		/*********************************
		 * 5. Change order
		 *********************************/
		Methods.checkactv_change_order(actv, dlg, dlg2, pos_InList);
			
//		// debug
//		if (et != null) {
//			
//			Toast.makeText(actv, 
//					"New num: " + et.getText().toString(), 
//					Toast.LENGTH_SHORT).show();
//			
////			return;
//			
//		} else {//if (et != null)
//
//			Toast.makeText(actv, 
//					"EditText => Null!", 
//					Toast.LENGTH_SHORT).show();
//			
////			return;
//			
//		}//if (et != null)

		
	}//private void dlg_checkactv_change_serial_num_btn_ok()

	private void register_item_bt_ok() {
		/*********************************
		 * dlg	=> dlg_register.xml
		 * dlg2	=> dlg_register_genre.xml
		 * 
		 * 1. Vibrate
		 * 2. Input empty?
		 * 
		 * 3. Register
		 *********************************/
		vib.vibrate(Methods.vibLength_click);
		
		/*********************************
		 * 2. Input empty?
		 *********************************/
		EditText et = 
			(EditText) dlg.findViewById(R.id.dlg_rgstr_item_et_text);
		
		if (et.getText().toString().equals("")) {
			
			// debug
			Toast.makeText(actv, "No input!", 2000).show();
			
			return;
			
		}//if (!et.getText().toString().equals(""))
		
		/*********************************
		 * 3. Register
		 *********************************/
		Methods.register_item(actv, dlg);
	
	}//private void register_item_bt_ok()

	private void register_list_bt_ok() {
		/*********************************
		 * dlg	=> dlg_register.xml
		 * dlg2	=> dlg_register_genre.xml
		 * 
		 * 1. Vibrate
		 * 2. Input empty?
		 * 
		 * 3. Register
		 *********************************/
		vib.vibrate(Methods.vibLength_click);
		
		/*********************************
		 * 2. Input empty?
		 *********************************/
		EditText et = 
			(EditText) dlg2.findViewById(R.id.dlg_register_list_et);
		
		if (et.getText().toString().equals("")) {
			
			// debug
			Toast.makeText(actv, "No input!", 2000).show();
			
			return;
			
		}//if (!et.getText().toString().equals(""))
		
		/*********************************
		 * 3. Register
		 *********************************/
		Methods.register_list(actv, dlg, dlg2);
		
	}//private void register_list_bt_ok()

	private void register_genre_bt_ok() {
		/*********************************
		 * dlg	=> dlg_register.xml
		 * dlg2	=> dlg_register_genre.xml
		 * 
		 * 1. Vibrate
		 * 2. Input empty?
		 * 
		 * 3. Register
		 *********************************/
		vib.vibrate(Methods.vibLength_click);
		
		/*********************************
		 * 2. Input empty?
		 *********************************/
		EditText et = 
			(EditText) dlg2.findViewById(R.id.dlg_register_genre_et);
		
		if (et.getText().toString().equals("")) {
			
			// debug
			Toast.makeText(actv, "No input!", 2000).show();
			
			return;
			
		}//if (!et.getText().toString().equals(""))
		
//		// debug
//		Toast.makeText(actv, et.getText().toString(), 2000).show();
		
		/*********************************
		 * 3. Register
		 *********************************/
		Methods.register_genre(actv, dlg, dlg2);
		
		
	}//private void register_genre_bt_ok()

}//public class DialogButtonOnClickListener implements OnClickListener
