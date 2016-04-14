package com.example.samyaksau.e_bulletin.ebulletin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.samyaksau.e_bulletin.R;
import com.example.samyaksau.e_bulletin.helper.AlertManager;
import com.example.samyaksau.e_bulletin.helper.AppConstants;
import com.example.samyaksau.e_bulletin.helper.AppValidations;
import com.example.samyaksau.e_bulletin.helper.MethodUtills;
import com.example.samyaksau.e_bulletin.httputil.Action;
import com.example.samyaksau.e_bulletin.httputil.OnHttpTaskListener;
import com.example.samyaksau.e_bulletin.httputil.Request;
import com.example.samyaksau.e_bulletin.httputil.RequestMethod;
import com.example.samyaksau.e_bulletin.httputil.Response;
import com.example.samyaksau.e_bulletin.result.GetLoginResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * @Author   Samyak Sau
 * @College Jaipur National University, Jaipur
 */
public class Login extends BaseActivity implements OnHttpTaskListener {

    private EditText eEmail, ePassword;
    private Button bLogin, bRegistration;
    private TextView tvForgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        setHeader(getString(R.string.header_login));
        initView();
    }

    private void initView() {

        findViewById(R.id.button_logout).setVisibility(View.INVISIBLE);
        eEmail = (EditText)findViewById(R.id.edittext_login_email);
        ePassword = (EditText)findViewById(R.id.edittext_login_password);
        bLogin = (Button)findViewById(R.id.button_login_login);
        bRegistration = (Button)findViewById(R.id.button_login_registration);
        tvForgetPassword = (TextView)findViewById(R.id.textview_forget_password);

        bLogin.setOnClickListener(this);
        bRegistration.setOnClickListener(this);
        tvForgetPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {
            case R.id.button_login_login:
                eEmail.setError(null);
                ePassword.setError(null);

                if (!AppValidations.isNetworkConnected(this)) {
                    AlertManager.showDialog(this, getString(R.string.alert_title), AppConstants.NETWORK_ERROR, getString(R.string.ok));
                    return;
                }

                if (TextUtils.isEmpty(eEmail.getText())) {
                    eEmail.setError(getString(R.string.v_email));
                    eEmail.requestFocus();
                    return;
                }

                if (!AppValidations.checkEmail(eEmail.getText().toString())) {
                    eEmail.setError(getString(R.string.v_email_invalid));
                    eEmail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(ePassword.getText())) {
                    ePassword.setError(getString(R.string.v_password));
                    ePassword.requestFocus();
                    return;
                }

                sendHttpRequest(Action.LOGIN, eEmail.getText().toString(), ePassword.getText().toString());
                break;

            /*****************************************************/
            case R.id.button_login_registration:
                startActivity(new Intent(getApplicationContext(), Registration.class));
                setDefault();
                break;

            /*****************************************************/
            case R.id.textview_forget_password:
                startActivity(new Intent(getApplicationContext(), ResetPassword.class));
                setDefault();
                break;
        }
    }

    private void setDefault(){
        eEmail.setText("");
        ePassword.setText("");
        eEmail.requestFocus();
    }

    @Override
    public void sendHttpRequest(Action ac, String... param) {
        showProgressBar();
        Request request = new Request(ac, this);
        request.addParam("email", param[0]);
        request.addParam("password", param[1]);
        request.execute(RequestMethod.GET, ac.toString());
    }

    @Override
    public void OnReceiveResponse(Action ac, Response response) {
        if (response.isSuccess()) {
            try {
                Gson gson = new Gson();
                Type type = new TypeToken<GetLoginResult>() {}.getType();

                GetLoginResult loginResult = gson.fromJson(response.getResult(), type);

                if (loginResult.success.trim().equalsIgnoreCase("true")){
                    startActivity(new Intent(getApplicationContext(), Home.class));

                    MethodUtills.setLoginPrefs(this, eEmail.getText().toString(), ePassword.getText().toString());
                    showToast(getString(R.string.welcome));
                    setDefault();
                    finish();
                } else {
                    showToast(loginResult.message);
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
