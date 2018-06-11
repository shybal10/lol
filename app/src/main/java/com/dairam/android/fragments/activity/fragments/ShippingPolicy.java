package com.dairam.android.fragments.activity.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.interfaces.listenerHideView;

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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class ShippingPolicy extends Fragment {
	JSONObject jsonobject;
	JSONArray jsonarray;
	ProgressDialog mProgressDialog;
	ArrayList<HashMap<String, String>> arraylist;
	Context context;
	Fragment fr;
	String contentString;

	TextView textViewTermsAndConditions;
	// WebView textViewTermsAndConditions;

	String mainurl;

	Button backButton;
	TextView backTextView;

	// "http://dairam.com/index.php?route=api/information/get&language=1&info_id=7&currency=KWD";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		SharedPreferences sp = getActivity().getSharedPreferences("lan",
				Context.MODE_PRIVATE);
		String lan = sp.getString("lan", "en");
		setLanguage(lan);
		SharedPreferences sp1 = getActivity().getSharedPreferences("tag",
				Context.MODE_PRIVATE);
		Editor editor = sp1.edit();
		editor.putString("act", "shippingpolicy");
		editor.commit();

		switch (lan) {
		case "en":
			mainurl = AppConstants.SHIPPING_URL + "1"
					+ "&info_id=7&currency=KWD";
			break;

		default:
			mainurl = AppConstants.SHIPPING_URL + "2"
					+ "&info_id=7&currency=KWD";
			break;
		}

		View v = inflater.inflate(R.layout.activity_shippingpolicy, container,
				false);
		
		textViewTermsAndConditions = (TextView) v.findViewById(R.id.webview);
		/*
		 * textViewTermsAndConditions = (WebView)v.findViewById(R.id.webview);
		 * textViewTermsAndConditions.getSettings().setJavaScriptEnabled(true);
		 * textViewTermsAndConditions
		 * .getSettings().setLoadWithOverviewMode(true);
		 * textViewTermsAndConditions.getSettings().setUseWideViewPort(false);
		 * textViewTermsAndConditions
		 * .setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		 * textViewTermsAndConditions.setScrollbarFadingEnabled(false);
		 * textViewTermsAndConditions
		 * .getSettings().setBuiltInZoomControls(true);
		 */

		backButton = (Button) v.findViewById(R.id.buttonbackshipping);
		backTextView = (TextView) v.findViewById(R.id.textViewbackshipping);
		Typeface fontFace = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/LibreBaskerville-Regular.otf");
		backTextView.setTypeface(fontFace);

		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				backToHome();
			}
		});

		backTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				backToHome();
			}
		});

		context = getActivity();
		new DownloadJSON().execute();
		
		switch (lan) {
		case "en":
			
			break;

		default:
			setupUI(v, false);
			break;
		}
		return v;
	}

	public void backToHome() {
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

			try {
				// Create an array
				arraylist = new ArrayList<HashMap<String, String>>();
				// Retrieve JSON Objects from the given URL address
				HttpParams params1 = new BasicHttpParams();
				HttpProtocolParams.setVersion(params1, HttpVersion.HTTP_1_1);
				HttpProtocolParams.setContentCharset(params1, "UTF-8");
				params1.setBooleanParameter("http.protocol.expect-continue",
						false);
				HttpClient httpclient = new DefaultHttpClient(params1);

				HttpPost httppost = new HttpPost(mainurl);
				try {
					HttpResponse http_response = httpclient.execute(httppost);

					HttpEntity entity = http_response.getEntity();
					String jsonText = EntityUtils.toString(entity, HTTP.UTF_8);
					Log.i("Response", jsonText);
					jsonobject = new JSONObject(jsonText);

				} catch (Exception e) {

				}

			} catch (Exception e) {
				System.out.println("the application time out");
				fr = new NoInternetFragment();
				FragmentManager fm = getFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
				fragmentTransaction.replace(R.id.fragment_place, fr);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void args) {

			try {

				if (jsonobject != null) {
					if (jsonobject.getBoolean("success")) {
						// Locate the array name in JSON
						jsonarray = jsonobject
								.getJSONArray(AppConstants.SHIPPING_JSON_OBJECT);

						for (int i = 0; i < jsonarray.length(); i++) {
							HashMap<String, String> map = new HashMap<String, String>();
							jsonobject = jsonarray.getJSONObject(i);
							// Retrive JSON Objects
							map.put(AppConstants.SHIPPING_TITLE, jsonobject
									.getString(AppConstants.SHIPPING_TITLE));
							map.put(AppConstants.SHIPPING_DESC, jsonobject
									.getString(AppConstants.SHIPPING_DESC));
							contentString = jsonobject
									.getString(AppConstants.SHIPPING_DESC);
							System.out.println("The content String is"
									+ contentString);
							// Set the JSON Objects into the array
							arraylist.add(map);

						}
						String text = "<html><body style=\"text-align:justify\"> %s </body></Html>";

						textViewTermsAndConditions.setText(Html
								.fromHtml(contentString));

						// textViewTermsAndConditions.loadData(String.format(text,
						// contentString), "text/html", "utf-8");
					} else {
						Toast.makeText(getActivity(), R.string.no_response,
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getActivity(), R.string.no_response,
							Toast.LENGTH_SHORT).show();
				}
				mProgressDialog.dismiss();
			} catch (Exception e) {
				System.out.println("The Exception handled " + e);

			}
			// Close the progress dialog

		}
	}

	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);
		listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) activity;
		listenerHideView.reversePromoItemHideTopbar();
		listenerHideView.hide();
		listenerHideView.dairamBar();

	}

	private void setLanguage(String lang) {

		String languageToLoad = lang;
		Locale locale = new Locale(languageToLoad);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getActivity().getResources().updateConfiguration(config, null);

	}

	public void setupUI(View view, boolean isHeading) {
		  Typeface myTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/HELVETICANEUELTARABIC-ROMAN.TTF");
		  /*  // TextView myTextView = (TextView)view.findViewById(R.id.textView1);
		     myTextView.setTypeface(myTypeface);
		     backTextView.setTypeface(myTypeface);*/
	
		 }

	Typeface getRegularTypeFace() {
		return Typeface.createFromAsset(context.getAssets(),
				context.getString(R.string.regular_typeface));
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
						backToHome();
						return true;
					}
				}
				return false;
			}
		});
		super.onActivityCreated(savedInstanceState);
	}

}
