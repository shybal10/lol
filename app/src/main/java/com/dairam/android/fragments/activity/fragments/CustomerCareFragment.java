package com.dairam.android.fragments.activity.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.DialogActivity;
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.interfaces.listenerHideView;
import com.dairam.android.fragments.activity.utillities.Util;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerCareFragment extends Fragment {

	Button submitButton;
	EditText nameEditText, phoneEditText, emailEditText, contetEditText;

	Spinner subjectSelectionSpinner;

	String nameString, phoneString, emailString, contentString;

	String subjectString;

	Boolean isAllValuePresent;

	Button backButton;
	TextView backTextView;

	ProgressDialog mProgressDialog;
	ArrayList<HashMap<String, String>> arraylist;

	String lan;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		SharedPreferences sp = getActivity().getSharedPreferences("lan",
				Context.MODE_PRIVATE);
		lan = sp.getString("lan", "en");
		setLanguage(lan);

		SharedPreferences sp1 = getActivity().getSharedPreferences("tag",
				Context.MODE_PRIVATE);
		Editor editor = sp1.edit();
		editor.putString("act", "customercare");
		editor.commit();
		View v = inflater.inflate(R.layout.activity_contact, container, false);

		submitButton = (Button) v.findViewById(R.id.buttonsubmitcustomercare);
		nameEditText = (EditText) v.findViewById(R.id.edittextnamecc);
		phoneEditText = (EditText) v.findViewById(R.id.editTextphonecc);
		emailEditText = (EditText) v.findViewById(R.id.editTextemailcc);
		contetEditText = (EditText) v.findViewById(R.id.editTextmessagecc);

		subjectSelectionSpinner = (Spinner) v
				.findViewById(R.id.spinnersubjectcc);
		nameEditText.setFilters(new InputFilter[] { new InputFilter() {
			public CharSequence filter(CharSequence src, int start, int end,
					Spanned dst, int dstart, int dend) {
				if (src.equals(""))
				{
					return src;
				}
				if (src.toString().matches("[a-zA-Z0-9 ]+")) {
					return src;
				}
				return "";
			}

		} });

		List<String> list = new ArrayList<String>();

		switch (lan) {
		case "en":
			list.add("General Information");
			list.add("Order Status");
			list.add("Problems with my order");
			list.add("My Account");
			list.add("Wholesale Inquiries");
			list.add("Website / technical inquiries");
			list.add("Others");
			break;

		default:
			list.add("معلومات عامة");
			list.add("حالة الطلب");
			list.add("مشاكل مع طلبي");
			list.add("حسابي");
			list.add("استفسارات الجملة");
			list.add("الموقع / الاستفسارات الفنية");
			list.add(" غير ذلك ");
			break;
		}

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
				getActivity(), R.layout.spinner_items_single, list);

		dataAdapter
				.setDropDownViewResource(R.layout.spinner_single_item_custcare);
		subjectSelectionSpinner.setAdapter(dataAdapter);
		backButton = (Button) v.findViewById(R.id.buttonbackcontact);
		backTextView = (TextView) v.findViewById(R.id.textViewbackcontact);

		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				BackEventToHome();
			}
		});		
		backTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				BackEventToHome();
			}
		});

		Typeface fontFace = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/LibreBaskerville-Regular.otf");
		backTextView.setTypeface(fontFace);

		subjectSelectionSpinner
				.setOnItemSelectedListener(new CustomOnItemSelectedListener());

		submitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {

				nameString = nameEditText.getText().toString();
				phoneString = phoneEditText.getText().toString();
				emailString = emailEditText.getText().toString();
				contentString = contetEditText.getText().toString();

				isAllValuePresent = isVAlidDetails(nameString, phoneString,emailString, contentString);

				if (isAllValuePresent == true) {
					String subjectString = subjectSelectionSpinner
							.getSelectedItem().toString();
					new GiveCustomerCareMessageTask(nameString, phoneString,
							emailString, subjectString, contentString)
							.execute();
				}
				else {
				}
			}
		});

		switch (lan) {
		case "en":
			break;

		default:
			setupUI(v, false);
			break;
		}
		return v;
	}

	public void BackEventToHome() {
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

	public Boolean isVAlidDetails(String name, String phone, String email,
			String content) {

		Boolean returnValue = false;
		if ((name.length() > 0) && (phone.length() > 0) && (email.length() > 0)
				&& (content.length() > 0)) {

			returnValue = emailValidator(email);
			if (returnValue == true) {
				String regexStr = "^[+]?[0-9]{8,12}$";

				// Confirming that the fields are not empty.
				if (((phone.equals(null)) || (phone.equals("")))) {
					// if(getLanguage().equals("1"))
					Toast.makeText(
							getActivity(),
							getResources()
									.getString(R.string.enter_valid_phone),
							Toast.LENGTH_SHORT).show();
					/*
					 * else Toast.makeText(getActivity(), "أدخل رقم هاتف صالح",
					 * Toast.LENGTH_SHORT).show();
					 */
					return false;
				} else if (phone.length() < 8 || phone.length() > 12
						|| phone.matches(regexStr) == false) {
					Toast.makeText(
							getActivity(),
							getResources()
									.getString(R.string.enter_valid_phone),
							Toast.LENGTH_SHORT).show();

					return false;
				} else {
					return true;
				}
			}

			else {

				Toast.makeText(getActivity(),
						getResources().getString(R.string.enter_valid_email),
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(getActivity(),
					getResources().getString(R.string.fill_all_fields),
					Toast.LENGTH_SHORT).show();

		}

		return returnValue;
	}

	public boolean emailValidator(String email) {
		Pattern pattern;
		Matcher matcher;
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public class CustomOnItemSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			subjectString = parent.getItemAtPosition(pos).toString();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) activity;
		listenerHideView.reversePromoItemHideTopbar();
		listenerHideView.hide();
		listenerHideView.dairamBar();
	}

	private void setLanguage(String lang) {

		String languageToLoad = lang;
		Locale locale = new Locale(languageToLoad);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getActivity().getResources().updateConfiguration(config, null);

	}

	public class GiveCustomerCareMessageTask extends
			AsyncTask<Void, String, Void> {
		String type, result;
		// boolean postStatus = false;
		JSONObject jObj = null;
		String Cname, phone, email, subject, message;

		InputStream istream;

		public GiveCustomerCareMessageTask(String name, String phone,
				String email, String subject, String message) {
			this.Cname = name;
			this.phone = phone;
			this.email = email;
			this.subject = subject;
			this.message = message;

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
			try {
				JSONObject objJson = new JSONObject();
				objJson.accumulate("name", Cname);
				objJson.accumulate("phone", phone);
				objJson.accumulate("email", email);
				objJson.accumulate("email_subject", subject);
				objJson.accumulate("enquiry", message);
				objJson.accumulate("language", getLanguage());
				// Log.e("Updload Request", ""+objJson.names().length());
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						AppConstants.UPLOAD_CONTACTUS_URL);
				// StringEntity entity = new StringEntity(objJson.toString());
				StringEntity entity = new StringEntity(objJson.toString(),
						HTTP.UTF_8);
				Log.e("Updload Request", objJson.toString());

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
					Log.e("Upload Result", result);
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
				Log.i("CustomerCareFrag",
						"Parsing POSTS Json\n" + jObj.toString());
				try {
					JSONObject resultObj = jObj;
					if (resultObj.getString("success").equals("true")) {
						// Toast.makeText(getActivity(),
						// "Your message has been sent successfully. We will be in touch with you soon",
						// Toast.LENGTH_SHORT).show();
						nameEditText.setText("");
						phoneEditText.setText("");
						emailEditText.setText("");
						contetEditText.setText("");
						subjectSelectionSpinner.setSelection(0);
						nameEditText.requestFocus();

						Intent intent = new Intent(getActivity(),
								DialogActivity.class);
						intent.putExtra("msg",
								resultObj.getString("success_msg"));
						startActivity(intent);
					} else {
						Toast.makeText(
								getActivity(),
								"Your message has not been sent successfully. Please try again",
								Toast.LENGTH_SHORT).show();
					}
				}

				catch (JSONException e) {
					e.printStackTrace();
				}

			} else {
				Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT)
						.show();
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

	/*
	 * public class doUploadNewAdressTask extends AsyncTask<Void, String, Void>
	 * {
	 * 
	 * String type, result; boolean postStatus = false; JSONObject jObj = null;
	 * String fName;
	 * 
	 * InputStream istream;
	 * 
	 * public doUploadNewAdressTask(String firstNameString) { this.fName =
	 * firstNameString;
	 * 
	 * }
	 * 
	 * @Override protected void onPreExecute() {
	 * 
	 * super.onPreExecute(); mProgressDialog = new ProgressDialog(getActivity(),
	 * R.style.MyTheme); mProgressDialog.setCancelable(false);
	 * mProgressDialog.setProgressStyle
	 * (android.R.style.Widget_ProgressBar_Small); mProgressDialog.show(); }
	 * 
	 * @Override protected Void doInBackground(Void... params) {
	 * 
	 * arraylist = new ArrayList<HashMap<String,String>>();
	 * 
	 * try { JSONObject objJson = new JSONObject(); objJson.accumulate("email",
	 * emailString); objJson.accumulate("name", nameString);
	 * objJson.accumulate("email_subject", subjectString);
	 * objJson.accumulate("enquiry",contentString );
	 * 
	 * HttpClient httpClient = new DefaultHttpClient(); HttpPost httppost = new
	 * HttpPost(AppConstants.album_list_url); StringEntity entity = new
	 * StringEntity(objJson.toString()); Log.i("Login Request",
	 * objJson.toString());
	 * 
	 * httppost.setEntity(entity); httppost.setHeader("Accept",
	 * "application/json"); httppost.setHeader("Content-type",
	 * "application/json"); HttpResponse response =
	 * httpClient.execute(httppost); istream =
	 * response.getEntity().getContent();
	 * 
	 * if (istream != null) {
	 * 
	 * BufferedReader bufferedReader = new BufferedReader(new
	 * InputStreamReader(istream)); String line = ""; result = ""; while ((line
	 * = bufferedReader.readLine()) != null)
	 * 
	 * result += line;
	 * 
	 * istream.close();
	 * 
	 * jObj = new JSONObject(result);
	 * 
	 * Log.i("Login Result", result); } else { result = null;
	 * Log.i("Input Stream", "Null"); }
	 * 
	 * } catch (JSONException e) { e.printStackTrace(); } catch
	 * (UnsupportedEncodingException e) { e.printStackTrace(); } catch
	 * (ClientProtocolException e) { e.printStackTrace(); } catch (IOException
	 * e) { e.printStackTrace(); }
	 * 
	 * return null; }
	 * 
	 * @Override protected void onPostExecute(Void result) { try { // Locate the
	 * array name in JSON jsonarray = jObj.getJSONArray("Album_Photos_List");
	 * 
	 * for (int i = 0; i < jsonarray.length(); i++) { HashMap<String, String>
	 * map = new HashMap<String, String>(); jObj = jsonarray.getJSONObject(i);
	 * imagePaths.add(jObj.getString("PhotoImage")); // Retrive JSON Objects
	 * map.put("PhotoImage", jObj.getString("PhotoImage")); // Set the JSON
	 * Objects into the array arraylist.add(map);
	 * 
	 * } } catch (JSONException e) { Log.e("Error", e.getMessage());
	 * e.printStackTrace(); }
	 * 
	 * AlbumGridViewAdapter adapter = new AlbumGridViewAdapter(getActivity(),
	 * imagePaths,album_id); albumGridView.setAdapter(adapter);
	 * mProgressDialog.dismiss();
	 * 
	 * } }
	 */
	public void setupUI(View view, boolean isHeading) {
		Typeface myTypeface;
		if (Util.getLanguage(getActivity()).equals("2")) {
			myTypeface = Typeface.createFromAsset(getActivity().getAssets(),
					"fonts/HELVETICANEUELTARABIC-ROMAN.TTF");
		} else {
			myTypeface = Typeface.createFromAsset(getActivity().getAssets(),
					"fonts/Georgia.ttf");
		}
		TextView myTextView = (TextView) view.findViewById(R.id.textView1);
		myTextView.setTypeface(myTypeface);
		backTextView.setTypeface(myTypeface);
		nameEditText.setTypeface(myTypeface);
		phoneEditText.setTypeface(myTypeface);
		emailEditText.setTypeface(myTypeface);
		contetEditText.setTypeface(myTypeface);

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
						BackEventToHome();
						return true;
					}
				}
				return false;
			}
		});
		super.onActivityCreated(savedInstanceState);
	}
}
