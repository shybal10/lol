package com.dairam.android.fragments.activity.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.interfaces.OnShowProductImageDialog;
import com.dairam.android.fragments.activity.parserfiles.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImagePagerAdapter extends RecyclingPagerAdapter {

    private Context       context;
    private List<String> imageIdList;

    private int           size;
    private boolean       isInfiniteLoop;
    OnShowProductImageDialog showProduct;
    int currentPosition;

    public ImagePagerAdapter(Context context, List<String> imageIdList) {
        this.context = context;
        this.imageIdList = imageIdList;
        this.size = imageIdList.size();
        isInfiniteLoop = false;
        
        Log.e("ImagePageAdapter","In the 1st Constructor");
    }
    public ImagePagerAdapter(Context context, List<String> imageIdList,OnShowProductImageDialog showProduct) {
        this.context = context;
        this.imageIdList = imageIdList;
        this.size = imageIdList.size();
        this.showProduct = showProduct;
        isInfiniteLoop = false;  
        
        Log.e("ImagePageAdapter","In the 2nd Constructor");
    }

    @Override
    public int getCount() {
        // Infinite loop
        return isInfiniteLoop ? Integer.MAX_VALUE : imageIdList.size();
    }

    /**
     * get really position
     * 
     * @param position
     * @return
     */
    private int getPosition(int position) {
    	try{
        return isInfiniteLoop ? position % size : position;
    	}catch(Exception e){
    		return 0;
    	}
    }

    @Override
    public View getView(int position, View view, ViewGroup container) {
        ViewHolder holder;
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
     
        return view;
    }

    private static class ViewHolder {

        ImageView imageView;
    }

    /**
     * @return the isInfiniteLoop
     */
    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    /**
     * @param isInfiniteLoop the isInfiniteLoop to set
     */
    public ImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }

}
