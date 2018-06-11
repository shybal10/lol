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
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.SignUpEvent;
import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.dialogfragment.ReadTermsAndConditions;
import com.dairam.android.fragments.activity.interfaces.OnAgreeTermsandConditions;
import com.dairam.android.fragments.activity.interfaces.listenerHideView;
import com.dairam.android.fragments.activity.parserfiles.JSONfunctions;
import com.dairam.android.fragments.activity.utillities.Util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterFragment extends Fragment {
	OnAgreeTermsandConditions isChecked;
	EditText firstNameEditText, lastNameEditText, emailEditText;
	EditText telephoneEditText, passwordEditText, reenterPasswordEditText;

	CheckBox agreeTermsCheckBox;
	String TermsandCondData;

	String firstName, lastName, email, telephone, password, reenterpassword;

	Boolean isValidEntry = false;

	Button registerButton;

	Button backButton;
	TextView backTextView,readTerms,readTermsCheckArabic;

	JSONObject jsonobject;
	JSONArray jsonarray;
	ProgressDialog mProgressDialog;
	ArrayList<HashMap<String, String>> arraylist;
	Context contex;

	String regexStr = "^[+]?[0-9]{10,13}$";

	// http://dairam.com/index.php?route=api/register/get&language=1&lastname=zxcas&email=tesasdsdft@
	// webna.in&telephone=12312322&password=123123&confirm=123123&firstname=asd

	// String mainurl =
	// "http://dairam.com/index.php?route=api/register/get&language=1&lastname=";
	String mainurl = "http://dairam.com/index.php?route=api/register/get&language=";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_register, container, false);

		contex = getActivity();
		SharedPreferences sp1 = getActivity().getSharedPreferences("tag",
				Context.MODE_PRIVATE);
		String p = "fragmentregister";
		Editor editor = sp1.edit();
		editor.putString("act", p);
		editor.commit();

		firstNameEditText = (EditText) v.findViewById(R.id.edittextnamereg);
		lastNameEditText = (EditText) v.findViewById(R.id.edittextlastnamereg);
		emailEditText = (EditText) v.findViewById(R.id.edittextemailreg);
		telephoneEditText = (EditText) v
				.findViewById(R.id.edittexttelephonereg);
		passwordEditText = (EditText) v.findViewById(R.id.edittextpasswordreg);
		reenterPasswordEditText = (EditText) v
				.findViewById(R.id.edittextrepasswordreg);
		registerButton = (Button) v.findViewById(R.id.buttonsubmitreg);
		agreeTermsCheckBox = (CheckBox) v.findViewById(R.id.checkBox1reg);

		backButton = (Button) v.findViewById(R.id.buttonbackregister);
		backTextView = (TextView) v.findViewById(R.id.textViewbackregister);
		readTermsCheckArabic = (TextView)v.findViewById(R.id.lblReadTerms);
		
		readTerms=(TextView) v.findViewById(R.id.lblReadTerms);
		readTerms.setText(Html.fromHtml("<u>"+getActivity().getString(R.string.readterms)+"</u>"));
		Typeface fontFace = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/LibreBaskerville-Regular.otf");
		backTextView.setTypeface(fontFace);

		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				callLoginPage();
			}
		});

		backTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				callLoginPage();
			}
		});
		readTerms.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				ReadTermsAndConditions myDialogFragmentTersmRead = new ReadTermsAndConditions();
				myDialogFragmentTersmRead.newInstance(TermsandCondData,isChecked);
				myDialogFragmentTersmRead.show(getFragmentManager(),
						"myDialogFragmentReadTerms");
				
			}
		});

		registerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				firstName = firstNameEditText.getText().toString();
				lastName = lastNameEditText.getText().toString();
				email = emailEditText.getText().toString();
				telephone = telephoneEditText.getText().toString();
				password = passwordEditText.getText().toString();
				reenterpassword = reenterPasswordEditText.getText().toString();

				if (checkNullFields(firstName, lastName, email, telephone,
						password, reenterpassword)) {
					if (emailValidator(email)) {
						if (password.length() > 4 && password.length() < 16) {
							if (password.equals(reenterpassword)) {
								if (telephone.length() < 10
										|| telephone.length() > 13
										|| telephone.matches(regexStr) == false) {
									Toast.makeText(
											getActivity(),
											getResources().getString(
													R.string.enter_valid_phone),
											Toast.LENGTH_SHORT).show();

								} else {
									if (agreeTermsCheckBox.isChecked() == true) {
										mainurl = AppConstants.REGISTER_NEW_USER;
										mainurl = mainurl + getLanguage()
												+ "&lastname=" + lastName
												+ "&email=" + email
												+ "&telephone=" + telephone
												+ "&password=" + password
												+ "&confirm=" + reenterpassword
												+ "&firstname=" + firstName;
										new DownloadJSON().execute();
									} else {
										Toast.makeText(
												getActivity(),
												getResources().getString(
														R.string.accept_terms),
												Toast.LENGTH_SHORT).show();
									}
								}
							} else
								Toast.makeText(
										getActivity(),
										getResources().getString(
												R.string.password_mismatch),
										Toast.LENGTH_SHORT).show();
						} else {
							if (getLanguage().equals("1"))
								Toast.makeText(
										getActivity(),
										"Password should have atleast 5 characters",
										Toast.LENGTH_SHORT).show();
							else
								Toast.makeText(
										getActivity(),
										"يجب ان تتكون كلمة المرور من 5 أحرف على الأقل ",
										Toast.LENGTH_SHORT).show();
						}
					} else
						Toast.makeText(
								getActivity(),
								getResources().getString(
										R.string.enter_valid_email),
								Toast.LENGTH_SHORT).show();
				}

				/*
				 * Boolean status = isValidDetails(firstName, lastName, email,
				 * telephone, password, reenterpassword);
				 * 
				 * if (status == true) {
				 * 
				 * if (agreeTermsCheckBox.isChecked() == true) {
				 * 
				 * if(((telephone.equals(null))||(telephone.equals("")))) {
				 * Toast.makeText(getActivity(),
				 * getResources().getString(R.string.enter_valid_phone),
				 * Toast.LENGTH_SHORT).show(); } else{ if(telephone.length()<10
				 * || telephone.length()>13 ||
				 * telephone.matches(regexStr)==false ) {
				 * Toast.makeText(getActivity(),
				 * getResources().getString(R.string.enter_valid_phone),
				 * Toast.LENGTH_SHORT).show(); }
				 * 
				 * else {
				 * //http://dairam.com/index.php?route=api/register/get&language
				 * =
				 * 1&lastname=zxcas&email=tesasdsdft@webna.in&telephone=12312322
				 * &password=123123&confirm=123123&firstname=asd
				 * 
				 * //
				 * "http://dairam.com/index.php?route=api/register/get&language=1&lastname="
				 * ; // String lang = getLanguage();
				 * 
				 * mainurl = mainurl+
				 * getLanguage()+"&lastname="+lastName+"&email="
				 * +email+"&telephone="
				 * +telephone+"&password="+password+"&confirm="
				 * +reenterpassword+"&firstname="+firstName; new
				 * DownloadJSON().execute(); } }
				 * 
				 * }
				 * 
				 * else{ Toast.makeText(getActivity(),
				 * "Accept Terms and Condition", Toast.LENGTH_SHORT).show(); }
				 * 
				 * 
				 * }else { if(getLanguage().equals("1"))
				 * Toast.makeText(getActivity(), "Passwords not matching",
				 * Toast.LENGTH_SHORT).show(); else
				 * Toast.makeText(getActivity(), "عدم تطابق في كلمة المرور ",
				 * Toast.LENGTH_SHORT).show(); }
				 */

			}
		});
		
		switch (getLanguage()) {
		case "en":
			
			break;

		default:
			
			setupUI(v, false);
			break;
		}
		
		new GetTermsTask().execute();
		isChecked=new OnAgreeTermsandConditions() {
			
			@Override
			public void onChecked(boolean checked) {
				agreeTermsCheckBox.setChecked(checked);				
			}
		};
		return v;
	}

	private boolean checkNullFields(String name, String lastName, String email,
			String telephone, String password, String repass) {
		if ((name.length() > 0) || (lastName.length() > 0)
				|| (email.length() > 0) || (telephone.length() > 0)
				|| (password.length() > 0) || (repass.length() > 0)) {
			return true;
		} else {
			Toast.makeText(getActivity(),
					getResources().getString(R.string.fill_all_fields),
					Toast.LENGTH_SHORT).show();
			return false;
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

	private void callLoginPage() {
		Fragment fr = new LoginFragment();
		Bundle bundle = new Bundle();
		bundle.putString("title", "Fromregister");
		bundle.putString("ID", "0");
		FragmentManager fm = getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_place, fr);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}

	public Boolean isValidDetails(String name, String lastName, String email,
			String telephone, String password, String repass) {

		Boolean isallDetailsValid = false;

		if ((name.length() > 0) && (lastName.length() > 0)
				&& (email.length() > 0) && (telephone.length() > 0)
				&& (password.length() > 0) && (repass.length() > 0)) {

			isallDetailsValid = emailValidator(email);

			if (isallDetailsValid == true) {

				if (password.length() > 3 && password.length() < 21) {
					if (password.equals(repass)) {

						return true;
					}

					else {
						System.out.println("Password mismatch");
					}
				} else {
					if (getLanguage().equals("1"))
						Toast.makeText(getActivity(),
								"Password should have atleast characters",
								Toast.LENGTH_SHORT).show();
					else
						Toast.makeText(
								getActivity(),
								"يجب ان تتكون كلمة المرور من 5 أحرف على الأقل ",
								Toast.LENGTH_SHORT).show();
				}
			}

			else {
				Toast.makeText(getActivity(),
						getResources().getString(R.string.enter_valid_email),
						Toast.LENGTH_SHORT).show();
				return false;
			}
		}

		return false;
	}

	/*
	 * Validating email
	 */
	public boolean emailValidator(String email) {
		Pattern pattern;
		Matcher matcher;
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) activity;
		listenerHideView.hide();
		listenerHideView.dairamBar();
	}

	public JSONObject postData() {
		Log.e("The url is", mainurl);
		HttpParams params1 = new BasicHttpParams();
		HttpProtocolParams.setVersion(params1, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params1, "UTF-8");
		params1.setBooleanParameter("http.protocol.expect-continue", false);
		HttpClient httpclient = new DefaultHttpClient(params1);
		HttpPost httppost = new HttpPost(mainurl);
		JSONObject jsonObj = null;
		try {
			HttpResponse http_response = httpclient.execute(httppost);

			HttpEntity entity = http_response.getEntity();
			String jsonText = EntityUtils.toString(entity, HTTP.UTF_8);
			Log.e("Response", jsonText);
			jsonObj = new JSONObject(jsonText);
		} catch (Exception e) {
			System.out.println("");
		}
		return jsonObj;
	}

	private class DownloadJSON extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Create a progressdialog
			mProgressDialog = new ProgressDialog(getActivity(), R.style.MyTheme);
			mProgressDialog.setCancelable(false);
			mProgressDialog
					.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
			mProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				/*arraylist = new ArrayList<HashMap<String, String>>();
				Log.e("Registration URL", mainurl);
				jsonobject = JSONfunctions.getJSONfromURL(mainurl);
				Log.e("Register Response", jsonobject.toString());*/
				jsonobject = postData();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void args) {

			try {
				if (jsonobject != null) {
					// Locate the array name in JSON
					if (jsonobject.has("success_msg")) {

						Answers.getInstance().logSignUp(new SignUpEvent()
								.putMethod("Dairam App Signup")
								.putSuccess(true));
						Log.e("Success msg",
								jsonobject.getString("success_msg"));
						if (getLanguage().equals("1"))
							Toast.makeText(
									getActivity(),
									//"Registered Successfully.Please Check Your Email to Activate Your Account",
									"Successful Registration, Happy Shopping",
									Toast.LENGTH_SHORT).show();
						else
							Toast.makeText(getActivity(),
									//"تم التسجيل بنجاح. يرجى تفقد بريدك الالكتروني لتفعيل الحساب",
									"تم التسجيل بنجاح. استمتع بالتسوق ",
									Toast.LENGTH_SHORT).show();

						// Toast.makeText(getActivity(),
						// "Registartion successfull",
						// Toast.LENGTH_SHORT).show();
						/*Fragment fr = new FragmentOne();
						FragmentManager fm = getFragmentManager();
						FragmentTransaction fragmentTransaction = fm
								.beginTransaction();
						fragmentTransaction.replace(R.id.fragment_place, fr);
						fragmentTransaction.addToBackStack(null);
						fragmentTransaction.commit();*/
						
						Fragment fr = new FragmentCategories();
						FragmentManager fm = getFragmentManager();
						FragmentTransaction fragmentTransaction = fm.beginTransaction();
						fragmentTransaction.replace(R.id.fragment_place, fr);
						fragmentTransaction.addToBackStack(null);
						fragmentTransaction.commit();
					} else if (jsonobject.has("reg_warning")) {
						Log.e("Warning msg",
								jsonobject.getString("reg_warning"));
						Toast.makeText(getActivity(),
								jsonobject.getString("reg_warning"),
								Toast.LENGTH_SHORT).show();
					} else {
						if (getLanguage().equals("1"))
							Toast.makeText(getActivity(),
									"Registration Failed. Please try again",
									Toast.LENGTH_SHORT).show();
						else
							Toast.makeText(
									getActivity(),
									"لم تتم عملية التسجيل . يرجى المحاولة فيما بعد ",
									Toast.LENGTH_SHORT).show();
					}
					// jsonobject = jsonobject.getJSONObject("success_msg");

					// Log.e("THE RESULT>>>>>>>>>>", jsonobject.toString());
				}
				else{
					Toast.makeText(getActivity(), R.string.no_response, Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();

			}
			mProgressDialog.dismiss();

		}
	}
	private class GetTermsTask extends AsyncTask<Void, Void, Void> {
		JSONObject  jsonObject;
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
			String TermsUrl = AppConstants.TERMS_CONDTN_URL + Util.getLanguage(getActivity());
			TermsUrl += "&info_id=5&currency=KWD";
			try {
				jsonObject = JSONfunctions.getJSONfromURL(TermsUrl);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			try {
				if(jsonObject != null){
					
					if(jsonObject.getBoolean("success")){
						JSONArray jsonArray = jsonObject.getJSONArray("information");
						jsonObject = jsonArray.getJSONObject(0);
						TermsandCondData = jsonObject.getString("description");
					}else{
						Toast.makeText(getActivity(), "Sorry.. No Data Available..", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(getActivity(), R.string.no_response, Toast.LENGTH_SHORT).show();
				}
				
			} catch (Exception e) {
				Toast.makeText(getActivity(), R.string.no_response, Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
			mProgressDialog.dismiss();
		}
	}

	public void setupUI(View view, boolean isHeading) {
		  Typeface myTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/HELVETICANEUELTARABIC-ROMAN.TTF");
		  if(Util.getLanguage(getActivity()).equals("2")){
		     TextView myTextView = (TextView)view.findViewById(R.id.textView1);
		     myTextView.setTypeface(myTypeface);
		     agreeTermsCheckBox.setTypeface(myTypeface);
		     readTermsCheckArabic.setTypeface(myTypeface);
		   /*  backTextView.setTypeface(myTypeface);*/
		     firstNameEditText.setTypeface(myTypeface);
		     lastNameEditText.setTypeface(myTypeface);
		     emailEditText.setTypeface(myTypeface);
		     telephoneEditText.setTypeface(myTypeface);
		     passwordEditText.setTypeface(myTypeface);
		     reenterPasswordEditText.setTypeface(myTypeface);
		  }

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
	                        	 callLoginPage();
	                         return true;
	                         }
	                     }
	                     return false;
	                 }
	             });
	   super.onActivityCreated(savedInstanceState);
	  }
}
