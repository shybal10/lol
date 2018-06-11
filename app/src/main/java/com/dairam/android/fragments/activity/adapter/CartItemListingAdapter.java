package com.dairam.android.fragments.activity.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.fragments.PromoItemFragment;
import com.dairam.android.fragments.activity.interfaces.OncartUpdated;
import com.dairam.android.fragments.activity.interfaces.listenerHideView;
import com.dairam.android.fragments.activity.parserfiles.ImageLoader;
import com.dairam.android.fragments.activity.parserfiles.JSONfunctions;
import com.dairam.android.fragments.activity.utillities.Util;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class CartItemListingAdapter extends BaseAdapter {

	// Declare Variables
	Context context;
	LayoutInflater inflater;

	public boolean outOfStockFlag = false;

	ArrayList<HashMap<String, String>> data;
	ArrayList<String> list = new ArrayList<String>();
	ArrayList<HashMap<String, String>> arraylistcart;

	public static final String cart_string = "cart";
	public static final String customerid = "id";
	public static final String firstname = "firstname";
	public static final String lastname = "lastname";
	public static final String email_string = "email";
	public static final String wishlist_String = "wishlist";

	SharedPreferences prefs;

	JSONObject jsonobject;
	JSONArray jsonarray;

	ImageLoader imageLoader;

	static Hashtable htSpinValues = new Hashtable();// to hold the selected item
													// in Spinner for Quantity;

	HashMap<String, String> resultp = new HashMap<String, String>();

	Fragment fr;
	ProgressDialog mProgressDialog;
	OncartUpdated updaterequest;
	Boolean selectStatus = false;
	View oldView;
	String cartItemRemoveUrl = "http://dairam.com/index.php?route=api/cart/remove&cart=";

	// String addToCartUrl = AppConstants.ADD_TO_CART_URL;
	String updatecartqtyUrl = AppConstants.UPDATE_CART_QTY_URL;

	public CartItemListingAdapter(Context context,
			ArrayList<HashMap<String, String>> arraylist,
			OncartUpdated updateCart) {
		this.context = context;
		data = arraylist;
		System.out.println("data for URL check" + data);
		imageLoader = new ImageLoader(context);
		this.updaterequest = updateCart;
		htSpinValues = new Hashtable();// to hold the selected item in Spinner
										// for Quantity
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	private Runnable mRunnableQty = new Runnable() {
		@Override
		public void run() {
			new RetrieveFeedTask().execute();
		}
	};

	public View getView(final int position, View convertView, ViewGroup parent) {

		TextView productNameTextview, priceTextView, quantityTextView;
		final TextView lblMinus, lblPlus, lblQtyValue;
		final Button btnRemoveCartItem;
		ImageView productImageView;
		final TextView cart_out;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View itemView = inflater.inflate(R.layout.single_cart_item_listing,
				parent, false);
		// Get the position
		resultp = data.get(position);
		setupUI(itemView, false);
		productNameTextview = (TextView) itemView
				.findViewById(R.id.textView1productname);
		productNameTextview.setSelected(true);
		priceTextView = (TextView) itemView.findViewById(R.id.textView1price);

		quantityTextView = (TextView) itemView
				.findViewById(R.id.textView1quantity);

		productImageView = (ImageView) itemView
				.findViewById(R.id.productimage_cart);

		// Plus and Minus Handling texts for Quantity
		lblMinus = (TextView) itemView.findViewById(R.id.lblMinusCart);
		lblPlus = (TextView) itemView.findViewById(R.id.lblPlusCart);
		lblQtyValue = (TextView) itemView.findViewById(R.id.lblQtyValueCart);
		btnRemoveCartItem = (Button)itemView.findViewById(R.id.btnCartRemove);
		
		
		/*if(Integer.parseInt(lblQtyValue.getText().toString().trim())> 1 && Integer.parseInt(lblQtyValue.getText().toString().trim()) < 10)		
		{
			lblMinus.setVisibility(View.VISIBLE);
			lblPlus.setVisibility(View.VISIBLE);
		}*/
		prefs = context.getSharedPreferences(PromoItemFragment.MyPREFERENCES,
				Context.MODE_PRIVATE);

		lblQtyValue.setText(resultp.get(AppConstants.CART_ITEM_LIST_QUANTITY));
		
		if(lblQtyValue.getText().toString().trim().equals("1"))
		{
			lblMinus.setVisibility(View.INVISIBLE);
			lblPlus.setVisibility(View.VISIBLE);
		}
		if(lblQtyValue.getText().toString().trim().equals("10"))
		{
			lblMinus.setVisibility(View.VISIBLE);
			lblPlus.setVisibility(View.INVISIBLE);
		}

		lblMinus.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				int qty = Integer.parseInt(lblQtyValue.getText().toString());
				Log.e("Quantity  ",""+qty);
				lblQtyValue.setText(""+(--qty));
				if(qty == 1){
					lblMinus.setVisibility(View.INVISIBLE);
					lblPlus.setVisibility(View.VISIBLE);
				}
				else if(qty > 1){
					lblMinus.setVisibility(View.VISIBLE);
					lblPlus.setVisibility(View.VISIBLE);
				}
				
				String custid = prefs.getString("id", "");
				HashMap<String, String> tempResult;
				tempResult = data.get(position);
				String product_id = tempResult
						.get(AppConstants.CART_ITEM_LIST_ID);
				updatecartqtyUrl = updatecartqtyUrl + product_id + "&id="
						+ custid + "&quantity=" + lblQtyValue.getText()
						+ "&language=" + getLanguage();

				new RetrieveFeedTask().execute();
				
				
				
				
				/*if (qty > 1) {
					lblQtyValue.setText("" + (qty - 1));
				}
				if (qty == 2){
					lblMinus.setVisibility(View.INVISIBLE);
					lblPlus.setVisibility(View.VISIBLE);
					//lblMinus.setEnabled(false);
				}
				if(qty < 10)
				{
					lblPlus.setVisibility(View.VISIBLE);
				}
				if (qty > 1) {
					//lblPlus.setEnabled(true);
					lblPlus.setVisibility(View.VISIBLE);
					lblMinus.setVisibility(View.VISIBLE);
					String custid = prefs.getString("id", "");
					HashMap<String, String> tempResult;
					tempResult = data.get(position);
					String product_id = tempResult
							.get(AppConstants.CART_ITEM_LIST_ID);
					updatecartqtyUrl = updatecartqtyUrl + product_id + "&id="
							+ custid + "&quantity=" + lblQtyValue.getText()
							+ "&language=" + getLanguage();

					new RetrieveFeedTask().execute();
					
					Handler myHandler = new Handler();
					myHandler.postDelayed(mRunnableQty, 2000);
				}*/
			}
		});
		lblPlus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int qty = Integer.parseInt(lblQtyValue.getText().toString());
				Log.e("Quantity  ",""+qty);
				lblQtyValue.setText(""+(++qty));
				if(qty == 10){
					lblMinus.setVisibility(View.VISIBLE);
					lblPlus.setVisibility(View.INVISIBLE);
				}
				else if(qty < 10){
					lblMinus.setVisibility(View.VISIBLE);
					lblPlus.setVisibility(View.VISIBLE);
				}
				
				
				String custid = prefs.getString("id", "");
				HashMap<String, String> tempResult;
				tempResult = data.get(position);
				String product_id = tempResult
						.get(AppConstants.CART_ITEM_LIST_ID);
				updatecartqtyUrl = updatecartqtyUrl + product_id + "&id="
						+ custid + "&quantity=" + lblQtyValue.getText()
						+ "&language=" + getLanguage();
				
				new RetrieveFeedTask().execute();
				
				
				/*if (qty < 10) {
					qty = qty + 1;
					Log.e("Quantity : ", "" + qty);
					lblQtyValue.setText("" + qty);
				}
				if (qty == 10)
				{
					lblPlus.setVisibility(View.INVISIBLE);
					lblMinus.setVisibility(View.VISIBLE);
					//lblPlus.setEnabled(false);
				}
				if (qty > 1) {
					lblMinus.setVisibility(View.VISIBLE);					
				}
				if (qty < 10) {
					//lblMinus.setEnabled(true);
					lblMinus.setVisibility(View.VISIBLE);
					lblPlus.setVisibility(View.VISIBLE);
					String custid = prefs.getString("id", "");
					HashMap<String, String> tempResult;
					tempResult = data.get(position);
					String product_id = tempResult
							.get(AppConstants.CART_ITEM_LIST_ID);
					updatecartqtyUrl = updatecartqtyUrl + product_id + "&id="
							+ custid + "&quantity=" + lblQtyValue.getText()
							+ "&language=" + getLanguage();
					
					new RetrieveFeedTask().execute();

					Handler myHandler = new Handler();
					myHandler.postDelayed(mRunnableQty, 2000);
				}*/
			}
		});
		btnRemoveCartItem.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				putOutOfStock(false);
				Log.e("remove button clicked", "Here it is");

				HashMap<String, String>arraylistSingleData = data.get(position);			

				String pro_ID_ = arraylistSingleData
						.get(AppConstants.CART_ITEM_LIST_ID);
				cartItemRemoveUrl = cartItemRemoveUrl + pro_ID_
						+ "&id=" + Util.getCustomerId(context) + "&language=1";

				new RemoveCartItemTask().execute();
				
			}
		});
		// ...................

		cart_out = (TextView) itemView.findViewById(R.id.textViewcart_stock);
		cart_out.setSelected(true);
		cart_out.setVisibility(View.INVISIBLE);
		Fragment f = ((Activity) context).getFragmentManager()
				.findFragmentById(R.id.fragment_place);

		switch (getLanguage()) {
		case "en":

			break;

		default:

			Typeface myTypeface = Typeface.createFromAsset(context.getAssets(),
					"fonts/HELVETICANEUELTARABIC-ROMAN.TTF");

			productNameTextview.setTypeface(myTypeface);
			cart_out.setTypeface(myTypeface);

			break;
		}
		Log.e("Result P ", resultp.toString());
		productNameTextview.setText(resultp
				.get(AppConstants.CART_ITEM_LIST_NAME));
		SharedPreferences sp = context.getSharedPreferences("lan",
				Context.MODE_PRIVATE);
		/*String itemPrice = "0";

		if (resultp.get(AppConstants.CART_ITEM_LIST_PRICE).contains("KD")) {
			itemPrice = resultp.get(AppConstants.CART_ITEM_LIST_PRICE)
					.replace("KD", "").trim();
		} else if (resultp.get(AppConstants.CART_ITEM_LIST_PRICE).contains(
				"د.ك")) {
			itemPrice = resultp.get(AppConstants.CART_ITEM_LIST_PRICE)
					.replace("د.ك", "").trim();
		}
		Double cartItemTotal = Double.parseDouble(itemPrice)
				* (Double.parseDouble(resultp
						.get(AppConstants.CART_ITEM_LIST_QUANTITY)));

		Locale fmtLocale = Locale.ENGLISH;
		NumberFormat formatter = NumberFormat.getInstance(fmtLocale);
		formatter.setMaximumFractionDigits(3);
		formatter.setMinimumFractionDigits(3);
		Log.e("Formated", "......." + formatter.format(cartItemTotal));*/
		//Log.e("lang tag", "......." + fmtLocale.toLanguageTag());

		if (sp.getString("lan", "en").equals("en")) {
			priceTextView.setText("Price:"
					+ resultp.get(AppConstants.CART_ITEM_LIST_PRICE));
			quantityTextView.setText("Total:" + resultp.get(AppConstants.CART_ITEM_LIST_TOTAL));
		} else {
			priceTextView.setText("السعر:"
					+ resultp.get(AppConstants.CART_ITEM_LIST_PRICE));
			quantityTextView.setText("المجموع :" + resultp.get(AppConstants.CART_ITEM_LIST_TOTAL));
		}

		Picasso.with(context)
				.load(resultp.get(AppConstants.CART_ITEM_LIST_IMAGE))
				.placeholder(R.drawable.loading320).into(productImageView);

		if (htSpinValues.get(position) != null) {
		} else {

			// Out of Stock data check
			try {
				int availableQuantity = Integer.parseInt(resultp
						.get("total_quantity"));
				int selectedQuantity = Integer.parseInt(resultp
						.get(AppConstants.CART_ITEM_LIST_QUANTITY)) - 1;

				if (availableQuantity <= selectedQuantity) {
					outOfStockFlag = true;
					putOutOfStock(true);
					// PK code updating for Label In-Stock

					cart_out.setText(cart_out.getText() + ""
							+ availableQuantity);
					cart_out.setVisibility(View.VISIBLE);
				} else {

				}
			} catch (Exception e) {

			}
		}
		/*View itemView1 = inflater.inflate(R.layout.spinercart, parent, false);

		itemView1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("dj f ehr fkfhjkurfh jnrjfh ujf edhsrf");
			}
		});*/

		return itemView;
	}

	class RetrieveFeedTask extends AsyncTask<Void, Void, Void> {

		String id;
		String cart_Count;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog = new ProgressDialog(context, R.style.MyTheme);
			mProgressDialog.setCancelable(false);
			mProgressDialog
					.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
			mProgressDialog.show();

			putOutOfStock(false);

		}

		@Override
		protected Void doInBackground(Void... params) {
			arraylistcart = new ArrayList<HashMap<String, String>>();
			jsonobject = JSONfunctions.getJSONfromURL(updatecartqtyUrl);
			Log.e("CartItemListAdapter", "UpdatQtyUrl: " + updatecartqtyUrl);
			return null;
		}

		@Override
		protected void onPostExecute(Void args) {
			mProgressDialog.dismiss();
			// addToCartUrl =
			// "http://dairam.com/index.php?route=api/cart/get&cart=";
			updatecartqtyUrl = "http://dairam.com/index.php?route=api/cart/update&cart=";
			try {

				if (jsonobject != null) {
					if (jsonobject.getBoolean("success")) {

						// Locate the array name in JSON
						jsonobject = jsonobject.getJSONObject("customer");

						id = jsonobject.getString(AppConstants.CART_ID);
						cart_Count = jsonobject
								.getString(AppConstants.CART_COUNT);

						if (id == null) {
							Toast.makeText(context,
									"Error in adding the Quantity",
									Toast.LENGTH_SHORT).show();
						} else {

							listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) context;
							listenerHideView.cartListCountUpdation(cart_Count);
							;

							Editor editor = prefs.edit();
							editor.putString(cart_string, cart_Count);
							editor.commit();
							if (updaterequest != null) {
								updaterequest.updateCart();
							}
						}

					} else {
						Toast.makeText(context, R.string.no_response,
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(context, R.string.no_response,
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();

			}
		}
	}

	public void putOutOfStock(boolean status) {

		Log.e("dfd", "msg");
		SharedPreferences sp = context.getSharedPreferences("MyPrefs",
				Context.MODE_PRIVATE);
		sp.edit().putBoolean("outofstock", status).apply();
	}

	public void setupUI(View view, boolean isHeading) {

		if ((view instanceof TextView)) {

			((TextView) view).setTypeface(getRegularTypeFace());
		}

	}

	Typeface getRegularTypeFace() {
		return Typeface.createFromAsset(context.getAssets(),
				context.getString(R.string.regular_typeface));
	}

	public String getLanguage() {
		SharedPreferences sp = context.getSharedPreferences("lan",
				Context.MODE_PRIVATE);
		if (sp.getString("lan", "en").equals("en"))
			return "1";
		else
			return "2";
	}
	
	class RemoveCartItemTask extends AsyncTask<Void, Void, Void> {

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
			mProgressDialog = new ProgressDialog(context, R.style.MyTheme);
			mProgressDialog.setCancelable(false);
			mProgressDialog
					.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
			mProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			Log.e("Cart remove URL", cartItemRemoveUrl);
			jsonobject = JSONfunctions.getJSONfromURL(cartItemRemoveUrl);

			return null;
		}

		@Override
		protected void onPostExecute(Void args) {
			mProgressDialog.dismiss();
			try {
				if (jsonobject != null) {
					Log.e("Cart remove Resp", jsonobject.toString());
					jsonobject = jsonobject.getJSONObject("customer");

					id = jsonobject.getString(AppConstants.CART_ID);
					firstname = jsonobject.getString(AppConstants.CART_FNAME);
					lastname = jsonobject.getString(AppConstants.CART_LNAME);
					email = jsonobject.getString(AppConstants.CART_EMAIL);
					cart_Count = jsonobject.getString(AppConstants.CART_COUNT);
					wishlistcount = jsonobject
							.getString(AppConstants.WISHLIST_COUNT);

					Editor editor = prefs.edit();
					editor.putString(customerid, id);
					editor.putString(firstname, firstname);
					editor.putString(lastname, lastname);
					editor.putString(email_string, email);
					editor.putString(cart_string, cart_Count);
					editor.putString(wishlist_String, wishlistcount);
					editor.commit();

					if (id == null) {
						Toast.makeText(context,
								"Error in removing the item",
								Toast.LENGTH_SHORT).show();
					} else {
						SharedPreferences sp = context
								.getSharedPreferences("lan",
										Context.MODE_PRIVATE);
						if (sp.getString("lan", "en").equals("en")){
							Toast.makeText(context,
									"Item removed from the cart",
									Toast.LENGTH_SHORT).show();
						}
						else{
							Toast.makeText(context, "تم حذف المنتج ",
									Toast.LENGTH_SHORT).show();
						}
						String lang = getLanguage();

						boolean isNetworkAvialble = Util.isNetworkAvailable(context);
						if (isNetworkAvialble) {
							if (updaterequest != null) {
								updaterequest.updateCart();
							}
								
						} else {
							Toast.makeText(context, "No network available",
									Toast.LENGTH_SHORT).show();
						}
						
						/*fr = new CartFragment();
						FragmentManager fm = getFragmentManager();
						FragmentTransaction fragmentTransaction = fm
								.beginTransaction();
						fragmentTransaction.replace(R.id.fragment_place, fr);
						fragmentTransaction.addToBackStack(null);
						fragmentTransaction.commit();*/
						
						listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) context;
						listenerHideView.cartListCountUpdation(cart_Count);
					}
				} else {
					
					Toast.makeText(context, R.string.no_response,
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}

			//mProgressDialog.dismiss();
		}

	}
}
