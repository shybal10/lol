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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.fragments.addNewAddressFragment;
import com.dairam.android.fragments.activity.parserfiles.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class AdressListviewAdapter extends BaseAdapter{
	// Declare Variables
		Context context;
		LayoutInflater inflater;
		ArrayList<HashMap<String, String>> data;
		ImageLoader imageLoader;
		HashMap<String, String> resultp = new HashMap<String, String>();
		ArrayList<String> list = new ArrayList<String>();
		Fragment fr;
		String lan;
		
		Button editButton;
		
		String address1,address2,address3,address4,address5,addressId;
	/*
		list.add(textview.getText().toString());*/
		public AdressListviewAdapter(Context context,ArrayList<HashMap<String, String>> arraylist) {
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
	
			TextView addTextView1,addTextView2,addTextView3,addTextView4,addTextView5;
			
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			 SharedPreferences sp=context.getSharedPreferences("lan", Context.MODE_PRIVATE);
	           lan=sp.getString("lan", "en");
	          setLanguage(lan);
			View itemView = inflater.inflate(R.layout.address_lister, parent, false);
			// Get the position
			resultp = data.get(position);
			
			addTextView1   =   (TextView)itemView.findViewById(R.id.textView1add1);
			addTextView2   =   (TextView)itemView.findViewById(R.id.textView2add2);
			addTextView3   =   (TextView)itemView.findViewById(R.id.textView3add3);
			addTextView4   =   (TextView)itemView.findViewById(R.id.textView4add4);
			addTextView5   =   (TextView)itemView.findViewById(R.id.textView5add5);
						
			editButton     =   (Button)itemView.findViewById(R.id.button1editaddress);
										
			System.out.println("The resultp"+resultp.toString());
			
            addTextView1.setText(resultp.get("ADD1"));
            addTextView2.setText(resultp.get("ADD2"));
            addTextView3.setText(resultp.get("ADD3"));//telephone
            addTextView4.setText(resultp.get("ADD4"));
            addTextView5.setText(resultp.get("ADD5"));     
            
            
            switch (lan) {
			case "en":
				
				break;

			default:				
				Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/HELVETICANEUELTARABIC-ROMAN.TTF");
				addTextView1.setTypeface(font);		
				addTextView2.setTypeface(font);	
				addTextView3.setTypeface(font);	
				addTextView4.setTypeface(font);	
				addTextView5.setTypeface(font);	
				break;
			}
            
            editButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					resultp    =   data.get(position);
					
					address1   =   resultp.get("ADD1");  
					address2   =   resultp.get("ADD2");  
					address3   =   resultp.get("ADD3");  
					address4   =   resultp.get("ADD4");  
					address5   =   resultp.get("ADD5"); 
					addressId  =   resultp.get("ADDID");
					  fr = new addNewAddressFragment();
				      Bundle bundle=new Bundle();
				      bundle.putString("address1", address1);
				      bundle.putString("address2", address2);
				      bundle.putString("address3", address3);
				      bundle.putString("address4", address4);
				      bundle.putString("address5", address5);
				      bundle.putString("addressid", addressId);
				      bundle.putString("edit", "edit");
				      fr.setArguments(bundle);
				      FragmentManager fm = ((Activity) context).getFragmentManager();
				      FragmentTransaction fragmentTransaction = fm.beginTransaction();
				      fragmentTransaction.replace(R.id.fragment_place, fr);
				      fragmentTransaction.addToBackStack(null);
				      fragmentTransaction.commit();
					
					 /*try{
						   id		=		getArguments().getString("ID");
						   url		=		getArguments().getString("urls");
						   title    =       getArguments().getString("Page");     */
				}
			});
		 	
			return itemView;
		}
		
		  private void setLanguage(String lang) { 
			   
		        String languageToLoad = lang;  
		        Locale locale = new Locale(languageToLoad);  
		        Locale.setDefault(locale);  
		        Configuration config = new Configuration();  
		        config.locale = locale;  
		        context.getResources().updateConfiguration(config, null);  
		        
		    }  
}
