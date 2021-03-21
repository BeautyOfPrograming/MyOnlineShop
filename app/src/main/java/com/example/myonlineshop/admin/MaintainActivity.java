package com.example.myonlineshop.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myonlineshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class MaintainActivity extends AppCompatActivity {

    private Button applyChanges, deleteThisProduct;
    private EditText name, price, description;
    private String productId = "";
    private ImageView image;
    private DatabaseReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintain);


        productId = getIntent().getStringExtra("pid");

        productRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productId);

        name = findViewById(R.id.product_maintain_name);
        price = findViewById(R.id.product_maintain_price);
        description = findViewById(R.id.product_maintain_description);
        image = findViewById(R.id.product_maintain_image);
        deleteThisProduct = findViewById(R.id.product_delete_apply_btn);
        deleteThisProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                removeProduct();


            }
        });

        applyChanges = findViewById(R.id.product_maintain_apply);

        displayInfo();

        applyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                apply();
            }
        });

    }

    private void removeProduct() {


        productRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(MaintainActivity.this, "This product is deleted successfully", Toast.LENGTH_SHORT).show();


                startActivity(new Intent(MaintainActivity.this, AdminCategoryActivity.class));
                finish();

            }
        });

    }

    private void apply() {

        String pName = name.getText().toString();
        String pPrice = price.getText().toString();
        String pDescription = description.getText().toString();


        if (pName.equals("")) {

            Toast.makeText(this, "Write down Product Name.", Toast.LENGTH_SHORT).show();
        } else if (pPrice.equals("")) {

            Toast.makeText(this, "Write down Product Price.", Toast.LENGTH_SHORT).show();
        } else if (pDescription.equals("")) {

            Toast.makeText(this, "Write down Product Description.", Toast.LENGTH_SHORT).show();
        } else {


            HashMap<String, Object> productMap = new HashMap<>();

            productMap.put("pid", productId);

            productMap.put("description", pDescription);
            productMap.put("price", pPrice);
            productMap.put("pname", pName);

            productRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {


                    if (task.isSuccessful()) {


                        Toast.makeText(MaintainActivity.this, "Your changes are applied successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MaintainActivity.this, AdminCategoryActivity.class));
                        finish();
                    }


                }
            });


        }


    }

    private void displayInfo() {

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    String pName = snapshot.child("pname").getValue().toString();
                    String pPrice = snapshot.child("price").getValue().toString();
                    String pDescription = snapshot.child("description").getValue().toString();
                    String pImage = snapshot.child("image").getValue().toString();

                    name.setText(pName);
                    price.setText(pPrice);
                    description.setText(pDescription);

                    Picasso.get().load(pImage).into(image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}