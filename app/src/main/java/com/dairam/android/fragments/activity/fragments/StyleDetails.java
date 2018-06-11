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
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.interfaces.listenerHideView;
import com.dairam.android.fragments.activity.parserfiles.JSONfunctions;
import com.dairam.android.fragments.activity.utillities.Util;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class StyleDetails extends Fragment {
	String desc;
	String url;
	String styleId;

	TextView txt;
	ImageView img;

	Button backButton;
	TextView backTextView;
	private ProgressDialog mProgressDialog;

	String getStyleDetailURL;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.style_details, container, false);

		backButton = (Button) v.findViewById(R.id.buttonbackstyledetails);
		backTextView = (TextView) v
				.findViewById(R.id.textViewbackstylestyledetails);
		backTextView.setSelected(true);

		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				backToStylePage();
			}
		});

		backTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				backToStylePage();
			}
		});

		txt = (TextView) v.findViewById(R.id.textstyle);
		img = (ImageView) v.findViewById(R.id.imageView1txt);
		txt.setClickable(true);
		txt.setMovementMethod(LinkMovementMethod.getInstance());
		try {
			desc = getArguments().getString("title");
			url = getArguments().getString("referenceurl");
			styleId = getArguments().getString("styleID");
			Log.e("Description", desc);
			Log.e("Url", url);
			Log.e("ID", styleId);
			SharedPreferences sp1 = getActivity().getSharedPreferences("tag",
					Context.MODE_PRIVATE);
			String p = "fragmentstyledetails";
			Editor editor = sp1.edit();
			editor.putString("act", p);
			editor.putString("title", desc);
			editor.putString("referenceurl", url);
			editor.putString("styleID", styleId);
			editor.commit();

		} catch (Exception e) {

			System.out.println("Exception caught" + e);

		}

		getStyleDetailURL = AppConstants.STYLE_DETAILS + styleId + "&language="
				+ getLanguage();
		new GetStyleDetails().execute();
		System.out.println("kjd " + desc + "kjf " + url);
		Picasso.with(getActivity()).load(url)
				.placeholder(R.drawable.loading320).into(img);

		switch (getLanguage()) {
		case "en":

			break;

		default:
			setupUI(v, false);
			break;
		}

		return v;

	}

	private void backToStylePage() {
		Fragment fr = new StylesFragment();
		FragmentManager fm = getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_place, fr);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);

		listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) activity;
		listenerHideView.reversePromoItemHideTopbar();
		listenerHideView.hide();
		listenerHideView.dairamBar();
		listenerHideView.makeDefaultHeader();
		listenerHideView.setTopBarForStyle();
	}

	private class GetStyleDetails extends AsyncTask<Void, Void, Void> {
		JSONObject jsonObj;

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
				Log.e("Style Details URL", getStyleDetailURL);
				jsonObj = JSONfunctions.getJSONfromURL(getStyleDetailURL);

			} catch (Exception e) {
			}

			return null;

		}

		@Override
		protected void onPostExecute(Void args) {
			mProgressDialog.dismiss();

			try {
				if (jsonObj.getBoolean("success")) {
					jsonObj = jsonObj.getJSONObject("style");
					txt.setText(jsonObj.getString("description"));
					backTextView.setText(jsonObj.getString("title"));
					Picasso.with(getActivity())
							.load(jsonObj.getString("image"))
							.placeholder(R.drawable.loading320).into(img);
				}
			} catch (Exception e) {
			}
		}

	}

	private String getLanguage() {
		SharedPreferences sp = getActivity().getSharedPreferences("lan",
				Context.MODE_PRIVATE);

		if (sp.getString("lan", "en").equals("en"))
			return "1";
		else
			return "2";
	}

	public void setupUI(View view, boolean isHeading) {
		Typeface myTypeface = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/HELVETICANEUELTARABIC-ROMAN.TTF");
		if (Util.getLanguage(getActivity()).equals("2")) {
			TextView myTextView = (TextView) view.findViewById(R.id.textView1);
			/*backTextView.setTypeface(myTypeface);*/
			txt.setTypeface(myTypeface);
		}
		else{
			myTypeface = Typeface.createFromAsset(getActivity()
					.getAssets(), "fonts/Georgia.ttf");
			/*backTextView.setTypeface(myTypeface);*/
			txt.setTypeface(myTypeface);
		}
	}

	/*Typeface getRegularTypeFace() {
		return Typeface.createFromAsset(getActivity().getAssets(),
				getActivity().getString(R.string.regular_typeface));
	}
*/
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		getView().setFocusableInTouchMode(true);
		getView().requestFocus();

		getView().setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						backToStylePage();
						return true;
					}
				}
				return false;
			}
		});
		super.onActivityCreated(savedInstanceState);
	}
}
