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

public class SubCategorySingleItemAdapter extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	ArrayList<HashMap<String, String>> data;
	ImageLoader imageLoader;
	HashMap<String, String> resultp = new HashMap<String, String>();
	TextView productNameTextView;
	TextView offerPriceTextView;
	TextView originalPriceTextView;
	TextView outOfStockTextView;

	TableRow onClickTableRow;
	Button addtoCartButton;
	Button detailsButton;

	public SubCategorySingleItemAdapter(Context context,
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

		System.out.println("I am in right adapter.........");
		ImageView flag;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View itemView = inflater.inflate(R.layout.activity_home_subitem,
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
		outOfStockTextView = (TextView) itemView
				.findViewById(R.id.textView_out);
		outOfStockTextView.setVisibility(View.INVISIBLE);

		Typeface fontFace = Typeface.createFromAsset(context.getAssets(),
				"fonts/Georgia.ttf");
		originalPriceTextView.setTypeface(fontFace);
		offerPriceTextView.setTypeface(fontFace);

		//originalPriceTextView.setVisibility(View.INVISIBLE);
		addtoCartButton.setVisibility(View.INVISIBLE);
		detailsButton.setVisibility(View.INVISIBLE);
		onClickTableRow.setVisibility(View.INVISIBLE);
		onClickTableRow.setBackgroundResource(android.R.color.transparent);

		// Locate the ImageView in listview_item.xml
		flag = (ImageView) itemView.findViewById(R.id.imageViewproductimag);
		if(resultp.get(AppConstants.SALES_SINGLE_SPECIAL).toString().equals("false"))
		{
			offerPriceTextView.setText(resultp
					.get(AppConstants.SUB_CATEGORY1_PRICE));
			originalPriceTextView.setVisibility(View.INVISIBLE);
		}
		else{
			offerPriceTextView.setText(resultp
					.get(AppConstants.SUB_CATEGORY1_SPECIAL));
			originalPriceTextView.setVisibility(View.VISIBLE);
			originalPriceTextView.setText(resultp.get(AppConstants.SUB_CATEGORY1_PRICE));
			originalPriceTextView.setPaintFlags(originalPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		}
		
		productNameTextView.setText(resultp
				.get(AppConstants.SUB_CATEGORY1_NAME));

		/*
		 * System.out.println("the offer price"+resultp.get(AppConstants.
		 * HOME_SUB_SPECIAL));
		 * System.out.println("The original price"+resultp.get
		 * (AppConstants.HOME_SUB_PRICE));
		 * System.out.println("The item name"+resultp
		 * .get(AppConstants.HOME_SUB_NAME));
		 */
		// Capture position and set results to the TextViews
		/* rank.setText(resultp.get(AppConstants.HOME_SUB_ID)); */

		/* country.setText(resultp.get(GridViewActivity.TAG_NAME)); */
		// Capture position and set results to the ImageView
		// Passes flag images URL into ImageLoader.class
		// imageLoader.DisplayImage(resultp.get(AppConstants.SUB_CATEGORY1_IMAGE),
		// flag);
		Picasso.with(context)
				.load(resultp.get(AppConstants.SUB_CATEGORY1_IMAGE))
				.placeholder(R.drawable.loading320).into(flag);
		// Capture ListView item click
		/*
		 * itemView.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { // Get the position
		 * resultp = data.get(position); Intent intent = new Intent(context,
		 * SingleItemView.class); // Pass all data rank intent.putExtra("title",
		 * resultp.get(MainActivity.TAG_ID)); // Pass all data country
		 * intent.putExtra("href", resultp.get(MainActivity.TAG_NAME)); // Pass
		 * all data population // Pass all data flag intent.putExtra("image",
		 * resultp.get(MainActivity.TAG_IMAGE)); // Start SingleItemView Class
		 * context.startActivity(intent); } });
		 */

		if ((resultp.get(AppConstants.HOME_SUB_OUT_OF_STOCK_FLAG).equals("0"))) {
			outOfStockTextView.setVisibility(View.VISIBLE);
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
			outOfStockTextView.setTypeface(myTypeface);
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
