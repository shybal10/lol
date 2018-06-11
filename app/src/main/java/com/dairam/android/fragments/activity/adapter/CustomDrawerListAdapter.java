package com.dairam.android.fragments.activity.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.utillities.Util;

import java.util.Locale;

public class CustomDrawerListAdapter extends ArrayAdapter<String>{
	private final Activity context;
	private final String[] web;
	private final Integer[] imageId;
	public CustomDrawerListAdapter(Activity context,
	String[] web, Integer[] imageId) {
	super(context, R.layout.drawer_list_item, web);
	this.context = context;
	this.web = web;
	this.imageId = imageId;

	}
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		TextView txtTitle ;
	LayoutInflater inflater = context.getLayoutInflater();
    SharedPreferences sp=context.getSharedPreferences("lan", Context.MODE_PRIVATE);
    String lan=sp.getString("lan", "en");
    setLanguage(lan);
	View rowView= inflater.inflate(R.layout.drawer_list_item, null, true);
	
	txtTitle = (TextView) rowView.findViewById(R.id.text1drawer);
	ImageView imageView = (ImageView) rowView.findViewById(R.id.imageViewdrawer);
	txtTitle.setText(web[position]);
	imageView.setImageResource(imageId[position]);
	
	switch (Util.getLanguage(context)) {
	case "1":
		/*Typeface myTypeface1 = Typeface.createFromAsset(context.getAssets(), "fonts/Georgia.ttf");
	    
		txtTitle.setTypeface(myTypeface1);*/
		break;

	default:
		
		Typeface myTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/HELVETICANEUELTARABIC-ROMAN.TTF");
	    
		txtTitle.setTypeface(myTypeface);
		break;
	}

	return rowView;
	}
	
	

	Typeface getRegularTypeFace() {
		return Typeface.createFromAsset(context.getAssets(),
				context.getString(R.string.regular_typeface));
	}
	public String getLanguage() {
		SharedPreferences sp = context.getSharedPreferences("lan",
				Context.MODE_PRIVATE);

		if (sp.getString("lan", "en").equals("en"))
			return "1";
		else
			return "2";
	}


  private void setLanguage(String lang) {  
	        String languageToLoad = lang;  
	        Locale locale = new Locale(languageToLoad);  
	        Locale.setDefault(locale);  
	        Configuration config = new Configuration();  
	        config.locale = locale;  
	        getContext().getResources().updateConfiguration(config, null);  
	    }  
	}