package com.example.samyaksau.e_bulletin.ebulletin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.example.samyaksau.e_bulletin.R;
import com.example.samyaksau.e_bulletin.helper.AppConstants;

/**
 * @Author   Samyak Sau
 * @College Jaipur National University, Jaipur
 */
public class Splash extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new MyCountDown(3000, 1000).start();
    }

    private class MyCountDown extends CountDownTimer {
        public MyCountDown (long millisInFuture, long countDownInterval){
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            SharedPreferences sp = getSharedPreferences(AppConstants.LOGIN_PREFS, MODE_PRIVATE);
            if (sp.getString(AppConstants.KEY_EMAIL, "").equals("") && sp.getString(AppConstants.KEY_PASSWORD, "").equals("") ){
                startActivity(new Intent(Splash.this, Login.class));
            } else {
                startActivity(new Intent(Splash.this, Home.class));
            }
            finish();
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }
    }
}
