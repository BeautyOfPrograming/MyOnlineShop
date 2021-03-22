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
    private   String answer1,answer2;


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

    private void saveQuestionsToDataBase() {


     final   DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
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

                    Intent inten = new Intent(ResetPassword.this, HomeActivity.class);

                    startActivity(inten);

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

                if (snapshot.exists()){

                    String ans1  = snapshot.child("answer1").getValue().toString();
                    String ans2  = snapshot.child("answer2").getValue().toString();

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