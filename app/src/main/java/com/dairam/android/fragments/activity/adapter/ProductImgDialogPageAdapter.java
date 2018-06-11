package com.dairam.android.fragments.activity.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.imgeview.ZoomableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductImgDialogPageAdapter extends  PagerAdapter{
	LayoutInflater inflater;
	List<String> imageList;
	Context context;
	//ImageView imageView;
	ZoomableImageView imageView;
	boolean isInfiniteLoop;
	int size;
	String selectedImage;
	public ProductImgDialogPageAdapter(Context context, List<String> imageList,int selectedPosition) {
		this.context = context;
		this.imageList = imageList;
		isInfiniteLoop = false;
		this.size = imageList.size();
		this.selectedImage = selectedImage;
	}

	@Override
    public int getCount() {
        // Infinite loop
        return isInfiniteLoop ? Integer.MAX_VALUE : imageList.size();
    }

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((LinearLayout) object);
	}

	@Override
	public Object instantiateItem(View view, int position) {		
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = inflater.inflate(R.layout.zoomable_image_pageradapter_singleitem, null);
		ZoomableImageView img = (ZoomableImageView) itemView.findViewById(R.id.zoom_imageview);
		Picasso.with(context).load(imageList.get(position)).into(img);
		
		
		

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

    

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// Remove viewpager_item.xml from ViewPager
		((ViewPager) container).removeView((LinearLayout) object);

	}
	public ProductImgDialogPageAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }
	
	private static class ViewHolder {

        ZoomableImageView imageView;
    }
}
