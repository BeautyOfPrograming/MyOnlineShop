package com.example.myonlineshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myonlineshop.model.Users;
import com.example.myonlineshop.prevelant.Prevalent;
import com.example.myonlineshop.sellers.SellerHomeActivity;
import com.example.myonlineshop.sellers.SellerRegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity  extends AppCompatActivity {



    private Button joinNowBtn,singNowbtn;

    private TextView sellerBegin;
    ProgressDialog loading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Paper.init(this);

        joinNowBtn = findViewById(R.id.sign_up_now_btn);
        singNowbtn = findViewById(R.id.sign_in_now_btn);
        sellerBegin = findViewById(R.id.become_seller);

         loading =  new ProgressDialog(this);


        sellerBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, SellerRegisterActivity.class);

                startActivity(intent);
            }
        });

        singNowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,LoginActivity.class);

                startActivity(intent);
            }
        });



        joinNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);

                startActivity(intent);
            }
        });



        String phone = Paper.book().read(Prevalent.userPhone);
        String pass = Paper.book().read(Prevalent.userPassword);


        if (phone!=""&& pass!=""){


            if(!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(pass)){

                allowAccess(phone,pass);
            }
        }




    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user  = FirebaseAuth.getInstance().getCurrentUser();

        if (user!=null){

            Intent intent = new Intent(MainActivity.this, SellerHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

    }

    private void allowAccess(String phone, String password) {



        loading.setTitle("Already logged in");
        loading.setMessage("please wait...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();



        final DatabaseReference databaseReference;


        databaseReference = FirebaseDatabase.getInstance().getReference();


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child("Users").child(phone).exists()) {

                    Users users = snapshot.child("Users").child(phone).getValue(Users.class);



                    Prevalent.currentOnlineUser = users;

                    if (users.getPhone().equals(phone)) {

                        if (users.getPass().equals(password)) {

                            Toast.makeText(MainActivity.this, users.getUser() + " Logged in successfully...", Toast.LENGTH_SHORT).show();
                            loading.dismiss();

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);

                            Prevalent.currentOnlineUser = users;

                            startActivity(intent);

                        }
                        else {

                            loading.dismiss();
                            Toast.makeText(MainActivity.this, "The phone or password is incorrect... ", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "failed Loged in ...", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(MainActivity.this, "you can not log in ....", Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }
        });

    }


}
