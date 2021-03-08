package com.example.myonlineshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myonlineshop.model.Products;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailsActivity extends AppCompatActivity {

    private FloatingActionButton addToCartBtn;
    private ImageView productImage;
    private TextView productName, ProductDescription, productPrice;
    private String productId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

//        addToCartBtn = findViewById(R.id.add_product_to_cart_btn);
        productImage = findViewById(R.id.detail_product_image);
        productName = findViewById(R.id.detail_product_name);
        ProductDescription = findViewById(R.id.detail_product_description);
        productPrice = findViewById(R.id.product_item_price);
        productId = getIntent().getStringExtra("pid");

        getProductDetails(productId);

    }

    private void getProductDetails(String productId) {

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Products");

        databaseRef.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

//                    Products products = snapshot.ge
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}