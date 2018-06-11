package com.dairam.android.fragments.activity.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.appconstants.AppConstants;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class DialogFragPerfumeNotification extends DialogFragment{
	
	ProgressDialog mProgressDialog;
	
	TextView Notification;
	Button btnOK;
	ImageView btnClose;
	public Activity activity;
	
	String message,discount,addrType,addressId;
	String firstName,lastName,address1,address2,pinCode,countryId,zoneId,language,custId;
	public DialogFragPerfumeNotification(){

	}
	
	public DialogFragPerfumeNotification(String message,String discount,String addrType,String addres_idString)
	{
		this.message = message;
		this.discount = discount;
		this.addrType = addrType;
		this.addressId = addres_idString;
	}
	
	
	public DialogFragPerfumeNotification(String message,String discount,String addrType,
										 String firstName, String lastName, String address1,
										 String address2, String postCode, String countryId,
										 String zoneId, String language, String custId)
	{
		this.message = message;
		this.discount = discount;
		this.addrType = addrType;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address1 = address1;
		this.address2 = address2;
		this.pinCode = postCode;
		this.countryId = countryId;
		this.zoneId = zoneId;
		this.language = language;
		this.custId = custId;
				
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.perfume_notify_dialog, container,
				false);
		
		activity = this.getActivity();
		
		getDialog().setCanceledOnTouchOutside(true);
		
		Notification = (TextView)rootView.findViewById(R.id.txtViewPerfumeNofity);
		btnOK = (Button)rootView.findViewById(R.id.btnNextPerfumeNotify);
		btnClose = (ImageView)rootView.findViewById(R.id.close_btn_dialog_perfume);
		
		Notification.setText(message);
		
		btnOK.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				if(addrType.equals("OLD"))
				{
					Log.e("DialogFragPerfNoti","Existing Address");
					gotoConfirmPage(addressId);
				}
				else
				{
					Log.e("DialogFragPerfNoti","New Address");
					new UploadNewAddress().execute();
				}
			getDialog().dismiss();	
			}
			
		});
		
		btnClose.setOnClickListener(new OnClickListener()
		{			
			@Override
			public void onClick(View v) {
				getDialog().dismiss();
				
			}
		});
		
		return rootView;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	  Dialog dialog = super.onCreateDialog(savedInstanceState);

	  // request a window without the title
	  dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
	  return dialog;
	}
	
	private void gotoConfirmPage(String shiping_ad) {

		Fragment fr = new CheckoutFragmentSecondStep();
		Bundle bundle = new Bundle();
		bundle.putString("shipping_address", shiping_ad);
		bundle.putString("discount", discount);
		fr.setArguments(bundle);
		Log.e("Discount", "In CheckoutFrag 1 Discount : " + discount);
		FragmentManager fm =  activity.getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_place, fr);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}
	
	
	
	public class UploadNewAddress extends AsyncTask<Void, Void, Void> {

		String type, result;
		boolean postStatus = false;
		JSONObject jObj = null;

		InputStream istream;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			mProgressDialog = new ProgressDialog(getActivity(), R.style.MyTheme);
			mProgressDialog.setCancelable(false);
			mProgressDialog
					.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
			mProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				
				JSONObject objJson = new JSONObject();
				objJson.accumulate("firstname", firstName);
				objJson.accumulate("lastname", lastName);
				objJson.accumulate("address_1", address1);
				objJson.accumulate("address_2", address2);
				objJson.accumulate("postcode", pinCode);
				objJson.accumulate("country_id", countryId);
				objJson.accumulate("zone_id", zoneId);
				objJson.accumulate("language", getLanguage());
				objJson.accumulate("cust_id", custId);

				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						AppConstants.MY_ORDER_ADDRESS_POST_JSON_URL);
				//StringEntity entity = new StringEntity(objJson.toString());
				StringEntity entity = new StringEntity(objJson.toString(), HTTP.UTF_8);
				
				Log.e("Request URL",AppConstants.MY_ORDER_ADDRESS_POST_JSON_URL);
				Log.e("Login Request", objJson.toString());

				httppost.setEntity(entity);
				httppost.setHeader("Accept", "application/json");
				httppost.setHeader("Content-type", "application/json");
				HttpResponse response = httpClient.execute(httppost);
				istream = response.getEntity().getContent();

				if (istream != null) {

					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(istream));
					String line = "";
					result = "";
					while ((line = bufferedReader.readLine()) != null)
						result += line;

					istream.close();

					jObj = new JSONObject(result);
					Log.e("Login Result", result);
				} else {
					result = null;
					Log.e("Input Stream", "Null");
				}

			} catch (JSONException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			if (jObj != null) {
				try {
					JSONObject resultObj = jObj.getJSONObject("Result");
					if (resultObj.getBoolean("success")) {
						
						Toast.makeText(activity,resultObj.getString("success_msg"),
								Toast.LENGTH_SHORT).show();
						gotoConfirmPage(""+resultObj.getInt("shipping_address"));

					} else 
					{
						Log.e("DialogPerfumeFrag", "Null Json Object");
					}

				}

				catch (JSONException e) {
					Log.e("DialogPerfumeFrag", "Null Json Object in Catch");
					e.printStackTrace();
				}

			} else {
				Toast.makeText(getActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
			}
			mProgressDialog.dismiss();

		}

	}
	
	public String getLanguage() {
		SharedPreferences sp = getActivity().getSharedPreferences("lan",
				Context.MODE_PRIVATE);

		if (sp.getString("lan", "en").equals("en"))
			return "1";
		else
			return "2";
	}


}
