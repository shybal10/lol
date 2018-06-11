package com.dairam.android.fragments.activity.dialogfragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.interfaces.OnAgreeTermsandConditions;
import com.dairam.android.fragments.activity.utillities.Util;

public class ReadTermsAndConditions extends DialogFragment {
	Button btnIAgree;
	WebView webTerms;
	TextView lblTermsHeading;
	ProgressDialog mProgressDialog;
	public String TermsContent;
	OnAgreeTermsandConditions clickCheck;
	public void  newInstance(String content, OnAgreeTermsandConditions isChecked){
		Log.e("Terms  ..", ""+content);
		this.TermsContent = content;
		this.clickCheck=isChecked;
		
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		final Dialog myDialog = new Dialog(getActivity());
		//myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		myDialog.setContentView(R.layout.read_terms_and_condition_dialog);


		webTerms = (WebView) myDialog.findViewById(R.id.webViewReadTerms);
		btnIAgree = (Button) myDialog.findViewById(R.id.btnIAgreeReadTerms);
		if(Util.getLanguage(getActivity()).equals("1")){
			btnIAgree.setBackgroundResource(R.drawable.iagree_btn);
		}else{
			btnIAgree.setBackgroundResource(R.drawable.iagree_btn_ar);
		}

		btnIAgree.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				clickCheck.onChecked(true);
				myDialog.dismiss();

			}
		});
		setContentWebView();
		myDialog.getWindow().getAttributes().windowAnimations = R.style.dialoganimation;
		myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		myDialog.getWindow().setLayout(getActivity().getResources().getDisplayMetrics().widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT);
		myDialog.getWindow().getAttributes().windowAnimations = R.style.dialoganimation;
		return myDialog;
	}

	
	
	public void setContentWebView(){
		webTerms.getSettings().setJavaScriptEnabled(true);
		webTerms.getSettings().setLoadWithOverviewMode(true);

		// textViewTermsAndConditions.getSettings().setUseWideViewPort(true);
		webTerms.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		webTerms.setScrollbarFadingEnabled(false);
		webTerms.getSettings().setBuiltInZoomControls(true);
		// textViewTermsAndConditions.setInitialScale(1);

		webTerms.getSettings().setBuiltInZoomControls(true);
		webTerms.getSettings().setSupportZoom(true);
		if(Util.getLanguage(getActivity()).equals("1")){
			webTerms.loadDataWithBaseURL(null,
				Util.getHtmlDatawithFont(getActivity(), TermsContent), "text/html", "UTF-8", "");
		}
		else{
			webTerms.loadDataWithBaseURL(null,
					Util.getHtmlDatawithFont(getActivity(), TermsContent), "text/html", "UTF-8", "");
		}
	}
	
	public void setupUI(View view, boolean isHeading) {
		  Typeface myTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/HELVETICANEUELTARABIC-ROMAN.TTF");
		  if(Util.getLanguage(getActivity()).equals("2")){
		     TextView myTextView = (TextView)view.findViewById(R.id.textView1);
		     myTextView.setTypeface(myTypeface);
		     lblTermsHeading.setTypeface(myTypeface);
		  }

		 }

	

}
