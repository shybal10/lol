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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.adapter.SubCategoryListViewAdapter;
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
import java.util.Locale;

public class SubcategoryFragment extends Fragment {

	String bannerTitleString;
	String referenceUrlString;
	String categoryId;

	JSONObject jsonobject;
	JSONArray jsonarray;
	ListView listview;
	SubCategoryListViewAdapter adapter;
	ProgressDialog mProgressDialog;
	ArrayList<HashMap<String, String>> arraylist;

	Context context;

	Button backButton;
	TextView backTextView;
	String backToPreviousurl;

	Fragment fr;
	String lan;
	TextView errorTextView;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_subcategory_fragment,
				container, false);

		Log.e("The data", "The right fragment");
		SharedPreferences sp = getActivity().getSharedPreferences("lan",
				Context.MODE_PRIVATE);
		lan = sp.getString("lan", "en");
		setLanguage(lan);

		System.out.println("The languahe selected........." + lan);

		backButton = (Button) v.findViewById(R.id.buttonbackcat);
		backTextView = (TextView) v.findViewById(R.id.textViewbackcat);

		errorTextView = (TextView) v.findViewById(R.id.errortext);

		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				BackToCategories();
			}
		});
		backTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				BackToCategories();
			}
		});

		listview = (ListView) v.findViewById(R.id.listViewsubcategory);
		context = getActivity();

		errorTextView = (TextView) v.findViewById(R.id.errortext);

		listview.setVisibility(View.VISIBLE);
		errorTextView.setVisibility(View.GONE);

		/*
		 * receiving the value from Bundle
		 */
		try {
			bannerTitleString = getArguments().getString("title");
			referenceUrlString = getArguments().getString("referenceurl");
			categoryId = getArguments().getString("categoryid");
			backToPreviousurl = referenceUrlString;

			SharedPreferences sp1 = getActivity().getSharedPreferences("tag",
					Context.MODE_PRIVATE);
			String p = "fragmentSubcategorylist";
			Editor editor = sp1.edit();
			editor.putString("act", p);
			editor.putString("title", bannerTitleString);
			editor.putString("referenceurl", referenceUrlString);
			editor.putString("categoryid", categoryId);
			editor.commit();

		} catch (Exception e) {
			System.out.println("Exception caught" + e);
		}

		switch (lan) {
		case "en":
			referenceUrlString = referenceUrlString
					+ "&language=1&currency=KWD";

			break;

		default:
			referenceUrlString = referenceUrlString
					+ "&language=2&currency=KWD";
			break;
		}
		/*
		 * referenceUrlString = referenceUrlString+"&language=1&currency=KWD";
		 * }else{ referenceUrlString =
		 * referenceUrlString+"&language=2&currency=KWD"; }
		 */

		// +
		Typeface fontFace = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/LibreBaskerville-Regular.otf");
		backTextView.setTypeface(fontFace);
		backTextView.setText(bannerTitleString.toUpperCase());

		/* referenceUrlString = referenceUrlString + "&language=1&currency=KWD"; */

		System.out.println("The referece string is" + referenceUrlString);
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

	@Override
	public void onAttach(Activity activity) {

		bannerTitleString = getArguments().getString("title");
		listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) activity;
		listenerHideView.hide();
		listenerHideView.dairamBar();
		super.onAttach(activity);

	}

	private void setLanguage(String lang) {

		String languageToLoad = lang;
		Locale locale = new Locale(languageToLoad);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getActivity().getResources().updateConfiguration(config, null);

	}

	private class DownloadJSON extends AsyncTask<Void, Void, Void> {

		JSONObject jObj = new JSONObject();

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

			System.out.println("The reference url string is"
					+ referenceUrlString);

			// Create an array
			arraylist = new ArrayList<HashMap<String, String>>();
			// Retrieve JSON Objects from the given URL address
			HttpParams params1 = new BasicHttpParams();
			HttpProtocolParams.setVersion(params1, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params1, "UTF-8");
			params1.setBooleanParameter("http.protocol.expect-continue", false);
			HttpClient httpclient = new DefaultHttpClient(params1);

			Log.e("SubCategoryFragment", referenceUrlString);
			HttpPost httppost = new HttpPost(referenceUrlString);

			try {
				HttpResponse http_response = httpclient.execute(httppost);

				HttpEntity entity = http_response.getEntity();
				String jsonText = EntityUtils.toString(entity, HTTP.UTF_8);
				Log.i("Response", jsonText);
				jsonobject = new JSONObject(jsonText);
				jObj = jsonobject;

			} catch (Exception e) {
				e.printStackTrace();
			}

			/*
			 * try { // Locate the array name in JSON jsonarray =
			 * jsonobject.getJSONArray(AppConstants.SUB_CATEGORY_JSON_OBJECT);
			 * 
			 * for (int i = 0; i < jsonarray.length(); i++) { HashMap<String,
			 * String> map = new HashMap<String, String>(); jsonobject =
			 * jsonarray.getJSONObject(i); // Retrive JSON Objects
			 * map.put(AppConstants.SUB_CATEGORY_NAME,
			 * jsonobject.getString(AppConstants.SUB_CATEGORY_NAME));
			 * map.put(AppConstants.SUB_CATEGORY_HREF,
			 * jsonobject.getString(AppConstants.SUB_CATEGORY_HREF));
			 * map.put(AppConstants.SUB_CATEGORY_ID,
			 * jsonobject.getString(AppConstants.SUB_CATEGORY_ID)); // Set the
			 * JSON Objects into the array arraylist.add(map);
			 * 
			 * }
			 * 
			 * } catch (JSONException e) { Log.e("Error", e.getMessage());
			 * e.printStackTrace(); }
			 */
			return null;
		}

		@Override
		protected void onPostExecute(Void args) {

			if (jsonobject != null) {

				try {
					if (jsonobject.getBoolean("success") == false) {
						if (jsonobject.getString("product_count").equals("0")) {
							listview.setVisibility(View.GONE);
							errorTextView.setVisibility(View.VISIBLE);
						} else {
							String url = "http://dairam.com/index.php?route=api/product/list";
							fr = new CategoryProductsFragment();
							Bundle bundle = new Bundle();
							bundle.putString("title", bannerTitleString);
							bundle.putString("referenceurl", url);
							bundle.putString("firstlevel", url);
							bundle.putString("firstleveltitle",
									bannerTitleString);
							bundle.putString("categoryid", categoryId);
							bundle.putBoolean("backselecter", true);
							fr.setArguments(bundle);
							FragmentManager fm = ((Activity) context)
									.getFragmentManager();
							FragmentTransaction fragmentTransaction = fm
									.beginTransaction();
							fragmentTransaction
									.replace(R.id.fragment_place, fr);
							fragmentTransaction.addToBackStack(null);
							fragmentTransaction.commit();
						}
					} else {
						try {
							// Locate the array name in JSON
							jsonarray = jsonobject
									.getJSONArray(AppConstants.SUB_CATEGORY_JSON_OBJECT);

							for (int i = 0; i < jsonarray.length(); i++) {
								HashMap<String, String> map = new HashMap<String, String>();
								jsonobject = jsonarray.getJSONObject(i);
								// Retrive JSON Objects
								map.put(AppConstants.SUB_CATEGORY_NAME,
										jsonobject
												.getString(AppConstants.SUB_CATEGORY_NAME));
								map.put(AppConstants.SUB_CATEGORY_HREF,
										jsonobject
												.getString(AppConstants.SUB_CATEGORY_HREF));
								map.put(AppConstants.SUB_CATEGORY_ID,
										jsonobject
												.getString(AppConstants.SUB_CATEGORY_ID));
								// Set the JSON Objects into the array
								arraylist.add(map);
							}

						} catch (JSONException e) {
							Log.e("Error", e.getMessage());
							e.printStackTrace();
						}
						if (jObj.has("Title")) {
							String tit;
							try {
								tit = jObj.getString("Title");
								Log.e("Title", tit);
								/*
								 * SharedPreferences
								 * sp1=getActivity().getSharedPreferences("tag",
								 * Context.MODE_PRIVATE); Editor editor =
								 * sp1.edit(); editor.putString("title", tit);
								 * getArguments().putString("title", tit);
								 * editor.commit();
								 */
								Log.e("Bundle Argument", getArguments()
										.getString("title"));
								bannerTitleString = tit;
								backTextView.setText(tit);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						if (arraylist.size() > 0) {

							listview.setVisibility(View.VISIBLE);
							errorTextView.setVisibility(View.GONE);

							System.out.println("Arraylist" + arraylist);
							try {
								// Pass the results into ListViewAdapter.java
								adapter = new SubCategoryListViewAdapter(
										context, arraylist, referenceUrlString,
										bannerTitleString);
								// Set the adapter to the ListView
								listview.setAdapter(adapter);
							} catch (Exception e) {
								System.out
										.println("The Exception handled " + e);

							}
						} else {
							listview.setVisibility(View.GONE);
							errorTextView.setVisibility(View.VISIBLE);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				/*
				 * if(jObj.has("Title")) { String tit; try { tit =
				 * jObj.getString("Title"); Log.e("Title",tit);
				 * SharedPreferences
				 * sp1=getActivity().getSharedPreferences("tag",
				 * Context.MODE_PRIVATE); Editor editor = sp1.edit();
				 * editor.putString("title", tit);
				 * getArguments().putString("title", tit); editor.commit();
				 * Log.e("Bundle Argument",getArguments().getString("title"));
				 * bannerTitleString = tit; backTextView.setText(tit); } catch
				 * (JSONException e) { e.printStackTrace(); } }
				 * 
				 * if (arraylist.size()>0) {
				 * 
				 * listview.setVisibility(View.VISIBLE);
				 * errorTextView.setVisibility(View.GONE);
				 * 
				 * 
				 * System.out.println("Arraylist"+arraylist); try{ // Pass the
				 * results into ListViewAdapter.java adapter = new
				 * SubCategoryListViewAdapter(context,
				 * arraylist,referenceUrlString,bannerTitleString); // Set the
				 * adapter to the ListView listview.setAdapter(adapter);
				 * }catch(Exception e){
				 * System.out.println("The Exception handled "+e);
				 * 
				 * } }else{ listview.setVisibility(View.GONE);
				 * errorTextView.setVisibility(View.VISIBLE); }
				 */
			} else {
				Toast.makeText(getActivity(), R.string.no_response,
						Toast.LENGTH_SHORT).show();
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

	private void BackToCategories() {
		Fragment fr = new FragmentCategories();
		FragmentManager fm = getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_place, fr);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}

	public void setupUI(View view, boolean isHeading) {
		Typeface myTypeface;
		if (Util.getLanguage(getActivity()).equals("2")) {
			myTypeface = Typeface.createFromAsset(getActivity().getAssets(),
					"fonts/HELVETICANEUELTARABIC-ROMAN.TTF");
		} else {
			myTypeface = Typeface.createFromAsset(getActivity().getAssets(),
					"fonts/Georgia.ttf");
		}
		/* // TextView myTextView = (TextView)view.findViewById(R.id.textView1); */
		backTextView.setTypeface(myTypeface);
		errorTextView.setTypeface(myTypeface);
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
						BackToCategories();
						return true;
					}
				}
				return false;
			}
		});
		super.onActivityCreated(savedInstanceState);
	}

}
