package com.dairam.android.fragments.activity.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.Toast;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.adapter.CategoryGridviewAdapter;
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.connectioncheck.ConnectionDetector;
import com.dairam.android.fragments.activity.interfaces.listenerHideView;
import com.dairam.android.fragments.activity.parserfiles.ImageLoader;
import com.dairam.android.fragments.activity.utillities.PrefUtil;

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

public class FragmentCategories extends Fragment {
	JSONObject jsonobject;
	JSONArray jsonarray;

	CategoryGridviewAdapter adapter;
	ProgressDialog mProgressDialog;
	ArrayList<HashMap<String, String>> arraylist;
	Context context;

	GridView gridView;
	ConnectionDetector cd;
	Boolean isInternetPresent;
	Fragment fr;
	int grid_Position;
	// boolean backToBestOffFlag = false;
	String mainurl;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_two, container, false);

		SharedPreferences sp = getActivity().getSharedPreferences("lan",
				Context.MODE_PRIVATE);
		String lan = sp.getString("lan", "en");
		grid_Position = PrefUtil.getCatGridCount(getActivity());
	
		switch (lan) {
		case "en":
			mainurl = AppConstants.CATEGORY_URL + "1&currency=KWD";
			break;

		default:
			mainurl = AppConstants.CATEGORY_URL + "2&currency=KWD";
			break;
		}
	
		SharedPreferences sp1 = getActivity().getSharedPreferences("tag",
				Context.MODE_PRIVATE);
		String p = "fragmentcategories";
		Editor editor = sp1.edit();
		editor.putString("act", p);
		editor.commit();

		ImageLoader imageLoader = new ImageLoader(getActivity());
		imageLoader.clearCache();

		gridView = (GridView) v.findViewById(R.id.gridViewcategories);

		context = getActivity();

		cd = new ConnectionDetector(context);
		isInternetPresent = cd.isConnectingToInternet();

		if (isInternetPresent) {
			new DownloadJSON().execute();
		}
		else {
			fr = new NoInternetFragment();
			FragmentManager fm = getFragmentManager();
			FragmentTransaction fragmentTransaction = fm.beginTransaction();
			fragmentTransaction.replace(R.id.fragment_place, fr);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
		}

		return v;
	}

	// DownloadJSON AsyncTask
	private class DownloadJSON extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Create a progressdialog
			mProgressDialog = new ProgressDialog(getActivity(), R.style.MyTheme);
			mProgressDialog.setCancelable(false);
			mProgressDialog
					.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
			mProgressDialog.show();
		}
		@Override
		protected Void doInBackground(Void... params) {
			// Create an array
			HttpParams params1 = new BasicHttpParams();
			HttpProtocolParams.setVersion(params1, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params1, "UTF-8");
			params1.setBooleanParameter("http.protocol.expect-continue", false);
			HttpClient httpclient = new DefaultHttpClient(params1);
			Log.e("FragmentCategory", "Category Url : " + mainurl);
			HttpPost httppost = new HttpPost(mainurl);

			try {
				HttpResponse http_response = httpclient.execute(httppost);
				HttpEntity entity = http_response.getEntity();
				String jsonText = EntityUtils.toString(entity, HTTP.UTF_8);
				jsonobject = new JSONObject(jsonText);
			} catch (Exception e) {

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void args) {

			try {
				// Retrieve JSON Objects from the given URL address
				try {
					if (jsonobject != null) {
						if (jsonobject.getBoolean("success")) {
							arraylist = new ArrayList<HashMap<String, String>>();
							// Locate the array name in JSON
							jsonarray = jsonobject
									.getJSONArray(AppConstants.CATEGORY_JSON_OBJECT);

							for (int i = 0; i < jsonarray.length(); i++) {
								HashMap<String, String> map = new HashMap<String, String>();
								jsonobject = jsonarray.getJSONObject(i);
								// Retrieve JSON Objects
								map.put(AppConstants.CATEGORY_ID, jsonobject
										.getString(AppConstants.CATEGORY_ID));
								map.put(AppConstants.CATEGORY_NAME, jsonobject
										.getString(AppConstants.CATEGORY_NAME));
								map.put(AppConstants.CATEGORY_HREF, jsonobject
										.getString(AppConstants.CATEGORY_HREF));
								map.put(AppConstants.CATEGORY_IMAGE, jsonobject
										.getString(AppConstants.CATEGORY_IMAGE));
								// Set the JSON Objects into the array
								arraylist.add(map);
								System.out.println("Arraylist" + arraylist);
							}

							adapter = new CategoryGridviewAdapter(context,
									arraylist);
							// Set the adapter to the ListView
							gridView.setAdapter(adapter);
							try {
								gridView.setSelection(grid_Position);
							} catch (Exception e) {
								gridView.setSelection(0);
							}
						} else {
							Toast.makeText(getActivity(), R.string.no_response,
									Toast.LENGTH_SHORT).show();
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
				System.out.println("The connection tme out");
				fr = new NoInternetFragment();
				FragmentManager fm = getFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
				fragmentTransaction.replace(R.id.fragment_place, fr);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
			}
			// Close the progressdialog
			mProgressDialog.dismiss();
		}
	}

	@Override
	public void onAttach(Activity activity) {
		listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) activity;
		listenerHideView.reversePromoItemHideTopbar();
		listenerHideView.defaultHeader();
		listenerHideView.makeDefaultHeader();
		listenerHideView.showTab();
		// listenerHideView.setTopBarForCategory();
		listenerHideView.ShopmenuRed();
		listenerHideView.loginlogout();

		hide_keyboard(activity);
		super.onAttach(activity);
	}

	@Override
	public void onPause() {

		super.onPause();
		if (mProgressDialog != null)
			mProgressDialog.dismiss();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	@Override
	public void onResume() {
		System.out.println("Entering into onResume Frequently");
		super.onResume();
	}

	public static void hide_keyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		// Find the currently focused view, so we can grab the correct window
		// token from it.
		View view = activity.getCurrentFocus();
		// If no view currently has focus, create a new one, just so we can grab
		// a window token from it
		if (view == null) {
			view = new View(activity);
		}
		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	/*
	 * private void CallHomePage() { PrefUtil.setGridcount(getActivity(), 0); fr
	 * = new FragmentOne(); FragmentManager fm = getFragmentManager();
	 * FragmentTransaction fragmentTransaction = fm.beginTransaction();
	 * fragmentTransaction.replace(R.id.fragment_place, fr);
	 * fragmentTransaction.addToBackStack(null); fragmentTransaction.commit();
	 * 
	 * Fragment fr = new FragmentCategories(); FragmentManager fm =
	 * getFragmentManager(); FragmentTransaction fragmentTransaction =
	 * fm.beginTransaction(); fragmentTransaction.replace(R.id.fragment_place,
	 * fr); fragmentTransaction.addToBackStack(null);
	 * fragmentTransaction.commit();
	 * 
	 * }
	 */

	/*
	 * @Override public void onActivityCreated(Bundle savedInstanceState) {
	 * getView().setFocusableInTouchMode(true); getView().requestFocus();
	 * 
	 * getView().setOnKeyListener(new OnKeyListener() {
	 * 
	 * @Override public boolean onKey(View v, int keyCode, KeyEvent event) { if
	 * (event.getAction() == KeyEvent.ACTION_DOWN) { if (keyCode ==
	 * KeyEvent.KEYCODE_BACK) { if(backToBestOffFlag) CallHomePage(); return
	 * true; } } return false; } });
	 * super.onActivityCreated(savedInstanceState); }
	 */
}
