package com.example.samyaksau.e_bulletin.ebulletin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.samyaksau.e_bulletin.R;

/**
 * @Author   Samyak Sau
 * @College Jaipur National University, Jaipur
 */
public class Home extends BaseActivity {

    private ImageButton ibViewNotice, ibPostNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        setHeader(getString(R.string.header_home));
        initView();
    }

    private void initView() {
        ibViewNotice = (ImageButton)findViewById(R.id.image_button_view_notice);
        ibPostNotice = (ImageButton)findViewById(R.id.image_button_post_notice);

        ibViewNotice.setOnClickListener(this);
        ibPostNotice.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()){
            case R.id.image_button_view_notice:
                startActivity(new Intent(getApplicationContext(), NoticeActivity.class));
                break;

            /*******************************************/
            case R.id.image_button_post_notice:
                startActivity( new Intent(getApplicationContext(), PostNotice.class));
                break;
        }
    }
}
