package com.dairam.android.fragments.activity.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.fragments.MyAccountItemFragment;
import com.dairam.android.fragments.activity.parserfiles.JSONfunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MyorderAdapter extends BaseAdapter {
	// Declare Variables

	JSONObject jsonobject;
	JSONArray jsonarray;
	ListView listview;
	ListViewAdapter adapter;
	ProgressDialog mProgressDialog;
	ArrayList<HashMap<String, String>> arraylist;
	SharedPreferences sharedpreferences;

	public static final String MyPREFERENCES = "MyPrefs";
	public static final String customerid = "id";
	public static final String firstname = "firstname";
	public static final String lastname = "lastname";
	public static final String email_string = "email";
	public static final String cart_string = "cart";
	public static final String wishlist_String = "wishlist";

	String idd_;

	Context context;
	LayoutInflater inflater;
	ArrayList<HashMap<String, String>> data;
	HashMap<String, String> resultp = new HashMap<String, String>();
	ArrayList<String> list = new ArrayList<String>();
	Fragment fr;
	Activity sampleActivity;
	int languagevariable = 1;/* try to read this value from the file in future */

	/*
	 * list.add(textview.getText().toString());
	 */
	public MyorderAdapter(Context context,
			ArrayList<HashMap<String, String>> arraylist, Activity activity) {
		this.context = context;
		data = arraylist;
		System.out.println("data for URL check" + data);

		activity = sampleActivity;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		// Declare Variables
		TextView orderid;
		TextView date;
		TextView status;
		TextView total, statusvalue;

		Button reload;

		System.out.println("Entered here... in the first layout list view ");

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View itemView = inflater.inflate(R.layout.myorder_single_item, parent,
				false);

		// Get the position
		resultp = data.get(position);

		orderid = (TextView) itemView.findViewById(R.id.textView2orderid);
		date = (TextView) itemView.findViewById(R.id.textVieworderdate);
		status = (TextView) itemView.findViewById(R.id.textViewamount);
		total = (TextView) itemView.findViewById(R.id.textViewstatus);
		statusvalue = (TextView) itemView.findViewById(R.id.txtStatusValue);
		statusvalue.setSelected(true);

		switch (getLanguage()) {
		case "en":

			break;

		default:
			Typeface myTypeface = Typeface.createFromAsset(context.getAssets(),
					"fonts/HELVETICANEUELTARABIC-ROMAN.TTF");

			orderid.setTypeface(myTypeface);
			date.setTypeface(myTypeface);
			status.setTypeface(myTypeface);
			total.setTypeface(myTypeface);
			statusvalue.setTypeface(myTypeface);
			break;
		}

		orderid.setSelected(true);

		orderid.setText(resultp.get(AppConstants.MY_ORDER_LIST_order_id));
		SharedPreferences sp = context.getSharedPreferences("lan",
				Context.MODE_PRIVATE);
		if (sp.getString("lan", "en").equals("en")) {
			date.setText("date: "
					+ resultp.get(AppConstants.MY_ORDER_LIST_date_added));
			status.setText("Total: "
					+ resultp.get(AppConstants.MY_ORDER_LIST_total));
			total.setText("Status: ");
			statusvalue.setText(resultp.get(AppConstants.MY_ORDER_LIST_status));
			Log.e("Result Print En",
					resultp.get(AppConstants.MY_ORDER_LIST_status));
			if (resultp.get(AppConstants.MY_ORDER_LIST_status).equals(
					"Complete")) {
				statusvalue.setTextColor(Color.GREEN);
			} else if (resultp.get(AppConstants.MY_ORDER_LIST_status).equals(
					"Pending")) {
				statusvalue.setTextColor(context.getResources().getColor(
						R.color.pending_orange));
			} else if (resultp.get(AppConstants.MY_ORDER_LIST_status).equals(
					"Failed")) {
				statusvalue.setTextColor(Color.RED);
			}
		} else {
			date.setText("التاريخ: "
					+ resultp.get(AppConstants.MY_ORDER_LIST_date_added));
			status.setText("المجموع : "
					+ resultp.get(AppConstants.MY_ORDER_LIST_total));
			total.setText("حالة الطلب: ");
			Log.e("Result Print",
					resultp.get(AppConstants.MY_ORDER_LIST_status));
			statusvalue.setText(resultp.get(AppConstants.MY_ORDER_LIST_status));
			if (resultp.get(AppConstants.MY_ORDER_LIST_status)
					.equals("انتهاء ")) {
				statusvalue.setTextColor(Color.GREEN);
			} else if (resultp.get(AppConstants.MY_ORDER_LIST_status).equals(
					"معلق ")) {
				statusvalue.setTextColor(context.getResources().getColor(
						R.color.pending_orange));
			} else if (resultp.get(AppConstants.MY_ORDER_LIST_status).equals(
					"العملية لم تتم ")) {
				statusvalue.setTextColor(Color.RED);
			}

		}

		reload = (Button) itemView.findViewById(R.id.buttonreload);

		reload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				resultp = data.get(position);
				idd_ = resultp.get(AppConstants.MY_ORDER_LIST_order_id);
				new DownloadJSON().execute();

			}
		});

		/*
		 * // Locate the ImageView in listview_item.xml flag = (ImageView)
		 * itemView.findViewById(R.id.imageViewsalegrid);
		 * 
		 * System.out.println("the imagee thing"+AppConstants.SALE_IMAGE);
		 */

		return itemView;
	}

	private class DownloadJSON extends AsyncTask<Void, Void, Void> {

		String first_Name;
		String last_Name;
		String cust_id;
		String email_id;
		String cart_count_;
		String wishlist_count_;
		String email;
		String customerID_;
		String successMsg;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Create a progressdialog
			mProgressDialog = new ProgressDialog(context, R.style.MyTheme);
			mProgressDialog.setCancelable(false);
			mProgressDialog
					.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
			mProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// Create an array
			arraylist = new ArrayList<HashMap<String, String>>();
			sharedpreferences = context.getSharedPreferences(MyPREFERENCES,
					Context.MODE_PRIVATE);
			if (sharedpreferences.contains(customerid)) {
				cust_id = sharedpreferences.getString(customerid, "");
			}
			String loginString = "http://dairam.com/index.php?route=api/reorder/get&id="
					+ cust_id
					+ "&orderid="
					+ idd_
					+ "&language="
					+ getLanguage();
			// Retrieve JSON Objects from the given URL address
			jsonobject = JSONfunctions.getJSONfromURL(loginString);

			return null;
		}

		@Override
		protected void onPostExecute(Void args) {
			mProgressDialog.dismiss();
			try {

				if (jsonobject != null) {
					// Locate the array name in JSON
					if (jsonobject.getBoolean("success")) {
						successMsg = jsonobject.getString("success_msg");
						jsonobject = jsonobject.getJSONObject("customer");

						cust_id = jsonobject.getString("id");
						first_Name = jsonobject.getString("firstname");
						last_Name = jsonobject.getString("lastname");
						email = jsonobject.getString("email");
						cart_count_ = jsonobject.getString("cart");
						wishlist_count_ = jsonobject.getString("wishlist");

						System.out.println("The email added is" + email);

						customerID_ = cust_id;
					}

					if (cust_id == null) {
						Toast.makeText(context, "Login failed",
								Toast.LENGTH_SHORT).show();
					}

					else {

						Editor editor = sharedpreferences.edit();
						editor.putString(customerid, cust_id);
						editor.putString(firstname, first_Name);
						editor.putString(lastname, last_Name);
						editor.putString(email_string, email_id);
						editor.putString(cart_string, cart_count_);
						editor.putString(wishlist_String, wishlist_count_);

						editor.commit();
						Toast.makeText(context, successMsg, Toast.LENGTH_SHORT)
								.show();
						/*
						 * if(getLanguage().equals("1")) Toast.makeText(context,
						 * "You have successfully added the products from order ID #"
						 * +idd_+" to your cart", Toast.LENGTH_SHORT).show();
						 * else{ Toast toast = Toast.makeText(context,
						 * " إلى سلة الشراء #"
						 * +idd_+"لقد قمت بنجاح بإضافة المنتج من الطلب رقم !",
						 * Toast.LENGTH_SHORT); TextView v = (TextView)
						 * toast.getView().findViewById(android.R.id.message);
						 * v.setTextDirection(TextView.TEXT_DIRECTION_ANY_RTL);
						 * v.setTextColor(Color.WHITE); toast.show(); }
						 */
						/*
						 * Toast.makeText(context, " إلى سلة الشراء #"+idd_+
						 * "لقد قمت بنجاح بإضافة المنتج من الطلب رقم !",
						 * Toast.LENGTH_SHORT).show();
						 */

						fr = new MyAccountItemFragment();
						FragmentManager fm = ((Activity) context)
								.getFragmentManager();
						FragmentTransaction fragmentTransaction = fm
								.beginTransaction();
						fragmentTransaction.replace(R.id.fragment_place, fr);
						fragmentTransaction.addToBackStack(null);
						fragmentTransaction.commit();

					}
				} else
					Toast.makeText(context, R.string.no_response,
							Toast.LENGTH_SHORT).show();
			} catch (JSONException e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();

			}

		}
	}

	public void setupUI(View view, boolean isHeading) {

		if ((view instanceof TextView)) {

			((TextView) view).setTypeface(getRegularTypeFace());
		}

	}

	Typeface getRegularTypeFace() {
		return Typeface.createFromAsset(context.getAssets(),
				context.getString(R.string.regular_typeface));
	}

	public String getLanguage() {
		SharedPreferences sp = context.getSharedPreferences("lan",
				Context.MODE_PRIVATE);
		if (sp.getString("lan", "en").equals("en"))
			return "1";
		else
			return "2";
	}

}
