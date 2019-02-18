package com.testingstudio33.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.irozon.sneaker.Sneaker;
import com.testingstudio33.R;
import com.testingstudio33.common.APIError;
import com.testingstudio33.common.ErrorUtils;
import com.testingstudio33.common.SaveSharedPreference;
import com.testingstudio33.common.SharedPrefsUtils;
import com.testingstudio33.common.Utils;
import com.testingstudio33.model.MLogin;
import com.testingstudio33.network.ApiClient;
import com.testingstudio33.network.ApiInterface;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.subscriptions.CompositeSubscription;

public class Login extends Utils implements View.OnClickListener {

    private TextView mBack;
    private TextInputLayout mTxtMobile;
    private TextInputEditText mEdtMobile;
    private TextInputLayout mTxtPassword;
    private TextInputEditText mEdtPassword;
    private Button mBtnLogin;
    private TextView mGotoRegister;
    private String smobile, spassword, user_response, account_info;
    Gson gson = new Gson();
    Observable<Boolean> observable;
    private Pattern pattern = android.util.Patterns.EMAIL_ADDRESS;
    private Matcher matcher;
    NiftyDialogBuilder dialogBuilder;
    protected CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        dialogBuilder = NiftyDialogBuilder.getInstance(this);
        initView();
    }

    private void initView() {
        mBack = (TextView) findViewById(R.id.back);
        mTxtMobile = (TextInputLayout) findViewById(R.id.txt_mobile);
        mEdtMobile = (TextInputEditText) findViewById(R.id.edt_mobile);
        mTxtPassword = (TextInputLayout) findViewById(R.id.txt_password);
        mEdtPassword = (TextInputEditText) findViewById(R.id.edt_password);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mGotoRegister = (TextView) findViewById(R.id.gotoRegister);
        mBack.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        mGotoRegister.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        smobile = Objects.requireNonNull(mEdtMobile.getText()).toString().trim();
        spassword = Objects.requireNonNull(mEdtPassword.getText()).toString().trim();

        if (v.getId() == R.id.back) {
            onBackPressed();
        } else if (v.getId() == R.id.gotoRegister) {
            startActivity(new Intent(getApplicationContext(), Registration.class));
        } else if (v.getId() == R.id.btn_login) {
            //Toast.makeText(this, "clicked login", Toast.LENGTH_SHORT).show();
            if (TextUtils.isEmpty(smobile)) {
                showtoast(mTxtMobile, "please enter mobile number");
            } else if (TextUtils.isEmpty(spassword)) {
                showtoast(mTxtPassword, "Please enter password");
            } else if (spassword.length() < 4) {
                showtoast(mTxtPassword, "Password should not less then four character");
            } else {
                hideKeyboard(this);
                mTxtMobile.setError(null);
                mTxtPassword.setError(null);
                if (isInternetAvailable) {
                    GetLoginData();
                } else {
                    Sneaker.with(Login.this)
                            .setTitle("OOPS Sorry for inconvenience !!")
                            .setMessage("Please check your internet connection and try again")
                            .autoHide(true) // Auto hide Sneaker view
                            .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT) // Height of the Sneaker layout
                            .setIcon(R.drawable.ic_error, R.color.white, false)
                            .setTypeface(Typeface.createFromAsset(this.getAssets(), "font/gowthamroundedbook.ttf"))
                            .sneakError();
                }
               /* dialogBuilder
                        .withTitle("Modal Dialog")                                  //.withTitle(null)  no title
                        .withTitleColor("#FFFFFF")                                  //def
                        .withDividerColor("#11000000")                              //def
                        .withMessage("This is a modal Dialog.")                     //.withMessage(null)  no Msg
                        .withMessageColor("#FFFFFFFF")                              //def  | withMessageColor(int resid)
                        .withDialogColor("#FFB300")                               //def  | withDialogColor(int resid)
                        .withIcon(getResources().getDrawable(R.mipmap.ic_launcher))
                        .withDuration(700)                                          //def
                        .withEffect(Slit)                                         //def Effectstype.Slidetop
                        .withButton1Text("OK")                                      //def gone
                        .withButton2Text("Cancel")                                  //def gone
                        .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                        //.setCustomView(R.layout.custom_view,v.getContext())         //.setCustomView(View or ResId,context)
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(v.getContext(), "i'm btn1", Toast.LENGTH_SHORT).show();
                                GetLoginData();
                                dialogBuilder.dismiss();
                            }
                        })
                        .setButton2Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogBuilder.dismiss();
                                Toast.makeText(v.getContext(), "i'm btn2", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();*/
            }
        }
    }

    private void GetLoginData() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        showProgressDialog(this, "getting response status...");
        Call<MLogin> call = apiInterface.getlogin(smobile, spassword);
        call.enqueue(new Callback<MLogin>() {
            @Override
            public void onResponse(@NonNull Call<MLogin> call, @NonNull Response<MLogin> response) {
                Log.w("Json Response => ", new GsonBuilder().setPrettyPrinting().create().toJson(response));
                dismissProgressDialog();
                MLogin log = response.body();
                assert log != null;
                if (response.isSuccessful()) {
                    account_info = gson.toJson(log.getLogin());
                    Log.i("account_info", account_info);
                    SaveSharedPreference ss = new SaveSharedPreference(Login.this);
                    ss.saveuserinfo(account_info);
                    SaveSharedPreference.setLoggedIn(getApplicationContext(), true);
                    Sneaker.with(Login.this)
                            .setTitle("Login Success!!")
                            .setMessage("logged Successfully")
                            .autoHide(true) // Auto hide Sneaker view
                            .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT) // Height of the Sneaker layout
                            .setIcon(R.drawable.ic_error, R.color.white, false)
                            .sneakSuccess();
                    user_response = SharedPrefsUtils.getStringPreference(Login.this, "account_info");
                    Intent login = new Intent(getApplicationContext(), Dashboard.class);
                    startActivity(login);
                    finish();
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    switch (error.getCode()) {
                        case 400:
                            //Toast.makeText(Login.this, String.valueOf(error.getMessage()), Toast.LENGTH_SHORT).show();
                            Sneaker.with(Login.this)
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
            public void onFailure(@NonNull Call<MLogin> call, @NonNull Throwable t) {
                t.printStackTrace();
                dismissProgressDialog();
                Log.e("Login-Failure-link", "No Response!");
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
