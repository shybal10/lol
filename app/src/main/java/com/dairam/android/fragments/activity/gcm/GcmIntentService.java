package com.dairam.android.fragments.activity.gcm;

/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.MainActivity;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.FileOutputStream;
/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService {

	String FILENAMENOTIFICATION = "notificationLog.txt";

	public static final int NOTIFICATION_ID = 0;
	private int notificationCount = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	OnClickListener onClickListener;

	String id;
	String data;
 
	public GcmIntentService() {
		super("GcmIntentService:");
		Log.i(TAG, "GcmIntentService: ");
	}

	public static final String TAG = "GCM Demo";

	@Override
	protected void onHandleIntent(Intent intent) {
		/*if (U3an.isActivityVisible()) {*/
			/*Bundle extras = intent.getExtras();
			data = extras.getString("payload");
			id = extras.getString("id");*/
			/*sendAlert();*/
		/*} else {*/
			Bundle extras = intent.getExtras();
			if (extras != null && !extras.isEmpty()) {

				GoogleCloudMessaging gcm = GoogleCloudMessaging
						.getInstance(this);
				String messageType = gcm.getMessageType(intent);

				// Log.i(TAG, "gcm: " +
				// gcm.toString()+"messageType"+messageType);
				Log.e(TAG, "Bundle gcm data : " + extras);

				if (messageType != null) { // has effect of unparcelling Bundle
					/*
					 * Filter messages based on message type. Since it is likely
					 * that GCM will be extended in the future with new message
					 * types, just ignore any message types you're not
					 * interested in, or that you don't recognize.
					 */
					if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
							.equals(messageType)) {
						sendNotification("Send error: " + extras.toString());
					} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
							.equals(messageType)) {
						sendNotification("Deleted messages on server: "
								+ extras.toString());
						// If it's a regular GCM message, do some work.
					} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
							.equals(messageType)) {
						// This loop represents the service doing some work.
						for (int i = 0; i < 2; i++) {
							Log.i(TAG, "Working... " + (i + 1) + "/2 @ "
									+ SystemClock.elapsedRealtime());
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
							}
						}
						Log.i(TAG,
								"Completed work @ "
										+ SystemClock.elapsedRealtime());

						// if (extras.getString("type").equals("post")) {
						// Log.i(TAG,"from :"+extras.getString("from"));
						Log.e(TAG, "payload :" + extras.toString());
						// Log.i(TAG,"android.support.content.wakelockid :"+extras.getInt("android.support.content.wakelockid"));
						// Log.i(TAG,"collapse_key :"+extras.getString("collapse_key"));
						
						data = extras.getString("payload");
						id = extras.getString("id");
						sendNotification(extras.getString("payload"));
					
					}
				}
			}
			// Release the wake lock provided by the WakefulBroadcastReceiver.
			GcmBroadcastReceiver.completeWakefulIntent(intent);
		}
	/*}*/

	/*// Alert Message if application is open
	private void sendAlert() {
		WriteDataToFile();
		final Activity currActivity = BaseActivity.getBaseActivity();
		currActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Utilities.show(currActivity, data, "New Notification",
						okClicked);
			}
		});
	}*/

	private OnClickListener okClicked = new OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.cancel();
		//BaseActivity.finishAndRestart(id+data);
		}
	};
	
	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	private void sendNotification(String msg) {
		WriteDataToFile();
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("notificationMsg", id + data);
		// intent.putExtra("TYPE", type);
		// intent.putExtra("ID", id);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack
		stackBuilder.addParentStack(MainActivity.class);
		// Adds the Intent to the top of the stack
		stackBuilder.addNextIntent(intent);
		// Gets a PendingIntent containing the entire back stack
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_CANCEL_CURRENT);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.dmicon)
				.setContentTitle("Dairam")
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setAutoCancel(true).setDefaults(Notification.DEFAULT_SOUND)
				.setDefaults(Notification.DEFAULT_VIBRATE).setContentText(msg)
				.setNumber(notificationCount++);
		mBuilder.setContentIntent(resultPendingIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}

	private void WriteDataToFile() {
		String data = "Notification";
		FileOutputStream fosdummyy;
		try {
			fosdummyy = openFileOutput(FILENAMENOTIFICATION,
					Context.MODE_PRIVATE);
			fosdummyy.write(data.getBytes());
			fosdummyy.close();
		} catch (Exception e) {

		}
	}
}
