package com.example.myonlineshop.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myonlineshop.HomeActivity;
import com.example.myonlineshop.MainActivity;
import com.example.myonlineshop.R;

public class AdminHomeActivity extends AppCompatActivity {


    private Button logOutBtn, checkOrdrsBtn, maintainBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);


        maintainBtn = findViewById(R.id.maintainBtn);

        maintainBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(AdminHomeActivity.this, HomeActivity.class);
                        intent.putExtra("Admin", "Admin");
                        startActivity(intent);


                    }
                }
        );

        checkOrdrsBtn = findViewById(R.id.check_new_orders);
        logOutBtn = findViewById(R.id.logOut);
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });


        checkOrdrsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminHomeActivity.this, AdminNewOrderActivity.class);
                startActivity(intent);
            }
        });

    }
}