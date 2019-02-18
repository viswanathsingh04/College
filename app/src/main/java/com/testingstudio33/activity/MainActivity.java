package com.testingstudio33.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.testingstudio33.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
    }

    public void myFancyMethod(View v) {
        //Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), Registration.class));
    }

    public void Log_in(View v) {
        //Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), Login.class));
    }
}
