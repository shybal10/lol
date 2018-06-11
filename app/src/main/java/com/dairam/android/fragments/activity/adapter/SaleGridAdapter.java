package com.dairam.android.fragments.activity.adapter;

import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.parserfiles.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class SaleGridAdapter extends BaseAdapter{
	// Declare Variables
			Context context;
			LayoutInflater inflater;
			ArrayList<HashMap<String, String>> data;
			ImageLoader imageLoader;
			HashMap<String, String> resultp = new HashMap<String, String>();
			ArrayList<String> list = new ArrayList<String>();
			Fragment fr;
			int languagevariable = 1;/*try to read this value from the file in future*/
		/*
			list.add(textview.getText().toString());*/
			public SaleGridAdapter(Context context,
					ArrayList<HashMap<String, String>> arraylist) {
				this.context = context;
				data = arraylist;
				System.out.println("data for URL check"+data);
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

				System.out.println("Entered here... in the first layout list view ");
				
				
				
				inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				View itemView = inflater.inflate(R.layout.sale_grid_singleitem, parent, false);
				// Get the position
				resultp = data.get(position);


				// Locate the ImageView in listview_item.xml
				flag = (ImageView) itemView.findViewById(R.id.imageViewsalegrid);

				System.out.println("the imagee thing"+ AppConstants.SALE_IMAGE);
			  //  imageLoader.DisplayImage(resultp.get(AppConstants.SALE_IMAGE), flag);
				Picasso.with(context).load(resultp.get(AppConstants.SALE_IMAGE)).placeholder(R.drawable.loading320).into(flag);
				return itemView;
			}

}
