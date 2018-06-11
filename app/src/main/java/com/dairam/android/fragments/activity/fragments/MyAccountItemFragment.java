package com.dairam.android.fragments.activity.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.interfaces.listenerHideView;
import com.dairam.android.fragments.activity.utillities.Util;

import java.util.Locale;

public class MyAccountItemFragment extends Fragment {

    LinearLayout linlay;

    Button backButton;
    TextView headerTextView;
    RelativeLayout editinfo, myorder, addressbook;

    Fragment fr;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String customerid = "id";
    public static final String firstname = "firstname";
    public static final String lastname = "lastname";
    public static final String email_string = "email";
    public static final String cart_string = "cart";
    public static final String wishlist_String = "wishlist";

    SharedPreferences sharedPreferences;

    String wishlist_count;
    String cart_count;
    String cust_id = null;

    Activity activity;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity = getActivity();

        SharedPreferences sharedpreferences = getActivity()
                .getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        cust_id = sharedpreferences.getString(customerid, "");
        SharedPreferences sp = getActivity().getSharedPreferences("lan",
                Context.MODE_PRIVATE);
        String lan = sp.getString("lan", "en");
        setLanguage(lan);
        View v = inflater
                .inflate(R.layout.fragment_myaccount, container, false);

        backButton = (Button) v.findViewById(R.id.buttonbackmyaccount);
        headerTextView = (TextView) v.findViewById(R.id.textViewbackmyaccount);
        editinfo = (RelativeLayout) v.findViewById(R.id.myaccounteditinfo);
        myorder = (RelativeLayout) v.findViewById(R.id.myaccountmyorder);
        addressbook = (RelativeLayout) v
                .findViewById(R.id.myaccountaddressbook);
        linlay = (LinearLayout) v.findViewById(R.id.rellaylin);

        sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        wishlist_count = sharedPreferences.getString("wishlist", "");
        cart_count = sharedPreferences.getString("cart", "");

        listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) activity;
        listenerHideView.cartListCountUpdation(cart_count);
        listenerHideView.wishListCountUpdation(wishlist_count);

        Typeface fontFace = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/LibreBaskerville-Regular.otf");
        headerTextView.setTypeface(fontFace);

        SharedPreferences sp1 = getActivity().getSharedPreferences("tag",
                Context.MODE_PRIVATE);
        String p = "fragmentaccount";
        Editor editor = sp1.edit();
        editor.putString("act", p);
        editor.commit();
        //listenerHideView.closeDrawer();

        linlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("This is error handling");
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                CallHomePage();
            }
        });

        myorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cust_id == "") {
                    Toast.makeText(getActivity(), getResources().getString(R.string.must_login),
                            Toast.LENGTH_SHORT).show();
                } else {
                    fr = new FragmentMyOrder();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm
                            .beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_place, fr);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                    SharedPreferences sp1 = getActivity().getSharedPreferences("tag", Context.MODE_PRIVATE);
                    String p = "fragmentmyorder";
                    Editor editor = sp1.edit();
                    editor.putString("act", p);
                    editor.commit();


                }
            }
        });
        headerTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CallHomePage();
            }
        });

        editinfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (cust_id == "") {
                    Toast.makeText(getActivity(), getResources().getString(R.string.must_login),
                            Toast.LENGTH_SHORT).show();
                } else {

                    fr = new EditInfoFragment();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm
                            .beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_place, fr);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });

        addressbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cust_id == "") {
                    Toast.makeText(getActivity(), getResources().getString(R.string.must_login),
                            Toast.LENGTH_SHORT).show();
                } else {

                    System.out.println("address book clicked");
                    fr = new AddressBookFragment();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm
                            .beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_place, fr);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });

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
        /*super.onAttach(activity);
		listenerHideView listenerHideView = (com.dairam.interfaces.listenerHideView) activity;
		listenerHideView.promoItemHideTopbar();
		listenerHideView.closeDrawer();*/

        super.onAttach(activity);
        listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) activity;
        listenerHideView.reversePromoItemHideTopbar();
        listenerHideView.hide();
        listenerHideView.dairamBar();

        // if(getLanguage().equals("2"))
        //listenerHideView.closeDrawer();

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

    public void setupUI(View view, boolean isHeading) {
        Typeface myTypeface;
        if (Util.getLanguage(getActivity()).equals("2")) {
            myTypeface = Typeface.createFromAsset(getActivity().getAssets(),
                    "fonts/HELVETICANEUELTARABIC-ROMAN.TTF");
        } else {
            myTypeface = Typeface.createFromAsset(getActivity().getAssets(),
                    "fonts/Georgia.ttf");
        }
        TextView myTextView = (TextView) view.findViewById(R.id.textView2);
        TextView myTextView1 = (TextView) view.findViewById(R.id.textViewmo);
        TextView myTextView2 = (TextView) view.findViewById(R.id.textViewab);
        myTextView.setTypeface(myTypeface);
        headerTextView.setTypeface(myTypeface);
        myTextView1.setTypeface(myTypeface);
        myTextView2.setTypeface(myTypeface);

    }

    Typeface getRegularTypeFace() {
        return Typeface.createFromAsset(getActivity().getAssets(),
                getActivity().getString(R.string.regular_typeface));
    }

    private void CallHomePage() {

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
