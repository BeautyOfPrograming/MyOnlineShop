package com.example.myonlineshop.sellers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myonlineshop.R;

public class SellerLoginActivity extends AppCompatActivity {

    private Button do_not_have_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        do_not_have_account = findViewById(R.id.do_not_have_account);

        do_not_have_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SellerLoginActivity.this,SellerActivity.class);
                startActivity(intent);
            }
        });
    }
}