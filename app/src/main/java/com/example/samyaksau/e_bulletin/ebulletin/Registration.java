package com.example.samyaksau.e_bulletin.ebulletin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.samyaksau.e_bulletin.R;
import com.example.samyaksau.e_bulletin.gcm.Controller;
import com.example.samyaksau.e_bulletin.helper.AlertManager;
import com.example.samyaksau.e_bulletin.helper.AppConstants;
import com.example.samyaksau.e_bulletin.helper.AppValidations;
import com.example.samyaksau.e_bulletin.helper.MLog;
import com.example.samyaksau.e_bulletin.helper.MethodUtills;
import com.example.samyaksau.e_bulletin.httputil.Action;
import com.example.samyaksau.e_bulletin.httputil.HttpConst;
import com.example.samyaksau.e_bulletin.httputil.OnHttpTaskListener;
import com.example.samyaksau.e_bulletin.httputil.Request;
import com.example.samyaksau.e_bulletin.httputil.RequestMethod;
import com.example.samyaksau.e_bulletin.httputil.Response;
import com.example.samyaksau.e_bulletin.result.GetRegistrationResult;
import com.google.android.gcm.GCMRegistrar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * @Author   Samyak Sau
 * @College Jaipur National University, Jaipur
 */
public class Registration extends BaseActivity implements OnHttpTaskListener {

    private EditText eFirstName, eLastName, eEmail, eContact, eUserName, ePassword, eConfirmPassword;
    private RadioButton rbFaculty, rbStudent;
    private Button bLogin, bCancel;

    //GCM
    private Controller aController;
    private AsyncTask <Void, Void, Void> mRegisterTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        setHeader(getString(R.string.header_registration));
        initView();
        gcmRegistration();
    }

    private void initView() {

        findViewById(R.id.button_logout).setVisibility(View.INVISIBLE);

        eFirstName = (EditText)findViewById(R.id.edittext_registration_first_name);
        eLastName = (EditText)findViewById(R.id.edittext_registration_last_name);
        eEmail = (EditText)findViewById(R.id.edittext_registration_email);
        eContact = (EditText)findViewById(R.id.edittext_registration_contact);
        eUserName = (EditText)findViewById(R.id.edittext_registration_username);
        ePassword = (EditText)findViewById(R.id.edittext_registration_password);
        eConfirmPassword = (EditText)findViewById(R.id.edittext_registration_confirm_password);
        rbFaculty = (RadioButton)findViewById(R.id.radiobutton_registration_faculty);
        rbStudent = (RadioButton)findViewById(R.id.radiobutton_registration_student);
        bLogin = (Button)findViewById(R.id.button_registration_login);
        bCancel = (Button)findViewById(R.id.button_registration_cancel);

        bLogin.setOnClickListener(this);
        bCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {
            case R.id.button_registration_login:
                eLastName.setError(null);
                eLastName.setError(null);
                eEmail.setError(null);
                eUserName.setError(null);
                ePassword.setError(null);
                eConfirmPassword.setError(null);

                if (!AppValidations.isNetworkConnected(this)){
                    AlertManager.showDialog(this, getString(R.string.alert_title), AppConstants.NETWORK_ERROR, getString(R.string.ok));
                    return;
                }

                if (TextUtils.isEmpty(eFirstName.getText())) {
                    eFirstName.setError(getString(R.string.v_first_name));
                    eFirstName.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(eLastName.getText())) {
                    eLastName.setError(getString(R.string.v_last_name));
                    eLastName.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(eContact.getText())) {
                    eContact.setError(getString(R.string.v_contact));
                    eContact.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(eUserName.getText())) {
                    eUserName.setError(getString(R.string.v_username));
                    eUserName.requestFocus();
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
                    ePassword.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(eConfirmPassword.getText())) {
                    eConfirmPassword.requestFocus();
                    return;
                }

                if (!ePassword.getText().toString().equals(eConfirmPassword.getText().toString())) {
                    showToast(getString(R.string.v_password_invalid));
                    return;
                }

                String disignation;
                if (rbStudent.isChecked()) {
                    disignation = rbStudent.getText().toString();
                } else {
                    disignation = rbFaculty.getText().toString();
                }

                sendHttpRequest(Action.REGISTRATION, eFirstName.getText().toString(), eLastName.getText().toString(), eEmail.getText().toString(), eContact.getText().toString(), eUserName.getText().toString(), ePassword.getText().toString(), disignation);
                break;

            /*************************************************************************/
            case R.id.button_registration_cancel:
                finish();
                break;
        }
    }

    @Override
    public void sendHttpRequest(Action ac, String... param) {
        showProgressBar();
        Request request = new Request(ac, this);
        request.addParam("firstName", param[0]);
        request.addParam("lastName", param[1]);
        request.addParam("email", param[2]);
        request.addParam("contact", param[3]);
        request.addParam("userName", param[4]);
        request.addParam("password", param[5]);
        request.addParam("designation", param[6]);
        request.execute(RequestMethod.GET, ac.toString());
    }

    @Override
    public void OnReceiveResponse(Action ac, Response response) {
        if (response.isSuccess()) {
            try {
                Gson gson = new Gson();
                Type type = new TypeToken<GetRegistrationResult>() {}.getType();

                GetRegistrationResult resResult = gson.fromJson(response.getResult(), type);

                if (resResult.success.trim().equalsIgnoreCase("true")) {
                    startActivity(new Intent(getApplicationContext(), Home.class));
                    MethodUtills.setLoginPrefs(this, eEmail.getText().toString(), ePassword.getText().toString());
                    finish();

                    showToast(getString(R.string.welcome));
                    setSharedPrefs();
                } else {
                    showToast(resResult.message);
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToast("This email-id is not available !");
            }
        } else {
            showToast(response.getErromMessage());
        }
        dismissProgressBar();
    }

    /***********************************************************************************/
    private void gcmRegistration() {
        // Get Global Controller Class object (see application tag in AndroidManifest.xml)
        final Controller aController = (Controller) getApplicationContext();
        // Check if Internet Connection present
        if (!aController.isConnectingToInternet()) {
            // Internet Connection is not present

            aController.showAlertDialog(this, getString(R.string.alert_title), AppConstants.NETWORK_ERROR, false);
            // stop execution code by return
            return;
        }

        // Check if GCM configuration is set
        if (HttpConst.YOUR_SERVER_URL == null || AppConstants.GOOGLE_SENDER_ID == null || HttpConst.YOUR_SERVER_URL.length() == 0 || AppConstants.GOOGLE_SENDER_ID.length() == 0) {

            // GCM sender id / server url in missing

            aController.showAlertDialog(this, getString(R.string.alert_title), getString(R.string.validation_server), false);
            // stop executing code by return
            return;
        }
    }

    private void setSharedPrefs() {
        SharedPreferences pref = getSharedPreferences("RunningState", MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putBoolean("KEY", false);
        editor.putString("Name", eFirstName.getText().toString());
        editor.putString("Email", eEmail.getText().toString());
        editor.putString("Contact", eContact.getText().toString());
        editor.commit();

        mGCM();
    }

    /******************************** GCM *********************************************/
    private void mGCM(){
        // Get Global Controller Class object (see application tag in AndroidManifest.xml)
        aController = (Controller)getApplicationContext();
        // Check if Internet present
        if (!aController.isConnectingToInternet()) {
            MLog.v("Internet Connection Error", "Please connect to Internet connection");
            // stop executing code by return
            return;
        }
        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);
        // Make sure the manifest permission was properly set
        GCMRegistrar.checkManifest(this);
        // Register custom Broadcast receiver to show message on activity
        registerReceiver(mHandleMessageReceiver, new IntentFilter(AppConstants.DISPLAY_MESSAGE_ACTION));
        // Get GCM registration id
        final String regId = GCMRegistrar.getRegistrationId(this);
        // Check if regId already presents
        if (regId.equals("")) {
            // Register with GCM
            GCMRegistrar.register(this, AppConstants.GOOGLE_SENDER_ID);
        } else {
            // Device is already registered on GCM Server
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                MLog.v("TAG", "Already registered with GCM Server");
            } else {
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
                final Context context = this;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            // Register on our server On server creates a new user
                            aController.register(context, eFirstName.getText().toString(), eEmail.getText().toString(), "1234", regId);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }
                };
                mRegisterTask.execute(null, null, null);
            }
        }
    }

    // Create a broadcast receiver to get message and show a screen
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(AppConstants.EXTRA_MESSAGE);
            // Waking up mobile if it is sleeping

            aController.acquireWakeLock(getApplicationContext());

            MLog.v("TAG", "Got Message: " + newMessage);
            // Releasing wake lock
            aController.releaseWakeLock();
        }
    };

    @Override
    protected void onDestroy() {
        // Cancel AsyncTask
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            // Unregister Broadcast Receiver
            unregisterReceiver(mHandleMessageReceiver);
            // Clear internal resources.
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegisterReceiverError", ">" + e.getMessage());
        }
        super.onDestroy();
    }
    /**********************************************************************************/
}
