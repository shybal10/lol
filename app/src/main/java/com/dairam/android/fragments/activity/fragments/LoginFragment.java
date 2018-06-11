package com.dairam.android.fragments.activity.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.LoginEvent;
import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.MainActivity;
import com.dairam.android.fragments.activity.adapter.ListViewAdapter;
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.interfaces.listenerHideView;
import com.dairam.android.fragments.activity.parserfiles.JSONfunctions;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.tasks.Task;

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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginFragment extends Fragment {
    /*
     * Shared preference variables for login data
     */
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String customerid = "id";
    public static final String firstname = "firstname";
    public static final String lastname = "lastname";
    public static final String email_string = "email";
    public static final String phone_string = "phone";
    public static final String cart_string = "cart";
    public static final String wishlist_String = "wishlist";
    private static final int RC_SIGN_IN = 0;
    // Logcat tag
    private static final String TAG1 = "MainActivity";
    // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;
    public static int REQ_CODE = 1009;
    private static MainActivity mainActivity;
    String TAG = "LoginFragment";
    String forgetPasswordUrl;
    JSONObject jsonobject;
    JSONArray jsonarray;
    ListView listview;
    ListViewAdapter adapter;
    ProgressDialog mProgressDialog;
    ArrayList<HashMap<String, String>> arraylist;
    Context context;
    String facebookEmail, facebookName, facebookUserid, facebookLocation;
    String mainurlLoging;
    Activity simpleActivity;
    String successmessage;
    EditText emailEditText;
    EditText passwordEditText;
    Button loginButton, login_button_fb, btn_sign_in_google;
    Button registerButton;
    String usernameString;
    String passwordString;
    Boolean isValidEmail = false;
    String urlString;
    String email, phone, firstName, lastName, location;
    Fragment fr;
    String googlePlusLoginGmailid, googlePlusLoginname;
    String dummyGooglePlusName, DummyGooglePlusEmail;
    String addToCartURL = "http://dairam.com/index.php?route=api/cart/get&cart=";
    String addToWishlistURl = "http://dairam.com/index.php?route=api/wishlist/get&wishlist=";
    SharedPreferences sharedpreferences;
    String FILENAME = "AndroidSSO_data";
    String id, url, title = null, bannerTitle, actionSpec;
    String backtoPrevurl, backtitle, category_id, referenceURl;
    boolean backSelector;
    String productName, price, Quantity;
    TextView forgetPassword;
    TextView backToHomeTextView;
    Button backTohomeButton;
    Button sign_in_Button;
    boolean login_Status = false;
    String customer_id;
    //LoginButton facebookLoginButton;
    CallbackManager callbackManager;
    GoogleApiClient googleApiClient;
    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    GoogleSignInClient mGoogleSignInClient;
    SignInButton google_sign_in_button;
    private ConnectionResult mConnectionResult;
    private String customerID_;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;
    // Your Face book APP ID
    //private static String APP_ID = "1474562186130845";
    private LoginButton loginButtonFB;
    private SharedPreferences mPrefs;
    private String googleEmail, googleName, googleId;

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

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences sp = getActivity().getSharedPreferences("lan",
                Context.MODE_PRIVATE);
        String lan = sp.getString("lan", "en");
        setLanguage(lan);
        View v = inflater.inflate(R.layout.activity_login, container, false);
        context = getActivity();

        jsonobject = new JSONObject();
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences sp1 = getActivity().getSharedPreferences("tag",
                Context.MODE_PRIVATE);
        String p = "fragmentlogin";
        Editor editor = sp1.edit();
        editor.putString("act", p);
        editor.commit();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        googleApiClient = new GoogleApiClient.Builder(getActivity())

                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // mGoogleApiClient = buildGoogleApiClient();
        //sign_in_Button = (Button) v.findViewById(R.id.btn_sign_in);




        try {
            title = getArguments().getString("Page");
            switch (title) {

                case "Promolist":
                    id = getArguments().getString("ID");
                    url = getArguments().getString("urls");
                    bannerTitle = getArguments().getString("Bannertitle");
                    actionSpec = getArguments().getString("itemAction");
                    break;

                case "Promodetails":
                    id = getArguments().getString("ID");
                    url = getArguments().getString("urls");
                    bannerTitle = getArguments().getString("Bannertitle");
                    actionSpec = getArguments().getString("itemAction");
                    productName = getArguments().getString("productname");
                    price = getArguments().getString("Price");
                    Quantity = getArguments().getString(
                            AppConstants.HOME_SUB_OUT_OF_STOCK_FLAG);
                    break;

                case "salelisting":
                    id = getArguments().getString("ID");
                    url = getArguments().getString("urls");
                    bannerTitle = getArguments().getString("Bannertitle");
                    actionSpec = getArguments().getString("itemAction");
                    break;

                case "saledetails":
                    id = getArguments().getString("ID");
                    url = getArguments().getString("urls");
                    bannerTitle = getArguments().getString("Bannertitle");
                    actionSpec = getArguments().getString("itemAction");
                    productName = getArguments().getString("productname");
                    price = getArguments().getString("Price");
                    Quantity = getArguments().getString(
                            AppConstants.HOME_SUB_OUT_OF_STOCK_FLAG);
                    break;

                case "catPro":
                    id = getArguments().getString("ID");
                    url = getArguments().getString("urls");
                    bannerTitle = getArguments().getString("Bannertitle");
                    actionSpec = getArguments().getString("itemAction");
                    backtoPrevurl = getArguments().getString("firstlevel");
                    backtitle = getArguments().getString("firstleveltitle");
                    category_id = getArguments().getString("category_id");
                    backSelector = getArguments().getBoolean(
                            AppConstants.BACKSELECTOR);
                    break;

                case "catDetails":
                    id = getArguments().getString("ID");
                    url = getArguments().getString("urls");
                    bannerTitle = getArguments().getString("Bannertitle");
                    actionSpec = getArguments().getString("itemAction");
                    productName = getArguments().getString("productname");
                    price = getArguments().getString("Price");
                    Quantity = getArguments().getString(
                            AppConstants.HOME_SUB_OUT_OF_STOCK_FLAG);

                    bannerTitle = getArguments().getString("title");
                    referenceURl = getArguments().getString("referenceurl");
                    backtoPrevurl = getArguments().getString("firstlevel");
                    backtitle = getArguments().getString("firstleveltitle");
                    category_id = getArguments().getString("category_id");
                    backSelector = getArguments().getBoolean(
                            AppConstants.BACKSELECTOR);

                    break;
                default:
                    id = getArguments().getString("ID");
                    url = getArguments().getString("urls");
                    bannerTitle = getArguments().getString("Bannertitle");
                    actionSpec = getArguments().getString("itemAction");
                    break;
            }
        } catch (Exception e) {

        }

        login_button_fb =  v.findViewById(R.id.login_button_fb);
        btn_sign_in_google = v.findViewById(R.id.btn_sign_in_google);
        //real buttons
        google_sign_in_button = v.findViewById(R.id.google_sign_in_button);
        loginButtonFB = v.findViewById(R.id.login_button);

        login_button_fb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButtonFB.performClick();

            }
        });

        btn_sign_in_google.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
               // google_sign_in_button.performClick();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });


        loginButton = (Button) v.findViewById(R.id.buttonlogin);
        registerButton = (Button) v.findViewById(R.id.buttonregister);

        emailEditText = (EditText) v.findViewById(R.id.editTextemail);
        passwordEditText = (EditText) v.findViewById(R.id.editTextpassword);

        backTohomeButton = (Button) v.findViewById(R.id.buttonbackhomelogin);
        backToHomeTextView = (TextView) v
                .findViewById(R.id.textviewheaderlogin);

        Typeface fontFace = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/LibreBaskerville-Regular.otf");
        backToHomeTextView.setTypeface(fontFace);

        backTohomeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BackToHome();
            }
        });

        backToHomeTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BackToHome();
            }
        });

        forgetPassword = (TextView) v.findViewById(R.id.textviewforget);
        forgetPassword.setTextColor(getResources().getColor(
                android.R.color.black));
        forgetPassword.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                forgetPassword.setTextColor(getResources().getColor(
                        android.R.color.holo_red_dark));

                String emailString = emailEditText.getText().toString();
                Boolean isValid = emailValidator(emailString);

                if (isValid == true) {
                    forgetPasswordUrl = AppConstants.FORGET_PASSWORD_URL
                            + emailString + "&language=" + getLanguage();

                    new DownloadJSONForgetpassword().execute();

                } else {
                    Toast.makeText(
                            getActivity(),
                            getResources()
                                    .getString(R.string.enter_valid_uname),
                            Toast.LENGTH_SHORT).show();
                }
                forgetPassword.setTextColor(Color.BLACK);

            }
        });

        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                hideKeyboard();
                usernameString = emailEditText.getText().toString();
                passwordString = passwordEditText.getText().toString();
                if ((usernameString.length() <= 0)
                        || (passwordString.length() <= 0)) {
                    Toast.makeText(getActivity(),
                            getResources().getString(R.string.fill_all_fields),
                            Toast.LENGTH_SHORT).show();
                } else {
                    new DownloadJSON().execute();
                }
            }
        });

        try {
            getProjectKeyHash();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }






        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                fr = new RegisterFragment();
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
        loginButtonFB.setReadPermissions("email");

        loginButtonFB.setFragment(this);
        callbackManager = CallbackManager.Factory.create();

        loginButtonFB.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e(TAG, "FBLogin .");

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object,
                                                    GraphResponse response) {
                                // Application code
                                Log.v("QWLoginActivity",
                                        response.toString());

                                facebookName = object.optString("first_name");
                                facebookEmail = object.optString("email");
                                facebookUserid = object.optString("id");

                                new DownloadJSONFacebook().execute();


                            }
                        });
                Bundle parameters = new Bundle();
                parameters
                        .putString("fields",
                                "id,first_name,last_name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
                LoginManager.getInstance().logOut();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity(), "Facebook login Cancel", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getActivity(), "Facebook login Error", Toast.LENGTH_LONG).show();

            }
        });


        google_sign_in_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });


        return v;
    }

    public void onStart() {
        super.onStart();

    }


    public void onStop() {
        super.onStop();
        /*
         * if (mGoogleApiClient.isConnected()) { mGoogleApiClient.disconnect();
		 * }
		 */
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) this.getActivity();


    }


    /**
     * Method to resolve any sign in errors
     */
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(getActivity(),
                        RC_SIGN_IN);
            } catch (SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }


    /**
     * Fetching user's information name, email, profile pic
     **/
    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                dummyGooglePlusName = personName;
                DummyGooglePlusEmail = email;

				/*
                 * txtName.setText(personName); txtEmail.setText(email);
				 */

                // by default the profile url gives 50x50 px image only
                // we can replace the value with whatever dimension we want by
                // replacing sz=X
                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + PROFILE_PIC_SIZE;

                // new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);

            } else {
                Toast.makeText(getActivity(), "Person information is null",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sign-in into google
     */
    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }

    /**
     * Revoking access from google
     */
    private void revokeGplusAccess() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.e(TAG, "User access revoked!");
                            mGoogleApiClient.connect();
                        }
                    });
        }
    }

    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        hide_keyboard(activity);
        simpleActivity = activity;
        listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) activity;
        listenerHideView.reversePromoItemHideTopbar();
        listenerHideView.hide();
        listenerHideView.dairamBar();
        listenerHideView.showBottomBar();

    }

    private String getLanguage() {
        SharedPreferences sp = getActivity().getSharedPreferences("lan",
                Context.MODE_PRIVATE);

        if (sp.getString("lan", "en").equals("en"))
            return "1";
        else
            return "2";
    }

    private void setLanguage(String lang) {

        String languageToLoad = lang;
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getResources().updateConfiguration(config, null);

    }

    public void BackToHome() {
        if (sharedpreferences.contains(customerid)) {
            customer_id = sharedpreferences.getString(customerid, "");
        }

        if (title != null) {
            if (login_Status == true) {
                if (actionSpec.equals(AppConstants.CART_IT)) {
                    Log.e("The Tittle", title);
                    addToCartURL = addToCartURL + id + "&id=" + customer_id
                            + "&quantity=1&language=1";
                    new AddToCartTask().execute();
                } else {
                    addToWishlistURl = addToWishlistURl + id + "&id="
                            + customer_id + "&quantity=1&language=1";
                    new RetrieveFeedTaskWishlist().execute();
                }

            } else {

                BackToPreviousPage();
            }

			/*
             * fr = new BrandListingSingleFragment(); Bundle bundle = new
			 * Bundle(); bundle.putString("title", bannerTitle);
			 * bundle.putString("referenceurl", url);
			 *
			 * fr.setArguments(bundle); FragmentManager fm = ((Activity)
			 * context).getFragmentManager(); FragmentTransaction
			 * fragmentTransaction = fm.beginTransaction();
			 * fragmentTransaction.replace(R.id.fragment_place, fr);
			 * fragmentTransaction.addToBackStack(null);
			 * fragmentTransaction.commit();
			 */

        } else {

            fr = new FragmentCategories();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    public void getProjectKeyHash() throws NoSuchAlgorithmException {
        PackageInfo info = null;
        try {
            info = getActivity().getPackageManager().getPackageInfo(
                    "com.example.fbapptest", PackageManager.GET_SIGNATURES);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        try {
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:",
                        "KeyHash: "
                                + Base64.encodeToString(md.digest(),
                                Base64.DEFAULT));
            }
        } catch (Exception e) {
            System.out.println("skfj");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            googleName = account.getDisplayName();
            googleEmail = account.getEmail();
            googleId = account.getId();

            new DownloadJSONGoogle().execute();


            // Signed in successfully, show authenticated UI.
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }


    // Login with google plus

    @Override
    public void onPause() {
        super.onPause();
        googleApiClient.disconnect();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(emailEditText.getWindowToken(), 0);

    }

    public void setupUI(View view, boolean isHeading) {
        Typeface myTypeface = Typeface.createFromAsset(getActivity()
                .getAssets(), "fonts/HELVETICANEUELTARABIC-ROMAN.TTF");
        TextView myTextView = (TextView) view.findViewById(R.id.textView1);
        myTextView.setTypeface(myTypeface);
        forgetPassword.setTypeface(myTypeface);
        emailEditText.setTypeface(myTypeface);
        passwordEditText.setTypeface(myTypeface);
        backToHomeTextView.setTypeface(myTypeface);
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
                        BackToHome();
                        return true;
                    }
                }
                return false;
            }
        });
        super.onActivityCreated(savedInstanceState);
    }

    private void BackToPreviousPage() {

        Bundle bundle;
        FragmentManager fm;
        FragmentTransaction fragmentTransaction;
        switch (title) {
            case "brandlist":
                fr = new BrandListingSingleFragment();
                bundle = new Bundle();
                bundle.putString("title", bannerTitle);
                bundle.putString("referenceurl", url);
                fr.setArguments(bundle);
                fm = ((Activity) context).getFragmentManager();
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
                fm = ((Activity) context).getFragmentManager();
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, fr);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

            case "Promodetails":
                fr = new PromoItemSingleProductDetailsFragment();
                bundle = new Bundle();
                bundle.putString("productid", id);
                bundle.putString("productname", productName);
                bundle.putString("Price", price);
                bundle.putString(AppConstants.HOME_SUB_OUT_OF_STOCK_FLAG, Quantity);
                bundle.putString("BackToPreviousUrl", url);
                bundle.putString("title", bannerTitle);
                fr.setArguments(bundle);
                fm = ((Activity) context).getFragmentManager();
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
                fm = ((Activity) context).getFragmentManager();
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, fr);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

            case "saledetails":
                fr = new SaleProductsDetails();
                bundle = new Bundle();
                bundle.putString("productid", id);
                bundle.putString("productname", productName);
                bundle.putString("Price", price);
                bundle.putString(AppConstants.HOME_SUB_OUT_OF_STOCK_FLAG, Quantity);
                bundle.putString("referenceURl", url);
                bundle.putString("BannerTitle", bannerTitle);
                fr.setArguments(bundle);

                fm = ((Activity) context).getFragmentManager();
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, fr);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

            case "catPro":
                fr = new CategoryProductsFragment();
                bundle = new Bundle();
                bundle.putString("title", bannerTitle);
                bundle.putString("referenceurl", url);
                bundle.putString("firstlevel", backtoPrevurl);
                bundle.putString("firstleveltitle", backtitle);
                bundle.putString("categoryid", category_id);
                bundle.putBoolean(AppConstants.BACKSELECTOR, backSelector);
                fr.setArguments(bundle);
                fm = ((Activity) context).getFragmentManager();
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, fr);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

            case "catDetails":
                fr = new ProductDetailsFragment();
                bundle = new Bundle();
                bundle.putString("productid", id);
                bundle.putString("productname", productName);
                bundle.putString("Price", price);
                bundle.putString(AppConstants.HOME_SUB_OUT_OF_STOCK_FLAG, Quantity);
                bundle.putString("title", bannerTitle);
                bundle.putString("referenceurl", referenceURl);
                bundle.putString("firstlevel", backtoPrevurl);
                bundle.putString("firstleveltitle", backtitle);
                bundle.putString("category_id", category_id);
                bundle.putBoolean(AppConstants.BACKSELECTOR, backSelector);
                fr.setArguments(bundle);
                fm = ((Activity) context).getFragmentManager();
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

    }

    // Login to the application
    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        String first_Name;
        String last_Name;
        String cust_id;
        String email_id;
        String phone_no;
        String cart_count_;
        String wishlist_count_;

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

            String loginString = AppConstants.LOGIN_URL + usernameString
                    + "&password=" + passwordString + "&language="
                    + getLanguage() + "&currency=KWD";
            // Retrieve JSON Objects from the given URL address
            Log.e("Login String", loginString);
            jsonobject = JSONfunctions.getJSONfromURL(loginString);

            // Log.e("Login Response", jsonobject.toString());

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            mProgressDialog.dismiss();
            try {
                if (jsonobject != null) {
                    if (jsonobject.getBoolean("success")) {

                        login_Status = true;
                        //Fabric Login Analatycs.............
                        Answers.getInstance().logLogin(new LoginEvent()
                                .putMethod("Dairam App Login")
                                .putSuccess(true));
                        successmessage = jsonobject.getString("success_msg");

                        jsonobject = jsonobject.getJSONObject("customer");

                        cust_id = jsonobject.getString("id");
                        first_Name = jsonobject.getString("firstname");
                        last_Name = jsonobject.getString("lastname");
                        email_id = jsonobject.getString("email");
                        phone_no = jsonobject.getString("phone");
                        cart_count_ = jsonobject.getString("cart");
                        wishlist_count_ = jsonobject.getString("wishlist");

                        customerID_ = cust_id;

                        if (cust_id == null) {
                            Toast.makeText(
                                    getActivity(),
                                    getResources().getString(
                                            R.string.login_failed),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Editor editor = sharedpreferences.edit();
                            editor.putString(customerid, cust_id);
                            editor.putString(firstname, first_Name);
                            editor.putString(lastname, last_Name);
                            editor.putString(email_string, email_id);
                            editor.putString(cart_string, cart_count_);
                            editor.putString(wishlist_String, wishlist_count_);
                            editor.putString(phone_string, phone_no);
                            editor.commit();

                            listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) simpleActivity;
                            listenerHideView.cartListCountUpdation(cart_count_);
                            listenerHideView
                                    .wishListCountUpdation(wishlist_count_);

                            Toast.makeText(getActivity(), successmessage,
                                    Toast.LENGTH_LONG).show();

                            BackToHome();

                            if (id == null) {


                                Fragment fr = new FragmentCategories();
                                FragmentManager fm = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fm
                                        .beginTransaction();
                                fragmentTransaction.replace(
                                        R.id.fragment_place, fr);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            } else {
                                if (title.equals("Promo")) {
                                    fr = new PromoItemFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("title", bannerTitle);
                                    bundle.putString("referenceurl", url);
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
                            }
                        }

                    } else {
                        JSONArray jsonArrayError = jsonobject
                                .getJSONArray("error");
                        // jsonobject = jsonArrayError.getJSONObject(0);
                        Toast.makeText(getActivity(),
                                jsonArrayError.getString(0), Toast.LENGTH_SHORT)
                                .show();
                        //Fabric Login Analatycs.............
                        Answers.getInstance().logLogin(new LoginEvent()
                                .putMethod("Dairam App Login")
                                .putSuccess(false));
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.no_response,
                            Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();

                successmessage = null;
            }

        }
    }

    private class DownloadJSONFacebook extends AsyncTask<Void, Void, Void> {

        String first_Name;
        String last_Name;
        String cust_id;
        String email_id;
        // String phone_no;
        String cart_count_;
        String wishlist_count_;

        String success_msg;

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
            facebookName = facebookName.replace(" ", "");
            String loginString = "http://dairam.com/index.php?route=api/fblogin/get&email="
                    + facebookEmail
                    + "&first_name="
                    + facebookName
                    + "&id="
                    + facebookUserid
                    + "&location="
                    + null
                    + "&language="
                    + getLanguage();
            Log.e("Facebook URL", loginString);

            // Retrieve JSON Objects from the given URL address
            jsonobject = JSONfunctions.getJSONfromURL(loginString);

            try {
                // Locate the array name in JSON
                success_msg = jsonobject.getString("success_msg");
                jsonobject = jsonobject.getJSONObject("customer");
                successmessage = jsonobject.getString("id");
                cust_id = jsonobject.getString("id");
                first_Name = jsonobject.getString("firstname");
                last_Name = jsonobject.getString("lastname");
                email = jsonobject.getString("email");
                cart_count_ = jsonobject.getString("cart");
                wishlist_count_ = jsonobject.getString("wishlist");

                customerID_ = cust_id;

            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();

                successmessage = null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            mProgressDialog.dismiss();
            if (successmessage == null) {
                //Fabric Login Analatycs.............
                Answers.getInstance().logLogin(new LoginEvent()
                        .putMethod("Dairam Facebook Login")
                        .putSuccess(false));
                Toast.makeText(getActivity(),
                        getResources().getString(R.string.login_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Editor editor = sharedpreferences.edit();
                editor.putString(customerid, cust_id);
                editor.putString(firstname, first_Name);
                editor.putString(lastname, last_Name);
                editor.putString(email_string, email_id);
                editor.putString(cart_string, cart_count_);
                editor.putString(wishlist_String, wishlist_count_);
                editor.putString(phone_string, "");
                editor.commit();
                login_Status = true;
                //Fabric Login Analatycs.............
                Answers.getInstance().logLogin(new LoginEvent()
                        .putMethod("Dairam Facebook Login")
                        .putSuccess(true));
                Toast.makeText(getActivity(), success_msg, Toast.LENGTH_SHORT)
                        .show();

                listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) simpleActivity;
                listenerHideView.cartListCountUpdation(cart_count_);
                listenerHideView.wishListCountUpdation(wishlist_count_);

				/*
				 * fr = new FragmentOne(); FragmentManager fm =
				 * getFragmentManager(); FragmentTransaction fragmentTransaction
				 * = fm .beginTransaction();
				 * fragmentTransaction.replace(R.id.fragment_place, fr);
				 * fragmentTransaction.addToBackStack(null);
				 * fragmentTransaction.commit();
				 */

                BackToHome();

            }
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
            String loginString = AppConstants.GOOGLE_LOGIN_URL
                    + googleEmail;

            // Retrieve JSON Objects from the given URL address
            jsonobject = JSONfunctions.getJSONfromURL(loginString);

            try {
                // Locate the array name in JSON
                success_msg = jsonobject.getString("success_msg");
                jsonobject = jsonobject.getJSONObject("customer");
                successmessage = jsonobject.getString("id");
                cust_id = jsonobject.getString("id");
                first_Name = jsonobject.getString("firstname");
                last_Name = jsonobject.getString("lastname");
                email = jsonobject.getString("email");
                cart_count_ = jsonobject.getString("cart");
                wishlist_count_ = jsonobject.getString("wishlist");



                customerID_ = cust_id;

            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();

                successmessage = null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            mProgressDialog.dismiss();
            if (successmessage == null) {
                //Fabric Login Analatycs.............
                Answers.getInstance().logLogin(new LoginEvent()
                        .putMethod("Dairam Google Login")
                        .putSuccess(false));
                Toast.makeText(getActivity(),
                        getResources().getString(R.string.login_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Editor editor = sharedpreferences.edit();
                editor.putString(customerid, cust_id);
                editor.putString(firstname, first_Name);
                editor.putString(lastname, last_Name);
                editor.putString(email_string, email);
                editor.putString(cart_string, cart_count_);
                editor.putString(wishlist_String, wishlist_count_);
                editor.putString(phone_string, "");
                editor.commit();
                login_Status = true;
                //Fabric Login Analatycs.............
                Answers.getInstance().logLogin(new LoginEvent()
                        .putMethod("Dairam Google  Login")
                        .putSuccess(true));
                Toast.makeText(getActivity(), success_msg, Toast.LENGTH_SHORT)
                        .show();

                listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) simpleActivity;
                listenerHideView.cartListCountUpdation(cart_count_);
                listenerHideView.wishListCountUpdation(wishlist_count_);


                BackToHome();

            }
        }
    }


    private class DownloadJSONForgetpassword extends
            AsyncTask<Void, Void, Void> {

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

            HttpParams params1 = new BasicHttpParams();
            HttpProtocolParams.setVersion(params1, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params1, "UTF-8");
            params1.setBooleanParameter("http.protocol.expect-continue", false);
            HttpClient httpclient = new DefaultHttpClient(params1);

            Log.e(TAG, "forget Pswd: " + forgetPasswordUrl);
            HttpPost httppost = new HttpPost(forgetPasswordUrl);
            // Log.e("Main url", forgetPasswordUrl);
            try {
                HttpResponse http_response = httpclient.execute(httppost);

                HttpEntity entity = http_response.getEntity();
                String jsonText = EntityUtils.toString(entity, HTTP.UTF_8);
                Log.i("Responsezz", jsonText);
                jsonobject = new JSONObject(jsonText);

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
                        Toast.makeText(getActivity(),
                                jsonobject.getString("success_msg"),
                                Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(getActivity(), R.string.no_response,
                            Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
            }

        }
    }

    class AddToCartTask extends AsyncTask<Void, Void, Void> {
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
            ArrayList<HashMap<String, String>> arraylistcart = new ArrayList<HashMap<String, String>>();

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
                                    "ØªÙ… Ø¥Ø¶Ø§ÙØ© Ø§Ù„Ù…Ù†ØªØ¬ Ø¥Ù„Ù‰ Ø§Ù„Ø³Ù„Ø©",
                                    Toast.LENGTH_SHORT).show();

                        Editor editor = sharedpreferences.edit();
                        editor.putString(cart_string, cart_Count);
                        editor.apply();

                        listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) simpleActivity;
                        listenerHideView.cartListCountUpdation(cart_Count);

                        BackToPreviousPage();

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
                                Toast.makeText(
                                        getActivity(),
                                        "Ã˜ÂªÃ™â€¦ Ã˜Â¥Ã˜Â¶Ã˜Â§Ã™ï¿½Ã˜Â© Ã˜Â§Ã™â€žÃ™â€¦Ã™â€ Ã˜ÂªÃ˜Â¬ Ã˜Â¥Ã™â€žÃ™â€° Ã˜Â§Ã™â€žÃ™â€¦Ã™ï¿½Ã˜Â¶Ã™â€žÃ˜Â© ",
                                        Toast.LENGTH_SHORT).show();

                            Editor editor = sharedpreferences.edit();
                            editor.putString(wishlist_String, wishlist_count);
                            editor.commit();
                            listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) simpleActivity;
                            listenerHideView
                                    .wishListCountUpdation(wishlist_count);
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
                e.printStackTrace();
            }

            BackToPreviousPage();
        }
    }






}
