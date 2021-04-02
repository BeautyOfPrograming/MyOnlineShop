package com.example.myonlineshop.sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myonlineshop.LoginActivity;
import com.example.myonlineshop.MainActivity;
import com.example.myonlineshop.R;
import com.example.myonlineshop.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class SellerRegisterActivity extends AppCompatActivity {

    private Button seller_login_btn, alreadyRegisterBtn;
    private EditText nameInput, phoneInput, emailInput, passInput, addressInput;
    ProgressDialog loading;
    private FirebaseAuth auth;
    private static FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);
        auth = FirebaseAuth.getInstance();
//        auth.addAuthStateListener(mAuthListener);


        nameInput = findViewById(R.id.seller_name);
        phoneInput = findViewById(R.id.seller_phone);
        emailInput = findViewById(R.id.seller_email);
        passInput = findViewById(R.id.seller_pass);
        addressInput = findViewById(R.id.seller_shop_business_address);


        loading = new ProgressDialog(SellerRegisterActivity.this);

        alreadyRegisterBtn = findViewById(R.id.seller_register);
        alreadyRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                registerSeller();
            }
        });


        seller_login_btn = findViewById(R.id.seller_already_account);
        seller_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SellerRegisterActivity.this, SellerLoginActivity.class);

                startActivity(intent);
            }
        });
    }

    private void registerSeller() {

        String name = nameInput.getText().toString();
        String phone = phoneInput.getText().toString();
        String email = emailInput.getText().toString();
        String pass = passInput.getText().toString();
        String address = addressInput.getText().toString();

        if (!name.equals("") && !phone.equals("") && !email.equals("") && !pass.equals("") && !address.equals("")) {

            loading.setTitle("Create your Seller Account");
            loading.setMessage("please wait while we are checking the credentials...");
//            loading.setCanceledOnTouchOutside(false);
//            loading.show();


            Log.d("Seller_0", "seller0");

            auth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("Seller_1", "seller1");
                            if (task.isSuccessful()) {

                                Log.d("Seller_2", "seller2");
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = auth.getCurrentUser();
//                                updateUI(user);

                                Log.d("Seller_3", "seller3");

                                final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                                String sid = auth.getCurrentUser().getUid();

                                HashMap<String, Object> sellerMap = new HashMap<>();
                                sellerMap.put("sid", sid);
                                sellerMap.put("phone", phone);
                                sellerMap.put("email", email);
                                sellerMap.put("address", address);
                                sellerMap.put("name", name);

                                rootRef.child("Sellers").child(sid).updateChildren(sellerMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        Toast.makeText(SellerRegisterActivity.this, "You are registered successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SellerRegisterActivity.this,SellerHomeActivity.class);
                                        startActivity(intent);
                                    }
                                });

                            } else {


//                                FirebaseAuthException e = (FirebaseAuthException)task.getException();
//                                Toast.makeText(LoginActivity.this, "Failed Registration: "+e.getMessage(), Toast.LENGTH_SHORT).show();
////                                message.hide();




                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SellerRegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                Log.d("Seller_4_error_1", "seller4error1");

//                                updateUI(null);
                            }
                        }
                    });


//
//            auth.createUserWithEmailAndPassword(email, pass)
//                    .addOnCompleteListener(
//                            new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//
//                                    if (task.isSuccessful()) {
//
//                                        final DatabaseReference refe;
//                                        refe = FirebaseDatabase.getInstance().getReference();
//
//                                        String sid = auth.getCurrentUser().getUid();
//
//                                        HashMap<String, Object> sellerMap = new HashMap<>();
//                                        sellerMap.put("sid", sid);
//                                        sellerMap.put("phone", phone);
//                                        sellerMap.put("email", email);
//                                        sellerMap.put("address", address);
//                                        sellerMap.put("name", name);
//
//                                        refe.child("Sellers").child(sid).updateChildren(sellerMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//
//                                                Toast.makeText(SellerRegisterActivity.this, "you are registerd succesfully", Toast.LENGTH_SHORT).show();
//
//
//                                            }
//                                        });
//
//
//                                    }
//
//
//                                }
//                            });
//


//
//            auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(
//
//                    new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//
//                            if (task.isSuccessful()) {
//
//
////                                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Sellers");
//
////                                HashMap<String, Object> userMap = new HashMap<>();
//
//
//                                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//                                String sid = auth.getCurrentUser().getUid();
//
//
//                                HashMap<String, Object> sellerMap = new HashMap<>();
//                                sellerMap.put("sid", sid);
//                                sellerMap.put("phone", phone);
//                                sellerMap.put("email", email);
//                                sellerMap.put("address", address);
//                                sellerMap.put("name", name);
//
//
//                                reference.child("Sellers").child(sid).updateChildren(sellerMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//
////                                        if (task.isSuccessful()) {
//
//
//                                            loading.dismiss();
//
//                                        Intent intent = new Intent(SellerRegisterActivity.this, SellerHomeActivity.class);
//                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                        startActivity(intent);
//                                        finish();
//                                        Toast.makeText(SellerRegisterActivity.this, "You are added successfully", Toast.LENGTH_SHORT).show();
//
////                                        }
//
//
//                                    }
//                                });
//
//                            }
//                        }
//
//                    }
//
//            );

        } else {


            Toast.makeText(this, "Please enter all fields !", Toast.LENGTH_SHORT).show();


        }


    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseUser firebaseuser = FirebaseAuth.getInstance().getCurrentUser();


        if (firebaseuser != null) {

            Intent intent = new Intent(SellerRegisterActivity.this, SellerHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            Toast.makeText(SellerRegisterActivity.this, "You are added successfully", Toast.LENGTH_SHORT).show();


        }


    }
}