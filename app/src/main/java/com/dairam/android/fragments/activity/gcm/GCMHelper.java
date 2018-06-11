/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dairam.android.fragments.activity.gcm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;

import com.dairam.android.fragments.activity.MainActivity;
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Main UI for the demo app.
 */
public class GCMHelper {

	// public final String EXTRA_MESSAGE = "message";
	public final String PROPERTY_REG_ID = "registration_id";
	private final String PROPERTY_APP_VERSION = "appVersion";
	private final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	/**
	 * Substitute you own sender ID here. This is the project number you got
	 * from the API Console, as described in "Getting Started."
	 */
	String SENDER_ID = "123983862833";

	/**
	 * Tag used on log messages.
	 */
	final String TAG = "GCMHelper";
	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();
	Context context;
	Activity activity;
	String mServerUrl;
	JSONObject mJsonObj;
	String regid;

	public GCMHelper(Activity activity, String mServerUrl) {
		this.activity = activity;
		context = activity.getApplicationContext();
		this.mServerUrl = mServerUrl;
	}

	@SuppressLint("NewApi")
	public void gcmRegisteration() {
		// Check device for Play Services APK. If check succeeds, proceed with
		// GCM registration.
		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(activity);
			regid = getRegistrationId(context);

			if (regid.isEmpty()) {
				registerInBackground();
			} else {
				Log.e(TAG, "No valid Google Play Services APK found.");
				sendRegistrationIdToBackend();
			}

		} else {
			Log.i(TAG, "No valid Google Play Services APK found.");
		}
	}

	/*
	 * Check the device to make sure it has the Google Play Services APK. If it
	 * doesn't, display a dialog that allows users to download the APK from the
	 * Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(activity);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "This device is not supported.");
				// Show alert dialog.
			}
			return false;
		}
		return true;
	}

	/**
	 * Stores the registration ID and the app versionCode in the application's
	 * {@code SharedPreferences}.
	 * 
	 * @param context
	 *            application's context.
	 * @param regId
	 *            registration ID
	 */

	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGcmPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	/**
	 * Gets the current registration ID for application on GCM service, if there
	 * is one.
	 * <p>
	 * If result is empty, the app needs to register.
	 * 
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	@SuppressLint("NewApi")
	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGcmPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		Log.e(TAG, "getRegistrationId: " + registrationId);
		return registrationId;
	}

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and the app versionCode in the application's
	 * shared preferences.
	 */
	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@SuppressWarnings("unused")
			private ProgressDialog progDialog;

			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regid = gcm.register(SENDER_ID);
					msg = "Device registered, registration ID=" + regid;

					Log.i("regIDGCM",
							"registerInBackground-regid" + regid);
					// GlobalConst.gcm_id=regid;
					// You should send the registration ID to your server over
					// HTTP, so it
					// can use GCM/HTTP or CCS to send messages to your app.
					sendRegistrationIdToBackend();

					// For this demo: we don't need to send it because the
					// device will send
					// upstream messages to a server that echo back the message
					// using the
					// 'from' address in the message.

					// Persist the regID - no need to register again.
					storeRegistrationId(context, regid);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				// mDisplay.append(msg + "\n");
				// progDialog.dismiss();
			}
		}.execute(null, null, null);
	}

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGcmPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences,
		// but
		// how you store the regID in your app is up to you.
		// Log.d(TAG,
		// "Ema.class.getSimpleName(): "+RegistrationActivity.class.getSimpleName());
		return activity.getSharedPreferences(
				MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
	}
	/**
	 * Sends the registration ID to your server over HTTP, so it can use
	 * GCM/HTTP or CCS to send messages to your app. Not needed for this demo
	 * since the device sends upstream messages to a server that echoes back the
	 * message using the 'from' address in the message.
	 */
	private void sendRegistrationIdToBackend() {
		
		try {
			// Log.e("Id..", regid);
			AppConstants.gcm_id = regid;
			Log.e("", AppConstants.gcm_id);
			new HttpPostAsync(mServerUrl).execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
class HttpPostAsync extends AsyncTask<String, String, String> {
		

		private String serverUrl;

		public HttpPostAsync(String serverUrl) {
			this.serverUrl = serverUrl;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// progDialog = new ProgressDialog(activity);
			// progDialog.setMessage("Loading...");
			// progDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String serverResponse = null;
			InputStream istream;

			try {
				
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(serverUrl);
				
				Log.e("urll", serverUrl);
				JSONObject objJson = new JSONObject();
				objJson.accumulate(AppConstants.device_Token, regid);
				objJson.accumulate(AppConstants.platform_Token, 1);
				
				Log.e("objJson................", objJson.toString());
				StringEntity entity = new StringEntity(objJson.toString());
				Log.i("Login Request", objJson.toString());

				httppost.setEntity(entity);
				httppost.setHeader("Accept", "application/json");
				httppost.setHeader("Content-type", "application/json");
				
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity httpEntity = response.getEntity();
				istream = httpEntity.getContent();
				try {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(istream, "iso-8859-1"), 8);
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
					istream.close();
					serverResponse = sb.toString();
					Log.i(TAG, "HTTPPOST-response: " + serverResponse);
				} catch (Exception e) {
					Log.e("log_tag", "Error converting result " + e.toString());
				}
			} catch (Exception e) {
				Log.e("log_tag", "Error in http connection " + e.toString());
			}
			// Convert response to string
			return serverResponse;
		}

		@Override
		protected void onPostExecute(String unused) {
			// progDialog.dismiss();
			Log.e("Response", "" + unused);

		}
	}

}
