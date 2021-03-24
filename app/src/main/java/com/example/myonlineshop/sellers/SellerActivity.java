package com.example.myonlineshop.sellers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myonlineshop.MainActivity;
import com.example.myonlineshop.R;

public class SellerActivity extends AppCompatActivity {

    private Button seller_already_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);
        seller_already_btn = findViewById(R.id.seller_already_account);
        seller_already_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SellerActivity.this, SellerLoginActivity.class);

                startActivity(intent);
            }
        });
    }
}