package com.dairam.android.fragments.activity.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.MainActivity;
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.fragments.FragmentCategories;
import com.dairam.android.fragments.activity.fragments.PromoItemFragment;
import com.dairam.android.fragments.activity.fragments.SaleFragment;
import com.dairam.android.fragments.activity.interfaces.listenerHideView;
import com.dairam.android.fragments.activity.parserfiles.ImageLoaderOne;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class ListViewAdapter extends BaseAdapter {

	// Declare Variables
	Context context;
	LayoutInflater inflater;
	ArrayList<HashMap<String, String>> data;
	ImageLoaderOne imageLoader;
	HashMap<String, String> resultp = new HashMap<String, String>();
	ArrayList<String> list = new ArrayList<String>();
	Fragment fr;

	/*
	 * list.add(textview.getText().toString());
	 */
	public ListViewAdapter(Context context,
			ArrayList<HashMap<String, String>> arraylist) {
		this.context = context;
		data = arraylist;
		System.out.println("data for URL check" + data);
		imageLoader = new ImageLoaderOne(context);
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
		// Declare Variables...
		ImageView flag;

		System.out.println("Entered here... in the first layout list view ");

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View itemView = inflater.inflate(R.layout.listview_item, parent, false);
		// Get the position
		resultp = data.get(position);
		setupUI(itemView, false);

		// Locate the ImageView in listview_item.xml
		flag = (ImageView) itemView.findViewById(R.id.flag);

		System.out.println("the imagee thing"
				+ AppConstants.HOME_PAGE_TAG_IMAGE);
		// imageLoader.DisplayImage(resultp.get(AppConstants.HOME_PAGE_TAG_IMAGE),
		// flag);
		Picasso.with(context)
				.load(resultp.get(AppConstants.HOME_PAGE_TAG_IMAGE))
				.placeholder(R.drawable.loading450x150).into(flag);

		// Capture ListView item click
		itemView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				System.out.println("The Item clicked");

				System.out.println("The reference link"
						+ AppConstants.HOME_PAGE_TAG_LINK);

				resultp = data.get(position);
				String title = resultp.get(AppConstants.HOME_PAGE_TAG_TITLE);
				String referenceURL = resultp
						.get(AppConstants.HOME_PAGE_TAG_LINK);

				listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) context;
				listenerHideView.reversePromoItemHideTopbar();
				listenerHideView.hide();
				listenerHideView.hidePopup();
				listenerHideView.hideSearchPopup();
				listenerHideView.dairamBar();
				hide_keyboard((MainActivity) context);

				if (title.equals("SALE") || title.equals("الخصومات")) {
					fr = new SaleFragment();
					FragmentManager fm = ((Activity) context)
							.getFragmentManager();
					FragmentTransaction fragmentTransaction = fm
							.beginTransaction();
					fragmentTransaction.replace(R.id.fragment_place, fr);
					fragmentTransaction.addToBackStack(null);
					fragmentTransaction.commit();
				} 
				else if(title.equals("shop all categories") || title.equals("جميع الأقسام")){
					fr = new FragmentCategories();
					/*Bundle bundle = new Bundle();
					bundle.putBoolean("back", true);
					fr.setArguments(bundle);*/
					FragmentManager fm = ((Activity) context)
							.getFragmentManager();
					FragmentTransaction fragmentTransaction = fm.beginTransaction();
					fragmentTransaction.replace(R.id.fragment_place, fr);
					fragmentTransaction.addToBackStack(null);
					fragmentTransaction.commit();					
				}
				else {

					fr = new PromoItemFragment();
					Bundle bundle = new Bundle();
					bundle.putString("title", title);
					bundle.putString("referenceurl", referenceURL);
					fr.setArguments(bundle);
					FragmentManager fm = ((Activity) context)
							.getFragmentManager();
					FragmentTransaction fragmentTransaction = fm
							.beginTransaction();
					fragmentTransaction.replace(R.id.fragment_place, fr);
					fragmentTransaction.addToBackStack(null);
					fragmentTransaction.commit();
				}
			}
		});
		return itemView;
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

	public static void hide_keyboard(Activity activity) {

		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		// Find the currently focused view, so we can grab the correct window
		// token from it.
		View view = activity.getCurrentFocus();
		// If no view currently has focus, create a new one, just so we can grab
		// a window token from it
		if (view == null) {
			view = new View(activity);
		}
		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
}
