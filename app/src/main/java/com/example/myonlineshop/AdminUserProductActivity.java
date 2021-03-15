package com.example.myonlineshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myonlineshop.model.Cart;
import com.example.myonlineshop.prevelant.Prevalent;
import com.example.myonlineshop.viewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminUserProductActivity extends AppCompatActivity {

    private RecyclerView productsLists;
    private DatabaseReference cartListRef;
    private String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_product);

        productsLists = findViewById(R.id.products_list);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        productsLists.setHasFixedSize(true);
        productsLists.setLayoutManager(layoutManager);

        userId = getIntent().getStringExtra("uid");
        cartListRef = FirebaseDatabase.getInstance().getReference().
                child("Cart List").child("Admin View")
                .child(userId).child("Products");


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef, Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {


                holder.txtProductName.setText(model.getPname());
                holder.txtProductPrice.setText(" Price = " + model.getPrice() + "$");
                holder.txtProductQuantity.setText("Quantity = " + model.getQuantity());

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.recycler_item, parent, false);
                CartViewHolder cartViewHolder = new CartViewHolder(view);

                return cartViewHolder;
            }
        };

        productsLists.setAdapter(adapter);
        adapter.startListening();
    }


}