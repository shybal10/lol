package com.dairam.android.fragments.activity.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.fragments.SubcategoryFragment;
import com.dairam.android.fragments.activity.parserfiles.ImageLoader;
import com.dairam.android.fragments.activity.utillities.PrefUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class CategoryGridviewAdapter extends BaseAdapter {

	// Declare Variables
	Context context;
	LayoutInflater inflater;
	ArrayList<HashMap<String, String>> data;
	ImageLoader imageLoader;
	HashMap<String, String> resultp = new HashMap<String, String>();
	Fragment fr;

	public CategoryGridviewAdapter(Context context,
			ArrayList<HashMap<String, String>> arraylist) {
		this.context = context;
		data = arraylist;
		imageLoader = new ImageLoader(context);

		imageLoader.clearCache();
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
		ImageView flag;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		SharedPreferences sp = context.getSharedPreferences("lan",
				Context.MODE_PRIVATE);
		String lan = sp.getString("lan", "en");
		setLanguage(lan);
		View itemView = inflater.inflate(R.layout.single_item_grid, parent,
				false);

		switch (lan) {
		case "en":
			
			break;

		default:
			setupUI(itemView, false);
			break;
		}
		
		// Get the position
		resultp = data.get(position);

		// Locate the TextViews in listview_item.xml
		rank = (TextView) itemView.findViewById(R.id.grid_item_label);

		/*
		 * country = (TextView) itemView.findViewById(R.id.country); population
		 * = (TextView) itemView.findViewById(R.id.population);
		 */
		// Locate the ImageView in listview_item.xml
		flag = (ImageView) itemView.findViewById(R.id.grid_item_image);

		// Capture position and set results to the TextViews
		Typeface fontFace = Typeface.createFromAsset(context.getAssets(),
				"fonts/FoglihtenNo04-070.otf");
		rank.setTypeface(fontFace);
		rank.setText(resultp.get(AppConstants.CATEGORY_NAME));
		/* country.setText(resultp.get(GridViewActivity.TAG_NAME)); */
		// Capture position and set results to the ImageView
		// Passes flag images URL into ImageLoader.class

		try {
			Picasso.with(context)
					.load(resultp.get(AppConstants.CATEGORY_IMAGE))
					.placeholder(R.drawable.loading320).into(flag);
			// imageLoader.DisplayImage(resultp.get(AppConstants.CATEGORY_IMAGE),
			// flag);

		} catch (Exception e) {
			e.printStackTrace();
			// imageLoader.clearCache();
			// imageLoader.DisplayImage(resultp.get(AppConstants.CATEGORY_IMAGE),
			// flag);
		}

		// Capture ListView item click
		itemView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
                  PrefUtil.setCatGridcount(context, position);
                  PrefUtil.setGridcount(context, 0);
				resultp = data.get(position);
				System.out.println("Position of category " + position);
				String titleString = resultp.get(AppConstants.CATEGORY_NAME);
				String referenceUrl = resultp.get(AppConstants.CATEGORY_HREF);

				String id = resultp.get(AppConstants.CATEGORY_ID);

				String url = AppConstants.SUB_CATEGORY_URL + id;

				fr = new SubcategoryFragment();
				Bundle bundle = new Bundle();
				bundle.putString("title", titleString);
				bundle.putString("referenceurl", url);
				bundle.putString("categoryid", id);
				fr.setArguments(bundle);
				FragmentManager fm = ((Activity) context).getFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
				fragmentTransaction.replace(R.id.fragment_place, fr);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();

			}
		});
		
		switch (getLanguage()) {
		case "en":
			
			break;

		default:
			Typeface myTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/HELVETICANEUELTARABIC-ROMAN.TTF");
		    
			break;
		}
		return itemView;
	}

	private void setLanguage(String lang) {
		String languageToLoad = lang;
		Locale locale = new Locale(languageToLoad);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		context.getResources().updateConfiguration(config, null);
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
	public String getLanguage() {
		SharedPreferences sp = context.getSharedPreferences("lan",
				Context.MODE_PRIVATE);

		if (sp.getString("lan", "en").equals("en"))
			return "1";
		else
			return "2";
	}
}
