package com.example.samyaksau.e_bulletin.ebulletin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samyaksau.e_bulletin.R;
import com.example.samyaksau.e_bulletin.helper.Anim;
import com.example.samyaksau.e_bulletin.helper.MethodUtills;

/**
 * @Author   Samyak Sau
 * @College Jaipur National University, Jaipur
 */
public class BaseActivity extends FragmentActivity implements View.OnClickListener {

    protected ProgressDialog dialog;
    protected Context context;
    private Handler handler = new Handler();
    public Button bContactUs, bTalk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        context = BaseActivity.this;
        // prepare for a progress bar dialog
        dialog = new ProgressDialog(context);
        dialog.setTitle(getString(R.string.loading_title));
        dialog.setMessage(getString(R.string.loading_msg));
        dialog.setCancelable(true);
        dialog.setIndeterminate(true);
    }

    public void setHeader(String string) {
        ((TextView)findViewById(R.id.tv_header)).setText(string);
    }

    public void onBackClick(View view) {
        Anim.runAlphaAnimation(this, view);
        finish();
    }

    public void showProgressBar() {
        try {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (dialog != null && !dialog.isShowing())
                        dialog.show();
                }
            }, 0 );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissProgressBar() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        try {
            Anim.runAlphaAnimation(this, view);
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showToast (String string) {
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
    }

    public void onHeaderButtonClick(View v) {
        Anim.runAlphaAnimation(this, v);
        switch (v.getId()) {
            case R.id.button_about_us :
                startActivity(new Intent(getApplicationContext(), AboutUs.class));
                break;
            case R.id.button_logout :
                showLogoutDialog(this);
                break;
        }
    }

    private void showLogoutDialog (final Context context) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this).setTitle(getString(R.string.alert_title)).setMessage(getString(R.string.logout_message)).setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                MethodUtills.setLoginPrefs(context, "","");
                Intent i = new Intent(context, Login.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        }).setNegativeButton(getString(R.string.no), null);
        alert.show();
    }
}
