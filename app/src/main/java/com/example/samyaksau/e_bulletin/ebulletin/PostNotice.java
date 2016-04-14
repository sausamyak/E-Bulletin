package com.example.samyaksau.e_bulletin.ebulletin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.samyaksau.e_bulletin.R;
import com.example.samyaksau.e_bulletin.helper.MLog;
import com.example.samyaksau.e_bulletin.httputil.Action;
import com.example.samyaksau.e_bulletin.httputil.HttpConst;
import com.example.samyaksau.e_bulletin.httputil.OnHttpTaskListener;
import com.example.samyaksau.e_bulletin.httputil.Request;
import com.example.samyaksau.e_bulletin.httputil.RequestMethod;
import com.example.samyaksau.e_bulletin.httputil.Response;
import com.example.samyaksau.e_bulletin.result.GetPostNoticeResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Calendar;

/**
 * @Author   Samyak Sau
 * @College Jaipur National University, Jaipur
 */
public class PostNotice extends BaseActivity implements OnHttpTaskListener {

    private static int DATE_PICKER = 0, TIME_PICKER = 1;
    private EditText eTitle, eDiscription, eDate, eTime;
    private Button bPostNotice, bCancel;
    private int yy, mm, dd;
    private int hh, min;
    private DatePickerDialog.OnDateSetListener odl;
    private TimePickerDialog.OnTimeSetListener otl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_notice);

        setHeader(getString(R.string.header_post_notice));
        initView();
    }

    private void initView() {
        eTitle = (EditText)findViewById(R.id.edittext_post_notice_title);
        eDiscription = (EditText)findViewById(R.id.edittext_post_notice_description);
        eDate = (EditText)findViewById(R.id.edittext_post_notice_date);
        eTime = (EditText)findViewById(R.id.edittext_post_notice_time);
        bPostNotice = (Button)findViewById(R.id.button_post_notice_submit);
        bCancel = (Button)findViewById(R.id.button_post_notice_cancel);

        bPostNotice.setOnClickListener(this);
        bCancel.setOnClickListener(this);
        eDate.setOnClickListener(this);
        eDate.setFocusable(false);
        eDate.setOnTouchListener(null);
        eTime.setOnClickListener(this);
        eTime.setFocusable(false);
        eTime.setOnTouchListener(null);

        setTimeAndDate();
    }

    private void setTimeAndDate() {
        Calendar c = Calendar.getInstance();
        yy = c.get(Calendar.YEAR);
        mm = c.get(Calendar.MONTH);
        dd = c.get(Calendar.DAY_OF_MONTH);

        hh = c.get(Calendar.HOUR);
        min = c.get(Calendar.MINUTE);

        odl = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                yy = year;
                mm = monthOfYear;
                dd = dayOfMonth;
                eDate.setText(getPadNo(dd) + "/" + (getPadNo(mm+1)) + "/" + getPadNo(yy));
            }
        };

        otl = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hh = hourOfDay;
                mm = minute;
                eTime.setText(getPadNo(hh) + ":" + getPadNo(mm));
            }
        };
    }

    private String getPadNo (int number) {
        if (number < 10) {
            return "0" + number;
        } else {
            return "" +number;
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {
            case R.id.button_post_notice_submit:
                eTitle.setError(null);
                eDiscription.setError(null);
                eDate.setError(null);
                eTime.setError(null);

                if (TextUtils.isEmpty(eTitle.getText())) {
                    eTitle.setError(getString(R.string.v_title));
                    eTitle.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(eDiscription.getText())) {
                    eDiscription.setError(getString(R.string.v_discription));
                    eDiscription.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(eDate.getText())) {
                    eDate.setError(getString(R.string.v_date));
                    eDate.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(eTime.getText())) {
                    eTime.setError(getString(R.string.v_time));
                    eTime.requestFocus();
                    return;
                }

                sendHttpRequest(Action.POST_NOTICE, eTitle.getText().toString(), eDiscription.getText().toString(), eDate.getText().toString(), eTime.getText().toString());
                break;

            /***********************************************/
            case R.id.button_post_notice_cancel:
                finish();
                break;

            /***********************************************/
            case R.id.edittext_post_notice_date:
                showDialog(DATE_PICKER);
                break;

            /**********************************************/
            case R.id.edittext_post_notice_time:
                showDialog(TIME_PICKER);
                break;
        }
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        if (id == DATE_PICKER) {
            return  new DatePickerDialog(this, odl, yy, mm, dd);
        } else {
            return  new TimePickerDialog(this, otl, hh, min,true);
        }
    }

    @Override
    public void sendHttpRequest(Action ac, String... param) {

        Request request = new Request(ac, this);
        switch (ac) {
            case POST_NOTICE:
                showProgressBar();
                request.addParam("tile", param[0]);
                request.addParam("description", param[1]);
                request.addParam("date", param[2]);
                request.addParam("time", param[3]);
                request.execute(RequestMethod.GET, ac.toString());
                break;

            case GCM_BROADCASTING:
                request.addParam("title", param[0]);
                request.addParam("description", param[1]);
                request.addParam("date", param[2]);
                request.addParam("time", param[3]);
                request.execute(RequestMethod.GET, HttpConst.PUSH_NOTIFICATION);
                break;
        }
    }

    @Override
    public void OnReceiveResponse(Action ac, Response response) {

        if (response.isSuccess()) {
            try {
                Gson gson = new Gson();
                switch (ac) {
                    case POST_NOTICE:
                        Type type = new TypeToken<GetPostNoticeResult>() {}.getType();
                        GetPostNoticeResult postNoticeResult = gson.fromJson(response.getResult(), type);
                        if (postNoticeResult.message.trim().equalsIgnoreCase("true")) {
                            startActivity(new Intent(getApplicationContext(), NoticeActivity.class));
                            showToast(postNoticeResult.message);

                            sendHttpRequest(Action.GCM_BROADCASTING, eTitle.getText().toString(), eDiscription.getText().toString(), eDate.getText().toString(), eTime.getText().toString());
                            finish();
                        } else {
                            showToast(postNoticeResult.message);
                        }
                        break;

                    case GCM_BROADCASTING:
                        MLog.v("Notification", "" + response.getResult());
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToast(e.getMessage());
            }
        } else {
            showToast(response.getErromMessage());
        }
        dismissProgressBar();
    }
}
