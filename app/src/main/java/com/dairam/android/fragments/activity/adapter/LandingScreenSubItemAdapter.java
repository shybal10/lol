package com.dairam.android.fragments.activity.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.parserfiles.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class LandingScreenSubItemAdapter extends BaseAdapter {
	// Declare Variables
	Context context;
	LayoutInflater inflater;
	ArrayList<HashMap<String, String>> data;
	ImageLoader imageLoader;
	HashMap<String, String> resultp = new HashMap<String, String>();
	TextView productNameTextView;
	TextView offerPriceTextView;
	TextView originalPriceTextView;
	TableRow onClickTableRow;
	TextView out_of_stock_label;

	Button addtoCartButton;
	Button detailsButton;

	public LandingScreenSubItemAdapter(Context context,
			ArrayList<HashMap<String, String>> arraylist) {
		this.context = context;
		data = arraylist;
		imageLoader = new ImageLoader(context);
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
		ImageView flag;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		final View itemView = inflater.inflate(R.layout.activity_home_subitem,
				parent, false);

		setupUI(itemView, false);
		// Get the position
		resultp = data.get(position);

		offerPriceTextView = (TextView) itemView
				.findViewById(R.id.textViewofferprice);
		originalPriceTextView = (TextView) itemView
				.findViewById(R.id.textVieworgprice);
		productNameTextView = (TextView) itemView
				.findViewById(R.id.textViewproductname);
		productNameTextView.setSelected(true);
		onClickTableRow = (TableRow) itemView
				.findViewById(R.id.tableRowonclickview);
		addtoCartButton = (Button) itemView.findViewById(R.id.buttoncart);
		detailsButton = (Button) itemView.findViewById(R.id.buttondetails);
		out_of_stock_label = (TextView) itemView
				.findViewById(R.id.textView_out);
		out_of_stock_label.setVisibility(View.INVISIBLE);

		Typeface fontFace = Typeface.createFromAsset(context.getAssets(),
				"fonts/Georgia.ttf");
		originalPriceTextView.setTypeface(fontFace);
		offerPriceTextView.setTypeface(fontFace, Typeface.BOLD);

		addtoCartButton.setVisibility(View.INVISIBLE);
		detailsButton.setVisibility(View.INVISIBLE);
		onClickTableRow.setVisibility(View.INVISIBLE);
		onClickTableRow.setBackgroundResource(android.R.color.transparent);

		// Locate the ImageView in listview_item.xml
		flag = (ImageView) itemView.findViewById(R.id.imageViewproductimag);

		offerPriceTextView.setText(resultp.get(AppConstants.HOME_SUB_PRICE));
		/*
		 * originalPriceTextView.setText(resultp.get(AppConstants.HOME_SUB_SPECIAL
		 * ));
		 */
		productNameTextView.setText(resultp.get(AppConstants.HOME_SUB_NAME));

		System.out.println("the offer price"
				+ resultp.get(AppConstants.HOME_SUB_SPECIAL));
		System.out.println("The original price"
				+ resultp.get(AppConstants.HOME_SUB_PRICE));
		System.out.println("The item name"
				+ resultp.get(AppConstants.HOME_SUB_NAME));

		if (resultp.get(AppConstants.HOME_SUB_SPECIAL).equals("false")) {
			System.out
					.println("Print me.............................................");
			originalPriceTextView.setVisibility(View.INVISIBLE);
		} else {
			originalPriceTextView.setVisibility(View.VISIBLE);
			originalPriceTextView.setText(resultp
					.get(AppConstants.HOME_SUB_SPECIAL));
			originalPriceTextView.setPaintFlags(originalPriceTextView
					.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		}

		// Capture position and set results to the ImageView
		// Passes flag images URL into ImageLoader.class
		// imageLoader.DisplayImage(resultp.get(AppConstants.HOME_SUB_IMAGE),
		// flag);
		Picasso.with(context).load(resultp.get(AppConstants.HOME_SUB_IMAGE))
				.placeholder(R.drawable.loading320).into(flag);

		if (resultp.get(AppConstants.HOME_SUB_OUT_OF_STOCK_FLAG).equals("0")) {
			out_of_stock_label.setVisibility(View.VISIBLE);
		}
		switch (getLanguage()) {
		case "en":

			break;

		default:

			Typeface myTypeface = Typeface.createFromAsset(context.getAssets(),
					"fonts/HELVETICANEUELTARABIC-ROMAN.TTF");

			offerPriceTextView.setTypeface(myTypeface);
			originalPriceTextView.setTypeface(myTypeface);
			// productNameTextView.setTypeface(myTypeface);
			out_of_stock_label.setTypeface(myTypeface);

			break;
		}

		return itemView;
	}

	public String getLanguage() {
		SharedPreferences sp = context.getSharedPreferences("lan",
				Context.MODE_PRIVATE);

		if (sp.getString("lan", "en").equals("en"))
			return "1";
		else
			return "2";
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
