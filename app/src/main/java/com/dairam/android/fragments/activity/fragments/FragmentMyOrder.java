package com.dairam.android.fragments.activity.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.adapter.MyorderAdapter;
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.interfaces.listenerHideView;
import com.dairam.android.fragments.activity.utillities.Util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentMyOrder extends Fragment {

	String mainString;
	Button backButton;
	TextView backTextView;

	ListView listview;
	MyorderAdapter adapter;
	ProgressDialog mProgressDialog;
	ArrayList<HashMap<String, String>> arraylist;

	Context context;
	SharedPreferences sharedPreferences;
	String id;

	public static final String MyPREFERENCES = "MyPrefs";
	public static final String customerid = "id";
	public static final String firstname = "firstname";
	public static final String lastname = "lastname";
	public static final String email_string = "email";

	JSONObject jsonobject;
	JSONArray jsonarray;

	Activity sampleActivity;

	Fragment fr;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES,
				Context.MODE_PRIVATE);

		final listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) getActivity();

		if (sharedPreferences.contains(customerid)) {
			id = sharedPreferences.getString(customerid, "");
		}
		View v = inflater.inflate(R.layout.layout_myorder, container, false);

		listview = (ListView) v.findViewById(R.id.listView1myorder);

		backButton = (Button) v.findViewById(R.id.buttonbackeditinfo);
		backTextView = (TextView) v.findViewById(R.id.textViewbackeditinfo);

		try {
			Typeface myTypeface;
			if (Util.getLanguage(getActivity()).equals("2")) {
				myTypeface = Typeface.createFromAsset(
						getActivity().getAssets(),
						"fonts/HELVETICANEUELTARABIC-ROMAN.TTF");
			} else {
				myTypeface = Typeface.createFromAsset(
						getActivity().getAssets(), "fonts/Georgia.ttf");
			}
			backTextView.setTypeface(myTypeface);
		} catch (Exception e) {
			System.out.println("jhfkdhfs");
		}

		context = getActivity();

		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				goBackPage();
			}
		});

		backTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				goBackPage();
			}
		});

		new DownloadJSON().execute();
		switch (getLanguage()) {
		case "en":

			break;

		default:
			setupUI(v, false);
			break;
		}

		return v;
	}

	public void setupUI(View view, boolean isHeading) {
		Typeface myTypeface = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/HELVETICANEUELTARABIC-ROMAN.TTF");
		TextView myTextView = (TextView) view.findViewById(R.id.textView1);
		myTextView.setTypeface(myTypeface);
		/* backTextView.setTypeface(myTypeface); */

	}

	private class DownloadJSON extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			mProgressDialog = new ProgressDialog(getActivity(), R.style.MyTheme);
			mProgressDialog.setCancelable(false);
			mProgressDialog
					.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
			mProgressDialog.show();

		}

		@Override
		protected Void doInBackground(Void... params) {

			mainString = AppConstants.MY_ORDER_LIST_JSON_URL + id
					+ "&language=" + getLanguage();

			HttpParams params1 = new BasicHttpParams();
			HttpProtocolParams.setVersion(params1, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params1, "UTF-8");
			params1.setBooleanParameter("http.protocol.expect-continue", false);
			HttpClient httpclient = new DefaultHttpClient(params1);

			HttpPost httppost = new HttpPost(mainString);
			Log.e("Main url", mainString);
			try {
				HttpResponse http_response = httpclient.execute(httppost);

				HttpEntity entity = http_response.getEntity();
				String jsonText = EntityUtils.toString(entity, HTTP.UTF_8);
				Log.i("Response", jsonText);
				jsonobject = new JSONObject(jsonText);

			} catch (Exception e) {

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void args) {
			try {
				// Create an array
				arraylist = new ArrayList<HashMap<String, String>>();
				// Retrieve JSON Objects from the given URL address
				/* jsonobject = JSONfunctions.getJSONfromURL(mainurl); */

				Log.e("JsonOBject", jsonobject + "");

				try {

					if (jsonobject != null) {
						if (jsonobject.getBoolean("success")) {

							// Locate the array name in JSON
							jsonarray = jsonobject
									.getJSONArray(AppConstants.MY_ORDER_LIST_JSON_OBJ);

							for (int i = 0; i < jsonarray.length(); i++) {
								HashMap<String, String> map = new HashMap<String, String>();
								jsonobject = jsonarray.getJSONObject(i);
								// Retrive JSON Objects
								map.put(AppConstants.MY_ORDER_LIST_order_id,
										jsonobject
												.getString(AppConstants.MY_ORDER_LIST_order_id));
								map.put(AppConstants.MY_ORDER_LIST_date_added,
										jsonobject
												.getString(AppConstants.MY_ORDER_LIST_date_added));
								map.put(AppConstants.MY_ORDER_LIST_status,
										jsonobject
												.getString(AppConstants.MY_ORDER_LIST_status));
								map.put(AppConstants.MY_ORDER_LIST_total,
										jsonobject
												.getString(AppConstants.MY_ORDER_LIST_total));

								// Set the JSON Objects into the array
								arraylist.add(map);
								// Pass the results into ListViewAdapter.java

							}
							adapter = new MyorderAdapter(context, arraylist,
									sampleActivity);
							// Set the adapter to the ListView
							listview.setAdapter(adapter);
						} else {
							if (jsonobject.has("error")) {
								Toast.makeText(getActivity(),
										jsonobject.getString("error"),
										Toast.LENGTH_SHORT).show();
							}
						}
					} else {
						Toast.makeText(getActivity(), R.string.no_response,
								Toast.LENGTH_SHORT).show();
					}

				} catch (JSONException e) {
					Log.e("Error", e.getMessage());
					e.printStackTrace();
				}
			} catch (Exception e) {

				System.out.println("the application time out");

				try {
					fr = new NoInternetFragment();
					FragmentManager fm = getFragmentManager();
					FragmentTransaction fragmentTransaction = fm
							.beginTransaction();
					fragmentTransaction.replace(R.id.fragment_place, fr);
					fragmentTransaction.addToBackStack(null);
					fragmentTransaction.commit();
				} catch (Exception e2) {

				}
			}

			// Close the progress dialog
			mProgressDialog.dismiss();
		}
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) activity;
		listenerHideView.reversePromoItemHideTopbar();
		listenerHideView.hide();
		listenerHideView.dairamBar();

		super.onAttach(activity);
	}

	public String getLanguage() {
		SharedPreferences sp = getActivity().getSharedPreferences("lan",
				Context.MODE_PRIVATE);
		if (sp.getString("lan", "en").equals("en"))
			return "1";
		else
			return "2";
	}

	private void goBackPage() {
		fr = new MyAccountItemFragment();
		FragmentManager fm = getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_place, fr);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();

		// listenerHideView.closeDrawer();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		getView().setFocusableInTouchMode(true);
		getView().requestFocus();

		getView().setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						goBackPage();
						return true;
					}
				}
				return false;
			}
		});
		super.onActivityCreated(savedInstanceState);
	}

}
