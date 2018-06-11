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
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.interfaces.listenerHideView;
import com.dairam.android.fragments.activity.parserfiles.JSONfunctions;
import com.dairam.android.fragments.activity.utillities.Util;

import org.json.JSONObject;

public class PAYPALPaymentFragment extends Fragment {

    Activity sample;
    WebView webViewPAYPAL;
    TextView backTextView;
    Button backButton;
    String PAYPALGatewayURL;
    ProgressDialog mProgressDialog;

    SharedPreferences sharedPrefs;
    public static final String MyPREFERENCES = "MyPrefs";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_knet_payment, container,
                false);

        sharedPrefs = getActivity().getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);

        Bundle b = getArguments();
        PAYPALGatewayURL = b.getString("gatewayUrl");

        SharedPreferences sp1 = getActivity().getSharedPreferences("tag",
                Context.MODE_PRIVATE);
        Editor editor = sp1.edit();
        editor.putString("act", "knetpaymentwebview");
        editor.putString("gatewayUrl", PAYPALGatewayURL);
        editor.commit();

        backButton = (Button) v.findViewById(R.id.buttonbacktermsknet);
        backTextView = (TextView) v.findViewById(R.id.textViewbacktermsknet);
        webViewPAYPAL = (WebView) v.findViewById(R.id.webviewknet);

        webViewPAYPAL.getSettings().setLoadWithOverviewMode(true);
        // webViewVISA.getSettings().setUseWideViewPort(true);
        webViewPAYPAL.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webViewPAYPAL.setScrollbarFadingEnabled(false);
        webViewPAYPAL.getSettings().setBuiltInZoomControls(true);
        webViewPAYPAL.getSettings().setSupportZoom(true);

        backTextView.setText("CREDIT CARD");

        // ...................back button press section
        // start............................
        backButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.e("Knet payment frag", "Back pressed Button");
                backToHome();

            }
        });

        backTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.e("Knet payment frag", "Back pressed TextView");
                backToHome();

            }
        });
        // ...................back button press section
        // end............................

        Log.e("Knet URL", PAYPALGatewayURL);
        webViewPAYPAL.getSettings().setJavaScriptEnabled(true);
        webViewPAYPAL.setWebViewClient(mWebViewClient);
        webViewPAYPAL.loadUrl(PAYPALGatewayURL);

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

        sample = activity;
        super.onAttach(activity);
        listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) activity;
        listenerHideView.makeDefaultHeader();
        listenerHideView.hide();
        listenerHideView.promoItemHideTopbar();
        listenerHideView.setTopBarForCart();

    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.e("URL in start", url);

            if (url.contains("checkout/success")) {
                // Success
                Log.e("Paypal Payment", "Payment Success");

            } else if (url.contains("checkout/checkout")) {
                Log.e("Paypal Payment", "Payment Failed");
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.e("URL in finish", url);
        }
    };

    private void backToHome() {
        Fragment fr = new FragmentCategories();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_place, fr);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void setupUI(View view, boolean isHeading) {
        Typeface myTypeface = Typeface.createFromAsset(getActivity()
                .getAssets(), "fonts/HELVETICANEUELTARABIC-ROMAN.TTF");
        // TextView myTextView = (TextView)view.findViewById(R.id.textView1);
        /* backTextView.setTypeface(myTypeface); */
    }

    private String getLanguage() {
        SharedPreferences sp = getActivity().getSharedPreferences("lan",
                Context.MODE_PRIVATE);

        if (sp.getString("lan", "en").equals("en"))
            return "1";
        else
            return "2";
    }

    Typeface getRegularTypeFace() {
        return Typeface.createFromAsset(getActivity().getAssets(),
                getActivity().getString(R.string.regular_typeface));
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
                        backToHome();
                        return true;
                    }
                }
                return false;
            }
        });
        super.onActivityCreated(savedInstanceState);
    }


    private class PostPaymentResult extends AsyncTask<Void, Void, Void> {

        JSONObject jObject;

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
            String postPayment;
            //http://dairam.com/index.php?route=api/paypal/success&id=74&language=1
            postPayment = AppConstants.POST_PAYPAL_PAYMENT_RESULT;

            postPayment += Util.getCustomerId(getActivity()) +
                    "&language=" + Util.getLanguage(getActivity());
            try {
                jObject = JSONfunctions.getJSONfromURL(postPayment);
            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (jObject != null) {
                try {
                    Log.e("Payment Post Response", jObject.toString());
                    //SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                    JSONObject obj = jObject.getJSONObject("customer");

                    Log.e("Cart", obj.getString("cart"));
                    Log.e("Wishlist", obj.getString("wishlist"));

                    Editor editor = sharedPrefs.edit();
                    editor.putString("cart", obj.getString("cart"));
                    editor.putString("wishlist", obj.getString("wishlist"));
                    editor.commit();
                    listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) sample;
                    listenerHideView.cartListCountUpdation(obj
                            .getString("cart"));
                    listenerHideView.wishListCountUpdation(obj
                            .getString("wishlist"));
                } catch (Exception e) {
                    Log.e("KnetResltFrag. postPay", "Exception");
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getActivity(), "Order not Placed. " + R.string.no_response, Toast.LENGTH_SHORT).show();
            }
            mProgressDialog.dismiss();
        }
    }
}
