package com.dairam.android.fragments.activity.Customisedpopup;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.dairam.android.fragments.R;

public class pops extends android.widget.PopupWindow {
	Context ctx;
	Button btnDismiss;
	TextView lblText;
	View popupView;
	
	public pops(Context context)
	{
	    super(context);
	
	    ctx = context;
	    popupView = LayoutInflater.from(context).inflate(R.layout.popup, null);
	    setContentView(popupView);
	
	    btnDismiss = (Button)popupView.findViewById(R.id.btn_dismiss);
	    lblText = (TextView)popupView.findViewById(R.id.text);
	
	    setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
	    setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
	
	    // Closes the popup window when touch outside of it - when looses focus
	    setOutsideTouchable(true);
	    setFocusable(true);
	
	    // Removes default black background
	    setBackgroundDrawable(new BitmapDrawable());
	
	    btnDismiss.setOnClickListener(new Button.OnClickListener(){
	        @Override
		        public void onClick(View v) {
	        	   dismiss();
		        }});
	   }
	   // End constructor
	   // Attaches the view to its parent anchor-view at position x and y
	   public void show(View anchor, int x, int y)
	   {
	      showAtLocation(anchor, Gravity.CENTER, x, y);
	   }
}