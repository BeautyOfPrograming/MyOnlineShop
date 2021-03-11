package com.example.myonlineshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmFinalOrderActivity extends AppCompatActivity {


    private TextView shipmentText;
    private EditText name,phone,address,city;
    private Button confirm;

    private  String totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        shipmentText = findViewById(R.id.shipment);
        name = findViewById(R.id.shipment_name);
        phone = findViewById(R.id.shipment_phone);
        address = findViewById(R.id.shipment_address);
        city= findViewById(R.id.shipment_city);
        confirm = findViewById(R.id.shipment_confirm);

        totalPrice = getIntent().getStringExtra("totalprice");
        Toast.makeText(this, "Total Amount : "+totalPrice, Toast.LENGTH_SHORT).show();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
    }
}