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
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class TermsAndconditionsFragment extends Fragment {

	JSONObject jsonobject;
	JSONArray jsonarray;
	ProgressDialog mProgressDialog;
	ArrayList<HashMap<String, String>> arraylist;
	Context context;
	Fragment fr;
	String contentString;

	WebView textViewTermsAndConditions;

	String mainUrl;

	TextView backTextView;
	Button backButton;
	String lan;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		SharedPreferences sp = getActivity().getSharedPreferences("lan",
				Context.MODE_PRIVATE);
		 lan = sp.getString("lan", "en");
		setLanguage(lan);

		SharedPreferences sp1 = getActivity().getSharedPreferences("tag",
				Context.MODE_PRIVATE);
		String p = "termsandconditions";
		Editor editor = sp1.edit();
		editor.putString("act", p);
		editor.commit();

		switch (lan) {
		case "en":
			mainUrl = AppConstants.TERMS_CONDTN_URL + "1"
					+ "&info_id=5&currency=KWD";

			break;

		default:
			mainUrl = AppConstants.TERMS_CONDTN_URL + "2"
					+ "&info_id=5&currency=KWD";
			break;
		}
		/*
		 * mainUrl =
		 * AppConstants.TERMS_CONDTN_URL+"1"+"&info_id=5&currency=KWD"; } else{
		 * mainUrl =
		 * AppConstants.TERMS_CONDTN_URL+"2"+"&info_id=5&currency=KWD"; }
		 */

		View v = inflater.inflate(R.layout.activity_termsandconditions,
				container, false);
		setupUI(v, false);
		textViewTermsAndConditions = (WebView) v.findViewById(R.id.webview);
		textViewTermsAndConditions.getSettings().setJavaScriptEnabled(true);
		textViewTermsAndConditions.getSettings().setLoadWithOverviewMode(true);

		// textViewTermsAndConditions.getSettings().setUseWideViewPort(true);
		textViewTermsAndConditions
				.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		textViewTermsAndConditions.setScrollbarFadingEnabled(false);
		textViewTermsAndConditions.getSettings().setBuiltInZoomControls(true);
		// textViewTermsAndConditions.setInitialScale(1);

		textViewTermsAndConditions.getSettings().setBuiltInZoomControls(true);
		textViewTermsAndConditions.getSettings().setSupportZoom(true);
		backButton = (Button) v.findViewById(R.id.buttonbackterms);
		backTextView = (TextView) v.findViewById(R.id.textViewbackterms);
		Typeface fontFace = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/LibreBaskerville-Regular.otf");
		backTextView.setTypeface(fontFace);
		backTextView.setEnabled(true);
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				BackToHomePage();
			}
		});

		backTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				BackToHomePage();
			}
		});
		textViewTermsAndConditions.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Log.e("urlllllll", "" + url);
				/* view.loadUrl(url); */

				if (url.equals("http://dairam.com/index.php?route=information/contact")) {
					Fragment custcarefrag = new CustomerCareFragment();

					FragmentManager fragmentManager = getFragmentManager();

					// Creating a fragment transaction
					FragmentTransaction ft = fragmentManager.beginTransaction();

					// Adding a fragment to the fragment transaction
					ft.replace(R.id.fragment_place, custcarefrag);

					// Committing the transaction
					ft.commit();
				} else if (url.equals("http://dairam.com/")) {
					textViewTermsAndConditions.getSettings()
							.setUseWideViewPort(true);
					// textViewTermsAndConditions.setInitialScale(50);
					textViewTermsAndConditions.loadUrl(url);
				} else {
					// textViewTermsAndConditions.getSettings().setUseWideViewPort(true);
					textViewTermsAndConditions.loadUrl(url);
				}
				// return super.shouldOverrideUrlLoading(view, url);
				return true;

			}
		});

		context = getActivity();
		new DownloadJSON().execute();
		switch (lan) {
		case "en":
			
			break;

		default:
			  Typeface myTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/HELVETICANEUELTARABIC-ROMAN.TTF");
			  
			 /*    backTextView.setTypeface(myTypeface);*/
			break;
		}
		
		return v;
	}

	public void BackToHomePage() {

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
			// Create a progressdialog
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

				HttpPost httppost = new HttpPost(mainUrl);
				try {
					HttpResponse http_response = httpclient.execute(httppost);

					HttpEntity entity = http_response.getEntity();
					String jsonText = EntityUtils.toString(entity, HTTP.UTF_8);
					Log.i("Response", jsonText);
					jsonobject = new JSONObject(jsonText);

				} catch (Exception e) {

				}

			} catch (Exception e) {
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
			Boolean flag = false;
			if (jsonobject != null) {
				try {
					flag = jsonobject.getBoolean("success");
				} catch (Exception e) {

				}
				if (flag) {
					try {
						// Locate the array name in JSON
						jsonarray = jsonobject
								.getJSONArray(AppConstants.TERMS_CONDTN_JSON_OBJECT);

						for (int i = 0; i < jsonarray.length(); i++) {
							HashMap<String, String> map = new HashMap<String, String>();
							jsonobject = jsonarray.getJSONObject(i);
							// Retrive JSON Objects
							map.put(AppConstants.TERMS_CONDTN_TITLE, jsonobject
									.getString(AppConstants.TERMS_CONDTN_TITLE));
							map.put(AppConstants.TERMS_CONDTN_DESC, jsonobject
									.getString(AppConstants.TERMS_CONDTN_DESC));
							contentString = jsonobject
									.getString(AppConstants.TERMS_CONDTN_DESC);
							// Set the JSON Objects into the array
							arraylist.add(map);
						}
					} catch (JSONException e) {
						Log.e("Error", e.getMessage());
						e.printStackTrace();
					}
					try {
						// textViewTermsAndConditions.setText(Html.fromHtml(contentString));
						System.out.println("The content String is......"
								+ contentString);

						textViewTermsAndConditions.getSettings()
								.setJavaScriptEnabled(true);
						textViewTermsAndConditions.loadDataWithBaseURL("",
								contentString, "text/html", "UTF-8", "");
						
					} catch (Exception e) {

					}
				} else {
					Toast.makeText(getActivity(), R.string.no_response,
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getActivity(), R.string.no_response,
						Toast.LENGTH_SHORT).show();
			}
			// Close the progress dialog
			mProgressDialog.dismiss();
		}
	}

	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);
		listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) activity;
		listenerHideView.reversePromoItemHideTopbar();
		listenerHideView.hide();
		listenerHideView.dairamBar();
		/* listenerHideView.hide(); */
		/* listenerHideView.showcategory("TERMS AND CONDITIONS"); */

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

		if ((view instanceof TextView)) {

			((TextView) view).setTypeface(getRegularTypeFace());
		}

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
						BackToHomePage();
						return true;
					}
				}
				return false;
			}
		});
		super.onActivityCreated(savedInstanceState);
	}

}
