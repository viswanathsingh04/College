package com.testingstudio33.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.irozon.sneaker.Sneaker;
import com.testingstudio33.R;
import com.testingstudio33.common.APIError;
import com.testingstudio33.common.ErrorUtils;
import com.testingstudio33.common.SaveSharedPreference;
import com.testingstudio33.common.Utils;
import com.testingstudio33.model.Login;
import com.testingstudio33.model.Mark;
import com.testingstudio33.network.ApiClient;
import com.testingstudio33.network.ApiInterface;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends Utils implements View.OnClickListener {
    Calendar calendar;
    SimpleDateFormat dateFormat;
    String strDate = null;
    private CardView mCard;
    private ImageView mUserImg;
    private TextView mTxtUname;
    private ImageView mUserIm3g;
    private TextView mTxtMobile;
    private Button mBtnAttend;
    Gson gson = new Gson();
    Login user_info;
    String user_response, today, token;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //Toast.makeText(this, getCurrentDateTime(), Toast.LENGTH_SHORT).show();
        today = getCurrentDateTime();
        SaveSharedPreference ss = new SaveSharedPreference(Dashboard.this);
        //user_response = SharedPrefsUtils.getStringPreference(Dashboard.this, "account_info");
        user_response = ss.getuserinfo(this);
        Log.i("account_info-response", user_response);
        user_info = gson.fromJson(user_response, Login.class);
        token = "Bearer " + user_info.getToken();
        initView();
    }

    private void initView() {
        mCard = (CardView) findViewById(R.id.card);
        mUserImg = (ImageView) findViewById(R.id.user_img);
        mTxtUname = (TextView) findViewById(R.id.txt_uname);
        mUserIm3g = (ImageView) findViewById(R.id.user_im3g);
        mTxtMobile = (TextView) findViewById(R.id.txt_mobile);
        mBtnAttend = (Button) findViewById(R.id.btn_attend);
        mBtnAttend.setOnClickListener(this);
        mTxtUname.setText(user_info.getName());
        mTxtMobile.setText(user_info.getPhone());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_attend) {
            if (isInternetAvailable) {
                UpdateAttend();
            } else {
                Sneaker.with(Dashboard.this)
                        .setTitle("OOPS Sorry for inconvenience !!")
                        .setMessage("Please check your internet connection and try again")
                        .autoHide(true) // Auto hide Sneaker view
                        .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT) // Height of the Sneaker layout
                        .setIcon(R.drawable.ic_error, R.color.white, false)
                        .sneakError();
            }
        }
    }

    private void UpdateAttend() {
        ApiInterface apiInterface = ApiClient.MarkRegister(token).create(ApiInterface.class);
        showProgressDialog(this, "getting response status...");
        Call<Mark> call = apiInterface.mark_attend(user_info.getId(), 1, today);
        call.enqueue(new Callback<Mark>() {
            @Override
            public void onResponse(@NonNull Call<Mark> call, @NonNull Response<Mark> response) {
                Log.w("Json Response => ", new GsonBuilder().setPrettyPrinting().create().toJson(response));
                dismissProgressDialog();
                Mark log = response.body();
                assert log != null;
                if (response.isSuccessful()) {
                    if (log.getStatus().equals("success")) {
                        Sneaker.with(Dashboard.this) // Activity, Fragment or ViewGroup
                                .setTitle("Success!!")
                                .setDuration(2000)
                                .setMessage("Congrats, registered your attendance successfully")
                                .autoHide(true) // Auto hide Sneaker view
                                .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT) // Height of the Sneaker layout
                                .setIcon(R.drawable.ic_error, R.color.white, false)
                                .sneakSuccess();
                    } else if (log.getStatus().equals("failure")) {
                        Sneaker.with(Dashboard.this) // Activity, Fragment or ViewGroup
                                .setTitle("Already registered")
                                .setDuration(2000)
                                .setMessage("Sorry!!! Already registered, Please contact administrator")
                                .autoHide(true) // Auto hide Sneaker view
                                .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT) // Height of the Sneaker layout
                                .setIcon(R.drawable.ic_error, R.color.white, false)
                                .sneakError();
                    }
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    if (error.getCode() != null) {
                        switch (error.getCode()) {
                            case 401:
                                Sneaker.with(Dashboard.this)
                                        .setTitle("Already registered!!")
                                        .setDuration(2000)
                                        .setMessage(error.getMessage())
                                        .autoHide(true) // Auto hide Sneaker view
                                        .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT) // Height of the Sneaker layout
                                        .setIcon(R.drawable.ic_error, R.color.white, false)
                                        .sneakError();
                                break;
                            case 404:
                                Sneaker.with(Dashboard.this) // Activity, Fragment or ViewGroup
                                        .setTitle("Invalid User!!")
                                        .setDuration(2000)
                                        .setMessage(error.getMessage())
                                        .autoHide(true) // Auto hide Sneaker view
                                        .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT) // Height of the Sneaker layout
                                        .setIcon(R.drawable.ic_error, R.color.white, false)
                                        .sneakError();
                                break;
                            default:
                                Sneaker.with(Dashboard.this) // Activity, Fragment or ViewGroup
                                        .setTitle("Technical issues")
                                        .setDuration(2000)
                                        .setMessage("Please try again after sometimes")
                                        .autoHide(true) // Auto hide Sneaker view
                                        .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT) // Height of the Sneaker layout
                                        .setIcon(R.drawable.ic_error, R.color.white, false)
                                        .sneakError();
                                break;
                        }
                    } else {
                        Sneaker.with(Dashboard.this) // Activity, Fragment or ViewGroup
                                .setTitle("Authorization issues")
                                .setDuration(2000)
                                .setMessage("Sorry you are not authorized. Please contact administrator")
                                .autoHide(true) // Auto hide Sneaker view
                                .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT) // Height of the Sneaker layout
                                .setIcon(R.drawable.ic_error, R.color.white, false)
                                .sneakError();
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<Mark> call, @NonNull Throwable t) {
                t.printStackTrace();
                dismissProgressDialog();
                Log.e("Login-Failure-link", "No Response!");
            }
        });

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Dashboard.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }


}
