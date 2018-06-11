package com.dairam.android.fragments.activity.dialogfragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.utillities.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyDialogCountries extends DialogFragment{
	static final String TAG = "MyDialogCountries";
	static ArrayList<HashMap<String, String>> countryarray;
	static Context context_;
	static Util dairamUtilities;
public static MyDialogCountries newInstance(ArrayList<HashMap<String, String>> country,Context context) {
	    countryarray  =  country;
		String title = "My Fragment";
		context_ = context;
		dairamUtilities = new Util();
		MyDialogCountries f = new MyDialogCountries();
        Bundle args = new Bundle();
        args.putString("title", title);
        f.setArguments(args);
        Log.e(TAG,"Arraylist count : "+country.size());
        return f;
    }

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String title = getArguments().getString("title");
		
		 final Dialog myDialog = new Dialog(getActivity());
		       myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
               myDialog.setContentView(R.layout.layout_dialog_countries);
         
        // ImageView closeImageView   =   (ImageView)myDialog.findViewById(R.id.close_btn_dialog_social);
        ListView  listView   =   (ListView)myDialog.findViewById(R.id.listView1);
        
        
        List<String> your_array_list = new ArrayList<String>();
        System.out.println("......................."+countryarray);
        for (int i = 0; i < countryarray.size(); i++) {
        	HashMap<String, String> resultp  =  countryarray.get(i);
        	
        	your_array_list.add(resultp.get("name"));
          
		}
       /* ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                context_, 
                R.layout.spinner_single_item,
                your_array_list );*/
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                context_, 
                R.layout.spinner_single_item_country,
                your_array_list );

        listView.setAdapter(arrayAdapter); 
        
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				HashMap<String, String> countryHash = countryarray.get(position);
				Log.e("Country Dialog","Currency Code : "+countryHash.get(AppConstants.CURRENCIES_CODE));
				Util.setCurrency(getActivity(), countryHash.get(AppConstants.CURRENCIES_CODE));
				
				//Log.e("Country Dialog","Currency in Pref: "+dairamUtilities.getCurrency(getActivity()));
				myDialog.cancel(); 
			}
		});
         
        /* closeImageView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				myDialog.cancel();
			}
		});
      */
        
		myDialog.getWindow().getAttributes().windowAnimations = R.style.dialoganimation;
		myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		myDialog.getWindow().setLayout(getActivity().getResources().getDisplayMetrics().widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT);
		myDialog.getWindow().getAttributes().windowAnimations = R.style.dialoganimation;
		myDialog.show();

		
		

		return myDialog;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) 
	{
		setDialogPosition();		
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	private void setDialogPosition() 
	{
		SharedPreferences sp=getActivity().getSharedPreferences("lan", Context.MODE_PRIVATE);
        String lan=sp.getString("lan", "en");
                
        Window window = this.getDialog().getWindow();
        if(lan.equals("en"))
        	window.setGravity(Gravity.BOTTOM | Gravity.LEFT);
        else
        	window.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
 
        WindowManager.LayoutParams params = window.getAttributes();
        params.y = dpToPx(0);
        window.setAttributes(params);
    }
 
    private int dpToPx(int dp) {
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }
	
	
}
