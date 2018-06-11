package com.dairam.android.fragments.activity.adapter;/*//package com.dairam.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tvishi.fogaa.HomeFragment;
import com.tvishi.fogaa.R;
import com.tvishi.fogaa.Utils.GlobalConst;
import com.tvishi.fogaa.Utils.ImageLoader;
import com.tvishi.fogaa.Utils.RoundedImageView;

public class BannerImageAdapter extends PagerAdapter {

	private Activity _activity;
	// private ArrayList<String> _imagePaths;
	// private int[] _imagePaths;
	ArrayList<BannerAdsData> data;
	private LayoutInflater inflater;
	public static String phone = null;
	ImageLoader loader;

	// constructor
	
	 * public BannerImageAdapter(Activity activity, ArrayList<String>
	 * imagePaths) { this._activity = activity; this._imagePaths = imagePaths; }
	 
	// public BannerImageAdapter(Activity activity, ArrayList<BannerAdsData>
	// data) {
	public BannerImageAdapter(Activity activity) {
		this._activity = activity;
		// this._imagePaths = imagePaths;
		this.data = HomeFragment.banner_ads;
		loader = new ImageLoader(activity);
	}

	@Override
	public int getCount() {
		//Log.e("Error........", Integer.toString(data.size()));
		return data.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((ImageView) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		RoundedImageView imgDisplay;
		Button btnClose;
		
		inflater = (LayoutInflater) _activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View viewLayout = inflater.inflate(R.layout.banner, container,false);

		imgDisplay = (RoundedImageView) viewLayout
				.findViewById(R.id.banner_img);
		phone = data.get(position).phone;

		if (data.get(position).image_url.contains("/")) {
//			UrlImageViewHelper.setUrlDrawable(imgDisplay, GlobalConst.SERVER
//					+ data.get(position).image_url);
			
			Picasso.with(_activity)
			.load( GlobalConst.SERVER
					+  data.get(position).image_url)
			
			.into(imgDisplay);
			
		}
		// loader.DisplayImage(GlobalConst.SERVER
		// + data.get(position).image_url, imgDisplay);

		else {
			imgDisplay
					.setImageResource(Integer.parseInt(data.get(position).image_url));
			System.out.println("test banner ........"
					+ data.get(position).image_url);
		}

		((ViewPager) container).addView(viewLayout);
		return viewLayout;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((ImageView) object);
	}
}
*/