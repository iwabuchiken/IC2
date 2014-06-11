package ic2.tasks;

import ic2.items.Furi;
import ic2.items.Word;
import ic2.main.CheckActv;
import ic2.utils.CONS;
import ic2.utils.DBUtils;
import ic2.utils.Methods;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import sl.libs.json.YahooFurigana;
import sl.libs.xml.XmlHandler;
import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class Task_SaveItems extends AsyncTask<String, Integer, Integer> {

	static Activity actv;
	
	Dialog dlg;
	
	public Task_SaveItems(Activity actv) {
		// TODO Auto-generated constructor stub
		this.actv = actv;
	}

	@Override
	protected Integer doInBackground(String... arg0) {
		
		boolean res = 
				DBUtils.updateData_Items_Status_All(actv, CheckActv.iList);
		
		////////////////////////////////

		// Return

		////////////////////////////////
		return res == true ? 1 : 0;
		
	}//protected Integer doInBackground(String... arg0) {

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(Integer res) {
		// TODO Auto-generated method stub
		super.onPostExecute(res);
		
		if (res.intValue() == 1) {
			
			// Log
			String msg_Log = "Statuses => saved";
			Log.d("Task_SaveItems.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", msg_Log);
			
			// debug
			Toast.makeText(actv, msg_Log, Toast.LENGTH_SHORT).show();
			
		} else {

			// Log
			String msg_Log = "Statuses => Not saved!";
			Log.d("Task_SaveItems.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", msg_Log);
			
			// debug
			Toast.makeText(actv, msg_Log, Toast.LENGTH_SHORT).show();
			
		}
		
		
	}//protected void onPostExecute(Integer res)

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

}//public class Task_GetYomi extends AsyncTask<String, Integer, Integer>
