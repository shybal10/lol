package com.dairam.android.fragments.activity.dialogfragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.MainActivity;

public class MyDialogFragmentSocialmedia extends DialogFragment{
public static MyDialogFragmentSocialmedia newInstance() {
		
		String title = "My Fragment";
		
		MyDialogFragmentSocialmedia f = new MyDialogFragmentSocialmedia();
        Bundle args = new Bundle();
        args.putString("title", title);
        f.setArguments(args);
        return f;
    }

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String title = getArguments().getString("title");
		
		 final Dialog myDialog = new Dialog(getActivity());
		       myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
               myDialog.setContentView(R.layout.dialog_layout_social);
         
         //ImageView closeImageView   =   (ImageView)myDialog.findViewById(R.id.close_btn_dialog_socialshare);
         Button facebookButton      =   (Button)myDialog.findViewById(R.id.facebookbtnsocial1);
         Button twitterButton       =   (Button)myDialog.findViewById(R.id.twitterbtnsocial1);
         Button instagramButton     =   (Button)myDialog.findViewById(R.id.instagrambtnsocial1);
         Button pinterestButton     =   (Button)myDialog.findViewById(R.id.pinterestbtnsocial1);
         
         TextView facebookText		= 	(TextView)myDialog.findViewById(R.id.textview1social);
         TextView twitterText		= 	(TextView)myDialog.findViewById(R.id.textview2social);
         TextView instagramText		= 	(TextView)myDialog.findViewById(R.id.textview3social);
         TextView pinterestText		= 	(TextView)myDialog.findViewById(R.id.textview4social);
         
         /*closeImageView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				myDialog.cancel();
			}
		});
      */
         facebookButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MainActivity)getActivity()).fbSelected();
			}
		});
         
        twitterButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				((MainActivity)getActivity()).twitterSelected();
			}
		});
        
        instagramButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MainActivity)getActivity()).instaGram();
			}
		});
        
        pinterestButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MainActivity)getActivity()).pinterest();
			}
		});
        
        facebookText.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MainActivity)getActivity()).fbSelected();
			}
		});
         
        twitterText.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				((MainActivity)getActivity()).twitterSelected();
			}
		});
        
        instagramText.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MainActivity)getActivity()).instaGram();
			}
		});
        
        pinterestText.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MainActivity)getActivity()).pinterest();
			}
		});
        
         
		myDialog.getWindow().getAttributes().windowAnimations = R.style.dialoganimation;
		myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		myDialog.getWindow().setLayout(getActivity().getResources().getDisplayMetrics().widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT);
		myDialog.show();

		myDialog.getWindow().getAttributes().windowAnimations = R.style.dialoganimation;

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
