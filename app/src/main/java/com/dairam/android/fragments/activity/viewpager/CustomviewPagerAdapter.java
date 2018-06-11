package com.dairam.android.fragments.activity.viewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.interfaces.OnShowProductImageDialog;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomviewPagerAdapter extends PagerAdapter {
    private Context       context;
    private List<String> imageIdList;

    private int           size;
    private boolean       isInfiniteLoop;
    OnShowProductImageDialog showProduct;
    int currentPosition;
    LayoutInflater inflater;

	public CustomviewPagerAdapter(Context context, List<String> imageIdList,
			OnShowProductImageDialog showProduct) {
		 this.context = context;
	        this.imageIdList = imageIdList;
	        this.size = imageIdList.size();
	        isInfiniteLoop = false;
	        this.showProduct = showProduct;
	        Log.e("ImagePageAdapter","In the 1st Constructor");
	}
	  public CustomviewPagerAdapter(Context context, List<String> imageIdList) {
	        this.context = context;
	        this.imageIdList = imageIdList;
	        this.size = imageIdList.size();
	        isInfiniteLoop = false;
	        
	        Log.e("ImagePageAdapter","In the 1st Constructor");
	    }
	@Override
	public int getCount() {
		  return isInfiniteLoop ? Integer.MAX_VALUE : imageIdList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((LinearLayout) object);
	}

	@Override
	public Object instantiateItem(View view, int position) {
		
		final int pos = position;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = inflater.inflate(R.layout.company_detail_imageitem, null);
		ImageView img = (ImageView) itemView.findViewById(R.id.zoom_imageview);
		if(imageIdList.get(position) != null ||!imageIdList.get(position).equals("") ||!imageIdList.get(position).equals("null"))
			Picasso.with(context).load(imageIdList.get(position)).into(img);
		
		img.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(showProduct != null){
					Log.e("ImagePageAdapter","Current Position"+pos);
					Log.e("ImagePageAdapter","Exact Position"+getPosition(currentPosition));
					showProduct.showDialog(imageIdList,pos);
				}else {
					Log.e("ImagePageAdapter","Null Interface");
				}				
			}
		});
		

		//Log.e("Zoeed ", "" + position + "..." + img.isZoomed());

		((ViewPager) view).addView(itemView);
		 return itemView;
		
		
		 /* ViewHolder holder;
	        currentPosition = position;
	        if (view == null) {
	            holder = new ViewHolder();
	            view = holder.imageView = new ImageView(context);
	            view.setTag(holder);
	            
	            holder.imageView.setOnClickListener(new OnClickListener() {				
					@Override
					public void onClick(View v) {
						if(showProduct != null){
							Log.e("ImagePageAdapter","Current Position"+currentPosition);
							Log.e("ImagePageAdapter","Exact Position"+getPosition(currentPosition));
							showProduct.showDialog(imageIdList,getPosition(currentPosition));
						}else {
							Log.e("ImagePageAdapter","Null Interface");
						}
					}
				});
	        } else {
	            holder = (ViewHolder)view.getTag();
	        }
	        
	        ImageLoader imageLoader   =  new ImageLoader(context);
	        
	        System.out.println("imageID"+imageIdList);
	        
	        
	        System.out.println("The Imahr jdd0.........."+imageIdList.get(getPosition(position)));
	        
	     
	        Picasso.with(context).load(imageIdList.get(getPosition(position))).placeholder(R.drawable.loading320).into(holder.imageView);
	       // imageLoader.DisplayImage(imageIdList.get(getPosition(position)),holder.imageView);
	     */
	       
	}

    private static class ViewHolder {

        ImageView imageView;
    }

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// Remove viewpager_item.xml from ViewPager
		((ViewPager) container).removeView((LinearLayout) object);

	}
	  private int getPosition(int position) {
	    	try{
	        return isInfiniteLoop ? position % size : position;
	    	}catch(Exception e){
	    		return 0;
	    	}
	    }
}
