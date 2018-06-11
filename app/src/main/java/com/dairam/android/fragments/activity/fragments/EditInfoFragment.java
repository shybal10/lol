package com.dairam.android.fragments.activity.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
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
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.interfaces.listenerHideView;
import com.dairam.android.fragments.activity.parserfiles.JSONfunctions;
import com.dairam.android.fragments.activity.utillities.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditInfoFragment extends Fragment {

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String customerid = "id";
    public static final String firstname = "firstname";
    public static final String lastname = "lastname";
    public static final String email_string = "email";
    public static final String phone_string = "phone";
    LinearLayout mainLayout;
    Button saveButton, backButton;
    TextView headerTextView;
    EditText firstNameEditText, lastNameEditText, emailEditText,
            telephoneEditText, passwordEditText, reenterEditText;
    String firstNameString, lastNameString, emailString, telephoneString,
            passwordString, reenterPasswordString;
    String editAccountUrl = AppConstants.EDIT_USER_ACCOUNT_URL;
    String custId = null;
    String regexStr = "^[+]?[0-9]{10,13}$";
    Fragment fr;
    JSONObject jsonobject;
    Context context;
    ProgressDialog mProgressDialog;
    /* Shared preference variables */
    SharedPreferences sharedPreferences;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences sp = getActivity().getSharedPreferences("lan",
                Context.MODE_PRIVATE);
        String lan = sp.getString("lan", "en");
        setLanguage(lan);
        View v = inflater.inflate(R.layout.fragment_editinfo, container, false);

        context = getActivity();

        sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);

        final listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) getActivity();

        SharedPreferences sp1 = getActivity().getSharedPreferences("tag",
                Context.MODE_PRIVATE);
        String p = "fragmenteditaccount";
        Editor editor = sp1.edit();
        editor.putString("act", p);
        editor.commit();

        backButton = (Button) v.findViewById(R.id.buttonbackeditinfo);
        headerTextView = (TextView) v.findViewById(R.id.textViewbackeditinfo);
        mainLayout = (LinearLayout) v.findViewById(R.id.linlayeditinfo);
        saveButton = (Button) v.findViewById(R.id.buttonsaveeditinfo);
        firstNameEditText = (EditText) v
                .findViewById(R.id.editTextfirstnameeditinfo);
        lastNameEditText = (EditText) v
                .findViewById(R.id.editTextlastnameeditinfo);
        emailEditText = (EditText) v.findViewById(R.id.editTextemaileditinfo);
        telephoneEditText = (EditText) v
                .findViewById(R.id.editTexttelephoneeditinfo);
        passwordEditText = (EditText) v
                .findViewById(R.id.editTextpasswordeditinfo);
        reenterEditText = (EditText) v
                .findViewById(R.id.editTextrepasseditinfo);

        firstNameEditText.setFilters(new InputFilter[]{new InputFilter() {
            public CharSequence filter(CharSequence src, int start, int end,
                                       Spanned dst, int dstart, int dend) {
                if (src.equals("")) { // for backspace
                    return src;
                }
                if (src.toString().matches("[a-zA-Z ]+")) {
                    return src;
                }
                return "";
            }
        }});
        lastNameEditText.setFilters(new InputFilter[]{new InputFilter() {
            public CharSequence filter(CharSequence src, int start, int end,
                                       Spanned dst, int dstart, int dend) {
                if (src.equals("")) { // for backspace
                    return src;
                }
                if (src.toString().matches("[a-zA-Z ]+")) {
                    return src;
                }
                return "";
            }
        }});

        Typeface fontFace = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/LibreBaskerville-Regular.otf");
        headerTextView.setTypeface(fontFace);

        if (sharedPreferences.contains(customerid)) {
            custId = sharedPreferences.getString(customerid, "");
        }
        if (sharedPreferences.contains(firstname)) {
            firstNameEditText.setText(sharedPreferences
                    .getString(firstname, ""));
        }
        if (sharedPreferences.contains(lastname)) {
            lastNameEditText.setText(sharedPreferences.getString(lastname, ""));
        }
        if (sharedPreferences.contains(email_string)) {

            Log.d("CODE EMAIL FACEBOOK:", sharedPreferences.getString(email_string, ""));
            System.out.println("Enters into here.... "
                    + sharedPreferences.getString(email_string, ""));
            emailEditText
                    .setText(sharedPreferences.getString(email_string, ""));
        }
        if (sharedPreferences.contains(phone_string)) {
            telephoneEditText.setText(sharedPreferences.getString(phone_string,
                    ""));
        }

        firstNameEditText.requestFocus();
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                firstNameString = firstNameEditText.getText().toString();
                lastNameString = lastNameEditText.getText().toString();
                emailString = emailEditText.getText().toString();
                telephoneString = telephoneEditText.getText().toString();
                passwordString = passwordEditText.getText().toString();
                reenterPasswordString = reenterEditText.getText().toString();

                if ((firstNameString.length() > 0)
                        || (lastNameString.length() > 0)
                        || (emailString.length() > 0)
                        || (passwordString.length() > 0)
                        || (reenterPasswordString.length() > 0)) {

                    System.out.println("Condition correct");

                    Boolean isEmailValid = false;
                    isEmailValid = emailValidator(emailString);

                    if (isEmailValid == true) {
                        // if(telephoneString.length()>0)
                        // {

                        if (telephoneString.length() < 10
                                || telephoneString.length() > 13
                                || telephoneString.matches(regexStr) == false) {
                            Toast.makeText(
                                    getActivity(),
                                    getResources().getString(
                                            R.string.enter_valid_phone),
                                    Toast.LENGTH_SHORT).show();

                        } else {

                            if (passwordString.equals(reenterPasswordString)) {
                                // call webservice for edit user account
                                // http://dairam.com/index.php?route=api/account/get&firstname=dsadasdsadsadsa&lastname=sdfasdf&telephone=123123213&id=82&password=123&confirm=123
                                callEditAccountService();

                                // Toast.makeText(getActivity(), "Values saved",
                                // Toast.LENGTH_SHORT).show();
                            } else {
                                if (getLanguage().equals("1"))
                                    Toast.makeText(getActivity(),
                                            "Password mismatch",
                                            Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(getActivity(),
                                            "كلمة المرور عدم تطابق",
                                            Toast.LENGTH_SHORT).show();

                                passwordEditText.setText("");
                                reenterEditText.setText("");
                                passwordEditText.requestFocus();
                            }
                        }
                        // }
                        // else
                        // {
                        // callEditAccountService();
                        // }

                    } else {
                        Toast.makeText(
                                getActivity(),
                                getResources().getString(
                                        R.string.enter_valid_email),
                                Toast.LENGTH_SHORT).show();

                        emailEditText.setText("");
                        emailEditText.requestFocus();
                    }

                } else {
                    Toast.makeText(getActivity(),
                            getResources().getString(R.string.fill_all_fields),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                goBackPage();
            }
        });

        headerTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goBackPage();
            }
        });

        mainLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("Do Nothing error Handling");
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

    private void callEditAccountService() {
        editAccountUrl += firstNameString + "&lastname=" + lastNameString
                + "&telephone=" + telephoneString + "&id=" + custId
                + "&password=" + passwordString + "&confirm="
                + reenterPasswordString + "";
        Log.d("Edit Account URL", editAccountUrl);

        new EditAccountTask().execute();
    }

    private boolean emailValidator(String email) {
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
        listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) activity;
        listenerHideView.reversePromoItemHideTopbar();
        listenerHideView.hide();
        listenerHideView.dairamBar();
        // listenerHideView.closeDrawer();

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
        TextView myTextView = (TextView) view.findViewById(R.id.textView1);
        myTextView.setTypeface(myTypeface);
        headerTextView.setTypeface(myTypeface);
        firstNameEditText.setTypeface(myTypeface);
        lastNameEditText.setTypeface(myTypeface);
        emailEditText.setTypeface(myTypeface);
        telephoneEditText.setTypeface(myTypeface);
        passwordEditText.setTypeface(myTypeface);
        reenterEditText.setTypeface(myTypeface);

    }

    Typeface getRegularTypeFace() {
        return Typeface.createFromAsset(context.getAssets(),
                context.getString(R.string.regular_typeface));
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

    private void goBackPage() {

        fr = new MyAccountItemFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_place, fr);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        // listenerHideView.closeDrawer();
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
                        goBackPage();
                        return true;
                    }
                }
                return false;
            }
        });
        super.onActivityCreated(savedInstanceState);
    }

    class EditAccountTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(context, R.style.MyTheme);
            mProgressDialog.setCancelable(false);
            mProgressDialog
                    .setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            mProgressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.e("Edit Info Frag", editAccountUrl);
            jsonobject = JSONfunctions.getJSONfromURL(editAccountUrl);

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            mProgressDialog.dismiss();
            try {
                if (jsonobject != null) {
                    // Locate the array name in JSON
                    String str = jsonobject.getString("sucsess");

                    Log.e("THE RESULT>>>>>>>>>>", str);
                    Editor prefEdit = sharedPreferences.edit();

                    prefEdit.putString("firstname", firstNameString);
                    prefEdit.putString("lastname", lastNameString);
                    prefEdit.putString("phone", telephoneString);
                    prefEdit.commit();

                    if (getLanguage().equals("1"))
                        Toast.makeText(context, "Account Updated",
                                Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(context, "تم تحديث المعلومات بنجاح ",
                                Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getActivity(), R.string.no_response,
                            Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                if (e.getMessage().equals("No value for sucsess")) {
                    Toast.makeText(context, "No Valid Password to Save",
                            Toast.LENGTH_SHORT).show();
                }
                e.printStackTrace();
            }

        }

    }
}
