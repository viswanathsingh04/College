package com.testingstudio33.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.testingstudio33.R;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Utils extends AppCompatActivity {

    public ConnectionDetector connectionDetector;
    public boolean isInternetAvailable, isNetAvailable;
    public ProgressDialog progressDialog;
    int range = 9;  // to generate a single number with this range, by default its 0..9
    int length = 6; // by default length is 4
    private static final String TAG = "Utils";
    private String date;
    private SimpleDateFormat format;
    Calendar calendar;
    SimpleDateFormat dateFormat;
    String strDate = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionDetector = new ConnectionDetector(getApplicationContext());
        isInternetAvailable = connectionDetector.isConnectingToInternet();
        isNetAvailable = connectionDetector.isNetworkAvailable();
    }

    public void showtoast(TextInputLayout v, String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        v.setError(msg);
        v.requestFocus();
    }

    public void showProgressDialog(Context context, String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(R.string.app_name);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        //progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    public String getCurrentDateTime() {
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        //dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        strDate = dateFormat.format(calendar.getTime());
        Log.i("strDate", strDate);
        return strDate;
    }

    public String getCurrentTime() {
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        strDate = "Current Time : " + dateFormat.format(calendar.getTime());
        Log.i("strDate", strDate);
        return strDate;
    }

    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void Validate(EditText et, String message) {
        et.setError(message);
    }

    public void hideKeyboard(Context context) {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void showKeyboard(Context context) {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public int generateRandomNumber() {
        int randomNumber;

        SecureRandom secureRandom = new SecureRandom();
        String s = "";
        for (int i = 0; i < length; i++) {
            int number = secureRandom.nextInt(range);
            if (number == 0 && i == 0) { // to prevent the Zero to be the first number as then it will reduce the length of generated pin to three or even more if the second or third number came as zeros
                i = -1;
                continue;
            }
            s = s + number;
        }
        randomNumber = Integer.parseInt(s);
        return randomNumber;
    }

    public void showDialogMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(TAG);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

    public String ConvertDate(String dd) {
        try {
            format = new SimpleDateFormat("yyyy-M-dd hh:mm:ss", Locale.US);
            SimpleDateFormat df2 = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
            //SimpleDateFormat df2 = new SimpleDateFormat("EEE MMM dd, yyyy", Locale.US);
            date = df2.format(format.parse(dd));
            System.out.println(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
