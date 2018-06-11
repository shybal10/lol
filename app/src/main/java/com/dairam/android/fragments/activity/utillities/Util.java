package com.dairam.android.fragments.activity.utillities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.fragments.FragmentCategories;

import java.util.Scanner;

public class Util {
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public final static void setCurrency(Context context, String currency) {
		SharedPreferences currencyPref = context.getSharedPreferences(
				"currencyPref", Context.MODE_PRIVATE);
		currencyPref.edit().putString("currency", currency).apply();
	}

	public final static String getCurrency(Context context) {
		SharedPreferences currencyPref = context.getSharedPreferences(
				"currencyPref", Context.MODE_PRIVATE);
		return currencyPref.getString("currency", "KWD");
	}

	public final static String getLanguage(Context context) {
		SharedPreferences sp = context.getSharedPreferences("lan",
				Context.MODE_PRIVATE);

		if (sp.getString("lan", "en").equals("en"))
			return "1";
		else
			return "2";
	}

	public final static void CallHomePageFragment() {
		/*Fragment fr = new FragmentOne();
		FragmentManager fm = fr.getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_place, fr);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();*/
		Fragment fr = new FragmentCategories();
		FragmentManager fm = fr.getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_place, fr);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}

	public final static void CallNoInternetFragment() {
		/*Fragment fr = new FragmentOne();
		FragmentManager fm = fr.getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_place, fr);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();*/
		
		Fragment fr = new FragmentCategories();
		FragmentManager fm = fr.getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_place, fr);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}
	public static final String getCustomerId(Context contex)
	{
		SharedPreferences sp = contex.getSharedPreferences("MyPrefs",
				Context.MODE_PRIVATE);
		return sp.getString("id", null);
	}

	public final static double fetchDouble(Context context, String strContent) {
		Scanner st = new Scanner(strContent);
		Log.e("Util","Doubel String : "+strContent);
		if(strContent.contains("د.ك")){
			strContent=strContent.replace("د.ك", "").trim();
			//Log.e("Util","Doubel String Trim: "+strContent);
			Log.e("return if ar ","...... : "+strContent);
			return Double.parseDouble(strContent);
		}else{		
		while (!st.hasNextDouble()) {
			try {
				st.next();
			} catch (Exception e) {
				//Log.e("Util",""+e.toString());
			}
		}
		return st.nextDouble();
	}
	}
	
	public static String getHtmlDatawithFont(Context context, String data) {
		  
		  String head = "<head><style type=\"text/css\">@font-face {font-family: 'MyFont';src: url(\"file:///android_asset/fonts/HELVETICANEUELTARABIC-ROMAN.TTF\")}body {font-family: 'MyFont';font-size: medium;text-align: justify;}</style></head>";

		  String htmlData = "<html>" + head + "<body>" + data + "</body></html>";
		  return htmlData;
		 }

}
