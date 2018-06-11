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
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.adapter.ViewPagerAdapter;
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.dialogfragment.DialogProductImage;
import com.dairam.android.fragments.activity.interfaces.OnShowProductImageDialog;
import com.dairam.android.fragments.activity.interfaces.listenerHideView;
import com.dairam.android.fragments.activity.parserfiles.ImageLoader;
import com.dairam.android.fragments.activity.parserfiles.JSONfunctions;
import com.dairam.android.fragments.activity.utillities.Util;
import com.dairam.android.fragments.activity.viewpager.CirclePageIndicator;
import com.dairam.android.fragments.activity.viewpager.CustomviewPagerAdapter;

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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WishListSingleItemDetails extends Fragment {
	OnShowProductImageDialog showProdImg;
	LinearLayout linlay;
	
	int maxStock = 0;
	Button bacButton;
	TextView headerTextView;

	JSONObject jsonobject;
	JSONArray jsonarray;
	ProgressDialog mProgressDialog;
	ArrayList<HashMap<String, String>> arraylist;

	String productid;
	String productName;
	String price;
	String backToPreviousUrl;
	String banerTitle;

	ViewPager viewPager;
	String CurrencyCode;
	ViewPagerAdapter.OnProductClicked listener;
	ViewPagerAdapter viewPagerAdapter;

	Fragment fr;
	Context context;

	float productPriceValue;

	ImageLoader imageLoader;

	ViewPager productDetailsImageView;
	CirclePageIndicator pagerIndicate;
	TextView productnameTextView;
	TextView descriptionTextView;
	ArrayList<HashMap<String, String>> arraylistcart;

	Activity sample;

	TextView amountButton;
	// Spinner quantityButton;
	TextView quantityText, lblMinus, lblPlus;

	String PRODUCTDETAILURL = "http://dairam.com/index.php?route=api/product/get&id=";

	String description, imageString, productPrice;

	Button shareButton;

	ImageButton cartItButton, wishlistitButton;
	String addToCartURL = "http://dairam.com/index.php?route=api/cart/get&cart=";
	String addToWishlistURl = "http://dairam.com/index.php?route=api/wishlist/get&wishlist=";

	/*
	 * http://dairam.com/index.php?route=api/wishlist/get&wishlist=107&id=77
	 * &language=1
	 */public static final String MyPREFERENCES = "MyPrefs";
	public static final String customerid = "id";
	public static final String firstname = "firstname";
	public static final String lastname = "lastname";
	public static final String email_string = "email";
	public static final String cart_string = "cart";
	public static final String wishlist_String = "wishlist";
	String lan;
	String dummyUrl;

	private List<String> imageIdList;

	SharedPreferences sharedPreferences;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.promoitemdetails, container, false);

		context = getActivity();
		Log.e("Wishlist", "wishlist2");
		sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES,
				Context.MODE_PRIVATE);
		linlay = (LinearLayout) v.findViewById(R.id.linlaypromodet);
		shareButton = (Button) v.findViewById(R.id.buttonsharepromodet);
		amountButton = (TextView) v.findViewById(R.id.buttonamntpromodet);
		// quantityButton = (Spinner)
		// v.findViewById(R.id.buttonquantitypromodet);
		bacButton = (Button) v.findViewById(R.id.buttonbackhomepromodet);
		headerTextView = (TextView) v
				.findViewById(R.id.textviewheaderpromodete);

		productnameTextView = (TextView) v.findViewById(R.id.lblProductName);
		productnameTextView.setSelected(true);
		descriptionTextView = (TextView) v
				.findViewById(R.id.textViewdescription);
		viewPager = (ViewPager) v.findViewById(R.id.pager);
		cartItButton = (ImageButton) v.findViewById(R.id.buttoncartpromodet);
		wishlistitButton = (ImageButton) v.findViewById(R.id.buttonwishlistpromodet);

		productDetailsImageView = (ViewPager) v
				.findViewById(R.id.imageView1);
		pagerIndicate = (CirclePageIndicator)v.findViewById(R.id.indicator);

		imageIdList = new ArrayList<String>();	

		amountButton.setSelected(true);		

		quantityText = (TextView) v.findViewById(R.id.lblQtyValueCartDetails);
		lblMinus = (TextView) v.findViewById(R.id.lblMinusCartDetails);
		lblPlus = (TextView) v.findViewById(R.id.lblPlusCartDetails);
		final DecimalFormat df = new DecimalFormat("#.000");
		lblPlus.setOnClickListener(new OnClickListener() 
		{			
			@Override
			public void onClick(View v) {				
				int qty = Integer.parseInt(quantityText.getText().toString().trim());
				double amt = Double.parseDouble(productPrice.substring(0,productPrice.length()-3));
				
				//if(qty > 0 && qty < 11){
				if(qty > 0 && qty < maxStock+1){
					quantityText.setText(""+(++qty));
				
					amountButton.setText(""+ df.format(amt * qty)+CurrencyCode);
				}
				if(qty == maxStock){
					lblPlus.setVisibility(View.INVISIBLE);
					lblMinus.setVisibility(View.VISIBLE);
				}
				else if(qty < maxStock && qty > 1){
					lblPlus.setVisibility(View.VISIBLE);
					lblMinus.setVisibility(View.VISIBLE);
				}
				
			}
		});		
		lblMinus.setOnClickListener(new OnClickListener() 
		{			
			@Override
			public void onClick(View v) {
				int qty = Integer.parseInt(quantityText.getText().toString().trim());
				double amt = Double.parseDouble(productPrice.substring(0,productPrice.length()-3));
				//if(qty > 1 && qty < 11)
				if(qty > 1 && qty < maxStock+1){
					quantityText.setText(""+(--qty));
					amountButton.setText(""+ df.format(amt * qty)+CurrencyCode);
				}
				if(qty == 1){
					lblMinus.setVisibility(View.INVISIBLE);
					lblPlus.setVisibility(View.VISIBLE);
				}
				else if(qty > 1 && qty < maxStock){
					lblPlus.setVisibility(View.VISIBLE);
					lblMinus.setVisibility(View.VISIBLE);
				}
				
			}
		});
		
		showProdImg = new OnShowProductImageDialog() {

			@Override
			public void showDialog(List<String> images,int selectedPosition) {
				DialogProductImage myDialogFragmentProdImg = new DialogProductImage();
				myDialogFragmentProdImg.newInstance(context, images,selectedPosition);
				myDialogFragmentProdImg.show(getFragmentManager(),
						"myDialogProductImage");
			}
		};

		cartItButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String id = null;

				if (sharedPreferences.contains(customerid)) {
					id = sharedPreferences.getString(customerid, "");
				}

				if (id == null) {

					String product_id = productid;
					fr = new LoginFragment();
					Bundle bundle = new Bundle();
					bundle.putString("Page", "Promodetails");
					bundle.putString("Bannertitle", productName);
					bundle.putString("urls", dummyUrl);
					bundle.putString("ID", product_id);
					bundle.putString("itemAction", AppConstants.CART_IT);
					fr.setArguments(bundle);
					FragmentManager fm = ((Activity) context)
							.getFragmentManager();
					FragmentTransaction fragmentTransaction = fm
							.beginTransaction();
					fragmentTransaction.replace(R.id.fragment_place, fr);
					fragmentTransaction.addToBackStack(null);
					fragmentTransaction.commit();
				}

				else {

					String product_id = productid;
					System.out.println("Login Done!!!" + product_id);
					addToCartURL = addToCartURL + product_id + "&id=" + id
							+ "&quantity=1&language=1";

					new RetrieveFeedTask().execute();
				}

			}
		});

		wishlistitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String id = null;

				if (sharedPreferences.contains(customerid)) {
					id = sharedPreferences.getString(customerid, "");
				}

				if (id == null) {

					String product_id = productid;
					fr = new LoginFragment();
					Bundle bundle = new Bundle();
					bundle.putString("Page", "Promodetails");
					bundle.putString("Bannertitle", productName);
					bundle.putString("urls", dummyUrl);
					bundle.putString("ID", product_id);
					bundle.putString("itemAction", AppConstants.CART_IT);
					fr.setArguments(bundle);
					FragmentManager fm = ((Activity) context)
							.getFragmentManager();
					FragmentTransaction fragmentTransaction = fm
							.beginTransaction();
					fragmentTransaction.replace(R.id.fragment_place, fr);
					fragmentTransaction.addToBackStack(null);
					fragmentTransaction.commit();
				}

				else {

					String product_id = productid;
					System.out.println("Login Done!!!" + product_id);
					addToWishlistURl = addToWishlistURl + product_id + "&id="
							+ id + "&quantity=1&language=1";

					new RetrieveFeedTaskWishlist().execute();
				}
			}

		});

		/*
		 * List<String> list = new ArrayList<String>(); list.add("1");
		 * list.add("2"); list.add("3"); list.add("4"); list.add("5");
		 * list.add("6"); list.add("7"); list.add("8"); list.add("9");
		 * list.add("10"); ArrayAdapter<String> dataAdapter = new
		 * ArrayAdapter<String>( getActivity(), R.layout.spinneritems, list);
		 * dataAdapter.setDropDownViewResource(R.layout.spinner_single_item);
		 * quantityButton.setAdapter(dataAdapter);
		 * 
		 * addListenerOnSpinnerItemSelection();
		 */

		shareButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ShareDet();
			}
		});

		bacButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				BackToPageUrl();
			}
		});

		headerTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BackToPageUrl();
			}
		});

		linlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				System.out.println("Bug handling");
			}
		});

		/* Getting value from the bundle */

		try {

			productid = getArguments().getString("productid");
			Log.e("id.", "" + productid);
			productName = getArguments().getString("productname");
			price = getArguments().getString("Price");
			backToPreviousUrl = getArguments().getString("BackToPreviousUrl");
			banerTitle = getArguments().getString("title");

			SharedPreferences sp1 = getActivity().getSharedPreferences("tag",
					Context.MODE_PRIVATE);
			String p = "fragmentpromodetails";
			Editor editor = sp1.edit();
			editor.putString("act", p);
			editor.putString("productid", productid);
			editor.putString("productname", productName);
			editor.putString("Price", price);
			editor.putString("BackToPreviousUrl", backToPreviousUrl);
			editor.putString("title", banerTitle);
			editor.commit();

		} catch (Exception e) {
			System.out
					.println("Exception caught not possible to retrieve value from bundle");
		}

		headerTextView.setText(productName.toUpperCase());
		productnameTextView.setText(productName);

		// PRODUCTDETAILURL =
		// PRODUCTDETAILURL+productid+"&language=1&currency=KWD";

		dummyUrl = PRODUCTDETAILURL;
		amountButton.setText(price);
		SharedPreferences sp = getActivity().getSharedPreferences("lan",
				Context.MODE_PRIVATE);
		lan = sp.getString("lan", "en");

		switch (lan) {
		case "en":
			System.out.println("Entered herennnnnnn");
			PRODUCTDETAILURL = PRODUCTDETAILURL + productid
					+ "&language=1&currency=KWD";
			break;

		default:
			System.out.println("Entered here.......nnnnnnn");
			PRODUCTDETAILURL = PRODUCTDETAILURL + productid
					+ "&language=2&currency=KWD";
			break;
		}
		new DownloadJSON().execute();
		listener = new ViewPagerAdapter.OnProductClicked() {

			@Override
			public void onClicked(String id) {
				listener = new ViewPagerAdapter.OnProductClicked() {

					@Override
					public void onClicked(String id) {

						Log.e("Data Clicked", "Here you go");
						productid = id;
						switch (lan) {
						case "en":
							System.out.println("Entered herennnnnnn");
							PRODUCTDETAILURL = PRODUCTDETAILURL + productid
									+ "&language=1&currency=KWD";//
							break;

						default:
							System.out.println("Entered here.......nnnnnnn");
							PRODUCTDETAILURL = PRODUCTDETAILURL + productid
									+ "&language=2&currency=KWD";
							break;
						}
						fr = new WishListSingleItemDetails();
						Bundle
						
						bundle = new Bundle();
						bundle.putString("productid", productid);
						bundle.putString("productname", productName);
						bundle.putString("Price", price);
						bundle.putString("BackToPreviousUrl", backToPreviousUrl);
						bundle.putString("title", banerTitle);

						fr.setArguments(bundle);
						FragmentManager fm = ((Activity) context)
								.getFragmentManager();
						FragmentTransaction fragmentTransaction = fm
								.beginTransaction();
						fragmentTransaction.replace(R.id.fragment_place, fr);
						fragmentTransaction.addToBackStack(null);
						fragmentTransaction.commit();
					}
				};
			}
		};
		listener = new ViewPagerAdapter.OnProductClicked() {

			@Override
			public void onClicked(String id) {
				productid = id;
				switch (lan) {
				case "en":
					System.out.println("Entered herennnnnnn");
					PRODUCTDETAILURL = PRODUCTDETAILURL + productid
							+ "&language=1&currency=KWD";//
					break;

				default:
					System.out.println("Entered here.......nnnnnnn");
					PRODUCTDETAILURL = PRODUCTDETAILURL + productid
							+ "&language=2&currency=KWD";
					break;
				}
				// Log.e("Product id", "" + id);
				new DownloadJSON().execute();

			}
		};

		switch (lan) {
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
		listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) activity;
		listenerHideView.promoItemHideTopbar();

		sample = activity;
		super.onAttach(activity);
	}

	private void BackToPageUrl() {
		fr = new WishListFragment();
		FragmentManager fm = getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_place, fr);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}

	/*
	 * Asyn:Task for loading the JSON objects
	 */
	private class DownloadJSON extends AsyncTask<Void, Void, Void> {

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
			// Create an array
			arraylist = new ArrayList<HashMap<String, String>>();
			// Retrieve JSON Objects from the given URL address
			HttpParams params1 = new BasicHttpParams();
			HttpProtocolParams.setVersion(params1, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params1, "UTF-8");
			params1.setBooleanParameter("http.protocol.expect-continue", false);
			HttpClient httpclient = new DefaultHttpClient(params1);

			Log.e("PRODUCTDETAILURL", PRODUCTDETAILURL);

			HttpPost httppost = new HttpPost(PRODUCTDETAILURL);
			try {
				HttpResponse http_response = httpclient.execute(httppost);

				HttpEntity entity = http_response.getEntity();
				String jsonText = EntityUtils.toString(entity, HTTP.UTF_8);
				Log.i("WISHLISTResponse", jsonText);

				jsonobject = new JSONObject(jsonText);

			} catch (Exception e) {

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void args) {

			Boolean flag = false;
			if (jsonobject != null) {

				try {
					flag = jsonobject.getBoolean("success");
				} catch (Exception e) {
					Log.e("The result", e.getMessage());
				}
				if (flag) {
					try {
						// Locate the array name in JSON

						jsonobject = jsonobject.getJSONObject("product");

						description = jsonobject.getString("description");
						imageString = jsonobject.getString("image");
						productPrice = jsonobject.getString("price");
						maxStock = Integer.parseInt(jsonobject.getString("quantity").trim());
						
						imageIdList.add(imageString);

						if (jsonobject.getString("image1") != null
								&& !(jsonobject.getString("image1")
										.equals("null")))
							imageIdList.add(jsonobject.getString("image1"));
						if (jsonobject.getString("image2") != null
								&& !(jsonobject.getString("image2")
										.equals("null")))
							imageIdList.add(jsonobject.getString("image2"));
						if (jsonobject.getString("image3") != null
								&& !(jsonobject.getString("image3")
										.equals("null")))
							imageIdList.add(jsonobject.getString("image3"));

						try {

							// Locate the array name in JSON
							jsonarray = jsonobject
									.getJSONArray(AppConstants.SINGLE_PRODUCT_SUB_JSON_OBJECT);

							for (int i = 0; i < jsonarray.length(); i++) {
								HashMap<String, String> map = new HashMap<String, String>();
								jsonobject = jsonarray.getJSONObject(i);
								// Retrive JSON Objects
								map.put(AppConstants.SINGLE_PRODUCT_SUB_ID,
										jsonobject
												.getString(AppConstants.SINGLE_PRODUCT_SUB_ID));
								map.put(AppConstants.SINGLE_PRODUCT_SUB_IMAGE,
										jsonobject
												.getString(AppConstants.SINGLE_PRODUCT_SUB_IMAGE));
								map.put(AppConstants.SINGLE_PRODUCT_SUB_REF,
										jsonobject
												.getString(AppConstants.SINGLE_PRODUCT_SUB_REF));

								// Set the JSON Objects into the array
								arraylist.add(map);

								System.out.println("The Images" + arraylist);

							}
						} catch (JSONException e) {
							Log.e("Error", e.getMessage());
							e.printStackTrace();
						}

					} catch (JSONException e) {
						Log.e("Error", e.getMessage());
						e.printStackTrace();
					}

					descriptionTextView.setText(description);
					imageLoader = new ImageLoader(context);

					CurrencyCode = productPrice.substring(
							productPrice.length() - 2, productPrice.length());

					System.out.println("The currency........" + CurrencyCode);

					if ((CurrencyCode.equals("KD"))
							|| (CurrencyCode.equals("BD"))
							|| (CurrencyCode.equals("QR"))
							|| (CurrencyCode.equals("SR"))) {

						try {
							productPriceValue = Float.parseFloat(productPrice
									.substring(0, productPrice.length() - 2));
						} catch (Exception e) {
							System.out.println("Cannot parse the value");
						}
					}

					else {
						try {
							productPriceValue = Float.parseFloat(productPrice
									.substring(0, productPrice.length() - 3));
						} catch (Exception e) {
							System.out.println("Cannot parse the value");
						}

					}

					Log.e("araylistrelatedproducts", arraylist + "");

					if (arraylist.size() > 0) {

						System.out.println("Enterng here");
						viewPagerAdapter = new ViewPagerAdapter(context,
								arraylist, listener);

						for (int i = 0; i < arraylist.size(); i++) {
							Log.e("Check value",
									""
											+ arraylist
													.get(i)
													.get((AppConstants.SUB_CATEGORY1_ID)));
						}
					} else {

					}
					viewPager.setAdapter(viewPagerAdapter);

					/*
					 * productDetailsImageView.setAdapter(new
					 * ImagePagerAdapter(context,
					 * imageIdList).setInfiniteLoop(true));
					 * productDetailsImageView .setOnPageChangeListener(new
					 * MyOnPageChangeListener());
					 * 
					 * productDetailsImageView.setInterval(4000);
					 * productDetailsImageView.startAutoScroll();
					 */

					if (imageIdList.size() > 1) {
						/*productDetailsImageView
								.setAdapter(new ImagePagerAdapter(context,
										imageIdList).setInfiniteLoop(true));*/
						productDetailsImageView
						.setAdapter(new CustomviewPagerAdapter(context,
								imageIdList, showProdImg));


						productDetailsImageView
								.setOnPageChangeListener(new MyOnPageChangeListener());
						pagerIndicate.setViewPager(productDetailsImageView);
					} else {
						/*productDetailsImageView
								.setAdapter(new ImagePagerAdapter(context,
										imageIdList).setInfiniteLoop(false));*/
						productDetailsImageView
						.setAdapter(new CustomviewPagerAdapter(context,
								imageIdList, showProdImg));
						pagerIndicate.setViewPager(productDetailsImageView);
					}
					
					Log.e("WishlistSingleItemDet","Maximum Stock : "+maxStock);
					if(maxStock > 10){
						maxStock = 10;
					}
					if(maxStock == 1){
						lblPlus.setVisibility(View.INVISIBLE);
						lblMinus.setVisibility(View.INVISIBLE);
					}
					else if(maxStock > 1){
						lblMinus.setVisibility(View.INVISIBLE);
						lblPlus.setVisibility(View.VISIBLE);
					}
				} else {
					Toast.makeText(getActivity(), R.string.no_response,
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getActivity(), R.string.no_response,
						Toast.LENGTH_SHORT).show();
			}
			// Close the progress dialog
			mProgressDialog.dismiss();
		}
	}

	/*
	 * public void addListenerOnSpinnerItemSelection() {
	 * 
	 * quantityButton .setOnItemSelectedListener(new
	 * CustomOnItemSelectedListener()); }
	 */

	public class CustomOnItemSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			/*
			 * Toast.makeText(parent.getContext(), "OnItemSelectedListener : " +
			 * parent.getItemAtPosition(pos).toString(),
			 * Toast.LENGTH_SHORT).show();
			 */

			String x = parent.getItemAtPosition(pos).toString();

			if (x == "1") {
				amountButton.setText(price);
			}

			else if (x == "2") {
				amountButton.setText(productPriceValue * 2 + "00"
						+ CurrencyCode);
			}

			else if (x == "3") {
				amountButton.setText(productPriceValue * 3 + "00"
						+ CurrencyCode);
			}

			else if (x == "4") {
				amountButton.setText(productPriceValue * 4 + "00"
						+ CurrencyCode);
			}

			else if (x == "5") {
				amountButton.setText(productPriceValue * 5 + "00"
						+ CurrencyCode);
			}

			else if (x == "6") {
				amountButton.setText(productPriceValue * 6 + "00"
						+ CurrencyCode);
			}

			else if (x == "7") {
				amountButton.setText(productPriceValue * 7 + "00"
						+ CurrencyCode);
			} else if (x == "8") {
				amountButton.setText(productPriceValue * 8 + "00"
						+ CurrencyCode);
			} else if (x == "9") {
				amountButton.setText(productPriceValue * 9 + "00"
						+ CurrencyCode);
			} else if (x == "10") {
				amountButton.setText(productPriceValue * 10 + "00"
						+ CurrencyCode);
			}

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}

	}

	public void ShareDet() {
		String productDescription = descriptionTextView.getText().toString();

		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		// shareIntent.setType("text/plain");
		Log.e("Product Name", productName);
		Log.e("Product Name", productDescription);
		shareIntent.setType("text/plain");
		shareIntent
				.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
		shareIntent.putExtra(Intent.EXTRA_TEXT, productName + "\n"
				+ productDescription);

		// shareIntent.putExtra(Intent.EXTRA_TEXT, productName);
		// shareIntent.putExtra("Intent.EXTRA_TEXT", "http://dairam.com/");
		startActivity(Intent.createChooser(shareIntent, "Share Product"));

		/*
		 * Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
		 * "arunpklearns@gmail.com", "", null));
		 * emailIntent.setType("text/plain");
		 * emailIntent.putExtra(Intent.EXTRA_SUBJECT,
		 * getString(R.string.app_name));
		 * emailIntent.putExtra(Intent.EXTRA_TEXT, "This is your Product");
		 * 
		 * startActivity(Intent.createChooser(emailIntent, "Share Product"));
		 */

	}

	class RetrieveFeedTask extends AsyncTask<Void, Void, Void> {
		String id;
		String cart_Count;

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
			// Create an array
			arraylistcart = new ArrayList<HashMap<String, String>>();

			// Retrieve JSON Objects from the given URL address
			jsonobject = JSONfunctions.getJSONfromURL(addToCartURL);
			return null;
		}

		@Override
		protected void onPostExecute(Void args) {
			mProgressDialog.dismiss();
			addToCartURL = "http://dairam.com/index.php?route=api/cart/get&cart=";

			Boolean flag = false;
			if (jsonobject != null) {
				try {
					flag = jsonobject.getBoolean("success");
				} catch (Exception e) {

				}
				if (flag) {

					try {
						// Locate the array name in JSON
						jsonobject = jsonobject.getJSONObject("customer");

						id = jsonobject.getString(AppConstants.CART_ID);
						cart_Count = jsonobject
								.getString(AppConstants.CART_COUNT);

					} catch (JSONException e) {
						Log.e("Error", e.getMessage());
						e.printStackTrace();

					}

					if (id == null) {
						Toast.makeText(
								getActivity(),
								getResources().getString(
										R.string.error_in_additem),
								Toast.LENGTH_SHORT).show();
					} else {
						SharedPreferences sp = getActivity()
								.getSharedPreferences("lan",
										Context.MODE_PRIVATE);

						if (sp.getString("lan", "en").equals("en"))
							Toast.makeText(getActivity(),
									"Item added to the cart",
									Toast.LENGTH_SHORT).show();
						else
							Toast.makeText(getActivity(),
									"تم إضافة المنتج إلى السلة ",
									Toast.LENGTH_SHORT).show();
						listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) sample;
						listenerHideView.cartListCountUpdation(cart_Count);
					}
				} else {
					Toast.makeText(getActivity(), R.string.no_response,
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getActivity(), R.string.no_response,
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	class RetrieveFeedTaskWishlist extends AsyncTask<Void, Void, Void> {

		String id;
		String wishlist_count;

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
			arraylistcart = new ArrayList<HashMap<String, String>>();

			// Retrieve JSON Objects from the given URL address
			jsonobject = JSONfunctions.getJSONfromURL(addToWishlistURl);

			return null;
		}

		@Override
		protected void onPostExecute(Void args) {
			mProgressDialog.dismiss();
			addToWishlistURl = "http://dairam.com/index.php?route=api/wishlist/get&wishlist=";
			Boolean flag = false;
			if (jsonobject != null) {
				try {
					flag = jsonobject.getBoolean("success");
				} catch (Exception e) {

				}
				if (flag) {
					try {
						// Locate the array name in JSON
						jsonobject = jsonobject.getJSONObject("customer");

						id = jsonobject.getString(AppConstants.CART_ID);
						wishlist_count = jsonobject
								.getString(AppConstants.WISHLIST_COUNT);

					} catch (JSONException e) {
						Log.e("Error", e.getMessage());
						e.printStackTrace();

					}
					if (id == null) {
						Toast.makeText(
								getActivity(),
								getResources().getString(
										R.string.error_in_additem),
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(
								getActivity(),
								getResources().getString(
										R.string.item_added_wishlist),
								Toast.LENGTH_SHORT).show();

						listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) sample;
						listenerHideView.wishListCountUpdation(wishlist_count);
					}
				} else {
					Toast.makeText(getActivity(), R.string.no_response,
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getActivity(), R.string.no_response,
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int position) {
			/*
			 * indexText.setText(new StringBuilder().append((position) %
			 * ListUtils.getSize(imageIdList) + 1).append("/")
			 * .append(ListUtils.getSize(imageIdList)));
			 */
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		// stop auto scroll when onPause
	//	productDetailsImageView.stopAutoScroll();

		if (mProgressDialog != null)
			mProgressDialog.dismiss();
	}

	public void setupUI(View view, boolean isHeading) {
		if (Util.getLanguage(getActivity()).equals("2")) {

			Typeface myTypeface = Typeface.createFromAsset(getActivity()
					.getAssets(), "fonts/HELVETICANEUELTARABIC-ROMAN.TTF");
			TextView myTextView = (TextView) view
					.findViewById(R.id.textviewsuggestion);
			headerTextView.setTypeface(myTypeface);
			productnameTextView.setTypeface(myTypeface);
			//TextView myTextView1 = (TextView) view.findViewById(R.id.textView1);
			TextView myTextView2 = (TextView) view
					.findViewById(R.id.textViewdescription);
			//TextView myTextView3 = (TextView) view.findViewById(R.id.textView2);
			myTextView.setTypeface(myTypeface);
			//myTextView1.setTypeface(myTypeface);
			myTextView2.setTypeface(myTypeface);
			//myTextView3.setTypeface(myTypeface);
		/*	headerTextView.setTypeface(myTypeface);*/
		}

	}

	Typeface getRegularTypeFace() {
		return Typeface.createFromAsset(context.getAssets(),
				context.getString(R.string.regular_typeface));
	}

	@Override
	public void onResume() {
		super.onResume();
		// start auto scroll when onResume
	//	productDetailsImageView.startAutoScroll();
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
						BackToPageUrl();
						return true;
					}
				}
				return false;
			}
		});
		super.onActivityCreated(savedInstanceState);
	}
}
