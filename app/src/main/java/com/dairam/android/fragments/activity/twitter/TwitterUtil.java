package com.dairam.android.fragments.activity.twitter;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;

import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.webkit.CookieManager;

public class TwitterUtil implements Callback {
	private static String TWITTER_CONSUMER_KEY = "7vUmecej5EThU29ALIeYP1eHD";
	private static String TWITTER_CONSUMER_SECRET_KEY = "qhdRFUYaAWmkVKN2RQwXBeYwOaIM3l2B6KrSO4kAJWul48FroL";
	public static String CALLBACK_URL = "http://fashionworldadmin.mawaqaademo.com/";
	private static Twitter mTwitter;
	private static SharedPreferences sharedPref;
	private static Editor editor;
	private static CommonsHttpOAuthConsumer mHttpOauthConsumer;
	private static DefaultOAuthProvider mHttpOauthprovider;
	private static final String SHARED = "Twitter_Preferences";
	private static final String TWEET_AUTH_KEY = "auth_key";
	private static final String TWEET_AUTH_SECRET_KEY = "auth_secret_key";
	private static final String TWEET_USER_NAME = "user_name";
	private static final String TWEET_USER_PROFILE_IMAGE = "user_profile_image";
	private static final String TWEET_USER_ID = "user_name_id";
	protected static final String TAG = null;
	private static final String URL_REQUEST_TOKEN = "https://api.twitter.com/oauth/request_token";
	private static final String URL_ACCESS_TOKEN = "https://api.twitter.com/oauth/access_token";
	private static final String URL_AUTHORIZE = "https://api.twitter.com/oauth/authorize";
	private ProgressDialog mProgressDlg;

	// Preference Constants
	static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";
	static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";

	String oauthVerifier;

	private static TwitterUtil instance;
	private Context context;
	private static AccessToken mAccessToken;
	private LoginListener mListener;
	private Handler mHandler = null;

	public TwitterUtil(Context context) {
		this.context = context;
		mHandler = new Handler(this);
	}

	public static TwitterUtil getInstance() throws IllegalStateException {
		if (instance == null) {
			throw new IllegalStateException(
					"TwitterUtil not initialized. Call TwitterUtil.init() before calling getInstance.");
		}
		return instance;
	}

	public void setLoginListener(LoginListener listener) {
		mListener = listener;
	}

	public static void init(Context context) {
		if (instance == null) {
			instance = new TwitterUtil(context);
			instance.init();
		}
	}

	private void init() {
		if (mTwitter == null) {
			mTwitter = new TwitterFactory().getInstance();
			sharedPref = context.getSharedPreferences(SHARED,
					Context.MODE_PRIVATE);
			editor = sharedPref.edit();
			mHttpOauthConsumer = new CommonsHttpOAuthConsumer(
					TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET_KEY);
			mHttpOauthprovider = new DefaultOAuthProvider(URL_REQUEST_TOKEN,
					URL_ACCESS_TOKEN, URL_AUTHORIZE);
			mAccessToken = getAccessToken();
			configureToken();
		}
	}

	public void storeAccessToken(AccessToken accessToken, String username,
			String profileUrl, String id) {
		editor.putString(TWEET_AUTH_KEY, accessToken.getToken());
		editor.putString(TWEET_AUTH_SECRET_KEY, accessToken.getTokenSecret());
		editor.putString(TWEET_USER_NAME, username);
		editor.putString(TWEET_USER_PROFILE_IMAGE, profileUrl);
		editor.putString(TWEET_USER_ID, id);
		editor.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
		editor.putString(PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());

		editor.commit();
	}

	public AccessToken  resetAccessToken() {
		editor.putString(TWEET_AUTH_KEY, null);
		editor.putString(TWEET_AUTH_SECRET_KEY, null);
		editor.putString(TWEET_USER_NAME, null);
		editor.commit();
		try {
			CookieManager cookieManager = CookieManager.getInstance();
			cookieManager.setCookie("http://api.twitter.com", null);
			cookieManager.removeSessionCookie();
		} catch (Exception e) {
			e.printStackTrace();
		}
		mTwitter.setOAuthAccessToken(null);
		//mTwitter.shutdown();
		mAccessToken = null;
		return mAccessToken;
	}

	public String getUsername() {
		return sharedPref.getString(TWEET_USER_NAME, "");
	}

	public String getUserID() {
		return sharedPref.getString(TWEET_USER_ID, "");
	}

	public String getUserProfileImageURL() {
		return sharedPref.getString(TWEET_USER_PROFILE_IMAGE, "");
	}

	private AccessToken getAccessToken() {
		String token = sharedPref.getString(TWEET_AUTH_KEY, null);
		String tokenSecret = sharedPref.getString(TWEET_AUTH_SECRET_KEY, null);

		if (token != null && tokenSecret != null)
			return new AccessToken(token, tokenSecret);
		else
			return null;
	}
	public AccessToken logout() {
		return resetAccessToken();
	}

	public User createUser(String handle) throws TwitterException {
		return mTwitter.showUser(handle);
	}

	public int uploadPic(File file, String message) throws Exception {
		int statusId;
		try {

			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
			builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET_KEY);
			String token = mAccessToken.getToken();
			String secret = mAccessToken.getTokenSecret();
			AccessToken accessTokenUpdate = new AccessToken(token, secret);
			Twitter twitterUpdate = new TwitterFactory(builder.build())
					.getInstance(accessTokenUpdate);

			StatusUpdate status = new StatusUpdate(message);
			status.setMedia(file);
			twitter4j.Status responseStatus = twitterUpdate
					.updateStatus(status);
			statusId = (int) responseStatus.getId();
		} catch (TwitterException e) {
			Log.d("TAG", "Pic Upload error" + e.getErrorMessage());
			throw e;
		}

		return statusId;
	}

	public Status updateTwitterStatus(String twitterHandle) {
		try {
			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
			builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET_KEY);
			String token = mAccessToken.getToken();
			String secret = mAccessToken.getTokenSecret();
			AccessToken accessTokenUpdate = new AccessToken(token, secret);
			Twitter twitterUpdate = new TwitterFactory(builder.build())
					.getInstance(accessTokenUpdate);
			twitter4j.Status responseStatus = twitterUpdate
					.updateStatus(twitterHandle);
			int statusId = (int) responseStatus.getId();
			Log.d(TAG, "updateTwitterStatus :"+statusId);
			return responseStatus;
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void authorize(final Activity activity) throws NullPointerException {
		if (mListener == null) {
			throw new NullPointerException("LoginListener not set.");
		}
		if (isUserLoggedIn()) {
			mListener.loginSucess();
			
			
			
			
			
			return;
		}
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (mProgressDlg != null) {
					mProgressDlg.dismiss();
					mProgressDlg = null;
				}
				mProgressDlg = new ProgressDialog(activity);
				mProgressDlg.setCancelable(false);
				mProgressDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
				mProgressDlg.setMessage("Initializing ...");
				mProgressDlg.show();
			}
		});
		String authUrl = "";
		int what = 1;
		try {
			authUrl = mHttpOauthprovider.retrieveRequestToken(
					mHttpOauthConsumer, CALLBACK_URL);
			what = 0;
			Log.d(TAG, "Request token url " + authUrl);
		} catch (Exception e) {
			Log.d(TAG, "Failed to get request token");
			e.printStackTrace();
		}
		mHandler.sendMessage(mHandler.obtainMessage(what, 1, 0, authUrl));
	}

	public boolean handleMessage(Message msg) {
		mProgressDlg.dismiss();
		if (msg.what == 1) {
			mListener.loginEror(-1);
			mProgressDlg = null;
		} else if (msg.what == 1001) {
			try {
				String handle = msg.getData().getString("handle");
				User user = mTwitter.showUser(handle);
				Callback callback = (Callback) msg.getCallback();
				Message message = new Message();
				message.what = msg.what;
				message.obj = user;
				callback.handleMessage(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if (msg.arg1 == 1) {
				showLoginDialog((String) msg.obj);
			} else {
				mListener.loginSucess();
				mProgressDlg = null;
			}
		}
		return true;
	}

	private void showLoginDialog(String url) {
		final TwDialogListener listener = new TwDialogListener() {
			@Override
			public void onComplete(String value) {
				processToken(value);
			}

			@Override
			public void onError(String value) {
				mListener.loginEror(-1);
			}
		};

		TwitterDialog dialog = new TwitterDialog(mProgressDlg.getContext(),
				url, listener);
		dialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				mListener.loginEror(-1);

			}
		});
		dialog.show();
	}

	public boolean isUserLoggedIn() {
		try {
			if (mAccessToken == null) {
				return false;
			}
			User user = mTwitter.verifyCredentials();
			if (user != null) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private void configureToken() {
		if (mAccessToken != null) {
			try {
				mTwitter.setOAuthConsumer(TWITTER_CONSUMER_KEY,
						TWITTER_CONSUMER_SECRET_KEY);
			} catch (Exception e) {

			}
			mTwitter.setOAuthAccessToken(mAccessToken);
		}
	}

	public void processToken(String callbackUrl) {
		mProgressDlg.setMessage("Finalizing ...");
		// mProgressDlg.show();

		final String verifier = getVerifier(callbackUrl);
		oauthVerifier = getVerifier(callbackUrl);

		new Thread() {
			@Override
			public void run() {
				int what = 1;

				try {
					mHttpOauthprovider.retrieveAccessToken(mHttpOauthConsumer,
							verifier);

					mAccessToken = new AccessToken(
							mHttpOauthConsumer.getToken(),
							mHttpOauthConsumer.getTokenSecret());

					configureToken();

					User user = mTwitter.verifyCredentials();
					storeAccessToken(mAccessToken, user.getName(),
							String.valueOf(user.getProfileImageURL()),
							String.valueOf(user.getId()));

					what = 0;
				} catch (Exception e) {
					Log.d(TAG, "Error getting access token");

					e.printStackTrace();
				}

				mHandler.sendMessage(mHandler.obtainMessage(what, 2, 0));
			}
		}.start();
	}

	private String getVerifier(String callbackUrl) {
		String verifier = "";

		try {
			callbackUrl = callbackUrl.replace("twitterapp", "http");

			URL url = new URL(callbackUrl);
			String query = url.getQuery();

			String array[] = query.split("&");

			for (String parameter : array) {
				String v[] = parameter.split("=");

				if (URLDecoder.decode(v[0], "UTF-8").equals(
						oauth.signpost.OAuth.OAUTH_VERIFIER)) {
					verifier = URLDecoder.decode(v[1], "UTF-8");
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return verifier;
	}
}

interface TwDialogListener {
	public void onComplete(String value);

	public void onError(String value);
}