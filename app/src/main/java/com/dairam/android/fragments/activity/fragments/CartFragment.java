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
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.StartCheckoutEvent;
import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.adapter.CartItemListingAdapter;
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.interfaces.OncartUpdated;
import com.dairam.android.fragments.activity.interfaces.listenerHideView;
import com.dairam.android.fragments.activity.parserfiles.JSONfunctions;
import com.dairam.android.fragments.activity.utillities.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;

public class CartFragment extends Fragment implements OnItemSelectedListener {
	OncartUpdated updateCart;
	String TAG = "CartFragment";

	TextView cartSubtotalTextView, totalAmountTextView;
	EditText discountEditText;
	TextView cartSubTotalValue,specialPromoDiscount;
	//Spinner Country;
	ListView itemListView;
	Button checkoutButton;
	View footer;
	LayoutInflater inflater;

	JSONObject jsonobject;
	JSONArray jsonarray;

	Button backToHomeButton;
	TextView backToHomeTextView;
	CartItemListingAdapter cartItemListingAdapter;
	String[] countryStrings;

	boolean cartEmpty = false;

	ProgressDialog mProgressDialog;
	ArrayList<HashMap<String, String>> arraylist;
	ArrayList<HashMap<String, String>> arraylistcartdata;

	HashMap<String, String> arraylistSingleData;
	ArrayList<String> arrayList2 = new ArrayList<String>();
	Fragment fr;

	String price, subtotal, promotionPrice;
	Context context;

	InputMethodManager imm;

	public static final String MyPREFERENCES = "MyPrefs";
	public static final String customerid = "id";
	public static final String firstname = "firstname";
	public static final String lastname = "lastname";
	public static final String email_string = "email";
	public static final String cart_string = "cart";
	public static final String wishlist_String = "wishlist";

	SharedPreferences sharedPreferences;

	//Fabric Variables
	String fabricAmount,fabricItemCount;

	String customer_id;

	String referenceUrl = "http://dairam.com/index.php?route=api/cartitems/list&id=";
	String cartItemRemoveUrl = "http://dairam.com/index.php?route=api/cart/remove&cart=";
	
	View clickView;
	Boolean tempflag = false;

	LinearLayout linlay ;
	ImageButton imageButton;
	Button removButton;

	Activity sample;
	int oldposition = 100;

	RelativeLayout linlay2;
	TextView errorTextView;
	TextView HeaderText;

	LinearLayout topLinlay;
	//TextView cart_total;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_cart, container, false);
		setupUI(v);
		// InputMethodManager imm to manage softkeyboard.. Arun
		imm = (InputMethodManager) getActivity().getSystemService(
				Context.INPUT_METHOD_SERVICE);
		putOutOfStock(false);

		context = getActivity();
		sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES,
				Context.MODE_PRIVATE);

		HeaderText = (TextView) v.findViewById(R.id.textviewheadercart);
		cartSubtotalTextView = (TextView) v.findViewById(R.id.textView2);
		totalAmountTextView = (TextView) v
				.findViewById(R.id.textviewtotalprice);

		backToHomeButton = (Button) v.findViewById(R.id.buttonbackhomecart);
		backToHomeTextView = (TextView) v.findViewById(R.id.textviewheadercart);

		itemListView = (ListView) v.findViewById(R.id.listView1cartitem);
		checkoutButton = (Button) v.findViewById(R.id.buttoncheckoutcart);

		// setting footer to listview............
		inflater = getActivity().getLayoutInflater();
		footer = (ViewGroup) inflater.inflate(R.layout.cartitemlist_footer,
				itemListView, false);
		itemListView.addFooterView(footer);

		//Country = (Spinner) footer.findViewById(R.id.spinner1cart);
		discountEditText = (EditText) footer
				.findViewById(R.id.editTextdiscountcode);
		specialPromoDiscount = (TextView)footer.findViewById(R.id.lblSplPromoLabelCartValue);
		cartSubTotalValue = (TextView)footer.findViewById(R.id.lblcartSubtotalLabelCartValue);
		specialPromoDiscount.setSelected(true);
		// ..............

		/*cart_total = (TextView) footer.findViewById(R.id.textView1carttotal);*/
		SharedPreferences sp1 = getActivity().getSharedPreferences("tag",
				Context.MODE_PRIVATE);
		Editor editor = sp1.edit();
		editor.putString("act", "cartfragment");
		editor.commit();

		/*cart_total.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				hide_keyboard(getActivity());
			}
		});
*/
		
		
		/*
		 * topLinlay.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { hide_keyboard(getActivity());
		 * } });
		 */
		linlay2 = (RelativeLayout) v.findViewById(R.id.rellayCartTot);
		errorTextView = (TextView) v.findViewById(R.id.textViewerrror);
		
		setupUI(v,true);
		
		/*Typeface myTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/HELVETICANEUELTARABIC-ROMAN.TTF");
		Typeface fontFace = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/LibreBaskerville-Regular.otf");
		HeaderText.setTypeface(fontFace);		
		backToHomeTextView.setTypeface(myTypeface);*/

		itemListView.setVisibility(View.VISIBLE);
		linlay2.setVisibility(View.VISIBLE);
		errorTextView.setVisibility(View.GONE);

		if (v instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) v).getChildCount(); i++) {

				View innerView = ((ViewGroup) v).getChildAt(i);
				setupUI(innerView);
			}
		}

		checkoutButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				boolean flag = getOutOfStock();
				Log.e("the flag", flag + "");

				if (flag) {
					if (getLanguage().equals("1"))
						Toast.makeText(getActivity(), "Out of Stock",
								Toast.LENGTH_SHORT).show();
					else
						Toast.makeText(getActivity(), "غير متوفر حالياً",
								Toast.LENGTH_SHORT).show();

				} else {

					if (!cartEmpty) {
						String discountText = discountEditText.getText()
								.toString();

						//Fabric Checkout Code............
						Answers.getInstance().logStartCheckout(new StartCheckoutEvent()
								.putTotalPrice(BigDecimal.valueOf(Double.parseDouble(fabricAmount.replaceAll("\\D+",""))))
								.putCurrency(Currency.getInstance("KWD"))
								.putItemCount(Integer.parseInt(fabricItemCount)));

						fr = new CheckoutFragmentFirstStep();
						Bundle bundle = new Bundle();
						bundle.putString("discount", discountText);
						fr.setArguments(bundle);
						Log.e("Discount", "In Cart Frag Discount : "
								+ discountText);
						FragmentManager fm = ((Activity) context)
								.getFragmentManager();
						FragmentTransaction fragmentTransaction = fm
								.beginTransaction();
						fragmentTransaction.replace(R.id.fragment_place, fr);
						fragmentTransaction.addToBackStack(null);
						fragmentTransaction.commit();
					}
				}
			}
		});

		/*
		 * Country = (Spinner) v.findViewById(R.id.spinner1cart);
		 * discountEditText = (EditText)
		 * v.findViewById(R.id.editTextdiscountcode);
		 */

		/*
		 * discountEditText.setOnFocusChangeListener(new OnFocusChangeListener()
		 * {
		 * 
		 * @Override public void onFocusChange(View v, boolean hasFocus) {
		 * if(!hasFocus) {
		 * imm.hideSoftInputFromWindow(discountEditText.getWindowToken(), 0);
		 * //imm
		 * .hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken
		 * (), 0); } } });
		 */

		itemListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {

				removButton = (Button) view.findViewById(R.id.btnCartRemove);

				final int pos = position;

				/*
				 * removButton.setOnClickListener(new View.OnClickListener() {
				 * 
				 * @Override public void onClick(View v) { putOutOfStock(false);
				 * Log.e("The remove button clicked", "Here it is");
				 * 
				 * arraylistSingleData = arraylistcartdata.get(pos);
				 * 
				 * String pro_ID_ = arraylistSingleData
				 * .get(AppConstants.CART_ITEM_LIST_ID); cartItemRemoveUrl =
				 * cartItemRemoveUrl + pro_ID_ + "&id=" + customer_id +
				 * "&language=1";
				 * 
				 * new RetrieveFeedTask().execute();
				 * cartItemListingAdapter.notifyDataSetChanged(); } });
				 */

			}
		});
		if (sharedPreferences.contains(customerid)) {
			customer_id = sharedPreferences.getString(customerid, "");
		}

		if (customer_id == null) {
			Toast.makeText(getActivity(),
					getResources().getString(R.string.must_login),
					Toast.LENGTH_SHORT).show();
			/*
			 * if (getLanguage().equals("1")) Toast.makeText(getActivity(),
			 * "Please login to continue", Toast.LENGTH_SHORT).show(); else
			 * Toast.makeText(getActivity(), "يرجى الدخول للاستمرار ",
			 * Toast.LENGTH_SHORT).show();
			 */

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
			String lang = getLanguage();

			referenceUrl = referenceUrl + customer_id + "&language=" + lang;

			boolean isNetworkAvialble = Util.isNetworkAvailable(getActivity());
			if (isNetworkAvialble) {
				new DownloadJSON().execute();
			} else {
				Toast.makeText(getActivity(), "No network available",
						Toast.LENGTH_SHORT).show();
			}

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

		/*fr = new FragmentOne();
		FragmentManager fm = getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_place, fr);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();*/
		
		fr = new FragmentCategories();
		FragmentManager fm = getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_place, fr);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();

	}

	/*
	 * private void setSelectLayVisibility(View view, boolean visibility) { if
	 * (visibility) { linlay = (LinearLayout)
	 * view.findViewById(R.id.linlayselcart);
	 * linlay.setVisibility(View.VISIBLE);
	 * 
	 * imageButton = (ImageButton) view
	 * .findViewById(R.id.imagetransparentcart);
	 * imageButton.setVisibility(View.VISIBLE);
	 * 
	 * removButton = (Button) view .findViewById(R.id.imagetransparentremove);
	 * removButton.setVisibility(View.VISIBLE); } else { linlay = (LinearLayout)
	 * view.findViewById(R.id.linlayselcart);
	 * linlay.setVisibility(View.INVISIBLE);
	 * 
	 * imageButton = (ImageButton) view
	 * .findViewById(R.id.imagetransparentcart);
	 * imageButton.setVisibility(View.INVISIBLE);
	 * 
	 * removButton = (Button) view .findViewById(R.id.imagetransparentremove);
	 * removButton.setVisibility(View.INVISIBLE); } }
	 */

	public void setupUI(View view) {
		// Set up touch listener for non-text box views to hide keyboard.
		if (!(view instanceof EditText)) {

			view.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					Log.e("Touch", "In Touch Listener");
					hideSoftKeyboard(getActivity());
					return false;
				}

			});
		}
	}

	public static void hideSoftKeyboard(Activity activity) {
		if (activity == null || activity.getCurrentFocus() == null)
			return;
		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
				.getWindowToken(), 0);
	}

	public String getLanguage() {
		SharedPreferences sp = getActivity().getSharedPreferences("lan",
				Context.MODE_PRIVATE);
		if (sp.getString("lan", "en").equals("en"))
			return "1";
		else
			return "2";
	}

	private class DownloadJSON extends AsyncTask<Void, Void, Void> {
		int cart_Count = 0;
		JSONObject JsonObj, jsonobjectCurrency;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(mProgressDialog !=null)             //done by siva
			{
				mProgressDialog = null;
			}
			mProgressDialog = new ProgressDialog(getActivity(), R.style.MyTheme);
			mProgressDialog.setCancelable(false);
			mProgressDialog
					.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
			mProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {

				arraylist = new ArrayList<HashMap<String, String>>();
				Log.e(TAG, "Currency URL" + AppConstants.CURRENCIES_URL
						+ getLanguage());
				jsonobjectCurrency = JSONfunctions
						.getJSONfromURL(AppConstants.CURRENCIES_URL
								+ getLanguage());

				arraylistcartdata = new ArrayList<HashMap<String, String>>();
				Log.e("Cart Item List URL", referenceUrl);
				jsonobject = JSONfunctions.getJSONfromURL(referenceUrl);
				JsonObj = jsonobject;

			} catch (Exception e) {

				try {
					fr = new NoInternetFragment();
					FragmentManager fm = getFragmentManager();
					FragmentTransaction fragmentTransaction = fm
							.beginTransaction();
					fragmentTransaction.replace(R.id.fragment_place, fr);
					fragmentTransaction.addToBackStack(null);
					fragmentTransaction.commit();
				} catch (Exception e2) {

				}

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void args) {
			mProgressDialog.dismiss();
			try {

				if (jsonobjectCurrency != null) {
					Log.e(TAG,
							"Currency Json: " + jsonobjectCurrency.toString());
					jsonarray = jsonobjectCurrency
							.getJSONArray(AppConstants.CURRENCIES_JSON_OBJ);
					for (int i = 0; i < jsonarray.length(); i++) {
						HashMap<String, String> map = new HashMap<String, String>();
						jsonobjectCurrency = jsonarray.getJSONObject(i);
						map.put(AppConstants.CURRENCIES_NAME,
								jsonobjectCurrency
										.getString(AppConstants.CURRENCIES_NAME));
						arraylist.add(map);
						arrayList2.add(jsonobjectCurrency
								.getString(AppConstants.CURRENCIES_NAME));
					}
				} else {
					Log.e(TAG, "Currncy Service Error");
					Toast.makeText(context, R.string.no_response_currency,
							Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {
				Log.e(TAG, "Exceptin in Currency Part");
				e.printStackTrace();
			}

			try {
				if (jsonobject != null) {
					if (jsonobject.getBoolean("success")) {
						Log.e("Cart list Resp 1", jsonobject.toString());

						jsonarray = jsonobject
								.getJSONArray(AppConstants.CART_ITEM_LIST_JSON_OBJ);
						cart_Count = jsonarray.length();
						fabricItemCount = cart_Count+"";
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

							map.put("total_quantity",
									jsonobject.getString("total_quantity"));

							// Set the JSON Objects into the array
							arraylistcartdata.add(map);
						}

						try {
							JSONArray jsonArrayPrice = JsonObj
									.getJSONArray("total");

							JsonObj = jsonArrayPrice.getJSONObject(0);
							Log.e("Price.....", JsonObj.getString("total"));
							subtotal = JsonObj.getString("sub_total");

							if (JsonObj.has("special_promotions"))
								promotionPrice = JsonObj
										.getString("special_promotions");
							else {
								if (getLanguage().equals("1"))
									promotionPrice = "0 KD";
								else
									promotionPrice = "0 د.ك";
							}

							price = JsonObj.getString("total");
							fabricAmount = price;
						} catch (Exception e) {
							System.out.println("Exception" + e.getMessage());
							Log.e("Exception", "Exception in Cart Item Listing");
							e.printStackTrace();
						}

						if (arraylistcartdata.size() > 0) {

							/*
							 * itemListView.setLayoutParams(new
							 * RelativeLayout.LayoutParams
							 * (LayoutParams.FILL_PARENT,
							 * 200*arraylistcartdata.size()));
							 */

							cartEmpty = false;

							System.out.println("Arraylist" + arraylist);

							countryStrings = arrayList2
									.toArray(new String[arrayList2.size()]);

							ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(
									getActivity(),
									R.layout.spinner_items_single,
									countryStrings);
							adapter_state
									.setDropDownViewResource(R.layout.spinner_single_item);
							//Country.setAdapter(adapter_state);

							cartItemListingAdapter = new CartItemListingAdapter(
									getActivity(), arraylistcartdata,
									updateCart);
							itemListView.setAdapter(cartItemListingAdapter);

							errorTextView.setVisibility(View.GONE);
							SharedPreferences sp = context
									.getSharedPreferences("lan",
											Context.MODE_PRIVATE);
							
							specialPromoDiscount.setText(promotionPrice);
							cartSubTotalValue.setText(subtotal);

							if (sp.getString("lan", "en").equals("en")) {
								cartSubtotalTextView.setText("ORDER TOTAL: "
										+ price);
								/*
								 * shippingTextView.setText("Special Promotions: "
								 * + promotionPrice);
								 */
							} else {

								cartSubtotalTextView.setText("مجموع الطلب : "
										+ price);
								/*
								 * cartSubtotalTextView.setText("المجموع الكلي : "
								 * + price);
								 */
								/*
								 * shippingTextView.setText("خصم خاص : " +
								 * promotionPrice);
								 */
							}

							// totalAmountTextView.setText(price);
							try {
							} catch (Exception e) {
								System.out
										.println("The Exception handled " + e);

							}
						} else {
							linlay2.setVisibility(View.GONE);
							itemListView.setVisibility(View.GONE);
							errorTextView.setVisibility(View.VISIBLE);
							cartEmpty = true;
						}
						sharedPreferences.edit()
								.putString(cart_string, "" + cart_Count)
								.commit();
						listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) sample;
						listenerHideView.cartListCountUpdation("" + cart_Count);

					} else {
						linlay2.setVisibility(View.GONE);
						itemListView.setVisibility(View.GONE);
						errorTextView.setVisibility(View.VISIBLE);
						cartEmpty = true;

						sharedPreferences.edit()
								.putString(cart_string, "" + "0").commit();
						listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) sample;
						listenerHideView.cartListCountUpdation("" + "0");
					}

				} else {
					Toast.makeText(getActivity(), R.string.no_response,
							Toast.LENGTH_SHORT).show();

					// Util.CallHomePageFragment();
					CallHomePage();
				
				}

			} catch (Exception e) {
				Toast.makeText(getActivity(), R.string.no_response,
						Toast.LENGTH_SHORT).show();
				// Util.CallHomePageFragment();
				Log.e("Exception", "Exception in Cart Item Listing Outer");
				e.printStackTrace();
			}

			//mProgressDialog.dismiss();//new chg sv 2june16
		}
	}

	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		/*Country.setSelection(position);
		String selState = (String) Country.getSelectedItem();*/
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	// AsynchTask For deleting the cart item
	/*
	 * class RetrieveFeedTask extends AsyncTask<Void, Void, Void> {
	 * 
	 * String id; String cart_Count; String firstname; String lastname; String
	 * email; String wishlistcount;
	 * 
	 * @Override protected void onPreExecute() { super.onPreExecute();
	 * 
	 * // Create a progressdialog mProgressDialog = new
	 * ProgressDialog(getActivity(), R.style.MyTheme);
	 * mProgressDialog.setCancelable(false); mProgressDialog
	 * .setProgressStyle(android.R.style.Widget_ProgressBar_Small);
	 * mProgressDialog.show(); }
	 * 
	 * @Override protected Void doInBackground(Void... params) {
	 * Log.e("Cart remove URL", cartItemRemoveUrl); jsonobject =
	 * JSONfunctions.getJSONfromURL(cartItemRemoveUrl);
	 * 
	 * return null; }
	 * 
	 * @Override protected void onPostExecute(Void args) {
	 * mProgressDialog.dismiss(); try { if (jsonobject != null) {
	 * Log.e("Cart remove Resp", jsonobject.toString()); jsonobject =
	 * jsonobject.getJSONObject("customer");
	 * 
	 * id = jsonobject.getString(AppConstants.CART_ID); firstname =
	 * jsonobject.getString(AppConstants.CART_FNAME); lastname =
	 * jsonobject.getString(AppConstants.CART_LNAME); email =
	 * jsonobject.getString(AppConstants.CART_EMAIL); cart_Count =
	 * jsonobject.getString(AppConstants.CART_COUNT); wishlistcount = jsonobject
	 * .getString(AppConstants.WISHLIST_COUNT);
	 * 
	 * Editor editor = sharedPreferences.edit(); editor.putString(customerid,
	 * id); editor.putString(firstname, firstname); editor.putString(lastname,
	 * lastname); editor.putString(email_string, email);
	 * editor.putString(cart_string, cart_Count);
	 * editor.putString(wishlist_String, wishlistcount); editor.commit();
	 * 
	 * if (id == null) { Toast.makeText(getActivity(),
	 * "Error in removing the item", Toast.LENGTH_SHORT).show(); } else {
	 * SharedPreferences sp = getActivity() .getSharedPreferences("lan",
	 * Context.MODE_PRIVATE); if (sp.getString("lan", "en").equals("en")){
	 * Toast.makeText(getActivity(), "Item removed from the cart",
	 * Toast.LENGTH_SHORT).show(); } else{ Toast.makeText(getActivity(),
	 * "تم حذف المنتج ", Toast.LENGTH_SHORT).show(); } String lang =
	 * getLanguage();
	 * 
	 * referenceUrl = referenceUrl + customer_id + "&language=" + lang;
	 * 
	 * boolean isNetworkAvialble = Util.isNetworkAvailable(getActivity()); if
	 * (isNetworkAvialble) { new DownloadJSON().execute(); } else {
	 * Toast.makeText(getActivity(), "No network available",
	 * Toast.LENGTH_SHORT).show(); }
	 * 
	 * fr = new CartFragment(); FragmentManager fm = getFragmentManager();
	 * FragmentTransaction fragmentTransaction = fm .beginTransaction();
	 * fragmentTransaction.replace(R.id.fragment_place, fr);
	 * fragmentTransaction.addToBackStack(null); fragmentTransaction.commit();
	 * 
	 * listenerHideView listenerHideView =
	 * (com.dairam.interfaces.listenerHideView) sample;
	 * listenerHideView.cartListCountUpdation(cart_Count); } } else {
	 * 
	 * Toast.makeText(getActivity(), R.string.no_response,
	 * Toast.LENGTH_SHORT).show(); } } catch (JSONException e) {
	 * 
	 * Log.e("Error", e.getMessage()); e.printStackTrace(); }
	 * 
	 * //mProgressDialog.dismiss(); }
	 * 
	 * }
	 */

	public static void hide_keyboard(Activity activity) {

		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		// Find the currently focused view, so we can grab the correct window
		// token from it.
		View view = activity.getCurrentFocus();
		// If no view currently has focus, create a new one, just so we can grab
		// a window token from it
		if (view == null) {
			view = new View(activity);
		}
		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	private boolean getOutOfStock() {
		SharedPreferences sp = getActivity().getSharedPreferences("MyPrefs",
				Context.MODE_PRIVATE);
		return sp.getBoolean("outofstock", false);
	}

	public void putOutOfStock(boolean status) {

		Log.e("dfd", "msg");
		SharedPreferences sp = getActivity().getSharedPreferences("MyPrefs",
				Context.MODE_PRIVATE);
		sp.edit().putBoolean("outofstock", status).apply();
	}

	public void setupUI(View view, boolean isHeading) {
		
		Typeface myTypeface = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/HELVETICANEUELTARABIC-ROMAN.TTF");
		
		Typeface myTypeface1 = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/Georgia.ttf");
		if(Util.getLanguage(getActivity()).equals("2")){
			backToHomeTextView.setTypeface(myTypeface);
		}
		else{
			backToHomeTextView.setTypeface(myTypeface1);
		}

		/*if ((view instanceof TextView)) {
			
			if (Util.getLanguage(getActivity()).equals("2")) {			

				((TextView) view).setTypeface(myTypeface);
			//((TextView) view).setTypeface(getRegularTypeFace());
			}
		}*/

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
