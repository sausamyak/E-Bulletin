package com.example.samyaksau.e_bulletin.ebulletin;

import android.os.Bundle;
import android.view.View;

import com.example.samyaksau.e_bulletin.R;

/**
 * @Author   Samyak Sau
 * @College Jaipur National University, Jaipur
 */
public class AboutUs extends BaseActivity {

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.about_us);
        super.onCreate(savedInstanceState);

        setHeader(getString(R.string.header_about_us));
        initView();
    }

    private void initView() {

        findViewById(R.id.button_about_us).setVisibility(View.INVISIBLE);
        findViewById(R.id.button_logout).setVisibility(View.INVISIBLE);
    }
}
