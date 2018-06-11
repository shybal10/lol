package com.dairam.android.fragments.activity.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.adapter.ListViewAdapter;
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

public class FragmentOne extends Fragment {
	JSONObject jsonobject;
	JSONArray jsonarray;
	ListView listview;
	ListViewAdapter adapter;
	ProgressDialog mProgressDialog;
	ArrayList<HashMap<String, String>> arraylist;

	Context context;

	LinearLayout category;
	ConnectionDetector cd;

	Boolean isInternetPresent = false;
	Fragment fr;

	public static final String MyPREFERENCES = "MyPrefs";
	public static final String customerid = "id";
	public static final String firstname = "firstname";
	public static final String lastname = "lastname";
	public static final String email_string = "email";
	public static final String cart_string = "cart";
	public static final String wishlist_String = "wishlist";

	SharedPreferences sharedPreferences;

	String cart_count_;
	String wishlisr_count_;

	Activity sample;
	String mainurl;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_one, container, false);

		setupUI(v, false);

		sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES,
				Context.MODE_PRIVATE);

		SharedPreferences sp1 = getActivity().getSharedPreferences("tag",
				Context.MODE_PRIVATE);
		String p = "fragmentone";
		Editor editor = sp1.edit();
		editor.putString("act", p);
		editor.commit();

		SharedPreferences sp = getActivity().getSharedPreferences("lan",
				Context.MODE_PRIVATE);
		String lan = sp.getString("lan", "en");

		System.out.println("The language is" + lan);

		switch (lan) {
		case "en":
			System.out.println("Entered herennnnnnn");
			mainurl = AppConstants.HOME_PAGE_URL + "1&currency=KWD";
			break;

		default:
			System.out.println("Entered here.......nnnnnnn");
			mainurl = AppConstants.HOME_PAGE_URL + "2&currency=KWD";
			break;
		}

		ImageLoader imageLoader = new ImageLoader(getActivity());
		imageLoader.clearCache();

		String id_ = null;

		if (sharedPreferences.contains(customerid)) {
			id_ = sharedPreferences.getString(customerid, "");
		}

		if (id_ == null) {

		} else {
			cart_count_ = sharedPreferences.getString(cart_string, "");
			wishlisr_count_ = sharedPreferences.getString(wishlist_String, "");
			listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) sample;
			listenerHideView.cartListCountUpdation(cart_count_);
			listenerHideView.wishListCountUpdation(wishlisr_count_);
		}

		listview = (ListView) v.findViewById(R.id.listViewcatogories);

		context = getActivity();
		cd = new ConnectionDetector(context);
		isInternetPresent = cd.isConnectingToInternet();

		if (isInternetPresent) {

			System.out.println("Internet present");
			new DownloadJSON().execute();
		}

		else {
			System.out.println("Internet not available");

			fr = new NoInternetFragment();
			FragmentManager fm = getFragmentManager();
			FragmentTransaction fragmentTransaction = fm.beginTransaction();
			fragmentTransaction.replace(R.id.fragment_place, fr);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
		}

		return v;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) activity;
		listenerHideView.reversePromoItemHideTopbar();
		listenerHideView.defaultHeader();
		listenerHideView.makeDefaultHeader();
		listenerHideView.showTab();
		//listenerHideView.ShopmenuRed();
		listenerHideView.setTopBarForCategory();
		listenerHideView.loginlogout();
		sample = activity;
	}

	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
		}
		return false;
	}

	private class DownloadJSON extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			System.out.println("Here I am............." + mainurl);
			mProgressDialog = new ProgressDialog(getActivity(), R.style.MyTheme);
			mProgressDialog.setCancelable(false);
			mProgressDialog
					.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
			mProgressDialog.show();

		}

		@Override
		protected Void doInBackground(Void... params) {

			HttpParams params1 = new BasicHttpParams();
			HttpProtocolParams.setVersion(params1, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params1, "UTF-8");
			params1.setBooleanParameter("http.protocol.expect-continue", false);
			HttpClient httpclient = new DefaultHttpClient(params1);
			try {
				HttpPost httppost = new HttpPost(mainurl);
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
				// Create an array
				arraylist = new ArrayList<HashMap<String, String>>();
				// Retrieve JSON Objects from the given URL address
				/* jsonobject = JSONfunctions.getJSONfromURL(mainurl); */

				try {
					if (jsonobject != null) {
						if (jsonobject.getBoolean("success")) {

							// Locate the array name in JSON
							jsonarray = jsonobject
									.getJSONArray(AppConstants.HOME_PAGE_JSON_OBJECT);

							for (int i = 0; i < jsonarray.length(); i++) {
								HashMap<String, String> map = new HashMap<String, String>();
								jsonobject = jsonarray.getJSONObject(i);
								// Retrieve JSON Objects
								map.put(AppConstants.HOME_PAGE_TAG_TITLE,
										jsonobject
												.getString(AppConstants.HOME_PAGE_TAG_TITLE));
								map.put(AppConstants.HOME_PAGE_TAG_LINK,
										jsonobject
												.getString(AppConstants.HOME_PAGE_TAG_LINK));
								map.put(AppConstants.HOME_PAGE_TAG_IMAGE,
										jsonobject
												.getString(AppConstants.HOME_PAGE_TAG_IMAGE));

								// Set the JSON Objects into the array
								arraylist.add(map);

							

							}
							adapter = new ListViewAdapter(context,
									arraylist);
							// Set the adapter to the ListView
							listview.setAdapter(adapter);
						} else {
							Toast.makeText(getActivity(), R.string.no_response,
									Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(getActivity(), R.string.no_response,
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {

					e.printStackTrace();
				}

			} catch (Exception e) {

				try {

					System.out.println("Enterd.... here.........."
							+ e.getMessage());

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
	public void onPause() {

		super.onPause();
		if (mProgressDialog != null)
			mProgressDialog.dismiss();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
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
	
	private void CallHomePage() {
		PrefUtil.setGridcount(getActivity(), 0);
		PrefUtil.setBrandGridcount(getActivity(), 0);	
		PrefUtil.setCatGridcount(getActivity(), 0);	
		/*fr = new FragmentOne();
		FragmentManager fm = getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_place, fr);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();*/
		
		Fragment fr = new FragmentCategories();
		FragmentManager fm = getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_place, fr);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();

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
						
						CallHomePage();
						return true;
					}
				}
				return false;
			}
		});
		super.onActivityCreated(savedInstanceState);
	}

}
