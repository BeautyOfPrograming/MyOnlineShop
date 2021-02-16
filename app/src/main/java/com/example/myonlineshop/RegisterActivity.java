package com.example.myonlineshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {


    ProgressDialog loading;
    private Button createAccountButton;

    DatabaseReference databaseReference;
    FirebaseApp firebaseApp;
    private EditText userInput, phoneInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        firebaseApp = FirebaseApp.initializeApp(this);


        createAccountButton = findViewById(R.id.sign_up_now_btn);

        userInput = findViewById(R.id.register_username_input);
        phoneInput = findViewById(R.id.register_phone_input);
        passwordInput = findViewById(R.id.rgister_user_password);


        loading = new ProgressDialog(RegisterActivity.this);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createAccount();
            }


        });

    }

    private void createAccount() {


        String name = userInput.getText().toString();
        String phone = phoneInput.getText().toString();
        String password = passwordInput.getText().toString();


        if (TextUtils.isEmpty(name)) {

            Toast.makeText(RegisterActivity.this, "please enter your name ..", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {

            Toast.makeText(RegisterActivity.this, "please enter your phonenuber ..", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {

            Toast.makeText(RegisterActivity.this, "please enter your password ..", Toast.LENGTH_SHORT).show();
        } else {


            loading.setTitle("Create Account");
            loading.setMessage("please wait while we are checking the credentials...");
            loading.setCanceledOnTouchOutside(false);
            loading.show();

            validate(name, phone, password);


        }
    }

    private void validate(String name, String phone, String password) {


        databaseReference = FirebaseDatabase.getInstance().getReference();

//        firebaseApp = databaseReference;
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!(snapshot.child("Users").child(phone).exists())) {

                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("user", name);
                    userdataMap.put("phone", password);
                    userdataMap.put("pass", password);

                    databaseReference.child("Users").child(phone).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                Toast.makeText(RegisterActivity.this, "Congratulation your account has been created.", Toast.LENGTH_SHORT).show();

                                loading.dismiss();


                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);

                                startActivity(intent);


                            } else {
                                loading.dismiss();
                                Toast.makeText(RegisterActivity.this, "Network Error:   ", Toast.LENGTH_SHORT).show();


                            }

                        }
                    });


                } else {

                    Toast.makeText(RegisterActivity.this, "This " + phone + " number is existed", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please try another number", Toast.LENGTH_SHORT).show();


                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);

                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}