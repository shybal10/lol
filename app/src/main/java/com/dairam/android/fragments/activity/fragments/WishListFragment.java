package com.dairam.android.fragments.activity.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.adapter.WishlistScreenSubitemAdapter;
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.interfaces.listenerHideView;
import com.dairam.android.fragments.activity.parserfiles.ImageLoader;
import com.dairam.android.fragments.activity.parserfiles.JSONfunctions;
import com.dairam.android.fragments.activity.seekbarcomponents.RangeSeekBar;
import com.dairam.android.fragments.activity.utillities.PrefUtil;

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
import java.util.Arrays;
import java.util.HashMap;

public class WishListFragment extends Fragment {
	Button backButton;
	TextView headerTextView;
	Button sortButton;

	Point p;
	int HeightTopBar;

	String bannerTitle;
	String lan;
	Fragment fr;

	Activity sample;

	SharedPreferences sharedpreferences;

	String addToCartURL = AppConstants.WISHLIST_TO_CART_URL;
	// http://dairam.com/index.php?route=api/cart/wishlisttocart&wishlist=147&id=77&quantity=1&language=1

	String referenceURL = "http://dairam.com/index.php?route=api/wishlistitems/list&id=";
	// http://dairam.com/index.php?route=api/wishlistitems/list&id=77&language=1
	// -- Wishlist items

	JSONObject jsonobject;
	JSONArray jsonarray;
	GridView gridView;
	WishlistScreenSubitemAdapter adapter;
	ProgressDialog mProgressDialog;
	ArrayList<HashMap<String, String>> arraylist;
	ArrayList<HashMap<String, String>> arraylistcart;
	Context context;

	TextView errorTextView;

	LinearLayout linearLayout;
	ViewGroup linearLayoutForRangebar;

	int positionOfGrid;
	Boolean tempFlag = false;
	View oldviView;
	HashMap<String, String> singleItemData = new HashMap<String, String>();

	String dummyURl;
	/*
	 * Range bar sample
	 */
	int leftSeekbar, rightSeekbar;
	// Corresponds to Color.LTGRAY
	private static final int DEFAULT_BAR_COLOR = 0xffcccccc;
	private static final int DEFAULT_THUMB_COLOR = 0xffdedede;

	// Corresponds to android.R.color.holo_blue_light.
	private static final int DEFAULT_CONNECTING_LINE_COLOR = 0xffe30712;
	// private static final int HOLO_BLUE = 0xff33b5e5;

	// Sets the initial values such that the image will be drawn
	// private static final int DEFAULT_THUMB_COLOR_NORMAL = -1;
	// private static final int DEFAULT_THUMB_COLOR_PRESSED = -1;

	// Sets variables to save the colors of each attribute
	private int mBarColor = DEFAULT_BAR_COLOR;
	private int mConnectingLineColor = DEFAULT_CONNECTING_LINE_COLOR;
	private int mThumbColorNormal = DEFAULT_THUMB_COLOR;
	private int mThumbColorPressed = DEFAULT_THUMB_COLOR;

	LinearLayout linlay;

	int oldPosition;

	String removeFromWishlistUrl = "http://dairam.com/index.php?route=api/wishlist/remove&wishlist=";

	/*
	 * Range bar ends
	 */
	public static final String MyPREFERENCES = "MyPrefs";
	public static final String customerid = "id";
	public static final String firstname = "firstname";
	public static final String lastname = "lastname";
	public static final String email_string = "email";
	public static final String cart_string = "cart";
	public static final String wishlist_String = "wishlist";

	SharedPreferences sharedPreferences;
int grid_Position;
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_wishlist, container, false);
		

		Log.e("Wishlist", "wishlist1");
		sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES,
				Context.MODE_PRIVATE);
		ImageLoader imageLoader = new ImageLoader(getActivity());
		imageLoader.clearCache();

		SharedPreferences sp1 = getActivity().getSharedPreferences("tag",
				Context.MODE_PRIVATE);
		Editor editor = sp1.edit();
		editor.putString("act", "wishlistfragment");
		editor.commit();
 grid_Position = PrefUtil.getGridCount(getActivity());
		linlay = (LinearLayout) v.findViewById(R.id.linlaypromo);

		errorTextView = (TextView) v.findViewById(R.id.textView1error);

		backButton = (Button) v.findViewById(R.id.buttonbackhome);
		headerTextView = (TextView) v.findViewById(R.id.textviewheader);
		sortButton = (Button) v.findViewById(R.id.buttonsort);
		gridView = (GridView) v.findViewById(R.id.gridViewhomepageitems);
		linearLayout = (LinearLayout) v
				.findViewById(R.id.linearlayoutheaderpromo);

		errorTextView.setVisibility(View.GONE);
		gridView.setVisibility(View.VISIBLE);

		Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
				R.drawable.dairam_header2);

		HeightTopBar = largeIcon.getHeight();

		System.out.println("-----------------------" + largeIcon.getHeight()
				+ "----------------------");
		/* HeightTopBar = linearLayout.getHeight(); */
		sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES,
				Context.MODE_PRIVATE);
		linlay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("Please do nothing");
			}
		});
		Typeface fontFace = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/LibreBaskerville-Regular.otf");
		headerTextView.setTypeface(fontFace);
		context = getActivity();
		/*
		 * Getting value from the Bundle
		 */
		try {

			String id = null;
			if (sharedPreferences.contains(customerid)) {
				id = sharedPreferences.getString(customerid, "");
			}
			if (id == null) {
				Toast.makeText(getActivity(),
						getResources().getString(R.string.must_login),
						Toast.LENGTH_SHORT).show();
				fr = new LoginFragment();
				Bundle bundle = new Bundle();
				bundle.putString("Page", "wishlist");
				bundle.putString("Bannertitle", bannerTitle);
				bundle.putString("urls", dummyURl);
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

				referenceURL = referenceURL + id + "&language=" + lang;
				new DownloadJSON().execute();
			}

			dummyURl = referenceURL;
			System.out.println("........" + "reference url" + referenceURL);
		} catch (Exception e) {

			System.out.println("Exception caught" + e);

		}

		SharedPreferences sp = getActivity().getSharedPreferences("lan",
				Context.MODE_PRIVATE);
		lan = sp.getString("lan", "en");

		switch (lan) {
		case "en":
			System.out.println("Entered herennnnnnn");
			referenceURL = referenceURL;
			break;

		default:
			System.out.println("Entered here.......nnnnnnn");
			referenceURL = referenceURL + "&language=2&currency=KWD";
			break;
		}
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				LoadHomePage();
			}
		});

		headerTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				LoadHomePage();
			}
		});
		int[] location = new int[2];

		// Get the x, y location and store it in the location[] array
		// location[0] = x, location[1] = y.
		sortButton.getLocationOnScreen(location);

		// Initialize the Point with x, and y positions
		p = new Point();
		p.x = location[0];
		p.y = location[1];

		sortButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				SharedPreferences sp = getActivity().getSharedPreferences(
						"lan", Context.MODE_PRIVATE);
				String lan = sp.getString("lan", "en");

				switch (lan) {
				case "en":
					showPopup(context, p);
					break;

				default:
					showPopupar(context, p);
					break;
				}
			}
		});

		/*
		 * OnClick Listener for grid view
		 */
		gridView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
PrefUtil.setGridcount(getActivity(), position);
				positionOfGrid = position;

				Button addToCartButton = (Button) arg1
						.findViewById(R.id.buttoncart);
				Button detailsButton = (Button) arg1
						.findViewById(R.id.buttondetails);
				Button deleteButton = (Button) arg1
						.findViewById(R.id.buttondelete);
				TableRow tableRow = (TableRow) arg1
						.findViewById(R.id.tableRowonclickview);

				if (tempFlag == false) {

					oldviView = arg1;
					addToCartButton.setVisibility(View.VISIBLE);
					detailsButton.setVisibility(View.VISIBLE);
					tempFlag = true;
					tableRow.setVisibility(View.VISIBLE);
					deleteButton.setVisibility(View.VISIBLE);
					tableRow.setBackgroundResource(R.drawable.transbg_withclose);

					oldPosition = position;
				} else {
					Button oldcart = (Button) oldviView
							.findViewById(R.id.buttoncart);
					Button olddetails = (Button) oldviView
							.findViewById(R.id.buttondetails);
					Button olddelete = (Button) oldviView
							.findViewById(R.id.buttondelete);
					TableRow oldTableRow = (TableRow) oldviView
							.findViewById(R.id.tableRowonclickview);

					oldcart.setVisibility(View.INVISIBLE);
					olddetails.setVisibility(View.INVISIBLE);
					olddelete.setVisibility(View.INVISIBLE);
					oldTableRow
							.setBackgroundResource(android.R.color.transparent);

					addToCartButton.setVisibility(View.VISIBLE);
					detailsButton.setVisibility(View.VISIBLE);
					deleteButton.setVisibility(View.VISIBLE);
					tableRow.setVisibility(View.VISIBLE);
					tableRow.setBackgroundResource(R.drawable.transbg_withclose);
					tempFlag = true;

					oldviView = arg1;
					oldPosition = position;
				}

				tableRow.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						adapter.notifyDataSetChanged();
					}
				});

				deleteButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						String id = null;

						System.out.println("Add to cart button clicked");

						if (sharedPreferences.contains(customerid)) {
							id = sharedPreferences.getString(customerid, "");
						}

						if (id == null) {
							singleItemData = arraylist.get(positionOfGrid);
							// String product_id =
							// singleItemData.get(AppConstants.SUB_CATEGORY1_ID);
							String product_id = singleItemData.get("id");
							fr = new LoginFragment();
							Bundle bundle = new Bundle();
							bundle.putString("Page", "Wishlist");
							bundle.putString("Bannertitle", bannerTitle);
							bundle.putString("urls", dummyURl);
							bundle.putString("ID", product_id);
							bundle.putString("itemAction", AppConstants.CART_IT);
							fr.setArguments(bundle);
							FragmentManager fm = ((Activity) context)
									.getFragmentManager();
							FragmentTransaction fragmentTransaction = fm
									.beginTransaction();
							fragmentTransaction
									.replace(R.id.fragment_place, fr);
							fragmentTransaction.addToBackStack(null);
							fragmentTransaction.commit();
						}

						else {

							singleItemData = arraylist.get(positionOfGrid);
							String product_id = singleItemData.get("id");
							System.out.println("Login Done!!!" + product_id);
							removeFromWishlistUrl = removeFromWishlistUrl
									+ product_id + "&id=" + id + "&language=1";

							new RetrieveFeedTaskRemoveWishlist().execute();
						}

						System.out.println("Add to cart button clicked");
						adapter.notifyDataSetChanged();
					}
				});

				addToCartButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {

						String id = null;

						System.out.println("Add to cart button clicked");
						Log.e("Arylst inaddtocabutton",
								arraylist.toString());
						Log.e("Position of Grid", "" + positionOfGrid);

						if (sharedPreferences.contains(customerid)) {
							id = sharedPreferences.getString(customerid, "");
						}
						Log.e("customerId", id);
						if (id == null) {
							singleItemData = arraylist.get(positionOfGrid);
							// String product_id =
							// singleItemData.get(AppConstants.SUB_CATEGORY1_ID).toString();
							String product_id = singleItemData.get("id")
									.toString();
							fr = new LoginFragment();
							Bundle bundle = new Bundle();
							bundle.putString("Page", "Promo");
							bundle.putString("Bannertitle", bannerTitle);
							bundle.putString("urls", dummyURl);
							bundle.putString("ID", product_id);
							bundle.putString("itemAction", AppConstants.CART_IT);
							fr.setArguments(bundle);
							FragmentManager fm = ((Activity) context)
									.getFragmentManager();
							FragmentTransaction fragmentTransaction = fm
									.beginTransaction();
							fragmentTransaction
									.replace(R.id.fragment_place, fr);
							fragmentTransaction.addToBackStack(null);
							fragmentTransaction.commit();
						}

						else {

							singleItemData = arraylist.get(positionOfGrid);
							// String product_id =
							// singleItemData.get(AppConstants.SUB_CATEGORY1_ID).toString();
							String product_id = singleItemData.get("id")
									.toString();
							System.out.println("Login Done!!!" + product_id);
							String lang = getLanguage();
							addToCartURL = addToCartURL + product_id + "&id="
									+ id + "&quantity=1&language=" + lang;

							new RetrieveFeedTask().execute();
						}

						System.out.println("Add to cart button clicked");
						adapter.notifyDataSetChanged();
					}
				});

				detailsButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						singleItemData = null;
						singleItemData = arraylist.get(positionOfGrid);

						// Toast.makeText(getActivity(),singleItemData.toString(),
						// Toast.LENGTH_SHORT).show();

						// String product_id =
						// singleItemData.get(AppConstants.SUB_CATEGORY1_ID);
						String product_id = singleItemData.get("id").toString();
						String product_name = singleItemData.get(
								AppConstants.SUB_CATEGORY1_NAME).toString();
						String productprice = singleItemData.get(
								AppConstants.SUB_CATEGORY1_PRICE).toString();

						System.out.println("The MAin URL for keeping session"
								+ referenceURL);

						fr = new WishListSingleItemDetails();
						Bundle bundle = new Bundle();
						Log.e("id", "" + product_id);
						bundle.putString("productid", product_id);
						bundle.putString("productname", product_name);
						bundle.putString("Price", productprice);
						bundle.putString("BackToPreviousUrl", dummyURl);
						bundle.putString("title", bannerTitle);

						fr.setArguments(bundle);
						FragmentManager fm = ((Activity) context)
								.getFragmentManager();
						FragmentTransaction fragmentTransaction = fm
								.beginTransaction();
						fragmentTransaction.replace(R.id.fragment_place, fr);
						fragmentTransaction.addToBackStack(null);
						fragmentTransaction.commit();

					}
				});

			}
		});

		linearLayoutForRangebar = (ViewGroup) v.findViewById(R.id.mylayout);
		RangeSeekBar<Integer> seekBar = new RangeSeekBar<Integer>(0, 1000,
				context);
		seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
			@Override
			public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar,
					Integer minValue, Integer maxValue) {
				// handle changed range values
				Log.i("TAG", "User selected new range values: MIN=" + minValue
						+ ", MAX=" + maxValue);

				referenceURL = referenceURL + "&lower=" + minValue + "&higher="
						+ maxValue;
				new DownloadJSON().execute();
			}
		});

		// linearLayoutForRangebar.addView(seekBar);
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
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		bundle.putInt("BAR_COLOR", mBarColor);
		bundle.putInt("CONNECTING_LINE_COLOR", mConnectingLineColor);
		bundle.putInt("THUMB_COLOR_NORMAL", mThumbColorNormal);
		bundle.putInt("THUMB_COLOR_PRESSED", mThumbColorPressed);
	}

	@Override
	public void onAttach(Activity activity) {
		sample = activity;
		listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) activity;
		listenerHideView.makeDefaultHeader();
		listenerHideView.promoItemHideTopbar();
		listenerHideView.setTopBarForWishlist();

		super.onAttach(activity);
	}

	public void LoadHomePage() {
		/*fr = new FragmentOne();
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

	public String getLanguage() {
		SharedPreferences sp = getActivity().getSharedPreferences("lan",
				Context.MODE_PRIVATE);
		if (sp.getString("lan", "en").equals("en"))
			return "1";
		else
			return "2";
	}

	// DownloadJSON AsyncTask
	private class DownloadJSON extends AsyncTask<Void, Void, Void> {

		int wishlistCount = 0;

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
			// Retrieve JSON Objects from the given URL address
			// Retrieve JSON Objects from the given URL address
			HttpParams params1 = new BasicHttpParams();
			HttpProtocolParams.setVersion(params1, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params1, "UTF-8");
			params1.setBooleanParameter("http.protocol.expect-continue", false);
			HttpClient httpclient = new DefaultHttpClient(params1);

			Log.e("PRODUCTDETAILURL", referenceURL);

			HttpPost httppost = new HttpPost(referenceURL);
			try {
				HttpResponse http_response = httpclient.execute(httppost);

				HttpEntity entity = http_response.getEntity();
				String jsonText = EntityUtils.toString(entity, HTTP.UTF_8);
				Log.i("Response", jsonText);
				System.out
						.println("$$$$$$$$$$$$$$$$$$$$$$$$$$zfdsfddgfdgdg$$$$$$$$$$$$$$$$$$$$$$$$$$  "
								+ jsonText);
				jsonobject = new JSONObject(jsonText);

			} catch (Exception e) {

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void args) {

			try {

				if (jsonobject != null) {
					if (jsonobject.getBoolean("success")) {
						// Locate the array name in JSON
						jsonarray = jsonobject.getJSONArray("products");

						wishlistCount = jsonarray.length();
						for (int i = 0; i < jsonarray.length(); i++) {
							HashMap<String, String> map = new HashMap<String, String>();
							jsonobject = jsonarray.getJSONObject(i);
							// Retrive JSON Objects
							map.put("id", jsonobject.getString("id"));
							map.put("name", jsonobject.getString("name"));
							map.put("price", jsonobject.getString("price"));
							/*
							 * map.put(AppConstants.HOME_SUB_SPECIAL,
							 * jsonobject.
							 * getString(AppConstants.HOME_SUB_SPECIAL));
							 */
							map.put("href", jsonobject.getString("href"));
							map.put("thumb", jsonobject.getString("thumb"));
							/*
							 * map.put(AppConstants.HOME_SUB_LANG,
							 * jsonobject.getString
							 * (AppConstants.HOME_SUB_LANG));
							 * map.put(AppConstants.HOME_SUB_CURRENCY,
							 * jsonobject.getString
							 * (AppConstants.HOME_SUB_CURRENCY));
							 */
							// Set the JSON Objects into the array
							arraylist.add(map);
							Log.e("Arraylist in Wishlist", arraylist.toString());
						}

					} else {
						//Toast.makeText(getActivity(), R.string.no_response,Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getActivity(), R.string.no_response,
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}

			// /////////////////////////////////////////////////
			listenerHideView listenerHideView;
			if (arraylist.size() > 0) {
				// Pass the results into ListViewAdapter.java
				adapter = new WishlistScreenSubitemAdapter(context, arraylist);
				// Set the adapter to the ListView
				gridView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				try{
					gridView.setSelection(grid_Position);	
				}catch(Exception e){
					gridView.setSelection(0);	
				}
				listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) sample;
				listenerHideView.wishListCountUpdation("" + wishlistCount);

				sharedpreferences.edit()
						.putString("wishlist", "" + wishlistCount).commit();
			} else {
				errorTextView.setVisibility(View.VISIBLE);
				gridView.setVisibility(View.GONE);
				sharedpreferences.edit().putString("wishlist", "0").commit();
				listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) sample;
				listenerHideView.wishListCountUpdation("0");
			}

			// Close the progressdialog
			mProgressDialog.dismiss();
		}
	}

	@Override
	public void onPause() {

		super.onPause();
		if (mProgressDialog != null)
			mProgressDialog.dismiss();
	}

	public void onWindowFocusChanged(boolean hasFocus) {

		int[] location = new int[2];

		// Get the x, y location and store it in the location[] array
		// location[0] = x, location[1] = y.
		sortButton.getLocationOnScreen(location);

		// Initialize the Point with x, and y positions
		p = new Point();
		p.x = location[0];
		p.y = location[1];
	}

	// The method that displays the pop up.
	private void showPopup(Context context1, Point p) {

		Bitmap largeIcon1 = BitmapFactory.decodeResource(getResources(),
				R.drawable.listing_box);

		int popupWidth = largeIcon1.getWidth();
		int popupHeight = largeIcon1.getHeight();

		// Inflate the popup_layout.xml
		LinearLayout viewGroup = (LinearLayout) getActivity().findViewById(
				R.id.popups);
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = layoutInflater.inflate(R.layout.layout_popup, viewGroup);

		// Creating the PopupWindow
		final PopupWindow popup = new PopupWindow(context);
		popup.setContentView(layout);
		popup.setWidth(popupWidth);
		popup.setHeight(popupHeight);
		popup.setFocusable(true);

		// Some offset to align the popup a bit to the right, and a bit down,
		// relative to button's position.
		int OFFSET_X = 30;
		int OFFSET_Y = 30;

		// Clear the default translucent background
		popup.setBackgroundDrawable(new BitmapDrawable());

		Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
				R.drawable.dairam_header2);

		HeightTopBar = largeIcon.getHeight();

		System.out.println("-----------------------" + largeIcon.getHeight()
				+ "----------------------");
		HeightTopBar = linearLayout.getHeight();

		// Displaying the pop up at the specified location, + offsets.

		popup.showAtLocation(layout, Gravity.RIGHT | Gravity.TOP, p.x
				+ OFFSET_X - 15, p.y + OFFSET_Y + HeightTopBar - 25);

		// storing string resources into Array
		String[] popupitem = getResources().getStringArray(R.array.popuparray);

		// Button sortstandardorder =
		// (Button)layout.findViewById(R.id.buttonstandardorder);
		Button sortnewest = (Button) layout
				.findViewById(R.id.button2sortbynewest);
		Button sortpopularity = (Button) layout
				.findViewById(R.id.button3sortbypopularity);
		// Button sortrandom = (Button)layout.findViewById(R.id.button4random);

		/*
		 * sortstandardorder.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { popup.dismiss(); new
		 * DownloadJSON().execute(); } });
		 */

		sortnewest.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				referenceURL = referenceURL
						+ "&sort=newest&lower=1&higher=1000";
				popup.dismiss();
				new DownloadJSON().execute();
			}
		});

		sortpopularity.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				referenceURL = referenceURL
						+ "&sort=popular&lower=1&higher=1000";
				popup.dismiss();
				new DownloadJSON().execute();
			}
		});

		/*
		 * sortrandom.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { referenceURL = referenceURL
		 * +"&sort=random&lower=1&higher=1000"; popup.dismiss(); new
		 * DownloadJSON().execute(); } });
		 */

		ArrayList<String> ppopupItemList = new ArrayList<String>();
		ppopupItemList.addAll(Arrays.asList(popupitem));

		ListAdapter listAdapter = new ArrayAdapter<String>(getActivity(),
				R.layout.layout_popup_singleitem, ppopupItemList);
	}

	class RetrieveFeedTask extends AsyncTask<Void, Void, Void> {

		String id;
		String cart_Count, wishlist_Count;

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
			arraylistcart = new ArrayList<HashMap<String, String>>();

			// Retrieve JSON Objects from the given URL address
			Log.e("addtoCardURLinWishlist", addToCartURL);
			jsonobject = JSONfunctions.getJSONfromURL(addToCartURL);

			return null;
		}

		@Override
		protected void onPostExecute(Void args) {
			mProgressDialog.dismiss();
			addToCartURL = AppConstants.WISHLIST_TO_CART_URL;
			try {

				if (jsonobject != null) {
					if (jsonobject.getBoolean("success")) {
						// Locate the array name in JSON
						jsonobject = jsonobject.getJSONObject("customer");

						id = jsonobject.getString(AppConstants.CART_ID);
						cart_Count = jsonobject
								.getString(AppConstants.CART_COUNT);
						wishlist_Count = jsonobject
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
										"Item added to the cart",
										Toast.LENGTH_SHORT).show();
							else
								Toast.makeText(getActivity(),
										"تم إضافة المنتج إلى السلة ",
										Toast.LENGTH_SHORT).show();

							sharedpreferences = getActivity()
									.getSharedPreferences(MyPREFERENCES,
											Context.MODE_PRIVATE);
							sharedpreferences.edit()
									.putString("cart", cart_Count).commit();
							sharedpreferences.edit()
									.putString("wishlist", wishlist_Count)
									.commit();

							listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) sample;
							listenerHideView.cartListCountUpdation(cart_Count);
							;
							listenerHideView
									.wishListCountUpdation(wishlist_Count);
							;

							fr = new WishListFragment();
							FragmentManager fm = getFragmentManager();
							FragmentTransaction fragmentTransaction = fm
									.beginTransaction();
							fragmentTransaction
									.replace(R.id.fragment_place, fr);
							fragmentTransaction.addToBackStack(null);
							fragmentTransaction.commit();
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

	class RetrieveFeedTaskRemoveWishlist extends AsyncTask<Void, Void, Void> {

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
			jsonobject = JSONfunctions.getJSONfromURL(removeFromWishlistUrl);

			return null;
		}

		@Override
		protected void onPostExecute(Void args) {
			mProgressDialog.dismiss();
			removeFromWishlistUrl = "http://dairam.com/index.php?route=api/wishlist/remove&wishlist=";
			try {
				if (jsonobject != null) {
					if (jsonobject.getBoolean("success")) {
						// Locate the array name in JSON
						jsonobject = jsonobject.getJSONObject("customer");

						id = jsonobject.getString(AppConstants.CART_ID);
						wishlist_count = jsonobject
								.getString(AppConstants.WISHLIST_COUNT);

						if (id == null) {
							Toast.makeText(getActivity(),
									"Error in removing the item",
									Toast.LENGTH_SHORT).show();
						} else {
							if (getLanguage().equals("1"))
								Toast.makeText(getActivity(),
										"Item removed from wishlist",
										Toast.LENGTH_SHORT).show();
							else
								Toast.makeText(getActivity(), "تم حذف المنتج ",
										Toast.LENGTH_SHORT).show();

							listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) sample;
							listenerHideView
									.wishListCountUpdation(wishlist_count);
							// Here updating the wishlist count

							Editor editor = sharedpreferences.edit();
							editor.putString(wishlist_String, wishlist_count);
							editor.commit();
							fr = new WishListFragment();
							FragmentManager fm = getFragmentManager();
							FragmentTransaction fragmentTransaction = fm
									.beginTransaction();
							fragmentTransaction
									.replace(R.id.fragment_place, fr);
							fragmentTransaction.addToBackStack(null);
							fragmentTransaction.commit();
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

	private void showPopupar(Context context, Point p) {
		final Context context2 = context;
		// TODO Auto-generated method stub
		Bitmap largeIcon1 = BitmapFactory.decodeResource(getResources(),
				R.drawable.listing_box);

		int popupWidth = largeIcon1.getWidth();
		int popupHeight = largeIcon1.getHeight() + 10;

		// Inflate the popup_layout.xml
		LinearLayout viewGroup = (LinearLayout) getActivity().findViewById(
				R.id.popups);
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = layoutInflater.inflate(R.layout.layout_popup_ar,
				viewGroup);

		// Creating the PopupWindow
		final PopupWindow popup = new PopupWindow(context);
		popup.setContentView(layout);
		popup.setWidth(popupWidth);
		popup.setHeight(popupHeight);
		popup.setFocusable(true);

		// Some offset to align the popup a bit to the right, and a bit down,
		// relative to button's position.
		int OFFSET_X = 30;
		int OFFSET_Y = 30;

		// Clear the default translucent background
		popup.setBackgroundDrawable(new BitmapDrawable());

		Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
				R.drawable.dairam_header2);

		HeightTopBar = largeIcon.getHeight();

		popup.showAtLocation(layout, Gravity.LEFT | Gravity.TOP, p.x + OFFSET_X
				- 15, p.y + OFFSET_Y + HeightTopBar - 68);

		// storing string resources into Array
		String[] popupitem = getResources().getStringArray(R.array.popuparray);
		/*
		 * ListView listView =
		 * (ListView)layout.findViewById(R.id.listViewpopup);
		 */

		// Button sortstandardorder =
		// (Button)layout.findViewById(R.id.buttonstandardorder_ar);
		Button sortnewest = (Button) layout
				.findViewById(R.id.button2sortbynewest_ar);
		Button sortpopularity = (Button) layout
				.findViewById(R.id.button3sortbypopularity_ar);
		// Button sortrandom =
		// (Button)layout.findViewById(R.id.button4random_ar);

		/*
		 * sortstandardorder.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) {
		 * 
		 * fr = new PromoItemFragment(); Bundle bundle=new Bundle();
		 * bundle.putString("title",bannerTitle );
		 * bundle.putString("referenceurl", referenceURL);
		 * fr.setArguments(bundle); FragmentManager fm = ((Activity)
		 * context2).getFragmentManager(); FragmentTransaction
		 * fragmentTransaction = fm.beginTransaction();
		 * fragmentTransaction.replace(R.id.fragment_place, fr);
		 * fragmentTransaction.addToBackStack(null);
		 * fragmentTransaction.commit(); popup.dismiss();
		 * 
		 * } });
		 */

		sortnewest.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("THE REFERENCE URL IS" + referenceURL);
				referenceURL = referenceURL
						+ "&sort=newest&lower=1&higher=1000";

				fr = new PromoItemFragment();
				Bundle bundle = new Bundle();
				bundle.putString("title", bannerTitle);
				bundle.putString("referenceurl", referenceURL);
				fr.setArguments(bundle);
				FragmentManager fm = ((Activity) context2).getFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
				fragmentTransaction.replace(R.id.fragment_place, fr);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
				popup.dismiss();

			}
		});

		sortpopularity.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("THE REFERENCE URL IS" + referenceURL);
				referenceURL = referenceURL
						+ "&sort=popular&lower=1&higher=1000";

				fr = new PromoItemFragment();
				Bundle bundle = new Bundle();
				bundle.putString("title", bannerTitle);
				bundle.putString("referenceurl", referenceURL);
				fr.setArguments(bundle);
				FragmentManager fm = ((Activity) context2).getFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
				fragmentTransaction.replace(R.id.fragment_place, fr);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
				popup.dismiss();
			}
		});

		/*
		 * sortrandom.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * System.out.println("THE REFERENCE URL IS"+referenceURL); referenceURL
		 * = referenceURL +"&sort=random&lower=1&higher=1000";
		 * 
		 * fr = new PromoItemFragment(); Bundle bundle=new Bundle();
		 * bundle.putString("title",bannerTitle );
		 * bundle.putString("referenceurl", referenceURL);
		 * fr.setArguments(bundle); FragmentManager fm = ((Activity)
		 * context2).getFragmentManager(); FragmentTransaction
		 * fragmentTransaction = fm.beginTransaction();
		 * fragmentTransaction.replace(R.id.fragment_place, fr);
		 * fragmentTransaction.addToBackStack(null);
		 * fragmentTransaction.commit(); popup.dismiss(); } });
		 */

		ArrayList<String> ppopupItemList = new ArrayList<String>();
		ppopupItemList.addAll(Arrays.asList(popupitem));

		ListAdapter listAdapter = new ArrayAdapter<String>(getActivity(),
				R.layout.layout_popup_singleitem, ppopupItemList);

	}

	public void setupUI(View view, boolean isHeading) {
		
		  Typeface myTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/HELVETICANEUELTARABIC-ROMAN.TTF");
		  if(getLanguage().equals("1")){
			  myTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Georgia.ttf");
		  }else{			  
		   //  TextView myTextView = (TextView)view.findViewById(R.id.textView1);
		     headerTextView.setTypeface(myTypeface);
		  }
		   
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
						PrefUtil.setGridcount(getActivity(), 0);
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
