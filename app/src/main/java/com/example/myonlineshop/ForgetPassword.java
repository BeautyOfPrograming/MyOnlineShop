package com.example.myonlineshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ForgetPassword extends AppCompatActivity {

    String check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        check = getIntent().getStringExtra("check");

    }

    @Override
    protected void onStart() {
        super.onStart();


        if (check.equals("login")) {


        } else if (check.equals("settings")) {

        }
    }
}