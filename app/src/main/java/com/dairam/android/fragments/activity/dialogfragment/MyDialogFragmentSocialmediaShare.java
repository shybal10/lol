package com.dairam.android.fragments.activity.dialogfragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.app.ShareCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.twitter.TwiteerInitialization;
import com.dairam.android.fragments.activity.utillities.BitmapUtil;
import com.facebook.FacebookDialog;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.internal.WebDialog;
import com.pinterest.pinit.PinIt;
import com.pinterest.pinit.PinItListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyDialogFragmentSocialmediaShare extends DialogFragment {
	/*private UiLifecycleHelper uiHelper;
	private String prodName, prodDescription, imageString, linkUrl;
	private Bitmap b;

	ProgressDialog mProgressDialog;
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	private boolean pendingPublishReauthorization = false;

	TwiteerInitialization twiteerInitialization;
	public  MyDialogFragmentSocialmediaShare(){

	}

	public  MyDialogFragmentSocialmediaShare newInstance(String shareProdName,
															   String shareProdDescription, String shareImageString,
															   String shareLinkUrl) {

		MyDialogFragmentSocialmediaShare fragment = new MyDialogFragmentSocialmediaShare();
		fragment.prodName = shareProdName;
		fragment.prodDescription = shareProdDescription;
		fragment.imageString = shareImageString;
		fragment.linkUrl = shareLinkUrl;
		return fragment;
	}


	public  MyDialogFragmentSocialmediaShare(String shareProdName,
			String shareProdDescription, String shareImageString,
			String shareLinkUrl) {

		// String title = "My Fragment";
		this.prodName = shareProdName;
		this.prodDescription = shareProdDescription;
		this.imageString = shareImageString;
		this.linkUrl = shareLinkUrl;
		
		Log.e("Dialog Socal Share","Prod Name: "+prodName);
		Log.e("Dialog Socal Share","Prod Desc: "+prodDescription);
		Log.e("Dialog Socal Share","Prod Img: "+imageString);
		Log.e("Dialog Socal Share","Prod linkUrl: "+shareLinkUrl);

		//MyDialogFragmentSocialmedia f = new MyDialogFragmentSocialmedia();

		// * Bundle args = new Bundle(); args.putString("title", title);
		 /*//* f.setArguments(args);

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// String title = getArguments().getString("title");

		final Dialog myDialog = new Dialog(getActivity());
		myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		myDialog.setContentView(R.layout.dialog_layout_social_share);

		// ImageView closeImageView =
		// (ImageView)myDialog.findViewById(R.id.close_btn_dialog_social);
		Button facebookButton = (Button) myDialog
				.findViewById(R.id.facebookbtnsocial);
		Button twitterButton = (Button) myDialog
				.findViewById(R.id.twitterbtnsocial);
		Button instagramButton = (Button) myDialog
				.findViewById(R.id.instagrambtnsocial);
		Button pinterestButton = (Button) myDialog
				.findViewById(R.id.pinterestbtnsocial);
		Button gplusButton = (Button) myDialog
				.findViewById(R.id.gplusbtnsocial);

		LinearLayout layFacebook = (LinearLayout) myDialog
				.findViewById(R.id.linlay1socailmedis);
		LinearLayout layTwitter = (LinearLayout) myDialog
				.findViewById(R.id.linlay1socailmedia2);
		LinearLayout layInsta = (LinearLayout) myDialog
				.findViewById(R.id.linlay1socailmedia3);
		LinearLayout layPint = (LinearLayout) myDialog
				.findViewById(R.id.linlay1socailmedia4);
		LinearLayout layGplus = (LinearLayout) myDialog
				.findViewById(R.id.linlay1socailmedia5);

		*//*
		 * closeImageView.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { myDialog.cancel();
		 * 
		 * } });
		 *//*

		facebookButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.e("FB Button", "Facebook Buton clicked");
				fbShare();
			}
		});
		layFacebook.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.e("FB Button", "Facebook Buton clicked");
				fbShare();
			}
		});

		twitterButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.e("Twitter Button", "Twitter Buton clicked");
				tweetShare();
			}
		});
		layTwitter.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.e("Twitter Button", "Twitter Buton clicked");
				tweetShare();
			}
		});

		instagramButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Log.e("Instagram Error", "Instagram button Clicked");
				instagramShare();
			}
		});
		layInsta.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Log.e("Instagram Error", "Instagram button Clicked");
				instagramShare();
			}
		});

		pinterestButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// Pinterest app id: 1442496
				Log.e("Pinterest Error", "Pinterest button Clicked");
				PinterestShare();
			}
		});
		layPint.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// Pinterest app id: 1442496
				Log.e("Pinterest Error", "Pinterest button Clicked");
				PinterestShare();
			}
		});
		gplusButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// Pinterest app id: 1442496
				Log.e("Pinterest Error", "Pinterest button Clicked");
				googlePlusShare();
			}
		});
		layGplus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// Pinterest app id: 1442496
				Log.e("Pinterest Error", "Pinterest button Clicked");
				googlePlusShare();
			}
		});

		myDialog.getWindow().getAttributes().windowAnimations = R.style.dialoganimation;
		myDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		myDialog.getWindow().setLayout(
				getActivity().getResources().getDisplayMetrics().widthPixels,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		myDialog.show();

		myDialog.getWindow().getAttributes().windowAnimations = R.style.dialoganimation;
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		uiHelper.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			pendingPublishReauthorization = savedInstanceState.getBoolean(
					PENDING_PUBLISH_KEY, false);
		}

		return myDialog;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setDialogPosition();
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	private void setDialogPosition() {
		SharedPreferences sp = getActivity().getSharedPreferences("lan",
				Context.MODE_PRIVATE);
		String lan = sp.getString("lan", "en");

		Window window = this.getDialog().getWindow();
		if (lan.equals("en"))
			window.setGravity(Gravity.BOTTOM | Gravity.LEFT);
		else
			window.setGravity(Gravity.BOTTOM | Gravity.RIGHT);

		WindowManager.LayoutParams params = window.getAttributes();
		params.y = dpToPx(0);
		window.setAttributes(params);
	}

	private int dpToPx(int dp) {
		DisplayMetrics metrics = getActivity().getResources()
				.getDisplayMetrics();
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				metrics);
	}

	@Override
	public void onResume() {
		super.onResume();

		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
		}
		uiHelper.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		super.onSaveInstanceState(outState);
		outState.putBoolean(PENDING_PUBLISH_KEY, pendingPublishReauthorization);
		uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	private void fbShare() {
		if (FacebookDialog.canPresentShareDialog(getActivity(),
				FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
			FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(
					getActivity()).setCaption(prodName)
					.setDescription(prodDescription).setLink(linkUrl)
					.setPicture(imageString).build();

			uiHelper.trackPendingDialogCall(shareDialog.present());
		} else {
			publishStory();
		}
	}

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			Log.e("Session.............", "........" + session.toString());
			onSessionStateChange(session, state, exception);
		}
	};

	private void onSessionStateChange(com.facebook.Session session,
			com.facebook.SessionState state, Exception exception) {

		if (state.isOpened()) {

			if (pendingPublishReauthorization
					&& state.equals(SessionState.OPENED_TOKEN_UPDATED)) {
				pendingPublishReauthorization = false;
				publishStory();
			}
		} else if (state.isClosed()) {
			// shareLyt.setVisibility(View.GONE);
		}
	}

	private void publishStory() {
		Session session = Session.getActiveSession();

		if (session != null && session.isOpened()) {

			publishFeedDialog();
			return;

		} else {
			*//*
			 * Toast.makeText(getActivity(), "Please Login", Toast.LENGTH_SHORT)
			 * .show();
			 *//*
			Session.openActiveSession(getActivity(), true,
					Arrays.asList("public_profile", "user_friends", "email"),
					new Session.StatusCallback() {

						@SuppressWarnings("deprecation")
						@Override
						public void call(Session session, SessionState state,
								Exception exception) {
							Log.e("TAG",
									"Access Token" + session.getAccessToken());
							if (session.isOpened()) {

								Request.executeMeRequestAsync(session,
										new Request.GraphUserCallback() {
											@Override
											public void onCompleted(
													GraphUser user,
													Response response) {
												if (user != null) {
													*//*
													 * Log.i(TAG, "User ID " +
													 * user.getId()); Log.i(TAG,
													 * "Name " +
													 * user.getFirstName());
													 * Log.i(TAG, "Name " +
													 * user.asMap()
													 * .get("email")
													 * .toString());
													 * 
													 * facebookName = user
													 * .getFirstName();
													 * facebookEmail = user
													 * .asMap() .get("email")
													 * .toString();
													 * facebookUserid = user
													 * .getId();
													 * Log.e("Facebook Name",
													 * facebookEmail); new
													 * DownloadJSONFacebook()
													 * .execute();
													 *//*
													publishFeedDialog();
												}
											}
										});
							}
						}
					});
		}

	}

	private void publishFeedDialog() {
		Log.d("FBShare", "publishFeedDialog()");
		Bundle postParams = new Bundle();
		postParams.putString("caption", prodName);
		postParams.putString("description", prodDescription);
		postParams.putString("link", linkUrl);
		postParams.putString("picture", imageString);
		com.facebook.widget.WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(
				getActivity(), Session.getActiveSession(), postParams))
				.setOnCompleteListener(new OnCompleteListener() {

					@Override
					public void onComplete(Bundle values,
							FacebookException error) {
						if (error == null) {
							// When the story is posted, echo the success
							// and the post Id.
							final String postId = values.getString("post_id");
							if (postId != null) {
								*//*
								 * Toast.makeText(getActivity(),
								 * "Posted story, id: "+postId,
								 * Toast.LENGTH_SHORT).show();
								 *//*
								Log.e("Social Share", "Posted in FB id: "
										+ postId);
							} else {
								// User clicked the Cancel button
								*//*
								 * Toast.makeText(getActivity(),
								 * "Publish cancelled",
								 * Toast.LENGTH_SHORT).show();
								 *//*

								Log.e("Social Share", "Publish Canceled");
							}
						} else if (error instanceof FacebookOperationCanceledException) {
							// User clicked the "x" button
							*//*
							 * Toast.makeText(getActivity(),
							 * "Publish cancelled", Toast.LENGTH_SHORT).show();
							 *//*
							Log.e("Social Share", "Publish Canceled");
						} else {
							// Generic, ex: network error
							*//*
							 * Toast.makeText(getActivity(),
							 * "Error posting story",
							 * Toast.LENGTH_SHORT).show();
							 *//*
							Log.e("Social Share", "Error Posting");
						}
					}

				}).build();
		feedDialog.show();
	}

	private void tweetShare() {
		String message;
		message = prodName + "\n" + prodDescription;
		getBitmap();
		if (message.length() > 140)
			Toast.makeText(getActivity(),
					getString(R.string.twtr_share_size_exceeds),
					Toast.LENGTH_SHORT).show();
		if (!intentShareOther(getString(R.string.twitter_package))) {
			if (b != null) {
				twiteerInitialization = new TwiteerInitialization(
						getActivity(), getSlectedBytesArrayForTwitter(b),
						message);
				// twiteerInitialization.getTwitterLogin();
				setTwiteerInitialization(twiteerInitialization);
			}
			Toast.makeText(getActivity(), getString(R.string.no_twitter),
					Toast.LENGTH_SHORT).show();
		}
	}

	private void instagramShare() {
		try {

			Log.e("Instagram Error", "Instagram button Clicked");
			getBitmap();
			File file = getSlectedBytesArrayForTwitter(b);
			if (file != null) {
				Uri uri = Uri.fromFile(getSlectedBytesArrayForTwitter(b));
				Intent shareIntent = new Intent(
						Intent.ACTION_SEND);

				shareIntent.setType("image*//*");
				shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

				shareIntent.setPackage(getString(R.string.inst_package));
				startActivity(shareIntent);
			} else {
				Log.e("Instagram Error", "Image not found");
				Toast.makeText(getActivity(), getString(R.string.no_image),
						Toast.LENGTH_SHORT).show();
			}
		} catch (ActivityNotFoundException e) {
			Log.e("Instagram Error", "Instagram App not found");
			Toast.makeText(getActivity(), getString(R.string.no_instagram),
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {

			Log.e("Instagram Error", "Exception Catched");
			e.printStackTrace();

		}
	}

	private void PinterestShare() {
		PinIt.setPartnerId("1442496");
		PinIt pinIt = new PinIt();

		pinIt.setUrl(linkUrl);
		pinIt.setImageUrl(imageString);
		pinIt.setDescription(prodDescription);
		pinIt.setListener(new PinItListener() {
			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onComplete(boolean completed) {
				super.onComplete(completed);
				if (completed) {
					System.out.println("Pinit complete");
					Log.e("Pinit Complete", "Pinit complete");
				}
			}

			@Override
			public void onException(Exception e) {
				super.onException(e);
				System.out.println("Pinit Exception");
				Log.e("Pinit Error", "Pinit Exception");
				e.printStackTrace();
			}
		});
		pinIt.doPinIt(getActivity());
	}

	private void googlePlusShare() {
		try {
			Log.e("G+ ", "In G+ share");
			getBitmap();
			Log.e("Img GPlus Share","Bitmap: "+b.toString());
			File picFile = getSlectedBytesArrayForTwitter(b);
			Log.e("file name..........", ""+picFile.getTotalSpace());
			Uri pictureUri = Uri.fromFile(picFile);
			Intent shareIntent = ShareCompat.IntentBuilder
					.from(getActivity())
					.setText(prodName + "\n" + prodDescription + "\n" + linkUrl)
					.setType("image/jpeg").setStream(pictureUri).getIntent()
					.setPackage("com.google.android.apps.plus");
			startActivity(shareIntent);
		} catch (Exception e) {
			Log.e("G+ Error ", "Error In G+ share");
			e.printStackTrace();
		}
	}

	private boolean intentShareOther(String sharePackagename) {
		boolean isAppinstalled = false;
		// progressBar.setVisibility(View.VISIBLE);
		List<Intent> targetedShareIntents = new ArrayList<Intent>();
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("image*//*,text*//*");
		List<ResolveInfo> resInfo = getActivity().getPackageManager()
				.queryIntentActivities(shareIntent, 0);
		if (!resInfo.isEmpty()) {
			for (ResolveInfo resolveInfo : resInfo) {
				String packageName = resolveInfo.activityInfo.packageName;
				Intent targetedShareIntent = new Intent(
						Intent.ACTION_SEND);
				targetedShareIntent.setType("image*//*,text*//*");
				targetedShareIntent.putExtra(
						Intent.EXTRA_SUBJECT, prodName);
				if (packageName.equals(sharePackagename)) {
					targetedShareIntent.putExtra(
							Intent.EXTRA_TEXT, prodName + "\n"
									+ prodDescription + "\n" + linkUrl);
					File file = getSlectedBytesArrayForTwitter(b);
					if (file != null) {
						Uri uri = Uri.fromFile(file);
						targetedShareIntent.putExtra(
								Intent.EXTRA_STREAM, uri);
					}
					targetedShareIntent.setPackage(packageName);
					targetedShareIntents.add(targetedShareIntent);
					break;
				} else {
					targetedShareIntent.putExtra(
							Intent.EXTRA_TEXT, prodDescription);
				}

			}
			// progressBar.setVisibility(View.GONE);
			if (targetedShareIntents.size() > 0) {
				isAppinstalled = true;
				Intent chooserIntent = Intent.createChooser(
						targetedShareIntents.remove(0), "Select app to share");

				chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
						targetedShareIntents.toArray(new Parcelable[] {}));

				startActivity(chooserIntent);
			}
		}
		return isAppinstalled;
	}

	public File getSlectedBytesArrayForTwitter(Bitmap image) {
		if (image == null)
			return null;

		File sdCard = Environment.getExternalStorageDirectory();
		File dir = new File(sdCard.getAbsolutePath() + "/Pictures");
		dir.mkdirs();
		File file = new File(dir, "FWPic"+SystemClock.currentThreadTimeMillis()+".png");			
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, baos);
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(file);
			try {
				fileOutputStream.write(baos.toByteArray());
				fileOutputStream.flush();
				fileOutputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return file;

	}

	private void getBitmap() {

		if (BitmapUtil.PICASSO_ENABLED) {
			Log.e("Share Dialog","Bitmap String: "+imageString);
			Picasso.with(getActivity()).load(imageString).into(target);
		}
	}

	public void setTwiteerInitialization(
			TwiteerInitialization twiteerInitialization) {
		this.twiteerInitialization = twiteerInitialization;
	}

	private Target target = new Target() {

		@Override
		public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
			if (bitmap != null) {
				b = bitmap;
				// mProgressDialog.setVisibility(View.GONE);
			}
		}

		@Override
		public void onBitmapFailed(Drawable arg0) {
			// mProgressDialog.setVisibility(View.GONE);
		}

		@Override
		public void onPrepareLoad(Drawable arg0) {
			// progressBar.setVisibility(View.VISIBLE);
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

*/
}
