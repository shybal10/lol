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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.adapter.SampleAdapter;
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.connectioncheck.ConnectionDetector;
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

public class StylesFragment extends Fragment implements
		AbsListView.OnScrollListener, AbsListView.OnItemClickListener {

	private static final String TAG = "StaggeredGridActivityFragment";

	//private StaggeredGridView mGridView;
	private GridView mGridView;
	private boolean mHasRequestedMore;
	private SampleAdapter mAdapter;

	private ArrayList<String> mData;

	private JSONObject jsonobject;
	private JSONArray jsonarray;
	private ProgressDialog mProgressDialog;
	private ArrayList<HashMap<String, String>> arraylist;

	Context context;

	ConnectionDetector cd;
	Boolean isInternetPresent;
	Fragment fr;

	Button backButton;
	TextView backTextView;

	String mainUrl;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		/*
		 * { mainUrl = AppConstants.STYLE_URL+"1"; } else{ mainUrl =
		 * AppConstants.STYLE_URL+"2"; }
		 */

		SharedPreferences sp1 = getActivity().getSharedPreferences("tag",
				Context.MODE_PRIVATE);
		String p = "fragmentstyle";
		Editor editor = sp1.edit();
		editor.putString("act", p);
		editor.commit();

		View v = inflater.inflate(R.layout.fragments_style, container, false);

		//mGridView = (StaggeredGridView) v.findViewById(R.id.grid_view);
		mGridView = (GridView) v.findViewById(R.id.grid_view);
		backButton = (Button) v.findViewById(R.id.buttonbackstyle);
		backTextView = (TextView) v.findViewById(R.id.textViewbackstyle);

		Typeface fontFace = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/LibreBaskerville-Regular.otf");
		backTextView.setTypeface(fontFace);

		SharedPreferences sp = getActivity().getSharedPreferences("lan",
				Context.MODE_PRIVATE);
		String lan = sp.getString("lan", "en");

		switch (lan) {

		case "en":
			mainUrl = AppConstants.STYLE_URL + "1&currency=KWD";
			break;

		default:
			mainUrl = AppConstants.STYLE_URL + "2&currency=KWD";
			setupUI(v, false);
			break;
		}

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

		final LayoutInflater layoutInflater = getActivity().getLayoutInflater();
		View header = layoutInflater.inflate(R.layout.list_item_header_footer,
				null);
		View footer = layoutInflater.inflate(R.layout.list_item_header_footer,
				null);
		//mGridView.addHeaderView(header);
		//mGridView.addFooterView(footer);

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

		mGridView.setOnScrollListener(this);
		mGridView.setOnItemClickListener(this);

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

	@Override
	public void onScrollStateChanged(final AbsListView view,
			final int scrollState) {
		Log.d("stylfrag", "onScrlSteChngd:" + scrollState);
	}

	@Override
	public void onScroll(final AbsListView view, final int firstVisibleItem,
			final int visibleItemCount, final int totalItemCount) {
		Log.d("stylfrag", "onScroll firstVisibleItem:" + firstVisibleItem
				+ " visibleItemCount:" + visibleItemCount + " totalItemCount:"
				+ totalItemCount);
		// our handling
		if (!mHasRequestedMore) {
			int lastInScreen = firstVisibleItem + visibleItemCount;
			if (lastInScreen >= totalItemCount) {
				Log.d("stylfrag", "onScroll lastInScreen - so load more");
				mHasRequestedMore = true;
				onLoadMoreItems();
			}
		}
	}

	private void onLoadMoreItems() {
		/*
		 * final ArrayList<String> sampleData = SampleData.generateSampleData();
		 * for (String data : sampleData) { mAdapter.add(data); } // stash all
		 * the data in our backing store mData.addAll(sampleData);
		 */
		// notify the adapter that we can update now
		// mAdapter.notifyDataSetChanged();
		mHasRequestedMore = false;
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		// Toast.makeText(getActivity(), "Item Clicked: " + position,
		// Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) activity;
		listenerHideView.reversePromoItemHideTopbar();
		listenerHideView.hide();
		listenerHideView.dairamBar();
		listenerHideView.makeDefaultHeader();
		listenerHideView.setTopBarForStyle();
	}

	/*
	 * Asynch task for parsing data from JSON
	 */
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
			// Create an array
			try {
				arraylist = new ArrayList<HashMap<String, String>>();
				// Retrieve JSON Objects from the given URL address
				HttpParams params1 = new BasicHttpParams();
				HttpProtocolParams.setVersion(params1, HttpVersion.HTTP_1_1);
				HttpProtocolParams.setContentCharset(params1, "UTF-8");
				params1.setBooleanParameter("http.protocol.expect-continue",
						false);
				HttpClient httpclient = new DefaultHttpClient(params1);
				Log.e("Frag Style MainURL", mainUrl);
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
				System.out.println("Connection time out");
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
								.getJSONArray(AppConstants.STYLE_JSON_OBJECT);

						for (int i = 0; i < jsonarray.length(); i++) {
							HashMap<String, String> map = new HashMap<String, String>();
							jsonobject = jsonarray.getJSONObject(i);
							// Retrive JSON Objects
							map.put(AppConstants.STYLE_ID,
									jsonobject.getString(AppConstants.STYLE_ID));
							map.put(AppConstants.STYLE_TITLE, jsonobject
									.getString(AppConstants.STYLE_TITLE));
							map.put(AppConstants.STYLE_DESCRIPTION, jsonobject
									.getString(AppConstants.STYLE_DESCRIPTION));
							map.put(AppConstants.STYLE_IMAGE, jsonobject
									.getString(AppConstants.STYLE_IMAGE));
							map.put(AppConstants.STYLE_HREF, jsonobject
									.getString(AppConstants.STYLE_HREF));

							// Set the JSON Objects into the array
							arraylist.add(map);

							System.out.println("The arraylist parsed is"
									+ arraylist);

						}

						System.out.println("Arraylist" + arraylist);
						try {
							mAdapter = new SampleAdapter(getActivity(),
									R.id.txt_line1, arraylist);

							mGridView.setAdapter(mAdapter);
							mProgressDialog.dismiss();

						} catch (Exception e) {
							System.out.println("The Exception handled " + e);

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
		}
	}

	public void setupUI(View view, boolean isHeading) {
		Typeface myTypeface = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/HELVETICANEUELTARABIC-ROMAN.TTF");
		if(Util.getLanguage(getActivity()).equals("2")){
			/*backTextView.setTypeface(myTypeface);*/
		}
	}

	Typeface getRegularTypeFace() {
		return Typeface.createFromAsset(context.getAssets(),
				context.getString(R.string.regular_typeface));
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mProgressDialog.dismiss();
		super.onDestroy();
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
