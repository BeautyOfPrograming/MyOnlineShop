package com.example.myonlineshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myonlineshop.admin.AdminCategoryActivity;
import com.example.myonlineshop.admin.MaintainActivity;
import com.example.myonlineshop.prevelant.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;

public class ResetPassword extends AppCompatActivity {

    String check;
    private TextView pageTitle, ask_enter_questions;
    private EditText question1, question2, findphonenumber;
    private Button verify;
    private String answer1, answer2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        check = getIntent().getStringExtra("check");

        pageTitle = findViewById(R.id.pageTitle);
        findphonenumber = findViewById(R.id.find_phone_number);
        ask_enter_questions = findViewById(R.id.ask_enter_questions);
        question1 = findViewById(R.id.q1);
        question2 = findViewById(R.id.q2);
        verify = findViewById(R.id.verify);

    }

    @Override
    protected void onStart() {
        super.onStart();

        findphonenumber.setVisibility(View.GONE);

        if (check.equals("login")) {

            findphonenumber.setVisibility(View.VISIBLE);

            verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    verifyUser();
                }
            });

        } else if (check.equals("settings")) {

            pageTitle.setText("Set Security Questions ");
            ask_enter_questions.setText("Please set Answers for security questions ");
            verify.setTag("Set");

            displayPreviousAnswers();

            verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    answer1 = question1.getText().toString().toLowerCase();
                    answer2 = question2.getText().toString().toLowerCase();

                    if (TextUtils.isEmpty(answer1) && TextUtils.isEmpty(answer2)) {

                        Toast.makeText(ResetPassword.this, "Please enter Both Questions", Toast.LENGTH_SHORT).show();
                    } else {

                        saveQuestionsToDataBase();

                    }


                }
            });
        }
    }

    private void verifyUser() {

        String phone = findphonenumber.getText().toString();
        String answer1 = question1.getText().toString().toLowerCase();
        String answer2 = question2.getText().toString().toLowerCase();


        if (TextUtils.isEmpty(phone)) {

            Toast.makeText(this, "Please Enter your phone Number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(answer1)) {

            Toast.makeText(this, "Please Enter your 1st answer", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(answer2)) {

            Toast.makeText(this, "Please Enter your 2nd answer", Toast.LENGTH_SHORT).show();
        }


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(phone);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    String Mphone = snapshot.child("phone").getKey().toLowerCase();


                    if (snapshot.child("Security Questions").exists()) {


                        String ans1 = snapshot.child("Security Questions").child("answer1").getValue().toString();
                        String ans2 = snapshot.child("Security Questions").child("answer2").getValue().toString();


                        if (!ans1.equals(answer1)) {

                            Toast.makeText(ResetPassword.this, "Your first Answer is incorrect ", Toast.LENGTH_SHORT).show();
                        } else if (!ans2.equals(answer2)) {

                            Toast.makeText(ResetPassword.this, "Your 2nd Answer is incorrect ", Toast.LENGTH_SHORT).show();

                        } else {


                            AlertDialog.Builder builder = new AlertDialog.Builder(ResetPassword.this);
                            builder.setTitle("New Password");

                            final EditText editText = new EditText(ResetPassword.this);
                            editText.setHint("write your new password");
                            builder.setView(editText);

                            builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    if (!editText.getText().toString().equals("")) {

                                        reference.child("pass")
                                                .setValue(editText.getText()
                                                        .toString())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {


                                                            Toast.makeText(ResetPassword.this, "Password is sat successfully", Toast.LENGTH_SHORT).show();

                                                            startActivity(new Intent(ResetPassword.this, LoginActivity.class));
                                                            finish();

                                                        }
                                                    }
                                                });
                                        {

                                        }

                                    }
                                }
                            });

                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.cancel();
                                }
                            });
                            builder.show();

                        }

                    } else {
                        Toast.makeText(ResetPassword.this, "This user phone dose not exist", Toast.LENGTH_SHORT).show();

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void saveQuestionsToDataBase() {


        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(Prevalent.currentOnlineUser.getPhone());

        HashMap<String, Object> userdataMap = new HashMap<>();
        userdataMap.put("answer1", answer1);
        userdataMap.put("answer2", answer2);


        reference.child("Security Questions").updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    Toast.makeText(ResetPassword.this, "You have answered Security Questions Successfully", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(ResetPassword.this, HomeActivity.class);

                    startActivity(intent);

                }

            }
        });

    }

    private void displayPreviousAnswers() {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(Prevalent.currentOnlineUser.getPhone());


        reference.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    String ans1 = snapshot.child("answer1").getValue().toString();
                    String ans2 = snapshot.child("answer2").getValue().toString();

                    question1.setText(ans1);
                    question2.setText(ans2);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}