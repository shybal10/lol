package com.dairam.android.fragments.activity.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.parserfiles.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewPagerAdapter extends PagerAdapter {
	// Declare Variables
	Context context;
	String[] rank;
	String[] country;
	String[] population;
	int[] flag;
	LayoutInflater inflater;
	int flaglength;
	int start;
	int stop;

	ArrayList<HashMap<String, String>> data;
	HashMap<String, String> resultp = new HashMap<String, String>();

	Boolean firstEndreach = false;
	ImageLoader imageLoader;
	OnProductClicked listener;
	

	public interface OnProductClicked {
		void onClicked(String id);
	}

	public ViewPagerAdapter(Context context,
			ArrayList<HashMap<String, String>> arraylist,
			OnProductClicked listener2) {

		this.context = context;
		data = arraylist;
		imageLoader = new ImageLoader(context);
		this.listener = listener2;

	}

	@Override
	public int getCount() {

		if (data.size() / 3 < 1) {
			return 1;
		} else {
			return data.size() / 3;
		}

	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((RelativeLayout) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {

		ImageView imgflag;
		ImageView imageView2;
		ImageView imageView3;
		resultp = data.get(position);
		
		Log.e("length of data", data.size()+"");
		
		
		final RelativeLayout relay1;
		final RelativeLayout relay2;
		final RelativeLayout relay3;

		final Button button1;
		final Button button2;
		final Button button3;

		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = inflater.inflate(R.layout.viewpager_item, container,false);

		relay1 = (RelativeLayout) itemView.findViewById(R.id.rellay1viewpager);
		relay2 = (RelativeLayout) itemView.findViewById(R.id.rellay2viewpager);
		relay3 = (RelativeLayout) itemView.findViewById(R.id.rellay3viewpager);

		button1 = (Button) itemView.findViewById(R.id.detail1viewpager);
		button2 = (Button) itemView.findViewById(R.id.detail2viewpager);
		button3 = (Button) itemView.findViewById(R.id.detail3viewpager);

		relay1.setVisibility(View.INVISIBLE);
		relay2.setVisibility(View.INVISIBLE);
		relay3.setVisibility(View.INVISIBLE);

		button1.setVisibility(View.INVISIBLE);
		button2.setVisibility(View.INVISIBLE);
		button3.setVisibility(View.INVISIBLE);
		
		Log.e("Arraylist Data in PagerAdapter",data.toString());

		relay1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) 
			{
				if(relay1.getVisibility()== View.VISIBLE)
				{
					relay1.setVisibility(View.INVISIBLE);
					button1.setVisibility(View.INVISIBLE);
				}
				else
				{
					relay1.setVisibility(View.VISIBLE);
					button1.setVisibility(View.VISIBLE);
				}

			}
		});
		
		relay2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) 
			{
				if(relay2.getVisibility()== View.VISIBLE)
				{
					relay2.setVisibility(View.INVISIBLE);
					button2.setVisibility(View.INVISIBLE);
				}
				else
				{
					relay2.setVisibility(View.VISIBLE);
					button2.setVisibility(View.VISIBLE);
				}
			}
		});
		relay3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) 
			{
				if(relay3.getVisibility()== View.VISIBLE)
				{
					relay3.setVisibility(View.INVISIBLE);
					button3.setVisibility(View.INVISIBLE);
				}
				else
				{
					relay3.setVisibility(View.VISIBLE);
					button3.setVisibility(View.VISIBLE);
				}
			}
		});

		button1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("The resultp " + resultp.get(AppConstants.SUB_CATEGORY1_ID));
			}
		});

		// Locate the ImageView in viewpager_item.xml
		imgflag    = (ImageView) itemView.findViewById(R.id.flag1);
		imageView2 = (ImageView) itemView.findViewById(R.id.flag2);
		imageView3 = (ImageView) itemView.findViewById(R.id.flag3);
		
	         if (data.size() == 1) {
				imageView2.setVisibility(View.INVISIBLE);
				imageView3.setVisibility(View.INVISIBLE);
			}
	         if (data.size() == 2) {
	 			imageView3.setVisibility(View.INVISIBLE);
	 		}

		imgflag.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (position == 0) {
					
					Log.e("Arraylist data in ViewPagerAdapter",data.toString());
					resultp = data.get(position);
					System.out.println("AppConstants.SUB_CATEGORY1_ID"
					        + resultp.get(AppConstants.SUB_CATEGORY1_ID));
					System.out.println("AppConstants.SUB_CATEGORY1_NAME"
							+ AppConstants.SUB_CATEGORY1_NAME);
					System.out.println("Imageview Clicked");
					
					relay1.setVisibility(View.VISIBLE);
					button1.setVisibility(View.VISIBLE);
					
					relay2.setVisibility(View.INVISIBLE);
					button2.setVisibility(View.INVISIBLE);
					relay3.setVisibility(View.INVISIBLE);
					button3.setVisibility(View.INVISIBLE);
				}

				else {
					int dummy_int = position * 3;
					
					resultp = data.get(position + dummy_int - 1);
					System.out.println("AppConstants.SUB_CATEGORY1_ID"
							+ resultp.get(AppConstants.SUB_CATEGORY1_ID));
					System.out.println("AppConstants.SUB_CATEGORY1_NAME"
							+ AppConstants.SUB_CATEGORY1_NAME);
					System.out.println("Imageview Clicked");
					
					relay1.setVisibility(View.VISIBLE);
					button1.setVisibility(View.VISIBLE);
					
					relay2.setVisibility(View.INVISIBLE);
					button2.setVisibility(View.INVISIBLE);
					relay3.setVisibility(View.INVISIBLE);
					button3.setVisibility(View.INVISIBLE);
				}

			}
		});
		imageView2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				try{

				if (position == 0) {
					resultp = data.get(position + 1);
					System.out.println("AppConstants.SUB_CATEGORY1_ID"
							+ resultp.get(AppConstants.SUB_CATEGORY1_ID));
					System.out.println("AppConstants.SUB_CATEGORY1_NAME"
							+ AppConstants.SUB_CATEGORY1_NAME);
					System.out.println("Imageview Clicked");
					relay2.setVisibility(View.VISIBLE);
					button2.setVisibility(View.VISIBLE);
					
					relay1.setVisibility(View.INVISIBLE);
					button1.setVisibility(View.INVISIBLE);
					relay3.setVisibility(View.INVISIBLE);
					button3.setVisibility(View.INVISIBLE);
					
					
				} else {
					int dummy_int = position * 3;
					resultp = data.get(position + dummy_int);

					System.out.println("AppConstants.SUB_CATEGORY1_ID"
							+ resultp.get(AppConstants.SUB_CATEGORY1_ID));
					System.out.println("AppConstants.SUB_CATEGORY1_NAME"
							+ AppConstants.SUB_CATEGORY1_NAME);
					System.out.println("Imageview Clicked");
					relay2.setVisibility(View.VISIBLE);
					button2.setVisibility(View.VISIBLE);
					
					relay1.setVisibility(View.INVISIBLE);
					button1.setVisibility(View.INVISIBLE);
					relay3.setVisibility(View.INVISIBLE);
					button3.setVisibility(View.INVISIBLE);
				}
				}catch(Exception e){
					System.out.println("");
				}
			}
		});

		imageView3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
          try{
				if (position == 0) {
					resultp = data.get(position + 2);
					System.out.println("AppConstants.SUB_CATEGORY1_ID"
							+ resultp.get(AppConstants.SUB_CATEGORY1_ID));
					System.out.println("AppConstants.SUB_CATEGORY1_NAME"
							+ AppConstants.SUB_CATEGORY1_NAME);
					System.out.println("Imageview Clicked");
					relay3.setVisibility(View.VISIBLE);
					button3.setVisibility(View.VISIBLE);
					
					relay1.setVisibility(View.INVISIBLE);
					button1.setVisibility(View.INVISIBLE);
					relay2.setVisibility(View.INVISIBLE);
					button2.setVisibility(View.INVISIBLE);
				}

				else {
					int dummy_int = position * 3;
					resultp = data.get(position + dummy_int + 1);
					System.out.println("AppConstants.SUB_CATEGORY1_ID"
							+ resultp.get(AppConstants.SUB_CATEGORY1_ID));
					System.out.println("AppConstants.SUB_CATEGORY1_NAME"
							+ AppConstants.SUB_CATEGORY1_NAME);
					System.out.println("Imageview Clicked");
					relay3.setVisibility(View.VISIBLE);
					button3.setVisibility(View.VISIBLE);
					
					relay1.setVisibility(View.INVISIBLE);
					button1.setVisibility(View.INVISIBLE);
					relay2.setVisibility(View.INVISIBLE);
					button2.setVisibility(View.INVISIBLE);
				}
		}catch(Exception e){
			
		}

			}
		});
		// Button Action for viewing details
		button1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (position == 0) {
					resultp = data.get(position);
					System.out.println("AppConstants.SUB_CATEGORY1_ID"
							+ resultp.get(AppConstants.SUB_CATEGORY1_ID));
					System.out.println("AppConstants.SUB_CATEGORY1_NAME"
							+ AppConstants.SUB_CATEGORY1_NAME);

				}

				else {
					int dummy_int = position * 3;
					resultp = data.get(position + dummy_int - 1);
					System.out.println("AppConstants.SUB_CATEGORY1_ID"
							+ resultp.get(AppConstants.SUB_CATEGORY1_ID));
					System.out.println("AppConstants.SUB_CATEGORY1_NAME"
							+ AppConstants.SUB_CATEGORY1_NAME);
					// Action goes here
				}
				listener.onClicked(resultp.get(AppConstants.SUB_CATEGORY1_ID));
			}
		});

		button2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (position == 0) {
					resultp = data.get(position + 1);
					System.out.println("AppConstants.SUB_CATEGORY1_ID"
							+ resultp.get(AppConstants.SUB_CATEGORY1_ID));
					System.out.println("AppConstants.SUB_CATEGORY1_NAME"
							+ AppConstants.SUB_CATEGORY1_NAME);
					

				}

				else {
					int dummy_int = position * 3;
					resultp = data.get(position + dummy_int);
					System.out.println("AppConstants.SUB_CATEGORY1_ID"
							+ resultp.get(AppConstants.SUB_CATEGORY1_ID));
					System.out.println("AppConstants.SUB_CATEGORY1_NAME"
							+ AppConstants.SUB_CATEGORY1_NAME);
					// Action goes here
				}
				listener.onClicked(resultp.get(AppConstants.SUB_CATEGORY1_ID));
			}
		});
		button3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (position == 0) {
					resultp = data.get(position + 2);
					System.out.println("AppConstants.SUB_CATEGORY1_ID"
							+ resultp.get(AppConstants.SUB_CATEGORY1_ID));
					System.out.println("AppConstants.SUB_CATEGORY1_NAME"
							+ AppConstants.SUB_CATEGORY1_NAME);

				}

				else {
					int dummy_int = position * 3;
					resultp = data.get(position + dummy_int + 1);
					System.out.println("AppConstants.SUB_CATEGORY1_ID"
							+ resultp.get(AppConstants.SUB_CATEGORY1_ID));
					System.out.println("AppConstants.SUB_CATEGORY1_NAME"
							+ AppConstants.SUB_CATEGORY1_NAME);
					// Action goes here
				}
				listener.onClicked(resultp.get(AppConstants.SUB_CATEGORY1_ID));
			}
		});

		try {

			if (position == 0) {

				resultp = data.get(position);

				Log.e("Url........",
						resultp.get(AppConstants.SUB_CATEGORY1_IMAGE));
				Log.e("Url........", resultp.get(AppConstants.SUB_CATEGORY1_ID));
				// imageLoader.DisplayImage(resultp.get(AppConstants.SUB_CATEGORY1_IMAGE),
				// imgflag);
				Picasso.with(context)
						.load(resultp.get(AppConstants.SUB_CATEGORY1_IMAGE))
						.placeholder(R.drawable.loading320).into(imgflag);

				resultp = data.get(position + 1);
				// imageLoader.DisplayImage(resultp.get(AppConstants.SUB_CATEGORY1_IMAGE),
				// imageView2);
				Picasso.with(context)
						.load(resultp.get(AppConstants.SUB_CATEGORY1_IMAGE))
						.placeholder(R.drawable.loading320).into(imageView2);

				Log.e("Url........",
						resultp.get(AppConstants.SUB_CATEGORY1_IMAGE));
				Log.e("Url........", resultp.get(AppConstants.SUB_CATEGORY1_ID));

				resultp = data.get(position + 2);
				// imageLoader.DisplayImage(resultp.get(AppConstants.SUB_CATEGORY1_IMAGE),
				// imageView3);
				Picasso.with(context)
						.load(resultp.get(AppConstants.SUB_CATEGORY1_IMAGE))
						.placeholder(R.drawable.loading320).into(imageView3);

				Log.e("Url........",
						resultp.get(AppConstants.SUB_CATEGORY1_IMAGE));
				Log.e("Url........", resultp.get(AppConstants.SUB_CATEGORY1_ID));
			} else {

				int dummy_int = position * 3;

				resultp = data.get(position + dummy_int - 1);

				Log.e("Url........",
						resultp.get(AppConstants.SUB_CATEGORY1_IMAGE));
				Log.e("Url........", resultp.get(AppConstants.SUB_CATEGORY1_ID));
				// imageLoader.DisplayImage(resultp.get(AppConstants.SUB_CATEGORY1_IMAGE),
				// imgflag);
				Picasso.with(context)
						.load(resultp.get(AppConstants.SUB_CATEGORY1_IMAGE))
						.placeholder(R.drawable.loading320).into(imgflag);

				resultp = data.get(position + dummy_int);
				// imageLoader.DisplayImage(resultp.get(AppConstants.SUB_CATEGORY1_IMAGE),
				// imageView2);
				Picasso.with(context)
						.load(resultp.get(AppConstants.SUB_CATEGORY1_IMAGE))
						.placeholder(R.drawable.loading320).into(imageView2);

				Log.e("Url........",
						resultp.get(AppConstants.SUB_CATEGORY1_IMAGE));
				Log.e("Url........", resultp.get(AppConstants.SUB_CATEGORY1_ID));

				resultp = data.get(position + dummy_int + 1);
				// imageLoader.DisplayImage(resultp.get(AppConstants.SUB_CATEGORY1_IMAGE),
				// imageView3);
				Picasso.with(context)
						.load(resultp.get(AppConstants.SUB_CATEGORY1_IMAGE))
						.placeholder(R.drawable.loading320).into(imageView3);

				Log.e("Url........",
						resultp.get(AppConstants.SUB_CATEGORY1_IMAGE));
				Log.e("Url........", resultp.get(AppConstants.SUB_CATEGORY1_ID));
			}
		} catch (Exception e) {
			System.out.println("ArrayIndexOutOfBoundExceptionCaught");
		}

		// Add viewpager_item.xml to ViewPager
		((ViewPager) container).addView(itemView);
		
		return itemView;

	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// Remove viewpager_item.xml from ViewPager
		((ViewPager) container).removeView((RelativeLayout) object);

	}
}
