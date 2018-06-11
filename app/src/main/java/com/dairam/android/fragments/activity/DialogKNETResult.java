package com.dairam.android.fragments.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.PurchaseEvent;
import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.appconstants.AppConstants;
import com.dairam.android.fragments.activity.fragments.FragmentCategories;
import com.dairam.android.fragments.activity.interfaces.listenerHideView;
import com.dairam.android.fragments.activity.parserfiles.JSONfunctions;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;

public class DialogKNETResult extends Fragment {
	Activity sample;
	TextView details, backTextView;
	
	TextView paymentId, result, postdate, tranId, auth, ref, trackId;
	Button backButton;
	ArrayList<String> KNETResut = new ArrayList<String>();
	ArrayList<String> KNETList;
	
	String payment;
	
	ProgressDialog mProgressDialog;
	
	String orderId = null;
	
	SharedPreferences sharedPrefs;
	
	public static final String MyPREFERENCES = "MyPrefs";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.knet_payment_result, container,
				false);
		
		sharedPrefs = getActivity().getSharedPreferences(MyPREFERENCES,
				Context.MODE_PRIVATE);

		details = (TextView) v.findViewById(R.id.paymentresult);
		backButton = (Button) v.findViewById(R.id.buttonbacktermsknetres);
		backTextView = (TextView) v.findViewById(R.id.textViewbacktermsknetres);

		paymentId = (TextView) v.findViewById(R.id.paymentid);
		result = (TextView) v.findViewById(R.id.result_data);
		postdate = (TextView) v.findViewById(R.id.postdate_data);
		tranId = (TextView) v.findViewById(R.id.tranid_data);
		auth = (TextView) v.findViewById(R.id.auth_data);
		ref = (TextView) v.findViewById(R.id.ref_data);
		trackId = (TextView) v.findViewById(R.id.trackid_data);		

		Bundle data = getArguments();
		KNETResut = data.getStringArrayList("result");
		payment = data.getString("payment");
		
		if(payment.equals("VISA"))
			backTextView.setText("CREDIT CARD");
		else if(payment.equals("KNET"))
			backTextView.setText("KNET PAYMENT");
		
		SharedPreferences sp1=getActivity().getSharedPreferences("tag", Context.MODE_PRIVATE);
		//Set arrayset = new HashSet(KNETResut);
		Editor editor = sp1.edit();
		editor.putString("act", "knetpaymentresult");
		/*editor.putStringSet("result", arrayset);*/
		editor.commit(); 

// ...................back button press section start............................
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
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
// ...................back button press section end............................
		KNETList = new ArrayList<String>();
		//for(int i=2;i<KNETResut.size();i++)
		//{
			for(String str : KNETResut)
			{
				if(!str.endsWith("="))
				{
					for(String str1 : str.split("="))
						KNETList.add(str1);
				}
				else
				{
					KNETList.add(str.replace("=", " "));
					KNETList.add("  ");
				}
			}
		//}
			//Result=CAPTURED
			String captureStatus = "CAPTURED";
			if(KNETList.contains(captureStatus))
			{
				Log.e("DialogKNET Resp","Captured");
				if(getLanguage().equals("1"))
					Toast.makeText(getActivity(), "Invoice and delivery details has been sent to your e-mail.", Toast.LENGTH_LONG).show();
				else
					Toast.makeText(getActivity(), "تم ارسال تفاصيل الفاتورة و التوصيل الى بريدك الالكتروني ",
							Toast.LENGTH_LONG).show();
			}
			else
			{
				Log.e("DialogKNET Resp","Not Captured...");
				if(getLanguage().equals("1"))
					Toast.makeText(getActivity(), " Payment Failed. Please try again", Toast.LENGTH_LONG).show();
				else
					Toast.makeText(getActivity(), " دفع فشل. يرجى المحاولة فيما بعد ", Toast.LENGTH_LONG).show();
			}
			
			new PostPaymentResult().execute();
			
			paymentId.setText(KNETList.get(3));
			result.setText(KNETList.get(5));
			postdate.setText(KNETList.get(7));
			tranId.setText(KNETList.get(9));
			auth.setText(KNETList.get(11));
			ref.setText(KNETList.get(13));
			trackId.setText(KNETList.get(15));
			orderId = KNETList.get(17);
			
			Log.e("Order Id", orderId);	
			

		return v;
	}

	private String getLanguage() {
		SharedPreferences sp = getActivity().getSharedPreferences("lan",
				Context.MODE_PRIVATE);

		if (sp.getString("lan", "en").equals("en"))
			return "1";
		else
			return "2";
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
	private void backToHome()
    {
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
			
			String customer_id = sharedPrefs.getString("id", "");
			String postPayment = "";
			
			if(payment.equals("KNET"))
				postPayment = AppConstants.POST_KNET_PAYMENT_RESULT;
			else if(payment.equals("VISA"))
				postPayment = AppConstants.POST_VISA_PAYMENT_RESULT;
			
			postPayment = postPayment+KNETList.get(3)+
						"&Result="+ KNETList.get(5)+
						"&PostDate="+ KNETList.get(7)+
						"&TranID="+ KNETList.get(9)+
						"&Auth="+ KNETList.get(11)+						
						"&Ref="+ KNETList.get(13)+											
						"&TrackID="+ KNETList.get(15)+											
						"&UDF1="+ KNETList.get(17)+											
						"&id="+ customer_id;
			try {
				Log.e("KnetResultFrag .postPay", postPayment);

				// jObject = pickJSONObjURL(mainUrl);
				jObject = JSONfunctions.getJSONfromURL(postPayment);
			} catch (Exception e) {
				Log.e("Error", "Error in pickJSONObjURL(mainUrl)");
				e.printStackTrace();

			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(jObject != null)
			{
				try {
					Answers.getInstance().logPurchase(new PurchaseEvent()
							.putItemPrice(BigDecimal.valueOf(0.0))
							.putCurrency(Currency.getInstance("KWD"))
							.putItemName(payment+" Purchase")
							.putItemType("")
							.putItemId("")
							.putSuccess(true));
					Log.e("Payment Post Response",jObject.toString());
					//SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
					
					JSONObject obj = jObject.getJSONObject("customer");
					
					Log.e("Cart",obj.getString("cart"));
					Log.e("Wishlist",obj.getString("wishlist"));
					
					Editor editor = sharedPrefs.edit();
					editor.putString("cart", obj.getString("cart"));
					editor.putString("wishlist", obj.getString("wishlist"));
					editor.commit();
					listenerHideView listenerHideView = (com.dairam.android.fragments.activity.interfaces.listenerHideView) sample;
					listenerHideView.cartListCountUpdation(obj
							.getString("cart"));
					listenerHideView.wishListCountUpdation(obj
							.getString("wishlist"));					
				} 
				catch (Exception e) 
				{
					Log.e("KnetResultFrag.postPay","Exception");
					e.printStackTrace();
				}
			}
			else{
				Answers.getInstance().logPurchase(new PurchaseEvent()
						.putItemPrice(BigDecimal.valueOf(0.0))
						.putCurrency(Currency.getInstance("KWD"))
						.putItemName(payment+" Purchase")
						.putItemType("")
						.putItemId("")
						.putSuccess(false));
				Toast.makeText(getActivity(), "Order not Placed. "+R.string.no_response, Toast.LENGTH_SHORT).show();
			}				
			mProgressDialog.dismiss();
		}
	}
	
	private void CallHomePage() {

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
