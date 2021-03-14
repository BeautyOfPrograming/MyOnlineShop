package com.example.myonlineshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myonlineshop.prevelant.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {


    private TextView shipmentText;
    private EditText name, phone, address, city;
    private Button confirm;

    private String totalPrice;

    private String saveCurrentlyDate;
    private String saveCurrentlyTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        shipmentText = findViewById(R.id.shipment);
        name = findViewById(R.id.shipment_name);
        phone = findViewById(R.id.shipment_phone);
        address = findViewById(R.id.shipment_address);
        city = findViewById(R.id.shipment_city);
        confirm = findViewById(R.id.shipment_confirm);

        totalPrice = getIntent().getStringExtra("totalprice");
        Toast.makeText(this, "Total Amount : " + totalPrice, Toast.LENGTH_SHORT).show();


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkParameter();

            }
        });
    }

    private void checkParameter() {

        String sname, sphone, saddress, scity;
        sname = name.getText().toString();
        sphone = phone.getText().toString();
        saddress = address.getText().toString();
        scity = city.getText().toString();

        if (TextUtils.isEmpty(sname)) {

            Toast.makeText(this, "please enter your name ", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(sphone)) {

            Toast.makeText(this, "please enter your phone  ", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(saddress)) {

            Toast.makeText(this, "please enter your address ", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(scity)) {

            Toast.makeText(this, "please enter your city ", Toast.LENGTH_SHORT).show();

        } else {


            nowConfirm();
        }

    }

    private void nowConfirm() {

        String key = "";

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentlyDateFormat = new SimpleDateFormat("MM:dd:yy");
        saveCurrentlyDate = currentlyDateFormat.format(calendar.getTime());


        SimpleDateFormat currentlyTimeFormat = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentlyTime = currentlyTimeFormat.format(calendar.getTime());

        key = saveCurrentlyDate + saveCurrentlyTime;

        final DatabaseReference ProductsRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());

        HashMap<String, Object> orderMap = new HashMap<>();

        orderMap.put("totalamount", totalPrice);
        orderMap.put("name", name.getText().toString());
        orderMap.put("phone", phone.getText().toString());
        orderMap.put("address", address.getText().toString());
        orderMap.put("city", city.getText().toString());
        orderMap.put("date", saveCurrentlyDate);
        orderMap.put("time", saveCurrentlyTime);
        orderMap.put("state", "not shipped");

        ProductsRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){

                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User View")
                            .child(Prevalent.currentOnlineUser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(ConfirmFinalOrderActivity.this, "Your order has been submitted successfully ", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ConfirmFinalOrderActivity.this,HomeActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                }

            }
        });


    }
}