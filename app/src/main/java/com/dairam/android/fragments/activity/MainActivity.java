package com.dairam.android.fragments.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.LoginEvent;
import com.crashlytics.android.answers.SearchEvent;
import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.adapter.CustomDrawerListAdapter;
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.connectioncheck.ConnectionDetector;
import com.dairam.android.fragments.activity.fragments.AddressBookFragment;
import com.dairam.android.fragments.activity.fragments.BrandListingSingleFragment;
import com.dairam.android.fragments.activity.fragments.BrandProductDetailsFragment;
import com.dairam.android.fragments.activity.fragments.CartFragment;
import com.dairam.android.fragments.activity.fragments.CategoryProductsFragment;
import com.dairam.android.fragments.activity.fragments.CheckoutFragmentFirstStep;
import com.dairam.android.fragments.activity.fragments.CheckoutFragmentSecondStep;
import com.dairam.android.fragments.activity.fragments.CheckoutLastStage;
import com.dairam.android.fragments.activity.fragments.CustomerCareFragment;
import com.dairam.android.fragments.activity.fragments.EditInfoFragment;
import com.dairam.android.fragments.activity.fragments.FragmentBrnad;
import com.dairam.android.fragments.activity.fragments.FragmentCategories;
import com.dairam.android.fragments.activity.fragments.FragmentMyOrder;
import com.dairam.android.fragments.activity.fragments.FragmentOne;
import com.dairam.android.fragments.activity.fragments.KNETPaymentFragment;
import com.dairam.android.fragments.activity.fragments.LoginFragment;
import com.dairam.android.fragments.activity.fragments.MyAccountItemFragment;
import com.dairam.android.fragments.activity.fragments.NewsletterSignup;
import com.dairam.android.fragments.activity.fragments.NoInternetFragment;
import com.dairam.android.fragments.activity.fragments.PromoItemFragment;
import com.dairam.android.fragments.activity.fragments.PromoItemSingleProductDetailsFragment;
import com.dairam.android.fragments.activity.fragments.RegisterFragment;
import com.dairam.android.fragments.activity.fragments.SaleFragment;
import com.dairam.android.fragments.activity.fragments.SaleProductsDetails;
import com.dairam.android.fragments.activity.fragments.SalesItemDisplayFragment;
import com.dairam.android.fragments.activity.fragments.ShippingPolicy;
import com.dairam.android.fragments.activity.fragments.StyleDetails;
import com.dairam.android.fragments.activity.fragments.StylesFragment;
import com.dairam.android.fragments.activity.fragments.SubcategoryFragment;
import com.dairam.android.fragments.activity.fragments.TermsAndconditionsFragment;
import com.dairam.android.fragments.activity.fragments.WishListFragment;
import com.dairam.android.fragments.activity.interfaces.listenerHideView;
import com.dairam.android.fragments.activity.parserfiles.JSONfunctions;
import com.dairam.android.fragments.activity.popupfile.TransparentPanel;
import com.dairam.android.fragments.activity.popupfiles.ActionItem;
import com.dairam.android.fragments.activity.popupfiles.NewQAAdapter;
import com.dairam.android.fragments.activity.utillities.PrefUtil;
import com.dairam.android.fragments.activity.utillities.Util;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.pushwoosh.BasePushMessageReceiver;
import com.pushwoosh.BaseRegistrationReceiver;
import com.pushwoosh.PushManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


public class MainActivity extends Activity implements listenerHideView,
        ConnectionCallbacks, OnConnectionFailedListener {
    public static MainActivity Activity;

    JSONObject jsonobject;
    JSONArray jsonarray;
    ListView listview;
    ProgressDialog mProgressDialog;
    ArrayList<HashMap<String, String>> arraylist;
    ArrayList<HashMap<String, String>> arraylistcountry;
    String addToCartURL = "http://dairam.com/index.php?route=api/cart/get&cart=";
    String addToWishlistURl = "http://dairam.com/index.php?route=api/wishlist/get&wishlist=";

    RelativeLayout ruiellay;
    Boolean isInternetPresent;
    private GoogleApiClient mGoogleApiClient;
    public ConnectionResult mConnectionResult;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private static final int RC_SIGN_IN = 0;
    private static final int ID_FB = 1;
    private static final int ID_TWITER = 2;
    private static final int ID_INSA = 3;
    private static final int ID_PINT = 4;

    private static final int ID_AR = 5;
    private static final int ID_EN = 6;

    // gplus login arun done vars................
    private PendingIntent mSignInIntent;
    private int mSignInProgress;
    private static final int STATE_DEFAULT = 0;
    private static final int STATE_SIGN_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;
    private static final String SAVED_PROGRESS = "sign_in_progress";
    private int mSignInError;
    private static final int DIALOG_PLAY_SERVICES_ERROR = 0;

    String googleLoginURL;

    String actionSpec, title, id, bannerTitle, url, price, quantity, name;
    String backtoPrevurl, backtitle, category_id;
    boolean backSelector;
    // gplus login arun done vars................

    String lan_;

    private String FILENAMELANGUAGE = "LanguageSelection.txt";
    Fragment fr;

    Button moreButton;
    LinearLayout topTabLinearLayout;

    // Within which the entire activity is enclosed
    /* DrawerLayout mDrawerLayout; */
    DrawerLayout mDrawerLayout;

    // ListView represents Navigation Drawer
    ListView mDrawerList;

    // ActionBarDrawerToggle indicates the presence of Navigation Drawer in the
    // action bar
    ActionBarDrawerToggle mDrawerToggle;

    // Title of the action bar
    String mTitle = "";

    AlertDialog dialog;

    /*
     * / Top header with back and sort by button and a text which is controlled
     * accordingly..
     */
    LinearLayout linearLayoutHeader, linearLayoutBottomBar;
    View dividerViewBottom;
    Button backToHomeButton, sortByButton;
    TextView topHeaderTextView;

    Button categoryButton;
    Button brandsButton;
    Button searchButton;
    Button loginButton;
    Button shopButton;
    Button stylesButton;
    Button wishListButton;
    Button cartButton;

    TextView wishListCount;
    TextView cartListCount;

    ConnectionDetector connectionDetector;
    SharedPreferences sharedpreferences;

    View diView;

    private Animation animShow, animHide;

    private Animation animup, animdown;
    Point p;

    Integer[] imageId = {
    /* R.drawable.more, */
            R.drawable.language, R.drawable.country, R.drawable.myaccount,
            R.drawable.socialmedia, R.drawable.newsletter,
            R.drawable.shippingpolicy, R.drawable.termscondtns,
            R.drawable.customercare};

    NewQAAdapter adapter;
    com.dairam.android.fragments.activity.popupfiles.QuickAction mQuickAction;
    com.dairam.android.fragments.activity.popupfiles.QuickActionLanguage mQuickAction_languageItem;
    TransparentPanel popup;
    Button hideButton;

    String language;

    EditText searchEdit;

    ActionItem arabicpopup, englishPopup;

    LinearLayout bottombarLayout;
    SharedPreferences sharedPreferences;

    Button facebookButton, twitterButton, pinterestButton, instagramButton;
    com.dairam.android.fragments.activity.dialogfragment.MyDialogFragment myDialogFragment;
    com.dairam.android.fragments.activity.dialogfragment.MyDialogFragmentSocialmedia myDialogFragmentsocial;
    com.dairam.android.fragments.activity.dialogfragment.MyDialogCountries myDialogFragmentcountry;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String customerid = "id";
    public static final String firstname = "firstname";
    public static final String lastname = "lastname";
    public static final String email_string = "email";
    public static final String cart_string = "cart";
    public static final String wishlist_String = "wishlist";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private int totalCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        // push notification

        //Register receivers for push notifications
        registerReceivers();

        //Create and start push manager
        PushManager pushManager = PushManager.getInstance(this);

        //Start push manager, this will count app open for Pushwoosh stats as well
        try {
            pushManager.onStartup(this);
        }
        catch(Exception e)
        {
            //push notifications are not available or AndroidManifest.xml is not configured properly
        }

        //Register for push!
        pushManager.registerForPushNotifications();

        checkMessage(getIntent());

        prefs = getPreferences(Context.MODE_PRIVATE);
        editor = prefs.edit();
        totalCount = prefs.getInt("counter", 0);
        totalCount++;
        editor.putInt("counter", totalCount);
        editor.commit();
        if(totalCount==14)
        {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(R.string.rating_title)
                    .setMessage(R.string.rating_msg)
                    .setCancelable(false)
                    .setPositiveButton("Rate Now", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            try
                            {
                                Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                                // To count with Play market backstack, After pressing back button,
                                // to taken back to our application, we need to add following flags to intent.
                                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                try {
                                    startActivity(goToMarket);
                                } catch (ActivityNotFoundException e) {
                                    startActivity(new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                                }

                            }
                            catch (Exception xx)
                            {xx.toString();}
                            //startActivity(new Intent(getApplicationContext(),RatingActivity.class));
                        }
                    })
                    .setNegativeButton("Remind Me Later", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            editor.putInt("counter", 10);
                            editor.commit();

                        }
                    })
                    .show();
        }


        //mGoogleApiClient = buildGoogleApiClient();

        // Configure sign-in to request the user's ID, email address, and basic profile. ID and
        // basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

// Build a GoogleApiClient with access to GoogleSignIn.API and the options above.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


		/*
		 * try { PackageInfo info = getPackageManager().getPackageInfo(
		 * "com.dairam.android.fragments", PackageManager.GET_SIGNATURES); for
		 * (Signature signature : info.signatures) { MessageDigest md =
		 * MessageDigest.getInstance("SHA"); md.update(signature.toByteArray());
		 * Log.d("KeyHash:", Base64.encodeToString(md.digest(),
		 * Base64.DEFAULT)); } } catch (NameNotFoundException e) {
		 * 
		 * } catch (NoSuchAlgorithmException e) {
		 * 
		 * }
		 */
        sharedpreferences = getApplicationContext().getSharedPreferences(
                MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences sp = MainActivity.this.getSharedPreferences("lan",
                Context.MODE_PRIVATE);
        String lan = sp.getString("lan", "en");
        setLanguage(lan);
        language = lan;
        setContentView(R.layout.activity_main);

        popup = (TransparentPanel) findViewById(R.id.popup_window);

        bottombarLayout = (LinearLayout) findViewById(R.id.LinearLayout3);

        // Start out with the popup initially hidden.....
        popup.setVisibility(View.GONE);

        searchEdit = (EditText) findViewById(R.id.searcheditText);

        animShow = AnimationUtils.loadAnimation(this, R.anim.popup_show);
        animHide = AnimationUtils.loadAnimation(this, R.anim.popup_hide);

        animup = AnimationUtils.loadAnimation(this, R.anim.anim_up);
        animdown = AnimationUtils.loadAnimation(this, R.anim.anim_down);

        hideButton = (Button) findViewById(R.id.hide_popup_button);
        // btnGo = (Button)findViewById(R.id.search_btn_go);

        wishListCount = (TextView) findViewById(R.id.textViewwishlistcount);
        cartListCount = (TextView) findViewById(R.id.textViewcartcount);

        wishListCount.setVisibility(View.INVISIBLE);
        cartListCount.setVisibility(View.INVISIBLE);

        updateCartAndWishlistCount();

        arabicpopup = new ActionItem(ID_AR, "     العربية    ", null);
        englishPopup = new ActionItem(ID_EN, "   English  ", null);
        mQuickAction_languageItem = new com.dairam.android.fragments.activity.popupfiles.QuickActionLanguage(
                getApplicationContext());
        mQuickAction_languageItem.addActionItem(arabicpopup);
        mQuickAction_languageItem.addActionItem(englishPopup);

		/* Pop up files */
        diView = (View) findViewById(R.id.divview);

        adapter = new NewQAAdapter(getApplicationContext());

        ActionItem facebookItem = null, twitterItem = null, instagramItem = null, pintrestItem = null;

        switch (lan) {

            case "en":
                facebookItem = new ActionItem(ID_FB, "facebook", getResources()
                        .getDrawable(R.drawable.fb_icon_58x58));
                twitterItem = new ActionItem(ID_TWITER, "twitter", getResources()
                        .getDrawable(R.drawable.twtr_icon_58x58));
                instagramItem = new ActionItem(ID_INSA, "instagram", getResources()
                        .getDrawable(R.drawable.instgrm_icon_58x58));
                pintrestItem = new ActionItem(ID_PINT, "Pinterest", getResources()
                        .getDrawable(R.drawable.pinterest58x58));
                break;

            default:
                facebookItem = new ActionItem(ID_FB, "الفيسبوك", getResources()
                        .getDrawable(R.drawable.fb_icon_58x58));
                twitterItem = new ActionItem(ID_TWITER, "تويتر", getResources()
                        .getDrawable(R.drawable.twtr_icon_58x58));
                instagramItem = new ActionItem(ID_INSA, "إينستاجرام",
                        getResources().getDrawable(R.drawable.instgrm_icon_58x58));
                pintrestItem = new ActionItem(ID_PINT, "بينتيريست", getResources()
                        .getDrawable(R.drawable.pinterest58x58));
                break;

        }

        mQuickAction = new com.dairam.android.fragments.activity.popupfiles.QuickAction(
                getApplicationContext());
        mQuickAction.addActionItem(facebookItem);
        mQuickAction.addActionItem(twitterItem);
        mQuickAction.addActionItem(instagramItem);
        mQuickAction.addActionItem(pintrestItem);

        // setup the action item click listener
        mQuickAction
                .setOnActionItemClickListener(new com.dairam.android.fragments.activity.popupfiles.QuickAction.OnActionItemClickListener() {
                    @Override
                    public void onItemClick(
                            com.dairam.android.fragments.activity.popupfiles.QuickAction quickAction,
                            int pos, int actionId) {
                        ActionItem actionItem = quickAction.getActionItem(pos);

                        if (actionId == ID_FB) { // Add item selected
                            fbSelected();
                            try {// Closing the drawer
                                mDrawerLayout.closeDrawer(mDrawerList);
                            } catch (Exception e) {
                                System.out
                                        .println("Exception caught here in isolated view ");
                            }
                        } else if (actionId == ID_TWITER) {
                            twitterSelected();

                            try {// Closing the drawer
                                mDrawerLayout.closeDrawer(mDrawerList);
                            } catch (Exception e) {
                                System.out
                                        .println("Exception caught here in isolated view ");
                            }
                        } else if (actionId == ID_INSA) {
                            instaGram();

                            try {// Closing the drawer
                                mDrawerLayout.closeDrawer(mDrawerList);
                            } catch (Exception e) {
                                System.out
                                        .println("Exception caught here in isolated view ");
                            }
                        } else if (actionId == ID_PINT) {
                            pinterest();

                            try {// Closing the drawer
                                mDrawerLayout.closeDrawer(mDrawerList);
                            } catch (Exception e) {
                                System.out
                                        .println("Exception caught here in isolated view ");
                            }
                        }
                    }
                });

        readFile();

        topTabLinearLayout = (LinearLayout) findViewById(R.id.LinearLayouttopmenu);

        linearLayoutHeader = (LinearLayout) findViewById(R.id.linearlayoutheader);
        linearLayoutBottomBar = (LinearLayout) findViewById(R.id.LinearLayout3);
        dividerViewBottom = (View) findViewById(R.id.dividerview);
        backToHomeButton = (Button) findViewById(R.id.buttonback2home);
        sortByButton = (Button) findViewById(R.id.button2sortby);

        topHeaderTextView = (TextView) findViewById(R.id.textView1topheader);

        categoryButton = (Button) findViewById(R.id.categorybutton);
        brandsButton = (Button) findViewById(R.id.brandsbutton);
        searchButton = (Button) findViewById(R.id.searchbutton);
        loginButton = (Button) findViewById(R.id.loginbutton);

        shopButton = (Button) findViewById(R.id.shopbutton);
        stylesButton = (Button) findViewById(R.id.stylebutton);
        wishListButton = (Button) findViewById(R.id.wishlistbutton);
        cartButton = (Button) findViewById(R.id.cartbutton);

        String id = null;
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(customerid)) {
            id = sharedPreferences.getString(customerid, "");
        }

        if (id == null) {
        } else {
            System.out.println("The language is" + lan);

            switch (lan) {
                case "en":
                    loginButton.setBackground(getResources().getDrawable(
                            R.drawable.logout_icon));
                    break;

                default:
                    loginButton.setBackground(getResources().getDrawable(
                            R.drawable.logout2_ar));
                    break;
            }
        }

        int[] location = new int[2];

        // Get the x, y location and store it in the location[] array
        // location[0] = x, location[1] = y.
        shopButton.getLocationOnScreen(location);

        // Initialize the Point with x, and y positions
        p = new Point();
        p.x = location[0];
        p.y = location[1];

        backToHomeButton.setVisibility(View.INVISIBLE);
        sortByButton.setVisibility(View.INVISIBLE);

        sortByButton.setVisibility(View.INVISIBLE);

        SharedPreferences sp1 = getApplicationContext().getSharedPreferences(
                "tag", Context.MODE_PRIVATE);
        String activityName = sp1.getString("act", "");

        if (activityName == "fragmentone") {
            fr = new FragmentOne();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (activityName == "fragmentstyle") {
            fr = new StylesFragment();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (activityName == "fragmentbrand") {
            fr = new FragmentBrnad();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (activityName == "fragmentSingleBrandList") {
            String bannerTitle = sp1.getString("bannertitle", "");
            String referenceUrl = sp1.getString("referenceurl", "");
            fr = new BrandListingSingleFragment();
            Bundle bundle = new Bundle();
            bundle.putString("title", bannerTitle);
            bundle.putString("referenceurl", referenceUrl);
            fr.setArguments(bundle);
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (activityName == "fragmentcategories") {
            fr = new FragmentCategories();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (activityName == "fragmentpromodetails") {
            String productid = sp1.getString("productid", "");
            String productname = sp1.getString("productname", "");
            String price = sp1.getString("Price", "");
            String backTOPrevurl = sp1.getString("BackToPreviousUrl", "");
            String title = sp1.getString("title", "");
            String quantity = sp1.getString(
                    AppConstants.HOME_SUB_OUT_OF_STOCK_FLAG, "");
            fr = new PromoItemSingleProductDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("productid", productid);
            bundle.putString("productname", productname);
            bundle.putString("Price", price);
            bundle.putString("BackToPreviousUrl", backTOPrevurl);
            bundle.putString("title", title);
            bundle.putString(AppConstants.HOME_SUB_OUT_OF_STOCK_FLAG, quantity);
            fr.setArguments(bundle);
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (activityName.equals("fragmentmyorder")) {
            fr = new FragmentMyOrder();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (activityName == "SaleProductsDetails") {
            String productid = sp1.getString("productid", "");
            String productname = sp1.getString("productname", "");
            String price = sp1.getString("Price", "");
            String backTOPrevurl = sp1.getString("BackToPreviousUrl", "");
            String title = sp1.getString("title", "");
            String quantity = sp1.getString(
                    AppConstants.HOME_SUB_OUT_OF_STOCK_FLAG, "");
            fr = new SaleProductsDetails();
            Bundle bundle = new Bundle();
            bundle.putString("productid", productid);
            bundle.putString("productname", productname);
            bundle.putString("Price", price);
            bundle.putString("referenceURl", backTOPrevurl);
            bundle.putString("BannerTitle", title);
            bundle.putString(AppConstants.HOME_SUB_OUT_OF_STOCK_FLAG, quantity);
            fr.setArguments(bundle);
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (activityName == "fragmentSubcategorylist") {

            String bannerTitle = sp1.getString("title", "");
            String referenceUrl = sp1.getString("referenceurl", "");
            String categoryId = sp1.getString("categoryid", "");
            fr = new SubcategoryFragment();
            Bundle bundle = new Bundle();
            bundle.putString("title", bannerTitle);
            bundle.putString("referenceurl", referenceUrl);
            bundle.putString("categoryid", categoryId);
            fr.setArguments(bundle);
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (activityName == "fragmentSubcategorygrid") {

            String bannerTitle = sp1.getString("title", "");
            String referenceUrl = sp1.getString("referenceurl", "");
            String firsr = sp1.getString("firstlevel", "");
            String firsttit = sp1.getString("firstleveltitle", "");
            String cat_id = sp1.getString("categoryid", "");
            boolean backSelector = sp1.getBoolean("backselecter", false);

            fr = new CategoryProductsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("title", bannerTitle);
            bundle.putString("referenceurl", referenceUrl);
            bundle.putString("firstlevel", firsr);
            bundle.putString("firstleveltitle", firsttit);
            bundle.putString("categoryid", cat_id);
            bundle.putBoolean("backselecter", backSelector);
            fr.setArguments(bundle);
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (activityName == "fragmentaccount") {
            fr = new MyAccountItemFragment();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (activityName == "fragmenteditaccount") {
            fr = new EditInfoFragment();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (activityName == "fragmentaddressbook") {
            fr = new AddressBookFragment();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (activityName == "fragmentnewaddressbook") {
            // fr = new addNewAddressFragment();
            fr = new AddressBookFragment();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (activityName == "fragmentbranddetails") {
            String productid = sp1.getString("productid", "");
            String productname = sp1.getString("productname", "");
            String price = sp1.getString("Price", "");
            String bannertitle = sp1.getString("Bannertitle", "");
            String referenceUrl = sp1.getString("referenceurl", "");
            String quantity = sp1.getString(
                    AppConstants.HOME_SUB_OUT_OF_STOCK_FLAG, "");

            fr = new BrandProductDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("productid", productid);
            bundle.putString("productname", productname);
            bundle.putString("Price", price);
            bundle.putString("Bannertitle", bannertitle);
            bundle.putString("referenceurl", referenceUrl);
            bundle.putString(AppConstants.HOME_SUB_OUT_OF_STOCK_FLAG, quantity);

            fr.setArguments(bundle);
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (activityName == "fragmentsales") {
            fr = new SaleFragment();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (activityName == "fragmentstyledetails") {
            String bannerTitle = sp1.getString("title", "");
            String referenceUrl = sp1.getString("referenceurl", "");
            String styleId = sp1.getString("styleID", "");
            Fragment fr = new StyleDetails();
            Bundle bundle = new Bundle();
            bundle.putString("title", bannerTitle);
            bundle.putString("referenceurl", referenceUrl);
            bundle.putString("styleID", styleId);
            fr.setArguments(bundle);
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (activityName == "fragmentregister") {
            fr = new RegisterFragment();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (activityName == "fragmentlogin") {
            fr = new LoginFragment();
            Bundle bundle = new Bundle();
            bundle.putString("title", "FromHomeScreen");
            bundle.putString("ID", "0");
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (activityName == "termsandconditions") {
            fr = new TermsAndconditionsFragment();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (activityName == "shippingpolicy") {
            fr = new ShippingPolicy();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (activityName == "customercare") {
            fr = new CustomerCareFragment();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (activityName == "newslettersignup") {
            fr = new NewsletterSignup();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (activityName == "fragmentsaleSingleitemgrid") {
            String bannerTitle = sp1.getString("title", "");
            String referenceUrl = sp1.getString("referenceurl", "");
            fr = new SalesItemDisplayFragment();
            Bundle bundle = new Bundle();
            bundle.putString("title", bannerTitle);
            bundle.putString("referenceurl", referenceUrl);
            fr.setArguments(bundle);
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (activityName == "fragmentpromoitemfragment") {
            String bannerTitle = sp1.getString("title", "");
            String referenceUrl = sp1.getString("referenceurl", "");
            fr = new PromoItemFragment();
            Bundle bundle = new Bundle();
            bundle.putString("title", bannerTitle);
            bundle.putString("referenceurl", referenceUrl);
            fr.setArguments(bundle);
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (activityName == "wishlistfragment") {
            fr = new WishListFragment();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (activityName == "cartfragment") {
            fr = new CartFragment();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (activityName == "checkoutfrag1") {
            String discount = sp1.getString("discount", "");
            Bundle bundle = new Bundle();
            bundle.putString("discount", discount);
            fr = new CheckoutFragmentFirstStep();
            fr.setArguments(bundle);
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (activityName == "checkoutfrag2") {
            String discount = sp1.getString("discount", "");
            String addressId = sp1.getString("shipping_address", "");
            Bundle bundle = new Bundle();
            bundle.putString("discount", discount);
            bundle.putString("shipping_address", addressId);
            fr = new CheckoutFragmentSecondStep();
            fr.setArguments(bundle);
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (activityName == "checkoutfraglast") {
            String addressId = sp1.getString("shipping_address", "");
            String discount = sp1.getString("discount", "");
            String paymentMode = sp1.getString("paymentmode", "");
            Bundle bundle = new Bundle();
            bundle.putString("shipping_address", addressId);
            bundle.putString("discount", discount);
            bundle.putString("paymentmode", paymentMode);
            fr = new CheckoutLastStage();
            fr.setArguments(bundle);
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (activityName == "knetpaymentwebview") {
            String KNETPaymentGatewayURL = sp1.getString("gatewayUrl", "");
            Bundle bundle = new Bundle();
            bundle.putString("gatewayUrl", KNETPaymentGatewayURL);
            fr = new KNETPaymentFragment();
            fr.setArguments(bundle);
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
		/*
		 * else if(activityName == "knetpaymentresult") { Set arrayset = new
		 * HashSet(); arrayset = sp1.getStringSet("result", null); ArrayList
		 * KNETResut = new ArrayList<String>(arrayset); Bundle bundle = new
		 * Bundle(); bundle.putStringArrayList("result", KNETResut); fr = new
		 * DialogKNETResult(); fr.setArguments(bundle); FragmentManager fm =
		 * getFragmentManager(); FragmentTransaction fragmentTransaction =
		 * fm.beginTransaction();
		 * fragmentTransaction.replace(R.id.fragment_place, fr);
		 * fragmentTransaction.addToBackStack(null);
		 * fragmentTransaction.commit(); }
		 */

        else {
			/*
			 * fr = new FragmentOne(); FragmentManager fm =
			 * getFragmentManager(); FragmentTransaction fragmentTransaction =
			 * fm.beginTransaction();
			 * fragmentTransaction.replace(R.id.fragment_place, fr);
			 * fragmentTransaction.addToBackStack(null);
			 * fragmentTransaction.commit();
			 */

            fr = new FragmentCategories();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        topHeaderTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

				/*
				 * fr = new FragmentOne(); FragmentManager fm =
				 * getFragmentManager(); FragmentTransaction fragmentTransaction
				 * = fm.beginTransaction();
				 * fragmentTransaction.replace(R.id.fragment_place, fr);
				 * fragmentTransaction.addToBackStack(null);
				 * fragmentTransaction.commit();
				 */

                fr = new FragmentCategories();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, fr);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        backToHomeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                topTabLinearLayout.setVisibility(View.VISIBLE);
                backToHomeButton.setVisibility(View.INVISIBLE);
                sortByButton.setVisibility(View.INVISIBLE);

				/*
				 * fr = new FragmentOne(); FragmentManager fm =
				 * getFragmentManager(); FragmentTransaction fragmentTransaction
				 * = fm.beginTransaction();
				 * fragmentTransaction.replace(R.id.fragment_place, fr);
				 * fragmentTransaction.addToBackStack(null);
				 * fragmentTransaction.commit();
				 */

                fr = new FragmentCategories();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, fr);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        moreButton = (Button) findViewById(R.id.morebutton);
        moreButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popup.setVisibility(View.GONE);
                SharedPreferences sp = MainActivity.this.getSharedPreferences(
                        "lan", Context.MODE_PRIVATE);
                String lan = sp.getString("lan", "en");

                switch (lan) {
                    case "en":
                        try {
                            if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                                // Notice the Gravity.Right

                                mDrawerLayout.closeDrawer(Gravity.RIGHT
                                        | Gravity.BOTTOM);

                                bottombarLayout.setVisibility(View.VISIBLE);
                            } else {
                                mDrawerLayout.openDrawer(Gravity.RIGHT
                                        | Gravity.BOTTOM);
                            }
                        } catch (Exception e) {
                            System.out.println("Here it caught an exception" + e);
                        }

                        break;

                    default:
                        try {
                            if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                                // Notice the Gravity.Right
                                mDrawerLayout.closeDrawer(Gravity.LEFT
                                        | Gravity.BOTTOM);
                            } else {
                                mDrawerLayout.openDrawer(Gravity.LEFT
                                        | Gravity.BOTTOM);
                            }
                        } catch (Exception e) {
                            System.out.println("Here it caught an exception" + e);
                        }

                        break;
                }
            }

        });

        mTitle = (String) getTitle();

        // Getting reference to the DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        View header = (View) getLayoutInflater().inflate(
                R.layout.listview_header, null);

        header.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SharedPreferences sp = MainActivity.this.getSharedPreferences(
                        "lan", Context.MODE_PRIVATE);
                String lan = sp.getString("lan", "en");

                switch (lan) {
                    case "en":
                        try {
                            if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                                // Notice the Gravity.Right

                                mDrawerLayout.closeDrawer(Gravity.RIGHT
                                        | Gravity.BOTTOM);

                                bottombarLayout.setVisibility(View.VISIBLE);
                            } else {
                                mDrawerLayout.openDrawer(Gravity.RIGHT
                                        | Gravity.BOTTOM);
                            }
                        } catch (Exception e) {
                            System.out.println("Here it caught an exception" + e);
                        }
                        break;

                    default:
                        try {
                            if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                                // Notice the Gravity.Right
                                mDrawerLayout.closeDrawer(Gravity.LEFT
                                        | Gravity.BOTTOM);
                            } else {
                                mDrawerLayout.openDrawer(Gravity.LEFT
                                        | Gravity.BOTTOM);
                            }
                        } catch (Exception e) {
                            System.out.println("Here it caught an exception" + e);
                        }

                        break;
                }
            }
        });

        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        mDrawerList.addHeaderView(header);

        // Setting DrawerToggle on DrawerLayout
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        CustomDrawerListAdapter adapter = new CustomDrawerListAdapter(
                MainActivity.this, getResources().getStringArray(R.array.More),
                imageId);

        // Setting the adapter on mDrawerList
        mDrawerList.setAdapter(adapter);

        // Setting item click listener for the mDrawerList
        mDrawerList.setOnItemClickListener(new OnItemClickListener() {

            Fragment drawerItemFragment;

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                bottombarLayout.setVisibility(View.VISIBLE);

                // Getting an array of rivers
                String[] rivers = getResources().getStringArray(R.array.More);

                // Currently selected river
                try {

                    mTitle = rivers[position - 1];
                    System.out.println("The position" + position);

                    System.out.println("The Title String is" + mTitle);
                } catch (Exception e) {
                    System.out.println("The Newsletter fnsh");
                }

                if ((mTitle.equals("Newsletter sign up"))
                        || (mTitle.equals("ابقى على تواصل"))) {
                    drawerItemFragment = new NewsletterSignup();
                    FragmentManager fragmentManager = getFragmentManager();
                    // Creating a fragment transaction
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    // Adding a fragment to the fragment transaction
                    ft.replace(R.id.fragment_place, drawerItemFragment);
                    // Committing the transaction
                    ft.commit();
                    try {
                        // Closing the drawer
                        mDrawerLayout.closeDrawer(mDrawerList);
                    } catch (Exception e) {
                        System.out
                                .println("Exception caught here in isolated view ");
                    }

                } else if ((mTitle.equals("Language"))
                        || (mTitle.equals("اللغة"))) {
                    myDialogFragment = com.dairam.android.fragments.activity.dialogfragment.MyDialogFragment
                            .newInstance();
                    myDialogFragment.show(getFragmentManager(),
                            "myDialogFragment");
					/*
					 * Log.e("The language", language); switch (language) { case
					 * "en": ArabicClicked(); break;
					 * 
					 * default: EnglishClicked(); break; }
					 */
                } else if ((mTitle.equals("Customer care"))
                        || (mTitle.equals("اتصل بنا"))) {
                    drawerItemFragment = new CustomerCareFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    // Creating a fragment transaction
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    // Adding a fragment to the fragment transaction
                    ft.replace(R.id.fragment_place, drawerItemFragment);
                    // Committing the transaction
                    ft.commit();
                    try {
                        // Closing the drawer
                        mDrawerLayout.closeDrawer(mDrawerList);
                    } catch (Exception e) {
                        System.out
                                .println("Exception caught here in isolated view ");
                    }
                } else if ((mTitle.equals("Terms and conditions"))
                        || (mTitle.equals("الشروط والأحكام"))) {
                    drawerItemFragment = new TermsAndconditionsFragment();

                    FragmentManager fragmentManager = getFragmentManager();

                    // Creating a fragment transaction
                    FragmentTransaction ft = fragmentManager.beginTransaction();

                    // Adding a fragment to the fragment transaction
                    ft.replace(R.id.fragment_place, drawerItemFragment);

                    // Committing the transaction
                    ft.commit();
                    try {
                        // Closing the drawer
                        mDrawerLayout.closeDrawer(mDrawerList);
                    } catch (Exception e) {
                        System.out
                                .println("Exception caught here in isolated view ");
                    }
                } else if ((mTitle.equals("Shipping policy"))
                        || (mTitle.equals("سياسة الشحن"))) {
                    drawerItemFragment = new ShippingPolicy();

                    FragmentManager fragmentManager = getFragmentManager();
                    // Creating a fragment transaction
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    // Adding a fragment to the fragment transaction
                    ft.replace(R.id.fragment_place, drawerItemFragment);
                    // Committing the transaction
                    ft.commit();

                    try {
                        // Closing the drawer
                        mDrawerLayout.closeDrawer(mDrawerList);
                    } catch (Exception e) {
                        System.out
                                .println("Exception caught here in isolated view ");
                    }
                } else if ((mTitle.equals("Social Media Accounts"))
                        || (mTitle.equals("التواصل الاجتماعي"))) {

                    myDialogFragmentsocial = com.dairam.android.fragments.activity.dialogfragment.MyDialogFragmentSocialmedia
                            .newInstance();
                    myDialogFragmentsocial.show(getFragmentManager(),
                            "myDialogFragment");

                } else if ((mTitle.equals("Country"))
                        || (mTitle.equals("الدولة"))) {
                    new DownloadJSON().execute();

                } else if ((mTitle.equals("My Account"))
                        || (mTitle.equals("حسابي"))) {

                    drawerItemFragment = new MyAccountItemFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    // Creating a fragment transaction
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    // Adding a fragment to the fragment transaction
                    ft.replace(R.id.fragment_place, drawerItemFragment);
                    // Committing the transaction
                    ft.commit();

                    try {
                        // Closing the drawer
                        mDrawerLayout.closeDrawer(mDrawerList);
                    } catch (Exception e) {
                        System.out.println("Exception caught here in isolated view ");
                    }

					/*
					 * try { // Closing the drawer
					 * mDrawerLayout.closeDrawer(Gravity.RIGHT |
					 * Gravity.BOTTOM); mDrawerLayout.closeDrawer(mDrawerList);
					 * } catch (Exception e) { System.out
					 * .println("Exception caught here in isolated view "); }
					 */
                }
            }
        });
    }

    public void selectFrag(View view) {
        PrefUtil.setGridcount(getApplicationContext(), 0);

        bottombarLayout.setVisibility(View.VISIBLE);

        if (view == findViewById(R.id.brandsbutton))
        {
            connectionDetector = new ConnectionDetector(getApplicationContext());
            isInternetPresent = connectionDetector.isConnectingToInternet();
            if (isInternetPresent) {
                popup.setVisibility(View.GONE);
                fr = new FragmentBrnad();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, fr);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            } else {
                fr = new NoInternetFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, fr);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

        }

        if (view == findViewById(R.id.categorybutton)) {

            try {
                mDrawerLayout.closeDrawer(Gravity.RIGHT | Gravity.BOTTOM);
            } catch (Exception e) {
                System.out.println("Exception caught");
            }

            connectionDetector = new ConnectionDetector(getApplicationContext());
            isInternetPresent = connectionDetector.isConnectingToInternet();

            if (isInternetPresent) {
                popup.setVisibility(View.GONE);

                fr = new FragmentOne();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, fr);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

				/*
				 * fr = new FragmentCategories(); FragmentManager fm =
				 * getFragmentManager(); FragmentTransaction fragmentTransaction
				 * = fm.beginTransaction();
				 * fragmentTransaction.replace(R.id.fragment_place, fr);
				 * fragmentTransaction.addToBackStack(null);
				 * fragmentTransaction.commit();
				 */
            } else {
                fr = new NoInternetFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, fr);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }

        if (view == findViewById(R.id.shopbutton)) {

			/*
			 * try { mDrawerLayout.closeDrawer(Gravity.RIGHT | Gravity.BOTTOM);
			 * } catch (Exception e) { System.out.println("Exception caught"); }
			 */

            try {
                // Closing the drawer
                mDrawerLayout.closeDrawer(mDrawerList);
            } catch (Exception e) {
                System.out.println("Exception caught here in isolated view ");
            }

            connectionDetector = new ConnectionDetector(getApplicationContext());
            isInternetPresent = connectionDetector.isConnectingToInternet();

            if (isInternetPresent) {
                System.out
                        .println("Entered here................................");
                popup.setVisibility(View.GONE);

                fr = new FragmentCategories();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, fr);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

				/*
				 * fr = new FragmentOne(); FragmentManager fm =
				 * getFragmentManager(); FragmentTransaction fragmentTransaction
				 * = fm.beginTransaction();
				 * fragmentTransaction.replace(R.id.fragment_place, fr);
				 * fragmentTransaction.addToBackStack(null);
				 * fragmentTransaction.commit();
				 */
            } else {
                fr = new NoInternetFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, fr);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
        if (view == findViewById(R.id.stylebutton)) {

            try {
                // Closing the drawer
                mDrawerLayout.closeDrawer(mDrawerList);
            } catch (Exception e) {
                System.out.println("Exception caught here in isolated view ");
            }
            connectionDetector = new ConnectionDetector(getApplicationContext());
            isInternetPresent = connectionDetector.isConnectingToInternet();

            if (isInternetPresent) {
                popup.setVisibility(View.GONE);
                fr = new StylesFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, fr);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            } else {
                fr = new NoInternetFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, fr);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }

        if (view == findViewById(R.id.loginbutton)) {
            signOutFromGplus();
			/*
			 * try { mDrawerLayout.closeDrawer(Gravity.RIGHT | Gravity.BOTTOM);
			 * } catch (Exception e) { System.out.println("Exception caught"); }
			 */

            try {
                // Closing the drawer
                mDrawerLayout.closeDrawer(mDrawerList);
            } catch (Exception e) {
                System.out.println("Exception caught here in isolated view ");
            }
            connectionDetector = new ConnectionDetector(getApplicationContext());
            isInternetPresent = connectionDetector.isConnectingToInternet();

            if (isInternetPresent) {
                String id = null;
                popup.setVisibility(View.GONE);
                SharedPreferences sharedPreferences = getApplicationContext()
                        .getSharedPreferences(MyPREFERENCES,
                                Context.MODE_PRIVATE);
                if (sharedPreferences.contains(customerid)) {
                    id = sharedPreferences.getString(customerid, "");
                }

                if (id == null) {
                    fr = new LoginFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("title", "FromHomeScreen");
                    bundle.putString("ID", "0");
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm
                            .beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_place, fr);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    new DownloadJSONLogout().execute();

                }

            } else {
                fr = new NoInternetFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, fr);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }

        if (view == findViewById(R.id.wishlistbutton)) {
			/*
			 * try { mDrawerLayout.closeDrawer(Gravity.RIGHT | Gravity.BOTTOM);
			 * } catch (Exception e) { System.out.println("Exception caught"); }
			 */

            try {
                // Closing the drawer
                mDrawerLayout.closeDrawer(mDrawerList);
            } catch (Exception e) {
                System.out.println("Exception caught here in isolated view ");
            }
            connectionDetector = new ConnectionDetector(getApplicationContext());
            isInternetPresent = connectionDetector.isConnectingToInternet();

            if (isInternetPresent) {
                popup.setVisibility(View.GONE);

                System.out.println("Do nothing");
                fr = new WishListFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, fr);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            } else {
                fr = new NoInternetFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, fr);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

        }

        if (view == findViewById(R.id.cartbutton)) {
			/*
			 * try { mDrawerLayout.closeDrawer(Gravity.RIGHT | Gravity.BOTTOM);
			 * } catch (Exception e) { System.out.println("Exception caught"); }
			 */

            try {
                // Closing the drawer
                mDrawerLayout.closeDrawer(mDrawerList);
            } catch (Exception e) {
                System.out.println("Exception caught here in isolated view ");
            }
            connectionDetector = new ConnectionDetector(getApplicationContext());
            isInternetPresent = connectionDetector.isConnectingToInternet();

            if (isInternetPresent) {
                popup.setVisibility(View.GONE);
                System.out.println("Do nothing");
                fr = new CartFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, fr);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            } else {
                fr = new NoInternetFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, fr);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

        }

        if (view == findViewById(R.id.searchbutton)) {

			/*
			 * try { mDrawerLayout.closeDrawer(Gravity.RIGHT | Gravity.BOTTOM);
			 * } catch (Exception e) { System.out.println("Exception caught"); }
			 */

            try {
                // Closing the drawer
                mDrawerLayout.closeDrawer(mDrawerList);
            } catch (Exception e) {
                System.out.println("Exception caught here in isolated view ");
            }
            connectionDetector = new ConnectionDetector(getApplicationContext());
            isInternetPresent = connectionDetector.isConnectingToInternet();

            if (isInternetPresent) {

                popup.setVisibility(View.VISIBLE);
                popup.startAnimation(animShow);
                linearLayoutHeader.setVisibility(View.GONE);
                hideButton.setEnabled(true);

                searchEdit.setText("");
                searchEdit.requestFocus();
                InputMethodManager imm = (InputMethodManager) getApplicationContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchEdit, 0);
                // imm.hideSoftInputFromWindow(searchEdit.getWindowToken(), 0);
                hideButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {

                        popup.setVisibility(View.GONE);
                        popup.startAnimation(animHide);
                        linearLayoutHeader.setVisibility(View.VISIBLE);
                        hideButton.setEnabled(false);

                        linearLayoutHeader.setVisibility(View.VISIBLE);

                        InputMethodManager imm = (InputMethodManager) getApplicationContext()
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(
                                searchEdit.getWindowToken(), 0);

						/* popup.setVisibility(View.GONE); */
                    }
                });

				/*
				 * btnGo.setOnClickListener(new OnClickListener() {
				 * 
				 * @Override public void onClick(View v) {
				 * if(searchEdit.length()>0) { InputMethodManager imm =
				 * (InputMethodManager) getApplicationContext()
				 * .getSystemService(Context.INPUT_METHOD_SERVICE);
				 * imm.hideSoftInputFromWindow(searchEdit.getWindowToken(), 0);
				 * 
				 * searchItem(searchEdit.getText().toString()); }
				 * 
				 * } });
				 */

                searchEdit.setOnKeyListener(new OnKeyListener() {

                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        InputMethodManager imm = (InputMethodManager) getApplicationContext()
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            if (searchEdit.length() > 0) {
                                imm.hideSoftInputFromWindow(
                                        searchEdit.getWindowToken(), 0);
                                //Fabric Search.............................
                                Answers.getInstance().logSearch(new SearchEvent()
                                        .putQuery(searchEdit.getText().toString()));

                                searchItem(searchEdit.getText().toString());
                            }
                        }
                        return false;
                    }
                });

				/*
				 * searchEdit.addTextChangedListener(new TextWatcher(){ public
				 * void afterTextChanged(Editable s) {
				 * 
				 * if (s.length()>3) { InputMethodManager imm =
				 * (InputMethodManager) getApplicationContext()
				 * .getSystemService(Context.INPUT_METHOD_SERVICE);
				 * imm.hideSoftInputFromWindow(searchEdit.getWindowToken(), 0);
				 * 
				 * 
				 * searchItem(s.toString()); }
				 * 
				 * } public void beforeTextChanged(CharSequence s, int start,
				 * int count, int after){
				 * 
				 * } public void onTextChanged(CharSequence s, int start, int
				 * before, int count){
				 * 
				 * } });
				 */
            } else {
                fr = new NoInternetFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, fr);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // if(getFragmentManager().getBackStackEntryCount()==0){
        Log.e("Stack Count ", "0");
        finish();
        // }else{
        // Log.e("Stack Count ",
        // "......"+getFragmentManager().getBackStackEntryCount());

        super.onBackPressed();
        // }

    }

    /* Reading the language specifier from the file. */
    public void readFile() {
        try {
            FileInputStream fis = openFileInput(FILENAMELANGUAGE);
            InputStreamReader in = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(in);
            String data = br.readLine();
            System.out.println("The data from the file is " + data);
        } catch (Exception e) {
            System.out.println("Exception caught in reading the file " + e);
        }
    }

    @Override
    public void hide() {

		/*
		 * int width = 0; int height = 0; topTabLinearLayout.setLayoutParams(new
		 * LayoutParams(width, height));
		 */
        topTabLinearLayout.setVisibility(View.GONE);
        diView.setVisibility(View.GONE);
    }

    @Override
    public void showTopBar(String string) {
        System.out.println("Reached inside the method" + string);
        backToHomeButton.setVisibility(View.VISIBLE);
        sortByButton.setVisibility(View.INVISIBLE);
        topHeaderTextView.setText(string);
        topHeaderTextView.setVisibility(View.VISIBLE);
    }

    public void showTab() {
        topTabLinearLayout.setVisibility(View.VISIBLE);
        diView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showcategory(String string) {

        backToHomeButton.setVisibility(View.VISIBLE);
        sortByButton.setVisibility(View.INVISIBLE);
        topHeaderTextView.setText(" " + string);
        sortByButton.setVisibility(View.INVISIBLE);
        topHeaderTextView.setVisibility(View.VISIBLE);
        linearLayoutHeader.setBackgroundResource(R.drawable.dairam_header2);
    }

    @Override
    public void defaultHeader() {

        backToHomeButton.setVisibility(View.INVISIBLE);
        sortByButton.setVisibility(View.INVISIBLE);
        topHeaderTextView.setVisibility(View.INVISIBLE);

        try {
            SharedPreferences sp = MainActivity.this.getSharedPreferences(
                    "lan", Context.MODE_PRIVATE);
            String lan = sp.getString("lan", "en");

            switch (lan) {
                case "en":
                    linearLayoutHeader
                            .setBackgroundResource(R.drawable.dairamheader);
                    break;
                default:
                    linearLayoutHeader
                            .setBackgroundResource(R.drawable.dairam_ar_header);
            }
        } catch (Exception e) {
            System.out.println("The Exception caught" + e.getMessage());
        }

    }

    @Override
    public void setTopBarForCategory() {
        SharedPreferences sp = MainActivity.this.getSharedPreferences("lan",
                Context.MODE_PRIVATE);
        String lan = sp.getString("lan", "en");

        switch (lan) {
            case "en":
			/*
			 * categoryButton
			 * .setBackgroundResource(R.drawable.categories_icon_sel);
			 */
                categoryButton.setBackgroundResource(R.drawable.bestoff_sel);
                break;

            default:
                categoryButton.setBackgroundResource(R.drawable.bestoff_ar_sel);
                break;
        }
    }

    @Override
    public void makeDefaultHeader() {

        SharedPreferences sp = MainActivity.this.getSharedPreferences("lan",
                Context.MODE_PRIVATE);
        String lan = sp.getString("lan", "en");

        switch (lan) {
            case "en":
                categoryButton.setBackgroundResource(R.drawable.bestoff);
                brandsButton.setBackgroundResource(R.drawable.brandsicon);
                searchButton.setBackgroundResource(R.drawable.search_icon);
                loginButton.setBackgroundResource(R.drawable.loginicon);

                shopButton.setBackgroundResource(R.drawable.shop_icon);
                stylesButton.setBackgroundResource(R.drawable.style_icon);
                wishListButton.setBackgroundResource(R.drawable.wishlist);
                cartButton.setBackgroundResource(R.drawable.cart);
                break;

            default:
                categoryButton.setBackgroundResource(R.drawable.bestoff_ar);
                brandsButton.setBackgroundResource(R.drawable.brands_ar);
                searchButton.setBackgroundResource(R.drawable.search_ar);
                loginButton.setBackgroundResource(R.drawable.login_ar);

                shopButton.setBackgroundResource(R.drawable.shop_ar);
                stylesButton.setBackgroundResource(R.drawable.style_ar);
                wishListButton.setBackgroundResource(R.drawable.wishlist_ar);
                cartButton.setBackgroundResource(R.drawable.cart_ar);
                break;
        }

    }

    @Override
    public void setTopBarForBrands() {

        SharedPreferences sp = MainActivity.this.getSharedPreferences("lan",
                Context.MODE_PRIVATE);
        String lan = sp.getString("lan", "en");

        switch (lan) {
            case "en":
                brandsButton.setBackgroundResource(R.drawable.brands_icon_sel);
                break;

            default:
                brandsButton.setBackgroundResource(R.drawable.brands_ar_sel);
                break;
        }

    }

    @Override
    public void setTopBarForStyle() {
        SharedPreferences sp = MainActivity.this.getSharedPreferences("lan",
                Context.MODE_PRIVATE);
        String lan = sp.getString("lan", "en");

        switch (lan) {
            case "en":
                stylesButton.setBackgroundResource(R.drawable.style_icon_sel);
                break;

            default:
                stylesButton.setBackgroundResource(R.drawable.style_ar_sel);
                break;
        }

    }

    @Override
    public void setTopBarForShop() {
        SharedPreferences sp = MainActivity.this.getSharedPreferences("lan",
                Context.MODE_PRIVATE);
        String lan = sp.getString("lan", "en");

        switch (lan) {
            case "en":
                shopButton.setBackgroundResource(R.drawable.shop_icon_sel);
                break;

            default:
                shopButton.setBackgroundResource(R.drawable.shop_ar_sel);
                break;
        }

    }

    @Override
    public void setTopBarForCart() {
        SharedPreferences sp = MainActivity.this.getSharedPreferences("lan",
                Context.MODE_PRIVATE);
        String lan = sp.getString("lan", "en");

        switch (lan) {
            case "en":
                cartButton.setBackgroundResource(R.drawable.cart_sel);
                break;

            default:
                cartButton.setBackgroundResource(R.drawable.cart_ar_sel);
                break;
        }

    }

    @Override
    public void setTopBarForWishlist() {

        SharedPreferences sp = MainActivity.this.getSharedPreferences("lan",
                Context.MODE_PRIVATE);
        String lan = sp.getString("lan", "en");
        switch (lan) {
            case "en":
                wishListButton.setBackgroundResource(R.drawable.wishlist_sel);
                break;

            default:
                wishListButton.setBackgroundResource(R.drawable.wishlist_ar_sel);
                break;
        }
    }

    @Override
    public void showSortBy() {

        sortByButton.setVisibility(View.VISIBLE);

    }

	/*
	 * IMPORTANT FINAL STARTS (non-Javadoc)
	 * 
	 * @see com.dairam.interfaces.listenerHideView#promoItemHideTopbar()
	 */

    @Override
    public void promoItemHideTopbar() {
        linearLayoutHeader.setVisibility(View.GONE);
        topTabLinearLayout.setVisibility(View.GONE);
        diView.setVisibility(View.GONE);
    }

    @Override
    public void showBottomBar() {
        linearLayoutBottomBar.setVisibility(View.VISIBLE);
        dividerViewBottom.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBottomBar() {
        linearLayoutBottomBar.setVisibility(View.GONE);
        dividerViewBottom.setVisibility(View.GONE);
    }

	/*
	 * IMPORTANT FINAL ENDS
	 */

    @Override
    public void reversePromoItemHideTopbar() {

        linearLayoutHeader = (LinearLayout) findViewById(R.id.linearlayoutheader);
        topTabLinearLayout = (LinearLayout) findViewById(R.id.LinearLayouttopmenu);
        linearLayoutHeader.setVisibility(View.VISIBLE);
        topTabLinearLayout.setVisibility(View.VISIBLE);
    }

    public void fbSelected() {

        try {
            String facebookUrl = "https://www.facebook.com/dairamcom";

            Uri uri = Uri.parse("fb://facewebmodal/f?href=" + facebookUrl);
            // Uri uri = Uri.parse("fb://facewebmodal/f?href=" +
            // "fb://profile/344207749051029");
            Log.e("FB URI in Try", "" + uri);
            startActivity(new Intent(Intent.ACTION_VIEW, uri));

        } catch (Exception e) {// or else open in browser instead
            String facebookUrl = "https://www.facebook.com/dairamcom";
            Log.e("FB URI in Catch", "" + facebookUrl);
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));
        }
    }

    public void twitterSelected() {

        System.out.println("Enter into twitter");

        Uri uri = Uri.parse("https://twitter.com/dairam_com");
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.twitter.android");

        try {
            startActivity(likeIng);

        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://twitter.com/dairam_com")));
        }
    }

    public void instaGram() {
        Uri uri = Uri.parse("http://instagram.com/dairam_com");
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
        likeIng.setPackage("com.instagram.android");
        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/dairam_com  ")));
        }
    }

    public void pinterest() {
        Uri uri = Uri.parse("http://www.pinterest.com/dairamcom/");
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
        likeIng.setPackage("com.pinterest");
        try {
            startActivity(likeIng);

        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.pinterest.com/dairamcom/")));
        }
    }

    public void gPlus() {
        Uri uri = Uri.parse("http://www.plus.google.com/dairam_com/");
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
        likeIng.setPackage("com.pinterest");
        try {
            startActivity(likeIng);

        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.plus.google.com/dairam_com/")));
        }
    }

    @Override
    public void shopmenublack() {
        SharedPreferences sp = MainActivity.this.getSharedPreferences("lan",
                Context.MODE_PRIVATE);
        String lan = sp.getString("lan", "en");

        switch (lan) {
            case "en":
                shopButton.setBackgroundResource(R.drawable.shop_icon);
                break;

            default:
                shopButton.setBackgroundResource(R.drawable.shop_ar);
                break;
        }

    }

    @Override
    public void ShopmenuRed() {

        SharedPreferences sp = MainActivity.this.getSharedPreferences("lan",
                Context.MODE_PRIVATE);
        String lan = sp.getString("lan", "en");

        switch (lan) {
            case "en":
                shopButton.setBackgroundResource(R.drawable.shop_icon_sel);
                break;

            default:
                shopButton.setBackgroundResource(R.drawable.shop_ar_sel);
                break;
        }

    }

    @Override
    public void wishListCountUpdation(String countString) {
        String id_ = "";
        sharedPreferences = getApplicationContext().getSharedPreferences(
                MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(customerid)) {
            id_ = sharedPreferences.getString(customerid, "");
        }
        if (id_ == "") {
            wishListCount.setVisibility(View.INVISIBLE);
        } else if (id_ == null) {
            wishListCount.setVisibility(View.INVISIBLE);
        } else {

            if (countString.equals("0")) {
                wishListCount.setVisibility(View.INVISIBLE);
            } else {
                wishListCount.setVisibility(View.VISIBLE);
                wishListCount.setText(" " + countString + " ");
            }

        }
    }

    @Override
    public void cartListCountUpdation(String countString) {

        Log.e("Cart Count Updation", "Cart Count Updation");
        String id_ = "";
        sharedPreferences = getApplicationContext().getSharedPreferences(
                MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(customerid)) {
            id_ = sharedPreferences.getString(customerid, "");
        }
        if (id_ == "") {
            cartListCount.setVisibility(View.INVISIBLE);
        } else if (id_ == null) {
            cartListCount.setVisibility(View.INVISIBLE);
        } else {
            if (countString.equals("0")) {
                cartListCount.setVisibility(View.INVISIBLE);
            } else {
                cartListCount.setVisibility(View.VISIBLE);
                cartListCount.setText(" " + countString + " ");
            }
        }

    }

    @Override
    public void search() {

        sortByButton.setVisibility(View.VISIBLE);
        sortByButton.setText("Cancel");
        linearLayoutHeader.setBackgroundColor(Color.parseColor("#BCBFBA"));
        searchButton.setBackgroundResource(R.drawable.search_icon_sel);

    }

    private void setLanguage(String lang) {
        String languageToLoad = lang;
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, null);
    }

    public void searchItem(String s) {

        popup.startAnimation(animHide);
        hideButton.setEnabled(false);

        popup.setVisibility(View.GONE);
        linearLayoutHeader.setVisibility(View.VISIBLE);
        String content = s.replaceAll(" ", "%20");
        Log.e("MainActivity Search", content);
        // String url =
        // "http://dairam.com/index.php?route=api/product/list&search="+s+"&language=1&currency=KWD";
        String url = "http://dairam.com/index.php?route=api/product/list&search="
                + content.trim();
        fr = new PromoItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", "Search");
        bundle.putString("referenceurl", url);
        fr.setArguments(bundle);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_place, fr);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void dairamBar() {
        linearLayoutHeader.setVisibility(View.GONE);
    }

    @Override
    public void hidePopup() {
        bottombarLayout.setVisibility(View.VISIBLE);
    }

    public void hideSearchPopup() {
        popup.setVisibility(View.GONE);
    }

    public void CancelDialog() {
        System.out.println("No Action");
    }

    public void EnglishClicked() {
        SharedPreferences sp = MainActivity.this.getSharedPreferences("lan",
                Context.MODE_PRIVATE);
        Editor e = sp.edit();
        e.putString("lan", "en");
        e.commit();

        MainActivity.this.finish();
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void ArabicClicked() {
        SharedPreferences sp = MainActivity.this.getSharedPreferences("lan",
                Context.MODE_PRIVATE);
        Editor e = sp.edit();
        e.putString("lan", "ar");
        e.commit();

        MainActivity.this.finish();
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
////old code of G+login..uncomment if need
   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
		*//*
		 * switch (requestCode) { case RC_SIGN_IN: if (requestCode ==
		 * RC_SIGN_IN) { if (resultCode != RESULT_OK) { mSignInClicked = false;
		 * }
		 * 
		 * mIntentInProgress = false;
		 * 
		 * if (!mGoogleApiClient.isConnecting()) { mGoogleApiClient.connect(); }
		 * } break;
		 * 
		 * default: Session.getActiveSession().onActivityResult(this,
		 * requestCode, resultCode, data); break; }
		 *//*
        switch (requestCode) {
            case RC_SIGN_IN:
                if (resultCode == RESULT_OK) {
                    // If the error resolution was successful we should continue
                    // processing errors.
                    mSignInProgress = STATE_SIGN_IN;
                } else {
                    // If the error resolution was not successful or the user
                    // canceled,
                    // we should stop processing errors.
                    mSignInProgress = STATE_DEFAULT;

                }

                if (!mGoogleApiClient.isConnecting()) {
                    // If Google Play services resolved the issue with a dialog then
                    // onStart is not called so we need to re-attempt connection
                    // here.
                    mGoogleApiClient.connect();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                Session.getActiveSession().onActivityResult(this, requestCode,
                        resultCode, data);
                break;

        }

        Log.e("The Requested code", requestCode + "");

    }*/////old code of G+login..uncomment if need

    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this,
                    R.style.MyTheme);
            mProgressDialog.setCancelable(false);
            mProgressDialog
                    .setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                // Create an array
                arraylistcountry = new ArrayList<HashMap<String, String>>();
                // Retrieve JSON Objects from the given URL address
                SharedPreferences sp = MainActivity.this.getSharedPreferences(
                        "lan", Context.MODE_PRIVATE);
                // jsonobject =
                // JSONfunctions.getJSONfromURL(AppConstants.CURRENCIES_URL);
                if (sp.getString("lan", "en").equals("en")) {
                    jsonobject = JSONfunctions
                            .getJSONfromURL("http://dairam.com/index.php?route=api/currency/list&language=1");
                } else {
                    jsonobject = JSONfunctions
                            .getJSONfromURL("http://dairam.com/index.php?route=api/currency/list&language=2");
                }

            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            mProgressDialog.dismiss();

            try {
                if (jsonobject != null) {
                    if (jsonobject.getBoolean("success")) {

                        // Locate the array name in JSON
                        jsonarray = jsonobject
                                .getJSONArray(AppConstants.CURRENCIES_JSON_OBJ);

                        for (int i = 0; i < jsonarray.length(); i++) {
                            HashMap<String, String> map = new HashMap<String, String>();
                            jsonobject = jsonarray.getJSONObject(i);
                            // Retrive JSON Objects
                            map.put(AppConstants.CURRENCIES_NAME, jsonobject
                                    .getString(AppConstants.CURRENCIES_NAME));

                            map.put(AppConstants.CURRENCIES_CODE, jsonobject
                                    .getString(AppConstants.CURRENCIES_CODE));
							/*
							 * map.put(AppConstants.HOME_PAGE_TAG_LINK,
							 * jsonobject.getString
							 * (AppConstants.HOME_PAGE_TAG_LINK));
							 * map.put(AppConstants.HOME_PAGE_TAG_IMAGE,
							 * jsonobject.getString
							 * (AppConstants.HOME_PAGE_TAG_IMAGE));
							 */
                            // Set the JSON Objects into the array
                            arraylistcountry.add(map);
                        }

                        System.out.println("Arraylist countries"
                                + arraylistcountry);
                        myDialogFragmentcountry = com.dairam.android.fragments.activity.dialogfragment.MyDialogCountries
                                .newInstance(arraylistcountry,
                                        getApplicationContext());
                        myDialogFragmentcountry.show(getFragmentManager(),
                                "myDialogFragment");
                    } else {
                        Toast.makeText(getApplicationContext(),
                                R.string.no_response, Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            R.string.no_response, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

        }
    }

    @Override
    public void loginlogout() {
        String id = null;

        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(customerid)) {
            id = sharedPreferences.getString(customerid, "");
        }

        if (id == null) {
            System.out.println("Do nothing");
        } else {
            SharedPreferences sp = MainActivity.this.getSharedPreferences(
                    "lan", Context.MODE_PRIVATE);
            String lan = sp.getString("lan", "en");
            switch (lan) {
                case "en":
                    Log.e("Enterstocondition", "Logoutarabci");
                    loginButton.setBackground(getResources().getDrawable(
                            R.drawable.logout_icon));
                    break;

                default:
                    loginButton.setBackground(getResources().getDrawable(
                            R.drawable.logout2_ar));
                    break;
            }

        }

    }

/*
    public static void callFacebookLogout(Context context) {
        Session session = Session.getActiveSession();
        if (session != null) {

            if (!session.isClosed()) {
                session.closeAndClearTokenInformation();
                // clear your preferences if saved
            }
        } else {

            session = new Session(context);
            Session.setActiveSession(session);

            session.closeAndClearTokenInformation();
            // clear your preferences if saved

        }

    }
*/

    private class DownloadJSONLogout extends AsyncTask<Void, Void, Void> {

        String message_ = " ";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this,
                    R.style.MyTheme);
            mProgressDialog.setCancelable(false);
            mProgressDialog
                    .setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                // Create an array
                arraylistcountry = new ArrayList<HashMap<String, String>>();
                // Retrieve JSON Objects from the given URL address

                String cust_id = null;
                SharedPreferences sharedPreferences = getApplicationContext()
                        .getSharedPreferences(MyPREFERENCES,
                                Context.MODE_PRIVATE);
                if (sharedPreferences.contains(customerid)) {
                    cust_id = sharedPreferences.getString(customerid, "");
                }

                String logout_Url = AppConstants.LOGOUT_URL + cust_id
                        + "&language=" + getLanguage();
                Log.e("Logout URL", logout_Url);
                jsonobject = JSONfunctions.getJSONfromURL(logout_Url);

                Log.e("Logout Response", jsonobject.toString());

                message_ = jsonobject.getString("success_msg");

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
                        jsonarray = jsonobject
                                .getJSONArray(AppConstants.CURRENCIES_JSON_OBJ);

                        for (int i = 0; i < jsonarray.length(); i++) {
                            HashMap<String, String> map = new HashMap<String, String>();
                            jsonobject = jsonarray.getJSONObject(i);
                            // Retrive JSON Objects
                            map.put(AppConstants.CURRENCIES_NAME, jsonobject
                                    .getString(AppConstants.CURRENCIES_NAME));
							/*
							 * map.put(AppConstants.HOME_PAGE_TAG_LINK,
							 * jsonobject.getString
							 * (AppConstants.HOME_PAGE_TAG_LINK));
							 * map.put(AppConstants.HOME_PAGE_TAG_IMAGE,
							 * jsonobject.getString
							 * (AppConstants.HOME_PAGE_TAG_IMAGE));
							 */
                            // Set the JSON Objects into the array
                            arraylistcountry.add(map);

                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                R.string.no_response, Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            R.string.no_response, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            SharedPreferences sharedpreferences;
            sharedpreferences = getApplicationContext().getSharedPreferences(
                    MyPREFERENCES, Context.MODE_PRIVATE);
            Editor editor = sharedpreferences.edit();
            editor.putString(customerid, null);
            editor.putString(firstname, null);
            editor.putString(lastname, null);
            editor.putString(email_string, null);
            editor.putString(cart_string, null);
            editor.putString(wishlist_String, null);

            editor.commit();

            sharedpreferences.edit().clear().commit();

            mProgressDialog.dismiss();
            Toast.makeText(getApplicationContext(), message_, Toast.LENGTH_LONG)
                    .show();
            finish();
           // callFacebookLogout(getApplicationContext());
            signOutFromGplus();
            deleteCache(getApplicationContext());

            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    public static void deleteCache(Context context) {
        try {
            Log.e("Delet Cache", "In delete Catche method");
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
        }
    }

    public static boolean deleteDir(File dir) {
        Log.e("Delet Cache dir", "In delete Catche Dir method");
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    private void updateCartAndWishlistCount() {

        String id_ = null;
        sharedPreferences = getApplicationContext().getSharedPreferences(
                MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(customerid)) {
            id_ = sharedPreferences.getString(customerid, "");
        }

        if (id_ == null) {
            cartListCount.setVisibility(View.INVISIBLE);
        } else if (id_ == "") {
            cartListCount.setVisibility(View.INVISIBLE);
        } else {
            String cart_count_ = sharedPreferences.getString(cart_string, "");
            String wishlisr_count_ = sharedPreferences.getString(
                    wishlist_String, "");
            listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) this;
            listenerHideView.cartListCountUpdation(cart_count_);
            listenerHideView.wishListCountUpdation(wishlisr_count_);
        }
    }

	/*
	 * @Override public void onConnectionFailed(ConnectionResult arg0) { if
	 * (!arg0.hasResolution()) {
	 * GooglePlayServicesUtil.getErrorDialog(arg0.getErrorCode(), this, 0)
	 * .show(); return; }
	 * 
	 * if (!mIntentInProgress) { // Store the ConnectionResult for later usage
	 * mConnectionResult = arg0;
	 * 
	 * if (mSignInClicked) { // The user has already clicked 'sign-in' so we
	 * attempt to // resolve all // errors until the user is signed in, or they
	 * cancel. resolveSignInError(); } }
	 * 
	 * }
	 * 
	 * @Override public void onConnected(Bundle arg0) { mSignInClicked = false;
	 * // Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
	 * 
	 * // Get user's information getProfileInformation(); }
	 * 
	 * @Override public void onConnectionSuspended(int arg0) {
	 * 
	 * }
	 */

    // Gplus signin code
    // section......................................................................

    @Override
    protected void onStart() {
        super.onStart();
		/*
		 * if(!mGoogleApiClient.isConnected()) mGoogleApiClient.connect();
		 */
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_PROGRESS, mSignInProgress);
    }

    /*
     * onConnected is called when our Activity successfully connects to Google
     * Play services. onConnected indicates that an account was selected on the
     * device, that the selected account has granted any requested permissions
     * to our app and that we were able to establish a service connection to
     * Google Play services.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Reaching onConnected means we consider the user signed in.
        Log.i("In MActivity", "onConnected");

        // Update the user interface to reflect that the user is signed in.
        if (Util.isNetworkAvailable(getApplicationContext())) {
            // getProfileInformation();
        } else {
            fr = new FragmentCategories();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

			/*
			 * fr = new FragmentOne(); FragmentManager fm =
			 * getFragmentManager(); FragmentTransaction fragmentTransaction =
			 * fm.beginTransaction();
			 * fragmentTransaction.replace(R.id.fragment_place, fr);
			 * fragmentTransaction.addToBackStack(null);
			 * fragmentTransaction.commit();
			 */

        }

        // Indicate that the sign in process is complete.
        mSignInProgress = STATE_DEFAULT;
    }

    /*
     * onConnectionFailed is called when our Activity could not connect to
     * Google Play services. onConnectionFailed indicates that the user needs to
     * select an account, grant permissions or resolve an error in order to sign
     * in.
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes
        // might
        // be returned in onConnectionFailed.
        Log.i("In MainActivity",
                "onConnectionFailed: ConnectionResult.getErrorCode() = "
                        + result.getErrorCode());

        if (result.getErrorCode() == ConnectionResult.API_UNAVAILABLE) {
            // An API requested for GoogleApiClient is not available. The
            // device's current
            // configuration might not be supported with the requested API or a
            // required component
            // may not be installed, such as the Android Wear application. You
            // may need to use a
            // second GoogleApiClient to manage the application's optional APIs.
        } else if (mSignInProgress != STATE_IN_PROGRESS) {
            // We do not have an intent in progress so we should store the
            // latest
            // error resolution intent for use when the sign in button is
            // clicked.
            mSignInIntent = result.getResolution();
            mSignInError = result.getErrorCode();

            if (mSignInProgress == STATE_SIGN_IN) {
                // STATE_SIGN_IN indicates the user already clicked the sign in
                // button
                // so we should continue processing errors until the user is
                // signed in
                // or they click cancel.
                resolveSignInError();
            }
        }

    }

    /*
     * Starts an appropriate intent or dialog for user interaction to resolve
     * the current error preventing the user from being signed in. This could be
     * a dialog allowing the user to select an account, an activity allowing the
     * user to consent to the permissions being requested by your app, a setting
     * to enable device networking, etc.
     */
    private void resolveSignInError() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

        //mGoogleApiClient.connect();

        /*if (mSignInIntent != null) {
            // We have an intent which will allow our user to sign in or
            // resolve an error. For example if the user needs to
            // select an account to sign in with, or if they need to consent
            // to the permissions your app is requesting.

            try {
                // Send the pending intent that we stored on the most recent
                // OnConnectionFailed callback. This will allow the user to
                // resolve the error currently preventing our connection to
                // Google Play services.
                mSignInProgress = STATE_IN_PROGRESS;
                startIntentSenderForResult(mSignInIntent.getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (SendIntentException e) {
                Log.i("in MainActivity", "Sign in intent could not be sent: "
                        + e.getLocalizedMessage());
                // The intent was canceled before it was sent. Attempt to
                // connect to
                // get an updated ConnectionResult.
                mSignInProgress = STATE_SIGN_IN;
                mGoogleApiClient.connect();
            }
        } else {
            // Google Play services wasn't able to provide an intent for some
            // error types, so we show the default Google Play services error
            // dialog which may still start an intent on our behalf if the
            // user can resolve the issue.
            showDialog(DIALOG_PLAY_SERVICES_ERROR);
        }*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from
        //   GoogleSignInApi.getSignInIntent(...);
        switch (requestCode) {
            case RC_SIGN_IN:

                if (requestCode == RC_SIGN_IN) {
                    GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                    if (result.isSuccess()) {
                        GoogleSignInAccount acct = result.getSignInAccount();
                        // Get account information

                        String personName = acct.getDisplayName();
                        // String personPhotoUrl = acct.getImage().getUrl();
                        // String personGooglePlusProfile = acct.getUrl();
                        String email = acct.getEmail();

                        Log.e("dkjf", "Name: " + personName + ", plusProfile: "
                                + ", email: " + email
                                + ", Image: ");

                        LoginWithGoogle(personName, email);
                    }
                }
                    break;
                    default:
                        super.onActivityResult(requestCode, resultCode, data);
                       /* Session.getActiveSession().onActivityResult(this, requestCode,
                                resultCode, data);*/
                        break;

                }


                Log.e("The Requested code", requestCode + "");

        }
        @Override
        public void onConnectionSuspended ( int cause){
            // The connection to Google Play services was lost for some reason.
            // We call connect() to attempt to re-establish the connection or get a
            // ConnectionResult that we can attempt to resolve.
            mGoogleApiClient.connect();
        }

        // Gplus signin code..

    public void signInWithGplus(String title, String actionSpec, String id,
                                String bannerTitle, String url) {
        this.title = title;
        this.actionSpec = actionSpec;
        this.id = id;
        this.bannerTitle = bannerTitle;
        this.url = url;
        resolveSignInError();
    }

    // Gplus signin code..
    public void signInWithGplus(String title, String actionSpec, String id,
                                String bannerTitle, String url, String backurl, String backtitl, String catID, boolean bckSel) {
        this.title = title;
        this.actionSpec = actionSpec;
        this.id = id;
        this.bannerTitle = bannerTitle;
        this.url = url;
        this.backtoPrevurl = backurl;
        this.backtitle = backtitl;
        this.category_id = catID;
        this.backSelector = bckSel;
        resolveSignInError();
    }

    // Gplus signin code..
    public void signInWithGplus(String title, String actionSpec, String id,
                                String bannerTitle, String url, String pri, String quant, String nam) {
        this.title = title;
        this.actionSpec = actionSpec;
        this.id = id;
        this.bannerTitle = bannerTitle;
        this.url = url;
        this.price = pri;
        this.quantity = quant;
        this.name = nam;
        resolveSignInError();
    }

    //////old code of G+login..uncomment if need
   /* private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                Log.e("dkjf", "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);

                LoginWithGoogle(personName, email);

				*//*
				 * fr = new LoginFragment(); Bundle bundle = new Bundle();
				 * bundle.putString("title", "FromHomeScreen");
				 * bundle.putString("ID", "0"); FragmentManager fm =
				 * getFragmentManager(); FragmentTransaction fragmentTransaction
				 * = fm.beginTransaction();
				 * fragmentTransaction.replace(R.id.fragment_place, fr);
				 * fragmentTransaction.addToBackStack(null);
				 * fragmentTransaction.commit();
				 *//*
            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/////old code of G+login..uncomment if need

    private void LoginWithGoogle(String name, String email_id) {

        try {
			/*
			 * googleLoginURL = AppConstants.GOOGLE_LOGIN_URL +
			 * URLEncoder.encode(email_id + "&first_name=" + name, "UTF-8");
			 */
            name = name.replace(" ", "");
            googleLoginURL = AppConstants.GOOGLE_LOGIN_URL + email_id
                    + "&first_name=" + name + "&language=" + getLanguage();
            Log.e("Google LOgin", googleLoginURL);
        } catch (Exception ee) {
            Log.e("Exception", " " + ee.getMessage());
            ee.printStackTrace();
        }
        new DownloadJSONGoogle().execute();

    }

    private GoogleApiClient buildGoogleApiClient() {
        // When we build the GoogleApiClient we specify where connected and
        // connection failed callbacks should be returned, which Google APIs our
        // app uses and which OAuth 2.0 scopes our app requests.
        return new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
    }

    private void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            // ...
                        }
                    });
           /* Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();*/
        }
    }

    private class DownloadJSONGoogle extends AsyncTask<Void, Void, Void> {

        String first_Name;
        String last_Name;
        String cust_id;
        String email_id;
        // String phone_no;
        String cart_count_;
        String wishlist_count_;
        String success_msg;
        JSONObject jObj;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            Log.e("in preexecute", "DownloadJSONGoogle PreExecute");
            mProgressDialog = new ProgressDialog(MainActivity.this,
                    R.style.MyTheme);
            mProgressDialog.setCancelable(false);
            mProgressDialog
                    .setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            arraylist = new ArrayList<HashMap<String, String>>();

            String loginString = googleLoginURL;
            Log.e("GPlus loginURL", loginString);
            jsonobject = JSONfunctions.getJSONfromURL(loginString);
            Log.e("JsoObject", "" + jsonobject);
            Log.e("Json Object", jsonobject.toString());
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            mProgressDialog.dismiss();
            try {
                Log.e("Json Object", jsonobject.toString());
                // Locate the array name in JSON
                success_msg = jsonobject.getString("success_msg");
                jObj = jsonobject.getJSONObject("customer");
                // successmessage = jObj.getString("id");
                cust_id = jObj.getString("id");
                first_Name = jObj.getString("firstname");
                last_Name = jObj.getString("lastname");
                email_id = jObj.getString("email");
                cart_count_ = jObj.getString("cart");
                wishlist_count_ = jObj.getString("wishlist");

                // Log.e("The email added is", email);

                // customerID_ = cust_id;

            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();

                // successmessage = null;
            }

            if (cust_id == null) {
                //Fabric Google Login Analatycs
                Answers.getInstance().logLogin(new LoginEvent()
                        .putMethod("Dairam Google+ Login")
                        .putSuccess(false));
                Toast.makeText(getApplicationContext(), "Login failed",
                        Toast.LENGTH_SHORT).show();
            } else {
                //Fabric Google Login Analatycs
                Answers.getInstance().logLogin(new LoginEvent()
                        .putMethod("Dairam Google+ Login")
                        .putSuccess(true));

                SharedPreferences sharedpreferences = getApplicationContext()
                        .getSharedPreferences(MyPREFERENCES,
                                Context.MODE_PRIVATE);
                Editor editor = sharedpreferences.edit();
                editor.putString(customerid, cust_id);
                editor.putString(firstname, first_Name);
                editor.putString(lastname, last_Name);
                editor.putString(email_string, email_id);
                editor.putString(cart_string, cart_count_);
                editor.putString(wishlist_String, wishlist_count_);
                editor.putString("phone", "");

                editor.commit();
                Toast.makeText(getApplicationContext(), success_msg,
                        Toast.LENGTH_LONG).show();

				/*
				 * listenerHideView listenerHideView =
				 * (com.dairam.interfaces.listenerHideView) MainActivity.this;
				 * listenerHideView.cartListCountUpdation(cart_count_);
				 * listenerHideView.wishListCountUpdation(wishlist_count_);
				 */


                cartListCountUpdation(cart_count_);
                wishListCountUpdation(wishlist_count_);
                String customer_id = sharedpreferences.getString(customerid, "");
                if (id == null) {

                } else {


                    Log.e("The Title eeeeeeeeee", title);
                    if (actionSpec.equals(AppConstants.CART_IT)) {
                        Log.e("The Tittle  uuuuuuuuuu", title);
                        addToCartURL = addToCartURL + id + "&id=" + customer_id
                                + "&quantity=1&language=1";
                        new AddToCartTask().execute();
                    } else {
                        addToWishlistURl = addToWishlistURl + id + "&id="
                                + customer_id + "&quantity=1&language=1";
                        new RetrieveFeedTaskWishlist().execute();

                    }
					
					
					
/*
					if (title.equals("Promo")) {

						if (actionSpec.equals(AppConstants.CART_IT)) {

						} else {

						}
						fr = new PromoItemFragment();
						Bundle bundle = new Bundle();
						bundle.putString("title", bannerTitle);
						bundle.putString("referenceurl", url);
						fr.setArguments(bundle);
						FragmentManager fm = getFragmentManager();
						FragmentTransaction fragmentTransaction = fm
								.beginTransaction();
						fragmentTransaction.replace(R.id.fragment_place, fr);
						fragmentTransaction.addToBackStack(null);
						fragmentTransaction.commit();

					}*/
                }
                BackToPreviousPage();
            }
        }
    }

    public String getLanguage() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences(
                "lan", Context.MODE_PRIVATE);
        if (sp.getString("lan", "en").equals("en"))
            return "1";
        else
            return "2";
    }

    @Override
    public void closeDrawer() {
        SharedPreferences sp = MainActivity.this.getSharedPreferences("lan",
                Context.MODE_PRIVATE);
        String lan = sp.getString("lan", "en");

        switch (lan) {
            case "en":
                Log.e("MainActivity", "Close Drawer Method " + lan);
                try {
                    if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                        // Notice the Gravity.Right

                        mDrawerLayout.closeDrawer(Gravity.RIGHT | Gravity.BOTTOM);

                        bottombarLayout.setVisibility(View.VISIBLE);
                    } else {
                        mDrawerLayout.openDrawer(Gravity.RIGHT | Gravity.BOTTOM);
                    }
                } catch (Exception e) {
                    System.out.println("Here it caught an exception" + e);
                }

                break;

            default:
                try {
                    Log.e("MainActivity", "Close Drawer Method " + lan);
                    if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                        // Notice the Gravity.Right
                        mDrawerLayout.closeDrawer(Gravity.LEFT | Gravity.BOTTOM);

                    } else {
                        mDrawerLayout.openDrawer(Gravity.LEFT | Gravity.BOTTOM);
                    }
                } catch (Exception e) {
                    System.out.println("Here it caught an exception" + e);
                }

                break;
        }
    }

    // Add to wishlist items
    class RetrieveFeedTaskWishlist extends AsyncTask<Void, Void, Void> {

        String id;
        String wishlist_count;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(getApplicationContext(),
                    R.style.MyTheme);
            mProgressDialog.setCancelable(false);
            mProgressDialog
                    .setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Create an array
            ArrayList<HashMap<String, String>> arraylistcart = new ArrayList<HashMap<String, String>>();

            // Retrieve JSON Objects from the given URL address
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
                                    getApplicationContext(),
                                    getResources().getString(
                                            R.string.error_in_additem),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            if (getLanguage().equals("1"))
                                Toast.makeText(getApplicationContext(),
                                        "Item added to the wishlist",
                                        Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getApplicationContext(),
                                        "تم إضافة المنتج إلى المفضلة ",
                                        Toast.LENGTH_SHORT).show();

                            Editor editor = sharedpreferences.edit();
                            editor.putString(wishlist_String, wishlist_count);
                            editor.commit();
                            wishListCountUpdation(wishlist_count);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                R.string.no_response, Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            R.string.no_response, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            BackToPreviousPage();
        }
    }

    class AddToCartTask extends AsyncTask<Void, Void, Void> {
        String id;
        String cart_Count;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Create a progressdialog
            mProgressDialog = new ProgressDialog(getApplicationContext(),
                    R.style.MyTheme);
            mProgressDialog.setCancelable(false);
            mProgressDialog
                    .setProgressStyle(android.R.style.Widget_ProgressBar_Small);
/*			mProgressDialog.show();*/
        }

        @Override
        protected Void doInBackground(Void... params) {

            // Create an array
            ArrayList<HashMap<String, String>> arraylistcart = new ArrayList<HashMap<String, String>>();
            // Retrieve JSON Objects from the given URL address
            jsonobject = JSONfunctions.getJSONfromURL(addToCartURL);
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
		/*	mProgressDialog.dismiss();*/
            addToCartURL = "http://dairam.com/index.php?route=api/cart/get&cart=";
            try {
                if (jsonobject != null) {
                    // Locate the array name in JSON
                    jsonobject = jsonobject.getJSONObject("customer");
                    id = jsonobject.getString(AppConstants.CART_ID);
                    cart_Count = jsonobject.getString(AppConstants.CART_COUNT);
                    if (id == null) {
                        Toast.makeText(
                                getApplicationContext(),
                                getResources().getString(
                                        R.string.error_in_additem),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        SharedPreferences sp = getApplicationContext()
                                .getSharedPreferences("lan",
                                        Context.MODE_PRIVATE);

                        if (sp.getString("lan", "en").equals("en"))
                            Toast.makeText(getApplicationContext(),
                                    "Item added to the cart",
                                    Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getApplicationContext(),
                                    "تم إضافة المنتج إلى السلة ",
                                    Toast.LENGTH_SHORT).show();

                        Editor editor = sharedpreferences.edit();
                        editor.putString(cart_string, cart_Count);
                        editor.apply();
                        cartListCountUpdation(cart_Count);

                        BackToPreviousPage();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            R.string.no_response, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Log.e("TAG", e.getMessage());
                e.printStackTrace();
            }

        }
    }

    private void BackToPreviousPage() {

        Bundle bundle;
        FragmentManager fm;
        FragmentTransaction fragmentTransaction;

        //Log.e("Entered here ... dance", title);
        if (title != null) {
            switch (title) {
                case "brandlist":
                    fr = new BrandListingSingleFragment();
                    bundle = new Bundle();
                    bundle.putString("title", bannerTitle);
                    bundle.putString("referenceurl", url);

                    fr.setArguments(bundle);
                    fm = getFragmentManager();
                    fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_place, fr);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;

                case "Promolist":
                    fr = new PromoItemFragment();
                    bundle = new Bundle();
                    bundle.putString("title", title);
                    bundle.putString("referenceurl", url);
                    fr.setArguments(bundle);
                    fm = getFragmentManager();
                    fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_place, fr);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;

                case "Promodetails":
                    fr = new PromoItemSingleProductDetailsFragment();
                    bundle = new Bundle();
                    bundle.putString("productid", id);
                    bundle.putString("productname", name);
                    bundle.putString("Price", price);
                    bundle.putString(AppConstants.HOME_SUB_OUT_OF_STOCK_FLAG, quantity);
                    bundle.putString("BackToPreviousUrl", url);
                    bundle.putString("title", bannerTitle);
                    fr.setArguments(bundle);
                    fm = getFragmentManager();
                    fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_place, fr);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;

                case "salelisting":
                    fr = new SalesItemDisplayFragment();
                    bundle = new Bundle();
                    bundle.putString("title", title);
                    bundle.putString("referenceurl", url);
                    fr.setArguments(bundle);
                    fm = getFragmentManager();
                    fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_place, fr);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;

                case "saledetails":
                    fr = new SaleProductsDetails();
                    bundle = new Bundle();
                    bundle.putString("productid", id);
                    bundle.putString("productname", name);
                    bundle.putString("Price", price);
                    bundle.putString(AppConstants.HOME_SUB_OUT_OF_STOCK_FLAG, quantity);
                    bundle.putString("referenceURl", url);
                    bundle.putString("BannerTitle", bannerTitle);
                    fr.setArguments(bundle);

                    fm = getFragmentManager();
                    fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_place, fr);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;


                case "catPro":
                    Log.e("Here I am ", "msg");
                    fr = new CategoryProductsFragment();
                    bundle = new Bundle();
                    bundle.putString("title", bannerTitle);
                    bundle.putString("referenceurl", url);
                    bundle.putString("firstlevel", backtoPrevurl);
                    bundle.putString("firstleveltitle", backtitle);
                    bundle.putString("categoryid", category_id);
                    bundle.putBoolean(AppConstants.BACKSELECTOR, backSelector);
                    fr.setArguments(bundle);
                    fm = getFragmentManager();
                    fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_place, fr);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;

                default:
                    fr = new FragmentCategories();
                    fm = getFragmentManager();
                    fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_place, fr);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
            }
        } else {
            fr = new FragmentCategories();
            fm = getFragmentManager();
            fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
    private void checkMessage(Intent intent)
    {
        if (null != intent)
        {
            if (intent.hasExtra(PushManager.PUSH_RECEIVE_EVENT))
            {
                showMessage("push message is " + intent.getExtras().getString(PushManager.PUSH_RECEIVE_EVENT));
            }
            else if (intent.hasExtra(PushManager.REGISTER_EVENT))
            {
                //showMessage("register");
            }
            else if (intent.hasExtra(PushManager.UNREGISTER_EVENT))
            {
                showMessage("unregister");
            }
            else if (intent.hasExtra(PushManager.REGISTER_ERROR_EVENT))
            {
                showMessage("register error");
            }
            else if (intent.hasExtra(PushManager.UNREGISTER_ERROR_EVENT))
            {
                showMessage("unregister error");
            }

            resetIntentValues();
        }
    }

    /**
     * Will check main Activity intent and if it contains any PushWoosh data, will clear it
     */
    private void resetIntentValues()
    {
        Intent mainAppIntent = getIntent();

        if (mainAppIntent.hasExtra(PushManager.PUSH_RECEIVE_EVENT))
        {
            mainAppIntent.removeExtra(PushManager.PUSH_RECEIVE_EVENT);
        }
        else if (mainAppIntent.hasExtra(PushManager.REGISTER_EVENT))
        {
            mainAppIntent.removeExtra(PushManager.REGISTER_EVENT);
        }
        else if (mainAppIntent.hasExtra(PushManager.UNREGISTER_EVENT))
        {
            mainAppIntent.removeExtra(PushManager.UNREGISTER_EVENT);
        }
        else if (mainAppIntent.hasExtra(PushManager.REGISTER_ERROR_EVENT))
        {
            mainAppIntent.removeExtra(PushManager.REGISTER_ERROR_EVENT);
        }
        else if (mainAppIntent.hasExtra(PushManager.UNREGISTER_ERROR_EVENT))
        {
            mainAppIntent.removeExtra(PushManager.UNREGISTER_ERROR_EVENT);
        }

        setIntent(mainAppIntent);
    }

    private void showMessage(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    //Registration receiver
    BroadcastReceiver mBroadcastReceiver = new BaseRegistrationReceiver()
    {
        @Override
        public void onRegisterActionReceive(Context context, Intent intent)
        {
            checkMessage(intent);
        }
    };

    //Push message receiver
    private BroadcastReceiver mReceiver = new BasePushMessageReceiver()
    {
        @Override
        protected void onMessageReceive(Intent intent)
        {
            //JSON_DATA_KEY contains JSON payload of push notification.
            showMessage("push message is " + intent.getExtras().getString(JSON_DATA_KEY));
        }
    };

    //Registration of the receivers
    public void registerReceivers()
    {
        IntentFilter intentFilter = new IntentFilter(getPackageName() + ".action.PUSH_MESSAGE_RECEIVE");

        registerReceiver(mReceiver, intentFilter, getPackageName() +".permission.C2D_MESSAGE", null);

        registerReceiver(mBroadcastReceiver, new IntentFilter(getPackageName() + "." + PushManager.REGISTER_BROAD_CAST_ACTION));
    }

    public void unregisterReceivers()
    {
        //Unregister receivers on pause
        try
        {
            unregisterReceiver(mReceiver);
        }
        catch (Exception e)
        {
            // pass.
        }

        try
        {
            unregisterReceiver(mBroadcastReceiver);
        }
        catch (Exception e)
        {
            //pass through
        }
    }
    @Override
    public void onResume()
    {
        super.onResume();

        //Re-register receivers on resume
        registerReceivers();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        //Unregister receivers on pause
        unregisterReceivers();
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);

        checkMessage(intent);
    }

}
