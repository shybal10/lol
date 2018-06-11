package com.dairam.android.fragments.activity.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.interfaces.listenerHideView;
import com.dairam.android.fragments.activity.utillities.Util;

public class CheckoutFragmentSecondStep extends Fragment {
	String TAG = "CheckoutFragmentSecondStep";
	Button nextButton;
	Button backToHomeButton;
	Context context;

	Button buttonCC, buttonCOD, buttonKNET, buttonPP, buttonCU;
	String paymentMode = "COD";

	Fragment fr;

	String discount;
	String addressId;
	TextView backTextView;

	Activity sample;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		context = getActivity();
		View v = inflater.inflate(R.layout.layout_secondstep, container, false);

		Bundle b = getArguments();
		addressId = b.getString("shipping_address");
		discount = b.getString("discount");

		SharedPreferences sp1 = getActivity().getSharedPreferences("tag",
				Context.MODE_PRIVATE);
		Editor editor = sp1.edit();
		editor.putString("act", "checkoutfrag2");
		editor.putString("shipping_address", addressId);
		editor.putString("discount", discount);
		editor.commit();

		nextButton = (Button) v.findViewById(R.id.buttonnext);
		backToHomeButton = (Button) v.findViewById(R.id.buttonbackregister);
		backTextView = (TextView) v.findViewById(R.id.textViewbackcheckout);
		// chargedExtra = (TextView)v.findViewById(R.id.dkfj);

		buttonCC = (Button) v.findViewById(R.id.buttoncart);
		buttonCOD = (Button) v.findViewById(R.id.buttonc1);
		buttonKNET = (Button) v.findViewById(R.id.buttonc2);
		buttonPP = (Button) v.findViewById(R.id.butt);
		buttonCU = (Button) v.findViewById(R.id.butt5);

		backTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goBackPage();
			}
		});

		nextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fr = new CheckoutLastStage();
				Bundle bundle = new Bundle();
				bundle.putString("shipping_address", addressId);
				bundle.putString("discount", discount);
				bundle.putString("paymentmode", paymentMode);
				fr.setArguments(bundle);
				FragmentManager fm = getFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
				fragmentTransaction.replace(R.id.fragment_place, fr);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
			}
		});

		backToHomeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goBackPage();
			}
		});

		buttonCC.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				paymentMode = "VISA";
				setPaymentButton((byte) 1);
			}
		});
		buttonCOD.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				paymentMode = "COD";
				setPaymentButton((byte) 2);
			}
		});
		buttonKNET.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				paymentMode = "KNET";
				setPaymentButton((byte) 3);
			}
		});
		/*
		 * buttonPP.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { paymentMode = "PAYPAL";
		 * setPaymentButton((byte) 4); } });
		 */
		buttonCU.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				paymentMode = "CASHU";
				setPaymentButton((byte) 5);
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

	private void setPaymentButton(byte paybutton) {
		switch (paybutton) {
		case 1:
			if (getLanguage().equals("1")) {
				buttonCC.setBackgroundResource(R.drawable.bubble1_sel);
				buttonCOD.setBackgroundResource(R.drawable.bubble2);
				buttonKNET.setBackgroundResource(R.drawable.bubble3);
				buttonPP.setBackgroundResource(R.drawable.bubble4);
				buttonCU.setBackgroundResource(R.drawable.bubble5);
			} else {
				buttonCC.setBackgroundResource(R.drawable.bub_1_sel);
				buttonCOD.setBackgroundResource(R.drawable.bubble2_ar);
				buttonKNET.setBackgroundResource(R.drawable.bub_3);
				buttonPP.setBackgroundResource(R.drawable.bub_4);
				buttonCU.setBackgroundResource(R.drawable.bub_5);
			}
			break;

		case 2:
			if (getLanguage().equals("1")) {

				buttonCOD.setBackgroundResource(R.drawable.bubble2_a);
				buttonCC.setBackgroundResource(R.drawable.bubble1_a);
				buttonKNET.setBackgroundResource(R.drawable.bubble3);
				buttonPP.setBackgroundResource(R.drawable.bubble4);
				buttonCU.setBackgroundResource(R.drawable.bubble5);
			} else {
				buttonCOD.setBackgroundResource(R.drawable.bub_2_sel);
				buttonCC.setBackgroundResource(R.drawable.bub_1);
				buttonKNET.setBackgroundResource(R.drawable.bub_3);
				buttonPP.setBackgroundResource(R.drawable.bub_4);
				buttonCU.setBackgroundResource(R.drawable.bub_5);
			}
			break;

		case 3:
			if (getLanguage().equals("1")) {
				buttonKNET.setBackgroundResource(R.drawable.bubble_knet_sel);
				buttonCC.setBackgroundResource(R.drawable.bubble1_a);
				buttonCOD.setBackgroundResource(R.drawable.bubble2);
				buttonPP.setBackgroundResource(R.drawable.bubble4);
				buttonCU.setBackgroundResource(R.drawable.bubble5);
			} else {
				buttonKNET.setBackgroundResource(R.drawable.bubble_knet_sel_ar);
				buttonCC.setBackgroundResource(R.drawable.bub_1);
				buttonCOD.setBackgroundResource(R.drawable.bubble2_ar);
				buttonPP.setBackgroundResource(R.drawable.bub_4);
				buttonCU.setBackgroundResource(R.drawable.bub_5);
			}
			break;

		case 4:
			if (getLanguage().equals("1")) {
				buttonPP.setBackgroundResource(R.drawable.bubble4_sel);
				buttonCC.setBackgroundResource(R.drawable.bubble1_a);
				buttonCOD.setBackgroundResource(R.drawable.bubble2);
				buttonKNET.setBackgroundResource(R.drawable.bubble3);
				buttonCU.setBackgroundResource(R.drawable.bubble5);
			} else {
				buttonPP.setBackgroundResource(R.drawable.bub_4_sel);
				buttonCC.setBackgroundResource(R.drawable.bub_1);
				buttonCOD.setBackgroundResource(R.drawable.bubble2_ar);
				buttonKNET.setBackgroundResource(R.drawable.bub_3);
				buttonCU.setBackgroundResource(R.drawable.bub_5);
			}
			break;

		case 5:
			if (getLanguage().equals("1")) {
				buttonCU.setBackgroundResource(R.drawable.bubble5_sel);
				buttonCC.setBackgroundResource(R.drawable.bubble1_a);
				buttonCOD.setBackgroundResource(R.drawable.bubble2);
				buttonKNET.setBackgroundResource(R.drawable.bubble3);
				buttonPP.setBackgroundResource(R.drawable.bubble4);
			} else {
				buttonCU.setBackgroundResource(R.drawable.bub_5_sel);
				buttonCC.setBackgroundResource(R.drawable.bub_1);
				buttonKNET.setBackgroundResource(R.drawable.bub_3);
				buttonCOD.setBackgroundResource(R.drawable.bubble2_ar);
				buttonPP.setBackgroundResource(R.drawable.bub_4);
			}
			break;

		default:
			break;
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
		TextView myTextView = (TextView) view.findViewById(R.id.txtpaymethod);
		myTextView.setTypeface(myTypeface);
		backTextView.setTypeface(myTypeface);

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

	private void goBackPage() {
		fr = new CheckoutFragmentFirstStep();
		Bundle bundle = new Bundle();
		bundle.putString("discount", discount);
		fr.setArguments(bundle);
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
						goBackPage();
						return true;
					}
				}
				return false;
			}
		});
		super.onActivityCreated(savedInstanceState);
	}
}
