package com.dairam.android.fragments.activity.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.interfaces.listenerHideView;
import com.dairam.android.fragments.activity.parserfiles.JSONfunctions;
import com.dairam.android.fragments.activity.utillities.Util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
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

public class CheckoutFragmentFirstStep extends Fragment implements
		OnClickListener {
	String TAG = "CheckoutFragmentFirstStep";
	ProgressDialog mProgressDialog;
	Activity sample;
	Fragment fr;
	JSONObject jsonobject;
	JSONArray jsonarray;

	Boolean temp = false;
	boolean defAddres = true;
	ArrayList<HashMap<String, String>> arraylist;
	ArrayList<HashMap<String, String>> arraylistzone;
	ArrayList<HashMap<String, String>> adressList;
	String[] countryStrings;
	String[] zoneStrings;
	String[] adresStrings;

	ArrayList<String> arrayList2 = new ArrayList<String>();
	ArrayList<String> arrayList3;
	ArrayList<String> arrayList4 = new ArrayList<String>();

	String firstzone, country_id, zone_id, addres_idString;

	Context context;

	String zone_url = AppConstants.ZONE_URL;

	Button nextButton;
	public static final String MyPREFERENCES = "MyPrefs";
	public static final String customerid = "id";

	EditText firstNameEditText, LastNameEditText, address1EditText,
			address2EditText, txtMobileNumber;
	// telephoneEditText;
	EditText LabelfirstName, LabelLastName, Labeladdress1, LabelpostCode,
			LabelCountry, LabelZone;
	RadioGroup adresselect;
	RadioButton defaultAdres, custom;
	LinearLayout def, cust;
	Spinner adressSelspinner, countrySpinner, zoneSpinner;
	private SharedPreferences sharedPreferences;
	String id = null;

	String discount;

	Button backButton;
	TextView backTextView;

	boolean countrySpinnerTouch;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		context = getActivity();
		Bundle b = getArguments();
		try {
			discount = b.getString("discount");

			SharedPreferences sp1 = getActivity().getSharedPreferences("tag",
					Context.MODE_PRIVATE);
			Editor editor = sp1.edit();
			editor.putString("act", "checkoutfrag1");
			editor.putString("discount", discount);
			editor.commit();
		} catch (Exception e) {
			if (getLanguage().equals("1"))
				Toast.makeText(context,
						"Voucher Code ignored.. Please re-enter",
						Toast.LENGTH_LONG).show();
			else
				Toast.makeText(context, "رمز الكوبون غير صالح",
						Toast.LENGTH_LONG).show();
			e.printStackTrace();
			Log.e("Error...", e.toString());
		}
		/*
		 * if(b!=null) { discount = b.getString("discount"); } else {
		 * Log.e("Bundle ERRor","Null Bundle"); }
		 */

		final View v = inflater.inflate(R.layout.layout_checkout_step1,
				container, false);

		initializeView(v);

		backButton = (Button) v.findViewById(R.id.buttonbackfnister);
		backTextView = (TextView) v.findViewById(R.id.textViewbackcheckout);
		backButton.setOnClickListener(this);
		backTextView.setOnClickListener(this);

		Typeface fontFace = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/LibreBaskerville-Regular.otf");
		backTextView.setTypeface(fontFace);

		sharedPreferences = context.getSharedPreferences(MyPREFERENCES,
				Context.MODE_PRIVATE);
		if (sharedPreferences.contains(customerid)) {
			id = sharedPreferences.getString(customerid, "");
		}
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.e("default", "" + defAddres);

				if (!defAddres) {
					String firstNameString = null, addString = null, addString2 = null, lastNameString = null, postString = null;

					firstNameString = firstNameEditText.getText().toString()
							.trim();
					lastNameString = LastNameEditText.getText().toString()
							.trim();
					addString = address1EditText.getText().toString().trim();
					postString = txtMobileNumber.getText().toString().trim();
					addString2 = address2EditText.getText().toString().trim();

					if ((firstNameString.length() == 0)
							|| (addString.length() == 0)
							|| (lastNameString.length() == 0)
							|| (postString.length() == 0)
					/* && (addString2.length() == 0) */) {
						Toast.makeText(
								getActivity(),
								getResources().getString(
										R.string.fill_all_fields),
								Toast.LENGTH_SHORT).show();
					} else {
						if((txtMobileNumber.getText().toString().trim().length()>=8))
							new CheckPerfumeTask(country_id, false).execute();
						else
							Toast.makeText(getActivity(), R.string.enter_valid_phone, Toast.LENGTH_SHORT).show();
					}
				} else {
					if (addres_idString != null)
						new CheckPerfumeTask(addres_idString, true).execute();
					else
						Toast.makeText(
								getActivity(),
								getResources().getString(
										R.string.required_shipping_address),
								Toast.LENGTH_SHORT).show();
				}

			}

		});

		if (defaultAdres.isChecked()) {
			//LabelpostCode.setVisibility(View.GONE);
			LabelpostCode.setVisibility(View.VISIBLE);
		} else {
			LabelpostCode.setVisibility(View.VISIBLE);
		}

		adresselect.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				int pos = adresselect.indexOfChild(v.findViewById(checkedId));
				switch (pos) {
				case 0:
					defAddres = true;

					updateUI(defAddres);
					//LabelpostCode.setVisibility(View.GONE);
					LabelpostCode.setVisibility(View.VISIBLE);
					break;
				case 1:
					defAddres = false;

					updateUI(defAddres);

					LabelpostCode.setVisibility(View.VISIBLE);
					break;

				}

			}

		});
		new DownloadJSONForAdress().execute();

		adressSelspinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						HashMap<String, String> resultp = adressList
								.get(position);
						LabelfirstName.setText(resultp.get("firstname"));
						LabelLastName.setText(resultp.get("lastname"));
						Labeladdress1.setText(resultp.get("address_1"));

						LabelpostCode.setText(resultp.get("postcode"));
						LabelCountry.setText(resultp.get("country"));
						LabelZone.setText(resultp.get("zone"));
						addres_idString = resultp.get("address_id");

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});

		countrySpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {

						HashMap<String, String> resultp = arraylist.get(pos);

						String idString = resultp.get("id");
						zone_url = AppConstants.ZONE_URL + idString
								+ "&language=" + getLanguage();

						System.out.println("The Country id" + idString);
						country_id = idString;
						if (idString == firstzone) {
							Log.e("Checkout frag 1", "in if");
							if (countrySpinnerTouch)
								new DownloadJSONZone().execute();

						} else {
							Log.e("Checkout frag 1", "in else");

							new DownloadJSONZone().execute();
						}

					}

					public void onNothingSelected(AdapterView<?> parent) {
					}
				});

		countrySpinner.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.e("Country Spinner", "Country Spinner Touched");
				countrySpinnerTouch = true;
				// asyncCompleteFlag = true;
				return false;
			}
		});

		zoneSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				zone_id = arraylistzone.get(position).get("id");
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		zoneSpinner.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.e("Zone Spinner", "Zone Spinner Touched");
				// asyncCompleteFlag = true;
				countrySpinnerTouch = false;
				return false;
			}
		});

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

	public void updateUI(boolean defAddres) {
		if (!defAddres) {
			def.setVisibility(View.GONE);
			cust.setVisibility(View.VISIBLE);
			txtMobileNumber.setVisibility(View.VISIBLE);
		} else {
			def.setVisibility(View.VISIBLE);
			cust.setVisibility(View.GONE);
			txtMobileNumber.setVisibility(View.VISIBLE);
		}
	}

	private void initializeView(View v) {
		def = (LinearLayout) v.findViewById(R.id.layoutdefault);
		cust = (LinearLayout) v.findViewById(R.id.layoutCustom);
		adressSelspinner = (Spinner) v.findViewById(R.id.addressspinner);
		firstNameEditText = (EditText) v.findViewById(R.id.edittextnamefn);
		LastNameEditText = (EditText) v.findViewById(R.id.edittextLastname);
		address1EditText = (EditText) v.findViewById(R.id.edittextaddressfn);
		address2EditText = (EditText) v.findViewById(R.id.edittextaddress2);
		txtMobileNumber = (EditText) v.findViewById(R.id.edittextpostcodefn);
		countrySpinner = (Spinner) v.findViewById(R.id.spinnercountryfn);
		zoneSpinner = (Spinner) v.findViewById(R.id.spinnerzonefn);
		nextButton = (Button) v.findViewById(R.id.buttonnext);
		LabelfirstName = (EditText) v.findViewById(R.id.labeTextnamefn);
		LabelLastName = (EditText) v.findViewById(R.id.labelTextLastname);
		Labeladdress1 = (EditText) v.findViewById(R.id.labelTextaddressfn);
		LabelpostCode = (EditText) v.findViewById(R.id.labelTextpostcodefn);
		LabelCountry = (EditText) v.findViewById(R.id.labelTextcountry);
		LabelZone = (EditText) v.findViewById(R.id.labelTextcountryZone);
		adresselect = (RadioGroup) v.findViewById(R.id.radioGroup1);
		defaultAdres = (RadioButton) v.findViewById(R.id.defaultSel);
		custom = (RadioButton) v.findViewById(R.id.custom);
		defaultAdres.setChecked(true);

	}

	private class DownloadJSON extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (mProgressDialog != null) {
				Log.e(TAG, "ProgresDialog in Country Task");
				mProgressDialog.show();
			}

		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				arraylist = new ArrayList<HashMap<String, String>>();
				jsonobject = JSONfunctions
						.getJSONfromURL("http://dairam.com/index.php?route=api/country/list&language="
								+ getLanguage());

			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void args) {
			try {
				if (jsonobject != null) {
					if (jsonobject.getBoolean("success")) {
						jsonarray = jsonobject.getJSONArray("country");

						for (int i = 0; i < jsonarray.length(); i++) {
							HashMap<String, String> map = new HashMap<String, String>();
							jsonobject = jsonarray.getJSONObject(i);
							map.put("id", jsonobject.getString("id"));
							map.put("name", jsonobject.getString("name"));

							arraylist.add(map);

							arrayList2.add(jsonobject.getString("name"));
						}
						// Setting Country spinner and Calling Zone Gettin
						// Task
						System.out.println("Arraylist" + arraylist);

						countryStrings = arrayList2
								.toArray(new String[arrayList2.size()]);

						ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(
								getActivity(), R.layout.spinner_items_single,
								countryStrings);
						adapter_state
								.setDropDownViewResource(R.layout.spinner_single_item);
						countrySpinner.setAdapter(adapter_state);

						HashMap<String, String> resultp = arraylist.get(0);

						String idString = resultp.get("id");

						// http://dairam.com/index.php?route=api/zone/get&country_code=17&language=1

						zone_url = AppConstants.ZONE_URL + idString
								+ "&language=" + getLanguage();
						System.out.println("The Country id" + idString);

						firstzone = idString;

						if (temp == false) {
							// Close the progress dialog
							mProgressDialog.dismiss();
							new DownloadJSONZone().execute();
						} else {
							System.out.println("Do nothing");
						}
					} else {
						Toast.makeText(getActivity(), R.string.no_countries,
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getActivity(), R.string.no_countries,
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
		}
	}

	private class DownloadJSONZone extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			temp = true;
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
				Log.e(TAG, "zone URL : " + zone_url);
				jsonobject = JSONfunctions.getJSONfromURL(zone_url);

			} catch (Exception e) {

				System.out.println("the application time out" + e.getMessage());

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void args) {

			mProgressDialog.dismiss();
			try {
				if (jsonobject != null) {
					Log.e("Json Obj in ZoneAsync", jsonobject.toString());
					// Locate the array name in JSON
					jsonarray = jsonobject.getJSONArray("zones");

					System.out.println("The result" + jsonarray.length());

					for (int i = 0; i < jsonarray.length(); i++) {
						HashMap<String, String> map = new HashMap<String, String>();
						jsonobject = jsonarray.getJSONObject(i);
						// Retrive JSON Objects
						map.put("id", jsonobject.getString("id"));
						map.put("name", jsonobject.getString("name"));

						// Set the JSON Objects into the array
						arraylistzone.add(map);

						arrayList3.add(jsonobject.getString("name"));
					}
					zoneStrings = arrayList3.toArray(new String[arrayList3
							.size()]);

					ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(
							getActivity(), R.layout.spinner_items_single,
							zoneStrings);
					adapter_state
							.setDropDownViewResource(R.layout.spinner_single_item);
					zoneSpinner.setAdapter(adapter_state);
					countrySpinnerTouch = false;
				} else {
					Toast.makeText(getActivity(), R.string.no_zones,
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}

		}
	}

	private class DownloadJSONForAdress extends AsyncTask<Void, Void, Void> {

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
				adressList = new ArrayList<HashMap<String, String>>();
				// Retrieve JSON Objects from the given URL address
				// jsonobject = JSONfunctions
				// .getJSONfromURL("http://dairam.com/index.php?route=api/address/list&id="
				// + "65");
				String mainString = "http://dairam.com/index.php?route=api/address/list&id="
						+ id;
				HttpParams params1 = new BasicHttpParams();
				HttpProtocolParams.setVersion(params1, HttpVersion.HTTP_1_1);
				HttpProtocolParams.setContentCharset(params1, "UTF-8");
				params1.setBooleanParameter("http.protocol.expect-continue",
						false);
				HttpClient httpclient = new DefaultHttpClient(params1);

				Log.e(TAG, "Addres Pic Url: " + mainString);
				HttpPost httppost = new HttpPost(mainString);
				Log.e("Main url", mainString);

				HttpResponse http_response = httpclient.execute(httppost);
				HttpEntity entity = http_response.getEntity();
				String jsonText = EntityUtils.toString(entity, HTTP.UTF_8);
				Log.i("Response", jsonText);
				jsonobject = new JSONObject(jsonText);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void args) {

			try {
				if (jsonobject != null) {

					if (!jsonobject.getBoolean("success")) {
						Log.e(TAG, "Json respons False");

						custom.setChecked(true);

					} else {
						if (!jsonobject.has("error")) {
							JSONArray jarray1 = jsonobject
									.getJSONArray("addresses");

							for (int i = 0; i < jarray1.length(); i++) {
								HashMap<String, String> map = new HashMap<String, String>();

								map.put("title", jarray1.getJSONObject(i)
										.getString("title"));
								map.put("address_id", jarray1.getJSONObject(i)
										.getString("address_id"));
								map.put("firstname", jarray1.getJSONObject(i)
										.getString("firstname"));
								map.put("lastname", jarray1.getJSONObject(i)
										.getString("lastname"));
								map.put("address_1", jarray1.getJSONObject(i)
										.getString("address_1"));
								map.put("address_2", jarray1.getJSONObject(i)
										.getString("address_2"));
								map.put("city", jarray1.getJSONObject(i)
										.getString("city"));
								map.put("postcode", jarray1.getJSONObject(i)
										.getString("telephone"));
								/*map.put("postcode", jarray1.getJSONObject(i)
										.getString("postcode"));*/
								map.put("zone", jarray1.getJSONObject(i)
										.getString("zone"));
								map.put("zone_code", jarray1.getJSONObject(i)
										.getString("zone_code"));
								map.put("country", jarray1.getJSONObject(i)
										.getString("country"));

								adressList.add(map);

								arrayList4.add(jarray1.getJSONObject(i)
										.getString("title"));
							}
							System.out.println("Arraylist" + arrayList4);
							if (arrayList4.size() > 0) {
								adresStrings = arrayList4
										.toArray(new String[arrayList4.size()]);

								ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(
										getActivity(),
										R.layout.spinner_items_single,
										adresStrings);
								adapter_state
										.setDropDownViewResource(R.layout.spinner_single_item);
								adressSelspinner.setAdapter(adapter_state);
							} else {
								custom.setChecked(true);
							}
						} else {
							Log.e(TAG, "Json response has Error");
							custom.setChecked(true);
						}
						// new DownloadJSON().execute();
					}
					new DownloadJSON().execute();
				} else {
					Log.e(TAG, "No Response form server");
					Toast.makeText(getActivity(), R.string.no_response,
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
				// }

			} catch (Exception e) {

				System.out.println("the application time out" + e.getMessage());

			}

			mProgressDialog.dismiss();
		}
	}

	public class doUploadNewAdressTask extends AsyncTask<Void, String, Void> {

		String type, result;
		boolean postStatus = false;
		JSONObject jObj = null;
		String fName, lName, aD1, aD2, post;

		InputStream istream;

		public doUploadNewAdressTask(String firstNameString,
				String lastNameString, String addString, String addString2,
				String postString) {
			this.fName = firstNameString;
			this.lName = lastNameString;
			this.aD1 = addString;
			this.aD2 = addString2;
			this.post = postString;// postcode is used as mobilenumber

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

				JSONObject objJson = new JSONObject();
				objJson.accumulate("firstname", fName);
				objJson.accumulate("lastname", lName);
				objJson.accumulate("address_1", aD1);
				objJson.accumulate("address_2", aD2);
				objJson.accumulate("postcode", "");
				objJson.accumulate("telephone", post);
				objJson.accumulate("country_id", country_id);
				objJson.accumulate("zone_id", zone_id);
				objJson.accumulate("language", "1");
				objJson.accumulate("cust_id", id);

				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						AppConstants.MY_ORDER_ADDRESS_POST_JSON_URL);
				// StringEntity entity = new StringEntity(objJson.toString());
				StringEntity entity = new StringEntity(objJson.toString(),
						HTTP.UTF_8);
				Log.i("Login Request", objJson.toString());

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
				Log.i(TAG, "Parsing POSTS Json\n" + jObj.toString());
				try {
					JSONObject resultObj = jObj.getJSONObject("Result");
					if (resultObj.getBoolean("success")) {
						Toast.makeText(getActivity(),
								"" + resultObj.getString("success_msg"),
								Toast.LENGTH_SHORT).show();
						gotoConfirmPage(resultObj.getString("shipping_address"));

					} else {

					}

				}

				catch (JSONException e) {
					e.printStackTrace();
				}

			} else {
				Toast.makeText(context, R.string.no_response,
						Toast.LENGTH_SHORT).show();
			}
			mProgressDialog.dismiss();

		}

	}

	public class CheckPerfumeTask extends AsyncTask<Void, String, Void> {

		String coraid;
		boolean operation;
		JSONObject jsonObj = null;

		InputStream istream;

		public CheckPerfumeTask(String coraid, boolean opr) {
			this.coraid = coraid;
			this.operation = opr;
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
				String checkUrl = "";
				if (operation) {
					Log.e("CheckoutFrag1st", "Existing Address");
					checkUrl = "http://dairam.com/index.php?route=api/checkproduct/get&id="
							+ id
							+ "&address_id="
							+ coraid
							+ "&language="
							+ getLanguage();
				} else {
					Log.e("CheckoutFrag1st", "New Address");
					checkUrl = "http://dairam.com/index.php?route=api/checkproduct/get&id="
							+ id
							+ "&country_id="
							+ coraid
							+ "&language="
							+ getLanguage();
				}
				Log.e("Perfume Check URL", checkUrl);
				jsonObj = JSONfunctions.getJSONfromURL(checkUrl);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mProgressDialog.dismiss();
			try {
				if (jsonObj != null) {
					if (jsonObj.getBoolean("success")) {
						if (operation)
							gotoConfirmPage(addres_idString);
						else {
							Log.e("ChecoutFrag1st", "Here");
							new doUploadNewAdressTask(firstNameEditText
									.getText().toString(), LastNameEditText
									.getText().toString(), address1EditText
									.getText().toString(), address2EditText
									.getText().toString(), txtMobileNumber
									.getText().toString()).execute();
						}
					} else {
						DialogFragPerfumeNotification dialog;

						String message = jsonObj.getString("error");

						if (operation) {
							dialog = new DialogFragPerfumeNotification(message,
									discount, "OLD", addres_idString);
						} else {
							dialog = new DialogFragPerfumeNotification(message,
									discount, "NEW", firstNameEditText
											.getText().toString(),
									LastNameEditText.getText().toString(),
									address1EditText.getText().toString(),
									address2EditText.getText().toString(),
									txtMobileNumber.getText().toString(),
									country_id, zone_id, getLanguage(), id);
						}

						dialog.show(getFragmentManager(), "My Dialog");

					}
				} else {
					Toast.makeText(getActivity(), R.string.no_response,
							Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
			}
		}

	}

	private void gotoConfirmPage(String shiping_ad) {

		fr = new CheckoutFragmentSecondStep();
		Bundle bundle = new Bundle();
		bundle.putString("shipping_address", shiping_ad);
		bundle.putString("discount", discount);
		fr.setArguments(bundle);
		Log.e("Discount", "In CheckoutFrag 1 Discount : " + discount);
		FragmentManager fm = ((Activity) context).getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_place, fr);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonbackfnister:

		case R.id.textViewbackcheckout:

			LoadHomePage();
			break;

		default:
			break;
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

	// telephoneEditText;
	public void setupUI(View view, boolean isHeading) {
		Typeface myTypeface;
		if (Util.getLanguage(getActivity()).equals("2")) {
			myTypeface = Typeface.createFromAsset(getActivity().getAssets(),
					"fonts/HELVETICANEUELTARABIC-ROMAN.TTF");
		} else {
			myTypeface = Typeface.createFromAsset(getActivity().getAssets(),
					"fonts/Georgia.ttf");
		}
		backTextView.setTypeface(myTypeface);
		firstNameEditText.setTypeface(myTypeface);
		LastNameEditText.setTypeface(myTypeface);
		address1EditText.setTypeface(myTypeface);
		address2EditText.setTypeface(myTypeface);
		txtMobileNumber.setTypeface(myTypeface);

	}

	Typeface getRegularTypeFace() {
		return Typeface.createFromAsset(context.getAssets(),
				context.getString(R.string.regular_typeface));
	}

	public void LoadHomePage() {
		fr = new CartFragment();
		FragmentManager fm = getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_place, fr);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();

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
						LoadHomePage();
						return true;
					}
				}
				return false;
			}
		});
		super.onActivityCreated(savedInstanceState);
	}
}
