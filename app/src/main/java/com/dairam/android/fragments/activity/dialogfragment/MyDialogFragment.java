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

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.MainActivity;

public class MyDialogFragment extends DialogFragment {	
	public static MyDialogFragment newInstance() {		
		String title = "My Fragment";
        MyDialogFragment f = new MyDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        f.setArguments(args);
        return f;
    }

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		 final Dialog myDialog = new Dialog(getActivity());
		       myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
               myDialog.setContentView(R.layout.dialog_layout);
         
         Button englishButton  		=	(Button)myDialog.findViewById(R.id.buttonenglish_dialog);
         Button arabicButton        =   (Button)myDialog.findViewById(R.id.buttonarabic_dialog);
        
        englishButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((MainActivity)getActivity()).EnglishClicked();
			}
		});
         
         arabicButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((MainActivity)getActivity()).ArabicClicked();
			}
		});
         
		myDialog.getWindow().getAttributes().windowAnimations = R.style.dialoganimation;
		myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));		
		myDialog.getWindow().getAttributes().windowAnimations = R.style.dialoganimation;
		myDialog.getWindow().setLayout(getActivity().getResources().getDisplayMetrics().widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT);
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
        	window.setGravity(Gravity.BOTTOM | Gravity.CENTER);
        else
        	window.setGravity(Gravity.BOTTOM | Gravity.CENTER);
 
        WindowManager.LayoutParams params = window.getAttributes();
        params.y = dpToPx(0);
        window.setAttributes(params);
    }
 
    private int dpToPx(int dp) {
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }
	
	
}
