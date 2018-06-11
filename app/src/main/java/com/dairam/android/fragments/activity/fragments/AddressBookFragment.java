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
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.adapter.AdressListviewAdapter;
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.interfaces.listenerHideView;
import com.dairam.android.fragments.activity.parserfiles.JSONfunctions;
import com.dairam.android.fragments.activity.utillities.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;




public class AddressBookFragment extends Fragment {

	Button backButton, addnewButton;
	TextView headerTextView;
	ListView addressListView;

	Fragment fr;
	LinearLayout mainlay;

	// OpenHelper db_obj;
	ProgressDialog mProgressDialog;
	SharedPreferences sp;

	static final String addressone = "addressone";
	static final String addresstwo = "addresstwo";
	static final String addressthree = "addressthree";
	static final String addressfour = "addressfour";
	static final String addressfive = "addressfive";
	static final String addressId = "addressid";

	String PickAddressBookURL = "";

	String addressLine1String, addressLine2String, addressLine3String,
			addressLine4String, addressLine5String, addressLineIdString;

	ArrayList<HashMap<String, String>> ADDRESS_DATA = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> ADDRESS_DATA_LISTER = new ArrayList<HashMap<String, String>>();

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_addressbook, container,
				false);

		sp = getActivity()
				.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
		final listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) getActivity();

		backButton = (Button) v.findViewById(R.id.buttonbackaddressbook);
		headerTextView = (TextView) v.findViewById(R.id.textViewbackadressbook);

		addnewButton = (Button) v.findViewById(R.id.buttonaddressbook);
		addressListView = (ListView) v.findViewById(R.id.listViewaddressbook);

		mainlay = (LinearLayout) v.findViewById(R.id.addresslinlay);

		Typeface fontFace = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/LibreBaskerville-Regular.otf");
		headerTextView.setTypeface(fontFace);

		SharedPreferences sp1 = getActivity().getSharedPreferences("tag",
				Context.MODE_PRIVATE);
		String p = "fragmentaddressbook";
		Editor editor = sp1.edit();
		editor.putString("act", p);
		editor.commit();

		String custId = sp.getString("id", "");

		PickAddressBookURL = AppConstants.PICK_ADDRESSBOOOK_URL;

		PickAddressBookURL = PickAddressBookURL + custId;
		new RetrieveAddressTask().execute();
		/*
		 * try{ ADDRESS_DATA = db_obj.address_Records();
		 * 
		 * if (ADDRESS_DATA.size() > 0) { Log.e("ARRAY", "not null");
		 * 
		 * for(int i =0;i<ADDRESS_DATA.size();i++) { HashMap<String, String> map
		 * = ADDRESS_DATA.get(i); HashMap<String, String> map1 = new
		 * HashMap<String, String>();
		 * 
		 * 
		 * addressLine1String = map.get("addressone"); addressLine2String =
		 * map.get("addresstwo"); addressLine3String = map.get("addressthree");
		 * addressLine4String = map.get("addressfour"); addressLine5String =
		 * map.get("addressfive");
		 * 
		 * map1.put ("ADD1",addressLine1String); map1.put
		 * ("ADD2",addressLine2String); map1.put ("ADD3",addressLine3String);
		 * map1.put ("ADD4",addressLine4String); map1.put
		 * ("ADD5",addressLine5String);
		 * 
		 * ADDRESS_DATA_LISTER.add (map1); } }
		 * 
		 * AdressListviewAdapter adapter = new
		 * AdressListviewAdapter(getActivity(), ADDRESS_DATA_LISTER);
		 * addressListView.setAdapter(adapter);
		 * 
		 * }catch(Exception e){ System.out.println("Exception"); }
		 */
		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				callToMyAccount();
				// listenerHideView.closeDrawer();
			}
		});

		headerTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				callToMyAccount();
			}
		});

		addnewButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// fr = new AddNewAddressInAddressBook();
				fr = new addNewAddressFragment();
				FragmentManager fm = getFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
				fragmentTransaction.replace(R.id.fragment_place, fr);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
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
		super.onAttach(activity);
		listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) activity;
		listenerHideView.promoItemHideTopbar();
		System.out.println("Enters into the fragment");
	}

	public void callToMyAccount() {
		fr = new MyAccountItemFragment();
		FragmentManager fm = getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_place, fr);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}

	class RetrieveAddressTask extends AsyncTask<Void, Void, Void> {
		JSONObject jsonobject;
		JSONArray jsonArray;
		ArrayList<HashMap<String, String>> arraylist;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(mProgressDialog != null)
				mProgressDialog.dismiss();
			
			mProgressDialog = new ProgressDialog(getActivity(), R.style.MyTheme);
			mProgressDialog.setCancelable(false);
			mProgressDialog
					.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
			mProgressDialog.show();
			
		}

		@Override
		protected Void doInBackground(Void... params) {
			arraylist = new ArrayList<HashMap<String, String>>();
			Log.e("AddressBookFrag", PickAddressBookURL);
			jsonobject = JSONfunctions.getJSONfromURL(PickAddressBookURL);
			return null;
		}

		@Override
		protected void onPostExecute(Void args) {
			try {
				if (jsonobject != null) {
					Log.e("AddressBookFrag","Retreive Address : "+jsonobject.toString());
					if (jsonobject.getBoolean("success")) {
						jsonArray = jsonobject.getJSONArray("addresses");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject JObjInner = new JSONObject();

							JObjInner = jsonArray.getJSONObject(i);
							HashMap<String, String> map = new HashMap<String, String>();

							String addr1 = JObjInner.getString("address_1");
							String addr2 = JObjInner.getString("address_2");
							String addr3 = JObjInner.getString("telephone");//telephone
							String addr4 = JObjInner.getString("country");
							String addr5 = JObjInner.getString("zone");
							String addrId = JObjInner.getString("address_id");

							map.put(addressone, addr1);
							map.put(addresstwo, addr2);
							map.put(addressthree, addr3);//telephone
							map.put(addressfour, addr4);
							map.put(addressfive, addr5);
							map.put(addressId, addrId);

							arraylist.add(map);
						}

						if (arraylist.size() > 0) {
							for (int i = 0; i < arraylist.size(); i++) {
								HashMap<String, String> map = arraylist.get(i);
								HashMap<String, String> map1 = new HashMap<String, String>();

								addressLine1String = map.get("addressone");
								addressLine2String = map.get("addresstwo");
								addressLine3String = map.get("addressthree");//telephone
								addressLine4String = map.get("addressfour");
								addressLine5String = map.get("addressfive");
								addressLineIdString = map.get("addressid");

								map1.put("ADD1", addressLine1String);
								map1.put("ADD2", addressLine2String);
								map1.put("ADD3", addressLine3String);//telephone
								map1.put("ADD4", addressLine4String);
								map1.put("ADD5", addressLine5String);
								map1.put("ADDID", addressLineIdString);

								ADDRESS_DATA_LISTER.add(map1);
							}
							AdressListviewAdapter adapter = new AdressListviewAdapter(
									getActivity(), ADDRESS_DATA_LISTER);
							addressListView.setAdapter(adapter);
						} else {
							if (getLanguage().equals("1"))
								Toast.makeText(getActivity(),
										"No Addresses in Address Book",
										Toast.LENGTH_LONG).show();
							else
								Toast.makeText(getActivity(),
										"لا يوجد عنوان في دفتر العناوين",
										Toast.LENGTH_LONG).show();
						}
					} else {
						if (getLanguage().equals("1"))
							Toast.makeText(getActivity(),
									"No Address in Address Book",
									Toast.LENGTH_LONG).show();
						else
							Toast.makeText(getActivity(),
									"لا يوجد عنوان في دفتر العناوين",
									Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(getActivity(), R.string.no_response,
							Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				Log.e("AddressBook", "Error in Json Retreival");
				e.printStackTrace();
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

	public void setupUI(View view, boolean isHeading) {
		Typeface myTypeface;
		if (Util.getLanguage(getActivity()).equals("2")) {
			myTypeface = Typeface.createFromAsset(getActivity().getAssets(),
					"fonts/HELVETICANEUELTARABIC-ROMAN.TTF");
		} else {
			myTypeface = Typeface.createFromAsset(getActivity().getAssets(),
					"fonts/Georgia.ttf");
		}
		TextView myTextView = (TextView) view
				.findViewById(R.id.Headeraddressbook);
		myTextView.setTypeface(myTypeface);
		headerTextView.setTypeface(myTypeface);
	}

	Typeface getRegularTypeFace() {
		return Typeface.createFromAsset(getActivity().getAssets(),
				getActivity().getString(R.string.regular_typeface));
	}

	// //////////////////////////

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		getView().setFocusableInTouchMode(true);
		getView().requestFocus();

		getView().setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						callToMyAccount();
						return true;
					}
				}
				return false;
			}
		});
		super.onActivityCreated(savedInstanceState);
	}
	
}
