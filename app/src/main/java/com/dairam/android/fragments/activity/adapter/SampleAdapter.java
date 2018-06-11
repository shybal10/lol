package com.dairam.android.fragments.activity.adapter;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.fragments.StyleDetails;
import com.dairam.android.fragments.activity.parserfiles.ImageLoader;
import com.dairam.android.fragments.activity.utillities.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

/*
 * ADAPTER
 */

public class SampleAdapter extends BaseAdapter {

    private static final String TAG = "SampleAdapter";
    ArrayList<HashMap<String, String>> data;
    
    Context context_;
    
    ImageLoader imageLoader;
    
    Boolean click =  false;
    View oldview;
    ViewHolder vh;
    
	HashMap<String, String> resultp = new HashMap<String, String>();

    static class ViewHolder {
    	//DynamicHeightImageView txtLineOne;
    	ImageView txtLineOne;
        Button btnGo;
        TextView txt;
        LinearLayout linlay;
        TableRow tableRow;
    }

    private final LayoutInflater mLayoutInflater;
    private final Random mRandom;
  /* private final ArrayList<Integer> mBackgroundColors;*/
    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    public SampleAdapter(Context context,int textViewResourceId, ArrayList<HashMap<String, String>> arraylist) {
        super();
        
        data   =   arraylist;
        context_   =  context;
        imageLoader   =    new ImageLoader(context);
        mLayoutInflater = LayoutInflater.from(context);
        mRandom = new Random();
        /*mBackgroundColors = new ArrayList<Integer>();
        mBackgroundColors.add(R.color.orange);
        mBackgroundColors.add(R.color.green);
        mBackgroundColors.add(R.color.blue);
        mBackgroundColors.add(R.color.yellow);
        mBackgroundColors.add(R.color.grey);  */
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        
        if (convertView == null) {
        	
        	 SharedPreferences sp=context_.getSharedPreferences("lan", Context.MODE_PRIVATE);
	          String lan=sp.getString("lan", "en");
	          setLanguage(lan);
            convertView = mLayoutInflater.inflate(R.layout.list_item_sample, parent, false);
            final View old  = convertView;
            vh = new ViewHolder();
            //vh.txtLineOne = (DynamicHeightImageView) convertView.findViewById(R.id.txt_line1);
            vh.txtLineOne = (ImageView) convertView.findViewById(R.id.txt_line1);
            vh.btnGo = (Button) convertView.findViewById(R.id.buttongo);
            vh.txt    =   (TextView)convertView.findViewById(R.id.textView1descriptionstyle);
            vh.linlay =   (LinearLayout)convertView.findViewById(R.id.linearlayoutstraggered);
            vh.tableRow  =  (TableRow)convertView.findViewById(R.id.tableRowonclickview);            

            setupUI(convertView, false);
            
            vh.tableRow.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
			       
					vh.linlay.setVisibility(View.INVISIBLE);
				}
			});
            
        	vh.linlay.setVisibility(View.INVISIBLE);
            vh.txtLineOne.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					
					if (click  == false) {
						vh.linlay =   (LinearLayout)old.findViewById(R.id.linearlayoutstraggered);
						vh.btnGo = (Button) old.findViewById(R.id.buttongo);
						vh.linlay.setVisibility(View.VISIBLE);
						vh.btnGo.setVisibility(View.VISIBLE);
						
						click   =  true;
						oldview    =   old;
						 vh.btnGo.setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									
									 resultp   =   data.get(position);
							         String desc  =  resultp.get(AppConstants.STYLE_DESCRIPTION);
							         
							            
							          System.out.println("desc....."+desc);
									  Fragment  fr =  new StyleDetails();
									  Bundle bundle=new Bundle();
									  bundle.putString("title", desc);
								      bundle.putString("referenceurl", resultp.get(AppConstants.STYLE_IMAGE));
								      bundle.putString("styleID",resultp.get(AppConstants.STYLE_ID));
								      
									  fr.setArguments(bundle);
								      
									  FragmentManager fm = ((Activity) context_).getFragmentManager();
								      FragmentTransaction fragmentTransaction = fm.beginTransaction();
								      fragmentTransaction.replace(R.id.fragment_place, fr);
								      fragmentTransaction.addToBackStack(null);
								      fragmentTransaction.commit();
								}
							});
						
					}
					
					else{
						 vh.linlay =   (LinearLayout)oldview.findViewById(R.id.linearlayoutstraggered);
						  vh.btnGo = (Button) oldview.findViewById(R.id.buttongo);
						vh.linlay.setVisibility(View.INVISIBLE);
						vh.btnGo.setVisibility(View.INVISIBLE);
						
						 vh.linlay =   (LinearLayout)old.findViewById(R.id.linearlayoutstraggered);
						  vh.btnGo = (Button) old.findViewById(R.id.buttongo);
						vh.linlay.setVisibility(View.VISIBLE);
						vh.btnGo.setVisibility(View.VISIBLE);
						
						
						oldview   =   old;
						
						 vh.btnGo.setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View v) {
									
									 resultp   =   data.get(position);
							            
							            String desc  =  resultp.get(AppConstants.STYLE_DESCRIPTION);
									// TODO Auto-generated method stub
									  Fragment  fr =  new StyleDetails();
									
									  System.out.println("desc....."+desc);
									  Bundle bundle=new Bundle();
									  bundle.putString("title", desc);
									  bundle.putString("referenceurl", resultp.get(AppConstants.STYLE_IMAGE));
									  fr.setArguments(bundle);
									  FragmentManager fm = ((Activity) context_).getFragmentManager();
								      FragmentTransaction fragmentTransaction = fm.beginTransaction();
								      fragmentTransaction.replace(R.id.fragment_place, fr);
								      fragmentTransaction.addToBackStack(null);
								      fragmentTransaction.commit();
								}
							});
					}
					
				}
			});
          
            
          /*  vh.btnGo.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					System.out.println("item Clicked");
				}
			});*/
            
          
            
            resultp   =   data.get(position);
            
            //String desc  =  resultp.get(AppConstants.STYLE_DESCRIPTION);
            String desc  =  resultp.get(AppConstants.STYLE_TITLE);
            if(desc.length()>25){
            	desc  =  desc.substring(0, 25);
            	desc  =  desc+"...";
            }
            vh.txt.setText(desc);
            vh.btnGo.setVisibility(View.INVISIBLE);
        
            
            convertView.setTag(vh);
        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }

        double positionHeight = getPositionRatio(position);
   /*     int backgroundIndex = position >= mBackgroundColors.size() ?
                position % mBackgroundColors.size() : position;

        convertView.setBackgroundResource(mBackgroundColors.get(backgroundIndex));
*/
        Log.d(TAG, "getView position:" + position + " h:" + positionHeight);

        //vh.txtLineOne.setHeightRatio(positionHeight);
        
     /*   vh.txtLineOne.setText(getItem(position) + position);    */
        vh.btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
            /*    Toast.makeText(getContext(), "Button Clicked Position " +
                        position, Toast.LENGTH_SHORT).show();*/
            }
        });
        
        
      
        
        
        try{
	        resultp     =     data.get(position);
	    	//imageLoader.DisplayImage(resultp.get(AppConstants.STYLE_IMAGE), vh.txtLineOne);
	    	Picasso.with(context_).load(resultp.get(AppConstants.STYLE_IMAGE)).placeholder(R.drawable.loading320).into(vh.txtLineOne);
	    	
        }catch(Exception e){
        	System.out.println("Handled array index out of bound exception done"+e);
        }
        
        return convertView;
    }

    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
            Log.d(TAG, "getPositionRatio:" + position + " ratio:" + ratio);
        }
        return ratio;
    }

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5 the width
    }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}
	 private void setLanguage(String lang) { 
		   
	        String languageToLoad = lang;  
	        Locale locale = new Locale(languageToLoad);  
	        Locale.setDefault(locale);  
	        Configuration config = new Configuration();  
	        config.locale = locale;  
	        context_.getResources().updateConfiguration(config, null);  
	        
	    }  
		public void setupUI(View view, boolean isHeading) {

			/*if ((view instanceof TextView)) {

				((TextView) view).setTypeface(getRegularTypeFace());
			}*/
			
			if(Util.getLanguage(context_).equals("2")){
				Typeface myTypeface = Typeface.createFromAsset(context_
						.getAssets(), "fonts/HELVETICANEUELTARABIC-ROMAN.TTF");
				vh.txt.setTypeface(myTypeface);
				}

		}

		Typeface getRegularTypeFace() {
			return Typeface.createFromAsset(context_.getAssets(),
					context_.getString(R.string.regular_typeface));
		}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
}