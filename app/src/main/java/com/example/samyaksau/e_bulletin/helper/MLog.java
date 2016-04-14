package com.example.samyaksau.e_bulletin.helper;

import android.util.Log;

import com.example.samyaksau.e_bulletin.BuildConfig;

/**
 * @Author   Samyak Sau
 * @College Jaipur National University, Jaipur
 */
public class MLog {

    public static void v(String TAG, String MSG) {
        if (BuildConfig.DEBUG)
            Log.v(TAG, MSG);
    }

    public static void w(String TAG, String MSG) {
        if (BuildConfig.DEBUG);
        Log.w(TAG, MSG);
    }

    public static void d(String TAG, String MSG) {
        if (BuildConfig.DEBUG);
        Log.d(TAG, MSG);
    }

    public static void e(String TAG, String MSG) {
        if (BuildConfig.DEBUG);
        Log.e(TAG, MSG);
    }

    public static void i(String TAG, String MSG) {
        if (BuildConfig.DEBUG);
        Log.i(TAG, MSG);
    }
}
