package com.example.myonlineshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myonlineshop.model.Users;
import com.example.myonlineshop.prevelant.Prevelant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {


    Button btnSingin;

    TextView admin, notadmin;

    EditText inputPhone;
    EditText inputPass;
    ProgressDialog loading;

    private static String parentDnName = "Users";

    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        btnSingin = findViewById(R.id.sign_in_now_btn);

        inputPhone = findViewById(R.id.app_phone_number);
        inputPass = findViewById(R.id.app_password);

        checkBox = findViewById(R.id.app_checkbox);

        admin = findViewById(R.id.app_admin);
        notadmin = findViewById(R.id.app_not_admin);

        Paper.init(getApplicationContext());


        loading = new ProgressDialog(LoginActivity.this);


        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnSingin.setText("Admin Log in");
                admin.setVisibility(View.GONE);
                notadmin.setVisibility(View.VISIBLE);

                parentDnName = "Admins";
            }
        });

        notadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnSingin.setText("Log in");
                admin.setVisibility(View.VISIBLE);
                notadmin.setVisibility(View.GONE);
                parentDnName = "Users";

            }
        });

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

        if (checkBox.isChecked()) {

            Paper.book().write(Prevelant.userPhone, phone);
            Paper.book().write(Prevelant.userPassword, password);
        }

        final DatabaseReference databaseReference;


        databaseReference = FirebaseDatabase.getInstance().getReference();


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(parentDnName).child(phone).exists()) {

                    Users users = snapshot.child(parentDnName).child(phone).getValue(Users.class);

                    if (users.getPhone().equals(phone)) {

                        if (users.getPass().equals(password)) {

                            if (parentDnName.equals("Admins")) {


                                Toast.makeText(LoginActivity.this, "welcome admin Loged in successfully...", Toast.LENGTH_SHORT).show();
                                loading.dismiss();

                                Intent intent = new Intent(LoginActivity.this, AdminAddNewProductActivity.class);

                                startActivity(intent);

                            } else if (parentDnName.equals("Users")) {


                                Toast.makeText(LoginActivity.this, "Loged in successfully...", Toast.LENGTH_SHORT).show();
                                loading.dismiss();

                                Intent intent = new Intent(LoginActivity.this, Home.class);

                                startActivity(intent);

                            }


                        } else {

                            loading.dismiss();
                            Toast.makeText(LoginActivity.this, "The phone or password is incorrect... ", Toast.LENGTH_SHORT).show();

                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "failed Loged in ...", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(LoginActivity.this, "you can not log in ....", Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }
        });


    }

}