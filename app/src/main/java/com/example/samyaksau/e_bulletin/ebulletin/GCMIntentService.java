package com.example.samyaksau.e_bulletin.ebulletin;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.samyaksau.e_bulletin.R;
import com.example.samyaksau.e_bulletin.details.NoticeDetail;
import com.example.samyaksau.e_bulletin.gcm.Controller;
import com.example.samyaksau.e_bulletin.helper.AppConstants;
import com.google.android.gcm.GCMBaseIntentService;

/**
 * @Author   Samyak Sau
 * @College Jaipur National University, Jaipur
 */
public class GCMIntentService extends GCMBaseIntentService {

    public static final String NOTICE_ID = "0";
    private static int notificationCount = 0;
    private static final String TAG = "GCMIntentService";
    private Controller aController = null;

    private String title = "", description = "", date = "", time = "";

    public GCMIntentService() {
        super(AppConstants.GOOGLE_SENDER_ID);
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
        try {
            if (aController == null)aController = (Controller) getApplicationContext();
            Log.i(TAG, "Device registered: regId = " + registrationId);
            aController.displayMessageOnScreen(context, getString(R.string.welcome));
            SharedPreferences pref = getSharedPreferences("RunningState", MODE_PRIVATE);
            aController.register(context, pref.getString("Name", ""), pref.getString("Email", ""),pref.getString("Contact", ""), registrationId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        if (aController == null)aController = (Controller)getApplicationContext();
        Log.i(TAG, "Device unregistered");
        aController.displayMessageOnScreen(context, getString(R.string.gcm_unregistered));
        aController.unregister(context, registrationId);
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        if (aController == null)aController = (Controller)getApplicationContext();
        Log.i(TAG, "Received message");
        title = intent.getExtras().getString("title");
        description = intent.getExtras().getString("description");
        date = intent.getExtras().getString("date");
        time = intent.getExtras().getString("time");
        aController.displayMessageOnScreen(context, description);
        // notifies user
        generateNotification(context, title, description, date, time);
    }

    @Override
    protected void onDeletedMessages(Context context, int total) {
        if (aController == null)aController = (Controller)getApplicationContext();
        Log.i(TAG, "Received deleted message notification");
        String message = getString(R.string.gcm_deleted, total);
        aController.displayMessageOnScreen(context, message);
        // notifies user
        generateNotification(context, title, message, date, time);
    }

    @Override
    protected void onError(Context context, String errorId) {
        if (aController == null)aController = (Controller)getApplicationContext();
        Log.i(TAG, "Received error: " + errorId);

        aController.displayMessageOnScreen(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        if (aController == null)aController = (Controller)getApplicationContext();
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        aController.displayMessageOnScreen(context, getString(R.string.gcm_recoverable_error, errorId));
        return super.onRecoverableError(context, errorId);
    }

    private static void generateNotification(Context context, String title, String description, String date, String time) {
        int icon = AppConstants.ICON;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, description, when);
        if (description == null)description = AppConstants.FIRST_MSG;
        if (title == null)title = AppConstants.TAG;
        NoticeDetail noticeDetail = new NoticeDetail();
        noticeDetail.setNoticeId(NOTICE_ID);
        noticeDetail.setTitle(title);
        noticeDetail.setDescription(description);
        noticeDetail.setDate(date);
        noticeDetail.setTime(time);
        Intent notificationIntent = null;

        if (description.equalsIgnoreCase(AppConstants.FIRST_MSG)) {
            notificationIntent = new Intent(context, Splash.class);
        } else {
            notificationIntent = new Intent(context, ViewNotice.class).putExtra(AppConstants.KEY_NOTICE, noticeDetail);
        }
        // set intent so it does not start a new activity

        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        Notification.Builder builder = new Notification.Builder(context).setContentTitle(title).setContentText(description).setContentIntent(intent);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        notification = builder.build();

      // According to older API.
      //  notification.setLatestEventInfo(context, title, description, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;
        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(notificationCount, notification);
        notificationCount++;

    }
}
