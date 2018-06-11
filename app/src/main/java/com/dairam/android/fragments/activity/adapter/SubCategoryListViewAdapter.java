package com.dairam.android.fragments.activity.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.fragments.CategoryProductsFragment;
import com.dairam.android.fragments.activity.parserfiles.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

public class SubCategoryListViewAdapter extends BaseAdapter {

	// Declare Variables
	Context context;
	LayoutInflater inflater;
	ArrayList<HashMap<String, String>> data;
	ImageLoader imageLoader;
	HashMap<String, String> resultp = new HashMap<String, String>();
	ArrayList<String> list = new ArrayList<String>();
	Fragment fr;
	String backURL, backtitle;

	/*
	 * list.add(textview.getText().toString());
	 */
	public SubCategoryListViewAdapter(Context context,
			ArrayList<HashMap<String, String>> arraylist, String backurl,
			String title) {
		this.context = context;
		data = arraylist;
		System.out.println("data for URL check" + data);
		imageLoader = new ImageLoader(context);

		backURL = backurl;
		backtitle = title;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		// Declare Variables
		TextView rank;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View itemView = inflater.inflate(R.layout.subcategory_single_item,
				parent, false);

		setupUI(itemView, false);
		// Get the position
		resultp = data.get(position);

		// Locate the ImageView in listview_item.xml
		rank = (TextView) itemView.findViewById(R.id.textViewsubitem);

		rank.setText(resultp.get(AppConstants.SUB_CATEGORY_NAME));
		switch (getLanguage()) {
		case "en":

			break;

		default:
			Typeface myTypeface = Typeface.createFromAsset(context.getAssets(),
					"fonts/HELVETICANEUELTARABIC-ROMAN.TTF");

			rank.setTypeface(myTypeface);
			break;
		}
		System.out.println("the imagee thing"
				+ AppConstants.HOME_PAGE_TAG_IMAGE);
		/*
		 * imageLoader.DisplayImage(resultp.get(AppConstants.HOME_PAGE_TAG_IMAGE)
		 * , flag);
		 */

		// Capture ListView item click
		itemView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				resultp = data.get(position);
				String titleString = resultp
						.get(AppConstants.SUB_CATEGORY_NAME);
				String referenceUrl = resultp
						.get(AppConstants.SUB_CATEGORY_HREF);
				String category_id = resultp.get(AppConstants.SUB_CATEGORY_ID);

				Log.e("Category ID", category_id);

				// referenceUrl = referenceUrl+"&category="+category_id;

				/*
				 * SharedPreferences sp = context.getSharedPreferences("lan",
				 * Context.MODE_PRIVATE); String lan=sp.getString("lan", "en");
				 * System.out.println("lan "+lan);
				 * Log.d("language in SubCatListViewAdptr", lan); switch (lan) {
				 * case "en": referenceUrl = referenceUrl
				 * +"&language=1&currency=KWD"; break;
				 * 
				 * 
				 * default: referenceUrl = referenceUrl
				 * +"&language=2&currency=KWD"; break; }
				 */

				System.out.println("The reference URL subcategory "
						+ referenceUrl);
				Log.e("Reference URL from SubCategoryListViewAdapter",
						referenceUrl);

				fr = new CategoryProductsFragment();
				Bundle bundle = new Bundle();
				bundle.putString("title", titleString);
				bundle.putString("referenceurl", referenceUrl);
				bundle.putString("firstlevel", backURL);
				bundle.putString("firstleveltitle", backtitle);
				bundle.putString("categoryid", category_id);
				fr.setArguments(bundle);
				FragmentManager fm = ((Activity) context).getFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
				fragmentTransaction.replace(R.id.fragment_place, fr);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();

			}
		});
		return itemView;
	}

	public void setupUI(View view, boolean isHeading) {

		if ((view instanceof TextView)) {

			((TextView) view).setTypeface(getRegularTypeFace());
		}

	}

	public String getLanguage() {
		SharedPreferences sp = context.getSharedPreferences("lan",
				Context.MODE_PRIVATE);

		if (sp.getString("lan", "en").equals("en"))
			return "1";
		else
			return "2";
	}

	Typeface getRegularTypeFace() {
		return Typeface.createFromAsset(context.getAssets(),
				context.getString(R.string.regular_typeface));
	}
}
