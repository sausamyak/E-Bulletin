package com.example.samyaksau.e_bulletin.helper;

import com.example.samyaksau.e_bulletin.R;

/**
 * @Author   Samyak Sau
 * @College Jaipur National University, Jaipur
 */
public class AppConstants {
    public static final String KEY_NOTICE = "notice_detail";
    public static final String LOGIN_PREFS = "loginPrefs";
    public static final String KEY_EMAIL = "Email";
    public static final String KEY_PASSWORD = "Password";

    public static String NETWORK_ERROR = "Please connect to working Internet connection !";

    /***********************************  GCM  *****************************************/
    // Google Project id
    public static final String GOOGLE_SENDER_ID = "305257965416";
    // Place  here your Google project id

    public static final int ICON = R.drawable.app_logo;
    public static final String TAG = "eBulletin";
    public static final String FIRST_MSG = "Welcome's you to e-Bulletin App !";
    public static final String DISPLAY_MESSAGE_ACTION = "com.example.samyaksau.e_bulletin.gcm.DISPLAY_MESSAGE";
    public static final String EXTRA_MESSAGE = "message";
    public static final String MAIN_PACKAGE ="com.example.samyaksau.e_bulletin.ebulletin";
}
