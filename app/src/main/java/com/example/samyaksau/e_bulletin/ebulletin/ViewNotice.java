package com.example.samyaksau.e_bulletin.ebulletin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.samyaksau.e_bulletin.R;
import com.example.samyaksau.e_bulletin.details.NoticeDetail;
import com.example.samyaksau.e_bulletin.helper.AppConstants;

/**
 * @Author   Samyak Sau
 * @College Jaipur National University, Jaipur
 */
public class ViewNotice extends BaseActivity {

    private NoticeDetail noticeDetail = null;
    private TextView tvTitle, tvDescription, tvTimeDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_notice);

        setHeader(getString(R.string.header_view_notice));
        if (getIntent() != null) {
            noticeDetail = (NoticeDetail)getIntent().getSerializableExtra(AppConstants.KEY_NOTICE);
        }
        initView();
    }

    private void initView() {
        tvTitle = (TextView)findViewById(R.id.textview_notice_detail_title);
        tvDescription = (TextView)findViewById(R.id.textview_notice_detail_description);
        tvTimeDate = (TextView)findViewById(R.id.textview_notice_detail_time_and_date);

        if (noticeDetail != null) {

            tvTitle.setText(noticeDetail.getTitle());
            tvDescription.setText(noticeDetail.getDescription());
            tvTimeDate.setText(noticeDetail.getDate() + " " + noticeDetail.getTime());
        }
    }

    @Override
    public void onBackPressed() {
        if (noticeDetail != null){
            if (noticeDetail.getNoticeId() != null) {
                if (noticeDetail.getNoticeId().toString().equalsIgnoreCase(GCMIntentService.NOTICE_ID)) {
                    Intent i = new Intent(ViewNotice.this, NoticeActivity.class);

                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            }
        }
        super.onBackPressed();
    }
}
