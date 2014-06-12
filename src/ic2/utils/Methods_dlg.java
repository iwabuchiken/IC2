package ic2.utils;

import ic2.items.CL;
import ic2.listeners.dialog.DB_CL;
import ic2.listeners.dialog.DB_TL;
import ic2.listeners.dialog.DOI_CL;
import ic2.main.CheckActv;
import ic2.main.R;
import ic2.utils.Tags.DialogTags;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class Methods_dlg {

	public static void dlg_db_activity(Activity actv) {
		/*----------------------------
		 * 1. Dialog
		 * 2. Prep => List
		 * 3. Adapter
		 * 4. Set adapter
		 * 
		 * 5. Set listener to list
		 * 6. Show dialog
			----------------------------*/
		Dialog dlg = Methods_dlg.dlg_template_cancel(
									actv,
									R.layout.dlg_db_admin, 
									R.string.dlg_db_admin_title, 
									R.id.dlg_db_admin_bt_cancel, 
									Methods.DialogButtonTags.dlg_generic_dismiss);
		
		/*----------------------------
		 * 2. Prep => List
			----------------------------*/
		String[] choices = {
				actv.getString(R.string.dlg_db_admin_item_backup_db),
				actv.getString(R.string.dlg_db_admin_item_refresh_db),
//				actv.getString(R.string.dlg_db_admin_item_refatcor_db),
				actv.getString(R.string.dlg_db_admin_item_restore_db),
				actv.getString(R.string.dlg_db_admin_item_get_yomi),
				actv.getString(R.string.dlg_db_admin_item_upload_db),
				actv.getString(R.string.dlg_db_admin_item_copy_external_db)
//				actv.getString(R.string.dlg_db_admin_item_post_data),
		};
		
		List<String> list = new ArrayList<String>();
		
		for (String item : choices) {
			
			list.add(item);
			
		}
		
		/*----------------------------
		 * 3. Adapter
			----------------------------*/
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				actv,
//				R.layout.dlg_db_admin,
				android.R.layout.simple_list_item_1,
				list
				);

		/*----------------------------
		 * 4. Set adapter
			----------------------------*/
		ListView lv = (ListView) dlg.findViewById(R.id.dlg_db_admin_lv);
		
		lv.setAdapter(adapter);
		
		/*----------------------------
		 * 5. Set listener to list
			----------------------------*/
//		lv.setTag(Tags.DialogItemTags.dlg_db_admin_lv);
//		lv.setTag(Tags.DialogTags.dlg_db_admin_lv);
		lv.setTag(Methods.DialogItemTags.dlg_db_admin_lv);
		
		lv.setOnItemClickListener(new DOI_CL(actv, dlg));
		
		/*----------------------------
		 * 6. Show dialog
			----------------------------*/
		dlg.show();
		
		
	}//public static void dlg_db_activity(Activity actv)

	public static Dialog
	dlg_template_cancel
	(Activity actv,
			
			int layoutId, int titleStringId,
			
			int cancelButtonId, Methods.DialogButtonTags cancelTag) {
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

	public static void dlg_SortList(Activity actv) {
		// TODO Auto-generated method stub
		Dialog dlg = Methods_dlg.dlg_template_cancel(
				actv,
				R.layout.dlg_db_admin, 
				R.string.dlg_sort_list_title, 
				R.id.dlg_db_admin_bt_cancel, 
				Methods.DialogButtonTags.dlg_generic_dismiss);

		/*----------------------------
		* 2. Prep => List
		----------------------------*/
		String[] choices = {
					actv.getString(R.string.dlg_sort_list_item_name),
					actv.getString(R.string.dlg_sort_list_created_at)
		};
		
		List<String> list = new ArrayList<String>();
		
		for (String item : choices) {
		
			list.add(item);
		
		}
		
		/*----------------------------
		* 3. Adapter
		----------------------------*/
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				actv,
				//R.layout.dlg_db_admin,
				android.R.layout.simple_list_item_1,
				list
		);
		
		/*----------------------------
		* 4. Set adapter
		----------------------------*/
		ListView lv = (ListView) dlg.findViewById(R.id.dlg_db_admin_lv);
		
		lv.setAdapter(adapter);
		
		/*----------------------------
		* 5. Set listener to list
		----------------------------*/
		lv.setTag(Methods.DialogItemTags.dlg_sort_list_lv);
//		lv.setTag(Tags.DialogTags.dlg_sort_list_lv);
		
		lv.setOnItemClickListener(new DOI_CL(actv, dlg));
		
		/*----------------------------
		* 6. Show dialog
		----------------------------*/
		dlg.show();

	}//public static void dlg_SortList(Activity actv)

	public static void
	dlg_Edit_List_Title
	(Activity actv, 
			long check_list_id, 
			Dialog dlg1, 
			CL check_list, 
			int cl_position) {
		// TODO Auto-generated method stub

		Dialog dlg2 = new Dialog(actv);
		
		//
		dlg2.setContentView(R.layout.dlg_checkactv_edit_item_text);
		
		// Title
		dlg2.setTitle(R.string.dlg_main_actv_long_click_lv_edit_title);

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
				Methods.DialogButtonTags.dlg_edit_list_title_btn_ok);
		
		btn_cancel.setTag(
				Methods.DialogButtonTags.dlg_generic_dismiss_second_dialog);
		
		/*********************************
		 * 3-2. Set current text
		 *********************************/
		EditText et = (EditText) dlg2.findViewById(R.id.dlg_checkactv_edit_item_text_et);
		
		String text = check_list.getName();
		
		et.setText(text);
		
//		et.setSelection(0);
		
		et.setSelection(text.length());
		
		/*********************************
		 * 4. Add listeners => OnTouch
		 *********************************/
		//
		btn_ok.setOnTouchListener(new DB_TL(actv, dlg2));
		btn_cancel.setOnTouchListener(new DB_TL(actv, dlg2));
		
		/*********************************
		 * 5. Add listeners => OnClick
		 *********************************/
		//
		btn_ok.setOnClickListener(new DB_CL(actv, dlg1, dlg2, cl_position));
		btn_cancel.setOnClickListener(
					new DB_CL(actv, dlg1, dlg2));
		
		/*********************************
		 * 6. Show dialog
		 *********************************/
		dlg2.show();
		
		
	}//dlg_Edit_List_Title

}//public class Methods_dlg
