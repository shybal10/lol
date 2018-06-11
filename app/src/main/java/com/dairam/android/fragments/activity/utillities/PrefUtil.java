package com.dairam.android.fragments.activity.utillities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefUtil {
	
	private static final String PREF_FRAG_HEAD = "page_heading";
	 public static void setGridcount(final Context context, final int heading) {
	  SharedPreferences sp = PreferenceManager
	    .getDefaultSharedPreferences(context);
	  sp.edit().putInt(PREF_FRAG_HEAD, heading).commit();
	 }
	 public static int getGridCount(final Context context) {
	  SharedPreferences sp = PreferenceManager
	    .getDefaultSharedPreferences(context);
	  return sp.getInt(PREF_FRAG_HEAD, 0);
	 }
	 
	 
	 private static final String PREF_BRAND_HEAD = "brand_heading";
	 public static void setBrandGridcount(final Context context, final int heading) {
	  SharedPreferences sp = PreferenceManager
	    .getDefaultSharedPreferences(context);
	  sp.edit().putInt(PREF_FRAG_HEAD, heading).commit();
	 }
	 public static int getBrandGridCount(final Context context) {
	  SharedPreferences sp = PreferenceManager
	    .getDefaultSharedPreferences(context);
	  return sp.getInt(PREF_FRAG_HEAD, 0);
	 }
	 
	 private static final String PREF_CAT_HEAD = "categories_heading";
	 public static void setCatGridcount(final Context context, final int heading) {
	  SharedPreferences sp = PreferenceManager
	    .getDefaultSharedPreferences(context);
	  sp.edit().putInt(PREF_FRAG_HEAD, heading).commit();
	 }
	 public static int getCatGridCount(final Context context) {
	  SharedPreferences sp = PreferenceManager
	    .getDefaultSharedPreferences(context);
	  return sp.getInt(PREF_FRAG_HEAD, 0);
	 }
	 
}
