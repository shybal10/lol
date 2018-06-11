package com.dairam.android.fragments.activity.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.DialogKNETResult;
import com.dairam.android.fragments.activity.interfaces.listenerHideView;

import java.util.ArrayList;

public class VISAPaymentFragment extends Fragment {
	
	Activity sample;
	WebView webViewVISA;
	TextView backTextView;
    Button backButton;
	String VISAPaymentGatewayURL;
	@Override
	public View onCreateView(LayoutInflater inflater,
		      ViewGroup container, Bundle savedInstanceState) 
	{
		View v = inflater.inflate(R.layout.activity_knet_payment, container, false);
	
		Bundle b = getArguments();
		VISAPaymentGatewayURL = b.getString("gatewayUrl");
		
		SharedPreferences sp1=getActivity().getSharedPreferences("tag", Context.MODE_PRIVATE);
		Editor editor = sp1.edit();
		editor.putString("act", "knetpaymentwebview");
		editor.putString("gatewayUrl", VISAPaymentGatewayURL);
		editor.commit(); 
		
		backButton    =   (Button)v.findViewById(R.id.buttonbacktermsknet);
		backTextView  =   (TextView)v.findViewById(R.id.textViewbacktermsknet);
		webViewVISA	  =	  (WebView)v.findViewById(R.id.webviewknet);
		
		webViewVISA.getSettings().setLoadWithOverviewMode(true);
		//webViewVISA.getSettings().setUseWideViewPort(true);
		webViewVISA.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		webViewVISA.setScrollbarFadingEnabled(false);
		webViewVISA.getSettings().setBuiltInZoomControls(true);
		webViewVISA.getSettings().setSupportZoom(true);
		
		backTextView.setText("CREDIT CARD");
		
//...................back button press section start............................
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.e("Knet payment frag","Back pressed Button");
				backToHome();
				
			}
		});
		
		backTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.e("Knet payment frag","Back pressed TextView");
				backToHome();
				
			}
		});
//...................back button press section end............................
		
		
		Log.e("Knet URL",VISAPaymentGatewayURL);
		webViewVISA.getSettings().setJavaScriptEnabled(true);
		webViewVISA.setWebViewClient(mWebViewClient);
		webViewVISA.loadUrl(VISAPaymentGatewayURL);
		
		switch (getLanguage()) {
		case "en":
			
			break;

		default:
			setupUI(v, false);	
			break;
		}
		
	
		return v;
	}
	
	@Override
	public void onAttach(Activity activity) {

		sample = activity;
		super.onAttach(activity);
		listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) activity;
		listenerHideView.makeDefaultHeader();
		listenerHideView.hide();
		listenerHideView.promoItemHideTopbar();
		listenerHideView.setTopBarForCart();

	}
	
	private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.e("URL in start",url);
            
            if(url.contains("Result="))
            {
            	//Success
            	Log.e("KNET Payment","Payment Success");
            	url =  url.replace("%20", " ");
            	ArrayList<String> KNETResut = new ArrayList<String>();
            	String[] retval = url.split("&");
				for(String str : retval)
				{
					Log.e("Splited String(&)",str);
					//for(String str1 : str.split("="))
					KNETResut.add(str);
				}
				/*Intent intent = new Intent(getActivity(),
						DialogKNETResult.class);
				intent.putStringArrayListExtra("result", KNETResut);
				startActivity(intent);*/
				
				Fragment fr  =   new DialogKNETResult();
				Bundle b = new Bundle();
				b.putStringArrayList("result", KNETResut);
				b.putString("payment", "VISA");
				fr.setArguments(b);
				
				FragmentManager fm = getFragmentManager();
			    FragmentTransaction fragmentTransaction = fm.beginTransaction();
			    fragmentTransaction.replace(R.id.fragment_place, fr);
			    fragmentTransaction.addToBackStack(null);
			    fragmentTransaction.commit();
            }
            else
            {
            	//Error
            	Log.e("VISA Payment","Payment Failed");
            	//Toast.makeText(getActivity(), "Payment Failed", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.e("URL in finish",url);
        }
    };
    
    private void backToHome()
    {
    	/*Fragment fr = new FragmentOne();
		FragmentManager fm = getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_place, fr);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();*/
    	
    	Fragment fr = new FragmentCategories();
		FragmentManager fm = getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_place, fr);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
    }
    public void setupUI(View view, boolean isHeading) {
    	  Typeface myTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/HELVETICANEUELTARABIC-ROMAN.TTF");
    	  //   TextView myTextView = (TextView)view.findViewById(R.id.textView1);
    	    /* backTextView.setTypeface(myTypeface);*/
    	   
    	 }
	private String getLanguage()
	{
		SharedPreferences sp = getActivity().getSharedPreferences("lan",
				Context.MODE_PRIVATE);
		
		if(sp.getString("lan", "en").equals("en"))
			return "1";
		else
			return "2";					
	}

	Typeface getRegularTypeFace() {
		return Typeface.createFromAsset(getActivity().getAssets(),
				getActivity().getString(R.string.regular_typeface));
	}
	
	@Override
	  public void onActivityCreated(Bundle savedInstanceState) {
	     getView().setFocusableInTouchMode(true);
	     getView().requestFocus();

	     getView().setOnKeyListener(new OnKeyListener() {
	             @Override
	             public boolean onKey(View v, int keyCode, KeyEvent event) {
	                     if (event.getAction() == KeyEvent.ACTION_DOWN) {
	                         if (keyCode == KeyEvent.KEYCODE_BACK) {
	                        	 backToHome();
	                         return true;
	                         }
	                     }
	                     return false;
	                 }
	             });
	   super.onActivityCreated(savedInstanceState);
	  }
}
