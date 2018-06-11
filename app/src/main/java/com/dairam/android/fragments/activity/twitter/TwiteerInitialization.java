package com.dairam.android.fragments.activity.twitter;

import java.io.File;

import com.dairam.android.fragments.R;

import twitter4j.User;
import twitter4j.auth.AccessToken;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class TwiteerInitialization {
	public static final String TAG = "TwiteerInitialization";

	public TwitterUtil twitterUtil = null;
	public User twitterUser = null;
	public Context context;
	public Activity activity;
	public File castedImage;
	public String stringMessage;

	public TwiteerInitialization(Activity activity, File castedImage,
			String stringMessage) {

		this.activity = activity;
		this.context = activity.getApplicationContext();
		this.castedImage = castedImage;
		this.stringMessage = stringMessage;
		
		
		TwitterUtil.init(activity.getApplicationContext());
		twitterUtil = TwitterUtil.getInstance();
		getTwitterLogin();

	}
	public TwiteerInitialization(Activity activity) {

		this.activity = activity;
		this.context = activity.getApplicationContext();
		

	}
	public void getTwitterLogin() {

		TwitterUtil.init(activity.getApplicationContext());
		twitterUtil = TwitterUtil.getInstance();

		new Thread(new Runnable() {
			@Override
			public void run() {
				twitterUtil.setLoginListener(twitterLoginListener);
				twitterUtil.authorize(activity);
				
				
			}
		}).start();

	}
	
	public AccessToken logout() {
		return twitterUtil.logout();
	}
	
	LoginListener twitterLoginListener = new LoginListener() {

		@Override
		public void loginSucess() {

			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					populatetwitterUserDetails();
					Toast.makeText(activity, "Login success", Toast.LENGTH_SHORT).show();
				}
			});

		}

		@Override
		public void loginEror(int statusCode) {

		}
	};

	public void populatetwitterUserDetails() {
		new PicStatusUpdate().execute();

	}
	public void uploadToTwitter() {
		new PicStatusUpdate().execute();
	}
	ProgressDialog progressDialog;

	public class PicStatusUpdate extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {

			progressDialog = new ProgressDialog(activity);
			progressDialog.setMessage(activity.getString(R.string.updating_twitter));
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(false);
			progressDialog.show();

			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				Log.i(TAG, "castedImage: " + castedImage.getAbsolutePath());
				
				int statusId = twitterUtil
						.uploadPic(castedImage, stringMessage);
				Log.i(TAG, "statusId: " + statusId);
			} catch (Exception e) {

				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			progressDialog.dismiss();
			Toast.makeText(activity.getApplicationContext(),
					activity.getString(R.string.status_updated_twitter), Toast.LENGTH_SHORT).show();
			super.onPostExecute(result);
		}

	}

	@SuppressWarnings("unused")
	private class StatusUpdateAsyncTask extends
			AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			/*
			 * progressDialog = new ProgressDialog(getActivity());
			 * progressDialog.setMessage("Updating to twitter...");
			 * progressDialog.setIndeterminate(false);
			 * pDialog.setCancelable(false); progressDialog.show();
			 */
		}

		@Override
		protected String doInBackground(String... args) {

			String status = args[0];
			twitter4j.Status responseCheck = twitterUtil
					.updateTwitterStatus(status);

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// progressDialog.dismiss();
			Toast.makeText(activity.getApplicationContext(),
					activity.getString(R.string.status_updated_twitter), Toast.LENGTH_SHORT).show();
			super.onPostExecute(result);
		}

	}

}
