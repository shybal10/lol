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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.interfaces.listenerHideView;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewsletterSignup extends Fragment {

    EditText emailEditText;
    Button submitButton;

    String emailSignupString;

    Boolean isEmailValid = false;

    String newsLetterSignupURL = "http://dairam.com/index.php?route=api/newsletter/get&language=1&email=";

    LinearLayout relay;

    Button backButton;
    TextView backTextView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences sp = getActivity().getSharedPreferences("lan",
                Context.MODE_PRIVATE);
        String lan = sp.getString("lan", "en");
        setLanguage(lan);
        View v = inflater.inflate(R.layout.activity_newsletter, container,
                false);
        SharedPreferences sp1 = getActivity().getSharedPreferences("tag",
                Context.MODE_PRIVATE);
        String p = "newslettersignup";
        Editor editor = sp1.edit();
        editor.putString("act", p);
        editor.commit();

        relay = (LinearLayout) v.findViewById(R.id.newsletterrellay);
        emailEditText = (EditText) v
                .findViewById(R.id.editText1emailnewsletter);
        submitButton = (Button) v.findViewById(R.id.buttonsubmitnewsletter);
        backButton = (Button) v.findViewById(R.id.buttonbacknewsletter);
        backTextView = (TextView) v.findViewById(R.id.textViewbacknewsletter);

        Typeface fontFace = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/LibreBaskerville-Regular.otf");
        backTextView.setTypeface(fontFace);

        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                BackToHome();
            }
        });

        backTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                BackToHome();
            }
        });
        relay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                System.out.println("Error handling");
            }
        });

		/*
         * Email sign up
		 */
        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                emailSignupString = emailEditText.getText().toString();

                isEmailValid = emailValidator(emailSignupString);

                if (isEmailValid == true) {

                    try {
                        emailSignupString = URLEncoder.encode(
                                emailSignupString, "UTF-8");
                        newsLetterSignupURL = newsLetterSignupURL
                                + emailSignupString;
                        new RetrieveFeedTask().execute(newsLetterSignupURL);

						/*Fragment fr = new FragmentOne();
						FragmentManager fm = getFragmentManager();
						FragmentTransaction fragmentTransaction = fm
								.beginTransaction();
						fragmentTransaction.replace(R.id.fragment_place, fr);
						fragmentTransaction.addToBackStack(null);
						fragmentTransaction.commit();*/

                        Fragment fr = new FragmentCategories();
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_place, fr);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    } catch (Exception e) {
                        System.out.println("Error in encoding the string" + e);
                    }
                } else {
                    Toast.makeText(
                            getActivity(),
                            getResources()
                                    .getString(R.string.enter_valid_email),
                            Toast.LENGTH_SHORT).show();
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

        return v;
    }

    public String getLanguage() {
        SharedPreferences sp = getActivity().getSharedPreferences("lan",
                Context.MODE_PRIVATE);
        if (sp.getString("lan", "en").equals("en"))
            return "1";
        else
            return "2";
    }

    private void BackToHome() {
		/*Fragment fr = new FragmentOne();
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

    private void setLanguage(String lang) {

        String languageToLoad = lang;
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getResources().updateConfiguration(config, null);

    }

    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, LoginFragment> {

        String SetServerString = "";

        protected LoginFragment doInBackground(String... urls) {

            HttpClient Client = new DefaultHttpClient();

            // Create Request to server and get response
            HttpGet httpget = new HttpGet(newsLetterSignupURL);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            try {
                SetServerString = Client.execute(httpget, responseHandler);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }

        protected void onPostExecute(LoginFragment feed) {

            if (SetServerString.contains("false")) {

                if (getLanguage().equals("1"))
                    Toast.makeText(
                            getActivity(),
                            "This email ID is already subscribed to our newsletter",
                            Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getActivity(),
                            "هذا البريد الالكتروني تم تسجيله مسبقاً ",
                            Toast.LENGTH_LONG).show();
            } else {
                if (getLanguage().equals("1"))
                    Toast.makeText(getActivity(),
                            "Thank you for joining our newsletter",
                            Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getActivity(),
                            "شكراً لانضمامك لنشرتنا الالكترونية",
                            Toast.LENGTH_LONG).show();

            }

        }
    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) activity;
        listenerHideView.reversePromoItemHideTopbar();
        listenerHideView.hide();
        listenerHideView.dairamBar();
        // listenerHideView.closeDrawer();

    }

    public void setupUI(View view, boolean isHeading) {
        Typeface myTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/HELVETICANEUELTARABIC-ROMAN.TTF");
        TextView myTextView = (TextView) view.findViewById(R.id.textViewbacknewsletter);
        myTextView.setTypeface(myTypeface);
		   /*  backTextView.setTypeface(myTypeface);*/

        emailEditText.setTypeface(myTypeface);


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
                        BackToHome();
                        return true;
                    }
                }
                return false;
            }
        });
        super.onActivityCreated(savedInstanceState);
    }

}
