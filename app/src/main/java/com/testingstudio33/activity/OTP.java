package com.testingstudio33.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.testingstudio33.R;
import com.testingstudio33.common.APIError;
import com.testingstudio33.common.ErrorUtils;
import com.testingstudio33.common.SharedPrefsUtils;
import com.testingstudio33.common.Utils;
import com.testingstudio33.model.Test;
import com.testingstudio33.network.ApiClient;
import com.testingstudio33.network.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTP extends Utils {

    TextView otp, mobi, timer, txt_dont_receive, txt_resend;
    EditText otp_box_1,otp_box_2,otp_box_3,otp_box_4,otp_box_5,otp_box_6;
    String smobi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_otp);
        smobi = SharedPrefsUtils.getStringPreference(this, "mobi");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        otp = findViewById(R.id.otp);
        txt_resend = findViewById(R.id.txt_resend);
        timer = findViewById(R.id.timer);
        txt_dont_receive = findViewById(R.id.txt_dont_receive);
        otp_box_1 = findViewById(R.id.otp_box_1);
        otp_box_2 = findViewById(R.id.otp_box_2);
        otp_box_3 = findViewById(R.id.otp_box_3);
        otp_box_4 = findViewById(R.id.otp_box_4);
        otp_box_5 = findViewById(R.id.otp_box_5);
        otp_box_6 = findViewById(R.id.otp_box_6);
        mobi = findViewById(R.id.mobi);
        otp.setText(Html.fromHtml(getResources().getString(R.string.otp1)));
        mobi.setText(SharedPrefsUtils.getStringPreference(this, "mobi"));
        otp_box_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(editable!=null){
                    if(editable.length()==1)
                        otp_box_2.requestFocus();
                }
            }
        });
        otp_box_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable!=null){
                    if(editable.length()==1)
                        otp_box_3.requestFocus();
                }
            }
        });
        otp_box_3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable!=null){
                    if(editable.length()==1)
                        otp_box_4.requestFocus();
                }
            }
        });
        otp_box_4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable!=null){
                    if(editable.length()==1)
                        otp_box_5.requestFocus();
                }
            }
        });
        otp_box_5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable!=null){
                    if(editable.length()==1)
                        otp_box_6.requestFocus();
                }
            }
        });

    }

    private void VerifyOTP() {
        ApiInterface apiInterface = ApiClient.getOTPClient().create(ApiInterface.class);
        showProgressDialog(this, "verifying OTP...");
        Call<Test> call = apiInterface.getlog();
        call.enqueue(new Callback<Test>() {
            @Override
            public void onResponse(@NonNull Call<Test> call, @NonNull Response<Test> response) {
                Log.w("Json Response => ", new GsonBuilder().setPrettyPrinting().create().toJson(response));
                dismissProgressDialog();
                Test log = response.body();
                assert log != null;
                if (response.isSuccessful()) {
                    SharedPrefsUtils.setBooleanPreference(OTP.this, "otp_verified", true);
                    Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    switch (error.getCode()) {
                        case 400:
                            Toast.makeText(OTP.this, String.valueOf(error.getMessage()), Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Test> call, @NonNull Throwable t) {
                t.printStackTrace();
                dismissProgressDialog();
                Log.e("Login-Failure-link", "No Response!");
            }
        });

    }

    public void reverseTimer(int Seconds,final TextView tv){

        new CountDownTimer(Seconds* 1000+1000, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                tv.setText("TIME : " + String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
                //tv.setText("TIME : " + String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
            }

            public void onFinish() {
                tv.setText("Completed");
            }
        }.start();
    }

}
