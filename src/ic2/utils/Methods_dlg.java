package ic2.utils;

import ic2.items.CL;
import ic2.listeners.dialog.DB_CL;
import ic2.listeners.dialog.DB_TL;
import ic2.listeners.dialog.DOI_CL;
import ic2.main.CheckActv;
import ic2.main.R;
import ic2.utils.Tags.DialogTags;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

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
									Tags.DialogButtonTags.dlg_generic_dismiss);
		
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

	public static void dlg_SortList(Activity actv) {
		// TODO Auto-generated method stub
		Dialog dlg = Methods_dlg.dlg_template_cancel(
				actv,
				R.layout.dlg_db_admin, 
				R.string.dlg_sort_list_title, 
				R.id.dlg_db_admin_bt_cancel, 
				Tags.DialogButtonTags.dlg_generic_dismiss);

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
				Tags.DialogButtonTags.dlg_edit_list_title_btn_ok);
		
		btn_cancel.setTag(
				Tags.DialogButtonTags.DLG_GENERIC_DISMISS_SECOND_DIALOG);
		
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

	public static void
	dlg_Change_Genre
	(Activity actv, 
			long check_list_id,
			Dialog dlg1, 
			CL check_list, 
			int item_position) {
		// TODO Auto-generated method stub
		
		Dialog dlg2 = new Dialog(actv);
		
		//
		dlg2.setContentView(R.layout.dlg_change_genre);
		
		// Title
		dlg2.setTitle(
					actv.getString(R.string.dlg_main_actv_long_click_lv_change_genre)
					+ " : "
					+ check_list.getName());

		////////////////////////////////

		// spinner

		////////////////////////////////
		Spinner sp = (Spinner) dlg2.findViewById(R.id.dlg_change_genre_sp);
		
		List<String> genreList = Methods.get_genre_list(actv);

		Collections.sort(genreList);
		
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
		/*********************************
		 * 2. Get views
		 *********************************/
		//
		Button btn_ok = 
			(Button) dlg2.findViewById(R.id.dlg_change_genre_btn_ok);
		
		Button btn_cancel = 
				(Button) dlg2.findViewById(R.id.dlg_change_genre_btn_cancel);
		
		/*********************************
		 * 3. Set tags
		 *********************************/
		//
		btn_ok.setTag(
				Tags.DialogButtonTags.dlg_change_genre_btn_ok);
		
		btn_cancel.setTag(
				Tags.DialogButtonTags.DLG_GENERIC_DISMISS_SECOND_DIALOG);
		
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
		btn_ok.setOnClickListener(
					new DB_CL(actv, dlg1, dlg2, item_position));
		btn_cancel.setOnClickListener(
					new DB_CL(actv, dlg1, dlg2));
		
		/*********************************
		 * 6. Show dialog
		 *********************************/
		dlg2.show();		
		
	}//dlg_Change_Genre

	public static void
	dlg_Edit_CL
	(Activity actv,
			long check_list_id,
			Dialog dlg1, 
			CL check_list, 
			int item_position) {
		// TODO Auto-generated method stub
		
		Dialog dlg2 = new Dialog(actv);
		
		//
		dlg2.setContentView(R.layout.dlg_edit_cl);
		
		// Title
		dlg2.setTitle(R.string.dlg_edit_cl_title);

		/*********************************
		 * 2. Get views
		 *********************************/
		//
		Button btn_ok = 
			(Button) dlg2.findViewById(R.id.dlg_edit_cl_btn_ok);
		
		Button btn_cancel = 
				(Button) dlg2.findViewById(R.id.dlg_edit_cl_btn_cancel);
		
		/*********************************
		 * 3. Set tags
		 *********************************/
		//
		btn_ok.setTag(
				Tags.DialogButtonTags.dlg_edit_cl_btn_ok);
		
		btn_cancel.setTag(
				Tags.DialogButtonTags.DLG_GENERIC_DISMISS_SECOND_DIALOG);
//		Tags.DialogButtonTags.dlg_generic_dismiss_second_dialog);
		
		/*********************************
		 * 3-2. Set current text
		 *********************************/
		////////////////////////////////

		// set: title

		////////////////////////////////
		EditText et_Title = 
				(EditText) dlg2.findViewById(R.id.dlg_edit_cl_et_title);
		
		String text = check_list.getName();
		
		et_Title.setText(text);
		
//		et.setSelection(0);
		
		et_Title.setSelection(text.length());

		////////////////////////////////

		// set: genre

		////////////////////////////////
		EditText et_Yomi = 
				(EditText) dlg2.findViewById(R.id.dlg_edit_cl_et_yomi);
		
//		String yomi = check_list.getName();
		
		et_Yomi.setText(check_list.getYomi());

		////////////////////////////////

		// spinner

		////////////////////////////////
		Spinner sp = (Spinner) dlg2.findViewById(R.id.dlg_edit_cl_sp_genre);
		
		List<String> genreList = Methods.get_genre_list(actv);

		Collections.sort(genreList);
		
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
		
		////////////////////////////////

		// spinner: set selection

		////////////////////////////////
		int num = 0;
		
		for (int i = 0; i < adp.getCount(); i++) {
			
			String genreName = adp.getItem(i);
	
			if (genreName.equals(
					Methods.get_genre_name_from_genre_id(
							actv, check_list.getGenre_id()))) {
//				actv.getString(R.string.generic_label_all))) {
				
				num = i;
				
				break;
				
			}//if (si.getName() == condition)
			
		}//for (int i = 0; i < adapter.getCount(); i++)

		sp.setSelection(num);
		
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
		btn_ok.setOnClickListener(new DB_CL(actv, dlg1, dlg2, item_position));
		btn_cancel.setOnClickListener(
					new DB_CL(actv, dlg1, dlg2));
		
		/*********************************
		 * 6. Show dialog
		 *********************************/
		dlg2.show();		
		
		
	}//dlg_Edit_CL

	public static void
	dlg_Conf_Dup_CL
	(Activity actv, 
			long check_list_id,
			Dialog dlg1, CL check_list, int item_position) {
		// TODO Auto-generated method stub
		
		Dialog dlg2 = Methods_dlg.dlg_Tmpl_OkCancel_SecondDialog(
					actv, 
					R.layout.dlg_tmpl_confirm_simple, 
					actv.getString(R.string.generic_tv_confirm), 
					
					R.id.dlg_tmpl_confirm_simple_btn_ok, 
					R.id.dlg_tmpl_confirm_simple_btn_cancel, 
					
					Tags.DialogButtonTags.DLG_CONF_DUPLIST_OK, 
//					Tags.DialogTags.dlg_generic_dismiss_second_dialog, 
					Tags.DialogButtonTags.DLG_GENERIC_DISMISS_SECOND_DIALOG, 
					dlg1, item_position);
//		dlg1, item_position);
		
		////////////////////////////////

		// set: texts

		////////////////////////////////
		TextView tv_Message = 
				(TextView) dlg2.findViewById(
						R.id.dlg_tmpl_confirm_simple_tv_message);
		
		TextView tv_ItemName = 
				(TextView) dlg2.findViewById(
						R.id.dlg_tmpl_confirm_simple_tv_item_name);
		
		tv_Message.setText(actv.getString(R.string.dlg_dup_checklist_message));
		tv_ItemName.setText(check_list.getName());
		
		dlg2.show();
		
		
		
	}//dlg_Conf_Dup_CL

	public static
	Dialog dlg_Tmpl_OkCancel_SecondDialog
	(Activity actv, 
			int layoutId, String title,
			int okButtonId, int cancelButtonId,
			
			Tags.DialogButtonTags okTag, 
			Tags.DialogButtonTags cancelTag,
			
			Dialog dlg1) {
		/****************************
		 * Steps
		 * 1. Set up
		 * 2. Add listeners => OnTouch
		 * 3. Add listeners => OnClick
		 ****************************/
		
		// 
		Dialog dlg2 = new Dialog(actv);
		
		//
		dlg2.setContentView(layoutId);
		
		// Title
		dlg2.setTitle(title);
//		dlg2.setTitle(titleStringId);
		
		/****************************
		 * 2. Add listeners => OnTouch
		 ****************************/
		//
		Button btn_ok = (Button) dlg2.findViewById(okButtonId);
		Button btn_cancel = (Button) dlg2.findViewById(cancelButtonId);
		
		//
		btn_ok.setTag(okTag);
		btn_cancel.setTag(cancelTag);
		
		//
		btn_ok.setOnTouchListener(new DB_TL(actv, dlg2));
		btn_cancel.setOnTouchListener(new DB_TL(actv, dlg2));
		
		/****************************
		 * 3. Add listeners => OnClick
		 ****************************/
		//
		btn_ok.setOnClickListener(new DB_CL(actv, dlg1, dlg2));
		btn_cancel.setOnClickListener(new DB_CL(actv, dlg1, dlg2));
		
		//
		//dlg2.show();
		
		return dlg2;
		
	}//public static Dialog dlg_template_okCancel_SecondDialog()
	
	public static
	Dialog dlg_Tmpl_OkCancel_SecondDialog
	(Activity actv, 
			int layoutId, String title,
			int okButtonId, int cancelButtonId,
			
			Tags.DialogButtonTags okTag, 
			Tags.DialogButtonTags cancelTag,
			
			Dialog dlg1, int item_Position_inList) {
		/****************************
		 * Steps
		 * 1. Set up
		 * 2. Add listeners => OnTouch
		 * 3. Add listeners => OnClick
		 ****************************/
		
		// 
		Dialog dlg2 = new Dialog(actv);
		
		//
		dlg2.setContentView(layoutId);
		
		// Title
		dlg2.setTitle(title);
//		dlg2.setTitle(titleStringId);
		
		/****************************
		 * 2. Add listeners => OnTouch
		 ****************************/
		//
		Button btn_ok = (Button) dlg2.findViewById(okButtonId);
		Button btn_cancel = (Button) dlg2.findViewById(cancelButtonId);
		
		//
		btn_ok.setTag(okTag);
		btn_cancel.setTag(cancelTag);
		
		//
		btn_ok.setOnTouchListener(new DB_TL(actv, dlg2));
		btn_cancel.setOnTouchListener(new DB_TL(actv, dlg2));
		
		/****************************
		 * 3. Add listeners => OnClick
		 ****************************/
		//
		btn_ok.setOnClickListener(
				new DB_CL(actv, dlg1, dlg2, item_Position_inList));
//		btn_ok.setOnClickListener(new DB_CL(actv, dlg1, dlg2));
		btn_cancel.setOnClickListener(new DB_CL(actv, dlg1, dlg2));
		
		//
		//dlg2.show();
		
		return dlg2;
		
	}//public static Dialog dlg_template_okCancel_SecondDialog()

}//public class Methods_dlg
