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
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.interfaces.AfterLogin;
import com.dairam.android.fragments.activity.interfaces.listenerHideView;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class BrandListingSingleFragment extends Fragment {
	AfterLogin afterLogin;

	String TAG = "BrandListingSingleFragment";

	Button backButton;
	TextView headerTextView, errorText;
	Button sortButton;

	Point p;
	int HeightTopBar;

	String bannerTitle, referenceURL;
	ViewGroup linearLayoutForRangebar;
	Fragment fr;

	JSONObject jsonobject;
	JSONArray jsonarray;
	GridView gridView;
	BrandListingSingleITemAdapter adapter;
	ProgressDialog mProgressDialog;

	ArrayList<HashMap<String, String>> arraylist;
	ArrayList<HashMap<String, String>> arraylistcart;

	Activity sample;

	Context context;

	String dummyUrl;
	LinearLayout linearLayout;

	int positionOfGrid;
	Boolean tempFlag = false;
	View oldviView;
	HashMap<String, String> singleItemData = new HashMap<String, String>();

	/*
	 * Range bar sample
	 */
	int leftSeekbar, rightSeekbar;
	// Corresponds to Color.LTGRAY
	private static final int DEFAULT_BAR_COLOR = 0xffcccccc;

	// Corresponds to android.R.color.holo_blue_light.
	private static final int DEFAULT_CONNECTING_LINE_COLOR = 0xff33b5e5;
	private static final int HOLO_BLUE = 0xff33b5e5;

	// Sets the initial values such that the image will be drawn
	private static final int DEFAULT_THUMB_COLOR_NORMAL = -1;
	private static final int DEFAULT_THUMB_COLOR_PRESSED = -1;

	// Sets variables to save the colors of each attribute
	private int mBarColor = DEFAULT_BAR_COLOR;
	private int mConnectingLineColor = DEFAULT_CONNECTING_LINE_COLOR;
	private int mThumbColorNormal = DEFAULT_THUMB_COLOR_NORMAL;
	private int mThumbColorPressed = DEFAULT_THUMB_COLOR_PRESSED;

	LinearLayout linlay;

	String dummyURL;

	String addToCartURL = "http://dairam.com/index.php?route=api/cart/get&cart=";
	public static final String MyPREFERENCES = "MyPrefs";
	public static final String customerid = "id";
	public static final String firstname = "firstname";
	public static final String lastname = "lastname";
	public static final String email_string = "email";
	public static final String cart_string = "cart";
	public static final String wishlist_String = "wishlist";

	SharedPreferences sharedPreferences;

	String referenceUrl;
	int grid_Position;

	// Initializes the RangeBar in the application private RangeBar range bar;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_branddetails, container,
				false);

		grid_Position = PrefUtil.getGridCount(getActivity());

		backButton = (Button) v.findViewById(R.id.buttonbackhome);
		headerTextView = (TextView) v.findViewById(R.id.textviewheader);
		sortButton = (Button) v.findViewById(R.id.buttonsort);
		gridView = (GridView) v.findViewById(R.id.gridViewbrandpage);
		errorText = (TextView) v.findViewById(R.id.errortextBrand);
		linearLayout = (LinearLayout) v
				.findViewById(R.id.linearlayoutheaderbrand);

		gridView.setVisibility(View.VISIBLE);
		errorText.setVisibility(View.GONE);
		sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES,
				Context.MODE_PRIVATE);

		linlay = (LinearLayout) v.findViewById(R.id.linlaybrandlist);
		linlay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				System.out.println("Do nothing");
			}
		});

		Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
				R.drawable.dairam_header2);

		HeightTopBar = largeIcon.getHeight();
		HeightTopBar = linearLayout.getHeight();
		linearLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		context = getActivity();
		/*
		 * Getting value from the Bundle
		 */
		try {

			System.out.println("Entering into reading values");
			bannerTitle = getArguments().getString("title");
			referenceURL = getArguments().getString("referenceurl");

			SharedPreferences sp1 = getActivity().getSharedPreferences("tag",
					Context.MODE_PRIVATE);
			String p = "fragmentSingleBrandList";
			Editor editor = sp1.edit();
			editor.putString("act", p);
			editor.putString("bannertitle", bannerTitle);
			editor.putString("referenceurl", referenceURL);
			editor.commit();

			dummyUrl = referenceURL;
			dummyURL = referenceURL;
			System.out.println("......................" + bannerTitle
					+ referenceURL + ".............");
		} catch (Exception e) {

			System.out.println("Exception caught" + e);

		}

		SharedPreferences sp = getActivity().getSharedPreferences("lan",
				Context.MODE_PRIVATE);
		final String lan = sp.getString("lan", "en");

		switch (lan) {
		case "en":
			referenceUrl = referenceURL + "1";
			break;

		default:
			referenceUrl = referenceURL + "2";
			break;
		}

		headerTextView.setText(bannerTitle);

		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.e("BackButton", "Back Button in BrandListingSingleFragment");
				LoadHomePage();
			}
		});

		headerTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.e("BackButton",
						"Back Button in BrandListingSingleFragment  1");
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

		new DownloadJSON().execute();

		sortButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

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

		gridView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				PrefUtil.setGridcount(getActivity(), position);
				HashMap<String, String> map = arraylist.get(position);

				if ((map.get(AppConstants.HOME_SUB_OUT_OF_STOCK_FLAG))
						.equals("0")) {

				} else {
					positionOfGrid = position;

					System.out.println("Entered into and item clicked");

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
						System.out.println("Entered into onClick listener");

						System.out.println("The Visibility state of the button"
								+ addToCartButton.getVisibility());

						addToCartButton.setVisibility(View.VISIBLE);
						detailsButton.setVisibility(View.VISIBLE);
						tableRow.setVisibility(View.VISIBLE);
						tableRow.setBackgroundResource(R.drawable.transbg_withclose);
						tempFlag = true;
						oldviView = arg1;
					}

					tableRow.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							adapter.notifyDataSetChanged();
						}
					});

					addToCartButton
							.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View arg0) {

									String id = null;
									adapter.notifyDataSetChanged();

									if (sharedPreferences.contains(customerid)) {
										id = sharedPreferences.getString(
												customerid, "");
									}

									if (id == null) {
										singleItemData = arraylist
												.get(positionOfGrid);
										String product_id = singleItemData
												.get(AppConstants.SUB_CATEGORY1_ID1);
										fr = new LoginFragment();
										Bundle bundle = new Bundle();
										bundle.putString("Page", "brandlist");
										bundle.putString("Bannertitle",
												bannerTitle);
										bundle.putString("urls", dummyURL);
										bundle.putString("ID", product_id);

										fr.setArguments(bundle);
										FragmentManager fm = ((Activity) context)
												.getFragmentManager();
										FragmentTransaction fragmentTransaction = fm
												.beginTransaction();
										fragmentTransaction.replace(
												R.id.fragment_place, fr);
										fragmentTransaction
												.addToBackStack(null);
										fragmentTransaction.commit();
									}

									else {
										singleItemData = arraylist
												.get(positionOfGrid);
										String product_id = singleItemData
												.get("id");
										System.out.println("Login Done!!!"
												+ product_id);
										addToCartURL = addToCartURL
												+ product_id + "&id=" + id
												+ "&quantity=1&language=1";

										new RetrieveFeedTask().execute();
									}
									System.out
											.println("Add to cart button clicked");
									adapter.notifyDataSetChanged();
								}
							});

					detailsButton
							.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View arg0) {
									singleItemData = arraylist
											.get(positionOfGrid);
									String product_id = singleItemData
											.get(AppConstants.BRAND_SINGLE_PRODUCT_ID);
									String product_name = singleItemData
											.get(AppConstants.SUB_CATEGORY1_NAME);
									String productprice = singleItemData
											.get(AppConstants.SUB_CATEGORY1_PRICE);
									String quantity = singleItemData
											.get(AppConstants.HOME_SUB_OUT_OF_STOCK_FLAG);
									fr = new BrandProductDetailsFragment();

									/*
									 * Bundle bundle=new Bundle();
									 * bundle.putString("productid",
									 * product_id);
									 * bundle.putString("productname",
									 * product_name); bundle.putString("Price",
									 * productprice);
									 * bundle.putString("Bannertitle",
									 * bannerTitle);
									 * bundle.putString("referenceurl",
									 * dummyURL); System.out.println(
									 * "*********************produt*********************"
									 * +product_id);
									 */

									Bundle bundle = new Bundle();
									bundle.putString("productid", product_id);
									System.out
											.println("$$$$$$$$$$$$$$$$PRODUCT_ID$$$$$$$$$$$$$  "
													+ product_id);
									bundle.putString("productname",
											product_name);
									bundle.putString("Price", productprice);
									bundle.putString(
											AppConstants.HOME_SUB_OUT_OF_STOCK_FLAG,
											quantity);
									bundle.putString("BackToPreviousUrl",
											dummyURL);
									bundle.putString("title", bannerTitle);

									SharedPreferences sp1 = getActivity()
											.getSharedPreferences("tag",
													Context.MODE_PRIVATE);
									String p = "fragmentpromodetails";
									Editor editor = sp1.edit();
									editor.putString("act", p);
									editor.putString("productid", product_id);
									editor.putString("productname",
											product_name);
									editor.putString("Price", productprice);
									editor.putString("BackToPreviousUrl",
											dummyURL);
									editor.putString("title", bannerTitle);
									editor.putString(
											AppConstants.HOME_SUB_OUT_OF_STOCK_FLAG,
											quantity);
									editor.commit();

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

		/* ..............Range bar starts........................ */
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

				String modifiedURL = referenceURL;
				SharedPreferences sp = getActivity().getSharedPreferences(
						"lan", Context.MODE_PRIVATE);
				final String lan = sp.getString("lan", "en");

				switch (lan) {
				case "en":
					modifiedURL = modifiedURL + "1";
					break;

				default:
					modifiedURL = modifiedURL + "2";
					break;
				}

				referenceUrl = modifiedURL + "&lower=" + minValue + "&higher="
						+ maxValue;

				System.out.println("The reference url............."
						+ referenceUrl);
				new DownloadJSON().execute();
			}
		});

		linearLayoutForRangebar.addView(seekBar);
		setupUI(v, true);
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
	public void onActivityCreated(Bundle savedInstanceState) {
		getView().setFocusableInTouchMode(true);
		getView().requestFocus();

		getView().setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						LoadHomePage();
						PrefUtil.setGridcount(getActivity(), 0);
						return true;
					}
				}
				return false;
			}
		});
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		sample = activity;
		listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) activity;
		listenerHideView.promoItemHideTopbar();
		super.onAttach(activity);

	}

	public void LoadHomePage() {
		fr = new FragmentBrnad();
		FragmentManager fm = getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_place, fr);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}

	// DownloadJSON AsyncTask
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
			// Create an array
			arraylist = new ArrayList<HashMap<String, String>>();
			// Retrieve JSON Objects from the given URL address
			HttpParams params1 = new BasicHttpParams();
			HttpProtocolParams.setVersion(params1, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params1, "UTF-8");
			params1.setBooleanParameter("http.protocol.expect-continue", false);
			HttpClient httpclient = new DefaultHttpClient(params1);

			HttpPost httppost = new HttpPost(referenceUrl);

			System.out.println("the Url is........." + referenceUrl);
			try {
				HttpResponse http_response = httpclient.execute(httppost);

				HttpEntity entity = http_response.getEntity();
				String jsonText = EntityUtils.toString(entity, HTTP.UTF_8);
				Log.i("Response", jsonText);
				jsonobject = new JSONObject(jsonText);

			} catch (Exception e) {
				e.printStackTrace();
			}

			System.out.println("The REFERENCE URL FOR BRANDLISTING"
					+ referenceURL);

			return null;
		}

		@Override
		protected void onPostExecute(Void args) {

			try {
				if (jsonobject != null) {
					if (jsonobject.getBoolean("success")) {
						// Locate the array name in JSON
						headerTextView.setText(jsonobject
								.getString("manufacturer_name"));
						jsonarray = jsonobject
								.getJSONArray(AppConstants.BRAND_SINGLE_JSONOBJECT);

						for (int i = 0; i < jsonarray.length(); i++) {
							HashMap<String, String> map = new HashMap<String, String>();
							jsonobject = jsonarray.getJSONObject(i);
							// Retrive JSON Objects
							map.put(AppConstants.BRAND_SINGLE_PRODUCT_ID,
									jsonobject
											.getString(AppConstants.BRAND_SINGLE_PRODUCT_ID));
							map.put(AppConstants.BRAND_SINGLE_NAME, jsonobject
									.getString(AppConstants.BRAND_SINGLE_NAME));
							map.put(AppConstants.BRAND_SINGLE_PRICE, jsonobject
									.getString(AppConstants.BRAND_SINGLE_PRICE));
							map.put(AppConstants.BRAND_SINGLE_SPECIAL,
									jsonobject
											.getString(AppConstants.BRAND_SINGLE_SPECIAL));
							map.put(AppConstants.BRAND_SINGLE_LINK, jsonobject
									.getString(AppConstants.BRAND_SINGLE_LINK));
							map.put(AppConstants.BRAND_SINGLE_IMAGE, jsonobject
									.getString(AppConstants.BRAND_SINGLE_IMAGE));
							map.put(AppConstants.HOME_SUB_OUT_OF_STOCK_FLAG,
									jsonobject
											.getString(AppConstants.HOME_SUB_OUT_OF_STOCK_FLAG));
							// Set the JSON Objects into the array
							arraylist.add(map);
							System.out.println("Arraylist" + arraylist);

							if (arraylist.size() > 0) {
								adapter = new BrandListingSingleITemAdapter(
										context, arraylist);
								gridView.setAdapter(adapter);
								try {
									gridView.setSelection(grid_Position);
								} catch (Exception e) {
									gridView.setSelection(0);
								}
								adapter.notifyDataSetChanged();

								referenceURL = dummyUrl;

								gridView.setVisibility(View.VISIBLE);
								errorText.setVisibility(View.GONE);
								// Close the progressdialog
							} else {
								gridView.setVisibility(View.GONE);
								errorText.setVisibility(View.VISIBLE);
							}
						}
					} else {
						gridView.setVisibility(View.GONE);
						errorText.setVisibility(View.VISIBLE);
					}
				} else {
					Toast.makeText(getActivity(), R.string.no_response,
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				Log.e(TAG, "Line : 649 " + e.getMessage());
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

	public void onWindowFocusChanged(boolean hasFocus) {
		Toast.makeText(getActivity(), "Entered into windoestate",
				Toast.LENGTH_SHORT).show();

		int[] location = new int[2];

		// Get the x, y location and store it in the location[] array
		// location[0] = x, location[1] = y.
		sortButton.getLocationOnScreen(location);

		// Initialize the Point with x, and y positions
		p = new Point();
		p.x = location[0];
		p.y = location[1];
	}

	private void showPopupar(Context context1, Point p) {

		final Context context2 = context1;
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
		 * fr = new BrandListingSingleFragment(); Bundle bundle=new Bundle();
		 * bundle.putString("title",bannerTitle );
		 * bundle.putString("referenceurl", referenceURL);
		 * fr.setArguments(bundle); FragmentManager fm = ((Activity)
		 * context).getFragmentManager(); FragmentTransaction
		 * fragmentTransaction = fm.beginTransaction();
		 * fragmentTransaction.replace(R.id.fragment_place, fr);
		 * fragmentTransaction.addToBackStack(null);
		 * fragmentTransaction.commit(); popup.dismiss();
		 * 
		 * 
		 * 
		 * } });
		 */

		sortnewest.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				referenceURL = referenceURL + "&sort=newest&lower=1&higher=200";

				fr = new BrandListingSingleFragment();
				Bundle bundle = new Bundle();
				bundle.putString("title", bannerTitle);
				// bundle.putString("referenceurl", referenceURL);
				bundle.putString("referenceurl", dummyUrl);
				fr.setArguments(bundle);
				FragmentManager fm = ((Activity) context).getFragmentManager();
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
				referenceURL = referenceURL
						+ "&sort=popular&lower=1&higher=200";

				fr = new BrandListingSingleFragment();
				Bundle bundle = new Bundle();
				bundle.putString("title", bannerTitle);
				// bundle.putString("referenceurl", referenceURL);
				bundle.putString("referenceurl", dummyUrl);
				fr.setArguments(bundle);
				FragmentManager fm = ((Activity) context).getFragmentManager();
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
		 * @Override public void onClick(View v) { referenceURL = referenceURL
		 * +"&sort=random&lower=1&higher=200";
		 * 
		 * fr = new BrandListingSingleFragment(); Bundle bundle=new Bundle();
		 * bundle.putString("title",bannerTitle ); //
		 * bundle.putString("referenceurl", referenceURL);
		 * bundle.putString("referenceurl", dummyUrl); fr.setArguments(bundle);
		 * FragmentManager fm = ((Activity) context).getFragmentManager();
		 * FragmentTransaction fragmentTransaction = fm.beginTransaction();
		 * fragmentTransaction.replace(R.id.fragment_place, fr);
		 * fragmentTransaction.addToBackStack(null);
		 * fragmentTransaction.commit(); popup.dismiss(); } });
		 */

		ArrayList<String> ppopupItemList = new ArrayList<String>();
		ppopupItemList.addAll(Arrays.asList(popupitem));

		ListAdapter listAdapter = new ArrayAdapter<String>(getActivity(),
				R.layout.layout_popup_singleitem, ppopupItemList);

	}

	/* pop up sample */

	// The method that displays the popup.

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
		// Displaying the popup at the specified location, + offsets.

		popup.showAtLocation(layout, Gravity.RIGHT | Gravity.TOP, p.x
				+ OFFSET_X - 15, p.y + OFFSET_Y + HeightTopBar - 25);

		// storing string resources into Array
		String[] popupitem = getResources().getStringArray(R.array.popuparray);
		/*
		 * ListView listView =
		 * (ListView)layout.findViewById(R.id.listViewpopup);
		 */

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
		 * @Override public void onClick(View arg0) {
		 * 
		 * popup.dismiss();
		 * 
		 * new DownloadJSON().execute();
		 * 
		 * } });
		 */

		sortnewest.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				popup.dismiss();
				String modifiedURL = referenceURL;

				SharedPreferences sp = getActivity().getSharedPreferences(
						"lan", Context.MODE_PRIVATE);
				final String lan = sp.getString("lan", "en");

				switch (lan) {
				case "en":
					modifiedURL = modifiedURL + "1";
					break;

				default:
					modifiedURL = modifiedURL + "2";
					break;
				}
				referenceUrl = modifiedURL + "&sort=newest&lower=1&higher=200";
				new DownloadJSON().execute();

			}
		});

		sortpopularity.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				popup.dismiss();
				String modifiedURL = referenceURL;

				SharedPreferences sp = getActivity().getSharedPreferences(
						"lan", Context.MODE_PRIVATE);
				final String lan = sp.getString("lan", "en");

				switch (lan) {
				case "en":
					modifiedURL = modifiedURL + "1";
					break;

				default:
					modifiedURL = modifiedURL + "2";
					break;
				}
				referenceUrl = modifiedURL + "&sort=popular&lower=1&higher=200";
				new DownloadJSON().execute();

			}
		});

		/*
		 * sortrandom.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * 
		 * popup.dismiss(); String modifiedURL = referenceURL;
		 * 
		 * SharedPreferences sp=getActivity().getSharedPreferences("lan",
		 * Context.MODE_PRIVATE); final String lan=sp.getString("lan", "en");
		 * 
		 * switch (lan) { case "en": modifiedURL = modifiedURL+"1"; break;
		 * 
		 * default: modifiedURL = modifiedURL+"2"; break; }
		 * 
		 * referenceUrl = modifiedURL+"&sort=popular&lower=1&higher=200"; new
		 * DownloadJSON().execute();
		 * 
		 * } });
		 */

		ArrayList<String> ppopupItemList = new ArrayList<String>();
		ppopupItemList.addAll(Arrays.asList(popupitem));

		ListAdapter listAdapter = new ArrayAdapter<String>(getActivity(),
				R.layout.layout_popup_singleitem, ppopupItemList);

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
			jsonobject = JSONfunctions.getJSONfromURL(addToCartURL);

			return null;
		}

		@Override
		protected void onPostExecute(Void args) {
			mProgressDialog.dismiss();
			addToCartURL = "http://dairam.com/index.php?route=api/cart/get&cart=";
			try {
				if (jsonobject != null) {
					// Locate the array name in JSON
					jsonobject = jsonobject.getJSONObject("customer");
					id = jsonobject.getString(AppConstants.CART_ID);
					cart_Count = jsonobject.getString(AppConstants.CART_COUNT);

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
						;

						Editor editor = sharedPreferences.edit();
						editor.putString(cart_string, cart_Count);
						editor.apply();
					}
				} else {
					Toast.makeText(getActivity(), R.string.no_response,
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				Log.e(TAG, e.getMessage());
				e.printStackTrace();
			}
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
		/*
		 * TextView myTextView = (TextView)view.findViewById(R.id.textView1);
		 * myTextView.setTypeface(myTypeface);
		 */
		headerTextView.setTypeface(myTypeface);
		errorText.setTypeface(myTypeface);
	}

	Typeface getRegularTypeFace() {
		return Typeface.createFromAsset(context.getAssets(),
				context.getString(R.string.regular_typeface));
	}

}