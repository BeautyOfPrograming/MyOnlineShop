package com.example.myonlineshop.sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myonlineshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

public class SellerLoginActivity extends AppCompatActivity {

    private Button do_not_have_account;

    private FirebaseAuth mAuth;

    private EditText email, password;

    private Button logIn;
    private String semail, spassword;


    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        loading = new ProgressDialog(SellerLoginActivity.this);

        // Initialize Firebase Auth

        mAuth = FirebaseAuth.getInstance();
        do_not_have_account = findViewById(R.id.do_not_have_account);

        email = findViewById(R.id.seller_email_login);
        password = findViewById(R.id.seller_pass);
        logIn = findViewById(R.id.seller_login);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                semail = email.getText().toString();
                spassword = password.getText().toString();

                if (TextUtils.isEmpty(semail)) {

                    Toast.makeText(SellerLoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(spassword)) {

                    Toast.makeText(SellerLoginActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();

                } else {

                    loading.setTitle("Logging into your Seller Account");
                    loading.setMessage("please wait while we are checking the credentials...");
                    loading.setCanceledOnTouchOutside(true);
                    loading.show();
                    reload();
                }


            }
        });

        do_not_have_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SellerLoginActivity.this, SellerRegisterActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
//            reload();
        }
    }

    private void reload() {


        mAuth.signInWithEmailAndPassword(semail, spassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(SellerLoginActivity.this, "Authentication Successes.",
                                    Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SellerLoginActivity.this, SellerHomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);


                        } else {


                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SellerLoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();


                        }
                    }
                });
    }

}