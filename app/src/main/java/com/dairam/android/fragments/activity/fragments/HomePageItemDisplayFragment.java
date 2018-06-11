package com.dairam.android.fragments.activity.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.adapter.LandingScreenSubItemAdapter;
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.interfaces.listenerHideView;
import com.dairam.android.fragments.activity.parserfiles.JSONfunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HomePageItemDisplayFragment extends Fragment {

	String bannerTitle;
	String referenceURL;
	JSONObject jsonobject;
	JSONArray jsonarray;
	GridView gridView;
	LandingScreenSubItemAdapter adapter;
	ProgressDialog mProgressDialog;
	ArrayList<HashMap<String, String>> arraylist;
	Context context;
	Fragment fragment;
	Boolean tempFlag = false;
	View oldviView;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_home_page_item, container,
				false);
		setupUI(v, false);
		context = getActivity();

		gridView = (GridView) v.findViewById(R.id.gridViewhomepageitem);

		try {
			bannerTitle = getArguments().getString("title");
			referenceURL = getArguments().getString("referenceurl");
		} catch (Exception e) {

			System.out.println("Exception caught" + e);

		}

		new DownloadJSON().execute();

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				System.out.println("Entered into and item clicked");

				Button addToCartButton = (Button) arg1
						.findViewById(R.id.buttoncart);
				Button detailsButton = (Button) arg1
						.findViewById(R.id.buttondetails);
				TableRow tableRow = (TableRow) arg1
						.findViewById(R.id.tableRowonclickview);

				if (tempFlag == false) {
					oldviView = arg1;
					addToCartButton.setVisibility(View.VISIBLE);
					detailsButton.setVisibility(View.VISIBLE);
					tempFlag = true;
					tableRow.setVisibility(View.VISIBLE);
					tableRow.setBackgroundResource(R.drawable.transbg_withclose);
				} else {
					Button oldcart = (Button) oldviView
							.findViewById(R.id.buttoncart);
					Button olddetails = (Button) oldviView
							.findViewById(R.id.buttondetails);
					TableRow oldTableRow = (TableRow) oldviView
							.findViewById(R.id.tableRowonclickview);
					oldcart.setVisibility(View.INVISIBLE);
					olddetails.setVisibility(View.INVISIBLE);
					oldTableRow
							.setBackgroundResource(android.R.color.transparent);
					oldTableRow.setVisibility(View.INVISIBLE);

					addToCartButton.setVisibility(View.VISIBLE);
					detailsButton.setVisibility(View.VISIBLE);
					tableRow.setVisibility(View.VISIBLE);
					tableRow.setBackgroundResource(R.drawable.transbg_withclose);

					tempFlag = true;
					oldviView = arg1;

				}
			}
		});
		return v;

	}

	@Override
	public void onAttach(Activity activity) {
		listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) activity;
		listenerHideView.hide();
		bannerTitle = getArguments().getString("title");
		listenerHideView.showcategory(bannerTitle);
		listenerHideView.showSortBy();

		super.onAttach(activity);
	}

	// DownloadJSON AsyncTask
	private class DownloadJSON extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Create a progress dialog
			mProgressDialog = new ProgressDialog(getActivity(), R.style.MyTheme);
			mProgressDialog.setCancelable(false);
			mProgressDialog
					.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
			mProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// Create an array
			arraylist = new ArrayList<HashMap<String, String>>();

			// Retrieve JSON Objects from the given URL address
			jsonobject = JSONfunctions.getJSONfromURL(referenceURL
					+ "&language=1&currency=KWD");
			return null;
		}

		@Override
		protected void onPostExecute(Void args) {
			// Pass the results into ListViewAdapter.java
			try {
				if (jsonobject != null) {
					if (jsonobject.getBoolean("success")) {
						
						// Locate the array name in JSON
						jsonarray = jsonobject.getJSONArray(AppConstants.HOME_SUB_JSON_OBJECT);
						for (int i = 0; i < jsonarray.length(); i++) {
							HashMap<String, String> map = new HashMap<String, String>();
							jsonobject = jsonarray.getJSONObject(i);
							
							// Retrieve JSON Objects
							map.put(AppConstants.HOME_SUB_ID, jsonobject
									.getString(AppConstants.HOME_SUB_ID));
							map.put(AppConstants.HOME_SUB_NAME, jsonobject
									.getString(AppConstants.HOME_SUB_NAME));
							map.put(AppConstants.HOME_SUB_PRICE, jsonobject
									.getString(AppConstants.HOME_SUB_PRICE));
							map.put(AppConstants.HOME_SUB_SPECIAL, jsonobject
									.getString(AppConstants.HOME_SUB_SPECIAL));
							map.put(AppConstants.HOME_SUB_URL, jsonobject
									.getString(AppConstants.HOME_SUB_URL));
							map.put(AppConstants.HOME_SUB_IMAGE, jsonobject
									.getString(AppConstants.HOME_SUB_IMAGE));
							map.put(AppConstants.HOME_SUB_LANG, jsonobject
									.getString(AppConstants.HOME_SUB_LANG));
							map.put(AppConstants.HOME_SUB_CURRENCY, jsonobject
									.getString(AppConstants.HOME_SUB_CURRENCY));

							// Set the JSON Objects into the array
							arraylist.add(map);
							System.out.println("Arraylist" + arraylist);
						}
						adapter = new LandingScreenSubItemAdapter(context,
								arraylist);
						
						// Set the adapter to the ListView
						gridView.setAdapter(adapter);
						adapter.notifyDataSetChanged();

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

	public void setupUI(View view, boolean isHeading) {

		if ((view instanceof TextView)) {

			((TextView) view).setTypeface(getRegularTypeFace());
		}

	}

	Typeface getRegularTypeFace() {
		return Typeface.createFromAsset(context.getAssets(),
				context.getString(R.string.regular_typeface));
	}
}