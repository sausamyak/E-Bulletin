package com.example.samyaksau.e_bulletin.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @Author   Samyak Sau
 * @College Jaipur National University, Jaipur
 */
public class MethodUtills {

    public static void setLoginPrefs(Context context, String email, String password) {

        SharedPreferences sp = context.getSharedPreferences(AppConstants.LOGIN_PREFS,0);
        SharedPreferences.Editor et = sp.edit();
        et.putString(AppConstants.KEY_EMAIL, email);
        et.putString(AppConstants.KEY_PASSWORD, password);
        et.commit();
    }
}
