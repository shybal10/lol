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
import android.view.View.OnClickListener;
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

import com.crashlytics.android.answers.AddToCartEvent;
import com.crashlytics.android.answers.Answers;
import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.adapter.SubCategorySingleItemAdapter;
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.connectioncheck.ConnectionDetector;
import com.dairam.android.fragments.activity.interfaces.listenerHideView;
import com.dairam.android.fragments.activity.parserfiles.ImageLoader;
import com.dairam.android.fragments.activity.parserfiles.JSONfunctions;
import com.dairam.android.fragments.activity.seekbarcomponents.RangeSeekBar;
import com.dairam.android.fragments.activity.utillities.PrefUtil;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.HashMap;

public class CategoryProductsFragment extends Fragment {
	String TAG = "CategoryProductsFragment";
	JSONObject jsonobject;
	JSONArray jsonarray;
	GridView gridView;
	TextView errorText;
	SubCategorySingleItemAdapter adapter;
	ProgressDialog mProgressDialog;
	ArrayList<HashMap<String, String>> arraylist;
	Boolean tempFlag = false;
	View oldviView;
	Activity sample;
	ViewGroup linearLayoutForRangebar;

	String bannerString;
	String referenceurl;
	String tempUrl;
	String dummyUrl;
	String backtoPrevurl, backtitle;

	//Fabric put Variables...........
	String fabricProdId = "",fabricProdName = "",fabricProdPrice = "";

	Fragment fr;
	int positionOfGrid;

	ConnectionDetector cd;

	HashMap<String, String> singleItemData = new HashMap<String, String>();

	Boolean isInternetPresent = false;

	Context context;

	Button backButton;
	TextView backTextView;

	int HeightTopBar;

	Button sortButton;
	SharedPreferences sharedPreferences;

	String category_id;

	boolean backSelector = false;

	public static final String MyPREFERENCES = "MyPrefs";
	public static final String customerid = "id";
	public static final String firstname = "firstname";
	public static final String lastname = "lastname";
	public static final String email_string = "email";
	public static final String cart_string = "cart";
	public static final String wishlist_String = "wishlist";

	LinearLayout linearLayout;

	String addToCartURL = AppConstants.ADD_TO_CART_URL;
	Point p;

	ArrayList<HashMap<String, String>> arraylistcart;
	int grid_Position;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_categoryproducts,
				container, false);
		
		grid_Position = PrefUtil.getGridCount(getActivity());

		ImageLoader imageLoader = new ImageLoader(getActivity());
		imageLoader.clearCache();

		gridView = (GridView) v.findViewById(R.id.gridViewcategories);
		errorText = (TextView) v.findViewById(R.id.errortextSubCat);
		backButton = (Button) v.findViewById(R.id.buttonbackcatpro);
		backTextView = (TextView) v.findViewById(R.id.textViewbackcatpro);

		sortButton = (Button) v.findViewById(R.id.buttonsortcatpro);
		linearLayout = (LinearLayout) v
				.findViewById(R.id.linearlayoutheadersale);

		gridView.setVisibility(View.VISIBLE);
		errorText.setVisibility(View.GONE);

		sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES,
				Context.MODE_PRIVATE);
		int[] location = new int[2];

		// Get the x, y location and store it in the location[] array
		// location[0] = x, location[1] = y.
		sortButton.getLocationOnScreen(location);

		// Initialize the Point with x, and y positions
		p = new Point();
		p.x = location[0];
		p.y = location[1];

		sortButton.setOnClickListener(new OnClickListener() {
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

		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				backToPage();
			}
		});

		backTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				backToPage();
			}
		});

		try {
			bannerString = getArguments().getString("title");
			tempUrl = getArguments().getString("referenceurl");
			backtoPrevurl = getArguments().getString("firstlevel");
			backtitle = getArguments().getString("firstleveltitle");
			category_id = getArguments().getString("categoryid");

			try {
				backSelector = getArguments().getBoolean(
						AppConstants.BACKSELECTOR);
				Log.e("CategoryProductsFrgmt", "No Exception");
			} catch (Exception e) {
				backSelector = false;
				Log.e("CategoryProductsFragme",
						"Exception occured while getting bundle arguments");
				e.printStackTrace();
			}
			Log.e(TAG, "BackSelector : " + backSelector);

			SharedPreferences sp1 = getActivity().getSharedPreferences("tag",
					Context.MODE_PRIVATE);
			String p = "fragmentSubcategorygrid";
			Editor editor = sp1.edit();
			editor.putString("act", p);
			editor.putString("title", bannerString);
			editor.putString("referenceurl", tempUrl);
			editor.putString("firstlevel", backtoPrevurl);
			editor.putString("firstleveltitle", backtitle);
			editor.putString("categoryid", category_id);
			editor.putBoolean(AppConstants.BACKSELECTOR, backSelector);
			editor.commit();

		} catch (Exception e) {
			System.out.println("Exception caught" + e);
			Log.e("Exception", e.toString());
		}
		
		/*
		 * Typeface fontFace =
		 * Typeface.createFromAsset(getActivity().getAssets(),
		 * "fonts/LibreBaskerville-Regular.otf");
		 * backTextView.setTypeface(fontFace);
		 */

		if (bannerString.length() > 10) {

			String sample_dummyString = bannerString.substring(0, 10)
					.toString() + "...";
			backTextView.setText(sample_dummyString.toUpperCase());
		} else {
			backTextView.setText(bannerString.toUpperCase());
		}

		SharedPreferences sp = getActivity().getSharedPreferences("lan",
				Context.MODE_PRIVATE);
		String lan = sp.getString("lan", "en");
		Log.d("language", lan);

		referenceurl = referenceurl + "&category=" + category_id;
		tempUrl = tempUrl + "&category=" + category_id;
		switch (lan) {
		case "en":
			referenceurl = tempUrl + "&language=1&currency=KWD";
			break;

		default:
			referenceurl = tempUrl + "&language=2&currency=KWD";
			break;
		}
		dummyUrl = referenceurl;
		context = getActivity();

		cd = new ConnectionDetector(context);
		isInternetPresent = cd.isConnectingToInternet();

		if (isInternetPresent) {
			new DownloadJSON().execute();
		}

		else {
			fr = new NoInternetFragment();
			FragmentManager fm = getFragmentManager();
			FragmentTransaction fragmentTransaction = fm.beginTransaction();
			fragmentTransaction.replace(R.id.fragment_place, fr);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
		}

		linearLayoutForRangebar = (ViewGroup) v.findViewById(R.id.mylayout);
		RangeSeekBar<Integer> seekBar = new RangeSeekBar<Integer>(0, 200,
				context);
		seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
			@Override
			public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar,
					Integer minValue, Integer maxValue) {
				// handle changed range values
				Log.i("TAG", "User selected new range values: MIN=" + minValue
						+ ", MAX=" + maxValue);

				referenceurl = referenceurl + "&lower=" + minValue + "&higher="
						+ maxValue;
				new DownloadJSON().execute();

			}
		});

		linearLayoutForRangebar.addView(seekBar);

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				PrefUtil.setGridcount(getActivity(), position);
				HashMap<String, String> map = arraylist.get(position);
				if ((map.get(AppConstants.HOME_SUB_OUT_OF_STOCK_FLAG))
						.equals("0")) {

				} else {

					positionOfGrid = position;
					Button addToCartButton = (Button) arg1
							.findViewById(R.id.buttoncart);
					Button detailsButton = (Button) arg1
							.findViewById(R.id.buttondetails);
					TableRow tableRow = (TableRow) arg1
							.findViewById(R.id.tableRowonclickview);
					if (tempFlag == false) {
						oldviView = arg1;
						addToCartButton.setVisibility(View.VISIBLE);
						detailsButton.setVisibility(View.VISIBLE);
						tempFlag = true;
						tableRow.setVisibility(View.VISIBLE);
						tableRow.setBackgroundResource(R.drawable.transbg_withclose);

					} else {

						Button oldcart = (Button) oldviView
								.findViewById(R.id.buttoncart);
						Button olddetails = (Button) oldviView
								.findViewById(R.id.buttondetails);
						TableRow oldTableRow = (TableRow) oldviView
								.findViewById(R.id.tableRowonclickview);

						oldcart.setVisibility(View.INVISIBLE);
						olddetails.setVisibility(View.INVISIBLE);
						oldTableRow
								.setBackgroundResource(android.R.color.transparent);

						addToCartButton.setVisibility(View.VISIBLE);
						detailsButton.setVisibility(View.VISIBLE);
						tableRow.setVisibility(View.VISIBLE);
						tableRow.setBackgroundResource(R.drawable.transbg_withclose);
						tempFlag = true;
						oldviView = arg1;
					}

					tableRow.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							adapter.notifyDataSetChanged();
						}
					});

					addToCartButton
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									String id = null;

									adapter.notifyDataSetChanged();

									if (sharedPreferences.contains(customerid)) {
										id = sharedPreferences.getString(
												customerid, "");
									}

									if (id == null) {
										if (getLanguage().equals("1"))
											Toast.makeText(
													getActivity(),
													"You must login to perform this action",
													Toast.LENGTH_SHORT).show();
										else
											Toast.makeText(getActivity(),
													"لك يجب تسجيل الدخول",
													Toast.LENGTH_SHORT).show();
										singleItemData = arraylist
												.get(positionOfGrid);
										String product_id = singleItemData
												.get("product_id");
										
										fr = new LoginFragment();
										Bundle bundle = new Bundle();
										bundle.putString("Page", "catPro");
										bundle.putString("Bannertitle", bannerString);
										bundle.putString("urls",referenceurl);
										bundle.putString("ID", product_id);
										bundle.putString("firstlevel", backtoPrevurl);
										bundle.putString("firstleveltitle", backtitle);
										bundle.putString("category_id", category_id);
										bundle.putString("itemAction",AppConstants.CART_IT);
										bundle.putBoolean(AppConstants.BACKSELECTOR, backSelector);
										fr.setArguments(bundle);
										FragmentManager fm = ((Activity) context)
												.getFragmentManager();
										FragmentTransaction fragmentTransaction = fm
												.beginTransaction();
										fragmentTransaction.replace(
												R.id.fragment_place, fr);
										fragmentTransaction.commit();
									}

									else {
										singleItemData = arraylist
												.get(positionOfGrid);
										String product_id = singleItemData
												.get(AppConstants.SUB_CATEGORY1_ID);

										//Fabric Values..............................
										fabricProdId = product_id;
										fabricProdName =singleItemData.get(AppConstants.SUB_CATEGORY1_NAME);
										fabricProdPrice = singleItemData.get(AppConstants.SUB_CATEGORY1_PRICE);

										addToCartURL = addToCartURL
												+ product_id + "&id=" + id
												+ "&quantity=1&language=1";
										new RetrieveFeedTask().execute();
									}
									adapter.notifyDataSetChanged();
								}
							});

					detailsButton
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {

									singleItemData = arraylist
											.get(positionOfGrid);

									String product_id = singleItemData
											.get(AppConstants.SUB_CATEGORY1_ID);
									String product_name = singleItemData
											.get(AppConstants.SUB_CATEGORY1_NAME);
									String productprice;
									if (singleItemData
											.get(AppConstants.SUB_CATEGORY1_SPECIAL)
											.toString().equals("false")) {
										productprice = singleItemData
												.get(AppConstants.SUB_CATEGORY1_PRICE);
									} else {
										productprice = singleItemData
												.get(AppConstants.SUB_CATEGORY1_SPECIAL);
									}
									String quantity = singleItemData
											.get(AppConstants.HOME_SUB_OUT_OF_STOCK_FLAG);
									fr = new ProductDetailsFragment();
									Bundle bundle = new Bundle();
									bundle.putString("productid", product_id);
									bundle.putString("productname",
											product_name);
									bundle.putString("Price", productprice);
									bundle.putString(
											AppConstants.HOME_SUB_OUT_OF_STOCK_FLAG,
											quantity);
									bundle.putString("title", bannerString);
									Log.e("Rfrnce URL while dtails",
											referenceurl);
									bundle.putString("referenceurl",
											referenceurl);
									bundle.putString("firstlevel",
											backtoPrevurl);
									bundle.putString("firstleveltitle",
											backtitle);
									bundle.putString("category_id", category_id);
									Log.e(TAG, "BackSelector Before Bundle "
											+ backSelector);
									bundle.putBoolean(
											AppConstants.BACKSELECTOR,
											backSelector);
									fr.setArguments(bundle);
									FragmentManager fm = ((Activity) context)
											.getFragmentManager();
									FragmentTransaction fragmentTransaction = fm
											.beginTransaction();
									fragmentTransaction.replace(
											R.id.fragment_place, fr);
									fragmentTransaction.addToBackStack(null);
									fragmentTransaction.commit();
								}
							});
				}
			}
		});

		switch (getLanguage()) {
		case "en":

			break;

		default:
			setupUI(v, false);
			break;
		}
		setupUI(v, true);
		return v;
	}

	public void onAttach(Activity activity) {
		/*
		 * Hiding the top layout for the category.
		 */

		listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) activity;
		listenerHideView.promoItemHideTopbar();
		sample = activity;
		super.onAttach(activity);

	}

	public void backToPage() {
		if (backSelector) {
			fr = new FragmentCategories();
			FragmentManager fm = getFragmentManager();
			FragmentTransaction fragmentTransaction = fm.beginTransaction();
			fragmentTransaction.replace(R.id.fragment_place, fr);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
		} else {
			fr = new SubcategoryFragment();
			Bundle bundle = new Bundle();
			bundle.putString("title", backtitle);
			bundle.putString("referenceurl", backtoPrevurl);
			fr.setArguments(bundle);
			FragmentManager fm = ((Activity) context).getFragmentManager();
			FragmentTransaction fragmentTransaction = fm.beginTransaction();
			fragmentTransaction.replace(R.id.fragment_place, fr);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
		}

	}

	private class DownloadJSON extends AsyncTask<Void, Void, Void> {
		JSONObject jObj = new JSONObject();

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
			arraylist = new ArrayList<HashMap<String, String>>();
			// Retrieve JSON Objects from the given URL address
			HttpParams params1 = new BasicHttpParams();
			HttpProtocolParams.setVersion(params1, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params1, "UTF-8");
			params1.setBooleanParameter("http.protocol.expect-continue", false);
			HttpClient httpclient = new DefaultHttpClient(params1);
			Log.e(TAG,"Category Products URL : "+referenceurl);
			HttpPost httppost = new HttpPost(referenceurl);

			try {
				HttpResponse http_response = httpclient.execute(httppost);

				HttpEntity entity = http_response.getEntity();
				String jsonText = EntityUtils.toString(entity, HTTP.UTF_8);
				Log.i("Response", jsonText);
				jsonobject = new JSONObject(jsonText);

				jObj = jsonobject;

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void args) {

			try {
				if (jsonobject != null) {
					// Putting Heading to Fragment
					if (jObj.has("Title")) {
						String tit;
						try {
							tit = jObj.getString("Title");
							Log.e("Title", tit);
							bannerString = tit;
							backTextView.setText(tit);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					// Puting Fragment Data to the Fragment
					if (jsonobject.getBoolean("success")) {
						jsonarray = jsonobject
								.getJSONArray(AppConstants.SUB_CATEGORY1_JSON_OBJECT);

						for (int i = 0; i < jsonarray.length(); i++) {
							HashMap<String, String> map = new HashMap<String, String>();
							jsonobject = jsonarray.getJSONObject(i);

							// Retrive JSON Objects.....
							map.put(AppConstants.SUB_CATEGORY1_ID, jsonobject
									.getString(AppConstants.SUB_CATEGORY1_ID1));
							map.put(AppConstants.SUB_CATEGORY1_NAME, jsonobject
									.getString(AppConstants.SUB_CATEGORY1_NAME));
							map.put(AppConstants.SUB_CATEGORY1_HREF, jsonobject
									.getString(AppConstants.SUB_CATEGORY1_HREF));
							map.put(AppConstants.SUB_CATEGORY1_IMAGE,
									jsonobject
											.getString(AppConstants.SUB_CATEGORY_IMAGE));
							map.put(AppConstants.SUB_CATEGORY1_PRICE,
									jsonobject
											.getString(AppConstants.SUB_CATEGORY1_PRICE));
							map.put(AppConstants.SUB_CATEGORY1_SPECIAL,
									jsonobject
											.getString(AppConstants.SUB_CATEGORY1_SPECIAL));
							map.put(AppConstants.HOME_SUB_OUT_OF_STOCK_FLAG,
									jsonobject
											.getString(AppConstants.HOME_SUB_OUT_OF_STOCK_FLAG));
							// Set the JSON Objects into the array
							arraylist.add(map);
						}
						if (arraylist.size() > 0) {
							try {
								gridView.setVisibility(View.VISIBLE);
								errorText.setVisibility(View.GONE);
								// Pass the results into ListViewAdapter.java
								adapter = new SubCategorySingleItemAdapter(
										context, arraylist);
								// Set the adapter to the ListView
								gridView.setAdapter(adapter);
								try {
									gridView.setSelection(grid_Position);
								} catch (Exception e) {
									gridView.setSelection(0);
								}
								referenceurl = dummyUrl;  
							} catch (Exception e) {
							}
						} else {
							gridView.setVisibility(View.GONE);
							errorText.setVisibility(View.VISIBLE);
						}

						Log.e("Array List Prod det..", arraylist.toString());
					} else {
						Log.e(TAG, "Response gives Success:False");
						gridView.setVisibility(View.GONE);
						errorText.setVisibility(View.VISIBLE);
					}
				} else {
					Toast.makeText(getActivity(), R.string.no_response,
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			mProgressDialog.dismiss();
		}
	}

	@Override
	public void onPause() {

		super.onPause();
		if (mProgressDialog != null)
			mProgressDialog.dismiss();

	}

	@Override
	public void onDestroy() {

		ImageLoader imageLoader = new ImageLoader(getActivity());
		imageLoader.clearCache();
		super.onDestroy();
	}

	@SuppressWarnings("deprecation")
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

		HeightTopBar = linearLayout.getHeight();
		// Displaying the popup at the specified location, + offsets.

		popup.showAtLocation(layout, Gravity.RIGHT | Gravity.TOP, p.x
				+ OFFSET_X - 15, p.y + OFFSET_Y + HeightTopBar - 25);

		// storing string resources into Array
		String[] popupitem = getResources().getStringArray(R.array.popuparray);
		/*
		 * ListView listView =
		 * (ListView)layout.findViewById(R.id.listViewpopup);
		 */

		// Button sortstandardorder = (Button)
		// layout.findViewById(R.id.buttonstandardorder);
		Button sortnewest = (Button) layout
				.findViewById(R.id.button2sortbynewest);
		Button sortpopularity = (Button) layout
				.findViewById(R.id.button3sortbypopularity);
		// Button sortrandom = (Button) layout.findViewById(R.id.button4random);

		/*
		 * sortstandardorder.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) {
		 * 
		 * new DownloadJSON().execute();
		 * 
		 * popup.dismiss();
		 * 
		 * } });
		 */

		sortnewest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				System.out.println("THE REFERENCE URL IS" + referenceurl);
				referenceurl = referenceurl + "&sort=newest&lower=1&higher=200";
				new DownloadJSON().execute();
				popup.dismiss();

			}
		});

		sortpopularity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("THE REFERENCE URL IS" + referenceurl);
				referenceurl = referenceurl
						+ "&sort=popular&lower=1&higher=200";
				new DownloadJSON().execute();
				popup.dismiss();
			}
		});

		/*
		 * sortrandom.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * System.out.println("THE REFERENCE URL IS" + referenceurl);
		 * referenceurl = referenceurl + "&sort=random&lower=1&higher=200"; new
		 * DownloadJSON().execute(); popup.dismiss(); } });
		 */

		ArrayList<String> ppopupItemList = new ArrayList<String>();
		ppopupItemList.addAll(Arrays.asList(popupitem));

		ListAdapter listAdapter = new ArrayAdapter<String>(getActivity(),
				R.layout.layout_popup_singleitem, ppopupItemList);

	}

	private void showPopupar(Context context, Point p) {

		final Context context2 = context;
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

		// Displaying the popup at the specified location, + offsets.

		popup.showAtLocation(layout, Gravity.LEFT | Gravity.TOP, p.x + OFFSET_X
				- 15, p.y + OFFSET_Y + HeightTopBar - 68);

		// storing string resources into Array
		String[] popupitem = getResources().getStringArray(R.array.popuparray);
		/*
		 * ListView listView =
		 * (ListView)layout.findViewById(R.id.listViewpopup);
		 */

		// Button sortstandardorder = (Button)
		// layout.findViewById(R.id.buttonstandardorder_ar);
		Button sortnewest = (Button) layout
				.findViewById(R.id.button2sortbynewest_ar);
		Button sortpopularity = (Button) layout
				.findViewById(R.id.button3sortbypopularity_ar);
		// Button sortrandom = (Button)
		// layout.findViewById(R.id.button4random_ar);

		/*
		 * sortstandardorder.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) {
		 * 
		 * new DownloadJSON().execute();
		 * 
		 * popup.dismiss();
		 * 
		 * } });
		 */
		sortnewest.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				referenceurl = referenceurl + "&sort=newest&lower=1&higher=200";
				new DownloadJSON().execute();
				popup.dismiss();
			}
		});

		sortpopularity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				referenceurl = referenceurl
						+ "&sort=popular&lower=1&higher=200";
				new DownloadJSON().execute();
				popup.dismiss();
			}
		});

		ArrayList<String> ppopupItemList = new ArrayList<String>();
		ppopupItemList.addAll(Arrays.asList(popupitem));

		ListAdapter listAdapter = new ArrayAdapter<String>(getActivity(),
				R.layout.layout_popup_singleitem, ppopupItemList);
	}

	public String getLanguage() {
		SharedPreferences sp = getActivity().getSharedPreferences("lan",
				Context.MODE_PRIVATE);
		if (sp.getString("lan", "en").equals("en"))
			return "1";
		else
			return "2";
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
			Log.e(TAG, "Add to Cart : " + addToCartURL);
			jsonobject = JSONfunctions.getJSONfromURL(addToCartURL);

			return null;
		}

		@Override
		protected void onPostExecute(Void args) {
			addToCartURL = "http://dairam.com/index.php?route=api/cart/get&cart=";
			try {
				if (jsonobject != null) {
					if (jsonobject.getBoolean("success")) {
						Log.e(TAG, "Addto Cart Resp: " + jsonobject.toString());
						jsonobject = jsonobject.getJSONObject("customer");

						id = jsonobject.getString(AppConstants.CART_ID);
						cart_Count = jsonobject
								.getString(AppConstants.CART_COUNT);
						if (id == null) {
							// Toast.makeText(getActivity(),
							// "Error in adding the item",Toast.LENGTH_SHORT).show();
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

							Answers.getInstance().logAddToCart(new AddToCartEvent()
									.putItemPrice(BigDecimal.valueOf(Double.valueOf(fabricProdPrice.replaceAll("\\D+",""))))
									.putCurrency(Currency.getInstance("KWD"))
									.putItemName(fabricProdName)
									.putItemType("")
									.putItemId(fabricProdId));
						}
					} else {
						Toast.makeText(
								getActivity(),
								getResources().getString(
										R.string.error_in_additem),
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
			mProgressDialog.dismiss();
		}
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
		backTextView.setTypeface(myTypeface);
		errorText.setTypeface(myTypeface);

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
						backToPage();
						PrefUtil.setGridcount(getActivity(), 0);
						return true;
					}
				}
				return false;
			}
		});
		super.onActivityCreated(savedInstanceState);
	}
}
