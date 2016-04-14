package com.example.samyaksau.e_bulletin.helper;

import android.app.AlertDialog;
import android.content.Context;

/**
 * @Author  Samyak Sau
 * @College Jaipur National University, Jaipur
 */
public class AlertManager {
    public static void showDialog(final Context context, String title, String msg, String btn) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        // alert.setIcon(R.drawable.app_logo);
        alert.setTitle(title);
        alert.setMessage(msg);
        alert.setPositiveButton(btn, null);
        alert.show();
    }
}
