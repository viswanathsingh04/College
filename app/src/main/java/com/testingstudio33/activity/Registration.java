package com.testingstudio33.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.irozon.sneaker.Sneaker;
import com.testingstudio33.R;
import com.testingstudio33.common.APIError;
import com.testingstudio33.common.ErrorUtils;
import com.testingstudio33.common.SaveSharedPreference;
import com.testingstudio33.common.SharedPrefsUtils;
import com.testingstudio33.common.Utils;
import com.testingstudio33.model.MRegister;
import com.testingstudio33.network.ApiClient;
import com.testingstudio33.network.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registration extends Utils implements View.OnClickListener {

    private TextView mBack;
    private TextInputLayout mTxtName;
    private EditText mEdtName;
    private TextInputLayout mTxtMobile;
    private EditText mEdtMobile;
    private TextInputLayout mTxtPass;
    private EditText mEdtPass;
    private TextInputLayout mTxtEdtConfirmPass;
    private EditText mEdtConfirmPass;
    private Button mBtnRegister;
    private TextView mGotologin;
    String sname, smobile, spassword, scpassword;
    SharedPrefsUtils sharedPrefsUtils;
    int otp_length, otp, otp_expiry;
    String authkey, msg, sender, account_info;
    Gson gson = new Gson();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        sharedPrefsUtils = new SharedPrefsUtils();
        otp_length = getApplicationContext().getResources().getInteger(R.integer.otp_length);
        otp_expiry = getApplicationContext().getResources().getInteger(R.integer.otp_expiry);
        authkey = getApplicationContext().getResources().getString(R.string.authkey);
        sender = getApplicationContext().getResources().getString(R.string.sender);
        otp = generateRandomNumber();
        initView();
    }

    public void Register(View v) {
        startActivity(new Intent(getApplicationContext(), OTP.class));
    }

    private void initView() {
        mTxtName = (TextInputLayout) findViewById(R.id.txt_name);
        mEdtName = (EditText) findViewById(R.id.edt_name);
        mTxtMobile = (TextInputLayout) findViewById(R.id.txt_mobile);
        mEdtMobile = (EditText) findViewById(R.id.edt_mobile);
        mTxtPass = (TextInputLayout) findViewById(R.id.txt_pass);
        mEdtPass = (EditText) findViewById(R.id.edt_pass);
        mTxtEdtConfirmPass = (TextInputLayout) findViewById(R.id.txt_edt_confirm_pass);
        mEdtConfirmPass = (EditText) findViewById(R.id.edt_confirm_pass);
        mBtnRegister = (Button) findViewById(R.id.btn_register);
        mGotologin = (TextView) findViewById(R.id.gotologin);
        mBack = (TextView) findViewById(R.id.back);
        mGotologin.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
        mBack.setOnClickListener(this);
        Toast.makeText(this, "your otp is:" + otp, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        sname = mEdtName.getText().toString().trim();
        smobile = mEdtMobile.getText().toString().trim();
        spassword = mEdtPass.getText().toString().trim();
        scpassword = mEdtConfirmPass.getText().toString().trim();

        if (v.getId() == R.id.back) {
            onBackPressed();
        } else if (v.getId() == R.id.gotologin) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else if (v.getId() == R.id.btn_register) {
            if (TextUtils.isEmpty(sname)) {
                showtoast(mTxtName, "please enter name");
            } else if (TextUtils.isEmpty(smobile)) {
                showtoast(mTxtMobile, "please enter mobile number");
            } else if (TextUtils.isEmpty(spassword)) {
                showtoast(mTxtPass, "Please enter password");
            } else if (spassword.length() < 4) {
                showtoast(mTxtPass, "Password should not less then four character");
            } else if (TextUtils.isEmpty(scpassword)) {
                showtoast(mTxtEdtConfirmPass, "Please enter confirm password");
            } else if (scpassword.length() < 4) {
                showtoast(mTxtEdtConfirmPass, "Confirm Password should not less then four character");
            } else if (!spassword.equals(scpassword)) {
                showtoast(mTxtEdtConfirmPass, "Password Not matching");
            } else {
                hideKeyboard(this);
                mTxtName.setError(null);
                mTxtMobile.setError(null);
                mTxtPass.setError(null);
                mTxtEdtConfirmPass.setError(null);
                SharedPrefsUtils.setStringPreference(this, "mobi", smobile);
                if (isNetAvailable) {
                    Register();
                } else {
                    Sneaker.with(Registration.this)
                            .setTitle("OOPS Sorry for inconvenience !!")
                            .setMessage("Please check your internet connection and try again")
                            .autoHide(true) // Auto hide Sneaker view
                            .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT) // Height of the Sneaker layout
                            .setIcon(R.drawable.ic_error, R.color.white, false)
                            .sneakError();
            }
        }
    }

}

    private void Register() {
        ApiInterface apiInterface = ApiClient.getTClient().create(ApiInterface.class);
        showProgressDialog(this, "getting response status...");
        Call<MRegister> call = apiInterface.putRegister(sname, "vpsingh.ems@gmail.com", smobile, spassword, scpassword);
        call.enqueue(new Callback<MRegister>() {
            @Override
            public void onResponse(@NonNull Call<MRegister> call, @NonNull Response<MRegister> response) {
                Log.w("Json Response => ", new GsonBuilder().setPrettyPrinting().create().toJson(response));
                dismissProgressDialog();
                MRegister log = response.body();
                assert log != null;
                if (response.isSuccessful()) {
                    account_info = gson.toJson(log.getRegister());
                    Log.i("account_info", account_info);
                    SaveSharedPreference.setLoggedIn(getApplicationContext(), true);
                    if (!TextUtils.isEmpty(account_info)) {
                        SharedPrefsUtils.setStringPreference(Registration.this, "account_info", account_info);
                    }
                    Sneaker.with(Registration.this)
                            .setTitle("Action Creation Successful!!")
                            .setMessage("User Registered Successfully")
                            .autoHide(true) // Auto hide Sneaker view
                            .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT) // Height of the Sneaker layout
                            .setIcon(R.drawable.ic_error, R.color.white, false)
                            .sneakSuccess();
                    Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                    startActivity(intent);
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    switch (error.getCode()) {
                        case 400:
                            Sneaker.with(Registration.this)
                                    .setTitle("OOPS Sorry for inconvenience !!")
                                    .setMessage(error.getMessage())
                                    .autoHide(true) // Auto hide Sneaker view
                                    .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT) // Height of the Sneaker layout
                                    .setIcon(R.drawable.ic_error, R.color.white, false)
                                    .sneakError();
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MRegister> call, @NonNull Throwable t) {
                t.printStackTrace();
                dismissProgressDialog();
                Log.e("Login-Failure-link", "No Response!");
            }
        });
    }

    /*private void SendOtp() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        showProgressDialog(this, "getting response status...");
        Call<MOtp> call = apiInterface.sendotp(otp_length, authkey, msg, sender, otp, Integer.parseInt(smobile),otp_expiry , null );
        call.enqueue(new Callback<MOtp>() {
            @Override
            public void onResponse(@NonNull Call<MOtp> call, @NonNull Response<MOtp> response) {
                Log.w("Json Response => ", new GsonBuilder().setPrettyPrinting().create().toJson(response));
                dismissProgressDialog();
                MOtp log = response.body();
                assert log != null;
                if (response.isSuccessful()) {

                }
            }

            @Override
            public void onFailure(@NonNull Call<MOtp> call, @NonNull Throwable t) {
                t.printStackTrace();
                dismissProgressDialog();
                Log.e("Login-Failure-link", "No Response!");
            }
        });
    }*/


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
