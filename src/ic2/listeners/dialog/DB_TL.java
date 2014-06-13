package ic2.listeners.dialog;

import ic2.utils.Methods;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class DB_TL implements OnTouchListener {

	/*----------------------------
	 * Fields
		----------------------------*/
	//
	Activity actv;
	Dialog dlg;
	
	public DB_TL(Activity actv, Dialog dlg) {
		//
		this.actv = actv;
		this.dlg = dlg;
	}
	
	public DB_TL(Activity actv) {
		//
		this.actv = actv;
	}

//	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		Methods.DialogButtonTags tag_name = (Methods.DialogButtonTags) v.getTag();
		
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			switch (tag_name) {
			
			case dlg_generic_dismiss:
			case dlg_generic_dismiss_second_dialog:
				
			case dlg_register_genre_bt_ok:
			case dlg_register_list_bt_ok:
				
			case dlg_rgstr_item_bt_ok:
				
			case dlg_checkactv_change_serial_num_btn_ok:
			case dlg_checkactv_edit_item_text_btn_ok:
			case dlg_edit_list_title_btn_ok:
				
			case dlg_change_genre_btn_ok:
				
				//
				v.setBackgroundColor(Color.GRAY);
				
				break;
			}//switch (tag_name)
		
			break;//case MotionEvent.ACTION_DOWN:
			
		case MotionEvent.ACTION_UP:
			switch (tag_name) {

			case dlg_generic_dismiss:
			case dlg_generic_dismiss_second_dialog:
				
			case dlg_register_genre_bt_ok:
			case dlg_register_list_bt_ok:
				
			case dlg_rgstr_item_bt_ok:
				
			case dlg_checkactv_change_serial_num_btn_ok:
			case dlg_checkactv_edit_item_text_btn_ok:
				
			case dlg_edit_list_title_btn_ok:
				
			case dlg_change_genre_btn_ok:
				
				//
				v.setBackgroundColor(Color.WHITE);
				
				break;
			}//switch (tag_name)
		
			break;//case MotionEvent.ACTION_UP:
		
		}//switch (event.getActionMasked())
		return false;
	}

}
