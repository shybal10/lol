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
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.adapter.ViewPagerAdapter;
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.dialogfragment.DialogProductImage;
//import com.dairam.android.fragments.activity.dialogfragment.MyDialogFragmentSocialmediaShare;
import com.dairam.android.fragments.activity.interfaces.OnShowProductImageDialog;
import com.dairam.android.fragments.activity.interfaces.listenerHideView;
import com.dairam.android.fragments.activity.parserfiles.ImageLoader;
import com.dairam.android.fragments.activity.parserfiles.JSONfunctions;
import com.dairam.android.fragments.activity.utillities.Util;
import com.dairam.android.fragments.activity.viewpager.AutoScrollViewPager;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class BrandProductDetailsFragment extends Fragment {

	OnShowProductImageDialog showProdImg;
	String TAG = "BrandProductDetailsFragment";
	int maxStock = 0;
	LinearLayout linlay;
	String lan;
	Button bacButton;
	TextView headerTextView;
	final DecimalFormat df = new DecimalFormat("#.000");

	JSONObject jsonobject;
	JSONArray jsonarray;
	ProgressDialog mProgressDialog;
	ArrayList<HashMap<String, String>> arraylist;
	String quantity;
	String productid;
	String productName;
	String price;
	String backToPreviousUrl;
	String banerTitle;
	ScrollView scrl;
	AutoScrollViewPager viewPager;
	String CurrencyCode;

	ViewPagerAdapter viewPagerAdapter;

	Fragment fr;
	Context context;

	float productPriceValue;

	ImageLoader imageLoader;

	ViewPager productDetailsImageView;
	TextView productnameTextView;

	TextView descriptionTextView;
	ArrayList<HashMap<String, String>> arraylistcart;

	Activity sample;

	TextView amountButton;
	// Spinner quantityButton;
	TextView quantityText, lblMinus, lblPlus;

	String PRODUCTDETAILURL = "http://dairam.com/index.php?route=api/product/get&id=";

	String description, imageString, productPrice;
	String shareImageString;
	String shareLinkUrl, shareProdId;

	Button shareButton;

	com.dairam.android.fragments.activity.interfaces.listenerHideView listenerHideView;

	ImageButton cartItButton, wishlistitButton;
	String addToCartURL = "http://dairam.com/index.php?route=api/cart/get&cart=";
	String addToWishlistURl = "http://dairam.com/index.php?route=api/wishlist/get&wishlist=";
	CirclePageIndicator pagerIndicate;
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

	String dummyUrl;

	SharedPreferences sharedPreferences;

	private List<String> imageIdList;
	ViewPagerAdapter.OnProductClicked listener;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.promoitemdetails, container, false);
		// setupUI(v, false);
		quantity = getArguments().getString(
				AppConstants.HOME_SUB_OUT_OF_STOCK_FLAG);
		context = getActivity();
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
		/* productDetailsImageView = (ImageView)v.findViewById(R.id.imageView1); */
		productnameTextView = (TextView) v.findViewById(R.id.lblProductName);
		productnameTextView.setSelected(true);
		descriptionTextView = (TextView) v
				.findViewById(R.id.textViewdescription);
		scrl = (ScrollView) v.findViewById(R.id.scrollView1);

		amountButton.setSelected(true);
		pagerIndicate = (CirclePageIndicator) v.findViewById(R.id.indicator);

		quantityText = (TextView) v.findViewById(R.id.lblQtyValueCartDetails);
		lblMinus = (TextView) v.findViewById(R.id.lblMinusCartDetails);
		lblPlus = (TextView) v.findViewById(R.id.lblPlusCartDetails);

		lblPlus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int qty = Integer.parseInt(quantityText.getText().toString()
						.trim());
				double amt = Double.parseDouble(productPrice.substring(0,
						productPrice.length() - 3));

				// if(qty > 0 && qty < 11){
				if (qty > 0 && qty < maxStock + 1) {
					quantityText.setText("" + (++qty));

					amountButton.setText("" + df.format(amt * qty)
							+ CurrencyCode);
				}
				if (qty == maxStock) {
					lblPlus.setVisibility(View.INVISIBLE);
					lblMinus.setVisibility(View.VISIBLE);
				} else if (qty < maxStock && qty > 1) {
					lblPlus.setVisibility(View.VISIBLE);
					lblMinus.setVisibility(View.VISIBLE);
				}
			}
		});
		lblMinus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int qty = Integer.parseInt(quantityText.getText().toString()
						.trim());
				double amt = Double.parseDouble(productPrice.substring(0,
						productPrice.length() - 3));
				// if(qty > 1 && qty < 11)
				if (qty > 1 && qty < maxStock + 1) {
					quantityText.setText("" + (--qty));
					amountButton.setText("" + df.format(amt * qty)
							+ CurrencyCode);
				}
				if (qty == 1) {
					lblMinus.setVisibility(View.INVISIBLE);
					lblPlus.setVisibility(View.VISIBLE);
				} else if (qty > 1 && qty < maxStock) {
					lblPlus.setVisibility(View.VISIBLE);
					lblMinus.setVisibility(View.VISIBLE);
				}

			}
		});

		/*
		 * descriptionTextView.setOnTouchListener(new View.OnTouchListener() {
		 * 
		 * @Override public boolean onTouch(View v, MotionEvent event) {
		 * scrl.requestDisallowInterceptTouchEvent(true); return false; } });
		 * 
		 * descriptionTextView.setMovementMethod(new ScrollingMovementMethod());
		 */

		viewPager = (AutoScrollViewPager) v.findViewById(R.id.pager);
		cartItButton = (ImageButton) v.findViewById(R.id.buttoncartpromodet);
		wishlistitButton = (ImageButton) v
				.findViewById(R.id.buttonwishlistpromodet);
		/*
		 * leftarrowButton = (Button) v.findViewById(R.id.buttonleftarrow);
		 * rightArrowButton = (Button) v.findViewById(R.id.buttonrightarrow);
		 * leftarrowButton1 = (Button) v.findViewById(R.id.buttonleftarrow1);
		 * rightArrowButton1 = (Button) v.findViewById(R.id.buttonrightarrow1);
		 */

		productDetailsImageView = (ViewPager) v.findViewById(R.id.imageView1);

		imageIdList = new ArrayList<String>();

		showProdImg = new OnShowProductImageDialog() {

			@Override
			public void showDialog(List<String> images, int selectedPosition) {
				DialogProductImage myDialogFragmentProdImg = new DialogProductImage();
				myDialogFragmentProdImg.newInstance(context, images,
						selectedPosition);
				myDialogFragmentProdImg.show(getFragmentManager(),
						"myDialogProductImage");
			}
		};

		/*
		 * leftarrowButton.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * productDetailsImageView.scrollOnce(AutoScrollViewPager.LEFT); } });
		 * rightArrowButton.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * productDetailsImageView.scrollOnce(AutoScrollViewPager.RIGHT); } });
		 * leftarrowButton1.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * viewPager.scrollOnce(AutoScrollViewPager.LEFT); } });
		 * 
		 * rightArrowButton1.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * viewPager.scrollOnce(AutoScrollViewPager.RIGHT); } });
		 */
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
					bundle.putString("Page", "branddetails");
					bundle.putString("Bannertitle", productName);
					bundle.putString("urls", dummyUrl);
					bundle.putString("ID", product_id);
					bundle.putString("itemAction", AppConstants.CART_IT);
					bundle.putString("productname", productName);
					bundle.putString(
							AppConstants.HOME_SUB_OUT_OF_STOCK_FLAG,
							quantity);					
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
							+ "&quantity=" + quantityText.getText()
							+ "&language=1";

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

		List<String> list = new ArrayList<String>();

		/*
		 * if ((Integer.parseInt(quantity)<10)){ Log.e("entered into here",
		 * Integer.parseInt(quantity)+""); for (int i = 1; i <=
		 * Integer.parseInt(quantity); i++) { String dummy =
		 * Integer.toString(i); list.add(dummy) ; } }else{
		 */

		/*
		 * list.add("1"); list.add("2"); list.add("3"); list.add("4");
		 * list.add("5"); list.add("6"); list.add("7"); list.add("8");
		 * list.add("9"); list.add("10");
		 * 
		 * ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
		 * getActivity(), R.layout.spinneritems, list);
		 * dataAdapter.setDropDownViewResource(R.layout.spinner_single_item);
		 * quantityButton.setAdapter(dataAdapter);
		 */

		// addListenerOnSpinnerItemSelection();

		shareButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.e("Share Button", "In BrandProductDetailsFragment");
				String shareProdName = productnameTextView.getText().toString();
				String shareProdDescription = descriptionTextView.getText()
						.toString();
				String tempurl = shareLinkUrl + "&product_id=" + shareProdId;

//				MyDialogFragmentSocialmediaShare myDialogFragmentsocial = new MyDialogFragmentSocialmediaShare(shareProdName, shareProdDescription, shareImageString,
//						tempurl);
//
//				myDialogFragmentsocial.show(getFragmentManager(),
//						"myDialogFragment Share");

				String url=tempurl;

				Intent shareIntent = new Intent(Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				shareIntent.putExtra(Intent.EXTRA_TEXT, url);
				startActivity(Intent.createChooser(shareIntent, "Share Yaser Mall Application using"));
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

			/*
			 * productid = getArguments().getString("productid"); productName =
			 * getArguments().getString("productname"); price =
			 * getArguments().getString("Price"); backToPreviousUrl =
			 * getArguments().getString("BackToPreviousUrl"); banerTitle =
			 * getArguments().getString("title");
			 */

			SharedPreferences sp1 = getActivity().getSharedPreferences("tag",
					Context.MODE_PRIVATE);
			productid = sp1.getString("productid", "");
			productName = sp1.getString("productname", "");
			price = sp1.getString("Price", "");
			backToPreviousUrl = sp1.getString("BackToPreviousUrl", "");
			banerTitle = sp1.getString("title", "");

			/*
			 * String p = "fragmentpromodetails"; Editor editor = sp1.edit();
			 * editor.putString("act", p); editor.putString("productid", p);
			 * editor.putString("productname", p); editor.putString("Price", p);
			 * editor.putString("BackToPreviousUrl", p);
			 * editor.putString("title", p); editor.commit();
			 */

		} catch (Exception e) {
			System.out
					.println("Exception caught not possible to retrieve value from bundle");
		}
		Typeface fontFace = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/LibreBaskerville-Regular.otf");
		headerTextView.setTypeface(fontFace);
		headerTextView.setText(productName.toUpperCase());
		productnameTextView.setText(productName);
		dummyUrl = PRODUCTDETAILURL;

		SharedPreferences sp = getActivity().getSharedPreferences("lan",
				Context.MODE_PRIVATE);
		lan = sp.getString("lan", "en");

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
		/*
		 * if (lan == "en") { PRODUCTDETAILURL =
		 * PRODUCTDETAILURL+productid+"&language=1&currency=KWD"; } else{
		 * PRODUCTDETAILURL =
		 * PRODUCTDETAILURL+productid+"&language=2&currency=KWD"; }
		 */
		System.out
				.println("################### PRODUCT ID ##############################"
						+ productid);
		amountButton.setText(productPriceValue + "KD");
		new DownloadJSON().execute();

		listener = new ViewPagerAdapter.OnProductClicked() {

			@Override
			public void onClicked(String id) {
				PRODUCTDETAILURL = "http://dairam.com/index.php?route=api/product/get&id=";
				productid = id;
				Log.e("OnProductClicked",
						"In OnProductClicked Listener ProductId: " + id);
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
				fr = new BrandProductDetailsFragment();

				/*
				 * Bundle bundle=new Bundle(); bundle.putString("productid",
				 * product_id); bundle.putString("productname", product_name);
				 * bundle.putString("Price", productprice);
				 * bundle.putString("Bannertitle", bannerTitle);
				 * bundle.putString("referenceurl", dummyURL);
				 * System.out.println(
				 * "*********************produt*********************"
				 * +product_id);
				 */

				Bundle bundle = new Bundle();
				bundle.putString("productid", productid);
				bundle.putString("productname", productName);
				bundle.putString("Price", price);
				bundle.putString(AppConstants.HOME_SUB_OUT_OF_STOCK_FLAG,
						quantity);
				bundle.putString("BackToPreviousUrl", backToPreviousUrl);
				bundle.putString("title", banerTitle);

				fr.setArguments(bundle);
				FragmentManager fm = ((Activity) context).getFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
				fragmentTransaction.replace(R.id.fragment_place, fr);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
			}
		};

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
		listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) activity;
		listenerHideView.promoItemHideTopbar();
		listenerHideView.hideBottomBar();

		sample = activity;
		super.onAttach(activity);
	}

	private void BackToPageUrl() {
		// fr = new PromoItemFragment();
		listenerHideView.showBottomBar();
		fr = new BrandListingSingleFragment();
		Bundle bundle = new Bundle();
		bundle.putString("title", banerTitle);
		bundle.putString("referenceurl", backToPreviousUrl);
		fr.setArguments(bundle);
		FragmentManager fm = ((Activity) context).getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_place, fr);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}

	/*
	 * Asyn:Task for loading the JSON objects
	 */
	private class DownloadJSON extends AsyncTask<Void, Void, Void> {
		String prodName = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Create a progress dialog
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
			// Retrieve JSON Objects from the given URL address.....
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
					if (jsonobject.getBoolean("success")) {
						jsonobject = jsonobject.getJSONObject("product");
						prodName = jsonobject.getString("name");
						productName = jsonobject.getString("name");
						description = jsonobject.getString("description");
						imageString = jsonobject.getString("image");
						productPrice = jsonobject.getString("price");
						maxStock = Integer.parseInt(jsonobject.getString(
								"quantity").trim());

						shareImageString = jsonobject.getString("image");
						shareLinkUrl = jsonobject.getString("url");
						shareProdId = jsonobject.getString("id");

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

						// Locate the array name in JSON for Related
						// Products..................
						try {
							if (jsonobject
									.getJSONArray(AppConstants.SINGLE_PRODUCT_SUB_JSON_OBJECT) != null) {
								jsonarray = jsonobject
										.getJSONArray(AppConstants.SINGLE_PRODUCT_SUB_JSON_OBJECT);

								for (int i = 0; i < jsonarray.length(); i++) {
									HashMap<String, String> map = new HashMap<String, String>();
									jsonobject = jsonarray.getJSONObject(i);
									// Retrieve JSON Objects
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
								}

							}
						} catch (Exception e) {
							Log.e(TAG, "Exctption in Related Products");
							e.printStackTrace();
						}
						SharedPreferences sp1 = getActivity()
								.getSharedPreferences("tag",
										Context.MODE_PRIVATE);
						sp1.edit().putString("productid", productid).commit();

						// setting  TextViews.............................................
						descriptionTextView.setText(Html.fromHtml(description));
						productnameTextView.setText(prodName);
						headerTextView.setText(productName.toUpperCase());
						Log.e("Price in onPostExecute",
								"Price in OnPostExecute : " + productPrice);

						CurrencyCode = productPrice.substring(
								productPrice.length() - 2,
								productPrice.length());

						// CurrencyCode
						// Checking...........................................
						imageLoader = new ImageLoader(context);
						System.out.println("The currency........"
								+ CurrencyCode);
						if ((CurrencyCode.equals("KD"))
								|| (CurrencyCode.equals("BD"))
								|| (CurrencyCode.equals("QR"))
								|| (CurrencyCode.equals("SR"))) {

							try {
								productPriceValue = Float
										.parseFloat(productPrice.substring(0,
												productPrice.length() - 2));
							} catch (Exception e) {
								System.out.println("Cannot parse the value");
							}
						} else {
							try {
								productPriceValue = Float
										.parseFloat(productPrice.substring(0,
												productPrice.length() - 3));
							} catch (Exception e) {
								System.out.println("Cannot parse the value");
							}
						}
						// View Pager Adapter Setting for related products............
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
							viewPager.setAdapter(viewPagerAdapter);
							if (arraylist.size() > 1) {
								// leftarrowButton1.setVisibility(View.VISIBLE);
								// rightArrowButton1.setVisibility(View.VISIBLE);
							} else {
								// leftarrowButton1.setVisibility(View.INVISIBLE);
								// rightArrowButton1.setVisibility(View.INVISIBLE);
							}
						} else {
							// leftarrowButton1.setVisibility(View.INVISIBLE);
							// rightArrowButton1.setVisibility(View.INVISIBLE);
						}

						// amountButton.setText(productPriceValue + "KD");//
						// Arun
						// amountButton.setText(productPrice + "KD"); // added
						amountButton.setText(productPrice); // code
						// line

						// Product imageview view pager
						// updation.................................

						System.out.println("The image list........"
								+ imageIdList);

						if (imageIdList.size() > 1) {
							productDetailsImageView
									.setAdapter(new CustomviewPagerAdapter(
											context, imageIdList, showProdImg));
							productDetailsImageView
									.setOnPageChangeListener(new MyOnPageChangeListener());
							pagerIndicate.setViewPager(productDetailsImageView);
							/*
							 * productDetailsImageView.setInterval(4000);
							 * productDetailsImageView.startAutoScroll();
							 */
						} else {
							/*
							 * productDetailsImageView .setAdapter(new
							 * ImagePagerAdapter(context,
							 * imageIdList).setInfiniteLoop(false));
							 */
							productDetailsImageView
									.setAdapter(new CustomviewPagerAdapter(
											context, imageIdList, showProdImg));
							pagerIndicate.setViewPager(productDetailsImageView);
						}
					} else {
						Log.e(TAG, "Json giving Success:False");
						Toast.makeText(getActivity(), R.string.no_response,
								Toast.LENGTH_SHORT).show();
					}
					Log.e(TAG, "Maximum Stock : " + maxStock);
					if (maxStock > 10) {
						maxStock = 10;
					}
					if (maxStock == 1) {
						lblPlus.setVisibility(View.INVISIBLE);
						lblMinus.setVisibility(View.INVISIBLE);
					} else if (maxStock > 1) {
						lblMinus.setVisibility(View.INVISIBLE);
						lblPlus.setVisibility(View.VISIBLE);
					}
				} else {
					Toast.makeText(getActivity(), R.string.no_response,
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				Log.e("Error", "Outer  " + e.getMessage());
				e.printStackTrace();
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
			}

			else if (x == "8") {
				amountButton.setText(productPriceValue * 8 + "00"
						+ CurrencyCode);
			}

			else if (x == "9") {
				amountButton.setText(productPriceValue * 9 + "00"
						+ CurrencyCode);
			}

			else if (x == "10") {
				amountButton.setText(productPriceValue * 10 + "00"
						+ CurrencyCode);
			}

			else {
				amountButton.setText(productPriceValue * 1 + "00"
						+ CurrencyCode);
			}
		}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}
	}

	public void ShareDet() {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, productName + "\n"
				+ description);
		shareIntent.putExtra("Intent.EXTRA_TEXT", "http://dairam.com/");
		startActivity(Intent.createChooser(shareIntent, "Share Product"));
	}

	private void intShare() {
		// String[] blacklist = new String[]{"com.any.package",
		// "net.other.package"};
		String[] blacklist = new String[] {};
		// your share intent
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "some text");
		intent.putExtra(Intent.EXTRA_SUBJECT, "a subject");
		// ... anything else you want to add invoke custom chooser
		startActivity(generateCustomChooserIntent(intent, blacklist));
	}

	private Intent generateCustomChooserIntent(Intent prototype,
			String[] forbiddenChoices) {
		List<Intent> targetedShareIntents = new ArrayList<Intent>();
		List<HashMap<String, String>> intentMetaInfo = new ArrayList<HashMap<String, String>>();
		Intent chooserIntent;
		String[] nameOfAppsToShareWith = new String[] { "facebook", "twitter",
				"gmail", "instagram", "com.pinterest" };

		Intent dummy = new Intent(prototype.getAction());
		dummy.setType(prototype.getType());
		List<ResolveInfo> resInfo = getActivity().getPackageManager()
				.queryIntentActivities(dummy, 0);

		if (!resInfo.isEmpty()) {
			for (ResolveInfo resolveInfo : resInfo) {
				if (resolveInfo.activityInfo == null
						|| Arrays.asList(forbiddenChoices).contains(
								resolveInfo.activityInfo.packageName))
					continue;
				// Get all the posible sharers
				HashMap<String, String> info = new HashMap<String, String>();
				info.put("packageName", resolveInfo.activityInfo.packageName);
				info.put("className", resolveInfo.activityInfo.name);
				String appName = String.valueOf(resolveInfo.activityInfo
						.loadLabel(getActivity().getPackageManager()));
				info.put("simpleName", appName);
				// Add only what we want
				if (Arrays.asList(nameOfAppsToShareWith).contains(
						appName.toLowerCase())) {
					intentMetaInfo.add(info);
				}
			}

			if (!intentMetaInfo.isEmpty()) {
				// sorting for nice readability
				Collections.sort(intentMetaInfo,
						new Comparator<HashMap<String, String>>() {
							@Override
							public int compare(HashMap<String, String> map,
									HashMap<String, String> map2) {
								return map.get("simpleName").compareTo(
										map2.get("simpleName"));
							}
						});

				// create the custom intent list
				for (HashMap<String, String> metaInfo : intentMetaInfo) {
					Intent targetedShareIntent = (Intent) prototype.clone();
					targetedShareIntent.setPackage(metaInfo.get("packageName"));
					targetedShareIntent.setClassName(
							metaInfo.get("packageName"),
							metaInfo.get("className"));
					targetedShareIntents.add(targetedShareIntent);
				}
				// String shareVia = getString(R.string.offer_share_via);
				String shareVia = "Share Via";
				String shareTitle = shareVia.substring(0, 1).toUpperCase()
						+ shareVia.substring(1);
				chooserIntent = Intent.createChooser(targetedShareIntents
						.remove(targetedShareIntents.size() - 1), shareTitle);
				chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
						targetedShareIntents.toArray(new Parcelable[] {}));
				return chooserIntent;
			}
		}
		return Intent.createChooser(prototype, "Share via last");
	}

	class RetrieveFeedTask extends AsyncTask<Void, Void, Void> {

		String id;
		String cart_Count;

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
			Log.e(TAG, "AddtoCart : " + addToCartURL);
			jsonobject = JSONfunctions.getJSONfromURL(addToCartURL);
			return null;
		}

		@Override
		protected void onPostExecute(Void args) {
			mProgressDialog.dismiss();
			addToCartURL = "http://dairam.com/index.php?route=api/cart/get&cart=";

			try {
				if (jsonobject != null) {
					if (jsonobject.getBoolean("success")) {
						jsonobject = jsonobject.getJSONObject("customer");

						id = jsonobject.getString(AppConstants.CART_ID);
						cart_Count = jsonobject
								.getString(AppConstants.CART_COUNT);

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

							Editor editor = sharedPreferences.edit();
							editor.putString(cart_string, cart_Count);
							editor.commit();
						}
					} else {
						Log.e(TAG, "Response gives Success:False");
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
			Log.e(TAG, "Add to Wishlist: " + addToWishlistURl);
			jsonobject = JSONfunctions.getJSONfromURL(addToWishlistURl);

			return null;
		}

		@Override
		protected void onPostExecute(Void args) {
			mProgressDialog.dismiss();
			addToWishlistURl = "http://dairam.com/index.php?route=api/wishlist/get&wishlist=";

			try {
				if (jsonobject != null) {
					if (jsonobject.getBoolean("success")) {
						// Locate the array name in JSON
						jsonobject = jsonobject.getJSONObject("customer");

						id = jsonobject.getString(AppConstants.CART_ID);
						wishlist_count = jsonobject
								.getString(AppConstants.WISHLIST_COUNT);

						if (id == null) {
							Toast.makeText(
									getActivity(),
									getResources().getString(
											R.string.error_in_additem),
									Toast.LENGTH_SHORT).show();
						} else {
							if (getLanguage().equals("1"))
								Toast.makeText(getActivity(),
										"Item added to the wishlist",
										Toast.LENGTH_SHORT).show();
							else
								Toast.makeText(getActivity(),
										"تم إضافة المنتج إلى المفضلة ",
										Toast.LENGTH_SHORT).show();

							listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) sample;
							listenerHideView
									.wishListCountUpdation(wishlist_count);

							Editor editor = sharedPreferences.edit();
							editor.putString(wishlist_String, wishlist_count);
							editor.commit();
						}
					} else {
						Log.e(TAG, "Resoponse gives Success:False");
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

	public String getLanguage() {
		SharedPreferences sp = getActivity().getSharedPreferences("lan",
				Context.MODE_PRIVATE);
		if (sp.getString("lan", "en").equals("en"))
			return "1";
		else
			return "2";
	}

	//
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
		// productDetailsImageView.stopAutoScroll();

		if (mProgressDialog != null)
			mProgressDialog.dismiss();
	}

	@Override
	public void onResume() {
		super.onResume();
		// start auto scroll when onResume
		// productDetailsImageView.startAutoScroll();
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
		/*
		 * TextView myTextView = (TextView) view.findViewById(R.id.textView2);
		 * myTextView.setTypeface(myTypeface);
		 */
		headerTextView.setTypeface(myTypeface);
		productnameTextView.setTypeface(myTypeface);
		descriptionTextView.setTypeface(myTypeface);

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
