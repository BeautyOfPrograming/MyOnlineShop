package com.example.myonlineshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {


    Button btnSingin;


    EditText inputPhone;
    EditText inputPass;
    ProgressDialog loading;

    private String parentDnName = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        btnSingin = findViewById(R.id.sign_in_now_btn);

        inputPhone = findViewById(R.id.app_phone_number);
        inputPass = findViewById(R.id.app_password);


        loading = new ProgressDialog(LoginActivity.this);


        btnSingin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                logingUser();
            }

        });


    }

    private void logingUser() {


        String phone = inputPhone.getText().toString();
        String password = inputPass.getText().toString();


        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(LoginActivity.this, "please enter your phonenuber ..", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {

            Toast.makeText(LoginActivity.this, "please enter your password ..", Toast.LENGTH_SHORT).show();

        } else {


            loading.setTitle("Loging Account");
            loading.setMessage("please wait while we are checking the credentials...");
            loading.setCanceledOnTouchOutside(false);
            loading.show();
            allowAccessToaccount(phone, password);


        }


    }

    private void allowAccessToaccount(String phone, String password) {

        final DatabaseReference databaseReference;


        databaseReference = FirebaseDatabase.getInstance().getReference();


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(parentDnName).child(phone).exists()) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}