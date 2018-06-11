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
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.interfaces.listenerHideView;
import com.dairam.android.fragments.activity.parserfiles.ImageLoader;
import com.dairam.android.fragments.activity.parserfiles.JSONfunctions;
import com.dairam.android.fragments.activity.utillities.Util;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SaleFragment extends Fragment {

	RelativeLayout relativeLayoutwomen, relativeLayoutmen, relativeLayoutkids,
			relativeLayoutall;
	ImageView womanImageView, menImageView, kidsImageView, allImageView;

	HashMap<String, String> resultp = new HashMap<String, String>();

	JSONObject jsonobject;
	JSONArray jsonarray;
	ProgressDialog mProgressDialog;
	ArrayList<HashMap<String, String>> arraylist;

	Context context;
	Fragment fr;

	ImageLoader imageLoader;

	GridView gridView;

	Button buttonBackSale;
	TextView textViewBackSale;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		SharedPreferences sp = getActivity().getSharedPreferences("lan",
				Context.MODE_PRIVATE);
		String lan = sp.getString("lan", "en");

		View v = inflater.inflate(R.layout.fragment_sale, container, false);
	
		SharedPreferences sp1 = getActivity().getSharedPreferences("tag",
				Context.MODE_PRIVATE);
		String p = "fragmentsales";
		Editor editor = sp1.edit();
		editor.putString("act", p);
		editor.commit();

		context = getActivity();

		/* gridView = (GridView)v.findViewById(R.id.gridViewsale); */

		imageLoader = new ImageLoader(context);

		imageLoader.clearCache();

		relativeLayoutwomen = (RelativeLayout) v
				.findViewById(R.id.relativelaywoman);
		relativeLayoutmen = (RelativeLayout) v
				.findViewById(R.id.relativelaymen);
		relativeLayoutkids = (RelativeLayout) v
				.findViewById(R.id.relativelaykids);
		relativeLayoutall = (RelativeLayout) v
				.findViewById(R.id.relativelaysale);

		buttonBackSale = (Button) v.findViewById(R.id.buttonbacksale);
		textViewBackSale = (TextView) v.findViewById(R.id.textViewbacksale);

		Typeface fontFace = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/LibreBaskerville-Regular.otf");
		textViewBackSale.setTypeface(fontFace);

		buttonBackSale.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				BackToHome();
			}
		});

		textViewBackSale.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				BackToHome();
			}
		});

		womanImageView = (ImageView) v.findViewById(R.id.imageviewwoman);
		menImageView = (ImageView) v.findViewById(R.id.imageviewmen);
		kidsImageView = (ImageView) v.findViewById(R.id.imageviekids);
		allImageView = (ImageView) v.findViewById(R.id.imagevieall);

		womanImageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				resultp = arraylist.get(0);
				String title = resultp.get(AppConstants.SALE_TITLE);
				String href = resultp.get(AppConstants.SALE_HREF);

				System.out.println("The title and href is" + title + href);
				/*CallnewFragment(
						"WOMAN",
						"http://dairam.com/index.php?route=api/sales/list&id=women&language=1&currency=KWD");*/
				CallnewFragment(
						"WOMAN",
						"http://dairam.com/index.php?route=api/sales/list&id=women&language=");

			}
		});

		menImageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				/*CallnewFragment(
						"MEN",
						"http://dairam.com/index.php?route=api/sales/list&id=men&language=1&currency=KWD");*/
				CallnewFragment(
						"MEN",
						"http://dairam.com/index.php?route=api/sales/list&id=men&language=");
			}
		});

		kidsImageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CallnewFragment("KIDS",
						"http://dairam.com/index.php?route=api/sales/list&id=kid&language=");
			}
		});

		allImageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CallnewFragment("ALL SALE",
						"http://dairam.com/index.php?route=api/sales/list&language=");
			}
		});

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

	private void BackToHome() {
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
				SharedPreferences sp = getActivity().getSharedPreferences(
						"lan", Context.MODE_PRIVATE);
				if (sp.getString("lan", "en").equals("en"))
					jsonobject = JSONfunctions
							.getJSONfromURL(AppConstants.SALE_URL);
				else
					jsonobject = JSONfunctions
							.getJSONfromURL(AppConstants.SALE_URL_AR);

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
				mProgressDialog.dismiss();
			} catch (Exception e) {
				System.out.println("The Exception caught");
			}

			try {

				if (jsonobject != null) {
					if (jsonobject.getBoolean("success")) {
						// Locate the array name in JSON
						jsonarray = jsonobject
								.getJSONArray(AppConstants.SALE_JSONOBJECT);

						for (int i = 0; i < jsonarray.length(); i++) {
							HashMap<String, String> map = new HashMap<String, String>();
							jsonobject = jsonarray.getJSONObject(i);

							// Retrive JSON Objects
							map.put(AppConstants.SALE_TITLE, jsonobject
									.getString(AppConstants.SALE_TITLE));
							map.put(AppConstants.SALE_IMAGE, jsonobject
									.getString(AppConstants.SALE_IMAGE));
							map.put(AppConstants.SALE_HREF, jsonobject
									.getString(AppConstants.SALE_HREF));
							// Set the JSON Objects into the array
							arraylist.add(map);

						}

						resultp = arraylist.get(0);
						// imageLoader.DisplayImage(resultp.get(AppConstants.SALE_IMAGE),
						// womanImageView);
						Picasso.with(context)
								.load(resultp.get(AppConstants.SALE_IMAGE))
								.placeholder(R.drawable.loading320)
								.into(womanImageView);

						resultp = arraylist.get(1);
						Picasso.with(context)
								.load(resultp.get(AppConstants.SALE_IMAGE))
								.placeholder(R.drawable.loading320)
								.into(menImageView);

						resultp = arraylist.get(2);
						Picasso.with(context)
								.load(resultp.get(AppConstants.SALE_IMAGE))
								.placeholder(R.drawable.loading320)
								.into(kidsImageView);

						resultp = arraylist.get(3);
						Picasso.with(context)
								.load(resultp.get(AppConstants.SALE_IMAGE))
								.placeholder(R.drawable.loading320)
								.into(allImageView);
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

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) activity;
		listenerHideView.reversePromoItemHideTopbar();
		listenerHideView.hide();
		listenerHideView.hidePopup();
		listenerHideView.dairamBar();
	}

	public void CallnewFragment(String title, String referenceUrl) {
		fr = new SalesItemDisplayFragment();
		Bundle bundle = new Bundle();
		bundle.putString("title", title);
		bundle.putString("referenceurl", referenceUrl);
		fr.setArguments(bundle);
		FragmentManager fm = ((Activity) context).getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_place, fr);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		System.out.println("On Destroy called");

		imageLoader.clearCache();
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
		     TextView myTextView = (TextView)view.findViewById(R.id.textView1);
		     TextView men = (TextView)view.findViewById(R.id.textViewmen);
		     TextView kids = (TextView)view.findViewById(R.id.textViewkids);
		     textViewBackSale.setTypeface(myTypeface);
		     myTextView.setTypeface(myTypeface);
		    /* textViewBackSale.setTypeface(myTypeface);*/
		     men.setTypeface(myTypeface);
		     kids.setTypeface(myTypeface);
		
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
						BackToHome();
						return true;
					}
				}
				return false;
			}
		});
		super.onActivityCreated(savedInstanceState);
	}
}
