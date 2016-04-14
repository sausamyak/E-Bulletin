package com.example.samyaksau.e_bulletin.ebulletin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.samyaksau.e_bulletin.R;
import com.example.samyaksau.e_bulletin.helper.AppValidations;
import com.example.samyaksau.e_bulletin.httputil.Action;
import com.example.samyaksau.e_bulletin.httputil.OnHttpTaskListener;
import com.example.samyaksau.e_bulletin.httputil.Request;
import com.example.samyaksau.e_bulletin.httputil.RequestMethod;
import com.example.samyaksau.e_bulletin.httputil.Response;
import com.example.samyaksau.e_bulletin.result.GetLoginResult;
import com.example.samyaksau.e_bulletin.result.GetPasswordResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * @Author   Samyak Sau
 * @College Jaipur National University, Jaipur
 */
public class ResetPassword extends BaseActivity implements OnHttpTaskListener {

    private EditText eEmail, ePassword;
    private Button bReset, bCancel;
    private LinearLayout layoutPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        setHeader(getString(R.string.header_reset_password));
        initView();
    }

    private void initView() {
        eEmail = (EditText)findViewById(R.id.edittext_reset_email);
        ePassword = (EditText)findViewById(R.id.edittext_reset_password);
        layoutPassword = (LinearLayout)findViewById(R.id.layout_password);
        bReset = (Button)findViewById(R.id.button_reset_password);
        bCancel = (Button)findViewById(R.id.button_reset_cancel);

        bReset.setOnClickListener(this);
        bCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {
            case R.id.button_reset_password:

                if (bReset.getText().toString().equalsIgnoreCase(getString(R.string.reset))){

                    eEmail.setError(null);

                    if (TextUtils.isEmpty(eEmail.getText())){
                        eEmail.setError(getString(R.string.v_email));
                        eEmail.requestFocus();
                        return;
                    }

                    if (!AppValidations.checkEmail(eEmail.getText().toString())) {
                        eEmail.setError(getString(R.string.v_email_invalid));
                        eEmail.requestFocus();
                        return;
                    }

                    sendHttpRequest(Action.SEND_PASSWORD, eEmail.getText().toString());
                } else {
                    ePassword.setError(null);

                    if (TextUtils.isEmpty(eEmail.getText())) {
                        eEmail.setError(getString(R.string.v_password));
                        ePassword.requestFocus();
                        return;
                    }

                    sendHttpRequest(Action.LOGIN, eEmail.getText().toString(), ePassword.getText().toString());
                }
                break;

            /***********************************************************************************/
            case R.id.button_reset_cancel:
                finish();
                break;
        }
    }

    @Override
    public void sendHttpRequest(Action ac, String... param) {
        showProgressBar();
        Request request = new Request(ac, this);
        switch (ac) {
            case SEND_PASSWORD:
                request.addParam("email", param[0]);
                request.execute(RequestMethod.GET, ac.toString());
                break;

            case LOGIN:
                request.addParam("email", param[0]);
                request.addParam("password", param[1]);
                request.execute(RequestMethod.GET, ac.toString());
                break;
        }
    }

    @Override
    public void OnReceiveResponse(Action ac, Response response) {
        if (response.isSuccess()) {
            try {

                Gson gson = new Gson();
                switch (ac) {
                    case SEND_PASSWORD:

                        Type typeSendPassword = new TypeToken<GetPasswordResult>() {}.getType();

                        GetPasswordResult sendPasswordResult = gson.fromJson(response.getResult(), typeSendPassword);

                        if (sendPasswordResult.success.trim().equalsIgnoreCase("true")) {
                            showToast(sendPasswordResult.message);

                            layoutPassword.setVisibility(View.VISIBLE);
                            eEmail.setFocusable(false);
                            eEmail.setOnTouchListener(null);
                            eEmail.setCursorVisible(false);
                            bReset.setText(getString(R.string.sign_in));
                        } else {
                            showToast(sendPasswordResult.message);
                        }
                        break;

                    case LOGIN:
                        Type typeLogin = new TypeToken<GetLoginResult>() {}.getType();

                        GetLoginResult loginResult = gson.fromJson(response.getResult(), typeLogin);

                        if (loginResult.success.trim().equalsIgnoreCase("true")) {
                            startActivity(new Intent(getApplicationContext(), Home.class));

                            showToast(getString(R.string.welcome));
                            finish();
                        } else {
                            showToast(loginResult.message);
                        }
                        break;
                }

            } catch (Exception e ) {
                e.printStackTrace();
                showToast(e.getMessage());
            }
        } else {
            showToast(response.getErromMessage());
        }
        dismissProgressBar();
    }
}
