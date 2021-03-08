package com.example.myonlineshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.myonlineshop.model.Products;
import com.example.myonlineshop.prevelant.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailsActivity extends AppCompatActivity {

    private FloatingActionButton addToCartBtn;
    private ImageView productImage;
    private TextView productName, productDescription, productPrice;
    private String productId = "";
    private ElegantNumberButton elegantNumberButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        addToCartBtn = findViewById(R.id.add_product_to_cart_btn);
        productImage = findViewById(R.id.detail_product_image);
        productName = findViewById(R.id.detail_product_name);
        productDescription = findViewById(R.id.detail_product_description);
        productPrice = findViewById(R.id.detail_product_price);
        elegantNumberButton = findViewById(R.id.add_mines_product);
        productId = getIntent().getStringExtra("pid");

        getProductDetails(productId);

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addingToCartList();
            }
        });

    }

    private void addingToCartList() {

        String saveCurrentlyDate ,saveCurrentlyTime;

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentlyDateFormat = new SimpleDateFormat("MM:dd:yy");
        saveCurrentlyDate = currentlyDateFormat.format(calendar.getTime());


        SimpleDateFormat currentlyTimeFormat = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentlyTime = currentlyTimeFormat.format(calendar.getTime());

        final DatabaseReference cartlistRef  = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String,Object> map = new HashMap<>();
        map.put("pid",productId);
        map.put("pname",productName);
        map.put("price",productPrice);
        map.put("date",saveCurrentlyDate);
        map.put("time ",saveCurrentlyTime);
        map.put("quantity",elegantNumberButton.getNumber());
        map.put("discount","");

        cartlistRef.child("User View").child(Prevalent.currentOnlineUser.getPhone()).child("Products")
                .child(productId).child(productId).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){


                    cartlistRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone()).child("Products")
                            .child(productId).child(productId).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(DetailsActivity.this, "Product added to cart", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(DetailsActivity.this,HomeActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        });


    }

    private void getProductDetails(String productId) {

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Products");

        databaseRef.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    Products products = snapshot.getValue(Products.class);
                    productName.setText(products.getPname());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());

                    Picasso.get().load(products.getImage()).into(productImage);
                }
            }




            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}