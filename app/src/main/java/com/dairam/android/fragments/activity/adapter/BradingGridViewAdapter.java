package com.dairam.android.fragments.activity.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.fragments.BrandListingSingleFragment;
import com.dairam.android.fragments.activity.parserfiles.ImageLoader;
import com.dairam.android.fragments.activity.utillities.PrefUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class BradingGridViewAdapter extends BaseAdapter{
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
		public BradingGridViewAdapter(Context context,
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

			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View itemView = inflater.inflate(R.layout.brand_single_item, parent, false);
			// Get the position
			resultp = data.get(position);


			// Locate the ImageView in listview_item.xml
			flag = (ImageView) itemView.findViewById(R.id.imageViewbrandsingleitem);

			//Log.e("Branding GridAdapter","Position "+position +"  ImgUrl : "+resultp.get(AppConstants.BRANDLISTING_IMAGE));
			//imageLoader.DisplayImage(resultp.get(AppConstants.BRANDLISTING_IMAGE), flag);
			try {
				if (resultp.get(AppConstants.BRANDLISTING_IMAGE) != null)
					Picasso.with(context).load(resultp.get(AppConstants.BRANDLISTING_IMAGE)).placeholder(R.drawable.loading320).into(flag);

			}
			catch (Exception xx)
			{

			}
			// Capture ListView item click
			itemView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

					PrefUtil.setBrandGridcount(context, position);

				resultp   =   data.get(position);

				String referenceUrl   = resultp.get(AppConstants.BRANDLISTING_HREF);
				Log.e("Brand Detail url",referenceUrl);
				String brandID = resultp.get(AppConstants.BRANDLISTING_ID);
				System.out.println("The reference Url......"+referenceUrl);

				referenceUrl  =  referenceUrl+"&id="+brandID+"&language=";

				System.out.println("the final url is "+referenceUrl);


			/*	"&id=11&language=1"*/
				System.out.println("The Item clicked");

					System.out.println("The reference link"+AppConstants.HOME_PAGE_TAG_LINK);

					resultp        		=   data.get(position);
					String title  		=   resultp.get(AppConstants.BRAND_SINGLE_NAME);
					/*String referenceURL =	resultp.get(referenceUrl);*/
					  fr = new BrandListingSingleFragment();
				      Bundle bundle=new Bundle();
				      bundle.putString("title", title);
				      bundle.putString("referenceurl", referenceUrl);

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

}
