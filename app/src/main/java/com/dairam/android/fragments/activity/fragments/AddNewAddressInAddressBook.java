package com.dairam.android.fragments.activity.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.parserfiles.JSONfunctions;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;


public class AddNewAddressInAddressBook extends Fragment {

	final String ZONE_URL = "http://dairam.com/index.php?route=api/zone/get&country_code=";
	EditText addressLine1EditText, addressLine2EditText, addressLine3EditText,
			addressLine4EditText, addressLine5EditText;

	Button backButton, saveButton;

	LinearLayout linlay;

	TextView headerTextView;

	Spinner countrySpinner, zoneSpinner;

	String addressId;
	String addressLine1, addressLine2, addressLine3, addressLine4,
			addressLine5;
	String custId, firstName, lastName;

	SharedPreferences sp;
	String pic_country_url;

	ProgressDialog mProgressDialog;

	ArrayList<HashMap<String, String>> arraylist;
	ArrayList<HashMap<String, String>> arraylistzone;

	ArrayList<String> arrayList2 = new ArrayList<String>();
	ArrayList<String> arrayList3;

	String firstzone, country_id, zone_id;
	String[] countryStrings;
	String[] zoneStrings;

	String zone_url_str;
	int zoneSpinerSelectPosition;

	boolean asyncCompleteFlag, countrySpinnerTouch;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_addnewaddress, container,
				false);
		sp = getActivity().getSharedPreferences("lan", Context.MODE_PRIVATE);
		pic_country_url = AppConstants.PICK_COUNTRIES_URL;

		setupUI(v, false);
		addressLine1EditText = (EditText) v
				.findViewById(R.id.editTextadress1addnew);
		addressLine2EditText = (EditText) v
				.findViewById(R.id.editTextadress2addnew);
		addressLine3EditText = (EditText) v
				.findViewById(R.id.editTextadress3addnew);
		countrySpinner = (Spinner) v.findViewById(R.id.spincountryadnew);
		zoneSpinner = (Spinner) v.findViewById(R.id.spinzoneadnew);
		headerTextView = (TextView) v.findViewById(R.id.textView1heading);

		addressLine3EditText.setVisibility(View.GONE);

		setHeading();

		SharedPreferences sp1 = getActivity().getSharedPreferences("tag",
				Context.MODE_PRIVATE);
		String p = "fragmentnewaddressbook";
		Editor editor = sp1.edit();
		editor.putString("act", p);
		editor.commit();
		addressLine1EditText.requestFocus();

		backButton = (Button) v.findViewById(R.id.buttonbackeditinfo);
		headerTextView = (TextView) v.findViewById(R.id.textViewbackeditinfo);
		saveButton = (Button) v.findViewById(R.id.buttonsaveaddnew);
		linlay = (LinearLayout) v.findViewById(R.id.linlayaddnew);
		// db_obj = new OpenHelper(getActivity());

		Typeface fontFace = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/LibreBaskerville-Regular.otf");
		headerTextView.setTypeface(fontFace);
		asyncCompleteFlag = false;
		new GetCountriesTask().execute();

		countrySpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {

						HashMap<String, String> resultp = arraylist.get(pos);
						String idString = resultp.get("id");
						zone_url_str = ZONE_URL + idString + "&language="
								+ getLanguage();

						System.out.println("The Country id" + idString);
						country_id = idString;
						if (idString == firstzone) {
							if (countrySpinnerTouch)
								new GetZoneTask().execute();
						} else {
							new GetZoneTask().execute();
						}
					}

					public void onNothingSelected(AdapterView<?> parent) {
					}
				});
		zoneSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {
						zoneSpinerSelectPosition = pos;
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});

		countrySpinner.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				countrySpinnerTouch = true;
				asyncCompleteFlag = true;
				return false;
			}
		});

		zoneSpinner.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				asyncCompleteFlag = true;
				countrySpinnerTouch = false;
				return false;
			}
		});

		saveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addressLine1 = addressLine1EditText.getText().toString();
				addressLine2 = addressLine2EditText.getText().toString();
				addressLine3 = addressLine3EditText.getText().toString();
				addressLine4 = countrySpinner.getSelectedItem().toString();
				addressLine5 = zoneSpinner.getSelectedItem().toString();
				if ((addressLine1.length() > 0) || (addressLine2.length() > 0)
						|| (addressLine3.length() > 0)) {
					/*
					 * if (flag == true) { db_obj.DeletrowImage(address1); }
					 * db_obj.addressDetailsInsertion(addressLine1,
					 * addressLine2, addressLine3, addressLine4, addressLine5);
					 * backEventPressed();
					 */
					new doUploadNewAdressTask(addressLine1, addressLine2,
							addressLine3, addressLine4, addressLine5).execute();
				} else {
					Toast.makeText(getActivity(),
							getResources().getString(R.string.fill_all_fields),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		return v;
	}

	public void setupUI(View view, boolean isHeading) {
		Typeface myTypeface = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/HELVETICANEUELTARABIC-ROMAN.TTF");
		TextView myTextView = (TextView) view
				.findViewById(R.id.textView1heading);
		myTextView.setTypeface(myTypeface);
		addressLine1EditText.setTypeface(myTypeface);
		addressLine2EditText.setTypeface(myTypeface);
		addressLine3EditText.setTypeface(myTypeface);
		addressLine4EditText.setTypeface(myTypeface);
		addressLine5EditText.setTypeface(myTypeface);

	}

	Typeface getRegularTypeFace() {
		return Typeface.createFromAsset(getActivity().getAssets(),
				getActivity().getString(R.string.regular_typeface));
	}

	public String getLanguage() {
		SharedPreferences sp = getActivity().getSharedPreferences("lan",
				Context.MODE_PRIVATE);
		if (sp.getString("lan", "en").equals("en"))
			return "1";
		else
			return "2";
	}

	public void setHeading() {
		if (getLanguage().equals("1"))
			headerTextView.setText("ADD NEW ADDRESS");
		else
			headerTextView.setText("إضافة عنوان جديد");
	}

	private class GetCountriesTask extends AsyncTask<Void, Void, Void> {
		JSONObject jsonObject;
		JSONArray jsonArray;

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
				pic_country_url = pic_country_url + getLanguage();

				Log.e("AddNewAddressInAddressBook", pic_country_url);

				arraylist = new ArrayList<HashMap<String, String>>();
				jsonObject = JSONfunctions.getJSONfromURL(pic_country_url);

				jsonArray = jsonObject.getJSONArray("country");

				for (int i = 0; i < jsonArray.length(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					jsonObject = jsonArray.getJSONObject(i);

					// Retrieve JSON Objects
					map.put("id", jsonObject.getString("id"));
					map.put("name", jsonObject.getString("name"));

					// Set the JSON Objects into the array
					arraylist.add(map);

					arrayList2.add(jsonObject.getString("name"));
				}
				Log.e("arraylist", arraylist.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void args) {
			countryStrings = arrayList2.toArray(new String[arrayList2.size()]);
			ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(
					getActivity(), R.layout.spinner_items_single,
					countryStrings);
			adapter_state.setDropDownViewResource(R.layout.spinner_single_item);
			countrySpinner.setAdapter(adapter_state);

			HashMap<String, String> resultp = arraylist.get(0);
			String idString = resultp.get("id");

			zone_url_str = ZONE_URL + idString + "&language=" + getLanguage();
			mProgressDialog.dismiss();
			new GetZoneTask().execute();
			firstzone = idString;
		}
	}

	protected class GetZoneTask extends AsyncTask<Void, Void, Void> {
		JSONObject jsonObject;
		JSONArray jsonArray;
		String language;

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
				// Create an array
				arraylistzone = new ArrayList<HashMap<String, String>>();
				arrayList3 = new ArrayList<String>();
				jsonObject = JSONfunctions.getJSONfromURL(zone_url_str);

				jsonArray = jsonObject.getJSONArray("zones");

				for (int i = 0; i < jsonArray.length(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					jsonObject = jsonArray.getJSONObject(i);

					// Retrieve JSON Objects
					map.put("id", jsonObject.getString("id"));
					map.put("name", jsonObject.getString("name"));

					// Set the JSON Objects into the array
					arraylistzone.add(map);
					arrayList3.add(jsonObject.getString("name"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void args) {
			zoneStrings = arrayList3.toArray(new String[arrayList3.size()]);
			ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(
					getActivity(), R.layout.spinner_items_single, zoneStrings);
			adapter_state.setDropDownViewResource(R.layout.spinner_single_item);
			zoneSpinner.setAdapter(adapter_state);
			countrySpinnerTouch = false;
			// Close the progress dialog
			mProgressDialog.dismiss();
		}
	}

	public class doUploadNewAdressTask extends AsyncTask<Void, String, Void> {

		String type, result;
		boolean postStatus = false;
		JSONObject jObj = null;
		String aD1, aD2, post, selcountry, selzone;

		InputStream istream;

		public doUploadNewAdressTask(String addString, String addString2,
				String postString, String countryname, String zonename) {
			this.aD1 = addString;
			this.aD2 = addString2;
			this.post = postString;
			this.selcountry = countryname;
			this.selzone = zonename;

		}

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
			// jObj = FetchJson.getJSONfromURL(GlobalConst.ALL_POST_API);

			try {
				SharedPreferences sharedPreferences = getActivity()
						.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
				custId = sharedPreferences.getString("id", "");
				firstName = sharedPreferences.getString("firstname", "");
				lastName = sharedPreferences.getString("lastname", "");
				HashMap<String, String> resultp = arraylistzone
						.get(zoneSpinerSelectPosition);
				String idString = resultp.get("id");
				zone_id = idString;

				JSONObject objJson = new JSONObject();

				objJson.accumulate("cust_id", custId);
				objJson.accumulate("firstname", firstName);
				objJson.accumulate("lastname", lastName);
				objJson.accumulate("address_1", aD1);
				objJson.accumulate("address_2", aD2);
				objJson.accumulate("postcode", post);
				objJson.accumulate("country_id", country_id);
				objJson.accumulate("zone_id", zone_id);
				objJson.accumulate("language", getLanguage());

				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						AppConstants.MY_ORDER_ADDRESS_POST_JSON_URL);
				// StringEntity entity = new StringEntity(objJson.toString());
				StringEntity entity = new StringEntity(objJson.toString(),
						HTTP.UTF_8);
				Log.e("New Address Request URL",
						AppConstants.MY_ORDER_ADDRESS_POST_JSON_URL);
				Log.e("New Address Request", objJson.toString());

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

					// result = Util.convertInputStreamToString(istream);

					jObj = new JSONObject(result);
					Log.i("Login Result", result);
				} else {
					result = null;
					Log.i("Input Stream", "Null");
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
				Log.i("CHECKOUT_FRAG", "Parsing POSTS Json\n" + jObj.toString());
				try {
					JSONObject resultObj = jObj.getJSONObject("Result");
					if (resultObj.getString("success").equals("true")) {
						Toast.makeText(getActivity(),
								"" + resultObj.getString("success_msg"),
								Toast.LENGTH_SHORT).show();
						clearFields();
						// gotoConfirmPage(resultObj.getString("shipping_address"));

					} else {
						Toast.makeText(getActivity(),
								"" + resultObj.getString("success_msg"),
								Toast.LENGTH_SHORT).show();
					}

				}

				catch (JSONException e) {
					e.printStackTrace();
				}

			} else {
				Toast.makeText(getActivity(), R.string.no_response,
						Toast.LENGTH_SHORT).show();
			}
			mProgressDialog.dismiss();

		}

	}

	public void clearFields() {
		addressLine1EditText.setText("");
		addressLine2EditText.setText("");
		addressLine3EditText.setText("");
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
						// backEventPressed();
						return true;
					}
				}
				return false;
			}
		});
		super.onActivityCreated(savedInstanceState);
	}

}
