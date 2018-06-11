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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.PurchaseEvent;
import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.adapter.CartItemListingAdapter;
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.interfaces.OncartUpdated;
import com.dairam.android.fragments.activity.interfaces.listenerHideView;
import com.dairam.android.fragments.activity.parserfiles.JSONfunctions;
import com.dairam.android.fragments.activity.utillities.Util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;

public class CheckoutLastStage extends Fragment implements
		OnItemSelectedListener {
	OncartUpdated updateCart;
	TextView cartSubtotalTextView, specialPromoTextView, voucherTextView,
			shippingTextView, codfeeTextView, totalAmountTextView;
	EditText discountEditText;
	// Spinner Country;
	ListView itemListView;
	/* Button checkoutButton; */

	//Fabric Variables
	String fabricPrice="",fabricCartId="";

	View footer;

	JSONObject jsonobject;
	JSONArray jsonarray;

	Button backToHomeButton;
	TextView backToHomeTextView;
	CartItemListingAdapter cartItemListingAdapter;
	String[] countryStrings;

	ProgressDialog mProgressDialog;
	ArrayList<HashMap<String, String>> arraylist;
	ArrayList<HashMap<String, String>> arraylistcartdata;

	HashMap<String, String> arraylistSingleData;
	ArrayList<String> arrayList2 = new ArrayList<String>();
	Fragment fr;

	String price;
	Context context;
	Button confirmButton;

	public static final String MyPREFERENCES = "MyPrefs";
	public static final String customerid = "id";
	public static final String firstname = "firstname";
	public static final String lastname = "lastname";
	public static final String email_string = "email";
	public static final String cart_string = "cart";
	public static final String wishlist_String = "wishlist";

	SharedPreferences sharedPreferences;

	String customer_id;

	// http://dairam.com/index.php?route=api/checkoutlist/list&id=74&shipping_id=132&language=1&payment_method=cod
	// String referenceUrl =
	// "http://dairam.com/index.php?route=api/cartitems/list&id=";

	// NEW SERVICE for Android ----
	// http://dairam.com/index.php?route=api/checkoutlistconfirm/list&id=74&shipping_id=132&language=1&payment_method=cod&voucher=12345

	// String referenceUrl =
	// "http://dairam.com/index.php?route=api/checkoutlist/list&id=";
	String referenceUrl = "http://dairam.com/index.php?route=api/checkoutlistconfirm/list&id=";

	View clickView;
	Boolean tempflag = false;

	String cartItemRemoveUrl = "http://dairam.com/index.php?route=api/cart/remove&cart=";

	LinearLayout linlay;
	ImageButton imageButton;
	// Button removButton;
	TextView lblPlus, lblMinus, lblQtyCartItem;

	Activity sample;
	int oldposition = 100;

	LinearLayout linlay2;
	TextView errorTextView;

	int languageVariable;
	String addressId;
	String discount;
	String paymentMode;

	String mainUrl, KNETConfirmUrl, VISAConfirmUrl;
	String lan;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.layout_checkout, container, false);

		SharedPreferences sp = getActivity().getSharedPreferences("lan",
				Context.MODE_PRIVATE);
		lan = sp.getString("lan", "en");

		Bundle b = getArguments();
		addressId = b.getString("shipping_address");
		discount = b.getString("discount");
		paymentMode = b.getString("paymentmode");

		SharedPreferences sp1 = getActivity().getSharedPreferences("tag",
				Context.MODE_PRIVATE);
		Editor editor = sp1.edit();
		editor.putString("act", "checkoutfraglast");
		editor.putString("shipping_address", addressId);
		editor.putString("discount", discount);
		editor.putString("paymentmode", paymentMode);
		editor.commit();

		Log.e("Discount", "In CheckoutFrag Last Discount : " + discount);
		Log.e("Address ID", "In CheckoutFrag Last Address : " + addressId);
		Log.e("Payment Mode", "In CheckoutFrag Last Payment : " + paymentMode);

		if (lan.equals("en")) {
			languageVariable = 1;
		} else {
			languageVariable = 2;
		}

		context = getActivity();
		sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES,
				Context.MODE_PRIVATE);

		backToHomeButton = (Button) v.findViewById(R.id.buttonbackhomecart);
		backToHomeTextView = (TextView) v.findViewById(R.id.textviewheadercart);

		itemListView = (ListView) v.findViewById(R.id.listView1cartitem);

		// List View Footer Setting Up...........
		inflater = getActivity().getLayoutInflater();
		footer = (ViewGroup) inflater.inflate(
				R.layout.checkout_last_list_footer, itemListView, false);
		itemListView.addFooterView(footer);

		cartSubtotalTextView = (TextView) footer
				.findViewById(R.id.textviewcarttotalprice);
		specialPromoTextView = (TextView) footer
				.findViewById(R.id.textviewspclpromoprice);
		voucherTextView = (TextView) footer
				.findViewById(R.id.textviewvoucherprice);
		shippingTextView = (TextView) footer
				.findViewById(R.id.textviewcartshipingprice);
		codfeeTextView = (TextView) footer
				.findViewById(R.id.textviewcartcodprice);
		totalAmountTextView = (TextView) footer
				.findViewById(R.id.textviewtotalprice);
		confirmButton = (Button) footer.findViewById(R.id.buttonconfirmorder);

		linlay2 = (LinearLayout) v.findViewById(R.id.linlaywthscroll);
		errorTextView = (TextView) v.findViewById(R.id.textViewerrror);

		itemListView.setVisibility(View.VISIBLE);
		linlay2.setVisibility(View.VISIBLE);
		errorTextView.setVisibility(View.GONE);

		/*
		 * checkoutButton.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { fr = new
		 * CheckoutFragmentFirstStep(); Bundle bundle = new Bundle();
		 * fr.setArguments(bundle); FragmentManager fm = ((Activity)
		 * context).getFragmentManager(); FragmentTransaction
		 * fragmentTransaction = fm.beginTransaction();
		 * fragmentTransaction.replace(R.id.fragment_place, fr);
		 * fragmentTransaction.addToBackStack(null);
		 * fragmentTransaction.commit(); } });
		 */

		// Country = (Spinner) v.findViewById(R.id.spinner1cart);
		discountEditText = (EditText) v.findViewById(R.id.editTextdiscountcode);

		itemListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				final int pos = position;

			}
		});

		if (sharedPreferences.contains(customerid)) {
			customer_id = sharedPreferences.getString(customerid, "");
		}

		if (customer_id == null) {
			Toast.makeText(getActivity(), "Please login to continue",
					Toast.LENGTH_SHORT).show();
			fr = new LoginFragment();
			Bundle bundle = new Bundle();
			bundle.putString("Page", "cart");
			bundle.putString("Bannertitle", "CART");
			bundle.putString("urls", null);
			bundle.putString("ID", null);
			bundle.putString("itemAction", null);
			fr.setArguments(bundle);
			FragmentManager fm = ((Activity) context).getFragmentManager();
			FragmentTransaction fragmentTransaction = fm.beginTransaction();
			fragmentTransaction.replace(R.id.fragment_place, fr);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
		}

		else {

			/*
			 * referenceUrl = referenceUrl + customer_id + "&shipping_id=" +
			 * addressId + "&language=" + getLanguage() + "&payment_method=" +
			 * paymentMode.toLowerCase() + "&voucher=" + discount;
			 */
			referenceUrl = referenceUrl + customer_id + "&shipping_id="
					+ addressId + "&language=" + getLanguage()
					+ "&payment_method=" + paymentMode.toLowerCase()
					+ "&voucher=" + discount;
			new DownloadJSON().execute();
		}
		backToHomeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				CallHomePage();
			}
		});

		backToHomeTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CallHomePage();
			}
		});

		confirmButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String cust_ID = customer_id;
				String vouchercode = discount;
				String shiping_id = addressId;
				int language = languageVariable;

				switch (paymentMode) {
				case "COD":
					Log.e("Language", "" + language);
					Log.e("Customer Id ", "" + cust_ID);
					// http://dairam.com/index.php?route=api/confirm/get&id=64&shipping_id=86&voucher=1111&language=1&payment_method=cod
					// http://dairam.com/index.php?route=api/confirm/get&id=74&shipping_id=128&voucher=1111&language=2&payment_method=cod
					mainUrl = "http://dairam.com/index.php?route=api/confirm/get&id="
							+ cust_ID
							+ "&shipping_id="
							+ shiping_id
							+ "&voucher="
							+ discount
							+ "&language="
							+ language
							+ "&payment_method=cod";

					new RetrieveFeedTaskConfirmOrder().execute();
					break;

				case "KNET":
					KNETConfirmUrl = AppConstants.ORDER_CONFIRM_KNET;

					KNETConfirmUrl += cust_ID + "&shipping_id=" + shiping_id
							+ "&voucher=" + discount + "&language=" + language
							+ "&payment_method=knet";
					
					Log.e("CheckoutLastStage", "Knet Url: "+KNETConfirmUrl);					

					new KNETConfirmOrder().execute();

					break;
				case "VISA":
					VISAConfirmUrl = AppConstants.ORDER_CONFIRM_VISA;

					VISAConfirmUrl += cust_ID + "&shipping_id=" + shiping_id
							+ "&voucher=" + discount + "&language=" + language
							+ "&payment_method=visa";

					new VISAConfirmOrder().execute();

					break;

				case "PAYPAL":
					// http://dairam.com/index.php?route=api/paypal/get&id=74&shipping_id=98&voucher=1111&language=1&payment_method=pp_standard
					VISAConfirmUrl = AppConstants.ORDER_CONFIRM_PAYPAL;

					VISAConfirmUrl += cust_ID + "&shipping_id=" + shiping_id
							+ "&voucher=" + discount + "&language=" + language
							+ "&payment_method=pp_standard";

					new VISAConfirmOrder().execute();

					break;
				case "CASHU":
					// http://dairam.com/index.php?route=api/paypal/get&id=74&shipping_id=98&voucher=1111&language=1&payment_method=pp_standard
					VISAConfirmUrl = AppConstants.ORDER_CONFIRM_CASHU;

					VISAConfirmUrl += cust_ID + "&shipping_id=" + shiping_id
							+ "&voucher=" + discount + "&language=" + language
							+ "&payment_method=cashu";

					//new CASHUConfirmOrder().execute();.
					fr = new CASHUPaymentFragment();
					Bundle b = new Bundle();
					b.putString("gatewayUrl", VISAConfirmUrl);
					fr.setArguments(b);

					FragmentManager fm = getFragmentManager();
					FragmentTransaction fragmentTransaction = fm
							.beginTransaction();
					fragmentTransaction.replace(R.id.fragment_place, fr);
					fragmentTransaction.addToBackStack(null);
					fragmentTransaction.commit();
					break;
				default:
					Log.e("Checkout Last Stage", "Error in Payment Mode");
					Toast.makeText(getActivity(),
							"Sorry..Payment Mode not Specified. Try Again",
							Toast.LENGTH_LONG).show();
					break;
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
		updateCart = new OncartUpdated() {

			@Override
			public void updateCart() {
				// TODO Auto-generated method stub

			}
		};

		updateCart = new OncartUpdated() {

			@Override
			public void updateCart() {
				String lang = getLanguage();

				referenceUrl = referenceUrl + customer_id + "&language=" + lang;

				boolean isNetworkAvialble = Util
						.isNetworkAvailable(getActivity());
				if (isNetworkAvialble) {
					new DownloadJSON().execute();
				} else {
					Toast.makeText(getActivity(), "No network available",
							Toast.LENGTH_SHORT).show();
				}
			}
		};

		setupUI(v, true);
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

	private void CallHomePage() {
		fr = new CheckoutFragmentSecondStep();
		FragmentManager fm = getFragmentManager();
		Bundle bundle = new Bundle();
		bundle.putString("shipping_address", addressId);
		bundle.putString("discount", discount);
		fr.setArguments(bundle);
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_place, fr);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}

	private String getLanguage() {
		SharedPreferences sp = getActivity().getSharedPreferences("lan",
				Context.MODE_PRIVATE);

		if (sp.getString("lan", "en").equals("en"))
			return "1";
		else
			return "2";
	}

	private class DownloadJSON extends AsyncTask<Void, Void, Void> {

		String subtotal = "0 KD", shipping = "0KD", voucher = "0KD",
				specialpromo = "0KD", codfee = "0KD";
		JSONObject JsonObj;

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
				arraylist = new ArrayList<HashMap<String, String>>();
				// Retrieve JSON Objects from the given URL address

				/*
				 * JSONObject jb = JSONfunctions .getJSONfromURL(
				 * "http://dairam.com/index.php?route=api/checkoutlist/list&id=144&shipping_id=324&language=1&payment_method=cod&voucher="
				 * );
				 */

				/*
				 * 
				 * InputStream inputStream = null; try { // Set up HTTP post
				 * 
				 * // HttpClient is more then less deprecated. Need to change to
				 * URLConnection HttpClient httpClient = new
				 * DefaultHttpClient();
				 * 
				 * HttpPost httpPost = new HttpPost(
				 * "http://dairam.com/index.php?route=api/checkoutlist/list&id=124&shipping_id=321&language=1&payment_method=cod&voucher="
				 * ); // httpPost.setEntity(new UrlEncodedFormEntity(param));
				 * HttpResponse httpResponse = httpClient.execute(httpPost);
				 * HttpEntity httpEntity = httpResponse.getEntity();
				 * 
				 * // Read content & Log inputStream = httpEntity.getContent();
				 * } catch (UnsupportedEncodingException e1) {
				 * Log.e("UnsupportedEncodingException", e1.toString());
				 * e1.printStackTrace(); } catch (ClientProtocolException e2) {
				 * Log.e("ClientProtocolException", e2.toString());
				 * e2.printStackTrace(); } catch (IllegalStateException e3) {
				 * Log.e("IllegalStateException", e3.toString());
				 * e3.printStackTrace(); } catch (IOException e4) {
				 * Log.e("IOException", e4.toString()); e4.printStackTrace(); }
				 * // Convert response to string using String Builder try {
				 * BufferedReader bReader = new BufferedReader(new
				 * InputStreamReader(inputStream, "utf-8"), 8); StringBuilder
				 * sBuilder = new StringBuilder();
				 * 
				 * String line = null; while ((line = bReader.readLine()) !=
				 * null) { sBuilder.append(line + "\n"); }
				 * 
				 * inputStream.close(); String result = sBuilder.toString();
				 * 
				 * Log.e("nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn", result);
				 * 
				 * } catch (Exception e) {
				 * Log.e("StringBuilding & BufferedReader",
				 * "Error converting result " + e.toString()); }
				 */

				jsonobject = JSONfunctions
						.getJSONfromURL(AppConstants.CURRENCIES_URL);

				/*
				 * try { // Locate the array name in JSON jsonarray = jsonobject
				 * .getJSONArray(AppConstants.CURRENCIES_JSON_OBJ);
				 * 
				 * for (int i = 0; i < jsonarray.length(); i++) {
				 * HashMap<String, String> map = new HashMap<String, String>();
				 * jsonobject = jsonarray.getJSONObject(i); // Retrive JSON
				 * Objects map.put(AppConstants.CURRENCIES_NAME, jsonobject
				 * .getString(AppConstants.CURRENCIES_NAME));
				 * 
				 * // Set the JSON Objects into the array arraylist.add(map);
				 * 
				 * arrayList2.add(jsonobject
				 * .getString(AppConstants.CURRENCIES_NAME));
				 * 
				 * System.out.println("The arraylist " + arrayList2);
				 * 
				 * } } catch (JSONException e) { Log.e("Error", e.getMessage());
				 * e.printStackTrace(); }
				 */

				arraylistcartdata = new ArrayList<HashMap<String, String>>();

				Log.e("Chekout Last URL", referenceUrl);
				jsonobject = JSONfunctions.getJsonObjectMethod(referenceUrl);
				JsonObj = jsonobject;

				Log.e("Result in Chek Last", JsonObj.toString());

			} catch (Exception e) {

				System.out.println("the application time out");

				try {
					/*
					 * fr = new NoInternetFragment(); FragmentManager fm =
					 * getFragmentManager(); FragmentTransaction
					 * fragmentTransaction = fm .beginTransaction();
					 * fragmentTransaction.replace(R.id.fragment_place, fr);
					 * fragmentTransaction.addToBackStack(null);
					 * fragmentTransaction.commit();
					 */
				} catch (Exception e2) {

				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void args) {
			try {

				if (jsonobject != null) {
					if (jsonobject.getBoolean("success")) {
						jsonarray = jsonobject
								.getJSONArray(AppConstants.CART_ITEM_LIST_JSON_OBJ);

						for (int i = 0; i < jsonarray.length(); i++) {
							HashMap<String, String> map = new HashMap<String, String>();
							jsonobject = jsonarray.getJSONObject(i);
							// Retrive JSON Objects
							map.put(AppConstants.CART_ITEM_LIST_ID, jsonobject
									.getString(AppConstants.CART_ITEM_LIST_ID));
							map.put(AppConstants.CART_ITEM_LIST_NAME,
									jsonobject
											.getString(AppConstants.CART_ITEM_LIST_NAME));
							map.put(AppConstants.CART_ITEM_LIST_PRICE,
									jsonobject
											.getString(AppConstants.CART_ITEM_LIST_PRICE));
							map.put(AppConstants.CART_ITEM_LIST_QUANTITY,
									jsonobject
											.getString(AppConstants.CART_ITEM_LIST_QUANTITY));
							map.put(AppConstants.CART_ITEM_LIST_TOTAL,
									jsonobject
											.getString(AppConstants.CART_ITEM_LIST_TOTAL));
							map.put(AppConstants.CART_ITEM_LIST_IMAGE,
									jsonobject
											.getString(AppConstants.CART_ITEM_LIST_IMAGE));

							// Set the JSON Objects into the array
							arraylistcartdata.add(map);

						}

						if (arraylistcartdata.size() > 0) {

							System.out.println("Arraylist" + arraylist);

							countryStrings = arrayList2
									.toArray(new String[arrayList2.size()]);

							cartItemListingAdapter = new CartItemListingAdapter(
									getActivity(), arraylistcartdata,
									updateCart);
							itemListView.setAdapter(cartItemListingAdapter);

							errorTextView.setVisibility(View.GONE);

							try {
								JSONArray jarrayPrice = JsonObj
										.getJSONArray("total");
								if (jarrayPrice.length() > 0) {
									JsonObj = jarrayPrice.getJSONObject(0);

									subtotal = JsonObj.getString("sub_total");

									cartSubtotalTextView.setText(subtotal);
									if (JsonObj.has("shipping")) {
										shipping = JsonObj
												.getString("shipping");
										Double shipingAmount = 0.000;

										// to check if Subtoal is less than 10
										// to show
										// Toast
										if (getLanguage().equals("1"))
											shipingAmount = Double
													.valueOf(shipping.replace(
															"KD", ""));
										else
											shipingAmount = Double
													.valueOf(shipping.replace(
															"د.ك", ""));
										// if(Integer.valueOf(shipping.replace("KD",""))>0||Integer.valueOf(shipping.replace("د.ك",""))>0)
										Log.e("CheckoutLastStage",
												"Shipping Amount : "
														+ shipingAmount);
										if (shipingAmount > 0) {
											switch (lan) {
											case "en":
												Toast.makeText(
														getActivity(),
														"Free Shipping for orders above 20 KD , delivery charges 2KD for orders below 20 KD ",
														Toast.LENGTH_LONG)
														.show();
												// "2 KWD Additional Shipping Charges will be Added for Orders 20 KWD and Less."

												break;

											default:
												Toast.makeText(
														getActivity(),
														"التوصيل مجاني للطلبات الاكثر من 20 د.ك ،سيتم إحتساب 2 د.ك في حال كان الطلب اقل من 20 د.ك. ",
														Toast.LENGTH_LONG)
														.show();
												// "سيتم إضافة مبلغ ٢ دينار كويتي رسوم شحن لأي طلب قيمته ١٠ دنانير أو أقل"

												break;
											}
										}
										shippingTextView.setText(shipping);
									}

									if (JsonObj.has("voucher")) {
										voucher = JsonObj.getString("voucher");
										voucherTextView.setText(voucher);
									}
									if (JsonObj.has("special_promotions")) {
										specialpromo = JsonObj
												.getString("special_promotions");
										specialPromoTextView
												.setText(specialpromo);
									}
									if (JsonObj.has("cod_fee")) {
										codfee = JsonObj.getString("cod_fee");
										codfeeTextView.setText(codfee);
									}
									price = JsonObj.getString("total");
									totalAmountTextView.setText(price);

									fabricPrice = price;

								} else {
									Log.e("Checkout Last", "No Price Array");
								}
							} catch (Exception e) {
								System.out
										.println("ExcePTION" + e.getMessage());
								e.printStackTrace();
							}
						} else {
							linlay2.setVisibility(View.GONE);
							itemListView.setVisibility(View.GONE);
							errorTextView.setVisibility(View.VISIBLE);
						}
					} else {
						arraylistcartdata.clear();
						;
						cartItemListingAdapter = new CartItemListingAdapter(
								getActivity(), arraylistcartdata, updateCart);
						itemListView.setAdapter(cartItemListingAdapter);
						linlay2.setVisibility(View.GONE);
						itemListView.setVisibility(View.GONE);
						errorTextView.setVisibility(View.VISIBLE);
						/*
						 * Toast.makeText(getActivity(), R.string.noitem_cart,
						 * Toast.LENGTH_LONG).show();
						 */
					}
				} else {
					Toast.makeText(getActivity(), R.string.no_response,
							Toast.LENGTH_SHORT).show();
				}

			} catch (Exception e) {

			}

			try {

				// Close the progress dialog
				mProgressDialog.dismiss();
			} catch (Exception e) {

			}
		}
	}

	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		/*
		 * Country.setSelection(position); String selState = (String)
		 * Country.getSelectedItem();
		 */

		/* selVersion.setText("Selected Android OS:" + selState); */
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	// AsynchTask For deleting the cart item
	class RetrieveFeedTask extends AsyncTask<Void, Void, Void> {

		String id;
		String cart_Count;
		String firstname;
		String lastname;
		String email;
		String wishlistcount;

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
			// Create an array

			// Retrieve JSON Objects from the given URL address
			jsonobject = JSONfunctions.getJSONfromURL(cartItemRemoveUrl);

			return null;
		}

		@Override
		protected void onPostExecute(Void args) {
			mProgressDialog.dismiss();

			try {
				if (jsonobject != null) {
					if (jsonobject.getBoolean("success")) {
						// Locate the array name in JSON
						jsonobject = jsonobject.getJSONObject("customer");

						id = jsonobject.getString(AppConstants.CART_ID);
						firstname = jsonobject
								.getString(AppConstants.CART_FNAME);
						lastname = jsonobject
								.getString(AppConstants.CART_LNAME);
						email = jsonobject.getString(AppConstants.CART_EMAIL);
						cart_Count = jsonobject
								.getString(AppConstants.CART_COUNT);
						wishlistcount = jsonobject
								.getString(AppConstants.WISHLIST_COUNT);

						Editor editor = sharedPreferences.edit();
						editor.putString(customerid, id);
						editor.putString(firstname, firstname);
						editor.putString(lastname, lastname);
						editor.putString(email_string, email);
						editor.putString(cart_string, cart_Count);
						editor.putString(wishlist_String, wishlistcount);

						editor.commit();

						if (id == null) {
							Toast.makeText(getActivity(),
									"Error in removing the item",
									Toast.LENGTH_SHORT).show();
						} else {
							SharedPreferences sp = getActivity()
									.getSharedPreferences("lan",
											Context.MODE_PRIVATE);
							if (sp.getString("lan", "en").equals("en"))
								Toast.makeText(getActivity(),
										"Item removed from the cart",
										Toast.LENGTH_SHORT).show();
							else
								Toast.makeText(getActivity(), "تم حذف المنتج ",
										Toast.LENGTH_SHORT).show();
							fr = new CheckoutLastStage();
							Bundle bundle = new Bundle();
							bundle.putString("shipping_address", addressId);
							fr.setArguments(bundle);
							FragmentManager fm = getFragmentManager();
							FragmentTransaction fragmentTransaction = fm
									.beginTransaction();
							fragmentTransaction
									.replace(R.id.fragment_place, fr);
							fragmentTransaction.addToBackStack(null);
							fragmentTransaction.commit();

							listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) sample;
							listenerHideView.cartListCountUpdation(cart_Count);
							;
						}
					} else {
						Toast.makeText(getActivity(), R.string.no_response,
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getActivity(), R.string.no_response,
							Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();

			}
		}

	}

	// AsynchTask For deleting the cart item
	class RetrieveFeedTaskConfirmOrder extends AsyncTask<Void, Void, Void> {
		JSONObject jObject = null;

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
				Log.e("Main url", mainUrl);

				// jObject = pickJSONObjURL(mainUrl);
				// jObject = JSONfunctions.getJSONfromURL(mainUrl);

				jObject = JSONfunctions.getJsonObjectMethod(mainUrl);

				Log.e("Response confirm", jObject.toString());
			} catch (Exception e) {
				Log.e("Error", "Error in pickJSONObjURL(mainUrl)");
				e.printStackTrace();

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void args) {
			mProgressDialog.dismiss();
			try {
				String jobjString = jObject.toString(4);
				Log.e("JSon Obj", jobjString);
				if (jObject != null) {
					// Log.e("Success", jObject.getString("success"));

					// if (jObject.getString("success").equals("false"))
					if (jObject.getBoolean("success") == false) {
						Toast.makeText(context, jObject.getString("error"),
								Toast.LENGTH_LONG).show();
						Answers.getInstance().logPurchase(new PurchaseEvent()
								.putItemPrice(BigDecimal.valueOf(0.0))
								.putCurrency(Currency.getInstance("KWD"))
								.putItemName("COD Purchase")
								.putItemType("")
								.putItemId("")
								.putSuccess(false));
					} else {

						Answers.getInstance().logPurchase(new PurchaseEvent()
								.putItemPrice(BigDecimal.valueOf(0.0))
								.putCurrency(Currency.getInstance("KWD"))
								.putItemName("COD Purchase")
								.putItemType("")
								.putItemId("")
								.putSuccess(true));

						// sets cart count in preferece to zero

						JSONObject obj = jObject.getJSONObject("customer");
						Editor editor = sharedPreferences.edit();
						editor.putString(cart_string, obj.getString("cart"));
						editor.putString("wishlist", obj.getString("wishlist"));
						editor.commit();
						listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) sample;
						listenerHideView.cartListCountUpdation(obj
								.getString("cart"));
						listenerHideView.wishListCountUpdation(obj
								.getString("wishlist"));

						/*
						 * SharedPreferences sp = context.getSharedPreferences(
						 * "lan", Context.MODE_PRIVATE);
						 */
						if (getLanguage().equals("1"))
							Toast.makeText(
									getActivity(),
									"Invoice and delivery details has been sent to your e-mail.",
									Toast.LENGTH_LONG).show();
						else
							Toast.makeText(
									getActivity(),
									"تم ارسال تفاصيل الفاتورة و التوصيل الى بريدك الالكتروني ",
									Toast.LENGTH_LONG).show();

						// place shop fragment
						/*
						 * fr = new FragmentOne(); FragmentManager fm =
						 * getFragmentManager(); FragmentTransaction
						 * fragmentTransaction = fm .beginTransaction();
						 * fragmentTransaction.replace(R.id.fragment_place, fr);
						 * fragmentTransaction.addToBackStack(null);
						 * fragmentTransaction.commit();
						 */

						fr = new FragmentCategories();
						FragmentManager fm = getFragmentManager();
						FragmentTransaction fragmentTransaction = fm
								.beginTransaction();
						fragmentTransaction.replace(R.id.fragment_place, fr);
						fragmentTransaction.addToBackStack(null);
						fragmentTransaction.commit();
					}
				} else {
					Toast.makeText(context,
							"Server Error.. Order not Confirmed",
							Toast.LENGTH_LONG).show();
				}

			} catch (Exception e) {
				Log.e("Response JSON", "Error Printing JSon Data");
				e.printStackTrace();
			}

		}

		protected JSONObject pickJSONObjURL(String Url) throws JSONException {
			JSONObject JSonObj = null;
			InputStream is = null;
			String result = "";

			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpGet httpget = new HttpGet(Url);
				HttpResponse response = httpclient.execute(httpget);
				// Log.e("Response Status Line",response.getStatusLine().toString());
				// resp = response;
				HttpEntity entity = response.getEntity();
				is = entity.getContent();

			} catch (Exception e) {
				Log.e("Error", "HTTP Error......");
				e.printStackTrace();
			}

			try {
				Log.e("Input Stream", is.toString());
				BufferedReader br = new BufferedReader(new InputStreamReader(
						is, "iso-8859-1"), 8);
				String b;
				StringBuilder sb = new StringBuilder();
				while ((b = br.readLine()) != null) {
					sb.append(b + "\n");
					Log.e("StrigBuilder", sb.toString());
				}
				is.close();
				result = sb.toString();
				// br.close();

			} catch (Exception e) {
				Log.e("Error", "Error in reading Stream");
				e.printStackTrace();
			}

			try {
				Log.e("Result", result);
				JSonObj = new JSONObject(result);
			} catch (JSONException e) {
				Log.e("Error", "Error in JSON Parsing.........");
				e.printStackTrace();
			}
			return JSonObj;
		}

	}

	class KNETConfirmOrder extends AsyncTask<Void, Void, Void> {
		JSONObject jsonObj;
		String paymentPathKNET;

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
				Log.e("KNET Confirm URL", KNETConfirmUrl);
				// jsonObj = JSONfunctions.getJSONfromURL(KNETConfirmUrl);
				jsonObj = JSONfunctions.getJsonObjectMethod(KNETConfirmUrl);
			} catch (Exception e) {
				Log.e("Error", "Error in pickJSONObjURL(KNETConfirmUrl)");
				e.printStackTrace();

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void args) {

			mProgressDialog.dismiss();

			try {

				if (jsonObj != null) {
					Log.e("Knet Response", jsonObj.toString());
					if (jsonObj.getBoolean("success")) {
						paymentPathKNET = jsonObj.getString("path");

						/*
						 * Editor editor = sharedPreferences.edit();
						 * editor.putString(cart_string,"0"); editor.commit();
						 * listenerHideView listenerHideView =
						 * (com.dairam.interfaces.listenerHideView) sample;
						 * listenerHideView.cartListCountUpdation("0");
						 */

						fr = new KNETPaymentFragment();
						Bundle b = new Bundle();
						b.putString("gatewayUrl", paymentPathKNET);
						fr.setArguments(b);

						FragmentManager fm = getFragmentManager();
						FragmentTransaction fragmentTransaction = fm
								.beginTransaction();
						fragmentTransaction.replace(R.id.fragment_place, fr);
						fragmentTransaction.addToBackStack(null);
						fragmentTransaction.commit();
					} else {
						Log.e("Error", "KNET Confirmation Request Failed");
						Toast.makeText(getActivity(),
								"Order Confirmation via KNET Failed",
								Toast.LENGTH_SHORT).show();
					}
				}

			} catch (Exception e) {
				Log.e("Error", "Error in Json Object");
				e.printStackTrace();
			}
		}

	}

	class VISAConfirmOrder extends AsyncTask<Void, Void, Void> {
		JSONObject jsonObj;
		String paymentPathVISA;

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
				Log.e("KNET Confirm URL", VISAConfirmUrl);
				// jsonObj = JSONfunctions.getJSONfromURL(KNETConfirmUrl);
				jsonObj = JSONfunctions.getJsonObjectMethod(VISAConfirmUrl);
			} catch (Exception e) {
				Log.e("Error", "Error in pickJSONObjURL(VISAConfirmUrl)");
				e.printStackTrace();

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void args) {

			mProgressDialog.dismiss();

			try {
				if (jsonObj != null) {
					Log.e("Knet Response", jsonObj.toString());
					if (jsonObj.getBoolean("success")) {
						paymentPathVISA = jsonObj.getString("path");

						fr = new VISAPaymentFragment();
						Bundle b = new Bundle();
						b.putString("gatewayUrl", paymentPathVISA);
						fr.setArguments(b);

						FragmentManager fm = getFragmentManager();
						FragmentTransaction fragmentTransaction = fm
								.beginTransaction();
						fragmentTransaction.replace(R.id.fragment_place, fr);
						fragmentTransaction.addToBackStack(null);
						fragmentTransaction.commit();
					} else {
						Log.e("Error", "VISA Confirmation Request Failed");
						Toast.makeText(getActivity(),
								"Order Confirmation via VISA Failed",
								Toast.LENGTH_SHORT).show();
					}
				}

			} catch (Exception e) {
				Log.e("Error", "Error in Json Object");
				e.printStackTrace();
			}
		}

	}
	
	/*class CASHUConfirmOrder extends AsyncTask<Void, Void, Void> {
		JSONObject jsonObj;
		String paymentPathVISA;

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
				Log.e("CashU Confirm URL", VISAConfirmUrl);
				// jsonObj = JSONfunctions.getJSONfromURL(KNETConfirmUrl);
				jsonObj = JSONfunctions.getJsonObjectMethod(VISAConfirmUrl);
			} catch (Exception e) {
				Log.e("Error", "Error in pickJSONObjURL(VISAConfirmUrl)");
				e.printStackTrace();

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void args) {

			mProgressDialog.dismiss();

			try {
				if (jsonObj != null) {
					Log.e("Knet Response", jsonObj.toString());
					if (jsonObj.getBoolean("success")) {
						paymentPathVISA = jsonObj.getString("path");

						fr = new VISAPaymentFragment();
						Bundle b = new Bundle();
						b.putString("gatewayUrl", paymentPathVISA);
						fr.setArguments(b);

						FragmentManager fm = getFragmentManager();
						FragmentTransaction fragmentTransaction = fm
								.beginTransaction();
						fragmentTransaction.replace(R.id.fragment_place, fr);
						fragmentTransaction.addToBackStack(null);
						fragmentTransaction.commit();
					} else {
						Log.e("Error", "VISA Confirmation Request Failed");
						Toast.makeText(getActivity(),
								"Order Confirmation via VISA Failed",
								Toast.LENGTH_SHORT).show();
					}
				}

			} catch (Exception e) {
				Log.e("Error", "Error in Json Object");
				e.printStackTrace();
			}
		}

	}*/

	public void setupUI(View view, boolean isHeading) {
		/*
		 * if (Util.getLanguage(getActivity()).equals("2")) { Typeface
		 * myTypeface = Typeface.createFromAsset(getActivity() .getAssets(),
		 * "fonts/HELVETICANEUELTARABIC-ROMAN.TTF");
		 */

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
		backToHomeTextView.setTypeface(myTypeface);
		errorTextView.setTypeface(myTypeface);
	}

	Typeface getRegularTypeFace() {
		return Typeface.createFromAsset(context.getAssets(),
				context.getString(R.string.regular_typeface));
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
						CallHomePage();
						return true;
					}
				}
				return false;
			}
		});
		super.onActivityCreated(savedInstanceState);
	}
}
