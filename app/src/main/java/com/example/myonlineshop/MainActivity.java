package com.example.myonlineshop;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity  extends AppCompatActivity {



    private Button joinNowBtn,singNowbtn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        joinNowBtn = findViewById(R.id.sign_up_now_btn);
        singNowbtn = findViewById(R.id.sign_in_now_btn);
    }
}
