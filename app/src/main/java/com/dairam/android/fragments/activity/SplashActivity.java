package com.dairam.android.fragments.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.crashlytics.android.answers.Answers;
import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.gcm.GCMHelper;
import com.dairam.android.fragments.activity.utillities.PrefUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import io.fabric.sdk.android.Fabric;

public class SplashActivity extends Activity {

	private Button englshButton;
	private Button arabicButton;
	
	JSONObject jsonobject;
	JSONArray jsonarray;
	ProgressDialog mProgressDialog;
	ArrayList<HashMap<String, String>> arraylist;

	Context context;

	Fragment fr;

	String country;

	Boolean isInternetPresent = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Fabric.with(this, new Answers());
		setContentView(R.layout.activity_splash);
		
		PrefUtil.setBrandGridcount(getApplicationContext(), 0);
		PrefUtil.setCatGridcount(getApplicationContext(), 0);
	
		context = getApplicationContext();
		try {
			TelephonyManager telephonyManager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
			AppConstants.DEVICE_ID = telephonyManager.getDeviceId();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			getProjectKeyHash();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//gcmRegisteration();
		getCountryName();
		
		SharedPreferences sp1 = getApplicationContext().getSharedPreferences("tag", Context.MODE_PRIVATE);
		String p = "fragmentcategories";
		Editor editor = sp1.edit();
		editor.putString("act", p);
		editor.commit();

		englshButton = (Button) findViewById(R.id.buttonenglish);
		arabicButton = (Button) findViewById(R.id.buttonarabic);

		englshButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
			
				SharedPreferences sp = SplashActivity.this
						.getSharedPreferences("lan", Context.MODE_PRIVATE);
				Editor e = sp.edit();
				e.putString("lan", "en");
				e.commit();
				finish();
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				overridePendingTransition(0, 0);
			}
		});

		arabicButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				SharedPreferences sp = SplashActivity.this.getSharedPreferences("lan", Context.MODE_PRIVATE);
				Editor e = sp.edit();
				e.putString("lan", "ar");
				e.commit();
				finish();
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), MainActivity.class);
				startActivity(intent);
				int num = 2;
				String number = Integer.toString(num);
			}
		});

	}

	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}

	public void getCountryName() {

	}

	/*private class DownloadJSON extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				// Create an array
				arraylist = new ArrayList<HashMap<String, String>>();
				// Retrieve JSON Objects from the given URL address
				jsonobject = JSONfunctions
						.getJSONfromURL(AppConstants.IP_FETCHING_URL);
				country = jsonobject
						.getString(AppConstants.IP_FETCHING_COUNTRY);

			} catch (Exception e) {
				
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void args) {

			System.out.println("The country name " + country);

		}
	}*/
	public void getProjectKeyHash() throws NoSuchAlgorithmException {
		PackageInfo info = null;
		try {
			info = getPackageManager().getPackageInfo("com.dairam.android.fragments",
					PackageManager.GET_SIGNATURES);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Signature signature : info.signatures) {
			MessageDigest md = MessageDigest.getInstance("SHA");
			md.update(signature.toByteArray());
			Log.e("KeyHash:","KeyHash: "
							+ Base64.encodeToString(md.digest(), Base64.DEFAULT));
		}
	}
	public void gcmRegisteration() { 		
		GCMHelper gcmHelper = new GCMHelper(this,AppConstants.Add_Device_Token);
		gcmHelper.gcmRegisteration();
	}
}
